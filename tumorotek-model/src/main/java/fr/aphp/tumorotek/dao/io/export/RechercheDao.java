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
package fr.aphp.tumorotek.dao.io.export;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine Recherche. Interface créée le
 * 25/02/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.3
 *
 */
@Repository
public interface RechercheDao extends CrudRepository<Recherche, Integer> {

	/**
	 * Recherche les Recherches d'un utilisateur.
	 * 
	 * @param utilisateur : utilisateur dont on veut connaître les Recherches.
	 * @return la liste des Recherches de l'utilisateur
	 */
	@Query("SELECT r FROM Recherche r WHERE " + "r.createur = ?1")
	List<Recherche> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Recherche les Recherches d'une banque.
	 * 
	 * @param banqueId Id de la banque dont on veut connaître les Recherches.
	 * @return la liste des Recherches de la banque.
	 */
	@Query("SELECT r FROM Recherche r " + "left join r.banques b " + "WHERE b.banqueId = ?1")
	List<Recherche> findByBanqueId(Integer banqueId);

	/**
	 * Recherche les Recherches pour plusieurs Banques.
	 * 
	 * @param banquesId Liste d'Ids de Banques dont on veut connaître les
	 *                  Recherches.
	 * @return la liste des Recherches des Banques.
	 */
	@Query("SELECT distinct(r) FROM Recherche r " + "left join r.banques b " + "WHERE b.banqueId in (?1)")
	List<Recherche> findByBanqueIdinList(List<Integer> banquesId);

	/**
	 * Recherche les Recherches d'un intitulé.
	 * 
	 * @param intitule : intitulé dont on veut connaître les Recherches.
	 * @return la liste des Recherches de l'intitulé
	 */
	@Query("SELECT r FROM Recherche r " + "WHERE r.intitule like ?1")
	List<Recherche> findByIntitule(String intitule);

	/**
	 * Recherche les Recherches d'un utilisateur par intitulé.
	 * 
	 * @param intitulé    : intitulé dont on veut connaître les Recherches.
	 * @param utilisateur : Utilisateur ayant créé les Recherches.
	 * @return la liste des Recherches de l'intitulé
	 */
	@Query("SELECT r FROM Recherche r " + "WHERE r.intitule like ?1 " + "AND r.createur = ?2")
	List<Recherche> findByIntituleUtilisateur(String intitule, Utilisateur utilisateur);

	/**
	 * Recherche les Recherches à partir d'un Affichage.
	 * 
	 * @param affichage : Affichage dont on veut connaître les Recherches.
	 * @return la liste des Recherches d'un affichage
	 */
	@Query("SELECT r FROM Recherche r " + "WHERE r.affichage = ?1")
	List<Recherche> findByAffichage(Affichage affichage);

	/**
	 * Recherche les Recherches à partir d'une Requête.
	 * 
	 * @param requete : Requête dont on veut connaître les Recherches.
	 * @return la liste des Recherches d'une Requête
	 */
	@Query("SELECT r FROM Recherche r " + "WHERE r.requete = ?1")
	List<Recherche> findByRequete(Requete requete);

	/**
	 * Recherche toutes les Recherches, sauf celle dont l'id est passé en paramètre.
	 * 
	 * @param rechercheId Identifiant de la Recherche que l'on souhaite exclure de
	 *                    la liste retournée.
	 * @return une liste de Recherches.
	 */
	@Query("SELECT r FROM Recherche r " + "WHERE r.rechercheId != ?1")
	List<Recherche> findByExcludedId(Integer rechercheId);

}
