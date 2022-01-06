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
package fr.aphp.tumorotek.dao.test.interfacage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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



import fr.aphp.tumorotek.dao.interfacage.ConsultationIntfDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.interfacage.ConsultationIntf;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO ConsultationIntfDao et le bean du domaine ConsultationIntf.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0.13.1
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Config.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class ConsultationIntfDaoTest extends AbstractDaoTest
{

   @Autowired
 ConsultationIntfDao consultationIntfDao;
   @Autowired
 UtilisateurDao utilisateurDao;

   public ConsultationIntfDaoTest(){

   }

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-test-mysql.xml"};
   }

   @Test
public void setConsultationIntfDao(final ConsultationIntfDao c){
      this.consultationIntfDao = c;
   }

   @Test
public void setUtilisateurDao(final UtilisateurDao u){
      this.utilisateurDao = u;
   }

   @Test
public void testReadAll(){
      final List<ConsultationIntf> liste = IterableUtils.toList(consultationIntfDao.findAll());
      assertTrue(liste.size() == 4);
   }

   @Test
public void testFindByUtilisateurInDates() throws ParseException{
      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Calendar c1 = Calendar.getInstance();
      c1.setTime(sdf.parse("20/04/2015 12:12:00"));
      final Calendar c2 = Calendar.getInstance();
      c2.setTime(sdf.parse("30/04/2015 16:55:24"));
      List<ConsultationIntf> liste = consultationIntfDao.findByUtilisateurInDates(u1, c1, c2);
      assertTrue(liste.size() == 3);

      liste = consultationIntfDao.findByUtilisateurInDates(utilisateurDao.findById(2), c1, c2);
      assertTrue(liste.size() == 0);

      liste = consultationIntfDao.findByUtilisateurInDates(utilisateurDao.findById(3), c1, c2);
      assertTrue(liste.size() == 1);

      c2.setTime(sdf.parse("30/04/2015 16:50:00"));
      liste = consultationIntfDao.findByUtilisateurInDates(u1, c1, c2);
      assertTrue(liste.size() == 2);

      liste = consultationIntfDao.findByUtilisateurInDates(null, c1, c2);
      assertTrue(liste.size() == 0);
   }

   @Test
public void testFindByEmetteurInDates() throws ParseException{
      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
      final Calendar c1 = Calendar.getInstance();
      c1.setTime(sdf.parse("20/04/2015 12:12:00"));
      final Calendar c2 = Calendar.getInstance();
      c2.setTime(sdf.parse("30/04/2015 16:55:24"));
      List<ConsultationIntf> liste = consultationIntfDao.findByEmetteurInDates("APIX", c1, c2);
      assertTrue(liste.size() == 2);

      liste = consultationIntfDao.findByEmetteurInDates("APIX%", c1, c2);
      assertTrue(liste.size() == 3);

      liste = consultationIntfDao.findByEmetteurInDates("%GLIMS%", c1, c2);
      assertTrue(liste.size() == 0);

      liste = consultationIntfDao.findByEmetteurInDates("DIAMIC", c1, c2);
      assertTrue(liste.size() == 1);

      c2.setTime(sdf.parse("30/04/2015 16:50:00"));
      liste = consultationIntfDao.findByEmetteurInDates("APIX", c1, c2);
      assertTrue(liste.size() == 1);

      liste = consultationIntfDao.findByEmetteurInDates(null, c1, c2);
      assertTrue(liste.size() == 0);
   }

   @Test
