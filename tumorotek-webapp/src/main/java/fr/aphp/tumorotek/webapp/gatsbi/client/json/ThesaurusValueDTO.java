/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.ThesaurusValue;


@JsonPropertyOrder({
	"champId",
	"templateThesaurusId",
	"thesaurusId",
	"thesaurusValue",
	"position"
})
public class ThesaurusValueDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer champId;
	private Integer templateThesaurusId;
	private Integer thesaurusId;
	private String thesaurusValue;
	private Integer position;
	
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
	
	public void setTemplateThesaurusId(Integer _t) {
		this.templateThesaurusId = _t;
	}
	
	@JsonProperty
	public Integer getThesaurusId() {
		return thesaurusId;
	}

	public void setThesaurusId(Integer _t) {
		this.thesaurusId = _t;
	}
	
	@JsonProperty
	public String getThesaurusValue() {
		return thesaurusValue;
	}
	
	public void setThesaurusValue(String _v) {
		this.thesaurusValue = _v;
	}
	
	@JsonProperty
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer _p) {
		this.position = _p;
	}
	
	@JsonIgnore
	public ThesaurusValue toThesaurusValue() {
		return new ThesaurusValue(champId, templateThesaurusId, templateThesaurusId, thesaurusValue, position);
	}
	
	
	
	
	
}