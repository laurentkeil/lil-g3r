package com.mdl.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Surfacturation;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.StatutDAO;
import com.mdl.dao.SurfacturationDAO;
import com.mdl.dao.UtilisateurDAO;
import com.mdl.pdf.EnvoyerFacture;

/**
 * Servlet d'affichage d'un message de confirmation, mise en place dans la bd +
 * déblocage du compte client et envoi d'un mail avec la surfacturation et
 * redirection automatique vers la liste des surfaturation.
 * 
 */
public class EnvoyerSurfacturation extends HttpServlet {
    /**
     * déclaration des constantes
     */
    public static final String ATT_SESSION_USER     = "sessionUtilisateur";
    public static final String ATT_SESSION_COMMANDE = "sessionCommande";
    public static final String ATT_NUMCOM           = "icu";
    public static final String ATT_PRIX             = "prix";
    public static final String ATT_ENVOI            = "envoi";
    public static final String ATT_SURFACT          = "surfact";
    public static final String CONF_DAO_FACTORY     = "daofactory";
    public static final String ACCES_CONNEXION      = "/ConnexionClient";
    public static final String ATT_SESSION_EMPL     = "sessionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";

    /**
     * déclaration des vues auxquelles on fait rediriger l'utilisateur de
     * l'application web
     */
    public static final String ACCES_SUCCES         = "/ConfirmationPaiementSurfacturation";

    private SurfacturationDAO  surfacturationDao;
    private UtilisateurDAO     utilisateurDao;
    private StatutDAO          statutDao;

    /**
     * initialisation de la surfacturation et ses informations en base de donnée
     */
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO surfacturation */
        this.surfacturationDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getSurfacturationDao();
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
        this.statutDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getStatutDao();
    }

    /**
     * affichage d'un message de confirmation et création en bd de la commande
     * et email avec bordereau d envoi
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding( "UTF-8" );
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connectés.
         */
        if ( session.getAttribute( ATT_SESSION_EMPL ) != null ) {
            response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_EMPL );
        } else if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        } else {
            /**
             * récupération en requete de la commande
             */
            String icu = request.getParameter( ATT_NUMCOM );
            String prix = request.getParameter( ATT_PRIX );

            /**
             * upadte de la surfacturation en base de donnée lors de la
             * confirmation du paiement
             */
            surfacturationDao.payer( icu, prix );

            Surfacturation surfacturation = surfacturationDao.trouverSurfactCommande( icu );

            /**
             * Déblocage du compte client
             */
            statutDao.setStatut( surfacturation.getId_client(), 1 );

            /**
             * Récupération du mail utilisateur en session
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            /**
             * Récupération du client en BD
             */
            Utilisateur utilisateur = utilisateurDao.trouver( mail );
            /**
             * Envoie de la surfacture pdf (surfacture) par mail
             */
            EnvoyerFacture pdf2 = new EnvoyerFacture();
            try {
                pdf2.EmailFacture( mail, surfacturation, utilisateur );
            } catch ( Exception e ) {
            }

            /* Redirection vers la page de confirmation d envoi de mail */
            response.sendRedirect( request.getContextPath() + ACCES_SUCCES );
        }

    }

}