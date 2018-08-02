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
package fr.aphp.tumorotek.manager.coeur.cession;

import java.util.List;

import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.ECederObjetStatut;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Interface pour le manager du bean de domaine CederObjet.
 * Interface créée le 29/01/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0.10
 *
 */
public interface CederObjetManager
{

   /**
    * Recherche un CederObjet dont l'identifiant est passé en paramètre.
    * @param cederObjetPK Identifiant du CederObjet que l'on recherche.
    * @return Un CederObjet.
    */
   CederObjet findByIdManager(CederObjetPK cederObjetPK);

   /**
    * Recherche tous les CederObjets présents dans la base.
    * @return Liste de CederObjets.
    */
   List<CederObjet> findAllObjectsManager();

   /**
    * Recherche les CederObjets sauf celui dont la clé primaire est
    * passée en paramètre.
    * @param pk CederObjetPK.
    * @return une liste de CederObjets.
    */
   List<CederObjet> findByExcludedPKManager(CederObjetPK pk);

   /**
    * Recherche les CederObjets dont l'entité est égale au paramètre.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjets.
    */
   List<CederObjet> findByEntiteManager(Entite entite);

   /**
    * Recherche les tous CederObjets de type Echantillon.
    * @return une liste de CederObjets.
    */
   List<CederObjet> getAllEchantillonsCederObjetsManager();

   /**
    * Recherche tous les CederObjets de type ProdDerive.
    * @return une liste de CederObjets.
    */
   List<CederObjet> getAllProdDerivesCederObjetsManager();

   /**
    * Recherche tous les CederObjet pour l'objet passé en paramètre.
    * @param obj Objet pour lequel on recherche des CederObjets.
    * @return Liste ordonnée de CederObjets.
    */
   List<CederObjet> findByObjetManager(Object obj);
   
   /**
    * Recherche tous les CederObjet pour l'objet passé en paramètre dans un statut précis
    * @param obj Objet pour lequel on recherche des CederObjets.
    * @param statut statut de l'objet cédé
    * @return Liste ordonnée de CederObjets.
    */
   List<CederObjet> findByObjetAndStatutManager(Object obj, ECederObjetStatut statut);

   /**
    * Recherche toutes les Cessions pour l'objet passé en paramètre
    * et qui respecte le statut passé en paramètres.
    * @param statut Statut des cessions recherchées.
    * @param obj Objet pour lequel on recherche des Cessions.
    * @return Liste ordonnée de Cessions.
    */
   List<Cession> getAllCessionsByStatutAndObjetManager(String statut, Object obj);

   /**
    * Recherche les CederObjets dont la cession et l'entité sont
    * passées en paramètres.
    * @param cession Cession des CederObjets recherchés.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   List<CederObjet> findByCessionEntiteManager(Cession cession, Entite entite);

   /**
    * Recherche les CederObjets de type échantillon dont la cession est
    * passée en paramètre.
    * @param cession Cession des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   List<CederObjet> getEchantillonsCedesByCessionManager(Cession cession);

   /**
    * Recherche les CederObjets de type ProdDerive dont la cession est
    * passée en paramètre.
    * @param cession Cession des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   List<CederObjet> getProdDerivesCedesByCessionManager(Cession cession);

   /**
    * Recherche les doublons du CederObjet passé en paramètre.
    * @param cederObjet CederObjet pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(CederObjet cederObjet);

   /**
    * Vérifie que la clé PK du CederObjet est valide.
    * @param cederObjet CederObjet que l'on souhaite tester.
    * @return True si la clé est valide.
    */
   Boolean isValidPK(CederObjet cederObjet);

   /**
    * Vérifie qu'un CederObjet est valide.
    * @param cederObjet CederObjet à créer.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   void validateObjectManager(CederObjet cederObjet, Cession cession, Entite entite, Unite quantiteUnite, String operation);

   /**
    * Persist une instance de CederObjet dans la base de données.
    * @param cederObjet CederObjet à créer.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   void createObjectManager(CederObjet cederObjet, Cession cession, Entite entite, Unite quantiteUnite);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param cederObjet Objet à persister.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   void updateObjectManager(CederObjet cederObjet, Cession cession, Entite entite, Unite quantiteUnite);

   /**
    * Supprime un CederObjet de la base de données.
    * @param cederObjet CederObjet à supprimer de la base de données.
    */
   void removeObjectManager(CederObjet cederObjet);

   /**
    * Compte les echantillons ou dérivés impliqués dans la session.
    * @param cess
    * @param entite 
    * @return le compte
    * @since 2.0.10
    */
   Long findObjectsCessedCountManager(Cession cess, Entite e);

   /**
    * Recherche les codes des objets dont la cession et l'entité sont
    * passées en paramètres.
    * @param cession Cession des CederObjets recherchés.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de String codes échantillons/dérivés
    * @since 2.0.10
    */
   List<String> findCodesByCessionEntiteManager(Cession cession, Entite entite);

}
