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

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.manager.exception.StringEtiquetteOverSizeException;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

//import javassist.bytecode.annotation.ArrayMemberValue;

public class ComponentPrinter
{

   private static Logger log = LoggerFactory.getLogger(ComponentPrinter.class);

   private Exception runtimeException = null;

   /**
    * fonction d impression d etiquettes.
    *
    * @param componentsToBePrinted
    * @param nb
    * @param confDir
    * @param printerTumoFileBean
    * @return
    * @throws PrinterException
    */
   public int printComponents(final List<ComponentBarcodeLabel> componentsToBePrinted, final Imprimante imprimante,
      final String rawLang, final BarcodeFieldDefault by) throws PrinterException{
      int codeRetour = 0;

      if(rawLang == null){
         final PrinterJob printJob = PrinterJob.getPrinterJob();
         // --- Create a new book to add pages to
         final Book book = new Book();
         final PageFormat documentPageFormat = new PageFormat();
         final Paper paper = new Paper();
         // pour etiquette Mbio:
         int largeur = 72;
         // pour etiquette Brady:
         final Integer largeurTmp = imprimante.getLargeur();
         if(largeurTmp != null && largeurTmp > 0){
            largeur = largeurTmp;
         }
         // pour etiquette Mbio:
         int longueur = 108;
         // pour etiquette Brady:
         // int longueur = 68;
         final Integer longueurTmp = imprimante.getLongueur();
         if(longueurTmp != null && longueurTmp > 0){
            longueur = longueurTmp;
         }

         // pour etiquette Mbio:
         int orientation = PageFormat.PORTRAIT;
         // pour etiquette Brady:
         // int orientation = PageFormat.LANDSCAPE;
         orientation = imprimante.getOrientation();
         if(orientation == 1){
            orientation = PageFormat.PORTRAIT;
         }else if(orientation == 2){
            orientation = PageFormat.LANDSCAPE;
         }

         paper.setSize(largeur, longueur);
         paper.setImageableArea(0, 0, largeur, longueur);
         documentPageFormat.setPaper(paper);
         documentPageFormat.setOrientation(orientation);

         if(componentsToBePrinted != null && componentsToBePrinted.size() > 0){
            for(int i = 0; i < componentsToBePrinted.size(); i++){
               final ComponentBarcodeLabel aComponent = componentsToBePrinted.get(i);
               final ComponentPrintable aPrintableComponent = new ComponentPrintable(aComponent, printJob);
               aPrintableComponent.setComponentPrinter(this);

               printJob.setPrintable(aPrintableComponent, documentPageFormat);
               book.append(aPrintableComponent, documentPageFormat); //, nb);
            }

            printJob.setPageable(book);
            // recherche des imprimantes

            final PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            // recherche de l imprimante et affectation
            boolean imprimanteTrouvee = false;
            for(int i = 0; i < printServices.length; i++){
               if(printServices[i].getName().equals(imprimante.getNom())){
                  imprimanteTrouvee = true;
                  try{
                     runtimeException = null;
                     printJob.setPrintService(printServices[i]);
                     printJob.print();
                     codeRetour = 1;
                  }catch(final PrinterAbortException e){
                     // throw new StringEtiquetteOverSizeException("code : ");

                     if(runtimeException != null){
                        if(runtimeException instanceof StringEtiquetteOverSizeException){
                           throw (StringEtiquetteOverSizeException) runtimeException;
                        }
                        throw new PrinterException(runtimeException.getMessage());
                     }
                  }
               }
            }

            if(!imprimanteTrouvee){
               final StringBuffer sb = new StringBuffer();
               sb.append("L'imprimante '");
               sb.append(imprimante.getNom());
               sb.append("' n'a pas été détectée par TumoroteK. ");
               sb.append("Impression impossible.");
               log.error(sb.toString());
               codeRetour = -1;
            }
         }
      }else{ // RAW printing
         codeRetour = printRaw(componentsToBePrinted, imprimante, rawLang, by);
      }

      return codeRetour;
   }

   //	/**
   //	 * fonction qui lance l impression d une liste d etiquette.
   //	 *
   //	 * @param ArrayList
   //	 *            liste des etiquettes
   //	 * @param int nb
   //	 * @param ImprimanteBean
   //	 * @param ModeleEtiquetteBean
   //	 * @param raw printing Language
   //	 * @return
   //	 * @throws PrinterException
   //	 */
   //	public int print(List<LigneEtiquette> lignes, int nb,
   //			Imprimante imprimanteBean, Modele modele, String rawLang) throws PrinterException {
   //		List<ComponentBarcodeLabel> components = getComponents(lignes,
   //				imprimanteBean, modele, null);
   //		int codeRetour = printComponents(components, nb, imprimanteBean, rawLang);
   //		return codeRetour;
   //	}
   //
   //	/**
   //	 * fonction qui lance l impression d une liste d etiquette.
   //	 *
   //	 * @param ArrayList
   //	 *            liste des etiquettes
   //	 * @param int nb
   //	 * @param ImprimanteBean
   //	 * @param ModeleEtiquetteBean
   //	 * @param raw printing Language
   //	 * @return
   //	 * @throws PrinterException
   //	 */
   //	public int printList(List<List<LigneEtiquette>> liste, int nb,
   //			Imprimante imprimanteBean, Modele modele, String rawLang) throws PrinterException {
   //		List<ComponentBarcodeLabel> components = new ArrayList<ComponentBarcodeLabel>();
   //		for (int i = 0; i < liste.size(); i++) {
   //			components.add(getComponent(liste.get(i), imprimanteBean, modele, null));
   //		}
   //		int codeRetour = printComponents(components, nb, imprimanteBean, rawLang);
   //		return codeRetour;
   //	}

