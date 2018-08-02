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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;

/**
 *
 * Objet persistant mappant la table UTILISATEUR.
 * Classe créée le 10/09/09.
 *
 * @since 2.0.10 Ajout de la colonne plateforme origine pour spécifier la
 * plateforme d'origine de création d'un utilisateur.
 * @since 2.0.13 Modification de findTimeOutBefore pour retourner tous les
 * utilisateurs pas encore archivés alors que timeout dépassé.
 *
 * @author Pierre Ventadour
 * @version 2.0.13
 *
 */
@Entity
@Table(name = "UTILISATEUR")
@NamedQueries(value = {
   @NamedQuery(name = "Utilisateur.findByLogin", query = "SELECT u FROM Utilisateur u WHERE u.login = ?1 " + "ORDER BY archive"),
   @NamedQuery(name = "Utilisateur.findByLoginAndArchive",
      query = "SELECT u FROM Utilisateur u " + "WHERE u.login = ?1 AND u.archive = ?2 AND u.superAdmin = 0 "
         + "AND u.plateformeOrig in (?3)"),
   @NamedQuery(name = "Utilisateur.findByArchive",
      query = "SELECT u FROM Utilisateur u " + "WHERE u.archive = ?1 AND u.superAdmin = 0 " + "AND u.plateformeOrig in (?2)"),
   @NamedQuery(name = "Utilisateur.findBydnLdap", query = "SELECT u FROM Utilisateur u WHERE u.dnLdap = ?1"),
   @NamedQuery(name = "Utilisateur.findByEmail", query = "SELECT u FROM Utilisateur u WHERE u.email = ?1"),
   @NamedQuery(name = "Utilisateur.findByTimeOut", query = "SELECT u FROM Utilisateur u WHERE u.timeOut = ?1"),
   @NamedQuery(name = "Utilisateur.findByTimeOutBefore",
      query = "SELECT u FROM Utilisateur u WHERE u.archive = 0 " + "and u.timeOut < ?1"),
   @NamedQuery(name = "Utilisateur.findByTimeOutAfter", query = "SELECT u FROM Utilisateur u WHERE u.timeOut > ?1"),
   @NamedQuery(name = "Utilisateur.findByCollaborateur", query = "SELECT u FROM Utilisateur u " + "WHERE u.collaborateur = ?1"),
   @NamedQuery(name = "Utilisateur.findByReservationId",
      query = "SELECT u FROM Utilisateur u " + "left join u.reservations r " + "WHERE r.reservationId = ?1"),
   @NamedQuery(name = "Utilisateur.findByIdWithFetch",
      query = "SELECT u FROM Utilisateur u LEFT JOIN FETCH " + "u.collaborateur WHERE u.utilisateurId = ?1"),
   @NamedQuery(name = "Utilisateur.findByExcludedId", query = "SELECT u FROM Utilisateur u " + "WHERE u.utilisateurId != ?1"),
   @NamedQuery(name = "Utilisateur.findByOrder",
      query = "SELECT u FROM Utilisateur u WHERE superAdmin = 0 " + "ORDER BY u.login"),
   @NamedQuery(name = "Utilisateur.findByOrderWithArchiveExcludeSuperAdmin",
      query = "SELECT u FROM Utilisateur u " + "WHERE u.archive = ?1 and u.superAdmin = 0 " + "AND u.plateformeOrig in (?2) "
         + "ORDER BY u.login"),
   @NamedQuery(name = "Utilisateur.findByOrderWithArchiveIncludeSuperAdmin",
   query = "SELECT u FROM Utilisateur u " + "WHERE u.archive = ?1 and ( u.plateformeOrig in (?2) OR (u.superAdmin = 1 ))"
      + "ORDER BY u.login"),
   @NamedQuery(name = "Utilisateur.findByLoginPassAndArchive",
      query = "SELECT u FROM Utilisateur u " + "WHERE u.login = ?1 " + "AND u.password = ?2 " + "AND u.archive = ?3")})
public class Utilisateur implements TKdataObject, java.io.Serializable, Comparable<Utilisateur>
{

   private static final long serialVersionUID = 225451547843654684L;

   private Integer utilisateurId;
   private String login;
   private String password;
   private Boolean archive = false;
   private String encodedPassword;
   private String dnLdap;
   private String email;
   private Date timeOut;
   private boolean superAdmin;
   private Plateforme plateformeOrig;

   private Collaborateur collaborateur;
   private Set<Reservation> reservations = new HashSet<>();
   private Set<CodeSelect> codeSelects = new HashSet<>();
   private Set<CodeUtilisateur> codeUtilisateurs = new HashSet<>();
   private Set<ProfilUtilisateur> profilUtilisateurs = new HashSet<>();
   private Set<Plateforme> plateformes = new HashSet<>();
   private Set<AffectationImprimante> affectationImprimantes = new HashSet<>();

   /** Constructeur par défaut. */
   public Utilisateur(){}

   @Id
   @Column(name = "UTILISATEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getUtilisateurId(){
      return utilisateurId;
   }

   public void setUtilisateurId(final Integer id){
      this.utilisateurId = id;
   }

   @Column(name = "LOGIN", nullable = false, length = 100)
   public String getLogin(){
      return login;
   }

   public void setLogin(final String log){
      this.login = log;
   }

   @Column(name = "PASSWORD", nullable = false, length = 100)
   public String getPassword(){
      return password;
   }

   public void setPassword(final String pass){
      this.password = pass;
   }

   @Column(name = "ARCHIVE", nullable = false)
   public boolean isArchive(){
      return archive;
   }

