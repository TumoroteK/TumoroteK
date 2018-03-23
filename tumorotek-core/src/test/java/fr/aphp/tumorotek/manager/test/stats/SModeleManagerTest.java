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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.dao.stats.SModeleIndicateurDao;
import fr.aphp.tumorotek.dao.stats.SubdivisionDao;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.stats.IndicateurManager;
import fr.aphp.tumorotek.manager.stats.SModeleManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.manager.validation.exception.ValidationException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.SModeleIndicateur;
import fr.aphp.tumorotek.model.stats.Subdivision;

public class SModeleManagerTest extends AbstractManagerTest4
{

   @Autowired
   private SModeleDao modeleDao;
   @Autowired
   private SModeleManager sModeleManager;
   @Autowired
   private PlateformeManager plateformeManager;
   @Autowired
   private IndicateurManager indicateurManager;
   @Autowired
   private BanqueManager banqueManager;
   @Autowired
   private SModeleIndicateurDao sModeleIndicateurDao;
   @Autowired
   private SubdivisionDao subdivisionDao;

   @Test
   public void testFindByIdManager(){
      final Integer id = 1;
      SModele im = sModeleManager.findByIdManager(id);
      assertNotNull(im);
      assertTrue(im.getNom().equals("MOD1"));

      im = sModeleManager.findByIdManager(2);
      assertTrue(im.getNom().equals("MOD2"));

      final Integer id2 = 13;
      final SModele im2 = sModeleManager.findByIdManager(id2);
      assertNull(im2);
   }

   @Test
   public void testFindAllObjectsManager(){
      final List<SModele> ims = modeleDao.findAll();
      assertTrue(ims.size() == 3);
   }

   @Test
   public void testFindByPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<SModele> list = sModeleManager.findByPlateformeManager(pf1);
      assertTrue(list.size() == 2);
      assertTrue(list.contains(sModeleManager.findByIdManager(1)));
      assertTrue(list.contains(sModeleManager.findByIdManager(2)));

      list = sModeleManager.findByPlateformeManager(pf2);
      assertTrue(list.size() == 1);
      assertTrue(list.contains(sModeleManager.findByIdManager(3)));

