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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.echantillon.AbstractEchantillonDecoratorRowRenderer;
import fr.aphp.tumorotek.action.echantillon.FicheMultiEchantillons;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
*
* Controller gérant la fiche formulaire multi échantillon sous le gestionnaire
* GATSBI.
*
* @author mathieu BARTHELEMY
* @version 2.3.0-gatsbi
*
*/
public class FicheMultiEchantillonsGatsbi extends FicheMultiEchantillons
{

   private static final long serialVersionUID = 3863329092781960062L;

   private Contexte contexte;

   private final List<Listbox> reqListboxes = new ArrayList<>();

   private final List<Combobox> reqComboboxes = new ArrayList<>();

   private final List<Div> reqDivs = new ArrayList<>(); // contient conformite Div et crAnapath

   // @wire
   private Groupbox groupEchantillon;

   private Groupbox groupInfosCompEchan;

   private final AbstractEchantillonDecoratorRowRenderer echanDecoRendererGatsbi = new EchantillonDecoratorRowRendererGatsbi();

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      contexte = GatsbiController.initWireAndDisplay(this, 3, true, reqListboxes, reqComboboxes, reqDivs, groupEchantillon,
         groupInfosCompEchan);

      // affichage conditionnel infos prelevement
      GatsbiController.initWireAndDisplayForIds(this, 2, "natureDiv");

      // inner list
      // deletable
      // force pas affichage emplacement et statut stockage en fin de grid
      GatsbiControllerEchantillon.drawColumnsForEchantillons(contexte, echantillonsList, echanDecoRendererGatsbi, true, true, false);
   }

   @Override
   protected void setGroupInfosCompEchanOpen(final boolean b){
      groupInfosCompEchan.setOpen(b);
   }

   @Override
   protected void scrollToTop(){
      // ne remonte le scroll que suite ajout échantillon
      if (!getAddedEchantillons().isEmpty()) {
         Clients.scrollIntoView(this.getSelfComponent().getFellow("gatsbiContainer"));
      }
   }
   
   @Override
   protected void scrollToBottom(){
   }

   /*********** switch required ******************/

   /**
    * Surcharge Gastbi pour conserver sélectivement la
    * contrainte de sélection obligatiure des listes nature et statut juridique
    * dans le contexte TK historique
    */
   @Override
   protected void checkRequiredListboxes(){
      GatsbiController.checkRequiredNonInputComponents(reqListboxes, null, null);
   }

   /**
    * Plus d'obligation
    */
   @Override
   public void onSelect$typesBoxEchan(){}

   @Override
   protected void prepareCrAnapath(){
      if(crAnapathNomBox.getValue() != null && !crAnapathNomBox.getValue().equals("")){
         getCrAnapath().setNom(crAnapathNomBox.getValue());
      }else{ // empty value, check required
         final Div crAnapathDiv = reqDivs.stream().filter(d -> d.getId().equals("crAnapathDiv")).findFirst().orElse(null);
         if(crAnapathDiv != null){ // required value
            throw new WrongValueException(crAnapathDiv, Labels.getLabel("validation.syntax.empty"));
         }else{ // emptyallowed
            setCrAnapath(null);
            setAnapathStream(null);
         }
      }
   }

   @Override
   public void onClick$addEchantillons(final Event event){

      GatsbiController.checkRequiredNonInputComponents(reqListboxes, reqComboboxes, reqDivs);

      super.onClick$addEchantillons(event);
   }

   /*********** inner lists ******************/

   @Override
   public AbstractEchantillonDecoratorRowRenderer getEchanDecoRenderer(){
      return echanDecoRendererGatsbi;
   }

   /*********** Paramétrages ******************/

   /**
    * Redirection d'évènement lors du choix d'un paramétrage
    */
   public void onGetInjectionDossierExterneDone(final ForwardEvent e){
      final ResultatInjection res = (ResultatInjection) e.getOrigin().getData();
      injectEchantillon(res.getEchantillon());      
   }
   
  /**
    * Si pas de paramétrage, applique le scrollToBottom non surchargé
    * pour scroller en bas de la liste vers le bouton stockage
    */
   @Override
   protected void prepareNextEchantillons() {
      GatsbiController.addNewObjectForContext(SessionUtils.getCurrentGatsbiContexteForEntiteId(3), self, 
         e -> { super.scrollToBottom(); }, null, getPrelevement());
   }   
   
   /**
    * Un parametrage échantillon a été sélectionné.
    *
    * @param param
    * @throws Exception
    */
   public void onGetSelectedParametrage(final ForwardEvent evt) throws Exception{
      try{

         GatsbiController.getSelectedParametrageFromSelectEvent(SessionUtils.getCurrentGatsbiContexteForEntiteId(3),
            SessionUtils.getCurrentBanque(sessionScope), getObjectTabController(),
            null, () -> { scrollToTop(); }, evt);
      }catch(final GatsbiException e){
         Messagebox.show(handleExceptionMessage(e), "Error", Messagebox.OK, Messagebox.ERROR);
      }
   }
   
   public void onSelectParametrageClose(){
      super.scrollToBottom();    
   }
}
