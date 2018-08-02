/**
 * 
 */
package fr.aphp.tumorotek.manager.exception;

import fr.aphp.tumorotek.model.TKdataObject;

/**
 * Exception représentant une erreur de validation
 * 
 * @author Gille Chapelot
 *
 */
public class ValidationException extends TKException
{

   private static final long serialVersionUID = 7312227211267679679L;

   /**
    * Constructeur
    */
   public ValidationException(String message, TKdataObject obj, Throwable cause) {
      super(message, obj);
      this.initCause(cause);
   }

   public ValidationException(String message, TKdataObject obj){
      super(message, obj);
   }
   
   public ValidationException(String message){
      super(message);
   }
   
}
