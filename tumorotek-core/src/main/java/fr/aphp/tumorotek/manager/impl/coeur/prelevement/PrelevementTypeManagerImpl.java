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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementTypeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement
												.PrelevementTypeValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Implémentation du manager du bean de domaine PrelevementType.
 * Classe créée le 05/10/09.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PrelevementTypeManagerImpl implements PrelevementTypeManager {
	
	private Log log = LogFactory.getLog(PrelevementTypeManager.class);
	
	/* Beans injectes par Spring*/
	private PrelevementTypeDao prelevementTypeDao;
	private PrelevementTypeValidator prelevementTypeValidator;
	private PlateformeDao plateformeDao;

	public PrelevementTypeManagerImpl() {
	}
	
	/* Properties setters */
	public void setPrelevementTypeDao(PrelevementTypeDao pDao) {
		this.prelevementTypeDao = pDao;
	}
	
	public void setPrelevementTypeValidator(
			PrelevementTypeValidator ptValidator) {
		this.prelevementTypeValidator = ptValidator;
	}
	
	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}
	
	@Override
	public void createObjectManager(Object obj) {
		
		// On vérifie que la pf n'est pas null. Si c'est le cas on envoie
		// une exception
		if (((TKThesaurusObject) obj).getPlateforme() == null) {
			throw new RequiredObjectIsNullException(
					"PrelevementType", "creation", "Plateforme");
		} else {
			((TKThesaurusObject) obj)
				.setPlateforme(plateformeDao
					.mergeObject(((TKThesaurusObject) obj).getPlateforme()));
		}
		
		BeanValidator.validateObject(obj, 
				new Validator[]{prelevementTypeValidator});
		if (!findDoublonManager(obj)) {
			prelevementTypeDao.createObject((PrelevementType) obj);
			log.info("Enregistrement objet PrelevementType " + obj.toString());
		} else {
			log.warn("Doublon lors creation objet PrelevementType "
					+ obj.toString());
			throw new DoublonFoundException("PrelevementType", "creation");
		}			
	}
	
	@Override
	public void updateObjectManager(Object obj) {
		BeanValidator.validateObject(obj, 
				new Validator[]{prelevementTypeValidator});
		if (!findDoublonManager(obj)) {
			prelevementTypeDao.updateObject((PrelevementType) obj);
			log.info("Modification objet PrelevementType " + obj.toString());
		} else {
			log.warn("Doublon lors modification objet PrelevementType "
					+ obj.toString());
			throw new DoublonFoundException("PrelevementType", "modification");
		}
	}

//	@Override
//	public List<PrelevementType> findAllObjectsManager() {
//		log.info("Recherche totalite des PrelevementType");
//		return prelevementTypeDao.findByOrder();
//	}

	@Override
	public List<PrelevementType> findByTypeLikeManager(String type, 
											boolean exactMatch) {
		if (!exactMatch) {
			type = type + "%";
		}
		log.debug("Recherche PrelevementType par type: " 
				+ type + " exactMatch " + String.valueOf(exactMatch));
		return prelevementTypeDao.findByType(type);
	}

	@Override
	public void removeObjectManager(Object obj) {
		if (obj != null) {
			//if (!isUsedObjectManager(obj)) {
			prelevementTypeDao
				.removeObject(((PrelevementType) obj)
									.getPrelevementTypeId());
			log.info("Suppression objet PrelevementType " + obj.toString());
//			} else {
//				log.warn("Suppression objet PrelevementType " + obj.toString()
//						+ " impossible car est reference (par Prelevement)");
//				throw new ObjectUsedException();
//			}
		} else {
			log.warn("Suppression d'un PrelevementType null");
		}
	}

	@Override
	public boolean findDoublonManager(Object o) {
		if (o != null) {
			PrelevementType type = (PrelevementType) o;
			if (type.getPrelevementTypeId() == null) {
				return prelevementTypeDao.findAll().contains(type);
			} else {
				return prelevementTypeDao.findByExcludedId(
					type.getPrelevementTypeId()).contains(type);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUsedObjectManager(Object o) {
		PrelevementType prelevementType = 
			prelevementTypeDao.mergeObject((PrelevementType) o);
		return prelevementType.getPrelevements().size() > 0;
	}

	@Override
	public List<PrelevementType> findByIncaCatManager(String incaCat) {
		log.debug("Recherche PrelevementType par categorie INCa: " 
				+ incaCat);
		return prelevementTypeDao.findByIncaCat(incaCat);
	}
	
	@Override
	public List< ? extends TKThesaurusObject> 
									findByOrderManager(Plateforme pf) {
		return prelevementTypeDao.findByOrder(pf);
	}
	
	@Override
	public TKThesaurusObject findByIdManager(Integer id) {
		return prelevementTypeDao.findById(id);
	}

}
