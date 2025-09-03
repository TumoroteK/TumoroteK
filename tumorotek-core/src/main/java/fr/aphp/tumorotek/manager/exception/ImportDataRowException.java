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
import java.util.List;

import fr.aphp.tumorotek.manager.exception.uimessage.MultipleUIMessageForRow;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForCell;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;
import fr.aphp.tumorotek.manager.io.imports.ImportCellError;
import fr.aphp.tumorotek.manager.io.imports.ImportRowError;

/**
 * exception lancée quand des erreurs sont rencontrées lors de la lecture des lignes de données d'un fichier d'import
 * elle contient une liste de toutes les erreurs rencontrées pour le lot de données traitées, erreurs regroupées par ligne
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportDataRowException extends ImportDataException implements ImportExceptionWithDetailByRowInterface
{

   private static final long serialVersionUID = 1L;
   
   private List<ImportRowError> listImportRowError = new ArrayList<ImportRowError>();
   
   //si il y a un lotissement, une ImportDataRowException peut être envoyée alors que des lignes ont été importées par le traitement des lots précédents la détection de la 1ere erreur
   //cette information sera retournée à l'utilisateur
   private int nbImportedRows;

   public ImportDataRowException() {
      super();
   }
   
   public ImportDataRowException(String message) {
      super(message);
   }
   
   public ImportDataRowException(final List<ImportRowError> listImportRowError){
      super();
      this.listImportRowError = listImportRowError;
   }
   
   public ImportDataRowException(final List<ImportRowError> listImportRowError, int nbImportedRows){
      super();
      this.listImportRowError = listImportRowError;
      this.nbImportedRows = nbImportedRows;
   }

   public List<ImportRowError> getListImportRowError(){
      return listImportRowError;
   }   

   /**
    * retourne une liste de tous les messages à afficher pour les lignes ayant une erreur sur l'une de leurs cellules
    * ce message sera construit comme suit :
    * "nom de la colonne concernée par l'erreur 1 : type de l'erreur, détail | nom de la colonne concernée par l'erreur 2 : type de l'erreur, détail..."
    * A noter que le message ne ramènera que 15 erreurs max pour ne pas avoir un texte trop long
    * la liste est triée par numéro de ligne ayant une erreur 
    * @return un MultipleUIMessageForRow constitué de tous les messages internationalisés à utiliser pour chaque erreur de chaque ligne
    */
   @Override
   public List<UIMessageForRowInterface> buildSortedUIMessageForRow() {
      List<UIMessageForRowInterface> result = null;
      final int MAX_ERROR_BY_ROW = 15;
      ImportCellError currentCellError = null;
      if(listImportRowError != null) {
         result = new ArrayList<UIMessageForRowInterface>();
         List<ImportCellError> listImportCellErrors = null;
         for(ImportRowError importRowError: listImportRowError) {
            //gestion de chaque ligne : lecture de toutes les erreurs liées aux cellules et création pour chaque d'un UIMessageForCell (dans la limite de 15 par ligne)
            listImportCellErrors = importRowError.getListImportCellError();
            int nbCellError = listImportCellErrors.size();
            List<UIMessageForCell> listUIMessageForCell = new ArrayList<UIMessageForCell>();
            for(int i=0; i<Math.min(nbCellError, MAX_ERROR_BY_ROW); i++) {
               currentCellError = listImportCellErrors.get(i);
               AbstractImportCellScopeException wrongImportValueException = currentCellError.getImportCellScopeException();
               if(wrongImportValueException != null) {
                  UIMessage uiMessage = wrongImportValueException.buildUIMessage();
                  listUIMessageForCell.add(new UIMessageForCell(currentCellError.getIndexCellInRow(), uiMessage.getI18nKey(), uiMessage.getParams()));
               }
            }
            result.add(new MultipleUIMessageForRow(importRowError.getRowNum(), listUIMessageForCell));

            Collections.sort(result, Comparator.comparing(UIMessageForRowInterface::getRowNum));
         }
      }
      
      return result;
   }

   //si le traitement est loti et que des imports ont pu être réalisés, le message renvoyé à l'utilisateur est différent.
   @Override
   public UIMessage retrieveMessageAvecNombreErreur() {
      //si le traitement n'est pas loti ou que l'erreur a été rencontré sur le 1er lot, message standard
      if(nbImportedRows == 0) {
         return super.retrieveMessageAvecNombreErreur();
      }
      else {//sinon, on indique qu'un certain nombre de lignes ont quand même pu être traitées : toutes celles avant le début de ce lot
         return new UIMessage("importTemplate.statistiques.errors.lot", new String[] { String.valueOf(nbImportedRows),
                                                                                       String.valueOf(getNbErrors())});         
      }
   }
   
   @Override
   public int getNbErrors() {
      return listImportRowError.size();
   }
   
   @Override
   public int getNbImportedRows() {
      return nbImportedRows;
   }
}
