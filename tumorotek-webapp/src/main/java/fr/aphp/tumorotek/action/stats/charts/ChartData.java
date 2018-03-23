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
package fr.aphp.tumorotek.action.stats.charts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.PieModel;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.GraphesModele;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Classe contenant tous les graphes modeles qui sont
 * utilisés pour l'affichage.
 *
 * date: 20/01/2014
 *
 * @author Marc DESCHAMPS
 * @version 2.0.10
 */
public class ChartData
{

   GraphesModele patientPfModel = null;
   GraphesModele prelevementPfModel = null;
   GraphesModele echanPfModel = null;
   GraphesModele derivePfModel = null;
   GraphesModele cessionPfModel = null;

   GraphesModele patientCollModel = null;
   GraphesModele prelevementCollModel = null;
   GraphesModele echanCollModel = null;
   GraphesModele deriveCollModel = null;
   GraphesModele cessionCollModel = null;

   GraphesModele echansCedesCollModel = null;
   GraphesModele derivesCedesCollModel = null;

   GraphesModele prelTypeCollModel = null;
   GraphesModele prelEtabCollModel = null;
   GraphesModele prelConsentCollModel = null;

   GraphesModele echanTypeCollModel = null;
   GraphesModele echanCimCollModel = null;
   GraphesModele echanOrgCollModel = null;

   GraphesModele deriveTypeCollModel = null;

   GraphesModele cessionTypeCollModel = null;

   private boolean isOracle = false;

   private final ChartDataTriggers triggers;

