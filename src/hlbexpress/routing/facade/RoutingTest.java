package hlbexpress.routing.facade;

import hlbexpress.routing.facade.RoutingStub.Center;
import hlbexpress.routing.facade.RoutingStub.GetCenterList;
import hlbexpress.routing.facade.RoutingStub.GetCenterListResponse;
import hlbexpress.routing.facade.RoutingStub.GetNextDestination;
import hlbexpress.routing.facade.RoutingStub.GetNextDestinationResponse;
import hlbexpress.routing.facade.RoutingStub.GetShortestPath;
import hlbexpress.routing.facade.RoutingStub.GetShortestPathResponse;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

public class RoutingTest {
    public static void main( String[] args ) {
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
                    if ( centers[i].getCountries() == null ) {
                        continue;
                    }
                    for ( int j = 0; j < centers[i].getCountries().length; j++ ) {
                        message += " " + centers[i].getCountries()[j].getName() + "; ";
                    }
                }
                System.out.println( "Message is-----------" + message );
                // prints adjacent countries to that center

                // invoke getDestination
                GetNextDestination nextdest = new GetNextDestination();
                nextdest.setOrigin( "Arlon" );
                nextdest.setDestination( "Hasselt" );
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
}
