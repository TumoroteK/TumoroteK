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
package fr.aphp.tumorotek.manager.test.coeur.annotation;

import fr.aphp.tumorotek.dao.annotation.AnnotationDefautDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.ChampAnnotationValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.ItemValidator;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.TKFantomableObject;
import fr.aphp.tumorotek.model.coeur.annotation.*;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * Classe de test pour le manager ChampAnnotationManager.
 * Classe créée le 05/02/10.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0
 *
 */
public class ChampAnnotationManagerTest extends AbstractManagerTest4 {
	
	/* Managers injectes par Spring*/
	@Autowired
	private ChampAnnotationManager champAnnotationManager;
	@Autowired
	private ChampAnnotationValidator champAnnotationValidator;
	@Autowired
	private TableAnnotationDao tableAnnotationDao;
	@Autowired
	private DataTypeDao dataTypeDao;
	@Autowired
	private UtilisateurDao utilisateurDao;
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private AnnotationDefautDao annotationDefautDao;
	@Autowired
	private ItemValidator itemValidator;
	@Autowired
	private BanqueDao banqueDao;
	@Autowired
	private OperationTypeDao operationTypeDao;

	public ChampAnnotationManagerTest() {		
	}

	
	@Test
	public void testFindAllObjectsManager() {
		List<ChampAnnotation> champs = champAnnotationManager
												.findAllObjectsManager();
		assertTrue(champs.size() == 48);
	}
	
	/**
	 * Test la methode findByNomLikeManager.
	 */
	@Test
	public void testFindByNomLikeManager() {
		//teste une recherche exactMatch
		List<ChampAnnotation> tables = 
			champAnnotationManager.findByNomLikeManager("Bool1", true);
		assertTrue(tables.size() == 1);
		//teste une recherche non exactMatch
		tables = champAnnotationManager.findByNomLikeManager("Num", false);
		assertTrue(tables.size() == 2);
		//teste une recherche infructueuse
		tables = champAnnotationManager.findByNomLikeManager("LUX", true);
		assertTrue(tables.size() == 0);
		//null recherche
		tables = champAnnotationManager.findByNomLikeManager(null, false);
		assertTrue(tables.size() == 0);
	}
	
	@Test
	public void testFindByTableManager() {
		TableAnnotation t = tableAnnotationDao.findById(3);
		List<ChampAnnotation> tables = 
					champAnnotationManager.findByTableManager(t);
		assertTrue(tables.size() == 5);
		t = tableAnnotationDao.findById(1);
		tables = champAnnotationManager.findByTableManager(t);
		assertTrue(tables.size() == 2);
		t = tableAnnotationDao.findById(6);
		tables = champAnnotationManager.findByTableManager(t);
		assertTrue(tables.size() == 2);
		tables = champAnnotationManager.findByTableManager(null);
		assertTrue(tables.size() == 0);
	}
	
	@Test
	public void testFindDoublon() {
		//Cree le doublon
		ChampAnnotation c1 = champAnnotationManager
							.findByNomLikeManager("Date1", true).get(0);
		ChampAnnotation c2 = new ChampAnnotation();
		c2.setNom(c1.getNom());
		c2.setTableAnnotation(c1.getTableAnnotation());
		assertTrue(c2.equals(c1));
		assertTrue(champAnnotationManager.findDoublonManager(c2));
	}
	
	@Test
	public void testGetAnnotationDefautsManager() {
		ChampAnnotation chp = champAnnotationManager
							.findByNomLikeManager("Texte1", true).get(0);
		assertTrue(champAnnotationManager
							.getAnnotationDefautsManager(chp).size() == 1);
		chp = new ChampAnnotation();
		assertTrue(champAnnotationManager
							.getAnnotationDefautsManager(chp).size() == 0);
	}
	
	@Test
	public void testGetAnnotationValeursManager() {
		ChampAnnotation chp = champAnnotationManager
							.findByNomLikeManager("Alphanum1", true).get(0);
		assertTrue(champAnnotationManager
							.getAnnotationValeursManager(chp).size() == 3);
		chp = new ChampAnnotation();
		assertTrue(champAnnotationManager
							.getAnnotationValeursManager(chp).size() == 0);
	}
	
