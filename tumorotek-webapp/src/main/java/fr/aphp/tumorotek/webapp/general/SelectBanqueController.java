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
package fr.aphp.tumorotek.webapp.general;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.prelevement.gatsbi.exception.GatsbiException;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.ext.ResourceRequest;
import fr.aphp.tumorotek.dto.ParametreDTO;
import fr.aphp.tumorotek.manager.administration.ParametresManager;
/**
 * Modale choix plateforme et banque après authentification.
 * Gère l'archivage automatique si timeout.
 * Affiche et enregistre la dernière connection si paramètrée
 * dans tumorotek.properties.
 * Redirige automatiquement si une seule collection disponible.
 * Date: 16/06/2012
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1
 *
 */
public class SelectBanqueController extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 1L;

   private List<Banque> banques = new ArrayList<>();

   private Banque selectedBanque;

   private List<Plateforme> plateformes = new ArrayList<>();

   private Plateforme selectedPlateforme;

   private Utilisateur user;

   private Row rowPlateformeTitle;

   private Row rowPlateforme;

   private Row rowBanqueTitle;

   private Row rowBanque;

   private Row rowMdpWarning;

   private Html labelMdpWarning;

   private Label lastCxLabel;

   private Button validate;

   private Button logout;

   private Component[] selectionComponents;

   private Row rowMdpArchive1;

   private Row rowMdpArchive2;

   private Row rowMdpArchive3;

   private Component[] archiveComponents;

   private Row ldapErrorRow;

   private Row rowRedirect;

   private Row rowResourceRedirect;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      selectionComponents =
         new Component[] {this.rowPlateformeTitle, this.rowPlateforme, this.rowBanqueTitle, this.rowBanque, this.validate};
      archiveComponents = new Component[] {this.rowMdpArchive1, this.rowMdpArchive2, this.rowMdpArchive3};

      //user = getLoggedUtilisateur();
      user = ConnexionUtils.getLoggedUtilisateur();
		//Si l'utilisateur est archivé, il ne peut pas se connecter => on force à null comme si l'utilisateur n'existait pas
		if(user != null && user.isArchive()) {
		   user = null;
		}

      initWindow();

      ConnexionUtils.initToutesCollectionsAccesses(banques, selectedPlateforme, user);
   }

   public void initWindow(){
      boolean archive = false;
      Integer nbJours = 0;

      // since 2.0.11
      // Auth LDAP peux produire user == null car auth OK
      // mais pas login correspondant dans TK
      if(user != null){
         if(user.getTimeOut() != null){
            final Calendar today = Calendar.getInstance();
            final Calendar userCal = Calendar.getInstance();
            userCal.setTime(user.getTimeOut());

            archive = today.after(userCal);

            if(!archive){
               while(today.before(userCal) && nbJours < 31){
                  today.add(Calendar.DAY_OF_MONTH, 1);
                  nbJours++;
               }
            }
         }
         if(archive){
            for(int i = 0; i < archiveComponents.length; i++){
               archiveComponents[i].setVisible(true);
            }
            for(int i = 0; i < selectionComponents.length; i++){
               selectionComponents[i].setVisible(false);
            }

            // archivage de l'utilisateur
            ManagerLocator.getUtilisateurManager().archiveUtilisateurManager(user, user);
         }else{

            // on récupère le bundle de paramétrage de l'application
            ResourceBundle res = null;
            if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
               res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
            }
            // on récupère la propriété définissant si on doit sauver
            // les connexions
            String lastCxValue = "";
            if(null != res && res.containsKey(TkParam.SAUVER_CONNEXION.getKey())
               && res.getString(TkParam.SAUVER_CONNEXION.getKey()) != null
               && res.getString(TkParam.SAUVER_CONNEXION.getKey()).equals("true")){
               // last connection
               final Operation lastCx = ManagerLocator.getOperationManager().findLastByUtilisateurAndTypeManager(
                  ManagerLocator.getOperationTypeManager().findByNomLikeManager("Login", true).get(0), user, 1);
               if(lastCx != null){
                  lastCxValue = Labels.getLabel("login.last.date") + " "
                     + new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.getDefault()).format(lastCx.getDate().getTime());
               }
               // sauvegarde de la connexion
               saveConnection();
            }
            lastCxLabel.setValue(lastCxValue);

            initPlateformesAndBanques();

            for(int i = 0; i < archiveComponents.length; i++){
               archiveComponents[i].setVisible(false);
            }
            for(int i = 0; i < selectionComponents.length; i++){
               selectionComponents[i].setVisible(true);
            }

            // s'il n'y a qu'une seule pf, on cache les lignes de
            // sélection
            if(plateformes.size() <= 1){
               rowPlateforme.setVisible(false);
               rowPlateformeTitle.setVisible(false);
            }

            // si le mdp expire dans moins de 30 jours, on va afficher
            // un warning
            if(nbJours < 31 && nbJours > 0){
               labelMdpWarning
                  .setContent(ObjectTypesFormatters.getLabel("login.compte.warning", new String[] {String.valueOf(nbJours)}));
               rowMdpWarning.setVisible(true);
            }else{ // autoconnect si une seule collection et rien à afficher
               if(plateformes.size() <= 1 && banques.size() == 1){
                  logout.setVisible(false);
                  for(int i = 0; i < selectionComponents.length; i++){
                     selectionComponents[i].setVisible(false);
                  }
                  rowRedirect.setVisible(true);

                  onClick$validate();
               }
            }

            // redirect si demande resources extérieures
            // @since 2.2.2-diamic, la redirection peux concerner plusieurs prélèvements
            if(sessionScope.containsKey("resourceRequest")){
               if(!((ResourceRequest<?>) sessionScope.get("resourceRequest")).isEmpty()){

                  // banque et plateforme
                  setSelectedBanque(((ResourceRequest<?>) sessionScope.get("resourceRequest")).getBanque());
                  setSelectedPlateforme(((ResourceRequest<?>) sessionScope.get("resourceRequest")).getBanque().getPlateforme());

                  for(int i = 0; i < selectionComponents.length; i++){
                     selectionComponents[i].setVisible(false);
                  }
                  rowResourceRedirect.setVisible(true);
                  Events.echoEvent("onClick$validate", self, null);

               }
            }
         }
      }else{ // LDAP auth ok mais pas compte TK associe
         for(int i = 0; i < selectionComponents.length; i++){
            selectionComponents[i].setVisible(false);
         }
         for(int i = 0; i < archiveComponents.length; i++){
            archiveComponents[i].setVisible(false);
         }
         ldapErrorRow.setVisible(true);
         ((Html) ldapErrorRow.getFirstChild())
            .setContent(ObjectTypesFormatters.getLabel("login.ldap.nulluser", new String[] {ConnexionUtils.getLoggedLogin()}));
      }
   }

   public void initPlateformesAndBanques(){
      // on récupère les plateformes accessibles
      plateformes = ManagerLocator.getUtilisateurManager().getAvailablePlateformesManager(user);

      if(plateformes.size() > 0){
         selectedPlateforme = plateformes.get(0);
      }else{
         selectedPlateforme = null;
      }

      // init des banques
      banques = ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(user, selectedPlateforme, false);

      if(banques.size() > 0){
         selectedBanque = banques.get(0);
      }else{
         selectedBanque = null;
      }
   }

   /**
    * Méthode appelée lors de l'appui sur la touche ENTREE.
    */
   public void onOK(){
      onClick$validate();
   }

   /**
    * Recupere la banque selectionne et la passe en variable de session,
    * prepare les droits et les catalogues.
    * Si 'toutes collections' est choisi, passe en variable de session
    * la liste de banque pour lesquelles l'utilisateur a des droits
    * d'admin.
    * Si aucune collection disponible (selectBanque = null) alors permet
    * la connection que si l'utilisateur est superAdmin.
    */
   public void onClick$validate(){
      if(selectedBanque != null){
         try{
            ConnexionUtils.selectConnection(user, selectedPlateforme, selectedBanque, banques, session);
            Executions.sendRedirect("/zuls/main/main.zul");
         }catch(final GatsbiException e){
            throw new WrongValueException(rowBanque.getFirstChild(), AbstractController.handleExceptionMessage(e));
         }
      }else{
         // cas admin première installation aucune collection
         // et TK-27 plateforme nouvellement créée par adminPF
         if(user.isSuperAdmin()
            || ManagerLocator.getUtilisateurManager().getPlateformesManager(user).contains(selectedPlateforme)){
            final Map<String, Object> sessionScp = session.getAttributes();
            sessionScp.put("User", user);

            sessionScp.put("Plateforme", selectedPlateforme);
            // Enregistre la liste de paramètres dans la session
            SessionUtils.savePlatformParamsToSession(sessionScope);

            ConnexionUtils.generateDroitsForSelectedBanque(null, selectedPlateforme, user, sessionScope);
            Executions.sendRedirect("/zuls/main/main.zul");
         }
      }
   }

   public void onClientInfo$winSelectBanque(final ClientInfoEvent event){
      sessionScope.put("screenWidth", event.getDesktopWidth());
      sessionScope.put("screenHeight", event.getDesktopHeight());
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque selected){
      this.selectedBanque = selected;
   }

   /**
    * Filtre les banques par plateforme.
    * @param event Event : seléction sur la liste plateformesBox.
    * @throws Exception
    */
   public void onSelect$plateformesBox(final Event event) throws Exception{
      if(selectedPlateforme != null){
         banques = ManagerLocator.getUtilisateurManager().getAvailableBanquesByPlateformeManager(user, selectedPlateforme, false);
      }else{
         banques = new ArrayList<>();
      }

      ConnexionUtils.initToutesCollectionsAccesses(banques, selectedPlateforme, user);

      if(banques.size() > 0){
         selectedBanque = banques.get(0);
      }else{
         selectedBanque = null;
      }
   }

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur u){
      this.user = u;
   }

   public static void setTheme(final Execution exe, final String theme){
      final Cookie cookie = new Cookie("zktheme", theme);
      //store 30 days
      cookie.setMaxAge(60 * 60 * 24 * 30);
      String cp = exe.getContextPath();
      // if path is empty, cookie path will be request path,
      // which causes problems
      if(cp.length() == 0){
         cp = "/";
      }
      cookie.setPath(cp);
      ((HttpServletResponse) exe.getNativeResponse()).addCookie(cookie);
   }

   /**
    * Cette méthode va sauvegarder en base l'opération de connexion
    * à TK (si l'application est paramétrée comme tel).
    */
   public void saveConnection(){

      // sauvegarde de la connection
      final Operation creationOp = new Operation();
      creationOp.setDate(Utils.getCurrentSystemCalendar());
      final List<OperationType> tmp = ManagerLocator.getOperationTypeManager().findByNomLikeManager("Login", true);
      if(tmp.size() > 0){
         ManagerLocator.getOperationManager().createObjectManager(creationOp, user, tmp.get(0), user);

      }
   }

   public List<Plateforme> getPlateformes(){
      return plateformes;
   }

   public void setPlateformes(final List<Plateforme> p){
      this.plateformes = p;
   }

   public Plateforme getSelectedPlateforme(){
      return selectedPlateforme;
   }

   public void setSelectedPlateforme(final Plateforme sPlateforme){
      this.selectedPlateforme = sPlateforme;
   }
}
