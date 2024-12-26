/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
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
package fr.aphp.tumorotek.manager.io.imports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EChampSupprimePourSerologie;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.utils.ItemForErrorResult;

/**
 * classe qui gère tous les contrôles permettant de savoir si un modèle d'import (partagé) est compatible avec une banque donnée
 * @author chuet
 *
 */
public class CompatibiliteEntreImportTemplateEtBanqueValidator
{

   private final Logger log = LoggerFactory.getLogger(CompatibiliteEntreImportTemplateEtBanqueValidator.class);
   
   private static final Integer MAP_KEY__CHAMP_DELEGUE = 1;
   private static final Integer MAP_KEY__CHAMP = 2;
  
   private static final String I18N_KEY__ANNOTATION_ERROR = "import.modele.incoherence.annotation.error";
   private static final String I18N_KEY__ANNOTATION_TODO = "import.modele.incoherence.annotation.todo";
   private static final String I18N_KEY__CHAMP_DELEGUE_ERROR = "import.modele.incoherence.champDelegue.error";
   private static final String I18N_KEY__CHAMP_DELEGUE_TODO = "import.modele.incoherence.champDelegue.todo";
   private static final String I18N_KEY__CHAMP_ERROR = "import.modele.incoherence.champ.error";
   private static final String I18N_KEY__CHAMP_TODO = "import.modele.incoherence.champ.todo";
   private static final String I18N_KEY__CHAMP_TODO2 = "import.modele.incoherence.champ.todo2";
   private static final String I18N_KEY__CHAMP_OBLIGATOIRE_ERROR = "import.modele.incoherence.champObligatoire.error";
   private static final String I18N_KEY__CHAMP_OBLIGATOIRE_TODO = "import.modele.incoherence.champObligatoire.todo";
   
   private ImportColonneManager importColonneManager;
   private TableAnnotationManager tableAnnotationManager;
   private ChampEntiteManager champEntiteManager;
   
   //Sauvegarde du lien champs / importColonne dans une map :
   // - clé type du champ 
   // - valeur : une map : 
   //              - clé id du champ
   //              - valeur : importColonne
   Map<Integer, Map<Integer, ImportColonne>> mapLienChampEtImportColonne = new HashMap<Integer, Map<Integer,ImportColonne>>();

   public void setImportColonneManager(ImportColonneManager importColonneManager){
      this.importColonneManager = importColonneManager;
   }
   public void setTableAnnotationManager(TableAnnotationManager tableAnnotationManager){
      this.tableAnnotationManager = tableAnnotationManager;
   }
   public void setChampEntiteManager(ChampEntiteManager champEntiteManager){
      this.champEntiteManager = champEntiteManager;
   }
   
