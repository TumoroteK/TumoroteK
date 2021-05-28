/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsby.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
	"contexteId",
	"contexteLibelle",
	"type",
	"archive",
	"siteInter",
	"parametrages",
	"rChampEntites"
})
public class Contexte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer contexteId;
	private String contexteLibelle;
	private ContexteType type;
	private Boolean archive;
	private Boolean siteInter = true;
	private List<Parametrage> parametrages = new ArrayList<Parametrage>();
	private List<ChampEntite> champEntites = new ArrayList<ChampEntite>();
	
	@JsonProperty
	public Integer getContexteId() {
		return contexteId;
	}

	public void setContexteId(Integer contexteId) {
		this.contexteId = contexteId;
	}

	@JsonProperty
	public String getContexteLibelle() {
		return contexteLibelle;
	}

	public void setContexteLibelle(String _l) {
		this.contexteLibelle = _l;
	}

	@JsonProperty
	public ContexteType getType() {
		return type;
	}

	public void setType(ContexteType _t) {
		this.type = _t;
	}

	@JsonProperty
	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean _a) {
		this.archive = _a;
	}
	
	@JsonProperty
	public Boolean getSiteInter() {
		return siteInter;
	}

	public void setSiteInter(Boolean _s) {
		this.siteInter = _s;
	}

	@JsonProperty
	public List<Parametrage> getParametrages() {
		return parametrages;
	}

	public void setParametrages(List<Parametrage> parametrages) {
		this.parametrages = parametrages;
	}

	@JsonProperty("rChampEntites")
	public List<ChampEntite> getChampEntites() {
		return champEntites;
	}

	public void setChampEntites(List<ChampEntite> champEntites) {
		this.champEntites = champEntites;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Contexte contexte = (Contexte) obj;

        return Objects.equals(contexteLibelle, contexte.getContexteLibelle())
        	&& Objects.equals(type, contexte.getType());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((contexteLibelle == null) ? 0 : contexteLibelle.hashCode());
    	result = prime * result + ((type == null) ? 0 : type.hashCode());
    	return result;
	}
	
	@JsonIgnore
	public List<Integer> getHiddenChamps() {
		return champEntites.stream()
				.filter(c -> !c.getVisible())
				.map(ChampEntite::getChampId)
			.collect(Collectors.toList());
	}
	
}