package UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.mdl.forms.CommandeForm;

/**
 * Classe de tests unitaires de la gestion du formulaire de commande, pour les
 * vérifications etc.
 */
public class CommandeFormTest {

    public CommandeFormTest() {
    }

    @Test
    public void testGetIdCanton() {
        CommandeForm com = new CommandeForm( null );
        try {
            assertEquals( com.getIdCanton( "Belgique", 6220 ), 3 );
            assertEquals( com.getIdCanton( "Belgique", 1100 ), 11 );
            assertEquals( com.getIdCanton( "Croatie", 56480 ), 29 );
        } catch ( Exception e ) {
            fail( "L'id du canton retourné est incorrect" );
        }
    }

    @Test
    public void testGetCanton() {
        CommandeForm com = new CommandeForm( null );
        try {
            assertEquals( com.getCanton( "Belgique", 6220 ), "Mons" );
            assertEquals( com.getCanton( "Belgique", 1100 ), "Bruxelles" );
            assertEquals( com.getCanton( "Croatie", 56480 ), "Croatie" );
        } catch ( Exception e ) {
            fail( "Le canton retourné est incorrect" );
        }
    }

}
