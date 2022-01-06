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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.test.annotation.Rollback;

import fr.aphp.tumorotek.dao.cession.RetourDao;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.test.AbstractDaoTest;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Classe de test pour le DAO RetourDao et le bean
 * du domaine Retour.
 *
 * @author Mathieu BARTHELEMY.
 * @version 25/01/2010
 *
 */
public class RetourDaoTest extends AbstractDaoTest
{

   @Autowired
 RetourDao retourDao;
   @Autowired
 EntiteDao entiteDao;
   @Autowired
 CollaborateurDao collaborateurDao;
   @Autowired
 TransformationDao transformationDao;
   @Autowired
 ObjetStatutDao objetStatutDao;

   /** Constructeur. */
   public RetourDaoTest(){}

   @Test
public void setRetourDao(final RetourDao rDao){
      this.retourDao = rDao;
   }

   @Test
public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Test
public void setCollaborateurDao(final CollaborateurDao cDao){
      this.collaborateurDao = cDao;
   }

   @Test
public void setTransformationDao(final TransformationDao tDao){
      this.transformationDao = tDao;
   }

   @Test
public void setObjetStatutDao(final ObjetStatutDao oDao){
      this.objetStatutDao = oDao;
   }

   /**
    * Test l'appel de la méthode findAll().
    */
   @Test
public void testReadAllRetours(){
      final List<Retour> retours = IterableUtils.toList(retourDao.findAll());
      assertTrue(retours.size() == 8);
   }

   @Test
public void testFindByMaxId(){
      final List<Integer> res = retourDao.findByMaxId();
      assertTrue(res.size() == 1);
      assertTrue(res.get(0) == 8);
   }

   @Test
public void testFindByObject(){
      Entite e = entiteDao.findByNom("Echantillon").get(0);
      List<Retour> retours = retourDao.findByObject(1, e);
      assertTrue(retours.size() == 5);
      e = entiteDao.findByNom("ProdDerive").get(0);
      retours = retourDao.findByObject(3, e);
      assertTrue(retours.size() == 2);
      retours = retourDao.findByObject(1, e);
      assertTrue(retours.size() == 0);
      retours = retourDao.findByObject(null, null);
      assertTrue(retours.size() == 0);
   }

   /**
    * Test l'appel de la méthode findByExcludedId().
    */
   @Test
public void testFindByExcludedId(){
      List<Retour> retours = retourDao.findByExcludedId(1, 1, entiteDao.findById(3));
      assertTrue(retours.size() == 4);
      retours = retourDao.findByExcludedId(9, 1, entiteDao.findById(3));
      assertTrue(retours.size() == 5);
      retours = retourDao.findByExcludedId(null, null, null);
      assertTrue(retours.size() == 0);
   }

