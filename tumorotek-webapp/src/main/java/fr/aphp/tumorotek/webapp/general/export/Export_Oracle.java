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
package fr.aphp.tumorotek.webapp.general.export;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlMacroComponent;

import fr.aphp.tumorotek.action.utilisateur.ProfilExport;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

import oracle.sql.ARRAY;

/**
 * @version 2.2.3-rc1
 */
public class Export_Oracle extends Export
{

   private int outParamIdx = 2;

   public Export_Oracle(final Desktop d, final int ent, final List<Integer> o, final List<Banque> b, final ProfilExport pE,
      final short type, final Utilisateur u, final List<Integer> rI, final HtmlMacroComponent htmlMacroComponent,
      final Map<String, ?> params, EContexte contexte){
      super(d, ent, o, b, pE, type, u, rI, htmlMacroComponent, params, contexte);
   }

   @Override
   public String initSQL(){
      return "{call get_export_result(?,?)}";
   }

   @Override
   public String initCessionObjsExportSQL(){
      outParamIdx = 3;
      return "{call select_cession_data(?,?,?)}";
   }

   @Override
   public String initGSOExportSQL(){
      return "{call select_tvgso_data(?,?)}";
   }

   @Override
   public String initINCaExportSQL(){
      return "{call select_inca_data(?)}";
   }

   @Override
   public String initBIOCAPExportSQL(){
      outParamIdx = 1;
      return "{call select_biocap_data(?)}";
   }
   
   @Override
   public String initPatientTableAnonymeSQL() {
		return "{call create_tmp_patient_anonyme()}";
	}

   @Override
   protected void fetchResultSet() throws SQLException{

      // out cession parameter = 3
      if(getCession() != null){
         outParamIdx = 3;
      }

      ((CallableStatement) getPreparedStatement()).registerOutParameter(outParamIdx, oracle.jdbc.OracleTypes.CURSOR);
      getPreparedStatement().setFetchSize(100);
      getPreparedStatement().executeQuery();
      setMainRset((ResultSet) ((CallableStatement) getPreparedStatement()).getObject(outParamIdx));
   }

   @Override
   protected void populateDeriveParentsList() throws SQLException{
      final String query = "{call get_echanORPrelID_byProdD(?,?)}";
      setPreparedStatement(getConnection().prepareCall(query));
      // register the type of the out param - an Oracle specific type
      ((CallableStatement) getPreparedStatement()).registerOutParameter(1, oracle.jdbc.OracleTypes.ARRAY, "ID_LIST");
      ((CallableStatement) getPreparedStatement()).registerOutParameter(2, oracle.jdbc.OracleTypes.ARRAY, "ID_LIST");
      echantillonIds.clear();
      getPreparedStatement().executeQuery();
      final ARRAY a = (ARRAY) ((CallableStatement) getPreparedStatement()).getObject(1);
      final BigDecimal[] values = (BigDecimal[]) a.getArray();
      for(final BigDecimal v : values){
         if(v != null){
            echantillonIds.add(v.intValue());
         }
      }

      prelevementIds.clear();
      final ARRAY b = (ARRAY) ((CallableStatement) getPreparedStatement()).getObject(2);
      final BigDecimal[] values2 = (BigDecimal[]) b.getArray();
      for(final BigDecimal v : values2){
         if(v != null){
            prelevementIds.add(v.intValue());
         }
      }
   }

   @Override
   protected void initAnnoTables() throws SQLException{}

