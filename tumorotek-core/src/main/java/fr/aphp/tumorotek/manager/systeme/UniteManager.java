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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.systeme.UniteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.systeme.UniteValidator;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Implémentation du manager du bean de domaine Unite. Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Service
public class UniteManager {

	private final Log log = LogFactory.getLog(UniteManager.class);

	@Autowired
	private UniteDao uniteDao;

	@Autowired
	private UniteValidator uniteValidator;

	/**
	 * Recherche une unité dont l'identifiant est passé en paramètre.
	 * 
	 * @param uniteId Identifiant de l'unité que l'on recherche.
	 * @return Une Unite.
	 */
	@Transactional(readOnly = true)
	public Unite findByIdManager(final Integer uniteId) {
		return uniteDao.findById(uniteId).orElse(null);
	}

	/**
	 * Recherche toutes les unités présentes dans la base.
	 * 
	 * @return Liste d'Unite.
	 */
	@Transactional(readOnly = true)
	public List<Unite> findAllObjectsManager() {
		log.debug("Recherche de toutes les Unites");
		return uniteDao.findByOrder();
	}

	/**
	 * Recherche toutes les unités dont l'unite commence comme celle passée en
	 * paramètre.
	 * 
	 * @param unite      Unite que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 *                   exactes.
	 * @return Liste d'Unite
	 */
	@Transactional(readOnly = true)
	public List<Unite> findByUniteLikeManager(String unite, final boolean exactMatch) {
		log.debug("Recherche Unite par unite " + unite + " exactMatch " + String.valueOf(exactMatch));
		if (unite != null) {
			if (!exactMatch) {
				unite = unite + "%";
			}
			return uniteDao.findByUnite(unite);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Recherche toutes les unités dont le type commence comme celui passé en
	 * paramètre.
	 * 
	 * @param type       Type que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 *                   exactes.
	 * @return Liste d'Unite
	 */
	@Transactional(readOnly = true)
	public List<Unite> findByTypeLikeManager(String type, final boolean exactMatch) {
		log.debug("Recherche Unite par type " + type + " exactMatch " + String.valueOf(exactMatch));
		if (type != null) {
			if (!exactMatch) {
				type = type + "%";
			}
			return uniteDao.findByTypeWithOrder(type);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Recherche les doublons de l'Unite passé en paramètre.
	 * 
	 * @param unite Unite pour laquelle on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	@Transactional(readOnly = true)
	public Boolean findDoublonManager(final Unite unite) {
		return IterableUtils.toList(uniteDao.findAll()).contains(unite);
	}

	/**
	 * Test si une unité est liée à des objets de la base.
	 * 
	 * @param unite Unite que l'on souhaite tester.
	 * @return Vrai si l'Unite est utilisée.
	 */
	@Transactional(readOnly = true)
	public Boolean isUsedObjectManager(Unite unite) {
		int nb = 0;
		if (unite != null) {
			unite = uniteDao.findById(unite.getId()).orElse(null);

			if (unite != null) {
				nb = nb + unite.getEchantillonQuantites().size();
				nb = nb + unite.getPrelevementQuantites().size();
				nb = nb + unite.getProdDeriveConcs().size();
				nb = nb + unite.getProdDeriveQuantites().size();
				nb = nb + unite.getProdDeriveVolumes().size();
				nb = nb + unite.getTransformationQuantites().size();
				nb = nb + unite.getCederObjetQuantites().size();
			}
		}

		return (nb > 0);
	}

	/**
	 * Persist une instance d'Unite dans la base de données.
	 * 
	 * @param unite Nouvelle instance de l'objet à créer.
	 * @throws DoublonFoundException Lance une exception si un doublon de l'objet à
	 *                               créer se trouve déjà dans la base.
	 */
	@Transactional
	public void createObjectManager(final Unite unite) {
		if (findDoublonManager(unite)) {
			log.warn("Doublon lors de la creation de l'objet Unite : " + unite.toString());
			throw new DoublonFoundException("Unite", "creation");
		} else {
			BeanValidator.validateObject(unite, new Validator[] { uniteValidator });
			uniteDao.save(unite);
			log.info("Enregistrement de l'objet Unite : " + unite.toString());
		}
	}

	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * 
	 * @param unite Objet à mettre à jour dans la base.
	 * @throws DoublonFoundException Lance une exception si un doublon de l'objet à
	 *                               créer se trouve déjà dans la base.
	 */
	@Transactional
	public void updateObjectManager(final Unite unite) {
		if (findDoublonManager(unite)) {
			log.warn("Doublon lors de la modif de l'objet Unite : " + unite.toString());
			throw new DoublonFoundException("Unite", "modification");
		} else {
			BeanValidator.validateObject(unite, new Validator[] { uniteValidator });
			uniteDao.save(unite);
			log.info("Modification de l'objet Unite : " + unite.toString());
		}
	}

	/**
	 * Supprime une Unite de la base de données.
	 * 
	 * @param unite Unite à supprimer de la base de données.
	 * @throws DoublonFoundException Lance une exception si l'objet est utilisé par
	 *                               des échantillons.
	 */
	@Transactional
	public void removeObjectManager(final Unite unite) {
		if (isUsedObjectManager(unite)) {
			log.warn("Objet utilisé lors de la suppression de l'objet Unite : " + unite.toString());
			throw new ObjectUsedException("Unite", "suppression");
		} else {
			uniteDao.deleteById(unite.getId());
			log.info("Suppression de l'objet Unite : " + unite.toString());
		}
	}

}
