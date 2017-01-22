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
 * Vérification des champs de l'inscription et ajout du client dans la base de
 * données s'il n'y a pas de problèmes
 * 
 * 
 * 
 */
public final class InscriptionForm {
    private static final String CHAMP_EMAIL         = "email";
    private static final String CHAMP_EMAIL_CONF    = "emailConf";
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
    private static final String CHAMP_LU            = "lu";
    // Champs du formulaire à récupérer
    private UtilisateurDAO      utilisateurDao;

    /**
     * Constructeur de InscriptionForm
     * 
     * @param utilisateurDao
     * 
     */
    public InscriptionForm( UtilisateurDAO utilisateurDao ) {
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
     * Vérification des champs du formulaire et ajoout du client dans la base de
     * données s'il n'y a pas d'erreur.
     * 
     * @param request
     * @return un objet Utilisateur
     * 
     */
    public Utilisateur inscrireUtilisateur( HttpServletRequest request ) {
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String emailConf = getValeurChamp( request, CHAMP_EMAIL_CONF );
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
        String lu = getValeurChamp( request, CHAMP_LU );
        // Récupération des informations des champs du formulaire
        Utilisateur utilisateur = new Utilisateur();
        try {

            try {
                validationEmail( email, emailConf );
                utilisateur.setEmail( email );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_EMAIL, e.getMessage() );
            }

            traiterMotsDePasse( motDePasse, confirmation, utilisateur );

            utilisateur.setSexe( sexe );

            try {
                validationNom( nom );
                utilisateur.setNom( nom );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_NOM, e.getMessage() );
            }

            try {
                validationPrenom( prenom );
                utilisateur.setPrenom( prenom );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_PRENOM, e.getMessage() );
            }

