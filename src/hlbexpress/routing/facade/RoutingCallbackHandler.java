
/**
 * RoutingCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package hlbexpress.routing.facade;

    /**
     *  RoutingCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class RoutingCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public RoutingCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public RoutingCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getShortestPath method
            * override this method for handling normal response from getShortestPath operation
            */
           public void receiveResultgetShortestPath(
                    hlbexpress.routing.facade.RoutingStub.GetShortestPathResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getShortestPath operation
           */
            public void receiveErrorgetShortestPath(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getCenterList method
            * override this method for handling normal response from getCenterList operation
            */
           public void receiveResultgetCenterList(
                    hlbexpress.routing.facade.RoutingStub.GetCenterListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getCenterList operation
           */
            public void receiveErrorgetCenterList(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getNextDestination method
            * override this method for handling normal response from getNextDestination operation
            */
           public void receiveResultgetNextDestination(
                    hlbexpress.routing.facade.RoutingStub.GetNextDestinationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNextDestination operation
           */
            public void receiveErrorgetNextDestination(java.lang.Exception e) {
            }
                


    }
    