
/**
 * IncomingOutgoingB2BCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package hlbexpress.b2b.facade;

    /**
     *  IncomingOutgoingB2BCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class IncomingOutgoingB2BCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public IncomingOutgoingB2BCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public IncomingOutgoingB2BCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getAllParcelsOutgoing method
            * override this method for handling normal response from getAllParcelsOutgoing operation
            */
           public void receiveResultgetAllParcelsOutgoing(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetAllParcelsOutgoingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllParcelsOutgoing operation
           */
            public void receiveErrorgetAllParcelsOutgoing(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteParcelIncoming method
            * override this method for handling normal response from deleteParcelIncoming operation
            */
           public void receiveResultdeleteParcelIncoming(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.DeleteParcelIncomingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteParcelIncoming operation
           */
            public void receiveErrordeleteParcelIncoming(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for confirmReception method
            * override this method for handling normal response from confirmReception operation
            */
           public void receiveResultconfirmReception(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.ConfirmReceptionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from confirmReception operation
           */
            public void receiveErrorconfirmReception(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for addNewParcelIncoming method
            * override this method for handling normal response from addNewParcelIncoming operation
            */
           public void receiveResultaddNewParcelIncoming(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.AddNewParcelIncomingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addNewParcelIncoming operation
           */
            public void receiveErroraddNewParcelIncoming(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getParcelIncoming method
            * override this method for handling normal response from getParcelIncoming operation
            */
           public void receiveResultgetParcelIncoming(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetParcelIncomingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getParcelIncoming operation
           */
            public void receiveErrorgetParcelIncoming(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAllParcelsIncoming method
            * override this method for handling normal response from getAllParcelsIncoming operation
            */
           public void receiveResultgetAllParcelsIncoming(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetAllParcelsIncomingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAllParcelsIncoming operation
           */
            public void receiveErrorgetAllParcelsIncoming(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getIncomingParcels method
            * override this method for handling normal response from getIncomingParcels operation
            */
           public void receiveResultgetIncomingParcels(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetIncomingParcelsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getIncomingParcels operation
           */
            public void receiveErrorgetIncomingParcels(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getParcelOutgoing method
            * override this method for handling normal response from getParcelOutgoing operation
            */
           public void receiveResultgetParcelOutgoing(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.GetParcelOutgoingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getParcelOutgoing operation
           */
            public void receiveErrorgetParcelOutgoing(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteParcelOutgoing method
            * override this method for handling normal response from deleteParcelOutgoing operation
            */
           public void receiveResultdeleteParcelOutgoing(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.DeleteParcelOutgoingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteParcelOutgoing operation
           */
            public void receiveErrordeleteParcelOutgoing(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendParcelOutgoing method
            * override this method for handling normal response from sendParcelOutgoing operation
            */
           public void receiveResultsendParcelOutgoing(
                    hlbexpress.b2b.facade.IncomingOutgoingB2BStub.SendParcelOutgoingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendParcelOutgoing operation
           */
            public void receiveErrorsendParcelOutgoing(java.lang.Exception e) {
            }
                


    }
    