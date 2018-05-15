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
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

public class FicheChampThesaurus extends AbstractFicheCombineController
{

   private static final long serialVersionUID = 7626936673907542566L;

   private String newValue = "";
   private String newSecondValue = "";
   private boolean booleanValue = false;
   private Object valeurThesaurus;
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
      this.path = pathToPage;
      this.creationMode = creation;
      this.valeurThesaurus = oldValue;

      presentationLabel.setValue(title);

      newValue = initValeur();
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
   public String initValeur(){
      String valeur = "";

      if(valeurThesaurus.getClass().getSimpleName().equals("Nature")){
         valeur = ((Nature) valeurThesaurus).getNature();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("PrelevementType")){
         valeur = ((PrelevementType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("EchantillonType")){
         valeur = ((EchantillonType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("EchanQualite")){
         valeur = ((EchanQualite) valeurThesaurus).getEchanQualite();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ProdType")){
         valeur = ((ProdType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ProdQualite")){
         valeur = ((ProdQualite) valeurThesaurus).getProdQualite();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ConditType")){
         valeur = ((ConditType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ConditMilieu")){
         valeur = ((ConditMilieu) valeurThesaurus).getMilieu();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ConsentType")){
         valeur = ((ConsentType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("Risque")){
         valeur = ((Risque) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("Protocole")){
         valeur = ((Protocole) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ModePrepa")){
         valeur = ((ModePrepa) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ModePrepaDerive")){
         valeur = ((ModePrepaDerive) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("CessionExamen")){
         valeur = ((CessionExamen) valeurThesaurus).getExamen();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("DestructionMotif")){
         valeur = ((DestructionMotif) valeurThesaurus).getMotif();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ProtocoleType")){
         valeur = ((ProtocoleType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("Specialite")){
         valeur = ((Specialite) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("Categorie")){
         valeur = ((Categorie) valeurThesaurus).getNom();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("ConteneurType")){
         valeur = ((ConteneurType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("EnceinteType")){
         valeur = ((EnceinteType) valeurThesaurus).getType();
      }else if(valeurThesaurus.getClass().getSimpleName().equals("NonConformite")){
         valeur = ((NonConformite) valeurThesaurus).getNom();
      }

      return valeur;
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
   public void onLaterUpdate(){
      if(!valeurThesaurus.getClass().getSimpleName().equals("NonConformite")){
         newValue = newValue.toUpperCase().trim();
      }

      try{
         // enregistrement en fonction du thésaurus
         if(valeurThesaurus.getClass().getSimpleName().equals("Nature")){
            saveNautre();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("PrelevementType")){
            savePrelevementType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("EchantillonType")){
            saveEchantillonType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("EchanQualite")){
            saveEchanQualite();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ProdType")){
            saveProdType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ProdQualite")){
            saveProdQualite();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ConditType")){
            saveConditType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ConditMilieu")){
            saveConditMilieu();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ConsentType")){
            saveConsentType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("Risque")){
            saveRisque();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("Protocole")){
            saveProtocole();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ModePrepa")){
            saveModePrepa();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ModePrepaDerive")){
            saveModePrepaDerive();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("CessionExamen")){
            saveCessionExamen();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("DestructionMotif")){
            saveDestructionMotif();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ProtocoleType")){
            saveProtocoleType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("Specialite")){
            saveSpecialite();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("Categorie")){
            saveCategorie();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("ConteneurType")){
            saveConteneurType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("EnceinteType")){
            saveEnceinteType();
         }else if(valeurThesaurus.getClass().getSimpleName().equals("NonConformite")){
            saveNonConformite();
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

   /*******************************************************/
   /************************ SAVES ************************/
   /*******************************************************/

   /**
    * Sauvegarde la modification apportée au thésaurus Nature.
    */
   public void saveNautre(){
      ((Nature) valeurThesaurus).setNature(newValue);
      if(creationMode){
         ManagerLocator.getNatureManager().createObjectManager((Nature) valeurThesaurus);
      }else{
         ManagerLocator.getNatureManager().updateObjectManager((Nature) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * PrelevementType.
    */
   public void savePrelevementType(){
      ((PrelevementType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getPrelevementTypeManager().createObjectManager((PrelevementType) valeurThesaurus);
      }else{
         ManagerLocator.getPrelevementTypeManager().updateObjectManager((PrelevementType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * EchantillonType.
    */
   public void saveEchantillonType(){
      ((EchantillonType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getEchantillonTypeManager().createObjectManager((EchantillonType) valeurThesaurus);
      }else{
         ManagerLocator.getEchantillonTypeManager().updateObjectManager((EchantillonType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * EchanQualite.
    */
   public void saveEchanQualite(){
      ((EchanQualite) valeurThesaurus).setEchanQualite(newValue);
      if(creationMode){
         ManagerLocator.getEchanQualiteManager().createObjectManager((EchanQualite) valeurThesaurus);
      }else{
         ManagerLocator.getEchanQualiteManager().updateObjectManager((EchanQualite) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ProdType.
    */
   public void saveProdType(){
      ((ProdType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getProdTypeManager().createObjectManager((ProdType) valeurThesaurus);
      }else{
         ManagerLocator.getProdTypeManager().updateObjectManager((ProdType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ProdQualite.
    */
   public void saveProdQualite(){
      ((ProdQualite) valeurThesaurus).setProdQualite(newValue);
      if(creationMode){
         ManagerLocator.getProdQualiteManager().createObjectManager((ProdQualite) valeurThesaurus);
      }else{
         ManagerLocator.getProdQualiteManager().updateObjectManager((ProdQualite) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ConditMilieu.
    */
   public void saveConditMilieu(){
      ((ConditMilieu) valeurThesaurus).setMilieu(newValue);
      if(creationMode){
         ManagerLocator.getConditMilieuManager().createObjectManager((ConditMilieu) valeurThesaurus);
      }else{
         ManagerLocator.getConditMilieuManager().updateObjectManager((ConditMilieu) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ConditType.
    */
   public void saveConditType(){
      ((ConditType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getConditTypeManager().createObjectManager((ConditType) valeurThesaurus);
      }else{
         ManagerLocator.getConditTypeManager().updateObjectManager((ConditType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ConsentType.
    */
   public void saveConsentType(){
      ((ConsentType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getConsentTypeManager().createObjectManager((ConsentType) valeurThesaurus);
      }else{
         ManagerLocator.getConsentTypeManager().updateObjectManager((ConsentType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ModePrepa.
    */
   public void saveModePrepa(){
      ((ModePrepa) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getModePrepaManager().createObjectManager((ModePrepa) valeurThesaurus);
      }else{
         ManagerLocator.getModePrepaManager().updateObjectManager((ModePrepa) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ModePrepaDerive.
    */
   public void saveModePrepaDerive(){
      ((ModePrepaDerive) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getModePrepaDeriveManager().createObjectManager((ModePrepaDerive) valeurThesaurus);
      }else{
         ManagerLocator.getModePrepaDeriveManager().updateObjectManager((ModePrepaDerive) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * CessionExamen.
    */
   public void saveCessionExamen(){
      ((CessionExamen) valeurThesaurus).setExamen(newValue);
      if(creationMode){
         ManagerLocator.getCessionExamenManager().createObjectManager((CessionExamen) valeurThesaurus);
      }else{
         ManagerLocator.getCessionExamenManager().updateObjectManager((CessionExamen) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * DestructionMotif.
    */
   public void saveDestructionMotif(){
      ((DestructionMotif) valeurThesaurus).setMotif(newValue);
      if(creationMode){
         ManagerLocator.getDestructionMotifManager().createObjectManager((DestructionMotif) valeurThesaurus);
      }else{
         ManagerLocator.getDestructionMotifManager().updateObjectManager((DestructionMotif) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ProtocoleType.
    */
   public void saveProtocoleType(){
      ((ProtocoleType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getProtocoleTypeManager().createObjectManager((ProtocoleType) valeurThesaurus);
      }else{
         ManagerLocator.getProtocoleTypeManager().updateObjectManager((ProtocoleType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * Categorie.
    */
   public void saveCategorie(){
      ((Categorie) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getCategorieManager().createObjectManager((Categorie) valeurThesaurus);
      }else{
         ManagerLocator.getCategorieManager().updateObjectManager((Categorie) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * Specialite.
    */
   public void saveSpecialite(){
      ((Specialite) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getSpecialiteManager().createObjectManager((Specialite) valeurThesaurus);
      }else{
         ManagerLocator.getSpecialiteManager().updateObjectManager((Specialite) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * ConteneurType.
    */
   public void saveConteneurType(){
      ((ConteneurType) valeurThesaurus).setType(newValue);
      if(creationMode){
         ManagerLocator.getConteneurTypeManager().createObjectManager((ConteneurType) valeurThesaurus);
      }else{
         ManagerLocator.getConteneurTypeManager().updateObjectManager((ConteneurType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * Risque.
    */
   public void saveRisque(){
      ((Risque) valeurThesaurus).setNom(newValue);
      ((Risque) valeurThesaurus).setInfectieux(valueCheckbox.isChecked());
      if(creationMode){
         ManagerLocator.getRisqueManager().createObjectManager((Risque) valeurThesaurus);
      }else{
         ManagerLocator.getRisqueManager().updateObjectManager((Risque) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * EnceinteType.
    */
   public void saveEnceinteType(){
      ((EnceinteType) valeurThesaurus).setType(newValue);
      ((EnceinteType) valeurThesaurus).setPrefixe(newSecondValue);
      if(creationMode){
         ManagerLocator.getEnceinteTypeManager().createObjectManager((EnceinteType) valeurThesaurus);
      }else{
         ManagerLocator.getEnceinteTypeManager().updateObjectManager((EnceinteType) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * nonConformite.
    */
   public void saveNonConformite(){
      ((NonConformite) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getNonConformiteManager().createObjectManager((NonConformite) valeurThesaurus);
      }else{
         ManagerLocator.getNonConformiteManager().updateObjectManager((NonConformite) valeurThesaurus);
      }
   }

   /**
    * Sauvegarde la modification apportée au thésaurus 
    * Protocole.
    */
   public void saveProtocole(){
      ((Protocole) valeurThesaurus).setNom(newValue);
      if(creationMode){
         ManagerLocator.getProtocoleManager().createObjectManager((Protocole) valeurThesaurus);
      }else{
         ManagerLocator.getProtocoleManager().updateObjectManager((Protocole) valeurThesaurus);
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
