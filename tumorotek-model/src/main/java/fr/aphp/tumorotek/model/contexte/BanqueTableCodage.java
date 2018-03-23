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

import fr.aphp.tumorotek.model.code.TableCodage;

@Entity
@Table(name = "BANQUE_TABLE_CODAGE")
@AssociationOverrides({
   @AssociationOverride(name = "pk.banque", joinColumns = @JoinColumn(name = "BANQUE_ID", referencedColumnName = "BANQUE_ID")),
   @AssociationOverride(name = "pk.tableCodage",
      joinColumns = @JoinColumn(name = "TABLE_CODAGE_ID", referencedColumnName = "TABLE_CODAGE_ID"))})
@NamedQueries(value = {@NamedQuery(name = "BanqueTableCodage.findByBanque",
   query = "SELECT b FROM BanqueTableCodage b " + "WHERE b.pk.banque = ?1 ORDER BY b.pk.tableCodage.nom")})
public class BanqueTableCodage
{

   private BanqueTableCodagePK pk = new BanqueTableCodagePK();
   private Boolean libelleExport;

   @Override
   public String toString(){
      if(this.getBanque() != null && this.getTableCodage() != null){
         return "{" + this.getBanque().getNom() + " - " + this.getTableCodage().getNom() + "}";
      }
      return "{Empty BanqueTableCodage}";
   }

   @EmbeddedId
   @AttributeOverrides({@AttributeOverride(name = "banque", column = @Column(name = "BANQUE_ID")),
      @AttributeOverride(name = "tableCodage", column = @Column(name = "TABLE_CODAGE_ID"))})
   public BanqueTableCodagePK getPk(){
      return pk;
   }

   public void setPk(final BanqueTableCodagePK btck){
      this.pk = btck;
   }

   @Column(name = "LIBELLE_EXPORT", nullable = false)
   public Boolean getLibelleExport(){
      return libelleExport;
   }

   public void setLibelleExport(final Boolean l){
      this.libelleExport = l;
   }

   @Transient
   public Banque getBanque(){
      return this.pk.getBanque();
   }

   public void setBanque(final Banque b){
      this.pk.setBanque(b);
   }

   @Transient
   public TableCodage getTableCodage(){
      return this.pk.getTableCodage();
   }

   public void setTableCodage(final TableCodage t){
      this.pk.setTableCodage(t);
   }

   /**
    * 2 banqueTableCodage sont considérées comme égales 
    * si elles ont la même pk.
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
      final BanqueTableCodage test = (BanqueTableCodage) obj;
      return (this.pk != null && (this.pk == test.pk || this.pk.equals(test.pk)));
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

      hash = 7 * hash + hashPk;

      return hash;
   }
}
