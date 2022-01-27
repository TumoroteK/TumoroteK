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

import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.DroitObjet;
import fr.aphp.tumorotek.model.utilisateur.DroitObjetPK;
import fr.aphp.tumorotek.model.utilisateur.Profil;

/**
 *
 * Interface pour le manager du bean de domaine DroitObjet.
 * Interface créée le 19/05/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface DroitObjetManager
{

   /**
    * Recherche un DroitObjet dont l'identifiant est passé en paramètre.
    * @param droitObjetPK Identifiant du DroitObjet que l'on recherche.
    * @return Un DroitObjet.
    */
   DroitObjet findByIdManager(DroitObjetPK droitObjetPK);

   /**
    * Recherche tous les DroitObjets présents dans la base.
    * @return Liste de DroitObjets.
    */
   List<DroitObjet> findAllObjectsManager();

   /**
    * Recherche les DroitObjets sauf celui dont la clé primaire est
    * passée en paramètre.
    * @param pk DroitObjetPK.
    * @return une liste de DroitObjets.
    */
   List<DroitObjet> findByExcludedPKManager(DroitObjetPK pk);

   /**
    * Recherche les DroitObjets dont le Profil est égale au paramètre.
    * @param profil Profil des DroitObjets recherchés.
    * @return une liste de DroitObjets.
    */
   List<DroitObjet> findByProfilManager(Profil profil);

   /**
    * Recherche tous les DroitObjets ppur un couple de valeurs Profil
    * et entité.
    * @param profil Profil des DroitObjet recherchés.
    * @param entite Entite des DroitObjet recherchés.
    * @return Liste ordonnée de DroitObjets.
    */
   List<DroitObjet> findByProfilEntiteManager(Profil profil, Entite entite);

   /**
    * Recherche tous les types d'opération pour un couple de 
    * valeurs Profil
    * et entité.
    * @param profil Profil des DroitObjet recherchés.
    * @param entite Entite des DroitObjet recherchés.
    * @return Liste d'OperationType.
    */
   List<OperationType> getOperationsByProfilEntiteManager(Profil profil, String nomEntite);

   /**
    * Recherche tous les DroitObjets pour un couple de valeurs Profil
    * et OperationType.
    * @param profil Profil des DroitObjet recherchés.
    * @param type OperationType des DroitObjet recherchés.
    * @return Liste ordonnée de DroitObjets.
    */
   List<DroitObjet> findByProfilOperationManager(Profil profil, OperationType type);

   /**
    * Recherche les doublons pour un profilutilisateur.
    * @param profil Profil du DroitObjet.
    * @param entite Entité  du DroitObjet.
    * @param type OperationType du DroitObjet.
    * @return True si le DroitObjet existe déjà.
    */
   Boolean findDoublonManager(Profil profil, Entite entite, OperationType type);

   /**
    * Valide l'objet formé par les associations en paramètres.
    * @param profil Profil du DroitObjet.
    * @param entite Entité  du DroitObjet.
    * @param type OperationType du DroitObjet.
    */
   void validateObjectManager(Profil profil, Entite entite, OperationType type);

   /**
    * Persist une instance de DroitObjet dans la base de données.
    * @param droitObjet DroitObjet à créer.
    * @param profil Profil du DroitObjet.
    * @param entite Entité  du DroitObjet.
    * @param type OperationType du DroitObjet.
    */
   void saveManager(DroitObjet droitObjet, Profil profil, Entite entite, OperationType type);

   /**
    * Supprime un DroitObjet de la base de données.
    * @param droitObjet DroitObjet à supprimer de la base de données.
    */
   void deleteByIdManager(DroitObjet droitObjet);

   /**
    * Vérifie si un profil à le droit sur l'opération spécifiée sur toutes 
    * les entités passées en paramètres
    * @param profil
    * @param type OperationType
    * @param entites
    * @return true si le profil possède tous les droits sur l'entite.
    */
   Boolean hasProfilOperationOnEntitesManager(Profil profil, OperationType type, List<Entite> entites);
}
