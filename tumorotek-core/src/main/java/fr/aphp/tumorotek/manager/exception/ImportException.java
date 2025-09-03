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

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;

/**
 * exception parent des exceptions liées aux imports.
 * Elle est abstraite : les classes filles doivent définir la clé du message internationalisé à afficher 
 * à l'utilisateur pour le problème associé à l'exception
 *  
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public abstract class ImportException extends Exception
{
   private static final long serialVersionUID = 1L;

   protected ImportException() {
      super();
   }
   
   protected ImportException(String message) {
      super(message);
   }

   protected ImportException(String message, Throwable cause) {
      super(message, cause);
   }   
   
   /**
    * @return la clé du message internationalisé associé à l'exception
    */
   abstract protected String getI18nKey();
   
   /**
    * @return l'UIMessage associé à la clé internationalisée récupérée par {@link #getI18nKey()}.
    * A utiliser dans le cas d'un message ne contenant pas de partie variable
    */
   public UIMessage buildUIMessage() {
      return buildUIMessage(null);
   }
   
   /**
    * A utiliser dans le cas d'un message contenant une ou plusieurs parties variables
    * @return l'{@link UIMessage} associé à la clé internationalisée récupérée par {@link #getI18nKey()} en prenant en compte 
    * les valeurs passées en paramètres pour renseigner les parties variables (tokens)
    */
   protected UIMessage buildUIMessage(String[] params) {
      if(getI18nKey() != null) {
         return new UIMessage(getI18nKey(), params);
      }
      return null;
   }
}
