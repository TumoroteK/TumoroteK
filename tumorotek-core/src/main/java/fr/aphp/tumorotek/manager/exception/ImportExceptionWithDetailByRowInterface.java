/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.exception;

import java.util.List;

import fr.aphp.tumorotek.manager.exception.uimessage.UIMessageForRowInterface;

/**
 * interface définie pour gérer les spécificités liées aux exceptions remontant à l'utilisateur un détail 
 * par ligne du fiche importé
 *  
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public interface ImportExceptionWithDetailByRowInterface
{
   /**
    * retourne une liste des messages à afficher pour les lignes ayant une erreur
    * la liste est triée par numéro de ligne 
    * @return une liste de UIMessageForRowInterface correspondant à un ou plusieurs messages internationalisés à utiliser pour chaque ligne en erreur
    */
   List<UIMessageForRowInterface> buildSortedUIMessageForRow();
   
   /**
   * dans certains cas, l'import a pu être traité partiellement : cette méthode permet de récupérer le nombre de lignes importées (il s'agit toujours des premières)
   * cela permettra d'indiquer "Import Fait" dans le fichier de correction pour les lignes traitées
   * pour les exceptions lancées par des traitements de contrôle des prérequis, cette méthode renverra 0
   */
   int getNbImportedRows();
   

}
