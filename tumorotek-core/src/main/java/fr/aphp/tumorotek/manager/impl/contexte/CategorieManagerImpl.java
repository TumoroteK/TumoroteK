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

import fr.aphp.tumorotek.dao.contexte.CategorieDao;
import fr.aphp.tumorotek.manager.context.CategorieManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.CategorieValidator;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 * 
 * Implémentation du manager du bean de domaine Categorie.
 * Interface créée le 06/01/10.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CategorieManagerImpl implements CategorieManager {
	
	private Log log = LogFactory.getLog(CategorieManager.class);
	
	/** Bean Dao CategorieDao. */
	private CategorieDao categorieDao;
	private CategorieValidator categorieValidator;

	/**
	 * Setter du bean CategorieDao.
	 * @param cDao est le bean Dao.
	 */
	public void setCategorieDao(CategorieDao cDao) {
		this.categorieDao = cDao;
	}

	public void setCategorieValidator(CategorieValidator cValidator) {
		this.categorieValidator = cValidator;
	}

	/**
	 * Recherche une Categorie dont l'identifiant est passé en paramètre.
	 * @param categorieId Identifiant de la Categorie que l'on recherche.
	 * @return Une Categorie.
	 */
	@Override
	public Categorie findByIdManager(Integer categorieId) {
		return categorieDao.findById(categorieId);
	}
	
	/**
	 * Recherche toutes les Categories présentes dans la base.
	 * @return Liste de Categories.
	 */
	@Override
	public List<Categorie> findAllObjectsManager() {
		return categorieDao.findByOrder();
	}

	/**
	 * Recherche toutes les Categories dont le nom commence
	 * comme celui passé en paramètre.
	 * @param nom Categorie que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de Categories.
	 */
	@Override
	public List<Categorie> findByNomLikeManager(
			String nom, boolean exactMatch) {
		
		log.debug("Recherche Categorie par " 
				+ nom + " exactMatch " + String.valueOf(exactMatch));
		if (nom != null) {
			if (!exactMatch) {
				nom = nom + "%";
			}
			return categorieDao.findByNom(nom);
		} else {
			return new ArrayList<Categorie>();
		}
	}
	
	@Override
	public boolean findDoublonManager(Object o) {
		if (o != null) {
			Categorie cat = (Categorie) o;
			if (cat.getCategorieId() == null) {
				return categorieDao.findAll().contains(cat);
			} else {
				return categorieDao.findByExcludedId(
						cat.getCategorieId()).contains(cat);
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isUsedObjectManager(Object obj) {
		Categorie cat = categorieDao.mergeObject((Categorie) obj);
		return cat.getEtablissements().size() > 0;
	}

	@Override
	public void createObjectManager(Object obj) {
		BeanValidator.validateObject(obj, new Validator[]{
				categorieValidator});
		if (!findDoublonManager(obj)) {
			categorieDao.createObject((Categorie) obj);
			log.info("Enregistrement objet Categorie " 
					+ obj.toString());
		} else {
			log.warn("Doublon lors creation objet Categorie " 
					+ obj.toString());
			throw new DoublonFoundException("Categorie", 
					"creation");
		}	
	}

	@Override
	public void updateObjectManager(Object obj) {
		BeanValidator.validateObject(obj, new Validator[]{
				categorieValidator});
		if (!findDoublonManager(obj)) {
			categorieDao.updateObject((Categorie) obj);
			log.info("Modification objet Categorie " 
					+ obj.toString());
		} else {
			log.warn("Doublon lors modification objet Categorie "
					+ obj.toString());
			throw new DoublonFoundException("Categorie", 
					"modification");
		}
	}

	@Override
	public void removeObjectManager(Object obj) {
		if (obj != null) {
//			if (!isUsedObjectManager(obj)) {
				categorieDao.removeObject(
						((Categorie) obj).getCategorieId());
				log.info("Suppression objet Categorie " 
						+ obj.toString());
//			} else {
//				log.warn("Suppression objet Categorie " 
//						+ obj.toString()
//						+ " impossible car est reference " 
//						+ "(par Etablissement)");
//				throw new ObjectUsedException();
//			}
		} else {
			log.warn("Suppression d'une Categorie null");
		}
	}
}
