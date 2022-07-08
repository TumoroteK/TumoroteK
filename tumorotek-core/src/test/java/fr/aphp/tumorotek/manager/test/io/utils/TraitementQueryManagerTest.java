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
package fr.aphp.tumorotek.manager.test.io.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.utils.TraitementQueryManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.systeme.Entite;

public class TraitementQueryManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TraitementQueryManager traitementQueryManager;

   @Autowired
   private ChampEntiteManager champEntiteManager;

   @Autowired
   private EntiteManager entiteManager;

   @Autowired
   private BanqueManager banqueManager;

   @Autowired
   private ChampAnnotationManager champAnnotationManager;

   @Autowired
   private ItemDao itemDao;

   @Autowired
   private EtablissementDao etablissementDao;

   @Autowired
   private ServiceDao serviceDao;

   @Autowired
   private CollaborateurDao collaborateurDao;

   @Test
   public void testfindFileUploadedManager(){
      final List<Integer> res = new ArrayList<Integer>();

      res.addAll(traitementQueryManager.findFileUploadedManager(null, null, null, false));
      assertTrue(res.isEmpty());

      // /////////////////////////////////////////////////////////////////////////////:
      // Champ Entite cr anapath
      final Champ crAnapath = new Champ(champEntiteManager.findByIdManager(255));
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, null, null, false));
      assertTrue(res.isEmpty());

      Entite targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, null, true));
      assertTrue(res.isEmpty());

      final List<Banque> banques = new ArrayList<>();
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.isEmpty());

      banques.addAll(banqueManager.findAllObjectsManager());
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.size() == 3);
      assertTrue(res.contains(1));
      assertTrue(res.contains(2));
      assertTrue(res.contains(3));

      banques.remove(banqueManager.findByIdManager(1));
      res.clear();
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.isEmpty());

      // target prelevement
      banques.add(banqueManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(2);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.size() == 2);
      assertTrue(res.contains(1));
      assertTrue(res.contains(2));
      res.clear();

      // target patient
      targetE = entiteManager.findByIdManager(1);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(3));
      res.clear();

      // target derive
      targetE = entiteManager.findByIdManager(8);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, false));
      assertTrue(res.size() == 2);
      assertTrue(res.contains(1));
      assertTrue(res.contains(3));
      res.clear();

      // /////////////////////////////////////////////////////////////////////////////:
      // cr anapath empty
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(4));
      res.clear();

      banques.remove(banqueManager.findByIdManager(1));
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(4));
      res.clear();

      // target prelevement
      banques.add(banqueManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(2);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(3));
      res.clear();

      // target patient
      targetE = entiteManager.findByIdManager(1);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(1));
      res.clear();

      // target derive
      targetE = entiteManager.findByIdManager(8);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnapath, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(4));
      res.clear();

      // /////////////////////////////////////////////////////////////////////////////:
      // Champ annotation fichier
      Champ crAnno = new Champ(champAnnotationManager.findByIdManager(14));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(1));

      banques.remove(banqueManager.findByIdManager(1));
      res.clear();
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.isEmpty());

      // target prelevement
      banques.add(banqueManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(2);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(1));
      res.clear();

      // target patient
      targetE = entiteManager.findByIdManager(1);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(3));
      res.clear();

      // target derive
      targetE = entiteManager.findByIdManager(8);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.size() == 2);
      assertTrue(res.contains(1));
      assertTrue(res.contains(3));
      res.clear();

      // champAnno file mais pas valeur
      crAnno = new Champ(champAnnotationManager.findByIdManager(13));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.isEmpty());

      // champAnno non file 
      crAnno = new Champ(champAnnotationManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, false));
      assertTrue(res.isEmpty());

      // /////////////////////////////////////////////////////////////////////////////:
      // Champ Entite cr anapath empty
      crAnno = new Champ(champAnnotationManager.findByIdManager(14));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 3);
      assertTrue(res.contains(2));
      assertTrue(res.contains(3));
      assertTrue(res.contains(4));

      banques.remove(banqueManager.findByIdManager(1));
      res.clear();
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(4));
      res.clear();

      // target prelevement
      banques.add(banqueManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(2);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 3);
      assertTrue(res.contains(1));
      assertTrue(res.contains(2));
      assertTrue(res.contains(3));
      res.clear();

      // target patient
      targetE = entiteManager.findByIdManager(1);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 2);
      assertTrue(res.contains(1));
      assertTrue(res.contains(3));
      res.clear();

      // target derive
      targetE = entiteManager.findByIdManager(8);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 1);
      assertTrue(res.contains(4));
      res.clear();

      // champAnno file mais pas valeur
      crAnno = new Champ(champAnnotationManager.findByIdManager(13));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 4);
      res.clear();

      // champAnno non file 
      crAnno = new Champ(champAnnotationManager.findByIdManager(1));
      targetE = entiteManager.findByIdManager(3);
      res.addAll(traitementQueryManager.findFileUploadedManager(crAnno, targetE, banques, true));
      assertTrue(res.size() == 4);
      res.clear();
   }

   @Test
   public void findTKStockableObjectsByTempStockWithBanquesManager(){
      Float temp = new Float(-20.0);

      final Banque b1 = banqueManager.findByIdManager(1);
      final List<Banque> banques = new ArrayList<>();

      // nulls
      List<? extends Object> resultats =
         traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(null, temp, "<", banques, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, null, banques, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "", banques, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, ">", null, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, ">", banques, false);
      assertTrue(resultats.isEmpty());

      // Echantillons
      banques.add(b1);
      // null temp
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         null, "<", banques, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "<", banques, false);
      assertTrue(resultats.size() == 1);
      assertTrue(resultats.get(0) instanceof Echantillon);

      // ids
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "<", banques, true);
      assertTrue(resultats.size() == 1);
      assertTrue(resultats.get(0) instanceof Integer);
      assertTrue((Integer) resultats.get(0) == 2);

      // operateur + valeurs -> 0 resultats
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, ">=", banques, false);
      assertTrue(resultats.isEmpty());

      // on ajoute la banque 2
      final Banque b2 = banqueManager.findByIdManager(2);
      banques.add(b2);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "<", banques, false);
      assertTrue(resultats.size() == 1);

      // si banque2 seulement alors plus d'échantillons
      banques.remove(b1);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "<", banques, false);
      assertTrue(resultats.isEmpty());

      // is null
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         null, "is null", banques, false);
      assertTrue(resultats.size() == 1);
      banques.add(b1);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(3),
         temp, "is null", banques, false);
      assertTrue(resultats.size() == 3);

      /* ProdDerive */
      banques.remove(b2);
      temp = new Float(-75.0);

      // null temp
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         null, "=", banques, false);
      assertTrue(resultats.isEmpty());

      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, "=", banques, false);
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.get(0) instanceof ProdDerive);
      assertTrue(resultats.get(1) instanceof ProdDerive);

      // ids
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, "=", banques, true);
      assertTrue(resultats.size() == 2);
      assertTrue(resultats.get(0) instanceof Integer);
      assertTrue(resultats.get(1) instanceof Integer);
      assertTrue(resultats.contains(new Integer(1)));
      assertTrue(resultats.contains(new Integer(2)));

      // operateur + valeurs -> 0 resultats
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, ">", banques, false);
      assertTrue(resultats.isEmpty());

      // on ajoute la banque 2
      banques.add(b2);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, "=", banques, false);
      assertTrue(resultats.size() == 2);

      // si banque2 seulement alors plus de dérivés
      banques.remove(b1);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, "=", banques, false);
      assertTrue(resultats.isEmpty());

      // is null
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         null, "is null", banques, false);
      assertTrue(resultats.size() == 1);
      banques.add(b1);
      resultats = traitementQueryManager.findTKStockableObjectsByTempStockWithBanquesManager(entiteManager.findByIdManager(8),
         temp, "is null", banques, false);
      assertTrue(resultats.size() == 2);
   }

   @Test
   public void testFindObjetByCritereInListWithBanquesManager(){

      // sexe patient
      final Entite ePatient = entiteManager.findByIdManager(1);
      final ChampEntite champEntite = champEntiteManager.findByEntiteAndNomManager(ePatient, "sexe").get(0);

      // création du champ avec ajout du parent si nécessaire
      final Champ champ = new Champ(champEntite);
      //	if (parent1 != null) {
      //		champ.setChampParent(parent1);
      //	}

      Critere critere = new Critere(champ, "=", null);

      final List<Banque> banques = banqueManager.findAllObjectsManager();

      // cherche patients sexe M
      final List<Object> sexes = new ArrayList<>();
      sexes.add("M");

      List<Integer> res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, sexes, false);

      assertTrue(res.size() == 1);

      // cherche patients sexe M ou F
      sexes.add("F");
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, sexes, false);

      assertTrue(res.size() == 3);

      // cumulative cherche patients sexe M ET F
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, sexes, true);

      assertTrue(res.isEmpty());

      // cumulative cherche patients sexe M ET F
      sexes.remove("M");
      sexes.add("Ind");
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, sexes, true);

      assertTrue(res.isEmpty());

      // cherche risques 
      final Entite ePrel = entiteManager.findByIdManager(2);

      final ChampEntite champRisques = champEntiteManager.findByEntiteAndNomManager(ePrel, "Risques").get(0);
      final Champ parent1 = new Champ(champRisques);
      parent1.setChampParent(null);

      // risque nom
      final Entite risqueEntite = entiteManager.findByNomManager("Risque").get(0);
      final ChampEntite nomRisque = champEntiteManager.findByEntiteAndNomManager(risqueEntite, "nom").get(0);

      // création du champ avec ajout du parent si nécessaire
      final Champ champ2 = new Champ(nomRisque);
      champ2.setChampParent(parent1);

      critere = new Critere(champ2, "=", null);

      final List<Object> risques = new ArrayList<>();
      risques.add("HIV");

      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, risques, false);

      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche prelevements risque HIV OU LEUCEMIE
      risques.add("LEUCEMIE");
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, risques, false);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche prelevements risque HIV ET LEUCEMIE
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, risques, true);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche prelevements risque HIV ET LEUCEMIE ET GRIPPE A
      risques.add("GRIPPE A");
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, risques, true);
      assertTrue(res.isEmpty());

      // cherche prelevements risque HIV OU LEUCEMIE OU GRIPPE A
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, risques, false);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // champ annotation thesaurusM
      final ChampAnnotation thes2 = champAnnotationManager.findByIdManager(12);

      // création du champ avec ajout du parent si nécessaire
      final Champ champA12 = new Champ(thes2);
      critere = new Critere(champA12, "=", null);

      final List<Object> itemVals = new ArrayList<>();
      itemVals.add(itemDao.findById(5));// item2-2);

      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, false);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche objetIds annotationValeurs item2-2 OU item3-2-max
      itemVals.add(itemDao.findById(6));// item3-2-max;
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, false);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche objetIds annotationValeurs item2-2 ET item3-2-max
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, true);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // cherche objetIds annotationValeurs item2-2 ET item3-2-max ET item2-1
      itemVals.add(itemDao.findById(4)); //item1-2
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, true);
      assertTrue(res.isEmpty());

      // cherche objetIds annotationValeurs item2-2 OU item3-2-max OU item2-1
      res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, false);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      // champ annotation autre? ex thes simple
      final Champ champA1 = new Champ(champAnnotationManager.findByIdManager(1));
      critere = new Critere(champA1, "=", null);
      itemVals.clear();
      final Item it = new Item();
      it.setLabel("NEW");
      itemVals.add(it);

      boolean catched = false;
      try{
         res = traitementQueryManager.findObjetByCritereInListWithBanquesManager(critere, banques, itemVals, false);
      }catch(final IllegalArgumentException ie){
         catched = true;
      }
      assertTrue(catched);
   }

   @Test
   public void testFindPrelevementIdsThroughLaboInterManager(){
      final List<Integer> res = new ArrayList<>();

      List<Banque> banks = banqueManager.findAllObjectsManager();

      // etablissements
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(etablissementDao.findById(1), banks));
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      res.clear();
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(etablissementDao.findById(2), banks));
      assertTrue(res.isEmpty());

      // services
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(serviceDao.findById(1), banks));
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      res.clear();
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(serviceDao.findById(4), banks));
      assertTrue(res.isEmpty());

      // operateur
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(collaborateurDao.findById(2), banks));
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 1);

      res.clear();
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(collaborateurDao.findById(1), banks));
      assertTrue(res.isEmpty());

      // nulls
      res.clear();
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(collaborateurDao.findById(1), null));
      assertTrue(res.isEmpty());
      res.addAll(traitementQueryManager.findPrelevementIdsViaLaboInterManager(null, banks));
      assertTrue(res.isEmpty());
   }
}
