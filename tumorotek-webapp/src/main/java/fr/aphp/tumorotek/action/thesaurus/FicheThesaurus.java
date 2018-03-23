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
package fr.aphp.tumorotek.action.thesaurus;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.CrudManager;
import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.manager.context.CategorieManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.EnceinteType;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

public class FicheThesaurus extends AbstractFicheCombineController
{

   // private Log log = LogFactory.getLog(FicheThesaurus.class);

   private static final long serialVersionUID = -7373242015410721878L;

   private Button addNewValeur;
   private Grid gridValeurs;
   private Grid valeursListGrid;
   /**
    * Objets principaux.
    */
   private Thesaurus typeThesaurus;
   private boolean isAdmin = false;
   private ThesaurusRowRenderer listValeursRenderer = new ThesaurusRowRenderer();
   private CrudManager thesManager;
   private List<? extends Object> listValeurs = new ArrayList<>();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // Initialisation des listes de composants
      setObjLabelsComponents(new Component[] {});

      setObjBoxsComponents(new Component[] {});

      setRequiredMarks(new Component[] {});

      drawActionsForThesaurus();

      addNewValeur.setVisible(false);

      if(winPanel != null){
         winPanel.setHeight(getMainWindow().getPanelHeight() - 5 + "px");
      }

      getBinder().loadAll();

   }

   @Override
   public void switchToStaticMode(){
      super.switchToStaticMode(true);

      editC.setVisible(false);
      deleteC.setVisible(false);
      createC.setVisible(false);
      addNewC.setVisible(false);
      addNewValeur.setVisible(true);

      getBinder().loadComponent(self);
   }

   @Override
   public void cloneObject(){}

   @Override
   public void clearData(){
      super.clearData();
   }

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
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setObject(final TKdataObject obj){
      this.typeThesaurus = (Thesaurus) obj;

      super.setObject(typeThesaurus);

      winPanel.setTitle(ObjectTypesFormatters.getLabel("thesaurus.fiche.title", new String[] {typeThesaurus.getLabel()}));

      initListeValeurs(true);
   }

   /**
    * Initialise la liste des objets du thésaurus à afficher.
    * @param reset si manager change
    */
   public void initListeValeurs(final boolean reset){
      listValeurs = new ArrayList<>();
      if(reset){
         thesManager = null;
         if(typeThesaurus.getNom().equals("Nature")){
            thesManager = ManagerLocator.getNatureManager();
         }else if(typeThesaurus.getNom().equals("PrelevementType")){
            thesManager = ManagerLocator.getPrelevementTypeManager();
         }else if(typeThesaurus.getNom().equals("EchantillonType")){
            thesManager = ManagerLocator.getEchantillonTypeManager();
         }else if(typeThesaurus.getNom().equals("EchanQualite")){
            thesManager = ManagerLocator.getEchanQualiteManager();
         }else if(typeThesaurus.getNom().equals("ProdType")){
            thesManager = ManagerLocator.getProdTypeManager();
         }else if(typeThesaurus.getNom().equals("ProdQualite")){
            thesManager = ManagerLocator.getProdQualiteManager();
         }else if(typeThesaurus.getNom().equals("ConditType")){
            thesManager = ManagerLocator.getConditTypeManager();
         }else if(typeThesaurus.getNom().equals("ConditMilieu")){
            thesManager = ManagerLocator.getConditMilieuManager();
         }else if(typeThesaurus.getNom().equals("ConsentType")){
            thesManager = ManagerLocator.getConsentTypeManager();
         }else if(typeThesaurus.getNom().equals("Risque")){
            thesManager = ManagerLocator.getRisqueManager();
         }else if(typeThesaurus.getNom().equals("ModePrepa")){
            thesManager = ManagerLocator.getModePrepaManager();
         }else if(typeThesaurus.getNom().equals("ModePrepaDerive")){
            thesManager = ManagerLocator.getModePrepaDeriveManager();
         }else if(typeThesaurus.getNom().equals("CessionExamen")){
            thesManager = ManagerLocator.getCessionExamenManager();
         }else if(typeThesaurus.getNom().equals("DestructionMotif")){
            thesManager = ManagerLocator.getDestructionMotifManager();
         }else if(typeThesaurus.getNom().equals("ProtocoleType")){
            thesManager = ManagerLocator.getProtocoleTypeManager();
         }else if(typeThesaurus.getNom().equals("Specialite")){
            thesManager = ManagerLocator.getSpecialiteManager();
         }else if(typeThesaurus.getNom().equals("Categorie")){
            thesManager = ManagerLocator.getCategorieManager();
         }else if(typeThesaurus.getNom().equals("ConteneurType")){
            thesManager = ManagerLocator.getConteneurTypeManager();
         }else if(typeThesaurus.getNom().equals("EnceinteType")){
            thesManager = ManagerLocator.getEnceinteTypeManager();
         }else if(typeThesaurus.getNom().equals("Protocole")){
            thesManager = ManagerLocator.getProtocoleManager();
         }
      }

      // si c'est un thes de non conformité
      if(thesManager == null){
         if(typeThesaurus.getNom().equals("NonConformiteArrivee")){
            listValeurs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
               SessionUtils.getPlateforme(sessionScope), "Arrivee", ManagerLocator.getEntiteManager().findByIdManager(2));
         }else if(typeThesaurus.getNom().equals("NonConformiteTraitementEchan")){
            listValeurs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
               SessionUtils.getPlateforme(sessionScope), "Traitement", ManagerLocator.getEntiteManager().findByIdManager(3));
         }else if(typeThesaurus.getNom().equals("NonConformiteCessionEchan")){
            listValeurs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
               SessionUtils.getPlateforme(sessionScope), "Cession", ManagerLocator.getEntiteManager().findByIdManager(3));
         }else if(typeThesaurus.getNom().equals("NonConformiteTraitementDerive")){
            listValeurs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
               SessionUtils.getPlateforme(sessionScope), "Traitement", ManagerLocator.getEntiteManager().findByIdManager(8));
         }else if(typeThesaurus.getNom().equals("NonConformiteCessionDerive")){
            listValeurs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
               SessionUtils.getPlateforme(sessionScope), "Cession", ManagerLocator.getEntiteManager().findByIdManager(8));
         }
      }else if(thesManager instanceof TKThesaurusManager){
         listValeurs = ((TKThesaurusManager) thesManager).findByOrderManager(SessionUtils.getPlateforme(sessionScope));
      }else{
         if(typeThesaurus.getNom().equals("Specialite")){
            listValeurs = ((SpecialiteManager) thesManager).findAllObjectsManager();
         }else if(typeThesaurus.getNom().equals("Categorie")){
            listValeurs = ((CategorieManager) thesManager).findAllObjectsManager();
         }
      }

      getBinder().loadAttribute(valeursListGrid, "model");
      getBinder().loadComponent(valeursListGrid);
   }

   /**
    * Initialise la valeur à afficher : utilisé lors d'un clic sur le
    * bouton delete.
    */
   public String getValeur(final Object value){
      if(value.getClass().getSimpleName().equals("Specialite")){
         return ((Specialite) value).getNom();
      }else if(value.getClass().getSimpleName().equals("Categorie")){
         return ((Categorie) value).getNom();
      }else if(value.getClass().getSimpleName().equals("NonConformite")){
         return ((NonConformite) value).getNom();
      }else{
         return ((TKThesaurusObject) value).getNom();
      }
   }

   /**
    * Clic sur le bouton addNewValeur.
    */
   public void onClick$addNewValeur(){
      // on récupère le thesaurus associé à l'event
      Object value = null;

      // création du nouvel objet
      if(typeThesaurus.getNom().equals("Nature")){
         value = new Nature();
      }else if(typeThesaurus.getNom().equals("PrelevementType")){
         value = new PrelevementType();
      }else if(typeThesaurus.getNom().equals("EchantillonType")){
         value = new EchantillonType();
      }else if(typeThesaurus.getNom().equals("EchanQualite")){
         value = new EchanQualite();
      }else if(typeThesaurus.getNom().equals("ProdType")){
         value = new ProdType();
      }else if(typeThesaurus.getNom().equals("ProdQualite")){
         value = new ProdQualite();
      }else if(typeThesaurus.getNom().equals("ConditType")){
         value = new ConditType();
      }else if(typeThesaurus.getNom().equals("ConditMilieu")){
         value = new ConditMilieu();
      }else if(typeThesaurus.getNom().equals("ConsentType")){
         value = new ConsentType();
      }else if(typeThesaurus.getNom().equals("Risque")){
         value = new Risque();
      }else if(typeThesaurus.getNom().equals("ModePrepa")){
         value = new ModePrepa();
      }else if(typeThesaurus.getNom().equals("ModePrepaDerive")){
         value = new ModePrepaDerive();
      }else if(typeThesaurus.getNom().equals("CessionExamen")){
         value = new CessionExamen();
      }else if(typeThesaurus.getNom().equals("DestructionMotif")){
         value = new DestructionMotif();
      }else if(typeThesaurus.getNom().equals("Protocole")){
         value = new Protocole();
      }else if(typeThesaurus.getNom().equals("ProtocoleType")){
         value = new ProtocoleType();
      }else if(typeThesaurus.getNom().equals("Specialite")){
         value = new Specialite();
      }else if(typeThesaurus.getNom().equals("Categorie")){
         value = new Categorie();
      }else if(typeThesaurus.getNom().equals("ConteneurType")){
         value = new ConteneurType();
      }else if(typeThesaurus.getNom().equals("EnceinteType")){
         value = new EnceinteType();
      }else if(typeThesaurus.getNom().contains("NonConformite")){
         value = new NonConformite();
         ((NonConformite) value).setPlateforme(SessionUtils.getPlateforme(sessionScope));
         if(typeThesaurus.getNom().equals("NonConformiteArrivee")){
            ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
               .findByEntiteAndTypeManager("Arrivee", ManagerLocator.getEntiteManager().findByIdManager(2)).get(0));
         }else if(typeThesaurus.getNom().equals("NonConformiteTraitementEchan")){
            ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
               .findByEntiteAndTypeManager("Traitement", ManagerLocator.getEntiteManager().findByIdManager(3)).get(0));
         }else if(typeThesaurus.getNom().equals("NonConformiteCessionEchan")){
            ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
               .findByEntiteAndTypeManager("Cession", ManagerLocator.getEntiteManager().findByIdManager(3)).get(0));
         }else if(typeThesaurus.getNom().equals("NonConformiteTraitementDerive")){
            ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
               .findByEntiteAndTypeManager("Traitement", ManagerLocator.getEntiteManager().findByIdManager(8)).get(0));
         }else if(typeThesaurus.getNom().equals("NonConformiteCessionDerive")){
            ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
               .findByEntiteAndTypeManager("Cession", ManagerLocator.getEntiteManager().findByIdManager(8)).get(0));
         }
      }

      if(value instanceof TKThesaurusObject){
         ((TKThesaurusObject) value).setPlateforme(SessionUtils.getPlateforme(sessionScope));
      }

      // création du titre
      final String title = ObjectTypesFormatters.getLabel("thesaurus.creation.valeur", new String[] {typeThesaurus.getLabel()});

      // appel de la fenêtre modale
      openChampThesaurusWindow(page, Path.getPath(self), value, getConstraint(), getSecondConstraint(), title, true);
   }

   /**
    * Clique sur l'image onClickUpdateItem.
    * @param event
    */
   public void onClickUpdateItem(final ForwardEvent event){
      // on récupère le thesaurus associé à l'event
      final Object value = event.getData();

      // creation du titre
      final String title =
         ObjectTypesFormatters.getLabel("thesaurus.modification.valeur", new String[] {typeThesaurus.getLabel()});

      // appel de la modale
      openChampThesaurusWindow(page, Path.getPath(self), value, getConstraint(), getSecondConstraint(), title, false);
   }

   private boolean isUsedValeur(final Object value){
      if(value.getClass().getSimpleName().equals("Specialite")){
         return ManagerLocator.getSpecialiteManager().isUsedObjectManager(value);
      }else if(value.getClass().getSimpleName().equals("Categorie")){
         return ManagerLocator.getCategorieManager().isUsedObjectManager(value);
      }else if(value.getClass().getSimpleName().equals("NonConformite")){
         return ManagerLocator.getNonConformiteManager().isUsedObjectManager((NonConformite) value);
      }else{
         return thesManager.isUsedObjectManager(value);
      }
   }

   /**
    * Clique sur l'image onClickDeleteItem.
    * @param event
    */
   public void onClickDeleteItem(final ForwardEvent event){
      // on récupère le thesaurus associé à l'event
      final Object value = event.getData();

      final String nom = getValeur(value);

      final boolean isUsed = isUsedValeur(value);
      boolean isDeletable = true;

      String message = ObjectTypesFormatters.getLabel("message.deletion.message",
         new String[] {ObjectTypesFormatters.getLabel("message.deletion.value.thesaurus", new String[] {nom})});
      if(isUsed){
         if(typeThesaurus.getNom().equals("Nature") || typeThesaurus.getNom().equals("EchantillonType")
            || typeThesaurus.getNom().equals("ProdType") || typeThesaurus.getNom().equals("ConsentType")
            || typeThesaurus.getNom().equals("EnceinteType")){
            message = Labels.getLabel("thesaurus.deletion.isUsedNotNull");
            isDeletable = false;
         }else{
            message = Labels.getLabel("thesaurus.deletion.isUsed");
         }
      }

      if(isDeletable){
         if(Messagebox.show(message, Labels.getLabel("message.deletion.title"), Messagebox.YES | Messagebox.NO,
            Messagebox.QUESTION) == Messagebox.YES){

            try{
               if(thesManager != null){
                  // suppression
                  thesManager.removeObjectManager(value);
               }else{
                  if(typeThesaurus.getNom().contains("NonConformite")){
                     ManagerLocator.getNonConformiteManager().removeObjectManager((NonConformite) value);
                  }
               }

               initListeValeurs(false);
               getBinder().loadComponent(gridValeurs);

            }catch(final RuntimeException re){
               Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
            }
         }
      }else{
         Messagebox.show(message, Labels.getLabel("message.deletion.title"), Messagebox.OK, Messagebox.ERROR);
      }
   }

   /**
    * Rend les boutons d'actions cliquables ou non.
    */
   public void drawActionsForThesaurus(){
      if(sessionScope.containsKey("AdminPF")){
         isAdmin = (Boolean) sessionScope.get("AdminPF");
      }

      // si l'utilisateur est admin PF => boutons cliquables
      if(isAdmin){
         addNewValeur.setDisabled(false);
      }else{
         addNewValeur.setDisabled(true);
      }

      listValeursRenderer.setAdmin(isAdmin);
   }

   /**
    * Crée la contrainte liée au thésaurus. Cette contrainte sera utilisée
    * dans le Textbox de la modale FicheChampThesaurus.
    * @return
    */
   public ConstWord getConstraint(){
      ConstWord constraint = null;

      // création du nouvel objet
      if(typeThesaurus.getNom().equals("Nature")){
         constraint = ThesaurusConstraints.getNatureConstraint();
      }else if(typeThesaurus.getNom().equals("PrelevementType")){
         constraint = ThesaurusConstraints.getPrelevementTypeConstraint();
      }else if(typeThesaurus.getNom().equals("EchantillonType")){
         constraint = ThesaurusConstraints.getEchantillonTypeConstraint();
      }else if(typeThesaurus.getNom().equals("EchanQualite")){
         constraint = ThesaurusConstraints.getEchanQualiteConstraint();
      }else if(typeThesaurus.getNom().equals("ProdType")){
         constraint = ThesaurusConstraints.getProdTypeConstraint();
      }else if(typeThesaurus.getNom().equals("ProdQualite")){
         constraint = ThesaurusConstraints.getProdQualiteConstraint();
      }else if(typeThesaurus.getNom().equals("ConditType")){
         constraint = ThesaurusConstraints.getConditTypeConstraint();
      }else if(typeThesaurus.getNom().equals("ConditMilieu")){
         constraint = ThesaurusConstraints.getConditMilieurConstraint();
      }else if(typeThesaurus.getNom().equals("ConsentType")){
         constraint = ThesaurusConstraints.getConsentTypeConstraint();
      }else if(typeThesaurus.getNom().equals("Risque")){
         constraint = ThesaurusConstraints.getRisqueConstraint();
      }else if(typeThesaurus.getNom().equals("ModePrepa")){
         constraint = ThesaurusConstraints.getModePrepaConstraint();
      }else if(typeThesaurus.getNom().equals("ModePrepaDerive")){
         constraint = ThesaurusConstraints.getModePrepaConstraint();
      }else if(typeThesaurus.getNom().equals("CessionExamen")){
         constraint = ThesaurusConstraints.getCessionExamenConstraint();
      }else if(typeThesaurus.getNom().equals("DestructionMotif")){
         constraint = ThesaurusConstraints.getDestructionMotifConstraint();
      }else if(typeThesaurus.getNom().equals("ProtocoleType")){
         constraint = ThesaurusConstraints.getProtocoleTypeConstraint();
      }else if(typeThesaurus.getNom().equals("Protocole")){
         constraint = ThesaurusConstraints.getProtocoleConstraint();
      }else if(typeThesaurus.getNom().equals("Specialite")){
         constraint = ThesaurusConstraints.getSpecialiteConstraint();
      }else if(typeThesaurus.getNom().equals("Categorie")){
         constraint = ThesaurusConstraints.getCategorieConstraint();
      }else if(typeThesaurus.getNom().equals("ConteneurType")){
         constraint = ThesaurusConstraints.getConteneurTypeConstraint();
      }else if(typeThesaurus.getNom().equals("EnceinteType")){
         constraint = ThesaurusConstraints.getEnceinteTypeConstraint();
      }else if(typeThesaurus.getNom().contains("NonConformite")){
         constraint = ThesaurusConstraints.getNonConformiteConstraint();
      }

      return constraint;
   }

   /**
    * Crée la contrainte liée au thésaurus. Cette contrainte sera utilisée
    * dans le Textbox de la modale FicheChampThesaurus.
    * @return
    */
   public ConstWord getSecondConstraint(){
      ConstWord constraint = null;

      // création du nouvel objet
      if(typeThesaurus.getNom().equals("EnceinteType")){
         constraint = ThesaurusConstraints.getEnceinteTypePrefixeConstraint();
      }

      return constraint;
   }

   /**
    * Méthode appelée par la fenêtre FicheChampThesaurus quand
    * l'utilisateur valide la modif.
    * @param e Event contenant la valeur modifiée.
    */
   public void onGetSaveDoneOnValue(final Event e){

      // si une valeur est renvoyé => modif complète
      if(e.getData() != null){
         initListeValeurs(false);
         getBinder().loadComponent(gridValeurs);
      }
   }

   /***********************************************************/
   /****************** GETTERS et SETTERS *********************/
   /***********************************************************/

   public Thesaurus getTypeThesaurus(){
      return typeThesaurus;
   }

   public void setTypeThesaurus(final Thesaurus typeTh){
      this.typeThesaurus = typeTh;
   }

   public boolean isAdmin(){
      return isAdmin;
   }

   public void setAdmin(final boolean is){
      this.isAdmin = is;
   }

   public ThesaurusRowRenderer getListValeursRenderer(){
      return listValeursRenderer;
   }

   public void setListValeursRenderer(final ThesaurusRowRenderer listRenderer){
      this.listValeursRenderer = listRenderer;
   }

   public List<? extends Object> getListValeurs(){
      return listValeurs;
   }

   public void setListValeurs(final List<Object> listVals){
      this.listValeurs = listVals;
   }

   @Override
   public void setFocusOnElement(){}

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

}
