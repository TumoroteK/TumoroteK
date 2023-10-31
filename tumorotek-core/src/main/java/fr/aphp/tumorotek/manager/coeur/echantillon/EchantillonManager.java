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
package fr.aphp.tumorotek.manager.coeur.echantillon;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Echantillon.
 * Interface créée le 25/09/09.
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public interface EchantillonManager
{

   /**
    * Recherche un échantillon dont l'identifiant est passé en paramètre.
    * @param echantillonId Identifiant de l'échantillon que l'on recherche.
    * @return Un Echantillon.
    */
   Echantillon findByIdManager(Integer echantillonId);

   /**
    * Recherche tous les échantillons présents dans la base.
    * @return Liste de Echantillons.
    */
   List<Echantillon> findAllObjectsManager();

   /**
    * Recherche tous les échantillons dont l'id est dans la liste.
    * @param ids Liste des identifiants.
    * @return Liste de Echantillons.
    */
   List<Echantillon> findByIdsInListManager(List<Integer> ids);

   /**
    * Recherche les échantillons pour une liste de banques.
    * @param banks Liste des banques.
    * @return Une liste d'échantillons
    */
   List<Echantillon> findByBanquesManager(List<Banque> banks);

   /**
    * Recherche tous les ids des Echantillons des banque.
    * @param banques liste de Banques pour lesquelles on recherche.
    * @return Liste de Echantillons.
    */
   List<Integer> findAllObjectsIdsByBanquesManager(List<Banque> banques);

   /**
    * Recherche une liste d'échantillons dont le type est passé en paramètre.
    * @param echantillonType EchantillonType pour lequel on recherche des
    * échantillons.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByEchantillonTypeManager(EchantillonType type);

   /**
    * Recherche une liste d'échantillons dont la date est plus récente que
    * celle passée en paramètre.
    * @param date Date pour laquelle on recherche des échantillons.
    * @param banques Banque auxquelles appartiennent les échantillons.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByDateStockAfterDateWithBanqueManager(Date date, List<Banque> banque);

   /**
    * Recherche une liste d'échantillons dont la date est plus récente que
    * celle passée en paramètre.
    * @param date Date pour laquelle on recherche des échantillons.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByDateStockAfterDateManager(Date date);

   /**
    * Recherche une liste d'échantillons restants (quantite ou volume 
    * superieurs à 0) pour le prelevement passé en paramètre.
    * @param prelevement Prelevement pour lequel 
    * on recherche des échantillons restants.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findRestantsByPrelevementManager(Prelevement p);

   /**
    * Recherche une liste d'échantillons pour le prelevement et le statut
    * passés en paramètre.
    * @param prelevement Prelevement pour lequel 
    * on recherche des échantillons.
    * @param statut Statut des échantillons recherchés.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByPrelevementAndStatutManager(Prelevement p, ObjetStatut statut);

   /**
    * Recherche une liste d'échantillons dont le code commence comme
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByCodeLikeManager(String code, boolean exactMatch);

   /**
    * Recherche une liste d'échantillons dont le code commence comme
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param banque Banque à laquelle appartient l'échantillon.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findByCodeLikeWithBanqueManager(String code, Banque banque, boolean exactMatch);

   /**
    * Recherche une liste d'Ids d'échantillons dont le code contient
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des échantillons.
    * @param banques Banque auxquelles appartiennent les échantillons.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'Echantillons.
    */
   List<Integer> findByCodeLikeBothSideWithBanqueReturnIdsManager(String code, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche les Ids d'échantillons d'un patient.
    * @param nom Nom du patient.
    * @param exactMatch
    * @return Liste d'échantillons.
    * @version 2.0.10
    */
   List<Integer> findByPatientNomReturnIdsManager(String nom, List<Banque> banks, boolean exactMatch);

   /**
    * Recherche tous les échantillons Ids dont le code
    * est présent dans la liste passée en paramètres.
    * @param criteres Liste de cocde d'échantillons.
    * @param banks Liste des banques des échantillons.
    * @param notfounds liste codes non trouvés
    * @return Liste d'ids d'échantillons.
    * @version 2.1
    */
   List<Integer> findByCodeInListManager(List<String> criteres, List<Banque> banques, List<String> notfounds);

   /**
    * Recherche tous les échantillons ids issus des patients passés
    * en paramètres.
    * @param criteres Liste des noms ou nips de patients.
    * @param banks Liste des banques des échantillons.
    * @return Liste d'ids échantillons.
    */
   List<Integer> findByPatientNomOrNipInListManager(List<String> criteres, List<Banque> banks);

   /**
    * Recherche le prélèvement dont l'échantillon est passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche un
    * prélèvement.
    * @return Un Prelevement.
    */
   Prelevement getPrelevementManager(Echantillon echantillon);

   /**
    * Recherche une liste de produits dérivés dont l'échantillon est 
    * passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche des
    * produits dérivés.
    * @return List de ProdDerives.
    */
   List<ProdDerive> getProdDerivesManager(Echantillon echantillon);

   /**
    * Recherche un emplacement dont l'échantillon est 
    * passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche un
    * emplacement.
    * @return Emplacement de l'échantillon.
    */
   Emplacement getEmplacementManager(Echantillon echantillon);

   /**
    * Recherche d'adresse de l'emplacement dont l'échantillon est 
    * passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche 
    * l'adresse de son emplacement.
    * @return Adresse de l'emplacement de l'échantillon.
    */
   String getEmplacementAdrlManager(Echantillon echantillon);

   /**
    * Recherche la liste des codes utilisés par les échantillons liés à la
    * banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueManager(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les échantillons liés à la
    * banque passée en paramètre et dont la quantité n'est pas égale à 0.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueAndQuantiteManager(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les échantillons pour associer à un produit dérivé :
    * Echantillons liés à la banque passée en paramètre et dont la quantité n'est pas égale à 0, ou dans une cession de type traitement
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForDerivesByBanque(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les échantillons liés à la
    * banque passée en paramètre et qui sont stockés.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueAndStockesManager(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les échantillons liés à la
    * banque passée en paramètre et qui sont stockés.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForMultiBanquesAndStockesManager(List<Banque> banques);

   /**
    * Recherche les doublons (= tous les échantillons appartenant à la même 
    * plateforme et partageant le même code) de l'Echantillon passé en paramètre.
    * @param echantillon Un Echantillon pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    * @version 2.1
    */
   Boolean findDoublonManager(Echantillon echantillon);

   /**
    * Test si un échantillon est lié à des produits dérivés.
    * @param echantillon Echantillon que l'on souhaite tester.
    * @return Vrai si l'échantillon référence des dérivés.
    */
   Boolean isUsedObjectManager(Echantillon echantillon);

   /**
    * Test si un échantillon est associé à une ou plusieurs cessions.
    * @param echantillon Echantillon que l'on souhaite tester.
    * @return Vrai l'échantillon a été cédé une fois au moins.
    */
   Boolean isCessedObjectManager(Echantillon echantillon);

   /**
    * Recherche tous les échantillons dont la date de creation systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banques Banque auxquelles appartiennent les échantillons.
    * @return Liste d'Echantillon.
    */
   List<Integer> findAfterDateCreationReturnIdsManager(Calendar date, List<Banque> banques);

   /**
    * Recherche tous les échantillons dont la date de modification systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banques Banque auxquelles appartiennent les échantillons.
    * @return Liste d'Echantillon.
    */
   List<Echantillon> findAfterDateModificationManager(Calendar date, Banque banque);

   /**
    * Recherche les derniers Echantillons créés dans le systeme.
    * @param banques Banques auxquelles appartiennent les Echantillons.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste d'Echantillons.
    */
   List<Echantillon> findLastCreationManager(List<Banque> banques, int nbResults);

   /**
    * Teste les items 50 à 53 pour un échantillon.
    * Modifiée par Mathieu le 22/09/2011 pour intégrer le fait que les 
    * ressources associées doivent être issues de prelevements à la 
    * même date de prelevement.
    * @param echantillon Echantillon à tester.
    * @param liste de banques dans lesquelles rechercher les données associées 
    * à l'échantillon.
    * @return True ou false.
    */
   boolean itemINCa50To53Manager(Echantillon echantillon, String value);

   /**
    * Persist une instance d'Echantillon dans la base de données.
    * @param echantillon Nouvelle instance de l'objet à créer.
    * @param banque Banque associée à l'échantillon (obligatoire).
    * @param prelevement Prlvt associé (obligatoire).
    * @param collaborateur Collaborateur associé.
    * @param statut Statut de l'échantillon.
    * @param emplacement Emplacement de stockage de l'échantillon.
    * @param type Type de l'échantillon (obligatoire).
    * @param codesAssigne de codes assignes.
    * @param quantite Unité de la quantité.
    * @param qualite Qualité de l'échantillon.
    * @param preparation Mode de préparation de l'échantillon.
    * @param listAnnoToCreateOrUpdate des valeurs d'annotation à enregistrer
    * @param filesCreated liste des fichiers créés physiquement 
    * lors de la transaction (utilisés pour rollback)
    * @param utilisateur Utilisateur ayant créé l'échantillon.
    * @param baseDir directory pour enregistrer un fichier associé
    * dans le file system
    * @param doValidation True : la validation sera faite.
    */
   void createObjectManager(Echantillon echantillon, Banque banque, Prelevement prelevement, Collaborateur collaborateur,
      ObjetStatut statut, Emplacement emplacement, EchantillonType type, List<CodeAssigne> codesAssigne, Unite quantite,
      EchanQualite qualite, ModePrepa preparation, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      List<File> filesCreated, Utilisateur utilisateur, boolean doValidation, String baseDir, boolean isImport);

   /**
    * Surcharge de la creation d'un echantillon pour intégrer à 
    * la transaction la création d'un fichier associé.
    * @param echantillon Nouvelle instance de l'objet à créer.
    * @param banque Banque associée à l'échantillon (obligatoire).
    * @param prelevement Prlvt associé (obligatoire).
    * @param collaborateur Collaborateur associé.
    * @param statut Statut de l'échantillon.
    * @param emplacement Emplacement de stockage de l'échantillon.
    * @param type Type de l'échantillon (obligatoire).
    * @param liste de codes assignes.
    * @param quantite Unité de la quantité.
    * @param qualite Qualité de l'échantillon.
    * @param preparation Mode de préparation de l'échantillon.
    * @param anapath CrAnapath de l'échantillon.
    * @param stream contenu du CrAnapath
    * @param filesCreated liste des fichiers créés physiquement 
    * lors de la transaction (utilisés pour rollback)
    * @param utilisateur Utilisateur ayant créé l'échantillon.
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    * @since 2.0.11
    */
   void createObjectWithCrAnapathManager(Echantillon echantillon, Banque banque, Prelevement prelevement,
      Collaborateur collaborateur, ObjetStatut statut, Emplacement emplacement, EchantillonType type,
      List<CodeAssigne> codesAssigne, Unite quantite, EchanQualite qualite, ModePrepa preparation, Fichier anapath,
      InputStream anapathStream, List<File> filesCreated, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      Utilisateur utilisateur, boolean doValidation, String baseDir, boolean isImport);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * Enregistre les modifications sur le compte-rendu anapath: 
    * Si le fichier est réferencé par plusieurs echantillons, la methode 
    * entraine la creation d'un nouveau fichier et d'une nouvelle reference 
    * pour l'echantillon qui demande la modification.
    * @param echantillon Objet à mettre à jour dans la base.
    * @param banque Banque associée à l'échantillon (obligatoire).
    * @param prelevement Prlvt associé (obligatoire).
    * @param collaborateur Collaborateur associé.
    * @param statut Statut de l'échantillon.
    * @param emplacement Emplacement de stockage de l'échantillon.
    * @param type Type de l'échantillon (obligatoire).
    * @param liste de codes assignes.
    * @param liste de codes assignes a supprimer.
    * @param quantite Unité de la quantité.
    * @param qualite Qualité de l'échantillon.
    * @param preparation Mode de préparation de l'échantillon.
    * @param liste des valeurs d'annotation à enregistrer
    * @param liste des valeurs d'annotation à supprimer. 
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param utilisateur Utilisateur ayant modifié l'échantillon.
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void updateObjectManager(Echantillon echantillon, Banque banque, Prelevement prelevement, Collaborateur collaborateur,
      ObjetStatut statut, Emplacement emplacement, EchantillonType type, List<CodeAssigne> codesAssigne,
      List<CodeAssigne> codesToDelete, Unite quantite, EchanQualite qualite, ModePrepa preparation,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, List<File> filesCreated,
      List<File> filesToDelete, Utilisateur utilisateur, boolean doValidation, List<OperationType> operations, String baseDir);

   /**
    * Surcharge de la modification d'un echantillon pour intégrer à 
    * la transaction la création/modification d'un fichier associé.
    * Si le fichier est réferencé par plusieurs echantillons, la methode 
    * entraine la creation d'un nouveau fichier et d'une nouvelle reference 
    * pour l'echantillon qui demande la modification.
    * @param echantillon Objet à mettre à jour dans la base.
    * @param banque Banque associée à l'échantillon (obligatoire).
    * @param prelevement Prlvt associé (obligatoire).
    * @param collaborateur Collaborateur associé.
    * @param statut Statut de l'échantillon.
    * @param emplacement Emplacement de stockage de l'échantillon.
    * @param type Type de l'échantillon (obligatoire).
    * @param liste de codes assignes.
    * @param liste de codes assignes a supprimer.
    * @param quantite Unité de la quantité.
    * @param qualite Qualité de l'échantillon.
    * @param preparation Mode de préparation de l'échantillon.
    * @param anapath CrAnapath de l'échantillon.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param stream contenu du CrAnapath
    * @param liste des valeurs d'annotation à enregistrer
    * @param liste des valeurs d'annotation à supprimer. 
    * @param utilisateur Utilisateur ayant modifié l'échantillon.
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   public void updateObjectWithCrAnapathManager(Echantillon echantillon, Banque banque, Prelevement prelevement,
      Collaborateur collaborateur, ObjetStatut statut, Emplacement emplacement, EchantillonType type, List<CodeAssigne> codes,
      List<CodeAssigne> codesToDelete, Unite quantite, EchanQualite qualite, ModePrepa preparation, Fichier anapath,
      InputStream anapathStream, List<File> filesCreated, List<File> filesToDelete,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, Utilisateur utilisateur,
      boolean doValidation, List<OperationType> operations, String baseDir);

   /**
    * Supprime un objet de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param echantillon Echantillon à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    * @throws ObjectUsedException si echantillon reference des derives
    * ou est référencé par des cessions.
    */
   void removeObjectManager(Echantillon echantillon, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Cette méthode met à jour les associations entre un échantillon et
    * une liste de CodeAssignes (organes ou morpho), et assigne le code
    * qui sera exporté si ce code est en creation.
    * @param echantillon pour lequel on veut mettre à jour les codes.
    * @param codes Liste des CodesAssigne que l'on créer ou modifier.
    * @param code en cours creation qui sera exporté 
    * (cad assignation de l'export lors de la creation de ce code)
    * @param boolean isOrgane si liste de codes organe manipulée
    * @param utilisateur realisant les operations creation/modification
    * @param jdbcSuite contenant les ids et statements permettant 
    * la creation des objets en full JDBC
    * @return une liste de codes revenant de la base de données.
    * @version 2.0.10.6
    */
   List<CodeAssigne> createOrUpdateCodesAssignesManager(Echantillon echantillon, List<CodeAssigne> codes,
      //CodeAssigne codeToExport,
      boolean isOrgane, Utilisateur usr, EchantillonJdbcSuite ijdbcSuite) throws SQLException;

   /**
    * Ecris le path pour le cr anapath.
    * @param base Directory du sytème de fichier
    * @param bank Banque
    * @param file Fichier 
    * @return path
    */
   String writeCrAnapathFilePath(String baseDir, Banque bank, Fichier file);

   /**
    * Supprime un objet de la base de données et en cascade tous les objets 
    * dont il est le parent, deletion cascadant à leur tour sur les objets 
    * en descendant la hierarchie. 
    * @param echantillon Echantillon à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    */
   void removeObjectCascadeManager(Echantillon echantillon, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Recupere les echantillons pour un prelevement passé en paramètre.
    * @param prel
    * @return liste d'échantillons
    */
   List<Echantillon> findByPrelevementManager(Prelevement prel);

   /**
    * Change l'echantillon de collection vers celle 
    * passée en paramètre. Une erreur est lancée si l'échantillon 
    * est déplacé vers une banque appartenant à une autre plateforme.
    * @param echan à migrer
    * @param bank d'arrivée
    * @param doValidation si true
    * @param u utilisateur enregistrant l'opération.
    * @param liste des fichiers à supprimer après transaction. La suppression 
    * sera réalisée dans la méthode parente.
    * @param liste de déplacements (uniques) de fichiers à programmer [Correctif bug TK-155]
   * @version 2.2.0
   */
   void switchBanqueCascadeManager(Echantillon echan, Banque bank, 
                        boolean doValidation, Utilisateur u,
                        List<File> filesToDelete, Set<MvFichier> filesToMove);
   /**
    * Réalise la modification multiple d'une liste d'échantillons.
    * @param echantillons Liste des echantillons à mettre à jour, cad clone 
    * des échantillons ayant au moins une valeur modifiée 
    * @param Liste échantillons à la base de la modification multiple, cette liste est 
    * utilisée pour la création des annotations Fichier en batch
    * @param list codes assignes
    * @param list des codes assignes a supprimer
    * @param fichier cr_anapath a enregistrer
    * @param stream contenu du cr_anapath
    * @param boolean deleteAnapath delete tous les fichiers si true
    * @param list Annotations associées à mettre à jour ou créer
    * (doivent donc avoir objet, champ, et banque referencees)
    * @param list Annotations associées à supprimmer
    * @param liste non conformites après traitement
    * @param liste non conformites avant cession
    * @param utilisateur Utilisateur voulant modifier les patients.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void updateMultipleObjectsManager(List<Echantillon> echantillonsToUpdate, List<Echantillon> baseEchantillons,
      List<CodeAssigne> codeOrganes, List<CodeAssigne> codesToDelete, Fichier crAnapath, InputStream anapathStream,
      Boolean deleteAnapath, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete,
      List<NonConformite> ncfsTrait, List<NonConformite> ncfsCess, Utilisateur utilisateur, String baseDir);

   /**
    * Met à jour le préfixe du code de plusieurs échantillons. 
    * Méthode utilisée
    * lors du changement du code d'un prélèvement.
    * Si un doublon est détecté, les autres échantillons sont quand même mis à jour
    * @param echantillons Liste des échantillons à maj.
    * @param oldPrefixe Ancien préfixe.
    * @param newPrefixe Nouveau préfixe.
    * @param utilisateur Utilisateur.
    * @return Map contenant la liste des échantillons mis à jour et celle des échantillons en doublon.
    */
   Map<String, List<Echantillon>> updateCodeEchantillonsManager(List<Echantillon> echantillons, String oldPrefixe, String newPrefixe,
      Utilisateur utilisateur);   
  
   
   /**
    * Modifie l'emplacement de l'échantillon et son statut.
    * @param echantillon Echantillon à modifier.
    * @param statut Statut de l'échantillon.
    * @param emplacement Emplacement.
    * @param utilisateur utilisateur.
    * @param operations Opérations.
    */
   void saveEchantillonEmplacementManager(Echantillon echantillon, ObjetStatut statut, Emplacement emplacement,
      Utilisateur utilisateur, List<OperationType> operations);

   /**
    * Compte le nombre d'échantillons d'un prelevement.
    * @param prlvt
    * @return
    */
   Long findCountByPrelevementManager(Prelevement prlvt);

   /**
    * Compte le nombre d'échantillons d'un prelevement qui ont la
    * qté > 0.
    * @param prlvt
    * @return
    */
   Long findCountRestantsByPrelevementManager(Prelevement prlvt);

   /**
    * Compte le nombre d'échantillons d'un prelevement qui sont
    * stockés ou réservés.
    * @param prlvt
    * @return
    */
   Long findCountByPrelevementAndStockeReserveManager(Prelevement prlvt);

   /**
    * Compte le nombre d'échantillons dont l'opérateur est le 
    * collaborateur passé en param
    * @param collaborateur
    * @return compte Long
    */
   Long findCountByOperateurManager(Collaborateur colla);

   /**
    * Compte le nombre d'échantillons créés par un collaborateur passé en param
    * @param collaborateur
    * @return
    */
   Long findCountByCollaborateur(Collaborateur colla);

   /**
    * Compte le nombre d'échantillons créés par un collaborateur passé en param
    * @param collaborateur
    * @return
    */
   Long findCountCreatedByCollaborateurManager(Collaborateur colla);

   /**
    * Surcharge du manager updateObject pour lui ajouter les non 
    * conformites.
    * @param echantillon
    * @param banque
    * @param prelevement
    * @param collaborateur
    * @param statut
    * @param emplacement
    * @param type
    * @param codes
    * @param codesToDelete
    * @param quantite
    * @param qualite
    * @param preparation
    * @param anapath
    * @param anapathStream
    * @param listAnnoToCreateOrUpdate
    * @param listAnnoToDelete
    * @param utilisateur
    * @param doValidation
    * @param operations
    * @param baseDir
    * @param noconfsTraitement
    * @param noconfsCession
    */
   void updateObjectWithNonConformitesManager(Echantillon echantillon, Banque banque, Prelevement prelevement,
      Collaborateur collaborateur, ObjetStatut statut, Emplacement emplacement, EchantillonType type, List<CodeAssigne> codes,
      List<CodeAssigne> codesToDelete, Unite quantite, EchanQualite qualite, ModePrepa preparation, Fichier anapath,
      InputStream anapathStream, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      List<AnnotationValeur> listAnnoToDelete, Utilisateur utilisateur, boolean doValidation, List<OperationType> operations,
      String baseDir, List<NonConformite> noconfsTraitement, List<NonConformite> noconfsCession);

   /**
    * Surcharge de la methode de creation d'un échantillon intégrant la 
    * relation avec les non-conformites.
    * @param echantillon
    * @param banque
    * @param prelevement
    * @param collaborateur
    * @param statut
    * @param emplacement
    * @param type
    * @param codes
    * @param quantite
    * @param qualite
    * @param preparation
    * @param anapath
    * @param anapathStream
    * @param listAnnoToCreateOrUpdate
    * @param utilisateur
    * @param doValidation
    * @param baseDir
    * @param isImport
    * @param noconfsTrait
    * @param noconfsCess
    * @since 2.0.10
    */
   void createObjectWithNonConformitesManager(Echantillon echantillon, Banque banque, Prelevement prelevement,
      Collaborateur collaborateur, ObjetStatut statut, Emplacement emplacement, EchantillonType type, List<CodeAssigne> codes,
      Unite quantite, EchanQualite qualite, ModePrepa preparation, Fichier anapath, InputStream anapathStream, 
      List<AnnotationValeur> listAnnoToCreateOrUpdate, Utilisateur utilisateur, boolean doValidation, String baseDir,
      boolean isImport, List<NonConformite> noconfsTrait, List<NonConformite> noconfsCess);

   /**
    * Préparations batch statements pour full JDBC d'un échantillon, des valeurs annotations, codes 
    * organes/diagnostics et non conformités associées.
    * Les validations sont identiques à celles appliquées par la création en 
    * JPA.
    * Méthode développée suite à problèmatique d'import > 500 échantillons (projet 
    * ESTEBAN CHU Tours). Le temps moyen de l'enregistrement d'un échantillon est 
    * divisé par 4 (200 à 50 msec).
    * @param jdbcSuite contenant les ids et statements permettant 
    * la creation des objets en full JDBC
    * @param echantillon
    * @param banque
    * @param prelevement
    * @param collaborateur
    * @param statut
    * @param emplacement
    * @param type
    * @param quantite
    * @param qualite
    * @param preparation
    * @param codes
    * @param listAnnoToCreateOrUpdate
    * @param noconfsTrait
    * @param noconfsCess
    * @param utilisateur
    * @param doValidation
    * @param isImport
    * @param requiredChampEntiteIds gatsbi champ entite required
    * @return echantillon_id assigne au nouvel enregistrement
    * @version 2.3.0-gatsbi
    */
   Integer prepareObjectJDBCManager(EchantillonJdbcSuite jdbcSuite, Echantillon echantillon, Banque banque,
      Prelevement prelevement, Collaborateur collaborateur, ObjetStatut statut, Emplacement emplacement, EchantillonType type,
      Unite quantite, EchanQualite qualite, ModePrepa preparation, List<CodeAssigne> codes,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<NonConformite> noconfsTrait, List<NonConformite> noconfsCess,
      Utilisateur utilisateur, boolean doValidation, boolean isImport, List<Integer> requiredChampEntiteIds) throws SQLException;

   /**
    * Supprime les échantillons et en cascade les dérivés dont 
    * ils sont parents à partir des ids passés en paramètres.
    * @param ids
    * @param String fantome commentaire
    * @param u Utilisateur (opération suppr)
    * @since 2.0.12
    */
   void removeListFromIdsManager(List<Integer> ids, String comment, Utilisateur u);

   /**
    * Renvoie tous les échantillons pour un code spécifié dans toutes 
    * les collections d'une plateforme passée en paramètre.
    * @param code
    * @param pf Plateforme
    * @return liste échantillons
    * @since 2.1
    */
   List<Echantillon> findByCodeInPlateformeManager(String code, Plateforme pf);

   void updateEchantillon(Echantillon echantillon);

   List<Echantillon> findByCodeInListWithPlateforme(List<String> codes, Plateforme pf);

   /**
    * Calcule le délai de stockage.
    * Actuellement défini par la différence entre la date de prélèvement et
    * la date de stockage de l' echantillon.
    * (anciennement délai de congélation, prenait en compte la date de 
    * congélation dans un site intermédiaire.
    * Le délai est persisté et affiché en minutes donc ces dates doivent contenir 
    * obligatoirement des heures/minutes pour etre prise en compte dans le calcul.
    * @param echan
    * @param prel
    * @return delai en millisecondes
    * @since 2.2.2
    */
   long calculDelaiStockage(Echantillon echan, Prelevement prel);

   /**
    * Renvoie tous les échantillons ayant eu une dégradation possible
    * @param banks
    * @param impact
    * @return liste d'ids
    * @since 2.3
    */
   List<Integer> findByBanksAndImpact(List<Banque> banks, List<Boolean> impact);   
   
   /**
    * Factorisation check avant create update.
    * 
    * @since 2.3.0-gatsbi
    */
   void checkRequiredObjectsAndValidate(Echantillon echantillon, Banque banque, EchantillonType type, ObjetStatut statut,
      String operation, Utilisateur utilisateur, List<CodeAssigne> codes, boolean doValidation, boolean isImport);

   /**
    * Recherche tous les échantillons ids issus des patients dont les valeurs passées 
    * en paramètres correspondent à un identifiant, un nip, ou un nom.
    * @param idsNipsNoms Liste des identifiants noms ou nips de patients.
    * @param banks Liste des banques des échantillons.
    * @return Liste d'ids échantillons.
    * @since 2.3.0-gatsbi
    */
   List<Integer> findByPatientIdentifiantOrNomOrNipInListManager(List<String> idsNipsNoms, List<Banque> selectedBanques);

   /**
    * Recherche tous les échantillons ids issus des patients dont l'identifiant, nip ou nom 
    * correspond au critère passé en paramètre 
    * @param search critere
    * @param banks Liste des banques des échantillons.
    * @return Liste d'ids échantillons.
    * @since 2.3.0-gatsbi
    */
   List<Integer> findByPatientIdentifiantOrNomOrNipReturnIdsManager(String search, List<Banque> selectedBanques, boolean b);

   /**
    * Met à jour le délai de congélation des échantillons qui appartient à un prélèvement.
    *
    * @param prelevement Le prélèvement
    */
   void updateDelaiCongelation(Prelevement prelevement);

   /**
    * Vérifie si un Prélèvement contient des échantillons avec des délais de congélation saisis manuellement
    * qui ne sont pas calculés.
    *
    * @param prelevement Le Prélèvement à vérifier.
    * @return  false: si tous les échantillons du Prélèvement ont un délai de congélation calculé,
    *          true: dès qu'un échantillon avec un délai de congélation saisi manuellement est trouvé.
    */
    boolean hasEchantillonWithNonCalculatedDelai(Prelevement prelevement);

}
