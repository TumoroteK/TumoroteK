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
package fr.aphp.tumorotek.manager.coeur.prodderive;

import java.util.List;

import fr.aphp.tumorotek.manager.PfDependantTKThesaurusManager;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;

/**
 *
 * Interface pour le manager du bean de domaine ProdType.
 * Interface créée le 29/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ProdTypeManager extends PfDependantTKThesaurusManager<ProdType>
{

   /**
    * Recherche un type de produit dérivé dont l'identifiant est 
    * passé en paramètre.
    * @param prodTypeId Identifiant du type que l'on recherche.
    * @return Un ProdType.
    */
   @Override
   ProdType findByIdManager(Integer prodTypeId);

   /**
    * Recherche tous les types de produits dérivés dont la valeur commence
    * comme celle passée en paramètre.
    * @param type Type que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de ProdType.
    */
   List<ProdType> findByTypeLikeManager(String type, boolean exactMatch);

   //	/**
   //	 * Recherche les doublons du ProdType passé en paramètre.
   //	 * @param type ProdType pour lequel on cherche des doublons.
   //	 * @return True s'il existe des doublons.
   //	 */
   //	Boolean findDoublonManager(ProdType type);
   //	
   //	/**
   //	 * Test si un type de produit dérivé est lié à des 
   //	 * produits dérivés.
   //	 * @param type ProdType que l'on souhaite tester.
   //	 * @return Vrai si la qualité est utilisée.
   //	 */
   //	Boolean isUsedObjectManager(ProdType type);
   //	
   //	/**
   //	 * Persist une instance de ProdType dans la base de données.
   //	 * @param type Nouvelle instance de l'objet à créer.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void createObjectManager(ProdType type);
   //	
   //	/**
   //	 * Sauvegarde les modifications apportées à un objet persistant.
   //	 * @param type Objet à mettre à jour dans la base.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void updateObjectManager(ProdType type);
   //	
   //	/**
   //	 * Supprime un ProdType de la base de données.
   //	 * @param type EchanQualite à supprimer de la base de données.
   //	 * @throws DoublonFoundException Lance une exception si l'objet
   //	 * est utilisé par des échantillons.
   //	 */
   //	void removeObjectManager(ProdType type);

}
