/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;

@JsonPropertyOrder({"contexteId", "nom", "type", "archive", "siteIntermediaire", "listContexteParametrageNom",
   "listContexteChampEntite"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContexteDTO implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer contexteId;

   private String nom;

   private String type;

   private Boolean archive;

   private Boolean siteIntermediaire = true;

   private List<ParametrageDTO> parametrageDTOs = new ArrayList<>();

   private List<ChampEntiteDTO> champEntiteDTOs = new ArrayList<>();

   @JsonProperty
   public Integer getContexteId(){
      return contexteId;
   }

   public void setContexteId(final Integer contexteId){
      this.contexteId = contexteId;
   }

   @JsonProperty
   public String getNom(){
      return nom;
   }

   public void setNom(final String _n){
      this.nom = _n;
   }

   @JsonProperty
   public String getType(){
      return this.type;
   }

   public void setType(final String t){
      this.type = t;
   }

   @JsonProperty
   public Boolean getArchive(){
      return this.archive;
   }

   public void setArchive(final Boolean _a){
      this.archive = _a;
   }

   @JsonProperty
   public Boolean getSiteIntermediaire(){
      return siteIntermediaire;
   }

   public void setSiteIntermediaire(final Boolean _s){
      this.siteIntermediaire = _s;
   }

   @JsonProperty("listContexteParametrageNom")
   public List<ParametrageDTO> getParametrageDTOs(){
      return parametrageDTOs;
   }

   public void setParametrageDTOs(final List<ParametrageDTO> _p){
      this.parametrageDTOs = _p;

   }

   @JsonProperty("listContexteChampEntite")
   public List<ChampEntiteDTO> getChampEntiteDTOs(){
      return champEntiteDTOs;
   }

   public void setChampEntiteDTOs(final List<ChampEntiteDTO> _d){
      this.champEntiteDTOs = _d;
   }

   @JsonIgnore
   public Contexte toContexte(){
      return new Contexte(contexteId, nom, ContexteType.getByType(type), archive, siteIntermediaire,
         parametrageDTOs.stream().map(p -> p.toParametrage()).collect(Collectors.toList()),
         champEntiteDTOs.stream().map(c -> c.toChampEntite()).collect(Collectors.toList()));
   }
}
