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
 * Servlet permettant de contrôler les informations destinées à la consultation des problèmes signalés
 * par un coursier
 * 
 *
 */
public class ConsulterLitigeCoursier extends HttpServlet {
	//Constantes de redirection
	public static final String VUE              	 = "/WEB-INF/pagesJSP/employe/listerLitigesCoursier.jsp";
    public static final String VUE_DETAIL       	 = "/WEB-INF/pagesJSP/employe/creerLitigeCoursier.jsp";
    public static final String VUE_TRAITEMENT   	 = "/WEB-INF/pagesJSP/employe/litigeEnCoursTraitement.jsp";
    public static final String VUE_ERREUR      		 = "/WEB-INF/pagesJSP/erreur.jsp";
    public static final String ACCES_CONNEXION 		 = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER   = "/AccueilClient";
    // Attributs de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL = "sessionEmploye";
    public static final String ATT_SESSION_ID   = "sessionIdEmpl";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY = "daofactory";
    // Attributs utiles pour la page JSP
    public static final String ATT_LIST_LITIGE  = "litigeList";
    public static final String ATT_LIST_DETAIL  = "listDetail";
    public static final String ATT_LITIGE       = "litige";
    public static final String ATT_NEW  		= "nouveau";
    public static final String ATT_CREATION     = "creation";
    public static final String ATT_USER         = "utilisateur";
    public static final String ATT_NUMLITIGE    = "numLitige";
    public static final String ATT_ID           = "idEmpl";
    
    private LitigeDAO        litigeDao;

    /**
     * Initialisation du DAO des litiges pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
    }

    /**
     * Méthode permettant de rediriger vers la page JSP
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        HttpSession session = request.getSession();

    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		// Si l'utilisateur est connecté, il est redirigé vers sa page d'accueil
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si l'employé n'est pas connecté, il est redirigé vers sa page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	String nouveau = request.getParameter("nouveau");
        	if (nouveau.equals("0") || nouveau.equals("1")){
        		// Liste des problèmes signalés par le coursier
        		RechercheLitigesCoursier form = new RechercheLitigesCoursier(litigeDao);
            	List<Litige> litigeList = new ArrayList<Litige>();
            	litigeList = form.rechercheCoursier( request, nouveau );
            	int id = (Integer) session.getAttribute(ATT_SESSION_ID);
            	request.setAttribute( ATT_ID, id );	
                request.setAttribute( ATT_LIST_LITIGE, litigeList );
                request.setAttribute( ATT_NEW, nouveau );
                this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        	} else{
        		this.getServletContext().getRequestDispatcher( VUE_ERREUR ).forward( request, response );
        	}
        	
        }

    }

    /**
     * Méthode permettant de contrôler les informations issues du formulaire pour la consultation des détails 
     * d'un litige
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	RechercheLitigesCoursier form = new RechercheLitigesCoursier(litigeDao);
        	int id = (Integer) session.getAttribute(ATT_SESSION_ID);
        	Litige litige = form.creerLitigeCoursier( request, id );
        	if (litige == null){
        		this.getServletContext().getRequestDispatcher( VUE_TRAITEMENT ).forward( request, response );
        	}else{
        		request.setAttribute( ATT_LITIGE, litige );
    	        request.setAttribute( ATT_ID, id );
    	        this.getServletContext().getRequestDispatcher( VUE_DETAIL ).forward( request, response );
        	}
        	
        }

    }
}
