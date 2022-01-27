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

import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine ImportHistorique.
 * Interface créée le 09/02/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0.10.3
 *
 */
public interface ImportHistoriqueManager
{

   /**
    * Recherche un ImportHistorique dont l'identifiant est passé en paramètre.
    * @param importHistoriqueId Id de l'objet que l'on recherche.
    * @return Un ImportHistorique.
    */
   ImportHistorique findByIdManager(Integer importHistoriqueId);

   /**
    * Recherche tous les ImportHistoriques présents dans la base.
    * @return Liste de ImportHistoriques.
    */
   List<ImportHistorique> findAllObjectsManager();

   /**
    * Recherche un ImportHistorique dont le la banque est passée en paramètre.
    * @param importTemplate Template de l'objet que l'on recherche.
    * @return Une liste d'ImportHistoriques.
    */
   List<ImportHistorique> findByTemplateWithOrderManager(ImportTemplate importTemplate);

   /**
    * Recherche les Importations d'un ImportHistorique.
    * @param importHistorique ImportHistorique.
    * @return Liste d'Importation.
    */
   List<Importation> findImportationsByHistoriqueManager(ImportHistorique importHistorique);

   /**
    * Recherche les Importations d'un ImportHistorique et d'une Entité.
    * @param importHistorique ImportHistorique.
    * @param entite Entite.
    * @return Liste d'Importation.
    */
   List<Importation> findImportationsByHistoriqueAndEntiteManager(ImportHistorique importHistorique, Entite entite);

   /**
    * Recherche les Importations d'une Entité pour un id.
    * @param entite Entite.
    * @param objetId Identifiant.
    * @return Liste d'Importation.
    */
   List<Importation> findImportationsByObjectManager(Object object);

   /**
    * Recherche les Importations d'une Entité pour un id.
    * @param entite Entite.
    * @param objetId Identifiant.
    * @return Liste d'Importation.
    */
   List<Importation> findImportationsByEntiteAndObjectIdManager(Entite entite, Integer objetId);

   /**
    * Persist une instance d'ImportHistorique dans la base de données.
    * @param ImportHistorique Nouvelle instance de l'objet à créer.
    * @param importTemplate ImportTemplate de l'historique.
    * @param utilisateur Utilisateur.
    * @param importations Importations réalisées lors de l'historique.
    */
   void saveManager(ImportHistorique importHistorique, ImportTemplate importTemplate, Utilisateur utilisateur,
      List<Importation> importations);

   /**
    * Supprime un ImportHistorique de la base de données.
    * @param importHistorique Objet à supprimer de la base de données.
    */
   void deleteByIdManager(ImportHistorique importHistorique);

   /**
    * Supprime une Importation de la base de données.
    * @param importation Objet à supprimer de la base de données.
    */
   void removeImportationManager(Importation importation);

   /**
    * Renvoies les prélèvements importés lors de l'opération représentée 
    * par l'historique.
    * @param ih1 ImportHistorique
    * @return liste Prelevement
    * @since 2.0.10.3
    */
   List<Prelevement> findPrelevementByImportHistoriqueManager(ImportHistorique ih);

}
