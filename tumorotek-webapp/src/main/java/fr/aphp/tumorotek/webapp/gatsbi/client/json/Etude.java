/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
	"etudeId",
	"titre",
	"acronyme",
	"archive",
	// "dateDebut", 
	// "dateFin",
	// "plateformeId",
	"rContextes"
})
public class Etude implements Serializable {

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

	@JsonProperty("rContextes")
	public List<ContexteDTO> getrContextes() {
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

        Etude etude = (Etude) obj;

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
