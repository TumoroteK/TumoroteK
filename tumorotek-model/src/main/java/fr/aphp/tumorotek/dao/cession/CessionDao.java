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
package fr.aphp.tumorotek.dao.cession;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Interface pour le DAO du bean de domaine Cession.
 * Interface créée le 26/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface CessionDao extends GenericDaoJpa<Cession, Integer>
{

   /**
    * Recherche les Cessions dont le numéro est égal au paramètre.
    * @param numero Numéro de la Cession recherchée.
    * @return une liste de Cessions.
    */
   List<Cession> findByNumero(String numero);

   /**
    * Recherche les Ids des Cessions dont le numéro et la banque est égaux
    * aux paramètres.
    * @param numero Numéro de la Cession recherchée.
    * @param banques Banques des Cessions recherchées.
    * @return une liste de Cessions.
    */
   List<Integer> findByNumeroWithBanqueReturnIds(String numero, List<Banque> banques);

   /**
    * Recherche toutes les Cessions d'une banque ordonnées.
    * @param banque Banque de la Cession recherchée.
    * @return Liste ordonnée de Cessions.
    */
   List<Cession> findByBanqueWithOrder(Banque banque);

   /**
    * Recherche les numéros des Cessions dont la banque est passée
    * en paramètre sauf celle dont l'identifiant est en paramètre.
    * @param id Identifiant de la cession à exclure.
    * @param banque Banque de la Cession recherchée.
    * @return Liste de numéros.
    */
   List<String> findByExcludedIdNumeros(Integer id, Banque banque);

   /**
    * Recherche les numéros des Cessions dont la banque est passée
    * en paramètre.
    * @param banque Banque de la Cession recherchée.
    * @return Liste de numéros.
    */
   List<String> findByBanqueSelectNumero(Banque banque);

   /**
    * Recherche les cessions dont le numéro 
    * est 'like' le paramètre pour la plateforme spécifiée.
    * @param code Code pour lequel on recherche des cessions.
    * @param pf Plateforme.
    * @return une liste de cessions.
    * @since 2.1
    */
   List<Cession> findByNumeroInPlateforme(String code, Plateforme pf);

   /**
    * Recherche les Cessions dont le Contrat est passé
    * en paramètre.
    * @param contrat Contrat des Cessions recherchées.
    * @return Liste de cessions.
    */
   List<Cession> findByContrat(Contrat contrat);

   /**
    * Recherche les Ids des Cessions dont la valeur du statut est passée
    * en paramètre.
    * @param statut Valeur du CessionStatut des Cessions recherchées.
    * @param banques Banques des Cessions recherchées.
    * @return Liste de cessions.
    */
   List<Integer> findByCessionStatutAndBanqueReturnIds(String statut, List<Banque> banque);

   /**
    * Recherche les Ids des Cessions dont l'état et la banque sont passés
    * en paramètre.
    * @param etat EtatIncomplet des Cessions recherchées.
    * @param banque Banque des Cessions recherchées.
    * @return Liste de cessions.
    */
   List<Integer> findByEtatIncompletAndBanquesReturnIds(boolean etat, List<Banque> banques);

   /**
    * Recherche les Cessions pour une liste de collections spécifiées.
    * @param banks liste de collections
    * @return une liste de Cessions.
    */
   List<Cession> findByBanques(List<Banque> banks);

   /**
    * Recherche toutes les Cessions dont l'id est dans la liste.
    * @param ids Liste d'identifiants.
    * @return Liste ordonnée de Cessions.
    */
   List<Cession> findByIdInList(List<Integer> ids);

   /**
    * Recherche les ids des cessions des banques de la liste.
    * @param banques Banques des cessions recherchés.
    * @return Liste de Cessions.
    */
   List<Integer> findByBanquesAllIds(List<Banque> banques);

   List<Integer> findByServiceDest(Service service);

   /**
    * Compte les cessions dont le demandeur est le collaborateur 
    * passée en paramètre.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByDemandeur(Collaborateur collaborateur);

   /**
    * Compte les cessions dont le destinataire est le collaborateur 
    * passée en paramètre.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByDestinataire(Collaborateur collaborateur);

   /**
    * Compte les cessions dont l'executant est le collaborateur 
    * passée en paramètre.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByExecutant(Collaborateur collaborateur);
}
