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
package fr.aphp.tumorotek.action.imprimante;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.action.imports.ImportChampDecorator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Classe gérant la création et l'édition des lignes d'une étiquette.
 * Créée le 15/06/2011.
 * @author Pierre VENTADOUR.
 *
 */
public class FicheLigneEtiquetteModale extends AbstractFicheCombineController {
	
	// private Log log = LogFactory.getLog(FicheLigneEtiquetteModale.class);

	private static final long serialVersionUID = 6047056651132329158L;
	
	// Components
	private Radio texteRadio;
	private Radio codeRadio;
	private Grid champsGrid;
	private Listbox entitesBox;
	private Listbox champsBox;
	private Listbox entitesToPrintBox;
	private Listbox formatagesBox;
	private Listbox fontsBox;
	private Listbox stylesBox;
	private Listbox sizesBox;
	private Textbox caractereBox;
	private Hbox positionDiv;
	private Intbox premierBox;
	private Intbox dernierBox;
	private Row fontRow;
	private Row styleRow;
	private Row sizeRow;
	private Row contenuTitleRow;
	private Row enteteRow;
	private Row contenuRow;
	
	// Objets principaux
	private LigneEtiquette ligneEtiquette;
	private ListModelList<ChampLigneEtiquette> champs = 
		new ListModelList<ChampLigneEtiquette>();
	private List<ChampLigneEtiquette> champsToRemove = 
		new ArrayList<ChampLigneEtiquette>();
	private Component parent;
	private ChampLigneEtiquetteRowRenderer champsRowRenderer = 
		new ChampLigneEtiquetteRowRenderer();
	private ListModelList<Entite> entites = new ListModelList<Entite>();
	private Entite selectedEntite;
	private ListModelList<Entite> entitesToPrint = new ListModelList<Entite>();
	private Entite selectedEntiteToPrint;
	private ListModelList<ImportChampDecorator> champsDecorator = 
		new ListModelList<ImportChampDecorator>();
	private ImportChampDecorator selectedChamp;
	private ListModelList<String> formatages = new ListModelList<String>();
	private String selectedFormatage = "";
	private String caractere = "";
	private Integer premier;
	private Integer dernier;
	private List<String> fonts = new ArrayList<String>();
	private String selectedFont = "";
	private List<String> styles = new ArrayList<String>();
	private String selectedStyle = "";
	private List<Integer> sizes = new ArrayList<Integer>();
	private Integer selectedSize;
	
	//qrcode
	private Boolean isQRCode = false;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {		
		super.doAfterCompose(comp);
		
		// if (winPanel != null) {
		//	winPanel.setHeight("80%");
		//}
		
		// Initialisation des listes de composants
		setObjLabelsComponents(new Component[]{
		});
		
		setObjBoxsComponents(new Component[]{
		});
		
		setRequiredMarks(new Component[]{
		});	
		
		getBinder().loadAll();
	}
	
	/**
	 * Initialise la fenêtre : le parent, l'imprimante et passe en mode
	 * create ou edit.
	 * @param imp
	 * @param comp
	 * @param creationMode True si la ligne est nouvelle.
	 */
	public void initModale(LigneEtiquetteDecorator deco, 
			Component comp, boolean creationMode, boolean isQRCode) {
		this.isQRCode = isQRCode;
		setParent(comp);
		setObject(deco.getLigneEtiquette());
		champs.addAll(deco.getChamps());
		if (!creationMode) {
			switchToEditMode();
		} else {
			switchToCreateMode();
		}
		
	}

	@Override
	public void cloneObject() {
		setClone(this.ligneEtiquette.clone());
	}
	
	@Override
	public void onClick$cancelC() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	@Override
	public void onClick$revertC() {
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}
	
	public void onClick$validateC() {
		Clients.showBusy(Labels
				.getLabel("general.display.wait"));
		Events.echoEvent("onLaterUpdateLigneEtiquette", self, null);
	}
	
