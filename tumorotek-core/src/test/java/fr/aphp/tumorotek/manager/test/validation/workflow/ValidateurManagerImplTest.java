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
package fr.aphp.tumorotek.manager.test.validation.workflow;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import static fr.aphp.tumorotek.model.validation.OperateursLogiques.AND;
import static fr.aphp.tumorotek.model.validation.OperateursLogiques.OR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fr.aphp.tumorotek.dao.validation.NiveauValidationDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.impl.validation.workflow.ResultatValidation;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.test.AbstractInMemoryManagerTest;
import fr.aphp.tumorotek.manager.validation.workflow.NiveauValidationManager;
import fr.aphp.tumorotek.manager.validation.workflow.ValidateurManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.CritereValidation;
import fr.aphp.tumorotek.model.validation.NiveauValidation;
import fr.aphp.tumorotek.model.validation.OperateursComparaison;
import fr.aphp.tumorotek.model.validation.OperateursLogiques;
import fr.aphp.tumorotek.model.validation.Validation;

/**
 * @author Gille Chapelot
 *
 */
@Transactional(readOnly = true)
public class ValidateurManagerImplTest extends AbstractInMemoryManagerTest
{

   @Autowired
   private ValidateurManager validateurManager;

   @Autowired
   private ChampEntiteManager champEntiteManager;

   @Autowired
   private ChampAnnotationManager champAnnotationManager;

   @Autowired
   private PrelevementManager prelevementManager;

   @Autowired
   private EntiteManager entiteManager;

   @Autowired
   private NiveauValidationManager niveauValidationManager;

   @Autowired
   private NiveauValidationDao niveauValidationDao;

   private Prelevement prelevement;

   @Before
   public void setUp(){
      prelevement = prelevementManager.findByIdManager(1);
   }

