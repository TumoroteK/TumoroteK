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

import java.awt.Component;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.model.imprimante.Imprimante;

public class ComponentPrinter
{

   private static Log log = LogFactory.getLog(ComponentPrinter.class);

   /**
    * fonction d impression d etiquettes.
    * @param componentsToBePrinted
    * @param nb
    * @param confDir
    * @param printerTumoFileBean
    * @return
    */
   public static int printComponents(final List<? extends Component> componentsToBePrinted, final int nb, final Imprimante imprimante){
      int codeRetour = 0;
      final PrinterJob printJob = PrinterJob.getPrinterJob();
      //--- Create a new book to add pages to
      final Book book = new Book();
      final PageFormat documentPageFormat = new PageFormat();
      final Paper paper = new Paper();
      //pour etiquette Mbio:
      int largeur = 72;
      //pour etiquette Brady:
      final Integer largeurTmp = imprimante.getLargeur();
      if(largeurTmp != null && largeurTmp > 0){
         largeur = largeurTmp;
      }
      //pour etiquette Mbio:
      int longueur = 108;
      //pour etiquette Brady:
      // int longueur = 68;
      final Integer longueurTmp = imprimante.getLongueur();
      if(longueurTmp != null && longueurTmp > 0){
         longueur = longueurTmp;
      }

      //pour etiquette Mbio:
      int orientation = PageFormat.PORTRAIT;
      //pour etiquette Brady:
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
            final Component aComponent = componentsToBePrinted.get(i);
            final ComponentPrintable aPrintableComponent = new ComponentPrintable(aComponent);
            printJob.setPrintable(aPrintableComponent, documentPageFormat);
            book.append(aPrintableComponent, documentPageFormat, nb);
         }

         printJob.setPageable(book);
         //recherche des imprimantes

         final PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

         //recherche de l imprimante et affectation
         for(int i = 0; i < printServices.length; i++){
            if(printServices[i].getName().equals(imprimante.getNom())){
               try{
                  printJob.setPrintService(printServices[i]);
                  printJob.print();
                  codeRetour = 1;
               }catch(final PrinterException pe){
                  log.error("printComponents:PrinterException" + pe);
               }catch(final Exception e){
                  log.error("printComponents:Exception" + e);
               }
            }
         }
      }

      return codeRetour;
   }
   
   /**
    * fonction qui lance l impression d une liste d etiquette.
    * @param ArrayList liste des etiquettes 
    * @param int nb
    * @param ImprimanteBean
    * @param ModeleEtiquetteBean
    * @return
    */
   public static <T> int print(final List<Vector<T>> liste, final int nb, final Imprimante imprimanteBean, final String texte){
      final List<ComponentBarcodeLabel> components = getComponents(liste, imprimanteBean, texte);
      final int codeRetour = printComponents(components, nb, imprimanteBean);
      return codeRetour;
   }

//   /**
//    * fonction qui lance l impression d une liste d etiquette.
//    * @param ArrayList liste des etiquettes 
//    * @param int nb
//    * @param ImprimanteBean
//    * @param ModeleEtiquetteBean
//    * @return
//    */
//   public static int print(final List<Vector<?>> liste, final int nb, final Imprimante imprimanteBean, final String texte){
//      final List<ComponentBarcodeLabel> components = getComponents(liste, imprimanteBean, texte);
//      final int codeRetour = printComponents(components, nb, imprimanteBean);
//      return codeRetour;
//   }

//   /**
//    * 
//    * @param liste
//    * @param printerTumoFileBean
//    * @return
//    */
//   public static List<ComponentBarcodeLabel> getComponents(final List<Vector<?>> liste, final Imprimante imprimanteBean, final String texte){
//      final List<ComponentBarcodeLabel> components = new ArrayList<>();
//
//      for(int i = 0; i < liste.size(); i++){
//         final Vector<?> aData = liste.get(i);
//         final ComponentBarcodeLabel aBarcodeLabel =
//            new ComponentBarcodeLabel(aData, texte, imprimanteBean.getAbscisse(), imprimanteBean.getOrdonnee());
//         components.add(aBarcodeLabel);
//      }
//
//      return components;
//   }
   
   /**
    * 
    * @param liste
    * @param printerTumoFileBean
    * @return
    */
   public static <T> List<ComponentBarcodeLabel> getComponents(final List<Vector<T>> liste, final Imprimante imprimanteBean, final String texte){
      final List<ComponentBarcodeLabel> components = new ArrayList<>();

      for(int i = 0; i < liste.size(); i++){
         final Vector<T> aData = liste.get(i);
         final ComponentBarcodeLabel aBarcodeLabel =
            new ComponentBarcodeLabel(aData, texte, imprimanteBean.getAbscisse(), imprimanteBean.getOrdonnee());
         components.add(aBarcodeLabel);
      }

      return components;
   }

}
