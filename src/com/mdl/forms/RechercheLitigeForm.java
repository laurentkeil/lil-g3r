package com.mdl.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.*;
//import com.mdl.dao.LitigeDAO;

/**
 * Classe permettant la recherche de litiges en fonction de certains critères
 * 
 *
 */
public class RechercheLitigeForm {

	 private LitigeDAO      litigeDao;
	 /**
	  * Constructeur de RechercheLitigeForm
	  * @param litigeDao
	  * 
	  */
	 public RechercheLitigeForm( LitigeDAO litigeDao ) {
	        this.litigeDao = litigeDao;
	    }
	 
	 private String resultat;
	 private Map<String, String> erreurs      = new HashMap<String, String>();
	     
	 
	 private static final String CHAMP_RESEARCH  = "research";
	 private static final String CHAMP_STATUT  = "statut";
	 private static final String CHAMP_RECH  = "rech";
	 // Champs du formulaire à récupérer
	 
	 /**
	  * Méthode permettant de rechercher des litiges en fonction de certains critères
	  * @param request
	  * @param mail
	  * @return une liste d'objets Litige
	  */
	 public List<Litige> rechercheLitige( HttpServletRequest request, String mail) {
	     String research = getValeurChamp( request, CHAMP_RESEARCH );
	     String statut = getValeurChamp( request, CHAMP_STATUT );
	     String rech = getValeurChamp( request, CHAMP_RECH );
		 // Récupération des informations des champs du formulaire
	     List<Litige> litigeList = new ArrayList<Litige>();
	     litigeList = litigeDao.trouverLitigesRech(statut, mail, rech, research);

	     return litigeList;
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
