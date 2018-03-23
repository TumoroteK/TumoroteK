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
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoEchantillonExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class TvgsoEchantillonExportTest extends AbstractManagerTest
{

   private TvgsoEchantillonExport tvgsoEchantillonExport;
   private BanqueDao banqueDao;
   private EchantillonDao echantillonDao;

   @SuppressWarnings("deprecation")
   public TvgsoEchantillonExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setTvgsoEchantillonExport(final TvgsoEchantillonExport iExport){
      this.tvgsoEchantillonExport = iExport;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void testGetIsTumoral(){
      final Echantillon e = new Echantillon();
      final EchantillonType eType = new EchantillonType();
      e.setEchantillonType(eType);
      eType.setType("TT2 : TISSU TUMORAL - TUMEUR 2");
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("O"));
      eType.setType("TZ : TISSU TUMORAL - TUMEUR 2");
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("N"));
      eType.setType("SN : SERUM");
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("N"));

      eType.setType("SN : SERUM");
      e.setTumoral(true);
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("O"));

      eType.setType("TT2 : TISSU TUMORAL - TUMEUR 2");
      e.setTumoral(false);
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("O"));

      e.setTumoral(null);
      assertTrue(tvgsoEchantillonExport.getIsTumoral(e).equals("O"));

   }

   public void testGetModePreparation() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(2641);
      assertTrue(tvgsoEchantillonExport.getModePreparation(con, e).equals(""));
      e.setEchantillonId(30346);
      assertTrue(tvgsoEchantillonExport.getModePreparation(con, e).equals("1"));
      e.setEchantillonId(17870);
      assertTrue(tvgsoEchantillonExport.getModePreparation(con, e).equals("3"));

      final ModePrepa mode = new ModePrepa();
      mode.setNom("7 : new");
      e.setModePrepa(mode);
      assertTrue(tvgsoEchantillonExport.getModePreparation(con, e).equals("9"));
      
      con.close();
   }

   public void testGetControles() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(31454);
      assertTrue(tvgsoEchantillonExport.getControles(con, e).equals(""));
      e.setEchantillonId(15900);
      assertTrue(tvgsoEchantillonExport.getControles(con, e).equals("1"));
      e.setEchantillonId(17833);
      assertTrue(tvgsoEchantillonExport.getControles(con, e).equals("5"));
      
      con.close();
   }

   public void testGetPourcentage() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setBanque(banqueDao.findById(1));
      e.setEchantillonId(351);
      assertTrue(tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("100"));
      e.setEchantillonId(19278);
      assertTrue(tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("60"));
      e.setEchantillonId(17709);
      e.setBanque(banqueDao.findById(3));
      assertTrue(tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("0"));

      e.setEchantillonId(14995);
      assertTrue(tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, e).equals(""));
      e.setEchantillonId(352);
      assertTrue(tvgsoEchantillonExport.getPourcentageCellulesTumorales(con, e).equals(""));
      
      con.close();
   }

   public void testgetRessourceBiolAssociee(){
      final Echantillon e = echantillonDao.findById(1);
      assertTrue(tvgsoEchantillonExport.getRessourceBiolAssociee(e, "CELLULES").equals("1"));
      assertTrue(tvgsoEchantillonExport.getRessourceBiolAssociee(e, "%URINE%").equals("N"));
   }

   public void testGetADNconstitutionnel() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(814);
      assertTrue(tvgsoEchantillonExport.getADNconstitutionnel(con, e).equals("1"));
      e.setEchantillonId(12911);
      assertTrue(tvgsoEchantillonExport.getADNconstitutionnel(con, e).equals("N"));
      e.setEchantillonId(1000);
      assertTrue(tvgsoEchantillonExport.getADNconstitutionnel(con, e).equals(""));
      
      con.close();
   }
}
