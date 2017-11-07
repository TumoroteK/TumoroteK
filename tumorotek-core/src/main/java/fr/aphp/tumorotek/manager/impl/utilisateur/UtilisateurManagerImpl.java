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
package fr.aphp.tumorotek.manager.impl.utilisateur;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.utilisateur.UtilisateurValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class UtilisateurManagerImpl implements UtilisateurManager {
	
	private Log log = LogFactory.getLog(UtilisateurManager.class);
	
	private UtilisateurDao utilisateurDao;
	private ProfilUtilisateurManager profilUtilisateurManager;
	private UtilisateurValidator utilisateurValidator;
	private PlateformeDao plateformeDao;
	private PlateformeManager plateformeManager;
	private CollaborateurDao collaborateurDao;
	private OperationTypeDao operationTypeDao;
	private OperationManager operationManager;
	private BanqueDao banqueDao;
	private BanqueManager banqueManager;

	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}

	public void setProfilUtilisateurManager(
			ProfilUtilisateurManager pManager) {
		this.profilUtilisateurManager = pManager;
	}

	public void setUtilisateurValidator(UtilisateurValidator uValidator) {
		this.utilisateurValidator = uValidator;
	}

	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	public void setPlateformeManager(PlateformeManager pManager) {
		this.plateformeManager = pManager;
	}

	public void setOperationTypeDao(OperationTypeDao oDao) {
		this.operationTypeDao = oDao;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setBanqueManager(BanqueManager bManager) {
		this.banqueManager = bManager;
	}

	@Override
	public Utilisateur findByIdManager(Integer utilisateurId) {
		return utilisateurDao.findById(utilisateurId);
	}
	
	@Override
	public List<Utilisateur> findByLoginManager(String login) {
		if (login != null) {
			return utilisateurDao.findByLogin(login);
		} else {
			return new ArrayList<Utilisateur>();
		}
	}
	
	@Override
	public List<Utilisateur> findByLoginAndArchiveManager(String login,
			boolean archive, List<Plateforme> pfs) {
		if (login != null) {
			return utilisateurDao.findByLoginAndArchive(login, archive, pfs);
		} else {
			return new ArrayList<Utilisateur>();
		}
	}
	
	@Override
	public List<Utilisateur> findByArchiveManager(boolean archive, 
												List<Plateforme> pfs) {
		return utilisateurDao.findByOrderWithArchive(archive, pfs);
	}
	
	/**
	 * Recherche tous les Utilisateur présents dans la base.
	 * @return Liste de Utilisateur.
	 */
	@Override
	public List<Utilisateur> findAllObjectsManager() {
		return utilisateurDao.findByOrder();
	}
	
	@Override
	public Set<Plateforme> getPlateformesManager(Utilisateur utilisateur) {
		if (utilisateur != null) {
			utilisateur = utilisateurDao.mergeObject(utilisateur);
			Set<Plateforme> pfs = utilisateur.getPlateformes();
			pfs.size();
			
			return pfs;
		} else {
			return new HashSet<Plateforme>();
		}
	}

	@Override
	public Boolean findDoublonManager(Utilisateur utilisateur) {
		if (utilisateur != null) {
			if (utilisateur.getUtilisateurId() == null) {
				return utilisateurDao.findAll().contains(utilisateur);
			} else {
				return utilisateurDao.findByExcludedId(
					utilisateur.getUtilisateurId()).contains(utilisateur);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean isUsedObjectManager(Utilisateur utilisateur) {
		if (utilisateur != null && utilisateur.getUtilisateurId() != null) {
			utilisateur = utilisateurDao.mergeObject(utilisateur);
			return (operationManager
					.findByUtilisateurManager(
						utilisateur).size() > 0);
		} else {
			return false;
		}
	}

	@Override
	public void createObjectManager(Utilisateur utilisateur,
			Collaborateur collaborateur,
			List<ProfilUtilisateur> profils, 
			List<Plateforme> plateformes,
			Utilisateur admin, 
			Plateforme origine) {
		
		//Doublon
		if (!findDoublonManager(utilisateur)) {
			utilisateur.setCollaborateur(collaborateurDao
						.mergeObject(collaborateur));
			utilisateur.setPlateformeOrig(origine);
			
			// validation due l'utilisateur
			BeanValidator.validateObject(
					utilisateur, new Validator[]{utilisateurValidator});
			
			// validation des profils
			if (profils != null) {
				for (int i = 0; i < profils.size(); i++) {
					ProfilUtilisateur obj = profils.get(i);
					
					profilUtilisateurManager.validateObjectManager(
							utilisateur, 
							obj.getBanque(), 
							obj.getProfil());
				}
			}
			
			utilisateurDao.createObject(utilisateur);
			log.info("Enregistrement objet Utilisateur " 
					+ utilisateur.toString());
			
			//Enregistrement de l'operation associee
			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, admin,
							operationTypeDao.findByNom("Creation").get(0), 
														utilisateur);
			
			// enregistrements des profils
			updateProfilsAndPlateformes(utilisateur, profils, 
					profils, plateformes);
				
		} else {
			log.warn("Doublon lors creation objet Utilisateur "
					+ utilisateur.toString());
			throw new DoublonFoundException("Utilisateur", "creation");
		}
		
	}
	
	@Override
	public void updateObjectManager(Utilisateur utilisateur,
			Collaborateur collaborateur, 
			List<ProfilUtilisateur> profils,
			List<Plateforme> plateformes,
			Utilisateur admin,
			OperationType oType) {
		
		List<ProfilUtilisateur> profilsToCreate = 
			new ArrayList<ProfilUtilisateur>();
		//Doublon
		if (!findDoublonManager(utilisateur)) {
			utilisateur.setCollaborateur(collaborateurDao
						.mergeObject(collaborateur));
			
			// validation due l'utilisateur
			BeanValidator.validateObject(
					utilisateur, new Validator[]{utilisateurValidator});
			
			// validation des profils
			if (profils != null) {
				for (int i = 0; i < profils.size(); i++) {
					ProfilUtilisateur obj = profils.get(i);
					
					if (profilUtilisateurManager.findByIdManager(
							obj.getPk()) == null) {
						profilUtilisateurManager.validateObjectManager(
								utilisateur, 
								obj.getBanque(), 
								obj.getProfil());
						profilsToCreate.add(obj);
					}
				}
			}
			
			utilisateurDao.updateObject(utilisateur);
			log.info("Enregistrement objet Utilisateur " 
					+ utilisateur.toString());
			
			//Enregistrement de l'operation associee
			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, admin,
							oType, utilisateur);
			
			// enregistrements des profils
			updateProfilsAndPlateformes(utilisateur, profils, 
					profilsToCreate, plateformes);
				
		} else {
			log.warn("Doublon lors modification objet Utilisateur "
					+ utilisateur.toString());
			throw new DoublonFoundException("Utilisateur", "modification");
		}
	}

	/**
	 * Cette méthode met à jour les associations entre un utilisateur,
	 * une liste de profils et une liste de plateformes.
	 * @param utilisateur Utilisateur pour lequel on veut mettre à jour
	 * les associations.
	 * @param profils Liste de tous les profils de l'utilisateur : ceux 
	 * déjà existant et ceux a creer.
	 * @param profilsToCreate Liste des profils a creer.
	 * @param plateformes Liste des plateformes que l'on veut associer a 
	 * l'utilisateur.
	 */
	public void updateProfilsAndPlateformes(Utilisateur utilisateur, 
			List<ProfilUtilisateur> profils,
			List<ProfilUtilisateur> profilsToCreate,
			List<Plateforme> plateformes) {
		Utilisateur util = utilisateurDao.mergeObject(utilisateur);
		
		List<ProfilUtilisateur> oldProfils = new ArrayList<ProfilUtilisateur>();
		List<ProfilUtilisateur> profilsToRemove = 
			new ArrayList<ProfilUtilisateur>();
		if (profils != null) {
			oldProfils = profilUtilisateurManager
				.findByUtilisateurManager(util, null);
			for (int i = 0; i < oldProfils.size(); i++) {
				if (!profils.contains(oldProfils.get(i))) {
					profilsToRemove.add(oldProfils.get(i));
				}
			}
			
			for (int i = 0; i < profilsToRemove.size(); i++) {
				profilUtilisateurManager.removeObjectManager(
						profilsToRemove.get(i));
			}
			
			if (profilsToCreate != null) {
				// enregistrements des profilsutilisateurs
				for (int i = 0; i < profilsToCreate.size(); i++) {
					ProfilUtilisateur obj = profilsToCreate.get(i);
					profilUtilisateurManager.createObjectManager(
							obj, 
							utilisateur, 
							obj.getBanque(), 
							obj.getProfil());
				}
			}
		}
		
		if (plateformes != null) {
			Iterator<Plateforme> it = util.getPlateformes().iterator();
			List<Plateforme> pfsToRemove = new ArrayList<Plateforme>();
			// on parcourt les Plateformes qui sont actuellement associés
			// a l'utilisateur
			while (it.hasNext()) {
				Plateforme tmp = it.next();
				// si une Plateforme n'est pas dans la nouvelle liste, on
				// la conserve afin de la retirer par la suite
				if (!plateformes.contains(tmp)) {
					pfsToRemove.add(tmp);
				}
			}
			
			// on parcourt la liste des Plateformes à retirer de
			// l'association
			for (int i = 0; i < pfsToRemove.size(); i++) {
				Plateforme pf = plateformeDao.mergeObject(pfsToRemove.get(i));
				// on retire la pf de chaque coté de l'association
				util.getPlateformes().remove(pf);
				pf.getUtilisateurs().remove(util);
				
				log.debug("Suppression de l'association entre " 
						+ "l'utilisateur : " 
						+ util.toString() + " et la plateforme : "
						+ pf.toString());
			}
			
			// on parcourt la nouvelle liste de plateformes
			for (int i = 0; i < plateformes.size(); i++) {
				// si une plateforme n'était pas associé a l'utilisateur
				if (!util.getPlateformes().contains(plateformes.get(i))) {
					// on ajoute la plateforme des deux cotés de l'association
					util.getPlateformes().add(plateformeDao.mergeObject(
							plateformes.get(i)));
					plateformeDao.mergeObject(
							plateformes.get(i)).getUtilisateurs().add(util);
					
					log.debug("Ajout de l'association entre " 
							+ "l'utilisateur : " 
							+ util.toString() + " et la plateforme : "
							+ plateformes.get(i).toString());
				}
			}
		}
	}
	
	@Override
	public Utilisateur updatePasswordManager(Utilisateur utilisateur, 
			String newPwd, Integer nbMois,
			Utilisateur admin) {
		if (utilisateur != null && newPwd != null
				&& admin != null
				&& !newPwd.equals(utilisateur.getPassword())) {
			utilisateur.setPassword(newPwd);
			
			// augmentation du timeout s'il n'était pas null
			if (utilisateur.getTimeOut() != null
					&& nbMois != null
					&& nbMois > 0) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, nbMois);
				utilisateur.setTimeOut(cal.getTime());
			}
			
			utilisateurDao.mergeObject(utilisateur);
			// si jamais l'utilisateur réalisant la modif
			// correspondant à celui dont on change le mdp
			// on met à jour l'admin pour que les modifs ne
			// soient pas écrasées lors de la création
			// de l'opération
			if (admin.equals(utilisateur)) {
				admin = utilisateur;
			}
			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, admin,
			operationTypeDao.findByNom("Modification").get(0), 
				utilisateur);
		}
		
		return utilisateur;
	}
	
	@Override
	public void archiveUtilisateurManager(Utilisateur utilisateur,
			Utilisateur admin) {
		if (utilisateur != null && admin != null) {
			utilisateur.setArchive(true);
			utilisateurDao.mergeObject(utilisateur);
			
			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, admin,
			operationTypeDao.findByNom("Archivage").get(0), 
				utilisateur);
		}
	}

	@Override
	public void removeObjectManager(Utilisateur utilisateur) {
		if (utilisateur != null) {
			if (isUsedObjectManager(utilisateur)) {
				log.warn("Objet utilisé lors de la suppression de l'objet " 
						+ "Utilisateur : " 
						+ utilisateur.toString());
				throw new 
					ObjectUsedException("utilisateur.suppression.impossible", 
																	false);
			} else {
				// utilisateur = utilisateurDao.mergeObject(utilisateur);
				// suppression des ProfilsUtilisateur
				List<ProfilUtilisateur> objets = profilUtilisateurManager
					.findByUtilisateurManager(utilisateur, null);
				for (int i = 0; i < objets.size(); i++) {
					profilUtilisateurManager.removeObjectManager(objets.get(i));
				}
				//remove cascade codes
//				Iterator<CodeSelect> codeSelIt = 
//									utilisateur.getCodeSelects().iterator();
//				while (codeSelIt.hasNext()) {
//					codeSelectManager.removeObjectManager(codeSelIt.next());
//				}
//				Iterator<CodeUtilisateur> codeUIt = 
//								utilisateur.getCodeUtilisateurs().iterator();
//				while (codeUIt.hasNext()) {
//					codeUtilisateurManager.removeObjectManager(codeUIt.next());
//				}
				
				utilisateurDao.removeObject(utilisateur.getUtilisateurId());
				log.info("Suppression de l'objet Utilisateur : " 
						+ utilisateur.toString());
				
				//Supprime operations associes
				List<Operation> ops = operationManager
									.findByObjectManager(utilisateur);
				for (int i = 0; i < ops.size(); i++) {
					operationManager.removeObjectManager(ops.get(i));
				}
			}
		} else {
			log.warn("Suppression d'un Utilisateur null");
		}
	}

	@Override
	public List<Banque> getAvailableBanquesAsAdminManager(
			Utilisateur utilisateur) {
		if (utilisateur != null) {
			List<Banque> availables = new ArrayList<Banque>();
			
			if (utilisateur.isSuperAdmin()) {
				for (Banque banque : banqueManager.findAllObjectsManager()) {
					if (!banque.getArchive()) {
						availables.add(banque);
					}
				}	
			} else {
				// on parcourt tous les profils de l'utilisateur pour avoir
				// ses banques
				// @since 2.1 retire bank archive
				List<ProfilUtilisateur> profils = profilUtilisateurManager
					.findByUtilisateurManager(utilisateur, false);
				
				
				for (int i = 0; i < profils.size(); i++) {
					if (profils.get(i).getProfil().getAdmin()) {
						if (!availables.contains(profils.get(i).getBanque())) {
							availables.add(profils.get(i).getBanque());
						}
					}
				}
				
				// on parcourt les pfs dont l'utilisateur est l'admin
				Set<Plateforme> pfs = getPlateformesManager(utilisateur);
				Iterator<Plateforme> it = pfs.iterator();
				while (it.hasNext()) {
					// pour chaque pf on récupère ses banques
					Set<Banque> bks = plateformeManager
						.getBanquesManager(it.next());
					Iterator<Banque> it2 = bks.iterator();
					
					// @since 2.1 retire bank archive
					while (it2.hasNext()) {
						Banque bk = it2.next();
						if (!availables.contains(bk) && !bk.getArchive()) {
							availables.add(bk);
						}
					}
				}
			}
			
			return availables;
		} else {
			return new ArrayList<Banque>();
		}
	}

	@Override
	public List<Banque> getAvailableBanquesManager(Utilisateur utilisateur) {
		if (utilisateur != null) {
			List<Banque> availables = new ArrayList<Banque>();
			
			if (utilisateur.isSuperAdmin()) {
				for (Banque banque : banqueManager.findAllObjectsManager()) {
					if (!banque.getArchive()) {
						availables.add(banque);
					}
				}				
			} else {
				// on parcourt les pfs dont l'utilisateur est l'admin
				Set<Plateforme> pfs = getPlateformesManager(utilisateur);
				Iterator<Plateforme> it = pfs.iterator();
				while (it.hasNext()) {
					// pour chaque pf on récupère ses banques
					Set<Banque> bks = plateformeManager
						.getBanquesManager(it.next());
					Iterator<Banque> it2 = bks.iterator();
					
					// @since 2.1 retire bank archive
					while (it2.hasNext()) {
						Banque bk = it2.next();
						if (!availables.contains(bk) && !bk.getArchive()) {
							availables.add(bk);
						}
					}
				}
				
				// on parcourt tous les profils de l'utilisateur pour avoir
				// ses banques
				// @since 2.1 retire bank archive
				List<ProfilUtilisateur> profils = profilUtilisateurManager
					.findByUtilisateurManager(utilisateur, false);
				
				
				for (int i = 0; i < profils.size(); i++) {
					if (!availables.contains(profils.get(i).getBanque())) {
						availables.add(profils.get(i).getBanque());
					}
				}
			}
			
			return availables;
		} else {
			return new ArrayList<Banque>();
		}
	}
	
	@Override
	public List<Plateforme> getAvailablePlateformesManager(
			Utilisateur utilisateur) {
		List<Plateforme> plateformes = new ArrayList<Plateforme>();
		
		if (utilisateur != null) {
			// @since 2.1
			if (utilisateur.isSuperAdmin()) {
				plateformes.addAll(plateformeManager
									.findAllObjectsManager());
			} else {
				// on parcourt les pfs dont l'utilisateur est l'admin
				Set<Plateforme> pfs = getPlateformesManager(utilisateur);
				Iterator<Plateforme> it = pfs.iterator();
				while (it.hasNext()) {
					// pour chaque pf
					plateformes.add(it.next());
				}
				
				// since 2.1 banque ne doit pas être archivée
				List<ProfilUtilisateur> profils = profilUtilisateurManager
					.findByUtilisateurManager(utilisateur, false);
				// pour chaque profil
				for (int i = 0; i < profils.size(); i++) {
					// si la plateforme de la banque accessible via
					// ce profil n'est pas ds la liste 
					if (!plateformes.contains(profils.get(i)
							.getBanque().getPlateforme())) {
						plateformes.add(profils.get(i)
							.getBanque().getPlateforme());
					}
				}
			}
		}
		
		Collections.sort(plateformes);
		
		return plateformes;
	}

	@Override
	public List<Banque> getAvailableBanquesByPlateformeManager(
			Utilisateur utilisateur, Plateforme plateforme) {
		List<Banque> banques = new ArrayList<Banque>();
		
		if (utilisateur != null && plateforme != null) {
			// on récupère les banques pour lesquelles le user
			// a des droits d'accès
			List<Banque> bksAccessibles = 
				getAvailableBanquesManager(utilisateur);
			// on récupère les banques de la pf
			// archive = false since 2.1
			List<Banque> bksDePf = banqueDao
					.findByPlateformeAndArchive(plateforme, false);
			
			// on ne conserve que les banques de la PF pour
			// lesquelles le user a des droits
			for (int i = 0; i < bksAccessibles.size(); i++) {
				if (bksDePf.contains(bksAccessibles.get(i))) {
					banques.add(bksAccessibles.get(i));
				}
			}
		}
		
		return banques;
	}

	@Override
	public List<Utilisateur> findByLoginPasswordAndArchiveManager(String login,
			String pass, boolean archive) {
		
		if (login != null && pass != null) {
			return utilisateurDao.findByLoginPassAndArchive(
					login, pass, archive);
		} else {
			return new ArrayList<Utilisateur>();
		}
	}
	
	@Override
	public void archiveScheduledUtilisateursManager(Utilisateur admin) {
		if (admin != null) {
			List<Utilisateur> usersToArchive = utilisateurDao
				.findByTimeOutBefore(Calendar.getInstance().getTime());
			for (Utilisateur usr : usersToArchive) {
				archiveUtilisateurManager(usr, admin);
				log.info("Archivage automatisé par timeout de l'utilisateur: "
						+ usr.getLogin());
			}
		}
	}
}
