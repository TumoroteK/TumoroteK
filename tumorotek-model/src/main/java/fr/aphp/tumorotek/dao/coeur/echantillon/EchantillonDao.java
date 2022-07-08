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
package fr.aphp.tumorotek.dao.coeur.echantillon;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine Echantillon.
 * Interface créée le 21/09/09.
 *
 * @author Pierre Ventadour
 *
 * @version 2.1
 *
 */
public interface EchantillonDao extends GenericDaoJpa<Echantillon, Integer>
{

   /**
    * Recherche les échantillons dont le code est égal au paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByCode(String code);

   /**
    * Recherche les échantillons dont l'id est dans la liste.
    * @param ids Liste d'identifiants.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByIds(List<Integer> ids);

   /**
    * Recherche les échantillons dont le code est égal au paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByCodeWithBanque(String code, Banque banque);

   /**
    * Recherche les échantillons dont le code
    * est 'like' le paramètre pour la plateforme spécifiée.
    * @param code Code pour lequel on recherche des échantillons.
    * @param pf Plateforme.
    * @return une liste d'échantillons.
    * @since 2.1
    */
   List<Echantillon> findByCodeInPlateforme(String code, Plateforme pf);

   /**
    * Recherche les Ids d'échantillons dont le code est égal au paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return une liste d'échantillons.
    */
   List<Integer> findByCodeWithBanqueReturnIds(String code, Banque banque);

   /**
    * Recherche les échantillons dont la date de stockage est plus récente
    * que celle passée en paramètre.
    * @param date Calendar pour laquelle on recherche des échantillons.
    * @param banque Banque à laquelle appartient l'échantillon.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByDateStockAfterDateWithBanque(Calendar date, Banque banque);

   /**
    * Recherche les échantillons dont la date de stockage est plus récente
    * que celle passée en paramètre.
    * @param date Calendar pour laquelle on recherche des échantillons.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByDateStockAfterDate(Calendar date);

   /**
    * Recherche tous les codes Echantillons, sauf celui dont l'id est passé
    * en paramètre.
    * @param echantillonId Identifiant de l'Echantillon que l'on souhaite
    * exclure de la liste retournée.
    * @param bank
    * @return une liste de codes Echantillons.
    */
   List<String> findByExcludedIdCodes(Integer echantillonId, Banque bank);

   /**
    * Recherche les échantillons dont le statut est passé en paramètre.
    * @param statut ObjetStatut pour lequel on recherche des échantillons.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByObjetStatut(ObjetStatut statut);

   /**
    * Recherche les échantillons dont la qualité est passée en paramètre.
    * @param qualite EchanQualite des échantillons que l'on recherche.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByEchanQualite(EchanQualite qualite);

   /**
    * Recherche les échantillons dont le type est passé en paramètre.
    * @param type EchantillonType pour lequel on recherche des échantillons.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByEchantillonType(EchantillonType type);

   //	/**
   //	 * Recherche les échantillons dont le crAnapath est passé en paramètre.
   //	 * @param crAnapath Fichier des échantillons que l'on recherche.
   //	 * @return une liste d'échantillons.
   //	 */
   //	List<Echantillon> findByCrAnapath(Fichier crAnapath);

   /**
    * Recherche les échantillons dont le mode de préparation est passé
    * en paramètre.
    * @param modePrepa ModePrepa pour lequel on recherche des échantillons.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByModePrepa(ModePrepa modePrepa);

   /**
    * Recherche les échantillons dont le prélèvement est passé en
    * paramètre.
    * @param prelevement Prelevement des échantillons que l'on recherche.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByPrelevement(Prelevement prelevement);

   /**
    * Recherche les échantillons restants dont le prélèvement est passé en
    * paramètre et dont la quantite est supérieure à O.
    * @param prelevement Prelevement des échantillons que l'on recherche.
    * @return une liste d'échantillons.
    */
   List<Echantillon> findRestantsByPrelevement(Prelevement prelevement);

   /**
    * Recherche les échantillons dont le prélèvement et le statut sont passés
    * en paramètres.
    * @param prelevement Prelevement des échantillons que l'on recherche.
    * @param statut Statut des échantillons recherchés (STOCKE...).
    * @return une liste d'échantillons.
    */
   List<Echantillon> findByPrelevementAndStatut(Prelevement prelevement, ObjetStatut statut);

