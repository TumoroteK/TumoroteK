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
package fr.aphp.tumorotek.dao.test.contexte;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.CoordonneeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.test.PopulateBeanForTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Coordonnee;
//import fr.aphp.tumorotek.model.contexte.Etablissement;
//import fr.aphp.tumorotek.model.contexte.Service;
//import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Classe de test pour le DAO CoordonneDao et le bean du domaine Coordonnee.
 *
 * @author Pierre Ventadour
 * @version 09/09/2009
 *
 */
public class CoordonneeDaoTest extends AbstractDaoTest
{


   @Autowired
 CoordonneeDao coordonneeDao;
   //	@Autowired
 EtablissementDao etablissementDao;
   //	@Autowired
 ServiceDao serviceDao;
   //	@Autowired
 TransporteurDao transporteur;

   /** valeur du nom pour la maj. */
   @Autowired
 final String updatedAdr = "Adr mise a jour";

   /** Constructeur. */
   public CoordonneeDaoTest(){

   }

   /**
    * Setter du bean Dao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setCoordonneeDao(final CoordonneeDao cDao){
      this.coordonneeDao = cDao;
   }

   //	/**
   //	 * Setter du bean etablissementDao.
   //	 * @param eDao est le bean Dao.
   //	 */
   //	@Test
public void setEtablissementDao(EtablissementDao eDao) {
   //		this.etablissementDao = eDao;
   //	}
   //	
   //	/**
   //	 * Setter du bean serviceDao.
   //	 * @param sDao est le bean Dao.
   //	 */
   //	@Test
public void setServiceDao(ServiceDao sDao) {
   //		this.serviceDao = sDao;
   //	}
   //	
   //	/**
   //	 * Setter du bean transporteurDao.
   //	 * @param tDao est le bean Dao.
   //	 */
   //	@Test
public void setTransporteurDao(TransporteurDao tDao) {
   //		this.transporteurDao = tDao;
   //	}

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllCoordonnees(){
      final List<Coordonnee> coordonnees = IterableUtils.toList(coordonneeDao.findAll());
      assertTrue(coordonnees.size() == 5);
   }

   /**
    * Test l'appel de la méthode findByAdresse().
    */
   @Test
public void testFindByAdresse(){
      List<Coordonnee> coordonnees = coordonneeDao.findByAdresse("40 rue Worth");
      assertTrue(coordonnees.size() == 1);
      //pour verifier que la relation one-to-one fonctionne
      assertTrue(coordonnees.iterator().next().getEtablissement().getEtablissementId() == 2);
      coordonnees = coordonneeDao.findByAdresse("Adr 1");
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCp().
    */
   @Test
public void testFindByCp(){
      List<Coordonnee> coordonnees = coordonneeDao.findByCp("75010");
      assertTrue(coordonnees.size() == 1);
      coordonnees = coordonneeDao.findByCp("43100");
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByVille().
    */
   @Test
public void testFindByVille(){
      List<Coordonnee> coordonnees = coordonneeDao.findByVille("PARIS");
      assertTrue(coordonnees.size() == 1);
      coordonnees = coordonneeDao.findByVille("LILLE");
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPays().
    */
   @Test
public void testFindByPays(){
      List<Coordonnee> coordonnees = coordonneeDao.findByPays("FRANCE");
      assertTrue(coordonnees.size() == 5);
      coordonnees = coordonneeDao.findByPays("ITALIE");
      assertTrue(coordonnees.size() == 0);
   }

   //	/**
   //	 * Test l'appel de la méthode findByEtablissement().
   //	 */
   //	@Test
public void testFindByEtablissement() {
   //		Etablissement e = etablissementDao.findById(1);
   //		List<Coordonnee> coordonnees = coordonneeDao.findByEtablissement(e);
   //		assertTrue(coordonnees.size() == 1);
   //		e = etablissementDao.findById(10);
   //		coordonnees = coordonneeDao.findByEtablissement(e);
   //		assertTrue(coordonnees.size() == 0);
   //	}
   //	
   //	/**
   //	 * Test l'appel de la méthode findByService().
   //	 */
   //	@Test
public void testFindByServiceId() {
   //		Service s = serviceDao.findById(1);
   //		List<Coordonnee> coordonnees = coordonneeDao.findByService(s);
   //		assertTrue(coordonnees.size() == 1);
   //		assertTrue(coordonnees.get(0).getCoordonneeId().equals(1));
   //		s = serviceDao.findById(10);
   //		coordonnees = coordonneeDao.findByService(s);
   //		assertTrue(coordonnees.size() == 0);
   //	}
   //	
   //	/**
   //	 * Test l'appel de la méthode findByTransporteur().
   //	 */
   //	@Test
public void testFindByTransporteur() {
   //		Transporteur t = transporteur.findById(1);
   //		List<Coordonnee> coordonnees = coordonneeDao.findByTransporteur(t);
   //		assertTrue(coordonnees.size() == 1);
   //		assertTrue(coordonnees.get(0).getCoordonneeId().equals(1));
   //		t = transporteur.findById(10);
   //		coordonnees = coordonneeDao.findByTransporteur(t);
   //		assertTrue(coordonnees.size() == 0);
   //	}

   /**
    * Test l'appel de la méthode findByCollaborateurId().
    */
   @Test
public void testFindByCollaborateurId(){
      List<Coordonnee> coordonnees = coordonneeDao.findByCollaborateurId(4);
      assertTrue(coordonnees.size() == 1);
      assertTrue(coordonnees.get(0).getCoordonneeId().equals(3));
      coordonnees = coordonneeDao.findByCollaborateurId(10);
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateurIdAndExcludedId().
    */
   @Test
public void testFindByCollaborateurIdAndExcludedId(){
      List<Coordonnee> coordonnees = coordonneeDao.findByCollaborateurIdAndExcludedId(4, 3);
      assertTrue(coordonnees.size() == 0);

      coordonnees = coordonneeDao.findByCollaborateurIdAndExcludedId(4, 2);
      assertTrue(coordonnees.size() == 1);
      assertTrue(coordonnees.get(0).getCoordonneeId().equals(3));

      coordonnees = coordonneeDao.findByCollaborateurIdAndExcludedId(10, 1);
      assertTrue(coordonnees.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<Coordonnee> list = coordonneeDao.findByExcludedId(1);
      assertTrue(list.size() == 4);

      list = coordonneeDao.findByExcludedId(10);
      assertTrue(list.size() == 5);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'une coordonnee.
    * @throws Exception lance une exception en cas d'eereur sur les données.
    */
   @Rollback(false)
   @Test
public void testCrudCoordonnee() throws Exception{

      final Coordonnee c = new Coordonnee();

      // on remplit la nouvelle coordonnee avec les données du fichier
      // "coordonnee.properties"
      try{
         PopulateBeanForTest.populateBean(c, "coordonnee");
      }catch(final Exception exc){
         System.out.println(exc.getMessage());
      }
      // Test de l'insertion
      coordonneeDao.save(c);
      final int id = c.getCoordonneeId();
      assertTrue(IterableUtils.toList(coordonneeDao.findAll()).size() == 6);

      // Test de l'update
      final Coordonnee c2 = coordonneeDao.findById(id);
      // Vérification des données entrées dans la base
      assertNotNull(c2);
      assertTrue(c2.getAdresse().equals("adresse de test"));
      assertTrue(c2.getCp().equals("75010"));
      assertTrue(c2.getVille().equals("PARIS"));
      assertTrue(c2.getPays().equals("FRANCE"));
      assertTrue(c2.getTel().equals("0155302658"));
      assertTrue(c2.getFax().equals("0145454545"));
      assertTrue(c2.getMail().equals("mail@mail.fr"));
      c2.setAdresse(updatedAdr);
      coordonneeDao.save(c2);
      assertTrue(c2.getAdresse().equals(updatedAdr));

      // Test de la délétion
      coordonneeDao.deleteById(id);
      assertFalse(coordonneeDao.findById(id).isPresent());
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String adresse = "Adresse1";
      final String adresse2 = "Adresse2";
      final String ville = "PARIS";
      final String ville2 = "LYON";
      final Coordonnee c1 = new Coordonnee();
      c1.setAdresse(adresse);
      c1.setVille(ville);
      final Coordonnee c2 = new Coordonnee();
      c2.setAdresse(adresse);
      c2.setVille(ville);

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));
      // 2 objets sont égaux entre eux
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      // Vérification de la différenciation de 2 objets
      c2.setAdresse(adresse2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));
      c2.setAdresse(adresse);
      c2.setVille(ville2);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c2.setVille(null);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      c2.setAdresse(null);
      c2.setVille(ville);
      assertFalse(c1.equals(c2));
      assertFalse(c2.equals(c1));

      final Categorie c3 = new Categorie();
      assertFalse(c1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String adresse = "Adresse1";
      final String cp = "75000";
      final String ville = "PARIS";
      final String pays = "FRANCE";
      final String tel = "548654656";
      final String fax = "5878636";
      final String mail = "mail@mail.fr";
      final Coordonnee c1 = new Coordonnee();
      c1.setCoordonneeId(1);
      c1.setAdresse(adresse);
      c1.setVille(ville);
      c1.setCp(cp);
      c1.setVille(ville);
      c1.setPays(pays);
      c1.setTel(tel);
      c1.setFax(fax);
      c1.setMail(mail);
      final Coordonnee c2 = new Coordonnee();
      c2.setCoordonneeId(1);
      c2.setAdresse(adresse);
      c2.setVille(ville);
      c2.setCp(cp);
      c2.setVille(ville);
      c2.setPays(pays);
      c2.setTel(tel);
      c2.setFax(fax);
      c2.setMail(mail);
      final Coordonnee c3 = new Coordonnee();
      assertTrue(c3.hashCode() > 0);

      final int hash = c1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(c1.hashCode() == c2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());

   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final Coordonnee c1 = coordonneeDao.findById(1);
      final Coordonnee c2 = c1.clone();
      assertTrue(c1.equals(c2));

      if(c1.getCoordonneeId() != null){
         assertTrue(c1.getCoordonneeId() == c2.getCoordonneeId());
      }else{
         assertNull(c2.getCoordonneeId());
      }

      if(c1.getAdresse() != null){
         assertTrue(c1.getAdresse().equals(c2.getAdresse()));
      }else{
         assertNull(c2.getAdresse());
      }

      if(c1.getCp() != null){
         assertTrue(c1.getCp().equals(c2.getCp()));
      }else{
         assertNull(c2.getCp());
      }

      if(c1.getVille() != null){
         assertTrue(c1.getVille().equals(c2.getVille()));
      }else{
         assertNull(c2.getVille());
      }

      if(c1.getPays() != null){
         assertTrue(c1.getPays().equals(c2.getPays()));
      }else{
         assertNull(c2.getPays());
      }

      if(c1.getTel() != null){
         assertTrue(c1.getTel().equals(c2.getTel()));
      }else{
         assertNull(c2.getTel());
      }

      if(c1.getFax() != null){
         assertTrue(c1.getFax().equals(c2.getFax()));
      }else{
         assertNull(c2.getFax());
      }

      if(c1.getMail() != null){
         assertTrue(c1.getMail().equals(c2.getMail()));
      }else{
         assertNull(c2.getMail());
      }
   }

}