public void testFindByUtilisateurEmetteurInDates() throws ParseException{
      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Calendar c1 = Calendar.getInstance();
      c1.setTime(sdf.parse("20/04/2015 12:12:00"));
      final Calendar c2 = Calendar.getInstance();
      c2.setTime(sdf.parse("30/04/2015 16:55:24"));
      List<ConsultationIntf> liste = consultationIntfDao.findByUtilisateurEmetteurInDates(u1, "APIX", c1, c2);
      assertTrue(liste.size() == 2);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(u1, "%", c1, c2);
      assertTrue(liste.size() == 3);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(utilisateurDao.findById(2), "%", c1, c2);
      assertTrue(liste.size() == 0);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(utilisateurDao.findById(3), "APIX v2.3", c1, c2);
      assertTrue(liste.size() == 1);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(utilisateurDao.findById(3), "APIX", c1, c2);
      assertTrue(liste.size() == 0);

      c2.setTime(sdf.parse("30/04/2015 16:50:00"));
      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(u1, "APIX", c1, c2);
      assertTrue(liste.size() == 1);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(null, "%", c1, c2);
      assertTrue(liste.size() == 0);

      liste = consultationIntfDao.findByUtilisateurEmetteurInDates(u1, null, c1, c2);
      assertTrue(liste.size() == 0);
   }

   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

      final ConsultationIntf c1 = new ConsultationIntf();
      final Utilisateur u1 = utilisateurDao.findById(1);
      c1.setIdentification("NEW_DOS_ID");
      c1.setEmetteurIdent("GLIMS v88");
      final Calendar c = Calendar.getInstance();
      c.setTime(sdf.parse("20/02/2016 12:12:12"));
      c1.setDate(c);
      c1.setUtilisateur(u1);

      final int totSize = IterableUtils.toList(consultationIntfDao.findAll()).size();

      // Test de l'insertion
      consultationIntfDao.save(c1);
      assertTrue(IterableUtils.toList(consultationIntfDao.findAll()).size() == totSize + 1);
      assertTrue(c1.getConsultationIntfId() != null);

      final ConsultationIntf c2 = consultationIntfDao.findById(c1.getConsultationIntfId());
      // Vérification des données entrées dans la base
      assertNotNull(c2);
      assertTrue(c2.getIdentification().equals("NEW_DOS_ID"));
      assertTrue(c2.getDate().equals(c));
      assertTrue(c2.getEmetteurIdent().equals("GLIMS v88"));
      assertTrue(c2.getUtilisateur().equals(u1));

      // Test de la délétion
      consultationIntfDao.deleteById(c2.getConsultationIntfId());
      assertNull(consultationIntfDao.findById(c2.getConsultationIntfId()));

      testReadAll();
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   @Test
public void testEquals() throws ParseException{

      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

      final ConsultationIntf ci1 = new ConsultationIntf();
      final ConsultationIntf ci2 = new ConsultationIntf();

      final String id1 = "id1";
      final String id2 = "id2";
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final String emetId1 = "id1";
      final String emetId2 = "id2";
      final Calendar c1 = Calendar.getInstance();
      c1.setTime(sdf.parse("12/12/2015 12:12:00"));
      final Calendar c2 = Calendar.getInstance();
      c2.setTime(sdf.parse("13/12/2015 12:12:00"));

      assertTrue(ci1.equals(ci1));

      ci1.setIdentification(id1);
      ci1.setUtilisateur(u1);
      ci1.setDate(c1);
      ci1.setEmetteurIdent(emetId1);
      ci2.setIdentification(id1);
      ci2.setUtilisateur(u1);
      ci2.setDate(c1);
      ci2.setEmetteurIdent(emetId1);

      // L'objet 1 n'est pas égal à null
      assertFalse(ci1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ci1.equals(ci1));
      // 2 objets sont égaux entre eux
      assertTrue(ci1.equals(ci2));
      assertTrue(ci2.equals(ci1));

      ci2.setIdentification(id2);
      assertFalse(ci1.equals(ci2));
      ci1.setIdentification(id2);
      assertTrue(ci1.equals(ci2));
      ci2.setIdentification(null);
      assertFalse(ci1.equals(ci2));
      ci2.setIdentification(id2);
      assertTrue(ci1.equals(ci2));

      ci2.setUtilisateur(u2);
      assertFalse(ci1.equals(ci2));
      ci1.setUtilisateur(u2);
      assertTrue(ci1.equals(ci2));
      ci2.setUtilisateur(null);
      assertFalse(ci1.equals(ci2));
      ci2.setUtilisateur(u2);
      assertTrue(ci1.equals(ci2));

      ci2.setDate(c2);
      assertFalse(ci1.equals(ci2));
      ci1.setDate(c2);
      assertTrue(ci1.equals(ci2));
      ci2.setDate(null);
      assertFalse(ci1.equals(ci2));
      ci2.setDate(c2);
      assertTrue(ci1.equals(ci2));

      ci2.setEmetteurIdent(emetId2);
      assertFalse(ci1.equals(ci2));
      ci1.setEmetteurIdent(emetId2);
      assertTrue(ci1.equals(ci2));
      ci2.setEmetteurIdent(null);
      assertFalse(ci1.equals(ci2));
      ci2.setEmetteurIdent(emetId2);
      assertTrue(ci1.equals(ci2));

      final Categorie c3 = new Categorie();
      assertFalse(ci1.equals(c3));
   }

   @Test
public void testHashCode() throws ParseException{

      final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

      final ConsultationIntf ci1 = new ConsultationIntf();
      final ConsultationIntf ci2 = new ConsultationIntf();

      final String id1 = "id1";
      final String id2 = "id2";
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final String emetId1 = "id1";
      final String emetId2 = "id2";
      final Calendar c1 = Calendar.getInstance();
      c1.setTime(sdf.parse("12/12/2015 12:12:00"));
      final Calendar c2 = Calendar.getInstance();
      c2.setTime(sdf.parse("13/12/2015 12:12:00"));

      //null
      assertTrue(ci1.hashCode() == ci2.hashCode());

      //Identification
      ci2.setIdentification(id1);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setIdentification(id2);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setIdentification(id1);
      assertTrue(ci1.hashCode() == ci2.hashCode());

      //Utilisateur
      ci2.setUtilisateur(u1);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setUtilisateur(u2);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setUtilisateur(u1);
      assertTrue(ci1.hashCode() == ci2.hashCode());

      //EmetId
      ci2.setEmetteurIdent(emetId1);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setEmetteurIdent(emetId2);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setEmetteurIdent(emetId1);
      assertTrue(ci1.hashCode() == ci2.hashCode());

      //date
      ci2.setDate(c1);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setDate(c2);
      assertFalse(ci1.hashCode() == ci2.hashCode());
      ci1.setDate(c1);
      assertTrue(ci1.hashCode() == ci2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = ci1.hashCode();
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());
      assertTrue(hash == ci1.hashCode());

   }

}
