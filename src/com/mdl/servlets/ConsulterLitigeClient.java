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

import com.mdl.beans.Litige;
import com.mdl.dao.LitigeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.parser.Convertion;

/**
 * Servlet permettant de contrôler les informations utiles à la consultation des litiges des clients
 * 
 *
 */
public class ConsulterLitigeClient extends HttpServlet {
	// Constante de redirection
    public static final String VUE              	  = "/WEB-INF/pagesJSP/client/listerLitige.jsp";
    public static final String VUE_DETAIL       	  = "/WEB-INF/pagesJSP/client/consulterLitigeClient.jsp";
    public static final String ACCES_CONNEXION  	  = "/ConnexionClient";
    public static final String ACCES_CONNECTE_EMPL    = "/AccueilEmploye";
    // attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    // Attributs utiles pour les pages JSP
    public static final String ATT_LIST_LITIGE  = "litigeList";
    public static final String ATT_LIST_DETAIL  = "listDetail";
    public static final String ATT_LITIGE       = "litige";
    public static final String ATT_CREATION      = "creation";
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_NUMLITIGE      = "numLitige";
    // Constante de la DAO Factory
    public static final String CONF_DAO_FACTORY = "daofactory";

    private LitigeDAO        litigeDao;

    /**
     * Initialisation de la DAO des litiges permettant l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
    }

    /**
     * Méthode pour la redirection vers la page JSP
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        
    	if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
    		// Si l'employé est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        }else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	// Si l'utilisateur n'est pas connecté, il est redirigé vers la page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String email = (String) session.getAttribute( ATT_SESSION_USER );
            // Liste des litiges du client
            List<Litige> litigeList = litigeDao.lister( email );
            request.setAttribute( ATT_LIST_LITIGE, litigeList );
            
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * Méthode permettant de contrôler les données issues du formulaire, pour afficher les détails
     * du litige
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String numLitige = request.getParameter( ATT_NUMLITIGE );
            Litige litige = litigeDao.trouver( numLitige );
            
            // Permet de savoir si la dernière description est une réponse ou non pour savoir
            // si on doit afficher un textarea
            List<Litige> listDetail = new ArrayList<Litige>();
            listDetail = litige.getDetail();
            Litige valeur = new Litige();
            Iterator i = listDetail.iterator();
            while(i.hasNext()){
              valeur = (Litige) i.next();
              litige.setReponse(valeur.isReponse());
            }
        	
            String creation = litige.getDateCreation();
            
            request.setAttribute( ATT_LITIGE, litige );
            request.setAttribute( ATT_CREATION, creation );
            this.getServletContext().getRequestDispatcher( VUE_DETAIL ).forward( request, response );
        }

    }

}
