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
package fr.aphp.tumorotek.manager.coeur.prodderive;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine ProdDerive.
 * Interface créée le 02/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.2.0
 *
 */
public interface ProdDeriveManager
{

   /**
    * Recherche un ProdDerive dont l'identifiant est passé en paramètre.
    * @param prodDeriveId Identifiant du ProdDerive que l'on recherche.
    * @return Un ProdDerive.
    */
   ProdDerive findByIdManager(Integer prodDeriveId);

   /**
    * Recherche tous les Produits Derives présents dans la base.
    * @return Liste de ProdDerives.
    */
   List<ProdDerive> findAllObjectsManager();

   /**
    * Recherche des dérivés en fonction d'une liste de banques.
    * @param banks Liste des banques.
    * @return Une liste de produits dérivés.
    */
   List<ProdDerive> findByBanquesManager(List<Banque> banks);

   /**
    * Recherche tous les ids des ProdDerives des banque.
    * @param banques liste de Banques pour lesquelles on recherche.
    * @return Liste de ProdDerives.
    */
   List<Integer> findAllObjectsIdsByBanquesManager(List<Banque> banques);

   /**
    * Recherche une liste de produits dérivés dont la date de stockage
    * est plus récente que celle passée en paramètre.
    * @param date Date de stockage pour laquelle on recherche des produits
    * dérivés.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByDateStockAfterDateManager(Date date);

   /**
    * Recherche une liste de produits dérivés dont la date de transformation
    * est plus récente que celle passée en paramètre.
    * @param date Date de transformation pour laquelle on recherche des
    * produits dérivés.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByDateTransfoAfterDateManager(Date date);

   /**
    * Recherche une liste de produits dérivés dont la date de transformation
    * est plus récente que celle passée en paramètre.
    * @param date Date de transformation pour laquelle on recherche des
    * produits dérivés.
    * @param banques liste Banque auxquelles appartient le derivé.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByDateTransformationAfterDateWithBanqueManager(Date date, List<Banque> banques);

   /**
    * Recherche une liste de produits dérivés dont le code commence comme
    * celui passé en paramètre.
    * @param code Code pour lequel on recherche des produits dérivés.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByCodeLikeManager(String code, boolean exactMatch);

   /**
    * Recherche une liste de produits dérivés dont le code ou le code labo
    * commence comme celui passé en paramètre.
    * @param code Code pour lequel on recherche des produits dérivés.
    * @param banque Banque à laquelle appartient le dérivé.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByCodeOrLaboWithBanqueManager(String code, Banque banque, boolean exactMatch);

   /**
    * Recherche une liste de produits dérivés dont le code ou le code labo
    * contient celui passé en paramètre.
    * @param code Code pour lequel on recherche des produits dérivés.
    * @param banques liste Banque auxqueles appartient le dérivé.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdDerive.
    */
   List<Integer> findByCodeOrLaboBothSideWithBanqueReturnIdsManager(String code, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche la liste des codes utilisés par les produits dérivés liés à 
    * la banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueManager(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les produits dérivés liés à 
    * la banque passée en paramètre et dont la quantité n'est pas égale à 0.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueAndQuantiteManager(Banque banque);
   
   /**
    * Recherche la liste des codes utilisés par les dérivés pour associer à un produit dérivé :
    * Dérivés liés à la banque passée en paramètre et dont la quantité n'est pas égale à 0, ou dans une cession de type traotement
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForDerivesByBanque(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les dérivés liés à la
    * banque passée en paramètre et qui sont stockés.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueAndStockesManager(Banque banque);

   /**
    * Recherche la liste des codes utilisés par les dérivés liés à la
    * banque passée en paramètre et qui sont stockés.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForMultiBanquesAndStockesManager(List<Banque> banques);

   /**
    * Recherche une liste de produits dérivés dont le parent et
    * le type sont passés en paramètre.
    * @param parent Parent du produit dérivé.
    * @param type Type des dérivés recherchés.
    * @return List de ProdDerives.
    */
   List<ProdDerive> findByParentAndTypeManager(Object parent, String type);

   /**
    * Recherche les produits dérivés issus d'un patient.
    * @param nom Nom du patient.
    * @param boolean exactMatch
    * @return Liste de produits dérivés.
    * @version 2.0.10
    */
   List<Integer> findByPatientNomReturnIdsManager(String nom, List<Banque> banks, boolean exactMatch);

   /**
    * Recherche une liste de produits dérivés dont le prodDerive est 
    * passé en paramètre.
    * @param prodDerive ProdDerive pour lequel on recherche des
    * produits dérivés.
    * @return List de ProdDerives.
    */
   List<ProdDerive> getProdDerivesManager(ProdDerive prodDerive);

   /**
    * Recherche un emplacement dont le dérivé est 
    * passé en paramètre.
    * @param prodDerive ProdDerive pour lequel on recherche un
    * emplacement.
    * @return Emplacement du dérivé.
    */
   Emplacement getEmplacementManager(ProdDerive prodDerive);

   /**
    * Recherche d'adresse de l'emplacement dont l'échantillon est 
    * passé en paramètre.
    * @param prodDerive ProdDerive pour lequel on recherche 
    * l'adresse de son emplacement.
    * @return Adresse de l'emplacement du dérivé.
    */
   String getEmplacementAdrlManager(ProdDerive prodDerive);

   /**
    * Recherche les doublons (= tous les dérivés appartenant à la même 
    * plateforme et partageant le même code) du ProdDerive passé en paramètre.
    * @param prodDerive Un ProdDerive pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    * @version 2.1
    */
   Boolean findDoublonManager(ProdDerive prodDerive);

   /**
    * Recherche tous les dérivés dont la date de creation systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banques liste Banque auxquelles appartient le dérivé.
    * @return Liste de ProdDerive.
    */
   List<Integer> findAfterDateCreationReturnIdsManager(Calendar date, List<Banque> banques);

   /**
    * Recherche tous les dérivés dont la date de modification systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque Banque à laquelle appartient le dérivé.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findAfterDateModificationManager(Calendar date, Banque banque);

   /**
    * Recupère l'objet parent d'un produit dérivé.
    * @param prodDerive ProdDerive dont on souhaite obtenir le parent.
    * @return L'objet parent du produit dérivé.
    */
   Object findParent(ProdDerive prodDerive);

   /**
    * Recherche les derniers produits dérivés créés dans le systeme.
    * @param banques Banques auxquelles appartiennent les dérivés.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findLastCreationManager(List<Banque> banques, int nbResults);

   /**
    * Recherche tous les dérivés dont la transformation est passee en 
    * parametre.
    * @param transformation
    * @return Liste de ProdDerive.
    */
   List<ProdDerive> findByTransformationManager(Transformation transfo);

   /**
    * récupère le prélèvement dont est issu le dérivé.
    * @param prodDerive Dérivé pour lequel on recherche le prlvt.
    * @return Prelevement parent du dérivé.
    */
   Prelevement getPrelevementParent(ProdDerive prodDerive);

   /**
    * Récupère le patient dont est issu le dérivé.
    * @param prodDerive Dérivé pour lequel on recherche le prlvt.
    * @return Patient parent du dérivé.
    */
   Patient getPatientParentManager(ProdDerive prodDerive);

   /**
    *    Persist une instance de ProdDerive dans la base de données.
    * @param prodDerive Nouvelle instance de l'objet à créer.
    * @param banque Banque assoiée (obligatoire).
    * @param type Type du produit dérivé (obligatoire).
    * @param statut Statut du produit dérivé.
    * @param collab Collaborateur du produit dérivé.
    * @param emplacement Emplacement du produit dérivé.
    * @param volumeUnite Unité de volume du produit dérivé.
    * @param concUnite Unité de concentration du produit dérivé.
    * @param quantiteUnite Unité de quantité du produit dérivé.
    * @param qualite Qualité du produit dérivé.
    * @param transfo Transformation dont est issu le produit dérivé.
    * @param liste des valeurs d'annotation à enregistrer
    * @param filesCreated liste de fichier créés
    * @param utilisateur Utilisateur ayant créé le prod dérivé.
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void createObjectManager(ProdDerive prodDerive, Banque banque, ProdType type, ObjetStatut statut, Collaborateur collab,
      Emplacement emplacement, Unite volumeUnite, Unite concUnite, Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
      ProdQualite qualite, Transformation transfo, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<File> filesCreated,
      Utilisateur utilisateur, boolean doValidation, String baseDir, boolean isImport);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param prodDerive Nouvelle instance de l'objet à créer.
    * @param banque Banque assoiée (obligatoire).
    * @param type Type du produit dérivé (obligatoire).
    * @param statut Statut du produit dérivé.
    * @param collab Collaborateur du produit dérivé.
    * @param emplacement Emplacement du produit dérivé.
    * @param volumeUnite Unité de volume du produit dérivé.
    * @param concUnite Unité de concentration du produit dérivé.
    * @param quantiteUnite Unité de quantité du produit dérivé.
    * @param qualite Qualité du produit dérivé.
    * @param transfo Transformation dont est issu le produit dérivé.
    * @param liste des valeurs d'annotation à enregistrer
    * @param liste des valeurs d'annotation à supprimer.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param utilisateur Utilisateur ayant modifié le prod dérivé.
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void updateObjectManager(ProdDerive prodDerive, Banque banque, ProdType type, ObjetStatut statut, Collaborateur collab,
      Emplacement emplacement, Unite volumeUnite, Unite concUnite, Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
      ProdQualite qualite, Transformation transfo, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      List<AnnotationValeur> listAnnoToDelete, List<File> filesCreated, List<File> filesToDelete, 
      Utilisateur utilisateur, boolean doValidation, List<OperationType> operations, String baseDir);

   /**
    * Cette méthode met à jour une liste de produits dérivés.
    * @param prodDerives Liste des dérivés à mettre à jour.
    * @param Liste dérivés à la base de la modification multiple, cette liste est 
    * utilisée pour la création des annotations Fichier en batch
    * @param list Annotations associées à mettre à jour ou créer
    * (doivent donc avoir objet, champ, et banque referencees)
    * @param list Annotations associées à supprimmer
    * @param liste non conformites après traitement
    * @param liste non conformites avant cession
    * @param utilisateur Utilisateur voulant modifier les dérivés.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void updateMultipleObjectsManager(List<ProdDerive> prodDerives, List<ProdDerive> baseProdDerives,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, List<NonConformite> ncfsTrait,
      List<NonConformite> ncfsCess, Utilisateur utilisateur, String baseDir);

   /**
    * Supprime un ProdDerive de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param prodDerive ProdDerive à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    * @throws ObjectUsedException si derive reference des derives
    * ou est référencé par des cessions.
    */
   void removeObjectManager(ProdDerive prodDerive, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Supprime un objet de la base de données et en cascade tous les objets 
    * dont il est le parent, deletion cascadant à leur tour sur les objets 
    * en descendant la hierarchie. 
    * @param derive ProdDerive à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    */
   void removeObjectCascadeManager(ProdDerive derive, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Cree une liste de derives avec une liste d'annotations dont les 
    * valeurs sont identiques pour chacun.
    * @param listDerives
    * @param banque
    * @param transfo
    * @param utilisateur
    * @param listAnnotations
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    * @param noconfsTrait
    * @param noconfsCess
    * @since 2.0.12
    */
   void createDeriveListWithAnnotsManager(List<ProdDerive> listDerives, Banque banque, Transformation transfo,
      Utilisateur utilisateur, List<AnnotationValeur> listAnnotations, String baseDir, List<NonConformite> noconfsTrait,
      List<NonConformite> noconfsCess);

   /**
    * Test si un dérivé est lié à d'autres produits dérivés.
    * @param derive ProdDerive que l'on souhaite tester.
    * @return Vrai si le derive référence des dérivés.
    */
   Boolean isUsedObjectManager(ProdDerive derive);

   /**
   * Test si le derivé est associé à une ou plusieurs cessions.
   * @param derive ProdDerive que l'on souhaite tester.
   * @return Vrai le dérivé a été cédé une fois au moins.
   */
   Boolean isCessedObjectManager(ProdDerive derive);

   /**
   * Recherche les dérivés dont l'id se trouve dans la liste.
   * @param ids Liste d'identifiants.
   * @return Liste de dérivés.
   */
   List<ProdDerive> findByIdsInListManager(List<Integer> ids);

   /**
    * Recherche les produits dérivés dont l'id est dans la liste et le statut est statusId.
    * @param ids Liste d'identifiants.
    * @param statusId statusId (table obect_statut).
    * @return Liste de produits dérivés.
    */
   List<ProdDerive> findByIdsAndStatus(List<Integer> ids, Integer statusId);
   
   /**
    * Trouve les derives pour l'objet parent passé en paramètres.
    * Peut chercher ou non recursivement tous les derives isssus des 
    * derives du parent passé en paramètre.
    * @param parent TKAnnotableObject
    * @param true si recursive
    * @return liste de derives
    */
   List<ProdDerive> findByParentManager(TKAnnotableObject parent, boolean recursive);

   /**
    * Peuple la liste passée en deuxième paramètre avec toutes 
    * les derives descendants de ceux passés dans la liste 
    * en premier paramètre.
    * @param drvs 
    * @param coll
    */
   void findRecursiveDerivesManager(List<ProdDerive> drvs, List<ProdDerive> coll);

   /**
    * Change le dérivé de collection vers celle 
    * passée en paramètre.  Une erreur est lancée si le dérivé 
    * est déplacé vers une banque appartenant à une autre plateforme.
    * @param derive à migrer
    * @param bank d'arrivée
    * @param doValidation si true
    * @param u utilisateur enregistrant l'opération.
    * @param liste des fichiers à supprimer après transaction
    * @param liste de déplacements (uniques) de fichiers à programmer [Correctif bug TK-155]
    * @version 2.2.0
   */
   void switchBanqueCascadeManager(ProdDerive derive, Banque bank,
                        boolean doValidation, Utilisateur u, 
                        List<File>filesToDelete, Set<MvFichier> filesToMove);

   /**
    * Supprime une Transformation de la base de données.
    * @param transformation Transformation à supprimer de la base 
    * de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    * @throws DoublonFoundException Lance une exception si l'objet
    * est utilisé par des échantillons.
    */
   void removeObjectCascadeManager(Transformation transformation, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Recherche une liste de produits dérivés dont le code se trouve
    * dans la liste passée en paramètre.
    * @param criteres Codes pour lesquels on recherche des produits dérivés.
    * @param banques liste Banque auxqueles appartient le dérivé.
    * @param notfounds liste codes non trouvés
    * @return Liste d'ids de ProdDerive.
    * @version 2.1
    */
   List<Integer> findByCodeInListManager(List<String> criteres, List<Banque> banques, List<String> notfounds);

   /**
    * Recherche les produits dérivés issus des patients.
    * @param criteres Noms ou NIP des patients.
    * @param banques liste Banque auxqueles appartient le dérivé.
    * @return Liste d'ids de produits dérivés.
    */
   List<Integer> findByPatientNomOrNipInListManager(List<String> criteres, List<Banque> banks);

   /**
    * Surcharge du manager updateObject pour lui ajouter les non 
    * conformites.
    * @param prodDerive
    * @param banque
    * @param type
    * @param statut
    * @param collab
    * @param emplacement
    * @param volumeUnite
    * @param concUnite
    * @param quantiteUnite
    * @param modePrepaDerive
    * @param qualite
    * @param transfo
    * @param listAnnoToCreateOrUpdate
    * @param listAnnoToDelete
    * @param utilisateur
    * @param doValidation
    * @param operations
    * @param baseDir
    * @param noconfsTraitement
    * @param noconfsCession
    */
   void updateObjectWithNonConformitesManager(ProdDerive prodDerive, Banque banque, ProdType type, ObjetStatut statut,
      Collaborateur collab, Emplacement emplacement, Unite volumeUnite, Unite concUnite, Unite quantiteUnite,
      ModePrepaDerive modePrepaDerive, ProdQualite qualite, Transformation transfo,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, 
      Utilisateur utilisateur, boolean doValidation, List<OperationType> operations, String baseDir,
      List<NonConformite> noconfsTraitement, List<NonConformite> noconfsCession);

   /**
    * Surcharge de la methode de creation d'un échantillon intégrant la 
    * relation avec les non-conformites.
    * @param prodDerive
    * @param banque
    * @param type
    * @param statut
    * @param collab
    * @param emplacement
    * @param volumeUnite
    * @param concUnite
    * @param quantiteUnite
    * @param modePrepaDerive
    * @param qualite
    * @param transfo
    * @param listAnnoToCreateOrUpdate
    * @param utilisateur
    * @param doValidation
    * @param baseDir
    * @param isImport
    * @param noconfsTrait
    * @param noconfsCess
    * @since 2.0.10
    */
   void createObjectWithNonConformitesManager(ProdDerive prodDerive, Banque banque, ProdType type, ObjetStatut statut,
      Collaborateur collab, Emplacement emplacement, Unite volumeUnite, Unite concUnite, Unite quantiteUnite,
      ModePrepaDerive modePrepaDerive, ProdQualite qualite, Transformation transfo,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, Utilisateur utilisateur, boolean doValidation,
      String baseDir, boolean isImport, List<NonConformite> noconfsTrait, List<NonConformite> noconfsCess);

   /**
    * Compte le nombre de produit dérivés créés par un collaborateur passé en param
    * @param collaborateur
    * @return
    */
   public Long findCountCreatedByCollaborateurManager(Collaborateur colla);

   /**
    * Compte le nombre de produit dérivés dont l'operateur 
    * est le collaborateur passé en param
    * @param collaborateur
    * @return
    */
   public Long findCountByOperateurManager(Collaborateur colla);

   /**
    * Céation lots de dérivés sous la forme d'une transformation, en une 
    * seule transaction. Appellée lors de l'import.
    * @param derives
    * @param b
    * @param u
    * @param parent
    * @param valeurs
    * @param empls
    * @param qteMax
    * @param uniteTransfo
    * @param dateS
    * @param retourObservations
    * @param baseDir
    * @param isImport
    * @param noconfsTrait
    * @param noconfsCess
    * @since 2.0.12
    */
   void createProdDerivesManager(List<ProdDerive> derives, Banque b, Utilisateur u, TKAnnotableObject parent,
      Hashtable<ProdDerive, List<AnnotationValeur>> valeurs, Hashtable<ProdDerive, Emplacement> empls, Float qteMax,
      Unite uniteTransfo, Calendar dateS, String retourObservations, String baseDir, boolean isImport,
      Hashtable<ProdDerive, List<NonConformite>> noconfsTrait, Hashtable<ProdDerive, List<NonConformite>> noconfsCess);

   /**
    * Supprime les dérivés et en cascade les dérivés dont 
    * ils sont parents à partir des ids passés en paramètres.
    * @param ids
    * @param String fantome commentaire
    * @param u Utilisateur (opération suppr)
    * @param liste des fichiers à supprimer après transaction
    * @since 2.0.12
    */
   void removeListFromIdsManager(List<Integer> ids, String comment, Utilisateur u);

   /**
    * Renvoie tous les dérivés pour un code spécifié dans toutes 
    * les collections d'une plateforme passée en paramètre.
    * @param code
    * @param pf Plateforme
    * @return liste dérivés
    * @since 2.1
    */
   List<ProdDerive> findByCodeInPlateformeManager(String code, Plateforme pf);
   
   /**
    * Recherche sur une plateforme les dérivés dont le code est contenu dans la liste passée en paramètre
    * @param listCodes liste des codes recherchés
    * @param pf plateforme sur laquelle la recherche est effectuée
    * @since 2.2.0 
    */
   List<ProdDerive> findByListCodeWithPlateforme(List<String> listCodes, Plateforme pf);

   /**
    * Renvoie tous les dérivés ayant eu une dégradation possible
    * @param banks
    * @param impact
    * @return liste d'ids
    * @since 2.3
    */
   List<Integer> findByBanksAndImpact(List<Banque> banks, List<Boolean> impact);
   
   /**
    * Mets à jour le préfixe du code de plusieurs produits dérivés.
    * Méthode utilisée lors du changement du code d'un prélèvement/échantillon/derive.
    * @param produitsDerives Liste des produitsDerives à mettre à jour.
    * @param oldPrefixe Ancien préfixe.
    * @param newPrefixe Nouveau préfixe.
    * @param utilisateur Utilisateur.
    * @return une map des produitsDerives mis à jour et des produitsDerives non mis à jour à cause d'un doublon.
    */
    Map<String,List<ProdDerive>> updateCodeDerivesManager(final List<ProdDerive> produitsDerives, final String oldPrefixe,
      final String newPrefixe, final Utilisateur utilisateur);
    
    /**
     * Cette méthode récupère les produits dérivés associés au statut spécifié à partir de la liste donnée d'objets CederObjet.
     * Le résultat est renvoyé sous forme d'une liste de produits dérivés.
     * @param cederObjets La liste d'objets CederObjet à utiliser dans la recherche.
     * @param statusId La valeur représentant l'identifiant du statut pour filtrer les produits dérivés.
     * @return Une liste de produits dérivés ayant le statut spécifié. Si aucun produit dérivé correspondant n'est trouvé,
     * une liste vide est renvoyée.
     */
    List<ProdDerive> findProdDerivesWithStatusFromCederObject(List<CederObjet> cederObjets, Integer statusId);   
   
}