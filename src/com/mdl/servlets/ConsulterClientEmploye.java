package com.mdl.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Statut_client;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.RechercheClientForm;

/**
 * Servlet contrôlant les informations utiles pour le listage des clients et
 * pour la consultation du profil d'un certain client
 * 
 * 
 * 
 */
public class ConsulterClientEmploye extends HttpServlet {
    // Constante de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/employe/listerClient.jsp";
    public static final String VUE_DETAIL          = "/WEB-INF/pagesJSP/employe/consulterProfilClient.jsp";
    public static final String ACCES_CONNEXION     = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    // Constante du DAO Factory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attributs utiles pour les pages JSP
    public static final String ATT_LIST_CLIENT     = "clientList";
    public static final String ATT_LIST_DETAIL     = "listDetail";
    public static final String ATT_CLIENT          = "client";
    public static final String ATT_STATUT          = "statut";
    public static final String ATT_SUR             = "surfacturation";
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_MAIL_CLIENT     = "mailClient";
    
    private static final String FICHIER_PROPERTIES     = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_AVANCE_MIN          = "avance_min";
    private static final String PROPERTY_PREMIUM_MIN         = "premium_min";
    private static final String PROPERTY_SURFACT_AVANCE 	 = "surfact_avance";
    private static final String PROPERTY_SURFACT_PREMIUM     = "surfact_premium";
    private static final String PROPERTY_LITIGE_TORT_AVANCE  = "litige_tort_avance";
    private static final String PROPERTY_LITIGE_TORT_PREMIUM = "litige_tort_premium";
    private static final String PROPERTY_NB_LITIGE_TORT      = "nb_litige_tort";

    private UtilisateurDAO     utilisateurDao;
    private StatutDAO          statutDao;
    private SurfacturationDAO  surfacturationDao;
    private CommandeDAO        commandeDao;

