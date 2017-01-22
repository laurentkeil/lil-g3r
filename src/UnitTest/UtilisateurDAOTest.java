package UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mdl.beans.Utilisateur;
import com.mdl.dao.DAOFactory;
import com.mdl.dao.UtilisateurDAO;

/**
 * Classe de tests unitaires pour la récupération de données concernant
 * l'utilisateur dans la bd (dao)
 * 
 */
public class UtilisateurDAOTest extends HttpServlet {

    // Constante de la DAOFactory
    public static final String CONF_DAO_FACTORY = "daofactory";

    private UtilisateurDAO     utilisateurDao;

    /**
     * Initialisation du DAO de l'utilisateur pour l'interaction avec la base de
     * données
     */
    public void init() throws ServletException {
        this.utilisateurDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUtilisateurDao();
    }

    public UtilisateurDAOTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreer() {
        Utilisateur user = new Utilisateur();
        user.setNom( "testnom" );
        user.setPrenom( "testprenom" );
        user.setSexe( "M" );
        user.setAdresseRue( "rue test" );
        user.setAdresseNum( "78" );
        user.setAdresseBoite( "47c" );
        user.setAdresseLoc( "Villetest" );
        user.setAdresseCode( "5462" );
        user.setAdressePays( "Belgique" );
        user.setTelephonePortable( "0487/456545" );
        user.setTelephoneFixe( "003271565485" );
        user.setEmail( "test@hotmail.com" );
        user.setMotDePasse( "mdptest" );
        Calendar naissance = new GregorianCalendar( 1992, 3, 3 );
        java.sql.Date date = new java.sql.Date( naissance.getTimeInMillis() );
        user.setDateNaissance( date );
        user.setNum_tva( "" );
        utilisateurDao.creer( user );
    }

    @Test
    public void testTrouver() {
        Utilisateur user = utilisateurDao.trouver( "laurent_0306@hotmail.com" );
        try {
            assertEquals( user.getNom(), "Keil" );
        } catch ( Exception e ) {
            fail( "L'utilisateur trouvé n'est pas conforme." );
        }
    }

    @Test
    public void testTrouverAvecId() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testInscription() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testModifier() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testListerAll() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testListerAllInscrit() {
        fail( "Not yet implemented" );
    }

    @Test
    public void testTrouverClientRech() {
        fail( "Not yet implemented" );
    }

}
