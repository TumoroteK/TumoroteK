/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2015)
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
package fr.aphp.tumorotek.webapp.general.export;

import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.GroupsModelArray;

import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;

public class TableGroupModel extends GroupsModelArray<TableAnnotation, TableGroupModel.TableInfo, Object, Object>
{
   private static final long serialVersionUID = 1L;

   public TableGroupModel(final List<TableAnnotation> data, final Comparator<TableAnnotation> cmpr){
      super(data.toArray(new TableAnnotation[0]), cmpr);
   }

   @Override
   protected TableInfo createGroupHead(final TableAnnotation[] groupdata, final int index, final int col){
      return new TableInfo(groupdata[0], index, col);
   }

   //		protected String createGroupFoot(TableAnnotation[] groupdata, int index, int col) {
   //			// Return the sum number of each group
   //			return String.valueOf(groupdata.length);
   //		}

   public static class TableInfo
   {
      private final TableAnnotation firstChild;

      private final int groupIndex;

      private final int colIndex;

      public TableInfo(final TableAnnotation firstChild, final int groupIndex, final int colIndex){
         super();
         this.firstChild = firstChild;
         this.groupIndex = groupIndex;
         this.colIndex = colIndex;
      }

      public TableAnnotation getFirstChild(){
         return firstChild;
      }

      public int getGroupIndex(){
         return groupIndex;
      }

      public int getColIndex(){
         return colIndex;
      }
   }
}
