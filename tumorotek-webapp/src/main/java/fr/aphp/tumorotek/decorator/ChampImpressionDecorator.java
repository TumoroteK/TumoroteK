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

import java.util.Objects;

import fr.aphp.tumorotek.model.io.export.ChampEntite;

public class ChampImpressionDecorator
{

   private ChampEntite champEntite;

   private Boolean imprimer = true;

   public ChampImpressionDecorator(final ChampEntite champ){
      this.champEntite = champ;
   }

   public ChampEntite getChampEntite(){
      return champEntite;
   }

   public void setChampEntite(final ChampEntite champE){
      this.champEntite = champE;
   }

   public Boolean getImprimer(){
      return imprimer;
   }

   public void setImprimer(final Boolean imp){
      this.imprimer = imp;
   }

   public String getNom(){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      iProperty.append(champEntite.getEntite().getNom());
      iProperty.append(".");

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(champEntite.getNom().endsWith("Id")){
         champOk = champEntite.getNom().substring(0, champEntite.getNom().length() - 2);
      }else{
         champOk = champEntite.getNom();
      }
      iProperty.append(champOk);

      return iProperty.toString();
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ChampImpressionDecorator test = (ChampImpressionDecorator) obj;
      return Objects.equals(test.getChampEntite(), champEntite);
   }

   @Override
   public int hashCode(){
      int hash = 7;
      int hashChampEntite = 0;

      if(this.champEntite != null){
         hashChampEntite = this.champEntite.hashCode();
      }

      hash = 7 * hash + hashChampEntite;

      return hash;
   }

}
