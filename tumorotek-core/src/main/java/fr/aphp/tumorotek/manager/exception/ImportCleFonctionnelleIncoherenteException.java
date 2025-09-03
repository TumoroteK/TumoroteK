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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRow;
import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;

/**
 * dans le cas où des colonnes de contrôles sont renseignées par l'utilisateur pour sécuriser les codes, 
 * cette classe d'exception sera utilisée si une ou plusieurs données renseignées ne correspond pas à la valeur en base de données
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class ImportCleFonctionnelleIncoherenteException extends ImportDataException implements ImportExceptionWithDetailByRowInterface
{
   private static final long serialVersionUID = 1L;
   
   private static final String I18N_KEY__CORRECTIF_CLE_FONCTIONNELLE_INCORRECTE = "importTemplate.correctif.error.codeIncorrect";

   //nom de la colonne contenant le code fonctionnel (1ere colonne du fichier d'import) : 
   private String nomKeyColonne;
   private String nomColonneForControle;
   //liste des codes. N'est utilisé que pour tracer un message explicite dans les logs
   private List<String> listCode = new ArrayList<String>();
   private List<UIMessageForRowInterface> listUIMessageByRow = new ArrayList<UIMessageForRowInterface>();
   
   public ImportCleFonctionnelleIncoherenteException(String nomKeyColonne, String colonneForControle) {
      super();
      this.nomKeyColonne = nomKeyColonne;
      this.nomColonneForControle = colonneForControle;
   }
   
   public void addErrors(Integer numRow, String code, String valeurSaisie, String valeurInBdd) {
      listCode.add(code);
      listUIMessageByRow.add(new UIMessageForRow(numRow, I18N_KEY__CORRECTIF_CLE_FONCTIONNELLE_INCORRECTE, 
         new String[] {nomKeyColonne, code, valeurSaisie, valeurInBdd, nomColonneForControle}));
   }
   
   @Override
   public String getMessage() {
      StringBuilder message = new StringBuilder("Import : les codes suivants sont incohérents avec les valeurs de contrôle : ");
      if(listCode != null) {
         message.append(listCode.stream().map(code -> String.valueOf(code)).collect(Collectors.joining(", ")));
      }
      
      return message.toString();
   }
   
   @Override
   public int getNbErrors(){
      return listUIMessageByRow.size();
   }

   @Override
   public int getNbImportedRows() {
      return 0;
   }
   
   //pour cette exception, les UIMessageForRowInterface sont créés au moment de la détection de l'erreur (pour faciliter la gestion 
   //des paramètres du message qui sont nombreux). La liste est donc alimentée au fur et à mesure. 
   //Par conséquent, la méthode buildSortedUIMessageForRow() ne fait que renvoyer cette liste
   @Override
   public List<UIMessageForRowInterface> buildSortedUIMessageForRow() {
      return listUIMessageByRow;
   }
   
}
