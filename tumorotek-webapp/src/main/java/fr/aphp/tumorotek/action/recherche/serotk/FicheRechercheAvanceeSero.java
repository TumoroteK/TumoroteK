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
package fr.aphp.tumorotek.action.recherche.serotk;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.recherche.FicheRechercheAvancee;
import fr.aphp.tumorotek.manager.context.DiagnosticManager;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Diagnostic;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Méthodes pour les recherches avancées du contexte SEROTK
 * Class créée le 07/06/2018
 *
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 */
public class FicheRechercheAvanceeSero extends FicheRechercheAvancee
{

   /**
    *
    */
   private static final long serialVersionUID = 3235082304872498863L;

   // Contexte Serotheque
   private Listbox protocolesBox;

   private Textbox diagCompBox;

   private List<Protocole> protocoles;

   private Listbox diagSeroBox;

   private List<Diagnostic> diagnostics;

   @Override
   protected void prepareContextComponents(){
      objPrelevementContextComponents = new Component[] {this.protocolesBox, this.diagCompBox};
      objMaladieContextComponents = new Component[] {this.diagSeroBox};
   }

   @Override
   protected void initContextsLists(){
      protocoles = new ArrayList<>();
      protocoles
         .addAll(ManagerLocator.getManager(ProtocoleManager.class).findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      protocoles.add(0, null);

      diagnostics = new ArrayList<>();
      diagnostics
         .addAll(ManagerLocator.getManager(DiagnosticManager.class).findByOrderManager(SessionUtils.getPlateforme(sessionScope)));
      diagnostics.add(0, null);
   }

   @Override
   public void executeQueriesForMaladiesContext(){
      final Entite maladieEntite =
         ManagerLocator.getManager(EntiteManager.class).findByNomManager(Maladie.class.getSimpleName()).get(0);

      final ChampDelegue delegate = new ChampDelegue();
      delegate.setNom("Delegate");
      delegate.setEntite(maladieEntite);
      Champ parent1 = new Champ(delegate);
      parent1.setChampParent(parent1ToQueryMaladie);
      Champ parent2 = new Champ(delegate);
      parent2.setChampParent(parent2ToQueryMaladie);

      // pour chaque champ interrogeable pour les prlvts
      for(int i = 0; i < objMaladieContextComponents.length; i++){
         // si c'est un textbox

         if(objMaladieContextComponents[i].getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objMaladieContextComponents[i];
            // si une valeur a été saisie0
            if(current.getValue() != null && !current.getValue().equals("")){
               // exécution de la requête
               executeSimpleQueryForTextbox(current, parent1, parent2, false);

               oneValueEntered = true;
            }
         }

         // si c'est un listbox
         if(objMaladieContextComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objMaladieContextComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               if(current.getId().equals("diagSeroBox")){
                  //ChampEntite protocoles = ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Protocoles").get(0);
                  final ChampDelegue diagnostic = ManagerLocator.getManager(ChampDelegueManager.class)
                     .findByNomAndEntiteAndContexte("Diagnostic", maladieEntite, EContexte.SEROLOGIE).get(0);
                  parent1 = new Champ(diagnostic);
                  parent2 = new Champ(diagnostic);
                  final Champ champParent = new Champ(delegate);
                  champParent.setChampParent(parent1ToQueryMaladie);
                  parent1.setChampParent(champParent);
                  parent2.setChampParent(champParent);
               }

               executeSimpleQueryForListbox(current, parent1, parent2, ((String) current.getAttribute("attribut")).toLowerCase());
               oneValueEntered = true;
            }
         }
      }

   }

   @Override
   protected void executeQueriesForPrelevementsContext(){
      final Entite prlvtEntite =
         ManagerLocator.getManager(EntiteManager.class).findByNomManager(Prelevement.class.getSimpleName()).get(0);
      //ChampEntite delegate = ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Delegate").get(0);
      final ChampEntite delegate = new ChampEntite();
      delegate.setNom("Delegate");
      delegate.setEntite(prlvtEntite);
      Champ parent1 = new Champ(delegate);
      parent1.setChampParent(parent1ToQueryPrlvt);
      Champ parent2 = new Champ(delegate);
      parent2.setChampParent(parent2ToQueryPrlvt);

      // pour chaque champ interrogeable pour les prlvts
      for(int i = 0; i < objPrelevementContextComponents.length; i++){
         // si c'est un textbox

         if(objPrelevementContextComponents[i].getClass().getSimpleName().equals("Textbox")){
            final Textbox current = (Textbox) objPrelevementContextComponents[i];
            // si une valeur a été saisie0
            if(current.getValue() != null && !current.getValue().equals("")){
               if(current.getId().equals("diagCompBox")){
                  parent1 = new Champ(delegate);
                  parent1.setChampParent(parent1ToQueryPrlvt);
                  parent2 = new Champ(delegate);
                  parent2.setChampParent(parent1ToQueryPrlvt);
               }
               // exécution de la requête
               executeSimpleQueryForTextbox(current, parent1, parent2, false);

               oneValueEntered = true;
            }
         }

         // si c'est un listbox
         if(objPrelevementContextComponents[i].getClass().getSimpleName().equals("Listbox")){
            final Listbox current = (Listbox) objPrelevementContextComponents[i];
            // si une valeur a été saisie
            if(current.getSelectedIndex() > 0){
               if(current.getId().equals("protocolesBox")){
                  //ChampEntite protocoles = ManagerLocator.getChampEntiteManager().findByEntiteAndNomManager(prlvtEntite, "Protocoles").get(0);
                  final ChampDelegue protocoles = ManagerLocator.getManager(ChampDelegueManager.class)
                     .findByNomAndEntiteAndContexte("Protocoles", prlvtEntite, EContexte.SEROLOGIE).get(0);
                  parent1 = new Champ(protocoles);
                  parent2 = new Champ(protocoles);
                  final Champ champParent = new Champ(delegate);
                  champParent.setChampParent(parent1ToQueryPrlvt);
                  parent1.setChampParent(champParent);
                  parent2.setChampParent(champParent);
               }

               executeSimpleQueryForListbox(current, parent1, parent2, ((String) current.getAttribute("attribut")).toLowerCase());
               oneValueEntered = true;
            }
         }
      }
   }

   public List<Protocole> getProtocoles(){
      return protocoles;
   }

   public void setProtocoles(final List<Protocole> protocoles){
      this.protocoles = protocoles;
   }

   public List<Diagnostic> getDiagnostics(){
      return diagnostics;
   }

   public void setDiagnostics(final List<Diagnostic> diagnostics){
      this.diagnostics = diagnostics;
   }

}
