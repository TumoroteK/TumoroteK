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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * JAXB annotated class.
 * Mappe un bloc action de patient de message HPRIM (1.03) envoyé par GEMA.
 * Evenement patient peut être: enregistrementPatient, venuePatient, fusionPatient 
 * donc contient obligatoirement un element Patient et optionellement un élément 
 * patientElimine en cas de fusion.
 * Date: 14/08/2012
 * 
 * @author mathieu
 * @version 2.0.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "evenementPatient")
@XmlType(name = "")
public class ActionPatient {

	// attribut action= {création, modification, 
	// remplacement, fusion, defusion, suppression}
	@XmlAttribute(name = "action", required = true)
    private String action = null;
	@XmlElement(name = "patient", required = true)
	private Patient patient;
	@XmlElement(name = "patientElimine", required = false)
	private Patient patientElimine;
	@XmlElement(required = false)
	private ActionPatient.Venue venue;
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String a) {
		this.action = a;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient p) {
		this.patient = p;
	}

	public Patient getPatientElimine() {
		return patientElimine;
	}

	public void setPatientElimine(Patient p) {
		this.patientElimine = p;
	}

	@Override
	public boolean equals(Object obj) {
		
		boolean res = getPatient()
			.equals(((ActionPatient) obj).getPatient());
		
		if (action.equals("fusion")) {
			res = res && getPatientElimine()
				.equals(((ActionPatient) obj).getPatientElimine());
		}
		
		return res;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		int hashPatient = 0;
		int hashPatientElimine = 0;
		
		if (this.patient != null) {
			hashPatient = this.patient.hashCode();
		}
		if (this.patientElimine != null) {
			hashPatientElimine = this.patientElimine.hashCode();
		}
		
		hash = 31 * hash + hashPatient;
		hash = 31 * hash + hashPatientElimine;
		
		return hash;
	}
	
	/*******************************************************/
	/******************* Venue = NDA************************/
	/*******************************************************/
	
	public ActionPatient.Venue getVenue() {
		return venue;
	}

	public void setMelbase(ActionPatient.Venue v) {
		this.venue = v;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
			"identifiant"
	})
	public static class Venue {

		@XmlElement(required = false)
		private ActionPatient.Venue.Identifiant identifiant;
	
		public ActionPatient.Venue.Identifiant getIdentifiant() {
			return identifiant;
		}

		public void setIdentifiant(ActionPatient.Venue.Identifiant id) {
			this.identifiant = id;
		}
		
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = {
				"emetteur"
		})
		public static class Identifiant {

			@XmlElement(required = false)
		 	private ActionPatient.Venue.Identifiant.Emetteur emetteur;
			 
			public ActionPatient.Venue.Identifiant.Emetteur getEmetteur() {
				return emetteur;
			}

			public void setEmetteur(ActionPatient.Venue
										.Identifiant.Emetteur emet) {
				this.emetteur = emet;
			}
			 
			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = {
					"valeur"
			})
			public static class Emetteur {

				@XmlElement(required = false)
			 	private ActionPatient.Venue.Identifiant.Emetteur.Valeur valeur;
				 
				public ActionPatient.Venue.Identifiant.Emetteur.Valeur getValeur() {
					return valeur;
				}

				public void setValeur(ActionPatient.Venue.Identifiant.Emetteur.Valeur v) {
					this.valeur = v;
				}
				
				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "valeur")
				public static class Valeur {

					@XmlValue
					private String value;
					 
					public String getValue() {
						return value;
					}

					public void setValue(String value) {
						this.value = value;
					}
				}
			}
		}
		
	}
}