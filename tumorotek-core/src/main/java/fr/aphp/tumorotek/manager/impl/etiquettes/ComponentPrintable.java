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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.RepaintManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import fr.aphp.tumorotek.manager.ConfigManager;
import fr.aphp.tumorotek.manager.exception.StringEtiquetteOverSizeException;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Classe permettant l'impression d'un composant.
 *
 * @author Pierre Ventadour.
 *
 */
public class ComponentPrintable implements Printable
{

   private final ComponentBarcodeLabel componentToBePrinted;

   private final PrinterJob printJob;

   private ComponentPrinter componentPrinter;

   private static Logger log = LoggerFactory.getLogger(ComponentBarcodeLabel.class);

   // private static final String BARCODE = "code barre ";
   // private static final int BARCODE_MAX_HEIGHT = 56;

   private List<LigneEtiquette> lignes = new ArrayList<>();

   private final int abscisse;

   private final int ordonnee;

   private final int maxWidth;

   private Boolean isQRCode = false;

   private final String code;

   public ComponentPrintable(final ComponentBarcodeLabel cToBePrinted, final PrinterJob pj){
      this.componentToBePrinted = cToBePrinted;
      this.printJob = pj;
      this.maxWidth = componentToBePrinted.getMaxWidth();
      this.lignes = componentToBePrinted.getLignes();
      this.abscisse = componentToBePrinted.getAbscisse();
      this.ordonnee = componentToBePrinted.getOrdonnee();
      this.isQRCode = componentToBePrinted.getIsQRCode();
      this.code = componentToBePrinted.getCode();
   }

   @Override
   public int print(final Graphics g, final PageFormat pageFormat, final int pageIndex)
      throws StringEtiquetteOverSizeException, PrinterAbortException{
      final Graphics2D g2d = (Graphics2D) g;
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      disableDoubleBuffering(componentToBePrinted);
      try{
         if(!isQRCode){
            paint(g2d, lignes, abscisse, ordonnee);
         }else{
            paintQRCode(g2d, lignes);
         }
      }catch(final Exception e){
         getComponentPrinter().setRuntimeException(e);
         printJob.cancel();
      }

      enableDoubleBuffering(componentToBePrinted);
      return Printable.PAGE_EXISTS;
   }

   public static void disableDoubleBuffering(final Component c){
      final RepaintManager currentManager = RepaintManager.currentManager(c);
      currentManager.setDoubleBufferingEnabled(false);
   }

   public static void enableDoubleBuffering(final Component c){
      final RepaintManager currentManager = RepaintManager.currentManager(c);
      currentManager.setDoubleBufferingEnabled(true);
   }

