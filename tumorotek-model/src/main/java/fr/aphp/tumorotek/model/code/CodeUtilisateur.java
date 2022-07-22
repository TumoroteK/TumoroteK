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
package fr.aphp.tumorotek.model.code;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table CODE_UTILISATEUR.
 * Enregistrement des codes diagnostiques personnels aux utilisateurs,
 * cad n'appartenant pas aux classification ADICAP et CIM.
 *
 * Classe créée le 24/09/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@Entity
@Table(name = "CODE_UTILISATEUR")
@NamedQueries(value = {
   @NamedQuery(name = "CodeUtilisateur.findByCodeLike",
      query = "SELECT c FROM CodeUtilisateur c WHERE c.code like ?1 " + "AND c.banque in (?2)"),
   @NamedQuery(name = "CodeUtilisateur.findByLibelleLike",
      query = "SELECT c FROM CodeUtilisateur c WHERE c.libelle like ?1 " + "AND c.banque in (?2)"),
   @NamedQuery(name = "CodeUtilisateur.findByUtilisateurAndBanque",
      query = "SELECT c FROM CodeUtilisateur c " + "WHERE c.utilisateur = ?1 AND c.banque = ?2"),
   @NamedQuery(name = "CodeUtilisateur.findByCodeDossier",
      query = "SELECT c FROM CodeUtilisateur c " + "WHERE c.codeDossier = ?1"),
   @NamedQuery(name = "CodeUtilisateur.findByRootDossier",
      query = "SELECT c FROM CodeUtilisateur c " + "WHERE c.codeDossier is null AND c.codeParent is null "
         + "AND c.banque = ?1 ORDER BY c.codeUtilisateurId"),
   @NamedQuery(name = "CodeUtilisateur.findByCodeParent", query = "SELECT c FROM CodeUtilisateur c " + "WHERE c.codeParent = ?1"),
   @NamedQuery(name = "CodeUtilisateur.findByExcludedId",
      query = "SELECT c FROM CodeUtilisateur c " + "WHERE c.codeUtilisateurId != ?1"),
   @NamedQuery(name = "CodeUtilisateur.findByTranscodage", query = "SELECT c FROM CodeUtilisateur c " + "JOIN c.transcodes t "
      + "WHERE t.tableCodage = ?1 AND t.codeId = ?2 " + "AND c.banque in (?3) ORDER BY c.code")})
public class CodeUtilisateur implements CodeCommon, Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer codeUtilisateurId;

   private String code;

   private String libelle;

   private Utilisateur utilisateur;

   private Banque banque;

   private CodeDossier codeDossier;

   private CodeUtilisateur codeParent;

   private Set<CodeUtilisateur> codesUtilisateur = new HashSet<>();

   private Set<TranscodeUtilisateur> transcodes = new HashSet<>();

   private CodeSelect codeSelect;

   public CodeUtilisateur(){}

   @Id
   @Column(name = "CODE_UTILISATEUR_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getCodeUtilisateurId(){
      return codeUtilisateurId;
   }

   public void setCodeUtilisateurId(final Integer id){
      this.codeUtilisateurId = id;
   }

   @Override
   @Column(name = "CODE", nullable = false, length = 50)
   public String getCode(){
      return code;
   }

   @Override
   public void setCode(final String c){
      this.code = c;
   }

   @Override
   @Column(name = "LIBELLE", length = 300)
   public String getLibelle(){
      return libelle;
   }

   @Override
   public void setLibelle(final String l){
      this.libelle = l;
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
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   @ManyToOne
   @JoinColumn(name = "CODE_DOSSIER_ID", nullable = true)
   public CodeDossier getCodeDossier(){
      return codeDossier;
   }

   public void setCodeDossier(final CodeDossier cDos){
      this.codeDossier = cDos;
   }

   @ManyToOne
   @JoinColumn(name = "CODE_PARENT_ID", nullable = true)
   public CodeUtilisateur getCodeParent(){
      return codeParent;
   }

   public void setCodeParent(final CodeUtilisateur cP){
      this.codeParent = cP;
   }

   @OneToMany(mappedBy = "codeParent")
   public Set<CodeUtilisateur> getCodesUtilisateur(){
      return codesUtilisateur;
   }

   public void setCodesUtilisateur(final Set<CodeUtilisateur> codesU){
      this.codesUtilisateur = codesU;
   }

   @OneToMany(mappedBy = "codeUtilisateur", cascade = {CascadeType.ALL})
   public Set<TranscodeUtilisateur> getTranscodes(){
      return transcodes;
   }

   public void setTranscodes(final Set<TranscodeUtilisateur> codes){
      this.transcodes = codes;
   }

   /**
    * 2 codes utilisateur sont considérés comme égaux s'ils ont le
    * meme code, et les memes references vers l'utilisateur et la banque.
    * @param obj est le code à tester.
    * @return true si les codes sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final CodeUtilisateur test = (CodeUtilisateur) obj;
      return ((this.code == test.code || (this.code != null && this.code.equals(test.code)))
         && (this.utilisateur == test.utilisateur || (this.utilisateur != null && this.utilisateur.equals(test.utilisateur)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque))));
   }

   /**
    * Le hashcode est calculé sur tous le code et les references
    * vers l'utilisateur et la banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashCode = 0;
      int hashBanque = 0;
      int hashUtilisateur = 0;

      if(this.code != null){
         hashCode = this.code.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }
      if(this.utilisateur != null){
         hashUtilisateur = this.utilisateur.hashCode();
      }

      hash = 7 * hash + hashCode;
      hash = 7 * hash + hashBanque;
      hash = 7 * hash + hashUtilisateur;

      return hash;
   }

   @Override
   public String toString(){
      if(this.code != null){
         return "{CodeUtilisateur: " + this.code + "}";
      }
      return "{Empty CodeUtilisateur}";
   }

   @Override
   public CodeUtilisateur clone(){
      final CodeUtilisateur clone = new CodeUtilisateur();
      clone.setCodeUtilisateurId(this.getCodeUtilisateurId());
      clone.setCode(this.getCode());
      clone.setLibelle(this.getLibelle());
      clone.setBanque(this.getBanque());
      clone.setUtilisateur(this.getUtilisateur());
      clone.setCodeDossier(this.getCodeDossier());
      clone.setCodeParent(this.getCodeParent());
      clone.setCodesUtilisateur(this.getCodesUtilisateur());
      clone.setTranscodes(this.getTranscodes());
      clone.setCodeSelect(this.getCodeSelect());
      return clone;
   }

   @Override
   @Transient
   public Integer getCodeId(){
      return getCodeUtilisateurId();
   }

   @Override
   public void setCodeId(final Integer id){
      this.codeUtilisateurId = id;
   }

   @Override
   @Transient
   public CodeSelect getCodeSelect(){
      return codeSelect;
   }

   @Override
   public void setCodeSelect(final CodeSelect s){
      codeSelect = s;
   }
}
