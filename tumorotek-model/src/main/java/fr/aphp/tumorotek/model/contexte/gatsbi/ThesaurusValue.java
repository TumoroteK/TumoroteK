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
import java.util.Objects;

public class ThesaurusValue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer champId;
	private Integer templateThesaurusId;
	private Integer thesaurusId;
	private String thesaurusValue;
	private Integer position;
	
	public ThesaurusValue() {
	}
	
	public ThesaurusValue(Integer champId, Integer templateThesaurusId, Integer thesaurusId, String thesaurusValue,
			Integer position) {
		super();
		this.champId = champId;
		this.templateThesaurusId = templateThesaurusId;
		this.thesaurusId = thesaurusId;
		this.thesaurusValue = thesaurusValue;
		this.position = position;
	}

	public Integer getChampId() {
		return champId;
	}
	
	public void setChampId(Integer _i) {
		this.champId = _i;
	}
	
	public Integer getTemplateThesaurusId() {
		return templateThesaurusId;
	}
	
	public void setTemplateThesaurusId(Integer _i) {
		this.templateThesaurusId = _i;
	}
	
	public Integer getThesaurusId() {
		return thesaurusId;
	}
	
	public void setThesaurusId(Integer _i) {
		this.thesaurusId = _i;
	}
	
	public String getThesaurusValue() {
		return thesaurusValue;
	}
	
	public void setThesaurusValue(String _v) {
		this.thesaurusValue = _v;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer _p) {
		this.position = _p;
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