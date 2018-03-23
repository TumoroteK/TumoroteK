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
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 * Classe 'Decorateur' qui reprend les attributs de Entité.
 * pour les afficher dans la liste associée.
 * Decorator créé le 26/05/2010.
 *
 * @version 2.0
 * @author GOUSSEAU Maxime
 *
 */
public class ChampDecorator implements Comparable<Object>
{

   private Champ champ;

   public ChampDecorator(final Champ c){
      this.champ = c;
   }

   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ c){
      this.champ = c;
   }

   public String getLabelLong(){
      String nomEntite = null;
      String nomChamp = null;
      if(this.champ.getChampAnnotation() != null){
         nomEntite = this.champ.getChampAnnotation().getTableAnnotation().getEntite().getNom();
         nomChamp = this.champ.getChampAnnotation().getNom();
         return Labels.getLabel("Entite." + nomEntite) + " - " + nomChamp;
      }else{
         final StringBuffer nom = new StringBuffer();
         if(this.champ.getChampParent() == null){
            nom.append(this.champ.getChampEntite().getEntite().getNom());
            nom.append(" - ");
            nom.append(getLabelForChampEntite(this.champ.getChampEntite()));
         }else{
            nom.append(this.champ.getChampParent().getChampEntite().getEntite().getNom());
            nom.append(" - ");
            nom.append(getLabelForChampEntite(this.champ.getChampParent().getChampEntite()));
         }
         return nom.toString();
      }
   }

   public String getLabelForChampEntite(final ChampEntite c){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      iProperty.append(c.getEntite().getNom());
      iProperty.append(".");

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(c.getNom().endsWith("Id")){
         champOk = c.getNom().substring(0, c.getNom().length() - 2);
      }else{
         champOk = c.getNom();
      }
      iProperty.append(champOk);

      // on ajoute la valeur du champ
      return Labels.getLabel(iProperty.toString());
   }

   public String getLabel(){
      if(champ.getChampAnnotation() != null){
         return champ.nom();
      }else{
         return Labels.getLabel("Champ." + champ.toString());
      }
   }

   /**
    * Decore une liste d'Champs.
    * @param champs
    * @return Champs décorés.
    */
   public static List<ChampDecorator> decorateListe(final List<Champ> champs){
      final List<ChampDecorator> liste = new ArrayList<>();
      final Iterator<Champ> it = champs.iterator();
      while(it.hasNext()){
         liste.add(new ChampDecorator(it.next()));
      }
      return liste;
   }

   /**
    * Extrait les Contrats d'une liste de Decorator.
    * @param Contrats
    * @return Contrats décorés.
    */
   public static List<Champ> extractListe(final List<ChampDecorator> champs){
      final List<Champ> liste = new ArrayList<>();
      final Iterator<ChampDecorator> it = champs.iterator();
      while(it.hasNext()){
         liste.add(it.next().getChamp());
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

      final ChampDecorator deco = (ChampDecorator) obj;
      return this.getChamp().equals(deco.getChamp());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashChamp = 0;

      if(this.champ != null){
         hashChamp = this.champ.hashCode();
      }

      hash = 7 * hash + hashChamp;

      return hash;
   }

   @Override
   public int compareTo(final Object object){
      return compareTo((ChampDecorator) object);
   }

   private int compareTo(final ChampDecorator cd){
      return this.getLabel().compareTo(cd.getLabel());
   }

}
