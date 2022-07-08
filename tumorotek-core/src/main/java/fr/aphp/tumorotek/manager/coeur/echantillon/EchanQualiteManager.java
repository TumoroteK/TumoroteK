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
package fr.aphp.tumorotek.manager.coeur.echantillon;

import java.util.List;

import fr.aphp.tumorotek.manager.PfDependantTKThesaurusManager;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;

/**
 *
 * Interface pour le manager du bean de domaine EchanQualite.
 * Interface créée le 24/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EchanQualiteManager extends PfDependantTKThesaurusManager<EchanQualite>
{

   /**
    * Recherche une qualité d'échantillon dont l'identifiant est
    * passé en paramètre.
    * @param echanQualiteId Identifiant de la qualité que l'on recherche.
    * @return Une EchanQualite.
    */
   @Override
   EchanQualite findByIdManager(Integer echanQualiteId);

   /**
    * Recherche toutes les qualités d'échantillons dont la valeur commence
    * comme celle passée en paramètre.
    * @param qualite Qualité que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de EchanQualite.
    */
   List<EchanQualite> findByQualiteLikeManager(String qualite, boolean exactMatch);

   //	/**
   //	 * Recherche les doublons de l'EchanQualite passée en paramètre.
   //	 * @param qualite Une EchanQualite pour laquelle on cherche des doublons.
   //	 * @return True s'il existe des doublons.
   //	 */
   //	Boolean findDoublonManager(EchanQualite qualite);
   //
   //	/**
   //	 * Test si une qualité d'échantillons est liée à des échantillons.
   //	 * @param qualite EchanQualite que l'on souhaite tester.
   //	 * @return Vrai si la qualité est utilisée.
   //	 */
   //	Boolean isUsedObjectManager(EchanQualite qualite);
   //
   //	/**
   //	 * Persist une instance d'EchanQualite dans la base de données.
   //	 * @param qualite Nouvelle instance de l'objet à créer.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void createObjectManager(EchanQualite qualite);
   //
   //	/**
   //	 * Sauvegarde les modifications apportées à un objet persistant.
   //	 * @param qualite Objet à mettre à jour dans la base.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void updateObjectManager(EchanQualite qualite);
   //
   //	/**
   //	 * Supprime une EchanQualite de la base de données.
   //	 * @param qualite EchanQualite à supprimer de la base de données.
   //	 * @throws DoublonFoundException Lance une exception si l'objet
   //	 * est utilisé par des échantillons.
   //	 */
   //	void removeObjectManager(EchanQualite qualite);

}
