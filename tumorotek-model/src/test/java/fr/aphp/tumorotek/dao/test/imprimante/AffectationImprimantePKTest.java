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

import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimantePK;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Classe de test pour le bean du domaine AffectationImprimantePK.
 *
 * @author Pierre Ventadour.
 * @version 21/03/2011.
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class AffectationImprimantePKTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private UtilisateurDao utilisateurDao;
   /** Bean Dao. */
   private BanqueDao banqueDao;
   /** Bean Dao. */
   private ImprimanteDao imprimanteDao;

   public AffectationImprimantePKTest(){

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

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   public void testEquals() throws ParseException{
      final AffectationImprimantePK pk1 = new AffectationImprimantePK();
      final AffectationImprimantePK pk2 = new AffectationImprimantePK();

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
      final AffectationImprimantePK pk1 = new AffectationImprimantePK();
      final AffectationImprimantePK pk2 = new AffectationImprimantePK();

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

      final Imprimante i1 = imprimanteDao.findById(1);
      final Imprimante i2 = imprimanteDao.findById(2);
      final Imprimante[] imprimantesId = new Imprimante[] {null, i1, i2};
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Utilisateur u2 = utilisateurDao.findById(2);
      final Utilisateur u3 = utilisateurDao.findById(1);
      final Utilisateur[] utilisateurs = new Utilisateur[] {null, u1, u2, u3};
      final Banque b1 = banqueDao.findById(1);
      final Banque b2 = banqueDao.findById(2);
      final Banque b3 = banqueDao.findById(1);
      final Banque[] banques = new Banque[] {null, b1, b2, b3};

      final AffectationImprimantePK pk1 = new AffectationImprimantePK();
      final AffectationImprimantePK pk2 = new AffectationImprimantePK();

      for(int i = 0; i < imprimantesId.length; i++){
         for(int j = 0; j < utilisateurs.length; j++){
            for(int j2 = 0; j2 < banques.length; j2++){
               for(int k = 0; k < imprimantesId.length; k++){
                  for(int l = 0; l < utilisateurs.length; l++){
                     for(int l2 = 0; l2 < banques.length; l2++){

                        pk1.setImprimante(imprimantesId[i]);
                        pk1.setUtilisateur(utilisateurs[j]);
                        pk1.setBanque(banques[j2]);

                        pk2.setImprimante(imprimantesId[k]);
                        pk2.setUtilisateur(utilisateurs[l]);
                        pk2.setBanque(banques[l2]);

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
      final Imprimante i1 = imprimanteDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final Banque b1 = banqueDao.findById(1);
      final AffectationImprimantePK pk1 = new AffectationImprimantePK();
      pk1.setImprimante(i1);
      pk1.setUtilisateur(u1);
      pk1.setBanque(b1);

      assertTrue(pk1.toString().equals("{" + pk1.getUtilisateur().toString() + " (Utilisateur), " + pk1.getBanque().toString()
         + " (Banque), " + pk1.getImprimante().toString() + " (Imprimante)}"));

      pk1.setImprimante(null);
      assertTrue(pk1.toString().equals("{Empty AffectationImprimantePK}"));
      pk1.setImprimante(i1);

      pk1.setUtilisateur(null);
      assertTrue(pk1.toString().equals("{Empty AffectationImprimantePK}"));
      pk1.setUtilisateur(u1);

      pk1.setBanque(null);
      assertTrue(pk1.toString().equals("{Empty AffectationImprimantePK}"));
      pk1.setBanque(b1);

      final AffectationImprimantePK pk2 = new AffectationImprimantePK();
      assertTrue(pk2.toString().equals("{Empty AffectationImprimantePK}"));
   }

}
