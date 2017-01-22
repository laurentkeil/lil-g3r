package com.mdl.forms;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mdl.beans.Tracking;
import com.mdl.dao.TrackingDAO;

/**
 * Vérification et traitement des données pour le tracking d'un colis
 * 
 *
 */
public final class EspacePublicForm {
	// champ du formulaire à récupérer
    private static final String CHAMP_REFERENCE = "reference";
    private static final String CHAMP_MAIL = "mail";
    private String              resultat;
    private Map<String, String> erreurs         = new HashMap<String, String>();
    private TrackingDAO         trackingDao;

    /**
     * Constructeur de EspacePublicForm
     * @param trackingDao
     * 
     */
    public EspacePublicForm( TrackingDAO trackingDao ) {
        this.trackingDao = trackingDao;
    }

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

    /**
     * Vérification et traitement des données pour le tracking d'un colis
     * @param request
     * @return une liste d'objets Tracking
     * 
     */
    public List<Tracking> trackingColis( HttpServletRequest request ) {
        //Récupération des champs du formulaire 
        String icuColis = getValeurChamp( request, CHAMP_REFERENCE );
        String mail = getValeurChamp( request, CHAMP_MAIL );
        Tracking tracking = new Tracking();
        List<Tracking> trackingListOne = new ArrayList<Tracking>();
        List<Tracking> trackingListTwo = new ArrayList<Tracking>();
        List<Tracking> trackingList = new ArrayList<Tracking>();
        /* Validation du champ email. */
        try {
            validationReference( icuColis );
        } catch ( Exception e ) {
            setErreur( CHAMP_REFERENCE, e.getMessage() );
        }
        try{

        	/* Permet d'ajouter une vérification en plus si la sécurité n'est pas assez forte
        	validationEmail( mail );
        	boolean same = trackingDao.emailIdentique(mail, icuColis);*/
        	boolean same = true;
        	//Vérifie si l'e-mail référencé est le même que celui du responsable du colis
        	
            if (same){
            	/*
            	 * Pour le tracking, nous avons deux listes, une contenant des données issues de la
            	 * table "operation_colis" et une autre contenant les données issues de la table 
            	 * "operation_chariot". Afin d'avoir les informations dans l'ordre chronologique,
            	 * nous concaténons les deux listes en fonction des dates des opérations.
            	 */
             	trackingListOne = trackingDao.trackingColis( icuColis );
                trackingListTwo = trackingDao.trackingChariot( icuColis );
                Tracking trackingColis = new Tracking();
                Tracking trackingChariot = new Tracking();
                /*
                 *  la variable "fin" veut dire qu'un élément a été trouvé et que ça ne sert
                 *  à rien de continuer d'explorer les autres
                 */
                boolean fin = false;
                /*
                 * La variable "vide" signifie que la liste est vide
                 */
                boolean vide = false;
                // chariotVide signifie qu'il n'y a plus d'opération chariot dans la liste
            	boolean chariotVide = true;
            	/*
            	 *  fait signifie qu'une certaine opération a été faite et ça ne sert plus à rien de
            	 *  la considérer (ça permet d'éviter les duplications)
            	 */
            	boolean fait = false;
            	// Permet d'éviter les duplications ou les éléments non affichés
                int y = 0;
                if (trackingListOne.size() >= trackingListTwo.size()){
                	// Vérification de la taille des listes pour l'ordre des boucles imbriquées
                    Iterator i = trackingListOne.iterator(); // on crée un Iterator pour
                     while ( i.hasNext() ) {
                    	 // Boucle sur les opérations colis
                        trackingColis = (Tracking) i.next();
                        Iterator j = trackingListTwo.iterator();
                        vide = true;
                        if (y == 1 || j.hasNext())
                        	// Permet d'avoir les éléments du chariot si celui-ci est la dernière donnée à considérer
                        	fin = false;
                        while ( j.hasNext() && !fin ) {
                        	// Boucle sur les opérations chariots
                        	chariotVide = false;
                          	vide = false;
                           	fin = false;
                           	y++;
                            trackingChariot = (Tracking) j.next();
                            try {
                                if (trackingColis.getDate().compareTo(trackingChariot.getDate()) > 0){
                                 	// traitement du cas date colis est après date chariot
                                     trackingList.add( trackingChariot );
                                     if (!j.hasNext() ){
                                    	// cas où il n'y a plus d'opération chariot
                                      	chariotVide = true;
                                    	fin = true;
                                    	fait = true;
                                     }
                                     // On l'enlève pour éviter de le prendre en compte une seconde fois
                                     trackingListTwo.remove(trackingChariot);
                                     // On redéfinit j
                                     j = trackingListTwo.iterator();
                                 } else {
                                     // sinon la date colis est après la date chariot
                                 	trackingList.add( trackingColis );
                                 	fin = true;
                                 }
                        	} catch (Exception e) {
                        		e.printStackTrace();
                        	}
                            if (!j.hasNext()  && y != 1){
                            	// Signifie qu'il n'y a plus d'opérations chariot
                            	fin = true;
                            }

                            }
                        if ((vide && chariotVide) || (fait) ){
                        	// Cas où il reste des opérations colis mais qu'il n'y a plus de chariot
                          	trackingList.add( trackingColis );
                        }
                        if (!i.hasNext() && y != 1){
                        	vide = true;
                        }
                   }
                   if (!chariotVide){
                	   // Cas où il reste encore des chariots alors qu'il n'y a plus d'opérations colis
                	   Iterator j = trackingListTwo.iterator();
                	   while ( j.hasNext() ) {
                		   trackingChariot = (Tracking) j.next();
                		   trackingList.add( trackingChariot );
                	   }
                   }
                   if (vide && fin && chariotVide && !fait)
                	   trackingList.add( trackingColis );
                   // Cas où il reste une opération colis à prendre en compte
                }else{
                	// Cas où il y a plus d'opération chariot que d'opérations colis
                	Iterator i = trackingListTwo.iterator(); // on crée un Iterator pour
                	boolean colisVide = false;
                    while ( i.hasNext() ) {
                    	// On boucle sur les opérations chariot
                    	trackingChariot = (Tracking) i.next();
                        Iterator j = trackingListOne.iterator();
                        vide = true;
                        fait = false;
                        if (y == 1 || !colisVide)
                        	fin = false;
                        // Permet d'avoir les éléments du chariot si celui-ci est la dernière donnée à considérer
                        while ( j.hasNext() && !fin ) {
                        	// Boucle sur les opérations colis
                        	vide = false;
                            fin = false;
                            y++;
                            trackingColis = (Tracking) j.next();
                            try {
                            	if (trackingColis.getDate().compareTo(trackingChariot.getDate()) > 0){
                            		// traitement du cas date colis est après date chariot
                                    trackingList.add( trackingChariot );
                                    fin = true;
                                    fait = true;
                                } else {
                                	// traitement du cas date chariot est après date colis
                                    trackingList.add( trackingColis );
                                    if (y == 1 && !j.hasNext() )
                                    	colisVide = true;
                                    // Cas où il n'y a plus de colis dans la liste
                                    if (!j.hasNext()){
                                    	vide = true;
                                    }
                                    trackingListOne.remove(trackingColis);
                                    j = trackingListOne.iterator();
                                }
                        	} catch (Exception e) {
                        		e.printStackTrace();
                        	}
                            if (!j.hasNext()  && y != 1){
                            	vide = true;
                            }
                        }
                        if ((vide || colisVide) && !fait ){
                          	trackingList.add( trackingChariot );
                        }

                        if (!i.hasNext() && !colisVide){
                        	j = trackingListOne.iterator();
                        	while ( j.hasNext() ) {
                        		trackingColis = (Tracking) j.next();
                            	trackingList.add( trackingColis );
                        	}
                        	
                        	
                        }  
                    }
                    
                }
            }
        } catch ( Exception e ) {
            setErreur( CHAMP_MAIL, e.getMessage() );
        }
        if ( erreurs.isEmpty() ) {
            resultat = "Succés de la consultation.";
        } else {
            resultat = "Echec de la consultation.";
        }

        return trackingList;
    }

    /**
     * Validation de l'ICU saisi
     * @param reference
     * @throws Exception
     * 
     */
    private void validationReference( String reference ) throws Exception {
        if ( reference != null ) {
            if ( reference.length() < 0 ) {
                throw new Exception( "La référence doit contenir au moins 8 caractères." );
            }
            if ( !trackingDao.existeColis( reference ) ) {
                throw new Exception( "Cette référence n'a pas été trouvée, merci de la vérifier." );
            }
        } else {
            throw new Exception( "La référence doit être fournie." );
        }
    }
    
    /**
     * Vérification de l'adresse e-mail
     * @param email
     * @throws FormValidationException
     * 
     */
    private void validationEmail( String email ) throws FormValidationException {
    	if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) )
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }

    /**
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     * @param champ
     * @param message
     * 
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
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
            return valeur;
        }
    }
}