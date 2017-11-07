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
package fr.aphp.tumorotek.dao.test.coeur.prelevement;

import java.util.HashSet;
import java.util.List;
import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.ProtocoleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementDelegate;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 * 
 * Classe de test pour la delegation des prélèvements.
 * Classe de test créée le 01/02/2012.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class PrelevementDelegateDaoTest extends AbstractDaoTest {
	
	private NatureDao natureDao;
	private PrelevementDao prelevementDao;
	private BanqueDao banqueDao;
	private ConsentTypeDao consentTypeDao;
	private ContexteDao contexteDao;
	private ProtocoleDao protocoleDao;
	
	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setNatureDao(NatureDao nDao) {
		this.natureDao = nDao;
	}

	public void setConsentTypeDao(ConsentTypeDao ctDao) {
		this.consentTypeDao = ctDao;
	}
	
	public void setContexteDao(ContexteDao ceDao) {
		this.contexteDao = ceDao;
	}

	public void setProtocoleDao(ProtocoleDao pDao) {
		this.protocoleDao = pDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	/**
	 * Constructeur.
	 */
	public PrelevementDelegateDaoTest() {
	}

	/**
	 * Test l'appel de la méthode toString().
	 */
	public void testToString() {
		PrelevementDelegate p1 = prelevementDao.findById(1).getPrelevementSero();
		assertTrue(p1.toString().equals("{PRLVT1}.CONT1"));
		p1 = new PrelevementDelegate();
		assertTrue(p1.toString().equals("{Empty delegate}"));
	}	
	
	
	/**
	 * Test l'insertion, la mise à jour et la suppression d'un prelevement avec 
	 * son delegate.
	 * @throws Exception lance une exception en cas de problème lors du CRUD.
	 */
	@Rollback(false)
	public void testCrudPrelAndDelegate() throws Exception {
		
		Prelevement p = new Prelevement();
		
		/*Champs obligatoires*/
		Banque b = banqueDao.findById(2);
		p.setBanque(b);
		p.setCode("prelDel1");
		Nature n = natureDao.findById(1);
		p.setNature(n);
		ConsentType ct = consentTypeDao.findById(2);
		p.setConsentType(ct);
		
		PrelevementSero ps1 = new PrelevementSero();
		ps1.setLibelle("SERO1");
		List<Protocole> protos = protocoleDao.findAll();
		ps1.setProtocoles(new HashSet<Protocole>(protos));
		ps1.setContexte(contexteDao.findById(1));
		ps1.setPrelevement(p);
		p.setDelegate(ps1);
		
		// Test de l'insertion
		prelevementDao.createObject(p);
		assertTrue(prelevementDao.findAll().size() == 6);
		
		p = prelevementDao.findByCode("prelDel1").get(0);
		assertNotNull(p.getPrelevementSero());
		PrelevementSero ps2 = (PrelevementSero) p.getPrelevementSero();
		assertTrue(ps2.getLibelle().equals("SERO1"));
		assertTrue(ps2.getProtocoles().size() == 3);
		
		protos = protocoleDao.findByNom("OFSEP");
		ps2.setProtocoles(new HashSet<Protocole>(protos));
		
		prelevementDao.updateObject(p);
		assertTrue(prelevementDao.findAll().size() == 6);
		
		// Test de la mise à jour
		p = prelevementDao.findByCode("prelDel1").get(0);
		assertNotNull(p.getPrelevementSero());
		ps2 = p.getPrelevementSero();
		assertTrue(ps2.getLibelle().equals("SERO1"));
		assertTrue(ps2.getProtocoles().size() == 1);
		assertTrue(ps2.getProtocoles().iterator().next().getNom().equals("OFSEP"));
		
		// Test de la délétion
		p.setDelegate(null);
		p.setCode("updated");
		prelevementDao.updateObject(p);
		p = prelevementDao.findByCode("updated").get(0);
		assertTrue(p.getPrelevementSero() == null);
		
		prelevementDao.removeObject(p.getPrelevementId());
		assertTrue(prelevementDao.findAll().size() == 5);
	}
	
	public void testIsEmpty() {
		PrelevementSero pSero = new PrelevementSero();
		assertTrue(pSero.isEmpty());
		pSero.setLibelle("libelle");
		assertFalse(pSero.isEmpty());
		pSero.setLibelle("");
		assertTrue(pSero.isEmpty());
		pSero.setLibelle(null);
		assertTrue(pSero.isEmpty());
		pSero.getProtocoles().add(new Protocole());
		assertFalse(pSero.isEmpty());
		pSero.setProtocoles(null);
		assertTrue(pSero.isEmpty());
	}
	
	/**
	 * Test des méthodes surchargées "equals" et hashcode pour
	 * la table transcodeUtilisateur.
	 */
	public void testEqualsAndHashCode() {
		PrelevementDelegate p1 = new PrelevementDelegate();
		PrelevementDelegate p2 = new PrelevementDelegate();
		assertFalse(p1.equals(null));
		assertNotNull(p2);
		assertTrue(p1.equals(p2));
		assertTrue(p1.equals(p2));
		assertTrue(p1.hashCode() == p2.hashCode());
		
		p1.setPrelevement(prelevementDao.findById(1));
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		assertTrue(p1.hashCode() != p2.hashCode());
		p2.setPrelevement(prelevementDao.findById(2));
		assertFalse(p1.equals(p2));
		assertFalse(p2.equals(p1));
		assertTrue(p1.hashCode() != p2.hashCode());
		p1.setPrelevement(prelevementDao.findById(2));
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		assertTrue(p1.hashCode() == p2.hashCode());
		
		// dummy
		Categorie c = new Categorie();
		assertFalse(p1.equals(c));
	}
}