   /**
    * A la construction initialise les comptes pour les pfs.
    * Ces comptes ne seront pas mis à jours.
    * @param pfs Liste des pfs actuellement accessible lors de 
    * la session par l'utilisateur
    */
   public ChartData(final Date date_debut, final Date date_fin, final Set<Plateforme> pfs, final ChartDataTriggers trgs,
      final String pfNom, final String bNom){

      triggers = trgs;

      isOracle = SessionUtils.isOracleDBMS();

      patientCollModel = null;

      prelevementCollModel = null;
      prelTypeCollModel = null;
      prelEtabCollModel = null;
      prelConsentCollModel = null;

      echanCollModel = null;
      echanTypeCollModel = null;
      echanCimCollModel = null;
      echanOrgCollModel = null;

      deriveCollModel = null;
      deriveTypeCollModel = null;

      cessionCollModel = null;
      echansCedesCollModel = null;
      derivesCedesCollModel = null;
      cessionTypeCollModel = null;

      if(triggers.getPatientPfModelTrg()){
         patientPfModel = ManagerLocator.getGraphesModeleManager().platformeViewByPatientManager(date_debut, date_fin, isOracle);
         restrictPfs(patientPfModel, pfs);
      }
      if(triggers.getPatientCollModelTrg()){
         patientCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByPatientManager(date_debut, date_fin, pfNom, isOracle);
      }
      //--- 
      if(triggers.getPrelevementPfModelTrg()){
         prelevementPfModel =
            ManagerLocator.getGraphesModeleManager().platformeViewByPrelevementManager(date_debut, date_fin, isOracle);
         restrictPfs(prelevementPfModel, pfs);
      }
      if(triggers.getPrelevementCollModelTrg()){
         prelevementCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByPrelevementManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getPrelEtabCollModelTrg()){
         prelEtabCollModel = ManagerLocator.getGraphesModeleManager().prelevementByEtablissementByCollectionManager(date_debut,
            date_fin, bNom, pfNom, isOracle);
      }
      if(triggers.getPrelTypeCollModelTrg()){
         prelTypeCollModel = ManagerLocator.getGraphesModeleManager().prelevementTypeByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);
      }
      if(triggers.getPrelConsentCollModelTrg()){
         prelConsentCollModel = ManagerLocator.getGraphesModeleManager().prelevementByConsentementByCollectionManager(date_debut,
            date_fin, bNom, pfNom, isOracle);
      }
      //---
      if(triggers.getEchanPfModelTrg()){
         echanPfModel =
            ManagerLocator.getGraphesModeleManager().platformeViewByEchantillonManager(date_debut, date_fin, isOracle);
         restrictPfs(echanPfModel, pfs);
      }
      if(triggers.getEchanCollModelTrg()){
         echanCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByEchantillonManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getEchanTypeCollModelTrg()){
         echanTypeCollModel = ManagerLocator.getGraphesModeleManager().echantillonTypeByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);
      }
      if(triggers.getEchanCimCollModelTrg()){
         echanCimCollModel = ManagerLocator.getGraphesModeleManager().echantillonsCIM10ByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);
      }
      if(triggers.getEchanOrgCollModelTrg()){
         echanOrgCollModel = ManagerLocator.getGraphesModeleManager().echantillonsADICAPByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);
      }

      //---
      if(triggers.getDerivePfModelTrg()){
         derivePfModel = ManagerLocator.getGraphesModeleManager().platformeViewByDeriveManager(date_debut, date_fin, isOracle);
         restrictPfs(derivePfModel, pfs);
      }
      if(triggers.getDeriveCollModelTrg()){
         deriveCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByDeriveManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getDeriveTypeCollModelTrg()){
         deriveTypeCollModel =
            ManagerLocator.getGraphesModeleManager().deriveTypeByCollectionManager(date_debut, date_fin, bNom, pfNom, isOracle);
      }

      //---

      if(triggers.getCessionPfModelTrg()){
         cessionPfModel = ManagerLocator.getGraphesModeleManager().platformeViewByCessionManager(date_debut, date_fin, isOracle);
         restrictPfs(cessionPfModel, pfs);
      }
      if(triggers.getCessionCollModelTrg()){
         cessionCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByCessionManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getEchansCedesCollModelTrg()){
         echansCedesCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByEchansCedesManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getDerivesCedesCollModelTrg()){
         derivesCedesCollModel =
            ManagerLocator.getGraphesModeleManager().collectionViewByDerivesCedesManager(date_debut, date_fin, pfNom, isOracle);
      }
      if(triggers.getCessionTypeCollModelTrg()){
         cessionTypeCollModel =
            ManagerLocator.getGraphesModeleManager().cessionTypeByCollectionManager(date_debut, date_fin, bNom, pfNom, isOracle);
      }
   }

   /**
    * Mets à jour les comptes pour les collections quand un 
    * changement de pf est demandé.
    * @param pfNom
    */
   public void updateCountsAfterPlateformeChange(final Date date_debut, final Date date_fin, final String pfNom){
      patientCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByPatientManager(date_debut, date_fin, pfNom, isOracle);

      prelevementCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByPrelevementManager(date_debut, date_fin, pfNom, isOracle);

      echanCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByEchantillonManager(date_debut, date_fin, pfNom, isOracle);

      deriveCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByDeriveManager(date_debut, date_fin, pfNom, isOracle);

      cessionCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByCessionManager(date_debut, date_fin, pfNom, isOracle);

      echansCedesCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByEchansCedesManager(date_debut, date_fin, pfNom, isOracle);

      derivesCedesCollModel =
         ManagerLocator.getGraphesModeleManager().collectionViewByDerivesCedesManager(date_debut, date_fin, pfNom, isOracle);

      prelTypeCollModel = null;
      prelEtabCollModel = null;
      prelConsentCollModel = null;
      echanTypeCollModel = null;
      echanCimCollModel = null;
      echanOrgCollModel = null;
      deriveTypeCollModel = null;
      cessionTypeCollModel = null;
   }

   /**
    * Mets à jour les comptes pour les collection pour les propriétés 
    * spécifiques de l'entité passé en paramètre
    * @param bNom
    * @param entiteNom (Prelevement/Echantillon/ProdDerive/Cession)
    */
   public void updateCountsAfterBanqueChange(final Date date_debut, final Date date_fin, final String bNom,
      final String entiteNom, final String pfNom){

      if(entiteNom == null || entiteNom.equals("Prelevement")){
         prelTypeCollModel = ManagerLocator.getGraphesModeleManager().prelevementTypeByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);

         prelEtabCollModel = ManagerLocator.getGraphesModeleManager().prelevementByEtablissementByCollectionManager(date_debut,
            date_fin, bNom, pfNom, isOracle);

         prelConsentCollModel = ManagerLocator.getGraphesModeleManager().prelevementByConsentementByCollectionManager(date_debut,
            date_fin, bNom, pfNom, isOracle);

         echanTypeCollModel = null;
         echanCimCollModel = null;
         echanOrgCollModel = null;
         deriveTypeCollModel = null;
         cessionTypeCollModel = null;
      }

      if(entiteNom == null || entiteNom.equals("Echantillon")){
         echanTypeCollModel = ManagerLocator.getGraphesModeleManager().echantillonTypeByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);

         echanCimCollModel = ManagerLocator.getGraphesModeleManager().echantillonsCIM10ByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);

         echanOrgCollModel = ManagerLocator.getGraphesModeleManager().echantillonsADICAPByCollectionManager(date_debut, date_fin,
            bNom, pfNom, isOracle);

         prelTypeCollModel = null;
         prelEtabCollModel = null;
         prelConsentCollModel = null;
         deriveTypeCollModel = null;
         cessionTypeCollModel = null;
      }

      if(entiteNom == null || entiteNom.equals("ProdDerive")){
         deriveTypeCollModel =
            ManagerLocator.getGraphesModeleManager().deriveTypeByCollectionManager(date_debut, date_fin, bNom, pfNom, isOracle);
         prelTypeCollModel = null;
         prelEtabCollModel = null;
         prelConsentCollModel = null;
         echanTypeCollModel = null;
         echanCimCollModel = null;
         echanOrgCollModel = null;
         cessionTypeCollModel = null;
      }

      if(entiteNom == null || entiteNom.equals("Cession")){
         cessionTypeCollModel =
            ManagerLocator.getGraphesModeleManager().cessionTypeByCollectionManager(date_debut, date_fin, bNom, pfNom, isOracle);
         prelTypeCollModel = null;
         prelEtabCollModel = null;
         prelConsentCollModel = null;
         echanTypeCollModel = null;
         echanCimCollModel = null;
         echanOrgCollModel = null;
         deriveTypeCollModel = null;
      }
   }

   /**
    * Filtre les résultats obtenus lors des requêtes de comptes de 
    * plateformes pour ne conserver que ceux concernant les pfs 
    * de la liste passée en paramètre
    * @param grM modèle à filtrer
    * @param pfs Liste de plateformes accessibles
    */
   private void restrictPfs(final GraphesModele grM, final Set<Plateforme> pfs){

      final List<String> pfNamesRestrict = new ArrayList<>();
      for(final Plateforme pf : pfs){
         pfNamesRestrict.add(pf.getNom());
      }

      final List<String> cKeys = new ArrayList<>();
      if(grM != null && pfs != null && !pfs.isEmpty()){
         cKeys.addAll(grM.getCountsMap().keySet());
         for(final String pfKey : cKeys){
            if(!pfNamesRestrict.contains(pfKey)){
               grM.getCountsMap().remove(pfKey);
            }
         }
      }
   }

   /**
    * Transforme un grapheModele en CategoryModel
    * @param GrapheModele data
    * @return CatagoryModel model
    */
   public static void buildCategoryModel(final GraphesModele grM, final CategoryModel model){
      if(model != null && grM != null){
         model.clear();
         for(final Map.Entry<String, List<Integer>> entry : grM.getCountsMap().entrySet()){
            for(int i = 0; i < entry.getValue().size(); i++){
               model.setValue("main" + (i == 0 ? "" : i), entry.getKey(), entry.getValue().get(i));
            }
         }
      }
   }

   /**
    * Transforme un grapheModele en PieModel
    * @param GrapheModele data
    * @return PieModel model
    */
   public static void buildPieModel(final GraphesModele grM, final PieModel model){
      if(model != null && grM != null){
         model.clear();
         for(final Map.Entry<String, List<Integer>> entry : grM.getCountsMap().entrySet()){
            model.setValue(entry.getKey(), entry.getValue().get(0));
         }
      }
   }

   public GraphesModele getPatientPfModel(){
      return patientPfModel;
   }

   public GraphesModele getPrelevementPfModel(){
      return prelevementPfModel;
   }

   public GraphesModele getEchanPfModel(){
      return echanPfModel;
   }

   public GraphesModele getDerivePfModel(){
      return derivePfModel;
   }

   public GraphesModele getCessionPfModel(){
      return cessionPfModel;
   }

   public GraphesModele getPatientCollModel(){
      return patientCollModel;
   }

   public GraphesModele getPrelevementCollModel(){
      return prelevementCollModel;
   }

   public GraphesModele getEchanCollModel(){
      return echanCollModel;
   }

   public GraphesModele getDeriveCollModel(){
      return deriveCollModel;
   }

   public GraphesModele getCessionCollModel(){
      return cessionCollModel;
   }

   public GraphesModele getPrelTypeCollModel(){
      return prelTypeCollModel;
   }

   public GraphesModele getPrelEtabCollModel(){
      return prelEtabCollModel;
   }

   public GraphesModele getPrelConsentCollModel(){
      return prelConsentCollModel;
   }

   public GraphesModele getEchanTypeCollModel(){
      return echanTypeCollModel;
   }

   public GraphesModele getEchanCimCollModel(){
      return echanCimCollModel;
   }

   public GraphesModele getEchanOrgCollModel(){
      return echanOrgCollModel;
   }

   public GraphesModele getDeriveTypeCollModel(){
      return deriveTypeCollModel;
   }

   public GraphesModele getCessionTypeCollModel(){
      return cessionTypeCollModel;
   }

   public GraphesModele getEchansCedesCollModel(){
      return echansCedesCollModel;
   }

   public GraphesModele getDerivesCedesCollModel(){
      return derivesCedesCollModel;
   }

   public ChartDataTriggers getTriggers(){
      return triggers;
   }
}
