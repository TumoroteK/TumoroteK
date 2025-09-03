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
package fr.aphp.tumorotek.action.imports;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.decorator.factory.EntiteDecoratorFactory;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * cette classe permet de gérer les spécificités liées à l'entité associée Maladie dans le cas des imports.
 * 
 * @since 2.3.1.0 (TK-538)
 * @author chuet
 *
 */
public class MaladieSpecificiteForEntitesAssocieesImport
{
   /**
    * l'entité Maladie ne peut pas être supprimée donc le bouton doit toujours être caché dans ce cas
    * @param entiteesAssocieesDecorators
    * @param gatsbi
    * @param pathToRespond
    */
   public static void manageVisibilityOfBoutonDelete(List<EntiteDecoratorForOneToManyComponent> entiteesAssocieesDecorators) {
      if(entiteesAssocieesDecorators != null) {
         for(EntiteDecoratorForOneToManyComponent entiteDecorator : entiteesAssocieesDecorators) {
            if(entiteDecorator.getEntite().getEntiteId() == EEntiteId.MALADIE.getId()) {
               entiteDecorator.setBoutonDeleteVisible(false);
            }
         }
      }
   }
   
   /**
    * gère le cas où l'entité prélèvement ou l'entité patient est ajoutée de la liste des entités associées pour l'import. 
    * si les entités passées en paramètre contiennent les entités Patient et Prélèvement mais pas Maladie, Maladie est ajoutée et est non supprimable
    * @param entiteesAssocieesDecorators
    * @param gatsbi
    * @return
    */
   public static void manageAdding(List<EntiteDecoratorForOneToManyComponent> entiteesAssocieesDecorators, boolean gatsbi, String pathToRespond) {
      Entite entiteMaladie = ManagerLocator.getEntiteManager().findByIdManager(EEntiteId.MALADIE.getId());
      //création du décorator avec le bouton delete non visible
      EntiteDecoratorForOneToManyComponent entiteMaladieDecorator = EntiteDecoratorFactory.decorateForOneToManyComponent(entiteMaladie, gatsbi, false);
      if(entiteesAssocieesDecorators.contains(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByIdManager(EEntiteId.PATIENT.getId())))
         && entiteesAssocieesDecorators.contains(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByIdManager(EEntiteId.PRELEVEMENT.getId())))
         && !entiteesAssocieesDecorators.stream().map(decorator -> decorator.getEntite()).collect(Collectors.toList()).contains(entiteMaladie)){
            ImportUtils.addEntiteDecoratorToEntitesAssociees(entiteMaladieDecorator, entiteesAssocieesDecorators, pathToRespond);
      }
   }
   
   /**
    * gère le cas où l'entité prélèvement ou l'entité patient est supprimée de la liste des entités associées pour l'import. 
    * Si l'entité maladie est présente dans la liste, il faut la supprimer
    * @param entiteDecoratorToDelete
    * @param entiteesAssocieesDecorators
    */
   public static void manageDeleting(EntiteDecoratorForOneToManyComponent entiteDecoratorToDelete, List<EntiteDecoratorForOneToManyComponent> entiteesAssocieesDecorators, String pathToRespond) {
      Entite entiteMaladie = ManagerLocator.getEntiteManager().findByIdManager(EEntiteId.MALADIE.getId());
      if(entiteDecoratorToDelete.getEntite().getEntiteId() == EEntiteId.PRELEVEMENT.getId() || entiteDecoratorToDelete.getEntite().getEntiteId() == EEntiteId.PATIENT.getId()) {
            Optional<EntiteDecoratorForOneToManyComponent> optionalMaladieDecorator = entiteesAssocieesDecorators.stream().filter(decorator -> decorator.getEntite().equals(entiteMaladie)).findFirst();
            if(optionalMaladieDecorator.isPresent()) {
               ImportUtils.removeEntiteDecoratorFromEntitesAssociees(optionalMaladieDecorator.get(), entiteesAssocieesDecorators, pathToRespond);
            }
      }
   }
   
   
}
