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
package fr.aphp.tumorotek.manager.impl.stats;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.dao.stats.SModeleIndicateurDao;
import fr.aphp.tumorotek.dao.stats.SubdivisionDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.stats.SModeleManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stats.SModeleValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import java.util.*;

/**
 * Modeles Manager pour les Indicateurs Qualites
 * 
 * @author jhusson
 * @version 2.1
 * 
 */
public class SModeleManagerImpl implements SModeleManager {

	private Log log = LogFactory.getLog(SModeleManager.class);

	private SModeleDao sModeleDao;
	private PlateformeDao plateformeDao;
	private SModeleValidator sModeleValidator;
	private SModeleIndicateurDao sModeleIndicateurDao;
	private BanqueDao banqueDao;
	private SubdivisionDao subdivisionDao;

	private static final List<SModele> NO_LIST = new ArrayList<SModele>();

	public void setSModeleDao(SModeleDao mDao) {
		this.sModeleDao = mDao;
	}

	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	public void setBanqueDao(BanqueDao banqueDao) {
		this.banqueDao = banqueDao;
	}

	public void setSModeleValidator(SModeleValidator s) {
		this.sModeleValidator = s;
	}

	public void setsModeleIndicateurDao(SModeleIndicateurDao smd) {
		this.sModeleIndicateurDao = smd;
	}

	public void setSubdivisionDao(SubdivisionDao s) {
		this.subdivisionDao = s;
	}

	@Override
	public SModele findByIdManager(Integer modeleId) {
		log.debug("Recherche de tous les Indicateurs Modeles");
		return sModeleDao.findById(modeleId);
	}

	@Override
	public List<SModele> findAllObjectsManager() {
		return sModeleDao.findAll();
	}

	@Override
	public List<SModele> findByPlateformeManager(Plateforme plateforme) {
		if (plateforme != null) {
			return sModeleDao.findByPlateforme(plateforme);
		} else {
			return NO_LIST;
		}
	}

	@Override
	public Set<Banque> getBanquesManager(SModele im) {
		if (im != null && im.getSmodeleId() != null) {
			log.debug("Recherche de toutes les banques d'un conteneur");
			im = sModeleDao.mergeObject(im);
			Set<Banque> banques = im.getBanques();
			banques.isEmpty();
			return banques;
		} else {
			return new HashSet<Banque>();
		}
	}

	@Override
	public Boolean findDoublonManager(SModele modele) {
		if (modele != null) {
			if (modele.getSmodeleId() == null) {
				return sModeleDao.findAll().contains(modele);
			} else {
				return sModeleDao.findByExcludedId(
						modele.getSmodeleId()).contains(modele);
			}
		} else {
			return false;
		}
	}

	@Override
	public Boolean isUsedObjectManager(SModele modele) {
		return false;
	}

	@Override
	public void createObjectManager(SModele modele,
			Plateforme plateforme, List<Indicateur> indicateurs,
			List<Banque> banques) {
		// plateforme required
		if (plateforme != null) {
			modele.setPlateforme(plateformeDao.mergeObject(plateforme));
		} else {
			log.warn("Objet obligatoire Plateforme manquant"
					+ " lors de la création d'un Indicateur Modele");
			throw new RequiredObjectIsNullException("IndicateurModele",
					"creation", "Plateforme");
		}

		// Test s'il y a des doublons
		if (findDoublonManager(modele)) {
			log.warn("Doublon lors de la creation de l'objet Modele : "
					+ modele.toString());
			throw new DoublonFoundException("Modele", "creation");
		} 

		BeanValidator.validateObject(modele,
				new Validator[] { sModeleValidator });

		if (banques != null) {
			modele.getBanques().addAll(banques);
		}
		
		sModeleDao.createObject(modele);
		
		updateAssociations(modele, indicateurs);
		
		// updateBanquesAssociation(banques, modele);

		log.debug("Enregistrement de l'objet Modele : " + modele.toString());
	}

	@Override
	public SModele updateObjectManager(SModele modele,
			Plateforme plateforme, List<Indicateur> indicateurs,
			List<Banque> banques) {
		// plateforme required
		if (plateforme != null) {
			modele.setPlateforme(plateformeDao.mergeObject(plateforme));
		} else {
			log.warn("Objet obligatoire Plateforme manquant"
					+ " lors de la modification d'un IndicateurModele");
			throw new RequiredObjectIsNullException("IndicateurModele",
					"modification", "Plateforme");
		}

		// Test s'il y a des doublons
		if (findDoublonManager(modele)) {
			log.warn("Doublon lors de la modification de l'objet IndicateurModele : "
					+ modele.toString());
			throw new DoublonFoundException("IndicateurModele", "modification");
		}

		// validation
		BeanValidator.validateObject(modele,
				new Validator[] { sModeleValidator });
		
		sModeleDao.updateObject(modele);
		
//		if (banques != null) {
//			modele.getBanques().addAll(banques);
//			sModeleDao.mergeObject(modele);
//		}
		modele = updateBanquesAssociation(banques, modele);
		
		modele = updateAssociations(modele, indicateurs);
		
		log.debug("Modification de l'objet Modele : " + modele.toString());
		
		return modele;
	}

	@Override
	public void removeObjectManager(SModele modele) {
		if (modele != null) {
			sModeleDao.removeObject(modele.getSmodeleId());
			log.info("Suppression de l'objet IndicateurModele : "
					+ modele.toString());
		} else {
			log.warn("Suppression d'un IndicateurModele null");
		}
	}

