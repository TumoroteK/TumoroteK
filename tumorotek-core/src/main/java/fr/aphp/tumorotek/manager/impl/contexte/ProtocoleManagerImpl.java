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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ProtocoleDao;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.ProtocoleValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 * 
 * Implémentation du manager du bean de domaine Protocole.
 * Classe créée le 13/02/2012.
 * 
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class ProtocoleManagerImpl implements ProtocoleManager {
	
	private Log log = LogFactory.getLog(ProtocoleManager.class);
	
	private ProtocoleDao protocoleDao;
	private ProtocoleValidator protocoleValidator;
	private PlateformeDao plateformeDao;

	public ProtocoleManagerImpl() {
	}
	
	/* Properties setters */
	public void setProtocoleDao(ProtocoleDao pDao) {
		this.protocoleDao = pDao;
	}
	
	public void setProtocoleValidator(ProtocoleValidator pValidator) {
		this.protocoleValidator = pValidator;
	}

	public void setPlateformeDao(PlateformeDao pfDao) {
		this.plateformeDao = pfDao;
	}

	@Override
	public void createObjectManager(Object obj) {
		
		Protocole pt = (Protocole) obj;
		
		// On vérifie que la pf n'est pas null. Si c'est le cas on envoie
		// une exception
		if (pt.getPlateforme() == null) {
			log.warn("Objet obligatoire Plateforme "
					+ "manquant lors de la creation " 
					+  "d'un objet Protocole");
			throw new RequiredObjectIsNullException(
					"Protocole", "creation", "Plateforme");
		} else {
			pt.setPlateforme(plateformeDao.mergeObject(pt.getPlateforme()));
		}
		
		BeanValidator.validateObject(pt, new Validator[]{protocoleValidator});
		if (!findDoublonManager(pt)) {
			protocoleDao.createObject(pt);
			log.info("Enregistrement objet Protocole " + pt.toString());
		} else {
			log.warn("Doublon lors creation objet Protocole "
					+ pt.toString());
			throw new DoublonFoundException("Protocole", "creation");
		}			
	}
	
	@Override
	public void updateObjectManager(Object obj) {
		BeanValidator.validateObject(obj, new Validator[]{protocoleValidator});
		if (!findDoublonManager(obj)) { 
			protocoleDao.updateObject((Protocole) obj);
			log.info("Modification objet Protocole " + obj.toString());
		} else {
			log.warn("Doublon lors modification objet Protocole "
					+ obj.toString());
			throw new DoublonFoundException("Protocole", "modification");
		}
	}

	@Override
	public List<Protocole> findAllObjectsManager() {
		return protocoleDao.findAll();
	}
	

	@Override
	public void removeObjectManager(Object obj) {
		if (obj != null) {
//			if (!isUsedObjectManager(obj)) {
				protocoleDao.removeObject(((Protocole) obj).getProtocoleId());
				log.info("Suppression objet Protocole " + obj.toString());
//			} else {
//				log.warn("Suppression objet Risque " + obj.toString()
//						+ " impossible car est reference (par Prelevement)");
//				throw new ObjectUsedException("thesaurus.deletion.isUsed", 
//																	false);
//			}
		} else {
			log.warn("Suppression d'un Protocole null");
		}
	}

	@Override
	public boolean findDoublonManager(Object o) {
		if (o != null) {
			Protocole pt = (Protocole) o;
			if (pt.getProtocoleId() == null) {
				return protocoleDao.findAll().contains(pt);
			} else {
				return protocoleDao.findByExcludedId(
						pt.getProtocoleId()).contains(pt);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUsedObjectManager(Object o) {
		Protocole pt = protocoleDao.mergeObject((Protocole) o);
		return pt.getPrelevements().size() > 0;
	}

	@Override
	public List<TKThesaurusObject> findByOrderManager(Plateforme pf) {
		return protocoleDao.findByOrder(pf);
	}

	@Override
	public TKThesaurusObject findByIdManager(Integer id) {
		return null;
	}
}
