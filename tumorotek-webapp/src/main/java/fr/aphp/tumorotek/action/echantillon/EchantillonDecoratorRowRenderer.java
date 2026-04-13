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
package fr.aphp.tumorotek.action.echantillon;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.helper.ContexteHelper;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * EchantillonDecoratorRenderer affiche dans la grid temporaire
 * les membres d'EchantillonDecorator2 lors de l'ajout
 * multiple dans sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 18/11/2010
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 */
public class EchantillonDecoratorRowRenderer extends AbstractEchantillonDecoratorRowRenderer
{

   /**
    * Rendu des colonnes spécifiques échantillon, sera surchargé par Gatsbi.
    *
    * @param row
    * @param deco
    */
   @Override
   protected void renderEchantillon(final Row row, final EchantillonDTO deco)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{
      
      //l'affichage des colonnes organes et codes lésionnels est conditionné au contexte - récupération de la condition dans EchantillonRowRenderer
      //pour être sûr d'avoir la même règle de gestion dans tous les tableaux affichant des données échantillons !
      if(EchantillonRowRenderer.displayEchansOrganeEtCodeLesionnel()) {
         // codes organes : liste des codes exportés pour échantillons
         ObjectTypesFormatters.drawCodesExpLabel(deco.getCodesOrgsToCreateOrEdit(), row, null, false, 2);
   
         // codes lésionnels : liste des codes exportés pour échantillons
         ObjectTypesFormatters.drawCodesExpLabel(deco.getCodesLesToCreateOrEdit(), row, null, false, 3);
      }
      
      // type
      if(deco.getType() != null){
         new Label(deco.getType()).setParent(row);
      }else{
         new Label().setParent(row);
      }

      // quantité
      if(deco.getOnlyQuantiteInit() != null){
         new Label(deco.getOnlyQuantiteInit()).setParent(row);
      }else{
         new Label().setParent(row);
      }

   }
   
}