    /**
     * Initialisation des DAO de l'utilisateur, du statut et de la
     * surfacturation pour l'interaction avec la base de données.
     * 
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * Méthode redirigeant vers la page souhaitée
     * 
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

    	
    	
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );

        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            // si l'utilisateur est connecté, il est redirigé vers sa page
            // d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si l'employé n'est pas connecté, il est redirigé vers la page de
            // connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else {
        	// Permet d'aller rechercher les informations dans le fichier de configuration
       	 	Properties properties = new Properties();
            String CHAMP_AVANCE_MIN;
            String CHAMP_PREMIUM_MIN;
            String CHAMP_SURFACT_AVANCE;
            String CHAMP_SURFACT_PREMIUM;
            String CHAMP_LITIGE_TORT_AVANCE;
            String CHAMP_LITIGE_TORT_PREMIUM;
            String CHAMP_NB_LITIGE_TORT;

            double NB_LITIGE_TORT;
            double AVANCE_MIN;
            double PREMIUM_MIN;
            double SURFACT_AVANCE;
            double SURFACT_PREMIUM;
            double LITIGE_TORT_AVANCE;
            double LITIGE_TORT_PREMIUM;

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

            if ( fichierProperties == null ) {
                throw new ServletException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
            }

            try {
                properties.load( fichierProperties );
                CHAMP_AVANCE_MIN          = properties.getProperty( PROPERTY_AVANCE_MIN );
                CHAMP_PREMIUM_MIN         = properties.getProperty( PROPERTY_PREMIUM_MIN );
                CHAMP_SURFACT_AVANCE      = properties.getProperty( PROPERTY_SURFACT_AVANCE );
                CHAMP_SURFACT_PREMIUM     = properties.getProperty( PROPERTY_SURFACT_PREMIUM );
                CHAMP_LITIGE_TORT_AVANCE  = properties.getProperty( PROPERTY_LITIGE_TORT_AVANCE );
                CHAMP_LITIGE_TORT_PREMIUM = properties.getProperty( PROPERTY_LITIGE_TORT_PREMIUM );
                CHAMP_NB_LITIGE_TORT = properties.getProperty( PROPERTY_NB_LITIGE_TORT );

                NB_LITIGE_TORT = Double.parseDouble( CHAMP_NB_LITIGE_TORT );
                AVANCE_MIN 			= Double.parseDouble(CHAMP_AVANCE_MIN);
                PREMIUM_MIN 		= Double.parseDouble(CHAMP_PREMIUM_MIN);
                SURFACT_AVANCE		= Double.parseDouble(CHAMP_SURFACT_AVANCE);
                SURFACT_PREMIUM 	= Double.parseDouble(CHAMP_SURFACT_PREMIUM);
                LITIGE_TORT_AVANCE 	= Double.parseDouble(CHAMP_LITIGE_TORT_AVANCE);
                LITIGE_TORT_PREMIUM = Double.parseDouble(CHAMP_LITIGE_TORT_PREMIUM);
                
                if (SURFACT_AVANCE < 0)
                	SURFACT_AVANCE = 0;
                if (SURFACT_PREMIUM < 0)
                    SURFACT_PREMIUM = 0;
                if (LITIGE_TORT_AVANCE < 0)
                	LITIGE_TORT_AVANCE = 0;
                if (LITIGE_TORT_PREMIUM < 0)
                	LITIGE_TORT_PREMIUM = 0;
            } catch ( IOException e ) {
                throw new ServletException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
            }
            if ( request.getParameter( "mail" ).equals( "all" ) ) {
                /*
                 * Cas où l'employé consulte tous les clients Vérifie pour
                 * chaque client pour savoir si il faut le bloquer.
                 */
                List<Utilisateur> clientList = utilisateurDao.listerAllInscrit();
                List<Utilisateur> clientListTemp = clientList;
                Iterator i = clientListTemp.iterator();
                while ( i.hasNext() ) {
                    Utilisateur client = (Utilisateur) i.next();
                    int user_id = client.getId();
                    String email = client.getEmail();
                    /*
                     * acces en bd a la liste de surfacturation (payees impayées
                     * ou toutes suivant les informations provenant du client)
                     */
                    List<Surfacturation> surfact = null;
                    surfact = surfacturationDao.listerImpayees( client.getEmail() );
                    /*
                     * Itère sur la liste des surfacturations pour vérifier si
                     * il y en a des non-payées depuis + de 15 jours après leur
                     * notification. Si oui, blocage du compte.
                     */
                    Iterator j = surfact.iterator();
                    boolean continuer = true;
                    while ( j.hasNext() && continuer ) {
                        Surfacturation surf = (Surfacturation) j.next();
                        if ( surf.getJourRestant() == 0 && surf.getDate_paiement() == null ) {
                            continuer = false;
                            statutDao.bloquerCompte( surf.getId_client(),
                                    "surfacturation" );
                        }
                    }

                    Statut_client statut = statutDao.trouver( user_id ); // recherche
                                                                         // le
                    // statut du
                    // compte de
                    // l'utilisateur

                    // Gestion du statut seulement s'il n'est pas bloqué !
                    if ( !statut.getStatut().equals( "0" ) ) {
                        int nbSurfactImpayees = commandeDao.nbSurfactImpayees( email );
                        int chiffreAffaire = commandeDao.chiffreAffaire( email );
                        int nbLitigesTord = commandeDao.nbLitigesTort( email );

                        /*
                         * gestion du statut du compte
                         */
                        if ( chiffreAffaire <= PREMIUM_MIN && chiffreAffaire >= AVANCE_MIN && nbSurfactImpayees <= SURFACT_AVANCE
                                && nbLitigesTord <= LITIGE_TORT_AVANCE ) {
                            statutDao.setStatut( user_id, 2 ); // avancé
                        } else if ( chiffreAffaire > PREMIUM_MIN && nbSurfactImpayees <= SURFACT_PREMIUM && nbLitigesTord <= LITIGE_TORT_PREMIUM ) {
                            statutDao.setStatut( user_id, 3 ); // premium
                        } else {
                            statutDao.setStatut( user_id, 1 ); // normal
                        }
                    }
                }

                Utilisateur client = new Utilisateur();
                Iterator k = clientList.iterator(); // on crée un Iterator pour
                                                    // parcourir notre HashSet
                while ( k.hasNext() ) {
                    client = (Utilisateur) k.next();
                    Statut_client statut = statutDao.trouver( client.getId() );
                    // Retrouve le statut du client
                    client.setStatut( statut.getStatut() );
                }
                request.setAttribute( ATT_LIST_CLIENT, clientList );

