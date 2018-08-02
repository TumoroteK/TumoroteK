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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractListeController2;
import fr.aphp.tumorotek.component.CalendarBox;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @since 2.0.10 lance la requête sur la date de destruction en parallèle
 *
 * @author Mathieu BARTHELEMY
 *
 */
public class FicheRechercheAvanceeCession extends AbstractFicheRechercheAvancee
{

   /**
    * Components.
    */
   // Components patient
   private Textbox numeroCessionBox;
   private Listbox statutCessionBox;
   private Listbox typeCessionBox;
   private Listbox operateursDateCessionBox;
   private Listbox operateursDateValidationBox;
   private CalendarBox dateCession1Box;
   private CalendarBox dateCession2Box;
   private Datebox dateValidation1Box;
   private Datebox dateValidation2Box;
   private Listbox demandeurCessionBox;
   private Listbox contratCessionBox;
   private Listbox examenCessionBox;
   private Listbox destructionCessionBox;
   private Textbox titreCessionBox;
   private Textbox descriptionCessionBox;
   private Textbox observationsCessionBox;

   /**
    * Associations.
    */
   private List<CessionStatut> cessionStatuts = new ArrayList<>();
   private List<CessionType> cessionTypes = new ArrayList<>();
   private List<Collaborateur> demandeurs = new ArrayList<>();
   private List<Contrat> contrats = new ArrayList<>();
   private List<CessionExamen> cessionExamens = new ArrayList<>();
   private List<DestructionMotif> destructions = new ArrayList<>();

   private boolean isDateCession = false;

   /**
    * Objets principaux.
    */

   private static final long serialVersionUID = -1039814818005529339L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      objCessionComponents = new Component[] {this.numeroCessionBox, this.statutCessionBox, this.typeCessionBox,
         this.dateCession1Box, this.dateCession2Box, this.dateValidation1Box, this.dateValidation2Box, this.demandeurCessionBox,
         this.contratCessionBox, this.examenCessionBox, this.destructionCessionBox, this.titreCessionBox,
         this.descriptionCessionBox, this.observationsCessionBox};

      objOperateurs = new Component[] {this.operateursDateCessionBox, this.operateursDateValidationBox};