   /**
    * Test l'insertion, la mise à jour et la suppression d'un Retour.
    * @throws Exception lance une exception en cas d'erreur.
    */
   @Rollback(false)
   @Test
public void testCrudRetour() throws Exception{

      final String adrl1 = "CC1.R1.T1.BT1.77";
      final String adrl2 = "CC1.R1.T1.BT2.5";
      final Retour retour = new Retour();
      retour.setObjetId(4);
      retour.setEntite(entiteDao.findByNom("Echantillon").get(0));
      final Calendar dateSortie = Calendar.getInstance();
      dateSortie.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:25"));
      retour.setDateSortie(dateSortie);

      retour.setTempMoyenne(new Float(12.3));
      retour.setSterile(false);
      retour.setCollaborateur(collaborateurDao.findById(2));
      retour.setObservations("sortie pour transformation");
      retour.setTransformation(transformationDao.findById(4));
      retour.setOldEmplacementAdrl(adrl1);
      retour.setObjetStatut(objetStatutDao.findById(4));

      // Test de l'insertion
      retourDao.save(retour);
      assertTrue(IterableUtils.toList(retourDao.findAll()).size() == 9);

      final Integer rId = retour.getRetourId();

      // Test de la mise à jour
      final Retour retour2 = retourDao.findById(rId);
      assertNotNull(retour2);
      assertTrue(retour2.getObjetId() == 4);
      assertTrue(retour2.getEntite().equals(entiteDao.findByNom("Echantillon").get(0)));
      assertTrue(
         retour2.getDateSortie().getTime().equals(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/01/2010 12:10:25")));
      assertTrue(retour2.getDateRetour() == null);
      assertTrue(retour2.getTempMoyenne().equals(new Float(12.3)));
      assertFalse(retour2.getSterile());
      assertTrue(retour2.getCollaborateur().equals(collaborateurDao.findById(2)));
      assertTrue(retour2.getObservations().equals("sortie pour transformation"));
      assertTrue(retour2.getTransformation().equals(transformationDao.findById(4)));
      assertNull(retour2.getCession());
      assertNull(retour2.getConteneur());
      assertNull(retour2.getIncident());
      assertTrue(retour2.getOldEmplacementAdrl().equals(adrl1));
      assertTrue(retour2.getObjetStatut().getStatut().equals("NON STOCKE"));

      // update
      //retour.setDateSortie(null);
      final Calendar dateRetour = Calendar.getInstance();
      dateRetour.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("02/01/2010 16:47:10"));
      retour2.setDateRetour(dateRetour);
      retour2.setTempMoyenne(new Float(22.69));
      retour2.setSterile(true);
      retour2.setCollaborateur(null);
      retour2.setObservations("sortie pour traitement");
      retour2.setTransformation(null);
      retour2.setOldEmplacementAdrl(adrl2);
      retour2.setObjetStatut(objetStatutDao.findById(2));

      retourDao.save(retour2);

      assertNotNull(retourDao.findById(rId).getDateSortie());
      assertNotNull(retourDao.findById(rId).getDateRetour());
      assertTrue(retourDao.findById(rId).getTempMoyenne().equals(new Float(22.69)));
      assertTrue(retourDao.findById(rId).getSterile());
      assertNull(retourDao.findById(rId).getCollaborateur());
      assertTrue(retourDao.findById(rId).getObservations().equals("sortie pour traitement"));
      assertNull(retourDao.findById(rId).getTransformation());
      assertTrue(retourDao.findById(rId).getOldEmplacementAdrl().equals(adrl2));
      assertTrue(retour2.getObjetStatut().getStatut().equals("EPUISE"));

      // Test de la délétion
      retourDao.deleteById(rId);
      assertNull(retourDao.findById(rId));
      testReadAllRetours();

   }

   /**
    * Test de la méthode surchargée "equals".
    * @throws ParseException 
    */
   @Test
public void testEqualsAndHashCode() throws ParseException{
      final Retour r1 = new Retour();
      final Retour r2 = new Retour();
      assertFalse(r1.equals(null));
      assertNotNull(r2);
      assertTrue(r1.equals(r1));
      assertTrue(r1.equals(r2));
      assertTrue(r1.hashCode() == r2.hashCode());

      final Integer i1 = new Integer(1);
      final Integer i2 = new Integer(2);
      final Integer i3 = new Integer(2);

      r1.setObjetId(i1);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r2.setObjetId(i2);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r1.setObjetId(i2);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());
      r1.setObjetId(i3);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());

      final Entite e1 = entiteDao.findById(1);
      final Entite e2 = entiteDao.findById(2);
      final Entite e3 = new Entite();
      e3.setNom(e2.getNom());
      assertFalse(e1.equals(e2));
      assertFalse(e1.hashCode() == e2.hashCode());
      assertTrue(e2.equals(e3));
      r1.setEntite(e1);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r2.setEntite(e2);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r1.setEntite(e3);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());
      r1.setEntite(e2);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());

      final Calendar date1 = Calendar.getInstance();
      date1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2010"));
      final Calendar date2 = Calendar.getInstance();
      date2.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("08/01/2010"));
      assertFalse(date1.equals(date2));
      assertFalse(date1.hashCode() == date2.hashCode());
      r1.setDateSortie(date1);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r2.setDateSortie(date2);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r1.setDateSortie(date2);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());

      r1.setDateRetour(date1);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r2.setDateRetour(date2);
      assertFalse(r1.equals(r2));
      assertFalse(r2.equals(r1));
      assertTrue(r1.hashCode() != r2.hashCode());
      r1.setDateRetour(date2);
      assertTrue(r1.equals(r2));
      assertTrue(r2.equals(r1));
      assertTrue(r1.hashCode() == r2.hashCode());

      // dummy
      final Categorie c = new Categorie();
      assertFalse(r1.equals(c));
   }

   /**
    * Test la méthode toString.
    * @throws ParseException 
    */
   @Test
