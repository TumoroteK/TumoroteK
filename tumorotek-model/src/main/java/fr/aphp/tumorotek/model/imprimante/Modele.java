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
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Objet persistant mappant la table MODELE.
 * Classe créée le 17/03/11.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "MODELE")
//@NamedQueries(value = {
//   @NamedQuery(name = "Modele.findByPlateforme",
//      query = "SELECT m FROM Modele m " + "WHERE m.plateforme = ?1 " + "ORDER BY m.nom"),
//   @NamedQuery(name = "Modele.findByNomAndPlateforme",
//      query = "SELECT m FROM Modele m " + "WHERE m.nom = ?1 AND m.plateforme = ?2"),
//   @NamedQuery(name = "Modele.findByExcludedId", query = "SELECT m FROM Modele m " + "WHERE m.modeleId != ?1")})
public class Modele implements TKdataObject, Serializable
{

   private static final long serialVersionUID = 5436849527391012790L;

   private Integer modeleId;
   private String nom;
   private Boolean isDefault;
   private String texteLibre;
   private Boolean isQRCode = false;

   private ModeleType modeleType;
   private Plateforme plateforme;

   private Set<AffectationImprimante> affectationImprimantes = new HashSet<>();
   private Set<LigneEtiquette> ligneEtiquettes = new HashSet<>();

   public Modele(){

   }

   @Id
   @Column(name = "MODELE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getModeleId(){
      return modeleId;
   }

   public void setModeleId(final Integer m){
      this.modeleId = m;
   }

   @Column(name = "NOM", nullable = false, length = 25)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "TEXTE_LIBRE", nullable = true, length = 20)
   public String getTexteLibre(){
      return texteLibre;
   }

   public void setTexteLibre(final String t){
      this.texteLibre = t;
   }

   @Column(name = "IS_DEFAULT", nullable = false)
   public Boolean getIsDefault(){
      return isDefault;
   }

   public void setIsDefault(final Boolean isD){
      this.isDefault = isD;
   }

   @Column(name = "IS_QRCODE", nullable = false)
   public Boolean getIsQRCode(){
      return isQRCode;
   }

   public void setIsQRCode(final Boolean isQR){
      this.isQRCode = isQR;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "MODELE_TYPE_ID", nullable = false)
   public ModeleType getModeleType(){
      return modeleType;
   }

   public void setModeleType(final ModeleType m){
      this.modeleType = m;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme p){
      this.plateforme = p;
   }

   @OneToMany(mappedBy = "modele", cascade = {CascadeType.REMOVE})
   public Set<AffectationImprimante> getAffectationImprimantes(){
      return affectationImprimantes;
   }

   public void setAffectationImprimantes(final Set<AffectationImprimante> aImprimantes){
      this.affectationImprimantes = aImprimantes;
   }

   @OneToMany(mappedBy = "modele", cascade = {CascadeType.REMOVE})
   public Set<LigneEtiquette> getLigneEtiquettes(){
      return ligneEtiquettes;
   }

   public void setLigneEtiquettes(final Set<LigneEtiquette> liEtiquettes){
      this.ligneEtiquettes = liEtiquettes;
   }

   /**
    * 2 Modele sont considérés comme égaux si tous leurs attributs
    * sont égaux.
    * @param obj est l'imprimante à tester.
    * @return true si les imprimantes sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Modele test = (Modele) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur tous les attributs.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashPf = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.plateforme != null){
         hashPf = this.plateforme.hashCode();
      }

      hash = 7 * hash + hashNom;
      hash = 31 * hash + hashPf;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + ", " + plateforme.getNom() + "(Plateforme)}";
      }else{
         return "{Empty Modele}";
      }
   }

   @Override
   public Modele clone(){
      final Modele clone = new Modele();

      clone.setModeleId(this.modeleId);
      clone.setNom(this.nom);
      clone.setModeleType(this.modeleType);
      clone.setPlateforme(this.plateforme);
      clone.setIsDefault(this.isDefault);
      clone.setTexteLibre(this.texteLibre);
      clone.setAffectationImprimantes(this.affectationImprimantes);
      clone.setLigneEtiquettes(this.ligneEtiquettes);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getModeleId();
   }

}
