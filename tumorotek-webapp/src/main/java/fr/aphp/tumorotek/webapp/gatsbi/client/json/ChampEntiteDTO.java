/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
 *
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les
 * conditions de la licence CeCILL telle que diffusée par le CEA, le
 * CNRS et l'INRIA sur le site "http://www.cecill.info".
 * En contrepartie de l'accessibilité au code source et des droits de
 * copie, de modification et de redistribution accordés par cette
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée.
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur
 * l'auteur du programme, le titulaire des droits patrimoniaux et les
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les
 * risques associés au chargement,  à l'utilisation,  à la modification
 * et/ou au  développement et à la reproduction du logiciel par
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut
 * le rendre complexe à manipuler et qui le réserve donc à des
 * développeurs et des professionnels  avertis possédant  des
 * connaissances  informatiques approfondies.  Les utilisateurs sont
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser
 * et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.webapp.gatsbi.client.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import fr.aphp.tumorotek.model.contexte.gatsbi.ChampEntite;

@JsonPropertyOrder({
	"champId",
	"champOrdre",
	"contexteChampEntiteId",
	"dateFormat",
	"isChampReferToThesaurus",
	"obligatoire",
	"visible", 
	"inTableau",
	"ordreTableau",
	"rThesauruses"
})
public class ChampEntiteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer champId;
	private Integer champOrdre;
	private Integer contexteChampEntiteId;
	private String dateFormat;
	private String isChampReferToThesaurus;
	private Boolean obligatoire = false;
	private Boolean visible = true;
	private Boolean inTableau = false;
	private Integer ordreTableau;
	private List<ThesaurusValueDTO> thesaurusValueDTOs = new ArrayList<ThesaurusValueDTO>();
	
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
	
	@JsonProperty
	public Boolean getInTableau() {
		return inTableau;
	}

	public void setInTableau(Boolean _t) {
		this.inTableau = _t;
	}

	@JsonProperty
	public Integer getOrdreTableau() {
		return ordreTableau;
	}

	public void setOrdreTableau(Integer _o) {
		this.ordreTableau = _o;
	}

	@JsonProperty("rThesauruses")
	public List<ThesaurusValueDTO> getThesaurusValueDTOs() {
		return thesaurusValueDTOs;
	}
	
	public void setThesaurusValueDTOs(List<ThesaurusValueDTO> _v) {
		this.thesaurusValueDTOs = _v;
	}
	
	public ChampEntite toChampEntite() {
		return new ChampEntite(champId, champOrdre, contexteChampEntiteId, 
			dateFormat, isChampReferToThesaurus, obligatoire, visible, 
			inTableau, ordreTableau,
			thesaurusValueDTOs
				.stream().map(v -> v.toThesaurusValue())
				.collect(Collectors.toList())
			);
	}
}