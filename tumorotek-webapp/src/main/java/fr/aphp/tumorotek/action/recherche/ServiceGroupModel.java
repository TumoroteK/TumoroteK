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
package fr.aphp.tumorotek.action.recherche;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.GroupsModelArray;

import fr.aphp.tumorotek.model.contexte.Service;

public class ServiceGroupModel extends GroupsModelArray<Service, ServiceGroupModel.ServiceInfo, Object, Object>
{
   private static final long serialVersionUID = 1L;

   public ServiceGroupModel(final List<Service> data, final Comparator<Service> cmpr){
      super(data.toArray(new Service[0]), cmpr);
   }

   @Override
   protected ServiceInfo createGroupHead(final Service[] groupdata, final int index, final int col){
      return new ServiceInfo(groupdata[0], index, col);
   }

   //		protected String createGroupFoot(TableAnnotation[] groupdata, int index, int col) {
   //			// Return the sum number of each group
   //			return String.valueOf(groupdata.length);
   //		}

   public static class ServiceInfo
   {
      private final Service firstChild;
      private final int groupIndex;
      private final int colIndex;

      public ServiceInfo(final Service firstChild, final int groupIndex, final int colIndex){
         super();
         this.firstChild = firstChild;
         this.groupIndex = groupIndex;
         this.colIndex = colIndex;
      }

      public Service getFirstChild(){
         return firstChild;
      }

      public int getGroupIndex(){
         return groupIndex;
      }

      public int getColIndex(){
         return colIndex;
      }
   }

   public List<Service> getSelectedServices(){
      final List<Service> servs = new ArrayList<>();
      for(final Object obj : super.getSelection()){
         if(obj instanceof Service){
            if(((Service) obj).getServiceId() != null){
               servs.add((Service) obj);
            }else{
               servs.add(new Service());
            }
         }
      }
      return servs;
   }

}
