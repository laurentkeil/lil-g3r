package com.mdl.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.LitigeDAO;

/**
 * Classe permettant de les vérifications pour la création d'un litige
 * à partir d'un problème signalé par les coursiers et de créer ce litige.
 * 
 *
 */
public class CreationLitigeCoursierForm {

	
	
	 private LitigeDAO      litigeDao;

	 /**
	  * Constructeur de CreationLitigeCoursierForm
	  * @param litigeDao
	  * 
	  */
	 public CreationLitigeCoursierForm( LitigeDAO litigeDao ) {
	        this.litigeDao = litigeDao;
	    }
	 
	private static final String CHAMP_OBJET  = "objet";
	private static final String CHAMP_TYPE   = "type";
	private static final String CHAMP_DESC  = "description";
	private static final String	CHAMP_STATUT = "statut";
	private static final String	CHAMP_IDEMPL	= "idEmpl";
	private static final String	CHAMP_COLIS	= "colis";
	private static final String	CHAMP_ICU	= "icu";
	private static final String	CHAMP_LITIGE	= "numLitigeCoursier";
	private static final String	CHAMP_MAIL	= "mail";
	private static final String	CHAMP_ID	= "idLitige";
	// Champs du formulaire à récupérer
	/**
	 * Création du litige à partir d'un problème signalé par un coursier
	 * @param request
	 * @return un objet Litige
	 * 
	 */
	public Litige creerLitige( HttpServletRequest request) {
	     String objet = getValeurChamp( request, CHAMP_OBJET);
	     String type = getValeurChamp( request, CHAMP_TYPE);
	     String statut = getValeurChamp( request, CHAMP_STATUT);
	     String description = getValeurChamp( request, CHAMP_DESC );
	     String idEmpl = getValeurChamp(request, CHAMP_IDEMPL);
	     String colis = getValeurChamp(request, CHAMP_COLIS);
	     String icu = getValeurChamp(request, CHAMP_ICU);
	     String numLitigeCoursier = getValeurChamp(request, CHAMP_LITIGE);
	     String mail = getValeurChamp(request, CHAMP_MAIL);
	     String id = getValeurChamp(request, CHAMP_ID);
	     // Récupération des champs du formulaire 
		 
	     Litige litige = new Litige();
	     if (id != null){
	    	 // Dans le cas où un litige existe déjà, une simple réponse est ajoutée
	    	 litigeDao.insererReponse(description, "3", "3", id, idEmpl);
	    	 litigeDao.refLitige(id, numLitigeCoursier);
	     }else {
	    	 // Sinon on crée le litige
	    	 litige.setColis(colis);
	    	 litige.setICU(icu);
		     litige.setObjet( objet );
			 litige.setDescription( description );
		     litige.setType(type);
		     litige.setStatut(statut);
		     litige.setMail(mail);
		     litige.setNumLitigeCoursier(numLitigeCoursier);
		     litigeDao.creerLitigeCoursier( litige, idEmpl);
	     }
	     litigeDao.lockLitigeCoursier(numLitigeCoursier);
	     return litige;
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
