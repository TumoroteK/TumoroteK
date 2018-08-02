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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;

public class LigneEtiquetteDecorator
{

   private LigneEtiquette ligneEtiquette;
   private List<ChampLigneEtiquette> champs = new ArrayList<>();
   private List<ChampLigneEtiquette> champsToRemove = new ArrayList<>();

   public LigneEtiquetteDecorator(final LigneEtiquette le, final List<ChampLigneEtiquette> c){
      ligneEtiquette = le;

      champs = new ArrayList<>();
      if(c != null){
         champs = c;
      }else{
         if(le != null && le.getLigneEtiquetteId() != null){
            champs = ManagerLocator.getChampLigneEtiquetteManager().findByLigneEtiquetteManager(ligneEtiquette);
         }
      }
   }

   public LigneEtiquette getLigneEtiquette(){
      return ligneEtiquette;
   }

   public void setLigneEtiquette(final LigneEtiquette l){
      this.ligneEtiquette = l;
   }

   public List<ChampLigneEtiquette> getChamps(){
      return champs;
   }

   public void setChamps(final List<ChampLigneEtiquette> c){
      this.champs = c;
   }

   /**
    * Decore une liste de cederobjets.
    * @param cederobjets
    * @return CederObjets décorées.
    */
   public static List<LigneEtiquetteDecorator> decorateListe(final List<LigneEtiquette> objets){
      final List<LigneEtiquetteDecorator> liste = new ArrayList<>();
      final Iterator<LigneEtiquette> it = objets.iterator();
      while(it.hasNext()){
         liste.add(new LigneEtiquetteDecorator(it.next(), null));
      }
      return liste;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final LigneEtiquetteDecorator deco = (LigneEtiquetteDecorator) obj;
      return this.getLigneEtiquette().equals(deco.getLigneEtiquette());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashLigne = 0;

      if(this.getLigneEtiquette() != null){
         hashLigne = this.getLigneEtiquette().hashCode();
      }

      hash = 7 * hash + hashLigne;

      return hash;
   }

   public List<ChampLigneEtiquette> getChampsToRemove(){
      return champsToRemove;
   }

   public void setChampsToRemove(final List<ChampLigneEtiquette> c){
      this.champsToRemove = c;
   }

}
