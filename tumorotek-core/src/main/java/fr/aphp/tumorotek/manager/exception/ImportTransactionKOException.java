package fr.aphp.tumorotek.manager.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;

/**
 * exception lancée quand un traitement d'import plante lors d'exécution d'une transaction de mise à jour des données
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportTransactionKOException extends AbstractImportFileScopeException implements ImportExceptionWithDetailByRowInterface
{

   private static final long serialVersionUID = -4211270354720559961L;
   
   private final Logger log = LoggerFactory.getLogger(ImportTransactionKOException.class);
   
   private int nbImportedRows;

   public ImportTransactionKOException(String message) {
      super(message);
   }
   
   public ImportTransactionKOException(String message, Throwable cause) {
      super(message, cause);
   }
   
   public ImportTransactionKOException(String message, Throwable cause, int nbImportedRows) {
      this(message, cause);
      this.nbImportedRows = nbImportedRows;
   }
   
   //dans ce type d'exception, il n'y a pas d'erreur à la ligne
   @Override
   public List<UIMessageForRowInterface> buildSortedUIMessageForRow() {
      return null;
   }

   @Override
   public int getNbImportedRows() {
      return nbImportedRows;
   }
   
   @Override
   protected String getI18nKey(){
      return "importTemplate.dl.importPatiel";
   }
   
   //Affichage de l'erreur technique pour aider au diagnostic. 
   //Le message pouvant être long, on tronque à 165 caractères si un bouton de détail est à afficher, pour éviter de cacher ce dernier
   //A noter que d'un point de vue responsabilité des objets, il aurait été plus judicieux de faire cette règle qui tronque 
   //au niveau de l'appelant ResultatsImportModale.java mais ce cas est très à la marge par rapport à toutes les autres exceptions gérées pour lesquelles
   //le message n'est pas dynamique donc décision de déporter la règle dans le cas unique qui la nécessite pour "cibler" facilement et faciliter d'éventuels tests... 
   @Override
   public UIMessage retrieveMessageAvecNombreErreur(){
      String[] params = new String[1];
      if (getCause() != null && getCause().getMessage() != null) {
         String param = getCause().getMessage();
         log.error("Erreur lors du traitement d'import {} ", param);
         if(existCorrectionsFile()) {
            final int legnthMax = 165;
            if(param.length() > legnthMax) {
               param = new StringBuilder(param.substring(0,legnthMax)).append(" ...").toString();
            }
         }
         params[0] = param;
      }
      else {
         params[0] = "";
      }
      return new UIMessage("importTemplate.resultats.warn.detail.transactionKO.importPartiel", params);
   }

   //pas de détail si aucune ligne n'a été importée
   @Override
   public UIMessage retrieveDetail(){
      if(nbImportedRows > 0) {
         return buildUIMessage();
      }
      return null;
   }

   @Override
   public boolean existCorrectionsFile(){
      return nbImportedRows > 0 ;
   }
   
   @Override
   public UIMessage retrieveUIMessageForMainMessage() {
      UIMessage result = null;
      if(nbImportedRows == 0) {
         result = new UIMessage("importTemplate.resultats.warn.title.transactionKO", null);
      }
      else {
         result = new UIMessage("importTemplate.resultats.warn.title.transactionKO.importPartiel", new String[] { String.valueOf(nbImportedRows)});
      }
      
      return result;
   }
   
   @Override
   public String retrieveI18KeyForDownloadButton() {
      return "importTemplate.dl.importPatiel.button";
   }   
}
