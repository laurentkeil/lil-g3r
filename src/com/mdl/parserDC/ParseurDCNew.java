package com.mdl.parserDC;

import static com.mdl.parserDC.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.parserDC.DAOUtilitaire.initialisationRequetePreparee;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.mdl.dao.DAOException;
import com.mdl.parser.GenerateICU;

public class ParseurDCNew {

    private CentersType doc;
    private String[]    emp;
    private int[]       veh;

    private String      url            = "jdbc:oracle:thin:@st-oracle.info.fundp.ac.be:1521:xe";
    private String      driver         = "oracle.jdbc.driver.OracleDriver";
    private String      nomUtilisateur = "G3.HLB";
    private String      motDePasse     = "uuj2aepo";

    // TODO daoFactory ?
    public ParseurDCNew() {
        try {
            JAXBContext ctx = JAXBContext.newInstance( CentersType.class );
            Unmarshaller uu = ctx.createUnmarshaller();
            ObjectFactory factory = new ObjectFactory();
            doc = factory.createCentersType();
            doc = (CentersType) uu.unmarshal( new File( "dispatching-centers.xml" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void parse() {
        CenterType centerType = doc.getCenter();
        int idCentre = trouverIDCentre( centerType.getName() );
        parseVehicles( centerType.getVehicles(), idCentre );
        parseEmployees( centerType.getEmployees(), idCentre );
        parseTrolleys( centerType.getTrolleys(), idCentre );
    }

    private int trouverIDCentre( String nameCentre ) {

        if ( nameCentre.equals( "Liège" ) ) {
            return 4;
        }
        else if ( nameCentre.equals( "Hasselt" ) ) {
            return 5;
        }
        else if ( nameCentre.equals( "Arlon" ) ) {
            return 1;
        }
        else if ( nameCentre.equals( "Namur" ) ) {
            return 2;
        } else if ( nameCentre.equals( "Mons" ) ) {
            return 3;
        } else if ( nameCentre.equals( "Wavre" ) ) {
            return 6;
        } else if ( nameCentre.equals( "Louvain" ) ) {
            return 7;
        } else if ( nameCentre.equals( "Gand" ) ) {
            return 9;
        } else if ( nameCentre.equals( "Anvers" ) ) {
            return 8;
        } else if ( nameCentre.equals( "Bruges" ) ) {
            return 10;
        } else if ( nameCentre.equals( "Aéroport-Liège" ) ) {
            return 24;
        } else if ( nameCentre.equals( "Aéroport-Bruxelles" ) ) {
            return 11;
        } else {
            System.out.println( "Erreur - centre inconnu" );
            return 2;
        }
    }

    public void parseVehicles( VehiclesType vehicles, int idCentre ) {
        List<VehicleType> vehicleList = vehicles.getVehicle();
        emp = new String[vehicleList.size()];
        veh = new int[vehicleList.size()];
        int i = 0;
        for ( VehicleType vehicle : vehicleList ) {
            Dimension dim = vehicle.getDimension();
            emp[i] = vehicle.getEmployeeincharge();
            Float poids = vehicle.getUsefulweight();
            // String model = vehicle.getModel();
            // Integer nbPassager = vehicle.getPassengernumber();

            Connection connexion = null;
            PreparedStatement preparedStatement = null;
            ResultSet valeursAutoGenerees = null;

            try {
                Class.forName( driver );
            } catch ( ClassNotFoundException e ) {
                throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
            }
            try {
                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );
                String generatedColumns[] = { "identifiant" };
                String requete = "insert into vehicule_coursier (hauteur, largeur, longueur, poids_max) values (?,?,?,?)";

                preparedStatement = connexion.prepareStatement( requete, generatedColumns );
                preparedStatement.setFloat( 1, dim.getTotalheight() );
                preparedStatement.setFloat( 2, dim.getUsefulwidth() );
                preparedStatement.setFloat( 3, dim.getUsefullength() );
                preparedStatement.setFloat( 4, poids );

                int statut = preparedStatement.executeUpdate();

                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException( "Echec de la création du vehicule, aucune ligne ajoutée dans la table." );
                }
                /* Récupération de l'id auto-généré lors de l'insertion */

                valeursAutoGenerees = preparedStatement.getGeneratedKeys();
                if ( valeursAutoGenerees.next() ) {
                    /*
                     * récupération de l'id du vehicule
                     */
                    veh[i] = valeursAutoGenerees.getInt( 1 );
                } else {
                    throw new DAOException( "Echec de la création du vehicule, aucun ID auto-généré retourné." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
            }
            i++;
        }
    }

    public void parseEmployees( EmployeesType employes, int idCentre ) {
        List<EmployeeType> employeeList = employes.getEmployee();
        for ( EmployeeType employe : employeeList ) {

            String date_naissance = employe.getBirthdate();
            String prenom = employe.getFirstname();
            AddressType adresse = employe.getHomeaddress();
            String id = employe.getIdemployee();
            String nom = employe.getLastname();
            String telephone = employe.getPhone();
            int role = -1;
            if ( employe.getRole().equals( "coursier" ) ) {
                role = 0;
            }
            else if ( employe.getRole().equals( "préposé" ) ) {
                role = 1;
            }
            else {
                System.out.println( "pas de role pour coursier " + id );
            }
            UnavailabilitiesType dispo = employe.getUnavailabilities();

            String requete = "INSERT INTO COURSIER (nom, prenom, sexe, mot_de_passe, mail, adr_rue, adr_num, "
                    + "adr_boite, adr_code_postal, adr_localite, adr_pays, telephone_portable, date_naissance, "
                    + "statut_marital, telephone_urgence, responsable_log, ref_coursier_centre_id, ref_coursier_vehicule_id"
                    + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'dd/mm/yyyy'),?,?,?,?,?)";

            Connection connexion = null;

            PreparedStatement preparedStatement = null;
            int intVehicule = 1;

            for ( int i = 0; i < emp.length; i++ ) {
                if ( emp[i].equals( id ) ) {
                    intVehicule = i;
                }
            }
            if ( intVehicule == -1 )
                throw new DAOException( "Vehicule non trouve" );
            try {
                Class.forName( driver );
            } catch ( ClassNotFoundException e ) {
                throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
            }
            try {
                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );
                String motDePasse2 = HashPassword( id + "@hlb-express.be", "test" );
                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                        true, nom, prenom, "M",
                        motDePasse2, ( id + "@hlb-express.be" ), adresse.getStreet(), adresse.getNumber(),
                        adresse.getBox(), adresse.getZip(), adresse.getCity(), adresse.getCountry(), telephone,
                        date_naissance,
                        1, "0000", role, idCentre, intVehicule );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'un coursier, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }

            List<UnavailabilityType> listeDispo = dispo.getUnavailability();
            for ( UnavailabilityType indispo : listeDispo ) {
                // TODO vérifier ce qu'il se passe quand il n'y a pas de centre
                // assigné (il ne faut alors pas en tenir compte)
                if ( indispo.getAssignedcenter() != null ) {
                    int centre = trouverIDCentre( indispo.getAssignedcenter() );
                    GregorianCalendar dateFrom = indispo.getFrom().toGregorianCalendar();
                    GregorianCalendar dateTo = indispo.getTo().toGregorianCalendar();
                    while ( dateFrom.before( dateTo ) ) {
                        String requete2 = "INSERT INTO TRAVAIL_TEMPORAIRE"
                                + "(date_travail, ref_travail_centre_id,ref_travail_coursier_id)"
                                + "VALUES"
                                + "(to_date(?,'yyyy-mm-dd'),?,(select identifiant from coursier where mail = ?))";

                        String dateGreg = dateFrom.get( GregorianCalendar.YEAR ) + "-" +
                                ( dateFrom.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                                dateFrom.get( GregorianCalendar.DAY_OF_MONTH );
                        try {
                            /* Récupération d'une connexion depuis la Factory */
                            connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );
                            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete2,
                                    true,
                                    dateGreg, centre, id + "@hlb-express.be" );

                            int statut2 = preparedStatement.executeUpdate();
                            /*
                             * Analyse du statut retourné par la requête
                             * d'insertion
                             */
                            if ( statut2 == 0 ) {
                                throw new DAOException(
                                        "Echec de la création d'un coursier, aucune ligne ajoutée dans la table." );
                            }
                        } catch ( SQLException e ) {
                            throw new DAOException( e );
                        } finally {
                            fermeturesSilencieuses( preparedStatement, connexion );
                        }
                        dateFrom.add( GregorianCalendar.DAY_OF_MONTH, 1 );
                    }
                }
            }

        }
    }

