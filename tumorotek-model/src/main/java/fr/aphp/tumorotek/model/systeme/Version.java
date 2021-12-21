/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.model.systeme;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table VERSION.
 * Classe créée le 17/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.1.4
 * @since 2.0
 */
@Entity
@Table(name = "VERSION")
//@NamedQueries(value = {@NamedQuery(name = "Version.findByVersion", query = "SELECT v FROM Version v WHERE v.version = ?1"),
//   @NamedQuery(name = "Version.findByDate", query = "SELECT v FROM Version v WHERE v.date = ?1"),
//   @NamedQuery(name = "Version.findByDateChronologique", query = "SELECT v FROM Version v ORDER BY v.date ASC"),
//   @NamedQuery(name = "Version.findByDateAntiChronologique", query = "SELECT v FROM Version v ORDER BY v.date DESC"),
//   @NamedQuery(name = "Version.findDoublon", query = "SELECT v FROM Version v WHERE v.version = ?1 AND v.date = ?2")})
public class Version implements Serializable
{
   private Integer versionId;
   private String version;
   private Date date;
   private String nomSite;
   private static final long serialVersionUID = 8764654676154L;

   public Version(){}

   @Id
   @Column(name = "VERSION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getVersionId(){
      return versionId;
   }

   public void setVersionId(final Integer vid){
      this.versionId = vid;
   }

   @Column(name = "VERSION", nullable = false, length = 20)
   public String getVersion(){
      return this.version;
   }

   public void setVersion(final String v){
      this.version = v;
   }

   @Column(name = "\"DATE\"", nullable = true)
   public Date getDate(){
      if(date != null){
         return new Date(date.getTime());
      }else{
         return null;
      }
   }

   public void setDate(final Date d){
      if(d != null){
         this.date = new Date(d.getTime());
      }else{
         this.date = null;
      }
   }

   @Column(name = "NOM_SITE", nullable = true, length = 100)
   public String getNomSite(){
      return nomSite;
   }

   public void setNomSite(final String n){
      this.nomSite = n;
   }

   /**
    * 2 versions sont considérées comme égales si elles ont les mêmes
    * date et version.
    * @param obj est la version à tester.
    * @return true si les versions sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Version test = (Version) obj;
      if(this.version == null){
         if(test.version == null){
            if(this.date == null){
               return (test.date == null);
            }else{
               return (this.date.equals(test.date));
            }
         }else{
            return false;
         }
      }else if(this.date == null){
         if(test.date == null){
            return (this.version.equals(test.version));
         }else{
            return false;
         }
      }else{
         return (this.version.equals(test.version) && this.date.equals(test.date));
      }
   }

   /**
    * Le hashcode est calculé sur les attributs version et date.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashVersion = 0;
      int hashDate = 0;

      if(this.version != null){
         hashVersion = this.version.hashCode();
      }
      if(this.date != null){
         hashDate = this.date.hashCode();
      }

      hash = 31 * hash + hashVersion;
      hash = 31 * hash + hashDate;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.version != null || this.date != null){
         return "{" + this.version + " " + this.date + "}";
      }else{
         return "{Empty Version}";
      }
   }
}