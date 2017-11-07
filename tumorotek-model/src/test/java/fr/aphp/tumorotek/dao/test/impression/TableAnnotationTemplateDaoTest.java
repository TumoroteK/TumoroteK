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

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.impression.TableAnnotationTemplateDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

/**
 * 
 * Classe de test pour le DAO TableAnnotationTemplateDao et le bean 
 * du domaine ChampEntiteBloc.
 * 
 * @author Pierre Ventadour.
 * @version 30/07/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class TableAnnotationTemplateDaoTest extends AbstractDaoTest {
	
	/** Bean Dao. */
	private TableAnnotationTemplateDao tableAnnotationTemplateDao;
	/** Bean Dao. */
	private TableAnnotationDao tableAnnotationDao;
	/** Bean Dao. */
	private TemplateDao templateDao;
	
	public TableAnnotationTemplateDaoTest() {
		
	}

	public void setTableAnnotationTemplateDao(
			TableAnnotationTemplateDao tDao) {
		this.tableAnnotationTemplateDao = tDao;
	}

	public void setTableAnnotationDao(TableAnnotationDao tDao) {
		this.tableAnnotationDao = tDao;
	}

	public void setTemplateDao(TemplateDao tDao) {
		this.templateDao = tDao;
	}
	
	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAll() {
		List<TableAnnotationTemplate> liste = 
			tableAnnotationTemplateDao.findAll();
		assertTrue(liste.size() == 1);
	}
	
	/**
	 * Test l'appel de la méthode findById().
	 */
	public void testFindById() {
		TableAnnotation ta1 = tableAnnotationDao.findById(2);
		Template t1 = templateDao.findById(1);
		TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);
		
		TableAnnotationTemplate tat = tableAnnotationTemplateDao.findById(pk);
		assertNotNull(tat);
		
		Template t2 = templateDao.findById(2);
		pk = new TableAnnotationTemplatePK(t2, ta1);
		tat = tableAnnotationTemplateDao.findById(pk);
		assertNull(tat);
	}
	
	/**
	 * Test l'appel de la méthode findByExcludedPK().
	 */
	public void testFindByExcludedPK() {
		TableAnnotation ta1 = tableAnnotationDao.findById(1);
		Template t1 = templateDao.findById(1);
		TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);
		
		List<TableAnnotationTemplate> liste = 
			tableAnnotationTemplateDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 1);
		
		TableAnnotation ta2 = tableAnnotationDao.findById(2);
		pk = new TableAnnotationTemplatePK(t1, ta2);
		liste = tableAnnotationTemplateDao.findByExcludedPK(pk);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByTemplate().
	 */
	public void testFindByTemplate() {
		Template t1 = templateDao.findById(1);
		List<TableAnnotationTemplate> liste = 
			tableAnnotationTemplateDao.findByTemplate(t1);
		assertTrue(liste.size() == 1);
		
		Template t2 = templateDao.findById(2);
		liste = tableAnnotationTemplateDao.findByTemplate(t2);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'insertion, la mise à jour et la suppression 
	 * d'un BlocImpressionTemplate.
	 **/
	@Rollback(false)
	public void testCrud() {
		
		TableAnnotationTemplate tat = new TableAnnotationTemplate();
		TableAnnotation ta1 = tableAnnotationDao.findById(1);
		Template t1 = templateDao.findById(1);
		Integer ordre = 5;
		Integer ordreUp = 6;
		
		tat.setTableAnnotation(ta1);
		tat.setTemplate(t1);
		tat.setOrdre(ordre);
		
		// Test de l'insertion
		tableAnnotationTemplateDao.createObject(tat);
		assertTrue(tableAnnotationTemplateDao.findAll().size() == 2);
		
		// Test de la mise à jour
		TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK();
		pk.setTableAnnotation(ta1);
		pk.setTemplate(t1);
		TableAnnotationTemplate tat2 = tableAnnotationTemplateDao.findById(pk);
		assertNotNull(tat2);
		assertTrue(tat2.getTemplate().equals(t1));
		assertTrue(tat2.getTableAnnotation().equals(ta1));
		assertTrue(tat2.getOrdre() == ordre);
		
		//update
		tat2.setOrdre(ordreUp);
		tableAnnotationTemplateDao.updateObject(tat2);
		assertTrue(tableAnnotationTemplateDao.findById(pk).equals(tat2));
		assertTrue(tableAnnotationTemplateDao.findById(pk)
				.getOrdre() == ordreUp);
		assertTrue(tableAnnotationTemplateDao.findAll().size() == 2);
		
		// Test de la délétion
		tableAnnotationTemplateDao.removeObject(pk);
		assertNull(tableAnnotationTemplateDao.findById(pk));
		assertTrue(tableAnnotationTemplateDao.findAll().size() == 1);
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException {

		TableAnnotationTemplate tat1 = new TableAnnotationTemplate();
		TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
		
		// L'objet 1 n'est pas égal à null
		assertFalse(tat1.equals(null));
		// L'objet 1 est égale à lui même
		assertTrue(tat1.equals(tat1));
		// 2 objets sont égaux entre eux
		assertTrue(tat1.equals(tat2));
		assertTrue(tat2.equals(tat1));
	
		populateClefsToTestEqualsAndHashCode(tat1, tat2);
			
		//dummy test
		Banque b = new Banque();
		assertFalse(tat1.equals(b));		
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 * @throws ParseException 
	 */
	public void testHashCode() throws ParseException {
		TableAnnotationTemplate tat1 = new TableAnnotationTemplate();
		TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
		TableAnnotationTemplate tat3 = new TableAnnotationTemplate();
		
		assertTrue(tat1.hashCode() == tat2.hashCode());
		assertTrue(tat2.hashCode() == tat3.hashCode());
		assertTrue(tat3.hashCode() > 0);
		
		//teste dans methode precedente
		populateClefsToTestEqualsAndHashCode(tat1, tat2);
		
		int hash = tat1.hashCode();
		// 2 objets égaux ont le même hashcode
		assertTrue(tat1.hashCode() == tat2.hashCode());
		// un même objet garde le même hashcode dans le temps
		assertTrue(hash == tat1.hashCode());
		assertTrue(hash == tat1.hashCode());
		assertTrue(hash == tat1.hashCode());
		assertTrue(hash == tat1.hashCode());
	}
	
	private void populateClefsToTestEqualsAndHashCode(
			TableAnnotationTemplate tat1, 
			TableAnnotationTemplate tat2) throws ParseException {
		
		TableAnnotation ta1 = tableAnnotationDao.findById(1);
		TableAnnotation ta2 = tableAnnotationDao.findById(2);
		Template t1 = templateDao.findById(1);
		TableAnnotationTemplatePK pk1 = new 
			TableAnnotationTemplatePK(t1, ta1);
		TableAnnotationTemplatePK pk2 = new 
			TableAnnotationTemplatePK(t1, ta2);
		TableAnnotationTemplatePK pk3 = new 
			TableAnnotationTemplatePK(t1, ta1);
		TableAnnotationTemplatePK[] pks = new 
			TableAnnotationTemplatePK[]{null, pk1, pk2, pk3};
				
		for (int i = 0; i < pks.length; i++) {
			for (int k = 0; k < pks.length; k++) {
								
				tat1.setPk(pks[i]);				
				tat2.setPk(pks[k]);
				
				if (((i == k) || (i + k == 4))) {
						assertTrue(tat1.equals(tat2));
						assertTrue(tat1.hashCode() 
								== tat2.hashCode());
				} else {
					assertFalse(tat1.equals(tat2));
				}
			}
		}
	}
	
	/**
	 * Test la méthode toString.
	 */
	public void testToString() {
		TableAnnotation ta1 = tableAnnotationDao.findById(2);
		Template t1 = templateDao.findById(1);
		TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);
		TableAnnotationTemplate tat1 = tableAnnotationTemplateDao.findById(pk);
		
		assertTrue(tat1.toString().
				equals("{" + tat1.getPk().toString() + "}"));
		
		TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
		tat2.setPk(null);
		assertTrue(tat2.toString().equals("{Empty TableAnnotationTemplate}"));
	}
	
	/**
	 * Test la méthode clone.
	 */
	public void testClone() {
		TableAnnotation ta1 = tableAnnotationDao.findById(2);
		Template t1 = templateDao.findById(1);
		TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(t1, ta1);
		TableAnnotationTemplate tat1 = tableAnnotationTemplateDao.findById(pk);
		
		TableAnnotationTemplate tat2 = new TableAnnotationTemplate();
		tat2 = tat1.clone();
		
		assertTrue(tat1.equals(tat2));
				
		if (tat1.getPk() != null) {
			assertTrue(tat1.getPk().equals(tat2.getPk()));
		} else {
			assertNull(tat2.getPk());
		}
		
		if (tat1.getOrdre() != null) {
			assertTrue(tat1.getOrdre() == tat2.getOrdre());
		} else {
			assertNull(tat2.getOrdre());
		}
	}

}
