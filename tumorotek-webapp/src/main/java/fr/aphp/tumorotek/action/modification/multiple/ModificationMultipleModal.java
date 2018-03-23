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
package fr.aphp.tumorotek.action.modification.multiple;

import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'un
 * champ rensegnable à l'aide d'une modale (ex Collaborateur...) .
 * Classe créée le 04/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ModificationMultipleModal extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 7926197055359163741L;

   /**
    * Components specific textbox.
    */
   private Label oneValueLabel;
   private Label eraseModalLink;
   private Image delete;
   private Label eraseValueLabel;

   private Object backFromModal;

   /**
    * @return la valeur formattée de l'objet unique.
    */
   public String getCurrentValue(){
      if(getStringValues().size() > 0){
         return getStringValues().get(0);
      }else{
         return null;
      }
   }

   public void onClick$oneValueModalLink(){
      callModalOpening();
   }

   public void onClick$eraseModalLink(){
      callModalOpening();
   }

   /**
    * Ouvre la modale.
    */
   private void callModalOpening(){
      openCollaborationsWindow(page, "general.recherche", "select", null, "Collaborateur", null, Path.getPath(self), null);
   }

   /**
    * Méthode appelée par la fenêtre CollaborationsController quand
    * l'utilisateur sélectionne un collaborateur.
    * @param e Event contenant le collaborateur sélectionné.
    */
   public void onGetObjectFromSelection(final Event e){
      // si un collaborateur a été sélectionné
      if(e.getData() != null){
         backFromModal = e.getData();
         if(getStringValues().size() == 1){
            oneValueLabel.setValue(formatLocalObject(backFromModal));
         }else{
            eraseValueLabel.setVisible(true);
            eraseValueLabel.setValue(formatLocalObject(backFromModal));
         }
      }
   }

   @Override
   public void initComponentsInWindow(){
      super.initComponentsInWindow();
      backFromModal = getOldUniqueValue();
      // affiche le bouton delete
      if(getStringValues().size() == 1){
         if(!getStringValues().get(0).equals("")){
            delete.setVisible(true);
         }
      }
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public String formatLocalObject(final Object obj){
      if(obj != null){
         if(getChamp().equals("collaborateur")){
            return ObjectTypesFormatters.collaborateurFormatter((Collaborateur) obj);
         }
      }
      return (String) obj;
   }

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      eraseModalLink.setVisible(visible);
   }

   @Override
   public Object extractValueFromEraserBox(){
      return backFromModal;
   }

   @Override
   public Object extractValueFromMultiBox(){
      return backFromModal;
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationModal", true));
   }

   /**
    * Efface la valeur du boolean.
    */
   public void onClick$delete(){
      backFromModal = null;
      delete.setVisible(false);
      oneValueLabel.setValue(null);
   }

   @Override
   public void passValueToEraserBox(){}

   @Override
   public void passNullToEraserBox(){}
}
