package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mdl.beans.Employe;
import java.sql.PreparedStatement;

/**
 * @see EmployeDAO
 *  
 *
 */
public class EmployeDAOImpl implements EmployeDAO {

    private DAOFactory          daoFactory;
    private static final String SQL_SELECT_PAR_EMAIL = "SELECT * FROM empl_service_clientele WHERE mail = ?";
    private static final String SQL_INSERT           = "INSERT INTO empl_service_clientele (nom, prenom,sexe, statut_marital, adr_rue, adr_num, adr_boite, adr_localite, adr_code_postal, adr_pays, "
                                                             + "telephone_fixe, telephone_portable, mail, mot_de_passe, date_naissance) "
                                                             + "VALUES (?,?,?,?,?,?,?, ?, ?,?, ?,?,?,?,?)";
    
    /**
     * Constructeur de EmployeDAOImpl
     * @param daoFactory
     */
    EmployeDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /**
     * @see EmployeDAO
     *  
     *
     */
    public Employe trouver( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employe employe = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL,
                    false, email );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                employe = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return employe;
    }

    /**
     * @see EmployeDAO
     *  
     *
     */
    public void creer( Employe employe ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT, false,
                    employe.getNom(), employe.getPrenom(), employe.getSexe(), employe.getStatutMarital(),
                    employe.getAdresse_rue(), employe.getAdresse_num(),
                    employe.getAdresse_boite(), employe.getAdresse_loc(), employe.getAdresse_code(), employe.getPays(),
                    employe.getTelephoneFixe(),
                    employe.getTelephonePortable(), employe.getEmail(), employe.getMotDePasse(),
                    employe.getDate_naissance() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création de l'employe, aucune ligne ajoutée dans la table." );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * Permet de récupérer les informations issues de la requête SQL
     * @param resultSet
     * @return un objet Employe
     * @throws SQLException
     *  
     */
    private static Employe map( ResultSet resultSet ) throws SQLException {
        Employe employe = new Employe();
        employe.setId( resultSet.getInt( "identifiant" ) );
        employe.setEmail( resultSet.getString( "mail" ) );
        employe.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
        employe.setNom( resultSet.getString( "nom" ) );
        employe.setPrenom( resultSet.getString( "prenom" ) );
        employe.setSexe( resultSet.getString( "sexe" ) );
        employe.setStatutMarital( resultSet.getString( "statut_marital" ) );
        employe.setAdresse_rue( resultSet.getString( "adr_rue" ) );
        employe.setAdresse_num( resultSet.getString( "adr_num" ) );
        employe.setAdresse_boite( resultSet.getString( "adr_boite" ) );
        employe.setAdresse_loc( resultSet.getString( "adr_localite" ) );
        employe.setAdresse_code( resultSet.getString( "adr_code_postal" ) );
        employe.setPays( resultSet.getString( "adr_pays" ) );
        employe.setTelephonePortable( resultSet.getString( "telephone_portable" ) );
        employe.setTelephoneFixe( resultSet.getString( "telephone_fixe" ) );
        employe.setDate_naissance( (Date) resultSet.getObject( "date_naissance" ) );
        return employe;
    }

}
