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
package fr.aphp.tumorotek.interfacage.sgl.view.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.interfacage.sgl.SglHandler;
import fr.aphp.tumorotek.interfacage.sgl.view.ViewHandler;
import fr.aphp.tumorotek.interfacage.sgl.view.ViewResultProcessor;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;

public class ViewHandlerImpl implements ViewHandler  {
	
	private final Log log = LogFactory.getLog(SglHandler.class);
		
	private String camelConfigLocation;

	public void setCamelConfigLocation(String _c) {
		this.camelConfigLocation = _c;
	}

	@Override
	public DossierExterne sendQuery(Emetteur _e, String sglNumDos, 
					String propFileName, ViewResultProcessor processor) {
		
		log.debug("send query for emetteur: " + _e.toString());
		log.debug("send query for code: " + sglNumDos);
	
		Map<String,Object> args = new HashMap<String, Object>();
		args.put("numDos", sglNumDos);
				 
		DossierExterne dExt = queryView(sglNumDos, propFileName, processor);
		if (dExt != null) {
			dExt.setEmetteur(_e);
		}
		return dExt;
	}
	

	public DossierExterne queryView(String numDos, String propFileName, 
											ViewResultProcessor processor) {
		DossierExterne dExt = null;
		ResourceBundle jdbcBundle = getJdbcBundle(propFileName);
		if (jdbcBundle != null) {
			log.debug("property file loaded: " + jdbcBundle.toString());
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet rSet = null;
			
			try {
				java.util.Properties props = new java.util.Properties();
				props.put("user", jdbcBundle.getString("username"));
				props.put("password", jdbcBundle.getString("password"));

				// Class.forName("net.sourceforge.jtds.jdbc.Driver");
				log.debug("jdbc driver: " + jdbcBundle.getString("driver"));
				Class.forName(jdbcBundle.getString("driver"));
				
				log.debug("jdbc url connection: " + jdbcBundle.getString("jdbcUrl"));
				con = DriverManager.getConnection(jdbcBundle.getString("jdbcUrl"), props);				
				
				stmt = con.prepareStatement(jdbcBundle.getString("statement"));
				stmt.setString(1, numDos);
				
				log.debug("executing view query: " + jdbcBundle.getString("statement"));
				
				rSet = stmt.executeQuery();
				
				log.debug("processing view query result");

				// one result max
				if (rSet.next()) {
					dExt = processor.processResult(rSet);
				}
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				if (con != null) { try { con.close(); } catch (Exception e) { con = null; }}
				if (stmt != null) { try { stmt.close();} catch (Exception e) { stmt = null; }}
				if (rSet != null) { try { rSet.close();} catch (Exception e) { rSet = null; }}
			}
		}
		return dExt;
	}
	
	private ResourceBundle getJdbcBundle(String propFileName) {
				
		if (camelConfigLocation != null && propFileName != null) {
			File file = new File(camelConfigLocation + propFileName);
			FileInputStream fis = null;
			InputStreamReader reader = null;
			ResourceBundle bundle = null;
			
			log.debug("stream property file looked path: " + file.getAbsolutePath());

			if (file.isFile()) { // Also checks for existance
				log.debug("stream property file as bundle: " + file.getAbsolutePath());
				try {
					fis = new FileInputStream(file);
					reader = new InputStreamReader(fis, 
							Charset.forName("UTF-8"));
					bundle = new PropertyResourceBundle(reader);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try { reader.close(); } catch (IOException e) { reader = null; }
					try { fis.close();} catch (IOException e) { fis = null; }
				}
			} else {
				log.error("view property file not loaded: " + propFileName);
				throw new RuntimeException("view.jdbc.properties.not.found");
			}
			return bundle;
		}

		return null;
	}

}
