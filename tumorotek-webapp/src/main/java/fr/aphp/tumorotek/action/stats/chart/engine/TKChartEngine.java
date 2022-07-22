package fr.aphp.tumorotek.action.stats.chart.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DataUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

import fr.aphp.tumorotek.action.stats.charts.ChartColors;

/**
 * ChartEngine permettant la customization du dession de
 * graphes JfreeChart de type bar et chart.
 *
 * @author Mathieu BARTHELEMY
 * @author Marc DESCHAMPS
 *
 */
public class TKChartEngine extends JFreeChartEngine
{

   private static final long serialVersionUID = 1L;

   private final ChartColors colors = new ChartColors(1.0f);

   private Color[] colorSet = colors.getColorsSet();

   @Override
   public boolean prepareJFreeChart(final JFreeChart jfchart, final Chart chart){

      final TextTitle tt = new TextTitle(chart.getTitle() != null ? chart.getTitle() : "", new Font("Arial", Font.BOLD, 14));
      tt.setPosition(RectangleEdge.TOP);
      tt.setHorizontalAlignment(HorizontalAlignment.LEFT);
      tt.setMargin(0.0, 10, 0.0, 0.0);
      jfchart.setTitle(tt);

      if(chart.getType().equals("bar")){
         customizeCategoryPlot(jfchart);
      }else if(chart.getType().equals("pie")){
         customizePiePlot(jfchart);
      }else if(chart.getType().equals("stacked_bar")){
         customizeCategoryStackedPlot(jfchart);
      }

      return false;
   }

   private void customizeCategoryPlot(final JFreeChart jfchart){
      final CategoryPlot plot = jfchart.getCategoryPlot();

      //plot.setBackgroundPaint(new Color(221,223,238));
      // plot.setBackgroundPaint(Color.white);
      // plot.setDomainGridlinePaint(Color.white);
      // plot.setDomainGridlinesVisible(true);
      // plot.setRangeGridlinePaint(Color.white);

      final NumberFormat currency = NumberFormat.getInstance(Locale.FRENCH);
      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setNumberFormatOverride(currency);

      final CategoryItemRenderer cRenderer = new CustomRenderer();
      plot.setRenderer(cRenderer);

      cRenderer
         .setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{1} : {2}", NumberFormat.getInstance(Locale.FRENCH)));

      final BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setBarPainter(new StandardBarPainter());
      renderer.setDrawBarOutline(false);
      renderer.setShadowVisible(false);

      final CategoryAxis domainAxis = plot.getDomainAxis();
      //Rotation
      domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 7.0));
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      domainAxis.setCategoryMargin(0.05);
   }

   private void customizePiePlot(final JFreeChart jfchart){
      final PiePlot piePlot = (PiePlot) jfchart.getPlot();
      piePlot.setLabelBackgroundPaint(Color.white);

      final Paint[] colors = getColorSet();
      final DefaultDrawingSupplier defaults = new DefaultDrawingSupplier();
      piePlot.setDrawingSupplier(new DefaultDrawingSupplier(colors, new Paint[] {defaults.getNextFillPaint()},
         new Paint[] {defaults.getNextOutlinePaint()}, new Stroke[] {defaults.getNextStroke()},
         new Stroke[] {defaults.getNextOutlineStroke()}, new Shape[] {defaults.getNextShape()}));

      piePlot.setShadowPaint(null);

      piePlot.setSectionOutlinesVisible(false);

      piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}"));

      piePlot.setToolTipGenerator(new StandardPieToolTipGenerator(Locale.FRENCH));

      piePlot.setMaximumLabelWidth(0.30);

      piePlot.setInteriorGap(0.02);

      piePlot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);

      jfchart.getPlot().setOutlineVisible(false);
   }

   private void customizeCategoryStackedPlot(final JFreeChart jfchart){
      final CategoryPlot plot = jfchart.getCategoryPlot();

      //plot.setBackgroundPaint(new Color(221,223,238));
      // plot.setBackgroundPaint(Color.white);
      // plot.setDomainGridlinePaint(Color.white);
      // plot.setDomainGridlinesVisible(true);
      // plot.setRangeGridlinePaint(Color.white);

      final NumberFormat currency = NumberFormat.getInstance(Locale.FRENCH);
      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setNumberFormatOverride(currency);

      final CategoryItemRenderer c1 = new CustomStackedRenderer();
      plot.setRenderer(0, c1);

      c1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator()
      {
         private static final long serialVersionUID = 1L;

         @Override
         protected Object[] createItemArray(final CategoryDataset dataset, final int row, final int column){
            final Object[] result = new Object[5];
            result[0] = dataset.getRowKey(row).toString();
            result[1] = dataset.getColumnKey(column).toString();
            final Number value = dataset.getValue(row, column);
            if(value != null){
               result[2] = NumberFormat.getInstance(Locale.FRENCH).format(value);
            }else{
               result[2] = "";
            }
            if(value != null){
               final double total = DataUtilities.calculateColumnTotal(dataset, column);
               final double percent = value.doubleValue() / total;
               result[3] = NumberFormat.getInstance(Locale.FRENCH).format(percent);
               result[4] = NumberFormat.getInstance(Locale.FRENCH).format(total);
            }

            return result;
         }

         @Override
         public String generateToolTip(final CategoryDataset dataset, final int series, final int item){
            final Object[] items = createItemArray(dataset, series, item);
            String result;
            if(series == 0){ // tkobj stockes
               result = MessageFormat.format(Labels.getLabel("charts.engine.tooltip.stock"), items);
            }else{
               result = MessageFormat.format(Labels.getLabel("charts.engine.tooltip.nonstock"), items);
            }
            return result;
         }
      });

      final BarRenderer r1 = (StackedBarRenderer) plot.getRenderer(0);
      r1.setBarPainter(new StandardBarPainter());
      r1.setDrawBarOutline(false);
      r1.setShadowVisible(false);

      r1.setSeriesPaint(1, Color.GRAY);

      final CategoryAxis domainAxis = plot.getDomainAxis();
      //Rotation
      domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 7.0));
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      domainAxis.setCategoryMargin(0.05);
   }

   public class CustomRenderer extends BarRenderer
   {

      private static final long serialVersionUID = 1L;

      private final Paint[] colors;

      int transparency = 95;

      public CustomRenderer(){
         this.colors = getColorSet();
      }

      @Override
      public Paint getItemPaint(final int row, final int column){
         return this.colors[column % this.colors.length];
      }
   }

   public class CustomStackedRenderer extends StackedBarRenderer
   {

      private static final long serialVersionUID = 1L;

      private final Color[] colorSet = getColorSet();

      private final Color[] colorSetA = colors.addAlphaToColorSet(getColorSet(), 128);

      @Override
      public Paint getItemPaint(final int series, final int category){
         if(this.colorSet.length > 0){
            if(series == 0){
               return this.colorSet[category % this.colorSet.length];
            }else{
               return this.colorSetA[category % this.colorSetA.length];

            }
         }else{ // default color
            return new Color(0f, 0f, 0f, 1);

         }
      }
   }

   public Color[] getColorSet(){
      return colorSet;
   }

   public void setColorSet(final Color[] cs){
      this.colorSet = cs;
   }

   public ChartColors getColors(){
      return colors;
   }
}
