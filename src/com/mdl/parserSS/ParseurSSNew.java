package com.mdl.parserSS;

import static com.mdl.parserDC.DAOUtilitaire.fermeturesSilencieuses;
import static com.mdl.parserDC.DAOUtilitaire.initialisationRequetePreparee;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.dao.DAOException;
import com.mdl.parser.GenerateICU;
import com.mdl.parserDC.DAOConfigurationException;

public class ParseurSSNew {

    private ClientsType clients;

    private String      url            = "jdbc:oracle:thin:@st-oracle.info.fundp.ac.be:1521:xe";
    private String      driver         = "oracle.jdbc.driver.OracleDriver";
    private String      nomUtilisateur = "G3.HLB";
    private String      motDePasse     = "uuj2aepo";

    public ParseurSSNew() {
        try {
            JAXBContext ctx = JAXBContext.newInstance( ClientsType.class );
            Unmarshaller uu = ctx.createUnmarshaller();
            ObjectFactory factory = new ObjectFactory();
            clients = factory.createClientsType();
            clients = (ClientsType) uu.unmarshal( new File( "SS.xml" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void parse() {
        List<ClientType> listeClients = clients.getClient();

        for ( ClientType client : listeClients ) {
            AccountType compte = client.getAccount();
            AddressType adresse = client.getAddress();
            GregorianCalendar date_naissance = client.getBirthdate().toGregorianCalendar();
            String dateNaiss = date_naissance.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                    ( date_naissance.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                    date_naissance.get( GregorianCalendar.YEAR );
            String prenom = client.getFirstname();
            String nom = client.getLastname();
            String telephone = client.getPhone();
            ProfileType profil = client.getProfile();

            String email = compte.getEmail();
            String motPasse = compte.getPassword();
            GregorianCalendar dateReg = compte.getRegistrationdate().toGregorianCalendar();
            String dateEnr = dateReg.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                    ( dateReg.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                    dateReg.get( GregorianCalendar.YEAR );
            // boolean active = compte.isActivated();
            String boite = adresse.getBox();
            String localite = adresse.getCity();
            String pays = adresse.getCountry();
            String num = adresse.getNumber();
            String rue = adresse.getStreet();
            String codePostal = adresse.getZip();
            // telephone_fixe = null
            // num_tva = null

            String requete = "INSERT INTO CLIENT"
                    + "(nom, prenom, sexe, mot_de_passe, mail, adr_rue, adr_num, adr_boite, adr_code_postal, adr_localite"
                    + ",adr_pays, telephone_portable, date_naissance, validation, date_inscription, date_derniere_activite)"
                    + "VALUES"
                    + "(?,?,?,?,?,?,?,?,?,?"
                    + ",?,?,to_date(?,'dd/mm/yyyy'),?,to_date(?,'dd/mm/yyyy'),to_date(?,'dd/mm/yyyy'))";

            Connection connexion = null;
            PreparedStatement preparedStatement = null;
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            String myEncryptionPassword = "con/fir$mation d'1nscription";
            textEncryptor.setPassword( myEncryptionPassword );
            String myEncryptedValidation = textEncryptor.encrypt( "oui" );

            BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
            String myEncryptionPassword2 = "HLB-Express";
            textEncryptor2.setPassword( myEncryptionPassword2 );
            String plainText = textEncryptor2.encrypt( motPasse );

            try {
                Class.forName( driver );
            } catch ( ClassNotFoundException e ) {
                throw new DAOConfigurationException( "Le driver est introuvable dans le classpath.", e );
            }

            try {
                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                        true,
                        nom, prenom, "M", plainText, email, rue, num, boite, codePostal, localite,
                        pays, telephone, dateNaiss, myEncryptedValidation, dateEnr, "12/07/2012" );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'un client, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }

            int statutCli = -1;
            String tmp = profil.getType();
            if ( tmp.equals( "basic" ) ) {
                statutCli = 1;
            } else if ( tmp.equals( "advanced" ) ) {
                statutCli = 2;
            } else if ( tmp.equals( "premium" ) ) {
                statutCli = 3;
            } else {
                System.out.println( "Erreur: statut inconnu" );
            }
            // int defautsDePaiement = profil.getPayementdefaults();
            // float revenu = profil.getRevenue();
            // int colisEnvoyes = profil.getSentparcels();
            // int nbLitigesTort = profil.getWronglitiges();

            requete = "INSERT INTO STATUT_CLIENT "
                    + "(statut, date_nouveau_statut,ref_statut_client_id)"
                    + "values"
                    + "(?,to_date(?,'dd/mm/yyyy'),(SELECT identifiant FROM CLIENT WHERE mail = ?))";

            try {
                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                        true,
                        statutCli, dateEnr, email );

                int statut = preparedStatement.executeUpdate();
                /* Analyse du statut retourné par la requête d'insertion */
                if ( statut == 0 ) {
                    throw new DAOException(
                            "Echec de la création d'un statut, aucune ligne ajoutée dans la table." );
                }
            } catch ( SQLException e ) {
                throw new DAOException( e );
            } finally {
                fermeturesSilencieuses( preparedStatement, connexion );
            }

            if ( client.getPickups() != null ) {

                PickupsType pickups = client.getPickups(); // TODO

                List<PickupType> listePickups = pickups.getPickup();

                for ( PickupType pickup : listePickups ) {
                    // AddressType adresse_fact = pickup.getBillingaddress();
                    float prix = pickup.getChargedamount();
                    Dimension dimensions = pickup.getDeclareddimension();
                    AddressType adresseLivraison = pickup.getDeliveryaddress();
                    String nomCentreLivraison = pickup.getDeliverycenter();
                    DeliveryproblemType problemes = pickup.getDeliveryproblem();
                    InsuranceType assurance = pickup.getInsurance();
                    LitigeType litige = pickup.getLitige();
                    AddressType adressePickup = pickup.getPickupaddress();
                    String nomCentrePickup = pickup.getPickupcenter();
                    ReceiverType donneesDestinataire = pickup.getReceiver();
                    GregorianCalendar dateCommande = pickup.getRequestdate().toGregorianCalendar();
                    Dimension nouvDimensions = pickup.getReviseddimension();
                    boolean accuse = pickup.isReceipt();

                    requete = "INSERT INTO COMMANDE"
                            + "(adr_pick_nom, adr_pick_prenom, adr_pick_rue, adr_pick_num, adr_pick_boite, adr_pick_code_postal"
                            + ",adr_pick_localite, adr_pick_pays, telephone_expediteur, adr_dest_nom, adr_dest_prenom, adr_dest_rue"
                            + ",adr_dest_num, adr_dest_boite, adr_dest_code_postal, adr_dest_localite, adr_dest_pays, telephone_destinataire"
                            + ",date_pickup, prix, frais_port, statut_compte, ref_client_id, ref_centre_source_id, ref_centre_dest_id, date_enregistrement)"
                            + "VALUES"
                            + "(?,?,?,?,?,?"
                            + ",?,?,?,?,?,?"
                            + ",?,?,?,?,?,?"
                            + ",to_date(?,'dd/mm/yyyy'),?,?,?,(SELECT identifiant FROM CLIENT WHERE mail = ?),?,?,to_date(?,'dd/mm/yyyy'))";
                    String pickRue = adressePickup.getStreet();
                    String pickNum = adressePickup.getNumber();
                    String pickBoite = adressePickup.getBox();
                    String pickCode = adressePickup.getZip();
                    String pickLocalite = adressePickup.getCity();
                    String pickPays = adressePickup.getCountry();
                    String destNom = donneesDestinataire.getLastname();
                    String destPrenom = donneesDestinataire.getFirstname();
                    String destRue = adresseLivraison.getStreet();
                    String destNum = adresseLivraison.getNumber();
                    String destBoite = adresseLivraison.getBox();
                    String destCode = adresseLivraison.getZip();
                    String destLocalite = adresseLivraison.getCity();
                    String destPays = adresseLivraison.getCountry();
                    String destTelephone = donneesDestinataire.getPhone();
                    String dateCom = dateCommande.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                            ( dateCommande.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                            dateCommande.get( GregorianCalendar.YEAR );
                    int centreSource = trouverIDCentre( nomCentrePickup );
                    int centreDest = trouverIDCentre( nomCentreLivraison );

                    try {
                        /* Récupération d'une connexion depuis la Factory */
                        connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                        preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                                true,
                                nom, prenom, pickRue, pickNum, pickBoite, pickCode
                                , pickLocalite, pickPays, telephone, destNom, destPrenom, destRue
                                , destNum, destBoite, destCode, destLocalite, destPays, destTelephone
                                , dateCom, prix, 0, statutCli, email, centreSource, centreDest, dateCom );

                        int statut = preparedStatement.executeUpdate();
                        /* Analyse du statut retourné par la requête d'insertion */
                        if ( statut == 0 ) {
                            throw new DAOException(
                                    "Echec de la création d'une commande, aucune ligne ajoutée dans la table." );
                        }
                    } catch ( SQLException e ) {
                        throw new DAOException( e );
                    } finally {
                        fermeturesSilencieuses( preparedStatement, connexion );
                    }

                    requete = "INSERT INTO COLIS"
                            + "(ICU, poids_renseigne,longueur,largeur,hauteur,"
                            + "valeur_estimee, accuse_reception, ref_commande_id, ref_assurance_id)"
                            + "values"
                            + "(?,?,?,?,?,"
                            + "?,?,(select max(identifiant) from commande),?)";
                    String icu = ( new GenerateICU() ).ICUGenColis();
                    int hauteur = dimensions.getHeight();
                    int longueur = dimensions.getLength();
                    int poids = dimensions.getWeight();
                    int largeur = dimensions.getWidth();
                    float prixEstime = assurance.getAmount();
                    int accuseNb = 0;
                    if ( accuse ) {
                        accuseNb = 1;
                    } else if ( !accuse ) {
                        accuseNb = 0;
                    }
                    int assuranceNb = 3;
                    if ( assurance.getType().equals( "forfait" ) ) {
                        assuranceNb = 1;
                    } else if ( assurance.getType().equals( "montant" ) ) {
                        assuranceNb = 2;
                    }
                    try {
                        /* Récupération d'une connexion depuis la Factory */
                        connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                        preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                                true,
                                icu, poids, longueur, largeur, hauteur,
                                prixEstime, accuseNb, assuranceNb );

                        int statut = preparedStatement.executeUpdate();
                        /* Analyse du statut retourné par la requête d'insertion */
                        if ( statut == 0 ) {
                            throw new DAOException(
                                    "Echec de la création d'un colis, aucune ligne ajoutée dans la table." );
                        }
                    } catch ( SQLException e ) {
                        throw new DAOException( e );
                    } finally {
                        fermeturesSilencieuses( preparedStatement, connexion );
                    }

                    // en cas de surfacturation
                    if ( pickup.getExtranotification() != null ) {
                        requete = "INSERT INTO SURFACTURATION"
                                + "(poids_reel, difference_prix,ref_colis_id,date_notification,date_paiement_effectif)"
                                + "values"
                                + "(?,?,(select max(identifiant) from colis),to_date(?,'dd/mm/yyyy'),null)";

                        GregorianCalendar dateNotification = pickup.getExtranotification().toGregorianCalendar();
                        float surfacturation = pickup.getExtraamount();
                        int nouvPoids = nouvDimensions.getWeight();
                        String dateSur = dateNotification.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                                ( dateNotification.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                                dateNotification.get( GregorianCalendar.YEAR );

                        try {
                            /* Récupération d'une connexion depuis la Factory */
                            connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                                    true,
                                    nouvPoids, surfacturation, dateSur );

                            int statut = preparedStatement.executeUpdate();
                            /*
                             * Analyse du statut retourné par la requête
                             * d'insertion
                             */
                            if ( statut == 0 ) {
                                throw new DAOException(
                                        "Echec de la création d'une surfacturation, aucune ligne ajoutée dans la table." );
                            }
                        } catch ( SQLException e ) {
                            throw new DAOException( e );
                        } finally {
                            fermeturesSilencieuses( preparedStatement, connexion );
                        }
                    }

                    // en cas de litige
                    if ( litige != null ) {
                        requete = "INSERT INTO LITIGE"
                                + "(type,autre, responsabilite_client,ref_colis_id,date_creation)"
                                + "VALUES"
                                + "(?,'neant',?,(select max(identifiant) from colis),to_date(?,'dd/mm/yyyy'))";
                        GregorianCalendar dateLitige = litige.getDate().toGregorianCalendar();
                        String sDateLitige = dateLitige.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                                ( dateLitige.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                                dateLitige.get( GregorianCalendar.YEAR );
                        int typeLitige = 1;
                        if ( litige.getReason().equals( "loss" ) ) {
                            typeLitige = 2;
                        } else if ( litige.getReason().equals( "deterioration" ) ) {
                            typeLitige = 1;
                        } else if ( litige.getReason().equals( "other" ) ) {
                            typeLitige = 5;
                        } else {
                            System.out.println( "type litige inconnu" );
                        }
                        int responsabiliteClient = 0;
                        if ( litige.getResult().equals( "wrong" ) ) {
                            responsabiliteClient = 1;
                        } else if ( litige.getResult().equals( "justified" ) ) {
                            responsabiliteClient = 0;
                        } else {
                            System.out.println( "Erreur sur le statut du litige" );
                        }
                        try {
                            /* Récupération d'une connexion depuis la Factory */
                            connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                                    true, typeLitige, responsabiliteClient, sDateLitige
                                    );

                            int statut = preparedStatement.executeUpdate();
                            /*
                             * Analyse du statut retourné par la requête
                             * d'insertion
                             */
                            if ( statut == 0 ) {
                                throw new DAOException(
                                        "Echec de la création d'un litige, aucune ligne ajoutée dans la table." );
                            }
                        } catch ( SQLException e ) {
                            throw new DAOException( e );
                        } finally {
                            fermeturesSilencieuses( preparedStatement, connexion );
                        }

                        // ajout dans operation_litige pour le statut du litige
                        requete = "INSERT INTO OPERATION_LITIGE"
                                + "(type,statut,ref_litige_id,date_operation)"
                                + "VALUES"
                                + "(2,?,(select max(identifiant) from litige),to_date(?,'dd/mm/yyyy'))";
                        // 1 ouvert,3 en cours ,4 fermé sDateLitige
                        int litStatut = 0;
                        if ( litige.getStatus().equals( "open" ) ) {
                            litStatut = 1;
                        } else if ( litige.getStatus().equals( "in-negociation" ) ) {
                            litStatut = 3;
                        } else if ( litige.getStatus().equals( "closed" ) ) {
                            litStatut = 4;
                        } else {
                            System.out.println( "Erreur, statut litige inconnu" );
                        }
                        try {
                            /* Récupération d'une connexion depuis la Factory */
                            connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                            preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion, requete,
                                    true, litStatut, sDateLitige
                                    );

                            int statut = preparedStatement.executeUpdate();
                            /*
                             * Analyse du statut retourné par la requête
                             * d'insertion
                             */
                            if ( statut == 0 ) {
                                throw new DAOException(
                                        "Echec de la création d'un litige, aucune ligne ajoutée dans la table operation_litige." );
                            }
                        } catch ( SQLException e ) {
                            throw new DAOException( e );
                        } finally {
                            fermeturesSilencieuses( preparedStatement, connexion );
                        }

                    }

                    // en cas de probleme
                    if ( problemes != null ) {
                        String mailCoursier = problemes.getCoursier() + "@hlb-express.be";
                        String noteProbleme = problemes.getDescription();
                        GregorianCalendar dateNotifProb = problemes.getDate().toGregorianCalendar();
                        String sDateNotifProb = dateNotifProb.get( GregorianCalendar.DAY_OF_MONTH ) + "-" +
                                ( dateNotifProb.get( GregorianCalendar.MONTH ) + 1 ) + "-" +
                                dateNotifProb.get( GregorianCalendar.YEAR );
                        if ( problemes.getStatus().equals( "transformedtolitige" ) ) {
                            requete = "INSERT INTO NOTIFICATION_PROBLEME"
                                    + "(type,note,ref_not_coursier_id,ref_notification_litige_id,ref_notification_colis_id,date_creation,verrou)"
                                    + "values"
                                    + "(5,?,(select identifiant from coursier where mail = ?),(select max(identifiant) from litige),(select max(identifiant) from colis),to_date(?,'dd/mm/yyyy'),2)";
                            try {
                                /*
                                 * Récupération d'une connexion depuis la
                                 * Factory
                                 */
                                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                                        requete,
                                        true,
                                        noteProbleme, mailCoursier, sDateNotifProb );

                                int statut = preparedStatement.executeUpdate();
                                /*
                                 * Analyse du statut retourné par la requête
                                 * d'insertion
                                 */
                                if ( statut == 0 ) {
                                    throw new DAOException(
                                            "Echec de la création d'une surfacturation, aucune ligne ajoutée dans la table." );
                                }
                            } catch ( SQLException e ) {
                                throw new DAOException( e );
                            } finally {
                                fermeturesSilencieuses( preparedStatement, connexion );
                            }
                        } else {
                            requete = "INSERT INTO NOTIFICATION_PROBLEME"
                                    + "(type,note,ref_not_coursier_id,ref_notification_colis_id,date_creation,verrou)"
                                    + "values"
                                    + "(5,?,(select id from coursier where mail = ?),(select max(identifiant) from colis),to_date(?,'dd/mm/yyyy'),2)";
                            try {
                                /*
                                 * Récupération d'une connexion depuis la
                                 * Factory
                                 */
                                connexion = DriverManager.getConnection( url, nomUtilisateur, motDePasse );

                                preparedStatement = (PreparedStatement) initialisationRequetePreparee( connexion,
                                        requete,
                                        true,
                                        noteProbleme, mailCoursier, sDateNotifProb );

                                int statut = preparedStatement.executeUpdate();
                                /*
                                 * Analyse du statut retourné par la requête
                                 * d'insertion
                                 */
                                if ( statut == 0 ) {
                                    throw new DAOException(
                                            "Echec de la création d'une surfacturation, aucune ligne ajoutée dans la table." );
                                }
                            } catch ( SQLException e ) {
                                throw new DAOException( e );
                            } finally {
                                fermeturesSilencieuses( preparedStatement, connexion );
                            }
                        }

                    }
                }

            }

            // TODO

        }
    }

    public static void main( String[] args ) {
        ParseurSSNew parseur = new ParseurSSNew();
        parseur.parse();
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
            return 0;
        }
    }
}
