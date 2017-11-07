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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.io.export.RechercheDao;
import fr.aphp.tumorotek.dao.io.export.RequeteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Recherche;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Classe de test pour le DAO RechercheDao et le 
 * bean du domaine Recherche.
 * Classe de test créée le 13/04/10.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class RechercheDaoTest extends AbstractDaoTest  {

	/** Bean Dao. */
	private RechercheDao rechercheDao;
	private UtilisateurDao utilisateurDao;
	private RequeteDao requeteDao;
	private AffichageDao affichageDao;
	private BanqueDao banqueDao;
	
	private EntityManagerFactory entityManagerFactory;

	/** Constructeur. */
	public RechercheDaoTest() {
	}

	/**
	 * Setter du bean RechercheDao.
	 * @param aDao est le bean Dao.
	 */
	public void setRechercheDao(RechercheDao aDao) {
		this.rechercheDao = aDao;
	}

	/**
	 * Setter du bean UtilisateurDao.
	 * @param uDao est le bean Dao.
	 */
	public void setUtilisateurDao(UtilisateurDao uDao) {
		this.utilisateurDao = uDao;
	}
	
	/**
	 * Setter du bean AffichageDao.
	 * @param aDao est le bean Dao.
	 */
	public void setAffichageDao(AffichageDao aDao) {
		this.affichageDao = aDao;
	}
	
	/**
	 * Setter du bean RequeteDao.
	 * @param rDao est le bean Dao.
	 */
	public void setRequeteDao(RequeteDao rDao) {
		this.requeteDao = rDao;
	}
	
	/**
	 * Setter du bean BanqueDao.
	 * @param bDao est le bean Dao.
	 */
	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}
	
	public void setEntityManagerFactory(EntityManagerFactory ef) {
		this.entityManagerFactory = ef;
	}

	public void testFindByBanqueId() {
    	List<Recherche> liste = rechercheDao.findByBanqueId(1);
    	assertTrue(liste.size() == 3);
    	
    	liste = rechercheDao.findByBanqueId(3);
    	assertTrue(liste.size() == 1);
    	
    	liste = rechercheDao.findByBanqueId(null);
    	assertTrue(liste.size() == 0);
    }
	
	public void testFindByBanqueInList() {
    	List<Integer> bks = new ArrayList<Integer>();
    	bks.add(1);
    	List<Recherche> liste = rechercheDao.findByBanqueIdinList(bks);
    	assertTrue(liste.size() == 3);
    	
    	bks.add(2);
    	liste = rechercheDao.findByBanqueIdinList(bks);
    	assertTrue(liste.size() == 4);
    	
    	liste = rechercheDao.findByBanqueIdinList(null);
    	assertTrue(liste.size() == 0);
    }


    /**
	 * Test l'appel de la méthode findRecherchesByUtilisateur().
	 */
    public void testFindRecherchesByUtilisateur() throws Exception {
		List<Utilisateur> utilisateurs = this.utilisateurDao.findAll();
		Iterator<Utilisateur> itUtil = utilisateurs.iterator();
		while (itUtil.hasNext()) {
			Utilisateur utilisateur = itUtil.next();
			List<Recherche> recherches = this.rechercheDao
					.findByUtilisateur(utilisateur);
			Iterator<Recherche> it = recherches.iterator();
			while (it.hasNext()) {
				assertTrue(it.next().getCreateur().equals(utilisateur));
			}
		}
	}
    
    /**
	 * Test l'appel de la méthode findByExcludedId().
	 */
	public void testFindByExcludedId() {
		List<Recherche> liste = rechercheDao.findAll();
		Iterator<Recherche> it = liste.iterator();
		while (it.hasNext()) {
			Recherche temp = it.next();
			List<Recherche> recherches = 
				rechercheDao.findByExcludedId(temp.getRechercheId());
			assertTrue(recherches.size() == liste.size() - 1);		
			assertFalse(recherches.contains(temp));
		}
		
	}
	
	public void testFindByRequete() {
		Requete r1 = requeteDao.findById(1);
		List<Recherche> liste = rechercheDao.findByRequete(r1);
		assertTrue(liste.size() == 0);
		
		Requete r3 = requeteDao.findById(3);
		liste = rechercheDao.findByRequete(r3);
		assertTrue(liste.size() == 2);
		
		liste = rechercheDao.findByRequete(null);
		assertTrue(liste.size() == 0);
	}
	
	/**
	 * Test l'appel de la méthode findByIntituleUtilisateur().
	 */
    public void testFindByIntituleUtilisateur() throws Exception {
		Utilisateur u1 = utilisateurDao.findById(1);
		Utilisateur u2 = utilisateurDao.findById(2);
		List<Recherche> liste = rechercheDao
			.findByIntituleUtilisateur("Aff%", u1);
		assertTrue(liste.size() == 1);
		
		liste = rechercheDao
			.findByIntituleUtilisateur("yug%", u1);
		assertTrue(liste.size() == 0);
		
		liste = rechercheDao
			.findByIntituleUtilisateur("Essen%", u2);
		assertTrue(liste.size() == 0);
		
		liste = rechercheDao
			.findByIntituleUtilisateur(null, u1);
		assertTrue(liste.size() == 0);
	
		liste = rechercheDao
			.findByIntituleUtilisateur("Essen%", null);
		assertTrue(liste.size() == 0);
	}

    /**
     * Test l'insertion, la mise à jour et la suppression 
	 * d'un recherche.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	public void testCrudRecherche() throws Exception {
		final int idCreateur = 3;
		final int idRequete = 3;
		final int idAffichage = 2;
		
		final Utilisateur createur = this.utilisateurDao.findById(idCreateur);
		final Requete requete = this.requeteDao.findById(idRequete);
		final Affichage affichage = this.affichageDao.findById(idAffichage);
		final String intitule = "intitule";
		
		ArrayList<Banque> banques = new ArrayList<Banque>();
		banques.add(this.banqueDao.findById(2));
		banques.add(this.banqueDao.findById(3));
		banques.add(this.banqueDao.findById(1));

		Recherche r = new Recherche();
		r.setIntitule(intitule);
		r.setCreateur(createur);
		r.setAffichage(affichage);
		r.setRequete(requete);
		r.setBanques(banques);
		
		// Test de l'insertion
		Integer idObject = new Integer(-1);		
		this.rechercheDao.createObject(r);
		List<Recherche> recherches = this.rechercheDao.findAll();
		Iterator<Recherche> itRecherche = recherches.iterator();
		boolean found = false;
		while (itRecherche.hasNext()) {
			Recherche temp = itRecherche.next();
			if (temp.equals(r)) {
				found = true;
				idObject = temp.getRechercheId();
				break;
			}
		}
		assertTrue(found);

		Recherche r2 = this.rechercheDao.findById(r.getRechercheId());
		assertNotNull(r2);
		assertNotNull(this.rechercheDao.findById(idObject).getIntitule());
		assertTrue(r2.getIntitule().equals("intitule"));
		assertNotNull(this.rechercheDao.findById(idObject).getCreateur());
		assertTrue(r2.getCreateur().equals(createur));
		assertNotNull(this.rechercheDao.findById(idObject).getAffichage());
		assertTrue(r2.getAffichage().equals(affichage));
		assertNotNull(this.rechercheDao.findById(idObject).getRequete());
		assertTrue(r2.getRequete().equals(requete));
		assertNotNull(this.rechercheDao.findById(idObject).getBanques());
		Iterator<Banque> it = rechercheDao.findById(idObject)
				.getBanques().iterator();
		while (it.hasNext()) {
			assertTrue(r2.getBanques().contains(it.next()));
		}
		
		
		// Test de la mise à jour
		final int idCreateur2 = 2;
		final int idRequete2 = 2;
		final int idAffichage2 = 1;
		
		final Utilisateur updatedCreateur = this.utilisateurDao
				.findById(idCreateur2);
		final String updatedIntitule = "intitule2";
		final Requete updatedRequete = this.requeteDao.findById(idRequete2);
		final Affichage updatedAffichage = this.affichageDao
				.findById(idAffichage2);
		
		ArrayList<Banque> updatedBanques = new ArrayList<Banque>();
		updatedBanques.add(this.banqueDao.findById(2));
		updatedBanques.add(this.banqueDao.findById(1));

		r2.setIntitule(updatedIntitule);
		r2.setCreateur(updatedCreateur);
		r2.setAffichage(updatedAffichage);
		r2.setBanques(updatedBanques);
		r2.setRequete(updatedRequete);

		this.rechercheDao.updateObject(r2);
		assertNotNull(this.rechercheDao.findById(idObject).getIntitule());
		assertTrue(this.rechercheDao.findById(idObject).getIntitule()
				.equals(updatedIntitule));
		assertNotNull(this.rechercheDao.findById(idObject).getCreateur());
		assertTrue(this.rechercheDao.findById(idObject).getCreateur()
				.equals(updatedCreateur));
		assertNotNull(this.rechercheDao.findById(idObject).getAffichage());
		assertTrue(this.rechercheDao.findById(idObject).getAffichage()
				.equals(updatedAffichage));
		assertNotNull(this.rechercheDao.findById(idObject).getRequete());
		assertTrue(this.rechercheDao.findById(idObject).getRequete()
				.equals(updatedRequete));
		assertNotNull(this.rechercheDao.findById(idObject).getBanques());
		assertTrue(this.rechercheDao.findById(idObject).getBanques()
				.equals(updatedBanques));
		// Test de la délétion
		this.rechercheDao.removeObject(idObject);
		assertNull(this.rechercheDao.findById(idObject));

	}
	
	/**
	 * test toString().
	 */
	public void testToString() {
		Recherche r1 = rechercheDao.findById(1);
		assertTrue(r1.toString().equals(r1.getIntitule()));
		
		Recherche r2 = new Recherche();
		assertTrue(r2.toString().equals("{Empty Recherche}"));
	}
	
	/**
	 * Test de la méthode surchargée "equals".
	 */
	public void testEquals() {
		//On boucle sur les 32 possibilités
		for (int i = 0; i < Math.pow(2, 4); i++) {
			Recherche recherche1 = new Recherche();
			Recherche recherche2 = new Recherche();
			int toTest = i % 8;
			Affichage affichage = null;
			if (toTest >= 8) {
				affichage = affichageDao.findById(2);
			}
			recherche1.setAffichage(affichage);
			recherche2.setAffichage(affichage);
			toTest = i % 4;
			Requete requete = null;
			if (toTest >= 4) {
				requete = requeteDao.findById(2);
			}
			recherche1.setRequete(requete);
			recherche2.setRequete(requete);
			toTest = i % 2;
			Utilisateur createur = null;
			if (toTest >= 2) {
				createur = utilisateurDao.findById(2);
			}
			recherche1.setCreateur(createur);
			recherche2.setCreateur(createur);
			String intitule = null;
			
			if (toTest > 0) {
				intitule = "Intitule";
			}
			recherche1.setIntitule(intitule);
			recherche2.setIntitule(intitule);
			//On compare les 2 recherches
			assertTrue(recherche1.equals(recherche2));
		}
	}
	
	/**
	 * Test de la méthode surchargée "hashcode".
	 */
	public void testHashCode() {
		//On boucle sur les 32 possibilités
		for (int i = 0; i < Math.pow(2, 4); i++) {
			Recherche recherche = new Recherche();			
			int hash = 7;
			int toTest = i % 8;
			Affichage affichage = null;
			int hashAffichage = 0;
			if (toTest >= 8) {
				affichage = affichageDao.findById(2);
				hashAffichage = affichage.hashCode();
			}
			toTest = i % 4;
			Requete requete = null;
			int hashRequete = 0;
			if (toTest >= 4) {
				requete = requeteDao.findById(2);
				hashRequete = requete.hashCode();
			}
			Utilisateur createur = null;
			int hashCreateur = 0;
			if (i >= 2) {				
				createur = utilisateurDao.findById(3);
				hashCreateur = createur.hashCode();
			}			
			toTest = i % 2;
			String intitule = null;
			int hashIntitule = 0;
			if (toTest > 0) {				
				intitule = "Intitule";
				hashIntitule = intitule.hashCode();
			}
			hash = 31 * hash + hashIntitule;
			hash = 31 * hash + hashCreateur;
			hash = 31 * hash + hashAffichage;
			hash = 31 * hash + hashRequete;
			
			recherche.setAffichage(affichage);
			recherche.setRequete(requete);
			recherche.setCreateur(createur);
			recherche.setIntitule(intitule);
			//On vérifie que le hashCode est bon
			assertTrue(recherche.hashCode() == hash);
			assertTrue(recherche.hashCode() == hash);
			assertTrue(recherche.hashCode() == hash);
		}
	}
}