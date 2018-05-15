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
import java.util.Date;

import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.IncaPatientExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class IncaPatientExportTest extends AbstractManagerTest
{

   /** Bean Manager. */
   private IncaPatientExport incaPatientExport;

   public void setIncaPatientExport(final IncaPatientExport iExport){
      this.incaPatientExport = iExport;
   }

   @SuppressWarnings("deprecation")
   public IncaPatientExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void testGetFiness() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setNip("TGVSO NIP");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(2);

      assertTrue(incaPatientExport.getFiness(con, prel, p).equals("310783063 : HOPITAL LA GRAVE"));

      prel.setPrelevementId(8);
      assertTrue(incaPatientExport.getFiness(con, prel, p).equals("310783048 : CHU PURPAN"));
      boolean catched = false;
      try{
         prel.setPrelevementId(4);
         assertTrue(incaPatientExport.getFiness(con, prel, p).equals("D310783048 : CHU PURPAN"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("FINESS manquant pour le patient TGVSO NIP"));
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetPatientId() throws SQLException, ClassNotFoundException{
      final Patient p = new Patient();
      p.setPatientId(125);

      assertTrue(incaPatientExport.getPatientId(p).equals("125"));

      p.setPatientId(null);
      boolean catched = false;
      try{
         assertTrue(incaPatientExport.getPatientId(p).equals("TVGSO NIP"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("Identifiant patient obligatoire manquant"));
      }
      assertTrue(catched);
   }

   public void testGetDateNaissance() throws ParseException{
      final Patient p = new Patient();
      p.setNip("TVGSO NIP");
      p.setDateNaissance(new SimpleDateFormat("dd-MM-yyyy").parse("24-03-1978"));

      final DateFormat dateFormatterINCA = new SimpleDateFormat("dd/MM/yyyy");

      assertTrue(incaPatientExport.getDateNaissance(p, dateFormatterINCA).equals("24/03/1978"));

      boolean catched = false;
      try{
         p.setDateNaissance(null);
         assertTrue(incaPatientExport.getDateNaissance(p, null).equals("TVGSO NIP"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("Date de naissance manquante"));
      }
      assertTrue(catched);

      catched = false;
      try{
         p.setDateNaissance(new Date(0));
         assertTrue(incaPatientExport.getDateNaissance(p, dateFormatterINCA).equals("TVGSO NIP"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("Date de naissance manquante"));
      }
      assertTrue(catched);
   }

   public void testGetSexe(){
      final Patient p = new Patient();
      p.setSexe("M");
      assertTrue(incaPatientExport.getSexe(p).equals("M"));

      p.setSexe("F");
      assertTrue(incaPatientExport.getSexe(p).equals("F"));

      p.setSexe("I");
      assertTrue(incaPatientExport.getSexe(p).equals("I"));

      p.setSexe(null);
      assertTrue(incaPatientExport.getSexe(p).equals("I"));

      p.setSexe("Z");
      assertTrue(incaPatientExport.getSexe(p).equals("I"));
   }

   public void testGetPatientEtat() throws SQLException, ClassNotFoundException{

      final Patient p = new Patient();

      p.setPatientEtat("V");
      assertTrue(incaPatientExport.getPatientEtat(p).equals("V"));

      p.setPatientEtat("D");
      assertTrue(incaPatientExport.getPatientEtat(p).equals("D"));

      p.setPatientEtat("Inconnu");
      assertTrue(incaPatientExport.getPatientEtat(p).equals("I"));

      p.setPatientEtat(null);
      assertTrue(incaPatientExport.getPatientEtat(p).equals("I"));

   }

   public void testGetDateEtat() throws SQLException, ClassNotFoundException, ParseException{
      final DateFormat dateFormatterINCA = new SimpleDateFormat("yyyyMMdd");

      final Patient p = new Patient();

      boolean catched = false;
      try{
         assertTrue(incaPatientExport.getDateEtat(p, dateFormatterINCA).equals("00000000"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("Date etat patient manquante"));
      }
      assertTrue(catched);

      p.setDateEtat(dateFormatterINCA.parse("18760222"));
      assertTrue(incaPatientExport.getDateEtat(p, dateFormatterINCA).equals("18760222"));

      p.setDateDeces(dateFormatterINCA.parse("19990909"));
      assertTrue(incaPatientExport.getDateEtat(p, dateFormatterINCA).equals("19990909"));
   }

   public void testGetCauseDeces() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      p.setPatientId(1939);

      final Banque b = new Banque();
      b.setBanqueId(1);

      assertTrue(incaPatientExport.getCauseDeces(con, p, b).equals("1"));

      p.setPatientId(59);
      assertTrue(incaPatientExport.getCauseDeces(con, p, b).equals(""));
      
      con.close();
   }

}
