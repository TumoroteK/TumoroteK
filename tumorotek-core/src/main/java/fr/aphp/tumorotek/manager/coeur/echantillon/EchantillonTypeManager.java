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
import java.util.Set;

import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;

/**
 *
 * Interface pour le manager du bean de domaine EchantillonType.
 * Interface créée le 23/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface EchantillonTypeManager extends TKThesaurusManager<EchantillonType>
{

   /**
    * Recherche un type d'échantillon dont l'identifiant est 
    * passé en paramètre.
    * @param echantillonTypeId Identifiant du type que l'on recherche.
    * @return Un EchantillonType.
    */
   @Override
   EchantillonType findByIdManager(Integer echantillonTypeId);

   /**
    * Recherche le type de l'échantillon passé en paramètre.
    * @param echantillon Echantillon pour lequel on recherche un type.
    * @return Un EchantillonType.
    */
   EchantillonType findByEchantillonManager(Echantillon echantillon);

   /**
    * Recherche les échantillons liés au type passé en paramètre.
    * @param type Type des échantillons que l'on recherche.
    * @return Liste d'échantillons.
    */
   Set<Echantillon> getEchantillonsManager(EchantillonType type);

   /**
    * Recherche tous les types d'échantillons dont la valeur commence
    * comme celle passée en paramètre.
    * @param type Type que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste de EchantillonType.
    */
   List<EchantillonType> findByTypeLikeManager(String type, boolean exactMatch);

   //	/**
   //	 * Recherche les doublons de l'EchantillonType passé en paramètre.
   //	 * @param type Un EchantillonType pour lequel on cherche des doublons.
   //	 * @return True s'il existe des doublons.
   //	 */
   //	Boolean findDoublonManager(EchantillonType type);
   //	
   //	/**
   //	 * Test si un type d'échantillons est lié à des échantillons.
   //	 * @param type EchantillonType que l'on souhaite tester.
   //	 * @return Vrai si le type est utilisé.
   //	 */
   //	Boolean isUsedObjectManager(EchantillonType type);
   //	
   //	/**
   //	 * Persist une instance d'EchantillonType dans la base de données.
   //	 * @param type Nouvelle instance de l'objet à créer.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void createObjectManager(EchantillonType type);
   //	
   //	/**
   //	 * Sauvegarde les modifications apportées à un objet persistant.
   //	 * @param type Objet à mettre à jour dans la base.
   //	 * @throws DoublonFoundException Lance une exception si un doublon de
   //	 * l'objet à créer se trouve déjà dans la base.
   //	 */
   //	void updateObjectManager(EchantillonType type);
   //	
   //	/**
   //	 * Supprime un EchantillonType de la base de données.
   //	 * @param type EchantillonType à supprimer de la base de données.
   //	 * @throws DoublonFoundException Lance une exception si l'objet
   //	 * est utilisé par des échantillons.
   //	 */
   //	void removeObjectManager(EchantillonType type);

}
