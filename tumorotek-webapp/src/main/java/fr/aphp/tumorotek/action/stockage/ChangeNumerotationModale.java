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
package fr.aphp.tumorotek.action.stockage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.Errors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleListModel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectReferencedException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;
import fr.aphp.tumorotek.model.stockage.TerminaleNumerotation;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Modale permettant d'informer l'utilisateur sur la modification
 * de la numérotation d'une liste de terminales.
 * Date: 07/12/2011
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class ChangeNumerotationModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = -4675282379020609559L;

   private Image warnImg;
   private Image stopImg;
   private Html warnLabel;
   private Listbox numerotationsBox;
   private MainWindow main;
   private Row rowNumerotationTerminales;
   private Button validate;

   private Conteneur conteneur;
   private Enceinte enceinte;
   private List<Terminale> terminales = new ArrayList<>();
   private List<TerminaleNumerotation> numerotations = new ArrayList<>();
   private TerminaleNumerotation selectedNumerotation;

   public void init(final Object parent, final MainWindow mw){
      main = mw;
      if(parent != null){
         // récupération des terminales enfants
         if(parent instanceof Conteneur){
            conteneur = (Conteneur) parent;
            terminales = ManagerLocator.getConteneurManager().getAllTerminalesInArborescenceManager(conteneur);
         }else if(parent instanceof Enceinte){
            enceinte = (Enceinte) parent;
            terminales = ManagerLocator.getEnceinteManager().getAllTerminalesInArborescenceManager(enceinte);
         }
      }

      // affichage du warning ou du stop si jamais il n'y a pas
      // de terminales à modifier
      if(terminales.size() > 0){
         warnImg.setVisible(true);
         if(conteneur != null){
            warnLabel.setContent(ObjectTypesFormatters.getLabel("terminale.numerotation.warn.conteneur",
               new String[] {conteneur.getCode(), String.valueOf(terminales.size())}));
         }else{
            warnLabel.setContent(ObjectTypesFormatters.getLabel("terminale.numerotation.warn.enceinte",
               new String[] {enceinte.getNom(), String.valueOf(terminales.size())}));
         }
      }else{
         stopImg.setVisible(true);
         warnLabel.setContent(Labels.getLabel("terminale.numerotation.stop"));
         rowNumerotationTerminales.setVisible(false);
         validate.setDisabled(true);
      }

      // récup' des numérotations
      numerotations = ManagerLocator.getTerminaleNumerotationManager().findAllObjectsManager();
      numerotations.add(0, null);
      selectedNumerotation = null;

      numerotationsBox.setModel(new SimpleListModel<>(numerotations));
      if(!numerotations.isEmpty()){
         numerotationsBox.setSelectedItem(numerotationsBox.getItemAtIndex(0));
      }
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onClick$validate(){
      if(numerotationsBox.getSelectedItem() != null){
         selectedNumerotation = (TerminaleNumerotation) numerotationsBox.getSelectedItem().getValue();
      }else{
         throw new WrongValueException(numerotationsBox, Labels.getLabel("validation.syntax.empty"));
      }

      if(selectedNumerotation == null){
         throw new WrongValueException(numerotationsBox, Labels.getLabel("validation.syntax.empty"));
      }

      Clients.showBusy(null);
      Events.echoEvent("onLaterUpdate", self, null);
   }

   public void onLaterUpdate(){
      try{
         // MAJ de toutes les terminales
         ManagerLocator.getTerminaleManager().updateNumerotationForMultiTerminales(terminales, selectedNumerotation,
            SessionUtils.getLoggedUser(sessionScope));

         // fermeture de la fenêtre
         Events.postEvent(new Event("onClose", self.getRoot()));

         // update de la fenêtre
         getMain().updateSelectedBanque(SessionUtils.getSelectedBanques(sessionScope).get(0));

         // ferme wait message
         Clients.clearBusy();
      }catch(final RuntimeException re){
         Clients.clearBusy();
         Messagebox.show(handleExceptionMessage(re), "Error", Messagebox.OK, Messagebox.ERROR);
      }finally{
         Clients.clearBusy();
      }
   }

   /**
    * Génère le message qui sera affiché dans la fenêtre d'erreurs.
    */
   public String handleExceptionMessage(final RuntimeException ex){
      String message = Labels.getLabel("validation.exception.inconnu");
      if(ex instanceof ValidationException){
         message = Labels.getLabel("validation.error");
         message = message + "\n";
         final Iterator<Errors> errs = (((ValidationException) ex).getErrors()).iterator();
         String errCode;
         while(errs.hasNext()){
            errCode = errs.next().getFieldError().getCode();
            if(Labels.getLabel(errCode) != null){
               message = message + Labels.getLabel(errCode);
            }else{
               message = message + "TK erreur label: " + errCode;
            }

            message = message + "\n";
         }
      }else if(ex instanceof DoublonFoundException){
         message = ObjectTypesFormatters.getLabel("validation.doublon",
            new String[] {((DoublonFoundException) ex).getEntite(), ((DoublonFoundException) ex).getOperation()});
      }else if(ex instanceof RequiredObjectIsNullException){
         message =
            ObjectTypesFormatters
               .getLabel("validation.requiredObject",
                  new String[] {((RequiredObjectIsNullException) ex).getEntite(),
                     ((RequiredObjectIsNullException) ex).getRequiredObject(),
                     ((RequiredObjectIsNullException) ex).getOperation()});
      }else if(ex instanceof ObjectUsedException){
         message = Labels.getLabel(((ObjectUsedException) ex).getKey());
      }else if(ex instanceof ObjectReferencedException){
         message = Labels.getLabel(((ObjectReferencedException) ex).getKey());
      }else if(ex instanceof WrongValueException){
         throw ex;
      }else{
         message = Labels.getLabel(ex.getMessage());
      }
      // aucun message n'a pu être généré -> exception inattendue
      if(message == null){
         message = ex.getClass().getSimpleName() + " : " + ex.getMessage();
      }

      return message;
   }

   public Conteneur getConteneur(){
      return conteneur;
   }

   public void setConteneur(final Conteneur c){
      this.conteneur = c;
   }

   public Enceinte getEnceinte(){
      return enceinte;
   }

   public void setEnceinte(final Enceinte e){
      this.enceinte = e;
   }

   public List<Terminale> getTerminales(){
      return terminales;
   }

   public void setTerminales(final List<Terminale> t){
      this.terminales = t;
   }

   public List<TerminaleNumerotation> getNumerotations(){
      return numerotations;
   }

   public void setNumerotations(final List<TerminaleNumerotation> n){
      this.numerotations = n;
   }

   public TerminaleNumerotation getSelectedNumerotation(){
      return selectedNumerotation;
   }

   public void setSelectedNumerotation(final TerminaleNumerotation s){
      this.selectedNumerotation = s;
   }

   public MainWindow getMain(){
      return main;
   }

   public void setMain(final MainWindow m){
      this.main = m;
   }

}
