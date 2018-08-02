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
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.ChampImprimePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Interface pour le manager du bean de domaine ChampImprime.
 * Interface créée le 26/07/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ChampImprimeManager
{

   /**
    * Recherche un ChampImprime dont l'identifiant est 
    * passé en paramètre.
    * @param pk Identifiant du ChampImprime que 
    * l'on recherche.
    * @return Un ChampImprime.
    */
   ChampImprime findByIdManager(ChampImprimePK pk);

   /**
    * Recherche tous les ChampImprimes présents dans la base.
    * @return Liste de ChampImprimes.
    */
   List<ChampImprime> findAllObjectsManager();

   /**
    * Recherche les ChampImprimes sauf celui dont la clé 
    * primaire est passée en paramètre.
    * @param pk ChampImprimePK.
    * @return Liste de ChampImprimes.
    */
   List<ChampImprime> findByExcludedPKManager(ChampImprimePK pk);

   /**
    * Recherche les ChampImprimes dont le Template est égal au 
    * paramètre.
    * @param template Template des ChampImprimes recherchés.
    * @return une liste de ChampImprimes.
    */
   List<ChampImprime> findByTemplateManager(Template template);

   /**
    * Recherche les ChampImprimes dont le Template et le bloc sont
    * égaux aux paramètres.
    * @param template Template des ChampImprimes recherchés.
    * @param bloc BlocImpression des ChampImprimes recherchés.
    * @return une liste de ChampImprimes.
    */
   List<ChampImprime> findByTemplateAndBlocManager(Template template, BlocImpression bloc);

   /**
    * Recherche les doublons pour un ChampImprime.
    * @param template Template  du ChampImprime.
    * @param champEntite ChampEntite du ChampImprime.
    * @param bloc BlocImpression du ChampImprime.
    * @return True si le ChampImprime existe déjà.
    */
   Boolean findDoublonManager(Template template, ChampEntite champEntite, BlocImpression bloc);

   /**
    * Valide l'objet formé par les associations en paramètres.
    *@param template Template  du ChampImprime.
    * @param champEntite ChampEntite du ChampImprime.
    * @param bloc BlocImpression du ChampImprime.
    */
   void validateObjectManager(Template template, ChampEntite champEntite, BlocImpression bloc);

   /**
    * Persist une instance de ChampImprime dans la base de données.
    * @param champImprime ChampImprime à créer.
    * @param template Template  du ChampImprime.
    * @param champEntite ChampEntite du ChampImprime.
    * @param bloc BlocImpression du ChampImprime.
    */
   void createObjectManager(ChampImprime champImprime, Template template, ChampEntite champEntite, BlocImpression bloc);

   /**
    * Maj une instance de ChampImprime dans la base de données.
    * @param champImprime ChampImprime à Maj.
    * @param template Template  du ChampImprime.
    * @param champEntite ChampEntite du ChampImprime.
    * @param bloc BlocImpression du ChampImprime.
    */
   void updateObjectManager(ChampImprime champImprime, Template template, ChampEntite champEntite, BlocImpression bloc);

   /**
    * Supprime une instance de ChampImprime de la base.
    * @param champImprime Objet à supprimer.
    */
   void removeObjectManager(ChampImprime champImprime);

}
