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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB annotated class.
 * Mappe un bloc EvenementPatient de message HPRIM (1.03) envoyé par GEMA.
 * Evenement patient peut contenir: enregistrementPatient, venuePatient, fusionPatient
 * Date: 14/08/2012
 *
 * @author mathieu
 * @version 2.0.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "evenementPatient")
@XmlType(name = "")
public class EvenementPatient
{

   @XmlElement(name = "enregistrementPatient", required = false)
   private ActionPatient enregistrementPatient;

   @XmlElement(name = "venuePatient", required = false)
   private ActionPatient venuePatient;

   @XmlElement(name = "fusionPatient", required = false)
   private ActionPatient fusionPatient;

   public ActionPatient getEnregistrementPatient(){
      return enregistrementPatient;
   }

   public void setEnregistrementPatient(final ActionPatient e){
      this.enregistrementPatient = e;
   }

   public ActionPatient getVenuePatient(){
      return venuePatient;
   }

   public void setVenuePatient(final ActionPatient v){
      this.venuePatient = v;
   }

   public ActionPatient getFusionPatient(){
      return fusionPatient;
   }

   public void setFusionPatient(final ActionPatient f){
      this.fusionPatient = f;
   }

   public ActionPatient getActionPatient(){
      if(getEnregistrementPatient() != null){
         return getEnregistrementPatient();
      }else if(getVenuePatient() != null){
         return getVenuePatient();
      }else if(getFusionPatient() != null){
         return getFusionPatient();
      }
      return null;
   }

   @Override
   public boolean equals(final Object obj){

      if(getEnregistrementPatient() != null){
         return getEnregistrementPatient().equals(((EvenementPatient) obj).getEnregistrementPatient());
      }else if(getVenuePatient() != null){
         return getVenuePatient().equals(((EvenementPatient) obj).getVenuePatient());
      }else if(getFusionPatient() != null){
         return getFusionPatient().equals(((EvenementPatient) obj).getFusionPatient());
      }

      return false;
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashEnregistrementPatient = 0;
      int hashVenuePatient = 0;
      final int hashFusionPatient = 0;

      if(this.enregistrementPatient != null){
         hashEnregistrementPatient = this.enregistrementPatient.hashCode();
      }
      if(this.venuePatient != null){
         hashVenuePatient = this.venuePatient.hashCode();
      }
      if(this.fusionPatient != null){
         hashVenuePatient = this.fusionPatient.hashCode();
      }

      hash = 31 * hash + hashEnregistrementPatient;
      hash = 31 * hash + hashVenuePatient;
      hash = 31 * hash + hashFusionPatient;

      return hash;
   }
}
