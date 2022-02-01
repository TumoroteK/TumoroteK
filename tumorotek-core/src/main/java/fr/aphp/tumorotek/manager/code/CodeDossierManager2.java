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
package fr.aphp.tumorotek.manager.code;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeDossierValidator;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeDossier. Interface créée le
 * 06/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Service
public class CodeDossierManager2 {

	private final Log log = LogFactory.getLog(CodeDossierManager2.class);

	@Autowired
	private CodeDossierDao codeDossierDao;

	@Autowired
	private CodeUtilisateurManager codeUtilisateurManager;

	@Autowired
	private OperationManager operationManager;

	@Autowired
	private OperationTypeDao operationTypeDao;

	@Autowired
	private BanqueDao banqueDao;

	@Autowired
	private UtilisateurDao utilisateurDao;

	@Autowired
	private CodeDossierValidator codeDossierValidator;

	@Autowired
	private CodeSelectManager codeSelectManager;

	/**
	 * Recherche toutes les dossiers.
	 * 
	 * @return List contenant les dossiers.
	 */
	public List<CodeDossier> findAllCodeDossiersManager() {
		return IterableUtils.toList(codeDossierDao.findAll());
	}

	/**
	 * Recherche le dossier dont le nom est like celui passé en paramètre.
	 * 
	 * @param nom     Nom pour lequel on recherche un dossier.
	 * @param boolean exactMatch
	 * @param banque
	 * @return List contenant les codeDossiers.
	 */
	public List<CodeDossier> findByNomLikeManager(String nom, final boolean exactMatch, final Banque bank) {
		if (!exactMatch) {
			nom = "%" + nom + "%";
		}
		log.debug("Recherche CodeUtilisateur par code: " + nom + " exactMatch " + String.valueOf(exactMatch));
		return codeDossierDao.findByNomLike(nom, bank);
	}

	/**
	 * Recherche les dossier du Dossier passé en paramètre.
	 * 
	 * @param dossier dont on veut les dossiers contenus.
	 * @return une liste de codeDossiers.
	 */
	public List<CodeDossier> findByCodeDossierParentManager(final CodeDossier parent) {
		return codeDossierDao.findByCodeDossierParent(parent);
	}

	/**
	 * Recherche les dossiers de CodeUtilisateur a la racine.
	 * 
	 * @param banque
	 * @return une liste de codeDossiers.
	 */
	public List<CodeDossier> findByRootCodeDossierUtilisateurManager(final Banque bank) {
		return codeDossierDao.findByRootCodeDossierUtilisateur(bank);
	}

	/**
	 * Recherche les dossiers de CodeSelect a la racine pour un utilisateur et une
	 * banque donnée.
	 * 
	 * @param utilisateur
	 * @param banque
	 * @return une liste de codeDossiers.
	 */
	public List<CodeDossier> findByRootCodeDossierSelectManager(final Utilisateur user, final Banque bank) {
		return codeDossierDao.findByRootCodeDossierSelect(user, bank);
	}

