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

import fr.aphp.tumorotek.dao.io.imports.ImportHistoriqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.systeme.Entite;

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

   @Autowired
 ImportHistoriqueDao importHistoriqueDao;
   @Autowired
 ImportationDao importationDao;
   @Autowired
 EntiteDao entiteDao;

   public ImportationDaoTest(){

   }

   @Test
public void setImportHistoriqueDao(final ImportHistoriqueDao iDao){
      this.importHistoriqueDao = iDao;
   }

   @Test
public void setImportationDao(final ImportationDao iDao){
      this.importationDao = iDao;
   }

   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAll(){
      final List<Importation> liste = IterableUtils.toList(importationDao.findAll());
      assertTrue(liste.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByHistorique().
    */
   @Test
public void testFindByHistorique(){
      final ImportHistorique ih1 = importHistoriqueDao.findById(1);
      List<Importation> liste = importationDao.findByHistorique(ih1);
      assertTrue(liste.size() == 2);

      final ImportHistorique ih2 = importHistoriqueDao.findById(2);
      liste = importationDao.findByHistorique(ih2);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistorique(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByHistoriqueAndEntite().
    */
   @Test
public void testFindByHistoriqueAndEntite(){
      final ImportHistorique ih1 = importHistoriqueDao.findById(1);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Entite e3 = entiteDao.findById(3);
      List<Importation> liste = importationDao.findByHistoriqueAndEntite(ih1, e1);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByHistoriqueAndEntite(ih1, e2);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByHistoriqueAndEntite(ih1, e3);
      assertTrue(liste.size() == 0);

      final ImportHistorique ih2 = importHistoriqueDao.findById(2);
      liste = importationDao.findByHistoriqueAndEntite(ih2, e1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistoriqueAndEntite(null, e1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByHistoriqueAndEntite(ih1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByEntiteAndObjetId().
    */
   @Test
public void testFindByEntiteAndObjetId(){
      final Entite e1 = entiteDao.findById(1);
      final Entite e3 = entiteDao.findById(3);
      List<Importation> liste = importationDao.findByEntiteAndObjetId(e1, 1);
      assertTrue(liste.size() == 1);

      liste = importationDao.findByEntiteAndObjetId(e1, 15);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteAndObjetId(e3, 1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteAndObjetId(null, 1);
      assertTrue(liste.size() == 0);

      liste = importationDao.findByEntiteAndObjetId(e1, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'une Importation.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      final ImportHistorique ih = importHistoriqueDao.findById(1);
      final Entite e1 = entiteDao.findById(1);
      final Importation i1 = new Importation();
      i1.setEntite(e1);
      i1.setImportHistorique(ih);
      i1.setObjetId(2);

      // Test de l'insertion
      importationDao.save(i1);
      final Integer id = i1.getImportationId();
      assertTrue(IterableUtils.toList(importationDao.findAll()).size() == 3);

      // Test de la mise à jour
      final Importation i2 = importationDao.findById(id);
      assertNotNull(i2);
      assertNotNull(i2.getEntite());
      assertNotNull(i2.getImportHistorique());
      assertTrue(i2.getObjetId() == 2);

      i2.setObjetId(3);
      importationDao.save(i2);
      assertTrue(importationDao.findById(id).getObjetId() == 3);

      // Test de la délétion
      importationDao.deleteById(id);
      assertNull(importationDao.findById(id));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final ImportHistorique ih1 = importHistoriqueDao.findById(1);
      final ImportHistorique ih2 = importHistoriqueDao.findById(2);
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
      i2.setEntite(e1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setEntite(e2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setEntite(e1);
      assertTrue(i1.equals(i2));

      /*ImportHistorique*/
      i2.setImportHistorique(ih1);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setImportHistorique(ih2);
      assertFalse(i1.equals(i2));
      assertFalse(i2.equals(i1));
      i1.setImportHistorique(ih1);
      assertTrue(i1.equals(i2));
      assertTrue(i2.equals(i1));

      final Categorie c3 = new Categorie();
      assertFalse(i1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final Integer o1 = 1;
      final Integer o2 = 2;
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final ImportHistorique ih1 = importHistoriqueDao.findById(1);
      final ImportHistorique ih2 = importHistoriqueDao.findById(2);
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
      i2.setEntite(e1);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setEntite(e2);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setEntite(e1);
      assertTrue(i1.hashCode() == i2.hashCode());

      /*Specialite*/
      i2.setImportHistorique(ih1);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setImportHistorique(ih2);
      assertFalse(i1.hashCode() == i2.hashCode());
      i1.setImportHistorique(ih1);
      assertTrue(i1.hashCode() == i2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = i1.hashCode();
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
      assertTrue(hash == i1.hashCode());
   }

   /**
    * test toString().
    */
   @Test
public void testToString(){
      final Importation i1 = importationDao.findById(1);
      assertTrue(i1.toString().equals(
         "{" + i1.getObjetId() + ", " + i1.getEntite().getNom() + "(Entite) " + i1.getImportHistorique().toString() + "}"));

      final Importation i2 = new Importation();
      assertTrue(i2.toString().equals("{Empty Importation}"));
   }

   /**
    * Test la méthode clone.
    */
   @Test
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

      if(i1.getEntite() != null){
         assertTrue(i1.getEntite().equals(i2.getEntite()));
      }else{
         assertNull(i2.getEntite());
      }

      if(i1.getImportHistorique() != null){
         assertTrue(i1.getImportHistorique().equals(i2.getImportHistorique()));
      }else{
         assertNull(i2.getImportHistorique());
      }
   }

}
