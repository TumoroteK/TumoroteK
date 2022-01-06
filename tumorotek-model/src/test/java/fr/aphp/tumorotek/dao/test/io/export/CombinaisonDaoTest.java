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
package fr.aphp.tumorotek.dao.test.io.export;

import java.util.Iterator;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.CombinaisonDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Combinaison;

/**
 *
 * Classe de test pour le DAO CombinaisonDao et le
 * bean du domaine Combinaison.
 * Classe de test créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class CombinaisonDaoTest extends AbstractDaoTest
{


   @Autowired
 CombinaisonDao combinaisonDao;
   @Autowired
 ChampDao champDao;
   @Autowired
 EntiteDao entiteDao;
   @Autowired
 ChampEntiteDao champEntiteDao;
   @Autowired
 DataTypeDao dataTypeDao;

   /** Constructeur. */
   public CombinaisonDaoTest(){}

   /**
    * Setter du bean CombinaisonDao.
    * @param oDao est le bean Dao.
    */
   @Test
public void setCombinaisonDao(final CombinaisonDao oDao){
      this.combinaisonDao = oDao;
   }

   /**
    * Setter du bean ChampDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   /**
    * Setter du bean EntiteDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Setter du bean EntiteDao.
    * @param cDao est le bean Dao.
    */
   @Test
public void setChampEntiteDao(final ChampEntiteDao ceDao){
      this.champEntiteDao = ceDao;
   }

   /**
    * Setter du bean DataTypeDao.
    * @param dtDao est le bean Dao.
    */
   @Test
