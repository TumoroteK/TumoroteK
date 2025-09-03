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
import fr.aphp.tumorotek.model.io.imports.ImportColonne;

/**
 * exception lancée quand on veut remonter un warning à l'utilisateur concernant une valeur existante qui doit être mise à jour.
 * Dans ce cas, l'import est fait "en mode supervision" 
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportEcrasementDonneeWarningForCellException extends AbstractImportCellScopeException
{
   private static final long serialVersionUID = 1L;

   private String oldValue;
   private String newValue;
   
   public ImportEcrasementDonneeWarningForCellException(ImportColonne col, String oldValue, String newValue) {
      super(col);
      this.oldValue=oldValue;
      this.newValue=newValue;
   }
   
   @Override
   protected String getI18nKey() {
      return "importTemplate.correctif.warning.ecrasement";
   }

   
   //message utilisé dans le module d'import pour la création (module d'import historique) : le message
   //affiché à l'utilisateur n'est pas internationalisé
   @Override
   public String getMessage(){
      return getColonne().getNom() + ": valeur existante, " + oldValue + " à remplacer par " + newValue;
   }

   //internationalisation du message à afficher à l'utilisateur : introduit avec l'évolution TK-538 
   //concernant les imports pour modifier les annotations
   @Override
   public UIMessage buildUIMessage() {
      return buildUIMessage(new String[] {getColonne().getNom(), oldValue, newValue});
   }

}
