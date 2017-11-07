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
package fr.aphp.tumorotek.manager.test.impression;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.impression.BlocImpressionManager;
import fr.aphp.tumorotek.manager.impression.ChampEntiteBlocManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampEntiteBloc;
import fr.aphp.tumorotek.model.impression.ChampEntiteBlocPK;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 * 
 * Classe de test pour le manager ChampEntiteBlocManager.
 * Classe créée le 30/07/2010.
 * 
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ChampEntiteBlocManagerTest extends AbstractManagerTest4 {
	

	@Autowired
	private ChampEntiteBlocManager champEntiteBlocManager;
	@Autowired
	private BlocImpressionManager blocImpressionManager;
	@Autowired
	private ChampEntiteManager champEntiteManager;
	
	public ChampEntiteBlocManagerTest() {	
	}

	@Test
	public void testFindById() {
		ChampEntite ce1 = champEntiteManager.findByIdManager(2);
		BlocImpression bi1 = blocImpressionManager.findByIdManager(2);
		ChampEntiteBlocPK pk = new ChampEntiteBlocPK(bi1, ce1);
		ChampEntiteBloc ceb = champEntiteBlocManager.findByIdManager(pk);
		assertNotNull(ceb);
		
		ChampEntiteBloc cebNull = champEntiteBlocManager.findByIdManager(null);
		assertNull(cebNull);
	}
	
	/**
	 * Test la méthode findAllObjects.
	 */
	@Test
	public void testFindAll() {
		List<ChampEntiteBloc> list = champEntiteBlocManager
			.findAllObjectsManager();
		assertTrue(list.size() > 0);
	}

	/**
	 * Test la méthode findByBlocManager.
	 */
	@Test
	public void testFindByBlocManager() {
		BlocImpression bi1 = blocImpressionManager.findByIdManager(1);
		List<ChampEntiteBloc> list = champEntiteBlocManager
			.findByBlocManager(bi1);
		assertTrue(list.size() == 3);
		
		BlocImpression bi5 = blocImpressionManager.findByIdManager(5);
		list = champEntiteBlocManager.findByBlocManager(bi5);
		assertTrue(list.size() == 10);
		
		list = champEntiteBlocManager.findByBlocManager(null);
		assertTrue(list.size() == 0);
	}

}
