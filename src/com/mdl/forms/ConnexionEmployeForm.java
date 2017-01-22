package com.mdl.forms;
 
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Employe;
import com.mdl.dao.EmployeDAO;
 
/**
 * Classe permettant de faire les vérifications lors de la connexion de l'employé
 * 
 *
 */
public final class ConnexionEmployeForm {
    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    // Champs des formulaires à récupérer
 
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
 
    private EmployeDAO     employeDao;
    
    /**
     * Constructeur de ConnexionEmployeForm
     * @param employeDao
     * 
     */
    public ConnexionEmployeForm( EmployeDAO employeDao ) {
        this.employeDao = employeDao;
    }
    
    /**
     * Getter du résultat
     * @return résultat
     * 
     */
    public String getResultat() {
        return resultat;
    }
 
    /**
     * Getteur des erreurs
     * @return un Map des erreurs
     * 
     */
    public Map<String, String> getErreurs() {
        return erreurs;
    }
 
    /**
     * Permet de vérifier et de traiter les informations issues du formulaire de connexion
     * @param request
     * @return un objet Employé
     * 
     */
    public Employe connecterEmploye( HttpServletRequest request ) {
        // Récupération des champs du formulaire 
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
 
        Employe employe = new Employe();
        
        // Validation du champ email. 
        try {
            validationEmail( email );
            	try{ 
            		// Traitement du mot de passe
                	traiterMotDePasse( motDePasse, employe, email );
                } catch (FormValidationException e){
                	 setErreur( CHAMP_EMAIL, e.getMessage() );
                }
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        employe.setEmail( email );
        
        if ( erreurs.isEmpty() ) {
        } else {
            resultat = "Echec de la connexion.";
        }
 
        return employe;
    }
    
    /**
     * Vérification du mot de passe
     * @param motDePasse
     * @param employe
     * @param email
     * @throws FormValidationException
     * 
     */
    private void traiterMotDePasse( String motDePasse, Employe employe, String email ) throws FormValidationException{
        String mdp;
        String mail;
        
        employe = employeDao.trouver(email);
        mdp = employe.getMotDePasse();
        
        
        
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "HLB-Express";
        textEncryptor.setPassword(myEncryptionPassword);
        String plainText = textEncryptor.decrypt(mdp);
        
        //mon mot de passe après décryptage avec la même clé
        if (motDePasse == null){
        	throw new FormValidationException( "Les informations introduites sont incorrectes." );
        }
        if (motDePasse.equals(plainText) != true){
        	throw new FormValidationException( "Les informations introduites sont incorrectes." );
        }

        employe.setMotDePasse( mdp );
    }
    /**
     * Valide l'adresse e-mail saisie.
     * @param email
     * @throws FormValidationException
     * 
     */
    private void validationEmail( String email ) throws FormValidationException {
    	if ( email != null ) {
    		// Vérification du format de l'e-mail selon un regex
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( employeDao.trouver( email ) == null ) {
                throw new FormValidationException( "Les informations introduites sont incorrectes." );
            }
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
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}