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
package fr.aphp.tumorotek.model.validation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author Gille Chapelot
 *
 */
@Entity
@Table(name = "VALIDATION")
public class Validation
{

   private Integer id;
   private String libelle;
   private List<CritereValidation> criteres;
   private Set<Validation> enfants;
   private OperateursLogiques operateur;
   private NiveauValidation niveauValidation;

   /**
    * @return the id
    */
   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "VALIDATION_ID")
   public Integer getId(){
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(Integer id){
      this.id = id;
   }

   /**
    * @return the libelle
    */
   @Column(name="LIBELLE")
   public String getLibelle(){
      return libelle;
   }

   /**
    * @param libelle the libelle to set
    */
   public void setLibelle(String libelle){
      this.libelle = libelle;
   }

   /**
    * @return the criteres
    */
   @ManyToMany
   @JoinTable(name = "VALIDATION_CRITERE_VALIDATION", joinColumns = {@JoinColumn(name = "VALIDATION_ID")},
   inverseJoinColumns = @JoinColumn(name = "CRITERE_VALIDATION_ID"))
   @LazyCollection(LazyCollectionOption.FALSE)
   public List<CritereValidation> getCriteres(){
      return criteres;
   }

   /**
    * @param criteres the criteres to set
    */
   public void setCriteres(List<CritereValidation> criteres){
      this.criteres = criteres;
   }

   /**
    * @return the enfants
    */
   @ManyToMany
   @JoinTable(name = "SOUS_VALIDATION", joinColumns = {@JoinColumn(name = "VALIDATION_ID")},
   inverseJoinColumns = @JoinColumn(name = "SOUS_VALIDATION_ID"))
   @LazyCollection(LazyCollectionOption.FALSE)
   public Set<Validation> getEnfants(){
      return enfants;
   }

   /**
    * @param enfants the enfants to set
    */
   public void setEnfants(Set<Validation> enfants){
      this.enfants = enfants;
   }

   /**
    * @return the operateur
    */
   @Enumerated(EnumType.STRING)
   public OperateursLogiques getOperateur(){
      return operateur;
   }

   /**
    * @param operateur the operateur to set
    */
   public void setOperateur(OperateursLogiques operateur){
      this.operateur = operateur;
   }

   /**
    * @return the niveauValidation
    */
   @ManyToOne
   @JoinColumn(name="NIVEAU_VALIDATION_ID")
   public NiveauValidation getNiveauValidation(){
      return niveauValidation;
   }

   /**
    * @param niveauValidation the niveauValidation to set
    */
   public void setNiveauValidation(NiveauValidation niveauValidation){
      this.niveauValidation = niveauValidation;
   }

   @Override
   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {

      StringBuilder sb = new StringBuilder("Validation = [libelle: " + libelle + " ; ");

      for(Iterator<CritereValidation> iterator = criteres.iterator(); iterator.hasNext();){
         CritereValidation critereValidation = (CritereValidation) iterator.next();
         sb.append(critereValidation);
         sb.append( iterator.hasNext() ? (" " + operateur + " ") : "" );
      }

      if( !criteres.isEmpty() && !enfants.isEmpty() ) {
         sb.append(" " + operateur + " ");
      }
      
      for(Iterator<Validation> iterator = enfants.iterator(); iterator.hasNext();){
         Validation validation = (Validation) iterator.next();
         sb.append(validation);
         sb.append( iterator.hasNext() ? (" " + operateur + " ") : "" );
      }
      
      return sb.append("]").toString();

   }

}
