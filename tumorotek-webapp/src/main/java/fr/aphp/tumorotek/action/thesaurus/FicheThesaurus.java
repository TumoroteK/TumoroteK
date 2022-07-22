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
import fr.aphp.tumorotek.manager.PfDependantTKThesaurusManager;
import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.qualite.NonConformiteManager;
import fr.aphp.tumorotek.model.AbstractPfDependantThesaurusObject;
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
import fr.aphp.tumorotek.model.contexte.Diagnostic;
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
   private Thesaurus<? extends TKThesaurusObject> typeThesaurus;

   private boolean isAdmin = false;

   private ThesaurusRowRenderer listValeursRenderer = new ThesaurusRowRenderer();

   private TKThesaurusManager<? extends TKThesaurusObject> thesManager;

   private List<?> listValeurs = new ArrayList<>();

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
      this.typeThesaurus = (Thesaurus<?>) obj;

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

         thesManager = ManagerLocator.getThesaurusManager(typeThesaurus.getThesaurusObjectClass());

      }/*else if(typeThesaurus.getNom().equals("Diagnostic")){
         thesManager = ManagerLocator.getManager(DiagnosticManager.class);
       }*/

      final boolean thesaurusNonConformite = NonConformite.class.equals(typeThesaurus.getThesaurusObjectClass());

      if(!thesaurusNonConformite){
         if(thesManager instanceof PfDependantTKThesaurusManager){
            listValeurs =
               ((PfDependantTKThesaurusManager<?>) thesManager).findByOrderManager(SessionUtils.getPlateforme(sessionScope));
         }else{
            listValeurs = ((TKThesaurusManager<?>) thesManager).findByOrderManager();
         }
      }else{

         final NonConformiteManager ncManager = (NonConformiteManager) thesManager;

         switch(typeThesaurus.getQualifier()){
            case "NonConformiteArrivee":
               listValeurs = ncManager.findByPlateformeEntiteAndTypeStringManager(SessionUtils.getPlateforme(sessionScope),
                  "Arrivee", ManagerLocator.getEntiteManager().findByIdManager(2));
               break;
            case "NonConformiteTraitementEchan":
               listValeurs = ncManager.findByPlateformeEntiteAndTypeStringManager(SessionUtils.getPlateforme(sessionScope),
                  "Traitement", ManagerLocator.getEntiteManager().findByIdManager(3));
               break;
            case "NonConformiteCessionEchan":
               listValeurs = ncManager.findByPlateformeEntiteAndTypeStringManager(SessionUtils.getPlateforme(sessionScope),
                  "Cession", ManagerLocator.getEntiteManager().findByIdManager(3));
               break;
            case "NonConformiteTraitementDerive":
               listValeurs = ncManager.findByPlateformeEntiteAndTypeStringManager(SessionUtils.getPlateforme(sessionScope),
                  "Traitement", ManagerLocator.getEntiteManager().findByIdManager(8));
               break;
            case "NonConformiteCessionDerive":
               listValeurs = ncManager.findByPlateformeEntiteAndTypeStringManager(SessionUtils.getPlateforme(sessionScope),
                  "Cession", ManagerLocator.getEntiteManager().findByIdManager(8));
               break;
            default:
               throw new TKException("Type de non-conformité [" + typeThesaurus.getQualifier() + "] inconnu");
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

      try{
         value = typeThesaurus.getThesaurusObjectClass().newInstance();
      }catch(InstantiationException | IllegalAccessException e){
         throw new TKException("Impossible d'instancier " + typeThesaurus.getThesaurusObjectClass().getSimpleName());
      }

      if(value instanceof NonConformite){

         switch(typeThesaurus.getQualifier()){
            case "NonConformiteArrivee":
               ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
                  .findByEntiteAndTypeManager("Arrivee", ManagerLocator.getEntiteManager().findByIdManager(2)).get(0));
               break;
            case "NonConformiteTraitementEchan":
               ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
                  .findByEntiteAndTypeManager("Traitement", ManagerLocator.getEntiteManager().findByIdManager(3)).get(0));
               break;
            case "NonConformiteCessionEchan":
               ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
                  .findByEntiteAndTypeManager("Cession", ManagerLocator.getEntiteManager().findByIdManager(3)).get(0));
               break;
            case "NonConformiteTraitementDerive":
               ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
                  .findByEntiteAndTypeManager("Traitement", ManagerLocator.getEntiteManager().findByIdManager(8)).get(0));
               break;
            case "NonConformiteCessionDerive":
               ((NonConformite) value).setConformiteType(ManagerLocator.getConformiteTypeManager()
                  .findByEntiteAndTypeManager("Cession", ManagerLocator.getEntiteManager().findByIdManager(8)).get(0));
               break;
            default:
               throw new TKException("Type de non-conformité [" + typeThesaurus.getQualifier() + "] inconnu");
         }

      }

      if(value instanceof AbstractPfDependantThesaurusObject){
         ((AbstractPfDependantThesaurusObject) value).setPlateforme(SessionUtils.getPlateforme(sessionScope));
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

   /**
    * Clique sur l'image onClickDeleteItem.
    * @param event
    */
   @SuppressWarnings("unchecked")
   public void onClickDeleteItem(final ForwardEvent event){
      // on récupère le thesaurus associé à l'event
      final TKThesaurusObject value = (TKThesaurusObject) event.getData();

      final List<Class<? extends TKThesaurusObject>> checkIsUsedClassList = new ArrayList<>();
      checkIsUsedClassList.add(Nature.class);
      checkIsUsedClassList.add(EchantillonType.class);
      checkIsUsedClassList.add(ProdType.class);
      checkIsUsedClassList.add(ConsentType.class);
      checkIsUsedClassList.add(EnceinteType.class);

      final String nom = getValeur(value);

      final boolean isUsed = ((TKThesaurusManager<TKThesaurusObject>) thesManager).isUsedObjectManager(value);
      boolean isDeletable = true;

      String message = ObjectTypesFormatters.getLabel("message.deletion.message",
         new String[] {ObjectTypesFormatters.getLabel("message.deletion.value.thesaurus", new String[] {nom})});
      if(isUsed){
         if(checkIsUsedClassList.contains(typeThesaurus.getThesaurusObjectClass())){
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
               ((TKThesaurusManager<TKThesaurusObject>) thesManager).removeObjectManager(value);

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
      if(Nature.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getNatureConstraint();
      }else if(PrelevementType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getPrelevementTypeConstraint();
      }else if(EchantillonType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getEchantillonTypeConstraint();
      }else if(EchanQualite.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getEchanQualiteConstraint();
      }else if(ProdType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getProdTypeConstraint();
      }else if(ProdQualite.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getProdQualiteConstraint();
      }else if(ConditType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getConditTypeConstraint();
      }else if(ConditMilieu.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getConditMilieurConstraint();
      }else if(ConsentType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getConsentTypeConstraint();
      }else if(Risque.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getRisqueConstraint();
      }else if(ModePrepa.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getModePrepaConstraint();
      }else if(ModePrepaDerive.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getModePrepaConstraint();
      }else if(CessionExamen.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getCessionExamenConstraint();
      }else if(DestructionMotif.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getDestructionMotifConstraint();
      }else if(ProtocoleType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getProtocoleTypeConstraint();
      }else if(Protocole.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getProtocoleConstraint();
      }else if(Diagnostic.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getDiagnosticConstraint();
      }else if(Specialite.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getSpecialiteConstraint();
      }else if(Categorie.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getCategorieConstraint();
      }else if(ConteneurType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getConteneurTypeConstraint();
      }else if(EnceinteType.class.equals(typeThesaurus.getThesaurusObjectClass())){
         constraint = ThesaurusConstraints.getEnceinteTypeConstraint();
      }else if(NonConformite.class.equals(typeThesaurus.getThesaurusObjectClass())){
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
      if(EnceinteType.class.equals(typeThesaurus.getThesaurusObjectClass())){
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

   public Thesaurus<?> getTypeThesaurus(){
      return typeThesaurus;
   }

   public void setTypeThesaurus(final Thesaurus<?> typeTh){
      this.typeThesaurus = typeTh;
   }

   @Override
   public boolean isAdmin(){
      return isAdmin;
   }

   @Override
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
