package fr.aphp.tumorotek.manager.exception;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.TKdataObject;

/**
 * Classe gérant les exceptions lancées par TK. Elle contient des
 * champs permettant d'identifier (si nécessaire) l'objet sur lequel
 * porte cette execption.
 * Classe créée le 07/02/2012.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TKException extends RuntimeException
{

   private static final long serialVersionUID = 9081275137758719319L;

   private String entiteObjetException;
   private String identificationObjetException;
   private String message;
   private TKdataObject tkObj;

   public TKException(){
      super();
   }

   public TKException(final String message){
      super();
      setMessage(message);
   }

   public TKException(final String message, final TKdataObject tkObj){
      super();
      setMessage(message);
      setTkObj(tkObj);
   }

   public TKException(final String message, final String idoe){
      super();
      setMessage(message);
      setIdentificationObjetException(idoe);
   }

   @Override
   public String getMessage(){
      if(message != null){
         if(tkObj != null && tkObj instanceof TKStockableObject){
            return ((TKStockableObject) tkObj).getCode() + " : " + message;
         }
         return message;
      }else{
         return super.getMessage();
      }
   }

   public void setMessage(final String message){
      this.message = message;
   }

   public String getEntiteObjetException(){
      return entiteObjetException;
   }

   public void setEntiteObjetException(final String e){
      this.entiteObjetException = e;
   }

   public String getIdentificationObjetException(){
      return identificationObjetException;
   }

   public void setIdentificationObjetException(final String i){
      this.identificationObjetException = i;
   }

   public TKdataObject getTkObj(){
      return tkObj;
   }

   public void setTkObj(final TKdataObject t){
      this.tkObj = t;
   }

}
