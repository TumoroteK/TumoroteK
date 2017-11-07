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
package fr.aphp.tumorotek.action.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.code.CodeAssigneDecorator;
import fr.aphp.tumorotek.action.code.CodeAssigneEditableGrid;
import fr.aphp.tumorotek.action.controller.AbstractController;
import fr.aphp.tumorotek.action.controller.AbstractFicheController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.utils.Utils;
import fr.aphp.tumorotek.webapp.general.SessionUtils;


/**
 * Composant présentant le formulaire permettant de 
 * requeter la création du fichier excel représentant 
 * le bilan d'activité Inca.
 * Date: 03/12/2010
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class IncaReport extends AbstractFicheController {

	private static final long serialVersionUID = 1L;
	
	private Datebox d1box;
	private Datebox d2box;
	private Intbox intervBox;
	private Listbox collectionsBox;
	private Listbox etabsBox;
	private Listbox consentsBox;
	private Listbox infosBox;
	
	private Datebox d1Orgbox;
	private Datebox d2Orgbox;
	private Listbox collectionsOrgBox;
	private Listbox naturesSainBox;
	private Listbox echanTypeSainBox;
	private Listbox naturesSangBox;
	private Listbox echanTypeSangBox;
	private Listbox consentsOrgBox;
	
	private Checkbox datePrelBox;
	
	private List<Banque> banques = new ArrayList<Banque>();
	private List<Nature> natures = new ArrayList<Nature>();
	private List<EchantillonType> echanTypes = new ArrayList<EchantillonType>();
	private List<ConsentType> consents = new ArrayList<ConsentType>();
	private List<Etablissement> etabs = new ArrayList<Etablissement>();
	private List<CimMaster> cimsRequested = new ArrayList<CimMaster>();

	public List<Banque> getBanques() {
		return banques;
	}
	
	public List<Nature> getNatures() {
		return natures;
	}

	public List<EchantillonType> getEchanTypes() {
		return echanTypes;
	}

	public List<ConsentType> getConsents() {
		return consents;
	}

	public List<Etablissement> getEtabs() {
		return etabs;
	}

	public List<CimMaster> getCimsRequested() {
		return cimsRequested;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		banques = ManagerLocator.getUtilisateurManager()
			.getAvailableBanquesAsAdminManager(SessionUtils
											.getLoggedUser(sessionScope));
		
		natures = (List<Nature>) ManagerLocator.getNatureManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		echanTypes = (List<EchantillonType>) 
			ManagerLocator.getEchantillonTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		consents = (List<ConsentType>) ManagerLocator.getConsentTypeManager()
			.findByOrderManager(SessionUtils.getPlateforme(sessionScope));
		etabs = ManagerLocator.getEtablissementManager()
													.findAllObjectsManager();
		
		getCodesCimController().setIsOrg(false);
		getCodesCimController().setToCims(true);
				
		getBinder().loadAll();
	}
	
	/**
	 * Bouton de validation du formulaire.
	 * Génération du rapport 1.
	 */
	public void onClick$report1() {
		
		if (d1box.getValue() != null 
					&& intervBox.getValue() != null) {
		
			Clients.showBusy(null);
			Events.echoEvent("onLaterExport1", self, null);
		}
	}
	
	/**
	 * Bouton de validation du formulaire.
	 * Génération du rapport 2.
	 */
	public void onClick$report2() {
		
		if (d1Orgbox.getValue() != null) {
		
			Clients.showBusy(null);
			Events.echoEvent("onLaterExport2", self, null);
		}
	}
	
	public void onLaterExport1() {
		
		HSSFWorkbook wb = createIncaReportWorkBook(1);
		
		// ferme wait message
		Clients.clearBusy();
		
		// download du fichier excell
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		String date = new SimpleDateFormat("yyyyMMddHHmm")
			.format(cal.getTime());
		sb.append(ObjectTypesFormatters
				.getLabel("inca.report.filename", 
				new String[]{date}));
		sb.append(".xls");
		
		AbstractController.downloadExportFile(wb, sb.toString());
	}
	
	public void onLaterExport2() {
		
		HSSFWorkbook wb = createIncaReportWorkBook(2);
		
		// ferme wait message
		Clients.clearBusy();
		
		// download du fichier excell
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		String date = new SimpleDateFormat("yyyyMMddHHmm")
			.format(cal.getTime());
		sb.append(ObjectTypesFormatters
				.getLabel("inca.report.filename.organe", 
				new String[]{date}));
		sb.append(".xls");
		
		AbstractController.downloadExportFile(wb, sb.toString());
	}
	
	/**
	 * Crée le fichier Excel contenant les feuilles du rapport d'activité.
	 * @param report int spécifiant quel rapport doit être dessiné.
	 * @return HSSFWorkbook le fichier excel
	 */
	@SuppressWarnings("unchecked")
	private HSSFWorkbook createIncaReportWorkBook(int report) {
		
		HSSFWorkbook wb = null;
		
		if (collectionsBox.getSelectedItems() != null) {
			
			// création de la feuille excell
			wb = new HSSFWorkbook();
		
			List<Banque> cumuls = new ArrayList<Banque>();
			List<Banque> single = new ArrayList<Banque>();
			Iterator<Listitem> listIts; 
			if (report == 1) {
				listIts = collectionsBox.getSelectedItems().iterator();
			} else if (report == 2) {
				listIts = collectionsOrgBox.getSelectedItems().iterator();
			} else  {
				throw new IllegalArgumentException("report must 1 or 2");
			}
			Banque next;
			while (listIts.hasNext()) {
				next = (Banque) listIts.next().getValue();
				cumuls.add(next);
				single.add(next);
				
				if (report == 1) {
					drawOneSheetReport1(wb, single);
				} else if (report == 2) {
					drawOneSheetReport2(wb, single);
				}
				single.clear();
			}
			
			// dessine une feuille pour le cumulatif
			if (cumuls.size() > 1) {
				if (report == 1) {
					drawOneSheetReport1(wb, cumuls);
				} else if (report == 2) {
					drawOneSheetReport2(wb, cumuls);
				} else  {
					throw new IllegalArgumentException("report must 1 or 2");
				}
			}
		}
		
		return wb;
	}
		
	/**
	 * Dessine une feuille dans le bilan d'activité numéro 1 
	 * pour la liste de banque passée en paramètre.
	 * Si la liste ne contient qu'une banque alors le nom de 
	 * la feuille prend le nom de la banque.
	 * @param wb
	 * @param banks
	 */
	@SuppressWarnings("unchecked")
	private void drawOneSheetReport1(HSSFWorkbook wb, List<Banque> banks) {
		
		if (banks != null && !banks.isEmpty()) {
			String sheetname;
			if (banks.size() == 1) {
				sheetname = banks.get(0).getNom();
			} else {
				sheetname = "Total";
			}
			HSSFSheet sheet = wb.createSheet(sheetname);
			
			// ajout des entêtes avec les dates
			HSSFRow headersRow = sheet.createRow(0);
			
			if (d2box.getValue() == null) {
				d2box.setValue(Utils.getCurrentSystemDate());
			}
			
			// cellule vide
			headersRow.createCell((short) 0);
			List<Date> dates = createDateHeaders(d1box.getValue(), 
					d2box.getValue(), intervBox.getValue());
			HSSFCell cell;
			//HSSFCellStyle style;
		    //HSSFDataFormat format = wb.createDataFormat();
		    //style = wb.createCellStyle();
			//style.setDataFormat(format.getFormat("m/d/yy"));
		    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			
			for (int i = 0; i < dates.size(); i++) {		
				cell = headersRow.createCell((short) (i + 1));
				cell.setCellValue(new HSSFRichTextString(format
											.format(dates.get(i))));
				//cell.setCellStyle(style);
			}
			
			// dessine chaque ligne
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(d1box.getValue());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(d2box.getValue());
			List<Etablissement> selEtabs = (List<Etablissement>) 
										getSelectedFromListbox(etabsBox);
			List<ConsentType> cTypes = (List<ConsentType>)
									getSelectedFromListbox(consentsBox);
			List<ConsentType> infoTypes = (List<ConsentType>)
										getSelectedFromListbox(infosBox);
			
			List<Long> counts;
			// Patient
			HSSFRow patRow1 = sheet.createRow(1);
			counts = ManagerLocator.getIncaReportManager()
				.countPrelevedByDatesManager(cal1, cal2, intervBox.getValue(), 
										banks, false, datePrelBox.isChecked());
			drawOneReportRow(patRow1, "inca.report.nouveauDosPatient", 
															true, counts);
			HSSFRow patRow2 = sheet.createRow(2);
			counts = ManagerLocator.getIncaReportManager()
				.countPrelevedByDatesExtManager(cal1, cal2, 
						intervBox.getValue(), banks, selEtabs, 
						datePrelBox.isChecked());
			drawOneReportRow(patRow2, "inca.report.extDosPatient", 
														true, counts);
			HSSFRow patRow3 = sheet.createRow(3);
			counts = ManagerLocator.getIncaReportManager()
				.countPrelevedByDatesManager(cal1, cal2, intervBox.getValue(), 
										banks, true, datePrelBox.isChecked());
			drawOneReportRow(patRow3, "inca.report.dosPatientTotal", 
														true, counts);
			
			HSSFRow patRow4 = sheet.createRow(4);
			counts = ManagerLocator.getIncaReportManager()
				.countEclConsentByDatesManager(infoTypes, cal1, cal2, 
									intervBox.getValue(), banks);
			drawOneReportRow(patRow4, "inca.report.informations", 
													true, counts);
			
			HSSFRow patRow5 = sheet.createRow(5);
			counts = ManagerLocator.getIncaReportManager()
				.countEclConsentByDatesManager(cTypes, cal1, cal2, 
									intervBox.getValue(), banks);
			drawOneReportRow(patRow5, "inca.report.consentEclaires", 
													true, counts);
			
			// Echantillons
			HSSFRow echRow1 = sheet.createRow(6);
			counts = ManagerLocator.getIncaReportManager()
				.countSamplesManager(cal1, cal2, intervBox.getValue(), 
																banks, false);
			drawOneReportRow(echRow1, "inca.report.nouveauEchanCong", 
																true, counts);
			
			HSSFRow echRow2 = sheet.createRow(7);
			counts = ManagerLocator.getIncaReportManager()
				.countSamplesExtManager(cal1, cal2, intervBox.getValue(), 
															banks, selEtabs);
			drawOneReportRow(echRow2, "inca.report.extEchanCong", true, counts);
							
			HSSFRow echRow3 = sheet.createRow(8);
			counts = ManagerLocator.getIncaReportManager()
				.countEchansByCessTypesManager(ManagerLocator
					.getCessionTypeManager()
						.findByTypeLikeManager("Sanitaire", true).get(0), 
					d1box.getValue(), d2box.getValue(), intervBox.getValue(), 
							banks);
			drawOneReportRow(echRow3, "inca.report.echansCessSan", 
																true, counts);
			
			HSSFRow echRow4 = sheet.createRow(9);
			counts = ManagerLocator.getIncaReportManager()
				.countEchansByCessTypesManager(ManagerLocator
						.getCessionTypeManager()
					.findByTypeLikeManager("Recherche", true).get(0), 
				d1box.getValue(), d2box.getValue(), intervBox.getValue(), 
						banks);
			drawOneReportRow(echRow4, "inca.report.echansCessRech", 
																true, counts);
			
			HSSFRow echRow5 = sheet.createRow(10);
			counts = ManagerLocator.getIncaReportManager()
			.countEchansByCessTypesManager(ManagerLocator
				.getCessionTypeManager()
					.findByTypeLikeManager("Destruction", true).get(0), 
				d1box.getValue(), d2box.getValue(), intervBox.getValue(), 
						banks);
			drawOneReportRow(echRow5, "inca.report.echansCessDest", 
																true, counts);
			
			HSSFRow echRow6 = sheet.createRow(11);
			counts = ManagerLocator.getIncaReportManager()
				.countSamplesManager(cal1, cal2, 
										intervBox.getValue(), banks, true);
			drawOneReportRow(echRow6, "inca.report.echansTot", true, counts);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List< ? extends Object> getSelectedFromListbox(Listbox box) {
		List<Object> objs = new ArrayList<Object>();
		Iterator<Listitem> listIt;
		if (box.getSelectedItems() != null) {
			listIt = box.getSelectedItems().iterator();
			while (listIt.hasNext()) {
				objs.add(listIt.next().getValue());
			}
		}
		return objs;
		
	} 
	
	/**
	 * Crée la liste de headers pour le tableau report 1 comprenant les 
	 * dates limites supérieures pour chaque intervalle de dates calculé.
	 * @param d1
	 * @param d2
	 * @param interv
	 * @return
	 */
	private List<Date> createDateHeaders(Date d1, Date d2, Integer interv) {
		List<Date> headers = new ArrayList<Date>();
		long ms = 86400000;
		Long jourInMs = ms * interv;
		Date interm1 = new Date(d1.getTime());
		// la borne supérieure devient exclusive si on retire 1 jour
		Date interm2 = new Date(d1.getTime() + jourInMs - ms);

		// ajoute l'intervalle pour réaliser chaque compte
		while (interm2.before(d2) || interm2.equals(d2)) {
			headers.add(new Date(interm2.getTime()));
			interm1.setTime(interm2.getTime() + ms);
			interm2.setTime(interm2.getTime() + jourInMs);
		}
						
		// ajoute le dernier intervalle au besoin pour compléter
		if (interm1.before(d2)) {
			headers.add(d2);
		}
		
		return headers;
	}
	
	/**
	 * Dessine une feuille dans le bilan d'activité numéro 2 
	 * pour la liste de banque passée en paramètre.
	 * Si la liste ne contient qu'une banque alors le nom de 
	 * la feuille prend le nom de la banque.
	 * @param wb
	 * @param banks
	 */
	@SuppressWarnings("unchecked")
	private void drawOneSheetReport2(HSSFWorkbook wb, List<Banque> banks) {
		
		if (banks != null && !banks.isEmpty()) {
			String sheetname;
			if (banks.size() == 1) {
				sheetname = banks.get(0).getNom();
			} else {
				sheetname = "Total";
			}
			HSSFSheet sheet = wb.createSheet(sheetname);
			HSSFCell cell;
			
			//date
			if (d2Orgbox.getValue() == null) {
				d2Orgbox.setValue(Utils.getCurrentSystemDate());
			}
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(d1Orgbox.getValue());
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(d2Orgbox.getValue());
			
			HSSFRow dateRow = sheet.createRow(0);
			dateRow.createCell((short) 0);
			cell = dateRow.createCell((short) 1);
			HSSFCellStyle cellStyle = wb.createCellStyle();
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cell.setCellStyle(cellStyle);
	        	
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			cell.setCellValue(new HSSFRichTextString(format
				.format(cal1.getTime()) 
						+ " - " + format.format(cal2.getTime())));
			Region reg = new Region(0, (short) 1, 0, (short) 7);         
			sheet.addMergedRegion(reg);
			
			// ajout des entêtes
			HSSFRow headersRow = sheet.createRow(1);
			
			// cellule vide
			headersRow.createCell((short) 0);
			String[] headersKey = new String[] {"inca.report2.nbTot", 
												"inca.report2.nbColl",
												"inca.report2.sainAssoc",
												"inca.report2.sangAssoc",	
												"inca.report2.consentEclaires",
												"inca.report2.donneesAsso"};
			
			for (int i = 0; i < headersKey.length; i++) {		
				cell = headersRow.createCell((short) (i + 1));
				cell.setCellValue(new HSSFRichTextString(Labels
												.getLabel(headersKey[i])));
			}

			// dessine chaque ligne
			List<Nature> sainNatsAss = new ArrayList<Nature>();
			List<EchantillonType> sainTypesAss = 
								new ArrayList<EchantillonType>();
			List<Nature> sangNatsAss = new ArrayList<Nature>();
			List<EchantillonType> sangTypesAss = 
								new ArrayList<EchantillonType>();
			List<ConsentType> consentAss = new ArrayList<ConsentType>();
			
			sainNatsAss = (List<Nature>) getSelectedFromListbox(naturesSainBox);
			sainTypesAss = (List<EchantillonType>) 
									getSelectedFromListbox(echanTypeSainBox);
			sangNatsAss = (List<Nature>) getSelectedFromListbox(naturesSangBox);
			sangTypesAss = (List<EchantillonType>) 
									getSelectedFromListbox(echanTypeSangBox);
			consentAss = (List<ConsentType>) 
										getSelectedFromListbox(consentsOrgBox);
			
			// codes CIM
			getCimsRequested().clear();
		
			List<CodeAssigne> codesAss = CodeAssigneDecorator
				.undecorateListe(getCodesCimController()
													.getObjToCreateOrEdit());
			CimMaster cim;
			for (int i = 0; i < codesAss.size(); i++) {
				if (codesAss.get(i).getTableCodage() != null
					&& codesAss.get(i).getTableCodage()
											.getNom().equals("CIM_MASTER")) {
					cim = ManagerLocator.getCimMasterManager()
						.findByIdManager(codesAss.get(i).getCodeRefId());
					if (cim != null) {
						getCimsRequested().add(cim);
					}
				}
			}
			
			List<Long> counts = null;
			HSSFRow row;
			int i;
			for (i = 0; i < getCimsRequested().size(); i++) {
				row = sheet.createRow(i + 2);
				counts = ManagerLocator.getIncaReportManager()
					.countsPrelsAndAssociatesByCimManager(getCimsRequested()
																	.get(i), 
										null, cal1, cal2, banks, sainNatsAss, 
						sainTypesAss, sangNatsAss, sangTypesAss, consentAss);
				
				drawOneReportRow(row, getCimsRequested().get(i).getCode() + " " 
					+ getCimsRequested().get(i).getLibelle(), false, counts);
			}
		}
	}
	
	/**
	 * Dessine une ligne avec entête et une suite de cellules pour chaque 
	 * compte.
	 * @param row ligne à dessiner
	 * @param entête
	 * @param il3 si le precedent param est une key il3
	 * @param counts
	 */
	private void drawOneReportRow(HSSFRow row, String key, boolean il3, 
													List<Long> counts) {
		if (il3) {
			row.createCell((short) 0)
				.setCellValue(new HSSFRichTextString(Labels.getLabel(key)));
		} else {
			row.createCell((short) 0)
				.setCellValue(new HSSFRichTextString(key));
		}
		if (counts != null) {
			HSSFCell cell;
			for (int i = 0; i < counts.size(); i++) {
				cell = row.createCell((short) (i + 1));
				if (counts.get(i) != null) {
					cell.setCellValue(counts.get(i));
				}
			}
		}
	}

	@Override
	public TKdataObject getObject() {
		return null;
	}

	@Override
	public void setNewObject() {		
	}

	@Override
	public TKdataObject getParentObject() {
		return null;
	}

	@Override
	public void setParentObject(TKdataObject obj) {		
	}
	
	public CodeAssigneEditableGrid getCodesCimController() {
		return (CodeAssigneEditableGrid) 
			self.getFellow("cimsEditor").getFirstChild()
				.getAttributeOrFellow("codesAssitGridDiv$composer", true);
	}
}
