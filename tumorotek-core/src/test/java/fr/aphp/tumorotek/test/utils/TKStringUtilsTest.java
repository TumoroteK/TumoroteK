package fr.aphp.tumorotek.test.utils;

import fr.aphp.tumorotek.utils.TKStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TKStringUtilsTest {
    /*
    getSafeSheetName: Test de la méthode avec des caractères interdits:
     */
    @Test
    public void testSafeSheetNameWithForbiddenCharacters() {
        String input = "Sheet/Name\\With?Forbidden*Characters[]";
        String expected = "Sheet-Name-With-Forbidden-Charac";
        assertEquals(expected, TKStringUtils.getSafeSheetName(input));
    }

    /*
    getSafeSheetName: Test de la troncature du nom de feuille
     */
    @Test
    public void testSafeSheetNameTruncation() {
        String input = "ThisIsAVeryLongSheetNameThatExceedsThirtyOneCharacters";
        String expected = "ThisIsAVeryLongSheetNameThatExce";
        assertEquals(expected, TKStringUtils.getSafeSheetName(input));
    }

    /*
    getSafeSheetName: Test avec une entrée nulle:
     */
    @Test
    public void testSafeSheetNameWithNullInput() {
        // Test with null input
        String expected = "Sheet";
        assertEquals(expected, TKStringUtils.getSafeSheetName(null));
    }

    /*
    getSafeSheetName: Test avec une entrée vide:
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSafeSheetNameWithEmptyInput() {
        TKStringUtils.getSafeSheetName("");
    }



}