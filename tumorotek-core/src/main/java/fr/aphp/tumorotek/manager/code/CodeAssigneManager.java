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

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.CodeAssigneDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueTableCodageDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeCommonValidator;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodagePK;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 *
 * Interface pour le Manager du bean de domaine CodeAssigne. Concerne la
 * creation de CodeAssigne représentant les codes assignes pour l'organe, le
 * type lésionnel/morphologique et le diagnostic. Interface créée le 26/06/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3
 *
 */
@Service
public class CodeAssigneManager {

	private final Log log = LogFactory.getLog(CodeAssigneManager.class);

	@Autowired
	private CodeAssigneDao codeAssigneDao;

	@Autowired
	private EchantillonDao echantillonDao;

	@Autowired
	private TableCodageDao tableCodageDao;

	@Autowired
	private OperationManager operationManager;

	@Autowired
	private OperationTypeDao operationTypeDao;

	@Autowired
	private CodeCommonValidator codeCommonValidator;

	@Autowired
	private BanqueTableCodageDao banqueTableCodageDao;

	@Autowired
	private PrelevementDao prelevementDao;

	/**
	 * Recherche toutes les instances de codes présentes dans la codification.
	 * 
	 * @return List contenant les codes.
	 */
	public List<CodeAssigne> findAllObjectsManager() {
		return IterableUtils.toList(codeAssigneDao.findAll());
	}

	/**
	 * Recherche les codes assignes dont le code like celui au paramètre.
	 * 
	 * @param code       Code pour lequel on recherche des codes assignes.
	 * @param exactMatch
	 * @return une liste de codes assignes.
	 */
	public List<CodeAssigne> findByCodeLikeManager(String code, final boolean exactMatch) {
		if (!exactMatch) {
			code = "%" + code + "%";
		}
		log.debug("Recherche CodeAssigne par code: " + code + " exactMatch " + String.valueOf(exactMatch));
		return codeAssigneDao.findByCodeLike(code);
	}

	/**
	 * Recherche les codes assignes dont le libelle like celui au paramètre.
	 * 
	 * @param libelle    Libelle pour lequel on recherche des codes assignes.
	 * @param exactMatch
	 * @return une liste de codes assignes.
	 */
	public List<CodeAssigne> findByLibelleLikeManager(String libelle, final boolean exactMatch) {
		if (!exactMatch) {
			libelle = "%" + libelle + "%";
		}
		log.debug("Recherche CodeAssigne par libelle: " + libelle + " exactMatch " + String.valueOf(exactMatch));
		return codeAssigneDao.findByLibelleLike(libelle);
	}

	/**
	 * Recherche les codes qui sont assignes comme type lésionnel/morpho pour
	 * l'échantillon.
	 * 
	 * @param echantillon.
	 * @return une liste de codes assignes.
	 */
	public List<CodeAssigne> findCodesMorphoByEchantillonManager(final Echantillon echan) {
		return codeAssigneDao.findCodesMorphoByEchantillon(echan);
	}

	/**
	 * Recherche les codes qui sont assignes pour definir l'organe dont est issu
	 * l'échantillon.
	 * 
	 * @param echantillon.
	 * @return une liste de codes assignes.
	 */
	public List<CodeAssigne> findCodesOrganeByEchantillonManager(final Echantillon echan) {
		return codeAssigneDao.findCodesOrganeByEchantillon(echan);
	}

	/**
	 * Cherche les doublons en se basant sur la methode equals() surchargee par les
	 * entites. Si l'objet est modifie donc a un id attribue par le SGBD, ce dernier
	 * est retire de la liste findAll.
	 * 
	 * @param table CodeAssigne dont on cherche la presence dans la base
	 * @return true/false
	 */
	public boolean findDoublonManager(final CodeAssigne code) {
		if (code.getCodeAssigneId() == null) {
			return codeAssigneDao.findByCodeAndEchantillon(code.getCode(), code.getEchantillon()).contains(code);
		}
		return codeAssigneDao.findByExcludedId(code.getCodeAssigneId(), code.getCode(), code.getEchantillon())
				.contains(code);
	}

