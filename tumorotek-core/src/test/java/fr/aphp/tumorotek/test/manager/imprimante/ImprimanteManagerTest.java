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
package fr.aphp.tumorotek.test.manager.imprimante;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.context.PlateformeManager;
import fr.aphp.tumorotek.manager.imprimante.AffectationImprimanteManager;
import fr.aphp.tumorotek.manager.imprimante.ImprimanteApiManager;
import fr.aphp.tumorotek.manager.imprimante.ImprimanteManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.ImprimanteApi;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.test.manager.AbstractManagerTest4;

/**
 *
 * Classe de test pour le manager ImprimanteManager.
 * Classe créée le 17/03/2011.
 *
 * @author Pierre Ventadour.
 * @version 2.0.11
 *
 */
public class ImprimanteManagerTest extends AbstractManagerTest4
{

   @Autowired
   private ImprimanteManager imprimanteManager;
   @Autowired
   private ImprimanteApiManager imprimanteApiManager;
   @Autowired
   private PlateformeManager plateformeManager;
   @Autowired
   private AffectationImprimanteManager affectationImprimanteManager;
   @Autowired
   private UtilisateurDao utilisateurDao;
   @Autowired
   private BanqueDao banqueDao;
   @Autowired
   private ModeleDao modeleDao;

   public ImprimanteManagerTest(){}

   @Test
   public void testFindById(){
      final Imprimante m = imprimanteManager.findByIdManager(1);
      assertNotNull(m);
      assertTrue(m.getNom().equals("PDF"));

      final Imprimante mNull = imprimanteManager.findByIdManager(10);
      assertNull(mNull);
   }

   /**
    * Test la méthode findAllObjects.
    */
   @Test
   public void testFindAll(){
      final List<Imprimante> list = imprimanteManager.findAllObjectsManager();
      assertTrue(list.size() == 2);
   }

   /**
    * Test la méthode findByPlateformeManager.
    */
   @Test
   public void testFindByPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<Imprimante> list = imprimanteManager.findByPlateformeManager(pf1);
      assertTrue(list.size() == 2);

      list = imprimanteManager.findByPlateformeManager(pf2);
      assertTrue(list.size() == 0);

      list = imprimanteManager.findByPlateformeManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByNomAndPlateformeManager.
    */
   @Test
   public void testFindByNomAndPlateformeManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<Imprimante> list = imprimanteManager.findByNomAndPlateformeManager("PDF", pf1);
      assertTrue(list.size() == 1);

      list = imprimanteManager.findByNomAndPlateformeManager("ygqué", pf1);
      assertTrue(list.size() == 0);

      list = imprimanteManager.findByNomAndPlateformeManager("PDF", pf2);
      assertTrue(list.size() == 0);

      list = imprimanteManager.findByNomAndPlateformeManager(null, pf2);
      assertTrue(list.size() == 0);

