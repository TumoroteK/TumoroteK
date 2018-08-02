package fr.aphp.tumorotek.model.validation;

import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Calendar;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Entity implementation class for Entity: DetailValidation
 *
 */
@Entity
@Table(name = "DETAIL_VALIDATION")
public class DetailValidation implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer id;
   private Calendar dateValidation;
   private NiveauValidation niveauValidation;
   private Utilisateur valideur;
   private String commentaireValidation;

   public DetailValidation(){
      super();
   }

   @Id
   @Column(name = "DETAIL_VALIDATION_ID", unique = true, nullable = false)
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   public Integer getId(){
      return this.id;
   }

   public void setId(Integer id){
      this.id = id;
   }

   @Column(name = "DATE_VALIDATION", nullable=false)
   public Calendar getDateValidation(){
      return this.dateValidation;
   }

   public void setDateValidation(Calendar dateValidation){
      this.dateValidation = dateValidation;
   }

   @ManyToOne
   @JoinColumn(name="NIVEAU_VALIDATION_ID", nullable=false)
   public NiveauValidation getNiveauValidation(){
      return niveauValidation;
   }

   public void setNiveauValidation(NiveauValidation niveauValidation){
      this.niveauValidation = niveauValidation;
   }

   @ManyToOne
   @JoinColumn(name = "UTILISATEUR_ID", nullable=false)
   public Utilisateur getValideur(){
      return this.valideur;
   }

   public void setValideur(Utilisateur valideur){
      this.valideur = valideur;
   }

   @Column(name = "COMMENTAIRE_VALIDATION")
   public String getCommentaireValidation(){
      return this.commentaireValidation;
   }

   public void setCommentaireValidation(String commentaireValidation){
      this.commentaireValidation = commentaireValidation;
   }

}
