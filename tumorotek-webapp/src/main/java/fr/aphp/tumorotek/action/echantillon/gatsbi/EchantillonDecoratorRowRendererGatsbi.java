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
package fr.aphp.tumorotek.action.echantillon.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.echantillon.AbstractEchantillonDecoratorRowRenderer;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * EchantillonDecoratorRenderer surcharge gatsbi
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.3.0-gatsbi
 */
public class EchantillonDecoratorRowRendererGatsbi extends AbstractEchantillonDecoratorRowRenderer
{

   private boolean iconesRendered = false;

   private final Contexte contexte;

   public EchantillonDecoratorRowRendererGatsbi(){
      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(3);
   }

   @Override
   public void render(final Row row, final EchantillonDTO deco, final int index){
      super.render(row, deco, index);
   }

   @Override
   protected void renderEchantillon(final Row row, final EchantillonDTO deco)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         if(!chpId.equals(55) && !chpId.equals(57)){ // statut et emplacement toujours rendus
            GatsbiControllerEchantillon.applyEchantillonDecoratorChpRender(chpId, row, deco, false, false);
         }
      }
   }

   @Override
   public void setIconesRendered(final boolean _i){
      this.iconesRendered = _i;
   }

   @Override
   public boolean areIconesRendered(){
      return iconesRendered;
   }
}