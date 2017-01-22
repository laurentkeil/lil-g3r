package com.mdl.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @author CrespeigneRomain
 *
 */
public class DAOFactory {

    private static final String FICHIER_PROPERTIES       = "/com/mdl/dao/dao.properties";
    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomutilisateur";
    private static final String PROPERTY_MOT_DE_PASSE    = "motdepasse";

    private String              url;
    private String              username;
    private String              password;

    /**
     * Constructeur de la DAOFactory
     * @param url
     * @param username
     * @param password
     * @author CrespeigneRomain
     */
    DAOFactory( String url, String username, String password ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /*
     * M�thode charg�e de r�cup�rer les informations de connexion � la base de
     * donn�es, charger le driver JDBC et retourner une instance de la Factory
     */
    /**
     * Méthode chargée de récupérer les informations de connexion à la base de données, 
     * charge le driver JDBC et retourner une instance de la Factory
     * @return
     * @throws DAOConfigurationException
     * @author CrespeigneRomain
     */
    public static DAOFactory getInstance() throws DAOConfigurationException {
        Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }

        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
        }

        DAOFactory instance = new DAOFactory( url, nomUtilisateur, motDePasse );
        return instance;
    }

    /**
     * Méthode chargée de fournir une connexion à la base de données
     * @return 
     * @throws SQLException
     * @author CrespeigneRomain
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection( url, username, password );
    }

    /**
     * Méthode de récupération de l'implémentation des différents DAO
     * @author CrespeigneRomain
     */
    public UtilisateurDAO getUtilisateurDao() {
    	
        return new UtilisateurDAOImpl( this );
    }
    
    public CommandeDAO getCommandeDao(){
    	return new CommandeDAOImpl( this );
    }
    
    public LitigeDAO getLitigeDao(){
    	return new LitigeDAOImpl( this );
    }
    
    public EmployeDAO getEmployeDao(){
    	return new EmployeDAOImpl( this );
    }
    
    public TrackingDAO getTrackingDao(){
    	return new TrackingDAOImpl( this );
    }
    
    public StatutDAO getStatutDao(){
    	return new StatutDAOImpl( this );
    }
    
    public SurfacturationDAO getSurfacturationDao(){
    	return new SurfacturationDAOImpl( this );
    }
}