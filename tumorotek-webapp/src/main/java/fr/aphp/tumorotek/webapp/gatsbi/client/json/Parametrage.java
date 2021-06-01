/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
	"parametrageId",
	"parametrageLibelle"
})
public class Parametrage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer parametrageId;
	private String parametrageLibelle;
	
	@JsonProperty
	public Integer getParametrageId() {
		return parametrageId;
	}
	
	public void setParametrageId(Integer _i) {
		this.parametrageId = _i;
	}
	
	@JsonProperty
	public String getParametrageLibelle() {
		return parametrageLibelle;
	}
	
	public void setParametrageLibelle(String _l) {
		this.parametrageLibelle = _l;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Parametrage param = (Parametrage) obj;

        return Objects.equals(parametrageId, param.getParametrageId());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((parametrageId == null) ? 0 : parametrageId.hashCode());
    	return result;
	}
}