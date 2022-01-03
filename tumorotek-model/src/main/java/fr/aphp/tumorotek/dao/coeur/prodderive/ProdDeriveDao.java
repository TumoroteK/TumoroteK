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
package fr.aphp.tumorotek.dao.coeur.prodderive;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine ProdDerive. Interface créée le
 * 28/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
@Repository
public interface ProdDeriveDao extends CrudRepository<ProdDerive, Integer> {

	/**
	 * Recherche les produits dérivés dont le code est égal au paramètre.
	 * 
	 * @param code Code pour lequel on recherche des produits dérivés.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.code like ?1")
	List<ProdDerive> findByCode(String code);

	/**
	 * Recherche les produits dérivés dont le code ou le code labo est égal au
	 * paramètre.
	 * 
	 * @param code   Code pour lequel on recherche des produits dérivés.
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE (p.code like ?1 OR p.codeLabo like ?1) AND p.banque = ?2")
	List<ProdDerive> findByCodeOrLaboWithBanque(String code, Banque banque);

	/**
	 * Recherche les dérivés dont le code est 'like' le paramètre pour la plateforme
	 * spécifiée.
	 * 
	 * @param code Code pour lequel on recherche des dérivés.
	 * @param pf   Plateforme.
	 * @return une liste de dérivés.
	 * @since 2.1
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.code like ?1 AND p.banque.plateforme = ?2")
	List<ProdDerive> findByCodeInPlateforme(String code, Plateforme pf);

	/**
	 * Recherche les Ids de produits dérivés dont le code ou le code labo est égal
	 * au paramètre.
	 * 
	 * @param code   Code pour lequel on recherche des produits dérivés.
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p WHERE (p.code like ?1 OR p.codeLabo like ?1) AND p.banque = ?2")
	List<Integer> findByCodeOrLaboWithBanqueReturnIds(String code, Banque banque);

	/**
	 * Recherche les produits dérivés dont le code labo est égal au paramètre.
	 * 
	 * @param code Code Labo pour lequel on recherche des produits dérivés.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.codeLabo like ?1")
	List<ProdDerive> findByCodeLabo(String code);

	/**
	 * Recherche les produits dérivés dont la date de stockage est plus récente que
	 * celle passée en paramètre.
	 * 
	 * @param date Calendar pour laquelle on recherche des produits dérivés.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.dateStock > ?1")
	List<ProdDerive> findByDateStockAfterDate(Calendar date);

	/**
	 * Recherche les produits dérivés dont la date de transformation est plus
	 * récente que celle passée en paramètre.
	 * 
	 * @param date Calendar pour laquelle on recherche des produits dérivés.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.dateTransformation > ?1")
	List<ProdDerive> findByDateTransformationAfterDate(Calendar date);

	/**
	 * Recherche les produits dérivés dont la date de transformation est plus
	 * récente que celle passée en paramètre.
	 * 
	 * @param date   Calendar pour laquelle on recherche des produits dérivés.
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.dateTransformation > ?1 AND p.banque = ?2")
	List<ProdDerive> findByDateTransformationAfterDateWithBanque(Calendar date, Banque banque);

	/**
	 * Recherche tous les codes ProdDerives, sauf celui dont l'id est passé en
	 * paramètre.
	 * 
	 * @param prodDeriveId Identifiant du ProdDerive que l'on souhaite exclure de la
	 *                     liste retournée.
	 * @param bank
	 * @return une liste de codes de ProdDerive.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.prodDeriveId != ?1 and banque = ?2")
	List<String> findByExcludedIdCodes(Integer prodDeriveId, Banque bank);

	/**
	 * Recherche les produits dérivés dont le statut est passé en paramètre.
	 * 
	 * @param statut ObjetStatut pour lequel on recherche des produits dérivés.
	 * @return une liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.objetStatut = ?1")
	List<ProdDerive> findByObjetStatut(ObjetStatut statut);

	/**
	 * Recherche les produits dérivés dont le type est passé en paramètre.
	 * 
	 * @param type ProdType des produits dérivés que l'on recherche.
	 * @return une liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.prodType = ?1")
	List<ProdDerive> findByProdType(ProdType type);

	/**
	 * Recherche les produits dérivés dont la qualité est passé en paramètre.
	 * 
	 * @param qualite ProdQualite des produits dérivés que l'on recherche.
	 * @return une liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.prodQualite = ?1")
	List<ProdDerive> findByProdQualite(ProdQualite qualite);

	/**
	 * Recherche les produits dérivés dont la préparation est passé en paramètre.
	 * 
	 * @param mode ModePrepaDerive des produits dérivés que l'on recherche.
	 * @return une liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.modePrepaDerive = ?1")
	List<ProdDerive> findByModePrepaDerive(ModePrepaDerive mode);

	/**
	 * Recherche les produits dérivés dont la transformation est passée en
	 * paramètre.
	 * 
	 * @param transformation Transformation des produits dérivés que l'on recherche.
	 * @return une liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.transformation = ?1 ORDER BY p.prodDeriveId")
	List<ProdDerive> findByTransformation(Transformation transformation);

	/**
	 * Recherche les codes de produits dérivés dont la banque est passée en
	 * paramètre.
	 * 
	 * @param banque Banque des produits dérivés que l'on recherche.
	 * @return une liste de codes de produits dérivés.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.banque = ?1")
	List<String> findByBanqueSelectCode(Banque banque);

	/**
	 * Recherche les codes de dérivés dont la banque et le statut sont passés en
	 * paramètres.
	 * 
	 * @param banques     Liste des Banques des dérivés que l'on recherche.
	 * @param objetStatut ObjetStatut des dérivés.
	 * @return une liste de codes de dérivés.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.banque in (?1) AND p.objetStatut = ?2 ORDER BY p.code")
	List<String> findByBanqueInListStatutSelectCode(List<Banque> banques, ObjetStatut objetStatut);

	/**
	 * Recherche les codes de dérivés dont la banque et le statut sont passés en
	 * paramètres.
	 * 
	 * @param banque      Banque des dérivés que l'on recherche.
	 * @param objetStatut ObjetStatut des dérivés.
	 * @return une liste de codes de dérivés.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.banque = ?1 AND p.objetStatut = ?2 ORDER BY p.code")
	List<String> findByBanqueStatutSelectCode(Banque banque, ObjetStatut objetStatut);

	/**
	 * Recherche les ProdDerives d'une enceinte terminale.
	 * 
	 * @param entite    Entité représentant les ProdDerives.
	 * @param terminale Enceinte terminale
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.prodDeriveId IN (select empl.objetId FROM Emplacement empl "
			+ "WHERE empl.entite = ?1 AND empl.terminale = ?2)")
	List<ProdDerive> findByTerminale(Entite entite, Terminale terminale);

	/**
	 * Recherche les ProdDerives d'une enceinte terminale.
	 * 
	 * @param terminale Enceinte terminale
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.emplacement.terminale = ?1")
	List<ProdDerive> findByTerminaleDirect(Terminale terminale);

	/**
	 * Recherche les codes de dérivés dont la banque est passée en paramètre et la
	 * quantité est non égale à 0.
	 * 
	 * @param banque Banque des dérivés que l'on recherche.
	 * @return une liste de codes de dérivés.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.banque = ?1 AND (p.quantite > 0 OR p.quantite IS NULL) "
			+ "AND p.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE')")
	List<String> findByBanqueAndQuantiteSelectCode(Banque banque);

	/**
	 * Recherche les codes de dérivés dont la banque est passée en paramètre et la
	 * quantité est non égale à 0 OU dérivé faisant partie d'une cession de type
	 * traitement
	 * 
	 * @param banque Banque des échantillons que l'on recherche.
	 * @return une liste de codes de dérivés.
	 */
	@Query("SELECT p.code FROM ProdDerive p WHERE p.banque = ?1 AND (((p.quantite > 0 OR p.quantite IS NULL) "
			+ "AND p.objetStatut.statut not in ('EPUISE', 'ENCOURS', 'RESERVE'))OR ("
			+ "p.prodDeriveId in (SELECT c.pk.objetId FROM CederObjet c WHERE c.pk.entite.nom = 'ProdDerive' AND c.pk.cession.cessionType.type = 'Traitement' AND c.statut = 'TRAITEMENT'"
			+ ")))")
	List<String> findAllCodesByBanqueAndQuantiteNotNullOrInCessionTraitement(Banque banque);

