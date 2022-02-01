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

import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

/**
 *
 * Interface pour le manager du bean de domaine CouleurEntiteType. Interface
 * créée le 30/04/10.
 *
 * @author Pierre Ventadour
 * @version 2.3
 *
 */
public interface CouleurEntiteTypeManager {

	/**
	 * Recherche une CouleurEntiteType dont l'identifiant est passé en paramètre.
	 * 
	 * @param couleurEntiteTypeId Identifiant de la CouleurEntiteType que l'on
	 *                            recherche.
	 * @return Une CouleurEntiteType.
	 */
	CouleurEntiteType findByIdManager(Integer couleurEntiteTypeId);

	/**
	 * Recherche toutes les CouleurEntiteTypes présentes dans la base.
	 * 
	 * @return Liste de CouleurEntiteTypes.
	 */
	List<CouleurEntiteType> findAllObjectsManager();

	/**
	 * Recherche les couleurs définies pour une banque.
	 * 
	 * @param banque Banque pour laquelle on cherche des couleurs.
	 * @return Liste de CouleurEntiteType.
	 */
	List<CouleurEntiteType> findAllObjectsByBanqueManager(Banque banque);

	/**
	 * Recherche les couleurs pour les EchantillonType d'une banque.
	 * 
	 * @param banque Banque pour laquelle on cherche des couleurs.
	 * @return Liste de CouleurEntiteType.
	 */
	List<CouleurEntiteType> findAllCouleursForEchanTypeByBanqueManager(Banque banque);

	/**
	 * Recherche les couleurs pour les ProdType d'une banque.
	 * 
	 * @param banque Banque pour laquelle on cherche des couleurs.
	 * @return Liste de CouleurEntiteType.
	 */
	List<CouleurEntiteType> findAllCouleursForProdTypeByBanqueManager(Banque banque);

	/**
	 * Recherche les doublons de l'Incident passé en paramètre.
	 * 
	 * @param incident Incident pour lequel on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(CouleurEntiteType couleurEntiteType);

	/**
	 * Persist une instance de CouleurEntiteType dans la base de données.
	 * 
	 * @param couleurEntiteType Nouvelle instance de l'objet à créer.
	 */
	void createObjectManager(CouleurEntiteType couleurEntiteType, Couleur couleur, Banque banque,
			EchantillonType echantillonType, ProdType prodType);

	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * 
	 * @param couleurEntiteType Objet à persister.
	 */
	void updateObjectManager(CouleurEntiteType couleurEntiteType, Couleur couleur, Banque banque,
			EchantillonType echantillonType, ProdType prodType);

	/**
	 * Supprime un Incident de la base de données.
	 * 
	 * @param incident Incident à supprimer de la base de données.
	 */
	void removeObjectManager(CouleurEntiteType couleurEntiteType);

}
