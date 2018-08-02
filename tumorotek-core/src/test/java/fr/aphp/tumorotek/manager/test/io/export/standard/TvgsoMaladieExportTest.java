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

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.manager.impl.io.export.standard.ItemException;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoMaladieExport;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

/**
 * TODO LES BDD sont en durs --> utiliser les propriétés du POM Parent
 * TODO Qu'est-ce la DB 'toul' ?
 * @author 7007168
 *
 */
public class TvgsoMaladieExportTest extends AbstractManagerTest
{

   private TvgsoMaladieExport tvgsoMaladieExport;
   private BanqueDao banqueDao;

   @SuppressWarnings("deprecation")
   public TvgsoMaladieExportTest(){
      setAutowireMode(AUTOWIRE_BY_NAME);
   }

   public void setTvgsoMaladieExport(final TvgsoMaladieExport iE){
      this.tvgsoMaladieExport = iE;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testGetDiagnosticPrincipal() throws SQLException, ClassNotFoundException{

      Class.forName("com.mysql.cj.jdbc.Driver");
      final Connection con =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "toul?characterEncoding=UTF-8", "root", "root");
      final Prelevement prel = new Prelevement();
      prel.setPrelevementId(10219);
      prel.setBanque(banqueDao.findById(1));

      assertTrue(tvgsoMaladieExport.getDiagnosticPrincipal(con, prel).equals("C50.9"));
      prel.setPrelevementId(8);
      assertTrue(tvgsoMaladieExport.getDiagnosticPrincipal(con, prel).equals(""));

      prel.setPrelevementId(10330);
      boolean catched = false;
      try{
         tvgsoMaladieExport.getDiagnosticPrincipal(con, prel);
      }catch(final ItemException ie){
         catched = true;
      }
      assertTrue(catched);

      prel.setPrelevementId(10219);
      final Maladie mal = new Maladie();
      prel.setMaladie(mal);
      assertTrue(tvgsoMaladieExport.getDiagnosticPrincipal(con, prel).equals("C50.9"));

      mal.setCode("ZZ.2");
      assertTrue(tvgsoMaladieExport.getDiagnosticPrincipal(con, prel).equals("ZZ.2"));

      mal.setCode("ZZ.6");
      assertTrue(tvgsoMaladieExport.getDiagnosticPrincipal(con, prel).equals("ZZ.6"));
      
      con.close();
   }

}
