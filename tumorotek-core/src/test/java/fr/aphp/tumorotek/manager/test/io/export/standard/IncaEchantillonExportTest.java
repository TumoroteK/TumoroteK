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
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.IncaEchantillonExport;
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
public class IncaEchantillonExportTest extends AbstractManagerTest
{

   private IncaEchantillonExport incaEchantillonExport;
   private EmplacementDao emplacementDao;
   private BanqueDao banqueDao;
   private EchantillonDao echantillonDao;

   @SuppressWarnings("deprecation")
   public IncaEchantillonExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setIncaEchantillonExport(final IncaEchantillonExport iExport){
      this.incaEchantillonExport = iExport;
   }

   public void setEmplacementDao(final EmplacementDao eDao){
      this.emplacementDao = eDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void testGetIsTumoral(){
      final Echantillon e = new Echantillon();

      e.setTumoral(true);
      assertTrue(incaEchantillonExport.getIsTumoral(e).equals("O"));

      e.setTumoral(false);
      assertTrue(incaEchantillonExport.getIsTumoral(e).equals("N"));

      e.setTumoral(null);
      boolean catched = false;
      try{
         assertTrue(incaEchantillonExport.getIsTumoral(e).equals("N"));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);

   }

   public void testGetModeConservation(){
      // cette methode se teste sur la base toul
      final Echantillon e = new Echantillon();
      e.setEmplacement(emplacementDao.findById(234917));
      //	assertTrue(incaEchantillonExport.getModeConservation(e).equals("4"));
      e.setEmplacement(emplacementDao.findById(142674));
      //	assertTrue(incaEchantillonExport.getModeConservation(e).equals("2"));
      e.setEmplacement(emplacementDao.findById(244401));
      //	assertTrue(incaEchantillonExport.getModeConservation(e).equals("1"));
      e.setEmplacement(null);
      boolean catched = false;
      try{
         assertTrue(incaEchantillonExport.getModeConservation(e).equals("5"));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);

      // sur tumo2test
      // assertTrue(incaEchantillonExport
      //		.getModeConservation(echantillonDao.findById(2)).equals("5"));
   }

   public void testGetEchantillonType(){
      final Echantillon e = new Echantillon();
      final EchantillonType eType = new EchantillonType();
      e.setEchantillonType(eType);
      eType.setType("TT2 : TissuL TUMORAL - TUMEUR 2");
      assertTrue(incaEchantillonExport.getEchantillonType(e).equals("T"));

      eType.setType("TT2 : CELLuLES TUMORAL - TUMEUR 2");
      assertTrue(incaEchantillonExport.getEchantillonType(e).equals("C"));

      eType.setType("FAITH NO MORE");
      assertTrue(incaEchantillonExport.getEchantillonType(e).equals("9"));
   }

   public void testGetModePreparation() throws ClassNotFoundException, SQLException{

      final Echantillon e = new Echantillon();
      final ModePrepa m = new ModePrepa();
      e.setModePrepa(m);
      m.setNom("DMSO like");
      assertTrue(incaEchantillonExport.getModePreparation(e).equals("1"));
      m.setNom("quel CULOTté");
      assertTrue(incaEchantillonExport.getModePreparation(e).equals("2"));
      m.setNom("Tissulaire");
      assertTrue(incaEchantillonExport.getModePreparation(e).equals("3"));
      m.setNom("Pearl JAM");
      assertTrue(incaEchantillonExport.getModePreparation(e).equals("9"));

      e.setModePrepa(null);
      boolean catched = false;
      try{
         assertTrue(incaEchantillonExport.getModePreparation(e).equals("7"));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
   }

   public void testGetDelaiCongelation(){
      final Echantillon e = new Echantillon();
      assertTrue(incaEchantillonExport.getDelaiCongelation(e).equals("9"));
      e.setDelaiCgl(new Float(0));
      assertTrue(incaEchantillonExport.getDelaiCongelation(e).equals("9"));
      e.setDelaiCgl(new Float(19));
      assertTrue(incaEchantillonExport.getDelaiCongelation(e).equals("1"));
      e.setDelaiCgl(new Float(30));
      assertTrue(incaEchantillonExport.getDelaiCongelation(e).equals("2"));
   }

   public void testGetControles() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(15900);
      assertTrue(incaEchantillonExport.getControles(con, e).equals("1"));
      e.setEchantillonId(17833);
      assertTrue(incaEchantillonExport.getControles(con, e).equals("5"));

      e.setEchanQualite(null);
      boolean catched = false;
      try{
         e.setEchantillonId(31454);
         assertTrue(incaEchantillonExport.getControles(con, e).equals(""));
      }catch(final ItemException ie){
         catched = true;
         assertTrue(ie.getSeverity() == 2);
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetPourcentage() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setBanque(banqueDao.findById(1));
      e.setEchantillonId(351);
      assertTrue(incaEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("100"));
      e.setEchantillonId(19278);
      assertTrue(incaEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("60"));
      e.setEchantillonId(17709);
      e.setBanque(banqueDao.findById(3));
      assertTrue(incaEchantillonExport.getPourcentageCellulesTumorales(con, e).equals("0"));

      boolean catched = false;
      try{
         e.setEchantillonId(14995);
         assertTrue(incaEchantillonExport.getPourcentageCellulesTumorales(con, e).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);
      catched = false;
      try{
         e.setEchantillonId(352);
         assertTrue(incaEchantillonExport.getPourcentageCellulesTumorales(con, e).equals(""));
      }catch(final ItemException ie){
         assertTrue(ie.getSeverity() == 2);
         catched = true;
      }
      assertTrue(catched);
      
      con.close();
   }

   public void testGetProdTypeAssocie() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(7720);
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Dd][nN])|([Dd][nN][Aa]).*").equals("1"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Rr][nN])|([Rr][nN][Aa]).*").equals("1"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*").equals("N"));

      e.setEchantillonId(423);
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Dd][nN])|([Dd][nN][Aa]).*(ADN)|(DNA)").equals("N"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Rr][nN])|([Rr][nN][Aa]).*").equals("1"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*").equals("N"));

      e.setEchantillonId(10);
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Dd][nN])|([Dd][nN][Aa]).*").equals("N"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*([Aa][Rr][nN])|([Rr][nN][Aa]).*").equals("N"));
      assertTrue(incaEchantillonExport.getProdTypeAssocie(con, e, ".*[Pp][Rr][Oo][Tt][Ee][Ii][Nn].*").equals("N"));

      con.close();
   }

   public void testgetRessourceBiolAssociee(){
      final Echantillon e = echantillonDao.findById(1);
      assertTrue(incaEchantillonExport.getRessourceBiolAssociee(e, "cellules").equals("O"));
      assertTrue(incaEchantillonExport.getRessourceBiolAssociee(e, "%URINE%").equals("N"));
   }

   public void testGetADNconstitutionnel() throws ClassNotFoundException, SQLException{
      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Echantillon e = new Echantillon();
      e.setEchantillonId(814);
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "%Attente%").equals("O"));
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "%CONSENTEMENT%").equals(""));
      e.setEchantillonId(12911);
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "%OPPOSITION%").equals("N"));
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "%CONSENTEMENT%").equals(""));
      e.setEchantillonId(1000);
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "ATTENTE").equals(""));
      assertTrue(incaEchantillonExport.getADNconstitutionnel(con, e, "%CONSENTEMENT%").equals(""));
      
      con.close();
   }
}
