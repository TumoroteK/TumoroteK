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

package fr.aphp.tumorotek.action.recherche;

import org.zkoss.zk.ui.event.Event;

/**
 * cette interface permet de définir les méthodes correspondant aux évènements liés aux actions possibles dans la modale
 * ResultatsModale si la recherche ramène plus de 500 éléments.
 * Cela permet d'éviter d'introduire une regression en cas de refactoring car ces méthodes peuvent être appelées sur des écrans de type RechercheAvancee
 * (cas standard de l'affichage des 500 premiers de la recherche par exemple) ou sur des écrans de liste (cas de l'affichage des objets associés au 
 * résultat de la recherche).
 * Or ces 2 types d'écran n'héritaient pas jusqu'à présent de la même classe donc en cas de modification une méthode pouvait être définie uniquement 
 * dans AbstractFicheRechercheAvancee ou respectivement dans AbstractListeController2 ce qui pouvait générer une regression sur l'autre cas d'usage 
 * (cf tickets TK-875 et TK-876)
 */
public interface ResultatsModaleEvent
{
   void onShowResultsFromResultatsIds();
   
   /**
    * Evenement relayant l'envoi vers une nouvelle cession 
    * d'un trop grand nombre de résultats (envoyé depuis ResultatsModale)
    */
   void onDoNewCession();
   
   void onDoBatchDelete();
   
   /**
    * Evenement relayant l'export d'un trop grand nombre de
    * résultats (envoyé depuis ResultatsModale)
    * @version 2.1
    */
   void onDoExport(final Event e);
}
