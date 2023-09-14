/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.IntervalleType;


@JsonPropertyOrder({
   "code"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntervalleUniteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IntervalleType code;
	
	@JsonProperty
	public IntervalleType getCode(){
      return code;
   }

   public void setCode(IntervalleType code){
      this.code = code;
   }
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        IntervalleUniteDTO intv = (IntervalleUniteDTO) obj;

        return Objects.equals(code, intv.getCode());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((code == null) ? 0 : code.hashCode());
    	return result;
	}
}
