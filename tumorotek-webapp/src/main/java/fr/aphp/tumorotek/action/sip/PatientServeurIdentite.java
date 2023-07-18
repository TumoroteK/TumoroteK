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
package fr.aphp.tumorotek.action.sip;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.joran.spi.ActionException;
import fr.aphp.tumorotek.model.coeur.patient.Patient;

public class PatientServeurIdentite implements Sip
{

   private final Logger log = LoggerFactory.getLogger(PatientServeurIdentite.class);

   ServeurIdentitesFileBean serveurIdentitesFileBean;

   public String DBMS;

   public String HOST;

   public String DRIVER;

   public String DATABASE;

   public String LOGIN;

   public String PASSWORD;

   public String TABLE_PATIENT;

   public String NIP;

   public String NOM;

   public String NOM_PATRON;

   public String PRENOM;

   public String SEXE;

   public String SEXE_TYPE;

   public String SEXE_FEMME;

   public String SEXE_HOMME;

   public String DATE_NAISS;

   public String NIP_TYPE;

   public String NUM_DOSSIER;

   public final String LIKE = "LIKE";

   public final String EQUAL = "=";

   private Connection connection = null;

   private PreparedStatement prepaStmt = null;

   //private Statement stmt = null;
   private ResultSet resultSet = null;

   /**
    * Constructor for PatientServeurIdentite.
    */
   public PatientServeurIdentite(final String identificationPlateforme){
      super();
      serveurIdentitesFileBean = LoadPropertiesServeurIdentitesFile.getServeurIdentitesFileBean(identificationPlateforme);

      DBMS = serveurIdentitesFileBean.getDbms();
      HOST = serveurIdentitesFileBean.getHost();
      DRIVER = serveurIdentitesFileBean.getDriver();
      DATABASE = serveurIdentitesFileBean.getDatabase();
      LOGIN = serveurIdentitesFileBean.getLogin();
      PASSWORD = serveurIdentitesFileBean.getPassword();

      TABLE_PATIENT = serveurIdentitesFileBean.getTablePatient();
      NIP = serveurIdentitesFileBean.getNip();
      NOM = serveurIdentitesFileBean.getNom();
      NOM_PATRON = serveurIdentitesFileBean.getNomPatron();
      PRENOM = serveurIdentitesFileBean.getPrenom();
      SEXE = serveurIdentitesFileBean.getSexe();

      SEXE_TYPE = serveurIdentitesFileBean.getSexeType();
      SEXE_FEMME = serveurIdentitesFileBean.getSexeFemme();
      SEXE_HOMME = serveurIdentitesFileBean.getSexeHomme();

      DATE_NAISS = serveurIdentitesFileBean.getDateNaiss();

      NIP_TYPE = serveurIdentitesFileBean.getNipType();

      NUM_DOSSIER = serveurIdentitesFileBean.getNumDossier();
   }

   /**
    * Constructor for PatientServeurIdentite.
    * @param db
    */
   /*public PatientServeurIdentite(DbAPI db) {
   	super(db);
   }*/

   /**
    * Requête sql pour récupérer les patients du serveur d'identités.
    * @param column
    * @return String
    */
   private String openQueryPatientServeur(final String column, final String equal, Integer nbMaxPatients){

      String sql = "";
      sql = "SELECT " + TABLE_PATIENT + "." + NIP + ", " + TABLE_PATIENT + "." + NOM + ", " + TABLE_PATIENT + "." + NOM_PATRON
         + ", " + TABLE_PATIENT + "." + PRENOM + ", " + TABLE_PATIENT + "." + SEXE + ", " + TABLE_PATIENT + "." + DATE_NAISS + " "
         + "FROM " + TABLE_PATIENT + " " + "WHERE " + TABLE_PATIENT + "." + column + " " + equal + " ?";

      // si le nombre de patients à retourner n'est pas valide,
      // on le fixe à 10.
      if(nbMaxPatients == null){
         nbMaxPatients = 10;
      }

      if(nbMaxPatients > 0){
         sql = sql + " AND rownum < " + nbMaxPatients;
      }

      log.debug("Requete : {}", sql);
      return sql;
   }

   /**
    * Requête sql pour récupérer les patients du serveur d'identités.
    * @return String
    */
   private String openQueryPatientServeurNip(final Integer nbMaxPatients){
      String sql = "";
      if("ENTIER".equalsIgnoreCase(NIP_TYPE)){
         sql = openQueryPatientServeur(NIP, EQUAL, nbMaxPatients);
      }else{
         sql = openQueryPatientServeur(NIP, LIKE, nbMaxPatients);
      }
      return sql;
   }

