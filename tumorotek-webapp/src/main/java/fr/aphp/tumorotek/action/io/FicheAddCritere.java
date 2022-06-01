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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ChampDecorator;
import fr.aphp.tumorotek.decorator.CritereDecorator;
import fr.aphp.tumorotek.decorator.EntiteDecorator;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.AbstractTKChamp;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Dessine la fiche modale permettant à l'utilisateur de créer un
 * nouveau critère pour la recherche complexe
 * Date: 11/04/2011.
 *
 * 2.0.8 Ajout de l'etablissement preleveur
 *
 * @author Pierre VENTADOUR
 * @version 2.3.0-gatsbi
 *
 *
 */
public class FicheAddCritere extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 7394049019102721264L;

   private Panel winPanel;
   private Component parent;
   private Banque banque;
   private AnnotateDataBinder binder;

   // components
   private Row rowSousChamp;
   private Row rowChamp;
   private Row rowOperateur;
   private Row rowValeur;
   private Listbox entitesBox;
   private Listbox sousChampsBox;
   private Listbox champsBox;
   private Listbox operateursBox;
   private Button addCritere;

   // variables
   private List<EntiteDecorator> entites = new ArrayList<>();
   private List<ChampDecorator> sousChamps;
   private List<ChampDecorator> champs;
   private List<String> operateurs;
   private Entite entite;
   private Champ sousChamp;
   private Champ champ;
   private String operateur;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      if(winPanel != null){
         winPanel.setHeight("265px");
      }
   }

   public void init(final Component p, final Banque b){
      parent = p;
      banque = b;

      //On initialise la liste d'entités
      if(this.entites.isEmpty()){
         this.entites.add(null);
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Maladie").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0)));
         this.entites.add(new EntiteDecorator(ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0)));
      }

      this.entitesBox.setModel(new SimpleListModel<>(entites));
      this.champs = new ArrayList<>();
      this.champsBox.setModel(new SimpleListModel<>(champs));
      this.sousChamps = new ArrayList<>();
      this.sousChampsBox.setModel(new SimpleListModel<>(sousChamps));
      this.operateurs = new ArrayList<>();
      this.operateursBox.setModel(new SimpleListModel<>(operateurs));

      binder.loadComponent(self);
   }

   /**
    * Sélection de l'entité.
    */
   public void onSelect$entitesBox(){

      this.rowChamp.setVisible(true);
      this.champsBox.setVisible(true);

      champs = new ArrayList<>();
      if(this.entitesBox.getSelectedIndex() > 0){
         entite = entites.get(this.entitesBox.getSelectedIndex()).getEntite();

         // @since gatsbi
         // final Stream<ChampEntite> champEntiteStream =
         //   ManagerLocator.getManager(ChampEntiteManager.class).findByEntiteAndImportManager(entite, true).stream();
         final Stream<ChampEntite> champEntiteStream = 
       		  GatsbiController.findByEntiteImportAndIsNullableManager(entite, true, null).stream();
         final Stream<ChampEntite> customChampsEntiteStream = getCustomChampEntite().stream();

         //Ajout des champs entité
         Stream.concat(champEntiteStream, customChampsEntiteStream).filter(this::isNotExcluded).map(Champ::new)
            .map(ChampDecorator::new).forEach(champs::add);

         //Ajout des champs annotation
         ManagerLocator.getManager(TableAnnotationManager.class).findByEntiteAndBanqueManager(entite, banque).stream()
            .map(table -> ManagerLocator.getManager(ChampAnnotationManager.class).findByTableManager(table))
            .flatMap(listChampsAnnotation -> listChampsAnnotation.stream()).map(Champ::new).map(ChampDecorator::new)
            .forEach(champs::add);

         //Ajout des champs délégués
         ManagerLocator.getManager(ChampDelegueManager.class).findByEntiteAndContexte(entite, SessionUtils.getCurrentContexte())
            .stream().map(Champ::new).map(ChampDecorator::new).forEach(champs::add);

         champs.sort(Comparator.comparing(ChampDecorator::getLabel));

      }else{
         /** si aucune entité n'est sélectionnée, on cache les champs. */
         this.champsBox.setVisible(false);
         this.rowChamp.setVisible(false);
      }
      champs.add(0, null);

      /** On ajoute les données dans la liste affichable. */
      this.champsBox.setModel(new SimpleListModel<>(champs));
      this.champ = null;

      /** On cache les autres lignes. */
      this.sousChampsBox.setVisible(false);
      this.rowSousChamp.setVisible(false);
      sousChamp = null;
      this.operateursBox.setVisible(false);
      this.rowOperateur.setVisible(false);
      operateur = null;
      this.rowValeur.setVisible(false);
      this.addCritere.setVisible(false);
   }

   /**
    * Afficher les opérateurs selon le champ
    * @param champ champ
    */
   private void afficherOperateurs(final Champ champ){
      if(null != champ && null != champ.dataType() && null != champ.dataType().getType()){
         if("calcule".equals(champ.dataType().getType())){
            afficherOperateurs(champ.getChampAnnotation().getChampCalcule().getDataType());
         }else{
            afficherOperateurs(champ.dataType());
         }
      }
   }

   /**
    * Afficher le sopérateurs selon le datatype du champ
    * @param dataType datatype
    */
   private void afficherOperateurs(final DataType dataType){
      operateurs.add("=");
      operateurs.add("!=");

      switch(dataType.getType()){
         case "alphanum":
         case "thesaurus":
         case "texte":
         case "thesaurusM":
            operateurs.add("like");
            operateurs.add("not like");
            break;
         case "date":
         case "datetime":
         case "num":
         case "duree":
            operateurs.add("<");
            operateurs.add("<=");
            operateurs.add(">");
            operateurs.add(">=");
            break;
         default:
            break;
      }

      // hack age au prélèvement
      if(champ.getChampEntite() != null && !champ.getChampEntite().getNom().equals("AgeAuPrelevement")){
         operateurs.add(Labels.getLabel("critere.is.null"));
      }
   }

   /**
    * Renvoie true si le champ entité passé en paramètre est exclu des critères de recherche
    * @param champEntite
    * @return
    */
   private boolean isNotExcluded(final ChampEntite champEntite){

      final Map<String, List<String>> excludedChampsMap = new HashMap<>();
      excludedChampsMap.put("Echantillon", Arrays.asList(new String[] {"EmplacementId"}));
      excludedChampsMap.put("ProdDerive", Arrays.asList(new String[] {"EmplacementId"}));

      //Le champ id de l'entité courante est systématiquement exclu (e.g. : PatientId pour Patient)
      boolean excluded = champEntite.getNom().equals(champEntite.getEntite().getNom() + "Id");

      final List<String> excludedChampsForEntite = excludedChampsMap.get(champEntite.getEntite().getNom());

      //Sinon, on regarde si le champ est dans la liste des champs exclus pour l'entité
      if(!excluded && excludedChampsForEntite != null){
         excluded = excludedChampsForEntite.contains(champEntite.getNom());
      }

      return !excluded;

   }

   public void onSelect$champsBox(){

      if(champsBox.getSelectedIndex() > 0){

         champ = champs.get(champsBox.getSelectedIndex()).getChamp();

         String nomChamp = null;
         if(champ.getChampAnnotation() != null){
            nomChamp = champ.getChampAnnotation().getNom();
         }else if(champ.getChampEntite() != null){
            nomChamp = champ.getChampEntite().getNom();
         }else if(champ.getChampDelegue() != null){
            nomChamp = champ.getChampDelegue().getNom();
         }

         //Si le champ est un identifiant, on alimente la liste de sousChamps.
         if(nomChamp.matches("^[a-zA-Z]+Id$")){

            this.rowOperateur.setVisible(false);
            this.operateursBox.setVisible(false);
            this.rowSousChamp.setVisible(true);
            this.sousChampsBox.setVisible(true);

            sousChamps = new ArrayList<>();
            sousChamp = null;

            final String nomEntiteChamp = champ.entite().getNom();
            String nomEntiteReel = null;

            //Cas particuliers pour lesquels le nom du champ ne correspond pas au nom de l'entité
            if("Prelevement".equals(nomEntiteChamp)){
               if("OperateurId".equals(nomChamp)){
                  nomEntiteReel = "Collaborateur";
               }else if("PreleveurId".equals(nomChamp)){
                  nomEntiteReel = "Collaborateur";
               }else if("ServicePreleveurId".equals(nomChamp)){
                  nomEntiteReel = "Service";
               }else if("QuantiteUniteId".equals(nomChamp)){
                  nomEntiteReel = "Unite";
               }
//               else if("Protocoles".equals(nomChamp)){
//                  nomEntiteReel = "Protocole";
//               }
            }else if("Echantillon".equals(nomEntiteChamp)){
               if("QuantiteUniteId".equals(nomChamp)){
                  nomEntiteReel = "Unite";
               }
            }else if("ProdDerive".equals(nomEntiteChamp)){
               if("VolumeUniteId".equals(nomChamp)){
                  nomEntiteReel = "Unite";
               }else if("QuantiteUniteId".equals(nomChamp)){
                  nomEntiteReel = "Unite";
               }else if("ConcUniteId".equals(nomChamp)){
                  nomEntiteReel = "Unite";
               }
            }else if("Cession".equals(nomEntiteChamp)){
               if("DemandeurId".equals(nomChamp)){
                  nomEntiteReel = "Collaborateur";
               }else if("DestinataireId".equals(nomChamp)){
                  nomEntiteReel = "Collaborateur";
               }else if("ServiceDestId".equals(nomChamp)){
                  nomEntiteReel = "Service";
               }else if("ExecutantId".equals(nomChamp)){
                  nomEntiteReel = "Collaborateur";
               }
            }else if("Service".equals(nomEntiteChamp)){
               // hack Etablissement preleveur
               nomEntiteReel = "Etablissement";
            }else if(champ.getChampAnnotation() != null || champ.getChampDelegue() != null
               || (champ.getChampEntite() != null && !"AgeAuPrelevement".equals(nomChamp))){
               // hack age au prélèvement
               operateurs.add(Labels.getLabel("critere.is.null"));
            }

            //Si on n'est pas dans un cas particulier, alors le nom de l'entité est celui du champ (moins le suffixe "Id")
            if(nomEntiteReel == null){
               nomEntiteReel = nomChamp.replaceAll("Id$", "");
            }

            final List<Entite> ents = ManagerLocator.getEntiteManager().findByNomManager(nomEntiteReel);
            Entite entite2 = null;
            if(ents.size() > 0){
               entite2 = ents.get(0);
            }

            /** On cherche les SousChamp du Champ. */
            final Stream<? extends AbstractTKChamp> champsEntitesStream =
               ManagerLocator.getManager(ChampEntiteManager.class).findByEntiteManager(entite2).stream();
            final Stream<? extends AbstractTKChamp> champsDeleguesStream = ManagerLocator.getManager(ChampDelegueManager.class)
               .findByEntiteAndContexte(entite2, SessionUtils.getCurrentContexte()).stream();

            Stream.concat(champsEntitesStream, champsDeleguesStream).filter(c -> !c.getNom().matches("^[a-zA-Z]+Id$"))
               .map(c -> new Champ(c, champ)).map(ChampDecorator::new).forEach(sousChamps::add);

            sousChamps.sort(Comparator.comparing(ChampDecorator::getLabelLong));

            sousChamps.add(0, null);

         }else{
            this.rowSousChamp.setVisible(false);
            this.sousChampsBox.setVisible(false);
            this.rowOperateur.setVisible(true);
            this.operateursBox.setVisible(true);

            operateurs = new ArrayList<>();
            operateurs.add(null);

            entite = entites.get(this.entitesBox.getSelectedIndex()).getEntite();

            afficherOperateurs(champ);

         }
      }

      else{
         /** si aucun champ n'est sélectionné, on cache les opérateurs. */
         this.operateursBox.setVisible(false);
         this.rowOperateur.setVisible(false);
      }

      /** On ajoute les données dans la liste affichable. */
      this.sousChampsBox.setModel(new SimpleListModel<>(sousChamps));
      sousChamp = null;
      /** On ajoute les données dans la liste affichable. */
      this.operateursBox.setModel(new SimpleListModel<>(operateurs));
      operateur = null;

      /** On cache les autres lignes. */
      this.rowValeur.setVisible(false);
      this.addCritere.setVisible(false);
   }

   public void onSelect$sousChampsBox(){

      DataType dataType = null;
      if(sousChampsBox.getSelectedIndex() > 0){
         sousChamp = sousChamps.get(sousChampsBox.getSelectedIndex()).getChamp();
         dataType = sousChamp.dataType();

         this.rowOperateur.setVisible(true);
         this.operateursBox.setVisible(true);

         operateurs = new ArrayList<>();
         operateurs.add(null);

         //entite = entites.get(this.entitesBox.getSelectedIndex());

         /** On affiche les différents opérateurs selon le type. */
         if(dataType.getType() != null){
            if(dataType.getType().equals("alphanum") || dataType.getType().equals("thesaurus")
               || dataType.getType().equals("boolean") || dataType.getType().equals("texte")
               || dataType.getType().equals("thesaurusM") || dataType.getType().equals("hyperlien")){
               operateurs.add("=");
               operateurs.add("!=");
               if(dataType.getType().equals("alphanum") || dataType.getType().equals("thesaurus")
                  || dataType.getType().equals("texte") || dataType.getType().equals("thesaurusM")){
                  operateurs.add("like");
                  operateurs.add("not like");
               }
            }else if(dataType.getType().matches("date.*") || dataType.getType().equals("num")){
               operateurs.add("=");
               operateurs.add("!=");
               operateurs.add("<");
               operateurs.add("<=");
               operateurs.add(">");
               operateurs.add(">=");
            }
            operateurs.add(Labels.getLabel("critere.is.null"));
         }
      }else{
         // si aucun sousChamp n'est sélectionné, on cache les opérateurs.
         this.operateursBox.setVisible(false);
         this.rowOperateur.setVisible(false);
      }

      /** On ajoute les données dans la liste affichable. */
      this.operateursBox.setModel(new SimpleListModel<>(operateurs));
      operateur = null;

      /** On cache les autres lignes. */
      this.rowValeur.setVisible(false);
      this.addCritere.setVisible(false);
   }

   public void onSelect$operateursBox(){
      if(operateursBox.getSelectedIndex() > 0){
         this.operateur = operateurs.get(operateursBox.getSelectedIndex());
         this.rowValeur.setVisible(true);
         this.addCritere.setVisible(true);
      }else{
         /** si aucun opérateur n'est sélectionné, on cache la valeur. */
         this.rowValeur.setVisible(false);
         this.addCritere.setVisible(false);
      }
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$addCritere(){
      /** On ajoute les données dans la liste affichable. */
      CritereDecorator cd = null;

      String value = "";
      if(!operateur.equals(Labels.getLabel("critere.is.null"))){
         value = "XXX";
      }else{
         operateur = "is null";
      }

      if(sousChamp != null){
         cd = new CritereDecorator(new Critere(sousChamp, operateur, value));
      }else{
         cd = new CritereDecorator(new Critere(champ, operateur, value));
      }

      Events.postEvent("onGetCritere", getParent(), cd);
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /**
    * Renvoie une liste de champs entités à ajouter aux champs existants
    * @return
    */
   private List<ChampEntite> getCustomChampEntite(){

      final List<ChampEntite> customChampsEntite = new ArrayList<>();

      //On utilise directement les id car il peut potentiellement exister plusieurs ChampEntite de même nom
      //pour une même entité. TODO Ajouter une contrainte d'unicité sur nom + entité pour les champs entité ?
      switch(entite.getNom()){
         case "Prelevement":
            //EtablissementId
        	// since 2.3.0-gatsbi etablissement depend de service preleveur
            if (SessionUtils.getCurrentGatsbiContexteForEntiteId(entite.getEntiteId()) == null 
             		|| SessionUtils.getCurrentGatsbiContexteForEntiteId(entite.getEntiteId()).isChampIdVisible(29)) {
            	customChampsEntite.add(ManagerLocator.getManager(ChampEntiteManager.class).findByIdManager(193));
            }
            //Age au prélèvement
            customChampsEntite.add(ManagerLocator.getManager(ChampEntiteManager.class).findByIdManager(254));
            break;
         case "Echantillon":
            //TempStock
            customChampsEntite.add(ManagerLocator.getChampEntiteManager().findByIdManager(265));
            break;
         case "ProdDerive":
            //TempStock
            customChampsEntite.add(ManagerLocator.getChampEntiteManager().findByIdManager(266));
            break;
      }

      return customChampsEntite;

   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<EntiteDecorator> getEntites(){
      return entites;
   }

   public void setEntites(final List<EntiteDecorator> e){
      this.entites = e;
   }

   public List<ChampDecorator> getSousChamps(){
      return sousChamps;
   }

   public void setSousChamps(final List<ChampDecorator> s){
      this.sousChamps = s;
   }

   public List<ChampDecorator> getChamps(){
      return champs;
   }

   public void setChamps(final List<ChampDecorator> c){
      this.champs = c;
   }

   public List<String> getOperateurs(){
      return operateurs;
   }

   public void setOperateurs(final List<String> o){
      this.operateurs = o;
   }

   public Entite getEntite(){
      return entite;
   }

   public void setEntite(final Entite e){
      this.entite = e;
   }

   public Champ getSousChamp(){
      return sousChamp;
   }

   public void setSousChamp(final Champ s){
      this.sousChamp = s;
   }

   public Champ getChamp(){
      return champ;
   }

   public void setChamp(final Champ c){
      this.champ = c;
   }

   public String getOperateur(){
      return operateur;
   }

   public void setOperateur(final String o){
      this.operateur = o;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

}
