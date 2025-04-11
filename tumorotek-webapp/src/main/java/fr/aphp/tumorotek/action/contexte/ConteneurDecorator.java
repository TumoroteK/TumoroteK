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

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * Decore le conteneur pour lui appliquer des caractérisitiques liées à la plateforme du cas d'utilisation
 * En effet un conteneur peut être partagé par une autre plateforme ce qui amène des règles de gestion particulières
 * notamment concernant :
 * - la suppression
 * - l'affichage du libellé du conteneur
 *
 * @author mathieu BARTHELEMY
 * @version 2.0.10
 * @since 2.0.10
 */
public class ConteneurDecorator
{

   private Conteneur conteneur;

   //plateforme dépendant du cas d'utilisation (Fiche Banque ou Fiche Plateforme notamment). 
   //Elle est comparée à la plateforme du conteneur pour appliquer des règles de gestion particulières dans le cas d'un conteneur partagé
   private Plateforme plateforme;

   //indique que ce conteneurDecorator est supprimable (ce n'est pas toujours le cas notamment pour la Fiche Plateforme pour laquelle seuls les conteneurs partagés le sont).
   //permettra donc notamment d'afficher ou non la croix rouge associée à la suppression en mode édition
   private Boolean supprimable;
   
   public ConteneurDecorator(Conteneur conteneur, Plateforme plateforme, Boolean supprimable) {
      this.conteneur = conteneur;
      this.plateforme = plateforme;
      this.supprimable = supprimable;
   }

   public Boolean isSupprimable(){
      return supprimable;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme plateforme){
      this.plateforme = plateforme;
   }

   //TK-636
   public String getLibelleComplet() {
      return getConteneur().getLibelleForPlateforme(plateforme);
   }
   
   @Override
   public ConteneurDecorator clone(){
      return new ConteneurDecorator(getConteneur(), getPlateforme(), isSupprimable());
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
      return ((this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme)))
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
      if(this.plateforme != null){
         hashPf = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashConteneur;
      hash = 31 * hash + hashPf;

      return hash;
   }

}
