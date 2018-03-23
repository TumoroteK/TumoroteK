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
import java.util.Iterator;
import java.util.List;

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
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Dessine la fiche modale permettant à l'utilisateur de créer un
 * nouveau critère pour la recherche complexe
 * Date: 11/04/2011.
 *
 * 2.0.8 Ajout de l'etablissement preleveur
 *
 * @author Pierre VENTADOUR
 * @version 2.0.8
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
         // On cherche les ChampEntite de l'Entite.
         final List<ChampEntite> champsEntite = ManagerLocator.getChampEntiteManager().findByEntiteAndImportManager(entite, true);
         final Iterator<ChampEntite> itCE = champsEntite.iterator();
         while(itCE.hasNext()){
            final ChampEntite ce = itCE.next();
            if(!ce.getNom().equals(ce.getEntite().getNom() + "Id")){
               if(ce.getEntite().getNom().equals("Patient")){
                  champs.add(new ChampDecorator(new Champ(ce)));
               }else if(ce.getEntite().getNom().equals("Prelevement")){
                  champs.add(new ChampDecorator(new Champ(ce)));
               }else if(ce.getEntite().getNom().equals("Echantillon")){
                  if(!ce.getNom().equals("EmplacementId")){
                     champs.add(new ChampDecorator(new Champ(ce)));
                  }
               }else if(ce.getEntite().getNom().equals("ProdDerive")){
                  if(!ce.getNom().equals("EmplacementId")){
                     champs.add(new ChampDecorator(new Champ(ce)));
                  }
               }else{
                  champs.add(new ChampDecorator(new Champ(ce)));
               }
            }
         }

         addCustomChampToPrelevement(champs);

         // On cherche les ChampAnnotation de l'Entite.
         // Ajout des annotations
         final List<TableAnnotation> tas =
            ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(entite, banque);
         final List<ChampAnnotation> champsAnnotations = new ArrayList<>();
         for(int i = 0; i < tas.size(); i++){
            champsAnnotations.addAll(ManagerLocator.getChampAnnotationManager().findByTableManager(tas.get(i)));
         }
         for(int i = 0; i < champsAnnotations.size(); i++){
            champs.add(new ChampDecorator(new Champ(champsAnnotations.get(i))));
         }
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

   public void onSelect$champsBox(){
      DataType dataType = null;
      if(champsBox.getSelectedIndex() > 0){
         champ = champs.get(champsBox.getSelectedIndex()).getChamp();
         dataType = champ.dataType();

         boolean id = false;
         /** Si le champ est un identifiant, on remplit la liste de
          * sousChamps.
          */
         if(champ.getChampAnnotation() != null){
            if(champ.getChampAnnotation().getNom().matches("^[a-zA-Z]+Id$")){
               id = true;
            }
         }else if(champ.getChampEntite() != null){
            if(champ.getChampEntite().getNom().matches("^[a-zA-Z]+Id$")){
               id = true;
            }
         }

         if(id){
            this.rowOperateur.setVisible(false);
            this.operateursBox.setVisible(false);
            this.rowSousChamp.setVisible(true);
            this.sousChampsBox.setVisible(true);
            sousChamps = new ArrayList<>();
            sousChamps.add(null);
            sousChamp = null;

            /** On cherche l'entité correspondant au Champ */
            final String nomChampEntite = champ.getChampEntite().getNom();
            String nomEntite = null;
            if(champ.getChampEntite().getEntite().getNom().equals("Prelevement")){
               if(champ.getChampEntite().getNom().equals("OperateurId")){
                  nomEntite = "Collaborateur";
               }else if(champ.getChampEntite().getNom().equals("PreleveurId")){
                  nomEntite = "Collaborateur";
               }else if(champ.getChampEntite().getNom().equals("ServicePreleveurId")){
                  nomEntite = "Service";
               }else if(champ.getChampEntite().getNom().equals("QuantiteUniteId")){
                  nomEntite = "Unite";
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("Echantillon")){
               if(champ.getChampEntite().getNom().equals("QuantiteUniteId")){
                  nomEntite = "Unite";
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("ProdDerive")){
               if(champ.getChampEntite().getNom().equals("VolumeUniteId")){
                  nomEntite = "Unite";
               }else if(champ.getChampEntite().getNom().equals("QuantiteUniteId")){
                  nomEntite = "Unite";
               }else if(champ.getChampEntite().getNom().equals("ConcUniteId")){
                  nomEntite = "Unite";
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("Cession")){
               if(champ.getChampEntite().getNom().equals("DemandeurId")){
                  nomEntite = "Collaborateur";
               }else if(champ.getChampEntite().getNom().equals("DestinataireId")){
                  nomEntite = "Collaborateur";
               }else if(champ.getChampEntite().getNom().equals("ServiceDestId")){
                  nomEntite = "Service";
               }else if(champ.getChampEntite().getNom().equals("ExecutantId")){
                  nomEntite = "Collaborateur";
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("Service")){
               // hack Etablissement preleveur
               nomEntite = "Etablissement";
            }
            if(nomEntite == null){
               nomEntite = nomChampEntite.substring(0, nomChampEntite.length() - 2);
            }
            final List<Entite> ents = ManagerLocator.getEntiteManager().findByNomManager(nomEntite);
            Entite entite2 = null;
            if(ents.size() > 0){
               entite2 = ents.get(0);
            }

            /** On cherche les SousChamp du Champ. */
            final List<ChampEntite> champsEntite = ManagerLocator.getChampEntiteManager().findByEntiteManager(entite2);
            final Iterator<ChampEntite> itCE = champsEntite.iterator();
            while(itCE.hasNext()){
               final ChampEntite ce = itCE.next();
               if(ce != null){
                  if(!ce.getNom().matches("^[a-zA-Z]+Id$")){
                     final Champ sschp = new Champ(ce);
                     sschp.setChampParent(champ);
                     sousChamps.add(new ChampDecorator(sschp));
                  }
               }
            }

         }else{
            // si aucun champ n'est sélectionné, on cache les opérateurs.
            this.rowSousChamp.setVisible(false);
            this.sousChampsBox.setVisible(false);
            this.rowOperateur.setVisible(true);
            this.operateursBox.setVisible(true);

            operateurs = new ArrayList<>();
            operateurs.add(null);

            entite = entites.get(this.entitesBox.getSelectedIndex()).getEntite();

            /** On affiche les différents opérateurs selon le type. */
            if(dataType.getType() != null){
               if(dataType.getType().equals("alphanum") || dataType.getType().equals("thesaurus")
                  || dataType.getType().equals("boolean") || dataType.getType().equals("texte")
                  || dataType.getType().equals("thesaurusM")){
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
               // hack age au prélèvement
               if(champ.getChampEntite() != null && !champ.getChampEntite().getNom().equals("AgeAuPrelevement")){
                  operateurs.add(Labels.getLabel("critere.is.null"));
               }
            }
         }
      }else{
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

   /**
    * Ajoute un critère non existant dans la base à l'entité 
    * prélèvement.
    * @since 2.0.10
    */
   private void addCustomChampToPrelevement(final List<ChampDecorator> champs){
      if(entite.getNom().equals("Prelevement")){
         // Etablissement preleveur
         champs.add(6, new ChampDecorator(new Champ(ManagerLocator.getChampEntiteManager().findByIdManager(193))));

         // Age au prélèvement
         champs.add(8, new ChampDecorator(new Champ(ManagerLocator.getChampEntiteManager().findByIdManager(254))));
      }else if(entite.getNom().equals("Echantillon")){
         champs.add(5, new ChampDecorator(new Champ(ManagerLocator.getChampEntiteManager().findByIdManager(265))));
      }else if(entite.getNom().equals("ProdDerive")){
         champs.add(10, new ChampDecorator(new Champ(ManagerLocator.getChampEntiteManager().findByIdManager(266))));
      }
   }

}
