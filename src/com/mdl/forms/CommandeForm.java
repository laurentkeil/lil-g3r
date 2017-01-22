package com.mdl.forms;

import hlbexpress.routing.facade.RoutingCommande;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import com.mdl.beans.Commande;
import com.mdl.beans.Utilisateur;
import com.mdl.dao.UtilisateurDAO;

/**
 * 
 * Classe de vérification du formulaire, calcul de la tarification, messages
 * d'erreurs et création de la commande dans le bean si aucune erreur.
 * 
 */
public class CommandeForm {
    /**
     * Déclaration des constantes pour la récupération des champs
     */
    public static final String  ATT_SESSION_USER     = "sessionUtilisateur";
    public static final String  ATT_CONFIRM          = "confirm";
    private static final String CHAMP_DATE_COMMANDE  = "datePickup";
    private static final String CHAMP_MOIS_COMMANDE  = "moisCommande";
    private static final String CHAMP_ANNEE_COMMANDE = "anneeCommande";
    private static final String CHAMP_VALEUR         = "valeurEstimee";
    private static final String CHAMP_ADRDEFAUT      = "AdresseDefaut";

    private static final String CHAMP_NOMEXP         = "nom_expediteur";
    private static final String CHAMP_PRENOMEXP      = "prenom_expediteur";
    private static final String CHAMP_TELEXP         = "tel_expediteur";
    private static final String CHAMP_NUMEXP         = "adresse_num_expediteur";
    private static final String CHAMP_RUEEXP         = "adresse_rue_expediteur";
    private static final String CHAMP_LOCEXP         = "adresse_loc_expediteur";
    private static final String CHAMP_CODEEXP        = "adresse_code_expediteur";
    private static final String CHAMP_BOITEEXP       = "adresse_boite_expediteur";
    private static final String CHAMP_ADDADREXP      = "ajoutAdrExp";

    private static final String CHAMP_NOMDEST        = "nom_destinataire";
    private static final String CHAMP_PRENOMDEST     = "prenom_destinataire";
    private static final String CHAMP_TELDEST        = "tel_destinataire";
    private static final String CHAMP_PAYSDEST       = "adresse_pays_destinataire";
    private static final String CHAMP_NUMDEST        = "adresse_num_destinataire";
    private static final String CHAMP_RUEDEST        = "adresse_rue_destinataire";
    private static final String CHAMP_LOCDEST        = "adresse_loc_destinataire";
    private static final String CHAMP_CODEDEST       = "adresse_code_destinataire";
    private static final String CHAMP_BOITEDEST      = "adresse_boite_destinataire";
    private static final String CHAMP_ADDADRDEST     = "ajoutAdrDest";

    private static final String CHAMP_POIDS          = "poids";
    private static final String CHAMP_HAUTEUR        = "dimension_hauteur";
    private static final String CHAMP_LONGUEUR       = "dimension_longueur";
    private static final String CHAMP_LARGEUR        = "dimension_largeur";
    private static final String CHAMP_TYPEASSURANCE  = "typeAssurance";
    private static final String CHAMP_ACCUSE         = "accuseReception";

    
    private static final String FICHIER_PROPERTIES         = "/com/mdl/config/configuration.properties";
    private static final String PROPERTY_PRIX_BEL_BAS      = "prix_bel_bas";
    private static final String PROPERTY_PRIX_AUTRE        = "prix_autre";
    private static final String PROPERTY_PRIX_INT_HAUT 	   = "prix_int_haut";
    private static final String PROPERTY_PRIX_CANTON       = "prix_canton";
    private static final String PROPERTY_PRIX_FORFAIT  	   = "prix_forfait";
    private static final String PROPERTY_PRIX_MONTANT_BAS  = "prix_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_BAS   = "pct_montant_bas";
    private static final String PROPERTY_PCT_MONTANT_HAUT  = "pct_montant_haut";
    private static final String PROPERTY_PRIX_SANS_ASS     = "prix_sans_ass";
    private static final String PROPERTY_PRIX_ACC     	   = "prix_acc";
    private static final String PROPERTY_PRIX_SANS_ACC     = "prix_sans_ass";
    private static final String PROPERTY_POIDS_BASE        = "poids_base";
    private static final String PROPERTY_VALEUR_MONTANT    = "valeur_montant";
    private static final String PROPERTY_NB_CANTON_MAX	   = "nb_canton_max";
    /**
     * déclaration du résultat à afficher et de la map d'erreur contenant toutes
     * les erreurs dans les champs
     */
    private String              resultat;
    private Map<String, String> erreurs              = new HashMap<String, String>();

