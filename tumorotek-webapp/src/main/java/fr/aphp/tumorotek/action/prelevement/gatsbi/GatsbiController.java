/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.imports.ImportColonneDecorator;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.Entite;
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
								if (formElement instanceof Combobox) {
									cboxes.add(((Combobox) formElement));
								} else if (formElement instanceof Listbox) {
									lboxes.add(((Listbox) formElement));
								} else if (formElement instanceof InputElement) {
									((InputElement) formElement)
										.setConstraint(muteConstraintFromContexte(((InputElement) formElement).getConstraint(), isReq));
								} else if (formElement instanceof CalendarBox) {
									((CalendarBox) formElement).setConstraint("no empty");
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
						
						

						// Model
						log.debug("finding thesaurus values for model ".concat((String) div.getAttribute("listmodel")));
						List<TKThesaurusObject> lModel = (List<TKThesaurusObject>) 
								PropertyUtils.getProperty(controller, (String) div.getAttribute("listmodel"));
						
						List<TKThesaurusObject> thesObjs = 
							filterExistingListModel(contexte, lModel, Integer.valueOf((String) div.getAttribute("champId")));					
						
						// ListModelList conversion
						if (!((String) div.getAttribute("listmodel")).matches(".*Model")) {
							PropertyUtils.setProperty(controller, (String) div.getAttribute("listmodel"), thesObjs);
						} else { // ListModelList conversion
							PropertyUtils.setProperty(controller, 
								(String) div.getAttribute("listmodel"), new ListModelList<Object>(thesObjs));
						}
						log.debug("Thes values: ".concat(thesObjs.toString()).concat(" applied to ").concat(div.getId()));				
					}
				}
			}
		}
	}
	
	public static <T> List<T> filterExistingListModel(Contexte contexte, List<T> lModel, Integer chpId) {
		
		List<ThesaurusValue> values = 
				contexte.getThesaurusValuesForChampEntiteId(chpId);
			Collections.sort(values, Comparator.comparing(ThesaurusValue::getPosition, 
					Comparator.nullsLast(Comparator.naturalOrder())));
		
		List<T> thesObjs = new ArrayList<T>();
		if (lModel.contains(null)) {
			thesObjs.add(null);
		}
		for (ThesaurusValue val : values) {
			thesObjs.add(lModel.stream()
				.filter(v -> v != null && (v instanceof TKThesaurusObject) 
					&& ((TKThesaurusObject) v).getId().equals(val.getThesaurusId()))
					.findAny()
					.orElseThrow(() -> new TKException("gatsbi.thesaurus.value.notfound", val.getThesaurusValue())));
		}
		
		return thesObjs;
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
	
	public static Constraint muteConstraintFromContexte(Constraint constraint, boolean required) {
		if (constraint != null) {			
			log.debug("Constraint " + constraint.toString() 
				+ " being switched to " + (required ? "" : " not ") + "required");
			
			if (constraint instanceof TumoTextConstraint) {
				((TumoTextConstraint) constraint).setNullable(!required);
			} else if (constraint instanceof SimpleConstraint) {
				int flags = ((SimpleConstraint) constraint).getFlags();
				if (required) {
					flags = flags | SimpleConstraint.NO_EMPTY;
				} // else not possible to remove required flag with bitwise operator ???
				
				constraint = new SimpleConstraint(flags);		
			} else {
				throw new RuntimeException("Constraint unknown");
			}
		} else if (required) { // add required constraint
			constraint = new SimpleConstraint("no empty");
		}
		
		return constraint;
	}
	
	// Interceptions
	// imports
	public static List<ChampEntite> 
		findByEntiteImportAndIsNullableManager(final Entite entite, final Boolean canImport, 
				final Boolean isNullable, final Contexte contexte) {
		
		final List<ChampEntite> chpE = new ArrayList<ChampEntite>();
		
		if (contexte == null) { // TK defaut
			chpE.addAll(ManagerLocator.getChampEntiteManager()
				.findByEntiteImportAndIsNullableManager(entite, canImport, isNullable));
		} else { // surcharge gatsbi
			chpE.addAll(ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entite, canImport));
		
			// filtre les champs visibles suivant le contexte Gatsbi
			List<Integer> hiddenIds = contexte.getHiddenChampEntiteIds();
			addPrelevementComplementaryIds(hiddenIds);
			chpE.removeIf(chp -> hiddenIds.contains(chp.getId()));
			
			if (isNullable != null) {
				// filtre les champs obligatoires suivant le contexte Gatsbi
				// surcharge la propriété isNullable de manière non persistante
				List<Integer> reqIds = contexte.getRequiredChampEntiteIds();
				if (isNullable) { // champs non obligatoires
					chpE.removeIf(chp -> reqIds.contains(chp.getId()));
					chpE.stream().forEach(chp -> chp.setNullable(true)); 
				} else { // obligatoires
					chpE.removeIf(chp -> !reqIds.contains(chp.getId()));
					chpE.stream().forEach(chp -> chp.setNullable(false));
				}
			}
		}
		return chpE;	
	}
	
	public static List<ImportColonneDecorator> decorateImportColonnes(final List<ImportColonne> cols,
			final boolean isSubderive, final Contexte contexte) {
		
		List<ImportColonneDecorator> decos = ImportColonneDecorator.decorateListe(cols, isSubderive);
		
		// surcharge la propriété deletable suivant le contexte gastby
		if (contexte != null) {
			decos.forEach(d -> d.setCanDelete(!contexte.isChampIdRequired(d.getColonne().getChamp().getChampEntite().getId())));
		}
		return decos;

	}
	
	// prelevement specific
	public static void addPrelevementComplementaryIds(List<Integer> ids) {
		// unite quantite
		if (ids.contains(40)) ids.add(41); // unite adds unite id
		if (ids.contains(256)) ids.add(257); // non conformite adds raisons no conf
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
