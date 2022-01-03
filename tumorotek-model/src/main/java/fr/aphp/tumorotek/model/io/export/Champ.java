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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table CHAMP.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.3
 *
 */
@Entity
@Table(name = "CHAMP")
// @NamedQueries(value = {@NamedQuery(name = "Champ.findEnfants", query = "SELECT c FROM Champ c WHERE champParent = ?1")})
public class Champ implements Comparable<Champ>
{

   private Integer champId;
   private ChampEntite champEntite;
   private ChampAnnotation champAnnotation;
   private ChampDelegue champDelegue;
   private Champ champParent;
   private Set<ChampLigneEtiquette> champLigneEtiquettes = new HashSet<>();

   public Champ(){
      super();
   }

   public Champ(final ChampEntite chEnt){
      super();
      this.champEntite = chEnt;
   }

   public Champ(final ChampAnnotation chAnno){
      super();
      this.champAnnotation = chAnno;
   }

   public Champ(final ChampDelegue chDel){
      super();
      this.champDelegue = chDel;
   }

   public Champ(final AbstractTKChamp ch, final Champ champParent) {
      
      if(ch instanceof ChampEntite) {
         this.champEntite = (ChampEntite)ch;
      }else if(ch instanceof ChampAnnotation) {
         this.champAnnotation = (ChampAnnotation)ch;
      }else if(ch instanceof ChampDelegue) {
         this.champDelegue = (ChampDelegue)ch;
      }
      
      this.champParent = champParent;
      
   }
   
