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
package fr.aphp.tumorotek.dao.test.utilisateur;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.ProfilDao;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.utilisateur.Profil;

/**
 *
 * Classe de test pour le DAO ProfilDao et le bean
 * du domaine Profil.
 *
 * @author Pierre Ventadour.
 * @version 18/05/2010
 * @version 2.1
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ProfilDaoTest extends AbstractDaoTest
{

   private ProfilDao profilDao;
   private PlateformeDao plateformeDao;

   public ProfilDaoTest(){

   }

   public void setProfilDao(final ProfilDao pDao){
      this.profilDao = pDao;
   }

   public void setPlateformeDao(final PlateformeDao p){
      this.plateformeDao = p;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<Profil> liste = profilDao.findAll();
      assertTrue(liste.size() == 5);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<Profil> liste = profilDao.findByOrder();
      assertTrue(liste.size() == 5);
      assertTrue(liste.get(0).getNom().equals("ADMINISTRATEUR DE COLLECTION"));
      assertTrue(liste.get(0).getAccesAdministration());
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){

      List<Profil> liste = profilDao.findByNom("CONSULTATION");
      assertTrue(liste.size() == 1);

      liste = profilDao.findByNom("GESTION");
      assertTrue(liste.size() == 0);

      liste = profilDao.findByNom("GESTION%");
      assertTrue(liste.size() == 2);

      liste = profilDao.findByNom("");
      assertTrue(liste.size() == 0);

      liste = profilDao.findByNom("%");
      assertTrue(liste.size() == 5);

      liste = profilDao.findByNom(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Profil> liste = profilDao.findByExcludedId(1);
      assertTrue(liste.size() == 4);

      liste = profilDao.findByExcludedId(10);
      assertTrue(liste.size() == 5);

      liste = profilDao.findByExcludedId(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un profil.
    * @throws Exception lance une exception en cas d'erreur.
    * @version 2.1
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Profil p = new Profil();
      p.setNom("NOM");
      p.setAnonyme(false);
      p.setAdmin(true);
      p.setAccesAdministration(true);
      p.setProfilExport(1);
      p.setPlateforme(plateformeDao.findById(2));

      // Test de l'insertion
      profilDao.createObject(p);
      assertNotNull(p.getProfilId());

      final Integer pId = p.getProfilId();

      final Profil p2 = profilDao.findById(pId);
      // Vérification des données entrées dans la base
      assertNotNull(p2);
      assertTrue(p2.getNom().equals("NOM"));
      assertFalse(p2.getAnonyme());
      assertTrue(p2.getAccesAdministration());
      assertTrue(p2.getProfilExport() == 1);
      // @since 2.1
      assertFalse(p2.isArchive());
      assertTrue(p2.getPlateforme().equals(plateformeDao.findById(2)));

      // Test de la mise à jour
      p2.setNom("UP");
      p2.setAnonyme(true);
      p2.setAccesAdministration(false);
      p2.setProfilExport(2);
      p2.setArchive(true);
      p2.setPlateforme(plateformeDao.findById(1));
      profilDao.updateObject(p2);
      assertTrue(profilDao.findById(pId).getNom().equals("UP"));
      assertTrue(profilDao.findById(pId).getProfilExport() == 2);
      assertTrue(profilDao.findById(pId).getAnonyme());
      assertFalse(profilDao.findById(pId).getAccesAdministration());
      assertTrue(profilDao.findById(pId).isArchive());
      assertTrue(p2.getPlateforme().equals(plateformeDao.findById(1)));

      // Test de la délétion
      profilDao.removeObject(pId);
      assertNull(profilDao.findById(pId));
   }

   /**
    * Test de la méthode surchargée "equals".
    * @version 2.1
    */
   public void testEquals(){
      final String nom = "NOM";
      final String nom2 = "NOM2";
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);

      final Profil p1 = new Profil();
      p1.setNom(nom);
      p1.setPlateforme(pf1);
      final Profil p2 = new Profil();
      p2.setNom(nom);
      p2.setPlateforme(pf1);

      // L'objet 1 n'est pas égal à null
      assertFalse(p1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(p1.equals(p1));
      // 2 objets sont égaux entre eux
      assertTrue(p1.equals(p2));
      assertTrue(p2.equals(p1));

      p1.setNom(null);
      p1.setPlateforme(null);
      p2.setNom(null);
      p2.setPlateforme(null);
      assertTrue(p1.equals(p2));
      p1.setNom(nom);
      assertFalse(p1.equals(p2));
      p2.setNom(nom);
      assertTrue(p1.equals(p2));
      p2.setNom(nom2);
      assertFalse(p1.equals(p2));
      p2.setNom("NOM");
      assertTrue(p1.equals(p2));
      p2.setNom(null);
      assertFalse(p1.equals(p2));

      p1.setPlateforme(pf1);
      p1.setNom(null);
      p2.setPlateforme(pf1);
      assertTrue(p1.equals(p2));
      p2.setPlateforme(pf2);
      assertFalse(p1.equals(p2));
      p2.setPlateforme(null);
      assertFalse(p1.equals(p2));
      final Plateforme newPF = new Plateforme();
      newPF.setNom("PLATEFORME 1");
      p2.setPlateforme(newPF);
      assertTrue(p1.equals(p2));

      final Categorie c3 = new Categorie();
      assertFalse(p1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String nom1 = "NOM";
      final String nom2 = "NOM2";
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);

      final Profil p1 = new Profil();
      final Profil p2 = new Profil();
      assertTrue(p1.hashCode() > 0);

      //null
      assertTrue(p1.hashCode() == p2.hashCode());

      //Nom
      p2.setNom(nom1);
      assertFalse(p1.hashCode() == p2.hashCode());
      p1.setNom(nom2);
      assertFalse(p1.hashCode() == p2.hashCode());
      p1.setNom(nom1);
      assertTrue(p1.hashCode() == p2.hashCode());

      //Plateforme
      p2.setPlateforme(pf1);
      assertFalse(p1.hashCode() == p2.hashCode());
      p1.setPlateforme(pf2);
      assertFalse(p1.hashCode() == p2.hashCode());
      p1.setPlateforme(pf1);
      assertTrue(p1.hashCode() == p2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = p1.hashCode();
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
      assertTrue(hash == p1.hashCode());
   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Profil p1 = profilDao.findById(1);
      assertTrue(p1.toString().equals("{" + p1.getNom() + "}"));

      final Profil p2 = new Profil();
      assertTrue(p2.toString().equals("{Empty Profil}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Profil p1 = profilDao.findById(1);
      Profil p2 = new Profil();
      p2 = p1.clone();

      assertTrue(p1.equals(p2));

      if(p1.getProfilId() != null){
         assertTrue(p1.getProfilId() == p2.getProfilId());
      }else{
         assertNull(p2.getProfilId());
      }

      if(p1.getNom() != null){
         assertTrue(p1.getNom().equals(p2.getNom()));
      }else{
         assertNull(p2.getNom());
      }

      if(p1.getAnonyme() != null){
         assertTrue(p1.getAnonyme().equals(p2.getAnonyme()));
      }else{
         assertNull(p2.getAnonyme());
      }

      if(p1.getAccesAdministration() != null){
         assertTrue(p1.getAccesAdministration().equals(p2.getAccesAdministration()));
      }else{
         assertNull(p2.getAccesAdministration());
      }

      if(p1.getDroitObjets() != null){
         assertTrue(p1.getDroitObjets().equals(p2.getDroitObjets()));
      }else{
         assertNull(p2.getDroitObjets());
      }

      if(p1.getProfilUtilisateurs() != null){
         assertTrue(p1.getProfilUtilisateurs().equals(p2.getProfilUtilisateurs()));
      }else{
         assertNull(p2.getProfilUtilisateurs());
      }

      if(p1.getProfilExport() != null){
         assertTrue(p1.getProfilExport() == p2.getProfilExport());
      }else{
         assertNull(p2.getProfilExport());
      }
   }

   /**
    * @since 2.1
    */
   public void testFindByPlateformeAndArchive(){

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);

      List<Profil> profils = profilDao.findByPlateformeAndArchive(pf1, false);
      assertTrue(profils.size() == 3);
      assertTrue(profils.get(0).getProfilId() == 4);

      profils = profilDao.findByPlateformeAndArchive(pf1, true);
      assertTrue(profils.size() == 1);
      assertTrue(profils.get(0).getProfilId() == 3);

      profils = profilDao.findByPlateformeAndArchive(pf2, false);
      assertTrue(profils.size() == 1);
      assertTrue(profils.get(0).getProfilId() == 5);

      profils = profilDao.findByPlateformeAndArchive(pf2, true);
      assertTrue(profils.size() == 0);

      profils = profilDao.findByPlateformeAndArchive(pf1, null);
      assertTrue(profils.size() == 0);

      profils = profilDao.findByPlateformeAndArchive(null, true);
      assertTrue(profils.size() == 0);
   }

}
