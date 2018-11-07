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

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;

/**
 * Renderer liste utilisateurs + profils affichée dans la fiche détaillée FicheBanque.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class ProfilUtilisateurRowRenderer implements RowRenderer<ProfilUtilisateur>
{

   // @since 2.1
   // si true render login - banque
   // sinon rendre login - profil
   private boolean banqueRender = false;
   private boolean editMode;

   public ProfilUtilisateurRowRenderer(final boolean _b){
      banqueRender = _b;
   }

   @Override
   public void render(final Row row, final ProfilUtilisateur profil, final int index) throws Exception{

      // login
      final Label loginLabel = new Label(profil.getUtilisateur().getLogin());
      if(profil.getUtilisateur().isArchive()){
         loginLabel.setClass("formLinkArchive");
      }else{
         loginLabel.setClass("formLink");
      }
      loginLabel.addForward(null, loginLabel.getParent(), "onClickProfilUtilisateur", profil);
      loginLabel.setParent(row);

      if(!banqueRender){
         // profil
         final Label profilLabel = new Label(profil.getProfil().getNom());
         // @since 2.1
         if(profil.getProfil().isArchive()){
            profilLabel.setClass("formLinkArchive");
         }else{
            profilLabel.setClass("formLink");
         }
         profilLabel.addForward(null, profilLabel.getParent(), "onClickProfilUtilisateurProfil", profil);
         profilLabel.setParent(row);
      }else{
         // banque
         final Label banqueLabel = new Label(profil.getBanque().getNom());
         // @since 2.1
         if(profil.getBanque().getArchive()){
            banqueLabel.setClass("formLinkArchive");
         }else{
            banqueLabel.setClass("formLink");
         }
         banqueLabel.addForward(null, banqueLabel.getParent(), "onClickProfilUtilisateurBanque", profil);
         banqueLabel.setParent(row);
      }
      
      if(editMode) {
         Image removeIcon = new Image("/images/icones/small_delete.png");
         removeIcon.addForward(Events.ON_CLICK, removeIcon.getParent(), "onClickRemoveProfil", profil);
         removeIcon.setStyle("cursor:pointer");
         removeIcon.setParent(row);
      }
      
   }

   public boolean isEditMode(){
      return editMode;
   }

   public void setEditMode(boolean editMode){
      this.editMode = editMode;
   }

   
   
}
