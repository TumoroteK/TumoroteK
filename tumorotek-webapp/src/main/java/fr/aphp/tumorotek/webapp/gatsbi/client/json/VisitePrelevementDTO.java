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
   "contexteParametrageNom"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitePrelevementDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer nbPrelevementMin;
	private Integer nbPrelevementMax;
	private ParametrageDTO contexteParametrageNom;
		
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
   public ParametrageDTO getContexteParametrageNom(){
      return contexteParametrageNom;
   }

   public void setContexteParametrageNom(ParametrageDTO contexteParametrageNom){
      this.contexteParametrageNom = contexteParametrageNom;
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

        return Objects.equals(contexteParametrageNom, visPrel.getContexteParametrageNom());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((contexteParametrageNom == null) ? 0 : contexteParametrageNom.hashCode());
    	return result;
	}
	
	@JsonIgnore
	public VisitePrelevement toVisitePrelevement() {
	   return new VisitePrelevement(nbPrelevementMin, nbPrelevementMax, 
	      contexteParametrageNom != null ? contexteParametrageNom.toParametrage() : null);
	   
	}
}