   /**
    * Recherche les codes d'échantillons dont la banque est passée en
    * paramètre.
    * @param banque Banque des échantillons que l'on recherche.
    * @return une liste de codes d'échantillons.
    */
   List<String> findByBanqueSelectCode(Banque banque);

   /**
    * Recherche les codes d'échantillons dont la banque est passée en
    * paramètre et la quantité est non égale à 0.
    * @param banque Banque des échantillons que l'on recherche.
    * @return une liste de codes d'échantillons.
    */
   List<String> findByBanqueAndQuantiteSelectCode(Banque banque);

   /**
    * Recherche les codes d'échantillons dont la banque est passée en
    * paramètre et la quantité est non égale à 0 OU l'échantillon faisant partie d'une cession de type traitement
    * @param banque Banque des échantillons que l'on recherche.
    * @return une liste de codes d'échantillons.
    */
   List<String> findAllCodesByBanqueAndQuantiteNotNullOrInCessionTraitement(Banque banque);

   /**
    * Recherche les codes d'échantillons dont la banque et le statut
    * sont passés en paramètres.
    * @param banque Banque des échantillons que l'on recherche.
    * @param objetStatut ObjetStatut des échantillons.
    * @return une liste de codes d'échantillons.
    */
   List<String> findByBanqueStatutSelectCode(Banque banque, ObjetStatut objetStatut);

   /**
    * Recherche les codes d'échantillons dont la banque et le statut
    * sont passés en paramètres.
    * @param banques Liste des Banques des échantillons que
    * l'on recherche.
    * @param objetStatut ObjetStatut des échantillons.
    * @return une liste de codes d'échantillons.
    */
   List<String> findByBanqueInListStatutSelectCode(List<Banque> banques, ObjetStatut objetStatut);

   /**
    * Recherche les échantillons d'une enceinte terminale.
    * @param entite Entité représentant les échantillons.
    * @param terminale Enceinte terminale
    * @return Liste d'échantillons.
    */
   List<Echantillon> findByTerminale(Entite entite, Terminale terminale);

   /**
    * Recherche les échantillons d'une enceinte terminale.
    * @param entite Entité représentant les échantillons.
    * @param terminale Enceinte terminale
    * @return Liste d'échantillons.
    */
   List<Echantillon> findByTerminaleDirect(Terminale terminale);

   /**
    * Recherche les Echantillons pour une liste de collections spécifiées.
    * @param banks liste de collections
    * @return Une liste de Echantillons.
    */
   List<Echantillon> findByBanques(List<Banque> banks);

   /**
    * Recherche les Ids d'Echantillons pour un patient donnée.
    * @param nom Nom du patient.
    * @return Une liste de Echantillons.
    */
   List<Integer> findByPatientNomReturnIds(String nom, Banque banque);

   /**
    * INCa reporting: compte les echantillons dont la date de congélation
    * est incluse dans les dates passées en paramètres et qui appartiennent
    * à la banque. Une liste d'établissement peut être passée en paramètre
    * pour exclure tous les échantillons techniqués dans ces derniers (pour
    * compter les échantillons extérieurs aux établissement
    * locaux par exemple).
    * @param d1 date limite inf
    * @param d2 date limite sup
    * @param banks liste de Banque
    * @return Integer compte sous la forme d'une liste
    */
   List<Long> findCountSamplesByDates(Calendar d1, Calendar d2, List<Banque> banks);

   /**
    * INCa reporting: compte les echantillons dont la date de congélation
    * est incluse dans les dates passées en paramètres et qui appartiennent
    * à la banque. Une liste d'établissement est passée en paramètre
    * pour exclure tous les échantillons techniqués dans ces derniers (pour
    * compter les échantillons extérieurs aux établissement
    * locaux par exemple).
    * @param d1 date limite inf
    * @param d2 date limite sup
    * @param banks liste de Banque
    * @param exts liste d'Etablissement
    * @return Integer compte sous la forme d'une liste
    */
   List<Long> findCountSamplesByDatesExt(Calendar d1, Calendar d2, List<Banque> banks, List<Etablissement> exts);