	/**
	 * Cette méthode met à jour les associations entre un modele et ses
	 * statements.
	 * 
	 * @param modele  IndicateurModele pour lequel on veut mettre à jour les
	 *            associations.
	 * @param statementToCreate Liste des statement a associer.
	 * @return SModele modifié
	 * @version 2.1
	 */
	public SModele updateAssociations(SModele sModele, List<Indicateur> indicateurs) {
		
		if (indicateurs != null) {

			sModele = sModeleDao.mergeObject(sModele);

			Iterator<SModeleIndicateur> it = sModele.getSModeleIndicateurs().iterator();
			List<SModeleIndicateur> smToRemove = new ArrayList<SModeleIndicateur>();
			while (it.hasNext()) {
				SModeleIndicateur tmp = it.next();
				if (!indicateurs.contains(tmp.getIndicateur())) {
					smToRemove.add(tmp);
				}
			}

			for (int i = 0; i < smToRemove.size(); i++) {
				SModeleIndicateur sm = sModeleIndicateurDao.mergeObject(smToRemove.get(i));
				sModele.getSModeleIndicateurs().remove(sm);
				sModeleIndicateurDao.removeObject(sm.getPk());

				log.debug("Suppression de l'association entre le sModele : " 
						+ sModele.toString() + " et l'indicateur : "
						+ sm.getIndicateur().toString());
			}

			for (int i = 0; i < indicateurs.size(); i++) {
				SModeleIndicateur sm = new SModeleIndicateur();
				SModeleIndicateurPK pk = new SModeleIndicateurPK(sModele, indicateurs.get(i));
				sm.setPk(pk);
				sm.setOrdre(i + 1);
				if (!sModele.getSModeleIndicateurs().contains(sm)) {
					sModele.getSModeleIndicateurs()
					.add(sModeleIndicateurDao.mergeObject(sm));

					log.debug("Ajout de l'association entre le sModele : " 
							+ sModele.toString() + " et l'indicateur : "
							+ indicateurs.get(i).toString());
				} else { // on modifie l'ordre du medecin present avec la liste
					sm = sModeleIndicateurDao.findById(pk);
					sm.setOrdre(i + 1);
					sModeleIndicateurDao.mergeObject(sm);
				}
			}
		}
		
		return sModele;

	}

	public SModele updateBanquesAssociation(List<Banque> banques,
			SModele model) {
		if (banques != null) {
			model = sModeleDao.mergeObject(model);
			if (banques != null) {
				Iterator<Banque> it = model.getBanques().iterator();
				List<Banque> banquesToRemove = new ArrayList<Banque>();
				// on parcourt les Banques qui sont actuellement associés
				// au modele
				while (it.hasNext()) {
					Banque tmp = it.next();
					// si une Banque n'est pas dans la nouvelle liste, on
					// la conserve afin de la retirer par la suite
					if (!banques.contains(tmp)) {
						banquesToRemove.add(tmp);
					}
				}

				// on parcourt la liste des Banques à retirer de
				// l'association
				for (int i = 0; i < banquesToRemove.size(); i++) {
					Banque bank = banqueDao.mergeObject(banquesToRemove.get(i));
					// on retire la Banque de chaque coté de l'association
					model.getBanques().remove(bank);
					bank.getSModeles().remove(model);

					log.debug("Suppression de l'association entre le "
							+ "modele : " + model.toString() + " et la banque : "
							+ bank.toString());
				}

				// on parcourt la nouvelle liste de banques
				for (Banque b : banques) {
					// si une banque n'était pas associée au modele
					if (!model.getBanques().contains(b)) {
						// on ajoute la Banque des deux cotés de l'association
						model.getBanques().add(banqueDao.mergeObject(b));
						banqueDao.mergeObject(b).getSModeles().add(model);

						log.debug("Ajout de l'association entre le "
								+ "Modele : " + model.toString()
								+ " et la banque : " + b.toString());
					}
				}
			}
		}
		return model;
	}
	
	@Override
	public Set<SModeleIndicateur> getSModeleIndicateursManager(SModele sM) {
		sM = sModeleDao.mergeObject(sM);
		Set<SModeleIndicateur> indics = sM.getSModeleIndicateurs();
		indics.isEmpty(); // operation empechant LazyInitialisationException
		return indics;
	}
	
	@Override
	public List<Indicateur> getIndicateursManager(SModele sM) {
		List<Indicateur> indics = new ArrayList<Indicateur>();
		if (sM != null) {
			SModele sModele = sModeleDao.mergeObject(sM);
			LinkedHashSet<SModeleIndicateur> sms = 
				new LinkedHashSet<SModeleIndicateur>(sModele.getSModeleIndicateurs());
			Iterator<SModeleIndicateur> it = sms.iterator();
			while (it.hasNext()) {
				SModeleIndicateur sm = it.next();
				indics.add(sm.getIndicateur());
			}
		}
		return indics;
	}

	@Override
	public Subdivision getSubdivisionManager(SModele sM) {
		List<Subdivision> subs = subdivisionDao.findByModele(sM);
		if (!subs.isEmpty()) {
			if (subs.size() == 1) {
				return subs.get(0);
			} else {
				throw new RuntimeException("stats.modele.indicateurs.toomany.subdivisions");
			}
		}
		return null;
	}

}