   public void setArchive(final boolean arch){
      this.archive = arch;
   }

   @Column(name = "ENCODED_PASSWORD", nullable = true, length = 100)
   public String getEncodedPassword(){
      return encodedPassword;
   }

   public void setEncodedPassword(final String encodedPass){
      this.encodedPassword = encodedPass;
   }

   @Column(name = "DN_LDAP", nullable = true, length = 100)
   public String getDnLdap(){
      return dnLdap;
   }

   public void setDnLdap(final String ldap){
      this.dnLdap = ldap;
   }

   @Column(name = "EMAIL", nullable = true, length = 50)
   public String getEmail(){
      return email;
   }

   public void setEmail(final String mail){
      this.email = mail;
   }

   @Column(name = "TIMEOUT", nullable = true)
   public Date getTimeOut(){
      if(timeOut != null){
         return new Date(timeOut.getTime());
      }else{
         return null;
      }
   }

   public void setTimeOut(final Date time){
      if(time != null){
         this.timeOut = new Date(time.getTime());
      }else{
         this.timeOut = null;
      }
   }

   @Column(name = "SUPER", nullable = false)
   public boolean isSuperAdmin(){
      return superAdmin;
   }

   public void setSuperAdmin(final boolean superA){
      this.superAdmin = superA;
   }

   @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinColumn(name = "COLLABORATEUR_ID", nullable = true)
   public Collaborateur getCollaborateur(){
      return collaborateur;
   }

   public void setCollaborateur(final Collaborateur c){
      this.collaborateur = c;
   }

   @OneToMany(mappedBy = "utilisateur")
   public Set<Reservation> getReservations(){
      return reservations;
   }

   public void setReservations(final Set<Reservation> res){
      this.reservations = res;
   }

   @OneToMany(mappedBy = "utilisateur")
   public Set<CodeSelect> getCodeSelects(){
      return codeSelects;
   }

   public void setCodeSelects(final Set<CodeSelect> codes){
      this.codeSelects = codes;
   }

   @OneToMany(mappedBy = "utilisateur")
   public Set<CodeUtilisateur> getCodeUtilisateurs(){
      return codeUtilisateurs;
   }

   public void setCodeUtilisateurs(final Set<CodeUtilisateur> codes){
      this.codeUtilisateurs = codes;
   }

   @OneToMany(mappedBy = "pk.utilisateur")
   public Set<ProfilUtilisateur> getProfilUtilisateurs(){
      return profilUtilisateurs;
   }

   public void setProfilUtilisateurs(final Set<ProfilUtilisateur> profils){
      this.profilUtilisateurs = profils;
   }

   @ManyToMany(targetEntity = Plateforme.class)
   @JoinTable(name = "PLATEFORME_ADMINISTRATEUR", joinColumns = @JoinColumn(name = "UTILISATEUR_ID"),
      inverseJoinColumns = @JoinColumn(name = "PLATEFORME_ID"))
   public Set<Plateforme> getPlateformes(){
      return this.plateformes;
   }

   public void setPlateformes(final Set<Plateforme> pfs){
      this.plateformes = pfs;
   }

   @OneToMany(mappedBy = "pk.utilisateur", cascade = {CascadeType.REMOVE})
   public Set<AffectationImprimante> getAffectationImprimantes(){
      return affectationImprimantes;
   }

   public void setAffectationImprimantes(final Set<AffectationImprimante> aImprimantes){
      this.affectationImprimantes = aImprimantes;
   }

   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ORIG_ID", nullable = true)
   public Plateforme getPlateformeOrig(){
      return plateformeOrig;
   }

   public void setPlateformeOrig(final Plateforme p){
      this.plateformeOrig = p;
   }

   /**
    * 2 utilisateurs sont considérées comme égaux s'ils ont le même login.
    * @param obj est la utilisateur à tester.
    * @return true si les utilisateurs sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Utilisateur test = (Utilisateur) obj;
      return ((this.login == test.login || (this.login != null && this.login.equals(test.login))));
   }

   /**
    * Le hashcode est calculé sur les attributs login.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashLogin = 0;

      if(this.login != null){
         hashLogin = this.login.hashCode();
      }

      hash = 31 * hash + hashLogin;

      return hash;
   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.login != null){
         return "{" + this.login + "}";
      }else{
         return "{Empty Utilisateur}";
      }
   }

   /**
    * Cree un clone de l'objet.
    * @return clone Utilisateur.
    */
   @Override
   public Utilisateur clone(){
      final Utilisateur clone = new Utilisateur();

      clone.setUtilisateurId(this.utilisateurId);
      clone.setLogin(this.login);
      clone.setPassword(this.password);
      clone.setArchive(this.archive);
      clone.setEncodedPassword(this.encodedPassword);
      clone.setDnLdap(this.dnLdap);
      clone.setEmail(this.email);
      clone.setTimeOut(this.timeOut);
      clone.setCollaborateur(this.collaborateur);
      clone.setSuperAdmin(this.superAdmin);
      clone.setReservations(this.reservations);
      clone.setCodeSelects(this.codeSelects);
      clone.setCodeUtilisateurs(this.codeUtilisateurs);
      clone.setProfilUtilisateurs(this.profilUtilisateurs);
      clone.setPlateformes(this.plateformes);
      clone.setAffectationImprimantes(this.affectationImprimantes);
      clone.setPlateformeOrig(getPlateformeOrig());

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getUtilisateurId();
   }

   @Override
   public int compareTo(final Utilisateur us){
      return this.getLogin().compareToIgnoreCase(us.getLogin());
   }
}
