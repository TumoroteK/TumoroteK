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
import java.util.List;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.IncaComplementExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class IncaComplementExportTest extends AbstractManagerTest
{

   private IncaComplementExport incaComplementExport;

   private BanqueDao banqueDao;

   @SuppressWarnings("deprecation")
   public IncaComplementExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setIncaComplementExport(final IncaComplementExport iExport){
      this.incaComplementExport = iExport;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testGetCRAnapathInterrogeable() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(5321);
      assertTrue(incaComplementExport.getCRAnapathInterro(con, p).equals("O"));
      p.setPrelevementId(3412);
      assertTrue(incaComplementExport.getCRAnapathInterro(con, p).equals("N"));
      p.setPrelevementId(1000);
      assertTrue(incaComplementExport.getCRAnapathInterro(con, p).equals(""));
      
      con.close();
   }

   public void testGetDonneesClinBase() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(7268);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getDonneesClinBase(con, p, b).equals("N"));
      p.setPatientId(33);
      assertTrue(incaComplementExport.getDonneesClinBase(con, p, b).equals("N"));
      p.setPatientId(3412);
      assertTrue(incaComplementExport.getDonneesClinBase(con, p, b).equals("N"));
      
      con.close();
   }

   public void testGetInclusionTherap() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(722);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getInclusionTherap(con, p, b).equals(""));
      p.setPatientId(119);
      assertTrue(incaComplementExport.getInclusionTherap(con, p, b).equals(""));
      p.setPatientId(3412);
      assertTrue(incaComplementExport.getInclusionTherap(con, p, b).equals(""));
      p.setPatientId(6709);
      assertTrue(incaComplementExport.getInclusionTherap(con, p, b).equals(""));
      p.setPatientId(9405);
      assertTrue(incaComplementExport.getInclusionTherap(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetNomProtocoleTherap() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(7242);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getNomProtocoleTherap(con, p, b).equals(""));
      p.setPatientId(11921);
      assertTrue(incaComplementExport.getNomProtocoleTherap(con, p, b).equals(""));
      p.setPatientId(3412);
      assertTrue(incaComplementExport.getNomProtocoleTherap(con, p, b).equals(""));
      p.setPatientId(6709);
      assertTrue(incaComplementExport.getNomProtocoleTherap(con, p, b).equals(""));
      p.setPatientId(9405);
      assertTrue(incaComplementExport.getNomProtocoleTherap(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetCaryotype() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(7242);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaComplementExport.getCaryotype(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetAnomalieCaryo() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(7242);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getAnomalieCaryo(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetAnomalieGenomique() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);

      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getAnomalieGenomique(con, p, b).equals("O"));
      p.setPatientId(3);
      assertTrue(incaComplementExport.getAnomalieGenomique(con, p, b).equals("N"));
      p.setPatientId(4);
      assertTrue(incaComplementExport.getAnomalieGenomique(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetAnomalieGenomiqueDescr() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(58);
      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals("Syndrome XXY"));

      b.setBanqueId(2);
      assertTrue(incaComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals(""));

      p.setPatientId(3);
      assertTrue(incaComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals(""));

      p.setPatientId(4);
      b.setBanqueId(1);
      assertTrue(incaComplementExport.getAnomalieGenomiqueDescr(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetControleQualite(){
      final Echantillon e = new Echantillon();

      assertTrue(incaComplementExport.getControleQualite(e).equals(""));

      final EchanQualite qual = new EchanQualite();
      qual.setEchanQualite("DO");
      e.setEchanQualite(qual);
      assertTrue(incaComplementExport.getControleQualite(e).equals("O"));

      qual.setEchanQualite("Aucun");
      assertTrue(incaComplementExport.getControleQualite(e).equals("N"));
   }

   public void testGetInclusionProtocoleRech() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(7381);
      assertTrue(incaComplementExport.getInclusionProtocoleRech(con, p).equals(""));
      p.setPrelevementId(1698);
      assertTrue(incaComplementExport.getInclusionProtocoleRech(con, p).equals(""));
      p.setPrelevementId(4);
      assertTrue(incaComplementExport.getInclusionProtocoleRech(con, p).equals(""));
      
      con.close();
   }

   public void testGetNomProtocoleRech() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(13842);
      assertTrue(incaComplementExport.getNomProtocoleRech(con, p).equals(""));
      p.setPrelevementId(31456);
      assertTrue(incaComplementExport.getNomProtocoleRech(con, p).equals(""));
      p.setPrelevementId(25);
      assertTrue(incaComplementExport.getNomProtocoleRech(con, p).equals(""));
      
      con.close();
   }

   public void testGetChampSpecCancer() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(2);
      assertTrue(incaComplementExport.getChampSpecCancer(con, p).equals(""));
      
      con.close();
   }

   public void testGetContact() throws SQLException, ClassNotFoundException{
      Banque b = banqueDao.findById(1);
      List<String> contact = incaComplementExport.getContact(b);
      assertTrue(contact.get(0).equals("VIAL"));
      assertTrue(contact.get(1).equals("CHRISTOPHE"));
      assertTrue(contact.get(2).equals(""));
      assertTrue(contact.get(3).equals("0142490000"));

      b = banqueDao.findById(2);
      contact = incaComplementExport.getContact(b);
      assertTrue(contact.get(0).equals("DUFAY"));
      assertTrue(contact.get(1).equals("NATHALIE"));
      assertTrue(contact.get(2).equals(""));
      assertTrue(contact.get(3).equals("0142490000"));

      b = banqueDao.findById(3);
      boolean catched = false;
      try{
         contact = incaComplementExport.getContact(b);
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);

   }
}
