package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ChampEntite;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ThesaurusValue;

//protected Groupbox gridFormPrlvtComp;
//protected Groupbox groupPatient;
//protected Div groupLaboInter;
//protected Groupbox groupEchans;
//protected Groupbox groupDerivesPrlvt;

//// Identifiants prélèvement
//private Div identifiantBlockDiv;
//private Div codeDiv;
//private Div codeLaboDiv;
//private Div natureDiv;
//
//// /////
//// group patient + maladie
//// private Groupbox groupPatient; // géré dans classe parente 
//
//// Resume patient
//private Div patientBlockDiv;
//private Div nipDiv;
//private Div ndaDiv;
//private Div nomDiv;
//private Div prenomDiv;
//private Div dateNaisDiv;
//private Div sexeDiv;
//
//// Maladie
//// private Div linkMaladie; // géré dans classe parente
//private Div libelleDiv;
//private Div codeMaladieDiv;
//
//// //////
//// group prelevement
//// private Groupbox groupPrlvt; // géré dans classe parente
//
//// Informations prélèvement
//private Div infoPrelBlockDiv;
//private Div datePrelDiv;
//private Div typeDiv;
//private Div sterileDiv;
//private Div risquesDiv;
//private Div etabPreleveurDiv;
//private Div servicePreleveurDiv;
//private Div preleveurDiv;
//
//// Conditionnement
//private Div conditBlockDiv;
//private Div conditTypeDiv;
//private Div conditNbrDiv;
//private Div conditMilieuDiv;
//
//// Consentement
//private Div consentBlockDiv;
//private Div consentTypeDiv;
//private Div consentDateDiv;
//
//// //////
//// Transfert site preleveur vers site stockage
//// private Groupbox gridFormPrlvtComp; // géré dans classe parente
//
//// Depart site preleveur
//private Div departBlockDiv;
//private Div dateDepartDiv;
//private Div transporteurDiv;
//private Div tempTranspDiv;
//private Div congPrelDiv;
//
//// Sites d'analyse / Labos intermédiaires
//// private Div groupLaboInter; // géré dans classe parente
//
//// Arrivee site stockage
//private Div arriveeBlockDiv;
//private Div dateArriveeDiv;
//private Div operateurDiv;
//private Div quantiteDiv;
//private Div conformeArriveeDiv;
//private Div congBiothequeDiv;
//
//// //////
//// Echantillons
//// private Groupbox groupEchans; // géré dans classe parente
//
//// //////
//// Dérives
//// private Groupbox groupDerivesPrlvt; // géré dans classe parente
//


public class GatsbiController {
	
	private static final Log log = LogFactory.getLog(GatsbiController.class);

	private static final String[] divBlockIds = new String[] {
			"identifiantBlockDiv",
			"patientBlockDiv",
			"infoPrelBlockDiv",
			"conditBlockDiv",
			"consentBlockDiv",
			"departBlockDiv",
			"arriveeBlockDiv"
		};
		
	
	private static final String[] divItemIds = new String[] {
		"codeDiv",
		"codeLaboDiv",
		"natureDiv",
		"nipDiv",
		"ndaDiv",
		"nomDiv",
		"prenomDiv",
		"dateNaisDiv",
		"sexeDiv",
		"libelleDiv",
		"codeMaladieDiv",
		"datePrelDiv",
		"typeDiv",
		"sterileDiv",
		"risquesDiv",
		"etabPreleveurDiv",
		"servicePreleveurDiv",
		"preleveurDiv",
		"conditTypeDiv",
		"conditNbrDiv",
		"conditMilieuDiv",
		"consentTypeDiv",
		"consentDateDiv",
		"dateDepartDiv",
		"transporteurDiv",
		"tempTranspDiv",
		"congPrelDiv",
		"dateArriveeDiv",
		"operateurDiv",
		"quantiteDiv",
		"conformeArriveeDiv",
		"congBiothequeDiv"
	};
	
	public GatsbiController() {
	}
	
	public static List<Div> wireBlockDivsFromMainComponent(Div main) {
		return Arrays.stream(divBlockIds)
			.map(id -> (Div) main.getFellowIfAny(id))
			.filter(d -> d != null)
			.collect(Collectors.toList());
	}
	
