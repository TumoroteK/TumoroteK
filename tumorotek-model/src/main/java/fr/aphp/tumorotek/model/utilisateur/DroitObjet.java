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
package fr.aphp.tumorotek.model.utilisateur;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table DROIT_OBJET.
 * Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.0
 *
 */
@Entity
@Table(name = "DROIT_OBJET")
@AssociationOverrides({@AssociationOverride(name = "pk.profil", joinColumns = @JoinColumn(name = "PROFIL_ID")),
   @AssociationOverride(name = "pk.entite", joinColumns = @JoinColumn(name = "ENTITE_ID")),
   @AssociationOverride(name = "pk.operationType", joinColumns = @JoinColumn(name = "OPERATION_TYPE_ID"))})
@NamedQueries(value = {@NamedQuery(name = "DroitObjet.findByDoublon", query = "SELECT d FROM DroitObjet d WHERE d.pk = ?1"),
   @NamedQuery(name = "DroitObjet.findByExcludedPK", query = "SELECT d FROM DroitObjet d " + "WHERE d.pk != ?1"),
   @NamedQuery(name = "DroitObjet.findByProfil", query = "SELECT d FROM DroitObjet d " + "WHERE d.pk.profil = ?1"),
   @NamedQuery(name = "DroitObjet.findByProfilEntite",
      query = "SELECT d FROM DroitObjet d " + "WHERE d.pk.profil = ?1 AND d.pk.entite = ?2"),
   @NamedQuery(name = "DroitObjet.findByProfilOperation",
      query = "SELECT d FROM DroitObjet d " + "WHERE d.pk.profil = ?1 " + "AND d.pk.operationType = ?2")})
public class DroitObjet implements Serializable
{

   private static final long serialVersionUID = 1L;

   private DroitObjetPK pk = new DroitObjetPK();

   /** Constructeur par défaut. */
   public DroitObjet(){}

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "profil", column = @Column(name = "PROFIL_ID")),
      @AttributeOverride(name = "entite", column = @Column(name = "ENTITE_ID")),
      @AttributeOverride(name = "operationType", column = @Column(name = "OPERATION_TYPE_ID"))})
   public DroitObjetPK getPk(){
      return pk;
   }

   public void setPk(final DroitObjetPK dpk){
      this.pk = dpk;
   }

   @Transient
   public Entite getEntite(){
      return this.pk.getEntite();
   }

   public void setEntite(final Entite ent){
      this.pk.setEntite(ent);
   }

   @Transient
   public Profil getProfil(){
      return this.pk.getProfil();
   }

   public void setProfil(final Profil prof){
      this.pk.setProfil(prof);
   }

   @Transient
   public OperationType getOperationType(){
      return this.pk.getOperationType();
   }

   public void setOperationType(final OperationType op){
      this.pk.setOperationType(op);
   }

   /**
    * 2 Droits Objet sont consideres egales
    * si elles ont la même PK.
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
      final DroitObjet test = (DroitObjet) obj;
      return ((this.pk == test.pk || (this.pk != null && this.pk.equals(test.pk))));
   }

   /**
    * Le hashcode est calculé sur la pk.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){
      int hash = 7;
      int hashPk = 0;

      if(this.pk != null){
         hashPk = this.pk.hashCode();
      }

      hash = 31 * hash + hashPk;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.pk != null){
         return "{" + this.pk.toString() + "}";
      }
      return "{Empty DroitObjet}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone DroitObjet.
    */
   @Override
   public DroitObjet clone(){
      final DroitObjet clone = new DroitObjet();

      clone.setPk(this.getPk());

      return clone;
   }
}
