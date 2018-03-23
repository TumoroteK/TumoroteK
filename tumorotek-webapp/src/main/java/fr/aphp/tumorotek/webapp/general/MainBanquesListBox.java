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

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;

import fr.aphp.tumorotek.action.MainWindow;

public class MainBanquesListBox extends Listbox
{

   private static final long serialVersionUID = -8780717656148814516L;

   /**
    * Méthode appelée lors de la sélection d'une banque : tous les 
    * formulaires, listes... seront ré-initialisés.
    */
   public void onSelect(){
      Clients.showBusy(Labels.getLabel("general.changement.banque.encours"));
      Events.echoEvent("onLaterUpdate", this, null);
   }

   public void onLaterUpdate(){
      getMainWindow().updateSelectedBanque(null);

      Clients.clearBusy();
   }

   //	
   //	/**
   //	 * Méthode récupérant le controller du panel des protocolesExt.
   //	 * @return ContratController classe gérant le panel 
   //	 * des ProtocoleExt.
   //	 */
   //	public AnnotationsController getAnnotationsController() {
   //		
   //		Tabbox mainTabbox = getMainTabbox();
   //		Tabpanels panels = mainTabbox.getTabpanels();
   //		
   //		return (AnnotationsController) panels
   //				.getFellow("administrationPanel")
   //				.getFellow("winAdministration")
   //				.getFellow("adminTabbox")
   //				.getFellow("annotationsPanel")
   //				.getFellow("annotationsMacro")
   //				.getFellow("winAnnotations")
   //				.getAttributeOrFellow("winAnnotations$composer", true);
   //		
   //	}
   //	
   //	/**
   //	 * Méthode récupérant le controller du panel des protocolesExt.
   //	 * @return ContratController classe gérant le panel 
   //	 * des ProtocoleExt.
   //	 */
   //	public ContratController getContratController() {
   //		
   //		Tabbox mainTabbox = getMainTabbox();
   //		Tabpanels panels = mainTabbox.getTabpanels();
   //		
   //		return (ContratController) panels
   //				.getFellow("administrationPanel")
   //				.getFellow("winAdministration")
   //				.getFellow("adminTabbox")
   //				.getFellow("contratPanel")
   //				.getFellow("contratMacro")
   //				.getFellow("winContrat")
   //				.getAttributeOrFellow("winContrat$composer", true);
   //		
   //	}
   //	
   //	/**
   //	 * Méthode récupérant le controller du panel des collaborations.
   //	 * @return ContratController classe gérant le panel 
   //	 * des ProtocoleExt.
   //	 */
   //	public CollaborationsController getCollaborationsController() {
   //		
   //		Tabbox mainTabbox = getMainTabbox();
   //		Tabpanels panels = mainTabbox.getTabpanels();
   //		
   //		return (CollaborationsController) panels
   //				.getFellow("administrationPanel")
   //				.getFellow("winAdministration")
   //				.getFellow("adminTabbox")
   //				.getFellow("collaborationsPanel")
   //				.getFellow("collaborationsMacro")
   //				.getFellow("winCollaborations")
   //				.getAttributeOrFellow("winCollaborations$composer", true);
   //		
   //	}
   //	
   //	/**
   //	 * Méthode récupérant le controller du panel des profils.
   //	 * @return ContratController classe gérant le panel 
   //	 * des ProtocoleExt.
   //	 */
   //	public ProfilController getProfilController() {
   //		
   //		Tabbox mainTabbox = getMainTabbox();
   //		Tabpanels panels = mainTabbox.getTabpanels();
   //		
   //		return (ProfilController) panels
   //				.getFellow("administrationPanel")
   //				.getFellow("winAdministration")
   //				.getFellow("adminTabbox")
   //				.getFellow("profilsPanel")
   //				.getFellow("profilsMacro")
   //				.getFellow("winProfil")
   //				.getAttributeOrFellow("winProfil$composer", true);
   //		
   //	}
   //	
   //	/**
   //	 * Méthode récupérant le controller du panel des utilisateurs.
   //	 * @return ContratController classe gérant le panel 
   //	 * des ProtocoleExt.
   //	 */
   //	public UtilisateurController getUtilisateurController() {
   //		
   //		Tabbox mainTabbox = getMainTabbox();
   //		Tabpanels panels = mainTabbox.getTabpanels();
   //		
   //		return (UtilisateurController) panels
   //				.getFellow("administrationPanel")
   //				.getFellow("winAdministration")
   //				.getFellow("adminTabbox")
   //				.getFellow("utilisateursPanel")
   //				.getFellow("utilisateursMacro")
   //				.getFellow("winUtilisateur")
   //				.getAttributeOrFellow("winUtilisateur$composer", true);
   //		
   //	}

   public MainWindow getMainWindow(){
      return (MainWindow) this.getPage().getFellow("mainWin").getAttributeOrFellow("mainWin$composer", true);
   }
}
