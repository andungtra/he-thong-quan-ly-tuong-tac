//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.23 at 09:37:43 PM ICT 
//


package org.hcmus.tis.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xWorkItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xWorkItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xPreDefinedFields" type="{http://www.w3schools.com}xPreDefinedFields"/>
 *         &lt;element name="xAddionalFields" type="{http://www.w3schools.com}xAddionalFields"/>
 *       &lt;/sequence>
 *       &lt;attribute name="refName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xWorkItem", propOrder = {
    "xPreDefinedFields",
    "xAddionalFields"
})
public class XWorkItem {

    @XmlElement(required = true)
    protected XPreDefinedFields xPreDefinedFields;
    @XmlElement(required = true)
    protected XAddionalFields xAddionalFields;
    @XmlAttribute(name = "refName", required = true)
    protected String refName;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the xPreDefinedFields property.
     * 
     * @return
     *     possible object is
     *     {@link XPreDefinedFields }
     *     
     */
    public XPreDefinedFields getXPreDefinedFields() {
        return xPreDefinedFields;
    }

    /**
     * Sets the value of the xPreDefinedFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link XPreDefinedFields }
     *     
     */
    public void setXPreDefinedFields(XPreDefinedFields value) {
        this.xPreDefinedFields = value;
    }

    /**
     * Gets the value of the xAddionalFields property.
     * 
     * @return
     *     possible object is
     *     {@link XAddionalFields }
     *     
     */
    public XAddionalFields getXAddionalFields() {
        return xAddionalFields;
    }

    /**
     * Sets the value of the xAddionalFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link XAddionalFields }
     *     
     */
    public void setXAddionalFields(XAddionalFields value) {
        this.xAddionalFields = value;
    }

    /**
     * Gets the value of the refName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefName() {
        return refName;
    }

    /**
     * Sets the value of the refName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefName(String value) {
        this.refName = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
