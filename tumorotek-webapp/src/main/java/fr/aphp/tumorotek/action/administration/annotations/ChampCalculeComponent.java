/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action.administration.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Text;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.io.FicheAffichage;
import fr.aphp.tumorotek.action.io.FicheChampsAffichageModale;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.utils.Duree;

/**
 * MacroComponent dessinant les composant editables permettant à
 * l'utilisateur de renseigner un champ calculé.
 * Classe créée le 20/02/2018
 *
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 *
 */
public class ChampCalculeComponent extends Div
{

   /**
    *
    */
   private static final long serialVersionUID = -1054789694219857585L;

   /**
    * ChampCalculé lié au composant
    */
   private ChampCalcule champCalcule = new ChampCalcule();

   /**
    * Banque utilisée lors de la récupération des champs sélectionnables sur la {@link FicheChampsAffichageModale}
    */
   private Banque banque;

   /**
    * DataTypes filtres sur les champs sélectionnables appliqués sur la {@link FicheChampsAffichageModale}
    */
   private List<DataType> allowedDataTypeList = new ArrayList<>();

   /**
    * le champ reçu après sélection sur la modale {@link FicheChampsAffichageModale}
    */
   private Champ selectedChamp;

   private final Div description = new Div();

   private final Grid grid = new Grid();

   private final Columns cols = new Columns();

   private final Column champ1Col = new Column(Labels.getLabel("annotation.champCalcule.champ1"));

   private final Column operateurCol = new Column(Labels.getLabel("annotation.champCalcule.operateur"));

   private final Column champ2Col = new Column(Labels.getLabel("annotation.champCalcule.champ2"));

   private final Column valeurCol = new Column(Labels.getLabel("annotation.champCalcule.valeur"));

   private final Column dureeCol = new Column(Labels.getLabel("annotation.champCalcule.duree"));

   private final Rows rows = new Rows();

   private final Row row = new Row();

   private final Cell champ1Cell = new Cell();

   private final Text champ1Value = new Text(Labels.getLabel("annotation.champCalcule.selectionner"));

   private final Cell operateurCell = new Cell();

   private final ListModelList<String> lm = new ListModelList<>(Arrays.asList("+", "-"));

   private final Selectbox operateurBox = new Selectbox();

   private final Cell champ2Cell = new Cell();

   private final Text champ2Value = new Text(Labels.getLabel("annotation.champCalcule.selectionner"));

   private final Textbox valeurCell = new Textbox();

   DureeComponent dureeCell = new DureeComponent();

   /**
    * Initialise le composant ChampCalculé
    */
   public ChampCalculeComponent(){
      super();
      buildComponent();
   }

   /**
    * Initialise le composant ChampCalculé en le liant avec le champCalcule passé en paramètre
    */
   public ChampCalculeComponent(final ChampCalcule champCalcule){
      super();
      this.setChampCalcule(champCalcule);
   }

   /**
    * Création du squelette d'affichage du composent
    */
   private void buildComponent(){
      final String descriptionLabel = Labels.getLabel("annotation.champCalcule.description");

      final Html html = new Html();
      html.setContent(descriptionLabel);
      description.setStyle("font-family: arial, sans-serif");
      description.appendChild(html);

      this.appendChild(description);

      //Paramétrage grid
      grid.setSclass("gridListStyle");

      this.setWidth("1000px");
      this.appendChild(grid);
      grid.appendChild(cols);
      cols.appendChild(champ1Col);
      cols.appendChild(operateurCol);
      cols.appendChild(champ2Col);
      cols.appendChild(valeurCol);
      cols.appendChild(dureeCol);

      champ1Cell.appendChild(champ1Value);
      champ1Cell.setClass("formLink");
      operateurCell.appendChild(operateurBox);
      champ2Cell.appendChild(champ2Value);
      champ2Cell.setClass("formLink");

      valeurCell.setId("valeurCell");

      grid.appendChild(rows);
      rows.appendChild(row);
      row.appendChild(champ1Cell);
      row.appendChild(operateurCell);
      row.appendChild(champ2Cell);
      row.appendChild(valeurCell);
      row.appendChild(dureeCell);
      operateurBox.setModel(lm);

      addEventListeners();

      adaptColumns();
   }

