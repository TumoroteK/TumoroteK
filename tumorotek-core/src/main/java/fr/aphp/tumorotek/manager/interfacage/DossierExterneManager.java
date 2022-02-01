package fr.aphp.tumorotek.manager.interfacage;

import java.util.Hashtable;
import java.util.List;

import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

/**
 *
 * Interface pour le manager du bean de domaine DossierExterne.
 * Interface créée le 07/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.2.3-genno
 *
 */
public interface DossierExterneManager
{

   /**
    * Recherche un DossierExterne dont l'identifiant est passé en paramètre.
    * @param dossierExterneId Identifiant du dossier que l'on recherche.
    * @return Un DossierExterne.
    */
   DossierExterne findByIdManager(Integer dossierExterneId);

   /**
    * Recherche tous les DossierExternes présents dans la base.
    * @return Liste de DossierExternes.
    */
   List<DossierExterne> findAllObjectsManager();

   /**
    * Recherche tous les DossierExternes d'un Emetteur.
    * @param emetteur Emetteur des dossiers recherchés.
    * @return Liste de DossierExternes.
    */
   List<DossierExterne> findByEmetteurManager(Emetteur emetteur);

   /**
    * Recherche tous les DossierExternes d'un Emetteur et d'un numéro.
    * @param emetteur Emetteur des dossiers recherchés.
    * @param numero Identification des dossiers recherchés.
    * @return Liste de DossierExternes.
    */
   List<DossierExterne> findByEmetteurAndIdentificationManager(Emetteur emetteur, String numero);

   /**
    * Recherche tous les DossierExternes des Emetteurs et d'un numéro.
    * @param emetteurs Liste des Emetteurs des dossiers recherchés.
    * @param numero Identification des dossiers recherchés.
    * @return Liste de DossierExternes.
    */
   List<DossierExterne> findByEmetteurInListAndIdentificationManager(List<Emetteur> emetteurs, String identification);

   /**
    * Recherche tous les DossierExternes pour un numéro.
    * @param numero Identification des dossiers recherchés.
    * @return Liste de DossierExternes.
    */
   List<DossierExterne> findByIdentificationManager(String numero);

   /**
    * Recherche les doublons d'un DossierExterne.
    * @param dossierExterne DossierExterne dont on cherche des doublons.
    * @return True s'il y a un doublon.
    */
   boolean findDoublonManager(DossierExterne dossierExterne);

   /**
    * Vérifie que le dossier externe est valide.
    * @param dossierExterne dossier à tester.
    * @param emetteur Emetteur.
    * @param blocExternes Liste des blocs du dossier.
    * @param valeurExternes Liste des valeurs des blocs.
    */
   void validateDossierExterneManager(DossierExterne dossierExterne, Emetteur emetteur, List<BlocExterne> blocExternes,
      Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes);

   /**
    * Crée un dossier externe.
    * @param dossierExterne dossier à créer.
    * @param emetteur Emetteur.
    * @param blocExternes Liste des blocs du dossier.
    * @param valeurExternes Liste des valeurs des blocs
    * @param max taille table pour First In First Out
    * @version 2.1
    */
   void createObjectManager(DossierExterne dossierExterne, Emetteur emetteur, List<BlocExterne> blocExternes,
      Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes, int max);

   /**
    * Supprime un dossier.
    * @param dossierExterne Dossier à supprimer.
    */
   void removeObjectManager(DossierExterne dossierExterne);
   
   /**
    * Recherche tous les DossierExternes contenant une valeur externe 'like" la valeur 
    * passée en paramètre pour le champ spécifié
    * @param emetteur
    * @param champ entite Id
    * @param valeur;
    * @return Liste de DossierExternes.
    * @since 2.2.3-genno
    */
   List<DossierExterne> findChildrenByEmetteurValeurManager(Emetteur emet, Integer champEntiteId, String valeur);

   /**
    * Recherche les dossiers externes 'parent' = entite Id is null 
    * pour un émetteur passé en paramètre
    * @param emetteur
    * @return liste dossiers externes
    */
   List<DossierExterne> findByEmetteurAndEntiteNullManager(Emetteur emet);
}