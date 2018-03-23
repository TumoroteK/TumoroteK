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
package fr.aphp.tumorotek.dao.test.coeur.patient;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Classe de test pour le DAO LienFamilialDao et le
 * bean du domaine LienFamilial.
 * Classe de test créée le 28/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class LienFamilialDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private LienFamilialDao lienDao;

   /**
    * Constructeur.
    */
   public LienFamilialDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param lDao est le bean Dao.
    */
   public void setLienFamilialDao(final LienFamilialDao lDao){
      this.lienDao = lDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      LienFamilial l1 = lienDao.findById(1);
      assertTrue(l1.toString().equals("{" + l1.getNom() + "}"));
      l1 = new LienFamilial();
      assertTrue(l1.toString().equals("{Empty LienFamilial}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllLienFamilials(){
      final List<LienFamilial> liens = lienDao.findAll();
      assertTrue(liens.size() == 6);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<LienFamilial> prels = lienDao.findByExcludedId(1);
      assertTrue(prels.size() == 5);
      prels = lienDao.findByExcludedId(8);
      assertTrue(prels.size() == 6);
      prels = lienDao.findByExcludedId(null);
      assertTrue(prels.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<LienFamilial> liens = lienDao.findByNom("%Pere%");
      assertTrue(liens.size() == 2);
      liens = lienDao.findByNom("Niece");
      assertTrue(liens.size() == 0);
      liens = lienDao.findByNom("Tante-Neveu");
      assertTrue(liens.size() == 1);
      liens = lienDao.findByNom(null);
      assertTrue(liens.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un lien.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudLienFamilial() throws Exception{
      final LienFamilial r = new LienFamilial();
      r.setNom("Neveu-Tante");
      r.setReciproque(lienDao.findByNom("Tante-Neveu").get(0));
      r.setAscendant(true);
      // Test de l'insertion
      lienDao.createObject(r);
      assertEquals(new Integer(7), r.getLienFamilialId());

      // Test de la mise à jour
      final LienFamilial l2 = lienDao.findById(new Integer(7));
      assertNotNull(l2);
      assertTrue(l2.getNom().equals("Neveu-Tante"));
      assertTrue(l2.getReciproque().equals(lienDao.findByNom("Tante-Neveu").get(0)));
      assertTrue(l2.getAscendant());
      l2.setNom("GandPere-PetitFils");
      l2.setReciproque(null);
      l2.setAscendant(false);
      lienDao.updateObject(l2);
      assertTrue(lienDao.findById(new Integer(7)).getNom().equals("GandPere-PetitFils"));
      assertTrue(lienDao.findById(new Integer(7)).getReciproque() == null);
      assertFalse(lienDao.findById(new Integer(7)).getAscendant());

      // Test de la délétion
      lienDao.removeObject(new Integer(7));
      assertNull(lienDao.findById(new Integer(7)));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){

      final LienFamilial l1 = new LienFamilial();
      final LienFamilial l2 = new LienFamilial();

      // L'objet 1 n'est pas égal à null
      assertFalse(l1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(l1.equals(l1));
      // 2 objets sont égaux entre eux
      assertTrue(l1.equals(l2));
      assertTrue(l2.equals(l1));

      final String[] noms = new String[] {null, "lien1", "lien2", "lien1"};

      for(int i = 0; i < noms.length; i++){
         l1.setNom(noms[i]);
         for(int k = 0; k < noms.length; k++){
            l2.setNom(noms[k]);
            if((i == k) || (k + i == 4)){
               assertTrue(l1.equals(l2));
               assertTrue(l2.equals(l1));
            }else{
               assertFalse(l1.equals(l2));
               assertFalse(l2.equals(l1));
            }
         }
      }

      //dummy test
      final Banque b = new Banque();
      assertFalse(l1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){

      final LienFamilial l1 = new LienFamilial();
      l1.setLienFamilialId(1);
      final LienFamilial l2 = new LienFamilial();
      l2.setLienFamilialId(2);
      final LienFamilial r3 = new LienFamilial();
      r3.setLienFamilialId(3);

      assertTrue(l1.hashCode() == l2.hashCode());
      assertTrue(l2.hashCode() == r3.hashCode());
      assertTrue(r3.hashCode() > 0);

      final String[] noms = new String[] {null, "lien1", "lien2", ""};

      for(int i = 0; i < noms.length; i++){
         l1.setNom(noms[i]);
         for(int k = 0; k < noms.length; k++){
            l2.setNom(noms[k]);
            if(i == k){
               assertTrue(l1.hashCode() == l2.hashCode());
            }
         }
      }

      final int hash = l1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(l1.hashCode() == l2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());

   }

}
