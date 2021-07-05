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
package fr.aphp.tumorotek.action.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.factory.DelegateFactory;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.action.prelevement.gatsbi.GatsbiController;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public abstract class AbstractFicheModifMultiController extends AbstractFicheController
{

   private static final long serialVersionUID = 1L;

   private TKdataObject baseObject;
   private final String modifEncoursLabel = "general.modifMulti.encours";

   private List<? extends Object> objsToEdit = new ArrayList<>();

   public void setObjsToEdit(final List<? extends Object> objs){
      this.objsToEdit = objs;
   }

   public List<? extends Object> getObjsToEdit(){
      return this.objsToEdit;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // init l'objet qui recevra les modifs multiples
      setNewObject();

      winPanel.setHeight(getMainWindow().getPanelHeight() - 2 + "px");
   }

   @Override
   public TKdataObject getObject(){
      return baseObject;
   }

   public void setBaseObject(final TKdataObject obj){
      this.baseObject = obj;
   }

   /**
    * Methode qui recupere la modification spécifiée dans le composant de 
    * modification multiple.
    * L'event passe en data un objet simpleChampValue.
    * @param e
    */
   public void onGetChangeOnChamp(final Event e){

      boolean isDelegateProperty = false;
      final SimpleChampValue tmp = (SimpleChampValue) e.getData();

      TKDelegateObject<? extends TKdataObject> delegate = null;
      if(!EContexte.DEFAUT.equals(SessionUtils.getCurrentContexte()) && getObject() instanceof TKDelegetableObject) {

         delegate = DelegateFactory.getDelegate(getObject(), SessionUtils.getCurrentContexte());

         ((TKDelegetableObject)getObject()).setDelegate(delegate);

         try{
            isDelegateProperty = PropertyUtils.describe(delegate).keySet().contains(tmp.getChamp());
         }catch(Exception ex){
            log.error(ex);
         }
         
      }

      try{

         final Object beanToModify;
         if(!isDelegateProperty) {
            beanToModify = getObject();
         }
         else {
            beanToModify = delegate;
         }
         
         if(!(tmp.getValue() instanceof List)){
            PropertyUtils.setSimpleProperty(beanToModify, tmp.getChamp(), tmp.getValue());
         }else{
            PropertyUtils.setSimpleProperty(beanToModify, tmp.getChamp(), ((List<Object>) tmp.getValue()).get(0));

            if(tmp.getChamp().contains("Init")){
               PropertyUtils.setSimpleProperty(beanToModify, tmp.getChamp().replace("Init", "Unite"),
                  ((List<Object>) tmp.getValue()).get(1));
            }else{
               PropertyUtils.setSimpleProperty(beanToModify, tmp.getChamp() + "Unite", ((List<Object>) tmp.getValue()).get(1));
            }
         }

      }catch(final Exception ex){
         log.error(ex);
      }

      final StringBuffer sb = new StringBuffer();
      sb.append("[");
      if(tmp.getPrintValue() != null){
         sb.append(tmp.getPrintValue());
      }else{
         sb.append(" ");
      }
      sb.append("]");
      sb.append(" - ");

      updateLabelChanged(tmp.getChamp(), sb.toString(), false);
   }

   /**
    * Recoit l'information 'reset' du pop up de modification multiple.
    * L'event contient le champLabel spécifié pour le 'reset'.
    * @param event
    */
   public void onResetMulti(final Event event){
      final SimpleChampValue tmp = (SimpleChampValue) event.getData();
      updateLabelChanged(tmp.getChamp(), "", true);
   }

   /**
    * Met à jour le labelChanged en fonction du champ passé en 
    * paramètre.
    * @param champ
    * @param printValue valeur de la modification multiple à afficher.
    * @param true si reset demandé
    */
   public abstract void updateLabelChanged(String champ, String printValue, boolean reset);

   /**
    * Valide les modifications multiples.
    * Passe la fiche en affichage statique.
    * @param event Event : clique sur le bouton validateModifMultiple.
    * @throws Exception
    */
   public void onClick$validateModifMultiple(){
      Clients.showBusy(Labels.getLabel(modifEncoursLabel));
      Events.echoEvent("onLaterUpdateMultiple", self, null);
   }

   /**
    * Sort du contexte modification multiple.
    */
   public void onClick$revert(){
      Clients.showBusy(Labels.getLabel("general.display.wait"));
      Events.echoEvent("onLaterRevertMultiple", self, null);
   }

   public void onLaterRevertMultiple(){
      getObjectTabController().onMultiModifDone();
      // ferme wait message
      Clients.clearBusy();
   }


   public void onLaterUpdateMultiple(){
      try{
         updateMultiObjects();

         // redessine la page d'annotation pour static
         //getFicheAnnotation().drawAnnotationPanelContent(false);

         // on vérifie que l'on retrouve bien la page contenant la liste
         // des objets
         if(getObjectTabController().getListe() != null){
            // ajout du dérivé à la liste
            getObjectTabController().getListe().updateMultiObjectsGridListFromOtherPage((List<TKdataObject>) getObjsToEdit());
         }

         getObjectTabController().onMultiModifDone();

         // ferme wait message
         Clients.clearBusy();

      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         log.error(re);
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Modification multiple d'objets.
    */
   public abstract void updateMultiObjects();

   /**
    * Ordonne a la fiche Annotation de preparer les listes de 
    * valeurs à modifier, créer, supprimer.
    * @return true si une de ces listes n'est pas vide.
    */
   public boolean updateMultiAnnotationValeurs(){
      getObjectTabController().getFicheAnnotation().populateValeursActionLists(false, true);
      return (!getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate().isEmpty()
         || !getObjectTabController().getFicheAnnotation().getValeursToDelete().isEmpty());
   }

   /**
    * Méthode qui ferme la fiche : seule la liste sera visible.
    * @param event onClose de la fiche.
    */
   public void onClose$winPanel(final Event event){
      Events.getRealOrigin((ForwardEvent) event).stopPropagation();
      getObjectTabController().clearStaticFiche();
      getObjectTabController().switchToOnlyListeMode();
   }
   
	/**
	 * Cette restriction est implémentée par Gatsbi
	 * @param liste de valeurs de thesaurus initiale
	 * @param champ entite id
	 * @return liste filtrée par le contexte défini par le gestionnaire Gatsbi
	 * @since 2.3.0-gatsbi
	 */
	protected List<Object> applyAnyThesaurusRestriction(List<Object> thObjs, Integer chpId) {	
		return thObjs;
	}
	
	/**
	 * Cette mutation est implémentée par Gatsbi
	 * @param contrainte dont le flag NO_EMPTY est muté suivant le caractère obligatoire 
	 * défini par Gatsbi
	 * @param champ entite id
	 * @return contrainte mutée
	 * @since 2.3.0-gatsbi
	 */
	protected Constraint muteAnyRequiredConstraint(Constraint cstr, Integer chpId) {
		return cstr;
	}
	
	/**
	 * Cette mutation est implémentée par Gatsbi
	 * @param flag isObligatoire
	 * @param champ entite id
	 * @return flag isObligatoire muté
	 * @since 2.3.0-gatsbi
	 */
	protected boolean switchAnyRequiredFlag(Boolean flag, Integer chpId) {
		return flag;
	}
}
