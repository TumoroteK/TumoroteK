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
package fr.aphp.tumorotek.test.utils.etiquettes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.manager.etiquettes.TumoBarcodePrinter;
import fr.aphp.tumorotek.manager.impl.etiquettes.RawLanguageTranslater;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

public class ImprimerCodesBarresTest extends AbstractManagerTest4
{

   @Autowired
   private TumoBarcodePrinter tumoBarcodePrinter;

//   @Autowired
//   private BanqueManager banqueManager;

//   private Vector<String> createSimpleData(){
//      final Vector<String> result = new Vector<>();
//
//      result.add("aaa");
//      result.add("bbb");
//      result.add("ccc");
//      result.add("ddd");
//      result.add("eee");
//      result.add("fff");
//
//      return result;
//   }

   @Test
   /**
    * TODO Test sans test ?
    */
   public void testImpressionSimpleTumo(){
      final Imprimante imp = new Imprimante();
      imp.setNom("PDF");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(0);
      imp.setLongueur(0);
      imp.setOrientation(1);

      try{
         // List<String> erreurs = new ArrayList<String>();

         //TumoBarcodePrinter printer = new TumoBarcodePrinter(
         //	imp, "TEST");

//         final int completed = 0;
         //completed = printer.printData(createSimpleData(), 1);

//         if(completed == 1){
//            System.out.println("Impression terminée");
//         }else{
//            System.out.println("Erreur d'impression");
//         }
      }catch(final Exception e){
         final String msg = "ImprimerCodesBarres error! " + e.getMessage();
         System.out.println(msg);
      }
   }

   @Test
   public void test18charsBarCode(){
      final Imprimante imp = new Imprimante();
      imp.setNom("PDF");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(62);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = new Modele();
      mod.setTexteLibre("TEST");
      mod.setIsDefault(null);

      final String testGs2 = "MT2BAD2440491.14";

      final String strToprinte = testGs2;

      final List<List<LigneEtiquette>> llignes = new ArrayList<>();

      final List<LigneEtiquette> l = new ArrayList<>();
      final LigneEtiquette l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu(strToprinte);
      l.add(l1);
      final LigneEtiquette l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setContenu(String.valueOf(strToprinte.length()));
      l.add(l2);

      llignes.add(l);

      try{
         int completed = 0;
         completed = tumoBarcodePrinter.printListCopiesData(llignes, 1, imp, mod, null, null);
         assertTrue(completed == 1);
      }catch(final Exception e){
         System.out.println("ImprimerCodesBarres error! " + e.getMessage());
      }
   }

   @Test
   public void testSEPAGEZPL(){
      final Imprimante imp = new Imprimante();
      imp.setNom("RAW");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(62);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = new Modele();
      mod.setTexteLibre("TEST");
      mod.setIsDefault(null);

      // SEPAGE LIKE
      final List<LigneEtiquette> l = new ArrayList<>();
      // cb code echan
      final LigneEtiquette l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu("MT2BAD2440491.14");
      l.add(l1);
      // code echan
      final LigneEtiquette l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setContenu("MT2BAD2440491.14");
      l.add(l2);
      // cb code prel
      final LigneEtiquette l3 = new LigneEtiquette();
      l3.setIsBarcode(true);
      l3.setContenu("MT2BAD2440491");
      l.add(l3);
      // code prel
      final LigneEtiquette l4 = new LigneEtiquette();
      l4.setIsBarcode(false);
      l4.setContenu("MT2BAD2440491");
      l.add(l4);
      // type
      final LigneEtiquette l5 = new LigneEtiquette();
      l5.setIsBarcode(false);
      l5.setContenu("CULOT CELLULAIRE");
      l.add(l5);
      // Nom
      final LigneEtiquette l6 = new LigneEtiquette();
      l6.setIsBarcode(false);
      l6.setEntete("Patient:");
      l6.setContenu("SCHUMMER");
      l.add(l6);
      // DateStock
      final LigneEtiquette l7 = new LigneEtiquette();
      l7.setIsBarcode(false);
      l7.setEntete("Date cong:");
      l7.setContenu("10/12/2012 15:00:23");
      l.add(l7);
      // Quantite
      final LigneEtiquette l8 = new LigneEtiquette();
      l8.setIsBarcode(false);
      l8.setEntete("Quantite:");
      l8.setContenu("20 mL");
      l.add(l8);

      final String out = RawLanguageTranslater.printZPL(l, 5, 5, 300, null);

      System.out.println(out);

      final byte[] bytes = out.getBytes();
      final DocFlavor byteFlavor = DocFlavor.BYTE_ARRAY.TEXT_PLAIN_UTF_8;
      new SimpleDoc(bytes, byteFlavor, null);
      final PrintServiceAttributeSet aset = new HashPrintServiceAttributeSet();
      aset.add(new PrinterName("RAW", null));
      final PrintService[] services = PrintServiceLookup.lookupPrintServices(byteFlavor, aset);
      if(services.length > 0){
         //		    DocPrintJob job = services[0].createPrintJob();
         //		    try {
         //		       // job.print(bytesDoc, null);
         //		    } catch (PrintException pe) {
         //		    	pe.printStackTrace();
         //		    }
      }
   }

