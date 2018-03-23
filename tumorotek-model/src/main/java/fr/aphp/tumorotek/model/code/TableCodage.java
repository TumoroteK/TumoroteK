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
package fr.aphp.tumorotek.model.code;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;

/**
 *
 * Objet persistant mappant la table TABLE_CODES.
 * Créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "TABLE_CODAGE")
@NamedQueries(value = {@NamedQuery(name = "TableCodage.findByNom", query = "SELECT t FROM TableCodage t WHERE t.nom = ?1")})
public class TableCodage implements Serializable
{

   private Integer tableCodageId;
   private String nom;
   private String version;

   private Set<CodeSelect> codeSelects = new HashSet<>();
   private Set<CodeAssigne> codeAssignes = new HashSet<>();
   //private Set<Banque> banques = new HashSet<Banque>();
   private Set<BanqueTableCodage> banqueTableCodages = new HashSet<>();

   private static final long serialVersionUID = 174651351343L;

   /** Constructeur par défaut. */
   public TableCodage(){}

   @Id
   @Column(name = "TABLE_CODAGE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTableCodageId(){
      return this.tableCodageId;
   }

   public void setTableCodageId(final Integer id){
      this.tableCodageId = id;
   }

   @Column(name = "NOM", nullable = false, length = 25)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "VERSION", nullable = true)
   public String getVersion(){
      return version;
   }

   public void setVersion(final String v){
      this.version = v;
   }

   @OneToMany(mappedBy = "tableCodage")
   public Set<CodeSelect> getCodeSelects(){
      return this.codeSelects;
   }

   public void setCodeSelects(final Set<CodeSelect> codes){
      this.codeSelects = codes;
   }

   @OneToMany(mappedBy = "tableCodage")
   public Set<CodeAssigne> getCodeAssignes(){
      return this.codeAssignes;
   }

   public void setCodeAssignes(final Set<CodeAssigne> codes){
      this.codeAssignes = codes;
   }

   //	@ManyToMany(
   //			targetEntity = Banque.class,
   //			mappedBy = "tablesCodage"
   //	)
   //	public Set<Banque> getBanques() {
   //		return banques;
   //	}
   //
   //	public void setBanques(Set<Banque> bs) {
   //		this.banques = bs;
   //	}

   @OneToMany(mappedBy = "pk.tableCodage")
   public Set<BanqueTableCodage> getBanqueTableCodages(){
      return banqueTableCodages;
   }

   public void setBanqueTableCodages(final Set<BanqueTableCodage> btcs){
      this.banqueTableCodages = btcs;
   }

   /**
    * 2 tables codes sont considérés comme egales s'ils ont le même nom.
    * @param obj est le code à tester.
    * @return true si les codes sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final TableCodage test = (TableCodage) obj;
      if(this.nom != null){
         return this.nom.equals(test.nom);
      }else{ //impossible
         return false;
      }
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

   @Override
   public TableCodage clone(){
      final TableCodage clone = new TableCodage();
      clone.setTableCodageId(this.tableCodageId);
      clone.setNom(this.nom);
      clone.setVersion(this.version);
      clone.setCodeAssignes(this.codeAssignes);
      clone.setCodeSelects(this.codeSelects);
      return clone;
   }

   @Override
   public String toString(){
      return "{TableCodage: " + getNom() + "}";
   }
}
