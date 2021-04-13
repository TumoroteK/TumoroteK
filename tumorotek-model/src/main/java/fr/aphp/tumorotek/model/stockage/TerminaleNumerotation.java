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

/**
 *
 * Objet persistant mappant la table TERMINALE_NUMEROTATION.
 * Classe créée le 11/09/09.
 *
 * @author Maxime Gousseau
 * @version 2.0
 *
 */
@Entity
@Table(name = "TERMINALE_NUMEROTATION")
@NamedQueries(value = {
   @NamedQuery(name = "TerminaleNumerotation.findByLigne", query = "SELECT t FROM TerminaleNumerotation t WHERE t.ligne = ?1"),
   @NamedQuery(name = "TerminaleNumerotation.findByColonne",
      query = "SELECT t FROM TerminaleNumerotation t " + "WHERE t.colonne = ?1")})
public class TerminaleNumerotation implements Serializable
{

   private static final long serialVersionUID = -6067095158709656961L;

   private Integer terminaleNumerotationId;
   private String ligne;
   private String colonne;

   private Set<Terminale> terminales = new HashSet<>();

   /** Constructeur par défaut. */
   public TerminaleNumerotation(){}

   @Id
   @Column(name = "TERMINALE_NUMEROTATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTerminaleNumerotationId(){
      return this.terminaleNumerotationId;
   }

   public void setTerminaleNumerotationId(final Integer id){
      this.terminaleNumerotationId = id;
   }

   @Column(name = "LIGNE", nullable = false, length = 3)
   public String getLigne(){
      return this.ligne;
   }

   public void setLigne(final String lig){
      this.ligne = lig;
   }

   @Column(name = "COLONNE", nullable = false, length = 3)
   public String getColonne(){
      return this.colonne;
   }

   public void setColonne(final String col){
      this.colonne = col;
   }

   @OneToMany(mappedBy = "terminaleNumerotation")
   public Set<Terminale> getTerminales(){
      return this.terminales;
   }

   public void setTerminales(final Set<Terminale> terms){
      this.terminales = terms;
   }

   /**
    * 2 numérotations sont considérées comme égales si elles ont les mêmes
    * lignes et colonnes.
    * @param obj est la numérotation à tester.
    * @return true si les numérotations sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final TerminaleNumerotation test = (TerminaleNumerotation) obj;
      return ((this.colonne == test.colonne || (this.colonne != null && this.colonne.equals(test.colonne)))
         && (this.ligne == test.ligne || (this.ligne != null && this.ligne.equals(test.ligne))));
   }

   /**
    * Le hashcode est calculé sur les attributs ligne et prénom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashLigne = 0;
      int hashColonne = 0;

      if(this.ligne != null){
         hashLigne = this.ligne.hashCode();
      }
      if(this.colonne != null){
         hashColonne = this.colonne.hashCode();
      }

      hash = 31 * hash + hashLigne;
      hash = 31 * hash + hashColonne;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.ligne != null || this.colonne != null){
         return "{" + this.ligne + " " + this.colonne + "}";
      }else{
         return "{Empty TerminaleNumerotation}";
      }
   }
}
