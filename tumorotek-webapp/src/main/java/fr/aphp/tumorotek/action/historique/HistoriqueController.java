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
package fr.aphp.tumorotek.action.historique;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.annotation.FicheAnnotation;
import fr.aphp.tumorotek.action.controller.*;
import fr.aphp.tumorotek.decorator.I3listBoxItemRenderer;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.ConsultationIntf;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.webapp.general.FileDownloadTumo;
import fr.aphp.tumorotek.webapp.general.SessionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistoriqueController extends AbstractObjectTabController {
	
	private Log log = LogFactory.getLog(HistoriqueController.class);

	private static final long serialVersionUID = 527753922586818481L;
	
	private Grid historiqueGrid;
	private Button find;
	private Button export;
	
	private Date date1;
	private Date date2;
	private List<Operation> operations = new ArrayList<Operation>();
	private List<String> operateursDates = new ArrayList<String>();
	private String operateur = "=";
	private List<OperationType> operationTypes = 
			new ArrayList<OperationType>();
	private OperationType operationType;
	private List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
	private Utilisateur selectedUtilisateur;
	private List<Utilisateur> selUsers = new ArrayList<Utilisateur>();
	private HistoriqueRenderer operationRenderer = new HistoriqueRenderer();
	
	private static I3listBoxItemRenderer typeRenderer = 
						new I3listBoxItemRenderer("nom");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setBinder(new AnnotateDataBinder(comp));
		
		if (mainBorder != null) {
			mainBorder.setHeight(getMainWindow().getPanelHeight() + "px");
		}
		
		if (winPanel != null) {
			winPanel.setHeight(getMainWindow().getPanelHeight() + "px");
		}
		
		operateursDates = new ArrayList<String>();
		operateursDates.add("=");
		operateursDates.add("<=");
		operateursDates.add(">=");
		operateursDates.add("[..]");
		
		operationTypes = ManagerLocator
			.getOperationTypeManager().findAllObjectsManager();
		
		operationTypes.add(0, null);
		operationType = null;
		
		//2.0.13
		for (Emetteur emet : ManagerLocator.getEmetteurManager().findAllObjectsManager()) {
			OperationType consultIntfType = new OperationType();
			consultIntfType.setNom(ObjectTypesFormatters
				.getLabel("interfacage.operationType.consultation", 
						new String[]{emet.getIdentification()}));
			consultIntfType.setEmetteur(emet);
			operationTypes.add(consultIntfType);
		}
		
		List<Plateforme> pfs = new ArrayList<Plateforme>();
		pfs.add(SessionUtils.getPlateforme(sessionScope));
		
		utilisateurs = ManagerLocator.getUtilisateurManager()
							.findByArchiveManager(false, pfs);
		
		utilisateurs.add(0, null);
		selectedUtilisateur = null;
		
		getBinder().loadComponent(self);
		
	}
	
	@Override
	public TKdataObject loadById(Integer id) {
		return null;
	}
	
	/**
	 * Méthode appelée lors de l'appui sur la touche ENTREE : 
	 * valide le formulaire en mode création ou validation.
	 */
	public void onOK() {
		Events.postEvent(new Event("onClick", find));
	}
	
	public void onClick$find() {
		Clients.showBusy(null);
		Events.echoEvent("onLaterFind", self, null);
	}
	
	public void onLaterFind() {
		
		operations.clear();
		
		boolean showLogin = false;
		ResourceBundle res = null;
    	if (ManagerLocator.getResourceBundleTumo()
    			.doesResourceBundleExists("tumorotek.properties")) {
    		res = ManagerLocator.getResourceBundleTumo()
    			.getResourceBundle("tumorotek.properties");
    	}
    	// on récupère la propriété définissant si on doit sauver
    	// et afficher les connexions
    	String save = null;
    	if (res.containsKey("SAUVER_CONNEXION_TK")) {
    		save = res.getString("SAUVER_CONNEXION_TK");
    	}
    	if (save != null && save.equals("true")) {
    		showLogin = true;
    	}
		
		String operateur1 = null;
		String operateur2 = null;
		Calendar cal1 = null;
		Calendar cal2 = null;
		if (operateur.equals("=")) {
			if (date1 != null) {
				cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				cal1.set(Calendar.HOUR, 0);
				cal1.set(Calendar.MINUTE, 0);
				operateur1 = ">=";
				
				cal2 = Calendar.getInstance();
				cal2.setTime(date1);
				cal2.set(Calendar.HOUR, 23);
				cal2.set(Calendar.MINUTE, 59);
				operateur2 = "<=";
			}
		} else if (operateur.equals("<=")) {
			if (date1 != null) {
				cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				cal1.set(Calendar.HOUR, 23);
				cal1.set(Calendar.MINUTE, 59);
				operateur1 = "<=";
			}
		} else if (operateur.equals(">=")) {
			if (date1 != null) {
				cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				cal1.set(Calendar.HOUR, 0);
				cal1.set(Calendar.MINUTE, 0);
				operateur1 = ">=";
			}
		} else if (operateur.equals("[..]")) {
			if (date1 != null && date2 != null) {
				cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				cal1.set(Calendar.HOUR, 0);
				cal1.set(Calendar.MINUTE, 0);
				operateur1 = ">=";
				cal2 = Calendar.getInstance();
				cal2.setTime(date2);
				cal2.set(Calendar.HOUR, 23);
				cal2.set(Calendar.MINUTE, 59);
				operateur2 = "<=";
			}
		}
		
		selUsers.clear();
		if (selectedUtilisateur != null) {
			selUsers.add(selectedUtilisateur);
		} else {
			selUsers.addAll(utilisateurs);
			selUsers.remove(0);
		}
		
		// 2.0.13.1 consultations interfacages
		if (operationType != null 
				&& operationType.getOperationTypeId() == null
				&& operationType.getEmetteur() != null) {
			List<ConsultationIntf> consults = ManagerLocator
					.getConsultationIntfManager()
					.findByUtilisateurOrEmetteurInDatesManager(selectedUtilisateur, 
							"%" + operationType.getEmetteur().getIdentification()+ "%",
							cal1, cal2);
			
			Operation op;
			for (ConsultationIntf cI : consults) {
				op = new Operation();
				op.setUtilisateur(cI.getUtilisateur());
				op.setDate(cI.getDate());
				op.setOperationType(operationType);
				op.setIdentificationDossier(cI.getIdentification());
				operations.add(op);
			}
		} else { // historique search	
			operations = ManagerLocator.getOperationManager()
				.findByMultiCriteresManager(operateur1, cal1, 
						operateur2, cal2, 
						operationType, selUsers,
						showLogin);
		}
		
		ListModel<Operation> list = new ListModelList<Operation>(operations);
		historiqueGrid.setModel(list);
		getBinder().loadComponent(historiqueGrid);
		
		if (operations.size() > 0) {
			export.setDisabled(false);
		} else {
			export.setDisabled(true);
		}
		
		Clients.clearBusy();
	}
	
	public void onClick$export() {
		Clients.showBusy(null);
		Events.echoEvent("onLaterExport", self, null);
	}
	
	public void onLaterExport() {
		HSSFWorkbook wb = ManagerLocator.getExportUtils()
			.createExcellWorkBook(Labels
					.getLabel("general.historique"));
		HSSFSheet sheet = wb.getSheetAt(0);
		
		List<String> values = new ArrayList<String>();
		values.add(Labels.getLabel("historique.resultats.recherche"));
		ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 0, values);
		
		values = new ArrayList<String>();
		values.add("");
		ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 1, values);
		
		values = new ArrayList<String>();
		values.add(Labels.getLabel("historique.utilisateur"));
		values.add(Labels.getLabel("historique.operationType"));
		values.add(Labels.getLabel("historique.entite"));
		values.add(Labels.getLabel("historique.identifiant"));
		values.add(Labels.getLabel("historique.banque"));
		values.add(Labels.getLabel("historique.date"));
		ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 2, values);
		
		for (int i = 0; i < operations.size(); i++) {
			Operation op = operations.get(i);
			values = new ArrayList<String>();
			values.add(op.getUtilisateur().getLogin());
			values.add(HistoriqueRenderer.getOperationType(op));
			values.add(HistoriqueRenderer.getEntite(op));
			values.add(HistoriqueRenderer.getIdentifiantForOperation(op));
			values.add(HistoriqueRenderer.getBanqueForOperation(op));
			values.add(ObjectTypesFormatters
					.dateRenderer2(op.getDate()));
			if (op.getV1()) {
				values.add("V1");
			}
			
			ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 
					i + 3, values);
		}
		
		// download du fichier excell
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		sb.append(Labels.getLabel("general.historique"));
		sb.append("_");
		String date = new SimpleDateFormat("yyyyMMddHHmm")
			.format(cal.getTime());
		sb.append(date);
		sb.append(".xls");
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
	
			AMedia media = new AMedia(sb.toString(), 
					"xls", 
					"application/vnd.ms-excel",
					out.toByteArray());
			FileDownloadTumo.save(media, desktop);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					out = null;
				}
			}
		}
		
		Clients.clearBusy();
	}

	@Override
	public FicheAnnotation getFicheAnnotation() {
		return null;
	}

	@Override
	public AbstractFicheCombineController getFicheCombine() {
		return null;
	}

	@Override
	public AbstractFicheEditController getFicheEdit() {
		return null;
	}

	@Override
	public AbstractFicheModifMultiController getFicheModifMulti() {
		return null;
	}

	@Override
	public AbstractFicheStaticController getFicheStatic() {
		return null;
	}

	@Override
	public AbstractListeController2 getListe() {
		return null;
	}
	
	/************************************************************/
	/************          GETTERS - SETTERS         ************/
	/************************************************************/

	public List<String> getOperateursDates() {
		return operateursDates;
	}

	public void setOperateursDates(List<String> o) {
		this.operateursDates = o;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date d) {
		this.date1 = d;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date d) {
		this.date2 = d;
	}

	public HistoriqueRenderer getOperationRenderer() {
		return operationRenderer;
	}

	public void setOperationRenderer(HistoriqueRenderer o) {
		this.operationRenderer = o;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> op) {
		this.operations = op;
	}

	public String getOperateur() {
		return operateur;
	}

	public void setOperateur(String op) {
		this.operateur = op;
	}

	public List<OperationType> getOperationTypes() {
		return operationTypes;
	}

	public void setOperationTypes(List<OperationType> o) {
		this.operationTypes = o;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType o) {
		this.operationType = o;
	}

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> u) {
		this.utilisateurs = u;
	}

	public Utilisateur getSelectedUtilisateur() {
		return selectedUtilisateur;
	}

	public void setSelectedUtilisateur(Utilisateur selected) {
		this.selectedUtilisateur = selected;
	}

	public I3listBoxItemRenderer getTypeRenderer() {
		return typeRenderer;
	}

}
