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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Objet persistant mappant la table TITRE.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "TITRE")
//@NamedQueries(value = {@NamedQuery(name = "Titre.findByTitre", query = "SELECT t FROM Titre t WHERE t.titre like ?1"),
//   @NamedQuery(name = "Titre.findByCollaborateurId",
//      query = "SELECT t FROM Titre t " + "left JOIN t.collaborateurs c " + "WHERE c.collaborateurId = ?1"),
//   @NamedQuery(name = "Titre.findByOrder", query = "SELECT t FROM Titre t ORDER BY t.titre")})
public class Titre implements java.io.Serializable
{

   private static final long serialVersionUID = 78644354386453143L;

   private Integer titreId;
   private String titre;

   private Set<Collaborateur> collaborateurs = new HashSet<>();

   /** Constructeur par défaut. */
   public Titre(){}

   //	/**
   //	 * Constructeur avec paramètres.
   //	 * @param id est la clé primaire.
   //	 * @param t est l'attribut titre.
   //	 */
   //	public Titre(Integer id, String t) {
   //		this.titreId = id;
   //		this.titre = t;
   //	}

   @Id
   @Column(name = "TITRE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTitreId(){
      return titreId;
   }

   public void setTitreId(final Integer tId){
      this.titreId = tId;
   }

   @Column(name = "TITRE", nullable = false, length = 100)
   public String getTitre(){
      return titre;
   }

   public void setTitre(final String newTitre){
      this.titre = newTitre;
   }

   @OneToMany(mappedBy = "titre")
   public Set<Collaborateur> getCollaborateurs(){
      return collaborateurs;
   }

   public void setCollaborateurs(final Set<Collaborateur> newCollaborateurs){
      this.collaborateurs = newCollaborateurs;
   }

   /**
    * 2 titres sont considérés comme égaux s'ils ont le même titre.
    * @param obj est le titre à tester.
    * @return true si les titres sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Titre test = (Titre) obj;
      return ((this.titre == test.titre || (this.titre != null && this.titre.equals(test.titre))));
   }

   /**
    * Le hashcode est calculé sur l'attribut titre.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashTitre = 0;

      if(this.titre != null){
         hashTitre = this.titre.hashCode();
      }

      hash = 31 * hash + hashTitre;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.titre + "}";
   }

}