	/**
	 * Recherche les ProdDerives pour une liste de collections spécifiées.
	 * 
	 * @param banks liste de collections
	 * @return Une liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.banque in (?1) ORDER BY p.banque, p.code")
	List<ProdDerive> findByBanques(List<Banque> banks);

	/**
	 * Recherche les ProdDerives en fonction du parent et du type du produit.
	 * 
	 * @param objetId  Identifiant du parent.
	 * @param entite   Entite du parent.
	 * @param prodType Type des dérivés recherchés.
	 * @return Une liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.transformation.objetId = ?1 AND p.transformation.entite = ?2 "
			+ "AND p.prodType.nom like ?3")
	List<ProdDerive> findByParentAndType(Integer objetId, Entite entite, String prodType);

	/**
	 * Recherche les ids des dérivés issus d'un patient en passant par la liason
	 * avec un échantillon.
	 * 
	 * @param nom Nom du patient.
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p, Echantillon e JOIN e.prelevement as prlvt "
			+ "JOIN prlvt.maladie as m JOIN m.patient as pat WHERE p.transformation.objetId = e.echantillonId "
			+ "AND p.transformation.entite.nom = 'Echantillon' AND (pat.nom like ?1 OR pat.nip like ?1)  AND p.banque = ?2")
	List<Integer> findByEchantillonPatientNomReturnIds(String nom, Banque banque);

	/**
	 * Recherche les dérivés issus d'un patient en passant par la liason avec un
	 * prelevement.
	 * 
	 * @param nom Nom du patient.
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p, Prelevement prlvt JOIN prlvt.maladie as m "
			+ "JOIN m.patient as pat WHERE p.transformation.objetId = prlvt.prelevementId "
			+ "AND p.transformation.entite.nom = 'Prelevement' AND pat.nom like ?1 AND p.banque = ?2")
	List<Integer> findByPrelevementPatientNomreturnIds(String nom, Banque banque);

	/**
	 * Recherche les ids des produits dérivés l'id est dans la liste.
	 * 
	 * @param ids Liste d'identifiants.
	 * @return Liste de produits dérivés.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.prodDeriveId in (?1)")
	List<ProdDerive> findByIdInList(List<Integer> ids);

	/**
	 * Recherche les ids des dérivés des banques de la liste.
	 * 
	 * @param banques Banques des dérivés recherchés.
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p WHERE p.banque in (?1)")
	List<Integer> findByBanquesAllIds(List<Banque> banques);

	/**
	 * Recherche les ProdDerives en fonction du parent.
	 * 
	 * @param objetId Identifiant du parent.
	 * @param entite  Entite du parent.
	 * @return Une liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.transformation.objetId = ?1 AND p.transformation.entite = ?2 ")
	List<ProdDerive> findByParent(Integer objetId, Entite entite);

	/**
	 * Recherche les Ids de produits dérivés dont le code est dans la liste passée
	 * en paramètres.
	 * 
	 * @param criteres Codes pour lequel on recherche des produits dérivés.
	 * @param banques  Banques auxquelles appartiennent les dérivés.
	 * @return une liste de tuples ids / code.
	 * @version 2.1
	 */
	@Query("SELECT p.prodDeriveId, p.code FROM ProdDerive p WHERE p.code in (?1) AND p.banque in (?2)")
	List<Object[]> findByCodeInListWithBanque(List<String> criteres, List<Banque> banques);