	public void onClick$createC() {
		Clients.showBusy(Labels
				.getLabel("general.display.wait"));
		Events.echoEvent("onLaterUpdateLigneEtiquette", self, null);
	}
	
	/**
	 * Mets à jour toutes les infos de la ligne et renvoie à la fiche
	 * du modèle une LigneEtiquetteDecorator.
	 */
	public void onLaterUpdateLigneEtiquette() {
		ligneEtiquette.setIsBarcode(codeRadio.isChecked());
		if (!ligneEtiquette.getIsBarcode()) {
			ligneEtiquette.setFont(selectedFont);
			ligneEtiquette.setStyle(selectedStyle);
			ligneEtiquette.setSize(selectedSize);
		} else {
			ligneEtiquette.setFont(null);
			ligneEtiquette.setStyle(null);
			ligneEtiquette.setSize(null);
			ligneEtiquette.setEntete(null);
			ligneEtiquette.setContenu(null);
		}
		
		setEmptyToNulls();
		
		// définition du style
		if (ligneEtiquette.getStyle() == null) {
			ligneEtiquette.setStyle("PLAIN");
		} else {
			if (ligneEtiquette.getStyle().equals(
					Labels.getLabel("Champ.Modele.Style.Plain"))) {
				ligneEtiquette.setStyle("PLAIN");
			} else if (ligneEtiquette.getStyle().equals(
					Labels.getLabel("Champ.Modele.Style.Bold"))) {
				ligneEtiquette.setStyle("BOLD");
			} else if (ligneEtiquette.getStyle().equals(
					Labels.getLabel("Champ.Modele.Style.Italic"))) {
				ligneEtiquette.setStyle("ITALIC");
			}
		}
		
		// gestion de l'ordre des champs
		for (int i = 0; i < champs.size(); i++) {
			champs.get(i).setOrdre(i + 1);
		}
		
		// création de l'objet à renvoyer
		LigneEtiquetteDecorator deco = new LigneEtiquetteDecorator(
				ligneEtiquette,
				champs);
		deco.setChampsToRemove(champsToRemove);
		
		// ferme wait message
		Clients.clearBusy();
		// post le résultat
		Events.postEvent("onGetLigneEtiquette", getParent(), deco);	
		// fermeture de la fenêtre
		Events.postEvent(new Event("onClose", self.getRoot()));
	}

	@Override
	public void createNewObject() {
	}

	@Override
	public void updateObject() {
	}
	
