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

import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 *
 * Interface pour le manager du bean de domaine PatientMedecin.<br>
 * Interface créée le 02/11/09.<br>
 * <br>
 * Actions:<br>
 * 	- Enregistrer un medecin referent (controle de doublons)
 * 	- Modifier un l'ordre du medecin referent (controle de doublons)
 * 	- Supprimer un medecin referent
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface PatientMedecinManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param TableAnnotationBanque medecin à créer
    * @param patient associe (non null)
    * @param collaborateur Collaborateur associe (non null)	 
    * @param ordre lien familial associee (non null)	 
    * @param operation String creation / modification
    */
   void createOrsaveManager(PatientMedecin medecin, Patient patient, Collaborateur collaborateur, String operation);

   /**
    * Cherche les doublons en se basant sur la methode equals()
    * surchargee par les entites.
    * @param medecin PatientMedecin dont on cherche la presence dans la base
    * @return true/false
    */
   boolean findDoublonManager(PatientMedecin medecin);

   /**
    * Supprime un objet de la base de données.
    * @param medecin PatientMedecin à supprimer de la base de données.
    */
   void deleteByIdManager(PatientMedecin medecin);
}
