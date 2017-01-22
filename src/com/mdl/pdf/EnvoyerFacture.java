package com.mdl.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.DATE;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mdl.beans.Commande;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;

/**
 * Classe permettant de générer un formulaire, une facture, et l'envoyer par
 * mail à l'utilisateur
 * 
 */
public class EnvoyerFacture {

    private static final String FICHIER_PROPERTIES     = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_SMTP          = "smtp";
    private static final String PROPERTY_FROM          = "expediteur";
    private static final String PROPERTY_USERNAME 	   = "username";
    private static final String PROPERTY_PASSWORD      = "password";
    private static final String PROPERTY_FICHIER       = "fichier";
    private static final String PROPERTY_URL           = "url";
    /**
     * Envoie un email avec une facture (pdf) à l'utilisateur contenant des
     * informations sur le client et la commande et son prix
     * 
     * @param email
     * @param commande
     * @param utilisateur
     * @throws Exception 
     */
    public void EmailFacture( String email, Commande commande, Utilisateur utilisateur ) throws Exception {

    	// Permet d'aller rechercher les informations dans le fichier de configuration
    	Properties propertiesTemp = new Properties();
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
            propertiesTemp.load( fichierProperties );
            CHAMP_SMTP = propertiesTemp.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = propertiesTemp.getProperty( PROPERTY_FROM );
            CHAMP_NAME = propertiesTemp.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = propertiesTemp.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = propertiesTemp.getProperty( PROPERTY_FICHIER );
            CHAMP_URL  = propertiesTemp.getProperty( PROPERTY_URL );
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
        
        
        // stmp à utiliser
       String smtpHost = CHAMP_SMTP;
       String from = CHAMP_FROM;
       // Adresse mail d'envoi
       String to = email;
       // Identifiants si il faut une identification
       String username = CHAMP_NAME;
       String password = CHAMP_PASS;

       String content = "Bienvenue sur le site web de la société "
                + "<a href=\""+ CHAMP_URL +"/"
                + CHAMP_FICHIER
                + "/AccueilClient\">HLB-Express</a>,</br></br>"
                + "Votre formulaire de facturation se trouve en pièce jointe. Vous pouvez y trouver les informations sur les tarifs "
                + "de la commande que vous avez déjà payée.</br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail vous a été envoyé automatiquement par HLB-Express.";
        String subject = "Facturation de commande";

        Properties properties = new Properties();

        properties.put( "mail.smtp.host", smtpHost );
        properties.put( "mail.smtp.auth", "true" );
        properties.put( "mail.smtp.user", username );
        properties.put( "mail.smtp.password", password );

        Session session = Session.getDefaultInstance( properties, null );

        ByteArrayOutputStream outputStream = null;

        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent( content, "text/html; charset=UTF-8" );

            outputStream = new ByteArrayOutputStream();
            GenererPDF( outputStream, commande, utilisateur );
            byte[] bytes = outputStream.toByteArray();

            DataSource dataSource = new ByteArrayDataSource( bytes, "application/pdf" );
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler( new DataHandler( dataSource ) );
            pdfBodyPart.setFileName( "Facturation_commande.pdf" );

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart( textBodyPart );
            mimeMultipart.addBodyPart( pdfBodyPart );

            InternetAddress iaSender = new InternetAddress( from );
            InternetAddress iaRecipient = new InternetAddress( to );

            MimeMessage mimeMessage = new MimeMessage( session );
            mimeMessage.setSender( iaSender );
            mimeMessage.setSubject( subject );
            mimeMessage.setRecipient( Message.RecipientType.TO, iaRecipient );
            mimeMessage.setContent( mimeMultipart );

            Transport tr = session.getTransport( "smtp" );
            tr.connect( smtpHost, username, password );
            mimeMessage.saveChanges();

            // tr.send(message);
            /**
             * Genere l'erreur. Avec l authentification, oblige d utiliser
             * sendMessage meme pour une seule adresse...
             */

            tr.sendMessage( mimeMessage, mimeMessage.getAllRecipients() );
            tr.close();

            System.out.println( "sent from " + from +
                    ", to " + to +
                    "; server = " + smtpHost );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            if ( null != outputStream ) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch ( Exception ex ) {
                }
            }
        }
    }

    /**
     * Génère un pdf avec les informations de la commande, le client et la
     * facture de la commande
     * 
     * @param outputStream
     * @param commande
     * @param utilisateur
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Commande commande, Utilisateur utilisateur ) throws Exception {
        Document document = new Document( PageSize.A4 );
        PdfWriter writer = PdfWriter.getInstance( document, outputStream );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, commande, utilisateur );
    }

    /**
     * Envoie un email avec une surfacturation (pdf) à l'utilisateur contenant
     * des informations sur le client et la commande et son prix
     * 
     * @param email
     * @param surfact
     * @param utilisateur
     * @throws Exception 
     */
    public void EmailFacture( String email, Surfacturation surfact, Utilisateur utilisateur ) throws Exception {

    	// Permet d'aller rechercher les informations dans le fichier de configuration
    	Properties propertiesTemp = new Properties();
        String CHAMP_SMTP;
        String CHAMP_FROM;
        String CHAMP_NAME;
        String CHAMP_PASS;
        String CHAMP_FICHIER;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            propertiesTemp.load( fichierProperties );
            CHAMP_SMTP = propertiesTemp.getProperty( PROPERTY_SMTP );
            CHAMP_FROM = propertiesTemp.getProperty( PROPERTY_FROM );
            CHAMP_NAME = propertiesTemp.getProperty( PROPERTY_USERNAME );
            CHAMP_PASS = propertiesTemp.getProperty( PROPERTY_PASSWORD );
            CHAMP_FICHIER = propertiesTemp.getProperty( PROPERTY_FICHIER );
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

        String content = "Bienvenue sur le site web de la société "
                + "<a href=\"http://localhost:8080/"
                + CHAMP_FICHIER
                + "/AccueilClient\">HLB-Express</a>,</br></br>"
                + "Votre formulaire de facturation se trouve en pièce jointe. Vous pouvez y trouver les informations sur la surfacturation payée "
                + "de votre commande.</br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail vous a été envoyé automatiquement par HLB-Express.";
        String subject = "Surfacturation de commande";

        Properties properties = new Properties();
        properties.put( "mail.smtp.host", smtpHost );
        Session session = Session.getDefaultInstance( properties, null );

        ByteArrayOutputStream outputStream = null;

        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent( content, "text/html; charset=UTF-8" );

            outputStream = new ByteArrayOutputStream();
            GenererPDF( outputStream, surfact, utilisateur );
            byte[] bytes = outputStream.toByteArray();

            DataSource dataSource = new ByteArrayDataSource( bytes, "application/pdf" );
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler( new DataHandler( dataSource ) );
            pdfBodyPart.setFileName( "Surfacturation_commande.pdf" );

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart( textBodyPart );
            mimeMultipart.addBodyPart( pdfBodyPart );

            InternetAddress iaSender = new InternetAddress( from );
            InternetAddress iaRecipient = new InternetAddress( to );

            MimeMessage mimeMessage = new MimeMessage( session );
            mimeMessage.setSender( iaSender );
            mimeMessage.setSubject( subject );
            mimeMessage.setRecipient( Message.RecipientType.TO, iaRecipient );
            mimeMessage.setContent( mimeMultipart );

            Transport tr = session.getTransport( "smtp" );
            tr.connect( smtpHost, username, password );
            mimeMessage.saveChanges();

            // tr.send(message);
            /**
             * Genere l'erreur. Avec l authentification, oblige d utiliser
             * sendMessage meme pour une seule adresse...
             */

            tr.sendMessage( mimeMessage, mimeMessage.getAllRecipients() );
            tr.close();

            System.out.println( "sent from " + from +
                    ", to " + to +
                    "; server = " + smtpHost );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            if ( null != outputStream ) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch ( Exception ex ) {
                }
            }
        }
    }

    /**
     * Génère un pdf avec les informations de la commande, le client et la
     * surfacturation de la commande à envoyer par mail
     * 
     * @param outputStream
     * @param surfact
     * @param utilisateur
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Surfacturation surfact, Utilisateur utilisateur )
            throws Exception {
        Document document = new Document( PageSize.A4 );
        PdfWriter writer = PdfWriter.getInstance( document, outputStream );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, surfact, utilisateur );

    }

    /**
     * Génère un pdf avec les informations de la surfacturation pour l'afficher
     * en ligne
     * 
     * @param outputStream
     * @param surfact
     * @param utilisateur
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Surfacturation surfact, Utilisateur utilisateur,
            HttpServletResponse response )
            throws Exception {
        Document document = new Document( PageSize.A4 );

        response.setContentType( "application/pdf" );
        response.setHeader( "Content-Disposition", " inline; filename=Surfacturation.pdf" );
        PdfWriter writer = PdfWriter.getInstance( document, response.getOutputStream() );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, surfact, utilisateur );

    }

    /**
     * Génère un pdf avec les informations de la commande, le client et la
     * surfacturation de la commande qui est consultable en ligne
     * 
     * @param outputStream
     * @param surfact
     * @param utilisateur
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Commande commande, Utilisateur utilisateur,
            HttpServletResponse response ) throws Exception {
        Document document = new Document( PageSize.A4 );
        response.setContentType( "application/pdf" );
        response.setHeader( "Content-Disposition", " inline; filename=Facture.pdf" );

        PdfWriter writer = PdfWriter.getInstance( document, response.getOutputStream() );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, commande, utilisateur );

    }

    /**
     * Informations du pdf concernant la commande, le client et la facture de la
     * commande
     * 
     * @param outputStream
     * @param commande
     * @param utilisateur
     * @throws Exception
     */
    public void InformationPDF( Document document, Commande commande, Utilisateur utilisateur ) throws Exception {
        try {

            document.addTitle( "Facture" );
            document.addAuthor( "HLB-Express" );
            document.addSubject( "Facture" );
            document.addKeywords( "HLB-Express, Commande" );

            document.open();

            BaseFont fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font titre = new Font( fonte );
            titre.setStyle( Font.BOLD );
            titre.setSize( 25.0f );
            /* 95 x 95 px */

            Paragraph paragraph = new Paragraph( new Chunk( "HLB - Express", titre ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );
            paragraph = new Paragraph( new Chunk( "Facture \n\n\n", titre ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font titreTable = new Font( fonte );
            titreTable.setStyle( Font.BOLD | Font.ITALIC );
            titreTable.setSize( 14.0f );

            fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font corpsTable = new Font( fonte );
            corpsTable.setSize( 14.0f );

            float[] colsWidth = { 1f, 2f }; // Code 1
            PdfPTable tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            paragraph = new Paragraph( new Chunk( "Référence de la commande : " + commande.getIcu(), corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            paragraph = new Paragraph( new Chunk( "Date de paiement : "
                    + commande.getDate_enregistrement() 
                    ,
                    corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            PdfPCell cell = new PdfPCell( new Paragraph( new Chunk( "Client", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );
            tableau.addCell( new Paragraph( new Chunk( "Nom & Prénom", corpsTable ) ) );
            tableau.addCell( utilisateur.getNom() + " " + utilisateur.getPrenom() );
            tableau.addCell( new Paragraph( new Chunk( "Rue, numéro & boîte", corpsTable ) ) );
            if ( utilisateur.getAdresseBoite() != null )
                tableau.addCell( utilisateur.getAdresseRue() + ", "
                        + utilisateur.getAdresseNum() + " - Boîte : " + utilisateur.getAdresseBoite() );
            else
                tableau.addCell( utilisateur.getAdresseRue() + ", "
                        + utilisateur.getAdresseNum() );
            tableau.addCell( new Paragraph( new Chunk( "Pays", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdressePays() );
            tableau.addCell( new Paragraph( new Chunk( "Code postal", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdresseCode() );
            tableau.addCell( new Paragraph( new Chunk( "Localité", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdresseLoc() );
            tableau.addCell( new Paragraph( new Chunk( "Téléphone", corpsTable ) ) );
            tableau.addCell( utilisateur.getTelephonePortable() );
            if ( utilisateur.getNum_tva() != null ) {
                tableau.addCell( new Paragraph( new Chunk( "Numéro de TVA", corpsTable ) ) );
                tableau.addCell( utilisateur.getNum_tva() );
            }

            document.add( tableau );

            tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            cell = new PdfPCell( new Paragraph( new Chunk( "Tarification de la commande", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );

            tableau.addCell( new Paragraph( new Chunk( "Poids",
                    corpsTable ) ) );
            tableau.addCell( "" + commande.getPoids() + " kg" );

            tableau.addCell( new Paragraph( new Chunk( "Valeur estimée",
                    corpsTable ) ) );
            tableau.addCell( "" + commande.getValeurEstimee() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix en fonction du poids et du pays de destination",
                    corpsTable ) ) );
            tableau.addCell( "" + commande.getPrixBase() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Frais de port",
                    corpsTable ) ) );
            tableau.addCell( "" + commande.getPrixCentreTraverse() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix de l'assurance",
                    corpsTable ) ) );
            tableau.addCell( "" + commande.getPrixAssurance() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix de l'accusé de réception", corpsTable ) ) );
            tableau.addCell( commande.getPrixAccuse() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix Total", corpsTable ) ) );
            tableau.addCell( commande.getPrix() + " €" );

            document.add( tableau );

        } catch ( DocumentException de ) {
            de.printStackTrace();
        } catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        /* Fermeture du document */
        document.close();
    }

    /**
     * Informations du pdf concernant la surfacturation de la commande, le
     * client et la facture de la commande
     * 
     * @param outputStream
     * @param commande
     * @param utilisateur
     * @throws Exception
     */
    public void InformationPDF( Document document, Surfacturation surfact, Utilisateur utilisateur ) throws Exception {
        try {
            document.addTitle( "Facture" );
            document.addAuthor( "HLB-Express" );
            document.addSubject( "Facture" );
            document.addKeywords( "HLB-Express, Commande" );

            document.open();

            BaseFont fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font titre = new Font( fonte );
            titre.setStyle( Font.BOLD );
            titre.setSize( 25.0f );
            /* 95 x 95 px */

            Paragraph paragraph = new Paragraph( new Chunk( "HLB - Express", titre ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );
            paragraph = new Paragraph( new Chunk( "Facture \n\n\n", titre ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font titreTable = new Font( fonte );
            titreTable.setStyle( Font.BOLD | Font.ITALIC );
            titreTable.setSize( 14.0f );

            fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font corpsTable = new Font( fonte );
            corpsTable.setSize( 14.0f );

            float[] colsWidth = { 1f, 2f }; // Code 1
            PdfPTable tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            paragraph = new Paragraph( new Chunk( "Référence de la commande : " + surfact.getICU(), corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            paragraph = new Paragraph( new Chunk( "Date de paiement de la surfacturation : "
                    + surfact.getDate_paiement() 
                    ,
                    corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            PdfPCell cell = new PdfPCell( new Paragraph( new Chunk( "Client", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );
            tableau.addCell( new Paragraph( new Chunk( "Nom & Prénom", corpsTable ) ) );
            tableau.addCell( utilisateur.getNom() + " " + utilisateur.getPrenom() );
            tableau.addCell( new Paragraph( new Chunk( "Rue, numéro & boîte", corpsTable ) ) );
            if ( utilisateur.getAdresseBoite() != null )
                tableau.addCell( utilisateur.getAdresseRue() + ", "
                        + utilisateur.getAdresseNum() + " - Boîte : " + utilisateur.getAdresseBoite() );
            else
                tableau.addCell( utilisateur.getAdresseRue() + ", "
                        + utilisateur.getAdresseNum() );
            tableau.addCell( new Paragraph( new Chunk( "Pays", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdressePays() );
            tableau.addCell( new Paragraph( new Chunk( "Code postal", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdresseCode() );
            tableau.addCell( new Paragraph( new Chunk( "Localité", corpsTable ) ) );
            tableau.addCell( utilisateur.getAdresseLoc() );
            tableau.addCell( new Paragraph( new Chunk( "Téléphone", corpsTable ) ) );
            tableau.addCell( utilisateur.getTelephonePortable() );
            if ( utilisateur.getNum_tva() != null ) {
                tableau.addCell( new Paragraph( new Chunk( "Numéro de TVA", corpsTable ) ) );
                tableau.addCell( utilisateur.getNum_tva() );
            }

            document.add( tableau );

            tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            cell = new PdfPCell( new Paragraph( new Chunk( "Information sur la surfacturation", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );

            tableau.addCell( new Paragraph( new Chunk( "Poids renseigné",
                    corpsTable ) ) );
            tableau.addCell( "" + surfact.getPoids_renseigne() + " kg" );

            tableau.addCell( new Paragraph( new Chunk( "Poids réel",
                    corpsTable ) ) );
            tableau.addCell( "" + surfact.getPoids_reel() + " kg" );

            tableau.addCell( new Paragraph( new Chunk( "Prix avant surfacturation",
                    corpsTable ) ) );
            tableau.addCell( "" + surfact.getPrix_before() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix de la surfacturation",
                    corpsTable ) ) );
            tableau.addCell( "" + surfact.getPrix_surfact() + " €" );

            tableau.addCell( new Paragraph( new Chunk( "Prix total surfacturé",
                    corpsTable ) ) );
            tableau.addCell( "" + surfact.getPrix_total() + " €" );

            document.add( tableau );

        } catch ( DocumentException de ) {
            de.printStackTrace();
        } catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        /* Fermeture du document */
        document.close();
    }
}
