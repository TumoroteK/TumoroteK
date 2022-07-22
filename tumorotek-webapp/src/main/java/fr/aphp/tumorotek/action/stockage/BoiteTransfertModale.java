/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * mathieu.barthelemy@sls.aphp.fr
 * pierre.ventadour@sls.aphp.fr
 * nathalie.dufay@chu-lyon.fr
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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.EmplacementDecorator;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovement;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovementComparator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 *
 * Modale dédiée aux positionnement de TKStockableObject dans un portoir virtuel
 * de transfert vers un système de stockage automatisé.
 * A la validation, une liste de mouvements <code>StorageMovement</code> sont envoyés
 * à l'émetteur défini par interfacage.
 *
 * @version 2.1
 * @author Mathieu BARTHELEMY
 *
 */
public class BoiteTransfertModale extends FicheDeplacerEmplacements
{

   private static final long serialVersionUID = 2L;

   @WireVariable("sessionScope")
   private Map<String, Object> _sessionScope;

   @Wire("#modeleBoite")
   private Div modeleBoite;

   private boolean storageDestock = false;

   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      Selectors.wireComponents(view, this, false);
      initModelisation(10, 10);
   }

   @Init
   public void init(@BindingParam("objs") final List<TKStockableObject> objs, @BindingParam("destockageMode") Boolean _d){

      if(_d == null){
         _d = false;
      }
      storageDestock = _d;

      setSelectionMode(false);
      setStockerMode(true);
      setDestockageMode(false);

      getDeplacements().clear();
      getDeplacementsRestants().clear();

      // prepare les listes d'emplacement decorators
      if(objs != null){
         for(final TKStockableObject tkObj : objs){
            // ne concerne que les objets stockés!
            if(tkObj.getEmplacement() != null){
               // utilise l'emplacement de stockage de l'objet
               // qq soit le mode stockage/destockage
               // car la methode EmplacementDecorator.equals
               // se base uniquement sur getEmplacement
               // override jpa proxy loading par findById
               final EmplacementDecorator deco = new EmplacementDecorator(
                  ManagerLocator.getEmplacementManager().findByIdManager(tkObj.getEmplacement().getEmplacementId()));

               deco.setCode(tkObj.getCode());

               if(!storageDestock){
                  deco.setAdrl(null);
                  deco.setAdrlDestination(ManagerLocator.getEmplacementManager().getAdrlManager(tkObj.getEmplacement(), true));
               }else{ // destockage -> rack devient destination
                  deco.setAdrl(ManagerLocator.getEmplacementManager().getAdrlManager(tkObj.getEmplacement(), true));
                  deco.setAdrlDestination(null);
               }

               getDeplacements().add(deco);
               getDeplacementsRestants().add(deco);
               getEmplacementDepart().add(deco);
            }
         }
      }
      if(getDeplacementsRestants().size() > 0){
         setSelectedEmplacement(getDeplacementsRestants().get(0));
      }
   }

   /**
    * Transformes les déplacements (EmplacementDecorator) en
    * mouvements (StorageMovements) afin de les poster à l'émetteur
    * défini par l'interfacage.
    * Ferme automatiquement la modale.
    * @param Window view modale
    */
   @Command
   public void send(@ContextParam(ContextType.VIEW) final Window w){

      postStorageMovementsData(makeStorageMovements());

      w.onClose();
   }

   /**
    * Envoi à l'interfacage des informations permettant
    * de produire un fichier tabulé contenant
    * les emplacements à fournir avec les codes objests
    * concernés.
    * @since 2.1.4.1
    */
   public void postStorageMovementsData(final List<StorageMovement> moves){
      if(moves != null && moves.size() > 0){
         try{
            // Recepteurs
            for(final Recepteur recept : SessionUtils.getRecepteursInterfacages(_sessionScope)){
               ManagerLocator.getSenderFactory().sendEmplacements(recept, moves, SessionUtils.getLoggedUser(_sessionScope));
            }
         }catch(final Exception e){
            Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
         }
      }
   }

   private List<StorageMovement> makeStorageMovements(){

      final List<StorageMovement> moves = new ArrayList<>();

      String adrl;
      String adrlDest;
      for(final EmplacementDecorator deco : getDeplacements()){

         adrl = null;
         adrl = deco.getAdrl();
         adrlDest = deco.getAdrlDestination();

         moves.add(new StorageMovement(deco.getCode(), adrl, adrlDest));
      }

      // sort by automated storage adrl to facilitate
      // robot handles
      Collections.sort(moves, new StorageMovementComparator(!storageDestock));

      return moves;
   }

   public void initModelisation(final int nbCol, final int nbRow){

      final Vlayout mainVbox = new Vlayout();
      mainVbox.setSpacing("0");
      mainVbox.setStyle("overflow:visible");

      final int width = 20 + (30 * nbCol);
      mainVbox.setWidth(width + "px");

      // création des abscisses
      final Hlayout separator = new Hlayout();
      separator.setHeight("5px");
      separator.setParent(mainVbox);
      separator.setStyle("overflow:visible");
      final Hlayout hBoxA = new Hlayout();
      hBoxA.setStyle("overflow:visible");
      hBoxA.setSpacing("0");
      final Div div = new Div();
      div.setWidth("20px");
      div.setParent(hBoxA);
      for(int j = 0; j < nbCol; j++){
         final Div divAbs = new Div();
         divAbs.setWidth("30px");
         divAbs.setParent(hBoxA);
         //divAbs.setAlign("center");
         divAbs.setStyle("text-align: center");
         final Label abs = new Label();
         abs.setSclass("formLabel");
         abs.setValue(getValueAbscisse(j + 1));
         abs.setParent(divAbs);
      }
      hBoxA.setParent(mainVbox);

      int cpt = 0;
      final List<Hlayout> lignes = new ArrayList<>();
      for(int i = 0; i < nbRow; i++){
         final Hlayout hBox = new Hlayout();
         hBox.setSpacing("0");
         hBox.setStyle("overflow:visible");

         // création des ordonnées
         final Div divOrd = new Div();
         divOrd.setWidth("20px");
         divOrd.setHeight("23px");
         divOrd.setParent(hBox);
         final Label ord = new Label();
         ord.setSclass("formLabel");
         ord.setValue(getValueOrdonnee(i + 1));
         ord.setParent(divOrd);
         for(int j = 0; j < nbCol; j++){
            final Emplacement newE = new Emplacement();
            newE.setPosition(cpt + 1);
            final EmplacementDecorator deco = new EmplacementDecorator(newE);
            deco.setPosition(cpt + 1);

            final Div img = createImage(deco);
            img.setParent(hBox);
            getImagesEmplacements().add(img);

            ++cpt;
         }
         // on stocke les lignes dans une liste
         lignes.add(hBox);
      }
      for(int i = 0; i < lignes.size(); i++){
         lignes.get(i).setParent(mainVbox);
      }

      mainVbox.setParent(modeleBoite);
   }

   @Override
   public Div createImage(final EmplacementDecorator deco){
      final Div div = super.createImage(deco);
      div.removeForward("onClick", self, "onClickImage");
      div.addForward("onClick", modeleBoite, "onClickImage", div);
      div.removeForward("onDrop", self, "onDropImage");
      div.addForward("onDrop", modeleBoite, "onDropImage", div);
      return div;
   }

   @Override
   @Command
   @NotifyChange("deplacements")
   public void onClickImage(@ContextParam(ContextType.TRIGGER_EVENT) final Event e){

      // override -> stockage mode utile
      if(e.getData() != null){
         final Div img = (Div) e.getData();
         selectImageInStockageMode(img);
      }
   }

   @Command
   @NotifyChange("deplacements")
   public void onDropImage(@ContextParam(ContextType.TRIGGER_EVENT) final ForwardEvent e){
      super.onDropImage((DropEvent) e.getOrigin());
   }

   @Override
   public void selectImageInStockageMode(final Div img){
      final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");
      // si l'emplacement n'est pas l'emplacement courant
      if(!deco.equals(getSelectedEmplacement()) && !getEmplacementReserves().contains(deco)){
         // l'image ne représente pas un emplacement de destination
         if(!getEmplacementsDestDep().containsKey(deco)){
            if(getSelectedEmplacement() != null){
               // la destination doit être vide
               if(deco.getVide()){
                  img.setSclass("imageMovedEmplacement");
                  // on met à jour l'emplacement courant => celui
                  // pour qui on vient de choisir la destination
                  final EmplacementDecorator upDeco = getDeplacements().get(getDeplacements().indexOf(getSelectedEmplacement()));

                  // met à jour la position dans le rack
                  if(!storageDestock){
                     upDeco.setAdrl(deco.getPosition().toString());
                  }else{
                     upDeco.setAdrlDestination(deco.getPosition().toString());
                  }

                  // on ajoute la destination à la hashtable
                  getEmplacementsDestDep().put(deco, upDeco);

                  // on nullify l'emplacement courant de la liste des
                  // emplacements à déplacer
                  deleteFromDeplacementRestants(getSelectedEmplacement());

                  setSelectedEmplacement(findFirstFromDeplacementRestants());
               }
            }
         }else{
            clearDeplacement(img);
         }
      }
   }

   private void clearDeplacement(final Div img){
      final EmplacementDecorator deco = (EmplacementDecorator) img.getAttribute("empDeco");
      if(getDeplacements().contains(getEmplacementsDestDep().get(deco))){
         // on récupère l'emplacement de départ
         final int dpctIdx = getDeplacements().indexOf(getEmplacementsDestDep().get(deco));
         final EmplacementDecorator decoOtherSide = getDeplacements().get(dpctIdx);
         // on vide sa position dans le rack
         if(!storageDestock){
            decoOtherSide.setAdrl("");
         }else{ // destockage
            decoOtherSide.setAdrlDestination("");
            // decoOtherSide.setEmplDestination(null);
         }

         // remet le decorator à sa place
         // dans la liste des deplacements à compléter
         getDeplacementsRestants().add(dpctIdx, decoOtherSide);

         img.setSclass("imageEmplacement");

         // l'emplacement courant est le 1er de la liste
         setSelectedEmplacement(findFirstFromDeplacementRestants());

         // on met a jour l'hashtable
         getEmplacementsDestDep().remove(deco);
      }
   }

   @Command
   @NotifyChange("deplacements")
   public void clearDeplacement(@BindingParam("dpct") final EmplacementDecorator deco){
      final Div img = !isStockerMode() ? getImagesEmplacements().get(Integer.valueOf(deco.getAdrl()) - 1)
         : getImagesEmplacements().get(Integer.valueOf(deco.getAdrlDestination()) - 1);
      clearDeplacement(img);
   }

   @Override
   public String getValueAbscisse(final Integer num){
      return String.valueOf(num);
   }

   @Override
   public String getValueOrdonnee(final Integer num){
      return String.valueOf(num);
   }

   public String getLocationLabel(){
      if(!storageDestock){
         return Labels.getLabel("portoir.transfert.position");
      }else{ // destockage
         return Labels.getLabel("portoir.transfert.destockage.adresse");
      }
   }

   public String getDestinationLabel(){
      if(storageDestock){
         return Labels.getLabel("portoir.transfert.position");
      }else{ // stockage
         return Labels.getLabel("portoir.transfert.stockage.destination");
      }
   }

   public String getTitle(){
      if(!storageDestock){
         return Labels.getLabel("portoir.transfert.modal.stockage.title");
      }else{ // destockage
         return Labels.getLabel("portoir.transfert.modal.destockage.title");
      }
   }

   /**
    * Nullify dans les déplacement restants la position representée par le decorator
    * passé en paramètre.
    * @param deco
    */
   public void deleteFromDeplacementRestants(final EmplacementDecorator deco){
      getDeplacementsRestants().set(getDeplacementsRestants().indexOf(deco), null);
   }

   /**
    * Renvoie le premier déplacement non null des déplacements restants
    * @return deco
    */
   public EmplacementDecorator findFirstFromDeplacementRestants(){
      for(final EmplacementDecorator dpctR : getDeplacementsRestants()){
         if(dpctR != null){
            return dpctR;
         }
      }
      return null;
   }
}
