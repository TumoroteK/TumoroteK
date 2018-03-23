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
package fr.aphp.tumorotek.dao.test.qualite;

import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;

/**
 *
 * Classe de test pour le DAO OperationTypeDao et le
 * bean du domaine OperationType.
 * Classe de test créée le 14/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class OperationTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private OperationTypeDao operationTypeDao;

   /**
    * Constructeur.
    */
   public OperationTypeDaoTest(){}

   /**
    * Properties Dao.
    */
   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllOperationTypes(){
      final List<OperationType> ops = operationTypeDao.findAll();
      assertTrue(ops.size() == 23);
   }

   /**
    * Test l'appel de la méthode findByNom().
    */
   public void testFindByNom(){
      List<OperationType> ops = operationTypeDao.findByNom("Consultation");
      assertTrue(ops.size() == 1);
      ops = operationTypeDao.findByNom("Quitter");
      assertTrue(ops.size() == 0);
      ops = operationTypeDao.findByNom("Modif%");
      assertTrue(ops.size() == 2);
      ops = operationTypeDao.findByNom(null);
      assertTrue(ops.size() == 0);
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      OperationType o1 = operationTypeDao.findById(1);
      assertTrue(o1.toString().equals("{" + o1.getNom() + "}"));
      o1 = new OperationType();
      assertTrue(o1.toString().equals("{Empty OperationType}"));
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'une OperationType.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudOperationType() throws Exception{
      final OperationType o = new OperationType();
      o.setNom("Quitter");
      o.setProfilable(true);
      // Test de l'insertion
      operationTypeDao.createObject(o);

      // Test de la mise à jour
      final OperationType o2 = operationTypeDao.findById(o.getOperationTypeId());
      assertNotNull(o2);
      assertTrue(o2.getNom().equals(o.getNom()));
      assertTrue(o2.getProfilable().equals(o.getProfilable()));

      o2.setNom("Revenir");
      o2.setProfilable(false);
      operationTypeDao.updateObject(o2);
      assertTrue(operationTypeDao.findById(o2.getOperationTypeId()).getNom().equals("Revenir"));
      assertFalse(operationTypeDao.findById(o2.getOperationTypeId()).getProfilable());

      // Test de la délétion
      operationTypeDao.removeObject(o2.getOperationTypeId());
      assertNull(operationTypeDao.findById(o2.getOperationTypeId()));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final OperationType o1 = new OperationType();
      final OperationType o2 = new OperationType();

      // L'objet 1 n'est pas égal à null
      assertFalse(o1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(o1.equals(o1));
      // 2 objets sont égaux entre eux
      assertTrue(o1.equals(o2));
      assertTrue(o2.equals(o1));

      final String[] noms = new String[] {null, "nom1", "nom2", ""};

      for(int i = 0; i < noms.length; i++){
         for(int j = 0; j < noms.length; j++){
            o1.setNom(noms[i]);
            o2.setNom(noms[j]);
            if(i == j){
               assertTrue(o1.equals(o2));
            }else{
               assertFalse(o1.equals(o2));
            }
         }
      }

      //dummy test
      final Banque b = new Banque();
      assertFalse(o1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final OperationType o1 = new OperationType();
      o1.setOperationTypeId(1);
      final OperationType o2 = new OperationType();
      o2.setOperationTypeId(2);
      final OperationType t3 = new OperationType();
      o1.setOperationTypeId(3);
      assertTrue(t3.hashCode() > 0);

      final String[] noms = new String[] {null, "nom1", "nom2", ""};

      for(int i = 0; i < noms.length; i++){
         for(int j = 0; j < noms.length; j++){
            o1.setNom(noms[i]);
            o2.setNom(noms[j]);
            if(i == j){
               assertTrue(o1.hashCode() == o2.hashCode());
            }
         }
      }

      final int hash = o1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(o1.hashCode() == o2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == o1.hashCode());
      assertTrue(hash == o1.hashCode());
      assertTrue(hash == o1.hashCode());
      assertTrue(hash == o1.hashCode());
   }

}
