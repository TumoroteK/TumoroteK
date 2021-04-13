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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.ProfilUtilisateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Renderer d'un Utilisateur.
 *
 * @author pierre
 *
 */
public class UtilisateurRowRenderer implements RowRenderer<Utilisateur>
{

   private Utilisateur user;

   @Override
   public void render(final Row row, final Utilisateur utilisateur, final int index) throws Exception{

      // code
      final Label codeLabel = new Label(utilisateur.getLogin());
      codeLabel.addForward(null, codeLabel.getParent(), "onClickObject", utilisateur);
      if(utilisateur.isArchive()){
         codeLabel.setClass("formLinkArchive");
      }else{
         codeLabel.setClass("formLink");
      }
      codeLabel.setParent(row);

      // Gestion des autorisations
      final Vbox vBox = new Vbox();

      // si admin général
      if(utilisateur.isSuperAdmin()){
         final StringBuffer sb = new StringBuffer(Labels.getLabel("utilisateur.admin.general"));
         final Label autorisationLabel = new Label(sb.toString());
         if(utilisateur.isArchive()){
            autorisationLabel.setClass("formArchiveValue");
         }
         autorisationLabel.setParent(vBox);
         
         row.setSclass("gold");
      }

      // si admin de plateforme
      final Set<Plateforme> pfs = ManagerLocator.getUtilisateurManager().getPlateformesManager(utilisateur);
      final Iterator<Plateforme> it = pfs.iterator();
      // on parcourt les plateformes
      while(it.hasNext()){
         // on ajoute le nom de la plateforme et le role
         final StringBuffer sb = new StringBuffer();
         sb.append(it.next().getNom());
         sb.append(" - ");
         sb.append(Labels.getLabel("utilisateur.admin.plateforme"));
         final Label autorisationLabel = new Label(sb.toString());
         if(utilisateur.isArchive()){
            autorisationLabel.setClass("formArchiveValue");
         }
         autorisationLabel.setParent(vBox);
      }

      // si admin de collection

      // on récup les banques avec des droits d'admin
      final List<Banque> availableBanques = ManagerLocator.getUtilisateurManager().getAvailableBanquesAsAdminManager(user);
      final List<ProfilUtilisateur> profils =
         ManagerLocator.getProfilUtilisateurManager().findByUtilisateurManager(utilisateur, false);
      // pour chaque collection
      for(int i = 0; i < profils.size(); i++){
         if(availableBanques.contains(profils.get(i).getBanque())){
            // on ajoute le nom de la banque et le role
            final StringBuffer sb = new StringBuffer();
            
            // @since 2.2.1
            // ajoute la PF si pas la PF courante
            if (profils.get(i).getBanque().getPlateforme().equals(SessionUtils.getCurrentPlateforme())) {
            	sb.append(profils.get(i).getBanque().getNom());
            } else {
            	sb.append(profils.get(i).getBanque().getBanqueAndPlateformeNoms());
            }
            sb.append(" - ");
            sb.append(profils.get(i).getProfil().getNom());
            final Label autorisationLabel = new Label(sb.toString());
            if(utilisateur.isArchive()){
               autorisationLabel.setClass("formArchiveValue");
            }
            autorisationLabel.setParent(vBox);
         }
      }
      vBox.setParent(row);

      new Label(utilisateur.getPlateformeOrig() != null ?   
    		utilisateur.getPlateformeOrig().getNom() : "").setParent(row);

      new Label(ObjectTypesFormatters.dateRenderer2(ManagerLocator
    	.getOperationManager().findDateCreationManager(utilisateur)))
         .setParent(row);

      new Label(ObjectTypesFormatters.dateRenderer2(utilisateur.getTimeOut())).setParent(row);

   }

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur u){
      this.user = u;
   }

}
