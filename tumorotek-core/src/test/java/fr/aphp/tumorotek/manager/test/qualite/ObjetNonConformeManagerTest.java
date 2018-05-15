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
package fr.aphp.tumorotek.manager.test.qualite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;

/**
 *
 * Classe de test pour le manager ObjetNonConforme.
 * Classe créée le 09/11/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.10.6
 *
 */
public class ObjetNonConformeManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ObjetNonConformeManager objetNonConformeManager;
   @Autowired
   private NonConformiteDao nonConformiteDao;
   @Autowired
   private ConformiteTypeDao conformiteTypeDao;
   @Autowired
   private PrelevementDao prelevementDao;
   @Autowired
   private EchantillonDao echantillonDao;
   @Autowired
   private ProdDeriveDao prodDeriveDao;
   @Autowired
   private CessionDao cessionDao;
   @Autowired
   @Qualifier("dataSource")
   private DataSource dataSource;

   public ObjetNonConformeManagerTest(){}

   @Test
   public void testFindById(){
      final ObjetNonConforme n = objetNonConformeManager.findByIdManager(1);
      assertNotNull(n);

      final ObjetNonConforme nNull = objetNonConformeManager.findByIdManager(100);
      assertNull(nNull);
   }

   @Test
   public void testFindAll(){
      final List<ObjetNonConforme> list = objetNonConformeManager.findAllObjectsManager();
      assertEquals(6, list.size());
   }

   @Test
   public void testFindByObjetManager(){
      List<ObjetNonConforme> liste = objetNonConformeManager.findByObjetManager(prelevementDao.findById(2));
      assertTrue(liste.size() == 1);

      liste = objetNonConformeManager.findByObjetManager(prelevementDao.findById(1));
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetManager(echantillonDao.findById(2));
      assertTrue(liste.size() == 1);

      liste = objetNonConformeManager.findByObjetManager(echantillonDao.findById(3));
      assertTrue(liste.size() == 2);

      liste = objetNonConformeManager.findByObjetManager(echantillonDao.findById(1));
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetManager(prodDeriveDao.findById(3));
      assertTrue(liste.size() == 2);

      liste = objetNonConformeManager.findByObjetManager(cessionDao.findById(3));
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetManager(null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindByObjetAndTypeManager(){
      List<ObjetNonConforme> liste = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(2), "Arrivee");
      assertTrue(liste.size() == 1);
      liste = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(2), conformiteTypeDao.findById(1));
      assertTrue(liste.size() == 1);

      liste = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee");
      assertTrue(liste.size() == 0);
      liste = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(2), "Traitement");
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(3), "Traitement");
      assertTrue(liste.size() == 1);
      liste = objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(3), "Cession");
      assertTrue(liste.size() == 1);
      liste = objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(3), "Autre");
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement");
      assertTrue(liste.size() == 0);
      liste = objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(3), "Traitement");
      assertTrue(liste.size() == 1);

      liste = objetNonConformeManager.findByObjetAndTypeManager(cessionDao.findById(3), "Traitement");
      assertTrue(liste.size() == 0);

      liste = objetNonConformeManager.findByObjetAndTypeManager(null, "Traitement");
      assertTrue(liste.size() == 0);
      liste = objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(3), null);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testCrud(){
      // on test avec des valeurs null
      objetNonConformeManager.createUpdateOrRemoveObjectManager(prelevementDao.findById(1), nonConformiteDao.findById(1), null);
      assertEquals(6, objetNonConformeManager.findAllObjectsManager().size());

      objetNonConformeManager.createUpdateOrRemoveObjectManager(null, nonConformiteDao.findById(1), "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // création valide pour un prlvt
      objetNonConformeManager.createUpdateOrRemoveObjectManager(prelevementDao.findById(1), nonConformiteDao.findById(1),
         "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 7);
      final ObjetNonConforme o1 = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").get(0);
      assertTrue(o1.getObjetId() == 1);
      assertTrue(o1.getEntite().getNom().equals("Prelevement"));
      assertTrue(o1.getNonConformite().equals(nonConformiteDao.findById(1)));

      // création valide pour un échantillon
      objetNonConformeManager.createUpdateOrRemoveObjectManager(echantillonDao.findById(1), nonConformiteDao.findById(4),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 8);
      final ObjetNonConforme o2 =
         objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").get(0);
      assertTrue(o2.getObjetId() == 1);
      assertTrue(o2.getEntite().getNom().equals("Echantillon"));
      assertTrue(o2.getNonConformite().equals(nonConformiteDao.findById(4)));

      // création valide pour un dérivé
      objetNonConformeManager.createUpdateOrRemoveObjectManager(prodDeriveDao.findById(1), nonConformiteDao.findById(8),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      final ObjetNonConforme o2Bis =
         objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").get(0);
      assertTrue(o2Bis.getObjetId() == 1);
      assertTrue(o2Bis.getEntite().getNom().equals("ProdDerive"));
      assertTrue(o2Bis.getNonConformite().equals(nonConformiteDao.findById(8)));

      // update valide pour un prlvt
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prelevementDao.findById(1), 
      //				nonConformiteDao.findById(2), 
      //				"Arrivee");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o3 = objetNonConformeManager.findByObjetAndTypeManager(
      //				prelevementDao.findById(1), "Arrivee").get(0);
      //		assertTrue(o3.getObjetId() == 1);
      //		assertTrue(o3.getEntite().getNom().equals("Prelevement"));
      //		assertTrue(o3.getNonConformite().equals(nonConformiteDao.findById(2)));

      // update valide pour un échantillon
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				echantillonDao.findById(1), 
      //				nonConformiteDao.findById(5), 
      //				"Traitement");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o4 = objetNonConformeManager.findByObjetAndTypeManager(
      //				echantillonDao.findById(1), "Traitement").get(0);
      //		assertTrue(o4.getObjetId() == 1);
      //		assertTrue(o4.getEntite().getNom().equals("Echantillon"));
      //		assertTrue(o4.getNonConformite().equals(nonConformiteDao.findById(5)));

      // update valide pour un dérivé
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prodDeriveDao.findById(1), 
      //				nonConformiteDao.findById(9), 
      //				"Traitement");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o4Bis = objetNonConformeManager
      //			.findByObjetAndTypeManager(
      //				prodDeriveDao.findById(1), "Traitement").get(0);
      //		assertTrue(o4Bis.getObjetId() == 1);
      //		assertTrue(o4Bis.getEntite().getNom().equals("ProdDerive"));
      //		assertTrue(o4Bis.getNonConformite().equals(
      //				nonConformiteDao.findById(9)));		
      //		

      // update sans chgt pour un prlvt
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prelevementDao.findById(1), 
      //				nonConformiteDao.findById(2), 
      //				"Arrivee");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o5 = objetNonConformeManager.findByObjetAndTypeManager(
      //				prelevementDao.findById(1), "Arrivee").get(0);
      //		assertTrue(o5.getObjetId() == 1);
      //		assertTrue(o5.getEntite().getNom().equals("Prelevement"));
      //		assertTrue(o5.getNonConformite().equals(nonConformiteDao.findById(2)));

      // update sans chgt pour un échantillon
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				echantillonDao.findById(1), 
      //				nonConformiteDao.findById(5), 
      //				"Traitement");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o6 = objetNonConformeManager.findByObjetAndTypeManager(
      //				echantillonDao.findById(1), "Traitement").get(0);
      //		assertTrue(o6.getObjetId() == 1);
      //		assertTrue(o6.getEntite().getNom().equals("Echantillon"));
      //		assertTrue(o6.getNonConformite().equals(nonConformiteDao.findById(5)));
      //		
      //		// update sans chgt pour un dérivé
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prodDeriveDao.findById(1), 
      //				nonConformiteDao.findById(5), 
      //				"Traitement");
      //		assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      //		ObjetNonConforme o6Bis = objetNonConformeManager
      //			.findByObjetAndTypeManager(
      //				prodDeriveDao.findById(1), "Traitement").get(0);
      //		assertTrue(o6Bis.getObjetId() == 1);
      //		assertTrue(o6Bis.getEntite().getNom().equals("ProdDerive"));
      //		assertTrue(o6Bis.getNonConformite().equals(
      //				nonConformiteDao.findById(5)));

      // Suppression pour un prlvt
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prelevementDao.findById(1), 
      //				null, 
      //				"Arrivee");
      objetNonConformeManager.removeObjectManager(o1);
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 8);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").size() == 0);

      // suppression pour un échantillon
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				echantillonDao.findById(1), 
      //				null, 
      //				"Traitement");
      objetNonConformeManager.removeObjectManager(o2);
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 7);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").size() == 0);

      // suppression pour un dérivé
      //		objetNonConformeManager.createUpdateOrRemoveObjectManager(
      //				prodDeriveDao.findById(1), 
      //				null, 
      //				"Traitement");
      objetNonConformeManager.removeObjectManager(o2Bis);
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").size() == 0);

      // Suppression pour un prlvt sans non conformité
      objetNonConformeManager.createUpdateOrRemoveObjectManager(prelevementDao.findById(1), null, "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // suppression pour un échantillon sans non conformité
      objetNonConformeManager.createUpdateOrRemoveObjectManager(echantillonDao.findById(1), null, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // suppression pour un dérivé sans non conformité
      objetNonConformeManager.createUpdateOrRemoveObjectManager(prodDeriveDao.findById(1), null, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // entite type non conformité incompatible
      boolean catched = false;
      try{
         objetNonConformeManager.createUpdateOrRemoveObjectManager(prodDeriveDao.findById(1), nonConformiteDao.findById(4),
            "Traitement");
      }catch(final RuntimeException re){
         catched = true;
      }
      assertTrue(catched);
   }

   @Test
   public void testCrudList(){
      List<NonConformite> liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(1));
      liste.add(nonConformiteDao.findById(2));
      // on test avec des valeurs null
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), liste, null);
      assertEquals(6, objetNonConformeManager.findAllObjectsManager().size());

      objetNonConformeManager.createUpdateOrRemoveListObjectManager(null, liste, "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), null, "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // création valide pour un prlvt
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), liste, "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 8);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").size() == 2);
      final ObjetNonConforme o1 = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").get(0);
      assertTrue(o1.getObjetId() == 1);
      assertTrue(o1.getEntite().getNom().equals("Prelevement"));
      assertTrue(o1.getNonConformite().equals(nonConformiteDao.findById(2)));

      // création valide pour un échantillon
      liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(4));
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillonDao.findById(1), liste, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 9);
      final ObjetNonConforme o2 =
         objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").get(0);
      assertTrue(o2.getObjetId() == 1);
      assertTrue(o2.getEntite().getNom().equals("Echantillon"));
      assertTrue(o2.getNonConformite().equals(nonConformiteDao.findById(4)));

      // création valide pour un dérivé
      liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(8));
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prodDeriveDao.findById(1), liste, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 10);
      final ObjetNonConforme o2B =
         objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").get(0);
      assertTrue(o2B.getObjetId() == 1);
      assertTrue(o2B.getEntite().getNom().equals("ProdDerive"));
      assertTrue(o2B.getNonConformite().equals(nonConformiteDao.findById(8)));

      // update valide pour un prlvt
      liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(2));
      liste.add(nonConformiteDao.findById(3));
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), liste, "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 10);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").size() == 2);
      final ObjetNonConforme o3 = objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").get(0);
      assertTrue(o3.getObjetId() == 1);
      assertTrue(o3.getEntite().getNom().equals("Prelevement"));
      assertTrue(o3.getNonConformite().equals(nonConformiteDao.findById(2)));

      // update valide pour un échantillon
      liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(4));
      liste.add(nonConformiteDao.findById(5));
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillonDao.findById(1), liste, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 11);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").size() == 2);
      final ObjetNonConforme o4 =
         objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").get(0);
      assertTrue(o4.getObjetId() == 1);
      assertTrue(o4.getEntite().getNom().equals("Echantillon"));
      assertTrue(o4.getNonConformite().equals(nonConformiteDao.findById(5)));

      // update valide pour un dérivé
      liste = new ArrayList<>();
      liste.add(nonConformiteDao.findById(8));
      liste.add(nonConformiteDao.findById(9));
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prodDeriveDao.findById(1), liste, "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 12);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").size() == 2);
      final ObjetNonConforme o4B =
         objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").get(0);
      assertTrue(o4B.getObjetId() == 1);
      assertTrue(o4B.getEntite().getNom().equals("ProdDerive"));
      assertTrue(o4B.getNonConformite().equals(nonConformiteDao.findById(9)));

      // Suppression pour un prlvt
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), new ArrayList<NonConformite>(),
         "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 10);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").size() == 0);

      // suppression pour un échantillon
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillonDao.findById(1), new ArrayList<NonConformite>(),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 8);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").size() == 0);

      // suppression pour un dérivé
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prodDeriveDao.findById(1), new ArrayList<NonConformite>(),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);
      assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").size() == 0);

      // Suppression pour un prlvt sans non conformité
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), new ArrayList<NonConformite>(),
         "Arrivee");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // suppression pour un échantillon sans non conformité
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillonDao.findById(1), new ArrayList<NonConformite>(),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

      // suppression pour un dérivé sans non conformité
      objetNonConformeManager.createUpdateOrRemoveListObjectManager(prodDeriveDao.findById(1), new ArrayList<NonConformite>(),
         "Traitement");
      assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);
   }

   @Test
   public void testFindObjetIdsByNonConformitesManager(){
      final List<NonConformite> noconfs = new ArrayList<>();
      noconfs.add(nonConformiteDao.findById(4));
      noconfs.add(nonConformiteDao.findById(1));
      noconfs.add(nonConformiteDao.findById(8));
      final List<Integer> ids = objetNonConformeManager.findObjetIdsByNonConformitesManager(noconfs);
      assertEquals(2, ids.size());
      assertTrue(ids.contains(2));
      assertTrue(ids.contains(3));

      noconfs.clear();

      assertTrue(objetNonConformeManager.findObjetIdsByNonConformitesManager(noconfs).isEmpty());
      assertTrue(objetNonConformeManager.findObjetIdsByNonConformitesManager(null).isEmpty());
   }

   @Test
   public void testCrudListJDBC() throws SQLException{

      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      final EchantillonJdbcSuite suite = new EchantillonJdbcSuite();
      try{
         conn = DataSourceUtils.getConnection(dataSource);

         stmt = conn.createStatement();

         rs = stmt.executeQuery("select max(objet_non_conforme_id) " + "from OBJET_NON_CONFORME");
         rs.first();
         final Integer maxOnId = rs.getInt(1);
         suite.setMaxObjetNonConformeId(maxOnId);

         final String sql5 = "insert into OBJET_NON_CONFORME (OBJET_NON_CONFORME_ID, "
            + "OBJET_ID, ENTITE_ID, NON_CONFORMITE_ID) " + "values (?,?,?,?)";
         suite.setPstmtNc(conn.prepareStatement(sql5));

         List<NonConformite> liste = new ArrayList<>();

         // on test avec des valeurs null ou vides
         objetNonConformeManager.prepareListJDBCManager(suite, null, liste);
         assertEquals(6, objetNonConformeManager.findAllObjectsManager().size());
         objetNonConformeManager.prepareListJDBCManager(suite, echantillonDao.findById(1), null);
         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);
         objetNonConformeManager.prepareListJDBCManager(suite, echantillonDao.findById(1), liste);
         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

         // id n'a pas changé
         assertTrue(suite.getMaxObjetNonConformeId().equals(maxOnId));

         liste.add(nonConformiteDao.findById(1));
         liste.add(nonConformiteDao.findById(2));

         // requiredExceptions
         final Echantillon echan = new Echantillon();
         boolean catched = false;
         try{
            objetNonConformeManager.prepareListJDBCManager(suite, echan, liste);
         }catch(final RequiredObjectIsNullException e){
            catched = true;
         }
         assertTrue(catched);
         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

         // rollback conformite type incohérente
         liste.add(nonConformiteDao.findById(4));

         catched = false;
         try{
            objetNonConformeManager.prepareListJDBCManager(suite, prelevementDao.findById(1), liste);
         }catch(final RuntimeException e){
            assertTrue(e.getMessage().equals("conformiteType.entite.illegal"));
            catched = true;
         }
         assertTrue(catched);
         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);

         // id n'a pas changé
         assertTrue(suite.getMaxObjetNonConformeId().equals(maxOnId));

         liste.clear();
         liste.add(nonConformiteDao.findById(1));
         liste.add(nonConformiteDao.findById(2));

         suite.getPstmtNc().clearBatch();

         // création valide pour un prlvt
         objetNonConformeManager.prepareListJDBCManager(suite, prelevementDao.findById(1), liste);

         // id a hangé
         assertTrue(suite.getMaxObjetNonConformeId().equals(maxOnId + 2));

         // création valide pour un échantillon
         liste.clear();
         liste.add(nonConformiteDao.findById(4));
         objetNonConformeManager.prepareListJDBCManager(suite, echantillonDao.findById(1), liste);

         // id a hangé
         assertTrue(suite.getMaxObjetNonConformeId().equals(maxOnId + 3));

         // création valide pour un dérivé
         liste = new ArrayList<>();
         liste.add(nonConformiteDao.findById(8));
         objetNonConformeManager.prepareListJDBCManager(suite, prodDeriveDao.findById(1), liste);

         // id a hangé
         assertTrue(suite.getMaxObjetNonConformeId().equals(maxOnId + 4));

         suite.getPstmtNc().executeBatch();

         //asserts
         // Prelevement
         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 10);
         assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").size() == 2);
         final ObjetNonConforme o1 =
            objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").get(0);
         assertTrue(o1.getObjetId() == 1);
         assertTrue(o1.getEntite().getNom().equals("Prelevement"));
         assertTrue(o1.getNonConformite().equals(nonConformiteDao.findById(2)));
         // Echantilon
         final ObjetNonConforme o2 =
            objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").get(0);
         assertTrue(o2.getObjetId() == 1);
         assertTrue(o2.getEntite().getNom().equals("Echantillon"));
         assertTrue(o2.getNonConformite().equals(nonConformiteDao.findById(4)));
         // Derive
         final ObjetNonConforme o2B =
            objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").get(0);
         assertTrue(o2B.getObjetId() == 1);
         assertTrue(o2B.getEntite().getNom().equals("ProdDerive"));
         assertTrue(o2B.getNonConformite().equals(nonConformiteDao.findById(8)));

         // Suppressions
         objetNonConformeManager.createUpdateOrRemoveListObjectManager(prelevementDao.findById(1), new ArrayList<NonConformite>(),
            "Arrivee");
         objetNonConformeManager.createUpdateOrRemoveListObjectManager(echantillonDao.findById(1), new ArrayList<NonConformite>(),
            "Traitement");
         objetNonConformeManager.createUpdateOrRemoveListObjectManager(prodDeriveDao.findById(1), new ArrayList<NonConformite>(),
            "Traitement");

         assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prelevementDao.findById(1), "Arrivee").isEmpty());
         assertTrue(objetNonConformeManager.findByObjetAndTypeManager(echantillonDao.findById(1), "Traitement").isEmpty());
         assertTrue(objetNonConformeManager.findByObjetAndTypeManager(prodDeriveDao.findById(1), "Traitement").isEmpty());

         assertTrue(objetNonConformeManager.findAllObjectsManager().size() == 6);
      }catch(final Exception e){
         e.printStackTrace();
      }finally{
         if(null != stmt)
            stmt.close();
         if(null != rs)
            rs.close();
         if(null != conn)
            conn.close();
         suite.closePs();
      }
   }

}