   /**
    * vérifie que le modèle passé en paramètre peut être exécuté sur la banque passée en paramètre.
    * Pour cela, analyse les annotations, les champs délégués (cas d'un modèle d'une banque de contexte sérologie), les champs visibles pour un modèle Gatsbi
    * @param importTemplate
    * @param banque
    * @return null si le modèle est compatible avec la banque
    */
   public IncompatibiliteEntreImportTemplateEtBanqueResult validate(ImportTemplate importTemplate, Banque banque) {
      //Normalement, on n'appelle pas 2 fois de suite la méthode valide sur le même objet. Mais par sécurité, on nettoie tout de même
      mapLienChampEtImportColonne.clear();
      
      log.debug("CompatibiliteEntreImportTemplateEtBanqueValidator.validate()");
      if(importTemplate != null && banque != null) {
         log.debug("validation de la compatibilité entre le modèle {} et la banque {}", importTemplate.getNom(), banque.getNom());
         IncompatibiliteEntreImportTemplateEtBanqueResult result = null;
         
         //Récupère les colonnes de l'import, les trie par "type" de champ et garde le lien entre "champId" et importColonne
         log.debug("Récupération des colonnes de l'import et trie par 'type' de champ");
         List<ImportColonne> listImportColonne = importColonneManager.findByImportTemplateManager(importTemplate);
         ColonneSortingResult colonneSortingResult = sortImportColonneByType(listImportColonne);
         log.debug("nombre de colonnes d'import trouvées : {}", listImportColonne.size());
         
         //---------------- validations :
         log.debug("début des validations");
         //champs d'annotations :
         log.debug("validation des champs d'annotation");
         InfoErrorColonneAnnotation infoErrorColonneAnnotation = validateTableAnnotation(colonneSortingResult.getInfoColonneAnnotation(), banque);
         
         //contrôles liés aux champs délégués présents dans le modèle :
         log.debug("validation des champs délégués");
         ItemForErrorResult<ImportColonne> itemChampDelegue = validateChampDelegue(colonneSortingResult.getListChampDelegueForTemplate(), banque);
         
         //Autres contrôles liés aux particularités du contexte de banque passée en paramètre :
         log.debug("contrôles dépendant des particularités du contexte de la banque");
         ItemForErrorResult<ImportColonne> itemChampEntite = null;
         EContexte banqueEContexte = EContexte.findByNom(banque.getContexte().getNom());
          
         //   - 1) la banque est de contexte sérologie (des champs sont "supprimés" dans ce contexte)
         if(banqueEContexte.equals(EContexte.SEROLOGIE)) {
            log.debug("validation pour une banque de contexte sérologie");
            itemChampEntite =  validateChampSerologie(colonneSortingResult.getListChampEntiteForTemplate(), banque);
         }
         else if (banqueEContexte.equals(EContexte.GATSBI)) {
        //   - 2) la banque est de contexte Gatsbi :
            log.debug("validation pour une banque de contexte Gatsbi");
            itemChampEntite =  validateChampGatsbi(colonneSortingResult.getListChampEntiteForTemplate(), banque);
         }
         
         //vérifie les conditions suivantes et renvoie une incompatibilité si ce n'est pas le cas : 
         // - tous les champs obligatoires dans la banque passée en paramètre sont bien présents et obligatoires dans le modèle.
         // - tous les champs obligatoires dans le modèle sont bien obligatoires pour la banque passée en paramètre
         //Le bloquage aurait pu se restreindre au contrôle sur la présence dans le modèle des champs obligatoires dans le contexte sans regarder s'ils sont obligatoires dans le modèle 
         //car cette notion obligatoire au niveau du modèle est utilisée :
         // - pour la modification d'un modèle. Or pour un modèle partagé, la modification est impossible.
         // - lors de l'exécution du modèle mais dans ce cas, c'est bien la notion obligatoire du champ dans le contexte d'exécution qui est pris en compte
         //Mais le fait de contrôler la parfaite exactitude de la notion "obligatoire" permet d'être cohérent entre ce qui sera pris en compte et ce qui est affiché sur la fiche du modèle.
         log.debug("validation de la cohérence des champs obligatoires des 2 côtés");
         InfoErrorChampObligatoire infoChampObligatoire = validateChampObligatoire(colonneSortingResult.getListChampEntiteForTemplate(),
            importTemplate.getBanque(), banque);
         
         log.debug("fin des validations");
         //---------------- fin validations
         
         //construction du résutat
         log.debug("construction du résultat");
         if(infoErrorColonneAnnotation !=null || itemChampDelegue != null || itemChampEntite != null || infoChampObligatoire != null) {
            ItemForErrorResult<TableAnnotation> itemTableAnnotations = null;
            List<ImportColonne> listImportColonnesAnnotation = null;
            if(infoErrorColonneAnnotation != null) {
               itemTableAnnotations = infoErrorColonneAnnotation.getItemForErrorResult();
               listImportColonnesAnnotation = infoErrorColonneAnnotation.getListImportColonneAnnotationForTemplate();
            }
            
            ItemForErrorResult<Entite> itemObligation = null;
            List<ChampEntite> listChampObligatoirePourBanqueMaisNonPresentDansModele = null;
            if(infoChampObligatoire != null) {
               itemObligation = infoChampObligatoire.getItemForErrorResult();
               listChampObligatoirePourBanqueMaisNonPresentDansModele = infoChampObligatoire.getListChampObligatoirePourBanqueMaisNonPresentDansModele();
               //si itemChampDelegue ou itemChampEntite sont non null et listChampObligatoirePourBanqueMaisNonPresentDansModele non vide, 
               //on change le toDo pour préciser que les champs obligatoires manquants seront ajoutés.
               if(listChampObligatoirePourBanqueMaisNonPresentDansModele != null && listChampObligatoirePourBanqueMaisNonPresentDansModele.size() > 0) {
                  if(itemChampDelegue != null) {
                     itemChampDelegue.setTodoKeyI18n(I18N_KEY__CHAMP_TODO2);
                  }
                  else if(itemChampEntite != null ) {
                     itemChampEntite.setTodoKeyI18n(I18N_KEY__CHAMP_TODO2);
                  }
               }
            }
            
            result = new IncompatibiliteEntreImportTemplateEtBanqueResult(itemTableAnnotations, listImportColonnesAnnotation,
                                                                           itemChampDelegue, itemChampEntite,
                                                                           itemObligation, listChampObligatoirePourBanqueMaisNonPresentDansModele);
            log.debug("fin de la construction du résultat");
         }
         
         return result;
      }
      else {
         String nomBanque = "{null}";
         String nomImportTemplate = "{null}";
         if(banque != null) {
            nomBanque = banque.toString();
         }
         if(importTemplate != null) {
            nomImportTemplate = importTemplate.toString();
         }
         throw new NullPointerException("Impossible de valider la compatibilité entre le template d'import " + nomImportTemplate + " et la banque " + nomBanque);
      }
   }
 
