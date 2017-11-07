package fr.aphp.tumorotek.action.stockage;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.stockage.Enceinte;

public class EnceinteRowRenderer implements RowRenderer<Enceinte> {
	
	private Long nbLibresTotaux = (long) 0;
	private Long nbOccupesTotaux = (long) 0;
	
	public EnceinteRowRenderer() {
		nbLibresTotaux = (long) 0;
		nbOccupesTotaux = (long) 0;
	}

	@Override
	public void render(Row row, Enceinte data, int index) throws Exception {
		// si l'élément n'est pas null
		if (data != null) {
			Enceinte enc = data;
			
			// code
			Label nomLabel = new Label(enc.getNom());
			nomLabel.setClass("formValue");
			nomLabel.setStyle("font-weight : bold;");
			nomLabel.setParent(row);
			
			// emplacements libres
			Long nbEmplacementsLibres = ManagerLocator.getEnceinteManager()
				.getNbEmplacementsLibresByPS(enc);
			nbLibresTotaux = nbLibresTotaux + nbEmplacementsLibres;
			Label libreLabel = new Label(String.valueOf(nbEmplacementsLibres));
			libreLabel.setParent(row);
			
			// emplacements occupes
			Long nbEmplacementsOccupes = ManagerLocator.getEnceinteManager()
				.getNbEmplacementsOccupesByPS(enc);
			nbOccupesTotaux = nbOccupesTotaux + nbEmplacementsOccupes;
			Label occupeLabel = new Label(
					String.valueOf(nbEmplacementsOccupes));
			occupeLabel.setParent(row);
			
			// total
			Long total = nbEmplacementsLibres + nbEmplacementsOccupes;
			Label totalLabel = new Label(
					String.valueOf(total));
			totalLabel.setParent(row);
			
			// pourcentage
			Label pourcLabel = null;
			if (total != 0) {
				Float puiss = nbEmplacementsOccupes.floatValue() * 100;
				Float pourc = puiss / total.floatValue();
				pourcLabel = new Label(String.valueOf(
						ObjectTypesFormatters.floor(pourc, 2)) + "%");
			} else {
				pourcLabel = new Label("-");
			}
			pourcLabel.setParent(row);
		} else {
			// sinon on va afficher les données complètes du cont.
			// code
			Label nomLabel = new Label(Labels.getLabel("conteneur.total"));
			nomLabel.setClass("formValue");
			nomLabel.setStyle("font-weight : bold;");
			nomLabel.setParent(row);
			
			// emplacements libres
			Label libreLabel = new Label(String.valueOf(nbLibresTotaux));
			libreLabel.setParent(row);
			
			// emplacements occupes
			Label occupeLabel = new Label(String.valueOf(nbOccupesTotaux));
			occupeLabel.setParent(row);
			
			// total
			Long total = nbLibresTotaux + nbOccupesTotaux;
			Label totalLabel = new Label(String.valueOf(total));
			totalLabel.setParent(row);
			
			// pourcentage
			Label pourcLabel = null;
			if (total != 0) {
				Float puiss = nbOccupesTotaux.floatValue() * 100;
				Float pourc = puiss / total.floatValue();
				pourcLabel = new Label(String.valueOf(
						ObjectTypesFormatters.floor(pourc, 2)) + "%");
			} else {
				pourcLabel = new Label("-");
			}
			pourcLabel.setParent(row);
		}
	}

}
