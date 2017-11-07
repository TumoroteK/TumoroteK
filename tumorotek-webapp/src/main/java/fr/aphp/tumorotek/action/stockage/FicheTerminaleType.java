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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.stockage.TerminaleType;

public class FicheTerminaleType extends AbstractFicheCombineController {
	
	// private Log log = LogFactory.getLog(FicheTerminaleType.class);

	private static final long serialVersionUID = 437801609974005595L;
	
	// static components
	private Label nomLabel;
	private Label hauteurLabel;
	private Label longeurLabel;
	private Label nbPlacesLabel;
	private Div modeleBoite;
	private Row rowStaticTaille;
	private Row rowStaticNbPlaces;
	private Label numerotationLabelStatic;
	private Component[] objStaticComponents;
	
	// edit components
	private Textbox nomBox;
	private Label nomRequired;
	private Label numerotationLabelEdit;
	private Checkbox numerotationBox;
	private Row warningPlateformes;
	
	// new components
	private Row rowStyleBoite;
	private Radio radioBoiteRectangulaire;
	private Radio radioBoiteComplexe;
	private Row rowFormatTitle;
	private Row rowEditTaille;
	private Row rowEditNbLignes;
	private Row rowDefinitionLignes;
	private Row rowPrevisualiser;
	private Intbox hauteurBox;
	private Intbox longueurBox;
	private Intbox nbLignesBox;
	private Grid lignesGrid;
	private Component[] objCreateComponents;
	
