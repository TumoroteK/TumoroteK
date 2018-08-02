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
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPrelevementExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class TvgsoPrelevementExportTest extends AbstractManagerTest
{

   private TvgsoPrelevementExport tvgsoPrelevementExport;
   private BanqueDao banqueDao;

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   @SuppressWarnings("deprecation")
   public TvgsoPrelevementExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setTvgsoPrelevementExport(final TvgsoPrelevementExport iP){
      this.tvgsoPrelevementExport = iP;
   }

   public void testGetCentreStockage() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon echan = new Echantillon();
      echan.setEchantillonId(449);
      assertTrue(tvgsoPrelevementExport.getCentreStockage(con, echan).equals("7TGSOCHUTLS"));

      echan.setEchantillonId(1040);
      assertTrue(tvgsoPrelevementExport.getCentreStockage(con, echan).equals("8TGSOCLCTLS"));

      echan.setEchantillonId(986);
      boolean catched = false;
      try{
         tvgsoPrelevementExport.getCentreStockage(con, echan);
      }catch(final ItemException ie){
         catched = true;
      }

      echan.setEchantillonId(20243);
      catched = true;
      try{
         tvgsoPrelevementExport.getCentreStockage(con, echan);
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetDatePrelevement() throws ParseException{
      final Prelevement p = new Prelevement();
      p.setCode("TVGSO CODE");
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd-MM-yyyy").parse("11-09-2011"));
      p.setDatePrelevement(cal);

      final DateFormat dateFormatterINCA = new SimpleDateFormat("dd/MM/yyyy");

      assertTrue(tvgsoPrelevementExport.getDatePrelevement(p, dateFormatterINCA).equals("11/09/2011"));

      p.setDatePrelevement(null);
      assertTrue(tvgsoPrelevementExport.getDatePrelevement(p, null).equals(""));

      cal.setTime(new Date(0));
      p.setDatePrelevement(cal);
      assertTrue(tvgsoPrelevementExport.getDatePrelevement(p, null).equals(""));
   }

   public void testGetModePrelevement(){
      final Prelevement p = new Prelevement();
      boolean catched = false;
      try{
         assertTrue(tvgsoPrelevementExport.getModePrelevement(p).equals("c"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
      final PrelevementType pType = new PrelevementType();
      pType.setType("B : BIOPSIE");
      p.setPrelevementType(pType);
      assertTrue(tvgsoPrelevementExport.getModePrelevement(p).equals("B"));
      pType.setType("L : LIQUIDE");
      assertTrue(tvgsoPrelevementExport.getModePrelevement(p).equals("C"));
      pType.setType("C : CYTOPONCTION");
      assertTrue(tvgsoPrelevementExport.getModePrelevement(p).equals("C"));
      pType.setType("I : ILL VALUE");
      catched = false;
      try{
         assertTrue(tvgsoPrelevementExport.getModePrelevement(p).equals("c"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 3);
      }
      assertTrue(catched);
   }

   public void testGetClassif(){
      final Banque b = banqueDao.findById(1);
      b.setNom("banque-C2.0");
      assertTrue(tvgsoPrelevementExport.getClassif(b).equals("C"));
      b.setNom("banque-A");
      assertTrue(tvgsoPrelevementExport.getClassif(b).equals("A"));
      b.setNom("banque-CA");
      assertTrue(tvgsoPrelevementExport.getClassif(b).equals("C"));
      b.setNom("banque-D");
      boolean catched = false;
      try{
         assertTrue(tvgsoPrelevementExport.getClassif(b).equals("D"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
   }

   public void testGetCodeOrgane() throws ClassNotFoundException, SQLException{

      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(7);
      assertTrue(tvgsoPrelevementExport.getCodeOrgane(con, e).equals("LM"));
      e.setEchantillonId(113);
      assertTrue(tvgsoPrelevementExport.getCodeOrgane(con, e).equals("FF"));
      e.setEchantillonId(3658);
      assertTrue(tvgsoPrelevementExport.getCodeOrgane(con, e).equals(""));
      
      con.close();
   }

   public void getTypeLesionnel() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(5991);
      assertTrue(tvgsoPrelevementExport.getTypeLesionnel(con, e).equals("OKDCA7A0"));
      e.setEchantillonId(714);
      assertTrue(tvgsoPrelevementExport.getTypeLesionnel(con, e).equals("NKPF2111  RP"));

      e.setEchantillonId(368);
      assertTrue(tvgsoPrelevementExport.getTypeLesionnel(con, e).equals(""));
      
      con.close();
   }

   public void testGetVersionPTNM() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(10753);
      assertTrue(tvgsoPrelevementExport.getVersionPTNM(con, p).equals("6"));
      p.setPrelevementId(532);
      assertTrue(tvgsoPrelevementExport.getVersionPTNM(con, p).equals("X"));
      p.setPrelevementId(531);
      assertTrue(tvgsoPrelevementExport.getVersionPTNM(con, p).equals(""));
      
      con.close();
   }

   public void testGetTailleTumeurPT() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(12718);
      assertTrue(tvgsoPrelevementExport.getTailleTumeurPT(con, p).equals("2"));
      p.setPrelevementId(15377);
      assertTrue(tvgsoPrelevementExport.getTailleTumeurPT(con, p).equals("4b"));
      p.setPrelevementId(14866);
      assertTrue(tvgsoPrelevementExport.getTailleTumeurPT(con, p).equals("is"));
      p.setPrelevementId(11416);
      assertTrue(tvgsoPrelevementExport.getTailleTumeurPT(con, p).equals(""));
      
      con.close();
   }

   public void testGetEnvGangPN() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(13201);
      assertTrue(tvgsoPrelevementExport.getEnvGangPN(con, p).equals("X"));
      p.setPrelevementId(12800);
      assertTrue(tvgsoPrelevementExport.getEnvGangPN(con, p).equals("0"));
      p.setPrelevementId(9525);
      assertTrue(tvgsoPrelevementExport.getEnvGangPN(con, p).equals("1"));
      p.setPrelevementId(9189);
      assertTrue(tvgsoPrelevementExport.getEnvGangPN(con, p).equals(""));
      
      con.close();
   }

   public void testGetExtMeta() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement p = new Prelevement();
      p.setPrelevementId(8509);
      assertTrue(tvgsoPrelevementExport.getExtMetastaticPM(con, p).equals("0"));
      p.setPrelevementId(5881);
      assertTrue(tvgsoPrelevementExport.getExtMetastaticPM(con, p).equals("X"));
      p.setPrelevementId(9189);
      assertTrue(tvgsoPrelevementExport.getExtMetastaticPM(con, p).equals(""));
      
      con.close();
   }

}
