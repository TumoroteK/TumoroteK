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
package fr.aphp.tumorotek.utils.etiquettes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

/**
 * Classe pour l'impression des codes barres.
 * @author Pierre Ventadour.
 *
 */
public class ComponentBarcodeLabel extends Component
{

   private static final long serialVersionUID = -8481492542890431288L;

   private static Log log = LogFactory.getLog(ComponentBarcodeLabel.class);

   private final Vector<?> data;

   private final String nomEtablissement;

   private final int abscisse;

   private final int ordonnee;

   /**
   * Instanciation d un objet imprimable a partir de donnees.
   * @param Vector<String>_data
   * @param String _nomEtablissement
   */
   public ComponentBarcodeLabel(final Vector<?> d, final String e, final int ab, final int or){
      data = d;
      nomEtablissement = e;
      abscisse = ab;
      ordonnee = or;

   }

   /**
    * Method: paint <p>.
    *
    * @param g a value of type Graphics
    * @return void
    */
   @Override
   public void paint(final Graphics g){
      paint(g, data, this.abscisse, this.ordonnee);
   }

   // On double la taille du code barres
   public void paint(final Graphics g, final Vector<?> aData, final int x0, final int y0){
      log.info("paint:debut");

      //--- Set the drawing color to black
      final Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(Color.black);

      Barcode barcode1 = null;
      Barcode barcode2 = null;
      Image imageBarcode1 = null;
      Image imageBarcode2 = null;

      final Font font1 = new Font("Times New Roman", Font.BOLD, 4);
      final Font font2 = new Font("Times New Roman", Font.BOLD, 6);
      final Font font3 = new Font("Times New Roman", Font.BOLD, 8);

      final String titreParam1 = "Prel: ";
      final String titreParam2 = "Tube: ";
      final String titreParam3 = "Type: ";
      final String titreParam4 = "Patient: ";
      final String titreParam5 = "Date Cong: ";
      final String titreParam6 = "Quantite: ";

      final String param1 = (String) aData.get(0);
      final String param2 = (String) aData.get(1);
      final String param3 = (String) aData.get(2);
      final String param4 = (String) aData.get(3);
      final String param5 = (String) aData.get(4);
      final String param6 = (String) aData.get(5);

      try{
         if(param1 == null || param1.trim().equals("")){

         }else{
            barcode1 = BarcodeFactory.createCode128B(param1);
            barcode1.setBarHeight(56);
            barcode1.setDrawingText(false);
            barcode1.setResolution(100);
         }

         if(param2 == null || param2.trim().equals("")){

         }else{
            barcode2 = BarcodeFactory.createCode128B(param2);
            barcode2.setBarHeight(56);
            barcode2.setDrawingText(false);
            barcode2.setResolution(100);
         }
      }catch(final BarcodeException be){
         // Error handling
         log.error("paint:BarcodeException" + be);
      }

      if(barcode1 == null){
         imageBarcode1 = new BufferedImage(1, 56, BufferedImage.TYPE_BYTE_GRAY);
      }else{
         try{
            imageBarcode1 = BarcodeImageHandler.getImage(barcode1);
         }catch(final OutputException e){
            log.error(e);
         }
      }

      if(barcode2 == null){
         imageBarcode2 = new BufferedImage(1, 56, BufferedImage.TYPE_BYTE_GRAY);
      }else{
         try{
            imageBarcode2 = BarcodeImageHandler.getImage(barcode2);
         }catch(final OutputException e){
            log.error(e);
         }
      }

      final int d = 4;

      // height1 = 56/4 = 14
      final int height1 = imageBarcode1.getHeight(this) / d;
      final int width1 = imageBarcode1.getWidth(this) / d;

      final int height2 = imageBarcode2.getHeight(this) / d;
      final int width2 = imageBarcode2.getWidth(this) / d;

      g2d.drawImage(imageBarcode1, x0 - 4, y0 + 0, width1, height1, this);
      g2d.setFont(font1);
      g2d.drawString(titreParam1, x0 + 0, y0 + 20);
      g2d.setFont(font3);
      g2d.drawString(param1, x0 + 11, y0 + 20);
      g2d.drawImage(imageBarcode2, x0 - 4, y0 + 21, width2, height2, this);
      g2d.setFont(font1);
      g2d.drawString(titreParam2, x0 + 0, y0 + 41);
      g2d.setFont(font3);
      g2d.drawString(param2, x0 + 11, y0 + 41);
      g2d.setFont(font1);
      g2d.drawString(titreParam3, x0 + 0, y0 + 45);
      g2d.setFont(font1);
      g2d.drawString(param3, x0 + 22, y0 + 45);
      g2d.setFont(font1);
      g2d.drawString(titreParam4, x0 + 0, y0 + 49);
      g2d.setFont(font1);
      g2d.drawString(param4, x0 + 22, y0 + 49);
      g2d.setFont(font1);
      g2d.drawString(titreParam5, x0 + 0, y0 + 53);
      g2d.setFont(font1);
      g2d.drawString(param5, x0 + 22, y0 + 53);
      g2d.setFont(font1);
      g2d.drawString(titreParam6, x0 + 0, y0 + 57);
      g2d.setFont(font1);
      g2d.drawString(param6, x0 + 22, y0 + 57);
      g2d.setFont(font2);
      g2d.drawString(nomEtablissement, x0 + 0, y0 + 64);

      log.info("paint:fin");
   }

}
