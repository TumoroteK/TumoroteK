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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Filedownload;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.utilisateur.ProfilExport;
import fr.aphp.tumorotek.component.ProgressBarComponent;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.ResultSetToCsv;
import fr.aphp.tumorotek.webapp.general.ResultSetToExcel;

/**
 * SuperClass Export : Exports des données type Patient, Prelevement,
 * Echantillon, Produits Dérivés, Cession
 *
 * @author jhusson
 * @version 2.3.0-gatsbi
 */
public class Export extends Thread
{
	protected static Logger log = LoggerFactory.getLogger(Export.class);

	private static final Map<Integer, String> typeMap;
	static{
		typeMap = new HashMap<>();
		typeMap.put(1, "Patients");
		typeMap.put(2, "Prelevements");
		typeMap.put(3, "Echantillons");
		typeMap.put(5, "Cessions");
		typeMap.put(8, "Produitsderives");
	}

	private int progress = 0;
	private static final String EXPORT_PROGRESS = "progressbar.recherche.progress";
	private static final String DATA_PROGRESS = "progressbar.data.collect";
	private static final String ANNO_PROGRESS = "progressbar.annotations.collect";
	private static final String INTERM_PROGRESS = "progressbar.interm.progress";
	private static final String JOIN_PROGRESS = "progressbar.data.join";
	private static final String END_PROGRESS = "progressbar.export.finish";
	private static final String DATE_FORMAT = "yyyyMMddHHmm";

	protected static final String UNDERSCORE = "_";
	protected static final String COMMA = ",";
	protected Map<String, String> mapCorrespondanceAnnotationName = new HashMap<>();
	protected int countAnnotation = 0;
	protected int cessionObjcountAnnotation = 0;

	private static final int batchSize = 1000;

	protected Desktop desktop;
	protected List<Integer> objsId;
	protected int total;
	protected Utilisateur user;
	protected EContexte ctx;

	private List<Integer> restrictedAnnosTableIds = new ArrayList<>();

	protected int entite;
	protected String labelType;
	protected String sheetName = " ";
	protected short exportType = 1;
	protected List<Banque> collections;

   // @since 2.3.0-gatsbi
   protected Integer etudeId = null;
	
	protected List<Integer> patientIds = new ArrayList<>();
	protected List<Integer> prelevementIds = new ArrayList<>();
	protected List<Integer> echantillonIds = new ArrayList<>();

	// bool
	protected ProfilExport profilExport;
	protected boolean hasPrelevement;
	protected boolean firstAnno = true;
	// Export progress
	protected ProgressBarComponent progressBar;
	protected HtmlMacroComponent progressBarComponent;

	protected Connection connection;

	private ResultSet mainRset;
	private ResultSet laboRset;
	private ResultSet intermRset;
	private ResultSet retourEchanRset;
	private ResultSet deriveRetourRset;

	private Statement statement;
	private PreparedStatement preparedStatement;

	private SXSSFWorkbook wb = new SXSSFWorkbook(100);
	private ByteArrayOutputStream outStr = new ByteArrayOutputStream();

	// @since 2.1
	private Map<String, ?> params;

	// protected volatile Thread updateUIBarThread;

	// ajout cession pour export Echantillons/Derives pour une cession
	private Cession cession;

	// ajout contexte car export GSO est fonction du contexte
	private Contexte contexte;

	// ajout format export csv
	private boolean csv = false;

	public Export(){}

	public Export(final Desktop d, final List<Integer> o, final ProfilExport p, final HtmlMacroComponent htmlMacroComponent, final EContexte contexte){
		objsId = o;
		profilExport = p;
		init(d, htmlMacroComponent);
	}

	/**
	 * Constructor
	 * 
	 * @param d
	 * @param ent
	 * @param o
	 * @param b
	 * @param e
	 * @param u
	 * @param htmlMacroComponent
	 */
	public Export(final Desktop d, final int ent, final List<Integer> o, final List<Banque> b, final ProfilExport p,
			final short type, final Utilisateur u, final List<Integer> rI, final HtmlMacroComponent htmlMacroComponent,
			final Map<String, ?> _p, final EContexte contexte){

		objsId = o;
		profilExport = p;

		params = _p;

		ctx = contexte;

		init(d, htmlMacroComponent);

		entite = ent;
		collections = b;
		exportType = type;
		user = u;
		
		getRestrictedAnnosTableIds().addAll(rI);
		
      // since 2.3.0-gatsbi
      if(ctx.equals(EContexte.GATSBI)){
         etudeId = collections.get(0).getEtude().getEtudeId();
      }
	}

	protected void init(final Desktop d, final HtmlMacroComponent htmlMacroComponent){
		desktop = d;

		desktop.enableServerPush(true);

		this.progressBarComponent = htmlMacroComponent;
		this.progressBar = ((ProgressBarComponent) progressBarComponent.getFellow("progressPanel")
				.getAttributeOrFellow("progressPanel$composer", true));
		progressBar.setExportThread(this);

		total = objsId.size();
	}

