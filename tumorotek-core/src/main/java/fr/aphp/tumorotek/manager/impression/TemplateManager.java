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

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine Template.
 * Interface créée le 26/07/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface TemplateManager
{

   /**
    * Recherche un Template dont l'identifiant est passé en paramètre.
    * @param templateId Identifiant du Template que l'on recherche.
    * @return Un Template.
    */
   Template findByIdManager(Integer templateId);

   /**
    * Recherche tous les Templates présents dans la base.
    * @return Liste de Templates.
    */
   List<Template> findAllObjectsManager();

   /**
    * Recherche un Template dont le la banque est passée en paramètre.
    * @param banque Banque du template que l'on recherche.
    * @return Un Template.
    */
   List<Template> findByBanqueManager(Banque banque);

   /**
    * Recherche un Template dont la banque et l'entité sont passées
    *  en paramètre.
    * @param banque Banque du template.
    * @param entite Entite du template.
    * @return Un Template.
    */
   List<Template> findByBanqueEntiteManager(Banque banque, Entite entite);

   /**
    * Recherche les doublons du Template passé en paramètre.
    * @param template Un Template pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Template template);

   /**
    * Persist une instance de Template dans la base de données.
    * @param template Nouvelle instance de l'objet à créer.
    * @param banque Banque du template.
    * @param entite Entite du template.
    * @param blocs Liste de BlocImpressionTemplate.
    * @param champs Liste de ChampImprime.
    * @param annotations Liste de TableAnnotation.
    */
   void createObjectManager(Template template, Banque banque, Entite entite, List<BlocImpressionTemplate> blocs,
      List<ChampImprime> champs, List<TableAnnotationTemplate> annotations);

   /**
    * Persist une instance de Template dans la base de données.
    * @param template Nouvelle instance de l'objet à créer.
    * @param banque Banque du template.
    * @param entite Entite du template.
    * @param blocs Liste de BlocImpressionTemplate.
    * @param champs Liste de ChampImprime.
    * @param annotations Liste de TableAnnotation.
    * @param cles Liste des CleImpression
    *
    * @since 2.2.0
    */
   void createObjectManager(Template template, Banque banque, Entite entite, List<BlocImpressionTemplate> blocs,
      List<ChampImprime> champs, List<TableAnnotationTemplate> annotations, List<CleImpression> cles);

   /**
    * Persist une instance de Template dans la base de données.
    * @param template Instance de l'objet à maj.
    * @param banque Banque du template.
    * @param entite Entite du template.
    * @param blocs Liste de BlocImpressionTemplate.
    * @param blocsToCreate Liste de BlocImpressionTemplate à créer.
    * @param champs Liste de ChampImprime.
    * @param champsToCreate Liste de ChampImprime à créer.
    * @param annotations Liste de TableAnnotation.
    * @param annotationsToCreate Liste de TableAnnotation à créer.
    */
   void updateObjectManager(Template template, Banque banque, Entite entite, List<BlocImpressionTemplate> blocs,
      List<BlocImpressionTemplate> blocsToCreate, List<ChampImprime> champs, List<ChampImprime> champsToCreate,
      List<TableAnnotationTemplate> annotations, List<TableAnnotationTemplate> annotationsToCreate);

   /**
   * Persist une instance de Template dans la base de données.
   * @param template Instance de l'objet à maj.
   * @param banque Banque du template.
   * @param entite Entite du template.
   * @param blocs Liste de BlocImpressionTemplate.
   * @param blocsToCreate Liste de BlocImpressionTemplate à créer.
   * @param champs Liste de ChampImprime.
   * @param champsToCreate Liste de ChampImprime à créer.
   * @param annotations Liste de TableAnnotation.
   * @param annotationsToCreate Liste de TableAnnotation à créer.
   * @param cles Liste des CleImpression
   *
   * @since 2.2.0
   */
   void updateObjectManager(Template template, Banque banque, Entite entite, List<BlocImpressionTemplate> blocs,
      List<BlocImpressionTemplate> blocsToCreate, List<ChampImprime> champs, List<ChampImprime> champsToCreate,
      List<TableAnnotationTemplate> annotations, List<TableAnnotationTemplate> annotationsToCreate, List<CleImpression> cles);

   /**
    * Supprime un Template de la base de données.
    * @param template Template à supprimer de la base de données.
    */
   void removeObjectManager(Template template);

}
