package com.mdl.forms;
 
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.mail.Email;
 
/**
 * Classe permettant de traiter les données utiles pour envoyer un lien et pour réinitialiser le mot de passe
 * 
 *
 */
public final class MdpPerduClientForm {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    private static final String CHAMP_CONF   = "confirmation";
    
    private static final String FICHIER_PROPERTIES     = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_MDP_REINIT    = "mdp_reinit_max";
 
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
 
    private UtilisateurDAO      utilisateurDao;

    /**
     * Constructeur de MdpPerduClientForm
     * @param utilisateurDao
     * 
     */
    public MdpPerduClientForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
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
     * Méthode permettant de vérifier l'adresse e-mail et l'envoi d'un mail si celui-ci est correct
     * @param request
     * 
     */
    public void demandeMotDePasse( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
      
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
       
        if (erreurs.isEmpty() ) {
        		String emailChiffre = chiffrementEmail(email);
        		String dateChiffree = chiffrementDate();
        		try {
					Email.envoiResetMdp(email, emailChiffre, dateChiffree);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        } else {
            resultat = "Echec de la réinitialisation.";
        }
    }
    
    public void resetMotDePasse( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );

        Utilisateur utilisateur =  utilisateurDao.trouver( email );
        traiterMotsDePasse( motDePasse, confirmation, utilisateur );
       
        if (erreurs.isEmpty() ) {
        	utilisateurDao.modifier( utilisateur );
        } else {
            resultat = "Echec de la réinitialisation.";
        }
    }
    
    public boolean verificationReinit( HttpServletRequest request, String date, String email ) throws Exception {
        boolean delai = false;
        
        Properties properties = new Properties();
        String CHAMP_MDP_REINIT;
        int MDP_REINIT;
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_MDP_REINIT = properties.getProperty( PROPERTY_MDP_REINIT );
            MDP_REINIT = Integer.parseInt(CHAMP_MDP_REINIT);
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
        
        // Déchiffrement de la date et de l'email pour les vérifications
    	email = email.replaceAll("\\s", "+");
    	date = date.replaceAll("\\s", "+");
        String emailDechiffre = dechiffrementEmail(email);
        String dateDechiffree = dechiffrementDate(date);
        // Date courante
        Date dateCourante = new Date();
	    long dateCourant = dateCourante.getTime()/1;
		// Transformations des dates pour la vérification
		long dateReinit = Long.parseLong(dateDechiffree);

        if ( utilisateurDao.trouver( emailDechiffre ) == null ){
        	delai = false;
        } else if (dateCourant <= (dateReinit + MDP_REINIT)){
        	// Permet de ne laisser le lien de validation utilisable que 24h.
        	delai = true;
        } else {
        	delai = false;
        }
        return delai;
    }
    
    
    
    
    /**
     * Valide l'adresse email saisie.
     */
    private void validationEmail( String email ) throws FormValidationException {
    	if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( utilisateurDao.trouver( email ) == null ) {
                throw new FormValidationException( "Les informations introduites sont incorrectes." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }
 
    /**
     * Méthode permettant de chiffrer un email
     * @param email
     * @return un email chiffré
     * 
     */
    private String chiffrementEmail(String email){
    	 BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
         String myEncryptionPassword = "HLB-Express R31n1t1al1$ation";
         textEncryptor.setPassword( myEncryptionPassword );
         String myEncryptedPassword = textEncryptor.encrypt( email );

    	 return myEncryptedPassword;
    }
    
    /**
     * Méthode permettant de chiffrer d'une date
     * @return une date chiffrée
     * 
     */
    private String chiffrementDate(){
    	Date date = new Date();
	    long diff = date.getTime();
		String dateString = String.valueOf(diff); 
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "HLB-Express R31n1t1al1$ation";
        textEncryptor.setPassword( myEncryptionPassword );

        String myEncryptedPassword = textEncryptor.encrypt( dateString );
   	 	return myEncryptedPassword;
    }
    
    /**
     * Méthode permettant de déchiffrer un email
     * @param email
     * @return un email déchiffré
     * 
     */
    public String dechiffrementEmail(String email){
    	 BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
         String myEncryptionPassword = "HLB-Express R31n1t1al1$ation";
         textEncryptor.setPassword( myEncryptionPassword );
         String plainText = textEncryptor.decrypt(email);

    	 return plainText;
    }
    
    /**
     * Méthode permettant de déchiffrer d'une date
     * @return une date déchiffrée
     * 
     */
    private String dechiffrementDate(String date){
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "HLB-Express R31n1t1al1$ation";
        textEncryptor.setPassword( myEncryptionPassword );
        String plainText = textEncryptor.decrypt(date);
        return plainText;
    }
    
    /**
     * Chiffrement du mot de passe
     * @param motDePasse
     * @param confirmation
     * @param utilisateur
     * 
     */
    private void traiterMotsDePasse( String motDePasse, String confirmation, Utilisateur utilisateur ) {
        try {
            validationMotsDePasse( motDePasse, confirmation );

            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            String myEncryptionPassword = "HLB-Express";
            textEncryptor.setPassword( myEncryptionPassword );
            String myEncryptedPassword = textEncryptor.encrypt( motDePasse );

            utilisateur.setMotDePasse( myEncryptedPassword );

        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
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
                throw new FormValidationException(
                        "Les mots de passe entrés sont différents, merci de les saisir à nouveau." );
            } else if ( motDePasse.length() < 3 ) {
            	// Minimum 3 caractères
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }
    /**
     * Setter des Erreurs
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
            return valeur;
        }
    }
}