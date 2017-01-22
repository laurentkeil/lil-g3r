package com.mdl.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Litige;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.LitigeDAO;
import com.mdl.forms.RechercheLitigeForm;
import com.mdl.parser.Convertion;

/**
 * Servlets permettant de contrôler les informations utiles pour la consultation des litiges selon leur
 * catégorie
 * 
 *
 */
public class ConsulterLitigesCat extends HttpServlet {
	// Constante de redirection
    public static final String VUE                 = "/WEB-INF/pagesJSP/employe/listerTousLesLitiges.jsp";
    public static final String VUE_DETAIL          = "/WEB-INF/pagesJSP/employe/consulterLitigeEmploye.jsp";
    public static final String VUE_ERREUR          = "/WEB-INF/pagesJSP/erreur.jsp";
    public static final String ACCES_CONNEXION     = "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_USER = "/AccueilClient";
    // Attribut de session
    public static final String ATT_SESSION_USER    = "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL    = "sessionEmploye";
    public static final String ATT_SESSION_ID      = "sessionIdEmpl";
    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY    = "daofactory";
    // Attributs utiles pour la JSP
    public static final String ATT_LIST_LITIGE     = "litigeList";
    public static final String ATT_LIST_DETAIL     = "listDetail";
    public static final String ATT_LITIGE          = "litige";
    public static final String ATT_STATUT          = "statut";
    public static final String ATT_CREATION        = "creation";
    public static final String ATT_USER            = "utilisateur";
    public static final String ATT_NUMLITIGE       = "numLitige";
    public static final String ATT_ID              = "idEmpl";

    private LitigeDAO          litigeDao;

    /**
     * Initialisation de la DAO des litiges pour l'interactions avec la base de données
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
        request.setCharacterEncoding( "UTF-8" );
        
        if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
        	// Si l'utilisateur est connecté, il est redirigé vers sa page d'accueil
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        } else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            // Si aucun employé n'est connecté, il est redirigé vers sa page de connexion
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String statut = request.getParameter( "statut" );
            String mail = (String) session.getAttribute( ATT_SESSION_EMPL );
            if ( statut.equals( "1" ) || statut.equals( "2" ) || statut.equals( "3" ) || statut.equals( "4" )
                    || statut.equals( "5" )
                    || statut.equals( "6" ) || statut.equals( "0" ) ) {
            	// Permet de vérifier de ne pas avoir une autre catégorie
                List<Litige> litigeList = new ArrayList<Litige>();
                if ( statut.equals( "0" ) ) {
                	// Liste tous les litiges
                    litigeList = litigeDao.listerAll();
                } else if ( statut.equals( "5" ) ) {
                	// Listes les litiges en attente d'une réponse de l'employé et dont l'employé a répondu personnellement
                    String newStatut = "2";
                    litigeList = litigeDao.trouverLitigesEmpl( newStatut, mail );
                } else if ( statut.equals( "6" ) ) {
                	// Listes les litiges en attente d'une réponse du client et dont l'employé a répondu personnellement
                    String newStatut = "3";
                    litigeList = litigeDao.trouverLitigesEmpl( newStatut, mail );
                } else {
                	// Litige fermé
                    litigeList = litigeDao.trouverLitigesCat( statut );
                }

                request.setAttribute( ATT_LIST_LITIGE, litigeList );
                request.setAttribute( ATT_STATUT, statut );
                this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            } else {
                this.getServletContext().getRequestDispatcher( VUE_ERREUR ).forward( request, response );
            }

        }

    }

    /**
     * Méthode permettant de contrôler les données issues du formulaire pour l'affichage des détails d'un litige
     * et de l'insertion d'une réponse
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            String recherche = getValeurChamp( request, "recherche" );
            List<Litige> listDetail = new ArrayList<Litige>();
            String statut = getValeurChamp( request, "statut" );
            if ( recherche.equals( "oui" ) ) {
            	// Lorsqu'on effectue une recherche
                String mail = (String) session.getAttribute( ATT_SESSION_EMPL );
                RechercheLitigeForm form = new RechercheLitigeForm( litigeDao );
                listDetail = form.rechercheLitige( request, mail );
                request.setAttribute( ATT_LIST_LITIGE, listDetail );
                request.setAttribute( ATT_STATUT, statut );
                this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
            } else {
                String numLitige = request.getParameter( ATT_NUMLITIGE );
                Litige litige = litigeDao.trouverLitigeEmpl( numLitige );
                int id = (Integer) session.getAttribute( ATT_SESSION_ID );
                // Permet de savoir si la dernière description est une réponse
                // ou non pour savoir
                // si on doit afficher un textarea

                listDetail = litige.getDetail();
                Litige valeur = new Litige();
                Iterator i = listDetail.iterator();
                while ( i.hasNext() ) {
                    valeur = (Litige) i.next();
                    litige.setReponse( valeur.isReponse() );
                }


                String creation = litige.getDateCreation();

                request.setAttribute( ATT_LITIGE, litige );
                request.setAttribute( ATT_CREATION, creation );
                request.setAttribute( ATT_ID, id );
                this.getServletContext().getRequestDispatcher( VUE_DETAIL ).forward( request, response );
            }
        }

    }
    
	 /**
	  * Méthode utilitaire qui retourne NULL si un champ est vide, et son contenu sinon.
	  * @param request
	  * @param nomChamp
	  * @return la valeur
	  * 
	  */ 
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }
}
