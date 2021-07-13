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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;

import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.ChampEntite;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
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
		contexte.setType(ContexteType.PRELEVEMENT);
		contexte.setContexteLibelle("test_contexte");
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

		// when
		try {
			prelevementManager
			.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		} catch (RequiredObjectIsNullException re) {
			caught = true;
		}

		// then
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

		// when
		try {
			prelevementManager
			.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		} catch (RequiredObjectIsNullException re) {
			caught = true;
			assertTrue(re.getRequiredObject().equals("Nature"));
		}

		// then
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

		// when
		try {
			prelevementManager
			.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, false, null);
		} catch (RequiredObjectIsNullException re) {
			caught = true;
			assertTrue(re.getRequiredObject().equals("ConsentType"));
		}

		// then
		assertTrue(caught);
	}

	@Test
	public void whenGatsbiApplies_ThenValidation_addErrorForMappedRequiredField() {
		// given
		List<Integer> ids = Arrays.asList(45, 44, 30, 31, 249, 29, 28, 32, 34, 33, 27, 35, 36, 37, 38, 39, 40, 256);
		addChampEntiteAsObligatoireToContexte(ids);  
		ValidationException ve = null;
		p.setCode("TESTPREL");

		// when 
		try {
			prelevementManager
			.checkRequiredObjectsAndValidate(p, null, null, null, null, null, null, null, true, null);
		} catch (ValidationException e) {
			ve = e;
		}

		// then
		assertTrue(ve != null);
		List<FieldError> errs = ve.getErrors().get(0).getFieldErrors();
		assertTrue(errs.get(0).getField().equals("numeroLabo"));
		assertTrue(errs.get(0).getCode().equals("prelevement.numeroLabo.empty"));
		assertTrue(errs.get(1).getField().equals("patientNda"));
		assertTrue(errs.get(1).getCode().equals("prelevement.patientNda.empty"));
		assertTrue(errs.get(2).getField().equals("datePrelevement"));
		assertTrue(errs.get(2).getCode().equals("prelevement.datePrelevement.empty"));
		assertTrue(errs.get(3).getField().equals("prelevementType"));
		assertTrue(errs.get(3).getCode().equals("prelevement.prelevementType.empty"));
		assertTrue(errs.get(4).getField().equals("risques"));
		assertTrue(errs.get(4).getCode().equals("prelevement.risques.empty"));
		assertTrue(errs.get(5).getField().equals("servicePreleveur"));
		assertTrue(errs.get(5).getCode().equals("prelevement.servicePreleveur.empty"));
		assertTrue(errs.get(6).getField().equals("preleveur"));
		assertTrue(errs.get(6).getCode().equals("prelevement.preleveur.empty"));
		assertTrue(errs.get(7).getField().equals("conditType"));
		assertTrue(errs.get(7).getCode().equals("prelevement.conditType.empty"));
		assertTrue(errs.get(8).getField().equals("conditNbr"));
		assertTrue(errs.get(8).getCode().equals("prelevement.conditNbr.empty"));
		assertTrue(errs.get(9).getField().equals("conditMilieu"));
		assertTrue(errs.get(9).getCode().equals("prelevement.conditMilieu.empty"));
		assertTrue(errs.get(10).getField().equals("consentDate"));
		assertTrue(errs.get(10).getCode().equals("prelevement.consentDate.empty"));
		assertTrue(errs.get(11).getField().equals("dateDepart"));
		assertTrue(errs.get(11).getCode().equals("prelevement.dateDepart.empty"));
		assertTrue(errs.get(12).getField().equals("transporteur"));
		assertTrue(errs.get(12).getCode().equals("prelevement.transporteur.empty"));
		assertTrue(errs.get(13).getField().equals("transportTemp"));
		assertTrue(errs.get(13).getCode().equals("prelevement.transportTemp.empty"));
		assertTrue(errs.get(14).getField().equals("dateArrivee"));
		assertTrue(errs.get(14).getCode().equals("prelevement.dateArrivee.empty"));
		assertTrue(errs.get(15).getField().equals("operateur"));
		assertTrue(errs.get(15).getCode().equals("prelevement.operateur.empty"));
		assertTrue(errs.get(16).getField().equals("quantite"));
		assertTrue(errs.get(16).getCode().equals("prelevement.quantite.empty"));
		assertTrue(errs.get(17).getField().equals("conformeArrivee"));
		assertTrue(errs.get(17).getCode().equals("prelevement.conformeArrivee.empty"));

		assertTrue(ve.getErrors().get(0).getAllErrors().size() == ids.size());
	}

	private void addChampEntiteAsObligatoireToContexte(List<Integer> chpIds) {
		for (Integer i : chpIds) {
			ChampEntite chpE = new ChampEntite();
			chpE.setChampId(i);
			chpE.setObligatoire(true);
			contexte.getChampEntites().add(chpE);	
		}
	}


}
