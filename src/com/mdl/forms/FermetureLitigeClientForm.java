package com.mdl.forms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Litige;
import com.mdl.dao.DAOException;
import com.mdl.dao.LitigeDAO;

/**
 * Classe permettant de fermer le litige d'un client
 * 
 *
 */
public class FermetureLitigeClientForm {

	
	
	 private LitigeDAO      litigeDao;

	 /**
	  * Constructeur de FermetureLitigeClientForm
	  * @param litigeDao
	  * 
	  */
	 public FermetureLitigeClientForm( LitigeDAO litigeDao ) {
	        this.litigeDao = litigeDao;
	    }
	 
	 private String resultat;
	 private Map<String, String> erreurs      = new HashMap<String, String>();
	     
	 /**
	  * Getter du résultat
	  * @return un résultat
	  * 
	  */
	 public String getResultat() {
		 return resultat;
	 }
	  
	 /**
	  * Getter des erreurs
	  * @return une map des erreurs
	  * 
	  */
	 public Map<String, String> getErreurs() {
		 return erreurs;
	 }
	 
	 // Champs du formulaire à récupérer
	 private static final String CHAMP_FERMETURE  = "fermeture";
	 private static final String CHAMP_ID  = "idLitige";
	 private static final String CHAMP_TYPE  = "type";
	 private static final String CHAMP_STATUT  = "statut";
	 private static final String CHAMP_EMPL  = "idEmpl";
	 private static final String CHAMP_RESPON  = "respon";
	 private static final String CHAMP_MAIL    = "mail";
	 
	 /**
	  * Fermeture du litige d'un client renvoyant un booléen pour permettant de bloquer un compte
	  * si le client a plus de 3 litiges le tenant responsable
	  * @param request
	  * @return un booléen
	  * 
	  */
	 public boolean fermerLitige( HttpServletRequest request) {
	     String fermeture = getValeurChamp( request, CHAMP_FERMETURE );
	     String responsabilite = getValeurChamp( request, CHAMP_RESPON );
	     String id = getValeurChamp( request, CHAMP_ID );
	     String type = getValeurChamp( request, CHAMP_TYPE );
	     String statut = getValeurChamp( request, CHAMP_STATUT );
	     String idEmpl = getValeurChamp( request, CHAMP_EMPL );
	     String mail = getValeurChamp( request, CHAMP_MAIL );
	     // Récupération des informations des champs du formulaire
	     if(responsabilite.equals("oui")){
	    	 // Responsabilité du client dans le litige
	    	 responsabilite = "1";
	     }else{
	    	 responsabilite = "0";
	     }
	     
		 String note = "Votre litige a été fermé car votre problème a été réglé. Merci de votre confiance envers HLB-Express.";
	     litigeDao.fermerLitige(note, statut, type, id, idEmpl);
	     litigeDao.responsabiliteLitige(id,responsabilite);
	     boolean mauvais = false;
	     try {
			mauvais = litigeDao.litigeResponsable(mail);
	     } catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
	     
	     return mauvais;
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
