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
package fr.aphp.tumorotek.action.numerotation;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstCode;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Numerotation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheNumerotation extends AbstractFicheCombineController
{

   // private Log log = LogFactory.getLog(FicheNumerotation.class);

   private static final long serialVersionUID = -48088936138017866L;

   // Labels components
   private Label collectionLabel;
   private Label entiteLabel;
   private Label codeLabel;
   private Label codeHelpLabel;
   private Label startLabel;
   private Label currentLabel;
   private Label nbChiffresLabel;
   private Label zeroFillLabel;

   // Editable Components
   private Label collectionRequired;
   private Label entiteRequired;
   private Label startRequired;
   private Label currentRequired;
   private Intbox nbChiffresBox;
   private Checkbox zeroFillBox;
   private Intbox startBox;
   private Intbox currentBox;
   private Listbox collectionBox;
   private Listbox entitesBox;
   private Textbox codePrefixeNumerotation;
   private Textbox codeSuffixeNumerotation;
   private Label xxxLabel;
   private Label codeHelpEditLabel;

   private Numerotation numerotation;
   private String codePrefixe;
   private String codeSuffixe;
   private List<Banque> banques = new ArrayList<>();
   private Banque selectedBanque;
   private List<Entite> allEntites = new ArrayList<>();
   private List<Entite> entites = new ArrayList<>();
   private Entite selectedEntite;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setDeletionMessage("message.deletion.numerotation");
      setCascadable(false);
      setFantomable(false);
      setDeletable(true);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {this.collectionLabel, this.entiteLabel, this.codeLabel, this.codeHelpLabel,
         this.startLabel, this.currentLabel, this.nbChiffresLabel, this.zeroFillLabel});

      setObjBoxsComponents(
         new Component[] {this.startBox, this.currentBox, this.nbChiffresBox, this.zeroFillBox, this.collectionBox,
            this.entitesBox, this.codePrefixeNumerotation, this.codeSuffixeNumerotation, this.xxxLabel, this.codeHelpEditLabel});

      setRequiredMarks(new Component[] {this.collectionRequired, this.entiteRequired, this.startRequired, this.currentRequired});

      drawActionsForNumerotation();

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();

   }

   @Override
   public void setObject(final TKdataObject obj){
      this.numerotation = (Numerotation) obj;

      super.setObject(numerotation);
   }

   @Override
   public void cloneObject(){
      setClone(this.numerotation.clone());
   }

   @Override
   public Numerotation getObject(){
      return this.numerotation;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public NumerotationController getObjectTabController(){
      return (NumerotationController) super.getObjectTabController();
   }

   @Override
   public void setNewObject(){
      setObject(new Numerotation());
      super.setNewObject();
   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(this.numerotation.equals(new Numerotation()));

      getBinder().loadComponent(self);
   }

   @Override
   public void switchToEditMode(){
      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      super.switchToEditMode();

      if(this.numerotation.getZeroFill() != null){
         zeroFillBox.setChecked(this.numerotation.getZeroFill());
      }

      this.startBox.setVisible(false);
      this.startLabel.setVisible(true);
      this.collectionBox.setVisible(false);
      this.collectionLabel.setVisible(true);
      this.entitesBox.setVisible(false);
      this.entiteLabel.setVisible(true);

      getBinder().loadComponent(self);

   }

   @Override
   public void switchToCreateMode(){

      super.switchToCreateMode();

      // Initialisation du mode (listes, valeurs...)
      initEditableMode();

      this.currentLabel.setVisible(true);
      this.currentBox.setVisible(false);

      zeroFillBox.setChecked(false);

      getBinder().loadComponent(self);

   }

   @Override
   public void setFocusOnElement(){
      codePrefixeNumerotation.setFocus(true);
   }

   @Override
   public void setFieldsToUpperCase(){
      if(codePrefixe != null){
         codePrefixe = codePrefixe.toUpperCase();
      }

      if(codeSuffixe != null){
         codeSuffixe = codeSuffixe.toUpperCase();
      }
   }

   @Override
   public void onClick$createC(){
      // Validations
      if(selectedBanque == null){
         throw new WrongValueException(collectionBox, Labels.getLabel("numerotation.error.banque"));
      }
      if(selectedEntite == null){
         throw new WrongValueException(entitesBox, Labels.getLabel("numerotation.error.entite"));
      }

      Clients.showBusy(Labels.getLabel("numerotation.creation.encours"));
      Events.echoEvent("onLaterCreate", self, null);
   }

   @Override
   public void onLaterCreate(){
      try{
         createNewObject();
         cloneObject();
         switchToStaticMode();
         disableToolBar(false);

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void createNewObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();
      setFieldsToUpperCase();

      this.numerotation.setZeroFill(zeroFillBox.isChecked());
      final StringBuffer sb = new StringBuffer();
      if(codePrefixe != null){
         sb.append(codePrefixe);
      }
      sb.append("[]");
      if(codeSuffixe != null){
         sb.append(codeSuffixe);
      }
      this.numerotation.setCodeFormula(sb.toString());
      this.numerotation.setCurrentIncrement(this.numerotation.getStartIncrement() - 1);

      // update de l'objet
      ManagerLocator.getNumerotationManager().createObjectManager(numerotation, selectedBanque, selectedEntite);

      if(getListeNumerotation() != null){
         // ajout de l'utilisateur à la liste
         getListeNumerotation().addToObjectList(numerotation);
      }
   }

   @Override
   public String getDeleteWaitLabel(){
      return Labels.getLabel("deletion.general.wait");
   }

   @Override
   public void onClick$addNewC(){
      switchToCreateMode();
   }

   @Override
   public void onClick$editC(){
      if(this.numerotation != null){
         switchToEditMode();
      }
   }

   @Override
   public void onClick$cancelC(){
      clearData();
   }

   @Override
   public void onClick$revertC(){
      clearConstraints();
      super.onClick$revertC();
   }

   @Override
   public void clearData(){
      clearConstraints();
      super.clearData();
   }

   @Override
   public boolean prepareDeleteObject(){
      setDeleteMessage(
         ObjectTypesFormatters.getLabel("message.deletion.message", new String[] {Labels.getLabel(getDeletionMessage())}));
      return false;
   }

   @Override
   public void removeObject(final String comments){
      ManagerLocator.getNumerotationManager().removeObjectManager(numerotation);
   }

   @Override
   public void setEmptyToNulls(){}

   /**
    * Recupere le controller du composant representant la liste associee
    * a l'entite de domaine a partir de l'evenement.
    * @param event Event
    * @return fiche ListeNumerotation
    */
   private ListeNumerotation getListeNumerotation(){
      return getObjectTabController().getListe();
   }

   @Override
   public void onClick$validateC(){
      // Validations
      if(this.numerotation.getCurrentIncrement() < this.numerotation.getStartIncrement()){
         throw new WrongValueException(currentBox, Labels.getLabel("numerotation.error.currentIncrement"));
      }

      Clients.showBusy(Labels.getLabel("numerotation.creation.encours"));
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         updateObject();
         cloneObject();
         switchToStaticMode();

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         // ferme wait message
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }

   @Override
   public void updateObject(){
      // on remplit l'utilisateur en fonction des champs nulls
      setEmptyToNulls();
      setFieldsToUpperCase();

      this.numerotation.setZeroFill(zeroFillBox.isChecked());
      final StringBuffer sb = new StringBuffer();
      if(codePrefixe != null){
         sb.append(codePrefixe);
      }
      sb.append("[]");
      if(codeSuffixe != null){
         sb.append(codeSuffixe);
      }
      this.numerotation.setCodeFormula(sb.toString());

      // update de l'objet
      ManagerLocator.getNumerotationManager().updateObjectManager(numerotation, numerotation.getBanque(),
         numerotation.getEntite());

      if(getListeNumerotation() != null){
         // ajout de l'utilisateur à la liste
         getListeNumerotation().updateObjectGridList(this.numerotation);
      }
   }

   /**
    * Filtre les entités par banque.
    * @param event Event : seléction sur la liste collectionBox.
    * @throws Exception
    */
   public void onSelect$collectionBox(final Event event) throws Exception{
      entites = new ArrayList<>();
      if(selectedBanque != null){
         Clients.clearWrongValue(collectionBox);
         final List<Entite> tmp = ManagerLocator.getNumerotationManager().findByBanqueSelectEntiteManager(selectedBanque);
         for(int i = 0; i < allEntites.size(); i++){
            if(!tmp.contains(allEntites.get(i))){
               entites.add(allEntites.get(i));
            }
         }
      }else{
         entites.addAll(allEntites);

         throw new WrongValueException(collectionBox, Labels.getLabel("numerotation.error.banque"));
      }
      entites.add(0, null);
      final ListModel<Entite> listTmp = new ListModelList<>(entites);
      entitesBox.setModel(listTmp);

      if(!entites.contains(selectedEntite)){
         selectedEntite = null;
      }

      entitesBox.setSelectedIndex(entites.indexOf(selectedEntite));
   }

   /**
    * Sélectionne l'entité.
    * @param event Event : seléction sur la liste entitesBox.
    * @throws Exception
    */
   public void onSelect$entitesBox(final Event event) throws Exception{
      final int ind = entitesBox.getSelectedIndex();
      selectedEntite = entites.get(ind);
      if(selectedEntite != null){
         Clients.clearWrongValue(entitesBox);
      }else{
         throw new WrongValueException(entitesBox, Labels.getLabel("numerotation.error.entite"));
      }
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codePrefixeNumerotation. Cette valeur sera mise en majuscules.
    */
   public void onBlur$codePrefixeNumerotation(){
      codePrefixeNumerotation.setValue(codePrefixeNumerotation.getValue().toUpperCase());
   }

   /**
    * Méthode appelée après la saisie d'une valeur dans le champ
    * codeSuffixeNumerotation. Cette valeur sera mise en majuscules.
    */
   public void onBlur$codeSuffixeNumerotation(){
      codeSuffixeNumerotation.setValue(codeSuffixeNumerotation.getValue().toUpperCase());
   }

   public void initEditableMode(){
      allEntites = new ArrayList<>();
      allEntites.add(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0));
      allEntites.add(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0));
      allEntites.add(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0));
      allEntites.add(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0));

      final Utilisateur user = SessionUtils.getLoggedUser(sessionScope);
      banques = ManagerLocator.getBanqueManager().findByUtilisateurIsAdminManager(user, SessionUtils.getPlateforme(sessionScope));
      banques.add(0, null);
      selectedBanque = numerotation.getBanque();

      entites = new ArrayList<>();
      if(selectedBanque != null){
         final List<Entite> tmp = ManagerLocator.getNumerotationManager().findByBanqueSelectEntiteManager(selectedBanque);
         for(int i = 0; i < allEntites.size(); i++){
            if(!tmp.contains(allEntites.get(i))){
               entites.add(allEntites.get(i));
            }
         }
      }else{
         entites.addAll(allEntites);
      }
      entites.add(0, null);
      final ListModel<Entite> listTmp = new ListModelList<>(entites);
      entitesBox.setModel(listTmp);
      entitesBox.setSelectedIndex(entites.indexOf(selectedEntite));

      // init du code
      if(numerotation.getCodeFormula() != null){
         if(numerotation.getCodeFormula().contains("[]")){
            codePrefixe = numerotation.getCodeFormula().substring(0, numerotation.getCodeFormula().indexOf("["));
            codeSuffixe = numerotation.getCodeFormula().substring(numerotation.getCodeFormula().indexOf("]") + 1,
               numerotation.getCodeFormula().length());
         }else{
            codePrefixe = numerotation.getCodeFormula();
         }
      }else{
         codePrefixe = "";
         codeSuffixe = "";
      }
   }

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(collectionBox);
      Clients.clearWrongValue(entitesBox);
      Clients.clearWrongValue(codePrefixeNumerotation);
      Clients.clearWrongValue(codeSuffixeNumerotation);
      Clients.clearWrongValue(startBox);
      Clients.clearWrongValue(currentBox);
      Clients.clearWrongValue(nbChiffresBox);
   }

   public String getZeroFillFormated(){
      if(this.numerotation != null){
         return ObjectTypesFormatters.booleanLitteralFormatter(this.numerotation.getZeroFill());
      }else{
         return "";
      }
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForNumerotation(){
      boolean admin = false;
      if(sessionScope.containsKey("AdminPF")){
         admin = true;
      }else if(sessionScope.containsKey("Admin")){
         admin = (Boolean) sessionScope.get("Admin");
      }

      // si l'utilisateur est admin PF => boutons cliquables
      if(admin){
         setCanNew(true);
         setCanEdit(true);
         setCanDelete(true);
      }else{
         setCanNew(false);
         setCanEdit(false);
         setCanDelete(false);
      }
   }

   public ConstCode getCodeConstraint(){
      return NumerotationConstraint.getCodeConstraint();
   }

   /*********************************************************/
   /********************** ACCESSEURS. **********************/
   /*********************************************************/

   public Numerotation getNumerotation(){
      return numerotation;
   }

   public void setNumerotation(final Numerotation n){
      this.numerotation = n;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

   public List<Entite> getAllEntites(){
      return allEntites;
   }

   public void setAllEntites(final List<Entite> allE){
      this.allEntites = allE;
   }

   public List<Entite> getEntites(){
      return entites;
   }

   public void setEntites(final List<Entite> e){
      this.entites = e;
   }

   public Entite getSelectedEntite(){
      return selectedEntite;
   }

   public void setSelectedEntite(final Entite selected){
      this.selectedEntite = selected;
   }

   public String getCodePrefixe(){
      return codePrefixe;
   }

   public void setCodePrefixe(final String codeP){
      this.codePrefixe = codeP;
   }

   public String getCodeSuffixe(){
      return codeSuffixe;
   }

   public void setCodeSuffixe(final String codeS){
      this.codeSuffixe = codeS;
   }

}
