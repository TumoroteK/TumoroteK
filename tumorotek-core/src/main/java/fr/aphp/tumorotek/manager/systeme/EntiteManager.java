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
package fr.aphp.tumorotek.manager.systeme;

import java.util.List;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine Entite. Interface créée le
 * 30/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EntiteManager {

	/**
	 * Recherche une entité dont l'identifiant est passé en paramètre.
	 * 
	 * @param entiteId Identifiant du type que l'on recherche.
	 * @return Une Entite.
	 */
	Entite findByIdManager(Integer entiteId);

	/**
	 * Recherche toutes les entités présentes dans la base.
	 * 
	 * @return Liste de Entite.
	 */
	List<Entite> findAllObjectsManager();

	/**
	 * Recherche toutes les entités dont le nom est passée en paramètre.
	 * 
	 * @param nom Nom de l'entité que l'on recherche.
	 * @return Liste de Entite.
	 */
	List<Entite> findByNomManager(String nom);

	/**
	 * Recherche toutes les entités annotables.
	 * 
	 * @return Liste de Entite.
	 */
	List<Entite> findAnnotablesManager();

	/**
	 * Manager qui renvoie l'objet correspondant à l'entité et à l'objectId passés
	 * en paramètres.
	 * 
	 * @param entite   Classe de l'objet que l'on recherche.
	 * @param objectId Identifiant de l'objet que l'on recherche.
	 * @return Une instance de la classe dont le nom correspond à l'entité passée en
	 *         paramètre.
	 */
	Object findObjectByEntiteAndIdManager(Entite entite, Integer objectId);

	/**
	 * Manager qui renvoie les ids des objets après filtrage par les banques. Filtre
	 * sur les banques si paramètre non null et non vide.
	 * 
	 * @param entite Classe de l'objet que l'on recherche.
	 * @param liste  objectId Identifiant les objet que l'on recherche.
	 * @param filtre sur les banques
	 * @return Une instance de la classe dont le nom correspond à l'entité passée en
	 *         paramètre.
	 * @since 2.0.10
	 */
	List<Integer> findIdsByEntiteAndIdAfterBanqueFiltreManager(Entite entite, List<Integer> objectId,
			List<Banque> banks);

}
