package fr.aphp.tumorotek.manager.test.stockage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dto.OutputStreamData;
import fr.aphp.tumorotek.manager.impl.stockage.planconteneur.PlanCongelateurAvecBoiteExcelGenerator;
import fr.aphp.tumorotek.manager.io.document.DataAsTable;
import fr.aphp.tumorotek.manager.io.document.DocumentData;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

public class PlanCongelateurAvecBoiteGeneratorTest extends AbstractManagerTest4
{
   @Autowired
   private PlanCongelateurAvecBoiteExcelGenerator avecBoiteGenerator;

   @Autowired
   private ConteneurManager conteneurManager;

   private List<Conteneur> containers;

   @Before
   public void setUp(){
      Plateforme plateforme = new Plateforme();
      plateforme.setPlateformeId(1);

      containers = conteneurManager.findByPlateformeOrigWithOrderManager(plateforme);
   }

   @Test
   public void testBuildDetailPlanAvecBoites(){
      // Assume we are testing the first container
      Conteneur conteneur = containers.get(0);

      DocumentData data = avecBoiteGenerator.buildDetailPlan(conteneur);

      System.out.println(((DataAsTable)data).write());

   }

   @Test
   public void testGenerateAvecBoite2WithPOI() throws IOException{
      long startTime = System.nanoTime();
      OutputStreamData result = avecBoiteGenerator.generate(containers);
      long endTime = System.nanoTime();
      long executionTime = endTime - startTime; // in nanoseconds

      long executionTimeMillis = executionTime / 1_000_000;
      System.out.println("Execution time: " + executionTimeMillis + " ms");

      // Assert
      assertNotNull(result);
      System.out.println("File Name: " + result.getFileName());
      assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", result.getContentType());

      ByteArrayOutputStream byteArrayOutputStream = result.getOutputStream();

      try( FileOutputStream fileOutputStream = new FileOutputStream(new File(result.getFileName())) ){
         byteArrayOutputStream.writeTo(fileOutputStream);

      }

   }
}
