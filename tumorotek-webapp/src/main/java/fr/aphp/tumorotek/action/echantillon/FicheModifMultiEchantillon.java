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
package fr.aphp.tumorotek.action.echantillon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.action.modification.multiple.CodesPack;
import fr.aphp.tumorotek.action.modification.multiple.ConformitePack;
import fr.aphp.tumorotek.action.modification.multiple.FilePack;
import fr.aphp.tumorotek.action.modification.multiple.SimpleChampValue;
import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Controller gérant la fiche de modification multiple d'échantillons.
 * Controller créé le 20/03/2011.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class FicheModifMultiEchantillon extends AbstractFicheModifMultiController
{

   private static final long serialVersionUID = 4384639895874573764L;

   private Label echantillonTypeLabelChanged;
   //private Label quantiteLabelChanged;
   private Label quantiteInitLabelChanged;
   private Label modePrepaLabelChanged;
   private Label sterileLabelChanged;
   private Label dateStockLabelChanged;
   private Label delaiCglLabelChanged;
   private Label operateurLabelChanged;
   private Label qualiteLabelChanged;
   private Label crAnapathLabelChanged;
   private Label tumoralLabelChanged;
   private Label organeLabelChanged;
   private Label lateraliteLabelChanged;
   private Label codeLesLabelChanged;
   private Label nonConformeTraitementLabelChanged;
   private Label nonConformeCessionLabelChanged;
   protected Label qualiteEchanLabel;
   protected Div qualiteEchanValue;
   
   protected Row infosComplementairesRow;
   protected Row lateraliteRow;

   private List<NonConformite> nonConformitesTraitement = null;

   private List<NonConformite> nonConformitesCession = null;

   private List<? extends Object> lats;

   public List<? extends Object> getLats(){
      return lats;
   }

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      final List<LabelCodeItem> labs = new ArrayList<>();
      labs.add(new LabelCodeItem(Labels.getLabel("echantillon.lateralite.G"), "G"));
      labs.add(new LabelCodeItem(Labels.getLabel("echantillon.lateralite.D"), "D"));
      labs.add(new LabelCodeItem(Labels.getLabel("echantillon.lateralite.I"), "I"));
      labs.add(new LabelCodeItem(Labels.getLabel("echantillon.lateralite.B"), "B"));
      lats = labs;
   }

   @Override
   public void setNewObject(){
      setBaseObject(new Echantillon());
   }

   @Override
   public Echantillon getObject(){
      return (Echantillon) super.getObject();
   }

   @Override
   public AbstractObjectTabController getObjectTabController(){
      return ((EchantillonController) self.getParent().getParent().getParent().getFellow("winEchantillon")
         .getAttributeOrFellow("winEchantillon$composer", true));
   }

   @Override
   public void updateLabelChanged(final String champ, final String printValue, final boolean reset){
      if("echantillonType".equals(champ)){
         echantillonTypeLabelChanged.setValue(printValue);
         echantillonTypeLabelChanged.setVisible(!reset);
         //		} else if ("quantite".equals(champ)) {
         //			quantiteLabelChanged.setValue(printValue);
         //			quantiteLabelChanged.setVisible(!reset);
      }else if("quantiteInit".equals(champ)){
         quantiteInitLabelChanged.setValue(printValue);
         quantiteInitLabelChanged.setVisible(!reset);
      }else if("modePrepa".equals(champ)){
         modePrepaLabelChanged.setValue(printValue);
         modePrepaLabelChanged.setVisible(!reset);
      }else if("sterile".equals(champ)){
         sterileLabelChanged.setValue(printValue);
         sterileLabelChanged.setVisible(!reset);
      }else if("dateStock".equals(champ)){
         dateStockLabelChanged.setValue(printValue);
         dateStockLabelChanged.setVisible(!reset);
      }else if("delaiCgl".equals(champ)){
         delaiCglLabelChanged.setValue(printValue);
         delaiCglLabelChanged.setVisible(!reset);
      }else if("collaborateur".equals(champ)){
         operateurLabelChanged.setValue(printValue);
         operateurLabelChanged.setVisible(!reset);
      }else if("echanQualite".equals(champ)){
         qualiteLabelChanged.setValue(printValue);
         qualiteLabelChanged.setVisible(!reset);
      }else if("crAnapath".equals(champ)){
         crAnapathLabelChanged.setValue(printValue);
         crAnapathLabelChanged.setVisible(!reset);
      }else if("tumoral".equals(champ)){
         tumoralLabelChanged.setValue(printValue);
         tumoralLabelChanged.setVisible(!reset);
      }else if("codeOrganes".equals(champ)){
         organeLabelChanged.setValue(printValue);
         organeLabelChanged.setVisible(!reset);
      }else if("lateralite".equals(champ)){
         lateraliteLabelChanged.setValue(printValue);
         lateraliteLabelChanged.setVisible(!reset);
      }else if("codeMorphos".equals(champ)){
         codeLesLabelChanged.setValue(printValue);
         codeLesLabelChanged.setVisible(!reset);
      }else if("conformeTraitement".equals(champ)){
         nonConformeTraitementLabelChanged.setValue(printValue);
         nonConformeTraitementLabelChanged.setVisible(!reset);
      }else if("conformeCession".equals(champ)){
         nonConformeCessionLabelChanged.setValue(printValue);
         nonConformeCessionLabelChanged.setVisible(!reset);
      }
   }

   @Override
   public void updateMultiObjects(){

      final List<Echantillon> clones = new ArrayList<>();
      List<CodeAssigne> codes = null;
      //List<CodeAssigne> codesMorphos = null;
      List<CodeAssigne> codesToDelete = new ArrayList<>();
      //CodeAssigne codeOrgExp = null;
      //CodeAssigne codeLesExp = null;
      Fichier crAnapath = null;
      InputStream anapathStream = null;
      Boolean deleteCrAnapath = null;

      boolean hasAnyChange = false;
      for(int i = 0; i < getObjsToEdit().size(); i++){
         final Echantillon current = ((Echantillon) getObjsToEdit().get(i)).clone();

         // ehcantillonType
         if(!echantillonTypeLabelChanged.getValue().equals("")){
            current.setEchantillonType(getObject().getEchantillonType());
            hasAnyChange = true;
         }

         // quantite
         //			if (!quantiteLabelChanged.getValue().equals("")) {
         //				current.setQuantite(getObject().getQuantite());
         //				current.setQuantiteUnite(getObject().getQuantiteUnite());
         //				hasAnyChange = true;
         //			}

         // quantiteInit
         if(!quantiteInitLabelChanged.getValue().equals("")){
            current.setQuantite(getObject().getQuantiteInit());
            current.setQuantiteInit(getObject().getQuantiteInit());
            current.setQuantiteUnite(getObject().getQuantiteUnite());
            hasAnyChange = true;
         }

         // modePrepa
         if(!modePrepaLabelChanged.getValue().equals("")){
            current.setModePrepa(getObject().getModePrepa());
            hasAnyChange = true;
         }

         // sterile
         if(!sterileLabelChanged.getValue().equals("")){
            current.setSterile(getObject().getSterile());
            hasAnyChange = true;
         }

         // dateStock
         if(!dateStockLabelChanged.getValue().equals("")){
            current.setDateStock(getObject().getDateStock());
            hasAnyChange = true;
         }

         // delai cgl
         if(!delaiCglLabelChanged.getValue().equals("")){
            current.setDelaiCgl(getObject().getDelaiCgl());
            hasAnyChange = true;
         }

         // operateur
         if(!operateurLabelChanged.getValue().equals("")){
            current.setCollaborateur(getObject().getCollaborateur());
            hasAnyChange = true;
         }

         // qualite
         if(!qualiteLabelChanged.getValue().equals("")){
            current.setEchanQualite(getObject().getEchanQualite());
            hasAnyChange = true;
         }

         // anapath
         if(!crAnapathLabelChanged.getValue().equals("")){
            crAnapath = getObject().getCrAnapath();
            anapathStream = getObject().getAnapathStream();
            hasAnyChange = true;
            deleteCrAnapath = (crAnapath == null);
         }

         // tumoral
         if(!tumoralLabelChanged.getValue().equals("")){
            current.setTumoral(getObject().getTumoral());
            hasAnyChange = true;
         }

         // organe
         if(!organeLabelChanged.getValue().equals("") || !codeLesLabelChanged.getValue().equals("")){
            if(!organeLabelChanged.getValue().equals("")){
               codesToDelete.addAll(ManagerLocator.getCodeAssigneManager().findCodesOrganeByEchantillonManager(current));
            }
            if(!codeLesLabelChanged.getValue().equals("")){
               codesToDelete.addAll(ManagerLocator.getCodeAssigneManager().findCodesMorphoByEchantillonManager(current));
            }
            codes = new ArrayList<>();
            codes.addAll(getObject().getCodesAssignes());

            //codeOrgExp = getObject().getCodeOrganeExport();
            hasAnyChange = true;
         }

         // lateralite
         if(!lateraliteLabelChanged.getValue().equals("")){
            current.setLateralite(getObject().getLateralite());
            hasAnyChange = true;
         }

         // conforme Traitement
         if(!nonConformeTraitementLabelChanged.getValue().equals("")){
            current.setConformeTraitement(getObject().getConformeTraitement());
            hasAnyChange = true;
         }

         // conforme Cession
         if(!nonConformeCessionLabelChanged.getValue().equals("")){
            current.setConformeCession(getObject().getConformeCession());
            hasAnyChange = true;
         }

         // sort de la boucle de suite si pas chgts
         if(!hasAnyChange){
            break;
         }
         clones.add(current);
      }

      hasAnyChange = updateMultiAnnotationValeurs() || hasAnyChange;

      if(codes != null && codes.isEmpty()){
         codes = null;
      }

      if(codesToDelete.isEmpty()){
         codesToDelete = null;
      }

      if(hasAnyChange){
         ManagerLocator.getEchantillonManager().updateMultipleObjectsManager(clones, (List<Echantillon>) getObjsToEdit(), codes,
            codesToDelete, crAnapath, anapathStream, deleteCrAnapath,
            getObjectTabController().getFicheAnnotation().getValeursToCreateOrUpdate(),
            getObjectTabController().getFicheAnnotation().getValeursToDelete(), nonConformitesTraitement, nonConformitesCession,
            SessionUtils.getLoggedUser(sessionScope), SessionUtils.getSystemBaseDir());

         getObjectTabController().getFicheAnnotation().clearValeursLists(true);

         // si aucune exception
         if(!clones.isEmpty()){
            setObjsToEdit(clones);
         }
      }

      if(null != anapathStream){
         try{
            anapathStream.close();
         }catch(IOException e){
            log.error(e);
         }
      }
   }

   /*************************************************************************/
   /************************** CHAMPS ***************************************/
   /*************************************************************************/

   public void onClick$echantillonTypeMultiLabel(){
      final List<? extends Object> types =
         ManagerLocator.getEchantillonTypeManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "Champ.Echantillon.EchantillonType.Type", "echantillonType", (List<Object>) types, "type", null, null, false, null,
         true);
   }

   public void onClick$quantiteInitMultiLabel(){

      final List<Unite> quantiteUnites = ManagerLocator.getUniteManager().findByTypeLikeManager("masse", true);
      quantiteUnites.addAll(ManagerLocator.getUniteManager().findByTypeLikeManager("discret", true));
      quantiteUnites.addAll(ManagerLocator.getUniteManager().findByTypeLikeManager("volume", true));

      final List<? extends Object> units = quantiteUnites;

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Quantification", getObjsToEdit(),
         "Champ.Echantillon.QuantiteInit", "quantiteInit", (List<Object>) units, "unite", null, null, false, null, false);
   }

   public void onClick$modePrepaMultiLabel(){
      final List<? extends Object> modes =
         ManagerLocator.getModePrepaManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "Champ.Echantillon.ModePrepa", "modePrepa", (List<Object>) modes, "nom", null, null, false, null, false);
   }

   public void onClick$sterileMultiLabel(){

      List<? extends Object> bools;
      final List<Boolean> bools2 = new ArrayList<>();
      bools2.add(new Boolean(true));
      bools2.add(new Boolean(false));

      bools = bools2;

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "general.sterile", "sterile", (List<Object>) bools, "bool", null, null, false, null, false);
   }

   public void onClick$dateStockMultiLabel(){
      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Calendarbox", getObjsToEdit(),
         "Champ.Echantillon.DateStock", "dateStock", null, null, null, null, false, null, null);
   }

   public void onClick$delaiCglMultiLabel(){
      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Floatbox", getObjsToEdit(),
         "Champ.Echantillon.DelaiCgl", "delaiCgl", null, null, null, new SimpleConstraint(SimpleConstraint.NO_NEGATIVE), false,
         null, false);
   }

   public void onClick$operateurMultiLabel(){
      final List<? extends Object> ops = ManagerLocator.getCollaborateurManager().findAllActiveObjectsWithOrderManager();

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Combobox", getObjsToEdit(),
         "Champ.Echantillon.Collaborateur", "collaborateur", (List<Object>) ops, "nomAndPrenom", null, null, false, null, false);
   }

   public void onClick$qualiteMultiLabel(){
      final List<? extends Object> quals =
         ManagerLocator.getEchanQualiteManager().findByOrderManager(SessionUtils.getPlateforme(sessionScope));

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "Champ.Echantillon.EchanQualite", "echanQualite", (List<Object>) quals, "echanQualite", null, null, false, null, false);
   }

   public void onClick$crAnapathMultiLabel(){

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Filebox", getObjsToEdit(),
         "ficheEchantillon.crAnapathLabel", "crAnapath", null, null, null, null, false, null, false);
   }

   public void onClick$tumoralMultiLabel(){

      List<? extends Object> bools;
      final List<Boolean> bools2 = new ArrayList<>();
      bools2.add(new Boolean(true));
      bools2.add(new Boolean(false));

      bools = bools2;

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "Champ.Echantillon.Tumoral", "tumoral", (List<Object>) bools, "bool", null, null, false, null, false);
   }

   public void onClick$organeMultiLabel(){
      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "CodesBox", getObjsToEdit(),
         "Champ.Echantillon.Organe", "codeOrganes", null, null, null, null, false, null, false);
   }

   public void onClick$lateraliteMultiLabel(){

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Listbox", getObjsToEdit(),
         "Champ.Echantillon.Lateralite", "lateralite", (List<Object>) getLats(), "label", null, null, false, null, false);
   }

   public void onClick$codeLesMultiLabel(){
      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "CodesBox", getObjsToEdit(),
         "ficheEchantillon.codeLesionelLabel", "codeMorphos", null, null, null, null, false, null, false);
   }

   public void onClick$nonConformeTraitementMultiLabel(){

      final List<? extends Object> nonConfs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
         SessionUtils.getPlateforme(sessionScope), "Traitement", getObjectTabController().getEntiteTab());

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Conformitebox", getObjsToEdit(),
         "Champ.Echantillon.ConformeTraitement", "conformeTraitement", (List<Object>) nonConfs, "Traitement", null, null, false,
         null, false);
   }

   public void onClick$nonConformeCessionMultiLabel(){

      final List<? extends Object> nonConfs = ManagerLocator.getNonConformiteManager().findByPlateformeEntiteAndTypeStringManager(
         SessionUtils.getPlateforme(sessionScope), "Cession", getObjectTabController().getEntiteTab());

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Conformitebox", getObjsToEdit(),
         "Champ.Echantillon.ConformeCession", "conformeCession", (List<Object>) nonConfs, "Cession", null, null, false, null,
         false);
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   /**
    * Surcharge la méthode pour court-circuiter la reception de 
    * l'event te gérer les modifications sur le sexe et l'état. 
    * Si l'etat change alors nullify date Etat ou date Deces.
    */
   @Override
   public void onGetChangeOnChamp(final Event e){

      final SimpleChampValue tmp = (SimpleChampValue) e.getData();

      if(tmp.getValue() instanceof LabelCodeItem){

         // transforme leLabelCodeItem en sa valeur
         tmp.setValue(((LabelCodeItem) tmp.getValue()).getCode());

         Events.postEvent(new Event("onGetChangeOnChamp", self, tmp));

      }else if(tmp.getValue() instanceof CodesPack){
         final CodesPack pck = (CodesPack) tmp.getValue();
         if("codeOrganes".equals(tmp.getChamp())){
            getObject().getCodesAssignes().addAll(CodeAssigneDecorator.undecorateListe(pck.getCodesToCreateOrEdit()));
            //getObject().setCodeOrganeExport(pck.getCodeToExport());
         }else{
            getObject().getCodesAssignes().addAll(CodeAssigneDecorator.undecorateListe(pck.getCodesToCreateOrEdit()));
            //getObject().setCodeLesExport(pck.getCodeToExport());
         }

         final StringBuffer sb = new StringBuffer();
         sb.append("[");
         if(tmp.getPrintValue() != null){
            sb.append(tmp.getPrintValue());
         }else{
            sb.append(" ");
         }
         sb.append("]");
         sb.append(" - ");

         updateLabelChanged(tmp.getChamp(), sb.toString(), false);
      }else if(tmp.getValue() instanceof FilePack){
         final FilePack pck = (FilePack) tmp.getValue();

         getObject().setCrAnapath(pck.getFile());
         getObject().setAnapathStream(pck.getStream());

         final StringBuffer sb = new StringBuffer();
         sb.append("[");
         if(tmp.getPrintValue() != null){
            sb.append(tmp.getPrintValue());
         }else{
            sb.append(" ");
         }
         sb.append("]");
         sb.append(" - ");

         updateLabelChanged(tmp.getChamp(), sb.toString(), false);
      }else if(tmp.getValue() instanceof ConformitePack){
         final ConformitePack pck = (ConformitePack) tmp.getValue();
         if(tmp.getChamp().equals("conformeTraitement")){
            if(nonConformitesTraitement != null){
               nonConformitesTraitement.clear();
            }else{
               nonConformitesTraitement = new ArrayList<>();
            }
            if(pck.getNonConforme() != null){
               if(pck.getNonConforme()){
                  getObject().setConformeTraitement(false);
                  nonConformitesTraitement.addAll(pck.getNonConformites());
               }else if(!pck.getNonConforme()){
                  getObject().setConformeTraitement(true);
               }
            }
         }else if(tmp.getChamp().equals("conformeCession")){
            if(nonConformitesCession != null){
               nonConformitesCession.clear();
            }else{
               nonConformitesCession = new ArrayList<>();
            }
            if(pck.getNonConforme() != null){
               if(pck.getNonConforme()){
                  getObject().setConformeCession(false);
                  nonConformitesCession.addAll(pck.getNonConformites());
               }else if(!pck.getNonConforme()){
                  getObject().setConformeCession(true);
               }
            }
         }
         final StringBuffer sb = new StringBuffer();
         sb.append("[");
         if(tmp.getPrintValue() != null){
            sb.append(tmp.getPrintValue());
         }else{
            sb.append(" ");
         }
         sb.append("]");

         updateLabelChanged(tmp.getChamp(), sb.toString(), false);
      }else{
         super.onGetChangeOnChamp(e);
      }
   }

   @Override
   public void onResetMulti(final Event event){
      final SimpleChampValue tmp = (SimpleChampValue) event.getData();

      if("codeOrganes".equals(tmp.getChamp()) || "codeMorphos".equals(tmp.getChamp()) || "crAnapath".equals(tmp.getChamp())){
         final List<CodeAssigne> codesToRemove = new ArrayList<>();
         if("codeOrganes".equals(tmp.getChamp())){
            final Iterator<CodeAssigne> codesIt = getObject().getCodesAssignes().iterator();
            CodeAssigne next;
            while(codesIt.hasNext()){
               next = codesIt.next();
               if(next.getIsOrgane()){
                  codesToRemove.add(next);
               }
            }
         }else if("codeMorphos".equals(tmp.getChamp())){
            final Iterator<CodeAssigne> codesIt = getObject().getCodesAssignes().iterator();
            CodeAssigne next;
            while(codesIt.hasNext()){
               next = codesIt.next();
               if(next.getIsMorpho()){
                  codesToRemove.add(next);
               }
            }
         }
         getObject().getCodesAssignes().removeAll(codesToRemove);
         updateLabelChanged(tmp.getChamp(), "", true);
      }else if("crAnapath".equals(tmp.getChamp())){
         getObject().setCrAnapath(null);
         getObject().setAnapathStream(null);
         updateLabelChanged(tmp.getChamp(), "", true);
      }else if("conformeTraitement".equals(tmp.getChamp())){
         nonConformitesTraitement = null;
         updateLabelChanged(tmp.getChamp(), "", true);
      }else if("conformeCession".equals(tmp.getChamp())){
         nonConformitesCession = null;
         updateLabelChanged(tmp.getChamp(), "", true);
      }else{
         super.onResetMulti(event);
      }
   }
}
