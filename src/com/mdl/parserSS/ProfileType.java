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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for profileType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="profileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="basic"/>
 *             &lt;enumeration value="advanced"/>
 *             &lt;enumeration value="premium"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="sentparcels" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="payementdefaults" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="wronglitiges" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="revenue" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "profileType" )
public class ProfileType {

    @XmlAttribute( name = "type", required = true )
    protected String type;
    @XmlAttribute( name = "sentparcels", required = true )
    protected int    sentparcels;
    @XmlAttribute( name = "payementdefaults", required = true )
    protected int    payementdefaults;
    @XmlAttribute( name = "wronglitiges", required = true )
    protected int    wronglitiges;
    @XmlAttribute( name = "revenue", required = true )
    protected float  revenue;

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setType( String value ) {
        this.type = value;
    }

    /**
     * Gets the value of the sentparcels property.
     * 
     */
    public int getSentparcels() {
        return sentparcels;
    }

    /**
     * Sets the value of the sentparcels property.
     * 
     */
    public void setSentparcels( int value ) {
        this.sentparcels = value;
    }

    /**
     * Gets the value of the payementdefaults property.
     * 
     */
    public int getPayementdefaults() {
        return payementdefaults;
    }

    /**
     * Sets the value of the payementdefaults property.
     * 
     */
    public void setPayementdefaults( int value ) {
        this.payementdefaults = value;
    }

    /**
     * Gets the value of the wronglitiges property.
     * 
     */
    public int getWronglitiges() {
        return wronglitiges;
    }

    /**
     * Sets the value of the wronglitiges property.
     * 
     */
    public void setWronglitiges( int value ) {
        this.wronglitiges = value;
    }

    /**
     * Gets the value of the revenue property.
     * 
     */
    public float getRevenue() {
        return revenue;
    }

    /**
     * Sets the value of the revenue property.
     * 
     */
    public void setRevenue( float value ) {
        this.revenue = value;
    }

}