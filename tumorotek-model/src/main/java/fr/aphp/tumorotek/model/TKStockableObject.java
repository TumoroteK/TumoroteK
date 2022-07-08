/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
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
package fr.aphp.tumorotek.model;

import java.util.Calendar;
import java.util.Comparator;

import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 * Interface representant les Entite systeme de TK stockables. Interface créée
 * le 13/08/12.
 *
 * @author julien HUSSON
 * @version 2.0.8
 */
public interface TKStockableObject extends TKAnnotableObject
{

   String getCode();

   void setCode(String s);

   Calendar getDateStock();

   void setDateStock(Calendar cal);

   TKThesaurusObject getType();

   void setType(TKThesaurusObject o);

   Float getQuantite();

   void setQuantite(Float quant);

   Unite getQuantiteUnite();

   void setQuantiteUnite(Unite qUnite);

   Boolean getConformeCession();

   Boolean getConformeTraitement();

   Emplacement getEmplacement();

   void setEmplacement(Emplacement e);

   ObjetStatut getObjetStatut();

   void setObjetStatut(ObjetStatut s);

   /**
    * Comparator permettant d'ordonner une liste de dérivés par leur codes.
    * Date: 27/05/2013
    *
    * Soit compare les ids si compareId = true,
    * Sinon, compare les codes en commencant par le suffixe.
    *
    * @author Mathieu BARTHELEMY
    * @version 2.0.12
    *
    */
   public static class CodeComparator implements Comparator<TKStockableObject>
   {

      private boolean compareId = true;

      //TODO pourquoi toujours à true et non utiliser "c" ?
      public CodeComparator(final boolean c){
         compareId = true;
      }

      @Override
      public int compare(final TKStockableObject p1, final TKStockableObject p2){
         if(compareId && p1.listableObjectId() != null){
            return p1.listableObjectId().compareTo(p2.listableObjectId());
         }
         return compareAfterDot(p1.getCode(), p2.getCode());
      }

      private int compareAfterDot(final String s1, final String s2){
         if(s1.contains(".") && s2.contains(".") && s1.substring(0, s1.indexOf(".")).equals(s2.substring(0, s2.indexOf(".")))){
            try{
               return new Integer(s1.substring(s1.indexOf(".") + 1)).compareTo(new Integer(s2.substring(s2.indexOf(".") + 1)));
            }catch(final NumberFormatException ne){
               return compareAfterDot(s1.substring(s1.indexOf(".") + 1), s2.substring(s2.indexOf(".") + 1));
            }
         }
         return s1.compareTo(s2);
      }
   }
}
