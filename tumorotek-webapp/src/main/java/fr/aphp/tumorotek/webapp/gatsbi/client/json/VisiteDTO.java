/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.Visite;


@JsonPropertyOrder({
   "nom",   
   "ordre",
   "intervalleDepuisInitiale",
   "intervalleUnite",
   "listVisitePrelevement"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisiteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nom;
	private Integer ordre;
	private Integer intervalleDepuisInclusion;
	private IntervalleUniteDTO intervalleUnite;
	
	private List<VisitePrelevementDTO> visitePrelevements = new ArrayList<VisitePrelevementDTO>();
	
	@JsonProperty
	public String getNom(){
      return nom;
   }

   public void setNom(String nom){
      this.nom = nom;
   }

   @JsonProperty
   public Integer getOrdre(){
      return ordre;
   }

   public void setOrdre(Integer ordre){
      this.ordre = ordre;
   }

   @JsonProperty
   public Integer getIntervalleDepuisInclusion(){
      return intervalleDepuisInclusion;
   }

   public void setIntervalleDepuisInclusion(Integer intervalleDepuisInclusion){
      this.intervalleDepuisInclusion = intervalleDepuisInclusion;
   }

   @JsonProperty
   public IntervalleUniteDTO getIntervalleUnite(){
      return intervalleUnite;
   }

   public void setIntervalleUnite(IntervalleUniteDTO intervalleUnite){
      this.intervalleUnite = intervalleUnite;
   }

   @JsonProperty("listVisitePrelevement")
	public List<VisitePrelevementDTO> getVisitePrelevements() {
		return visitePrelevements;
	}

	public void setVisitePrelevements(List<VisitePrelevementDTO> _r) {
		this.visitePrelevements = _r;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        VisiteDTO vis = (VisiteDTO) obj;

        return Objects.equals(nom, vis.getNom());
	}
	
	@Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
    	result = prime * result + ((nom == null) ? 0 : nom.hashCode());
    	return result;
	}
	
	@JsonIgnore
	public Visite toVisite() {
	   return new Visite(nom, ordre, intervalleDepuisInclusion, getIntervalleUnite().getCode(), 
	      visitePrelevements.stream().map(vp -> vp.toVisitePrelevement()).collect(Collectors.toList()));
	}
}
