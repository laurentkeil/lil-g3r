package com.mdl.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.LitigeDAO;

/**
 * Classe permettant la création d'un litige par un client
 * 
 * 
 * 
 */
public class CreationLitigeClientForm {
    private LitigeDAO litigeDao;

    /**
     * Constructeur de CreationLitigeClientForm
     * 
     * @param litigeDao
     * 
     */
    public CreationLitigeClientForm( LitigeDAO litigeDao ) {
        this.litigeDao = litigeDao;
    }

    private String              resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    /**
     * Getter du résultat
     * 
     * @return le résultat
     * 
     */
    public String getResultat() {
        return resultat;
    }

    /**
     * Getter des erreurs
     * 
     * @return une map des erreurs
     */
    public Map<String, String> getErreurs() {
        return erreurs;
    }

    // Champs du formulaire à récupérer
    private static final String CHAMP_OBJET = "objet";
    private static final String CHAMP_ID    = "colis";
    private static final String CHAMP_DESC  = "description";

    /**
     * Vérification des champs pour la création d'un litige
     * 
     * @param request
     * @return un objet Litige
     * 
     */
    public Litige creerLitige( HttpServletRequest request ) {
        String objet = request.getParameter( CHAMP_OBJET );
        String id_colis = getValeurChamp( request, CHAMP_ID );
        String description = getValeurChamp( request, CHAMP_DESC );
        // Récupération des champs du formulaire
        Litige litige = new Litige();
        litige.setColis( id_colis );
        try {
            // Validation de l'objet du litige
            validationObjet( objet, id_colis );
            litige.setObjet( objet );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_OBJET, e.getMessage() );
        }

        try {
            // Validation de la description
            litige.setDescription( description );
            validationDescription( description );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_DESC, e.getMessage() );
        }
        if ( erreurs.isEmpty() ) {
            litige.setType( "2" );
            // Type = 2 car c'est un message du client
            litigeDao.creer( litige );
            resultat = "Votre problème a bien été transmis à un de nos employés.";
        } else {
            resultat = "Echec de l'envoi du litige.";
        }
        return litige;
    }

    /**
     * Vérification du champ objet du litige
     * 
     * @param objet
     * @param id_colis
     * @throws FormValidationException
     * 
     */
    private void validationObjet( String objet, String id_colis ) throws FormValidationException {
        if ( objet.equals( "defaut" ) ) {
            throw new FormValidationException( "L'objet du problème doit être mentionné." );
        } else {
            Boolean trouve = litigeDao.existeLitige( objet, id_colis );
            if ( trouve ) {
                throw new FormValidationException( "Un dossier est déjà ouvert pour un problème de ce type." );
            }
        }
    }

    /**
     * Vérification du champ description du litige
     * 
     * @param description
     * @throws FormValidationException
     * 
     */
    private void validationDescription( String description ) throws FormValidationException {
        if ( description == null ) {
            throw new FormValidationException( "La description doit être mentionnée." );
        } else if ( ( description.indexOf( ">" ) != -1 ) || ( description.indexOf( "<" ) != -1 )
                || ( description.indexOf( "&" ) != -1 ) ) {
            // Le champ description ne peut avoir ces caractères pour éviter
            // l'injection de code html
            throw new FormValidationException( " Les caractères \">\", \"<\" et \"&\" sont interdits ! " );
        }

    }

    /**
     * Setter des erreurs
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