   /**
    * Compte les échantillons cédés dans le cadre de cession dont
    * le type est passé en paramètre et dont la date de
    * validation/destruction et incluses entre les dates passées
    * en paramètres. Les échantillons comptés doivent appartenir
    * à la liste de banque passée en paramètres.
    * @param cType
    * @param d1
    * @param d2
    * @param banks
    * @return compte
    */
   List<Long> findCountEchansByCessTypes(CessionType cType, Date d1, Date d2, List<Banque> banks);

   /**
   <<<<<<< .working
    * Compte les échantillons auxquels est affecté un collaborateur
    * passée en paramètres.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByCollaborateur(Collaborateur colla);

   /**
    * Compte les échantillons auxquels est affecté un collaborateur
    * passée en paramètres.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountCreatedByCollaborateur(Collaborateur colla);

   /**
    * Compte les échantillons dont l'opérateur est le collaborateur
    * passée en paramètres.
    * @param collaborateur
    * @return long
    */
   List<Long> findCountByOperateur(Collaborateur colla);

   /**
   
    * Recherche parmis les échantillons d'une maladie ceux dont le type
    * est un de ceux passé en paramètre et appartenant à une des banques
    * de la liste passée en paramètre. En cas de maladie nulle, utilise le
    * critère prélèvement.
    * @param maladie
    * @param types
    * @param banks
    * @param Prelevement
    * @return liset échantillons.
    */
   List<Echantillon> findAssociateEchansOfType(Maladie maladie, List<EchantillonType> types, List<Banque> banks,
      Prelevement prelevement);

   /**
    * Recherche les Echantillons pour une maladie et un type donnés, issus
    * d'un prélèvement à la date passée en paramètre.
    * @param maladie Maladie.
    * @param type EchantillonType.
    * @param date Calendar date de Prélèvement
    * @return Une liste de Echantillons.
    */
   List<Echantillon> findByMaladieAndType(Maladie maladie, String type, Calendar date);

   /**
    * Recherche les ids des échantillons des banques de la liste.
    * @param banques Banques des échantillons recherchés.
    * @return Liste de Echantillons.
    */
   List<Integer> findByBanquesAllIds(List<Banque> banques);

   /**
    * Compte le nombre d'échantillons d'un prélèvement.
    */
   List<Long> findCountByPrelevement(Prelevement prlvt);

   /**
    * Compte le nombre d'échantillons restants (qté > 0) d'un prélèvement.
    */
   List<Long> findCountRestantsByPrelevement(Prelevement prlvt);

   /**
    * Compte le nombre d'échantillons d'un prélèvement qui sont stockés
    * ou reservés.
    */
   List<Long> findCountByPrelevementAndStockeReserve(Prelevement prlvt);

   /**
    * Recherche les échantillons Ids dont les patients ont un nom
    * ou un nip dans la liste.
    * @param criteres Criteres pour lesquels on recherche des échantillons.
    * @param banques Banques auxquelles appartiennent les échantillons.
    * @return une liste d'ids.
    */
   List<Integer> findByPatientNomOrNipInList(List<String> criteres, List<Banque> banques);

   /**
    * Recherche les échantillons Ids dont le code ou le num de labo
    * est dans la liste.
    * @param criteres Criteres pour lesquels on recherche des échantillons.
    * @param banques Banques auxquelles appartiennent les échantillons.
    * @return une liste de tuples ids / code.
    * @version 2.1
    */
   List<Object[]> findByCodeInListWithBanque(List<String> criteres, List<Banque> banques);

   List<Echantillon> findByCollaborateur(Collaborateur cP);

   /**
    * Recherche les échantillons pour cet emplacement, caractérisé par une position
    * dans une terminale (car l'emplacement peut ne pas être créé).
    * Cette recherche a pour objectif un contrôle de validation à la
    * création des nouveaux objets.
    * @param Terminale
    * @param Integer position
    * @return liste Echantillons
    */
   List<Echantillon> findByEmplacement(Terminale terminale, Integer position);

   /**
    * Recherche des échnatillons correspondant à une liste de code dans une plateforme donnée
    * @param codes liste des codes à rechercher
    * @param pf plateforme dans laquelle effectuer la recherche
    */
   List<Echantillon> findByCodeInListWithPlateforme(List<String> codes, Plateforme pf);
}
