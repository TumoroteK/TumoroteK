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
package fr.aphp.tumorotek.dto;

import java.util.Calendar;

import fr.aphp.tumorotek.manager.impl.coeur.prelevement.ECasMajDelaiCongelFromPrelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * cette classe permet de stocker toutes les informations nécessaires pour gérer l'éventuelle mise à jour des délais 
 * de congélation des échantillons d'un prélèvement dont la date de prélèvement a été modifié
 * les différents attributs sont mis à jour au fur et à mesure des actions de l'utilisateur. L'objet est passé
 * à la méthode du back responsable de la mise à jour du prélèvement.
 * 
 * @since 2.3.1.0 (demande TK-427)
 * @author chuet
 *
 */
public class MajDelaiCongelFromPrelevementDTO
{
   //prélèvement concerné par le changement de date de prélèvement qui peut avoir un impact sur les délais de congélation de ses échantillons
   private Prelevement prelevement;
   
   //date de prélèvement avant modification par l'utilisateur
   private Calendar oldDatePrelevement;
   
   //valeur d'une enum, qui définit l'impact de la mise à jour de la date de prélèvement sur les délais de congélation des échantillons
   private ECasMajDelaiCongelFromPrelevement casMajDelaiCongel;
   
   //// éléments pour construire le message à afficher à l'utilisateur pour lui demander de confirmer des modifications envisagées
   //clé des messages internationalisés
   private String keyI18nTitreModaleConfirmation;
   private String keyI18nMessageModaleConfirmation;
   //nombre d'échantillons concernés par les modifications envisagées : il sera utilisé pour personnaliser le message de demande de confirmation
   private int nbEchantillonConcerneParConfirmation;
   //nombre d'échantillons rattachés au prélèvement (utilisé pour personnaliser le message de demande de confirmation)
   private int nbTotalEchantillon;
   ////
   
   //vaut true si l'utilisateur a répondu oui dans la modale de confirmation de la mise à jour des délais
   private Boolean confirmationUtilisateur;

   
   public MajDelaiCongelFromPrelevementDTO(Prelevement prelevement) {
      this.prelevement = prelevement;
   }
   
   public Prelevement getPrelevement(){
      return prelevement;
   }
   
   public Calendar getOldDatePrelevement(){
      return oldDatePrelevement;
   }
   public void setOldDatePrelevement(Calendar oldDatePrelevement){
      this.oldDatePrelevement = oldDatePrelevement;
   }
   public Calendar getNewDatePrelevement(){
      if(prelevement != null) {
         return prelevement.getDatePrelevement();
      }
      
      return null;
   }


   public ECasMajDelaiCongelFromPrelevement getCasMajDelaiCongel(){
      return casMajDelaiCongel;
   }

   public void setCasMajDelaiCongel(ECasMajDelaiCongelFromPrelevement casMajDelaiCongel){
      this.casMajDelaiCongel = casMajDelaiCongel;
   }
   
   public String getKeyI18nTitreModaleConfirmation(){
      return keyI18nTitreModaleConfirmation;
   }
   public void setKeyI18nTitreModaleConfirmation(String keyI18nTitreModaleConfirmation){
      this.keyI18nTitreModaleConfirmation = keyI18nTitreModaleConfirmation;
   }
   public String getKeyI18nMessageModaleConfirmation(){
      return keyI18nMessageModaleConfirmation;
   }
   public void setKeyI18nMessageModaleConfirmation(String keyI18nMessageModaleConfirmation){
      this.keyI18nMessageModaleConfirmation = keyI18nMessageModaleConfirmation;
   }
   public int getNbEchantillonConcerneParConfirmation(){
      return nbEchantillonConcerneParConfirmation;
   }
   public void setNbEchantillonConcerneParConfirmation(int nbEchantillonConcerneParConfirmation){
      this.nbEchantillonConcerneParConfirmation = nbEchantillonConcerneParConfirmation;
   }
   public int getNbTotalEchantillon(){
      return nbTotalEchantillon;
   }
   public void setNbTotalEchantillon(int nbTotalEchantillon){
      this.nbTotalEchantillon = nbTotalEchantillon;
   }
   
   public Boolean isConfirmationUtilisateur(){
      return confirmationUtilisateur;
   }
   public void setConfirmationUtilisateur(Boolean confirmationUtilisateur){
      this.confirmationUtilisateur = confirmationUtilisateur;
   }
}
