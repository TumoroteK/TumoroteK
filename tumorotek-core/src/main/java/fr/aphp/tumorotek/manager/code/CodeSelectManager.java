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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.CodeSelectDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeSelect. Interface créée le
 * 02/07/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@Service
public class CodeSelectManager {

	private final Log log = LogFactory.getLog(CodeSelectManager.class);

	@Autowired
	private CodeSelectDao codeSelectDao;

	@Autowired
	private CodeDossierDao codeDossierDao;

	@Autowired
	private OperationManager operationManager;

	@Autowired
	private OperationTypeDao operationTypeDao;

	@Autowired
	private BanqueDao banqueDao;

	@Autowired
	private UtilisateurDao utilisateurDao;

	@Autowired
	private CommonUtilsManager commonUtilsManager;

	/**
	 * Recherche toutes les instances de codes favoris.
	 * 
	 * @return List contenant les codes.
	 */
	public List<CodeSelect> findAllObjectsManager() {
		return IterableUtils.toList(codeSelectDao.findAll());
	}

	/**
	 * Recherche les codes référencés par les CodeSelects pour utilisateur et la
	 * banque passés en paramètres et dont le code est like celui passé en
	 * paramètre.
	 * 
	 * @param code
	 * @param exactMatch
	 * @param l'utilisateur pour lequel on recherche des CodeSelects.
	 * @param la            banque pour laquelle on recherche des CodeSelects.
	 * @return une liste de CodeCommon.
	 */
	public List<CodeCommon> findByCodeOrLibelleLikeManager(String codeOrLibelle, final boolean exactMatch,
			final Utilisateur u, final Banque b) {

		if (!exactMatch) {
			codeOrLibelle = ".*" + codeOrLibelle + ".*";
		}

		final List<CodeSelect> codeUserBanque = new ArrayList<>();
		if (u == null) {
			codeUserBanque.addAll(codeSelectDao.findByBanque(b));
		} else {
			codeUserBanque.addAll(findByUtilisateurAndBanqueManager(u, b));
		}

		return extractCodeCommonFromCodeSelect(codeUserBanque, codeOrLibelle);
	}

	/**
	 * Recherche les codes du Dossier passé en paramètre.
	 * 
	 * @param dossier dont on veut les codes contenus.
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeSelect> findByCodeDossierManager(final CodeDossier parent) {
		return codeSelectDao.findByCodeDossier(parent);
	}

	/**
	 * Recherche les codes référencés par les codes favoris du Dossier passé en
	 * paramètre.
	 * 
	 * @param dossier dont on veut les codes référencés contenus.
	 * @return une liste de codes Common.
	 */
	public List<CodeCommon> findCodesFromSelectByDossierManager(final CodeDossier parent) {
		return extractCodeCommonFromCodeSelect(codeSelectDao.findByCodeDossier(parent), null);
	}

	/**
	 * Recherche les codes referencés par les codes favoris qui sont non contenus
	 * dans un dossier pour un utilisateur et une banque donnée.
	 * 
	 * @param utilisateur
	 * @param banque
	 * @return une liste de codes CodeCommon.
	 */
	public List<CodeCommon> findByRootDossierManager(final Utilisateur u, final Banque bank) {
		final List<CodeSelect> codes = codeSelectDao.findByRootDossier(u, bank);
		return extractCodeCommonFromCodeSelect(codes, null);
	}

