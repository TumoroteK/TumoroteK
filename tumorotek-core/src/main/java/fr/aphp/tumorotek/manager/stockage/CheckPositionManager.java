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

import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;

public interface CheckPositionManager
{

   /**
    * Test si une position d'une Enceinte est libre. Les ids
    * en paramètre permettent d'exclure des éléments de la
    * recherche (pour l'update par ex).
    * @param enceinte Enceinte à tester.
    * @param position Position à tester.
    * @param terminaleId Id de la terminale à exclure.
    * @param enceinteId Id de l'enceinte à exclure.
    * @return True si la position est libre.
    */
   public Boolean checkPositionLibreInEnceinteManager(Enceinte enceinte, Integer position, Integer terminaleId,
      Integer enceinteId);

   /**
    * Test si une position d'un conteneur est libre. Les ids
    * en paramètre permettent d'exclure des éléments de la
    * recherche (pour l'update par ex).
    * @param conteneur Conteneur à tester.
    * @param position Position à tester.
    * @param enceinteId Id de l'enceinte à exclure.
    * @return True si la position est libre.
    */
   public Boolean checkPositionLibreInConteneurManager(Conteneur conteneur, Integer position, Integer enceinteId);

}
