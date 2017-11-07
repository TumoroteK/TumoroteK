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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

/**
 * Classe permettant de gérer l'impression d'étiquettes de type V1 : 
 * l'utilisateur peut définir le contenu à imprimer.
 * @author pierre
 *
 */
public class FicheEtiquetteModale { // extends AbstractFicheCombineController {
		
	// private static final long serialVersionUID = 2519424454111578877L;
	
	@Wire("#fwinEtiquetteModale")
	private Window fwinEtiquetteModale;
	@Wire("#copiesCheckbox")
	private Checkbox copiesCheckbox;
	@Wire("#listeCheckbox")
	private Checkbox listeCheckbox;
	@Wire("#premierNumeroBox")
	private Intbox premierNumeroBox;
	@Wire("#dernierNumeroBox")
	private Intbox dernierNumeroBox;
	
	private Window imprimanteWindow;
	
	private Integer nbrCopies = 1;
	private String codePrelevement;
	private String numeroTube;
	private String type;
	private String patient;
	private String dateCongelation;
	private String quantite;
	private Integer premierNumeroTube;
	private Integer dernierNumeroTube;
	
	private Boolean copies = false;
	private Boolean listeInc = true;

	
	private Modele modele;
	private Imprimante selectedImprimante;
	private String rawLang = null;
	private BarcodeFieldDefault barcodeBy = new BarcodeFieldDefault();
	private List<String> separators = new ArrayList<String>();
	private String separator;
	
	//private List<LigneEtiquette> lignesToPrint;
	private Vector<String> mbioData;
	private List<List<LigneEtiquette>> listlignes = new ArrayList<List<LigneEtiquette>>();
	
