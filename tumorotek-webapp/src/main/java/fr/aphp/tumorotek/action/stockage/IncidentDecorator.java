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
package fr.aphp.tumorotek.action.stockage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.stockage.Incident;

/**
 * Classe 'Decorateur' qui reprend les attributs d'Incident.
 * pour les afficher dans la liste associée.

 * date: 25/03/10.
 *
 * @version 2.0
 * @author Pierre Ventadour
 *
 */
public class IncidentDecorator {

	private Incident incident;
	
	public IncidentDecorator(Incident incid) {
		this.incident = incid;
	}

	public Incident getIncident() {
		return incident;
	}

	public void setIncident(Incident i) {
		this.incident = i;
	}
	
	public String getNom() {
		return this.incident.getNom();
	}
	
	public String getDescription() {
		return this.incident.getDescription();
	}
	
	public String getDateFormatted() {
		SimpleDateFormat df;
		df = new SimpleDateFormat(Labels
					.getLabel("validation.date.format"));
		return df.format(this.incident.getDate());
	}
	
	/**
	 * Decore une liste de cessions.
	 * @param cessions
	 * @return Cessions décorées.
	 */
	public static List<IncidentDecorator> 
			decorateListe(List<Incident> incidents) {
		List<IncidentDecorator> liste = 
									new ArrayList<IncidentDecorator>();
		Iterator<Incident> it = incidents.iterator();
		while (it.hasNext()) {
			liste.add(new IncidentDecorator(it.next()));
		}
		return liste;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		IncidentDecorator deco = (IncidentDecorator) obj;
		return this.getIncident().equals(deco.getIncident());
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashIncident = 0;
		
		if (this.incident != null) {
			hashIncident = this.incident.hashCode();
		}
		
		hash = 7 * hash + hashIncident;

		return hash;
	}
	
}
