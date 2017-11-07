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
package fr.aphp.tumorotek.action.etiquettes;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.io.export.Champ;

public class FicheEtiquetteDynModale extends FicheEtiquetteModale {

	// private static final long serialVersionUID = -8351742550456394725L;

	@Wire("#fwinEtiquetteDynModale")
	private Window fwinEtiquetteDynModale;
	@Wire("#copiesCheckbox")
	private Checkbox copiesCheckbox;
	@Wire("#listeCheckbox")
	private Checkbox listeCheckbox;
	@Wire("#premierNumeroBox")
	private Intbox premierNumeroBox;
	@Wire("#dernierNumeroBox")
	private Intbox dernierNumeroBox;
	@Wire("#rowsEtiquette")
	private Rows rowsEtiquette;			
	
	private List<Textbox> textboxes = new ArrayList<Textbox>();
	private List<Checkbox> checkboxes = new ArrayList<Checkbox>();
	private List<Label> labels = new ArrayList<Label>();

	private List<LigneEtiquette> lignes = new ArrayList<LigneEtiquette>();
	
	private List<Row> rowsToAdd = new ArrayList<Row>();

	// private Row nbCopiesRow;
	// private Row tubeListeRow;

	@AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        for (Row r : rowsToAdd) {
            rowsEtiquette.appendChild(r);
		}
    }

	@Init
	public void init(@ExecutionArgParam("modele") Modele m, 
			@ExecutionArgParam("imprimante") Imprimante imp,
			@ExecutionArgParam("rawLang") String rL,
			@ExecutionArgParam("barcodeBy") BarcodeFieldDefault bY,
			@ExecutionArgParam("parent") Window win, 
			@ExecutionArgParam("lignes") List<LigneEtiquette> _ligs) {
		setModele(m);
		setRawLang(rL);
		setSelectedImprimante(imp);	
		setImprimanteWindow(win);
		setBarcodeBy(bY);
		
		initSeparators();
		
		if (getModele() != null) {
			if (_ligs == null) {
				lignes = ManagerLocator.getLigneEtiquetteManager()
					.findByModeleManager(getModele());
			} else {
				lignes = _ligs;
			}
		}

		drawLignes();

	}

	private void drawLignes() {
		rowsToAdd.clear();
		for (int i = 0; i < lignes.size(); i++) {
			Row row = new Row();
			if (lignes.get(i).getEntete() != null) {
				Label label = new Label(lignes.get(i).getEntete());
				label.setSclass("formLabel");
				label.setParent(row);
			} else {
				if (lignes.get(i).getIsBarcode()) {
					Image img = new Image();
					img.setSrc("/images/icones/codeBarres.png");
					img.setParent(row);
				} else {
					List<ChampLigneEtiquette> champs = new ArrayList<ChampLigneEtiquette>();
					if (lignes.get(i).getLigneEtiquetteId() != null) {
						champs = ManagerLocator.getChampLigneEtiquetteManager()
								.findByLigneEtiquetteManager(lignes.get(i));
					}

					if (champs.size() > 0) {
						formateChampsForLigne(lignes.get(i), row, row)
								.setParent(row);
					} else {
						Label label = new Label(
								Labels.getLabel("etiquette.texte.libre"));
						label.setSclass("formValue");
						label.setParent(row);
					}

				}
			}

			Textbox box = new Textbox();
			box.setConstraint(getNomConstraint());
			// box.setWidth("150px");
			box.setHflex("1");
			if (lignes.get(i).getContenu() != null) {
				box.setValue(lignes.get(i).getContenu());
			}
			box.setParent(row);
			textboxes.add(box);

			// checkbox d'incrémentation
			Div div = new Div();
			Checkbox check = new Checkbox();
			check.setChecked(false);
			//check.setVisible(false);
			check.setParent(div);
			checkboxes.add(check);
			Label lab = new Label();
			lab.setParent(div);
			labels.add(lab);
			div.setParent(row);

			rowsToAdd.add(row);
		}		
	}

	public Component formateChampsForLigne(LigneEtiquette ligne, Row row,
			Component parent) {
		List<String> valuesEchans = new ArrayList<String>();
		List<String> valuesDerives = new ArrayList<String>();
		List<ChampLigneEtiquette> champs = ManagerLocator
				.getChampLigneEtiquetteManager().findByLigneEtiquetteManager(
						ligne);
		for (int i = 0; i < champs.size(); i++) {
			Champ chp = champs.get(i).getChamp();
			StringBuffer sb = new StringBuffer();
			if (chp.getChampEntite() != null) {
				sb.append(ObjectTypesFormatters.getLabelForChampEntite(chp
						.getChampEntite()));
				if (champs.get(i).getEntite().getNom().equals("Echantillon")) {
					valuesEchans.add(sb.toString());
				} else {
					valuesDerives.add(sb.toString());
				}
			}
		}

		if (valuesEchans.size() + valuesDerives.size() > 0) {
			Label champsLabel;
			StringBuffer sbTmp = new StringBuffer();
			if (valuesEchans.size() > 0) {
				sbTmp.append(valuesEchans.get(0));
				sbTmp.append(" [Echantillon]");
				champsLabel = new Label(sbTmp.toString());
			} else {
				sbTmp.append(valuesDerives.get(0));
				sbTmp.append(" [ProdDerive]");
				champsLabel = new Label(sbTmp.toString());
			}

			if (valuesEchans.size() + valuesDerives.size() > 1) {

				Hbox labelAndLinkBox = new Hbox();
				labelAndLinkBox.setSpacing("5px");
				Label moreLabel = new Label("...");
				moreLabel.setClass("formLink");
				Popup malPopUp = new Popup();
				malPopUp.setParent(labelAndLinkBox);

				// affichage des données des échantillons
				Label lab;
				Vbox popupVbox = new Vbox();
				StringBuffer labStr = new StringBuffer();
				for (int i = 0; i < valuesEchans.size(); i++) {
					labStr.append(valuesEchans.get(i));
					if (i + 1 < valuesEchans.size()) {
						labStr.append(", ");
					} else {
						labStr.append(" [Echantillon]");
					}
				}
				if (!labStr.toString().equals("")) {
					lab = new Label(labStr.toString());
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}

				// affichage des données des dérivés
				labStr = new StringBuffer();
				for (int i = 0; i < valuesDerives.size(); i++) {
					labStr.append(valuesDerives.get(i));
					if (i + 1 < valuesDerives.size()) {
						labStr.append(", ");
					} else {
						labStr.append(" [ProdDerive]");
					}
				}
				if (!labStr.toString().equals("")) {
					lab = new Label(labStr.toString());
					lab.setSclass("formValue");
					popupVbox.appendChild(lab);
				}

				malPopUp.appendChild(popupVbox);
				moreLabel.setTooltip(malPopUp);
				labelAndLinkBox.appendChild(champsLabel);
				labelAndLinkBox.appendChild(moreLabel);
				return labelAndLinkBox;
			} else {
				return champsLabel;
			}
		} else {
			return new Label();
		}
	}

	public void onOK() {
	}

