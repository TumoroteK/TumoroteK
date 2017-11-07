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
package fr.aphp.tumorotek.dao.test.io.imports;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.imports.ImportColonneDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * 
 * Classe de test pour le DAO ImportColonneDao et le bean 
 * du domaine ImportColonne.
 * Classe créée le 24/01/2011.
 * 
 * @author Pierre VENTADOUR
 * @version 2.0.13.2
 *
 */
public class ImportColonneDaoTest extends AbstractDaoTest {
	
	private ImportColonneDao importColonneDao;
	private ImportTemplateDao importTemplateDao;
	private ChampDao champDao;
	private EntiteDao entiteDao;
	private DataTypeDao dataTypeDao;
	
	public ImportColonneDaoTest() {
		
	}

	public void setImportColonneDao(ImportColonneDao i) {
		this.importColonneDao = i;
	}
	
	public void setImportTemplateDao(ImportTemplateDao iDao) {
		this.importTemplateDao = iDao;
	}

	public void setChampDao(ChampDao cDao) {
		this.champDao = cDao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	public void setDataTypeDao(DataTypeDao dDao) {
		this.dataTypeDao = dDao;
	}


	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<ImportColonne> entites = importColonneDao.findAll();
		assertTrue(entites.size() == 103);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateWithOrder().
	 */
	public void testFindByTemplateWithOrder() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateWithOrder(it1);
		assertTrue(liste.size() == 31);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateWithOrder(it3);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateWithOrder(it2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateWithOrder(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateWithOrderSelectNom().
	 */
	public void testFindByTemplateWithOrderSelectNom() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		List<String> liste = 
			importColonneDao.findByTemplateWithOrderSelectNom(it1);
		assertTrue(liste.size() == 31);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateWithOrderSelectNom(it3);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateWithOrderSelectNom(it2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateWithOrderSelectNom(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateAndEntite().
	 */
	public void testFindByTemplateAndEntite() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		Entite e1 = entiteDao.findById(1);
		Entite e10 = entiteDao.findById(10);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateAndEntite(it1, e1);
		assertTrue(liste.size() == 6);
		liste = importColonneDao.findByTemplateAndEntite(it1, e10);
		assertTrue(liste.size() == 0);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateAndEntite(it3, e1);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateAndEntite(it2, e1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndEntite(null, e1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndEntite(it2, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndEntite(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateAndEntite().
	 */
	public void testFindByTemplateAndAnnotationEntite() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		Entite e2 = entiteDao.findById(2);
		Entite e10 = entiteDao.findById(10);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateAndAnnotationEntite(it1, e2);
		assertTrue(liste.size() == 5);
		liste = importColonneDao.findByTemplateAndAnnotationEntite(it1, e10);
		assertTrue(liste.size() == 0);
		
		Entite e1 = entiteDao.findById(1);
		liste = importColonneDao.findByTemplateAndAnnotationEntite(it1, e1);
		assertTrue(liste.size() == 0);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateAndAnnotationEntite(it2, e2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationEntite(null, e2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationEntite(it2, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationEntite(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateAndDataType().
	 */
	public void testFindByTemplateAndDataType() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		DataType d1 = dataTypeDao.findById(1);
		DataType d7 = dataTypeDao.findById(7);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateAndDataType(it1, d1);
		assertTrue(liste.size() == 12);
		liste = importColonneDao.findByTemplateAndDataType(it1, d7);
		assertTrue(liste.size() == 0);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateAndDataType(it3, d1);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateAndDataType(it2, d1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndDataType(null, d1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndDataType(it2, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndDataType(null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateAndAnnotationDatatype().
	 */
	public void testFindByTemplateAndAnnotationDatatype() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		DataType d1 = dataTypeDao.findById(1);
		DataType d7 = dataTypeDao.findById(7);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateAndAnnotationDatatype(it1, d1);
		assertTrue(liste.size() == 1);
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(it1, d7);
		assertTrue(liste.size() == 1);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(it3, d1);
		assertTrue(liste.size() == 0);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(it2, d1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(null, d1);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(it2, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndAnnotationDatatype(
				null, null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedIdWithTemplate().
	 */
	public void testFindByExcludedIdWithTemplate() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		List<ImportColonne> liste = 
			importColonneDao.findByExcludedIdWithTemplate(1, it1);
		assertTrue(liste.size() == 30);
		liste = importColonneDao.findByExcludedIdWithTemplate(500, it1);
		assertTrue(liste.size() == 31);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByExcludedIdWithTemplate(12, it3);
		assertTrue(liste.size() == 2);
		liste = importColonneDao.findByExcludedIdWithTemplate(1, it3);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByExcludedIdWithTemplate(1, it2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByExcludedIdWithTemplate(1, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByExcludedIdWithTemplate(null, it1);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedIdWithTemplateSelectNom().
	 */
	public void testFindByExcludedIdWithTemplateSelectNom() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		List<String> liste = 
			importColonneDao.findByExcludedIdWithTemplateSelectNom(1, it1);
		assertTrue(liste.size() == 30);
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(
				500, it1);
		assertTrue(liste.size() == 31);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(12, it3);
		assertTrue(liste.size() == 2);
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(1, it3);
		assertTrue(liste.size() == 3);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(1, it2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(1, null);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByExcludedIdWithTemplateSelectNom(
				null, it1);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplateAndThesaurus().
	 */
	public void testFindByTemplateAndThesaurus() {
		ImportTemplate it1 = importTemplateDao.findById(1);
		List<ImportColonne> liste = 
			importColonneDao.findByTemplateAndThesaurus(it1);
		assertTrue(liste.size() == 8);
		
		ImportTemplate it3 = importTemplateDao.findById(3);
		liste = importColonneDao.findByTemplateAndThesaurus(it3);
		assertTrue(liste.size() == 0);
		
		ImportTemplate it2 = importTemplateDao.findById(2);
		liste = importColonneDao.findByTemplateAndThesaurus(it2);
		assertTrue(liste.size() == 0);
		
		liste = importColonneDao.findByTemplateAndThesaurus(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'un ImportColonne.
	 * @throws Exception lance une exception en cas d'erreur.
	 */
	@Rollback(false)
	public void testCrud() throws Exception {
				
		ImportTemplate it = importTemplateDao.findById(2);
		ImportColonne ic = new ImportColonne();
		String nom = "Colonne";
		String nomUp = "UP";
		Champ c = champDao.findById(1);
		
		ic.setImportTemplate(it);
		ic.setNom(nom);
		ic.setChamp(c);
		ic.setOrdre(1);
		
		// Test de l'insertion
		importColonneDao.createObject(ic);
		assertNotNull(ic.getImportColonneId());
		
		Integer iId = ic.getImportColonneId();
		
		// Test de la mise à jour
		ImportColonne ic2 = importColonneDao.findById(iId);
		assertNotNull(ic2);
		assertNotNull(ic2.getImportTemplate());
		assertNotNull(ic2.getChamp());
		assertTrue(ic2.getNom().equals(nom));
		assertTrue(ic2.getOrdre() == 1);

		ic2.setNom(nomUp);
		importColonneDao.updateObject(ic2);
		assertTrue(importColonneDao.findById(iId).
				getNom().equals(nomUp));
		
		// Test de la délétion
		importColonneDao.removeObject(iId);
		assertNull(importColonneDao.findById(iId));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		String nom = "nom";
		String nom2 = "nom2";
		ImportTemplate it1 = importTemplateDao.findById(1);
		ImportTemplate it2 = importTemplateDao.findById(2);
		Champ ch1 = champDao.findById(1);
		Champ ch2 = champDao.findById(2);
		ImportColonne ic1 = new ImportColonne();
		ImportColonne ic2 = new ImportColonne();
		ic1.setNom(nom);
		ic1.setImportTemplate(it1);
		ic2.setNom(nom);
		ic2.setImportTemplate(it1);
		
		// L'objet 1 n'est pas égal à null
		assertFalse(ic1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(ic1.equals(ic1));
		// 2 objets sont égaux entre eux
		assertTrue(ic1.equals(ic2));
		assertTrue(ic2.equals(ic1));
		
		ic1.setImportTemplate(null);
		ic1.setNom(null);
		ic2.setImportTemplate(null);
		ic2.setNom(null);
		ic1.setChamp(null);
		ic2.setChamp(null);
		assertTrue(ic1.equals(ic2));
		ic2.setNom(nom);
		assertFalse(ic1.equals(ic2));
		ic1.setNom(nom);
		assertTrue(ic1.equals(ic2));
		ic2.setNom(nom2);
		assertFalse(ic1.equals(ic2));
		ic2.setNom(null);
		assertFalse(ic1.equals(ic2));
		ic2.setImportTemplate(it1);
		assertFalse(ic1.equals(ic2));
		
		ic1.setImportTemplate(it1);
		ic1.setNom(null);
		ic2.setNom(null);
		ic2.setImportTemplate(it1);
		assertTrue(ic1.equals(ic2));
		ic2.setImportTemplate(it2);
		assertFalse(ic1.equals(ic2));
		ic2.setNom(nom);
		assertFalse(ic1.equals(ic2));
		ic1.setNom(nom);
		ic2.setImportTemplate(it1);
		assertTrue(ic1.equals(ic2));
		
		ic2.setChamp(ch1);
		assertFalse(ic1.equals(ic2));
		ic1.setChamp(ch1);
		assertTrue(ic1.equals(ic2));
		ic2.setChamp(ch2);
		assertFalse(ic1.equals(ic2));
		ic2.setChamp(null);
		assertFalse(ic1.equals(ic2));
		
		// Vérification de la différenciation de 2 objets
		ic1.setNom(nom);
		assertFalse(ic1.equals(ic2));
		ic2.setNom(nom2);
		ic2.setImportTemplate(it1);
		assertFalse(ic1.equals(ic2));
		

		Categorie c3 = new Categorie();
		assertFalse(ic1.equals(c3));
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		String nom = "nom";
		String nom2 = "nom2";
		ImportTemplate it1 = importTemplateDao.findById(1);
		ImportTemplate it2 = importTemplateDao.findById(2);
		Champ ch1 = champDao.findById(1);
		Champ ch2 = champDao.findById(2);
		ImportColonne ic1 = new ImportColonne();
		ImportColonne ic2 = new ImportColonne();
		//null
		assertTrue(ic1.hashCode() == ic2.hashCode());
		
		//Nom
		ic2.setNom(nom);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setNom(nom2);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setNom(nom);
		assertTrue(ic1.hashCode() == ic2.hashCode());
		
		//Template
		ic2.setImportTemplate(it1);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setImportTemplate(it2);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setImportTemplate(it1);
		assertTrue(ic1.hashCode() == ic2.hashCode());
		
		//Champ
		ic2.setChamp(ch1);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setChamp(ch2);
		assertFalse(ic1.hashCode() == ic2.hashCode());
		ic1.setChamp(ch1);
		assertTrue(ic1.hashCode() == ic2.hashCode());
		
		// un même objet garde le même hashcode dans le temps
		int hash = ic1.hashCode();
		assertTrue(hash == ic1.hashCode());
		assertTrue(hash == ic1.hashCode());
		assertTrue(hash == ic1.hashCode());
		assertTrue(hash == ic1.hashCode());
		
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		ImportColonne ic1 = importColonneDao.findById(1);
		assertTrue(ic1.toString().
				equals("{" + ic1.getNom() + ", " 
						+ ic1.getImportTemplate().getNom() 
						+ "(ImportTemplate)}"));
		
		ImportColonne ic2 = new ImportColonne();
		assertTrue(ic2.toString().equals("{Empty ImportColonne}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		ImportColonne ic1 = importColonneDao.findById(1);
		ImportColonne ic2 = new ImportColonne();
		ic2 = ic1.clone();
		
		assertTrue(ic1.equals(ic2));
		
		if (ic1.getImportColonneId() != null) {
			assertTrue(ic1.getImportColonneId() == ic2.getImportColonneId());
		} else {
			assertNull(ic2.getImportColonneId());
		}
		
		if (ic1.getImportTemplate() != null) {
			assertTrue(ic1.getImportTemplate().equals(ic2.getImportTemplate()));
		} else {
			assertNull(ic2.getImportTemplate());
		}
		
		if (ic1.getNom() != null) {
			assertTrue(ic1.getNom().equals(ic2.getNom()));
		} else {
			assertNull(ic2.getNom());
		}
		
		if (ic1.getChamp() != null) {
			assertTrue(ic1.getChamp().equals(ic2.getChamp()));
		} else {
			assertNull(ic2.getChamp());
		}
		
		if (ic1.getOrdre() != null) {
			assertTrue(ic1.getOrdre() == ic2.getOrdre());
		} else {
			assertNull(ic2.getOrdre());
		}
	}

}
