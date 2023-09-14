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
package fr.aphp.tumorotek.model.coeur.annotation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 * Objet persistant mappant la table ANNOTATION_VALEUR.
 *
 * Date: 15/09/2009
 *
 * @author Mathieu Barthelemy
 * @version 2.2.0
 * @since 2.0
 *
 */
@Entity
@Table(name = "ANNOTATION_VALEUR")
@NamedQueries(value = {
   @NamedQuery(name = "AnnotationValeur.findByChampAndObjetId",
      query = "SELECT a FROM AnnotationValeur a WHERE" + " a.champAnnotation = ?1 AND a.objetId = ?2"),
   @NamedQuery(name = "AnnotationValeur.findByTableAndBanque",
      query = "SELECT a FROM AnnotationValeur a JOIN a.champAnnotation c" + " WHERE c.tableAnnotation = ?1 AND a.banque = ?2"),
   @NamedQuery(name = "AnnotationValeur.findByExcludedId",
      query = "SELECT a FROM AnnotationValeur a " + "WHERE a.champAnnotation = ?1 AND a.objetId = ?2 "
         + "AND a.annotationValeurId != ?3"),
   @NamedQuery(name = "AnnotationValeur.findByObjectIdAndEntite",
      query = "SELECT a FROM AnnotationValeur a " + "join a.champAnnotation as chp " + "join chp.tableAnnotation as tbl "
         + "WHERE a.objetId = ?1 AND tbl.entite = ?2 " + "ORDER BY a.champAnnotation.id"),
   @NamedQuery(name = "AnnotationValeur.findCountByItem",
      query = "SELECT count(a) FROM AnnotationValeur a WHERE " + "a.item = ?1"),
   @NamedQuery(name = "AnnotationValeur.findCountByTableAnnotationBanque", query = "SELECT count(a) FROM AnnotationValeur a "
      + "JOIN a.champAnnotation c " + "WHERE c.tableAnnotation = ?1 AND a.banque = ?2")})
public class AnnotationValeur extends AnnotationCommon implements Serializable, TKFileSettableObject
{

   private final Log log = LogFactory.getLog(AnnotationValeur.class);

   private static final long serialVersionUID = 1L;

   private Integer annotationValeurId;

   private Integer objetId;

   private String alphanum;

   private String texte;

   private Calendar date;

   private Boolean bool;

   private Item item;

   private Fichier fichier;

   private InputStream stream = null;

   private ChampAnnotation champAnnotation;

   private Banque banque;

   /** Constructeur par défaut. */
   public AnnotationValeur(){}

   @Id
   @Column(name = "ANNOTATION_VALEUR_ID")
   @GeneratedValue(generator = "autoincrement")
   // @GenericGenerator(name = "autoincrement", strategy = "increment")
   @GenericGenerator(name = "autoincrement", strategy = "native",
      parameters = {@Parameter(name = "sequence", value = "annoValSeq")})
   public Integer getAnnotationValeurId(){
      return this.annotationValeurId;
   }

   public void setAnnotationValeurId(final Integer id){
      this.annotationValeurId = id;
   }

   @Column(name = "OBJET_ID", nullable = false)
   public Integer getObjetId(){
      return this.objetId;
   }

   public void setObjetId(final Integer objId){
      this.objetId = objId;
   }

   @Override
   @Column(name = "ALPHANUM", nullable = true, length = 100)
   public String getAlphanum(){
      return this.alphanum;
   }

   @Override
   public void setAlphanum(final String alpha){
      this.alphanum = alpha;
   }

   @Override
   //@Lob
   @Column(name = "TEXTE", nullable = true, length = 4000)
   public String getTexte(){
      return this.texte;
   }

   @Override
   public void setTexte(final String tex){
      this.texte = tex;
   }

