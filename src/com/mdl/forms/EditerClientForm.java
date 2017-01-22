package com.mdl.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.text.BasicTextEncryptor;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOException;
import com.mdl.dao.UtilisateurDAO;

/**
 * Classe permettant les vérifications et la modifications des informations du
 * client
 * 
 * 
 * 
 */
public final class EditerClientForm {
    // Champs du formaulaire à récupérer
    private static final String CHAMP_EMAIL         = "email";
    private static final String CHAMP_PASS          = "motdepasse";
    private static final String CHAMP_CONF          = "confirmation";
    private static final String CHAMP_SEXE          = "sexe";
    private static final String CHAMP_NOM           = "nom";
    private static final String CHAMP_PRENOM        = "prenom";
    private static final String CHAMP_TEL_PORTABLE  = "telephonePortable";
    private static final String CHAMP_TEL_FIXE      = "telephoneFixe";
    private static final String CHAMP_TVA           = "num_tva";
    private static final String CHAMP_ADRESSE_RUE   = "adresse_rue";
    private static final String CHAMP_ADRESSE_NUM   = "adresse_num";
    private static final String CHAMP_ADRESSE_BOITE = "adresse_boite";
    private static final String CHAMP_ADRESSE_LOC   = "adresse_loc";
    private static final String CHAMP_ADRESSE_CODE  = "adresse_code";
    private static final String CHAMP_ADRESSE_PAYS  = "adresse_pays";
    private static final String CHAMP_NAISSANCE     = "datenaissance";

    private UtilisateurDAO      utilisateurDao;

    /**
     * Constructeur de EditerClientForm
     * 
     * @param utilisateurDao
     * 
     */
    public EditerClientForm( UtilisateurDAO utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    private String              resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    /**
     * Getter du résultat
     * 
     * @return un résultat
     * 
     */
    public String getResultat() {
        return resultat;
    }

    /**
     * Getter des erreurs
     * 
     * @return une map des erreurs
     * 
     */
    public Map<String, String> getErreurs() {
        return erreurs;
    }

    /**
     * Vérification et modification du profil du client
     * 
     * @param request
     * @param utilisateur
     * @return un objet l'Utilisateur
     * 
     */
    public Utilisateur editerClient( HttpServletRequest request, Utilisateur utilisateur ) {
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String sexe = getValeurChamp( request, CHAMP_SEXE );
        String nom = getValeurChamp( request, CHAMP_NOM );
        String prenom = getValeurChamp( request, CHAMP_PRENOM );
        String adresse_rue = getValeurChamp( request, CHAMP_ADRESSE_RUE );
        String adresse_num = getValeurChamp( request, CHAMP_ADRESSE_NUM );
        String adresse_boite = getValeurChamp( request, CHAMP_ADRESSE_BOITE );
        String adresse_loc = getValeurChamp( request, CHAMP_ADRESSE_LOC );
        String adresse_code = getValeurChamp( request, CHAMP_ADRESSE_CODE );
        String adresse_pays = getValeurChamp( request, CHAMP_ADRESSE_PAYS );
        String telephonePortable = getValeurChamp( request, CHAMP_TEL_PORTABLE );
        String telephoneFixe = getValeurChamp( request, CHAMP_TEL_FIXE );
        String num_tva = getValeurChamp( request, CHAMP_TVA );
        String dateNaissance = getValeurChamp( request, CHAMP_NAISSANCE );
        // Récupération des champs du formulaire

        try {
            if ( motDePasse != null ) {
                traiterMotsDePasse( motDePasse, confirmation, utilisateur );
            }

            if ( sexe != null ) {
                utilisateur.setSexe( sexe );
            }

            if ( nom != null ) {
                utilisateur.setNom( nom );
            }

            if ( prenom != null ) {

                utilisateur.setPrenom( prenom );
            }

            if ( adresse_rue != null ) {
                utilisateur.setAdresseRue( adresse_rue );
            }

            if ( adresse_num != null ) {
                utilisateur.setAdresseNum( adresse_num );
            }

            if ( adresse_boite != null ) {
                utilisateur.setAdresseBoite( adresse_boite );
            } else {
                utilisateur.setAdresseBoite( null );
            }

            if ( adresse_loc != null ) {
                utilisateur.setAdresseLoc( adresse_loc );
            }

            if ( adresse_pays != null ) {
                utilisateur.setAdressePays( adresse_pays );
            }

            if ( num_tva != null ) {
                utilisateur.setNum_tva( num_tva );
            } else {
                utilisateur.setNum_tva( null );
            }

            if ( adresse_code != null ) {
                try {
                    validationCode( adresse_code );
                    utilisateur.setAdresseCode( adresse_code );
                } catch ( FormValidationException e ) {
                    setErreur( CHAMP_ADRESSE_CODE, e.getMessage() );
                }
            }

            if ( telephonePortable != null ) {
                try {
                    validationTelephone( telephonePortable );
                    utilisateur.setTelephonePortable( telephonePortable );
                } catch ( FormValidationException e ) {
                    setErreur( CHAMP_TEL_PORTABLE, e.getMessage() );
                }
            }

            if ( telephoneFixe != null ) {
                try {
                    validationTelephone( telephoneFixe );
                    utilisateur.setTelephoneFixe( telephoneFixe );
                } catch ( FormValidationException e ) {
                    setErreur( CHAMP_TEL_FIXE, e.getMessage() );
                }
            } else {
                utilisateur.setTelephoneFixe( null );
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
                Date dn = new Date(); // récupération de la date
                dn = sdf.parse( dateNaissance );
                Integer jourNaissance = dn.getDate();
                Integer moisNaissance = dn.getMonth() + 1;
                Integer anneeNaissance = dn.getYear() + 1900;

                validationNaissance( jourNaissance, moisNaissance, anneeNaissance ); // validation
                // Mise en forme de la date de naissance sous la forme
                // GregorianCalendar
                utilisateur.setJourNaissance( jourNaissance.toString() );
                utilisateur.setMoisNaissance( moisNaissance.toString() );
                utilisateur.setAnneeNaissance( anneeNaissance.toString() );
                Calendar naissance = new GregorianCalendar( anneeNaissance, ( moisNaissance - 1 ), jourNaissance );
                // Conversion du GregorianCalendar vers Date
                java.sql.Date date = new java.sql.Date( naissance.getTimeInMillis() );
                utilisateur.setDateNaissance( date );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_NAISSANCE, e.getMessage() );
            } catch ( ParseException e ) {
                System.out.println( "pas cool" );
                setErreur( CHAMP_NAISSANCE, "Date non-valide" );
            }

            if ( erreurs.isEmpty() ) {
                utilisateurDao.modifier( utilisateur );

                resultat = "Succès de la modification.";
            } else {
                resultat = "Echec de la modification.";
            }
        } catch ( DAOException e ) {
            resultat = "Echec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }

    /**
     * Vérification du mot de passe
     * 
     * @param motDePasse
     * @param confirmation
     * @param utilisateur
     * 
     */
    private void traiterMotsDePasse( String motDePasse, String confirmation, Utilisateur utilisateur ) {
        try {
            validationMotsDePasse( motDePasse, confirmation );
            BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
            String myEncryptionPassword = "HLB-Express";
            textEncryptor.setPassword( myEncryptionPassword );
            String myEncryptedPassword = textEncryptor.encrypt( motDePasse );
            // Chiffrement du mot de passe à l'aide d'un clé
            utilisateur.setMotDePasse( myEncryptedPassword );

        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
        }
    }

    /**
     * Vérification du mot de passe et de ce confirmation
     * 
     * @param motDePasse
     * @param confirmation
     * @throws FormValidationException
     * 
     */
    private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new FormValidationException(
                        "Les mots de passe entrés sont différents, merci de les saisir à nouveau." );
            } else if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }

