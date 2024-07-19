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

import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Objet persistant mappant la table PROFIL_UTILISATEUR.
 * Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 *
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.2.4.1
 *
 */
@Entity
@Table(name = "PROFIL_UTILISATEUR")
@AssociationOverrides({@AssociationOverride(name = "pk.profil", joinColumns = @JoinColumn(name = "PROFIL_ID")),
   @AssociationOverride(name = "pk.utilisateur", joinColumns = @JoinColumn(name = "UTILISATEUR_ID")),
   @AssociationOverride(name = "pk.banque", joinColumns = @JoinColumn(name = "BANQUE_ID"))})
@NamedQueries(value = {
   @NamedQuery(name = "ProfilUtilisateur.findDoublon", query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk = ?1"),
   @NamedQuery(name = "ProfilUtilisateur.findByExcludedPK", query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk != ?1"),
   @NamedQuery(name = "ProfilUtilisateur.findByProfil",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.profil = ?1 AND p.pk.utilisateur.archive = ?2"
         + " ORDER BY p.pk.utilisateur.login"),
   @NamedQuery(name = "ProfilUtilisateur.findByUtilisateur",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.utilisateur = ?1 AND p.pk.banque.archive = ?2 "
         + "order by p.pk.banque.nom"),
   @NamedQuery(name = "ProfilUtilisateur.findByBanque",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.banque = ?1 AND p.pk.utilisateur.archive = ?2 "
         + "ORDER BY p.pk.utilisateur.login"),
   @NamedQuery(name = "ProfilUtilisateur.findByUtilisateurProfil",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.utilisateur = ?1 AND p.pk.profil = ?2"),
   @NamedQuery(name = "ProfilUtilisateur.findByUtilisateurBanque",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.utilisateur = ?1 AND p.pk.banque = ?2"),
   @NamedQuery(name = "ProfilUtilisateur.findByBanqueProfil",
      query = "SELECT p FROM ProfilUtilisateur p " + "WHERE p.pk.banque = ?1 AND p.pk.profil = ?2"),
   @NamedQuery(name = "ProfilUtilisateur.findCountDistinctProfilForUserAndPlateforme",
      query = "SELECT count(distinct p.pk.profil) FROM ProfilUtilisateur p JOIN p.pk.banque b "
         + "WHERE p.pk.utilisateur = ?1 AND b.plateforme = ?2")})
public class ProfilUtilisateur implements Serializable, Comparable<ProfilUtilisateur>
{

   private static final long serialVersionUID = 1L;

   private ProfilUtilisateurPK pk = new ProfilUtilisateurPK();

   public ProfilUtilisateur(){}

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "profil", column = @Column(name = "PROFIL_ID")),
      @AttributeOverride(name = "utilisateur", column = @Column(name = "UTILISATEUR_ID")),
      @AttributeOverride(name = "banque", column = @Column(name = "BANQUE_ID"))})
   public ProfilUtilisateurPK getPk(){
      return pk;
   }

   public void setPk(final ProfilUtilisateurPK ppk){
      this.pk = ppk;
   }

   @Transient
   public Utilisateur getUtilisateur(){
      return this.getPk().getUtilisateur();
   }

   public void setUtilisateur(final Utilisateur util){
      this.getPk().setUtilisateur(util);
   }

   @Transient
   public Banque getBanque(){
      return this.getPk().getBanque();
   }

   public void setBanque(final Banque bank){
      this.getPk().setBanque(bank);
   }

   @Transient
   public Profil getProfil(){
      return this.getPk().getProfil();
   }

   public void setProfil(final Profil prof){
      this.getPk().setProfil(prof);
   }

   /**
    * 2 profils sont considérés comme égaux s'ils ont la
    * même clé embedded et le même admin.
    * @param obj est le profil à tester.
    * @return true si les profils sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final ProfilUtilisateur test = (ProfilUtilisateur) obj;
      return ((this.pk == test.pk || (this.pk != null && this.pk.equals(test.pk))));
   }

   /**
    * Le hashcode est calculé sur l'attribut admin et la
    * cle embedded.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashPk = 0;

      if(this.pk != null){
         hashPk = this.pk.hashCode();
      }

      hash = 7 * hash + hashPk;

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
      return "{Empty ProfilUtilisateur}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone DroitObjet.
    */
   @Override
   public ProfilUtilisateur clone(){
      final ProfilUtilisateur clone = new ProfilUtilisateur();

      clone.setPk(this.getPk());

      return clone;
   }

   @Override
   public int compareTo(final ProfilUtilisateur o){
      return this.getUtilisateur().compareTo(o.getUtilisateur());
   }

}