	/**
	 * Recherche les dossiers de codes utilisateurs pour l'utilisateur et la banque
	 * passées en paramètres.
	 * 
	 * @param l'utilisateur pour lequel on recherche des dossiers.
	 * @param la            banque
	 * @return une liste de CodeDossiers.
	 */
	public List<CodeDossier> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b) {
		return codeDossierDao.findByUtilisateurAndBanque(u, b);
	}

	/**
	 * Recherche les dossiers de codes ajoutés au favoris pour l'utilisateur et la
	 * banque passées en paramètres.
	 * 
	 * @param l'utilisateur pour lequel on recherche des dossiers.
	 * @param la            banque
	 * @return une liste de CodeDossiers.
	 */
	public List<CodeDossier> findBySelectUtilisateurAndBanqueManager(final Utilisateur u, final Banque b) {
		return codeDossierDao.findBySelectUtilisateurAndBanque(u, b);
	}

	/**
	 * Cherche les doublons en se basant sur la methode equals() surchargee par les
	 * entites. Si l'objet est modifie donc a un id attribue par le SGBD, ce dernier
	 * est retire de la liste findAll.
	 * 
	 * @param table CodeUtilisateur dont on cherche la presence dans la base
	 * @return true/false
	 */
	public boolean findDoublonManager(final CodeDossier dos) {
		if (dos.getCodeDossierId() == null) {
			return IterableUtils.toList(codeDossierDao.findAll()).contains(dos);
		}
		return codeDossierDao.findByExcludedId(dos.getCodeDossierId()).contains(dos);
	}

	/**
	 * Enregsitre ou modifie un dossier.
	 * 
	 * @param dos
	 * @param dossier     parent
	 * @param banque
	 * @param utilisateur
	 * @param code        parent
	 * @param String      operation creation/modification
	 */
	public void createOrUpdateManager(final CodeDossier dos, final CodeDossier parent, final Banque bank,
			final Utilisateur utilisateur, final String operation) {

		if (operation == null) {
			throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
		}

		// Validation
		checkRequiredObjectsAndValidate(dos, bank, utilisateur, operation);

		if (parent != null) {
			dos.setDossierParent(codeDossierDao.save(parent));
		}

		// Doublon
		if (!findDoublonManager(dos)) {
			if ((operation.equals("creation") || operation.equals("modification"))) {
				if (operation.equals("creation")) {
					codeDossierDao.save(dos);
					log.debug("Enregistrement objet CodeDossier " + dos.toString());
					CreateOrUpdateUtilities.createAssociateOperation(dos, operationManager,
							operationTypeDao.findByNom("Creation").get(0), dos.getUtilisateur());
				} else {
					codeDossierDao.save(dos);
					log.debug("Modification objet CodeDossier " + dos.toString());
					CreateOrUpdateUtilities.createAssociateOperation(dos, operationManager,
							operationTypeDao.findByNom("Modification").get(0), dos.getUtilisateur());
				}
			} else {
				throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet CodeDossier " + dos.toString());
			throw new DoublonFoundException("CodeUtilisateur", operation);
		}

	}

	/**
	 * Supprime un dossier de la base de données ainsi que tous les codes et
	 * sous-dossiers dont il est parent en cascade.
	 * 
	 * @param code CodeDossier à supprimer de la base de données.
	 */
	public void removeObjectManager(final CodeDossier dos) {
		if (dos != null) {

			// supprime les codes
			if (!dos.getCodeSelect()) {
				final Iterator<CodeUtilisateur> it = codeUtilisateurManager.findByCodeDossierManager(dos).iterator();
				while (it.hasNext()) {
					codeUtilisateurManager.removeObjectManager(it.next());
				}
			} else {
				final Iterator<CodeSelect> it = codeSelectManager.findByCodeDossierManager(dos).iterator();
				while (it.hasNext()) {
					codeSelectManager.removeObjectManager(it.next());
				}
			}

			// supprime les sous-dossiers
			final Iterator<CodeDossier> itDos = findByCodeDossierParentManager(dos).iterator();
			while (itDos.hasNext()) {
				removeObjectManager(itDos.next());
			}

			codeDossierDao.deleteById(dos.getCodeDossierId());
			log.info("Suppression objet CodeDossier " + dos.toString());
			// Supprime operations associes
			CreateOrUpdateUtilities.removeAssociateOperations(dos, operationManager);
		} else {
			log.warn("Suppression d'un CodeDossier null");
		}

	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes sont non nulls et
	 * lance la validation via le Validator.
	 * 
	 * @param dos         CodeDossier
	 * @param bank
	 * @param utilisateur
	 * @param operation   demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(final CodeDossier dos, final Banque bank,
			final Utilisateur utilisateur, final String operation) {
		// Banque required
		if (bank != null) {
			// merge banque object
			dos.setBanque(banqueDao.save(bank));
		} else if (dos.getBanque() == null) {
			log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " du code dossier");
			throw new RequiredObjectIsNullException("CodeDossier", operation, "Banque");
		}

		// Utilisateur required
		if (utilisateur != null) {
			// merge utilisateur object
			dos.setUtilisateur(utilisateurDao.save(utilisateur));
		} else if (dos.getUtilisateur() == null) {
			log.warn("Objet obligatoire Utilisateur manquant" + " lors de la " + operation + " du code utilisateur");
			throw new RequiredObjectIsNullException("CodeDossier", operation, "Utilisateur");
		}

		// Validation
		BeanValidator.validateObject(dos, new Validator[] { codeDossierValidator });
	}

	/**
	 * Recherche tous les dossiers de codes favoris ou utilisateur définis pour une
	 * banque à la racine de l'arborescence.
	 * 
	 * @param banque Banque
	 * @return une liste de CodeDossier.
	 */
	public List<CodeDossier> findByRootDossierBanqueManager(final Banque bank, final Boolean select) {
		return codeDossierDao.findByRootDossierBanque(bank, select);
	}
}
