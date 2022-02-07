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
package fr.aphp.tumorotek.manager.test.io.imports;

import static org.junit.Assert.*;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le manager ImportTemplateManager.
 * Classe créée le 25/01/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ImportTemplateManagerTest extends AbstractManagerTest4
{

	@Autowired
	private ImportTemplateManager importTemplateManager;
	@Autowired
	private BanqueDao banqueDao;
	@Autowired
	private EntiteDao entiteDao;
	@Autowired
	private ImportColonneManager importColonneManager;
	@Autowired
	private ChampEntiteDao champEntiteDao;
	@Autowired
	private ChampManager champManager;

	public ImportTemplateManagerTest(){

	}

	/**
	 * Test la méthode findById.
	 */
	@Test
	public void testFindById(){
		final ImportTemplate temp = importTemplateManager.findByIdManager(1);
		assertNotNull(temp);

		final ImportTemplate tempNull = importTemplateManager.findByIdManager(10);
		assertNull(tempNull);
	}

	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll(){
		final List<ImportTemplate> list = importTemplateManager.findAllObjectsManager();
		assertEquals(4, list.size());
	}

	/**
	 * Test la méthode findByBanqueManager.
	 */
	@Test
	public void testFindByBanqueManager(){
		final Banque b1 = banqueDao.findById(1).orElse(null);
		final Banque b3 = banqueDao.findById(3).orElse(null);

		List<ImportTemplate> list = importTemplateManager.findByBanqueManager(b1);
		assertEquals(3, list.size());

		list = importTemplateManager.findByBanqueManager(b3);
		assertTrue(list.size() == 0);

		list = importTemplateManager.findByBanqueManager(null);
		assertTrue(list.size() == 0);
	}

	/**
	 * Test de la méthode getEntiteManager().
	 */
	@Test
	public void testGetEntiteManager(){
		final Entite e = entiteDao.findById(2).orElse(null);

		final ImportTemplate it1 = importTemplateManager.findByIdManager(1);
		Set<Entite> liste = importTemplateManager.getEntiteManager(it1);
		assertTrue(liste.size() == 5);
		assertTrue(liste.contains(e));

		final ImportTemplate it3 = importTemplateManager.findByIdManager(3);
		liste = importTemplateManager.getEntiteManager(it3);
		assertTrue(liste.size() == 1);
		assertFalse(liste.contains(e));

		liste = importTemplateManager.getEntiteManager(new ImportTemplate());
		assertTrue(liste.size() == 0);

		liste = importTemplateManager.getEntiteManager(null);
		assertTrue(liste.size() == 0);
	}

	/**
	 * Test la méthode findDoublon.
	 */
	@Test
	public void testFindDoublon(){

		final Banque b1 = banqueDao.findById(1).orElse(null);
		final Banque b3 = banqueDao.findById(3).orElse(null);
		final ImportTemplate it1 = new ImportTemplate();
		it1.setNom("IMPORT");
		assertFalse(importTemplateManager.findDoublonManager(it1));
		it1.setBanque(b1);
		assertFalse(importTemplateManager.findDoublonManager(it1));

		it1.setNom("IMPORT AUTO");
		assertTrue(importTemplateManager.findDoublonManager(it1));
		it1.setBanque(b3);
		assertFalse(importTemplateManager.findDoublonManager(it1));

		final ImportTemplate it2 = importTemplateManager.findByIdManager(1);
		assertFalse(importTemplateManager.findDoublonManager(it2));

		it2.setNom("IMPORT AUTO");
		assertTrue(importTemplateManager.findDoublonManager(it2));

		assertFalse(importTemplateManager.findDoublonManager(null));
	}

	@Test
	public void testCrud() throws ParseException{
		createObjectManager();
		updateObjectManager();
	}

	/**
	 * Teste la methode createObjectManager. 
	 * @throws ParseException 
	 */
	public void createObjectManager() throws ParseException{

		final ImportTemplate it1 = new ImportTemplate();
		final Banque b1 = banqueDao.findById(1).orElse(null);
		final Entite e1 = entiteDao.findById(1).orElse(null);
		final Entite e2 = entiteDao.findById(2).orElse(null);
		final ImportColonne ic1 = importColonneManager.findByIdManager(1);
		List<ImportColonne> colonnes = new ArrayList<>();

		final int tots = importTemplateManager.findAllObjectsManager().size();
		final int cTots = importColonneManager.findAllObjectsManager().size();
		final int aTots = champManager.findAllObjectsManager().size();

		Boolean catched = false;
		// on test l'insertion avec la banque null
		try{
			importTemplateManager.createObjectManager(it1, null, null, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);

		// on test l'insertion d'un doublon
		it1.setNom("IMPORT AUTO");
		try{
			importTemplateManager.createObjectManager(it1, b1, null, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("DoublonFoundException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);

		// doublon sur une colonne
		colonnes.add(ic1);
		colonnes.add(ic1);
		try{
			importTemplateManager.createObjectManager(it1, b1, null, colonnes);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("DoublonFoundException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);

		// validation d'une colonne
		colonnes = new ArrayList<>();
		final ImportColonne ic2 = new ImportColonne();
		ic2.setNom("COL");
		colonnes.add(ic2);
		try{
			importTemplateManager.createObjectManager(it1, b1, null, colonnes);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);

		// Test de la validation lors de la création
		it1.setNom("Template de test");
		try{
			validationInsert(it1);
		}catch(final ParseException e){
			e.printStackTrace();
		}
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);

		// insertion valide avec les assos à null
		it1.setNom("Template de test");
		it1.setDescription("DESC");
		it1.setIsEditable(true);
		importTemplateManager.createObjectManager(it1, b1, null, null);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);
		final Integer idT1 = it1.getImportTemplateId();

		// Vérification
		final ImportTemplate itTest = importTemplateManager.findByIdManager(idT1);
		assertNotNull(itTest);
		assertTrue(itTest.getNom().equals("Template de test"));
		assertTrue(itTest.getDescription().equals("DESC"));
		assertTrue(itTest.getIsEditable());
		assertNotNull(itTest.getBanque());

		// insertion valide avec les assos
		final ImportTemplate it2 = new ImportTemplate();
		it2.setNom("Autre template");
		final List<Entite> entites = new ArrayList<>();
		entites.add(e1);
		entites.add(e2);

		final Champ ch1 = new Champ();
		ch1.setChampEntite(champEntiteDao.findById(1)).orElse(null);
		final Champ ch2 = new Champ();
		ch2.setChampEntite(champEntiteDao.findById(2)).orElse(null);
		ic2.setOrdre(1);
		ic2.setChamp(ch1);
		ic2.setImportTemplate(it2);
		final ImportColonne ic3 = new ImportColonne();
		ic3.setNom("COL2");
		ic3.setOrdre(2);
		ic3.setChamp(ch2);
		ic3.setImportTemplate(it2);
		colonnes = new ArrayList<>();
		colonnes.add(ic2);
		colonnes.add(ic3);

		importTemplateManager.createObjectManager(it2, b1, entites, colonnes);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 2);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots + 2);
		assertTrue(champManager.findAllObjectsManager().size() == aTots + 2);
		final Integer idT2 = it2.getImportTemplateId();

		// Vérification
		final ImportTemplate itTest2 = importTemplateManager.findByIdManager(idT2);
		assertNotNull(itTest2);
		assertTrue(itTest2.getNom().equals("Autre template"));
		assertNull(itTest2.getDescription());
		assertNull(itTest2.getIsEditable());
		assertNotNull(itTest2.getBanque());
		assertTrue(importTemplateManager.getEntiteManager(itTest2).size() == 2);
		final List<ImportColonne> cols = importColonneManager.findByImportTemplateManager(itTest2);
		assertTrue(cols.size() == 2);
		assertTrue(cols.get(0).getNom().equals("COL"));
		assertTrue(cols.get(0).getChamp().equals(ch1));

		importTemplateManager.removeObjectManager(itTest);
		importTemplateManager.removeObjectManager(itTest2);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);
		assertTrue(champManager.findAllObjectsManager().size() == aTots);
	}

	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	public void updateObjectManager() throws ParseException{

		final ImportTemplate it = new ImportTemplate();
		final Banque b1 = banqueDao.findById(1).orElse(null);
		final Entite e1 = entiteDao.findById(1).orElse(null);
		final Entite e2 = entiteDao.findById(2).orElse(null);
		final Entite e3 = entiteDao.findById(3).orElse(null);

		final int tots = importTemplateManager.findAllObjectsManager().size();
		final int cTots = importColonneManager.findAllObjectsManager().size();
		final int aTots = champManager.findAllObjectsManager().size();

		List<ImportColonne> colonnes = new ArrayList<>();
		final Champ ch1 = new Champ();
		ch1.setChampEntite(champEntiteDao.findById(1)).orElse(null);
		final Champ ch2 = new Champ();
		ch2.setChampEntite(champEntiteDao.findById(2)).orElse(null);
		final Champ ch3 = new Champ();
		ch3.setChampEntite(champEntiteDao.findById(3)).orElse(null);
		final ImportColonne ic1 = new ImportColonne();
		ic1.setNom("COL1");
		ic1.setOrdre(1);
		ic1.setChamp(ch1);
		ic1.setImportTemplate(it);
		final ImportColonne ic2 = new ImportColonne();
		ic2.setNom("COL2");
		ic2.setOrdre(2);
		ic2.setChamp(ch2);
		ic2.setImportTemplate(it);
		final ImportColonne ic3 = new ImportColonne();
		ic3.setNom("COL3");
		ic3.setOrdre(2);
		ic3.setChamp(ch3);
		ic3.setImportTemplate(it);
		colonnes = new ArrayList<>();
		colonnes.add(ic1);
		colonnes.add(ic2);
		colonnes.add(ic3);

		it.setNom("TEST");
		importTemplateManager.createObjectManager(it, b1, null, colonnes);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots + 3);
		assertTrue(champManager.findAllObjectsManager().size() == aTots + 3);
		final Integer idT = it.getImportTemplateId();

		final ImportTemplate itUp = importTemplateManager.findByIdManager(idT);
		Boolean catched = false;
		// on test l'update avec la banque null
		try{
			importTemplateManager.updateObjectManager(itUp, null, null, null, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);

		// on test l'update d'un doublon
		itUp.setNom("IMPORT AUTO");
		try{
			importTemplateManager.updateObjectManager(itUp, b1, null, null, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("DoublonFoundException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);

		// doublon sur une colonne
		final ImportColonne icE = importColonneManager.findByIdManager(1);
		colonnes = new ArrayList<>();
		colonnes.add(icE);
		colonnes.add(icE);
		try{
			importTemplateManager.updateObjectManager(itUp, b1, null, colonnes, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("DoublonFoundException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots + 3);

		// validation d'une colonne
		colonnes = new ArrayList<>();
		final ImportColonne ic4 = new ImportColonne();
		ic4.setNom("COL4");
		colonnes.add(ic4);
		ic4.setImportTemplate(itUp);
		try{
			importTemplateManager.updateObjectManager(itUp, b1, null, colonnes, null);
		}catch(final Exception e){
			if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
				catched = true;
			}
		}
		assertTrue(catched);
		catched = false;
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots + 3);

		// Test de la validation lors de la création
		itUp.setNom("Template de test");
		try{
			validationUpdate(itUp);
		}catch(final ParseException e){
			e.printStackTrace();
		}
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);

		// update valide avec les assos à null
		itUp.setNom("Template de test");
		itUp.setDescription("DESC");
		itUp.setIsEditable(true);
		importTemplateManager.updateObjectManager(itUp, b1, null, null, null);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);

		// Vérification
		final ImportTemplate itTest = importTemplateManager.findByIdManager(idT);
		assertNotNull(itTest);
		assertTrue(itTest.getNom().equals("Template de test"));
		assertTrue(itTest.getDescription().equals("DESC"));
		assertTrue(itTest.getIsEditable());
		assertNotNull(itTest.getBanque());

		// update valide avec les assos
		List<Entite> entites = new ArrayList<>();
		entites.add(e1);
		entites.add(e2);
		importTemplateManager.updateObjectManager(itTest, b1, entites, null, null);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);

		// Vérification
		final ImportTemplate itTest2 = importTemplateManager.findByIdManager(idT);
		assertNotNull(itTest2);
		assertTrue(importTemplateManager.getEntiteManager(itTest2).size() == 2);

		// update valide avec d'autres assos
		entites = new ArrayList<>();
		entites.add(e1);
		entites.add(e3);
		colonnes = new ArrayList<>();
		ic2.setNom("TT2");
		ic2.getChamp().setChampEntite(champEntiteDao.findById(4)).orElse(null);
		ic2.setImportTemplate(itTest2);
		final Champ ch5 = new Champ();
		ch5.setChampEntite(champEntiteDao.findById(5)).orElse(null);
		ic4.setNom("COL4");
		ic4.setOrdre(4);
		ic4.setChamp(ch5);
		ic4.setImportTemplate(itTest2);
		colonnes.add(ic1);
		colonnes.add(ic2);
		colonnes.add(ic4);
		final List<ImportColonne> colsToRemove = new ArrayList<>();
		colsToRemove.add(ic3);

		itTest2.setNom("TEST");
		importTemplateManager.updateObjectManager(itTest2, b1, entites, colonnes, colsToRemove);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots + 1);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots + 3);
		assertTrue(champManager.findAllObjectsManager().size() == aTots + 3);

		// Vérification
		final ImportTemplate itTest3 = importTemplateManager.findByIdManager(idT);
		assertNotNull(itTest3);
		assertTrue(importTemplateManager.getEntiteManager(itTest3).size() == 2);
		assertTrue(importTemplateManager.getEntiteManager(itTest3).contains(e3));
		assertFalse(importTemplateManager.getEntiteManager(itTest3).contains(e2));

		final List<ImportColonne> cols = importColonneManager.findByImportTemplateManager(itTest3);
		assertTrue(cols.size() == 3);
		assertTrue(cols.get(0).getNom().equals("COL1"));
		assertTrue(cols.get(0).getChamp().equals(ch1));
		assertTrue(cols.get(1).getNom().equals("TT2"));
		assertTrue(cols.get(1).getChamp().equals(ch2));
		assertTrue(cols.get(1).getChamp().getChampEntite().equals(champEntiteDao.findById(4))).orElse(null);
		assertTrue(cols.get(2).getNom().equals("COL4"));
		assertTrue(cols.get(2).getChamp().equals(ch5));

		importTemplateManager.removeObjectManager(itTest3);
		assertTrue(importTemplateManager.findAllObjectsManager().size() == tots);
		assertTrue(importColonneManager.findAllObjectsManager().size() == cTots);
		assertTrue(champManager.findAllObjectsManager().size() == aTots);
	}

	/**
	 * Test la validation d'un template lors de sa création.
	 * @param template à tester.
	 * @throws ParseException 
	 */
	public void validationInsert(final ImportTemplate template) throws ParseException{

		final Banque b1 = banqueDao.findById(1).orElse(null);
		boolean catchedInsert = false;

		// On teste une insertion avec un attribut nom non valide
		String[] emptyValues = new String[] {null, "", "  ", "%¢¢kjs", createOverLength(50)};
		for(int i = 0; i < emptyValues.length; i++){
			catchedInsert = false;
			try{
				template.setNom(emptyValues[i]);
				importTemplateManager.createObjectManager(template, b1, null, null);
			}catch(final Exception e){
				if(e.getClass().getSimpleName().equals("ValidationException")){
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		template.setNom("TEST");

		// On teste une insertion avec un attribut description non valide
		emptyValues = new String[] {"", "  ", createOverLength(250)};
		for(int i = 0; i < emptyValues.length; i++){
			catchedInsert = false;
			try{
				template.setDescription(emptyValues[i]);
				importTemplateManager.createObjectManager(template, b1, null, null);
			}catch(final Exception e){
				if(e.getClass().getSimpleName().equals("ValidationException")){
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		template.setDescription("TEST");
	}

	/**
	 * Test la validation d'un template lors de sa modif.
	 * @param template à tester.
	 * @throws ParseException 
	 */
	public void validationUpdate(final ImportTemplate template) throws ParseException{

		final Banque b1 = banqueDao.findById(1).orElse(null);
		boolean catchedInsert = false;

		// On teste un update avec un attribut nom non valide
		String[] emptyValues = new String[] {null, "", "  ", "%¢¢kjs", createOverLength(50)};
		for(int i = 0; i < emptyValues.length; i++){
			catchedInsert = false;
			try{
				template.setNom(emptyValues[i]);
				importTemplateManager.updateObjectManager(template, b1, null, null, null);
			}catch(final Exception e){
				if(e.getClass().getSimpleName().equals("ValidationException")){
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		template.setNom("TEST");

		// On teste un update avec un attribut description non valide
		emptyValues = new String[] {"", "  ", createOverLength(250)};
		for(int i = 0; i < emptyValues.length; i++){
			catchedInsert = false;
			try{
				template.setDescription(emptyValues[i]);
				importTemplateManager.updateObjectManager(template, b1, null, null, null);
			}catch(final Exception e){
				if(e.getClass().getSimpleName().equals("ValidationException")){
					catchedInsert = true;
				}
			}
			assertTrue(catchedInsert);
		}
		template.setDescription("TEST");
	}

}
