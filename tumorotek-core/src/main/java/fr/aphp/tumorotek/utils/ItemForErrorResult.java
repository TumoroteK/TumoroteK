/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
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
package fr.aphp.tumorotek.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * classe générique qui permet de définir un objet pour afficher un message à l'utilisateur suite à une erreur de validation.
 * Cette classe comprend :
 *  - un message explicitant l'erreur, représenté par une clé d'internationalisation
 *  - une liste des données en erreur pour explicter le message
 *  - un dernier message proposant une solution à l'utilisateur
 * @author chuet
 *
 * @param <T> classe des objets contenu dans la liste des données explictant le message d'erreur
 */
public class ItemForErrorResult<T>
{
   
   private String erreurKeyI18n = null;
   private List<T> listDataInError = new ArrayList<T>();
   private String todoKeyI18n = null;
   
   public ItemForErrorResult(String erreurKeyI18n, List<T> listDataInError, String todoKeyI18n) {
      this.erreurKeyI18n = erreurKeyI18n;
      this.listDataInError = listDataInError;
      this.todoKeyI18n = todoKeyI18n;
   }

   public String getErreurKeyI18n(){
      return erreurKeyI18n;
   }

   public List<T> getListDataInError(){
      return listDataInError;
   }

   public String getTodoKeyI18n(){
      return todoKeyI18n;
   }

   public void setTodoKeyI18n(String todoKeyI18n){
      this.todoKeyI18n = todoKeyI18n;
   }

}
