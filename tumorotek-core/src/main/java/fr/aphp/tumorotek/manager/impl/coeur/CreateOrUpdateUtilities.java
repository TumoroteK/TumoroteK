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
package fr.aphp.tumorotek.manager.impl.coeur;

import java.util.List;

import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

public final class CreateOrUpdateUtilities
{

   private CreateOrUpdateUtilities(){}

   /**
    * Enregistre automatiquement les operations de creation/modification pour
    * l'objet specifie.
    * @param obj 
    * @param manager
    * @param oType
    * @param utilisateur
    */
   public static void createAssociateOperation(final Object obj, final OperationManager manager, final OperationType oType,
      final Utilisateur utilisateur){
      //Enregistrement de l'operation associee
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      manager.saveManager(creationOp, utilisateur, oType, obj);
   }

   /**
    * Supprime toutes les operations associees a l'objet specifie.
    * @param obj
    * @param manager
    */
   public static void removeAssociateOperations(final Object obj, final OperationManager manager, final String comments,
      final Utilisateur user){

      final List<Operation> ops = manager.findByObjectManager(obj);
      for(int i = 0; i < ops.size(); i++){
         manager.deleteByIdManager(ops.get(i));
      }

      if(obj instanceof TKFantomableObject){
         manager.createPhantomManager((TKFantomableObject) obj, comments, user);
      }
   }

   /**
    * Supprime toutes les operations associees a l'objet specifie.
    * @param obj
    * @param manager
    */
   public static void removeAssociateOperations(final Object obj, final OperationManager manager){
      final List<Operation> ops = manager.findByObjectManager(obj);
      for(int i = 0; i < ops.size(); i++){
         manager.deleteByIdManager(ops.get(i));
      }
   }

   /**
    * Supprime toutes les importations associees a l'objet specifie.
    * @param obj
    * @param manager
    */
   public static void removeAssociateImportations(final Object obj, final ImportHistoriqueManager manager){

      final List<Importation> ops = manager.findImportationsByObjectManager(obj);
      for(int i = 0; i < ops.size(); i++){
         manager.removeImportationManager(ops.get(i));
      }
   }

   /**
    * Supprime toutes les non conformites associees a l'objet specifie.
    * @param obj
    * @param manager
    */
   public static void removeAssociateNonConformites(final TKAnnotableObject obj, final ObjetNonConformeManager manager){

      final List<ObjetNonConforme> ncfs = manager.findByObjetManager(obj);
      for(int i = 0; i < ncfs.size(); i++){
         manager.deleteByIdManager(ncfs.get(i));
      }
   }
}
