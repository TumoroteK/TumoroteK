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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.IncaPrelevementExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class IncaPrelevementExportTest extends AbstractManagerTest
{

   private IncaPrelevementExport incaPrelevementExport;
   private BanqueDao banqueDao;

   @SuppressWarnings("deprecation")
   public IncaPrelevementExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setIncaPrelevementExport(final IncaPrelevementExport iP){
      this.incaPrelevementExport = iP;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testGetCentreStockage() throws SQLException, ClassNotFoundException{
      final Echantillon echan = new Echantillon();
      final Banque b = new Banque();
      echan.setBanque(b);
      echan.setEchantillonId(986);
      boolean catched = false;
      try{
         incaPrelevementExport.getCentreStockage(echan);
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      final Service s = new Service();
      b.setProprietaire(s);
      catched = false;
      try{
         incaPrelevementExport.getCentreStockage(echan);
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      final Etablissement e = new Etablissement();
      s.setEtablissement(e);
      catched = false;
      try{
         incaPrelevementExport.getCentreStockage(echan);
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      e.setFiness("1478");
      assertTrue(incaPrelevementExport.getCentreStockage(echan).equals("1478"));

      e.setFiness("336958");
      assertTrue(incaPrelevementExport.getCentreStockage(echan).equals("336958"));
   }

   public void testGetDatePrelevement() throws ParseException{
      final Prelevement p = new Prelevement();
      p.setCode("TVGSO CODE");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd-MM-yyyy").parse("11-09-2011"));
      p.setDatePrelevement(cal);

      final DateFormat dateFormatterINCA = new SimpleDateFormat("dd/MM/yyyy");

      assertTrue(incaPrelevementExport.getDatePrelevement(p, dateFormatterINCA).equals("11/09/2011"));

      p.setDatePrelevement(null);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getDatePrelevement(p, null).equals(""));
      }catch(final ItemException e){
         catched = true;
      }
      assertTrue(catched);
      cal.setTime(new Date(0));
      p.setDatePrelevement(cal);
      catched = false;
      try{
         assertTrue(incaPrelevementExport.getDatePrelevement(p, null).equals(""));
      }catch(final ItemException e){
         catched = true;
      }
   }

   public void testGetModePrelevement(){
      final Prelevement p = new Prelevement();
      boolean catched = false;
      try{
         assertNull(incaPrelevementExport.getModePrelevement(p));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);

      final PrelevementType pType = new PrelevementType();
      pType.setType("B : BIOPSIE");
      p.setPrelevementType(pType);
      assertTrue(incaPrelevementExport.getModePrelevement(p).equals("B"));
      pType.setType("L : LIQUIDE");
      assertTrue(incaPrelevementExport.getModePrelevement(p).equals("L"));
      pType.setType("C : CYTOPONCTION");
      assertTrue(incaPrelevementExport.getModePrelevement(p).equals("C"));
      pType.setType("I : ILL VALUE");
      catched = false;
      try{
         assertTrue(incaPrelevementExport.getModePrelevement(p).equals("c"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 3);
      }
      assertTrue(catched);
   }

   public void testGetClassif(){
      Banque b = banqueDao.findById(1);
      assertTrue(incaPrelevementExport.getClassif(b).equals("AC"));
      b = banqueDao.findById(3);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getClassif(b).equals("D"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
   }

   public void testGetCodeOrgane() throws ClassNotFoundException, SQLException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con = DriverManager
         .getConnection("jdbc:mysql://localhost:3306/" + "test_tumorotek_bto?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(1);
      assertTrue(incaPrelevementExport.getCodeOrgane(con, e, "A").equals("BL"));
      assertTrue(incaPrelevementExport.getCodeOrgane(con, e, "C").equals("C02.0.1234"));
      e.setEchantillonId(3);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getCodeOrgane(con, e, "A").equals("DC : COLON"));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void getTypeLesionnel() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con = DriverManager
         .getConnection("jdbc:mysql://localhost:3306/" + "test_tumorotek_bto?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(1);
      assertTrue(incaPrelevementExport.getTypeLesionnel(con, e, "A").equals("BL0211-2"));
      assertTrue(incaPrelevementExport.getTypeLesionnel(con, e, "C").equals("D5-22050"));

      e.setEchantillonId(3);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getTypeLesionnel(con, e, "A").equals(""));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetTypeEvent() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(321);
      assertTrue(incaPrelevementExport.getTypeEvent(con, p).equals("3 : METASTASE"));
      p.setPrelevementId(1);
      assertTrue(incaPrelevementExport.getTypeEvent(con, p).equals("9 : INCONNU"));
      p.setPrelevementId(323);
      assertTrue(incaPrelevementExport.getTypeEvent(con, p).equals("1 : TUMEUR PRIMITIVE"));

      boolean catched = false;
      try{
         p.setPrelevementId(16151);
         assertTrue(incaPrelevementExport.getTypeEvent(con, p).equals("9 : INCONNU"));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 3);
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetVersionPTNM() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(10753);
      assertTrue(incaPrelevementExport.getVersionPTNM(con, p).equals("6"));
      p.setPrelevementId(532);
      assertTrue(incaPrelevementExport.getVersionPTNM(con, p).equals("X"));
      p.setPrelevementId(531);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getVersionPTNM(con, p).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);

      con.close();
   }

   public void testGetTailleTumeurPT() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(12718);
      assertTrue(incaPrelevementExport.getTailleTumeurPT(con, p).equals("2"));
      p.setPrelevementId(15377);
      assertTrue(incaPrelevementExport.getTailleTumeurPT(con, p).equals("4b"));
      p.setPrelevementId(14866);
      assertTrue(incaPrelevementExport.getTailleTumeurPT(con, p).equals("is"));
      p.setPrelevementId(11416);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getTailleTumeurPT(con, p).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetEnvGangPN() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(13201);
      assertTrue(incaPrelevementExport.getEnvGangPN(con, p).equals("X"));
      p.setPrelevementId(12800);
      assertTrue(incaPrelevementExport.getEnvGangPN(con, p).equals("0"));
      p.setPrelevementId(9525);
      assertTrue(incaPrelevementExport.getEnvGangPN(con, p).equals("1"));
      p.setPrelevementId(9189);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getEnvGangPN(con, p).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetExtMeta() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(8509);
      assertTrue(incaPrelevementExport.getExtMetastaticPM(con, p).equals("0"));
      p.setPrelevementId(5881);
      assertTrue(incaPrelevementExport.getExtMetastaticPM(con, p).equals("X"));
      p.setPrelevementId(9189);
      boolean catched = false;
      try{
         assertTrue(incaPrelevementExport.getExtMetastaticPM(con, p).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }
}
