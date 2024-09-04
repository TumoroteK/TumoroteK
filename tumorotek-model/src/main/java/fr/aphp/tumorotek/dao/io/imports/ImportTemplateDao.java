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
package fr.aphp.tumorotek.dao.io.imports;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;

/**
 *
 * Interface pour le DAO du bean de domaine ImportTemplate.
 *
 * Date: 24/01/2011
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public interface ImportTemplateDao extends GenericDaoJpa<ImportTemplate, Integer>
{

   /**
    * Recherche les ImportTemplates d'une banque ordonnés par nom.
    * @param banque Banque des Imprtemplates.
    * @return Liste d'ImportTemplate.
    */
   List<ImportTemplate> findByBanqueWithOrder(Banque banque);
   
   //TK-537 : début
   /**
    * Recherche les ImportTemplates d'une banque à un statut de partage donné et trie le résultat par nom.
    * @param partageStatutCode code du statut de partage à prendre en compte
    * @param banque Banque des ImportTemplates.
    * @return Liste d'ImportTemplate triés par nom.
    */
   List<ImportTemplate> findTemplateByStatutPartageAndBanqueWithOrder(Integer partageStatutCode, Banque banque);

   /**
    * Recherche les ImportTemplates d'une plateforme à un statut de partage donné et à une valeur "archive" donnée et tri le résultat par nom.
    * @param partageStatutCode code du statut de partage à prendre en compte
    * @param archive valeur (true ou false) pour ne ramener que les modèles archivés ou non
    * @param plateforme Plateforme des ImportTemplates.
    * @return Liste d'ImportTemplate triés par nom.
    */
   List<ImportTemplate> findTemplateByStatutPartageAndArchiveAndPlateformeWithOrder(Integer partageStatutCode, Boolean archive, Plateforme plateforme);
   
   /**
    * Recherche les ImportTemplates partagés et utilisés par une banque donnée
    * @param banqueId l'id de la banque à prendre en compte
    * @return Liste d'ImportTemplate triés par nom
    */
   List<ImportTemplate> findTemplatePartageUtiliseByBanque(Integer banqueId);
   //TK-537 : fin 
   
   /**
    * Recherche les ImportTemplates dont l'id est différent de celui
    * passé en paramètres.
    * @param excludedId Id à exclure.
    * @return Liste d'ImportTemplate.
    */
   List<ImportTemplate> findByExcludedId(Integer excludedId);

}
