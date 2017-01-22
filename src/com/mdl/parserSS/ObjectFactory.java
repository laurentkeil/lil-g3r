//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.01.14 at 04:24:38 PM CET 
//

package com.mdl.parserSS;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the hlbexpress.parse package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Clients_QNAME = new QName( "http://www.hlbexpress.com/namespaces/HLB-head-office",
                                                      "clients" );

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: hlbexpress.parse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientsType }
     * 
     */
    public ClientsType createClientsType() {
        return new ClientsType();
    }

    /**
     * Create an instance of {@link DeliveryproblemType }
     * 
     */
    public DeliveryproblemType createDeliveryproblemType() {
        return new DeliveryproblemType();
    }

    /**
     * Create an instance of {@link PickupsType }
     * 
     */
    public PickupsType createPickupsType() {
        return new PickupsType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link ProblemType }
     * 
     */
    public ProblemType createProblemType() {
        return new ProblemType();
    }

    /**
     * Create an instance of {@link PickupType }
     * 
     */
    public PickupType createPickupType() {
        return new PickupType();
    }

    /**
     * Create an instance of {@link Dimension }
     * 
     */
    public Dimension createDimension() {
        return new Dimension();
    }

    /**
     * Create an instance of {@link ProfileType }
     * 
     */
    public ProfileType createProfileType() {
        return new ProfileType();
    }

    /**
     * Create an instance of {@link AccountType }
     * 
     */
    public AccountType createAccountType() {
        return new AccountType();
    }

    /**
     * Create an instance of {@link InsuranceType }
     * 
     */
    public InsuranceType createInsuranceType() {
        return new InsuranceType();
    }

    /**
     * Create an instance of {@link LitigeType }
     * 
     */
    public LitigeType createLitigeType() {
        return new LitigeType();
    }

    /**
     * Create an instance of {@link ReceiverType }
     * 
     */
    public ReceiverType createReceiverType() {
        return new ReceiverType();
    }

    /**
     * Create an instance of {@link ClientType }
     * 
     */
    public ClientType createClientType() {
        return new ClientType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientsType }
     * {@code >}
     * 
     */
    @XmlElementDecl( namespace = "http://www.hlbexpress.com/namespaces/HLB-head-office", name = "clients" )
    public JAXBElement<ClientsType> createClients( ClientsType value ) {
        return new JAXBElement<ClientsType>( _Clients_QNAME, ClientsType.class, null, value );
    }

}