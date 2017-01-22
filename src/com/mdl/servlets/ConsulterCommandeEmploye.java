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

import com.mdl.beans.Commande;
import com.mdl.beans.Statut_client;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.forms.RechercheClientForm;
import com.mdl.forms.RechercheCommandeForm;
import com.mdl.parser.Convertion;

/**
 * Servlet permettant de contrôler les informations utiles pour lister les commandes et les détails 
 * d'une commande
 * 
 *
 */
public class ConsulterCommandeEmploye extends HttpServlet {
	// Constantes de redirection
    public static final String VUE              	= "/WEB-INF/pagesJSP/employe/listerCommande.jsp";
    public static final String VUE_DETAIL       	= "/WEB-INF/pagesJSP/employe/consulterCommandeClient.jsp";
    public static final String ACCES_CONNEXION  	= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY = "daofactory";
    // Attributs utiles pour les pages JSP
    public static final String ATT_LIST_COMMANDE  = "commandeList";
    public static final String ATT_LIST_DETAIL    = "listDetail";
    public static final String ATT_COMMANDE       = "commande";
    public static final String ATT_USER           = "utilisateur";
    public static final String ATT_MAIL_CLIENT    = "mailClient";
    public static final String ATT_MAIL		      = "mail";
    // Attributs de session
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    private CommandeDAO        commandeDao;

    /**
     * Initialisation du DAO de la commande pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * Méthode permettant de redirigé vers la page JSP
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		// Si l'utilisateur est déjà connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
        	// Si l'employé n'est pas connecté, il est redirigé vers la page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else {
        	List<Commande> commandeList = new ArrayList<Commande>();
        	if (request.getParameter("mail").equals("all")){
        		// Cas où on liste toutes les commandes
        		 commandeList = commandeDao.listerAll();
            }else if (request.getParameter("mail").equals("Bel")){
            	// Cas où on liste les commandes du territoire belge
            	commandeList = commandeDao.listerBel();
            }else if (request.getParameter("mail").equals("Ent")){
            	// Cas où on liste les commandes entrantes sur le territoire belge
            	commandeList = commandeDao.listerEnt();
            }else if (request.getParameter("mail").equals("Sor")){
            	// Cas où on liste les commandes sortantes du territoire belge
            	commandeList = commandeDao.listerSor();
            } else {
            	String mail = request.getParameter("mail");
            	// Cas où on liste les commandes d'un client
            	commandeList = commandeDao.lister(mail);
            }
        	  String cat = request.getParameter("mail");
        	  request.setAttribute( ATT_MAIL, cat );
        	  request.setAttribute( ATT_LIST_COMMANDE, commandeList );
              
              this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }
    
    /**
     * Méthode permettant de contrôler les informations issues du formulaire
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	RechercheCommandeForm form = new RechercheCommandeForm(commandeDao);
            List<Commande> commandeList = new ArrayList<Commande>();
            Commande commande = new Commande();
            commandeList = form.rechercheCommande( request );
            String cat = form.getValeurChamp(request, ATT_MAIL);
      	  	request.setAttribute( ATT_MAIL, cat );
            request.setAttribute( ATT_LIST_COMMANDE, commandeList );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        		
        }
    }

}
