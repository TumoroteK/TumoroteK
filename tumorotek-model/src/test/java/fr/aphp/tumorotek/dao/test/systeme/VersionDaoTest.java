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
package fr.aphp.tumorotek.dao.test.systeme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.systeme.VersionDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Version;

/**
 * 
 * Classe de test pour le DAO VersionDao et le bean du domaine Version.
 * Classe de test créée le 26/05/2011.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class VersionDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private VersionDao versionDao;
	
	public VersionDaoTest() {
		
	}

	public void setVersionDao(VersionDao vDao) {
		this.versionDao = vDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testFindAll() {
		List<Version> liste = versionDao.findAll();
		assertTrue(liste.size() == 3);
	}
	
	/**
	 * Test l'appel de la méthode findByDateChronologique().
	 */
	public void testFindByDateChronologique() {
		List<Version> liste = versionDao.findByDateChronologique();
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getVersion().equals("2.0 beta 1"));
	}
	
	/**
	 * Test l'appel de la méthode findByDateAntiChronologique().
	 */
	public void testFindByDateAntiChronologique() {
		List<Version> liste = versionDao.findByDateAntiChronologique();
		assertTrue(liste.size() == 3);
		assertTrue(liste.get(0).getVersion().equals("2.0"));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {
		String nom = "1.0";
		String nom2 = "2.0";
		Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
		Version v1 = new Version();
		Version v2 = new Version();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(v1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(v1.equals(v1));
		
		/*null*/
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		
		/*Version*/
		v2.setVersion(nom);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setVersion(nom2);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setVersion(nom);
		assertTrue(v1.equals(v2));
		assertTrue(v2.equals(v1));
		
		/*Date*/
		v2.setDate(d1);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setDate(d2);
		assertFalse(v1.equals(v2));
		assertFalse(v2.equals(v1));
		v1.setDate(d1);
		assertTrue(v1.equals(v2));
		
		Categorie c3 = new Categorie();
		assertFalse(v1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		String nom = "1.0";
		String nom2 = "2.0";
		Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
		Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
		Version v1 = new Version();
		Version v2 = new Version();
		
		/*null*/
		assertTrue(v1.hashCode() == v2.hashCode());
		
		/*Nom*/
		v2.setVersion(nom);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setVersion(nom2);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setVersion(nom);
		assertTrue(v1.hashCode() == v2.hashCode());
		
		/*Prenom*/
		v2.setDate(d1);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setDate(d2);
		assertFalse(v1.hashCode() == v2.hashCode());
		v1.setDate(d1);
		assertTrue(v1.hashCode() == v2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = v1.hashCode();
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
		assertTrue(hash == v1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		Version v1 = versionDao.findById(1);
		assertTrue(v1.toString().
				equals("{" + v1.getVersion() + " " 
						+ v1.getDate() + "}"));
		
		Version v2 = new Version();
		assertTrue(v2.toString().equals("{Empty Version}"));
	}

}
