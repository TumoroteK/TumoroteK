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

import fr.aphp.tumorotek.manager.impl.stockage.planconteneur.PlanCongelateurSansBoiteExcelGenerator;
import fr.aphp.tumorotek.manager.io.document.*;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.utils.TKStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class PlanCongelateurSansBoiteGeneratorTest extends AbstractManagerTest4 {

    @Autowired
    private ConteneurManager conteneurManager;
    @Test
    public void testBuildEntetePlan() {
        // Arrange
        Conteneur conteneur = new Conteneur();
        Service service = new Service();
        service.setNom("Service Name");
        Etablissement etabli = new Etablissement();
        etabli.setNom("Etab Name");
        service.setEtablissement(etabli);
        conteneur.setNom("NBT -80 / Congél VI1");
        conteneur.setDescription("TSU 400 V601");
        conteneur.setService(service);
        Locale locale = Locale.ENGLISH;

        // Expected values
        String expectedDate = TKStringUtils.getCurrentDate(null);
        System.out.println(expectedDate);
        List<LabelValue> expectedLabelValues = new ArrayList<>();
        expectedLabelValues.add(new LabelValue( expectedDate, null,  true, false));
        expectedLabelValues.add(new LabelValue("Nom de congélateur", conteneur.getNom(), false, true));
        expectedLabelValues.add(new LabelValue("Description", conteneur.getDescription(), false, false));
        expectedLabelValues.add(new LabelValue("Etablissement / service", "Service Name", false, false));

        PlanCongelateurSansBoiteExcelGenerator PlanCongelateurSansBoiteExcelGenerator = new PlanCongelateurSansBoiteExcelGenerator();

        // Act
        DocumentContext documentContext = PlanCongelateurSansBoiteExcelGenerator.buildEntetePlan(conteneur, locale);

        // Assert
        List<LabelValue> actualLabelValues = documentContext.getListLabelValue();
        assertEquals(expectedLabelValues.size(), actualLabelValues.size());

        for (int i = 0; i < expectedLabelValues.size(); i++) {
            LabelValue expected = expectedLabelValues.get(i);
            LabelValue actual = actualLabelValues.get(i);
            System.out.println( expected + "e:a" +  actual);
            // Assert the value
            if (i==0) {
                // Special handling for the date to ensure it matches the expected date format
                assertEquals(expected.getLabel(), actual.getLabel());
            } else {
                assertEquals(expected.getLabel(), actual.getLabel());
                assertEquals(expected.getValue(), actual.getValue());
                assertEquals(expected.isLabelInBold(), actual.isLabelInBold());
                assertEquals(expected.isValueInBold(), actual.isValueInBold());
            }


        }
    }
    

    @Test
        public void testBuildPiedPagePlan() {
        // Arrange
        Conteneur conteneur = new Conteneur();
        conteneur.setNom("Test Conteneur");
        PlanCongelateurSansBoiteExcelGenerator PlanCongelateurSansBoiteExcelGenerator = new PlanCongelateurSansBoiteExcelGenerator();

        // Act
        DocumentFooter result = PlanCongelateurSansBoiteExcelGenerator.buildPiedPagePlan(conteneur);

        // Assert
        assertNotNull(result);
        assertEquals("Test Conteneur", result.getLeftData());
        assertNull(result.getRightData()); // Assuming field1 and field2 are set to null in the constructor of DocumentFooter
        assertNull(result.getCenterData());
    }

//    @Test
//    public void buildDetailPlanTest(){
//        Conteneur c = conteneurManager.findByIdManager(1);
//        Locale locale = Locale.ENGLISH;
//        PlanCongelateurSansBoiteExcelGenerator planCongelateurSansBoiteExcelGenerator = new PlanCongelateurSansBoiteExcelGenerator();
//        DataAsTable doculment = (DataAsTable) planCongelateurSansBoiteExcelGenerator.buildDetailPlan(c, locale);
//        System.out.println(doculment);
//
//    }
}