   private ColonneSortingResult sortImportColonneByType(List<ImportColonne> listImportColonne) {
      ColonneSortingResult result = new ColonneSortingResult();
      for(ImportColonne importColonne : listImportColonne) {
         Integer mapKey = null; 
         Integer champId = null;
         if(importColonne.getChamp().getChampAnnotation() != null) {
            result.getListImportColonneAnnotationForTemplate().add(importColonne);
            TableAnnotation tableAnnotation = importColonne.getChamp().getChampAnnotation().getTableAnnotation();
            if(!result.getListTableAnnotationForTemplate().contains(tableAnnotation)) {
               result.getListTableAnnotationForTemplate().add(tableAnnotation);
            }
         }
         else if(importColonne.getChamp().getChampDelegue() != null) {
            ChampDelegue champDelegue = importColonne.getChamp().getChampDelegue();
            result.getListChampDelegueForTemplate().add(champDelegue);
            mapKey = MAP_KEY__CHAMP_DELEGUE;
            champId = champDelegue.getId();
         }         
         else if(importColonne.getChamp().getChampEntite() != null) {
            ChampEntite champEntite = importColonne.getChamp().getChampEntite();
            result.getListChampEntiteForTemplate().add(champEntite);
            mapKey = MAP_KEY__CHAMP;
            champId = champEntite.getId();
         }
         if(mapKey != null && champId != null) {
            getMapLienChampEtImportColonne(mapKey).put(champId, importColonne);
         }
      }      
   
      return result;
   }
   
     private InfoErrorColonneAnnotation validateTableAnnotation(InfoColonneAnnotation infoColonneAnnotation, Banque banque) {
      if(infoColonneAnnotation.isNotEmpty()) {
         List<Banque> currentBanqueAsList = new ArrayList<Banque>();
         currentBanqueAsList.add(banque);
         List<TableAnnotation> listTableAnnotationForCurrentBanque = tableAnnotationManager.findByBanquesManager(currentBanqueAsList, true);
         List<TableAnnotation> listTableAnnotationForTemplateNotInBanque = createNewListFromList1MinusList2(infoColonneAnnotation.getListTableAnnotation(), listTableAnnotationForCurrentBanque);
         if(listTableAnnotationForTemplateNotInBanque.size() > 0) {
            ItemForErrorResult<TableAnnotation> errorTableAnnotation = new ItemForErrorResult<TableAnnotation>(
               I18N_KEY__ANNOTATION_ERROR, listTableAnnotationForTemplateNotInBanque, I18N_KEY__ANNOTATION_TODO);
         
            return new InfoErrorColonneAnnotation(errorTableAnnotation, infoColonneAnnotation.filterImportColonneFromTableAnnotation(listTableAnnotationForTemplateNotInBanque));
         }
      }
      return null;
   }
   
