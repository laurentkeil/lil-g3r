package com.mdl.servlets;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.parser.GenerateICU;
import com.mdl.pdf.EnvoyerFacture;
import com.mdl.pdf.EnvoyerFormulaire;

/**
 * Servlet de conf confirmation de commande. Affichage des informations encodés
 * par l'utilisateur lors de sa création de commande et récupération des
 * informations entrées par l'utilisateur.
 * 
 */
public class ConfirmerCommande extends HttpServlet {
    /**
     * déclaration des constantes
     */
    public static final String  ATT_CONFIRM                = "submit";
    public static final String  ATT_COMMANDE               = "commande";
    public static final String  ATT_FORM                   = "form";
    public static final String  ATT_SESSION_USER           = "sessionUtilisateur";
    public static final String  ATT_SESSION_COMMANDE       = "sessionCommande";
    public static final String  CONF_DAO_FACTORY           = "daofactory";
    public static final String  ACCES_CONNEXION            = "/ConnexionClient";
    public static final String  ATT_SESSION_EMPL           = "sessionEmploye";
    public static final String  ACCES_CONNECTE_EMPL        = "/AccueilEmploye";

    public static final String  ATT_CARNET                 = "carnet_adresses";
    public static final String  ATT_TAILLE                 = "taille";
    public static final String  ATT_NOM                    = "nom";
    public static final String  ATT_PRENOM                 = "prenom";
    public static final String  ATT_RUE                    = "rue";
    public static final String  ATT_NUM                    = "num";
    public static final String  ATT_BOITE                  = "boite";
    public static final String  ATT_CODE                   = "code";
    public static final String  ATT_LOC                    = "loc";
    public static final String  ATT_PAYS                   = "pays";
    public static final String  ATT_TEL                    = "tel";
    public static final String  ATT_LISTEPAYS              = "listePays";

    private static final String FICHIER_PROPERTIES         = "/com/mdl/config/configuration.properties";

    /*
     * constantes pour les attributs de requetes correspondant à l'utilisation
     * du javascript
     */
    public static final String  ATT_PRIX_BASE_BELGIQUE_BAS = "prix_base_bel_bas";
    public static final String  ATT_PRIX_BASE_INT_HAUT     = "prix_base_int_haut";
    private static final String ATT_PRIX_BASE_AUTRE        = "prix_base_autre";
    public static final String  ATT_PRIX_FORFAIT           = "prix_forfait";
    public static final String  ATT_PRIX_MONTANT_BAS       = "prix_montant_bas";
    public static final String  ATT_PCT_MONTANT_BAS        = "pct_montant_bas";
    public static final String  ATT_PCT_MONTANT_HAUT       = "pct_montant_haut";
    public static final String  ATT_POIDS_BASE             = "poids_base";
    private static final String ATT_PRIX_ACC               = "prix_acc";

    private static final String PROPERTY_PRIX_BASE_AUTRE   = "prix_autre";
    private static final String PROPERTY_PRIX_BEL_BAS      = "prix_bel_bas";
    private static final String PROPERTY_PRIX_INT_HAUT     = "prix_int_haut";
    private static final String PROPERTY_SURPLUS_BEL       = "surplus_bel";
    private static final String PROPERTY_SURPLUS_INT       = "surplus_int";
    private static final String PROPERTY_PRIX_FORFAIT      = "prix_forfait";
    private static final String PROPERTY_PRIX_MONTANT_BAS  = "prix_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_BAS   = "pct_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_HAUT  = "pct_montant_haut";
    private static final String PROPERTY_POIDS_BASE        = "poids_base";
    private static final String PROPERTY_PRIX_ACC          = "prix_acc";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String  VUE_RETOUR                 = "/WEB-INF/pagesJSP/client/passageCommande.jsp";
    public static final String  VUE_SUCCES                 = "/WEB-INF/pagesJSP/client/envoiFormulaireClient.jsp";

    private UtilisateurDAO      utilisateurDao;
    private CommandeDAO         commandeDao;

