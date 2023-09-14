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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;

/**
 * Objet persistant mappant la table DIAGNOSTIC.
 * Contexte SeroTK.
 * Classe créée le 05/06/2018
 *
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 *
 */
@Entity
@Table(name = "DIAGNOSTIC")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "DIAGNOSTIC_ID"))})
@GenericGenerator(name = "autoincrement", strategy = "increment")
@NamedQueries(value = {
   @NamedQuery(name = "Diagnostic.findByNom", query = "SELECT p FROM Diagnostic p WHERE p.nom like ?1 " + "order by p.nom"),
   @NamedQuery(name = "Diagnostic.findByExcludedId", query = "SELECT p FROM Diagnostic p " + "WHERE p.id != ?1"),
   @NamedQuery(name = "Diagnostic.findByOrder", query = "SELECT p FROM Diagnostic p " + "ORDER BY p.nom"), @NamedQuery(
      name = "Diagnostic.findByPfOrder", query = "SELECT p FROM Diagnostic p " + "WHERE p.plateforme = ?1 ORDER BY p.nom")})
public class Diagnostic extends AbstractPfDependantThesaurusObject implements Serializable
{

   private static final long serialVersionUID = -2506949180590820975L;

   private String description;

   private Set<Maladie> maladies = new HashSet<>();

   /** Constructeur par défaut. */
   public Diagnostic(){}

   @Column(name = "DESCRIPTION", nullable = true)
   //Lob
   public String getDescription(){
      return description;
   }

   public void setDescription(final String description){
      this.description = description;
   }

   @OneToMany(mappedBy = "diagnostic", targetEntity = MaladieSero.class)
   public Set<Maladie> getMaladies(){
      return maladies;
   }

   public void setMaladies(final Set<Maladie> maladies){
      this.maladies = maladies;
   }

   @Override
   public String toString(){
      if(this.getNom() != null){
         return "{" + this.getNom() + "}";
      }
      return "{Empty Diagnostic}";
   }

}
