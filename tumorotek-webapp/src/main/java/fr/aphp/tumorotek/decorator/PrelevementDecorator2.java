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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;

/**
 * Classe 'Decorateur' qui reprend les attributs de Prelevement.
 * pour les afficher dans la liste associées à une maladie:
 *  - Date de prelevement
 *  - Code
 *  - Nature
 *  - Type
 *  - Consentement
 *  - Nb d'échantillons restants par rapport au total
 * date: 19/11/09
 *
 * @version 2.0
 * @author mathieu
 *
 */
public class PrelevementDecorator2
{

   private Prelevement prelevement;

   /**
    * Constructeur.
    * @param prel
    */
   public PrelevementDecorator2(final Prelevement prel){
      this.prelevement = prel;
   }

   public Prelevement getPrelevement(){
      return this.prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

   /**
    * Utilise le format de date AbstractFicheController.
    * @return date du prélèvement formattée pour affichage zk
    */
   public String getFormattedPrelevementDate(){
      return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDatePrelevement());
   }

   public String getCode(){
      if(this.prelevement != null){
         return this.prelevement.getCode();
      }else{
         return null;
      }
   }

   public String getPatientNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      //Maladie maladie = ManagerLocator.getPrelevementManager()
      //	.getMaladieManager(this.prelevement);
      final Maladie maladie = this.prelevement.getMaladie();

