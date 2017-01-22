
/**
 * RoutingUnknownCenterExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package hlbexpress.routing.facade;

public class RoutingUnknownCenterExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1386059197974L;
    
    private hlbexpress.routing.facade.RoutingStub.RoutingUnknownCenterException faultMessage;

    
        public RoutingUnknownCenterExceptionException() {
            super("RoutingUnknownCenterExceptionException");
        }

        public RoutingUnknownCenterExceptionException(java.lang.String s) {
           super(s);
        }

        public RoutingUnknownCenterExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RoutingUnknownCenterExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(hlbexpress.routing.facade.RoutingStub.RoutingUnknownCenterException msg){
       faultMessage = msg;
    }
    
    public hlbexpress.routing.facade.RoutingStub.RoutingUnknownCenterException getFaultMessage(){
       return faultMessage;
    }
}
    