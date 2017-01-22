package com.mdl.forms;
 
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.mdl.beans.Employe;
import com.mdl.dao.*;
 
/**
 * Classe "temporaire" permettant d'ajouter un employé pour la facilité des tests 
 * de l'application web sans devoir accéder directement à la base de données.
 * 
 *
 */
public final class InscriptionEmployeForm {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_CONF   = "confirmation";
    private static final String CHAMP_SEXE 	 = "sexe";
    private static final String CHAMP_STATUT = "statutMarital";
    private static final String CHAMP_NOM    = "nom";
    private static final String CHAMP_PRENOM = "prenom";
    private static final String CHAMP_TEL_FIXE    = "telephoneFixe";
    private static final String CHAMP_TEL_PORTABLE = "telephonePortable";
    private static final String CHAMP_ADRESSE_RUE  = "adresse_rue";
    private static final String CHAMP_ADRESSE_NUM  = "adresse_num";
    private static final String CHAMP_ADRESSE_BOITE = "adresse_boite";
    private static final String CHAMP_ADRESSE_LOC  = "adresse_loc";
    private static final String CHAMP_ADRESSE_CODE = "adresse_code";
    private static final String CHAMP_ADRESSE_PAYS = "adresse_pays";
    private static final String CHAMP_JOUR_NAISSANCE = "jour";
    private static final String CHAMP_MOIS_NAISSANCE    = "mois";
    private static final String CHAMP_ANNEE_NAISSANCE    = "annee";
    // Champs du formulaire à récupérer
    private EmployeDAO      employeDao;

