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

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud terminale d'un arbre contenant les
 * stockages.
 * Classe créée le 25/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class TerminaleNode extends TumoTreeNode
{

   private Terminale terminale;
   private String libelle = "";
   private boolean vide;
   private Conteneur conteneur;
   private Banque selectedBanque;

   public TerminaleNode(final Terminale term, final Banque banque){
      this.terminale = term;
      this.selectedBanque = banque;

      if(this.terminale != null && !this.terminale.equals(new Terminale())){
         final StringBuffer sb = new StringBuffer();
         sb.append(this.terminale.getNom());
         sb.append(" [");
         /*sb.append(ManagerLocator.getTerminaleManager()
         		.getNumberEmplacementsLibres(terminale));*/
         sb.append("]");
         this.libelle = sb.toString();
         this.vide = false;
      }else{
         this.libelle = Labels.getLabel("stockage.position.libre");
         this.vide = true;
      }
   }

   /**
    * Teste si la terminale est accessible pour la banque sélectionnée.
    */
   public Boolean availableTerminale(){
      boolean ok = true;

      if(terminale.getBanque() != null && !terminale.getBanque().equals(selectedBanque)){
         ok = false;
      }

      return ok;
   }

   public boolean isNotFull(){
      System.out.println(ManagerLocator.getTerminaleManager().getNumberEmplacementsLibresManager(terminale));
      return ManagerLocator.getTerminaleManager().getNumberEmplacementsLibresManager(terminale) > 0;
   }

   /**
    * C'est une feuille.
    */
   @Override
   public boolean isLeaf(){

      return true;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final TerminaleNode node = (TerminaleNode) obj;
      return this.terminale.equals(node.getTerminale());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashTerminale = 0;

      if(this.terminale != null){
         hashTerminale = this.terminale.hashCode();
      }

      hash = 7 * hash + hashTerminale;

      return hash;
   }

   @Override
   public void readChildren(){}

   public Terminale getTerminale(){
      return terminale;
   }

   public void setTerminale(final Terminale t){
      this.terminale = t;
   }

   public String getLibelle(){
      return libelle;
   }

   public void setLibelle(final String lib){
      this.libelle = lib;
   }

   public boolean isVide(){
      return vide;
   }

   public void setVide(final boolean v){
      this.vide = v;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

}
