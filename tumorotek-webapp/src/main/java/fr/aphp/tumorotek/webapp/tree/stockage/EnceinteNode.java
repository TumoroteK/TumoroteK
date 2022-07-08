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
import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud enceinte d'un arbre contenant les
 * stockages.
 * Classe créée le 25/03/10.
 *
 * @author Pierre Ventadour.
 * @version 2.0.
 *
 */
public class EnceinteNode extends TumoTreeNode
{

   private Enceinte enceinte;

   private String libelle = "";

   private Long nbEmplacementsLibres = (long) 0;

   private Long nbEmplacementsOccupes = (long) 0;

   private Float pourcentage = (float) 0;

   private boolean vide;

   private Conteneur conteneur;

   private Integer niveau;

   private Banque selectedBanque;

   public EnceinteNode(final Enceinte enc, final Long nbLibres, final Long nbOccupes, final Banque bank){
      this.enceinte = enc;
      this.selectedBanque = bank;

      if(this.enceinte != null && !this.enceinte.equals(new Enceinte())){
         vide = false;
         if(nbLibres != null){
            nbEmplacementsLibres = nbLibres;
         }else{
            nbEmplacementsLibres = ManagerLocator.getEnceinteManager().getNbEmplacementsLibresByPS(enceinte);
         }

         if(nbOccupes != null){
            nbEmplacementsOccupes = nbOccupes;
         }else{
            nbEmplacementsOccupes = ManagerLocator.getEnceinteManager().getNbEmplacementsOccupesByPS(enceinte);
         }

         final StringBuffer sb = new StringBuffer();
         sb.append(this.enceinte.getNom());
         if(this.enceinte.getAlias() != null && !this.enceinte.getAlias().equals("")){
            sb.append(" {");
            sb.append(this.enceinte.getAlias());
            sb.append("}");
         }
         sb.append(" [");
         sb.append(nbEmplacementsLibres);
         sb.append("]");

         final Long total = nbEmplacementsLibres + nbEmplacementsOccupes;
         if(total != 0){
            final float puiss = nbEmplacementsOccupes.floatValue() * 100;
            pourcentage = puiss / total.floatValue();
            sb.append(" - ");
            sb.append(ObjectTypesFormatters.floor(pourcentage, 1));
            sb.append("%");
         }
         this.libelle = sb.toString();
      }else{
         pourcentage = null;
         this.libelle = Labels.getLabel("stockage.position.libre");
         nbEmplacementsLibres = (long) 0;
         vide = true;
      }
   }

