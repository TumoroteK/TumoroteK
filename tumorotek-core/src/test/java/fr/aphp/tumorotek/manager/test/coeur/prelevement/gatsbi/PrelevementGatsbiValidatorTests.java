/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.manager.test.coeur.prelevement.gatsbi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.ChampEntite;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;

public class PrelevementGatsbiValidatorTests extends AbstractManagerTest4
{

	private Prelevement p;
	private Contexte contexte;
	
	   @Autowired
	   private PrelevementManager prelevementManager;
	   
	   @Before
	   public void setUp() {
		   contexte = new Contexte();  
		   Banque bank = new Banque();
		   Etude etude = new Etude();
		   etude.addToContextes(contexte);
		   bank.setEtude(etude);
		   p = new Prelevement();
		   p.setBanque(bank);
	   }
	   
	   @Test
	   public void whenGastbiApplies_ThenPrelevementNatureAndConsentTypeAreNotRequiredAnymore() {
		   // given
		   ChampEntite nature = new ChampEntite();
		   nature.setChampId(24);
		   nature.setObligatoire(false);
		   contexte.getChampEntites().add(nature);	   
		   ChampEntite consentType = new ChampEntite();
		   consentType.setChampId(26);
		   consentType.setObligatoire(false);
		   contexte.getChampEntites().add(consentType);	   

		   
		   boolean caught = false;
		   try {
			  prelevementManager
			   	.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		   } catch (RequiredObjectIsNullException re) {
			   caught = true;
		   }
		   assertFalse(caught);
	   }
	   
	   @Test
	   public void whenGastbiApplies_ThenPrelevementNatureMayBeRequired() {
		   // given
		   ChampEntite nature = new ChampEntite();
		   nature.setChampId(24);
		   nature.setObligatoire(true);
		   contexte.getChampEntites().add(nature);
		   
		   boolean caught = false;
		   try {
			  prelevementManager
			   	.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		   } catch (RequiredObjectIsNullException re) {
			   caught = true;
			   assertTrue(re.getRequiredObject().equals("Nature"));
		   }
		   assertTrue(caught);
	   }
	   
	   @Test
	   public void whenGastbiApplies_ThenConsentTypeMayBeRequired() {
		   // given
		   ChampEntite consentType = new ChampEntite();
		   consentType.setChampId(26);
		   consentType.setObligatoire(true);
		   contexte.getChampEntites().add(consentType);	 
		   
		   boolean caught = false;
		   try {
			  prelevementManager
			   	.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		   } catch (RequiredObjectIsNullException re) {
			   caught = true;
			   assertTrue(re.getRequiredObject().equals("ConsentType"));
		   }
		   assertTrue(caught);
	   }
	   

}
