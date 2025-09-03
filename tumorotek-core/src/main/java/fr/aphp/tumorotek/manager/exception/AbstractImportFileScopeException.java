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
 * Pour le traitement des imports, exception parent des exceptions liées à un fichier (permet de remonter
 * une erreur liée aux prérequis ou toutes les erreurs rencontrées lors de la lecture des lignes du fichier)
 * 
 * @since 2.3.1 (TK-538 : import de mise à jour des annotations)
 * @author chuet
 *
 */
public abstract class AbstractImportFileScopeException extends ImportException implements ImportFileScopeException
{

   private static final long serialVersionUID = 1L;
   
   protected AbstractImportFileScopeException() {
      super();
   }
   
   protected AbstractImportFileScopeException(String message) {
      super(message);
   }

   protected AbstractImportFileScopeException(String message, Throwable cause) {
      super(message, cause);
   }
   
   /**
    * renvoie un {@link UIMessage} pour le message internationalisé à afficher en gras en haut de la popup de résultat (après l'image Avertissement)
    * peut être surchargé dans des cas spécifiques comme le plantage lors d'une transaction
    * A noter que généralement, le nom du message contient en paramètre le nom de l'onglet excel traité. Celui-ci est pris en compte automatiquement côté front
    * sans avoir à le passer en paramètre à ce niveau 
    * @return UIMessage
    */
   @Override
   public UIMessage retrieveUIMessageForMainMessage() {
      return new UIMessage("importTemplate.resultats.warn.title", null);
   }
   
   /**
    * clé de message internationalisé à utiliser pour le bouton de téléchargement du détail des erreurs.
    * Valeur par défaut qui peut être surchargée notamment dans le cas du plantage d'une transaction ou du warning concernant l'écrasement de données
    * @return
    */
   @Override
   public String retrieveI18KeyForDownloadButton() {
      return "importTemplate.dl.correctif.button";
   }
   
   /**
    * quand l'exception correspond à un warning (exemple : le risque d'écrasement de données), on peut afficher des boutons demandant 
    * à l'utilisateur de continuer ou pas sinon, on ne les affiche pas (cas le plus courant)
    */
   @Override
   public boolean isWarning() {
      return false;
   }
}
