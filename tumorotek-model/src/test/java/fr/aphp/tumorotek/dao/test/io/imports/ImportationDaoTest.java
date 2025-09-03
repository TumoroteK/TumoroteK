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
package fr.aphp.tumorotek.dao.test.io.imports;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.io.imports.EImportationType;
import fr.aphp.tumorotek.model.io.imports.Importation;

/**
 *
 * Classe de test pour le DAO ImportationDao et le bean
 * du domaine Importation.
 * Classe créée le 09/02/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class ImportationDaoTest extends AbstractDaoTest
{

   private ImportationDao importationDao;

   public ImportationDaoTest(){

   }

   public void setImportationDao(final ImportationDao iDao){
      this.importationDao = iDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<Importation> liste = importationDao.findAll();
      assertTrue(liste.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByHistorique().
    */
   public void testFindByHistorique(){
      final Integer importHistoriqueId1 = 1;
      List<Importation> liste = importationDao.findByHistoriqueId(importHistoriqueId1);
      assertTrue(liste.size() == 2);

      final Integer importHistoriqueId2 = 2;
      liste = importationDao.findByHistoriqueId(importHistoriqueId2);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistoriqueId(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByHistoriqueAndEntite().
    */
   public void testFindByHistoriqueAndEntite(){
      final Integer importHistoriqueId1 = 1;
      final Integer entiteId1 = 1;
      final Integer entiteId2 = 2;
      final Integer entiteId3 = 3;
      List<Importation> liste = importationDao.findByHistoriqueIdAndEntiteId(importHistoriqueId1, entiteId1);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByHistoriqueIdAndEntiteId(importHistoriqueId1, entiteId2);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByHistoriqueIdAndEntiteId(importHistoriqueId1, entiteId3);
      assertTrue(liste.size() == 0);

      final Integer importHistoriqueId2 = 2;
      liste = importationDao.findByHistoriqueIdAndEntiteId(importHistoriqueId2, entiteId1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistoriqueIdAndEntiteId(null, entiteId1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistoriqueIdAndEntiteId(importHistoriqueId1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEntiteAndObjetId().
    */
   public void testFindByEntiteAndObjetId(){
      final Integer entiteId1 = 1;
      final Integer entiteId3 = 3;
      List<Importation> liste = importationDao.findByEntiteIdAndObjetId(entiteId1, 1);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByEntiteIdAndObjetId(entiteId1, 15);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteIdAndObjetId(entiteId3, 1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteIdAndObjetId(null, 1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteIdAndObjetId(entiteId1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'une Importation.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Integer importHistoriqueId1 = 1;
      final Integer entiteId1 = 1;
      final Importation i1 = new Importation();
      i1.setEntiteId(entiteId1);
      i1.setImportHistoriqueId(importHistoriqueId1);
      i1.setObjetId(2);
      i1.setTypeCode(EImportationType.CREATION.getCode());

      // Test de l'insertion
      importationDao.createObject(i1);
      final Integer id = i1.getImportationId();
      assertTrue(importationDao.findAll().size() == 3);

      // Test de la mise à jour
      final Importation i2 = importationDao.findById(id);
      assertNotNull(i2);
      assertNotNull(i2.getEntiteId());
      assertNotNull(i2.getImportHistoriqueId());
      assertTrue(i2.getObjetId() == 2);

      i2.setObjetId(3);
      importationDao.updateObject(i2);
      assertTrue(importationDao.findById(id).getObjetId() == 3);

      // Test de la délétion
      importationDao.removeObject(id);
      assertNull(importationDao.findById(id));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      //objetIds
      final Integer o1 = 1;
      final Integer o2 = 2;
      //entiteIds
      final Integer e1 = 1;
      final Integer e2 = 2;
      //importHistoriqueIds
      final Integer ih1 = 1;
      final Integer ih2 = 2;
      final Importation i1 = new Importation();
      final Importation i2 = new Importation();

      // L'objet 1 n'est pas égal à null
      assertFalse(i1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(i1.equals(i1));

      /*null*/
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));

      /*objjetId*/
      i2.setObjetId(o1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setObjetId(o2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setObjetId(o1);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));

      /*Entite*/
      i2.setEntiteId(e1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setEntiteId(e2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setEntiteId(e1);
      assertTrue(i1.equals(i2));

      /*ImportHistorique*/
      i2.setImportHistoriqueId(ih1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setImportHistoriqueId(ih2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setImportHistoriqueId(ih1);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));

      final Categorie c3 = new Categorie();
      assertFalse(i1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      //objetIds
      final Integer o1 = 1;
      final Integer o2 = 2;
      //entiteIds
      final Integer e1 = 1;
      final Integer e2 = 2;
      //importHistoriqueIds
      final Integer ih1 = 1;
      final Integer ih2 = 2;
      final Importation i1 = new Importation();
      final Importation i2 = new Importation();

      /*null*/
      assertTrue(i1.hashCode() == i2.hashCode());

      /*Nom*/
      i2.setObjetId(o1);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setObjetId(o2);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setObjetId(o1);
      assertTrue(i1.hashCode() == i2.hashCode());

      /*Prenom*/
      i2.setEntiteId(e1);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setEntiteId(e2);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setEntiteId(e1);
      assertTrue(i1.hashCode() == i2.hashCode());

      /*Specialite*/
      i2.setImportHistoriqueId(ih1);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setImportHistoriqueId(ih2);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setImportHistoriqueId(ih1);
      assertTrue(i1.hashCode() == i2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = i1.hashCode();
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Importation i1 = importationDao.findById(1);
      final Importation i2 = i1.clone();
      assertTrue(i1.equals(i2));

      if(i1.getImportationId() != null){
         assertTrue(i1.getImportationId() == i2.getImportationId());
      }else{
         assertNull(i2.getImportationId());
      }

      if(i1.getObjetId() != null){
         assertTrue(i1.getObjetId() == i2.getObjetId());
      }else{
         assertNull(i2.getObjetId());
      }

      if(i1.getEntiteId() != null){
         assertTrue(i1.getEntiteId().equals(i2.getEntiteId()));
      }else{
         assertNull(i2.getEntiteId());
      }

      if(i1.getImportHistoriqueId() != null){
         assertTrue(i1.getImportHistoriqueId().equals(i2.getImportHistoriqueId()));
      }else{
         assertNull(i2.getImportHistoriqueId());
      }
   }

}
