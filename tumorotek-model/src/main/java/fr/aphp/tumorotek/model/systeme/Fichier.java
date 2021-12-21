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

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 *
 * Objet persistant mappant la table FICHIER.
 * Classe créée le 10/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Entity
@Table(name = "FICHIER")
//@NamedQueries(value = {@NamedQuery(name = "Fichier.findByPath", query = "SELECT f FROM Fichier f WHERE f.path like ?1"),
//   @NamedQuery(name = "Fichier.findByEchantillonId",
//      query = "SELECT f FROM Fichier f " + "WHERE f.echantillon.echantillonId = ?1"),
//   @NamedQuery(name = "Fichier.findFilesSharingPathForEchans",
//      query = "SELECT e FROM Echantillon e " + "WHERE e.crAnapath.path = ?1"),
//   @NamedQuery(name = "Fichier.findFilesSharingPathForAnnos",
//      query = "SELECT a FROM AnnotationValeur a " + "WHERE a.fichier.path = ?1"),
//   @NamedQuery(name = "Fichier.findByExcludedId", query = "SELECT f FROM Fichier f WHERE f.fichierId != ?1"),
//   @NamedQuery(name = "Fichier.findByOrder", query = "SELECT f FROM Fichier f ORDER BY f.path")})
public class Fichier implements java.io.Serializable
{

   private static final long serialVersionUID = 786784651431543113L;

   private Integer fichierId;
   private String path;
   private String nom;
   private String mimeType;

   private Echantillon echantillon;
   private AnnotationValeur valeur;

   /** Constructeur par défaut. */
   public Fichier(){}

   /**
    * Constructeur avec paramètres.
    * @param id .
    * @param p .
    */
   public Fichier(final Integer id, final String p){
      this.fichierId = id;
      this.path = p;
   }

   @Id
   @Column(name = "FICHIER_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getFichierId(){
      return fichierId;
   }

   public void setFichierId(final Integer fId){
      this.fichierId = fId;
   }

   @Column(name = "NOM", nullable = false, length = 50)
   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   @Column(name = "PATH", nullable = false, length = 250)
   public String getPath(){
      return path;
   }

   public void setPath(final String p){
      this.path = p;
   }

   @Column(name = "MIMETYPE", nullable = false, length = 100)
   public String getMimeType(){
      return mimeType;
   }

   public void setMimeType(final String mType){
      this.mimeType = mType;
   }

   @OneToOne(mappedBy = "crAnapath")
   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon echan){
      this.echantillon = echan;
   }

   @OneToOne(mappedBy = "fichier")
   public AnnotationValeur getAnnotationValeur(){
      return valeur;
   }

   public void setAnnotationValeur(final AnnotationValeur val){
      this.valeur = val;
   }

   /**
    * 2 objets sont considérés comme égaux s'ils ont le même path.
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
      final Fichier test = (Fichier) obj;
      return (((this.path != null && this.path.equals(test.path)) || this.path == test.path)
         && (this.echantillon == test.echantillon || (this.echantillon != null && this.echantillon.equals(test.echantillon)))
         && (this.valeur == test.valeur || (this.valeur != null && this.valeur.equals(test.valeur))));
   }

   /**
    * Le hashcode est calculé sur les attributs path/echantillon/valeur.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashPath = 0;
      int hashEchantillon = 0;
      int hashValeur = 0;

      if(this.path != null){
         hashPath = this.path.hashCode();
      }
      if(this.echantillon != null){
         hashEchantillon = this.echantillon.hashCode();
      }
      if(this.valeur != null){
         hashValeur = this.valeur.hashCode();
      }

      hash = 31 * hash + hashPath;
      hash = 31 * hash + hashEchantillon;
      hash = 31 * hash + hashValeur;

      return hash;

   }

   /**
    * Méthode surchargeant le toString() de l'objet.
    */
   @Override
   public String toString(){
      if(this.path != null){
         return "{" + this.path + "}";
      }else{
         return "{Empty Fichier}";
      }
   }

   @Override
   public Fichier clone(){
      final Fichier clone = new Fichier();
      clone.setFichierId(getFichierId());
      clone.setNom(getNom());
      clone.setPath(getPath());
      clone.setMimeType(getMimeType());
      clone.setAnnotationValeur(getAnnotationValeur());
      clone.setEchantillon(getEchantillon());
      return clone;
   }

   public Fichier cloneNoId(){
      final Fichier clone = new Fichier();
      clone.setNom(getNom());
      clone.setPath(getPath() != null ? new String(getPath()) : null);
      clone.setMimeType(getMimeType());
      return clone;
   }

   @Transient
   public void setTKFileSettableObject(final TKFileSettableObject f){
      if(f != null){
         if(f instanceof Echantillon){
            setEchantillon((Echantillon) f);
         }else if(f instanceof AnnotationValeur){
            setAnnotationValeur((AnnotationValeur) f);
         }
      }
   }
}
