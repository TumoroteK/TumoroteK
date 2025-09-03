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
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.action.imports.strategy.ImportTemplateStrategy;
import fr.aphp.tumorotek.component.OneToManyComponent;
import fr.aphp.tumorotek.decorator.EntiteDecorator;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;

/**
 * @version 2.0.13.2
 * @author Mathieu BARTHELEMY
 */
public class EntitesAssocieesImport extends OneToManyComponent<EntiteDecoratorForOneToManyComponent>
{

   private static final long serialVersionUID = -8965931524670323917L;

   private List<EntiteDecoratorForOneToManyComponent> objects = new ArrayList<>();

   private String pathToRespond;
   
   private boolean gatsbi = false;

   //TK-538 : permet de gérer les particularités liées au modèle d'import associé à ce composant
   private ImportTemplateStrategy importTemplateStrategy;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      objLinkLabel = new Label();
      super.doAfterCompose(comp);
   }

   @Override
   public List<EntiteDecoratorForOneToManyComponent> getObjects(){
      return this.objects;
   }

   @Override
   public void setObjects(final List<EntiteDecoratorForOneToManyComponent> objs){
      this.objects = objs;
      updateComponent();
   }

   /**
    * Surcharge pour imposer que la dernière banque ne puisse jamais
    * être éffacée laissant une table orpheline.
    */
   @Override
   public void updateComponent(){
      if(deleteHeader.isVisible()){
         deleteHeader.setVisible(getObjects().size() > 1);
      }
      super.updateComponent();
   }

   @Override
   public void addToListObjects(final EntiteDecoratorForOneToManyComponent obj){
      ImportUtils.addEntiteDecoratorToEntitesAssociees(obj,getObjects(), pathToRespond);
   }

   @Override
   public void deleteObj(final Object obj){
      //hack pour gérer la maladie avec le prélèvement
      MaladieSpecificiteForEntitesAssocieesImport.manageDeleting((EntiteDecoratorForOneToManyComponent)obj, getObjects(), pathToRespond);
      super.deleteObj(obj);
   }
   
   @Override
   public void removeFromListObjects(final Object obj){
      ImportUtils.removeEntiteDecoratorFromEntitesAssociees((EntiteDecoratorForOneToManyComponent)obj, getObjects(), pathToRespond);
   }

   @Override
   public String getGroupHeaderValue(){
      final StringBuffer sb = new StringBuffer();
      sb.append(Labels.getLabel("importTemplate.entites"));
      sb.append(" (");
      sb.append(getObjects().size());
      sb.append(")");
      return sb.toString();
   }

   @Override
   public List<? extends Object> findObjectsAddable(){
      // Enités ajoutables
      final List<EntiteDecorator> entites = new ArrayList<>();
      if(importTemplateStrategy != null) {
         entites.addAll(importTemplateStrategy.defineAddableEntites());
      }

      // retire les Entités deja assignés
      for(int i = 0; i < getObjects().size(); i++){
         entites.remove(getObjects().get(i));
      }

      return entites;
   }

   @Override
   public void drawActionForComponent(){}

   @Override
   public void onClick$objLinkLabel(final Event event){}

   /**
    * Ajout warning 
    */
   @Override
   public void onClick$deleteImage(final Event event){
      String phrase = ObjectTypesFormatters.getLabel("importTemplate.entite.remove.warning", new String[] {});

      final Object obj = AbstractListeController2.getBindingData((ForwardEvent) event, false);

      if(obj != null && ((EntiteDecorator) obj).getEntite().getEntiteId() == 7){ // maladie
         phrase = phrase + " " + Labels.getLabel("importTemplate.maladie.remove.warning");
      }

      if(Messagebox.show(phrase, Labels.getLabel("general.warning"), Messagebox.YES | Messagebox.NO,
         Messagebox.QUESTION) == Messagebox.YES){
         super.onClick$deleteImage(event);
         //TK-538 :
         if(importTemplateStrategy != null) {
            Integer nbMaxEntiteSelectionnable = importTemplateStrategy.defineNbMaxEntiteSelectionnable();
            addObj.setVisible(nbMaxEntiteSelectionnable == null || getObjects().size() < nbMaxEntiteSelectionnable);
         }
         return;
      }

   }

   /**
    * Surcharge pour ne pas appliquer clear sur la liste.
    */
   @Override
   public void switchToCreateMode(){
      objects = new ArrayList<>();
      getBinder().loadComponent(objectsList);
      switchToEditMode(true);
   }

   /**
    * Surcharge pour :
    *  - imposer que la dernière entité ne puisse jamais
    * être effacée car au moins une entité est considérée comme obligatoire 
    * dans l'objet lié.
    *  - et bloquer l'ajout d'un entite si il y a une limite 
    */
   @Override
   public void switchToEditMode(final boolean avecAccesAuxBoutonsAddEtDelete){
      super.switchToEditMode(avecAccesAuxBoutonsAddEtDelete);
      deleteHeader.setVisible(getObjects().size() > 1);
      manageVisbilityForAddObj();
   }
   
   public String getPathToRespond(){
      return pathToRespond;
   }

   public void setPathToRespond(final String pRespond){
      this.pathToRespond = pRespond;
   }

   @Override
   public void onClick$addSelObj(){
      getFicheImportTemplate().showCopyFieldsButton();
      if(objectsBox.getSelectedIndex() > -1){
         addToListObjects((EntiteDecoratorForOneToManyComponent) objectsBox.getModel().getElementAt(objectsBox.getSelectedIndex()));
      }else{ // selectionne le premier de la liste
         if(objectsBox.getItemCount() > 0){
            addToListObjects((EntiteDecoratorForOneToManyComponent) objectsBox.getModel().getElementAt(0));
         }
      }

      // Patient-Maladie-Prelevement Hack pour import : ajout de la maladie si non présent et Patient et Prélèvement ajoutés en bloquant la suppression 
      MaladieSpecificiteForEntitesAssocieesImport.manageAdding(getObjects(), gatsbi, pathToRespond);
      updateComponent();

      // affiche les composants
      onClick$cancelSelObj();
      //TK-538 : modification des annotations d'un objet :
      //surcharge de la visibilité du bouton "ajouter" faite dans onClick$cancelSelObj(), dans le cas où le nombre max
      //d'entités ajoutables est atteint, il ne doit plus être visible
      manageVisbilityForAddObj();
   }

   public FicheImportTemplate getFicheImportTemplate(){
      return (FicheImportTemplate) self.getParent().getAttributeOrFellow("fwinImportTemplate$composer", true);
   }
   public boolean isGatsbi(){
      return gatsbi;
   }

   public void setGatsbi(boolean gatsbi){
      this.gatsbi = gatsbi;
   }
   
   public ImportTemplateStrategy getImportTemplateStrategy(){
      return importTemplateStrategy;
   }

   public void setImportTemplateStrategy(ImportTemplateStrategy importTemplateStrategy){
      this.importTemplateStrategy = importTemplateStrategy;
   }

   private void manageVisbilityForAddObj() {
      if(importTemplateStrategy != null) {
         Integer nbMaxEntiteSelectionnable = importTemplateStrategy.defineNbMaxEntiteSelectionnable();
         addObj.setVisible(nbMaxEntiteSelectionnable == null || getObjects().size() < nbMaxEntiteSelectionnable);
      }
   }
   
}
