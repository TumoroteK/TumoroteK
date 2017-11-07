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
package fr.aphp.tumorotek.action.stats.charts;

import java.awt.Color;
import java.awt.Paint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.AbstractChartModel;
import org.zkoss.zul.Area;
import org.zkoss.zul.Button;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleCategoryModel;
import org.zkoss.zul.SimplePieModel;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import fr.aphp.tumorotek.action.stats.chart.engine.TKChartEngine;
import fr.aphp.tumorotek.model.stats.GraphesModele;

/**
 * MacroComponent container pour graphe JfreeChart
 * 
 * @author Marc DESCHAMPS
 * @version 2.0.13
 *
 */
public class TKChart extends HtmlMacroComponent {

	private static final long serialVersionUID = -4430614130184318712L;
	
	TKChartEngine engine = new TKChartEngine();
	GraphesModele model;
	String title = "";
	String height = "200px";
	String barType = "bar";
	String type = barType;
	Boolean zoomed = false;
	SimpleCategoryModel catModel = new SimpleCategoryModel();
	SimplePieModel pieModel = new SimplePieModel();
	
	ListModelList<CategoryDetails> categories =
			new ListModelList<CategoryDetails>();
	
	private Event onMouseOutEvent = null;
	
	@Wire
	private Chart chart;
	
	@Wire
	private Listbox categoriesList;
	
	@Wire 
	private Vlayout toolBox;
	
	@Wire 
	private Button zoom;
	
//	@Wire 
//	private Button close;
	
	@Wire 
	private Button switchType;
	
	@Wire 
	private Popup buttonsPop;
	
	@Wire
	private Listheader valueHeader;
	
	@Wire
	private Listheader nonStockHeader;

	public TKChart() {
		
		compose();
		
		// init small bar chart
		chart.setEngine(engine);
		chart.setType(type);
		valueHeader.setLabel(Labels.getLabel("charts.listheader.value"));
		nonStockHeader.setLabel(Labels.getLabel("charts.listheader.nonstock"));
		
		chart.setHeight(height);
		
		categoriesList.setHeight(height);
	}

	public GraphesModele getModel() {
		return model;
	}

	public void setModel(GraphesModele model) {
		this.model = model;
		if (model != null && model.getStacked()) {
			barType = "stacked_bar";
			type = barType;
			chart.setType(barType);

			valueHeader.setLabel(Labels.getLabel("charts.listheader.stock"));
			nonStockHeader.setVisible(true);
		}
		initModel();
	}
	
	private void initModel() {
		if (type.equals("bar")) {
			ChartData.buildCategoryModel(model, catModel);
			chart.setModel(catModel);
		} else if (type.equals("pie")) {
			ChartData.buildPieModel(model, pieModel);
			chart.setModel(pieModel);
		} else if (type.equals("stacked_bar")) {
			ChartData.buildCategoryModel(model, catModel);
			chart.setModel(catModel);
		} 
	//	((TKChartEngine) chart.getEngine())
	//		.setColorSet(ChartColors.getColorsSet());
		initCategories();
		updateWidth();
	}
	
	private void initCategories() {
		categories.clear();
		int i = 0;
		if (getModel() != null) {
			for (String key : getModel().getCountsMap().keySet()) {
				CategoryDetails cat = new CategoryDetails(key, 
						getModel().getCountsMap().get(key).get(0), 
						type.equals("stacked_bar") ? getModel().getCountsMap().get(key).get(1) : null,
						true, 
						((TKChartEngine) chart.getEngine()).getColors().getColorAtIndex(i));
				categories.add(cat);
				i++;
			}
		}
		categories.setMultiple(true);
		categories.setSelection(categories);
		categoriesList.setModel(categories);
		
	}

	@Listen("onClick=#chart")
    public void doPostTooltipText(Event event) {
		Area a = ((Area) ((MouseEvent) event).getAreaComponent());
		if (this.getParent() != null && a != null) {
			Events.postEvent("onChartAreaClick", this.getParent().getParent(), 
				a.getTooltiptext());
		}
    }
	
	@Listen("onClick=button#data")
	public void updateCategoryModel() {		
		// colors
		List<Paint> newColors = new ArrayList<Paint>();
		List<CategoryDetails> sels = 
			new ArrayList<CategoryDetails>(categories.getSelection());
		Collections.sort(sels);
		AbstractChartModel newModel = null;
		if (type.equals("bar")) { 
			newModel = new SimpleCategoryModel();
			for (CategoryDetails detail : sels) {
				((CategoryModel) newModel).setValue("main", detail.getCategory(), 
						detail.getValue());
				newColors.add(detail.getColor());
			} 
		} else if (type.equals("pie")) { 
			newModel = new SimplePieModel();
			for (CategoryDetails detail : sels) {
				((PieModel) newModel).setValue(detail.getCategory(), detail.getValue());
				newColors.add(detail.getColor());
			}
		} else if (type.equals("stacked_bar")) { 
			newModel = new SimpleCategoryModel();
			for (CategoryDetails detail : sels) {
				((CategoryModel) newModel).setValue("main", detail.getCategory(), 
						detail.getValue());
				((CategoryModel) newModel).setValue("main1", detail.getCategory(), 
						detail.getNonStockValue());
				newColors.add(detail.getColor());
			} 
		}	

		((TKChartEngine) chart.getEngine())
			.setColorSet(newColors.toArray(new Color[newColors.size()]));
		
		chart.setModel(newModel);
	}
	