   @Test
   public void testValidationOKCritereTrue(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere = buildPrelevementCritereTrue();
      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationOKCritereFalse(){

      final NiveauValidation niveauUndefined = niveauValidationManager.findCriticiteLevelUndefined();
      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere = buildPrelevementCritereFalse();
      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));
      assertThat(resultatValidation.getNiveauValidation(), is(equalTo(niveauUndefined)));

   }

   @Test
   public void testValidationNOKCritereTrue(){

      final NiveauValidation niveauError = niveauValidationDao.findById(2);

      final CritereValidation critere = buildPrelevementCritereTrue();
      final Validation validation = buildSingleCritereValidation(niveauError, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));

   }

   @Test
   public void testValidationNOKCritereFalse(){

      final NiveauValidation niveauError = niveauValidationDao.findById(2);

      final CritereValidation critere = buildPrelevementCritereFalse();
      final Validation validation = buildSingleCritereValidation(niveauError, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationCrossEntite(){

      final Patient patient = prelevement.getMaladie().getPatient();

      final Entite entitePatient = entiteManager.findByNomManager(patient.entiteNom()).get(0);

      final ChampEntite champEntitePatientNom = champEntiteManager.findByEntiteAndNomManager(entitePatient, "Nom").get(0);
      final Champ champPatientNom = new Champ(champEntitePatientNom);

      //Critere : nom du patient = nom du patient
      final CritereValidation criterePatient = new CritereValidation();
      criterePatient.setChamp(champPatientNom);
      criterePatient.setOperateur(OperateursComparaison.EGAL);
      criterePatient.setValeurRef(patient.getNom());

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final Validation validation = buildSingleCritereValidation(niveauOk, criterePatient);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationChampRef(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);

      final ChampEntite champEntitePrelevementConforme =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "ConformeArrivee").get(0);
      final Champ champPrelevementConforme = new Champ(champEntitePrelevementConforme);

      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champPrelevementConforme);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setChampRef(champPrelevementConforme);

      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationChampThesaurus(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);
      final Entite natureEntite = entiteManager.findByNomManager("Nature").get(0);

      final ChampEntite champEntitePrelevementNatureId =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "NatureId").get(0);
      final Champ champPrelevementNatureId = new Champ(champEntitePrelevementNatureId);

      final ChampEntite champEntiteNatureNature = champEntiteManager.findByEntiteAndNomManager(natureEntite, "Nom").get(0);
      final Champ champNatureNature = new Champ(champEntiteNatureNature);
      champNatureNature.setChampParent(champPrelevementNatureId);

      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champNatureNature);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setValeurRef("SANG");

      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationChampAnnotation(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final ChampAnnotation champAnnotation = champAnnotationManager.findByIdManager(1);
      final Champ champ = new Champ(champAnnotation);

      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champ);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setValeurRef("Valeur annotation Prélèvement 1");

      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationMultiCritereTrue(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereTrue();

      final Validation validation = buildSimpleValidation(niveauOk, AND, critere1, critere2);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationMultiCritereFalse(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereFalse();

      final Validation validation = buildSimpleValidation(niveauOk, AND, critere1, critere2);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));

   }

   @Test
   public void testPrioriteNiveauValidationError(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();
      final NiveauValidation niveauWarn = niveauValidationDao.findById(3);
      final NiveauValidation niveauError = niveauValidationDao.findById(2);

      final CritereValidation critere1 = buildPrelevementCritereFalse();
      final CritereValidation critere2 = buildPrelevementCritereTrue();
      final CritereValidation critere3 = buildPrelevementCritereTrue();

      final Validation validationError = buildSingleCritereValidation(niveauError, critere1);
      final Validation validationWarn = buildSingleCritereValidation(niveauWarn, critere2);
      final Validation validationOK = buildSingleCritereValidation(niveauOk, critere3);

      final Action action = buildAction(validationError, validationWarn, validationOK);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.getNiveauValidation(), is(equalTo(niveauWarn)));

   }

   @Test
   public void testValidationBranch(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereFalse();

      final Validation validation1 = buildSingleCritereChildValidation(critere1);
      final Validation validation2 = buildSingleCritereChildValidation(critere2);
      final Validation validationMere = buildValidation(niveauOk, OR, validation1, validation2);

      final Action action = buildAction(validationMere);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationCritereBranch(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereTrue();

      final Validation validation = buildSingleCritereChildValidation(critere2);

      final Validation validationMere = new Validation();
      validationMere.setNiveauValidation(niveauOk);
      validationMere.setOperateur(AND);
      validationMere.setCriteres(Arrays.asList(critere1));
      validationMere.setEnfants(new HashSet<>(Arrays.asList(validation)));

      final Action action = buildAction(validationMere);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationActionMultiValidationOK(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereTrue();
      final CritereValidation critere3 = buildPrelevementCritereTrue();
      final CritereValidation critere4 = buildPrelevementCritereTrue();

      final Validation validation1 = buildSimpleValidation(niveauOk, AND, critere1, critere2);
      final Validation validation2 = buildSimpleValidation(niveauOk, AND, critere3, critere4);

      final Action action = buildAction(validation1, validation2);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(true));

   }

   @Test
   public void testValidationActionMultiValidationKO(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critere1 = buildPrelevementCritereTrue();
      final CritereValidation critere2 = buildPrelevementCritereTrue();
      final CritereValidation critere3 = buildPrelevementCritereFalse();
      final CritereValidation critere4 = buildPrelevementCritereFalse();

      final Validation validation1 = buildSimpleValidation(niveauOk, AND, critere1, critere2);
      final Validation validation2 = buildSimpleValidation(niveauOk, AND, critere3, critere4);

      final Action action = buildAction(validation1, validation2);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));

   }

   @Test
   public void testCauses(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final CritereValidation critereOk = buildPrelevementCritereTrue();
      final CritereValidation critereKo = buildPrelevementCritereFalse();

      final Validation validation = buildSimpleValidation(niveauOk, AND, critereOk, critereKo);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.getCauses().size(), is(1));
      assertThat(resultatValidation.getCauses().keySet(), hasItem(critereKo));

   }

   @Test
   public void testAnomalieNiveauOK(){

      final NiveauValidation niveauOk = niveauValidationManager.findCriticiteLevelOk();

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);

      final ChampEntite champEntitePrelevementConforme =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "DatePrelevement").get(0);
      final Champ champPrelevementConforme = new Champ(champEntitePrelevementConforme);

      //Critere : prélèvement conforme à l'arrivée = prélèvement conforme à l'arrivée => toujours true
      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champPrelevementConforme);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setValeurRef("XXXX");

      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));
      assertThat(resultatValidation.getAnomalie().keySet(), hasItem(critere));

   }

   @Test
   public void testAnomalieNiveauNOK(){

      final NiveauValidation niveauOk = niveauValidationDao.findById(2);

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);

      final ChampEntite champEntitePrelevementConforme =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "DatePrelevement").get(0);
      final Champ champPrelevementConforme = new Champ(champEntitePrelevementConforme);

      //Critere : prélèvement conforme à l'arrivée = prélèvement conforme à l'arrivée => toujours true
      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champPrelevementConforme);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setValeurRef("XXXX");

      final Validation validation = buildSingleCritereValidation(niveauOk, critere);
      final Action action = buildAction(validation);

      final ResultatValidation resultatValidation = validateurManager.validerAction(prelevement, action);

      assertThat(resultatValidation.isValide(), is(false));
      assertThat(resultatValidation.getAnomalie().keySet(), hasItem(critere));

   }

   /**
    * Retourne un critère de validation sur le prélèvement dont l'évalutation est toujours <i>true</i>
    * @return
    */
   private CritereValidation buildPrelevementCritereTrue(){

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);

      final ChampEntite champEntitePrelevementConforme =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "ConformeArrivee").get(0);
      final Champ champPrelevementConforme = new Champ(champEntitePrelevementConforme);

      //Critere : prélèvement conforme à l'arrivée = prélèvement conforme à l'arrivée => toujours true
      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champPrelevementConforme);
      critere.setOperateur(OperateursComparaison.EGAL);
      critere.setValeurRef(Boolean.toString(prelevement.getConformeArrivee()));

      return critere;

   }

   /**
    * Retourne un critère de validation sur le prélèvement dont l'évalutation est toujours <i>false</i>
    * @return
    */
   private CritereValidation buildPrelevementCritereFalse(){

      final Entite entitePrelevement = entiteManager.findByNomManager(prelevement.entiteNom()).get(0);

      final ChampEntite champEntitePrelevementConforme =
         champEntiteManager.findByEntiteAndNomManager(entitePrelevement, "ConformeArrivee").get(0);
      final Champ champPrelevementConforme = new Champ(champEntitePrelevementConforme);

      //Critere : prélèvement conforme à l'arrivée != prélèvement conforme à l'arrivée => toujours false
      final CritereValidation critere = new CritereValidation();
      critere.setChamp(champPrelevementConforme);
      critere.setOperateur(OperateursComparaison.DIFFERENT);
      critere.setValeurRef(Boolean.toString(prelevement.getConformeArrivee()));

      return critere;

   }

   /**
    * Construit un objet validation constitué de validations filles
    * @param niveau
    * @param operateur
    * @param children
    * @param criteres
    * @return
    */
   private Validation buildValidation(final NiveauValidation niveau, final OperateursLogiques operateur,
      final Validation... validations){

      final Validation res = new Validation();

      res.setOperateur(operateur);
      res.setNiveauValidation(niveau);
      res.setEnfants(new HashSet<Validation>(Arrays.asList(validations)));
      res.setCriteres(new ArrayList<CritereValidation>());

      return res;

   }

   /**
    * Construit un objet Validation sans validations filles
    * @param operateur
    * @param niveau
    * @param criteres
    * @return
    */
   private Validation buildSimpleValidation(final NiveauValidation niveau, final OperateursLogiques operateur,
      final CritereValidation... criteres){

      final Validation res = new Validation();

      res.setOperateur(operateur);
      res.setNiveauValidation(niveau);
      res.setEnfants(new HashSet<Validation>());
      res.setCriteres(Arrays.asList(criteres));

      return res;

   }

   /**
    * Construit un objet Validation à critère unique
    * @param operateur
    * @param niveau
    * @param criteres
    * @return
    */
   private Validation buildSingleCritereValidation(final NiveauValidation niveau, final CritereValidation critere){
      return buildSimpleValidation(niveau, null, critere);
   }

   /**
    * Construit un objet Validation sans niveau (validation fille)
    * @param operateur
    * @param niveau
    * @param criteres
    * @return
    */
   private Validation buildSingleCritereChildValidation(final CritereValidation critere){
      return buildSingleCritereValidation(null, critere);
   }

   /**
    * Construit un objet Action de type {@link fr.aphp.tumorotek.model.validation.ActionType.ActionType.VALIDATION ActionType.VALIDATION}
    * @param validations
    * @return
    */
   private Action buildAction(final Validation... validations){

      //On n'affecte pas de valeur à typeValidation et entite car ces champs servent uniquement
      //à la récupération des actions en base, ils n'ont pas de rôle fonctionnel dans la validation d'une entité

      final Action res = new Action();
      res.setListValidations(Arrays.asList(validations));

      return res;

   }

}
