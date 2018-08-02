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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * PatientRenderer affiche dans le Row
 * les membres de Patient sous forme de labels.
 *
 * @see http://en.wikibooks.org/wiki/ZK/Examples
 * Date: 14/04/2010
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class PatientRowRenderer extends TKSelectObjectRenderer<Patient>
{

   private List<Banque> banques = new ArrayList<>();
   private Patient patient;

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

      // dessine les champs spécifiques Patient
      final Patient pat = data;
      patient = pat;

      if(anonyme){
         final Label nomLabel = createAnonymeLink();
         nomLabel.addForward(null, nomLabel.getParent(), "onClickObject", pat);
         nomLabel.setParent(row);
         createAnonymeBlock().setParent(row);
         createAnonymeBlock().setParent(row);
      }else{
         final Label nomLabel = new Label(pat.getNom());
         nomLabel.addForward(null, nomLabel.getParent(), "onClickObject", pat);
         nomLabel.setClass("formLink");
         nomLabel.setParent(row);
         new Label(pat.getPrenom()).setParent(row);
         new Label(pat.getNip()).setParent(row);
      }

      new Label(PatientUtils.setSexeFromDBValue(pat)).setParent(row);

      if(anonyme){
         createAnonymeBlock().setParent(row);
      }else{
         new Label(ObjectTypesFormatters.dateRenderer2(pat.getDateNaissance())).setParent(row);
         //	    	String dateN = null;
         //	    	if (this.patient.getDateNaissance() != null) {
         //				Calendar c = Calendar.getInstance();
         //				c.setTime(this.patient.getDateNaissance());
         //				dateN = String.valueOf(c.get(Calendar.YEAR));
         //			}
         //			new Label(dateN).setParent(row);
      }
      // Etat
      new Label(PatientUtils.setEtatFromDBValue(pat)).setParent(row);
      new Label(PatientUtils.getDateDecesOrEtat(pat)).setParent(row);

      // Consentements	    

      // affiche la maladie si maladie non defaut et liens vers popUps
      // si autre maladies
      drawMaladieLabel(pat, row);

      // affiche le compte de prélèvements consultables
      new Label(getNbPrelevements()).setParent(row);

      // codes organes : liste des codes exportés pour échantillons
      ObjectTypesFormatters.drawCodesExpLabel(ManagerLocator.getCodeAssigneManager().findFirstCodesOrgByPatientManager(pat), row,
         null, true);

   }

   /**
    * Dessine dans un label le ou les libelles de maladies 
    * l'utilisation d'un tooltip pour afficher la totalité.
    * @param
    * @param row Parent
    */
   private void drawMaladieLabel(final Patient pat, final Row row){

      final List<Maladie> mals = ManagerLocator.getMaladieManager().findByPatientNoSystemManager(pat);

      // on va afficher les maladies de la plus récente
      // à la plus ancienne
      final List<Maladie> maladies = new ArrayList<>();
      for(int i = 0; i < mals.size(); i++){
         maladies.add(0, mals.get(i));
      }

      if(!maladies.isEmpty()){
         final Label mal1Label = new Label(maladies.get(0).getLibelle());
         // dessine le label avec un lien vers popup 
         if(maladies.size() > 1){
            final Hbox labelAndLinkBox = new Hbox();
            labelAndLinkBox.setSpacing("5px");
            final Label moreLabel = new Label("...");
            moreLabel.setClass("formLink");
            final Popup malPopUp = new Popup();
            malPopUp.setParent(row.getParent().getParent().getParent());
            final Iterator<Maladie> it = maladies.iterator();
            Maladie next;
            String label = null;
            Label libelleStaticLabel = null;
            final Vbox popupVbox = new Vbox();
            while(it.hasNext()){
               next = it.next();
               if(next.getCode() != null){
                  label = next.getLibelle() + " [" + next.getCode() + "]";
               }else{
                  label = next.getLibelle();
               }
               libelleStaticLabel = new Label(label);
               libelleStaticLabel.setSclass("formValue");

               popupVbox.appendChild(libelleStaticLabel);
            }
            malPopUp.appendChild(popupVbox);
            moreLabel.setTooltip(malPopUp);
            labelAndLinkBox.appendChild(mal1Label);
            labelAndLinkBox.appendChild(moreLabel);
            labelAndLinkBox.setParent(row);
         }else{
            mal1Label.setParent(row);
         }
      }else{
         new Label().setParent(row);
      }
   }

   public String getNbPrelevements(){
      return String.valueOf(PatientUtils.getNbPrelsForPatientAndUser(patient, banques));
   }
}
