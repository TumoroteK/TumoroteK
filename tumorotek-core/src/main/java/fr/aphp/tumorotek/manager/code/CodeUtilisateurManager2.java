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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.CodeUtilisateurDao;
import fr.aphp.tumorotek.dao.code.TranscodeUtilisateurDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeCommonValidator;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.code.TranscodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le Manager du bean de domaine CodeUtilisateur. Interface créée
 * le 21/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
@Service
public class CodeUtilisateurManager2 {

	private final Log log = LogFactory.getLog(CodeUtilisateurManager2.class);

	@Autowired
	private CodeUtilisateurDao codeUtilisateurDao;

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
	private CodeCommonValidator codeCommonValidator;

	@Autowired
	private CommonUtilsManager commonUtilsManager;

	@Autowired
	private TranscodeUtilisateurDao transcodeUtilisateurDao;

	/**
	 * Recherche toutes les instances de codes présentes dans la codification.
	 * 
	 * @return List contenant les codes.
	 */
	public List<CodeUtilisateur> findAllObjectsManager() {
		return IterableUtils.toList(codeUtilisateurDao.findAll());
	}

	/**
	 * Recherche les codes dont le code est like celui passé en paramètre.
	 * 
	 * @param code    Code pour lequel on recherche des codes.
	 * @param boolean exactMatch
	 * @param liste   de banques
	 * @return Liste de codes.
	 */
	public List<CodeUtilisateur> findByCodeLikeManager(String code, final boolean exactMatch,
			final List<Banque> banks) {
		final List<CodeUtilisateur> codes = new ArrayList<>();
		if (banks != null && !banks.isEmpty()) {
			if (!exactMatch) {
				code = "%" + code + "%";
			}
			// codes.addAll(codeUtilisateurDao.findByCodeLike(code, banks));
			codes.addAll(codeUtilisateurDao.findByCodeLikeAndBanqueId(code,
					banks.stream().map(Banque::getBanqueId).collect(Collectors.toList())));
		}
		return codes;
	}

	/**
	 * Recherche les codes dont le libellé est like celui passé en paramètre.
	 * 
	 * @param libelle Description du code que l'on recherche.
	 * @param boolean exactMatch
	 * @param liste   de banque
	 * @return une liste de codes.
	 */
	public List<CodeUtilisateur> findByLibelleLikeManager(String libelle, final boolean exactMatch,
			final List<Banque> banks) {
		final List<CodeUtilisateur> codes = new ArrayList<>();
		if (banks != null && !banks.isEmpty()) {
			if (!exactMatch) {
				libelle = "%" + libelle + "%";
			}
			codes.addAll(codeUtilisateurDao.findByLibelleLike(libelle, banks));
		}
		return codes;
	}

	/**
	 * Recherche les codes du Dossier passé en paramètre.
	 * 
	 * @param dossier dont on veut les codes contenus.
	 * @return une liste de codes CodeUtilisateur.
	 */
	public List<CodeUtilisateur> findByCodeDossierManager(final CodeDossier parent) {
		return codeUtilisateurDao.findByCodeDossier(parent);
	}

	/**
	 * Recherche les codes non contenus dans un dossier.
	 * 
	 * @param banque
	 * @return une liste de codes CodeUtilisateur.
	 */
	public List<CodeUtilisateur> findByRootDossierManager(final Banque bank) {
		return codeUtilisateurDao.findByRootDossier(bank);
	}

	/**
	 * Recherche les codes heritant d'un code parent.
	 * 
	 * @param codeUtilisateur parent
	 * @return une liste de codes CodeUtilisateur.
	 */
	public List<CodeUtilisateur> findByCodeParentManager(final CodeUtilisateur parent) {
		return codeUtilisateurDao.findByCodeParent(parent);
	}

