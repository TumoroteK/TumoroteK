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

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.IncaMaladieExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class IncaMaladieExportTest extends AbstractManagerTest
{

   private IncaMaladieExport incaMaladieExport;
   private BanqueDao banqueDao;

   @SuppressWarnings("deprecation")
   public IncaMaladieExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setIncaMaladieExport(final IncaMaladieExport iE){
      this.incaMaladieExport = iE;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testGetDiagnosticPrincipal() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(10219);
      prel.setBanque(banqueDao.findById(1));

      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals("C50.9"));

      boolean catched = false;
      try{
         assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, false).equals(""));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      prel.setPrelevementId(8);
      catched = false;
      try{
         assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals(""));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      prel.setPrelevementId(10330);
      catched = false;
      try{
         incaMaladieExport.getDiagnosticPrincipal(con, prel, true);
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      prel.setPrelevementId(10219);
      final Maladie mal = new Maladie();
      prel.setMaladie(mal);
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals("C50.9"));
      catched = false;
      try{
         assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, false).equals(""));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      mal.setLibelle("INDETERMINEE");
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals("C50.9"));
      catched = false;
      try{
         assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, false).equals(""));
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      mal.setCode("ZZ.2");
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals("ZZ.2"));
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, false).equals("ZZ.2"));

      mal.setCode("ZZ.6");
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, true).equals("ZZ.6"));
      assertTrue(incaMaladieExport.getDiagnosticPrincipal(con, prel, false).equals("ZZ.6"));
      
      con.close();
   }

   public void testGetDateDiag() throws SQLException, ClassNotFoundException, ParseException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(10219);

      final DateFormat dateFormatterINCA = new SimpleDateFormat("dd/MM/yyyy");

      assertTrue(incaMaladieExport.getDateDiag(con, prel, true, dateFormatterINCA).equals("30/04/2010"));

      assertTrue(incaMaladieExport.getDateDiag(con, prel, false, dateFormatterINCA).equals(""));

      prel.setPrelevementId(10330);
      assertTrue(incaMaladieExport.getDateDiag(con, prel, true, dateFormatterINCA).equals(""));

      final Maladie mal = new Maladie();
      prel.setMaladie(mal);
      prel.setPrelevementId(10219);
      assertTrue(incaMaladieExport.getDateDiag(con, prel, true, dateFormatterINCA).equals("30/04/2010"));
      assertTrue(incaMaladieExport.getDateDiag(con, prel, false, dateFormatterINCA).equals(""));

      mal.setDateDiagnostic(dateFormatterINCA.parse("12/12/1212"));
      assertTrue(incaMaladieExport.getDateDiag(con, prel, true, dateFormatterINCA).equals("12/12/1212"));
      assertTrue(incaMaladieExport.getDateDiag(con, prel, false, dateFormatterINCA).equals("12/12/1212"));

      con.close();
   }

   public void testGetVersionCTNM() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(7705);

      assertTrue(incaMaladieExport.getVersionCTNM(con, prel).equals("X"));

      prel.setPrelevementId(8);
      assertTrue(incaMaladieExport.getVersionCTNM(con, prel).equals(""));
      
      con.close();
   }

   public void testGetTailleTumeur() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(4286);

      assertTrue(incaMaladieExport.getTailleTumeur(con, prel).equals("4"));

      prel.setPrelevementId(8);
      assertTrue(incaMaladieExport.getTailleTumeur(con, prel).equals(""));
      
      con.close();
   }

   public void testGetEnvahGangR() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(9856);

      assertTrue(incaMaladieExport.getEnvahGangR(con, prel).equals("0"));

      prel.setPrelevementId(8);
      assertTrue(incaMaladieExport.getEnvahGangR(con, prel).equals(""));
      
      con.close();
   }

   public void testGetExtMetastatique() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(5702);

      assertTrue(incaMaladieExport.getExtMetastatique(con, prel).equals("X"));

      prel.setPrelevementId(8);
      assertTrue(incaMaladieExport.getExtMetastatique(con, prel).equals(""));
      
      con.close();
   }
}
