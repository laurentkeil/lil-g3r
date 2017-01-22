package com.mdl.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.*;

/**
 * Classe permettant de rechercher des surfacturations
 * 
 *
 */
public class RechercheSurfacturationForm {
	 private SurfacturationDAO surfacturationDao;

	 /**
	  * Constructeur de RechercheSurfacturationForm
	  * @param surfacturationDao
	  */
	 public RechercheSurfacturationForm( SurfacturationDAO surfacturationDao ) {
	        this.surfacturationDao = surfacturationDao;
	    }
	 
	 
	 // champs du formulaire à récupérer
	 private static final String CHAMP_RESEARCH  = "research";
	 private static final String CHAMP_RECH  = "rech";
	 private static final String CHAMP_SURFACT  = "surfact";
	 
	 /**
	  * Méthode permettant de retrouver les surfacturations (tous, payées et impayées)
	  * @param request
	  * @return une liste de surfacturation
	  * 
	  */
	 public List<Surfacturation> rechercheSurfact( HttpServletRequest request) {
	     String research = getValeurChamp( request, CHAMP_RESEARCH );
	     String rech = getValeurChamp( request, CHAMP_RECH );
	     String surfact = getValeurChamp( request, CHAMP_SURFACT );
		 
	     List<Surfacturation> surfactList = new ArrayList<Surfacturation>();
	     surfactList = surfacturationDao.trouverSurfactRech(rech, research, surfact);
	    	 
	     return surfactList;
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
