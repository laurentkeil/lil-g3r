package com.mdl.servlets;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.InscriptionForm;
import com.mdl.mail.Email;

/**
 * Servlet permettant le contrôle des informations utiles pour l'inscription
 * d'un client
 * 
 * 
 * 
 */
public class InscriptionClient extends HttpServlet {
    // Attributs utiles pour la page JSP
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_FORM            = "form";
    public static final String ATT_LISTEPAYS       = "listePays";
    // Constantes de redirection
    public static final String VUE_ARRIVEE         = "/WEB-INF/pagesJSP/inscriptionClient.jsp";
    public static final String VUE_SORTIE          = "/WEB-INF/pagesJSP/inscriptionClient.jsp";
    public static final String VUE_SUCCES          = "/WEB-INF/pagesJSP/succesInscription.jsp";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    public static final String ACCES_CONNECTE      = "/AccueilClient";
    public static final String ACCES_SUCCES        = "/ConnexionClient";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";

    private UtilisateurDAO     utilisateurDao;

    /**
     * Initialisation du DAO de l'utilisateur pour l'interaction avec la base de
     * données
     * 
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    /**
     * Méthode permettant la redirection vers la page JSP pour l'inscription du
     * client
     * 
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            // Si un employé est connecté, il est redirigé vers sa page
            // d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );

            // Si aucun utilisateur n'est connecté, il est redirigé vers la page
            // d'inscription
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        } else {
            // Sinon, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
    }

    /**
     * Méthode permettant le contrôle des informations utiles pour l'inscription
     * d'un client
     * 
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        request.setCharacterEncoding( "UTF-8" );
        InscriptionForm form = new InscriptionForm( utilisateurDao );

        Utilisateur utilisateur = form.inscrireUtilisateur( request );

        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        if ( form.getErreurs().isEmpty() ) {
            Email mail = new Email();
            try {
                // Envoi d'un e-mail de confirmation d'inscription
                request.setAttribute( "form_result", form.getResultat() );
                mail.envoiMail( utilisateur );
                this.getServletContext().getRequestDispatcher( VUE_SUCCES ).forward( request, response );
            } catch ( Exception e ) {
            }
        } else {
            /*
             * Construit l'algo de routage avec le WS
             */
            RoutingCommande routing = new RoutingCommande();

            /*
             * Récupère la liste des pays d'Europe du WS
             */
            List<String> listePays = routing.getListPays();
            request.setAttribute( ATT_LISTEPAYS, listePays );

            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        }

    }

}