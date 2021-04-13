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

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.action.administration.annotations.DureeComponent;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.utils.Duree;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une Duree
 * Classe créée le 26/03/2018
 *
 * @author Answald BOURNIQUE
 * @version 2.2.0
 * @since 2.2.0
 */
public class ModificationMultipleDuree extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 7926197055359163741L;

   /**
    * Div contenant la box DureeComponent dans le cas de valeurs multiples existantes (cf. rowMultiValue)
    */
   private Div dureeDiv;

   /**
    * Div contenant la box DureeComponent dans le cas de nouvelle valeur (cf. rowOneValue)
    */
   private Div dureeOneDiv;

   /**
    * Box de saisie de la durée
    */
   private DureeComponent dureeBox;

   /**
    * Traitement de la duree
    */
   private Duree duree;
   /**
    * Si une valeur est obligatoire
    */
   private Boolean isOblig;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      dureeBox = new DureeComponent();
      dureeOneDiv.appendChild(dureeBox);
   }
   
   /**
    * Méthode intialisant le composant, spécifique au composant Duree
    * @param pathToPage Chemin vers la page qui demande une modif.
    * @param methodToCall Méthode à appeler
    * @param objs Liste des objets à modifier
    * @param label Code pour label du champ dans .properties 
    * internationalisation.
    * @param entiteToEdit Nom de l'entité à modifier. 
    * @param champToEdit Champ de l'entité à modifier.
    * @param ent nom de l'entite a afficher dans l'intitulé
    * @param Constraint à appliquer
    * @param Boolean true si champAnnotation combine
    * @param isOblig true si le champ est obligatoire (non null, non 0)
    */
   public void init(final String pathToPage, final String methodToCall, final List<? extends Object> objs, final String label,
      final String champToEdit, final String entiteNom, final Constraint constr, final Boolean isComb, final Boolean isOblig){
      this.isOblig = isOblig;
      super.init(pathToPage, methodToCall, objs, label, champToEdit, entiteNom, constr, isComb);
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public String formatLocalObject(final Object obj){
      // verifie la presence annotation combine
      if(obj != null){
         if("system.tk.unknownExistingValue".equals(obj)){
            return Labels.getLabel("system.tk.unknownExistingValue");
         }
         duree = new Duree(new Long(obj.toString()), Duree.SECONDE);
         dureeBox.setDuree(duree);
         return ObjectTypesFormatters.formatDuree(duree);
      }
      return null;
   }

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      dureeBox.setDuree(null);
      dureeDiv.appendChild(dureeBox);
      dureeDiv.setVisible(visible);
   }

   @Override
   public Object extractValueFromEraserBox(){
      if(0 != dureeBox.getDuree().getTemps(Duree.SECONDE)){
         return dureeBox.getDuree().getTemps(Duree.SECONDE).toString();
      }
      return null;
   }

   @Override
   public Object extractValueFromMultiBox(){
      Long temps = dureeBox.getDuree().getTemps(Duree.SECONDE);
      if(null != temps && 0 != temps){
         return dureeBox.getDuree().getTemps(Duree.SECONDE).toString();
      }
      return null;
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationDuree", true));
   }

   @Override
   public void passValueToEraserBox(){
      if(null != getSelectedValue()){
         Duree duree = new Duree(new Long(getSelectedValue().toString()), Duree.SECONDE);
         dureeBox.setDuree(duree);
      }else{
         dureeBox.setDuree(null);
      }
      //      eraseMultiTextbox.setValue(duree.getTemps(Duree.SECONDE).toString());
   }

   @Override
   public void passNullToEraserBox(){
      dureeBox.setDuree(null);
   }

   /**
    * Forcer la vérification si valeur obligatoire
    */
   @Override
   public void onClick$validate(){
      if(isOblig && (dureeBox.getDuree() == null || dureeBox.getDuree().getTemps(Duree.SECONDE) == 0)){
         throw new WrongValueException(dureeBox, Labels.getLabel("anno.duree.empty"));
      }
      super.onClick$validate();
   }

}
