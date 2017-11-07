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
package fr.aphp.tumorotek.action.cession.retour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;

/**
 * Classe 'Decorateur' qui reprend les attributs de Retour.
 * pour les afficher dans la liste associée.
 * Decorator créé le 03/04/2011.
 *
 * @version 2.0
 * @author Mathieu BARTHELEMY
 *
 */
public class RetourDecorator implements TKdataObject {
	
	private Retour retour;
	private ObjetStatut statut;
	
	public RetourDecorator(Retour r, ObjetStatut statut) {
		this.retour = r;
		this.statut = statut;
	}

	public Retour getRetour() {
		return retour;
	}

	public void setRetour(Retour r) {
		this.retour = r;
	}
	
	public String getDateSortie() {
		return ObjectTypesFormatters
			.dateRenderer2(retour.getDateSortie());
	}
	
	public String getDateRetour() {
		return ObjectTypesFormatters
			.dateRenderer2(retour.getDateRetour());
	}
	
	public String getTempMoyenne() {
		if (retour.getTempMoyenne() != null) {
			return retour.getTempMoyenne() + " °C";
		} 
		return null;
	}
	
	public String getSterile() {
		return ObjectTypesFormatters
			.booleanLitteralFormatter(retour.getSterile());
	}
	
	public String getCollaborateur() {
		if (retour.getCollaborateur() != null) {
			return retour.getCollaborateur().nomAndPrenom();
		}
		return null;
	}
	
	/**
	 * Affiche l'objet de la sortie si il existe sous la forme 
	 * entite: identifiant de la sortie.
	 * Pour transformation, l'identifiant est la liste des dérivés.
	 * @return objet de la sortie formatté
	 */
	public String getSortieObjet() {
		if (retour.getCession() != null) {
			return Labels.getLabel("Entite.Cession") 
				+ ": " + retour.getCession().getNumero();
		} else if (retour.getTransformation() != null) {
			return Labels.getLabel("Entite.Transformation") + ": " 
				+ Labels.getLabel("listeRetour.derives");
		} else if (retour.getIncident() != null) {
			return Labels.getLabel("Entite.Incident") + ": " 
				+ retour.getIncident().getNom();
		} 
//		else if (retour.getOldEmplacementAdrl() != null) {
//			return Labels.getLabel("ficheRetour.deplacement") + ": " 
//				+ retour.getOldEmplacementAdrl();
//		} 
		else {
			return retour.getObservations();
		}
	}
	
	/**
	 * Retourne le temps de sorti en mins, arrondi à la valeur inférieure
	 * calculé entre la date de sortie et la date de retour. Le calcul n'est 
	 * réalisé que si heure:mins sont renseignées.
	 * @return Integer le délai en nb de mins
	 */
	public Integer getDelaiInMins() {
		if (retour.getDateRetour() != null 
			&& (retour.getDateSortie().get(Calendar.HOUR_OF_DAY) > 0 
					|| retour.getDateSortie().get(Calendar.MINUTE) > 0) 
			&& (retour.getDateRetour().get(Calendar.HOUR_OF_DAY) > 0 
					|| retour.getDateRetour().get(Calendar.MINUTE) > 0)) {
			long milli = -1;
			
			milli = retour.getDateRetour().getTimeInMillis() 
					- retour.getDateSortie().getTimeInMillis();
			
			if (milli > 0) {
				int mins = Math.round(((float) milli / 60000));			
				return new Integer(mins);
			}
			
		}
		return null;
	}
	
	/**
	 * Retourne le temps de sorti avec le formate hhmm.
	 * @return
	 */
	public String getDelaiInHeureMin() {
		Integer delai = getDelaiInMins();
		
		if (delai != null && delai > 0) {
			return getHeureMinuteLabel(delai);
		} else {
			return null;
		}
	}
	
	/**
	 * Decore une liste de Retours.
	 * @param retours
	 * @return Retours décorés.
	 */
	public static List<RetourDecorator> 
							decorateListe(List<Retour> retours, ObjetStatut statut) {
		List<RetourDecorator> liste = 
									new ArrayList<RetourDecorator>();
		Iterator<Retour> it = retours.iterator();
		while (it.hasNext()) {
			liste.add(new RetourDecorator(it.next(), statut));
		}
		return liste;
	}
	
	/**
	 * Extrait les Retours d'une liste de Decorator.
	 * @param Retours decorators.
	 * @return Retous extraits.
	 */
	public static List<Retour> extractListe(List<RetourDecorator> retours) {
		List<Retour> liste = new ArrayList<Retour>();
		Iterator<RetourDecorator> it = retours.iterator();
		while (it.hasNext()) {
			liste.add(it.next().getRetour());
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
		
		RetourDecorator deco = (RetourDecorator) obj;
		return this.getRetour().equals(deco.getRetour());
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashContrat = 0;
		
		if (this.retour != null) {
			hashContrat = this.retour.hashCode();
		}
		
		hash = 7 * hash + hashContrat;

		return hash;
	}
	
	/**
	 * Retourne un délai formatté en heure/min.
	 * @param minuteValue Délai en minutes.
	 * @return Délai en heure/min.
	 */
	public String getHeureMinuteLabel(Integer minuteValue) {
		Integer heure = minuteValue / 60;
		Integer heureDelai = 0;
		Integer minDelai = 0;
		String resultat = "";
		if (heure > 0) {
			heureDelai = heure.intValue();
			minDelai = minuteValue - (heureDelai * 60);
			StringBuffer sb = new StringBuffer();
			sb.append(heureDelai.toString());
			sb.append("h ");
			sb.append(minDelai.toString());
			sb.append("min");
			resultat = sb.toString();
		} else {
			heureDelai = 0;
			minDelai = minuteValue;
			StringBuffer sb = new StringBuffer();
			sb.append(heureDelai.toString());
			sb.append("h ");
			sb.append(minDelai.toString());
			sb.append("min");
			resultat = sb.toString();
		}
		return resultat;
	}
	
	public String getRowStyle() {
		if (retour.getDateRetour() == null 
				&& statut != null 
				&& statut.getStatut().equals("ENCOURS")) {
			return "background-color : #FEBAB3";
		} else {
			return null;
		}
	}
	
	public String getLinkStyle() {
		if (retour.getDateRetour() == null 
				&& statut.getStatut().equals("ENCOURS")) {
			return "color : red";
		} else {
			return null;
		}
	}
	
	public String getEditClass() {
		if (statut != null && statut.getStatut().equals("ENCOURS")) {
			if (retour.getDateRetour() != null) {
				return "gridEditDsb";
			}
		}
		return "gridEdit";
	}

	@Override
	public Integer listableObjectId() {
		if (getRetour() != null) {
			return getRetour().getRetourId();
		}
		return null;
	}
	
	@Override
	public TKdataObject clone() {
		RetourDecorator deco = new RetourDecorator(getRetour(), statut);
		return deco;
	}
	
}