    /**
     * déclaration de l'objet servant à récupérer en base de donnée les
     * informations disponibles sur l'utilisateur connecté.
     */
    private UtilisateurDAO      utilisateurDao;

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    /**
     * Constructeur qui initialise l'objet DAO pour la récupération des
     * informations en BD
     */
    public CommandeForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    /**
     * Création d'une commande après vérification de chacun des champs
     */
    public Commande creerCommande( HttpServletRequest request, HttpSession session ) {

        /**
         * Construit l'algo de routage
         */
        RoutingCommande routing = new RoutingCommande();

        /**
         * Récupération de la valeur de chaque champs dans la requête reçue.
         */
        String datePickup = getValeurChamp( request, CHAMP_DATE_COMMANDE );
        String valeur = getValeurChamp( request, CHAMP_VALEUR );
        String adrDefaut = getValeurChamp( request, CHAMP_ADRDEFAUT );

        String nomExp = getValeurChamp( request, CHAMP_NOMEXP );
        String prenomExp = getValeurChamp( request, CHAMP_PRENOMEXP );
        String telExp = getValeurChamp( request, CHAMP_TELEXP );
        String numExp = getValeurChamp( request, CHAMP_NUMEXP );
        String rueExp = getValeurChamp( request, CHAMP_RUEEXP );
        String locExp = getValeurChamp( request, CHAMP_LOCEXP );
        String codeExp = getValeurChamp( request, CHAMP_CODEEXP );
        String boiteExp = getValeurChamp( request, CHAMP_BOITEEXP );
        String addAdrExp = getValeurChamp( request, CHAMP_ADDADREXP );

        String nomDest = getValeurChamp( request, CHAMP_NOMDEST );
        String prenomDest = getValeurChamp( request, CHAMP_PRENOMDEST );
        String telDest = getValeurChamp( request, CHAMP_TELDEST );
        String paysDest = getValeurChamp( request, CHAMP_PAYSDEST );
        String numDest = getValeurChamp( request, CHAMP_NUMDEST );
        String rueDest = getValeurChamp( request, CHAMP_RUEDEST );
        String locDest = getValeurChamp( request, CHAMP_LOCDEST );
        String codeDest = getValeurChamp( request, CHAMP_CODEDEST );
        String boiteDest = getValeurChamp( request, CHAMP_BOITEDEST );
        String addAdrDest = getValeurChamp( request, CHAMP_ADDADRDEST );

        String poids = getValeurChamp( request, CHAMP_POIDS );
        String hauteur = getValeurChamp( request, CHAMP_HAUTEUR );
        String longueur = getValeurChamp( request, CHAMP_LONGUEUR );
        String largeur = getValeurChamp( request, CHAMP_LARGEUR );
        String typeAssurance = getValeurChamp( request, CHAMP_TYPEASSURANCE );
        String accuse = getValeurChamp( request, CHAMP_ACCUSE );

        Commande commande = new Commande();

        /**
         * Traitement de chaque champ récupéré pour le valider ou y ajouter une
         * erreur. Création de la commande dans le bean Commande.
         */
        try {

            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
            Date dn = new Date(); // récupération de la date
            dn = sdf.parse( datePickup );
            Integer jour = dn.getDate();
            Integer mois = dn.getMonth() + 1;
            Integer annee = dn.getYear() + 1900;
            traiterDate( jour.toString(), mois.toString(), annee.toString(), commande );

            int jourPick = Integer.parseInt( commande.getJourCommande() );
            int moisPick = Integer.parseInt( commande.getMoisCommande() );
            int anneePick = Integer.parseInt( commande.getAnneeCommande() );
            Calendar d = new GregorianCalendar( anneePick, ( moisPick - 1 ), jourPick );
            java.sql.Date date = new java.sql.Date( d.getTimeInMillis() );
            commande.setDatePickup( date );

            traiterValeur( valeur, commande );

            /**
             * récupération de l'utilisateur dans la base de donnée grâce à son
             * mail récupéré en session
             */
            String mail = (String) session.getAttribute( ATT_SESSION_USER );
            Utilisateur utilisateur = utilisateurDao.trouver( mail );

            /**
             * Si l'utilisateur choisit l'adresse par défaut (expéditeur), les
             * erreurs sont déjà traités donc on accède au bean utilisateur
             * récupéré grâce à l'accès BD et placement des informations dans le
             * bean commande.
             */
            if ( adrDefaut.equals( "Default" ) ) {
                traiterPaysExp( utilisateur.getAdressePays() );
                commande.setNom_expediteur( utilisateur.getNom() );
                commande.setPrenom_expediteur( utilisateur.getPrenom() );
                commande.setTel_expediteur( utilisateur.getTelephonePortable() );
                commande.setAdresse_num_expediteur( utilisateur.getAdresseNum() );
                commande.setAdresse_rue_expediteur( utilisateur.getAdresseRue() );
                commande.setAdresse_loc_expediteur( utilisateur.getAdresseLoc() );
                commande.setAdresse_code_expediteur( utilisateur.getAdresseCode() );
                try {
                    commande.setCentre_exp_id( getIdCanton( "Belgique",
                            Integer.parseInt( commande.getAdresse_code_expediteur() ) ) );
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                commande.setAdresse_boite_expediteur( utilisateur.getAdresseBoite() );
            } else {
                traiterNomExp( nomExp, commande );
                traiterPrenomExp( prenomExp, commande );
                traiterTelExp( telExp, commande );
                traiterNumExp( numExp, commande );
                traiterRueExp( rueExp, commande );
                traiterLocExp( locExp, commande );
                traiterCodeExp( codeExp, commande );
                commande.setAdresse_boite_expediteur( boiteExp );
            }

            traiterNomDest( nomDest, commande );
            traiterPrenomDest( prenomDest, commande );
            traiterTelDest( telDest, commande );
            traiterPaysDest( paysDest, commande );
            traiterNumDest( numDest, commande );
            traiterRueDest( rueDest, commande );
            traiterLocDest( locDest, commande );
            traiterCodeDest( codeDest, commande );

            traiterPoids( poids, commande );
            traiterDimension( hauteur, longueur, largeur, commande );

            commande.setAdresse_boite_destinataire( boiteDest );
            commande.setTypeAssurance( typeAssurance );
            commande.setAccuseReception( Boolean.parseBoolean( accuse ) );

            /**
             * Mise en place des informations pour le routing
             */
            String cantonExp = null;
            String cantonDest = null;
            if ( commande.getAdresse_code_expediteur() != null && commande.getAdresse_code_destinataire() != null ) {
                try {
                    cantonExp = getCanton( "Belgique", Integer.parseInt( commande.getAdresse_code_expediteur() ) );
                } catch ( FormValidationException e ) {
                    setErreur( CHAMP_CODEEXP, e.getMessage() );
                }
                try {
                    cantonDest = getCanton( commande.getAdresse_pays_destinataire(),
                            Integer.parseInt( commande.getAdresse_code_destinataire() ) );
                } catch ( FormValidationException e ) {
                    setErreur( CHAMP_CODEDEST, e.getMessage() );
                }
            }

            /**
             * Récupération du nombre de centre traversé (accès au Webservice de
             * routage)
             */
            Integer nbCanton = routing.getNbCanton( cantonExp, cantonDest );

            /**
             * si la case est cochée, on met un booléen à true qui permettra
             * d'enregistrer l'adresse
             */
            if ( addAdrExp != null ) {
                commande.setAdd_carnet_exp( true );
            }
            if ( addAdrDest != null ) {
                commande.setAdd_carnet_dest( true );
            }

            /**
             * récupération d'un résultat suivant les erreurs dans l'incription
             * du formulaire de commande.
             */
            if ( erreurs.isEmpty() ) {
                traiterPrix( commande, nbCanton );
                resultat = "Succès de la création de la commande.";
            } else {
                resultat = "Echec de la création de la commande.";
            }
        } catch ( Exception e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la création." );
            resultat = "Echec de la création de la commande : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return commande;
    }

    /**
     * traite le prix pour la facturation. Prix de base base en fonction du pays
     * et du poids +prix en fonction du nombre de centres traversés +prix en
     * fonction de l'assurance +prix si accusé
     * @throws Exception 
     */
    private void traiterPrix( Commande commande, Integer nbCanton ) throws Exception {
    	
    	// Permet d'aller rechercher les informations dans le fichier de configuration
   	 	Properties properties = new Properties();
        String CHAMP_PRIX_BEL_BAS;
        String CHAMP_PRIX_AUTRE;
        String CHAMP_PRIX_INT_HAUT;
        String CHAMP_PRIX_CANTON;
        String CHAMP_PRIX_FORFAIT;
        String CHAMP_PRIX_MONTANT_BAS;
        String CHAMP_PCT_MONTANT_BAS;
        String CHAMP_PCT_MONTANT_HAUT;
        String CHAMP_PRIX_SANS_ASS;
        String CHAMP_PRIX_ACC;
        String CHAMP_PRIX_SANS_ACC;
        String CHAMP_POIDS_BASE;
        String CHAMP_VALEUR_MONTANT;
        String CHAMP_NB_CANTON_MAX;

        double PRIX_BEL_BAS;
        double PRIX_AUTRE;
        double PRIX_INT_HAUT;
        double PRIX_CANTON;
        double PRIX_FORFAIT;
        double PRIX_MONTANT_BAS;
        double PCT_MONTANT_BAS;
        double PCT_MONTANT_HAUT;
        double PRIX_SANS_ASS;
        double PRIX_ACC;
        double PRIX_SANS_ACC;
        double POIDS_BASE;
        double VALEUR_MONTANT;
        double NB_CANTON_MAX;
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );

        if ( fichierProperties == null ) {
            throw new Exception( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            CHAMP_PRIX_BEL_BAS = properties.getProperty( PROPERTY_PRIX_BEL_BAS );
            CHAMP_PRIX_AUTRE = properties.getProperty( PROPERTY_PRIX_AUTRE );
            CHAMP_PRIX_INT_HAUT = properties.getProperty( PROPERTY_PRIX_INT_HAUT );
            CHAMP_PRIX_CANTON = properties.getProperty( PROPERTY_PRIX_CANTON );
            CHAMP_PRIX_FORFAIT = properties.getProperty( PROPERTY_PRIX_FORFAIT );
            CHAMP_PRIX_MONTANT_BAS = properties.getProperty( PROPERTY_PRIX_MONTANT_BAS );
            CHAMP_PCT_MONTANT_BAS = properties.getProperty( PROPERTY_PCT_MONTANT_BAS );
            CHAMP_PCT_MONTANT_HAUT = properties.getProperty( PROPERTY_PCT_MONTANT_HAUT );
            CHAMP_PRIX_SANS_ASS = properties.getProperty( PROPERTY_PRIX_SANS_ASS );
            CHAMP_PRIX_ACC = properties.getProperty( PROPERTY_PRIX_ACC );
            CHAMP_PRIX_SANS_ACC = properties.getProperty( PROPERTY_PRIX_SANS_ACC );
            CHAMP_POIDS_BASE    = properties.getProperty( PROPERTY_POIDS_BASE );
            CHAMP_VALEUR_MONTANT    = properties.getProperty( PROPERTY_VALEUR_MONTANT );
            CHAMP_NB_CANTON_MAX	    = properties.getProperty( PROPERTY_NB_CANTON_MAX );
            
            PRIX_BEL_BAS  = Double.parseDouble(CHAMP_PRIX_BEL_BAS);
            PRIX_AUTRE    = Double.parseDouble(CHAMP_PRIX_AUTRE);
            PRIX_INT_HAUT = Double.parseDouble(CHAMP_PRIX_INT_HAUT);
            PRIX_CANTON   = Double.parseDouble(CHAMP_PRIX_CANTON);
            PRIX_FORFAIT  = Double.parseDouble(CHAMP_PRIX_FORFAIT);
            PRIX_MONTANT_BAS   = Double.parseDouble(CHAMP_PRIX_MONTANT_BAS);
            PCT_MONTANT_BAS    = Double.parseDouble(CHAMP_PCT_MONTANT_BAS);
            PCT_MONTANT_HAUT   = Double.parseDouble(CHAMP_PCT_MONTANT_HAUT);
            PRIX_SANS_ASS      = Double.parseDouble(CHAMP_PRIX_SANS_ASS);
            PRIX_ACC	       = Double.parseDouble(CHAMP_PRIX_ACC);
            PRIX_SANS_ACC	   = Double.parseDouble(CHAMP_PRIX_SANS_ACC);
            POIDS_BASE 		   = Double.parseDouble(CHAMP_POIDS_BASE);
            VALEUR_MONTANT	   = Double.parseDouble(CHAMP_VALEUR_MONTANT);
            NB_CANTON_MAX	   = Integer.parseInt(CHAMP_NB_CANTON_MAX);
            
        } catch ( IOException e ) {
            throw new Exception( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }
    	
        if ( commande.getAdresse_pays_destinataire().equals( "Belgique" ) && commande.getPoids() <= POIDS_BASE ) {
            commande.setPrixBase( PRIX_BEL_BAS );
        } else if ( !commande.getAdresse_pays_destinataire().equals( "Belgique" ) && commande.getPoids() > POIDS_BASE ) {
            commande.setPrixBase( PRIX_INT_HAUT );
        } else {
            commande.setPrixBase( PRIX_AUTRE );
        }

        // {min entre centre traversés et 4}
        if ( nbCanton >= NB_CANTON_MAX ) {
            commande.setPrixCentreTraverse( NB_CANTON_MAX * PRIX_CANTON );
        } else {
            commande.setPrixCentreTraverse( (double) ( nbCanton * PRIX_CANTON ) );
        }

        if ( commande.getTypeAssurance().equals( "forfait" ) ) {
            commande.setPrixAssurance( PRIX_FORFAIT );
        } else if ( commande.getTypeAssurance().equals( "montant" ) && commande.getValeurEstimee() < VALEUR_MONTANT ) {
            commande.setPrixAssurance( PRIX_MONTANT_BAS + PCT_MONTANT_BAS * commande.getValeurEstimee() );
        } else if ( commande.getTypeAssurance().equals( "montant" ) && commande.getValeurEstimee() >= VALEUR_MONTANT ) {
            commande.setPrixAssurance( PCT_MONTANT_HAUT * commande.getValeurEstimee() );
        } else {
            commande.setPrixAssurance( PRIX_SANS_ASS );
        }

        if ( commande.getAccuseReception() ) {
            commande.setPrixAccuse( PRIX_ACC );
        } else {
            commande.setPrixAccuse( PRIX_SANS_ACC );
        }

        commande.setPrix( commande.getPrixBase() + commande.getPrixCentreTraverse() + commande.getPrixAssurance()
                + commande.getPrixAccuse() );
    }

    /**
     * Traitement des champs encodés par l'utilisateur et renvoit des exceptions
     * en les placant dans un map d'erreur pour les afficher à l'utilisateur.
     * Placement dans le bean commande si aucune erreur.
     */
    private void traiterDate( String jour, String mois, String annee, Commande commande ) {
        try {
            validationDate( jour, mois, annee );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_DATE_COMMANDE, e.getMessage() );
        }
        /**
         * Inscrit la date du lendemain par défaut
         * */
        if ( ( jour == null ) || ( mois == null ) || ( annee == null ) ) {
            Calendar auj = new GregorianCalendar();
            Integer ann = auj.get( Calendar.YEAR );
            Integer m = auj.get( Calendar.MONTH ) + 1;
            Integer j = auj.get( Calendar.DAY_OF_MONTH ) + 1;

            Calendar date = new GregorianCalendar( ann, m, j );

            /*
             * Si le jour du lendemain n'est plus dans le meme mois etc...
             */
            if ( ( ( j > 30 ) && ( ( m == 3 ) || ( m == 5 ) || ( m == 9 ) || ( m == 11 ) ) )
                    || ( ( j > 29 ) && ( m == 2 ) && ( ( (GregorianCalendar) date ).isLeapYear( ann ) ) )
                    || ( ( j > 28 ) && ( m == 2 ) && !( ( (GregorianCalendar) date ).isLeapYear( ann ) ) ) || ( j > 31 ) ) {
                j = 1;
                m = m + 1;
                if ( m > 12 ) {
                    m = 1;
                    ann = ann + 1;
                }
            }

            jour = j.toString();
            mois = m.toString();
            annee = ann.toString();
        }

        commande.setJourCommande( jour );
        commande.setMoisCommande( mois );
        commande.setAnneeCommande( annee );
    }

    private void traiterValeur( String valeur, Commande commande ) {
        double valeurEstimee = 0;
        try {
            valeurEstimee = validationValeur( valeur );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_VALEUR, e.getMessage() );
        }
        commande.setValeurEstimee( valeurEstimee );
    }

    private void traiterPaysExp( String paysExp ) {
        try {
            if ( !paysExp.equals( "Belgique" ) ) {
                throw new FormValidationException( "L'expéditeur doit résider en Belgique." );
            }
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ADRDEFAUT, e.getMessage() );
        }
    }

