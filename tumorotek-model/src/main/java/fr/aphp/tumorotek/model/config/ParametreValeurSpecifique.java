package fr.aphp.tumorotek.model.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="PARAMETRE_VALEUR_SPECIFIQUE", uniqueConstraints = {
      @UniqueConstraint(columnNames = {"plateformeId", "code"})
})
@NamedQueries({
   @NamedQuery(name = "ParametreValeurSpecifique.findAllByPlateformeId",
               query = "SELECT p FROM ParametreValeurSpecifique p WHERE p.plateformeId = ?1"),
   @NamedQuery(name = "ParametreValeurSpecifique.findByPlateformeIdAndCode",
      query = "SELECT p FROM ParametreValeurSpecifique p WHERE p.plateformeId = ?1 AND p.code = ?2"),
})

public class ParametreValeurSpecifique
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer parameterId;

   private Integer plateformeId;
   private String code;
   private String valeur;

   private String type;

   private String groupe;


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

   public Integer getParameterId(){
      return parameterId;
   }

   public void setParameterId(Integer parameterId){
      this.parameterId = parameterId;
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
}
