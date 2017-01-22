//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.01.14 at 04:24:38 PM CET 
//

package com.mdl.parserSS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for pickupType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="pickupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pickupaddress" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}addressType"/>
 *         &lt;element name="billingaddress" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}addressType" minOccurs="0"/>
 *         &lt;element name="deliveryaddress" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}addressType"/>
 *         &lt;element name="pickupcenter">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Li�ge"/>
 *               &lt;enumeration value="Hasselt"/>
 *               &lt;enumeration value="Arlon"/>
 *               &lt;enumeration value="Namur"/>
 *               &lt;enumeration value="Mons"/>
 *               &lt;enumeration value="Wavre"/>
 *               &lt;enumeration value="Louvain"/>
 *               &lt;enumeration value="Gand"/>
 *               &lt;enumeration value="Anvers"/>
 *               &lt;enumeration value="Bruges"/>
 *               &lt;enumeration value="A�roport-Bruxelles"/>
 *               &lt;enumeration value="A�roport-Li�ge"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliverycenter">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Li�ge"/>
 *               &lt;enumeration value="Hasselt"/>
 *               &lt;enumeration value="Arlon"/>
 *               &lt;enumeration value="Namur"/>
 *               &lt;enumeration value="Mons"/>
 *               &lt;enumeration value="Wavre"/>
 *               &lt;enumeration value="Louvain"/>
 *               &lt;enumeration value="Gand"/>
 *               &lt;enumeration value="Anvers"/>
 *               &lt;enumeration value="Bruges"/>
 *               &lt;enumeration value="A�roport-Bruxelles"/>
 *               &lt;enumeration value="A�roport-Li�ge"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="receiver" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}receiverType"/>
 *         &lt;element name="insurance" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}insuranceType"/>
 *         &lt;element name="receipt" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="declareddimension" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}dimension"/>
 *         &lt;element name="reviseddimension" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}dimension"/>
 *         &lt;element name="chargedamount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="extraamount" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="extranotification" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="litige" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}litigeType" minOccurs="0"/>
 *         &lt;element name="deliveryproblem" type="{http://www.hlbexpress.com/namespaces/HLB-head-office}deliveryproblemType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="requestdate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "pickupType", propOrder = {
        "pickupaddress",
        "billingaddress",
        "deliveryaddress",
        "pickupcenter",
        "deliverycenter",
        "receiver",
        "insurance",
        "receipt",
        "declareddimension",
        "reviseddimension",
        "chargedamount",
        "extraamount",
        "extranotification",
        "litige",
        "deliveryproblem"
} )
public class PickupType {

    @XmlElement( required = true )
    protected AddressType          pickupaddress;
    protected AddressType          billingaddress;
    @XmlElement( required = true )
    protected AddressType          deliveryaddress;
    @XmlElement( required = true )
    protected String               pickupcenter;
    @XmlElement( required = true )
    protected String               deliverycenter;
    @XmlElement( required = true )
    protected ReceiverType         receiver;
    @XmlElement( required = true )
    protected InsuranceType        insurance;
    protected boolean              receipt;
    @XmlElement( required = true )
    protected Dimension            declareddimension;
    @XmlElement( required = true )
    protected Dimension            reviseddimension;
    protected float                chargedamount;
    protected Float                extraamount;
    @XmlSchemaType( name = "dateTime" )
    protected XMLGregorianCalendar extranotification;
    protected LitigeType           litige;
    protected DeliveryproblemType  deliveryproblem;
    @XmlAttribute( name = "requestdate", required = true )
    @XmlSchemaType( name = "dateTime" )
    protected XMLGregorianCalendar requestdate;

    /**
     * Gets the value of the pickupaddress property.
     * 
     * @return possible object is {@link AddressType }
     * 
     */
    public AddressType getPickupaddress() {
        return pickupaddress;
    }

    /**
     * Sets the value of the pickupaddress property.
     * 
     * @param value
     *            allowed object is {@link AddressType }
     * 
     */
    public void setPickupaddress( AddressType value ) {
        this.pickupaddress = value;
    }

    /**
     * Gets the value of the billingaddress property.
     * 
     * @return possible object is {@link AddressType }
     * 
     */
    public AddressType getBillingaddress() {
        return billingaddress;
    }

    /**
     * Sets the value of the billingaddress property.
     * 
     * @param value
     *            allowed object is {@link AddressType }
     * 
     */
    public void setBillingaddress( AddressType value ) {
        this.billingaddress = value;
    }

