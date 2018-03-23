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

import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine ImportColonne.
 * Interface créée le 25/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0.13.2
 *
 */
public interface ImportColonneManager
{

   /**
    * Recherche un ImportColonne dont l'identifiant est passé en paramètre.
    * @param importColonneId Id que l'on recherche.
    * @return Un ImportColonne.
    */
   ImportColonne findByIdManager(Integer importColonneId);

   /**
    * Recherche tous les ImportColonnes présents dans la base.
    * @return Liste d'ImportColonnes.
    */
   List<ImportColonne> findAllObjectsManager();

   /**
    * Recherche un ImportColonne dont le template est passée en paramètre.
    * @param template ImportTemplate des ImportColonnes que l'on recherche.
    * @return Une liste d'ImportColonnes.
    */
   List<ImportColonne> findByImportTemplateManager(ImportTemplate template);

   /**
    * Recherche les ImportColonnes de l'ImportTemplate et de l'entité.
    * @param importTemplate Template.
    * @param entite Entité.
    * @return Liste d'ImportColonnes.
    */
   List<ImportColonne> findByTemplateAndEntiteManager(ImportTemplate importTemplate, Entite entite);

   /**
    * Recherche les ImportColonnes de l'ImportTemplate et de l'entité
    * (qui sont liées à des annotations).
    * @param importTemplate Template.
    * @param entite Entité.
    * @return Liste d'ImportColonnes.
    */
   List<ImportColonne> findByTemplateAndAnnotationEntiteManager(ImportTemplate importTemplate, Entite entite);

   /**
    * Recherche les ImportColonnes de l'ImportTemplate et du datatype.
    * @param importTemplate Template.
    * @param dataType DataType.
    * @return Liste d'ImportColonnes.
    */
   List<ImportColonne> findByTemplateAndDataTypeManager(ImportTemplate importTemplate, DataType dataType);

   /**
    * Recherche les ImportColonnes de l'ImportTemplate qui sont des thésuarus.
    * @param importTemplate Template.
    * @return Liste d'ImportColonnes.
    */
   List<ImportColonne> findByTemplateAndThesaurusManager(ImportTemplate importTemplate);

   /**
    * Recherche les ImportColonnes de l'ImportTemplate qui sont des annotations de 
    * type thésaurus ou depuis v 2.0.13.2 des thésaurus multiples
    * @param importTemplate Template.
    * @return Liste d'ImportColonnes.
    * @version 2.0.13.2
    */
   List<ImportColonne> findByTemplateAndAnnotationThesaurusManager(ImportTemplate importTemplate);

   /**
    * Recherche les doublons de l'ImportColonne passé en paramètre.
    * @param importColonne Un ImportColonne pour lequel on 
    * cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(ImportColonne importColonne);

   /**
    * Recherche des doublons dans une liste d'ImportColonne.
    * @param colonnes Liste d'ImportColonnes.
    * @return True s'il y a des doublons.
    */
   Boolean findDoublonInListManager(List<ImportColonne> colonnes);

   /**
    * Valide une instance d'ImportColonne dans la base de données.
    * @param importColonne Nouvelle instance de l'objet à créer.
    * @param template ImportTemplate.
    * @param champ Champ.
    */
   void validateObjectManager(ImportColonne importColonne, ImportTemplate template, Champ champ, String operation);

   /**
    * Persist une instance d'ImportColonne dans la base de données.
    * @param importColonne Nouvelle instance de l'objet à créer.
    * @param template ImportTemplate.
    * @param champ Champ.
    */
   void createObjectManager(ImportColonne importColonne, ImportTemplate template, Champ champ);

   /**
    * Persist une instance d'ImportColonne dans la base de données.
    * @param importColonne Instance de l'objet à maj.
    * @param template ImportTemplate.
    * @param champ Champ.
    */
   void updateObjectManager(ImportColonne importColonne, ImportTemplate template, Champ champ);

   /**
    * Supprime un ImportColonne de la base de données.
    * @param importColonne ImportColonne à supprimer de la base de données.
    */
   void removeObjectManager(ImportColonne importColonne);

}
