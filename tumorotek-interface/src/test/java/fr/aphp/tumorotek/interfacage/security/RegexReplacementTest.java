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
package fr.aphp.tumorotek.interfacage.security;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegexReplacementTest {

   //   Tester l'expression régulière  ".*\|+$"
   @Test
   public void testEndsWith_SimpleCase() {
      String input = "Hello|";
      assertEquals("Simple case with single pipe", input.matches(".*\\|+$"), input.endsWith("|"));
   }

   @Test
   public void testEndsWith_MultiplePipes() {
      String input = "Hello||||";
      assertTrue("Multiple pipes at end should match both patterns",
         input.matches(".*\\|+$") && input.endsWith("|"));
   }

   @Test
   public void testEndsWith_EmptyString() {
      String input = "";
      assertFalse("Empty string should not match either pattern",
         input.matches(".*\\|+$") || input.endsWith("|"));
   }

   @Test
   public void testEndsWith_PipeInMiddle() {
      String input = "Hel|lo";
      assertFalse("Pipe in middle should not match either pattern",
         input.matches(".*\\|+$") || input.endsWith("|"));
   }

   //   Tester l'expression régulière  ".*ACK.*

   @Test
   public void testContainsACK_SimpleCase() {
      String input = "This is an ACK message";
      assertEquals("Simple ACK case", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContainsACK_PartialMatch() {
      String input = "BACKUP";
      assertEquals("BACKUP string case", input.matches(".*ACK.*"), input.contains("ACK"));

   }

   @Test
   public void testContainsACK_CaseSensitivity() {
      String input = "This is an ack message";
      assertFalse(input.matches(".*ACK.*"));
      assertFalse(input.contains("ACK"));
   }

   //   Tester l'expression régulière  ".*STORAGE.*"

   @Test
   public void testContainsStorage_SimpleCase() {
      String input = "STORAGE device";
      assertEquals("Simple STORAGE case", input.matches(".*STORAGE.*"), input.contains("STORAGE"));
   }

   @Test
   public void testContainsStorage_EmptyString() {
      String input = "";
      assertFalse("Empty string should not match either pattern",
         input.matches(".*STORAGE.*") || input.contains("STORAGE"));
   }


   //   Tester l'expression régulière  ".*Biothèque Sein.*"
   @Test
   public void testContainsBiotheque_SimpleCase() {
      String input = "Test Biothèque Sein case";
      assertEquals("Simple Biothèque Sein case", input.matches(".*Biothèque Sein.*"), input.contains("Biothèque Sein"));
   }

   @Test
   public void testContainsBiotheque_SpecialCharacters() {
      String input = "Test Biotheque Sein case"; // Without è
      assertFalse(input.matches(".*Biothèque Sein.*"));
      assertFalse(input.contains("Biothèque Sein"));
   }

   @Test
   public void testContainsBiotheque_PartialMatch() {
      String input = "Biothèque without Sein";
      assertFalse("Partial match should fail for both",
         input.matches(".*Biothèque Sein.*") || input.contains("Biothèque Sein"));
   }
}
