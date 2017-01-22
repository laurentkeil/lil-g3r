package com.mdl.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.UtilisateurDAO;

/**
 * Classe permettant d'effectuer des recherches en fonction de certains
 * critères.
 * 
 * 
 * 
 */
public class RechercheClientForm {

    private UtilisateurDAO utilisateurDao;

    /**
     * Constructeur de RechercheClientForm
     * 
     * @param utilisateurDao
     * 
     */
    public RechercheClientForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
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

    private static final String CHAMP_RESEARCH = "research";
    private static final String CHAMP_RECH     = "rech";

    // Champs du formulaire à récupérer
    /**
     * Méthode permettant de rechercher des clients à partir de certains
     * critères
     * 
     * @param request
     * @return une liste d'objets Utilisateur
     */
    public List<Utilisateur> rechercheClient( HttpServletRequest request ) {
        String research = getValeurChamp( request, CHAMP_RESEARCH );
        String rech = getValeurChamp( request, CHAMP_RECH );
        // Récupération des informations des champs du formulaire
        List<Utilisateur> clientList = new ArrayList<Utilisateur>();

        try {
            validationRech( research, rech );
            clientList = utilisateurDao.trouverClientRech( rech, research );
            Iterator k = clientList.iterator(); // on crée un Iterator pour
            // parcourir notre HashSet
            Utilisateur client = new Utilisateur();
            while ( k.hasNext() ) {

                client = (Utilisateur) k.next();

                System.out.println( client.getId() );
            }
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_RECH, e.getMessage() );
        }
        if ( erreurs.isEmpty() ) {
            resultat = "Votre problème a bien été transmis à un de nos employés.";
        } else {
            resultat = "Echec de l'envoi du litige.";
        }
        return clientList;
    }

    /**
     * Vérification des informations introduites pour la recherche
     * 
     * @param research
     * @param rech
     * @throws FormValidationException
     * 
     */
    private void validationRech( String research, String rech ) throws FormValidationException {
        if ( research.equals( "defaut" ) ) {
            throw new FormValidationException( "Le choix de la recherche doit être mentionné." );
        } else if ( research.equals( "1" ) ) {
            // Si c'est par rapport à l'e-mail
            if ( rech != null ) {
                if ( !rech.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                    throw new FormValidationException( "Merci de saisir une adresse mail valide." );
                } else if ( utilisateurDao.trouver( rech ) == null ) {
                    throw new FormValidationException( "Cette adresse email n'existe pas." );
                }
            } else {
                throw new FormValidationException( "Merci de saisir une adresse mail." );
            }
        } else {
            if ( rech == null ) {
                throw new FormValidationException( "Merci de saisir une référence." );
            }
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
