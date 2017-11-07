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
package fr.aphp.tumorotek.manager.utilisateur;

import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.Profil;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le manager du bean de domaine Profil.
 * Interface créée le 20/05/2010.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface ProfilManager {

	/**
	 * Recherche un Profil dont l'identifiant est passé en paramètre.
	 * @param profilId Identifiant du Profil que l'on recherche.
	 * @return Un Profil.
	 */
	Profil findByIdManager(Integer profilId);
	
	/**
	 * Recherche tous les Profils présents dans la base, ordonnés
	 * par nom.
	 * @return Liste de Profils.
	 */
	List<Profil> findAllObjectsManager();
	
	/**
	 * Recherche les doublons du Profil passé en paramètre.
	 * @param profil Un Profil pour lequel on cherche des doublons.
	 * @return True s'il existe des doublons.
	 */
	Boolean findDoublonManager(Profil profil);
	
	/**
	 * Test si un Profil est lié à des utilisateurs.
	 * @param profil Profil que l'on souhaite tester.
	 * @return Vrai si le Profil est utilisé.
	 */
	Boolean isUsedObjectManager(Profil profil);
	
	/**
	 * Persist une instance de Profil dans la base de données.
	 * @param profil Nouvelle instance de l'objet à créer.
	 * @param droitObjets Liste de DroitObjet
	 * @param pf Plateforme
	 * @version 2.1
	 */
	void createObjectManager(Profil profil, 
			List<DroitObjet> droitObjets,
			Utilisateur admin, Plateforme pf);
	
	/**
	 * Sauvegarde les modifications apportées à un objet persistant.
	 * @param profil Objet à mettre à jour dans la base.
	 * @param droitObjets Liste de DroitObjet.
	 * @param admin Utilisateur créant le profil.
	 */
	void updateObjectManager(Profil profil, 
			List<DroitObjet> droitObjets,
			Utilisateur admin);
	
	/**
	 * Supprime un Profil de la base de données.
	 * @param profil Profil à supprimer de la base de données.
	 * @param admin Utilisateur modifiant le profil.
	 */
	void removeObjectManager(Profil profil);

	/**
	 * Recherche les profils pour une plateforme donnée 
	 * selon leur statut archivé. 
	 * Si archive = null, renvoie tous les profils pour la pf.
	 * @param pf Plateforme
	 * @param archive boolean
	 * @return liste Profil
	 * @since 2.1
	 */
	List<Profil> findByPlateformeAndArchiveManager(Plateforme pf, Boolean archive);
	
}
