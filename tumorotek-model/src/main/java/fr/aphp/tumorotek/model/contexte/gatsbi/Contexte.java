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
package fr.aphp.tumorotek.model.contexte.gatsbi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Contexte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer contexteId;
	private String contexteLibelle;
	private ContexteType contexteType;
	private Boolean archive;
	private Boolean siteInter = true;
	protected List<Parametrage> parametrages = new ArrayList<Parametrage>();
	protected List<ChampEntite> champEntites = new ArrayList<ChampEntite>();
	
	public Contexte() {
	}
	
	public Contexte(Integer contexteId, String contexteLibelle, ContexteType contexteType, Boolean archive,
			Boolean siteInter, List<Parametrage> parametrages, List<ChampEntite> champEntites) {
		super();
		this.contexteId = contexteId;
		this.contexteLibelle = contexteLibelle;
		this.contexteType = contexteType;
		this.archive = archive;
		this.siteInter = siteInter;
		this.parametrages = parametrages;
		this.champEntites = champEntites;
	}

	public Integer getContexteId() {
		return contexteId;
	}

	public void setContexteId(Integer contexteId) {
		this.contexteId = contexteId;
	}

	public String getContexteLibelle() {
		return contexteLibelle;
	}

	public void setContexteLibelle(String _l) {
		this.contexteLibelle = _l;
	}

	public ContexteType getContexteType() {
		return contexteType;
	}

	public void setContexteType(ContexteType _t) {
		this.contexteType = _t;
	}

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean _a) {
		this.archive = _a;
	}
	
	public Boolean getSiteInter() {
		return siteInter;
	}

	public void setSiteInter(Boolean _s) {
		this.siteInter = _s;
	}

	public List<Parametrage> getParametrages() {
		return parametrages;
	}

	public void setParametrages(List<Parametrage> parametrages) {
		this.parametrages = parametrages;
	}

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
       	&& Objects.equals(contexteType, contexte.getContexteType());
	}
	
	@Override
   public int hashCode() {
       final int prime = 31;
       int result = 1;
   	result = prime * result + ((contexteLibelle == null) ? 0 : contexteLibelle.hashCode());
   	result = prime * result + ((contexteType == null) ? 0 : contexteType.hashCode());
   	return result;
	}
	
	public List<Integer> getHiddenChampEntiteIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (ChampEntite c : champEntites) {
			if (!c.getVisible()) {
				ids.add(c.getChampId());
			}
		}
		return ids;
	}
	
	public List<Integer> getRequiredChampEntiteIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (ChampEntite c : champEntites) {
			if (c.getVisible() && c.getObligatoire()) {
				ids.add(c.getChampId());
			}
		}
		return ids;
	}
	
	/**
	 * Collecte et renvoie les champs de contexte considérés comme un thésaurus dont les 
	 * valeurs sont à filtrer. Contient les champs:
	 *  - les thesaurus (IsChampReferToThesaurus = true)
	 *  - les champs présentant des valeurs de thésaurus (ex: quantite, thésaurus des unités à filtrer)
	 * @return liste champs entite ids
	 */
	public List<Integer> getThesaurusChampEntiteIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (ChampEntite c : champEntites) {
			if (c.getVisible() && (c.getIsChampReferToThesaurus() != null 
					|| !c.getThesaurusValues().isEmpty())) {  
				ids.add(c.getChampId());
			}
		}
		return ids;
	}
	
	public List<ThesaurusValue> getThesaurusValuesForChampEntiteId(Integer id) {
		
		List<ThesaurusValue> tValues = new ArrayList<ThesaurusValue>();
		for (ChampEntite c : champEntites) {
			if (c.getChampId().equals(id)) {
				tValues.addAll(c.getThesaurusValues());
			}
		}
		return tValues;
	}
	
	public boolean isChampIdRequired(Integer id) {
		for (ChampEntite c : champEntites) {
			if (c.getChampId().equals(id)) {
				return c.getVisible() && c.getObligatoire();
			}
		}
		return false;
	}
	
	public boolean isChampIdVisible(Integer id) {
		for (ChampEntite c : champEntites) {
			if (c.getChampId().equals(id)) {
				return c.getVisible();
			}
		}
		return true;
	}	
	
	public List<Integer> getChampEntiteInTableauOrdered() {	
		List<Integer> ids = new ArrayList<Integer>();
		
		Collections.sort(champEntites);
		
		for (ChampEntite c : champEntites) {
			if (c.getVisible() && c.getInTableau()) {
				ids.add(c.getChampId());
			}
		}		
		return ids;
	}
	
}
