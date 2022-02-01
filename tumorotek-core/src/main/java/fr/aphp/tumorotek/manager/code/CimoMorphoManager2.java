/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
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
package fr.aphp.tumorotek.manager.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import fr.aphp.tumorotek.dao.code.CimoMorphoDao;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimoMorpho;

/**
 *
 * Interface pour le Manager du bean de domaine CimoMorpho. Interface créée le
 * 21/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Service
public class CimoMorphoManager2 implements CodeCommonManager<CimoMorpho> {

	private final Log log = LogFactory.getLog(CimoMorphoManager2.class);

	private CimoMorphoDao cimoMorphoDao;

	public void setCimoMorphoDao(final CimoMorphoDao cDao) {
		this.cimoMorphoDao = cDao;
	}

	@Override
	public List<CimoMorpho> findAllObjectsManager() {
		return IterableUtils.toList(cimoMorphoDao.findAll());
	}

	/**
	 * Parcours egalement le champ CIM_REF qui represente l'equivalent en code
	 * CIM_MASTER.
	 */
	@Override
	public List<CimoMorpho> findByCodeLikeManager(String code, final boolean exactMatch) {
		if (!exactMatch) {
			code = "%" + code + "%";
		}
		log.debug("Recherche Cimo par code: " + code + " exactMatch " + String.valueOf(exactMatch));
		final Set<CimoMorpho> cimos = new HashSet<>();
		cimos.addAll(cimoMorphoDao.findByCodeLike(code));
		cimos.addAll(cimoMorphoDao.findByCimRefLike(code));
		return new ArrayList<>(cimos);
	}

	@Override
	public List<CimoMorpho> findByLibelleLikeManager(String libelle, final boolean exactMatch) {
		if (!exactMatch) {
			libelle = "%" + libelle + "%";
		}
		log.debug("Recherche Cimo par libelle: " + libelle + " exactMatch " + String.valueOf(exactMatch));
		return cimoMorphoDao.findByLibelleLike(libelle);
	}

	/**
	 * Recherche les codes ADICAP topo issus du transcodage du code Cimo passé en
	 * paramètre.
	 * 
	 * @param code cimo qui sera transcodé
	 * @return Liste de codes Adicap
	 */
	public Set<Adicap> getAdicapsManager(final CimoMorpho cimo) {
		Set<Adicap> adicaps = new HashSet<>();
		final CimoMorpho cimoM = cimoMorphoDao.save(cimo);
		adicaps = cimoM.getAdicaps();
		adicaps.size(); // operation empechant LazyInitialisationException
		return adicaps;
	}
}
