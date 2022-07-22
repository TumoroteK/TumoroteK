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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * Objet persistant mappant la table STATS_INDICATEUR_MODELE. Classe créée le
 * 12/03/13.
 *
 * @author Marc DESCHAMPS
 * @version 2.1
 *
 */

@Entity
@Table(name = "STATS_MODELE")
@NamedQueries(value = {
   @NamedQuery(name = "SModele.findByPlateforme", query = "SELECT m FROM SModele m WHERE m.plateforme = ?1 " + "ORDER BY m.nom"),
   @NamedQuery(name = "SModele.findByNomAndPlateforme", query = "SELECT m FROM SModele m WHERE m.nom = ?1 AND m.plateforme = ?2"),
   @NamedQuery(name = "SModele.findByExcludedId", query = "SELECT m FROM SModele m WHERE m.smodeleId != ?1")})
public class SModele implements Serializable, Comparable<SModele>
{

   private static final long serialVersionUID = 336055468641245917L;

   private Integer smodeleId;

   private String nom;

   // private ModeleType modeleType;
   private Plateforme plateforme;

   //	private Boolean isSubdivised;
   //	private Boolean isDefault;
   //	private String subdivisionType;
   //	private String subdivisionName;
   private Set<SModeleIndicateur> sModeleIndicateurs = new HashSet<>();

   private Set<Banque> banques = new HashSet<>();

   private String description;

   private Subdivision subdivision;

   public SModele(){

   }

   public SModele(final String nom, final Boolean isDefault){
      setNom(nom);
   }

   @Id
   @Column(name = "STATS_MODELE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getSmodeleId(){
      return smodeleId;
   }

   public void setSmodeleId(final Integer i){
      this.smodeleId = i;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return nom;
   }

   public void setNom(final String nom){
      this.nom = nom;
   }

   @OneToMany(mappedBy = "pk.sModele", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @OrderBy("ordre")
   public Set<SModeleIndicateur> getSModeleIndicateurs(){
      return sModeleIndicateurs;
   }

   public void setSModeleIndicateurs(final Set<SModeleIndicateur> sM){
      this.sModeleIndicateurs = sM;
   }

   @ManyToMany(targetEntity = Banque.class)
   @JoinTable(name = "STATS_MODELE_BANQUE", joinColumns = @JoinColumn(name = "STATS_MODELE_ID"),
      inverseJoinColumns = @JoinColumn(name = "BANQUE_ID"))
   @OrderBy("banqueId")
   public Set<Banque> getBanques(){
      return this.banques;
   }

   public void setBanques(final Set<Banque> banks){
      this.banques = banks;
   }

   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme plateforme){
      this.plateforme = plateforme;
   }

   @Column(name = "DESCRIPTION", nullable = true)
   public String getDescription(){
      return description;
   }

   public void setDescription(final String description){
      this.description = description;
   }

   @ManyToOne
   @JoinColumn(name = "SUBDIVISION_ID", nullable = true)
   public Subdivision getSubdivision(){
      return subdivision;
   }

   public void setSubdivision(final Subdivision subdivision){
      this.subdivision = subdivision;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    *
    */
   @Override
   public String toString(){
      String resultat = super.toString();
      if(this.nom != null){
         return resultat += "{" + this.nom + "}";
      }else{
         return resultat += "{Empty SModele}";
      }
   }

   /**
    * 2 requetes sont considérées comme égales si tous leurs attributs sont
    * égaux. 2 requete egales ne peuvent pas avoir le même ordre.
    *
    * @param obj
    *            est le statement à tester.
    * @return true si les statement sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || (!(obj instanceof SModele))){
         return false;
      }
      final SModele im = (SModele) obj;
      return ((this.nom == im.nom || (this.nom != null && this.nom.equals(im.nom)))
         && (this.plateforme == im.plateforme || (this.plateforme != null && this.plateforme.equals(im.plateforme))));
   }

   @Override
   public int hashCode(){
      int hashCode = 17;
      hashCode = 31 * hashCode + ((nom == null) ? 0 : nom.hashCode());
      hashCode = 31 * hashCode + ((plateforme == null) ? 0 : plateforme.hashCode());
      return hashCode;
   }

   @Override
   public SModele clone(){
      final SModele clone = new SModele();
      clone.setSmodeleId(this.smodeleId);
      clone.setNom(this.nom);
      clone.setPlateforme(this.plateforme);
      clone.setSubdivision(getSubdivision());
      clone.setSModeleIndicateurs(getSModeleIndicateurs());
      return clone;
   }

   @Override
   public int compareTo(final SModele o){
      if(o != null){
         return this.getNom().compareToIgnoreCase(o.getNom());
      }
      return 0;
   }
}
