package com.mdl.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Tracking;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.TrackingDAO;
import com.mdl.forms.EspacePublicForm;

/**
 * Servlet contrôlant les informations utiles pour le tracking d'un colis à partir de son ICU
 * 
 *
 */
public class EspacePublic extends HttpServlet {
	// Constantes de redirection
    public static final String VUE_ARRIVEE         = "/WEB-INF/pagesJSP/espacePublic.jsp";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    public static final String ACCES_CONNECTE      = "/AccueilClient";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Attributs utiles pour la page JSP
    public static final String ATT_CO              = "connecte";
    public static final String ATT_RECHERCHE       = "recherche";
    public static final String ATT_TRACKING        = "trackingList";
    public static final String ATT_FORM            = "form";
    public static final String ATT_REF             = "reference";

    private TrackingDAO        trackingDao;

    /**
     * Initialisation du DAO du tracking pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.trackingDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTrackingDao();
    }

    /**
     * Méthode permettant la redirection vers la page JSP pour le tracking d'un colis
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
        HttpSession session = request.getSession();
        boolean connecte = false;
        // Permet de savoir si l'utilisateur est déjà connecté pour l'affichage du menu
        boolean recherche = false;
        // Permet de ne pas afficher le tableau issu de la recherche
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	// Si l'employé est connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
        	// Quand l'utilisateur est connecté
            connecte = true;
            request.setAttribute( ATT_CO, connecte );
            request.setAttribute( ATT_RECHERCHE, recherche );
            
        }
        this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );

    }

    /**
     * Méthode permettant de contrôler les informations issues du formulaire pour afficher et rechercher les données 
     * pour le tracking d'un colis
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EspacePublicForm form = new EspacePublicForm( trackingDao );
        List<Tracking> trackingList = new ArrayList<Tracking>();
        String reference = form.getValeurChamp( request, ATT_REF );
        trackingList = form.trackingColis( request );
        boolean connecte = false;
        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
            connecte = true;
        }
        request.setAttribute( ATT_CO, connecte );
        boolean recherche = true;
        request.setAttribute( ATT_RECHERCHE, recherche );
        request.setAttribute( ATT_REF, reference );
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_TRACKING, trackingList );
        if ( form.getErreurs().isEmpty() ) {
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        } else {
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        }
    }

}