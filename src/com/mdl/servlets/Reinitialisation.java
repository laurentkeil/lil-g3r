package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.forms.ConnexionClientForm;
import com.mdl.forms.MdpPerduClientForm;

/**
 * Servlet contrôlant les informations relatives à la modification d'un mot de passe pour un client
 * 
 *
 */
public class Reinitialisation extends HttpServlet {
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_FORM            = "form";
    public static final String ATT_MAIL            = "mail";
    public static final String CONFIRMATION		   = "confirmation";
    public static final String DELAI			   = "delai";
    public static final String VUE_ARRIVEE         = "/WEB-INF/pagesJSP/reinitialisation.jsp";
    public static final String ACCES_CONNECTE      = "/AccueilClient";
    public static final String ACCES_CONNECTE_EMPL = "/AccueilEmploye";
    public static final String CONF_DAO_FACTORY    = "daofactory";
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";

    private UtilisateurDAO     utilisateurDao;

    /**
     * Initialisation de la DAO de l'utilisateur pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    /**
     * Méthode permettant de rediriger vers la page JSP pour la demande de réinitialisation du mot de passe
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        HttpSession session = request.getSession();
        // Contrôle des sessions
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	String date = request.getParameter("v");
        	String mail = request.getParameter("reinit");
        	MdpPerduClientForm form = new MdpPerduClientForm( utilisateurDao );
            boolean delai = false;
			try {
				delai = form.verificationReinit(request, date, mail);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // Permet d'avoir le bon chiffrement
        	mail = mail.replaceAll("\\s", "+");
            mail = form.dechiffrementEmail(mail);
         	request.setAttribute(ATT_MAIL, mail);
         	request.setAttribute(DELAI, delai);
            this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
        } else {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
    }

    /**
     * Méthode permettant de contrôler les données récupérées du formulaire pour réintialiser le
     * mot de passe.
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
        HttpSession session = request.getSession();

        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	 boolean confirmation;
        	 MdpPerduClientForm form = new MdpPerduClientForm( utilisateurDao );
             form.resetMotDePasse(request);
             request.setAttribute( ATT_FORM, form );
             if ( form.getErreurs().isEmpty() ) {
             	confirmation = true;
             	request.setAttribute(CONFIRMATION, confirmation);
                 this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
             } else {
             	confirmation = false;
             	request.setAttribute(CONFIRMATION, confirmation);
                this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
             }
        } else {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE );
        }
       
    }

}