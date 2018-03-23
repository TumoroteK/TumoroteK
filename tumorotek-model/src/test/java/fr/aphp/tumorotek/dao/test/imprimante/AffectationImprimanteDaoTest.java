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

import java.text.ParseException;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.AffectationImprimanteDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimantePK;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le DAO AffectationImprimanteDao et le bean
 * du domaine AffectationImprimante.
 *
 * @author Pierre Ventadour.
 * @version 21/03/2011.
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class AffectationImprimanteDaoTest extends AbstractDaoTest
{

   /** Bean DAO. */
   private AffectationImprimanteDao affectationImprimanteDao;
   /** Bean DAO. */
   private UtilisateurDao utilisateurDao;
   /** Bean DAO. */
   private BanqueDao banqueDao;
   /** Bean DAO. */
   private ImprimanteDao imprimanteDao;
   /** Bean DAO. */
   private ModeleDao modeleDao;

   public AffectationImprimanteDaoTest(){

   }

   public void setAffectationImprimanteDao(final AffectationImprimanteDao aDao){
      this.affectationImprimanteDao = aDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setImprimanteDao(final ImprimanteDao iDao){
      this.imprimanteDao = iDao;
   }

   public void setModeleDao(final ModeleDao mDao){
      this.modeleDao = mDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<AffectationImprimante> liste = affectationImprimanteDao.findAll();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findById().
    */
   public void testFindById(){
      final Imprimante i1 = imprimanteDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      AffectationImprimantePK pk = new AffectationImprimantePK(u1, b1, i1);

      AffectationImprimante ai = affectationImprimanteDao.findById(pk);
      assertNotNull(ai);

      pk = new AffectationImprimantePK(u1, b3, i1);
      ai = affectationImprimanteDao.findById(pk);
      assertNull(ai);
   }

   /**
    * Test l'appel de la méthode findByExcludedPK().
    */
   public void testFindByExcludedPK(){
      final Imprimante i1 = imprimanteDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      AffectationImprimantePK pk = new AffectationImprimantePK(u1, b1, i1);

      List<AffectationImprimante> liste = affectationImprimanteDao.findByExcludedPK(pk);
      assertTrue(liste.size() == 3);

      pk = new AffectationImprimantePK(u1, b3, i1);
      liste = affectationImprimanteDao.findByExcludedPK(pk);
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByBanqueUtilisateur().
    */
   public void testFindByUtilisateurBanque(){
      final Banque b1 = banqueDao.findById(1);
      final Banque b3 = banqueDao.findById(3);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u4 = utilisateurDao.findById(4);

      List<AffectationImprimante> liste = affectationImprimanteDao.findByBanqueUtilisateur(b1, u1);
      assertTrue(liste.size() == 1);

      liste = affectationImprimanteDao.findByBanqueUtilisateur(b3, u1);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteDao.findByBanqueUtilisateur(b1, u4);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteDao.findByBanqueUtilisateur(b1, null);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteDao.findByBanqueUtilisateur(null, u1);
      assertTrue(liste.size() == 0);

      liste = affectationImprimanteDao.findByBanqueUtilisateur(null, null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un ProfilUtilisateur.
    **/
   @Rollback(false)
   public void testCrud(){

      final AffectationImprimante ai = new AffectationImprimante();
      final Imprimante i1 = imprimanteDao.findById(1);
      final Modele m1 = modeleDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Banque b3 = banqueDao.findById(3);

      ai.setUtilisateur(u1);
      ai.setImprimante(i1);
      ai.setBanque(b3);
      ai.setModele(null);

      // Test de l'insertion
      affectationImprimanteDao.createObject(ai);
      assertTrue(affectationImprimanteDao.findAll().size() == 5);

      // Test de la mise à jour
      final AffectationImprimantePK pk = new AffectationImprimantePK();
      pk.setUtilisateur(u1);
      pk.setImprimante(i1);
      pk.setBanque(b3);
      final AffectationImprimante aiUp = affectationImprimanteDao.findById(pk);
      assertNotNull(aiUp);
      assertNotNull(aiUp.getImprimante());
      assertNotNull(aiUp.getBanque());
      assertNotNull(aiUp.getUtilisateur());
      assertNull(aiUp.getModele());
      aiUp.setModele(m1);
      affectationImprimanteDao.updateObject(aiUp);
      final AffectationImprimante aiTest = affectationImprimanteDao.findById(pk);
      assertNotNull(aiTest);
      assertNotNull(aiTest.getImprimante());
      assertNotNull(aiTest.getBanque());
      assertNotNull(aiTest.getUtilisateur());
      assertNotNull(aiTest.getModele());

      // Test de la délétion
      affectationImprimanteDao.removeObject(pk);
      assertNull(affectationImprimanteDao.findById(pk));
   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{

      final AffectationImprimante ai1 = new AffectationImprimante();
      final AffectationImprimante ai2 = new AffectationImprimante();

      // L'objet 1 n'est pas égal à null
      assertFalse(ai1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ai1.equals(ai1));
      // 2 objets sont égaux entre eux
      assertTrue(ai1.equals(ai2));
      assertTrue(ai2.equals(ai1));

      populateClefsToTestEqualsAndHashCode(ai1, ai2);

      //dummy test
      final Banque b = new Banque();
      assertFalse(ai1.equals(b));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    * @throws ParseException 
    */
   public void testHashCode() throws ParseException{
      final AffectationImprimante ai1 = new AffectationImprimante();
      final AffectationImprimante ai2 = new AffectationImprimante();
      final AffectationImprimante ai3 = new AffectationImprimante();

      assertTrue(ai1.hashCode() == ai2.hashCode());
      assertTrue(ai2.hashCode() == ai3.hashCode());
      assertTrue(ai3.hashCode() > 0);

      //teste dans methode precedente
      populateClefsToTestEqualsAndHashCode(ai1, ai2);

      final int hash = ai1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ai1.hashCode() == ai2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ai1.hashCode());
      assertTrue(hash == ai1.hashCode());
      assertTrue(hash == ai1.hashCode());
      assertTrue(hash == ai1.hashCode());
   }

   private void populateClefsToTestEqualsAndHashCode(final AffectationImprimante ai1, final AffectationImprimante ai2)
      throws ParseException{

      final Imprimante i1 = imprimanteDao.findById(1);
      final Banque b = banqueDao.findById(3);
      final Utilisateur u3 = utilisateurDao.findById(3);
      final Utilisateur u4 = utilisateurDao.findById(4);
      final AffectationImprimantePK pk1 = new AffectationImprimantePK(u3, b, i1);
      final AffectationImprimantePK pk2 = new AffectationImprimantePK(u4, b, i1);
      final AffectationImprimantePK pk3 = new AffectationImprimantePK(u3, b, i1);
      final AffectationImprimantePK[] pks = new AffectationImprimantePK[] {null, pk1, pk2, pk3};

      for(int i = 0; i < pks.length; i++){
         for(int k = 0; k < pks.length; k++){

            ai1.setPk(pks[i]);
            ai2.setPk(pks[k]);

            if(((i == k) || (i + k == 4))){
               assertTrue(ai1.equals(ai2));
               assertTrue(ai1.hashCode() == ai2.hashCode());
            }else{
               assertFalse(ai1.equals(ai2));
            }
         }
      }
   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Imprimante i1 = imprimanteDao.findById(1);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final AffectationImprimantePK pk1 = new AffectationImprimantePK(u1, b, i1);
      final AffectationImprimante ai1 = affectationImprimanteDao.findById(pk1);

      assertTrue(ai1.toString().equals("{" + ai1.getPk().toString() + "}"));

      final AffectationImprimante po2 = new AffectationImprimante();
      po2.setPk(null);
      assertTrue(po2.toString().equals("{Empty AffectationImprimante}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Imprimante i1 = imprimanteDao.findById(1);
      final Banque b = banqueDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final AffectationImprimantePK pk1 = new AffectationImprimantePK(u1, b, i1);
      final AffectationImprimante ai1 = affectationImprimanteDao.findById(pk1);

      AffectationImprimante ai2 = new AffectationImprimante();
      ai2 = ai1.clone();

      assertTrue(ai1.equals(ai2));

      if(ai1.getPk() != null){
         assertTrue(ai1.getPk().equals(ai2.getPk()));
      }else{
         assertNull(ai2.getPk());
      }

   }
}
