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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Interface pour le Manager du bean de domaine TableCodage. Interface créée le
 * 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Service
public class TableCodageManager2 {
	@Autowired
	private TableCodageDao tableCodageDao;

	@Autowired
	private AdicapManager adicapManager;

	@Autowired
	private CimMasterManager cimMasterManager;

	@Autowired
	private CimoMorphoManager cimoMorphoManager;

	@Autowired
	private CodeUtilisateurManager codeUtilisateurManager;

	/**
	 * Recherche toutes les instances de TableCodage.
	 * 
	 * @return List contenant les tables de codage.
	 */
	public List<TableCodage> findAllObjectsManager() {
		return IterableUtils.toList(tableCodageDao.findAll());
	}

	/**
	 * Recherche la table de codage dont le nom est like celui passé en paramètre.
	 * 
	 * @param nom Nom pour lequel on recherche une table.
	 * @return List contenant les tables de codage.
	 */
	public List<TableCodage> findByNomManager(final String nom) {
		return tableCodageDao.findByNom(nom);
	}

	/**
	 * Trouve tous les codes au travers des tables de transcodage pour le code et sa
	 * table de codage passés en paramètres.
	 * 
	 * @param code   dont on cherche les transcodes
	 * @param tables auxquelles peuvent appartenir les transcodes.
	 * @param liste  de banque (pour les codes utilisateur)
	 * @return
	 */
	public List<CodeCommon> transcodeManager(final CodeCommon code, final List<TableCodage> tables,
			final List<Banque> banks) {
		final List<CodeCommon> resTrans = new ArrayList<>();
		if (code != null && tables != null) {

			final boolean containsAdicap = tables.contains(tableCodageDao.findByNom("ADICAP").get(0));
			final boolean containsCim = tables.contains(tableCodageDao.findByNom("CIM_MASTER").get(0));
			final boolean containsCimo = tables.contains(tableCodageDao.findByNom("CIMO_MORPHO").get(0));
			final boolean containsUser = tables.contains(tableCodageDao.findByNom("UTILISATEUR").get(0));

			if (code instanceof Adicap) {
				if (containsCim) {
					resTrans.addAll(adicapManager.getCimMastersManager((Adicap) code));
				}
				if (containsCimo) {
					resTrans.addAll(adicapManager.getCimoMorphosManager((Adicap) code));
				}
				if (containsUser) {
					resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
				}
			} else if (code instanceof CimMaster) {
				if (containsAdicap) {
					resTrans.addAll(cimMasterManager.getAdicapsManager((CimMaster) code));
				}
				if (containsUser) {
					resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
				}
			} else if (code instanceof CimoMorpho) {
				if (containsAdicap) {
					resTrans.addAll(cimoMorphoManager.getAdicapsManager((CimoMorpho) code));
				}
				if (containsUser) {
					resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
				}
			} else if (code instanceof CodeUtilisateur) {
				resTrans.addAll(codeUtilisateurManager.getTranscodesManager((CodeUtilisateur) code, tables));
			}
		}
		return resTrans;
	}

	/**
	 * Trouve tous les codes au travers des tables de transcodage pour la chaine de
	 * caractere passées en paramètres. Trouve pour chaque table le code dont le
	 * code ou le libellé est égal à la chaine de caractère passée en paramètre et
	 * trouve tous les transcodes pour les autres tables.
	 * 
	 * @param string     chaine de caractères à matcher sur la base de codes.
	 * @param liste      de table de codifications.
	 * @param liste      de banque (pour les codes utilisateur)
	 * @param exactMatch true si recherche exacte a partir code ou libelle
	 * @return
	 */
	public List<CodeCommon> findCodesAndTranscodesFromStringManager(final String codeorLib,
			final List<TableCodage> tables, final List<Banque> banks, final boolean exactMatch) {

		final Set<CodeCommon> codes = new LinkedHashSet<>();

		final List<CodeCommon> res = new ArrayList<>();
		final List<CodeCommon> trans = new ArrayList<>();
		if (tables != null) {
			for (int i = 0; i < tables.size(); i++) {

				res.clear();
				trans.clear();

				if ("ADICAP".equals(tables.get(i).getNom())) {
					res.addAll(adicapManager.findByCodeLikeManager(codeorLib, exactMatch));
					res.addAll(adicapManager.findByLibelleLikeManager(codeorLib, exactMatch));
				} else if ("CIM_MASTER".equals(tables.get(i).getNom())) {
					res.addAll(cimMasterManager.findByCodeLikeManager(codeorLib, exactMatch));
					res.addAll(cimMasterManager.findByLibelleLikeManager(codeorLib, exactMatch));
				} else if ("CIMO_MORPHO".equals(tables.get(i).getNom())) {
					res.addAll(cimoMorphoManager.findByCodeLikeManager(codeorLib, exactMatch));
					res.addAll(cimoMorphoManager.findByLibelleLikeManager(codeorLib, exactMatch));
				} else if ("UTILISATEUR".equals(tables.get(i).getNom())) {
					res.addAll(codeUtilisateurManager.findByCodeLikeManager(codeorLib, exactMatch, banks));
					res.addAll(codeUtilisateurManager.findByLibelleLikeManager(codeorLib, exactMatch, banks));
				}

				// cherche transcodes dans autre tables
				final Iterator<CodeCommon> resIt = res.iterator();
				CodeCommon next;
				while (resIt.hasNext()) {
					next = resIt.next();
					trans.addAll(transcodeManager(next, tables, banks));
				}

				codes.addAll(res);
				codes.addAll(trans);
			}
		}

		return new ArrayList<>(codes);
	}

	/**
	 * Transforme une liste de CodeCommon en la liste de codes équivalente.
	 * 
	 * @param codes
	 * @return liste de codes String
	 */
	public List<String> getListCodesFromCodeCommon(final List<CodeCommon> codes) {
		final List<String> codesStr = new ArrayList<>();
		if (codes != null) {
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getCode() != null) {
					codesStr.add(codes.get(i).getCode());
				}
			}
		}
		return codesStr;
	}

	/**
	 * Transforme une liste de CodeCommon en la liste de libelles équivalente.
	 * 
	 * @param codes
	 * @return liste de libelles String
	 */
	public List<String> getListLibellesFromCodeCommon(final List<CodeCommon> codes) {
		final List<String> libellesStr = new ArrayList<>();
		if (codes != null) {
			for (int i = 0; i < codes.size(); i++) {
				if (codes.get(i).getLibelle() != null) {
					libellesStr.add(codes.get(i).getLibelle());
				}
			}
		}
		return libellesStr;
	}
}
