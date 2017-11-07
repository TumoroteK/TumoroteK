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
package fr.aphp.tumorotek.dao.test.imprimante;

import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.imprimante.ModeleTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.ModeleType;

/**
 * 
 * Classe de test pour le DAO ModeleTypeDao et le bean du domaine ModeleType.
 * 
 * @author Pierre Ventadour.
 * @version 18/03/2011
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ModeleTypeDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private ModeleTypeDao modeleTypeDao;
	
	public ModeleTypeDaoTest() {
		
	}

	public void setModeleTypeDao(ModeleTypeDao mDao) {
		this.modeleTypeDao = mDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ModeleType> liste = modeleTypeDao.findAll();
		assertTrue(liste.size() == 2);
	}
	
	/**
	 * Test l'appel de la méthode findByOrder().
	 */
	public void testFindByOrder() {
		List<ModeleType> liste = modeleTypeDao.findByOrder();
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getType().equals("Etiquettes"));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "test";
		String nom2 = "test2";
		ModeleType mt1 = new ModeleType();
		mt1.setType(nom);
		ModeleType mt2 = new ModeleType();
		mt2.setType(nom);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(mt1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(mt1.equals(mt1));
		// 2 objets sont égaux entre eux
		assertTrue(mt1.equals(mt2));
		assertTrue(mt2.equals(mt1));
		
		mt1.setType(null);
		mt2.setType(null);
		assertTrue(mt1.equals(mt2));

		mt2.setType(nom);
		assertFalse(mt1.equals(mt2));
		
		mt1.setType(nom);
		mt2.setType(nom2);
		assertFalse(mt1.equals(mt2));
		
		Categorie c = new Categorie();
		assertFalse(mt1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws Exception Lance une exception.
	 */
	public void testHashCode() throws Exception {
		String nom = "test";
		ModeleType mt1 = new ModeleType();
		mt1.setType(nom);
		ModeleType mt2 = new ModeleType();
		mt2.setType(nom);
		ModeleType mt3 = new ModeleType();
		
		assertTrue(mt3.hashCode() > 0);
		
		int hash = mt1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(mt1.hashCode() == mt2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == mt1.hashCode());
		assertTrue(hash == mt1.hashCode());
		assertTrue(hash == mt1.hashCode());
		assertTrue(hash == mt1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		ModeleType mt1 = modeleTypeDao.findById(1);
		assertTrue(mt1.toString().
				equals("{" + mt1.getType() + "}"));
		
		ModeleType mt2 = new ModeleType();
		assertTrue(mt2.toString().equals("{Empty ModeleType}"));
	}

}
