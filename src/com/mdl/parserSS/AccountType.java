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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for accountType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="accountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="email" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="registrationdate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="activated" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="lastconnexion" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "accountType" )
public class AccountType {

    @XmlAttribute( name = "email", required = true )
    protected String               email;
    @XmlAttribute( name = "password", required = true )
    protected String               password;
    @XmlAttribute( name = "registrationdate", required = true )
    @XmlSchemaType( name = "dateTime" )
    protected XMLGregorianCalendar registrationdate;
    @XmlAttribute( name = "activated", required = true )
    protected boolean              activated;
    @XmlAttribute( name = "lastconnexion" )
    @XmlSchemaType( name = "dateTime" )
    protected XMLGregorianCalendar lastconnexion;

    /**
     * Gets the value of the email property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEmail( String value ) {
        this.email = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPassword( String value ) {
        this.password = value;
    }

    /**
     * Gets the value of the registrationdate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getRegistrationdate() {
        return registrationdate;
    }

    /**
     * Sets the value of the registrationdate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setRegistrationdate( XMLGregorianCalendar value ) {
        this.registrationdate = value;
    }

    /**
     * Gets the value of the activated property.
     * 
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Sets the value of the activated property.
     * 
     */
    public void setActivated( boolean value ) {
        this.activated = value;
    }

    /**
     * Gets the value of the lastconnexion property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getLastconnexion() {
        return lastconnexion;
    }

    /**
     * Sets the value of the lastconnexion property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setLastconnexion( XMLGregorianCalendar value ) {
        this.lastconnexion = value;
    }

}
