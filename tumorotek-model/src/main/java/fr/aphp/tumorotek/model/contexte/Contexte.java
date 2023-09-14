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
package fr.aphp.tumorotek.model.contexte;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;

/**
 * Objet persistant mappant la table CONTEXTE.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "CONTEXTE")
@NamedQueries(value = {@NamedQuery(name = "Contexte.findByNom", query = "SELECT c FROM Contexte c WHERE c.nom = ?1"),
   @NamedQuery(name = "Contexte.findByBanqueId",
      query = "SELECT c FROM Contexte c " + "left join c.banques b " + "WHERE b.banqueId = ?1"),
   @NamedQuery(name = "Contexte.findByOrder", query = "SELECT c FROM Contexte c ORDER BY c.nom")})
public class Contexte implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer contexteId;

   private String nom;

   private String libelle;

   private Set<Banque> banques = new HashSet<>();

   private Set<Catalogue> catalogues = new HashSet<>();

   /** Constructeur par défaut. */
   public Contexte(){}

   //	/**
   //	 * Constructeur avec paramètres.
   //	 * @param cId .
   //	 * @param n .
   //	 */
   //	public Contexte(Integer cId, String n) {
   //		this.contexteId = cId;
   //		this.nom = n;
   //	}

   @Id
   @Column(name = "CONTEXTE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getContexteId(){
      return this.contexteId;
   }

   public void setContexteId(final Integer cId){
      this.contexteId = cId;
   }

   @Column(name = "NOM", nullable = false, length = 25)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   public void setLibelle(final String libelle){
      this.libelle = libelle;
   }

   @Column(name = "LIBELLE", length = 50)
   public String getLibelle(){
      return this.libelle;
   }

   @OneToMany(mappedBy = "contexte")
   public Set<Banque> getBanques(){
      return this.banques;
   }

   public void setBanques(final Set<Banque> newBanques){
      this.banques = newBanques;
   }

   @ManyToMany(mappedBy = "contextes", targetEntity = Catalogue.class)
   public Set<Catalogue> getCatalogues(){
      return catalogues;
   }

   public void setCatalogues(final Set<Catalogue> catals){
      this.catalogues = catals;
   }

   /**
    * 2 contextes sont considérées comme égaux s'ils ont le même nom.
    * @param obj est le contexte à tester.
    * @return true si les contextes sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Contexte test = (Contexte) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }

      hash = 31 * hash + hashNom;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.nom + "}";
   }

}
