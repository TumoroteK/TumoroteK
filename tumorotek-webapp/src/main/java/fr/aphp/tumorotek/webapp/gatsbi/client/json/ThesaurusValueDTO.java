/**
  * projet-tk@sesan.fr
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.ThesaurusValue;


@JsonPropertyOrder({
	"champId",
	"templateThesaurusId",
	"thesaurusId",
	"thesaurusValue",
	"position"
})
public class ThesaurusValueDTO extends ThesaurusValue implements Serializable {

	private static final long serialVersionUID = 1L;
	
}