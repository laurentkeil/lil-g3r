package com.mdl.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Statut_client;
import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.forms.RechercheClientForm;
import com.mdl.parser.Convertion;

/**
 * Servlet permettant de contrôler les informations liées aux clients bloqués, de les lister et
 * de envoyer à la page JSP appropriée pour afficher les informations souhaitées
 * 
 *
 */
public class ClientsBloquesEmploye extends HttpServlet {
	// Constantes utilisées pour la redirection
    public static final String VUE                    = "/WEB-INF/pagesJSP/employe/listeClientsBloques.jsp";
    public static final String ACCES_CONNEXION  	  = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL    = "/AccueilEmploye";
    // constante de la DaoFactory
    public static final String CONF_DAO_FACTORY     = "daofactory";
    // constantes pour les attributs à envoyer à la page JSP
    public static final String ATT_LIST_CLIENT  	= "clientList";
    public static final String ATT_LIST_DETAIL  	= "listDetail";
    public static final String ATT_CLIENT       	= "client";
    public static final String ATT_LIST_STATUT  	= "statutList";
    public static final String ATT_SUR     	    	= "surfacturation";
    public static final String ATT_USER         	= "utilisateur";
    public static final String ATT_MAIL_CLIENT      = "mailClient";
    // attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    // Initialisation des DAO
    private UtilisateurDAO        utilisateurDao;
    private StatutDAO 			  statutDao;
    private SurfacturationDAO 	  surfacturationDao;

    
    /**
     * initialisation du DAO de l'utilisateur, du statut et de la sufacturation 
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getSurfacturationDao();
    }

    /**
     * Méthode permettant de d'appeler les informations liées aux clients ayant un compte bloqué
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
      
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		//Si un client est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
        	// Si l'employé n'est pas connecté, il est redirigé vers la page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else {
        		/*
        		 * Sinon, les recherches des informations sur les clients ayant un compte bloqué sont effectuées
        		 * Vérifie pour chaque client pour savoir si il faut le bloquer.
        		 */
        		List<Utilisateur> clientList = utilisateurDao.listerAll();
	   		 	Iterator i = clientList.iterator();
	            while ( i.hasNext()) {
	                Utilisateur client = (Utilisateur) i.next();
	                /*
	                 * acces en bd a la liste de surfacturation (payees impayées ou
	                 * toutes suivant les informations provenant du client)
	                 */
	                List<Surfacturation> surfact = null;
	                surfact = surfacturationDao.listerImpayees( client.getEmail() );
	                /*
	                 * Itère sur la liste des surfacturations pour vérifier si il y en a
	                 * des non-payées depuis + de 15 jours après leur notification. Si
	                 * oui, blocage du compte.
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
	            }
	            
        		 List<Statut_client> statutList = statutDao.trouverAllBloques();
                 Utilisateur client = new Utilisateur();
                 Statut_client statut = new Statut_client();
                 Surfacturation surfacturation = new Surfacturation();
                 Iterator k=statutList.iterator();
                 while(k.hasNext()){
                 	statut = (Statut_client) k.next();
                 	client = utilisateurDao.trouverAvecId(statut.getClient());
                 	statut.setMail(client.getEmail());
                 	statut.setNom(client.getNom());
                 	statut.setPrenom(client.getPrenom());
                 	if (statut.getCause().equals("surfacturation")){
                 		surfacturation = surfacturationDao.trouver(statut.getMail());
                 		statut.setICU(surfacturation.getICU());
                 	}
                 }
                 request.setAttribute(ATT_LIST_STATUT, statutList);
                 
                 this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            
           
        }

    }
    
    /**
     * Méthode permettant de rechercher les informations de clients bloqués en fonction d'un critère
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	RechercheClientForm form = new RechercheClientForm(utilisateurDao);
        	List<Statut_client> statutList = statutDao.trouverAllBloques();
            List<Utilisateur> clientList = new ArrayList<Utilisateur>();
            Utilisateur client = new Utilisateur();
            Statut_client statut = new Statut_client();
            Surfacturation surfacturation = new Surfacturation();
            Iterator k=statutList.iterator(); 
            clientList = form.rechercheClient( request );
            while(k.hasNext()){
            	statut = (Statut_client) k.next();
            	Iterator l=clientList.iterator(); 
            	boolean existe = false;
                while(l.hasNext() && !existe){
                	client = (Utilisateur) l.next();
                	String idClient = Integer.toString(client.getId());
                	if(statut.getClient().equals(idClient)){
                		statut.setMail(client.getEmail());
                    	statut.setNom(client.getNom());
                    	statut.setPrenom(client.getPrenom());
                    	if (statut.getCause().equals("surfacturation")){
                    		surfacturation = surfacturationDao.trouver(statut.getMail());
                    		statut.setICU(surfacturation.getICU());
                    	}
                    	existe = true;
                    	l.remove();
                	}
                	
                }
                if (!existe){
            		k.remove();
            	}
            }
            request.setAttribute( ATT_LIST_STATUT, statutList );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        		
        }
    }

}