   /**
    * Au click sur le champ1, on ouvre la fenêtre de sélection de champ, restreint à certains datatypes
    */
   public void onClick$champ1(){
      this.setWidth("100%");
      selectedChamp = this.champCalcule.getChamp1();
      // On affiche la fenêtre de sélection de champs uniquement pour les datatypes avec opération possible
      final List<DataType> dataTypes =
         ManagerLocator.getDataTypeManager().findByTypesManager(Arrays.asList("num", "date", "datetime", "duree"));

      new FicheAffichage().openChampsAffichageWindow(this.getPage(), this, new ArrayList<Champ>(), banque, false, dataTypes,
         true);
      //Evenement pour rafraichir l'affichage
      Events.echoEvent("onChange$Champ1", this, null);
   }

   /**
    * Au click sur le champ2, on ouvre la fenêtre de sélection de champ, restreint à certains datatypes (en fonction du champ1 et de l'opérateur)
    */
   public void onClick$champ2(){
      selectedChamp = this.champCalcule.getChamp2();
      // On affiche la fenêtre de sélection de champs
      new FicheAffichage().openChampsAffichageWindow(this.getPage(), this, new ArrayList<Champ>(), banque, false,
         allowedDataTypeList, true);
      //Evenement pour rafraichir l'affichage
      Events.echoEvent("onChange$Champ2", this, null);
   }

   /**
    * A la sélection du champ1 on met tout le reste à zéro et on attend la sélection de l'opérateur
    */
   public void onChange$Champ1(){
      if(null != selectedChamp){
         champ1Value.setValue(ObjectTypesFormatters.formatChampLabel(selectedChamp, true, "-"));
         champCalcule.setChamp1(selectedChamp);
      }else{
         champ1Value.setValue(Labels.getLabel("annotation.champCalcule.selectionner"));
         champCalcule.setChamp1(null);
      }
      champCalcule.setOperateur(null);
      operateurBox.setSelectedIndex(-1);
      lm.clearSelection();
      champCalcule.setChamp2(null);
      champCalcule.setValeur(null);
      valeurCell.setValue(null);
      allowedDataTypeList = new ArrayList<>();

      adaptColumns();
   }

   /**
    * A la sélection du champ2
    */
   public void onChange$Champ2(){
      if(null != selectedChamp){
         champ2Value.setValue(ObjectTypesFormatters.formatChampLabel(selectedChamp, true, "-"));
         champCalcule.setChamp2(selectedChamp);
      }else{
         champ2Value.setValue(Labels.getLabel("annotation.champCalcule.selectionner"));
         champCalcule.setChamp2(null);
      }

      valeurCell.setValue(null);
      champCalcule.setValeur(null);
      dureeCell.setDuree(null);

      adaptColumns();
   }

   /**
    * A la sélection d'un opérateur on restreint les champs champ2 en fonction du type de champ1.
    */
   public void onSelect$operateur(){
      if(-1 != operateurBox.getSelectedIndex()){
         champCalcule.setOperateur(lm.getElementAt(operateurBox.getSelectedIndex()));
      }

      generateAllowedDataTypeList();

      champCalcule.setChamp2(null);
      valeurCell.setValue(null);
      champCalcule.setValeur(null);
      dureeCell.setDuree(null);

      adaptColumns();
   }

   /**
    * Génère la liste des datatypes autorisés pour la sélection du champ2 selon le champ1
    */
   private void generateAllowedDataTypeList(){
      if(null != champCalcule.getChamp1() && null != champCalcule.getOperateur()){
         generateAllowedDataTypeList(champCalcule);
      }
   }

   private void generateAllowedDataTypeList(final ChampCalcule champCalcule){
      final DataType champ1DataType = champCalcule.getChamp1().dataType();

      if("calcule".equals(champ1DataType.getType())){
         generateAllowedDataTypeList(champCalcule.getChamp1().getChampAnnotation().getChampCalcule());
      }else{
         generateAllowedDataTypeList(champ1DataType);
      }
   }

