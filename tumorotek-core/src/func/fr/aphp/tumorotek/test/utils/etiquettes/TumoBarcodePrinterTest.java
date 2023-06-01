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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.etiquettes.BarcodeFieldDefault;
import fr.aphp.tumorotek.manager.etiquettes.TumoBarcodePrinter;
import fr.aphp.tumorotek.manager.imprimante.ModeleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

public class TumoBarcodePrinterTest extends AbstractManagerTest4
{

   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private TumoBarcodePrinter tumoBarcodePrinter;
   @Autowired
   private ModeleManager modeleManager;

   //	public void testPrintData() {
   //		Imprimante imp = new Imprimante();
   //		imp.setNom("PDF");
   //		imp.setAbscisse(0);
   //		imp.setOrdonnee(0);
   //		imp.setLargeur(0);
   //		imp.setLongueur(0);
   //		imp.setOrientation(1);
   //
   //		Modele mod = new Modele();
   //		mod.setTexteLibre("TEXT");
   //		mod.setIsDefault(null);
   //
   //		List<LigneEtiquette> l = new ArrayList<LigneEtiquette>();
   //		LigneEtiquette l1 = new LigneEtiquette();
   //		l1.setIsBarcode(true);
   //		l1.setContenu("aaa");
   //		l.add(l1);
   //
   //		
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printData(l, 1, imp, mod, null);
   //			assertTrue(completed == 1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //		
   //		// ZPL driver
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printData(l, 1, imp, mod, "ZPL");
   //			assertTrue(completed == 1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //		
   //		// ZPL socket IP
   //		imp.setAdresseIp("127.0.0.1");
   //		imp.setPort(9100);
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printData(l, 1, imp, mod, "ZPL");
   //			assertTrue(completed == -1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //	
   //	}
   //
   //	public void testPrintListData() {
   //		Imprimante imp = new Imprimante();
   //		imp.setNom("PDF");
   //		imp.setAbscisse(0);
   //		imp.setOrdonnee(0);
   //		imp.setLargeur(0);
   //		imp.setLongueur(0);
   //		imp.setOrientation(1);
   //
   //		Modele mod = modeleManager.findByIdManager(1);
   //		mod.setTexteLibre("TEXT");
   //		mod.setIsDefault(true);
   //
   //		List<LigneEtiquette> l = new ArrayList<LigneEtiquette>();
   //		LigneEtiquette l1 = new LigneEtiquette();
   //		l1.setIsBarcode(true);
   //		l1.setContenu("aaa");
   //		l.add(l1);
   //		
   //		List<LigneEtiquette> li2 = new ArrayList<LigneEtiquette>();
   //		LigneEtiquette l2 = new LigneEtiquette();
   //		l2.setIsBarcode(false);
   //		l2.setContenu("bbb");
   //		li2.add(l2);
   //
   //
   //		/*
   //		 * Vector<String> result2 = new Vector<String>(); result2.add("aaa");
   //		 * result2.add("bbb"); result2.add("ccc"); result2.add("ddd");
   //		 * result2.add("eee"); result2.add("fff");
   //		 */
   //
   //		List<List<LigneEtiquette>> ll = new ArrayList<List<LigneEtiquette>>();
   //		ll.add(l);
   //		ll.add(li2);
   //		// datas.add(result2);
   //
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printListData(ll, imp, mod, null);
   //			assertTrue(completed == 1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //		
   //		// ZPL driver
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printListData(ll, imp, mod, "ZPL");
   //			assertTrue(completed == 1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //		
   //		// ZPL socket IP
   //		imp.setAdresseIp("127.0.0.1");
   //		imp.setPort(9100);
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printListData(ll, imp, mod, "ZPL");
   //			assertTrue(completed == -1);
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //	}

