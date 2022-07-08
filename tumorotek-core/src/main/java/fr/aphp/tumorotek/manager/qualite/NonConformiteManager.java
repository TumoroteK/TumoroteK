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
package fr.aphp.tumorotek.manager.qualite;

import java.util.List;

import fr.aphp.tumorotek.manager.PfDependantTKThesaurusManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le manager du bean de domaine NonConformite.
 * Interface créée le 08/11/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface NonConformiteManager extends PfDependantTKThesaurusManager<NonConformite>
{

   /**
    * Recherche un NonConformite dont l'identifiant est passé en paramètre.
    * @param nonConformiteId Identifiant du NonConformite que l'on recherche.
    * @return Un NonConformite.
    */
   @Override
   NonConformite findByIdManager(Integer nonConformiteId);

   /**
    * Recherche tous les NonConformites présents dans la base.
    * @return Liste de NonConformites.
    */
   List<NonConformite> findAllObjectsManager();

   /**
    * Recherche tous les NonConformites en fct de leur type
    * (sous le forme de l'objet) et de la plateforme.
    * @param plateforme Plateforme des NonConformites.
    * @param typeObj Type des NonConformites recherchés.
    * @return Liste de NonConformites.
    */
   List<NonConformite> findByPlateformeAndTypeObjManager(Plateforme plateforme, ConformiteType typeObj);

   /**
    * Recherche tous les NonConformites en fct de leur type
    * (sous le forme d'un string) et de la plateforme.
    * @param plateforme Plateforme des NonConformites.
    * @param type Type des NonConformites recherchés.
    * @param Entite concernée
    * @return Liste de NonConformites.
    */
   List<NonConformite> findByPlateformeEntiteAndTypeStringManager(Plateforme plateforme, String typeStr, Entite e);

   /**
    * Extrait les non conformités à partir d'une liste d'objets
    * ObjetNonConforme.
    * (Mauvais design JPA? entre objets et non conformités, voir
    * Operation)
    * @param objs
    * @return liste des non conformités
    */
   List<NonConformite> getFromObjetNonConformes(List<ObjetNonConforme> objs);
}
