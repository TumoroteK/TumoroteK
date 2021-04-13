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
package fr.aphp.tumorotek.action.prelevement.serotk;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Label;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prelevement.FicheModifMultiPrelevement;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @author GCH
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 *
 */
public class FicheModifMultiPrelevementSero extends FicheModifMultiPrelevement
{

   private static final long serialVersionUID = 1759167562352668210L;

   private Label protocolesLabelChanged;
   private Label complDiagLabelChanged;

   public void onClick$protocoleMultiLabel(){

      final List<? extends Object> protocoles = ManagerLocator.getManager(ProtocoleManager.class).findByOrderManager(SessionUtils.getCurrentPlateforme());

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "MultiListbox", (List<Object>) getObjsToEdit(),
         "Champ.Prelevement.SEROLOGIE.Protocoles", "protocoles", (List<Object>) protocoles, "nom", null, null, false, null,
         false);
   }

   public void onClick$complDiagMultiLabel() {
      
      final List<? extends Object> protocoles = ManagerLocator.getManager(ProtocoleManager.class).findByOrderManager(SessionUtils.getCurrentPlateforme());

      openModificationMultipleWindow(page, Path.getPath(self), "onGetChangeOnChamp", "Textbox", (List<Object>) getObjsToEdit(),
         "Champ.Prelevement.SEROLOGIE.Libelle", "libelle", (List<Object>) protocoles, null, null, null, false, null,
         false);
      
   }
   
   @Override
   public void updateLabelChanged(String champ, String printValue, boolean reset){

      super.updateLabelChanged(champ, printValue, reset);

      if("protocoles".equals(champ)){
         protocolesLabelChanged.setValue(printValue);
         protocolesLabelChanged.setVisible(!reset);
      }
      else if("libelle".equals(champ)) {
         complDiagLabelChanged.setValue(printValue);
         complDiagLabelChanged.setVisible(!reset);
      }

   }

   /**
    * Ajout des champs d'information spécifiques au contexte 
    * Sérothèque
    * @since 2.2.1
    */
   @Override
   protected boolean checkHasChanged(Prelevement current) {

      boolean hasAnyChange = super.checkHasChanged(current);

     // maj de protocoles
     if(!"".equals(protocolesLabelChanged.getValue())){

        PrelevementSero delegate = null;
        if(null == current.getDelegate()) {
           delegate = new PrelevementSero();
           current.setDelegate(delegate);
           delegate.setDelegator(current);
        }
        else {
           delegate = (PrelevementSero)current.getDelegate();
        }

        delegate.setProtocoles(((PrelevementSero)getObject().getDelegate()).getProtocoles());
        hasAnyChange = true;

     }
     // maj de complément diagnostic
     else if(!"".equals(complDiagLabelChanged.getValue())){

        PrelevementSero delegate = null;
        if(null == current.getDelegate()) {
           delegate = new PrelevementSero();
           current.setDelegate(delegate);
           delegate.setDelegator(current);
        }
        else {
           delegate = (PrelevementSero)current.getDelegate();
        }

        delegate.setLibelle(((PrelevementSero)getObject().getDelegate()).getLibelle());
        hasAnyChange = true;

     }

      return hasAnyChange;

   }

}