                this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            } else {
                // cas où on affiche les détails du client
                String mail = request.getParameter( "mail" );
                Utilisateur client = utilisateurDao.trouver( mail );
                Statut_client statut = statutDao.trouver( client.getId() );
                Surfacturation surfacturation = surfacturationDao.trouver( mail );

                int user_id = client.getId();
                /*
                 * acces en bd a la liste de surfacturation (payees impayées ou
                 * toutes suivant les informations provenant du client)
                 */
                List<Surfacturation> surfact = null;
                surfact = surfacturationDao.listerImpayees( client.getEmail() );
                /*
                 * Itère sur la liste des surfacturations pour vérifier si il y
                 * en a des non-payées depuis + de 15 jours après leur
                 * notification. Si oui, blocage du compte.
                 */
                Iterator j = surfact.iterator();
                boolean continuer = true;
                while ( j.hasNext() && continuer ) {
                    Surfacturation surf = (Surfacturation) j.next();
                    if ( surf.getJourRestant() == 0 && surf.getDate_paiement() == null ) {
                        continuer = false;
                        statutDao.bloquerCompte( surf.getId_client(),
                                "surfacturation" );
                    }
                }

                // Gestion du statut seulement s'il n'est pas bloqué !
                if ( !statut.getStatut().equals( "0" ) ) {
                    int nbSurfactImpayees = commandeDao.nbSurfactImpayees( mail );
                    int chiffreAffaire = commandeDao.chiffreAffaire( mail );
                    int nbLitigesTord = commandeDao.nbLitigesTort( mail );

                    /*
                     * gestion du statut du compte
                     */
                    if ( chiffreAffaire <= PREMIUM_MIN && chiffreAffaire >= AVANCE_MIN && nbSurfactImpayees <= SURFACT_AVANCE
                            && nbLitigesTord <= LITIGE_TORT_AVANCE ) {
                        statutDao.setStatut( user_id, 2 ); // avancé
                    } else if ( chiffreAffaire > PREMIUM_MIN && nbSurfactImpayees <= SURFACT_PREMIUM && nbLitigesTord <= LITIGE_TORT_PREMIUM ) {
                        statutDao.setStatut( user_id, 3 ); // premium
                    } else {
                        statutDao.setStatut( user_id, 1 ); // normal
                    }
                }
                // Mettre à jour le statut du client pour la page JSP
                statut = statutDao.trouver( client.getId() );

                Date date = client.getDateNaissance();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime( date );
                int annee = calendar.get( Calendar.YEAR );
                int mois = calendar.get( Calendar.MONTH ) + 1;
                int jour = calendar.get( Calendar.DAY_OF_MONTH );
                String anneeNaissance = Integer.toString( annee );
                String moisNaissance = Integer.toString( mois );
                String jourNaissance = Integer.toString( jour );
                client.setAnneeNaissance( anneeNaissance );
                client.setMoisNaissance( moisNaissance );
                client.setJourNaissance( jourNaissance );
                request.setAttribute( ATT_CLIENT, client );
                request.setAttribute( ATT_STATUT, statut );
                request.setAttribute( ATT_SUR, surfacturation );
                this.getServletContext().getRequestDispatcher( VUE_DETAIL ).forward( request, response );
            }

        }

    }

    /**
     * Méthode permettant de retrouver les informations en fonction de certains
     * critères.
     * 
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            RechercheClientForm form = new RechercheClientForm( utilisateurDao );
            List<Utilisateur> clientList = new ArrayList<Utilisateur>();
            Utilisateur client = new Utilisateur();
            clientList = form.rechercheClient( request );

            Iterator k = clientList.iterator(); // on crée un Iterator pour
                                                // parcourir notre HashSet
            while ( k.hasNext() ) {

                client = (Utilisateur) k.next();

                System.out.println( client.getId() );
                Statut_client statut = statutDao.trouver( client.getId() );
                client.setStatut( statut.getStatut() );
            }
            request.setAttribute( ATT_LIST_CLIENT, clientList );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );

        }
    }

}
