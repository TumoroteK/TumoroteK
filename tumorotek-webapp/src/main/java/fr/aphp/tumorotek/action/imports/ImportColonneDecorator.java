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
package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Decorateur ImportColonne pour affichage ImportTemplate.
 *
 * @version 2.0.12
 * @author Mathieu BARTHELEMY
 *
 */
public class ImportColonneDecorator {

	private ImportColonne colonne;
	private Boolean canDelete = null;
	private Boolean canMove = true;
	private Boolean disableEditLabel = false;

	public ImportColonneDecorator(final ImportColonne ic) {
		colonne = ic;
	}

	public ImportColonne getColonne() {
		return colonne;
	}

	public void setColonne(final ImportColonne c) {
		this.colonne = c;
	}

	public String getChamp() {
		String champ = "";
		if (colonne.getChamp() != null) {
			if (colonne.getChamp().getChampEntite() != null) {
				champ = getLabelForChampEntite(colonne.getChamp().getChampEntite());
			} else if (colonne.getChamp().getChampDelegue() != null) {
				champ = Labels.getLabel(colonne.getChamp().getChampDelegue()
						.getILNLabelForChampDelegue(SessionUtils.getCurrentContexte()));
			} else {
				champ = colonne.getChamp().getChampAnnotation().getNom();
			}
		} else { // subderive header
			if (colonne.getNom().equals("code.parent")) {
				champ = Labels.getLabel("import.colonne.subderive.parent");
			} else if (colonne.getNom().equals("qte.transf")) {
				champ = Labels.getLabel("import.colonne.subderive.qte.transf");
			} else if (colonne.getNom().equals("evt.date")) {
				champ = Labels.getLabel("import.colonne.subderive.evt.date");
			}
		}
		return champ;
	}

	public String getFormat() {
		String format = "";
		if (colonne.getChamp() != null) {
			if (colonne.getChamp().getChampEntite() != null) {
				if (colonne.getChamp().getChampEntite().getNom().equals("EmplacementId")) {
					format = Labels.getLabel("importColonne.Type.Emplacement");
				} else if (colonne.getChamp().getChampEntite().getQueryChamp() != null) {
					format = "thesaurus";
				} else {
					format = colonne.getChamp().getChampEntite().getDataType().getType();
				}
			} else if (colonne.getChamp().getChampDelegue() != null) {
				format = colonne.getChamp().getChampDelegue().getDataType().getType();
			} else {
				format = colonne.getChamp().getChampAnnotation().getDataType().getType();
			}
		} else { // subderive header
			if (colonne.getNom().equals("code.parent")) {
				format = "alphanum";
			} else if (colonne.getNom().equals("qte.transf")) {
				format = "num";
			} else if (colonne.getNom().equals("evt.date")) {
				format = "datetime";
			}
		}
		return format;
	}

	public String getEntite() {
		String entite = "";
		if (colonne.getChamp() != null) {
			if (colonne.getChamp().getChampEntite() != null) {
				entite = Labels.getLabel("Entite." + colonne.getChamp().getChampEntite().getEntite().getNom());
			} else if (colonne.getChamp().getChampDelegue() != null) {
				entite = Labels.getLabel("Entite." + colonne.getChamp().getChampDelegue().getEntite().getNom());
			} else {
				entite = Labels.getLabel(
						"Entite." + colonne.getChamp().getChampAnnotation().getTableAnnotation().getEntite().getNom());
			}
		} else {
			entite = Labels.getLabel("import.colonne.subderive.header");
		}
		return entite;
	}

	public boolean getCanDelete() {
		if (canDelete != null) {
			return canDelete;
		} else if (colonne.getChamp() != null && colonne.getChamp().getChampEntite() != null) {
			return colonne.getChamp().getChampEntite().isNullable();
		}
		return true;
	}

	public boolean getCanMove() {
		return canMove;
	}

	public void setCanMove(final Boolean b) {
		canMove = b;
	}

	public Boolean getDisableEditLabel() {
		return disableEditLabel;
	}

	public void setDisableEditLabel(final Boolean d) {
		this.disableEditLabel = d;
	}

	public String getLabelForChampEntite(final ChampEntite champ) {
		final StringBuffer iProperty = new StringBuffer();
		iProperty.append("Champ.");
		iProperty.append(champ.getEntite().getNom());
		iProperty.append(".");

		String champOk = "";
		// si le nom du champ finit par "Id", on le retire
		if (champ.getNom().endsWith("Id")) {
			champOk = champ.getNom().substring(0, champ.getNom().length() - 2);
		} else {
			champOk = champ.getNom();
		}
		iProperty.append(champOk);

		// on ajoute la valeur du champ
		return Labels.getLabel(iProperty.toString());
	}

	/**
	 * Decore une liste de ImportColonne.
	 * 
	 * @param import colonnes
	 * @param isSubderive
	 *            rend artificiellement les 3 colonnes de l'entete non supprimables
	 * @return ImportColonnes décorées.
	 */
	public static List<ImportColonneDecorator> decorateListe(final List<ImportColonne> cols,
			final boolean isSubderive) {
		final List<ImportColonneDecorator> liste = new ArrayList<>();
		final Iterator<ImportColonne> it = cols.iterator();
		int i = 0;
		while (it.hasNext()) {
			liste.add(new ImportColonneDecorator(it.next()));
			// rend les 3 premiers decorateurs non supprimables
			if (i < 3 && isSubderive) {
				liste.get(i).setCanDelete(false);
				liste.get(i).setCanMove(false);
				liste.get(i).setDisableEditLabel(true);
			}
			i++;
		}
		return liste;
	}

	/**
	 * Extrait les CederObjets d'une liste de Decorator.
	 * 
	 * @param CederObjets
	 * @return CederObjets décorés.
	 */
	public static List<ImportColonne> extractListe(final List<ImportColonneDecorator> cols) {
		final List<ImportColonne> liste = new ArrayList<>();
		final Iterator<ImportColonneDecorator> it = cols.iterator();

		while (it.hasNext()) {
			liste.add(it.next().getColonne());
		}
		return liste;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}

		final ImportColonneDecorator deco = (ImportColonneDecorator) obj;
		return this.getColonne().equals(deco.getColonne());

	}

	@Override
	public int hashCode() {

		int hash = 7;
		int hashColonne = 0;

		if (this.getColonne() != null) {
			hashColonne = this.getColonne().hashCode();
		}

		hash = 7 * hash + hashColonne;

		return hash;
	}

	public void setCanDelete(final Boolean canDelete) {
		this.canDelete = canDelete;
	}

}
