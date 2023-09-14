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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.code.CodeAssigneEditableGrid;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 * Classe gérant une fenêtre modal pour la modification multiple d'une
 * de codeAssigne.
 * Introduit la classe CodesPack pour transporter dans un seul objet les
 * liste des codes à créer, à supprimer et le code de la liste annoté
 * comme exporté.
 * Classe créée le 22/03/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ModificationMultipleCodeAssigne extends AbstractModificationMultipleComponent
{

   private static final long serialVersionUID = 3551763682958457361L;

   //private Div codesUniqueDiv;
   private Div codesEraseDiv;

   public CodeAssigneEditableGrid getCodesUniqueController(){
      return (CodeAssigneEditableGrid) self.getFellow("codesUniqueEditor").getFirstChild()
         .getAttributeOrFellow("codesAssitGridDiv$composer", true);
   }

   public CodeAssigneEditableGrid getCodesEraseController(){
      return (CodeAssigneEditableGrid) self.getFellow("codesEraseEditor").getFirstChild()
         .getAttributeOrFellow("codesAssitGridDiv$composer", true);
   }

   public boolean isOrg(){
      return "codeOrganes".equals(getChamp());
   }

   @Override
   public void init(final String pathToPage, final String methodToCall, final List<? extends Object> objs, final String label,
      final String champToEdit, final String entiteNom, final Constraint constr, final Boolean isComb){
      super.init(pathToPage, methodToCall, objs, label, champToEdit, entiteNom, constr, isComb);

      getCodesEraseController().setIsOrg(isOrg());
      getCodesUniqueController().setIsOrg(isOrg());
      getCodesEraseController().setIsMorpho(!isOrg());
      getCodesUniqueController().setIsMorpho(!isOrg());
      getCodesEraseController().setBanque(getMainWindow().getSelectedBanque());
      getCodesUniqueController().setBanque(getMainWindow().getSelectedBanque());
   }

   @Override
   public Object extractValueFromEraserBox(){
      return getPackFromCodeEditor(getCodesEraseController());
   }

   @Override
   public Object extractValueFromMultiBox(){
      return getPackFromCodeEditor(getCodesUniqueController());
   }

   private CodesPack getPackFromCodeEditor(final CodeAssigneEditableGrid editor){
      // encore en edition
      if(editor.getCurrentObjEdited() != null && editor.getCurrentObjEdited().getEdition()){
         throw new WrongValueException(editor.getObjGrid(), Labels.getLabel("grid.currentObjet.edited"));
      }

      final CodesPack pack = new CodesPack();

      for(int i = 0; i < editor.getObjs().size(); i++){
         // trouve l'export
         if(((CodeAssigneDecorator) editor.getObjs().get(i)).getExport()){
            pack.setCodeToExport((CodeAssigne) editor.getObjs().get(i).getObj());
            break;
         }
      }
      // prepare la liste de CodeAssigne a passer
      pack.setCodesToCreateOrEdit(editor.getObjs());

      // codesAssigneToDelete
      final Iterator<SmallObjDecorator> it = editor.getObjToDelete().iterator();
      while(it.hasNext()){
         pack.getCodesToDelete().add((CodeAssigne) ((CodeAssigneDecorator) it.next()).getObj());
      }

      return pack;
   }

   @Override
   public void setConstraintsToBoxes(final Constraint constr){}

   @Override
   public void setEraserBoxeVisible(final boolean visible){
      codesEraseDiv.setVisible(visible);
   }

   @Override
   public void passValueToEraserBox(){
      if(getSelectedValue() != null){
         final CodesPack pck = (CodesPack) getSelectedValue();
         getCodesEraseController().setObjs(pck.getCodesToCreateOrEdit());
      }else{
         getCodesEraseController().reset();
      }
      getCodesEraseController().reloadGrid();
   }

   @Override
   public void passNullToEraserBox(){}

   @Override
   public void extractValuesFromObjects(){
      setHasNulls(false);
      final List<CodeAssigne> codes = new ArrayList<>();
      for(int i = 0; i < getListObjets().size(); i++){
         codes.clear();
         // on extrait le code exporté
         // CodeAssigne exp;
         if(isOrg()){
            codes.addAll(
               ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager((Echantillon) getListObjets().get(i)));
            //exp = (((Echantillon)
            //		getListObjets().get(i)).getCodeOrganeExport());
         }else{
            codes.addAll(
               ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager((Echantillon) getListObjets().get(i)));
            //exp = (((Echantillon)
            //		getListObjets().get(i)).getCodeLesExport());
         }

         if(!codes.isEmpty()){
            final CodesPack pck = new CodesPack();
            pck.setCodesToCreateOrEdit(CodeAssigneDecorator.cleanDecorateListe(codes));
            //pck.setCodeToExport(exp.clone());

            if(!getValues().contains(pck)){
               getValues().add(pck);
               getStringValues().add(formatLocalObject(pck));
            }
         }else{
            setHasNulls(true);
         }
      }
   }

   @Override
   public void setUniqueValueToMultiBox(){
      setNewValue(((CodesPack) getOldUniqueValue()).clone());
      getCodesUniqueController().setObjs(((CodesPack) getNewValue()).getCodesToCreateOrEdit());
   }

   /**
    * Formatte l'affichage d'un objet CodePack sous la forme d'une
    * ligne de codes (sans libelles) avec une asterix pour noter
    * le code exporté.
    */
   @Override
   public String formatLocalObject(final Object obj){
      String out = null;
      if(obj != null){
         if(((CodesPack) obj).getCodesToCreateOrEdit() != null){
            final StringBuilder bld = new StringBuilder();
            for(int i = 0; i < ((CodesPack) obj).getCodesToCreateOrEdit().size(); i++){
               bld.append(((CodeAssigneDecorator) ((CodesPack) obj).getCodesToCreateOrEdit().get(i)).getCode());
               // export
               if(((CodeAssigneDecorator) ((CodesPack) obj).getCodesToCreateOrEdit().get(i)).getObj()
                  .equals(((CodesPack) obj).getCodeToExport())){
                  bld.append("*");
               }
               // connecteur ' - '
               if(i < ((CodesPack) obj).getCodesToCreateOrEdit().size() - 1){
                  bld.append(" - ");
               }
            }
            out = bld.toString();
         }
      }
      return out;
   }

   @Override
   public AnnotateDataBinder getBinder(){
      return ((AnnotateDataBinder) self.getParent().getAttributeOrFellow("modificationCodeAssigne", true));
   }
}
