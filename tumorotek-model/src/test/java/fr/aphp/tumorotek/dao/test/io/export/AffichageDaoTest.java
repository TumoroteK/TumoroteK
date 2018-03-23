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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.io.export.AffichageDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO AffichageDao et le
 * bean du domaine Affichage.
 * Classe de test créée le 23/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class AffichageDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private AffichageDao affichageDao;
   private UtilisateurDao utilisateurDao;
   private BanqueDao banqueDao;

   /** Constructeur. */
   public AffichageDaoTest(){}

   /**
    * Setter du bean AffichageDao.
    * @param aDao est le bean Dao.
    */
   public void setAffichageDao(final AffichageDao aDao){
      this.affichageDao = aDao;
   }

   /**
    * Setter du bean UtilisateurDao.
    * @param uDao est le bean Dao.
    */
   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void testFindByBanque(){
      final Banque b1 = banqueDao.findById(1);
      List<Affichage> liste = affichageDao.findByBanque(b1);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2);
      liste = affichageDao.findByBanque(b2);
      assertTrue(liste.size() == 1);

      final Banque b3 = banqueDao.findById(3);
      liste = affichageDao.findByBanque(b3);
      assertTrue(liste.size() == 0);

      liste = affichageDao.findByBanque(null);
      assertTrue(liste.size() == 0);
   }

   public void testFindByBanqueInList(){
      final List<Banque> bks = new ArrayList<>();
      final Banque b1 = banqueDao.findById(1);
      bks.add(b1);
      List<Affichage> liste = affichageDao.findByBanqueInList(bks);
      assertTrue(liste.size() == 3);

      final Banque b2 = banqueDao.findById(2);
      bks.add(b2);
      liste = affichageDao.findByBanqueInList(bks);
      assertTrue(liste.size() == 4);

      liste = affichageDao.findByBanqueInList(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findAffichagesByUtilisateur().
    */
   public void testFindAffichagesByUtilisateur() throws Exception{
      final List<Utilisateur> utilisateurs = this.utilisateurDao.findAll();
      final Iterator<Utilisateur> itUtil = utilisateurs.iterator();
      while(itUtil.hasNext()){
         final Utilisateur utilisateur = itUtil.next();
         final List<Affichage> affichages = this.affichageDao.findByUtilisateur(utilisateur);
         final Iterator<Affichage> it = affichages.iterator();
         while(it.hasNext()){
            assertTrue(it.next().getCreateur().equals(utilisateur));
         }
      }
   }

   /**
   * Test l'appel de la méthode findByIntituleUtilisateur().
   */
   public void testFindByIntituleUtilisateur() throws Exception{
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      List<Affichage> liste = affichageDao.findByIntituleUtilisateur("Essen%", u1);
      assertTrue(liste.size() == 1);

      liste = affichageDao.findByIntituleUtilisateur("yug%", u1);
      assertTrue(liste.size() == 0);

      liste = affichageDao.findByIntituleUtilisateur("Essen%", u2);
      assertTrue(liste.size() == 0);

      liste = affichageDao.findByIntituleUtilisateur(null, u1);
      assertTrue(liste.size() == 0);

      liste = affichageDao.findByIntituleUtilisateur("Essen%", null);
      assertTrue(liste.size() == 0);
   }

   /**
   * Test l'appel de la méthode findByExcludedId().
   */
   public void testFindByExcludedId(){
      final List<Affichage> liste = affichageDao.findAll();
      final Iterator<Affichage> it = liste.iterator();
      while(it.hasNext()){
         final Affichage temp = it.next();
         final List<Affichage> affichages = affichageDao.findByExcludedId(temp.getAffichageId());
         assertTrue(affichages.size() == liste.size() - 1);
         assertFalse(affichages.contains(temp));
      }

   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
   * d'un affichage.
   * @throws Exception lance une exception en cas de problème lors du CRUD.
   */
   @Rollback(false)
   public void testCrudAffichage() throws Exception{
      final Utilisateur createur = this.utilisateurDao.findById(3);
      final String intitule = "intitule";
      final Integer nbLignes = new Integer(25);
      final Banque b1 = banqueDao.findById(1);

      final Affichage a = new Affichage();
      a.setIntitule(intitule);
      a.setCreateur(createur);
      a.setNbLignes(nbLignes);
      a.setBanque(b1);

      // Test de l'insertion
      Integer idObject = new Integer(-1);
      this.affichageDao.createObject(a);
      final List<Affichage> affichages = this.affichageDao.findAll();
      final Iterator<Affichage> itAffichage = affichages.iterator();
      boolean found = false;
      while(itAffichage.hasNext()){
         final Affichage temp = itAffichage.next();
         if(temp.equals(a)){
            found = true;
            idObject = temp.getAffichageId();
            break;
         }
      }
      assertTrue(found);

      // Test de la mise à jour
      final Affichage a2 = this.affichageDao.findByIntitule("intitule").get(0);
      assertNotNull(a2);
      assertNotNull(this.affichageDao.findById(idObject).getIntitule());
      assertTrue(a2.getIntitule().equals("intitule"));
      assertNotNull(this.affichageDao.findById(idObject).getCreateur());
      assertTrue(a2.getCreateur().equals(createur));
      assertNotNull(this.affichageDao.findById(idObject).getNbLignes());
      assertTrue(a2.getNbLignes().equals(25));
      assertNotNull(this.affichageDao.findById(idObject).getBanque());
      assertTrue(a2.getBanque().equals(b1));

      final Utilisateur updatedCreateur = this.utilisateurDao.findById(1);
      final String updatedIntitule = "intitule2";
      final Integer updatedNbLignes = new Integer(30);
      final Banque updatedBanque = banqueDao.findById(2);

      a2.setIntitule(updatedIntitule);
      a2.setCreateur(updatedCreateur);
      a2.setNbLignes(updatedNbLignes);
      a2.setBanque(updatedBanque);

      this.affichageDao.updateObject(a2);
      assertNotNull(this.affichageDao.findById(idObject).getIntitule());
      assertTrue(this.affichageDao.findById(idObject).getIntitule().equals(updatedIntitule));
      assertNotNull(this.affichageDao.findById(idObject).getCreateur());
      assertTrue(this.affichageDao.findById(idObject).getCreateur().equals(updatedCreateur));
      assertNotNull(this.affichageDao.findById(idObject).getNbLignes());
      assertTrue(this.affichageDao.findById(idObject).getNbLignes().equals(updatedNbLignes));
      assertNotNull(this.affichageDao.findById(idObject).getBanque());
      assertTrue(this.affichageDao.findById(idObject).getBanque().equals(updatedBanque));
      // Test de la délétion
      this.affichageDao.removeObject(idObject);
      assertNull(this.affichageDao.findById(idObject));

   }

   /**
    * test toString().
    */
   public void testToString(){
      final Affichage a1 = affichageDao.findById(1);
      assertTrue(a1.toString().equals("{" + a1.getIntitule() + "}"));

      final Affichage a2 = new Affichage();
      assertTrue(a2.toString().equals("{Empty Affichage}"));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      //On boucle sur les 4 possibilités
      for(int i = 0; i < Math.pow(2, 2); i++){
         final Affichage affichage1 = new Affichage();
         final Affichage affichage2 = new Affichage();
         Utilisateur createur = null;
         if(i >= 2){
            createur = utilisateurDao.findById(2);
         }
         affichage1.setCreateur(createur);
         affichage2.setCreateur(createur);
         String intitule = null;
         final int toTest = i % 2;
         if(toTest > 0){
            intitule = "Intitule";
         }
         affichage1.setIntitule(intitule);
         affichage2.setIntitule(intitule);
         //On compare les 2 affichages
         assertTrue(affichage1.equals(affichage2));
      }
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      //On boucle sur les 4 possibilités
      for(int i = 0; i < Math.pow(2, 2); i++){
         final Affichage affichage = new Affichage();
         int hash = 7;
         Utilisateur createur = null;
         int hashCreateur = 0;
         if(i >= 2){
            createur = utilisateurDao.findById(3);
            hashCreateur = createur.hashCode();
         }
         final int toTest = i % 2;
         String intitule = null;
         int hashIntitule = 0;
         if(toTest > 0){
            intitule = "Intitule";
            hashIntitule = intitule.hashCode();
         }
         hash = 31 * hash + hashCreateur;
         hash = 31 * hash + hashIntitule;
         affichage.setCreateur(createur);
         affichage.setIntitule(intitule);
         //On vérifie que le hashCode est bon
         assertTrue(affichage.hashCode() == hash);
         assertTrue(affichage.hashCode() == hash);
         assertTrue(affichage.hashCode() == hash);
      }
   }

}
