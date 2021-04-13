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
package fr.aphp.tumorotek.test.utils;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.utils.ConversionUtils;

/**
 * @author Gille Chapelot
 *
 */
@RunWith(Enclosed.class)
public class ConversionUtilsTest
{

   public static class TestsNonParametres{

      //Test de conversion non supportés
      @Test(expected=TKException.class)
      public void testConvertNotSupported(){
         Object o = "test";
         ConversionUtils.convert(o, List.class);
      }

      //Test de conversion Float -> Float
      @Test
      public void testConvertFloatToFloat(){

         Object o = Float.valueOf(10.2f);
         Float converted = ConversionUtils.convertToFloat(o);

         assertEquals(Float.valueOf(10.2f), converted);

      }

      //Test de conversion Integer -> Float
      @Test
      public void testConvertIntegerToFloat(){

         Object o = Integer.valueOf(10);
         Float converted = ConversionUtils.convertToFloat(o);

         assertEquals( Float.valueOf(10) , converted);

      }

      //Test de conversion Double -> Float
      @Test
      public void testConvertDoubleToFloat(){

         Object o = Double.valueOf(10.32d);
         Float converted = ConversionUtils.convertToFloat(o);

         assertEquals( new Float(10.32d) , converted);

      }

      //Test de conversion String -> Float
      @Test
      public void testConvertStringToFloat(){

         Object o = "10.75";
         Float converted = ConversionUtils.convertToFloat(o);

         assertEquals(Float.valueOf("10.75"), converted);

      }

      //Test conversion String non-numérique -> Float
      @Test(expected=TKException.class)
      public void testConvertNotNumericStringToFloat(){

         Object o = "toto";
         ConversionUtils.convertToFloat(o);

      }

      //Test conversion Float non supportée
      @Test(expected=TKException.class)
      public void testUnsupportedToFloat() {

         Object o = new Date();
         ConversionUtils.convertToFloat(o);

      }

      //Test de conversion Float -> Double
      @Test
      public void testConvertFloatToDouble(){

         Object o = Float.valueOf(10.2f);
         Double converted = ConversionUtils.convertToDouble(o);

         assertEquals(Double.valueOf(10.2f), converted);

      }

      //Test de conversion Integer -> Double
      @Test
      public void testConvertIntegerToDouble(){

         Object o = Integer.valueOf(10);
         Double converted = ConversionUtils.convertToDouble(o);

         assertEquals(Double.valueOf(10), converted);

      }

      //Test de conversion Double -> Double
      @Test
      public void testConvertDoubleToDouble(){

         Object o = Double.valueOf(10.32d);
         Double converted = ConversionUtils.convertToDouble(o);

         assertEquals(Double.valueOf(10.32d), converted);

      }

      //Test de conversion String -> Double
      @Test
      public void testConvertStringToDouble(){

         Object o = "10.75";
         Double converted = ConversionUtils.convertToDouble(o);

         assertEquals(Double.valueOf("10.75"), converted);

      }

      //Test conversion String non-numérique -> Double
      @Test(expected=TKException.class)
      public void testConvertNotNumericStringToDouble(){

         Object o = "toto";
         ConversionUtils.convertToDouble(o);

      }

      //Test conversion Double non supportée
      @Test(expected=TKException.class)
      public void testUnsupportedToDouble() {

         Object o = new Date();
         ConversionUtils.convertToDouble(o);

      }

      //Test de conversion Float -> Integer
      @Test
      public void testConvertFloatToInteger(){

         Object o = Float.valueOf(10.2f);
         Integer converted = ConversionUtils.convertToInteger(o);

         Integer expected = Float.valueOf(10.2f).intValue();

         assertEquals(expected , converted);

      }

      //Test de conversion Integer -> Integer
      @Test
      public void testConvertIntegerToInteger(){

         Object o = Integer.valueOf(10);
         Integer converted = ConversionUtils.convertToInteger(o);

         assertEquals(Integer.valueOf(10), converted);

      }

      //Test de conversion Double -> Integer
      @Test
      public void testConvertDoubleToInteger(){

         Object o = Double.valueOf(10.32d);
         Integer converted = ConversionUtils.convertToInteger(o);

         Integer expected = Double.valueOf(10.32d).intValue();

         assertEquals(expected, converted);

      }

      //Test de conversion String -> Integer
      @Test
      public void testConvertStringToInteger(){

         Object o = "23";
         Integer converted = ConversionUtils.convertToInteger(o);

         assertEquals(Integer.valueOf("23"), converted);

      }

      //Test conversion String non-numérique -> Integer
      @Test(expected=TKException.class)
      public void testConvertNotNumericStringToInteger(){

         Object o = "toto";
         ConversionUtils.convertToInteger(o);

      }

      //Test conversion Integer non supportée
      @Test(expected=TKException.class)
      public void testUnsupportedToInteger() {

         Object o = new Date();
         ConversionUtils.convertToInteger(o);

      }

      //Test de conversion Boolean -> Boolean
      @Test
      public void testConvertBooleanToBoolean(){

         Object o = Boolean.TRUE;
         Boolean converted = ConversionUtils.convertToBoolean(o);

         assertEquals(Boolean.TRUE , converted);

      }