public void testToString() throws ParseException{
      final Retour r1 = retourDao.findById(1);
      assertTrue(r1.toString().equals("{Echantillon:1 01/01/2010 00:00:00}"));

      final Retour r2 = new Retour();
      assertTrue(r2.toString().equals("{Empty Retour}"));
      r2.setEntite(entiteDao.findById(1));
      assertTrue(r2.toString().equals("{Empty Retour}"));
      r2.setEntite(null);
      final Calendar date1 = Calendar.getInstance();
      date1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("05/01/2010"));
      r2.setDateSortie(date1);
      assertTrue(r2.toString().equals("{Empty Retour}"));
   }

   @Test
public void testClone(){
      final Retour r2 = retourDao.findById(2);
      final Retour clone = r2.clone();
      assertTrue(clone.getRetourId().equals(r2.getRetourId()));
      assertTrue(clone.getObjetId().equals(r2.getObjetId()));
      assertTrue(clone.getEntite().equals(r2.getEntite()));
      assertTrue(clone.getDateSortie().equals(r2.getDateSortie()));
      assertTrue(clone.getDateRetour().equals(r2.getDateRetour()));
      assertTrue(clone.getTempMoyenne().equals(r2.getTempMoyenne()));
      assertTrue(clone.getSterile().equals(r2.getSterile()));
      assertTrue(clone.getObservations().equals(r2.getObservations()));
      assertTrue(clone.getCollaborateur().equals(r2.getCollaborateur()));
      assertTrue(clone.getCession().equals(r2.getCession()));
      assertTrue(clone.getOldEmplacementAdrl().equals(r2.getOldEmplacementAdrl()));
      assertNull(clone.getTransformation());
      assertNull(clone.getConteneur());
      assertNull(clone.getIncident());
      assertTrue(clone.getObjetStatut().equals(r2.getObjetStatut()));
      assertTrue(clone.getImpact().equals(r2.getImpact()));

   }

   @Test
public void testFindByObjDates() throws ParseException{
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      List<Retour> rs = retourDao.findByObjDates(cal, 1, entiteDao.findById(3), -1);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(4));

      rs = retourDao.findByObjDates(cal, 4, entiteDao.findById(3), 0);
      assertTrue(rs.isEmpty());

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("01/01/2010 12:12"));
      rs = retourDao.findByObjDates(cal, 4, entiteDao.findById(3), 0);
      assertTrue(rs.isEmpty());

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010"));
      rs = retourDao.findByObjDates(cal, 4, entiteDao.findById(3), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourDao.findByObjDates(cal, 3, entiteDao.findById(8), 0);
      assertTrue(rs.size() == 2);
      assertTrue(rs.get(0).getRetourId() > 6);

      rs = retourDao.findByObjDates(cal, 3, null, 0);
      assertTrue(rs.isEmpty());

      rs = retourDao.findByObjDates(cal, null, entiteDao.findById(8), 2);
      assertTrue(rs.isEmpty());

      rs = retourDao.findByObjDates(null, 3, entiteDao.findById(8), 1);
      assertTrue(rs.isEmpty());
   }

   @Test
public void testFindObjIdsByDatesAndEntite() throws ParseException{
      final Calendar cal = Calendar.getInstance();
      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      List<Integer> rs = retourDao.findObjIdsByDatesAndEntite(cal, entiteDao.findById(3));
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).equals(1));

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2012 12:12"));
      rs = retourDao.findObjIdsByDatesAndEntite(cal, entiteDao.findById(3));
      assertTrue(rs.isEmpty());

      cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010"));
      rs = retourDao.findObjIdsByDatesAndEntite(cal, entiteDao.findById(3));
      assertTrue(rs.size() == 2);
      assertTrue(rs.contains(1));
      assertTrue(rs.contains(4));

      rs = retourDao.findObjIdsByDatesAndEntite(cal, entiteDao.findById(8));
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).equals(3));

      rs = retourDao.findObjIdsByDatesAndEntite(cal, null);
      assertTrue(rs.isEmpty());

      rs = retourDao.findObjIdsByDatesAndEntite(null, entiteDao.findById(8));
      assertTrue(rs.isEmpty());
   }

   @Test
