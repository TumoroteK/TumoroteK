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
package fr.aphp.tumorotek.dao.test.cession;

import java.text.ParseException;

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le bean du domaine CederObjetPK.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class CederObjetPKTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CessionDao cessionDao;
   /** Bean Dao. */
   private EntiteDao entiteDao;

   /** Constructeur. */
   public CederObjetPKTest(){

   }

   public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{
      final CederObjetPK pk1 = new CederObjetPK();
      final CederObjetPK pk2 = new CederObjetPK();

      // L'objet 1 n'est pas égal à null
      assertFalse(pk1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(pk1.equals(pk1));

      /*null*/
      assertTrue(pk1.equals(pk2));
      assertTrue(pk2.equals(pk1));

      populateClefsToTestEqualsAndHashCode();

      final Categorie c3 = new Categorie();
      assertFalse(pk1.equals(c3));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final CederObjetPK pk1 = new CederObjetPK();
      final CederObjetPK pk2 = new CederObjetPK();

      /*null*/
      assertTrue(pk1.hashCode() == pk2.hashCode());

      populateClefsToTestEqualsAndHashCode();

      // un même objet garde le même hashcode dans le temps
      final int hash = pk1.hashCode();
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
      assertTrue(hash == pk1.hashCode());
   }

   private void populateClefsToTestEqualsAndHashCode() throws ParseException{

      final Integer objetId1 = 1;
      final Integer objetId2 = 2;
      final Integer objetId3 = 1;
      final Integer[] objetsId = new Integer[] {null, objetId1, objetId2, objetId3};
      final Cession c1 = cessionDao.findById(1);
      final Cession c2 = cessionDao.findById(2);
      final Cession c3 = cessionDao.findById(1);
      final Cession[] cessions = new Cession[] {null, c1, c2, c3};
      final Entite e1 = entiteDao.findById(3);
      final Entite e2 = entiteDao.findById(8);
      final Entite e3 = entiteDao.findById(3);
      final Entite[] entites = new Entite[] {null, e1, e2, e3};

      final CederObjetPK pk1 = new CederObjetPK();
      final CederObjetPK pk2 = new CederObjetPK();

      for(int i = 0; i < objetsId.length; i++){
         for(int j = 0; j < cessions.length; j++){
            for(int j2 = 0; j2 < entites.length; j2++){

               for(int k = 0; k < objetsId.length; k++){
                  for(int l = 0; l < cessions.length; l++){
                     for(int l2 = 0; l2 < entites.length; l2++){

                        pk1.setObjetId(objetsId[i]);
                        pk1.setCession(cessions[j]);
                        pk1.setEntite(entites[j2]);

                        pk2.setObjetId(objetsId[k]);
                        pk2.setCession(cessions[l]);
                        pk2.setEntite(entites[l2]);

                        if(((i == k) || (i + k == 4)) && ((j == l) || (j + l == 4)) && ((j2 == l2) || (j2 + l2 == 4))){
                           assertTrue(pk1.equals(pk2));
                           assertTrue(pk1.hashCode() == pk2.hashCode());
                        }else{
                           assertFalse(pk1.equals(pk2));
                        }
                     }
                  }
               }
            }
         }
      }

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Integer objetId1 = 1;
      final Cession c1 = cessionDao.findById(1);
      final Entite e1 = entiteDao.findById(3);
      final CederObjetPK pk1 = new CederObjetPK();
      pk1.setObjetId(objetId1);
      pk1.setEntite(e1);
      pk1.setCession(c1);

      assertTrue(pk1.toString().equals("{" + pk1.getCession().toString() + " (Cession), " + pk1.getEntite().toString()
         + " (Entite), " + pk1.getObjetId() + " (ObjetId)}"));

      pk1.setObjetId(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setObjetId(objetId1);

      pk1.setEntite(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setEntite(e1);

      pk1.setCession(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setCession(c1);

      pk1.setObjetId(null);
      pk1.setEntite(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setEntite(e1);
      pk1.setObjetId(objetId1);

      pk1.setEntite(null);
      pk1.setCession(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setCession(c1);
      pk1.setEntite(e1);

      pk1.setObjetId(null);
      pk1.setCession(null);
      assertTrue(pk1.toString().equals("{Empty CederObjetPK}"));
      pk1.setCession(c1);

      final CederObjetPK pk2 = new CederObjetPK();
      assertTrue(pk2.toString().equals("{Empty CederObjetPK}"));
   }

}
