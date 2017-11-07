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
package fr.aphp.tumorotek.manager.context;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le manager du bean de domaine Banque.
 * Interface créée le 01/10/09.
 * 
 * Modifié le 31/01/2013 pour ajouter la gestion des tables annotations de 
 * Cession au travers de la banque.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface BanqueManager {

	/**
	 * Recherche une Banque dont l'identifiant est passé en paramètre.
	 * @param banqueId Identifiant de la banque que l'on recherche.
	 * @return Une Banque.
	 */
	Banque findByIdManager(Integer banqueId);
	
	/**
	 * Recherche toutes les banques présentes dans la base.
	 * @return Liste de Banques.
	 */
	List<Banque> findAllObjectsManager();
	
	/**
	 * Recherche les prélèvements liés à la banque passée en paramètre.
	 * @param banque Banque pour laquellle on recherche des prélèvements.
	 * @return Liste de Prelevements.
	 */
	Set<Prelevement> getPrelevementsManager(Banque banque);
	
	/**
	 * Recherche les échantillons liés à la banque passée en paramètre.
	 * @param banque Banque pour laquellle on recherche des échantillons.
	 * @return Liste d'Echantillons.
	 */
	Set<Echantillon> getEchantillonsManager(Banque banque);
	
	/**
	 * Recherche les produit dérivés liés à la banque passée en paramètre.
	 * @param banque Banque pour laquellle on recherche des produits dérivés.
	 * @return Liste de ProdDerives.
	 */
	Set<ProdDerive> getProdDerivesManager(Banque banque);
	
	/**
	 * Recherche les Services de stockage liés à la banque passée en paramètre.
	 * @param banque Banque pour laquellle on recherche des produits dérivés.
	 * @return Liste de Services.
	 */
	Set<Service> getServicesStockageManager(Banque banque);
	
	/**
	 * Trouve les catalogues associés au contexte auquel la banque
	 * appartient.
	 * @param banqueId
	 * @return list Catalogue
	 */
	List<Catalogue> findContexteCataloguesManager(Integer banqueId);
	
	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit de
	 * consultation sur l'entité spécifiée pour la plateforme spécifiée. 
	 * Tient compte des banques sur lesquelles l'utilisateur a un droit d'admin.
	 * @param utilisateur
	 * @param entite
	 * @param plateforme
	 * @version 2.0.13
	 */
	List<Banque> findByEntiteConsultByUtilisateurManager(Utilisateur usr, 
											Entite entite, Plateforme pf);
	
	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit de
	 * modification sur l'entité spécifiée pour une plateforme spécifiée.
	 * Tient compte des banques sur lesquelles l'utilisateur 
	 * a un droit d'admin.
	 * @param utilisateur
	 * @param entite
	 * @param plateforme
	 */
	List<Banque> findByEntiteModifByUtilisateurManager(Utilisateur usr, 
											Entite entite, Plateforme pf);
	
	/**
	 * Trouve les banques définissant un 'autoriseCrossPatient' true ou 
	 * false. 
	 * @param autoriseCrossPatient
	 */
	List<Banque> findByAutoriseCrossPatientManager(boolean cross);
	
	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit
	 * d'administrateur pour la plateforme spécifiée.
	 * @param user
	 * @param plateforme
	 */
	List<Banque> findByUtilisateurIsAdminManager(Utilisateur usr, 
													Plateforme pf);
	
	/**
	 * Trouve les banques pour lesquelles l'utilisateur a un droit
	 * en fonction d'une plateforme.
	 */
	List<Banque> findByUtilisateurAndPFManager(Utilisateur usr,
			Plateforme pf);

	/**
	 * Trouve les banques appartenant à la plateforme, avec un filtre 
	 * sur le statut archivé d'une banque.
	 * Renvoie toutes les banques si filtre archive = null
	 * @param pltf
	 * @param archive
	 * @return liste de banques.
	 * @version 2.1
	 */
	List<Banque> findByPlateformeAndArchiveManager(Plateforme pltf, Boolean archive);
	
	/**
	 * Enregistre ou modifie un objet Banque dans la base de données.
	 * @param banque 
	 * @param plateforme
	 * @param service Service propriétaire
	 * @param responsable Collaborateur 
	 * @param conteneurs Liste de conteneurs associés
	 * @param codifications Liste de codifications associées
	 * @param tables Liste de TableAnnotations Patient associées
	 * @param tables Liste de TableAnnotations Prelevement associées
	 * @param tables Liste de TableAnnotations Echantillon associées
	 * @param tables Liste de TableAnnotations Derives associées
	 * @param tables Liste de TableAnnotations Cession associées
	 * @param coulTypes Liste de CouleurEntiteType associées
	 * @param couleur associée au stockage échantillon
	 * @param couleur associée au stockage dérivés
	 * @param utilisateur Utilisateur réalisant l'opération
	 * @param operation creation/modification
	 * @param base directory pour créer dossiers sytèmes associés.
	 */
	void createOrUpdateObjectManager(Banque banque, Plateforme pf,
			Contexte contexte, Service service, Collaborateur responsable,
			Collaborateur contact, List<Conteneur> conteneurs, 
			List<BanqueTableCodage> codifications,
			List<TableAnnotation> tablesPatient,
			List<TableAnnotation> tablesPrlvt,
			List<TableAnnotation> tablesEchan,
			List<TableAnnotation> tablesDerive,
			List<TableAnnotation> tablesCess,
			List<CouleurEntiteType> coulTypes,
			Couleur couleurEchan, 
			Couleur couleurDerive,
			Utilisateur utilisateur,
			String operation,
			String basedir);

	/**
	 * Recherche la présence de doublon d'un objet Banque.
	 * @param banque
	 * @return true si doublon
	 */
	boolean findDoublonManager(Banque banque);

	/**
	 * Supprime la banque et ses opérations associées du système.
	 * @param comments commentaires liés à la suppression
	 * @param Utilisateur réalisant la suppression.
	 * @param base directory pour effacer dossiers sytèmes associés.
	 * @param boolean force true pour forcer la suppression de la banque.
	 */
	void removeObjectManager(Banque banque, String comments, Utilisateur user,
											String basedir, boolean force);

//	/**
//	 * Recherche les codifications liées à la banque passée en paramètre.
//	 * @param banque Banque pour laquelle on recherche des codifications.
//	 * @return Liste de TableCodage.
//	 */
//	Set<TableCodage> getTablesCodageManager(Banque banque);

	/**
	 * Recherche les conteneurs liés à la banque passée en paramètre.
	 * @param banque Banque pour laquelle on recherche des conteneurs.
	 * @return Liste de Conteneur.
	 */
	Set<Conteneur> getConteneursManager(Banque banque);

	
	/**
	 * Recherche toutes les banques sur lesquelles l'utilisateur 
	 * passé en paramètre a un profil assigné. Les banques sont 
	 * retournées ordonnées par leur nom.
	 * @param u Utilisateur
	 * @return liste de banques 
	 */
	List<Banque> findByProfilUtilisateurManager(Utilisateur u);
	
	/**
	 * Recherche toutes les associations entre une banque et les tables de 
	 * codifications qui lui ont été associées.
	 * @param banque
	 * @return liste BanqueTableCodage.
	 */
	List<BanqueTableCodage> getBanqueTableCodageByBanqueManager(Banque banque);
	
	/**
	 * Recherche les imprimantes liées à la banque passée en paramètre.
	 * @param banque Banque pour laquelle on recherche des imprimantes.
	 * @return Set d'Imrpimantes.
	 */
	Set<Imprimante> getImprimantesManager(Banque banque);

	/**
	 * Vérifie si la banque est référencée par du matériel bioogique ou 
	 * de tracabilité (prélèvements, échantillons, dérivés, cessions).
	 * Si c'est le cas la banque n'est pas supprimable.
	 * @param bank
	 * @return true 
	 */
	boolean isReferencedObjectManager(Banque bank);
	
	/**
	 * Trouve une liste de banque d'accueil possible à un utilisateur 
	 * pour la migration du  prelevement passe en paramètre. 
	 * Les banques doivent être administrées par l'utilisateur 
	 * et partager les conteneurs des emplacements attribués 
	 * aux objets issus du prelevement.
	 * @param p Prelevement
	 * @param utilisateur 
	 * @return liste de Banque
	 */
	List<Banque> findBanqueForSwitchManager(Prelevement p, Utilisateur u); 
}
