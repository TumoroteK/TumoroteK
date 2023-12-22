package fr.aphp.tumorotek.utils;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

/**
 * Classe utilitaire pour gérer les boîtes de dialogue et les interactions utilisateur dans l'application.
 * Cette classe ne doit pas être instanciée, car elle se compose de méthodes statiques pour la gestion des messages.
 */

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
      // Affiche une boîte de dialogue et renvoie un entier représentant le bouton sur lequel vous avez appuyé.
      int response =  Messagebox.show(message,title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION );
      // Renvoie vrai si l'utilisateur a répondu "Oui" (valeur 16), sinon renvoie faux (valeur 32 pour "Non").
      return response == Messagebox.YES;
   }

   /**
    * Affiche une boîte de dialogue modale d'erreur avec un titre et un message personnalisés.
    *
    * @param title Le titre de la boîte de dialogue.
    * @param errorMessage Le message d'erreur à afficher.
    */
   public static void openErrorModal(String title, String errorMessage) {
      Clients.clearBusy();
      Messagebox.show(errorMessage, title, Messagebox.OK, Messagebox.EXCLAMATION);
   }

   /**
    * Affiche une boîte de dialogue modale d'information avec un titre et un message personnalisés.
    *
    * @param title Le titre de la boîte de dialogue.
    * @param message Le message informatif à afficher.
    */
   public static void openInfoModal(String title, String message) {
      Clients.clearBusy();
      Messagebox.show(message, title, Messagebox.OK, Messagebox.INFORMATION);
   }



}

