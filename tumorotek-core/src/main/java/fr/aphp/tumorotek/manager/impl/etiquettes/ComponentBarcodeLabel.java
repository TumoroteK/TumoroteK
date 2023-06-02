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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;

/**
 * Classe pour l'impression des codes barres.
 *
 * @author Pierre Ventadour.
 * @author Julien Husson
 *
 *         modifiée le 05 Aout 2012
 */
public class ComponentBarcodeLabel extends Component
{

   private static final long serialVersionUID = -8481492542890431288L;

   // private data;
   private List<LigneEtiquette> lignes = new ArrayList<>();

   private int abscisse;

   private int ordonnee;

   private int maxWidth;

   private Boolean isQRCode = false;

   private String code;

   /**
    * Instanciation d un objet imprimable a partir de donnees.
    *
    * @param isQRCode
    * @param code
    *
    * @param Vector
    *            <String>_data
    * @param String
    *            _nomEtablissement
    */
   public ComponentBarcodeLabel(final List<LigneEtiquette> l, final int ab, final int or, final int w, final Boolean isQRCode,
      final String code){
      setLignes(l);
      setAbscisse(ab);
      setOrdonnee(or);
      // la width passée est en 1/72 inchs
      setMaxWidth(w);
      this.setIsQRCode(isQRCode);
      this.setCode(code);
   }

   public Boolean getIsQRCode(){
      return isQRCode;
   }

   public void setIsQRCode(final Boolean isQRCode){
      this.isQRCode = isQRCode;
   }

   public List<LigneEtiquette> getLignes(){
      return lignes;
   }

   public void setLignes(final List<LigneEtiquette> lignes){
      this.lignes = lignes;
   }

   public String getCode(){
      return code;
   }

   public void setCode(final String code){
      this.code = code;
   }

   public int getAbscisse(){
      return abscisse;
   }

   public void setAbscisse(final int abscisse){
      this.abscisse = abscisse;
   }

   public int getOrdonnee(){
      return ordonnee;
   }

   public void setOrdonnee(final int ordonnee){
      this.ordonnee = ordonnee;
   }

   public int getMaxWidth(){
      return maxWidth;
   }

   public void setMaxWidth(final int maxWidth){
      this.maxWidth = maxWidth;
   }

   /**
    * Method: paint
    *
    * @param g a value of type Graphics
    * @return void
    */
   //	public void paint(Graphics g) {
   //		if (!isQRCode) {
   //			paint(g, lignes, this.abscisse, this.ordonnee);
   //		} else {
   //			paintQRCode(g, lignes, this.abscisse, this.ordonnee);
   //		}
   //	}