   /**
    * @throws IOException 
    * @throws UnknownHostException 
    * @see https://km.zebra.com/kb/index?page=content&id=SO7149&actp=LIST
    */
   @Test
   public void testSEPAGEZPLthroughSocket() throws UnknownHostException, IOException{

      final Imprimante imp = new Imprimante();
      imp.setNom("RAW");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(62);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = new Modele();
      mod.setTexteLibre("TEST TK POUR GABRIEL");
      mod.setIsDefault(null);

      // SEPAGE LIKE
      final List<LigneEtiquette> l = new ArrayList<>();
      // cb code echan
      final LigneEtiquette l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu("MT2BAD2440491.14");
      l.add(l1);
      // code echan
      final LigneEtiquette l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setContenu("MT2BAD2440491.14");
      l.add(l2);
      // cb code prel
      final LigneEtiquette l3 = new LigneEtiquette();
      l3.setIsBarcode(true);
      l3.setContenu("MT2BAD2440491");
      l.add(l3);
      // code prel
      final LigneEtiquette l4 = new LigneEtiquette();
      l4.setIsBarcode(false);
      l4.setContenu("MT2BAD2440491");
      l.add(l4);
      // type
      final LigneEtiquette l5 = new LigneEtiquette();
      l5.setIsBarcode(false);
      l5.setContenu("CULOT CELLULAIRE");
      l.add(l5);
      // Nom
      final LigneEtiquette l6 = new LigneEtiquette();
      l6.setIsBarcode(false);
      l6.setEntete("Patient:");
      l6.setContenu("SCHUMMER");
      l.add(l6);
      // DateStock
      final LigneEtiquette l7 = new LigneEtiquette();
      l7.setIsBarcode(false);
      l7.setEntete("Date cong:");
      l7.setContenu("10/12/2012 15:00:23");
      l.add(l7);
      // Quantite
      final LigneEtiquette l8 = new LigneEtiquette();
      l8.setIsBarcode(false);
      l8.setEntete("Quantite:");
      l8.setContenu("20 10^6 CELL");
      l.add(l8);

      final BarcodeFieldDefault by = new BarcodeFieldDefault(new Float(1.0), new Float(5.0), 10);

      final String out = RawLanguageTranslater.printZPL(l, 5, 5, 300, by);

      System.out.println(out);

      assertFalse(out.contains("10^6 CELL"));
      assertTrue(out.contains("10_5e6 CELL"));

      // The line below illustrates the default port 6101 for mobile printers 9100 is the default port number
      // for desktop and tabletop printers
      //  Socket clientSocket=new Socket("10.181.198.59",9100);

      //  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream() );
      //The data being sent in the lines below illustrate CPCL  one can change the data for the corresponding 
      //language being used (ZPL, EPL)   

      // outToServer.writeBytes(out);

      // clientSocket.close();
   }

   /**
    * Penser à ecouter sur le port 8001 avec netcat
    *  sudo nc -l 8001
    * @throws UnknownHostException
    * @throws IOException
    */
   @Test
   public void testJSCRIPTthroughSocket() throws UnknownHostException, IOException{

      final Imprimante imp = new Imprimante();
      imp.setNom("RAW");
      imp.setAbscisse(100);
      imp.setOrdonnee(100);
      imp.setLargeur(62);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = new Modele();
      mod.setTexteLibre("TEST TK POUR GABRIEL");
      mod.setIsDefault(null);

      // SEPAGE LIKE
      final List<LigneEtiquette> l = new ArrayList<>();
      // cb code echan
      final LigneEtiquette l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu("MT2BAD2440491.14");
      l.add(l1);
      // code echan
      final LigneEtiquette l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setContenu("MT2BAD2440491.14");
      l.add(l2);
      // cb code prel
      final LigneEtiquette l3 = new LigneEtiquette();
      l3.setIsBarcode(true);
      l3.setContenu("MT2BAD2440491");
      l.add(l3);
      // code prel
      final LigneEtiquette l4 = new LigneEtiquette();
      l4.setIsBarcode(false);
      l4.setContenu("MT2BAD2440491");
      l4.setStyle("BOLD");
      l.add(l4);
      // type
      final LigneEtiquette l5 = new LigneEtiquette();
      l5.setIsBarcode(false);
      l5.setSize(10);
      l5.setContenu("CULOT CELLULAIRE");
      l.add(l5);
      // Nom
      final LigneEtiquette l6 = new LigneEtiquette();
      l6.setIsBarcode(false);
      l6.setEntete("Patient:");
      l6.setContenu("SCHUMMER");
      l6.setStyle("ITALIC");
      l.add(l6);
      // DateStock
      final LigneEtiquette l7 = new LigneEtiquette();
      l7.setIsBarcode(false);
      l7.setEntete("Date cong:");
      l7.setContenu("10/12/2012 15:00:23");
      l.add(l7);
      // Quantite
      final LigneEtiquette l8 = new LigneEtiquette();
      l8.setIsBarcode(false);
      l8.setEntete("Quantite:");
      l8.setContenu("20 mL");
      l.add(l8);

      final BarcodeFieldDefault by = new BarcodeFieldDefault(new Float(1.5), new Float(5.0), 7);

      final String out = RawLanguageTranslater.printJSCRIPT(l, 72, 72, 100, 10, 300, by);

      System.out.println(out);

      // The line below illustrates the default port 6101 for mobile printers 9100 is the default port number
      // for desktop and tabletop printers
      // Socket clientSocket=new Socket("10.181.198.59",9100);

      System.out.println(InetAddress.getByName(null));

      // pour ecouter le resultat sur le port 8001
      // sudo nc -l 8001

      //  Socket clientSocket=new Socket("127.0.0.1",8001);

      //  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream() );
      //The data being sent in the lines below illustrate CPCL  one can change the data for the corresponding 
      //language being used (ZPL, EPL)   

      // outToServer.writeBytes(out);

      // clientSocket.close();
   }

}
