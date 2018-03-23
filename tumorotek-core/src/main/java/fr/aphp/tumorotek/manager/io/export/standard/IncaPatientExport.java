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
package fr.aphp.tumorotek.manager.io.export.standard;

import java.sql.Connection;
import java.text.DateFormat;

import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Classe regroupant les methodes récupérant les items
 * du bloc INFORMATIONS PATIENT spécifié par l'export INCa/TVGSO.
 * Date: 01/08/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface IncaPatientExport
{

   /**
    * Item 1 Lieu prise en charge du patient: FINESS OBLIGATOIRE.
    * @param con
    * @param prelevement
    * @param patient
    * @return FINESS
    */
   String getFiness(Connection con, Prelevement prel, Patient pat);

   /**
    * Item 2 Identification du Patient: OBLIGATOIRE.
    * ID TK système
    * @param patient
    * @return patient_id
    */
   String getPatientId(Patient pat);

   /**
    * Item 3 Date de naissance du patient OBLIGATOIRE.
    * @param patient
    * @param DateFormat df
    * @return date de naissance formatée ou ""
    */
   String getDateNaissance(Patient pat, DateFormat df);

   /**
    * Item 4 Sexe du patient OBLIGATOIRE.
    * Valeur par défaut I si null ou autre que M, F, I
    * @param patient
    * @return sexe (M, F, I) ou ""
    */
   String getSexe(Patient pat);

   /**
    * Item 5 Etat du patient. "I" par défaut
    * OBLIGATOIRE.
    * @param patient
    * @return Etat
    */
   String getPatientEtat(Patient pat);

   /**
    * Item 6 Date de l'etat du patient.
    * OBLIGATOIRE.
    * @param patient
    * @param DateFormat
    * @return Date as String
    */
   String getDateEtat(Patient pat, DateFormat df);

   /**
    * Item 7 Cause de décès du patient. "" par défaut
    * @param con
    * @param patient
    * @param bank
    * @return Cause [1-9] ou ""
    */
   String getCauseDeces(Connection con, Patient pat, Banque bank);

}
