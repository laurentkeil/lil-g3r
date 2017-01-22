package UnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import hlbexpress.routing.facade.RoutingCommande;

import org.junit.Test;

/**
 * Classe de tests unitaires du webservice de routing et des méthodes de
 * récupération de données.
 * 
 */
public class RoutingCommandeTest {

    public RoutingCommandeTest() {
    }

    @Test
    public void testGetListPays() {
        RoutingCommande rc = new RoutingCommande();
        try {
            assertEquals( rc.getListPays().get( 1 ), "Autriche" );
            assertEquals( rc.getListPays().get( 5 ), "Croatie" );
            assertEquals( rc.getListPays().get( 15 ), "Italie" );
            assertEquals( rc.getListPays().get( 28 ), "Suède" );
        } catch ( Exception e ) {
            fail( "La liste de pays retournée n'est pas correcte ni ordonnée." );
        }
    }

    @Test
    public void testGetNbCanton() {
        RoutingCommande rc = new RoutingCommande();
        try {
            assertEquals( rc.getNbCanton( "Namur", "Liège" ), 1 );
            assertEquals( rc.getNbCanton( "Mons", "Suède" ), 4 );
            assertEquals( rc.getNbCanton( "Mons", "Liège" ), 2 );
            assertEquals( rc.getNbCanton( "Mons", "Mons" ), 1 );
        } catch ( Exception e ) {
            fail( "Le nombre de cantons calculé n'est pas correcte." );
        }
        try {
            assertEquals( rc.getNbCanton( "Mons", "VilleFausse" ), 0 );
        } catch ( Exception e ) {
            fail( "Le nombre de canton n'est pas nul." );
        }
    }

}