   @Override
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "ANNO_DATE", nullable = true)
   public Calendar getDate(){
      if(date != null){
         final Calendar cal = Calendar.getInstance();
         cal.setTime(date.getTime());
         return cal;
      }
      return null;
   }

   @Override
   public void setDate(final Calendar cal){
      if(cal != null){
         this.date = Calendar.getInstance();
         this.date.setTime(cal.getTime());
      }else{
         this.date = null;
      }
   }

   @Override
   @Column(name = "BOOL", nullable = true)
   public Boolean getBool(){
      return this.bool;
   }

   @Override
   public void setBool(final Boolean b){
      this.bool = b;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "ITEM_ID", nullable = true)
   public Item getItem(){
      return this.item;
   }

   @Override
   public void setItem(final Item it){
      this.item = it;
   }

   @OneToOne(orphanRemoval = true)
   @JoinColumn(name = "FICHIER_ID", nullable = true)
   public Fichier getFichier(){
      return this.fichier;
   }

   public void setFichier(final Fichier f){
      this.fichier = f;
   }

   // permet à l'objet de contenir sa référence vers le stream
   // utile lors de la creation d'une annotation de type fichier
   @Transient
   public InputStream getStream(){
      return this.stream;
   }

   public void setStream(final InputStream str){
      this.stream = str;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "CHAMP_ANNOTATION_ID", nullable = false)
   public ChampAnnotation getChampAnnotation(){
      return this.champAnnotation;
   }

   @Override
   public void setChampAnnotation(final ChampAnnotation chpA){
      this.champAnnotation = chpA;
   }

   @Override
   @ManyToOne
   @JoinColumn(name = "BANQUE_ID", nullable = false)
   public Banque getBanque(){
      return banque;
   }

   @Override
   public void setBanque(final Banque bank){
      this.banque = bank;
   }

   @Override
   @Transient
   public boolean isEmpty(){
      return (super.isEmpty() && (getFichier() == null));
   }

   /**
    * 2 valeurs sont considerees comme egales si ils ont les mêmes references
    * vers le champ annotation, vers l'objet et vers la banque
    * auxquelles elles sont attribuees. La propriete item intervient pour
    * differencier les valeurs lors d'une selection multiple.
    * @param obj est la valeur à tester.
    * @return true si les valeur sont egales.
    */
   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }
      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final AnnotationValeur test = (AnnotationValeur) obj;

      return (((this.objetId != null && this.objetId.equals(test.objetId)) || this.objetId == test.objetId)
         && (this.champAnnotation == test.champAnnotation
            || (this.champAnnotation != null && this.champAnnotation.equals(test.champAnnotation)))
         && (this.item == test.item || (this.item != null && this.item.equals(test.item)))
         && (this.banque == test.banque || (this.banque != null && this.banque.equals(test.banque))));
   }

   /**
    * Le hashcode est calculé sur les references vers l'objet, le
    * champ annotation, l'item auxquels la valeur est attribuee.
    * @return la valeur du hashcode.
    */
   @Override
   public int hashCode(){

      int hash = 7;
      int hashObjetId = 0;
      int hashChpId = 0;
      int hashItem = 0;
      int hashBanque = 0;

      if(this.objetId != null){
         hashObjetId = this.objetId.hashCode();
      }
      if(this.champAnnotation != null){
         hashChpId = this.champAnnotation.hashCode();
      }
      if(this.item != null){
         hashItem = this.item.hashCode();
      }
      if(this.banque != null){
         hashBanque = this.banque.hashCode();
      }

      hash = 31 * hash + hashObjetId;
      hash = 31 * hash + hashChpId;
      hash = 31 * hash + hashItem;
      hash = 31 * hash + hashBanque;

      return hash;
   }

   @Override
   public String toString(){
      String output = "{Empty AnnotationValeur}";

      if(this.champAnnotation != null){
         output = "{Valeur: " + this.champAnnotation.getNom() + ".";
         if(this.alphanum != null){
            output = output + this.alphanum;
         }else if(this.bool != null){
            output = output + bool.toString();
         }else if(this.date != null){
            output = output + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.date.getTime());
         }else if(this.texte != null){
            if(texte.length() > 5){
               output = output + texte.substring(0, 4) + "...";
            }else{
               output = output + texte;
            }
         }else if(this.item != null){
            output = output + item.getLabel();
         }else if(this.fichier != null){
            output = output + fichier.toString();
         }else{
            return "{Empty AnnotationValeur}";
         }
         output = output + "}";
      }
      return output;
   }

   /**
    * Cree un clone d'une valeur d'annotation.
    * @return clone
    */
   @Override
   public AnnotationValeur clone(){
      final AnnotationValeur clone = new AnnotationValeur();
      clone.setAnnotationValeurId(this.getAnnotationValeurId());
      clone.setChampAnnotation(this.getChampAnnotation());
      clone.setObjetId(this.getObjetId());
      clone.setAlphanum(this.getAlphanum());
      clone.setBool(this.getBool());
      clone.setDate(this.getDate());
      clone.setTexte(this.getTexte());
      if(getFichier() != null){
         clone.setFichier(this.getFichier().clone());
      }else{
         clone.setFichier(null);
      }
      clone.setStream(cloneStream());
      // clone.setStream(getStream());
      clone.setItem(this.getItem());
      clone.setBanque(this.getBanque());
      return clone;
   }

   /**
    * Copie le stream associé à un objet annotation valeur.
    * @return copie du stream.
    */
   public InputStream cloneStream(){
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      if(getStream() != null){
         try{
            int count;
            final byte[] buffer = new byte[8192];
            while((count = getStream().read(buffer)) > 0){
               bos.write(buffer, 0, count);
            }
            final byte[] ba = bos.toByteArray();
            getStream().close();
            setStream(new ByteArrayInputStream(ba));
            return new ByteArrayInputStream(ba);
         }catch(final IOException e){
            log.error(e);
            return null;
         }
      }
      return null;

   }

   public String formateAnnotationValeur(){
      String value = "";

      if(this.alphanum != null){
         value = this.alphanum;
      }else if(this.texte != null){
         value = this.texte;
      }else if(this.date != null){
         value = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.date.getTime());
      }else if(this.bool != null){
         if(this.bool){
            return "Oui";
         }
         return "Non";
      }else if(this.item != null){
         value = this.item.getLabel();
      }else if(this.fichier != null){ // @since version 2.1 TEST???
         value = this.fichier.getNom();
      }

      return value;
   }

   /**
    * Renvoie l'objet valeur en fonction du data type du champ.
    * @return Object type String, Boolean, Double, Date
    * @since 2.0
    */
   @Transient
   public Object getValeur(){
      Object valeur = null;
      if(null != champAnnotation && null != champAnnotation.getDataType()){
         valeur = getValeur(champAnnotation.getDataType());
      }
      return valeur;
   }

   /**
    * Renvoie l'objet valeur en fonction du data type du champ.
    * @param dataType datatype du champ
    * @return Object type String, Boolean, Double, Date
    * @since 2.2.0
    */
   @Transient
   private Object getValeur(final DataType dataType){
      Object valeur = null;
      switch(dataType.getType()){
         case "alphanum":
         case "hyperlien":
            valeur = getAlphanum();
            break;
         case "num":
            valeur = Double.parseDouble(getAlphanum());
            break;
         case "boolean":
            valeur = getBool();
            break;
         case "date":
         case "datetime":
            valeur = getDate();
            break;
         case "texte":
            valeur = getTexte();
            break;
         case "thesaurus":
         case "thesaurusM":
            valeur = getItem().getLabel(); //TODO Pourquoi on retourne un string et non l'Item ? le string est retourné avec formateAnnotationValeur()
            break;
         case "fichier":
            valeur = getFichier().getNom(); // TODO Pourquoi on retourne un string et non le fichier ? le string est retourné avec formateAnnotationValeur().
            break;
         case "duree":
            valeur = Long.parseLong(getAlphanum());
            break;
         case "calcule":
            getValeur(this.getChampAnnotation().getChampCalcule().getDataType());
            break;
         default:
            break;
      }
      return valeur;
   }

   /**
    * Assigne la valeur selon le type d'objet passé en paramètre et selon le datatype si renseigné
    * @param valeur valeur
    */
   @Transient
   public void setValeur(final Object valeur){
      if(valeur instanceof String){
         if(null != this.getChampAnnotation() && null != this.getChampAnnotation().getDataType()){
            if("texte".equals(this.getChampAnnotation().getDataType().getType())){
               this.setTexte(valeur.toString());
            }
         }else{
            this.setAlphanum(valeur.toString());
         }
      }else if(valeur instanceof Number){
         this.setAlphanum(valeur.toString());
      }else if(valeur instanceof Date){
         final Calendar cal = Calendar.getInstance();
         cal.setTime((Date) valeur);
         this.setDate(cal);
      }else if(valeur instanceof Calendar){
         this.setDate((Calendar) valeur);
      }else if(valeur instanceof Boolean){
         this.setBool((Boolean) valeur);
      }else if(valeur instanceof Fichier){
         this.setFichier((Fichier) valeur);
      }else if(valeur instanceof Item){
         this.setItem((Item) valeur);
      }
   }

   @Transient
   @Override
   public Fichier getFile(){
      return getFichier();
   }

   @Transient
   @Override
   public void setFile(final Fichier f){
      setFichier(f);
   }
}
