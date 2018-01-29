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

import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Version;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet first load at startup.
 * Lance les scripts de routine TumoroteK
 * Date: 09/08/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1.4
 * @since 2.0
 */
public class InitServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private Log log = LogFactory.getLog(InitServlet.class);

   @Override
   public void init() throws ServletException {
      super.init();

      try {
         log.info("----- TumoroteK startup routines -----");
         Context context = new InitialContext();
         String baseDir = (String) context.lookup("java:comp/env/tk/tkFileSystem");

         // Fait un update de la table version pour être en conformité avec le nouveau système de Liquibase
         // à terme cette fonction sera bien destinée à faire le rollback
         rollback(context);

         // updates
         performUpdates(context);

         // file system
         if (baseDir != null) {
            File f = new File(baseDir);
            // crée le base directory au besoin
            if (f.exists() && f.isDirectory() && f.list().length == 0) {
               log.info("----- Mise en place du base directory TumoroteK -----");
               List<Plateforme> pfs = ManagerLocator.getPlateformeManager().findAllObjectsManager();
               String pfDir;
               List<Banque> banks = new ArrayList<Banque>();
               for (int i = 0; i < pfs.size(); i++) {
                  pfDir = "pt_" + pfs.get(i).getPlateformeId() + "/";
                  banks.addAll(ManagerLocator.getBanqueManager().findByPlateformeAndArchiveManager(pfs.get(i), null));
                  for (int j = 0; j < banks.size(); j++) {
                     new File(baseDir + pfDir + "coll_" + banks.get(j).getBanqueId() + "/cr_anapath").mkdirs();
                     new File(baseDir + pfDir + "coll_" + banks.get(j).getBanqueId() + "/anno").mkdirs();
                     log.info("base directory de la collection " + banks.get(j).getNom() + " généré");
                  }
                  banks.clear();
               }
            }
         }
      } catch (NamingException ex) {
         log.error(ex);
      }
   }

   public DataSource dataSource(Context context) throws NamingException {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
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
   public void performUpdates(Context context) {
      log.info("----- Recherche des mises à jours database -----");
      Version currentVersion = ManagerLocator.getVersionManager().findByCurrentVersionManager();
      List<Version> allVersions = ManagerLocator.getVersionManager().findAllObjectsManager();
      String nextVersion = ObjectTypesFormatters.getLabel("app.version", new String[]{});
      if (null != currentVersion) {
         Database database = null;
         try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource(context).getConnection()));
         } catch (DatabaseException | SQLException | NamingException e) {
            log.error(e.toString());
         }
         try {
            Liquibase liquibase = new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            Contexts contexts = new Contexts();
            for (Version version : allVersions) {
               if (null != version.getVersion()) {
                  contexts.add(version.getVersion());
               }
            }
            // Liquibase mettra à jour en fonction des versions précédemment installées.
            liquibase.update(contexts);

            // Pour faire un rollback
            //liquibase.rollback("2.1.4-SNAPSHOT", contexts);
         } catch (LiquibaseException e) {
            log.error(e.toString());
         }
      }

      // Enregistre la version qui vient d'être installée
      if(null != currentVersion && !currentVersion.getVersion().equals(nextVersion)){
         Version version = new Version();
         version.setVersion(nextVersion);
         version.setNomSite(currentVersion.getNomSite());
         version.setDate(new Date());
         ManagerLocator.getVersionManager().createObjectManager(version);
      }else if(null == currentVersion){
         Version version = new Version();
         version.setVersion(nextVersion);
         version.setDate(new Date());
         ManagerLocator.getVersionManager().createObjectManager(version);
      }
   }

   public void rollback(Context context) {
      log.info("----- Rollback -----");
      Database database = null;
      try {
         database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource(context).getConnection()));
      } catch (DatabaseException | SQLException | NamingException e) {
         log.error(e.toString());
      }
      try {
         Liquibase liquibase = new Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);

         liquibase.update("2.1.3");

         // Pour faire un rollback
         //liquibase.rollback("2.1.4-SNAPSHOT", "*");
      } catch (LiquibaseException e) {
         log.error(e.toString());
      }
   }

   @Override
   public void destroy() {
      super.destroy();
   }
}