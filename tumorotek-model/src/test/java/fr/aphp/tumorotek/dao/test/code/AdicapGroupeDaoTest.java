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
package fr.aphp.tumorotek.dao.test.code;

import java.util.List;

import fr.aphp.tumorotek.dao.code.AdicapGroupeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.contexte.Categorie;

public class AdicapGroupeDaoTest extends AbstractDaoTest
{

   private AdicapGroupeDao adicapGroupeDao;

   /**
    * Constructeur.
    */
   public AdicapGroupeDaoTest(){}

   @Override
   protected String[] getConfigLocations(){
      return new String[] {"applicationContextDao-codes-test-mysql.xml"};
   }

   public void setAdicapGroupeDao(final AdicapGroupeDao agDao){
      this.adicapGroupeDao = agDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllAdicapGroupes(){
      final List<AdicapGroupe> adicapGroupes = adicapGroupeDao.findAll();
      assertTrue(adicapGroupes.size() == 58);
   }

   public void testFindDictionnaires(){
      final List<AdicapGroupe> adicapGroupes = adicapGroupeDao.findDictionnaires();
      assertTrue(adicapGroupes.size() == 8);
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final Integer id1 = 1;
      final Integer id2 = 2;
      final AdicapGroupe a1 = new AdicapGroupe();
      final AdicapGroupe a2 = new AdicapGroupe();

      // L'objet 1 n'est pas égal à null
      assertFalse(a1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(a1.equals(a1));

      /*null --> Ids ne pouvant etre nuls car table systemes*/
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));

      /*Id*/
      a2.setAdicapGroupeId(id1);
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));
      a1.setAdicapGroupeId(id2);
      assertFalse(a1.equals(a2));
      assertFalse(a2.equals(a1));
      a1.setAdicapGroupeId(id1);
      assertTrue(a1.equals(a2));
      assertTrue(a2.equals(a1));

      final Categorie c = new Categorie();
      assertFalse(a1.equals(c));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){

      final Integer id1 = 1;
      final AdicapGroupe a1 = new AdicapGroupe();
      a1.setAdicapGroupeId(id1);
      final AdicapGroupe a2 = new AdicapGroupe();
      a2.setAdicapGroupeId(id1);
      final AdicapGroupe a3 = new AdicapGroupe();
      a3.setAdicapGroupeId(null);
      assertTrue(a3.hashCode() > 0);

      final int hash = a1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(a1.hashCode() == a2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());
      assertTrue(hash == a1.hashCode());

   }

   public void testToString(){
      final Adicap a = new Adicap();
      a.setCode("Disease");
      assertTrue(a.toString().equals("{Adicap: Disease}"));
   }

}
