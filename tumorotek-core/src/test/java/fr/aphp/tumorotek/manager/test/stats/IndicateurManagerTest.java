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
package fr.aphp.tumorotek.manager.test.stats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.stats.IndicateurDao;
import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.manager.stats.IndicateurManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;

public class IndicateurManagerTest extends AbstractManagerTest4
{

   @Autowired
   private IndicateurManager indicateurManager;
   @Autowired
   private IndicateurDao indicateurDao;
   @Autowired
   private SModeleDao sModeleDao;

   private final Integer totCounts = 5;

   @Test
   public void testFindById(){
      Indicateur s = indicateurManager.findByIdManager(1);
      assertNotNull(s);
      s = indicateurManager.findByIdManager(8);
      assertNull(s);
   }

   @Test
   public void testFindAllObjects(){
      final List<Indicateur> ss = indicateurManager.findAllObjectsManager();
      assertTrue(ss.size() == totCounts);
   }

   @Test
   public void testFindBySModeleManager(){
      final SModele m1 = sModeleDao.findById(1).orElse(null);
      final SModele m2 = sModeleDao.findById(2).orElse(null);

      List<Indicateur> ss = indicateurManager.findBySModeleManager(m1);
      assertTrue(ss.size() == 2);
      assertTrue(ss.contains(indicateurDao.findById(1))).orElse(null);
      assertTrue(ss.contains(indicateurDao.findById(2))).orElse(null);

      ss = indicateurManager.findBySModeleManager(m2);
      assertTrue(ss.isEmpty());

      ss = indicateurManager.findBySModeleManager(null);
      assertTrue(ss.isEmpty());
   }

   @Test
   public void testValidateObject(){
      final Indicateur indic = new Indicateur();

      Boolean catched = false;
      // nom empty
      try{
         indicateurManager.createObjectManager(indic);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.indicateur.nom.empty", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      // nom regexp
      indic.setNom("¤¤¤OEOEO");
      try{
         indicateurManager.createObjectManager(indic);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.indicateur.nom.illegal", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      // nom oversize
      indic.setNom(createOverLength(200));
      try{
         indicateurManager.createObjectManager(indic);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.indicateur.nom.tooLong", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts);
   }

   @Test
   public void testCrud() throws ParseException{
      final Integer idIndic = createObjectManager();
      updateObjectManager(idIndic);
   }

   private Integer createObjectManager(){
      final Indicateur indic = new Indicateur();
      indic.setNom("testCreate");
      indic.setCallingProcedure("call");
      indic.setDescription("Une courte description");

      indicateurManager.createObjectManager(indic);
      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts + 1);

      //un statement ne peux avoir le même ordre dans un meme modele
      final Integer idIndic = indic.getIndicateurId();
      final Indicateur itest = indicateurManager.findByIdManager(idIndic);
      assertNotNull(itest);
      assertTrue(itest.getNom().equals("testCreate"));
      assertTrue(itest.getCallingProcedure().equals("call"));
      assertTrue(itest.getDescription().equals("Une courte description"));

      final Indicateur i2 = new Indicateur();
      i2.setNom("testCreate");
      i2.setDescription("Une courte description");
      i2.setCallingProcedure("call");
      boolean catched = false;
      try{
         indicateurManager.createObjectManager(i2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            ;
         }
         catched = true;
      }
      assertTrue(catched);
      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts + 1);

      return idIndic;
   }

   private void updateObjectManager(final Integer idIndic) throws ParseException{

      Indicateur itest = indicateurDao.findById(idIndic).orElse(null);
      itest.setNom("¤¤¤¤OO");
      itest.setCallingProcedure("call2");
      itest.setDescription("descr2");

      // nom regexp
      boolean catched = false;
      try{
         indicateurManager.updateObjectManager(itest);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.indicateur.nom.illegal", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);

      assertTrue(indicateurDao.findById(idIndic).getNom().equals("testCreate")).orElse(null);
      assertTrue(indicateurDao.findById(idIndic).getCallingProcedure().equals("call")).orElse(null);
      assertTrue(indicateurDao.findById(idIndic).getDescription().equals("Une courte description")).orElse(null);

      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts + 1);

      itest.setNom("UpdatedNom");

      indicateurManager.updateObjectManager(itest);
      itest = indicateurManager.findByIdManager(idIndic);
      assertTrue(itest.getNom().equals("UpdatedNom"));
      assertTrue(itest.getCallingProcedure().equals("call2"));
      assertTrue(itest.getDescription().equals("descr2"));
      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts + 1);

      indicateurManager.removeObjectManager(itest);
      assertTrue(indicateurManager.findAllObjectsManager().size() == totCounts);

   }
}