    /**
     * Gets the value of the deliveryaddress property.
     * 
     * @return possible object is {@link AddressType }
     * 
     */
    public AddressType getDeliveryaddress() {
        return deliveryaddress;
    }

    /**
     * Sets the value of the deliveryaddress property.
     * 
     * @param value
     *            allowed object is {@link AddressType }
     * 
     */
    public void setDeliveryaddress( AddressType value ) {
        this.deliveryaddress = value;
    }

    /**
     * Gets the value of the pickupcenter property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPickupcenter() {
        return pickupcenter;
    }

    /**
     * Sets the value of the pickupcenter property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPickupcenter( String value ) {
        this.pickupcenter = value;
    }

    /**
     * Gets the value of the deliverycenter property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDeliverycenter() {
        return deliverycenter;
    }

    /**
     * Sets the value of the deliverycenter property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDeliverycenter( String value ) {
        this.deliverycenter = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * @return possible object is {@link ReceiverType }
     * 
     */
    public ReceiverType getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     * 
     * @param value
     *            allowed object is {@link ReceiverType }
     * 
     */
    public void setReceiver( ReceiverType value ) {
        this.receiver = value;
    }

    /**
     * Gets the value of the insurance property.
     * 
     * @return possible object is {@link InsuranceType }
     * 
     */
    public InsuranceType getInsurance() {
        return insurance;
    }

    /**
     * Sets the value of the insurance property.
     * 
     * @param value
     *            allowed object is {@link InsuranceType }
     * 
     */
    public void setInsurance( InsuranceType value ) {
        this.insurance = value;
    }

    /**
     * Gets the value of the receipt property.
     * 
     */
    public boolean isReceipt() {
        return receipt;
    }

    /**
     * Sets the value of the receipt property.
     * 
     */
    public void setReceipt( boolean value ) {
        this.receipt = value;
    }

    /**
     * Gets the value of the declareddimension property.
     * 
     * @return possible object is {@link Dimension }
     * 
     */
    public Dimension getDeclareddimension() {
        return declareddimension;
    }

    /**
     * Sets the value of the declareddimension property.
     * 
     * @param value
     *            allowed object is {@link Dimension }
     * 
     */
    public void setDeclareddimension( Dimension value ) {
        this.declareddimension = value;
    }

    /**
     * Gets the value of the reviseddimension property.
     * 
     * @return possible object is {@link Dimension }
     * 
     */
    public Dimension getReviseddimension() {
        return reviseddimension;
    }

    /**
     * Sets the value of the reviseddimension property.
     * 
     * @param value
     *            allowed object is {@link Dimension }
     * 
     */
    public void setReviseddimension( Dimension value ) {
        this.reviseddimension = value;
    }

    /**
     * Gets the value of the chargedamount property.
     * 
     */
    public float getChargedamount() {
        return chargedamount;
    }

    /**
     * Sets the value of the chargedamount property.
     * 
     */
    public void setChargedamount( float value ) {
        this.chargedamount = value;
    }

    /**
     * Gets the value of the extraamount property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getExtraamount() {
        return extraamount;
    }

    /**
     * Sets the value of the extraamount property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setExtraamount( Float value ) {
        this.extraamount = value;
    }

    /**
     * Gets the value of the extranotification property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getExtranotification() {
        return extranotification;
    }

    /**
     * Sets the value of the extranotification property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setExtranotification( XMLGregorianCalendar value ) {
        this.extranotification = value;
    }

    /**
     * Gets the value of the litige property.
     * 
     * @return possible object is {@link LitigeType }
     * 
     */
    public LitigeType getLitige() {
        return litige;
    }

    /**
     * Sets the value of the litige property.
     * 
     * @param value
     *            allowed object is {@link LitigeType }
     * 
     */
    public void setLitige( LitigeType value ) {
        this.litige = value;
    }

    /**
     * Gets the value of the deliveryproblem property.
     * 
     * @return possible object is {@link DeliveryproblemType }
     * 
     */
    public DeliveryproblemType getDeliveryproblem() {
        return deliveryproblem;
    }

    /**
     * Sets the value of the deliveryproblem property.
     * 
     * @param value
     *            allowed object is {@link DeliveryproblemType }
     * 
     */
    public void setDeliveryproblem( DeliveryproblemType value ) {
        this.deliveryproblem = value;
    }

    /**
     * Gets the value of the requestdate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getRequestdate() {
        return requestdate;
    }

    /**
     * Sets the value of the requestdate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setRequestdate( XMLGregorianCalendar value ) {
        this.requestdate = value;
    }

}
