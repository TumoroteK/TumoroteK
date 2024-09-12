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