   //	protected void init() throws DesktopUnavailableException, InterruptedException {
   //		int type_export = 0;
   //		try {
   //			String query = "{call CREATE_OR_DROP_SEQUENCES(0)}";
   //			java.sql.PreparedStatement preparedStatement = connection
   //					.prepareStatement(query);
   //			preparedStatement.executeQuery();
   //			preparedStatement.close();
   //
   //			load_sequences();
   //			
   //			if(exportType == ConfigManager.BIOCAP_EXPORT) {
   //				type_export = ConfigManager.BIOCAP_EXPORT;
   //				export_biocap_features();
   //			} else {
   //				type_export = entite;
   //			}
   //
   //			String sql = "{call get_export_result(?,?)}";
   //			java.sql.CallableStatement p = connection.prepareCall(sql);
   //			p.setInt(1, type_export);
   //			p.registerOutParameter(2, OracleTypes.CURSOR);
   //			p.executeQuery();
   //			setMainRset((ResultSet) p.getObject(2));
   //
   //			// ------ LABO INTER
   //
   //			if (entite == ConfigManager.ENTITE_ID_PRELEVEMENT
   //					|| entite == ConfigManager.ENTITE_ID_ECHANTILLON
   //					|| entite == ConfigManager.ENTITE_ID_DERIVE && exportType != ConfigManager.BIOCAP_EXPORT) {
   //				String sql2 = "SELECT * FROM TMP_LABO_INTER_EXPORT";
   //				java.sql.PreparedStatement p2 = connection.prepareCall(sql2);
   //				if (p2.execute()) {
   //					setLaboRset(p2.getResultSet());
   //				}
   //				hasPrelevement = true;
   //			}
   //			connection.commit();
   //			
   //			create_export();
   //
   //		} catch (SQLException e1) {
   //			log.error(e1);
   //		} finally {
   //			try {
   //				drop_table();
   //				drop_sequence();
   //			} catch (SQLException e) {
   //				log.error(e);
   //			}
   //		}
   //	}