      list = sModeleManager.findByPlateformeManager(null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindDoublon(){

      final String nom1 = "MOD1";
      final String nom2 = "MyModele";
      final Plateforme pf1 = plateformeManager.findByIdManager(1);

      final SModele m1 = new SModele();
      m1.setPlateforme(pf1);
      m1.setNom(nom1);
      assertTrue(sModeleManager.findDoublonManager(m1));

      m1.setNom(nom2);
      assertFalse(sModeleManager.findDoublonManager(m1));

      final SModele m2 = sModeleManager.findByIdManager(2);
      assertFalse(sModeleManager.findDoublonManager(m2));

      m2.setNom(nom1);
      assertTrue(sModeleManager.findDoublonManager(m2));

      assertFalse(sModeleManager.findDoublonManager(null));
   }

   @Test
   public void testValidateObject(){
      final SModele modele = new SModele();
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      Boolean catched = false;
      // nom empty
      try{
         sModeleManager.createObjectManager(modele, pf2, null, null);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.modele.nom.empty", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      // nom regexp
      modele.setNom("¤¤¤OEOEO");
      try{
         sModeleManager.createObjectManager(modele, pf2, null, null);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.modele.nom.illegal", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      // nom oversize
      modele.setNom(createOverLength(50));
      try{
         sModeleManager.createObjectManager(modele, pf2, null, null);
      }catch(final ValidationException ve){
         assertTrue(ve.getErrors().size() == 1);
         assertEquals("stats.modele.nom.tooLong", ve.getErrors().get(0).getFieldError().getCode());
         catched = true;
      }
      assertTrue(catched);
      catched = false;

      assertTrue(indicateurManager.findAllObjectsManager().size() == 5);
   }

   @Test
   public void testGetIndicateursManager(){
      final SModele m1 = sModeleManager.findByIdManager(1);
      assertTrue(sModeleManager.getIndicateursManager(m1).size() == 2);
      assertTrue(sModeleManager.getIndicateursManager(m1).get(0).equals(indicateurManager.findByIdManager(1)));
      assertTrue(sModeleManager.getIndicateursManager(m1).get(1).equals(indicateurManager.findByIdManager(2)));
      final SModele m3 = sModeleManager.findByIdManager(3);
      assertTrue(sModeleManager.getIndicateursManager(m3).size() == 1);
      final SModele m2 = sModeleManager.findByIdManager(2);
      assertTrue(sModeleManager.getIndicateursManager(m2).isEmpty());
      assertTrue(sModeleManager.getIndicateursManager(null).isEmpty());
   }

   @Test
   public void testCrud(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      SModele sM1 = new SModele();
      sM1.setDescription("Une courte description");

      final int sModeleTotCount = 3;
      final int indicTotCount = 5;
      final int sMIndicTotCount = 3;

      boolean catched = false;
      // on test l'insertion avec une pf nulle
      try{
         sModeleManager.createObjectManager(sM1, null, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount);

      // on test l'insertion d'un doublon
      sM1.setNom("MOD1");
      try{
         sModeleManager.createObjectManager(sM1, pf1, null, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount);

      sM1.setNom("MyModele");
      sModeleManager.createObjectManager(sM1, pf1, new ArrayList<Indicateur>(), null);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount + 1);
      assertTrue(indicateurManager.findAllObjectsManager().size() == indicTotCount);

      sM1 = sModeleManager.findByIdManager(sM1.getSmodeleId());
      assertTrue(sM1.getNom().equals("MyModele"));
      assertTrue(sM1.getDescription().equals("Une courte description"));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM1).isEmpty());
      assertTrue(sModeleManager.getBanquesManager(sM1).isEmpty());

      // suppression
      sModeleManager.removeObjectManager(sM1);

      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount);
      assertTrue(indicateurManager.findAllObjectsManager().size() == indicTotCount);

      // create et update indicateurs + modeles
      List<Indicateur> indicateurs = new ArrayList<>();
      indicateurs.add(indicateurManager.findByIdManager(3));
      indicateurs.add(indicateurManager.findByIdManager(4));
      List<Banque> banques = banqueManager.findByPlateformeAndArchiveManager(pf1, false);

      SModele sM2 = new SModele();
      sM2.setNom("MyModele2");
      sM2.setDescription("Une courte description");

      sModeleManager.createObjectManager(sM2, pf1, indicateurs, banques);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount + 1);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount + 2);
      sM2 = sModeleManager.findByIdManager(sM2.getSmodeleId());
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2).size() == 2);
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(3), 1)));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(4), 1)));
      assertTrue(sModeleManager.getBanquesManager(sM2).size() == 3);
      assertTrue(sModeleManager.getBanquesManager(sM2).containsAll(banqueManager.findByPlateformeAndArchiveManager(pf1, false)));

      assertNull(sModeleManager.getSubdivisionManager(sM2));

      // update
      sM2 = sModeleManager.findByIdManager(sM2.getSmodeleId());
      indicateurs.clear();
      indicateurs.add(indicateurManager.findByIdManager(5));
      banques.remove(banqueManager.findByIdManager(1));

      sM2.setNom("MyModele3");

      sM2.setDescription("descr3");

      sM2 = sModeleManager.updateObjectManager(sM2, pf1, indicateurs, banques);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount + 1);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount + 1);

      assertTrue(sM2.getSModeleIndicateurs().size() == 1);
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(5), 1)));
      assertTrue(sM2.getBanques().size() == 2);
      assertTrue(sM2.getBanques().containsAll(banques));

      sM2 = sModeleManager.findByIdManager(sM2.getSmodeleId());
      assertTrue(sM2.getNom().equals("MyModele3"));
      assertTrue(sM2.getDescription().equals("descr3"));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2).size() == 1);
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(5), 1)));
      assertTrue(sModeleManager.getBanquesManager(sM2).size() == 2);
      assertTrue(sModeleManager.getBanquesManager(sM2).containsAll(banques));

      assertTrue(sModeleManager.getSubdivisionManager(sM2).equals(subdivisionDao.findById(2)));

      // suppr banques + indicateurs
      indicateurs.clear();
      banques.clear();
      sM2 = sModeleManager.updateObjectManager(sM2, pf1, indicateurs, banques);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount + 1);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount);

      assertTrue(sM2.getSModeleIndicateurs().isEmpty());
      assertTrue(sM2.getBanques().isEmpty());

      sM2 = sModeleManager.findByIdManager(sM2.getSmodeleId());
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2).isEmpty());
      assertTrue(sModeleManager.getBanquesManager(sM2).isEmpty());

      // restaure
      indicateurs = indicateurManager.findAllObjectsManager();
      banques = banqueManager.findByPlateformeAndArchiveManager(pf1, false);
      sM2 = sModeleManager.updateObjectManager(sM2, pf1, indicateurs, banques);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount + 1);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount + 5);

      assertTrue(sM2.getSModeleIndicateurs().size() == 5);
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(1), 1)));
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(2), 1)));
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(3), 1)));
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(4), 1)));
      assertTrue(sM2.getSModeleIndicateurs().contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(5), 1)));
      assertTrue(sM2.getBanques().size() == 3);
      assertTrue(sM2.getBanques().containsAll(banqueManager.findByPlateformeAndArchiveManager(pf1, false)));

      sM2 = sModeleManager.findByIdManager(sM2.getSmodeleId());
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2).size() == 5);
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(1), 1)));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(2), 1)));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(3), 1)));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(4), 1)));
      assertTrue(sModeleManager.getSModeleIndicateursManager(sM2)
         .contains(new SModeleIndicateur(sM2, indicateurManager.findByIdManager(5), 1)));
      assertTrue(sModeleManager.getBanquesManager(sM2).size() == 3);
      assertTrue(sModeleManager.getBanquesManager(sM2).containsAll(banqueManager.findByPlateformeAndArchiveManager(pf1, false)));

      // incoherences subdivisions pour le sModele
      catched = false;
      try{
         sModeleManager.getSubdivisionManager(sM2);
      }catch(final RuntimeException re){
         assertTrue(re.getMessage().equals("stats.modele.indicateurs.toomany.subdivisions"));
         catched = true;
      }
      assertTrue(catched);

      sModeleManager.removeObjectManager(sM2);
      assertTrue(sModeleManager.findAllObjectsManager().size() == sModeleTotCount);
      assertTrue(sModeleIndicateurDao.findAll().size() == sMIndicTotCount);
      assertTrue(indicateurManager.findAllObjectsManager().size() == indicTotCount);
   }

   @Test
   public void testGetSubdivisionManager(){
      final SModele sM1 = sModeleManager.findByIdManager(1);
      final Subdivision s = sModeleManager.getSubdivisionManager(sM1);
      assertTrue(s.equals(subdivisionDao.findById(1)));
      assertNull(sModeleManager.getSubdivisionManager(sModeleManager.findByIdManager(2)));
      assertNull(sModeleManager.getSubdivisionManager(null));
   }
}