//	public void onCheck$copiesRadio() {
//		nbCopiesRow.setVisible(true);
//		tubeListeRow.setVisible(false);
//		for (int i = 0; i < labels.size(); i++) {
//			labels.get(i).setVisible(true);
//		}
//		for (int i = 0; i < checkboxes.size(); i++) {
//			checkboxes.get(i).setVisible(false);
//		}
//	}
//
//	public void onCheck$listeRadio() {
//		nbCopiesRow.setVisible(false);
//		tubeListeRow.setVisible(true);
//		for (int i = 0; i < labels.size(); i++) {
//			labels.get(i).setVisible(false);
//		}
//		for (int i = 0; i < checkboxes.size(); i++) {
//			checkboxes.get(i).setVisible(true);
//		}
//	}
	
	@Override
	public List<LigneEtiquette> createListeEtiquette(String inc) {
		List<LigneEtiquette> ls = new ArrayList<LigneEtiquette>();
		ls.addAll(lignes);

		for (int i = 0; i < textboxes.size(); i++) {
			if (lignes.get(i).getEntete() == null) {

				lignes.get(i).setEntete("");
			}

			String contenu = textboxes.get(i).getValue();
			ls.get(i).setContenu(contenu != null ? contenu : "");
		}
		return ls;
	}
	
	@Override
	public void createListLignes(int i) {
		List<LigneEtiquette> tmpLigne = new ArrayList<LigneEtiquette>();
		LigneEtiquette ligne;
		// on parcourt chaque textbox pour récupérer
		// la valeur saisie
		for (int j = 0; j < textboxes.size(); j++) {
			ligne = new LigneEtiquette();
			if (lignes.get(j).getEntete() != null) {
				ligne.setEntete(lignes.get(j).getEntete());
			} else {
				ligne.setEntete("");
			}
			String contenu = textboxes.get(j).getValue();

			// si le checkbox d'incrémentation est
			// coché
			if (checkboxes.get(j).isChecked()) {
				if (contenu.equals("")) {
					contenu += i;
				} else {
					contenu += getSeparator() + i;
				}
			}
			ligne.setContenu(contenu != null ? contenu : "");
			ligne.setIsBarcode(lignes.get(j).getIsBarcode());
			ligne.setSize(lignes.get(j).getSize());
			ligne.setStyle(lignes.get(j).getStyle());
			ligne.setFont(lignes.get(j).getFont());
			tmpLigne.add(ligne);
			ligne = new LigneEtiquette();
		}

		getListlignes().add(tmpLigne);
	}
	
	@Override
	public Vector<String> createMbioData() {
		return null;
	}
	
	

