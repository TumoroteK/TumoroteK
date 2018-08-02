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
import java.util.List;

import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.model.code.CodeAssigne;

/**
 * Classe de transport contenant une liste de codes assigne à créer,
 * une liste de codes assignes à supprimer et le code à exporter parmi les
 * codes à créer.
 * Date: 22/03/2011.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CodesPack
{

   private static final long serialVersionUID = 3551763682958457361L;

   private List<SmallObjDecorator> codesToCreateOrEdit = new ArrayList<>();
   private CodeAssigne codeToExport = null;
   private List<CodeAssigne> codesToDelete = new ArrayList<>();

   public static long getSerialVersionUID(){
      return serialVersionUID;
   }

   public List<SmallObjDecorator> getCodesToCreateOrEdit(){
      return codesToCreateOrEdit;
   }

   public CodeAssigne getCodeToExport(){
      return codeToExport;
   }

   public List<CodeAssigne> getCodesToDelete(){
      return codesToDelete;
   }

   public void setCodesToCreateOrEdit(final List<SmallObjDecorator> c){
      this.codesToCreateOrEdit = c;
   }

   public void setCodeToExport(final CodeAssigne c){
      c.setCodeAssigneId(null);
      c.setEchantillon(null);
      //c.setEchanExpOrg(null);
      //c.setEchanExpLes(null);
      this.codeToExport = c;
   }

   public void setCodesToDelete(final List<CodeAssigne> c){
      this.codesToDelete = c;
   }

   public boolean isEmpty(){
      return codeToExport == null && getCodesToCreateOrEdit().isEmpty();
      //			&& getCodesToDelete().isEmpty();
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || !(obj instanceof CodesPack)){
         return false;
      }
      final CodesPack test = (CodesPack) obj;
      return ((this.codeToExport == test.codeToExport
         || (this.codeToExport != null && this.codeToExport.equals(test.codeToExport)))
         && (this.codesToDelete == test.codesToDelete
            || (this.codesToDelete != null && this.codesToDelete.equals(test.codesToDelete)))
         && (this.codesToCreateOrEdit == test.codesToCreateOrEdit
            || (this.codesToCreateOrEdit != null && this.codesToCreateOrEdit.equals(test.codesToCreateOrEdit))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashCodeToExport = 0;
      int hashCodesToDelete = 0;
      int hashCodesToCreateOrEdit = 0;

      if(this.codeToExport != null){
         hashCodeToExport = this.codeToExport.hashCode();
      }
      if(this.codesToDelete != null){
         hashCodesToDelete = this.codesToDelete.hashCode();
      }
      if(this.codesToCreateOrEdit != null){
         hashCodesToCreateOrEdit = this.codesToCreateOrEdit.hashCode();
      }

      hash = 31 * hash + hashCodeToExport;
      hash = 31 * hash + hashCodesToDelete;
      hash = 31 * hash + hashCodesToCreateOrEdit;

      return hash;
   }

   @Override
   public CodesPack clone(){
      final CodesPack clone = new CodesPack();

      if(getCodeToExport() != null){
         clone.setCodeToExport(getCodeToExport().clone());
      }

      for(int i = 0; i < getCodesToCreateOrEdit().size(); i++){
         clone.getCodesToCreateOrEdit().add(((CodeAssigneDecorator) getCodesToCreateOrEdit().get(i)).clone());
      }
      for(int i = 0; i < getCodesToDelete().size(); i++){
         clone.getCodesToDelete().add(getCodesToDelete().get(i).clone());
      }

      return clone;
   }
}
