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
package fr.aphp.tumorotek.dao.io.export;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine Champ.
 * Interface créée le 25/11/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 */
public interface ChampEntiteDao extends GenericDaoJpa<ChampEntite, Integer>
{

   /**
    * Recherche les champs dont l'entité est passée en paramètre.
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @return Liste d'Entité.
    */
   List<ChampEntite> findByEntite(Entite entite);

   /**
    * Recherche les champs dont l'entité et le nom sont passés en paramètres.
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @param nom Nom du champ à rechercher.
    * @return Liste de ChampEntités.
    */
   List<ChampEntite> findByEntiteAndNom(Entite entite, String nom);

   /**
    * Recherche les champs importables (ou non) dont l'entité est
    * passée en paramètre.
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @param canImport True ou false.
    * @return Liste de ChampEntités.
    */
   List<ChampEntite> findByEntiteAndImport(Entite entite, Boolean canImport);
   
   /**
    * Recherche les champs importables (ou non) dont l'entité est
    * passée en paramètre et dont le datatype correspond à la liste
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @param canImport True ou false.
    * @param dataTypeList liste des datatypes souhaités
    * @return Liste de ChampEntités.
    */
   List<ChampEntite> findByEntiteAndImportAndDataType(Entite entite, Boolean canImport, List<DataType> dataTypeList);

   /**
    * Recherche les champs importables (ou non) dont l'entité est
    * passée en paramètre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @param canImport True ou false.
    * @param isNullable True ou false.
    * @return Liste de ChampEntités.
    */
   List<ChampEntite> findByEntiteImportObligatoire(Entite entite, Boolean canImport, Boolean isNullable);

   /**
    * Recherche tous les champs entités associés à un template 
    * d'importation pour une entité.
    * @param template
    * @param entite
    * @return List ChampEntite
    * @since 2.0.12
    */
   List<ChampEntite> findByImportTemplateAndEntite(ImportTemplate template, Entite e);
}
