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
package fr.aphp.tumorotek.manager.impression;

import java.util.List;

import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

/**
 *
 * Interface pour le manager du bean de domaine BlocImpressionTemplate.
 * Interface créée le 23/07/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface BlocImpressionTemplateManager
{

   /**
    * Recherche un BlocImpressionTemplate dont l'identifiant est 
    * passé en paramètre.
    * @param pk Identifiant du BlocImpressionTemplate que 
    * l'on recherche.
    * @return Un BlocImpressionTemplate.
    */
   BlocImpressionTemplate findByIdManager(BlocImpressionTemplatePK pk);

   /**
    * Recherche tous les BlocImpressionTemplates présents dans la base.
    * @return Liste de BlocImpressionTemplates.
    */
   List<BlocImpressionTemplate> findAllObjectsManager();

   /**
    * Recherche les BlocImpressionTemplates sauf celui dont la clé 
    * primaire est passée en paramètre.
    * @param pk BlocImpressionTemplatePK.
    * @return Liste de BlocImpressionTemplates.
    */
   List<BlocImpressionTemplate> findByExcludedPKManager(BlocImpressionTemplatePK pk);

   /**
    * Recherche les BlocImpressionTemplates dont le Template est égal au 
    * paramètre.
    * @param template Template des BlocImpressionTemplate recherchés.
    * @return une liste de BlocImpressionTemplate.
    */
   List<BlocImpressionTemplate> findByTemplateManager(Template template);

   /**
    * Recherche les doublons pour un BlocImpressionTemplate.
    * @param template Template  du BlocImpressionTemplate.
    * @param blocImpression BlocImpression du BlocImpressionTemplate.
    * @return True si le BlocImpressionTemplate existe déjà.
    */
   Boolean findDoublonManager(Template template, BlocImpression blocImpression);

   /**
    * Valide l'objet formé par les associations en paramètres.
    * @param template Template  du BlocImpressionTemplate.
    * @param blocImpression BlocImpression du BlocImpressionTemplate.
    */
   void validateObjectManager(Template template, BlocImpression blocImpression);

   /**
    * Persist une instance de BlocImpressionTemplate dans la base de données.
    * @param blocImpressionTemplate BlocImpressionTemplate à créer.
    * @param template Template  du BlocImpressionTemplate.
    * @param blocImpression BlocImpression du BlocImpressionTemplate.
    */
   void saveManager(BlocImpressionTemplate blocImpressionTemplate, Template template, BlocImpression blocImpression);

   /**
    * Maj une instance de BlocImpressionTemplate dans la base de données.
    * @param blocImpressionTemplate BlocImpressionTemplate à maj.
    * @param template Template  du BlocImpressionTemplate.
    * @param blocImpression BlocImpression du BlocImpressionTemplate.
    */
   void saveManager(BlocImpressionTemplate blocImpressionTemplate, Template template, BlocImpression blocImpression);

   /**
    * Supprime une instance de BlocImpressionTemplate de la base.
    * @param blocImpressionTemplate Objet à supprimer.
    */
   void deleteByIdManager(BlocImpressionTemplate blocImpressionTemplate);
}
