/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.VisitePrelevement;


@JsonPropertyOrder({
   "nbPrelevementMin", 
   "nbPrelevementMax",
   "contexteParametrage"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitePrelevementDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer nbPrelevementMin;
	private Integer nbPrelevementMax;
	private ParametrageDTO contexteParametrage;
		
	@JsonProperty
	public Integer getNbPrelevementMin(){
      return nbPrelevementMin;
   }

   public void setNbPrelevementMin(Integer nbPrelevementMin){
      this.nbPrelevementMin = nbPrelevementMin;
   }

   @JsonProperty
   public Integer getNbPrelevementMax(){
      return nbPrelevementMax;
   }

   public void setNbPrelevementMax(Integer nbPrelevementMax){
      this.nbPrelevementMax = nbPrelevementMax;
   }

   @JsonProperty
   public ParametrageDTO getContexteParametrage(){
      return contexteParametrage;
   }

   public void setContexteParametrageNom(ParametrageDTO contexteParametrage){
      this.contexteParametrage = contexteParametrage;
   }

   @Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        VisitePrelevementDTO visPrel = (VisitePrelevementDTO) obj;

        return Objects.equals(contexteParametrage, visPrel.getContexteParametrage());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((contexteParametrage == null) ? 0 : contexteParametrage.hashCode());
    	return result;
	}
	
	@JsonIgnore
	public VisitePrelevement toVisitePrelevement() {
	   return new VisitePrelevement(nbPrelevementMin, nbPrelevementMax, 
	      contexteParametrage != null ? contexteParametrage.toParametrage() : null);
	   
	}
}
