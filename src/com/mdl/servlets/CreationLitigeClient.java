package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Commande;
import com.mdl.beans.Litige;
import com.mdl.dao.CommandeDAO;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.LitigeDAO;
import com.mdl.forms.CreationLitigeClientForm;

/**
 * Servlet permettant de contrôler les informations relatives à la création des litiges par le client
 * 
 *
 */
public class CreationLitigeClient extends HttpServlet {
	// Attributs utiles pour les pages JSP
    public static final String ATT_LITIGE          = "litige";
    public static final String ATT_FORM            = "form";
    public static final String ATT_COM             = "colis";
    public static final String ATT_ICU             = "ICU";
    // Constantes de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/client/creationLitigeClient.jsp";
    public static final String VUE_CREE            = "/WEB-INF/pagesJSP/client/confirmationLitige.jsp";
    public static final String VUE_DEPART          = "/HistoriqueCommandes";
    public static final String ACCES_CONNEXION     = "/ConnexionClient";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    // Attributs de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";

    private LitigeDAO          litigeDao;
    private CommandeDAO 	   commandeDao;

    /**
     * Initialisation du DAO des litiges et de la commande pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.litigeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getLitigeDao();
        this.commandeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCommandeDao();
    }

    /**
     * Méthode permettant de rediriger vers la page JSP et de contrôler les informations des détails du litige
     * du client.
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
 
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
      
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
        	// Si l'employé est connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            // Si aucun utilisateur n'est connecté, il est redirigé vers sa page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
        	// Sinon, on récupère les informations utiles pour la création du litige
            String ICU = request.getParameter( "litige" );
            Commande commande = commandeDao.trouver(ICU);
            String colis = commande.getIdColis();
            request.setAttribute( ATT_ICU, ICU );
            request.setAttribute( ATT_COM, colis );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

    }

    /**
     * Méthode permettant de contrôler les informations issues du formulaire
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        request.setCharacterEncoding( "UTF-8" );
        CreationLitigeClientForm form = new CreationLitigeClientForm( litigeDao );
        
        Litige litige = form.creerLitige( request );

        String colis = form.getValeurChamp( request, "colis" );
        String ICU = form.getValeurChamp( request, "ICU" );
        request.setAttribute( ATT_ICU, ICU );
        request.setAttribute( ATT_COM, colis );
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_LITIGE, litige );
        if ( form.getErreurs().isEmpty() ) {
        	 
             this.getServletContext().getRequestDispatcher( VUE_CREE ).forward( request, response );
        }else{
        	 
             this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
       
    }

}
