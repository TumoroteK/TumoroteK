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
package fr.aphp.tumorotek.action.recherche;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Group;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheRechercheINCa extends AbstractFicheRechercheAvancee
{

   private static final long serialVersionUID = -6046959508126653655L;

   private static final String INCA = "INCa";

   /**
    * Components.
    */
   // Components patient
   private Datebox dateNaissance1Box;
   private Datebox dateNaissance2Box;
   private Listbox operateursDateNaissanceBox;
   private Datebox dateEtat1Box;
   private Datebox dateEtat2Box;
   private Listbox operateursDateEtatBox;
   private Listbox sexesBox;
   private Listbox etatsBox;
   // Components maladie
   private Textbox codeMaladieBox;
   private Datebox dateDiagnosticMaladie1Box;
   private Datebox dateDiagnosticMaladie2Box;
   private Listbox operateursDateDiagBox;
   // Components prélèvement
   private CalendarBox datePrelevement1Box;
   private CalendarBox datePrelevement2Box;
   private Listbox operateursDatePrlvtBox;
   private Listbox typePrlvtBox;
   // Components echantillon
   private Listbox typeEchantillonBox;
   private Decimalbox quantiteEchantillonBox;
   private Listbox uniteEchantillonBox;
   private Listbox tumoralEchantillonBox;
   private Listbox operateursQuantiteEchantillonBox;
   private Textbox codeLesionnelBox;
   private Textbox codeOrganeBox;
   // Groupes
   private Group groupPatients;
   private Group groupMaladies;
   private Group groupPrelevements;
   private Group groupEchantillons;

   /**
    * Objets principaux.
    */
   private Champ parentToQueryPatient;
   private Champ parentToQueryMaladie;
   private Champ parentToQueryPrlvt;
   private Champ parentToQueryEchantillon;

   /**
    * Liste d'objets.
    */
   private List<String> sexes = new ManagedList<>();
   private List<String> etats = new ManagedList<>();
   private List<PrelevementType> typePrlvts = new ArrayList<>();
   private List<EchantillonType> echantillonTypes = new ArrayList<>();
   private List<String> tumoraux = new ArrayList<>();
   private List<Unite> unites = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      objPatientComponents = new Component[] {this.dateNaissance1Box, this.dateNaissance2Box, this.dateEtat1Box,
         this.dateEtat2Box, this.sexesBox, this.etatsBox};

      objMaladieComponents =
         new Component[] {this.codeMaladieBox, this.dateDiagnosticMaladie1Box, this.dateDiagnosticMaladie2Box};

      objPrelevementComponents = new Component[] {this.datePrelevement1Box, this.datePrelevement2Box, this.typePrlvtBox};

      objEchantillonComponents = new Component[] {this.typeEchantillonBox, this.quantiteEchantillonBox, this.uniteEchantillonBox,
         this.tumoralEchantillonBox, this.codeLesionnelBox, this.codeOrganeBox};

      objOperateurs = new Component[] {this.operateursDateDiagBox, this.operateursDateEtatBox, this.operateursDateNaissanceBox,
         this.operateursDatePrlvtBox, this.operateursQuantiteEchantillonBox};

      objProdDeriveComponents = new Component[] {};

      getBinder().loadAll();
   }

   /**
    * Initialise la fiche de recherche.
    * 
    * @param entite
    *            Entité que l'on recherche.
    * @param path
    *            Path pour renvoyer les résultats.
    */

   public void initRechercheAvancee(final Entite entite, final String path, final AbstractListeController2 listeController){
      this.entiteToSearch = entite;
      this.pathToRespond = path;

      setObjectTabController(listeController.getObjectTabController());

      // init des listes
      sexes = new ArrayList<>();
      sexes.add(null);
      sexes.add(Labels.getLabel("patient.sexe.homme"));
      sexes.add(Labels.getLabel("patient.sexe.femme"));
      sexes.add(Labels.getLabel("patient.sexe.ind"));
      etats = new ArrayList<>();
      etats.add(null);
      etats.add(Labels.getLabel("patient.etat.vivant"));
      etats.add(Labels.getLabel("patient.etat.decede"));
      etats.add(Labels.getLabel("patient.etat.inconnu"));
      operateursDecimaux = new ArrayList<>();
      operateursDecimaux.add("=");
      operateursDecimaux.add("<");
      operateursDecimaux.add(">");
      typePrlvts = ManagerLocator.getPrelevementTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      typePrlvts.add(0, null);
      echantillonTypes = ManagerLocator.getEchantillonTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      echantillonTypes.add(0, null);
      tumoraux = new ArrayList<>();
      tumoraux.add(null);
      tumoraux.add(Labels.getLabel("general.checkbox.true"));
      tumoraux.add(Labels.getLabel("general.checkbox.false"));
      unites = ManagerLocator.getUniteManager().findAllObjectsManager();
      unites.add(0, null);

      operateursDates = new ArrayList<>();
      operateursDates.add("=");
      operateursDates.add("<");
      operateursDates.add(">");
      operateursDates.add("[..]");

      objAnnotationsComponent = new ArrayList<>();

      drawAllAnnotationsINCa();
      createSearchHistoryListbox(entiteToSearch.getNom() + INCA);

      getBinder().loadComponent(self);
   }

   /**
    * dessine toutes les annotations INCa.
    */
   public void drawAllAnnotationsINCa(){
      // ajout des annotations pour les patients
      List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
      Entite tmp = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      if(banks.size() == 1){
         drawINCaAnnotationsContent(banks.get(0), tmp, groupMaladies);
      }else{
         for(int i = 0; i < banks.size(); i++){
            drawINCaAnnotationsContent(banks.get(i), tmp, groupMaladies);
         }
      }

      // ajout des annotations pour les prelevements
      banks = SessionUtils.getSelectedBanques(sessionScope);
      tmp = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      if(banks.size() == 1){
         drawINCaAnnotationsContent(banks.get(0), tmp, groupEchantillons);
      }else{
         for(int i = 0; i < banks.size(); i++){
            drawINCaAnnotationsContent(banks.get(i), tmp, groupEchantillons);
         }
      }

      // ajout des annotations pour les échantillons
      banks = SessionUtils.getSelectedBanques(sessionScope);
      tmp = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      if(banks.size() == 1){
         drawINCaAnnotationsContent(banks.get(0), tmp, null);
      }else{
         for(int i = 0; i < banks.size(); i++){
            drawINCaAnnotationsContent(banks.get(i), tmp, null);
         }
      }
   }

   @Override
   public void onClick$find(){
      Clients.showBusy(Labels.getLabel("recherche.avancee.en.cours"));
      Events.echoEvent("onLaterFind", self, null);
   }

   @Override
   public void onOK(){
      onClick$find();
   }

   public void onOpen$groupPatients(){
      final String id = groupPatients.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   public void onOpen$groupMaladies(){
      final String id = groupMaladies.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   public void onOpen$groupPrelevements(){
      final String id = groupPrelevements.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   public void onOpen$groupEchantillons(){
      final String id = groupEchantillons.getUuid();
      final String idTop = panelChildrenWithScroll.getUuid();
      Clients.evalJavaScript(
         "document.getElementById('" + idTop + "')" + ".scrollTop = document.getElementById('" + id + "')" + ".offsetTop;");
   }

   /**
    * Exécution de la requête.
    */

   public void onLaterFind(){
      oneValueEntered = false;
      resultats = new ArrayList<>();

      // création des chemins d'accès aux differents éléments
      if(entiteToSearch.getNom().equals("Patient")){
         // createChampsParentsToQueryPatients();
      }else if(entiteToSearch.getNom().equals("Prelevement")){
         // createChampsParentsToQueryPrelevements();
      }else if(entiteToSearch.getNom().equals("Echantillon")){
         createChampsParentsToQueryEchantillons();
      }else if(entiteToSearch.getNom().equals("ProdDerive")){
         // createChampsParentsToQueryProdDerives();
      }

      // traite les critères sur les champs des patients
      executeQueriesForPatients();

      // traite les critères sur les champs des maladies
      executeQueriesForMaladies();

      // traite les critères sur les champs des prlvts
      executeQueriesForPrelevements();

      // traite les critères sur les champs des échantillons
      executeQueriesForEchantillons();

      // traite les critères sur les anntations
      executeQueriesForAnnotations();

      // si aucune valeur n'a été saisie dans aucun des champs, on
      // récupère tous les objets
      if(!oneValueEntered){
         if(entiteToSearch.getNom().equals("Patient")){
            resultatsIds.addAll(ManagerLocator.getPatientManager().findAllObjectsIdsManager());
         }else if(entiteToSearch.getNom().equals("Prelevement")){
            resultatsIds.addAll(ManagerLocator.getPrelevementManager()
               .findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
         }else if(entiteToSearch.getNom().equals("Echantillon")){
            resultatsIds.addAll(ManagerLocator.getEchantillonManager()
               .findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
         }else if(entiteToSearch.getNom().equals("ProdDerive")){
            resultatsIds.addAll(ManagerLocator.getProdDeriveManager()
               .findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
         }
      }else{
         final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);
         if(criteresStandards.size() > 0){
            if(!otherQuery){
               resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCriteresWithBanquesManager(criteresStandards,
                  banques, valeursStandards);
            }else{
               // sinon on fait une intersection des résultats de la
               // requête avec ceux déjà trouvés
               final List<Integer> objects = ManagerLocator.getTraitementQueryManager()
                  .findObjetByCriteresWithBanquesManager(criteresStandards, banques, valeursStandards);

               resultatsIds = ListUtils.intersection(resultatsIds, objects);
            }
            otherQuery = true;
         }
      }

      // ferme wait message
      Clients.clearBusy();

      createSearchHistory(entiteToSearch.getNom() + INCA);

      if(resultatsIds.size() > 500){
         openResultatsWindow(page, resultatsIds, self, "Echantillon", getObjectTabController());
         // search history settings
      }else if(resultatsIds.size() > 0){
         onShowResults();
      }else{
         Messagebox.show(Labels.getLabel("recherche.avancee.no.results"), Labels.getLabel("recherche.avancee.no.results.title"),
            new Messagebox.Button[] {Messagebox.Button.CANCEL, Messagebox.Button.RETRY}, Messagebox.QUESTION,
            new org.zkoss.zk.ui.event.EventListener<ClickEvent>()
            {
               @Override
               public void onEvent(final ClickEvent e){
                  switch(e.getButton()){
                     case CANCEL: // CANCEL is clicked
                        Events.postEvent(new Event("onClose", self.getRoot()));
                        break;
                     case RETRY: // RETRY is clicked
                        criteresStandards.clear();
                        valeursStandards.clear();
                        otherQuery = false;
                        break;
                     default: // if e.getButton() returns null
                  }
               }
            });
      }
   }

   /**
    * Crée les champs parents qui permettront d'accéder aux éléments lors d'une
    * recherche sur les échantillons.
    */
   public void createChampsParentsToQueryEchantillons(){
      // accès aux échantillons
      parentToQueryEchantillon = null;
      // accès aux prélèvements
      final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      final ChampEntite champEntitePrelevement =
         ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "PrelevementId").get(0);
      parentToQueryPrlvt = new Champ(champEntitePrelevement);
      // accès aux maladies
      final Entite prlvtEntite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      final ChampEntite champEntiteMaladie =
         ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "MaladieId").get(0);
      parentToQueryMaladie = new Champ(champEntiteMaladie);
      parentToQueryMaladie.setChampParent(parentToQueryPrlvt);
      // Accès aux patients
      final Entite maladieEntite = ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0);
      final ChampEntite champEntitePatient =
         ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(maladieEntite, "PatientId").get(0);
      parentToQueryPatient = new Champ(champEntitePatient);
      parentToQueryPatient.setChampParent(parentToQueryMaladie);
      // 1er accès aux produits dérivés
      final ChampEntite champEntiteEchanProdDerives =
         ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(echanEntite, "ProdDerives").get(0);
      parent1ToQueryProdDerive = new Champ(champEntiteEchanProdDerives);
      parent1ToQueryProdDerive.setChampParent(parentToQueryEchantillon);
      parent2ToQueryProdDerive = null;

   }

   /**
    * Exécute les requêtes avec des critères sur les champs des patients.
    */
   public void executeQueriesForPatients(){
      // pour chaque champ interrogeable pour les patients
      for(int i = 0; i < objPatientComponents.length; i++){
         // si c'est un listbox
         if(objPatientComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objPatientComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               String value = "";
               if(current.getId().equals("sexesBox")){
                  final String tmp = sexes.get(current.getSelectedIndex());
                  if(tmp.equals(Labels.getLabel("patient.sexe.homme"))){
                     value = "M";
                  }else if(tmp.equals(Labels.getLabel("patient.sexe.femme"))){
                     value = "F";
                  }else if(tmp.equals(Labels.getLabel("patient.sexe.ind"))){
                     value = "Ind";
                  }
               }else if(current.getId().equals("etatsBox")){
                  final String tmp = etats.get(current.getSelectedIndex());
                  if(tmp.equals(Labels.getLabel("patient.etat.vivant"))){
                     value = "V";
                  }else if(tmp.equals(Labels.getLabel("patient.etat.decede"))){
                     value = "D";
                  }else if(tmp.equals(Labels.getLabel("patient.etat.inconnu"))){
                     value = "Inconnu";
                  }
               }

               // exécution de la requête
               executeSimpleQueryForListboxWithSpecialValue(current, parentToQueryPatient, null, value, oneValueEntered);

               oneValueEntered = true;
            }
         }

         // si c'est un datebox
         if(objPatientComponents[i].getClass().getSimpleName().equals("Datebox")){
            final Datebox current = (Datebox) objPatientComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){

               String operateur = "";
               // si la requete porte sur la 1er date de naissance
               if(current.getId().equals("dateNaissance1Box")){
                  int idx = 0;
                  if(operateursDateNaissanceBox.getSelectedIndex() > 0){
                     idx = operateursDateNaissanceBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateNaissanceBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">=";
                  }
               }else if(current.getId().equals("dateNaissance2Box")){
                  // si la requete porte sur la 2eme date de naissance
                  int idx = 0;
                  if(operateursDateNaissanceBox.getSelectedIndex() > 0){
                     idx = operateursDateNaissanceBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateNaissanceBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<=";
                  }
               }else if(current.getId().equals("dateEtat1Box")){
                  int idx = 0;
                  if(operateursDateEtatBox.getSelectedIndex() > 0){
                     idx = operateursDateEtatBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateEtatBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">=";
                  }
               }else if(current.getId().equals("dateEtat2Box")){
                  // si la requete porte sur la 2eme date de naissance
                  int idx = 0;
                  if(operateursDateEtatBox.getSelectedIndex() > 0){
                     idx = operateursDateEtatBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateEtatBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<=";
                  }
               }

               // exécution de la requête
               if(operateur != null){
                  executeSimpleQueryForDatebox(current, parentToQueryPatient, null, oneValueEntered, operateur, false);
               }

               oneValueEntered = true;
            }
         }
      }
   }

   /**
    * Exécute les requêtes avec des critères sur les champs des maladies.
    */
   public void executeQueriesForMaladies(){
      // pour chaque champ interrogeable pour les maladies
      for(int i = 0; i < objMaladieComponents.length; i++){
         // si c'est un textbox
         if(objMaladieComponents[i].getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objMaladieComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null && !current.getValue().equals("")){
               // exécution de la requête
               executeSimpleQueryForTextbox(current, parentToQueryMaladie, null, oneValueEntered, false);

               oneValueEntered = true;
            }
         }

         // si c'est un datebox
         if(objMaladieComponents[i].getClass().getSimpleName().equals("Datebox")){
            final Datebox current = (Datebox) objMaladieComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){

               String operateur = "";
               // si la requete porte sur la 1er date de diag
               if(current.getId().equals("dateDiagnosticMaladie1Box")){
                  int idx = 0;
                  if(operateursDateDiagBox.getSelectedIndex() > 0){
                     idx = operateursDateDiagBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateDiagBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">=";
                  }
               }else if(current.getId().equals("dateDiagnosticMaladie2Box")){
                  // si la requete porte sur la 2eme date de diag
                  int idx = 0;
                  if(operateursDateDiagBox.getSelectedIndex() > 0){
                     idx = operateursDateDiagBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateDiagBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<=";
                  }
               }

               // exécution de la requête
               if(operateur != null){
                  executeSimpleQueryForDatebox(current, parentToQueryMaladie, null, oneValueEntered, operateur, false);
               }

               oneValueEntered = true;
            }
         }
      }
   }

   /**
    * Exécute les requêtes avec des critères sur les champs des prlvts.
    */
   public void executeQueriesForPrelevements(){
      // pour chaque champ interrogeable pour les prlvts
      for(int i = 0; i < objPrelevementComponents.length; i++){
         // si c'est un listbox
         if(objPrelevementComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objPrelevementComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               // exécution de la requête
               executeSimpleQueryForListbox(current, parentToQueryPrlvt, null, oneValueEntered, null);

               oneValueEntered = true;
            }
         }

         // si c'est un datebox
         if(objPrelevementComponents[i].getClass().getSimpleName().equals("CalendarBox")){
            final CalendarBox current = (CalendarBox) objPrelevementComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){

               String operateur = "";
               // si la requete porte sur la 1er date de naissance
               if(current.getId().equals("datePrelevement1Box")){
                  int idx = 0;
                  if(operateursDatePrlvtBox.getSelectedIndex() > 0){
                     idx = operateursDatePrlvtBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">=";
                  }
               }else if(current.getId().equals("datePrelevement2Box")){
                  // si la requete porte sur la 2eme date de naissance
                  int idx = 0;
                  if(operateursDatePrlvtBox.getSelectedIndex() > 0){
                     idx = operateursDatePrlvtBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDatePrlvtBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<=";
                  }
               }

               // exécution de la requête
               if(operateur != null){
                  executeSimpleQueryForCalendarbox(current, parentToQueryPrlvt, null, oneValueEntered, operateur);
               }

               oneValueEntered = true;
            }
         }

      }
   }

   /**
    * Exécute les requêtes avec des critères sur les champs des échantillons.
    */
   public void executeQueriesForEchantillons(){
      // pour chaque champ interrogeable pour les échantillons
      for(int i = 0; i < objEchantillonComponents.length; i++){
         // si c'est un textbox
         if(objEchantillonComponents[i].getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objEchantillonComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null && !current.getValue().equals("")){
               // si la requete porte sur la quantité
               if(current.getId().equals("codeOrganeBox")){

                  // execution de la requête
                  executeCodesQuery(current, parentToQueryEchantillon, "CodesAssignes", "CodeOrgane", false);

               }else if(current.getId().equals("codeLesionnelBox")){

                  // execution de la requête
                  executeCodesQuery(current, parentToQueryEchantillon, "CodesAssignes", "CodeOrgane", true);

               }else{
                  // exécution de la requête
                  executeSimpleQueryForTextbox(current, parentToQueryEchantillon, null, oneValueEntered, false);

               }
               oneValueEntered = true;
            }
         }

         // si c'est un listbox
         if(objEchantillonComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objEchantillonComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               if(current.getId().equals("tumoralEchantillonBox")){
                  final String tmp = tumoraux.get(current.getSelectedIndex());
                  boolean tum = false;
                  if(tmp.equals(Labels.getLabel("general.checkbox.true"))){
                     tum = true;
                  }

                  // exécution de la requête
                  executeSimpleQueryForListboxWithSpecialValue(current, parentToQueryEchantillon, null, tum, oneValueEntered);
               }else{
                  // exécution de la requête
                  executeSimpleQueryForListbox(current, parentToQueryEchantillon, null, oneValueEntered, null);
               }
               oneValueEntered = true;
            }
         }

         // si c'est un decimalbox
         if(objEchantillonComponents[i].getClass().getSimpleName().equals("Decimalbox")){
            final Decimalbox current = (Decimalbox) objEchantillonComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){
               String operateur = "";
               // si la requete porte sur la quantité
               if(current.getId().equals("quantiteEchantillonBox")){
                  int idx = 0;
                  if(operateursQuantiteEchantillonBox.getSelectedIndex() > 0){
                     idx = operateursQuantiteEchantillonBox.getSelectedIndex();
                  }
                  operateur = (String) operateursQuantiteEchantillonBox.getListModel().getElementAt(idx);
               }

               // exécution de la requête
               executeSimpleQueryForDecimalbox(current, parentToQueryEchantillon, null, oneValueEntered, operateur, false);

               oneValueEntered = true;
            }
         }
      }
   }

   /**
    * Exécute les requêtes avec des critères sur les champs des annotations.
    */
   public void executeQueriesForINCaAnnotations(){
      // pour chaque champ interrogeable pour les annotations
      for(int i = 0; i < objAnnotationsComponent.size(); i++){
         Champ parent = null;
         if(objAnnotationsComponent.get(i).getAttribute("entite").equals("Patient")){
            parent = parentToQueryPatient;
         }else if(objAnnotationsComponent.get(i).getAttribute("entite").equals("Prelevement")){
            parent = parentToQueryPrlvt;
         }else if(objAnnotationsComponent.get(i).getAttribute("entite").equals("Echantillon")){
            parent = parentToQueryEchantillon;
         }

         // si c'est un textbox
         if(objAnnotationsComponent.get(i).getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objAnnotationsComponent.get(i);
            // si une valeur a été saisie
            if(current.getValue() != null && !current.getValue().equals("")){
               // exécution de la requête
               executeSimpleQueryForTextbox(current, parent, null, oneValueEntered, false);

               oneValueEntered = true;
            }
         }

         // si c'est un listbox
         if(objAnnotationsComponent.get(i).getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objAnnotationsComponent.get(i);
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){

               // exécution de la requête
               executeSimpleQueryForListbox(current, parent, null, oneValueEntered, null);

               oneValueEntered = true;
            }
         }

         // si c'est un datebox
         if(objAnnotationsComponent.get(i).getClass().getSimpleName().equals("Datebox")){
            final Datebox current = (Datebox) objAnnotationsComponent.get(i);
            // si une valeur a été saisie
            if(current.getValue() != null){

               executeSimpleQueryForDatebox(current, parent, null, oneValueEntered, "=", false);

               oneValueEntered = true;
            }
         }

         // si c'est un intbox
         if(objAnnotationsComponent.get(i).getClass().getSimpleName().equals("Decimalbox")){
            final Decimalbox current = (Decimalbox) objAnnotationsComponent.get(i);
            // si une valeur a été saisie
            if(current.getValue() != null){

               // exécution de la requête
               executeSimpleQueryForDecimalbox(current, parent, null, oneValueEntered, "like", true);

               oneValueEntered = true;
            }
         }
      }
   }

   /**
    * Exécute la requête permettant de récupérer des prlvts en fonction du
    * nombre d'échantillons.
    * 
    * @param first
    *            True si c'est la 1ère requête que l'on exécute.
    * @param operateur
    *            Opérateur de la requête.
    * @param nbEchantillons
    *            Nombre d'échantillons.
    * @return La liste de résultats mise à jour.
    */

   public void executeCodesQuery(final Textbox current, final Champ parent1,
      final String entiteInEchantillon, final String entiteFinale, final boolean isMorpho){

      // on récupère la ou les banques sélectionnée(s)
      final List<Banque> banques = SessionUtils.getSelectedBanques(sessionScope);

      String value = current.getValue();
      final List<CodeCommon> liste = ManagerLocator.getTableCodageManager().findCodesAndTranscodesFromStringManager(value,
         getTablesForBanques(), banques, false);

      value = value + "%";

      // si c'est la 1ère requête les résultats vont directement
      // dans la liste
      if(!searchForProdDerives){
         final Entite codesEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         final ChampEntite champCodes =
            ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(codesEntite, entiteInEchantillon).get(0);
         final Champ tmp = new Champ(champCodes);
         tmp.setChampParent(parent1);

         final Entite codeAssigne = ManagerLocator.getEntiteManager().findByNomManager(entiteFinale).get(0);
         final ChampEntite champCode =
            ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(codeAssigne, "Code").get(0);
         final Champ champFinal = new Champ(champCode);
         champFinal.setChampParent(tmp);

         // création du critère
         final Critere critere = new Critere(champFinal, "", "");

         if(!otherQuery){
            resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesManager(critere,
               banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
               ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho);
         }else{
            // sinon on fait une intersection des résultats de la
            // requête avec ceux déjà trouvés
            final List<Integer> objects = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesManager(
               critere, banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
               ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho);

            resultatsIds = ListUtils.intersection(resultatsIds, objects);
         }
      }else{
         final Entite echanEntite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         if(!otherQuery){
            resultatsIds = ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesDerivesVersionManager(
               banques, ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
               ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho, echanEntite);
         }else{
            // sinon on fait une intersection des résultats de la
            // requête avec ceux déjà trouvés
            final List<Integer> objects =
               ManagerLocator.getTraitementQueryManager().findObjetByCritereOnCodesWithBanquesDerivesVersionManager(banques,
                  ManagerLocator.getTableCodageManager().getListCodesFromCodeCommon(liste),
                  ManagerLocator.getTableCodageManager().getListLibellesFromCodeCommon(liste), value, isMorpho, echanEntite);

            resultatsIds = ListUtils.intersection(resultatsIds, objects);
         }
      }
      otherQuery = true;
   }

   @Override
   public void onOpen$groupAnnotations(){}

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   /*******************************************************/
   /***************** GETTERS - SETTERS *******************/
   /*******************************************************/

   public Champ getParentToQueryPatient(){
      return parentToQueryPatient;
   }

   public void setParentToQueryPatient(final Champ p){
      this.parentToQueryPatient = p;
   }

   public Champ getParentToQueryMaladie(){
      return parentToQueryMaladie;
   }

   public void setParentToQueryMaladie(final Champ p){
      this.parentToQueryMaladie = p;
   }

   public Champ getParentToQueryPrlvt(){
      return parentToQueryPrlvt;
   }

   public void setParentToQueryPrlvt(final Champ p){
      this.parentToQueryPrlvt = p;
   }

   public Champ getParentToQueryEchantillon(){
      return parentToQueryEchantillon;
   }

   public void setParentToQueryEchantillon(final Champ p){
      this.parentToQueryEchantillon = p;
   }

   public List<String> getSexes(){
      return sexes;
   }

   public void setSexes(final List<String> s){
      this.sexes = s;
   }

   public List<String> getEtats(){
      return etats;
   }

   public void setEtats(final List<String> e){
      this.etats = e;
   }

   public List<PrelevementType> getTypePrlvts(){
      return typePrlvts;
   }

   public void setTypePrlvts(final List<PrelevementType> t){
      this.typePrlvts = t;
   }

   public List<EchantillonType> getEchantillonTypes(){
      return echantillonTypes;
   }

   public void setEchantillonTypes(final List<EchantillonType> e){
      this.echantillonTypes = e;
   }

   public List<String> getTumoraux(){
      return tumoraux;
   }

   public void setTumoraux(final List<String> t){
      this.tumoraux = t;
   }

   public List<Unite> getUnites(){
      return unites;
   }

   public void setUnites(final List<Unite> u){
      this.unites = u;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}

   @Override
   public List<String> getOpenedGroups(){
      return new ArrayList<>();
   }

   @Override
   public void setGroupPatientsOpened(final Boolean g){}

   @Override
   public void setGroupMaladiesOpened(final Boolean g){}

   @Override
   public void setGroupPrelevementsOpened(final Boolean g){}

   @Override
   public void setGroupEchantillonsOpened(final Boolean g){}

   @Override
   public void setGroupProdDerivesOpened(final Boolean g){}

}
