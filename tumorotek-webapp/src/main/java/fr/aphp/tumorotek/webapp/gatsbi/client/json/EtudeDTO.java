/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({
	"etudeId",
	"titre",
	"acronyme",
	"archive",
	"listContexte"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtudeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer etudeId;
	private String titre;
	private String acronyme;
	private Boolean archive;
	private List<ContexteDTO> contextes = new ArrayList<ContexteDTO>();
	
	@JsonProperty
	public Integer getEtudeId() {
		return etudeId;
	}

	public void setEtudeId(Integer _i) {
		this.etudeId = _i;
	}

	@JsonProperty
	public String getTitre() {
		return titre;
	}

	public void setTitre(String _t) {
		this.titre = _t;
	}

	@JsonProperty
	public String getAcronyme() {
		return acronyme;
	}

	public void setAcronyme(String _a) {
		this.acronyme = _a;
	}

	@JsonProperty
	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean _a) {
		this.archive = _a;
	}

	@JsonProperty("listContexte")
	public List<ContexteDTO> getContextes() {
		return contextes;
	}

	public void setContextes(List<ContexteDTO> _r) {
		this.contextes = _r;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        EtudeDTO etude = (EtudeDTO) obj;

        return Objects.equals(titre, etude.getTitre());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((titre == null) ? 0 : titre.hashCode());
    	return result;
	}
}