   //	public void paint(Graphics g, List<LigneEtiquette> l, int x0, int y0) {
   //		// --- Set the drawing color to black
   //		Graphics2D g2d = (Graphics2D) g;
   //		g2d.setPaint(Color.black);
   //		// paramètres
   //		int dW = 4;
   //		int dH = 4;
   //		int resolution = 100;
   //
   //		for (int i = 0; i < lignes.size(); i++) {
   //			LigneEtiquette le = lignes.get(i);
   //			String contenu = le.getContenu();
   //			String entete = le.getEntete();
   //			// si c'est un barcode
   //			if (le.getIsBarcode()) {
   //				// on calcule l'écart avec la ligne précédent
   //				if (i > 0) {
   //					y0 = y0 + 2;
   //				}
   //				Barcode barcode = null;
   //				Image imageBarcode = null;
   //				// création du code-barres
   //				try {
   //					if (contenu == null || contenu.trim().equals("")) {
   //
   //					} else {
   //						barcode = BarcodeFactory.createCode128B(contenu);
   //						barcode.setBarHeight(56);
   //						barcode.setDrawingText(false);
   //						barcode.setResolution(resolution);
   //						barcode.setDrawingQuietSection(false);
   //					}
   //				} catch (BarcodeException be) {
   //					log.error(be);
   //				}
   //
   //				// création de l'image du code-barres
   //				if (barcode == null) {
   //					imageBarcode = new BufferedImage(1, 56,
   //							BufferedImage.TYPE_BYTE_GRAY);
   //				} else {
   //					try {
   //						imageBarcode = BarcodeImageHandler.getImage(barcode);
   //					} catch (OutputException e) {
   //						 log.error("An error occurred : ", e);
   //					}
   //				}
   //
   //				// la variable width est en 1/72 par inchs. La valeur
   //				// résolution contient le nombre de pixels contenus
   //				// dans 1 inch. Donc, pour connaitre la width
   //				// max de l'image en pixels :
   //				// int maxWidth = (width / 72) * 80;
   //				// height1 = 56/4 = 14
   //				int height = imageBarcode.getHeight(this) / dH;
   //				int width = imageBarcode.getWidth(this) / dW;
   //
   //				if (width > maxWidth) {
   //					throw new StringEtiquetteOverSizeException(BARCODE+ "\n"+"code :"+code+ "\n");
   //				}
   //				// on dessine le code-barres
   //				g2d.drawImage(imageBarcode, x0, y0, width, height, this);
   //				y0 = y0 + 16;
   //			} else {
   //				// définition de la font
   //				String famille = ConfigManager.TIMES_FONT_FAMILY;
   //				int style = 0;
   //				int size = 4;
   //				// définition de la famille
   //				if (le.getFont() != null && !le.getFont().equals("")) {
   //					famille = le.getFont();
   //				}
   //				// définition du style
   //				if (le.getStyle() != null && !le.getStyle().equals("")) {
   //					if (le.getStyle().equals("BOLD")) {
   //						style = Font.BOLD;
   //					} else if (le.getStyle().equals("ITALIC")) {
   //						style = Font.ITALIC;
   //					} else {
   //						style = Font.PLAIN;
   //					}
   //				}
   //				// définition de la taille
   //				if (le.getSize() != null) {
   //					size = le.getSize();
   //				}
   //				// Nouvelle font
   //				Font font = new Font(famille, style, size);
   //				// on calcule l'écart avec la ligne précédent
   //				y0 = y0 + size;
   //				// ajout du texte
   //				g2d.setFont(font);
   //
   //				g2d.drawString(entete.concat(contenu), x0, y0);
   //
   //				FontMetrics fm = g2d.getFontMetrics(font);
   //				if (fm.stringWidth(entete.concat(contenu)) > this.maxWidth) {
   //					throw new StringEtiquetteOverSizeException(le.getEntete() + " "+contenu+"\n"+"code :"+code+ "\n");
   //				}
   //			}
   //		}
   //	}
   //
   //	public void paintQRCode(Graphics g, List<LigneEtiquette> l, int x0, int y0) {
   //		Graphics2D graphics = (Graphics2D) g;
   //		StringBuffer sb = new StringBuffer();
   //
   //		for (int i = 0; i < lignes.size(); i++) {
   //			LigneEtiquette le = lignes.get(i);
   //			sb.append(le.getEntete());
   //			sb.append(le.getContenu());
   //			if (i == 0) {
   //				drawString(sb, graphics);
   //			}
   //			sb.append("\n");
   //		}
   //
   //		try {
   //			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
   //			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
   //
   //			QRCodeWriter qrCodeWriter = new QRCodeWriter();
   //			BitMatrix byteMatrix = qrCodeWriter.encode(sb.toString(),
   //					BarcodeFormat.QR_CODE, maxWidth, maxWidth, hintMap);
   //
   //			int matrixWidth = byteMatrix.getWidth();
   //
   //			// Paint and save the image using the ByteMatrix
   //
   //			graphics.setColor(Color.BLACK);
   //
   //			for (int i = 0; i < matrixWidth; i++) {
   //				for (int j = 10; j < matrixWidth; j++) {
   //					if (byteMatrix.get(i, j)) {
   //						graphics.fillRect(i, j, 1, 1);
   //					}
   //				}
   //			}
   //
   //		} catch (Exception ex) {
   //			log.error(ex);
   //		}
   //
   //	}
   //
   //	private void drawString(StringBuffer sb, Graphics2D g2d) {
   //
   //		String content = "";
   //		if (sb != null) {
   //			content = sb.toString();
   //		}
   //		// définition de la font
   //		String famille = ConfigManager.TIMES_FONT_FAMILY;
   //		int style = 0;
   //		int size = 4;
   //		// font
   //		Font font = new Font(famille, style, size);
   //		g2d.setFont(font);
   //
   //		g2d.drawString(content, 0, size);
   //
   //		FontMetrics fm = g2d.getFontMetrics(font);
   //		if (fm.stringWidth(content) > this.maxWidth) {
   //			throw new StringEtiquetteOverSizeException(content + "\n"+"code :"+code+ "\n");
   //		}
   //	}
}
