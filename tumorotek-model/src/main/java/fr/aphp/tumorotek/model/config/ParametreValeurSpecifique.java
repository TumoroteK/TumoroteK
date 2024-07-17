package fr.aphp.tumorotek.model.config;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PARAMETRE_VALEUR_SPECIFIQUE")
@NamedQueries({
   @NamedQuery(name = "ParametreValeurSpecifique.findAllByPlateformeId",
               query = "SELECT p FROM ParametreValeurSpecifique p WHERE p.plateformeId = ?1"),
   @NamedQuery(name = "ParametreValeurSpecifique.findByPlateformeIdAndCode",
      query = "SELECT p FROM ParametreValeurSpecifique p WHERE p.plateformeId = ?1 AND p.code = ?2"),
})

public class ParametreValeurSpecifique
{
   @Id
   @GeneratedValue(generator = "autoincrement")
   @GenericGenerator(name = "autoincrement", strategy = "increment")
   @Column(name = "PARAMETRE_VALEUR_SPECIFIQUE_ID", nullable = false)
   private Integer parametreValeurSpecifiqueId;

   @Column(name = "PLATEFORME_ID", nullable = false)
   private Integer plateformeId;

   @Column(name = "CODE", nullable = false, length = 50)
   private String code;

   @Column(name = "VALEUR", nullable = false)
   private String valeur;

   @Column(name = "TYPE", length = 25)
   private String type;

   @Column(name = "GROUPE", length = 25)
   private String groupe;

   // Getters and Setters

   public Integer getParametreValeurSpecifiqueId(){
      return parametreValeurSpecifiqueId;
   }

   public void setParametreValeurSpecifiqueId(Integer parametreValeurSpecifiqueId){
      this.parametreValeurSpecifiqueId = parametreValeurSpecifiqueId;
   }

   public Integer getPlateformeId(){
      return plateformeId;
   }

   public void setPlateformeId(Integer plateformeId){
      this.plateformeId = plateformeId;
   }

   public String getCode(){
      return code;
   }

   public void setCode(String code){
      this.code = code;
   }

   public String getValeur(){
      return valeur;
   }

   public void setValeur(String valeur){
      this.valeur = valeur;
   }

   public String getType(){
      return type;
   }

   public void setType(String type){
      this.type = type;
   }

   public String getGroupe(){
      return groupe;
   }

   public void setGroupe(String groupe){
      this.groupe = groupe;
   }
}
