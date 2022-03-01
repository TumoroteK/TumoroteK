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
package fr.aphp.tumorotek.manager.systeme;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.systeme.VersionDao;
import fr.aphp.tumorotek.manager.systeme.VersionManager;
import fr.aphp.tumorotek.model.systeme.Version;

/**
 *
 * Implémentation du manager du bean de domaine Version. Interface créée le
 * 26/05/2011.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Service
public class VersionManager {

	private final Log log = LogFactory.getLog(VersionManager.class);

	@Autowired
	private VersionDao versionDao;

	@Transactional(readOnly = true)
	public Version findByIdManager(final Integer versionId) {
		return versionDao.findById(versionId).orElse(null);
	}

	@Transactional(readOnly = true)
	public List<Version> findAllObjectsManager() {
		log.debug("Recherche de toutes les Versions.");
		return IterableUtils.toList(versionDao.findAll());
	}

	@Transactional(readOnly = true)
	public List<Version> findByDateChronologiqueManager() {
		log.debug("Recherche de toutes les Versions par " + "ordre chronologique.");
		return versionDao.findByDateChronologique();
	}

	@Transactional(readOnly = true)
	public Version findByCurrentVersionManager() {
		log.debug("Recherche de la version courante");
		final List<Version> versions = versionDao.findByDateAntiChronologique();

		if (versions.size() > 0) {
			return versions.get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public void createObjectManager(final Version version) {
		versionDao.save(version);
	}
}
