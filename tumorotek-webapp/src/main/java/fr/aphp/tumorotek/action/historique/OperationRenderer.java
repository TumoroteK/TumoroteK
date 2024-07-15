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
package fr.aphp.tumorotek.action.historique;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.qualite.Operation;

/**
 * OperationRenderer affiche dans le Row
 * les membres d'Operation sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 23/11/2010
 *
 * @author Pierre Ventadour.
 * @version 2.0
 */
public class OperationRenderer implements RowRenderer<Operation>
{

   @Override
   public void render(final Row row, final Operation op, final int index) throws Exception{

      // Utilisateur
      // peut être null si synchronisation identités
      new Label(op.getUtilisateur() != null ? op.getUtilisateur().getLogin() : null).setParent(row);

      // Type d'opération
      new Label(getOperationType(op)).setParent(row);

      // date de stockage
      new Label(ObjectTypesFormatters.dateRenderer2(op.getDate())).setParent(row);

      // applique style si v1
      if(op.getV1()){
         final Label lab = new Label("v1");
         lab.setStyle("color : #ff0000");
         row.setStyle("font-style : italic");
         lab.setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   public String getOperationType(final Operation op){
      final StringBuffer type = new StringBuffer();
      
      final String operationNom = op.getOperationType().getNom();
      final String label = Labels.getLabel("OperationType." + operationNom);

      if(label != null){
         type.append(label);
      }else{
         type.append(operationNom);
      }

      if(operationNom.equals("Creation")){
         if(ManagerLocator.getImportHistoriqueManager()
            .findImportationsByEntiteAndObjectIdManager(op.getEntite(), op.getObjetId()).size() > 0){
            type.append(" (Import)");
         }
      }

      return type.toString();
   }

}
