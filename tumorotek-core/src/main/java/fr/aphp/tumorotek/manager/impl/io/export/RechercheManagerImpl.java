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
package fr.aphp.tumorotek.manager.impl.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.RechercheDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.RechercheManager;
import fr.aphp.tumorotek.manager.io.export.RequeteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.RechercheValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Implémentation du manager du bean de domaine Recherche. Classe créée le
 * 25/02/10.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 * 
 */
public class RechercheManagerImpl implements RechercheManager {

	private Log log = LogFactory.getLog(RechercheManager.class);

	/** Liste des recherches du manager. */
	private List<Recherche> recherches = new ArrayList<Recherche>();
	/** Bean Dao RechercheDao. */
	private RechercheDao rechercheDao = null;
	/** Bean Dao AffichageDao. */
	private AffichageDao affichageDao = null;
	/** Bean Dao RequeteDao. */
	private RequeteDao requeteDao = null;
	/** Bean Dao BanqueDao. */
	private BanqueDao banqueDao = null;
	/** Bean Manager RequeteManager. */
	private RequeteManager requeteManager = null;
	/** Bean Manager AffichageManager. */
	private AffichageManager affichageManager = null;
	/** Bean Validator. */
	private RechercheValidator rechercheValidator = null;

	public RechercheManagerImpl() {
		super();
	}

	/**
	 * Setter du bean RechercheDao.
	 * 
	 * @param recDao
	 *            est le bean Dao.
	 */
	public void setRechercheDao(RechercheDao recDao) {
		this.rechercheDao = recDao;
	}

	/**
	 * Setter du bean AffichageDao.
	 * 
	 * @param affDao
	 *            est le bean Dao.
	 */
	public void setAffichageDao(AffichageDao affDao) {
		this.affichageDao = affDao;
	}

	/**
	 * Setter du bean RequeteDao.
	 * 
	 * @param reqDao
	 *            est le bean Dao.
	 */
	public void setRequeteDao(RequeteDao reqDao) {
		this.requeteDao = reqDao;
	}

	/**
	 * Setter du bean BanqueDao.
	 * 
	 * @param bDao
	 *            est le bean Dao.
	 */
	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	/**
	 * Setter du bean RequeteManager.
	 * 
	 * @param rManager
	 *            est le bean Manager
	 */
	public void setRequeteManager(RequeteManager rManager) {
		this.requeteManager = rManager;
	}

	/**
	 * Setter du bean AffichageManager.
	 * 
	 * @param aManager
	 *            est le bean Manager
	 */
	public void setAffichageManager(AffichageManager aManager) {
		this.affichageManager = aManager;
	}

	/**
	 * Setter du bean RechercheValidator.
	 * 
	 * @param rechercheValidator
	 *            est le bean Validator.
	 */
	public void setRechercheValidator(RechercheValidator validator) {
		this.rechercheValidator = validator;
	}

	/**
	 * Recherche une Recherche dont l'identifiant est passé en paramètre.
	 * 
	 * @param id
	 *            Identifiant de la Recherche que l'on recherche.
	 * @return une Recherche.
	 */
	@Override
	public Recherche findByIdManager(Integer id) {
		// On vérifie que l'identifiant n'est pas nul
		if (id == null) {
			log.warn("Objet obligatoire identifiant manquant lors de la "
					+ "recherche par l'identifiant d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche par identifiant", "identifiant");
		}
		return rechercheDao.findById(id);
	}

	/**
	 * Recherche toutes les Recherches présentes dans la BDD.
	 * 
	 * @return Liste de Recherches.
	 */
	@Override
	public List<Recherche> findAllObjectsManager() {
		return rechercheDao.findAll();
	}

