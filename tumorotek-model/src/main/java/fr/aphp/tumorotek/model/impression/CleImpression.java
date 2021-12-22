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
package fr.aphp.tumorotek.model.impression;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.io.export.Champ;

/**
 *
 * Objet persistant mappant la table CLE_IMPRESSION.
 * Une cleImpression est associée à un Champ.
 * Utilisée pour remplacer le nom de la clé par la valeur du champ, dans du texte ou un document.
 * 
 * Classe créée le 16/01/2018.
 *
 * @author Answald Bournique
 * @version 2.3
 *
 */
@Entity
@Table(name = "CLE_IMPRESSION")
//@NamedQueries(value = {@NamedQuery(name = "CleImpression.findByName", query = "SELECT c FROM CleImpression c WHERE c.nom = ?1"),
////   @NamedQuery(name = "CleImpression.findByTemplate", query = "SELECT c FROM CleImpression c WHERE c.template = ?1"),
//   @NamedQuery(name = "CleImpression.findByChamp", query = "SELECT c FROM CleImpression c WHERE c.champ = ?1")})
public class CleImpression implements Serializable
{

   private static final long serialVersionUID = -401351001870593573L;

   /**
    * Id de la clé
    */
   private Integer cleId;

   /**
    * Nom de la clé
    */
   private String nom;

   /**
    * Champ associé à la clé
    */
   private Champ champ;

   /**
    * Template associé
    */
//   private Template template;

   public CleImpression(){

   }

   /**
    * Id de la clé
    * @return Id de la clé
    */
   @Id
   @Column(name = "CLE_IMPRESSION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCleId(){
      return cleId;
   }

   /**
    * Id de la clé 
    * @param id Id de la clé
    */
   public void setCleId(final Integer id){
      this.cleId = id;
   }

   /**
    * Nom de la clé
    * @return Nom de la clé
    */
   @Column(name = "NOM", nullable = false)
   public String getNom(){
      return nom;
   }

   /**
    * Nom de la clé
    * @param nom Nom de la clé
    */
   public void setNom(final String nom){
      this.nom = nom;
   }

   /**
    * Champ associé
    * @return Champ associé
    */
   @ManyToOne
   @JoinColumn(name = "CHAMP_ID")
   public Champ getChamp(){
      return champ;
   }

   /**
    * Champ associé
    * @param champ Champ associé
    */
   public void setChamp(final Champ champ){
      this.champ = champ;
   }

   /**
    * Template associé
    * @return Template associé
    */
//   @ManyToOne(targetEntity = Template.class)
//   @JoinColumn(name = "TEMPLATE_ID")
//   public Template getTemplate(){
//      return template;
//   }

   /**
    * Template associé
    * @param template Template associé
    */
//   public void setTemplate(final Template template){
//      this.template = template;
//   }

   @Override
   public String toString(){
      return this.nom;
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((champ == null) ? 0 : champ.hashCode());
      result = prime * result + ((cleId == null) ? 0 : cleId.hashCode());
      result = prime * result + ((nom == null) ? 0 : nom.hashCode());
//      result = prime * result + ((template == null) ? 0 : template.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if(obj == null){
         return false;
      }
      if(getClass() != obj.getClass()){
         return false;
      }
      final CleImpression other = (CleImpression) obj;
      if(cleId == null){
         if(other.cleId != null){
            return false;
         }
      }else if(!cleId.equals(other.cleId)){
         return false;
      }
      if(nom == null){
         if(other.nom != null){
            return false;
         }
      }else if(!nom.equals(other.nom)){
         return false;
      }
//      if(template == null){
//         if(other.template != null){
//            return false;
//         }
//      }else if(!template.equals(other.template)){
//         return false;
//      }
      return true;
   }

}
