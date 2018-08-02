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
package fr.aphp.tumorotek.action.contexte;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Renderer affiche dans le Row
 * les membres de Banque sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 17/10/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 */
public class BanqueRowRenderer extends TKSelectObjectRenderer<Banque>
{

   //private Log log = LogFactory.getLog(BanqueRowRenderer.class);

   public BanqueRowRenderer(){}

   @Override
   public void render(final Row row, final Banque data, final int index){

      final Banque bank = data;

      final Label nomLabel = new Label(bank.getNom());
      nomLabel.addForward(null, nomLabel.getParent(), "onClickObject", bank);
      if(bank.getArchive()){
         nomLabel.setClass("formLinkArchive");
      }else{
         nomLabel.setClass("formLink");
      }
      nomLabel.setParent(row);
      if(bank.getProprietaire() != null){
         new Label(bank.getProprietaire().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }
      if(bank.getCollaborateur() != null){
         new Label(bank.getCollaborateur().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
      }
      //		drawObjectListLabel(ManagerLocator.getConteneurManager()
      //							.findByBanqueWithOrderManager(bank), "Nom", row);
   }

   //	/**
   //	 * Dessine dans un label le membre du premier objet et 
   //	 * l'utilisation d'un tooltip pour afficher la totalité de la liste 
   //	 * des autres.
   //	 * @param liste des objets
   //	 * @param champ a afficher (String)
   //	 * @param row Parent
   //	 */
   //	private void drawObjectListLabel(List< ? extends Object> objs, 
   //													String champ,  Row row) {
   //		
   //		Label label1 = new Label();
   //		
   //		try {
   //			if (!objs.isEmpty()) {
   //				Object tmp = PropertyUtils
   //										.getSimpleProperty(objs.get(0), champ);
   //				if (tmp != null) {
   //					label1.setValue((String) tmp);
   //				} 
   //				// dessine le label avec un lien vers popup 
   //				if (objs.size() > 1) {
   //					Hbox labelAndLinkBox = new Hbox();
   //					labelAndLinkBox.setSpacing("5px");
   //					Label moreLabel = new Label("...");
   //					moreLabel.setClass("formLink");
   //					Popup malPopUp = new Popup();
   //					malPopUp.setParent(row.getParent().getParent().getParent());
   //					Iterator< ? extends Object> it = objs.iterator();
   //					Object next;
   //					Object tmp2 = null;
   //					Label labelNext = null;
   //					Vbox popupVbox = new Vbox();
   //					while (it.hasNext()) {
   //						next = (Object) it.next(); 
   //						tmp2 = PropertyUtils
   //									.getSimpleProperty(next, champ);
   //						if (tmp2 != null) {
   //							labelNext = new Label((String) tmp2);
   //							labelNext.setSclass("formValue");
   //							popupVbox.appendChild(labelNext);
   //						}						
   //						
   //					}
   //					malPopUp.appendChild(popupVbox);
   //					moreLabel.setTooltip(malPopUp);
   //					labelAndLinkBox.appendChild(label1);
   //					labelAndLinkBox.appendChild(moreLabel);
   //					labelAndLinkBox.setParent(row);
   //				} else {
   //					label1.setParent(row);
   //				}
   //			}
   //		} catch (IllegalAccessException e) {
   //			log.error(e);
   //		} catch (InvocationTargetException e) {
   //			log.error(e);
   //		} catch (NoSuchMethodException e) {
   //			log.error(e);
   //		}
   //	}
}
