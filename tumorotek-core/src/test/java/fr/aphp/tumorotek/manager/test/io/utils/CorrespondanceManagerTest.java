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
package fr.aphp.tumorotek.manager.test.io.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest4;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;

public class CorrespondanceManagerTest extends AbstractManagerTest4
{

   @Autowired
   private PatientManager patientManager;
   @Autowired
   private MaladieManager maladieManager;
   @Autowired
   private PrelevementManager prelevementManager;
   @Autowired
   private EchantillonManager echantillonManager;
   @Autowired
   private ProdDeriveManager prodDeriveManager;
   @Autowired
   private CorrespondanceManager correspondanceManager;

   private List<Patient> allPatients = null;
   private List<Maladie> allMaladies = null;
   private List<Prelevement> allPrelevements = null;
   private List<Echantillon> allEchantillons = null;
   private List<ProdDerive> allDerives = null;

   @Autowired
   private MaladieDao maladieDao;

   @Autowired
   private List<Patient> getPatients(){
      if(allPatients == null){
         this.allPatients = patientManager.findAllObjectsManager();
      }
      return allPatients;
   }

   @Autowired
   private List<Maladie> getMaladies(){
      if(allMaladies == null){
         this.allMaladies = maladieManager.findAllObjectsManager();
      }
      return allMaladies;
   }

   @Autowired
   private List<Prelevement> getPrelevements(){
      if(allPrelevements == null){
         this.allPrelevements = prelevementManager.findAllObjectsManager();
      }
      return allPrelevements;
   }

   @Autowired
   private List<Echantillon> getEchantillons(){
      if(allEchantillons == null){
         this.allEchantillons = echantillonManager.findAllObjectsManager();
      }
      return allEchantillons;
   }

   private List<ProdDerive> getDerives(){
      if(allDerives == null){
         this.allDerives = prodDeriveManager.findAllObjectsManager();
      }
      return allDerives;
   }

   /**
    * Test de la récupération des patients correspondants aux maladies de 
    * la BDD.
    */
   @Test
   public void testRecuperePatientsDepuisMaladies(){
      /* On construit une liste de toutes les maladies (de type Object). */
      final List<Object> allMalObject = new ArrayList<>();
      allMalObject.addAll(getMaladies());
      /* On récupère tous les patients de la liste de maladies. */
      final List<Object> patients = correspondanceManager.recupereEntitesViaDAutres(allMalObject, "Patient");

      /* On vérifie qu'il n'y a pas de doublon de patients trouvés.  */
      for(int i = 0; i < patients.size(); i++){
         final Patient patient1 = (Patient) patients.get(i);
         for(int j = i + 1; j < patients.size(); j++){
            final Patient patient2 = (Patient) patients.get(j);
            assertFalse(patient1.equals(patient2));
         }
      }

      /* Pour chaque maladie de la BDD, on vérifie que son patient est dans la
       * liste des patients correspondants récupérée. */
      for(final Iterator<Maladie> itMaladies = getMaladies().iterator(); itMaladies.hasNext();){
         final Maladie maladie = itMaladies.next();
         if(maladie != null){
            assertTrue(patients.contains(maladie.getPatient()));
         }
      }
   }

