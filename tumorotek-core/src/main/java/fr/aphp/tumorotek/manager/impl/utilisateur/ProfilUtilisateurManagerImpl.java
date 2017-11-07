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
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.dao.utilisateur.ProfilUtilisateurDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.utilisateur.ProfilUtilisateurManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateurPK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Implémentation du manager du bean de domaine ProfilUtilisateur.
 * Interface créée le 19/05/2010.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ProfilUtilisateurManagerImpl implements ProfilUtilisateurManager {
	
	private Log log = LogFactory.getLog(ProfilUtilisateurManager.class);
	
	/** Bean Dao. */
	private ProfilUtilisateurDao profilUtilisateurDao;
	/** Bean Dao. */
	private UtilisateurDao utilisateurDao;
	/** Bean Dao. */
	private BanqueDao banqueDao;
	/** Bean Dao. */
	private ProfilDao profilDao;

	public void setProfilUtilisateurDao(ProfilUtilisateurDao pDao) {
		this.profilUtilisateurDao = pDao;
	}

	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setProfilDao(ProfilDao pDao) {
		this.profilDao = pDao;
	}
	
	@Override
	public ProfilUtilisateur findByIdManager(ProfilUtilisateurPK pk) {
		return profilUtilisateurDao.findById(pk);
	}

	@Override
	public List<ProfilUtilisateur> findAllObjectsManager() {
		log.debug("Recherche de tous les ProfilUtilisateurs");
		return profilUtilisateurDao.findAll();
	}

	@Override
	public List<ProfilUtilisateur> findByExcludedPKManager(
			ProfilUtilisateurPK pk) {
		if (pk != null) {
			return profilUtilisateurDao.findByExcludedPK(pk);
		} else {
			return profilUtilisateurDao.findAll();
		}
	}

	@Override
	public List<ProfilUtilisateur> findByProfilManager(Profil profil, Boolean archive) {
		if (archive != null) {
			return profilUtilisateurDao.findByProfil(profil, archive);
		} else {
			List<ProfilUtilisateur> pU = profilUtilisateurDao.findByProfil(profil, false);
			pU.addAll(profilUtilisateurDao.findByProfil(profil, true));
			Collections.sort(pU);
			return pU;
		}
	}
	
	@Override
	public List<ProfilUtilisateur> findByBanqueManager(Banque banque, Boolean archive) {
		if (archive != null) {
			return profilUtilisateurDao.findByBanque(banque, archive);
		} else {
			List<ProfilUtilisateur> pU = profilUtilisateurDao.findByBanque(banque, false);
			pU.addAll(profilUtilisateurDao.findByBanque(banque, true));
			Collections.sort(pU);
			return pU;
		}
		
	}
	
	@Override
	public List<ProfilUtilisateur> findByUtilisateurManager(
			Utilisateur utilisateur, Boolean archive) {
		if (archive != null) {
			return profilUtilisateurDao.findByUtilisateur(utilisateur, archive);
		} else {
			List<ProfilUtilisateur> pU = profilUtilisateurDao.findByUtilisateur(utilisateur, false);
			pU.addAll(profilUtilisateurDao.findByUtilisateur(utilisateur, true));
			Collections.sort(pU);
			return pU;
		}
	}

	@Override
	public List<ProfilUtilisateur> findByBanqueProfilManager(Banque banque,
			Profil profil) {
		if (profil != null && banque != null) {
			return profilUtilisateurDao.findByBanqueProfil(banque, profil);
		} else {
			return new ArrayList<ProfilUtilisateur>();
		}
	}

	@Override
	public List<ProfilUtilisateur> findByUtilisateurBanqueManager(
			Utilisateur utilisateur, Banque banque) {
		if (utilisateur != null && banque != null) {
			return profilUtilisateurDao.findByUtilisateurBanque(
					utilisateur, banque);
		} else {
			return new ArrayList<ProfilUtilisateur>();
		}
	}

	@Override
	public List<ProfilUtilisateur> findByUtilisateurProfilManager(
			Utilisateur utilisateur, Profil profil) {
		if (profil != null && profil != null) {
			return profilUtilisateurDao.findByUtilisateurProfil(
					
					utilisateur, profil);
		} else {
			return new ArrayList<ProfilUtilisateur>();
		}
	}

	@Override
	public Boolean findDoublonManager(Utilisateur utilisateur, Banque banque,
			Profil profil) {
		
		ProfilUtilisateurPK pk = new ProfilUtilisateurPK(
				profil, utilisateur, banque);
		
		return (profilUtilisateurDao.findById(pk) != null);
	}

	@Override
	public void validateObjectManager(Utilisateur utilisateur, Banque banque,
			Profil profil) {
		
		//profil required
		if (profil == null) {
			log.warn("Objet obligatoire Profil manquant"
				+ " lors de la validation d'un ProfilUtilisateur");
			throw new RequiredObjectIsNullException(
					"ProfilUtilisateur", "creation", "Profil");
		}
		
		//utilisateur required
		if (utilisateur == null) {
			log.warn("Objet obligatoire Utilisateur manquant"
				+ " lors de la validation d'un ProfilUtilisateur");
			throw new RequiredObjectIsNullException(
					"ProfilUtilisateur", "creation", "Utilisateur");
		}
		
		//OperationType required
		if (banque == null) {
			log.warn("Objet obligatoire Banque manquant"
				+ " lors de la validation d'un ProfilUtilisateur");
			throw new RequiredObjectIsNullException(
					"ProfilUtilisateur", "creation", "Banque");
		}
		
		//Doublon
		if (utilisateur.getUtilisateurId() != null) {
			if (findDoublonManager(utilisateur, banque, profil)) {
				
				log.warn("Doublon lors validation objet ProfilUtilisateur");
				throw new DoublonFoundException(
						"ProfilUtilisateur", "creation");
			}
		}
		
	}

	@Override
	public void createObjectManager(ProfilUtilisateur profilUtilisateur,
			Utilisateur utilisateur, Banque banque, Profil profil) {
		
		// validation de l'objet à créer
		validateObjectManager(utilisateur, banque, profil);
		
		profilUtilisateur.setProfil(profilDao.mergeObject(profil));
		profilUtilisateur.setUtilisateur(
				utilisateurDao.mergeObject(utilisateur));
		profilUtilisateur.setBanque(banqueDao.mergeObject(banque));
		
		// création
		profilUtilisateurDao.createObject(profilUtilisateur);
		
		log.info("Enregistrement objet ProfilUtilisateur " 
				+ profilUtilisateur.toString());
		
	}

	@Override
	public void removeObjectManager(ProfilUtilisateur profilUtilisateur) {
		if (profilUtilisateur != null) {
			profilUtilisateurDao.removeObject(
					profilUtilisateur.getPk());
			log.info("Suppression de l'objet ProfilUtilisateur : " 
					+ profilUtilisateur.toString());				
		} else {
			log.warn("Suppression d'un ProfilUtilisateur null");
		}
	}
}
