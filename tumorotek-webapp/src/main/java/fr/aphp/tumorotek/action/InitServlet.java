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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Version;
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
 * @since 2.0
 */
public class InitServlet extends HttpServlet
{
   private static final long serialVersionUID = 1L;
   private final Log log = LogFactory.getLog(InitServlet.class);

   @Override
   public void init() throws ServletException{
      super.init();

      try{
         log.info("----- TumoroteK startup routines -----");
         final Context context = new InitialContext();
         final Path baseDir = Paths.get((String) context.lookup("java:comp/env/tk/tkFileSystem"));

         // updates
         performUpdates(context);

         // file system
         if(baseDir != null){
            final File f = new File(baseDir.toUri());
            // crée le base directory au besoin
            if(f.exists() && f.isDirectory() && f.list().length == 0){
               log.info("----- Mise en place du base directory TumoroteK -----");
               final List<Plateforme> pfs = ManagerLocator.getPlateformeManager().findAllObjectsManager();
               Path pfDir;
               final List<Banque> banks = new ArrayList<>();
               for(Plateforme pf : pfs){
                  pfDir = Paths.get("pt_" + pf.getPlateformeId());
                  banks.addAll(ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(pf, null));
                  for(Banque bank : banks){
                     Path crAnapathPath = Paths.get(baseDir.toString(), pfDir.toString(), "coll_" + bank.getBanqueId(), "cr_anapath");
                     Path annoPath = Paths.get(baseDir.toString(), pfDir.toString(), "coll_" + bank.getBanqueId(), "anno");
                     new File(crAnapathPath.toUri()).mkdirs();
                     new File(annoPath.toUri()).mkdirs();
                     log.info("base directory de la collection " + bank.getNom() + " généré");
                  }
                  banks.clear();
               }
            }
         }
      }catch(final NamingException ex){
         log.error(ex);
      }
   }

   public DataSource dataSource(final Context context) throws NamingException{
      final DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName((String) context.lookup("java:comp/env/jdbc/driverClass"));
      dataSource.setUrl((String) context.lookup("java:comp/env/jdbc/url"));
      dataSource.setUsername((String) context.lookup("java:comp/env/jdbc/user"));
      dataSource.setPassword((String) context.lookup("java:comp/env/jdbc/password"));
      return dataSource;
   }

   /**
    * Applique la mise à jour des versions grâce à Liquibase.
    *
    * @param context qui correspond entre autres aux variables définies dans tumorotek##xxx.xml
    */
   public void performUpdates(final Context context){
      log.info("----- Recherche des mises à jours database -----");
      final Version currentVersion = ManagerLocator.getVersionManager().findByCurrentVersionManager();
      //final List<Version> allVersions = ManagerLocator.getVersionManager().findAllObjectsManager();
      final String nextVersion = ObjectTypesFormatters.getLabel("app.version", new String[] {});
      //if(null != currentVersion){
         Database database = null;
         try{
            database = DatabaseFactory.getInstance()
               .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(context).getConnection()));
         }catch(DatabaseException | SQLException | NamingException e){
            log.error(e.toString());
         }
         try{
            final Liquibase liquibase =
               new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            /*final Contexts contexts = new Contexts();
            for(final Version version : allVersions){
               if(null != version.getVersion()){
                  contexts.add(version.getVersion());
               }
            }*/
            // Liquibase mettra à jour en fonction des versions précédemment installées.
            //liquibase.update(contexts);
            liquibase.update("*");

            // Pour faire un rollback
            //liquibase.rollback("2.1.4-SNAPSHOT", contexts);
         }catch(final LiquibaseException e){
            log.error(e.toString());
         }
      //}

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
            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource(context).getConnection()));
      }catch(DatabaseException | SQLException | NamingException e){
         log.error(e.toString());
      }

      new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);

      // Pour faire un rollback
      //liquibase.rollback("2.1.4-SNAPSHOT", "*");
   }

   @Override
   public void destroy(){
      super.destroy();
   }
}