package com.mdl.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOConfigurationException;

/**
 * Classe permettant l'envoi de mails
 * @author CrespeigneRomain
 *
 */
public class Email {
	
	private static final String FICHIER_PROPERTIES     = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_SMTP          = "smtp";
    private static final String PROPERTY_FROM          = "expediteur";
    private static final String PROPERTY_USERNAME 	   = "username";
    private static final String PROPERTY_PASSWORD      = "password";
    private static final String PROPERTY_FICHIER       = "fichier";
    private static final String PROPERTY_URL           = "url";
	/**
	 * Méthode permettant d'envoyer un mail pour la confirmation de l'inscription
	 * @param utilisateur
	 * @throws Exception
	 * @author CrespeigneRomain
	 */
    public static void envoiMail( Utilisateur utilisateur ) throws Exception {
    	
    	
    	 Properties properties = new Properties();
         String CHAMP_SMTP;
         String CHAMP_FROM;
         String CHAMP_NAME;
         String CHAMP_PASS;
         String CHAMP_FICHIER;
         String CHAMP_URL;

         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

         if ( fichierProperties == null ) {
             throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
         }

         try {
             properties.load( fichierProperties );
             CHAMP_SMTP = properties.getProperty( PROPERTY_SMTP );
             CHAMP_FROM = properties.getProperty( PROPERTY_FROM );
             CHAMP_NAME = properties.getProperty( PROPERTY_USERNAME );
             CHAMP_PASS = properties.getProperty( PROPERTY_PASSWORD );
             CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
             CHAMP_URL  = properties.getProperty( PROPERTY_URL);
         } catch ( IOException e ) {
             throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
         }
         
         
    	// stmp à utiliser
        String smtpHost = CHAMP_SMTP;
        String from = CHAMP_FROM;
        // Adresse mail d'envoi
        String to = utilisateur.getEmail();
        // Identifiants si il faut une identification
        String username = CHAMP_NAME;
        String password = CHAMP_PASS;

        // Chiffrement de l'identifiant pour la confirmation d'inscription
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "con/fir$mation d'1nscription";
        textEncryptor.setPassword( myEncryptionPassword );
        String myEncryptedPassword = textEncryptor.encrypt( to );
        
        Properties props = new Properties();
        props.put( "mail.smtp.host", smtpHost );

        Session session = Session.getDefaultInstance( props );
        session.setDebug( true );
        
        // Contenu du mail
        MimeMessage message = new MimeMessage( session );
        message.setFrom( new InternetAddress( from ) );
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
        message.setSubject( "Confirmation d'inscription" );
        String contenu = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/AccueilClient\">HLB-Express</a>,</br></br>"
                + "votre compte est actuellement inactif, vous empêchant de vous y connecter. </br></br>"
                + "Il vous faut confirmer votre inscription en cliquant sur le lien ci-après avant de vous donner "
                + "accès à l'ensemble du site : "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/Confirmation?confirm=" + myEncryptedPassword
                + "\">cliquez ici</a></br></br>"
                + "La société HLB-Express vous remercie de la confiance que vous lui offrez. </br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        message.setContent( contenu, "text/html; charset=UTF-8" );

        Transport tr = session.getTransport( "smtp" );
        tr.connect( smtpHost, username, password );
        message.saveChanges();

        // tr.send(message);
        /**
         * Genere l'erreur. Avec l authentification, oblige d utiliser
         * sendMessage meme pour une seule adresse...
         */

        tr.sendMessage( message, message.getAllRecipients() );
        tr.close();

    }

