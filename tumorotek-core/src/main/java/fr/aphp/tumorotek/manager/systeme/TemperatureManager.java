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
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.systeme.TemperatureDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.systeme.TemperatureManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.systeme.TemperatureValidator;
import fr.aphp.tumorotek.model.systeme.Temperature;

/**
 * 
 * @version 2.3
 * @author Mathieu BARTHELEMY
 *
 */
@Service
public class TemperatureManager {

	private final Log log = LogFactory.getLog(TemperatureManager.class);

	@Autowired
	private TemperatureDao temperatureDao;

	@Autowired
	private TemperatureValidator temperatureValidator;

	@Transactional(readOnly = true)
	public Temperature findByIdManager(final Integer temperatureId) {
		return temperatureDao.findById(temperatureId).orElse(null);
	}

	@Transactional(readOnly = true)
	public List<Temperature> findAllObjectsManager() {
		log.debug("Recherche de toutes les Températures");
		return IterableUtils.toList(temperatureDao.findAll());
	}

	@Transactional(readOnly = true)
	public Boolean findDoublonManager(final Temperature temperature) {
		if (temperature != null) {
			if (temperature.getTemperatureId() == null) {
				return IterableUtils.toList(temperatureDao.findAll()).contains(temperature);
			} else {
				return temperatureDao.findByExcludedId(temperature.getTemperatureId()).contains(temperature);
			}
		} else {
			return false;
		}
	}

	@Transactional
	public void createObjectManager(final Temperature temperature) {
		// Test s'il y a des doublons
		if (findDoublonManager(temperature)) {
			log.warn("Doublon lors de la creation de l'objet Temperature : " + temperature.toString());
			throw new DoublonFoundException("Temperature", "creation");
		} else {

			// validation du Contrat
			BeanValidator.validateObject(temperature, new Validator[] { temperatureValidator });

			temperatureDao.save(temperature);

			log.info("Enregistrement de l'objet Temperature : " + temperature.toString());
		}
	}

	@Transactional
	public void updateObjectManager(final Temperature temperature) {
		// Test s'il y a des doublons
		if (findDoublonManager(temperature)) {
			log.warn("Doublon lors de la modification de " + "l'objet Temperature : " + temperature.toString());
			throw new DoublonFoundException("Temperature", "modification");
		} else {

			// validation du Contrat
			BeanValidator.validateObject(temperature, new Validator[] { temperatureValidator });

			temperatureDao.save(temperature);

			log.info("Enregistrement de l'objet Temperature : " + temperature.toString());
		}
	}

	@Transactional
	public void removeObjectManager(final Temperature temperature) {
		if (temperature != null) {
			temperatureDao.deleteById(temperature.getTemperatureId());
			log.info("Suppression de l'objet Temperature : " + temperature.toString());
		} else {
			log.warn("Suppression d'une Temperature null");
		}
	}
}
