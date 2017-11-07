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
package fr.aphp.tumorotek.dao.test.impression;

import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le DAO BlocImpressionDao et le bean 
 * du domaine BlocImpression.
 * 
 * @author Pierre Ventadour.
 * @version 22/07/2010.
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class BlocImpressionDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private BlocImpressionDao blocImpressionDao;
	/** Bean Dao. */
	private EntiteDao entiteDao;
	
	public BlocImpressionDaoTest() {
		
	}

	public void setBlocImpressionDao(BlocImpressionDao bDao) {
		this.blocImpressionDao = bDao;
	}
	
	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<BlocImpression> liste = blocImpressionDao.findAll();
		assertTrue(liste.size() > 0);
	}
	
	/**
	 * Test l'appel de la méthode findByEntite().
	 */
	public void testFindByEntite() {
		Entite e1 = entiteDao.findById(48);
		List<BlocImpression> liste = blocImpressionDao.findByEntite(e1);
		assertTrue(liste.size() == 0);
		
		Entite e2 = entiteDao.findById(2);
		liste = blocImpressionDao.findByEntite(e2);
		assertTrue(liste.size() == 6);
		
		liste = blocImpressionDao.findByEntite(null);
		assertTrue(liste.size() == 0);
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom1 = "nom";
		String nom2 = "nom2";
		Entite e1 = entiteDao.findById(1);
		Entite e2 = entiteDao.findById(2);
		BlocImpression b1 = new BlocImpression();
		BlocImpression b2 = new BlocImpression();
		b1.setNom(nom1);
		b1.setEntite(e1);
		b2.setNom(nom1);
		b2.setEntite(e1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(b1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(b1.equals(b1));
		// 2 objets sont égaux entre eux
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));
		
		b1.setEntite(null);
		b1.setNom(null);
		b2.setEntite(null);
		b2.setNom(null);
		assertTrue(b1.equals(b2));
		b2.setNom(nom1);
		assertFalse(b1.equals(b2));
		b1.setNom(nom1);
		assertTrue(b1.equals(b2));
		b2.setNom(nom2);
		assertFalse(b1.equals(b2));
		b2.setNom(null);
		assertFalse(b1.equals(b2));
		b2.setEntite(e1);
		assertFalse(b1.equals(b2));
		
		b1.setEntite(e1);
		b1.setNom(null);
		b2.setNom(null);
		b2.setEntite(e1);
		assertTrue(b1.equals(b2));
		b2.setEntite(e2);
		assertFalse(b1.equals(b2));
		b2.setNom(nom1);
		assertFalse(b1.equals(b2));
		
		// Vérification de la différenciation de 2 objets
		b1.setNom(nom1);
		assertFalse(b1.equals(b2));
		b2.setNom(nom2);
		b2.setEntite(e1);
		assertFalse(b1.equals(b2));
		

		Categorie c3 = new Categorie();
		assertFalse(b1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom1 = "nom";
		String nom2 = "nom2";
		Entite e1 = entiteDao.findById(1);
		Entite e2 = entiteDao.findById(2);
		BlocImpression b1 = new BlocImpression();
		BlocImpression b2 = new BlocImpression();
		//null
		assertTrue(b1.hashCode() == b2.hashCode());
		
		//Nom
		b2.setNom(nom1);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setNom(nom2);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setNom(nom1);
		assertTrue(b1.hashCode() == b2.hashCode());
		
		//ProtocoleType
		b2.setEntite(e1);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setEntite(e2);
		assertFalse(b1.hashCode() == b2.hashCode());
		b1.setEntite(e1);
		assertTrue(b1.hashCode() == b2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = b1.hashCode();
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());
		assertTrue(hash == b1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		BlocImpression b1 = blocImpressionDao.findById(1);
		assertTrue(b1.toString().
				equals("{" + b1.getNom() + "}"));
		
		BlocImpression b2 = new BlocImpression();
		assertTrue(b2.toString().equals("{Empty BlocImpression}"));
	}

}