	@Override
	public void run(){
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

			log.debug("sql connexion ok");

			// Compute Time
			// long startTime = System.nanoTime();
			// startTime = System.nanoTime();
			// long endTime = System.nanoTime();
			// ---------------------------------------------- //
			init();
			// ---------------------------------------------- //
			// endTime = System.nanoTime();
			// log.info("Total elapsed time in execution of method is :"
			//		+ ((double) (endTime - startTime) / 1000000000.0));

		}
		catch(final SQLTooManyAnnotationException e){
         log.error(e.getMessage(), e.getCause());
         e.getCause().printStackTrace();
         try{
            setExportDetails(0, 0, 0, null, null, e);
         }catch(DesktopUnavailableException | InterruptedException e1){}
      }
		catch(final ClassNotFoundException e){
			log.error("An error occurred: {}", e.toString()); 
		}catch(final DesktopUnavailableException e){
         log.warn(String.valueOf(e));
		}catch(final InterruptedException e){
			log.warn(e.getMessage());
		}catch(final Exception e){
			log.error(e.getMessage(), e);
			try{
				setExportDetails(0, 0, 0, null, null, e);
			}catch(DesktopUnavailableException | InterruptedException e1){}
		}finally{
			closeAll(true);
		}

	} // end main

	public void closeAll(final boolean detach){
		// close progressBar
		if(detach){
			detachProgressBarComponent();
		}
		if(detach && getDesktop().isServerPushEnabled()){
			getDesktop().enableServerPush(false);
		}

		if(getStatement() != null){
			try{
				// if (!getStatement().isClosed()) {
				getStatement().cancel();
				getStatement().close();
				// }
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setStatement(null);
			}
		}

		if(getPreparedStatement() != null){
			try{
				// if (!getPreparedStatement().isClosed()) {
				getPreparedStatement().cancel();
				getPreparedStatement().close();
				//}
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setPreparedStatement(null);
			}
		}

		// close ResultSet
		if(getMainRset() != null){
			try{
				getMainRset().close();
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setMainRset(null);
			}
		}
		if(getLaboRset() != null){
			try{
				getLaboRset().close();
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setLaboRset(null);
			}
		}
		if(getIntermRset() != null){
			try{
				getIntermRset().close();
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setIntermRset(null);
			}
		}

		// close connection
		if(getConnection() != null){
			try{
				// to avoid lock table operation if thread interrupted
				getConnection().rollback();
				getConnection().close();
			}catch(final SQLException e){
				log.error("An error occurred: {}", e.toString()); 
			}finally{
				setConnection(null);
			}
		}

		// close workbook
		if(getWb() != null){
			wb.dispose();
		}

		// close outputstream
		if(getOutStr() != null){
			try{
				getOutStr().close();
			}catch(final IOException e){
				outStr = null;
			}
		}

		if(isAlive() && !isInterrupted()){
			Thread.currentThread().interrupt();
		}
	}

	protected void init() throws Exception, DesktopUnavailableException, InterruptedException{

		String sql = initSQL();

		load_sequences();
		if(exportType == ConfigManager.TVGSO_EXPORT){
			export_tvgso_features();
			sql = initGSOExportSQL();
		}else if(exportType == ConfigManager.INCA_EXPORT){
			export_inca_features();
			sql = initINCaExportSQL();
		}else if(exportType == ConfigManager.BIOCAP_EXPORT){
			export_biocap_features();
			sql = initBIOCAPExportSQL();
		}else if(exportType == ConfigManager.BIOBANQUES_EXPORT){ // @since 2.1
			export_biobanques_features();
			sql = initBiobanquesExportSQL();
		}else if(getCession() != null){ // export specifique cession
			export_cession_features();
			sql = initCessionObjsExportSQL();
		}

		if(Thread.interrupted()){
			throw new InterruptedException();
		}

		setExportDetails(null, null, null, JOIN_PROGRESS, null, null);

		setPreparedStatement(getConnection().prepareCall(sql));
		if(exportType != ConfigManager.TVGSO_EXPORT && exportType != ConfigManager.INCA_EXPORT
				&& exportType != ConfigManager.BIOCAP_EXPORT){
			getPreparedStatement().setInt(1, entite);
			if(cession != null){
				getPreparedStatement().setInt(2, cessionObjcountAnnotation);
			}else if(exportType == ConfigManager.BIOBANQUES_EXPORT){ // @since 2.1
				final Boolean eds = (params != null && params.containsKey("eds")) ? ((Boolean) params.get("eds")) : false;
				getPreparedStatement().setBoolean(2, eds); // nip & patient_id switch si EDS
			}
		}else if(exportType == ConfigManager.TVGSO_EXPORT){
			getPreparedStatement().setBoolean(1, (contexte != null && contexte.getNom().equals("hematologie")));
		}

		fetchResultSet();

		if(exportType != ConfigManager.BIOCAP_EXPORT && exportType != ConfigManager.TVGSO_EXPORT
				&& exportType != ConfigManager.INCA_EXPORT && exportType != ConfigManager.BIOBANQUES_EXPORT
				&& (entite == ConfigManager.ENTITE_ID_PRELEVEMENT || entite == ConfigManager.ENTITE_ID_DERIVE
				|| entite == ConfigManager.ENTITE_ID_ECHANTILLON)){
			String sql2 = "SELECT * FROM TMP_LABO_INTER_EXPORT";
			setPreparedStatement(connection.prepareCall(sql2));
			if(getPreparedStatement().execute()){
				setLaboRset(getPreparedStatement().getResultSet());
				// getPreparedStatement().close();
			}
			hasPrelevement = true;

			// retours ResultSet
			if(entite == ConfigManager.ENTITE_ID_ECHANTILLON || entite == ConfigManager.ENTITE_ID_DERIVE){
				sql2 = "SELECT * FROM TMP_ECHAN_RETOUR_EXPORT";
				setPreparedStatement(connection.prepareCall(sql2));
				if(getPreparedStatement().execute()){
					setRetourEchanRset(getPreparedStatement().getResultSet());
				}
			}

			if(entite == ConfigManager.ENTITE_ID_DERIVE){
				sql2 = "SELECT * FROM TMP_DERIVE_RETOUR_EXPORT";
				setPreparedStatement(connection.prepareCall(sql2));
				if(getPreparedStatement().execute()){
					setDeriveRetourRset(getPreparedStatement().getResultSet());
				}
			}
		}
		// connection.commit();
		createXLS();
	}

	protected void fetchResultSet() throws SQLException{
		if(getPreparedStatement().execute()){
			setMainRset(getPreparedStatement().getResultSet());
			//getPreparedStatement().close();
		}
	}

	protected String initSQL(){
		return "{call get_export_result(?)}";
	}

	protected String initCessionObjsExportSQL() {
		if (ctx != null) {
			switch(ctx)	{
				case SEROLOGIE:
					return "{call select_cession_data_sero(?,?)}";
				default:
					return "{call select_cession_data(?,?)}";
			}
		}
		return "{call select_cession_data(?,?)}";
	}

	protected String initGSOExportSQL() {
		return "{call select_tvgso_data(?)}";
	}

	protected String initBIOCAPExportSQL(){
		return "{call select_biocap_data()}";
	}

	protected String initBiobanquesExportSQL(){
		return "{call select_biobanques_data(?,?)}";
	}

	protected String initINCaExportSQL(){
		return "{call select_inca_data()}";
	}
	
	protected String initPatientTableAnonymeSQL() {
		return "{call create_tmp_patient_table_anonyme()}";
	}

	protected void load_sequences() throws SQLException, DesktopUnavailableException, InterruptedException{
		// Creation des Annotations pour l'Entite en cours
		log.debug("load_sequence");

		createAndFillTmpAnnotationTable(entite);
		progress = 10;
		setExportDetails(progress, null, null, EXPORT_PROGRESS, null, null);
		if(entite == ConfigManager.ENTITE_ID_PATIENT){
			sheetName = ConfigManager.aSheetName[0];
			labelType = "export.patients.filename";
			export_patients(null);
			export_maladie();
		}else if(entite == ConfigManager.ENTITE_ID_PRELEVEMENT){
			sheetName = ConfigManager.aSheetName[1];
			labelType = "export.prelevements.filename";
			export_prelevements(null);
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_PATIENT);
			export_patients(patientIds);
			// connection.commit();
			// updateProgressBar(60);
		}else if(entite == ConfigManager.ENTITE_ID_ECHANTILLON){
			sheetName = ConfigManager.aSheetName[2];
			labelType = "export.echantillons.filename";
			if(exportType == ConfigManager.TVGSO_EXPORT){
				labelType = "export.tvgso.filename";
			}else if(exportType == ConfigManager.INCA_EXPORT){
				labelType = "export.inca.filename";
			}else if(exportType == ConfigManager.BIOCAP_EXPORT){
				labelType = "export.biocap.filename";
			}else if(exportType == ConfigManager.BIOBANQUES_EXPORT){ //@since 2.1
				labelType = "export.biobanques.filename";
			}
			export_echantillons(null);
			if(cession != null){
				cessionObjcountAnnotation = countAnnotation;
			}
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_PRELEVEMENT);
			export_prelevements(prelevementIds);
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_PATIENT);
			export_patients(patientIds);
			// connection.commit();
			// updateProgressBar(70);
		}else if(entite == ConfigManager.ENTITE_ID_DERIVE){
			sheetName = ConfigManager.aSheetName[3];
			labelType = "export.prodDerives.filename";
			export_derives();
			if(cession != null){
				cessionObjcountAnnotation = countAnnotation;
			}
			// updateProgressBar(30);
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_ECHANTILLON);
			export_echantillons(echantillonIds);
			// connection.commit();
			// updateProgressBar(40);
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_PRELEVEMENT);
			export_prelevements(prelevementIds);
			countAnnotation = 0;
			createAndFillTmpAnnotationTable(ConfigManager.ENTITE_ID_PATIENT);
			export_patients(patientIds);
			// connection.commit();
			// updateProgressBar(60);
		}else if(entite == ConfigManager.ENTITE_ID_CESSION){
			sheetName = ConfigManager.aSheetName[4];
			labelType = "export.cessions.filename";
			export_cession();
			connection.commit();
			// updateProgressBar(50);
		}
	}

	protected void export_patients(final List<Integer> o) throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export patient");

		setExportDetails(progress += 10, null, o == null ? total : o.size(), DATA_PROGRESS, "progressbar.entite.Patients", null);
		launchSQLproc(1, o);

		fillAnnotation(ConfigManager.ENTITE_ID_PATIENT);
	}

	protected void export_maladie() throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export maladie");

		// setExportDetails(progress += 10, null, null, "récupération des maladie", null);
		launchSQLproc(7, null);
	}

	protected void export_prelevements(final List<Integer> o)
			throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export prelevement");

		setExportDetails(progress += 10, null, o == null ? total : o.size(), DATA_PROGRESS, "progressbar.entite.Prelevements",
				null);
		launchSQLproc(2, o);

		fillAnnotation(ConfigManager.ENTITE_ID_PRELEVEMENT);

		export_labo_inter(o);

		populateImtermIds(patientIds, "SELECT distinct patient_id FROM TMP_PRELEVEMENT_EXPORT");
	}

	private void populateImtermIds(final List<Integer> intermIds, final String query) throws SQLException, InterruptedException{
		if(Thread.interrupted()){
			throw new InterruptedException();
		}

		try{
			setExportDetails(null, null, null, INTERM_PROGRESS, null, null);
			// RECUPERATION DES IDS PATIENTS CORRESPONDANTS
			setIntermRset(getStatement().executeQuery(query));
			// intermIds.clear();
			while(getIntermRset().next()){
				if(getIntermRset().getInt(1) > 0 && !intermIds.contains(getIntermRset().getInt(1))){
					intermIds.add(getIntermRset().getInt(1));
				}
			}
		}catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			throw e;
		}
	}

	protected void export_labo_inter(final List<Integer> o) throws SQLException, InterruptedException{

		log.debug("export labos");

		if(Thread.interrupted()){
			throw new InterruptedException();
		}
		try{
			List<Integer> ids;
			if(o == null){
				ids = objsId;
			}else{
				ids = o;
			}

			// CREATION DE LA TABLE TEMPORARAIRE LABO INTER
			final String sql = "{call create_tmp_labo_inter_table()}";
			// PreparedStatements can use variables and are more efficient
			setPreparedStatement(connection.prepareStatement(sql));
			getPreparedStatement().executeUpdate();
			getPreparedStatement().close();

			// REMPLISSAGE DE LA TABLE TEMP LABO INTER
			final String sql2 = "{call fill_tmp_labo_inter_table(?)}";
			setPreparedStatement(connection.prepareCall(sql2));

			int count = 0;
			for(final Integer i : ids){
				getPreparedStatement().setInt(1, i);
				getPreparedStatement().addBatch();

				if(++count % batchSize == 0){
					getPreparedStatement().executeBatch();
					getPreparedStatement().clearBatch();
				}
			}
			getPreparedStatement().executeBatch();
			getPreparedStatement().close();
		}catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			throw e;
		}
	}

	protected void export_retour(final List<Integer> o, final int entiteId) throws SQLException, InterruptedException{

		log.debug("export retour");

		if(Thread.interrupted()){
			throw new InterruptedException();
		}
		try{
			List<Integer> ids;
			if(o == null){
				ids = objsId;
			}else{
				ids = o;
			}

			// CREATION DE LA TABLE TEMPORARAIRE RETOUR
			final String sql = entiteId == ConfigManager.ENTITE_ID_ECHANTILLON ? "{call create_tmp_echan_retour_table()}"
					: "{call create_tmp_derive_retour_table()}";

			// PreparedStatements can use variables and are more efficient
			setPreparedStatement(connection.prepareStatement(sql));
			getPreparedStatement().executeUpdate();
			getPreparedStatement().close();

			// REMPLISSAGE DE LA TABLE TEMP RETOUR
			final String sql2 = entiteId == ConfigManager.ENTITE_ID_ECHANTILLON ? "{call fill_tmp_echan_retour_table(?)}"
					: "{call fill_tmp_derive_retour_table(?)}";
			setPreparedStatement(connection.prepareCall(sql2));

			int count = 0;
			for(final Integer i : ids){
				getPreparedStatement().setInt(1, i);
				getPreparedStatement().addBatch();

				if(++count % batchSize == 0){
					getPreparedStatement().executeBatch();
					getPreparedStatement().clearBatch();
				}
			}
			getPreparedStatement().executeBatch();
			getPreparedStatement().close();
		}catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			throw e;
		}
	}

	protected void export_echantillons(final List<Integer> o)
			throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export echantillons");

		setExportDetails(progress += 10, null, o == null ? total : o.size(), DATA_PROGRESS, "progressbar.entite.Echantillons",
				null);
		launchSQLproc(3, o);

		fillAnnotation(ConfigManager.ENTITE_ID_ECHANTILLON);

		export_retour(o, ConfigManager.ENTITE_ID_ECHANTILLON);

		populateImtermIds(prelevementIds, "SELECT DISTINCT prelevement_id FROM TMP_ECHANTILLON_EXPORT");
	}

	protected void export_derives() throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export derives");

		setExportDetails(progress += 10, null, total, DATA_PROGRESS, "progressbar.entite.ProduitsDerives", null);
		launchSQLproc(8, null);

		fillAnnotation(ConfigManager.ENTITE_ID_DERIVE);

		export_retour(objsId, ConfigManager.ENTITE_ID_DERIVE);

		if(Thread.interrupted()){
			throw new InterruptedException();
		}
		try{
			populateDeriveParentsList();
			getPreparedStatement().close();
		}catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			throw e;
		}
	}

	protected void populateDeriveParentsList() throws SQLException{

		log.debug("recup parents derives");

		// RECUPERATION DES IDS PREL CORRESPONDANTS
		ResultSet resultSetId = null;

		final String query = "{call get_echanORPrelID_byProdD()}";
		setPreparedStatement(connection.prepareCall(query));
		if(getPreparedStatement().execute()){
			echantillonIds.clear();
			resultSetId = getPreparedStatement().getResultSet();
			getPreparedStatement().getMoreResults(Statement.KEEP_CURRENT_RESULT);
			final ResultSet resultat2 = getPreparedStatement().getResultSet();
			while(resultSetId.next()){
				for(int i = 0; i < resultSetId.getMetaData().getColumnCount(); i++){
					echantillonIds.add(resultSetId.getInt(i + 1));
				}
			}
			resultSetId.close();
			prelevementIds.clear();
			while(resultat2.next()){
				for(int i = 0; i < resultat2.getMetaData().getColumnCount(); i++){
					prelevementIds.add(resultat2.getInt(i + 1));
				}
			}
			resultat2.close();
		}
	}

	protected void export_cession() throws SQLException, DesktopUnavailableException, InterruptedException{

		log.debug("export cession");

		setExportDetails(progress += 10, null, total, DATA_PROGRESS, "progressbar.entite.Cessions", null);
		launchSQLproc(5, null);

		fillAnnotation(ConfigManager.ENTITE_ID_CESSION);

	}

	protected void export_inca_features() throws SQLException, DesktopUnavailableException, InterruptedException{

		setExportDetails(progress += 10, null, total, "progressbar.inca.addition", "progressbar.entite.Echantillons", null);

		// CREATION DE LA TABLE TEMPORAIRE ADDITIVE INCA
		getStatement().executeUpdate("{call create_tmp_inca_adds()}");
		preparedStatement.close();

		// REMPLISSAGE DE LA TABLE ADDITIVE INCA
		setPreparedStatement(getConnection().prepareCall("{call fill_tmp_table_inca_adds(?)}"));
		int count = 0;
		for(final Integer i : objsId){
			getPreparedStatement().setInt(1, i);
			getPreparedStatement().addBatch();

			if(++count % batchSize == 0){
				getPreparedStatement().executeBatch();
				getPreparedStatement().clearBatch();
			}
		}
		getPreparedStatement().executeBatch();
		getPreparedStatement().close();
		getConnection().commit();
	}

	protected void export_tvgso_features() throws SQLException, DesktopUnavailableException, InterruptedException{

		setExportDetails(progress += 10, null, total, "progressbar.tvgso.addition", "progressbar.entite.Echantillons", null);

		// CREATION DE LA TABLE TEMPORAIRE ADDITIVE TVGSO
		getStatement().executeUpdate("{call create_tmp_tvgso_adds()}");
		preparedStatement.close();

		// REMPLISSAGE DE LA TABLE ADDITIVE TVGSO
		setPreparedStatement(getConnection().prepareCall("{call fill_tmp_table_adds(?,?)}"));
		int count = 0;
		for(final Integer i : objsId){
			getPreparedStatement().setInt(1, i);
			getPreparedStatement().setBoolean(2, (contexte != null && contexte.getNom().equals("hematologie")));
			getPreparedStatement().addBatch();
			// saveOperation((Patient) objs.get(i));
			if(++count % batchSize == 0){
				getPreparedStatement().executeBatch();
				getPreparedStatement().clearBatch();
			}
		}
		getPreparedStatement().executeBatch();
		getPreparedStatement().close();
		getConnection().commit();
	}

	protected void export_biocap_features() throws SQLException, DesktopUnavailableException, InterruptedException{

		setExportDetails(progress += 10, null, total, "progressbar.biocap.addition", "progressbar.entite.Echantillons", null);

		// CREATION DES TABLES TEMPORAIRES ADDITIVES BIOCAP
		getStatement().executeUpdate("{call create_tmp_biocap_adds()}");
		getStatement().execute("{call fill_tmp_biocap_adds()}");
	}

	/*
	 * @since 2.1
	 */
	protected void export_biobanques_features() throws SQLException, DesktopUnavailableException, InterruptedException{

		setExportDetails(progress += 10, null, total, "progressbar.biobanques.addition", "progressbar.entite.Echantillons", null);

		// CREATION DES TABLES TEMPORAIRES ADDITIVES BIOBANQUES
		getStatement().executeUpdate("{call create_tmp_biobanques_adds()}");
		getStatement().execute("{call fill_tmp_biobanques_adds()}");
	}

	protected void export_cession_features() throws SQLException, DesktopUnavailableException, InterruptedException{

		setExportDetails(progress += 10, null, total, "progressbar.cession.addition", "progressbar.entite.Echantillons", null);

		// CREATION DE LA TABLE TEMPORAIRE ADDITIVE CESSION
		getStatement().executeUpdate("{call create_tmp_cession_adds()}");
		preparedStatement.close();

		// REMPLISSAGE DE LA TABLE ADDITIVE CESSION
		setPreparedStatement(getConnection().prepareCall("{call fill_tmp_table_cession_adds(?)}"));
		getPreparedStatement().setInt(1, getCession().getCessionId());
		getPreparedStatement().execute();
		getPreparedStatement().close();
		getConnection().commit();
	}

	/**
	 * REMPLISSAGE DE LA TABLE TEMP ANNOTATION
	 * 
	 * @param entite
	 * @throws SQLException
	 * @throws InterruptedException 
	 * @throws DesktopUnavailableException 
	 */
	protected void fillAnnotation(final int entite) throws SQLException, DesktopUnavailableException, InterruptedException{
		if(exportType != ConfigManager.TVGSO_EXPORT && exportType != ConfigManager.BIOCAP_EXPORT
				&& exportType != ConfigManager.INCA_EXPORT){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}

			try{
				setExportDetails(progress += 10, null, null, ANNO_PROGRESS, "", null);

				final String sql1 = "{call fill_tmp_table_annotation(?, ?, ?)}";
				setPreparedStatement(connection.prepareCall(sql1));
				getPreparedStatement().setInt(1, 0);//TK-320 : forcer à 0 : ce paramètre n'est plus utilisé par la procédure stockée
				getPreparedStatement().setInt(2, entite);
				getPreparedStatement().setInt(3, countAnnotation);
				// getPreparedStatement().setInt(4, hasMultipleCollection);
				if(getPreparedStatement() != null){
					// && !getPreparedStatement().isClosed()) {
					getPreparedStatement().execute();
					getPreparedStatement().close();
					getConnection().commit();
				}
			}
			catch(SQLSyntaxErrorException sqlException) {
			   throw new SQLTooManyAnnotationException(sqlException);//TK-320 : permet de gérer l'erreur : "Row size too large. The maximum row size for the used" liée à un trop grand nombre d'annotations
			}catch(final Exception e){
				if(Thread.interrupted()){
					throw new InterruptedException();
				}
				throw e;
			}
		}
	}

	protected void createAndFillTmpAnnotationTable(final int entite_id) throws SQLException, InterruptedException{

		if(exportType != ConfigManager.TVGSO_EXPORT && exportType != ConfigManager.BIOCAP_EXPORT
				&& exportType != ConfigManager.INCA_EXPORT){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			try{
				log.debug("fill tmp table annotation " + entite_id);

				initAnnoTables();

				// peuple la table restrict à la première demande d'export annotations
				if(firstAnno){
					setPreparedStatement(getConnection()
							.prepareStatement("insert into TMP_TABLE_ANNOTATION_RESTRICT " + "(TABLE_ANNOTATION_ID) values (?)"));
					for(final Integer i : getRestrictedAnnosTableIds()){
						getPreparedStatement().setInt(1, i);
						getPreparedStatement().addBatch();
					}
					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
					firstAnno = false;
				}

				log.debug("fill annotation restrict");
				
	         //TK-320 : adaptation de la requête select
				StringBuilder selectInSb = new StringBuilder();
				int nbCollections = collections.size();
				for(int i=0; i<nbCollections; i++) {
				   if(i!=0) {
				      selectInSb = selectInSb.append(",");
				   }
				   selectInSb = selectInSb.append(collections.get(i).getBanqueId());
				}

				String query_get_annotation = "";
				query_get_annotation = "SELECT DISTINCT ca.CHAMP_ANNOTATION_ID, ca.nom, ca.data_type_id, "
						+ "tab.ORDRE, ca.ordre " 
						+ "FROM CHAMP_ANNOTATION ca " + "JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID "
						+ "JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID "
					   + "JOIN TABLE_ANNOTATION_BANQUE tab ON tab.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID "
						+ "WHERE ta.ENTITE_ID = " + entite_id + " AND tab.BANQUE_ID in (" + selectInSb.toString() + ") "
						+ " ORDER BY "
						+ "tab.ORDRE, ca.ORDRE";
										
				final List<Integer> annodates_idx = new ArrayList<>();
				final List<Integer> annoNums_idx = new ArrayList<>();
				final List<Integer> annoBools_idx = new ArrayList<>();
				final List<Integer> annoTexte_idx = new ArrayList<>();

				if(getStatement() == null){
					setStatement(getConnection().createStatement());
				}

				if(getStatement() != null){

					setIntermRset(getStatement().executeQuery(query_get_annotation));

					setPreparedStatement(
							getConnection().prepareStatement("insert into TMP_TABLE_ANNOTATION " + "(CHAMP_LABEL, CHAMP_ID) values (?,?)"));
					String chpLabel;
					while(getIntermRset().next()){
						countAnnotation++;
						if(getIntermRset().getInt(3) == 3 || getIntermRset().getInt(3) == 11){
							annodates_idx.add(countAnnotation);
						}else if(getIntermRset().getInt(3) == 5){
							annoNums_idx.add(countAnnotation);
						}else if(getIntermRset().getInt(3) == 2){
							annoBools_idx.add(countAnnotation);
						}else if(getIntermRset().getInt(3) == 6){
							annoTexte_idx.add(countAnnotation);
						}

						chpLabel = "A" + countAnnotation + UNDERSCORE + entite_id;

						mapCorrespondanceAnnotationName.put(chpLabel, getIntermRset().getString(2));

						getPreparedStatement().setString(1, chpLabel);
						getPreparedStatement().setInt(2, getIntermRset().getInt(1));
						getPreparedStatement().addBatch();
					}

					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
				}

				log.debug(" tmp champ completed");

				if(!annodates_idx.isEmpty()){
					setPreparedStatement(getConnection().prepareStatement("insert into TMP_ANNO_DATE_IDX values (?)"));
					for(final Integer i : annodates_idx){
						getPreparedStatement().setInt(1, i);
						getPreparedStatement().addBatch();
					}
					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
				}
				if(!annoNums_idx.isEmpty()){
					setPreparedStatement(getConnection().prepareStatement("insert into TMP_ANNO_NUMS_IDX values (?)"));
					for(final Integer i : annoNums_idx){
						getPreparedStatement().setInt(1, i);
						getPreparedStatement().addBatch();
					}
					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
				}
				if(!annoBools_idx.isEmpty()){
					setPreparedStatement(getConnection().prepareStatement("insert into TMP_ANNO_BOOLS_IDX values (?)"));
					for(final Integer i : annoBools_idx){
						getPreparedStatement().setInt(1, i);
						getPreparedStatement().addBatch();
					}
					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
				}
				if(!annoTexte_idx.isEmpty()){
					setPreparedStatement(getConnection().prepareStatement("insert into TMP_ANNO_TEXTES_IDX values (?)"));
					for(final Integer i : annoTexte_idx){
						getPreparedStatement().setInt(1, i);
						getPreparedStatement().addBatch();
					}
					getPreparedStatement().executeBatch();
					getPreparedStatement().close();
				}
			}catch(final Exception e){
				log.error(e.getMessage());
				e.printStackTrace();
				if(Thread.interrupted()){
					throw new InterruptedException();
				}
				throw e;
			}
		}
	}

	protected void initAnnoTables() throws SQLException{
		log.debug("init anno tables");
		// CREATION DEs TABLEs TEMPORAIREs
		final String sql = "{call create_tmp_annotation_table()}";
		// PreparedStatements can use variables and are more efficient
		setPreparedStatement(getConnection().prepareStatement(sql));
		getPreparedStatement().executeUpdate();
		getPreparedStatement().close();
	}

	//	protected void create_export()
	//			throws SQLException {
	//		try {
	//			if (exportType == ConfigManager.BIOCAP_EXPORT) {
	//				log.debug("----- BIOCAP EXPORT ------");
	//				try {
	//					InputStream is = XMLUtils.resultSetToXML(getMainRset());
	//					String inXSL = ConfigManager.BIOCAP_XSLT_FILE;
	//					String outTXT = ConfigManager.BIOCAP_OUT_FILE;
	//					File f = new File(outTXT);
	//					XMLTransform.transform(is, this.getClass()
	//							.getResourceAsStream(inXSL), f);
	//					try {
	//						setExportDetails(100, total, total, "termines", null, null);
	//						Executions.activate(desktop);
	//						downloadExportFileXls(f, desktop);
	//						Executions.deactivate(desktop);
	//					} catch (InterruptedException ex) {
	//						log.error("Error {}: ", String.valueOf(ex));
	//					}
	//					log.debug("----- BIOCAP TRANSFORMATION OK ------");
	//				} catch (TransformerConfigurationException e) {
	//					log.error("An error occurred: {}", e.toString()); 
	//				} catch (TransformerException e) {
	//					log.error("An error occurred: {}", e.toString()); 
	//				} catch (ParserConfigurationException e) {
	//					e.printStackTrace();
	//				}
	//			} else {
	//				createXLS();
	//			}
	//		} catch (Exception e) {
	//			log.error("An error occurred: {}", e.toString()); 
	//			throw new RuntimeException(e);
	//		}
	//	}

	protected void createXLS() throws Exception{
		if(Thread.interrupted()){
			throw new InterruptedException();
		}
		try{
			// filename
			final StringBuffer sb = new StringBuffer();
			final Calendar cal = Calendar.getInstance();
			final String date = new SimpleDateFormat(DATE_FORMAT).format(cal.getTime());
			sb.append(ObjectTypesFormatters.getLabel(labelType, new String[] {date}));
			// sb.append(".xlsx");

			if(!isCsv()){
				final ResultSetToExcel resultSetToExcel = new ResultSetToExcel(getWb(), this, getMainRset(), getLaboRset(),
						getRetourEchanRset(), getDeriveRetourRset(), profilExport, hasPrelevement, sheetName, cession != null);
				resultSetToExcel.setUpdateThread(this);
				resultSetToExcel.generate();
				setExportDetails(100, null, null, END_PROGRESS, null, null);

				downloadExportFileXls(wb, sb.toString());
			}else{
				downloadExportFileCSV(getMainRset(), profilExport, sb.toString());
			}

			saveOperations(objsId, user, entite, exportType, profilExport);
		}catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
			throw e;
		}
	}

	/*************************************************************************/
	/********************* METHODES UTILITAIRES ******************************/
	/*************************************************************************/

	public Map<String, String> getMapCorrespondanceAnnotationName(){
		return mapCorrespondanceAnnotationName;
	}

	protected void detachProgressBarComponent(){
		if(desktop.isServerPushEnabled()){
			try{
				Executions.activate(desktop);
			}catch(final DesktopUnavailableException e){
				log.warn(String.valueOf(e));
			}catch(final InterruptedException e){
				log.warn(String.valueOf(e));
			}
			if(!progressBar.isError()){
				progressBarComponent.getParent().detach();
			}
			Executions.deactivate(desktop);
		}
	}

	public void setExportDetails(final Integer value, final Integer v, final Integer c, final String t, final String t2,
			final Exception exc) throws DesktopUnavailableException, InterruptedException{

		if(desktop.isServerPushEnabled() && !Thread.interrupted()){
			Executions.activate(desktop);
			if(value != null){
				progressBar.setValue(value);
			}
			progressBar.setDetail(v, c, t, t2, exc);
			Executions.deactivate(desktop);
		}

	}

	Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler()
	{
		@Override
		public void uncaughtException(final Thread th, final Throwable ex){
			log.error("Uncaught exception: " + ex);
		}
	};

	/**
	 * Gère le download d'un fichier d'export xls.
	 * 
	 * @param wb
	 *            workbook
	 * @param fileName
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws DesktopUnavailableException 
	 */
	public void downloadExportFileXls(final Workbook wb, final String fileName)
			throws IOException, DesktopUnavailableException, InterruptedException{
		if(Thread.interrupted()){
			throw new InterruptedException();
		}
		wb.write(getOutStr());
		final AMedia media = new AMedia(fileName, "xlsx",
				// (hasMultipleCollection == 0) ? ConfigManager.OFFICE_EXCEL_MIME_TYPE :
				ConfigManager.OFFICE_OPENXML_MIME_TYPE, getOutStr().toByteArray());

		Executions.activate(desktop);
		Filedownload.save(media);
		Executions.deactivate(desktop);
	}

	public void downloadExportFileCSV(final ResultSet rSet, final ProfilExport pExport, 
														final String fileName) throws Exception{
		if(Thread.interrupted()){
			throw new InterruptedException();
		}

		new ResultSetToCsv(getOutStr(), this, getMainRset(), pExport, "|").generate();

		final AMedia media = new AMedia(fileName, "csv",
				// (hasMultipleCollection == 0) ? ConfigManager.OFFICE_EXCEL_MIME_TYPE :
				"text/plain", getOutStr().toByteArray());

		Executions.activate(desktop);
		Filedownload.save(media);
		Executions.deactivate(desktop);
	}

	//	/**
	//	 * Prepare le download d'un fichier d'export au format xls, donc à partir
	//	 * d'un ByteArrayOutputStream.
	//	 * 
	//	 * @param buf
	//	 *            ByteArrayOutputStream
	//	 * @param filename
	//	 * @param desktop2
	//	 */
	//	private void downloadExportFileXls(File file, Desktop desktop) {
	//		try {
	//			if (file != null) {
	//				AMedia media = new AMedia(file,
	//						ConfigManager.OFFICE_EXCEL_MIME_TYPE,
	//						ConfigManager.UNICODE_CHARSET);
	//				FileDownloadTumo.save(media, desktop);
	//			}
	//		} catch (FileNotFoundException e) {
	//			log.error("An error occurred: {}", e.toString()); 
	//		}
	//	}

	protected static synchronized void saveOperations(final List<Integer> objsId, final Utilisateur user, final int entite,
			final int exportType, final ProfilExport pE) throws SQLException, DesktopUnavailableException, InterruptedException{
		if(!Thread.interrupted()){

			ManagerLocator.getOperationManager().batchSaveManager(objsId, user,
					exportType == ConfigManager.INCA_EXPORT ? ManagerLocator.getOperationTypeManager().findByIdManager(22)
							: exportType == ConfigManager.TVGSO_EXPORT ? ManagerLocator.getOperationTypeManager().findByIdManager(21)
									: exportType == ConfigManager.BIOCAP_EXPORT ? ManagerLocator.getOperationTypeManager().findByIdManager(23)
											: pE != null && !pE.equals(ProfilExport.NOMINATIF) ? 
												ManagerLocator.getOperationTypeManager().findByIdManager(11) 
													: ManagerLocator.getOperationTypeManager().findByIdManager(2),
				Utils.getCurrentSystemCalendar(), ManagerLocator.getEntiteManager().findByIdManager(entite));
		}

		objsId.clear();

	}

	protected static class StringToStream
	{
		public static InputStream getStream(final String str){
			final String text = str;
			InputStream is = null;
			/*
			 * Convert String to InputStream using ByteArrayInputStream class.
			 * This class constructor takes the string byte array which can be
			 * done by calling the getBytes() method.
			 */
			try{
				is = new ByteArrayInputStream(text.getBytes(ConfigManager.UNICODE_CHARSET));
			}catch(final UnsupportedEncodingException e){
				e.printStackTrace();
			}
			return is;
		}
	}

	private void launchSQLproc(final int entiteId, final List<Integer> o) throws SQLException, InterruptedException{

		if(Thread.interrupted()){
			throw new InterruptedException();
		}

		try{
			String create = "";
			String fill = "";
			if(entiteId == 1){ // PATIENT
            boolean exportAnonyme = profilExport != null && !profilExport.equals(ProfilExport.NOMINATIF);

            switch(ctx) {
               case GATSBI:
                  create = exportAnonyme ? "{call create_tmp_anonyme_table_gatsbi(" + etudeId + ")}" 
                                       : "{call create_tmp_patient_table_gatsbi(" + etudeId + ")}";
                  fill = (exportAnonyme ? "{call fill_tmp_anonyme_table_gatsbi(?, " 
                        : "{call fill_tmp_table_patient_gatsbi(?, ")  
                     .concat(collections.get(0).getBanqueId().toString())
                     .concat(", ")
                     .concat(etudeId.toString())
                     .concat(")}");
                  break;
               default:
      				create = exportAnonyme ? initPatientTableAnonymeSQL() : "{call create_tmp_patient_table()}";
      				fill = exportAnonyme ? "{call fill_tmp_table_patient_anonyme(?)}" : "{call fill_tmp_table_patient(?)}";
            }
			} else if (entiteId == 7) { // MALADIE
				switch(ctx)	{
					case SEROLOGIE:
						create = "{call create_tmp_maladie_table_sero()}";
						fill = "{call fill_tmp_table_maladie_sero(?)}";
						break;
					case GATSBI:
                  create = "{call create_tmp_maladie_table_gatsbi(" + etudeId + ")}";
                  fill = "{call fill_tmp_table_maladie_gatsbi(?, " + etudeId + ")}";
                  break;
					default:
						create = "{call create_tmp_maladie_table()}";
						fill = "{call fill_tmp_table_maladie(?)}";
				}
			} else if (entiteId == 2) { // PRELEVEMENT
				switch(ctx) {
					case SEROLOGIE:
						create = "{call create_tmp_prel_sero_table()}";
						fill = "{call fill_tmp_table_prel_sero(?)}";
						break;
					case GATSBI:
                  create = "{call create_tmp_prelevement_table_gatsbi(" + etudeId + ")}";
                  fill = "{call fill_tmp_table_prel_gatsbi(?, " + etudeId + ")}";
                  break;
					default:
						create = "{call create_tmp_prelevement_table()}";
						fill = "{call fill_tmp_table_prel(?)}";
				}
			} else if (entiteId == 3) { // ECHANTILLON
				switch(ctx){
					case SEROLOGIE:
						create = "{call create_tmp_echan_table_sero()}";
						fill = "{call fill_tmp_table_echan_sero(?)}";
						break;
					case GATSBI:
                  create = "{call create_tmp_echantillon_table_gatsbi(" + etudeId + ")}";
                  fill = "{call fill_tmp_table_echan_gatsbi(?, " + etudeId + ")}";
                  break;
					default:
						create = "{call create_tmp_echantillon_table()}";
						fill = "{call fill_tmp_table_echan(?)}";
				}
			} else if (entiteId == 8) { // DERIVE
				create = "{call create_tmp_derive_table()}";
				fill = "{call fill_tmp_table_derive(?)}";
			}else if(entiteId == 5){ // CESSION
				create = "{call create_tmp_cession_table()}";
				fill = "{call fill_tmp_table_cession(?)}";
			}

			// CREATION DE LA TABLE TEMPORAIRE
			if(getStatement() == null){
				setStatement(getConnection().createStatement());
			}
			if(getStatement() != null){ // && !getStatement().isClosed()) {
				getStatement().executeUpdate(create);
			}

			List<Integer> ids = new ArrayList<Integer>();
			//TK-320 : apparamment o est toujours null (cf appel de export_xxx dans load_sequences()) :
			if(o == null){
				ids.addAll(objsId);
			}else{
				ids.addAll(o);
			}

			int j = 1;
			
			// REMPLISSAGE DE LA TABLE TEMP
			setPreparedStatement(getConnection().prepareCall(fill));
			
			int count = 0;
			for(final Integer i : ids){

				getPreparedStatement().setInt(1, i.intValue());
				getPreparedStatement().addBatch();
				// saveOperation((Patient) objs.get(i));
				setExportDetails(progress, j, o == null ? total : o.size(), null, null, null);
				j++;
				if(++count % batchSize == 0){
					getPreparedStatement().executeBatch();
					getPreparedStatement().clearBatch();
				}
			}
			// long startTime = System.nanoTime();
			// ---------------------------------------------- //
			getPreparedStatement().executeBatch();
			getPreparedStatement().close();
			getConnection().commit();
			// ---------------------------------------------- //
			//	long endTime = System.nanoTime();
			//	System.out.println("Total elapsed time in execution of method is :"
			//			+ ((double) (endTime - startTime) / 1000000000.0));

	       
		}
		catch(BatchUpdateException sqlException) {
         throw new SQLTooManyAnnotationException(sqlException);//TK-321 : permet de gérer l'erreur "Row 32 was cut by GROUP_CONCAT()" lié à un trop grand nombre d'annotation 
		}
		catch(final Exception e){
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
						
			throw e;
		}
	}

	public int getTotal(){
		return total;
	}

	public short getExportType(){
		return exportType;
	}

	public HtmlMacroComponent getProgressBarComponent(){
		return progressBarComponent;
	}

	/**
	 * Ferme le thread. Commande envoyée depuis la progress bar
	 */
	public void closeFromBar(){
		closeAll(false);
	}

	public ResultSet getMainRset(){
		return mainRset;
	}

	public void setMainRset(final ResultSet m){
		this.mainRset = m;
	}

	public ResultSet getLaboRset(){
		return laboRset;
	}

	public void setLaboRset(final ResultSet l){
		this.laboRset = l;
	}

	public ResultSet getRetourEchanRset(){
		return retourEchanRset;
	}

	public void setRetourEchanRset(final ResultSet r){
		this.retourEchanRset = r;
	}

	public ResultSet getDeriveRetourRset(){
		return deriveRetourRset;
	}

	public void setDeriveRetourRset(final ResultSet rd){
		this.deriveRetourRset = rd;
	}

	public PreparedStatement getPreparedStatement(){
		return preparedStatement;
	}

	public ResultSet getIntermRset(){
		return intermRset;
	}

	public void setIntermRset(final ResultSet i){
		this.intermRset = i;
	}

	public void setPreparedStatement(final PreparedStatement pst){
		this.preparedStatement = pst;
	}

	public Statement getStatement(){
		return statement;
	}

	public void setStatement(final Statement s){
		this.statement = s;
	}

	public Connection getConnection(){
		return connection;
	}

	public void setConnection(final Connection c){
		connection = c;
	}

	public Desktop getDesktop(){
		return desktop;
	}

	public void setDesktop(final Desktop d){
		this.desktop = d;
	}

	public SXSSFWorkbook getWb(){
		return wb;
	}

	public void setWb(final SXSSFWorkbook w){
		this.wb = w;
	}

	public ByteArrayOutputStream getOutStr(){
		return outStr;
	}

	public List<Integer> getRestrictedAnnosTableIds(){
		return restrictedAnnosTableIds;
	}

	public void setRestrictedAnnosTableIds(final List<Integer> rI){
		this.restrictedAnnosTableIds = rI;
	}

	public Cession getCession(){
		return cession;
	}

	public void setCession(final Cession cession){
		this.cession = cession;
	}

	public Contexte getContexte(){
		return contexte;
	}

	public void setContexte(final Contexte contexte){
		this.contexte = contexte;
	}

	public boolean isCsv(){
		return csv;
	}

	public void setCsv(final boolean csv){
		this.csv = csv;
	}
	
	public EContexte getCtx(){
		return ctx;
	}
}