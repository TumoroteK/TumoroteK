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

import fr.aphp.tumorotek.model.contexte.gatsbi.SchemaVisites;


@JsonPropertyOrder({
   "nom",
	"listVisite"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaVisitesDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<VisiteDTO> visites = new ArrayList<VisiteDTO>();
	
	@JsonProperty("listVisite")
	public List<VisiteDTO> getVisites() {
		return visites;
	}

	public void setVisites(List<VisiteDTO> _r) {
		this.visites = _r;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        SchemaVisitesDTO schema = (SchemaVisitesDTO) obj;

        return Objects.equals(visites, schema.getVisites());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((visites == null) ? 0 : visites.hashCode());
    	return result;
	}
	
	@JsonIgnore
	public SchemaVisites toSchemaVisites() {
	   return new SchemaVisites(visites.stream().map(v -> v.toVisite()).collect(Collectors.toList()));
	}
}
