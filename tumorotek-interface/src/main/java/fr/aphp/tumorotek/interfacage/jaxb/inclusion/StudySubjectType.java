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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for studySubjectType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="studySubjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="label" type="{http://openclinica.org/ws/beans}customStringType" minOccurs="0"/>
 *         &lt;element name="secondaryLabel" type="{http://openclinica.org/ws/beans}customStringType" minOccurs="0"/>
 *         &lt;element name="enrollmentDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="subject" type="{http://openclinica.org/ws/beans}subjectType"/>
 *         &lt;element name="studyRef" type="{http://openclinica.org/ws/beans}studyRefType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studySubjectType", propOrder = {"label", "secondaryLabel", "enrollmentDate", "subject", "studyRef"})
public class StudySubjectType
{

   @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
   protected String label;
   @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
   protected String secondaryLabel;
   @XmlElement(required = true)
   @XmlSchemaType(name = "date")
   protected XMLGregorianCalendar enrollmentDate;
   @XmlElement(required = true)
   protected SubjectType subject;
   @XmlElement(required = true)
   protected StudyRefType studyRef;

   /**
    * Gets the value of the label property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getLabel(){
      return label;
   }

   /**
    * Sets the value of the label property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setLabel(final String value){
      this.label = value;
   }

   /**
    * Gets the value of the secondaryLabel property.
    * 
    * @return
    *     possible object is
    *     {@link String }
    *     
    */
   public String getSecondaryLabel(){
      return secondaryLabel;
   }

   /**
    * Sets the value of the secondaryLabel property.
    * 
    * @param value
    *     allowed object is
    *     {@link String }
    *     
    */
   public void setSecondaryLabel(final String value){
      this.secondaryLabel = value;
   }

   /**
    * Gets the value of the enrollmentDate property.
    * 
    * @return
    *     possible object is
    *     {@link XMLGregorianCalendar }
    *     
    */
   public XMLGregorianCalendar getEnrollmentDate(){
      return enrollmentDate;
   }

   /**
    * Sets the value of the enrollmentDate property.
    * 
    * @param value
    *     allowed object is
    *     {@link XMLGregorianCalendar }
    *     
    */
   public void setEnrollmentDate(final XMLGregorianCalendar value){
      this.enrollmentDate = value;
   }

   /**
    * Gets the value of the subject property.
    * 
    * @return
    *     possible object is
    *     {@link SubjectType }
    *     
    */
   public SubjectType getSubject(){
      return subject;
   }

   /**
    * Sets the value of the subject property.
    * 
    * @param value
    *     allowed object is
    *     {@link SubjectType }
    *     
    */
   public void setSubject(final SubjectType value){
      this.subject = value;
   }

   /**
    * Gets the value of the studyRef property.
    * 
    * @return
    *     possible object is
    *     {@link StudyRefType }
    *     
    */
   public StudyRefType getStudyRef(){
      return studyRef;
   }

   /**
    * Sets the value of the studyRef property.
    * 
    * @param value
    *     allowed object is
    *     {@link StudyRefType }
    *     
    */
   public void setStudyRef(final StudyRefType value){
      this.studyRef = value;
   }

}