   public void paint(final Graphics2D g2d, final List<LigneEtiquette> l, final int x0, int y0)
      throws StringEtiquetteOverSizeException, IOException, OutputException{
      // --- Set the drawing color to black
      g2d.setPaint(Color.black);
      // paramètres
      final int dW = 4;
      final int dH = 4;
      // int resolution = 100;
      final int resolution = 300;
      // defaut font = Times new Roman (Windows), sinon DejaVu Serif (Linux)
      String selectedFont = ConfigManager.G2D_FONT_FAMILY;
      final List<String> fonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
      if(fonts.contains("Times New Roman")){
         selectedFont = "Times New Roman";
      }
      for(int i = 0; i < l.size(); i++){
         final LigneEtiquette le = l.get(i);
         final String contenu = le.getContenu();
         final String entete = le.getEntete();
         // si c'est un barcode
         if(le.getIsBarcode()){
            // on calcule l'écart avec la ligne précédent
            if(i > 0){
               y0 = y0 + 2;
            }
            Barcode barcode = null;
            BufferedImage imageBarcode = null;
            // création du code-barres
            try{
               if(contenu == null || contenu.trim().equals("")){

               }else{
                  barcode = BarcodeFactory.createCode128(contenu);
                  barcode.setBarHeight(16);
                  barcode.setDrawingText(false);
                  barcode.setResolution(resolution);
                  barcode.setDrawingQuietSection(false);
               }
            }catch(final BarcodeException be){
               log.error(be.getMessage(), be);
            }

            // création de l'image du code-barres
            if(barcode == null){
               imageBarcode = new BufferedImage(1, 56, BufferedImage.TYPE_BYTE_GRAY);
            }else{
               try{
                  imageBarcode = BarcodeImageHandler.getImage(barcode);
               }catch(final OutputException e){
                  log.error(e.getMessage(), e); 
               }
            }

            if(null != imageBarcode && null != barcode){
               // la variable width est en 1/72 par inchs. La valeur
               // résolution contient le nombre de pixels contenus
               // dans 1 inch. Donc, pour connaitre la width
               // max de l'image en pixels :
               // int maxWidth = (width / 72) * 80;
               // height1 = 56/4 = 14
               final int height = imageBarcode.getHeight(componentToBePrinted) / dH;
               int width = imageBarcode.getWidth(componentToBePrinted) / dW;

               if(width > maxWidth){
                  // throw new StringEtiquetteOverSizeException(BARCODE + "\n"
                  // + "code :" + codeObject + "\n");
                  // float divid = width / (float) maxWidth;
                  // height = Math.round(height /= divid);
                  barcode.setBarWidth(1);
                  // width = Math.round(width /= divid) - 1;
                  width = maxWidth - 2;

                  //					File bcImg = File.createTempFile("bc-", ".png");
                  //			        bcImg.deleteOnExit();
                  //			        FileOutputStream fos = new FileOutputStream(bcImg);
                  //			        BarcodeImageHandler.writePNG(barcode, fos);

                  // BufferedImage resized =
                  // 	Scalr.resize(imageBarcode, Scalr.Method.ULTRA_QUALITY,
                  //			Scalr.Mode.FIT_TO_WIDTH,
                  //			width, new BufferedImageOp[]{});

                  // on dessine le code-barres
                  g2d.drawImage(BarcodeImageHandler.getImage(barcode), x0, y0, width, height, null);
                  // imageBarcode.flush();

               }else{
                  // on dessine le code-barres
                  g2d.drawImage(imageBarcode, x0, y0, width, height, null);
               }
               y0 = y0 + height;
            }
         }else{
            int style = 0;
            int size = 4;
            // définition de la famille
            if(le.getFont() != null && !le.getFont().equals("")){
               selectedFont = le.getFont();
            }
            // définition du style
            if(le.getStyle() != null && !le.getStyle().equals("")){
               if(le.getStyle().equals("BOLD")){
                  style = Font.BOLD;
               }else if(le.getStyle().equals("ITALIC")){
                  style = Font.ITALIC;
               }else{
                  style = Font.PLAIN;
               }
            }
            // définition de la taille
            if(le.getSize() != null){
               size = le.getSize();
            }
            // Nouvelle font
            final Font font = new Font(selectedFont, style, size);
            // on calcule l'écart avec la ligne précédent
            y0 = y0 + size;
            // ajout du texte
            g2d.setFont(font);

            String content = "";

            if(entete != null){
               content = entete.concat(contenu != null ? contenu : "");
            }else{
               content = contenu;
            }

            g2d.drawString(content, x0, y0);

            final FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
            if(fm.stringWidth(content) > this.maxWidth){
               String message = content;
               if(code != null){
                  message = message + "\n" + "[" + code + "]\n";
               }
               throw new StringEtiquetteOverSizeException(message);
            }
         }
      }
   }

   public void paintQRCode(final Graphics g, final List<LigneEtiquette> l) throws StringEtiquetteOverSizeException{
      final Graphics2D graphics = (Graphics2D) g;
      final StringBuffer sb = new StringBuffer();

      for(int i = 0; i < l.size(); i++){
         final LigneEtiquette le = l.get(i);
         sb.append(le.getEntete());
         sb.append(le.getContenu());
         if(i == 0){
            drawString(sb, graphics);
         }
         sb.append("\n");
      }

      try{
         final Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
         hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

         final QRCodeWriter qrCodeWriter = new QRCodeWriter();
         final BitMatrix byteMatrix = qrCodeWriter.encode(sb.toString(), BarcodeFormat.QR_CODE, maxWidth, maxWidth, hintMap);

         final int matrixWidth = byteMatrix.getWidth();

         // Paint and save the image using the ByteMatrix

         graphics.setColor(Color.BLACK);

         for(int i = 0; i < matrixWidth; i++){
            for(int j = 10; j < matrixWidth; j++){
               if(byteMatrix.get(i, j)){
                  graphics.fillRect(i, j, 1, 1);
               }
            }
         }

      }catch(final Exception ex){
         log.error(ex.getMessage(), ex);
      }

   }

   private void drawString(final StringBuffer sb, final Graphics2D g2d) throws StringEtiquetteOverSizeException{

      String content = "";
      if(sb != null){
         content = sb.toString();
      }
      // définition de la font
      final String famille = ConfigManager.G2D_FONT_FAMILY;
      final int style = 0;
      final int size = 4;
      // font
      final Font font = new Font(famille, style, size);
      g2d.setFont(font);

      g2d.drawString(content, 0, size);

      final FontMetrics fm = g2d.getFontMetrics(font);
      if(fm.stringWidth(content) > maxWidth){
         throw new StringEtiquetteOverSizeException(content + "\n" + "code :" + code + "\n");
      }
   }

   public ComponentPrinter getComponentPrinter(){
      return componentPrinter;
   }

   public void setComponentPrinter(final ComponentPrinter componentPrinter){
      this.componentPrinter = componentPrinter;
   }
}