    private void traiterNomExp( String nomExp, Commande commande ) {
        try {
            validationChamp( nomExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOMEXP, e.getMessage() );
        }
        commande.setNom_expediteur( nomExp );
    }

    private void traiterPrenomExp( String prenomExp, Commande commande ) {
        try {
            validationChamp( prenomExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PRENOMEXP, e.getMessage() );
        }
        commande.setPrenom_expediteur( prenomExp );
    }

    private void traiterTelExp( String telExp, Commande commande ) {
        try {
            validationTelephoneExp( telExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_TELEXP, e.getMessage() );
        }
        commande.setTel_expediteur( telExp );
    }

    private void traiterNumExp( String numExp, Commande commande ) {
        try {
            validationCode( numExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NUMEXP, e.getMessage() );
        }
        commande.setAdresse_num_expediteur( numExp );
    }

    private void traiterRueExp( String rueExp, Commande commande ) {
        try {
            validationChamp( rueExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_RUEEXP, e.getMessage() );
        }
        commande.setAdresse_rue_expediteur( rueExp );
    }

    private void traiterLocExp( String locExp, Commande commande ) {
        try {
            validationChamp( locExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_LOCEXP, e.getMessage() );
        }
        commande.setAdresse_loc_expediteur( locExp );
    }

