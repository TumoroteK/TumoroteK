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
package fr.aphp.tumorotek.manager.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Etablissement;

/**
 *
 * Interface pour le Manager executant les requêtes composant
 * le bilan d'activités de l'INCa.
 * Interface créée le 19/05/10.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface IncaReportManager
{

   /**
    * Compte les échantillons traites en fonction de leur 
    * date de creation sans tenir compte de leur provenance. 
    * Le compte est incrémenté suivant un intervalle de temps.
    * @param cal1 Date limite inf
    * @param cal2 Date limite sup
    * @param interv en jours
    * @param banks
    * @param cumulative true si les comptes doivent s'incrémenter
    * @return
    */
   List<Long> countSamplesManager(Calendar cal1, Calendar cal2, Integer interv, List<Banque> banks, boolean cumulative);

   /**
    * Compte les échantillons traites en fonction de leur 
    * date de creation et de leur provenance.
    * Le compte est incrémenté suivant un intervalle de temps.
    * @param cal1 Date limite inf
    * @param cal2 Date limite sup
    * @param interv en jours
    * @param banks
    * @param exts Etablissements prélèveurs dont les échantillons doivent 
    * être exclus.
    * @return
    */
   List<Long> countSamplesExtManager(Calendar cal1, Calendar cal2, Integer interv, List<Banque> banks, List<Etablissement> exts);

   /**
    * Compte les échantillons cédés dans le cadre de cession dont 
    * le type est passé en paramètre et dont la date de 
    * validation/destruction et incluses entre les dates passées 
    * en paramètres. Les échantillons comptés doivent appartenir 
    * à la liste de banque passée en paramètres.
    * @param cType
    * @param d1
    * @param d2
    * @param interv en jours
    * @param banks
    * @return
    */
   List<Long> countEchansByCessTypesManager(CessionType cType, Date d1, Date d2, Integer interv, List<Banque> banks);

   /**
    * Compte les prélèvements (et toutes les données qui 
    * lui sont associées suivant les demandes de l'INCa) enregistrés 
    * dans le système dans l'intervalle défini par les deux dates 
    * passées en paramètres, appartenant à une des banques passées 
    * dans la liste, et dont un échantillon correspond au code organe passé en 
    * paramètre. 
    * Le compte se fait sur tous les codes CIM contenu 
    * dans l'arborescence du code passé en paramètre sous la forme de l'objet 
    * code d'un String (cofr ou libelle).
    * Le compte du matériel sain associé se fait sur les prèlevements 
    * ainsi que sur tous les échantillons issus de la même maladie.
    * @param code CIM code organe
    * @param value libre
    * @param date limite inf intervalle
    * @param date limite sup intervalle
    * @param liste de banques
    * @param liste de types prélévements considérés comme tissus sains
    * @param liste de types échantillons considérés comme tissus sains
    * @param liste de types prélévements considérés comme sang
    * @param liste de types échantillons considérés comme sang
    * @param liste de types consentements considérés comme éclairés
    * @return liste de compte et pourcentages
    */
   List<Long> countsPrelsAndAssociatesByCimManager(CimMaster code, String value, Calendar cal1, Calendar cal2, List<Banque> banks,
      List<Nature> sainTypes, List<EchantillonType> sainEchanTypes, List<Nature> sangTypes, List<EchantillonType> sangEchanTypes,
      List<ConsentType> consents);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence peut être est celle d'enregistrement 
    * du patient dans le système ou la date de prélèvement.
    * Le compte est incrémenté suivant un intervalle de temps.
    * @param cal1
    * @param cal2
    * @param interv en jours
    * @param banks
    * @param cumulative cumuler les comptes sur les banques
    * @param datePrel si date utilisée est la date de prélèvement
    * @return compte
    */
   List<Long> countPrelevedByDatesManager(Calendar cal1, Calendar cal2, Integer interv, List<Banque> banks, boolean cumulative,
      boolean datePrel);

   /**
    * Compte les patient qui ont été enregistrés par le système suite
    * à un prélèvements enregistré dans une des banques passées en paramètres.
    * La date utilisée comme référence peut être celle 
    * d'enregistrement du patient 
    * dans le système ou la date de prélèvement
    * Le compte est incrémenté suivant un intervalle de temps.
    * @param cal1
    * @param cal2
    * @param interv en jours
    * @param banks
    * @param exts Etablissements prélèveurs dont les échantillons doivent 
    * être exclus.
    * @param datePrel si date utilisée est la date de prélèvement
    * @return compte
    */
   List<Long> countPrelevedByDatesExtManager(Calendar cal1, Calendar cal2, Integer interv, List<Banque> banks,
      List<Etablissement> exts, boolean datePrel);

   /**
    * Compte les prélèvements enregistrés dans le système dans 
    * l'intervalle inclusif de dates passées, associés à un des 
    * types de consentement et appartenant à une des banque 
    * passés en paramètres. 
    * Le compte est incrémenté suivant un intervalle de temps.
    * @param types
    * @param cal1
    * @param cal2
    * @param interv en jours
    * @param banks
    * @return compte
    */
   List<Long> countEclConsentByDatesManager(List<ConsentType> types, Calendar cal1, Calendar cal2, Integer interv,
      List<Banque> banks);
}
