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
package fr.aphp.tumorotek.model.stats;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

/**
 *
 * Embedded Id pour la table STATS_MODELE_INDICATEUR.
 * Classe créée le 29/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class SModeleIndicateurPK implements Serializable
{

   private static final long serialVersionUID = 1L;

   private SModele sModele;
   private Indicateur indicateur;

   /** Constructeur par défaut. */
   public SModeleIndicateurPK(){}

   public SModeleIndicateurPK(final SModele m, final Indicateur i){
      this.sModele = m;
      this.indicateur = i;
   }

   @ManyToOne(fetch = FetchType.LAZY,targetEntity = SModele.class)
   public SModele getsModele(){
      return sModele;
   }

   public void setsModele(final SModele sModele){
      this.sModele = sModele;
   }

   @ManyToOne(fetch = FetchType.LAZY,targetEntity = Indicateur.class)
   public Indicateur getIndicateur(){
      return indicateur;
   }

   public void setIndicateur(final Indicateur i){
      this.indicateur = i;
   }

   /**
    * 2 PKs sont considérés comme égales si elles sont composees 
    * des mêmes clés.
    * @param obj est la PK à tester.
    * @return true si les PK sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final SModeleIndicateurPK test = (SModeleIndicateurPK) obj;
      return (((this.sModele != null && (this.sModele.equals(test.sModele)) || this.sModele == test.sModele)
         && ((this.indicateur != null && this.indicateur.equals(test.indicateur)) || this.indicateur == test.indicateur)));
   }

   /**
    * Le hashcode est calculé sur les clés.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      //int hash = 7;
      int hashModele = 0;
      int hashIndicateur = 0;

      if(this.sModele != null){
         hashModele = this.sModele.hashCode();
      }
      if(this.indicateur != null){
         hashIndicateur = this.indicateur.hashCode();
      }

      return hashModele + hashIndicateur;
   }

}
