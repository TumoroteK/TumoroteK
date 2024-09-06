package fr.aphp.tumorotek.test.utils;

import fr.aphp.tumorotek.utils.TKStringUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TKStringUtilsTest {

    /**
    getSafeSheetName: Test de la méthode avec des caractères interdits:
     */
    @Test
    public void testSafeSheetNameWithForbiddenCharacters() {
        String input = "Sheet/Name\\With?Forbidden*Characters[]";
        String expected = "Sheet-Name-With-Forbidden-Charac";
        assertEquals(expected, TKStringUtils.getSafeSheetName(input));
    }

    /**
    getSafeSheetName: Test de la troncature du nom de feuille
     */
    @Test
    public void testSafeSheetNameTruncation() {
        String input = "ThisIsAVeryLongSheetNameThatExceedsThirtyOneCharacters";
        String expected = "ThisIsAVeryLongSheetNameThatExce";
        assertEquals(expected, TKStringUtils.getSafeSheetName(input));
    }

    /**
    getSafeSheetName: Test avec une entrée nulle:
     */
    @Test
    public void testSafeSheetNameWithNullInput() {
        // Test with null input
        String expected = "Sheet";
        assertEquals(expected, TKStringUtils.getSafeSheetName(null));
    }

    /**
    getSafeSheetName: Test avec une entrée vide:
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSafeSheetNameWithEmptyInput() {
        TKStringUtils.getSafeSheetName("");
    }



    /**
     * Teste getCurrentDate avec un modèle de date valide pour vérifier le formatage correct.
     */
    @Test
    public void testGetCurrentDateWithValidPattern() {
        // Arrange
        String pattern = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String expected = now.format(formatter);

        String result = TKStringUtils.getCurrentDate(pattern);

        assertEquals(expected, result);
    }


    /**
     * Teste getCurrentDate avec un modèle null pour vérifier le comportement par défaut.
     */
    @Test
    public void testGetCurrentDateWithNullPattern() {
        String pattern = null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expected = now.format(formatter);

        String result = TKStringUtils.getCurrentDate(pattern);

        assertEquals(expected, result);
    }

    /**
     * Teste getCurrentDate avec un modèle invalide pour vérifier la gestion des erreurs.
     */
    @Test
    public void testGetCurrentDateWithInvalidPattern() {
        String pattern = "invalid-pattern";
        String expected = "Modèle de date invalide : invalid-pattern";

        String result = TKStringUtils.getCurrentDate(pattern);

        assertEquals(expected, result);
    }

    /**
     * Teste getCurrentDate avec un modèle de date vide pour vérifier le comportement par défaut.
     */
    @Test
    public void testGetCurrentDateWithEmptyPattern() {
        String pattern = "";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expected = now.format(formatter);

        String result = TKStringUtils.getCurrentDate(pattern);

        assertEquals(expected, result);
    }


}

