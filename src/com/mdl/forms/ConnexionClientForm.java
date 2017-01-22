package com.mdl.forms;
 
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.UtilisateurDAO;
 
/**
 * Classe permettant de faire les vérifications lors de la connexion du client
 * 
 *
 */
public final class ConnexionClientForm {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    // Champs des formulaires à récupérer
 
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
 
    private UtilisateurDAO      utilisateurDao;

    /**
     * Constructeur de ConnexionClientForm
     * @param utilisateurDao
     * 
     */
    public ConnexionClientForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }
    
    /**
     * Getter du résultat
     * @return le résultat
     * 
     */
    public String getResultat() {
        return resultat;
    }
 
    /**
     * Getter des erreurs
     * @return une Map des erreurs
     * 
     */
    public Map<String, String> getErreurs() {
        return erreurs;
    }
    /**
     * Permet de vérifier et de traiter les informations issues du formulaire de connexion
     * @param request
     * @return un objet Utilisateur
     * 
     */
    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {
        // Récupération des informations des formulaires
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
 
        Utilisateur utilisateur = new Utilisateur();
        
        // Validation du champ email. 
        try {
            validationEmail( email );
            	try{ 
            		// Vérification du champ mot de passe
                	traiterMotDePasse( motDePasse, utilisateur, email );
                } catch (FormValidationException e){
                	 setErreur( CHAMP_EMAIL, e.getMessage() );
                }
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );
        
        if ( erreurs.isEmpty() ) {
        	try{
        		utilisateur = traiterValidation(email);
        	}catch ( FormValidationException e ) {
                setErreur( CHAMP_EMAIL, e.getMessage() );
            }
            
        } else {
            resultat = "Echec de la connexion.";
        }
 
        return utilisateur;
    }
    
    /**
     * Vérification de la validation du compte lors de la connexion
     * @param email
     * @return un objet Utilisateur
     * @throws FormValidationException
     * 
     */
    private Utilisateur traiterValidation(String email) throws FormValidationException{
    	Utilisateur utilisateur = utilisateurDao.trouver( email );
    	String confirmation = utilisateur.getValidation();
    	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
 	    String myEncryptionPassword = "con/fir$mation d'1nscription";
 	    // Message utilisé lors du chiffrement de l'information de validation
 	    textEncryptor.setPassword(myEncryptionPassword);
        String myDecryptedValidation = textEncryptor.decrypt(confirmation);
        // Déchiffrement de l'information
        if(myDecryptedValidation.equals("oui")){
        	resultat = "Succès de la connexion.";
        } else {
        	throw new FormValidationException( "Echec de la connexion, votre compte n'est pas validé." );
        }
        return utilisateur;
    }
    
    /**
     * Vérification du mot de passe 
     * @param motDePasse
     * @param utilisateur
     * @param email
     * @throws FormValidationException
     * 
     */
    private void traiterMotDePasse( String motDePasse, Utilisateur utilisateur, String email ) throws FormValidationException{
        String mdp;
        
        utilisateur = utilisateurDao.trouver(email);
        mdp = utilisateur.getMotDePasse();
        
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "HLB-Express";
        textEncryptor.setPassword(myEncryptionPassword);
        String plainText = textEncryptor.decrypt(mdp);
        
        //mon mot de passe après déchiffrement avec la même clé 
        if (motDePasse == null){
        	throw new FormValidationException( "Les informations introduites sont incorrectes." );
        }
        if (motDePasse.equals(plainText) != true){
        	throw new FormValidationException( "Les informations introduites sont incorrectes." );
        }

        utilisateur.setMotDePasse( mdp );
    }
    
    /**
     * Validation de l'adresse e-mail saisie.
     * @param email
     * @throws FormValidationException
     * 
     */
    private void validationEmail( String email ) throws FormValidationException {
    	if ( email != null ) {
    		// Vérification du format de l'e-mail selon un regex
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