   private void generateAllowedDataTypeList(final DataType dataType){
      switch(dataType.getType()){
         case "num":
            allowedDataTypeList = Arrays.asList(ManagerLocator.getDataTypeManager().findByTypeManager("num"));
            break;
         case "duree":
            allowedDataTypeList = Arrays.asList(ManagerLocator.getDataTypeManager().findByTypeManager("duree"));
            break;
         case "date":
         case "datetime":
            if("+".equals(champCalcule.getOperateur())){
               allowedDataTypeList = Arrays.asList(ManagerLocator.getDataTypeManager().findByTypeManager("duree"));
            }else if("-".equals(champCalcule.getOperateur())){
               allowedDataTypeList =
                  ManagerLocator.getDataTypeManager().findByTypesManager(Arrays.asList("date", "datetime", "duree"));
            }
            break;
         default:
            break;
      }
   }

   /**
    * Méthode listener appelée une fois que l'utilisateur a choisi le champ depuis la ficheChampsAffichage
    * à afficher.
    * @param event provient de la modale FicheChampsAffichageModale qui envoie un evènement "onGetChamp" contenant le champ sélectionné
    */
   public void onGetChamps(final Event event){
      if(event.getData() != null){
         final List<?> champList = List.class.cast(event.getData());
         if(!champList.isEmpty()){
            final Champ currChamp = Champ.class.cast(champList.get(0));
            Champ sousChampTmp = null;

            if(currChamp.getChampEntite() != null && currChamp.getChampEntite().getQueryChamp() != null){
               sousChampTmp = new Champ(currChamp.getChampEntite().getQueryChamp());
               sousChampTmp.setChampParent(currChamp);
            }

            if(sousChampTmp != null){
               selectedChamp = sousChampTmp;
            }else{
               selectedChamp = currChamp;
            }
         }else{
            selectedChamp = null;
         }
      }
   }

