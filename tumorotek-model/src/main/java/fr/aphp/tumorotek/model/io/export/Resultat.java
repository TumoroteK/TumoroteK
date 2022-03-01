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
package fr.aphp.tumorotek.model.io.export;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table RESULTAT.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.3
 *
 */
@Entity
@Table(name = "RESULTAT")
//@NamedQueries(
//   value = {@NamedQuery(name = "Resultat.findByAffichage", query = "SELECT a.resultats FROM Affichage a " + "where a = ?1")})
public class Resultat implements Comparable<Resultat>
{

   private Integer resultatId;
   private String nomColonne;
   private Champ champ;
   private Boolean tri;
   private Integer ordreTri;
   private Integer position;
   private String format;
   private Affichage affichage;

   public Resultat(){
      super();
   }

   public Resultat(final String nomCol, final Champ ch, final Boolean t, final Integer oTri, final Integer pos, final String f,
      final Affichage aff){
      super();
      this.nomColonne = nomCol;
      this.champ = ch;
      this.tri = t;
      this.ordreTri = oTri;
      this.position = pos;
      this.format = f;
      this.affichage = aff;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "RESULTAT_ID", unique = true, nullable = false)
   public Integer getResultatId(){
      return resultatId;
   }

   public void setResultatId(final Integer rId){
      this.resultatId = rId;
   }

   @Column(name = "NOM_COLONNE", nullable = false)
   public String getNomColonne(){
      return nomColonne;
   }

   public void setNomColonne(final String nomCol){
      this.nomColonne = nomCol;
   }

   @OneToOne
   @JoinColumn(name = "CHAMP_ID", nullable = false)
   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ ch){
      this.champ = ch;
   }

   @Column(name = "TRI", nullable = false)
   public Boolean getTri(){
      return tri;
   }

   public void setTri(final Boolean t){
      this.tri = t;
   }

   @Column(name = "ORDRE_TRI", nullable = false)
   public Integer getOrdreTri(){
      return ordreTri;
   }

   public void setOrdreTri(final Integer oTri){
      this.ordreTri = oTri;
   }

   @Column(name = "POSITION", nullable = false)
   public Integer getPosition(){
      return position;
   }

   public void setPosition(final Integer pos){
      this.position = pos;
   }

   @Column(name = "FORMAT", nullable = true)
   public String getFormat(){
      return format;
   }

   public void setFormat(final String f){
      this.format = f;
   }

   @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "AFFICHAGE_ID", nullable = false)
   public Affichage getAffichage(){
      return affichage;
   }

   public void setAffichage(final Affichage aff){
      this.affichage = aff;
   }

   /**
    * 2 résultats sont considérés comme égaux s'ils ont
    * le même nom de colonne et la même position.
    * @param obj
    *            est le résultat à tester.
    * @return true si les résultats sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Resultat test = (Resultat) obj;
      /*if (this.position == null) {
      	if (test.position == null) {*/
      if(this.nomColonne == null){
         if(test.nomColonne == null){
            if(this.affichage == null){
               return test.affichage == null;
            }else{
               return this.affichage.equals(test.affichage);
            }
         }else{
            return false;
         }
      }else if(this.nomColonne.equals(test.nomColonne)){
         if(this.affichage == null){
            return test.affichage == null;
         }else{
            return this.affichage.equals(test.affichage);
         }
      }else{
         return false;
      }
      /* } else {
      	return false;
      }*/
      /*} else if (this.position.equals(test.position)) {
      	if (this.nomColonne == null) {
      		if (test.nomColonne == null) {
      			if (this.affichage == null) {
      				return test.affichage == null;
      			} else {
      				return this.affichage.equals(test.affichage);
      			}
      		} else {
      			return false;
      		}
      	} else if (this.nomColonne.equals(test.nomColonne)) {
      		if (this.affichage == null) {
      			return test.affichage == null;
      		} else {
      			return this.affichage.equals(test.affichage);
      		}
      	} else {
      		return false;
      	}
      } else {
      	return false;
      }*/
   }

   /**
    * Le hashcode est calculé sur les attributs nom de colonne et position.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNomColonne = 0;
      int hashAffichage = 0;
      //int hashPosition = 0;

      /*if (this.position != null) {
      	hashPosition = this.position.hashCode();
      }*/
      if(this.nomColonne != null){
         hashNomColonne = this.nomColonne.hashCode();
      }
      if(this.affichage != null){
         hashAffichage = this.affichage.hashCode();
      }

      /*hash = 31 * hash + hashPosition;*/
      hash = 31 * hash + hashNomColonne;
      hash = 31 * hash + hashAffichage;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.resultatId != null){
         return "{" + this.resultatId + "}";
      }else{
         return "{Empty Resultat}";
      }
   }

   public Resultat copy(){
      final Resultat retour = new Resultat();
      retour.setChamp(this.getChamp().copy());
      retour.setFormat(this.getFormat());
      retour.setNomColonne(this.getNomColonne());
      retour.setOrdreTri(this.getOrdreTri());
      retour.setPosition(this.getPosition());
      retour.setTri(this.getTri());
      return retour;
   }

   public boolean isCopy(final Resultat copie){
      if((copie == null) || copie.getClass() != this.getClass()){
         return false;
      }
      final Resultat test = copie;
      /*if (this.position == null) {
      	if (this.position.equals(test.getPosition())) {*/
      if(this.nomColonne == null){
         if(this.nomColonne.equals(test.getNomColonne())){
            if(this.champ == null){
               return test.champ == null;
            }else{
               return this.champ.equals(test.champ);
            }
         }else{
            return false;
         }
      }else if(this.nomColonne.equals(test.nomColonne)){
         if(this.affichage == null){
            return test.champ == null;
         }else{
            return this.champ.equals(test.champ);
         }
      }else{
         return false;
      }
      /*} else {
      		return false;
      	}
      } else if (this.position.equals(test.position)) {
      	if (this.nomColonne == null) {
      		if (test.nomColonne == null) {
      			if (this.affichage == null) {
      				return test.champ == null;
      			} else {
      				return this.champ.equals(test.champ);
      			}
      		} else {
      			return false;
      		}
      	} else if (this.nomColonne.equals(test.nomColonne)) {
      		if (this.champ == null) {
      			return test.champ == null;
      		} else {
      			return this.champ.equals(test.champ);
      		}
      	} else {
      		return false;
      	}
      } else {
      	return false;
      }*/
   }

   @Override
   public int compareTo(final Resultat o){
      return this.getPosition().compareTo(o.getPosition());
   }

}
