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

import java.util.List;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.DesktopCleanup;

import fr.aphp.tumorotek.webapp.general.export.Export;

/**
 * Clean up classe appelée lors de la destruction du Desktop,
 * c'est à dire quand
 * fermeture du navigateur ou de l'onglet.
 * Assures le nettoyage des fichiers _tmp qui
 * auraient persisté dans l'arborescence.
 * Date: 16/03/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class TKDesktopCleanup implements DesktopCleanup
{

   // 	private Log log = LogFactory.getLog(TKDesktopCleanup.class); 

   @Override
   public void cleanup(final Desktop desk) throws Exception{
      //		deleteTmpFiles();
      //		if (d.getRequestPath().contains("main.zul")) {
      //			 new SecurityContextLogoutHandler()
      //			 	.logout((HttpServletRequest) 
      //			 			Executions.getCurrent().getNativeRequest(), 
      //					 (HttpServletResponse) 
      //					 	Executions.getCurrent().getNativeResponse(), 
      //					 ((SecurityContext)
      //								SecurityContextHolder.getContext())
      //								.getAuthentication());
      //		}
      destroyExportAliveThreads(desk);
   }

   //	/**
   //	 * Nettoie l'arborescence en supprimant les _tmp qui auraient persisté.
   //	 */
   //	private void deleteTmpFiles() {
   //		
   //		File dir = new File(SessionUtils.getSystemBaseDir());
   //		
   //		if (dir != null) {	
   //			FilenameFilter filter = new FilenameFilter() { 
   //				public boolean accept(File dir, String name) { 
   //					return name.endsWith("_tmp"); 
   //				} 
   //			};  
   //			
   //			List<File> tmps = new ArrayList<File>();
   //			traverseDirectoriesToFindTemps(dir, tmps, filter);
   //			
   //			Iterator<File> tmpsIt = tmps.iterator();
   //			File next;
   //			while (tmpsIt.hasNext()) {
   //				next = tmpsIt.next();
   //				next.delete();
   //				log.info("Desktop cleanup: " + next.getPath() + " deleted");
   //			}
   //		}
   ////		} else {
   ////			log.warn("JNDI problem: TK base directory environment "
   ////					+ "is not set in context.xml!");
   ////		}
   //	}

   //	/**
   //	 * Traverse les dossiers recursivement pour récuperer tous les fichiers
   //	 * _tmp.
   //	 * @param dir
   //	 * @param tmps
   //	 * @param filter
   //	 */
   //	private void traverseDirectoriesToFindTemps(File dir, 
   //											List<File> tmps, 
   //											FilenameFilter filter) {
   //		if (dir.isDirectory()) {
   //			// ajoute _tmp
   //			File[] tmpsArray = dir.listFiles(filter);
   //			for (int j = 0; j < tmpsArray.length; j++) {
   //				tmps.add(tmpsArray[j]);
   //			}
   //			
   //			String[] children = dir.list(); 
   //			for (int i = 0; i < children.length; i++) { 
   //				traverseDirectoriesToFindTemps(new File(dir, children[i]), 
   //															tmps, filter); 
   //			} 
   //		} 
   //	}
   //	
   
   private void destroyExportAliveThreads(final Desktop desk){
      if(desk.hasAttribute("threads")){
         final List<Export> thrs = (List<Export>) desk.getAttribute("threads");
         for(final Export export : thrs){
            if(export.isAlive() && !export.isInterrupted()){
               export.interrupt();
            }
         }
      }
   }
}