    /**
     * Vérification du code postal
     * 
     * @param adresse_code
     * @throws FormValidationException
     * 
     */
    private void validationCode( String adresse_code ) throws FormValidationException {
        if ( adresse_code != null ) {
        } else {
            throw new FormValidationException( "Le code postal doit être mentionné." );
        }
    }

    /**
     * Vérification du téléphone
     * 
     * @param telephone
     * @throws FormValidationException
     * 
     */
    private void validationTelephone( String telephone ) throws FormValidationException {
        if ( telephone.length() < 8 ) {
            throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
        }
    }

    /**
     * Vérification de la date de naissance
     * 
     * @param jourNaissance
     * @param moisNaissance
     * @param anneeNaissance
     * @throws FormValidationException
     * 
     */
    private void validationNaissance( int JourNaissance, int MoisNaissance, int anneeNaissance )
            throws FormValidationException {
        Calendar naissance = new GregorianCalendar( anneeNaissance - 1, MoisNaissance, JourNaissance );
        Calendar maintenant = new GregorianCalendar();
        int age = maintenant.get( Calendar.YEAR ) - naissance.get( Calendar.YEAR );
        if ( ( ( naissance.get( Calendar.MONTH ) - 1 ) > maintenant.get( Calendar.MONTH ) )
                || ( ( ( naissance.get( Calendar.MONTH ) - 1 ) == maintenant.get( Calendar.MONTH ) ) && ( naissance
                        .get( Calendar.DAY_OF_MONTH ) > maintenant.get( Calendar.DAY_OF_MONTH ) ) ) ) {
            age--;
        }
        if ( age < 12 ) {
            // age minimum de 12 ans
            throw new FormValidationException( "L'âge minimum pour s'incrire est de 12 ans." );
        }
        if ( ( 0 < JourNaissance ) && ( JourNaissance < 32 ) && ( 0 < MoisNaissance )
                && ( MoisNaissance < 13 ) ) {
            if ( MoisNaissance == 2 ) {
                if ( ( ( JourNaissance > 29 ) && ( ( (GregorianCalendar) naissance )
                        .isLeapYear( anneeNaissance ) ) )
                        ||
                        ( ( JourNaissance > 28 ) && !( ( (GregorianCalendar) naissance )
                                .isLeapYear( anneeNaissance ) ) ) ) {
                    throw new FormValidationException( "La date de naissance n'est pas valide." );
                }
            }
            if ( ( JourNaissance > 30 )
                    && ( ( MoisNaissance == 3 ) || ( MoisNaissance == 5 ) || ( MoisNaissance == 9 ) || ( MoisNaissance == 11 ) ) ) {
                throw new FormValidationException( "La date de naissance n'est pas valide." );
            }
        } else {
            throw new FormValidationException( "La date de naissance n'est pas valide." );
        }
    }

    /**
     * Ajoute un message correspondant au champ spécifié à la map des erreurs
     * 
     * @param champ
     * @param message
     * 
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /**
     * Méthode utilitaire qui retourne NULL si un champ est vide, et son contenu
     * sinon.
     * 
     * @param request
     * @param nomChamp
     * @return la valeur
     * 
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }

}