      if(maladie != null && maladie.getPatient() != null){
         final Patient pat = maladie.getPatient();

         if(pat.getNom() != null && pat.getPrenom() != null){
            sb.append(pat.getNom());
            sb.append(" ");
            sb.append(pat.getPrenom());
         }else if(pat.getNom() != null){
            sb.append(pat.getNom());
         }else if(pat.getPrenom() != null){
            sb.append(pat.getPrenom());
         }
      }
      return sb.toString();
   }

   public String getMaladie(){
      //Maladie maladie = ManagerLocator.getPrelevementManager()
      //	.getMaladieManager(this.prelevement);

      if(this.prelevement.getMaladie() != null){
         return this.prelevement.getMaladie().getCode();
      }else{
         return null;
      }
   }

   public String getNature(){
      if(this.prelevement.getNature() != null){
         return this.prelevement.getNature().getNature();
      }else{
         return null;
      }
   }

   public String getType(){
      if(this.prelevement.getPrelevementType() != null){
         return this.prelevement.getPrelevementType().getType();
      }else{
         return null;
      }
   }

   public String getConsentement(){
      if(this.prelevement.getConsentType() != null){
         return this.prelevement.getConsentType().getType();
      }else{
         return null;
      }
   }

   public String getConsentementDate(){
      if(this.prelevement.getConsentDate() != null){
         return ObjectTypesFormatters.dateRenderer2(this.prelevement.getConsentDate());
      }else{
         return null;
      }
   }

   /**
    * Recupere le nombre d'echantillons restants.
    * @return x restants
    */
   public int getNbEchanRestants(){
      return ManagerLocator.getEchantillonManager().findRestantsByPrelevementManager(this.prelevement).size();
   }

   /**
    * Recupere les echantillons restants et totals pour concaténer
    * sous la forme total (x restants).
    * @return y (x restants)
    */
   public String getNbEchanRestantsSurTotal(){
      /*String totalNb = String
      	.valueOf(ManagerLocator.getPrelevementManager()
      				.getEchantillonsManager(this.prelevement).size());
      return totalNb + " (" + String.valueOf(getNbEchanRestants()) + " " 
      		+ Labels.getLabel("prelevement.echantillons.restants") + ")";*/

      final StringBuffer sb = new StringBuffer();
      sb.append(getNbEchanRestants());
      sb.append(" / ");
      sb.append(ManagerLocator.getPrelevementManager().getEchantillonsManager(this.prelevement).size());

      return sb.toString();
   }

   /**
    * Récupère la date de création système du prélèvement.
    * @return Date de création.
    */
   public String getDateCreation(){
      final Calendar date = ManagerLocator.getOperationManager().findDateCreationManager(this.prelevement);
      if(date != null){
         return ObjectTypesFormatters.dateRenderer2(date);
      }else{
         return null;
      }
   }

   public String getBanque(){
      if(this.prelevement.getBanque() != null){
         return this.prelevement.getBanque().getNom();
      }else{
         return null;
      }
   }

   public String getPreleveurNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      final Collaborateur preleveur = this.prelevement.getPreleveur();

      if(preleveur.getNom() != null && preleveur.getPrenom() != null){
         sb.append(preleveur.getNom());
         sb.append(" ");
         sb.append(preleveur.getPrenom());
      }else if(preleveur.getNom() != null){
         sb.append(preleveur.getNom());
      }else if(preleveur.getPrenom() != null){
         sb.append(preleveur.getPrenom());
      }
      return sb.toString();
   }

   public String getServicePreleveur(){
      if(this.prelevement.getServicePreleveur() != null){
         return this.prelevement.getServicePreleveur().getNom();
      }else{
         return null;
      }
   }

   public String getTypeConditionnement(){
      if(this.prelevement.getConditType() != null){
         return this.prelevement.getConditType().getType();
      }else{
         return null;
      }
   }

   public String getMilieuConditionnement(){
      if(this.prelevement.getConditMilieu() != null){
         return this.prelevement.getConditMilieu().getMilieu();
      }else{
         return null;
      }
   }

   public String getNombreConditionnement(){
      if(this.prelevement.getConditNbr() != null){
         return this.prelevement.getConditNbr().toString();
      }else{
         return null;
      }
   }

   /**
    * Utilise le format de date AbstractFicheController.
    * @return date départ formattée pour affichage zk
    */
   public String getFormattedDateDepart(){
      return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDateDepart());
   }

   /**
    * Utilise le format de date AbstractFicheController.
    * @return date arrivée formattée pour affichage zk
    */
   public String getFormattedDateArrivee(){
      return ObjectTypesFormatters.dateRenderer2(this.prelevement.getDateArrivee());
   }

   public String getTransporteur(){
      if(this.prelevement.getTransporteur() != null){
         return this.prelevement.getTransporteur().getNom();
      }else{
         return null;
      }
   }

   public String getTransportTemperature(){
      if(this.prelevement.getTransportTemp() != null){
         return this.prelevement.getTransportTemp().toString();
      }else{
         return null;
      }
   }

   public String getOperateurNomAndPrenom(){
      final StringBuffer sb = new StringBuffer();
      final Collaborateur operateur = this.prelevement.getOperateur();

      if(operateur.getNom() != null && operateur.getPrenom() != null){
         sb.append(operateur.getNom());
         sb.append(" ");
         sb.append(operateur.getPrenom());
      }else if(operateur.getNom() != null){
         sb.append(operateur.getNom());
      }else if(operateur.getPrenom() != null){
         sb.append(operateur.getPrenom());
      }
      return sb.toString();
   }

   /**
    * Concatène la quantité et son unité.
    * @return String.
    */
   public String getQuantite(){
      final StringBuffer sb = new StringBuffer();
      if(this.prelevement.getQuantite() != null){
         sb.append(this.prelevement.getQuantite());
      }else{
         sb.append("-");
      }

      if(this.prelevement.getQuantiteUnite() != null){
         sb.append(" ");
         sb.append(this.prelevement.getQuantiteUnite().getUnite());
      }

      return sb.toString();
   }

   public String getNdaPatient(){
      return this.prelevement.getPatientNda();
   }

   public String getNumeroLaboratoire(){
      return this.prelevement.getNumeroLabo();
   }

   public String getSterile(){
      if(this.prelevement.getSterile() != null){
         if(this.prelevement.getSterile().booleanValue() == true){
            return "oui";
         }else{
            return "non";
         }
      }else{
         return null;
      }
   }

   public String getEtatIncomplet(){
      if(this.prelevement.getEtatIncomplet() != null){
         if(this.prelevement.getEtatIncomplet().booleanValue() == true){
            return "oui";
         }else{
            return "non";
         }
      }else{
         return null;
      }
   }

   public String getArchive(){
      if(this.prelevement.getArchive() != null){
         if(this.prelevement.getArchive().booleanValue() == true){
            return "oui";
         }else{
            return "non";
         }
      }else{
         return null;
      }
   }

   /**
    * Decore une liste de prelevements.
    * @param prels
    * @return prelevements décorés.
    */
   public static List<PrelevementDecorator2> decorateListe(final List<Prelevement> prels){
      final List<PrelevementDecorator2> liste = new ArrayList<>();
      final Iterator<Prelevement> it = prels.iterator();
      while(it.hasNext()){
         liste.add(new PrelevementDecorator2(it.next()));
      }
      return liste;
   }

   @Override
   public boolean equals(final Object obj){

      if(this == obj){
         return true;
      }

      if((obj == null) || obj.getClass() != this.getClass()){
         return false;
      }

      final PrelevementDecorator2 deco = (PrelevementDecorator2) obj;
      return this.getPrelevement().equals(deco.getPrelevement());

   }

   @Override
   public int hashCode(){

      int hash = 7;
      int hashPrlvt = 0;

      if(this.prelevement != null){
         hashPrlvt = this.prelevement.hashCode();
      }

      hash = 7 * hash + hashPrlvt;

      return hash;
   }
}
