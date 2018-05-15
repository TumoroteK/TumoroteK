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
package fr.aphp.tumorotek.model.imprimante;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table CHAMP_LIGNE_ETIQUETTE.
 * Classe créée le 07/06/11.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "CHAMP_LIGNE_ETIQUETTE")
@NamedQueries(value = {
   @NamedQuery(name = "ChampLigneEtiquette.findByLigneEtiquette",
      query = "SELECT c FROM ChampLigneEtiquette c " + "WHERE c.ligneEtiquette = ?1 " + "ORDER BY c.ordre"),
   @NamedQuery(name = "ChampLigneEtiquette.findByLigneEtiquetteAndEntite", query = "SELECT c FROM ChampLigneEtiquette c "
      + "WHERE c.ligneEtiquette = ?1 " + "AND c.entite = ?2 " + "ORDER BY c.ordre")})
public class ChampLigneEtiquette implements Serializable
{

   private static final long serialVersionUID = -4039125555257317668L;

   private Integer champLigneEtiquetteId;
   private Integer ordre;
   private String expReg;

   private LigneEtiquette ligneEtiquette;
   private Champ champ;
   private Entite entite;

   public ChampLigneEtiquette(){

   }

   @Id
   @Column(name = "CHAMP_LIGNE_ETIQUETTE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getChampLigneEtiquetteId(){
      return champLigneEtiquetteId;
   }

   public void setChampLigneEtiquetteId(final Integer id){
      this.champLigneEtiquetteId = id;
   }

   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @Column(name = "EXP_REG", nullable = true, length = 25)
   public String getExpReg(){
      return expReg;
   }

   public void setExpReg(final String e){
      this.expReg = e;
   }

   @ManyToOne()
   @JoinColumn(name = "LIGNE_ETIQUETTE_ID", nullable = false)
   public LigneEtiquette getLigneEtiquette(){
      return ligneEtiquette;
   }

   public void setLigneEtiquette(final LigneEtiquette l){
      this.ligneEtiquette = l;
   }

   @ManyToOne()
   @JoinColumn(name = "CHAMP_ID", nullable = false)
   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ c){
      this.champ = c;
   }

   @ManyToOne()
   @JoinColumn(name = "ENTITE_ID	", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   /**
    * 2 Lignes sont considérées comme égales si tous leurs attributs
    * sont égaux.
    * @param obj est l'imprimante à tester.
    * @return true si les lignes sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ChampLigneEtiquette test = (ChampLigneEtiquette) obj;
      return ((this.ordre == test.ordre || (this.ordre != null && this.ordre.equals(test.ordre)))
         && (this.ligneEtiquette == test.ligneEtiquette
            || (this.ligneEtiquette != null && this.ligneEtiquette.equals(test.ligneEtiquette)))
         && (this.champ == test.champ || (this.champ != null && this.champ.equals(test.champ)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite))));
   }

   /**
    * Le hashcode est calculé sur tous les attributs.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashOrdre = 0;
      int hashLigne = 0;
      int hashChamp = 0;
      int hashEntite = 0;

      if(this.ordre != null){
         hashOrdre = this.ordre.hashCode();
      }
      if(this.ligneEtiquette != null){
         hashLigne = this.ligneEtiquette.hashCode();
      }
      if(this.champ != null){
         hashChamp = this.champ.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }

      hash = 7 * hash + hashOrdre;
      hash = 7 * hash + hashLigne;
      hash = 7 * hash + hashChamp;
      hash = 7 * hash + hashEntite;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.ligneEtiquette != null && this.ordre != null && this.champ != null && this.entite != null){
         return "{" + this.ordre + ", " + this.ligneEtiquette.getOrdre() + "(LigneEtiquette) " + this.entite.getNom()
            + "(Entite)}";
      }
      return "{Empty ChampLigneEtiquette}";
   }

   @Override
   public ChampLigneEtiquette clone(){
      final ChampLigneEtiquette clone = new ChampLigneEtiquette();

      clone.setChampLigneEtiquetteId(this.champLigneEtiquetteId);
      clone.setLigneEtiquette(this.ligneEtiquette);
      clone.setChamp(this.champ);
      clone.setEntite(this.entite);
      clone.setOrdre(this.ordre);
      clone.setExpReg(this.expReg);

      return clone;
   }

}
