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
package fr.aphp.tumorotek.webapp.general;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.io.export.standard.ExportCatalogueManager;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Thread permettant un affichage dynamique de l'évolution du processus
 * d'export (qui peut etre assez long).
 * @author pierre
 *
 */
public class ExportThread extends Thread
{

   private static Log log = LogFactory.getLog(ExportThread.class);

   private final Desktop desktop;

   private final String nomEntite;

   private final List<? extends Object> objs;

   private final List<Banque> banques;

   private final boolean isExportAnonyme;

   private final Utilisateur user;

   private Catalogue catalogue;

   public Catalogue getCatalogue(){
      return catalogue;
   }

   public void setCatalogue(final Catalogue c){
      this.catalogue = c;
   }

   public ExportThread(final Desktop d, final String ent, final List<? extends Object> o, final List<Banque> b, final boolean e,
      final Utilisateur u){
      desktop = d;
      nomEntite = ent;
      objs = o;
      banques = b;
      isExportAnonyme = e;
      user = u;
   }

   /**
    * Méthode principale lançant l'export.
    */
   @Override
   public void run(){
      // Compute Time
      long startTime = System.nanoTime();
      startTime = System.nanoTime();
      long endTime = System.nanoTime();
      // ----------------------------------------------
      if(nomEntite.equals("Patient")){
         exportPatients();
      }else if(nomEntite.equals("Prelevement")){
         exportPrelevements();
      }else if(nomEntite.equals("Echantillon")){
         if(getCatalogue() == null){
            exportEchantillons();
         }else{
            exportEchantillons(getCatalogue());
         }
      }else if(nomEntite.equals("ProdDerive")){
         exportProdDerives();
      }else if(nomEntite.equals("Cession")){
         exportCessions();
      }
      endTime = System.nanoTime();
      System.out.println("Total elapsed time in execution of method is :" + ((endTime - startTime) / 1000000000.0));
   }

   /*************************************************************************/
   /************************** EXPORT ***************************************/
   /*************************************************************************/

