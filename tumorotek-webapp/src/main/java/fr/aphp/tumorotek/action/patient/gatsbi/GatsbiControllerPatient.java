/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.patient.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.zul.Column;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.PatientRowRenderer;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.GatsbiController;

/**
 * Gatsbi controller regroupant les fonctionalités de modification dynamique de
 * l'interface spécifique aux patients
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 *
 */
public class GatsbiControllerPatient
{

   public static void addColumnForChpId(final Integer chpId, final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      switch(chpId){
         case 2: // nip
            drawNipColumn(grid);
            break;
         case 3: // nom
            drawNomColumn(grid);
            break;
         case 4: // nom naissance
            drawNomNaissanceColumn(grid);
            break;
         case 5: // prenom
            drawPrenomColumn(grid);
            break;
         case 6: // sexe
            drawSexeColumn(grid);
            break;
         case 7: // date naissance
            drawDateNaissanceColumn(grid);
            break;
         case 8: // ville naissance
            drawVilleNaissanceColumn(grid);
            break;           
         case 9: // pays naissance
            drawPaysNaissanceColumn(grid);
            break;
         case 10: // patient état
            drawPatientEtatColumn(grid);
            break;
         case 11: // date état
            drawDateEtatColumn(grid);
            break;
         case 12: // date décès
            drawDateDecesColumn(grid);
            break;
         case 227: // medecins
            drawMedecinsColumn(grid);
            break;       
         default:
            break;
      }
   }

