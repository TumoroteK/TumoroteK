package fr.aphp.tumorotek.manager.exception;

public class ExcelWriteException extends RuntimeException {
   // Constructor that accepts a message
   public ExcelWriteException(String message) {
      super(message);
   }

   public ExcelWriteException(String message, Throwable cause) {
      super(message, cause);
   }

   public ExcelWriteException(Throwable cause) {
      super(cause);
   }
}