    /**
     * Constructeur de InscriptionEmployeForm
     * @param employeDao
     * 
     */
    public InscriptionEmployeForm( EmployeDAO employeDao ) {
        this.employeDao = employeDao;
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
    
    /**
     * Méthode permettant d'ajouter un employé dans la base de données. Ceci a été implémenté
     * pour avoir plus de facilité pour les tests de l'application web
     * @param request
     * @return un objet Employe
     * 
     */
    public Employe inscrireEmploye( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String sexe = getValeurChamp( request, CHAMP_SEXE );
        String statutMarital = getValeurChamp( request, CHAMP_STATUT );
        String nom = getValeurChamp( request, CHAMP_NOM );
        String prenom = getValeurChamp(request, CHAMP_PRENOM);
        String adresse_rue = getValeurChamp(request, CHAMP_ADRESSE_RUE);
        String adresse_num = getValeurChamp(request, CHAMP_ADRESSE_NUM);
        String adresse_boite = getValeurChamp(request, CHAMP_ADRESSE_BOITE);
        String adresse_loc = getValeurChamp(request, CHAMP_ADRESSE_LOC);
        String adresse_code = getValeurChamp(request, CHAMP_ADRESSE_CODE);
        String adresse_pays = getValeurChamp(request, CHAMP_ADRESSE_PAYS);
        String telephoneFixe = getValeurChamp(request, CHAMP_TEL_FIXE);
        String telephonePortable = getValeurChamp(request, CHAMP_TEL_PORTABLE);
        String mois = getValeurChamp(request, CHAMP_MOIS_NAISSANCE);
        String jour = getValeurChamp(request, CHAMP_JOUR_NAISSANCE);
        String annee = getValeurChamp(request, CHAMP_ANNEE_NAISSANCE);
        // Récupération des informations des champs du formulaire
       
        Employe employe = new Employe();
        try{
        	 
	        try {
	            validationEmail( email );
	            employe.setEmail( email );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_EMAIL, e.getMessage() );
	        }
	        
	   
	        traiterMotsDePasse( motDePasse, confirmation, employe );
	        
	        employe.setSexe(sexe);
	        employe.setStatutMarital(statutMarital);
	        
	        try {
	            validationNom( nom );
	            employe.setNom( nom );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_NOM, e.getMessage() );
	        }
	        
	        
	        try {
	            validationPrenom( prenom );
	            employe.setPrenom( prenom );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_PRENOM, e.getMessage() );
	        }
	        
	        
	        try {
	            validationRue( adresse_rue );
	            employe.setAdresse_rue( adresse_rue );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ADRESSE_RUE, e.getMessage() );
	        }
	        
	        try {
	            validationNum( adresse_num );
	           
	            employe.setAdresse_num( adresse_num );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ADRESSE_NUM, e.getMessage() );
	        }
	        
	        employe.setAdresse_boite( adresse_boite );
	        
	        try {
	            validationLoc( adresse_loc );
	            employe.setAdresse_loc( adresse_loc );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ADRESSE_LOC, e.getMessage() );
	        }
	        
	        
	        try {
	            validationCode( adresse_code );
	            employe.setAdresse_code( adresse_code );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ADRESSE_CODE, e.getMessage() );
	        }
	        
	        try {
	            validationPays( adresse_pays );
	            employe.setPays( adresse_pays );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ADRESSE_PAYS, e.getMessage() );
	        }
	        
	        try {
	            validationTelephoneFixe( telephoneFixe );
	            employe.setTelephoneFixe( telephoneFixe );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_TEL_FIXE, e.getMessage() );
	        }
	        
	        try {
	            validationTelephonePortable( telephonePortable );
	            employe.setTelephonePortable( telephonePortable );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_TEL_PORTABLE, e.getMessage() );
	        }
	        
	        
	        try {
	            validationNaissance( jour, mois, annee );
	            // Mise en place d'un GregorianCalendar
	            employe.setJourNaissance( jour );
	            employe.setMoisNaissance( mois );
	            employe.setAnneeNaissance( annee );
	            int jourNaissance = Integer.parseInt(jour);
	        	int moisNaissance = Integer.parseInt(mois);
	        	int anneeNaissance = Integer.parseInt(annee);
	        	Calendar naissance = new GregorianCalendar(anneeNaissance, (moisNaissance-1), jourNaissance);
	        	java.sql.Date date = new java.sql.Date(naissance.getTimeInMillis());
	        	// Conversion du GregorianCalendar en Date
	        	employe.setDate_naissance( date );
	        } catch ( FormValidationException e ) {
	            setErreur( CHAMP_ANNEE_NAISSANCE, e.getMessage() );
	        }
	        
	     
	        if ( erreurs.isEmpty() ) {
	        	employeDao.creer( employe );
	            resultat = "Succès de l'inscription. Un e-mail de validation vous a été envoyé sur votre adresse e-mail";
	        } else {
	            resultat = "échec de l'inscription.";
	        }
        } catch ( DAOException e ) {
            resultat = "échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }
     
        return employe;
    }
    
    /**
     * Traitement du mot de passes, notamment pour le chiffrement de celui-ci
     * @param motDePasse
     * @param confirmation
     * @param employe
     * 
     */
    private void traiterMotsDePasse( String motDePasse, String confirmation, Employe employe ) {
        try {
            validationMotsDePasse( motDePasse, confirmation );
	        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	        String myEncryptionPassword = "HLB-Express";
	        textEncryptor.setPassword(myEncryptionPassword);
	        String myEncryptedPassword = textEncryptor.encrypt(motDePasse);
	        // Chiffrement du mot de passe à l'aide d'une clé
	        employe.setMotDePasse( myEncryptedPassword );
        
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
        }
    }
    
    /**
     * Validation du champ de l'email
     * @param email
     * @throws FormValidationException
     * 
     */
    private void validationEmail( String email ) throws FormValidationException {
    	if ( email != null ) {
    		// Vérification de l'e-mail grâce à ce regex
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( employeDao.trouver( email ) != null ) {
                throw new FormValidationException( "Cette adresse email est déjà utilisée, merci d'en choisir une autre." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }
     
    /**
     * Vérification du mot de passe et de sa confirmation
     * @param motDePasse
     * @param confirmation
     * @throws FormValidationException
     * 
     */
    private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new FormValidationException( "Les mots de passe entrés sont différents, merci de les saisir à nouveau." );
            } else if ( motDePasse.length() < 3 ) {
            	// Nombre de caractères min de 3
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }
     /**
      * Validation du nom
      * @param nom
      * @throws FormValidationException
      * 
      */
    private void validationNom( String nom ) throws FormValidationException {
        if ( nom == null ) {
            throw new FormValidationException( "Le nom d'employe doit être mentionné." );
        }
    }
    
    /**
     * Validation du prénom
     * @param prenom
     * @throws FormValidationException
     * 
     */
    private void validationPrenom( String prenom ) throws FormValidationException {
        if ( prenom == null ) {
            throw new FormValidationException( "Le prénom d'employe doit être mentionné." );
        }
    }
    
    /**
     * Validation de la rue
     * @param adresse_rue
     * @throws FormValidationException
     * 
     */
    private void validationRue( String adresse_rue ) throws FormValidationException {
        if (adresse_rue == null){
        	throw new FormValidationException( "La rue doit être mentionnée.");
        }
    }
    
    /**
     * Validation du numéro de la maison
     * @param adresse_num
     * @throws FormValidationException
     * 
     */
    private void validationNum( String adresse_num ) throws FormValidationException {
        if (adresse_num == null){
        	throw new FormValidationException( "Le numéro doit être mentionnée.");
        }
        if ( !adresse_num.matches( "^\\d+$" ) ) {
            throw new FormValidationException( "Le numéro de téléphone doit uniquement contenir des chiffres." );
        }
    }
    
    /**
     * Validation de la localité
     * @param adresse_loc
     * @throws FormValidationException
     * 
     */
    private void validationLoc( String adresse_loc ) throws FormValidationException {
        if (adresse_loc == null){
        	throw new FormValidationException( "La localité doit être mentionnée.");
        }
    }
    
    /**
     * Validation du pays
     * @param adresse_pays
     * @throws FormValidationException
     * 
     */
    private void validationPays( String adresse_pays ) throws FormValidationException {
        if (adresse_pays == null){
        	throw new FormValidationException( "Le pays doit être mentionné.");
        }
    }
    
    /**
     * Validation du code postal
     * @param adresse_code
     * @throws FormValidationException
     * 
     */
    private void validationCode( String adresse_code ) throws FormValidationException {
        if (adresse_code != null){
        	if ( !adresse_code.matches( "^\\d+$" ) ) {
        		throw new FormValidationException( "Le code postal doit uniquement contenir des chiffres." );
            } 
        }else{
        	throw new FormValidationException( "Le code postal doit être mentionné.");
        }
    }
    
    /**
     * Validation du téléphone fixe
     * @param telephone
     * @throws FormValidationException
     * 
     */
    private void validationTelephoneFixe( String telephone ) throws FormValidationException {
        if ( telephone != null ) {
            if ( !telephone.matches( "^\\d+$" ) ) {
                throw new FormValidationException( "Le numéro de téléphone doit uniquement contenir des chiffres." );
            } else if ( telephone.length() < 8 ) {
                throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
            }
        } else {
            throw new FormValidationException( "Le numéro de téléphone doit être mentionné." );
        }
    }
    
    /**
     * Validation du téléphone portable
     * @param telephone
     * @throws FormValidationException
     * 
     */
    private void validationTelephonePortable( String telephone ) throws FormValidationException {
    	if ( telephone != null ) {
            if ( !telephone.matches( "^\\d+$" ) ) {
                throw new FormValidationException( "Le numéro de téléphone doit uniquement contenir des chiffres." );
            } else if ( telephone.length() < 8 ) {
                throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
            }
        } else {
            throw new FormValidationException( "Le numéro de téléphone doit être mentionné." );
        }
    }
    
    /**
     * Validation de la date de naissance
     * @param jourNaissance
     * @param moisNaissance
     * @param anneeNaissance
     * @throws FormValidationException
     * 
     */
    private void validationNaissance (String jourNaissance, String moisNaissance, String anneeNaissance) throws FormValidationException {
    	if ((jourNaissance != null) && (moisNaissance != null) && (anneeNaissance != null)){
    		if ( (!jourNaissance.matches( "^\\d+$" )) || (!moisNaissance.matches( "^\\d+$" )) || (!anneeNaissance.matches( "^\\d+$" ))  ) {
    			throw new FormValidationException( "La date de naissance doit uniquement contenir des chiffres." );
    		} else {
	    		int newJourNaissance = Integer.parseInt(jourNaissance);
	        	int newMoisNaissance = Integer.parseInt(moisNaissance);
	        	int newAnneeNaissance = Integer.parseInt(anneeNaissance);
	        	Calendar naissance = new GregorianCalendar(newAnneeNaissance, newMoisNaissance, newJourNaissance);
	        	Calendar maintenant = new GregorianCalendar();
	        	int age = maintenant.get(Calendar.YEAR) - naissance.get(Calendar.YEAR);
	            if (((naissance.get(Calendar.MONTH)-1) > maintenant.get(Calendar.MONTH)) || (((naissance.get(Calendar.MONTH)-1) == maintenant.get(Calendar.MONTH)) && (naissance.get(Calendar.DAY_OF_MONTH) > maintenant.get(Calendar.DAY_OF_MONTH)))){
	              age--;
	            }
	    		if (age < 12){
	    			// Age minimum de 12 ans
	    			throw new FormValidationException("L'âge minimum pour s'incrire est de 12 ans.");
	    		}
	    		if ((0 < newJourNaissance) && (newJourNaissance < 32) && (0 < newMoisNaissance) && (newMoisNaissance < 13) ){
	    			if (newMoisNaissance == 2){
	    				if (((newJourNaissance > 29) && (((GregorianCalendar) naissance).isLeapYear(newAnneeNaissance))) || 
	    						((newJourNaissance > 28) && !(((GregorianCalendar) naissance).isLeapYear(newAnneeNaissance)))){ 
	    					throw new FormValidationException("La date de naissance n'est pas valide.");
	    				} 
	    			}
	    			if ((newJourNaissance > 30) && ((newMoisNaissance == 3) || (newMoisNaissance == 5) || (newMoisNaissance == 9) || (newMoisNaissance == 11))){
	    				throw new FormValidationException("La date de naissance n'est pas valide.");
	    			}
	    		}else{
	    			throw new FormValidationException("La date de naissance n'est pas valide.");
	    		}
    		}
    	}else{
    		
    		throw new FormValidationException("La date de naissance doit être mentionnée.");
    	}
    }
     
    /**
     * Ajoute un message correspondant au champ spécifié à la map des erreurs
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
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }
    
    

}