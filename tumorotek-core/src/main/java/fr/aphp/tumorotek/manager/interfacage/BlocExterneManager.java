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
package fr.aphp.tumorotek.manager.interfacage;

import java.util.List;

import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine BlocExterne.
 * Interface créée le 07/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface BlocExterneManager
{

   /**
    * Recherche un BlocExterne dont l'identifiant est passé en paramètre.
    * @param blocExterneId Identifiant du bloc que l'on recherche.
    * @return Un BlocExterne.
    */
   BlocExterne findByIdManager(Integer blocExterneId);

   /**
    * Recherche tous les BlocExterne présents dans la base.
    * @return Liste de BlocExternes.
    */
   List<BlocExterne> findAllObjectsManager();

   /**
    * Recherche tous les BlocExternes d'un Dossier.
    * @param dossierExterne DossierExterne des blocs recherchés.
    * @return Liste de BlocExterne.
    */
   List<BlocExterne> findByDossierExterneManager(DossierExterne dossierExterne);

   /**
    * Recherche tous les BlocExternes d'un Dossier et d'une entité.
    * @param dossierExterne DossierExterne des blocs recherchés.
    * @param entite Entite des blocs recherchés.
    * @return Liste de BlocExterne.
    */
   List<BlocExterne> findByDossierExterneAndEntiteManager(DossierExterne dossierExterne, Entite entite);

   /**
    * Retourne l'objet Entite correspondant à l'id se trouvant
    * dans la table.
    * @param blocExterne BlocExterne dont on veut l'Entite.
    * @return Une Entite.
    */
   Entite getEntiteManager(BlocExterne blocExterne);

   /**
    * Recherche les doublons d'un BlocExterne.
    * @param blocExterne BlocExterne dont on cherche des doublons.
    * @return True s'il y a un doublon.
    */
   boolean findDoublonManager(BlocExterne blocExterne);

   /**
    * Vérifie que le bloc externe est valide.
    * @param blocExterne Bloc Externe à tester.
    * @param dossierExterne Dossier.
    * @param entite Entité du bloc.
    */
   void validateBlocExterneManager(BlocExterne blocExterne, DossierExterne dossierExterne);

   /**
    * Crée un bloc externe.
    * @param blocExterne Bloc Externe à créer.
    * @param dossierExterne Dossier.
    * @param entite Entité du bloc.
    * @param valeurExternes Valeurs de ce bloc.
    */
   void createObjectManager(BlocExterne blocExterne, DossierExterne dossierExterne, List<ValeurExterne> valeurExternes);

   /**
    * Supprime un blocExterne.
    * @param blocExterne Bloc Externe à supprimer.
    */
   void removeObjectManager(BlocExterne blocExterne);

}
