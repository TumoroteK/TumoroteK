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
package fr.aphp.tumorotek.manager.coeur.patient;

import java.util.List;

import fr.aphp.tumorotek.manager.CrudManager;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;

/**
 *
 * Interface pour le manager du bean de domaine LienFamilial.<br>
 * Interface créée le 29/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un type de lien familial (controle de doublons)
 * 		-> l'enregistrement du reciproque est automatique<br>
 * 	- Modifier un type de lien familial (controle de doublons)
 * 		-> la modification du reciproque est automatique<br>
 * 	- Afficher toutes les types liens<br>
 * 	- Afficher avec un filtre sur le nom<br>
 * 	- Supprimer un type de lien familial
 * 		-> la suppression du reciproque est automatique<br>
 *
 * Note: L'utilisation de JPA impose l'auto-referencement de la table
 * a nullable, sinon il faut passer par les Ids et perdre les avantages des
 * references... la logique non nullable de cette auto-reference est donc geree
 * par la classe Manager ce qui implique que des NullPointerException puissent
 * survenir si la table est modifiee directement en base.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface LienFamilialManager extends CrudManager
{

   /**
    * Recherche toutes les instances de LienFamilial présentes dans la base.
    * @return List contenant les LienFamilial.
    */
   List<LienFamilial> findAllObjectsManager();

   /**
    * Recherche toutes les types de lien familiaux dont le nom est egal
    * ou 'like' celui en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de LienFamilial.
    */
   List<LienFamilial> findByNomLikeManager(String nom, boolean exactMatch);

}
