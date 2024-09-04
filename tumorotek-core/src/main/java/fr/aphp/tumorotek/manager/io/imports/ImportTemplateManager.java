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
package fr.aphp.tumorotek.manager.io.imports;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateStatutPartage;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine ImportTemplate.
 * Interface créée le 25/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ImportTemplateManager
{

   /**
    * Recherche un ImportTemplate dont l'identifiant est passé en paramètre.
    * @param tmportTemplateId Id du Template que l'on recherche.
    * @return Un ImportTemplate.
    */
   ImportTemplate findByIdManager(Integer importTemplateId);

   /**
    * Recherche tous les ImportTemplates présents dans la base.
    * @return Liste de ImportTemplates.
    */
   List<ImportTemplate> findAllObjectsManager();

   /**
    * Recherche un ImportTemplate dont le la banque est passée en paramètre.
    * @param banque Banque du template que l'on recherche.
    * @return Une liste d'ImportTemplate.
    */
   List<ImportTemplate> findByBanqueManager(Banque banque);

   //TK-537
   /**
    * récupère la liste des modèles d'imports créés par la banque en paramètre (utilisés ou non) ainsi que ceux partagés 
    * par les autres collections et utilisés par cette banque (au moins un import a été fait sur ceux-ci)
    * @param banque
    * @return liste de modèles
    */
   List<ImportTemplate> findImportTemplateCreatedOrUsedByBanqueWithOrder(Banque banque);
   
   //TK-537
   /**
    * récupère la liste des modèles au statut de partage passé en paramètre pour une banque donnée
    * La liste est triée par nom des modèles 
    * @param eImportTemplateStatutPartage
    * @param banque
    * @return
    */
   List<ImportTemplate> findTemplateByStatutPartageAndBanqueWithOrder(EImportTemplateStatutPartage eImportTemplateStatutPartage,
      Banque banque);

   //TK-537
   /**
    * récupère la liste des modèles d'import non archivés pour un statut partagé donné pour toutes les collections d'une plateforme donnée
    * La liste est triée par nom des modèles 
    * @param eImportTemplateStatutPartage
    * @param plateforme
    * @return
    */
   List<ImportTemplate> findTemplateNotArchiveByStatutPartageAndPlateformeWithOrder(EImportTemplateStatutPartage eImportTemplateStatutPartage,
      Plateforme plateforme);

   /**
    * met à jour le champ statutPartage d'un modèle d'import donné avec la valeur passée en paramètre. 
    * La mise à jour est tracée avec l'utilisateur passé en paramètre
    * @param importTemplateId
    * @param newValue
    * @param loggedUser
    * @throws IllegalArgumentException
    */
   void updateStatutPartage(Integer importTemplateId, EImportTemplateStatutPartage newValue, Utilisateur loggedUser) throws IllegalArgumentException;
   
   /**
    * met à jour le champ archive d'un modèle d'import donné avec la valeur passée en paramètre. 
    * La mise à jour est tracée avec l'utilisateur passé en paramètre
    * @param importTemplateId
    * @param newValue
    * @param loggedUser
    */
   void updateArchive(Integer importTemplateId, Boolean newValue, Utilisateur loggedUser);
   //fin TK-537
   
   /**
    * Renvoie les entités d'un ImportTemplate.
    * @param importTemplate ImportTemplate
    * @return Liste d'Entites.
    */
   Set<Entite> getEntiteManager(ImportTemplate importTemplate);

   /**
    * Recherche les doublons du ImportTemplate passé en paramètre.
    * @param importTemplate Un Template pour lequel on
    * cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(ImportTemplate importTemplate);

   /**
    * Persist une instance d'ImportTemplate dans la base de données.
    * @param importTemplate Nouvelle instance de l'objet à créer.
    * @param banque Banque du template.
    * @param entites Entites du template.
    * @param colonnes Liste d'ImportColonnes.
    */
   void createObjectManager(ImportTemplate importTemplate, Banque banque, List<Entite> entites,
      List<ImportColonne> colonnesToCreate, Utilisateur loggedUser);

   /**
    * Persist une instance d'ImportTemplate dans la base de données.
    * @param importTemplate Instance de l'objet à maj.
    * @param banque Banque du template.
    * @param entites Entites du template.
    * @param colonnes Liste d'ImportColonnes.
    */
   void updateObjectManager(ImportTemplate importTemplate, Banque banque, List<Entite> entites,
      List<ImportColonne> colonnesToCreate, List<ImportColonne> colonnesToremove, Utilisateur loggedUser);

   /**
    * Supprime un ImportTemplate de la base de données.
    * @param importTemplate Template à supprimer de la base de données.
    */
   void removeObjectManager(ImportTemplate importTemplate);
   
   /**
    * @param importTemplate modèle considéré
    * @return  true si le mdodèle passé en paramètre a été modifié après la dernière exécution
    */
   boolean hasBeenModifiedAfterLastExecution(ImportTemplate importTemplate, Banque utilisateurBanque);

}
