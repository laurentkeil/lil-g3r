//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.01.14 at 04:27:31 PM CET 
//


package com.mdl.parserDC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vehicleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehicleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dimension" type="{http://www.hlbexpress.com/namespaces/HLB-dispatching-centers}dimension"/>
 *         &lt;element name="employeeincharge" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="model" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="usefulweight" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="passengernumber" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehicleType", propOrder = {
    "dimension",
    "employeeincharge"
})
public class VehicleType {

    @XmlElement(required = true)
    protected Dimension dimension;
    @XmlElement(required = true)
    protected String employeeincharge;
    @XmlAttribute(name = "model")
    protected String model;
    @XmlAttribute(name = "usefulweight")
    protected Float usefulweight;
    @XmlAttribute(name = "passengernumber")
    protected Integer passengernumber;

    /**
     * Gets the value of the dimension property.
     * 
     * @return
     *     possible object is
     *     {@link Dimension }
     *     
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Sets the value of the dimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dimension }
     *     
     */
    public void setDimension(Dimension value) {
        this.dimension = value;
    }

    /**
     * Gets the value of the employeeincharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeincharge() {
        return employeeincharge;
    }

    /**
     * Sets the value of the employeeincharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeincharge(String value) {
        this.employeeincharge = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the usefulweight property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getUsefulweight() {
        return usefulweight;
    }

    /**
     * Sets the value of the usefulweight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setUsefulweight(Float value) {
        this.usefulweight = value;
    }

    /**
     * Gets the value of the passengernumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPassengernumber() {
        return passengernumber;
    }

    /**
     * Sets the value of the passengernumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPassengernumber(Integer value) {
        this.passengernumber = value;
    }

}