   public Champ(final AbstractTKChamp ch) {
      this(ch, null);
   }
   
   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "CHAMP_ID", unique = true, nullable = false)
   public Integer getChampId(){
      return champId;
   }

   public void setChampId(final Integer chId){
      this.champId = chId;
   }

   @ManyToOne
   @JoinColumn(name = "CHAMP_ENTITE_ID")
   public ChampEntite getChampEntite(){
      return champEntite;
   }

   public void setChampEntite(final ChampEntite sousEnt){
      this.champEntite = sousEnt;
   }

   @ManyToOne
   @JoinColumn(name = "CHAMP_ANNOTATION_ID")
   public ChampAnnotation getChampAnnotation(){
      return champAnnotation;
   }

   public void setChampAnnotation(final ChampAnnotation champAnno){
      this.champAnnotation = champAnno;
   }

   @ManyToOne
   @JoinColumn(name = "CHAMP_DELEGUE_ID")
   public ChampDelegue getChampDelegue(){
      return champDelegue;
   }

   public void setChampDelegue(final ChampDelegue champDelegue){
      this.champDelegue = champDelegue;
   }

   @OneToOne
   @JoinColumn(name = "CHAMP_PARENT_ID")
   public Champ getChampParent(){
      return champParent;
   }

   public void setChampParent(final Champ champPar){
      this.champParent = champPar;
   }

   @OneToMany(mappedBy = "champ", cascade = {CascadeType.REMOVE})
   public Set<ChampLigneEtiquette> getChampLigneEtiquettes(){
      return champLigneEtiquettes;
   }

   public void setChampLigneEtiquettes(final Set<ChampLigneEtiquette> c){
      this.champLigneEtiquettes = c;
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((champAnnotation == null) ? 0 : champAnnotation.hashCode());
      result = prime * result + ((champEntite == null) ? 0 : champEntite.hashCode());
      result = prime * result + ((champDelegue == null) ? 0 : champDelegue.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj){
      if(obj == null){
         return false;
      }
      final Champ other = (Champ) obj;
      if(null == this.champParent && null != other.champParent){
         return false;
      }
      if(null != this.champParent && null == other.champParent){
         return false;
      }
      if(null != this.champParent && null != other.champParent){
         return this.champParent.equals(other.champParent);
      }
      if(champAnnotation == null){
         if(other.champAnnotation != null){
            return false;
         }
      }else if(!champAnnotation.equals(other.champAnnotation)){
         return false;
      }
      if(champDelegue == null){
         if(other.champDelegue != null){
            return false;
         }
      }else if(!champDelegue.equals(other.champDelegue)){
         return false;
      }
      if(champEntite == null){
         if(other.champEntite != null){
            return false;
         }
      }else if(!champEntite.equals(other.champEntite)){
         return false;
      }
      return true;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.champAnnotation != null){
         return this.champAnnotation.getTableAnnotation().getEntite().getNom() + " " + this.getChampAnnotation().getNom();
      }else if(this.champDelegue != null){
         if(this.champParent != null){
            return champParentToString();
         }
         final String nomEntite = this.getChampDelegue().getEntite().getNom();
         final String contexte = this.getChampDelegue().getContexte().getNom();
         final String nomChampDelegue = StringUtils.capitalize(this.getChampDelegue().getNom()).replaceAll("Id$", "");
         return nomEntite + "." + contexte + "." + nomChampDelegue;
      }else if(this.champEntite != null){
         String champEntiteNom = this.champEntite.getNom();
         if(this.getChampEntite().getNom().matches("^[a-zA-Z]+Id$")){
            champEntiteNom = champEntiteNom.substring(0, champEntiteNom.length() - 2);
         }
         if(this.champParent != null){
            return champParentToString();
         }
         return this.getChampEntite().getEntite().getNom() + "." + champEntiteNom;
      }
      return "{Empty Champ}";
   }

   public String nom(){
      String retour = null;
      if(this.champAnnotation != null){
         retour = this.champAnnotation.getNom();
      }else if(this.champEntite != null){
         retour = this.champEntite.getNom();
         if(retour.matches("^[a-zA-Z]+Id$")){
            retour = retour.substring(0, retour.length() - 2);
         }
      }else if(this.champDelegue != null){
         retour = this.champDelegue.getNom().replaceAll("Id$", "");
      }
      return retour;
   }

   public DataType dataType(){
      DataType retour = null;
      if(this.champAnnotation != null){
         retour = this.champAnnotation.getDataType();
      }else if(this.champEntite != null){
         retour = this.champEntite.getDataType();
      }else if(this.champDelegue != null){
         retour = champDelegue.getDataType();
      }
      return retour;
   }

   public Entite entite(){
      Entite retour = null;
      if(this.champAnnotation != null){
         retour = this.champAnnotation.getTableAnnotation().getEntite();
      }else if(this.champDelegue != null){
         retour = this.getChampDelegue().getEntite();
      }else if(this.champEntite != null){
         retour = this.champEntite.getEntite();
      }
      return retour;
   }
   
   public String champParentToString() {
      
      final String champParentNom;
      if(this.champParent.getChampEntite() != null) {
         champParentNom = this.champParent.getChampEntite().getNom().replaceAll("Id$", "");
      }else {
         champParentNom = this.champParent.getChampDelegue().getNom().replaceAll("Id$", "");
      }
      
      final String entiteNom;
      if(this.champParent.getChampEntite() != null) {
         entiteNom = this.champParent.getChampEntite().getEntite().getNom();
      }else {
         entiteNom = this.champParent.getChampDelegue().getEntite().getNom();
      }
      
      final String champNom;
      if(this.getChampEntite() != null) {
         champNom = this.getChampEntite().getNom();
      }else {
         champNom = this.getChampDelegue().getNom();
      }
      
      return entiteNom + "." + champParentNom + "." + champNom;
      
   }

   @Override
   public int compareTo(final Champ champ){
      return this.nom().toLowerCase().compareTo(champ.nom().toLowerCase());
   }

   public Champ copy(){
      final Champ copy = new Champ();
      if(this.getChampAnnotation() != null){
         copy.setChampAnnotation(this.getChampAnnotation());
      }
      if(this.getChampEntite() != null){
         copy.setChampEntite(this.getChampEntite());
      }
      if(this.getChampParent() != null){
         copy.setChampParent(this.getChampParent().copy());
      }
      if(this.getChampDelegue() != null){
         copy.setChampDelegue(this.getChampDelegue());
      }
      return copy;
   }
}
