package com.mdl.servlets;
 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mdl.beans.Employe;
import com.mdl.dao.*;
import com.mdl.forms.InscriptionEmployeForm;
 

/**
 * Servlet permettant le contrôle des informations utiles pour l'inscription d'un employé pour 
 * facilité les tests de l'application web
 * 
 *
 */
public class InscriptionEmploye extends HttpServlet {
	// Attributs utiles pour la page JSP
    public static final String ATT_USER 		= "employe";
    public static final String ATT_FORM 		= "form";
    // Constantes de redirection
    public static final String VUE_ARRIVEE 		= "/WEB-INF/pagesJSP/employe/inscriptionEmploye.jsp";
    public static final String VUE_SORTIE 		= "/WEB-INF/pagesJSP/employe/inscriptionEmploye.jsp";
    // Constante de la DAO factory
    public static final String CONF_DAO_FACTORY = "daofactory";
    // Attribut de session
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    
         
    
    private EmployeDAO     employeDao;

    /**
     * Initialisation du DAO de l'employé pour l'interaction avec la base de données
     * 
     */
    public void init() throws ServletException {
        this.employeDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getEmployeDao();
    }
    
    /**
     * Méthode permettant la redirection vers la page JSP pour l'inscription de l'employé
     * 
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    		this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
    }
     
    
    /**
     * Méthode permettant le contrôle des informations utiles pour l'inscription d'un employé
     * 
     */
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

    	request.setCharacterEncoding( "UTF-8" );
        InscriptionEmployeForm form = new InscriptionEmployeForm(employeDao);
        
        Employe employe = form.inscrireEmploye( request );
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, employe );
         
        this.getServletContext().getRequestDispatcher( VUE_ARRIVEE ).forward( request, response );
    }
    
}