	@Override
	public String getDeleteWaitLabel() {
		return null;
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
	public void setEmptyToNulls() {
		if (this.ligneEtiquette.getEntete() != null
				&& this.ligneEtiquette.getEntete().equals("")) {
			this.ligneEtiquette.setEntete(null);
		}
		if (this.ligneEtiquette.getContenu() != null
				&& this.ligneEtiquette.getContenu().equals("")) {
			this.ligneEtiquette.setContenu(null);
		}
		if (this.ligneEtiquette.getFont() != null
				&& this.ligneEtiquette.getFont().equals("")) {
			this.ligneEtiquette.setFont(null);
		}
		if (this.ligneEtiquette.getStyle() != null
				&& this.ligneEtiquette.getStyle().equals("")) {
			this.ligneEtiquette.setStyle(null);
		}
	}

	@Override
	public void setFieldsToUpperCase() {
	}

	@Override
	public void setFocusOnElement() {
	}

	@Override
	public void setParentObject(TKdataObject obj) {
	}

	@Override
	public TKdataObject getObject() {
		return this.ligneEtiquette;
	}
	
	@Override
	public void setObject(TKdataObject obj) {
		this.ligneEtiquette = (LigneEtiquette) obj;

		super.setObject(obj);
	}
	
	@Override
	public void setNewObject() {
		setObject(new LigneEtiquette());
	}
	
	public void switchToCreateMode() {		
		super.switchToCreateMode();
		
		initEditableMode();
		getBinder().loadComponent(self);
	}
	
	public void switchToStaticMode() {
		super.switchToStaticMode(this.ligneEtiquette.equals(
				new LigneEtiquette()));
	}
	
	
	public void switchToEditMode() {
		super.switchToEditMode();
		
		initEditableMode();
		getBinder().loadComponent(self);
	}
	
	/**
	 * Init des listes.
	 */
	public void initEditableMode() {
		if(isQRCode) {
			codeRadio.setDisabled(true);
		}
		// gestion des radio box pour le code-barres.
		if (this.ligneEtiquette.getIsBarcode() != null
				&& this.ligneEtiquette.getIsBarcode()) {
			codeRadio.setChecked(true);
		} else {
			texteRadio.setChecked(true);
		}
		showFontValues(texteRadio.isChecked());
		
		// init des entités imprimables
		// entitesToPrint = new ArrayList<Entite>();
		entitesToPrint.add(null);
		entitesToPrint.add(ManagerLocator.getEntiteManager()
				.findByNomManager("Echantillon").get(0));
		entitesToPrint.add(ManagerLocator.getEntiteManager()
				.findByNomManager("ProdDerive").get(0));
		selectedEntiteToPrint = null;
		
		// init de la liste des formatages
		// formatages = new ArrayList<String>();
		formatages.add(null);
		formatages.add(Labels.getLabel(
				"fiche.ligne.etiquette.formatage.aucun"));
		formatages.add(Labels.getLabel(
				"fiche.ligne.etiquette.formatage.avant"));
		formatages.add(Labels.getLabel(
				"fiche.ligne.etiquette.formatage.apres"));
		formatages.add(Labels.getLabel(
				"fiche.ligne.etiquette.formatage.entre"));
		selectedFormatage = formatages.get(0);
		
		// init des fonts
		String[] tmp = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		fonts = new ArrayList<String>();
		for (int i = 0; i < tmp.length; i++) {
			fonts.add(tmp[i]);
		}
		selectedFont = ligneEtiquette.getFont();
		if (selectedFont == null) {
			if (fonts.contains("Times New Roman")) { // windows defaut
				selectedFont = "Times New Roman";
			} else { // linux defaut
				selectedFont = ConfigManager.G2D_FONT_FAMILY;
				// quel est la meilleure font family par défaut? La plus répandue
			}
		}
//		if (!fonts.contains(selectedFont)) {
//			fonts.add(selectedFont);
//		}
		ListModelList<String> list = new ListModelList<String>(fonts);
		fontsBox.setModel(list);
		list.addToSelection(selectedFont);
		
		// init des styles
		styles = new ArrayList<String>();
		styles.add(Labels.getLabel("Champ.Modele.Style.Plain"));
		styles.add(Labels.getLabel("Champ.Modele.Style.Bold"));
		styles.add(Labels.getLabel("Champ.Modele.Style.Italic"));
		if (ligneEtiquette.getStyle() == null) {
			selectedStyle = Labels.getLabel("Champ.Modele.Style.Plain");
		} else {
			if (ligneEtiquette.getStyle().equals("PLAIN")) {
				selectedStyle = Labels.getLabel("Champ.Modele.Style.Plain");
			} else if (ligneEtiquette.getStyle().equals("BOLD")) {
				selectedStyle = Labels.getLabel("Champ.Modele.Style.Bold");
			} else if (ligneEtiquette.getStyle().equals("ITALIC")) {
				selectedStyle = Labels.getLabel("Champ.Modele.Style.Italic");
			}
		}
		ListModelList<String> list2 = new ListModelList<String>(styles);
		stylesBox.setModel(list2);
		list2.addToSelection(selectedStyle);
		
		// init des sizes
		sizes = new ArrayList<Integer>();
		sizes.add(2);
		sizes.add(3);
		sizes.add(4);
		sizes.add(5);
		sizes.add(6);
		sizes.add(7);
		sizes.add(8);
		sizes.add(9);
		sizes.add(10);
		sizes.add(11);
		sizes.add(12);
		sizes.add(13);
		sizes.add(14);
		sizes.add(15);
		sizes.add(16);
		sizes.add(17);
		sizes.add(18);
		sizes.add(19);
		sizes.add(20);
		if (ligneEtiquette.getSize() == null) {
			selectedSize = 4;
		} else {
			selectedSize = ligneEtiquette.getSize();
		}
		ListModelList<Integer> list3 = new ListModelList<Integer>(sizes);
		sizesBox.setModel(list3);
		list3.addToSelection(selectedSize);
	}
	
	/**
	 * Si l'utilisateur check le radio texteRadio, on va afficher
	 * le paramétrage du texte : font, style, size....
	 */
	public void onCheck$texteRadio() {
		showFontValues(true);
	}
	
	/**
	 * Si l'utilisateur check le radio codeRadio, on va cacher
	 * le paramétrage du texte : font, style, size....
	 */
	public void onCheck$codeRadio() {
		showFontValues(false);
	}
	
	public void onSelect$entitesToPrintBox() {
		
		clearSelection(false);
		
		if (entitesToPrintBox.getSelectedIndex() > 0) {
			
			selectedEntiteToPrint = 
					entitesToPrint.get(entitesToPrintBox.getSelectedIndex());
			
			// init des entités
			//entites = new ArrayList<Entite>();
			entites.clear();
			entites.add(null);
			entites.add(ManagerLocator.getEntiteManager()
					.findByNomManager("Patient").get(0));
			entites.add(ManagerLocator.getEntiteManager()
					.findByNomManager("Maladie").get(0));
			entites.add(ManagerLocator.getEntiteManager()
					.findByNomManager("Prelevement").get(0));
			entites.add(ManagerLocator.getEntiteManager()
					.findByNomManager("Echantillon").get(0));
			if (selectedEntiteToPrint.getNom().equals("ProdDerive")) {
				entites.add(ManagerLocator.getEntiteManager()
					.findByNomManager("ProdDerive").get(0));
			}
		}
	}
	
	/**
	 * Filtre les champs par entités.
	 * @param event Event : seléction sur la liste entitesBox.
	 * @throws Exception
	 */
	public void onSelect$entitesBox(Event event) throws Exception {
		int ind = entitesBox.getSelectedIndex();
		selectedEntite = entites.get(ind);
		champsDecorator.clear();
		if (selectedEntite != null) {
			List<Champ> tmp = new ArrayList<Champ>();
			List<ChampEntite> ces = ManagerLocator.getChampEntiteManager()
				.findByEntiteAndImportManager(selectedEntite, true);
			
			for (int i = 0; i < ces.size(); i++) {
				tmp.add(new Champ(ces.get(i)));
			}
			champsDecorator.addAll(ImportChampDecorator.decorateListe(tmp));
		} // else {
			// champsDecorator = new ArrayList<ImportChampDecorator>();
		//}
		champsDecorator.add(0, null);
		
		// ListModel list = new ListModelList(champsDecorator);
		// champsBox.setModel(list);
		
		if (!champsDecorator.contains(selectedChamp)) {
			selectedChamp = null;
		}
		champsBox.setSelectedIndex(champsDecorator.indexOf(selectedChamp));
	}
	
	/**
	 * Sélectionne le champsBox.
	 * @param event Event : seléction sur la liste champsBox.
	 * @throws Exception
	 */
	public void onSelect$champsBox(Event event) throws Exception {
		int ind = champsBox.getSelectedIndex();
		selectedChamp = champsDecorator.get(ind);
	}
	
	/**
	 * Sélection d'un formatage.
	 * @param event Event : seléction sur la liste formatagesBox.
	 * @throws Exception
	 */
	public void onSelect$formatagesBox() {
		
		caractereBox.setVisible(false);
		caractere = null;
		positionDiv.setVisible(false);
		premier = null;
		dernier = null;
		
		int ind = formatagesBox.getSelectedIndex();
		
		if (ind > 0) {
			selectedFormatage = formatages.get(ind);
			
			if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.aucun"))) {
				caractereBox.setVisible(false);
				positionDiv.setVisible(false);
			} else if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.avant"))) {
				caractereBox.setVisible(true);
				positionDiv.setVisible(false);
			} else if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.apres"))) {
				caractereBox.setVisible(true);
				positionDiv.setVisible(false);
			} else if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.entre"))) {
				positionDiv.setVisible(true);
				caractereBox.setVisible(false);
			}
		} 
	}
	
	
	/**
	 * Sélection d'une police de caractères.
	 * @param event Event : seléction sur la liste fontsBox.
	 * @throws Exception
	 */
	public void onSelect$fontsBox() {
		int ind = fontsBox.getSelectedIndex();
		selectedFont = fonts.get(ind);
	}
	
	/**
	 * Sélection d'un style de caractères.
	 * @param event Event : seléction sur la liste stylesBox.
	 * @throws Exception
	 */
	public void onSelect$stylesBox() {
		int ind = stylesBox.getSelectedIndex();
		selectedStyle = styles.get(ind);
	}
	
	/**
	 * Sélection d'une taille de caractères.
	 * @param event Event : seléction sur la liste sizesBox.
	 * @throws Exception
	 */
	public void onSelect$sizesBox() {
		int ind = sizesBox.getSelectedIndex();
		selectedSize = sizes.get(ind);
	}
	
	/**
	 * Ajout d'un nouveau champ.
	 */
	public void onClick$addChamp() {
		if (selectedEntiteToPrint == null) {
			throw new WrongValueException(
					entitesToPrintBox, 
					Labels.getLabel("validation.syntax.empty"));
		}
		// creation d'un nouveau ChampLigneEtiquette
		ChampLigneEtiquette cle = new ChampLigneEtiquette();
		cle.setLigneEtiquette(ligneEtiquette);
		cle.setOrdre(1);
		cle.setEntite(selectedEntiteToPrint);
		if (selectedChamp == null) {
			throw new WrongValueException(
					champsBox, 
					Labels.getLabel("validation.syntax.empty"));
		}
		cle.setChamp(selectedChamp.getChamp());
		
		cle.setExpReg(null);
		
		if (selectedFormatage != null) {
			if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.avant"))) {
				if (caractere == null) {
					throw new WrongValueException(
						caractereBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				if (caractere != null && caractere.equals("")) {
					throw new WrongValueException(
						caractereBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				StringBuffer sb = new StringBuffer();
				sb.append("<");
				sb.append(caractere);
				cle.setExpReg(sb.toString());
			} else if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.apres"))) {
				if (caractere == null) {
					throw new WrongValueException(
						caractereBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				if (caractere != null && caractere.equals("")) {
					throw new WrongValueException(
						caractereBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				StringBuffer sb = new StringBuffer();
				sb.append(">");
				sb.append(caractere);
				cle.setExpReg(sb.toString());
			} else if (selectedFormatage.equals(Labels.getLabel(
					"fiche.ligne.etiquette.formatage.entre"))) {
				if (premier == null) {
					throw new WrongValueException(
						premierBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				if (dernier == null) {
					throw new WrongValueException(
						dernierBox, 
						Labels.getLabel("validation.syntax.empty"));
				}
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				sb.append(premier);
				sb.append(",");
				sb.append(dernier);
				sb.append("]");
				cle.setExpReg(sb.toString());
			}
		}
		
		// ajout du nouveau champ à la liste
		// if (!champs.contains(cle)) {
			champs.add(cle);
			// ListModel<ChampDecorator> list = new ListModelList<ChampDecorator>(champs);
			// champsGrid.setModel(list);
			// getBinder().loadComponent(champsGrid);
			// Clients.scrollIntoView(champsGrid);
		//}
		
		clearSelection(true);
	}
	
	private void clearSelection(boolean all) {
		// on réinitialise les listes
		selectedEntite = null;
		entitesBox.clearSelection();
		entites.clear();
		selectedChamp = null;
		champsBox.clearSelection();
		champsDecorator.clear();
		if (all) {
			selectedEntiteToPrint = entitesToPrint.get(0);		
			entitesToPrint.clearSelection();
			selectedFormatage = formatages.get(0);
			formatagesBox.clearSelection();
			onSelect$formatagesBox(); // re-initialise le composant
			// selectedEntiteToPrint = null;
		}
	}

	/**
	 * Clic sur l'image pour supprimer un champ.
	 * @param event
	 */
	public void onClickDeleteLigne(ForwardEvent event) {
		if (event.getData() != null) {
			ChampLigneEtiquette cle = (ChampLigneEtiquette) 
				event.getData();
			
			if (Messagebox.show(ObjectTypesFormatters
					.getLabel("message.deletion.message", 
							new String[]{Labels
						.getLabel("message.deletion.champ")}),
			Labels.getLabel("message.deletion.title"), 
			Messagebox.YES | Messagebox.NO, 
			Messagebox.QUESTION) == Messagebox.YES) {
				// si le champ était en base, il faudra le supprimer
				if (cle.getChampLigneEtiquetteId() != null) {
					champsToRemove.add(cle);
				}
				
				// on l'enlève de la liste
				champs.remove(cle);
				// ListModel list = new ListModelList(champs);
				// champsGrid.setModel(list);
				// getBinder().loadComponent(champsGrid);
			}
		}
	}
	
	/**
	 * Clic sur l'image pour monter une ligne.
	 * @param event
	 */
	public void onClickUpLigne(ForwardEvent event) {
		if (event.getData() != null) {
			ChampLigneEtiquette cle = (ChampLigneEtiquette) 
				event.getData();
			upObject(cle);
			getBinder().loadAttribute(champsGrid, "model");
		}
	}
	
	/**
	 * Clic sur l'image pour descendre une ligne.
	 * @param event
	 */
	public void onClickDownLigne(ForwardEvent event) {
		if (event.getData() != null) {
			ChampLigneEtiquette cle = (ChampLigneEtiquette) 
				event.getData();
			int tabIndex = champs.indexOf(cle);
			if (tabIndex + 1 < champs.size()) {
				upObject(champs.get(tabIndex + 1));
			}
			getBinder().loadAttribute(champsGrid, "model");
		}
	}
	
	/**
	 * Effectue l'operation de mouvements des objets au sein de la liste.
	 * @param objet a monter d'un cran
	 */
	private void upObject(ChampLigneEtiquette obj) {
		int tabIndex = champs.indexOf(obj);
		ChampLigneEtiquette supObjectInList = null;
		if (tabIndex - 1 > -1) {
			supObjectInList = champs.get(tabIndex - 1);
			champs.set(tabIndex, supObjectInList);
			champs.set(tabIndex - 1, obj);
		}
	}
	
	/**
	 * Affiche ou cache les lignes contenants les paramètres
	 * pour la police de caractères.
	 * @param show
	 */
	public void showFontValues(boolean show) {
		fontRow.setVisible(show);
		styleRow.setVisible(show);
		sizeRow.setVisible(show);
		contenuTitleRow.setVisible(show);
		enteteRow.setVisible(show);
		contenuRow.setVisible(show);
	}
	
	/**********************************************************************/
	/*********************** GETTERS **************************************/
	/**********************************************************************/
	
	public String getChampsTitle() {
		if (codeRadio.isChecked()) {
			return Labels.getLabel(
			"fiche.ligne.etiquette.champs.title.codeBarres");
		} else {
			return Labels.getLabel(
			"fiche.ligne.etiquette.champs.title.texte");
		}
	}
	
	public ConstWord getEnteteConstraint() {
		return LigneEtiquetteConstraints.getEnteteConstraint();
	}
	
	public ConstWord getContenuConstraint() {
		return LigneEtiquetteConstraints.getContenuConstraint();
	}

	public Component getParent() {
		return parent;
	}

	public void setParent(Component p) {
		this.parent = p;
	}

	public LigneEtiquette getLigneEtiquette() {
		return ligneEtiquette;
	}

	public void setLigneEtiquette(LigneEtiquette l) {
		this.ligneEtiquette = l;
	}

	public ListModelList<ChampLigneEtiquette> getChamps() {
		return champs;
	}

	public void setChamps(ListModelList<ChampLigneEtiquette> c) {
		this.champs = c;
	}

	public ChampLigneEtiquetteRowRenderer getChampsRowRenderer() {
		return champsRowRenderer;
	}

	public void setChampsRowRenderer(
			ChampLigneEtiquetteRowRenderer c) {
		this.champsRowRenderer = c;
	}

	public ListModelList<Entite> getEntites() {
		return entites;
	}

	public void setEntites(ListModelList<Entite> e) {
		this.entites = e;
	}

	public Entite getSelectedEntite() {
		return selectedEntite;
	}

	public void setSelectedEntite(Entite s) {
		this.selectedEntite = s;
	}

	public ListModelList<Entite> getEntitesToPrint() {
		return entitesToPrint;
	}

	public void setEntitesToPrint(ListModelList<Entite> e) {
		this.entitesToPrint = e;
	}

	public Entite getSelectedEntiteToPrint() {
		return selectedEntiteToPrint;
	}

	public void setSelectedEntiteToPrint(Entite s) {
		this.selectedEntiteToPrint = s;
	}

	public ListModelList<ImportChampDecorator> getChampsDecorator() {
		return champsDecorator;
	}

	public void setChampsDecorator(ListModelList<ImportChampDecorator> c) {
		this.champsDecorator = c;
	}

	public ImportChampDecorator getSelectedChamp() {
		return selectedChamp;
	}

	public void setSelectedChamp(ImportChampDecorator s) {
		this.selectedChamp = s;
	}

	public ListModelList<String> getFormatages() {
		return formatages;
	}

	public void setFormatages(ListModelList<String> f) {
		this.formatages = f;
	}

	public String getSelectedFormatage() {
		return selectedFormatage;
	}

	public void setSelectedFormatage(String s) {
		this.selectedFormatage = s;
	}

	public String getCaractere() {
		return caractere;
	}

	public void setCaractere(String c) {
		this.caractere = c;
	}

	public Integer getPremier() {
		return premier;
	}

	public void setPremier(Integer p) {
		this.premier = p;
	}

	public Integer getDernier() {
		return dernier;
	}

	public void setDernier(Integer d) {
		this.dernier = d;
	}

	public List<String> getFonts() {
		return fonts;
	}

	public void setFonts(List<String> f) {
		this.fonts = f;
	}

	public String getSelectedFont() {
		return selectedFont;
	}

	public void setSelectedFont(String s) {
		this.selectedFont = s;
	}

	public List<String> getStyles() {
		return styles;
	}

	public void setStyles(List<String> s) {
		this.styles = s;
	}

	public String getSelectedStyle() {
		return selectedStyle;
	}

	public void setSelectedStyle(String s) {
		this.selectedStyle = s;
	}

	public List<Integer> getSizes() {
		return sizes;
	}

	public void setSizes(List<Integer> s) {
		this.sizes = s;
	}

	public Integer getSelectedSize() {
		return selectedSize;
	}

	public void setSelectedSize(Integer s) {
		this.selectedSize = s;
	}

	public List<ChampLigneEtiquette> getChampsToRemove() {
		return champsToRemove;
	}

	public void setChampsToRemove(List<ChampLigneEtiquette> c) {
		this.champsToRemove = c;
	}

}
