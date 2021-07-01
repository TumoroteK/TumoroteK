package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.io.IOException;
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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ThesaurusValue;

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
		// patient
		"nipDiv",
		"nomDiv",
		"prenomDiv",
		"dateNaisDiv",
		"sexeDiv",
		// maladie
		"libelleDiv",
		"codeMaladieDiv",
		// prelevement
		"codeDiv",
		"codeLaboDiv",
		"natureDiv",
		"ndaDiv",
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
		// transfert site stockage
		"congPrelDiv",
		"dateDepartDiv",
		"transporteurDiv",
		"tempTranspDiv",
		"congPrelDiv",
		"dateArriveeDiv",
		"operateurDiv",
		"quantiteDiv",
		"conformeArriveeDiv",
		"congBiothequeDiv",
		// echantillon dans l'ordre fiche recherche TODO reordonner!
		"codeEchanDiv",
		"typeEchanDiv",
		"echanQteDiv",
		"delaiCglDiv",
		"echanDateStockDiv",
		// "echanTempStockDiv",
		"crAnapathDiv",
		"codesLesDiv",
		"codesOrgDiv",
		"echanQualiteDiv",
		// "echanStatutDiv",
		"echanModePrepaDiv",
		"echanConformeTraitDiv",
		"echanConformeCessDiv",
		//derive dans l'ordre fiche recherche TODO reordonner!
		"codeDeriveDiv",
		"deriveTypeDiv",
		"codeLaboDeriveDiv",
		"deriveQualiteDiv",
		// "deriveStatutDiv",
		"deriveQteDiv",
		"deriveVolDiv",
		"deriveDateStockDiv",
		// "deriveTempStockDiv",
		"deriveConformeTraitDiv",
		"deriveConformeCessionDiv"
		
	};
	
	public GatsbiController() {
	}
	
	public static List<Div> wireBlockDivsFromMainComponent(Component main) {
		return Arrays.stream(divBlockIds)
			.map(id -> (Div) main.getFellowIfAny(id))
			.filter(d -> d != null)
			.collect(Collectors.toList());
	}
	
	public static List<Div> wireItemDivsFromMainComponent(Component main) {
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
	
	public static void hideGroupBoxIfEmpty(Component comp) {
		
		comp.setVisible(comp.getChildren()
				.stream().filter(c -> (c instanceof Div))
				.filter(c -> !c.getChildren().isEmpty())
				.anyMatch(c -> c.isVisible()));
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
	public static void appliThesaurusValues(List<Div> items, Contexte contexte, AbstractController controller) 
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
						Collections.sort(values, Comparator.comparing(ThesaurusValue::getPosition, 
								Comparator.nullsLast(Comparator.naturalOrder())));

						// Model
						log.debug("finding thesaurus values for model ".concat((String) div.getAttribute("listmodel")));
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
						
						// ListModelList conversion
						if (!((String) div.getAttribute("listmodel")).matches(".*Model")) {
							PropertyUtils.setProperty(controller, (String) div.getAttribute("listmodel"), thesObjs);
						} else { // ListModelList conversion
							PropertyUtils.setProperty(controller, 
								(String) div.getAttribute("listmodel"), new ListModelList<TKThesaurusObject>(thesObjs));
						}
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
	public static Contexte mockOneContexte() throws JsonParseException, JsonMappingException, IOException {
		  
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Contexte cont = mapper.readValue( 
				  GatsbiController.class.getResourceAsStream("contexte.json"), Contexte.class);
		   
		   return cont;
	   }

}
