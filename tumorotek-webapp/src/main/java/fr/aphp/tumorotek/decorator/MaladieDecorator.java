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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;

/**
 * Classe 'Decorateur' qui reprend les attributs de Maladie.
 * pour les afficher par l'interface dans une liste de Maladie:
 *  - Libelle
 *  - Code
 *  - Date diagnostic
 *  - Nb de maladies enregistrées
 * date: 02/12/09
 *
 * @version 2.0
 * @author mathieu
 *
 */
public class MaladieDecorator {
	
	private Maladie maladie;
	
	/**
	 * Constructeur.
	 * @param pat
	 */
	public MaladieDecorator(Maladie mal) {
		this.maladie = mal;
	}
	
	public Maladie getMaladie() {
		return this.maladie;
	}
	
	public String getLibelle() {
		if (maladie != null) {
			return this.maladie.getLibelle();
		} else {
			return null;
		}
	}
	
	public String getCode() {
		if (maladie != null) {
			return this.maladie.getCode();
		} else {
			return null;
		}
	}
	
	/**
	 * Utilise le format de date AbstractFicheController.
	 * @return date diagnostic formattée pour affichage zk
	 */
	public String getFormattedDateDiag() {
		if (maladie != null) {
			return ObjectTypesFormatters
						.dateRenderer2(this.maladie.getDateDiagnostic());
		} else {
			return null;
		}
	}
	
	/**
	 * Utilise le format de date AbstractFicheController.
	 * @return date début formattée pour affichage zk
	 */
	public String getFormattedDateDebut() {
		if (maladie != null) {
			return ObjectTypesFormatters
							.dateRenderer2(this.maladie.getDateDebut());
		} else {
			return null;
		}
	}
	
	/**
	 * Récupère le nombres de prélèvements.
	 * @return nb
	 */
	public String getNbPrelevements() {
		if (maladie != null) {
			String totalNb = String.valueOf(ManagerLocator.getMaladieManager()
					.getPrelevementsManager(this.maladie).size());
			return totalNb;
		} else {
			return null;
		}
	}

	public String getPatientNomAndPrenom() {
		if (this.maladie != null && this.maladie.getPatient() != null) {
			StringBuffer sb = new StringBuffer();
			Patient pat = this.maladie.getPatient();

			if (pat.getNom() != null && pat.getPrenom() != null) {
				sb.append(pat.getNom());
				sb.append(" ");
				sb.append(pat.getPrenom());
			} else if (pat.getNom() != null) {
				sb.append(pat.getNom());
			} else if (pat.getPrenom() != null) {
				sb.append(pat.getPrenom());
			}
			return sb.toString();
		} else {
			return null;
		}
		
	}
	
	/**
	 * Décore une liste de maladies.
	 * @param maladies
	 * @return maladies décorés.
	 */
	public static List<MaladieDecorator> 
								decorateListe(List<Maladie> mals) {
		List<MaladieDecorator> liste = 
									new ArrayList<MaladieDecorator>();
		Iterator<Maladie> it = mals.iterator();
		while (it.hasNext()) {
			liste.add(new MaladieDecorator(it.next()));
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
		
		MaladieDecorator deco = (MaladieDecorator) obj;
		return this.getMaladie().equals(deco.getMaladie());		
	}
	
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashMaladie = 0;
		
		if (this.maladie != null) {
			hashMaladie = this.maladie.hashCode();
		}
		
		hash = 7 * hash + hashMaladie;

		return hash;
	}
}
