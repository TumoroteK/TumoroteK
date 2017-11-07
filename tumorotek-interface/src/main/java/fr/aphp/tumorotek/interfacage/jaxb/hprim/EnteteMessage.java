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
 * Mappe un bloc entete de message HPRIM (1.03) envoyé par GEMA.
 * Date: 14/08/2012
 * 
 * @author mathieu
 * @version 2.0.8
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "enteteMessage")
@XmlType(name = "")
public class EnteteMessage {

	@XmlElement(name = "identifiantMessage", required = true)
	private Long numEvt;
	@XmlElement(name = "dateHeureProduction", required = true)
	private String dateMes;
	
	public Long getNumEvt() {
		return numEvt;
	}
	
	public void setNumEvt(Long n) {
		this.numEvt = n;
	}
	
	public String getDateMes() {
		return dateMes;
	}
	
	public void setDateMes(String d) {
		this.dateMes = d;
	}
	
	@Override
	public boolean equals(Object obj) {
		return getNumEvt()
			.equals(((EnteteMessage) obj).getNumEvt());
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		int hashNumEvt = 0;
		
		if (this.numEvt != null) {
			hashNumEvt = this.numEvt.hashCode();
		}
		
		hash = 31 * hash + hashNumEvt;
		
		return hash;
	}
}