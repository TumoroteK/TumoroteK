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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;

/**
 *
 * Interface pour le manager du bean de domaine PatientSip.<br>
 * Interface créée le 02/05/11.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un patient venant du SIP dans la table temporaire
 * 		(controle de doublons)<br>
 * 	- Modifier un patient venant du SIP<br>
 * 	- Afficher tous les patients<br>
 * 	- Afficher avec un filtre sur le nom<br>
 * 	- Afficher avec un filtre sur le nip<br>
 * 	- Supprimer un patient venant du SIP<br>
 * 	- Vérifier si la modification d'un patient venant du SIP ou la modification
 *  d'un patient système doit etre effectuée à
 *  	partir d'un patient venant du SIP<br>
 *  - Modifier un patient système à partir
 *  	d'un patient venant du SIP (synchronisation)
 *
 *  @since 12/11/2012
 *  Modifiée le  pour intégrer les numéros de visite dans le
 *  parsage  des fichiers hl7.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.3
 *
 */
public interface PatientSipManager
{

   /**
    * Enregistre un patient venant du SIP dans la table temporaire.
    * Vérifie doublons. 
    * Si maxsize de la table temp est atteinte -> applique first in 
    * first out.
    * @param sipPatient
    * @param int max taille de la table à maintenir
    * @param isFusion true si methode appelee suite evt fusion patient
    */
   void createOrUpdatePatientInTempTableManager(PatientSip sipPatient, int max, boolean isFusion);

   /**
    * Modifie un patient dans la table temporaire si une modification 
    * est à appliquée.
    * @param sipPatient
    * @param isFusion true si methode appelee suite evt fusion patient
    */
   void updatePatientInTempTableManager(PatientSip sipPatient, boolean isFusion);

   /** 
    * Modifie de manière automatique un patient deja enregistré dans le 
    * système (synchronisation avec le serveur d'indentités patient). 
    * Trouve le patient par son nip, si modification à appliquer.
    * @param sipPatient
    * @param patient à synchroniser.
    */
   void updatePatientSystem(PatientSip sipPatient, Patient pat);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. Si l'objet est modifie donc a un id 
    * attribue par le SGBD, ce dernier est retire de la liste findAll.
    * @param patient PatientSip dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(PatientSip patient);

   /**
    * Supprime un objet de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param patient PatientSip à supprimer de la base de données.
    */
   void deleteByIdManager(PatientSip patient);

   /**
    * Recherche toutes les instances de Patient venant du SIP 
    * présentes dans la base dans la table temporaire.
    * @return List contenant les PatientSips.
    */
   List<PatientSip> findAllObjectsManager();

   /**
    * Recherche toutes les patients venant du Sip 
    * dont le nom est egal ou 'like' 
    * celui passé en parametre.
    * @param nom
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<PatientSip> findByNomLikeManager(String nom, boolean exactMatch);

   /**
    * Recherche toutes les patients venant du SIP dont le nip est egal
    * ou 'like' celui en parametre.
    * @param nip
    * @param boolean exactMatch
    * @return Liste de Patient.
    */
   List<PatientSip> findByNipLikeManager(String nip, boolean exactMatch);

   /**
    * Verifie si le patient doit être modifié (de manière automatique 
    * par synchronisation avec le serveur d'identité patient).
    * @param PatientSip
    * @return list de Fields qui sont l'objet des modifications
    */
   List<Field> isSynchronizedPatientManager(PatientSip pat);

   /**
    * Verifie si un patient venant du SIP correspond à une modification 
    *	d'un patient enregistré dans le système impliquant donc une 
    * synchronisation. Renvoie une erreur si plusieurs patients 
    * sont enregistrés dans le système pour le NIP du patient.
    * @param sipPatient
    * @return List de champs a modifier.
    */
   Patient doSynchronizePatientManager(PatientSip sipPatient);

   /**
    * Methode decoratrice de la methode de fusion de patientManager.
    * @see PatientManager.fusionPatientManager
    * @param p1 patient actif
    * @param p2 patient passif
    */
   void fusionPatientSystemManager(Patient p1, Patient p2);

   /**
    * Parse un message HL7 et extrait le segment PID dans un 
    * objet PATIENT_SIP
    * @param message HL7
    * @return objet PATIENT_SIP
    * @throws IOException
    */
   PatientSip parseHl7MessagePID(String message) throws IOException;

   /**
    * Parse un message HL7 pour extraire le NIP du segment MRG 
    * qui correspond à l'instruction d'une fusion.
    * Le nip est attribué à un PatientSip qui sera le patient 
    * passif. 
    * @param message
    * @return objet PATIENT_SIP
    * @throws IOException
    */
   PatientSip parseHl7MessageMRG(String message) throws IOException;

   /**
    * Recherche toutes les patients venant du SIP ayant un numero de sejour 
    * ou 'like' celui en parametre.
    * @param numero
    * @param boolean exactMatch
    * @return Liste de Patient.
    * @since 2.0.9
    */
   List<PatientSip> findByNumeroSejourManager(String numero, boolean exactMatch);
}