	// Objets Principaux.
	private TerminaleType terminaleType;
	private Integer hauteurValue;
	private Integer longueurValue;
	private Integer nbLignesValue = 1;
	private List<LigneBoite> lignesBoite = new ArrayList<LigneBoite>();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
				this.nomLabel,
				this.hauteurLabel,
				this.longeurLabel,
				this.nbPlacesLabel,
				this.numerotationLabelStatic
		});
		
		setObjBoxsComponents(new Component[]{
				this.nomBox,
				this.numerotationLabelEdit,
				this.numerotationBox,
				this.warningPlateformes
		});
		
		setRequiredMarks(new Component[]{
				this.nomRequired
		});	
		
		objStaticComponents = new Component[]{
				this.rowStaticTaille,
				this.rowStaticNbPlaces
		};
		
		objCreateComponents = new Component[]{
				this.rowStyleBoite,
				this.rowFormatTitle,
				this.rowEditTaille,
				this.rowEditNbLignes,
				this.rowDefinitionLignes,
				this.rowPrevisualiser,
				this.hauteurBox,
				this.longueurBox
		};
				
		drawActionsForFormat();
		
		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
		}
		
		getBinder().loadAll();
		
	}

	@Override
	public void cloneObject() {
		setClone(this.terminaleType.clone());
	}

	@Override
	public void createNewObject() {
		setEmptyToNulls();
		setFieldsToUpperCase();
		
		this.terminaleType.setDepartNumHaut(numerotationBox.isChecked());
		
		// on remplit l'objet terminaleType
		defineTerminaleType();
			
		// update de l'objet
		ManagerLocator.getTerminaleTypeManager()
			.createObjectManager(terminaleType);
		
		if (getListeTerminaleType() != null) {
			// ajout du format à la liste
			getListeTerminaleType().addToObjectList(
					this.terminaleType);
		}
	}

	@Override
	public void onClick$addNewC() {
		switchToCreateMode();
	}
	
	@Override
	public void onClick$cancelC() {
		clearData();
	}

	@Override
	public void onClick$deleteC() {
		if (this.terminaleType != null) {
			
			if (Messagebox.show(ObjectTypesFormatters.getLabel(
					"message.deletion.message", 
					new String[]{Labels
						.getLabel("message.deletion.terminaleType")}),
					Labels.getLabel("message.deletion.title"), 
					Messagebox.YES | Messagebox.NO, 
					Messagebox.QUESTION) == Messagebox.YES) {
			
				try {
					// suppression du profil
					ManagerLocator.getTerminaleTypeManager()
						.removeObjectManager(terminaleType);
					
					// on vérifie que l'on retrouve bien la page 
					// contenant la liste des formats
					if (getListeTerminaleType() != null) {
						
						// on enlève le format de la liste
						getListeTerminaleType().
						removeObjectAndUpdateList(this.terminaleType);
					}
					// clear form
					clearData();
				
				} catch (RuntimeException re) {
					Messagebox.show(handleExceptionMessage(re), 
							"Error", Messagebox.OK, Messagebox.ERROR);
				}
			}
		
		}
	}

	@Override
	public void onClick$editC() {
		if (this.terminaleType != null) {
			switchToEditMode();
		}
	}
	
	@Override
	public void onClick$revertC() {
		clearConstraints();
		super.onClick$revertC();
	}
	
	@Override
	public void onClick$validateC() {
		Clients.showBusy(Labels.getLabel(
				"terminaleType.creation.encours"));
		Events.echoEvent("onLaterUpdate", self, null);
	}
	
	public void onLaterUpdate() {
		try {
			updateObject();
			cloneObject();
			switchToStaticMode();

			// ferme wait message
			Clients.clearBusy();
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		} 
	}
	
	@Override
	public void onClick$createC() {
		Clients.showBusy(Labels.getLabel(
				"terminaleType.creation.encours"));
		Events.echoEvent("onLaterCreate", self, null);
	}
	
	public void onLaterCreate() {
		try {
			createNewObject();
			cloneObject();
			switchToStaticMode();
			disableToolBar(false);
		
			// ferme wait message
			Clients.clearBusy();
		} catch (RuntimeException re) {
			// ferme wait message
			Clients.clearBusy();
			Messagebox.show(handleExceptionMessage(re), 
					"Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Méthode appelée après la saisie d'une valeur dans le champ
	 * nomBox. Cette valeur sera mise en majuscules.
	 */
	public void onBlur$nomBox() {
		nomBox.setValue(nomBox.getValue().toUpperCase().trim());
	}
	
	@Override
	public void setFieldsToUpperCase() {
		if (this.terminaleType.getType() != null) {
			this.terminaleType.setType(
				this.terminaleType.getType().toUpperCase().trim());
		}
	}

	@Override
	public void setEmptyToNulls() {		
	}
	
	@Override
	public void clearData() {
		clearConstraints();
		super.clearData();
	}

	@Override
	public void switchToStaticMode() {
		super.switchToStaticMode(this.terminaleType.equals(
				new TerminaleType()));
		
		for (int i = 0; i < objStaticComponents.length; i++) {
			objStaticComponents[i].setVisible(true);
		}
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		// on vide la modélisation de la boite
		modeleBoite.getChildren().clear();
		// on modélise la boite
		modelisationFormat();
		
		getBinder().loadComponent(self);
	}
	
	@Override
	public void switchToEditMode() {
		
		if (this.terminaleType.getDepartNumHaut() != null) {
			numerotationBox.setChecked(this.terminaleType.getDepartNumHaut());
		} else {
			numerotationBox.setChecked(true);
		}
		
		super.switchToEditMode();
		
		for (int i = 0; i < objStaticComponents.length; i++) {
			objStaticComponents[i].setVisible(true);
		}
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(false);
		}
		
		hauteurLabel.setVisible(true);
		longeurLabel.setVisible(true);
		nbPlacesLabel.setVisible(true);
		
		getBinder().loadComponent(self);
		
	}
	
	@Override
	public void switchToCreateMode() {
		
		numerotationBox.setChecked(true);
		
		super.switchToCreateMode();
		
		// on affiche les bons composants
		for (int i = 0; i < objStaticComponents.length; i++) {
			objStaticComponents[i].setVisible(false);
		}
		for (int i = 0; i < objCreateComponents.length; i++) {
			objCreateComponents[i].setVisible(true);
		}
		
		// la création d'un format rectangulaire est sélectionné 
		// par défaut
		radioBoiteRectangulaire.setChecked(true);
		rowEditNbLignes.setVisible(false);
		rowDefinitionLignes.setVisible(false);
		nbLignesBox.setValue(1);
		//hauteurBox.setValue(null);
		//longueurBox.setValue(null);
		hauteurValue = null;
		longueurValue = null;
		lignesBoite = new ArrayList<LigneBoite>();
		SimpleListModel<LigneBoite> list = new SimpleListModel<LigneBoite>(lignesBoite);
		lignesGrid.setModel(list);
		
		modeleBoite.getChildren().clear();
		
		getBinder().loadComponent(self);
		
	}
	
	@Override
	public void setFocusOnElement() {
		nomBox.setFocus(true);
	}
	
	/**
	 * Check du format rectangulaire.
	 */
	public void onCheck$radioBoiteRectangulaire() {
		// on affiche les bons composants
		rowEditTaille.setVisible(true);
		rowEditNbLignes.setVisible(false);
		rowDefinitionLignes.setVisible(false);
		hauteurBox.setVisible(true);
		longueurBox.setVisible(true);
		
		// on ré-initialise ceux qui sont cachés
		nbLignesBox.setValue(1);
		lignesBoite = new ArrayList<LigneBoite>();
		SimpleListModel<LigneBoite> list = new SimpleListModel<LigneBoite>(lignesBoite);
		lignesGrid.setModel(list);
		getBinder().loadComponent(lignesGrid);
	}
	
	/**
	 * Check du format complexe.
	 */
	public void onCheck$radioBoiteComplexe() {
		// on affiche les bons composants
		rowEditTaille.setVisible(false);
		rowEditNbLignes.setVisible(true);
		rowDefinitionLignes.setVisible(false);
		
		// on ré-initialise ceux qui sont cachés
		hauteurBox.setVisible(false);
		hauteurBox.setValue(1);
		longueurBox.setValue(1);
		longueurBox.setVisible(false);
	}
	
	/**
	 * Clic sur le bouton pour définir chaque ligne.
	 */
	public void onClick$definirNbLignes() {
		rowDefinitionLignes.setVisible(true);
		
		// en fonction du nb de lignes, on initialise la grille
		lignesBoite = new ArrayList<LigneBoite>();
		for (int i = 0; i < nbLignesValue; i++) {
			LigneBoite li = new LigneBoite();
			li.setNom(ObjectTypesFormatters
				.getLabel("terminaleType.creation.nb.emplacement.ligne", 
				new String[]{String.valueOf(i + 1)}));
			lignesBoite.add(li);
		}
		
		getBinder().loadAttribute(lignesGrid, "model");
		getBinder().loadComponent(lignesGrid);
	}
	
	/**
	 * Visualisation de la boîte.
	 */
	public void onClick$previsualiser() {
		defineTerminaleType();
		
		// on vide la modélisation de la boite
		modeleBoite.getChildren().clear();
		
		this.terminaleType.setDepartNumHaut(numerotationBox.isChecked());
		
		// modélisation de la boîte
		modelisationFormat();
		
		getBinder().loadComponent(modeleBoite);
	}
	
	/**
	 * En fonction des valeurs saisies dans le formulaire, on
	 * remplit l'objet terminaleType.
	 */
	public void defineTerminaleType() {
		// si c'est une boite rectangulaire
		if (radioBoiteRectangulaire.isChecked()) {
			this.terminaleType.setHauteur(hauteurValue);
			this.terminaleType.setLongueur(longueurValue);
			Integer places = hauteurValue * longueurValue;
			this.terminaleType.setNbPlaces(places);
			this.terminaleType.setScheme(null);
		} else if (radioBoiteComplexe.isChecked()) {
			// sinon, boite complexe
			this.terminaleType.setHauteur(0);
			this.terminaleType.setLongueur(0);
			StringBuffer sb = new StringBuffer();
			int cpt = 0;
			// création du champ scheme
			for (int i = 0; i < lignesBoite.size(); i++) {
				sb.append(lignesBoite.get(i).getNbEmplacements());
				cpt += lignesBoite.get(i).getNbEmplacements();
				
				if (i < lignesBoite.size() - 1) {
					sb.append(";");
				}
			}
			this.terminaleType.setNbPlaces(cpt);
			if (!sb.toString().equals("")) {
				this.terminaleType.setScheme(sb.toString());
			} else {
				this.terminaleType.setScheme(null);
			}
		}
	}

	@Override
	public void updateObject() {
		// on remplit l'utilisateur en fonction des champs nulls
		setEmptyToNulls();
		setFieldsToUpperCase();
		
		this.terminaleType.setDepartNumHaut(numerotationBox.isChecked());
			
		// update de l'objet
		ManagerLocator.getTerminaleTypeManager()
			.updateObjectManager(terminaleType);
		
		if (getListeTerminaleType() != null) {
			// ajout de l'utilisateur à la liste
			getListeTerminaleType().updateObjectGridList(
					this.terminaleType);
		}
	}

	@Override
	public TerminaleType getObject() {
		return this.terminaleType;
	}
	
	@Override
	public void setObject(TKdataObject obj) {
		this.terminaleType = (TerminaleType) obj;
		
		super.setObject(terminaleType);		
	}
	
	@Override
	public void setNewObject() {
		setObject(new TerminaleType());
		super.setNewObject();
	}
	
	/**
	 * Rend les boutons d'actions cliquables ou non.
	 */
	public void drawActionsForFormat() {
		boolean admin = false;
		if (sessionScope.containsKey("AdminPF")) {
			admin = (Boolean) sessionScope.get("AdminPF");
		}
		
		// si l'utilisateur est admin PF => boutons cliquables
		if (admin) {
			setCanNew(true);
			setCanEdit(true);
			setCanDelete(true);
		} else {
			setCanNew(false);
			setCanEdit(false);
			setCanDelete(false);
		}
	}
	
	@Override
	public void disableToolBar(boolean b) {
		if (sessionScope.containsKey("Admin")
				&& (Boolean) sessionScope.get("Admin")) {
			super.disableToolBar(false);
		} else {
			super.disableToolBar(true);
		}
	}
	
	/**
	 * Méthode modélisant la structure d'une boite.
	 */
	public void modelisationFormat() {
		
		if (terminaleType.getScheme() == null) {
			
			if (terminaleType.getHauteur() != null
					&& terminaleType.getLongueur() != null) {
				Vbox mainVbox = new Vbox();
				mainVbox.setSpacing("0");
				
				int width = (28 * terminaleType.getLongueur());
				mainVbox.setWidth(width + "px");
				
				// int cpt = 0;
				for (int i = 0; i < terminaleType.getHauteur(); i++) {
					Hbox hBox = new Hbox();
					hBox.setSpacing("0");
					
					for (int j = 0; j < terminaleType.getLongueur(); j++) {
						
						Image img = new Image();
						img.setSrc(
						"/images/icones/emplacements/emplacementVIDE.png");
						img.setSclass("imageEmplacement");
						if (i == 0 && j == 0) {
							if (this.terminaleType != null 
									&& this.terminaleType.getDepartNumHaut() 
									!= null) {
								if (this.terminaleType.getDepartNumHaut()) {
									img.setSclass("imageDestockEmplacement");
								}
							}
						} else if (i == terminaleType.getHauteur() - 1 
								&& j == 0) {
							if (this.terminaleType != null 
									&& this.terminaleType.getDepartNumHaut() 
									!= null) {
								if (!this.terminaleType.getDepartNumHaut()) {
									img.setSclass("imageDestockEmplacement");
								}
							}
						}
						img.setParent(hBox);						
						// ++cpt;
					}
					hBox.setParent(mainVbox);
				}
				mainVbox.setParent(modeleBoite);
			}
			
		} else {
			
			String[] values = terminaleType.getScheme()
				.split(";");
			Vbox mainVbox = new Vbox();
			mainVbox.setSpacing("0");
			mainVbox.setWidth("100%");
			mainVbox.setAlign("center");
			// int cpt = 0;
			for (int i = 0; i < values.length; i++) {
				int nbPlaces = Integer.parseInt(values[i]);
				Hbox hBox = new Hbox();
				hBox.setSpacing("0");
				hBox.setAlign("center");
				for (int j = 0; j < nbPlaces; j++) {
					Image img = new Image();
					img.setSrc(
					"/images/icones/emplacements/emplacementVIDE.png");
					img.setSclass("imageEmplacement");
					
					if (i == 0 && j == 0) {
						if (this.terminaleType != null 
								&& this.terminaleType.getDepartNumHaut() 
								!= null) {
							if (this.terminaleType.getDepartNumHaut()) {
								img.setSclass("imageDestockEmplacement");
							}
						}
					} else if (i == values.length - 1 
							&& j == 0) {
						if (this.terminaleType != null 
								&& this.terminaleType.getDepartNumHaut() 
								!= null) {
							if (!this.terminaleType.getDepartNumHaut()) {
								img.setSclass("imageDestockEmplacement");
							}
						}
					}
					
					img.setParent(hBox);
					// ++cpt;
				}
				hBox.setParent(mainVbox);
			}
			mainVbox.setParent(modeleBoite);
		}
		
	}
	
	/**
	 * Méthode vidant tous les messages d'erreurs apparaissant dans
	 * les contraintes de la fiche.
	 */
	public void clearConstraints() {
		Clients.clearWrongValue(nomBox);
	}
	
	public String getDebutNumerotationFormated() {
		if (this.terminaleType != null
				&& this.terminaleType.getDepartNumHaut() != null) {
			if (this.terminaleType.getDepartNumHaut()) {
				return Labels.getLabel("terminaleType.numerotation.haut");
			} else {
				return Labels.getLabel("terminaleType.numerotation.bas");
			}
		} else {
			return Labels.getLabel("terminaleType.numerotation.haut");
		}
	}

	/*********************************************************/
	/********************** ACCESSEURS. **********************/
	/*********************************************************/

	public TerminaleType getTerminaleType() {
		return terminaleType;
	}

	public void setTerminaleType(TerminaleType tType) {
		this.terminaleType = tType;
	}
	
	public ConstWord getNomConstraint() {
		return TerminaleTypeConstraints.getNomConstraint();
	}
	
	public String getHauteurFormated() {
		String value = "-";
		
		if (this.terminaleType != null 
				&& this.terminaleType.getHauteur() != null
				&& this.terminaleType.getHauteur() > 0) {
			value = String.valueOf(this.terminaleType.getHauteur());
		}
		
		return value;
	}
	
	public String getLongueurFormated() {
		String value = "-";
		
		if (this.terminaleType != null 
				&& this.terminaleType.getLongueur() != null
				&& this.terminaleType.getLongueur() > 0) {
			value = String.valueOf(this.terminaleType.getLongueur());
		}
		
		return value;
	}
	
	/**
	 * Recupere le controller du composant representant la liste associee
	 * a l'entite de domaine a partir de l'evenement.
	 * @param event Event
	 * @return fiche ListeProfil
	 */
	private ListeTerminaleType getListeTerminaleType() {
		return (ListeTerminaleType) getObjectTabController().getListe();
	}

	public Integer getHauteurValue() {
		return hauteurValue;
	}

	public void setHauteurValue(Integer hValue) {
		this.hauteurValue = hValue;
	}

	public Integer getLongueurValue() {
		return longueurValue;
	}

	public void setLongueurValue(Integer lValue) {
		this.longueurValue = lValue;
	}

	public Integer getNbLignesValue() {
		return nbLignesValue;
	}

	public void setNbLignesValue(Integer nbValue) {
		this.nbLignesValue = nbValue;
	}

	public List<LigneBoite> getLignesBoite() {
		return lignesBoite;
	}

	public void setLignesBoite(List<LigneBoite> lsBoite) {
		this.lignesBoite = lsBoite;
	}

	@Override
	public String getDeleteWaitLabel() {
		return Labels.getLabel("deletion.general.wait");
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}
	@Override
	public boolean prepareDeleteObject() {
		return false;
	}

	@Override
	public void removeObject(String comments) {		
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}

}
