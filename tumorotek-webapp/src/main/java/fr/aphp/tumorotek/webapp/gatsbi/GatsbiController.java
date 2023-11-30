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
package fr.aphp.tumorotek.webapp.gatsbi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneEditableGrid;
import fr.aphp.tumorotek.action.constraints.TumoTextConstraint;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.echantillon.FicheEchantillonEdit.ConstQuantite;
import fr.aphp.tumorotek.action.echantillon.FicheEchantillonEdit.ConstQuantiteInit;
import fr.aphp.tumorotek.action.imports.ImportColonneDecorator;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;
import fr.aphp.tumorotek.model.contexte.gatsbi.Parametrage;
import fr.aphp.tumorotek.model.contexte.gatsbi.ThesaurusValue;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ContexteDTO;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ParametrageDTO;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.ParametrageValueDTO;
import fr.aphp.tumorotek.webapp.gatsbi.client.json.SchemaVisitesDTO;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class GatsbiController
{

   private static final Log log = LogFactory.getLog(GatsbiController.class);

   private static final Map<ContexteType, String[]> divBlockIds = new HashMap<ContexteType, String[]>()
   {
      private static final long serialVersionUID = 1L;
      {
         put(ContexteType.PATIENT, new String[] {"patientBlockDiv"});
         
         put(ContexteType.PRELEVEMENT, new String[] {"identifiantBlockDiv", "patientBlockDiv", "infoPrelBlockDiv",
            "conditBlockDiv", "consentBlockDiv", "departBlockDiv", "arriveeBlockDiv"});

         put(ContexteType.ECHANTILLON, new String[] {"identifiantBlockDiv", "infoEchanBlockDiv", "anapathInfosBlockDiv"});
      }
   };

   private static final Map<ContexteType, String[]> divItemIds = new HashMap<ContexteType, String[]>()
   {
      private static final long serialVersionUID = 1L;
      {
         // patient
         put(ContexteType.PATIENT, new String[] {"nipDiv", "nomDiv", "nomNaissanceDiv", "prenomDiv", "sexeDiv", 
            "dateNaissanceDiv", "paysNaissanceDiv", "villeNaissanceDiv", "etatDiv", "dateEtatDiv", "dateDecesDiv",
            "patientMedecinsDiv"});
            // "libelleDiv", "codeMaladieDiv"});
      // maladie
         put(ContexteType.MALADIE, new String[] {"codeDiv", "dateDiagnosticDiv", "visiteMedecinsDiv"});
         // prelevement
         put(ContexteType.PRELEVEMENT,
            new String[] {"codeDiv", "codeLaboDiv", "natureDiv", 
               "ndaDiv", 
               "datePrelDiv", "typeDiv", "sterileDiv", "risquesDiv",
               "etabPreleveurDiv", "servicePreleveurDiv", "preleveurDiv", "conditTypeDiv", "conditNbrDiv", "conditMilieuDiv",
               "consentTypeDiv", "consentDateDiv",
               // transfert site stockage
               "dateDepartDiv", "transporteurDiv", "tempTranspDiv", "congPrelDiv", "dateArriveeDiv", "operateurDiv",
               "quantiteDiv", "conformeArriveeDiv", "congBiothequeDiv"});
         // echantillon dans l'ordre fiche recherche
         put(ContexteType.ECHANTILLON,
            new String[] {"codeEchanDiv", "echantillonTypeDiv", "echanQteDiv", "echanModePrepaDiv", "echanSterileDiv",
               "echanRisquesDiv", "echanDateStockDiv", "delaiCglDiv", "echanOperateurDiv", "echanQualiteDiv", "echanStatutDiv",
               "echanEmplacementDiv", "echanTempStockDiv", "conformeCessionDiv", "conformeTraitementDiv", "crAnapathDiv",
               "tumoralDiv", "cOrganesDiv", "lateraliteDiv", "cMorphosDiv"});
         // derive dans l'ordre fiche recherche TODO reordonner!
         put(ContexteType.PROD_DERIVE, new String[] {"codeDeriveDiv", "deriveTypeDiv", "codeLaboDeriveDiv", "deriveQualiteDiv",
            // "deriveStatutDiv",
            "deriveQteDiv", "deriveVolDiv", "deriveDateStockDiv",
            // "deriveTempStockDiv",
            "deriveConformeTraitDiv", "deriveConformeCessionDiv"});
      }
   };

   public GatsbiController(){}

   /**
    * Récupére tous les ids des champs à ne plus afficher.
    * Surcharge l'appel de contexte.getHiddenChampEntiteIds afin d'ajouter 
    * des dépendances spécifiques par entité entre certains champs.
    * @param contexte 
    * @return liste ids
    */
   public static List<Integer> getHiddenChampEntiteIdsForContexte(Contexte c){
      List<Integer> hiddenIds = new ArrayList<Integer>();
      if(c != null){
         hiddenIds.addAll(c.getHiddenChampEntiteIds());
      }
      
      // correctif spécifique champs associés
      addAssociatedChampEntite(c, hiddenIds);

      return hiddenIds;
   }  
      
   /**
    * Récupére tous les ids des champs obligatoires.
    * @param contexte 
    * @return liste ids
    */
   public static List<Integer> getRequiredChampEntiteIdsForContexte(Contexte c){
      List<Integer> requiredIds = new ArrayList<Integer>();
      if(c != null){
         requiredIds.addAll(c.getRequiredChampEntiteIds());
      }
      
      // correctif spécifique champs associés
      addAssociatedChampEntite(c, requiredIds);

      return requiredIds;
   }
   
   /**
    * Ajoute les champs entites associés au comportement hidden/required 
    * si nécessaire afin d'éviter toute incohérence
    * @param c contexte
    * @param hiddenIds
    */
   private static void addAssociatedChampEntite(Contexte c, List<Integer> ids){
      // prelevement: nonconfs
      if (c.getContexteType().equals(ContexteType.PRELEVEMENT)) {
         // ncfs arrive
         if (ids.contains(256)) {
            if (!ids.contains(257)) ids.add(257); // raisons   
         } else {
            ids.remove(Integer.valueOf(257));
         }
      }
      
      // echantillon: quantite et nonconfs
      if (c.getContexteType().equals(ContexteType.ECHANTILLON)) {
         // quantite
         if (ids.contains(61)) {
            if (!ids.contains(62)) ids.add(62); // quantite_init    
            if (!ids.contains(63)) ids.add(63); // quantite_unite    
         } else {
            ids.remove(Integer.valueOf(62));
            ids.remove(Integer.valueOf(63));
         }
         
         // ncfs traitement
         if (ids.contains(243)) {
            if (!ids.contains(261)) ids.add(261); // raisons   
         } else {
            ids.remove(Integer.valueOf(261));
         }
         
         // ncfs cession
         if (ids.contains(244)) {
            if (!ids.contains(262)) ids.add(262); // raisons   
         } else {
            ids.remove(Integer.valueOf(262));
         }
      }
   }

   public static List<Div> wireBlockDivsFromMainComponent(ContexteType type, Component main){
      if(divBlockIds.containsKey(type)){
         return Arrays.stream(divBlockIds.get(type)).map(id -> (Div) main.getFellowIfAny(id)).filter(d -> d != null)
            .collect(Collectors.toList());
      }
      return new ArrayList<Div>();
   }

   public static List<Div> wireItemDivsFromMainComponent(ContexteType type, Component main){
      if(divItemIds.containsKey(type)){
         return Arrays.stream(divItemIds.get(type)).map(id -> (Div) main.getFellowIfAny(id)).filter(d -> d != null)
            .collect(Collectors.toList());
      }
      return new ArrayList<Div>();
   }

   public static List<Div> wireItemDivsFromMainComponentByIds(Component main, String... ids){
      if(ids.length > 0){
         return Arrays.stream(ids).map(id -> (Div) main.getFellowIfAny(id)).filter(d -> d != null).collect(Collectors.toList());
      }
      return new ArrayList<Div>();
   }

   public static void showOrhideItems(List<Div> items, List<Div> blocks, Contexte c){
      log.debug("showing or hiding items");
      if(items != null && c != null){

         List<Integer> hidden = getHiddenChampEntiteIdsForContexte(c);

         for(Div div : items){
            div.setVisible(
               !div.hasAttribute("champId") || !hidden.contains(Integer.valueOf((String) div.getAttribute("champId"))));
            log.debug((div.isVisible() ? "showing " : "hiding ").concat(div.getId()));
         }
      }

      log.debug("showing or hiding blocks");
      if(blocks != null){
         for(Div div : blocks){
            div.setVisible(div.hasFellow(div.getId().concat("Container"))
               && div.getFellow(div.getId().concat("Container")).getChildren().stream().anyMatch(d -> d.isVisible()));

            log.debug((div.isVisible() ? "showing " : "hiding ").concat(div.getId()));
         }
      }
   }

   public static void hideGroupBoxIfEmpty(Component comp){

      comp.setVisible(comp.getChildren().stream().filter(c -> (c instanceof Div)).filter(c -> !c.getChildren().isEmpty())
         .anyMatch(c -> c.isVisible()));
   }

   public static void switchItemsRequiredOrNot(List<Div> items, Contexte c, List<Listbox> lboxes, List<Combobox> cboxes,
      List<Div> reqDivs){
      log.debug("switch items required or not");
      if(items != null && c != null){

         List<Integer> required = getRequiredChampEntiteIdsForContexte(c);

         if(!required.isEmpty()){
            boolean isReq;
            for(Div div : items){
               isReq =
                  (div.hasAttribute("champId") && required.contains(Integer.valueOf((String) div.getAttribute("champId"))));
               
               Component formElement = null;
               formElement = findInputOrListboxElement(div);
               
               if(div.isVisible() && isReq){ // required
                  div.setSclass(div.getSclass().concat(" item-required"));

                  if(formElement != null){
                     if(formElement instanceof Combobox){
                        cboxes.add(((Combobox) formElement));
                     }else if(formElement instanceof Listbox){
                        lboxes.add(((Listbox) formElement));
                     }else if(formElement instanceof InputElement){
                        ((InputElement) formElement)
                           .setConstraint(muteConstraintFromContexte(((InputElement) formElement).getConstraint(), true));
                     }else if(formElement instanceof CalendarBox){
                        ((CalendarBox) formElement).setConstraint("no empty");
                     }
                  }else if(div.getId().startsWith("conforme") || div.getId().startsWith("crAnapath")
                     || div.getId().equals("cOrganesDiv") || div.getId().equals("cMorphosDiv")
                     || div.getId().equals("echanQteDiv")){
                     reqDivs.add(div);
                  }
               } else if(formElement instanceof InputElement) { // non required ou invisible -> modifie la contrainte 'non required' sur un champ input
                  ((InputElement) formElement)
                     .setConstraint(muteConstraintFromContexte(((InputElement) formElement).getConstraint(), false));
               }

               log.debug("switching ".concat(div.getId()).concat(" ").concat(!isReq ? "not" : "").concat("required"));
            }
         }
      }
   }

   /**
    * Applique la methode 'showOrHide' sur un set entier de contextes Patient,
    * Maladie, Prélèvement, Echantillon, Dérivé
    * 
    * @param itemDivs
    * @param blockDivs
    * @param contextes
    */
   public static void showOrhideItems(List<Div> itemDivs, List<Div> blockDivs, List<Contexte> contextes){
      for(Contexte c : contextes){
         if(c != null){
            showOrhideItems(itemDivs, blockDivs, c);
         }
      }
   }

   @SuppressWarnings("unchecked")
   public static void appliThesaurusValues(List<Div> items, Contexte contexte, AbstractController controller)
      throws TKException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
      log.debug("applying thesaurus values");
      if(items != null && contexte != null){

         List<Integer> thesaurii = contexte.getThesaurusChampEntiteIds();

         if(!thesaurii.isEmpty()){
            for(Div div : items){
               if(div.hasAttribute("listmodel")) {
                  
                  log.debug("applying thesaurus values for ".concat(div.getId()));
                  
                  // Model
                  log.debug("finding thesaurus values for model ".concat((String) div.getAttribute("listmodel")));
                  List<TKThesaurusObject> lModel =
                     (List<TKThesaurusObject>) PropertyUtils.getProperty(controller, (String) div.getAttribute("listmodel"));
                  
                  // le champ peut être un thésaurus (champId) ex: nature
                  // ou un thésaurus lié (thesChampId) ex: quantité unité, dans ce cas le thésaurus doit être 'visible'
                  // sinon toutes les valeurs sont affichées par défaut
                  Integer thesaurusChampId = null;
                  if (thesaurii.contains(Integer.valueOf((String) div.getAttribute("champId")))) {
                     thesaurusChampId = Integer.valueOf((String) div.getAttribute("champId"));
                  } else if (div.hasAttribute("thesChampId") 
                        && thesaurii.contains(Integer.valueOf((String) div.getAttribute("thesChampId")))) {
                     thesaurusChampId = Integer.valueOf((String) div.getAttribute("thesChampId"));
                  }

                  List<TKThesaurusObject> thesObjs =  filterExistingListModel(contexte, lModel, thesaurusChampId);

                  // ListModelList conversion
                  //Dans la recherche avancée, les listes à choix multiple (risques et conformité) ont été faites avec un ListModelList (nom de l'attribut suffixé par Model)
                  //Dans le reste (fiche edition), les listes sont des List. Dans ce cas l'attribut n'a pas de suffixe d'où la règle ci-dessous
                  if(!((String) div.getAttribute("listmodel")).matches(".*Model")){
                     PropertyUtils.setProperty(controller, (String) div.getAttribute("listmodel"), thesObjs);
                  }else{ // ListModelList conversion -> Recherche avancée prélèvement Risques
                     PropertyUtils.setProperty(controller, (String) div.getAttribute("listmodel"),
                        new ListModelList<Object>(thesObjs));
                  }
                  log.debug("Thes values: ".concat(thesObjs.toString()).concat(" applied to ").concat(div.getId()));
               }
            }
         }
      }
   }

   /**
    * Applique la methode 'appliThesaurusValues' sur un set entier de contextes
    * Patient, Maladie, Prélèvement, Echantillon, Dérivé
    * 
    * @param items
    * @param contextes
    * @param controller
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    */
   public static void appliThesaurusValues(List<Div> items, List<Contexte> contextes, AbstractController controller)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
      for(Contexte c : contextes){
         if(c != null){
            appliThesaurusValues(items, c, controller);
         }
      }
   }

   public static <T> List<T> filterExistingListModel(Contexte contexte, List<T> lModel, Integer chpId) throws TKException{

      List<ThesaurusValue> values = contexte.getThesaurusValuesForChampEntiteId(chpId);
      
      Collections.sort(values,
         Comparator.comparing(ThesaurusValue::getPosition, Comparator.nullsLast(Comparator.naturalOrder())));

      List<T> thesObjs = new ArrayList<T>();
      
      if (!values.isEmpty()) {  
         
         if(lModel.contains(null)){
            thesObjs.add(null);
         }
         
         for(ThesaurusValue val : values){
            thesObjs.add(lModel.stream().filter(
               v -> v != null && (((v instanceof TKThesaurusObject) && ((TKThesaurusObject) v).getId().equals(val.getThesaurusId()))
                  || ((v instanceof Unite) && ((Unite) v).getNom().equals(val.getThesaurusValue()))))
               .findAny().orElseThrow(() -> new TKException("gatsbi.thesaurus.value.notfound", val.getThesaurusValue())));
         }
      } else { // adds all thesaurus values
         //une évolution a été faite côté Gatsbi qui renvoie désormais toutes les valeurs du thesaurus si 
         //l'utilisateur n'en a saisi aucun (TG-148) => on ne passe plus ici que si il n'y a aucune valeur pour la plateforme
         //dans ce cas lModel est vide.
         thesObjs.addAll(lModel);
      }

      return thesObjs;
   }

   /**
    * Applique spécifiquement la validation pour 'sélection obligatoire' pour les
    * champs de formulaires de type liste, combobox et les divs regroupant les
    * checkboxes de non-conformité.
    * 
    * @param reqListboxes
    * @param reqComboboxes
    * @param conformeDivs
    */
   public static void checkRequiredNonInputComponents(List<Listbox> reqListboxes, List<Combobox> reqComboboxes,
      List<Div> reqDivs){

      if(reqListboxes != null){
         for(Listbox lb : reqListboxes){
            Clients.clearWrongValue(lb);
            if(lb.getSelectedItem() == null){
               Clients.scrollIntoView(lb);
               throw new WrongValueException(lb, Labels.getLabel("validation.syntax.empty"));
            }
         }
      }

      if(reqComboboxes != null){
         for(Combobox lb : reqComboboxes){
            Clients.clearWrongValue(lb);
            if(lb.getSelectedItem() == null){
               Clients.scrollIntoView(lb);
               throw new WrongValueException(lb, Labels.getLabel("validation.syntax.empty"));
            }
         }
      }

      if(reqDivs != null){
         for(Div div : reqDivs){
            Clients.clearWrongValue(div.getLastChild());

            boolean throwEmptyError = false;
            Component throwEmptyErrorComponent = null;

            if(div.getId().startsWith("conforme")){ // non conformite
               if(div.getLastChild().getChildren().stream().filter(c -> c instanceof Checkbox)
                  .noneMatch(c -> ((Checkbox) c).isChecked())){
                  throwEmptyError = true;
               }
            }else if(div.getId().equals("crAnapathDiv")){ // cr anapath
               Textbox tb =
                  (Textbox) div.getLastChild().getChildren().stream().filter(c -> c instanceof Textbox).findFirst().orElse(null);

               if(StringUtils.isEmpty(tb.getValue())){
                  throwEmptyError = true;
               }
            }else if(Arrays.asList("cOrganesDiv", "cMorphosDiv").contains(div.getId())){ // codes
               CodeAssigneEditableGrid controller = (CodeAssigneEditableGrid) div.getLastChild().getFirstChild()
                  .getFellow("codesAssitGridDiv").getAttributeOrFellow("codesAssitGridDiv$composer", true);
               if(controller.getObjs().isEmpty()){
                  throwEmptyError = true;
               }
            }else if("echanQteDiv".contains(div.getId())){ // echan quantite
               Decimalbox tb = (Decimalbox) div.getLastChild().getChildren().stream().filter(c -> c instanceof Decimalbox)
                  .findFirst().orElse(null);

               if(tb.getValue() == null){
                  throwEmptyError = true;
                  throwEmptyErrorComponent = tb;
               }else{
                  Listbox unitesBox = (Listbox) div.getLastChild().getChildren().stream().filter(c -> c instanceof Listbox)
                     .findFirst().orElse(null);

                  if(unitesBox.getSelectedCount() == 0){
                     throwEmptyError = true;
                     throwEmptyErrorComponent = unitesBox;
                  }
               }
            }

            if(throwEmptyError){
               Clients.scrollIntoView(div);
               throw new WrongValueException(throwEmptyErrorComponent == null ? div.getLastChild() : throwEmptyErrorComponent,
                  Labels.getLabel("validation.syntax.empty"));
            }
         }
      }
   }

   private static Component findInputOrListboxElement(Div item){
      // finds input element or listbox
      if(item.getLastChild() instanceof InputElement || item.getLastChild() instanceof Listbox
         || item.getLastChild() instanceof CalendarBox){
         return item.getLastChild();
      }else if(item.getLastChild() instanceof Div && (((Div) item.getLastChild()).getSclass() == null
         || !((Div) item.getLastChild()).getSclass().contains("skip-required"))){
         for(Component child : item.getLastChild().getChildren()){
            if(child instanceof InputElement || child instanceof Listbox || item.getLastChild() instanceof CalendarBox){
               return child;
            }
         }
      }
      return null;
   }

   public static Constraint muteConstraintFromContexte(Constraint constraint, boolean required){
      if(constraint != null){
         log.debug("Constraint " + constraint.toString() + " being switched to " + (required ? "" : "not ") + "required");

         if(constraint instanceof TumoTextConstraint){
            ((TumoTextConstraint) constraint).setNullable(!required);
         }else if(constraint instanceof ConstQuantite){ // echantillon
            ((ConstQuantite) constraint).setNullable(!required);
         }else if(constraint instanceof ConstQuantiteInit){ // echantillon
            ((ConstQuantiteInit) constraint).setNullable(!required);
         }else if(constraint instanceof SimpleConstraint){
            int flags = ((SimpleConstraint) constraint).getFlags();
            if(required){
               flags = flags | SimpleConstraint.NO_EMPTY;
            } // else not possible to remove required flag with bitwise operator ???

            constraint = new SimpleConstraint(flags);
         }else{
            throw new RuntimeException("TK custom constraint... should be parametrized to be switched to non emtpy");
         }
      }else if(required){ // add required constraint
         constraint = new SimpleConstraint("no empty");
      }

      return constraint;
   }

   // Interceptions
   // imports
   // recherche > affichages
   public static List<ChampEntite> findByEntiteImportAndIsNullableManager(final Entite entite, final Boolean canImport,
      final Boolean isNullable){

      final List<ChampEntite> chpE = new ArrayList<ChampEntite>();

      final Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(entite.getEntiteId());

      if(contexte == null){ // TK defaut
         if(isNullable != null){
            chpE
               .addAll(ManagerLocator.getChampEntiteManager().findByEntiteImportAndIsNullableManager(entite, canImport, isNullable));
         } else {
            chpE
               .addAll(ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entite, true));
         }
         
         // removes patient identifiant !!
         chpE.removeIf(c -> c.getId().equals(272));       
      }else{ // surcharge gatsbi
         chpE.addAll(ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entite, canImport));

         // filtre les champs visibles suivant le contexte Gatsbi
         List<Integer> hiddenIds = getHiddenChampEntiteIdsForContexte(contexte);
         chpE.removeIf(chp -> hiddenIds.contains(chp.getId()));

         if(isNullable != null){
            // filtre les champs obligatoires suivant le contexte Gatsbi
            // surcharge la propriété isNullable de manière non persistante
            List<Integer> reqIds = getRequiredChampEntiteIdsForContexte(contexte);
            if(isNullable){ // champs non obligatoires
               chpE.removeIf(chp -> reqIds.contains(chp.getId()));
               chpE.stream().forEach(chp -> chp.setNullable(true));
            }else{ // obligatoires
               chpE.removeIf(chp -> !reqIds.contains(chp.getId()));
               chpE.stream().forEach(chp -> chp.setNullable(false));
            }
         }
      }

      Collections.sort(chpE, Comparator.comparing(ChampEntite::getId));

      // ajout identifiant au début
      if (contexte != null && contexte.getContexteType().equals(ContexteType.PATIENT)) {
         Optional<ChampEntite> patientIdentifiantChpOpt = 
            chpE.stream().filter(c -> c.getId().equals(272)).findFirst();
         if (patientIdentifiantChpOpt.isPresent()) {
            chpE.remove(patientIdentifiantChpOpt.get());
            chpE.add(0, patientIdentifiantChpOpt.get());
         }
      }

      return chpE;
   }


   public static List<ImportColonneDecorator> decorateImportColonnes(final List<ImportColonne> cols, final boolean isSubderive){

      List<ImportColonneDecorator> decos = ImportColonneDecorator.decorateListe(cols, isSubderive);

      // surcharge la propriété deletable suivant le contexte gastbi
      Contexte c;
      for(ImportColonneDecorator deco : decos){
         c = SessionUtils
            .getCurrentGatsbiContexteForEntiteId(deco.getColonne().getChamp().getChampEntite().getEntite().getEntiteId());
         if(c != null){
            deco.setCanDelete(
               !getRequiredChampEntiteIdsForContexte(c).contains(deco.getColonne().getChamp().getChampEntite().getId()));
            deco.setVisiteGatsbi(c.getContexteType().equals(ContexteType.MALADIE));
         }
      }
      return decos;
   }

   // test only