    private void traiterCodeExp( String codeExp, Commande commande ) throws NumberFormatException {
        try {
            validationCode( codeExp );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_CODEEXP, e.getMessage() );
        }
        commande.setAdresse_code_expediteur( codeExp );
        /**
         * crée le centre canton correspondant au code postal
         */
        if ( codeExp != null ) {
            try {
                commande.setCentre_exp_id( getIdCanton( "Belgique",
                        Integer.parseInt( codeExp ) ) );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_CODEEXP, e.getMessage() );
            }
        }
        /**
         * Vérifie la correspondance entre le code postal, le pays et la ville
         * de l'expéditeur
         * 
         * try { List<String> listeVilles = getListesVilles( commande, "BE",
         * codeExp ); Iterator i = listeVilles.iterator(); boolean
         * villeCorrespond = false; while ( i.hasNext() && !villeCorrespond ) {
         * String ville = (String) i.next(); villeCorrespond = ville.equals(
         * commande.getAdresse_loc_expediteur() ); } if ( !villeCorrespond ) {
         * setErreur( CHAMP_LOCEXP, "La ville ne correspond pas au code postal "
         * + codeExp ); } } catch ( Exception e ) { e.printStackTrace(); }
         */
    }

    private void traiterNomDest( String nomDest, Commande commande ) {
        try {
            validationChamp( nomDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOMDEST, e.getMessage() );
        }
        commande.setNom_destinataire( nomDest );
    }

    private void traiterPrenomDest( String prenomDest, Commande commande ) {
        try {
            validationChamp( prenomDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PRENOMDEST, e.getMessage() );
        }
        commande.setPrenom_destinataire( prenomDest );
    }

    private void traiterTelDest( String telDest, Commande commande ) {
        try {
            validationTelephone( telDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_TELDEST, e.getMessage() );
        }
        commande.setTel_destinataire( telDest );
    }

    private void traiterPaysDest( String paysDest, Commande commande ) {
        try {
            validationChamp( paysDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PAYSDEST, e.getMessage() );
        }
        commande.setAdresse_pays_destinataire( paysDest );
    }

    private void traiterNumDest( String numDest, Commande commande ) {
        try {
            validationCode( numDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NUMDEST, e.getMessage() );
        }
        commande.setAdresse_num_destinataire( numDest );
    }

    private void traiterRueDest( String rueDest, Commande commande ) {
        try {
            validationChamp( rueDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_RUEDEST, e.getMessage() );
        }
        commande.setAdresse_rue_destinataire( rueDest );
    }

    private void traiterLocDest( String locDest, Commande commande ) {
        try {
            validationChamp( locDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_LOCDEST, e.getMessage() );
        }
        commande.setAdresse_loc_destinataire( locDest );
    }

    private void traiterCodeDest( String codeDest, Commande commande ) throws NumberFormatException {
        try {
            validationCode( codeDest );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_CODEDEST, e.getMessage() );
        }
        commande.setAdresse_code_destinataire( codeDest );
        /**
         * crée le centre canton correspondant au code postal
         */
        if ( codeDest != null ) {
            try {
                commande.setCentre_dest_id( getIdCanton( commande.getAdresse_pays_destinataire(),
                        Integer.parseInt( codeDest ) ) );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_CODEDEST, e.getMessage() );
            }
        }

    }

    private void traiterPoids( String poids, Commande commande ) {
        double poidsCommande = 0;
        try {
            poidsCommande = validationPoids( poids );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_POIDS, e.getMessage() );
        }
        commande.setPoids( poidsCommande );
    }

    private void traiterDimension( String hauteur, String longueur, String largeur, Commande commande ) {
        Integer hauteurCommande = 0;
        Integer longueurCommande = 0;
        Integer largeurCommande = 0;
        try {
            hauteurCommande = validationDimension( hauteur );
            longueurCommande = validationDimension( longueur );
            largeurCommande = validationDimension( largeur );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_HAUTEUR, e.getMessage() );
        }
        commande.setDimension_hauteur( hauteurCommande );
        commande.setDimension_longueur( longueurCommande );
        commande.setDimension_largeur( largeurCommande );
    }

    /**
     * Validation des champs encodés par l'utilisateur
     * 
     * @return renvoie dans le traitement d'un champs une exception s'il y a une
     *         erreur détecté, ou rien sinon.
     * @param champ
     * @throws FormValidationException
     */

    private void validationChamp( String champ ) throws FormValidationException {
        if ( champ == null ) {
            throw new FormValidationException( "Le champ doit être mentionné." );
        }
    }

    private void validationCode( String code ) throws FormValidationException {
        if ( code == null ) {
            throw new FormValidationException( "Le champ doit être mentionné." );
        }
    }

    private Integer validationDimension( String dim ) throws FormValidationException {
        Integer temp;
        if ( dim != null ) {
            try {
                temp = Integer.parseInt( dim );
                if ( temp <= 0 ) {
                    throw new FormValidationException( "La valeur doit être un entier positif non-nul." );
                }
            } catch ( NumberFormatException e ) {
                temp = -1;
                throw new FormValidationException( "La valeur doit être un entier." );
            }
        } else {
            temp = -1;
            throw new FormValidationException( "Merci d'entrer une valeur." );
        }
        return temp;
    }

    private double validationValeur( String valeur ) throws FormValidationException {
        double temp;
        if ( valeur != null ) {
            try {
                temp = Double.parseDouble( valeur );
                if ( temp <= 0 ) {
                    throw new FormValidationException( "La valeur doit être un nombre positif non-nul." );
                } else if ( (int) temp > 999999999 ) {
                    throw new FormValidationException( "9 chiffres avant la virgule maximum autorisés" );
                }
            } catch ( NumberFormatException e ) {
                temp = -1;
                throw new FormValidationException( "La valeur doit être un nombre." );
            }
        } else {
            temp = -1;
            throw new FormValidationException( "Merci d'entrer une valeur." );
        }
        return temp;
    }

    private double validationPoids( String poids ) throws FormValidationException {
        double temp;
        if ( poids != null ) {
            try {
                temp = Double.parseDouble( poids );
                if ( temp <= 0 ) {
                    throw new FormValidationException( "La valeur doit être un nombre positif non-nul." );
                } else if ( (int) temp > 100 ) {
                    throw new FormValidationException( "100 kg maximum autorisés" );
                }
            } catch ( NumberFormatException e ) {
                temp = -1;
                throw new FormValidationException( "La valeur doit être un nombre." );
            }
        } else {
            temp = -1;
            throw new FormValidationException( "Merci d'entrer une valeur." );
        }
        return temp;
    }

    private void validationTelephone( String telephone ) throws FormValidationException {
        if ( telephone != null ) {
            if ( telephone.length() < 8 ) {
                throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
            }
        }
    }

    private void validationTelephoneExp( String telephone ) throws FormValidationException {
        if ( telephone != null ) {
            if ( telephone.length() < 8 ) {
                throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
            }
        } else {
            throw new FormValidationException( "Le champ doit être mentionné." );
        }

    }

    private void validationDate( String jour, String mois, String annee ) throws FormValidationException {
        if ( ( jour != null ) && ( mois != null ) && ( annee != null ) ) {
            if ( !jour.matches( "^\\d+$" ) || !mois.matches( "^\\d+$" ) || !annee.matches( "^\\d+$" ) ) {
                throw new FormValidationException( "Le champ doit uniquement contenir des chiffres." );
            }
            int newJour = Integer.parseInt( jour );
            int newMois = Integer.parseInt( mois );
            int newAnnee = Integer.parseInt( annee );
            Calendar date = new GregorianCalendar( newAnnee, newMois, newJour );
            Calendar maintenant = new GregorianCalendar();

            if ( maintenant.get( Calendar.YEAR ) > date.get( Calendar.YEAR ) ) {
                throw new FormValidationException( "La date n'est pas valide." );
            } else if ( maintenant.get( Calendar.YEAR ) == date.get( Calendar.YEAR )
                    && maintenant.get( Calendar.MONTH ) + 1 > date.get( Calendar.MONTH ) ) {
                throw new FormValidationException( "La date n'est pas valide." );
            } else if ( maintenant.get( Calendar.YEAR ) == date.get( Calendar.YEAR )
                    && maintenant.get( Calendar.MONTH ) + 1 == date.get( Calendar.MONTH )
                    && maintenant.get( Calendar.DAY_OF_MONTH ) >= date.get( Calendar.DAY_OF_MONTH ) ) {
                throw new FormValidationException( "La date n'est pas valide." );
            }

            if ( ( 0 < newJour ) && ( newJour < 32 ) && ( 0 < newMois )
                    && ( newMois < 13 ) ) {
                if ( newMois == 2 ) {
                    if ( ( ( newJour > 29 ) && ( ( (GregorianCalendar) date )
                            .isLeapYear( newAnnee ) ) )
                            ||
                            ( ( newJour > 28 ) && !( ( (GregorianCalendar) date )
                                    .isLeapYear( newAnnee ) ) ) ) {
                        throw new FormValidationException( "La date n'est pas valide." );
                    }
                }
                if ( ( newJour > 30 )
                        && ( ( newMois == 3 ) || ( newMois == 5 ) || ( newMois == 9 ) || ( newMois == 11 ) ) ) {
                    throw new FormValidationException( "La date n'est pas valide." );
                }
            } else {
                throw new FormValidationException( "La date n'est pas valide." );
            }
        }
    }

    /**
     * Ajoute un message correspondant au champ spécifié à  la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /**
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }

    /**
     * 
     * @param pays
     * @param codePostal
     * @return Méthode utilitaire qui retourne un identifiant de canton en
     *         fonction du code postal et du pays encodés.
     * @throws Exception
     */
    public int getIdCanton( String pays, int codePostal ) throws FormValidationException
    {
        if ( pays.equals( "Belgique" ) ) {
            if ( codePostal >= 1000 && codePostal <= 1299 )
                return 11; // Bruxelles

            else if ( codePostal >= 1300 && codePostal <= 1499 )
                return 6; // Brabant wallon

            else if ( codePostal >= 1500 && codePostal <= 1999 )
                return 7; // Brabant flamand

            else if ( codePostal >= 3000 && codePostal <= 3499 )
                return 7; // Brabant flamand

            else if ( codePostal >= 2000 && codePostal <= 2999 )
                return 8; // Anvers

            else if ( codePostal >= 3500 && codePostal <= 3999 )
                return 5; // Limbourg

            else if ( codePostal >= 4000 && codePostal <= 4999 )
                return 4; // Liège

            else if ( codePostal >= 5000 && codePostal <= 5999 )
                return 2; // Namur

            else if ( codePostal >= 6000 && codePostal <= 6599 )
                return 3; // Hainaut

            else if ( codePostal >= 7000 && codePostal <= 7999 )
                return 3; // Hainaut

            else if ( codePostal >= 6600 && codePostal <= 6999 )
                return 1; // Luxembourg

            else if ( codePostal >= 8000 && codePostal <= 8999 )
                return 10; // Flande occidentale

            else if ( codePostal >= 9000 && codePostal <= 9999 )
                return 9; // Flande orientale

            throw new FormValidationException( " Le code postal encodé n'est pas correct " );
        }

        else if ( pays.equals( "Luxembourg" ) )
            return 12;

        else if ( pays.equals( "Allemagne" ) )
            return 13;

        else if ( pays.equals( "Danemark" ) )
            return 14;

        else if ( pays.equals( "Pays-Bas" ) )
            return 15;

        else if ( pays.equals( "Pologne" ) )
            return 16;

        else if ( pays.equals( "Suisse" ) )
            return 17;

        else if ( pays.equals( "France" ) )
            return 18;

        else if ( pays.equals( "Grande-Bretagne" ) )
            return 19;

        else if ( pays.equals( "Irlande" ) )
            return 20;

        else if ( pays.equals( "Espagne" ) )
            return 21;

        else if ( pays.equals( "Portugal" ) )
            return 22;

        else if ( pays.equals( "Italie" ) )
            return 23;

        else if ( pays.equals( "Aeroport_Liege" ) )
            return 24;

        else if ( pays.equals( "R._Tcheque" ) )
            return 25;

        else if ( pays.equals( "Autriche" ) )
            return 26;

        else if ( pays.equals( "Bulgarie" ) )
            return 27;

        else if ( pays.equals( "Chypre" ) )
            return 28;

        else if ( pays.equals( "Croatie" ) )
            return 29;

        else if ( pays.equals( "Estonie" ) )
            return 30;

        else if ( pays.equals( "Finlande" ) )
            return 31;

        else if ( pays.equals( "Grece" ) )
            return 32;

        else if ( pays.equals( "Hongrie" ) )
            return 33;

        else if ( pays.equals( "Lettonie" ) )
            return 34;

        else if ( pays.equals( "Littuanie" ) )
            return 35;

        else if ( pays.equals( "Malte" ) )
            return 36;

        else if ( pays.equals( "Roumanie" ) )
            return 37;

        else if ( pays.equals( "Slovaquie" ) )
            return 38;

        else if ( pays.equals( "Slovenie" ) )
            return 39;

        else if ( pays.equals( "Suede" ) )
            return 40;

        return 4;
    }

    /**
     * 
     * @param pays
     * @param codePostal
     * @return Le canton correspondant au pays et code postal.
     * @throws FormValidationException
     */
    public String getCanton( String pays, int codePostal ) throws FormValidationException
    {
        if ( pays.equals( "Belgique" ) ) {
            if ( codePostal >= 1000 && codePostal <= 1299 )
                return "Bruxelles"; // Bruxelles

            else if ( codePostal >= 1300 && codePostal <= 1499 )
                return "Wavre"; // Brabant wallon

            else if ( codePostal >= 1500 && codePostal <= 1999 )
                return "Louvain"; // Brabant flamand --- Est-ce bien louvain et
                                  // non Bxl ?

            else if ( codePostal >= 3000 && codePostal <= 3499 )
                return "Louvain"; // Brabant flamand

            else if ( codePostal >= 2000 && codePostal <= 2999 )
                return "Anvers"; // Anvers

            else if ( codePostal >= 3500 && codePostal <= 3999 )
                return "Hasselt"; // Limbourg

            else if ( codePostal >= 4000 && codePostal <= 4999 )
                return "Liège"; // Liège

            else if ( codePostal >= 5000 && codePostal <= 5999 )
                return "Namur"; // Namur

            else if ( codePostal >= 6000 && codePostal <= 6599 )
                return "Mons"; // Hainaut (Charleroi)

            else if ( codePostal >= 7000 && codePostal <= 7999 )
                return "Mons"; // Hainaut

            else if ( codePostal >= 6600 && codePostal <= 6999 )
                return "Arlon"; // Luxembourg

            else if ( codePostal >= 8000 && codePostal <= 8999 )
                return "Bruges"; // Flande occidentale

            else if ( codePostal >= 9000 && codePostal <= 9999 )
                return "Gand"; // Flande orientale

            throw new FormValidationException( " Le code postal encodé est incorrect " );
            // TODO faire des vérification / exceptions plus complètes
        } else if ( pays.equals( "France" ) || pays.equals( "Danemark" ) || pays.equals( "Grande-Bretagne" )
                || pays.equals( "Irlande" ) || pays.equals( "Espagne" ) || pays.equals( "Portugal" )
                || pays.equals( "Italie" ) || pays.equals( "Suisse" ) || pays.equals( "R.Tchèque" )
                || pays.equals( "Autriche" ) || pays.equals( "Bulgarie" ) || pays.equals( "Chypre" )
                || pays.equals( "Croatie" ) || pays.equals( "Estonie" ) || pays.equals( "Finlande" )
                || pays.equals( "Grèce" ) || pays.equals( "Hongrie" ) || pays.equals( "Lettonie" )
                || pays.equals( "Littuanie" ) || pays.equals( "Malte" ) || pays.equals( "Roumanie" )
                || pays.equals( "Slovaquie" ) || pays.equals( "Slovénie" ) || pays.equals( "Suède" )
                || pays.equals( "Luxembourg" ) || pays.equals( "Allemagne" ) || pays.equals( "Pays-Bas" )
                || pays.equals( "Pologne" ) || pays.equals( "Slovaquie" ) ) {
            return pays;
        } else {
            throw new FormValidationException( " Le pays encodé est incorrect" );
        }
    }

    /**
     * 
     * @param commande
     * @param pays
     * @param codePostal
     * @return Méthode utilitaire qui récupère la liste des villes associées à
     *         un code postal d'un pays particulier
     * @throws Exception
     */
    public List<String> getListesVilles( Commande commande, String pays, String codePostal ) throws Exception
    {
        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();

        searchCriteria.setQ( pays + " " + codePostal );
        ToponymSearchResult searchResult;

        List<String> listeVilles = new ArrayList<String>();
        try
        {
            WebService.setUserName( "sniper" );
            searchResult = WebService.search( searchCriteria );
            for ( Toponym toponym : searchResult.getToponyms() )
            {
                /*
                 * place le nom des entités ayant un code postal particulier
                 * dans une liste de villes
                 */
                listeVilles.add( toponym.getName() );
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        commande.setListeVilles( listeVilles );
        return listeVilles;
    }
}