//	public void onClick$print() {
//		if (isTest && selectedImprimante == null) {
//			throw new WrongValueException(imprimantesBox,
//					Labels.getLabel("etiquette.choix.imprimante.obligatoire"));
//		}

//		Vector<String> data = new Vector<String>();
//		List<List<LigneEtiquette>> dataListeEtiquette = new ArrayList<List<LigneEtiquette>>();
//
//		// si l'utilisateur veut imprimer plusieurs copies
//		if (copiesRadio.isChecked()) {
//			// on parcourt chaque textbox pour récupérer
//			// la valeur saisie
//			for (int i = 0; i < textboxes.size(); i++) {
//				if (lignes.get(i).getEntete() == null) {
//
//					lignes.get(i).setEntete("");
//				}
//
//				String contenu = textboxes.get(i).getValue();
//				lignes.get(i).setContenu(contenu != null ? contenu : "");
//				//mbio datas ...
//				data.add(lignes.get(i).getEntete() + lignes.get(i).getContenu());
//			}
//		} else if (listeRadio.isChecked()) {
//			// si l'utilisateur veut utiliser l'incrémentation
//			if (getPremierNumeroTube() == null) {
//				throw new WrongValueException(premierNumeroBox,
//						Labels.getLabel("validation.syntax.empty"));
//			}
//
//			if (getDernierNumeroTube() == null) {
//				throw new WrongValueException(dernierNumeroBox,
//						Labels.getLabel("validation.syntax.empty"));
//			}
//
//			if (getPremierNumeroTube() < getDernierNumeroTube()) {
//				for (int i = getPremierNumeroTube(); i <= getDernierNumeroTube(); i++) {
//					List<LigneEtiquette> tmpLigne = new ArrayList<LigneEtiquette>();
//					LigneEtiquette ligne;
//					// on parcourt chaque textbox pour récupérer
//					// la valeur saisie
//					for (int j = 0; j < textboxes.size(); j++) {
//						ligne = new LigneEtiquette();
//						if (lignes.get(j).getEntete() != null) {
//							ligne.setEntete(lignes.get(j).getEntete());
//						} else {
//							ligne.setEntete("");
//						}
//						String contenu = textboxes.get(j).getValue();
//
//						// si le checkbox d'incrémentation est
//						// coché
//						if (checkboxes.get(j).isChecked()) {
//							if (contenu.equals("")) {
//								contenu += i;
//							} else {
//								contenu += "." + i;
//							}
//						}
//						ligne.setContenu(contenu != null ? contenu : "");
//						ligne.setIsBarcode(lignes.get(j).getIsBarcode());
//						tmpLigne.add(ligne);
//						ligne = new LigneEtiquette();
//					}
//
//					dataListeEtiquette.add(tmpLigne);
//				}
//			}
//		}

