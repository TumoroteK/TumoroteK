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
package fr.aphp.tumorotek.manager.coeur.prelevement;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.manager.impl.systeme.MvFichier;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Prelevement.<br>
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public interface PrelevementManager
{

   /**
    * Persiste une instance afin de l'enregistrer dans la base de données.
    * @param prelevement Prelevement a creer
    * @param banque Banque associee (non null)
    * @param nature Nature associee (non null)
    * @param maladie Maladie associee
    * @param consentType ConsentType associe (non null)
    * @param preleveur Collaborateur preleveur associe
    * @param servicePreleveur Service preleveur associe
    * @param prelevementType PrelevementType associe
    * @param conditType ConditType associe
    * @param conditMilieu ConditMilieu associe
    * @param transporteur Transporteur associe
    * @param operateur Collaborateur operateur associe
    * @param quantiteUnite Unite quantite associee
    * @param List liste des laboInters
    * @param liste des valeurs d'annotation à enregistrer
    * @param filesCreated liste de fichier créés
    * @param utilisateur Utilisateur realisant la creation
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    */
   void createObjectManager(Prelevement prelevement, Banque banque, Nature nature, Maladie maladie, ConsentType consentType,
      Collaborateur preleveur, Service servicePreleveur, PrelevementType prelevementType, ConditType conditType,
      ConditMilieu conditMilieu, Transporteur transporteur, Collaborateur operateur, Unite quantiteUnite,
      List<LaboInter> laboInters, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<File> filesCreated,
      Utilisateur utilisateur, boolean doValidation, String baseDir, boolean isImport);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param prelevement Prelevement a modifier
    * @param banque Banque associee (non null)
    * @param nature Nature associee (non null)
    * @param maladie Maladie associee
    * @param consentType ConsentType associe (non null)
    * @param preleveur Collaborateur preleveur associe
    * @param servicePreleveur Service preleveur associe
    * @param prelevementType PrelevementType associe
    * @param conditType ConditType associe
    * @param conditMilieu ConditMilieu associe
    * @param transporteur Transporteur associe
    * @param operateur Collaborateur operateur associe
    * @param quantiteUnite Unite quantite associee
    * @param List liste des laboInters
    * @param liste des valeurs d'annotation à enregistrer
    * @param liste des valeurs d'annotation à supprimer.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param utilisateur Utilisateur realisant la modification
    * @param cascadeNonSterile indique a partir de quel LaboInter (ordre)
    * inclu on applique non-sterile en cascade
    * @param doValidation True : la validation sera faite.
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    * @param boolean multiple si modification multiple
    */
   void updateObjectManager(Prelevement prelevement, Banque banque, Nature nature, Maladie maladie, ConsentType consentType,
      Collaborateur preleveur, Service servicePreleveur, PrelevementType prelevementType, ConditType conditType,
      ConditMilieu conditMilieu, Transporteur transporteur, Collaborateur operateur, Unite quantiteUnite,
      List<LaboInter> laboInters, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete,
      List<File> filesCreated, List<File> filesToDelete, Utilisateur utilisateur, Integer cascadeNonSterile, boolean doValidation,
      String baseDir, boolean multiple);

   /**
    * Cherche les doublons en se basant sur le code du prélèvement
    * et l'appartenance à la plateforme de la banque à laquelle le prélèvement
    * doit être attribué.
    * @param prelevement Prelevement dont on cherche la presence dans la base
    * @return true/false
    * @version 2.1
    */
   boolean findDoublonManager(Prelevement prelevement);

   /**
    * Verifie avant la suppression que d'autres objets ne referencent
    * pas cet objet.
    * @param prelevement Prelevement a supprimer de la base de donnees.
    * @return true/false
    */
   boolean isUsedObjectManager(Prelevement prelevement);

   /**
    * Supprime un objet de la base de données.
    * La suppression engendre la creation d'un fantome de cet objet.
    * @param prelevement Prelevement à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param liste des fichiers à supprimer après transaction
    */
   void removeObjectManager(Prelevement prelevement, String comments, Utilisateur u, List<File> filesToDelete);

   /**
    * Supprime un objet de la base de données et en cascade tous les objets
    * dont il est le parent, deletion cascadant à leur tour sur les objets
    * en descendant la hierarchie.
    * @param prelevement Prelevement à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression
    * @param liste des fichiers à supprimer après transaction
    */
   void removeObjectCascadeManager(Prelevement prelevement, String comments, Utilisateur user, List<File> filesToDelete);

   /**
    * Recherche toutes les instances de Prelevement présentes dans la base.
    * @return List contenant les Prelevement.
    */
   List<Prelevement> findAllObjectsManager();

   /**
    * Recherche tous les prelevements dont le code est egal
    * ou 'like' celui en parametre.
    * @param code
    * @param boolean exactMatch
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByCodeLikeManager(String code, boolean exactMatch);

   /**
    * Recherche tous les prelevements dont le code est egal
    * ou 'like' celui en parametre.
    * @param code
    * @param banque Banque à laquelle appartient le prélèvement.
    * @param boolean exactMatch
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByCodeOrNumLaboLikeWithBanqueManager(String code, Banque banque, boolean exactMatch);

   /**
    * Recherche tous les prelevements Ids dont le code est egal
    * ou 'like' celui en parametre.
    * @param code
    * @param banques Banques auxquelles appartiennent le prélèvement.
    * @param boolean exactMatch
    * @return Liste de Prelevement.
    */
   List<Integer> findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(String code, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche tous les prelevements dont la date de prelevement est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @return Liste de Prelevement.
    */
   List<Prelevement> findAfterDatePrelevementManager(Date date);

   /**
    * Compte le nombre de prelevement créés par un collaborateur passé en param
    * @param collaborateur
    * @return
    */
   Long findCountCreatedByCollaborateurManager(Collaborateur colla);

   /**
    * Compte le nombre de prelevement dont le preleveur est le collaborateur
    * passé en param
    * @param collaborateur
    * @return
    */
   Long findCountByPreleveurManager(Collaborateur colla);

   /**
    * Compte le nombre de prelevement dans un service passé en param
    * @param collaborateur
    * @return
    */
   Long findCountByServiceManager(Service serv);

   /**
    * Recherche tous les prelevements dont la date de prelevement est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque liste de Banque auxquelles appartiennent les prélèvements.
    * @return Liste de Prelevement.
    */
   List<Prelevement> findAfterDatePrelevementWithBanqueManager(Date date, List<Banque> banques);

   /**
    * Recherche la maladie dont le prelevement est passé en paramètre.
    * @param prelevement Prelevement pour lequel on recherche une maladie.
    * @return Une Maladie.
    */
   Maladie getMaladieManager(Prelevement prelevement);

   /**
    * Recherche tous les prelevements dont la date de consentement est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @return Liste de Prelevement.
    */
   List<Prelevement> findAfterDateConsentementManager(Date date);

   /**
    * Recherche tous les prelevements dont la date de creation systeme est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banques liste de  Banques auxquelles appartiennent le prlvt.
    * @return Liste de Prelevement.
    */
   List<Integer> findAfterDateCreationReturnIdsManager(Calendar date, List<Banque> banques);

   /**
    * Recherche tous les prelevements dont la date de modification systeme est
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque Banque à laquelle appartient le prlvt.
    * @return Liste de Prelevement.
    */
   List<Prelevement> findAfterDateModificationManager(Calendar date, Banque banque);

   /**
    * Recherche un prélèvement dont l'identifiant est passé en paramètre.
    * @param prelevementId Identifiant du prélèvement que l'on recherche.
    * @return Un Prelevement.
    */
   Prelevement findByIdManager(Integer prelevementId);

   /**
    * Recherche les prlvts dont l'id se trouve dans la liste.
    * @param ids Liste d'identifiants.
    * @return Liste de prlvts.
    */
   List<Prelevement> findByIdsInListManager(List<Integer> ids);

   /**
    * Recherche tous les prelevements dont le type est egal
    * a celui en parametre.
    * @param prelevementType PrelevementType
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByTypeManager(PrelevementType prelevementType);

   /**
    * Recherche tous les prelevements dont la nature est egale
    * a celle en parametre.
    * @param nature Nature
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByNatureManager(Nature nature);

   /**
    * Recherche tous les prelevements dont le type de consentement est egal
    * a celui en parametre.
    * @param consentType ConsentType
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByConsentTypeManager(ConsentType consentType);

   /**
    * Recherche tous les prelevements dont le libelle de la maladie
    * associe est egale ou 'like' celui en parametre.
    * @param String libelle
    * @param boolean exactMatch
    * @return Liste de Prelevement.
    */
   List<Prelevement> findByMaladieLibelleLikeManager(String libelle, boolean exactMatch);

   /**
    * Recherche les prelevements pour le nda passée en paramètre.
    * @param patient nda.
    * @return une liste de prelevements.
    */
   List<Prelevement> findByNdaLikeManager(String nda);

   /**
    * Recherche la liste des codes utilisés par les prelevements liés à
    * la banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les codes.
    * @return Liste de codes.
    */
   List<String> findAllCodesForBanqueManager(Banque banque);

   /**
    * Recherche la liste des ndas utilisés par les prelevements liés à
    * la banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les ndas.
    * @return Liste de ndas.
    */
   List<String> findAllNdasForBanqueManager(Banque banque);

   /**
    * Recherche une liste d'échantillons dont le prélèvement est
    * passé en paramètre.
    * @param prelevement Prelevement pour lequel on recherche des
    * échantillons.
    * @return Set de Echantillon.
    */
   Set<Echantillon> getEchantillonsManager(Prelevement prelevement);

   /**
    * Recherche une liste de labo inters dont le prélèvement est
    * passé en paramètre.
    * @param prelevement Prelevement pour lequel on recherche des
    * labos.
    * @return Set de LaboInter.
    */
   Set<LaboInter> getLaboIntersManager(Prelevement prelevement);

   /**
    * Recherche une liste de labo inters dont le prélèvement est
    * passé en paramètre. Ces labos sont ordonnés par ordre.
    * @param prelevement Prelevement pour lequel on recherche des
    * labos.
    * @return Liste ordonnée de LaboInter.
    */
   List<LaboInter> getLaboIntersWithOrderManager(Prelevement prelevement);

   /**
    * Recherche une liste de dérivés dont le prélèvement est
    * passé en paramètre.
    * @param prelevement Prelevement pour lequel on recherche des
    * dérivés.
    * @return List de ProdDerive.
    */
   List<ProdDerive> getProdDerivesManager(Prelevement prelevement);

   /**
    * Recherche tous les prelevements d'un patient en passant par ses
    * maladies.
    * @param patient
    * @return Liste de Prelevements.
    */
   List<Prelevement> findAllPrelevementsManager(Patient patient);

   /**
    * Recherche les derniers Prelevements créés dans le systeme.
    * @param banques Banque auxquelles appartiennent les Prelevements.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de Prelevements.
    */
   List<Prelevement> findLastCreationManager(List<Banque> banques, int nbResults);

   /**
    * Applique la condition non-stérile en cascade sur les laboInter et les
    * echantillons. N'entraine pas l'enregistrement d'une opération de
    * modification pour les echantillons.
    * @param prelevement
    * @param labos liste des laboInters
    * @param ordreStart ordre (inclu) a partir duquel on cascade
    * @param boolean cascade ou non les echantillons.
    * @return
    */
   List<LaboInter> cascadeNonSterileManager(Prelevement prelevement, List<LaboInter> labos, Integer ordreStart,
      boolean cascadeEchans);

   /**
    * Recherche les prelevements pour une maladie donnée et pour la banque.
    * @param Maladie maladie
    * @param Banque bank
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieAndBanqueManager(Maladie mal, Banque bank);

   /**
    * Recherche les prelevements pour une maladie donnée et pour toutes les
    * banques excluant celle passée en paramètres.
    * @param Maladie maladie
    * @param Banque bank
    * @return une liste de prelevements.
    */
   List<Prelevement> findByMaladieAndOtherBanquesManager(Maladie mal, Banque bank);

   /**
    * Recherche les prélèvements pour une liste de collections spécifiées.
    * @param banks liste de collections
    * @return une liste de prelevements.
    */
   List<Prelevement> findByBanquesManager(List<Banque> banks);

   /**
    * Recherche tous les ids des Prelevements des banques.
    * @param banques liste de Banques pour lesquelles on recherche.
    * @return Liste de Prelevements.
    */
   List<Integer> findAllObjectsIdsByBanquesManager(List<Banque> banques);

   /**
    * Trouve tous les prélèvements issus d'un patient.
    * @param pat
    * @return
    */
   List<Prelevement> findByPatientManager(Patient pat);

   /**
    * Trouve tous les prélèvements issus d'un patient.
    * @param nom Nom du patient.
    * @param exactMatch
    * @return List<Integer> prelevementIds
    * @version 2.0.10
    */
   List<Integer> findByPatientNomReturnIdsManager(String nom, List<Banque> banks, boolean exactMatch);

   /**
    * Recherche tous les prelevements Ids dont le code ou le num
    * labo est présent dans la liste passée en paramètres.
    * @param criteres Liste de cocde ou num labo de prlvts.
    * @param banks Liste des banques des prlvts.
    * @return Liste d'ids prlvts.
    */
   List<Integer> findByCodeOrNumLaboInListManager(List<String> criteres, List<Banque> banques);

   /**
    * Recherche tous les prélèvements ids issus des patients passés
    * en paramètres.
    * @param criteres Liste des noms ou nips de patients.
    * @param banks Liste des banques des prlvts.
    * @return Liste d'ids prlvts.
    */
   List<Integer> findByPatientNomOrNipInListManager(List<String> criteres, List<Banque> banks);

   /**
    * Enregistre un l a création d'un prélèvement avec tous ses objets
    * associés (LaboInter, Annotations) et ses échantillons avec leurs
    * objets associés (Annotations, crAnapath). Implique que les objets
    *  prelevement et échantillons passés en paramètres contiennent toutes
    *  les références vers objets associés.
    * @param prelevement
    * @param annosPrel annotations du prelevement
    * @param echantillons
    * @param annosEchan annotations communes à l'ensemble des echantillons
    * @param banque
    * @param utilisateur
    * @param doValidation
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    */
   void createPrelAndEchansManager(Prelevement prelevement, List<AnnotationValeur> annosPrel, List<Echantillon> echantillons,
      List<AnnotationValeur> annosEchan, Banque banque, Utilisateur utilisateur, boolean doValidation, String baseDir);

   /**
    * Recherche les risques associés au prelevement  passé en paramètre.
    * @param prelevement Prelevement pour lequel on recherche les risques.
    * @return Liste de risques.
    */
   Set<Risque> getRisquesManager(Prelevement prelevement);

   /**
    * Trouve tous les objets dérivant d'un prélèvement, echantillons et
    * dérivés de manière récursives.
    * @param p prelevement
    * @return liste de TKAnnotableObject
    */
   List<TKAnnotableObject> getPrelevementChildrenManager(Prelevement p);

   /**
    * Estime si le prelevement est à l'origine d'au moins un echantillon
    * ou un dérivé qui a fait l'objet d'une cession.
    * Empêche tout changement de collection dans ce cas.
    * @param p
    * @return true si au moins un objet cédé.
    */
   boolean hasCessedObjectManager(Prelevement prel);

   public void switchBanqueMultiplePrelevementManager(Prelevement[] prlvts, Banque bank, boolean doValidation, Utilisateur usr);

   /**
    * Attribue à la banque passée en paramètre le prélèvement ainsi que
    * tous les objets issus de ce prélèvement, si aucun objet ne fait l'objet
    * d'une cession et si conteneurs de stockage sont compatibles entre
    * la banque de départ et celle d'arrivée.
    * @param prel
    * @param banque d'arrivée
    * @param doValidation false si validation non requises.
    * @param utilisateur
    * @param filesToDelete liste de fichier à supprimer. Les suppressions seront
    * réalisées dans la méthode parente switchBanqueMultiple
    * @param liste de déplacements (uniques) de fichiers à programmer. Les déplacements
   * seront réalisées dans la méthode parente switchBanqueMultiple [Correctif bug TK-155]
   * @verison 2.2.0
   */
   void switchBanqueCascadeManager(Prelevement prel, Banque bank, boolean doValidation, Utilisateur usr, List<File> filesToDelete,
      Set<MvFichier> filesToMove);

   /**
    * Attribue à la maladie passée en paramètre le prélèvement.
    * @param prel
    * @param maladie Maladie d'arrivée
    * @param utilisateur
    */
   void switchMaladieManager(Prelevement prel, Maladie maladie, Utilisateur usr);

   /**
    * Pour un prélèvement trouve la date qui a été attribuée à la congélation
    * (date départ, date départ site interm, date arrivé biothèque).
    * @param prel
    * @return Calendar cal
    */
   Calendar getDateCongelationManager(Prelevement prel);

   /**
    * Réalise la modification multiple d'une liste de prélèvements.
    * @param prelevements Liste des prelevements à mettre à jour
    * @param Liste prélèvements à la base de la modification multiple, cette liste est
    * utilisée pour la création des annotations Fichier en batch
    * @param list Annotations associées à mettre à jour ou créer
    * (doivent donc avoir objet, champ, et banque referencees)
    * @param list Annotations associées à supprimmer
    * @param list des non conformites à associer
    * @param boolean cascadeNonSterile pour modifier les prelevements et
    * tous les objets issus comme non stériles
    * @param utilisateur Utilisateur voulant modifier les patients.
    * @param base directory pour enregistrer un fichier associé
    * dans le file system
    */
   void updateMultipleObjectsManager(List<Prelevement> prelevements, List<Prelevement> basePrelevements,
      List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete, List<NonConformite> ncfs,
      boolean cascadeNonSterile, Utilisateur utilisateur, String baseDir);

   /**
    * Recherche les prélèvements dont le code correspond à
    * l'identification d'un dossier externe, en fct d'une liste de
    * banques et d'emetteurs.
    * @param banques Banques des prlvts.
    * @param emetteurs Emetteurs des dossiers.
    * @return Liste de prélèvements.
    */
   List<Prelevement> findByDossierExternesManager(List<Banque> banques, List<Emetteur> emetteurs);

   /**
    * Surcharge du manager createObject pour lui ajouter les non
    * conformites.
    * @param prelevement
    * @param banque
    * @param nature
    * @param maladie
    * @param consentType
    * @param preleveur
    * @param servicePreleveur
    * @param prelevementType
    * @param conditType
    * @param conditMilieu
    * @param transporteur
    * @param operateur
    * @param quantiteUnite
    * @param laboInters
    * @param listAnnoToCreateOrUpdate
    * @param utilisateur
    * @param doValidation
    * @param baseDir
    * @param isImport
    * @param noconfs
    */
   void createObjectWithNonConformitesManager(Prelevement prelevement, Banque banque, Nature nature, Maladie maladie,
      ConsentType consentType, Collaborateur preleveur, Service servicePreleveur, PrelevementType prelevementType,
      ConditType conditType, ConditMilieu conditMilieu, Transporteur transporteur, Collaborateur operateur, Unite quantiteUnite,
      List<LaboInter> laboInters, List<AnnotationValeur> listAnnoToCreateOrUpdate, Utilisateur utilisateur, boolean doValidation,
      String baseDir, boolean isImport, List<NonConformite> noconfs);

   /**
    * Surcharge du manager updateObject pour lui ajouter les non
    * conformites.
    * @param prelevement
    * @param banque
    * @param nature
    * @param maladie
    * @param consentType
    * @param preleveur
    * @param servicePreleveur
    * @param prelevementType
    * @param conditType
    * @param conditMilieu
    * @param transporteur
    * @param operateur
    * @param quantiteUnite
    * @param laboInters
    * @param listAnnoToCreateOrUpdate
    * @param listAnnoToDelete
    * @param utilisateur
    * @param cascadeNonSterile
    * @param doValidation
    * @param baseDir
    * @param multiple
    * @param noconfs
    */
   void updateObjectWithNonConformitesManager(Prelevement prelevement, Banque banque, Nature nature, Maladie maladie,
      ConsentType consentType, Collaborateur preleveur, Service servicePreleveur, PrelevementType prelevementType,
      ConditType conditType, ConditMilieu conditMilieu, Transporteur transporteur, Collaborateur operateur, Unite quantiteUnite,
      List<LaboInter> laboInters, List<AnnotationValeur> listAnnoToCreateOrUpdate, List<AnnotationValeur> listAnnoToDelete,
      Utilisateur utilisateur, Integer cascadeNonSterile, boolean doValidation, String baseDir, boolean multiple,
      List<NonConformite> noconfs);

   /**
    * Supprime les prélèvements et en cascade les échantillons/dérivés dont
    * ils sont parents à partir des ids passés en paramètres.
    * @param ids
    * @param String fantome commentaire
    * @param u Utilisateur (opération suppr)
    * @since 2.0.12
    */
   void removeListFromIdsManager(List<Integer> ids, String comments, Utilisateur u);

   /**
    * Recherche les prélèvements existants pour un patient consultables par un utilisateur
    * connecté à une plateforme passés en paramètres.
    * @param Patient patient
    * @param Utilisateur utilisateur
    * @return Plateforme plateforme
    * @since 2.0.13
    */
   List<Prelevement> findByPatientAndAuthorisationsManager(Patient pat, Plateforme pf, Utilisateur utilisateur);

   /**
    * Renvoie tous les prélèvements pour un code spécifié dans toutes
    * les collections d'une plateforme passée en paramètre.
    * @param code
    * @param pf Plateforme
    * @return liste prélèvement
    * @since 2.1
    */
   List<Prelevement> findByCodeInPlateformeManager(String code, Plateforme pf);

   /**
    * Verifie que les Objets devant etre obligatoirement associes sont non
    * nulls et lance la validation via le Validator. Set la maladie et le
    * patient en cascade car utilisée dans la validation.
    *
    * @param prelevement
    * @param banque
    * @param nature
    * @param consentType
    * @param maladie
    * @param laboInters
    * @param operation demandant la verification
    * @param utilisateur
    */
   void checkRequiredObjectsAndValidate(Prelevement prelevement, Banque banque, Nature nature, ConsentType consentType,
      Maladie maladie, List<LaboInter> laboInters, String operation, Utilisateur utilisateur, boolean doValidation,
      String baseDir);

   /**
    * Recherche tous les prélèvements ids issus des patients dont les valeurs passées 
    * en paramètres correspondent à un identifiant, un nip, ou un nom.
    * @param idsNipsNoms Liste des identifiants noms ou nips de patients.
    * @param banks Liste des banques des prélèvements.
    * @return Liste d'ids prélèvements.
    * @since 2.3.0-gatsbi
    */
   List<Integer> findByPatientIdentifiantOrNomOrNipInListManager(List<String> idsNipsNoms, List<Banque> selectedBanques);
}
