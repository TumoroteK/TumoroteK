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
package fr.aphp.tumorotek.manager.impl.coeur.echantillon;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchanQualiteDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchanQualiteManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.echantillon.
													EchanQualiteValidator;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Implémentation du manager du bean de domaine EchanQualite.
 * Classe créée le 24/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class EchanQualiteManagerImpl implements EchanQualiteManager {

	private Log log = LogFactory.getLog(EchanQualiteManager.class);
	
	/** Bean Dao EchanQualiteDao. */
	private EchanQualiteDao echanQualiteDao;
	/** Bean Dao EchantillonDao. */
	private EchantillonDao echantillonDao;
	/** Bean Validator. */
	private EchanQualiteValidator echanQualiteValidator;
	private PlateformeDao plateformeDao;
	
	/**
	 * Setter du bean EchanQualiteDao.
	 * @param eDao est le bean Dao.
	 */
	public void setEchanQualiteDao(EchanQualiteDao eDao) {
		this.echanQualiteDao = eDao;
	}

	/**
	 * Setter du bean EchantillonDao.
	 * @param eDao est le bean Dao.
	 */
	public void setEchantillonDao(EchantillonDao eDao) {
		this.echantillonDao = eDao;
	}
	
	public void setEchanQualiteValidator(EchanQualiteValidator validator) {
		this.echanQualiteValidator = validator;
	}
	
	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	/**
	 * Recherche une qualité d'échantillon dont l'identifiant est 
	 * passé en paramètre.
	 * @param echanQualiteId Identifiant de la qualité que l'on recherche.
	 * @return Une EchanQualite.
	 */
	public EchanQualite findByIdManager(Integer echanQualiteId) {
		return echanQualiteDao.findById(echanQualiteId);
	}
	
//	/**
//	 * Recherche toutes les qualités d'échantillons présentes dans la base.
//	 * @return Liste de EchanQualite.
//	 */
//	public List<EchanQualite> findAllObjectsManager() {
//		log.info("Recherche de toutes les EchanQualites");
//		return echanQualiteDao.findByOrder();
//	}
	
	/**
	 * Recherche toutes les qualités d'échantillons dont la valeur commence
	 * comme celle passée en paramètre.
	 * @param qualite Qualité que l'on recherche.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de EchanQualite.
	 */
	public List<EchanQualite> findByQualiteLikeManager(
			String qualite, boolean exactMatch) {
		log.debug("Recherche EchanQualite par " 
				+ qualite + " exactMatch " + String.valueOf(exactMatch));
		if (qualite != null) {
			if (!exactMatch) {
				qualite = qualite + "%";
			}
			return echanQualiteDao.findByQualite(qualite);
		} else {
			return new ArrayList<EchanQualite>();
		}
	}
	
	@Override
	public boolean findDoublonManager(Object obj) {		
		
		EchanQualite qualite = (EchanQualite) obj;
		
		if (qualite != null) {
			if (qualite.getEchanQualiteId() == null) {
				return echanQualiteDao.findAll().contains(qualite);
			} else {
				return echanQualiteDao.findByExcludedId(
					qualite.getEchanQualiteId())
						.contains(qualite);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUsedObjectManager(Object obj) {
		EchanQualite qualite = (EchanQualite) obj;

		List<Echantillon> list = echantillonDao.findByEchanQualite(qualite);
		
		return (list.size() > 0);
	}

	@Override
	public void createObjectManager(Object obj) {
		EchanQualite qualite = (EchanQualite) obj;
		// On vérifie que la pf n'est pas null. Si c'est le cas on envoie
		// une exception
		if (qualite.getPlateforme() == null) {
			throw new RequiredObjectIsNullException(
					"EchanQualite", "creation", "Plateforme");
		} else {
			qualite.setPlateforme(plateformeDao
					.mergeObject(qualite.getPlateforme()));
		}
		
		if (findDoublonManager(qualite)) {
			log.warn("Doublon lors de la creation de l'objet EchanQualite : " 
					+ qualite.toString());
			throw new DoublonFoundException("EchanQualite", "creation");
		} else {
			BeanValidator.validateObject(
					qualite, new Validator[]{echanQualiteValidator});
			echanQualiteDao.createObject(qualite);
			log.info("Enregistrement de l'objet EchanQualite : " 
					+ qualite.toString());
		}
	}

	@Override
	public void updateObjectManager(Object obj) {
		EchanQualite qualite = (EchanQualite) obj;
		if (findDoublonManager(qualite)) {
			log.warn("Doublon lors de la modification de l'objet " 
					+ "EchanQualite : " + qualite.toString());
			throw new DoublonFoundException("EchanQualite", "modification");
		} else {
			BeanValidator.validateObject(
					qualite, new Validator[]{echanQualiteValidator});
			echanQualiteDao.updateObject(qualite);
			log.info("Modification de l'objet EchanQualite : " 
					+ qualite.toString());
		}
	}

	@Override
	public void removeObjectManager(Object obj) {
		EchanQualite qualite = (EchanQualite) obj;
//		if (isUsedObjectManager(qualite)) {
//			log.warn("Objet utilisé lors de la suppression de l'objet " 
//					+ "EchanQualite : " + qualite.toString());
//			throw new ObjectUsedException("EchanQualite", "suppression");
//		} else {
			echanQualiteDao.removeObject(qualite.getEchanQualiteId());
//			log.info("Suppression de l'objet EchanQualite : " 
//					+ qualite.toString());
//		}
	}

	@Override
	public List< ? extends TKThesaurusObject> 
									findByOrderManager(Plateforme pf) {
		return echanQualiteDao.findByOrder(pf);
	}
}
