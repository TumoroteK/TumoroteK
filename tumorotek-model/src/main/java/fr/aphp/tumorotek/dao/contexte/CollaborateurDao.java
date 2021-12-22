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

import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.contexte.Titre;

/**
 *
 * Interface pour le DAO du bean de domaine Collaborateur.
 *
 * Date: 09/09/2009
 *
 * @author Pierre Ventadour
 * @version 2.3
 */
@Repository
public interface CollaborateurDao extends CrudRepository<Collaborateur, Integer> {

	/**
	 * Recherche tous les collaborateurs ordonnés par nom.
	 * 
	 * @return une liste ordonnée de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c ORDER BY c.nom")
	List<Collaborateur> findByOrder();

	/**
	 * Recherche les collaborateurs dont le nom est égal au paramètre.
	 * 
	 * @param nom pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.nom like ?1")
	List<Collaborateur> findByNom(String nom);

	/**
	 * Recherche les collaborateurs dont le prénom est égal au paramètre.
	 * 
	 * @param prenom pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.prenom like ?1")
	List<Collaborateur> findByPrenom(String prenom);

	/**
	 * Recherche les collaborateurs archivés.
	 * 
	 * @param archive .
	 * @return une liste de collaborateurs ordonnée.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.archive = ?1 ORDER BY c.nom, c.prenom")
	List<Collaborateur> findByArchive(boolean archive);

	// /**
	// * Recherche les collaborateurs dont les coordonnées sont
	// * passées en paramètre.
	// * @param coordonneeId pour lesquelles on recherche des collaborateurs.
	// * @return une liste de collaborateurs.
	// */
	// List<Collaborateur> findByCoordonneeId(Integer coordonneeId);

