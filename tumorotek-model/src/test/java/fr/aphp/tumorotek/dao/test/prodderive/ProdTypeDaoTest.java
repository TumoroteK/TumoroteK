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
package fr.aphp.tumorotek.dao.test.prodderive;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * 
 * Classe de test pour le DAO ProdTypeDao et le bean 
 * du domaine ProdType.
 * Classe créée le 28/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ProdTypeDaoTest extends AbstractDaoTest {
	
	private ProdTypeDao prodTypeDao;
	private PlateformeDao plateformeDao;
	
	public ProdTypeDaoTest() {
		
	}

	public void setProdTypeDao(ProdTypeDao pDao) {
		this.prodTypeDao = pDao;
	}
	
	public void setPlateformeDao(PlateformeDao pfDao) {
		this.plateformeDao = pfDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ProdType> types = prodTypeDao.findAll();
		assertTrue(types.size() == 3);
	}
	
	public void testFindByOrder() {
		Plateforme pf = plateformeDao.findById(1);
		List< ? extends TKThesaurusObject> list = 
							prodTypeDao.findByOrder(pf);
		assertTrue(list.size() == 3);
		assertTrue(list.get(0).getNom().equals("ADN"));
		pf = plateformeDao.findById(2);
		list = prodTypeDao.findByOrder(pf);
		assertTrue(list.size() == 0);
		list = prodTypeDao.findByOrder(null);
		assertTrue(list.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByType().
	 */
	public void testFindByType() {
		List<ProdType> types = prodTypeDao.findByType("ADN");
		assertTrue(types.size() == 1);
		types = prodTypeDao.findByType("RNA");
		assertTrue(types.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByProdDeriveId().
	 */
	public void testFindByProdDeriveId() {
		List<ProdType> types = prodTypeDao.findByProdDeriveId(1);
		assertTrue(types.size() == 1);
		assertTrue(types.get(0).getProdTypeId() == 1);
		types = prodTypeDao.findByProdDeriveId(3);
		assertTrue(types.size() == 1);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<ProdType> liste = prodTypeDao.findByExcludedId(1);
		assertTrue(liste.size() == 2);
		ProdType type = liste.get(0);
		assertNotNull(type);
		assertTrue(type.getProdTypeId() == 2);
		
		liste = prodTypeDao.findByExcludedId(15);
		assertTrue(liste.size() == 3);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un ProdType.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrudProdQualite() throws Exception {
		
		ProdType p = new ProdType();
		String updatedType = "MAJ";
		p.setPlateforme(plateformeDao.findById(1));
		p.setType("TYPE");
		// Test de l'insertion
		prodTypeDao.createObject(p);
		assertEquals(new Integer(4), p.getProdTypeId());
		
		// Test de la mise à jour
		ProdType p2 = prodTypeDao.findById(new Integer(4));
		assertNotNull(p2);
		assertTrue(p2.getType().equals("TYPE"));
		p2.setType(updatedType);
		prodTypeDao.updateObject(p2);
		assertTrue(prodTypeDao.findById(new Integer(4)).
				getType().equals(updatedType));
		
		// Test de la délétion
		prodTypeDao.removeObject(new Integer(4));
		assertNull(prodTypeDao.findById(new Integer(4)));
		
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String type = "Type";
		String type2 = "Type2";
		ProdType p1 = new ProdType();
		p1.setType(type);
		ProdType p2 = new ProdType();
		p2.setType(type);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(p1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(p1.equals(p1));
		// 2 objets sont égaux entre eux
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		
		// Vérification de la différenciation de 2 objets
		p2.setType(type2);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		
		p2.setType(null);
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		
		p1.setType(null);
		assertTrue(p1.equals(p2));
		p2.setType(type);
		assertFalse(p1.equals(p2));
		
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		p1.setType(p2.getType());
		p1.setPlateforme(pf1);
		p2.setPlateforme(pf1);
		assertTrue(p1.equals(p2));
		p2.setPlateforme(pf2);
		assertFalse(p1.equals(p2));
		
		Categorie c = new Categorie();
		assertFalse(p1.equals(c));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String type = "Type";
		ProdType p1 = new ProdType();
		p1.setType(type);
		ProdType p2 = new ProdType();
		p2.setType(type);
		ProdType p3 = new ProdType();
		p3.setType(null);
		assertTrue(p3.hashCode() > 0);
		
		Plateforme pf1 = plateformeDao.findById(1);
		Plateforme pf2 = plateformeDao.findById(2);
		p1.setPlateforme(pf1);
		p2.setPlateforme(pf1);
		p3.setPlateforme(pf2);
		
		int hash = p1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(p1.hashCode() == p2.hashCode());
		assertFalse(p1.hashCode() == p3.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		assertTrue(hash == p1.hashCode());
		
	}

	/**
	 * test toString().
	 */
	public void testToString() {
		ProdType p1 = prodTypeDao.findById(1);
		assertTrue(p1.toString().equals("{" + p1.getType() + "}"));
		
		ProdType p2 = new ProdType();
		assertTrue(p2.toString().equals("{Empty ProdType}"));
	}
}
