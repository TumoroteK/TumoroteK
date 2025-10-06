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
import fr.aphp.tumorotek.action.imports.ImportUtils;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.decorator.factory.EntiteDecoratorFactory;
import fr.aphp.tumorotek.manager.io.imports.modification.champannotation.ImportChampAnnotationEntiteStrategy;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.EImportTemplateType;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.systeme.EEntiteId;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

public class ModificationAnnotationObjetExistantStrategy extends AbstractImportTemplateStrategy
{
   private EContexte eContexte;
   
   //visibilité private pour obliger à instancier avec un EContexte
   private ModificationAnnotationObjetExistantStrategy() {}
   public ModificationAnnotationObjetExistantStrategy(EContexte eContexte) {
      this.eContexte = eContexte;
   }
   
   
   @Override
   public EImportTemplateType getEImportTemplateType() {
      return EImportTemplateType.MODIFICATION_ANNOTATION;
   }
   
   @Override
   public String defineKeyI18nForTitle() {
      return "importTemplate.modification.annotation.fiche.title";
   }
   
   @Override   
   public List<EntiteDecoratorForOneToManyComponent> defineAddableEntites(){
      List<EntiteDecoratorForOneToManyComponent> entitesDecorator = new ArrayList<EntiteDecoratorForOneToManyComponent>();
      entitesDecorator.add(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0)));
      entitesDecorator.add(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0)));
      entitesDecorator.add(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0)));
      entitesDecorator.add(EntiteDecoratorFactory.decorateForOneToManyComponent(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0)));

      return entitesDecorator;
   }

   /**
    * les champs obligatoires sont constitués de la clé fonctionnelle (un code), complétée éventuellement de données plus parlantes, pour contrôler 
    * dans certains cas comme pour le patient qu'il n'y a pas d'erreur au niveau du code 
    */
   @Override
   public List<ChampEntite> retrieveAllChampObligatoire(Entite entite, Banque banque) {
      ImportChampAnnotationEntiteStrategy entiteStrategy =  ManagerLocator.getImportChampAnnotationEntiteStrategyFactory().retrieveEntiteStrategy(EEntiteId.findById(entite.getEntiteId()), getEContexte());
      List<ChampEntite> result = new ArrayList<ChampEntite>();
      ChampEntite codeFonctionnel = entiteStrategy.retrieveChampForCleFonctionnelle(entite); 
      if(codeFonctionnel != null) {
       result.add(codeFonctionnel);
      }
      //ajout des éventuels champs de contrôle
      ChampEntite champEntiteForControle = entiteStrategy.retrieveChampForControle(entite);
      if(champEntiteForControle != null) {
         //si contexte Gatsbi, on vérifie que le champ est visible :
         if(eContexte == EContexte.GATSBI) {
            if(GatsbiController.isChampEntiteVisible(champEntiteForControle)) {
               result.add(champEntiteForControle);
            }
         }
         else {
            result.add(champEntiteForControle);
         }
      }
      
      return result;
   }
   
   @Override
   public List<Champ> retrieveAllForEntite(Entite entite, Banque banque) {
      return ImportUtils.retrieveAllChampAnnotationForEntite(entite, banque);
   }
   
   @Override
   public List<ImportColonneDecorator> decorateImportColonnes(List<ImportColonne> listImportColonne, Banque banque){
      //les seules colonnes obligatoires sont les colonnes associées à des champs standards (il s'agit de la clé fonctionnelle)
      //les autres sont pour des mises à jour qui ne peuvent pas être des suppressions de valeur donc pas de risque de supprimer la valeur d'un champ d'annotation obligatoire 
      List<ImportColonneDecorator> listImportColonneDecorator = new ArrayList<ImportColonneDecorator>();
      if(listImportColonne != null && banque != null) {
         for(ImportColonne importColonne : listImportColonne) {
            //champ standard
            if(importColonne.getChamp() != null && importColonne.getChamp().getChampEntite() != null){
               listImportColonneDecorator.add(ImportColonneDecoratorFactory.decorateColonneObligatoireEtNonDeplacable(importColonne, eContexte));
            }
            else { // annotation
               listImportColonneDecorator.add(new ImportColonneDecorator(importColonne, eContexte));
            }
         }
      }
      
      return listImportColonneDecorator;
   }

   //toutes les annotations à modifier doivent appartenir à la même entité => retourne 1 
   @Override
   public Integer defineNbMaxEntiteSelectionnable() {
      return 1;
   }
   
   //Dans ce cas, le code fonctionnel est en première position
   @Override
   public Integer defineNbLigneEnteteFixe() {
      return 1;
   }
   
   @Override
   public EContexte getEContexte(){
      return eContexte;
   }
   
   @Override
   public String defineNomMethodePourExecuterImport() {
      return "onLaterImportForModificationAnnotation";
   }
}
