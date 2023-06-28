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
package fr.aphp.tumorotek.action.imports;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * @version 2.3.0-gatsbi
 * @author Mathieu BARTHELEMY
 *
 */
public class ImportColonneRowRenderer implements RowRenderer<ImportColonne>
{

   private boolean visiteGatsbi = false;
   
   public ImportColonneRowRenderer(boolean _g) {
      visiteGatsbi = _g;
   }
   
   @Override
   public void render(final Row row, final ImportColonne data, final int index) throws Exception{
      final ImportColonne colonne = data;

      // nom colonne
      final Label colonneLabel = new Label(colonne.getNom());
      colonneLabel.setParent(row);
      String champ = ImportUtils.exctractChamp(colonne, visiteGatsbi);
      // Champ associé
      final Label champLabel = new Label(champ);
      champLabel.setParent(row);

      // format
      String format = "";
      if(colonne.getChamp() != null){
         if(colonne.getChamp().getChampEntite() != null){
            if(colonne.getChamp().getChampEntite().getNom().equals("EmplacementId")){
               format = Labels.getLabel("importColonne.Type.Emplacement");
            }else if(colonne.getChamp().getChampEntite().getQueryChamp() != null){
               format = "thesaurus";
            }else{
               format = colonne.getChamp().getChampEntite().getDataType().getType();
            }
         }else if(colonne.getChamp().getChampDelegue() != null){
            format = colonne.getChamp().getChampDelegue().getDataType().getType();
         }else{
            format = colonne.getChamp().getChampAnnotation().getDataType().getType();
         }
      }else{ // subderive header
         if(colonne.getNom().equals("code.parent")){
            format = "alphanum";
         }else if(colonne.getNom().equals("qte.transf")){
            format = "num";
         }else if(colonne.getNom().equals("evt.date")){
            format = "datetime";
         }
      }
      final Label formatLabel = new Label(format);
      formatLabel.setParent(row);

      // entité
      String entite = "";
      if(colonne.getChamp() != null){
         if(colonne.getChamp().getChampEntite() != null){
            if (!visiteGatsbi || colonne.getChamp().getChampEntite().getEntite().getEntiteId() != 7) {
               entite = Labels.getLabel("Entite." + colonne.getChamp().getChampEntite().getEntite().getNom());
            } else { // rendu entite Maladie -> Visite
               entite = Labels.getLabel("gatsbi.visite");
            }
         }else if(colonne.getChamp().getChampDelegue() != null){
            entite = Labels.getLabel("Entite." + colonne.getChamp().getChampDelegue().getEntite().getNom());
         }else{
            entite =
               Labels.getLabel("Entite." + colonne.getChamp().getChampAnnotation().getTableAnnotation().getEntite().getNom());
         }
      }else{
         entite = Labels.getLabel("import.colonne.subderive.header");
         // if (colonne.getNom().equals("code.parent")) {
         // entite = "alphanum";
         // } else if (colonne.getNom().equals("qte.transf")) {
         // format = "num";
         // } else if (colonne.getNom().equals("evt.date")) {
         // format = "datetime";
         // }
      }
      final Html entiteLabel = new Html(entite);
      entiteLabel.setParent(row);

      // obligatoire
      Boolean ob = false;
      if(colonne.getChamp() != null){
         if(colonne.getChamp().getChampEntite() != null){
            // @since 2.3.0-gatsbi
            // vérifie si contexte Gatsbi s'applique
            final Contexte c =
               SessionUtils.getCurrentGatsbiContexteForEntiteId(colonne.getChamp().getChampEntite().getEntite().getEntiteId());
            if(c == null){ // TK-defaut
               ob = !colonne.getChamp().getChampEntite().isNullable();
            }else{ // gatsbi
               ob = c.isChampIdRequired(colonne.getChamp().getChampEntite().getId());
            }
         }
      }else{ // subderive header
         ob = true;
      }
      final Label obligatoireLabel = new Label(ObjectTypesFormatters.booleanLitteralFormatter(ob));
      if(ob){
         obligatoireLabel.setSclass("requiredMark");
      }
      obligatoireLabel.setParent(row);
   }

   public String getLabelForChampEntite(final ChampEntite champ){
      final StringBuffer iProperty = new StringBuffer();
      iProperty.append("Champ.");
      iProperty.append(champ.getEntite().getNom());
      iProperty.append(".");

      String champOk = "";
      // si le nom du champ finit par "Id", on le retire
      if(champ.getNom().endsWith("Id")){
         champOk = champ.getNom().substring(0, champ.getNom().length() - 2);
      }else{
         champOk = champ.getNom();
      }
      iProperty.append(champOk);

      // on ajoute la valeur du champ
      return Labels.getLabel(iProperty.toString());
   }

}
