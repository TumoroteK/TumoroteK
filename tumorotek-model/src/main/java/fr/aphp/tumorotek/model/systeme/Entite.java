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
package fr.aphp.tumorotek.model.systeme;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.coeur.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.cession.Retour;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;

/**
 *
 * Objet persistant mappant la table ENTITE.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.3
 *
 */
@Entity
@Table(name = "ENTITE")
//@NamedQueries(value = {@NamedQuery(name = "Entite.findByNom", query = "SELECT e FROM Entite e WHERE e.nom like ?1"),
//   @NamedQuery(name = "Entite.findByMasc", query = "SELECT e FROM Entite e WHERE e.masc = ?1"), //FIXME Query non présente dans EntiteDao
//   @NamedQuery(name = "Entite.findAnnotables", query = "SELECT e FROM Entite e WHERE e.annotable = 1 " + "order by e.entiteId")})
public class Entite implements Serializable
{

   private static final long serialVersionUID = -7961160095742036172L;

   private Integer entiteId;
   private String nom;
   private Boolean masc; //FIXME jamais utilisé
   private Boolean annotable;

   private Set<DroitObjet> droitObjets = new HashSet<>();
   private Set<Retour> retours = new HashSet<>();
   private Set<Transformation> transformations = new HashSet<>();
   private Set<Emplacement> emplacements = new HashSet<>();
   private Set<CederObjet> cederObjets = new HashSet<>();
   private Set<Terminale> terminales = new HashSet<>();
   private Set<Enceinte> enceintes = new HashSet<>();
   private Set<ImportTemplate> importTemplates = new HashSet<>();
   private Set<ChampLigneEtiquette> champLigneEtiquettes = new HashSet<>();
   private Set<ObjetNonConforme> objetNonConformes = new HashSet<>();

   public Entite(){}

   @Id
   @Column(name = "ENTITE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getEntiteId(){
      return this.entiteId;
   }

   public void setEntiteId(final Integer id){
      this.entiteId = id;
   }

   @Column(name = "NOM", nullable = false, length = 25)
   public String getNom(){
      return this.nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   //FIXME jamais utilisé
   @Column(name = "MASC", nullable = false)
   public Boolean getMasc(){
      return this.masc;
   }

   //FIXME jamais utilisé
   public void setMasc(final Boolean m){
      this.masc = m;
   }

   @Column(name = "ANNOTABLE", nullable = false)
   public Boolean getAnnotable(){
      return this.annotable;
   }

   public void setAnnotable(final Boolean annot){
      this.annotable = annot;
   }

   @OneToMany(mappedBy = "pk.entite")
   public Set<DroitObjet> getDroitObjets(){
      return droitObjets;
   }

   public void setDroitObjets(final Set<DroitObjet> droitObjs){
      this.droitObjets = droitObjs;
   }

   @OneToMany(mappedBy = "entite")
   public Set<Retour> getRetours(){
      return retours;
   }

   public void setRetours(final Set<Retour> rets){
      this.retours = rets;
   }

   @OneToMany(mappedBy = "entite")
   public Set<Transformation> getTransformations(){
      return transformations;
   }

   public void setTransformations(final Set<Transformation> trans){
      this.transformations = trans;
   }

   @OneToMany(mappedBy = "entite")
   public Set<Emplacement> getEmplacements(){
      return emplacements;
   }

   public void setEmplacements(final Set<Emplacement> empls){
      this.emplacements = empls;
   }

   @OneToMany(mappedBy = "pk.entite")
   public Set<CederObjet> getCederObjets(){
      return cederObjets;
   }

   public void setCederObjets(final Set<CederObjet> cederObjs){
      this.cederObjets = cederObjs;
   }

   @OneToMany(mappedBy = "entite")
   public Set<Terminale> getTerminales(){
      return terminales;
   }

   public void setTerminales(final Set<Terminale> terms){
      this.terminales = terms;
   }

   @OneToMany(mappedBy = "entite")
   public Set<Enceinte> getEnceintes(){
      return enceintes;
   }

   public void setEnceintes(final Set<Enceinte> encs){
      this.enceintes = encs;
   }

   @ManyToMany(targetEntity = ImportTemplate.class, cascade = {})
   @JoinTable(name = "IMPORT_TEMPLATE_ENTITE", joinColumns = @JoinColumn(name = "ENTITE_ID"),
      inverseJoinColumns = @JoinColumn(name = "IMPORT_TEMPLATE_ID"))
   public Set<ImportTemplate> getImportTemplates(){
      return importTemplates;
   }

   public void setImportTemplates(final Set<ImportTemplate> i){
      this.importTemplates = i;
   }

   @OneToMany(mappedBy = "entite", cascade = {CascadeType.REMOVE})
   public Set<ChampLigneEtiquette> getChampLigneEtiquettes(){
      return champLigneEtiquettes;
   }

   public void setChampLigneEtiquettes(final Set<ChampLigneEtiquette> c){
      this.champLigneEtiquettes = c;
   }

   @OneToMany(mappedBy = "entite")
   public Set<ObjetNonConforme> getObjetNonConformes(){
      return objetNonConformes;
   }

   public void setObjetNonConformes(final Set<ObjetNonConforme> o){
      this.objetNonConformes = o;
   }

   /**
    * 2 entités sont considérées comme égales si elles ont le même nom.
    * @param obj est l'entité à tester.
    * @return true si les entités sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Entite test = (Entite) obj;
      if(this.nom == null){
         return (test.nom == null);
      }
      return (this.nom.equals(test.nom));
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
      if(this.nom != null){
         return "{" + this.nom + "}";
      }
      return "{Empty Entite}";
   }
}
