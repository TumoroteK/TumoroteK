/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.aphp.tumorotek.interfacage.jaxb.hm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * JAXB annotated class.
 * Mappe l'élément formulaire d'un message DME envoyé à HopitalManager.
 * Date: 29/09/2014
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10.2
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formulaire", propOrder = {"identifiant", "patient", "venue", "dateValeur", "codForm", "libForm", "rubriques"})
public class Formulaire
{

   @XmlElement(name = "identifiant", required = true)
   private String identifiant;

   @XmlElement(name = "patient", required = true)
   private Formulaire.Patient patient;

   @XmlElement(name = "venue", required = true)
   private Formulaire.Venue venue;

   @XmlElement(name = "dateValeur", required = true)
   private Formulaire.DateValeur dateValeur;

   @XmlElement(name = "codForm", required = true)
   private Integer codForm = 69;

   @XmlElement(name = "libForm", required = true)
   private String libForm = "Biothèque";

   @XmlElementWrapper(name = "rubriques", required = true)
   @XmlElement(name = "rubrique")
   protected List<Rubrique> rubriques;

   // constructeur publique
   public Formulaire(){
      patient = new Formulaire.Patient();
      venue = new Formulaire.Venue();
      dateValeur = new Formulaire.DateValeur(Calendar.getInstance().getTime());
   }

   /**
    * Constructeur à partir de l'objet Prelevement de TK
    * @param Prelevement prel
    * @param Boolean testMessage true si test (codForm = 79)
    * @param Integer nb d'echantillons enregistrés avec le prélèvement
    */
   public Formulaire(final Prelevement prel, final Boolean testMessage, final Integer nbEchans){
      if(prel.getPrelevementId() != null){
         setIdentifiant(prel.getPrelevementId().toString());
      }
      patient = new Formulaire.Patient();
      if(prel.getMaladie() != null && prel.getMaladie().getPatient().getNip() != null){
         patient.setIpp(addTrailingZeros(prel.getMaladie().getPatient().getNip(), 8));
      }
      venue = new Formulaire.Venue();
      if(prel.getPatientNda() != null){
         venue.setNda(prel.getPatientNda());
      }
      dateValeur = new Formulaire.DateValeur(Calendar.getInstance().getTime());
      // test only
      if(testMessage){
         codForm = 79;
      }
      //Rubriques
      // banque critère spécifique Rennes BIOTECTIS/BIOTECLIQ
      final Rubrique rb1 = new Rubrique();
      rb1.setCodRub("LST_TYPPRELVT1");
      if(prel.getBanque() != null){
         if(prel.getBanque().getNom() != null && prel.getBanque().getNom().matches(".*Biothèque Sein.*")){
            rb1.getValeurs().add("LIQ");
         }else{
            rb1.getValeurs().add("TIS");
         }
      }else{
         rb1.getValeurs().add("");
      }
      getRubriques().add(rb1);
      // date prélèvement
      final Rubrique rb2 = new Rubrique();
      rb2.setCodRub("DAT_PRLVT_TUMO2");
      if(prel.getDatePrelevement() != null){
         rb2.getValeurs().add(new SimpleDateFormat("yyyy-MM-dd").format(prel.getDatePrelevement().getTime()));
      }else{
         rb2.getValeurs().add("");
      }
      getRubriques().add(rb2);
      // nature
      final Rubrique rb3 = new Rubrique();
      rb3.setCodRub("TXT_NAT_PRLVT3");
      if(prel.getNature() != null){
         rb3.getValeurs().add(prel.getNature().getNom());
      }else{
         rb3.getValeurs().add("");
      }
      getRubriques().add(rb3);
      // nb echans
      final Rubrique rb4 = new Rubrique();
      rb4.setCodRub("NUM_NB_ECHANT4");
      rb4.getValeurs().add(nbEchans != null ? nbEchans.toString() : "0");
      getRubriques().add(rb4);
   }

   public String getIdentifiant(){
      return identifiant;
   }

   public void setIdentifiant(final String i){
      this.identifiant = i;
   }

   public Formulaire.Patient getPatient(){
      return patient;
   }

   public void setPatient(final Formulaire.Patient p){
      this.patient = p;
   }

   public Formulaire.Venue getVenue(){
      return venue;
   }

   public void setVenue(final Formulaire.Venue v){
      this.venue = v;
   }

   public Formulaire.DateValeur getDateValeur(){
      return dateValeur;
   }

   public void setDateValeur(final Formulaire.DateValeur d){
      this.dateValeur = d;
   }

   public Integer getCodForm(){
      return codForm;
   }

   public void setCodForm(final Integer c){
      this.codForm = c;
   }

   public String getLibForm(){
      return libForm;
   }

   public void setLibForm(final String c){
      this.libForm = c;
   }

   public List<Rubrique> getRubriques(){
      if(rubriques == null){
         rubriques = new ArrayList<>();
      }
      return this.rubriques;
   }

   public void setRubriques(final List<Rubrique> rbs){
      this.rubriques = rbs;
   }

   @Override
   public String toString(){
      return "Dme[" + getIdentifiant() + "]";
   }

   @Override
   public boolean equals(final Object obj){
      return getIdentifiant().equals(((Formulaire) obj).getIdentifiant());
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashIdentifiant = 0;

      if(this.identifiant != null){
         hashIdentifiant = this.identifiant.hashCode();
      }

      hash = 31 * hash + hashIdentifiant;

      return hash;
   }

   /*************************************************************************/
   /************************ Inner Classe Patient ****************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "patient", propOrder = {"ipp"})
   public static class Patient
   {

      @XmlElement(name = "ipp", required = true)
      private String ipp = "";

      public String getIpp(){
         return ipp;
      }

      public void setIpp(final String i){
         this.ipp = i;
      }

      @Override
      public boolean equals(final Object obj){
         return getIpp().equals(((Formulaire.Patient) obj).getIpp());
      }

      @Override
      public int hashCode(){
         int hash = 7;
         int hashIpp = 0;

         if(this.ipp != null){
            hashIpp = this.ipp.hashCode();
         }

         hash = 31 * hash + hashIpp;

         return hash;
      }
   }

   /*************************************************************************/
   /************************ Inner Classe id ********************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "venue", propOrder = {"nda"})
   public static class Venue
   {

      @XmlElement(name = "nda", required = true)
      private String nda = "";

      public String getNda(){
         return nda;
      }

      public void setNda(final String n){
         this.nda = n;
      }

   }

   /*************************************************************************/
   /************************ Inner Classe FId ********************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "dateValeur", propOrder = {"date", "heure"})
   public static class DateValeur
   {

      @XmlElement(name = "date", required = true)
      private String date = null;

      @XmlElement(name = "heure", required = true)
      private String heure = null;

      public DateValeur(final Date dateComplete){
         if(dateComplete != null){
            setDate(new SimpleDateFormat("yyyy-MM-dd").format(dateComplete));
            setHeure(new SimpleDateFormat("HH:mm").format(dateComplete));
         }
      }

      public String getDate(){
         return date;
      }

      public void setDate(final String d){
         this.date = d;
      }

      public String getHeure(){
         return heure;
      }

      public void setHeure(final String h){
         this.heure = h;
      }
   }

   private String addTrailingZeros(String str, final int total){
      if(str != null){
         final int adds = total - str.length();
         if(adds > 0){ // nb de zeros à ajouter
            for(int i = 1; i <= adds; i++){
               str = '0' + str;
            }
         }
      }
      return str;
   }
}
