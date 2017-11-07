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
package fr.aphp.tumorotek.manager.impl.contexte;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.SpecialiteDao;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.SpecialiteValidator;
import fr.aphp.tumorotek.model.contexte.Specialite;

/**
 * 
 * Implémentation du manager du bean de domaine Specialite.
 * Interface créée le 12/10/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class SpecialiteManagerImpl implements SpecialiteManager {
	
	private Log log = LogFactory.getLog(SpecialiteManager.class);
	
	/** Bean Dao SpecialiteDao. */
	private SpecialiteDao specialiteDao;
	private SpecialiteValidator specialiteValidator;

	/**
	 * Setter du bean SpecialiteDao.
	 * @param sDao est le bean Dao.
	 */
	public void setSpecialiteDao(SpecialiteDao sDao) {
		this.specialiteDao = sDao;
	}

	public void setSpecialiteValidator(SpecialiteValidator sValidator) {
		this.specialiteValidator = sValidator;
	}

	/**
	 * Recherche une Specialite dont l'identifiant est passé en paramètre.
	 * @param specialiteId Identifiant de la specialite que l'on recherche.
	 * @return Une Specialite.
	 */
	@Override
	public Specialite findByIdManager(Integer specialiteId) {
		return specialiteDao.findById(specialiteId);
	}
	
	/**
	 * Recherche toutes les specialites présentes dans la base.
	 * @return Liste de Specialites.
	 */
	@Override
	public List<Specialite> findAllObjectsManager() {
		return specialiteDao.findByOrder();
	}

	/**
	 * Recherche toutes les specialites dont le nom commence
	 * comme celui passé en paramètre.
	 * @param nom Specialite que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de Specialites.
	 */
	@Override
	public List<Specialite> findByNomLikeManager(String nom,
			boolean exactMatch) {
		log.debug("Recherche Specialite par " 
				+ nom + " exactMatch " + String.valueOf(exactMatch));
		if (nom != null) {
			if (!exactMatch) {
				nom = nom + "%";
			}
			return specialiteDao.findByNom(nom);
		} else {
			return new ArrayList<Specialite>();
		}
	}
	
	@Override
	public boolean findDoublonManager(Object o) {
		if (o != null) {
			Specialite spe = (Specialite) o;
			if (spe.getSpecialiteId() == null) {
				return specialiteDao.findAll().contains(spe);
			} else {
				return specialiteDao.findByExcludedId(
						spe.getSpecialiteId()).contains(spe);
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isUsedObjectManager(Object obj) {
		Specialite spe = specialiteDao.mergeObject((Specialite) obj);
		return spe.getCollaborateurs().size() > 0;
	}

	@Override
	public void createObjectManager(Object obj) {
		BeanValidator.validateObject(obj, new Validator[]{
				specialiteValidator});
		if (!findDoublonManager(obj)) {
			specialiteDao.createObject((Specialite) obj);
			log.info("Enregistrement objet Specialite " 
					+ obj.toString());
		} else {
			log.warn("Doublon lors creation objet Specialite " 
					+ obj.toString());
			throw new DoublonFoundException("Specialite", 
					"creation");
		}	
	}

	@Override
	public void updateObjectManager(Object obj) {
		BeanValidator.validateObject(obj, new Validator[]{specialiteValidator});
		if (!findDoublonManager(obj)) {
			specialiteDao.updateObject((Specialite) obj);
			log.info("Modification objet Specialite " 
					+ obj.toString());
		} else {
			log.warn("Doublon lors modification objet Specialite "
					+ obj.toString());
			throw new DoublonFoundException("Specialite", 
					"modification");
		}
	}

	@Override
	public void removeObjectManager(Object obj) {
		if (obj != null) {
//			if (!isUsedObjectManager(obj)) {
				specialiteDao.removeObject(
						((Specialite) obj).getSpecialiteId());
				log.info("Suppression objet Specialite " 
						+ obj.toString());
//			} else {
//				log.warn("Suppression objet Specialite " 
//						+ obj.toString()
//						+ " impossible car est reference " 
//						+ "(par Collaborateur)");
//				throw new ObjectUsedException();
//			}
		} else {
			log.warn("Suppression d'une Specialite null");
		}
	}
}