            try {
                validationRue( adresse_rue );
                utilisateur.setAdresseRue( adresse_rue );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_ADRESSE_RUE, e.getMessage() );
            }

            try {
                validationNum( adresse_num );

                utilisateur.setAdresseNum( adresse_num );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_ADRESSE_NUM, e.getMessage() );
            }

            utilisateur.setAdresseBoite( adresse_boite );

            try {
                validationLoc( adresse_loc );
                utilisateur.setAdresseLoc( adresse_loc );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_ADRESSE_LOC, e.getMessage() );
            }

            try {
                validationPays( adresse_pays );
                utilisateur.setAdressePays( adresse_pays );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_ADRESSE_PAYS, e.getMessage() );
            }

            try {
                validationCode( adresse_code, adresse_pays );
                utilisateur.setAdresseCode( adresse_code );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_ADRESSE_CODE, e.getMessage() );
            }

            try {
                validationTelephone( telephonePortable );
                utilisateur.setTelephonePortable( telephonePortable );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_TEL_PORTABLE, e.getMessage() );
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
                java.sql.Date date = new java.sql.Date( naissance.getTimeInMillis() );
                utilisateur.setDateNaissance( date );
                // Conversion de GregorianCalendar vers une date
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_NAISSANCE, e.getMessage() );
            } catch ( ParseException e ) {
                setErreur( CHAMP_NAISSANCE, "Date non-valide" );
            }

            if ( num_tva != null ) {
                utilisateur.setNum_tva( num_tva );
            }

            if ( lu == null ) {
                setErreur( CHAMP_LU, "Vous devez lire et approuver le réglement." );
            }

            if ( erreurs.isEmpty() ) {
                utilisateurDao.creer( utilisateur );
                resultat = "Succès de l'inscription. Un e-mail de validation vous a été envoyé sur votre adresse e-mail";
            } else {
                resultat = "échec de l'inscription.";
            }
        } catch ( DAOException e ) {
            resultat = "échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }

    /**
     * Chiffrement du mot de passe
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

            utilisateur.setMotDePasse( myEncryptedPassword );

        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
            setErreur( CHAMP_CONF, null );
        }
    }

    /**
     * Vérification de l'adresse e-mail
     * 
     * @param email
     * @param emailConf
     * @throws FormValidationException
     * 
     */
    private void validationEmail( String email, String emailConf ) throws FormValidationException {
        if ( email != null && emailConf != null ) {
            // Regex utilisé pour le format de l'adresse e-mail
            if ( ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) )
                    && ( !emailConf.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( utilisateurDao.trouver( email ) != null ) {
                throw new FormValidationException(
                        "Cette adresse email est déjà utilisée, merci d'en choisir une autre." );
            } else if ( !email.equals( emailConf ) ) {
                throw new FormValidationException(
                        "Les adresses e-mail entrées sont différentes, merci de les saisir à nouveau." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }

    /**
     * Vérification du mot de passe et de sa confirmation
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
                // Minimum 3 caractères
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }

    /**
     * Vérification du nom
     * 
     * @param nom
     * @throws FormValidationException
     * 
     */
    private void validationNom( String nom ) throws FormValidationException {
        if ( nom == null ) {
            throw new FormValidationException( "Le nom d'utilisateur doit être mentionné." );
        }
    }

    /**
     * Vérification du prénom
     * 
     * @param prenom
     * @throws FormValidationException
     * 
     */
    private void validationPrenom( String prenom ) throws FormValidationException {
        if ( prenom == null ) {
            throw new FormValidationException( "Le prénom d'utilisateur doit être mentionné." );
        }
    }

    /**
     * Vérification de la rue
     * 
     * @param adresse_rue
     * @throws FormValidationException
     * 
     */
    private void validationRue( String adresse_rue ) throws FormValidationException {
        if ( adresse_rue == null ) {
            throw new FormValidationException( "La rue doit être mentionnée." );
        }
    }

    /**
     * Vérification du numéro de la maison
     * 
     * @param adresse_num
     * @throws FormValidationException
     * 
     */
    private void validationNum( String adresse_num ) throws FormValidationException {
        if ( adresse_num == null ) {
            throw new FormValidationException( "Le numéro doit être mentionnée." );
        }
        if ( !adresse_num.matches( "^\\d+$" ) ) {
            throw new FormValidationException( "Le numéro de téléphone doit uniquement contenir des chiffres." );
        }
    }

    /**
     * Vérification de la localité
     * 
     * @param adresse_loc
     * @throws FormValidationException
     * 
     */
    private void validationLoc( String adresse_loc ) throws FormValidationException {
        if ( adresse_loc == null ) {
            throw new FormValidationException( "La localité doit être mentionnée." );
        }
    }

    /**
     * Vérification du pays
     * 
     * @param adresse_pays
     * @throws FormValidationException
     * 
     */
    private void validationPays( String adresse_pays ) throws FormValidationException {
        if ( adresse_pays == null ) {
            throw new FormValidationException( "Le pays doit être mentionné." );
        }
    }

    /**
     * @description vérifie le champ code, s'il est mentionné, s'il ne contient
     *              que des chiffres, s'il le code postal correspond au pays
     * @param adresse_code
     * @param adresse_pays
     * @throws FormValidationException
     */
    private void validationCode( String adresse_code, String adresse_pays ) throws FormValidationException {
        if ( adresse_code == null ) {
            throw new FormValidationException( "Le code postal doit être mentionné." );
        }
        if ( adresse_pays != null ) {
            if ( adresse_pays.equals( "Belgique" ) && ( Integer.parseInt( adresse_code ) < 1000
                    || Integer.parseInt( adresse_code ) > 9999 ) ) {
                throw new FormValidationException( "Le code postal est incorrect." );
            }
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
        if ( telephone != null ) {
            if ( telephone.length() < 8 ) {
                throw new FormValidationException( "Le numéro de téléphone doit contenir au moins 8 chiffres." );
            }
        } else {
            throw new FormValidationException( "Le numéro de téléphone doit être mentionné." );
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
        System.out.println( naissance.get( Calendar.YEAR ) );
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
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     * 
     * @param champ
     * @param message
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