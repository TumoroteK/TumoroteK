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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInterComparator;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;

/**
 *
 * Classe de test pour le DAO LaboInterDao et le
 * bean du domaine LaboInter.
 * Classe de test créée le 01/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class LaboInterDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private LaboInterDao laboInterDao;
   private PrelevementDao prelevementDao;
   private TransporteurDao transporteurDao;
   private CollaborateurDao collaborateurDao;
   private ServiceDao serviceDao;

   /** Valeurs des proprietes du labo pour la maj. */
   private final Integer ordreUpdated = 2;
   private final Boolean sterileUpdated = false;
   private final Float conserTempUpdated = new Float(25.0);
   private final Float transportTempUpdated = new Float(25.0);

   /**
    * Constructeur.
    */
   public LaboInterDaoTest(){}

   /**
    * Setter du bean Dao.
    * @param lDao est le bean Dao.
    */
   public void setLaboInterDao(final LaboInterDao lDao){
      this.laboInterDao = lDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setTransporteurDao(final TransporteurDao tDao){
      this.transporteurDao = tDao;
   }

   public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   public void setServiceDao(final ServiceDao sDao){
      this.serviceDao = sDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllLabos(){
      final List<LaboInter> labos = laboInterDao.findAll();
      assertTrue(labos.size() == 3);

      // sort list
      Collections.sort(labos, new LaboInterComparator());
      assertTrue(labos.get(0).getOrdre() == 1);
      assertTrue(labos.get(1).getOrdre() == 2);
      assertTrue(labos.get(2).getOrdre() == 3);
   }

   /**
    * Test l'appel de la méthode toString().
    */
   public void testToString(){
      LaboInter l1 = laboInterDao.findById(1);
      assertTrue(l1.toString().equals("{" + l1.getPrelevement().getCode() + ", " + l1.getOrdre().toString() + "}"));
      l1 = new LaboInter();
      assertTrue(l1.toString().equals("{Empty LaboInter}"));
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<LaboInter> labos = laboInterDao.findByExcludedId(1);
      assertTrue(labos.size() == 2);

      labos = laboInterDao.findByExcludedId(5);
      assertTrue(labos.size() == 3);
   }

   /**
    * Test l'appel de la méthode findByTransporteur().
    */
   public void testFindByTransporteur(){
      Transporteur t = transporteurDao.findById(1);
      List<LaboInter> labos = laboInterDao.findByTransporteur(t);
      assertTrue(labos.size() == 2);
      t = transporteurDao.findById(2);
      labos = laboInterDao.findByTransporteur(t);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByCollaborateur().
    */
   public void testFindByCollaborateur(){
      Collaborateur c = collaborateurDao.findById(2);
      List<LaboInter> labos = laboInterDao.findByCollaborateur(c);
      assertTrue(labos.size() == 3);
      c = collaborateurDao.findById(1);
      labos = laboInterDao.findByCollaborateur(c);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByService().
    */
   public void testFindByService(){
      Service s = serviceDao.findById(2);
      List<LaboInter> labos = laboInterDao.findByService(s);
      assertTrue(labos.size() == 1);
      s = serviceDao.findById(4);
      labos = laboInterDao.findByService(s);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevementWithOrder().
    */
   public void testFindByPrelevementWithOrder(){
      Prelevement p = prelevementDao.findById(1);
      List<LaboInter> labos = laboInterDao.findByPrelevementWithOrder(p);
      assertTrue(labos.size() == 3);
      assertTrue(labos.get(0).getLaboInterId() == 1);

      p = prelevementDao.findById(2);
      labos = laboInterDao.findByPrelevementWithOrder(p);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevementWithOnlyOrder().
    */
   public void testFindByPrelevementWithOnlyOrder(){
      Prelevement p = prelevementDao.findById(1);
      List<Integer> labos = laboInterDao.findByPrelevementWithOnlyOrder(p);
      assertTrue(labos.size() == 3);
      assertTrue(labos.get(0) == 1);

      p = prelevementDao.findById(2);
      labos = laboInterDao.findByPrelevementWithOnlyOrder(p);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByPrelevementWithOnlyOrderAndExcludedId().
    */
   public void testFindByPrelevementWithOnlyOrderAndExcludedId(){
      Prelevement p = prelevementDao.findById(1);
      List<Integer> labos = laboInterDao.findByPrelevementWithOnlyOrderAndExcludedId(p, 1);
      assertTrue(labos.size() == 2);
      assertTrue(labos.get(0) == 2);

      p = prelevementDao.findById(2);
      labos = laboInterDao.findByPrelevementWithOnlyOrderAndExcludedId(p, 1);
      assertTrue(labos.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression 
    * d'un laboratoire intermediaire.
    * @throws Exception lance une exception en cas de problème lors du CRUD.
    */
   @Rollback(false)
   public void testCrudLaboInter() throws Exception{
      final LaboInter l = new LaboInter();
      final Prelevement p = prelevementDao.findById(2);
      l.setPrelevement(p);
      l.setOrdre(1);
      final Service s = serviceDao.findById(3);
      l.setService(s);
      final Calendar arrDate = Calendar.getInstance();
      arrDate.setTime(new Date(4000));
      l.setDateArrivee(arrDate);
      l.setConservTemp(null);
      l.setSterile(true);
      l.setCongelation(false);
      l.setTransportTemp(new Float(0.0));
      final Calendar depDate = Calendar.getInstance();
      depDate.setTime(new Date(4200));
      l.setDateDepart(depDate);
      final Collaborateur c = collaborateurDao.findById(1);
      l.setCollaborateur(c);
      final Transporteur t = transporteurDao.findById(1);
      l.setTransporteur(t);

      // Test de l'insertion
      laboInterDao.createObject(l);
      assertEquals(new Integer(4), l.getLaboInterId());

      // Test de la mise à jour
      final LaboInter l2 = laboInterDao.findById(new Integer(4));
      assertNotNull(l2);
      assertTrue(l2.getPrelevement().equals(p));
      assertTrue(l2.getOrdre() == 1);
      assertTrue(l2.getService().equals(s));
      assertTrue(l2.getDateArrivee().equals(arrDate));
      assertTrue(l2.getConservTemp() == null);
      assertTrue(l2.getSterile());
      assertFalse(l2.getCongelation());
      assertTrue(l2.getTransportTemp().equals(new Float(0.0)));
      assertTrue(l2.getDateDepart().equals(depDate));
      assertTrue(l2.getCollaborateur().equals(c));
      assertTrue(l2.getTransporteur().equals(t));

      final Calendar dateDepartUpdated = Calendar.getInstance();
      dateDepartUpdated.setTime(new Date(5000));
      final Calendar dateArriveeUpdated = Calendar.getInstance();
      dateArriveeUpdated.setTime(new Date(6000));

      final Prelevement p2 = prelevementDao.findById(1);
      l2.setPrelevement(p2);
      l2.setOrdre(ordreUpdated);
      final Service s2 = serviceDao.findById(4);
      l2.setService(s2);
      l2.setDateArrivee(dateArriveeUpdated);
      l2.setConservTemp(conserTempUpdated);
      l2.setSterile(sterileUpdated);
      l2.setCongelation(true);
      l2.setTransportTemp(transportTempUpdated);
      l2.setDateDepart(dateDepartUpdated);
      final Collaborateur c2 = collaborateurDao.findById(2);
      l2.setCollaborateur(c2);
      final Transporteur t2 = transporteurDao.findById(2);
      l2.setTransporteur(t2);
      laboInterDao.updateObject(l2);
      assertTrue(laboInterDao.findById(new Integer(4)).getPrelevement().equals(p2));
      assertTrue(laboInterDao.findById(new Integer(4)).getOrdre() == ordreUpdated);
      assertTrue(laboInterDao.findById(new Integer(4)).getService().equals(s2));
      assertTrue(laboInterDao.findById(new Integer(4)).getDateArrivee().equals(dateArriveeUpdated));
      assertTrue(laboInterDao.findById(new Integer(4)).getConservTemp().equals(conserTempUpdated));
      assertFalse(laboInterDao.findById(new Integer(4)).getSterile());
      assertTrue(laboInterDao.findById(new Integer(4)).getCongelation());
      assertTrue(laboInterDao.findById(new Integer(4)).getTransportTemp().equals(transportTempUpdated));
      assertTrue(laboInterDao.findById(new Integer(4)).getDateDepart().equals(dateDepartUpdated));
      assertTrue(laboInterDao.findById(new Integer(4)).getCollaborateur().equals(c2));
      assertTrue(laboInterDao.findById(new Integer(4)).getTransporteur().equals(t2));

      // Test de la délétion
      laboInterDao.removeObject(new Integer(4));
      assertNull(laboInterDao.findById(new Integer(4)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final LaboInter l1 = new LaboInter();
      final LaboInter l2 = new LaboInter();

      // L'objet 1 n'est pas égal à null
      assertFalse(l1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(l1.equals(l1));
      // 2 objets sont égaux entre eux
      assertTrue(l1.equals(l2));
      assertTrue(l2.equals(l1));

      final Prelevement p1 = prelevementDao.findById(1);
      final Prelevement p2 = prelevementDao.findById(2);
      final Prelevement p3 = prelevementDao.findById(1);
      assertTrue(p1.equals(p3));
      final Prelevement[] prelevements = new Prelevement[] {null, p1, p2, p3};
      final Integer[] ordres = new Integer[] {null, 1, 2};

      for(int i = 0; i < prelevements.length; i++){
         l1.setPrelevement(prelevements[i]);
         for(int j = 0; j < ordres.length; j++){
            l1.setOrdre(ordres[j]);
            for(int k = 0; k < prelevements.length; k++){
               l2.setPrelevement(prelevements[k]);
               for(int l = 0; l < ordres.length; l++){
                  l2.setOrdre(ordres[l]);
                  if(((i == k) || (i + k == 4)) && (j == l)){
                     assertTrue(l1.equals(l2));
                  }else{
                     assertFalse(l1.equals(l2));
                  }
               }
            }
         }
      }

      //dummy test
      final Banque b = new Banque();
      assertFalse(l1.equals(b));

   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final LaboInter l1 = new LaboInter();
      l1.setLaboInterId(1);
      final LaboInter l2 = new LaboInter();
      l2.setLaboInterId(2);
      final LaboInter t3 = new LaboInter();
      l1.setLaboInterId(3);
      assertTrue(t3.hashCode() > 0);

      final Prelevement p1 = prelevementDao.findById(1);
      final Prelevement p2 = prelevementDao.findById(3);
      final Prelevement[] prelevements = new Prelevement[] {null, p1, p2};
      final Integer[] ordres = new Integer[] {null, 1, 2};

      for(int i = 0; i < prelevements.length; i++){
         l1.setPrelevement(prelevements[i]);
         for(int j = 0; j < ordres.length; j++){
            l1.setOrdre(ordres[j]);
            for(int k = 0; k < prelevements.length; k++){
               l2.setPrelevement(prelevements[k]);
               for(int l = 0; l < ordres.length; l++){
                  l2.setOrdre(ordres[l]);
                  if((i == k) && (j == l)){
                     assertTrue(l1.hashCode() == l2.hashCode());
                  }else{
                     assertFalse(l1.hashCode() == l2.hashCode());
                  }
               }
            }
         }
      }

      final int hash = l1.hashCode();
      // 2 objets égaux ont le même hashcode
      assertTrue(l1.hashCode() == l2.hashCode());
      // un même objet garde le même hashcode dans le temps
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
      assertTrue(hash == l1.hashCode());
   }

}
