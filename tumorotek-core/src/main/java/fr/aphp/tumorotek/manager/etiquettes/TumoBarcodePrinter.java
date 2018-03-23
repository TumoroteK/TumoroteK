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
package fr.aphp.tumorotek.manager.etiquettes;

import java.awt.print.PrinterException;
import java.util.List;

import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

/**
 * Modifiée par Mathieu BARTHELEMY pour ajouter méthode print générique.
 * Date: 07/04/2013
 *
 * @author teamtumo Interface pour les imprimantes utilisees par Tumo.
 * @since 2.0.10
 */
public interface TumoBarcodePrinter
{

   //	int printPatient(List<Patient> patients, int nb);
   //
   //	int printPrelevement(List<Prelevement> prelevement, int nb);
   //
   //	int printEchantillon(List<Echantillon> echantillons, int nb,
   //			Imprimante imprimante, Modele modele, String rawLang) throws PrinterException;
   //
   //	int printDerive(List<ProdDerive> derives, int nb, Imprimante imprimante,
   //			Modele modele, String rawLang) throws PrinterException;
   //
   //	int printCession(List<Cession> cessions, int nb);
   //	/**
   //	 * Lance l'impression d'un certain nb de copies d'etiquettes 
   //	 * codes a barres à partir d'une liste de valeurs.
   //	 * @param l liste de valeurs à imprimer (lignes)
   //	 * @param nb copies de l'étiquette
   //	 * @param imprimante cible
   //	 * @param modele d'impression TK
   //	 * @param raw printing language
   //	 * @return -1 en cas d'erreur
   //	 * @throws PrinterException
   //	 */
   //	int printData(List<LigneEtiquette> l, int nb, Imprimante imprimante,
   //			Modele modele, String rawLang) throws PrinterException;
   //	
   //
   //	/**
   //	 * Fonction qui lance l'impression de plusieurs etiquettes codes a barres 
   //	 * d'une à partir d'une liste de liste de valeurs.
   //	 * @param dataListeEtiquette liste de listes de lignes
   //	 * @param imprimante cible
   //	 * @param modele d'impression TK
   //	 * @param raw printing language
   //	 * @return -1 en cas d'erreurs
   //	 * @throws PrinterException
   //	 */
   //	int printListData(List<List<LigneEtiquette>> dataListeEtiquette,
   //			Imprimante imprimante, Modele modele, String rawLang) 
   //					throws PrinterException;

   /**
    * Méthode d'entrée pour impression d'échantillou ou de produits dérivés.
    * Cette méthode dispatche vers les méthodes printEchantillon et printDeribe 
    * qui ne devrait pas êtré différenciée
    * @param objects
    * @param i
    * @param imp
    * @param mod
    * @param raw printing language
    * @param BarcodeFieldDefault modifie parametres dessin codes-barres
    * @return int success si 0
    * @throws PrinterException 
    */
   int printStockableObjects(List<? extends TKStockableObject> objects, int i, Imprimante imp, Modele mod, String rawLang,
      BarcodeFieldDefault by) throws PrinterException;

   /**
    * Fonction qui effectue l'impression d'un certain nombre de copies d'etiquettes 
    * codes a barres d'une à partir d'une liste de liste de parametres.
    * @param l liste de listes de lignes (1 liste de ligne = 1 image étiquette)
    * @param nb copies de chaque image étiquette
    * @param imprimante cible
    * @param modele d'impression TK
    * @param raw printing language
    * @param BarcodeFieldDefault modifie parametres dessin codes-barres
    * @return -1 si erreur
    * @throws PrinterException
    */
   int printListCopiesData(List<List<LigneEtiquette>> l, int nb, Imprimante imprimante, Modele modele, String rawLang,
      BarcodeFieldDefault by) throws PrinterException;
}
