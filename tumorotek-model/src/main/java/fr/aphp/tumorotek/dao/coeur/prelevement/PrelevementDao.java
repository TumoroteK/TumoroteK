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
package fr.aphp.tumorotek.dao.coeur.prelevement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Interface pour le DAO du bean de domaine Prelevement.
 * Interface créée le 22/09/09.
 * Modifiée le 13/03/2013
 *
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface PrelevementDao extends GenericDaoJpa<Prelevement, Integer>
{

   /**
    * Recherche tous les codes prelevements sauf celui dont l'id est passé 
    * en paramètre.
    * @param prelevementId Identifiant du prelevement que l'on souhaite
    * exclure de la liste retournée.
    * @param banque
    * @return une liste de codes prelevement.
    */
   List<String> findByExcludedIdCodes(Integer prelevementId, Banque banque);

   /**
    * Recherche les prelevements dont le code est 'like' le paramètre.
    * @param code Code pour lequel on recherche des prelevements.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByCode(String code);

   /**
    * Recherche les prelevements dont le code ou le numéro de labo
    * est 'like' le paramètre.
    * @param code Code pour lequel on recherche des prelevements.
    * @param banque Banque à laquelle appartient le prélèvement.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByCodeOrNumLaboWithBanque(String code, Banque banque);

   /**
    * Recherche les prelevements dont le code 
    * est 'like' le paramètre pour la plateforme spécifiée.
    * @param code Code pour lequel on recherche des prelevements.
    * @param pf Plateforme à laquelle appartient le prélèvement.
    * @return une liste de prelevements.
    * @since 2.1
    */
   List<Prelevement> findByCodeInPlateforme(String code, Plateforme pf);

   /**
    * Recherche les prelevements Ids dont le code ou le numéro de labo
    * est 'like' le paramètre.
    * @param code Code pour lequel on recherche des prelevements.
    * @param banque Banque à laquelle appartient le prélèvement.
    * @return une liste de prelevements.
    */
   List<Integer> findByCodeOrNumLaboWithBanqueReturnIds(String code, Banque banque);

   /**
    * Recherche les prelevements dont la date de prelevement est plus récente
    * que celle passée en paramètre.
    * @param date Date pour laquelle on recherche des prelevements.
    * @return une liste de prelevement.
    */
   List<Prelevement> findByDatePrelevementAfterDate(Calendar date);

   /**
    * Recherche les prelevements dont la date de prelevement est plus récente
    * que celle passée en paramètre.
    * @param date Date pour laquelle on recherche des prelevements.
    * @param banque Banque à laquelle appartient le prélèvement.
    * @return une liste de prelevement.
    */
   List<Prelevement> findByDatePrelevementAfterDateWithBanque(Calendar date, Banque banque);

   /**
    * Recherche les prelevements dont la date de consentement est plus récente
    * que celle passée en paramètre.
    * @param date Date pour laquelle on recherche des prelevements.
    * @return une liste de prelevement.
    */
   List<Prelevement> findByConsentDateAfterDate(Date date);

   //	/**
   //	 * Recherche les prelevements dont la date de congelation est plus récente
   //	 * que celle passée en paramètre.
   //	 * @param date Date pour laquelle on recherche des prelevements.
   //	 * @return une liste de prelevement.
   //	 */
   //	List<Prelevement> findByDateCongelationAfterDate(Date date);

   /**
    * Recherche les prelevements dont le type est passé en paramètre.
    * @param type PrelevementType pour lequel on recherche des prelevements.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByPrelevementType(PrelevementType type);

   /**
    * Recherche les prelevements dont la nature est passé en paramètre.
    * @param type Nature pour lequelle on recherche des prelevements.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByNature(Nature nature);

   /**
    * Recherche les prelevements dont le type de consentement
    * est passé en paramètre.
    * @param type ConsentType pour lequel on recherche des prelevements.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByConsentType(ConsentType type);

   /**
    * Recherche les prelevements pour une maladie passée en paramètre.
    * @param libelle de Maladie pour lequel on recherche des prelevements.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieLibelleLike(String libelle);

   /**
    * Recherche les prelevements pour le nda passée en paramètre.
    * @param patient nda.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByNdaLike(String nda);

   /**
    * Recherche les codes de prelevements dont la banque est passée en 
    * paramètre.
    * @param banque Banque des prelevements que l'on recherche.
    * @return une liste de codes de prelevements.
    */
   List<String> findByBanqueSelectCode(Banque banque);

   /**
    * Recherche les ndas des prelevements dont la banque est passée en 
    * paramètre.
    * @param banque Banque des prelevements que l'on recherche.
    * @return une liste de ndas
    */
   List<String> findByBanqueSelectNda(Banque banque);

   /**
    * Recherche les prelevements pour une maladie donnée et pour la banque.
    * @param Maladie maladie
    * @param Banque bank
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieAndBanque(Maladie mal, Banque bank);

   /**
    * Recherche les prelevements pour une maladie donnée et pour toutes les
    * banques excluant celle passée en paramètres.
    * @param Maladie maladie
    * @param Banque bank
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieAndOtherBanques(Maladie mal, Banque bank);

   /**
    * Compte les prelevements créés par un collaborateur 
    * passée en paramètre.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountCreatedByCollaborateur(Collaborateur colla);

   /**
    * Compte les prelevements dont le collaborateur est le preleveur 
    * passée en paramètre.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByPreleveur(Collaborateur colla);

   /**
    * Compte les prelevements auxquels est affecté un service 
    * passée en paramètres.
    * @param service
    * @return long
    */
   List<Long> findCountByService(Service serv);

   /**
    * Recherche les prélèvements pour une liste de collections spécifiées.
    * @param banks liste de collections
    * @return une liste de prelevements.
    */
   List<Prelevement> findByBanques(List<Banque> banks);

   /**
    * Trouve tous les prélèvements issus d'un patient.
    * @param pat
    * @return
    */
   List<Prelevement> findByPatient(Patient pat);

   /**
    * Trouve tous les prélèvements issus d'un patient.
    * @param pat
    * @return
    */
   List<Integer> findByPatientNomReturnIds(String nom, Banque banque);

   /**
    * Trouve tous les prélèvements issus d'un patient.
    * @param pat
    * @return
    */
   List<Prelevement> findByNumberEchantillons(Long nb);

   /**
    * Compte les prélèvements enregistrés dans le système dans 
    * l'intervalle inclusif de dates passées, associés à un des 
    * types de consentement et appartenant à une des banque 
    * passés en paramètres. 
    * @param types
    * @param cal1
    * @param cal2
    * @param banks
    * @return compte
    */
   List<Long> findCountEclConsentByDates(List<ConsentType> types, Calendar cal1, Calendar cal2, List<Banque> banks);

   /**
    * Compte les prélèvements enregistrés dans le système dans 
    * l'intervalle inclusif de dates passées, dont un échantillon  
    * est associé à un code/libelle organe et appartenant à une des banque 
    * passés en paramètres. 
    * @param liste de codes ou de libelles organe 
    * @param cal1
    * @param cal2
    * @param banks
    * @return compte
    */
   List<Prelevement> findByOrganeByDates(List<String> codeOrLibelle, Calendar cal1, Calendar cal2, List<Banque> banks);

   /**
    * Recherche les prélèvements enregistrés dans le système dans 
    * l'intervalle inclusif de dates passées, dont un échantillon  
    * est associé à un code/libelle organe, appartenant à une des banque 
    * passés en paramètres et associés à un des consentements spécifiés 
    * dans la liste. 
    * @param liste de codes ou de libelles organe 
    * @param cal1
    * @param cal2
    * @param banks
    * @param consents
    * @return liste de prélèvements
    */
   List<Prelevement> findByOrganeByDatesConsent(List<String> codeOrLibelle, Calendar cal1, Calendar cal2, List<Banque> banks,
      List<ConsentType> consents);

   /**
    * Recherche les prélèvements associés à la maladie, à un des types 
    * de prélèvements, et appartenant à une des banques passées en paramètres.
    * @param maladie
    * @param nats
    * @param banks
    * @return liste de prélèvements
    */
   List<Prelevement> findAssociatePrelsOfType(Maladie maladie, List<Nature> nats, List<Banque> banks);

   /**
    * Recherche les prelevements 
    * pour une maladie donnée et une nature enregistrés 
    * à une date précise.
    * @param Maladie maladie
    * @param nature Nature.
    * @param date Calendar
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieAndNature(Maladie mal, String nature, Calendar date);

   /**
    * Recherche les prelevements dont l'identifiant se trouve dans la liste.
    * @param ids Liste d'identifiants.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByIdInList(List<Integer> ids);

   /**
    * Recherche les ids des prlvts des banques de la liste.
    * @param banques Banques des prlvts recherchés.
    * @return Liste de Prelevements.
    */
   List<Integer> findByBanquesAllIds(List<Banque> banques);

   /**
    * Trouve le prélèvement parent d'un échantillon.
    * @param echantillon
    * @return le Prelevement parent
    */
   List<Prelevement> findByEchantillonId(Integer echan);

   /**
    * Recherche les prélèvements dont le code et la banque sont présents 
    * dans les listes passées en paramètres.
    * @param banques Banques des prlvts.
    * @param codes Codes des prlvts.
    * @return Liste de prélèvements.
    */
   List<Prelevement> findByCodesAndBanquesInList(List<String> codes, List<Banque> banques);

   /**
    * Recherche les prélèvements d'un contexte serotheque pour 
    * une valeur de libelle de diagnostic. 
    * @param libelle diagnostic complementaire
    * @param banques
    * @return Liste de prélèvements.
    */
   List<Prelevement> findByComDiag(String libelle, List<Banque> banques);

   /**
    * Recherche les prelevements Ids dont les patients ont un nom
    * ou un nip dans la liste.
    * @param criteres Criteres pour lesquels on recherche des prelevements.
    * @param banques Banques auxquelles appartiennent les prélèvements.
    * @return une liste d'ids.
    */
   List<Integer> findByPatientNomOrNipInList(List<String> criteres, List<Banque> banques);

   /**
    * Recherche les prelevements Ids dont le code ou le num de labo
    * est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des prelevements.
    * @param banques Banques auxquelles appartiennent les prélèvements.
    * @return une liste d'ids.
    */
   List<Integer> findByCodeOrNumLaboInListWithBanque(List<String> criteres, List<Banque> banques);

   /**
    * Recherche une liste de prélèvements en fonction du nom de 
    * l'établissement auquel appartient le service préleveur ou le 
    * medecin preleveur.
    * @param nom
    * @param banks
    * @return liste de prélèvements
    * @since 2.0.10
    */
   List<Prelevement> findByEtablissementNom(String nom, List<Banque> banks);

   /**
    * Recherche une liste de prélèvements non attribuable à un 
    * établissement prélèveur.
    * @param banks
    * @return liste de prélèvements
    * @since 2.0.10
    */
   List<Prelevement> findByEtablissementVide(List<Banque> banks);

   /**
    * Recherche les prelevements dont le preleveur est passé en param
    * @param colla
    * @return liste de prélèvements
    */
   List<Prelevement> findByPreleveur(Collaborateur preleveur);

   /**
    * Recherche les prelevements dont l'operateur est passé en param
    * @param colla
    * @return liste de prélèvements
    */
   List<Prelevement> findByOperateur(Collaborateur operateur);

   /**
    * Recherche les prelevements dont le service est passé en param
    * @param service
    * @return liste de prélèvements
    */
   List<Prelevement> findByService(Service service);

   /**
    * Recherche les prélèvements existants pour un patient et pour une liste de 
    * banques consultables
    * @param Patient patient
    * @param List<Banque> banks
    * @return liste de prélèvements
    * @since 2.0.13
    */
   List<Prelevement> findByPatientAndBanques(Patient p, List<Banque> banks);
}
