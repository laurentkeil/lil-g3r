package com.mdl.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.LitigeDAO;

/**
 * Classe permettant d'insérer les réponses aux litiges
 * 
 * 
 * 
 */
public class ReponseLitigeClientForm {

    private LitigeDAO litigeDao;

    /**
     * Constructeur de ReponseLitigeClientForm
     * 
     * @param litigeDao
     * 
     */
    public ReponseLitigeClientForm( LitigeDAO litigeDao ) {
        this.litigeDao = litigeDao;
    }

    private String              resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    /**
     * Getter du résultat
     * 
     * @return un résultat
     * 
     */
    public String getResultat() {
        return resultat;
    }

    /**
     * Getter des erreurs
     * 
     * @return une map des erreurs
     * 
     */
    public Map<String, String> getErreurs() {
        return erreurs;
    }

    // Champs du formulaire à récupérer
    private static final String CHAMP_DESC   = "description";
    private static final String CHAMP_ID     = "idLitige";
    private static final String CHAMP_TYPE   = "type";
    private static final String CHAMP_STATUT = "statut";
    private static final String CHAMP_EMPL   = "idEmpl";

    /**
     * Méthode permettant d'insérer une réponse à un litige
     * 
     * @param request
     * 
     */
    public void repondreLitige( HttpServletRequest request ) {
        String description = getValeurChamp( request, CHAMP_DESC );
        String id = getValeurChamp( request, CHAMP_ID );
        String type = getValeurChamp( request, CHAMP_TYPE );
        String statut = getValeurChamp( request, CHAMP_STATUT );
        String idEmpl = getValeurChamp( request, CHAMP_EMPL );
        // Récupération des informations des champs du formulaire

        Litige litige = new Litige();

        try {
            litige.setDescription( description );
            validationDescription( description );

        } catch ( FormValidationException e ) {
            setErreur( CHAMP_DESC, e.getMessage() );
        }
        if ( erreurs.isEmpty() ) {
            litigeDao.insererReponse( description, statut, type, id, idEmpl );
            resultat = "Votre problème a bien été transmis à un de nos employés.";
        } else {
            resultat = "Echec de l'envoi du litige.";
        }

    }

    /**
     * Vérification de la description
     * 
     * @param description
     * @throws FormValidationException
     * 
     */
    private void validationDescription( String description ) throws FormValidationException {
        if ( description == null ) {
            throw new FormValidationException( "La description doit être mentionnée." );
        } else {
            description = description.replaceAll( "\r\n", "</br>" );
        }

    }

    /**
     * Ajoute un message correspondant au champ spécifié à la map erreurs
     * 
     * @param champ
     * @param message
     * 
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /**
     * Méthode utilitaire qui retourne NULL si un champ est vide, et son contenu
     * sinon.
     * 
     * @param request
     * @param nomChamp
     * @return la valeur
     * 
     */
    public static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }
}
