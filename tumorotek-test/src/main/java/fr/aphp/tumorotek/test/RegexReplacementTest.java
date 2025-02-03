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
package fr.aphp.tumorotek.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Cette classe teste les remplacements des expressions régulières utilisées dans l'application, en s'assurant
 * que les remplacements se comportent de manière égale à ce qui est attendu (assertEquals). Chaque méthode vérifie un cas spécifique
 * d'une expression régulière donnée et compare son comportement avec une alternative basée sur des chaînes de caractères.
 *
 * <p>Les tests incluent différents scénarios pour valider que les correspondances sont égales et pour éviter
 * les vulnérabilités liées aux expressions régulières.</p>
 *
 */

public class RegexReplacementTest
{


   // ***************** Tester l'expression régulière ".*XXX.*", qui a été remplacée par contains() *****************************
   @Test
   public void testContains_SimpleCase(){
      String input = "This is an ACK message";
      assertEquals("Cas simple avec ACK", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContains_ExactMatch(){
      String input = "ACK";
      assertEquals("Cas exact où la chaîne est strictement égale", input.matches("ACK"), input.equals("ACK"));
   }

   @Test
   public void testContains_PartialMatch(){
      String input = "BACKUP";
      assertEquals("Cas de chaîne BACKUP", input.matches(".*ACK.*"), input.contains("ACK"));

   }

   @Test
   public void testContains_CaseSensitivity(){
      String input = "This is an ack message";
      assertEquals("ACK est sensible à la casse pour input.matches", input.matches(".*ACK.*"), input.contains("ACK"));
   }


   @Test
   public void testContains_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit correspondre à aucun modèle", input.matches(".*STORAGE.*"),
         input.contains("STORAGE"));
   }

