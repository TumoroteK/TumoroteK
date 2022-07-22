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
import java.util.Set;

import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Maladie.<br>
 * Interface créée le 29/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer une maladie(controle de doublons)<br>
 * 	- Modifier une maladie (controle de doublons)<br>
 * 	- Afficher toutes les maladies<br>
 * 	- Afficher avec un filtre sur le libelle<br>
 * 	- Afficher avec un filtre sur le code diagnostic<br>
 * 	- Supprimer une maladie
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public interface MaladieManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param maladie Maladie a creer
    * @param patient Patient associee (non null)
    * @param medecins liste de Collaborateur medecins referents
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    */
   void createOrUpdateObjectManager(Maladie maladie, Patient patient, List<Collaborateur> medecins, Utilisateur utilisateur,
      String operation);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param maladie Maladie dont on cherche la presence dans la base
    * @return true/false
    * @version 2.2.3-genno
    */
   boolean findDoublonManager(Maladie maladie, Patient patient);

   /**
    * Verifie avant la suppression que d'autres objets ne referencent
    * pas cet objet.
    * @param maladie Maladie a supprimer de la base de donnees.
    * @return true/false
    */
   boolean isUsedObjectManager(Maladie maladie);

   /**
    * Supprime un objet de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param maladie Maladie à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void removeObjectManager(Maladie maladie, String comments, Utilisateur user);

   /**
    * Recherche les prelevements liés à la maladie passée en paramètre.
    * @param maladie Maladie pour laquelle on recherche les prelevements.
    * @return Liste de Prelevements.
    */
   Set<Prelevement> getPrelevementsManager(Maladie maladie);

   /**
    * Recherche les collaborateurs liés a la maladie passée en paramètre.
    * @param maladie Maladie pour laquelle on recherche les medecins
    * 	referents.
    * @return Liste de Collaborateur.
    */
   Set<Collaborateur> getCollaborateursManager(Maladie maladie);

   /**
    * Recherche toutes les instances de Maladie présentes dans la base.
    * @return List contenant les Maladie.
    */
   List<Maladie> findAllObjectsManager();

   /**
    * Recherche toutes les maladies dont le libelle est egal
    * ou 'like' celui en parametre.
    * @param libelle
    * @param boolean exactMatch
    * @return Liste de Maladie.
    */
   List<Maladie> findByLibelleLikeManager(String libelle, boolean exactMatch);

   /**
    * Recherche les maladies dont le libelle correspond à celui
    * fourni en paramètre pour un patient donnée.
    * @param libelle Libelle des maladies recherchées.
    * @param patient Patient
    * @return Liste de Maladies.
    * @since 2.2.3-genno
    */
   List<Maladie> findByLibelleAndPatientManager(String libelle, Patient pat);

   /**
    * Recherche toutes les maladies dont le code est egal
    * ou 'like' celui en parametre.
    * @param code
    * @param boolean exactMatch
    * @return Liste de Maladie.
    */
   List<Maladie> findByCodeLikeManager(String code, boolean exactMatch);

   /**
    * Recherche les maladies liés au patient passée en paramètre.
    * @param patient Patient pour laquelle on recherche les maladies.
    * @return Liste de Maladie.
    */
   Set<Maladie> getMaladiesManager(Patient patient);

   /**
    * Recherche les maladies assignées au patient uniquement
    * par l'utilisateur, et non pas par le systeme dans le cadre
    * de collection de prélèvements qui ne définissent pas de maladies.
    * @param patient
    * @return une liste de Maladie.
    */
   List<Maladie> findByPatientNoSystemManager(Patient patient);

   /**
    * Recherche les maladies assignées au patient.
    * @param patient
    * @return une liste de Maladie.
    */
   List<Maladie> findByPatientManager(Patient patient);

   /**
    * Compte le nombre de maladies pour le référent passé en param
    * @param collaborateur
    * @return Long compte
    */
   public Long findCountByReferentManager(Collaborateur colla);

}
