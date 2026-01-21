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
//NB : cette classe ne devrait pas avoir un nom aussi générique car elle concerne une Exception particulière portant sur un
//objet (utilisation de son identifiant)... Normalement la classe TKException, dont il est logique que toutes les Exceptions de l'application héritent,
//ne devrait contenir qu'un message et les paramètres associés à ce message pour gérer l'internationalisation
//une telle classe BasicTKException, héritant de TKException a été créée pour gérer le ticket TK-817
public class TKException extends RuntimeException
{

   private static final long serialVersionUID = 9081275137758719319L;

   private String entiteObjetException;

   private String identificationObjetException;

   //cet attribut est protected et non private pour que des classes filles puissent directement y accéder
   //sans avoir à renter dans le traitement de la méthode getMessage() de cette classe
   protected String message;

   private TKdataObject tkObj;

   public TKException(){
      super();
   }

   /**
    * /!\ si message correspond à la clé d'un message internationalisé et que ce message doit contenir en paramètre
    * identificationObjetException, celui-ci doit être associé à l'indice 1 ({1}). cf exploitation dans AbstractController.handleExceptionMessage()
    * @param message
    */
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
