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
package fr.aphp.tumorotek.manager.coeur.prelevement;

import java.util.List;

import fr.aphp.tumorotek.manager.PfDependantTKThesaurusManager;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Interface pour le manager du bean de domaine Risque.<br>
 * Interface créée le 05/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un risque(controle de doublons)<br>
 * 	- Modifier un risque(controle de doublons)<br>
 * 	- Afficher toutes les risques<br>
 * 	- Afficher avec un filtre sur le nom<br>
 *  - Afficher avec un filtre sur l'infectiosite<br>
 * 	- Supprimer un type de prelevement<br>
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface RisqueManager extends PfDependantTKThesaurusManager<Risque>
{

   /**
    * Recherche toutes les instances de Risque présentes dans la base.
    * @return List contenant les Risque.
    */
   List<Risque> findAllObjectsManager();

   /**
    * Recherche toutes les risques dont le nom est egal
    * ou 'like' celui en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Risque.
    */
   List<Risque> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche toutes les risques dont l'infectiosite
    * egale celle passee en parametre.
    * @param boolean infectieux
    * @return Liste de Risque.
    */
   List<Risque> findByInfectieuxManager(Boolean infectieux);

   /**
    * Recherche tous les risques associés aux prélèvements d'un 
    * patient pour toutes les collections de la plateforme passée en 
    * paramètre.
    * @param pat
    * @param plateforme
    * @return liste de risques infectieux
    */
   List<Risque> findByPatientAndPlateformeManager(Patient pat, Plateforme plateforme);
}
