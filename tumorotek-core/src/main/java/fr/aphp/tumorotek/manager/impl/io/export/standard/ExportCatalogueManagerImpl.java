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
package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.orm.jpa.JpaTransactionManager;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.export.ExportUtils;
import fr.aphp.tumorotek.manager.io.export.standard.ExportCatalogueManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ExportCatalogueManagerImpl implements ExportCatalogueManager
{

   private static Logger log = LoggerFactory.getLogger(ExportCatalogueManager.class);

   private JpaTransactionManager txManager;

   private EchantillonManager echantillonManager;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private ExportUtils exportUtils;

   private DateFormat dateFormatterINCA = new SimpleDateFormat("yyyyMMdd");

   private Echantillon echantillon;

   private Prelevement prelevement;

   private Patient patient;

   private Banque banque;

   public Echantillon getEchantillon(){
      return echantillon;
   }

   public void setEchantillon(final Echantillon e){
      this.echantillon = e;
   }

   public Prelevement getPrelevement(){
      return prelevement;
   }

   public void setPrelevement(final Prelevement p){
      this.prelevement = p;
   }

   public Patient getPatient(){
      return patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   public void setExportUtils(final ExportUtils eUtils){
      this.exportUtils = eUtils;
   }

   public ExportUtils getExportUtils(){
      return exportUtils;
   }

   public void setTxManager(final JpaTransactionManager tManager){
      this.txManager = tManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public DateFormat getDateFormatterINCA(){
      return dateFormatterINCA;
   }

   public void setDateFormatterINCA(final DateFormat df){
      this.dateFormatterINCA = df;
   }

   @Override
   public void initExportObjectsManager(){
      try{
         setPrelevement(echantillonManager.getPrelevementManager(getEchantillon()));
         setPatient(getPrelevement().getMaladie().getPatient());
         setBanque(getPrelevement().getBanque());
      }catch(final NullPointerException ne){
         throw new RuntimeException("objet manquant dans " + "la hierarchie Patient-Prelevement-Echantillon");
      }
   }

   @Override
   public List<String> getHeaders(){
      return null;
   }

   @Override
   public Map<String, String> objetExport(final Echantillon echan, final Connection conn){
      return null;
   }

   @Override
   public void exportEchansCatalogueManager(final List<Echantillon> echans, final Utilisateur utilisateur,
      final Catalogue catalogue) throws SQLException{

      if(echans != null){

         final HSSFWorkbook wb = getExportUtils().createExcellWorkBook("Export " + catalogue.getNom());
         final HSSFSheet sheet = wb.getSheetAt(0);
         int nbRow = 0;
         // entete?
         Connection conn = null;
         try{
            conn = txManager.getDataSource().getConnection();

            Echantillon e;
            HashMap<String, String> res;

            for(int i = 0; i < echans.size(); i++){
               e = echans.get(i);

               res = (HashMap<String, String>) objetExport(e, conn);
               getExportUtils().addDataToRow(sheet, 0, nbRow, new ArrayList<>(res.values()));
               nbRow++;

               CreateOrUpdateUtilities.createAssociateOperation(echantillon, operationManager,
                  operationTypeDao.findByNom("%" + catalogue.getNom()).get(0), utilisateur);
            }
            conn.close();
         }catch(final Exception e){
            log.error(e.getMessage());
            log.error("An error occurred: {}", e.toString()); 
         }finally{
            if(conn != null){
               try{
                  conn.close();
               }catch(final SQLException e){
                  conn = null;
               }
            }
         }

      }

   }

   /**
    * Methode permettant de recupere une valeur pour un item.
    * @param Connection con
    * @param requête SQL
    * @param message d'erreur
    * @param obligatoire si true
    * @param regexp
    * @param defaut
    */
   public static String fetchItemAsString(final Connection con, final String requete, final String errorMessage,
      final boolean obligatoire, final String regexp, final String defaut){
      Statement s = null;
      ResultSet rs = null;
      String item = null;

      try{
         s = con.createStatement();
         rs = s.executeQuery(requete);
         if(rs.next()){
            item = rs.getString(1);
         }
         rs.close();
         s.close();
         if(obligatoire){
            if(item == null || item.equals("")){
               if(defaut == null){
                  throw new ItemException(2, errorMessage);
               }
               return defaut;
            }
         }
         if(regexp != null && item != null){
            final Pattern p = Pattern.compile(regexp);
            final Matcher m = p.matcher(item);
            final boolean b = m.matches();
            if(!b){
               throw new ItemException(3, "Valeur inattendue " + item);
            }else if(m.groupCount() > 0){
               item = m.group(1);
            }
         }

         return item;
      }catch(final SQLException e){
         log.error("An error occurred: {}", e.toString()); 
      }finally{
         if(s != null){
            try{
               s.close();
            }catch(final SQLException e){
               s = null;
            }
         }
         if(rs != null){
            try{
               rs.close();
            }catch(final SQLException e){
               rs = null;
            }
         }
      }
      return defaut;
   }

   /**
    * Methode permettant de recupere une valeur au format Date
    * pour un item.
    * @param Connection con
    * @param requête SQL
    * @param message d'erreur
    * @param obligatoire si true
    * @param valeur par defaut si null
    */
   public static String fetchItemDate(final Connection con, final String requete, final String errorMessage,
      final boolean obligatoire, final String defaut, final DateFormat df){
      Statement s = null;
      ResultSet rs = null;
      Date item = null;

      try{
         s = con.createStatement();
         rs = s.executeQuery(requete);
         if(rs.next()){
            item = rs.getDate(1);
         }
         rs.close();
         s.close();
         if(obligatoire){
            if(item == null){
               if(defaut == null){
                  throw new ItemException(2, errorMessage);
               }
               return defaut;
            }
         }
         if(item != null){
            return df.format(item);
         }
      }catch(final SQLException sqle){
         log.error(sqle);
      }finally{
         if(s != null){
            try{
               s.close();
            }catch(final SQLException e){
               s = null;
            }
         }
         if(rs != null){
            try{
               rs.close();
            }catch(final SQLException e){
               rs = null;
            }
         }
      }
      return null;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   public Banque getBanque(){
      return banque;
   }

   @Override
   public void addExportDataIntoRow(final Connection conn, final Object obj, final int nbRow, final Echantillon echan,
      final Catalogue catalogue, final Utilisateur utilisateur){

      HashMap<String, String> res;

      res = (HashMap<String, String>) objetExport(echan, conn);
      printDataIntoRow(obj, 0, nbRow, new ArrayList<>(res.values()));
      if(!res.containsKey("Erreur")){
         CreateOrUpdateUtilities.createAssociateOperation(echantillon, operationManager,
            operationTypeDao.findByNom("%" + catalogue.getNom()).get(0), utilisateur);
      }

   }

   @Override
   public void printDataIntoRow(final Object obj, final int index, final int nbRow, final List<String> values){
      getExportUtils().addDataToRow((HSSFSheet) obj, 0, nbRow, values);
   }

}