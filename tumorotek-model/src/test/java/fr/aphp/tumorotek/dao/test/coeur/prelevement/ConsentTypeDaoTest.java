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

import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Classe de test pour le DAO ConsentTypeDao et le
 * bean du domaine ConsentType.
 * Classe de test créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class ConsentTypeDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ConsentTypeDao consentTypeDao;
   private PlateformeDao plateformeDao;

   /** Valeur du type pour la maj. */
   private final String updatedType = "Type mis a jour";

   /**
    * Constructeur.
    */
   public ConsentTypeDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param ctDao est le bean Dao.
    */
   public void setConsentTypeDao(final ConsentTypeDao ctDao){
      this.consentTypeDao = ctDao;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      ConsentType ct1 = consentTypeDao.findById(1);
      assertTrue(ct1.toString().equals("{" + ct1.getType() + "}"));
      ct1 = new ConsentType();
      assertTrue(ct1.toString().equals("{Empty ConsentType}"));
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllConsentType(){
      final List<ConsentType> types = consentTypeDao.findAll();
      assertTrue(types.size() == 3);
   }

   public void testFindByOrder(){
      Plateforme pf = plateformeDao.findById(1);
      List<? extends TKThesaurusObject> list = consentTypeDao.findByOrder(pf);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).getNom().equals("DECEDE"));
      pf = plateformeDao.findById(2);
      list = consentTypeDao.findByOrder(pf);
      assertTrue(list.size() == 1);
      list = consentTypeDao.findByOrder(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByType().
    */
   public void testFindByType(){
      List<ConsentType> types = consentTypeDao.findByType("EN ATTENTE");
      assertTrue(types.size() == 1);
      types = consentTypeDao.findByType("INCONNU");
      assertTrue(types.size() == 0);
      types = consentTypeDao.findByType("EN%");
      assertTrue(types.size() == 1);
      types = consentTypeDao.findByType(null);
      assertTrue(types.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un type de consentement.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudConsentType() throws Exception{
      final ConsentType ct = new ConsentType();
      ct.setType("INCONNU");
      ct.setPlateforme(plateformeDao.findById(1));
      // Test de l'insertion
      consentTypeDao.createObject(ct);
      assertEquals(new Integer(4), ct.getConsentTypeId());

      // Test de la mise à jour
      final ConsentType ct2 = consentTypeDao.findById(new Integer(4));
      assertNotNull(ct2);
      assertTrue(ct2.getType().equals("INCONNU"));
      ct2.setType(updatedType);
      consentTypeDao.updateObject(ct2);
      assertTrue(consentTypeDao.findById(new Integer(4)).getType().equals(updatedType));

      // Test de la délétion
      consentTypeDao.removeObject(new Integer(4));
      assertNull(consentTypeDao.findById(new Integer(4)));
   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String type = "Type";
      final String type2 = "Type2";
      final ConsentType ct1 = new ConsentType();
      ct1.setType(type);
      final ConsentType ct2 = new ConsentType();
      ct2.setType(type);

      // L'objet 1 n'est pas égal à null
      assertFalse(ct1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(ct1.equals(ct1));
      // 2 objets sont égaux entre eux
      assertTrue(ct1.equals(ct2));
      assertTrue(ct2.equals(ct1));

      // Vérification de la différenciation de 2 objets
      ct2.setType(type2);
      assertFalse(ct1.equals(ct2));
      assertFalse(ct2.equals(ct1));

      //passe la clef naturelle type a nulle pour un des objets
      ct2.setType(null);
      assertFalse(ct1.equals(ct2));
      assertFalse(ct2.equals(ct1));

      //passe la clef naturelle type a nulle pour l'autre objet
      ct1.setType(null);
      assertTrue(ct1.equals(ct2));
      ct2.setType(type);
      assertFalse(ct1.equals(ct2));

      //plateforme
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      ct1.setType(ct2.getType());
      ct1.setPlateforme(pf1);
      ct2.setPlateforme(pf1);
      assertTrue(ct1.equals(ct2));
      ct2.setPlateforme(pf2);
      assertFalse(ct1.equals(ct2));

      //dummy test
      final Banque b = new Banque();
      assertFalse(ct1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String type = "Type";
      final ConsentType ct1 = new ConsentType();
      ct1.setConsentTypeId(1);
      ct1.setType(type);
      final ConsentType ct2 = new ConsentType();
      ct2.setConsentTypeId(2);
      ct2.setType(type);
      final ConsentType ct3 = new ConsentType();
      ct3.setConsentTypeId(3);
      ct3.setType(null);
      assertTrue(ct3.hashCode() > 0);

      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      ct1.setPlateforme(pf1);
      ct2.setPlateforme(pf1);
      ct3.setPlateforme(pf2);

      final int hash = ct1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(ct1.hashCode() == ct2.hashCode());
      assertFalse(ct1.hashCode() == ct3.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());
      assertTrue(hash == ct1.hashCode());

   }

}
