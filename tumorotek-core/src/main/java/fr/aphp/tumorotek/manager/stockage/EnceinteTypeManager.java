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
package fr.aphp.tumorotek.manager.stockage;

import java.util.List;

import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

/**
 *
 * Interface pour le manager du bean de domaine EnceinteType.
 * Interface créée le 17/03/10.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EnceinteTypeManager extends TKThesaurusManager
{

   /**
    * Recherche un EnceinteType dont l'identifiant est passé en paramètre.
    * @param enceinteTypeId Identifiant du EnceinteType que l'on recherche.
    * @return Un EnceinteType.
    */
   @Override
   EnceinteType findByIdManager(Integer enceinteTypeId);

   /**
    * Recherche tous les EnceinteTypes présents dans la base,
    * sauf celui de type 'BOITE' pour une plateforme donnée.
    * @param plateforme
    * @return Liste ordonnée de EnceinteTypes.
    */
   List<EnceinteType> findAllObjectsExceptBoiteManager(Plateforme pf);

   /**
    * Recherche les types d'enceinte en fct de leur nom.
    * @param type Nom des types d'enceintes recherchées.
    * @return Liste d'enceintes.
    */
   List<EnceinteType> findByTypeManager(String type, Plateforme pf);

}
