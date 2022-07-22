/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.ThesaurusValue;

@JsonPropertyOrder({"champEntiteId", "thesaurusId", "thesaurusValue", "position"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThesaurusValueDTO implements Serializable
{

   private static final long serialVersionUID = 1L;

   private Integer champEntiteId;

   private Integer thesaurusId;

   private String thesaurusValue;

   private Integer position;

   @JsonProperty
   public Integer getChampEntiteId(){
      return champEntiteId;
   }

   public void setChampEntiteId(final Integer _i){
      this.champEntiteId = _i;
   }

   //	@JsonProperty
   //	public Integer getTemplateThesaurusId() {
   //		return templateThesaurusId;
   //	}
   //
   //	public void setTemplateThesaurusId(Integer _t) {
   //		this.templateThesaurusId = _t;
   //	}

   @JsonProperty
   public Integer getThesaurusId(){
      return thesaurusId;
   }

   public void setThesaurusId(final Integer _t){
      this.thesaurusId = _t;
   }

   @JsonProperty
   public String getThesaurusValue(){
      return thesaurusValue;
   }

   public void setThesaurusValue(final String _v){
      this.thesaurusValue = _v;
   }

   @JsonProperty
   public Integer getPosition(){
      return position;
   }

   public void setPosition(final Integer _p){
      this.position = _p;
   }

   @JsonIgnore
   public ThesaurusValue toThesaurusValue(){
      return new ThesaurusValue(champEntiteId, thesaurusId, thesaurusValue, position);
   }

}
