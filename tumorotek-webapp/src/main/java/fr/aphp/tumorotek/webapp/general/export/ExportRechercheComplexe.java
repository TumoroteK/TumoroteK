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
package fr.aphp.tumorotek.webapp.general.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;

import fr.aphp.tumorotek.action.io.RechercheUtils;
import fr.aphp.tumorotek.component.ProgressBarComponent;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.ExportUtils;

/**
 * Export des données issues d'une recherche complexe.
 * A partir de la liste d'objets, lance la récuperation des
 * champs d'affichage demandés et formate l'excel, dans un
 * thread séparé.
 * Applique la progressbar mis à jour avec le nombre de résultats
 * exportés.
 * Date: 18/02/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class ExportRechercheComplexe extends Export
{

   public ExportRechercheComplexe(){}

   private Affichage affichage;
   private String nomRecherche;

   private List<? extends Object> objs;

   public void init(final Desktop d, final List<? extends Object> o, final HtmlMacroComponent htmlMacroComponent,
      final Affichage a, final String n, final Utilisateur u){
      desktop = d;
      desktop.enableServerPush(true);

      user = u;

      this.progressBarComponent = htmlMacroComponent;
      this.progressBar = ((ProgressBarComponent) progressBarComponent.getFellow("progressPanel")
         .getAttributeOrFellow("progressPanel$composer", true));
      progressBar.setExportThread(this);

      objs = o;

      total = o.size();

      affichage = a;
      nomRecherche = n;
   }

   @Override
   public void run(){
      try{
         init();
      }catch(final InterruptedException ie){
         log.warn(ie.getMessage());
      }catch(final DesktopUnavailableException e){
         e.printStackTrace();
      }catch(final IOException e){
         log.error(e);
      }finally{
         // updateUIBarThread.interrupt();
         // close progressBar
         detachProgressBarComponent();
         desktop.enableServerPush(false);
         Executions.deactivate(desktop);
      }
   }

   @Override
   public void init() throws DesktopUnavailableException, InterruptedException, IOException{

      // loadProgressUIThread(Labels
      //		.getLabel("progressbar.recherche.export.type"));

      final List<List<Object>> matriceObjets = new ArrayList<>();

      setExportDetails(null, 1, 3, "progressbar.data.collect", null, null);
      for(int i = 0; i < objs.size(); i++){
         if(!Thread.interrupted()){
            matriceObjets.add(RechercheUtilsManager.getListeObjetsCorrespondants(objs.get(i), affichage, null));
            if(((int) Math.ceil((i * 100) / objs.size())) % 10 == 0 && !Thread.interrupted()){
               setExportDetails((int) Math.ceil((i * 100) / objs.size()), null, null, "progressbar.data.collect", null, null);
            }
         }else{
            throw new InterruptedException();
         }
      }

      final List<List<Object>> matriceAffichable = new ArrayList<>();

      setExportDetails(null, 2, 3, "progressbar.recherche.export.affichage", null, null);

      if(Thread.interrupted()){
         throw new InterruptedException();
      }
      //updateProgressBar((int) Math.ceil((i * 100) / objs.size()));
      /** On charge la matrice affichable. */
      RechercheUtils.loadMatriceAffichable(matriceObjets, matriceAffichable, affichage);

      // On supprime les doublons de la liste
      //			for (int i = 0; i < matriceAffichable.size(); i++) {
      //				List<Object> lo = matriceAffichable.get(i);
      //				for (int j = i + 1; j < matriceAffichable.size(); j++) {
      //					List<Object> loTemp = matriceAffichable.get(j);
      //					if (lo.size() == loTemp.size()) {
      //						boolean egale = true;
      //						for (int k = 0; k < lo.size(); k++) {
      //							if ((lo.get(k) == null && loTemp.get(k) != null)
      //									|| (lo.get(k) != null && !lo.get(k).equals(
      //											loTemp.get(k)))) {
      //								egale = false;
      //								break;
      //							}
      //						}
      //						if (egale) {
      //							matriceAffichable.remove(j);
      //							matriceObjets.remove(j);
      //							j--;
      //						}
      //					}
      //				}
      //			}
      //
      if(Thread.interrupted()){
         throw new InterruptedException();
      }

      setExportDetails(null, 3, 3, "progressbar.recherche.export.excel", null, null);
      RechercheUtils.exportTableExcel(getWb(), matriceAffichable, affichage);

      final String filename = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + nomRecherche + ".xlsx";

      // updateProgressBar(100);
      // Executions.activate(desktop);
      ExportUtils.downloadExportFileXls(getWb(), filename, getOutStr(), desktop);
      // Executions.deactivate(desktop);
   }

}
