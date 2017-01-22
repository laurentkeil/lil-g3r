
/**
 * IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package hlbexpress.b2b.facade;

public class IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1386755611654L;
    
    private hlbexpress.b2b.facade.IncomingOutgoingB2BStub.IncomingOutgoingB2BRoutingUnknownCenterExceptionException faultMessage;

    
        public IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException() {
            super("IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException");
        }

        public IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException(java.lang.String s) {
           super(s);
        }

        public IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public IncomingOutgoingB2BRoutingUnknownCenterExceptionExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(hlbexpress.b2b.facade.IncomingOutgoingB2BStub.IncomingOutgoingB2BRoutingUnknownCenterExceptionException msg){
       faultMessage = msg;
    }
    
    public hlbexpress.b2b.facade.IncomingOutgoingB2BStub.IncomingOutgoingB2BRoutingUnknownCenterExceptionException getFaultMessage(){
       return faultMessage;
    }
}
    