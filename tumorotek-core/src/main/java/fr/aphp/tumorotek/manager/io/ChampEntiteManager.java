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
package fr.aphp.tumorotek.manager.io;

import java.util.List;

import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
	 *
	 * Interface pour le manager du bean de domaine ChampEntite.
	 * Interface créée le 29/01/10.
	 *
	 * @author Maxime GOUSSEAU
	 * @version 2.0
	 *
	 */
public interface ChampEntiteManager
{

   /**
    * Recherche un ChampEntite dont l'identifiant est passé en paramètre.
    * @param id Identifiant du ChampEntite que l'on recherche.
    * @return un ChampEntite.
    */
   ChampEntite findByIdManager(Integer id);

   /**
    * Recherche tous les ChampEntite présents dans la BDD.
    * @return Liste de ChampEntite.
    */
   List<ChampEntite> findAllObjectsManager();

   /**
    * Recherche les champs dont l'entité est passée en paramètre.
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @return Liste d'Entité.
    */
   List<ChampEntite> findByEntiteManager(Entite entite);

   /**
    * Recherche les champs dont l'entité et le nom sont passés 
    * en paramètres. Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @param nom Nom du ChampEntite.
    * @return Liste d'Entité.
    */
   List<ChampEntite> findByEntiteAndNomManager(Entite entite, String nom);

   /**
    * Renvoie les ChampEntites importables (ou non) pour
    * une entité.
    * @param entite
    * @param canImport
    * @return Liste de ChampEntites.
    */
   List<ChampEntite> findByEntiteAndImportManager(Entite entite, Boolean canImport);

   /**
    * Renvoie les ChampEntites importables, nullable (ou non) pour
    * une entité.
    * @param entite
    * @param canImport
    * @param isNullable
    * @return Liste de ChampEntites.
    */
   List<ChampEntite> findByEntiteImportAndIsNullableManager(Entite entite, Boolean canImport, Boolean isNullable);
}
