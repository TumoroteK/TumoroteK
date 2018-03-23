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
package fr.aphp.tumorotek.action.utilisateur;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import fr.aphp.tumorotek.action.MainWindow;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.annotation.FicheAnnotationInline;
import fr.aphp.tumorotek.action.controller.AbstractFicheEditController;
import fr.aphp.tumorotek.action.controller.AbstractFicheModifMultiController;
import fr.aphp.tumorotek.action.controller.AbstractFicheStaticController;
import fr.aphp.tumorotek.action.controller.AbstractObjectTabController;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class UtilisateurController extends AbstractObjectTabController
{

   private static final long serialVersionUID = -719534987553770764L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      switchToOnlyListeMode();
      setStaticEditMode(false);
   }

   @Override
   public TKdataObject loadById(final Integer id){
      return ManagerLocator.getUtilisateurManager().findByIdManager(id);
   }

   @Override
   public FicheUtilisateur getFicheCombine(){
      return ((FicheUtilisateur) this.self.getFellow("ficheUtilisateur").getFellow("fwinUtilisateur")
         .getAttributeOrFellow("fwinUtilisateur$composer", true));
   }

   @Override
   public ListeUtilisateur getListe(){
      return ((ListeUtilisateur) this.self.getFellow("listeUtilisateur").getFellow("lwinUtilisateur")
         .getAttributeOrFellow("lwinUtilisateur$composer", true));
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

   public void selectUtilisateurInListe(final Utilisateur utilisateur){
      switchToFicheAndListeMode();

      if(getFicheCombine() != null){
         getFicheCombine().setObject(utilisateur);
         getFicheCombine().switchToStaticMode();
      }

      // si la liste cachée contient l'objet, on le sélectionne
      if(getListe().getListObjects().contains(utilisateur)){
         getListe().changeCurrentObject(utilisateur);
      }else{
         getListe().deselectRow();
      }
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

   /**
    * Selectionne le tab et renvoie le controller Utilisateur.
    * @param page
    * @return controller tab
    */
   public static AbstractObjectTabController backToMe(final MainWindow window, final Tabbox tabbox){

      UtilisateurController tabController = null;

      if(tabbox != null){
         // on récupère le panel de l'entite
         final Tabpanel tab = (Tabpanel) tabbox.getFellow("utilisateursPanel");

         window.createMacroComponent("/zuls/utilisateur/Utilisateur.zul", "winUtilisateur", tab);

         tabController =
            ((UtilisateurController) tab.getFellow("winUtilisateur").getAttributeOrFellow("winUtilisateur$composer", true));

         tabbox.setSelectedPanel(tab);
      }

      return tabController;
   }
}
