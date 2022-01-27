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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.etiquettes.TumoPrinterUtilsManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

public class TumoPrinterUtilsManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private TumoPrinterUtilsManager tumoPrinterUtilsManager;
   @Autowired
   private ModeleManager modeleManager;

   public TumoPrinterUtilsManagerTest(){}

   @Test
   public void testExtractStaticDataForEchantillon(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      List<LigneEtiquette> data = tumoPrinterUtilsManager.createListEtiquette(e1, null);

      assertTrue(data.size() == 8);
      assertTrue(data.get(0).getContenu().equals("PTRA"));
      assertTrue(data.get(1).getContenu().equals("PTRA"));
      assertTrue(data.get(2).getContenu().equals(".1"));
      assertTrue(data.get(3).getContenu().equals(".1"));
      assertTrue(data.get(4).getContenu().equals("CELLULES"));
      assertTrue(data.get(5).getContenu().equals("ELP"));
      assertTrue(data.get(6).getContenu().equals(""));
      assertTrue(data.get(7).getContenu().equals(""));

      final Modele mod = new Modele();
      final Echantillon e2 = echantillonManager.findByIdManager(2);
      data = tumoPrinterUtilsManager.createListEtiquette(e2, mod);

      assertTrue(data.size() == 8);
      assertTrue(data.get(0).getContenu().equals("PTRA"));
      assertTrue(data.get(1).getContenu().equals("PTRA"));
      assertTrue(data.get(2).getContenu().equals(".2"));
      assertTrue(data.get(3).getContenu().equals(".2"));
      assertTrue(data.get(4).getContenu().equals("CELLULES"));
      assertTrue(data.get(5).getContenu().equals("ELP"));
      assertTrue(data.get(6).getContenu().equals("16/03/2008"));
      assertTrue(data.get(7).getContenu().equals("25.0 FRAGMENTS"));

      mod.setTexteLibre("TEST");
      data = tumoPrinterUtilsManager.createListEtiquette(e2, mod);

      assertTrue(data.size() == 9);
      assertTrue(data.get(0).getContenu().equals("PTRA"));
      assertTrue(data.get(1).getContenu().equals("PTRA"));
      assertTrue(data.get(2).getContenu().equals(".2"));
      assertTrue(data.get(3).getContenu().equals(".2"));
      assertTrue(data.get(4).getContenu().equals("CELLULES"));
      assertTrue(data.get(5).getContenu().equals("ELP"));
      assertTrue(data.get(6).getContenu().equals("16/03/2008"));
      assertTrue(data.get(7).getContenu().equals("25.0 FRAGMENTS"));
      assertTrue(data.get(8).getContenu().equals("TEST"));
   }

   @Test
   public void testExtractStaticDataForProdDerive(){
      final ProdDerive p1 = prodDeriveManager.findByIdManager(1);
      List<LigneEtiquette> data = tumoPrinterUtilsManager.createListEtiquette(p1, null);

      assertTrue(data.size() == 8);
      assertTrue(data.get(0).getContenu().equals("PTRA"));
      assertTrue(data.get(1).getContenu().equals("PTRA"));
      assertTrue(data.get(2).getContenu().equals(".1.1"));
      assertTrue(data.get(3).getContenu().equals(".1.1"));
      assertTrue(data.get(4).getContenu().equals("ADN"));
      assertTrue(data.get(5).getContenu().equals("ELP"));
      assertTrue(data.get(6).getContenu().equals("16/03/2009"));
      assertTrue(data.get(7).getContenu().equals("10.0 FRAGMENTS"));

      final ProdDerive p3 = prodDeriveManager.findByIdManager(3);
      data = tumoPrinterUtilsManager.createListEtiquette(p3, null);

      assertTrue(data.size() == 8);
      assertTrue(data.get(0).getContenu().equals("EHT"));
      assertTrue(data.get(1).getContenu().equals("EHT"));
      assertTrue(data.get(2).getContenu().equals(".1.1"));
      assertTrue(data.get(3).getContenu().equals(".1.1"));
      assertTrue(data.get(4).getContenu().equals("ARN"));
      assertTrue(data.get(5).getContenu().equals("ELP"));
      assertTrue(data.get(6).getContenu().equals("08/07/2009"));
      assertTrue(data.get(7).getContenu().equals(""));
   }

   @Test
   public void testExtractDynDataForEchantillon(){
      final Echantillon e1 = echantillonManager.findByIdManager(1);
      final Modele m2 = modeleManager.findByIdManager(2);
      List<LigneEtiquette> data = tumoPrinterUtilsManager.extractDynData(e1, m2);
      assertTrue(data.size() == 7);
      assertTrue(data.get(0).getContenu().equals("PTRA.1"));
      assertTrue(data.get(1).getEntete().equals("Prel :"));
      assertTrue(data.get(1).getContenu().equals("PRLVT1"));
      assertTrue(data.get(2).getEntete().equals("Tube :"));
      assertTrue(data.get(2).getContenu().equals("1"));
      assertTrue(data.get(3).getEntete().equals("Pat :"));
      assertTrue(data.get(3).getContenu().equals("DEL MI"));
      assertTrue(data.get(4).getEntete().equals("Date :"));
      assertTrue(data.get(4).getContenu().equals(""));
      assertTrue(data.get(5).getEntete().equals("Qte :"));
      assertTrue(data.get(5).getContenu().equals(""));
      assertTrue(data.get(6).getContenu().equals("TumoroteK"));

      final Echantillon e2 = echantillonManager.findByIdManager(2);
      data = tumoPrinterUtilsManager.extractDynData(e2, m2);
      assertTrue(data.size() == 7);
      assertTrue(data.get(0).getContenu().equals("PTRA.2"));
      assertTrue(data.get(1).getEntete().equals("Prel :"));
      assertTrue(data.get(1).getContenu().equals("PRLVT1"));
      assertTrue(data.get(2).getEntete().equals("Tube :"));
      assertTrue(data.get(2).getContenu().equals("2"));
      assertTrue(data.get(3).getEntete().equals("Pat :"));
      assertTrue(data.get(3).getContenu().equals("DEL MI"));
      assertTrue(data.get(4).getEntete().equals("Date :"));
      assertTrue(data.get(4).getContenu().equals("16/03/2008"));
      assertTrue(data.get(5).getEntete().equals("Qte :"));
      assertTrue(data.get(5).getContenu().equals("25.0"));
      assertTrue(data.get(6).getContenu().equals("TumoroteK"));

      assertTrue(tumoPrinterUtilsManager.extractDynData(null, m2).size() == 0);
      assertTrue(tumoPrinterUtilsManager.extractDynData(e1, null).size() == 0);
      assertTrue(tumoPrinterUtilsManager.extractDynData(null, null).size() == 0);
   }

   @Test
   public void testExtractDynDataForProdDerive(){
      final ProdDerive p1 = prodDeriveManager.findByIdManager(1);
      final Modele m2 = modeleManager.findByIdManager(2);
      List<LigneEtiquette> data = tumoPrinterUtilsManager.extractDynData(p1, m2);
      assertTrue(data.size() == 7);
      assertTrue(data.get(0).getContenu().equals("PTRA.1.1"));
      assertTrue(data.get(1).getEntete().equals("Prel :"));
      assertTrue(data.get(1).getContenu().equals("PRLVT1"));
      assertTrue(data.get(2).getEntete().equals("Tube :"));
      assertTrue(data.get(2).getContenu().equals("1.1"));
      assertTrue(data.get(3).getEntete().equals("Pat :"));
      assertTrue(data.get(3).getContenu().equals(""));
      assertTrue(data.get(4).getEntete().equals("Date :"));
      assertTrue(data.get(4).getContenu().equals(""));
      assertTrue(data.get(5).getEntete().equals("Qte :"));
      assertTrue(data.get(5).getContenu().equals(""));
      assertTrue(data.get(6).getContenu().equals("TumoroteK"));

      final ProdDerive p2 = prodDeriveManager.findByIdManager(2);
      data = tumoPrinterUtilsManager.extractDynData(p2, m2);
      assertTrue(data.size() == 7);
      assertTrue(data.get(0).getContenu().equals("PTRA.1.2"));
      assertTrue(data.get(1).getEntete().equals("Prel :"));
      assertTrue(data.get(1).getContenu().equals("PRLVT1"));
      assertTrue(data.get(2).getEntete().equals("Tube :"));
      assertTrue(data.get(2).getContenu().equals("1.2"));
      assertTrue(data.get(3).getEntete().equals("Pat :"));
      assertTrue(data.get(3).getContenu().equals(""));
      assertTrue(data.get(4).getEntete().equals("Date :"));
      assertTrue(data.get(4).getContenu().equals(""));
      assertTrue(data.get(5).getEntete().equals("Qte :"));
      assertTrue(data.get(5).getContenu().equals(""));
      assertTrue(data.get(6).getContenu().equals("TumoroteK"));

      assertTrue(tumoPrinterUtilsManager.extractDynData(null, m2).size() == 0);
      assertTrue(tumoPrinterUtilsManager.extractDynData(p1, null).size() == 0);
      assertTrue(tumoPrinterUtilsManager.extractDynData(null, null).size() == 0);
   }

}
