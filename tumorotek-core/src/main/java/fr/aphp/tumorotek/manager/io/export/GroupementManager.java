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
package fr.aphp.tumorotek.manager.io.export;

import java.util.List;

import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;

/**
 *
 * Implémentation du manager du bean de domaine Groupement.
 * Classe créée le 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public interface GroupementManager
{

   /**
    * Recherche un Groupement dont l'identifiant est passé en paramètre.
    * @param groupementId Identifiant du Groupement que l'on recherche.
    * @return un Groupement.
    */
   Groupement findByIdManager(Integer groupementId);

   /**
    * Recherche tous les Groupements présents dans la BDD.
    * @return Liste de Groupements.
    */
   List<Groupement> findAllObjectsManager();

   /**
    * Copie un Groupement en BDD.
    * @param groupement Groupement à copier.
    * @return le Groupement copié.
    */
   Groupement copyGroupementManager(Groupement groupement);

   /**
    * Créé un Groupement en BDD.
    * @param groupement Groupement à créer.
    * @param critere1 Premier Critère du Groupement.
    * @param critere2 Deuxième Critère du Groupement.
    * @param operateur Opérateur du Groupement.
    * @param parent Groupement parent de la reqûete.
    */
   void saveManager(Groupement groupement, Critere critere1, Critere critere2, String operateur, Groupement parent);

   /**
    * Met à jour un Groupement en BDD.
    * @param groupement Groupement à mettre à jour.
   * @param critere1 Premier Critère du Groupement.
    * @param critere2 Deuxième Critère du Groupement.
    * @param operateur Opérateur du Groupement.
    * @param parent Groupement parent de la reqûete.
    */
   void saveManager(Groupement groupement, Critere critere1, Critere critere2, String operateur, Groupement parent);

   /**
    * Supprime un Groupement et ses enfants d'abord.
    * @param groupement Groupement à supprimer.
    */
   void deleteByIdManager(Groupement groupement);

   /**
    * Chercher les Groupements enfants du Groupement passé en paramètre.
    * @param groupement Groupement dont on souhaite obtenir la liste d'enfants.
    * @return liste des enfants (Groupements) d'un Groupement.
    */
   List<Groupement> findEnfantsManager(Groupement groupement);

   /**
    * Recherche les Critères dont le Groupement racine est passé en paramètre. 
    * @param groupementRacine Groupement racine dont on souhaite obtenir
    * les Critères.
    * @return la liste de tous les Critères descendants du Groupement racine.
    */
   List<Critere> findCriteresManager(Groupement groupementRacine);

   /**
    * Méthode qui permet de vérifier que 2 Groupements sont des copies (et que
    * leurs enfants aussi).
    * @param g Groupement premier Groupement à vérifier.
    * @param copie deuxième Groupement à vérifier.
    * @return true si les 2 Groupements sont des copies, false sinon.
    */
   Boolean isCopyManager(Groupement g, Groupement copie);
}
