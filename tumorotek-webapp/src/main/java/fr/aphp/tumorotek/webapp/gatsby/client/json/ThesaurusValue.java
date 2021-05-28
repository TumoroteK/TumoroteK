/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsby.client.json;

import java.io.Serializable;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


@JsonPropertyOrder({
	"champId",
	"templateThesaurusId",
	"thesaurusId",
	"thesaurusValue"
})
public class ThesaurusValue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer champId;
	private Integer templateThesaurusId;
	private Integer thesaurusId;
	private String thesaurusValue;
	
	@JsonProperty
	public Integer getChampId() {
		return champId;
	}
	
	public void setChampId(Integer _i) {
		this.champId = _i;
	}
	
	@JsonProperty
	public Integer getTemplateThesaurusId() {
		return templateThesaurusId;
	}
	
	public void setTemplateThesaurusId(Integer _i) {
		this.templateThesaurusId = _i;
	}
	
	@JsonProperty
	public Integer getThesaurusId() {
		return thesaurusId;
	}
	
	public void setThesaurusId(Integer _i) {
		this.thesaurusId = _i;
	}
	
	@JsonProperty
	public String getThesaurusValue() {
		return thesaurusValue;
	}
	
	public void setThesaurusValue(String _v) {
		this.thesaurusValue = _v;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ThesaurusValue val = (ThesaurusValue) obj;

        return Objects.equals(thesaurusId, val.getThesaurusId());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((thesaurusId == null) ? 0 : thesaurusId.hashCode());
    	return result;
	}
}