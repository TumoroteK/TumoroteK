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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;

/**
 * Objet persistant mappant la table PROTOCOLE.
 * Contexte SeroTK.
 * Classe créée le 09/01/12.
 *
 * @author MAthieu BARTHELEMY
 * @version 2.0.6
 *
 */
@Entity
@Table(name = "PROTOCOLE")
@NamedQueries(value = {
   @NamedQuery(name = "Protocole.findByNom", query = "SELECT p FROM Protocole p WHERE p.nom like ?1 " + "order by p.nom"),
   @NamedQuery(name = "Protocole.findByExcludedId", query = "SELECT p FROM Protocole p " + "WHERE p.protocoleId != ?1"),
   @NamedQuery(name = "Protocole.findByOrder", query = "SELECT p FROM Protocole p " + "WHERE p.plateforme = ?1 ORDER BY p.nom")})
public class Protocole implements java.io.Serializable, TKThesaurusObject
{

   private static final long serialVersionUID = 54864135646414L;

   private Integer protocoleId;
   private String nom;
   private String description;
   private Plateforme plateforme;
   private Set<Prelevement> prelevements = new HashSet<>();

   /** Constructeur par défaut. */
   public Protocole(){}

   @Id
   @Column(name = "PROTOCOLE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getProtocoleId(){
      return protocoleId;
   }

   public void setProtocoleId(final Integer pId){
      this.protocoleId = pId;
   }

   @Override
   @Column(name = "NOM", nullable = false, length = 200)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "DESCRIPTION", nullable = true)
   //Lob
   public String getDescription(){
      return description;
   }

   public void setDescription(final String description){
      this.description = description;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return plateforme;
   }

   @Override
   public void setPlateforme(final Plateforme plateforme){
      this.plateforme = plateforme;
   }

   @ManyToMany(mappedBy = "protocoles", targetEntity = PrelevementSero.class)
   public Set<Prelevement> getPrelevements(){
      return prelevements;
   }

   public void setPrelevements(final Set<Prelevement> prelevements){
      this.prelevements = prelevements;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même type.
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
      final Protocole test = (Protocole) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom)))
         && (this.plateforme == test.plateforme || (this.plateforme != null && this.plateforme.equals(test.plateforme))));
   }

   /**
    * Le hashcode est calculé sur l'attribut type.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashPF = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.plateforme != null){
         hashPF = this.plateforme.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashPF;

      return hash;

   }

   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }
      return "{Empty Protocole}";
   }

   @Override
   @Transient
   public Integer getId(){
      return getProtocoleId();
   }

}
