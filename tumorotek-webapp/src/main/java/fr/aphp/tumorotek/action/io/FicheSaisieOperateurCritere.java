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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.webapp.tree.export.CritereNode;
import fr.aphp.tumorotek.webapp.tree.export.GroupementNode;

public class FicheSaisieOperateurCritere extends AbstractFicheCombineController
{

   private static final long serialVersionUID = 1L;

   private String path;

   private Label operateurLabel;

   private Label operateurRequired;

   private Label critereLabel;

   private Label critereRequired;

   private Label errorLabel;

   private Listbox operateursBox;

   private Listbox criteresBox;

   /**
    *  Associations.
    */
   private List<String> operateurs;

   private List<CritereNode> criteres;

   /**
    * Objets principaux.
    */
   private String operateur;

   private CritereNode critereNode;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      setObjLabelsComponents(new Component[] {this.operateurLabel, this.critereLabel, this.errorLabel});

      setObjBoxsComponents(new Component[] {this.operateursBox, this.criteresBox});

      setRequiredMarks(new Component[] {this.operateurRequired, this.critereRequired});

      if(winPanel != null){
         winPanel.setHeight("200px");
      }

   }

   public void init(final String p){
      this.setPath(p);

      this.operateurs = new ArrayList<>();
      this.operateurs.add(null);
      this.operateurs.add(Labels.getLabel("general.selectlist.and"));
      this.operateurs.add(Labels.getLabel("general.selectlist.or"));

      this.operateursBox.setModel(new SimpleListModel<>(this.operateurs));

      this.criteres = new ArrayList<>();

      final FicheRequete fiche = getFicheRequete();
      final GroupementNode n = fiche.getNode();
      this.criteres.addAll(n.getLeaves());

      /** On présélectionne le Critere s'il n'y en a qu'un */
      if(this.criteres.size() == 1){
         this.criteresBox.setModel(new SimpleListModel<>(this.criteres));
         this.criteresBox.setSelectedIndex(0);
         this.critereNode = criteres.get(0);
      }else{
         this.criteres.add(0, null);
         this.criteresBox.setModel(new SimpleListModel<>(this.criteres));
      }

      this.errorLabel.setValue(null);
   }

   /**
    * Retourne le controller de la fiche d'un prlvt.
    * @param event
    * @return
    */
   private FicheRequete getFicheRequete(){
      return (FicheRequete) Path.getComponent(path).getAttributeOrFellow("winFicheRequete$composer", true);
   }

   @Override
   public void clearData(){
      clearConstraints();
   }

   @Override
   public void createNewObject(){}

   /**
    * Méthode vidant tous les messages d'erreurs apparaissant dans
    * les contraintes de la fiche.
    */
   public void clearConstraints(){
      Clients.clearWrongValue(operateursBox);
      Clients.clearWrongValue(criteresBox);

      this.errorLabel.setValue(null);
      this.errorLabel.setVisible(false);
   }

   /***********************************************************/
   /*****************  Events controllers.  *******************/
   /***********************************************************/

   public void onSelect$operateursBox(){
      if(operateursBox.getSelectedIndex() > 0){
         this.operateur = operateurs.get(operateursBox.getSelectedIndex());
      }else{
         this.operateur = null;
      }
   }

   public void onSelect$criteresBox(){
      if(criteresBox.getSelectedIndex() > 0){
         this.critereNode = criteres.get(criteresBox.getSelectedIndex());
      }else{
         this.critereNode = null;
      }
   }

   @Override
   public void onClick$cancelC(){
      // si le chemin d'accès à la page est correcte
      if(Path.getComponent(path) != null){
         // on envoie un event à cette page avec
         // l'établissement sélectionné
         Events.postEvent(new Event("onCloseSaisieOperateurCritere", Path.getComponent(path), null));
      }
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getParent().getParent()));
   }

   @Override
   public void onClick$validateC(){

      clearConstraints();

      validateForm();

      final Object[] retour = new Object[2];
      retour[0] = critereNode;
      retour[1] = operateursBox.getSelectedIndex();

      // si le chemin d'accès à la page est correcte
      if(Path.getComponent(path) != null){
         // on envoie un event à cette page avec
         // l'établissement sélectionné
         Events.postEvent(new Event("onCloseSaisieOperateurCritere", Path.getComponent(path), retour));
      }
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getParent().getParent()));
   }

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void updateObject(){}

   private void validateForm(){
      if(operateursBox.getModel().getSize() > 1 && operateursBox.getSelectedIndex() <= 0){
         throw new WrongValueException(operateursBox, Labels.getLabel("operateur.invalid.selection"));
      }
      if(critereNode == null){
         throw new WrongValueException(criteresBox, Labels.getLabel("critere.invalid.selection"));
      }
   }

   @Override
   public void setFieldsToUpperCase(){}

   /***********************************************************/
   /****************  Getters and Setters.  *******************/
   /***********************************************************/
   public List<String> getOperateurs(){
      return operateurs;
   }

   public void setOperateurs(final List<String> ops){
      this.operateurs = ops;
   }

   public List<CritereNode> getCriteres(){
      return criteres;
   }

   public void setCriteres(final List<CritereNode> crits){
      this.criteres = crits;
   }

   public String getOperateur(){
      return operateur;
   }

   public void setOperateur(final String op){
      this.operateur = op;
   }

   public CritereNode getCritereNode(){
      return critereNode;
   }

   public void setCritereNode(final CritereNode cNode){
      this.critereNode = cNode;
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
   public void onClick$addNewC(){}

   @Override
   public void onClick$deleteC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   @Override
   public void setFocusOnElement(){}

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
   public void removeObject(final String comments){}

   @Override
   public void setParentObject(final TKdataObject obj){}
}
