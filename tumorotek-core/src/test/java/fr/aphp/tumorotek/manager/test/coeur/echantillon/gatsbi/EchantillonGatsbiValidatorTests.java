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
package fr.aphp.tumorotek.manager.test.coeur.echantillon.gatsbi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;

import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.ChampEntite;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.model.contexte.gatsbi.Etude;

public class EchantillonGatsbiValidatorTests extends AbstractManagerTest4
{

   private Echantillon e;

   private Contexte contexte;

   @Autowired
   private EchantillonManager echantillonManager;

   @Autowired
   private ObjetStatutManager objetStatutManager;

   @Before
   public void setUp(){
      contexte = new Contexte();
      contexte.setContexteType(ContexteType.ECHANTILLON);
      contexte.setNom("test_contexte");
      Banque bank = new Banque();
      Etude etude = new Etude();
      etude.addToContextes(contexte);
      bank.setEtude(etude);
      e = new Echantillon();
      e.setBanque(bank);
      e.setObjetStatut(objetStatutManager.findByIdManager(1));
   }

   @Test
   public void checkRequiredObjectsAndValidate_shouldNotFailIfEchantillonTypeIsNotRequiredAnymore_whenGastbiApplies(){

      // given
      ChampEntite type = new ChampEntite();
      type.setChampEntiteId(58);
      type.setObligatoire(false);
      contexte.getChampEntites().add(type);
      boolean caught = false;

      // when
      try{
         echantillonManager.checkRequiredObjectsAndValidate(e, null, null, null, null, null, null, false, false);
      }catch(RequiredObjectIsNullException re){
         caught = true;
      }

      // then
      assertFalse(caught);
   }

   @Test
   public void checkRequiredObjectsAndValidate_shouldFailIfEchantillonTypeIsRequired_whenGastbiApplies(){
      // given
      ChampEntite type = new ChampEntite();
      type.setChampEntiteId(58);
      type.setObligatoire(true);
      contexte.getChampEntites().add(type);
      boolean caught = false;

      // when
      try{
         echantillonManager.checkRequiredObjectsAndValidate(e, null, null, null, null, null, null, false, false);
      }catch(RequiredObjectIsNullException re){
         caught = true;
         assertTrue(re.getRequiredObject().equals("EchantillonType"));
      }

      // then
      assertTrue(caught);
   }

   @Test
   public void checkRequiredObjectsAndValidate_shouldFailAndThrowErrorForEachRequiredField_whenGastbiApplies(){
      // given
      List<Integer> ids = Arrays.asList(53, 56, 60, 61, 62, 63, 67, 68, 70, 229, 230, 243, 244, 255);
      addChampEntiteAsObligatoireToContexte(ids);
      ValidationException ve = null;
      e.setCode("TESTECH");

      // when 
      try{
         // unite chp id = 63 comme dépendances dans les champs required
         // donc l'erreur va apparaitre deux fois pour ce champs
         echantillonManager.checkRequiredObjectsAndValidate(e, null, null, null, null, null, null, true, false);
      }catch(ValidationException e){
         ve = e;
      }

      // then
      assertTrue(ve != null);
      List<FieldError> errs = ve.getErrors().get(0).getFieldErrors();
      assertTrue(errs.get(0).getField().equals("collaborateur"));
      assertTrue(errs.get(0).getCode().equals("echantillon.collaborateur.empty"));
      assertTrue(errs.get(1).getField().equals("dateStock"));
      assertTrue(errs.get(1).getCode().equals("echantillon.dateStock.empty"));
      assertTrue(errs.get(2).getField().equals("lateralite"));
      assertTrue(errs.get(2).getCode().equals("echantillon.lateralite.empty"));
      assertTrue(errs.get(3).getField().equals("quantite"));
      assertTrue(errs.get(3).getCode().equals("echantillon.quantite.empty"));
      assertTrue(errs.get(4).getField().equals("quantiteInit"));
      assertTrue(errs.get(4).getCode().equals("echantillon.quantiteInit.empty"));
      assertTrue(errs.get(5).getField().equals("quantiteUnite"));
      assertTrue(errs.get(5).getCode().equals("echantillon.quantiteUnite.empty"));
      assertTrue(errs.get(6).getField().equals("delaiCgl"));
      assertTrue(errs.get(6).getCode().equals("echantillon.delaiCgl.empty"));
      assertTrue(errs.get(7).getField().equals("echanQualite"));
      assertTrue(errs.get(7).getCode().equals("echantillon.echanQualite.empty"));
      assertTrue(errs.get(8).getField().equals("modePrepa"));
      assertTrue(errs.get(8).getCode().equals("echantillon.modePrepa.empty"));
      assertTrue(errs.get(9).getField().equals("codesAssignes"));
      assertTrue(errs.get(9).getCode().equals("echantillon.codeOrganes.empty"));
      assertTrue(errs.get(10).getField().equals("codesAssignes"));
      assertTrue(errs.get(10).getCode().equals("echantillon.codeMorphos.empty"));
      assertTrue(errs.get(11).getField().equals("conformeTraitement"));
      assertTrue(errs.get(11).getCode().equals("echantillon.conformeTraitement.empty"));
      assertTrue(errs.get(12).getField().equals("conformeCession"));
      assertTrue(errs.get(12).getCode().equals("echantillon.conformeCession.empty"));
      assertTrue(errs.get(13).getField().equals("crAnapath"));
      assertTrue(errs.get(13).getCode().equals("echantillon.crAnapath.empty"));
      assertTrue(errs.get(14).getField().equals("quantiteUnite"));
      assertTrue(errs.get(14).getCode().equals("echantillon.quantiteUnite.empty"));

      assertTrue(ve.getErrors().get(0).getAllErrors().size() == ids.size() + 1);
   }

   private void addChampEntiteAsObligatoireToContexte(List<Integer> chpIds){
      for(Integer i : chpIds){
         ChampEntite chpE = new ChampEntite();
         chpE.setChampEntiteId(i);
         chpE.setObligatoire(true);
         contexte.getChampEntites().add(chpE);
      }
   }

}
