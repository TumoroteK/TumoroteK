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
import fr.aphp.tumorotek.model.systeme.Numerotation;

/**
 *
 * Interface pour le manager du bean de domaine Numerotation.
 * Interface créée le 18/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface NumerotationManager
{

   /**
    * Recherche une Numerotation dont l'identifiant est passé en 
    * paramètre.
    * @param numerotationId Identifiant de la Numerotation
    * que l'on recherche.
    * @return Une Numerotation.
    */
   Numerotation findByIdManager(Integer numerotationId);

   /**
    * Recherche toutes les Numerotations présentss dans la base.
    * @return Liste de Numerotations.
    */
   List<Numerotation> findAllObjectsManager();

   /**
    * Recherche les Numérotation des banques.
    * @param banques Liste de banques.
    * @return Liste de Numerotations.
    */
   List<Numerotation> findByBanquesManager(List<Banque> banques);

   /**
    * Recherche toutes les Numerotations de la banque et de l'entité.
    * @param banque Banque.
    * @param entite Entite.
    * @return Liste de Numerotations.
    */
   List<Numerotation> findByBanqueAndEntiteManager(Banque banque, Entite entite);

   /**
    * Recherche toutes les Entités ayant des numérotations pour
    * la banque.
    * @param banque Banque.
    * @return Liste d'Entites.
    */
   List<Entite> findByBanqueSelectEntiteManager(Banque banque);

   /**
    * Recherche les doublons de la Numerotation passée en paramètre.
    * @param numerotation Numerotation pour laquelle on cherche 
    * des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Numerotation numerotation);

   /**
    * Génère le code courant pour la numérotation.
    * @param numerotation Numerotation.
    * @return Code courant.
    */
   String getGeneratedCodeManager(Numerotation numerotation);

   /**
    * Persist une instance de Numerotation dans la base de données.
    * @param numerotation Nouvelle instance de l'objet à créer.
    * @param banque Banque.
    * @param entite Entite.
    */
   void createObjectManager(Numerotation numerotation, Banque banque, Entite entite);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param numerotation Objet à persister.
    * @param banque Banque.
    * @param entite Entite.
    */
   void updateObjectManager(Numerotation numerotation, Banque banque, Entite entite);

   /**
    * Supprime une Numerotation de la base de données.
    * @param numerotation Numerotation à supprimer de la base de données.
    */
   void removeObjectManager(Numerotation numerotation);

}
