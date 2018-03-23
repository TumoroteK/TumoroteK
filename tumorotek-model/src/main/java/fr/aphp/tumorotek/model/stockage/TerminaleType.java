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
package fr.aphp.tumorotek.model.stockage;

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

import fr.aphp.tumorotek.model.TKdataObject;

/**
 *
 * Objet persistant mappant la table TERMINALE_TYPE.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "TERMINALE_TYPE")
@NamedQueries(value = {@NamedQuery(name = "TerminaleType.findByType", query = "SELECT t FROM TerminaleType t WHERE t.type = ?1"),
   @NamedQuery(name = "TerminaleType.findByNbPlaces", query = "SELECT t FROM TerminaleType t WHERE t.nbPlaces = ?1"),
   @NamedQuery(name = "TerminaleType.findByHauteur", query = "SELECT t FROM TerminaleType t WHERE t.hauteur = ?1"),
   @NamedQuery(name = "TerminaleType.findByLongueur", query = "SELECT t FROM TerminaleType t WHERE t.longueur = ?1"),
   @NamedQuery(name = "TerminaleType.findByScheme", query = "SELECT t FROM TerminaleType t WHERE t.scheme = ?1"),
   @NamedQuery(name = "TerminaleType.findByExcludedId",
      query = "SELECT t FROM TerminaleType t " + "WHERE t.terminaleTypeId != ?1"),
   @NamedQuery(name = "TerminaleType.findByOrder", query = "SELECT t FROM TerminaleType t " + "ORDER BY t.type")})
public class TerminaleType implements TKdataObject, Serializable
{

   private static final long serialVersionUID = -6100807640651925022L;

   private Integer terminaleTypeId;
   private String type;
   private Integer nbPlaces;
   private Integer hauteur;
   private Integer longueur;
   private String scheme;
   private Boolean departNumHaut;

   private Set<Terminale> terminales = new HashSet<>();

   /** Constructeur par défaut. */
   public TerminaleType(){}

   @Id
   @Column(name = "TERMINALE_TYPE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTerminaleTypeId(){
      return this.terminaleTypeId;
   }

   public void setTerminaleTypeId(final Integer id){
      this.terminaleTypeId = id;
   }

   @Column(name = "TYPE", nullable = false, length = 25)
   public String getType(){
      return this.type;
   }

   public void setType(final String t){
      this.type = t;
   }

   @Column(name = "NB_PLACES", nullable = false)
   public Integer getNbPlaces(){
      return this.nbPlaces;
   }

   public void setNbPlaces(final Integer nb){
      this.nbPlaces = nb;
   }

   @Column(name = "HAUTEUR", nullable = false)
   public Integer getHauteur(){
      return this.hauteur;
   }

   public void setHauteur(final Integer haut){
      this.hauteur = haut;
   }

   @Column(name = "LONGUEUR", nullable = false)
   public Integer getLongueur(){
      return this.longueur;
   }

   public void setLongueur(final Integer l){
      this.longueur = l;
   }

   @Column(name = "SCHEME", nullable = true, length = 100)
   public String getScheme(){
      return this.scheme;
   }

   public void setScheme(final String sch){
      this.scheme = sch;
   }

   @Column(name = "DEPART_NUM_HAUT", nullable = false)
   public Boolean getDepartNumHaut(){
      return departNumHaut;
   }

   public void setDepartNumHaut(final Boolean depart){
      this.departNumHaut = depart;
   }

   @OneToMany(mappedBy = "terminaleType")
   public Set<Terminale> getTerminales(){
      return this.terminales;
   }

   public void setTerminales(final Set<Terminale> terms){
      this.terminales = terms;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même type.
    * @param obj est l'objet à tester.
    * @return true si les objets sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final TerminaleType test = (TerminaleType) obj;
      if(this.type == null){
         return (test.type == null);
      }else{
         return (this.type.equals(test.type));
      }
   }

   /**
    * Le hashcode est calculé sur l'attribut type.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashType = 0;

      if(this.type != null){
         hashType = this.type.hashCode();
      }

      hash = 31 * hash + hashType;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.type != null){
         return "{" + this.type + "}";
      }else{
         return "{Empty TerminaleType}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Enceinte.
    */
   @Override
   public TerminaleType clone(){
      final TerminaleType clone = new TerminaleType();

      clone.setTerminaleTypeId(this.terminaleTypeId);
      clone.setType(this.type);
      clone.setNbPlaces(this.nbPlaces);
      clone.setHauteur(this.hauteur);
      clone.setLongueur(this.longueur);
      clone.setScheme(this.scheme);
      clone.setDepartNumHaut(this.departNumHaut);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getTerminaleTypeId();
   }

}
