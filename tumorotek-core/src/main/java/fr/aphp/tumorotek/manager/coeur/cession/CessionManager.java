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
package fr.aphp.tumorotek.manager.coeur.cession;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.cession.CederObjet;
import fr.aphp.tumorotek.model.coeur.cession.Cession;
import fr.aphp.tumorotek.model.coeur.cession.CessionExamen;
import fr.aphp.tumorotek.model.coeur.cession.CessionStatut;
import fr.aphp.tumorotek.model.coeur.cession.CessionType;
import fr.aphp.tumorotek.model.coeur.cession.Contrat;
import fr.aphp.tumorotek.model.coeur.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Cession.
 * Interface créée le 01/02/10.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public interface CessionManager
{

   /**
    * Recherche une Cession dont l'identifiant est passé en paramètre.
    * @param cessionId Identifiant de la Cession que l'on recherche.
    * @return Une Cession.
    */
   Cession findByIdManager(Integer cessionId);

   /**
    * Recherche toutes les Cessions présentes dans la base.
    * @return Liste de Cessions.
    */
   List<Cession> findAllObjectsManager();

   /**
    * Recherche toutes les Cessions d'une banque.
    * @param banques liste Banque pour lesquelles on recherche.
    * @return Liste de Cessions.
    */
   List<Cession> findAllObjectsByBanquesManager(List<Banque> banques);

   /**
    * Recherche tous les ids des Cessions des banque.
    * @param banques liste de Banques pour lesquelles on recherche.
    * @return Liste de Cessions.
    */
   List<Integer> findAllObjectsIdsByBanquesManager(List<Banque> banques);

   /**
    * Recherche une liste de Cessions dont le numéro est égal à
    * celui passé en paramètre.
    * @param numero Numéro pour lequel on recherche des Cessions.
    * @return Liste de Cessions.
    */
   List<Cession> findByNumeroLikeManager(String numero, boolean exactMatch);

   /**
    * Recherche une liste de Cessions dont le numéro est égal à
    * celui passé en paramètre.
    * @param numero Numéro pour lequel on recherche des Cessions.
    * @param banques Banques auxquelles appartiennent la Cession.
    * @param exactMatch
    * @return Liste de Cessions.
    * @version 2.0.10
    */
   List<Integer> findByNumeroWithBanqueReturnIdsManager(String numero, List<Banque> banques, boolean exactMatch);

   /**
    * Recherche une liste de Cessions dont le statut est égal à
    * celui passé en paramètre.
    * @param statut Valeur du CessionStatut pour lequel on recherche 
    * des Cessions.
    * @param banques Banques auxquelles appartiennent les Cession.
    * @return Liste de Cessions.
    */
   List<Integer> findByStatutWithBanquesReturnIdsManager(String statut, List<Banque> banques);

   /**
    * Recherche une liste de Cessions dont l'état incomplet est égal à
    * celui passé en paramètre.
    * @param incomplet EtatIncomplet pour lequel on recherche des Cessions.
    * @param banques Banques auxquelles appartiennent la Cession.
    * @return Liste de Cessions.
    */
   List<Integer> findByEtatIncompletWithBanquesReturnIdsManager(boolean incomplet, List<Banque> banques);

   /**
    * Recherche la liste des numéros utilisés par les Cessions liées à la
    * banque passée en paramètre.
    * @param banque Banque pour laquelle on recherche les numéros.
    * @return Liste de numéros.
    */
   List<String> findAllCodesForBanqueManager(Banque banque);

   /**
    * Renvoie tous les CederObjets d'une cession.
    * @param cession Cession pour laquelle on veut les objets cédés.
    * @return Une liste de CederObjets.
    */
   Set<CederObjet> getCederObjetsManager(Cession cession);

   /**
    * Recherche les doublons (= toute cession appartenant à la même PF 
    * partageant le même numéro) de la Cession passée en paramètre.
    * @param cession Un Cession pour laquelle on cherche des doublons.
    * @return True s'il existe des doublons.
    * @version 2.1
    */
   Boolean findDoublonManager(Cession cession);

   /**
    * Test si une Cession est lié à des CederObjets ou à des Retours.
    * @param cession Cession que l'on souhaite tester.
    * @return Vrai si la Cession est utilisée.
    */
   Boolean isUsedObjectManager(Cession cession);

   /**
    * Recherche toutes les Cessions dont la date de creation systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banques Banques auxquelles appartiennent les Cessions.
    * @return Liste de Cessions.
    */
   List<Integer> findAfterDateCreationReturnIdsManager(Calendar date, List<Banque> banques);

   /**
    * Recherche toutes les Cessions dont la date de modification systeme est 
    * posterieure ou egale a celle passee en parametre.
    * @param date
    * @param banque Banque à laquelle appartient la Cession.
    * @return Liste de Cessions.
    */
   List<Cession> findAfterDateModificationManager(Calendar date, Banque banque);

   /**
    * Recherche les dernières Cessions créées dans le systeme.
    * @param banques Banques auxquelles appartiennent les Cessions.
    * @param nbResults Nombre de résultats souhaités.
    * @return Liste de Cessions.
    */
   List<Cession> findLastCreationManager(List<Banque> banques, int nbResults);

   /**
    * Persist une instance de Cession dans la base de données.
    * @param cession Nouvelle instance de l'objet à créer.
    * @param banque Banque de la cession.
    * @param cessionType Type de la cession.
    * @param cessionExamen Examen de la cession.
    * @param mta Contrat de la cession.
    * @param destinataire  Collaborateur destinataire.
    * @param servDest Service de destnation.
    * @param demandeur Collaborateur demandeur.
    * @param cessionStatut Statut de la cession.
    * @param executant Collaborateur exécutant.
    * @param transporteur Transporteur de la cession.
    * @param destructionMotif Motif de detruction.
    * @param liste des valeurs d'annotation à créer.
    * @param filesCreated liste de fichier créés
    * @param utilisateur Utilisateur ayant créé la cession.
    * @param cederObjets Liste de CederObjet.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void createObjectManager(Cession cession, Banque banque, CessionType cessionType, CessionExamen cessionExamen, Contrat mta,
      Collaborateur destinataire, Service servDest, Collaborateur demandeur, CessionStatut cessionStatut, Collaborateur executant,
      Transporteur transporteur, DestructionMotif destructionMotif, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      List<File> filesCreated, Utilisateur utilisateur, List<CederObjet> cederObjets, String baseDir);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param cession Objet à mettre à jour dans la base.
    * @param banque Banque de la cession.
    * @param cessionType Type de la cession.
    * @param cessionExamen Examen de la cession.
    * @param mta Contrat de la cession.
    * @param destinataire  Collaborateur destinataire.
    * @param servDest Service de destnation.
    * @param demandeur Collaborateur demandeur.
    * @param cessionStatut Statut de la cession.
    * @param executant Collaborateur exécutant.
    * @param transporteur Transporteur de la cession.
    * @param destructionMotif Motif de detruction.
    * @param liste des valeurs d'annotations à créer/modifier
    * @param liste des valeurs d'annotations à supprimer.
    * @param filesCreated liste de fichier créés
    * @param filesToDelete liste de fichier à supprimer
    * @param utilisateur Utilisateur ayant créé la cession.
    * @param cederObjets Liste de CederObjet.
    * @param base directory pour enregistrer un fichier associé 
    * dans le file system
    */
   void updateObjectManager(Cession cession, Banque banque, CessionType cessionType, CessionExamen cessionExamen, Contrat mta,
      Collaborateur destinataire, Service servDest, Collaborateur demandeur, CessionStatut cessionStatut, Collaborateur executant,
      Transporteur transporteur, DestructionMotif destructionMotif, List<AnnotationValeur> listAnnoToCreateOrUpdate,
      List<AnnotationValeur> listAnnoToDelete, List<File> filesCreated, List<File> filesToDelete, Utilisateur utilisateur,
      List<CederObjet> cederObjets, String baseDir);

   /**
    * Supprime une Cession de la base de données.
    * @param cession Cession à supprimer de la base de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    * @param filesToDelete liste de fichier à supprimer
    */
   void removeObjectManager(Cession cession, String comments, Utilisateur usr, List<File> filesToDelete);

   /**
    * Recherche les cessions dont l'id se trouve dans la liste.
    * @param ids Liste d'identifiants.
    * @return Liste de cessions.
    */
   List<Cession> findByIdsInListManager(List<Integer> ids);

   /**
    * Calcules les comptes / types echantillon/derive pour 
    * l'impression du bon de livraison de 
    * la cession passée en paramètre. 
    * Les natures des échantillons apparaissent en premier, suivies des natures
    * @param cession
    * @return Map<String,Number>
    * @version 2.0.10
    */
   Map<String, Number> getTypesAndCountsManager(Cession cession);

   /**
    * Compte le nombre de cessions dont le demandeur est le 
    * collaborateur passé en param
    * @param collaborateur
    * @return
    */
   public Long findCountByDemandeurManager(Collaborateur colla);

   /**
    * Compte le nombre de cessions dont le destinataire est le 
    * collaborateur passé en param
    * @param collaborateur
    * @return
    */
   public Long findCountByDestinataireManager(Collaborateur colla);

   /**
    * Compte le nombre de cessions dont l'executant est le 
    * collaborateur passé en param
    * @param collaborateur
    * @return
    */
   public Long findCountByExecutantManager(Collaborateur colla);

   /**
    * Supprime les prélèvements et en cascade les échantillons/dérivés dont 
    * ils sont parents à partir des ids passés en paramètres.
    * @param ids
    * @param String fantome commentaire
    * @param u Utilisateur (opération suppr)
    * @since 2.0.12
    */
   void removeListFromIdsManager(List<Integer> ids, String comment, Utilisateur u);

   /**
    * Renvoie toutes les cessions pour un numéro spécifié dans toutes 
    * les collections d'une plateforme passée en paramètre.
    * @param code
    * @param pf Plateforme
    * @return liste cessions
    * @since 2.1
    */
   List<Cession> findByNumeroInPlateformeManager(String numero, Plateforme pf);

   /**
    * Estampille la cession passée en paramètre avec la date correspondant 
    * à la dernière validation du contenu de la cession obtenue par scan(s) des 
    * boites de transfert.
    * @param cess
    * @param date complete full scan check Calendar
    */
   void applyScanCheckDateManager(Cession cess, Calendar scanDate);

}
