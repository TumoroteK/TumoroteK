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
package fr.aphp.tumorotek.interfacage.jaxb.hprim;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.aphp.tumorotek.interfacage.jaxb.adapters.hprim.HprimDateAdapter;

/**
 * JAXB annotated class.
 * Mappe un bloc HPRIM PersonnePhysique (1.03) envoyé par GEMA.
 * Date: 14/08/2012
 *
 * @author mathieu
 * @version 2.0.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "personnePhysique")
@XmlType(name = "")
public class PersonnePhysique
{

   @XmlAttribute(name = "sexe", required = true)
   private String sexe = null;
   @XmlElement(name = "nomUsuel", required = true)
   private String nom = null;
   @XmlElement(name = "nomNaissance")
   private String nomPatro;
   @XmlElementWrapper(name = "prenoms")
   @XmlElement(name = "prenom")
   private ArrayList<String> prenomList = null;
   @XmlElement(name = "dateNaissance")
   private DateNaissanceWrapper dateNaissanceWrapper = null;
   @XmlElement(name = "CDMORT")
   private String etatPatient = null;

   public String getSexe(){
      if(sexe != null && sexe.equals("")){
         return null;
      }
      return sexe;
   }

   public void setSexe(final String s){
      this.sexe = s;
   }

   public String getNom(){
      if(nom != null && nom.equals("")){
         return null;
      }
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   public String getNomPatro(){
      if(nomPatro != null && nomPatro.equals("")){
         return null;
      }
      return nomPatro;
   }

   public void setNomPatro(final String np){
      this.nomPatro = np;
   }

   public ArrayList<String> getPrenomList(){
      return prenomList;
   }

   public void setPrenomList(final ArrayList<String> pList){
      this.prenomList = pList;
   }

   public String getPrenom(){
      if(prenomList == null || prenomList.isEmpty()){
         return null;
      }
      return prenomList.get(0);
   }

   public DateNaissanceWrapper getDateNaissanceWrapper(){
      return dateNaissanceWrapper;
   }

   public void setDateNaissanceWrapper(final DateNaissanceWrapper dW){
      this.dateNaissanceWrapper = dW;
   }

   public Date getDateNaissance(){
      if(getDateNaissanceWrapper() != null){
         return dateNaissanceWrapper.getDate();
      }
      return null;
   }

   public String getEtatPatient(){
      if(etatPatient != null && etatPatient.equals("")){
         return null;
      }
      return etatPatient;
   }

   public void setEtatPatient(final String eP){
      this.etatPatient = eP;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "")
   public static class DateNaissanceWrapper
   {
      @XmlElement(name = "date")
      @XmlJavaTypeAdapter(value = HprimDateAdapter.class)
      private Date date;

      public Date getDate(){
         return date;
      }

      public void setDate(final Date d){
         this.date = d;
      }
   }
}
