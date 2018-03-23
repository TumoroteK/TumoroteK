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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe gérant la création et l'édition des modèles définis dans le
 * système.
 * Créée le 23/03/2011.
 * @author Pierre VENTADOUR.
 *
 */
public class FicheModeleModale extends AbstractFicheCombineController
{

   //private Log log = LogFactory.getLog(FicheModeleModale.class);

   private static final long serialVersionUID = 1L;

   // Components
   private Listbox modeleTypesBox;

   // Objets principaux
   private Modele modele;
   private Component parent;
   private List<ModeleType> modeleTypes = new ArrayList<>();
   private ModeleType selectedModeleType;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      if(winPanel != null){
         winPanel.setHeight("420px");
      }

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      getBinder().loadAll();
   }

   /**
    * Initialise la fenêtre : le parent, l'imprimante et passe en mode
    * create ou edit.
    * @param imp
    * @param comp
    */
   public void initImprimanteModale(final Modele mod, final Component comp){
      setParent(comp);
      setObject(mod);
      if(modele.getModeleId() != null){
         switchToEditMode();
      }else{
         switchToCreateMode();
      }
   }

   @Override
   public void cloneObject(){
      setClone(this.modele.clone());
   }

   @Override
   public void onClick$cancelC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$revertC(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public void onClick$createC(){
      // vérifiction qu'un type est sélectionné
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }

      Clients.showBusy(Labels.getLabel("fiche.modele.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void createNewObject(){
      setEmptyToNulls();
      modele.setIsDefault(true);
      // update de l'objet
      ManagerLocator.getModeleManager().createObjectManager(modele, SessionUtils.getPlateforme(sessionScope), selectedModeleType,
         null, null);
   }

   @Override
   public void onClick$validateC(){
      // vérifiction qu'un type est sélectionné
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }

      Clients.showBusy(Labels.getLabel("fiche.modele.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();

         // ferme wait message
         Clients.clearBusy();
         // fermeture de la fenetre + message envoyé au parent
         postValidateEvent();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Envoie un message au controller des imprimantes pour valider
    * la sauvegarde d'un modèle.
    */
   public void postValidateEvent(){
      // si le chemin d'accès à la page est correcte
      if(getParent() != null){
         // on envoie un event à cette page
         Events.postEvent(new Event("onGetModeleDone", getParent(), null));
      }

      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){

   }

   @Override
   public void setEmptyToNulls(){
      if(this.modele.getNom().equals("")){
         this.modele.setNom(null);
      }
      if(this.modele.getTexteLibre().equals("")){
         this.modele.setTexteLibre(null);
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public void updateObject(){
      setEmptyToNulls();

      modele.setIsDefault(true);
      // update de l'objet
      ManagerLocator.getModeleManager().updateObjectManager(modele, SessionUtils.getPlateforme(sessionScope), selectedModeleType,
         null, null, null, null);
   }

   @Override
   public TKdataObject getObject(){
      return this.modele;
   }

   @Override
   public void setObject(final TKdataObject obj){
      this.modele = (Modele) obj;

      super.setObject(obj);
   }

   @Override
   public void setNewObject(){
      setObject(new Modele());
   }

   @Override
   public void switchToCreateMode(){
      super.switchToCreateMode();
      modele.setIsDefault(true);

      initEditableMode();
      getBinder().loadComponent(self);
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.modele.equals(new Modele()));
   }

   @Override
   public void switchToEditMode(){
      super.switchToEditMode();

      initEditableMode();
      getBinder().loadComponent(self);
   }

   public void initEditableMode(){
      // init des modeles types
      modeleTypes = ManagerLocator.getModeleTypeManager().findAllObjectsManager();
      modeleTypes.add(0, null);

      if(this.modele.getModeleId() != null){
         selectedModeleType = this.modele.getModeleType();
      }else{
         selectedModeleType = null;
      }
   }

   public void onSelect$modeleTypesBox(){
      if(selectedModeleType == null){
         throw new WrongValueException(modeleTypesBox, Labels.getLabel("fiche.modele.error.modeleType"));
      }else{
         Clients.clearWrongValue(modeleTypesBox);
      }
   }

   public void onClick$testImpression(){
      // mock affectation pour sélectionner le modele
      final AffectationImprimante aff = new AffectationImprimante();
      aff.setModele(getModele());
      openImprimanteModeleModale(SessionUtils.getPlateforme(sessionScope), null, aff, null);
      // openEtiquetteWindow(page, true, modele, 
      //		new ArrayList<LigneEtiquette>(), null);
   }

   /**********************************************************************/
   /*********************** GETTERS **************************************/
   /**********************************************************************/

   public ConstWord getNomConstraint(){
      return ModeleConstraints.getNomConstraint();
   }

   public ConstWord getTexteLibreConstraint(){
      return ModeleConstraints.getTexteLibreConstraint();
   }

   public Modele getModele(){
      return modele;
   }

   public void setModele(final Modele m){
      this.modele = m;
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<ModeleType> getModeleTypes(){
      return modeleTypes;
   }

   public void setModeleTypes(final List<ModeleType> m){
      this.modeleTypes = m;
   }

   public ModeleType getSelectedModeleType(){
      return selectedModeleType;
   }

   public void setSelectedModeleType(final ModeleType s){
      this.selectedModeleType = s;
   }

}