    /**
     * Méthode permettant l'envoi d'un mail lorsqu'une réponse à un litige a été effectuée
     * @param mail
     * @param id
     * @param colis
     * @throws Exception
     * @author CrespeigneRomain
     */
    public static void envoiReponseLitige( String mail, String id, String colis ) throws Exception {

    	Properties properties = new Properties();
        String CHAMP_SMTP;
        String CHAMP_FROM;
        String CHAMP_NAME;
        String CHAMP_PASS;
        String CHAMP_FICHIER;
        String CHAMP_URL;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_SMTP = properties.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = properties.getProperty( PROPERTY_FROM );
            CHAMP_NAME = properties.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = properties.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
            CHAMP_URL  = properties.getProperty( PROPERTY_URL);
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
        
    	// stmp à utiliser
        String smtpHost = CHAMP_SMTP;
        String from = CHAMP_FROM;
        // Adresse mail d'envoi
        String to = mail;
        // Identifiants si il faut une identification
        String username = CHAMP_NAME;
        String password = CHAMP_PASS;
        Properties props = new Properties();
        props.put( "mail.smtp.host", smtpHost );
        props.put( "mail.smtp.auth", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);

        Session session = Session.getDefaultInstance( props );
        session.setDebug( true );
        
        // Contenu du mail
        MimeMessage message = new MimeMessage( session );
        message.setFrom( new InternetAddress( from ) );
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
        message.setSubject( "Réponse au litige" );
        String contenu = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/AccueilClient\">HLB-Express</a>,</br></br>"
                + "Une réponse à votre litige identifié par \"" + id + "\" concernant le colis \"" + colis
                + "\" a été formulée."
                + "Vous pouvez aller la consulter en vous connectant au site. </br> </br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        message.setContent( contenu, "text/html; charset=UTF-8" );

        Transport tr = session.getTransport( "smtp" );
        tr.connect( smtpHost, username, password );
        message.saveChanges();

        // tr.send(message);
        /**
         * Genere l'erreur. Avec l authentification, oblige d utiliser
         * sendMessage meme pour une seule adresse...
         */

        tr.sendMessage( message, message.getAllRecipients() );
        tr.close();

    }

    /**
     * Méthode permettant l'envoi d'un mail lors de la fermeture d'un litige
     * @param mail
     * @param id
     * @param colis
     * @throws Exception
     * @author CrespeigneRomain
     */
    public static void envoiFermetureLitige( String mail, String id, String colis ) throws Exception {
    	
    	Properties properties = new Properties();
        String CHAMP_SMTP;
        String CHAMP_FROM;
        String CHAMP_NAME;
        String CHAMP_PASS;
        String CHAMP_FICHIER;
        String CHAMP_URL;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_SMTP = properties.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = properties.getProperty( PROPERTY_FROM );
            CHAMP_NAME = properties.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = properties.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
            CHAMP_URL  = properties.getProperty( PROPERTY_URL);
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
        
    	// stmp à utiliser
        String smtpHost = CHAMP_SMTP;
        String from = CHAMP_FROM;
        // Adresse mail d'envoi
        String to = mail;
        // Identifiants si il faut une identification
        String username = CHAMP_NAME;
        String password = CHAMP_PASS;
        Properties props = new Properties();
        props.put( "mail.smtp.host", smtpHost );
        props.put( "mail.smtp.auth", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);

        Session session = Session.getDefaultInstance( props );
        session.setDebug( true );

        // Contenu du mail
        MimeMessage message = new MimeMessage( session );
        message.setFrom( new InternetAddress( from ) );
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
        message.setSubject( "Fermeture du litige" );
        String contenu = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/AccueilClient\">HLB-Express</a>,</br></br>"
                + "Votre litige identifié par \"" + id + "\" concernant le colis \"" + colis + "\" a été fermé."
                + "Vous pouvez aller la consulter en vous connectant au site. </br> </br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        message.setContent( contenu, "text/html; charset=UTF-8" );

        Transport tr = session.getTransport( "smtp" );
        tr.connect( smtpHost, username, password );
        message.saveChanges();

        // tr.send(message);
        /**
         * Genere l'erreur. Avec l authentification, oblige d utiliser
         * sendMessage meme pour une seule adresse...
         */

        tr.sendMessage( message, message.getAllRecipients() );
        tr.close();

    }
    
