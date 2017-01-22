package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mdl.beans.Statut_client;
import java.sql.PreparedStatement;

public class StatutDAOImpl implements StatutDAO {
    /*
     * Impl�mentation de la m�thode trouver() d�finie dans l'interface StatutDao
     */
    private static DAOFactory   daoFactory;
    private static final String SQL_TROUVER    = "SELECT * FROM statut_client WHERE ref_statut_client_id = ?";
    private static final String SQL_ALL_BLOQUE = "SELECT * FROM statut_client WHERE statut = 0";
    private static final String SQL_INSERT     = "INSERT INTO statut_client (statut, date_nouveau_statut, ref_statut_client_id) "
                                                       + "values (1,SYSDATE,?)";
    private static final String SQL_SETSTATUT  = "UPDATE STATUT_CLIENT SET statut = ?, cause = null WHERE ref_statut_client_id = ?";
    private static final String SQL_BLOQUE     = "UPDATE STATUT_CLIENT SET statut = 0, cause = ? WHERE ref_statut_client_id = ?";
    private static final String SQL_SETIDFREE  = "UPDATE statut_client SET ref_statut_colis_id = ? "
                                                       + "WHERE ref_statut_client_id = ?";

    StatutDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    public Statut_client trouver( int idClient ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Statut_client statut = null;
        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_TROUVER, false,
                    idClient );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es de l'�ventuel ResulSet retourn� */
            if ( resultSet.next() ) {
                statut = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return statut;
    }

    public List<Statut_client> trouverAllBloques() throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Statut_client> statutList = new ArrayList<Statut_client>();
        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_ALL_BLOQUE, false );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es de l'�ventuel ResulSet retourn� */
            while ( resultSet.next() ) {
                statutList.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return statutList;
    }

    public void nouveauClient( int idClient ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT, false,
                    idClient );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourn� par la requ�te d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see StatutDAO
     */
    public void setStatut( int idClient, int statutCompte ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SETSTATUT, false,
                    statutCompte, idClient );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Echec de l'update du statut du client, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see StatutDAO
     */
    public void setIdFree( int idColis, int idClient ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SETIDFREE, false,
                    idColis, idClient );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Echec de l'update du statut du colis, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see StatutDAO
     */
    public void bloquerCompte( int idClient, String cause ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_BLOQUE, false, cause,
                    idClient );

            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourn� par la requ�te d'insertion */
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    private static Statut_client map( ResultSet resultSet ) throws SQLException {
        Statut_client statut = new Statut_client();
        statut.setId( resultSet.getInt( "identifiant" ) );
        statut.setStatut( resultSet.getString( "statut" ) );
        statut.setCause( resultSet.getString( "cause" ) );
        statut.setClient( resultSet.getString( "ref_statut_client_id" ) );
        statut.setColis( resultSet.getString( "ref_statut_colis_id" ) );
        statut.setDate( resultSet.getDate( "date_nouveau_statut" ) );
        return statut;
    }

}
