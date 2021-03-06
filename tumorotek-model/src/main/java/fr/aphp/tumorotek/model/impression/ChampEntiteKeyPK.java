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
package fr.aphp.tumorotek.model.impression;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Embedded Id pour la table CHAMP_ENTITE_BLOC.
 * Classe créée le 30/07/2010.
 *
 * @author Pierre VENTADOUR.
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Embeddable
public class ChampEntiteKeyPK implements Serializable
{

   private static final long serialVersionUID = 4932675709255221762L;

   private CleImpression cleImpression;
   private ChampEntite champEntite;

   public ChampEntiteKeyPK(){

   }

   public ChampEntiteKeyPK(final CleImpression key, final ChampEntite champ){
      this.cleImpression = key;
      this.champEntite = champ;
   }

   @ManyToOne(targetEntity = CleImpression.class)
   public CleImpression getCleImpression(){
      return cleImpression;
   }

   public void setCleImpression(final CleImpression b){
      this.cleImpression = b;
   }

   @ManyToOne(targetEntity = ChampEntite.class)
   public ChampEntite getChampEntite(){
      return champEntite;
   }

   public void setChampEntite(final ChampEntite c){
      this.champEntite = c;
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
      final ChampEntiteKeyPK test = (ChampEntiteKeyPK) obj;
      return ((this.champEntite == test.champEntite || (this.champEntite != null && this.champEntite.equals(test.champEntite)))
         && (this.cleImpression == test.cleImpression
            || (this.cleImpression != null && this.cleImpression.equals(test.cleImpression))));
   }

   /**
    * Le hashcode est calculé sur les clés.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashChamp = 0;
      int hashKey = 0;

      if(this.champEntite != null){
         hashChamp = this.champEntite.hashCode();
      }
      if(this.cleImpression != null){
         hashKey = this.cleImpression.hashCode();
      }

      hash = 7 * hash + hashChamp;
      hash = 7 * hash + hashKey;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.champEntite != null && this.cleImpression != null){
         return "{" + champEntite.toString() + " (ChampEntite), " + cleImpression.toString() + " (BlocImpression)}";
      }
      return "{Empty ChampEntiteBlocPK}";
   }

}
