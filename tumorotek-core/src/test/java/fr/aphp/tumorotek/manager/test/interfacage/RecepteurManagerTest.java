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
package fr.aphp.tumorotek.manager.test.interfacage;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import fr.aphp.tumorotek.manager.interfacage.RecepteurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.interfacage.Recepteur;

/**
 * 
 * Classe de test pour le manager Recepteur.
 * Classe créée le 08/10/2014.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0.10.3
 *
 */
public class RecepteurManagerTest extends AbstractManagerTest4 {
	
	@Autowired
	private RecepteurManager recepteurManager;
//	
//	@SuppressWarnings("deprecation")
//	public RecepteurManagerTest() {
//		// pour eviter l'autowiring vers fantomeDao
//		this.setDependencyCheck(false);
//	}
//	
//	@Override
//	protected String[] getConfigLocations() {
//        return new String[]{ "applicationContextManagerBase.xml",
//       		 "applicationContextDaoBase.xml" };
//    }

	@Test
	public void testFindById() {
		Recepteur m = recepteurManager.findByIdManager(1);
		assertNotNull(m);
		
		Recepteur mNull = recepteurManager.findByIdManager(100);
		assertNull(mNull);
	}

	@Test
	public void testFindAll() {
		List<Recepteur> list = recepteurManager.findAllObjectsManager();
		assertTrue(list.size() == 2);
		assertTrue(list.get(1).getIdentification().equals("DME"));
	}

	@Test
	public void testFindByIdinListManager() {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		List<Recepteur> liste = recepteurManager.findByIdinListManager(ids);
		assertTrue(liste.size() == 1);
		assertTrue(liste.get(0).getIdentification().equals("DIAMIC-ACK"));
		
		ids.add(2);
		liste = recepteurManager.findByIdinListManager(ids);
		assertTrue(liste.size() == 2);
		assertTrue(liste.get(0).getIdentification().equals("DIAMIC-ACK"));
		assertTrue(liste.get(1).getIdentification().equals("DME"));
		
		liste = recepteurManager.findByIdinListManager(new ArrayList<Integer>());
		assertTrue(liste.size() == 0);
		
		liste = recepteurManager.findByIdinListManager(null);
		assertTrue(liste.size() == 0);
	}
	
	@Test
	public void testUpdateRecepteurEnvoiNum() {
		assertTrue(recepteurManager.findByIdManager(1).getEnvoiNum() == 1);
		// envoi_num = null
		recepteurManager.updateRecepteurEnvoiNum(recepteurManager.findByIdManager(1), false);
		assertTrue(recepteurManager.findByIdManager(1).getEnvoiNum() == 2);
		recepteurManager.updateRecepteurEnvoiNum(recepteurManager.findByIdManager(1), false);
		assertTrue(recepteurManager.findByIdManager(1).getEnvoiNum() == 3);
		recepteurManager.updateRecepteurEnvoiNum(recepteurManager.findByIdManager(1), false);
		assertTrue(recepteurManager.findByIdManager(1).getEnvoiNum() == 4);

		recepteurManager.updateRecepteurEnvoiNum(null, false);
		
		// remise à null
		recepteurManager.updateRecepteurEnvoiNum(recepteurManager.findByIdManager(1), true);
		assertNull(recepteurManager.findByIdManager(1).getEnvoiNum());
		
		recepteurManager.updateRecepteurEnvoiNum(recepteurManager.findByIdManager(1), false);
		assertTrue(recepteurManager.findByIdManager(1).getEnvoiNum() == 1);
		
	}

}
