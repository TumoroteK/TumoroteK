/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (14/07/2014)
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
package fr.aphp.tumorotek.model.ui;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table UI_REQUETE.
 * Classe créée le 16/07/14.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.11
 *
 */
@Entity
@Table(name = "UI_REQUETE")
@NamedQueries(value = {
   @NamedQuery(name = "UiRequete.findByUtilisateurAndEntite",
      query = "SELECT u FROM UiRequete u WHERE u.utilisateur = ?1 " + " AND u.entite = ?2 ORDER BY ordre"),
   @NamedQuery(name = "UiRequete.findByNomUtilisateurAndEntite",
      query = "SELECT u FROM UiRequete u WHERE u.utilisateur = ?1 " + " AND u.entite = ?2 AND u.nom = ?3 ORDER BY ordre")})
public class UiRequete implements java.io.Serializable //FIXME Class non utilisée ?? A quoi sert-elle ? Elle n'est pas créée en base non plus.
{

   private static final long serialVersionUID = -7133884479005275879L;

   private Integer uiRequeteId;
   private String nom;
   private Utilisateur utilisateur;
   private Entite entite;
   private Integer ordre;

   private Set<UiCompValue> uiCompValues = new HashSet<>();

   public UiRequete(){}

   public UiRequete(final Integer id, final Utilisateur u, final Entite e, final String n, final Integer o){
      setUiRequeteId(id);
      setUtilisateur(u);
      setEntite(e);
      setNom(n);
      setOrdre(o);
   }

   @Id
   @Column(name = "UI_REQUETE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getUiRequeteId(){
      return uiRequeteId;
   }

   public void setUiRequeteId(final Integer uId){
      this.uiRequeteId = uId;
   }

   @Column(name = "NOM", nullable = false, length = 250)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @ManyToOne
   @JoinColumn(name = "UTILISATEUR_ID", nullable = false)
   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   @ManyToOne
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   @Column(name = "ORDRE", nullable = false)
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(final Integer o){
      this.ordre = o;
   }

   @OneToMany(mappedBy = "uiRequete", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
   @OrderBy(value = "uiCompValueId")
   public Set<UiCompValue> getUiCompValues(){
      return uiCompValues;
   }

   public void setUiCompValues(final Set<UiCompValue> uicv){
      this.uiCompValues = uicv;
   }

   /**
    * 2 uiRequetes sont considérées comme égales si elles ont le même nom,
    * appartiennent au même utilisateur et pour la même entité.
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
      final UiRequete test = (UiRequete) obj;
      return ((this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur)))
         && (this.entite == test.entite || (this.entite != null && this.entite.equals(test.entite)))
         && (this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom))));
   }

   /**
    * Le hashcode est calculé sur les attributs définissant l'égalité.
    * i.e. utilisateur, entite et nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashUtilisateur = 0;
      int hashEntite = 0;
      int hashNom = 0;

      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }
      if(this.entite != null){
         hashEntite = this.entite.hashCode();
      }
      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }

      hash = 31 * hash + hashUtilisateur;
      hash = 31 * hash + hashEntite;
      hash = 31 * hash + hashNom;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.entite != null){
         return "{" + this.nom + ", " + this.entite.getNom() + "}";
      }else{
         return "{Empty UiRequete}";
      }
   }

}