	/**
	 * Enregistre ou modifie un code assigne.
	 * 
	 * @param code
	 * @param echantillon
	 * @param TableCodage code referent
	 * @param utilisateur
	 * @param String      operation creation/modification
	 */
	public void createOrUpdateManager(final CodeAssigne code, final Echantillon echantillon, final TableCodage table,
			final Utilisateur utilisateur, final String operation) {

		if (operation == null) {
			throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
		}

		// table codage non required mais implique codeRefId
		if (table != null) {
			code.setTableCodage(tableCodageDao.save(table));
		}

		// Validation
		checkRequiredObjectsAndValidate(code, echantillon, operation);

		// Doublon
		if (!findDoublonManager(code)) {
			if ((operation.equals("creation") || operation.equals("modification"))) {

				if (operation.equals("creation")) {
					codeAssigneDao.save(code);
					log.info("Enregistrement objet CodeAssigne " + code.toString());

					CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
							operationTypeDao.findByNom("Creation").get(0), utilisateur);
				} else {
					// enregistre une modification si code ou libelle
					// a changé
					final CodeAssigne inBase = codeAssigneDao.findById(code.getCodeAssigneId()).orElse(null);

					final boolean doRecordModif = (!inBase.getCode().equals(code.getCode()))
							|| ((inBase.getLibelle() != null && !inBase.getLibelle().equals(code.getLibelle()))
									|| (inBase.getLibelle() == null && code.getLibelle() != null));

					codeAssigneDao.save(code);
					log.info("Modification objet CodeAssigne " + code.toString());
					if (doRecordModif) {
						CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
								operationTypeDao.findByNom("Modification").get(0), utilisateur);
					}
				}
			} else {
				throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet CodeUtilisateur " + code.toString());
			throw new DoublonFoundException("CodeAssigne", operation);
		}
	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes sont non nulls et
	 * lance la validation via le Validator.
	 * 
	 * @param code        CodeAssigne
	 * @param echantillon
	 * @param operation   demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(final CodeAssigne code, final Echantillon echantillon,
			final String operation) {
		// Echantillon required
		if (echantillon != null) {
			// merge echantillon object
			code.setEchantillon(echantillonDao.save(echantillon));
		} else if (code.getEchantillon() == null) {
			log.warn("Objet obligatoire Echantillon manquant" + " lors de la " + operation + " du code assigne");
			throw new RequiredObjectIsNullException("CodeAssigne", operation, "Echantillon");
		}

		// Validation
		BeanValidator.validateObject(code, new Validator[] { codeCommonValidator });
	}

	/**
	 * Supprime un objet de la base de données.
	 * 
	 * @param code CodeAssigne à supprimer de la base de données.
	 */
	public void removeObjectManager(final CodeAssigne code) {
		if (code != null) {
			// nullify oneToOne relationship
			// if (code.getEchanExpOrg() != null
			// && code.getEchanExpOrg()
			// .getCodeOrganeExport() != null
			// && code.getEchanExpOrg()
			// .getCodeOrganeExport().equals(code)) {
			// code.getEchanExpOrg().setCodeOrganeExport(null);
			//// echantillonDao.save(code.getEchanExpOrg());
			// }
			// if (code.getEchanExpLes() != null
			// && code.getEchanExpLes()
			// .getCodeLesExport() != null
			// && code.getEchanExpLes()
			// .getCodeLesExport().equals(code)) {
			// code.getEchanExpLes().setCodeLesExport(null);
			//// echantillonDao.save(code.getEchanExpLes());
			// }
			codeAssigneDao.deleteById(code.getCodeAssigneId());
			log.info("Suppression objet CodeAssigne " + code.toString());
			// Supprime operations associes
			CreateOrUpdateUtilities.removeAssociateOperations(code, operationManager);
		} else {
			log.warn("Suppression d'un CodeAssigne null");
		}
	}