   private ItemForErrorResult<ImportColonne> validateChampDelegue(List<ChampDelegue> listChampDelegueForTemplate, Banque banque) {
      if(!listChampDelegueForTemplate.isEmpty()) {
         String nomContexteForCurrentBanque = banque.getContexte().getNom();
         for(ChampDelegue champDelegue : listChampDelegueForTemplate) {
            if(!champDelegue.getContexte().getNom().equals(nomContexteForCurrentBanque)) {
               //tous les champs délégués appartiennent au même contexte donc si la collection courante n'a pas le même contexte, 
               //tous les champs délégués sont incompatibles
               return buildImportErrorResult(listChampDelegueForTemplate, MAP_KEY__CHAMP_DELEGUE,
                  I18N_KEY__CHAMP_DELEGUE_ERROR, I18N_KEY__CHAMP_DELEGUE_TODO);
            }
         }
      }
      return null;
   }
   
   private ItemForErrorResult<ImportColonne> validateChampSerologie(List<ChampEntite> listChampEntiteForTemplate, Banque banque) {
      List<String> listContexteNom = Arrays.asList(EChampSupprimePourSerologie.values()).stream().map(enumValue -> enumValue.getNom()).collect(Collectors.toList());
      List<ChampEntite> listchampEntiteKO = new ArrayList<ChampEntite>();
      for(ChampEntite champEntiteForTemplate : listChampEntiteForTemplate) {
         if(listContexteNom.contains(champEntiteForTemplate.getNom())) {
            listchampEntiteKO.add(champEntiteForTemplate);
         }
      }
      if(listchampEntiteKO.size() > 0) {
         return buildImportErrorResult(listchampEntiteKO, MAP_KEY__CHAMP, I18N_KEY__CHAMP_ERROR, I18N_KEY__CHAMP_TODO);
      }
      return null;
   }
   
   //
   private List<Entite> retrieveEntiteFromListChampEntite(List<ChampEntite> listChampEntite) {
      List<Entite> listEntiteConcernee = new ArrayList<Entite>();
      for(ChampEntite champEntite : listChampEntite) {
         Entite entite = champEntite.getEntite();
         if(!listEntiteConcernee.contains(entite)) {
            listEntiteConcernee.add(entite);
         }
      }
      return listEntiteConcernee;
   }
   
   private List<ChampEntite> retrieveListChampEntiteForThisBanque(Banque banque, List<Entite> listEntite, Boolean nullable) {
      if(banque != null && banque.getEtude() != null) {
         EContexte banqueContexte = EContexte.findByNom(banque.getContexte().getNom());
         Etude banqueEtude = banque.getEtude();
         List<ChampEntite> listResult = new ArrayList<ChampEntite>();
         for(Entite entite : listEntite) {
            //récupération des champs pour cette entité et la banque en paramètre
            List<ChampEntite> listChampEntite = champEntiteManager.findByEntiteImportAndIsNullableManager(
                  entite, true, nullable, banqueContexte, 
                  banqueEtude == null ? null : banqueEtude.getContexteForEntite(entite.getEntiteId()));
            listResult.addAll(listChampEntite);
         }         
         return listResult;
      }
      return null;
   }
   
   private ItemForErrorResult<ImportColonne> validateChampGatsbi(List<ChampEntite> listChampEntiteForTemplate, Banque banque) {
      if(listChampEntiteForTemplate != null && banque != null && banque.getEtude() != null) {
         EContexte banqueContexte = EContexte.findByNom(banque.getContexte().getNom());
         Etude banqueEtude = banque.getEtude();
         //on va chercher les champs entité de la banque pour chaque entité présente dans le modèle
         List<Entite> listEntiteConcernee = new ArrayList<Entite>();
         List<ChampEntite> listChampEntiteForBanque = new ArrayList<ChampEntite>();
         for(ChampEntite champEntiteForTemplate : listChampEntiteForTemplate) {
            Entite entite = champEntiteForTemplate.getEntite();
            if(!listEntiteConcernee.contains(entite)) {
               listEntiteConcernee.add(entite);
               //récupération des champs pour cette entité et la banque en paramètre
               listChampEntiteForBanque.addAll(champEntiteManager.findByEntiteImportAndIsNullableManager(
                  entite, true, null, banqueContexte, 
                  banqueEtude.getContexteForEntite(entite.getEntiteId())));
            }
         }
         
         //comparaison entre la liste des champs du modèle et celle des champs de l'étude :
         List<ChampEntite> listChampEntiteForTemplateNonVisibleInBanque = createNewListFromList1MinusList2(listChampEntiteForTemplate, listChampEntiteForBanque);
         if(listChampEntiteForTemplateNonVisibleInBanque.size() > 0) {
            //récupération des importColonnes associés et construction de l'objet à retourner ItemForErrorResult<ImportColonne>
            return buildImportErrorResult(listChampEntiteForTemplateNonVisibleInBanque, MAP_KEY__CHAMP,
               I18N_KEY__CHAMP_ERROR, I18N_KEY__CHAMP_TODO);
         }
      }
      return null;
   }
   
