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
package fr.aphp.tumorotek.manager.impl.etiquettes;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;

/**
 * Classe utilitaire de traduction de LigneEtiquette en instructions en RAW
 * printing languages.
 * Classe créée le 01/02/2015
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.2
 *
 */
public class RawLanguageTranslater
{

   private static Logger log = LoggerFactory.getLogger(RawLanguageTranslater.class);

   public static String printZPL(final List<LigneEtiquette> l, final int x0, final int y0, final int dpiResolution,
      BarcodeFieldDefault by){

      final StringBuffer buf = new StringBuffer();
      buf.append("^XA"); // ZPL start

      // UTF8
      buf.append("^CI28"); // ZPL start

      // HOME LOCATION
      buf.append("^LH" + String.valueOf(x0 * (dpiResolution / 72)) + "," + String.valueOf(y0 * (dpiResolution / 72)));

      int y = 0;

      if(by == null){
         by = new BarcodeFieldDefault();
      }

      for(int i = 0; i < l.size(); i++){
         final LigneEtiquette le = l.get(i);
         final String contenu = le.getContenu();
         final String entete = le.getEntete();
         // si c'est un barcode
         if(le.getIsBarcode()){
            if(contenu != null && !contenu.trim().equals("")){
               buf.append("^BY");
               buf.append(String.format("%.0f", by.getModuleWidth()));
               buf.append(",");
               buf.append(String.format("%.1f", by.getWidthRatio()));
               buf.append(",");
               buf.append(String.valueOf(by.getBarCodeHeight()));
               buf.append("^FO0," + String.valueOf(y));
               buf.append("^BCN,59,N,N,N,A^FD");
               buf.append(contenu);
               buf.append("^FS");

               y = y + 65;
            }
         }else{
            String content = "";
            if(entete != null){
               if(contenu != null){
                  content = entete.concat(contenu);
               }
            }else{
               content = contenu;
            }

            final int fontHeight = (le.getSize() != null ? le.getSize() : 6) * (dpiResolution / 72);

            buf.append(String.format("^A0N,%d^FO0,%d", fontHeight, y));
            if(!content.contains("^")){
               buf.append("^FD");
               buf.append(content);
            }else{
               buf.append("^FH^FD");
               buf.append(content.replace("^", "_5e"));
            }
            buf.append("^FS");

            y = y + fontHeight + 3;
         }
      }

      // modele texte libre
      //		if (modele != null && modele.getTexteLibre() != null) {
      //			y = y + 10;
      //			buf.append("^A0N,30,25^FO0,"  + String.valueOf(y));
      //			buf.append("^FD");
      //			buf.append(modele.getTexteLibre());
      //			buf.append("^FS");
      //		}

      buf.append("^XZ");
      log.debug(buf.toString());
      return buf.toString();
   }

   /**
    * see https://www.cab.de/media/pushfile.cfm?file=153
    * @param l
    * @param h0 label height (1/72 inches)
    * @param wO label width (1/72 inches)
    * @param y0 y general offset (1/72 inches)
    * @param x0 x general offset (1/72 inches)
    * @param int dpiResolution
    * @param by
    * @return
    */
   public static String printJSCRIPT(final List<LigneEtiquette> l, final int h0, final int w0, final int x0, final int y0,
      final int dpiResolution, BarcodeFieldDefault by){

      final StringBuffer buf = new StringBuffer();
      buf.append("e PCX;*"); // erase
      buf.append(System.lineSeparator());
      buf.append("e IMG;*");
      buf.append(System.lineSeparator());

      // units milimeters
      buf.append("mm");
      buf.append(System.lineSeparator());

      // non slashed 0
      buf.append("zO");
      buf.append(System.lineSeparator());

      // JOB START
      buf.append("J");
      buf.append(System.lineSeparator());

      // ORIENTATION p263
      // reverse + smart mode
      // buf.append("O R, P");
      buf.append("O R, P");
      buf.append(System.lineSeparator());

      // label size p270
      // l1 photocell die cut with gaps
      // buf.append("S l1;");
      // xO
      // buf.append(String.valueOf(x0 / 72) + ",");
      // y0
      // buf.append(String.valueOf(y0 / 72) + ",");
      // h0
      // buf.append(String.valueOf(h0 / 72) + ",");
      // gap 2mm = 2/25.4 into inches
      // buf.append(String.format("%.3f", (h0 / 72) + (2/25.4))  + ",");
      // w0
      // buf.append(String.valueOf(w0 / 72));
      // buf.append(" System.getProperty("line.separator")\n");

      if(by == null){
         by = new BarcodeFieldDefault();
      }

      // interligne 1mm
      final double interLigneInches = 1;
      double textSize;

      // y0 y offset 1/72 inches -> mm
      double y = y0 * (25.4 / 72);

      for(int i = 0; i < l.size(); i++){
         final LigneEtiquette le = l.get(i);
         final String contenu = le.getContenu();
         final String entete = le.getEntete();
         // si c'est un barcode p100
         // C128 p129 Subcode B
         // B x0,y0,r,CODE128,h,ne;texte
         if(le.getIsBarcode()){
            if(contenu != null && !contenu.trim().equals("")){
               // x0 mm marge gauche
               buf.append("B " + String.format("%.1f", x0 * (25.4 / 72)));
               buf.append(",");
               buf.append(String.format("%.1f", y));
               buf.append(",");
               buf.append("0");
               buf.append(",");
               buf.append("CODE128,");

               buf.append(String.valueOf(by.getBarCodeHeight()));
               buf.append(",");
               // narrow bar en dot width dots ex: 1 dot correspond 1 / 300 inches
               final double b = (by.getModuleWidth() / (double) dpiResolution) * 25.4;
               buf.append(String.format("%.3f", b));
               buf.append(";");
               buf.append(contenu);

               y = y + by.getBarCodeHeight() + 1;
            }
         }else{
            // TEXTE p273
            // T[:name;]x,y,r,font,size[,effects];text CR
            String content = "";
            if(entete != null){
               if(contenu != null){
                  content = entete.concat(contenu);
               }
            }else{
               content = contenu;
            }

            buf.append("T " + String.format("%.1f", x0 * (25.4 / 72)));
            buf.append(",");
            buf.append(String.format("%.1f", y));
            buf.append(",");
            // rotation
            buf.append("0");
            buf.append(",");
            // font
            buf.append("3");
            buf.append(",");
            // text size in pts
            // 0.375 mm = 1 point voir p275
            // 12 pt par défaut
            textSize = (le.getSize() != null ? le.getSize() : 12) * 0.375;
            buf.append(String.format("%.1f", textSize));
            // effects
            if(le.getStyle() != null){
               if(le.getStyle().equals("BOLD")){
                  buf.append(",");
                  buf.append("b");
               }else if(le.getStyle().equals("ITALIC")){
                  buf.append(",");
                  buf.append("i");
               }
            }
            buf.append(";");
            buf.append(content);

            y = y + textSize + interLigneInches;
         }
         buf.append(System.lineSeparator());
      }

      // amount of labels
      buf.append("A 1");
      buf.append("\r\n");

      log.debug(buf.toString());
      return buf.toString();
   }

}