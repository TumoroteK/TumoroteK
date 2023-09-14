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

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.stockage.Enceinte;

public class EnceinteDecorator
{

   private Enceinte enceinte;

   private Integer nbNiveau;

   private Boolean isTerminale;

   private Integer sizeVisoGobeletMarguerite = null;

   private Integer nbPlaces;

   private Integer firstPosition;

   private Boolean disabled = false;

   public EnceinteDecorator(final Enceinte enc){
      this.enceinte = enc;
   }

   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte e){
      this.enceinte = e;
   }

   public Integer getNbNiveau(){
      return nbNiveau;
   }

   public void setNbNiveau(final Integer nb){
      this.nbNiveau = nb;
   }

   public Boolean getIsTerminale(){
      return isTerminale;
   }

   public void setIsTerminale(final Boolean isTerm){
      this.isTerminale = isTerm;
   }

   public Integer getNbPlaces(){
      return nbPlaces;
   }

   public void setNbPlaces(final Integer nb){
      this.nbPlaces = nb;
   }

   public Integer getFirstPosition(){
      return firstPosition;
   }

   public void setFirstPosition(final Integer first){
      this.firstPosition = first;
   }

   public boolean getVisibleListEnceinte(){
      return !this.isTerminale && this.sizeVisoGobeletMarguerite == null;
   }

   public boolean getVisibleLabelEnceinte(){
      return this.isTerminale || this.sizeVisoGobeletMarguerite != null;
   }

   public String getLabelEnceinte(){
      if(this.isTerminale && this.sizeVisoGobeletMarguerite != null){
         return Labels.getLabel("conteneur.enceinte.type.visotube") + " " + this.sizeVisoGobeletMarguerite.toString();
      }else if(this.isTerminale){
         return Labels.getLabel("conteneur.enceinte.type.boite");
      }else if(this.sizeVisoGobeletMarguerite != null){
         return Labels.getLabel("conteneur.enceinte.type.marguerite");
      }else{
         return null;
      }
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final EnceinteDecorator deco = (EnceinteDecorator) obj;
      return this.getEnceinte().equals(deco.getEnceinte());

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

   public Boolean getDisabled(){
      return disabled;
   }

   public void setDisabled(final Boolean d){
      this.disabled = d;
   }

   public Integer geSizeVisoGobeletMarguerite(){
      return sizeVisoGobeletMarguerite;
   }

   public void setSizeVisoGobeletMarguerite(final Integer is){
      this.sizeVisoGobeletMarguerite = is;
   }

}
