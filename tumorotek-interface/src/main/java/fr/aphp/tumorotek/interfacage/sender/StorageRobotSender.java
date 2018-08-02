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
package fr.aphp.tumorotek.interfacage.sender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import fr.aphp.tumorotek.manager.interfacage.ExtMessageSender;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;

/**
 *
 * Produit et dépose un fichier CSV à destination d'un robot
 * automatisant le stockage/destockage/deplacement des échantillons
 * (Projet initié CHU Grenoble IRELEC)
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1.1
 *
 */
public interface StorageRobotSender extends ExtMessageSender
{

   /**
    * Formate les emplacements en une liste d'échantillon dans un .CSV
    * @param tkEmpls Map<TKStockableObject, Emplacement>
    * @param oType type operation stockage/destockage/deplacement
    * @param recepteur interfacages
    */
   void sendEmplacements(Recepteur re, Map<TKStockableObject, Emplacement> tkEmpls, OperationType oType);

   /**
    * Produit le CSV qui sera déposé à l'attention du robot.
    * @param baos OutputStream contenu du fichier
    * @param tkEmpls List<Emplacement> à stocker / destocker
    * @param oType Stockage/Destockage
    * @param separator utilisé pour le CSV
    * @param nbLinesToBeProvide nb de lignes vides à ajouter au csv si, si différent -1
    * @param recepteur interfacages
    * @throws IOException
    */
   void makeCSVfromMap(Recepteur re, ByteArrayOutputStream baos, Map<TKStockableObject, Emplacement> tkEmpls, OperationType oType,
      String separator, int nbLinesToBeProvide) throws IOException;

}
