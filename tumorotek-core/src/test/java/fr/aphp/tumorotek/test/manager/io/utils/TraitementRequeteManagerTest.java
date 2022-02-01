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
package fr.aphp.tumorotek.test.manager.io.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementQueryManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementRequeteManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

public class TraitementRequeteManagerTest extends AbstractManagerTest4
{

   private final boolean testLyon = false;

   @Autowired
   private TraitementRequeteManager traitementRequeteManager;
   @Autowired
   private TraitementQueryManager traitementQueryManager;

   @Autowired
   private MaladieManager maladieManager;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private EntiteManager entiteManager;
   @Autowired
   private CollaborateurDao collaborateurDao;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private ConformiteTypeDao conformiteTypeDao;

   @Autowired
   private ChampEntiteDao champEntiteDao;
   @Autowired
   private ChampAnnotationDao champAnnotationDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ServiceDao serviceDao;
   @Autowired
   private ObjetStatutDao objetStatutDao;

   private List<Maladie> allMaladies = null;
   private List<Prelevement> allPrelevements = null;
   private List<ProdDerive> allDerives = null;

   private List<Maladie> getMaladies(){
      if(allMaladies == null){
         allMaladies = maladieManager.findAllObjectsManager();
      }
      return allMaladies;
   }

   private List<Prelevement> getPrelevements(){
      if(allPrelevements == null){
         allPrelevements = prelevementManager.findAllObjectsManager();
      }
      return allPrelevements;
   }

   //	@Autowired
   //	private List<Echantillon> getEchantillons() {
   //		if (allEchantillons == null) {
   //			allEchantillons = echantillonManager.findAllObjectsManager();
   //		}
   //		return allEchantillons;
   //	}

   private List<ProdDerive> getDerives(){
      if(allDerives == null){
         allDerives = prodDeriveManager.findAllObjectsManager();
      }
      return allDerives;
   }

   //	private List<Cession> getCessions() {
   //		if (allCessions == null) {
   //			allCessions = cessionManager.findAllObjectsManager();
   //		}
   //		return allCessions;
   //	}

   @Test
   public void testAdditionnerListesManagerTest(){

      /* TEST 1. */
      /* On créé 2 sous listes de prelevements. */
      final List<Object> prelevements1 = new ArrayList<>();
      final List<Object> prelevements2 = new ArrayList<>();
      int i = 0;
      for(; i < getPrelevements().size(); i++){
         if(i < getPrelevements().size() / 2){
            prelevements1.add(getPrelevements().get(i));
         }else{
            prelevements2.add(getPrelevements().get(i));
         }
      }

      /* On additionne les listes. */
      List<Object> resultats = traitementRequeteManager.additionnerListesManager(prelevements1, prelevements2);

      final Iterator<Prelevement> itPrelevements = getPrelevements().iterator();
      while(itPrelevements.hasNext()){
         final Prelevement prelevement = itPrelevements.next();
         assertTrue(resultats.contains(prelevement));
      }

      /* TEST 2. */
      /* On créé une liste de maladie et une liste de prélèvements. */
      final List<Object> prelevements = new ArrayList<>();
      final List<Object> maladies = new ArrayList<>();
      i = 0;
      for(; i < getMaladies().size(); i += 2){
         maladies.add(getMaladies().get(i));
      }
      i = 1;
      for(; i < getPrelevements().size(); i += 2){
         prelevements.add(getPrelevements().get(i));
      }
      /* On additionne les listes. (On récupère des prélèvements)*/
      resultats = traitementRequeteManager.additionnerListesManager(maladies, prelevements);
      final List<Object> resultats2 = traitementRequeteManager.additionnerListesManager(prelevements, maladies);
      assertTrue(resultats.containsAll(resultats2));
      assertTrue(resultats2.containsAll(resultats));
   }

   /**
    * Teste la méthode fusionnerListesManager.
    */
   @Test
   public void testFusionnerListesManagerTest(){

      /* TEST 1. */
      /* On créé 2 sous listes de prelevements. */
      final List<Object> prelevements1 = new ArrayList<>();
      final List<Object> prelevements2 = new ArrayList<>();
      int i = 0;
      for(; i < getPrelevements().size(); i++){
         if(i < getPrelevements().size() / 2){
            prelevements1.add(getPrelevements().get(i));
         }else{
            prelevements2.add(getPrelevements().get(i));
         }

      }

      /* On fusionne les listes. */
      List<Object> resultats = traitementRequeteManager.fusionnerListesManager(prelevements1, prelevements2);

      assertEquals(resultats.size(), 0);

      /* On ajoute le premier élément de la 2ème liste dans la 1ère. */
      prelevements1.add(prelevements2.get(0));
      /* On fusionne à nouveau les listes. */
      resultats = traitementRequeteManager.fusionnerListesManager(prelevements1, prelevements2);

      assertEquals(resultats.size(), 1);
      assertEquals(resultats.get(0), prelevements2.get(0));
      List<Object> resultats2 = traitementRequeteManager.fusionnerListesManager(prelevements1, prelevements2);
      assertTrue(resultats.containsAll(resultats2));
      assertTrue(resultats2.containsAll(resultats));

      /* TEST 2. */
      /* On créé une liste de maladie et une liste de prélèvements. */
      final List<Object> prelevements = new ArrayList<>();
      final List<Object> maladies = new ArrayList<>();
      i = 0;
      for(; i < getMaladies().size(); i += 2){
         maladies.add(getMaladies().get(i));
      }
      i = 1;
      for(; i < getPrelevements().size(); i += 2){
         prelevements.add(getPrelevements().get(i));
      }
      /* On fusionne les listes. (On récupère des prélèvements) */
      resultats = traitementRequeteManager.fusionnerListesManager(maladies, prelevements);
      resultats2 = traitementRequeteManager.fusionnerListesManager(prelevements, maladies);
      assertTrue(resultats.containsAll(resultats2));
      assertTrue(resultats2.containsAll(resultats));
   }

