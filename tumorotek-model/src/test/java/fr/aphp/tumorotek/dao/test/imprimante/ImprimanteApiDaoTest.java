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
package fr.aphp.tumorotek.dao.test.imprimante;

import java.util.List;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.imprimante.ImprimanteApiDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;

/**
 *
 * Classe de test pour le DAO ImprimanteApiDao et le bean du domaine Imprimante.
 *
 * @author Pierre Ventadour.
 * @version 18/03/2011
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ImprimanteApiDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ImprimanteApiDao imprimanteApiDao;

   public ImprimanteApiDaoTest(){

   }

   public void setImprimanteApiDao(final ImprimanteApiDao iDao){
      this.imprimanteApiDao = iDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<ImprimanteApi> liste = imprimanteApiDao.findAll();
      assertTrue(liste.size() == 2);
   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<ImprimanteApi> liste = imprimanteApiDao.findByOrder();
      assertTrue(liste.size() == 2);
      assertTrue(liste.get(0).getNom().equals("mbio"));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String nom = "test";
      final String nom2 = "test2";
      final ImprimanteApi ia1 = new ImprimanteApi();
      ia1.setNom(nom);
      final ImprimanteApi ia2 = new ImprimanteApi();
      ia2.setNom(nom);

      // L'objet 1 n'est pas égal à null
      assertFalse(ia1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ia1.equals(ia1));
      // 2 objets sont égaux entre eux
      assertTrue(ia1.equals(ia2));
      assertTrue(ia2.equals(ia1));

      ia1.setNom(null);
      ia2.setNom(null);
      assertTrue(ia1.equals(ia2));

      ia2.setNom(nom);
      assertFalse(ia1.equals(ia2));

      ia1.setNom(nom);
      ia2.setNom(nom2);
      assertFalse(ia1.equals(ia2));

      final Categorie c = new Categorie();
      assertFalse(ia1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws Exception Lance une exception.
    */
   public void testHashCode() throws Exception{
      final String nom = "test";
      final ImprimanteApi ia1 = new ImprimanteApi();
      ia1.setNom(nom);
      final ImprimanteApi ia2 = new ImprimanteApi();
      ia2.setNom(nom);
      final ImprimanteApi ia3 = new ImprimanteApi();

      assertTrue(ia3.hashCode() > 0);

      final int hash = ia1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ia1.hashCode() == ia2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ia1.hashCode());
      assertTrue(hash == ia1.hashCode());
      assertTrue(hash == ia1.hashCode());
      assertTrue(hash == ia1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final ImprimanteApi ia1 = imprimanteApiDao.findById(1);
      assertTrue(ia1.toString().equals("{" + ia1.getNom() + "}"));

      final ImprimanteApi ia2 = new ImprimanteApi();
      assertTrue(ia2.toString().equals("{Empty ImprimanteApi}"));
   }

}