   /**
    * Requête sql pour récupérer les patients du serveur d'identités.
    * @return String
    */
   private String openQueryPatientServeurNom(final Integer nbMaxPatients){
      final String sql = openQueryPatientServeur(NOM, LIKE, nbMaxPatients);
      return sql;
   }

   /**
    * Requête sql pour récupérer les patients du serveur d'identités.
    * @return String
    */
   private String openQueryPatientServeurNumDossier(final Integer nbMaxPatients){
      final String sql = openQueryPatientServeur(NUM_DOSSIER, LIKE, nbMaxPatients);
      return sql;
   }

   /**
    * Methode de connexion et d'accès à la base de données
    * du serveur d'identités des patients.
    * @param column
    * @param value
    * @param max
    * @see http://stackoverflow.com/questions/288828/how-to-use-a-jdbc-driver-from-an-arbitrary-location?rq=1
    */
   private List<Patient> getPatientsServeur(final String column, final String value){
      final List<Patient> listPatient = new ArrayList<>();
      try{
         // Oracle 8 load specific driver Hack
         //	URLClassLoader classLoader;
         //	classLoader = new URLClassLoader(new URL[]{new URL("jar:file:/home/mathieu2/apache-tomcat-7.0.40/conf/Catalina/localhost/sip/ojdbc14.jar!/")},
         //				this.getClass().getClassLoader());

         //	Driver driver = (Driver) Class.forName(DRIVER, true, classLoader).newInstance();
         ///	DriverManager.registerDriver(new Oracle8Driver(driver)); // register using the Delegating Driver

         //	Enumeration<Driver> dvs = DriverManager.getDrivers();
         Class.forName(DRIVER);
         connection = DriverManager.getConnection(DATABASE, LOGIN, PASSWORD);

         final InitTumoFileBean initTumoFileBean = LoadPropertiesInitTumoFile.getInitTumoFileBean();
         final Integer nbMax = initTumoFileBean.getMaxPatients();

         if("NIP".equalsIgnoreCase(column)){
            prepaStmt = connection.prepareStatement(openQueryPatientServeurNip(nbMax));
            if("ENTIER".equalsIgnoreCase(NIP_TYPE)){
               prepaStmt.setLong(1, Long.valueOf(value));
            }else{
               prepaStmt.setString(1, value);
            }
         }else if("NOM".equalsIgnoreCase(column)){
            prepaStmt = connection.prepareStatement(openQueryPatientServeurNom(nbMax));
            prepaStmt.setString(1, value);
         }else if("NUM_DOSSIER".equalsIgnoreCase(column)){
            prepaStmt = connection.prepareStatement(openQueryPatientServeurNumDossier(nbMax));
            prepaStmt.setString(1, value);
         }

         resultSet = prepaStmt.executeQuery();
         while(resultSet.next()){
            final Patient patient = getPatient();
            listPatient.add(patient);
         }
      }catch(final ClassNotFoundException cnfe){
         final String msg = "PatientServeurIdentite error " + "(Driver non chargé) ! : " + cnfe.getMessage();
         log.error(msg);
      }catch(final SQLException sqle){
         final String msg = "PatientServeurIdentite error " + "(Probleme de connexion a la base) ! : " + sqle.getMessage();
         log.error(msg);

         //		} catch (MalformedURLException e) {
         //			log.error(e.getMessage(), e);
         //		} catch (InstantiationException e) {
         //			log.error(e.getMessage(), e);
         //		} catch (IllegalAccessException e) {
         //			log.error(e.getMessage(), e);
      }finally{
         if(resultSet != null){
            try{
               resultSet.close(); //fermeture du resultset
            }catch(final Exception e){
               log.error(e.getMessage(), e); 
            }
         }
         //if(stmt!=null){
         //try{
         //stmt.close();//fermeture du statement
         //}
         //catch(Exception e){log.error(e.getMessage(), e); }
         //}
         if(prepaStmt != null){
            try{
               prepaStmt.close(); //fermeture du statement
            }catch(final Exception e){
               log.error(e.getMessage(), e); 
            }
         }
         if(connection != null){
            try{
               connection.close(); //fermeture de la connexion
            }catch(final Exception e){
               log.error(e.getMessage(), e); 
            }
         }
      }
      return listPatient;
   }