    public static void envoiResetMdp( String mail, String email, String date ) throws Exception {

    	Properties properties = new Properties();
        String CHAMP_SMTP;
        String CHAMP_FROM;
        String CHAMP_NAME;
        String CHAMP_PASS;
        String CHAMP_FICHIER;
        String CHAMP_URL;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_SMTP = properties.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = properties.getProperty( PROPERTY_FROM );
            CHAMP_NAME = properties.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = properties.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
            CHAMP_URL  = properties.getProperty( PROPERTY_URL);
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
        
    	// stmp à utiliser
        String smtpHost = CHAMP_SMTP;
        String from = CHAMP_FROM;
        // Adresse mail d'envoi
        String to = mail;
        // Identifiants si il faut une identification
        String username = CHAMP_NAME;
        String password = CHAMP_PASS;
        Properties props = new Properties();
        props.put( "mail.smtp.host", smtpHost );
        props.put( "mail.smtp.auth", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);

        System.out.println(email);
        System.out.println(date);
        
        Session session = Session.getDefaultInstance( props );
        session.setDebug( true );

        // Contenu du mail
        MimeMessage message = new MimeMessage( session );
        message.setFrom( new InternetAddress( from ) );
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
        message.setSubject( "Réinitialisation" );
        String contenu = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/AccueilClient\">HLB-Express</a>,</br></br>"
                + "une demande de réinitialisation du mot de passe a été faite. Pour continuer la procédure, "
                + "veuillez cliquer sur le lien ci-après. Ce lien est valide pour une durée de 24 heures après la "
                + "demande de réinitialisation. Si celui-ci n'est plus valide, il vous faudra recommencer la manipulation."
                + "Réinitilisation du mot de passe : "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/Reinitialisation?reinit="+email+"&v="+date
                + "\">cliquez ici</a></br></br>"
                + "La société HLB-Express vous remercie de la confiance que vous lui offrez. </br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        message.setContent( contenu, "text/html; charset=UTF-8" );

        Transport tr = session.getTransport( "smtp" );
        tr.connect( smtpHost, username, password );
        message.saveChanges();

        // tr.send(message);
        /**
         * Genere l'erreur. Avec l authentification, oblige d utiliser
         * sendMessage meme pour une seule adresse...
         */

        tr.sendMessage( message, message.getAllRecipients() );
        tr.close();

    }
    
    public static void envoiNotifSurpoids( String mail, String email, String ICU ) throws Exception {

    	Properties properties = new Properties();
        String CHAMP_SMTP;
        String CHAMP_FROM;
        String CHAMP_NAME;
        String CHAMP_PASS;
        String CHAMP_FICHIER;
        String CHAMP_URL;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_SMTP = properties.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = properties.getProperty( PROPERTY_FROM );
            CHAMP_NAME = properties.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = properties.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = properties.getProperty( PROPERTY_FICHIER );
            CHAMP_URL  = properties.getProperty( PROPERTY_URL);
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
    	// stmp à utiliser
        String smtpHost = CHAMP_SMTP;
        String from = CHAMP_FROM;
        // Adresse mail d'envoi
        String to = mail;
        // Identifiants si il faut une identification
        String username = CHAMP_NAME;
        String password = CHAMP_PASS;
        Properties props = new Properties();
        props.put( "mail.smtp.host", smtpHost );
        props.put( "mail.smtp.auth", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        Session session = Session.getDefaultInstance( props );
        session.setDebug( true );

        // Contenu du mail
        MimeMessage message = new MimeMessage( session );
        message.setFrom( new InternetAddress( from ) );
        message.addRecipient( Message.RecipientType.TO, new InternetAddress( to ) );
        message.setSubject( "Réinitialisation" );
        String contenu = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"+CHAMP_FICHIER+"/AccueilClient\">HLB-Express</a>,</br></br>"
                + "un surpoids de votre colis, identifié par la référence \""+ICU+"\", a été constaté entrainant une "
                + "surfacturation de votre commande. Il vous est possible de la consulter en vous connectant sur notre "
                + "site, sous la rubrique \"Surfacturation\" du menu \"Commande\". "
                + "La société HLB-Express vous remercie de la confiance que vous lui offrez. </br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        message.setContent( contenu, "text/html; charset=UTF-8" );

        Transport tr = session.getTransport( "smtp" );
        tr.connect( smtpHost, username, password );
        message.saveChanges();

        // tr.send(message);
        /**
         * Genere l'erreur. Avec l authentification, oblige d utiliser
         * sendMessage meme pour une seule adresse...
         */

        tr.sendMessage( message, message.getAllRecipients() );
        tr.close();

    }
    
}