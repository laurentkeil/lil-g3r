package hlbexpress.routing.facade;

import hlbexpress.routing.facade.RoutingStub.Center;
import hlbexpress.routing.facade.RoutingStub.GetCenterList;
import hlbexpress.routing.facade.RoutingStub.GetCenterListResponse;
import hlbexpress.routing.facade.RoutingStub.GetNextDestination;
import hlbexpress.routing.facade.RoutingStub.GetNextDestinationResponse;
import hlbexpress.routing.facade.RoutingStub.GetShortestPath;
import hlbexpress.routing.facade.RoutingStub.GetShortestPathResponse;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;

/**
 * 
 * Classe d'accès au webservice de routage permettant d'obtenir des informations
 * concernant les pays, les villes, les codes postaux et les cantons pour un
 * routage particulier. Elle consomme les webservices de routages nous étant
 * fourni.
 * 
 */
public class RoutingCommande {

    private List<String> listPays = new ArrayList<String>();

    public RoutingCommande() {
        try {

            RoutingStub rstub = new RoutingStub();
            GetCenterList clist = new GetCenterList();
            GetCenterListResponse clistResponse = rstub.getCenterList( clist );
            if ( clistResponse.is_returnSpecified() ) {
                // get all returned centers
                Center[] centers = clistResponse.get_return();
                String message = "";
                for ( int i = 0; i < centers.length; i++ ) {
                    message += "\n centerID = " + centers[i].getCenterID() + ", type = " + centers[i].getCenterType()
                            + ", Isocode =" + centers[i].getCenterCountry().getIsocode();
                    // mise en place de la liste des pays
                    if ( centers[i].getCenterType() == 2 ) {
                        this.listPays.add( centers[i].getCenterID() );
                    }
                    if ( centers[i].getCountries() == null ) {
                        continue;
                    }
                    for ( int j = 0; j < centers[i].getCountries().length; j++ ) {
                        message += " " + centers[i].getCountries()[j].getName() + "; ";
                    }
                }
                this.listPays.add( "Belgique" );
                System.out.println( "Message is-----------" + message );
                // prints adjacent countries to that center

                // invoke getDestination
                GetNextDestination nextdest = new GetNextDestination();
                nextdest.setOrigin( "Arlon" );
                nextdest.setDestination( "Pologne" );
                GetNextDestinationResponse response = rstub.getNextDestination( nextdest );
                System.out.println( "next hop is " + response.get_return() );

                // shortest path test
                GetShortestPath sp = new GetShortestPath();
                sp.setOrigin( "Arlon" );
                sp.setDestination( "Bruges" );
                GetShortestPathResponse spResponse = rstub.getShortestPath( sp );
                String message2 = "";
                for ( int i = 0; i < spResponse.get_return().length; i++ ) {
                    message2 += spResponse.get_return()[i] + ", ";
                }

                System.out.println( "Message last hop is " + message2 );

                // invoke getDestination with unexisting center
                // nextdest.setOrigin("Charleroi");
                // response = rstub.getNextDestination(nextdest);
                // System.out.println("next hop from " + response.get_return() +
                // " (getDestination)");

            }
        } catch ( AxisFault e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( RemoteException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( RoutingUnknownCenterExceptionException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // invoke getCenterList

    }

    /*
     * récupère la liste des pays
     */
    public List<String> getListPays() {
        return listPays;
    }

    /*
     * Place la liste des pays
     */
    public void setListPays( List<String> listPays ) {
        this.listPays = listPays;
    }

    /**
     * 
     * @param cantonExp
     * @param cantonDest
     * @return le nombre de cantons traversés en fonction d'une origine et
     *         destination (accès au webservice de routage)
     */
    public int getNbCanton( String cantonExp, String cantonDest ) {
        int nbCanton = 0;
        try {
            RoutingStub rstub = new RoutingStub();

            // shortest path test
            GetShortestPath sp = new GetShortestPath();
            sp.setOrigin( cantonExp );
            sp.setDestination( cantonDest );
            GetShortestPathResponse spResponse = rstub.getShortestPath( sp );
            nbCanton = spResponse.get_return().length;
        } catch ( AxisFault e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( RemoteException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( RoutingUnknownCenterExceptionException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nbCanton;
    }
}
