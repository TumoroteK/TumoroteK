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
package fr.aphp.tumorotek.manager.test.io.export.standard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoComplementExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class TvgsoComplementExportTest extends AbstractManagerTest
{

   private TvgsoComplementExport tvgsoComplementExport;
   private EchantillonDao echantillonDao;
   private BanqueDao banqueDao;

   @SuppressWarnings("deprecation")
   public TvgsoComplementExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setTvgsoComplementExport(final TvgsoComplementExport iExport){
      this.tvgsoComplementExport = iExport;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testGetDonneesClinBase() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(7268);
      assertTrue(tvgsoComplementExport.getDonneesClinBase(con, p).equals("N"));
      p.setPrelevementId(3348);
      assertTrue(tvgsoComplementExport.getDonneesClinBase(con, p).equals("O"));
      p.setPrelevementId(3412);
      assertTrue(tvgsoComplementExport.getDonneesClinBase(con, p).equals(""));
      
      con.close();
   }

   public void testGetInclusionTherap() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(7242);
      assertTrue(tvgsoComplementExport.getInclusionTherap(con, p).equals("O"));
      p.setPrelevementId(11921);
      assertTrue(tvgsoComplementExport.getInclusionTherap(con, p).equals("N"));
      p.setPrelevementId(3412);
      assertTrue(tvgsoComplementExport.getInclusionTherap(con, p).equals(""));
      p.setPrelevementId(6709);
      assertTrue(tvgsoComplementExport.getInclusionTherap(con, p).equals("O"));
      p.setPrelevementId(9405);
      assertTrue(tvgsoComplementExport.getInclusionTherap(con, p).equals("O"));
      
      con.close();
   }

   public void testGetNomProtocoleTherap() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(7242);
      assertTrue(tvgsoComplementExport.getNomProtocoleTherap(con, p).equals(""));
      p.setPrelevementId(11921);
      assertTrue(tvgsoComplementExport.getNomProtocoleTherap(con, p).equals(""));
      p.setPrelevementId(3412);
      assertTrue(tvgsoComplementExport.getNomProtocoleTherap(con, p).equals(""));
      p.setPrelevementId(6709);
      assertTrue(tvgsoComplementExport.getNomProtocoleTherap(con, p).equals("MERCK SERONO"));
      p.setPrelevementId(9405);
      assertTrue(tvgsoComplementExport.getNomProtocoleTherap(con, p).equals("Tarceva"));
      
      con.close();
   }

   public void testGetAnomalieGenomiqueDescr() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(58);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(tvgsoComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals("Syndrome XXY"));

      p.setPatientId(2);
      assertTrue(tvgsoComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals("Trisomie 21"));

      p.setPatientId(3);
      assertTrue(tvgsoComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals(""));
      b.setBanqueId(2);
      assertTrue(tvgsoComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals("Klinefelter"));

      p.setPatientId(4);
      b.setBanqueId(1);
      assertTrue(tvgsoComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetControleQualite(){
      Echantillon e = echantillonDao.findById(1);
      assertTrue(tvgsoComplementExport.getControleQualite(e).equals("O"));

      e.setEchanQualite(null);
      assertTrue(tvgsoComplementExport.getControleQualite(e).equals("O"));

      e = echantillonDao.findById(2);
      assertTrue(tvgsoComplementExport.getControleQualite(e).equals("O"));

      e.setEchanQualite(null);
      assertTrue(tvgsoComplementExport.getControleQualite(e).equals(""));
   }

   public void testGetInclusionProtocoleRech() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(7381);
      assertTrue(tvgsoComplementExport.getInclusionProtocoleRech(con, e).equals("O"));
      e.setEchantillonId(16698);
      assertTrue(tvgsoComplementExport.getInclusionProtocoleRech(con, e).equals("N"));
      e.setEchantillonId(4);
      assertTrue(tvgsoComplementExport.getInclusionProtocoleRech(con, e).equals(""));
      
      con.close();
   }

   public void testGetNomProtocoleRech() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(13842);
      assertTrue(tvgsoComplementExport.getNomProtocoleRech(con, e).equals("Marges ORL"));
      e.setEchantillonId(31456);
      assertTrue(tvgsoComplementExport.getNomProtocoleRech(con, e).equals("Oncodesign prostate"));
      e.setEchantillonId(25);
      assertTrue(tvgsoComplementExport.getNomProtocoleRech(con, e).equals(""));
      
      con.close();
   }

   public void testGetChampSpecCancer() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(tvgsoComplementExport.getChampSpecCancer(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetConsentement(){
      final Prelevement p = new Prelevement();
      final ConsentType cType = new ConsentType();
      cType.setType("Autorisation avant 2004");
      p.setConsentType(cType);
      assertTrue("Consentement",
         tvgsoComplementExport.getConsentement(p, ".*AUTORISATION.*", "(.*ATTENTE.*)|(.*REFUS.*)").equals("O"));
      cType.setType("En attente d'autorisation");
      assertTrue("Consentement",
         tvgsoComplementExport.getConsentement(p, ".*AUTORISATION.*", "(.*ATTENTE.*)|(.*REFUS.*)").equals("N"));
      cType.setType("REFUS");
      assertTrue("Consentement",
         tvgsoComplementExport.getConsentement(p, ".*AUTORISATION.*", "(.*ATTENTE.*)|(.*REFUS.*)").equals("N"));
      cType.setType("EPIC");
      assertTrue("Consentement",
         tvgsoComplementExport.getConsentement(p, ".*AUTORISATION.*", "(.*ATTENTE.*)|(.*REFUS.*)").equals(""));
   }

   public void testGetContactShort() throws SQLException, ClassNotFoundException{
      Banque b = banqueDao.findById(1);
      String contact = tvgsoComplementExport.getContactShort(b);
      assertTrue(contact.equals("VIAL CHRISTOPHE"));

      b = banqueDao.findById(2);
      contact = tvgsoComplementExport.getContactShort(b);
      assertTrue(contact.equals("DUFAY NATHALIE"));

      b = banqueDao.findById(3);
      boolean catched = false;
      try{
         contact = tvgsoComplementExport.getContactShort(b);
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);

   }

}