   //	protected void export_echantillons() throws SQLException, DesktopUnavailableException, InterruptedException {
   //		System.out.println("create echantillons export table");
   //
   //		// CREATION DE LA TABLE TEMPORAIRE ECHANTILLON
   //		String sql = "{call create_tmp_echantillon_table()}";
   //		// PreparedStatements can use variables and are more efficient
   //		java.sql.PreparedStatement preparedStatement = connection
   //				.prepareStatement(sql);
   //		preparedStatement.executeUpdate();
   //		preparedStatement.close();
   //
   //		// REMPLISSAGE DE LA TABLE TEMP ECHANTILLON
   //		String sql2 = "{call fill_tmp_table_echan(?)}";
   //		java.sql.PreparedStatement call = connection.prepareCall(sql2);
   //
   //		if (echantillonIds != null) {
   //			for (int i : echantillonIds) {
   //				call.setInt(1, i);
   //				call.addBatch();
   //			}
   //		} else {
   //			for (int i = 0; i < objsId.size(); i++) {
   //				// Echantillon echan = (Echantillon) objs.get(i);
   //				call.setInt(1, objsId.get(i));
   //				call.addBatch();
   //				// saveOperation((Echantillon) objs.get(i));
   //			}
   //		}
   //		call.executeBatch();
   //		call.close();
   //
   //		connection.commit();
   //
   //		// REMPLISSAGE DE LA TABLE TEMP ANNOTATION
   //		fillAnnotation(ConfigManager.ENTITE_ID_ECHANTILLON);
   //
   //		// RECUPERATION DES IDS PREL CORRESPONDANTS
   //		ResultSet resultSetId = null;
   //		String query = "SELECT distinct prelevement_id FROM tmp_echantillon_export";
   //		java.sql.PreparedStatement p = connection.prepareCall(query);
   //		if (p.execute()) {
   //			if (prelevementIds == null) {
   //				prelevementIds = new ArrayList<Integer>();
   //			}
   //			resultSetId = p.getResultSet();
   //			while (resultSetId.next()) {
   //				for (int i = 0; i < resultSetId.getMetaData().getColumnCount(); i++) {
   //					prelevementIds.add(resultSetId.getInt(i + 1));
   //				}
   //			}
   //		}
   //		p.close();
   //	}
   //
   //	protected void export_derives() throws SQLException, DesktopUnavailableException, InterruptedException {
   //		System.out.println("create derives export table");
   //		// CREATION DE LA TABLE TEMPORAIRE PROD DERIVE
   //		String sql = "{call create_tmp_derive_table()}";
   //		// PreparedStatements can use variables and are more efficient
   //		java.sql.PreparedStatement preparedStatement = connection
   //				.prepareStatement(sql);
   //		preparedStatement.executeUpdate();
   //		preparedStatement.close();
   //
   //		// REMPLISSAGE DE LA TABLE TEMP PROD DERIVE
   //		String sql2 = "{call fill_tmp_table_derive(?)}";
   //		java.sql.PreparedStatement call = connection.prepareCall(sql2);
   //
   //		for (int i = 0; i < objsId.size(); i++) {
   //			// ProdDerive pd = (ProdDerive) objsId.get(i);
   //			call.setInt(1, objsId.get(i));
   //			call.addBatch();
   //			// saveOperation((ProdDerive) objs.get(i));
   //		}
   //		call.executeBatch();
   //		call.close();
   //
   //		connection.commit();
   //
   //		fillAnnotation(ConfigManager.ENTITE_ID_DERIVE);
   //
   //		// RECUPERATION DES IDS PREL CORRESPONDANTS
   //
   //		String query = "{call get_echanORPrelID_byProdD(?,?)}";
   //		java.sql.CallableStatement p = connection.prepareCall(query);
   //		// register the type of the out param - an Oracle specific type
   //		p.registerOutParameter(1, OracleTypes.ARRAY, "ID_LIST");
   //		p.registerOutParameter(2, OracleTypes.ARRAY, "ID_LIST");
   //		System.out.println("... Get echan Ids ...");
   //		echantillonIds = new ArrayList<Integer>();
   //		p.executeQuery();
   //		ARRAY a = (ARRAY) p.getObject(1);
   //		BigDecimal[] values = (BigDecimal[]) a.getArray();
   //		for (BigDecimal v : values) {
   //			if (v != null) {
   //				echantillonIds.add(v.intValue());
   //			}
   //		}
   //
   //		prelevementIds = new ArrayList<Integer>();
   //		ARRAY b = (ARRAY) p.getObject(2);
   //		BigDecimal[] values2 = (BigDecimal[]) b.getArray();
   //		for (BigDecimal v : values2) {
   //			if (v != null) {
   //				prelevementIds.add(v.intValue());
   //			}
   //		}
   //
   //		// RECUPERATION DES IDS PREL CORRESPONDANTS
   //		// ResultSet resultSetId = null;
   //		// String query = "{call get_echanORPrelID_byProdD()}";
   //		// java.sql.PreparedStatement p = connection.prepareCall(query);
   //		// if (p.execute()) {
   //		// System.out.println("... Get echan Ids ...");
   //		// echantillonIds = new ArrayList<Integer>();
   //		// resultSetId = p.getResultSet();
   //		// p.getMoreResults(Statement.KEEP_CURRENT_RESULT);
   //		// ResultSet resultat2 = p.getResultSet();
   //		// while (resultSetId.next()) {
   //		// for (int i = 0; i < resultSetId.getMetaData().getColumnCount(); i++)
   //		// {
   //		// echantillonIds.add(resultSetId.getInt(i + 1));
   //		// }
   //		// }
   //		// prelevementIds = new ArrayList<Integer>();
   //		// while (resultat2.next()) {
   //		// for (int i = 0; i < resultat2.getMetaData().getColumnCount(); i++) {
   //		// prelevementIds.add(resultat2.getInt(i + 1));
   //		// }
   //		// }
   //		// System.out.println("prel id count > " + prelevementIds.size());
   //		// System.out.println("echan id count > " + echantillonIds.size());
   //		// }
   //		System.out.println("prel id count > " + prelevementIds.size());
   //		System.out.println("echan id count > " + echantillonIds.size());
   //		p.close();
   //	}
   //
   //	protected void createAndFillTmpAnnotationTable(int entite_id) {
   //		String query_get_annotation = "";
   //		if (hasMultipleCollection == 0) {
   //			query_get_annotation = "SELECT DISTINCT av.CHAMP_ANNOTATION_ID, ca.nom "
   //					+ "FROM ANNOTATION_VALEUR av JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID WHERE ta.ENTITE_ID = "
   //					+ entite_id + " AND av.BANQUE_ID = " + collection;
   //		} else {
   //			query_get_annotation = "SELECT DISTINCT av.CHAMP_ANNOTATION_ID, ca.nom "
   //					+ "FROM ANNOTATION_VALEUR av JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID WHERE ta.ENTITE_ID = "
   //					+ entite_id;
   //		}
   //		// VARIABLE DE RECUPERATION DES DATAS champ_nom/id annotation
   //		StringBuffer tableTmpAnnotationCreation = new StringBuffer();
   //		StringBuffer tableTmpAnnotationColumn = new StringBuffer();
   //		StringBuffer tableTmpAnnotationValues = new StringBuffer();
   //
   //		Statement stm;
   //		ResultSet rsp = null;
   //		try {
   //			stm = connection.createStatement();
   //			rsp = stm.executeQuery(query_get_annotation);
   //			// CREATION de la table DYNAMIQUE temporaire TMP_TABLE_ANNOTATION
   //			tableTmpAnnotationCreation
   //					.append("CREATE TABLE TMP_TABLE_ANNOTATION ( ");
   //			tableTmpAnnotationCreation.append("TMP_ID NUMBER(10) PRIMARY KEY");
   //			while (rsp.next()) {
   //				countAnnotation++;
   //				tableTmpAnnotationCreation.append(COMMA + "A" + countAnnotation
   //						+ UNDERSCORE + entite_id + " NUMBER(10) default "
   //						+ rsp.getInt(1) + " UNIQUE ");
   //				mapCorrespondanceAnnotationName.put("A" + countAnnotation
   //						+ UNDERSCORE + entite_id, rsp.getString(2));
   //				tableTmpAnnotationColumn.append(COMMA + "A" + countAnnotation
   //						+ UNDERSCORE + entite_id);
   //				tableTmpAnnotationValues.append(COMMA + rsp.getInt(1));
   //			}
   //			tableTmpAnnotationCreation.append(")");
   //
   //			Statement stm1 = connection.createStatement();
   //			// System.out.println(tableTmpAnnotationCreation.toString());
   //			stm1.executeUpdate(tableTmpAnnotationCreation.toString());
   //			// System.out
   //			// .println("INSERT INTO TMP_TABLE_ANNOTATION VALUES (annotation_id_seq.nextval"
   //			// + tableTmpAnnotationValues + ")");
   //			stm1.executeUpdate("INSERT INTO TMP_TABLE_ANNOTATION VALUES (annotation_id_seq.nextval"
   //					+ tableTmpAnnotationValues + ")");
   //
   //			// CREATION DEs TABLEs TEMPORAIREs
   //			String sql = "{call create_tmp_annotation_table()}";
   //			// PreparedStatements can use variables and are more efficient
   //			java.sql.PreparedStatement preparedStatement = connection
   //					.prepareStatement(sql);
   //			preparedStatement.executeUpdate();
   //			preparedStatement.close();
   //		} catch (SQLException e) {
   //			log.error(e);
   //		} finally {
   //			if (rsp != null) {
   //				try {
   //					rsp.close();
   //				} catch (SQLException e) {
   //					log.error(e);
   //				}
   //			}
   //		}
   //	}
   //
   //	private void drop_table() throws SQLException {
   //		System.out.println("table dropped.");
   //		// Delete temporary table
   //		String drop_sequences = "{call DROP_EXPORT_TABLE()}";
   //		// PreparedStatements can use variables and are more efficient
   //		java.sql.PreparedStatement p = connection
   //				.prepareStatement(drop_sequences);
   //		p.executeQuery();
   //		p.close();
   //	}
   //
   //	private void drop_sequence() throws SQLException {
   //		System.out.println("sequences dropped.");
   //		// Delete Oracle SEquences
   //		String drop_sequences = "{call CREATE_OR_DROP_SEQUENCES(1)}";
   //		// PreparedStatements can use variables and are more efficient
   //		java.sql.PreparedStatement p = connection
   //				.prepareStatement(drop_sequences);
   //		p.executeQuery();
   //		p.close();
   //	}
}
