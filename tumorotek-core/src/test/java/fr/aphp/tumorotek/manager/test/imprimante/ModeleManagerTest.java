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
package fr.aphp.tumorotek.manager.test.imprimante;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.imprimante.AffectationImprimanteManager;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleTypeManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
// import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
// import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
// import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;
import fr.aphp.tumorotek.model.io.export.Champ;
// import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le manager ModeleManager.
 * Classe créée le 17/03/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ModeleManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ModeleManager modeleManager;
   @Autowired
   private ModeleTypeManager modeleTypeManager;
   @Autowired
   private PlateformeManager plateformeManager;
   @Autowired
   private AffectationImprimanteManager affectationImprimanteManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ImprimanteDao imprimanteDao;
   @Autowired
   private LigneEtiquetteManager ligneEtiquetteManager;
   @Autowired
   private ChampLigneEtiquetteManager champLigneEtiquetteManager;
   @Autowired
   private ChampManager champManager;
   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private EntiteDao entiteDao;

   public ModeleManagerTest(){}

   @Test
   public void testFindById(){
      final Modele m = modeleManager.findByIdManager(1);
      assertNotNull(m);
      assertTrue(m.getNom().equals("NBT"));

      final Modele mNull = modeleManager.findByIdManager(10);
      assertNull(mNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Modele> list = modeleManager.findAllObjectsManager();
      assertTrue(list.size() == 3);
   }

   /**
    * Test la méthode findByPlateformeManager.
    */
   @Test
   public void testFindByPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<Modele> list = modeleManager.findByPlateformeManager(pf1);
      assertTrue(list.size() == 3);

      list = modeleManager.findByPlateformeManager(pf2);
      assertTrue(list.size() == 0);

      list = modeleManager.findByPlateformeManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomAndPlateformeManager.
    */
   @Test
   public void testFindByNomAndPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<Modele> list = modeleManager.findByNomAndPlateformeManager("NBT", pf1);
      assertTrue(list.size() == 1);

      list = modeleManager.findByNomAndPlateformeManager("qdvvqd", pf1);
      assertTrue(list.size() == 0);

      list = modeleManager.findByNomAndPlateformeManager("NBT", pf2);
      assertTrue(list.size() == 0);

      list = modeleManager.findByNomAndPlateformeManager(null, pf1);
      assertTrue(list.size() == 0);

      list = modeleManager.findByNomAndPlateformeManager("NBT", null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "NBT";
      final String nom2 = "GESTION";
      final Plateforme pf1 = plateformeManager.findByIdManager(1);

      final Modele m1 = new Modele();
      m1.setPlateforme(pf1);
      m1.setNom(nom1);
      assertTrue(modeleManager.findDoublonManager(m1));

      m1.setNom(nom2);
      assertFalse(modeleManager.findDoublonManager(m1));

      final Modele m2 = modeleManager.findByIdManager(2);
      assertFalse(modeleManager.findDoublonManager(m2));

      m2.setNom(nom1);
      assertTrue(modeleManager.findDoublonManager(m2));

      assertFalse(modeleManager.findDoublonManager(null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Modele m1 = modeleManager.findByIdManager(1);
      assertTrue(modeleManager.isUsedObjectManager(m1));

      final Modele m3 = modeleManager.findByIdManager(3);
      assertFalse(modeleManager.isUsedObjectManager(m3));

      assertFalse(modeleManager.isUsedObjectManager(new Modele()));
      assertFalse(modeleManager.isUsedObjectManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      //createObjectManagerTest();
      updateObjectManagerTest();
      //removeObjectManagerTest();
   }

   //	private void createObjectManagerTest() throws ParseException {
   //		
   //		Plateforme pf1 = plateformeManager.findByIdManager(1);
   //		ModeleType mt1 = modeleTypeManager.findByIdManager(1);
   //		Modele m1 = new Modele();
   //		m1.setNom("MODELE");
   //		m1.setTexteLibre("TEXTE");
   //		m1.setIsDefault(true);
   //		
   //		boolean catched = false;
   //		// on test l'insertion avec une pf nulle
   //		try {
   //			modeleManager.createObjectManager(m1, null, mt1, null, null);
   //		} catch (Exception e) {
   //			if (e.getClass().getSimpleName().equals(
   //					"RequiredObjectIsNullException")) {
   //				catched = true;
   //			}
   //		}
   //		assertTrue(catched);
   //		catched = false;
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		
   //		// on test l'insertion avec un type null
   //		try {
   //			modeleManager.createObjectManager(m1, pf1, null, null, null);
   //		} catch (Exception e) {
   //			if (e.getClass().getSimpleName().equals(
   //					"RequiredObjectIsNullException")) {
   //				catched = true;
   //			}
   //		}
   //		assertTrue(catched);
   //		catched = false;
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		
   //		// on test l'insertion d'un doublon
   //		m1.setNom("NBT");
   //		try {
   //			modeleManager.createObjectManager(m1, pf1, mt1, null, null);
   //		} catch (Exception e) {
   //			if (e.getClass().getSimpleName().equals(
   //					"DoublonFoundException")) {
   //				catched = true;
   //			}
   //		}
   //		assertTrue(catched);
   //		catched = false;
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		
   //		// on teste la validation du nom lors de la création
   //		String[] emptyValues = new String[]{null, "", "  ", "%¬ ↓»üß*d", 
   //				createOverLength(25)};
   //		for (int i = 0; i < emptyValues.length; i++) {
   //			catched = false;
   //			try {
   //				m1.setNom(emptyValues[i]);
   //				modeleManager.createObjectManager(m1, pf1, mt1, null, null);
   //			} catch (Exception e) {
   //				if (e.getClass().getSimpleName().equals(
   //						"ValidationException")) {
   //					catched = true;
   //				}
   //			}
   //			assertTrue(catched);
   //		}
   //		m1.setNom("NEW");
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		
   //		// on teste la validation du texte libre lors de la création
   //		emptyValues = new String[]{"", "  ", "%¬ ↓»üß*d", 
   //				createOverLength(20)};
   //		for (int i = 0; i < emptyValues.length; i++) {
   //			catched = false;
   //			try {
   //				m1.setTexteLibre(emptyValues[i]);
   //				modeleManager.createObjectManager(m1, pf1, mt1, null, null);
   //			} catch (Exception e) {
   //				if (e.getClass().getSimpleName().equals(
   //						"ValidationException")) {
   //					catched = true;
   //				}
   //			}
   //			assertTrue(catched);
   //		}
   //		m1.setTexteLibre("TEXTE");
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		
   //		// validation du isDefault
   //		catched = false;
   //		try {
   //			m1.setIsDefault(null);
   //			modeleManager.createObjectManager(m1, pf1, mt1, null, null);
   //		} catch (Exception e) {
   //			if (e.getClass().getSimpleName().equals(
   //					"ValidationException")) {
   //				catched = true;
   //			}
   //		}
   //		assertTrue(catched);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		m1.setIsDefault(false);
   //		
   //		// on test la validation d'une ligne
   //		List<LigneEtiquette> lignes = new ArrayList<LigneEtiquette>();
   //		lignes.add(new LigneEtiquette());
   //		catched = false;
   //		try {
   //			modeleManager.createObjectManager(m1, pf1, mt1, lignes, null);
   //		} catch (Exception e) {
   //			catched = true;
   //		}
   //		assertTrue(catched);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
   //		
   //		// insertion valide
   //		m1.setIsDefault(true);
   //		modeleManager.createObjectManager(m1, pf1, mt1, null, null);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 4);
   //		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
   //		Integer idM = m1.getModeleId();
   //		
   //		Modele mTest = modeleManager.findByIdManager(idM);
   //		assertNotNull(mTest);
   //		assertNotNull(mTest.getPlateforme());
   //		assertNotNull(mTest.getModeleType());
   //		assertTrue(mTest.getNom().equals("NEW"));
   //		assertTrue(mTest.getTexteLibre().equals("TEXTE"));
   //		assertTrue(mTest.getIsDefault());
   //		
   //		// création avec 2 lignes (dont aura 2 champs).
   //		Modele m2 = new Modele();
   //		m2.setNom("MODELE LiGNES");
   //		m2.setTexteLibre("TEXTE");
   //		m2.setIsDefault(false);
   //		
   //		// création des lignes
   //		lignes = new ArrayList<LigneEtiquette>();
   //		LigneEtiquette le1 = new LigneEtiquette();
   //		le1.setOrdre(1);
   //		le1.setEntete("ENTETE");
   //		le1.setContenu("CONTENU");
   //		le1.setFont("FONT");
   //		le1.setStyle("STYLE");
   //		le1.setIsBarcode(false);
   //		le1.setSize(4);
   //		LigneEtiquette le2 = new LigneEtiquette();
   //		le2.setOrdre(2);
   //		le2.setEntete("ENTETE2");
   //		le2.setContenu("CONTENU2");
   //		le2.setFont("FONT2");
   //		le2.setStyle("STYLE2");
   //		le2.setIsBarcode(false);
   //		le2.setSize(4);
   //		lignes.add(le1);
   //		lignes.add(le2);
   //		
   //		// création des champs
   //		Champ ch1 = new Champ();
   //		ch1.setChampEntite(champEntiteDao.findById(1)).orElse(null);
   //		Champ ch2 = new Champ();
   //		ch2.setChampEntite(champEntiteDao.findById(2)).orElse(null);
   //		ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
   //		cle1.setChamp(ch1);
   //		cle1.setEntite(entiteDao.findById(1)).orElse(null);
   //		cle1.setOrdre(1);
   //		ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
   //		cle2.setChamp(ch2);
   //		cle2.setEntite(entiteDao.findById(2)).orElse(null);
   //		cle2.setOrdre(2);
   //		List<ChampLigneEtiquette> champs = 
   //			new ArrayList<ChampLigneEtiquette>();
   //		champs.add(cle1);
   //		champs.add(cle2);
   //		Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> hash = 
   //			new Hashtable<LigneEtiquette, List<ChampLigneEtiquette>>();
   //		hash.put(le1, champs);
   //		
   //		modeleManager.createObjectManager(m2, pf1, mt1, lignes, hash);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 5);
   //		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 9);
   //		assertTrue(champLigneEtiquetteManager
   //				.findAllObjectsManager().size() == 12);
   //		assertTrue(champManager.findAllObjectsManager().size() == 141);
   //		Integer idM2 = m2.getModeleId();
   //		
   //		Modele mTest2 = modeleManager.findByIdManager(idM2);
   //		assertNotNull(mTest2);
   //		assertNotNull(mTest2.getPlateforme());
   //		List<LigneEtiquette> lis = ligneEtiquetteManager
   //			.findByModeleManager(mTest2);
   //		assertTrue(lis.size() == 2);
   //		assertTrue(lis.get(0).equals(le1));
   //		List<ChampLigneEtiquette> chps = champLigneEtiquetteManager
   //			.findByLigneEtiquetteManager(lis.get(0));
   //		assertTrue(chps.size() == 2);
   //		assertTrue(chps.get(0).equals(cle1));
   //		assertTrue(chps.get(0).getChamp().equals(ch1));
   //		assertTrue(champLigneEtiquetteManager
   //			.findByLigneEtiquetteManager(lis.get(1)).size() == 0);
   //				
   //		// suppression des profils et des droits
   //		modeleManager.removeObjectManager(mTest);
   //		modeleManager.removeObjectManager(mTest2);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
   //		assertTrue(champLigneEtiquetteManager
   //				.findAllObjectsManager().size() == 10);
   //		assertTrue(champManager.findAllObjectsManager().size() == 139);
   //	}

   private void updateObjectManagerTest() throws ParseException{

      final int cTots = champManager.findAllObjectsManager().size();

      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ModeleType mt1 = modeleTypeManager.findByIdManager(1);
      final Modele m = new Modele();
      m.setNom("MODELE");
      m.setTexteLibre("TEXTE");
      m.setIsDefault(false);

      // création des lignes
      List<LigneEtiquette> lignes = new ArrayList<>();
      final LigneEtiquette le1 = new LigneEtiquette();
      le1.setOrdre(1);
      le1.setEntete("ENTETE");
      le1.setContenu("CONTENU");
      le1.setFont("FONT");
      le1.setStyle("STYLE");
      le1.setIsBarcode(false);
      le1.setSize(4);
      final LigneEtiquette le2 = new LigneEtiquette();
      le2.setOrdre(2);
      le2.setEntete("ENTETE2");
      le2.setContenu("CONTENU2");
      le2.setFont("FONT2");
      le2.setStyle("STYLE2");
      le2.setIsBarcode(false);
      le2.setSize(4);
      lignes.add(le1);
      lignes.add(le2);

      // création des champs
      final Champ ch1 = new Champ();
      ch1.setChampEntite(champEntiteDao.findById(1)).orElse(null);
      final Champ ch2 = new Champ();
      ch2.setChampEntite(champEntiteDao.findById(2)).orElse(null);
      final ChampLigneEtiquette cle1 = new ChampLigneEtiquette();
      cle1.setChamp(ch1);
      cle1.setEntite(entiteDao.findById(1)).orElse(null);
      cle1.setOrdre(1);
      final ChampLigneEtiquette cle2 = new ChampLigneEtiquette();
      cle2.setChamp(ch2);
      cle2.setEntite(entiteDao.findById(2)).orElse(null);
      cle2.setOrdre(2);
      List<ChampLigneEtiquette> champs = new ArrayList<>();
      champs.add(cle1);
      champs.add(cle2);
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> hash =
         new Hashtable<>();
      hash.put(le1, champs);

      modeleManager.createObjectManager(m, pf1, mt1, lignes, hash);
      final Integer idM = m.getModeleId();
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);
      assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 9);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 12);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);

      final Modele mUp1 = modeleManager.findByIdManager(idM);
      boolean catched = false;
      // on test l'insertion avec une pf nulle
      try{
         modeleManager.updateObjectManager(mUp1, null, mt1, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // on test l'insertion avec un type null
      try{
         modeleManager.updateObjectManager(mUp1, pf1, null, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // on test l'insertion d'un doublon
      mUp1.setNom("NBT");
      try{
         modeleManager.updateObjectManager(mUp1, pf1, mt1, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // on teste la validation du nom lors de la création
      String[] emptyValues = new String[] {null, "", "  ", "%¬ ↓»üß*d", createOverLength(25)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            mUp1.setNom(emptyValues[i]);
            modeleManager.updateObjectManager(mUp1, pf1, mt1, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      mUp1.setNom("UP");
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // on teste la validation du texte libre lors de la création
      emptyValues = new String[] {"", "  ", "%¬ ↓»üß*d", createOverLength(20)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            mUp1.setTexteLibre(emptyValues[i]);
            modeleManager.updateObjectManager(mUp1, pf1, mt1, null, null, null, null);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      mUp1.setTexteLibre("TEXTE_UP");
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // validation du isDefault
      catched = false;
      try{
         mUp1.setIsDefault(null);
         modeleManager.updateObjectManager(mUp1, pf1, mt1, null, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);
      mUp1.setIsDefault(false);

      // on test la validation d'une ligne
      lignes = new ArrayList<>();
      lignes.add(new LigneEtiquette());
      catched = false;
      try{
         modeleManager.updateObjectManager(mUp1, pf1, mt1, lignes, null, null, null);
      }catch(final Exception e){
         catched = true;
      }
      assertTrue(catched);
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      // insertion valide sans modifs sur les associations
      mUp1.setIsDefault(true);
      modeleManager.updateObjectManager(mUp1, pf1, mt1, null, null, null, null);
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);

      final Modele mTest = modeleManager.findByIdManager(idM);
      assertNotNull(mTest);
      assertNotNull(mTest.getPlateforme());
      assertNotNull(mTest.getModeleType());
      assertTrue(mTest.getNom().equals("UP"));
      assertTrue(mTest.getTexteLibre().equals("TEXTE_UP"));
      assertTrue(mTest.getIsDefault());

      // modif sur les assos : on ajoute et retire un champ
      // de la première ligne
      final Champ ch3 = new Champ();
      ch3.setChampEntite(champEntiteDao.findById(3)).orElse(null);
      final ChampLigneEtiquette cle3 = new ChampLigneEtiquette();
      cle3.setChamp(ch3);
      cle3.setEntite(entiteDao.findById(1)).orElse(null);
      cle3.setOrdre(1);
      lignes = new ArrayList<>();
      lignes.add(le1);
      lignes.add(le2);
      champs = new ArrayList<>();
      champs.add(cle3);
      champs.add(cle2);
      final List<ChampLigneEtiquette> chpsToRmv = new ArrayList<>();
      chpsToRmv.add(cle1);
      hash.put(le1, champs);
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> hashToRmv =
         new Hashtable<>();
      hashToRmv.put(le1, chpsToRmv);

      modeleManager.updateObjectManager(mTest, pf1, mt1, lignes, null, hash, hashToRmv);
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);
      assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 9);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 12);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 2);

      final Modele mTest2 = modeleManager.findByIdManager(idM);
      assertTrue(ligneEtiquetteManager.findByModeleManager(mTest2).size() == 2);
      List<ChampLigneEtiquette> tmp =
         champLigneEtiquetteManager.findByLigneEtiquetteManager(ligneEtiquetteManager.findByModeleManager(mTest2).get(0));
      assertTrue(tmp.size() == 2);
      assertTrue(tmp.get(0).getChamp().equals(ch3));

      // modifs : on supprime deux lignes et en crée une autre
      // avec un champ
      lignes = new ArrayList<>();
      final LigneEtiquette le3 = new LigneEtiquette();
      le3.setOrdre(1);
      le3.setEntete("ENTETE3");
      le3.setContenu("CONTENU3");
      le3.setFont("FONT3");
      le3.setStyle("STYLE3");
      le3.setIsBarcode(false);
      le3.setSize(4);
      lignes.add(le3);
      final Champ ch4 = new Champ();
      ch4.setChampEntite(champEntiteDao.findById(4)).orElse(null);
      final ChampLigneEtiquette cle4 = new ChampLigneEtiquette();
      cle4.setChamp(ch4);
      cle4.setEntite(entiteDao.findById(1)).orElse(null);
      cle4.setOrdre(1);
      champs = new ArrayList<>();
      champs.add(cle4);
      hash.put(le3, champs);
      List<LigneEtiquette> lignesToRmv = new ArrayList<>();
      lignesToRmv = ligneEtiquetteManager.findByModeleManager(mTest2);

      modeleManager.updateObjectManager(mTest2, pf1, mt1, lignes, lignesToRmv, hash, null);
      assertTrue(modeleManager.findAllObjectsManager().size() == 4);
      assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 8);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 11);
      assertTrue(champManager.findAllObjectsManager().size() == cTots + 1);

      final Modele mTest3 = modeleManager.findByIdManager(idM);
      final List<LigneEtiquette> lis = ligneEtiquetteManager.findByModeleManager(mTest3);
      assertTrue(lis.size() == 1);
      assertTrue(lis.get(0).getEntete().equals("ENTETE3"));
      tmp = champLigneEtiquetteManager.findByLigneEtiquetteManager(lis.get(0));
      assertTrue(tmp.size() == 1);
      assertTrue(tmp.get(0).getChamp().equals(ch4));

      // suppression des profils et des droits
      modeleManager.removeObjectManager(mTest3);
      assertTrue(modeleManager.findAllObjectsManager().size() == 3);
      assertTrue(ligneEtiquetteManager.findAllObjectsManager().size() == 7);
      assertTrue(champLigneEtiquetteManager.findAllObjectsManager().size() == 10);
      assertTrue(champManager.findAllObjectsManager().size() == cTots);
   }

   //	private void removeObjectManagerTest() throws ParseException {
   //		// creation d'un modele
   //		Plateforme pf1 = plateformeManager.findByIdManager(1);
   //		ModeleType mt1 = modeleTypeManager.findByIdManager(1);
   //		Modele m = new Modele();
   //		m.setNom("MODELE");
   //		m.setTexteLibre("TEXTE");
   //		m.setIsDefault(false);
   //		modeleManager.createObjectManager(m, pf1, mt1, null, null);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 4);
   //		
   //		// creation d'une AffectationImprimante
   //		Banque b1 = banqueDao.findById(1).orElse(null);
   //		Imprimante i2 = imprimanteDao.findById(2).orElse(null);
   //		Utilisateur u1 = utilisateurDao.findById(1).orElse(null);
   //		AffectationImprimante ai = new AffectationImprimante();
   //		affectationImprimanteManager.createObjectManager(ai, u1, b1, i2, m);
   //		assertTrue(affectationImprimanteManager
   //					.findAllObjectsManager().size() == 5);
   //		
   //		// on vérifie que tous les objets sont supprimés
   //		modeleManager.removeObjectManager(m);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //		assertTrue(affectationImprimanteManager
   //				.findAllObjectsManager().size() == 4);
   //		
   //		modeleManager.removeObjectManager(null);
   //		assertTrue(modeleManager.findAllObjectsManager().size() == 3);
   //	}
}
