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
package fr.aphp.tumorotek.manager.io.production;

import java.io.IOException;
import java.util.List;

import fr.aphp.tumorotek.manager.io.document.DocumentWithDataAsTable;

/**
 * Interface pour la production d'un fichier à partir d'une liste d'objets de données.
 *
 * L'interface définit une méthode pour produire le fichier à partir
 * d'une liste d'objets {@link DocumentWithDataAsTable} pouvant correspondre à des feuilles excel ou à des pages
 * les éléments de sortie du traitement sont encapsulés dans un {@link DocumentProducerResult}.
 *
 *  <p>Le modèle de conception et l'architecture de cette classe ont été fournis par C.H.</p>
 */

public interface DocumentProducer {

    /**
     * Produit un fichier basé sur une liste d'objets contenant des données sous forme
     * de tableau et écrit le résultat dans un flux de sortie spécifié.
     *
     * @param listDocumentWithDataAsTable La liste des objets qui contiennent les données à inclure dans le document.
     * @return DocumentProducerResult Le résultat produit par cette opération
     *
     * @throws IOException Si une erreur se produit lors de l'écriture du document dans le flux de sortie.
     */

    DocumentProducerResult produce(List<DocumentWithDataAsTable> listDocumentWithDataAsTable) throws IOException;
}
