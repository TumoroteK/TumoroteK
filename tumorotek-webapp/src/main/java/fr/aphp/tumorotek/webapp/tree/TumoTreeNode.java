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

/**
 * Classe abstraite représentant un noeud d'un arbre TumoTreeModel.
 * Classe créée le 16/12/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public abstract class TumoTreeNode
{

   private boolean hideComplete = false;

   public boolean isHideComplete(){
      return hideComplete;
   }

   public void setHideComplete(final boolean hideComplete){
      this.hideComplete = hideComplete;
   }

   /**
    * Liste des enfants du noeud.
    */
   protected ArrayList<TumoTreeNode> children = null;

   public void setChildren(final ArrayList<TumoTreeNode> c){
      this.children = c;
   }

   /**
    * Méthode remplissant la liste children.
    */
   public abstract void readChildren();
   
   public ArrayList<TumoTreeNode> getChildren(){
      return children;
   }
   

   /**
    * Renvoie un enfant du noeud.
    * @param arg1 Indice de l'enfant recherché.
    * @return Noeud enfant.
    */
   public TumoTreeNode getChild(final int arg1){
      TumoTreeNode child = null;

      if(children == null){
         readChildren();
      }

      if(children != null && (arg1 > -1 && arg1 < children.size())){
         child = children.get(arg1);
      }

      return child;
   }

   /**
    * @return Nombre d'enfant du noeud. 
    */
   public int getChildCount(){
      if(children == null){
         readChildren();
      }

      if(children != null){
         return children.size();
      }

      return 0;
   }

   /**
    * Méthode abstraite pour savoir si le noeud est une feuille ou non.
    * @return True si le noeud est une feuille.
    */
   public abstract boolean isLeaf();

}