   /**
    * Ajoute les listeners permettant les actions sur les différentes parties du composant
    */
   private void addEventListeners(){
      // Définition des évènements
      champ1Cell.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClick$champ1();
         }
      });

      champ2Cell.addEventListener("onClick", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onClick$champ2();
         }
      });

      operateurBox.addEventListener("onSelect", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onSelect$operateur();
         }
      });

      this.addEventListener("onChange$Champ1", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onChange$Champ1();
         }
      });

      this.addEventListener("onChange$Champ2", new EventListener<Event>()
      {
         @Override
         public void onEvent(final Event event) throws Exception{
            onChange$Champ2();
         }
      });
   }

   /**
    * Gère l'affichage des colonnes selon les champs renseignés
    */
   private void adaptColumns(){
      operateurCol.setVisible(false);
      champ2Col.setVisible(false);
      valeurCol.setVisible(false);
      dureeCol.setVisible(false);
      if(null != champCalcule && null != champCalcule.getChamp1()){
         operateurCol.setVisible(true);
         if(null != champCalcule.getOperateur() && !"".equals(champCalcule.getOperateur())){
            champ2Col.setVisible(true);
            valeurCol.setVisible(false);
            dureeCol.setVisible(false);
            if(null != champCalcule.getChamp2()){
               valeurCol.setVisible(false);
               dureeCol.setVisible(false);
            }else{
               if("num".equals(champCalcule.getChamp1().dataType().getType())){
                  valeurCol.setVisible(true);
               }else if("duree".equals(champCalcule.getChamp1().dataType().getType())){
                  dureeCol.setVisible(true);
               }else if("date".equals(champCalcule.getChamp1().dataType().getType())
                  || "datetime".equals(champCalcule.getChamp1().dataType().getType())){
                  if("+".equals(champCalcule.getOperateur())){
                     dureeCol.setVisible(true);
                  }else if("-".equals(champCalcule.getOperateur())){
                     dureeCol.setVisible(true);
                  }
               }
            }
         }
      }
   }

   /**
    * Rempli le composant avec les valeurs du champCalcule
    */
   private void fillupComponent(){
      if(null != champCalcule){
         if(null != champCalcule.getChamp1()){
            champ1Value.setValue(ObjectTypesFormatters.formatChampLabel(champCalcule.getChamp1(), true, "-"));
         }
         if(null != champCalcule.getChamp2()){
            champ2Value.setValue(ObjectTypesFormatters.formatChampLabel(champCalcule.getChamp2(), true, "-"));
         }
         if(null != champCalcule.getOperateur()){
            for(int i = 0; i < lm.getSize(); i++){
               if(lm.getElementAt(i).equals(champCalcule.getOperateur())){
                  operateurBox.setSelectedIndex(i);
                  lm.clearSelection();
                  lm.addToSelection(lm.getElementAt(i));
                  operateurBox.setVisible(false);
                  operateurBox.setVisible(true);
               }
            }
         }
         if(null != champCalcule.getDataType() && null != champCalcule.getValeur() && !"".equals(champCalcule.getValeur())){
            final String dataType = champCalcule.getDataType().getType();
            if("duree".equals(dataType) || "date".equals(dataType) || "datetime".equals(dataType)){
               dureeCell.setDuree(new Duree(new Long(champCalcule.getValeur()), Duree.SECONDE));
            }else{
               valeurCell.setValue(champCalcule.getValeur());
            }
         }
      }
      adaptColumns();
      generateAllowedDataTypeList();
   }

   /**
    * Rempli le champCalcule avec les valeurs du composant
    */
   private void fillupChampCalcule(){
      if(null != champCalcule.getChamp1() && null != champCalcule.getChamp1().dataType()){
         if(null != valeurCell.getValue() && !"".equals(valeurCell.getValue())){
            champCalcule.setValeur(valeurCell.getValue());
         }
         if(null != dureeCell.getDuree() && 0 != dureeCell.getDuree().getTemps(Duree.SECONDE)){
            champCalcule.setValeur(dureeCell.getDuree().getTemps(Duree.SECONDE).toString());
         }
         generateChampCalculeType(champCalcule);
      }
   }

   private void generateChampCalculeType(final ChampCalcule champCalcule){
      if("calcule".equals(champCalcule.getChamp1().dataType().getType())){
         generateChampCalculeType(champCalcule.getChamp1().getChampAnnotation().getChampCalcule());
      }else{
         generateChampCalculeType(champCalcule.getChamp1(), champCalcule.getChamp2(), champCalcule.getValeur(),
            champCalcule.getOperateur());
      }
   }

   private void generateChampCalculeType(final Champ champ1, final Champ champ2, final String valeur, final String operateur){
      final String champ1Type = champ1.dataType().getType();
      String champ2Type = null;
      if(null != champ2){
         champ2Type = champ2.dataType().getType();
      }
      switch(champ1Type){
         case "num":
            champCalcule.setDataType(ManagerLocator.getDataTypeManager().findByTypeManager("num"));
            break;
         case "duree":
            champCalcule.setDataType(ManagerLocator.getDataTypeManager().findByTypeManager("duree"));
            break;
         case "date":
         case "datetime":
            if("+".equals(operateur)){
               champCalcule.setDataType(ManagerLocator.getDataTypeManager().findByTypeManager("datetime"));
            }else if("-".equals(operateur)){
               if("date".equals(champ2Type) || "datetime".equals(champ2Type)){
                  champCalcule.setDataType(ManagerLocator.getDataTypeManager().findByTypeManager("duree"));
               }else if(null != valeur || "num".equals(champ2Type)){
                  champCalcule.setDataType(ManagerLocator.getDataTypeManager().findByTypeManager("datetime"));
               }
            }
            break;
         default:
            break;
      }
   }

   /**
    * Retourne un champ calculé en fonction des valeurs du composents (saisies utilisateur)
    * @return champCalcule
    */
   public ChampCalcule getChampCalcule(){
      fillupChampCalcule();
      return champCalcule;
   }

   /**
    * Rempli le composent selon les propriétés du champCalcule
    * @param champCalcule le champ calculé
    */
   public void setChampCalcule(final ChampCalcule champCalcule){
      this.champCalcule = champCalcule;
      fillupComponent();
   }

   /**
    * Banque utilisée pour la sélection de champs
    * @param banque banque actuelle
    */
   public void setBanque(final Banque banque){
      this.banque = banque;
   }
}
