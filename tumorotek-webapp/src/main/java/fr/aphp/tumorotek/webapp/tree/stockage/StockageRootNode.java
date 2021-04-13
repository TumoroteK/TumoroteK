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
package fr.aphp.tumorotek.webapp.tree.stockage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant le noeud root d'un arbre contenant les
 * conteneurs.
 * Classe créée le 25/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.2.3-genno
 *
 */
public class StockageRootNode extends TumoTreeNode
{

   private List<Banque> banques = new ArrayList<>();
   // @since 2.1
   private final List<Conteneur> conteneurs = new ArrayList<>();

   // @since 2.2.3-genno
   // TK-289
   private final Plateforme curPf;
   
   public StockageRootNode(final List<Banque> banks, Plateforme _f){
      this.banques = banks;
      this.curPf = _f;
      
      conteneurs.addAll(ManagerLocator.getConteneurManager().findByBanquesWithOrderManager(banques));
   }

   @Override
   public boolean isLeaf(){
      return false;
   }

   /**
    * Recherche tous les conteneurs et crée un ConteneurNode
    * pour chacun.
    */
   @Override
   public void readChildren(){

      Collections.sort(conteneurs, new Conteneur.NomComparator());

      children = new ArrayList<>();
      for(int i = 0; i < conteneurs.size(); i++){
         Banque banque = null;
         if(banques.size() == 1){
            banque = banques.get(0);
         }
         final ConteneurNode node = new ConteneurNode(conteneurs.get(i), banque, curPf);
         // transmet l'information hideComplete au noeuds
         node.setHideComplete(isHideComplete());
         children.add(node);
      }
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   // @since 2.1
   public List<Conteneur> getConteneurs(){
      return conteneurs;
   }
}
