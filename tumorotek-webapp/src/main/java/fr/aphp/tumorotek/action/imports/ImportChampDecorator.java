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
package fr.aphp.tumorotek.action.imports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class ImportChampDecorator
{

   private Champ champ;

   public ImportChampDecorator(final Champ chp){
      this.champ = chp;
   }

   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ c){
      this.champ = c;
   }

   public String getNom(){
      String nom = "";
      if(champ.getChampEntite() != null){
         nom = getLabelForChampEntite(champ.getChampEntite());
      }else if(champ.getChampAnnotation() != null){
         nom = champ.getChampAnnotation().getNom();
      }else if(champ.getChampDelegue() != null){
         nom = getLabelForChampDelegue(champ.getChampDelegue());
      }
      return nom;
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

   public String getLabelForChampDelegue(final ChampDelegue c){
      return Labels.getLabel(c.getILNLabelForChampDelegue(SessionUtils.getCurrentContexte()));
   }

   /**
    * Decore une liste de cederobjets.
    * @param cederobjets
    * @return CederObjets décorées.
    */
   public static List<ImportChampDecorator> decorateListe(final List<Champ> objets){
      final List<ImportChampDecorator> liste = new ArrayList<>();
      final Iterator<Champ> it = objets.iterator();
      while(it.hasNext()){
         liste.add(new ImportChampDecorator(it.next()));
      }
      return liste;
   }

   /**
    * Extrait les CederObjets d'une liste de Decorator.
    * @param CederObjets
    * @return CederObjets décorés.
    */
   public static List<Champ> extractListe(final List<ImportChampDecorator> cedes){
      final List<Champ> liste = new ArrayList<>();
      final Iterator<ImportChampDecorator> it = cedes.iterator();

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

      final ImportChampDecorator deco = (ImportChampDecorator) obj;
      return this.getChamp().equals(deco.getChamp());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashChamp = 0;

      if(this.getChamp() != null){
         hashChamp = this.getChamp().hashCode();
      }

      hash = 7 * hash + hashChamp;

      return hash;
   }

}
