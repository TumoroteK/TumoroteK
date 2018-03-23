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
package fr.aphp.tumorotek.webapp.tree;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * Classe représentant un noeud service d'un arbre contenant les
 * établissements, services et collaborateurs.
 * Classe créée le 16/12/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class ServiceNode extends TumoTreeNode
{

   private Service service;

   public ServiceNode(final Service serv){
      this.service = serv;
   }

   /**
    * Recherche tous les collaborateurs du service et crée un 
    * CollaborateurNode pour chacun.
    */
   @Override
   public void readChildren(){
      final List<Collaborateur> collabs = ManagerLocator.getServiceManager().getCollaborateursWithOrderManager(service);

      children = new ArrayList<>();
      for(int i = 0; i < collabs.size(); i++){
         final CollaborateurNode node = new CollaborateurNode(collabs.get(i));
         children.add(node);
      }
   }

   public Service getService(){
      return service;
   }

   public void setService(final Service s){
      this.service = s;
   }

   /**
    * Ce n'est pas une feuille.
    */
   @Override
   public boolean isLeaf(){

      return false;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final ServiceNode node = (ServiceNode) obj;
      return this.getService().equals(node.getService());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashService = 0;

      if(this.service != null){
         hashService = this.service.hashCode();
      }

      hash = 7 * hash + hashService;

      return hash;
   }

}
