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
package fr.aphp.tumorotek.manager.test.io.export;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.manager.io.export.RechercheManager;
import fr.aphp.tumorotek.manager.io.export.RequeteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class RechercheManagerTest extends AbstractManagerTest4 {
	
	/** Bean Manager. */
	@Autowired
	private RechercheManager manager;
	@Autowired
	private AffichageManager affichageManager;
	@Autowired
	private RequeteManager requeteManager;
	@Autowired
	private UtilisateurDao utilisateurDao;
	@Autowired
	private ChampEntiteDao champEntiteDao;
	@Autowired
	private AffichageDao affichageDao;
	@Autowired
	private RequeteDao requeteDao;
	@Autowired
	private BanqueDao banqueDao;

	@Test
	public void testFindById() {
		Recherche recherche = manager.findByIdManager(2);
		assertNotNull(recherche);
		assertTrue(recherche.getIntitule().equals("Femmes"));
		
		Recherche rechercheNull = manager.findByIdManager(200);
		assertNull(rechercheNull);
	}
	
	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAllObjects() {
		List<Recherche> list = manager.findAllObjectsManager();
		//On vérifie chaque élément par un findById
		Iterator<Recherche> it = list.iterator();
		while (it.hasNext()) {
			Recherche temp = it.next();
			if (temp != null) {
				assertTrue(temp.equals(
						manager.findByIdManager(temp.getRechercheId())));
			}
		}
	}
		
//	@Test
//	public void testCopyRecherche() {
//		//On récupère un recherche
//		Recherche recherche = manager.findByIdManager(3);
//		assertNotNull(recherche);
//		Utilisateur util = utilisateurDao.findById(3);
//		Banque b = banqueDao.findById(1);
//		
//		// On teste une copie avec des attributs non valides
//		try {
//			manager.copyRechercheManager(recherche, null, b);
//		} catch (RequiredObjectIsNullException e) {
//			assertEquals("RequiredObjectIsNullException",
//					e.getClass().getSimpleName());
//		}
//		try {
//			manager.copyRechercheManager(null, util, b);
//		} catch (RequiredObjectIsNullException e) {
//			assertEquals("RequiredObjectIsNullException",
//					e.getClass().getSimpleName());
//		}
//		try {
//			manager.copyRechercheManager(recherche, util, null);
//		} catch (RequiredObjectIsNullException e) {
//			assertEquals("RequiredObjectIsNullException",
//					e.getClass().getSimpleName());
//		}
//
//		//On récupère la premiere requete
//		Recherche copie = manager.copyRechercheManager(recherche, util, b);
//		
//		//On verifie que la recherche et la copie sont identiques
//		assertTrue(manager.isCopyManager(recherche, copie));
//		
//		//On verifie que la copie est bien en base de donnees
//		Recherche copie2 = manager.findByIdManager(copie.getRechercheId());
//		assertNotNull(copie2);
//		
//		int idAffichage = copie2.getAffichage().getAffichageId();
//		int idRequete = copie2.getRequete().getRequeteId();
//		
//		//On supprime les éléments créés
//		manager.removeObjectManager(copie2);
//		
//		//On supprime l'affichage
//		Affichage affichage = affichageManager.findByIdManager(idAffichage);
//		affichageManager.removeObjectManager(affichage);
//		
//		//On supprime la requete
//		Requete requete = requeteManager.findByIdManager(idRequete);
//		requeteManager.removeObjectManager(requete);
//	}
//	
	@Test
	public void testRenameRecherche() {
		//On récupère un recherche
		Recherche recherche = manager.findByIdManager(4);
		assertNotNull(recherche);
		
		// On teste une insertion avec des attributs non valides
		String[] intituleValues = new String[]{"", "  ", null, "hfu_|(", 
				createOverLength(100)};
		for (int i = 0; i < intituleValues.length; i++) {
			try {
				manager.renameRechercheManager(recherche, intituleValues[i]);
			} catch (ValidationException e) {
				assertEquals("ValidationException", e.getClass()
						.getSimpleName());				
			}
			try {
				manager.renameRechercheManager(null, intituleValues[i]);
			} catch (RequiredObjectIsNullException e) {
				assertEquals("RequiredObjectIsNullException", e.getClass()
						.getSimpleName());				
			}
		}
		
		//On récupère un recherche
		recherche = manager.findByIdManager(1);
		assertNotNull(recherche);
		//On teste un recherche valide
		String oldIntitule = recherche.getIntitule();
		String intitule = recherche.getIntitule() + " MODIFICATION";
		manager.renameRechercheManager(recherche, intitule);
		
		//On récupère l'objet modifié
		Recherche recherche2 = manager.findByIdManager(
				recherche.getRechercheId());
		//On vérifie la modification de l'intitulé
		assertTrue(recherche2.getIntitule().equals(intitule));
		
		//On réécrit l'intitule original
		manager.renameRechercheManager(recherche, oldIntitule);
	}
	
	
	
	@Test
	public void testFindByUtilisateur() {
		Utilisateur util = utilisateurDao.findById(1);		
		List<Recherche> recherches = manager.findByUtilisateurManager(util);
		Iterator<Recherche> it = recherches.iterator();
		while (it.hasNext()) {
			Recherche temp = it.next();
			assertTrue(temp.getCreateur().equals(util));
		}
	}
	
	@Test
	public void testFindByIntituleAndUtilisateurManager() {
		Utilisateur u1 = utilisateurDao.findById(1);
		Utilisateur u2 = utilisateurDao.findById(2);
		List<Recherche> liste = manager
			.findByIntituleAndUtilisateurManager("Aff%", u1);
		assertTrue(liste.size() == 1);
		
		liste = manager
		.findByIntituleAndUtilisateurManager("yug%", u1);
		assertTrue(liste.size() == 0);
		
		liste = manager
		.findByIntituleAndUtilisateurManager("Essen%", u2);
		assertTrue(liste.size() == 0);
		
		liste = manager
		.findByIntituleAndUtilisateurManager(null, u1);
		assertTrue(liste.size() == 0);
	
		liste = manager
		.findByIntituleAndUtilisateurManager("Essen%", null);
		assertTrue(liste.size() == 0);
	}
	
	@Test
	public void testFindBanquesManager() {
		Recherche r1 = manager.findByIdManager(1);
		List<Banque> bks = manager.findBanquesManager(r1);
		assertTrue(bks.size() == 1);
		
		Recherche r2 = manager.findByIdManager(2);
		bks = manager.findBanquesManager(r2);
		assertTrue(bks.size() == 4);
		
		bks = manager.findBanquesManager(null);
		assertTrue(bks.size() == 0);
	}
	
	@Test
	public void testFindByBanqueInLIstManager() {
		List<Banque> bks = new ArrayList<Banque>();
		Banque b1 = banqueDao.findById(1);
		bks.add(b1);
		List<Recherche> liste = manager.findByBanqueInLIstManager(bks);
		assertTrue(liste.size() == 3);
		
		Banque b2 = banqueDao.findById(2);
		bks.add(b2);
		liste = manager.findByBanqueInLIstManager(bks);
		assertTrue(liste.size() == 4);
		
		liste = manager.findByBanqueInLIstManager(new ArrayList<Banque>());
		assertTrue(liste.size() == 0);
		
		liste = manager.findByBanqueInLIstManager(null);
		assertTrue(liste.size() == 0);
	}
	
	@Test
	public void testCrud() {
		/** Save. */
		Utilisateur util = utilisateurDao.findById(1);
		Banque b = banqueDao.findById(1);
		
		ChampEntite champEntite = champEntiteDao.findById(25);
		Champ champ1 = new Champ(champEntite);
		Resultat resultat1 = new Resultat("col1", champ1, true,
				1, 2, "format", null);
		champEntite = champEntiteDao.findById(54);
		Champ champ2 = new Champ(champEntite);
		Resultat resultat2 = new Resultat("col2", champ2, false,
				2, 1, null, null);		
		
		List<Resultat> res = new ArrayList<Resultat>();
		res.add(resultat1);
		res.add(resultat2);
		
		Affichage aff = new Affichage("aff", util, 20);		
		Critere crit = new Critere(champ1, "<", "30");		
		Groupement gRacine = new Groupement(crit, null, null, null);
		
		Requete req = new Requete("req", util, gRacine);
		
		ArrayList<Banque> banques = new ArrayList<Banque>();
		banques.add(this.banqueDao.findById(2));
		banques.add(this.banqueDao.findById(3));
		banques.add(this.banqueDao.findById(1));
		
		//On créé un recherche
		Recherche recherche = new Recherche("rech", aff, req, banques);
		manager.createObjectManager(recherche, aff, req, banques, util, b);
		//On vérifie que la sauvegarde a été correctement effectuée
		Recherche recherche2 = manager.findByIdManager(
				recherche.getRechercheId());
		assertNotNull(recherche2);
		
		assertTrue(recherche.equals(recherche2));
		assertTrue(manager.findAllObjectsManager().size() == 5);
		assertTrue(manager.findBanquesManager(recherche2).size() == 3);
		
		/** Update. */
		String oldIntitule = recherche.getIntitule();
		String intitule = "test modification recherche";		
		recherche2.setIntitule(intitule);
		
		ArrayList<Banque> oldBanques = banques;
		banques = new ArrayList<Banque>();
		banques.add(this.banqueDao.findById(4));
		
		Affichage oldAffichage = recherche.getAffichage();
		Affichage affichage = affichageManager.findByIdManager(3); 
		recherche2.setAffichage(affichage);
		
		Requete oldRequete = recherche.getRequete();
		Requete requete = requeteManager.findByIdManager(2);
		recherche2.setRequete(requete);
		
		manager.updateObjectManager(recherche2, recherche2.getAffichage(),
				recherche2.getRequete(), banques, recherche2
						.getCreateur(), b);
		assertTrue(manager.findAllObjectsManager().size() == 5);
		
		Recherche rechTest = manager.findByIdManager(
				recherche2.getRechercheId());
		assertFalse(rechTest.getIntitule().equals(oldIntitule));
		assertTrue(rechTest.getIntitule().equals(intitule));
		assertFalse(rechTest.getAffichage().equals(oldAffichage));
		assertTrue(rechTest.getAffichage().equals(affichage));
		assertFalse(rechTest.getRequete().equals(oldRequete));
		assertTrue(rechTest.getRequete().equals(requete));
		assertFalse(rechTest.getBanques().equals(oldBanques));
		assertTrue(manager.findBanquesManager(rechTest).size() == 1);
		assertTrue(manager.findBanquesManager(rechTest).get(0)
				.equals(banqueDao.findById(4)));
		
		/** Delete. */
		//On récupère un recherche
		int id = rechTest.getRechercheId();
		
		//On le supprime
		manager.removeObjectManager(rechTest);
		assertTrue(manager.findAllObjectsManager().size() == 4);
		//On essaye de le récupérer via l'identifiant
		recherche2 = manager.findByIdManager(id);
		//On vérifie qu'il n'existe pas dans la liste du manager
		assertNull(recherche2);
		
		//On supprime l'affichage
		affichageManager.removeObjectManager(oldAffichage);
		
		//On supprime la requete
		requeteManager.removeObjectManager(oldRequete);
	}
	
	@Test
	public void testFindDoublons() {
		Recherche recherche = new Recherche();
		Utilisateur createur = utilisateurDao.findById(1);
		recherche.setCreateur(createur);
		recherche.setIntitule("Femmes");
		Affichage affichage = affichageDao.findById(1);
		recherche.setAffichage(affichage);
		Requete requete = requeteDao.findById(3);
		recherche.setRequete(requete);
		
		ArrayList<Banque> banques = new ArrayList<Banque>();
		banques.add(banqueDao.findById(1));
		banques.add(banqueDao.findById(2));
		banques.add(banqueDao.findById(3));
		banques.add(banqueDao.findById(4));
		
		recherche.setBanques(banques);
				
		assertTrue(manager.findDoublonManager(recherche));
		
		recherche.setIntitule("Femems");
		assertFalse(manager.findDoublonManager(recherche));
	}
}
