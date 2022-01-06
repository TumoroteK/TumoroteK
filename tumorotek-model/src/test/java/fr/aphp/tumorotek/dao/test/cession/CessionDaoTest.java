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
package fr.aphp.tumorotek.dao.test.cession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import fr.aphp.tumorotek.dao.test.Config;



import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.cession.CessionExamenDao;
import fr.aphp.tumorotek.dao.cession.CessionStatutDao;
import fr.aphp.tumorotek.dao.cession.CessionTypeDao;
import fr.aphp.tumorotek.dao.cession.ContratDao;
import fr.aphp.tumorotek.dao.cession.DestructionMotifDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.CessionStatut;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Classe de test pour le DAO CessionDao et le bean
 * du domaine Cession.
 *
 * @author Pierre Ventadour.
 * @version 2.1
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class CessionDaoTest extends AbstractDaoTest
{

   @Autowired
 CessionDao cessionDao;
   @Autowired
 BanqueDao banqueDao;
   @Autowired
 CessionTypeDao cessionTypeDao;
   @Autowired
 CessionExamenDao cessionExamenDao;
   @Autowired
 ContratDao contratDao;
   @Autowired
 CollaborateurDao collaborateurDao;
   @Autowired
 ServiceDao serviceDao;
   @Autowired
 CessionStatutDao cessionStatutDao;
   @Autowired
 TransporteurDao transporteurDao;
   @Autowired
 DestructionMotifDao destructionMotifDao;
   @Autowired
 PlateformeDao plateformeDao;

   @Autowired
 final String updatedNumero = "999";
   @Autowired
 EntiteDao entiteDao;

   public CessionDaoTest(){}

   @Test
public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @Test
public void setCessionTypeDao(final CessionTypeDao dao){
      this.cessionTypeDao = dao;
   }

   @Test
public void setCessionExamenDao(final CessionExamenDao dao){
      this.cessionExamenDao = dao;
   }

   @Test
public void setContratDao(final ContratDao dao){
      this.contratDao = dao;
   }

   @Test
public void setCollaborateurDao(final CollaborateurDao dao){
      this.collaborateurDao = dao;
   }

   @Test
public void setServiceDao(final ServiceDao dao){
      this.serviceDao = dao;
   }

   @Test
public void setCessionStatutDao(final CessionStatutDao dao){
      this.cessionStatutDao = dao;
   }

   @Test
public void setTransporteurDao(final TransporteurDao dao){
      this.transporteurDao = dao;
   }

   @Test
public void setDestructionMotifDao(final DestructionMotifDao dao){
      this.destructionMotifDao = dao;
   }

   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Test
public void setPlateformeDao(final PlateformeDao p){
      this.plateformeDao = p;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllCessions(){
      final List<Cession> liste = IterableUtils.toList(cessionDao.findAll());
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByNumero().
    */
   @Test
public void testFindByNumero(){
      List<Cession> liste = cessionDao.findByNumero("55");
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByNumero("5411");
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByNumero(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByNumeroWithBanqueReturnIds().
    */
   @Test
public void testFindByNumeroWithBanqueReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);

      List<Integer> liste = cessionDao.findByNumeroWithBanqueReturnIds("55", banks);
      assertTrue(liste.size() == 1);

      banks.add(b2);
      liste = cessionDao.findByNumeroWithBanqueReturnIds("0", banks);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByNumeroWithBanqueReturnIds("55", banks);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByNumeroWithBanqueReturnIds("118", banks);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByNumeroWithBanqueReturnIds(null, banks);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByNumeroWithBanqueReturnIds("55", null);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByNumeroWithBanqueReturnIds(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * @since 2.1
    */
   @Test
public void testFindByNumeroInPlateforme(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<Cession> cess = cessionDao.findByNumeroInPlateforme("55", p1);
      assertTrue(cess.size() == 1);
      cessionDao.findByNumeroInPlateforme("118", p1);
      assertTrue(cess.size() == 1);
      cess = cessionDao.findByNumeroInPlateforme("55", null);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme("14", p1);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme("14", null);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme("%5%", p1);
      assertTrue(cess.size() == 2);
      cess = cessionDao.findByNumeroInPlateforme("%", p1);
      assertTrue(cess.size() == 4);
      cess = cessionDao.findByNumeroInPlateforme("%", null);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme(null, p1);
      assertTrue(cess.size() == 0);

      final Plateforme p2 = plateformeDao.findById(2);
      cess = cessionDao.findByNumeroInPlateforme("55", p2);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme("12", p2);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme("%", p2);
      assertTrue(cess.size() == 0);
      cess = cessionDao.findByNumeroInPlateforme(null, p2);
      assertTrue(cess.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByBanqueWithOrder().
    */
   @Test
public void testFindByBanqueWithOrder(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);

      List<Cession> liste = cessionDao.findByBanqueWithOrder(b1);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getNumero().equals("2"));

      liste = cessionDao.findByBanqueWithOrder(b2);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByBanqueWithOrder(b3);
      assertTrue(liste.size() == 0);
   }

   @Test
public void testFindByBanques(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Cession> res = cessionDao.findByBanques(banks);
      assertTrue(res.size() == 4);
   }

   @Test
public void testFindByBanquesAllIds(){
      final List<Banque> banks = new java.util.ArrayList<>();
      banks.add(banqueDao.findById(1));
      banks.add(banqueDao.findById(2));
      final List<Integer> res = cessionDao.findByBanquesAllIds(banks);
      assertTrue(res.size() == 4);
   }

   @Test
public void testFindByIdInList(){
      final List<Integer> ids = new ArrayList<>();
      ids.add(1);
      ids.add(2);
      ids.add(3);
      ids.add(10);
      List<Cession> res = cessionDao.findByIdInList(ids);
      assertTrue(res.size() == 3);

      res = cessionDao.findByIdInList(null);
      assertTrue(res.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByContrat().
    */
   @Test
public void testFindByContrat(){
      final Contrat contrat1 = contratDao.findById(1);
      final Contrat contrat2 = contratDao.findById(2);
      final Contrat contrat3 = contratDao.findById(3);

      List<Cession> liste = cessionDao.findByContrat(contrat1);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0).getNumero().equals("2"));

      liste = cessionDao.findByContrat(contrat2);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByContrat(contrat3);
      assertTrue(liste.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByExcludedIdNumeros().
    */
   @Test
public void testFindByExcludedIdNumeros(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);

      List<String> liste = cessionDao.findByExcludedIdNumeros(1, b1);
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).equals("2"));

      liste = cessionDao.findByExcludedIdNumeros(10, b1);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).equals("2"));

      liste = cessionDao.findByExcludedIdNumeros(55, b2);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByExcludedIdNumeros(3, b2);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByExcludedIdNumeros(null, b1);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByExcludedIdNumeros(null, b3);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByExcludedIdNumeros(55, null);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByExcludedIdNumeros(null, null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByBanqueSelectNumero().
    */
   @Test
public void testFindByBanqueSelectNumero(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(3);

      List<String> liste = cessionDao.findByBanqueSelectNumero(b1);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).equals("2"));

      liste = cessionDao.findByBanqueSelectNumero(b2);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByBanqueSelectNumero(b3);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCessionStatutAndBanqueReturnIds().
    */
   @Test
public void testFindByCessionStatutAndBanqueReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);

      List<Integer> liste = cessionDao.findByCessionStatutAndBanqueReturnIds("VALIDEE", banks);
      assertTrue(liste.size() == 2);

      liste = cessionDao.findByCessionStatutAndBanqueReturnIds("EN ATTENTE", banks);
      assertTrue(liste.size() == 0);

      banks.add(b2);
      liste = cessionDao.findByCessionStatutAndBanqueReturnIds("EN ATTENTE", banks);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByCessionStatutAndBanqueReturnIds("OUPS", banks);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByCessionStatutAndBanqueReturnIds(null, banks);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByCessionStatutAndBanqueReturnIds("VALIDEE", null);
      assertTrue(liste.size() == 0);

      liste = cessionDao.findByCessionStatutAndBanqueReturnIds(null, null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByEtatIncompletAndBanquesReturnIds().
    */
   @Test
public void testFindByEtatIncompletAndBanquesReturnIds(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final List<Banque> banks = new ArrayList<>();
      banks.add(b1);
      List<Integer> liste = cessionDao.findByEtatIncompletAndBanquesReturnIds(false, banks);
      assertTrue(liste.size() == 3);

      liste = cessionDao.findByEtatIncompletAndBanquesReturnIds(true, banks);
      assertTrue(liste.size() == 0);

      banks.add(b2);
      liste = cessionDao.findByEtatIncompletAndBanquesReturnIds(true, banks);
      assertTrue(liste.size() == 1);

      liste = cessionDao.findByEtatIncompletAndBanquesReturnIds(false, banks);
      assertTrue(liste.size() == 3);

      liste = cessionDao.findByEtatIncompletAndBanquesReturnIds(true, null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un Cession.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrudCession() throws Exception{

      final Banque b = banqueDao.findById(1);
      final CessionType cessType = cessionTypeDao.findById(1);
      final CessionExamen cessExam = cessionExamenDao.findById(1);
      final Contrat contrat = contratDao.findById(1);
      final Collaborateur collab = collaborateurDao.findById(1);
      final Service serv = serviceDao.findById(1);
      final CessionStatut statut = cessionStatutDao.findById(1);
      final Transporteur transp = transporteurDao.findById(1);
      final DestructionMotif motif = destructionMotifDao.findById(1);
      final Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      final Date upDate = new SimpleDateFormat("dd/MM/yyyy").parse("25/09/2009");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("25/09/2009 12:12:23"));

      final Cession c = new Cession();

      c.setNumero("1");
      c.setBanque(b);
      c.setCessionType(cessType);
      c.setDemandeDate(date);
      c.setCessionExamen(cessExam);
      c.setContrat(contrat);
      c.setEtudeTitre("ETUDE");
      c.setDestinataire(collab);
      c.setServiceDest(serv);
      c.setDescription("TEST");
      c.setDemandeur(collab);
      c.setCessionStatut(statut);
      c.setValidationDate(date);
      c.setExecutant(collab);
      c.setTransporteur(transp);
      c.setDepartDate(cal);
      c.setArriveeDate(cal);
      c.setObservations("TEST");
      c.setTemperature((float) -20);
      c.setDestructionMotif(motif);
      c.setDestructionDate(cal);
      c.setEtatIncomplet(false);
      //c.setArchive(true);

      // Test de l'insertion
      cessionDao.save(c);
      assertEquals(new Integer(5), c.getCessionId());

      // Test de la mise à jour
      final Cession c2 = cessionDao.findById(new Integer(5));
      assertNotNull(c2);
      assertTrue(c2.getNumero().equals("1"));
      assertNotNull(c2.getBanque());
      assertNotNull(c2.getCessionType());
      assertNotNull(c2.getDemandeDate());
      assertTrue(c2.getDemandeDate().equals(date));
      assertNotNull(c2.getCessionExamen());
      assertNotNull(c2.getContrat());
      assertNotNull(c2.getDestinataire());
      assertNotNull(c2.getServiceDest());
      assertTrue(c2.getDescription().equals("TEST"));
      assertNotNull(c2.getDemandeur());
      assertNotNull(c2.getCessionStatut());
      assertNotNull(c2.getValidationDate());
      assertNotNull(c2.getExecutant());
      assertNotNull(c2.getTransporteur());
      assertNotNull(c2.getDepartDate());
      assertNotNull(c2.getArriveeDate());
      assertTrue(c2.getObservations().equals("TEST"));
      assertTrue(c2.getTemperature() == -20);
      assertNotNull(c2.getDestructionMotif());
      assertNotNull(c2.getDestructionDate());
      assertNotNull(c2.getEtatIncomplet());
      assertFalse(c2.getArchive());

      c2.setNumero(updatedNumero);
      c2.setDemandeDate(upDate);
      cessionDao.save(c2);
      assertTrue(cessionDao.findById(new Integer(5)).getNumero() == updatedNumero);
      assertTrue(cessionDao.findById(new Integer(5)).getDemandeDate().equals(upDate));

      // Test de la délétion
      cessionDao.deleteById(new Integer(5));
      assertNull(cessionDao.findById(new Integer(5)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String num = "1";
      final String num2 = "2";
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Cession c1 = new Cession();
      final Cession c2 = new Cession();
      c1.setNumero(num);
      c1.setBanque(b1);
      c2.setNumero(num);
      c2.setBanque(b1);

      // L'objet 1 n'est pas égal à null
      assertFalse(c1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(c1.equals(c1));
      // 2 objets sont égaux entre eux
      assertTrue(c1.equals(c2));
      assertTrue(c2.equals(c1));

      c1.setBanque(null);
      c1.setNumero(null);
      c2.setBanque(null);
      c2.setNumero(null);
      assertTrue(c1.equals(c2));
      c2.setNumero(num);
      assertFalse(c1.equals(c2));
      c1.setNumero(num);
      assertTrue(c1.equals(c2));
      c2.setNumero(num2);
      assertFalse(c1.equals(c2));
      c2.setNumero(null);
      assertFalse(c1.equals(c2));
      c2.setBanque(b1);
      assertFalse(c1.equals(c2));

      c1.setBanque(b1);
      c1.setNumero(null);
      c2.setNumero(null);
      c2.setBanque(b1);
      assertTrue(c1.equals(c2));
      c2.setBanque(b2);
      assertFalse(c1.equals(c2));
      c2.setNumero(num);
      assertFalse(c1.equals(c2));

      // Vérification de la différenciation de 2 objets
      c1.setNumero(num);
      assertFalse(c1.equals(c2));
      c2.setNumero(num2);
      c2.setBanque(b1);
      assertFalse(c1.equals(c2));

      final Categorie c3 = new Categorie();
      assertFalse(c1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String num = "1";
      final String num2 = "2";
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Cession c1 = new Cession();
      final Cession c2 = new Cession();
      //null
      assertTrue(c1.hashCode() == c2.hashCode());

      //Nom
      c2.setNumero(num);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setNumero(num2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setNumero(num);
      assertTrue(c1.hashCode() == c2.hashCode());

      //ProtocoleType
      c2.setBanque(b1);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setBanque(b2);
      assertFalse(c1.hashCode() == c2.hashCode());
      c1.setBanque(b1);
      assertTrue(c1.hashCode() == c2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = c1.hashCode();
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());
      assertTrue(hash == c1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final Cession c1 = cessionDao.findById(1);
      assertTrue(c1.toString().equals("{" + c1.getNumero() + "}"));
      assertTrue(c1.listableObjectId().equals(new Integer(1)));
      assertTrue(c1.entiteNom().equals(entiteDao.findByNom("Cession").get(0).getNom()));

      final Cession c2 = new Cession();
      assertTrue(c2.toString().equals("{Empty Cession}"));
   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final Cession c1 = cessionDao.findById(1);
      Cession c2 = new Cession();
      c2 = c1.clone();

      assertTrue(c1.equals(c2));

      if(c1.getCessionId() != null){
         assertTrue(c1.getCessionId() == c2.getCessionId());
      }else{
         assertNull(c2.getCessionId());
      }

      if(c1.getNumero() != null){
         assertTrue(c1.getNumero().equals(c2.getNumero()));
      }else{
         assertNull(c2.getNumero());
      }

      if(c1.getBanque() != null){
         assertTrue(c1.getBanque().equals(c2.getBanque()));
      }else{
         assertNull(c2.getBanque());
      }

      if(c1.getCessionType() != null){
         assertTrue(c1.getCessionType().equals(c2.getCessionType()));
      }else{
         assertNull(c2.getCessionType());
      }

      if(c1.getDemandeDate() != null){
         assertTrue(c1.getDemandeDate().equals(c2.getDemandeDate()));
      }else{
         assertNull(c2.getDemandeDate());
      }

      if(c1.getContrat() != null){
         assertTrue(c1.getContrat().equals(c2.getContrat()));
      }else{
         assertNull(c2.getContrat());
      }

      if(c1.getDestinataire() != null){
         assertTrue(c1.getDestinataire().equals(c2.getDestinataire()));
      }else{
         assertNull(c2.getDestinataire());
      }

      if(c1.getServiceDest() != null){
         assertTrue(c1.getServiceDest().equals(c2.getServiceDest()));
      }else{
         assertNull(c2.getServiceDest());
      }

      if(c1.getDescription() != null){
         assertTrue(c1.getDescription().equals(c2.getDescription()));
      }else{
         assertNull(c2.getDescription());
      }

      if(c1.getDemandeur() != null){
         assertTrue(c1.getDemandeur().equals(c2.getDemandeur()));
      }else{
         assertNull(c2.getDemandeur());
      }

      if(c1.getCessionStatut() != null){
         assertTrue(c1.getCessionStatut().equals(c2.getCessionStatut()));
      }else{
         assertNull(c2.getCessionStatut());
      }

      if(c1.getValidationDate() != null){
         assertTrue(c1.getValidationDate().equals(c2.getValidationDate()));
      }else{
         assertNull(c2.getValidationDate());
      }

      if(c1.getExecutant() != null){
         assertTrue(c1.getExecutant().equals(c2.getExecutant()));
      }else{
         assertNull(c2.getExecutant());
      }

      if(c1.getTransporteur() != null){
         assertTrue(c1.getTransporteur().equals(c2.getTransporteur()));
      }else{
         assertNull(c2.getTransporteur());
      }

      if(c1.getDepartDate() != null){
         assertTrue(c1.getDepartDate().equals(c2.getDepartDate()));
      }else{
         assertNull(c2.getDepartDate());
      }

      if(c1.getArriveeDate() != null){
         assertTrue(c1.getArriveeDate().equals(c2.getArriveeDate()));
      }else{
         assertNull(c2.getArriveeDate());
      }

      if(c1.getObservations() != null){
         assertTrue(c1.getObservations().equals(c2.getObservations()));
      }else{
         assertNull(c2.getObservations());
      }

      if(c1.getTemperature() != null){
         assertTrue(c1.getTemperature().equals(c2.getTemperature()));
      }else{
         assertNull(c2.getTemperature());
      }

      if(c1.getDestructionMotif() != null){
         assertTrue(c1.getDestructionMotif().equals(c2.getDestructionMotif()));
      }else{
         assertNull(c2.getDestructionMotif());
      }

      if(c1.getDestructionDate() != null){
         assertTrue(c1.getDestructionDate().equals(c2.getDestructionDate()));
      }else{
         assertNull(c2.getDestructionDate());
      }

      if(c1.getEtatIncomplet() != null){
         assertTrue(c1.getEtatIncomplet().equals(c2.getEtatIncomplet()));
      }else{
         assertNull(c2.getEtatIncomplet());
      }

      if(c1.getArchive() != null){
         assertTrue(c1.getArchive().equals(c2.getArchive()));
      }else{
         assertNull(c2.getArchive());
      }

   }

}
