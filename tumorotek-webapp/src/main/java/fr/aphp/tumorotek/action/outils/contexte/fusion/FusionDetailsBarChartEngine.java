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
package fr.aphp.tumorotek.action.outils.contexte.fusion;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

public class FusionDetailsBarChartEngine extends JFreeChartEngine
{

   private static final long serialVersionUID = 1L;

   public Color baseColor;

   public FusionDetailsBarChartEngine(final Color c){
      baseColor = c;
   }

   @Override
   public boolean prepareJFreeChart(final JFreeChart jfchart, final Chart chart){

      final CategoryPlot plot = jfchart.getCategoryPlot();

      final NumberFormat currency = NumberFormat.getInstance(Locale.FRENCH);
      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      rangeAxis.setNumberFormatOverride(currency);

      final BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setBarPainter(new StandardBarPainter());
      renderer.setDrawBarOutline(false);
      renderer.setShadowVisible(false);

      renderer.setSeriesPaint(0, baseColor);

      final CategoryAxis domainAxis = plot.getDomainAxis();
      //Rotation 
      // domainAxis.setCategoryLabelPositions(
      //    CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 7.0)
      //);
      domainAxis.setLowerMargin(0.01);
      domainAxis.setUpperMargin(0.01);
      domainAxis.setCategoryMargin(0.05);

      return false;
   }
}
