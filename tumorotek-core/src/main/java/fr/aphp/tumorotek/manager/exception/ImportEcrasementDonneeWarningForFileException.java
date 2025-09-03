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

package fr.aphp.tumorotek.manager.exception;

import java.util.List;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.manager.io.imports.ImportRowError;

/**
 * Dans le cas d'un import "en mode supervision", exception lancée quand il a été détecté que dans le fichier à importer au moins une valeur existante doit être mise à jour.
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportEcrasementDonneeWarningForFileException extends ImportDataRowException
{

   private static final long serialVersionUID = 1L;

   public ImportEcrasementDonneeWarningForFileException(final List<ImportRowError> listImportRowError){
      super(listImportRowError);
   }
   
   public ImportEcrasementDonneeWarningForFileException(final List<ImportRowError> listImportRowError, int nbImportedRows){
      super(listImportRowError, nbImportedRows);
   }
   
   @Override
   public UIMessage retrieveUIMessageForMainMessage() {
      UIMessage result = null;
      if(getNbImportedRows() == 0) {
         result = new UIMessage("importTemplate.resultats.warn.title.warning.ecrasement", null);
      }
      else {
         result = new UIMessage("importTemplate.resultats.warn.title.warning.ecrasement.importPartiel", new String[] { String.valueOf(getNbImportedRows())});
      }
      
      return result;
   }
   
   @Override
   public UIMessage retrieveMessageAvecNombreErreur() {
      UIMessage result = null;
      if(getNbImportedRows() == 0) {
         result = new UIMessage("importTemplate.statistiques.errors.warning.ecrasement", new String[] {String.valueOf(getListImportRowError().size())});
      }
      else {
         result = new UIMessage("importTemplate.statistiques.errors.warning.ecrasement.importPartiel", new String[] { String.valueOf(getListImportRowError().size()), String.valueOf(getNbImportedRows())});
      }
      
      return result;
      
   }
   
   @Override
   public UIMessage retrieveDetail(){
      return new UIMessage("importTemplate.dl.warning.ecrasement", null);
   }
   
   @Override
   public String retrieveI18KeyForDownloadButton() {
      return "importTemplate.dl.warning.ecrasement.button";
   }
   
   @Override
   public boolean isWarning() {
      return true;
   }

}