	@Listen("onClick=button#switchType")
	public void switchType() {
		if (type.equals(barType)) {
			setType("pie");
			switchType.setImage("/images/chart/marginal.png");
		} else {
			setType(barType);
			switchType.setImage("/images/chart/pie.png");
		}
		updateCategoryModel();
		updateWidth();
	}
	
	@Listen("onClick=button#zoom")
	public void zoom() {
		zoomApplied();	
	}
	
	@Listen("onClick=button#close")
	public void close() {
		zoomApplied();	
	}
	
	public void zoomApplied(){
		Boolean z = !zoomed;
		setZoomed(z);
		if (z) {
			setHeight("700px");
			((Window) getParent()).setClosable(true);
			((Window) getParent()).setTitle(getTitle());
			((Window) getParent()).doModal();
		} else {
			setHeight("200px");
			((Window) getParent()).setClosable(false);
			((Window) getParent()).setTitle(null);
			((Window) getParent()).doEmbedded();		
		}	
	}
	
	
	@Listen("onClick=#excel")
	public void getDataAsExcel() {
		
		// créé et remplit le workbook
		int currentRow = 0;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = (HSSFSheet) wb.createSheet("data");
		
		HSSFRow row;
		Cell cell;
		
		// header
		row = sheet.createRow(currentRow);
		currentRow++;
		cell = row.createCell(0);
		cell.setCellType(Cell.CELL_TYPE_BLANK);
		cell = row.createCell(1);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(valueHeader.getLabel());
		if (type.equals("stacked_bar")) {
			cell = row.createCell(2);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(nonStockHeader.getLabel());
		}
		
		for (CategoryDetails detail : categories) {
			row = sheet.createRow(currentRow);
			currentRow++;
			cell = row.createCell(0);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(detail.getCategory());
			
			cell = row.createCell(1);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(detail.getValue());
			
			if (detail.getNonStockValue() != null) {
				cell = row.createCell(2);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue(detail.getNonStockValue());
			}
		}
		
		// downloadm2
		ByteArrayOutputStream outStr = new ByteArrayOutputStream();
		try {
			wb.write(outStr);
			Calendar cal = Calendar.getInstance();
			String date = 
				new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
			AMedia media = new AMedia(getTitle().replace(" ", "_") 
					+ "_" + date, "xls", "application/vnd.ms-excel",
					outStr.toByteArray());
			Filedownload.save(media);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Listen("onMouseOut=#chartDiv")
	public void closePopup(Event e) {
		if (onMouseOutEvent != null) {
			buttonsPop.close();
		} else {
			onMouseOutEvent = e;
		}
		
	}
	
	@Listen("onMouseOver=#buttonsPop")
	public void onMouseOverPopup(Event e) {
		if (onMouseOutEvent != null) {
			onMouseOutEvent.stopPropagation();
			onMouseOutEvent = null;
		}
	}
	
	@Listen("onMouseOut=#buttonsPop")
	public void onMouseOutPopup(Event e) {
		buttonsPop.close();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String t) {
		this.title = t;
		chart.setTitle(title);
	}

	public String getHeight() {
		return height;
	}

	public String getType() {
		return type;
	}

	public void setHeight(String h) {
		this.height = h;
		chart.setHeight(height);
		updateWidth();
	}
	
	private void updateWidth() {
		if (type.equals("pie")) {
			chart.setWidth(height);
		} else {
			chart.setWidth(String.valueOf(categories.size() > 10 ? 
				(!zoomed ? 35 : 50) * categories.size() : (!zoomed ? 400 : 600)) + "px");
		}
		// toolBox.setHeight(height);
	}

	public void setType(String t) {
		this.type = t;
		chart.setType(type);
	}

	public ListModelList<CategoryDetails> getCategories() {
		return categories;
	}

	public void setCategories(ListModelList<CategoryDetails> categories) {
		this.categories = categories;
	}

	public Boolean getZoomed() {
		return zoomed;
	}

	public void setZoomed(Boolean z) {
		this.zoomed = z;
		toolBox.setVisible(z);
		// close.setVisible(z);
		if (zoomed) {
			zoom.setImage("/images/chart/viewmag-.png");
		} else {
			zoom.setImage("/images/chart/viewmag+.png");
		}
	}


}