	@AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
    }
	
	@Init
	public void init(@ExecutionArgParam("modele") Modele m, 
			@ExecutionArgParam("imprimante") Imprimante imp,
			@ExecutionArgParam("rawLang") String rL,
			@ExecutionArgParam("barcodeBy") BarcodeFieldDefault bY,
			@ExecutionArgParam("parent") Window win) {
		modele = m;
		setRawLang(rL);
		setSelectedImprimante(imp);
		setImprimanteWindow(win);	
		setBarcodeBy(bY);
		
		initSeparators();
	}
	
	public void initSeparators() {
		getSeparators().add(".");
		getSeparators().add("-");
		getSeparators().add("_");
		
		setSeparator(".");
	}
	
	public void onOK() {
	}
	
	@Command
	@NotifyChange("copies")
	public void switchCopies() {
		setCopies(!copies);
	}
	
	@Command
	@NotifyChange("listeInc")
	public void switchListeInc() {
		setListeInc(!listeInc);
	}
	
	@Command
	public void cancel() {
		Events
			.postEvent("onClose", 
				fwinEtiquetteModale, null);
		Events
		.postEvent("onClose", 
			getImprimanteWindow(), null);
	}
	
	@Command
	public void print() {
		
		listlignes.clear();
		
		if (copiesCheckbox.isChecked()) {
			// setLignes(createListeEtiquette(""));
			listlignes.add(createListeEtiquette(""));
			setMbioData(createMbioData());
		} else {
			nbrCopies = 1;
		}
		if (listeCheckbox.isChecked()) {
			
			listlignes.clear();

			if (getPremierNumeroTube() == null) {
				throw new WrongValueException(
					premierNumeroBox, 
						Labels.getLabel("validation.syntax.empty"));
			}
			
			if (getDernierNumeroTube() == null) {
				throw new WrongValueException(
					dernierNumeroBox, 
						Labels.getLabel("validation.syntax.empty"));
			}
			
			if (getPremierNumeroTube() <= getDernierNumeroTube()) {
				for (int i = premierNumeroTube; i <= dernierNumeroTube; i++) {
					createListLignes(i);
				}
			}
		}
		
		// aucune boxe selectionné
		if (listlignes.isEmpty()) {
			// setLignes(createListeEtiquette(""));
			listlignes.add(createListeEtiquette(""));
		}
				
		if (getSelectedImprimante() != null) {
			// si on utilise l'API tumo
			if (getSelectedImprimante().getImprimanteApi().getNom().equals("tumo")) {
				try {            
					int completed = 0;
					
					completed = ManagerLocator.getTumoBarcodePrinter()
							.printListCopiesData(getListlignes(), nbrCopies, getSelectedImprimante(), 
									getModele(), getRawLang(), getBarcodeBy());
//					
//					if (copiesCheckbox.isChecked()) {
//						if (!listeCheckbox.isChecked()) {
//							completed = ManagerLocator.getTumoBarcodePrinter()
//								.printData(getLignes(), nbrCopies, getSelectedImprimante(), 
//										getModele(), rawLang);
//						} else { // les deux sont cochées
//							completed = ManagerLocator.getTumoBarcodePrinter()
//									.printListCopiesData(getListlignes(), nbrCopies, getSelectedImprimante(), 
//											getModele(),rawLang);
//						}
//					} else if (listeCheckbox.isChecked()) {
//						completed = ManagerLocator.getTumoBarcodePrinter()
//							.printListData(getListlignes(), getSelectedImprimante(), 
//									getModele(), rawLang);
//					} else { // impression une seule etiquette
//						completed = ManagerLocator.getTumoBarcodePrinter()
//								.printData(getLignes(), 1, getSelectedImprimante(), 
//										getModele(), rawLang);
//					}
					
					if (completed != 1) {
						if (completed == -1) {
							Messagebox.show(ObjectTypesFormatters
									.getLabel("validation.erreur" 
									+ ".imprimante.non.detectee", 
									new String[] {getSelectedImprimante().getNom()}), 
							"Error", Messagebox.OK, Messagebox.ERROR);
						} else {
							Messagebox.show(Labels
							.getLabel("validation.erreur.impression"), 
							"Error", Messagebox.OK, Messagebox.ERROR);
						}
					} else {
						Messagebox.show(Labels
								.getLabel("general.impression.ok"), 
								"OK", Messagebox.OK, 
								Messagebox.INFORMATION);
					}
				} catch (Exception re) {
					Messagebox.show(AbstractController.handleExceptionMessage(re), 
							"Error", Messagebox.OK, Messagebox.ERROR);
					
		    	} 
//				catch (Exception e) {
//		    		StringBuffer sb = new StringBuffer();
//		    		sb.append(Labels
//								.getLabel("validation.erreur.impression"));
//		    		sb.append(" ");
//		    		sb.append(e.getMessage());
//		    		Messagebox.show(sb.toString(), 
//							"Error", Messagebox.OK, Messagebox.ERROR);
//		    	}
			} else if (getSelectedImprimante().getImprimanteApi().getNom().equals("mbio")) {
				// si on utilise l'API mbio
				MBioFileProperties mBioFile = 
					new MBioFileProperties(getSelectedImprimante().getMbioPrinter());
				
				MBioBarcodePrinter printer = new MBioBarcodePrinter(
						mBioFile, 
						mBioFile.getConfDir());
				int completed = 0;
				completed = printer.printData(getMbioData(), getNbrCopies());
				
				if (completed != 1) {
					Messagebox.show(Labels
							.getLabel("validation.erreur.impression"), 
							"Error", Messagebox.OK, Messagebox.ERROR);
				} else {
					Messagebox.show(Labels.getLabel("general.impression.ok"), "Confirm Dialog", 
							Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, 
							new EventListener<Event>() {
							    public void onEvent(Event evt) throws InterruptedException {
							        if (evt.getName().equals("onOK")) {
							            System.out.println("Data Saved !");
							        } else if (evt.getName().equals("onIgnore")) {
							        	System.out.println("2");
							        } else {
							        	System.out.println("3");
							        }
							    }
					});
					
					
				}
			}
		}
		
		// fermeture de la fenêtre
		// Events.postEvent(new Event("onClose", fwinEtiquetteModale));
	}
	
	public void createListLignes(int i) {
		getListlignes().add(createListeEtiquette(getSeparator() + i));		
	}

	public List<LigneEtiquette> createListeEtiquette(String inc)  {
		LigneEtiquette l1, l2, l3, l4, l5, l6, l7, l8, l9;
		List<LigneEtiquette> ligneEtiquettes = new ArrayList<LigneEtiquette>();
		
		l1 = new LigneEtiquette();
		l1.setIsBarcode(true);
		l1.setContenu(codePrelevement);
		
		l2 = new LigneEtiquette();
		l2.setIsBarcode(false);
		l2.setEntete(ConfigManager.ENTETE_PRELEVEMENT);
		l2.setContenu(codePrelevement);
		
		String inContent = null;
		if (!inc.equals("")) {
			inContent = inc;
		} else {
			inContent = numeroTube;
		}
		
		l3 = new LigneEtiquette();
		l3.setIsBarcode(true);
		l3.setContenu(codePrelevement + inContent);
				
		l4 = new LigneEtiquette();
		l4.setIsBarcode(false);
		l4.setEntete(ConfigManager.ENTETE_TUBE);
		l4.setContenu(inContent);
		
		l5 = new LigneEtiquette();
		l5.setIsBarcode(false);
		l5.setEntete(ConfigManager.ENTETE_TYPE);
		l5.setContenu(type);
		
		l6 = new LigneEtiquette();
		l6.setIsBarcode(false);
		l6.setEntete(ConfigManager.ENTETE_PATIENT);
		l6.setContenu(patient);
		
		l7 = new LigneEtiquette();
		l7.setIsBarcode(false);
		l7.setEntete(ConfigManager.ENTETE_DATE_CONGELATION);
		l7.setContenu(dateCongelation);
		
		l8 = new LigneEtiquette();
		l8.setIsBarcode(false);
		l8.setEntete(ConfigManager.ENTETE_QUANTITE);
		l8.setContenu(quantite);
		
		l9 = new LigneEtiquette();
		l9.setIsBarcode(false);
		l9.setEntete("");
		if(modele != null){
		l9.setContenu(modele.getTexteLibre() != null ? modele.getTexteLibre()
				: "");
		} else {
			l9.setContenu("");
		}
		
		ligneEtiquettes.add(l1);
		ligneEtiquettes.add(l2);
		ligneEtiquettes.add(l3);
		ligneEtiquettes.add(l4);
		ligneEtiquettes.add(l5);
		ligneEtiquettes.add(l6);
		ligneEtiquettes.add(l7);
		ligneEtiquettes.add(l8);
		ligneEtiquettes.add(l9);
		
		return ligneEtiquettes;
	}
	
	public Vector<String> createMbioData() {
		Vector<String> data = new Vector<String>();
		//for mbio protocol ...
		data.add(codePrelevement);
		data.add(numeroTube);
		data.add(type);
		data.add(patient);
		data.add(dateCongelation);
		data.add(quantite);
		return data;
	}
	
	public ConstWord getNomConstraint() {
		return EtiquetteConstraints.getNomConstraint();
	}

	public String getCodePrelevement() {
		return codePrelevement;
	}

	public void setCodePrelevement(String c) {
		this.codePrelevement = c;
	}

	public String getNumeroTube() {
		return numeroTube;
	}

	public void setNumeroTube(String n) {
		this.numeroTube = n;
	}

	public String getType() {
		return type;
	}

	public void setType(String t) {
		this.type = t;
	}

	public String getPatient() {
		return patient;
	}

	public void setPatient(String p) {
		this.patient = p;
	}

	public String getDateCongelation() {
		return dateCongelation;
	}

	public void setDateCongelation(String d) {
		this.dateCongelation = d;
	}

	public String getQuantite() {
		return quantite;
	}

	public void setQuantite(String q) {
		this.quantite = q;
	}

	public Integer getNbrCopies() {
		return nbrCopies;
	}

	public void setNbrCopies(Integer nbr) {
		this.nbrCopies = nbr;
	}

	public Integer getPremierNumeroTube() {
		return premierNumeroTube;
	}

	public void setPremierNumeroTube(Integer pTube) {
		this.premierNumeroTube = pTube;
	}

	public Integer getDernierNumeroTube() {
		return dernierNumeroTube;
	}

	public void setDernierNumeroTube(Integer dTube) {
		this.dernierNumeroTube = dTube;
	}

	public Modele getModele() {
		return modele;
	}
	
	public void setModele(Modele m) {
		this.modele = m;
	}

	public Imprimante getSelectedImprimante() {
		return selectedImprimante;
	}

	public void setSelectedImprimante(Imprimante s) {
		this.selectedImprimante = s;
	}

	public Boolean getCopies() {
		return copies;
	}

	public void setCopies(Boolean c) {
		this.copies = c;
	}

	public Boolean getListeInc() {
		return listeInc;
	}

	public void setListeInc(Boolean li) {
		this.listeInc = li;
	}

	public Intbox getPremierNumeroBox() {
		return premierNumeroBox;
	}

	public void setPremierNumeroBox(Intbox premierNumeroBox) {
		this.premierNumeroBox = premierNumeroBox;
	}

	public Intbox getDernierNumeroBox() {
		return dernierNumeroBox;
	}

	public void setDernierNumeroBox(Intbox dernierNumeroBox) {
		this.dernierNumeroBox = dernierNumeroBox;
	}

//	public List<LigneEtiquette> getLignes() {
//		return lignesToPrint;
//	}
//
//	public void setLignes(List<LigneEtiquette> lignes) {
//		this.lignesToPrint = lignes;
//	}

	public Vector<String> getMbioData() {
		return mbioData;
	}

	public void setMbioData(Vector<String> m) {
		this.mbioData = m;
	}

	public List<List<LigneEtiquette>> getListlignes() {
		return listlignes;
	}

	public void setListlignes(List<List<LigneEtiquette>> l) {
		this.listlignes = l;
	}

	public Window getImprimanteWindow() {
		return imprimanteWindow;
	}

	public void setImprimanteWindow(Window i) {
		this.imprimanteWindow = i;
	}

	public String getRawLang() {
		return rawLang;
	}

	public void setRawLang(String rawLang) {
		this.rawLang = rawLang;
	}

	public BarcodeFieldDefault getBarcodeBy() {
		return barcodeBy;
	}

	public void setBarcodeBy(BarcodeFieldDefault b) {
		this.barcodeBy = b;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String s) {
		this.separator = s;
	}

	public List<String> getSeparators() {
		return separators;
	}
}
