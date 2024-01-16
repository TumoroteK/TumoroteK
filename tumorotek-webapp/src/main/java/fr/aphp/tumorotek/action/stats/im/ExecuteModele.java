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
package fr.aphp.tumorotek.action.stats.im;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.stats.im.export.ExportToExcel;
import fr.aphp.tumorotek.action.stats.im.export.ValueToExport;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.utils.Utils;

public class ExecuteModele
{

   protected static Log log = LogFactory.getLog(ExportToExcel.class);

   private SModele model;

   private Date date_debut;

   private Date date_fin;

   private static Map<Indicateur, ArrayList<ValueToExport>> dataMap = new HashMap<>();

   private static Connection connection;

   /**
    * Classe utilitaire executant les appels JDBC pour les calcules des
    * indicateurs et la transposition des résultats dans les objets
    * ValueToExport qui serviront à l'affichage dans la vue et la production
    * des fichiers tabulés. Date: 12/05/2015
    *
    * @author Julien HUSSON, Marc DESCHAMPS, Mathieu BARTHELEMY
    * @version 2.1
    *
    */
   public ExecuteModele(){}

   public ExecuteModele(final SModele modele, final Date startingDate, final Date endingDate,
      final Map<Indicateur, ArrayList<ValueToExport>> dM){
      model = modele;
      date_debut = startingDate;
      date_fin = endingDate;
      dataMap = dM;
   }

   public void start(){
      try{
         dataMap.clear();
         initJDBC();
         loadCalling(ManagerLocator.getIndicateurManager().findBySModeleManager(model),
            new ArrayList<>(ManagerLocator.getSModeleManager().getBanquesManager(model)), date_debut, date_fin);
         // printDataMap(); debug only

      }catch(final SQLException e){
         log.error(e);
         e.printStackTrace();
         throw new RuntimeException(e.getMessage());
      }finally{
         if(connection != null){
            try{
               connection.close();
            }catch(final SQLException e){
               log.error(e);
            }
         }
      }
   }

   private void initJDBC(){

      // if MySQL maybe add zeroDateTimeBehavior=convertToNull
      final String dbUrl = Utils.getDatabaseURL();
      final String dbClass = Utils.getDriverClass();
      final String username = Utils.getUsernameDB();
      final String password = Utils.getPasswordDB();

      try{
         // Connection to database
         final java.util.Properties props = new java.util.Properties();
         props.put("user", username);
         props.put("password", password);

         Class.forName(dbClass);

         connection = DriverManager.getConnection(dbUrl, props);
         connection.setAutoCommit(false);

         // Compute Time
         long startTime = System.nanoTime();
         startTime = System.nanoTime();
         long endTime = System.nanoTime();

         endTime = System.nanoTime();
         log.debug("Total elapsed time in modele " + model.getNom() + " execution :" + ((endTime - startTime) / 1000000000.0));
      }catch(final Exception e){
         log.error(e);
      }
   }

   private void loadCalling(final List<Indicateur> indicateurs, final List<Banque> banques, final Date sD, final Date eD)
      throws SQLException{

      for(final Indicateur idc : indicateurs){
         // currentCall = s.getCallingProcedure();
         dataMap.put(idc, new ArrayList<ValueToExport>());
         call(idc, banques, sD, eD);
      }
   }

   private void call(final Indicateur st, final List<Banque> banques, final Date sD, final Date eD) throws SQLException{

      final String query = "{call stats_TER_(?,?,?,?)}";
      final java.sql.PreparedStatement call = connection.prepareCall(query);

      call.setString(1, st.getCallingProcedure());
      call.setDate(2, new java.sql.Date(sD.getTime()));
      call.setDate(3, new java.sql.Date(eD.getTime()));

      call.setInt(4, model.getSmodeleId());

      callExecute(call, st);

      call.close();
      connection.commit();
      printDataMap();
   }

   private void callExecute(final PreparedStatement call, final Indicateur st) throws SQLException{
      if(call.execute()){
         log.debug("Call to string : " + call.toString());
         ResultSet rSet = null;
         try{
            rSet = call.getResultSet();

            while(rSet.next()){

               Number f = rSet.getFloat(2);
               if(rSet.wasNull()){
                  f = null;
               }

               dataMap.get(st).add(new ValueToExport(rSet.getInt(1), f,

                  rSet.getInt(3), rSet.getInt(4)));
            }
         }catch(final SQLException e){
            log.error(e);
            throw e;
         }finally{
            if(rSet != null){
               rSet.close();
            }
         }

      }
   }

   private void printDataMap(){
      for(final Entry<Indicateur, ArrayList<ValueToExport>> e : dataMap.entrySet()){
         for(final ValueToExport o : e.getValue()){
            log.debug(e.getKey() + " : " + o.toString());
         }
      }
   }

   public static Map<Indicateur, ArrayList<ValueToExport>> getDataMap(){
      return dataMap;
   }

   public static int getDiffYears(final Date first, final Date last){
      final Calendar a = getCalendar(first);
      final Calendar b = getCalendar(last);
      final int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);

      return diff;
   }

   public List<String> getYears(final Date first, final Date last){
      final List<String> years = new ArrayList<>();
      final Calendar a = getCalendar(first);
      final Calendar b = getCalendar(last);

      final int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
      int year = a.get(Calendar.YEAR);

      years.add(Integer.toString(year));
      for(int i = 0; i < diff; i++){
         year += 1;
         years.add(Integer.toString(year));
      }

      return years;
   }

   public static Calendar getCalendar(final Date date){
      final Calendar cal = Calendar.getInstance(Locale.US);
      cal.setTime(date);
      return cal;
   }
}