   /**
    * Export des prélèvements.
    */
   public void exportPrelevements(){
      // création de la feuille excell
      final HSSFWorkbook wb = ManagerLocator.getExportUtils().createExcellWorkBook("Prelevements");
      final HSSFSheet sheet = wb.getSheetAt(0);

      // ajout des entêtes du ficher
      int nbRow = addEnteteForExportFile(sheet, "export.prelevements", banques);
      // Récupération des entêtes des colonnes pour le prlvt
      List<String> entetes = new ArrayList<>();
      entetes = addEnteteForPrelevements(entetes);
      // récupération des annotations des prlvts
      Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      List<ChampAnnotation> casPrlvt = new ArrayList<>();

      if(banques.size() == 1){
         casPrlvt = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPrlvt.size(); i++){
         entetes.add(casPrlvt.get(i).getNom());
      }
      // ajout des entetes pour les patients
      entetes = addEnteteForPatients(entetes);
      // récupération des annotations des patients
      entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      List<ChampAnnotation> casPatient = new ArrayList<>();

      if(banques.size() == 1){
         casPatient = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPatient.size(); i++){
         entetes.add(casPatient.get(i).getNom());
      }
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
      nbRow++;

      int pourcentage = 0;
      try{
         // pour chaque prlvt sélectionné
         for(int i = 0; i < objs.size(); i++){
            final int tmp = (i * 100) / objs.size();
            // si le pourcentage doit etre mis a jour
            if(pourcentage != tmp){
               pourcentage = tmp;
            }
            final Prelevement prlvt = (Prelevement) objs.get(i);
            // ajout de ses données dans une nouvelle ligne
            final HSSFRow row = sheet.createRow(i + nbRow);
            ManagerLocator.getExportUtils().addPrelevementData(row, wb, 0, prlvt, casPrlvt, isExportAnonyme, user);

            // si le prlvt a un patient
            if(prlvt.getMaladie() != null){
               // ajout des données du patient
               final int idxCell = row.getLastCellNum();
               ManagerLocator.getExportUtils().addMaladieData(row, wb, idxCell, prlvt.getMaladie(), casPatient, isExportAnonyme,
                  null);
            }
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.prelevements.filename", new String[] {date}));
         sb.append(".xls");
         downloadExportFileXls(wb, sb.toString(), desktop);

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }finally{
         desktop.enableServerPush(false);
      }
   }

   /**
    * Exporte les patients.
    */
   public void exportPatients(){
      // création de la feuille excell
      final HSSFWorkbook wb = ManagerLocator.getExportUtils().createExcellWorkBook("Patients");
      final HSSFSheet sheet = wb.getSheetAt(0);

      // ajout des entêtes du ficher
      int nbRow = addEnteteForExportFile(sheet, "export.patients", banques);
      // Récupération des entêtes des colonnes pour le patient
      List<String> entetes = new ArrayList<>();
      entetes = addEnteteForPatients(entetes);
      // récupération des annotations des patients
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      List<ChampAnnotation> cas = new ArrayList<>();

      if(banques.size() == 1){
         cas = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < cas.size(); i++){
         entetes.add(cas.get(i).getNom());
      }
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
      nbRow++;

      int pourcentage = 0;
      try{
         // pour chaque patient sélectionné
         for(int i = 0; i < objs.size(); i++){
            final int tmp = (i * 100) / objs.size();
            // si le pourcentage doit etre mis a jour
            if(pourcentage != tmp){
               pourcentage = tmp;
               changeWaitMessage(pourcentage);
            }
            final Patient patient = (Patient) objs.get(i);
            final List<Maladie> maladies = ManagerLocator.getMaladieManager().findAllByPatientManager(patient);
            for(int j = 0; j < maladies.size(); j++){
               // ajout de ses données dans une nouvelle ligne
               final HSSFRow row = sheet.createRow(nbRow);
               ManagerLocator.getExportUtils().addMaladieData(row, wb, 0, maladies.get(j), cas, isExportAnonyme, user);
               nbRow++;
            }
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.patients.filename", new String[] {date}));
         sb.append(".xls");

         downloadExportFileXls(wb, sb.toString(), desktop);

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }finally{
         desktop.enableServerPush(false);
      }
   }

   /**
    * Export des échantillons.
    */
   public void exportEchantillons(){
      try{
         HSSFWorkbook wb = null;
         // création de la feuille excell
         wb = ManagerLocator.getExportUtils().createExcellWorkBook("Echantillons");
         final HSSFSheet sheet = wb.getSheetAt(0);

         // ajout des entêtes du ficher
         int nbRow = addEnteteForExportFile(sheet, "export.echantillons", banques);
         // Récupération des entêtes des colonnes pour les échantillons
         List<String> entetes = new ArrayList<>();
         entetes = addEnteteForEchantillons(entetes);
         // récupération des annotations des échantillons
         Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
         List<ChampAnnotation> casEchan = new ArrayList<>();

         if(banques.size() == 1){
            casEchan = getAnnotationsByBanquesAndEntite(banques, entite);
         }
         // ajout du nom des annotations aux entêtes de colonnes
         for(int i = 0; i < casEchan.size(); i++){
            entetes.add(casEchan.get(i).getNom());
         }

         // Récupération des entêtes des colonnes pour les prlvts
         entetes = addEnteteForPrelevements(entetes);
         // récupération des annotations des prlvts
         entite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
         List<ChampAnnotation> casPrlvt = new ArrayList<>();
         if(banques.size() == 1){
            casPrlvt = getAnnotationsByBanquesAndEntite(banques, entite);
         }
         // ajout du nom des annotations aux entêtes de colonnes
         for(int i = 0; i < casPrlvt.size(); i++){
            entetes.add(casPrlvt.get(i).getNom());
         }

         // ajout des entetes pour les patients
         entetes = addEnteteForPatients(entetes);
         // récupération des annotations des patients
         entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
         List<ChampAnnotation> casPatient = new ArrayList<>();
         if(banques.size() == 1){
            casPatient = getAnnotationsByBanquesAndEntite(banques, entite);
         }
         // ajout du nom des annotations aux entêtes de colonnes
         for(int i = 0; i < casPatient.size(); i++){
            entetes.add(casPatient.get(i).getNom());
         }

         ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
         nbRow++;

         int pourcentage = 0;
         // pour chaque échantillon sélectionné
         for(int i = 0; i < objs.size(); i++){
            final int tmp = (i * 100) / objs.size();
            // si le pourcentage doit etre mis a jour
            if(pourcentage != tmp){
               pourcentage = tmp;
            }
            final Echantillon echan = (Echantillon) objs.get(i);
            // ajout de ses données dans une nouvelle ligne
            final HSSFRow row = sheet.createRow(i + nbRow);
            ManagerLocator.getExportUtils().addEchantillonData(row, wb, 0, echan, casEchan, isExportAnonyme, user);

            final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(echan);

            // si l'échantillon a un prlvt parent
            if(prlvt != null){
               int idxCell = row.getLastCellNum();
               ManagerLocator.getExportUtils().addPrelevementData(row, wb, idxCell, prlvt, casPrlvt, isExportAnonyme, null);

               // si le prlvt a un patient
               if(prlvt.getMaladie() != null){
                  // ajout des données du patient
                  idxCell = row.getLastCellNum();
                  ManagerLocator.getExportUtils().addMaladieData(row, wb, idxCell, prlvt.getMaladie(), casPatient,
                     isExportAnonyme, null);
               }
            }
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.echantillons.filename", new String[] {date}));

         sb.append(".xls");

         downloadExportFileXls(wb, sb.toString(), desktop);

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }
   }

   public void exportEchantillons(final Catalogue cat){

      int pourcentage = 0;
      int nbRow = 0;

      Object wb = null;
      HSSFSheet sheet = null;

      ByteArrayOutputStream bouf = null;

      try{

         ExportCatalogueManager manager = null;
         if(cat.getNom().equals("INCa")){
            // création de la feuille excel
            wb = ManagerLocator.getExportUtils().createExcellWorkBook("Echantillons" + cat.getNom());
            sheet = ((HSSFWorkbook) wb).getSheetAt(0);

            manager = ManagerLocator.getExportINCaManager();

            // entetes
            ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, manager.getHeaders());
            nbRow++;

         }else if(cat.getNom().equals("TVGSO")){
            // creation du bufferedWriter
            bouf = new ByteArrayOutputStream();
            wb = new BufferedWriter(new OutputStreamWriter(bouf));

            manager = ManagerLocator.getExportTVGSOManager();

            // entetes
            ManagerLocator.getExportUtils().addDataToRowCSV((BufferedWriter) wb, 0, manager.getHeaders(), "|", "|\n");

         }

         if(manager == null){
            throw new RuntimeException("catalogue manager not found");
         }

         Connection conn = null;
         try{
            conn = ManagerLocator.getTxManager().getDataSource().getConnection();
            // pour chaque échantillon sélectionné
            for(int i = 0; i < objs.size(); i++){
               final int tmp = (i * 100) / objs.size();
               // si le pourcentage doit etre mis a jour
               if(pourcentage != tmp){
                  pourcentage = tmp;
                  changeWaitMessage(pourcentage);
               }
               final Echantillon echan = (Echantillon) objs.get(i);
               // ajout de ses données dans une nouvelle ligne
               // HSSFRow row = sheet.createRow(i + nbRow);
               if(cat.getNom().equals("INCa")){
                  manager.addExportDataIntoRow(conn, sheet, nbRow, echan, cat, user);
                  nbRow++;
               }else if(cat.getNom().equals("TVGSO")){
                  manager.addExportDataIntoRow(conn, wb, nbRow, echan, cat, user);
               }

            }
            conn.close();
         }catch(final Exception e){
            log.error(e.getMessage());
            log.error(e);
         }finally{
            if(conn != null){
               try{
                  conn.close();
               }catch(final SQLException e){
                  conn = null;
               }
            }
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.echantillons.filename", new String[] {date}) + "_" + cat.getNom());

         if(cat.getNom().equals("INCa")){
            sb.append(".xls");
            downloadExportFileXls((HSSFWorkbook) wb, sb.toString(), desktop);
         }else if(cat.getNom().equals("TVGSO")){
            sb.append(".csv");
            ((BufferedWriter) wb).flush();
            downloadExportFileCsv(bouf, sb.toString(), desktop);
            ((BufferedWriter) wb).close();
            if(null != bouf){
               bouf.close();
            }
         }

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }catch(final IOException ioe){
         log.error(ioe);
      }catch(final Exception e){
         log.error(e);
      }finally{
         desktop.enableServerPush(false);
         if(bouf != null){
            try{
               bouf.close();
            }catch(final Exception e){
               log.error(e);
               bouf = null;
               throw new RuntimeException(e);
            }
         }
         if(wb != null && wb instanceof BufferedWriter){
            try{
               ((BufferedWriter) wb).close();
            }catch(final Exception e){
               log.error(e);
               wb = null;
               throw new RuntimeException(e);
            }
         }
      }
   }

   /**
    * Export des produits dérivés.
    */
   public void exportProdDerives(){
      // création de la feuille excell
      final HSSFWorkbook wb = ManagerLocator.getExportUtils().createExcellWorkBook("ProdDerives");
      final HSSFSheet sheet = wb.getSheetAt(0);

      // ajout des entêtes du ficher
      int nbRow = addEnteteForExportFile(sheet, "export.prodDerives", banques);

      // Récupération des entêtes des colonnes pour les dérivés
      List<String> entetes = new ArrayList<>();
      entetes = addEnteteForProdDerives(entetes);
      // récupération des annotations des dérivés
      Entite entite = ManagerLocator.getEntiteManager().findByNomManager("ProdDerive").get(0);
      List<ChampAnnotation> casDerive = new ArrayList<>();
      if(banques.size() == 1){
         casDerive = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casDerive.size(); i++){
         entetes.add(casDerive.get(i).getNom());
      }

      // Récupération des entêtes des colonnes pour les échantillons
      entetes = addEnteteForEchantillons(entetes);
      // récupération des annotations des échantillons
      entite = ManagerLocator.getEntiteManager().findByNomManager("Echantillon").get(0);
      List<ChampAnnotation> casEchan = new ArrayList<>();
      if(banques.size() == 1){
         casEchan = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casEchan.size(); i++){
         entetes.add(casEchan.get(i).getNom());
      }
      final int nbColsEchan = 31 + casEchan.size();

      // Récupération des entêtes des colonnes pour les prlvts
      entetes = addEnteteForPrelevements(entetes);
      // récupération des annotations des prlvts
      entite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      List<ChampAnnotation> casPrlvt = new ArrayList<>();
      if(banques.size() == 1){
         casPrlvt = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPrlvt.size(); i++){
         entetes.add(casPrlvt.get(i).getNom());
      }

      // ajout des entetes pour les patients
      entetes = addEnteteForPatients(entetes);
      // récupération des annotations des patients
      entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      List<ChampAnnotation> casPatient = new ArrayList<>();
      if(banques.size() == 1){
         casPatient = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPatient.size(); i++){
         entetes.add(casPatient.get(i).getNom());
      }

      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
      nbRow++;

      int pourcentage = 0;
      try{
         // pour chaque dérivé sélectionné
         for(int i = 0; i < objs.size(); i++){
            final int tmp = (i * 100) / objs.size();
            // si le pourcentage doit etre mis a jour
            if(pourcentage != tmp){
               pourcentage = tmp;
               changeWaitMessage(pourcentage);
            }
            final ProdDerive derive = (ProdDerive) objs.get(i);
            // ajout de ses données dans une nouvelle ligne
            final HSSFRow row = sheet.createRow(i + nbRow);
            ManagerLocator.getExportUtils().addProdDeriveData(row, wb, 0, derive, casDerive, isExportAnonyme, user);

            // si on connait le parent du dérivé
            if(derive.getTransformation() != null){
               // si le parent est un échantillon
               if(derive.getTransformation().getEntite().getNom().equals("Echantillon")){
                  // on récupère l'échantillon
                  final Echantillon parentEchantillon =
                     (Echantillon) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(
                        derive.getTransformation().getEntite(), derive.getTransformation().getObjetId());

                  // ajout des données de l'échantillon
                  int idxCell = row.getLastCellNum();
                  ManagerLocator.getExportUtils().addEchantillonData(row, wb, idxCell, parentEchantillon, casEchan,
                     isExportAnonyme, null);

                  final Prelevement prlvt = ManagerLocator.getEchantillonManager().getPrelevementManager(parentEchantillon);

                  // si l'échantillon a un prlvt parent
                  if(prlvt != null){
                     idxCell = row.getLastCellNum();
                     ManagerLocator.getExportUtils().addPrelevementData(row, wb, idxCell, prlvt, casPrlvt, isExportAnonyme, null);

                     // si le prlvt a un patient
                     if(prlvt.getMaladie() != null){
                        // ajout des données du patient
                        idxCell = row.getLastCellNum();
                        ManagerLocator.getExportUtils().addMaladieData(row, wb, idxCell, prlvt.getMaladie(), casPatient,
                           isExportAnonyme, null);
                     }
                  }
                  // si le parent est un prélèvement
               }else if(derive.getTransformation().getEntite().getNom().equals("Prelevement")){
                  // on récupère le prlvt
                  final Prelevement parentPrlvt = (Prelevement) ManagerLocator.getEntiteManager().findObjectByEntiteAndIdManager(
                     derive.getTransformation().getEntite(), derive.getTransformation().getObjetId());

                  // on saute la partie concernant les échantillons
                  // pour ajouter les données du prlvt
                  int idxCell = row.getLastCellNum() + nbColsEchan;
                  ManagerLocator.getExportUtils().addPrelevementData(row, wb, idxCell, parentPrlvt, casPrlvt, isExportAnonyme,
                     null);

                  // si le prlvt a un patient
                  if(parentPrlvt.getMaladie() != null){
                     // ajout des données du patient
                     idxCell = row.getLastCellNum();
                     ManagerLocator.getExportUtils().addMaladieData(row, wb, idxCell, parentPrlvt.getMaladie(), casPatient,
                        isExportAnonyme, null);
                  }
               }
            }
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.prodDerives.filename", new String[] {date}));
         sb.append(".xls");
         downloadExportFileXls(wb, sb.toString(), desktop);

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }finally{
         desktop.enableServerPush(false);
      }
   }

   /**
    * Export des cessions.
    */
   public void exportCessions(){
      // création de la feuille excell
      final HSSFWorkbook wb = ManagerLocator.getExportUtils().createExcellWorkBook("Cessions");
      final HSSFSheet sheet = wb.getSheetAt(0);

      // ajout des entêtes du ficher
      int nbRow = addEnteteForExportFile(sheet, "export.cessions", banques);
      // Récupération des entêtes des colonnes pour la cession
      List<String> entetes = new ArrayList<>();
      entetes = addEnteteForCessions(entetes);
      // récupération des annotations des cessions
      final Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Cession").get(0);
      List<ChampAnnotation> cas = new ArrayList<>();
      if(banques.size() == 1){
         cas = getAnnotationsByBanquesAndEntite(banques, entite);
      }
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < cas.size(); i++){
         entetes.add(cas.get(i).getNom());
      }
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
      nbRow++;

      int pourcentage = 0;
      try{
         // pour chaque cession sélectionnée
         for(int i = 0; i < objs.size(); i++){
            final int tmp = (i * 100) / objs.size();
            // si le pourcentage doit etre mis a jour
            if(pourcentage != tmp){
               pourcentage = tmp;
               changeWaitMessage(pourcentage);
            }
            final Cession cession = (Cession) objs.get(i);
            // ajout de ses données dans une nouvelle ligne
            final HSSFRow row = sheet.createRow(i + nbRow);
            ManagerLocator.getExportUtils().addCessionData(row, wb, 0, cession, cas, user);
         }
         Executions.activate(desktop);
         Clients.clearBusy();

         // download du fichier excell
         final StringBuffer sb = new StringBuffer();
         final Calendar cal = Calendar.getInstance();
         final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
         sb.append(ObjectTypesFormatters.getLabel("export.cessions.filename", new String[] {date}));
         sb.append(".xls");
         downloadExportFileXls(wb, sb.toString(), desktop);

         Executions.deactivate(desktop);
      }catch(final InterruptedException ex){
         log.error(ex);
      }finally{
         desktop.enableServerPush(false);
      }
   }

   /*************************************************************************/
   /********************* METHODES UTILITAIRES ******************************/
   /*************************************************************************/

   /**
    * Modifie le message d'attente affiché.
    * @param pourcentage
    * @throws DesktopUnavailableException
    * @throws InterruptedException
    */
   public void changeWaitMessage(final int pourcentage) throws InterruptedException{
      // activation du serveur push
      Executions.activate(desktop);
      final StringBuffer sb = new StringBuffer();
      // modification du message
      sb.append(ObjectTypesFormatters.getLabel("export.wait.message", new String[] {String.valueOf(pourcentage)}));
      Clients.showBusy(sb.toString());
      Executions.deactivate(desktop);
   }

   /**
    * Gère le download d'un fichier d'export xls.
    */
   public static void downloadExportFileXls(final HSSFWorkbook wb, final String fileName, final Desktop desktop){
      ByteArrayOutputStream out = null;

      try{
         out = new ByteArrayOutputStream();
         wb.write(out);

         final AMedia media = new AMedia(fileName, "xls", "application/vnd.ms-excel", out.toByteArray());
         FileDownloadTumo.save(media, desktop);
      }catch(final Exception e){
         log.error(e);
      }finally{
         if(out != null){
            try{
               out.close();
            }catch(final IOException e){
               out = null;
            }
         }
      }
   }

   /**
    * Ajoute les entêtes du fichier d'export.
    * @param sheet Sheet du fichier excell.
    * @param key Clé pour afficher la 1ère ligne.
    * @return Indice de la ligne.
    */
   public static int addEnteteForExportFile(final HSSFSheet sheet, final String key, final List<Banque> banques){
      final List<String> tmp = new ArrayList<>();
      // Ajoute du titre du fichier
      tmp.add(Labels.getLabel(key));
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 0, tmp);
      // saut d'une ligne
      tmp.clear();
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 1, tmp);

      int nbRow;
      // si une seule banque de sélectionnée
      if(banques.size() == 1){
         // ajout du nom de la banque
         tmp.clear();
         tmp.add(ObjectTypesFormatters.getLabel("export.collection", new String[] {banques.get(0).getNom()}));
         ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 2, tmp);
         nbRow = 3;
      }else{
         nbRow = 2;
      }

      return nbRow;
   }

