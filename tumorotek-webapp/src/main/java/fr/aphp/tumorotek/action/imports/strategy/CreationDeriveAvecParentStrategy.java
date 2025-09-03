/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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

package fr.aphp.tumorotek.action.imports.strategy;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.imports.ImportColonneDecorator;
import fr.aphp.tumorotek.action.imports.ImportColonneDecoratorFactory;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.decorator.factory.EntiteDecoratorFactory;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateType;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 * stratégie liée à l'import de produits dérivés avec rattachement à un parent 
 * 
 * @since 2.3.1.0 (TK-538) 
 * @author chuet
 *
 */
public class CreationDeriveAvecParentStrategy extends AbstractImportTemplateStrategy
{
   private EContexte eContexte;
   
   //visibilité private pour obliger à instancier avec un EContexte
   private CreationDeriveAvecParentStrategy() {}
   public CreationDeriveAvecParentStrategy(EContexte eContexte) {
      this.eContexte = eContexte;
   }
   
   @Override
   public EImportTemplateType getEImportTemplateType() {
      return EImportTemplateType.CREATION_DERIVE;
   }
   
   @Override
   public String defineKeyI18nForTitle() {
      return "importTemplate.subderive.fiche.title";
   }

   @Override   
   public List<EntiteDecoratorForOneToManyComponent> defineAddableEntites(){
      List<EntiteDecoratorForOneToManyComponent> entitesDecorator = new ArrayList<EntiteDecoratorForOneToManyComponent>();
      entitesDecorator.add(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0)));

      return entitesDecorator;
   }
   
   @Override     
   public boolean isBlocDeriveParentVisible() {
      return true;
   }
   
   //force l'entité associée à "produit dérivé" et initialise le parent du produit dérivé en cours de création à la valeur "Produit dérivé"
   @Override
   public void doActionsSpecifiquesForInitNewObject(List<EntiteDecoratorForOneToManyComponent> entitesAssociees, ImportTemplate importTemplate, List<ImportColonneDecorator> importColonnesDecorator, Banque banque){
      
      Entite entiteDerive = ManagerLocator.getEntiteManager().findByIdManager(8);
      //associe l'entité dérivé :
      entitesAssociees.add(EntiteDecoratorFactory.decorateForOneToManyComponent(entiteDerive));
      //par défaut, le parent est un dérivé : l'utilisateur pourra modifier la valeur
      importTemplate.setDeriveParentEntite(entiteDerive);
      
      ///
      if(importColonnesDecorator == null) {
         importColonnesDecorator = new ArrayList<ImportColonneDecorator>();
      }
      //ajout des colonnes liées au parent du dérivé (spécificité de cette strategy) :
      importColonnesDecorator.addAll(makeSubderiveHeaderCols(importTemplate));
      
      //comme l'entité est forcée au produit dérivé, on construit les importColonnesDecorator des champs obligatoires pour cette entité :
      //ces colonnes seront déplaçables (cas par défaut) :
      final List<ChampEntite> champsEntite = GatsbiController.findByEntiteImportAndIsNullableManager(entiteDerive, true, false, banque);

      for(ChampEntite champEntite : champsEntite) {
         final ImportColonne importColonne = new ImportColonne();
         importColonne.setImportTemplate(importTemplate);
         importColonne.setChamp(new Champ(champEntite));
         importColonne.setImportTemplate(importTemplate);
         importColonnesDecorator.add(ImportColonneDecoratorFactory.decorateColonneObligatoire(importColonne, eContexte));
      }
   }
   
   //Les 3 colonnes liées au parent ne sont pas stockées en base de données (ça complique les choses :-( )
   @Override
   public void doActionsSpecifiquesBeforeCreatingOrUpdatingObject(List<ImportColonneDecorator> importColonnesDecorator){
      importColonnesDecorator.remove(0);
      importColonnesDecorator.remove(0);
      importColonnesDecorator.remove(0);
   }
   
   //avant les éléments récupérés par la méthode standard, on ajoute les colonnes liées au parent
   @Override
   public List<ImportColonneDecorator> defineColonnesDecoratorForImportTemplate(ImportTemplate importTemplate) {
      List<ImportColonneDecorator> listImportColonneDecorator = new ArrayList<ImportColonneDecorator>();
      if(importTemplate != null && importTemplate.getImportTemplateId() != null) {
         //les colonnes qui font le lien avec le parent du dérivé ne sont pas stockées dans le modèle : il faut donc les ajouter ....
         listImportColonneDecorator.addAll(makeSubderiveHeaderCols(importTemplate));

         //ajout des colonnes stockées en base pour ce modèle
         listImportColonneDecorator.addAll(super.defineColonnesDecoratorForImportTemplate(importTemplate));
      }
      
      return listImportColonneDecorator;
   }
   
   
   @Override
   public boolean forbidEditableModeForEntitesAssociees() {
      return true;
   }
   
   // Cette méthode n'est pas appelée dans le cas de ce modèle car l'utilisateur ne sélectionne pas l'entité (pas d'appel de FicheImportTemplate.onGetAddesObject(Event)).
   // L'entité est forcée à l'initialisation
   // Par conséquent on se contente de la méthode standard mais il faut avoir à l'esprit que les 3 champs "fictifs" utilisés dans ce modèle pour gérer les données
   // du parent du dérivé ne sont pas gérés par cette méthode
   @Override
   public List<ChampEntite> retrieveAllChampObligatoire(Entite entite, Banque banque) {
      return super.retrieveAllChampObligatoire(entite, banque);
   }
   
   //suele l'entité dérivé peut être sélectionnée => retourne 1
   @Override
   public Integer defineNbMaxEntiteSelectionnable() {
      return 1;
   }
   
   //dans ce cas, retourne 4 : les 3 concernant le parent plus le code du dérivé à créer
   @Override
   public Integer defineNbLigneEnteteFixe() {
      return 4;
   }
   
   private List<ImportColonneDecorator> makeSubderiveHeaderCols(final ImportTemplate it){//spécifiqur subderive : init
      final int UNDEFINED_ORDRE = 0;
      final List<ImportColonneDecorator> listColonneDecorator = new ArrayList<>();
      final ImportColonne colonneCodeParent = new ImportColonne();
      colonneCodeParent.setNom("code.parent");
      colonneCodeParent.setOrdre(UNDEFINED_ORDRE);
      colonneCodeParent.setImportTemplate(it);
      listColonneDecorator.add(ImportColonneDecoratorFactory.decorateColonneObligatoireNonDeplacableAuLibelleNonEditable(colonneCodeParent, eContexte));
      final ImportColonne colonneQuantiteTransformation = new ImportColonne();
      colonneQuantiteTransformation.setNom("qte.transf");
      colonneQuantiteTransformation.setOrdre(UNDEFINED_ORDRE);
      colonneQuantiteTransformation.setImportTemplate(it);
      listColonneDecorator.add(ImportColonneDecoratorFactory.decorateColonneObligatoireNonDeplacableAuLibelleNonEditable(colonneQuantiteTransformation, eContexte));
      final ImportColonne colonneDateEvenement = new ImportColonne();
      colonneDateEvenement.setNom("evt.date");
      colonneDateEvenement.setOrdre(UNDEFINED_ORDRE);
      colonneDateEvenement.setImportTemplate(it);
      listColonneDecorator.add(ImportColonneDecoratorFactory.decorateColonneObligatoireNonDeplacableAuLibelleNonEditable(colonneDateEvenement, eContexte));
      
      return listColonneDecorator;
   }

   @Override
   public EContexte getEContexte(){
      return eContexte;
   }   
}
