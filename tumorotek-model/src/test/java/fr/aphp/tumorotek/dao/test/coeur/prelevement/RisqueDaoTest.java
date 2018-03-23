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
package fr.aphp.tumorotek.dao.test.coeur.prelevement;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.prelevement.RisqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO RisqueDao et le
 * bean du domaine Risque.
 * Classe de test créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class RisqueDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private RisqueDao risqueDao;
   private PlateformeDao plateformeDao;

   /** Valeur du risque pour la maj. */
   private final String updatedNom = "Nom mis a jour";
   private final Boolean updatedInfectieux = new Boolean(false);

   /**
    * Constructeur.
    */
   public RisqueDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param tDao est le bean Dao.
    */
   public void setRisqueDao(final RisqueDao tDao){
      this.risqueDao = tDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      Risque r1 = risqueDao.findById(1);
      assertTrue(r1.toString().equals("{" + r1.getNom() + "}"));
      r1 = new Risque();
      assertTrue(r1.toString().equals("{Empty Risque}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllRisques(){
      final List<Risque> risques = risqueDao.findAll();
      assertTrue(risques.size() == 3);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<TKThesaurusObject> list = risqueDao.findByOrder(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("GRIPPE A"));
      pf = plateformeDao.findById(2);
      list = risqueDao.findByOrder(pf);
      assertTrue(list.size() == 1);
      list = risqueDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<Risque> risques = risqueDao.findByNom("GRIPPE A");
      assertTrue(risques.size() == 1);
      risques = risqueDao.findByNom("HERPES");
      assertTrue(risques.size() == 0);
      risques = risqueDao.findByNom("GRIPP%");
      assertTrue(risques.size() == 1);
      risques = risqueDao.findByNom(null);
      assertTrue(risques.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByInfectieux().
    */
   public void testFindByInfectieux(){
      List<Risque> risques = risqueDao.findByInfectieux(true);
      assertTrue(risques.size() == 2);
      risques = risqueDao.findByInfectieux(false);
      assertTrue(risques.size() == 1);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Risque> liste = risqueDao.findByExcludedId(1);
      assertTrue(liste.size() == 2);
      final Risque risque = liste.get(0);
      assertNotNull(risque);
      assertTrue(risque.getRisqueId() == 2);

      liste = risqueDao.findByExcludedId(15);
      assertTrue(liste.size() == 3);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un risque.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudRisque() throws Exception{
      final Risque r = new Risque();
      r.setNom("HERPES");
      r.setInfectieux(true);
      r.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      risqueDao.createObject(r);
      assertEquals(new Integer(4), r.getRisqueId());

      // Test de la mise à jour
      final Risque r2 = risqueDao.findById(new Integer(4));
      assertNotNull(r2);
      assertTrue(r2.getNom().equals("HERPES"));
      assertTrue(r2.getInfectieux());
      r2.setNom(updatedNom);
      r2.setInfectieux(updatedInfectieux);
      risqueDao.updateObject(r2);
      assertTrue(risqueDao.findById(new Integer(4)).getNom().equals(updatedNom));
      assertTrue(risqueDao.findById(new Integer(4)).getInfectieux().equals(updatedInfectieux));

      // Test de la délétion
      risqueDao.removeObject(new Integer(4));
      assertNull(risqueDao.findById(new Integer(4)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){

      final Risque r1 = new Risque();
      final Risque r2 = new Risque();

      // L'objet 1 n'est pas égal à null
      assertFalse(r1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(r1.equals(r1));
      // 2 objets sont égaux entre eux
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));

      final String[] risques = new String[] {null, "risque1", "risque2", ""};
      final Plateforme[] pfs = new Plateforme[] {null, plateformeDao.findById(1), plateformeDao.findById(2)};

      for(int i = 0; i < risques.length; i++){
         r1.setNom(risques[i]);
         for(int j = 0; j < pfs.length; j++){
            r1.setPlateforme(pfs[j]);
            for(int k = 0; k < risques.length; k++){
               r2.setNom(risques[k]);
               for(int l = 0; l < pfs.length; l++){
                  r2.setPlateforme(pfs[l]);
                  if((i == k) && (j == l)){
                     assertTrue(r1.equals(r2));
                     assertTrue(r2.equals(r1));
                  }else{
                     assertFalse(r1.equals(r2));
                     assertFalse(r2.equals(r1));
                  }
               }
            }
         }
      }

      //dummy test
      final Banque b = new Banque();
      assertFalse(r1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){

      final Risque r1 = new Risque();
      r1.setRisqueId(1);
      final Risque r2 = new Risque();
      r2.setRisqueId(2);
      final Risque r3 = new Risque();
      r3.setRisqueId(3);

      assertTrue(r1.hashCode() == r2.hashCode());
      assertTrue(r2.hashCode() == r3.hashCode());
      assertTrue(r3.hashCode() > 0);

      final String[] risques = new String[] {null, "risque1", "risque2", ""};
      final Plateforme[] pfs = new Plateforme[] {null, plateformeDao.findById(1), plateformeDao.findById(2)};

      for(int i = 0; i < risques.length; i++){
         r1.setNom(risques[i]);
         for(int j = 0; j < pfs.length; j++){
            r1.setPlateforme(pfs[j]);
            for(int k = 0; k < risques.length; k++){
               r2.setNom(risques[k]);
               for(int l = 0; l < pfs.length; l++){
                  r2.setPlateforme(pfs[l]);
                  if((i == k) && (j == l)){
                     assertTrue(r1.hashCode() == r2.hashCode());
                  }
               }
            }
         }
      }

      final int hash = r1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(r1.hashCode() == r2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == r1.hashCode());
      assertTrue(hash == r1.hashCode());
      assertTrue(hash == r1.hashCode());
      assertTrue(hash == r1.hashCode());

   }

}