	/**
	 * Recherche les codes lésionels assignes exportes pour chacun des echantillons
	 * issus du prélèvement passé en paramètre. Ordonne les codes suivant id des
	 * échantillons.
	 * 
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findCodesLesExportedByPrelevementManager(final Prelevement prel) {
		return codeAssigneDao.findCodesLesExportedByPrelevement(prel);
	}

	/**
	 * Recherche les codes organes assignes exportes pour chacun des echantillons
	 * issus du prélèvement passé en paramètre. Ordonne les codes suivant id des
	 * échantillons.
	 * 
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findCodesOrgExportedByPrelevementManager(final Prelevement prel) {
		return codeAssigneDao.findCodesOrgExportedByPrelevement(prel);
	}

	/**
	 * Recherche les codes organes assignes exportes pour chacun des echantillons
	 * issus du patient passé en paramètre. Ordonne les codes suivant id des
	 * échantillons.
	 * 
	 * @param pat Patient
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findCodesOrgExportedByPatientManager(final Patient pat) {
		return codeAssigneDao.findCodesOrgExportedByPatient(pat);
	}

	/**
	 * Recherche le premier code lésionel assigne pour chacun des echantillons issus
	 * du prélèvement passé en paramètre. Ordonne les codes suivant id des
	 * échantillons.
	 * 
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findFirstCodesLesByPrelevementManager(final Prelevement prel) {
		final List<CodeAssigne> results = new ArrayList<>();
		if (prel != null) {
			final List<Echantillon> echans = echantillonDao.findByPrelevement(prel);
			for (int i = 0; i < echans.size(); i++) {
				final List<CodeAssigne> tmp = codeAssigneDao.findCodesMorphoByEchantillon(echans.get(i));
				if (tmp.size() > 0) {
					results.add(tmp.get(0));
				}
			}
		}
		return results;
	}

	/**
	 * Recherche le premier code organe assigne pour chacun des echantillons issus
	 * du prélèvement passé en paramètre. Ordonne les codes suivant id des
	 * échantillons.
	 * 
	 * @param prel Prelevement
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findFirstCodesOrgByPrelevementManager(final Prelevement prel) {
		final List<CodeAssigne> results = new ArrayList<>();
		if (prel != null) {
			final List<Echantillon> echans = echantillonDao.findByPrelevement(prel);
			for (int i = 0; i < echans.size(); i++) {
				final List<CodeAssigne> tmp = codeAssigneDao.findCodesOrganeByEchantillon(echans.get(i));
				if (tmp.size() > 0) {
					results.add(tmp.get(0));
				}
			}
		}
		return results;
	}

	/**
	 * Recherche le premier code organe assigne pour chacun des echantillons issus
	 * du patient passé en paramètre. Ordonne les codes suivant id des échantillons.
	 * 
	 * @param pat Patient
	 * @return une liste de CodeAssignes.
	 */
	public List<CodeAssigne> findFirstCodesOrgByPatientManager(final Patient pat) {
		final List<CodeAssigne> results = new ArrayList<>();
		if (pat != null) {
			final List<Prelevement> prlvts = prelevementDao.findByPatient(pat);
			for (int k = 0; k < prlvts.size(); k++) {
				final List<Echantillon> echans = echantillonDao.findByPrelevement(prlvts.get(k));
				for (int i = 0; i < echans.size(); i++) {
					final List<CodeAssigne> tmp = codeAssigneDao.findCodesOrganeByEchantillon(echans.get(i));
					if (tmp.size() > 0) {
						results.add(tmp.get(0));
					}
				}
			}
		}
		return results;
	}

	/**
	 * Transforme une liste de codeAssigne en une liste de String obtenue à partir
	 * du code ou du libelle si l'association entre la banque et la table de codage
	 * le mentionne.
	 * 
	 * @param codes
	 * @return liste String, utilisée pour l'affichage.
	 */
	public List<String> formatCodesAsStringsManager(final List<CodeAssigne> codes) {
		final List<String> strs = new ArrayList<>();
		final List<BanqueTableCodage> btcs = new ArrayList<>();
		String s;
		BanqueTableCodagePK pk;
		BanqueTableCodage bc;
		BanqueTableCodage tmp;
		if (codes != null) {
			for (int i = 0; i < codes.size(); i++) {
				s = codes.get(i).getCode();
				pk = new BanqueTableCodagePK();
				pk.setTableCodage(codes.get(i).getTableCodage());
				pk.setBanque(codes.get(i).getEchantillon().getBanque());
				bc = new BanqueTableCodage();
				bc.setPk(pk);
				// ajoute a la liste pour eviter requete à chaque fois
				if (!btcs.contains(bc)) {
					tmp = banqueTableCodageDao.findById(pk).orElse(null);
					if (tmp != null) {
						btcs.add(tmp);
					}
				}
				// utilise le libelle si banqueTableCodage le specifie
				if (btcs.contains(bc) && btcs.get(btcs.indexOf(bc)).getLibelleExport()) {
					if (codes.get(i).getLibelle() != null) {
						s = codes.get(i).getLibelle();
					}
				}
				strs.add(s);
			}
		}
		return strs;
	}

