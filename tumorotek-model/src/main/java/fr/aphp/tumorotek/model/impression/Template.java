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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Objet persistant mappant la table TEMPLATE.
 * Classe créée le 21/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
@Entity
@Table(name = "TEMPLATE")
@NamedQueries(value = {
   @NamedQuery(name = "Template.findByBanque",
      query = "SELECT t FROM Template t " + "WHERE t.banque = ?1 " + "ORDER BY t.entite, t.nom"),
   @NamedQuery(name = "Template.findByBanqueEntite",
      query = "SELECT t FROM Template t " + "WHERE t.banque = ?1 AND t.entite = ?2 " + "ORDER BY t.nom"),
   @NamedQuery(name = "Template.findByExcludedId",
      query = "SELECT t FROM Template t " + "WHERE t.banque = ?1 AND t.templateId != ?2")})
public class Template extends Object implements TKdataObject, Serializable
{

   private static final long serialVersionUID = 5591185846747255061L;

   private Integer templateId;

   private ETemplateType type;

   private String nom;

   private String description;

   private Banque banque;

   private Entite entite;

   //Pour types BLOC
   private String enTete;

   private String piedPage;

   private Set<TableAnnotationTemplate> tableAnnotationTemplates = new HashSet<>();

   private Set<BlocImpressionTemplate> blocImpressionTemplates = new HashSet<>();

   private Set<ChampImprime> champImprimes = new HashSet<>();

   //Pour types DOC
   private String fichier;

   private List<CleImpression> cleImpressionList = new ArrayList<>();

   public Template(){

   }

   @Id
   @Column(name = "TEMPLATE_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getTemplateId(){
      return templateId;
   }

   public void setTemplateId(final Integer id){
      this.templateId = id;
   }

   @Column(name = "TYPE", nullable = false)
   @Enumerated(EnumType.STRING)
   public ETemplateType getType(){
      return type;
   }

   public void setType(final ETemplateType type){
      this.type = type;
   }

   @Column(name = "NOM", nullable = false)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "DESCRIPTION", nullable = true)
   public String getDescription(){
      return description;
   }

   public void setDescription(final String desc){
      this.description = desc;
   }

   @Column(name = "EN_TETE", nullable = true)
   public String getEnTete(){
      return enTete;
   }

   public void setEnTete(final String tete){
      this.enTete = tete;
   }

   @Column(name = "PIED_PAGE", nullable = true)
   public String getPiedPage(){
      return piedPage;
   }

   public void setPiedPage(final String pied){
      this.piedPage = pied;
   }

   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   @ManyToOne
   @JoinColumn(name = "ENTITE_ID", nullable = false)
   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   @OneToMany(mappedBy = "pk.template")
   public Set<BlocImpressionTemplate> getBlocImpressionTemplates(){
      return blocImpressionTemplates;
   }

   public void setBlocImpressionTemplates(final Set<BlocImpressionTemplate> blocImpressions){
      this.blocImpressionTemplates = blocImpressions;
   }

   @OneToMany(mappedBy = "pk.template")
   public Set<ChampImprime> getChampImprimes(){
      return champImprimes;
   }

   public void setChampImprimes(final Set<ChampImprime> champIs){
      this.champImprimes = champIs;
   }

   @OneToMany(mappedBy = "template", fetch = FetchType.LAZY)
   public List<CleImpression> getCleImpressionList(){
      return cleImpressionList;
   }

   public void setCleImpressionList(final List<CleImpression> cleImpressionList){
      this.cleImpressionList = cleImpressionList;
   }

   @OneToMany(mappedBy = "pk.template")
   public Set<TableAnnotationTemplate> getTableAnnotationTemplates(){
      return tableAnnotationTemplates;
   }

   public void setTableAnnotationTemplates(final Set<TableAnnotationTemplate> tables){
      this.tableAnnotationTemplates = tables;
   }

   /**
    * Path du fichier modèle
    * @return Path du fichier modèle
    */
   @Column(name = "FICHIER")
   public String getFichier(){
      return fichier;
   }

   /**
    * Path du fichier modèle
    * @param fichier Path du fichier modèle
    */
   public void setFichier(final String fichier){
      this.fichier = fichier;
   }

   /**
    * 2 templates sont considérées comme égaux s'ils ont le même
    * nom et la même banque.
    * @param obj est le template à tester.
    * @return true si les templates sont égaux.
    */
   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }else if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }else{
         final Template test = Template.class.cast(obj);
         if(this.nom == null){
            if(test.nom == null){
               if(this.banque == null){
                  return (test.banque == null);
               }else{
                  return (this.banque.equals(test.banque));
               }
            }else{
               return false;
            }
         }else if(this.banque == null){
            if(test.banque == null){
               return (this.nom.equals(test.nom));
            }else{
               return false;
            }
         }else{
            return (this.nom.equals(test.nom) && this.banque.equals(test.banque));
         }
      }
   }

   /**
    * Le hashcode est calculé sur les attributs nom et banque.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashNom = 0;
      int hashBanque = 0;

      if(this.nom != null){
         hashNom = this.nom.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }

      hash = 31 * hash + hashNom;
      hash = 31 * hash + hashBanque;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.nom != null){
         return "{" + this.nom + "}";
      }
      return "{Empty Template}";
   }

   @Override
   public Template clone(){
      final Template clone = new Template();

      clone.setTemplateId(this.templateId);
      clone.setType(this.type);
      clone.setBanque(this.banque);
      clone.setNom(this.nom);
      clone.setEntite(this.entite);
      clone.setDescription(this.description);
      clone.setFichier(this.fichier);
      clone.setEnTete(this.enTete);
      clone.setPiedPage(this.piedPage);

      return clone;
   }

   @Override
   public Integer listableObjectId(){
      return getTemplateId();
   }

}
