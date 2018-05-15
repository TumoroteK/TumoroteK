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
package fr.aphp.tumorotek.model.qualite;

import java.util.HashSet;
import java.util.Set;

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

import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Objet persistant mappant la table NON_CONFORMITE.
 * Classe créée le 08/11/11.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "NON_CONFORMITE")
@NamedQueries(value = {
   @NamedQuery(name = "NonConformite.findByTypeAndPf",
      query = "SELECT n FROM NonConformite n " + "WHERE n.conformiteType = ?1 " + "AND n.plateforme = ?2 " + "ORDER BY n.nom"),
   @NamedQuery(name = "NonConformite.findByTypePfAndNom",
      query = "SELECT n FROM NonConformite n " + "WHERE n.conformiteType = ?1 " + "AND n.plateforme = ?2 " + "AND n.nom like ?3 "
         + "ORDER BY n.nom"),
   @NamedQuery(name = "NonConformite.findByExcludedId",
      query = "SELECT n FROM NonConformite n " + "WHERE n.nonConformiteId != ?1")})
public class NonConformite implements java.io.Serializable
{

   private static final long serialVersionUID = -6139596888096490682L;

   private Integer nonConformiteId;
   private ConformiteType conformiteType;
   private Plateforme plateforme;
   private String nom;

   private Set<ObjetNonConforme> objetNonConformes = new HashSet<>();

   public NonConformite(){
      super();
   }

   @Id
   @Column(name = "NON_CONFORMITE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getNonConformiteId(){
      return nonConformiteId;
   }

   public void setNonConformiteId(final Integer id){
      this.nonConformiteId = id;
   }

   @ManyToOne()
   @JoinColumn(name = "CONFORMITE_TYPE_ID", nullable = false)
   public ConformiteType getConformiteType(){
      return conformiteType;
   }

   public void setConformiteType(final ConformiteType c){
      this.conformiteType = c;
   }

   @ManyToOne()
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme p){
      this.plateforme = p;
   }

   @Column(name = "NOM", nullable = false, length = 200)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @OneToMany(mappedBy = "nonConformite")
   public Set<ObjetNonConforme> getObjetNonConformes(){
      return objetNonConformes;
   }

   public void setObjetNonConformes(final Set<ObjetNonConforme> o){
      this.objetNonConformes = o;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final NonConformite test = (NonConformite) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.conformiteType == test.conformiteType
            || (this.conformiteType != null && this.conformiteType.equals(test.conformiteType)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashType = 0;
      int hashPlateforme = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.conformiteType != null){
         hashType = this.conformiteType.hashCode();
      }
      if(this.plateforme != null){
         hashPlateforme = this.plateforme.hashCode();
      }

      hash = 7 * hash + hashNom;
      hash = 8 * hash + hashType;
      hash = 8 * hash + hashPlateforme;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + ", " + conformiteType.getConformiteType() + "(ConformiteType), " + plateforme.getNom()
            + "(Plateforme)}";
      }
      return "{Empty NonConformite}";
   }

   @Override
   public NonConformite clone(){
      final NonConformite clone = new NonConformite();

      clone.setNonConformiteId(this.nonConformiteId);
      clone.setNom(this.nom);
      clone.setConformiteType(this.conformiteType);
      clone.setPlateforme(this.plateforme);

      return clone;
   }

}