      list = imprimanteManager.findByNomAndPlateformeManager("PDF", null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findByPlateformeSelectNomManager.
    */
   @Test
   public void testFindByPlateformeSelectNomManager(){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final Plateforme pf2 = plateformeManager.findByIdManager(2);

      List<String> list = imprimanteManager.findByPlateformeSelectNomManager(pf1);
      assertTrue(list.size() == 2);
      assertTrue(list.get(0).equals("PDF"));

      list = imprimanteManager.findByPlateformeSelectNomManager(pf2);
      assertTrue(list.size() == 0);

      list = imprimanteManager.findByPlateformeSelectNomManager(null);
      assertTrue(list.size() == 0);
   }

   /**
    * Test la méthode findDoublon.
    */
   @Test
   public void testFindDoublon(){

      final String nom1 = "PDF";
      final String nom2 = "TEST";
      final Plateforme pf1 = plateformeManager.findByIdManager(1);

      final Imprimante m1 = new Imprimante();
      m1.setPlateforme(pf1);
      m1.setNom(nom1);
      assertTrue(imprimanteManager.findDoublonManager(m1));

      m1.setNom(nom2);
      assertFalse(imprimanteManager.findDoublonManager(m1));

      final Imprimante m2 = imprimanteManager.findByIdManager(2);
      assertFalse(imprimanteManager.findDoublonManager(m2));

      m2.setNom(nom1);
      assertTrue(imprimanteManager.findDoublonManager(m2));

      assertFalse(imprimanteManager.findDoublonManager(null));

   }

   /**
    * Test la méthode isUsedObjectManager.
    */
   @Test
   public void testIsUsedObjectManager(){
      final Imprimante m1 = imprimanteManager.findByIdManager(1);
      assertTrue(imprimanteManager.isUsedObjectManager(m1));

      assertFalse(imprimanteManager.isUsedObjectManager(new Imprimante()));
      assertFalse(imprimanteManager.isUsedObjectManager(null));
   }

   /**
    * Test le CRUD d'un ProtocoleExt.
    * @throws ParseException 
    */
   @Test
   public void testCrud() throws ParseException{
      saveManagerTest();
      saveManagerTest();
      deleteByIdManagerTest();
   }

   private void saveManagerTest() throws ParseException{

      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ImprimanteApi ia1 = imprimanteApiManager.findByIdManager(1);
      final Imprimante i1 = new Imprimante();
      i1.setNom("IMP");

      boolean catched = false;
      // on test l'insertion avec une pf nulle
      try{
         imprimanteManager.saveManager(i1, null, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);

      // on test l'insertion avec un api null
      try{
         imprimanteManager.saveManager(i1, pf1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);

      // on test l'insertion d'un doublon
      i1.setNom("PDF");
      try{
         imprimanteManager.saveManager(i1, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);

      validationInsert(i1);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);

      // insertion valide
      i1.setAbscisse(10);
      i1.setOrdonnee(15);
      i1.setLargeur(20);
      i1.setLongueur(25);
      i1.setOrientation(1);
      i1.setMbioPrinter(3);
      i1.setResolution(200);
      imprimanteManager.saveManager(i1, pf1, ia1);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);
      final Integer idI = i1.getImprimanteId();

      final Imprimante mTest = imprimanteManager.findByIdManager(idI);
      assertNotNull(mTest);
      assertNotNull(mTest.getPlateforme());
      assertNotNull(mTest.getImprimanteApi());
      assertTrue(mTest.getNom().equals("NEW"));
      assertTrue(mTest.getAbscisse() == 10);
      assertTrue(mTest.getOrdonnee() == 15);
      assertTrue(mTest.getLargeur() == 20);
      assertTrue(mTest.getLongueur() == 25);
      assertTrue(mTest.getOrientation() == 1);
      assertTrue(mTest.getMbioPrinter() == 3);
      assertTrue(mTest.getAdresseIp().equals("10.15.23.169"));
      assertTrue(mTest.getResolution() == 200);
      assertTrue(mTest.getPort() == 9100);

      // suppression des profils et des droits
      imprimanteManager.deleteByIdManager(mTest);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);
   }

   private void saveManagerTest() throws ParseException{

      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ImprimanteApi ia1 = imprimanteApiManager.findByIdManager(1);
      final Imprimante im = new Imprimante();
      im.setNom("IMP");
      im.setAbscisse(5);
      im.setOrdonnee(5);
      im.setLargeur(5);
      im.setLongueur(5);
      im.setOrientation(5);
      im.setMbioPrinter(null);
      im.setResolution(150);
      im.setPort(150);
      imprimanteManager.saveManager(im, pf1, ia1);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);
      final Integer idI = im.getImprimanteId();

      final Imprimante iUp = imprimanteManager.findByIdManager(idI);
      boolean catched = false;
      // on test l'insertion avec une pf nulle
      try{
         imprimanteManager.saveManager(iUp, null, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      // on test l'insertion avec un api null
      try{
         imprimanteManager.saveManager(iUp, pf1, null);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("RequiredObjectIsNullException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      // on test l'insertion d'un doublon
      iUp.setNom("PDF");
      try{
         imprimanteManager.saveManager(iUp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("DoublonFoundException")){
            catched = true;
         }
      }
      assertTrue(catched);
      catched = false;
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      validationUpdate(iUp);
      iUp.setNom("UP");
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      // update valide
      iUp.setAbscisse(10);
      iUp.setOrdonnee(15);
      iUp.setLargeur(20);
      iUp.setLongueur(25);
      iUp.setOrientation(1);
      iUp.setMbioPrinter(5);
      iUp.setResolution(null);
      imprimanteManager.saveManager(iUp, pf1, ia1);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      final Imprimante mTest = imprimanteManager.findByIdManager(idI);
      assertNotNull(mTest);
      assertNotNull(mTest.getPlateforme());
      assertNotNull(mTest.getImprimanteApi());
      assertTrue(mTest.getNom().equals("UP"));
      assertTrue(mTest.getAbscisse() == 10);
      assertTrue(mTest.getOrdonnee() == 15);
      assertTrue(mTest.getLargeur() == 20);
      assertTrue(mTest.getLongueur() == 25);
      assertTrue(mTest.getOrientation() == 1);
      assertTrue(mTest.getMbioPrinter() == 5);
      assertTrue(mTest.getOrientation() == 1);
      assertTrue(mTest.getAdresseIp().equals("23.58.69.147"));
      assertNull(mTest.getResolution());
      assertTrue(mTest.getPort() == 9200);

      // suppression des profils et des droits
      imprimanteManager.deleteByIdManager(mTest);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);
   }

   private void deleteByIdManagerTest() throws ParseException{
      // creation d'une imprimante
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ImprimanteApi ia1 = imprimanteApiManager.findByIdManager(1);
      final Imprimante im = new Imprimante();
      im.setNom("IMP");
      im.setAbscisse(5);
      im.setOrdonnee(5);
      im.setLargeur(5);
      im.setLongueur(5);
      im.setOrientation(5);
      imprimanteManager.saveManager(im, pf1, ia1);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 3);

      // creation d'une AffectationImprimante
      final Banque b1 = banqueDao.findById(1);
      final Modele m1 = modeleDao.findById(1);
      final Utilisateur u1 = utilisateurDao.findById(1);
      final AffectationImprimante ai = new AffectationImprimante();
      affectationImprimanteManager.saveManager(ai, u1, b1, im, m1);
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 5);

      // on vérifie que tous les objets sont supprimés
      imprimanteManager.deleteByIdManager(im);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);
      assertTrue(affectationImprimanteManager.findAllObjectsManager().size() == 4);

      imprimanteManager.deleteByIdManager(null);
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);
   }

   private void validationInsert(final Imprimante imp){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ImprimanteApi ia1 = imprimanteApiManager.findByIdManager(1);
      // on teste la validation du nom lors de la création
      final String[] emptyValues = new String[] {null, "", "  ", "%¬ ↓»üß*d", createOverLength(50)};
      boolean catched = false;
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            imp.setNom(emptyValues[i]);
            imprimanteManager.saveManager(imp, pf1, ia1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      imp.setNom("NEW");
      assertTrue(imprimanteManager.findAllObjectsManager().size() == 2);

      // validation de l'abscisse
      catched = false;
      try{
         imp.setAbscisse(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setAbscisse(1);

      // validation de l'ordonnée
      catched = false;
      try{
         imp.setOrdonnee(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setOrdonnee(1);

      // validation de la largeur
      catched = false;
      try{
         imp.setLargeur(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setLargeur(1);

      // validation de la longeur
      catched = false;
      try{
         imp.setLongueur(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setLongueur(1);

      // validation de l'orientation
      catched = false;
      try{
         imp.setOrientation(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setOrientation(1);

      // validation de l'adresse IP
      catched = false;
      try{
         imp.setAdresseIp("1.KO.12.58");
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setAdresseIp("10.15.23.169");

      // validation du port IP
      catched = false;
      try{
         imp.setPort(-25);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setPort(9100);
   }

   private void validationUpdate(final Imprimante imp){
      final Plateforme pf1 = plateformeManager.findByIdManager(1);
      final ImprimanteApi ia1 = imprimanteApiManager.findByIdManager(1);
      // on teste la validation du nom lors de la création
      final String[] emptyValues = new String[] {null, "", "  ", "%¬ ↓»üß*d", createOverLength(50)};
      boolean catched = false;
      for(int i = 0; i < emptyValues.length; i++){
         catched = false;
         try{
            imp.setNom(emptyValues[i]);
            imprimanteManager.saveManager(imp, pf1, ia1);
         }catch(final Exception e){
            if(e.getClass().getSimpleName().equals("ValidationException")){
               catched = true;
            }
         }
         assertTrue(catched);
      }
      imp.setNom("NEW");

      // validation de l'abscisse
      catched = false;
      try{
         imp.setAbscisse(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setAbscisse(1);

      // validation de l'ordonnée
      catched = false;
      try{
         imp.setOrdonnee(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setOrdonnee(1);

      // validation de la largeur
      catched = false;
      try{
         imp.setLargeur(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setLargeur(1);

      // validation de la longeur
      catched = false;
      try{
         imp.setLongueur(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setLongueur(1);

      // validation de l'orientation
      catched = false;
      try{
         imp.setOrientation(null);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setOrientation(1);

      // validation de l'adresse IP
      catched = false;
      try{
         imp.setAdresseIp("11.22.89;AZ");
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setAdresseIp("23.58.69.147");

      // validation de l'adresse IP
      catched = false;
      try{
         imp.setPort(1258741);
         imprimanteManager.saveManager(imp, pf1, ia1);
      }catch(final Exception e){
         if(e.getClass().getSimpleName().equals("ValidationException")){
            catched = true;
         }
      }
      assertTrue(catched);
      imp.setPort(9200);
   }
}
