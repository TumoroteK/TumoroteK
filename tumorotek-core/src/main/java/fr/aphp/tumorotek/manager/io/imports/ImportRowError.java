/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.io.imports;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet de stocker toutes les erreurs rencontrées lors de la lecture d'une ligne d'un fichier d'import
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportRowError
{
   private List<ImportCellError> listImportCellError = new ArrayList<ImportCellError>();

   //numéro de la ligne contenant les erreurs
   private Integer rowNum;
   
   public ImportRowError(Integer rowNum) {
      this.rowNum = rowNum;
   }

   public Integer getRowNum(){
      return rowNum;
   }
   public void setRowNum(Integer rowNum){
      this.rowNum = rowNum;
   }
 

   public List<ImportCellError> getListImportCellError(){
      return listImportCellError;
   }
   public void setListImportCellError(List<ImportCellError> listImportCellError){
      this.listImportCellError = listImportCellError;
   }
   
   public void addImportCellError(ImportCellError importCellError) {
      if(importCellError != null) {
         listImportCellError.add(importCellError);
      }
   }
   
   public boolean isEmpty() {
      return listImportCellError == null || listImportCellError.isEmpty();
   }
}
