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
package fr.aphp.tumorotek.manager.io.export.standard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Super-interface définissant la methode de creation d'une ligne
 * de fichier tabulé d'export et la methode de generation du
 * fichier Excel d'export.
 * Date: 27/09/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface ExportCatalogueManager
{

   /**
    * Pour un echantillon, recupere toutes les informations 
    * spécifiées par le catalogue sous la forme d'une table de 
    * hachage item-resultat.
    * Renvoie une table avec une seule entrée si une erreur se 
    * produit lors de la récupération des données.
    * @param echan
    * @param connection database
    * @return Map<String, String>
    */
   Map<String, String> objetExport(Echantillon echan, Connection con);

   /**
    * Créee le fichier Excel contenant l'export vers un catalogue 
    * à partir d'une liste d'échantillons.
    * @param echans
    * @param utilisateur réalisant l'export
    * @param catalogue TVGSO, TVN, BIOCAP...
    */
   void exportEchansCatalogueManager(List<Echantillon> echans, Utilisateur utilisateur, Catalogue catalogue) throws SQLException;

   /**
    * Prepare les objets nécessaires à la procédure d'export.
    * @throws SQLException
    */
   void initExportObjectsManager();

   /**
    * Exporte les données d'un echantillon sous la forme d'une ligne 
    * dans le fichier Excel passé en paramètre.
    * Enregistre l'opération d'export pour l'échantillon.
    * @param conn
    * @param obj qui va recevoir la ligne ie HSSFSheet ou BufferedWriter
    * @param nbRow
    * @param echan
    * @param catalogue
    * @param utilisateur
    */
   void addExportDataIntoRow(Connection conn, Object obj, int nbRow, Echantillon echan, Catalogue catalogue,
      Utilisateur utilisateur);

   /**
    * Renvoie les headers du fichier d'export Catalogue.
    * @return headers sous la forme d'une liste.
    */
   List<String> getHeaders();

   /**
    * Methode qui imprime une ligne de sortie par échantillon. 
    * @param obj qui va recevoir la ligne ie HSSFSheet ou BufferedWriter
    * @param index colonne à laquelle débuter
    * @param nbRow ligne à laquelle ecrire (HSSFSheet uniquement)
    * @param values à exporter
    */
   void printDataIntoRow(Object obj, int index, int nbRow, List<String> values);
}
