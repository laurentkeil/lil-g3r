//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.01.14 at 04:27:31 PM CET 
//


package com.mdl.parserDC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for centersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="centersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="center" type="{http://www.hlbexpress.com/namespaces/HLB-dispatching-centers}centerType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "centersType", propOrder = {
    "center"
})
@XmlRootElement(name="centers", namespace="http://www.hlbexpress.com/namespaces/HLB-dispatching-centers")
public class CentersType {

    @XmlElement(required = true)
    protected CenterType center;

    /**
     * Gets the value of the center property.
     * 
     * @return
     *     possible object is
     *     {@link CenterType }
     *     
     */
    public CenterType getCenter() {
        return center;
    }

    /**
     * Sets the value of the center property.
     * 
     * @param value
     *     allowed object is
     *     {@link CenterType }
     *     
     */
    public void setCenter(CenterType value) {
        this.center = value;
    }

}