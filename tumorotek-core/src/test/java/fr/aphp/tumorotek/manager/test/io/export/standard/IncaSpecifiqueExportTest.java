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

import fr.aphp.tumorotek.manager.io.export.standard.IncaSpecifiqueExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class IncaSpecifiqueExportTest extends AbstractManagerTest
{

   private IncaSpecifiqueExport incaSpecifiqueExport;

   public void setIncaSpecifiqueExport(final IncaSpecifiqueExport iE){
      this.incaSpecifiqueExport = iE;
   }

   public void testGetQuestAntTabac() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaSpecifiqueExport.getQuestAntTabac(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetQuestFamilial() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaSpecifiqueExport.getQuestFamilial(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetQuestPro() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaSpecifiqueExport.getQuestPro(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetRadioNaif() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(1);
      assertTrue(incaSpecifiqueExport.getRadioNaif(con, e).equals(""));
      
      con.close();
   }

   public void testGetChimioNaif() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(1);
      assertTrue(incaSpecifiqueExport.getChimioNaif(con, e).equals(""));
      
      con.close();
   }

   public void testGetStatutTabac() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaSpecifiqueExport.getStatutTabac(con, p, b).equals(""));
      
      con.close();
   }

   public void testGetNPA() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(2);
      final Banque b = new Banque();
      b.setBanqueId(1);
      assertTrue(incaSpecifiqueExport.getNPA(con, p, b).equals(""));
      
      con.close();
   }

}