   @Test
   public void testPrintListCopiesData(){
      final Imprimante imp = new Imprimante();
      imp.setNom("PDF");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(0);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = modeleManager.findByIdManager(1);
      mod.setTexteLibre("TEXT");
      mod.setIsDefault(true);

      final List<LigneEtiquette> l = new ArrayList<>();
      final LigneEtiquette l1 = new LigneEtiquette();
      l1.setIsBarcode(true);
      l1.setContenu("aaà");
      l.add(l1);

      final LigneEtiquette l2 = new LigneEtiquette();
      l2.setIsBarcode(false);
      l2.setContenu("you know my nameàéè");
      l.add(l2);

      final List<LigneEtiquette> lbis = new ArrayList<>();
      final LigneEtiquette l1bis = new LigneEtiquette();
      l1bis.setIsBarcode(true);
      l1bis.setContenu("béè");
      lbis.add(l1bis);

      final LigneEtiquette l2bis = new LigneEtiquette();
      l2bis.setIsBarcode(false);
      l2bis.setEntete("song: ");
      l2bis.setContenu("jumping jack flashàéé");
      lbis.add(l2bis);

      final List<List<LigneEtiquette>> ll = new ArrayList<>();
      ll.add(l);
      ll.add(lbis);

      try{
         int completed = 0;
         completed = tumoBarcodePrinter.printListCopiesData(ll, 3, imp, mod, null, null);
         assertEquals(1, completed);

         completed = tumoBarcodePrinter.printListCopiesData(ll, 3, imp, mod, "ZPL", null);
         assertTrue(completed == 1);

         // adresse ISLS_0501 test socket
         // imp.setAdresseIp("10.181.198.59");
         // test RAW printer driver
         // imp.setAdresseIp(null);
         // adresse inexistante
         imp.setAdresseIp("0.99.0.181991");
         imp.setPort(9100);
         completed = tumoBarcodePrinter.printListCopiesData(ll, 3, imp, mod, "ZPL", null);
         assertTrue(completed == -1);
         // assertTrue(completed == 1);

         ll.clear();

         final BarcodeFieldDefault by = new BarcodeFieldDefault(new Float(1.0), new Float(2.35), 10);

         completed = tumoBarcodePrinter.printListCopiesData(null, 3, imp, mod, null, by);
         assertTrue(completed == 0);

      }catch(final Exception e){
         final String msg = "ImprimerCodesBarres error! " + e.getMessage();
         System.out.println(msg);
      }
   }

   //	public void testPrintEchantillons() {
   //		Imprimante imp = new Imprimante();
   //		imp.setNom("PDF");
   //		imp.setAbscisse(0);
   //		imp.setOrdonnee(0);
   //		imp.setLargeur(0);
   //		imp.setLongueur(0);
   //		imp.setOrientation(1);
   //
   //		Modele mod = new Modele();
   //		mod.setTexteLibre("TEXT");
   //		mod.setIsDefault(true);
   //
   //		List<Echantillon> liste = new ArrayList<Echantillon>();
   //		liste.add(echantillonManager.findByIdManager(1));
   //		liste.add(echantillonManager.findByIdManager(2));
   //		liste.add(echantillonManager.findByIdManager(3));
   //
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printEchantillon(liste, 1, imp, mod, null);
   //
   //			if (completed == 1) {
   //			} else {
   //			}
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //	}
   //
   //	public void testPrintDerive() {
   //		Imprimante imp = new Imprimante();
   //		imp.setNom("PDF");
   //		imp.setAbscisse(0);
   //		imp.setOrdonnee(0);
   //		imp.setLargeur(0);
   //		imp.setLongueur(0);
   //		imp.setOrientation(1);
   //
   //		Modele mod = new Modele();
   //		mod.setTexteLibre("TEXT");
   //		mod.setIsDefault(true);
   //
   //		List<ProdDerive> liste = new ArrayList<ProdDerive>();
   //		liste.add(prodDeriveManager.findByIdManager(1));
   //		liste.add(prodDeriveManager.findByIdManager(2));
   //		liste.add(prodDeriveManager.findByIdManager(3));
   //
   //		try {
   //			int completed = 0;
   //			completed = tumoBarcodePrinter.printDerive(liste, 1, imp, mod, null);
   //
   //			if (completed == 1) {
   //			} else {
   //			}
   //		} catch (Exception e) {
   //			String msg = "ImprimerCodesBarres error! " + e.getMessage();
   //		}
   //	}

   @Test
   public void testPrintForDynamiqueModele(){
      final Imprimante imp = new Imprimante();
      imp.setNom("PDF");
      imp.setAbscisse(0);
      imp.setOrdonnee(0);
      imp.setLargeur(0);
      imp.setLongueur(0);
      imp.setOrientation(1);

      final Modele mod = modeleManager.findByIdManager(2);

      final List<TKStockableObject> liste = new ArrayList<>();
      liste.add(echantillonManager.findByIdManager(1));
      liste.add(echantillonManager.findByIdManager(2));
      liste.add(echantillonManager.findByIdManager(3));
      liste.add(prodDeriveManager.findByIdManager(3));

      try{
         final BarcodeFieldDefault by = new BarcodeFieldDefault(new Float(1.0), new Float(2.35678), 10);

         int completed = 0;
         completed = tumoBarcodePrinter.printStockableObjects(liste, 3, imp, mod, null, by);
         assertEquals(1, completed);

         completed = tumoBarcodePrinter.printStockableObjects(liste, 2, imp, mod, "ZPL", by);
         assertTrue(completed == 1);

         imp.setAdresseIp("127.0.0.1");
         imp.setPort(9100);
         completed = tumoBarcodePrinter.printStockableObjects(liste, 1, imp, mod, "ZPL", by);
         assertTrue(completed == -1);
      }catch(final Exception e){
         final String msg = "ImprimerCodesBarres error! " + e.getMessage();
         System.out.println(msg);
      }
   }

}
