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
package fr.aphp.tumorotek.test.manager.systeme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager EntiteManager.
 * Classe créée le 30/09/09.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class EntiteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private BanqueDao banqueDao;

   public EntiteManagerTest(){}

   @Test
   public void testFindById(){
      final Entite entite = entiteManager.findByIdManager(1);
      assertNotNull(entite);
      assertTrue(entite.getNom().equals("Patient"));

      final Entite entiteNull = entiteManager.findByIdManager(100);
      assertNull(entiteNull);
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
   public void testReadAll(){
      final List<Entite> entites = entiteManager.findAllObjectsManager();
      assertTrue(entites.size() == 64);
   }

   /**
    * Test l'appel de la méthode findByNomManager().
    */
   @Test
   public void testFindByNomManager(){
      List<Entite> entites = entiteManager.findByNomManager("Stockage");
      assertTrue(entites.size() == 1);
      entites = entiteManager.findByNomManager("Test");
      assertTrue(entites.size() == 0);
   }

   @Test
   public void testFindAnnotablesManager(){
      final List<Entite> entites = entiteManager.findAnnotablesManager();
      assertTrue(entites.size() == 5);
      assertTrue(entites.get(2).getNom().equals("Echantillon"));
   }

   /**
    * Test de la méthode findObjectByEntiteAndIdManager().
    */
   @Test
   public void testFindObjectByEntiteAndIdManager(){
      final Entite echan = entiteManager.findByIdManager(3);
      final Object objEchan = entiteManager.findObjectByEntiteAndIdManager(echan, 2);
      assertNotNull(objEchan);
      assertTrue(objEchan.getClass().getSimpleName().equals("Echantillon"));
      final Echantillon newEchan = (Echantillon) objEchan;
      assertNotNull(newEchan);
      assertTrue(newEchan.getEchantillonId() == 2);

      final Entite prod = entiteManager.findByIdManager(8);
      final Object objProd = entiteManager.findObjectByEntiteAndIdManager(prod, 1);
      assertNotNull(objProd);
      assertTrue(objProd.getClass().getSimpleName().equals("ProdDerive"));
      final ProdDerive newProd = (ProdDerive) objProd;
      assertNotNull(newProd);
      assertTrue(newProd.getProdDeriveId() == 1);

      final Entite retour = entiteManager.findByIdManager(19);
      final Object objRetour = entiteManager.findObjectByEntiteAndIdManager(retour, 10);
      assertNull(objRetour);
      
      // @since 2.2.1 AbstractTKChamp utilise ID
      final Entite chpA = entiteManager.findByIdManager(30);
      final Object objChpA = entiteManager.findObjectByEntiteAndIdManager(chpA, 1);
      assertTrue(((ChampAnnotation) objChpA).getId() == 1);
      
   }

   @Test
   public void testFindIdsByEntiteAndIdAfterBanqueFiltreManager(){

      List<Integer> ids = new ArrayList<>();
      final List<Banque> banks = new ArrayList<>();

      final Entite echan = entiteManager.findByIdManager(3);
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(4);

      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(echan, ids, null);
      assertTrue(ids.size() == 4);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));
      assertTrue(ids.contains(4));

      ids.remove(0);
      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(echan, ids, banks);
      assertTrue(ids.size() == 3);
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));
      assertTrue(ids.contains(4));

      banks.add(banqueDao.findById(1));
      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(echan, ids, banks);
      assertTrue(ids.size() == 2);
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));

      final Entite prod = entiteManager.findByIdManager(8);
      banks.clear();
      banks.add(banqueDao.findById(2));
      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prod, ids, banks);
      assertTrue(ids.isEmpty());

      ids.add(4);
      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prod, ids, banks);
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(4));

      banks.add(banqueDao.findById(1));
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(4);
      ids = entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prod, ids, banks);
      assertTrue(ids.size() == 4);
      assertTrue(ids.contains(1));
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));
      assertTrue(ids.contains(4));

      final Entite cession = entiteManager.findByIdManager(5);
      banks.remove(banqueDao.findById(2));
      assertTrue(entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(cession, ids, banks).size() == 3);

      final Entite prelevement = entiteManager.findByIdManager(2);
      assertTrue(entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prelevement, ids, banks).size() == 3);

      final Entite retour = entiteManager.findByIdManager(19);
      assertFalse(entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(retour, ids, banks).isEmpty());

      ids.clear();
      assertTrue(entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prod, ids, banks).isEmpty());

      assertTrue(entiteManager.findIdsByEntiteAndIdAfterBanqueFiltreManager(prod, null, banks).isEmpty());

   }
}