	/**
	 * Recherche les codes pour un utilisateur et une banque.
	 * 
	 * @param utilisateur Utilisateur
	 * @param banque      Banque
	 * @return une liste de codes CodeUtilisateur.
	 */
	public List<CodeUtilisateur> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b) {
		return codeUtilisateurDao.findByUtilisateurAndBanque(u, b);
	}

	/**
	 * Cherche les doublons en se basant sur la methode equals() surchargee par les
	 * entites. Si l'objet est modifie donc a un id attribue par le SGBD, ce dernier
	 * est retire de la liste findAll.
	 * 
	 * @param table CodeUtilisateur dont on cherche la presence dans la base
	 * @return true/false
	 */
	public boolean findDoublonManager(final CodeUtilisateur code) {
		if (code.getCodeUtilisateurId() == null) {
			return IterableUtils.toList(codeUtilisateurDao.findAll()).contains(code);
		}
		return codeUtilisateurDao.findByExcludedId(code.getCodeUtilisateurId()).contains(code);
	}

	/**
	 * Enregsitre ou modifie un code utilisateur.
	 * 
	 * @param code
	 * @param dos
	 * @param banque
	 * @param utilisateur
	 * @param code        parent
	 * @param transcodes
	 * @param String      operation creation/modification
	 */
	public void createOrUpdateManager(final CodeUtilisateur code, final CodeDossier dos, final Banque bank,
			final Utilisateur utilisateur, final CodeUtilisateur parent, final Set<CodeCommon> transcodes,
			final String operation) {
		if (operation == null) {
			throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
		}

		// Validation
		checkRequiredObjectsAndValidate(code, bank, utilisateur, operation);

		// merge non required
		if (dos != null) {
			code.setCodeDossier(codeDossierDao.save(dos));
		}
		if (parent != null) {
			code.setCodeParent(codeUtilisateurDao.save(parent));
		}

		// Doublon
		if (!findDoublonManager(code)) {
			if ((operation.equals("creation") || operation.equals("modification"))) {
				if (operation.equals("creation")) {
					codeUtilisateurDao.save(code);
					log.info("Enregistrement objet CodeUtilisateur " + code.toString());
					CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
							operationTypeDao.findByNom("Creation").get(0), code.getUtilisateur());
				} else {
					codeUtilisateurDao.save(code);
					log.info("Modification objet CodeUtilisateur " + code.toString());
					CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
							operationTypeDao.findByNom("Modification").get(0), code.getUtilisateur());
				}
				if (transcodes != null) {
					updateTranscodes(code, transcodes);
				}
			} else {
				throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet CodeUtilisateur " + code.toString());
			throw new DoublonFoundException("CodeUtilisateur", operation);
		}
	}

	/**
	 * Supprime un objet de la base de données ainsi que tous les codes dont il est
	 * le parent en cascade.
	 * 
	 * @param code CodeUtilisateur à supprimer de la base de données.
	 */
	public void removeObjectManager(final CodeUtilisateur code) {
		if (code != null) {
			final Iterator<CodeUtilisateur> it = getCodesUtilisateurManager(code).iterator();
			while (it.hasNext()) {
				removeObjectManager(it.next());
			}

			codeUtilisateurDao.deleteById(code.getCodeUtilisateurId());
			log.info("Suppression objet CodeUtilisateur " + code.toString());
			// Supprime operations associes
			CreateOrUpdateUtilities.removeAssociateOperations(code, operationManager);
		} else {
			log.warn("Suppression d'un CodeUtilisateur null");
		}
	}

	/**
	 * Renvoie les codes utilisateurs héritant du code parent.
	 * 
	 * @param code parent.
	 * @return
	 */
	public Set<CodeUtilisateur> getCodesUtilisateurManager(final CodeUtilisateur code) {
		Set<CodeUtilisateur> codes = new HashSet<>();
		if (code.getCodeUtilisateurId() != null) {
			final CodeUtilisateur codeM = codeUtilisateurDao.save(code);
			codes = codeM.getCodesUtilisateur();
			codes.isEmpty(); // operation empechant LazyInitialisationException
		}
		return codes;
	}

	/**
	 * Recherche les codes Adicap, Cim ou Cimo issus du transcodage du code
	 * Utilisateur passé en paramètre.
	 * 
	 * @param code   utilisateur qui sera transcodé
	 * @param tables de codage auxquelles doivent appartenir les transcodes
	 * @return Liste de codes implemantant CodeCommon
	 */
	public Set<CodeCommon> getTranscodesManager(final CodeUtilisateur code, final List<TableCodage> tables) {
		final Set<CodeCommon> codes = new HashSet<>();
		if (code != null && tables != null && !tables.isEmpty()) {
			final CodeUtilisateur cM = codeUtilisateurDao.save(code);
			final Iterator<TranscodeUtilisateur> trsIt = cM.getTranscodes().iterator();
			TranscodeUtilisateur trU;
			while (trsIt.hasNext()) {
				trU = trsIt.next();
				if (tables.contains(trU.getTableCodage())) {
					codes.add(getCodeCommonFromTransCode(trU));
				}
			}
		}
		return codes;
	}

	/**
	 * Recherche les codes utilisateurs issus du transcodage du code passé en
	 * paramètre.
	 * 
	 * @param code
	 * @param liste de banques auxquelles appartiennent codes utilisateurs.
	 * @return liste de codes utilisateurs.
	 */
	public List<CodeUtilisateur> findByTranscodageManager(final CodeCommon code, final List<Banque> banks) {
		List<CodeUtilisateur> codes = new ArrayList<>();
		if (banks != null && !banks.isEmpty()) {
			final TableCodage tab = commonUtilsManager.getTableCodageFromCodeCommonManager(code);
			codes = codeUtilisateurDao.findByTranscodage(tab, code.getCodeId(), banks);
		}
		return codes;
	}

	/**
	 * Renvoie le code utilisateur correspondant au code id passé en paramètre.
	 * 
	 * @param codeId
	 * @return codeUtilisateur
	 */
	public CodeUtilisateur findByIdManager(final Integer codeId) {
		return codeUtilisateurDao.findById(codeId).orElse(null);
	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes sont non nulls et
	 * lance la validation via le Validator.
	 * 
	 * @param code        CodeUtilisateur
	 * @param bank
	 * @param utilisateur
	 * @param operation   demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(final CodeUtilisateur code, final Banque bank,
			final Utilisateur utilisateur, final String operation) {
		// Banque required
		if (bank != null) {
			// merge banque object
			code.setBanque(banqueDao.save(bank));
		} else if (code.getBanque() == null) {
			log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " du code utilisateur");
			throw new RequiredObjectIsNullException("CodeUtilisateur", operation, "Banque");
		}

		// Utilisateur required
		if (utilisateur != null) {
			// merge utilisateur object
			code.setUtilisateur(utilisateurDao.save(utilisateur));
		} else if (code.getUtilisateur() == null) {
			log.warn("Objet obligatoire Utilisateur manquant" + " lors de la " + operation + " du code utilisateur");
			throw new RequiredObjectIsNullException("CodeUtilisateur", operation, "Utilisateur");
		}

		// Validation
		BeanValidator.validateObject(code, new Validator[] { codeCommonValidator });
	}

	/**
	 * Cette méthode met à jour les associations entre un les codes de transcodage
	 * et le code utilisateur.
	 * 
	 * @param code       code utilisateur pour lequel on veut mettre à jour les
	 *                   transcodes.
	 * @param transcodes liste de transcodes que l'on veut associer au patient.
	 */
	private void updateTranscodes(final CodeUtilisateur codeU, final Collection<CodeCommon> transcodes) {

		final CodeUtilisateur code = codeUtilisateurDao.save(codeU);

		final Set<TranscodeUtilisateur> codes = code.getTranscodes();
		final Set<TranscodeUtilisateur> codesToRemove = new HashSet<>();

		Iterator<TranscodeUtilisateur> it = codes.iterator();
		// on parcourt les transcodes qui sont actuellement associés
		// au code
		while (it.hasNext()) {
			final TranscodeUtilisateur trTmp = it.next();
			final CodeCommon cTmp = getCodeCommonFromTransCode(trTmp);
			// si un transcode n'est pas dans la nouvelle liste, on
			// le conserve afin de le retirer par la suite
			if (!transcodes.contains(cTmp)) {
				codesToRemove.add(trTmp);
			} else {
				// on supprime le transcode de la liste à ajouter
				transcodes.remove(cTmp);
			}
		}

		// suppression des transcodes
		it = codesToRemove.iterator();
		while (it.hasNext()) {
			final TranscodeUtilisateur toRemove = it.next();
			code.getTranscodes().remove(toRemove);
			transcodeUtilisateurDao.deleteById(toRemove.getTranscodeUtilisateurId());
			log.debug("Suppression de l'association entre le code : " + code.toString() + " et le transcode : "
					+ getCodeCommonFromTransCode(toRemove).toString());
		}

		// on parcourt la nouvelle liste de transcodes
		final Iterator<CodeCommon> it2 = transcodes.iterator();
		while (it2.hasNext()) {
			final CodeCommon c = it2.next();

			final TranscodeUtilisateur newTr = makeTranscodeFromCodeCommon(c);
			newTr.setCodeUtilisateur(code);

			// on ajoute le transcode dans l'association
			code.getTranscodes().add(newTr);

			log.debug(
					"Ajout de l'association entre le code : " + code.toString() + " et le transcode : " + c.toString());
		}
		codeUtilisateurDao.save(code);
	}

	/**
	 * Cree une instance transcodeUTilisateur a partir d'une instance de CodeCommon.
	 * Seul le code utilisateur lui faisant reference n'est pas assigné.
	 * 
	 * @param code
	 * @return transcode
	 */
	private TranscodeUtilisateur makeTranscodeFromCodeCommon(final CodeCommon code) {
		final TranscodeUtilisateur newTr = new TranscodeUtilisateur();
		final TableCodage table = commonUtilsManager.getTableCodageFromCodeCommonManager(code);
		final Integer codeId = code.getCodeId();
		newTr.setCodeId(codeId);
		newTr.setTableCodage(table);

		return newTr;
	}

	/**
	 * Extrait le code contenu dans l'objet transcode utilisateur.
	 * 
	 * @param transcode
	 * @return code
	 */
	private CodeCommon getCodeCommonFromTransCode(final TranscodeUtilisateur trU) {
		return commonUtilsManager.findCodeByTableCodageAndIdManager(trU.getCodeId(), trU.getTableCodage());
	}
}