    public void parseTrolleys( TrolleysType chariots, int idCentre ) {
        List<TrolleyType> listeChariots = chariots.getTrolley();
        for ( TrolleyType chariot : listeChariots ) {
            Dimension dim = chariot.getDimension();
            // String statutChariot = chariot.getStatus();
            // génération ICUGenChariot()
            String requete = "INSERT INTO CHARIOT (ICU, hauteur, largeur, longueur, poids_max, statut, ref_chariot_setrouve_id, ref_chariot_direction_id)"
                    + "values"
                    + "(?,?,?,?,?,?,?,?)";

            Connection connexion = null;
            PreparedStatement preparedStatement = null;
            GenerateICU gen = new GenerateICU();
            try {
                Class.forName( driver );
            } catch ( ClassNotFoundException e ) {
                throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
            }
            try {
                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                        true,
                        // je mets les chariots en descelles par defaut et à
                        // destination du centre ou ils se trouvent
                        gen.ICUGenChariot(), (dim.getUsefulheight()*100), (dim.getUsefulwidth()*100), (dim.getUsefullength()*100), 500,
                        0, idCentre, idCentre );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'un chariot, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }
        }
    }

    public static String HashPassword( String login, String password )
    {
        String sel = "jkjefçàç)àç&@fe5753453";

        String passwordHashed = login + password + sel;

        MessageDigest mdEncPassword;

        try
        {
            mdEncPassword = MessageDigest.getInstance( "SHA-256" );
            mdEncPassword.update( passwordHashed.getBytes( "UTF-8" ), 0, passwordHashed.length() );
            passwordHashed = new BigInteger( 1, mdEncPassword.digest() ).toString( 16 );

            System.out.println( login + " : " + passwordHashed );
        } catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        } catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return passwordHashed;
    }

    public static void main( String[] args ) {
        ParseurDCNew parseur = new ParseurDCNew();
        parseur.parse();
    }

}
