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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO ImportTemplateDao et le bean
 * du domaine ImportTemplate.
 * Classe créée le 24/01/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class ImportTemplateDaoTest extends AbstractDaoTest
{

   @Autowired
 ImportTemplateDao importTemplateDao;
   @Autowired
 BanqueDao banqueDao;
   @Autowired
 EntiteDao entiteDao;

   public ImportTemplateDaoTest(){

   }

   @Test
public void setImportTemplateDao(final ImportTemplateDao i){
      this.importTemplateDao = i;
   }

   @Test
public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
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
      final List<ImportTemplate> liste = IterableUtils.toList(importTemplateDao.findAll());
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByBanqueWithOrder().
    */
   @Test
public void testFindByBanqueWithOrder(){
      final Banque b1 = banqueDao.findById(1);
      List<ImportTemplate> liste = importTemplateDao.findByBanqueWithOrder(b1);
      assertTrue(liste.size() == 3);
      assertTrue(liste.get(0).getNom().equals("IMPORT AUTO"));

      final Banque b2 = banqueDao.findById(2);
      liste = importTemplateDao.findByBanqueWithOrder(b2);
      assertTrue(liste.size() == 1);

      final Banque b3 = banqueDao.findById(3);
      liste = importTemplateDao.findByBanqueWithOrder(b3);
      assertTrue(liste.size() == 0);

      liste = importTemplateDao.findByBanqueWithOrder(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<ImportTemplate> liste = importTemplateDao.findByExcludedId(1);
      assertTrue(liste.size() == 3);

      liste = importTemplateDao.findByExcludedId(100);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un ImportTemplate.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrud() throws Exception{

      Integer tpId;

      final Banque b = banqueDao.findById(1);
      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Entite e3 = entiteDao.findById(3);
      final Entite e4 = entiteDao.findById(4);
      Set<Entite> entites = new HashSet<>();
      final ImportTemplate it = new ImportTemplate();
      final String nom = "Template";
      final String nomUp = "UP";

      it.setBanque(b);
      it.setNom(nom);
      it.setDescription("DESC");
      it.setIsEditable(true);
      entites.add(e1);
      entites.add(e2);
      it.setEntites(entites);

      // Test de l'insertion
      importTemplateDao.save(it);
      tpId = it.getImportTemplateId();
      assertNotNull(tpId);

      // Test de la mise à jour
      final ImportTemplate it2 = importTemplateDao.findById(tpId);
      assertNotNull(it2);
      assertNotNull(it2.getBanque());
      assertTrue(it2.getNom().equals(nom));
      assertTrue(it2.getDescription().equals("DESC"));
      assertTrue(it2.getIsEditable());
      assertTrue(it2.getEntites().size() == 2);
      assertNull(it2.getDeriveParentEntite());

      it2.setNom(nomUp);
      it2.setDeriveParentEntite(e3);
      entites = new HashSet<>();
      entites.add(e2);
      entites.add(e3);
      entites.add(e4);
      it2.setEntites(entites);
      importTemplateDao.save(it2);
      assertTrue(importTemplateDao.findById(tpId).getNom().equals(nomUp));
      assertTrue(importTemplateDao.findById(tpId).getEntites().size() == 3);
      assertTrue(it2.getDeriveParentEntite().equals(e3));

      // Test de la délétion
      importTemplateDao.deleteById(new Integer(5));
      assertNull(importTemplateDao.findById(new Integer(5)));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      final String nom = "nom";
      final String nom2 = "nom2";
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final ImportTemplate t1 = new ImportTemplate();
      final ImportTemplate t2 = new ImportTemplate();
      t1.setNom(nom);
      t1.setBanque(b1);
      t2.setNom(nom);
      t2.setBanque(b1);

      // L'objet 1 n'est pas égal à null
      assertFalse(t1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(t1.equals(t1));
      // 2 objets sont égaux entre eux
      assertTrue(t1.equals(t2));
      assertTrue(t2.equals(t1));

      t1.setBanque(null);
      t1.setNom(null);
      t2.setBanque(null);
      t2.setNom(null);
      assertTrue(t1.equals(t2));
      t2.setNom(nom);
      assertFalse(t1.equals(t2));
      t1.setNom(nom);
      assertTrue(t1.equals(t2));
      t2.setNom(nom2);
      assertFalse(t1.equals(t2));
      t2.setNom(null);
      assertFalse(t1.equals(t2));
      t2.setBanque(b1);
      assertFalse(t1.equals(t2));

      t1.setBanque(b1);
      t1.setNom(null);
      t2.setNom(null);
      t2.setBanque(b1);
      assertTrue(t1.equals(t2));
      t2.setBanque(b2);
      assertFalse(t1.equals(t2));
      t2.setNom(nom);
      assertFalse(t1.equals(t2));

      // Vérification de la différenciation de 2 objets
      t1.setNom(nom);
      assertFalse(t1.equals(t2));
      t2.setNom(nom2);
      t2.setBanque(b1);
      assertFalse(t1.equals(t2));

      final Categorie c3 = new Categorie();
      assertFalse(t1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      final String nom = "nom";
      final String nom2 = "nom2";
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final ImportTemplate t1 = new ImportTemplate();
      final ImportTemplate t2 = new ImportTemplate();
      //null
      assertTrue(t1.hashCode() == t2.hashCode());

      //Nom
      t2.setNom(nom);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setNom(nom2);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setNom(nom);
      assertTrue(t1.hashCode() == t2.hashCode());

      //ProtocoleType
      t2.setBanque(b1);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setBanque(b2);
      assertFalse(t1.hashCode() == t2.hashCode());
      t1.setBanque(b1);
      assertTrue(t1.hashCode() == t2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = t1.hashCode();
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   @Test
public void testToString(){
      final ImportTemplate t1 = importTemplateDao.findById(1);
      assertTrue(t1.toString().equals("{" + t1.getNom() + ", " + t1.getBanque().getNom() + "(Banque)}"));

      final ImportTemplate t2 = new ImportTemplate();
      assertTrue(t2.toString().equals("{Empty ImportTemplate}"));
   }

   /**
    * Test la méthode clone.
    */
   @Test
public void testClone(){
      final ImportTemplate t1 = importTemplateDao.findById(1);
      ImportTemplate t2 = new ImportTemplate();
      t2 = t1.clone();

      assertTrue(t1.equals(t2));

      if(t1.getImportTemplateId() != null){
         assertTrue(t1.getImportTemplateId() == t2.getImportTemplateId());
      }else{
         assertNull(t2.getImportTemplateId());
      }

      if(t1.getBanque() != null){
         assertTrue(t1.getBanque().equals(t2.getBanque()));
      }else{
         assertNull(t2.getBanque());
      }

      if(t1.getNom() != null){
         assertTrue(t1.getNom().equals(t2.getNom()));
      }else{
         assertNull(t2.getNom());
      }

      if(t1.getDescription() != null){
         assertTrue(t1.getDescription().equals(t2.getDescription()));
      }else{
         assertNull(t2.getDescription());
      }

      if(t1.getIsEditable() != null){
         assertTrue(t1.getIsEditable().equals(t2.getIsEditable()));
      }else{
         assertNull(t2.getIsEditable());
      }

      if(t1.getEntites() != null){
         assertTrue(t1.getEntites().equals(t2.getEntites()));
      }else{
         assertNull(t2.getEntites());
      }

      if(t1.getImportHistoriques() != null){
         assertTrue(t1.getImportHistoriques().equals(t2.getImportHistoriques()));
      }else{
         assertNull(t2.getImportHistoriques());
      }
      assertTrue(t1.getDeriveParentEntite().equals(t2.getDeriveParentEntite()));
   }

}
