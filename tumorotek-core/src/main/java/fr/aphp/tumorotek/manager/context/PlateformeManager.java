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

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Plateforme.
 * Interface créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface PlateformeManager
{

   /**
    * Recherche une Plateforme dont l'identifiant est passé en paramètre.
    * @param plateformeId Identifiant de la Plateforme que l'on recherche.
    * @return Une Plateforme.
    */
   Plateforme findByIdManager(Integer plateformeId);

   /**
    * Recherche toutes les Plateforme présentes dans la base.
    * @return Liste de Plateforme.
    */
   List<Plateforme> findAllObjectsManager();

   /**
    * Recherche les banque liées à la plateforme passée en paramètre.
    * @param plateforme Plateforme pour laquelle on recherche des
    * banques.
    * @return Liste de banques.
    */
   Set<Banque> getBanquesManager(Plateforme plateforme);

   /**
    * Recherche les utilisateurs administrateurs de la pf.
    * @param plateforme Plateforme pour laquelle on recherche des
    * administrateurs.
    * @return Liste de plateformes.
    */
   Set<Utilisateur> getUtilisateursManager(Plateforme plateforme);

   /**
    * Recherche les doublons de la plateforme passée en paramètre.
    * @param plateforme Une plateforme pour laquelle on cherche 
    * des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Plateforme plateforme);

   /**
    * Persist une instance de plateforme dans la base de données.
    * @param utilisateur Instance de l'objet à maj.
    * @param collaborateur Collaborateur
    * @param utilisateurs Liste d'administrateurs.
    * @param conteneurs Liste de conteneurs.
    * @param admin Utilisateur modifiant la plateforme.
    */
   void updateObjectManager(Plateforme plateforme, Collaborateur collaborateur, List<Utilisateur> utilisateurs,
      List<Conteneur> conteneurs, Utilisateur admin);
}
