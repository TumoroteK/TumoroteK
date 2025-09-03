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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.imports.ImportColonneDecorator;
import fr.aphp.tumorotek.action.imports.ImportUtils;
import fr.aphp.tumorotek.decorator.EntiteDecoratorForOneToManyComponent;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 * cette classe définit les fonctionnements par défaut qui peuvent être surchargés si besoin dans les classes filles
 * 
 * @since 2.3.1.0 (TK-538) 
 * @author chuet
 *
 */
public abstract class AbstractImportTemplateStrategy implements ImportTemplateStrategy
{

   //par défaut le bloc n'est pas visible
   @Override
   public boolean isBlocDeriveParentVisible(){
      return false;
   }

   //par défaut ne fait rien
   @Override
   public void doActionsSpecifiquesForInitNewObject(List<EntiteDecoratorForOneToManyComponent> entitesAssociees,
      ImportTemplate importTemplate, List<ImportColonneDecorator> importColonnesDecorator, Banque banque){
   }
   
   //par défaut ne fait rien
   @Override
   public void doActionsSpecifiquesBeforeCreatingOrUpdatingObject(List<ImportColonneDecorator> importColonnesDecorator){
   }

   @Override
   public boolean forbidEditableModeForEntitesAssociees(){
      return false;
   }
   
   @Override
   public List<ImportColonneDecorator> defineColonnesDecoratorForImportTemplate(ImportTemplate importTemplate) {
      List<ImportColonneDecorator> listImportColonneDecorator = new ArrayList<ImportColonneDecorator>();
      if(importTemplate != null && importTemplate.getImportTemplateId() != null) {
         List<ImportColonne> listImportColonne = new ArrayList<ImportColonne>();
         //ajout des colonnes stockées en base pour ce modèle
         listImportColonne.addAll(ManagerLocator.getImportColonneManager().findByImportTemplateManager(importTemplate));
         listImportColonneDecorator.addAll(decorateImportColonnes(listImportColonne, importTemplate.getBanque()));
      }

      return listImportColonneDecorator;
   }
   
   @Override
   public List<ChampEntite> retrieveAllChampObligatoire(Entite entite, Banque banque) {
      return GatsbiController.findByEntiteImportAndIsNullableManager(entite, true, false, banque);
   }

   @Override
   public List<Champ> retrieveAllForEntite(Entite entite, Banque banque) {
      List<Champ> listChampAjoutable = new ArrayList<Champ>();
      
      //ajout des champs standard :
      List<ChampEntite> listChampNullable = GatsbiController.findByEntiteImportAndIsNullableManager(entite, true, null, banque);
      int nbChampNullable = listChampNullable.size();
      for(int i = 0; i < nbChampNullable; i++){
         listChampAjoutable.add(new Champ(listChampNullable.get(i)));
      }
      
      //ajout des champs délégué :
      List<ChampDelegue> listChampDelegue = ManagerLocator.getChampDelegueManager()
         .findByEntiteAndContexte(entite, EContexte.valueOf(banque.getContexte().getNom()));
      int nbChampDelegue = listChampDelegue.size();
      for(int i = 0; i < nbChampDelegue; i++){
         listChampAjoutable.add(new Champ(listChampDelegue.get(i)));
      }
      
      //ajout des champs d'annotation :
      listChampAjoutable.addAll(ImportUtils.retrieveAllChampAnnotationForEntite(entite, banque));
      
      return listChampAjoutable;
   }

   @Override
   public List<ImportColonneDecorator> decorateImportColonnes(List<ImportColonne> listImportColonne, Banque banque){
      List<ImportColonneDecorator> listImportColonneDecorator = new ArrayList<ImportColonneDecorator>();
      if(listImportColonne != null && banque != null) {
         ChampEntite currentChampEntite = null;
         Integer currentEntiteId = null;
         //Contexte currentContexteGatsbi = null;
         Map<Integer, List<Integer>> mapListChampEntiteIdObligatoireByEntiteId = new HashMap<Integer, List<Integer>>();
         for(ImportColonne importColonne : listImportColonne) {
            ImportColonneDecorator importColonneDecorator = new ImportColonneDecorator(importColonne);
            //surchage éventuelle de l'attribut canDelete pour les champs obligatoires :
            //seuls les champs "standard" de l'application peuvent être obligatoire (pas les champs d'annotation) : 
            if(importColonne.getChamp() != null && importColonne.getChamp().getChampEntite() != null){
               if(getEContexte() == EContexte.GATSBI) {
                  //il faut regarder au niveau du contexte si le champ est obligatoire
                  currentChampEntite = importColonne.getChamp().getChampEntite();
                  currentEntiteId = currentChampEntite.getEntite().getEntiteId();
                  //currentContexteGatsbi = importTemplate.getBanque().getEtude().getContexteForEntite(currentEntiteId);
                  mapListChampEntiteIdObligatoireByEntiteId.computeIfAbsent(currentEntiteId, 
                     k -> ManagerLocator.getChampEntiteManager().retrieveRequiredChampEntiteIdsForGatsbiContexte(banque.getEtude().getContexteForEntite(k)));
                  importColonneDecorator.setCanDelete(mapListChampEntiteIdObligatoireByEntiteId.get(currentEntiteId).contains(currentChampEntite.getId()));
               }
               else {
                  //il faut s'appuyer sur la caractéristique nullable du champ
                  importColonneDecorator.setCanDelete(importColonne.getChamp().getChampEntite().isNullable());
               }
            }
            
            listImportColonneDecorator.add(importColonneDecorator);
         }
      }
      
      return listImportColonneDecorator;
   }
   
   abstract public EContexte getEContexte();
   
   //par défaut pas de max
   @Override
   public Integer defineNbMaxEntiteSelectionnable(){
      return null;
   }

   @Override
   public String defineNomMethodePourExecuterImport() {
      return "onLaterImport";
   }
}
