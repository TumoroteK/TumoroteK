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

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le DAO du bean de domaine Affichage. Interface créée le
 * 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.3
 */
@Repository
public interface AffichageDao extends CrudRepository<Affichage, Integer> {

	/**
	 * Recherche les Affichages d'un Utilisateur.
	 * 
	 * @param utilisateur : Utilisateur dont on veut connaître les Affichages.
	 * @return la liste des Affichages de l'Utilisateur
	 */
	@Query("SELECT a FROM Affichage a WHERE " + "a.createur = ?1")
	List<Affichage> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Recherche les Affichages d'une Banque.
	 * 
	 * @param banque Banque dont on veut connaître les Affichages.
	 * @return la liste des Affichages de la Banque.
	 */
	@Query("SELECT a FROM Affichage a WHERE " + "a.banque = ?1 " + "ORDER BY a.intitule")
	List<Affichage> findByBanque(Banque banque);

	/**
	 * Recherche les Affichages pour plusieurs Banques.
	 * 
	 * @param banques Liste de Banques dont on veut connaître les Affichages.
	 * @return la liste des Affichages des Banques.
	 */
	@Query("SELECT a FROM Affichage a WHERE " + "a.banque in (?1) " + "ORDER BY a.intitule")
	List<Affichage> findByBanqueInList(List<Banque> banques);

	/**
	 * Recherche les Affichages d'un intitulé.
	 * 
	 * @param intitulé : intitulé dont on veut connaître les Affichages.
	 * @return la liste des Affichages de l'intitulé
	 */
	@Query("SELECT a FROM Affichage a " + "WHERE a.intitule like ?1")
	List<Affichage> findByIntitule(String intitule);

	/**
	 * Recherche les Affichages d'un utilisateur par intitulé.
	 * 
	 * @param intitulé    : intitulé dont on veut connaître les Affichages.
	 * @param utilisateur : Utilisateur ayant créé les Affichages.
	 * @return la liste des Affichages de l'intitulé
	 */
	@Query("SELECT a FROM Affichage a " + "WHERE a.intitule like ?1 " + "AND a.createur = ?2")
	List<Affichage> findByIntituleUtilisateur(String intitule, Utilisateur utilisateur);

	/**
	 * Recherche tous les Affichages, sauf celui dont l'id est passé en paramètre.
	 * 
	 * @param affichageId Identifiant de l'Affichage que l'on souhaite exclure de la
	 *                    liste retournée.
	 * @return une liste d'Affichages.
	 */
	@Query("SELECT a FROM Affichage a " + "WHERE a.affichageId != ?1")
	List<Affichage> findByExcludedId(Integer affichageId);
}
