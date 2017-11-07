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

import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 * 
 * Implémentation du manager du bean de domaine LaboInter.
 * Classe créée le 05/10/09.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class LaboInterManagerImpl implements LaboInterManager {
	
	private Log log = LogFactory.getLog(LaboInterManager.class);
	
	/* Beans injectes par Spring*/
	private LaboInterDao laboInterDao;
	private PrelevementDao prelevementDao;
	private ServiceDao serviceDao;
	private CollaborateurDao collaborateurDao;
	private TransporteurDao transporteurDao;
	private LaboInterValidator laboInterValidator;


	public LaboInterManagerImpl() {
	}
	
	/* Properties setters */
	public void setLaboInterDao(LaboInterDao nDao) {
		this.laboInterDao = nDao;
	}
	
	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setServiceDao(ServiceDao sDao) {
		this.serviceDao = sDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	public void setTransporteurDao(TransporteurDao tDao) {
		this.transporteurDao = tDao;
	}
	
	
	public void setLaboInterValidator(LaboInterValidator lValidator) {
		this.laboInterValidator = lValidator;
	}

	@Override
	public void createObjectManager(LaboInter obj, Prelevement prelevement,
			Service service, Collaborateur collaborateur,
			Transporteur transporteur, boolean doValidation) {
		//Prelevement required
		if (prelevement == null) {
			log.warn("Objet obligatoire Prelevement manquant"
							+ " lors de la creation d'un LaboInter");
			throw new RequiredObjectIsNullException(
					"LaboInter", "creation", "Prelevement");
		}
		obj.setPrelevement(prelevementDao.mergeObject(prelevement));
		if (doValidation) { //Validation
			BeanValidator
				.validateObject(obj, new Validator[]{laboInterValidator});
		}
		//Doublon
		if (!findDoublonManager(obj)) {
			obj.setService(serviceDao.mergeObject(service));
			obj.setCollaborateur(collaborateurDao.mergeObject(collaborateur));
			obj.setTransporteur(transporteurDao.mergeObject(transporteur));
			laboInterDao.createObject(obj);
			log.info("Enregistrement objet LaboInter " + obj.toString());
			
			prelevementDao.mergeObject(prelevement).getLaboInters().add(obj);
			
		} else {
			log.warn("Doublon lors creation objet LaboInter "
					+ obj.toString());
			throw new DoublonFoundException("LaboInter", "creation");
		}	
	}
	
	@Override
	public void updateObjectManager(LaboInter obj, Prelevement prelevement,
			Service service, Collaborateur collaborateur,
			Transporteur transporteur, boolean doValidation) {
		//Prelevement required
		if (prelevement == null) {
			log.warn("Objet obligatoire Prelevement manquant lors" 
					+ " de la modification d'un LaboInter " + obj.toString());
			throw new RequiredObjectIsNullException(
					"LaboInter", "modification", "Prelevement");
		}
		obj.setPrelevement(prelevementDao.mergeObject(prelevement));
		if (doValidation) {
			//Validation
			BeanValidator
				.validateObject(obj, new Validator[]{laboInterValidator});
		}
		//Doublon
		if (!findDoublonManager(obj)) {
			laboInterDao.updateObject(obj);
			log.info("Modification objet LaboInter " + obj.toString());
		} else {
			log.warn("Doublon lors modification objet LaboInter "
					+ obj.toString());
			throw new DoublonFoundException("LaboInter", "modification");
		}
	}

	@Override
	public List<LaboInter> findAllObjectsManager() {
		log.debug("Recherche totalite des LaboInter");
		return laboInterDao.findAll();
	}

	@Override
	public List<LaboInter> findByServiceManager(Service service) {
		if (service != null) {
			log.debug("Recherche LaboInter par service: " 
					+ service.toString());
		}
		return laboInterDao.findByService(service);
	}
	
	@Override
	public List<LaboInter> 
		findByCollaborateurManager(Collaborateur collaborateur) {
		if (collaborateur != null) {
			log.debug("Recherche LaboInter par collaborateur: " 
				+ collaborateur.toString());
		}
		return laboInterDao.findByCollaborateur(collaborateur);
	}
	
	@Override
	public List<LaboInter> 
		findByTransporteurManager(Transporteur transporteur) {
		if (transporteur != null) {
			log.debug("Recherche LaboInter par collaborateur: " 
					+ transporteur.toString());
		}
		return laboInterDao.findByTransporteur(transporteur);
	}

	@Override
	public void removeObjectManager(LaboInter obj) {
		if (obj != null) {
			laboInterDao.removeObject(obj.getLaboInterId());
			log.info("Suppression objet LaboInter " + obj.toString());
		} else {
			log.warn("Suppression d'un LaboInter null");
		}
	}

	@Override
	public boolean findDoublonManager(LaboInter obj) {
		Prelevement prlvt = obj.getPrelevement();
			
		if (prlvt != null) {
			if (obj.getLaboInterId() == null) {
				return laboInterDao.findByPrelevementWithOnlyOrder(prlvt)
					.contains(obj.getOrdre());
			} else {
				return laboInterDao
					.findByPrelevementWithOnlyOrderAndExcludedId(
							prlvt, obj.getLaboInterId())
							.contains((obj.getOrdre()));
			}
		} else {
			return false;
		}
		
	}

	@Override
	public List<LaboInter> findByPrelevementWithOrder(Prelevement prelevement) {
		return laboInterDao.findByPrelevementWithOrder(prelevement);
	}

}
