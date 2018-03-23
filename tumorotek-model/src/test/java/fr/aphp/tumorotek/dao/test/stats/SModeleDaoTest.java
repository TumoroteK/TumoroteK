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
package fr.aphp.tumorotek.dao.test.stats;

import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.stats.IndicateurDao;
import fr.aphp.tumorotek.dao.stats.SModeleDao;
import fr.aphp.tumorotek.dao.stats.SModeleIndicateurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.SModeleIndicateur;
import fr.aphp.tumorotek.model.stats.SModeleIndicateurPK;

@TransactionConfiguration(defaultRollback = false)
public class SModeleDaoTest extends AbstractDaoTest
{

   private SModeleDao sModeleDao;
   private PlateformeDao plateformeDao;
   private IndicateurDao indicateurDao;
   private SModeleIndicateurDao sModeleIndicateurDao;

   public SModeleDaoTest(){

   }

   public void setSModeleDao(final SModeleDao mDao){
      this.sModeleDao = mDao;
   }

   public void setPlateformeDao(final PlateformeDao plateformeDao){
      this.plateformeDao = plateformeDao;
   }

   public void setIndicateurDao(final IndicateurDao i){
      this.indicateurDao = i;
   }

   public void setsModeleIndicateurDao(final SModeleIndicateurDao s){
      this.sModeleIndicateurDao = s;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAll(){
      final List<SModele> liste = sModeleDao.findAll();
      assertTrue(liste.size() == 3);
   }

   public void testFindById(){
      SModele sm = sModeleDao.findById(1);
      assertNotNull(sm);
      assertTrue(sm.getNom().equals("MOD1"));
      sm = sModeleDao.findById(null);
      assertNull(sm);
   }

   public void testFindByPlateforme(){
      final Plateforme pf1 = plateformeDao.findById(1);
      List<SModele> sms = sModeleDao.findByPlateforme(pf1);
      assertTrue(sms.size() == 2);
      assertTrue(sms.contains(sModeleDao.findById(1)));
      assertTrue(sms.contains(sModeleDao.findById(2)));

      final Plateforme pf2 = plateformeDao.findById(2);
      sms = sModeleDao.findByPlateforme(pf2);
      assertTrue(sms.size() == 1);
      assertTrue(sms.contains(sModeleDao.findById(3)));

      sms = sModeleDao.findByPlateforme(null);
      assertTrue(sms.isEmpty());
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un modèle.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{
      final Plateforme pf = plateformeDao.findById(1);
      final SModele m1 = new SModele();
      m1.setNom("TEST");
      m1.setPlateforme(pf);
      m1.setDescription("Une courte description");

      final SModeleIndicateur smi1 = new SModeleIndicateur();
      final SModeleIndicateurPK smi1PK = new SModeleIndicateurPK();
      smi1PK.setsModele(m1);
      smi1PK.setIndicateur(indicateurDao.findById(2));
      smi1.setPk(smi1PK);
      smi1.setOrdre(1);

      final SModeleIndicateur smi2 = new SModeleIndicateur();
      final SModeleIndicateurPK smi2PK = new SModeleIndicateurPK();
      smi2PK.setsModele(m1);
      smi2PK.setIndicateur(indicateurDao.findById(1));
      smi2.setPk(smi2PK);
      smi2.setOrdre(2);

      m1.getSModeleIndicateurs().add(smi1);
      m1.getSModeleIndicateurs().add(smi2);

      // Test de l'insertion
      sModeleDao.createObject(m1);
      assertNotNull(m1.getSmodeleId());

      final Integer sModId = m1.getSmodeleId();

      final SModele m2 = sModeleDao.findById(sModId);
      // Vérification des données entrées dans la base
      assertNotNull(m2);
      assertTrue(m2.getPlateforme().equals(pf));
      assertTrue(m2.getNom().equals("TEST"));
      assertTrue(m2.getDescription().equals("Une courte description"));
      assertTrue(m2.getSModeleIndicateurs().size() == 2);

      List<Indicateur> indics = indicateurDao.findBySModele(m2);
      assertTrue(indics.size() == 2);
      assertTrue(indics.get(1).equals(indicateurDao.findById(1)));
      assertTrue(indics.get(0).equals(indicateurDao.findById(2)));

      // Test de la mise à jour
      m2.getSModeleIndicateurs().remove(smi1);
      sModeleIndicateurDao.removeObject(smi1PK);

      m2.setNom("UP");
      m2.setDescription("auttro");

      sModeleDao.updateObject(m2);

      final SModele m3 = sModeleDao.findById(sModId);
      // Vérification des données entrées dans la base
      assertNotNull(m3);
      assertTrue(m3.getPlateforme().equals(pf));
      assertTrue(m3.getNom().equals("UP"));
      assertTrue(m3.getDescription().equals("auttro"));
      assertTrue(m3.getSModeleIndicateurs().size() == 1);

      indics = indicateurDao.findBySModele(m3);
      assertTrue(indics.size() == 1);
      assertTrue(indics.get(0).equals(indicateurDao.findById(1)));

      // Test de la délétion
      sModeleDao.removeObject(sModId);
      assertNull(sModeleDao.findById(sModId));

      testReadAll();
   }

   /** 
    * Test de la methode surchargee equals
    */
   public void testEquals(){

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){}

   /**
    * Test la méthode toString.
    */
   public void testToString(){

   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){

   }

}