   //vérifie les conditions suivantes et renvoie une incompatibilité si ce n'est pas le cas : 
   // - tous les champs obligatoires dans la banque passée en paramètre sont bien présents et obligatoires dans le modèle.
   // - tous les champs obligatoires dans le modèle sont bien obligatoires pour la banque passée en paramètre
   //Le bloquage aurait pu se restreindre au contrôle sur la présence dans le modèle des champs obligatoires dans le contexte sans regarder s'ils sont obligatoires dans le modèle 
   //car cette notion obligatoire au niveau du modèle est utilisée :
   // - pour la modification d'un modèle. Or pour un modèle partagé, la modification est impossible.
   // - lors de l'exécution du modèle mais dans ce cas, c'est bien la notion obligatoire du champ dans le contexte d'exécution qui est pris en compte
 //Mais le fait de contrôler la parfaite exactitude de la notion "obligatoire" permet d'être cohérent entre ce qui sera pris en compte et ce qui est affiché sur la fiche du modèle.
   private InfoErrorChampObligatoire validateChampObligatoire(final List<ChampEntite> listChampEntiteForTemplate,
      Banque templateBanque, Banque banque) {   
      EContexte templateEContexte = EContexte.findByNom(templateBanque.getContexte().getNom());
      EContexte banqueEContexte = EContexte.findByNom(banque.getContexte().getNom());
      
      //Si aucun des contextes n'est Gatsbi, les champs obligatoires sont forcément les mêmes 
      //car ce sont les mêmes pour les contextes anapat' et sérologie
      if(templateEContexte != EContexte.GATSBI && banqueEContexte != EContexte.GATSBI) {
         return null;
      }
      
      //récupération des champs obligatoires pour les 2 "côtés"
      List<ChampEntite> listChampEntiteObligatoireForTemplate = null;
      List<ChampEntite> listChampEntiteObligatoireForBanque = null;
      
      //la récupération des champs se fait par entité
      List<Entite> listEntiteForTemplate = retrieveEntiteFromListChampEntite(listChampEntiteForTemplate);
      
      //Côté template :
      //si non Gatsbi, l'information obligatoire est portée par le champEntite lui même :
      if(templateEContexte != EContexte.GATSBI) {
         listChampEntiteObligatoireForTemplate = listChampEntiteForTemplate.stream().filter(champEntite -> !champEntite.isNullable()).collect(Collectors.toList());
      }
      else {//sinon, on va chercher l'information dans les contextes
         listChampEntiteObligatoireForTemplate = new ArrayList<ChampEntite>();
         for(Entite entite : listEntiteForTemplate) {
            //filtre des champs de l'entité :
            List<ChampEntite> listChampForEntite = listChampEntiteForTemplate.stream().filter(champEntite -> champEntite.getEntite().equals(entite)).collect(Collectors.toList());
            //copie de la liste pour supprimer les champs entités non obligatoires directement sur cette liste
            List<ChampEntite> listChampObligatoireForEntite = new ArrayList<ChampEntite>(listChampForEntite);
            champEntiteManager.filterNullableOrNotChampEntite(Boolean.FALSE, templateBanque.getEtude().getContexteForEntite(entite.getEntiteId()), listChampObligatoireForEntite);
            listChampEntiteObligatoireForTemplate.addAll(listChampObligatoireForEntite);
         }
      }
      
      //côté banque :
      listChampEntiteObligatoireForBanque = retrieveListChampEntiteForThisBanque(banque, listEntiteForTemplate, Boolean.FALSE);
      
      //comparaisons :
      //- 1ere étape : champs obligatoires côté banque non obligatoires côté modèle :
      List<ChampEntite> listChampEntiteObligatoireForBanqueNonObligatoireInModele = createNewListFromList1MinusList2(listChampEntiteObligatoireForBanque, listChampEntiteObligatoireForTemplate);
      //- 2ere étape : champs obligatoires côté modèle non obligatoires côté banque :
      List<ChampEntite> listChampEntiteObligatoireInModeleNonObligatoireForBanque = createNewListFromList1MinusList2(listChampEntiteObligatoireForTemplate,listChampEntiteObligatoireForBanque);
      
      //cumul des 2 comparaisons
      List<ChampEntite> listCumulChampEntiteObligatoireNonCoherent = new ArrayList<ChampEntite>(listChampEntiteObligatoireForBanqueNonObligatoireInModele);
      listCumulChampEntiteObligatoireNonCoherent.addAll(listChampEntiteObligatoireInModeleNonObligatoireForBanque);
      
      if(listCumulChampEntiteObligatoireNonCoherent.size() > 0) {
         //Construction des données à retourner : ItemForErrorResult avec les entités concernées et les champs manquants
         //Récupération des entités concernées par les champs obligatoires manquants. 
         List<Entite> listEntiteDesChampsObligatoiresNonCoherents = new ArrayList<Entite>();
         for(ChampEntite champEntiteObligatoireNonCoherent : listCumulChampEntiteObligatoireNonCoherent) {
            if(!listEntiteDesChampsObligatoiresNonCoherents.contains(champEntiteObligatoireNonCoherent.getEntite())) {
               listEntiteDesChampsObligatoiresNonCoherents.add(champEntiteObligatoireNonCoherent.getEntite());
            }
         }
         
         //Filtre de la liste des champs non cohérents pour supprimer ceux présents dans le modèle mais non obligatoires alors qu'ils sont obligaoires dans la banque
         //En effet, cette liste sera utilisée pour ajouter des champs si l'utilisateur clique sur "Nouveau à partir de ce modèle"  :
         listCumulChampEntiteObligatoireNonCoherent.removeIf(champEntite -> listChampEntiteForTemplate.contains(champEntite));
         
         return new InfoErrorChampObligatoire(
                 new ItemForErrorResult<Entite>(
                    I18N_KEY__CHAMP_OBLIGATOIRE_ERROR, listEntiteDesChampsObligatoiresNonCoherents, I18N_KEY__CHAMP_OBLIGATOIRE_TODO),
                 listCumulChampEntiteObligatoireNonCoherent
               );
         
      }
      
      return null;
   }
   
