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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Classe 'Decorateur' qui reprend les attributs de Entité.
 * pour les afficher dans la liste associée.
 * Decorator créé le 26/05/2010.
 *
 * @version 2.3.0-gatsbi
 * @author GOUSSEAU Maxime
 *
 */
public class EntiteDecorator
{

   private Entite entite;
   private boolean gatsbi = false;

   public EntiteDecorator(final Entite e){
      this.entite = e;
   }
   
   public EntiteDecorator(final Entite e, final boolean _g){
      this.entite = e;
      this.gatsbi = _g;
   }

   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   public String getNom(){
      return this.entite.getNom();
   }

   public String getLabel(){
      String label = null;
      if (!gatsbi || entite.getEntiteId() != 7) {
         label = Labels.getLabel("Entite." + entite.getNom());
      } else { // gatsbi visite
         label = Labels.getLabel("gatsbi.visite");
      }
      if(null == label || "".equals(label)){
         label = getNom();
      }
      return label;
   }

   /**
    * Decore une liste d'Entites.
    * @param entites
    * @return Entites décorés.
    * @since 2.3.0-gatsbi, modifie Maladie -> Visite dans la décoration
    */
   public static List<EntiteDecorator> decorateListe(final Collection<Entite> entites, final boolean gatsbi){
      final List<EntiteDecorator> liste = new ArrayList<>();
      final Iterator<Entite> it = entites.iterator();
      while(it.hasNext()){
         liste.add(new EntiteDecorator(it.next(), gatsbi));
      }
      return liste;
   }

   /**
    * Extrait les Contrats d'une liste de Decorator.
    * @param Contrats
    * @return Contrats décorés.
    */
   public static List<Entite> extractListe(final List<EntiteDecorator> entites){
      final List<Entite> liste = new ArrayList<>();
      final Iterator<EntiteDecorator> it = entites.iterator();
      while(it.hasNext()){
         liste.add(it.next().getEntite());
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

      final EntiteDecorator deco = (EntiteDecorator) obj;
      return this.getEntite().equals(deco.getEntite());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashEntite = 0;

      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }

      hash = 7 * hash + hashEntite;

      return hash;
   }
}