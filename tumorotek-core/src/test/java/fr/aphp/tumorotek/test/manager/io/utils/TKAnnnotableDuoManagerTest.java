/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2015)
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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectPropertyDuo;
import fr.aphp.tumorotek.manager.io.utils.TKAnnotableDuoManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

public class TKAnnnotableDuoManagerTest extends AbstractManagerTest4
{

   @Autowired
   private TKAnnotableDuoManager tkAnnotableDuoManager;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private EntiteDao entiteDao;
   @Autowired
   private ObjetNonConformeManager objetNonConformeManager;
   @Autowired
   private ChampEntiteDao champEntiteDao;

   @Test
   public void testComparePrelevementsDuoManager(){

      final Entite prelEntite = entiteDao.findById(2);

      assertTrue(tkAnnotableDuoManager
         .compareObjectsDuoManager(null, new ArrayList<ChampEntite>(), new ArrayList<ChampAnnotation>()).isEmpty());

      final TKAnnotableObjectDuo duo = new TKAnnotableObjectDuo();
      duo.setEntite(prelEntite);

      assertTrue(tkAnnotableDuoManager
         .compareObjectsDuoManager(duo, new ArrayList<ChampEntite>(), new ArrayList<ChampAnnotation>()).isEmpty());

      final Prelevement p1 = prelevementManager.findByIdManager(1);
      duo.setSecondObj(p1);

      assertTrue(tkAnnotableDuoManager
         .compareObjectsDuoManager(duo, new ArrayList<ChampEntite>(), new ArrayList<ChampAnnotation>()).isEmpty());

      Prelevement pNew = p1.clone();
      pNew.setPrelevementId(null);
      duo.setFirstObj(pNew);

      assertTrue(tkAnnotableDuoManager
         .compareObjectsDuoManager(duo, new ArrayList<ChampEntite>(), new ArrayList<ChampAnnotation>()).isEmpty());

      final List<ChampEntite> chpsE = champEntiteDao.findByEntiteAndImport(prelEntite, true);
      // retire champs particulier risques & non.conforme.raison
      chpsE.remove(champEntiteDao.findById(249));
      chpsE.remove(champEntiteDao.findById(257));

      assertTrue(tkAnnotableDuoManager.compareObjectsDuoManager(duo, chpsE, new ArrayList<ChampAnnotation>()).isEmpty());

      pNew = prelevementManager.findByIdManager(2).clone();
      pNew.setPrelevementId(null);
      duo.setFirstObj(pNew);

      final List<TKAnnotableObjectPropertyDuo> propDuos =
         tkAnnotableDuoManager.compareObjectsDuoManager(duo, chpsE, new ArrayList<ChampAnnotation>());

      assertTrue(propDuos.size() == 13);

   }

}
