/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.manager.test.stockage;

import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.impl.io.production.DocumentWithDataAsTableExcelProducer;
import fr.aphp.tumorotek.manager.impl.stockage.planconteneur.PlanCongelateurAvecBoiteExcelGenerator;
import fr.aphp.tumorotek.manager.impl.stockage.planconteneur.PlanCongelateurSansBoiteExcelGenerator;
import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.DocumentData;
import fr.aphp.tumorotek.manager.io.document.detail.table.CellRow;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EnceinteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlanCongelateurSansBoiteGeneratorTest extends AbstractManagerTest4
{
   private PlanCongelateurSansBoiteExcelGenerator sansBoiteGenerator;

   private PlanCongelateurAvecBoiteExcelGenerator avecBoiteGenerator;

   @Autowired
   private DocumentWithDataAsTableExcelProducer documentWithDataAsTableExcelProducer;

   @Autowired
   private ConteneurManager conteneurManager;

   @Autowired
   private EnceinteManager enceinteManager;

   private List<Conteneur> containers;

   @Before
   public void setUp(){
      System.out.println(enceinteManager);
      System.out.println(documentWithDataAsTableExcelProducer);
      sansBoiteGenerator = new PlanCongelateurSansBoiteExcelGenerator(enceinteManager, documentWithDataAsTableExcelProducer);
      avecBoiteGenerator = new PlanCongelateurAvecBoiteExcelGenerator(enceinteManager, documentWithDataAsTableExcelProducer);
      Plateforme plateforme = new Plateforme();
      plateforme.setPlateformeId(1);

      containers = conteneurManager.findByPlateformeOrigWithOrderManager(plateforme);
   }


   @Test
   public void testBuildDetailPlanSansBoites(){
      Conteneur conteneur = containers.get(0);
      long startTime = System.currentTimeMillis();
      DocumentData documentData = sansBoiteGenerator.buildDetailPlan(conteneur);

      long endTime = System.currentTimeMillis();
      long executionTime = endTime - startTime;
      assertNotNull(documentData);
      System.out.println(documentData);
      System.out.println(executionTime);
      assertTrue(documentData instanceof DataAsTable);

      DataAsTable dataAsTable = (DataAsTable) documentData;
      List<CellRow> cellRows = dataAsTable.getListCellRow();
      assertNotNull(cellRows);
      assertFalse(cellRows.isEmpty());
   }

   @Test
   public void testBuildDetailPlanAvecBoites(){
      // Assume we are testing the first container
      Conteneur conteneur = containers.get(0);

      DataAsTable data = avecBoiteGenerator.createEnceintesSection(conteneur);
      data.print();
      System.out.println(data.getNbCellRow());

   }

   @Test
   public void testGenerateWithPOI() throws IOException{
      long startTime = System.nanoTime();
      OutputStreamData result = sansBoiteGenerator.generate(containers);
      long endTime = System.nanoTime();
      long executionTime = endTime - startTime; // in nanoseconds

      long executionTimeMillis = executionTime / 1_000_000;
      System.out.println("Execution time: " + executionTimeMillis + " ms");

      // Assert
      assertNotNull(result);
      System.out.println("File Name: " + result.getFileName());
      assertEquals("xlsx", result.getFormat());
      assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", result.getContentType());

      ByteArrayOutputStream byteArrayOutputStream = result.getOutputStream();

      try( FileOutputStream fileOutputStream = new FileOutputStream(new File(result.getFileName())) ){
         byteArrayOutputStream.writeTo(fileOutputStream);
         //        }

      }

   }
}