	@Test
	public void testGetItemsManager() {
		ChampAnnotation chp = champAnnotationManager
							.findByNomLikeManager("Thes1", true).get(0);
		Banque b1 = banqueDao.findById(1);
		assertTrue(champAnnotationManager
								.getItemsManager(chp, b1).size() == 2);
		assertTrue(champAnnotationManager
								.getItemsManager(chp, null).size() == 0);
		Banque b4 = banqueDao.findById(4);
		assertTrue(champAnnotationManager
									.getItemsManager(chp, b4).size() == 1);
		chp = new ChampAnnotation();
		assertTrue(champAnnotationManager
								.getItemsManager(chp, null).size() == 0);
	}
	
	@Test
	public void testFindMaxItemLength() {
		ChampAnnotation chp = champAnnotationManager
								.findByNomLikeManager("Thes2", true).get(0);
		Set<Item> items = champAnnotationManager.getItemsManager(chp, null);
		assertTrue(champAnnotationManager.findMaxItemLength(items) == 11);
		items =  champAnnotationManager
			.getItemsManager(champAnnotationManager
							.findByNomLikeManager("Bool1", true).get(0), null);
		assertTrue(champAnnotationManager.findMaxItemLength(items) == 0);
	}
	
	@Test
	public void testCRUD() {
		createObjectManagerTest();
		updateDefautsObjectManagerTest();
		updateItemsObjectManagerTest();
		removeObjectManagerTest();
	}
	