   /**
    * Methode de connexion et d'accès à la base de données
    * du serveur d'identités des patients.
    * @param nip
    * @throws ActionException
    */
   @Override
   public List<Patient> getPatientsServeurNip(final String nip){
      final List<Patient> listPatient = new ArrayList<>();
      if("ENTIER".equalsIgnoreCase(NIP_TYPE)){
         if(nip.matches("[0-9]+")){
            listPatient.addAll(getPatientsServeur("NIP", nip));
         }
      }else{
         listPatient.addAll(getPatientsServeur("NIP", nip));
      }
      return listPatient;
   }

   /**
    * Methode de connexion et d'accès à la base de
    * données du serveur d'identités des patients.
    * @param nom
    * @throws ActionException
    */
   @Override
   public List<Patient> getPatientsServeurNom(final String nom){
      final List<Patient> listPatient = getPatientsServeur("NOM", nom);
      return listPatient;
   }

   /**
    * Methode de connexion et d'accès à la base de
    * données du serveur d'identités des patients.
    * @param numDossier
    * @throws ActionException
    */
   @Override
   public List<Patient> getPatientsServeurNumDossier(final String numDossier){
      List<Patient> listPatient = new ArrayList<>();
      if(NUM_DOSSIER != null && !NUM_DOSSIER.trim().equals("")){
         listPatient = getPatientsServeur("NUM_DOSSIER", numDossier);
      }
      return listPatient;
   }

   /**
    * Retourne la valeur de la colonne (String).
    */
   public String getColumnString(final String columnName){
      try{
         if((resultSet.getString(columnName) == null) || (resultSet.getString(columnName).trim().equals(""))
            || (resultSet.getString(columnName).equalsIgnoreCase("NULL"))){
            return "";
         }else{
            return resultSet.getString(columnName);
         }
      }catch(final SQLException e){
         return "?";
      }
   }

   /**
    * Retourne la valeur de la colonne (Date).
    */
   public String getColumnDate(final String columnName){
      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      try{
         if(resultSet.getDate(columnName) == null){
            return "";
         }else{
            final Date dt = resultSet.getDate(columnName);
            return sdf.format(dt);
         }
      }catch(final SQLException e){
         return "00/00/1000";
      }
   }

   /**
    * Retourne la valeur de la colonne (int).
    */
   public int getColumnInt(final String columnName){
      try{
         return resultSet.getInt(columnName);
      }catch(final SQLException e){
         return -1;
      }
   }

   /**
    * Retourne le PatientBean.
    */
   public Patient getPatient(){
      final Patient patient = new Patient();
      if("ENTIER".equalsIgnoreCase(NIP_TYPE)){
         final InitTumoFileBean initTumoFileBean = LoadPropertiesInitTumoFile.getInitTumoFileBean();
         final int nipPatientLength = initTumoFileBean.getLongueurNip();

         final String newNipPatient = getColumnString(NIP);
         final int newNipPatientLength = newNipPatient.length();

         String prefix = "";
         if(newNipPatientLength < nipPatientLength){
            for(int i = newNipPatientLength; i < nipPatientLength; i++){
               prefix += "0";
            }
         }

         patient.setNip(prefix + newNipPatient);
      }else{
         patient.setNip(getColumnString(NIP));
      }
      patient.setNom(getColumnString(NOM));
      patient.setNomNaissance(getColumnString(NOM_PATRON));
      patient.setPrenom(getColumnString(PRENOM));

      String sexe = "";
      if("ENTIER".equalsIgnoreCase(SEXE_TYPE)){
         sexe = String.valueOf(getColumnInt(SEXE)).trim();
      }else{
         sexe = getColumnString(SEXE).trim();
      }

      if(SEXE_FEMME.equalsIgnoreCase(sexe)){
         patient.setSexe("F");
      }else if(SEXE_HOMME.equalsIgnoreCase(sexe)){
         patient.setSexe("M");
      }else{
         patient.setSexe("Ind");
      }

      java.util.Date date = null;
      try{
         date = new SimpleDateFormat("dd/MM/yyyy").parse(getColumnDate(DATE_NAISS));
      }catch(final ParseException e){
         log.error(e.getMessage(), e); 
      }
      patient.setDateNaissance(date);
      return patient;
   }
}