   /**
    *
    * @param liste
    * @param printerTumoFileBean
    * @return
    */
   public static List<ComponentBarcodeLabel> getComponents(final List<LigneEtiquette> lignes, final Imprimante imprimanteBean,
      final Modele modele, final String code){
      final List<ComponentBarcodeLabel> components = new ArrayList<>();

      Boolean isQRCode = false;
      if(modele != null){
         isQRCode = modele.getIsQRCode();
      }

      // on recherche la largeur max de l'étiquette
      int largeur = 72;
      final Integer largeurTmp = imprimanteBean.getLargeur();
      if(largeurTmp != null && largeurTmp > 0){
         largeur = largeurTmp;
      }

      final ComponentBarcodeLabel aBarcodeLabel =
         new ComponentBarcodeLabel(lignes, imprimanteBean.getAbscisse(), imprimanteBean.getOrdonnee(), largeur, isQRCode, code);
      components.add(aBarcodeLabel);
      return components;
   }

   /**
    *
    * @param code
    * @param liste
    * @param printerTumoFileBean
    * @return
    */
   public static ComponentBarcodeLabel getComponent(final List<LigneEtiquette> lignes, final Imprimante imprimanteBean,
      final Modele modele, final String code){

      Boolean isQRCode = false;
      if(modele != null){
         isQRCode = modele.getIsQRCode();
      }

      // on recherche la largeur max de l'étiquette
      int largeur = 72;
      final Integer largeurTmp = imprimanteBean.getLargeur();
      if(largeurTmp != null && largeurTmp > 0){
         largeur = largeurTmp - 2;
      }

      final ComponentBarcodeLabel aBarcodeLabel =
         new ComponentBarcodeLabel(lignes, imprimanteBean.getAbscisse(), imprimanteBean.getOrdonnee(), largeur, isQRCode, code);

      return aBarcodeLabel;
   }

   public void setRuntimeException(final Exception e){
      this.runtimeException = e;
   }

   private int printRaw(final List<ComponentBarcodeLabel> componentsToBePrinted, final Imprimante imprimante,
      final String rawLang, final BarcodeFieldDefault by) throws PrinterException{
      int codeRetour = -1;

      if(rawLang != null){
         final int x0 = imprimante.getAbscisse() != null ? imprimante.getAbscisse() : 0;
         final int y0 = imprimante.getOrdonnee() != null ? imprimante.getOrdonnee() : 0;
         final int resolution = imprimante.getResolution() != null ? imprimante.getResolution() : 300;

         final StringBuilder out = new StringBuilder();
         if(rawLang.equals("ZPL")){
            log.debug("ZPL printing");
            for(final ComponentBarcodeLabel cpl : componentsToBePrinted){
               out.append(RawLanguageTranslater.printZPL(cpl.getLignes(), x0, y0, resolution, by));
               out.append("\n");
            }
         }else if(rawLang.equals("JScript")){
            log.debug("JScript printing");
            for(final ComponentBarcodeLabel cpl : componentsToBePrinted){
               out.append(RawLanguageTranslater.printJSCRIPT(cpl.getLignes(), imprimante.getLargeur(), imprimante.getLongueur(),
                  x0, y0, resolution, by));
               // out.append("\n");
               // remplacé par System.lineSeparator dans printJSCRIPT
            }
         }
         // sock printing
         if(imprimante.getAdresseIp() != null && imprimante.getPort() != null){
            Socket clientSocket = null;
            try{
               log.debug(out.toString());
               log.debug("socket printing");
               clientSocket = new Socket(imprimante.getAdresseIp(), imprimante.getPort());
               final DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

               // ZPL special chars
               outToServer.write(out.toString().getBytes(StandardCharsets.UTF_8));
               // test fonctionnel J-SCRIPT à cause des \n ???
               //  outToServer.writeBytes(out.toString());

               codeRetour = 1;
               log.debug("Code retour {}:", codeRetour);
            }catch(final Exception e){
               e.printStackTrace();
            }finally{
               if(clientSocket != null){
                  try{
                     clientSocket.close();
                  }catch(final IOException e){
                     e.printStackTrace();
                  }
               }
            }
         }else{ // print through raw printing driver

            final byte[] bytes = out.toString().getBytes(StandardCharsets.UTF_8);
            final DocFlavor byteFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            final Doc bytesDoc = new SimpleDoc(bytes, byteFlavor, null);
            // PrintServiceAttributeSet aset = new HashPrintServiceAttributeSet();
            // aset.add(new PrinterName(imprimante.getNom(), null));
            final PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            if(services.length > 0){
               for(final PrintService ps : services){
                  if(ps.getName().equals(imprimante.getNom())){
                     final DocPrintJob job = services[0].createPrintJob();
                     try{
                        job.print(bytesDoc, null);
                        codeRetour = 1;
                     }catch(final PrintException pe){
                        pe.printStackTrace();
                        codeRetour = 0;
                     }
                     break;
                  }
               }
            }
         }
      }else{
         codeRetour = 0;
      }

      return codeRetour;
   }

}