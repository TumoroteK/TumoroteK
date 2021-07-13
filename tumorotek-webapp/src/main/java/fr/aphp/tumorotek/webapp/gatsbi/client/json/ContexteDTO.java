/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;

@JsonPropertyOrder({
	"contexteId",
	"contexteLibelle",
	"type",
	"archive",
	"siteInter",
	"parametrages",
	"rChampEntites"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContexteDTO extends Contexte implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ParametrageDTO> parametrageDTOs = new ArrayList<ParametrageDTO>();
	private List<ChampEntiteDTO> champEntiteDTOs = new ArrayList<ChampEntiteDTO>();
	
	@JsonProperty
	public String getType() {
		return super.getContexteType().getType();
	}
	
	public void setType(String t) {
		super.setContexteType(ContexteType.getByType(t));
	}

	@JsonProperty("parametrages")
	public List<ParametrageDTO> getParametrageDTOs() {
		return parametrageDTOs;
	}

	public void setParametrageDTOs(List<ParametrageDTO> _p) {
		this.parametrageDTOs = _p;
		this.getParametrages().clear();
		this.getParametrages().addAll(_p);
	}

	@JsonProperty("rChampEntites")
	public List<ChampEntiteDTO> getChampEntiteDTOs() {
		return champEntiteDTOs;
	}

	public void setChampEntiteDTOs(List<ChampEntiteDTO> _c) {
		this.champEntiteDTOs = _c;
		this.getChampEntites().clear();
		this.getChampEntites().addAll(_c);
	}	
}