   private <T> List<T> createNewListFromList1MinusList2(List<T> list1, List<T> list2) {
      List<T> result = new ArrayList<T>(list1);
      result.removeIf(champEntite -> list2.contains(champEntite));
      
      return result;
   }
   
   private <T extends AbstractTKChamp> ItemForErrorResult<ImportColonne> buildImportErrorResult(List<T> listChampKO, Integer keyMapLienChampColonne, String erreurKeyI18n, String todoKeyI18n) {
      List<ImportColonne> listImportColonneKO = new ArrayList<ImportColonne>();
      Map<Integer, ImportColonne> mapLienChampColonne = getMapLienChampEtImportColonne(keyMapLienChampColonne);
      for(AbstractTKChamp champKO : listChampKO) {
         listImportColonneKO.add((ImportColonne)mapLienChampColonne.get(champKO.getId()));
      }
      
      return new ItemForErrorResult<ImportColonne>(
         erreurKeyI18n, listImportColonneKO, todoKeyI18n);
   }
   
   
   private Map<Integer, ImportColonne> getMapLienChampEtImportColonne(Integer key){
      if(mapLienChampEtImportColonne.get(key) == null) {
         Map<Integer,ImportColonne> result = new HashMap<Integer,ImportColonne>();
         mapLienChampEtImportColonne.put(key,result);
         return result;
      }
      return mapLienChampEtImportColonne.get(key);
   }
   
   
   
   private class ColonneSortingResult {
      
