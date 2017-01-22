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
import com.mdl.forms.RechercheLitigeForm;
import com.mdl.forms.RechercheLitigesCoursier;
import com.mdl.forms.ReponseLitigeClientForm;
import com.mdl.parser.Convertion;

/**
 * Servlet permettant de contrôles les données utiles pour la suppression d'un problème signalé par un coursier
 * 
 *
 */
public class SupprimerProblCoursier extends HttpServlet {
	// Constantes de redirection
	public static final String VUE              	= "/ConsulterLitigeCoursier?nouveau=0";
    public static final String VUE_DETAIL       	= "/WEB-INF/pagesJSP/employe/creerLitigeCoursier.jsp";
    public static final String VUE_TRAITEMENT   	= "/WEB-INF/pagesJSP/employe/litigeEnCoursTraitement.jsp";
    public static final String VUE_ERREUR       	= "/WEB-INF/pagesJSP/erreur.jsp";
    public static final String ACCES_CONNEXION  	= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER  = "/AccueilClient";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    public static final String  ATT_SESSION_ID 		= "sessionIdEmpl";
    // Attributs utiles pour la page JSP
    public static final String ATT_LIST_LITIGE  	= "litigeList";
    public static final String ATT_LIST_DETAIL 		= "listDetail";
    public static final String ATT_LITIGE       	= "litige";
    public static final String ATT_NEW  			= "nouveau";
    public static final String ATT_CREATION      	= "creation";
    public static final String ATT_USER         	= "utilisateur";
    public static final String ATT_NUMLITIGE      	= "numLitige";
    public static final String ATT_ID         		= "idEmpl";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    
    private LitigeDAO        litigeDao;

    /**
     * Initialisation de la DAO des litiges pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
    }

    /**
     * Méthode permettant de contrôler les informations issues de la suppression d'un problème signalé par un coursier
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
        HttpSession session = request.getSession();
        // Contrôle des sessions
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	// Suppresion du problème signalé par le coursier
        	String numLitigeCoursier = request.getParameter("numLitigeCoursier");
        	RechercheLitigesCoursier form = new RechercheLitigesCoursier(litigeDao);
            form.fermerProblCoursier( request, numLitigeCoursier );
            response.sendRedirect( request.getContextPath() + VUE );
        	
        }

    }
}
