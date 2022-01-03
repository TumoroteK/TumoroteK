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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stockage.ConteneurPlateforme;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Objet persistant mappant la table PLATEFORME.
 * Classe créée le 09/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "PLATEFORME")
//@NamedQueries(value = {@NamedQuery(name = "Plateforme.findByNom", query = "SELECT p FROM Plateforme p WHERE p.nom = ?1"),
//   @NamedQuery(name = "Plateforme.findByAlias", query = "SELECT p FROM Plateforme p WHERE p.alias = ?1"),
//   @NamedQuery(name = "Plateforme.findByCollaborateur", query = "SELECT p FROM Plateforme p " + "WHERE p.collaborateur = ?1"),
//   @NamedQuery(name = "Plateforme.findByBanqueId",
//      query = "SELECT p FROM Plateforme p " + "left join p.banques b " + "WHERE b.banqueId = ?1"),
//   @NamedQuery(name = "Plateforme.findByIdWithFetch",
//      query = "SELECT p FROM Plateforme p LEFT JOIN FETCH " + "p.collaborateur WHERE p.plateformeId = ?1"),
//   @NamedQuery(name = "Plateforme.findByOrder", query = "SELECT p FROM Plateforme p ORDER BY p.nom"),
//   @NamedQuery(name = "Plateforme.findByExcludedId", query = "SELECT p FROM Plateforme p " + "WHERE p.plateformeId != ?1")})
public class Plateforme implements java.io.Serializable, TKFantomableObject, TKdataObject, Comparable<Plateforme>
{

   private static final long serialVersionUID = 54873512464151L;

   private Integer plateformeId;
   private String nom;
   private String alias;

   private Collaborateur collaborateur;
   private Set<Banque> banques = new HashSet<>();
   private Set<ConteneurPlateforme> conteneurPlateformes = new HashSet<>();
   private Set<Utilisateur> utilisateurs = new HashSet<>();
   private Set<Imprimante> imprimantes = new HashSet<>();
   private Set<Modele> modeles = new HashSet<>();
   private Set<SModele> sModeles = new HashSet<>();
   private Set<Contrat> contrats = new HashSet<>();
   private Set<NonConformite> nonConformites = new HashSet<>();

   /**
    * Constructeur par défaut.
    */
   public Plateforme(){}

   /**
    * Constructeur avec paramètres.
    * @param id est l'identifiant de l'objet dans la base de données.
    * @param n est le nom de la plateforme.
    * @param a est l'alias.
    */
   public Plateforme(final Integer id, final String n, final String a){
      this.plateformeId = id;
      this.nom = n;
      this.alias = a;
   }

   @Id
   @Column(name = "PLATEFORME_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getPlateformeId(){
      return plateformeId;
   }

   public void setPlateformeId(final Integer pId){
      this.plateformeId = pId;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "ALIAS", nullable = true, length = 5)
   public String getAlias(){
      return alias;
   }

   public void setAlias(final String a){
      this.alias = a;
   }

   @ManyToOne
   @JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
   public Collaborateur getCollaborateur(){
      return collaborateur;
   }

   public void setCollaborateur(final Collaborateur c){
      this.collaborateur = c;
   }

   @OneToMany(mappedBy = "plateforme")
   @OrderBy("nom")
   public Set<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final Set<Banque> newBanques){
      this.banques = newBanques;
   }

   @OneToMany(mappedBy = "pk.plateforme")
   public Set<ConteneurPlateforme> getConteneurPlateformes(){
      return conteneurPlateformes;
   }

   public void setConteneurPlateformes(final Set<ConteneurPlateforme> conts){
      this.conteneurPlateformes = conts;
   }

   @ManyToMany(mappedBy = "plateformes", targetEntity = Utilisateur.class)
   public Set<Utilisateur> getUtilisateurs(){
      return utilisateurs;
   }

   public void setUtilisateurs(final Set<Utilisateur> u){
      this.utilisateurs = u;
   }

   @OneToMany(mappedBy = "plateforme")
   public Set<Imprimante> getImprimantes(){
      return imprimantes;
   }

   public void setImprimantes(final Set<Imprimante> i){
      this.imprimantes = i;
   }

   @OneToMany(mappedBy = "plateforme")
   public Set<Modele> getModeles(){
      return modeles;
   }

   public void setModeles(final Set<Modele> m){
      this.modeles = m;
   }

   @OneToMany(mappedBy = "plateforme")
   public Set<SModele> getSModeles(){
      return sModeles;
   }

   public void setSModeles(final Set<SModele> ss){
      this.sModeles = ss;
   }

   @OneToMany(mappedBy = "plateforme")
   public Set<Contrat> getContrats(){
      return contrats;
   }

   public void setContrats(final Set<Contrat> c){
      this.contrats = c;
   }

   @OneToMany(mappedBy = "plateforme")
   public Set<NonConformite> getNonConformites(){
      return nonConformites;
   }

   public void setNonConformites(final Set<NonConformite> nonConformites){
      this.nonConformites = nonConformites;
   }

   /**
    * 2 plateformes sont considérées comme égales si elles ont le même nom.
    * @param obj est la plateforme à tester.
    * @return true si les plateformes sont égales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Plateforme test = (Plateforme) obj;
      return ((this.nom == test.nom || (this.nom != null && this.nom.equals(test.nom))));
   }

   /**
    * Le hashcode est calculé sur l'attribut nom.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }

      hash = 31 * hash + hashNom;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      return "{" + this.nom + "}";
   }

   @Override
   public Plateforme clone(){
      final Plateforme clone = new Plateforme();

      clone.setPlateformeId(this.plateformeId);
      clone.setCollaborateur(this.collaborateur);
      clone.setNom(this.nom);
      clone.setAlias(this.alias);
      clone.setBanques(this.banques);
      clone.setConteneurPlateformes(this.conteneurPlateformes);
      clone.setUtilisateurs(this.utilisateurs);
      clone.setImprimantes(this.imprimantes);
      clone.setModeles(this.modeles);
      clone.setSModeles(this.sModeles);

      return clone;
   }

   @Override
   public String entiteNom(){
      return "Plateforme";
   }

   @Override
   @Transient
   public Integer listableObjectId(){
      return getPlateformeId();
   }

   @Override
   @Transient
   public String getPhantomData(){
      return getNom();
   }

   @Override
   public int compareTo(final Plateforme arg0){
      return this.getNom().compareTo(arg0.getNom());
   }

}
