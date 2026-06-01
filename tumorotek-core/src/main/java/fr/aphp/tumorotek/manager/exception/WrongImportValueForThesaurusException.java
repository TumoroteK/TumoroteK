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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessage;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.utils.Utils;

/**
 * exception lancée quand la valeur renseignée pour un champ de type thesaurus ne correspond pas aux valeurs définies
 * pour ce thesaurus
 * A noter que les valeurs sont acceptées quelque soit la casse
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class WrongImportValueForThesaurusException extends AbstractImportCellScopeException
{

   private static final long serialVersionUID = 1L;
   
   //Pour éviter des problème d'affichage dans une cellule excel (TK-873), on n'affichera à l'utilisateur que les 35 premières valeurs du thesaurus
   public static final int MAX_VALEUR_AUTORISEE_AFFICHEE = 35;
   public static final String SEPARATEUR_VALEUR_AUTORISEE_AFFICHEE = ", ";
   
   private List<String> listValeurAutorisee = null;
   
   public WrongImportValueForThesaurusException(){
      super();
   }

   public WrongImportValueForThesaurusException(final ImportColonne col, final Set<String> listValeurAutorisee){
      super(col);
      this.listValeurAutorisee=new ArrayList<String>(listValeurAutorisee);
      //tri de la liste par ordre alphabétique.
      Collections.sort(this.listValeurAutorisee);
   }

   @Override
   protected String getI18nKey() {
      return "validation.wrong.import.thesaurus";
   }
   
   @Override
   public String getMessage(){
      return getColonne().getNom() + ": Erreur de validation, valeur de thésaurus inconnue.";
   }

   @Override
   public UIMessage buildUIMessage() {
      //TK-873 : on ne retourne que les x premières valeurs pour éviter un problème d'affichage dans excel
      return buildUIMessage(new String[] {getColonne().getNom(), 
                                             Utils.convertListToStringWithTruncationIfNecessary(listValeurAutorisee, 
                                             SEPARATEUR_VALEUR_AUTORISEE_AFFICHEE, MAX_VALEUR_AUTORISEE_AFFICHEE)});
   }
   
   protected List<String> getListValeurAutorisee(){
      return listValeurAutorisee;
   }
}
