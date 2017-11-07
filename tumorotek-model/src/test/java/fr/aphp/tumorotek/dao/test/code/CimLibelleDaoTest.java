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
package fr.aphp.tumorotek.dao.test.code;

import java.util.List;

import fr.aphp.tumorotek.dao.code.CimLibelleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.code.CimLibelle;
import fr.aphp.tumorotek.model.contexte.Categorie;

/**
 * 
 * Classe de test pour le DAO CimLibelleoDao et le bean du 
 * domaine CimLibelle.
 * Classe de test créée le 21/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CimLibelleDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private CimLibelleDao cimLibelleDao;
	
	/**
	 * Constructeur.
	 */
	public CimLibelleDaoTest() {
	}
	
	@Override
	protected String[] getConfigLocations()	{
		return new String[]{ "applicationContextDao-codes.xml" };
	}
	
	/**
	 * Setter du bean Dao.
	 * @param cDao est le bean Dao.
	 */
	public void setCimLibelleDao(CimLibelleDao cDao) {
		this.cimLibelleDao = cDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllLibelles() {
		List<CimLibelle> cims = cimLibelleDao.findAll();
		assertTrue(cims.size() == 32652);
	}
	
	public void testFindByLibelleLike() {
		List<CimLibelle> cims = cimLibelleDao.findByLibelleLike("%shigello%");
		assertTrue(cims.size() == 7);
		cims = cimLibelleDao.findByLibelleLike("TEST");
		assertTrue(cims.size() == 0);
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		Integer id1 = 1;
		Integer id2 = 2;
		CimLibelle c1 = new CimLibelle();
		CimLibelle c2 = new CimLibelle();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(c1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(c1.equals(c1));
		
		/*null --> Ids ne pouvant etre nuls car table systemes*/
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		
		/*Id*/
		c2.setLid(id1);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setLid(id2);
		assertFalse(c1.equals(c2));
		assertFalse(c2.equals(c1));
		c1.setLid(id1);
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
		
		Categorie c = new Categorie();
		assertFalse(c1.equals(c));
		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
			
		String lib1 = "lib1";
		CimLibelle c1 = new CimLibelle();
		c1.setLibelle(lib1);
		CimLibelle c2 = new CimLibelle();
		c2.setLibelle(lib1);
		CimLibelle c3 = new CimLibelle();
		c3.setLibelle(null);
		
		int hash = c1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(c1.hashCode() == c2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		assertTrue(hash == c1.hashCode());
		
	}
	
	public void testToString() {
		CimLibelle a = new CimLibelle();
		a.setLibelle("Disease");
		assertTrue(a.toString().equals("{CimLibelle: Disease}"));
	}

}