	/**
	 * Teste la methode createObjectManager. 
	 * @throws ParseException 
	 */
	private void createObjectManagerTest() {
		
		ChampAnnotation chp = new ChampAnnotation();
		/*Champs obligatoires*/
		chp.setNom("CHAMP_TEST");
		DataType dt = dataTypeDao.findByType("thesaurus").get(0);
		TableAnnotation t = tableAnnotationDao.findById(5);
		//chp.setTableAnnotation(tableAnnotationDao.findById(3));
		chp.setCombine(true);
		chp.setOrdre(4);
		// references vers items
		Item i1 = new Item(); i1.setLabel("i1"); i1.setValeur("val1");
		i1.setChampAnnotation(chp);
		Item i2 = new Item(); i2.setLabel("i2"); i2.setValeur("val2");
		i2.setChampAnnotation(chp);
		Item i3 = new Item(); i3.setLabel("i3¢€"); i3.setValeur(null);
		i3.setChampAnnotation(chp);
		List<Item> its = new ArrayList<Item>();
		its.add(i1); its.add(i2); its.add(i3);
		// references vers defauts
		AnnotationDefaut d1 = new AnnotationDefaut(); 
		d1.setItem(i1); d1.setChampAnnotation(chp); d1.setObligatoire(false);
		AnnotationDefaut d2 = new AnnotationDefaut(); 
		d2.setItem(i2); d2.setChampAnnotation(chp); d2.setObligatoire(false);
		List<AnnotationDefaut> defauts = new ArrayList<AnnotationDefaut>();
		defauts.add(d1); defauts.add(d2); 
		Utilisateur u = utilisateurDao.findById(2);
				
		//insertion
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp, t, dt, its, defauts, u,
														null, null, null);
		} catch (NullPointerException ne) {
			assertTrue(ne.getMessage().equals("operation cannot be "
									+ "set to null for createorUpdateMethod"));
		}
		// validation table null
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp, null, null, null, null, u, 
														null, "creation", null);
		} catch (RequiredObjectIsNullException re) {
			assertTrue(re.getRequiredObject().equals("TableAnnotation"));
		}
		// validation dataType null
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp, t, null, null, null, u, 
													null, "creation", null);
		} catch (RequiredObjectIsNullException re) {
			assertTrue(re.getRequiredObject().equals("DataType"));
		}
		// validationException item 
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp, t, dt, its, defauts, u, 
													null, "creation", null);
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
					.getCode().equals("item.label.illegal"));
		}
		i3.setLabel("i3");
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp, t, dt, its, defauts, u, 
															null, "", null);
		} catch (IllegalArgumentException ie) {
			assertTrue(ie.getMessage().equals("Operation must match "
										+ "'creation/modification' values"));
		}
		champAnnotationManager
			.createOrUpdateObjectManager(chp, t, dt, its, defauts, u, 
													null, "creation", null);
		assertTrue((champAnnotationManager
					.findByNomLikeManager("CHAMP_TEST", true)).size() == 1);
		assertTrue((champAnnotationManager
									.getItemsManager(chp, null)).size() == 3);
		assertTrue(champAnnotationManager
						.getAnnotationDefautsManager(chp).size() == 2);
		assertTrue(getOperationManager().findByObjectManager(chp).size() == 1);
		assertTrue(getOperationManager().findByObjectManager(chp).get(0)
										.getOperationType().getNom()
														.equals("Creation"));
		
		//Insertion d'un doublon engendrant une exception
		ChampAnnotation c2 = new ChampAnnotation();
		c2.setNom("CHAMP_TEST");
		c2.setTableAnnotation(t);
		assertTrue(chp.equals(c2));
		Boolean catched = false;
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(c2, t, dt, null, null, u, null, 
															"creation", null);
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equals(
					"DoublonFoundException")) {
				catched = true;
			}
		}
		assertTrue(catched);
		
		assertTrue(champAnnotationManager
				.findByNomLikeManager("CHAMP_TEST", false).size() == 1);
		assertTrue(getOperationManager().findByObjectManager(chp).size() == 1);
		
		// teste la creation d'un champ alphanum avec un defaut
		ChampAnnotation chp2 = new ChampAnnotation();
		/*Champs obligatoires*/
		chp2.setNom("ALPHANUM_TEST");
		DataType dt2 = dataTypeDao.findByType("alphanum").get(0);
		chp2.setCombine(true);
		chp2.setOrdre(8);
		// references vers defauts
		AnnotationDefaut dalpha = new AnnotationDefaut(); 
		dalpha.setAlphanum("defaut¢¢€"); 
		dalpha.setChampAnnotation(chp2); dalpha.setObligatoire(false);
		defauts = new ArrayList<AnnotationDefaut>();
		defauts.add(dalpha); 
				
		// validationException item 
		try {
			champAnnotationManager
				.createOrUpdateObjectManager(chp2, t, dt2, null, defauts, u, 
														null, "creation", null);
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
					.getCode().equals("anno.alphanum.illegal"));
		}
		dalpha.setAlphanum("defaut Value"); 
		champAnnotationManager
			.createOrUpdateObjectManager(chp2, t, dt2, null, defauts, u, null,
														"creation", null);
		assertTrue((champAnnotationManager
				.findByNomLikeManager("ALPHANUM_TEST", true)).size() == 1);
		assertTrue((champAnnotationManager
								.getItemsManager(chp2, null)).size() == 0);
		assertTrue(champAnnotationManager
					.getAnnotationDefautsManager(chp2).size() == 1);
		assertTrue(champAnnotationManager
				.getAnnotationDefautsManager(chp2).iterator().next()
				.getAlphanum().equals("defaut Value"));

	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	private void updateDefautsObjectManagerTest() {
		ChampAnnotation c = champAnnotationManager
						.findByNomLikeManager("CHAMP_TEST", false).get(0);
		c.setNom("NEWCHAMP");
		c.setCombine(false);
		// references vers items
		List<Item> its = 
			new ArrayList<Item>(champAnnotationManager
											.getItemsManager(c, null));
		// references vers defauts
		List<AnnotationDefaut> defauts = 
					new ArrayList<AnnotationDefaut>(
						champAnnotationManager
							.getAnnotationDefautsManager(c));
		defauts.remove(0);
		assertFalse(defauts.iterator().next().getObligatoire());
		AnnotationDefaut def1 = defauts.get(0);
		def1.setObligatoire(true);
		AnnotationDefaut d2 = new AnnotationDefaut();
		d2.setItem(itemDao.findById(90)); 
		d2.setChampAnnotation(c); d2.setObligatoire(false);
		defauts.add(d2); 
				
		Utilisateur u = utilisateurDao.findById(2);
		champAnnotationManager.createOrUpdateObjectManager(c, null, null, null, 
										defauts, u, null, "modification", null);
		assertFalse((champAnnotationManager
				.findByNomLikeManager("NEWCHAMP", true)).get(0).getCombine());
		assertTrue((champAnnotationManager
									.getItemsManager(c, null)).size() == 3);
		assertTrue(champAnnotationManager
								.getAnnotationDefautsManager(c).size() == 2);
		assertTrue(new ArrayList<AnnotationDefaut>(champAnnotationManager
			.getAnnotationDefautsManager(c)).get(0).getObligatoire()
				|| new ArrayList<AnnotationDefaut>(champAnnotationManager
			.getAnnotationDefautsManager(c)).get(1).getObligatoire());
		
		assertTrue(d2.equals(new ArrayList<AnnotationDefaut>(
			champAnnotationManager.getAnnotationDefautsManager(c)).get(0))
			|| d2.equals(new ArrayList<AnnotationDefaut>(champAnnotationManager
							.getAnnotationDefautsManager(c)).get(1)));
		assertTrue(getOperationManager().findByObjectManager(c).size() == 2);
		assertTrue(getOperationManager().findByObjetIdEntiteAndOpeTypeManager(c,
				operationTypeDao.findByNom("Modification").get(0)).size() == 1);
		
		champAnnotationManager.createOrUpdateObjectManager(c, null, null, 
								null, null, u, null, "modification", null);
		assertTrue((champAnnotationManager.getItemsManager(c, null))
														.size() == 3);
		assertTrue((champAnnotationManager
							.getAnnotationDefautsManager(c)).size() == 2);
		assertTrue(d2.equals(new ArrayList<AnnotationDefaut>(
			champAnnotationManager.getAnnotationDefautsManager(c)).get(0))
			|| d2.equals(new ArrayList<AnnotationDefaut>(champAnnotationManager
								.getAnnotationDefautsManager(c)).get(1)));
				
		champAnnotationManager.createOrUpdateObjectManager(c,  null, null,
													new ArrayList<Item>(), 
											new ArrayList<AnnotationDefaut>(), 
												u, null, "modification", null);
		assertTrue((champAnnotationManager.getItemsManager(c, null))
																.size() == 0);
		assertTrue((champAnnotationManager
							.getAnnotationDefautsManager(c)).size() == 0);
		// pour tester la cascade plus loin lors du testRemove
		champAnnotationManager.createOrUpdateObjectManager(c, null, null, its, 
									defauts, u, null, "modification", null);
		assertTrue((champAnnotationManager.getItemsManager(c, null))
																.size() == 3);
		assertTrue((champAnnotationManager
							.getAnnotationDefautsManager(c)).size() == 2);
		//Modification en un doublon engendrant une exception
		Boolean catched = false;
		try {
			c.setNom("Thes2");
			champAnnotationManager.createOrUpdateObjectManager(c, null, 
							null, null, null, u, null, "modification", null);
		} catch (DoublonFoundException e) {
			catched = true;
		}
		assertTrue(catched);
		assertTrue(champAnnotationManager
				.findByNomLikeManager("NEWCHAMP", true).size() == 1);
	}
	
	/**
	 * Teste la methode updateObjectManager. 
	 * @throws ParseException 
	 */
	private void updateItemsObjectManagerTest() {
		ChampAnnotation c = champAnnotationManager
						.findByNomLikeManager("NEWCHAMP", false).get(0);
		// references vers items
		// change soit label, soit valeur, soit les deux
		List<Item> its = 
			new ArrayList<Item>(champAnnotationManager
												.getItemsManager(c, null));
		its.get(0).setLabel("newLabel1");
		its.get(1).setValeur("newValeur2");
		its.get(2).setLabel("newLabel3");
		its.get(2).setValeur("newValeur3");
		Utilisateur u = utilisateurDao.findById(2);
		champAnnotationManager.createOrUpdateObjectManager(c, null, null, its, 
			new ArrayList<AnnotationDefaut>(), u, null, "modification", null);

		assertTrue((champAnnotationManager.getItemsManager(c, null))
															.size() == 3);
		assertTrue(champAnnotationManager
								.getAnnotationDefautsManager(c).size() == 0);

		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(0).getValeur()
													.equals("newValeur2"));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(0).getItemId()
											.equals(its.get(1).getItemId()));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(1).getValeur()
													.equals("val1"));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(1).getItemId()
											.equals(its.get(0).getItemId()));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(2).getLabel()
													.equals("newLabel3"));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(2).getValeur()
													.equals("newValeur3"));
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(2).getItemId()
										.equals(its.get(2).getItemId()));
		
		// ajout d'un item et suppression d'un autre
		its.remove(2);
		Item i4 = new Item();
		i4.setLabel("it4");
		i4.setChampAnnotation(c);
		its.add(i4);
		
		champAnnotationManager.createOrUpdateObjectManager(c, null, null, 
								its, null, u, null, "modification", null);
		assertTrue((champAnnotationManager.getItemsManager(c, null))
															.size() == 3);
		assertTrue(i4.equals(new ArrayList<Item>(
				champAnnotationManager.getItemsManager(c, null)).get(0))
				|| i4.equals(new ArrayList<Item>(champAnnotationManager
								.getItemsManager(c, null)).get(1))
				|| i4.equals(new ArrayList<Item>(champAnnotationManager
						.getItemsManager(c, null)).get(2)));
				
		//Modification en un doublon engendrant une exception
		its = new ArrayList<Item>(champAnnotationManager
												.getItemsManager(c, null));
		its.get(0).setLabel("¢Þ¢ÞÐ");
		Boolean catched = false;
		try {
			champAnnotationManager.createOrUpdateObjectManager(c, null, null, 
									its, null, u, null, "modification", null);
		} catch (ValidationException e) {
			catched = true;
		}
		assertTrue(catched);
		assertTrue(new ArrayList<Item>(champAnnotationManager
				.getItemsManager(c, null)).get(0).getLabel()
													.equals("i2"));
	}

	private void removeObjectManagerTest() {
		ChampAnnotation c = champAnnotationManager
					.findByNomLikeManager("NEWCHAMP", true).get(0);
		champAnnotationManager
			.removeObjectManager(c, null, utilisateurDao.findById(1), null);
		assertTrue(champAnnotationManager
					.findByNomLikeManager("NEWCHAMP", true).size() == 0);
		assertTrue(getOperationManager().findByObjectManager(c).size() == 0);
		
		// suppr champ alpha
		ChampAnnotation c2 = champAnnotationManager
			.findByNomLikeManager("ALPHANUM_TEST", true).get(0);
		champAnnotationManager.removeObjectManager(c2, null, 
										utilisateurDao.findById(1), null);
		
		champAnnotationManager.removeObjectManager(null, null, null, null);
		//verifie que l'etat des tables modifies est revenu identique
		testFindAllObjectsManager();
		//verification de la suppression cascade des associations
		assertTrue(itemDao.findAll().size() == 87);
		assertTrue(annotationDefautDao.findAll().size() == 11);
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(c);
		fs.add(c2);
		cleanUpFantomes(fs);	
	}
	
	
	@Test
	public void testChampValidation() {
		ChampAnnotation c = new ChampAnnotation();
		
		// nom 
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("champAnnotation.nom.empty"));
		}
		c.setNom("");
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("champAnnotation.nom.empty"));
		}
		c.setNom("¢$$E");
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("champAnnotation.nom.illegal"));
		}
		c.setNom(createOverLength(100));
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("champAnnotation.nom.tooLong"));
		}
		c.setNom("Bool");
		c.setDataType(dataTypeDao.findByType("boolean").get(0));
		c.setCombine(true);
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
				.getCode().equals("champAnnotation.combine"
											+ ".notWithbooleanOrFichier"));
		}
		c.setCombine(false);
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		c.setCombine(null);
		BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		c.setDataType(dataTypeDao.findByType("fichier").get(0));
		c.setCombine(null);
		BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		c.setCombine(false);
		BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		c.setCombine(true);
		try {
			BeanValidator.validateObject((Object) c, 
								new Validator[] {champAnnotationValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
				.getCode().equals("champAnnotation.combine"
											+ ".notWithbooleanOrFichier"));
		}
	}
	
	@Test
	public void testMoveChampOrderUpManager() {
		
		assertTrue(champAnnotationManager.findByNomLikeManager("Bool2", true)
				.get(0).getOrdre() == 1);
		assertTrue(champAnnotationManager.findByNomLikeManager("Date1", true)
				.get(0).getOrdre() == 2);
		assertTrue(champAnnotationManager.findByNomLikeManager("Num1", true)
				.get(0).getOrdre() == 3);
		assertTrue(champAnnotationManager.findByNomLikeManager("Texte1", true)
				.get(0).getOrdre() == 5);
		assertTrue(champAnnotationManager.findByNomLikeManager("Thes1", true)
				.get(0).getOrdre() == 6);
	
		ChampAnnotation c = champAnnotationManager
								.findByNomLikeManager("Texte1", false).get(0);
	
		// up le Texte1
		champAnnotationManager.moveChampOrderUpDownManager(c, true);
		assertTrue(champAnnotationManager.findByNomLikeManager("Bool2", true)
				.get(0).getOrdre() == 1);
		assertTrue(champAnnotationManager.findByNomLikeManager("Date1", true)
				.get(0).getOrdre() == 2);
		assertTrue(champAnnotationManager.findByNomLikeManager("Num1", true)
				.get(0).getOrdre() == 5);
		assertTrue(champAnnotationManager.findByNomLikeManager("Texte1", true)
				.get(0).getOrdre() == 3);
		assertTrue(champAnnotationManager.findByNomLikeManager("Thes1", true)
				.get(0).getOrdre() == 6);
		
		// down le texte1
		champAnnotationManager.moveChampOrderUpDownManager(c, false);
		assertTrue(champAnnotationManager.findByNomLikeManager("Bool2", true)
				.get(0).getOrdre() == 1);
		assertTrue(champAnnotationManager.findByNomLikeManager("Date1", true)
				.get(0).getOrdre() == 2);
		assertTrue(champAnnotationManager.findByNomLikeManager("Num1", true)
				.get(0).getOrdre() == 3);
		assertTrue(champAnnotationManager.findByNomLikeManager("Texte1", true)
				.get(0).getOrdre() == 5);
		assertTrue(champAnnotationManager.findByNomLikeManager("Thes1", true)
				.get(0).getOrdre() == 6);
		
		// up le c1 Bool2 -> ne change rien
		c = champAnnotationManager.findByNomLikeManager("Bool2", false).get(0);
		champAnnotationManager.moveChampOrderUpDownManager(c, true);
		assertTrue(champAnnotationManager.findByNomLikeManager("Bool2", true)
				.get(0).getOrdre() == 1);
		assertTrue(champAnnotationManager.findByNomLikeManager("Date1", true)
				.get(0).getOrdre() == 2);
		assertTrue(champAnnotationManager.findByNomLikeManager("Num1", true)
				.get(0).getOrdre() == 3);
		assertTrue(champAnnotationManager.findByNomLikeManager("Texte1", true)
				.get(0).getOrdre() == 5);
		assertTrue(champAnnotationManager.findByNomLikeManager("Thes1", true)
				.get(0).getOrdre() == 6);
	
		
		// down Thes1 -> ne change rien
		c = champAnnotationManager.findByNomLikeManager("Thes1", false).get(0);
		champAnnotationManager.moveChampOrderUpDownManager(c, false);
		assertTrue(champAnnotationManager.findByNomLikeManager("Bool2", true)
				.get(0).getOrdre() == 1);
		assertTrue(champAnnotationManager.findByNomLikeManager("Date1", true)
				.get(0).getOrdre() == 2);
		assertTrue(champAnnotationManager.findByNomLikeManager("Num1", true)
				.get(0).getOrdre() == 3);
		assertTrue(champAnnotationManager.findByNomLikeManager("Texte1", true)
				.get(0).getOrdre() == 5);
		assertTrue(champAnnotationManager.findByNomLikeManager("Thes1", true)
				.get(0).getOrdre() == 6);
		
	}

	@Test
	public void testItemValidation() {
		Item i = new Item();
		
		// label 
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.label.empty"));
		}
		i.setLabel("");
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.label.empty"));
		}
		i.setLabel("$$''");
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.label.illegal"));
		}
		i.setLabel(createOverLength(100));
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.label.tooLong"));
		}
		
		// validation ok car valeur null
		i.setLabel("label: -ok_");
		BeanValidator.validateObject((Object) i, 
									new Validator[] {itemValidator});
		
		i.setValeur("");
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.valeur.empty"));
		}
		i.setValeur("$$###''");
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.valeur.illegal"));
		}
		i.setValeur(createOverLength(100));
		try {
			BeanValidator.validateObject((Object) i, 
								new Validator[] {itemValidator});
		} catch (ValidationException ve) {
			assertTrue(ve.getErrors().get(0).getFieldError()
							.getCode().equals("item.valeur.tooLong"));
		}	
	}
	
	@Test
	public void testCreateOrDeleteFileDirectoryManager() throws IOException {
		if (new File("/tmp/pt_1").exists()) {
			// nettoyage
			try {
				FileUtils.deleteDirectory(new File("/tmp/pt_1"));
				Thread.sleep(500);
				assertFalse(new File("/tmp/pt_1").exists());
			} catch (IOException e) {
				assertFalse(true);
			} catch (InterruptedException e) {
				assertFalse(true);
				e.printStackTrace();
			} 
		}
		List<Banque> banques = new ArrayList<Banque>();
		// champ transient donc pas enregistrement
		ChampAnnotation c = new ChampAnnotation();
		champAnnotationManager
				.createOrDeleteFileDirectoryManager("/tmp/", c, false, banques);
		
		c = champAnnotationManager.findByNomLikeManager("bool2", true).get(0);
		banques = champAnnotationManager.getBanquesFromTableManager(c);
		champAnnotationManager
				.createOrDeleteFileDirectoryManager("/tmp/", c, false, banques);
		assertTrue(new File("/tmp/pt_1").exists());
		assertTrue(new File("/tmp/pt_1").list().length == 3);
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_4").isDirectory());
		assertTrue(new File("/tmp/pt_1/coll_2/anno/chp_4").isDirectory());
		assertTrue(new File("/tmp/pt_1/coll_3/anno/chp_4").isDirectory());
		
		c = champAnnotationManager
							.findByNomLikeManager("Alphanum1", true).get(0);
		banques = champAnnotationManager.getBanquesFromTableManager(c);
		// champ alphanum mais appartient a la table 1
		// ne doit pas ecraser /tmp/pt_1/coll_1
		champAnnotationManager
			.createOrDeleteFileDirectoryManager("/tmp/", c, false, banques);
		assertTrue(new File("/tmp/pt_1").list().length == 3);
		assertTrue(new File("/tmp/pt_1/coll_1/anno").list().length == 2);
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_4").isDirectory());
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_1").isDirectory());
		
		// ajout deux fichiers
		new File("/tmp/pt_1/coll_1/anno/chp_1/guns").createNewFile();
		new File("/tmp/pt_1/coll_1/anno/chp_1/roses").createNewFile();
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_1").list().length == 2);
		
		champAnnotationManager
			.createOrDeleteFileDirectoryManager("/tmp/", c, true, banques);
		assertTrue(new File("/tmp/pt_1/coll_1/anno").list().length == 1);
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_4").isDirectory());
		assertFalse(new File("/tmp/pt_1/coll_1/anno/chp_1").exists());
		
		// creation suppression d'un champ type fichier
		ChampAnnotation chp = new ChampAnnotation();
		Utilisateur u = utilisateurDao.findById(2);
		chp.setNom("CHAMP_TEST");
		chp.setOrdre(1);
		DataType dt = dataTypeDao.findByType("fichier").get(0);
		chp.setDataType(dt);
		chp.setTableAnnotation(tableAnnotationDao.findById(2));
		try {
			champAnnotationManager
			.createOrUpdateObjectManager(chp, null, dt, null, null, u, null, 
														"creation", null);
		} catch (RuntimeException re) {
			assertTrue(re.getMessage().equals("error.filesystem.access"));
		}
		champAnnotationManager
			.createOrUpdateObjectManager(chp, null, dt, null, null, u, null, 
														"creation", "/tmp/");
		assertTrue((champAnnotationManager
			.findByNomLikeManager("CHAMP_TEST", true)).size() == 1);
		assertTrue(new File("/tmp/pt_1/coll_1/anno/chp_" 
							+ chp.getChampAnnotationId()).isDirectory());
		
		ChampAnnotation cTest = champAnnotationManager
						.findByNomLikeManager("CHAMP_TEST", true).get(0);

		champAnnotationManager.removeObjectManager(cTest, null, u, "/tmp/");
		assertTrue(champAnnotationManager
				.findByNomLikeManager("CHAMP_TEST", true).size() == 0);
		assertFalse(new File("/tmp/pt_1/coll_1/anno/chp_"
				+ chp.getChampAnnotationId()).exists());

		
		// nettoyage
		try {
			FileUtils.deleteDirectory(new File("/tmp/pt_1"));
			Thread.sleep(500);
			assertFalse(new File("/tmp/pt_1").exists());
		} catch (IOException e) {
			assertFalse(true);
		} catch (InterruptedException e) {
			assertFalse(true);
			e.printStackTrace();
		} 
		
		List<TKFantomableObject> fs = new ArrayList<TKFantomableObject>();
		fs.add(cTest);
		cleanUpFantomes(fs);	
	}
	
	@Test
	public void testGetBanquesFromTableManager() {
		ChampAnnotation c = champAnnotationManager
							.findByNomManager("Bool2").get(0);
		assertTrue(champAnnotationManager
						.getBanquesFromTableManager(c).size() == 3);
		c = new ChampAnnotation();
		assertTrue(champAnnotationManager
						.getBanquesFromTableManager(c).size() == 0);
		TableAnnotation t = new TableAnnotation();
		c.setTableAnnotation(t);
		assertTrue(champAnnotationManager
						.getBanquesFromTableManager(c).size() == 0);
		assertTrue(champAnnotationManager
				.getBanquesFromTableManager(null).size() == 0);
	}
	
	@Test
	public void testGetAllChampFromTableManager() {
		ChampAnnotation c = champAnnotationManager
										.findByNomManager("Bool2").get(0);
		assertTrue(champAnnotationManager
							.getAllChampsFromTableManager(c).size() == 5);
		c = new ChampAnnotation();
		assertTrue(champAnnotationManager
							.getAllChampsFromTableManager(c).size() == 0);
		TableAnnotation t = new TableAnnotation();
		c.setTableAnnotation(t);
		assertTrue(champAnnotationManager
							.getAllChampsFromTableManager(c).size() == 0);
	}
	
	@Test
	public void testFindByEditByCatalogueManager() {
		TableAnnotation t = tableAnnotationDao.findById(3);
		List<ChampAnnotation> chps = champAnnotationManager
										.findByEditByCatalogueManager(t);
		assertTrue(chps.size() == 2);
		assertTrue(chps.get(0).getNom().equals("Texte1"));
		assertTrue(chps.get(1).getNom().equals("Thes1"));
		
		t = tableAnnotationDao.findById(2);
		chps = champAnnotationManager.findByEditByCatalogueManager(t);
		assertTrue(chps.size() == 3);
		
		chps = champAnnotationManager.findByEditByCatalogueManager(null);
		assertTrue(chps.size() == 0);
	}
	
	@Test
	public void testIsUsedItemManager() {
		Item i = itemDao.findById(1);
		assertTrue(champAnnotationManager.isUsedItemManager(i));
		i = itemDao.findById(3);
		assertFalse(champAnnotationManager.isUsedItemManager(i));
	}
	
	@Test
	public void testFindChampsFichiersByTableManager() {
		TableAnnotation t = tableAnnotationDao.findById(5);
		List<ChampAnnotation> chps = champAnnotationManager
									.findChampsFichiersByTableManager(t);
		assertTrue(chps.size() == 1);
		assertTrue(chps.get(0).equals(champAnnotationManager
							.findByNomLikeManager("file2", true).get(0)));
		t = tableAnnotationDao.findById(7);
		chps = champAnnotationManager.findChampsFichiersByTableManager(t);
		assertTrue(chps.size() == 0);
	}
	
	@Test
	public void testIsUsedObjectManager() {
		ChampAnnotation c = champAnnotationManager
					.findByNomLikeManager("Alphanum1", true).get(0);
    	assertTrue(champAnnotationManager
    							.isUsedObjectManager(c).isEmpty());
    	c = champAnnotationManager
					.findByNomLikeManager("Alphanum2", true).get(0);
    	assertTrue(champAnnotationManager
				.isUsedObjectManager(c).size() == 1);
    	assertTrue(champAnnotationManager
				.isUsedObjectManager(null).isEmpty());
	}
}