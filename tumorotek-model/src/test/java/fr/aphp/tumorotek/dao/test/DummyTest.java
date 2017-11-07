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
package fr.aphp.tumorotek.dao.test;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ContexteDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Contexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * 
 * Classe de test pour le DAO BanqueDao et le bean du domaine Banque.
 * 
 * @author Pierre Ventadour.
 * @version 08/09/2009
 *
 */
public class DummyTest extends AbstractDaoTest {
	
	/** Bean Dao BanqueDao. */
	private BanqueDao banqueDao;
	private PrelevementDao prelevementDao;
	private EchantillonDao echantillonDao;
	private CollaborateurDao collaborateurDao;
	
	public void setCollaborateurDao(CollaborateurDao collaborateurDao) {
		this.collaborateurDao = collaborateurDao;
	}

	/** Constructeur. */
	public DummyTest() {
	}

	/**
	 * Setter du bean BanqueDao.
	 * @param bDao est le bean Dao.
	 */
	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}
	
	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setEchantillonDao(EchantillonDao eDao) {
		this.echantillonDao = eDao;
	}

	/**
	 * Test l'appel de la méthode findAll().
	 */
	public void testReadAllBanques() {
		List<Banque> banques = banqueDao.findAll();
		Banque a = banques.get(1);
		System.out.println(a.getPrelevements().iterator().next().getBanque());
		List<Prelevement> prels = prelevementDao.findByCode("NPD5S");
		assertNotNull(prels);
	}
	
	public void testAssoc() {
		List<String> ps = prelevementDao.findByBanqueSelectCode(banqueDao.findById(2));
		System.out.println(ps.size());
		List<Prelevement> prels = prelevementDao.findByCode("NPD5S");
		System.out.println(prels.get(0).getBanque());
		assertNotNull(prels);
	}
	
	public void testAssoc2() {
		Banque banque = banqueDao.findById(new Integer(2));
		System.out.println(banque);
		List<Echantillon> echs = echantillonDao.findByCode("TUM%");
		assertNotNull(echs);
	}
	
	public void testAutraAssoc() {
		List<Collaborateur> cols = collaborateurDao.findAll();
		cols.get(5).getEtablissement();
		//cols.get(1).getBanques();
		System.out.println(cols.get(4).getEchantillons().iterator().next().getBanque());
	}

	

}
