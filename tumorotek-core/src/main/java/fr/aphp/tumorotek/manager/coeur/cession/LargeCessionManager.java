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

import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.manager.validation.coeur.cession.LargeCessionHelper;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager gérant la création d'une large cession
 * par appel procédure JDBC.
 * Classe créée le 15/11/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public interface LargeCessionManager
{

   /**
    * Ajoute une liste d'échantillons ou de dérivés à partir d'une liste d'ids en base à la cession
    * passée en paramètre et valide la cession.
    * La cession des objets est totale, et est réalisé par la procédure SQL largeCession.
    * @param cession
    * @param codes objs
    * @param entiteId
    * @param banks
    * @param utilisateur
    * @param Objet transport codes en erreurs!
    */
   LargeCessionHelper addObjectsAndValidateCession(Cession cession, List<String> codes, Integer entiteId, List<Banque> banks,
      Utilisateur utilisateur);

   /**
    * Recherche parmis les ids passés en paramètre les objets TK stockables dont le statut de stockage
    * est incompatible avec une large cession (tous statut qui n'est pas STOCKE).
    * @param list objs ids.
    * @param Integer entiteId.
    * @return Liste de codes objet à afficher à l'utilisateur.
    */
   List<String> findIncompatibleStatutsManager(List<Integer> ids, Integer entiteId);

   /**
    * Recherche parmis les ids passés en paramètre les objets TK stockable dont la date de stockage
    * est ultérieure à la date de départ de la cession
    * @param list objs ids.
    * @param Integer entiteId.
    * @param date départ Cession
    * @return Liste de codes objet à afficher à l'utilisateur.
    */
   List<String> findDateStockIncompatiblesManager(List<Integer> ids, Integer entiteId, Date dateCession);

   /**
    * Recherche parmis les ids passés en paramètre les objets TK stockable pour lesquels un évènement de
    * stockage aurait une date de retour ultérieure à  ultérieure à la date de départ de la cession
    * @param list objs ids.
    * @param Integer entiteId.
    * @param date départ Cession
    * @return Liste de codes objet à afficher à l'utilisateur.
    */
   List<String> findEvtStockIncompatiblesManager(List<Integer> ids, Integer entiteId, Date dateCession);
}
