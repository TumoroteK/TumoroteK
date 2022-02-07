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
package fr.aphp.tumorotek.manager.test.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager UiRequeteManager.
 * Classe créée le 23/07/2014.
 *
 * @author Mathieu BARTHELEMY.
 * @version 2.0.11
 *
 */
public class UiRequeteManagerTest extends AbstractManagerTest4
{
	//	@Autowired
	//	private UiRequeteManager uiRequeteManager;
	//	@Autowired
	//	private UtilisateurDao utilisateurDao;
	//	@Autowired
	//	private EntiteDao entiteDao;

	public UiRequeteManagerTest(){}

	// eviter warnings
	@Test
	public void test() {
		assertEquals(true, true);
	}

	//	public void testFindByIdManager(){
	//		UiRequete req = uiRequeteManager.findByIdManager(1);
	//		assertNotNull(req);
	//		assertTrue(req.getNom().equals("REQ1"));
	//		req = uiRequeteManager.findByIdManager(6);
	//		assertNull(req);
	//	}
	//
	//	public void testFindByNomUtilisateurAndEntiteManager(){
	//		List<UiRequete> reqs =
	//				uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(1), entiteDao.findById(2)).orElse(null);
	//		assertTrue(reqs.size() == 3);
	//
	//		reqs = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(1), entiteDao.findById(3)).orElse(null);
	//		assertTrue(reqs.isEmpty());
	//
	//		reqs = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(5), entiteDao.findById(3)).orElse(null);
	//		assertTrue(reqs.size() == 1);
	//
	//		reqs = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(5), null).orElse(null);
	//		assertTrue(reqs.isEmpty());
	//
	//		reqs = uiRequeteManager.findByUtilisateurAndEntiteManager(null, entiteDao.findById(2)).orElse(null);
	//		assertTrue(reqs.isEmpty());
	//	}
	//
	//	public void testFindDoublonManager(){
	//		UiRequete req = null;
	//		assertFalse(uiRequeteManager.findDoublonManager(req));
	//		req = new UiRequete(null, utilisateurDao.findById(1), entiteDao.findById(2), "REQ3", 4).orElse(null);
	//		assertTrue(uiRequeteManager.findDoublonManager(req));
	//		req.setNom("RE3b");
	//		assertFalse(uiRequeteManager.findDoublonManager(req));
	//		req.setNom("REQ3");
	//		assertTrue(uiRequeteManager.findDoublonManager(req));
	//		req.setUtilisateur(utilisateurDao.findById(5)).orElse(null);
	//		assertFalse(uiRequeteManager.findDoublonManager(req));
	//		req.setEntite(entiteDao.findById(3)).orElse(null);
	//		assertFalse(uiRequeteManager.findDoublonManager(req));
	//		req.setNom("REQADMIN");
	//		assertTrue(uiRequeteManager.findDoublonManager(req));
	//		assertFalse(uiRequeteManager.findDoublonManager(null));
	//	}
	//
	//	public void testCrud() throws ParseException{
	//		final List<UiCompValue> vals = new ArrayList<>();
	//		final UiCompValue u1 = new UiCompValue(null, null, "nipBox", "textbox", null, "121212", null, null);
	//		vals.add(u1);
	//		final Calendar dateNaissance = Calendar.getInstance();
	//		dateNaissance.setTime(new SimpleDateFormat("dd/mm/yyyy").parse("02/12/1965"));
	//		final UiCompValue u2 = new UiCompValue(null, null, "dateNaisBox", "datebox", null, null, null, dateNaissance);
	//		vals.add(u2);
	//
	//		// Test de l'insertion
	//		uiRequeteManager.createObjectManager("REQPAT", utilisateurDao.findById(2), entiteDao.findById(1), 1, vals).orElse(null);
	//
	//		UiRequete r1 = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).get(0).orElse(null);
	//		// Vérification des données entrées dans la base
	//		assertTrue(r1.getNom().equals("REQPAT"));
	//		assertTrue(r1.getOrdre() == 1);
	//		assertTrue(r1.getUiCompValues().size() == 2);
	//		final Iterator<UiCompValue> it = r1.getUiCompValues().iterator();
	//		UiCompValue v1 = it.next();
	//		assertTrue(v1.getUiRequete().getNom().equals("REQPAT"));
	//		assertTrue(v1.getIdComponent().equals("nipBox"));
	//		assertTrue(v1.getComponentClass().equals("textbox"));
	//		assertNull(v1.getIndexValue());
	//		assertNull(v1.getCalendarValue());
	//		assertNull(v1.getCheckValue());
	//		assertTrue(v1.getTextValue().equals("121212"));
	//		final UiCompValue v2 = it.next();
	//		assertTrue(v2.getUiRequete().getNom().equals("REQPAT"));
	//		assertTrue(v2.getIdComponent().equals("dateNaisBox"));
	//		assertTrue(v2.getComponentClass().equals("datebox"));
	//		assertNull(v2.getIndexValue());
	//		assertTrue(v2.getCalendarValue().equals(dateNaissance));
	//		assertNull(v2.getCheckValue());
	//		assertNull(v2.getTextValue());
	//
	//		// Test de la mise à jour
	//		r1.setOrdre(3);
	//		r1.setNom("REQPAT_CLEAR");
	//		final UiCompValue u3 = new UiCompValue(null, null, "sexeMFBox", "checkbox", null, null, false, null);
	//		vals.clear();
	//		vals.add(u3);
	//
	//		// null update 
	//		uiRequeteManager.mergeObjectManager(null, vals);
	//
	//		final UiRequete r2 =
	//				uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).get(0).orElse(null);
	//		assertTrue(r2.getNom().equals("REQPAT"));
	//		assertTrue(r2.getUiCompValues().size() == 2);
	//
	//		uiRequeteManager.mergeObjectManager(r1, vals);
	//
	//		r1 = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).get(0).orElse(null);
	//		assertTrue(r1.getNom().equals("REQPAT_CLEAR"));
	//		assertTrue(r1.getOrdre() == 3);
	//		assertTrue(r1.getUiCompValues().size() == 1);
	//		v1 = r1.getUiCompValues().iterator().next();
	//		assertTrue(v1.getUiRequete().getNom().equals("REQPAT_CLEAR"));
	//		assertTrue(v1.getIdComponent().equals("sexeMFBox"));
	//		assertTrue(v1.getComponentClass().equals("checkbox"));
	//		assertNull(v1.getIndexValue());
	//		assertNull(v1.getCalendarValue());
	//		assertFalse(v1.getCheckValue());
	//		assertNull(v1.getTextValue());
	//
	//		r1.setOrdre(4);
	//		uiRequeteManager.mergeObjectManager(r1, null);
	//
	//		r1 = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).get(0).orElse(null);
	//		assertTrue(r1.getOrdre() == 4);
	//		assertFalse(r1.getUiCompValues().isEmpty());
	//
	//		vals.clear();
	//		uiRequeteManager.mergeObjectManager(r1, vals);
	//		r1 = uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).get(0).orElse(null);
	//		assertTrue(r1.getUiCompValues().isEmpty());
	//
	//		// Test de la délétion
	//		uiRequeteManager.removeObjectManager(r1);
	//		assertTrue(uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(2), entiteDao.findById(1)).isEmpty()).orElse(null);
	//		// null remove
	//		uiRequeteManager.removeObjectManager(null);
	//	}
	//
	//	public void testUiRequeteValidation(){
	//
	//		// required objects
	//		boolean catched = false;
	//		try{
	//			uiRequeteManager.createObjectManager("TESTVAL", null, entiteDao.findById(5), 1, null).orElse(null);
	//		}catch(final RequiredObjectIsNullException re){
	//			catched = true;
	//		}
	//		assertTrue(catched);
	//		catched = false;
	//		try{
	//			uiRequeteManager.createObjectManager("TESTVAL", utilisateurDao.findById(5), null, 1, null).orElse(null);
	//		}catch(final RequiredObjectIsNullException re){
	//			catched = true;
	//		}
	//		assertTrue(catched);
	//		catched = false;
	//		try{
	//			uiRequeteManager.createObjectManager("TESTVAL", utilisateurDao.findById(5), entiteDao.findById(5), null, null).orElse(null);
	//		}catch(final RequiredObjectIsNullException re){
	//			catched = true;
	//		}
	//		assertTrue(catched);
	//		assertTrue(uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(5), entiteDao.findById(5)).isEmpty()).orElse(null);
	//
	//		// test doublon		
	//		catched = false;
	//		try{
	//			uiRequeteManager.createObjectManager("REQ2", utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final DoublonFoundException de){
	//			catched = true;
	//		}
	//		assertTrue(catched);
	//		assertTrue(
	//				uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(1), entiteDao.findById(2)).size() == 3).orElse(null);
	//
	//		// test validator
	//		List<Errors> errs = new ArrayList<>();
	//		try{
	//			uiRequeteManager.createObjectManager(null, utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final ValidationException ve){
	//			errs = ve.getErrors();
	//			assertTrue(errs.get(0).getFieldError().getCode().equals("uiRequete.nom.empty"));
	//		}
	//		assertFalse(errs.isEmpty());
	//		errs.clear();
	//		try{
	//			uiRequeteManager.createObjectManager("", utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final ValidationException ve){
	//			errs = ve.getErrors();
	//			assertTrue(errs.get(0).getFieldError().getCode().equals("uiRequete.nom.empty"));
	//		}
	//		assertFalse(errs.isEmpty());
	//		errs.clear();
	//		try{
	//			uiRequeteManager.createObjectManager("   ", utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final ValidationException ve){
	//			errs = ve.getErrors();
	//			assertTrue(errs.get(0).getFieldError().getCode().equals("uiRequete.nom.empty"));
	//		}
	//		assertFalse(errs.isEmpty());
	//		errs.clear();
	//		try{
	//			uiRequeteManager.createObjectManager("*}}˛¿<¿", utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final ValidationException ve){
	//			errs = ve.getErrors();
	//			assertTrue(errs.get(0).getFieldError().getCode().equals("uiRequete.nom.illegal"));
	//		}
	//		assertFalse(errs.isEmpty());
	//		errs.clear();
	//		try{
	//			uiRequeteManager.createObjectManager(createOverLength(250), utilisateurDao.findById(1), entiteDao.findById(2), 6, null).orElse(null);
	//		}catch(final ValidationException ve){
	//			errs = ve.getErrors();
	//			assertTrue(errs.get(0).getFieldError().getCode().equals("uiRequete.nom.tooLong"));
	//		}
	//		assertFalse(errs.isEmpty());
	//
	//		assertTrue(
	//				uiRequeteManager.findByUtilisateurAndEntiteManager(utilisateurDao.findById(1), entiteDao.findById(2)).size() == 3).orElse(null);
	//	}
}