public void testFindByObjInsideDates() throws ParseException{
      final Calendar cal1 = Calendar.getInstance();
      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("10/01/2010 12:12"));
      List<Retour> rs = retourDao.findByObjInsideDates(cal1, cal2, 1, entiteDao.findById(3), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(5));

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2009 12:12"));
      rs = retourDao.findByObjInsideDates(cal1, cal2, 1, entiteDao.findById(3), -1);
      assertTrue(rs.size() == 5);

      rs = retourDao.findByObjInsideDates(cal1, cal2, 4, entiteDao.findById(3), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourDao.findByObjInsideDates(cal1, cal2, 2, entiteDao.findById(3), 0);
      assertTrue(rs.isEmpty());

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2010"));

      rs = retourDao.findByObjInsideDates(cal1, cal1, 4, entiteDao.findById(3), 0);
      assertTrue(rs.size() == 1);
      assertTrue(rs.get(0).getRetourId().equals(6));

      rs = retourDao.findByObjInsideDates(cal1, cal1, 3, entiteDao.findById(8), 0);
      assertTrue(rs.size() == 2);
      assertTrue(rs.get(0).getRetourId() > 6);

      rs = retourDao.findByObjInsideDates(cal1, cal1, 3, null, 1);
      assertTrue(rs.isEmpty());

      rs = retourDao.findByObjInsideDates(cal1, cal1, null, entiteDao.findById(8), 2);
      assertTrue(rs.isEmpty());

      rs = retourDao.findByObjInsideDates(cal1, null, 3, entiteDao.findById(8), 3);
      assertTrue(rs.isEmpty());

      rs = retourDao.findByObjInsideDates(null, cal1, 3, entiteDao.findById(8), 4);
      assertTrue(rs.isEmpty());
   }

   @Test
public void testFindObjIdsInsideDatesEntite() throws ParseException{
      final Calendar cal1 = Calendar.getInstance();
      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2009 12:12"));
      final Calendar cal2 = Calendar.getInstance();
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("11/01/2010 12:12"));
      List<Integer> rs = retourDao.findObjIdsInsideDatesEntite(cal1, cal2, entiteDao.findById(3));
      assertTrue(rs.size() == 2);
      assertTrue(rs.contains(1));
      assertTrue(rs.contains(4));

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2009 12:12"));
      rs = retourDao.findObjIdsInsideDatesEntite(cal1, cal2, entiteDao.findById(3));
      assertTrue(rs.size() == 2);

      rs = retourDao.findObjIdsInsideDatesEntite(cal1, cal2, entiteDao.findById(8));
      assertTrue(rs.size() == 1);
      assertTrue(rs.contains(3));

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("30/12/2009 12:12"));
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/12/2009 12:12"));
      rs = retourDao.findObjIdsInsideDatesEntite(cal1, cal2, entiteDao.findById(8));
      assertTrue(rs.isEmpty());

      cal1.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("06/01/2010 12:12"));
      cal2.setTime(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("10/01/2010 12:12"));
      rs = retourDao.findObjIdsInsideDatesEntite(cal1, cal2, null);
      assertTrue(rs.isEmpty());

      rs = retourDao.findObjIdsInsideDatesEntite(cal1, null, entiteDao.findById(8));
      assertTrue(rs.isEmpty());

      rs = retourDao.findObjIdsInsideDatesEntite(null, cal2, entiteDao.findById(8));
      assertTrue(rs.isEmpty());
   }

   @Test
public void testFindByObjectDateRetourEmpty(){
      final List<Integer> objetsIds = new ArrayList<>();

      objetsIds.add(1);
      objetsIds.add(2);
      List<Retour> rets = retourDao.findByObjectsDateRetourEmpty(objetsIds, entiteDao.findById(3));
      assertTrue(rets.isEmpty());

      objetsIds.add(3);
      rets = retourDao.findByObjectsDateRetourEmpty(objetsIds, entiteDao.findById(8));
      assertTrue(rets.isEmpty());
   }

   @Test
public void testFindByObjectAndImpact(){
      List<Retour> impacts = retourDao.findByObjectAndImpact(1, entiteDao.findById(3), true);
      assertTrue(impacts.size() == 3);
      impacts = retourDao.findByObjectAndImpact(1, entiteDao.findById(3), false);
      assertTrue(impacts.size() == 1);
      impacts = retourDao.findByObjectAndImpact(1, entiteDao.findById(3), null);
      assertTrue(impacts.size() == 0);
      impacts = retourDao.findByObjectAndImpact(4, entiteDao.findById(3), true);
      assertTrue(impacts.size() == 0);
      impacts = retourDao.findByObjectAndImpact(2, entiteDao.findById(3), true);
      assertTrue(impacts.size() == 0);
      impacts = retourDao.findByObjectAndImpact(3, entiteDao.findById(8), true);
      assertTrue(impacts.size() == 0);
      impacts = retourDao.findByObjectAndImpact(3, entiteDao.findById(8), false);
      assertTrue(impacts.size() == 2);

   }

}