	/**
	 * Recherche tous les collaborateurs sauf celui dont l'id est passé en
	 * paramètre.
	 * 
	 * @param collaborateurId Identifiant du collaborateur que l'on souhaite exclure
	 *                        de la liste retournée.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.collaborateurId != ?1")
	List<Collaborateur> findByExcludedId(Integer collaborateurId);

	/**
	 * Recherche les collaborateurs dont l'établissement est passé en paramètre.
	 * 
	 * @param etablissement Etablissement pour lequel on recherche des
	 *                      collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.etablissement = ?1")
	List<Collaborateur> findByEtablissement(Etablissement etablissement);

	/**
	 * Recherche les collaborateurs ordonnées par nom dont l'établissement est passé
	 * en paramètre.
	 * 
	 * @param etablissement Etablissement pour lequel on recherche des
	 *                      collaborateurs.
	 * @return une liste ordonnée de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.etablissement = ?1 ORDER BY c.nom, c.prenom")
	List<Collaborateur> findByEtablissementWithOrder(Etablissement etablissement);

	/**
	 * Recherche les collaborateurs ordonnées par nom dont l'établissement est passé
	 * en paramètre et sont archivés ou non.
	 * 
	 * @param etablissement Etablissement pour lequel on recherche des
	 *                      collaborateurs.
	 * @param archive       True si le collaborateur est inactif.
	 * @return une liste ordonnée de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.etablissement = ?1 AND c.archive = ?2 " + "ORDER BY c.nom, c.prenom")
	List<Collaborateur> findByEtablissementArchiveWithOrder(Etablissement etablissement, boolean archive);

	/**
	 * Recherche les collaborateurs dont la spécialité est passée en paramètre.
	 * 
	 * @param specialite pour laquelle on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.specialite = ?1")
	List<Collaborateur> findBySpecialite(Specialite spec);

	/**
	 * Recherche les collaborateurs dont le titre est passé en paramètre.
	 * 
	 * @param titre pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c WHERE c.titre = ?1")
	List<Collaborateur> findByTitre(Titre t);

	/**
	 * Recherche les collaborateurs dont un service est passé en paramètre.
	 * 
	 * @param serviceId pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.services s WHERE s.serviceId = ?1")
	List<Collaborateur> findByServiceId(Integer serviceId);

	/**
	 * Recherche les collaborateurs dont un service est passé en paramètre.
	 * 
	 * @param serviceId pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT count(c) FROM Collaborateur c left join c.services s WHERE s.serviceId = ?1")
	List<Long> findCountByServiceId(Integer serviceId);

	/**
	 * Recherche les collaborateurs ordonnés par nom et prénom dont un service est
	 * passé en paramètre.
	 * 
	 * @param serviceId pour lequel on recherche des collaborateurs.
	 * @return une liste ordonnée de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.services s WHERE s.serviceId = ?1 " + "ORDER BY c.nom, c.prenom")
	List<Collaborateur> findByServiceIdWithOrder(Integer serviceId);

	/**
	 * Recherche les collaborateurs ordonnés par nom et prénom dont un service est
	 * passé en paramètre qui sont archivés ou non.
	 * 
	 * @param serviceId pour lequel on recherche des collaborateurs.
	 * @param archive   True si le collaborateur est inactif.
	 * @return une liste ordonnée de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.services s WHERE s.serviceId = ?1 AND c.archive = ?2 "
			+ "ORDER BY c.nom, c.prenom")
	List<Collaborateur> findByServiceIdArchiveWithOrder(Integer serviceId, boolean archive);

	/**
	 * Recherche les collaborateurs dont une plateforme est passée en paramètre.
	 * 
	 * @param plateformeId pour laquelle on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.plateformes p WHERE p.plateformeId = ?1")
	List<Collaborateur> findByPlateformeId(Integer plateformeId);

	/**
	 * Recherche les collaborateurs dont une banque est passée en paramètre.
	 * 
	 * @param banqueId pour laquelle on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.banques b WHERE b.banqueId = ?1")
	List<Collaborateur> findByBanqueId(Integer banqueId);

	/**
	 * Recherche les collaborateurs dont un utilisateur est passé en paramètre.
	 * 
	 * @param utilisateurId pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.utilisateurs u WHERE u.utilisateurId = ?1")
	List<Collaborateur> findByUtilisateurId(Integer utilisateurId);

	/**
	 * Recherche le collaborateur dont l'identifiant passé en paramètre. Les
	 * associations avec les tables ETABLISSEMENT, COORDONNEE, SPECIALITE, TITRE
	 * seront chargées par l'intermédiaire d'un fetch.
	 * 
	 * @param collaborateurId est l'identifiant du collaborateur recherché.
	 * @return un collaborateur.
	 */
	@Query("SELECT c FROM Collaborateur c LEFT JOIN FETCH c.etablissement LEFT JOIN FETCH c.coordonnees "
			+ "LEFT JOIN FETCH c.specialite LEFT JOIN FETCH c.titre WHERE c.collaborateurId = ?1")
	List<Collaborateur> findByIdWithFetch(Integer collaborateurId);

	/**
	 * Recherche les collaborateurs qui n'ont pas de services.
	 * 
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left JOIN c.services s WHERE s is null")
	List<Collaborateur> findByCollaborateurWithoutService();

	/**
	 * Recherche les collaborateurs qui n'ont pas de services et qui sont archivés
	 * ou non.
	 * 
	 * @param archive True si le collaborateur est inactif.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left JOIN c.services s WHERE s is null AND c.archive = ?1")
	List<Collaborateur> findByCollaborateurWithoutServiceAndArchive(boolean archive);

	/**
	 * Recherche les collaborateurs qui n'ont pas de services pour un établissement
	 * donné.
	 * 
	 * @param établissement
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left JOIN c.services s WHERE s is null AND c.etablissement = ?1")
	List<Collaborateur> findByEtablissementNoService(Etablissement etab);

	/**
	 * Recherche les collaborateurs dont l'etablissement est passé en paramètre.
	 * 
	 * @param serviceId pour lequel on recherche des collaborateurs.
	 * @return une liste de collaborateurs.
	 */
	@Query("SELECT count(c) FROM Collaborateur c WHERE c.etablissement = (?1)")
	List<Long> findCountByEtablissement(Etablissement etal);

	/**
	 * Recherche une liste de collaborateurs dont la ville contient le nom est passé
	 * en paramètre.
	 * 
	 * @param non de la ville pour laquelle on recherche des Collaborateurs.
	 * @return Liste de Collaborateurs.
	 */
	@Query("SELECT c FROM Collaborateur c left join c.coordonnees s WHERE s.ville like ?1 ORDER BY c.nom")
	List<Collaborateur> findByVille(String ville);

}