	/**
	 * Recherche les codes pour un utilisateur et une banque.
	 * 
	 * @param utilisateur Utilisateur
	 * @param banque      Banque
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeSelect> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b) {
		return codeSelectDao.findByUtilisateurAndBanque(u, b);
	}

	/**
	 * Recherche les codes référencés par les favoris pour un utilisateur et une
	 * banque.
	 * 
	 * @param utilisateur Utilisateur
	 * @param banque      Banque
	 * @return une liste de codes CodeSelect.
	 */
	public List<CodeCommon> findCodesFromSelectByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b) {
		return extractCodeCommonFromCodeSelect(codeSelectDao.findByUtilisateurAndBanque(u, b), null);
	}

	/**
	 * Cherche les doublons en se basant sur la methode equals() surchargee par les
	 * entites. Si l'objet est modifie donc a un id attribue par le SGBD, ce dernier
	 * est retire de la liste findAll.
	 * 
	 * @param table CodeUtilisateur dont on cherche la presence dans la base
	 * @return true/false
	 */
	public boolean findDoublonManager(final CodeSelect code) {
		if (code.getCodeSelectId() == null) {
			return IterableUtils.toList(codeSelectDao.findAll()).contains(code);
		}
		return codeSelectDao.findByExcludedId(code.getCodeSelectId()).contains(code);
	}

	/**
	 * Enregsitre ou modifie un code favori. La modification ne peut porter que sur
	 * le dossier parent (cad deplacement).
	 * 
	 * @param code
	 * @param dos
	 * @param banque
	 * @param utilisateur
	 * @param String      operation creation/modification
	 */
	public void createOrUpdateManager(final CodeSelect code, final CodeDossier dos, final Banque bank,
			final Utilisateur utilisateur, final String operation) {
		if (operation == null) {
			throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
		}

		// Validation
		checkRequiredObjectsAndValidate(code, bank, utilisateur, operation);

		// merge non required
		if (dos != null) {
			code.setCodeDossier(codeDossierDao.save(dos));
		}

		// Doublon
		if (!findDoublonManager(code)) {
			if ((operation.equals("creation") || operation.equals("modification"))) {
				if (operation.equals("creation")) {
					codeSelectDao.save(code);
					log.info("Enregistrement objet CodeSelect " + code.toString());
					CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
							operationTypeDao.findByNom("Creation").get(0), code.getUtilisateur());
				} else {
					codeSelectDao.save(code);
					log.info("Modification objet CodeSelect " + code.toString());
					CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
							operationTypeDao.findByNom("Modification").get(0), code.getUtilisateur());
				}
			} else {
				throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet CodeSelect " + code.toString());
			throw new DoublonFoundException("CodeSelect", operation);
		}
	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes sont non nulls.
	 * 
	 * @param code        CodeSelect
	 * @param bank
	 * @param utilisateur
	 * @param operation   demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(final CodeSelect code, final Banque bank,
			final Utilisateur utilisateur, final String operation) {
		// Banque required
		if (bank != null) {
			// merge banque object
			code.setBanque(banqueDao.save(bank));
		} else if (code.getBanque() == null) {
			log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " du code favori");
			throw new RequiredObjectIsNullException("CodeSelect", operation, "Banque");
		}

		// Utilisateur required
		if (utilisateur != null) {
			// merge utilisateur object
			code.setUtilisateur(utilisateurDao.save(utilisateur));
		} else if (code.getUtilisateur() == null) {
			log.warn("Objet obligatoire Utilisateur manquant" + " lors de la " + operation + " du code favori");
			throw new RequiredObjectIsNullException("CodeSelect", operation, "Utilisateur");
		}
	}

	/**
	 * Supprime un code favori de la base de données.
	 * 
	 * @param code CodeUtilisateur à supprimer de la base de données.
	 */
	public void removeObjectManager(final CodeSelect code) {
		if (code != null) {
			codeSelectDao.deleteById(code.getCodeSelectId());
			log.info("Suppression objet CodeSelect " + code.toString());
			// Supprime operations associes
			CreateOrUpdateUtilities.removeAssociateOperations(code, operationManager);
		} else {
			log.warn("Suppression d'un CodeUtilisateur null");
		}
	}

	/**
	 * Extrait les codes (CodeCommon) pour affichage a partir d'une liste de code
	 * favoris. Filtre eventuellement sur le code et libelle. Embarque une back
	 * reference vers l'objet code select.
	 * 
	 * @param codes
	 * @param codeOrLibelle
	 * @return liste de CodeCommon pour affichage
	 */
	private List<CodeCommon> extractCodeCommonFromCodeSelect(final List<CodeSelect> codes, final String codeOrLibelle) {
		final List<CodeCommon> res = new ArrayList<>();

		final Iterator<CodeSelect> it = codes.iterator();
		CodeSelect next;
		CodeCommon ref;
		while (it.hasNext()) {
			next = it.next();
			ref = commonUtilsManager.findCodeByTableCodageAndIdManager(next.getCodeId(), next.getTableCodage());

			if (ref != null) {
				ref.setCodeSelect(next);
				if (codeOrLibelle != null) {
					if (ref.getCode().matches(codeOrLibelle) || ref.getLibelle().matches(codeOrLibelle)) {
						res.add(ref);
					}
				} else {
					res.add(ref);
				}
			}
		}
		return res;
	}

	/**
	 * Recherche tous les codes favoris définis pour une banque à la racine de
	 * l'arborescence.
	 * 
	 * @param banque Banque
	 * @return une liste de CodeSelects.
	 */
	public List<CodeCommon> findByRootDossierAndBanqueManager(final Banque bank) {
		return extractCodeCommonFromCodeSelect(codeSelectDao.findByRootDossierAndBanque(bank), null);
	}
}