	/**
	 * Recherche les ids des dérivés issus de plusieurs patients en passant par la
	 * liason avec un échantillon.
	 * 
	 * @param criteres Noms ou NIP des patients.
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p, Echantillon e JOIN e.prelevement as prlvt "
			+ "JOIN prlvt.maladie as m JOIN m.patient as pat WHERE p.transformation.objetId = e.echantillonId "
			+ "AND p.transformation.entite.nom = 'Echantillon' AND (pat.nom in (?1) OR pat.nip in (?1)) "
			+ "AND p.banque in (?2)")
	List<Integer> findByEchantillonPatientNomInListReturnIds(List<String> criteres, List<Banque> banques);

	/**
	 * Recherche les dérivés issus de plusieurs patients en passant par la liason
	 * avec un prelevement.
	 * 
	 * @param criteres Noms ou NIP des patients.
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p.prodDeriveId FROM ProdDerive p, Prelevement prlvt JOIN prlvt.maladie as m "
			+ "JOIN m.patient as pat WHERE p.transformation.objetId = prlvt.prelevementId "
			+ "AND p.transformation.entite.nom = 'Prelevement' AND (pat.nom in (?1) OR pat.nip in (?1)) "
			+ "AND p.banque in (?2)")
	List<Integer> findByPrelevementPatientNomInListreturnIds(List<String> criteres, List<Banque> banques);

	/**
	 * Recherche les dérivés dont le Collaborateur est passé en param
	 * 
	 * @param Collaborateur
	 * @return Liste de ProdDerives.
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.collaborateur = ?1")
	List<ProdDerive> findByCollaborateur(Collaborateur collaborateur);

	/**
	 * Recherche les dérivés pour cet emplacement, caractérisé par une position dans
	 * une terminale (car l'emplacement peut ne pas être créé). Cette recherche a
	 * pour objectif un contrôle de validation à la création des nouveaux objets.
	 * 
	 * @param Terminale
	 * @param Integer   position
	 * @return liste Dérives
	 */
	@Query("SELECT p FROM ProdDerive p WHERE p.emplacement.terminale = ?1 AND p.emplacement.position = ?2")
	List<ProdDerive> findByEmplacement(Terminale terminale, Integer position);

