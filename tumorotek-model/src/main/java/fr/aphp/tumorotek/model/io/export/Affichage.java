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
package fr.aphp.tumorotek.model.io.export;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Objet persistant mappant la table AFFICHAGE.
 * Classe créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
@Entity
@Table(name = "AFFICHAGE")
@NamedQueries(
   value = {@NamedQuery(name = "Affichage.findByUtilisateur", query = "SELECT a FROM Affichage a WHERE " + "a.createur = ?1"),
      @NamedQuery(name = "Affichage.findByBanque",
         query = "SELECT a FROM Affichage a WHERE " + "a.banque = ?1 " + "ORDER BY a.intitule"),
      @NamedQuery(name = "Affichage.findByBanqueInList",
         query = "SELECT a FROM Affichage a WHERE " + "a.banque in (?1) " + "ORDER BY a.intitule"),
      @NamedQuery(name = "Affichage.findByIntitule", query = "SELECT a FROM Affichage a " + "WHERE a.intitule like ?1"),
      @NamedQuery(name = "Affichage.findByIntituleUtilisateur",
         query = "SELECT a FROM Affichage a " + "WHERE a.intitule like ?1 " + "AND a.createur = ?2"),
      @NamedQuery(name = "Affichage.findByExcludedId", query = "SELECT a FROM Affichage a " + "WHERE a.affichageId != ?1")})
public class Affichage implements TKdataObject, Comparable<Affichage>
{

   private Integer affichageId;

   private String intitule;

   private Utilisateur createur;

   private Integer nbLignes;

   private Banque banque;

   private List<Resultat> resultats = new ArrayList<>();

   private Set<Recherche> recherches = new HashSet<>();

   public Affichage(){
      super();
   }

   public Affichage(final String i, final Utilisateur cr, final Integer nbL){
      super();
      this.intitule = i;
      this.createur = cr;
      this.nbLignes = nbL;
      resultats = new ArrayList<>();
   }

   public Affichage(final String i, final Utilisateur cr, final Integer nbL, final List<Resultat> res){
      super();
      this.intitule = i;
      this.createur = cr;
      this.nbLignes = nbL;
      this.resultats = res;
   }

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "AFFICHAGE_ID", unique = true, nullable = false)
   public Integer getAffichageId(){
      return affichageId;
   }

   public void setAffichageId(final Integer affId){
      this.affichageId = affId;
   }

   @Column(name = "INTITULE", nullable = false)
   public String getIntitule(){
      return intitule;
   }

   public void setIntitule(final String i){
      this.intitule = i;
   }

   @OneToOne
   @JoinColumn(name = "CREATEUR_ID")
   public Utilisateur getCreateur(){
      return createur;
   }

   public void setCreateur(final Utilisateur cr){
      this.createur = cr;
   }

   @ManyToOne(optional = false)
   @JoinColumn(name = "BANQUE_ID")
   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   @Column(name = "NB_LIGNES", nullable = false)
   public Integer getNbLignes(){
      return nbLignes;
   }

   public void setNbLignes(final Integer nbL){
      this.nbLignes = nbL;
   }

   @OneToMany(mappedBy = "affichage")
   @OrderBy("position")
   public List<Resultat> getResultats(){
      return resultats;
   }

   public void setResultats(final List<Resultat> res){
      this.resultats = res;
   }

   @OneToMany(mappedBy = "affichage", cascade = CascadeType.REMOVE)
   public Set<Recherche> getRecherches(){
      return recherches;
   }

   public void setRecherches(final Set<Recherche> recherches){
      this.recherches = recherches;
   }

   public void ajouterResultat(final Resultat resultat){
      this.resultats.add(resultat);
   }

   public void supprimerResultatByIndex(final Integer index){
      this.resultats.remove(index);
   }

   public void deplacerResultat(final Resultat resultat, final Integer nouvellePosition){
      final Integer pivot = resultat.getPosition();
      if(pivot != nouvellePosition){
         final Iterator<Resultat> it = resultats.iterator();
         while(it.hasNext()){
            final Resultat temp = it.next();
            if(temp.getPosition() <= nouvellePosition && temp.getPosition() > pivot){
               temp.setPosition(temp.getPosition() - 1);
            }else if(temp.getPosition() >= nouvellePosition && temp.getPosition() < pivot){
               temp.setPosition(temp.getPosition() + 1);
            }
         }
      }
      resultat.setPosition(nouvellePosition);
   }

   public void renommerResultat(final Integer index, final String i){
      this.resultats.get(index).setNomColonne(i);
   }

   public void exporterVersFichierTabule(){
      // TODO reste à faire ???
   }

   /**
    * 2 affichages sont considérés comme égaux s'ils ont
    * le même intitulé et le même créateur.
    * @param obj est l'affichage à tester.
    * @return true si les affichages sont égaux.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }
      final Affichage test = (Affichage) obj;
      if(this.createur == null){
         if(test.createur == null){
            if(this.intitule == null){
               return (test.intitule == null);
            }
            return (this.intitule.equals(test.intitule));
         }
         return false;
      }else if(this.createur.equals(test.createur)){
         if(this.intitule == null){
            return (test.intitule == null);
         }
         return this.intitule.equals(test.intitule);
      }else{
         return false;
      }
   }

   /**
    * Le hashcode est calculé sur les attributs intitulé et créateur.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      Integer hashIntitule = 0;
      Integer hashCreateur = 0;

      if(this.createur != null){
         hashCreateur = this.createur.hashCode();
      }
      if(this.intitule != null){
         hashIntitule = this.intitule.hashCode();
      }

      hash = 31 * hash + hashCreateur;
      hash = 31 * hash + hashIntitule;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.intitule != null){
         return "{" + this.intitule + "}";
      }
      return "{Empty Affichage}";
   }

   @Override
   public Affichage clone(){
      final Affichage clone = new Affichage();
      clone.setCreateur(this.createur);
      clone.setIntitule(this.intitule);
      clone.setAffichageId(this.affichageId);
      clone.setNbLignes(this.nbLignes);
      clone.setResultats(this.resultats);
      clone.setBanque(this.banque);
      return clone;
   }

   @Override
   public int compareTo(final Affichage arg0){
      return this.intitule.compareTo(arg0.getIntitule());
   }

   public String popup(){
      final String retour = "";
      if(resultats != null){
         for(int i = 0; i < resultats.size(); i++){
            if(retour.equals("")){
               retour.concat(resultats.get(i).toString());
            }else{
               retour.concat(" - " + resultats.get(i).getNomColonne());
            }
         }
      }
      return retour;
   }

   @Override
   public Integer listableObjectId(){
      return getAffichageId();
   }

}