      getBinder().loadAll();
   }

   /**
    * Initialise la fiche de recherche.
    * 
    * @param entite
    *            Entité que l'on recherche.
    * @param path
    *            Path pour renvoyer les résultats.
    * @param ano
    *            True si la recherche est anonyme.
    */

   public void initRechercheAvancee(final Entite entite, final String path, final AbstractListeController2 listeController){
      this.entiteToSearch = entite;
      this.pathToRespond = path;

      setObjectTabController(listeController.getObjectTabController());

      // init des listes
      cessionStatuts = ManagerLocator.getCessionStatutManager().findAllObjectsManager();
      cessionStatuts.add(0, null);
      cessionTypes = ManagerLocator.getCessionTypeManager().findAllObjectsManager();
      cessionTypes.add(0, null);
      demandeurs = ManagerLocator.getCollaborateurManager().findAllObjectsWithOrderManager();
      demandeurs.add(0, null);
      contrats = ManagerLocator.getContratManager().findAllObjectsByPlateformeManager(SessionUtils.getPlateforme(sessionScope));
      contrats.add(0, null);
      cessionExamens = ManagerLocator.getCessionExamenManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      cessionExamens.add(0, null);
      destructions = ManagerLocator.getDestructionMotifManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      destructions.add(0, null);
      
      operateursDecimaux = new ArrayList<>();
      operateursDecimaux.add("=");
      operateursDecimaux.add("<");
      operateursDecimaux.add(">");

      operateursDates = new ArrayList<>();
      operateursDates.add("=");
      operateursDates.add("<");
      operateursDates.add(">");
      operateursDates.add("[..]");

      groupAnnotations.setLabel(Labels.getLabel("recherche.avancee.cessions.annotations"));

      createSearchHistoryListbox(entiteToSearch.getNom());

      getBinder().loadComponent(self);
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

   // Méthodes gérants l'ouverture des groupes pour descendre le scroll
   @Override
   public void onOpen$groupAnnotations(){
      // si le groupe n'a jamais été ouvert
      if(groupAnnotations.isOpen() && !annotationAlreadyOpen){
         // on init les annotations a afficher
         annotationAlreadyOpen = true;
         objAnnotationsComponent = new ArrayList<>();

         final List<Banque> banks = SessionUtils.getSelectedBanques(sessionScope);
         if(banks.size() == 1){
            drawAnnotationPanelContent(banks.get(0));
         }else{
            for(int i = 0; i < banks.size(); i++){
               drawAnnotationPanelContent(banks.get(i));
            }
         }
      }

      final String id = groupAnnotations.getUuid();
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

      // traite les critères sur les champs des cessions
      executeQueriesForCessions();

      // traite les critères sur les anntations
      executeQueriesForAnnotations();

      // si aucune valeur n'a été saisie dans aucun des champs, on
      // récupère tous les objets
      if(!oneValueEntered){
         resultatsIds.addAll(
            ManagerLocator.getCessionManager().findAllObjectsIdsByBanquesManager(SessionUtils.getSelectedBanques(sessionScope)));
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

            // 2.0.10 lance la requête sur la date de validation en parallèle
            if(isDateCession){
               // remplacement du champ entite et du Calendar en Date
               for(int i = 0; i < criteresStandards.size(); i++){
                  if(criteresStandards.get(i).getChamp().getChampEntite().getNom().equals("DestructionDate")){
                     criteresStandards.get(i).getChamp().setChampEntite(ManagerLocator.getChampEntiteManager()
                        .findByEntiteAndNomManager(criteresStandards.get(i).getChamp().getChampEntite().getEntite(), "DepartDate")
                        .get(0));
                     // Calendar cal = (Calendar) valeursStandards.get(i);
                     // valeursStandards.remove(i);
                     // valeursStandards.add(i, cal.getTime());
                  }
               }

               // relance la query
               resultatsIds.addAll(ManagerLocator.getTraitementQueryManager()
                  .findObjetByCriteresWithBanquesManager(criteresStandards, banques, valeursStandards));
            }

            otherQuery = true;
         }
      }

      // ferme wait message
      Clients.clearBusy();

      createSearchHistory(entiteToSearch.getNom());

      if(resultatsIds.size() > 500){
         openResultatsWindow(page, resultatsIds, self, "Cession", getObjectTabController());
         // search history settings
         // createSearchHistory();
      }else if(resultatsIds.size() > 0){
         onShowResults();
         // createSearchHistory();
      }else{
         Messagebox.show(Labels.getLabel("recherche.avancee.no.results"), Labels.getLabel("recherche.avancee.no.results.title"),
            new Messagebox.Button[] {Messagebox.Button.CANCEL, Messagebox.Button.RETRY}, Messagebox.QUESTION,
            new org.zkoss.zk.ui.event.EventListener<ClickEvent>()
            {
               @Override
               public void onEvent(final ClickEvent e){
                  if(e.getName().equals("onClose")){
                     Events.postEvent(new Event("onClose", self.getRoot()));
                  }else if(e.getButton() != null){
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
               }
            });
      }
   }

   /**
    * Exécute les requêtes avec des critères sur les champs des cessions.
    */
   public void executeQueriesForCessions(){
      // pour chaque champ interrogeable pour les cessions
      for(int i = 0; i < objCessionComponents.length; i++){
         // si c'est un textbox
         if(objCessionComponents[i].getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objCessionComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null && !current.getValue().equals("")){
               // exécution de la requête
               executeSimpleQueryForTextbox(current, null, null, true);

               oneValueEntered = true;
            }
         }else if(objCessionComponents[i].getClass().getSimpleName().equals("Intbox")){
            final Intbox current = (Intbox) objCessionComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){
               // exécution de la requête
               executeSimpleQueryForIntbox(current, null, null, "=");

               oneValueEntered = true;
            }
         }else if(objCessionComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objCessionComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               // exécution de la requête
               executeSimpleQueryForListbox(current, null, null, null);

               oneValueEntered = true;
            }
         }else if(objCessionComponents[i].getClass().getSimpleName().equals("Datebox")){
            final Datebox current = (Datebox) objCessionComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){

               String operateur = "";
               // si la requete porte sur la 1er date de début
               if(current.getId().equals("dateValidation1Box")){
                  int idx = 0;
                  if(operateursDateValidationBox.getSelectedIndex() > 0){
                     idx = operateursDateValidationBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateValidationBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">";
                  }
               }else if(current.getId().equals("dateValidation2Box")){
                  // si la requete porte sur la 2eme date de début
                  int idx = 0;
                  if(operateursDateValidationBox.getSelectedIndex() > 0){
                     idx = operateursDateValidationBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateValidationBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<";
                  }
               }

               // exécution de la requête
               if(operateur != null){
                  executeSimpleQueryForDatebox(current, null, null, operateur, false);
               }

               oneValueEntered = true;
            }
         }else if(objCessionComponents[i].getClass().getSimpleName().equals("CalendarBox")){
            final CalendarBox current = (CalendarBox) objCessionComponents[i];
            // si une valeur a été saisie
            if(current.getValue() != null){

               String operateur = "";
               // si la requete porte sur la 1er date de naissance
               if(current.getId().equals("dateCession1Box")){
                  int idx = 0;
                  isDateCession = true;

                  if(operateursDateCessionBox.getSelectedIndex() > 0){
                     idx = operateursDateCessionBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateCessionBox.getListModel().getElementAt(idx);
                  if(operateur.equals("[..]")){
                     operateur = ">=";
                  }
               }else if(current.getId().equals("dateCession2Box")){
                  isDateCession = true;

                  // si la requete porte sur la 2eme date de naissance
                  int idx = 0;
                  if(operateursDateCessionBox.getSelectedIndex() > 0){
                     idx = operateursDateCessionBox.getSelectedIndex();
                  }
                  operateur = (String) operateursDateCessionBox.getListModel().getElementAt(idx);

                  if(!operateur.equals("[..]")){
                     operateur = null;
                  }else{
                     operateur = "<=";
                  }
               }

               // exécution de la requête
               if(operateur != null){
                  executeSimpleQueryForCalendarbox(current, null, null, operateur);
               }

               oneValueEntered = true;
            }
         }
      }
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$deleteC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   /***********************************************************/
   /******************* GETTERS - SETTERS *********************/
   /***********************************************************/

   public List<CessionStatut> getCessionStatuts(){
      return cessionStatuts;
   }

   public void setCessionStatuts(final List<CessionStatut> c){
      this.cessionStatuts = c;
   }

   public List<CessionType> getCessionTypes(){
      return cessionTypes;
   }

   public void setCessionTypes(final List<CessionType> c){
      this.cessionTypes = c;
   }

   public List<Collaborateur> getDemandeurs(){
      return demandeurs;
   }

   public void setDemandeurs(final List<Collaborateur> d){
      this.demandeurs = d;
   }

   public List<Contrat> getContrats(){
      return contrats;
   }

   public void setContrats(final List<Contrat> c){
      this.contrats = c;
   }

   public List<CessionExamen> getCessionExamens(){
      return cessionExamens;
   }

   public void setCessionExamens(final List<CessionExamen> c){
      this.cessionExamens = c;
   }

   public List<DestructionMotif> getDestructions(){
      return destructions;
   }

   public void setDestructions(final List<DestructionMotif> d){
      this.destructions = d;
   }

   @Override
   public void setFocusOnElement(){}

   @Override
   public void setFieldsToUpperCase(){}

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

   public String getCessionDestructionLabel(){
      return Labels.getLabel("Champ.Cession.DepartDate") + "/" + Labels.getLabel("cession.type.destruction");
   }

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
