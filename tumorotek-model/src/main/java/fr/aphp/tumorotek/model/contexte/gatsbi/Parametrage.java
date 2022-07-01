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
import java.util.List;
import java.util.Objects;

public class Parametrage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer contexteParametrageId;
	private String nom;
	private List<ParametrageValue> parametrageValues = new ArrayList<ParametrageValue>();
	
	public Parametrage(Integer parametrageId, String nom, List<ParametrageValue> _v) {
		super();
		this.contexteParametrageId = parametrageId;
		this.nom = nom;
		if (_v != null) {
			this.parametrageValues.addAll(_v);
		}
	}

	public Integer getContexteParametrageId() {
		return contexteParametrageId;
	}
	
	public void setContexteParametrageId(Integer _i) {
		this.contexteParametrageId = _i;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String _l) {
		this.nom = _l;
	}

	public List<ParametrageValue> getParametrageValues() {
		return parametrageValues;
	}

	public void setParametrageValues(List<ParametrageValue> _v) {
		this.parametrageValues = _v;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Parametrage param = (Parametrage) obj;

        return Objects.equals(contexteParametrageId, param.getContexteParametrageId());
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
    	result = prime * result + ((contexteParametrageId == null) ? 0 : contexteParametrageId.hashCode());
    	return result;
	}
	
	public String getDefaultValuesForChampEntiteId(Integer id) {
		
		for (ParametrageValue v : parametrageValues) {
			if (v.getChampEntiteId().equals(id)) {
				return v.getDefaultValue();
			}
		}
		return null;
	}
}