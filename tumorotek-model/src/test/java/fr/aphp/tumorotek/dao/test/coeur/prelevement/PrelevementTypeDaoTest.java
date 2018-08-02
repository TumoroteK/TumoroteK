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

import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO PrelevementTypeDao et le
 * bean du domaine PrelevementType.
 * Classe de test créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PrelevementTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private PrelevementTypeDao prelevementTypeDao;
   private PlateformeDao plateformeDao;

   /** Valeur du type pour la maj. */
   private final String updatedType = "Type mis a jour";
   private final String updatedInca = "M";

   /**
    * Constructeur.
    */
   public PrelevementTypeDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param tDao est le bean Dao.
    */
   public void setPrelevementTypeDao(final PrelevementTypeDao tDao){
      this.prelevementTypeDao = tDao;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      PrelevementType pt1 = prelevementTypeDao.findById(1);
      assertTrue(pt1.toString().equals("{" + pt1.getType() + ", " + pt1.getIncaCat() + "}"));
      pt1 = new PrelevementType();
      assertTrue(pt1.toString().equals("{Empty PrelevementType}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllTypes(){
      final List<PrelevementType> types = prelevementTypeDao.findAll();
      assertTrue(types.size() == 4);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = prelevementTypeDao.findByPfOrder(pf);
      assertTrue(list.size() == 3);
      assertTrue(list.get(0).getNom().equals("BIOPSIE"));
      pf = plateformeDao.findById(2);
      list = prelevementTypeDao.findByPfOrder(pf);
      assertTrue(list.size() == 1);
      list = prelevementTypeDao.findByPfOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByType().
    */
   public void testFindByType(){
      List<PrelevementType> types = prelevementTypeDao.findByType("BIOPSIE");
      assertTrue(types.size() == 1);
      types = prelevementTypeDao.findByType("PIECE OPERATOIRE");
      assertTrue(types.size() == 0);
      types = prelevementTypeDao.findByType("%OPSIE");
      assertTrue(types.size() == 2);
      types = prelevementTypeDao.findByType(null);
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByIncaCat().
    */
   public void testFindByIncaCat(){
      List<PrelevementType> types = prelevementTypeDao.findByIncaCat("P");
      assertTrue(types.size() == 2);
      types = prelevementTypeDao.findByType("O");
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<PrelevementType> liste = prelevementTypeDao.findByExcludedId(1);
      assertTrue(liste.size() == 3);
      final PrelevementType type = liste.get(0);
      assertNotNull(type);
      assertTrue(type.getPrelevementTypeId() == 2);

      liste = prelevementTypeDao.findByExcludedId(15);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un type de prelevement.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudPrelevementType() throws Exception{
      final PrelevementType t = new PrelevementType();
      t.setType("PIECE OPERATOIRE");
      t.setIncaCat("O");
      t.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      prelevementTypeDao.createObject(t);
      assertEquals(new Integer(5), t.getPrelevementTypeId());

      // Test de la mise à jour
      final PrelevementType t2 = prelevementTypeDao.findById(new Integer(5));
      assertNotNull(t2);
      assertTrue(t2.getType().equals("PIECE OPERATOIRE"));
      assertTrue(t2.getIncaCat().equals("O"));
      t2.setType(updatedType);
      t2.setIncaCat(updatedInca);
      prelevementTypeDao.updateObject(t2);
      assertTrue(prelevementTypeDao.findById(new Integer(5)).getType().equals(updatedType));
      assertTrue(prelevementTypeDao.findById(new Integer(5)).getIncaCat().equals(updatedInca));

      // Test de la délétion
      prelevementTypeDao.removeObject(new Integer(5));
      assertNull(prelevementTypeDao.findById(new Integer(5)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "Type";
      final String type2 = "Type2";
      final String inca = "I";
      final String inca2 = "J";
      final PrelevementType t1 = new PrelevementType();
      //t1.setType(type);
      final PrelevementType t2 = new PrelevementType();
      //t2.setType(type);

      // L'objet 1 n'est pas égal à null
      assertFalse(t1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(t1.equals(t1));
      // 2 objets sont égaux entre eux
      assertTrue(t1.equals(t2));
      assertTrue(t2.equals(t1));

      final String[] types = new String[] {null, type, type2, ""};
      final String[] incas = new String[] {null, inca, inca2, ""};

      for(int i = 0; i < types.length; i++){
         t1.setType(types[i]);
         for(int j = 0; j < incas.length; j++){
            t1.setIncaCat(incas[j]);
            for(int k = 0; k < types.length; k++){
               t2.setType(types[k]);
               for(int l = 0; l < incas.length; l++){
                  t2.setIncaCat(incas[l]);
                  if((i == k) && (j == l)){
                     assertTrue(t1.equals(t2));
                     assertTrue(t2.equals(t1));
                  }else{
                     assertFalse(t1.equals(t2));
                     assertFalse(t2.equals(t1));
                  }
               }
            }
         }
      }

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      t1.setType(t2.getType());
      t1.setIncaCat(t2.getIncaCat());
      t1.setPlateforme(pf1);
      t2.setPlateforme(pf1);
      assertTrue(t1.equals(t2));
      t2.setPlateforme(pf2);
      assertFalse(t1.equals(t2));
      //dummy test
      final Banque b = new Banque();
      assertFalse(t1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "Type";
      final String inca = "Inca";
      final PrelevementType t1 = new PrelevementType();
      t1.setPrelevementTypeId(1);
      t1.setType(type);
      t1.setIncaCat(inca);
      final PrelevementType t2 = new PrelevementType();
      t2.setPrelevementTypeId(2);
      t2.setType(type);
      t2.setIncaCat(inca);
      final PrelevementType t3 = new PrelevementType();
      t1.setPrelevementTypeId(3);
      assertTrue(t3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      t1.setPlateforme(pf1);
      t2.setPlateforme(pf1);
      t3.setPlateforme(pf2);

      final int hash = t1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(t1.hashCode() == t2.hashCode());
      assertFalse(t1.hashCode() == t3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());
      assertTrue(hash == t1.hashCode());

   }

}
