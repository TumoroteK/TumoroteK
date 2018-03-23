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
package fr.aphp.tumorotek.decorator;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

/**
 * I3listBoxItemRenderer affiche dans le listitem
 * le membre de l'objet spécifié en utilisant le fichier
 * d'internationalisation.
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 22/03/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class I3listBoxItemRenderer implements ListitemRenderer<Object>
{

   private final Log log = LogFactory.getLog(I3listBoxItemRenderer.class);

   private final String fieldName;

   /**
    * Constructeur.
    */
   public I3listBoxItemRenderer(final String fname){
      this.fieldName = fname;
   }

   @Override
   public void render(final Listitem li, final Object data, final int index){

      if(data != null){
         try{
            final Field f = data.getClass().getDeclaredField(this.fieldName);
            // private -> acessible for refection only
            f.setAccessible(true);

            String label = Labels.getLabel(data.getClass().getSimpleName() + "." + (String) f.get(data));

            if(label == null){
               label = (String) f.get(data);
            }

            // si numero de version
            if(data.getClass().equals("TableCodage")){
               final Field fVersion = data.getClass().getDeclaredField("version");
               if(fVersion != null){
                  fVersion.setAccessible(true);
                  if(fVersion.get(data) != null){
                     label = label + " (" + (String) fVersion.get(data) + ")";
                  }
               }
            }
            final Listcell cell = new Listcell(label);
            cell.setParent(li);

            li.setValue(data);

         }catch(final SecurityException e1){
            log.error("rendering field reflection error", e1);
         }catch(final NoSuchFieldException e2){
            log.error("rendering field reflection error", e2);
         }catch(final IllegalArgumentException e3){
            log.error("rendering field reflection error", e3);
         }catch(final IllegalAccessException e4){
            log.error("rendering field reflection error", e4);
         }
      }
   }
}