	public static List<Div> wireItemDivsFromMainComponent(Div main) {
		return Arrays.stream(divItemIds)
			.map(id -> (Div) main.getFellowIfAny(id))
			.filter(d -> d != null)
			.collect(Collectors.toList());
	}
	
	public static void showOrhideItems(List<Div> items, List<Div> blocks, Contexte c) {
		log.debug("showing or hiding items");
		if (items != null && c != null) {
			
			List<Integer> hidden = c.getHiddenChampEntiteIds();
			
			for (Div div : items) {
				div.setVisible(!div.hasAttribute("champId") || !hidden.contains(Integer.valueOf((String) div.getAttribute("champId"))));
				log.debug((div.isVisible() ? "showing " : "hiding ").concat(div.getId()));
			}
		}
		
		log.debug("showing or hiding blocks");
		if (blocks != null) {
			for (Div div : blocks) {
				div.setVisible(div.hasFellow(div.getId().concat("Container")) && 
					div.getFellow(div.getId().concat("Container")).getChildren().stream().anyMatch(d -> d.isVisible()));
				
				log.debug((div.isVisible() ? "showing " : "hiding ").concat(div.getId()));
			}
		}
	}
	
	public static void switchItemsRequiredOrNot(List<Div> items, Contexte c, List<Listbox> lboxes, List<Combobox> cboxes, 
																								List<Div> reqConformeDivs) {
		log.debug("switch items required or not");
		if (items != null && c != null) {
			
			List<Integer> required = c.getRequiredChampEntiteIds();
			
			if (!required.isEmpty()) {
				boolean isReq;
				for (Div div : items) {
					if (div.isVisible()) {
						isReq = (div.hasAttribute("champId") && required.contains(Integer.valueOf((String) div.getAttribute("champId"))));
						if (isReq) { // required
							div.setSclass(div.getSclass().concat(" item-required"));
							
							Component formElement = null;
							
							formElement = findInputOrListboxElement(div);
							
							if (formElement != null) {
								if (formElement instanceof InputElement) {
									if (((InputElement) formElement).getConstraint() != null) {
										if (((InputElement) formElement).getConstraint() instanceof TumoTextConstraint) {
											((TumoTextConstraint) ((InputElement) formElement).getConstraint()).setNullable(false);
										} else if (((InputElement) formElement).getConstraint() instanceof SimpleConstraint) {
											
											((InputElement) formElement)
												.setConstraint(new SimpleConstraint(
														((SimpleConstraint) ((InputElement) formElement).getConstraint()).getFlags()
														+ SimpleConstraint.NO_EMPTY));		
											
											log.debug(((InputElement) formElement).getConstraint());
											
										} else {
											throw new RuntimeException(div.getId() + " input constraint unknown");
										}
									} else if (!(formElement instanceof Combobox)) {
										((InputElement) formElement).setConstraint("no empty");
									} else { // combobox
										cboxes.add(((Combobox) formElement));
									}
								} else if (formElement instanceof CalendarBox) {
									((CalendarBox) formElement).setConstraint("no empty");
								} else { // listbox
									lboxes.add(((Listbox) formElement));
								}
							} else if (div.getId().startsWith("conforme")) { // non-conformite
								reqConformeDivs.add(div);
							}					
						}
					
						log.debug("switching ".concat(div.getId()).concat(" ")
							.concat(!isReq ? "not" : "").concat("required"));
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void appliThesaurusValues(List<Div> items, Contexte contexte, AbstractFicheEditController controller) 
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		log.debug("applying thesaurus values");
		if (items != null && contexte != null) {
			
			List<Integer> thesaurii = contexte.getThesaurusChampEntiteIds();
			
			if (!thesaurii.isEmpty()) {
				for (Div div : items) {
					if(div.hasAttribute("champId") && div.hasAttribute("listmodel") 
							&& thesaurii.contains(Integer.valueOf((String) div.getAttribute("champId")))) {
						log.debug("applying thesaurus values for ".concat(div.getId()));
						
						List<ThesaurusValue> values = 
							contexte.getThesaurusValuesForChampEntiteId(Integer.valueOf((String) div.getAttribute("champId")));
						Collections.sort(values, Comparator.comparing(ThesaurusValue::getPosition));

						// Model
						Object lModel = PropertyUtils.getProperty(controller, (String) div.getAttribute("listmodel"));
						
						List<TKThesaurusObject> thesObjs = new ArrayList<TKThesaurusObject>();
						if (((List<TKThesaurusObject>) lModel).contains(null)) {
							thesObjs.add(null);
						}
						for (ThesaurusValue val : values) {
							thesObjs.add(((List<TKThesaurusObject>) lModel).stream()
								.filter(v -> v != null && v.getId().equals(val.getThesaurusId()))
									.findAny()
									.orElseThrow(() -> new TKException("gatsbi.thesaurus.value.notfound", val.getThesaurusValue())));
						}
						//lb.setModel(new ListModelList<TKThesaurusObject>(thesObjs));
						PropertyUtils.setProperty(controller, (String) div.getAttribute("listmodel"), thesObjs);
						log.debug("Thes values: ".concat(thesObjs.toString()).concat(" applied to ").concat(div.getId()));				
					}
				}
			}
		}
	}
	
	/**
	 * Applique spécifiquement la validation pour 'sélection obligatoire' pour les champs 
	 * de formulaires de type liste, combobox et les divs regroupant les checkboxes de 
	 * non-conformité.
	 * @param reqListboxes
	 * @param reqComboboxes
	 * @param conformeDivs
	 */
	public static void checkRequiredNonInputComponents(List<Listbox> reqListboxes, 
												List<Combobox> reqComboboxes, List<Div> conformeDivs) {

		if (reqListboxes != null) {
			for (Listbox lb : reqListboxes) {
				Clients.clearWrongValue(lb);
				if (lb.getSelectedItem() == null) {
					Clients.scrollIntoView(lb);
					throw new WrongValueException(lb, Labels.getLabel("validation.syntax.empty"));
				}
			}
		}
		
		if (reqComboboxes != null) {
			for (Combobox lb : reqComboboxes) {
				Clients.clearWrongValue(lb);
				if (lb.getSelectedItem() == null) {
					Clients.scrollIntoView(lb);
					throw new WrongValueException(lb, Labels.getLabel("validation.syntax.empty"));
				}
			}
		}
		
		if (conformeDivs != null) {
			for (Div div : conformeDivs) {
				Clients.clearWrongValue(div.getFirstChild());
				if (div.getLastChild().getChildren().stream()
					.filter(c -> c instanceof Checkbox)
					.noneMatch(c -> ((Checkbox) c).isChecked())) {
					Clients.scrollIntoView(div);
					throw new WrongValueException(div.getFirstChild(), Labels.getLabel("validation.syntax.empty"));
				}
			}
		}
	}
	
	private static Component findInputOrListboxElement(Div item) {
		// finds input element or listbox
		if (item.getLastChild() instanceof InputElement 
					|| item.getLastChild() instanceof Listbox
					|| item.getLastChild() instanceof CalendarBox) {
			return item.getLastChild();
		} else if (item.getLastChild() instanceof Div) {
			for (Component child : item.getLastChild().getChildren()) {
				if (child instanceof InputElement || child instanceof Listbox
						|| item.getLastChild() instanceof CalendarBox) {
					return  child;
				}
			}
		}
		return null;
	}
	
	
	// test only
	public static Contexte mockOneContexte() {
		   Contexte cont = new Contexte();
		   ChampEntite code = new ChampEntite();
		   code.setChampId(23);
		   code.setVisible(true);
		   code.setObligatoire(false);
		   cont.getChampEntites().add(code);
		   ChampEntite codeLabo = new ChampEntite();
		   codeLabo.setChampId(45);
		   codeLabo.setVisible(true);
		   cont.getChampEntites().add(codeLabo);
		   
		   // thes
		   ChampEntite nature = new ChampEntite();
		   nature.setChampId(24);
		   nature.setVisible(true);
		   nature.setObligatoire(true);
		   nature.setIsChampReferToThesaurus("nature");
		   ThesaurusValue ascite = new ThesaurusValue();
		   ascite.setChampId(24);
		   ascite.setPosition(2);
		   ascite.setThesaurusId(3);
		   ascite.setThesaurusValue("LIQUIDE D'ASCITE");
		   nature.getThesaurusValues().add(ascite);
		   ThesaurusValue sang = new ThesaurusValue();
		   sang.setChampId(24);
		   sang.setPosition(1);
		   sang.setThesaurusId(2);
		   sang.setThesaurusValue("SANG");
		   nature.getThesaurusValues().add(sang);
		   cont.getChampEntites().add(nature);
		   
		   ChampEntite consentType = new ChampEntite();
		   consentType.setChampId(26);
		   consentType.setVisible(true);
		   consentType.setIsChampReferToThesaurus("consentType");
		   ThesaurusValue enAttente = new ThesaurusValue();
		   enAttente.setChampId(26);
		   enAttente.setPosition(1);
		   enAttente.setThesaurusId(1);
		   enAttente.setThesaurusValue("EN ATTENTE");
		   consentType.getThesaurusValues().add(enAttente);
		   cont.getChampEntites().add(consentType);
		   
		   
		   
		   ChampEntite consentDate = new ChampEntite();
		   consentDate.setChampId(27);
		   consentDate.setVisible(true);
		   consentDate.setObligatoire(false);
		   cont.getChampEntites().add(consentDate);
		   
		  // listbox
		   ChampEntite prelType = new ChampEntite();
		   prelType.setChampId(31);
		   prelType.setVisible(true);
		   prelType.setObligatoire(false);
		   cont.getChampEntites().add(prelType);
		   
		   // multi listbox
		   ChampEntite risques = new ChampEntite();
		   risques.setChampId(249);
		   risques.setVisible(true);
		   risques.setObligatoire(false);
		   risques.setIsChampReferToThesaurus("risques");
		   ThesaurusValue hiv = new ThesaurusValue();
		   hiv.setChampId(249);
		   hiv.setPosition(1);
		   hiv.setThesaurusId(1);
		   hiv.setThesaurusValue("HIV");
		   risques.getThesaurusValues().add(hiv);
		   cont.getChampEntites().add(risques);
		   
		   // intboc
		   ChampEntite conditnbr = new ChampEntite();
		   conditnbr.setChampId(34);
		   conditnbr.setVisible(true);
		   conditnbr.setObligatoire(false);
		   cont.getChampEntites().add(conditnbr);
		   
		   // sites interm
		   // intboc
		   ChampEntite depart = new ChampEntite();
		   depart.setChampId(35);
		   depart.setVisible(true);
		   depart.setObligatoire(true);
		   cont.getChampEntites().add(depart);
		   
		   ChampEntite transporteur = new ChampEntite();
		   transporteur.setChampId(36);
		   transporteur.setVisible(true);
		   transporteur.setObligatoire(false);
		   cont.getChampEntites().add(transporteur);
		   
		   ChampEntite temp = new ChampEntite();
		   temp.setChampId(37);
		   temp.setVisible(true);
		   temp.setObligatoire(false);
		   cont.getChampEntites().add(temp);
		   		   
		   ChampEntite arrivee = new ChampEntite();
		   arrivee.setChampId(38);
		   arrivee.setVisible(true);
		   arrivee.setObligatoire(false);
		   cont.getChampEntites().add(arrivee);
		   
		   ChampEntite operateur = new ChampEntite();
		   operateur.setChampId(39);
		   operateur.setVisible(true);
		   operateur.setObligatoire(true);
		   cont.getChampEntites().add(operateur);
		   
		   ChampEntite qte = new ChampEntite();
		   qte.setChampId(40);
		   qte.setVisible(true);
		   qte.setObligatoire(false);
		   cont.getChampEntites().add(qte);
		   
		   ChampEntite ncf = new ChampEntite();
		   ncf.setChampId(256);
		   ncf.setVisible(true);
		   ncf.setObligatoire(true);
		   ncf.setIsChampReferToThesaurus("nonConformites");
		   ThesaurusValue err = new ThesaurusValue();
		   err.setChampId(256);
		   err.setPosition(1);
		   err.setThesaurusId(2);
		   err.setThesaurusValue("Erreur dossier");
		   ncf.getThesaurusValues().add(err);
		   cont.getChampEntites().add(ncf);
		   
		   ChampEntite congDepart = new ChampEntite();
		   congDepart.setChampId(267);
		   congDepart.setVisible(true);
		   congDepart.setObligatoire(true);
		   cont.getChampEntites().add(congDepart);
		   
		   ChampEntite congArrivee = new ChampEntite();
		   congArrivee.setChampId(268);
		   congArrivee.setVisible(true);
		   congArrivee.setObligatoire(true);
		   cont.getChampEntites().add(congArrivee);
		   
		   
		   cont.setSiteInter(true);
		   
		   return cont;
	   }

}
