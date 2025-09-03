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

import java.util.List;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;

/**
 * exception lancée quand la clé fonctionnelle pour une ou plusieurs lignes du fichier d'un import de modification
 * ne seront pas trouvées en base de données
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportKeyNotFoundException extends ImportDataException
{
   private static final long serialVersionUID = 1L;
   
   //nom de la colonne contenant la clé (1ere colonne du fichier d'import)
   private String nomKeyColonne;

   private List<String> listKeyNotFound;

   public ImportKeyNotFoundException(String nomKeyColonne, List<String> listKeyNotFound) {
      super();
      this.nomKeyColonne = nomKeyColonne;
      this.listKeyNotFound = listKeyNotFound;
   }
   
   public List<String> getListKeyNotFound(){
      return listKeyNotFound;
   }
   
   @Override
   public String getMessage() {
      StringBuilder message = new StringBuilder("Import : les codes suivants n'existent pas en base de données : ");
      if(listKeyNotFound != null) {
         message.append(listKeyNotFound.stream().collect(Collectors.joining(", ")));
      }
      
      return message.toString();
   }

   public UIMessage retrieveMessageForHeaderOfFichierCorrections() {
      String[] params = null;
      if(nomKeyColonne != null) {
         params = new String[] {nomKeyColonne};
      }
      return new UIMessage("importTemplate.correctif.error.codeInexistant", params);
   }
   
   @Override
   public int getNbErrors() {
      return listKeyNotFound.size();
   }   

   //pour les exceptions liées aux données, le message par défaut à afficher est le téléchargement du fichier de correction.
   //surcharge du message par défaut (téléchargement du fichier de correction) car le fichier à télécharger est différent 
   //il ne contient que les codes inexistants
   @Override
   protected String getI18nKey() {
      return "importTemplate.dl.correctif.codesInexistants";
   }
   
}
