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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TransactionConfiguration;

import fr.aphp.tumorotek.dao.cession.ContratDao;
import fr.aphp.tumorotek.dao.cession.ProtocoleTypeDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.EtablissementDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.Contrat;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 *
 * Classe de test pour le DAO ContratDao et le bean
 * du domaine Contrat.
 *
 * @author Pierre Ventadour.
 * @version 25/01/2010
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class ContratDaoTest extends AbstractDaoTest
{

   /** Bean Dao. */
   private ContratDao contratDao;
   /** Bean Dao. */
   private CollaborateurDao collaborateurDao;
   /** Bean Dao. */
   private ServiceDao serviceDao;
   /** Bean Dao. */
   private ProtocoleTypeDao protocoleTypeDao;
   /** Bean Dao. */
   private EtablissementDao etablissementDao;
   /** Bean Dao. */
   private PlateformeDao plateformeDao;
   /** valeur du nom pour la maj. */
   private final String updatedNumero = "Mis a jour";

   /** Constructeur. */
   public ContratDaoTest(){

   }

   public void setContratDao(final ContratDao cDao){
      this.contratDao = cDao;
   }

   public void setCollaborateurDao(final CollaborateurDao dao){
      this.collaborateurDao = dao;
   }

   public void setServiceDao(final ServiceDao dao){
      this.serviceDao = dao;
   }

   public void setProtocoleTypeDao(final ProtocoleTypeDao dao){
      this.protocoleTypeDao = dao;
   }

   public void setEtablissementDao(final EtablissementDao eDao){
      this.etablissementDao = eDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   public void testReadAllContrats(){
      final List<Contrat> liste = contratDao.findAll();
      assertTrue(liste.size() == 4);
   }

   /**
    * Test l'appel de la méthode findByNumero().
    */
   public void testFindByNumero(){
      List<Contrat> liste = contratDao.findByNumero("CONTRAT 78551269");
      assertTrue(liste.size() == 1);

      liste = contratDao.findByNumero("CONTRAT");
      assertTrue(liste.size() == 0);

      liste = contratDao.findByNumero("CONTRAT%");
      assertTrue(liste.size() == 3);

      liste = contratDao.findByNumero(null);
      assertTrue(liste.size() == 0);

   }

   /**
    * Test l'appel de la méthode findByOrder().
    */
   public void testFindByOrder(){
      final List<Contrat> liste = contratDao.findByOrder();
      assertTrue(liste.size() == 4);
      assertTrue(liste.get(0).getContratId().equals(3));
   }

   /**
    * Test l'appel de la méthode findByBanqueIdWithOrder().
    */
   public void testFindByBanqueIdWithOrder(){
      final Plateforme pf1 = plateformeDao.findById(1);
      List<Contrat> liste = contratDao.findByPlateforme(pf1);
      assertTrue(liste.size() == 3);

      final Plateforme pf2 = plateformeDao.findById(2);
      liste = contratDao.findByPlateforme(pf2);
      assertTrue(liste.size() == 1);

      liste = contratDao.findByPlateforme(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   public void testFindByExcludedId(){
      List<Contrat> liste = contratDao.findByExcludedId(1);
      assertTrue(liste.size() == 3);

      liste = contratDao.findByExcludedId(10);
      assertTrue(liste.size() == 4);

      liste = contratDao.findByExcludedId(null);
      assertTrue(liste.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un Contrat.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   public void testCrud() throws Exception{

      final Contrat contrat = new Contrat();
      final Collaborateur collab = collaborateurDao.findById(1);
      final Service serv = serviceDao.findById(1);
      final Etablissement etab = etablissementDao.findById(2);
      final ProtocoleType type = protocoleTypeDao.findById(1);
      final Plateforme pf = plateformeDao.findById(1);

      final Date demande = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2009");
      final Date validation = new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2009");
      final Date demandeRedac = new SimpleDateFormat("dd/MM/yyyy").parse("05/09/2009");
      final Date envoi = new SimpleDateFormat("dd/MM/yyyy").parse("10/09/2009");
      final Date signature = new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2009");
      final Date upSignature = new SimpleDateFormat("dd/MM/yyyy").parse("16/09/2009");
      final Float montant = new Float(666);
      contrat.setNumero("TEST");
      contrat.setDateDemandeCession(demande);
      contrat.setDateValidation(validation);
      contrat.setDateDemandeRedaction(demandeRedac);
      contrat.setDateEnvoiContrat(envoi);
      contrat.setDateSignature(signature);
      contrat.setTitreProjet("Titre");
      contrat.setDescription("DESC");
      contrat.setCollaborateur(collab);
      contrat.setService(serv);
      contrat.setProtocoleType(type);
      contrat.setPlateforme(pf);
      contrat.setEtablissement(etab);
      contrat.setMontant(montant);
      // Test de l'insertion
      contratDao.createObject(contrat);
      assertEquals(new Integer(5), contrat.getContratId());

      // Test de la mise à jour
      final Contrat contrat2 = contratDao.findById(new Integer(5));
      // Vérification des données entrées dans la base
      assertNotNull(contrat2);
      assertNotNull(contrat2.getCollaborateur());
      assertNotNull(contrat2.getService());
      assertNotNull(contrat2.getEtablissement());
      assertNotNull(contrat2.getProtocoleType());
      assertNotNull(contrat2.getPlateforme());
      assertTrue(contrat2.getNumero().equals("TEST"));
      assertTrue(contrat2.getDateDemandeCession().equals(demande));
      assertTrue(contrat2.getDateValidation().equals(validation));
      assertTrue(contrat2.getDateDemandeRedaction().equals(demandeRedac));
      assertTrue(contrat2.getDateEnvoiContrat().equals(envoi));
      assertTrue(contrat2.getDateSignature().equals(signature));
      assertTrue(contrat2.getTitreProjet().equals("Titre"));
      assertTrue(contrat2.getDescription().equals("DESC"));
      assertTrue(contrat2.getMontant().equals(montant));
      contrat2.setNumero(updatedNumero);
      contrat2.setDateSignature(upSignature);
      contratDao.updateObject(contrat2);
      assertTrue(contratDao.findById(new Integer(5)).getNumero().equals(updatedNumero));
      assertTrue(contratDao.findById(new Integer(5)).getDateSignature().equals(upSignature));
      // Test de la délétion
      contratDao.removeObject(new Integer(5));
      assertNull(contratDao.findById(new Integer(5)));

   }

   /**
    * Test de la méthode surchargée "equals".
    */
   public void testEquals(){
      final String num = "NUM";
      final String num2 = "NUM2";
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      final Contrat b1 = new Contrat();
      final Contrat b2 = new Contrat();

      // L'objet 1 n'est pas égal à null
      assertFalse(b1.equals(null));
      // L'objet 1 est égale à lui même
      assertTrue(b1.equals(b1));

      /*null*/
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      /*Nom*/
      b2.setNumero(num);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setNumero(num2);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setNumero(num);
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      /*Plateforme (nom etant egaux)*/
      b2.setPlateforme(pf1);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setPlateforme(pf2);
      assertFalse(b1.equals(b2));
      assertFalse(b2.equals(b1));
      b1.setPlateforme(pf1);
      assertTrue(b1.equals(b2));
      assertTrue(b2.equals(b1));

      final Categorie c = new Categorie();
      assertFalse(b1.equals(c));
   }

   /**
    * Test de la méthode surchargée "hashcode".
    */
   public void testHashCode(){
      final String num = "NUM";
      final String num2 = "NUM2";
      final Plateforme pf1 = plateformeDao.findById(1);
      final Plateforme pf2 = plateformeDao.findById(2);
      final Contrat b1 = new Contrat();
      final Contrat b2 = new Contrat();

      assertTrue(b1.hashCode() > 0);

      /*null*/
      assertTrue(b1.hashCode() == b2.hashCode());

      /*Nom*/
      b2.setNumero(num);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setNumero(num2);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setNumero(num);
      assertTrue(b1.hashCode() == b2.hashCode());

      /*Plateforme (nom etant egaux)*/
      b2.setPlateforme(pf1);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setPlateforme(pf2);
      assertFalse(b1.hashCode() == b2.hashCode());
      b1.setPlateforme(pf1);
      assertTrue(b1.hashCode() == b2.hashCode());

      // un même objet garde le même hashcode dans le temps
      final int hash = b1.hashCode();
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());
      assertTrue(hash == b1.hashCode());

   }

   /**
    * Test la méthode toString.
    */
   public void testToString(){
      final Contrat contrat1 = contratDao.findById(1);
      assertTrue(
         contrat1.toString().equals("{" + contrat1.getNumero() + ", " + contrat1.getPlateforme().getNom() + "(Plateforme)}"));

      final Contrat contrat2 = new Contrat();
      assertTrue(contrat2.toString().equals("{Empty Contrat}"));
   }

   /**
    * Test la méthode clone.
    */
   public void testClone(){
      final Contrat contrat1 = contratDao.findById(1);
      Contrat contrat2 = new Contrat();
      contrat2 = contrat1.clone();

      assertTrue(contrat1.equals(contrat2));

      if(contrat1.getContratId() != null){
         assertTrue(contrat1.getContratId() == contrat2.getContratId());
      }else{
         assertNull(contrat2.getContratId());
      }

      if(contrat1.getNumero() != null){
         assertTrue(contrat1.getNumero().equals(contrat2.getNumero()));
      }else{
         assertNull(contrat2.getNumero());
      }

      if(contrat1.getDateDemandeCession() != null){
         assertTrue(contrat1.getDateDemandeCession().equals(contrat2.getDateDemandeCession()));
      }else{
         assertNull(contrat2.getDateDemandeCession());
      }

      if(contrat1.getDateValidation() != null){
         assertTrue(contrat1.getDateValidation().equals(contrat2.getDateValidation()));
      }else{
         assertNull(contrat2.getDateValidation());
      }

      if(contrat1.getDateSignature() != null){
         assertTrue(contrat1.getDateSignature().equals(contrat2.getDateSignature()));
      }else{
         assertNull(contrat2.getDateSignature());
      }

      if(contrat1.getDateDemandeRedaction() != null){
         assertTrue(contrat1.getDateDemandeRedaction().equals(contrat2.getDateDemandeRedaction()));
      }else{
         assertNull(contrat2.getDateDemandeRedaction());
      }

      if(contrat1.getDateEnvoiContrat() != null){
         assertTrue(contrat1.getDateEnvoiContrat().equals(contrat2.getDateEnvoiContrat()));
      }else{
         assertNull(contrat2.getDateEnvoiContrat());
      }

      if(contrat1.getTitreProjet() != null){
         assertTrue(contrat1.getTitreProjet().equals(contrat2.getTitreProjet()));
      }else{
         assertNull(contrat2.getTitreProjet());
      }

      if(contrat1.getDescription() != null){
         assertTrue(contrat1.getDescription().equals(contrat2.getDescription()));
      }else{
         assertNull(contrat2.getDescription());
      }

      if(contrat1.getCollaborateur() != null){
         assertTrue(contrat1.getCollaborateur().equals(contrat2.getCollaborateur()));
      }else{
         assertNull(contrat2.getCollaborateur());
      }

      if(contrat1.getService() != null){
         assertTrue(contrat1.getService().equals(contrat2.getService()));
      }else{
         assertNull(contrat2.getService());
      }

      if(contrat1.getProtocoleType() != null){
         assertTrue(contrat1.getProtocoleType().equals(contrat2.getProtocoleType()));
      }else{
         assertNull(contrat2.getProtocoleType());
      }

      if(contrat1.getEtablissement() != null){
         assertTrue(contrat1.getEtablissement().equals(contrat2.getEtablissement()));
      }else{
         assertNull(contrat2.getEtablissement());
      }

      if(contrat1.getMontant() != null){
         assertTrue(contrat1.getMontant().equals(contrat2.getMontant()));
      }else{
         assertNull(contrat2.getMontant());
      }

      if(contrat1.getPlateforme() != null){
         assertTrue(contrat1.getPlateforme().equals(contrat2.getPlateforme()));
      }else{
         assertNull(contrat2.getPlateforme());
      }

   }

}