	/**
	 * Compte les produits dérivés créés par un collaborateur passée en paramètre.
	 * 
	 * @param collaborateur
	 * @return long
	 */
	@Query("SELECT count(p) FROM ProdDerive p, Operation o WHERE p.prodDeriveId = o.objetId "
			+ "and p.collaborateur = (?1) AND o.operationType.nom = 'Creation' AND o.entite.nom = 'ProdDerive'")
	List<Long> findCountCreatedByCollaborateur(Collaborateur colla);

	/**
	 * Compte les produits dérivés dont l'operateur est le collaborateur passée en
	 * paramètre.
	 * 
	 * @param collaborateur
	 * @return long
	 */
	@Query("SELECT count(p) FROM ProdDerive p WHERE p.collaborateur = ?1")
	List<Long> findCountByOperateur(Collaborateur colla);

	/**
	 * Compte les produits dérivés dont le parent est passé passée en paramètre sous
	 * la forme id + entite
	 * 
	 * @param parent id Integer
	 * @param Entite
	 * @return long since 2.1.1
	 */
	@Query("SELECT count(p) FROM ProdDerive p WHERE p.transformation.objetId = ?1 AND p.transformation.entite = ?2")
	List<Long> findCountByParent(Integer pId, Entite entite);

	/**
	 * Recherche sur une plateforme les dérivés dont le code est contenu dans la
	 * liste passée en paramètre
	 * 
	 * @param listCodes liste des codes recherchés
	 * @param pf        plateforme sur laquelle la recherche est effectuée
	 * @since 2.2.0
	 */
	@Query("SELECT e FROM ProdDerive e JOIN e.banque bq JOIN bq.plateforme pf WHERE e.code in (?1) AND pf = ?2 ")
	List<ProdDerive> findByListCodeWithPlateforme(List<String> listCodes, Plateforme pf);

	/**
	 * Recherche les dérivés ayant eu une dégradation possible du matériel
	 * 
	 * @param banque
	 * @param impact
	 * @return une liste d'ids.
	 */
	@Query("SELECT e.prodDeriveId FROM ProdDerive e, Retour r WHERE e.prodDeriveId = r.objetId "
			+ "and e.banque in (?1)" + "and r.impact in (?2) ")
	List<Integer> findByBanksAndImpact(List<Banque> banks, List<Boolean> impact);
}
