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
	"champId",
	"champOrdre",
	"contexteChampEntiteId",
	"dateFormat",
	"isChampReferToThesaurus",
	"obligatoire",
	"visible", 
	"rThesauruses"
})
public class ChampEntite implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer champId;
	private Integer champOrdre;
	private Integer contexteChampEntiteId;
	private String dateFormat;
	private String isChampReferToThesaurus;
	private Boolean obligatoire = false;
	private Boolean visible = true;
	private List<ThesaurusValue> thesaurusValues = new ArrayList<ThesaurusValue>();
	
	@JsonProperty
	public Integer getChampId() {
		return champId;
	}
	
	public void setChampId(Integer _i) {
		this.champId = _i;
	}
	
	@JsonProperty
	public Integer getChampOrdre() {
		return champOrdre;
	}
	public void setChampOrdre(Integer _o) {
		this.champOrdre = _o;
	}
	
	@JsonProperty
	public Integer getContexteChampEntiteId() {
		return contexteChampEntiteId;
	}
	
	public void setContexteChampEntiteId(Integer _c) {
		this.contexteChampEntiteId = _c;
	}
	
	@JsonProperty
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String _d) {
		this.dateFormat = _d;
	}

	@JsonProperty
	public String getIsChampReferToThesaurus() {
		return isChampReferToThesaurus;
	}
	
	public void setIsChampReferToThesaurus(String _f) {
		this.isChampReferToThesaurus = _f;
	}
	
	@JsonProperty
	public Boolean getObligatoire() {
		return obligatoire;
	}
	
	public void setObligatoire(Boolean _o) {
		this.obligatoire = _o;
	}
	
	@JsonProperty
	public Boolean getVisible() {
		return visible;
	}
	
	public void setVisible(Boolean _v) {
		this.visible = _v;
	}
	
	@JsonProperty("rThesauruses")
	public List<ThesaurusValue> getThesaurusValues() {
		return thesaurusValues;
	}
	
	public void setThesaurusValues(List<ThesaurusValue> _v) {
		this.thesaurusValues = _v;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ChampEntite chp = (ChampEntite) obj;

        return Objects.equals(champId, chp.getChampId());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((champId == null) ? 0 : champId.hashCode());
    	return result;
	}
}