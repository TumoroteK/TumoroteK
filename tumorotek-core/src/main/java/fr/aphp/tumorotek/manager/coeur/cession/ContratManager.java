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

import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Contrat.
 * Interface créée le 28/01/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ContratManager
{

   /**
    * Recherche un Contrat dont l'identifiant est passé en paramètre.
    * @param contratId Identifiant du Contrat que l'on recherche.
    * @return Un Contrat.
    */
   Contrat findByIdManager(Integer contratId);

   /**
    * Recherche tous les Contrats présents dans la base.
    * @return Liste de Contrats.
    */
   List<Contrat> findAllObjectsManager();

   /**
    * Recherche tous les Contrats (ordonnés par numero) d'une plateforme.
    * @param plateforme Plateforme pour laquelle on recherche.
    * @return Liste de Contrats.
    */
   List<Contrat> findAllObjectsByPlateformeManager(Plateforme plateforme);

   /**
    * Recherche les Cessions liées au Contrat passé en paramètre.
    * @param contrat Contrat pour lequel on recherche des cessions.
    * @return Liste de Cessions.
    */
   List<Cession> getCessionsManager(Contrat contrat);

   /**
    * Recherche une liste de Contrats dont le numero commence comme
    * celui passé en paramètre.
    * @param numero Numero pour lequel on recherche des Contrats.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de Contrats.
    */
   List<Contrat> findByNumeroLikeManager(String numero, boolean exactMatch);

   /**
    * Recherche les doublons du Contrat passé en paramètre.
    * @param contrat Contrat pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Contrat contrat);

   /**
    * Teste si le Contrat passé en paramètre est utilisé par d'autres
    * objets.
    * @param contrat Contrat que l'on test.
    * @return True si l'objet est utilisé.
    */
   Boolean isUsedObjectManager(Contrat contrat);

   /**
    * Persist une instance de Contrat dans la base de données.
    * @param contrat Nouvelle instance de l'objet à créer.
    * @param collaborateur Collaborateur du contrat.
    * @param service Service du Contrat.
    * @param protocoleType ProtocoleType du contrat.
    * @param plateforme Plateforme du contrat.
    */
   void saveManager(Contrat contrat, Collaborateur collaborateur, Service service, Etablissement etablissement,
      ProtocoleType protocoleType, Plateforme plateforme, Utilisateur utilisateur);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param contrat Objet à persister.
    * @param collaborateur Collaborateur du contrat.
    * @param service Service du Contrat.
    * @param protocoleType ProtocoleType du contrat.
    * @param plateforme Plateforme du contrat.
    */
   void saveManager(Contrat contrat, Collaborateur collaborateur, Service service, Etablissement etablissement,
      ProtocoleType protocoleType, Plateforme plateforme, Utilisateur utilisateur);

   /**
    * Supprime un Contrat de la base de données.
    * @param contrat Contrat à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void deleteByIdManager(Contrat contrat, String comments, Utilisateur usr);

}
