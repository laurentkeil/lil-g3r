package com.mdl.servlets;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.EditerClientForm;

/**
 * Servlet permettant de contrôler les informations utiles pour l'édition du
 * profil du client
 * 
 * 
 * 
 */
public class EditerClient extends HttpServlet {
    // Constantes de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/client/editerClient.jsp";
    public static final String VUE_PROFIL          = "/ProfilClient";
    public static final String ATT_LISTEPAYS       = "listePays";
    public static final String ACCES_CONNEXION     = "/ConnexionClient";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    // Attributs de sessions
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs utiles pour la page JSP
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_FORM            = "form";

    private UtilisateurDAO     utilisateurDao;

    /**
     * Initialisation de la DAO de l'utilisateur pour l'interaction avec la base
     * de données
     * 
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    /**
     * Méthode permettant de rediriger vers la page pour que le client puisse
     * éditer son profil
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
            // Si aucun utilisation n'est connecté, il est redirigé vers sa page
            // de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
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

            // Recherche de ses anciennes informations
            String email = (String) session.getAttribute( ATT_SESSION_USER );

            Utilisateur utilisateur = utilisateurDao.trouver( email );
            Date date = utilisateur.getDateNaissance();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime( date );
            int annee = calendar.get( Calendar.YEAR );
            int mois = calendar.get( Calendar.MONTH ) + 1;
            int jour = calendar.get( Calendar.DAY_OF_MONTH );
            String anneeNaissance = Integer.toString( annee );
            String moisNaissance = Integer.toString( mois );
            String jourNaissance = Integer.toString( jour );
            utilisateur.setAnneeNaissance( anneeNaissance );
            utilisateur.setMoisNaissance( moisNaissance );
            utilisateur.setJourNaissance( jourNaissance );
            request.setAttribute( ATT_USER, utilisateur );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * Méthode permettant de contrôler les informations issues du formulaire
     * d'édition de profil du client
     * 
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        request.setCharacterEncoding( "UTF-8" );
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute( ATT_SESSION_USER );
        Utilisateur utilisateur = utilisateurDao.trouver( email );
        EditerClientForm form = new EditerClientForm( utilisateurDao );

        utilisateur = form.editerClient( request, utilisateur );
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, utilisateur );

        if ( form.getErreurs().isEmpty() ) {
            response.sendRedirect( request.getContextPath() + VUE_PROFIL );
        } else {
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
        //
    }

}
