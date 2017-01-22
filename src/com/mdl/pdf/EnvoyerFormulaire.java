package com.mdl.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mdl.beans.Commande;

/**
 * Classe permettant d'envoyer le bordereau d'envoi d'un colis
 * 
 * @author CrespeigneRomain
 * 
 */
public class EnvoyerFormulaire {

	private static final String FICHIER_PROPERTIES     = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_SMTP          = "smtp";
    private static final String PROPERTY_FROM          = "expediteur";
    private static final String PROPERTY_USERNAME 	   = "username";
    private static final String PROPERTY_PASSWORD      = "password";
    private static final String PROPERTY_FICHIER       = "fichier";
    private static final String PROPERTY_URL           = "url";

    /**
     * Méthode permettant d'envoyer et le bordereau d'envoi d'un colis
     * 
     * @param email
     * @param commande
     * @author CrespeigneRomain
     * @throws Exception 
     */
    public void EmailFormulaire( String email, Commande commande ) throws Exception {

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
                + "<a href=\""+ CHAMP_URL +"/"+ CHAMP_FICHIER + "/AccueilClient\">HLB-Express</a>,</br></br>"
                + "Votre formulaire se trouve en pièce jointe. Celui-ci doit être imprimé <u><b>avant</b></u> "
                + "que le coursier ne soit venu chercher votre colis.</br></br>"
                + "A bientôt </br></br>"
                + "-----------------------------------------------------</br>"
                + "Ce mail a été envoyé automatiquement par HLB-Express.";
        String subject = "Bordereau d'envoi"; // this will be the subject of the
                                              // email

        Properties properties = new Properties();
        properties.put( "mail.smtp.host", smtpHost );
        properties.put( "mail.smtp.auth", "true" );
        Session session = Session.getDefaultInstance( properties, null );

        ByteArrayOutputStream outputStream = null;

        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent( content, "text/html; charset=UTF-8" );

            outputStream = new ByteArrayOutputStream();
            GenererPDF( outputStream, commande );
            byte[] bytes = outputStream.toByteArray();

            // Création temporaire du pdf
            DataSource dataSource = new ByteArrayDataSource( bytes, "application/pdf" );
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler( new DataHandler( dataSource ) );
            pdfBodyPart.setFileName( "Bordereau_d_envoi.pdf" );

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
            // tr.send( mimeMessage );
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
     * Génération du bordereau d'envoi sous format PDF à envoyer par mail
     * 
     * @param outputStream
     * @param commande
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Commande commande ) throws Exception {
        Document document = new Document( PageSize.A4 );
        // Création du PDF en inséreant les différentes informations
        PdfWriter writer = PdfWriter.getInstance( document, outputStream );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, commande );

    }

    /**
     * Génération du bordereau d'envoi sous format PDF à consulter en ligne
     * 
     * @param outputStream
     * @param commande
     * @throws Exception
     */
    public void GenererPDF( OutputStream outputStream, Commande commande, HttpServletResponse response )
            throws Exception {
        Document document = new Document( PageSize.A4 );
        response.setContentType( "application/pdf" );
        response.setHeader( "Content-Disposition", " inline; filename=Bordereau_envoi.pdf" );

        PdfWriter writer = PdfWriter.getInstance( document, response.getOutputStream() );
        writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseThumbs );
        InformationPDF( document, commande );

    }

    /**
     * Informations du pdf concernant le bordereau d'envoi
     * 
     * @param outputStream
     * @param commande
     * @param utilisateur
     * @throws Exception
     */
    public void InformationPDF( Document document, Commande commande ) throws Exception {
        try {

            document.addTitle( "Formulaire de commande" );
            document.addAuthor( "HLB-Express" );
            document.addSubject( "Formulaire de commande" );
            document.addKeywords( "HLB-Express, Commande" );

            document.open();

            BaseFont fonte = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
            Font titre = new Font( fonte );
            titre.setStyle( Font.BOLD );
            titre.setSize( 25.0f );
            /* 95 x 95 px */
            String id = Integer.toString( commande.getId() );
            BarcodeQRCode qrcode = new BarcodeQRCode( id, 95, 95, null );
            Image image = qrcode.getImage();
            image.setAlignment( Image.RIGHT | Image.TEXTWRAP );
            document.add( image );

            Paragraph paragraph = new Paragraph( new Chunk( "HLB - Express", titre ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );
            paragraph = new Paragraph( new Chunk( "Bordereau d'envoi \n\n\n", titre ) );
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

            paragraph = new Paragraph( new Chunk( "Date d'envoi : " + commande.getDatePickup() + " entre 8h et 12h.",
                    corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

            PdfPCell cell = new PdfPCell( new Paragraph( new Chunk( "Emetteur", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );
            tableau.addCell( new Paragraph( new Chunk( "Nom & Prénom", corpsTable ) ) );
            tableau.addCell( commande.getNom_expediteur() + " " + commande.getPrenom_expediteur() );
            // tableau.addCell(commande.getNom_expediteur()
            // +" "+commande.getPrenom_expediteur());
            tableau.addCell( new Paragraph( new Chunk( "Rue, numéro & boîte", corpsTable ) ) );
            if ( commande.getAdresse_boite_expediteur() != null )
                tableau.addCell( commande.getAdresse_rue_expediteur() + ", "
                        + commande.getAdresse_num_expediteur() + " - Boîte : " + commande.getAdresse_boite_expediteur() );
            else
                tableau.addCell( commande.getAdresse_rue_expediteur() + ", " + commande.getAdresse_num_expediteur() );
            tableau.addCell( new Paragraph( new Chunk( "Code postal", corpsTable ) ) );
            tableau.addCell( commande.getAdresse_code_expediteur() );
            tableau.addCell( new Paragraph( new Chunk( "Localité", corpsTable ) ) );
            tableau.addCell( commande.getAdresse_loc_expediteur() );
            tableau.addCell( new Paragraph( new Chunk( "Téléphone", corpsTable ) ) );
            tableau.addCell( commande.getTel_expediteur() );

            document.add( tableau );

            tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            cell = new PdfPCell( new Paragraph( new Chunk( "Destinataire", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );
            tableau.addCell( new Paragraph( new Chunk( "Nom & Prénom", corpsTable ) ) );
            tableau.addCell( commande.getNom_destinataire() + " " + commande.getPrenom_destinataire() );
            tableau.addCell( new Paragraph( new Chunk( "Rue, numéro & boîte", corpsTable ) ) );
            if ( commande.getAdresse_boite_destinataire() != null )
                tableau.addCell( commande.getAdresse_rue_destinataire() + ", "
                        + commande.getAdresse_num_destinataire() + " - Boîte :"
                        + commande.getAdresse_boite_destinataire() );
            else
                tableau.addCell( commande.getAdresse_rue_destinataire() + ", " + commande.getAdresse_num_destinataire() );
            tableau.addCell( new Paragraph( new Chunk( "Code postal", corpsTable ) ) );
            tableau.addCell( commande.getAdresse_code_destinataire() );
            tableau.addCell( new Paragraph( new Chunk( "Localité", corpsTable ) ) );
            tableau.addCell( commande.getAdresse_loc_destinataire() );
            tableau.addCell( new Paragraph( new Chunk( "(Téléphone)", corpsTable ) ) );
            tableau.addCell( commande.getTel_destinataire() );

            document.add( tableau );
            if ( commande.getTypeAssurance() == null ) {
                paragraph = new Paragraph( new Chunk( "Type d'assurance : Non référencée", corpsTable ) );
            } else {
                if ( commande.getTypeAssurance().equals( "forfait" ) ) {
                    paragraph = new Paragraph( new Chunk( "Type d'assurance : Assurance au forfait", corpsTable ) );
                } else if ( commande.getTypeAssurance().equals( "montant" ) ) {
                    paragraph = new Paragraph( new Chunk( "Type d'assurance : Assurance au montant", corpsTable ) );
                } else {
                    paragraph = new Paragraph( new Chunk( "Type d'assurance : Aucune assurance", corpsTable ) );
                }
            }
            document.add( paragraph );

            tableau = new PdfPTable( colsWidth );
            tableau.setWidthPercentage( 100 );

            tableau.setSpacingBefore( 25 );
            tableau.setSpacingAfter( 25 );
            cell = new PdfPCell( new Paragraph( new Chunk( "Réservé à HLB-Express", titreTable ) ) );
            cell.setColspan( 2 );
            cell.setHorizontalAlignment( Element.ALIGN_CENTER );

            tableau.addCell( cell );
            tableau.addCell( new Paragraph( new Chunk( "Poids (en kg)", corpsTable ) ) );
            tableau.addCell( "" + commande.getPoids() );
            tableau.addCell( new Paragraph( new Chunk( "Dimension (H x L x l en cm)", corpsTable ) ) );
            tableau.addCell( commande.getDimension_hauteur() + " x " + commande.getDimension_longueur() + " x "
                    + commande.getDimension_largeur() );
            if ( commande.getAccuseReception() ) {
                tableau.addCell( new Paragraph( new Chunk( "Accusé de réception", corpsTable ) ) );
                tableau.addCell( "Oui" );
            } else {
                tableau.addCell( new Paragraph( new Chunk( "Accusé de réception", corpsTable ) ) );
                tableau.addCell( "Non" );
            }

            document.add( tableau );

            paragraph = new Paragraph( new Chunk(
                    "\n\n Ce formulaire doit être imprimé et collé sur le colis avant l'arrivée du coursier.",
                    corpsTable ) );
            paragraph.setIndentationLeft( 20f );
            document.add( paragraph );

        } catch ( DocumentException de ) {
            de.printStackTrace();
        } catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
        /* Fermeture du document */
        document.close();
    }
}
