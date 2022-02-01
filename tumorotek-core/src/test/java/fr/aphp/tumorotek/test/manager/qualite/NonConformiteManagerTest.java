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
package fr.aphp.tumorotek.test.manager.qualite;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.qualite.ConformiteTypeManager;
import fr.aphp.tumorotek.manager.qualite.NonConformiteManager;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager NonConformite.
 * Classe créée le 08/11/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class NonConformiteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private NonConformiteManager nonConformiteManager;
   @Autowired
   private ConformiteTypeManager conformiteTypeManager;
   @Autowired
   private PlateformeDao plateformeDao;
   @Autowired
   private EntiteDao entiteDao;

   private final int tot = 11;

   public NonConformiteManagerTest(){
   }

   @Test
   public void testFindById(){
      final NonConformite n = nonConformiteManager.findByIdManager(1);
      assertNotNull(n);

      final NonConformite nNull = nonConformiteManager.findByIdManager(100);
      assertNull(nNull);
   }

   @Test
   public void testFindAll(){
      final List<NonConformite> list = nonConformiteManager.findAllObjectsManager();
      assertTrue(list.size() == tot);
   }

   @Test
   public void testFindByPlateformeAndTypeObjManager(){
      final ConformiteType t1 = conformiteTypeManager.findByIdManager(1);
      final Plateforme p1 = plateformeDao.findById(1);
      List<NonConformite> list = nonConformiteManager.findByPlateformeAndTypeObjManager(p1, t1);
      assertTrue(list.size() == 2);

      final Plateforme p2 = plateformeDao.findById(2);
      list = nonConformiteManager.findByPlateformeAndTypeObjManager(p2, t1);
      assertTrue(list.size() == 1);

      final ConformiteType t2 = conformiteTypeManager.findByIdManager(2);
      list = nonConformiteManager.findByPlateformeAndTypeObjManager(p2, t2);
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeAndTypeObjManager(null, t1);
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeAndTypeObjManager(p1, null);
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeAndTypeObjManager(null, null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindByPlateformeAndTypeStringManager(){
      final Plateforme p1 = plateformeDao.findById(1);
      List<NonConformite> list =
         nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p1, "Arrivee", entiteDao.findById(2));
      assertTrue(list.size() == 2);

      final Plateforme p2 = plateformeDao.findById(2);
      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p2, "Arrivee", entiteDao.findById(2));
      assertTrue(list.size() == 1);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p2, "%", entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p2, "Traitement", entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p1, "Traitement", entiteDao.findById(3));
      assertTrue(list.size() == 2);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p1, "Cession", entiteDao.findById(8));
      assertTrue(list.size() == 2);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(null, "Arrivee", entiteDao.findById(2));
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(p1, null, entiteDao.findById(3));
      assertTrue(list.size() == 0);

      list = nonConformiteManager.findByPlateformeEntiteAndTypeStringManager(null, null, null);
      assertTrue(list.size() == 0);
   }

   @Test
   public void testFindDoublon(){
      NonConformite n1 = new NonConformite();
      n1.setConformiteType(conformiteTypeManager.findByIdManager(1));
      n1.setPlateforme(plateformeDao.findById(1));
      n1.setNom("Test");
      assertFalse(nonConformiteManager.findDoublonManager(n1));

      n1.setNom("Problème livraison");
      assertTrue(nonConformiteManager.findDoublonManager(n1));

      n1.setPlateforme(plateformeDao.findById(2));
      assertFalse(nonConformiteManager.findDoublonManager(n1));

      n1.setPlateforme(plateformeDao.findById(1));
      n1.setConformiteType(conformiteTypeManager.findByIdManager(2));
      assertFalse(nonConformiteManager.findDoublonManager(n1));

      n1 = nonConformiteManager.findByIdManager(1);
      assertFalse(nonConformiteManager.findDoublonManager(n1));

      n1.setNom("Erreur dossier");
      assertTrue(nonConformiteManager.findDoublonManager(n1));
   }

   @Test
   public void testIsUsed(){
      final NonConformite n1 = nonConformiteManager.findByIdManager(1);
      assertNotNull(n1);
      assertTrue(nonConformiteManager.isUsedObjectManager(n1));
      final NonConformite n2 = nonConformiteManager.findByIdManager(2);
      assertNotNull(n2);
      assertFalse(nonConformiteManager.isUsedObjectManager(n2));
   }

  @Test
   public void testCrud(){
      // Insertion
      final NonConformite n1 = new NonConformite();
      n1.setNom("Test");
      n1.setConformiteType(conformiteTypeManager.findByIdManager(1));

      // test insertion avec pf null
      Boolean catched = false;
      try{
         nonConformiteManager.saveManager(n1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot);
      n1.setPlateforme(plateformeDao.findById(1));

      // test type avec pf null
      catched = false;
      n1.setConformiteType(null);
      try{
         nonConformiteManager.saveManager(n1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot);

      catched = false;
      // On teste l'insertion d'un doublon pour vérifier que l'exception
      // est lancé
      n1.setConformiteType(conformiteTypeManager.findByIdManager(1));
      n1.setNom("Problème livraison");
      try{
         nonConformiteManager.saveManager(n1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot);

      // On teste une insertion avec un attribut nom non valide
      final String[] emptyValues = new String[] {"", "  ", null, "€€¢¢¢®®®€", createOverLength(200)};
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            n1.setNom(emptyValues[i]);
            nonConformiteManager.saveManager(n1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot);

      // on teste une insertion valide
      n1.setNom("TEST");
      nonConformiteManager.saveManager(n1);
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);
      final Integer id = n1.getId();

      // Update
      final NonConformite n2 = nonConformiteManager.findByIdManager(id);
      assertTrue(n2.getNom().equals("TEST"));
      assertTrue(n2.getConformiteType().equals(conformiteTypeManager.findByIdManager(1)));
      assertTrue(n2.getPlateforme().equals(plateformeDao.findById(1)));

      // test update avec pf null
      catched = false;
      n2.setPlateforme(null);
      try{
         nonConformiteManager.saveManager(n2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);
      n2.setPlateforme(plateformeDao.findById(1));

      // test update avec pf null
      catched = false;
      n2.setConformiteType(null);
      try{
         nonConformiteManager.saveManager(n2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);
      n2.setConformiteType(conformiteTypeManager.findByIdManager(1));

      // On teste l'update d'un doublon pour vérifier que l'exception
      // est lancée
      n2.setNom("Erreur dossier");
      Boolean catchedUpdate = false;
      try{
         nonConformiteManager.saveManager(n2);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catchedUpdate = true;
         }
      }
      assertTrue(catchedUpdate);
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);
      // On teste une modif avec l'attribut type non valide
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            n2.setNom(emptyValues[i]);
            nonConformiteManager.saveManager(n2);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);

      // On teste une mise à jour valide
      n2.setNom("UP");
      n2.setPlateforme(plateformeDao.findById(2));
      n2.setConformiteType(conformiteTypeManager.findByIdManager(2));
      nonConformiteManager.saveManager(n2);
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot + 1);

      // on vérifie modif valide
      final NonConformite n3 = nonConformiteManager.findByIdManager(id);
      assertTrue(n3.getNom().equals("UP"));
      assertTrue(n3.getConformiteType().equals(conformiteTypeManager.findByIdManager(2)));
      assertTrue(n3.getPlateforme().equals(plateformeDao.findById(2)));

      // Suppression
      nonConformiteManager.removeObjectManager(n3);
      assertTrue(nonConformiteManager.findAllObjectsManager().size() == tot);
   }

  @Test
   public void testGetFromObjetNonConformes(){
      // null
      assertTrue(nonConformiteManager.getFromObjetNonConformes(null).isEmpty());

      final List<ObjetNonConforme> list = new ArrayList<>();
      final ObjetNonConforme o1 = new ObjetNonConforme();
      o1.setNonConformite(nonConformiteManager.findByIdManager(1));
      list.add(o1);
      List<NonConformite> res = new ArrayList<>();
      res = nonConformiteManager.getFromObjetNonConformes(list);
      assertTrue(res.size() == 1);
      assertTrue(res.get(0).equals(nonConformiteManager.findByIdManager(1)));

      final ObjetNonConforme o2 = new ObjetNonConforme();
      o2.setNonConformite(nonConformiteManager.findByIdManager(3));
      list.add(o2);
      res = nonConformiteManager.getFromObjetNonConformes(list);
      assertTrue(res.size() == 2);
      assertTrue(res.contains(nonConformiteManager.findByIdManager(1)));
      assertTrue(res.contains(nonConformiteManager.findByIdManager(3)));

      list.clear();
      assertTrue(nonConformiteManager.getFromObjetNonConformes(null).isEmpty());

   }
}