   @Test
   public void testContains_StartOfString(){
      String input = "ACK is here";
      assertEquals("ACK au début de la chaîne", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContains_EndOfString(){
      String input = "Message contains ACK";
      assertEquals("ACK à la fin de la chaîne", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContains_SpecialCharacters(){
      String input = "Test Biotheque Sein case"; // Sans è
      assertEquals("Les caractères spéciaux doivent correspondre exactement", input.matches(".*Biothèque Sein.*"),
         input.contains("Biothèque Sein"));
   }

   @Test
   public void testContains_WithSpaces(){
      String input = " A C K ";
      assertEquals("ACK avec des espaces", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContains_WithNumbers(){
      String input = "123ACK456";
      assertEquals("ACK entouré de nombres", input.matches(".*ACK.*"), input.contains("ACK"));
   }


   // ******************  Tester l'expression régulière  ".*XXX", , qui a été remplacée par endsWith() ***************************

   @Test
      public void testEndsWithX_SimpleCase(){
      String input = "TestBoolBox";
      assertEquals("Cas simple avec suffixe BoolBox", input.matches(".*BoolBox"), input.endsWith("BoolBox"));
   }


   @Test
   public void testEndsWith_ExactMatch(){
      String input = "Model";
      assertEquals("Cas où la chaîne est exactement Model", input.matches(".*Model"), input.endsWith("Model"));
   }

   @Test
   public void testEndsWith_PartialMatch(){
      String input = "BoolBoxExtra";
      assertEquals("Ne doit pas correspondre lorsque BoolBox n'est pas à la fin", input.matches(".*BoolBox"),
                  input.endsWith("BoolBox"));
   }

   @Test
   public void testEndsWith_CaseSensitivity(){
      String input = "Testboolbox"; // Different casing
      assertEquals("BoolBox est sensible à la casse pour input.matches", input.matches(".*BoolBox"),
                  input.endsWith("BoolBox"));
   }

   @Test
   public void testEndsWith_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit correspondre à aucun modèle", input.matches(".*Storage"),
                   input.endsWith("Storage"));
   }

   @Test
   public void testEndsWith_SpecialCharacters(){
      String input = "Test_BoolBox!";
      assertEquals("Les caractères spéciaux doivent correspondre exactement", input.matches(".*BoolBox!"),
                   input.endsWith("BoolBox!"));
   }

   @Test
   public void testEndsWith_WithSpaces(){
      String input = "Test BoolBox ";
      assertEquals("BoolBox avec un espace final ne doit pas correspondre", input.matches(".*BoolBox"),
                  input.endsWith("BoolBox"));
   }

   @Test
   public void testEndsWith_WithNumbers(){
      String input = "Model2024";
      assertEquals("Model ne doit correspondre que s'il est à la fin", input.matches(".*Model"),
                  input.endsWith("Model"));
   }



   // **************************  Tester l'expression régulière  "XXX.*", qui a été remplacée par startsWith() *******************


   @Test
   public void testStartsWith_SimpleCase(){
      String input = "annoBoxTest";
      assertEquals("Cas simple avec préfixe annoBox", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }


   @Test
   public void testStartsWith_ExactMatch(){
      String input = "annoBox";
      assertEquals("Cas de correspondance exacte", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   @Test
   public void testStartsWith_PartialMatch(){
      String input = "annoBoxTest";
      assertEquals("Le préfixe 'annoBox' doit correspondre correctement", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   @Test
   public void testStartsWith_CaseSensitive(){
      String input = "DateTest";
      assertEquals("Le préfixe date doit être sensible à la casse", input.matches("date.*"), input.startsWith("date"));
   }

   @Test
   public void testStartsWith_SpecialCharacters(){
      String input = "thesaurus@#";
      assertEquals("Cas avec des caractères spéciaux après thesaurus", input.matches("thesaurus.*"),
         input.startsWith("thesaurus"));
   }

   @Test
   public void testStartsWith_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   @Test
   public void testStartsWith_WithSpaces(){
      String input = " annoBoxTest";  // Note the leading space
      assertEquals("La chaîne avec un espace au début ne doit pas correspondre", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   @Test
   public void testStartsWith_WithNumbers(){
      String input = "2023Test";
      assertEquals("Le préfixe numérique '2023' doit être traité correctement", input.matches("2023.*"),
                  input.startsWith("2023"));
   }

   @Test
   public void testStartsWith_WithNumbersAndText(){
      String input = "2023Test2024";
      assertEquals("Les chaînes avec des nombres et du texte au début doivent être traitées correctement",
                  input.matches("2023.*"), input.startsWith("2023"));
   }

   @Test
   public void testStartsWith_SingleCharacter(){
      String input = "A";
      assertEquals("Une chaîne avec un seul caractère comme préfixe doit fonctionner", input.matches("A.*"),
                           input.startsWith("A"));
   }

   @Test
   public void testStartsWith_NotMatchingPrefix(){
      String input = "TestBox";
      assertEquals("La chaîne ne doit pas correspondre si le préfixe est incorrect", input.matches("annoBox.*"),
                           input.startsWith("annoBox"));
   }

   @Test
   public void testStartsWith_SpecialCharactersAtStart(){
      String input = "@hello";
      assertEquals("Les caractères spéciaux en début de chaîne doivent être pris en compte", input.matches("@.*"),
                            input.startsWith("@"));
   }

   //   ********** Tester l'expression régulière  "XXX.*YYY", qui a été remplacée par startsWith() && endsWith() *****************

   @Test
   public void testStartsWithEndsWith_SimpleCase(){
      String input = "ConformeTestRaison";
      assertEquals("Cas simple avec Conforme et Raison", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_ExactMatch(){
      String input = "ConformeRaison";
      assertEquals("Cas de correspondance exacte", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_OnlyPrefix(){
      String input = "Conforme";
      assertEquals("Seulement le préfixe. Ne doit pas correspondre", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }



   @Test
   public void testStartsWithEndsWithDotIncluded_WithDot(){
      String input = "ConformeTest.Raison";
      assertEquals("Cas avec du contenu entre Conforme et .Raison", input.matches("Conforme(.*)\\.Raison"),
         input.startsWith("Conforme") && input.endsWith(".Raison"));
   }

   @Test
   public void testStartsWithEndsWithDotIncluded_NoDot(){
      String input = "ConformeRaison";
      assertEquals("Cas sans point ne doit pas correspondre", input.matches("Conforme(.*)\\.Raison"),
         input.startsWith("Conforme") && input.endsWith(".Raison"));
   }

   @Test
   public void testStartsWithEndsWith_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_SpecialCharacters(){
      String input = "Conforme@123Raison!";
      assertEquals("Cas avec des caractères spéciaux", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_Spaces(){
      String input = "Conforme Test Raison ";
      assertEquals("Cas avec des espaces entre Conforme et Raison", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_MixedCase(){
      String input = "ConformeTestRaison";
      assertEquals("La casse doit correspondre exactement", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_Numbers(){
      String input = "Conforme123Raison456";
      assertEquals("Cas avec des nombres entre Conforme et Raison", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_PartialMatch(){
      String input = "ConformeSomethingElseRaison";
      assertEquals("Cas avec un autre mot entre Conforme et Raison", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_NonMatchingPrefix(){
      String input = "TestConformeRaison";
      assertEquals("Le préfixe ne doit pas correspondre si c'est incorrect", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_NonMatchingSuffix(){
      String input = "ConformeTestRaisonTest";
      assertEquals("Le suffixe ne doit pas correspondre si c'est incorrect", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testStartsWithEndsWith_MissingPrefix(){
      String input = "RaisonConforme";
      assertEquals("Le préfixe 'Conforme' doit être au début", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

//   ******************** Cas spécifiques à tester pour les expressions régulières *********************************************


//     Tester l'expression régulière  ".*-[0-9]+" (chaîne contient un tiret suivi d'au moins un chiffre,
//     peu importe ce qui précède.

   @Test
   public void testNumberSuffix_SimpleCase(){
      String input = "test-123";
      assertEquals("Cas simple avec un suffixe numérique", input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_MultipleHyphens(){
      String input = "test-abc-456";
      assertEquals("Cas avec plusieurs tirets", input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_NoHyphen(){
      String input = "test123";
      assertEquals("L'expression régulière ne doit pas correspondre à une chaîne sans tiret",
         input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_HyphenNoDigits(){
      String input = "test-abc";
      assertEquals("L'expression régulière ne doit pas correspondre à une chaîne avec un tiret mais sans chiffres après",
         input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_DigitsBeforeHyphen(){
      String input = "123-test-456";
      assertEquals("L'expression régulière doit correspondre à un modèle avec des chiffres après le tiret",
         input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_LeadingWhitespace(){
      String input = "  test-123";
      assertEquals("L'expression régulière doit correspondre même avec un espace avant le tiret",
         input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   @Test
   public void testNumberSuffix_NoMatch(){
      String input = "test-abc";
      assertEquals("Cas sans suffixe numérique", input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }


   //   Tester l'expression régulière  ".*\|+$"
   @Test
   public void testEndsWith_SimpleCase(){
      String input = "Hello|";
      assertEquals("Cas simple avec une seule barre verticale", input.matches(".*\\|+$"), input.endsWith("|"));
   }

   @Test
   public void testEndsWith_NoPipe(){
      String input = "Hello";
      assertEquals("L'expression régulière ne doit pas correspondre à une chaîne sans barre verticale à la fin",
         input.matches(".*\\|+$"),
         input.endsWith("|"));
   }

   @Test
   public void testEndsWith_MultiplePipes(){
      String input = "Hello||||";
      assertEquals("Des barres verticales multiples à la fin doivent correspondre aux deux modèles", input.matches(".*\\|+$"),
         input.endsWith("|"));
   }

   @Test
   public void testEndsWith_MixedCaseMultiplePipes(){
      String input = "Test|AB|C|";
      assertEquals("L'expression régulière doit correspondre avec des barres verticales multiples à la fin",
         input.matches(".*\\|+$"),
         input.endsWith("|"));
   }


   @Test
   public void testEndsWith_PipesInMiddle(){
      String input = "Hel|lo";
      assertEquals("L'expression régulière ne doit pas correspondre lorsque la barre verticale est au milieu",
         input.matches(".*\\|+$"),
         input.endsWith("|"));
   }


   @Test
   public void testEndsWith_PipeInMiddle(){
      String input = "Hel|lo";
      assertEquals("Une barre verticale au milieu ne doit correspondre à aucun modèle", input.matches(".*\\|+$"),
         input.endsWith("|"));
   }



}