      //Test de conversion Integer -> Boolean
      @Test
      public void testConvertIntegerToBooleanFalse(){

         Object o = Integer.valueOf(10);
         Boolean converted = ConversionUtils.convertToBoolean(o);

         assertEquals(Boolean.FALSE, converted);

      }

      //Test de conversion String -> Boolean
      @Test
      public void testConvertStringToBooleanTrue(){

         Object o = "trUe";
         Boolean converted = ConversionUtils.convertToBoolean(o);

         assertEquals(Boolean.TRUE, converted);

      }

      //Test de conversion String -> Boolean
      @Test
      public void testConvertStringToBooleanFalse(){

         Object o = "toto";
         Boolean converted = ConversionUtils.convertToBoolean(o);

         assertEquals(Boolean.FALSE, converted);

      }

      //Test de conversion Integer -> Boolean
      @Test
      public void testConvertIntegerToBooleanTrue(){

         Object o = Integer.valueOf(1);
         Boolean converted = ConversionUtils.convertToBoolean(o);

         assertEquals(Boolean.TRUE, converted);

      }

      //Test conversion Boolean non supportée
      @Test(expected=TKException.class)
      public void testUnsupportedToBoolean() {

         Object o = new Date();
         ConversionUtils.convertToInteger(o);

      }

      //Test conversion Date -> Date
      @Test
      public void testConvertDateToDate() {

         Object o = new Date();
         Date converted = ConversionUtils.convertToDate(o);

         assertEquals(o, converted);

      }

      //Test conversion Calendar -> Date
      @Test
      public void testConvertCalendarToDate() {

         Calendar o = Calendar.getInstance();
         Date converted = ConversionUtils.convertToDate(o);

         assertEquals(o.getTime(), converted);

      }

      //Test conversion String (format de date non prévu) -> Date
      @Test(expected=TKException.class)
      public void testConvertUnexpectedFormatDateStringToDate() {

         Date date = new Date();
         Object o = new SimpleDateFormat("EEE, MMM d, ''yy").format(date);

         ConversionUtils.convertToDate(o);

      }

      //Test conversion Calendar -> Calendar
      @Test
      public void testConvertCalendarToCalendar() {

         Object o = Calendar.getInstance();
         Calendar converted = ConversionUtils.convertToCalendar(o);

         assertEquals(o, converted);

      }

      //Test conversion Date -> Calendar
      @Test
      public void testConvertDateToCalendar() {

         Date o = new Date();
         Calendar converted = ConversionUtils.convertToCalendar(o);

         Calendar expected = Calendar.getInstance();
         expected.setTime(o);

         assertEquals(expected, converted);

      }

      //Test conversion String (format de date non prévu) -> Calendar
      @Test(expected=TKException.class)
      public void testConvertUnexpectedFormatDateStringToCalendar() {

         Date date = new Date();
         Object o = new SimpleDateFormat("EEE, MMM d, ''yy").format(date);

         ConversionUtils.convertToCalendar(o);

      }

      //Test conversion String (format de date non prévu) -> Date
      @Test(expected=TKException.class)
      public void testConvertUnexpectedFormatDateStringGetDateFormat() {

         ConversionUtils.getDateFormat("Wed, Jul 4, '01");

      }

   }

   @RunWith(Parameterized.class)
   public static class TestsDateParametre{

      @Parameter(value=0)
      public String dateString;

      @Parameter(value=1)
      public String dateFormat;

      @Parameters
      public static Collection<Object[]> dateFormats() {

         return Arrays.asList(new Object[][] {
            {"31/12/2000 02:32:54", "dd/MM/yyyy HH:mm:ss"},
            {"31/12/2000 02:32", "dd/MM/yyyy HH:mm"},
            {"31/12/2000", "dd/MM/yyyy"},
            {"2000/12/31 02:32:54", "yyyy/MM/dd HH:mm:ss"},
            {"2000/12/31 02:32", "yyyy/MM/dd HH:mm"},
            {"2000/12/31", "yyyy/MM/dd"}
         });

      }

      //Test conversion String -> Date
      @Test
      public void testConvertStringToDate() {

         SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

         Date date = new Date();
         Object o = sdf.format(date);

         Date converted = ConversionUtils.convertToDate(o);

         //On compare les dates sous forme de String car selon le format de date, on perd de la précision
         assertEquals(o, sdf.format(converted));

      }

      //Test conversion String -> Date
      @Test
      public void testConvertStringToCalendar() {

         SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

         Date date = new Date();
         Object o = sdf.format(date);

         Calendar converted = ConversionUtils.convertToCalendar(o);

         //On compare les dates sous forme de String car selon le format de date, on perd de la précision
         assertEquals(o, sdf.format(converted.getTime()));

      }

      //Test de récupération du format d'une date sous forme de String
      @Test
      public void testGetDateFormat(){

         String actual = ConversionUtils.getDateFormat(dateString);

         assertEquals(dateFormat, actual);

      }

   }

}
