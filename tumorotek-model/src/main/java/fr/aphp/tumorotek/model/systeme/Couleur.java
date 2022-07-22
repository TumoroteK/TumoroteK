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
package fr.aphp.tumorotek.model.systeme;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Objet persistant mappant la table COULEUR.
 * Classe créée le 29/04/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
@Entity
@Table(name = "COULEUR")
@NamedQueries(value = {@NamedQuery(name = "Couleur.findByCouleur", query = "SELECT c FROM Couleur c WHERE c.couleur like ?1"),
   @NamedQuery(name = "Couleur.findByVisotube",
      query = "SELECT c FROM Couleur c " + "WHERE c.ordreVisotube is not null " + "ORDER BY c.ordreVisotube")})
public class Couleur implements Serializable
{

   private static final long serialVersionUID = 5511266910422376116L;

   private Integer couleurId;

   private String couleur;

   private String hexa;

   private Integer ordreVisotube;

   private Set<Banque> banquesEchantillons = new HashSet<>();

   private Set<Banque> banquesProdDerives = new HashSet<>();

   private Set<CouleurEntiteType> couleurEntiteTypes = new HashSet<>();

   private Set<Enceinte> enceintes = new HashSet<>();

   private Set<Terminale> terminales = new HashSet<>();

   /** Constructeur par défaut. */
   public Couleur(){

   }

   @Id
   @Column(name = "COULEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCouleurId(){
      return couleurId;
   }

   public void setCouleurId(final Integer cId){
      this.couleurId = cId;
   }

   @Column(name = "COULEUR", nullable = false, length = 25)
   public String getCouleur(){
      return couleur;
   }

   public void setCouleur(final String c){
      this.couleur = c;
   }

   @Column(name = "HEXA", nullable = false, length = 10)
   public String getHexa(){
      return hexa;
   }

   public void setHexa(final String h){
      this.hexa = h;
   }

   @Transient
   public String getHexaCssStyle(){
      return "color: " + getHexa();
   }

   @Transient
   public String getCouleurMinCase(){
      if(getCouleur() != null){
         return getCouleur().toLowerCase();
      }
      return null;
   }

   @Column(name = "ORDRE_VISOTUBE", nullable = true)
   public Integer getOrdreVisotube(){
      return ordreVisotube;
   }

   public void setOrdreVisotube(final Integer o){
      this.ordreVisotube = o;
   }

   @OneToMany(mappedBy = "echantillonCouleur")
   public Set<Banque> getBanquesEchantillons(){
      return banquesEchantillons;
   }

   public void setBanquesEchantillons(final Set<Banque> banques){
      this.banquesEchantillons = banques;
   }

   @OneToMany(mappedBy = "prodDeriveCouleur")
   public Set<Banque> getBanquesProdDerives(){
      return banquesProdDerives;
   }

   public void setBanquesProdDerives(final Set<Banque> banques){
      this.banquesProdDerives = banques;
   }

   @OneToMany(mappedBy = "couleur")
   public Set<CouleurEntiteType> getCouleurEntiteTypes(){
      return couleurEntiteTypes;
   }

   public void setCouleurEntiteTypes(final Set<CouleurEntiteType> cTypes){
      this.couleurEntiteTypes = cTypes;
   }

   @OneToMany(mappedBy = "couleur")
   public Set<Enceinte> getEnceintes(){
      return enceintes;
   }

   public void setEnceintes(final Set<Enceinte> e){
      this.enceintes = e;
   }

   @OneToMany(mappedBy = "couleur")
   public Set<Terminale> getTerminales(){
      return terminales;
   }

   public void setTerminales(final Set<Terminale> t){
      this.terminales = t;
   }

   /**
    * 2 couleurs sont considérées comme égales si elles ont le même nom.
    * @param obj est la couleur à tester.
    * @return true si les couleurs sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Couleur test = (Couleur) obj;
      return ((this.couleur == test.couleur || (this.couleur != null && this.couleur.equals(test.couleur))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashCouleur = 0;

      if(this.couleur != null){
         hashCouleur = this.couleur.hashCode();
      }

      hash = 31 * hash + hashCouleur;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.couleur + "}";
   }

}
