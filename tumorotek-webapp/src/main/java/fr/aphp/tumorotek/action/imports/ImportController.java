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
package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateStatutPartage;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ImportController extends AbstractObjectTabController
{

   private static final long serialVersionUID = -9040454332621375258L;

   //TK-537
   private Panel templatesPartagesNonUtilisesPanel;

   private Listbox banquesBox;
   private Listbox templatesPartagesNonUtilisesBox;
   
   private List<ImportTemplateDecorator> templatesPartagesNonUtilises = new ArrayList<>();

   private List<Banque> banques = new ArrayList<>();

   private Banque selectedBanque;

   private ImportTemplateDecorator selectedTemplatePartageNonUtilise;
   //
   
   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setStaticEditMode(false);
      
      //TK-537
      setBinder(new AnnotateDataBinder(comp));
      
      initZoneImportTemplatePartageNonUtilise();
      //
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getImportTemplateManager().findByIdManager(id);
   }

   @Override
   public FicheImportTemplate getFicheCombine(){
      return ((FicheImportTemplate) this.self.getFellow("ficheImportTemplate").getFellow("fwinImportTemplate")
         .getAttributeOrFellow("fwinImportTemplate$composer", true));
   }

   @Override
   public ListeImportTemplate getListe(){
      return ((ListeImportTemplate) this.self.getFellow("westVbox").getFellow("listeImportTemplate").getFellow("lwinImportTemplate")
         .getAttributeOrFellow("lwinImportTemplate$composer", true));
   }
   
   public void initZoneImportTemplatePartageNonUtilise(){
      if(banques == null) {
         banques = new ArrayList<Banque>();
      }
      else {
         banques.clear();
      }
      
      refreshImportTemplatePartagesNonUtilises();
   }
   
   
   @Override
   public FicheAnnotation getFicheAnnotation(){
      return null;
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      return null;
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      return null;
   }
   
   //TK-537
   public void onSelect$banquesBox(){
      refreshImportTemplatePartagesNonUtilises();
   }

   public void onClick$displaySelectedImportTemplate(){
      if(selectedTemplatePartageNonUtilise != null) {
         switchToFicheAndListeMode();
   
         // on envoie le template à la fiche qui ne pourra pas être éditée donc pas besoin de cloner...
         getFicheCombine().setObject(selectedTemplatePartageNonUtilise);
         getFicheCombine().switchToStaticMode(); 
      }
   }
   
   private void refreshImportTemplatePartagesNonUtilises() {
      selectedTemplatePartageNonUtilise = null;
      List<ImportTemplate> importTemplatesPartagesNonUtilises = null;

      if(selectedBanque == null) {
         importTemplatesPartagesNonUtilises = ManagerLocator.getImportTemplateManager().findTemplateNotArchiveByStatutPartageAndPlateformeWithOrder(EImportTemplateStatutPartage.PARTAGE_ENCOURS, SessionUtils.getCurrentPlateforme());
      }
      else {
         importTemplatesPartagesNonUtilises = ManagerLocator.getImportTemplateManager().findTemplateByStatutPartageAndBanqueWithOrder(EImportTemplateStatutPartage.PARTAGE_ENCOURS, selectedBanque);         
      }

      //il faut supprimer les modèles présents dans la liste des "modèles utilisés"
      //extraction des importTemplate des importTemplateDecorator et suppression de importTemplatesPartagesNonUtilises
      List<ImportTemplate> importTemplatesUtilises =  getListe().getListObjects().stream().map(importTemplateDecorator -> importTemplateDecorator.getImportTemplate()).collect(Collectors.toList());
      importTemplatesPartagesNonUtilises.removeIf(importTemplate -> importTemplate.isPartage() && importTemplatesUtilises.contains(importTemplate)); 

      //transformation des importTemplate en importTemplateDecorator
      templatesPartagesNonUtilises = importTemplatesPartagesNonUtilises.stream().map(importTemplate -> buildImportTemplateDecorator(importTemplate)).collect(Collectors.toList());
      
      //cas de l'appel depuis doAfterCompose : 
      //on initialise la liste des banques avec celles contenues dans importTemplatePartagesNonUtilises (template de toute la plateforme)
      if(banques == null) {
         banques = new ArrayList<Banque>();
      }
      if(banques.isEmpty()) {
         for(ImportTemplate importTemplatePartageNonUtilise : importTemplatesPartagesNonUtilises) {
            Banque banque = importTemplatePartageNonUtilise.getBanque();
            if(!banques.contains(banque)) {
               banques.add(banque);
            }
         }
         Collections.sort(banques);
         
         banques.add(0,null);
         selectedBanque = banques.get(0);
         
         getBinder().loadComponent(banquesBox);
      }
   
      templatesPartagesNonUtilises.add(0, null);
      getBinder().loadComponent(templatesPartagesNonUtilisesBox);
            
   
   }
   //
   
   public Panel getTemplatesPartagesNonUtilisesPanel(){
      return templatesPartagesNonUtilisesPanel;
   }

   public List<ImportTemplateDecorator> getTemplatesPartagesNonUtilises(){
      return templatesPartagesNonUtilises;
   }

   public void setTemplatesPartagesNonUtilises(List<ImportTemplateDecorator> templatesPartagesNonUtilises){
      this.templatesPartagesNonUtilises = templatesPartagesNonUtilises;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(List<Banque> banques){
      this.banques = banques;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(Banque selectedBanque){
      this.selectedBanque = selectedBanque;
   }

   public ImportTemplateDecorator getSelectedTemplatePartageNonUtilise(){
      return selectedTemplatePartageNonUtilise;
   }

   public void setSelectedTemplatePartageNonUtilise(ImportTemplateDecorator selectedTemplatePartageNonUtilise){
      this.selectedTemplatePartageNonUtilise = selectedTemplatePartageNonUtilise;
   }
   
   public ImportTemplateDecorator buildImportTemplateDecorator(ImportTemplate importTemplate) {
      return new ImportTemplateDecorator(importTemplate, true, true);
   }
}