//   public static ContexteDTO mockOneContexteTEST() throws JsonParseException, JsonMappingException, IOException{
//
//      ObjectMapper mapper = new ObjectMapper();
//      mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//      ContexteDTO cont = mapper.readValue(GatsbiController.class.getResourceAsStream("contexte.json"), ContexteDTO.class);
//
//      return cont;
//   }
//
//   // test only
//   public static ParametrageDTO mockOneParametrageTEST(Integer pId) throws JsonParseException, JsonMappingException, IOException{
//
//      ObjectMapper mapper = new ObjectMapper();
//      mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//      return mapper.readValue(GatsbiController.class.getResourceAsStream("parametrage".concat(pId.toString()).concat(".json")),
//         ParametrageDTO.class);
//
//   }

   /**
    * Vérifie la visibilité d'un champ entité en - retrouvant le contexte - puis la
    * visibilité du champ
    * 
    * @param c ChampEntite
    * @return visibilité
    */
   public static boolean isChampEntiteVisible(ChampEntite c){
      if(c != null){
         Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(c.getEntite().getEntiteId());
         if(contexte != null){
            return !getHiddenChampEntiteIdsForContexte(contexte).contains(c.getId());
         }
      }
      return true;
   }

   //	/**
   //	 * Vérifie le caractère obligatoire d'un champ entité à partir du contexte
   //	 * 
   //	 * @param c ChampEntite
   //	 * @return obligatoire ?
   //	 */
   //	public static boolean isChampEntiteRequired(Integer eId, Integer cId, boolean isObligDefault) {
   //		if (eId != null && cId != null) {
   //			Contexte contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(eId);
   //			if (contexte != null) {
   //				return getRequiredChampEntiteIdsForContexte(contexte).contains(cId);
   //			} else {
   //				return isObligDefault;
   //			}
   //		}
   //		return false;
   //	}

   /**
    * 
    * @param bank
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    * @throws GatsbiException 
    */
   public static void doGastbiContexte(Banque... banks) throws GatsbiException{

      if(banks.length > 0){
         Etude etude = banks[0].getEtude(); // même en toutes collections, une seule étude !!

         UriComponentsBuilder etudeURIBld = UriComponentsBuilder
            .fromUriString(TkParam.GATSBI_API_URL_BASE.getValue().concat(TkParam.GATSBI_API_URL_ETUDE_PATH.getValue()));

         UriComponentsBuilder contexteURIBld = UriComponentsBuilder
            .fromUriString(TkParam.GATSBI_API_URL_BASE.getValue().concat(TkParam.GATSBI_API_URL_CONTEXTE_PATH.getValue()));

         log.debug("fetch etude from URL:" + (etudeURIBld.build(false).expand(etude.getEtudeId())).toUriString());

         try{
            RestTemplate restTemplate = new RestTemplate();
            
            // EtudeDTO etudeDTO2 =
            //   restTemplate.getForObject(etudeURIBld.build(false).expand(etude.getEtudeId()).toUri(), EtudeDTO.class);
            
            for(ContexteType cType : ContexteType.values()){
               log.debug("fetch contexte from URL:"
                  + (contexteURIBld.build(false).expand(etude.getEtudeId(), cType.getType())).toUriString());

               etude.addToContextes(
                  restTemplate.getForObject(contexteURIBld.build(false).expand(etude.getEtudeId(), cType.getType()).toUri(),
                     ContexteDTO.class).toContexte());
            }
            
            // schéma de visite: 1 par étude
            SchemaVisitesDTO schemaDTO = doGastbiSchemaVisite(etude.getEtudeId());
            if (schemaDTO != null) {
               etude.setSchemaVisites(schemaDTO.toSchemaVisites());
            }

            for(Banque bq : banks){ // applique l'étude enrichie des contextes à toutes les banques
               bq.setEtude(etude);
            }
         }catch(ResourceAccessException e){ // gatsbi inaccessible
            throw new GatsbiException("gatsbi.connexion.error");
         }catch(HttpClientErrorException e){ // étude inexistante
            throw new GatsbiException("gatsbi.resource.notfound");
         }catch(Exception e){
            throw new GatsbiException(e.getMessage());
         }
      }
   }

   /**
    * Enrichit la banque avec les contextes GATSBI, si nécessaire.
    * Si la banque à enrichir est égale à l'instance banque courante (session), renvoie cette 
    * instance car elle est déja enrichie, sinon appel webservices GATSBI pour enrichissement.
    * @param bank
    * @param sessionScp
    * @return banque enrichie des contextes GATSBI
    */
   public static Banque enrichesBanqueWithEtudeContextes(Banque bank, final Map<?, ?> sessionScp){

      if(bank != null){
         if(bank.getEtude() != null){ // gatsbi
            if(SessionUtils.getSelectedBanques(sessionScp).contains(bank)){ // shortcut
               return SessionUtils.getSelectedBanques(sessionScp).get(SessionUtils.getSelectedBanques(sessionScp).indexOf(bank)); // cette banque est déja enrichie
            }else{ // call webservice
               try{
                  doGastbiContexte(bank);
               }catch(GatsbiException e){
                  throw new RuntimeException(e);
               }
            }
         }
      }else{
         return SessionUtils.getCurrentBanque(sessionScp);
      }

      return bank;
   }

   /**
    * HTTP HEAD to GATSBI base url.
    * @return true if no exception thrown, false otherwise
    */
   public static boolean doesGatsbiRespond(){

      UriComponentsBuilder headURIBld = UriComponentsBuilder.fromUriString(TkParam.GATSBI_API_URL_BASE.getValue());

      log.debug("check if GATSBI URL HEAD responds");

      try{
         RestTemplate restTemplate = new RestTemplate();
         restTemplate.headForHeaders(headURIBld.build(false).toUri());
         return true;
      }catch(Exception e){ // gatsbi inaccessible
         log.warn(e.getMessage());
      }
      return false;
   }

   /**
    * @param parametrage id
    * @throws GatsbiException 
    */
   public static ParametrageDTO doGastbiParametrage(Integer pId) throws GatsbiException{

      try{
         UriComponentsBuilder parametrageURIBld = UriComponentsBuilder
            .fromUriString(TkParam.GATSBI_API_URL_BASE.getValue().concat(TkParam.GATSBI_API_URL_PARAMETRAGE_PATH.getValue()));

         log.debug("fetch parametrage from URL:" + (parametrageURIBld.build(false).expand(pId)).toUriString());

         RestTemplate restTemplate = new RestTemplate();
         return restTemplate.getForObject(parametrageURIBld.build(false).expand(pId).toUri(), ParametrageDTO.class);
      }catch(ResourceAccessException e){ // gatsbi inaccessible
         throw new GatsbiException("gatsbi.connexion.error");
      }catch(Exception e){
         throw new GatsbiException(e.getMessage());
      }
   }

   /**
    * @param etudeId
    * @throws GatsbiException 
    */
   public static SchemaVisitesDTO doGastbiSchemaVisite(Integer eId) throws GatsbiException{

      try{
         UriComponentsBuilder schemaVisiteURIBld = UriComponentsBuilder
            .fromUriString(TkParam.GATSBI_API_URL_BASE.getValue().concat(TkParam.GATSBI_API_URL_SCHEMAVISITES_PATH.getValue()));

         log.debug("fetch schema visites from URL:" + (schemaVisiteURIBld.build(false).expand(eId)).toUriString());

         RestTemplate restTemplate = new RestTemplate();
         return restTemplate.getForObject(schemaVisiteURIBld.build(false).expand(eId).toUri(), SchemaVisitesDTO.class);
      }catch(HttpClientErrorException he) {
         if (he.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return null; // 404 -> pas de schéma de visite défini, OK
         } else {
            throw new GatsbiException(he.getMessage());
         }
      }catch(ResourceAccessException e){ // gatsbi inaccessible
         throw new GatsbiException("gatsbi.connexion.error");
      }catch(Exception e){
         throw new GatsbiException(e.getMessage());
      }
   }
   
   /**
    * Transforme la liste des valeurs par défaut d'un paramétrage
    * 
    * @param param
    * @param banque
    * @return
    */
   public static ResultatInjection injectGatsbiObject(Contexte contexte, ParametrageDTO param, Banque banque,
      Consumer<Parametrage> validator){
      
      ResultatInjection injection = new ResultatInjection();
      
      try{
         // repose sur InjectionManager comme interfaçages
         // crée dossier externe pour le transport des données
         // values
         TKAnnotableObject tkObj = null;

         switch(contexte.getContexteType()){
            case PATIENT:
               tkObj = new Patient();
               // injection.setPatient((Patient) tkObj);
               break;
            case PRELEVEMENT:
               tkObj = new Prelevement();
               injection.setPrelevement((Prelevement) tkObj);
               break;
            case ECHANTILLON:
               tkObj = new Echantillon();
               injection.setEchantillon((Echantillon) tkObj);
               break;
            default:
               break;
         }

         tkObj.setBanque(banque);

         if(param != null){

            // apply specific validation
            if(validator != null){
               validator.accept(param.toParametrage());
            }

            BlocExterne bloc = new BlocExterne();
            ValeurExterne val;
            for(ParametrageValueDTO value : param.getParametrageValueDTOs()){
               if(!contexte.getHiddenChampEntiteIds().contains(value.getChampEntiteId())
                  && !StringUtils.isBlank(value.getDefaultValue())){
                  if(value.getThesaurusTableNom() != null && value.getThesaurusTableNom().trim().length() != 0 
                     && !contexte.getThesaurusValuesForChampEntiteId(value.getChampEntiteId()).isEmpty()){ // thesaurus value check!
                     for(String defvalue : value.getDefaultValue().split(";")){
                        if(!contexte.getThesaurusValuesForChampEntiteId(value.getChampEntiteId()).stream()
                           .anyMatch(v -> v.getThesaurusValue().equalsIgnoreCase(defvalue))){
                           throw new TKException("gatsbi.thesaurus.value.notfound", defvalue);
                        }
                     }
                  }
                  val = new ValeurExterne();
                  val.setChampEntiteId(value.getChampEntiteId());
                  val.setValeur(value.getDefaultValue());
                  bloc.getValeurs().add(val);
               }
            }

            ManagerLocator.getInjectionManager().injectBlocExterneInObject(tkObj, banque, bloc,
               new ArrayList<AnnotationValeur>());
         }

         return injection;
      }catch(TKException e){
         Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }

      return injection;
   }

   /**
    * Applique au rendement d'une page (contenu d'un onglet, formulaire), le wire des divs 
    * représentant chaque champ d'information, afin d'appliquer:
    *  - l'affichage conditionnel des champs
    *  - l'affichage conditionnel des blocks (invisible si vides)
    *  - l'application de la contrainte obligatoire oui/non en mode édition
    *  - la restriction des thesaurus en mode édition
    *  - l'affichage conditionnel des group boxes (invisible si vides)
    * @param controller
    * @param entiteId
    * @param edit
    * @param reqListboxes
    * @param reqComboboxes
    * @param reqDiv
    * @param groupsToHideWhenEmpy
    * @return
    */
   public static Contexte initWireAndDisplay(AbstractController controller, Integer entiteId, boolean edit,
      List<Listbox> reqListboxes, List<Combobox> reqComboboxes, List<Div> reqDiv, Groupbox... groupsToHideWhenEmpy){
      try{
         Contexte c = SessionUtils.getCurrentGatsbiContexteForEntiteId(entiteId);

         Div gatsbiContainer = (Div) controller.getSelfComponent().getFellow("gatsbiContainer");

         // wire
         List<Div> itemDivs = wireItemDivsFromMainComponent(c.getContexteType(), gatsbiContainer);
         List<Div> blockDivs = wireBlockDivsFromMainComponent(c.getContexteType(), gatsbiContainer);

         // display
         showOrhideItems(itemDivs, blockDivs, c);

         // required & thesaurus restriction
         if(edit){
            switchItemsRequiredOrNot(itemDivs, c, reqListboxes, reqComboboxes, reqDiv);

            appliThesaurusValues(itemDivs, c, controller);
         }

         for(Groupbox gp : groupsToHideWhenEmpy){
            hideGroupBoxIfEmpty(gp);
         }

         return c;
      }catch(Exception e){
         log.debug(e.getMessage(), e);
         Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }

      return null;
   }

   /**
    * Applique au rendement d'une page (contenu d'un onglet, formulaire), le wire des divs 
    * représentant chaque champ d'information, afin d'appliquer:
    *  - l'affichage conditionnel des champs
    *  - l'affichage conditionnel des blocks (invisible si vides)
    *  - l'application de la contrainte obligatoire oui/non en mode édition
    *  - la restriction des thesaurus en mode édition
    *  - l'affichage conditionnel des group boxes (invisible si vides)
    * @param controller
    * @param entiteId
    * @param edit
    * @param reqListboxes
    * @param reqComboboxes
    * @param reqDiv
    * @param groupsToHideWhenEmpy
    * @return
    */
   public static void initWireAndDisplayForIds(AbstractController controller, Integer entiteId, String... ids){
      try{
         Contexte c = SessionUtils.getCurrentGatsbiContexteForEntiteId(entiteId);

         Div gatsbiContainer = (Div) controller.getSelfComponent().getFellow("gatsbiContainer");

         // wire
         List<Div> itemDivs = wireItemDivsFromMainComponentByIds(gatsbiContainer, ids);

         // display
         showOrhideItems(itemDivs, null, c);

      }catch(Exception e){
         log.debug(e.getMessage(), e);
         Messagebox.show(AbstractController.handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Dessine une colonne.
    * @param grid composant parent
    * @param nameKey
    * @param width
    * @param align
    * @param child
    * @param sort
    * @param visible
    * @return composant column dessiné.
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalAccessException
    */
   public static Column addColumn(Grid grid, String nameKey, String width, String align, Component child, String sort,
      Boolean visible) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      // check box first immutable column
      Column col = new Column();
      col.setLabel(Labels.getLabel(nameKey));
      if(width != null){
         col.setWidth(width);
         col.setStyle("max-width: " + width);
      } else {
         col.setHflex("1");
         col.setStyle("min-width: 150px");
      }
      col.setAlign(align);
      if(child != null){
         child.setParent(col);
      }
      col.setSort(sort);
      col.setVisible(visible);
      col.setParent(grid.getColumns());

      return col;
   }
   
   /**
    * Dessine un listbox header.
    * @param listbox composant parent
    * @param nameKey
    * @param width
    * @param align
    * @param visible
    * @return composant column dessiné.
    */
   public static Listheader addListHeader(Listbox listbox, String nameKey, String width, String align,
      Boolean visible) {
      // check box first immutable column
      Listheader header = new Listheader();
      header.setLabel(Labels.getLabel(nameKey));
      if(width != null){
         header.setWidth(width);
      }
      header.setAlign(align);
      header.setVisible(visible);
      header.setParent(listbox.getListhead());

      return header;
   }

   // add new refactor
   public static void addNewObjectForContext(Contexte contexte, Component selfComponent, Consumer<Event> elseMethod, Event evt,
      TKdataObject parentObj){
      if(!contexte.getParametrages().isEmpty()){
         final Map<String, Object> args = new HashMap<String, Object>();
         args.put("contexte", contexte);
         args.put("parent", selfComponent);
         args.put("parentObj", parentObj);
         Executions.createComponents("/zuls/gatsbi/SelectParametrageModale.zul", null, args);
      }else{ // no parametrages
         // super.onClick$addNew(event);
         elseMethod.accept(evt);
      }
   }

   @SuppressWarnings("unchecked")
   public static void getSelectedParametrageFromSelectEvent(Contexte contexte, Banque banque,
      AbstractObjectTabController objectTabController, Consumer<Parametrage> validator, Runnable elseMethod, ForwardEvent evt)
      throws GatsbiException{

      ResultatInjection inject = null;
      if(((Map<String, Object>) evt.getOrigin().getData()).get("paramId") != null){
         ParametrageDTO parametrageDTO =
            GatsbiController.doGastbiParametrage(((Map<String, Integer>) evt.getOrigin().getData()).get("paramId"));

         inject = GatsbiController.injectGatsbiObject(contexte, parametrageDTO, banque, validator);
         
         // parent object
         if (evt != null && evt.getOrigin() != null && evt.getOrigin().getData() != null 
              && ((Map<String, Object>) evt.getOrigin().getData()).get("parentObj") != null) {
            TKdataObject parent = (TKdataObject) ((Map<String, Object>) evt.getOrigin().getData()).get("parentObj");
            if (contexte.getContexteType().equals(ContexteType.PRELEVEMENT)) {
               if (parent instanceof Prelevement) { // PrelevementController.createAnotherPrelevement 
                  inject.getPrelevement()
                     .setMaladie(((Prelevement) parent).getMaladie());
               } else if (parent instanceof Maladie) { // maladie => prelevement.switchToCreateMode 
                  inject.getPrelevement().setMaladie((Maladie)  parent);                
               }
            }
         }         
      }

      elseMethod.run();

      if(inject != null){
         Events.postEvent("onGatsbiParamSelected", objectTabController.getFicheEdit().getSelfComponent(), inject);
      }
   }

   /**
    * Récupère le contexte par défaut depuis gatsbi pour une entité
    * @param contexte type
    * @throws GatsbiException si erreur renvoyée par API GET 
    */
   public static Contexte getGastbiDefautContexteForType(ContexteType type) throws GatsbiException{

      UriComponentsBuilder contexteURIBld = UriComponentsBuilder
         .fromUriString(TkParam.GATSBI_API_URL_BASE.getValue().concat(TkParam.GATSBI_API_URL_CONTEXTE_PATH.getValue()));

      try{
         RestTemplate restTemplate = new RestTemplate();

         log.debug("fetch contexte from URL:" + (contexteURIBld.build(false).expand("defaut", 
               type.getType().toLowerCase())).toUriString());

         return restTemplate.getForObject(contexteURIBld.build(false)
            .expand("defaut", type.getType().toLowerCase()).toUri(), ContexteDTO.class)
            .toContexte();

      }catch(ResourceAccessException e){ // gatsbi inaccessible
         throw new GatsbiException("gatsbi.connexion.error");
      }catch(HttpClientErrorException e){ // étude inexistante
         throw new GatsbiException("gatsbi.resource.notfound");
      }catch(Exception e){
         throw new GatsbiException(e.getMessage());
      }
   }
   
   public static boolean isInGatsbiContexte(Banque banque) {
      return banque.getEtude() != null;
   }
}