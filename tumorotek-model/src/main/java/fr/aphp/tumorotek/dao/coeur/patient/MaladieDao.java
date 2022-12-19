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
package fr.aphp.tumorotek.dao.coeur.patient;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 *
 * Interface pour le DAO du bean de domaine Maladie.
 * Interface créée le 02/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.3-genno
 *
 */
public interface MaladieDao extends GenericDaoJpa<Maladie, Integer>
{

   /**
    * Recherche les maladies dont le libelle est 'like' le paramètre.
    * @param libelle Libelle des maladies recherchées.
    * @return Liste de Maladies.
    */
   List<Maladie> findByLibelle(String libelle);

   /**
    * Recherche les maladies dont le libelle correspond à celui
    * fourni en paramètre pour un patient donnée.
    * @param libelle Libelle des maladies recherchées.
    * @param patient Patient
    * @return Liste de Maladies.
    * @since 2.2.3-genno
    */
   List<Maladie> findByLibelleAndPatient(String libelle, Patient patient);

   /**
    * Recherche les maladies dont le code est 'like' le paramètre.
    * @param code Code des maladies recherchées.
    * @return Liste de Maladies.
    */
   List<Maladie> findByCode(String code);

   /**
    * Recherche toutes les maladies sauf celle dont l'id est passé
    * en paramètre.
    * @param maladieId Identifiant de la maladie que l'on souhaite
    * exclure de la liste retournée.
    * @param libelle pour reduire la liste d'objets retournés.
    * @return une liste de Maladie.
    */
   List<Maladie> findByExcludedId(Integer maladieId, String libelle);

   /**
    * Recherche toutes les maladies/visites assignées au patient.
    * @param patient
    * @return une liste de Maladie/Visites.
    * @since 2.3.0-gatsbi
    */
   List<Maladie> findAllByPatient(Patient patient);
   
   /**
    * Recherche les maladies assignées au patient, excluant toutes 
    * celles correspondant à des visites (banque_id not null)
    * @param patient
    * @return une liste de Maladie/Visites.
    * @since 2.3.0-gatsbi
    */
   List<Maladie> findByPatientExcludingVisites(Patient patient);

   /**
    * Recherche les maladies assignées au patient uniquement
    * par l'utilisateur, et non pas par le systeme dans le cadre
    * de collection de prélèvements qui ne définissent pas de maladies.
    * @param patient
    * @return une liste de Maladie.
    */
   List<Maladie> findByPatientNoSystem(Patient patient);

   List<Maladie> findByCollaborateurId(Integer collaborateurId);

   /**
    * Compte les maladies pour le médecin référent passé en paramètre.
    * @param Collaborateur
    * @return long
    */
   List<Long> findCountByReferent(Collaborateur referent);
   
   /**
    * Liste les maladies/visites définie pour la collection pour le patient.
    * @param patient
    * @param banque
    * @return une liste de visites.
    */
   List<Maladie> findVisites(Patient patient, Banque banque);
}
