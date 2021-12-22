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
package fr.aphp.tumorotek.dao.contexte;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine Banque.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour, Mathieu BARTHELEMY
 * @version 2.3
 */
@Repository
public interface BanqueDao extends CrudRepository<Banque, Integer> {

	/**
	 * Recherche les banques dont le nom est égal au paramètre.
	 * 
	 * @param nom pour lequel on recherche des banques.
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.nom = ?1")
	List<Banque> findByNom(String nom);

	/**
	 * Recherche les banques dont l'identification est égale au paramètre.
	 * 
	 * @param identification pour laquelle on recherche des banques.
	 * @return une liste banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.identification = ?1")
	List<Banque> findByIdentification(String identification);

	/**
	 * Recherche les banques qui autorisent les cross ref pour les patients.
	 * 
	 * @param autoriseCrossPatient .
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.autoriseCrossPatient = ?1")
	List<Banque> findByAutoriseCrossPatient(boolean autoriseCrossPatient);

	/**
	 * Recherche les banques archivées.
	 * 
	 * @param archive .
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.archive = ?1")
	List<Banque> findByArchive(boolean archive);

	/**
	 * Recherche les banques dont le collaborateur est passé en paramètre.
	 * 
	 * @param collaborateur pour lequel on recherche des banques.
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.collaborateur = ?1")
	List<Banque> findByCollaborateur(Collaborateur c);

	/**
	 * Recherche les banques dont le propriétaire est passé en paramètre.
	 * 
	 * @param service proprietaire pour lequel on recherche des banques.
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.proprietaire = ?1")
	List<Banque> findByProprietaire(Service proprietaire);

	/**
	 * Recherche les banques dont la plateforme est passée en paramètre. Filtre sur
	 * le statut archivé.
	 * 
	 * @param plateforme pour laquelle on recherche des banques.
	 * @param archive    true/false
	 * @return une liste de banques.
	 * @version 2.1
	 */
	@Query("SELECT b FROM Banque b WHERE b.plateforme = ?1 AND b.archive = ?2 ORDER BY b.nom")
	List<Banque> findByPlateformeAndArchive(Plateforme plateforme, boolean archive);

	/**
	 * Recherche la banque dont l'identifiant passé en paramètre. Les associations
	 * avec les tables COLLABORATEUR, SERVICE ET PLATEFORME seront chargées par
	 * l'intermédiaire d'un fetch.
	 * 
	 * @param banqueId est l'identifiant du collaborateur recherché.
	 * @return un collaborateur.
	 */
	@Query("SELECT b FROM Banque b LEFT JOIN FETCH b.collaborateur LEFT JOIN FETCH b.proprietaire "
			+ "LEFT JOIN FETCH b.plateforme WHERE b.banqueId = ?1")
	List<Banque> findByIdWithFetch(Integer banqueId);

	/**
	 * Recherche toutes les Banques ordonnées.
	 * 
	 * @return Liste ordonnée de Banques.
	 */
	@Query("SELECT b FROM Banque b ORDER BY b.nom")
	List<Banque> findByOrder();

	/**
	 * Trouve les catalogues associés au contexte auquel la banque appartient.
	 * 
	 * @param banqueId
	 * @return list Catalogue
	 */
	@Query("SELECT b.contexte.catalogues FROM Banque b WHERE b.banqueId = ?1")
	List<Catalogue> findContexteCatalogues(Integer banqueId);

	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit de consultation
	 * sur l'entité spécifiée pour la plateforme spécifiée.
	 * 
	 * @param user
	 * @param entite
	 * @param pf
	 * @return list BANQUE
	 */
	@Query("SELECT b FROM Banque b join b.profilUtilisateurs as profilU join profilU.pk.profil as profil "
			+ "join profil.droitObjets as droit WHERE b.plateforme = ?3 AND profilU.pk.utilisateur = ?1 "
			+ "AND droit.pk.entite = ?2 AND droit.pk.operationType.nom = 'Consultation' AND b.archive = 0")
	List<Banque> findByEntiteConsultByUtilisateur(Utilisateur usr, Entite entite, Plateforme pf);

	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit de modification
	 * sur l'entité spécifiée pour la plateforme spécifiée.
	 * 
	 * @param user
	 * @param entite
	 * @param pf
	 * @return list BANQUE
	 */
	@Query("SELECT b FROM Banque b join b.profilUtilisateurs as profilU join profilU.pk.profil as profil "
			+ "join profil.droitObjets as droit WHERE b.plateforme = ?3 AND profilU.pk.utilisateur = ?1 "
			+ "AND droit.pk.entite = ?2 AND droit.pk.operationType.nom = 'Modification' AND b.archive = 0 "
			+ "ORDER by b.nom")
	List<Banque> findByEntiteModifByUtilisateur(Utilisateur usr, Entite entite, Plateforme pf);

	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit d'administrateur
	 * pour une plateforme passée en paramètres.
	 * 
	 * @return liste de BANQUE
	 */
	@Query("SELECT distinct b FROM Banque b left join b.profilUtilisateurs as profilU "
			+ "left join profilU.pk.profil as profil join b.plateforme.utilisateurs as us WHERE b.plateforme = ?2 "
			+ "AND b.archive = 0 AND ((profilU.pk.utilisateur = ?1 AND profil.admin=1) OR us = ?1) " + "ORDER BY b.nom")
	List<Banque> findByUtilisateurIsAdmin(Utilisateur usr, Plateforme pf);

	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit en fonction d'une
	 * plateforme.
	 */
	@Query("SELECT distinct b FROM Banque b left join b.profilUtilisateurs as profilU "
			+ "left join profilU.pk.profil as profil join b.plateforme.utilisateurs as us WHERE b.plateforme = ?2 "
			+ "AND b.archive = 0 AND (profilU.pk.utilisateur = ?1 OR us = ?1) ORDER BY b.nom")
	List<Banque> findByUtilisateurAndPF(Utilisateur usr, Plateforme pf);

	/**
	 * Recherche toutes les banques sauf celle dont l'id est passé en paramètre.
	 * 
	 * @param banqueId Identifiant de la abnque que l'on souhaite exclure de la
	 *                 liste retournée.
	 * @return une liste de banques.
	 */
	@Query("SELECT b FROM Banque b WHERE b.banqueId != ?1")
	List<Banque> findByExcludedId(Integer banqueId);

	/**
	 * Recherche toutes les banques sur lesquelles l'utilisateur passé en paramètre
	 * a un profil assigné. Les banques sont retournées ordonnées par leur nom.
	 * 
	 * @param u Utilisateur
	 * @return liste de banques
	 */
	@Query("SELECT b FROM Banque b join b.profilUtilisateurs as profilU join profilU.pk.profil as profil "
			+ "WHERE profilU.pk.utilisateur = ?1 ORDER BY b.nom")
	List<Banque> findByProfilUtilisateur(Utilisateur u);

	/**
	 * Recherche toutes les banques pour auxquelles la table d'annotation a été
	 * passée en paramètres.
	 * 
	 * @param table TableAnnotation
	 * @return liste de banques
	 */
	@Query("SELECT b FROM Banque b JOIN b.tableAnnotationBanques t WHERE t.pk.tableAnnotation = ?1")
	List<Banque> findByTableAnnotation(TableAnnotation table);

	/**
	 * Recherche toutes les banques donnant l'accès au conteneur passé en paramètre.
	 * 
	 * @param conteneur
	 * @return liste de banques
	 * @since 2.2.1
	 */
	@Query("SELECT b FROM Banque b JOIN b.conteneurs c WHERE c = ?1")
	List<Banque> findByConteneur(Conteneur cont);
}
