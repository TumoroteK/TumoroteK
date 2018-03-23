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
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud conteneur d'un arbre contenant les
 * stockages.
 * Classe créée le 25/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class ConteneurNode extends TumoTreeNode
{

   private Conteneur conteneur;
   private String libelle = "";
   private Banque selectedBanque;

   public ConteneurNode(final Conteneur cont, final Banque banque){
      this.conteneur = cont;
      this.selectedBanque = banque;
      if(this.conteneur != null && !this.conteneur.equals(new Conteneur())){
         if(this.conteneur.getNom() != null){
            this.libelle = this.conteneur.getNom();
         }else{
            this.libelle = this.conteneur.getCode();
         }
      }else{
         this.libelle = "(Vide)";
      }
   }

   /**
    * Recherche toutes les enceintes du conteneur et crée un 
    * EnceinteNode pour chacun.
    */
   @Override
   public void readChildren(){
      children = new ArrayList<>();
      final List<Enceinte> enList = ManagerLocator.getEnceinteManager().findByConteneurWithOrderManager(conteneur);

      int cpt = 1;
      // on parcourt tous les enfants
      for(int i = 0; i < enList.size(); i++){
         final Enceinte enc = enList.get(i);
         // si des positions ne sont pas occupées par des enceintes
         // on les remplit par des emplacements vides
         while(enc.getPosition() > cpt){
            if(!isHideComplete()){
               final EnceinteNode node = new EnceinteNode(new Enceinte(), null, null, selectedBanque);
               node.getEnceinte().setConteneur(conteneur);
               node.getEnceinte().setEnceintePere(null);
               node.getEnceinte().setPosition(cpt);
               node.setConteneur(conteneur);
               node.setNiveau(1);
               children.add(node);
            }
            ++cpt;
         }

         // Enceinte non vide
         final EnceinteNode node = new EnceinteNode(enc, null, null, selectedBanque);
         node.setConteneur(conteneur);
         node.setNiveau(1);

         // n'ajoute pas le node si hideComplete et nb libres
         // égal à 0
         if(!isHideComplete() || node.getNbEmplacementsLibres() > 0){
            // transmet l'information hideComplete au noeuds
            node.setHideComplete(isHideComplete());
            children.add(node);
         }
         ++cpt;
      }

      if(!isHideComplete()){
         // si des positions ne sont pas occupées par des enceintes
         // on les remplit par des emplacements vides
         while(children.size() < this.conteneur.getNbrEnc()){
            final EnceinteNode node = new EnceinteNode(new Enceinte(), null, null, selectedBanque);
            node.getEnceinte().setConteneur(conteneur);
            node.getEnceinte().setEnceintePere(null);
            node.getEnceinte().setPosition(cpt);
            node.setConteneur(conteneur);
            node.setNiveau(1);
            ++cpt;
            children.add(node);
         }
      }
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

      final ConteneurNode node = (ConteneurNode) obj;
      return this.conteneur.equals(node.getConteneur());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashConteneur = 0;

      if(this.conteneur != null){
         hashConteneur = this.conteneur.hashCode();
      }

      hash = 7 * hash + hashConteneur;

      return hash;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public String getLibelle(){
      return libelle;
   }

   public void setLibelle(final String lib){
      this.libelle = lib;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

}
