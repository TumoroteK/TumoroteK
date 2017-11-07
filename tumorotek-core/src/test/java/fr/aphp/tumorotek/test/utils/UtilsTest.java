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

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Test;

import fr.aphp.tumorotek.utils.Utils;

public class UtilsTest {
	
	public UtilsTest() {
		
	}
	
	@Test
	public void testCreateListChars() {
		List<String> res = 
			Utils.createListChars(2, null, new ArrayList<String>());
		assertTrue(res.size() == 2);
		assertTrue(res.get(res.size() - 1).equals("B"));
		
		res = 
			Utils.createListChars(26, null, new ArrayList<String>());
		assertTrue(res.size() == 26);
		assertTrue(res.get(res.size() - 1).equals("Z"));
		
		res = 
			Utils.createListChars(27, null, new ArrayList<String>());
		assertTrue(res.size() == 27);
		assertTrue(res.get(res.size() - 1).equals("AA"));
		
		res = 
			Utils.createListChars(55, null, new ArrayList<String>());
		assertTrue(res.size() == 55);
		
		res = 
			Utils.createListChars(235, null, new ArrayList<String>());
		assertTrue(res.size() == 235);
		assertTrue(res.get(res.size() - 1).equals("IA"));
		
	}
	
	@Test
	public void testFormateString() {
		// test formatage "<"
		assertTrue(Utils.formateString("PTRA.1", "<.").equals("PTRA"));
		assertTrue(Utils.formateString("PTRA.1", "<&").equals(""));
		assertTrue(Utils.formateString("PTRA.*-1", "<.*-").equals("PTRA"));
		assertTrue(Utils.formateString("PTRA.", "<.").equals("PTRA"));
		assertTrue(Utils.formateString(".PTRA", "<.").equals(""));
		
		// test formatage ">"
		assertTrue(Utils.formateString("PTRA.1", ">.").equals("1"));
		assertTrue(Utils.formateString("PTRA.1", ">&").equals(""));
		assertTrue(Utils.formateString("PTRA.*-1", ">.*-").equals("1"));
		assertTrue(Utils.formateString("PTRA.", ">.").equals(""));
		assertTrue(Utils.formateString(".PTRA", ">.").equals("PTRA"));
		
		// test formatage ">"
		assertTrue(Utils.formateString("PTRA.1", "[2,4]").equals("TRA"));
		assertTrue(Utils.formateString("VENTADOUR_PIERRE", 
				"[10,15]").equals("_PIERR"));
		assertTrue(Utils.formateString("PTRA.1", "[8,10]").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "[2,10]")
				.equals("TRA.1"));
		assertTrue(Utils.formateString("PTRA.1", "[0,4]").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "[2,0]").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "[4,2]").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "[3,3]").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "[1,3]").equals("PTR"));
		assertTrue(Utils.formateString("PTRA.1", "[3,6]").equals("RA.1"));
		
		// test mauvais paramétrage
		assertTrue(Utils.formateString("", "<.").equals(""));
		assertTrue(Utils.formateString(null, "<.").equals(""));
		assertTrue(Utils.formateString("PTRA.1", "").equals("PTRA.1"));
		assertTrue(Utils.formateString("PTRA.1", null).equals("PTRA.1"));
	}
	
	@Test
	public void testExtractAssosPlateformesEmetteurs() {
		String value = "1:1,2;2:1,2,3;3:4";
		Hashtable<Integer, List<Integer>> res = Utils
			.extractAssosPlateformesEmetteursRecepteurs(value);
		assertTrue(res.containsKey(1));
		List<Integer> ids = res.get(1);
		assertTrue(ids.size() == 2);
		assertTrue(res.containsKey(2));
		ids = res.get(2);
		assertTrue(ids.size() == 3);
		assertTrue(res.containsKey(3));
		ids = res.get(3);
		assertTrue(ids.size() == 1);
		
		value = "1:1,2,3";
		res = Utils.extractAssosPlateformesEmetteursRecepteurs(value);
		assertTrue(res.containsKey(1));
		ids = res.get(1);
		assertTrue(ids.size() == 3);
		
		value = "1:1";
		res = Utils.extractAssosPlateformesEmetteursRecepteurs(value);
		assertTrue(res.containsKey(1));
		ids = res.get(1);
		assertTrue(ids.size() == 1);
		
		value = "10:14,2,3;2:250,3";
		res = Utils.extractAssosPlateformesEmetteursRecepteurs(value);
		assertTrue(res.containsKey(10));
		ids = res.get(10);
		assertTrue(ids.size() == 3);
		assertTrue(ids.get(0) == 14);
		assertTrue(res.containsKey(2));
		ids = res.get(2);
		assertTrue(ids.size() == 2);
		assertTrue(ids.get(0) == 250);
		
		value = "10:  14,2,3 ;2 :250, 3";
		res = Utils.extractAssosPlateformesEmetteursRecepteurs(value);
		assertTrue(res.containsKey(10));
		ids = res.get(10);
		assertTrue(ids.size() == 3);
		assertTrue(ids.get(0) == 14);
		assertTrue(res.containsKey(2));
		ids = res.get(2);
		assertTrue(ids.size() == 2);
		assertTrue(ids.get(0) == 250);
		
		res = Utils.extractAssosPlateformesEmetteursRecepteurs("");
		res = Utils.extractAssosPlateformesEmetteursRecepteurs(null);
	}
	
	@Test
	public void testStringToUpperCase() {
		assertTrue(Utils.stringToUpperCase("test").equals("TEST"));
		assertTrue(Utils.stringToUpperCase("TEST3").equals("TEST3"));
		assertTrue(Utils.stringToUpperCase("").equals(""));
		assertNull(Utils.stringToUpperCase(null));
	}
	
	@Test
	public void testStringToLowerCase() {
		assertTrue(Utils.stringToLowerCase("TEST3").equals("test3"));
		assertTrue(Utils.stringToLowerCase("test3").equals("test3"));
		assertTrue(Utils.stringToLowerCase("").equals(""));
		assertNull(Utils.stringToLowerCase(null));
	}
	
	@Test
	public void testReplaceCommaByDot() {
		assertTrue(Utils.replaceCommaByDot("15,56,89").equals("15.56.89"));
		assertTrue(Utils.replaceCommaByDot("Lalj,45").equals("Lalj.45"));
		assertTrue(Utils.replaceCommaByDot("").equals(""));
		assertNull(Utils.replaceCommaByDot(null));
	}
}
