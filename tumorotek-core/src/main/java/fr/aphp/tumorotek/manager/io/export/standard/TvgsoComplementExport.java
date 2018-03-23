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

import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Classe regroupant les methodes statiques récupérant les items
 * du bloc RENSEIGNEMENTS COMPLEMENTAIRES spécifié par l'export INCa/TVGSO.
 * Date: 22/09/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface TvgsoComplementExport extends IncaComplementExport
{

   /**
    * Données cliniques disponibles dans une base.
    * @param con
    * @param prelevement
    * @return "0", "N" ou ""
    */
   String getDonneesClinBase(Connection con, Prelevement prelevement);

   /**
    * Inclusion dans un protocole therapeutique.
    * @param con
    * @param prelevement
    * @return "O", "N" ou ""
    */
   String getInclusionTherap(Connection con, Prelevement prelevement);

   /**
    * Nom du protocole therapeutique.
    * @param con
    * @param prelevement
    * @return Nom du protocole ou ""
    */
   String getNomProtocoleTherap(Connection con, Prelevement prelevement);

   /**
    * Description de l'anomalie genomique. Annotation Texte ou Thesaurus sinon.
    * @param con
    * @param patient
    * @param banque
    * @return descrption de l'anomalie ou ""
    */
   @Override
   String getAnomalieGenomiqueDescr(Connection con, Patient patient, Banque bank);

   /**
    * Contrôle qualité au niveau de l'échantillon ou de ses dérivés si
    * aucune qualité n'est spécifiée pour l'échantillon.
    * @param echantillon
    * @return "O", "N" ou ""
    */
   @Override
   String getControleQualite(Echantillon echantillon);

   /**
    * Inclusion dans un protocole de recherche.
    * @param con
    * @param echantillon
    * @return "O", "N" ou ""
    */
   String getInclusionProtocoleRech(Connection con, Echantillon echantillon);

   /**
    * Nom du protocole de recherche.
    * @param con
    * @param echantillon
    * @return Nom du protocole ou ""
    */
   String getNomProtocoleRech(Connection con, Echantillon echantillon);

   /**
    * Champ specifique du cancer.
    * @param con
    * @param patient
    * @param banque
    * @return valeur du champ ou ""
    */
   String getChampSpecCancer(Connection con, Patient patient, Banque banque);

   /**
    * Consentement du patient.
    * @param prelevement
    * @param oType regexp type AUTORISATION
    * @param nType regexp type REFUS/ATTENTE
    * @return O, N du champ ou ""
    */
   String getConsentement(Prelevement prelevement, String oType, String nType);

   /**
    * Nom, prenom et coordonnées du responsable de la collection.
    * OBLIGATOIRE non vide.
    * @param banque
    * @param Nom prenom
    */
   String getContactShort(Banque banque);

}