   /**
    * Test de la récupération des patients correspondants aux prélèvements de 
    * la BDD.
    */
   @Test
   public void testRecuperePatientsDepuisPrelevements(){
      /* On construit une liste de tous les prélèvements (de type Object). */
      final List<Object> allPreObject = new ArrayList<>();
      allPreObject.addAll(getPrelevements());
      /* On récupère tous les patients de la liste de prélèvements. */
      final List<Object> patients = correspondanceManager.recupereEntitesViaDAutres(allPreObject, "Patient");

      /* On vérifie qu'il n'y a pas de doublon de patients trouvés.  */
      for(int i = 0; i < patients.size(); i++){
         final Patient patient1 = (Patient) patients.get(i);
         for(int j = i + 1; j < patients.size(); j++){
            final Patient patient2 = (Patient) patients.get(j);
            assertFalse(patient1.equals(patient2));
         }
      }

      /* Pour chaque prélèvement de la BDD, on vérifie que son patient est 
       * dans la liste des patients correspondants récupérée. */
      for(final Iterator<Prelevement> itPrelevements = getPrelevements().iterator(); itPrelevements.hasNext();){
         final Prelevement prelevement = itPrelevements.next();
         if(prelevement != null){
            if(prelevement.getMaladie() != null){
               final Maladie maladie = maladieDao.findById(prelevement.getMaladie().getMaladieId()).orElse(null);
               if(maladie.getPatient() != null){
                  assertTrue(patients.contains(maladie.getPatient()));
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des patients correspondants aux échantillons de 
    * la BDD.
    */
   @Test
   public void testRecuperePatientsDepuisEchantillons(){
      /* On construit une liste de tous les échantillons (de type Object). */
      final List<Object> allPreObject = new ArrayList<>();
      allPreObject.addAll(getEchantillons());
      /* On récupère tous les patients de la liste d'échantillons. */
      final List<Object> patients = correspondanceManager.recupereEntitesViaDAutres(allPreObject, "Patient");

      /* On vérifie qu'il n'y a pas de doublon de patients trouvés.  */
      for(int i = 0; i < patients.size(); i++){
         final Patient patient1 = (Patient) patients.get(i);
         for(int j = i + 1; j < patients.size(); j++){
            final Patient patient2 = (Patient) patients.get(j);
            assertFalse(patient1.equals(patient2));
         }
      }

      /* Pour chaque échantillon de la BDD, on vérifie que son patient est 
       * dans la liste des patients correspondants récupérée. */
      for(final Iterator<Echantillon> itEchantillons = getEchantillons().iterator(); itEchantillons.hasNext();){
         final Echantillon echantillon = itEchantillons.next();
         if(echantillon != null){
            final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
            if(prelevement != null){
               if(prelevement.getMaladie() != null){
                  final Maladie maladie = maladieDao.findById(prelevement.getMaladie().getMaladieId()).orElse(null);
                  if(maladie.getPatient() != null){
                     assertTrue(patients.contains(maladie.getPatient()));
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des patients correspondants aux produits dérivés
    * de la BDD.
    */
   @Test
   public void testRecuperePatientsDepuisProdDerives(){
      /* On construit une liste de tous les dérivés (de type Object). */
      final List<Object> allDerObject = new ArrayList<>();
      allDerObject.addAll(getDerives());
      /* On récupère tous les patients de la liste de produits dérivés. */
      final List<Object> patients = correspondanceManager.recupereEntitesViaDAutres(allDerObject, "Patient");

      /* On vérifie qu'il n'y a pas de doublon de patients trouvés.  */
      for(int i = 0; i < patients.size(); i++){
         final Patient patient1 = (Patient) patients.get(i);
         for(int j = i + 1; j < patients.size(); j++){
            final Patient patient2 = (Patient) patients.get(j);
            assertFalse(patient1.equals(patient2));
         }
      }

      /*
       * Pour chaque dérivé de la BDD, on vérifie que ses patients
       * sont dans la liste des patients correspondants récupérée.
       */
      for(final Iterator<ProdDerive> itDerives = getDerives().iterator(); itDerives.hasNext();){
         final ProdDerive derive = itDerives.next();
         if(derive != null){
            /* On récupère le parent du produit dérivé. */
            final Transformation transformation = derive.getTransformation();
            if(transformation.getEntite().getNom().equals("Echantillon")){
               final Echantillon echantillon = echantillonManager.findByIdManager(transformation.getObjetId());
               if(echantillon != null){
                  final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
                  if(prelevement != null){
                     final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
                     if(maladie != null){
                        final Patient patient = maladie.getPatient();
                        if(patient != null){
                           /* On vérifie que le patient est dans la liste
                            * des patients récupérée. */
                           assertTrue(patients.contains(patient));

                        }
                     }
                  }
               }
            }else if(transformation.getEntite().getNom().equals("Prelevement")){
               final Prelevement prelevement = prelevementManager.findByIdManager(transformation.getObjetId());
               if(prelevement != null){
                  final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
                  if(maladie != null){
                     final Patient patient = maladie.getPatient();
                     if(patient != null){
                        /* On vérifie que le patient est dans la liste
                         * des patients récupérée. */
                        assertTrue(patients.contains(patient));

                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des maladies correspondantes aux patients de 
    * la BDD.
    */
   @Test
   public void testRecupereMaladiesDepuisPatients(){
      /* On construit une liste de tous les patients (de type Object). */
      final List<Object> allPatObject = new ArrayList<>();
      allPatObject.addAll(getPatients());
      /* On récupère toutes les maladies de la liste de patients. */
      final List<Object> maladies = correspondanceManager.recupereEntitesViaDAutres(allPatObject, "Maladie");

      /* On vérifie qu'il n'y a pas de doublon de maladies trouvées.  */
      for(int i = 0; i < maladies.size(); i++){
         final Maladie maladie1 = (Maladie) maladies.get(i);
         for(int j = i + 1; j < maladies.size(); j++){
            final Maladie maladie2 = (Maladie) maladies.get(j);
            assertFalse(maladie1.equals(maladie2));
         }
      }

      /* Pour chaque patient de la BDD, on vérifie que sa maladie est dans la
       * liste des maladies correspondantes récupérée. */
      for(final Iterator<Patient> itPatients = getPatients().iterator(); itPatients.hasNext();){
         final Patient patient = itPatients.next();
         if(patient != null){
            /* On récupère les maladies du patient. */
            final Set<Maladie> setMaladies = maladieManager.getMaladiesManager(patient);
            if(setMaladies != null){
               /* On vérifie que les maladies sont dans la 
                * liste des maladies récupérée. */
               for(final Iterator<Maladie> itMaladies = setMaladies.iterator(); itMaladies.hasNext();){
                  final Maladie maladie = itMaladies.next();
                  assertTrue(maladies.contains(maladie));
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des maladies correspondantes aux prélèvements de 
    * la BDD.
    */
   @Test
   public void testRecupereMaladiesDepuisPrelevements(){
      /* On construit une liste de tous les prelevements (de type Object). */
      final List<Object> allPreObject = new ArrayList<>();
      allPreObject.addAll(getPrelevements());
      /* On récupère toutes les maladies de la liste de prelevements. */
      final List<Object> maladies = correspondanceManager.recupereEntitesViaDAutres(allPreObject, "Maladie");

      /* On vérifie qu'il n'y a pas de doublon de maladies trouvées.  */
      for(int i = 0; i < maladies.size(); i++){
         final Maladie maladie1 = (Maladie) maladies.get(i);
         for(int j = i + 1; j < maladies.size(); j++){
            final Maladie maladie2 = (Maladie) maladies.get(j);
            assertFalse(maladie1.equals(maladie2));
         }
      }

      /* Pour chaque prelevement de la BDD, on vérifie que sa maladie est dans
       * la liste des maladies correspondantes récupérée. */
      for(final Iterator<Prelevement> itPrelevements = getPrelevements().iterator(); itPrelevements.hasNext();){
         final Prelevement prelevement = itPrelevements.next();
         if(prelevement != null){
            /* On récupère la maladie du prelevement. */
            final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
            if(maladie != null){
               /* On vérifie que la maladie est dans la 
                * liste des maladies récupérée. */
               assertTrue(maladies.contains(maladie));
            }
         }
      }
   }

   /**
    * Test de la récupération des maladies correspondantes aux échantillons de 
    * la BDD.
    */
   @Test
   public void testRecupereMaladiesDepuisEchantillons(){
      /* On construit une liste de tous les echantillons (de type Object). */
      final List<Object> allEchObject = new ArrayList<>();
      allEchObject.addAll(getEchantillons());
      /* On récupère toutes les maladies de la liste d'échantillons. */
      final List<Object> maladies = correspondanceManager.recupereEntitesViaDAutres(allEchObject, "Maladie");

      /* On vérifie qu'il n'y a pas de doublon de maladies trouvées.  */
      for(int i = 0; i < maladies.size(); i++){
         final Maladie maladie1 = (Maladie) maladies.get(i);
         for(int j = i + 1; j < maladies.size(); j++){
            final Maladie maladie2 = (Maladie) maladies.get(j);
            assertFalse(maladie1.equals(maladie2));
         }
      }

      /* Pour chaque échantillon de la BDD, on vérifie que sa maladie est dans
       * la liste des maladies correspondantes récupérée. */
      for(final Iterator<Echantillon> itEchantillons = getEchantillons().iterator(); itEchantillons.hasNext();){
         final Echantillon echantillon = itEchantillons.next();
         if(echantillon != null){
            /* On récupère le prélèvement de l'échantillon. */
            final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
            if(prelevement != null){
               /* On récupère la maladie du prelevement. */
               final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
               /* On vérifie que la maladie est dans la 
                * liste des maladies récupérée. */
               assertTrue(maladies.contains(maladie));
            }
         }
      }
   }

   /**
    * Test de la récupération des maladies correspondantes aux produits dérivés
    * de la BDD.
    */
   @Test
   public void testRecupereMaladiesDepuisProdDerives(){
      /* On construit une liste de tous les dérivés (de type Object). */
      final List<Object> allDerObject = new ArrayList<>();
      allDerObject.addAll(getDerives());
      /* On récupère toutes les maladies de la liste de dérivés. */
      final List<Object> maladies = correspondanceManager.recupereEntitesViaDAutres(allDerObject, "Maladie");

      /* On vérifie qu'il n'y a pas de doublon de maladies trouvées.  */
      for(int i = 0; i < maladies.size(); i++){
         final Maladie maladie1 = (Maladie) maladies.get(i);
         for(int j = i + 1; j < maladies.size(); j++){
            final Maladie maladie2 = (Maladie) maladies.get(j);
            assertFalse(maladie1.equals(maladie2));
         }
      }

      /*
       * Pour chaque dérivé de la BDD, on vérifie que ses maladies
       * sont dans la liste des maladies correspondantes récupérée.
       */
      for(final Iterator<ProdDerive> itDerives = getDerives().iterator(); itDerives.hasNext();){
         final ProdDerive derive = itDerives.next();
         if(derive != null){
            /* On récupère le parent du produit dérivé. */
            final Transformation transformation = derive.getTransformation();
            if(transformation.getEntite().getNom().equals("Echantillon")){
               final Echantillon echantillon = echantillonManager.findByIdManager(transformation.getObjetId());
               if(echantillon != null){
                  final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
                  if(prelevement != null){
                     final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
                     if(maladie != null){
                        /* On vérifie que la maladie est dans la liste
                         * des maladies récupérée. */
                        assertTrue(maladies.contains(maladie));
                     }
                  }
               }
            }else if(transformation.getEntite().getNom().equals("Prelevement")){
               final Prelevement prelevement = prelevementManager.findByIdManager(transformation.getObjetId());
               if(prelevement != null){
                  final Maladie maladie = prelevementManager.getMaladieManager(prelevement);
                  if(maladie != null){
                     /* On vérifie que la maladie est dans la liste
                      * des maladies récupérée. */
                     assertTrue(maladies.contains(maladie));
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des prelevements correspondants aux patients de 
    * la BDD.
    */
   @Test
   public void testRecuperePrelevementsDepuisPatients(){
      /* On construit une liste de tous les patients (de type Object). */
      final List<Object> allPatObject = new ArrayList<>();
      allPatObject.addAll(getPatients());
      /* On récupère tous les prélèvements de la liste de patients. */
      final List<Object> prelevements = correspondanceManager.recupereEntitesViaDAutres(allPatObject, "Prelevement");

      /* On vérifie qu'il n'y a pas de doublon de prélèvements trouvés.  */
      for(int i = 0; i < prelevements.size(); i++){
         final Prelevement prelevement1 = (Prelevement) prelevements.get(i);
         for(int j = i + 1; j < prelevements.size(); j++){
            final Prelevement prelevement2 = (Prelevement) prelevements.get(j);
            assertFalse(prelevement1.equals(prelevement2));
         }
      }

      /* Pour chaque patient de la BDD, on vérifie que son prélèvement est
       * dans la liste des prélèvements correspondants récupérée. */
      for(final Iterator<Patient> itPatients = getPatients().iterator(); itPatients.hasNext();){
         final Patient patient = itPatients.next();
         if(patient != null){
            /* On récupère les maladies du patient. */
            final Set<Maladie> setMaladies = maladieManager.getMaladiesManager(patient);
            if(setMaladies != null){
               for(final Iterator<Maladie> itMaladies = setMaladies.iterator(); itMaladies.hasNext();){
                  final Maladie maladie = itMaladies.next();
                  /* On récupère les prélèvements de la maladie. */
                  final Set<Prelevement> setPrelevements = maladieManager.getPrelevementsManager(maladie);
                  for(final Iterator<Prelevement> itPrelevements = setPrelevements.iterator(); itPrelevements.hasNext();){
                     final Prelevement prelevement = itPrelevements.next();
                     assertTrue(prelevements.contains(prelevement));
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des prelevements correspondants aux maladies de 
    * la BDD.
    */
   @Test
   public void testRecuperePrelevementsDepuisMaladies(){
      /* On construit une liste de toutes les maladies (de type Object). */
      final List<Object> allMalObject = new ArrayList<>();
      allMalObject.addAll(getMaladies());
      /* On récupère tous les prélèvements de la liste de maladies. */
      final List<Object> prelevements = correspondanceManager.recupereEntitesViaDAutres(allMalObject, "Prelevement");

      /* On vérifie qu'il n'y a pas de doublon de prélèvements trouvés.  */
      for(int i = 0; i < prelevements.size(); i++){
         final Prelevement prelevement1 = (Prelevement) prelevements.get(i);
         for(int j = i + 1; j < prelevements.size(); j++){
            final Prelevement prelevement2 = (Prelevement) prelevements.get(j);
            assertFalse(prelevement1.equals(prelevement2));
         }
      }

      /* Pour chaque maladie de la BDD, on vérifie que son prélèvement est
       * dans la liste des prélèvements correspondants récupérée. */
      for(final Iterator<Maladie> itMaladies = getMaladies().iterator(); itMaladies.hasNext();){
         final Maladie maladie = itMaladies.next();
         if(maladie != null){
            /* On récupère les prélèvements de la maladie. */
            final Set<Prelevement> setPrelevements = maladieManager.getPrelevementsManager(maladie);
            for(final Iterator<Prelevement> itPrelevements = setPrelevements.iterator(); itPrelevements.hasNext();){
               final Prelevement prelevement = itPrelevements.next();
               assertTrue(prelevements.contains(prelevement));

            }
         }
      }
   }

   /**
    * Test de la récupération des prelevements correspondants aux echantillons
    * de la BDD.
    */
   @Test
   public void testRecuperePrelevementsDepuisEchantillons(){
      /* On construit une liste de tous les échantillons (de type Object). */
      final List<Object> allEchObject = new ArrayList<>();
      allEchObject.addAll(getEchantillons());
      /* On récupère tous les prélèvements de la liste d'échantillons. */
      final List<Object> prelevements = correspondanceManager.recupereEntitesViaDAutres(allEchObject, "Prelevement");

      /* On vérifie qu'il n'y a pas de doublon de prélèvements trouvés.  */
      for(int i = 0; i < prelevements.size(); i++){
         final Prelevement prelevement1 = (Prelevement) prelevements.get(i);
         for(int j = i + 1; j < prelevements.size(); j++){
            final Prelevement prelevement2 = (Prelevement) prelevements.get(j);
            assertFalse(prelevement1.equals(prelevement2));
         }
      }

      /* Pour chaque échantillon de la BDD, on vérifie que son prélèvement est
       * dans la liste des prélèvements correspondants récupérée. */
      for(final Iterator<Echantillon> itEchantillons = getEchantillons().iterator(); itEchantillons.hasNext();){
         final Echantillon echantillon = itEchantillons.next();
         if(echantillon != null){
            /* On récupère le prélèvement de l'échantillon. */
            final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
            assertTrue(prelevements.contains(prelevement));
         }
      }
   }

   /**
    * Test de la récupération des prelevements correspondants aux produits
    * dérivés de la BDD.
    */
   @Test
   public void testRecuperePrelevementsDepuisProdDerives(){
      /* On construit une liste de tous les dérivés (de type Object). */
      final List<Object> allDerObject = new ArrayList<>();
      allDerObject.addAll(getDerives());
      /* On récupère tous les prélèvements de la liste d'échantillons. */
      final List<Object> prelevements = correspondanceManager.recupereEntitesViaDAutres(allDerObject, "Prelevement");

      /* On vérifie qu'il n'y a pas de doublon de prélèvements trouvés.  */
      for(int i = 0; i < prelevements.size(); i++){
         final Prelevement prelevement1 = (Prelevement) prelevements.get(i);
         for(int j = i + 1; j < prelevements.size(); j++){
            final Prelevement prelevement2 = (Prelevement) prelevements.get(j);
            assertFalse(prelevement1.equals(prelevement2));
         }
      }

      /*
       * Pour chaque dérivé de la BDD, on vérifie que ses prélèvements
       * sont dans la liste des prélèvements correspondants récupérée.
       */
      for(final Iterator<ProdDerive> itDerives = getDerives().iterator(); itDerives.hasNext();){
         final ProdDerive derive = itDerives.next();
         if(derive != null){
            /* On récupère le parent du produit dérivé. */
            final Transformation transformation = derive.getTransformation();
            if(transformation.getEntite().getNom().equals("Echantillon")){
               final Echantillon echantillon = echantillonManager.findByIdManager(transformation.getObjetId());
               if(echantillon != null){
                  final Prelevement prelevement = echantillonManager.getPrelevementManager(echantillon);
                  if(prelevement != null){
                     /* On vérifie que le prélèvements est dans la liste
                      * des prélèvements récupérée. */
                     boolean in = false;
                     for(int i = 0; i < prelevements.size(); i++){
                        if(((Prelevement) prelevements.get(i)).getCode().equals(prelevement.getCode())){
                           in = true;
                        }
                     }
                     assertTrue(in);
                  }
               }
            }else if(transformation.getEntite().getNom().equals("Prelevement")){
               final Prelevement prelevement = prelevementManager.findByIdManager(transformation.getObjetId());
               if(prelevement != null){
                  /* On vérifie que le prélèvement est dans la liste des
                   * prélèvements récupérée. */
                  boolean in = false;
                  for(int i = 0; i < prelevements.size(); i++){
                     if(((Prelevement) prelevements.get(i)).getCode().equals(prelevement.getCode())){
                        in = true;
                     }
                  }
                  assertTrue(in);
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des échantillons correspondants aux patients de 
    * la BDD.
    */
   @Test
   public void testRecupereEchantillonsDepuisPatients(){
      /* On construit une liste de tous les patients (de type Object). */
      final List<Object> allPatObject = new ArrayList<>();
      allPatObject.addAll(getPatients());
      /* On récupère tous les échantillons de la liste de maladies. */
      final List<Object> echantillons = correspondanceManager.recupereEntitesViaDAutres(allPatObject, "Echantillon");

      /* On vérifie qu'il n'y a pas de doublon d'échantillons trouvés.  */
      for(int i = 0; i < echantillons.size(); i++){
         final Echantillon echantillon1 = (Echantillon) echantillons.get(i);
         for(int j = i + 1; j < echantillons.size(); j++){
            final Echantillon echantillon2 = (Echantillon) echantillons.get(j);
            assertFalse(echantillon1.equals(echantillon2));
         }
      }

      /*
       * Pour chaque patient de la BDD, on vérifie que ses échantillons sont
       * dans la liste des échantillons correspondants récupérée.
       */
      for(final Iterator<Patient> itPatients = getPatients().iterator(); itPatients.hasNext();){
         final Patient patient = itPatients.next();
         if(patient != null){
            /* On récupère les maladies du patient. */
            final Set<Maladie> setMaladies = maladieManager.getMaladiesManager(patient);
            for(final Iterator<Maladie> itMaladies = setMaladies.iterator(); itMaladies.hasNext();){
               final Maladie maladie = itMaladies.next();
               if(maladie != null){
                  /* On récupère les prélèvements de la maladie. */
                  final Set<Prelevement> prelevements = maladieManager.getPrelevementsManager(maladie);
                  if(prelevements != null){
                     for(final Iterator<Prelevement> itPrelevements = prelevements.iterator(); itPrelevements.hasNext();){
                        /* On récupère les échantillons
                         * du prélèvement. */
                        final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(itPrelevements.next());
                        if(echans != null){
                           /*
                            * On vérifie que les échantillons sont dans
                            * la liste des échantillons récupérée.
                            */
                           for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
                              final Echantillon echantillon = itEchantillons.next();
                              assertTrue(echantillons.contains(echantillon));
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des échantillons correspondants aux maladies de 
    * la BDD.
    */
   @Test
   public void testRecupereEchantillonsDepuisMaladies(){
      /* On construit une liste de toutes les maladies (de type Object). */
      final List<Object> allMalObject = new ArrayList<>();
      allMalObject.addAll(getMaladies());
      /* On récupère tous les échantillons de la liste de maladies. */
      final List<Object> echantillons = correspondanceManager.recupereEntitesViaDAutres(allMalObject, "Echantillon");

      /* On vérifie qu'il n'y a pas de doublon d'échantillons trouvés.  */
      for(int i = 0; i < echantillons.size(); i++){
         final Echantillon echantillon1 = (Echantillon) echantillons.get(i);
         for(int j = i + 1; j < echantillons.size(); j++){
            final Echantillon echantillon2 = (Echantillon) echantillons.get(j);
            assertFalse(echantillon1.equals(echantillon2));
         }
      }

      /* Pour chaque maladie de la BDD, on vérifie que ses échantillons sont 
       * dans la liste des échantillons correspondants récupérée. */
      for(final Iterator<Maladie> itMaladies = getMaladies().iterator(); itMaladies.hasNext();){
         final Maladie maladie = itMaladies.next();
         if(maladie != null){
            /* On récupère les prélèvements de la maladie. */
            final Set<Prelevement> prelevements = maladieManager.getPrelevementsManager(maladie);
            if(prelevements != null){
               for(final Iterator<Prelevement> itPrelevements = prelevements.iterator(); itPrelevements.hasNext();){
                  /* On récupère les échantillons du prélèvement. */
                  final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(itPrelevements.next());
                  if(echans != null){
                     /* On vérifie que les échantillons sont dans la 
                      * liste des échantillons récupérée. */
                     for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
                        final Echantillon echantillon = itEchantillons.next();
                        assertTrue(echantillons.contains(echantillon));
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des échantillons correspondants aux prélèvements
    * de la BDD.
    */
   @Test
   public void testRecupereEchantillonsDepuisPrelevements(){
      /* On construit une liste de tous les prélèvements (de type Object). */
      final List<Object> allPreObject = new ArrayList<>();
      allPreObject.addAll(getPrelevements());
      /* On récupère tous les échantillons de la liste de prélévements. */
      final List<Object> echantillons = correspondanceManager.recupereEntitesViaDAutres(allPreObject, "Echantillon");

      /* On vérifie qu'il n'y a pas de doublon d'échantillons trouvés.  */
      for(int i = 0; i < echantillons.size(); i++){
         final Echantillon echantillon1 = (Echantillon) echantillons.get(i);
         for(int j = i + 1; j < echantillons.size(); j++){
            final Echantillon echantillon2 = (Echantillon) echantillons.get(j);
            assertFalse(echantillon1.equals(echantillon2));
         }
      }

      /*
       * Pour chaque prélèvement de la BDD, on vérifie que ses échantillons
       * sont dans la liste des échantillons correspondants récupérée.
       */
      for(final Iterator<Prelevement> itPrelevements = getPrelevements().iterator(); itPrelevements.hasNext();){
         final Prelevement prelevement = itPrelevements.next();
         if(prelevement != null){
            /* On récupère les échantillons du prélèvement. */
            final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(prelevement);
            if(echans != null){
               /* On vérifie que les échantillons sont dans la liste des
                * échantillons récupérée. */
               for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
                  final Echantillon echantillon = itEchantillons.next();
                  assertTrue(echantillons.contains(echantillon));
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des échantillons correspondants aux produits
    * dérivés de la BDD.
    */
   @Test
   public void testRecupereEchantillonsDepuisProdDerives(){
      /* On construit une liste de tous les dérivés (de type Object). */
      final List<Object> allDerObject = new ArrayList<>();
      allDerObject.addAll(getDerives());
      /* On récupère tous les échantillons de la liste de prélévements. */
      final List<Object> echantillons = correspondanceManager.recupereEntitesViaDAutres(allDerObject, "Echantillon");

      /* On vérifie qu'il n'y a pas de doublon d'échantillons trouvés.  */
      for(int i = 0; i < echantillons.size(); i++){
         final Echantillon echantillon1 = (Echantillon) echantillons.get(i);
         for(int j = i + 1; j < echantillons.size(); j++){
            final Echantillon echantillon2 = (Echantillon) echantillons.get(j);
            assertFalse(echantillon1.equals(echantillon2));
         }
      }

      /*
       * Pour chaque dérivé de la BDD, on vérifie que ses échantillons
       * sont dans la liste des échantillons correspondants récupérée.
       */
      for(final Iterator<ProdDerive> itDerives = getDerives().iterator(); itDerives.hasNext();){
         final ProdDerive derive = itDerives.next();
         if(derive != null){
            /* On récupère le parent du produit dérivé. */
            final Transformation transformation = derive.getTransformation();
            if(transformation.getEntite().getNom().equals("Echantillon")){
               final Echantillon echantillon = echantillonManager.findByIdManager(transformation.getObjetId());
               if(echantillon != null){
                  /* On vérifie que l'échantillon est dans la liste des
                   * échantillons récupérée. */
                  assertTrue(echantillons.contains(echantillon));
               }
            }
         }
      }
   }

   /**
    * Test de la récupération des produits dérivés correspondants aux 
    * patients de la BDD.
    */
   @Test
   public void testRecupereProdDerivesDepuisPatients(){
      /* On construit une liste de tous les patients (de type Object). */
      final List<Object> allPatObject = new ArrayList<>();
      allPatObject.addAll(getPatients());
      /* On récupère tous les dérivés de la liste de patients. */
      final List<Object> prodDerives = correspondanceManager.recupereEntitesViaDAutres(allPatObject, "ProdDerive");

      /* On vérifie qu'il n'y a pas de doublon de dérivés trouvés.  */
      for(int i = 0; i < prodDerives.size(); i++){
         final ProdDerive prodDerive1 = (ProdDerive) prodDerives.get(i);
         for(int j = i + 1; j < prodDerives.size(); j++){
            final ProdDerive prodDerive2 = (ProdDerive) prodDerives.get(j);
            assertFalse(prodDerive1.equals(prodDerive2));
         }
      }

      /*
       * On crée une liste de derives qu'on va comparer avec la liste trouvée.
       */
      final ArrayList<ProdDerive> prods = new ArrayList<>();
      /* On recherche les maladies des patients. */
      for(final Iterator<Patient> itPatients = getPatients().iterator(); itPatients.hasNext();){
         final Patient patient = itPatients.next();
         final Set<Maladie> setMaladies = maladieManager.getMaladiesManager(patient);
         /* On recherche les prelevements des maladies. */
         for(final Iterator<Maladie> itMaladies = setMaladies.iterator(); itMaladies.hasNext();){
            final Maladie maladie = itMaladies.next();
            final Set<Prelevement> setPrelevements = maladieManager.getPrelevementsManager(maladie);
            for(final Iterator<Prelevement> itPrelevements = setPrelevements.iterator(); itPrelevements.hasNext();){
               final Prelevement prelevement = itPrelevements.next();
               /*
                * On ajoute les dérivés directs de chaque prélèvement s'ils
                * ne sont pas dans la liste 'prods'.
                */
               for(final Iterator<ProdDerive> itProdDerives =
                  prelevementManager.getProdDerivesManager(prelevement).iterator(); itProdDerives.hasNext();){
                  final ProdDerive temp = itProdDerives.next();
                  if(!prods.contains(temp)){
                     prods.add(temp);
                  }
               }
               // On récupère les échantillons de chaque prélèvement pour
               // récupérer leurs produits dérivés.
               final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(prelevement);
               if(echans != null){
                  for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
                     final Echantillon echantillon = itEchantillons.next();
                     /*
                      * On ajoute les dérivés directs de chaque
                      * échantillon s'ils ne sont pas dans la liste
                      * 'prods'.
                      */
                     for(final Iterator<ProdDerive> itProdDerives =
                        echantillonManager.getProdDerivesManager(echantillon).iterator(); itProdDerives.hasNext();){
                        final ProdDerive temp = itProdDerives.next();
                        if(!prods.contains(temp)){
                           prods.add(temp);
                        }
                     }
                  }
               }
            }
         }
      }
      // On recupere tous les descendants des produits derives recuperes
      for(int i = 0; i < prods.size(); i++){
         final ProdDerive prod = prods.get(i);
         final List<ProdDerive> enfants = prodDeriveManager.getProdDerivesManager(prod);
         if(enfants != null){
            for(final Iterator<ProdDerive> itEnfants = enfants.iterator(); itEnfants.hasNext();){
               final ProdDerive temp = itEnfants.next();
               if(!prods.contains(temp)){
                  prods.add(temp);
               }
            }
         }
      }

      // On compare les deux listes
      for(final Iterator<ProdDerive> itProds = prods.iterator(); itProds.hasNext();){
         final ProdDerive prod = itProds.next();
         assertTrue(prodDerives.contains(prod));
      }
      for(final Iterator<Object> itProds = prodDerives.iterator(); itProds.hasNext();){
         final ProdDerive prod = (ProdDerive) itProds.next();
         assertTrue(prods.contains(prod));
      }
   }

   /**
    * Test de la récupération des produits dérivés correspondants aux 
    * patients de la BDD.
    */
   @Test
   public void testRecupereProdDerivesDepuisMaladies(){
      /* On construit une liste de toutes les maladies (de type Object). */
      final List<Object> allMalObject = new ArrayList<>();
      allMalObject.addAll(getMaladies());
      /* On récupère tous les dérivés de la liste de maladies. */
      final List<Object> prodDerives = correspondanceManager.recupereEntitesViaDAutres(allMalObject, "ProdDerive");

      /* On vérifie qu'il n'y a pas de doublon de dérivés trouvés.  */
      for(int i = 0; i < prodDerives.size(); i++){
         final ProdDerive prodDerive1 = (ProdDerive) prodDerives.get(i);
         for(int j = i + 1; j < prodDerives.size(); j++){
            final ProdDerive prodDerive2 = (ProdDerive) prodDerives.get(j);
            assertFalse(prodDerive1.equals(prodDerive2));
         }
      }

      /*
       * On crée une liste de dérivés qu'on va comparer avec la liste trouvée.
       */
      final ArrayList<ProdDerive> prods = new ArrayList<>();
      /* On recherche les prelevements des maladies. */
      for(final Iterator<Maladie> itMaladies = getMaladies().iterator(); itMaladies.hasNext();){
         final Maladie maladie = itMaladies.next();
         final Set<Prelevement> setPrelevements = maladieManager.getPrelevementsManager(maladie);
         for(final Iterator<Prelevement> itPrelevements = setPrelevements.iterator(); itPrelevements.hasNext();){
            final Prelevement prelevement = itPrelevements.next();
            /*
             * On ajoute les dérivés directs de chaque prélèvement s'ils ne
             * sont pas dans la liste 'prods'.
             */
            for(final Iterator<ProdDerive> itProdDerives =
               prelevementManager.getProdDerivesManager(prelevement).iterator(); itProdDerives.hasNext();){
               final ProdDerive temp = itProdDerives.next();
               if(!prods.contains(temp)){
                  prods.add(temp);
               }
            }
            // On récupère les échantillons de chaque prélèvement pour
            // récupérer leurs produits dérivés.
            final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(prelevement);
            if(echans != null){
               for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
                  final Echantillon echantillon = itEchantillons.next();
                  /*
                   * On ajoute les dérivés directs de chaque échantillon
                   * s'ils ne sont pas dans la liste 'prods'.
                   */
                  for(final Iterator<ProdDerive> itProdDerives =
                     echantillonManager.getProdDerivesManager(echantillon).iterator(); itProdDerives.hasNext();){
                     final ProdDerive temp = itProdDerives.next();
                     if(!prods.contains(temp)){
                        prods.add(temp);
                     }
                  }
               }
            }
         }
      }
      // On recupere tous les descendants des produits derives recuperes
      for(int i = 0; i < prods.size(); i++){
         final ProdDerive prod = prods.get(i);
         final List<ProdDerive> enfants = prodDeriveManager.getProdDerivesManager(prod);
         if(enfants != null){
            for(final Iterator<ProdDerive> itEnfants = enfants.iterator(); itEnfants.hasNext();){
               final ProdDerive temp = itEnfants.next();
               if(!prods.contains(temp)){
                  prods.add(temp);
               }
            }
         }
      }

      // On compare les deux listes
      for(final Iterator<ProdDerive> itProds = prods.iterator(); itProds.hasNext();){
         final ProdDerive prod = itProds.next();
         assertTrue(prodDerives.contains(prod));
      }
      for(final Iterator<Object> itProds = prodDerives.iterator(); itProds.hasNext();){
         final ProdDerive prod = (ProdDerive) itProds.next();
         assertTrue(prods.contains(prod));
      }
   }

   /**
    * Test de la récupération des produits dérivés correspondants aux
    * prélèvements de la BDD.
    */
   @Test
   public void testRecupereProdDerivesDepuisPrelevements(){
      /* On construit une liste de tous les prélèvements (de type Object). */
      final List<Object> allPreObject = new ArrayList<>();
      allPreObject.addAll(getPrelevements());
      /* On récupère tous les dérivés de la liste de prélèvements. */
      final List<Object> prodDerives = correspondanceManager.recupereEntitesViaDAutres(allPreObject, "ProdDerive");

      /* On vérifie qu'il n'y a pas de doublon de dérivés trouvés.  */
      for(int i = 0; i < prodDerives.size(); i++){
         final ProdDerive prodDerive1 = (ProdDerive) prodDerives.get(i);
         for(int j = i + 1; j < prodDerives.size(); j++){
            final ProdDerive prodDerive2 = (ProdDerive) prodDerives.get(j);
            assertFalse(prodDerive1.equals(prodDerive2));
         }
      }

      /* On crée une liste de derives qu'on va comparer avec la 
       * liste trouvée. */
      final ArrayList<ProdDerive> prods = new ArrayList<>();
      for(final Iterator<Prelevement> itPrelevements = getPrelevements().iterator(); itPrelevements.hasNext();){
         final Prelevement prelevement = itPrelevements.next();
         /* On ajoute les dérivés direct de chaque prélèvement s'ils ne 
          * sont pas dans la liste 'prods'. */
         for(final Iterator<ProdDerive> itProdDerives =
            prelevementManager.getProdDerivesManager(prelevement).iterator(); itProdDerives.hasNext();){
            final ProdDerive temp = itProdDerives.next();
            if(!prods.contains(temp)){
               prods.add(temp);
            }
         }
         // On récupère les échantillons de chaque prélèvement pour récupérer
         // leurs produits dérivés.
         final Set<Echantillon> echans = prelevementManager.getEchantillonsManager(prelevement);
         if(echans != null){
            for(final Iterator<Echantillon> itEchantillons = echans.iterator(); itEchantillons.hasNext();){
               final Echantillon echantillon = itEchantillons.next();
               /* On ajoute les dérivés direct de chaque échantillon s'ils 
                * ne sont pas dans la liste 'prods'. */
               for(final Iterator<ProdDerive> itProdDerives =
                  echantillonManager.getProdDerivesManager(echantillon).iterator(); itProdDerives.hasNext();){
                  final ProdDerive temp = itProdDerives.next();
                  if(!prods.contains(temp)){
                     prods.add(temp);
                  }
               }
            }
         }
      }
      //On recupere tous les descendants des produits derives recuperes
      for(int i = 0; i < prods.size(); i++){
         final ProdDerive prod = prods.get(i);
         final List<ProdDerive> enfants = prodDeriveManager.getProdDerivesManager(prod);
         if(enfants != null){
            for(final Iterator<ProdDerive> itEnfants = enfants.iterator(); itEnfants.hasNext();){
               final ProdDerive temp = itEnfants.next();
               if(!prods.contains(temp)){
                  prods.add(temp);
               }
            }
         }
      }

      //On compare les deux listes
      for(final Iterator<ProdDerive> itProds = prods.iterator(); itProds.hasNext();){
         final ProdDerive prod = itProds.next();
         assertTrue(prodDerives.contains(prod));
      }
      for(final Iterator<Object> itProds = prodDerives.iterator(); itProds.hasNext();){
         final ProdDerive prod = (ProdDerive) itProds.next();
         assertTrue(prods.contains(prod));
      }
   }

   /**
    * Test de la récupération des produits dérivés correspondants aux
    * échantillons de la BDD.
    */
   @Test
   public void testRecupereProdDerivesDepuisEchantillons(){
      /* On construit une liste de tous les échantillons (de type Object). */
      final List<Object> allEchObject = new ArrayList<>();
      allEchObject.addAll(getEchantillons());
      /* On récupère tous les dérivés de la liste d'échantillons. */
      final List<Object> prodDerives = correspondanceManager.recupereEntitesViaDAutres(allEchObject, "ProdDerive");

      /* On vérifie qu'il n'y a pas de doublon de dérivés trouvés. */
      for(int i = 0; i < prodDerives.size(); i++){
         final ProdDerive prodDerive1 = (ProdDerive) prodDerives.get(i);
         for(int j = i + 1; j < prodDerives.size(); j++){
            final ProdDerive prodDerive2 = (ProdDerive) prodDerives.get(j);
            assertFalse(prodDerive1.equals(prodDerive2));
         }
      }

      /*
       * On crée une liste de derives qu'on va comparer avec la liste trouvée.
       */
      final ArrayList<ProdDerive> prods = new ArrayList<>();
      /* On cherche les produits dérivés directs de chaque échantillon. */
      for(final Iterator<Echantillon> itEchantillons = getEchantillons().iterator(); itEchantillons.hasNext();){
         final Echantillon echantillon = itEchantillons.next();
         /*
          * On ajoute les dérivés direct de chaque échantillon s'ils ne sont
          * pas dans la liste 'prods'.
          */
         for(final Iterator<ProdDerive> itProdDerives =
            echantillonManager.getProdDerivesManager(echantillon).iterator(); itProdDerives.hasNext();){
            final ProdDerive temp = itProdDerives.next();
            if(!prods.contains(temp)){
               prods.add(temp);
            }
         }
      }
      // On recupere tous les descendants des produits derives recuperes
      for(int i = 0; i < prods.size(); i++){
         final ProdDerive prod = prods.get(i);
         final List<ProdDerive> enfants = prodDeriveManager.getProdDerivesManager(prod);
         if(enfants != null){
            for(final Iterator<ProdDerive> itEnfants = enfants.iterator(); itEnfants.hasNext();){
               final ProdDerive temp = itEnfants.next();
               if(!prods.contains(temp)){
                  prods.add(temp);
               }
            }
         }
      }

      //On compare les deux listes
      for(final Iterator<ProdDerive> itProds = prods.iterator(); itProds.hasNext();){
         final ProdDerive prod = itProds.next();
         assertTrue(prodDerives.contains(prod));
      }
      for(final Iterator<Object> itProds = prodDerives.iterator(); itProds.hasNext();){
         final ProdDerive prod = (ProdDerive) itProds.next();
         assertTrue(prods.contains(prod));
      }
   }

   /*@Test
   public void testPerso() {
   	// On récupère la recherche
   	List<Banque> banques = banqueManager.findAllObjectsManager();
   	Recherche recherche = rechercheManager.findByIdManager(5);
   	Affichage affichage = recherche.getAffichage();
   	affichage.setResultats(resultatManager
   			.findByAffichageManager(affichage));
   	List<Object> objets = traitementRequeteManager
   			.traitementRequeteManager(recherche.getRequete(), banques);
   
   	Iterator it = null;
   	it = objets.iterator();
   	while (it.hasNext()) {
   		System.out.println(it.next());
   	}
   	System.out.println("FIN AFFICHAGE OBJETS DE REQUETE");
   	objets = correspondanceManager.recupereEntitesPourAffichageManager(
   			objets, affichage);
   
   	it = objets.iterator();
   	while (it.hasNext()) {
   		System.out.println(it.next());
   	}
   	System.out.println("FIN AFFICHAGE OBJETS LE PLUS HAUTS POSSIBLE");
   
   	it = objets.iterator();
   	while (it.hasNext()) {
   		Object objet = it.next();
   		// On itère la liste des résultats de l'affichage.
   		Iterator<Resultat> itRes = affichage.getResultats().iterator();
   		while (itRes.hasNext()) {
   			Resultat res = itRes.next();
   			// On récupère l'entité depuis le champ du Resultat.
   			Entite entite = null;
   			if (res != null) {
   				if (res.getChamp() != null) {
   					if (res.getChamp().getChampEntite() != null) {
   						entite = res.getChamp().getChampEntite()
   								.getEntite();
   					} else if (res.getChamp().getChampAnnotation()
   							!= null) {
   						entite = res.getChamp().getChampAnnotation()
   								.getTableAnnotation().getEntite();
   					}
   				}
   			}
   
   			if (entite != null) {
   				List<Object> lObj = new ArrayList<Object>();
   				lObj.add(objet);
   				Object recup = null;
   				System.out.println("classe de l'objet : "
   						+ objet.getClass().getSimpleName());
   				System.out.println("nom de l'entité : " + entite.getNom());
   				if (objet.getClass().getSimpleName()
   						.equals(entite.getNom())) {
   					recup = objet;
   				} else {
   					List<Object> recups = correspondanceManager
   							.recupereEntitesViaDAutres(lObj, entite
   									.getNom());
   
   				}
   
   			}
   		}
   
   	}
   }*/
}