    /**
     * initialisation de la commande et du client et ses informations en base de
     * données
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * affichage de la fiche récapitulative des informations de la commande crée
     * précédement, de la page jsp
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
            /* A la réception d'une requête GET, simple affichage du formulaire */
            this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
        }

    }

    /**
     * affichage de la fiche récapitulative des informations de la commande crée
     * précédement et récupération des informations de l'intéraction de
     * l'utilisateur sur la page, redirection sur les pages correspondantes,
     * création de la commande en base de donnée et envoi du formulaire par mail
     * si confirmation.
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        /**
         * récupération en session de la commande
         */
        Commande commande = (Commande) session.getAttribute( ATT_SESSION_COMMANDE );
        String mail = (String) session.getAttribute( ATT_SESSION_USER );

        if ( request.getParameter( ATT_CONFIRM ) != null ) {
            Utilisateur utilisateur = utilisateurDao.trouver( mail );
            /**
             * Date d'enregistrement de la commande
             */
            Date auj = new Date( System.currentTimeMillis() );
            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss" );
            String date = sdf.format( auj ) + "";
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
            commandeDao.creer( commande, utilisateur.getId() );
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
        } else {
            // Permet d'aller rechercher les informations dans le fichier de
            // configuration
            Properties properties = new Properties();
            request.setAttribute( ATT_COMMANDE, commande );

            String CHAMP_PRIX_BASE_BELGIQUE_BAS;
            String CHAMP_PRIX_BASE_INT_HAUT;
            String CHAMP_PRIX_AUTRE;
            String CHAMP_PRIX_FORFAIT;
            String CHAMP_PRIX_MONTANT_BAS;
            String CHAMP_PCT_MONTANT_BAS;
            String CHAMP_PCT_MONTANT_HAUT;
            String CHAMP_POIDS_BASE;
            String CHAMP_PRIX_ACC;

            double PRIX_BASE_BELGIQUE_BAS;
            double PRIX_BASE_INT_HAUT;
            double PRIX_BASE_AUTRE;
            double PRIX_FORFAIT;
            double PRIX_MONTANT_BAS;
            double PCT_MONTANT_BAS;
            double PCT_MONTANT_HAUT;
            double POIDS_BASE;
            double PRIX_ACC;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

            if ( fichierProperties == null ) {
                throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
            }

            try {
                properties.load( fichierProperties );
                CHAMP_PRIX_BASE_BELGIQUE_BAS = properties.getProperty( PROPERTY_PRIX_BEL_BAS );
                CHAMP_PRIX_BASE_INT_HAUT = properties.getProperty( PROPERTY_PRIX_INT_HAUT );
                CHAMP_PRIX_AUTRE = properties.getProperty( PROPERTY_PRIX_BASE_AUTRE );
                CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
                CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
                CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
                CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
                CHAMP_POIDS_BASE = properties.getProperty( PROPERTY_POIDS_BASE );
                CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
                PRIX_BASE_BELGIQUE_BAS = Double.parseDouble( CHAMP_PRIX_BASE_BELGIQUE_BAS );
                PRIX_BASE_INT_HAUT = Double.parseDouble( CHAMP_PRIX_BASE_INT_HAUT );
                PRIX_BASE_AUTRE = Double.parseDouble( CHAMP_PRIX_AUTRE );
                PRIX_FORFAIT = Double.parseDouble( CHAMP_PRIX_FORFAIT );
                PRIX_MONTANT_BAS = Double.parseDouble( CHAMP_PRIX_MONTANT_BAS );
                PCT_MONTANT_BAS = Double.parseDouble( CHAMP_PCT_MONTANT_BAS );
                PCT_MONTANT_HAUT = Double.parseDouble( CHAMP_PCT_MONTANT_HAUT );
                POIDS_BASE = Double.parseDouble( CHAMP_POIDS_BASE );
                PRIX_ACC = Double.parseDouble( CHAMP_PRIX_ACC );

                /*
                 * Placement en attribut de la requete des propriétés de
                 * configuration pour les utiliser en javascript
                 */
                request.setAttribute( ATT_PRIX_BASE_BELGIQUE_BAS, PRIX_BASE_BELGIQUE_BAS );
                request.setAttribute( ATT_PRIX_BASE_INT_HAUT, PRIX_BASE_INT_HAUT );
                request.setAttribute( ATT_PRIX_BASE_AUTRE, PRIX_BASE_AUTRE );
                request.setAttribute( ATT_PRIX_FORFAIT, PRIX_FORFAIT );
                request.setAttribute( ATT_PRIX_MONTANT_BAS, PRIX_MONTANT_BAS );
                request.setAttribute( ATT_PCT_MONTANT_BAS, PCT_MONTANT_BAS );
                request.setAttribute( ATT_PCT_MONTANT_HAUT, PCT_MONTANT_HAUT );
                request.setAttribute( ATT_POIDS_BASE, POIDS_BASE );
                request.setAttribute( ATT_PRIX_ACC, PRIX_ACC );
            } catch ( IOException e ) {
                throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
            }

            /*
             * récupération du carnet d'adresse en BD
             */
            List<List<String>> carnet_adresse = commandeDao.listerCarnet( mail );

            /*
             * Création des listes des champs d'une adresse du carnet d'adresses
             * afin de les récupérer dans la jsp
             */
            List<String> nom = new ArrayList<String>();
            List<String> prenom = new ArrayList<String>();
            List<String> rue = new ArrayList<String>();
            List<String> num = new ArrayList<String>();
            List<String> boite = new ArrayList<String>();
            List<String> code = new ArrayList<String>();
            List<String> loc = new ArrayList<String>();
            List<String> pays = new ArrayList<String>();
            List<String> tel = new ArrayList<String>();
            /*
             * itération sur le carnet d'adresses et mise en place des listes de
             * champs des adresses.
             */
            Iterator i = carnet_adresse.iterator();
            int taille = 0;
            while ( i.hasNext() ) {
                List<String> adresse = (List<String>) i.next();
                Iterator y = adresse.iterator();
                nom.add( (String) y.next() );
                prenom.add( (String) y.next() );
                rue.add( (String) y.next() );
                num.add( (String) y.next() );
                boite.add( (String) y.next() );
                code.add( (String) y.next() );
                loc.add( (String) y.next() );
                pays.add( (String) y.next() );
                tel.add( (String) y.next() );
                taille = taille + 1;
            }
            /*
             * mise en place des attributs dans la requête de la page
             */
            request.setAttribute( ATT_CARNET, carnet_adresse );
            request.setAttribute( ATT_NOM, nom );
            request.setAttribute( ATT_PRENOM, prenom );
            request.setAttribute( ATT_RUE, rue );
            request.setAttribute( ATT_NUM, num );
            request.setAttribute( ATT_BOITE, boite );
            request.setAttribute( ATT_CODE, code );
            request.setAttribute( ATT_LOC, loc );
            request.setAttribute( ATT_PAYS, pays );
            request.setAttribute( ATT_TEL, tel );
            request.setAttribute( ATT_TAILLE, taille );

            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );

            /**
             * Si l'utilisateur ne confirme pas sa commande et qu'il veut
             * retourner au formulaire de création, retour à l'affichage du
             * formulaire
             */
            this.getServletContext().getRequestDispatcher( VUE_RETOUR ).forward( request, response );
        }

    }
}