package com.mdl.forms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.DAOException;
import com.mdl.dao.LitigeDAO;
import com.mdl.dao.UtilisateurDAO;

/**
 * Classe permettant de recherches les problèmes signalés par des coursiers
 * 
 *
 */
public class RechercheLitigesCoursier {

    private LitigeDAO      litigeDao;

    /**
     * Constructeur de RechercheLitigesCoursier
     * @param litigeDao
     */
    public RechercheLitigesCoursier( LitigeDAO litigeDao ) {
        this.litigeDao = litigeDao;
    }

    /**
     * Méthode permettant de retrouver les problèmes signalés par les coursiers (les nouveaux et les traités)
     * @param request
     * @param nouveau
     * @return une liste d'objets des Litiges
     * 
     */
    public List<Litige> rechercheCoursier( HttpServletRequest request, String nouveau ) {
        List<Litige> litigeList = new ArrayList<Litige>();
        if ( nouveau.equals( "0" ) ) {
        	// Les nouveaux problèmes signalés
            try {
				litigeDao.unlockLitige();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String verrou = "0";
            litigeList = litigeDao.trouverLitigesCoursier( verrou );
        } else {
        	// Les problèmes traités et ceux qui ont été supprimés
            String verrou = "2";
            litigeList = litigeDao.trouverLitigesCoursier( verrou );
        }
        return litigeList;
    }

    private static final String CHAMP_LITIGE = "numLitigeCoursier";
    // Champ du formulaire à récupérer
    
    /**
     * Méthode permettant de créer un litige par rapport à un problème signalé par un coursier
     * @param request
     * @param idEmploye
     * @return un objet Litige
     * 
     */
    public Litige creerLitigeCoursier( HttpServletRequest request, int idEmploye ) {
        String id = getValeurChamp( request, CHAMP_LITIGE );
        // Récupération des informations du champ du formulaire
        String idEmpl = Integer.toString( idEmploye );
        Litige litige = new Litige();
        boolean traitement = litigeDao.traitementProbleme( id );
        if ( traitement ) {
        	// Dans le cas où le problème est en cours de traitement ou non.
            litige = null;
        } else {
            Litige problemeCoursier = litigeDao.trouverProbleme( id );
            String type = problemeCoursier.getObjet();
            String colis = problemeCoursier.getColis();
            String creation = problemeCoursier.getDate();
            String description = problemeCoursier.getDescriptionCoursier();
            litigeDao.priseEnCharge( id );
            Litige trouveLitige = litigeDao.trouverLitige( colis, type );
            litige.setId( trouveLitige.getId() );
            litige.setNumLitigeCoursier( id );
            litige.setDescriptionCoursier( description );
            litige.setObjet( type );
            litige.setDate( creation );
            litige.setColis( colis );
            Litige litigeClient = litigeDao.trouverClient( colis );
            litige.setICU(litigeClient.getICU());
            litige.setNom( litigeClient.getNom() );
            litige.setMail( litigeClient.getMail() );
            litige.setPrenom( litigeClient.getPrenom() );
            litige.setPrenomCoursier( problemeCoursier.getPrenomCoursier());
            litige.setNomCoursier(problemeCoursier.getNomCoursier());
            
        }
        return litige;
    }

    /**
     * Méthode permettant de fermer le problème signalé par un coursier
     * @param request
     * @param numLitigeCoursier
     * 
     */
    public void fermerProblCoursier( HttpServletRequest request, String numLitigeCoursier ) {

        litigeDao.fermerProbleme( numLitigeCoursier );
    }

	 /**
	  * Méthode utilitaire qui retourne NULL si un champ est vide, et son contenu sinon.
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
