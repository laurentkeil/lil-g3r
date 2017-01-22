package com.mdl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Classe contenant les méthodes utilisées constamment dans les différentes méthodes DAO
 * @author CrespeigneRomain
 *
 */
public final class DAOUtilitaire {

	/**
	 * Constructeur caché par défaut (car c'est une classe finale utilitaire, contenant uniquement
	 * des méthodes appelées de manière statique).
	 * @author CrespeigneRomain
	 */
    private DAOUtilitaire() {
    }

    
    /**
     * Fermeture silencieuse du resultSet
     * @param resultSet
     * @author CrespeigneRomain
     */
    public static void fermetureSilencieuse( ResultSet resultSet ) {
        if ( resultSet != null ) {
            try {
                resultSet.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec : " + e.getMessage() );
            }
        }
    }

    /**
     * Fermeture silencieuse du statement
     * @param statement
     * @author CrespeigneRomain
     */
    public static void fermetureSilencieuse( Statement statement ) {
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec : " + e.getMessage() );
            }
        }
    }

    /**
     * Fermeture silencieuse de la connexion
     * @param connexion
     * @author CrespeigneRomain
     */
    public static void fermetureSilencieuse( Connection connexion ) {
        if ( connexion != null ) {
            try {
                connexion.close();
            } catch ( SQLException e ) {
                System.out.println( "Echec : " + e.getMessage() );
            }
        }
    }

    /**
     * Fermetures silencieuses du statement et de la connexion
     * @param statement
     * @param connexion
     * @author CrespeigneRomain
     */
    public static void fermeturesSilencieuses( Statement statement, Connection connexion ) {
        fermetureSilencieuse( statement );
        fermetureSilencieuse( connexion );
    }

    /**
     * Fermetures silencieuses du resultSet, du statement et de la connexion
     * @param resultSet
     * @param statement
     * @param connexion
     * @author CrespeigneRomain
     */
    public static void fermeturesSilencieuses( ResultSet resultSet, Statement statement, Connection connexion ) {
        fermetureSilencieuse( resultSet );
        fermetureSilencieuse( statement );
        fermetureSilencieuse( connexion );
    }

    
    /**
     * Initialisation de la requête préparée basée sur la connexion passée en argument,
     * avec la requête SQL et les objets donnés.
     * @param connexion
     * @param sql
     * @param returnGeneratedKeys
     * @param objets
     * @return
     * @throws SQLException
     * @author CrespeigneRomain
     */
    public static PreparedStatement initialisationRequetePreparee( Connection connexion, String sql,
            boolean returnGeneratedKeys, Object... objets ) throws SQLException {
        PreparedStatement preparedStatement = connexion.prepareStatement( sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }
        return preparedStatement;
    }
}
