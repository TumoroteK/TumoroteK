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
import java.util.List;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovement;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.manager.interfacage.ExtMessageSender;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

/**
 *
 * Produit et dépose un fichier CSV à destination d'un robot
 * automatisant le stockage/destockage/deplacement des échantillons
 * (Projet initié CHU Grenoble IRELEC)
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 *
 */
public interface StorageRobotSender extends ExtMessageSender
{

	/**
	 * Formate les emplacements/mouvements en une liste d'échantillon dans un .CSV
	 * @param mvts dans le système de stockage robotisé
	 * @param recepteur interfacages
	 * @param utilisateur Utilisateur
	 */
	void sendEmplacements(Recepteur re, List<StorageMovement> storageMvts, 
											Utilisateur u);

	/**
	 * Produit le CSV qui sera déposé à l'attention du robot.
	 * @param baos OutputStream contenu du fichier
	 * @param mvts dans le système de stockage robotisé

	 * @param separator utilisé pour le CSV
	 * @param recepteur interfacages
	 * @throws IOException
	 */
	void makeCSVfromMap(Recepteur re, ByteArrayOutputStream baos, 
			List<StorageMovement> stoE,
			String separator) throws IOException;

	/**
	 * Ecrit une ligne dans le fichier NomRecette.csv
	 * @param baos OutputStream contenu du fichier
	 * @param filename nom fichier Recette à écrire
	 * @param user login à écrire
	 * @param separator utilisé pour le CSV
	 * @throws IOException
	 */
	void writeOneRecetteLine(ByteArrayOutputStream baos, String filename, Utilisateur user, String separator)
			throws IOException;

}