   /**
    * Recherche toutes les enceintes du conteneur et crée un
    * EnceinteNode pour chacun.
    */
   @Override
   public void readChildren(){
      children = new ArrayList<>();

      if(!vide && childrenAvailable()){
         if(ManagerLocator.getEnceinteManager().checkLastEnceinte(enceinte)){
            final List<Terminale> terminales = ManagerLocator.getTerminaleManager().findByEnceinteWithOrderManager(enceinte);

            int cpt = 1;
            for(int i = 0; i < terminales.size(); i++){
               final Terminale term = terminales.get(i);
               // si des positions ne sont pas occupées par des terminales
               // on les remplit par des emplacements vides
               while(term.getPosition() > cpt){
                  if(!isHideComplete()){
                     final TerminaleNode node = new TerminaleNode(new Terminale(), selectedBanque);
                     node.getTerminale().setEnceinte(enceinte);
                     node.getTerminale().setPosition(cpt);
                     node.setConteneur(conteneur);
                     children.add(node);
                  }
                  ++cpt;
               }

               // Terminale non vide
               final TerminaleNode node = new TerminaleNode(term, selectedBanque);
               // n'ajoute pas le node si hideComplete et si la
               // boite n'est pas pleine
               if(!isHideComplete() || node.isNotFull()){
                  // transmet l'information hideComplete au noeuds
                  children.add(node);
                  node.setConteneur(conteneur);
               }
               ++cpt;
            }

            // si des positions ne sont pas occupées par des terminales
            // on les remplit par des emplacements vides
            if(!isHideComplete()){
               while(children.size() < this.enceinte.getNbPlaces()){
                  final TerminaleNode node = new TerminaleNode(new Terminale(), selectedBanque);
                  node.getTerminale().setEnceinte(enceinte);
                  node.getTerminale().setPosition(cpt);
                  node.setConteneur(conteneur);
                  ++cpt;
                  children.add(node);
               }
            }

         }else{
            final List<Enceinte> enceintes = ManagerLocator.getEnceinteManager().findByEnceintePereWithOrderManager(enceinte);

            int cpt = 1;
            for(int i = 0; i < enceintes.size(); i++){
               final Enceinte enc = enceintes.get(i);
               // si des positions ne sont pas occupées par des enceintes
               // on les remplit par des emplacements vides
               while(enc.getPosition() > cpt){
                  if(!isHideComplete()){
                     final EnceinteNode node = new EnceinteNode(new Enceinte(), null, null, selectedBanque);
                     node.getEnceinte().setEnceintePere(enceinte);
                     node.getEnceinte().setConteneur(null);
                     node.getEnceinte().setPosition(cpt);
                     node.setConteneur(conteneur);
                     node.setNiveau(niveau + 1);
                     children.add(node);
                  }
                  ++cpt;
               }

               // Enceinte non vide
               final EnceinteNode node = new EnceinteNode(enc, null, null, selectedBanque);
               // n'ajoute pas le node si hideComplete et nb libres
               // égal à 0
               if(!isHideComplete() || node.getNbEmplacementsLibres() > 0){
                  node.setConteneur(conteneur);
                  node.setNiveau(niveau + 1);
                  node.setHideComplete(isHideComplete());
                  children.add(node);
               }
               ++cpt;
            }

            // si des positions ne sont pas occupées par des enceintes
            // on les remplit par des emplacements vides
            if(!isHideComplete()){
               while(children.size() < this.enceinte.getNbPlaces()){
                  final EnceinteNode node = new EnceinteNode(new Enceinte(), null, null, selectedBanque);
                  node.getEnceinte().setEnceintePere(enceinte);
                  node.getEnceinte().setConteneur(null);
                  node.getEnceinte().setPosition(cpt);
                  node.setConteneur(conteneur);
                  node.setNiveau(niveau + 1);
                  children.add(node);
                  ++cpt;
               }
            }
         }
      }
   }

   /**
    * Vérifie si l'enceinte est accessible (et donc ses enfants) pour la
    * banque sélectionnée.
    * @return True si les enfants sont accessibles.
    */
   public boolean childrenAvailable(){
      boolean ok = true;

      final Set<Banque> banks = ManagerLocator.getEnceinteManager().getBanquesManager(enceinte);
      if(banks.size() > 0 && !banks.contains(selectedBanque)){
         ok = false;
      }

      return ok;
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

      final EnceinteNode node = (EnceinteNode) obj;
      return this.enceinte.equals(node.getEnceinte());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEnceinte = 0;

      if(this.enceinte != null){
         hashEnceinte = this.enceinte.hashCode();
      }

      hash = 7 * hash + hashEnceinte;

      return hash;
   }

   @Override
   public String toString(){
      return enceinte.toString();
   }

   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte e){
      this.enceinte = e;
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

   public Long getNbEmplacementsLibres(){
      return nbEmplacementsLibres;
   }

   public void setNbEmplacementsLibres(final Long nbLibres){
      this.nbEmplacementsLibres = nbLibres;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public Integer getNiveau(){
      return niveau;
   }

   public void setNiveau(final Integer nv){
      this.niveau = nv;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

   public Long getNbEmplacementsOccupes(){
      return nbEmplacementsOccupes;
   }

   public void setNbEmplacementsOccupes(final Long nbOccupes){
      this.nbEmplacementsOccupes = nbOccupes;
   }

   public Float getPourcentage(){
      return pourcentage;
   }

   public void setPourcentage(final Float p){
      this.pourcentage = p;
   }

}
