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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;

/**
 *
 * Objet persistant mappant la table LIGNE_ETIQUETTE.
 * Classe créée le 07/06/11.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "LIGNE_ETIQUETTE")
@NamedQueries(value = {@NamedQuery(name = "LigneEtiquette.findByModele",
   query = "SELECT l FROM LigneEtiquette l " + "WHERE l.modele = ?1 " + "ORDER BY l.ordre")})
public class LigneEtiquette implements TKdataObject, Serializable
{

   private static final long serialVersionUID = 865826802338181646L;

   private Integer ligneEtiquetteId;
   private Integer ordre;
   private Boolean isBarcode;
   private String entete;
   private String contenu;
   private String font;
   private String style;
   private Integer size;

   private Modele modele;
   private Set<ChampLigneEtiquette> champLigneEtiquettes = new HashSet<>();

   public LigneEtiquette(){}

   @Id
   @Column(name = "LIGNE_ETIQUETTE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getLigneEtiquetteId(){
      return ligneEtiquetteId;
   }

   public void setLigneEtiquetteId(final Integer id){
      this.ligneEtiquetteId = id;
   }

   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @Column(name = "IS_BARCODE", nullable = true)
   public Boolean getIsBarcode(){
      return isBarcode;
   }

   public void setIsBarcode(final Boolean isB){
      this.isBarcode = isB;
   }

   @Column(name = "ENTETE", nullable = true, length = 25)
   public String getEntete(){
      return entete;
   }

   public void setEntete(final String e){
      this.entete = e;
   }

   @Column(name = "CONTENU", nullable = true, length = 50)
   public String getContenu(){
      return contenu;
   }

   public void setContenu(final String c){
      this.contenu = c;
   }

   @Column(name = "FONT", nullable = true, length = 25)
   public String getFont(){
      return font;
   }

   public void setFont(final String f){
      this.font = f;
   }

   @Column(name = "STYLE", nullable = true, length = 25)
   public String getStyle(){
      return style;
   }

   public void setStyle(final String s){
      this.style = s;
   }

   @Column(name = "FONT_SIZE", nullable = true)
   public Integer getSize(){
      return size;
   }

   public void setSize(final Integer s){
      this.size = s;
   }

   @ManyToOne()
   @JoinColumn(name = "MODELE_ID", nullable = false)
   public Modele getModele(){
      return modele;
   }

   public void setModele(final Modele m){
      this.modele = m;
   }

   @OneToMany(mappedBy = "ligneEtiquette", cascade = {CascadeType.REMOVE})
   public Set<ChampLigneEtiquette> getChampLigneEtiquettes(){
      return champLigneEtiquettes;
   }

   public void setChampLigneEtiquettes(final Set<ChampLigneEtiquette> c){
      this.champLigneEtiquettes = c;
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
      final LigneEtiquette test = (LigneEtiquette) obj;
      return ((this.ordre == test.ordre || (this.ordre != null && this.ordre.equals(test.ordre)))
         && (this.modele == test.modele || (this.modele != null && this.modele.equals(test.modele))));
   }

   /**
    * Le hashcode est calculé sur tous les attributs.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashOrdre = 0;
      int hashModele = 0;

      if(this.ordre != null){
         hashOrdre = this.ordre.hashCode();
      }
      if(this.modele != null){
         hashModele = this.modele.hashCode();
      }

      hash = 7 * hash + hashOrdre;
      hash = 31 * hash + hashModele;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.modele != null && this.ordre != null){
         return "{" + this.ordre + ", " + this.modele.getNom() + "(Modele)}";
      }else{
         return "{Empty LigneEtiquette}";
      }
   }

   @Override
   public LigneEtiquette clone(){
      final LigneEtiquette clone = new LigneEtiquette();

      clone.setLigneEtiquetteId(this.ligneEtiquetteId);
      clone.setModele(this.modele);
      clone.setOrdre(this.ordre);
      clone.setIsBarcode(this.isBarcode);
      clone.setEntete(this.entete);
      clone.setContenu(this.contenu);
      clone.setFont(this.font);
      clone.setStyle(this.style);
      clone.setSize(this.size);
      clone.setChampLigneEtiquettes(this.champLigneEtiquettes);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getLigneEtiquetteId();
   }

}
