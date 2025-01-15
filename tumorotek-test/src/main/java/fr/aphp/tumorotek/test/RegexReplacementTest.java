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

   //   Tester l'expression régulière  ".*\|+$"
   @Test
   public void testEndsWith_SimpleCase(){
      String input = "Hello|";
      assertEquals("Cas simple avec une seule barre verticale", input.matches(".*\\|+$"), input.endsWith("|"));
   }

   @Test
   public void testEndsWith_MultiplePipes(){
      String input = "Hello||||";
      assertEquals("Des barres verticales multiples à la fin doivent correspondre aux deux modèles", input.matches(".*\\|+$"),
         input.endsWith("|"));
   }

   @Test
   public void testEndsWith_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit correspondre à aucun modèle", input.matches(".*\\|+$"), input.endsWith("|"));
   }

   @Test
   public void testEndsWith_PipeInMiddle(){
      String input = "Hel|lo";
      assertEquals("Une barre verticale au milieu ne doit correspondre à aucun modèle", input.matches(".*\\|+$"),
         input.endsWith("|"));
   }

   //   Tester l'expression régulière  ".*ACK.*
   @Test
   public void testContainsACK_SimpleCase(){
      String input = "This is an ACK message";
      assertEquals("Cas simple avec ACK", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   @Test
   public void testContainsACK_PartialMatch(){
      String input = "BACKUP";
      assertEquals("Cas de chaîne BACKUP", input.matches(".*ACK.*"), input.contains("ACK"));

   }

   @Test
   public void testContainsACK_CaseSensitivity(){
      String input = "This is an ack message";
      assertEquals("ACK est sensible à la casse pour input.matches", input.matches(".*ACK.*"), input.contains("ACK"));
   }

   //   Tester l'expression régulière  ".*STORAGE.*"

   @Test
   public void testContainsStorage_SimpleCase(){
      String input = "STORAGE device";
      assertEquals("Cas simple avec STORAGE", input.matches(".*STORAGE.*"), input.contains("STORAGE"));
   }

   @Test
   public void testContainsStorage_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit correspondre à aucun modèle", input.matches(".*STORAGE.*"),
         input.contains("STORAGE"));
   }

   //   Tester l'expression régulière  ".*Biothèque Sein.*"
   @Test
   public void testContainsBiotheque_SimpleCase(){
      String input = "Test Biothèque Sein case";
      assertEquals("Cas simple avec Biothèque Sein", input.matches(".*Biothèque Sein.*"), input.contains("Biothèque Sein"));
   }

   @Test
   public void testContainsBiotheque_SpecialCharacters(){
      String input = "Test Biotheque Sein case"; // Without è
      assertEquals("Les caractères spéciaux doivent correspondre exactement", input.matches(".*Biothèque Sein.*"),
         input.contains("Biothèque Sein"));
   }

   @Test
   public void testContainsBiotheque_PartialMatch(){
      String input = "Biothèque without Sein";
      assertEquals("Une correspondance partielle ne doit pas réussir", input.matches(".*Biothèque Sein.*"),
         input.contains("Biothèque Sein"));
   }

   //   Tester l'expression régulière  "BoolBox"

   @Test
   public void testBoolBox_SimpleCase(){
      String input = "TestBoolBox";
      assertEquals("Cas simple avec suffixe BoolBox", input.matches(".*BoolBox"), input.endsWith("BoolBox"));
   }

   @Test
   public void testBoolBox_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches(".*BoolBox"), input.endsWith("BoolBox"));
   }

   @Test
   public void testBoolBox_PartialMatch(){
      String input = "BoolBoxExtra";
      assertEquals("Ne doit pas correspondre lorsque BoolBox n'est pas à la fin", input.matches(".*BoolBox"),
         input.endsWith("BoolBox"));
   }

   @Test
   public void testAnnoBox_SimpleCase(){
      String input = "annoBoxTest";
      assertEquals("Cas simple avec préfixe annoBox", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   //   Tester l'expression régulière  "annoBox"

   @Test
   public void testAnnoBox_ExactMatch(){
      String input = "annoBox";
      assertEquals("Cas de correspondance exacte", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   @Test
   public void testAnnoBox_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches("annoBox.*"), input.startsWith("annoBox"));
   }

   //   Tester l'expression régulière  "Conforme.*Raison"

   @Test
   public void testConformeRaison_SimpleCase(){
      String input = "ConformeTestRaison";
      assertEquals("Cas simple avec Conforme et Raison", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testConformeRaison_ExactMatch(){
      String input = "ConformeRaison";
      assertEquals("Cas de correspondance exacte", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   @Test
   public void testConformeRaison_OnlyPrefix(){
      String input = "Conforme";
      assertEquals("Seulement le préfixe. Ne doit pas correspondre", input.matches("Conforme.*Raison"),
         input.startsWith("Conforme") && input.endsWith("Raison"));
   }

   //   Tester l'expression régulière  "Conforme(.*)\.Raison"

   @Test
   public void testConformeDotRaison_SimpleCase(){
      String input = "Conforme.Raison";
      assertEquals("Cas simple avec un point", input.matches("Conforme(.*)\\.Raison"),
         input.startsWith("Conforme") && input.endsWith(".Raison"));
   }

   @Test
   public void testConformeDotRaison_WithContent(){
      String input = "ConformeTest.Raison";
      assertEquals("Cas avec du contenu entre Conforme et .Raison", input.matches("Conforme(.*)\\.Raison"),
         input.startsWith("Conforme") && input.endsWith(".Raison"));
   }

   @Test
   public void testConformeDotRaison_NoDot(){
      String input = "ConformeRaison";
      assertEquals("Cas sans point ne doit pas correspondre", input.matches("Conforme(.*)\\.Raison"),
         input.startsWith("Conforme") && input.endsWith(".Raison"));
   }

   //   Tester l'expression régulière  ".*-[0-9]+"

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
   public void testNumberSuffix_NoMatch(){
      String input = "test-abc";
      assertEquals("Cas sans suffixe numérique", input.matches(".*-[0-9]+"),
         input.lastIndexOf("-") != -1 && input.substring(input.lastIndexOf("-") + 1).matches("[0-9]+"));
   }

   //   Tester l'expression régulière  "INCa.*"
   @Test
   public void testINCa_SimpleCase(){
      String input = "INCaTest";
      assertEquals("Cas simple avec le préfixe INCa", input.matches("INCa.*"), input.startsWith("INCa"));
   }

   @Test
   public void testINCa_CaseInsensitive(){
      String input = "incatest";
      assertEquals("Le préfixe doit être sensible à la casse", input.matches("INCa.*"), input.startsWith("INCa"));

   }

   @Test
   public void testINCa_WithAdditionalContent(){
      String input = "INCaExtraContent";
      assertEquals("Cas avec du contenu supplémentaire après INCa", input.matches("INCa.*"), input.startsWith("INCa"));
   }

   @Test
   public void testINCa_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches("INCa.*"), input.startsWith("INCa"));
   }

   //   Tester l'expression régulière  "thesaurus.*"
   @Test
   public void testThesaurus_SimpleCase(){
      String input = "thesaurusTest";
      assertEquals("Cas simple avec le préfixe thesaurus", input.matches("thesaurus.*"), input.startsWith("thesaurus"));
   }

   public void testThesaurus_CaseInsensitive(){
      String input = "ThesaurusTest";
      assertEquals("Le préfixe thesaurus doit être sensible à la casse", input.matches("thesaurus.*"),
         input.startsWith("thesaurus.*"));

   }

   @Test
   public void testThesaurus_WithNumbers(){
      String input = "thesaurus123Test";
      assertEquals("Cas avec des chiffres dans le suffixe", input.matches("thesaurus.*"), input.startsWith("thesaurus"));
   }

   @Test
   public void testThesaurus_WithSpecialCharacters(){
      String input = "thesaurus@#";
      assertEquals("Cas avec des caractères spéciaux après thesaurus", input.matches("thesaurus.*"),
         input.startsWith("thesaurus"));
   }

   //   Tester l'expression régulière  ".*Model"
   @Test
   public void testModel_SimpleCase(){
      String input = "TestModel";
      assertEquals("Cas simple avec le suffixe Model", input.matches(".*Model"), input.endsWith("Model"));
   }

   @Test
   public void testModel_MultipleOccurrences(){
      String input = "FirstModelSecondModel";
      assertEquals("Cas avec plusieurs occurrences de Model", input.matches(".*Model"), input.endsWith("Model"));
   }

   @Test
   public void testModel_ExactMatch(){
      String input = "Model";
      assertEquals("Cas où la chaîne est exactement Model", input.matches(".*Model"), input.endsWith("Model"));
   }

   @Test
   public void testModel_WithWhitespace(){
      String input = "Test Model";
      assertEquals("Les espaces dans la chaîne ne doivent pas correspondre", input.matches(".*Model"), input.endsWith("Model"));

   }

   //   Tester l'expression régulière  "Maximum.*"

   @Test
   public void testMaximum_SimpleCase(){
      String input = "MaximumTest";
      assertEquals("Cas simple avec le préfixe Maximum", input.matches("Maximum.*"), input.startsWith("Maximum"));
   }

   @Test
   public void testMaximum_ContainsSpecialCharacters(){
      String input = "Maximum-Extra";
      assertEquals("Cas avec des caractères spéciaux après Maximum", input.matches("Maximum.*"), input.startsWith("Maximum"));
   }

   @Test
   public void testMaximum_MixedCase(){
      String input = "MAXIMUMTest";
      assertEquals("Le préfixe Maximum doit être sensible à la casse", input.matches("Maximum.*"), input.startsWith("Maximum"));
   }

   @Test
   public void testMaximum_WhitespaceBeforePrefix(){
      String input = " MaximumTest";
      assertEquals("Cas avec un espace avant le préfixe Maximum", input.matches("Maximum.*"), input.startsWith("Maximum"));
   }

   //   Tester l'expression régulière  "date.*"
   @Test
   public void testDate_SimpleCase(){
      String input = "dateTest";
      assertEquals("Cas simple avec le préfixe dat", input.matches("date.*"), input.startsWith("date"));
   }

   @Test
   public void testDate_CaseInsensitive(){
      String input = "DateTest";
      assertEquals("Le préfixe date doit être sensible à la casse", input.matches("date.*"), input.startsWith("date"));
   }

   @Test
   public void testDate_WithMultipleWords(){
      String input = "dateExtraTest";
      assertEquals("Cas avec plusieurs mots après le préfixe date", input.matches("date.*"), input.startsWith("date"));
   }

   @Test
   public void testDate_EmptyString(){
      String input = "";
      assertEquals("Une chaîne vide ne doit pas correspondre", input.matches("date.*"), input.startsWith("date"));
   }
}

