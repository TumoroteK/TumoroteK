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
package fr.aphp.tumorotek.model.imprimante;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table AFFECTATION_IMPRIMANTE.
 * Classe créée le 21/03/2011.
 *
 * @author Pierre Ventadour
 * @see http://boris.kirzner.info/blog/archives/2008/07/19/
 * hibernate-annotations-the-many-to-many-association-with-composite-key/
 * @version 2.3
 *
 */
@Entity
@Table(name = "AFFECTATION_IMPRIMANTE")
@AssociationOverrides({@AssociationOverride(name = "pk.utilisateur", joinColumns = @JoinColumn(name = "UTILISATEUR_ID")),
   @AssociationOverride(name = "pk.banque", joinColumns = @JoinColumn(name = "BANQUE_ID")),
   @AssociationOverride(name = "pk.imprimante", joinColumns = @JoinColumn(name = "IMPRIMANTE_ID"))})
//@NamedQueries(value = {
//   @NamedQuery(name = "AffectationImprimante.findByExcludedPK",
//      query = "SELECT a FROM AffectationImprimante a " + "WHERE a.pk != ?1"),
//   @NamedQuery(name = "AffectationImprimante.findByBanqueUtilisateur",
//      query = "SELECT a FROM AffectationImprimante a " + "WHERE a.pk.banque = ?1 AND a.pk.utilisateur = ?2")})
public class AffectationImprimante implements Serializable
{

   private static final long serialVersionUID = 1136473047454996768L;

   private AffectationImprimantePK pk = new AffectationImprimantePK();
   private Modele modele;

   public AffectationImprimante(){

   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "imprimante", column = @Column(name = "IMPRIMANTE_ID")),
      @AttributeOverride(name = "utilisateur", column = @Column(name = "UTILISATEUR_ID")),
      @AttributeOverride(name = "banque", column = @Column(name = "BANQUE_ID")),
      @AttributeOverride(name = "modele", column = @Column(name = "MODELE_ID"))})
   public AffectationImprimantePK getPk(){
      return pk;
   }

   public void setPk(final AffectationImprimantePK p){
      this.pk = p;
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
   public Imprimante getImprimante(){
      return this.getPk().getImprimante();
   }

   public void setImprimante(final Imprimante i){
      this.getPk().setImprimante(i);
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "MODELE_ID", nullable = true)
   public Modele getModele(){
      return this.modele;
   }

   public void setModele(final Modele m){
      this.modele = m;
   }

   /**
    * 2 profils sont considérés comme égaux s'ils ont la 
    * même clé embedded et le même admin.
    * @param obj est le profil à tester.
    * @return true si les profils sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

	   HERE -> refact equals
	   remettre lazy ?
	   
      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final AffectationImprimante test = (AffectationImprimante) obj;
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
      return "{Empty AffectationImprimante}";
   }

   /**
    * Cree un clone de l'objet.
    * @return clone DroitObjet.
    */
   @Override
   public AffectationImprimante clone(){
      final AffectationImprimante clone = new AffectationImprimante();

      clone.setPk(this.getPk());

      return clone;
   }
}