public void setDataTypeDao(final DataTypeDao dtDao){
      this.dataTypeDao = dtDao;
   }

   /**
     * Test l'insertion, la mise à jour et la suppression 
    * d'une combinaison.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   @Test
public void testCrudCombinaison() throws Exception{
      DataType dataType = dataTypeDao.findById(2);
      ChampEntite chEntite =
         new ChampEntite(this.entiteDao.findById(2), "champEntite1", dataType, false, true, "000-0", false, null);
      this.champEntiteDao.save(chEntite);
      final int idChEn1 = chEntite.getId();
      Champ ch = new Champ(chEntite);
      this.champDao.save(ch);
      final int id1 = ch.getChampId();
      dataType = dataTypeDao.findById(1);
      chEntite = new ChampEntite(this.entiteDao.findById(1), "champEntite2", dataType, false, false, null, false, null);
      this.champEntiteDao.save(chEntite);
      final int idChEn2 = chEntite.getId();
      ch = new Champ(chEntite);
      this.champDao.save(ch);
      final int id2 = ch.getChampId();

      final Champ champ1 = this.champDao.findById(id1);
      final Champ champ2 = this.champDao.findById(id2);

      final String operateur = "+";

      final Combinaison c = new Combinaison();
      c.setChamp1(champ1);
      c.setChamp2(champ2);
      c.setOperateur(operateur);

      // Test de l'insertion
      Integer idObject = new Integer(-1);
      this.combinaisonDao.save(c);
      final List<Combinaison> combinaisons = this.IterableUtils.toList(combinaisonDao.findAll());
      final Iterator<Combinaison> itCombinaison = combinaisons.iterator();
      boolean found = false;
      while(itCombinaison.hasNext()){
         final Combinaison temp = itCombinaison.next();
         if(temp.equals(c)){
            found = true;
            idObject = temp.getCombinaisonId();
            break;
         }
      }
      assertTrue(found);

      // Test de la mise à jour
      final Combinaison c2 = this.combinaisonDao.findById(idObject);
      assertNotNull(c2);
      assertNotNull(c2.getChamp1());
      assertTrue(c2.getChamp1().equals(champ1));
      assertNotNull(c2.getChamp2());
      assertTrue(c2.getChamp2().equals(champ2));
      assertNotNull(c2.getOperateur());
      assertTrue(c2.getOperateur().equals(operateur));

      final Champ updatedChamp1 = this.champDao.findById(id2);
      final Champ updatedChamp2 = this.champDao.findById(id1);
      final String updatedOperateur = "-";

      c2.setChamp1(updatedChamp1);
      c2.setChamp2(updatedChamp2);
      c2.setOperateur(updatedOperateur);

      this.combinaisonDao.save(c2);
      assertNotNull(this.combinaisonDao.findById(idObject).getChamp1());
      assertTrue(this.combinaisonDao.findById(idObject).getChamp1().equals(updatedChamp1));
      assertNotNull(this.combinaisonDao.findById(idObject).getChamp2());
      assertTrue(this.combinaisonDao.findById(idObject).getChamp2().equals(updatedChamp2));
      assertNotNull(this.combinaisonDao.findById(idObject).getOperateur());
      assertTrue(this.combinaisonDao.findById(idObject).getOperateur().equals(updatedOperateur));

      // Test de la délétion
      this.combinaisonDao.deleteById(idObject);
      assertNull(this.combinaisonDao.findById(idObject));

      //On supprimé les éléments créés
      this.champDao.deleteById(id1);
      this.champDao.deleteById(id2);
      this.champEntiteDao.deleteById(idChEn1);
      this.champEntiteDao.deleteById(idChEn2);
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   @Test
public void testEquals(){
      //On boucle sur les 8 possibilités
      for(int i = 0; i < Math.pow(2, 3); i++){
         final Combinaison combinaison1 = new Combinaison();
         final Combinaison combinaison2 = new Combinaison();
         String operateur = null;
         if(i >= 4){
            operateur = new String("+");
         }
         combinaison1.setOperateur(operateur);
         combinaison2.setOperateur(operateur);
         int toTest = i % 4;
         Champ champ1 = null;

         if(toTest >= 2){
            champ1 = champDao.findById(2);
         }
         combinaison1.setChamp1(champ1);
         combinaison2.setChamp1(champ1);
         toTest = toTest % 2;
         Champ champ2 = null;
         if(toTest > 0){
            champ2 = champDao.findById(3);
         }
         combinaison1.setChamp2(champ2);
         combinaison2.setChamp2(champ2);
         //On compare les 2 combinaisons
         assertTrue(combinaison1.equals(combinaison2));
      }
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   @Test
public void testHashCode(){
      //On boucle sur les 8 possibilités
      for(int i = 0; i < Math.pow(2, 3); i++){
         final Combinaison combinaison = new Combinaison();
         int hash = 7;
         Champ champ1 = null;
         int hashChamp1 = 0;
         if(i >= 4){
            champ1 = champDao.findById(2);
            hashChamp1 = champ1.hashCode();
         }
         int toTest = i % 4;
         Champ champ2 = null;
         int hashChamp2 = 0;
         if(toTest >= 2){
            champ2 = champDao.findById(3);
            hashChamp2 = champ2.hashCode();
         }
         toTest = toTest % 2;
         String operateur = null;
         int hashOperateur = 0;
         if(toTest > 0){
            operateur = new String("+");
            hashOperateur = operateur.hashCode();
         }
         hash = 31 * hash + hashChamp1;
         hash = 31 * hash + hashChamp2;
         hash = 31 * hash + hashOperateur;
         combinaison.setChamp1(champ1);
         combinaison.setChamp2(champ2);
         combinaison.setOperateur(operateur);
         //On vérifie que le hashCode est bon
         assertTrue(combinaison.hashCode() == hash);
         assertTrue(combinaison.hashCode() == hash);
         assertTrue(combinaison.hashCode() == hash);
      }
   }

   /**
    * test toString().
    */
   @Test
public void testToString(){
      final Combinaison c1 = combinaisonDao.findById(1);
      if(c1.getChamp1() != null && c1.getChamp2() != null && c1.getOperateur() != null){
         assertTrue(c1.toString().equals(c1.getChamp1().toString() + " " + c1.getOperateur() + " " + c1.getChamp2()));
      }else{
         assertTrue(c1.toString().equals("{" + c1.getCombinaisonId() + "}"));
      }

      final Combinaison c2 = new Combinaison();
      assertTrue(c2.toString().equals("{Empty Combinaison}"));
   }
}
