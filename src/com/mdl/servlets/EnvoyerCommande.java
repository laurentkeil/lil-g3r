package com.mdl.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Statut_client;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.parser.GenerateICU;
import com.mdl.pdf.EnvoyerFacture;
import com.mdl.pdf.EnvoyerFormulaire;

/**
 * Servlet d'affichage d'un message de confirmation et mise en place dans la bd
 * et envoi d'un mail avec le bordereau d'envoi
 * 
 */
public class EnvoyerCommande extends HttpServlet {
    /**
     * déclaration des constantes
     */
    public static final String ATT_COMMANDE         = "commande";
    public static final String ATT_FORM             = "form";
    public static final String ATT_SESSION_USER     = "sessionUtilisateur";
    public static final String ATT_SESSION_COMMANDE = "sessionCommande";
    public static final String CONF_DAO_FACTORY     = "daofactory";
    public static final String ACCES_CONNEXION      = "/ConnexionClient";
    public static final String ATT_SESSION_EMPL     = "sessionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";

    private static final String FICHIER_PROPERTIES  = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_COLIS_COM  = "nb_colis_com";
    
    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String VUE_SUCCES           = "/WEB-INF/pagesJSP/client/envoiFormulaireClient.jsp";

    private UtilisateurDAO     utilisateurDao;
    private CommandeDAO        commandeDao;
    private StatutDAO          statutDao;

    /**
     * initialisation de la commande et du client et ses informations en base de
     * donnée
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
        /* Récupération d'une instance de notre DAO statut */
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getStatutDao();
    }

    /**
     * affichage d'un message de confirmation et création en bd de la commande
     * et email avec bordereau d envoi
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connectés.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	
        	Properties properties = new Properties();
            String CHAMP_COLIS_COM;
            double COLIS_COM;
            // Nombre de commandes passées pour savoir si celui-ci est gratuit ou non

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

            if ( fichierProperties == null ) {
                throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
            }

            try {
                properties.load( fichierProperties );
                CHAMP_COLIS_COM = properties.getProperty( PROPERTY_COLIS_COM );
                COLIS_COM = Double.parseDouble(CHAMP_COLIS_COM);
                
            } catch ( IOException e ) {
                throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
            }
        	
            /**
             * récupération en session de la commande
             */
            Commande commande = (Commande) session.getAttribute( ATT_SESSION_COMMANDE );

            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            Statut_client statut = statutDao.trouver( utilisateur.getId() );
            int nbComPassee = 0;
            if ( statut.getStatut().equals( "2" ) ) { // avancé
                /*
                 * envoi gratuit tous les 12 envois pour le compte avancé.
                 * recherche l'id du dernier colis rendu gratuit, return 0 si il
                 * n'existe pas
                 */
                int idLastColisFree = commandeDao.getIdLastColis( mail );
                if ( idLastColisFree == 0 ) {
                    nbComPassee = commandeDao.nbComForFree( mail );
                } else {
                    nbComPassee = commandeDao.nbComForFree( mail, idLastColisFree );
                }
            }

            /**
             * Date d'enregistrement de la commande
             */
            Date auj = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss" );
            String date = sdf.format( auj )+"";
            commande.setDate_enregistrement( date );

            /**
             * Génération d'un icu et vérification que c'est icu n'existe pas
             * déjà (robustesse)
             */
            String icu;
            do {
                GenerateICU gen = new GenerateICU();
                icu = gen.ICUGenColis();
            } while ( commandeDao.verifICU( icu ) );
            commande.setIcu( icu );

            /**
             * création de la commande en base de donnée lors de la confirmation
             * de la commande
             */
            int idCom = commandeDao.creer( commande, utilisateur.getId() );
            if ( nbComPassee >= (COLIS_COM - 1) ) { 
            	// place l'id de la commande pour
                // signaler que celle-ci est
                // gratuite.
                statutDao.setIdFree( commandeDao.getIdColis( idCom ), utilisateur.getId() );
            }
            session.removeAttribute( ATT_SESSION_COMMANDE );
            /**
             * Envoie du formulaire pdf (bordereau d'envoi) par mail
             */
            EnvoyerFormulaire pdf = new EnvoyerFormulaire();
            try {
                pdf.EmailFormulaire( mail, commande );
            } catch ( Exception e ) {
            }
            /**
             * Envoie du formulaire pdf (facture) par mail
             */
            EnvoyerFacture pdf2 = new EnvoyerFacture();
            try {
                pdf2.EmailFacture( mail, commande, utilisateur );
            } catch ( Exception e ) {
            }
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        }

    }

}