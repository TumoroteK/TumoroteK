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

import fr.aphp.tumorotek.dao.io.export.ChampDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.dao.io.export.CombinaisonDao;
import fr.aphp.tumorotek.dao.io.export.CritereDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Combinaison;
import fr.aphp.tumorotek.model.io.export.Critere;

/**
 *
 * Classe de test pour le DAO CritereDao et le
 * bean du domaine Critere.
 * Classe de test créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class CritereDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private CritereDao critereDao;
   private ChampDao champDao;
   private CombinaisonDao combinaisonDao;
   private ChampEntiteDao champEntiteDao;

   /** Constructeur. */
   public CritereDaoTest(){}

   public void setCritereDao(final CritereDao cDao){
      this.critereDao = cDao;
   }

   public void setChampDao(final ChampDao cDao){
      this.champDao = cDao;
   }

   public void setCombinaisonDao(final CombinaisonDao coDao){
      this.combinaisonDao = coDao;
   }

   public void setChampEntiteDao(final ChampEntiteDao ceDao){
      this.champEntiteDao = ceDao;
   }

   /**
     * Test l'insertion, la mise à jour et la suppression 
    * d'un critere.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudCritere() throws Exception{
      final String operateur = "=";
      final String valeur = "valeur";

      ChampEntite champEntite = this.champEntiteDao.findById(55);
      Champ ch = new Champ(champEntite);
      this.champDao.createObject(ch);
      final int idCh1 = ch.getChampId();
      champEntite = this.champEntiteDao.findById(22);
      ch = new Champ(champEntite);
      this.champDao.createObject(ch);
      final int idCh2 = ch.getChampId();
      champEntite = this.champEntiteDao.findById(44);
      ch = new Champ(champEntite);
      this.champDao.createObject(ch);
      final int idCh3 = ch.getChampId();
      champEntite = this.champEntiteDao.findById(22);
      ch = new Champ(champEntite);
      this.champDao.createObject(ch);
      final int idCh4 = ch.getChampId();
      champEntite = this.champEntiteDao.findById(21);
      ch = new Champ(champEntite);
      this.champDao.createObject(ch);
      final int idCh = ch.getChampId();

      final Champ champ1 = this.champDao.findById(idCh1);
      final Champ champ2 = this.champDao.findById(idCh2);
      final Champ champ3 = this.champDao.findById(idCh3);
      final Champ champ4 = this.champDao.findById(idCh4);
      final Champ champ = this.champDao.findById(idCh);

      //On crée les combinaisons
      Combinaison co = new Combinaison(champ1, "+", champ2);
      this.combinaisonDao.createObject(co);
      final int idCo1 = co.getCombinaisonId();
      co = new Combinaison(champ3, "-", champ4);
      this.combinaisonDao.createObject(co);
      final int idCo2 = co.getCombinaisonId();

      final Combinaison combinaison = this.combinaisonDao.findById(idCo1);

      //On implémente un critère
      final Critere c = new Critere();
      c.setChamp(champ);
      c.setOperateur(operateur);
      c.setCombinaison(combinaison);
      c.setValeur(valeur);

      // Test de l'insertion
      Integer idObject = new Integer(-1);
      this.critereDao.createObject(c);
      final List<Critere> criteres = this.critereDao.findAll();
      final Iterator<Critere> itCritere = criteres.iterator();
      boolean found = false;
      while(itCritere.hasNext()){
         final Critere temp = itCritere.next();
         if(temp.equals(c)){
            found = true;
            idObject = temp.getCritereId();
            break;
         }
      }
      assertTrue(found);

      // Test de la mise à jour
      final Critere c2 = this.critereDao.findById(idObject);
      assertNotNull(c2);
      assertNotNull(c2.getOperateur());
      assertTrue(c2.getOperateur().equals(operateur));
      if(c2.getChamp() != null){
         assertTrue(c2.getChamp().equals(champ));
      }else{
         assertNull(champ);
      }
      if(c2.getCombinaison() != null){
         assertTrue(c2.getCombinaison().equals(combinaison));
      }else{
         assertNull(combinaison);
      }
      assertNotNull(c2.getValeur());
      assertTrue(c2.getValeur().equals(valeur));

      champEntite = this.champEntiteDao.findById(33);
      final Champ updatedChamp = new Champ(champEntite);
      this.champDao.createObject(updatedChamp);
      final int idChUpd = updatedChamp.getChampId();
      final String updatedOperateur = "!=";
      final Combinaison updatedCombinaison = this.combinaisonDao.findById(idCo2);
      final String updatedValeur = "valeur2";

      c2.setChamp(updatedChamp);
      c2.setOperateur(updatedOperateur);
      c2.setCombinaison(updatedCombinaison);
      c2.setValeur(updatedValeur);

      this.critereDao.updateObject(c2);
      assertNotNull(this.critereDao.findById(idObject).getOperateur());
      assertTrue(this.critereDao.findById(idObject).getOperateur().equals(updatedOperateur));
      if(this.critereDao.findById(idObject).getChamp() != null){
         assertTrue(this.critereDao.findById(idObject).getChamp().equals(updatedChamp));
      }else{
         assertNull(updatedChamp);
      }
      if(this.critereDao.findById(idObject).getCombinaison() != null){
         assertTrue(this.critereDao.findById(idObject).getCombinaison().equals(updatedCombinaison));
      }else{
         assertNull(updatedCombinaison);
      }
      assertNotNull(this.critereDao.findById(idObject).getValeur());
      assertTrue(this.critereDao.findById(idObject).getValeur().equals(updatedValeur));
      // Test de la délétion
      this.critereDao.removeObject(idObject);
      assertNull(this.critereDao.findById(idObject));

      //On supprime tous les éléments créés
      this.combinaisonDao.removeObject(idCo1);
      this.combinaisonDao.removeObject(idCo2);
      this.champDao.removeObject(idCh);
      this.champDao.removeObject(idChUpd);
   }

   /**
    * test toString().
    */
   public void testToString(){
      final Critere c1 = critereDao.findById(1);
      assertTrue(c1.toString().equals(c1.getChamp().toString() + " " + c1.getOperateur() + " " + c1.getValeur()));

      final Critere c2 = new Critere();
      assertTrue(c2.toString().equals("{Empty Critere}"));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      //On boucle sur les 16 possibilités
      for(int i = 0; i < Math.pow(2, 4); i++){
         final Critere critere1 = new Critere();
         final Critere critere2 = new Critere();
         String operateur = null;
         if(i >= 8){
            operateur = "=";
         }
         critere1.setOperateur(operateur);
         critere2.setOperateur(operateur);
         int toTest = i % 8;
         String valeur = null;
         if(toTest >= 4){
            valeur = new String("%B.0");
         }
         critere1.setValeur(valeur);
         critere2.setValeur(valeur);
         toTest = i % 4;
         Champ champ = null;
         if(toTest >= 2){
            champ = champDao.findById(2);
         }
         critere1.setChamp(champ);
         critere2.setChamp(champ);
         toTest = toTest % 2;
         Combinaison combinaison = null;
         if(toTest > 0){
            combinaison = combinaisonDao.findById(1);
         }
         critere1.setCombinaison(combinaison);
         critere2.setCombinaison(combinaison);
         //On compare les 2 criteres
         assertTrue(critere1.equals(critere2));
      }
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      //On boucle sur les 16 possibilités
      for(int i = 0; i < Math.pow(2, 4); i++){
         final Critere critere = new Critere();
         int hash = 7;
         String valeur = null;
         int hashValeur = 0;
         if(i >= 8){
            valeur = new String("0A00%");
            hashValeur = valeur.hashCode();
         }
         int toTest = i % 8;
         Champ champ = null;
         int hashChamp = 0;
         if(toTest >= 4){
            champ = champDao.findById(3);
            hashChamp = champ.hashCode();
         }
         toTest = toTest % 4;
         Combinaison combinaison = null;
         int hashCombinaison = 0;
         if(toTest >= 2){
            combinaison = combinaisonDao.findById(3);
            hashCombinaison = combinaison.hashCode();
         }
         toTest = i % 2;
         String operateur = null;
         int hashOperateur = 0;
         if(toTest > 0){
            operateur = "=";
            hashOperateur = operateur.hashCode();
         }
         hash = 31 * hash + hashValeur;
         hash = 31 * hash + hashChamp;
         hash = 31 * hash + hashCombinaison;
         hash = 31 * hash + hashOperateur;
         critere.setOperateur(operateur);
         critere.setValeur(valeur);
         critere.setChamp(champ);
         critere.setCombinaison(combinaison);
         //On vérifie que le hashCode est bon
         assertTrue(critere.hashCode() == hash);
         assertTrue(critere.hashCode() == hash);
         assertTrue(critere.hashCode() == hash);
      }
   }
}