   /**
    * Récupère la liste des champs d'annotations.
    * @param banques
    * @param entite
    * @return
    */
   public static List<ChampAnnotation> getAnnotationsByBanquesAndEntite(final List<Banque> banques, final Entite entite){
      final List<ChampAnnotation> cas = new ArrayList<>();

      for(int i = 0; i < banques.size(); i++){
         final List<TableAnnotation> tas =
            ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(entite, banques.get(i));

         for(int j = 0; j < tas.size(); j++){
            final Iterator<ChampAnnotation> it =
               ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(tas.get(j)).iterator();

            while(it.hasNext()){
               final ChampAnnotation ca = it.next();
               if(!cas.contains(ca)){
                  cas.add(ca);
               }
            }
         }
      }

      return cas;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un patient.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public static List<String> addEnteteForPatients(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Maladie.MaladieId"));
      entetes.add(Labels.getLabel("Champ.Maladie.Libelle"));
      entetes.add(Labels.getLabel("Champ.Maladie.Code"));
      entetes.add(Labels.getLabel("Champ.Maladie.DateDebut"));
      entetes.add(Labels.getLabel("Champ.Maladie.DateDiagnostic"));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"3"}));
      entetes.add(Labels.getLabel("Champ.Patient.PatientId"));
      entetes.add(Labels.getLabel("Champ.Patient.Nip"));
      entetes.add(Labels.getLabel("Champ.Patient.NomNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.Nom"));
      entetes.add(Labels.getLabel("Champ.Patient.Prenom"));
      entetes.add(Labels.getLabel("Champ.Patient.DateNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.Sexe"));
      entetes.add(Labels.getLabel("Champ.Patient.VilleNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.PaysNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.PatientEtat"));
      entetes.add(Labels.getLabel("Champ.Patient.DateEtat"));
      entetes.add(Labels.getLabel("Champ.Patient.DateDeces"));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"3"}));
      entetes.add(Labels.getLabel("Champ.Echantillon.Organe"));
      entetes.add(Labels.getLabel("patient.nbPrelevements"));
      entetes.add(Labels.getLabel("patient.date.creation"));
      entetes.add(Labels.getLabel("patient.utilisateur.creation"));

      return entetes;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un prlvt.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public static List<String> addEnteteForPrelevements(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Prelevement.PrelevementId"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Banque"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Code"));
      entetes.add(Labels.getLabel("Champ.Prelevement.NumeroLabo"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Nature"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DatePrelevement"));
      entetes.add(Labels.getLabel("Champ.Prelevement.PrelevementType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Sterile"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Risques"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConformeArrivee"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConformeArrivee.Raison"));
      entetes.add(Labels.getLabel("prelevement.etablissement"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ServicePreleveur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Preleveur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditNbr"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditMilieu"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConsentType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConsentDate"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DateDepart"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Transporteur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.TransportTemp"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DateArrivee"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Operateur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Quantite"));
      entetes.add(Labels.getLabel("Champ.Prelevement.PatientNda"));
      entetes.add(Labels.getLabel("general.diagnostic"));
      entetes.add(Labels.getLabel("prelevement.nb.total.echantillons"));
      entetes.add(Labels.getLabel("prelevement.nb.echantillons.restants"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Stockes"));
      entetes.add(Labels.getLabel("prelevement.age"));
      entetes.add(Labels.getLabel("prelevement.nbProdDerives"));
      entetes.add(Labels.getLabel("prelevement.date.creation"));
      entetes.add(Labels.getLabel("prelevement.utilisateur.creation"));

      return entetes;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un échantillon.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public List<String> addEnteteForEchantillons(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Echantillon.EchantillonId"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Banque"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Code"));
      entetes.add(Labels.getLabel("Champ.Echantillon.EchantillonType"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Quantite"));
      entetes.add(Labels.getLabel("Champ.Echantillon.QuantiteInit"));
      entetes.add(Labels.getLabel("Champ.Echantillon.DateStock"));
      entetes.add(Labels.getLabel("Champ.Echantillon.DelaiCgl"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Collaborateur"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Emplacement"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ObjetStatut"));
      entetes.add(Labels.getLabel("Champ.Echantillon.EchanQualite"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ModePrepa"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Sterile"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ConformeTraitement"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ConformeTraitement.Raison"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ConformeCession"));
      entetes.add(Labels.getLabel("Champ.Echantillon.ConformeCession.Raison"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Tumoral"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Lateralite"));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.organe.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.organe.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.organe.numero", new String[] {"3"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.codeAssigne.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.codeAssigne.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.codeAssigne.numero", new String[] {"3"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.codeAssigne.numero", new String[] {"4"}));
      entetes.add(ObjectTypesFormatters.getLabel("echantillon.codeAssigne.numero", new String[] {"5"}));
      entetes.add(Labels.getLabel("derives.nb"));
      entetes.add(Labels.getLabel("echantillon.date.creation"));
      entetes.add(Labels.getLabel("echantillon.utilisateur.creation"));

      return entetes;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un dérivé.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public List<String> addEnteteForProdDerives(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.ProdDerive.ProdDeriveId"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Banque"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Code"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ProdType"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.DateTransformation"));
      entetes.add(Labels.getLabel("ficheProdDerive.transformation.quantité"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.CodeLabo"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Volume"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.VolumeInit"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Conc"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Quantite"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.QuantiteInit"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.DateStock"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ModePrepaDerive"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ProdQualite"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Collaborateur"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.Emplacement"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ObjetStatut"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ConformeTraitement"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ConformeTraitement.Raison"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ConformeCession"));
      entetes.add(Labels.getLabel("Champ.ProdDerive.ConformeCession.Raison"));
      entetes.add(Labels.getLabel("derives.nb"));
      entetes.add(Labels.getLabel("prodDerive.date.creation"));
      entetes.add(Labels.getLabel("prodDerive.utilisateur.creation"));

      return entetes;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'une cession.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public List<String> addEnteteForCessions(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Cession.CessionId"));
      entetes.add(Labels.getLabel("Champ.Cession.Banque"));
      entetes.add(Labels.getLabel("Champ.Cession.Numero"));
      entetes.add(Labels.getLabel("Champ.Cession.CessionType"));
      entetes.add(Labels.getLabel("cession.echantillons"));
      entetes.add(Labels.getLabel("echantillons.nb"));
      entetes.add(Labels.getLabel("cession.prodDerive"));
      entetes.add(Labels.getLabel("derives.nb"));
      entetes.add(Labels.getLabel("Champ.Cession.Demandeur"));
      entetes.add(Labels.getLabel("Champ.Cession.DemandeDate"));
      entetes.add(Labels.getLabel("Champ.Cession.Contrat"));
      entetes.add(Labels.getLabel("Champ.Cession.EtudeTitre"));
      entetes.add(Labels.getLabel("Champ.Cession.CessionExamen"));
      entetes.add(Labels.getLabel("Champ.Cession.DestructionMotif"));
      entetes.add(Labels.getLabel("Champ.Cession.Description"));
      entetes.add(Labels.getLabel("cession.etablissement"));
      entetes.add(Labels.getLabel("Champ.Cession.ServiceDest"));
      entetes.add(Labels.getLabel("Champ.Cession.Destinataire"));
      entetes.add(Labels.getLabel("Champ.Cession.ValidationDate"));
      entetes.add(Labels.getLabel("Champ.Cession.DestructionDate"));
      entetes.add(Labels.getLabel("Champ.Cession.CessionStatut"));
      entetes.add(Labels.getLabel("Champ.Cession.Executant"));
      entetes.add(Labels.getLabel("Champ.Cession.DepartDate"));
      entetes.add(Labels.getLabel("Champ.Cession.ArriveeDate"));
      entetes.add(Labels.getLabel("Champ.Cession.Transporteur"));
      entetes.add(Labels.getLabel("Champ.Cession.Temperature"));
      entetes.add(Labels.getLabel("Champ.Cession.Observations"));
      entetes.add(Labels.getLabel("cession.date.creation"));
      entetes.add(Labels.getLabel("cession.utilisateur.creation"));

      return entetes;
   }

   /**
    * Prepare le download d'un fichier d'export au format csv, donc
    * à partir d'un ByteArrayOutputStream.
    * @param buf ByteArrayOutputStream
    * @param filename
    * @param desktop2
    */
   private void downloadExportFileCsv(final ByteArrayOutputStream out, final String filename, final Desktop desktop2){

      final AMedia media = new AMedia(filename, "csv", "text/csv", out.toByteArray());
      FileDownloadTumo.save(media, desktop);

   }
}
