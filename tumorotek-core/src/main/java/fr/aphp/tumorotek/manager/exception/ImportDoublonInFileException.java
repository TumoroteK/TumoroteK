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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRow;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;

/**
 * exception lancée quand un ou plusieurs cas de doublons existent au sein du fichier d'import : 
 * plusieurs lignes liées à la même clé fonctionnelle sont alors présentes
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportDoublonInFileException extends ImportDataException implements ImportExceptionWithDetailByRowInterface
{

   private static final long serialVersionUID = 1L;
   
   private static final String I18N_KEY__CORRECTIF_DOUBLON = "importTemplate.correctif.error.doublon";
   
   //nom de la colonne contenant la clé (1ere colonne du fichier d'import)
   private String nomKeyColonne;
   //la map des doublons contient en clé le code en doublon et en valeur les numéros des lignes contenant ce code (le numéro commence à 1)
   private Map<String, List<Integer>> mapDoublon = new HashMap<String, List<Integer>>();
   
   public ImportDoublonInFileException(String nomKeyColonne, Map<String, List<Integer>> mapDoublon) {
      super();
      this.nomKeyColonne = nomKeyColonne;
      this.mapDoublon = mapDoublon;
   }

   @Override
   public String getMessage() {
      StringBuilder message = new StringBuilder("Import : les codes suivants sont en doublon dans le fichier : ");
      if(mapDoublon != null) {
         message.append(mapDoublon.keySet().stream().collect(Collectors.joining(", ")));
      }
      
      return message.toString();
   }
   
   /**
    * retourne une liste de tous les messages à afficher pour les lignes ayant un doublon
    * ce message UIMessageForRowInterface contient le code en doublon et la liste des numéros de lignes ayant ce code
    * la liste est triée par numéro de ligne en doublon 
    * @return List<UIMessageForRowInterface>
    */
   public List<UIMessageForRowInterface> buildSortedUIMessageForRow() {
      List<UIMessageForRowInterface> result = new ArrayList<UIMessageForRowInterface>();
      Set<Map.Entry<String, List<Integer>>> mapEntries =  mapDoublon.entrySet();
      for (Map.Entry<String, List<Integer>> entry : mapEntries) {
         List<Integer> listRowNumForEntry = entry.getValue();
         String code = entry.getKey();
         for(Integer rowNum : listRowNumForEntry) {
            String listRowNumForEntryAsString = listRowNumForEntry.stream().map(numero -> String.valueOf(numero)).collect(Collectors.joining(", "));
            result.add(new UIMessageForRow(rowNum, I18N_KEY__CORRECTIF_DOUBLON, new String[] {nomKeyColonne, code, listRowNumForEntryAsString}));
         }
      }

      Collections.sort(result, Comparator.comparing(UIMessageForRowInterface::getRowNum));
      
      return result;
   }
   
   @Override
   public UIMessage retrieveMessageAvecNombreErreur() {
      return new UIMessage("importTemplate.statistiques.errors.doublon", new String[] { String.valueOf(getNbErrors())});
   }

  
   @Override
   public int getNbErrors() {
      return mapDoublon.size();
   }
   
   @Override
   public int getNbImportedRows() {
      return 0;
   }
}