	/**
	 * Renomme une Recherche (change son intitulé).
	 * 
	 * @param recherche
	 *            Recherche à renommer.
	 * @param intitule
	 *            nouvel intitulé de la Recherche.
	 */
	@Override
	public void renameRechercheManager(Recherche recherche, String intitule) {
		// On verifie que la recherche n'est pas nulle
		if (recherche == null) {
			log.warn("Objet obligatoire Recherche manquant lors "
					+ "du renommage d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"modification", "Recherche");
		}
		if (findByIdManager(recherche.getRechercheId()) == null) {
			throw new SearchedObjectIdNotExistException("Recherche", recherche
					.getRechercheId());
		} else {
			// on modifie l'intitule
			recherche.setIntitule(intitule);
			// On met a jour la recherche
			if (findDoublonManager(recherche)) {
				log.warn("Doublon lors de la modification de l'objet "
						+ "Recherche : " + recherche.toString());
				throw new DoublonFoundException("Recherche", "modification");
			} else {
				BeanValidator.validateObject(recherche,
						new Validator[] { rechercheValidator });
				rechercheDao.updateObject(recherche);
			}
		}
	}

	/**
//	 * Copie une Recherche en BDD.
//	 * 
//	 * @param recherche
//	 *            Recherche à copier.
//	 * @param copieur
//	 *            Utilisateur qui copie la Recherche.
//	 * @return la Recherche copiée.
//	 */
//	@Override
//	public Recherche copyRechercheManager(Recherche recherche,
//			Utilisateur copieur, Banque banque) {
//		// On verifie que la recherche n'est pas nulle
//		if (recherche == null) {
//			log.warn("Objet obligatoire Recherche manquant lors "
//					+ "de la copie d'un objet Recherche");
//			throw new RequiredObjectIsNullException("Recherche", "copie",
//					"Recherche");
//		}
//		// On verifie que l'utilisateur n'est pas nul
//		if (copieur == null) {
//			log.warn("Objet obligatoire Utilisateur manquant lors "
//					+ "de la copie d'un objet Recherche");
//			throw new RequiredObjectIsNullException("Recherche",
//					"modification", "Utilisateur");
//		}
//		Affichage affichage = null;
//		// On vérifie que l'affichage n'appartient pas au copieur
//		if (recherche.getAffichage().getCreateur().equals(copieur)) {
//			affichage = recherche.getAffichage();
//		} else {
//			// On copie l'affichage
//			affichage = affichageManager.copyAffichageManager(recherche
//					.getAffichage(), copieur, banque);
//			// On vérifie que la copie n'est pas déjà dans la liste du copieur
//			List<Affichage> affichages = affichageManager
//					.findByUtilisateurManager(copieur);
//			if (affichages.contains(affichage)) {
//				affichage = affichages.get(affichages.indexOf(affichage));
//			}
//		}
//
//		Requete requete = null;
//		// On vérifie que la requete n'appartient pas au copieur
//		if (recherche.getRequete().getCreateur().equals(copieur)) {
//			requete = recherche.getRequete();
//		} else {
//			// On copie la requete
//			requete = requeteManager.copyRequeteManager(recherche.getRequete(),
//					copieur, banque);
//			// On vérifie que la copie n'est pas déjà dans la liste du copieur
//			List<Requete> requetes = requeteManager
//					.findByUtilisateurManager(copieur);
//			if (requetes.contains(requete)) {
//				requete = requetes.get(requetes.indexOf(requete));
//			}
//		}
//		// copie de la recherche
//		Recherche r = new Recherche(recherche.getIntitule(), affichage,
//				requete, recherche.getBanques(), copieur);
//		BeanValidator.validateObject(r, new Validator[] { rechercheValidator });
//		// enregistrement de la recherche en BDD
//		rechercheDao.createObject(r);
//
//		updateBanques(r, r.getBanques());
//
//		// ajout de la recherche copiee dans la liste
//		recherches.add(r);
//
//		return r;
//	}

	/**
	 * Créé une nouvelle Recherche en BDD.
	 * 
	 * @param recherche
	 *            Recherche à créer.
	 * @param createur
	 *            Utilisateur qui créé la Recherche.
	 */
	@Override
	public void createObjectManager(Recherche recherche, Affichage affichage,
			Requete requete, List<Banque> banques, Utilisateur createur,
			Banque banque) {
		// On vérifie que la recherche n'est pas nulle
		if (recherche == null) {
			log.warn("Objet obligatoire Recherche manquant lors "
					+ "de la création d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche", "création",
					"Recherche");
		}
		// On vérifie que l'affichage n'est pas nul
		if (affichage == null) {
			log.warn("Objet obligatoire Affichage manquant lors "
					+ "de la création d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche", "création",
					"Affichage");
		}
		// On vérifie que la requete n'est pas nulle
		if (requete == null) {
			log.warn("Objet obligatoire Requete manquant lors "
					+ "de la création d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche", "création",
					"Requete");
		}
		// On vérifie que le createur n'est pas nul
		if (createur == null) {
			log.warn("Objet obligatoire Utilisateur manquant lors "
					+ "de la création d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche", "création",
					"Utilisateur");
		}
		if (affichage.getAffichageId() != null) {
			affichage = affichageDao.mergeObject(affichage);
		} else {
			affichageManager.createObjectManager(affichage, affichage
					.getResultats(), createur, banque);
		}
		recherche.setAffichage(affichage);

		if (requete.getRequeteId() != null) {
			requete = requeteDao.mergeObject(requete);
		} else {
			requeteManager.createObjectManager(requete, requete
					.getGroupementRacine(), createur, banque);
		}
		recherche.setRequete(requete);
		
		// On met l'utilisateur dans la recherche
		recherche.setCreateur(createur);
		BeanValidator.validateObject(recherche,
				new Validator[] { rechercheValidator });
		// On enregistre la recherche
		rechercheDao.createObject(recherche);

		
		if (banques != null) {
			updateBanques(recherche, banques);
		}

		// ajout de la recherche dans la liste
		recherches.add(recherche);
	}

	/**
	 * Met à jour une Recherche en BDD.
	 * 
	 * @param recherche
	 *            Recherche à mettre à jour.
	 * @param createur
	 *            Utilisateur qui met à jour la Recherche.
	 */
	@Override
	public void updateObjectManager(Recherche recherche, Affichage affichage,
			Requete requete, List<Banque> banques, Utilisateur createur,
			Banque banque) {
		// On vérifie que la recherche n'est pas nulle
		if (recherche == null) {
			log.warn("Objet obligatoire Recherche manquant lors "
					+ "de la modification d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"modification", "Recherche");
		}
		// On vérifie que l'affichage n'est pas nul
		if (affichage == null) {
			log.warn("Objet obligatoire Affichage manquant lors "
					+ "de la modification d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"modification", "Affichage");
		}
		// On vérifie que la requete n'est pas nulle
		if (requete == null) {
			log.warn("Objet obligatoire Requete manquant lors "
					+ "de la modification d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"modification", "Requete");
		}
		// On vérifie que le createur n'est pas nul
		if (createur == null) {
			log.warn("Objet obligatoire Utilisateur manquant lors "
					+ "de la modification d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"modification", "Utilisateur");
		}
		if (affichage.getAffichageId() != null) {
			affichage = affichageDao.mergeObject(affichage);
		} else {
			affichageManager.createObjectManager(affichage, affichage
					.getResultats(), createur, banque);
		}
		recherche.setAffichage(affichage);

		if (requete.getRequeteId() != null) {
			requete = requeteDao.mergeObject(requete);
		} else {
			requeteManager.createObjectManager(requete, requete
					.getGroupementRacine(), createur, banque);
		}
		recherche.setRequete(requete);

		// On met l'utilisateur dans la recherche
		recherche.setCreateur(createur);
		// On vérifie que le bean est valide
		BeanValidator.validateObject(recherche,
				new Validator[] { rechercheValidator });
		// On met à jour la recherche
		rechercheDao.updateObject(recherche);

		if (banques != null) {
			updateBanques(recherche, banques);
		}

		// mise a jour de la liste des recherches
		recherches = findAllObjectsManager();
	}

	/**
	 * Supprime une Recherche en BDD.
	 * 
	 * @param recherche
	 *            Recherche à supprimer
	 */
	@Override
	public void removeObjectManager(Recherche recherche) {
		// On vérifie que la recherche n'est pas nulle
		if (recherche == null) {
			throw new RequiredObjectIsNullException("Recherche", "suppression",
					"Recherche");
		}
		// On vérifie que la recherche est en BDD
		if (findByIdManager(recherche.getRechercheId()) == null) {
			throw new SearchedObjectIdNotExistException("Recherche", recherche
					.getRechercheId());
		} else {
			// suppression de la recherche dans la liste
			Iterator<Recherche> it = recherches.iterator();
			while (it.hasNext()) {
				Recherche temp = it.next();
				if (temp.getRechercheId().equals(recherche.getRechercheId())) {
					recherches.remove(temp);
					break;
				}
			}

			// On supprime la recherche
			rechercheDao.removeObject(recherche.getRechercheId());
		}
	}

	/**
	 * Recherche les Recherches dont l'utilisateur créateur est passé en
	 * paramètre.
	 * 
	 * @param util
	 *            Utilisateur qui à créé les Recherches recherchées.
	 * @return la liste de toutes les Recherches de l'Utilisateur.
	 */
	@Override
	public List<Recherche> findByUtilisateurManager(Utilisateur util) {
		// On vérifie que l'utilisateur n'est pas nul
		if (util == null) {
			log.warn("Objet obligatoire Utilisateur manquant lors de la "
					+ "recherche par l'Utilisateur d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche par Utilisateur", "Utilisateur");
		}
		return rechercheDao.findByUtilisateur(util);
	}
	
	/**
	 * Recherche les Recherches dont l'intitulé est passé en paramètre.
	 * 
	 * @param intitule Intitulé des Recherches recherchées.
	 * @return la liste de toutes les Recherches de l'intitulé.
	 */
	@Override
	public List<Recherche> findByIntituleManager(String intitule) {
		// On vérifie que l'utilisateur n'est pas nul
		if (intitule == null) {
			log.warn("Objet obligatoire Intitule manquant lors de la "
					+ "recherche par l'intitule d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche par Intitule", "Intitule");
		}
		return rechercheDao.findByIntitule(intitule);
	}
	
	@Override
	public List<Recherche> findByIntituleAndUtilisateurManager(String intitule,
			Utilisateur util) {
		if (intitule != null && util != null) {
			return rechercheDao.findByIntituleUtilisateur(intitule, util);
		} else {
			return new ArrayList<Recherche>();
		}
	}
	
	/**
	 * Recherche les Recherches dont la Requête est passée en paramètre.
	 * 
	 * @param requete Requete des Recherches recherchées.
	 * @return la liste de toutes les Recherches comprenant la Requête.
	 */
	@Override
	public List<Recherche> findByRequeteManager(Requete requete) {
		// On vérifie que l'utilisateur n'est pas nul
		if (requete == null) {
			log.warn("Objet obligatoire Requête manquant lors de la "
					+ "recherche par la requête d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche par Requête", "Requête");
		}
		return rechercheDao.findByRequete(requete);
	}
	
	/**
	 * Recherche les Recherches dont l'Affichage est passé en paramètre.
	 * 
	 * @param affichage Affichage des Recherches recherchées.
	 * @return la liste de toutes les Recherches comprenant l'Affichage.
	 */
	@Override
	public List<Recherche> findByAffichageManager(Affichage affichage) {
		// On vérifie que l'utilisateur n'est pas nul
		if (affichage == null) {
			log.warn("Objet obligatoire Affichage manquant lors de la "
					+ "recherche par l'affichage d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche par Affichage", "Affichage");
		}
		return rechercheDao.findByAffichage(affichage);
	}
	
	/**
	 * Récupère les banques d'une Recherche.
	 * @param recherche Recherche dont on souhaite récupérer les banques
	 * @return liste de banques de la recherche
	 */
	public List<Banque> findBanquesManager(Recherche recherche) {
		if (recherche != null) {
			recherche = rechercheDao.mergeObject(recherche);
			List<Banque> banques = recherche.getBanques();
			banques.size();
			return banques;
		} else {
			return new ArrayList<Banque>();
		}
	}

	/**
	 * Recherche les doublons d'une Recherche passée en paramètre.
	 * 
	 * @param recherche
	 *            une Recherche pour laquelle on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	@Override
	public Boolean findDoublonManager(Recherche recherche) {
		// On vérifie que la recherche n'est pas nulle
		if (recherche == null) {
			log.warn("Objet obligatoire Recherche manquant lors de la "
					+ "recherche de doublon d'un objet Recherche");
			throw new RequiredObjectIsNullException("Recherche",
					"recherche de doublon", "Recherche");
		}
		if (recherche.getRechercheId() == null) {
			return rechercheDao.findAll().contains(recherche);
		} else {
			return rechercheDao.findByExcludedId(recherche.getRechercheId())
					.contains(recherche);
		}

	}

	/**
	 * Méthode qui permet de vérifier que 2 Recherches sont des copies.
	 * 
	 * @param r
	 *            Recherche première Recherche à vérifier.
	 * @param copie
	 *            deuxième Recherche à vérifier.
	 * @return true si les 2 Recherches sont des copies, false sinon.
	 */
	public Boolean isCopyManager(Recherche r, Recherche copie) {
		if (copie == null) {
			return false;
		} else if (r.getIntitule() == null) {
			if (copie.getIntitule() == null) {
				if (r.getAffichage() == null) {
					if (r.getRequete() == null) {
						return (copie.getRequete() == null);
					} else {
						return requeteManager.isCopyManager(r.getRequete(),
								copie.getRequete());
					}
				} else if (affichageManager.isCopyManager(r.getAffichage(),
						copie.getAffichage())) {
					if (r.getRequete() == null) {
						return (copie.getRequete() == null);
					} else {
						return requeteManager.isCopyManager(r.getRequete(),
								copie.getRequete());
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (r.getIntitule().equals(copie.getIntitule())) {
				if (r.getAffichage() == null) {
					if (r.getRequete() == null) {
						return (copie.getRequete() == null);
					} else {
						return requeteManager.isCopyManager(r.getRequete(),
								copie.getRequete());
					}
				} else if (affichageManager.isCopyManager(r.getAffichage(),
						copie.getAffichage())) {
					if (r.getRequete() == null) {
						return (copie.getRequete() == null);
					} else {
						return requeteManager.isCopyManager(r.getRequete(),
								copie.getRequete());
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Cette méthode met à jour les associations entre une recherche et une
	 * liste de banques.
	 * 
	 * @param recherche
	 *            Recherche pour laquelle on veut mettre à jour les
	 *            associations.
	 * @param banques
	 *            Liste des Banques que l'on veut associer à la recherche.
	 */
	private void updateBanques(Recherche recherche, List<Banque> banques) {

		Recherche rec = rechercheDao.mergeObject(recherche);

		if (rec.getBanques() == null) {
			rec.setBanques(new ArrayList<Banque>());
		}
		Iterator<Banque> it = rec.getBanques().iterator();
		List<Banque> banquesToRemove = new ArrayList<Banque>();
		// on parcourt les banques qui sont actuellement associées
		// à la recherche
		while (it.hasNext()) {
			Banque tmp = it.next();
			// si une banque n'est pas dans la nouvelle liste, on
			// la conserve afin de la retirer par la suite
			if (!banques.contains(tmp)) {
				banquesToRemove.add(tmp);
			}
		}

		// on parcourt la liste la liste des banques à retirer de
		// l'association
		for (int i = 0; i < banquesToRemove.size(); i++) {
			Banque bank = banqueDao.mergeObject(banquesToRemove.get(i));
			// on retire la banque de chaque coté de l'association
			rec.getBanques().remove(bank);
			bank.getRecherches().remove(rec);

			log.debug("Suppression de l'association entre la banque : "
					+ bank.toString() + " et la recherche : " + rec.toString());
		}

		// on parcourt la nouvelle liste de banques
		for (int i = 0; i < banques.size(); i++) {
			// si une banque n'était pas associée à la recherche
			if (!rec.getBanques().contains(banques.get(i))) {
				// on ajoute la banque des deux cotés de l'association
				rec.getBanques().add(banqueDao.mergeObject(banques.get(i)));
				banqueDao.mergeObject(banques.get(i)).getRecherches().add(rec);

				log.debug("Ajout de l'association entre la recherche : "
						+ rec.toString() + " et la banque : "
						+ banques.get(i).toString());
			}
		}
	}

	@Override
	public List<Recherche> findByBanqueManager(Banque banque) {
		if (banque != null && banque.getBanqueId() != null) {
			return rechercheDao.findByBanqueId(banque.getBanqueId());
		} else {
			return new ArrayList<Recherche>();
		}
	}

	@Override
	public List<Recherche> findByBanqueInLIstManager(List<Banque> banques) {
		List<Integer> bksIds = new ArrayList<Integer>();
		if (banques != null) {
			for (int i = 0; i < banques.size(); i++) {
				if (banques.get(i).getBanqueId() != null) {
					bksIds.add(banques.get(i).getBanqueId());
				}
			}
		}
		
		if (bksIds.size() > 0) {
			return rechercheDao.findByBanqueIdinList(bksIds);
		} else {
			return new ArrayList<Recherche>();
		}
	}

}
