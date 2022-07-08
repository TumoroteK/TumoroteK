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
package fr.aphp.tumorotek.interfacage.jaxb.gilda;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.aphp.tumorotek.interfacage.jaxb.SipMessage;
import fr.aphp.tumorotek.interfacage.jaxb.adapters.gilda.GildaDateAdapter;
import fr.aphp.tumorotek.model.interfacage.PatientSip;

/**
 * JAXB annotated class.
 * Mappe un message ICAR envoyé par GILDA.
 * Utilise deux inner classes: une pour l'entete, une pour l'id du patient.
 * Date: 14/04/2011
 *
 * @author mathieu
 * @version 2.0
 * @see http://www.jmdoudoux.fr/java/dej/chap-jaxb.htm
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MESPAT")
@XmlType(name = "")
public class GildaMessage implements SipMessage
{

   @XmlElement(name = "ENTETE", required = true)
   private GildaMessage.Entete entete;

   @XmlElement(name = "ID", required = true)
   private GildaMessage.Id id;

   @XmlElement(name = "FID", required = false)
   private GildaMessage.FId fid;

   // constructeur publique
   public GildaMessage(){
      entete = new GildaMessage.Entete();
      id = new GildaMessage.Id();
      fid = new GildaMessage.FId();
   }

   public GildaMessage.Entete getEntete(){
      return entete;
   }

   public void setEntete(final GildaMessage.Entete en){
      this.entete = en;
   }

   public GildaMessage.Id getId(){
      return id;
   }

   public void setId(final GildaMessage.Id i){
      this.id = i;
   }

   public GildaMessage.FId getFid(){
      return fid;
   }

   public void setFid(final GildaMessage.FId f){
      this.fid = f;
   }

   @Override
   public String getNip(){
      return this.id.getNip();
   }

   @Override
   public String toString(){
      return "Message[" + getEntete().getNumEvt() + " nip: " + getId().getNip() + "]";
   }

   @Override
   public boolean equals(final Object obj){
      return getEntete().equals(((GildaMessage) obj).getEntete());
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashEntete = 0;

      if(this.entete != null){
         hashEntete = this.entete.hashCode();
      }

      hash = 31 * hash + hashEntete;

      return hash;
   }

   @Override
   public PatientSip toPatientSip(){
      final PatientSip sip = new PatientSip();
      sip.setNip(this.id.getNip());
      sip.setNom(this.id.getNom());
      sip.setNomNaissance(this.id.getNomPatro());
      sip.setPrenom(this.id.getPrenom());
      sip.setSexe(this.id.getSexe());
      sip.setDateNaissance(this.id.getDateNaissance());
      if(this.id.getEtatPatient() != null){
         if(this.id.getEtatPatient().equals("O")){
            sip.setPatientEtat("D");
         }
      }
      return sip;
   }

   @Override
   public PatientSip toPatientActif(){
      PatientSip sipA = null;
      if(getFid() != null){
         sipA = new PatientSip();
         sipA.setNip(this.fid.getNipA());
         sipA.setNom(this.fid.getNomA());
         sipA.setPrenom(this.fid.getPrenomA());
         sipA.setSexe(this.fid.getSexeA());
         sipA.setDateNaissance(this.fid.getDateNaissanceA());
      }
      return sipA;
   }

   @Override
   public PatientSip toPatientPassif(){
      PatientSip sipP = null;
      if(getFid() != null){
         sipP = new PatientSip();
         sipP.setNip(this.fid.getNipP());
         sipP.setNom(this.fid.getNomP());
         sipP.setPrenom(this.fid.getPrenomP());
         sipP.setSexe(this.fid.getSexeP());
         sipP.setDateNaissance(this.fid.getDateNaissanceP());
      }
      return sipP;
   }

   /*************************************************************************/
   /************************ Inner Classe Entete ****************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "")
   public static class Entete
   {

      @XmlElement(name = "CDEVT", required = true)
      private String codeEvt;

      @XmlElement(name = "NOEVT", required = true)
      private Long numEvt;

      @XmlElement(name = "DAMSG", required = true)
      private String dateMes;

      @XmlElement(name = "TYMSG", required = true)
      private Integer typeMes;

      public String getCodeEvt(){
         return codeEvt;
      }

      public void setCodeEvt(final String c){
         this.codeEvt = c;
      }

      public Long getNumEvt(){
         return numEvt;
      }

      public void setNumEvt(final Long n){
         this.numEvt = n;
      }

      public String getDateMes(){
         return dateMes;
      }

      public void setDateMes(final String d){
         this.dateMes = d;
      }

      public Integer getTypeMes(){
         return typeMes;
      }

      public void setTypeMes(final Integer t){
         this.typeMes = t;
      }

      @Override
      public boolean equals(final Object obj){
         return getNumEvt().equals(((GildaMessage.Entete) obj).getNumEvt());
      }

      @Override
      public int hashCode(){
         int hash = 7;
         int hashNumEvt = 0;

         if(this.numEvt != null){
            hashNumEvt = this.numEvt.hashCode();
         }

         hash = 31 * hash + hashNumEvt;

         return hash;
      }
   }

   /*************************************************************************/
   /************************ Inner Classe id ********************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "")
   public static class Id
   {

      @XmlElement(name = "NOIP", required = true)
      private String nip = null;

      @XmlElement(name = "NMMAL", required = true)
      private String nom = null;

      @XmlElement(name = "NMPATR")
      private String nomPatro;

      @XmlElement(name = "NMPMAL", required = true)
      private String prenom = null;

      @XmlElement(name = "DANAIS", required = true)
      @XmlJavaTypeAdapter(value = GildaDateAdapter.class)
      private Date dateNaissance = null;

      @XmlElement(name = "CDSEXM", required = true)
      private String sexe = null;

      @XmlElement(name = "CDMORT")
      private String etatPatient = null;

      public String getNip(){
         if(nip != null && nip.equals("")){
            return null;
         }
         return nip;
      }

      public void setNip(final String n){
         this.nip = n;
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

      public String getPrenom(){
         if(prenom != null && prenom.equals("")){
            return null;
         }
         return prenom;
      }

      public void setPrenom(final String p){
         this.prenom = p;
      }

      public Date getDateNaissance(){
         return dateNaissance;
      }

      public void setDateNaissance(final Date dN){
         this.dateNaissance = dN;
      }

      public String getSexe(){
         if(sexe != null && sexe.equals("")){
            return null;
         }
         return sexe;
      }

      public void setSexe(final String s){
         this.sexe = s;
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
   }

   /*************************************************************************/
   /************************ Inner Classe FId ********************************/
   /*************************************************************************/

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(name = "")
   public static class FId
   {

      @XmlElement(name = "NOIP", required = true)
      private String nipP = null;

      @XmlElement(name = "NMMAL", required = true)
      private String nomP = null;

      @XmlElement(name = "NMPMAL", required = true)
      private String prenomP = null;

      @XmlElement(name = "DANAIS", required = true)
      @XmlJavaTypeAdapter(value = GildaDateAdapter.class)
      private Date dateNaissanceP = null;

      @XmlElement(name = "CDSEXM", required = true)
      private String sexeP = null;

      @XmlElement(name = "NOIPFU", required = true)
      private String nipA = null;

      @XmlElement(name = "NMMALA", required = true)
      private String nomA = null;

      @XmlElement(name = "NMPMALA", required = true)
      private String prenomA = null;

      @XmlElement(name = "DANAISA", required = true)
      @XmlJavaTypeAdapter(value = GildaDateAdapter.class)
      private Date dateNaissanceA = null;

      @XmlElement(name = "CDSEXMA", required = true)
      private String sexeA = null;

      public String getNipP(){
         if(nipP != null && nipP.equals("")){
            return null;
         }
         return nipP;
      }

      public void setNipP(final String n){
         this.nipP = n;
      }

      public String getNipA(){
         if(nipA != null && nipA.equals("")){
            return null;
         }
         return nipA;
      }

      public void setNipA(final String n){
         this.nipA = n;
      }

      public String getNomP(){
         if(nomP != null && nomP.equals("")){
            return null;
         }
         return nomP;
      }

      public void setNomP(final String n){
         this.nomP = n;
      }

      public String getNomA(){
         if(nomA != null && nomA.equals("")){
            return null;
         }
         return nomA;
      }

      public void setNomA(final String n){
         this.nomA = n;
      }

      public String getPrenomP(){
         if(prenomP != null && prenomP.equals("")){
            return null;
         }
         return prenomP;
      }

      public void setPrenomP(final String p){
         this.prenomP = p;
      }

      public String getPrenomA(){
         if(prenomA != null && prenomA.equals("")){
            return null;
         }
         return prenomA;
      }

      public void setPrenomA(final String p){
         this.prenomA = p;
      }

      public Date getDateNaissanceP(){
         return dateNaissanceP;
      }

      public void setDateNaissanceP(final Date dN){
         this.dateNaissanceP = dN;
      }

      public Date getDateNaissanceA(){
         return dateNaissanceA;
      }

      public void setDateNaissanceA(final Date dN){
         this.dateNaissanceA = dN;
      }

      public String getSexeP(){
         if(sexeP != null && sexeP.equals("")){
            return null;
         }
         return sexeP;
      }

      public void setSexeP(final String s){
         this.sexeP = s;
      }

      public String getSexeA(){
         if(sexeA != null && sexeA.equals("")){
            return null;
         }
         return sexeA;
      }

      public void setSexeA(final String s){
         this.sexeA = s;
      }
   }
}
