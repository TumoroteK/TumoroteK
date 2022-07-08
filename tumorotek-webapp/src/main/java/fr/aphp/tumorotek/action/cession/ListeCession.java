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
package fr.aphp.tumorotek.action.cession;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Column;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.comparator.CessionsNbEchantillonsComparator;
import fr.aphp.tumorotek.action.comparator.CessionsNbProdDerivesComparator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ListeCession extends AbstractListeController2
{

   // private Log log = LogFactory.getLog(ListeCession.class);

   private static final long serialVersionUID = -7145037551036220641L;

   private List<Cession> listObjects = new ArrayList<>();

   private List<Cession> selectedObjects = new ArrayList<>();

   // Critères de recherche.
   private Radio numeroCession;

   private Radio statutCession;

   private Listbox statutBoxCession;

   private Column nbProdDerivesColumn;

   private Column nbEchantillonsColumn;

   // Variables formulaire pour les critères.
   private String searchNumero;

   private String[] statuts =
      new String[] {Labels.getLabel("Statut.VALIDEE"), Labels.getLabel("Statut.REFUSEE"), Labels.getLabel("Statut.EN_ATTENTE")};

   private static CessionRowRenderer listObjectRenderer = new CessionRowRenderer(true, false);

   private CessionsNbProdDerivesComparator comparatorDerivesAsc = new CessionsNbProdDerivesComparator(true);

   private CessionsNbProdDerivesComparator comparatorDerivesDesc = new CessionsNbProdDerivesComparator(false);

   private CessionsNbEchantillonsComparator comparatorEchantillonsAsc = new CessionsNbEchantillonsComparator(true);

   private CessionsNbEchantillonsComparator comparatorEchantillonsDesc = new CessionsNbEchantillonsComparator(false);

   public String getSearchNumero(){
      return searchNumero;
   }

   public void setSearchNumero(final String search){
      this.searchNumero = search;
   }

   public String[] getStatuts(){
      return statuts;
   }

   public void setStatuts(final String[] s){
      this.statuts = s;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      nbProdDerivesColumn.setSortAscending(comparatorDerivesAsc);
      nbProdDerivesColumn.setSortDescending(comparatorDerivesDesc);
      nbEchantillonsColumn.setSortAscending(comparatorEchantillonsAsc);
      nbEchantillonsColumn.setSortDescending(comparatorEchantillonsDesc);

      //setOnGetEventName("onGetCessionsFromSelection");
   }

   @Override
   public List<Cession> getListObjects(){
      return listObjects;
   }

   @Override
   public void setListObjects(final List<? extends TKdataObject> objs){
      this.listObjects = (List<Cession>) objs;
   }

   @Override
   public void addToListObjects(final TKdataObject obj, final Integer pos){
      if(obj != null){
         if(pos != null){
            getListObjects().add(pos.intValue(), (Cession) obj);
         }else{
            getListObjects().add((Cession) obj);
         }
      }
   }

   @Override
   public void removeObjectFromList(final TKdataObject obj){
      getListObjects().remove(obj);
   }

   @Override
   public List<Cession> getSelectedObjects(){
      return selectedObjects;
   }

   @Override
   public void setSelectedObjects(final List<? extends TKdataObject> objs){
      this.selectedObjects = (List<Cession>) objs;
   }

   @Override
   public void addToSelectedObjects(final TKdataObject obj){
      if(!getSelectedObjects().contains(obj)){
         getSelectedObjects().add((Cession) obj);
      }
   }

   @Override
   public void removeFromSelectedObjects(final TKdataObject obj){
      if(getSelectedObjects().contains(obj)){
         getSelectedObjects().remove(obj);
      }
   }

   @Override
   public TKSelectObjectRenderer<? extends TKdataObject> getListObjectsRenderer(){
      return listObjectRenderer;
   }

   @Override
   public CessionController getObjectTabController(){
      return (CessionController) super.getObjectTabController();
   }

   @Override
   public void passSelectedToList(){
      getListObjects().clear();
      getListObjects().addAll(getSelectedObjects());
   }

   @Override
   public void passListToSelected(){
      getSelectedObjects().clear();
      getSelectedObjects().addAll(getListObjects());
   }

   @Override
   public void initObjectsBox(){
      final List<Cession> cessions = ManagerLocator.getCessionManager()
         .findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope), getNbLastObjs());

      setListObjects(cessions);
      setCurrentRow(null);
      setCurrentObject(null);

      getBinder().loadAttribute(self.getFellow("objectsListGrid"), "model");
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * codeBoxCession. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onFocus$codeBoxCession(){
      numeroCession.setChecked(true);
   }

   /**
    * Méthode appelée après lors du focus sur le champ
    * statutBoxCession. Le radiobutton correspondant sera
    * automatiquement sélectionné.
    */
   public void onSelect$statutBoxCession(){
      statutCession.setChecked(true);
   }

   @Override
   public List<? extends TKdataObject> extractLastObjectsCreated(){
      return ManagerLocator.getCessionManager().findLastCreationManager(SessionUtils.getSelectedBanques(sessionScope),
         getNbLastObjs());
   }

   @Override
   public List<Integer> doFindObjects(){
      List<Integer> cessions = new ArrayList<>();

      if(dateCreation.isChecked()){
         cessions = ManagerLocator.getCessionManager().findAfterDateCreationReturnIdsManager(getSearchDateCreation(),
            SessionUtils.getSelectedBanques(sessionScope));
      }else if(numeroCession.isChecked()){
         if(searchNumero != null && !searchNumero.equals("")){
            cessions = ManagerLocator.getCessionManager().findByNumeroWithBanqueReturnIdsManager(searchNumero,
               SessionUtils.getSelectedBanques(sessionScope), true);
         }else{
            if(Messagebox.show(ObjectTypesFormatters.getLabel("message.research.message", new String[] {"Cession"}),
               Labels.getLabel("message.research.title"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.YES){
               cessions = ManagerLocator.getCessionManager()
                  .findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope));
            }
         }
      }else if(statutCession.isChecked()){
         // si la recherche se fait par le statut
         final int ind = statutBoxCession.getSelectedIndex();

         // le statut 3 correspond a une recherche sur l'état incomplet
         if(ind != 3){
            if(ind < 1){
               // l'indice 0 correspond au statut VALIDEE
               cessions = ManagerLocator.getCessionManager().findByStatutWithBanquesReturnIdsManager("VALIDEE",
                  SessionUtils.getSelectedBanques(sessionScope));
            }else{
               cessions = ManagerLocator.getCessionManager().findByStatutWithBanquesReturnIdsManager(statuts[ind],
                  SessionUtils.getSelectedBanques(sessionScope));
            }
         }
      }

      return cessions;
   }

   @Override
   public List<? extends TKdataObject> extractObjectsFromIds(final List<Integer> ids){
      if(ids != null && ids.size() > 0){
         return ManagerLocator.getCessionManager().findByIdsInListManager(ids);
      }
      return new ArrayList<Cession>();
   }

   @Override
   public void onClick$addNew(final Event event){
      getObjectTabController().switchToCreateMode(null, null, null);
   }

   /**
    * Méthode appelée pour ouvrir la page de recherche avancée.
    */
   public void onClick$findMore(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("recherche.avancee.cessions"));
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);

      openRechercheAvanceeCessionWindow(page, sb.toString(), entite, Path.getPath(self));
   }

   /**
    * Evenement lors du clique sur le nombre d'échantillons
    * Posté par {@link CessionRowRenderer#drawNbEchantillons(Cession, org.zkoss.zul.Row)}
    * @param event
    */
   public void onClickNbEchantillons(final Event event){
      final List<String> echCodes = (List<String>) event.getData();
      final List<TKAnnotableObject> echList = new ArrayList<>();
      for(final String code : echCodes){
         echList.add(ManagerLocator.getEchantillonManager().findByCodeLikeManager(code, true).get(0));
      }
      displayObjectsListData(echList);
   }

   /**
    * Evenement lors du clique sur le nombre de Produit Derives
    * Posté par {@link CessionRowRenderer#drawNbProdDerives(Cession, org.zkoss.zul.Row)}
    * @param event
    */
   public void onClickNbProdDerives(final Event event){
      final List<String> prodDeriveCodes = (List<String>) event.getData();
      final List<TKAnnotableObject> prodDerivesList = new ArrayList<>();
      for(final String code : prodDeriveCodes){
         prodDerivesList.add(ManagerLocator.getProdDeriveManager().findByCodeLikeManager(code, true).get(0));
      }
      displayObjectsListData(prodDerivesList);
   }

   /**
    * Evenement lors du clique sur le nombre d'échantillons
    * Posté par {@link CessionRowRenderer#drawNbEchantillons(Cession, org.zkoss.zul.Row)}
    * @param event
    */
   public void onClickEchantillonCode(final Event event){
      final String echCode = event.getData().toString();
      final Echantillon ech = ManagerLocator.getEchantillonManager().findByCodeLikeManager(echCode, true).get(0);
      displayObjectData(ech);
   }

   /**
    * Evenement lors du clique sur le nombre de Produit Dérivés
    * Posté par {@link CessionRowRenderer#drawNbProdDerives(Cession, org.zkoss.zul.Row)}
    * @param event
    */
   public void onClickProdDeriveCode(final Event event){
      final String prodDeriveCode = event.getData().toString();
      final ProdDerive prodDerive = ManagerLocator.getProdDeriveManager().findByCodeLikeManager(prodDeriveCode, true).get(0);
      displayObjectData(prodDerive);
   }

   /*************************************************************************/
   /************************** DROITS ***************************************/
   /*************************************************************************/
   @Override
   public void applyDroitsOnListe(){
      drawActionsButtons();

      if(sessionScope.containsKey("ToutesCollections")){
         // donne aucun droit en creation
         setCanNew(false);
      }

      super.applyDroitsOnListe();
   }

   public CessionsNbProdDerivesComparator getComparatorDerivesAsc(){
      return comparatorDerivesAsc;
   }

   public void setComparatorDerivesAsc(final CessionsNbProdDerivesComparator c){
      this.comparatorDerivesAsc = c;
   }

   public CessionsNbProdDerivesComparator getComparatorDerivesDesc(){
      return comparatorDerivesDesc;
   }

   public void setComparatorDerivesDesc(final CessionsNbProdDerivesComparator c){
      this.comparatorDerivesDesc = c;
   }

   public CessionsNbEchantillonsComparator getComparatorEchantillonsAsc(){
      return comparatorEchantillonsAsc;
   }

   public void setComparatorEchantillonsAsc(final CessionsNbEchantillonsComparator c){
      this.comparatorEchantillonsAsc = c;
   }

   public CessionsNbEchantillonsComparator getComparatorEchantillonsDesc(){
      return comparatorEchantillonsDesc;
   }

   public void setComparatorEchantillonsDesc(final CessionsNbEchantillonsComparator c){
      this.comparatorEchantillonsDesc = c;
   }

   @Override
   public void batchDelete(final List<Integer> ids, final String comment){
      ManagerLocator.getCessionManager().removeListFromIdsManager(ids, comment, SessionUtils.getLoggedUser(sessionScope));
   }
}
