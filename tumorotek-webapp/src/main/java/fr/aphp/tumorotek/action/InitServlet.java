/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.action;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Version;
import fr.aphp.tumorotek.param.TkParam;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Servlet first load at startup.
 * Lance les scripts de routine TumoroteK
 * Date: 09/08/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 * @since 2.0.0
 */
public class InitServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;

   private final Logger log = LoggerFactory.getLogger(InitServlet.class);

   @Override
   public void init() throws ServletException{
      super.init();

      log.info("----- TumoroteK startup routines -----");
      final Path baseDir = Paths.get(TkParam.FILESYSTEM.getValue());

      log.info(baseDir.toString());

      // Initialisation TUMOROTEK_CODES & TUMOROTEK_INTERFACAGES
      performInitialisation();

      // updates
      performUpdates();

      deleteContextFile();

      // file system
      if(baseDir != null){
         final File f = new File(baseDir.toUri());
         // crée le base directory au besoin
         if(f.exists() && f.isDirectory() && f.list().length == 0){
            log.info("----- Mise en place du base directory TumoroteK -----");
            final List<Plateforme> pfs = ManagerLocator.getPlateformeManager().findAllObjectsManager();
            Path pfDir;
            final List<Banque> banks = new ArrayList<>();
            for(final Plateforme pf : pfs){
               pfDir = Paths.get("pt_" + pf.getPlateformeId());
               banks.addAll(ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(pf, null));
               for(final Banque bank : banks){
                  final Path crAnapathPath =
                     Paths.get(baseDir.toString(), pfDir.toString(), "coll_" + bank.getBanqueId(), "cr_anapath");
                  final Path annoPath = Paths.get(baseDir.toString(), pfDir.toString(), "coll_" + bank.getBanqueId(), "anno");
                  new File(crAnapathPath.toUri()).mkdirs();
                  new File(annoPath.toUri()).mkdirs();
                  log.info("base directory de la collection {} généré", bank.getNom());
               }
               banks.clear();
            }
         }
      }

   }

   public enum ESchema
   {
      TUMOROTEK, TUMOROTEK_CODES, TUMOROTEK_INTERFACAGES
   }

   public DataSource dataSource(final ESchema eSchema){

      final DriverManagerDataSource dataSource = new DriverManagerDataSource();
      switch(eSchema){
         case TUMOROTEK_CODES:
            dataSource.setDriverClassName(TkParam.CODES_DATABASE_DRIVER.getValue());
            dataSource.setUrl(TkParam.CODES_DATABASE_URL.getValue());
            dataSource.setUsername(TkParam.CODES_DATABASE_USER.getValue());
            dataSource.setPassword(TkParam.CODES_DATABASE_PASSWORD.getValue());
            break;
         case TUMOROTEK_INTERFACAGES:
            dataSource.setDriverClassName(TkParam.INTERFACAGES_DATABASE_DRIVER.getValue());
            dataSource.setUrl(TkParam.INTERFACAGES_DATABASE_URL.getValue());
            dataSource.setUsername(TkParam.INTERFACAGES_DATABASE_USER.getValue());
            dataSource.setPassword(TkParam.INTERFACAGES_DATABASE_PASSWORD.getValue());
            break;
         default:
            dataSource.setDriverClassName(TkParam.TK_DATABASE_DRIVER.getValue());
            dataSource.setUrl(TkParam.TK_DATABASE_URL.getValue());
            dataSource.setUsername(TkParam.TK_DATABASE_USER.getValue());
            dataSource.setPassword(TkParam.TK_DATABASE_PASSWORD.getValue());
            break;
      }
      return dataSource;
   }

   /**
    * Applique la mise à jour des versions grâce à Liquibase.
    */
   public void performInitialisation(){
      log.info("----- Recherche des mises à jours database -----");

      // TUMOROTEK_CODES
      Database database = null;
      try{
         database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(ESchema.TUMOROTEK_CODES).getConnection()));
      }catch(DatabaseException | SQLException e){
         log.error(e.toString());
      }
      try{
         final Liquibase liquibase =
            new Liquibase("liquibase/changelog/db.changelog-init-codes.xml", new ClassLoaderResourceAccessor(), database);
         liquibase.update("*");
      }catch(final LiquibaseException e){
         log.error(e.toString());
      }

      // TUMOROTEK_INTERFACAGES
      try{
         database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(ESchema.TUMOROTEK_INTERFACAGES).getConnection()));
      }catch(DatabaseException | SQLException e){
         log.error(e.toString());
      }
      try{
         final Liquibase liquibase = new Liquibase("liquibase/changelog/interfacages/db.changelog-init-interfacages.xml",
            new ClassLoaderResourceAccessor(), database);
         liquibase.update("*");
      }catch(final LiquibaseException e){
         log.error(e.toString());
      }
   }

   /**
    * Applique la mise à jour des versions grâce à Liquibase.
    */
   public void performUpdates(){
      log.info("----- Recherche des mises à jours database -----");

      Database database = null;
      Database databaseIntf = null;
      try{
         database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(ESchema.TUMOROTEK).getConnection()));

         databaseIntf = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(ESchema.TUMOROTEK_INTERFACAGES).getConnection()));

      }catch(DatabaseException | SQLException e){
         log.error(e.toString());
      }
      try{
         final Liquibase liquibase =
            new Liquibase("liquibase/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
         liquibase.update("*");

         // interfacages
         final Liquibase liquibaseIntf = new Liquibase("liquibase/changelog/interfacages/db.changelog-master.xml",
            new ClassLoaderResourceAccessor(), databaseIntf);
         liquibaseIntf.update("*");

         // Pour faire un rollback
         //liquibase.rollback("2.1.4-SNAPSHOT", contexts);
      }catch(final LiquibaseException e){
         log.error(e.toString());
      }

      final Version currentVersion = ManagerLocator.getVersionManager().findByCurrentVersionManager();
      //final List<Version> allVersions = ManagerLocator.getVersionManager().findAllObjectsManager();
      final String nextVersion = ObjectTypesFormatters.getLabel("app.version", new String[] {});
      // Enregistre la version qui vient d'être installée
      if(null != currentVersion && !currentVersion.getVersion().equals(nextVersion)){
         final Version version = new Version();
         version.setVersion(nextVersion);
         version.setNomSite(currentVersion.getNomSite());
         version.setDate(new Date());
         ManagerLocator.getVersionManager().createObjectManager(version);
      }else if(null == currentVersion){
         final Version version = new Version();
         version.setVersion(nextVersion);
         version.setDate(new Date());
         ManagerLocator.getVersionManager().createObjectManager(version);
      }
   }

   public void rollback(final Context context){
      log.info("----- Rollback -----");
      Database database = null;
      try{
         database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(ESchema.TUMOROTEK).getConnection()));
      }catch(DatabaseException | SQLException e){
         log.error(e.toString());
      }

      new Liquibase("liquibase/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);

      // Pour faire un rollback
      //liquibase.rollback("2.1.4-SNAPSHOT", "*");
   }

   /**
    * Supprime le descripteur de configuration correspondant à la version déployée
    */
   private void deleteContextFile(){

      final Path confDir = Paths.get(TkParam.CONF_DIR.getValue());

      //On utlise cette manière de faire car Tomcat 7 ne sait pas récupérer la version
      //avec getClass().getPackage().getImplementationVersion()
      final Properties manifest = new Properties();
      try( InputStream is = getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF")){
         manifest.load(is);
      }catch(final IOException e){
         log.error("Impossible de lire le fichier MANIFEST.MF", e);
      }

      final String version = manifest.getProperty("Implementation-Version");

      final FilenameFilter contextFileFilter = (f, s) -> {
         return s.matches("^tumorotek##" + version + "\\.xml$");
      };

      //Recherche du fichier de configuration xml
      final File[] contextFiles = confDir.toFile().listFiles(contextFileFilter);

      if(contextFiles.length == 1){
         log.info("Suppression du descripteur de configuration {}",  contextFiles[0].getName());
         contextFiles[0].delete();
      }

   }

   @Override
   public void destroy(){
      super.destroy();
   }
}