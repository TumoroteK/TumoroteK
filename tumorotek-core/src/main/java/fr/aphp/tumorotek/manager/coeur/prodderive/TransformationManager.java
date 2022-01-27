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

import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Interface pour le manager du bean de domaine Transformation.
 * Interface créée le 30/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface TransformationManager
{

   /**
    * Recherche une transformation dont l'identifiant est passé en paramètre.
    * @param transformationId Identifiant de la transformation que l'on 
    * recherche.
    * @return Une Transformation.
    */
   Transformation findByIdManager(Integer transformationId);

   /**
    * Recherche toutes les transformations présentes dans la base.
    * @return Liste de Transformation.
    */
   List<Transformation> findAllObjectsManager();

   /**
    * Recherche la transformation qui est issue d'une entité et d'un ojet.
    * @param entite Entite dont est issue la transformation
    * @param objetId Identifiant de l'objet dont est issue la transformation.
    * @return une liste de transformation.
    */
   List<Transformation> findByEntiteObjetManager(Entite entite, Integer objetId);

   /**
    * Recherche une transformation en fonction de son objet parent.
    * @param parent Objet dont est issue la transformation
    * @return une liste de transformation.
    */
   List<Transformation> findByParentManager(Object parent);

   /**
    * Recherche les doublons de la Transformation passé en paramètre.
    * @param transformation Transformation pour laquelle on cherche 
    * des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Transformation transformation);

   //	/**
   //	 * Test si une transformation est liée à des produits dérivés.
   //	 * @param transformation Transformation que l'on souhaite tester.
   //	 * @return Vrai si la qualité est utilisée.
   //	 */
   //	Boolean isUsedObjectManager(Transformation transformation);

   /**
    * Persist une instance de Transformation dans la base de données.
    * @param transformation Nouvelle instance de l'objet à créer.
    * @param entite Entite parente de la transformation.
    * @param quantiteUnite Unite de la quatité.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   void saveManager(Transformation transformation, Entite entite, Unite quantiteUnite);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param qualite Objet à mettre à jour dans la base.
    * @param entite Entite parente de la transformation.
    * @param quantiteUnite Unite de la quatité.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   void saveManager(Transformation transformation, Entite entite, Unite quantiteUnite);

   /**
    * Supprime une Transformation de la base de données. Cascade 
    * automatiquement la deletion vers les derives.
    * @param transformation Transformation à supprimer de la base 
    * de données.
    * @param comments commentaires liés à la suppression
    * @param Utilisateur réalisant la suppression.
    */
   void deleteByIdManager(Transformation transformation, String comments, Utilisateur user);

   /**
    * Recherche tous les derives, issus de une ou plusieurs
    * transformation d'un objet Parent. Cherche par recursivite
    * les derives de derives.
    * @param parent Objet
    * @return une liste de derives.
    */
   List<ProdDerive> findAllDeriveFromParentManager(Object parent);
}
