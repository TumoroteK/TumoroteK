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
package fr.aphp.tumorotek.action.patient;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.decorator.TKSelectObjectRenderer;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.gatsbi.PatientIdentifiant;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * PatientRenderer affiche dans le Row
 * les membres de Patient sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 14/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class PatientRowRenderer extends TKSelectObjectRenderer<Patient>
{

   protected List<Banque> banques = new ArrayList<>();

   public PatientRowRenderer(final boolean select){
      setSelectionMode(select);
   }

   public void setBanques(final List<Banque> bs){
      this.banques = bs;
   }

   @Override
   public void render(final Row row, final Patient data, final int index){
      // dessine le checkbox
      super.render(row, data, index);
      
      renderObjets(row, data);
   }
   
   public void renderObjets(final Row row, final Object data){

      final Patient pat = (Patient) data;
      
      // @since gatsbi
      try{
         renderPatient(row, pat);
      }catch(final Exception e){
         // une erreur inattendue levée dans la récupération
         // ou le rendu d'une propriété patient
         // va arrêter le rendu du reste du tableau
         throw new RuntimeException(e);
      }
      
      // affiche la maladie si maladie non defaut et liens vers popUps
      // si autre maladies
      renderMaladies(pat, row, banques);
      
      // affiche le compte de prélèvements consultables
      renderNbPrels(row, pat);
      
      // codes organes : liste des codes exportés pour échantillons
      renderFirstCodeOrganeForPatient(row, pat);
   }
   
   protected void renderNbPrels(Row row, Patient pat){
      new Label(getNbPrelevements(pat)).setParent(row);      
   }

   /**
    * Rendu des colonnes spécifiques patient, sera surchargé par Gatsbi.
    * @param row
    * @param patient
    * @throws NoSuchMethodException
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    */
   protected void renderPatient(final Row row, final Patient pat)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{
      
      // nom
      renderAnonymisableAndClickableAlphanumProperty(row, pat, "nom", anonyme, "onClickObject", pat);
      
      // prenom 
      renderAnonymisableAndClickableAlphanumProperty(row, pat, "prenom", anonyme, null, null);

      // nip
      //TG-192 : cas particulier "Toutes collections" et patient "empty" :
      if(Sessions.getCurrent().hasAttribute("ToutesCollections") && pat.isEmptyPatient()) {
         //le patient étant empty il n'est forcément rattaché qu'à une seule banque donc n'a qu'un seul identifiant
         PatientIdentifiant patientIdentifiant = pat.getPatientIdentifiants().iterator().next();
         //on affiche le nom de celle-ci entre [], après l'identifiant 
         String identifiantAAfficher = patientIdentifiant.getIdentifiant().concat(" [").concat(patientIdentifiant.getBanque().getNom()).concat("]");
         renderClickableValue(row, identifiantAAfficher, pat);
      }
      else {
         // tous les autres cas (cas standard) :
         renderAnonymisableAndClickableAlphanumProperty(row, pat, "nip", anonyme, "onClickObject", pat);
      }

      // sexe
      new Label(PatientUtils.setSexeFromDBValue(pat)).setParent(row);

      // date de naissance
      renderDateNaissance(row, pat, anonyme);
      
      // Etat
      renderPatientEtat(row, pat);
      
      // date état
      renderPatientDateEtat(row, pat);
   }

   public static void renderDateNaissance(Row row, Patient pat, boolean anonyme) 
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
      if(anonyme){
         createAnonymeLabelIsClickable(false).setParent(row);
      }else{
         renderDateProperty(row, pat, "dateNaissance");
      }      
   }

   public static void renderPatientEtat(final Row row, final Patient pat){
      new Label(PatientUtils.setEtatFromDBValue(pat)).setParent(row);      
   }
   
   public static void renderPatientDateEtat(Row row, Patient pat){
      new Label(PatientUtils.getDateDecesOrEtat(pat)).setParent(row);      
   }
   
   public static void renderFirstCodeOrganeForPatient(Row row, Patient pat){
      ObjectTypesFormatters.drawCodesExpLabel(ManagerLocator.getCodeAssigneManager().findFirstCodesOrgByPatientManager(pat), row,
         null, true);      
   }


   /**
    * Dessine dans un label le ou les libelles de maladies
    * l'utilisation d'un tooltip pour afficher la totalité.
    * @param
    * @param row Parent
    */
   public static void renderMaladies(final Patient pat, final Row row, final List<Banque> banks){

      final List<Maladie> mals = ManagerLocator.getMaladieManager().findByPatientNoSystemNorVisiteManager(pat);
      
      if (Sessions.getCurrent().getAttribute("ToutesCollections") != null) {
         for (Banque bank : banks) {
            mals.addAll(new ArrayList<>(ManagerLocator.getMaladieManager().findVisitesManager(pat, bank)));
         }
      }

      // on va afficher les maladies de la plus récente
      // à la plus ancienne
      final List<String> maladies = new ArrayList<String>();
      for(int i = 0; i < mals.size(); i++){
         maladies.add(0, i == 0 ?
           mals.get(i).getLibelle() : 
           mals.get(i).getLibelle().concat( 
              (mals.get(i).getCode() != null ? " [" + mals.get(i).getCode() + "]" : "")
           ));
      }
      
      drawListStringLabel(row, maladies);
   }
   
   /**
    * Dessines une liste sous la forme <first String>...
    * @param row
    * @param str
    */
   public static Label drawListStringLabel(final Row row, List<String> str) {
      
      Label label = null;
      
      if(!str.isEmpty()){
         label = new Label(str.get(0));
         // dessine le label avec un lien vers popup
         if(str.size() > 1){
            final Hbox labelAndLinkBox = new Hbox();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup popUp = new Popup();
            popUp.setParent(row.getParent().getParent().getParent());
            final Iterator<String> it = str.iterator();
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               libelleStaticLabel = new Label(it.next());
               libelleStaticLabel.setSclass("formValue");
               popupVbox.appendChild(libelleStaticLabel);
            }
            popUp.appendChild(popupVbox);
            moreLabel.setTooltip(popUp);
            labelAndLinkBox.appendChild(label);
            labelAndLinkBox.appendChild(moreLabel);
            labelAndLinkBox.setParent(row);
         }else{
            label.setParent(row);
         }
      }else{
         label = new Label();
         label.setParent(row);
      }
      return label;
   }

   public String getNbPrelevements(Patient patient){
      return String.valueOf(PatientUtils.getNbPrelsForPatientAndUser(patient, banques));
   }
}