   /**
    * Teste la méthode traitementCritereManager.
    */
   @Test
   public void testTraitementCritereManager(){
      String codeP = "PTRA";
      if(testLyon){
         codeP = "TUM1173ARN";
      }

      final List<Banque> banques = IterableUtils.toList(banqueDao.findAll());

      /* On crée un critère. */
      /* On récupère le champEntite correspondant au code derive (79). */
      final ChampEntite codeDerive = champEntiteDao.findById(79);

      final Critere critere = new Critere(new Champ(codeDerive), "like", codeP);
      /* On traite le critère. */
      final List<Object> resultats = traitementRequeteManager.traitementCritereManager(critere, banques, codeP + "%", "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      Iterator<Object> it = resultats.iterator();
      while(it.hasNext()){
         final ProdDerive derive = (ProdDerive) it.next();
         // System.out.println(derive);
         assertTrue(derive.getCode().startsWith(codeP));
      }

      final Critere critere2 = new Critere(new Champ(codeDerive), "not like", codeP);
      /* On traite le critère. */
      final List<Object> resultats2 = traitementRequeteManager.traitementCritereManager(critere2, banques, codeP + "%", "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      it = resultats2.iterator();
      while(it.hasNext()){
         final ProdDerive derive = (ProdDerive) it.next();
         assertFalse(derive.getCode().startsWith(codeP));
      }

      /* On compare tous les résultats obtenus avec la liste de tous les 
       * prélèvements de la BDD. */
      resultats.addAll(resultats2);
      assertTrue(resultats.containsAll(getDerives()));

   }

   @Test
   public void testCritereWithChampParent(){
      final List<Banque> banques = IterableUtils.toList(banqueDao.findAll());
      // on va rechercher les prlvts dont la maladie a un certain code
      final String codeM = "12.56";

      /* On crée un critère. */
      /* On récupère le champEntite correspondant au code de la maladie. */
      final ChampEntite codeMaladie = champEntiteDao.findById(18);
      // On récup le champ MaladieId de la table prélèvement
      final ChampEntite maladieId = champEntiteDao.findById(25);

      Champ parent = new Champ(maladieId);
      Champ critereMaladie = new Champ(codeMaladie);
      critereMaladie.setChampParent(parent);

      Critere critere = new Critere(critereMaladie, "like", codeM + "%");
      /* On traite le critère. */
      final List<Object> resultats = traitementRequeteManager.traitementCritereManager(critere, banques, codeM + "%", "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      final Iterator<Object> it = resultats.iterator();
      while(it.hasNext()){
         final Prelevement prlvt = (Prelevement) it.next();
         assertTrue(prlvt.getMaladie().getCode().startsWith(codeM));
      }

      // on va recherche les patients de cette maladie
      final ChampEntite maladies = champEntiteDao.findById(222);
      parent = new Champ(maladies);
      critereMaladie = new Champ(codeMaladie);
      critereMaladie.setChampParent(parent);
      critere = new Critere(critereMaladie, "like", codeM + "%");
      /* On traite le critère. */
      // récupérer methode Pierre pour ajouter joins à la place de requête 
      // bad HQL de Maxime
      //		resultats 
      //				= traitementRequeteManager.traitementCritereManager(critere,
      //						banques, codeM + "%", "mysql");
      //		/* On vérifie que la liste résultante correspond au critère. */
      //		it = resultats.iterator();
      //		while (it.hasNext()) {
      //			Patient patient = (Patient) it.next();
      //			System.out.println(patient);
      //			assertTrue(patient.getNom().equals("DELPHINO"));
      //		}
   }

   @Test
   public void testCritereWithBanques(){
      String codeP = "%";
      if(testLyon){
         codeP = "TUM1173ARN";
      }

      /* On crée un critère. */
      /* On récupère le champEntite correspondant au code derive (79). */
      final ChampEntite codeDerive = champEntiteDao.findById(79);
      final Banque b1 = banqueDao.findById(1);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);

      final Critere critere = new Critere(new Champ(codeDerive), "like", codeP + "%");
      /* On traite le critère. */
      List<Object> resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, codeP, false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertEquals(3, resultats.size());

      // on ajoute la banque 2 pour avoir tous les dérivés
      final Banque b2 = banqueDao.findById(2);
      banques.add(b2);
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, codeP, false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 4);

      // si la liste est nulle ou vide, on récup tous les dérivés
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, null, codeP, false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 4);
      banques = new ArrayList<>();
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, codeP, false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 4);
   }

   @Test
   public void testMultiParents(){
      // Recherche des prlvts en fonction du nom d'un patient
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);

      final ChampEntite nomPatient = champEntiteDao.findById(3);
      Champ champ = new Champ(nomPatient);
      champ.setChampParent(parentToQueryPatient);
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      Critere critere = new Critere(champ, "like", "DEL" + "%");
      /* On traite le critère. */
      List<Object> resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, "DEL%", false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);

      // Recherche des patients en fct du code des prlvts
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);

      final ChampEntite codePrlvt = champEntiteDao.findById(23);
      champ = new Champ(codePrlvt);
      champ.setChampParent(parentToQueryPrlvt);
      critere = new Critere(champ, "like", "PRLV" + "%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, "PRLV%", false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertEquals(2, resultats.size());

      // Recherche des patients en fonction de leur nom
      champ = new Champ(nomPatient);
      banques.add(b1);
      critere = new Critere(champ, "like", "DEL" + "%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, "DEL%", false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      // Recherche des patients en fct du code des échantillons
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchantillon = new Champ(champEntiteEchantillons);
      parentToQueryEchantillon.setChampParent(parentToQueryPrlvt);

      final ChampEntite codeEchantillon = champEntiteDao.findById(54);
      champ = new Champ(codeEchantillon);
      champ.setChampParent(parentToQueryEchantillon);
      critere = new Critere(champ, "like", "PT" + "%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, "PTR%", false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);
   }

   @Test
   public void testFindIdsFromEntite(){
      // Recherche des prlvts en fonction des ids patient
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);

      final ChampEntite idP = champEntiteDao.findById(1);
      Champ champ = new Champ(idP);
      champ.setChampParent(parentToQueryPatient);
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      Critere critere = new Critere(champ, "in", "");

      final List<Integer> idsPat = new ArrayList<>();
      idsPat.add(1);
      idsPat.add(2);
      idsPat.add(3);
      idsPat.add(4);

      /* On traite le critère. */
      List<Object> resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsPat, true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertEquals(2, resultats.size());
      assertTrue(resultats.contains(1));
      assertTrue(resultats.contains(2));

      // Recherche des patients en fct des ids de prlvts
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);

      final ChampEntite idPrlvt = champEntiteDao.findById(21);
      champ = new Champ(idPrlvt);
      champ.setChampParent(parentToQueryPrlvt);
      critere = new Critere(champ, "in", "");

      final List<Integer> idsPrel = new ArrayList<>();
      idsPrel.add(1);
      idsPrel.add(2);
      idsPrel.add(3);
      idsPrel.add(4);

      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsPrel, true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.contains(1));
      assertTrue(resultats.contains(3));

      // Recherche des patients en fonction de leur ids
      champ = new Champ(idP);
      banques.add(b1);
      critere = new Critere(champ, "in", "");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsPat, true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 4);

      idsPat.clear();
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsPat, true);
      assertTrue(resultats.isEmpty());

      // Recherche des patients en fct du code des échantillons
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchantillon = new Champ(champEntiteEchantillons);
      parentToQueryEchantillon.setChampParent(parentToQueryPrlvt);

      final ChampEntite idE = champEntiteDao.findById(50);
      champ = new Champ(idE);
      champ.setChampParent(parentToQueryEchantillon);
      critere = new Critere(champ, "in", "");

      final List<Integer> idsEchan = new ArrayList<>();
      idsEchan.add(1);
      idsEchan.add(2);
      idsEchan.add(3);
      idsEchan.add(4);

      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsEchan, true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.contains(3));
      assertTrue(resultats.contains(1));

      idsEchan.clear();
      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, idsEchan, true);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findObjetByCritereWithBanquesManager(critere, banques, null, true);
      assertTrue(resultats.isEmpty());
   }

   @Test
   public void testFindIdsFromEntiteDeriveVersion(){
      // Recherche des derives en fonction des ids patients
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);
      // création du parent pour accéder aux prélèvement
      final ChampEntite champEntitePrlvtId = champEntiteDao.findById(52);
      final Champ parentToQueryOnePrlvt = new Champ(champEntitePrlvtId);
      parentToQueryOneMaladie.setChampParent(parentToQueryOnePrlvt);
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchans = champEntiteDao.findById(226);
      final Champ parentToQueryEchans = new Champ(champEntiteEchans);
      parentToQueryOnePrlvt.setChampParent(parentToQueryEchans);

      final ChampEntite idP = champEntiteDao.findById(1);
      Champ champ = new Champ(idP);
      champ.setChampParent(parentToQueryPatient);
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      Critere critere = new Critere(champ, "in", "");

      final List<Integer> idsPat = new ArrayList<>();
      idsPat.add(1);
      idsPat.add(2);
      idsPat.add(3);
      idsPat.add(4);

      /* On traite le critère. */
      List<Object> resultats =
         traitementQueryManager.findObjetByCritereWithBanquesDeriveVersionManager(critere, banques, idsPat, true, true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);
      assertTrue(resultats.contains(1));
      // assertTrue(resultats.contains(2));

      idsPat.clear();
      resultats = traitementQueryManager.findObjetByCritereWithBanquesDeriveVersionManager(critere, banques, idsPat, true, true);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findObjetByCritereWithBanquesDeriveVersionManager(critere, banques, null, true, true);
      assertTrue(resultats.isEmpty());

      // Recherche des patients en fct des ids de derives
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);
      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchan = new Champ(champEntiteEchantillons);
      parentToQueryEchan.setChampParent(parentToQueryPrlvt);
      final ChampEntite champEntiteDerives = champEntiteDao.findById(226);
      final Champ parentToQueryDerive = new Champ(champEntiteDerives);
      parentToQueryDerive.setChampParent(parentToQueryEchan);

      final ChampEntite idDer = champEntiteDao.findById(76);
      champ = new Champ(idDer);
      champ.setChampParent(parentToQueryDerive);
      critere = new Critere(champ, "in", "");

      final List<Integer> idsDer = new ArrayList<>();
      idsDer.add(1);
      idsDer.add(2);
      idsDer.add(4);
      idsDer.add(3);

      resultats = traitementQueryManager.findObjetByCritereWithBanquesDeriveVersionManager(critere, banques, idsDer, false, true);
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.contains(3));
      assertTrue(resultats.contains(1));

      // teste voie par prélèvement);
      final ChampEntite champEntiteDerives2 = champEntiteDao.findById(225);
      final Champ parentToQueryDerive2 = new Champ(champEntiteDerives2);
      parentToQueryDerive2.setChampParent(parentToQueryPrlvt);

      champ.setChampParent(parentToQueryDerive2);

      resultats = traitementQueryManager.findObjetByCritereWithBanquesDeriveVersionManager(critere, banques, idsDer, false, true);
      assertTrue(resultats.size() == 1);
      assertTrue(resultats.contains(3));

   }

   @Test
   public void testMultiCriteres(){
      final List<Critere> criteres = new ArrayList<>();
      final List<Object> values = new ArrayList<>();

      // Recherche des prlvts en fonction du nom d'un patient
      // et du code du prélèvement
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);

      // critere nom patient
      final ChampEntite nomPatient = champEntiteDao.findById(3);
      Champ champ = new Champ(nomPatient);
      champ.setChampParent(parentToQueryPatient);
      final Critere critere1 = new Critere(champ, "like", "DEL" + "%");
      // critere code prelevement
      final ChampEntite codePrlvt = champEntiteDao.findById(23);
      champ = new Champ(codePrlvt);
      champ.setChampParent(null);
      final Critere critere2 = new Critere(champ, "like", "PRLV" + "%");

      // Recherche des patients en fct du code des échantillons
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchantillon = new Champ(champEntiteEchantillons);
      parentToQueryEchantillon.setChampParent(null);
      final ChampEntite codeEchantillon = champEntiteDao.findById(54);
      champ = new Champ(codeEchantillon);
      champ.setChampParent(parentToQueryEchantillon);
      final Critere critere3 = new Critere(champ, "like", "PT" + "%");

      // critere sur une maladie
      final ChampEntite codeMaladie = champEntiteDao.findById(18);
      champ = new Champ(codeMaladie);
      champ.setChampParent(parentToQueryOneMaladie);
      final Critere critere4 = new Critere(champ, "like", "C45" + "%");

      // critere sur un champannotation
      final ChampAnnotation ca = champAnnotationDao.findById(9);
      champ = new Champ(ca);
      champ.setChampParent(parentToQueryEchantillon);
      final Critere critereA = new Critere(champ, "=", "textVal1");

      criteres.add(critere1);
      criteres.add(critere2);
      criteres.add(critere3);
      criteres.add(critere4);
      criteres.add(critereA);
      values.add("DEL%");
      values.add("PRLV%");
      values.add("PT%");
      values.add("C%");
      values.add("textVal1");
      /* On traite le critère. */
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      List<Integer> resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      assertTrue(resultats.size() == 1);

      // Recherche des prlvts en fonction du nom d'un patient
      // création du parent pour accéder aux patients
      champ = new Champ(nomPatient);
      champ.setChampParent(parentToQueryPatient);
      final Critere critere5 = new Critere(champ, "like", "DEL" + "%");
      criteres.clear();
      criteres.add(critere5);
      values.clear();
      values.add("DEL%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);

      // Recherche des patients en fct du code des prlvts
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);

      champ = new Champ(codePrlvt);
      champ.setChampParent(parentToQueryPrlvt);
      final Critere critere6 = new Critere(champ, "like", "PRLV" + "%");
      criteres.clear();
      criteres.add(critere6);
      values.clear();
      values.add("PRLV%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      /* On vérifie que la liste résultante correspond au critère. */
      assertEquals(1, resultats.size());

      // Recherche des patients en fonction de leur nom
      champ = new Champ(nomPatient);
      banques.add(b1);
      final Critere critere7 = new Critere(champ, "like", "DEL" + "%");
      criteres.clear();
      criteres.add(critere7);
      values.clear();
      values.add("DEL%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      // Recherche des patients en fct du code des échantillons
      // création du parent pour accéder aux échantillons
      champ = new Champ(codeEchantillon);
      parentToQueryEchantillon.setChampParent(parentToQueryPrlvt);
      champ.setChampParent(parentToQueryEchantillon);
      final Critere critere8 = new Critere(champ, "like", "PT" + "%");
      criteres.clear();
      criteres.add(critere8);
      values.clear();
      values.add("PT%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);
   }

   @Test
   public void testNumCritereAnnot(){

      final List<Critere> criteres = new ArrayList<>();
      final List<Object> values = new ArrayList<>();

      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchantillon = new Champ(champEntiteEchantillons);
      parentToQueryEchantillon.setChampParent(null);

      // critere sur un champannotation
      final ChampAnnotation ca = champAnnotationDao.findById(7);
      final Champ champ = new Champ(ca);
      champ.setChampParent(parentToQueryEchantillon);
      final Critere critere1 = new Critere(champ, ">", "textVal1");

      criteres.add(critere1);
      values.add(new BigDecimal(123.000));
      /* On traite le critère. */
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      final List<Integer> resultats = traitementQueryManager.findObjetByCriteresWithBanquesManager(criteres, banques, values);
      assertTrue(resultats.isEmpty());
   }

   /*@Test
   public void testProdDeriveQuery() {
   	// Recherche des dérivés en fonction du nom d'un patient
   	// création du parent pour accéder aux patients
   	ChampEntite champEntitePatientId = champEntiteDao.findById(16);
   	Champ parentToQueryPatient = new Champ(champEntitePatientId);
   	// création du parent pour accéder aux maladies
   	ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
   	Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
   	parentToQueryPatient.setChampParent(parentToQueryOneMaladie);
   	// création du parent pour accéder aux prélèvement
   	ChampEntite champEntitePrlvtId = champEntiteDao.findById(52);
   	Champ parentToQueryOnePrlvt = new Champ(champEntitePrlvtId);
   	parentToQueryOneMaladie.setChampParent(parentToQueryOnePrlvt);
   	
   	ChampEntite nomPatient = champEntiteDao.findById(3);
   	Champ champ = new Champ(nomPatient);
   	champ.setChampParent(parentToQueryPatient);
   	List<Banque> banques = new ArrayList<Banque>();
   	Banque b1 = banqueDao.findById(1);
   	banques.add(b1);
   	Critere critere = new Critere(champ, "like", "DEL" + "%");
   	List<Object> resultats 
   			= traitementQueryManager
   			.findObjetByCritereWithBanquesDeriveVersionManager(
   					critere, banques, "DEL%", true);
   	System.out.println(resultats);
   	
   	// Recherche des patients en fct du code des dérivés
   	ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
   	Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
   	// création du parent pour accéder aux prélèvements
   	ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
   	Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
   	parentToQueryPrlvt.setChampParent(parentToQueryMaladie);
   	// création du parent pour accéder aux dérivés
   	ChampEntite champEntiteDerives = champEntiteDao.findById(225);
   	Champ parentToQueryDerives = new Champ(champEntiteDerives);
   	parentToQueryDerives.setChampParent(parentToQueryPrlvt);
   	
   	ChampEntite codeDerive = champEntiteDao.findById(79);
   	champ = new Champ(codeDerive);
   	champ.setChampParent(parentToQueryDerives);
   	critere = new Critere(champ, "like", "PTR" + "%");
   	resultats = traitementQueryManager
   		.findObjetByCritereWithBanquesDeriveVersionManager(
   			critere, banques, "PTR%", false);
   	System.out.println(resultats);
   }*/

   @Test
   public void testProdDeriveQueryWithCriteresList(){
      final List<Critere> criteres = new ArrayList<>();
      final List<Object> values = new ArrayList<>();

      // Recherche des dérivés en fonction du nom d'un patient
      // et du code du prlvt
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);
      // création du parent pour accéder aux prélèvement
      final ChampEntite champEntitePrlvtId = champEntiteDao.findById(52);
      final Champ parentToQueryOnePrlvt = new Champ(champEntitePrlvtId);
      parentToQueryOneMaladie.setChampParent(parentToQueryOnePrlvt);
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchans = champEntiteDao.findById(226);
      final Champ parentToQueryEchans = new Champ(champEntiteEchans);
      parentToQueryOnePrlvt.setChampParent(parentToQueryEchans);

      final ChampEntite nomPatient = champEntiteDao.findById(3);
      Champ champ = new Champ(nomPatient);
      champ.setChampParent(parentToQueryPatient);
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      final Critere critere1 = new Critere(champ, "like", "DEL" + "%");

      final ChampEntite codePrlvt = champEntiteDao.findById(23);
      champ = new Champ(codePrlvt);
      champ.setChampParent(parentToQueryOnePrlvt);
      final Critere critere2 = new Critere(champ, "like", "PRLV" + "%");
      criteres.add(critere1);
      criteres.add(critere2);
      values.add("DEL%");
      values.add("PRLV%");
      /* On traite le critère. */
      List<Integer> resultats =
         traitementQueryManager.findObjetByCriteresWithBanquesDeriveVersionManager(criteres, banques, values, true);

      // Recherche des patients en fct du code des dérivés et du
      // numéro de labo
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);
      // création du parent pour accéder aux dérivés
      final ChampEntite champEntiteDerives = champEntiteDao.findById(225);
      final Champ parentToQueryDerives = new Champ(champEntiteDerives);
      parentToQueryDerives.setChampParent(parentToQueryPrlvt);

      final ChampEntite codeDerive = champEntiteDao.findById(79);
      champ = new Champ(codeDerive);
      champ.setChampParent(parentToQueryDerives);
      final Critere critere3 = new Critere(champ, "like", "PTR" + "%");
      final ChampEntite laboDerive = champEntiteDao.findById(80);
      champ = new Champ(laboDerive);
      champ.setChampParent(parentToQueryDerives);
      final Critere critere4 = new Critere(champ, "like", "PTR" + "%");
      criteres.clear();
      values.clear();
      criteres.add(critere3);
      criteres.add(critere4);
      values.add("PT%");
      values.add("LABO%");
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCriteresWithBanquesDeriveVersionManager(criteres, banques, values, false);
      assertTrue(!resultats.isEmpty());
      //System.out.println(resultats);
   }

   /**
    * Teste la méthode traitementArbreGroupementManager.
    */
   //	@Test
   //	public void testTraitementArbreGroupementManager() {
   //		List<Banque> banques = IterableUtils.toList(banqueDao.findAll());
   //		String codeP1 = "PRLVT1";
   //		String codeP2 = "PRLVT3";
   //		if (testLyon) {
   //			codeP1 = "TUM93";
   //			codeP2 = "PDM13LY";
   //		}
   //		Hashtable<Critere, Object> criteresvalues = 
   //			new Hashtable<Critere, Object>();
   //		
   //		/* TEST 1 : Groupement simple. */
   //		System.out.println("TEST 1 : Groupement simple.");
   //		
   //		/* On crée un critère. */
   //		/* On récupère le champEntite correspondant au code prelevement (23). */
   //		ChampEntite codePrelevement = champEntiteDao.findById(23);
   //		/* On crée les critères du groupement. */
   //		Critere critere1 = new Critere(new Champ(codePrelevement),
   //				"like", codeP1);
   //		Critere critere2 = new Critere(new Champ(codePrelevement),
   //				"like", codeP2);
   //		criteresvalues.put(critere1, codeP1);
   //		criteresvalues.put(critere2, codeP2);
   //		/* On crée le groupement. */
   //		Groupement groupement = new Groupement(critere1, critere2,
   //				ConstanteIO.CONDITION_OU, null);
   //		
   //		/* On traite le groupement. */
   //		List<Object> resultats = traitementRequeteManager
   //				.traitementArbreGroupementManager(groupement,
   //						banques, criteresvalues);
   //		assertTrue(resultats.size() > 0);
   //		/* On vérifie que la liste résultante correspond au critère. */
   //		Iterator<Object> it = resultats.iterator();
   //		while (it.hasNext()) {
   //			Prelevement prelevement = (Prelevement) it.next();
   //			System.out.println(prelevement);
   //			assertTrue(prelevement.getCode().equals(codeP1)
   //					|| prelevement.getCode().equals(codeP2));
   //		}
   //		
   //		/* TEST 2 : Arbre de groupements. */
   //		String idP = "3";
   //		if (testLyon) {
   //			idP = "855";
   //		}
   //		System.out.println("TEST 2 : Arbre de groupements.");
   //		
   //		/* On récupère le champEntite correspondant à id patient (1). */
   //		ChampEntite idPatient = champEntiteDao.findById(1);
   //		/* On crée un arbre de groupement. */
   //		Critere critere4 = new Critere(new Champ(idPatient),
   //				"<=", idP);
   //		
   //		criteresvalues.put(critere4, 855);
   //		Groupement racine = new Groupement(null, critere4,
   //				ConstanteIO.CONDITION_ET, null);
   //		groupement = new Groupement(critere1, critere2,
   //				ConstanteIO.CONDITION_OU, racine);
   //		/* On enregistre le groupement racine pour pouvoir l'explorer. */
   //		groupementManager.saveManager(groupement, groupement
   //				.getCritere1(), groupement.getCritere2(), groupement
   //				.getOperateur(), groupement.getParent());
   //		
   //		/* On traite le groupement. */
   //		resultats = traitementRequeteManager
   //				.traitementArbreGroupementManager(racine,
   //						banques, criteresvalues);
   //		assertTrue(resultats.size() > 0);
   //		/* On vérifie que la liste résultante correspond au critère. */
   //		it = resultats.iterator();
   //		while (it.hasNext()) {
   //			Prelevement prelevement = (Prelevement) it.next();
   //			System.out.println(prelevement);
   //			assertTrue(prelevement.getCode().equals(codeP1)
   //					|| prelevement.getCode().equals(codeP2));
   //			assertTrue(prelevement.getMaladie().getPatient().getPatientId()
   //					<= Integer.parseInt(idP));
   //		}
   //		
   //		/* On supprime les éléments créés. */
   //		groupementManager.removeObjectManager(racine);
   //		
   //		/* TEST 3 : Requête uni critère. */
   //		System.out.println("TEST 3 : Requête uni critère.");
   //		
   //		String codeP3 = "PRLVT";
   //		if (testLyon) {
   //			codeP3 = "TUM132";
   //		}
   //		
   //		/* On crée un arbre de groupement. */
   //		Critere critere5 = new Critere(new Champ(codePrelevement),
   //				"like", codeP3 + "%");
   //		criteresvalues.put(critere5, codeP3 + "%");
   //		
   //		Groupement groupement3 = new Groupement(critere5, null,
   //				ConstanteIO.CONDITION_ET, null);
   //		
   //		/* On enregistre le groupement racine pour pouvoir l'explorer. */
   //		groupementManager.saveManager(groupement3, groupement3
   //				.getCritere1(), groupement3.getCritere2(), groupement3
   //				.getOperateur(), groupement3.getParent());
   //		
   //		/* On traite le groupement. */
   //		resultats = traitementRequeteManager
   //				.traitementArbreGroupementManager(groupement3,
   //						banques, criteresvalues);
   //		assertTrue(resultats.size() > 0);
   //		/* On vérifie que la liste résultante correspond au critère. */
   //		it = resultats.iterator();
   //		while (it.hasNext()) {
   //			Prelevement prelevement = (Prelevement) it.next();
   //			System.out.println(prelevement);
   //			assertTrue(prelevement.getCode().startsWith(codeP3));
   //		}
   //		
   //		/* On supprime les éléments créés. */
   //		groupementManager.removeObjectManager(groupement3);
   //		
   //		
   //		/* TEST 4 : Requête dérivés. */
   //		System.out.println("TEST 4 : Requête dérivés.");
   //		
   //		String codeD = "PTRA.1.2";
   //		String codeP4 = "PRLVT1";
   //		if (testLyon) { 
   //			codeD = "%321ARN%";
   //			codeP4 = "TUM321";
   //		}
   //		
   //		/* On récupère le champEntite correspondant au code dérivé (79). */
   //		ChampEntite codeDerive = champEntiteDao.findById(79);
   //		
   //		/* On crée un arbre de groupement. */
   //		Critere critere6 = new Critere(new Champ(codeDerive),
   //				"like", codeD);
   //		
   //		/* On crée un arbre de groupement. */
   //		Critere critere7 = new Critere(new Champ(codePrelevement),
   //				"like", codeP4);
   //		criteresvalues.put(critere6, codeD);
   //		criteresvalues.put(critere7, codeP4);
   //		
   //		Groupement groupement4 = new Groupement(critere6, critere7,
   //				ConstanteIO.CONDITION_ET, null);
   //		
   //		/* On enregistre le groupement racine pour pouvoir l'explorer. */
   //		groupementManager.saveManager(groupement4, groupement4
   //				.getCritere1(), groupement4.getCritere2(), groupement4
   //				.getOperateur(), groupement4.getParent());
   //		
   //		/* On traite le groupement. */
   //		resultats = traitementRequeteManager
   //				.traitementArbreGroupementManager(groupement4,
   //						banques, criteresvalues);
   //		assertTrue(resultats.size() > 0);
   //		/* On vérifie que la liste résultante correspond au critère. */
   //		it = resultats.iterator();
   //		while (it.hasNext()) {
   //			ProdDerive prodDerive = (ProdDerive) it.next();			
   //			Object obj = prodDerive;
   //			boolean found = false;
   //			do {
   //				System.out.println(obj);		
   //				/* On récupère l'objet parent. */
   //				if (obj.getClass().getSimpleName().equals("ProdDerive")) {
   //					obj = prodDeriveManager.findParent(
   //							(ProdDerive) obj);
   //				} else if (obj.getClass().getSimpleName()
   //						.equals("Echantillon")) {
   //					obj = prelevementManager.findByIdManager(((Echantillon) obj)
   //							.getPrelevement().getPrelevementId());
   //				} else if (obj.getClass().getSimpleName()
   //						.equals("Prelevement")) {
   //					found = true;
   //				}
   //			} while (!found);
   //			assertTrue(((Prelevement) obj).getCode().equals(codeP4));
   //		}
   //		
   //		/* On supprime les éléments créés. */
   //		groupementManager.removeObjectManager(groupement4);
   //
   //		/* TEST 5 : Requête avec date. */
   //		System.out.println("TEST 5 : Requête avec date.");
   //
   //		/*
   //		 * On récupère le champEntite correspondant à la date du consentement
   //		 * (27).
   //		 */
   //		ChampEntite consentDate = champEntiteDao.findById(27);
   //
   //		/* On crée un arbre de groupement. */
   //		Critere critere8 = new Critere(new Champ(consentDate),
   //				"<", "1983-09-10");
   //		Date date = null;
   //		try {
   //			date = new SimpleDateFormat("dd/MM/yyyy")
   //				.parse("10/09/1983");
   //		} catch (ParseException e) {
   //			e.printStackTrace();
   //		}
   //		criteresvalues.put(critere8, date);
   //
   //		Groupement groupement5 = new Groupement(critere8, null,
   //				ConstanteIO.CONDITION_OU, null);
   //
   //		/* On enregistre le groupement racine pour pouvoir l'explorer. */
   //		groupementManager.saveManager(groupement5, groupement5
   //				.getCritere1(), groupement5.getCritere2(), groupement5
   //				.getOperateur(), groupement5.getParent());
   //
   //		/* On traite le groupement. */
   //		resultats = traitementRequeteManager
   //				.traitementArbreGroupementManager(groupement5,
   //						banques, criteresvalues);
   //		assertTrue(resultats.size() > 0);
   //		/* On vérifie que la liste résultante correspond au critère. */
   //		it = resultats.iterator();
   //		while (it.hasNext()) {
   //			Prelevement prelevement = (Prelevement) it.next();
   //			System.out.println(prelevement + " - consentDate : "
   //					+ prelevement.getConsentDate());
   //			assertTrue(prelevement.getConsentDate()
   //					.before(new java.util.Date(1983, 8, 10)));
   //		}
   //
   //		/* On supprime les éléments créés. */
   //		groupementManager.removeObjectManager(groupement5);
   //		
   //		if (testLyon) {
   //			/* TEST 6 : Champ annotation. */
   //			System.out.println("TEST 6 : ChampAnnotations.");
   //
   //			ChampAnnotation champAnno = champAnnotationDao.findById(72);
   //			
   //			ChampEntite champEchantillonCode = champEntiteDao.findById(54);
   //
   //			/* On crée un arbre de groupement. */
   //			Critere critere9 = new Critere(new Champ(champAnno), "=", "0");
   //			Critere critere10 = new Critere(new Champ(champEchantillonCode),
   //					"like", "TUM132%");
   //			criteresvalues.put(critere9, 0);
   //			criteresvalues.put(critere10, "TUM132%");
   //			
   //			Groupement groupement6 = new Groupement(critere9, critere5,
   //					ConstanteIO.CONDITION_ET, null);
   //			
   //			Groupement groupement7 = new Groupement(critere10, null,
   //					ConstanteIO.CONDITION_ET, groupement6);
   //			
   //			/* On enregistre le groupement racine pour pouvoir l'explorer. */
   //			groupementManager.saveManager(groupement7, groupement7
   //					.getCritere1(), groupement7.getCritere2(), groupement7
   //					.getOperateur(), groupement7.getParent());
   //			
   //			/* On traite le groupement. */
   //			resultats = traitementRequeteManager
   //					.traitementArbreGroupementManager(groupement7,
   //							banques, criteresvalues);
   //			assertTrue(resultats.size() > 0);
   //			/* On vérifie que la liste résultante correspond au critère. */
   //			it = resultats.iterator();
   //			while (it.hasNext()) {
   //				Echantillon echantillon = (Echantillon) it.next();
   //				System.out.println(echantillon);
   //				//assertTrue();
   //			}
   //			
   //			/* On supprime les éléments créés. */
   //			groupementManager.removeObjectManager(groupement7);
   //		}
   //	}

   @Test
   public void testFindPrelevementsByNbEchantillonsWithBanquesManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);

      List<Integer> liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", 2, banques);
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", 1, banques);
      assertTrue(liste.size() == 1);

      final Banque b2 = banqueDao.findById(2);
      banques.add(b2);
      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", 1, banques);
      assertTrue(liste.size() == 1);

      // mauvais paramétrage
      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager(null, 2, banques);
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("", 2, banques);
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", null, banques);
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", -1, banques);
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", 2, null);
      assertTrue(liste.size() == 0);
      banques = new ArrayList<>();
      liste = traitementQueryManager.findPrelevementsByNbEchantillonsWithBanquesManager("=", 2, banques);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindPrelevementsByAgePatientWithBanquesManager(){
      final Banque b2 = banqueDao.findById(2);
      List<Banque> banques = new ArrayList<>();
      banques.add(b2);

      List<Integer> liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("<", 12, banques, "mysql");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("<", 11, banques, "mysql");
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager(">", 10, banques, "mysql");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager(">", 10, banques, "mysql");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", 11, banques, "mysql");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);
      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", 12, banques, "mysql");
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("!=", 12, banques, "mysql");
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      // mauvais paramétrage
      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager(null, 2, banques, "mysql");
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("", 2, banques, "mysql");
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", null, banques, "mysql");
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", -1, banques, "mysql");
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", 2, null, "mysql");
      assertTrue(liste.size() == 0);
      banques = new ArrayList<>();
      liste = traitementQueryManager.findPrelevementsByAgePatientWithBanquesManager("=", 2, banques, "mysql");
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testCodesInList(){
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      final List<String> values = new ArrayList<>();
      values.add("BL");

      // Recherche des prlvts en fonction du nom d'un patient
      // création du parent pour accéder aux patients
      final ChampEntite champEntitePatientId = champEntiteDao.findById(16);
      final Champ parentToQueryPatient = new Champ(champEntitePatientId);
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladieId = champEntiteDao.findById(25);
      final Champ parentToQueryOneMaladie = new Champ(champEntiteMaladieId);
      parentToQueryPatient.setChampParent(parentToQueryOneMaladie);

      // Recherche des patients en fct du code des prlvts
      // création du parent pour accéder aux maladies
      final ChampEntite champEntiteMaladies = champEntiteDao.findById(222);
      final Champ parentToQueryMaladie = new Champ(champEntiteMaladies);
      // création du parent pour accéder aux prélèvements
      final ChampEntite champEntitePrelevements = champEntiteDao.findById(223);
      final Champ parentToQueryPrlvt = new Champ(champEntitePrelevements);
      parentToQueryPrlvt.setChampParent(parentToQueryMaladie);

      // Recherche des patients en fct du code des échantillons
      // création du parent pour accéder aux échantillons
      final ChampEntite champEntiteEchantillons = champEntiteDao.findById(224);
      final Champ parentToQueryEchantillon = new Champ(champEntiteEchantillons);
      parentToQueryEchantillon.setChampParent(parentToQueryPrlvt);

      // Recherche des des échantillons
      final ChampEntite champEntiteCodeOrganes = champEntiteDao.findById(241);
      final Champ parentToQueryOrganes = new Champ(champEntiteCodeOrganes);
      parentToQueryOrganes.setChampParent(parentToQueryEchantillon);

      final ChampEntite codeOrgane = champEntiteDao.findById(125);
      Champ champ = new Champ(codeOrgane);
      champ.setChampParent(parentToQueryOrganes);

      Critere critere = new Critere(champ, "like", "PT" + "%");
      /* On traite le critère. */
      List<Integer> resultats =
         traitementQueryManager.findObjetByCritereOnCodesWithBanquesManager(critere, banques, values, values, "BL", false);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      /* Même parents mais avec des codes morphos. */
      final ChampEntite champEntiteCodeMorphos = champEntiteDao.findById(241);
      final Champ parentToQueryMorphos = new Champ(champEntiteCodeMorphos);
      parentToQueryMorphos.setChampParent(parentToQueryEchantillon);

      final ChampEntite codeMorpho = champEntiteDao.findById(231);
      champ = new Champ(codeMorpho);
      champ.setChampParent(parentToQueryMorphos);

      critere = new Critere(champ, "like", "PT" + "%");
      /* On traite le critère. */
      resultats =
         traitementQueryManager.findObjetByCritereOnCodesWithBanquesManager(critere, banques, values, values, "K14%", true);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);
   }

   @Test
   public void testCodesInListForDerives(){
      final List<Banque> banques = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      banques.add(b1);
      final List<String> values = new ArrayList<>();
      values.add("BL");
      final Entite echanEntite = entiteManager.findByNomManager("Echantillon").get(0);

      /* On traite le critère. */
      List<Integer> resultats = traitementQueryManager.findObjetByCritereOnCodesWithBanquesDerivesVersionManager(banques, values,
         values, "", false, echanEntite);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereOnCodesWithBanquesDerivesVersionManager(banques, values, values,
         "K14%", true, echanEntite);
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);
   }

   @Test
   public void testFindEchantillonsByRequeteBiocapManager() throws ParseException{
      final Banque b2 = banqueDao.findById(2);
      List<Banque> banques = new ArrayList<>();
      banques.add(b2);
      final Service s1 = serviceDao.findById(1);
      List<Service> services = new ArrayList<>();
      services.add(s1);

      final Calendar dateInf = Calendar.getInstance();
      dateInf.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("17/09/1983 00:00:00"));
      final Calendar dateSup = Calendar.getInstance();
      dateSup.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("20/09/1983 00:00:00"));

      // Requête ok
      List<Integer> liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12, null);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 4);

      // restriction statut
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12,
         objetStatutDao.findById(1));
      assertTrue(liste.isEmpty());
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 1);

      // service empty
      services.clear();
      services.add(new Service());
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);

      // aucun service - enlève restriction sur service
      // since 2.0.13
      services.clear();
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 1);

      // Erreur sur l'age
      liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 11, null);
      assertTrue(liste.size() == 0);

      // Erreur sur la date inf
      dateInf.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("18/09/1983 00:00:00"));
      liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 18, null);
      assertTrue(liste.size() == 0);

      // Erreur sur la date sup
      dateInf.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("10/09/1983 00:00:00"));
      dateSup.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("15/09/1983 00:00:00"));
      liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 18, null);
      assertTrue(liste.size() == 0);

      // Erreur sur les services
      dateInf.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("10/09/1983 00:00:00"));
      dateSup.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("25/09/1983 00:00:00"));
      // services = new ArrayList<Service>();
      services.add(serviceDao.findById(2));
      liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 18, null);
      assertTrue(liste.size() == 0);

      // Erreur sur les banques
      services = new ArrayList<>();
      services.add(serviceDao.findById(1));
      banques = new ArrayList<>();
      banques.add(banqueDao.findById(1));
      liste =
         traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 18, null);
      assertTrue(liste.size() == 0);

      // mauvais paramétrage
      // on vérifie tout d'abord que les params sont bons		
      banques = new ArrayList<>();
      banques.add(banqueDao.findById(2));
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 12,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 1);

      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, null,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, dateSup, 0,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", null, services, dateInf, dateSup, 18,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", new ArrayList<Banque>(), services, dateInf,
         dateSup, 18, objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, null, dateInf, dateSup, 18,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);
      banques = new ArrayList<>();
      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, new ArrayList<Service>(), dateInf,
         dateSup, 18, objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, null, dateSup, 18,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);

      liste = traitementQueryManager.findEchantillonsByRequeteBiocapManager("mysql", banques, services, dateInf, null, 18,
         objetStatutDao.findById(3));
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindPrelevementsByMedecinsManager(){
      final Banque b1 = banqueDao.findById(1);
      List<Banque> banques = new ArrayList<>();
      banques.add(b1);
      Collaborateur c = collaborateurDao.findById(1);

      List<Integer> liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertEquals(0, liste.size());

      banques.add(banqueDao.findById(2));
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      c = collaborateurDao.findById(2);
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      c = collaborateurDao.findById(4);
      banques.add(banqueDao.findById(2));
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertTrue(liste.size() == 0);

      c = collaborateurDao.findById(3);
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertTrue(liste.size() == 1);
      assertTrue(liste.get(0) == 3);

      // mauvais paramétrage
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(null, banques);
      assertTrue(liste.size() == 0);
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, null);
      assertTrue(liste.size() == 0);
      banques = new ArrayList<>();
      liste = traitementQueryManager.findPrelevementsByMedecinsManager(c, banques);
      assertTrue(liste.size() == 0);
   }

   @Test
   public void testFindObjetIdsFromNonConformiteNomManager(){
      List<Integer> ids;
      final Plateforme pf1 = plateformeDao.findById(1);
      final ConformiteType c2 = conformiteTypeDao.findById(2);
      final List<Banque> banks = new ArrayList<>();

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Prob", c2, pf1, banks);
      assertEquals(1, ids.size());
      assertTrue(ids.contains(3));

      banks.add(banqueDao.findById(2));
      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Problème", c2, pf1, banks);
      assertTrue(ids.isEmpty());

      banks.add(banqueDao.findById(1));
      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Problème", c2, pf1, null);
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(3));

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Stérile", c2, pf1, banks);
      assertTrue(ids.isEmpty());

      final ConformiteType c3 = conformiteTypeDao.findById(3);
      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Non", c3, pf1, banks);
      assertTrue(ids.size() == 2);
      assertTrue(ids.contains(3));
      assertTrue(ids.contains(2));

      final ConformiteType c5 = conformiteTypeDao.findById(5);
      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Non", c5, pf1, banks);
      assertTrue(ids.size() == 1);
      assertTrue(ids.contains(3));

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Non", c2, pf1, banks);
      assertTrue(ids.isEmpty());

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("", c2, pf1, banks);
      assertTrue(ids.isEmpty());

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager(null, c2, pf1, banks);
      assertTrue(ids.isEmpty());

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Problème", null, pf1, banks);
      assertTrue(ids.isEmpty());

      ids = traitementQueryManager.findObjetIdsFromNonConformiteNomManager("Problème", c2, null, banks);
      assertTrue(ids.isEmpty());
   }

   @Test
   public void testNoConfCritereWithBanques(){
      final String ncf = "Problème%";

      /* On crée un critère. */
      /* On récupère le champEntite correspondant à raison conformité arrivée (257). */
      final ChampEntite raisonNc = champEntiteDao.findById(257);
      final Banque b1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(b1);

      final Critere critere = new Critere(new Champ(raisonNc), "like", ncf);
      /* On traite le critère. */
      List<Object> resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, ncf, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      // on ajoute la banque 2 pour avoir tous les prélèvements
      final Banque b2 = banqueDao.findById(2);
      banques.add(b2);
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, ncf, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      // si la liste est nulle ou vide, on récup tous les prélèvements
      resultats = traitementQueryManager.findObjetByCritereManager(critere, null, ncf, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 1);

      // is null
      critere.setOperateur("is null");
      critere.setValeur(null);
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, null, "mysql");
      assertEquals(3, resultats.size());
      banques.add(banqueDao.findById(3));
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, null, "mysql");
      assertTrue(resultats.size() == 4);

      // is null derives
      critere.getChamp().setChampEntite(champEntiteDao.findById(262));
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, null, "mysql");
      assertTrue(resultats.size() == 2);

      banques.clear();
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, ncf, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);

      // empty resultat
      critere.setOperateur("like");
      critere.setValeur(ncf);
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, "erreuzzzr%", "mysql");
      assertTrue(resultats.size() == 0);
   }

   @Test
   public void testTempStockCritereWithBanques(){
      Float temp = new Float(-20.0);

      /* Echantillons */
      /* On récupère le champEntite correspondant à temp stock échantillon (266). */
      final ChampEntite tempStockE = champEntiteDao.findById(265);
      final Banque b1 = banqueDao.findById(1);
      final List<Banque> banques = new ArrayList<>();
      banques.add(b1);

      Critere critere = new Critere(new Champ(tempStockE), "<", temp.toString());
      /* On traite le critère. */
      List<Object> resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, temp, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertEquals(1, resultats.size());
      assertTrue(resultats.get(0) instanceof Echantillon);

      temp = new Float(-75.0);

      /* ProdDerive */
      /* On récupère le champEntite correspondant à temp stock dérivé (266). */
      final ChampEntite tempStockD = champEntiteDao.findById(266);

      critere = new Critere(new Champ(tempStockD), "=", temp.toString());
      /* On traite le critère. */
      resultats = traitementQueryManager.findObjetByCritereManager(critere, banques, temp, "mysql");
      /* On vérifie que la liste résultante correspond au critère. */
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.get(0) instanceof ProdDerive);
      assertTrue(resultats.get(1) instanceof ProdDerive);
   }
}
