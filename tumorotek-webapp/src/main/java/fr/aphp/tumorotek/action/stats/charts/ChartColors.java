package fr.aphp.tumorotek.action.stats.charts;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;

public class ChartColors
{

   private Float alpha = 1.0f;

   public ChartColors(final Float transp){
      alpha = transp;
   }

   public static String toHtmlColor(final Color color){
      return "#" + toHexColor(color);
   }

   public static String toHexColor(final Color color){
      return StringUtils.leftPad(Integer.toHexString(color.getRGB() & 0xFFFFFF), 6, '0');
   }

   public Color[] getColorsSet(){

      // String hexTr = decodealpha();

      // 16 VGA colors
      //Color COLOR_0 = Color.decode("#" + hexTr + "000000");
      final Color COLOR_0 = new Color(0f, 0f, 0f, alpha);
      //Color COLOR_1 = Color.decode("#" + hexTr + "000080");
      final Color COLOR_1 = new Color(0, 0, 0.5f, alpha);
      // Color COLOR_2 = Color.decode("#" + hexTr + "008000");
      final Color COLOR_2 = new Color(0, 0.5f, 0, alpha);
      // Color COLOR_3 = Color.decode("#" + hexTr + "008080");
      final Color COLOR_3 = new Color(0, 0.5f, 0.5f, alpha);
      //Color COLOR_4 = Color.decode("#" + hexTr + "800000");
      final Color COLOR_4 = new Color(0.5f, 0, 0, alpha);
      //Color COLOR_5 = Color.decode("#" + hexTr + "800080");
      final Color COLOR_5 = new Color(0.5f, 0, 0.5f, alpha);
      //Color COLOR_6 = Color.decode("#" + hexTr + "808000");
      final Color COLOR_6 = new Color(0.5f, 0.5f, 0, alpha);
      //Color COLOR_7 = Color.decode("#" + hexTr + "C0C0C0");
      final Color COLOR_7 = new Color(0.75f, 0.75f, 0.75f, alpha);
      //Color COLOR_8 = Color.decode("#" + hexTr + "808080");
      final Color COLOR_8 = new Color(0.5f, 0.5f, 0.5f, alpha);
      //Color COLOR_9 = Color.decode("#" + hexTr + "0000FF");
      final Color COLOR_9 = new Color(0, 0, 1.0f, alpha);
      //Color COLOR_10 = Color.decode("#" + hexTr + "00FF00");
      final Color COLOR_10 = new Color(0, 1.0f, 0, alpha);
      //Color COLOR_11 = Color.decode("#" + hexTr + "00FFFF");
      final Color COLOR_11 = new Color(0, 1.0f, 1.0f, alpha);
      //Color COLOR_12 = Color.decode("#" + hexTr + "FF0000");
      final Color COLOR_12 = new Color(1.0f, 0, 0, alpha);
      //Color COLOR_13 = Color.decode("#" + hexTr + "FF00FF");
      final Color COLOR_13 = new Color(1.0f, 0, 1.0f, alpha);
      //Color COLOR_14 = Color.decode("#" + hexTr + "FFFF00");
      final Color COLOR_14 = new Color(1.0f, 1.0f, 0, alpha);
      //Color COLOR_15 = Color.decode("#" + hexTr + "FDE8D7");
      final Color COLOR_15 = new Color(0.992f, 0.91f, 0.843f, alpha);

      return new Color[] {COLOR_1, COLOR_2, COLOR_3, COLOR_4, COLOR_5, COLOR_6, COLOR_7, COLOR_8, COLOR_9, COLOR_10, COLOR_11,
         COLOR_12, COLOR_13, COLOR_14, COLOR_15, COLOR_0};
   }

   public Color getColorAtIndex(int idx){
      final Color[] colors = getColorsSet();
      if(idx >= colors.length){
         while(idx >= colors.length){
            idx = idx - colors.length;
         }
      }
      return colors[idx];
   }

   public Color[] addAlphaToColorSet(final Color[] paints, final int alpha){
      final Color[] ps = new Color[paints.length];
      for(int i = 0; i < paints.length; i++){
         final Color c = new Color(paints[i].getRed(), paints[i].getGreen(), paints[i].getBlue(), alpha);
         ps[i] = c;
      }
      return ps;
   }
}
