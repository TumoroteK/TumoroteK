package fr.aphp.tumorotek.utils;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;


public class MessagesUtils
{

   private MessagesUtils(){   }

   /**
    * Affiche une boîte de dialogue modale de question avec un titre et un message personnalisés.
    *
    * @param title Le titre de la boîte de dialogue.
    * @param message Le message/question à afficher.
    * @return Renvoie vrai si l'utilisateur a répondu "Oui" (valeur 16), sinon renvoie faux (valeur 32 pour "Non").
    */
   public static boolean openQuestionModal(String title, String message){
      Clients.clearBusy();

      int response =  Messagebox.show(message,title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION );

      // Renvoie vrai si l'utilisateur a répondu "Oui" (valeur 16), sinon renvoie faux (valeur 32 pour "Non").
      return response == Messagebox.YES;
   }

}

