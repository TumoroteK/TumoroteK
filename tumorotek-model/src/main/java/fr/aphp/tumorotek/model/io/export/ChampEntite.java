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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.impression.ChampEntiteBloc;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.stats.Subdivision;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table CHAMP_ENTITE.
 * Classe créée le 25/11/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@Entity
@Table(name = "CHAMP_ENTITE")
@NamedQueries(value = {@NamedQuery(name = "ChampEntite.findByEntite", query = "SELECT c FROM ChampEntite c WHERE c.entite = ?1"),
   @NamedQuery(name = "ChampEntite.findByEntiteAndImport",
      query = "SELECT c FROM ChampEntite c " + "WHERE c.entite = ?1 " + "AND c.canImport = ?2 ORDER BY c.champEntiteId"),
   @NamedQuery(name = "ChampEntite.findByEntiteImportObligatoire",
      query = "SELECT c FROM ChampEntite c " + "WHERE c.entite = ?1 " + "AND c.canImport = ?2 " + "AND c.nullable = ?3"),
   @NamedQuery(name = "ChampEntite.findByEntiteAndNom",
      query = "SELECT c FROM ChampEntite c " + "WHERE c.entite = ?1 AND c.nom = ?2"),
   @NamedQuery(name = "ChampEntite.findByImportTemplateAndEntite", query = "SELECT c.champEntite FROM ImportColonne i "
      + "JOIN i.champ c WHERE i.importTemplate = ?1 " + "AND c.champEntite.entite = ?2")})
public class ChampEntite implements Comparable<ChampEntite>
{

   private Integer champEntiteId;
   private Entite entite;
   private String nom;
   private DataType dataType;
   private Boolean nullable;
   private Boolean unique;
   private String valeurDefaut;
   private Boolean canImport;
   private ChampEntite queryChamp;
   private Subdivision subdivision;

   private Set<ChampImprime> champImprimes = new HashSet<>();
   private Set<ChampEntiteBloc> champEntiteBlocs = new HashSet<>();
   private Set<Champ> champs = new HashSet<>();

   public ChampEntite(){
      super();
   }

   public ChampEntite(final Entite e, final String n, final DataType dt, final Boolean nul, final Boolean u,
      final String valDefaut, final Boolean canI, final ChampEntite queryC){
      super();
      this.entite = e;
      this.nom = n;
      this.dataType = dt;
      this.nullable = nul;
      this.unique = u;
      this.valeurDefaut = valDefaut;
      this.canImport = canI;
      this.queryChamp = queryC;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "CHAMP_ENTITE_ID", unique = true, nullable = false)
   public Integer getChampEntiteId(){
      return champEntiteId;
   }

   public void setChampEntiteId(final Integer chId){
      this.champEntiteId = chId;
   }

   @ManyToOne
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite ent){
      this.entite = ent;
   }

   @Column(name = "NOM", nullable = false)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST})
   @JoinColumn(name = "DATA_TYPE_ID", nullable = false)
   public DataType getDataType(){
      return dataType;
   }

   public void setDataType(final DataType t){
      this.dataType = t;
   }

   @Column(name = "IS_NULL", nullable = false)
   public Boolean isNullable(){
      return nullable;
   }

   public void setNullable(final Boolean n){
      this.nullable = n;
   }

   @Column(name = "IS_UNIQUE", nullable = false)
   public Boolean isUnique(){
      return unique;
   }

   public void setUnique(final Boolean u){
      this.unique = u;
   }

   @Column(name = "CAN_IMPORT", nullable = false)
   public Boolean getCanImport(){
      return canImport;
   }

   public void setCanImport(final Boolean canI){
      this.canImport = canI;
   }

   @Column(name = "VALEUR_DEFAUT", nullable = true)
   public String getValeurDefaut(){
      return valeurDefaut;
   }

   public void setValeurDefaut(final String valDefaut){
      this.valeurDefaut = valDefaut;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "QUERY_CHAMP_ID", nullable = true)
   public ChampEntite getQueryChamp(){
      return queryChamp;
   }

   public void setQueryChamp(final ChampEntite queryC){
      this.queryChamp = queryC;
   }

   @OneToOne(mappedBy = "champEntite")
   public Subdivision getSubdivision(){
      return subdivision;
   }

   public void setSubdivision(final Subdivision subdivision){
      this.subdivision = subdivision;
   }

   @OneToMany(mappedBy = "pk.champEntite")
   public Set<ChampImprime> getChampImprimes(){
      return champImprimes;
   }

   public void setChampImprimes(final Set<ChampImprime> champIs){
      this.champImprimes = champIs;
   }

   @OneToMany(mappedBy = "pk.champEntite")
   public Set<ChampEntiteBloc> getChampEntiteBlocs(){
      return champEntiteBlocs;
   }

   public void setChampEntiteBlocs(final Set<ChampEntiteBloc> c){
      this.champEntiteBlocs = c;
   }

   @OneToMany(mappedBy = "champEntite")
   public Set<Champ> getChamps(){
      return champs;
   }

   public void setChamps(final Set<Champ> cs){
      this.champs = cs;
   }

   /**
    * 2 champEntites sont considérées comme égaux s'ils ont la même entité
    * et le même nom.
    * @param obj est le champEntite à tester.
    * @return true si les champEntites sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ChampEntite test = (ChampEntite) obj;
      if(this.entite == null){
         if(test.entite == null){
            if(this.nom == null){
               return test.nom == null;
            }else{
               return this.nom.equals(test.nom);
            }
         }else{
            return false;
         }
      }else if(this.entite.equals(test.entite)){
         if(this.nom == null){
            return test.nom == null;
         }else{
            return this.nom.equals(test.nom);
         }
      }else{
         return false;
      }
   }

   /**
    * Le hashcode est calculé sur les attributs entité et nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashEntite = 0;
      int hashNom = 0;

      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }
      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }

      hash = 31 * hash + hashEntite;
      hash = 31 * hash + hashNom;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }else{
         return "{Empty ChampEntite}";
      }
   }

   @Override
   public ChampEntite clone(){
      final ChampEntite clone = new ChampEntite();
      clone.setChampEntiteId(this.getChampEntiteId());
      clone.setDataType(this.getDataType());
      clone.setEntite(this.getEntite());
      clone.setNom(this.getNom());
      clone.setNullable(this.isNullable());
      clone.setUnique(this.isUnique());
      clone.setCanImport(this.canImport);
      clone.setValeurDefaut(this.getValeurDefaut());
      return clone;
   }

   @Override
   public int compareTo(final ChampEntite o){
      return this.getNom().compareTo(o.getNom());
   }
}
