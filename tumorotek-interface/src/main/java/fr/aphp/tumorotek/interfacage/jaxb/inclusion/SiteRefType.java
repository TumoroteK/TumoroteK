//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.10.23 at 04:30:23 PM CEST
//

package fr.aphp.tumorotek.interfacage.jaxb.inclusion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for siteRefType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="siteRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://openclinica.org/ws/beans}customStringType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "siteRefType", propOrder = {"identifier"})
public class SiteRefType
{

   @XmlElement(required = true)
   @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
   protected String identifier;

   /**
    * Gets the value of the identifier property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getIdentifier(){
      return identifier;
   }

   /**
    * Sets the value of the identifier property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setIdentifier(final String value){
      this.identifier = value;
   }

}
