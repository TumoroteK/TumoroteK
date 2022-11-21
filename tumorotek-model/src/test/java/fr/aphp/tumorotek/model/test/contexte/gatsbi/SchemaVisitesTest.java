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
package fr.aphp.tumorotek.model.test.contexte.gatsbi;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.gatsbi.IntervalleType;
import fr.aphp.tumorotek.model.contexte.gatsbi.Parametrage;
import fr.aphp.tumorotek.model.contexte.gatsbi.SchemaVisites;
import fr.aphp.tumorotek.model.contexte.gatsbi.Visite;
import fr.aphp.tumorotek.model.contexte.gatsbi.VisitePrelevement;

/**
 *
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.3.0-gatsbi
 *
 */
public class SchemaVisitesTest 
{


   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testProduceMaladiesFromSchema(){
      
      VisitePrelevement pSang = new VisitePrelevement(0, 2, new Parametrage(1, "SANG", null));
      VisitePrelevement pTissu = new VisitePrelevement(1, 1, new Parametrage(1, "TISSU", null));
      
      List<Visite> visites = new ArrayList<Visite>();
      visites.add(new Visite("VIS1", 1, 10, IntervalleType.JOURS, Arrays.asList(pSang, pTissu)));
      visites.add(new Visite("VIS2", 1, 3, IntervalleType.MOIS, Arrays.asList(pSang)));
      visites.add(new Visite("VIS3", 1, 1, IntervalleType.ANNEES, null));
      
      SchemaVisites schema = new SchemaVisites(visites);
      
      Patient p1 = new Patient();
      p1.setPatientId(1);
      
      List<Maladie> mals = schema.produceMaladiesFromSchema(p1, LocalDate.of(2022, 3, 14));
      assertTrue(mals.size() == 3);
      assertTrue(mals.get(0).getPatient().getPatientId().equals(p1.getPatientId()));
      assertTrue(mals.get(0).getLibelle().equals("VIS1"));
      assertTrue(mals.get(0).getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.of(2022, 3, 24)));
      assertTrue(mals.get(1).getPatient().getPatientId().equals(p1.getPatientId()));
      assertTrue(mals.get(1).getLibelle().equals("VIS2"));
      assertTrue(mals.get(1).getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.of(2022, 6, 14)));
      assertTrue(mals.get(2).getPatient().getPatientId().equals(p1.getPatientId()));
      assertTrue(mals.get(2).getLibelle().equals("VIS3"));
      assertTrue(mals.get(2).getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(LocalDate.of(2023, 3, 14)));

      
      // nulls
      mals = schema.produceMaladiesFromSchema(p1, null);
      assertTrue(mals.isEmpty());
      
      mals = schema.produceMaladiesFromSchema(null, LocalDate.of(2022, 3, 14));
      assertTrue(mals.isEmpty());
   }
}