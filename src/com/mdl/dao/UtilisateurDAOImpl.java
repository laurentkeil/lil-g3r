package com.mdl.dao;

import static com.mdl.dao.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Utilisateur;


/**
 * @see UtilisateurDAO
 * 
 * 
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {
    /*
     * Impl�mentation de la m�thode trouver() d�finie dans l'interface
     * UtilisateurDao
     */
    private static DAOFactory   daoFactory;
    private static final String SQL_SELECT_PAR_EMAIL   = "SELECT * FROM client WHERE mail = ?";
    private static final String SQL_SELECT_PAR_ID      = "SELECT * FROM client WHERE identifiant = ?";
    private static final String SQL_INSERT             = "INSERT INTO client (nom, prenom,sexe, adr_rue, adr_num, adr_boite, adr_localite, adr_code_postal, adr_pays, "
                                                               + "telephone_portable, telephone_fixe, mail, mot_de_passe, date_naissance, date_inscription,validation, num_tva) "
                                                               + "VALUES (?,?,?,?,?,?,?, ?,?, ?, ?,?,?,to_date(?,'DD-MM-YY'), SYSTIMESTAMP, ?, ?)";
    private static final String SQL_UPDATE_VALIDATION  = "UPDATE client SET validation = ? WHERE mail = ?";
    private static final String SQL_UPDATE_PROFIL      = "UPDATE client SET "
                                                               + "nom = ?, prenom = ?, sexe = ?, adr_rue = ?, adr_num = ?, adr_boite = ?, adr_localite = ?, adr_code_postal = ?, adr_pays=?,"
                                                               + "telephone_portable = ?, telephone_fixe = ?,  mot_de_passe = ?, date_naissance = ?, num_tva = ?"
                                                               + " WHERE mail = ? ";
    private static final String SQL_SELECT_ALL         = "SELECT * FROM CLIENT";

    private static final String SQL_SELECT_ALL_INSCRIT = "SELECT CLIENT.identifiant, CLIENT.nom, CLIENT.prenom, CLIENT.sexe, CLIENT.adr_rue, CLIENT.adr_num, CLIENT.adr_boite, CLIENT.adr_localite, CLIENT.adr_code_postal, "
                                                               + "CLIENT.adr_pays, CLIENT.telephone_portable, CLIENT.telephone_fixe, CLIENT.mail, CLIENT.date_naissance, CLIENT.validation, CLIENT.num_tva, CLIENT.date_inscription, "
                                                               + "CLIENT.mot_de_passe FROM CLIENT  , STATUT_CLIENT  "
                                                               + " WHERE CLIENT.identifiant = STATUT_CLIENT.ref_statut_client_id AND CLIENT.mail <> 'partenaire_etranger'";
    private static final String SQL_SELECT_RECH_MAIL   = "SELECT CLIENT.identifiant, CLIENT.nom, CLIENT.prenom, CLIENT.sexe, CLIENT.adr_rue, CLIENT.adr_num, CLIENT.adr_boite, CLIENT.adr_localite, CLIENT.adr_code_postal, "
                                                               + "CLIENT.adr_pays, CLIENT.telephone_portable, CLIENT.telephone_fixe, CLIENT.mail, CLIENT.date_naissance, CLIENT.validation, CLIENT.num_tva, CLIENT.date_inscription, "
                                                               + "CLIENT.mot_de_passe FROM CLIENT  , STATUT_CLIENT  "
                                                               + " WHERE CLIENT.identifiant = STATUT_CLIENT.ref_statut_client_id AND CLIENT.mail = ?";
    private static final String SQL_SELECT_RECH_NOM    = "SELECT CLIENT.identifiant, CLIENT.nom, CLIENT.prenom, CLIENT.sexe, CLIENT.adr_rue, CLIENT.adr_num, CLIENT.adr_boite, CLIENT.adr_localite, CLIENT.adr_code_postal, "
                                                               + "CLIENT.adr_pays, CLIENT.telephone_portable, CLIENT.telephone_fixe, CLIENT.mail, CLIENT.date_naissance, CLIENT.validation, CLIENT.num_tva, CLIENT.date_inscription, "
                                                               + "CLIENT.mot_de_passe FROM CLIENT  , STATUT_CLIENT  "
                                                               + " WHERE CLIENT.identifiant = STATUT_CLIENT.ref_statut_client_id AND CLIENT.nom = ?";

    UtilisateurDAOImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public Utilisateur trouver( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL,
                    false, email );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                utilisateur = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return utilisateur;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public Utilisateur trouverAvecId( String id ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ID, false,
                    id );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                utilisateur = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return utilisateur;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public static Utilisateur trouverUtilisateur( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;
        try {
            /* R�cup�ration d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL,
                    false, email );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de donn�es de l'�ventuel ResulSet retourn� */
            if ( resultSet.next() ) {
                utilisateur = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }

        return utilisateur;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public void creer( Utilisateur utilisateur ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "con/fir$mation d'1nscription";
        textEncryptor.setPassword( myEncryptionPassword );
        String myEncryptedValidation = textEncryptor.encrypt( "non" );
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_INSERT, false,
                    utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getSexe(), utilisateur.getAdresseRue(),
                    utilisateur.getAdresseNum(),
                    utilisateur.getAdresseBoite(), utilisateur.getAdresseLoc(), utilisateur.getAdresseCode(),
                    utilisateur.getAdressePays(), utilisateur.getTelephonePortable(), utilisateur.getTelephoneFixe(),
                    utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getDateNaissance(),
                    myEncryptedValidation, utilisateur.getNum_tva() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Echec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public boolean inscription( String email ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean existe = true;
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        String myEncryptionPassword = "con/fir$mation d'1nscription";
        textEncryptor.setPassword( myEncryptionPassword );
        String myEncryptedValidation = textEncryptor.encrypt( "oui" );
        // Permet de chiffer la valeur du champ de validation.
        try {
            Utilisateur utilisateurRech = new Utilisateur();
            utilisateurRech = trouverUtilisateur( email );
            if ( utilisateurRech == null ) {
                existe = false;
            } else {
                connexion = daoFactory.getConnection();
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                        SQL_UPDATE_VALIDATION, false, myEncryptedValidation, email );

                int statut = preparedStatement.executeUpdate();
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la mise à jour de l'utilisateur, aucune ligne modifiée dans la table." );
                }
                existe = true;
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return existe;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public void modifier( Utilisateur utilisateur ) throws IllegalArgumentException, DAOException {

        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_UPDATE_PROFIL, false,
                    utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getSexe(), utilisateur.getAdresseRue(),
                    utilisateur.getAdresseNum(),
                    utilisateur.getAdresseBoite(), utilisateur.getAdresseLoc(), utilisateur.getAdresseCode(),
                    utilisateur.getAdressePays(), utilisateur.getTelephonePortable(), utilisateur.getTelephoneFixe(),
                    utilisateur.getMotDePasse(), utilisateur.getDateNaissance(), utilisateur.getNum_tva(),
                    utilisateur.getEmail() );

            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException(
                        "Echec de la mise à jour de l'utilisateur, aucune ligne modifiée dans la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public List<Utilisateur> listerAll() throws DAOException {
        List<Utilisateur> listClient = new ArrayList<Utilisateur>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_ALL, false );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                listClient.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listClient;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public List<Utilisateur> listerAllInscrit() throws DAOException {
        List<Utilisateur> listClient = new ArrayList<Utilisateur>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, SQL_SELECT_ALL_INSCRIT,
                    false );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                listClient.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return listClient;
    }

    /**
     * @see UtilisateurDAO
     * 
     * 
     */
    public List<Utilisateur> trouverClientRech( String rech, String research )
            throws DAOException {
        List<Utilisateur> clientList = new ArrayList<Utilisateur>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connexion = daoFactory.getConnection();
            if ( research.equals( "1" ) ) {
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                        SQL_SELECT_RECH_MAIL, true, rech );
                resultSet = preparedStatement.executeQuery();
            } else {
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                        SQL_SELECT_RECH_NOM, true, rech );
                resultSet = preparedStatement.executeQuery();
            }

            while ( resultSet.next() ) {
                clientList.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return clientList;
    }

    /**
     * Permet de récupérer les valeurs résultantes de la recherche et de les
     * placer dans le bean Utilisateur
     * 
     * @param resultSet
     * @return un objet Utilisateur
     * @throws SQLException
     * 
     */
    private static Utilisateur map( ResultSet resultSet ) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId( resultSet.getInt( "identifiant" ) );
        utilisateur.setEmail( resultSet.getString( "mail" ) );
        utilisateur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
        utilisateur.setNom( resultSet.getString( "nom" ) );
        utilisateur.setPrenom( resultSet.getString( "prenom" ) );
        utilisateur.setSexe( resultSet.getString( "sexe" ) );
        utilisateur.setAdresseRue( resultSet.getString( "adr_rue" ) );
        utilisateur.setAdresseNum( resultSet.getString( "adr_num" ) );
        utilisateur.setAdresseBoite( resultSet.getString( "adr_boite" ) );
        utilisateur.setAdresseLoc( resultSet.getString( "adr_localite" ) );
        utilisateur.setAdresseCode( resultSet.getString( "adr_code_postal" ) );
        utilisateur.setAdressePays( resultSet.getString( "adr_pays" ) );
        utilisateur.setTelephonePortable( resultSet.getString( "telephone_portable" ) );
        utilisateur.setTelephoneFixe( resultSet.getString( "telephone_fixe" ) );
        utilisateur.setNum_tva( resultSet.getString( "num_tva" ) );
        utilisateur.setValidation( resultSet.getString( "validation" ) );
        utilisateur.setDateNaissance( (Date) resultSet.getObject( "date_naissance" ) );
        utilisateur.setDateInscription((Date) resultSet.getObject( "date_inscription" ) );
        return utilisateur;
    }

}
