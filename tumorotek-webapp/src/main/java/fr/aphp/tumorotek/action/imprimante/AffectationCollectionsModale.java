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
package fr.aphp.tumorotek.action.imprimante;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Classe permettant à l'utilisateur de d'affecter une imprimante
 * et un modèle pour une liste de banques.
 * @author pierre Ventadour.
 *
 */
public class AffectationCollectionsModale extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = 105656812700726498L;

   private Panel winPanel;

   private Listbox imprimantesBox;

   private Component parent;

   private List<Banque> banques = new ArrayList<>();

   private Utilisateur utilisateur;

   private Plateforme plateforme;

   private AnnotateDataBinder binder;

   private List<Imprimante> imprimantes = new ArrayList<>();

   private Imprimante selectedImprimante;

   private List<Modele> modeles = new ArrayList<>();

   private Modele selectedModele;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      binder = new AnnotateDataBinder(comp);
      binder.loadComponent(comp);

      if(winPanel != null){
         winPanel.setHeight("215px");
      }
   }

   public void init(final List<Banque> bks, final Component p, final Utilisateur user, final Plateforme pf){
      parent = p;
      banques = bks;
      utilisateur = user;
      plateforme = pf;

      // init des imprimantes et modèles
      imprimantes = ManagerLocator.getImprimanteManager().findByPlateformeManager(plateforme);
      imprimantes.add(0, null);
      selectedImprimante = null;
      modeles = ManagerLocator.getModeleManager().findByPlateformeManager(plateforme);
      modeles.add(0, null);
      selectedModele = null;

      getBinder().loadComponent(self);
   }

   public void onClick$cancel(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onSelect$imprimantesBox(){
      // validation
      if(selectedImprimante == null){
         throw new WrongValueException(imprimantesBox, Labels.getLabel("etiquette.choix.imprimante.obligatoire"));
      }
      Clients.clearWrongValue(imprimantesBox);
   }

   /**
    * Lance la recherche.
    */
   public void onClick$validate(){
      if(selectedImprimante == null){
         throw new WrongValueException(imprimantesBox, Labels.getLabel("etiquette.choix.imprimante.obligatoire"));
      }

      Clients.showBusy(null);
      Events.echoEvent("onLaterValidate", self, null);
   }

   /**
    * Execution de l'enregistrement.
    */
   public void onLaterValidate(){
      // pour chaque banque on va sauvegarder l'affectation
      for(int i = 0; i < banques.size(); i++){
         AffectationImprimante ai = null;
         // on cherche si une affectation existait déjà
         final List<AffectationImprimante> liste =
            ManagerLocator.getAffectationImprimanteManager().findByBanqueUtilisateurManager(banques.get(i), utilisateur);
         if(liste.size() > 0){
            ai = liste.get(0);
         }else{
            ai = new AffectationImprimante();
         }

         // on sauvegarde l'affectation
         ManagerLocator.getAffectationImprimanteManager().createObjectManager(new AffectationImprimante(), utilisateur,
            banques.get(i), selectedImprimante, selectedModele);

         // l'imprimante faisant partie de la PK, si celle ci a changé
         // et qu'une affectation existait, on la supprime
         if(ai.getImprimante() != null && !ai.getImprimante().equals(selectedImprimante)){
            ManagerLocator.getAffectationImprimanteManager().removeObjectManager(ai);
         }
      }

      // on renvoie les résultats
      Events.postEvent("onGetAffectationsDone", getParent(), null);
      // ferme wait message
      Clients.clearBusy();
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   /****************************************************/
   /************     GETTERS et SETTERS     ************/
   /****************************************************/

   public String getBanquesFormated(){
      final StringBuffer sb = new StringBuffer();

      for(int i = 0; i < banques.size(); i++){
         sb.append(banques.get(i).getNom());

         if(i + 1 < banques.size()){
            sb.append(", ");
         }else{
            sb.append(".");
         }
      }

      return sb.toString();
   }

   public Component getParent(){
      return parent;
   }

   public void setParent(final Component p){
      this.parent = p;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   public Plateforme getPlateforme(){
      return plateforme;
   }

   public void setPlateforme(final Plateforme p){
      this.plateforme = p;
   }

   public AnnotateDataBinder getBinder(){
      return binder;
   }

   public void setBinder(final AnnotateDataBinder b){
      this.binder = b;
   }

   public List<Imprimante> getImprimantes(){
      return imprimantes;
   }

   public void setImprimantes(final List<Imprimante> i){
      this.imprimantes = i;
   }

   public Imprimante getSelectedImprimante(){
      return selectedImprimante;
   }

   public void setSelectedImprimante(final Imprimante s){
      this.selectedImprimante = s;
   }

   public List<Modele> getModeles(){
      return modeles;
   }

   public void setModeles(final List<Modele> m){
      this.modeles = m;
   }

   public Modele getSelectedModele(){
      return selectedModele;
   }

   public void setSelectedModele(final Modele s){
      this.selectedModele = s;
   }

}
