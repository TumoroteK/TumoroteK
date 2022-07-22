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

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Patient.<br>
 * Interface créée le 30/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un patient (controle de doublons)<br>
 * 	- Modifier un patient (controle de doublons)<br>
 * 	- Afficher tous les patients<br>
 * 	- Afficher avec un filtre sur le nom<br>
 * 	- Afficher avec un filtre sur le nip<br>
 * 	- Afficher avec un filtre sur la date de naissance<br>
 *  - Lister tous les nips non nulls
 *  - Lister tous les noms
 *  - Récupérer les objets associés (PatientMedecin, PatientLien, Maladie)
 *  - Récupérer les Collaborateurs correspondant aux medecins
 * 	- Supprimer un patient
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.12
 *
 */
public interface PatientManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param patient Patient a creer
    * @param maladies liste de Maladies
    * @param medecins liste de Collaborateur medecins referents ordonnée
    * @param patientLiens liste de PatientLien liens familiaux
    * @param liste des valeurs d'annotation à enregistrer
    * @param liste des valeurs d'annotation à supprimer.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param utilisateur Utilisateur realisant la creation
    * @param operation String creation / modification
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    */
   void createOrUpdateObjectManager(Patient patient, List<Maladie> maladies, List<Collaborateur> medecins,
      List<PatientLien> patientLiens, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete,
      List<File> filesCreated, List<File> filesToDelete, Utilisateur utilisateur, String operation, String baseDir,
      boolean isImport);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param patient Patient dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(Patient patient);

   /**
    * Supprime un objet de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param patient Patient à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param filesToDelete liste de fichier à supprimer
    */
   void removeObjectManager(Patient patient, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Recherche les medecins liés au patient passée en paramètre.
    * @param patient Patient pour laquelle on recherche les medecins
    * 	referents.
    * @return Liste de PatientMedecin.
    */
   Set<PatientMedecin> getPatientMedecinsManager(Patient patient);

   /**
    * Recherche les liens familiaux liés au patient passée en paramètre.
    * Renvoi tous les liens dans lesquels le patient participe (en tant
    * que patient1 ou patient2).
    * @param patient Patient pour laquelle on recherche les liens familiaux.
    * @return Liste de PatientLien.
    */
   Set<PatientLien> getPatientLiensManager(Patient patient);

   /**
    * Recherche les collaborateurs correspondant aux objet PatientMedecin.
    * La liste des collaborateurs est ordonnee.
    * @param patient Patient pour laquelle on recherche les medecins.
    * @return Liste de Collaborateur.
    */
   List<Collaborateur> getMedecinsManager(Patient patient);

   /**
    * Recherche toutes les instances de Patient présentes dans la base.
    * @return List contenant les Patient.
    */
   List<Patient> findAllObjectsManager();

   /**
    * Recherche touts les ids de Patient présents dans la base.
    * @return List contenant les ids des Patient.
    */
   List<Integer> findAllObjectsIdsManager();

   /**
    * Recherche touts les ids de Patient présents dans la base ayant
    * des prélèvements dans les banques passées en paramètre.
    * @param banques Liste de banques.
    * @return List contenant les ids des Patient.
    */
   List<Integer> findAllObjectsIdsWithBanquesManager(List<Banque> banques);

   /**
    * Recherche toutes les patients dont le nom est egal ou 'like'
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<Patient> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche toutes les patients dont le nip est egal
    * ou 'like' celui en parametre.
    * @param nip
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<Patient> findByNipLikeManager(String nip, boolean exactMatch);

   /**
    * Recherche toutes les patients dont le nom est egal ou 'like'
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<Patient> findByNomLikeBothSideManager(String nom, boolean exactMatch);

   /**
    * Recherche toutes les patients dont le nom est egal ou 'like'
    * celui passé en parametre et ayant
    * des prélèvements dans les banques passées en paramètre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<Integer> findByNomLikeBothSideReturnIdsManager(String nom, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche toutes les patients dont le nip est egal
    * ou 'like' celui en parametre et ayant
    * des prélèvements dans les banques passées en paramètre.
    * @param nip
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<Integer> findByNipLikeBothSideReturnIdsManager(String nip, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche toutes les patients dont la date de naissance est egale
    * à celle passée en parametre.
    * @param date
    * @return Liste de Patient.
    */
   List<Patient> findByDateNaissanceManager(Date date);

   /**
    * Recherche tous les patients incomplets.
    * @return Liste de Patient.
    */
   List<Patient> findByEtatIncompletManager();

   /**
    * Recherche tous les patients dont la date de creation systeme est
    * posterieure ou egale a celle passee en parametre et ayant des prlvts
    * dans les banques passées en paramètres.
    * @param date
    * @return Liste de Patient.
    */
   List<Integer> findAfterDateCreationReturnIdsManager(Calendar date, List<Banque> banques);

   /**
    * Recherche tous les patients dont la date de modification systeme est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @return Liste de Patient.
    */
   List<Patient> findAfterDateModificationManager(Calendar date);

   /**
    * Recherche tous les nips (non null) des patients enregistrés
    * dans la base.
    * @return Liste de nips.
    */
   List<String> findAllNipsManager();

   /**
    * Recherche tous les noms des patients enregistrés dans la base.
    * @return Liste de noms.
    */
   List<String> findAllNomsManager();

   /**
    * Recherche les patients par le NDA de leurs prélèvements.
    * @param nda
    * @return Liste de Patient.
    */
   List<Patient> findByNdaLikeManager(String nda, boolean exactMatch);

   /**
    * Recherche les derniers Patients créés dans le systeme.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de Patients.
    */
   List<Patient> findLastCreationManager(List<Banque> banques, int nbResults);

   /**
    * Compte toutes les maladies d'un patient.
    * @param pat
    * @return compte
    */
   Long getTotMaladiesCountManager(Patient p);

   /**
    * Compte tous les prelevements d'un patient pour une banque.
    * @param pat
    * @param banque
    * @return compte
    */
   Long getCountPrelevementsByBanqueManager(Patient pat, Banque bank);

   /**
    * Compte tous les prelevements d'un patient.
    * @param pat
    * @return compte
    */
   Long getTotPrelevementsCountManager(Patient p);

   /**
    * Réalise la modification multiple d'une liste de patients.
    * @param patients Liste des patients à mettre à jour.
    * @param Liste patients à la base de la modification multiple, cette liste est
    * utilisée pour la création des annotations Fichier en batch
    * @param list Annotations associées à mettre à jour ou créer
    * (doivent donc avoir objet, champ, et banque referencees)
    * @param list Annotations associées à supprimer
    * @param utilisateur Utilisateur voulant modifier les patients.
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    */
   void updateMultipleObjectsManager(List<Patient> patients, List<Patient> basePatients,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, Utilisateur utilisateur,
      String baseDir);

   /**
    * Verifie avant la suppression que le patient ne référence pas de
    * prélèvements avant d'être supprimé.
    * pas cet objet.
    * @param patient Patient a supprimer de la base de donnees.
    * @return true/false
    */
   boolean isUsedObjectManager(Patient patient);

   /**
    * Recherche les patients dont l'id se trouve dans la liste.
    * @param ids Liste d'identifiants.
    * @return Liste de patients.
    */
   List<Patient> findByIdsInListManager(List<Integer> ids);

   /**
    * Verifie si le patient doit être modifié (de manière automatique
    * par synchronisation avec le serveur d'identité patient).
    * @param Patient venant du SIP
    * @param Patient venant du système
    */
   List<Field> isSynchronizedPatientManager(PatientSip sip, Patient pat);

   /**
    * Fusionne deux patients enregistrés dans le système.
    * Le premier patient passé en paramètre est celui qui sera conservé
    * dans le système et qui recevra les associations attribuées au
    * patient passif passé en deuxième paramètres.
    * Association à récupérer:<br>
    *  - maladies (si nouvelle, sinon récupérer juste prelevements)<br>
    *  - medecins referents (si nouveaux)<br>
    *  - liens familiaux (si nouveaux)<br>
    *  Crée une operation de fusion et enregistre un fantome pour le patient
    *  passif avec un commentaire sur l'operation de fusion (venant du sip ou
    *  de l'utilisateur...)
    * @param actif celui qui restera
    * @param passif celui qui sera supprimé
    * @param utilisateur qui a réalisé la fusion (peut être null)
    * @param String comments sur l'operation de fusion.
    * @para String baseDir
    */
   void fusionPatientManager(Patient patient, Patient passif, Utilisateur u, String comments);

   /**
    * Recherche les patients Ids dont le nom est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des patients.
    * @param banques Banques auxquelles appartiennent les patients.
    * @return une liste d'ids.
    */
   List<Integer> findByNomInListManager(List<String> criteres, List<Banque> banques);

   /**
    * Recherche les patients Ids dont le nip est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des patients.
    * @param banques Banques auxquelles appartiennent les patients.
    * @return une liste d'ids.
    */
   List<Integer> findByNipInListManager(List<String> criteres, List<Banque> banques);

   /**
    * Compte le nombre de maladies pour le référent passé en param
    * @param collaborateur
    * @return Long compte
    */
   public Long findCountByReferentManager(Collaborateur colla);

   /**
    * Supprime les patients à partir des ids passés en paramètres.
    * @param ids
    * @param String fantome commentaire
    * @param u Utilisateur (opération suppr)
    * @since 2.0.12
    */
   void removeListFromIdsManager(List<Integer> ids, String comment, Utilisateur u);

   /**
    * Find by Id
    * @param id
    * @return Patient
    */
   Patient findByIdManager(Integer id);

   /**
    * Trouve le patient en base correspondant au patient passé en paramètre,
    * sur la base de la méthode équals.
    * Méthode équivalente à la findDoublonManager, mais avec pour objectif de
    * renvoyer l'objet lui même dans le cadre d'un import en modification.
    * @param pat Patient
    * @return patient si existe, null sinon
    * @since 2.0.13
   =	 */
   Patient getExistingPatientManager(Patient pat);
}
