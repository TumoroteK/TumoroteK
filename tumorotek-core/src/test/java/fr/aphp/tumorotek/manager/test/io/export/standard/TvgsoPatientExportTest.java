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

import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPatientExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class TvgsoPatientExportTest extends AbstractManagerTest
{

   /** Bean Manager. */
   private TvgsoPatientExport tvgsoPatientExport;

   public void setTvgsoPatientExport(final TvgsoPatientExport tE){
      this.tvgsoPatientExport = tE;
   }

   @SuppressWarnings("deprecation")
   public TvgsoPatientExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void testGetPatientCode() throws SQLException, ClassNotFoundException{
      final Patient p = new Patient();
      p.setNip("TVGSO NIP");

      assertTrue(tvgsoPatientExport.getPatientCode(p).equals("TVGSO NIP"));
      boolean catched = false;
      try{
         p.setNip(null);
         assertTrue(tvgsoPatientExport.getPatientCode(p).equals("TVGSO NIP"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
         assertTrue(ie.getMessage().equals("Identifiant patient obligatoire manquant"));
      }
      assertTrue(catched);
   }

   public void testGetPatientEtat() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Patient p = new Patient();
      final Banque b = new Banque();
      b.setBanqueId(1);

      p.setPatientEtat("V");
      assertTrue(tvgsoPatientExport.getPatientEtat(con, p, b).equals("V"));

      p.setPatientEtat("D");
      p.setPatientId(58);
      assertTrue(tvgsoPatientExport.getPatientEtat(con, p, b).equals("D"));

      p.setPatientEtat("Inconnu");
      assertTrue(tvgsoPatientExport.getPatientEtat(con, p, b).equals("V"));

      p.setPatientEtat(null);
      p.setPatientId(1939);
      assertTrue(tvgsoPatientExport.getPatientEtat(con, p, b).equals("D"));

      p.setPatientId(59);
      assertTrue(tvgsoPatientExport.getPatientEtat(con, p, b).equals("I"));
      
      con.close();
   }

   public void testGetDateEtat() throws SQLException, ClassNotFoundException, ParseException{
      final DateFormat dateFormatterINCA = new SimpleDateFormat("yyyyMMdd");
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");

      final Patient p = new Patient();
      final Banque b = new Banque();
      b.setBanqueId(1);

      p.setPatientId(58);

      p.setDateEtat(dateFormatterINCA.parse("18760222"));
      assertTrue(tvgsoPatientExport.getDateEtat(con, p, dateFormatterINCA, b).equals("18760222"));

      p.setDateDeces(dateFormatterINCA.parse("19990909"));
      assertTrue(tvgsoPatientExport.getDateEtat(con, p, dateFormatterINCA, b).equals("19990909"));

      p.setDateEtat(null);
      p.setDateDeces(null);

      assertTrue(tvgsoPatientExport.getDateEtat(con, p, dateFormatterINCA, b).equals("20080102"));

      p.setPatientId(12969);
      assertTrue(tvgsoPatientExport.getDateEtat(con, p, dateFormatterINCA, b).equals("20090417"));

      p.setPatientId(59);
      assertTrue(tvgsoPatientExport.getDateEtat(con, p, dateFormatterINCA, b).equals("00000000"));
      
      con.close();
   }

}
