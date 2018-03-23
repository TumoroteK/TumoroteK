package fr.aphp.tumorotek.decorator;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import fr.aphp.tumorotek.model.contexte.Service;

/**
 * ServiceWithEtablissementRenderer affiche dans le listitem
 * le nom du service avec le nom de son établissement.
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 04/11/2011
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public class ServiceWithEtablissementRenderer implements ListitemRenderer<Object>
{

   @Override
   public void render(final Listitem item, final Object data, final int index) throws Exception{

      final Service serv = (Service) data;

      final StringBuffer sb = new StringBuffer();
      if(serv != null && serv.getNom() != null){
         sb.append(serv.getNom());
         if(serv.getEtablissement() != null && serv.getEtablissement().getNom() != null){
            sb.append(" (");
            sb.append(serv.getEtablissement().getNom());
            sb.append(")");
         }
      }

      new Listcell(sb.toString()).setParent(item);

   }

}