//		Imprimante imp = null;
//		if (isTest) {
//			imp = selectedImprimante;
//		} else {
//			// on récupère les imprimantes associées au compte
//			// pour la banque courante
//			List<AffectationImprimante> imprimantesAff = ManagerLocator
//					.getAffectationImprimanteManager()
//					.findByBanqueUtilisateurManager(
//							SessionUtils.getSelectedBanques(sessionScope)
//									.get(0),
//							SessionUtils.getLoggedUser(sessionScope));
//			if (imprimantesAff.size() > 0) {
//				imp = imprimantesAff.get(0).getImprimante();
//			}
//		}

//		if (getSelectedImprimante() != null) {
//			// si on utilise l'API tumo
//			if (getSelectedImprimante().getImprimanteApi().getNom().equals("tumo")) {
//				try {
//					int completed = 0;
//
//					if (copiesRadio.isChecked()) {
//						completed = ManagerLocator.getTumoBarcodePrinter()
//								.printData(lignes, getNbrCopies(), 
//										getSelectedImprimante(), getModele());
//					} else if (listeRadio.isChecked()) {
//						completed = ManagerLocator.getTumoBarcodePrinter()
//								.printListData(dataListeEtiquette, getSelectedImprimante(), 
//										getModele());
//					}
//
//					if (completed != 1) {
//						if (completed == -1) {
//							Messagebox.show(ObjectTypesFormatters
//									.getLabel("validation.erreur.imprimante.non.detectee",
//								new String[] { getSelectedImprimante().getNom() }),
//											"Error", Messagebox.OK,
//											Messagebox.ERROR);
//						} else {
//							Messagebox.show(Labels
//									.getLabel("validation.erreur.impression"),
//									"Error", Messagebox.OK, Messagebox.ERROR);
//						}
//					} else {
//						Messagebox.show(
//								Labels.getLabel("general.impression.ok"), "OK",
//								Messagebox.OK, Messagebox.INFORMATION);
//					}
//				} catch (RuntimeException re) {
//					Messagebox.show(AbstractController.handleExceptionMessage(re), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//
//				} catch (Exception e) {
//					StringBuffer sb = new StringBuffer();
//					sb.append(Labels.getLabel("validation.erreur.impression"));
//					sb.append(" ");
//					sb.append(e.getMessage());
//					Messagebox.show(sb.toString(), "Error", Messagebox.OK,
//							Messagebox.ERROR);
//				}
//			} else if (getSelectedImprimante().getImprimanteApi().getNom().equals("mbio")) {
//				// si on utilise l'API mbio
//				MBioFileProperties mBioFile = new MBioFileProperties(
//						getSelectedImprimante().getMbioPrinter());
//
//				MBioBarcodePrinter printer = new MBioBarcodePrinter(mBioFile,
//						mBioFile.getConfDir());
//				int completed = 0;
//				completed = printer.printData(data, getNbrCopies());
//
//				if (completed != 1) {
//					Messagebox.show(
//							Labels.getLabel("validation.erreur.impression"),
//							"Error", Messagebox.OK, Messagebox.ERROR);
//				} else {
//					Messagebox.show(Labels.getLabel("general.impression.ok"),
//							"OK", Messagebox.OK, Messagebox.INFORMATION);
//				}
//			}
//		}
//
//		// fermeture de la fenêtre
//		Events.postEvent(new Event("onClose", self.getRoot()));
//	}

//	public ConstWord getNomConstraint() {
//		return EtiquetteConstraints.getNomConstraint();
//	}


	public List<Textbox> getTextboxes() {
		return textboxes;
	}

	public void setTextboxes(List<Textbox> t) {
		this.textboxes = t;
	}


	public List<Checkbox> getCheckboxes() {
		return checkboxes;
	}

	public void setCheckboxes(List<Checkbox> c) {
		this.checkboxes = c;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> l) {
		this.labels = l;
	}
	
	@Override
	@Command
	public void cancel() {
		Events
			.postEvent("onClose", 
					fwinEtiquetteDynModale, null);
		Events
		.postEvent("onClose", 
			getImprimanteWindow(), null);
	}

}
