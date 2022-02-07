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
package fr.aphp.tumorotek.manager.test.dto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.dto.EchantillonDTOManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

/**
 *
 * Classe de test pour le manager EchantillonDTOManager.
 * Classe créée le 06/02/17.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1.
 *
 */
public class EchantillonDTOManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EchantillonDTOManager echantillonDTOManager;
   @Autowired
   private EchantillonDao echantillonDao;

   public EchantillonDTOManagerTest(){}

   @Test
   public void testInitEchantillonDecoratorManager(){
      EchantillonDTO dto = echantillonDTOManager.initEchantillonDecoratorManager(null);
      assertTrue(dto == null);

      // empty Echantillon
      dto = echantillonDTOManager.initEchantillonDecoratorManager(new Echantillon());
      assertTrue(dto.isNew());
      assertTrue(dto.getCode() == null);
      assertTrue(dto.getBanque() == null);
      // assertTrue(dto.getPrelevement() == null);
      assertTrue(dto.getDateStockage() == null);
      assertTrue(dto.getType() == null);
      assertTrue(dto.getQuantite().equals("- / -"));
      assertTrue(dto.getQuantiteInitiale().equals("-"));
      assertTrue(dto.getStatut() == null);
      assertTrue(dto.getNbDerives() == 0);
      assertTrue(dto.getNbCessions() == 0);
      assertTrue(dto.getDateCreation() == null);
      assertTrue(dto.getCollaborateurNomAndPrenom().equals(""));
      assertTrue(dto.getEmplacementAdrl() == null);
      assertTrue(dto.getTempStock() == null);
      assertTrue(dto.getEmplacementAdrlinMulti() == null);
      assertTrue(dto.getDelaiDeCongelation() == null);
      assertTrue(dto.getQualite() == null);
      assertTrue(dto.getTumoral() == null);
      assertTrue(dto.getModeDePreparation() == null);
      assertTrue(dto.getSterile() == null);
      assertTrue(dto.getEtatIncomplet() == null);
      assertTrue(dto.getArchive().equals("non"));

      // echantillon test2
      dto = echantillonDTOManager.initEchantillonDecoratorManager(echantillonDao.findById(2)).orElse(null);
      assertFalse(dto.isNew());
      assertTrue(dto.getCode().equals("PTRA.2"));
      assertTrue(dto.getBanque().equals("BANQUE1"));
      // assertTrue(dto.getPrelevement().equals("PTRA"));
      assertTrue(new SimpleDateFormat("yyyyMMddHHmmss").format(dto.getDateStockage().getTime()).equals("20080316000000"));
      assertTrue(dto.getType().equals("CELLULES"));
      assertTrue(dto.getQuantite().equals("25.0 / 25.0 FRAGMENTS"));
      assertTrue(dto.getQuantiteInitiale().equals("25.0 FRAGMENTS"));
      assertTrue(dto.getStatut().getStatut().equals("STOCKE"));
      assertTrue(dto.getNbDerives() == 0);
      assertTrue(dto.getNbCessions() == 1);
      assertTrue(new SimpleDateFormat("yyyyMMddHHmmss").format(dto.getDateCreation().getTime()).equals("20091031000000"));
      assertTrue(dto.getCollaborateurNomAndPrenom().equals("VIAL CHRISTOPHE"));
      assertTrue(dto.getEmplacementAdrl().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(dto.getTempStock().equals("-75.0"));
      assertTrue(dto.getEmplacementAdrlinMulti().equals("CC1.R1.T1.BT1.A-C"));
      assertTrue(dto.getDelaiDeCongelation() == null);
      assertTrue(dto.getQualite().equals("MELANGE MO"));
      assertTrue(dto.getTumoral() == null);
      assertTrue(dto.getModeDePreparation().equals("PREPA1"));
      assertTrue(dto.getSterile() == null);
      assert (dto.getEtatIncomplet().equals("non"));
      assertTrue(dto.getArchive().equals("non"));
   }

   @Test
   public void testDecorateAndUnDecorateManager(){
      final List<Echantillon> echans = IterableUtils.toList(echantillonDao.findAll());

      final List<EchantillonDTO> dtos = echantillonDTOManager.decorateListeManager(echans);
      assertTrue(dtos.size() == echans.size());

      final List<Echantillon> extracted = echantillonDTOManager.extractListeManager(dtos);
      assertTrue(extracted.containsAll(echans));
      assertTrue(echans.containsAll(extracted));

   }

}
