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
package fr.aphp.tumorotek.action.thesaurus;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.manager.context.DiagnosticManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Diagnostic;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

public class FicheChampThesaurus extends AbstractFicheCombineController
{

   private static final long serialVersionUID = 7626936673907542566L;

   private String newValue = "";
   private String newSecondValue = "";
   private boolean booleanValue = false;
   private TKThesaurusObject valeurThesaurus;
   private String path = "";
   private Label presentationLabel;
   private Textbox valueTextbox;
   private Textbox secondValueTextbox;
   private Checkbox valueCheckbox;
   private boolean creationMode = false;
   private Row rowSecondValue;
   private Row rowBooleanValue;
   private Label labelSecondValue;
   private Label labelBooleanValue;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      ((Panel) self.getFellow("winPanel")).setHeight("130px");

      getBinder().loadAll();
   }

   /**
    * Méthode intialisant le composant.
    * @param pathToPage Chemin vers la page qui demande une modif.
    */
   public void init(final String pathToPage, final Object oldValue, final Constraint constr, final Constraint constr2,
      final String title, final boolean creation){
      
      TKThesaurusObject oldValeurThesaurus = (TKThesaurusObject)oldValue;
      
      this.path = pathToPage;
      this.creationMode = creation;
      this.valeurThesaurus = oldValeurThesaurus;

      presentationLabel.setValue(title);

      newValue = valeurThesaurus.getNom();
      newSecondValue = initSecondeValeur();
      initBooleanValeur();

      // application de la contrainte
      setConstraintsToBoxes(constr, constr2);

      getBinder().loadComponent(self);
   }

   public void setConstraintsToBoxes(final Constraint constr, final Constraint constr2){
      valueTextbox.setConstraint(constr);
      secondValueTextbox.setConstraint(constr2);
   }

   /**
    * Initialise la valeur à afficher.
    */
   public String initSecondeValeur(){
      String valeur = "";

      if(valeurThesaurus.getClass().getSimpleName().equals("EnceinteType")){
         valeur = ((EnceinteType) valeurThesaurus).getPrefixe();

         rowSecondValue.setVisible(true);
         labelSecondValue.setValue(Labels.getLabel("conteneur.terminale.prefixe"));
      }

      return valeur;
   }

   /**
    * Initialise la valeur à afficher.
    */
   public void initBooleanValeur(){
      boolean valeur = false;

      if(valeurThesaurus.getClass().getSimpleName().equals("Risque")){
         rowBooleanValue.setVisible(true);
         labelBooleanValue.setValue(Labels.getLabel("Champ.Prelevement.Risque"));

         if(((Risque) valeurThesaurus).getInfectieux() != null){
            valeur = ((Risque) valeurThesaurus).getInfectieux();
         }

         valueCheckbox.setChecked(valeur);
      }
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * valueTextbox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$valueTextbox(){
      if(!valeurThesaurus.getClass().getSimpleName().equals("NonConformite")){
         valueTextbox.setValue(valueTextbox.getValue().toUpperCase().trim());
      }
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * secondValueTextbox. Cette valeur sera mise en majuscules.
    */
   public void onBlur$secondValueTextbox(){
      secondValueTextbox.setValue(secondValueTextbox.getValue().toUpperCase().trim());
   }

   /**
    * Méthode appelée lors du clic sur la touche ENTREE => validation.
    */
   @Override
   public void onOK(){
      valueTextbox.setValue(valueTextbox.getValue().toUpperCase().trim());
      secondValueTextbox.setValue(secondValueTextbox.getValue().toUpperCase().trim());
      onClick$validate();
   }

   public void onClick$validate(){
      Clients.showBusy(Labels.getLabel("thesaurus.enregistrement.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   /**
    * Sauvegarde du thésaurus.
    */
   @SuppressWarnings("unchecked")
   public void onLaterUpdate(){
      if(!valeurThesaurus.getClass().getSimpleName().equals("NonConformite")){
         newValue = newValue.toUpperCase().trim();
      }

      valeurThesaurus.setNom(newValue);
      
      //Alimentation des champs supplémentaires
      if(valeurThesaurus instanceof Risque) {
         ((Risque) valeurThesaurus).setInfectieux(valueCheckbox.isChecked());
      } else if (valeurThesaurus instanceof EnceinteType) {
    	  newSecondValue = newSecondValue.toUpperCase().trim();
    	 ((EnceinteType) valeurThesaurus).setPrefixe(newSecondValue);
      }
      
      try{
         
         TKThesaurusManager<? extends TKThesaurusObject> manager = ManagerLocator.getThesaurusManager(valeurThesaurus.getClass());
         
         if(creationMode) {
            ((TKThesaurusManager<TKThesaurusObject>)manager).createObjectManager(valeurThesaurus);
         }
         else {
            ((TKThesaurusManager<TKThesaurusObject>)manager).updateObjectManager(valeurThesaurus);
         }

         // ferme wait message
         Clients.clearBusy();

         // detach event
         detachWindow();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
      
   }

   /**
    * Méthode qui ferme la modale et appelle la méthode 
    * onGetSaveDoneOnValue de la FicheThesaurus.
    */
   public void detachWindow(){
      if(Path.getComponent(path) != null){
         Events.postEvent(new Event("onGetSaveDoneOnValue", Path.getComponent(path), valeurThesaurus));
      }
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Méthode appelée lors du clic sur le bouton revert : on ferme
    * la fenêtre.
    */
   public void onClick$revert(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * Diagnostic.
    */
   public void saveDiagnostic(){
      ((Diagnostic) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getManager(DiagnosticManager.class).createObjectManager((Diagnostic) valeurThesaurus);
      }else{
         ManagerLocator.getManager(DiagnosticManager.class).updateObjectManager((Diagnostic) valeurThesaurus);
      }
   }

   public String getNewValue(){
      return newValue;
   }

   public void setNewValue(final String newV){
      this.newValue = newV;
   }

   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$deleteC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   public boolean isCreationMode(){
      return creationMode;
   }

   public void setCreationMode(final boolean creationM){
      this.creationMode = creationM;
   }

   public Object getValeurThesaurus(){
      return valeurThesaurus;
   }

   public void setValeurThesaurus(final TKThesaurusObject valeurTh){
      this.valeurThesaurus = valeurTh;
   }

   public String getNewSecondValue(){
      return newSecondValue;
   }

   public void setNewSecondValue(final String nValue){
      this.newSecondValue = nValue;
   }

   public boolean isBooleanValue(){
      return booleanValue;
   }

   public void setBooleanValue(final boolean bValue){
      this.booleanValue = bValue;
   }

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}
}
