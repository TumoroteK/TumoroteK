/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * mathieu.barthelemy@sls.aphp.fr
 * nathalie.dufay@chu-lyon.fr
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
package fr.aphp.tumorotek.interfacage.sgl.view.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.interfacage.sgl.view.ViewResultProcessor;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

public class DemLigResultProcessor implements ViewResultProcessor
{

   private final Logger log = LoggerFactory.getLogger(ViewResultProcessor.class);

   // PRE_CODE != D_CODE
   // identification_dossier = Numero_Inlog
   // PERMANENT = NIP
   // NOM2 != PATIENT
   // NOM_NAISSANCE?
   // Date_naissance format YYYYMMDD
   // DATE PRELEVEMENT format YYYYMMDD sans heures?
   // ANNOTATION PRELEVEMENT suite retour M. Sauget 10/12/2018
   // NUMHOSPI = Anciennement NDA
   // Code_prelevement
   // Prelevement
   // UF
   // Nom UF associations

   private TableAnnotationManager tableAnnotationManager;

   private EntiteManager entiteManager;

   public void setTableAnnotationManager(final TableAnnotationManager _t){
      this.tableAnnotationManager = _t;
   }

   public void setEntiteManager(final EntiteManager _e){
      this.entiteManager = _e;
   }

   @Override
   public DossierExterne processResult(final ResultSet rSet, final Banque bank) throws SQLException{
      final DossierExterne ext = new DossierExterne();
      ext.setIdentificationDossier(rSet.getString("NUMERO_INLOG")); // 1
      ext.setDateOperation(Calendar.getInstance());

      // patient
      final BlocExterne blocPatient = new BlocExterne();
      // 10 - PERMANENT
      if(rSet.getString("PERMANENT") != null && !rSet.getString("PERMANENT").trim().equals("")){
         final ValeurExterne valNip = new ValeurExterne();
         valNip.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNip);
         valNip.setChampEntiteId(2);
         valNip.setValeur(rSet.getString("PERMANENT"));
         log.debug(valNip.getValeur());
      }
      // 5 - PATIENT = NOM USUEL?l2
      if(rSet.getString("PATIENT") != null && !rSet.getString("PATIENT").trim().equals("")){
         final ValeurExterne valNom = new ValeurExterne();
         valNom.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNom);
         valNom.setChampEntiteId(3);
         valNom.setValeur(rSet.getString("PATIENT"));
         log.debug(valNom.getValeur());
      }
      // ? - NOM NAISSANCE = absent?
      if(rSet.getString("NOM2") != null && !rSet.getString("NOM2").trim().equals("")){
         final ValeurExterne valNomNais = new ValeurExterne();
         valNomNais.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNomNais);
         valNomNais.setChampEntiteId(4);
         valNomNais.setValeur(rSet.getString("NOM2"));
         log.debug(valNomNais.getValeur());
      }
      // 7 - PRENOM
      if(rSet.getString("PRENOM") != null && !rSet.getString("PRENOM").trim().equals("")){
         final ValeurExterne valPrenom = new ValeurExterne();
         valPrenom.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valPrenom);
         valPrenom.setChampEntiteId(5);
         valPrenom.setValeur(rSet.getString("PRENOM"));
      }
      // 8 - Date_Naissance
      if(rSet.getString("DATE_NAISSANCE") != null && !rSet.getString("DATE_NAISSANCE").trim().equals("")){
         final ValeurExterne valDN = new ValeurExterne();
         valDN.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valDN);
         valDN.setChampEntiteId(7);
         valDN.setValeur(rSet.getString("DATE_NAISSANCE"));
         log.debug(valDN.getValeur());
      }
      //9 - SEXE
      if(rSet.getString("SEXE") != null && !rSet.getString("SEXE").trim().equals("")){
         final ValeurExterne valSexe = new ValeurExterne();
         valSexe.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valSexe);
         valSexe.setChampEntiteId(6);
         valSexe.setValeur(rSet.getString("SEXE"));
         log.debug(valSexe.getValeur());
      }

      if(!blocPatient.getValeurs().isEmpty()){
         blocPatient.setDossierExterne(ext);
         blocPatient.setEntiteId(1);
         blocPatient.setOrdre(1);
         ext.getBlocExternes().add(blocPatient);
      }

      // prel
      final BlocExterne blocPrel = new BlocExterne();
      // 2 - NUMERO_LABO Code_prelevement
      if(rSet.getString("CODE_PRELEVEMENT") != null && !rSet.getString("CODE_PRELEVEMENT").trim().equals("")){
         final ValeurExterne valNumLabo = new ValeurExterne();
         valNumLabo.setBlocExterne(blocPrel);
         blocPrel.getValeurs().add(valNumLabo);
         valNumLabo.setChampEntiteId(45);
         valNumLabo.setValeur(rSet.getString("CODE_PRELEVEMENT"));
         log.debug(valNumLabo.getValeur());
      }
      // 4 - DATE PRELEVEMENT SANS HEURES?
      if(rSet.getString("DATE_PRELEVEMENT") != null && !rSet.getString("DATE_PRELEVEMENT").trim().equals("")){
         final ValeurExterne valDatePrel = new ValeurExterne();
         valDatePrel.setBlocExterne(blocPrel);
         blocPrel.getValeurs().add(valDatePrel);
         valDatePrel.setChampEntiteId(30);

         final String datePrel = rSet.getString("DATE_PRELEVEMENT");
         // Cif (rSet.getString("HEURE_PREL") != null) {
         // 	datePrel = datePrel + rSet.getString("HEURE_PREL");
         // }

         valDatePrel.setValeur(datePrel);
         log.debug(valDatePrel.getValeur());
      }
      // ANNOTATIONS PRELEVEMENT
      final List<TableAnnotation> tables =
         tableAnnotationManager.findByEntiteAndBanqueManager(entiteManager.findByIdManager(2), bank);
      Integer chpId;
      // 7 - NUMHOSPI
      if(rSet.getString("NUMHOSPI") != null && !rSet.getString("NUMHOSPI").trim().equals("")){
         chpId = findChampAnnotationIdByName("Numéro d'hospitalisation", tables);
         if(chpId != null){
            final ValeurExterne valNumHospit = new ValeurExterne();
            valNumHospit.setBlocExterne(blocPrel);
            blocPrel.getValeurs().add(valNumHospit);
            valNumHospit.setChampAnnotationId(chpId);
            valNumHospit.setValeur(rSet.getString("NUMHOSPI"));
            log.debug(valNumHospit.getValeur());
         }else{
            log.warn("NUMHOSPI non traité en l'absence du champ d'annotation");
         }
      }
      // Code_prelevement
      if(rSet.getString("Code_prelevement") != null && !rSet.getString("Code_prelevement").trim().equals("")){
         chpId = findChampAnnotationIdByName("Code de prélèvement", tables);
         if(chpId != null){
            final ValeurExterne valCodePrel = new ValeurExterne();
            valCodePrel.setBlocExterne(blocPrel);
            blocPrel.getValeurs().add(valCodePrel);
            valCodePrel.setChampAnnotationId(chpId);
            valCodePrel.setValeur(rSet.getString("Code_prelevement"));
            log.debug(valCodePrel.getValeur());
         }else{
            log.warn("Code_prelevement non traité en l'absence du champ d'annotation");
         }
      }
      // Prelevement
      if(rSet.getString("Prelevement") != null && !rSet.getString("Prelevement").trim().equals("")){
         chpId = findChampAnnotationIdByName("Prélèvement", tables);
         if(chpId != null){
            final ValeurExterne valPrel = new ValeurExterne();
            valPrel.setBlocExterne(blocPrel);
            blocPrel.getValeurs().add(valPrel);
            valPrel.setChampAnnotationId(chpId);
            valPrel.setValeur(rSet.getString("Prelevement"));
            log.debug(valPrel.getValeur());
         }else{
            log.warn("Prelevement non traité en l'absence du champ d'annotation");
         }
      }
      // UF
      if(rSet.getString("UF") != null && !rSet.getString("UF").trim().equals("")){
         chpId = findChampAnnotationIdByName("UF", tables);
         if(chpId != null){
            final ValeurExterne valUF = new ValeurExterne();
            valUF.setBlocExterne(blocPrel);
            blocPrel.getValeurs().add(valUF);
            valUF.setChampAnnotationId(chpId);
            valUF.setValeur(rSet.getString("UF"));
            log.debug(valUF.getValeur());
         }else{
            log.warn("UF non traité en l'absence du champ d'annotation");
         }
      }
      // Nom UF
      // UF
      if(rSet.getString("NOM_UF") != null && !rSet.getString("NOM_UF").trim().equals("")){
         chpId = findChampAnnotationIdByName("Nom de l'UF", tables);
         if(chpId != null){
            final ValeurExterne valNomUF = new ValeurExterne();
            valNomUF.setBlocExterne(blocPrel);
            blocPrel.getValeurs().add(valNomUF);
            valNomUF.setChampAnnotationId(chpId);
            valNomUF.setValeur(rSet.getString("NOM_UF"));
            log.debug(valNomUF.getValeur());
         }else{
            log.warn("NOM_UF non traité en l'absence du champ d'annotation");
         }
      }

      if(!blocPrel.getValeurs().isEmpty()){
         blocPrel.setDossierExterne(ext);
         blocPrel.setEntiteId(2);
         blocPrel.setOrdre(3);
         ext.getBlocExternes().add(blocPrel);
      }

      return ext;
   }

   /**
    * Pour bien faire devrait être remplacé par un service spécifique
    * findChampAnnotationByNameEntiteAndBanque
    * @param name
    * @param tables
    * @return champ
    */
   private Integer findChampAnnotationIdByName(final String name, final List<TableAnnotation> tables){
      Set<ChampAnnotation> chps;
      for(final TableAnnotation tableAnnotation : tables){
         chps = tableAnnotationManager.getChampAnnotationsManager(tableAnnotation);
         for(final ChampAnnotation champAnnotation : chps){
            if(champAnnotation.getNom().equals(name)){
               return champAnnotation.getId();
            }
         }
      }
      return null;
   }
}
