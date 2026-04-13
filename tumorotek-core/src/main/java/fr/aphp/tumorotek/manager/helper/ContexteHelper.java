package fr.aphp.tumorotek.manager.helper;

import fr.aphp.tumorotek.model.contexte.EContexte;

public class ContexteHelper
{
   public static boolean isContexteSerologie(EContexte eContexte) {
      return eContexte != null && EContexte.SEROLOGIE.equals(eContexte);
   }
}
