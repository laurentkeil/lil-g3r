package com.mdl.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Commande;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.*;
//import com.mdl.dao.LitigeDAO;

public class RechercheCommandeForm {

	
	 private CommandeDAO commandeDao;

	 public RechercheCommandeForm( CommandeDAO commandeDao ) {
	        this.commandeDao = commandeDao;
	    }
	 
	 private String resultat;
	 private Map<String, String> erreurs      = new HashMap<String, String>();
	     
	 public String getResultat() {
		 return resultat;
	 }
	     
	 public Map<String, String> getErreurs() {
		 return erreurs;
	 }
	 
	 private static final String CHAMP_RESEARCH  = "research";
	 private static final String CHAMP_RECH      = "rech";
	 private static final String CHAMP_MAIL      = "mail";
	 
	 public List<Commande> rechercheCommande( HttpServletRequest request) {
	     String research = getValeurChamp( request, CHAMP_RESEARCH );
	     String rech = getValeurChamp( request, CHAMP_RECH );
	     String mail = getValeurChamp(request,CHAMP_MAIL);
		 
	     List<Commande> commandeList = new ArrayList<Commande>();
	     	
	     commandeList = commandeDao.trouverCommandeRech(rech, research, mail);
	    	 
		
	     return commandeList;
	 }
	 
	 
	
	 
	 private void setErreur( String champ, String message ) {
	        erreurs.put( champ, message );
	    }
	 
	 public static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
	        String valeur = request.getParameter( nomChamp );
	        if ( valeur == null || valeur.trim().length() == 0 ) {
	            return null;
	        } else {
	            return valeur.trim();
	        }
	    }
}
