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
package fr.aphp.tumorotek.action.contexte;

import org.zkoss.zk.ui.Component;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.TKdataObject;

public class TransporteurController extends AbstractObjectTabController
{

   private static final long serialVersionUID = 2265062196553397931L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      switchToOnlyListeMode();
      setStaticEditMode(false);
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getTransporteurManager().findByIdManager(id);
   }

   /**
    * Méthode permettant de passer en mode "fiche & liste" : liste 
    * et la fiche sont visibles.
    * Détache la fiche édition/création
    */
   @Override
   public void switchToFicheAndListeMode(){
      if(listeRegion.getWidth().equals("100%")){
         listeRegion.setWidth("40%");
      }
      listeRegion.setOpen(true);

      if(isStaticEditMode()){
         showStatic(true);
         clearEditDiv();
      }
   }

   @Override
   public FicheTransporteur getFicheCombine(){
      return ((FicheTransporteur) this.self.getFellow("ficheTransporteur").getFellow("fwinTransporteur")
         .getAttributeOrFellow("fwinTransporteur$composer", true));
   }

   @Override
   public ListeTransporteur getListe(){
      return ((ListeTransporteur) this.self.getFellow("listeTransporteur").getFellow("lwinTransporteur")
         .getAttributeOrFellow("lwinTransporteur$composer", true));
   }

   @Override
   public FicheAnnotation getFicheAnnotation(){
      return null;
   }

   @Override
   public FicheAnnotationInline getFicheAnnotationInline(){
      return null;
   }

   @Override
   public AbstractFicheEditController getFicheEdit(){
      return null;
   }

   @Override
   public AbstractFicheModifMultiController getFicheModifMulti(){
      return null;
   }

   @Override
   public AbstractFicheStaticController getFicheStatic(){
      return null;
   }

}