   public static Column drawNipColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.Nip", null, null, null, null, true);   
   }

   public static Column drawNomColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.Nom", null, null, null, "auto(nom)", true);
   }

   public static Column drawNomNaissanceColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.NomNaissance", null, null, null, null, true);
   }

   public static Column drawPrenomColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.Prenom", null, null, null, null, true);
   }

   public static Column drawSexeColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.Sexe", null, null, null, "auto(sexe)", true);
   }

   public static Column drawDateNaissanceColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.DateNaissance", null, null, null, "auto(dateNaissance)", true);
   }

   public static Column drawVilleNaissanceColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.VilleNaissance", null, null, null, null, true);
   }

   public static Column drawPaysNaissanceColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.PaysNaissance", null, null, null, null, true);
   }

   public static Column drawPatientEtatColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return  GatsbiController.addColumn(grid, "Champ.Patient.PatientEtat", null, null, null, null, true);
   }

   public static Column drawDateEtatColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.DateEtat", null, null, null, "auto(dateEtat)", true);
   }

   public static Column drawDateDecesColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "Champ.Patient.DateDeces", null, null, null, "auto(dateDeces)", true);
   }

   public static Column drawMedecinsColumn(final Grid grid)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException{
      return GatsbiController.addColumn(grid, "patient.medecins", null, null, null, null, true);
   }

   /**
    * Applique la methode de rendering correspondant au champEntité id passé en
    * paramètre
    *
    * @param chpId
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @throws ParseException
    */
   public static void applyPatientChpRender(final Integer chpId, final Row row, 
                                          final Patient pat, final boolean anonyme)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      switch(chpId){
         case 2: // nip
            TKSelectObjectRenderer
               .renderAnonymisableAndClickableAlphanumProperty(row, pat, "nip", anonyme, null, null);
            break;
         case 3: // nom
            TKSelectObjectRenderer
               .renderAnonymisableAndClickableAlphanumProperty(row, pat, "nom", anonyme, null, null);
            break;
         case 4: // nom naissance
            TKSelectObjectRenderer
               .renderAnonymisableAndClickableAlphanumProperty(row, pat, "nomNaissance", anonyme, null, null);
            break;
         case 5: // prenom
            TKSelectObjectRenderer
               .renderAnonymisableAndClickableAlphanumProperty(row, pat, "prenom", anonyme, null, null);
            break;
         case 6: // sexe
            renderSexe(row, pat);
            break;
         case 7: // date naissance
            PatientRowRenderer.renderDateNaissance(row, pat, anonyme);
            break;
         case 8: // ville naissance
            TKSelectObjectRenderer
               .renderAlphanumPropertyAsStringNoFormat(row, pat, "villeNaissance");
            break;
         case 9: // pays naissance
            TKSelectObjectRenderer
               .renderAlphanumPropertyAsStringNoFormat(row, pat, "paysNaissance");
            break;
         case 10: // état
            PatientRowRenderer.renderPatientEtat(row, pat);
            break;
         case 11: // date état
            TKSelectObjectRenderer.renderDateProperty(row, pat, "dateEtat");
            break;
         case 12: // date décès
            TKSelectObjectRenderer.renderDateProperty(row, pat, "dateDeces");
            break;
         case 227: // médecins
            renderMedecinsProperty(row, pat);
            break;

         default:
            break;
      }
   }

   public static void renderSexe(Row row, Patient pat){
      if(pat.getSexe() != null){
         new Label(PatientUtils.setSexeFromDBValue(pat)).setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   public static void renderMedecinsProperty(Row row, Patient pat){
      PatientRowRenderer.drawListStringLabel(row, 
         ManagerLocator.getPatientManager().getMedecinsManager(pat)
            .stream().map(c -> c.getNomAndPrenom())
            .collect(Collectors.toList()));
   } 
   
   public static void addListHeadForChpId(final Integer chpId, final Listbox listbox, Map<Integer,String> widths) {
      switch(chpId){
         case 2: // nip
            drawNipListheader(listbox, widths != null ? widths.get(2) : null);
            break;
         case 3: // nom
            drawNomListheader(listbox, widths != null ? widths.get(3) : null);
            break;
         case 4: // nom naissance
            break;
         case 5: // prenom
            drawPrenomListheader(listbox, widths != null ? widths.get(5) : null);
            break;
         case 6: // sexe
            drawSexeListheader(listbox, widths != null ? widths.get(6) : null);
            break;
         case 7: // date naissance
            drawDateNaissanceListheader(listbox, widths != null ? widths.get(7) : null);
            break;
         case 8: // ville naissance
            break;           
         case 9: // pays naissance
            break;
         case 10: // patient état
            break;
         case 11: // date état
            break;
         case 12: // date décès
            break;
         case 227: // medecins
            drawPatientMedecinsListheader(listbox, widths != null ? widths.get(227) : null);
            break;       
         default:
            break;
      }
   }
   
   public static Listheader drawIdentifiantListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.Identifiant", width, null, true);   
   }

   public static Listheader drawNipListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.Nip", width, null, true);   
   }

   public static Listheader drawNomListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.Nom", width, null, true);
   }
   
   public static Listheader drawPrenomListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.Prenom", width, null, true);
   }
   
   public static Listheader drawSexeListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.Sexe", width, null, true);
   }
   
   public static Listheader drawDateNaissanceListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "Champ.Patient.DateNaissance", width, null, true);
   }
   
   public static Listheader drawPatientMedecinsListheader(final Listbox listbox, final String width) {
      return GatsbiController.addListHeader(listbox, "patient.medecins", width, null, true);
   }
   
   /**
    * Dessines les listbox headers d'une listbox passée en paramètre, suivant un contexte Gatsbi.
    * @param contexte
    * @param listbox
    */
   public static void drawListheadersForPatients(final Contexte contexte, final Listbox listbox, 
      final boolean isFusion, Map<Integer,String> widths) {

      // identifiant header, toujours affichée
      drawIdentifiantListheader(listbox, widths != null ? widths.get(272) : null);

      // variable columns
      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         addListHeadForChpId(chpId, listbox, widths);
      }
      
      // fusion -> prelevements
      GatsbiController.addListHeader(listbox, "patients.prelevements.short", null, null, isFusion);
   }
}