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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * Decore le conteneur afin de distinguer ceux dont la plateforme
 * courante est la plateforme d'origine.
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.10
 * @since 2.0.10
 */
public class ConteneurDecorator
{

   private Conteneur conteneur;
   private Plateforme current;
   private Boolean deleteHeaderVisible;

   public ConteneurDecorator(final Conteneur c, final Plateforme current){
      setConteneur(c);
      setCurrent(current);
      setDeleteHeaderVisible(current != null ? !current.equals(c.getPlateformeOrig()) : true);
   }

   public Boolean getDeleteHeaderVisible(){
      return deleteHeaderVisible;
   }

   public void setDeleteHeaderVisible(final Boolean i){
      this.deleteHeaderVisible = i;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public Plateforme getCurrent(){
      return current;
   }

   public void setCurrent(final Plateforme current){
      this.current = current;
   }

   public static List<ConteneurDecorator> decorateListe(final List<Conteneur> conts, final Plateforme current){
      List<ConteneurDecorator> decos = null;

      if(conts != null){
         decos = new ArrayList<>();
         for(final Conteneur c : conts){
            decos.add(new ConteneurDecorator(c, current));
         }
      }

      return decos;
   }

   @Override
   public ConteneurDecorator clone(){
      return new ConteneurDecorator(getConteneur(), getCurrent());
   }

   /**
    * Récupère les conteneurs à passer dans la méthode d'update afin de 
    * modifier les relations Conteneur_Plateforme.
    * Exclue de la liste tous les conteneurs dont la plateforme de création 
    * correspond à la plateforme en cours
    * @param decos
    * @return List<Conteneur>
    */
   public static List<Conteneur> extractConteneursFromDecos(final List<ConteneurDecorator> decos){
      List<Conteneur> conts = null;
      if(decos != null){
         conts = new ArrayList<>();
         for(final ConteneurDecorator deco : decos){
            if(deco.getDeleteHeaderVisible()){
               conts.add(deco.getConteneur());
            }
         }
      }
      return conts;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ConteneurDecorator test = (ConteneurDecorator) obj;
      return ((this.current == test.current || (this.current != null && this.current.equals(test.current)))
         && (this.conteneur == test.conteneur || (this.conteneur != null && this.conteneur.equals(test.conteneur))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashConteneur = 0;
      int hashPf = 0;

      if(this.conteneur != null){
         hashConteneur = this.conteneur.hashCode();
      }
      if(this.current != null){
         hashPf = this.current.hashCode();
      }

      hash = 31 * hash + hashConteneur;
      hash = 31 * hash + hashPf;

      return hash;
   }
}
