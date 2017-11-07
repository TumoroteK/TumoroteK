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
package fr.aphp.tumorotek.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import fr.aphp.tumorotek.model.systeme.Version;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.utils.Utils;


/**
 * Servlet first load at startup.
 * Lance les scripts de routine TumoroteK v2
 * Date: 09/08/2011
 * 
 * @version 2.0
 * @author Mathieu BARTHELEMY
 *
 */
public class InitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(InitServlet.class);
	
	private String updatedVersion;
	private String jdbcDialect;

	@Override
	public void init() throws ServletException {
		super.init();
						
		try {
			log.info("-----TumoroteK v2 startup routines-----");
	        Context ctx = new InitialContext();
	        String baseDir = (String) 
	        				ctx.lookup("java:comp/env/tk/tkFileSystem");
	        jdbcDialect = (String) ctx.lookup("java:comp/env/jdbc/dialect");
	        
			// updates
	        performUpdates(this.getServletContext()
					.getRealPath("/updates"), jdbcDialect);
			
	        // file system
	        if (baseDir != null) {
	        	File f = new File(baseDir);
	        	// crée le base directory au besoin
	        	if (f.exists() && f.isDirectory() 
	        			&& f.list().length == 0) {
	        		log.info("-----Mise en place du base directory "
	        									+ "Tumorotek v2-----");
	        		List<Plateforme> pfs = ManagerLocator
	        			.getPlateformeManager().findAllObjectsManager();
	        		String pfDir;
	        		List<Banque> banks = new ArrayList<Banque>();
	        		for (int i = 0; i < pfs.size(); i++) {
	        			pfDir = "pt_" + pfs.get(i).getPlateformeId() + "/";
	        			banks.addAll(ManagerLocator
	        				.getBanqueManager()
	        					.findByPlateformeAndArchiveManager(pfs.get(i), null));
	        			for (int j = 0; j < banks.size(); j++) {
	        				new File(baseDir + pfDir + "coll_" 
	        						+ banks.get(j).getBanqueId() 
	        						+ "/cr_anapath").mkdirs();
		    				new File(baseDir + pfDir + "coll_" 
	        						+ banks.get(j).getBanqueId() 
	        						+ "/anno").mkdirs();
		    				log.info("base directory de la collection " 
		    						+ banks.get(j).getNom() + " généré");
	        			}
	        			banks.clear();
	        		}
	        	}
	        }
	      } catch (NamingException ex) {
	    	  log.error(ex);
	      }
	}
	
	/**
	 * Applique la mise à jour des versions.
	 * @param real path
	 * @param dbms dialect
	 */
	public void performUpdates(String servletContextPath, String dbms) {
		log.info("-----Recherche des mises à jours database-----");
		Version vCurr = ManagerLocator.getVersionManager()
									.findByCurrentVersionManager();
		if (vCurr != null) {
			List<File> sqls = parseUpdateFilesFromXML(servletContextPath, vCurr
					.getVersion(), dbms);
			if (!sqls.isEmpty()) {
				DefaultTransactionDefinition def = 
							new DefaultTransactionDefinition();
				def.setName("updateDBtx");
				def.setPropagationBehavior(TransactionDefinition
											.PROPAGATION_REQUIRED);

				TransactionStatus status = ManagerLocator.getTxManager()
						.getTransaction(def);

				try {

					EntityManager em = SharedEntityManagerCreator
							.createSharedEntityManager(ManagerLocator
									.getTxManager().getEntityManagerFactory());

					// realise l'execution des fichiers
					for (int i = 0; i < sqls.size(); i++) {
						executeSQLqueries(sqls.get(i), em);
					}

					// enregistre la version
					Version vUpd = new Version();
					vUpd.setVersion(updatedVersion);
					vUpd.setNomSite(vCurr.getNomSite());
					vUpd.setDate(Utils.getCurrentSystemDate());
					em.persist(vUpd);

					ManagerLocator.getTxManager().commit(status);
				} catch (RuntimeException re) {
					log.error("procédure d'update TK v2 avortée suite "
							+ "aux erreurs suivantes:", re);
					ManagerLocator.getTxManager().rollback(status);
				}
			}
		}
	}
	
	/**
	 * Récupère une liste de paths vers les fichiers .sql à passer dans l'ordre
	 * pour mettre à jour la base de données.
	 * @param contextPath vers le dossier /updates
	 * @param version actuelle de la base de données
	 * @return List de File mappant fichiers .sql
	 */
	private List<File> parseUpdateFilesFromXML(String contextPath, 
												String version, 
												String jpaDialect) {
		
		List<File> sqls = new ArrayList<File>();
		
		if (jpaDialect.contains("Oracle")) {
			jpaDialect = "oracle";
		} else {
			jpaDialect = "mysql";
		}
		
		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		Document document = null;
		try {
			document = sxb.build(new File(contextPath 
									+ "/update-versions.xml"));
		} catch (Exception e) {
			log.error(e);
		}

		// racine -> updates
		Element racine = document.getRootElement();
		Iterator<?> versionsIt = 
						racine.getChildren("version").iterator();
		
		Element curr = null;
		boolean getUpdates = false;
		while (versionsIt.hasNext()) {
			curr = (Element) versionsIt.next();
			if (getUpdates) {
				for (int i = 0; i < curr.getChildren().size(); i++) {
					sqls.add(new File(contextPath + "/" + jpaDialect + "/"
						+ ((Element) curr.getChildren().get(i)).getText()));
				}
			}	
			if (!getUpdates 
					&& curr.getAttributeValue("nom").equals(version)) {
				getUpdates = true;
			}	
		}
		updatedVersion = curr.getAttributeValue("nom");
		
		return sqls;
	}
	
	/**
	 * Parse le fichier et executes chaque ligne finissant par ';'.
	 * @param file fichier .sql à parser
	 * @param em EntityManager executant les requêtes
	 */
	private void executeSQLqueries(File sqlFile, EntityManager em) {
		String s = new String();  
		StringBuffer sb = new StringBuffer(); 
		Query q;
		FileReader fr = null;
		BufferedReader br = null;
		try {  
			fr = new FileReader(sqlFile);  
			br = new BufferedReader(fr);  
			while ((s = br.readLine()) != null) {  
				if (!s.matches("^--.*$") && !s.matches("^\\/\\*.*$")) {
					sb.append(s);  
				}
			}
			br.close();  
				
			// here is our splitter ! We use ";" 
			// as a delimiter for each request  
			// then we are sure to have well formed statements  
			String[] inst = sb.toString().split(";");  
			
			for (int i = 0; i < inst.length; i++) {
				// we ensure that there is no spaces before or 
				// after the request string  
				// in order to not execute empty statements  
				if (!inst[i].trim().equals("")) {
					q = em.createNativeQuery(inst[i]);
					if (!inst[i].matches("^select.*")) {
						log.info("update query >>" + inst[i]);  						
						q.executeUpdate();
					} else {
						log.info("diagnostic query");
						log.info(inst[i]);
						List<?> res = q.getResultList();
						for (int j = 0; j < res.size(); j++) {
							log.warn(res.get(j));
						}
					}
					
				} 
			}
		} catch (FileNotFoundException fe) {
			log.error(fe);
			throw new 
				RuntimeException("rollback triggered by preceding exception");
		} catch (IOException e) {
			log.error(e);
			throw new 
				RuntimeException("rollback triggered by preceding exception");
		} finally {
			try { br.close(); } catch (Exception e) { br = null; }
			try { fr.close(); } catch (Exception e) { fr = null; }
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
}
