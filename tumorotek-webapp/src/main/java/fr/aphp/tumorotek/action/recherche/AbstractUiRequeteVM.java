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
package fr.aphp.tumorotek.action.recherche;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Group;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.impl.InputElement;

import fr.aphp.tumorotek.action.recherche.historique.SearchHistory;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.ui.UiRequete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Abstract classe assurant la gestion du composant de la
 * vue permattant l'enregistrement et l'exécution des
 * UiRequetes.
 * Cette classe VM est directement appelée à l'initiation
 * de la classe AbstractRechercheController afin d'alléger
 * cette dernière des méthodes spécifiques à cette fonctionalités.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.11
 *
 */
public class AbstractUiRequeteVM
{

   private final Entite entite;
   private final ListModelList<UiRequete> uiRequetes = new ListModelList<>();
   private final ListModelList<SearchHistory> searchHistories = new ListModelList<>();

   /**
    * Constructeur peuple la liste d'UiRequetes
    * @param ut Utilisateur
    * @param et Entite
    */
   public AbstractUiRequeteVM(final Utilisateur ut, final Entite et, final List<SearchHistory> sessionHistList){

      entite = et;

      // uiRequetes.addAll(ManagerLocator.getUiRequeteManager()
      //		.findByUtilisateurAndEntiteManager(ut, et));

      if(sessionHistList != null && et != null){
         for(final SearchHistory searchHistory : sessionHistList){
            if(searchHistory.getType().contentEquals(et.getNom())){
               searchHistories.add(0, searchHistory);
            }
         }
      }
   }

   /**************************************************************************************/
   /**************************** Persistes UiRequetes ************************************/
   /**************************************************************************************/

   public ListModelList<UiRequete> getUiRequetes(){
      return uiRequetes;
   }

   /**************************************************************************************/
   /**************************** Transient UiRequetes ************************************/
   /**************************************************************************************/

   public ListModelList<SearchHistory> getSearchHistories(){
      return searchHistories;
   }

   /**
    * Créé l'historique de recherche et l'ajoute à la liste
    * Ajoute les composants d'une recherche à l'historique
    * @param usedComponents valeurs/composants utilisés hors annotations
    * @param objOps opérateurs hors annotations
    * @param usedAnnoComponents valeurs/composants utilisé en annotations
    * @param annoOps opérateurs annotations
    * @param resultsSize nb de résultats sortis
    */
   protected void createSearchHistory(final List<RechercheCompValues> usedComponents, final List<RechercheCompValues> objOps,
      final List<RechercheCompValues> usedAnnoComponents, final List<RechercheCompValues> annoOps, final int resultsSize){

      final SearchHistory searchHistory = new SearchHistory();

      searchHistory.getListSearchHistoryComponent().addAll(usedComponents);
      searchHistory.getListSearchHistoryComponent().addAll(objOps);
      searchHistory.getListAnnotationsComponent().addAll(usedAnnoComponents);
      searchHistory.getListSearchHistoryComponent().addAll(annoOps);
      searchHistory.setType(entite.getNom());
      searchHistory.setInfo(Utils.getCurrentSystemTime(), resultsSize);

      // add current search history to sessionScope
      searchHistories.add(searchHistory);
   }

   /**
    * Nettoie et peuple les composants de fiche à partir d'une 
    * sélection.
    * @param sH element searchHistory sélectionné
    * @param rechercheDiv Div racine de la fiche.
    */
   public void putSearchHistoryValues(final Div rechercheRows){

      // clean up
      for(final Component target : rechercheRows.getFellows()){
         if(target instanceof Listbox){
            ((Listbox) target).clearSelection();
         }else if(target instanceof InputElement){
            ((InputElement) target).setRawValue(null);
         }else if(target instanceof Checkbox){
            ((Checkbox) target).setChecked(false);
         }else if(target instanceof CalendarBox){
            ((CalendarBox) target).setValue(null);
         }
      }

      final SearchHistory sH = getSearchHistories().getSelection().iterator().next();
      final List<RechercheCompValues> rcvs = sH.getListSearchHistoryComponent();
      Group currGrp;
      Component target;
      for(final RechercheCompValues rechValue : rcvs){
         currGrp = (Group) rechercheRows.getFellow(rechValue.getGroupId());
         if(!currGrp.isOpen()){
            currGrp.setOpen(true);
         }
         target = currGrp.getFellow(rechValue.getCompId());

         if(target instanceof Listbox){
            if(rechValue.getSelectedIndexValue() != null && rechValue.getSelectedIndexValue() != -1){
               ((Listbox) target).setSelectedIndex(rechValue.getSelectedIndexValue());
            }else{
               ((Listbox) target).clearSelection();
            }
         }else if(target instanceof InputElement){
            if(rechValue.getTextValue() != null && !rechValue.getTextValue().equals("")){
               ((InputElement) target).setText(rechValue.getTextValue());
            }else{
               ((InputElement) target).setRawValue(null);
            }
         }else if(target instanceof Checkbox){
            if(rechValue.getCheckedValue() != null && rechValue.getCheckedValue()){
               ((Checkbox) target).setChecked(true);
            }
         }else if(target instanceof CalendarBox){
            if(rechValue.getCalendarValue() != null){
               ((CalendarBox) target).setValue(rechValue.getCalendarValue());
            }else{
               ((CalendarBox) target).setValue(null);
            }
         }else if(target instanceof Decimalbox){
            if(rechValue.getTextValue() != null && !rechValue.getTextValue().equals("")){
               ((Decimalbox) target).setText(rechValue.getTextValue());
            }else{
               ((Decimalbox) target).setRawValue(null);
            }
         }
      }
   }
}