      //résultat du tri des colonnes par type de champ. 
      //Pour les annotations, on aura besoin des tables d'annotation associées en plus des champs
      InfoColonneAnnotation infoColonneAnnotation = new InfoColonneAnnotation();
      List<ChampDelegue> listChampDelegueForTemplate = new ArrayList<ChampDelegue>();
      List<ChampEntite> listChampEntiteForTemplate = new ArrayList<ChampEntite>();
      
      public InfoColonneAnnotation getInfoColonneAnnotation(){
         return infoColonneAnnotation;
      }
      
      public List<ImportColonne> getListImportColonneAnnotationForTemplate(){
         return getInfoColonneAnnotation().getListImportColonneAnnotationForTemplate();
      }
      public List<TableAnnotation> getListTableAnnotationForTemplate(){
         return getInfoColonneAnnotation().getListTableAnnotation();
      }
      
      public List<ChampDelegue> getListChampDelegueForTemplate(){
         return listChampDelegueForTemplate;
      }

      public List<ChampEntite> getListChampEntiteForTemplate(){
         return listChampEntiteForTemplate;
      }
   }

   private class InfoColonneAnnotation {
      private List<ImportColonne> listImportColonneAnnotationForTemplate = new ArrayList<ImportColonne>();
      //liste des tables d'annotation des imports colonnes associées à un champ d'annotation
      private List<TableAnnotation> listTableAnnotation = new ArrayList<TableAnnotation>();
      
      public List<ImportColonne> getListImportColonneAnnotationForTemplate(){
         return listImportColonneAnnotationForTemplate;
      }
      public List<TableAnnotation> getListTableAnnotation(){
         return listTableAnnotation;
      }
      
      public boolean isNotEmpty() {
         return !listTableAnnotation.isEmpty();
      }
      
      public List<ImportColonne> filterImportColonneFromTableAnnotation(List<TableAnnotation> someTableAnnotation) {
         //filtre des importColonne correspondant aux tables d'annotation manquantes
         List<ImportColonne> listImportColonneAnnotationFiltree = new ArrayList<ImportColonne>(listImportColonneAnnotationForTemplate);
         listImportColonneAnnotationFiltree.removeIf(
            importColonne -> !someTableAnnotation.contains(importColonne.getChamp().getChampAnnotation().getTableAnnotation()));

         return listImportColonneAnnotationFiltree;
      }
   }

   private class InfoErrorColonneAnnotation {
      private ItemForErrorResult<TableAnnotation> itemForErrorResult;
      private List<ImportColonne> listImportColonneAnnotationForTemplate = new ArrayList<ImportColonne>();
      
      public InfoErrorColonneAnnotation(ItemForErrorResult<TableAnnotation> itemForErrorResult, List<ImportColonne> listImportColonneAnnotationForTemplate) {
         this.itemForErrorResult = itemForErrorResult;
         this.listImportColonneAnnotationForTemplate = listImportColonneAnnotationForTemplate;
      }
      
      public ItemForErrorResult<TableAnnotation> getItemForErrorResult(){
         return itemForErrorResult;
      }
      public List<ImportColonne> getListImportColonneAnnotationForTemplate(){
         return listImportColonneAnnotationForTemplate;
      }
   }
   
   private class InfoErrorChampObligatoire {
      //contient la liste des entités concernées par au moins un champ obligatoire incohérent
      private ItemForErrorResult<Entite> itemForErrorResult;
      private List<ChampEntite> listChampObligatoirePourBanqueMaisNonPresentDansModele = new ArrayList<ChampEntite>();
      
      public InfoErrorChampObligatoire(ItemForErrorResult<Entite> itemForErrorResult, List<ChampEntite> listChampObligatoirePourBanqueMaisNonPresentDansModele) {
         this.itemForErrorResult = itemForErrorResult;
         this.listChampObligatoirePourBanqueMaisNonPresentDansModele = listChampObligatoirePourBanqueMaisNonPresentDansModele;
      }
      
      public ItemForErrorResult<Entite> getItemForErrorResult(){
         return itemForErrorResult;
      }
      public List<ChampEntite> getListChampObligatoirePourBanqueMaisNonPresentDansModele(){
         return listChampObligatoirePourBanqueMaisNonPresentDansModele;
      }
   }
}
