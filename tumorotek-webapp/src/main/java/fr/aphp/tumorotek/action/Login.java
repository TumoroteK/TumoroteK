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
package fr.aphp.tumorotek.action;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.zkoss.image.AImage;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
import fr.aphp.tumorotek.utils.TKStringUtils;

public class Login extends AbstractController
{

   private static final long serialVersionUID = 1L;

   private Label errorLabel;

   private Image imgLogo;

   private Html htmlMsg;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

      final ParametresManager parametresManager = ManagerLocator.getManager(ParametresManager.class);

      //Chargement du message d'accueil personnalisé
      String msgAccueil = parametresManager.getMessageAccueil(false);

      if(StringUtils.isEmpty(msgAccueil)){
         msgAccueil = Labels.getLabel("login.welcome");
      }

      //Chargement du logo personnalisé
      AImage logo = null;

      final File logoFile = parametresManager.getLogoFile();
      if(logoFile.exists()){
         logo = new AImage(logoFile);
      }

      if(logo != null){
         imgLogo.setVisible(true);
         imgLogo.setContent(logo);
      }

      if(StringUtils.isNotEmpty(msgAccueil)){
         htmlMsg.setVisible(true);
         htmlMsg.setContent(TKStringUtils.cleanHtmlString(msgAccueil));
      }

      final AuthenticationException loginException =
         (AuthenticationException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

      setErrorMessage(loginException);

   }

   public void setErrorMessage(final Exception ex){
      if(ex != null){
         if((ex instanceof SessionAuthenticationException) && (ex.getMessage().matches("Maximum.*"))){
            errorLabel.setValue(Labels.getLabel("login.conc.error"));
         }else{
            errorLabel.setValue(Labels.getLabel("login.error"));
         }
      }else{
         errorLabel.setValue("");
      }
   }

}
