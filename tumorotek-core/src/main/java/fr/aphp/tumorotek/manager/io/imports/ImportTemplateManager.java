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
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

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
      List<ImportColonne> colonnesToCreate);

   /**
    * Persist une instance d'ImportTemplate dans la base de données.
    * @param importTemplate Instance de l'objet à maj.
    * @param banque Banque du template.
    * @param entites Entites du template.
    * @param colonnes Liste d'ImportColonnes.
    */
   void updateObjectManager(ImportTemplate importTemplate, Banque banque, List<Entite> entites,
      List<ImportColonne> colonnesToCreate, List<ImportColonne> colonnesToremove);

   /**
    * Supprime un ImportTemplate de la base de données.
    * @param importTemplate Template à supprimer de la base de données.
    */
   void removeObjectManager(ImportTemplate importTemplate);

}
