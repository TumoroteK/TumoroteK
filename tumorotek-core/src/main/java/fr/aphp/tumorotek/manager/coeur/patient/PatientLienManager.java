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

import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;

/**
 *
 * Interface pour le manager du bean de domaine PatientLien.<br>
 * Interface créée le 29/10/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un lien familial (controle de doublons)
 * 	- Modifier un lien familial (controle de doublons)
 * 	- Supprimer un type de lien familial
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface PatientLienManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param patientLien à créer
    * @param patient1 Patient1 associé (non null)
    * @param patient2 Patient2 associé (non null)	 
    * @param lienFamilial lien familial associé (non null)	 
    * @param operation String creation / modification
    */
   void createOrUpdateObjectManager(PatientLien patientLien, Patient patient1, Patient patient2, LienFamilial lienFamilial,
      String operation);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites. 
    * @param patientLien PatientLien dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(PatientLien patientLien);

   /**
    * Supprime un objet de la base de données.
    * @param patientLien PatientLien à supprimer de la base de données.
    */
   void removeObjectManager(PatientLien patientLien);
}