	/**
	 * Préparations batch statements pour full JDBC inserts d'une liste de codes
	 * assignes associes à l'echantillon passe en parametre. Les validations à la
	 * création sont identiques à celles lancées en JPA (doublons, syntaxe).
	 * 
	 * @param jdbcSuite   contenant les ids et statements permettant la creation des
	 *                    objets en full JDBC
	 * @param echantillon
	 * @param codes
	 * @param usr
	 * @since 2.0.10.6
	 */
	public void prepareListJDBCManager(final EchantillonJdbcSuite jdbcSuite, final Echantillon echantillon,
			final List<CodeAssigne> codes, final Utilisateur usr) throws SQLException {

		if (codes != null && !codes.isEmpty()) {

			final Integer maxCdId = jdbcSuite.getMaxCodeAssigneId();
			// Statement stmt = null;
			// PreparedStatement pstmtCd = null;
			// ResultSet rs2 = null;
			// PreparedStatement pstmtOp = null;

			try {
				// stmt = DataSourceUtils.getConnection(dataSource)
				// .createStatement();
				// rs2 = stmt.executeQuery("select max(code_assigne_id)"
				// + " from CODE_ASSIGNE");
				// rs2.first();
				// maxCdId = rs2.getInt(1);

				// String sql = "insert into CODE_ASSIGNE (CODE_ASSIGNE_ID, ECHANTILLON_ID, "
				// + "CODE, LIBELLE, CODE_REF_ID, TABLE_CODAGE_ID, IS_ORGANE, "
				// + "IS_MORPHO, ORDRE, EXPORT) "
				// + "values (?,?,?,?,?,?,?,?,?,?)";
				//
				// pstmtCd = DataSourceUtils.getConnection(dataSource)
				// .prepareStatement(sql);
				//
				// String sql2 = "insert into OPERATION (UTILISATEUR_ID, "
				// + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, "
				// + "DATE_, V1)"
				// + "values (?,?,?,?,?,?)";
				// pstmtOp = DataSourceUtils.getConnection(dataSource)
				// .prepareStatement(sql2);

				// si objetid null
				if (echantillon == null || echantillon.getEchantillonId() == null) {
					throw new RequiredObjectIsNullException("CodeAssigne", "creation", "EchantillonId");
				}
				final Integer objId = echantillon.getEchantillonId();

				// création des nouveaux codes assignes
				// HashSet exclue doublons
				for (final CodeAssigne code : new LinkedHashSet<>(codes)) {
					code.setEchantillon(echantillon);
					// Validation
					BeanValidator.validateObject(code, new Validator[] { codeCommonValidator });

					// Doublon
					// if (!findDoublonManager(code)) {
					// maxCdId ++;
					jdbcSuite.incrementMaxCodeAssigneId();
					jdbcSuite.getPstmtCd().clearParameters();
					jdbcSuite.getPstmtCd().setInt(1, jdbcSuite.getMaxCodeAssigneId());
					jdbcSuite.getPstmtCd().setInt(2, objId);
					jdbcSuite.getPstmtCd().setString(3, code.getCode());
					if (code.getLibelle() != null) {
						jdbcSuite.getPstmtCd().setString(4, code.getLibelle());
					} else {
						jdbcSuite.getPstmtCd().setNull(4, Types.VARCHAR);
					}
					if (code.getCodeRefId() != null) {
						jdbcSuite.getPstmtCd().setInt(5, code.getCodeRefId());
					} else {
						jdbcSuite.getPstmtCd().setNull(5, Types.INTEGER);
					}
					if (code.getTableCodage() != null) {
						jdbcSuite.getPstmtCd().setInt(6, code.getTableCodage().getTableCodageId());
					} else {
						jdbcSuite.getPstmtCd().setNull(6, Types.INTEGER);
					}
					if (code.getIsOrgane() != null) {
						jdbcSuite.getPstmtCd().setBoolean(7, code.getIsOrgane());
					} else {
						jdbcSuite.getPstmtCd().setNull(7, Types.BOOLEAN);
					}
					if (code.getIsMorpho() != null) {
						jdbcSuite.getPstmtCd().setBoolean(8, code.getIsMorpho());
					} else {
						jdbcSuite.getPstmtCd().setNull(8, Types.BOOLEAN);
					}
					jdbcSuite.getPstmtCd().setInt(9, code.getOrdre());
					jdbcSuite.getPstmtCd().setBoolean(10, code.getExport());

					jdbcSuite.getPstmtCd().addBatch();

					// operation
					jdbcSuite.getPstmtOp().clearParameters();
					jdbcSuite.getPstmtOp().setInt(1, usr.getUtilisateurId());
					jdbcSuite.getPstmtOp().setInt(2, jdbcSuite.getMaxCodeAssigneId());
					jdbcSuite.getPstmtOp().setInt(3, code.getIsOrgane() ? 41 : 54);
					jdbcSuite.getPstmtOp().setInt(4, 3);
					jdbcSuite.getPstmtOp().setTimestamp(5,
							new java.sql.Timestamp(Utils.getCurrentSystemCalendar().getTimeInMillis()));
					jdbcSuite.getPstmtOp().setBoolean(6, false);
					jdbcSuite.getPstmtOp().addBatch();
					// }
					// else {
					// throwDoublonException();
					// }

				}
				// pstmtCd.executeBatch();
				// pstmtOp.executeBatch();

			} catch (final Exception e) {
				// rollback create operation
				jdbcSuite.setMaxCodeAssigneId(maxCdId);
				throw e;
			} finally {
				//// if (stmt != null) {
				// try { stmt.close();
				// } catch (Exception e) { stmt = null; }
				// }
				// if (pstmtCd != null) {
				// try { pstmtCd.close();
				// } catch (Exception e) { pstmtCd = null; }
				// }
				// if (pstmtOp != null) {
				// try { pstmtOp.close();
				// } catch (Exception e) { pstmtOp = null; }
				// }
				// if (rs2 != null) {
				// try { rs2.close();
				// } catch (Exception e) { rs2 = null; }
				// }
			}
		}
	}
}
