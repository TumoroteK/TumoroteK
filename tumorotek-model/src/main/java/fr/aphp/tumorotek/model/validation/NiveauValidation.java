package fr.aphp.tumorotek.model.validation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the statut_validation database table.
 * 
 */
@Entity
@Table(name = "NIVEAU_VALIDATION")
@NamedQueries({@NamedQuery(name = "NiveauValidation.findMaxCriticiteNiveau",
   query = "FROM NiveauValidation n WHERE n.criticite = (SELECT MAX(criticite) FROM NiveauValidation)"),
   @NamedQuery(name = "NiveauValidation.findByCriticite",
      query = "FROM NiveauValidation n WHERE n.criticite = ?1")})
public class NiveauValidation implements Serializable
{

   private static final long serialVersionUID = 1L;
   private Integer id;
   private String cleMessage;
   private String couleur;
   private Integer criticite;
   private String libelle;

   public NiveauValidation(){}

   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "NIVEAU_VALIDATION_ID")
   public Integer getId(){
      return this.id;
   }

   public void setId(Integer id){
      this.id = id;
   }

   @Column(name = "CLE_MESSAGE")
   public String getCleMessage(){
      return this.cleMessage;
   }

   public void setCleMessage(String cleMessage){
      this.cleMessage = cleMessage;
   }

   @Column(name = "COULEUR")
   public String getCouleur(){
      return this.couleur;
   }

   public void setCouleur(String couleur){
      this.couleur = couleur;
   }

   @Column(name = "CRITICITE", nullable=false)
   public Integer getCriticite(){
      return this.criticite;
   }

   public void setCriticite(Integer criticite){
      this.criticite = criticite;
   }

   @Column(name = "LIBELLE")
   public String getLibelle(){
      return this.libelle;
   }

   public void setLibelle(String libelle){
      this.libelle = libelle;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj){
      if(this == obj){
         return true;
      }
      if(obj == null){
         return false;
      }
      if(!(obj instanceof NiveauValidation)){
         return false;
      }
      NiveauValidation other = (NiveauValidation) obj;
      if(id == null){
         if(other.id != null){
            return false;
         }
      }else if(!id.equals(other.id)){
         return false;
      }
      return true;
   }

}
