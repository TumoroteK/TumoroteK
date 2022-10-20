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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;

/**
 *
 * Interface pour le DAO du bean de domaine Patient.
 * Interface créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface PatientDao extends GenericDaoJpa<Patient, Integer>
{

   /**
    * Recherche les patients dont le NIP est 'like' le paramètre.
    * @param nip NIP des patients recherchés.
    * @return Liste de Patients.
    */
   List<Patient> findByNip(String nip);

   /**
    * Recherche les patients dont le NIP est 'like' le 1er paramètre
    * et dont l'identifiant est différent du 2eme parametre.
    * @param nip NIP des patients recherchés.
    * @param id Identifiant à exclure.
    * @return Liste de Patients.
    */
   List<Patient> findByNipWithExcludedId(String nip, Integer id);

   /**
    * Recherche les patients dont le nom est 'like' le paramètre.
    * @param nom Nom des patients recherchés.
    * @return Liste de Patients.
    */
   List<Patient> findByNom(String nom);

   /**
    * Recherche les ids des patients dont le NIP est 'like' le paramètre et
    * ayant des prélèvements dans les banques passées en paramètre.
    * @param nip NIP des patients recherchés.
    * @param Liste de banques.
    * @return Liste de Patients.
    */
   List<Integer> findByNipReturnIds(String nip, List<Banque> banques);

   /**
    * Recherche les ids des patients dont le nom est 'like' le paramètre et
    * ayant des prélèvements dans les banques passées en paramètre.
    * @param nom Nom des patients recherchés.
    * @param Liste de banques.
    * @return Liste de Patients.
    */
   List<Integer> findByNomReturnIds(String nom, List<Banque> banques);

   /**
    * Recherche les patients dont la date de naissance égale celle
    * passee en parametre.
    * @param date Date de naissance des patients recherchés.
    * @return Liste de Patients.
    */
   List<Patient> findByDateNaissance(Date date);

   /**
    * Recherche les patients enregistrés comme incomplets par le système.
    * @return Liste de Patients.
    */
   List<Patient> findByEtatIncomplet();

   /**
    * Recherche tous les patients sauf celui dont l'id est passé
    * en paramètre.
    * @param patientId Identifiant du patient que l'on souhaite
    * exclure de la liste retournée.
    * @param nom pour reduire la liste d'objets retournés.
    * @return une liste de Patient.
    */
   List<Patient> findByExcludedId(Integer patientId, String nom);

   /**
    * Recherche les patients dont l'id est dans la liste.
    * @param ids Liste d'identifiants.
    * @return Liste de Patients.
    */
   List<Patient> findByIdInList(List<Integer> ids);

   /**
    * Recherche tous les nips des patients.
    * @return une liste de nips.
    */
   List<String> findAllNips();

   /**
    * Recherche tous les noms des patients.
    * @return une liste de noms.
    */
   List<String> findAllNoms();

   /**
    * Compte toutes les maladies d'un patient.
    * @param pat
    * @return compte
    */
   List<Long> findCountMaladies(Patient pat);

   /**
    * Compte tous les prelevements d'un patient pour une banque.
    * @param pat
    * @param banque
    * @return compte
    */
   List<Long> findCountPrelevementsByBanque(Patient pat, Banque bank);

   /**
    * Compte tous les prelevements d'un patient.
    * @param pat
    * @return compte
    */
   List<Long> findCountPrelevements(Patient pat);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence est celle d'enregistrement du patient
    * dans le système.
    * @param cal1
    * @param cal2
    * @param banks
    * @return compte
    */
   List<Long> findCountPrelevedByDatesSaisie(Calendar cal1, Calendar cal2, List<Banque> banks);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence est celle de prélèvement.
    * @param cal1
    * @param cal2
    * @param banks
    * @return compte
    */
   List<Long> findCountPrelevedByDatesPrel(Calendar cal1, Calendar cal2, List<Banque> banks);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence est celle d'enregistrement du patient
    * dans le système.
    * Une liste d'établissement est passée en paramètre
    * pour exclure tous les échantillons techniqués dans ces derniers (pour
    * compter les échantillons extérieurs aux établissement
    * locaux par exemple).
    * @param cal1
    * @param cal2
    * @param banks
    * @param etabs
    * @return compte
    */
   List<Long> findCountPrelevedByDatesSaisieExt(Calendar cal1, Calendar cal2, List<Banque> banks, List<Etablissement> etabs);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence est celle de prélèvement.
    * Une liste d'établissement est passée en paramètre
    * pour exclure tous les échantillons techniqués dans ces derniers (pour
    * compter les échantillons extérieurs aux établissement
    * locaux par exemple).
    * @param cal1
    * @param cal2
    * @param banks
    * @param etabs
    * @return compte
    */
   List<Long> findCountPrelevedByDatesPrelExt(Calendar cal1, Calendar cal2, List<Banque> banks, List<Etablissement> etabs);

   /**
    * Recherche tous les ids des patients de la base.
    * @return Liste d'identifiants.
    */
   List<Integer> findByAllIds();

   /**
    * Recherche tous les ids des patients de la base ayant des prélèvements
    * dans les banques passées en paramètre.
    * @param Liste de banques.
    * @return Liste d'identifiants.
    */
   List<Integer> findByAllIdsWithBanques(List<Banque> banques);

   /**
    * Recherche les patients Ids dont le nom est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des patients.
    * @param banques Banques auxquelles appartiennent les patients.
    * @return une liste d'ids.
    */
   List<Integer> findByNomInList(List<String> criteres, List<Banque> banques);

   /**
    * Recherche les patients Ids dont le nip est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des patients.
    * @param banques Banques auxquelles appartiennent les patients.
    * @return une liste d'ids.
    */
   List<Integer> findByNipInList(List<String> criteres, List<Banque> banques);

   /**
    * Compte les patients pour le médecin référent passé en paramètre.
    * @param Collaborateur
    * @return long
    */
   List<Long> findCountByReferent(Collaborateur referent);
   
//   /**
//    * Recherche les patients dont le IDENTIFIANT est 'like' le paramètre.
//    * @param identifiant des patients recherchés.
//    * @return Liste de Patients.
//    */
//   List<Patient> findByIdentifiant(String identifiant);

   /**
    * Recherche les patients dont les identifiants sont passés en paramètres sous la 
    * forme d'une liste, pour une liste de collections.
    * @param identifiants
    * @param collections
    * @return liste patient ids
    */
   List<Integer> findByIdentifiantInList(List<String> identifiants, List<Banque> selectedBanques);

   /**
    * Recherche les patients pour un identifiant passé en paramètres, pour 
    * une liste de collections.
    * @param identifiant
    * @param collections
    * @param boolean exactMatch if true
    * @return liste patient ids
    */
   List<Integer> findByIdentifiantReturnIds(String identifiant, List<Banque> selectedBanques);

   /**
    * Recherche toutes les patients dont l'identifiant est egal
    * ou 'like' celui en parametre pour une liste de banques.
    * @param identifiant
    * @param boolean exactMatch
    * @param banques
    * @return Liste de Patient.
    */
   List<Patient> findByIdentifiant(String ident, List<Banque> selectedBanques);

}