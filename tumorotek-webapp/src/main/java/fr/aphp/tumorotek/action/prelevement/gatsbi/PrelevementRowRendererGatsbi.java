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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.prelevement.PrelevementRowRenderer;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.webapp.gatsbi.RowRendererGatsbi;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Controller gérant le rendu dynamique des lignes du tableau prélèvement sous
 * le gestionnaire GATSBI. Ecris donc toutes les colonnes possibles, mais dans
 * l'ordre spécifié par le contexte Gatsbi.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsi
 */
public class PrelevementRowRendererGatsbi extends PrelevementRowRenderer implements RowRendererGatsbi
{

   private Contexte contexte;

   // flag passe à true si la cellule congelation est déja rendue
   // afin d'éviter que cette cellule soit rendue deux fois
   private boolean congCellRendered;

   // par défaut les icones ne sont pas dessinées
   private boolean iconesRendered = false;
   
   // nom + prenom s'affichent dans une seule colonne si pas contexte Gatsbi
   // ou si spécifié par contexte Gatsbi
   private boolean nomPrenomPatientInTableau = false;
   
   // nip s'affiche dans une colonne si spécifié par contexte Gatsbi
   private boolean nipInTableau = false;

   public PrelevementRowRendererGatsbi(final boolean select, final boolean cols){
      super(select, cols);

      contexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(2);
      
      
      Contexte patientContexte = SessionUtils.getCurrentGatsbiContexteForEntiteId(1);
      
      nomPrenomPatientInTableau = patientContexte == null 
         || (patientContexte.isChampIdVisible(3) && patientContexte.isChampInTableau(3));
      nipInTableau = patientContexte != null 
         && patientContexte.isChampIdVisible(2) && patientContexte.isChampInTableau(2);
   }

   @Override
   protected void renderPrelevement(final Row row, final Prelevement prel)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      congCellRendered = false;

      for(final Integer chpId : contexte.getChampEntiteInTableauOrdered()){
         applyPrelevementChpRender(chpId, row, prel);
      }

      renderNbEchans(row, prel);
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
   private void applyPrelevementChpRender(final Integer chpId, final Row row, final Prelevement prel)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{

      switch(chpId){
         case 23:
            // "code" toujours rendu par défaut
            break;
         case 45: // numero labo
            renderNumeroLabo(row, prel);
            break;
         case 24: // nature
            renderThesObjectProperty(row, prel, "nature");
            break;
         case 44: // nda
            renderAlphanumPropertyAsStringNoFormat(row, prel, "patientNda");
            break;
         case 30: // date prelevement
            renderDateProperty(row, prel, "datePrelevement");
            break;
         case 31: // prelevement type
            renderThesObjectProperty(row, prel, "prelevementType");
            break;
         case 47: // sterile
            renderBoolProperty(row, prel, "sterile");
            break;
         case 249: // risques -> rendu sous la forme d'une icône
            break;
         case 29: // service preleveur
            renderServicePreleveur(row, prel);
            break;
         case 28: // preleveur
            renderCollaborateurProperty(row, prel, "preleveur");
            break;
         case 32: // condit type
            renderThesObjectProperty(row, prel, "conditType");
            break;
         case 34: // condit Nbr
            renderNumberProperty(row, prel, "conditNbr");
            break;
         case 33: // condit milieu
            renderThesObjectProperty(row, prel, "conditMilieu");
            break;
         case 26: // consent type
            renderThesObjectProperty(row, prel, "consentType");
            break;
         case 27: // consent date
            renderDateProperty(row, prel, "consentDate");
            break;
         case 35: // date depart
            renderDateProperty(row, prel, "dateDepart");
            break;
         case 36: // transporteur
            renderTransporteurProperty(row, prel);
            break;
         case 37: // transport temperature
            renderNumberProperty(row, prel, "transportTemp");
            break;
         case 269: // cong depart
            renderCongProperty(row, prel);
            break;
         case 38: // date arrivee
            renderDateProperty(row, prel, "dateArrivee");
            break;
         case 39: // operateur
            renderCollaborateurProperty(row, prel, "operateur");
            break;
         case 40: // quantite
            renderQuantiteProperty(row, prel, "quantite", "quantiteUnite");
            break;
         case 270: // cong arrivee
            renderCongProperty(row, prel);
            break;
         case 256: // conforme arrivee -> rendu sous la forme d'une icône (chpId=257 raisons, ignore)
            break;
         default:
            break;
      }
   }

   private void renderCongProperty(final Row row, final Prelevement prel){
      if(!congCellRendered){
         if(prel.getCongDepart() != null && prel.getCongDepart()){
            new Label(Labels.getLabel("prelevement.cong.DEPART")).setParent(row);
         }else if(prel.getCongArrivee() != null && prel.getCongArrivee()){
            new Label(Labels.getLabel("prelevement.cong.ARRIVEE")).setParent(row);
         }else{
            new Label().setParent(row);
         }
         congCellRendered = true;
      }
   }
   
   /**
    * Affiche toujours l'identifiant du patient pour la banque du prélèvement.
    * Et affiche en plus de manière conditionnelle nom + prenom et nip si besoin.
    * @param row
    * @param prel
    */
   @Override
   protected void renderPatient(Prelevement prel, Row row){
      if(prel.getMaladie() != null){
         //TG-182 : 
         final Label identifiantLabel = 
            new Label(prel.getMaladie().getPatient().setBanqueAndGetIdentifiant(prel.getBanque()));
         if(getAccessPatient()){
            identifiantLabel.addForward(null, identifiantLabel.getParent(), "onClickPatient", prel);
            identifiantLabel.setClass("formLink");
         }
         identifiantLabel.setParent(row);  
      }else{
         new Label().setParent(row);
      }
      
      // nom + prenom
      renderPatientNomAndNip(prel, row, nomPrenomPatientInTableau, nipInTableau);
   }
   
   private void renderNumeroLabo(final Row row, final Prelevement prel){
      if(prel.getNumeroLabo() != null){
         final Label numeroLabel = new Label(prel.getNumeroLabo());
         numeroLabel.addForward(null, numeroLabel.getParent(), "onClickObject", prel);
         numeroLabel.setClass("formLink");
         numeroLabel.setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   private void renderServicePreleveur(final Row row, final Prelevement prel){
      if(prel.getServicePreleveur() != null){
         new Label(prel.getServicePreleveur().getEtablissement().getNom().concat(" ").concat(prel.getServicePreleveur().getNom()))
            .setParent(row);
      }else{
         new Label().setParent(row);
      }
   }

   private void renderTransporteurProperty(final Row row, final Prelevement prel){
      if(prel.getTransporteur() != null){
         new Label(prel.getTransporteur().getNom()).setParent(row);
      }else{
         new Label().setParent(row);
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

   public void setContexte(Contexte _c){
      this.contexte = _c;
   }
}
