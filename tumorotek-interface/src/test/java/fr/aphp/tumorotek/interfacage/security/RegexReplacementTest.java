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
