/**
 * Copyright ou © ou Copr. SESAN
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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.aphp.tumorotek.dto.MajDelaiCongelFromPrelevementDTO;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.ETypeDelaiCongelation;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.ModaleMajDelaiCongelationConstants;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.utils.TKDateUtils;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Cette classe permet de gérer les règles de gestion liées à la mise à jour des délais de congélation 
 * des échantillons d'un prélèvement suite à la mise à jour de la date de prélèvement.
 * Pour rappel, le délai de congélation (aussi appelé délai de stockage) est la différence entre la date/heure de stockage
 * et la date/heure du prélèvement
 * 
 * @since 2.3.0.1 (TK-427)
 * @author chuet
 *
 */
public class MajDelaiCongelFromPrelevementProcessor
{
   private EchantillonManager echantillonManager;

   public void setEchantillonManager(EchantillonManager echantillonManager){
      this.echantillonManager = echantillonManager;
   }
   
   /**
    * détermine la demande de confirmation à afficher à l'utilisateur selon : 
    *   - les valeurs de la date de prélèvement avant et après modification
    *   - les valeurs actuelles des délais de congélation des échantillons du prélèvement
    * le résultat est la clé du message internationalisé 
    * qui est ajoutée à l'objet majDelaiCongelDTO passé en paramètre. Ce dernier sera complété au fur et à mesure du process
    *   
    * Les différents cas et les règles associées sont décrites au niveau de {@link ECasMajDelaiCongelFromPrelevement}  
    * 
    * @param majDelaiCongelDTO objet contenant les éléments liés à la mise à jour. Il sera complété au fur et à mesure du process  
    */
   public void defineConfirmationADemander(MajDelaiCongelFromPrelevementDTO majDelaiCongelDTO) {
      if(majDelaiCongelDTO != null) {
         String keyMessage = null;
         int nbEchantillonConcerneParMessage = 0;
         
         defineCasMajDelaiCongel(majDelaiCongelDTO);
       
         //Récupère les échantillons du prélèvement et les tri par type de délai de congélation :
         Map<ETypeDelaiCongelation, List<Echantillon>> mapEchantillonByTypeDelaiCongelation =
            findByPrelevementAndSortByTypeDelaiCongelation(majDelaiCongelDTO.getPrelevement(), majDelaiCongelDTO.getOldDatePrelevement());
         int nbTotalEchantillon = Utils.retrieveNbElementInMapOfList(mapEchantillonByTypeDelaiCongelation);

         ECasMajDelaiCongelFromPrelevement casMajDelaiCongel = majDelaiCongelDTO.getCasMajDelaiCongel();
         switch(casMajDelaiCongel){
            case CAS1__DATES_AVANT_APRES_VALIDES:
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) {
                  keyMessage = casMajDelaiCongel.retrieveKeyI18nMessageForModifDatePrelevement(nbTotalEchantillon);
                  nbEchantillonConcerneParMessage=mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL).size();
               }
               break;

            case CAS2__DATE_AVANT_NON_VALIDE__DATE_APRES_VALIDE:
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) {
                  keyMessage = casMajDelaiCongel.retrieveKeyI18nMessageForModifDatePrelevement(nbTotalEchantillon);
                  nbEchantillonConcerneParMessage=mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL).size();
               }
               break;
               
            case CAS3__DATE_AVANT_VALIDE__DATE_APRES_NON_VALIDE:
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.THEORIQUE)) {
                  keyMessage = casMajDelaiCongel.retrieveKeyI18nMessageForModifDatePrelevement(nbTotalEchantillon);
                  nbEchantillonConcerneParMessage=mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.THEORIQUE).size();
               }
               break;
               
            default:
               break;
         }
         
         majDelaiCongelDTO.setKeyI18nTitreModaleConfirmation(ModaleMajDelaiCongelationConstants.KEY_TITLE__PRELEVEMENT__CONFIRMATION);
         majDelaiCongelDTO.setKeyI18nMessageModaleConfirmation(keyMessage);
         majDelaiCongelDTO.setNbEchantillonConcerneParConfirmation(nbEchantillonConcerneParMessage);
         majDelaiCongelDTO.setNbTotalEchantillon(nbTotalEchantillon);
      }
   }
   
   private void defineCasMajDelaiCongel(MajDelaiCongelFromPrelevementDTO majDelaiCongelDTO) {
      if(majDelaiCongelDTO != null) {
         ECasMajDelaiCongelFromPrelevement casMajDelaiCongel = null;
         Calendar oldDatePrelevement = majDelaiCongelDTO.getOldDatePrelevement();
         Calendar newDatePrelevement = majDelaiCongelDTO.getNewDatePrelevement();
         
         boolean oldDateValideForCalculDelaiCongel = TKDateUtils.isDateNonNullWithHeureSignificative(oldDatePrelevement);
         boolean newDateValideForCalculDelaiCongel = TKDateUtils.isDateNonNullWithHeureSignificative(newDatePrelevement);
         
         if(oldDateValideForCalculDelaiCongel && newDateValideForCalculDelaiCongel) {
            casMajDelaiCongel = ECasMajDelaiCongelFromPrelevement.CAS1__DATES_AVANT_APRES_VALIDES;
         }
         if(!oldDateValideForCalculDelaiCongel && newDateValideForCalculDelaiCongel) {
            casMajDelaiCongel = ECasMajDelaiCongelFromPrelevement.CAS2__DATE_AVANT_NON_VALIDE__DATE_APRES_VALIDE;
         }
         if(oldDateValideForCalculDelaiCongel && !newDateValideForCalculDelaiCongel) {
            casMajDelaiCongel = ECasMajDelaiCongelFromPrelevement.CAS3__DATE_AVANT_VALIDE__DATE_APRES_NON_VALIDE;
         }
         
         majDelaiCongelDTO.setCasMajDelaiCongel(casMajDelaiCongel);
      }
   }
   
   /**
    * met à jour les délai de congélation si besoin. Les éléments pour déterminer si c'est nécessaire et
    * ceux nécessaires au traitement (notamment la réponse de l'utilisateur si une confirmation lui a été demandée)
    * sont dans majDelaiCongelDTO
    * @param majDelaiCongelDTO
    */
   public void process(MajDelaiCongelFromPrelevementDTO majDelaiCongelDTO) {
      if(majDelaiCongelDTO != null) {
         //on retourne chercher les données car il peut y avoir eu une modification sur les échantillons par un autre utilisateur
         //entre la première récupération pour 
         Map<ETypeDelaiCongelation, List<Echantillon>> mapEchantillonByTypeDelaiCongelation =
            findByPrelevementAndSortByTypeDelaiCongelation(majDelaiCongelDTO.getPrelevement(), majDelaiCongelDTO.getOldDatePrelevement());

         ECasMajDelaiCongelFromPrelevement casMajDelaiCongel = majDelaiCongelDTO.getCasMajDelaiCongel();
         Boolean confirmationUtilisateur = majDelaiCongelDTO.isConfirmationUtilisateur();
         List<Echantillon> listEchantillonToUpdate = new ArrayList<Echantillon>();
         switch(casMajDelaiCongel){
            case CAS1__DATES_AVANT_APRES_VALIDES:
               //par défaut on met à jour les échantillons avec délai théorique et ceux sans délai mais une date de stockage valide
               //si l'utilisateur confirme, on met aussi à jour les échantillons avec délai saisi et date de stockage avec heure
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.THEORIQUE)) {
                  listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.THEORIQUE));
               }
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.UNDEFINED_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) {               
                  listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.UNDEFINED_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL));
               }

               if(confirmationUtilisateur != null && confirmationUtilisateur) {
                  if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) {   
                     listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL));
                  }
               }
               
               echantillonManager.updateDelaiCongelationWithTheoriqueIfPossible(majDelaiCongelDTO.getNewDatePrelevement(), listEchantillonToUpdate);
               
               break;

            case CAS2__DATE_AVANT_NON_VALIDE__DATE_APRES_VALIDE:
               //par défaut on met à jour les échantillons sans délai mais une date de stockage valide
               //si l'utilisateur confirme, on met aussi à jour les échantillons avec délai saisi et date de stockage avec heure
               if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.UNDEFINED_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) { 
                  listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.UNDEFINED_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL));
               }

               if(confirmationUtilisateur != null && confirmationUtilisateur) {
                  if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL)) { 
                     listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL));
                  }
               }
               
               echantillonManager.updateDelaiCongelationWithTheoriqueIfPossible(majDelaiCongelDTO.getNewDatePrelevement(), listEchantillonToUpdate);

               break;
               
            case CAS3__DATE_AVANT_VALIDE__DATE_APRES_NON_VALIDE:
             //si l'utilisateur confirme, on supprime les délais théorique
               if(confirmationUtilisateur != null && confirmationUtilisateur) {
                  if(mapEchantillonByTypeDelaiCongelation.containsKey(ETypeDelaiCongelation.THEORIQUE)) { 
                     listEchantillonToUpdate.addAll(mapEchantillonByTypeDelaiCongelation.get(ETypeDelaiCongelation.THEORIQUE));
                  }
                  
                  echantillonManager.removeDelaiCongelation(listEchantillonToUpdate);
               }
               break;
               
            default:
               break;
         }
      }

   }
   
   /**
    * Récupère tous les échantillons du prélèvement passé en paramètre 
    * et les retourne classés selon le type de leur délai de congélation stocké en base de données.
    * Les types sont définis dans l'enum {@link ETypeDelaiCongelation}
    * @since 2.3.0.1 (TK-427)
    *
    * @param prelevement Le prélèvement dont les échantillons doivent être récupérés.
    * @param datePrelevement La date du prélèvement pour le calcul du délai de congélation théorique.
    * @return Une map d'échantillons.
    */
   private Map<ETypeDelaiCongelation, List<Echantillon>> findByPrelevementAndSortByTypeDelaiCongelation(Prelevement prelevement, Calendar datePrelevement) {
      // Récupère la liste des échantillons associés au prélèvement
      List<Echantillon> echantillons = echantillonManager.findByPrelevementManager(prelevement);
      Map<ETypeDelaiCongelation, List<Echantillon>> mapEchantillonsByTypeDelaiCongelation = new HashMap<ETypeDelaiCongelation, List<Echantillon>>();

      ETypeDelaiCongelation currentType;
      for (Echantillon echantillon : echantillons) {
         // Récupère le délai de congélation stocké en base de données (en minutes)
         Float delaiCongelationFromDB = echantillon.getDelaiCgl();
         // Calcule le délai de congélation théorique (appelé aussi délai de stockage) pour l'échantillon actuel
         long delaiCongelationTheorique = echantillonManager.calculDelaiStockage(echantillon, datePrelevement);
         // Convertit le délai calculé en minutes
         float delaiCongelationTheoriqueEnMinutes = TKDateUtils.convertMillisecondsToMinutes(delaiCongelationTheorique);
         if(delaiCongelationFromDB == null) {
            if(TKDateUtils.isDateNonNullWithHeureSignificative(echantillon.getDateStock())) {
               currentType = ETypeDelaiCongelation.UNDEFINED_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL;
            }
            else {
               currentType = ETypeDelaiCongelation.UNDEFINED_WITHOUT_DATE_STOCKAGE_VALIDE_FOR_CALCUL;
            }
         }
         else if (delaiCongelationFromDB == delaiCongelationTheoriqueEnMinutes) {
            currentType = ETypeDelaiCongelation.THEORIQUE;
         }
         else {
            if(TKDateUtils.isDateNonNullWithHeureSignificative(echantillon.getDateStock())) {
               currentType = ETypeDelaiCongelation.SAISI_WITH_DATE_STOCKAGE_VALIDE_FOR_CALCUL;
            }
            else {
               currentType = ETypeDelaiCongelation.SAISI_WITHOUT_DATE_STOCKAGE_VALIDE_FOR_CALCUL;
            }
         }
         mapEchantillonsByTypeDelaiCongelation.computeIfAbsent(currentType, key -> new ArrayList<Echantillon>()).add(echantillon);
      }
      
      return mapEchantillonsByTypeDelaiCongelation;
   }
   
}
