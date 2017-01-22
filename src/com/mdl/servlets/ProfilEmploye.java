package com.mdl.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.mdl.beans.Employe;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.EmployeDAO;

/**
 * Servlets contrôlant les informations utiles pour l'affichage du profil de l'employé
 * 
 *
 */
public class ProfilEmploye extends HttpServlet {
	// Constantes de redirection
    public static final String  VUE             	= "/WEB-INF/pagesJSP/employe/profilEmploye.jsp";
    public static final String ACCES_CONNEXION  	= "/ConnexionEmploye";
    public static final String ACCES_CONNECTE_EMPL  = "/AccueilEmploye";
    public static final String ACCES_CONNECTE_USER  = "/AccueilClient";
    // Attributs de session
    public static final String ATT_SESSION_USER 	= "sessionUtilisateur";
    public static final String ATT_SESSION_EMPL 	= "sessionEmploye";
    public static final String  ATT_SESSION_ID 		= "sessionIdEmpl";
    // Constante du DAOFactory
    public static final String CONF_DAO_FACTORY 	= "daofactory";
    // Attribut utile pour la page JSP
    public static final String ATT_USER 	    	= "employe";
         
    
    private EmployeDAO     employeDao;

    /**
     * Initialisation de la DAO de l'employé pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.employeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getEmployeDao();
    }
    
 
    /**
     * Méthode contrôlant les informations utiles pour l'affichage du profil de l'employé et redirection vers la page JSP
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
  
    	
    	HttpSession session = request.getSession();
    	request.setCharacterEncoding( "UTF-8" );
        // Vérification des sessions
    	if ( session.getAttribute( ATT_SESSION_USER ) != null ) {
    		response.sendRedirect( request.getContextPath() + ACCES_CONNECTE_USER );
        }else if ( session.getAttribute( ATT_SESSION_EMPL ) == null ) {
        	response.sendRedirect( request.getContextPath() + ACCES_CONNEXION );
        }else{
        	String email = (String) session.getAttribute(ATT_SESSION_EMPL);
        	
        	Employe employe = employeDao.trouver(email);
        	Date date = employe.getDate_naissance();
        	Calendar calendar =new GregorianCalendar();
        	calendar.setTime(date);
        	int annee =calendar.get(Calendar.YEAR);
        	int mois =calendar.get(Calendar.MONTH)+1;
        	int jour =calendar.get(Calendar.DAY_OF_MONTH);
        	String anneeNaissance = Integer.toString(annee);
        	String moisNaissance = Integer.toString(mois);
        	String jourNaissance = Integer.toString(jour);
        	employe.setAnneeNaissance(anneeNaissance); 
        	employe.setMoisNaissance(moisNaissance);
        	employe.setJourNaissance(jourNaissance);
        	
            request.setAttribute( ATT_USER, employe );
        	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
        
    }

}
