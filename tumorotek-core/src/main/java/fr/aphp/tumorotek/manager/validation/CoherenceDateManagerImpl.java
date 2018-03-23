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
package fr.aphp.tumorotek.manager.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.LaboInterDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInterComparator;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.utils.Utils;

public class CoherenceDateManagerImpl implements CoherenceDateManager
{

   private MaladieDao maladieDao;
   private PrelevementDao prelevementDao;
   private LaboInterDao laboInterDao;
   private EchantillonDao echantillonDao;
   private TransformationManager transformationManager;
   private EntiteManager entiteManager;

   public void setMaladieDao(final MaladieDao mDao){
      this.maladieDao = mDao;
   }

   public void setPrelevementDao(final PrelevementDao pDao){
      this.prelevementDao = pDao;
   }

   public void setLaboInterDao(final LaboInterDao lDao){
      this.laboInterDao = lDao;
   }

   public void setEchantillonDao(final EchantillonDao eDao){
      this.echantillonDao = eDao;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setTransformationManager(final TransformationManager tManager){
      this.transformationManager = tManager;
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.CoherenceDateManager#findPostRefDateForPatientManager(fr.aphp.tumorotek.model.coeur.patient.Patient)
    */
   @Override
   public Object[] findPostRefDateForPatientManager(final Patient patient){
      final Object[] dateAndCode = new Object[] {null, null};
      Object ref = null;
      String code = null;
      Object previous = null;
      String codePrevious = null;
      Object[] dateAndCodeForEchan;

      // boucle utilisée lors update uniquement
      if(patient.getPatientId() != null){

         // trouve les maladies
         final List<Maladie> maladies = new ArrayList<>(maladieDao.findByPatient(patient));
         for(int j = 0; j < maladies.size(); j++){
            if(maladies.get(j).getDateDebut() != null){
               ref = maladies.get(j).getDateDebut();
               code = "date.validation.supDateDebutUneMaladie";
            }else if(maladies.get(j).getDateDiagnostic() != null){
               ref = maladies.get(j).getDateDiagnostic();
               code = "date.validation.supDateDiagUneMaladie";
            }
            if(ref != null){
               if(previous != null){
                  if(((Date) ref).before((Date) previous)){
                     previous = ref;
                     codePrevious = code;
                  }
               }else{
                  previous = ref;
                  codePrevious = code;
               }
            }
         }

         // trouve les prelevements
         final List<Prelevement> prels = prelevementDao.findByPatient(patient);

         // trouve la date de reference
         for(int i = 0; i < prels.size(); i++){
            if(!prels.get(i).getArchive()){
               if(prels.get(i).getDatePrelevement() != null){
                  ref = prels.get(i).getDatePrelevement();
                  code = "date.validation" + ".supDateUnPrelevement";
               }else if(prels.get(i).getDateDepart() != null){
                  ref = prels.get(i).getDateDepart();
                  code = "date.validation" + ".supDateDepartUnPrelevement";
               }else{
                  final List<LaboInter> list = new ArrayList<>(laboInterDao.findByPrelevementWithOrder(prels.get(i)));

                  if(list != null){
                     int ordre;
                     // utilisation de precedent 
                     // car aucune certitude 
                     // sur l'ordre
                     // des labos dans le set
                     int precedent = list.size() + 1;
                     LaboInter labo = null;
                     for(int j = 0; j < list.size(); j++){
                        labo = list.get(j);
                        ordre = labo.getOrdre();
                        if(ordre < precedent){
                           if(labo.getDateArrivee() != null){
                              ref = labo.getDateArrivee();
                              precedent = ordre;
                              code = "date.validation" + ".supDateDateArriveeUnLaboInter";
                           }else if(labo.getDateDepart() != null){
                              ref = labo.getDateDepart();
                              precedent = ordre;
                              code = "date.validation" + ".supDateDateDepartUnLaboInter";
                           }
                        }
                     }
                  }
                  if(ref == null){
                     if(prels.get(i).getDateArrivee() != null){
                        ref = prels.get(i).getDateArrivee();
                        code = "date.validation" + ".supDateDepartUnPrelevement";
                     }
                  }
                  if(ref == null){
                     // trouve parmi les echantillons
                     dateAndCodeForEchan = findPostRefDateInEchantillonsManager(prels.get(i));
                     ref = dateAndCodeForEchan[0];
                     if(ref != null){
                        code = (String) dateAndCodeForEchan[1];
                     }
                  }
               }

               if(ref != null){
                  if(previous != null){
                     if(ValidationUtilities.checkWithDate(ref, null, previous, null, null, null, null, true)){
                        previous = ref;
                        codePrevious = code;
                     }
                  }else{
                     previous = ref;
                     codePrevious = code;
                  }
               }
            }
         }
      }
      if(previous != null){
         dateAndCode[0] = previous;
         dateAndCode[1] = codePrevious;
      }else{
         dateAndCode[0] = Utils.getCurrentSystemDate();
         dateAndCode[1] = "date.validation.supDateActuelle";
      }

      return dateAndCode;
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.CoherenceDateManager#findPostRefDateInEchantillonsManager(fr.aphp.tumorotek.model.coeur.prelevement.Prelevement)
    */
   @Override
   public Object[] findPostRefDateInEchantillonsManager(final Prelevement prelevement){
      final Object[] dateAndCode = new Object[] {null, null};
      Object ref = null;
      String code = null;
      Object previous = null;
      String codePrevious = null;
      Object[] dateAndCodeForEchan;

      // trouve les echantillons
      final List<Echantillon> echans = new ArrayList<>();
      if(prelevement.getPrelevementId() != null){
         echans.addAll(echantillonDao.findByPrelevement(prelevement));
      }else{
         echans.addAll(prelevement.getEchantillons());
      }

      // trouve la date de reference
      for(int i = 0; i < echans.size(); i++){
         if(!echans.get(i).getArchive()){
            if(echans.get(i).getDateStock() != null){
               ref = echans.get(i).getDateStock();
               code = "date.validation.supDateStockEchanEnfant";

            }else{
               dateAndCodeForEchan = findPostRefDateInDerivesManager(echans.get(i));
               ref = dateAndCodeForEchan[0];
               if(ref != null){
                  code = (String) dateAndCodeForEchan[1];
               }
            }

            if(ref != null){
               if(previous != null){
                  if(ValidationUtilities.checkWithDate(ref, null, previous, null, null, null, null, true)){
                     previous = ref;
                     codePrevious = code;
                  }
               }else{
                  previous = ref;
                  codePrevious = code;
               }
            }
         }
      }

      // trouve parmi les derives
      final Object[] dateAndCodeForDerives = findPostRefDateInDerivesManager(prelevement);
      ref = dateAndCodeForDerives[0];
      code = (String) dateAndCodeForDerives[1];

      if(ref != null){
         if(previous != null){
            if(ValidationUtilities.checkWithDate(ref, null, previous, null, null, null, null, true)){
               previous = ref;
               codePrevious = code;
            }
         }else{
            previous = ref;
            codePrevious = code;
         }
      }

      if(previous != null){
         dateAndCode[0] = previous;
         dateAndCode[1] = codePrevious;
      }else{
         dateAndCode[0] = Utils.getCurrentSystemDate();
         dateAndCode[1] = "date.validation.supDateActuelle";
      }

      return dateAndCode;
   }

   /**
    * Parcoure de manière récursive les labos inter 
    * du plus récent au plus ancien afin de trouver la première référence 
    * de date antérieure parmi eux.
    * @param echantillon
    * @return date référence de date.
    */
   private Calendar findLaboInterReferenceDate(final Prelevement prelevement){
      Calendar ref = null;
      if(prelevement.getLaboInters() != null){
         final List<LaboInter> list = new ArrayList<>();
         if(prelevement.getPrelevementId() != null){
            list.addAll(laboInterDao.findByPrelevementWithOrder(prelevement));
         }else{
            list.addAll(prelevement.getLaboInters());
         }

         int ordre;
         // utilisation de previous car aucune certitude sur l'ordre
         // des labos dans le set
         int previous = 0;
         LaboInter labo = null;
         for(int i = (list.size() - 1); i >= 0; i--){
            labo = list.get(i);
            ordre = labo.getOrdre();
            if(ordre > previous){
               if(labo.getDateDepart() != null){
                  ref = labo.getDateDepart();
                  previous = ordre;
               }else if(labo.getDateArrivee() != null){
                  ref = labo.getDateArrivee();
                  previous = ordre;
               }
            }
         }
      }

      return ref;
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.CoherenceDateManager#findAntRefDateInPrelevementManager(fr.aphp.tumorotek.model.coeur.prelevement.Prelevement, boolean)
    */
   @Override
   public Object[] findAntRefDateInPrelevementManager(final Prelevement prelevement, final boolean skipToDatePrel){
      final Object[] dateAndCode = new Object[] {null, null};
      Object ref = null;
      String code = null;
      if(!skipToDatePrel){
         if(prelevement.getDateArrivee() != null){
            ref = prelevement.getDateArrivee();
            code = "date.validation.infDateArriveePrelevement";
         }else{
            final Object laboInterRef = findLaboInterReferenceDate(prelevement);
            if(laboInterRef != null){
               ref = laboInterRef;
               code = "date.validation.infLastDateLaboInter";
            }else if(prelevement.getDateDepart() != null){
               ref = prelevement.getDateDepart().getTime();
               code = "date.validation.infDateDepartPrelevement";
            }
         }
      }
      if(skipToDatePrel || ref == null){
         if(prelevement.getDatePrelevement() != null){
            ref = prelevement.getDatePrelevement();
            code = "date.validation.infDatePrelevement";
         }else if(prelevement.getMaladie() != null && prelevement.getMaladie().getPatient().getDateNaissance() != null){
            ref = prelevement.getMaladie().getPatient().getDateNaissance();
            code = "date.validation.infDateNaissance";
         }
      }
      dateAndCode[0] = ref;
      dateAndCode[1] = code;

      return dateAndCode;
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.CoherenceDateManager#findPostRefDateInLabosManager(java.util.List)
    */
   
   @Override
   public Object[] findPostRefDateInLabosManager(final List<LaboInter> labos){
      final Object[] dateAndCode = new Object[] {null, null};
      Object ref = null;
      String code = null;

      Collections.sort(labos, new LaboInterComparator());

      for(int i = 0; i < labos.size(); i++){
         if(labos.get(i).getDateArrivee() != null){
            ref = labos.get(i).getDateArrivee();
            code = "date.validation.supDateArriveeUnLaboInter";
            break;
         }else if(labos.get(i).getDateDepart() != null){
            ref = labos.get(i).getDateDepart();
            code = "date.validation.supDateDepartUnLaboInter";
            break;
         }
      }
      dateAndCode[0] = ref;
      dateAndCode[1] = code;

      return dateAndCode;
   }

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.CoherenceDateManager#findAntRefDateForDeriveManager(fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive)
    */
   @Override
   public Object[] findAntRefDateForDeriveManager(final ProdDerive derive){
      final Object[] dateAndCode = new Object[] {null, null};
      Object ref = null;
      String code = null;
      //trouve le parent
      if(derive.getTransformation() != null){
         final Object parent = entiteManager.findObjectByEntiteAndIdManager(derive.getTransformation().getEntite(),
            derive.getTransformation().getObjetId());
         if(parent != null){
            if(parent.getClass().getSimpleName().equals("ProdDerive")){
               if(((ProdDerive) parent).getDateStock() != null){
                  ref = ((ProdDerive) parent).getDateStock();
                  code = "date.validation.infDateStockDeriveParent";
               }else if(((ProdDerive) parent).getDateTransformation() != null){
                  ref = ((ProdDerive) parent).getDateTransformation();
                  code = "date.validation.infDateTransfoDeriveParent";
               }else{
                  return findAntRefDateForDeriveManager((ProdDerive) parent);
               }
            }else if(parent.getClass().getSimpleName().equals("Echantillon")){
               if(((Echantillon) parent).getDateStock() != null){
                  ref = ((Echantillon) parent).getDateStock();
                  code = "date.validation.infDateStockEchan";
               }else{
                  final Prelevement prel = echantillonDao.mergeObject((Echantillon) parent).getPrelevement();
                  if(prel != null){
                     return findAntRefDateInPrelevementManager(prel, true);
                  }
               }
            }else{
               return findAntRefDateInPrelevementManager((Prelevement) parent, true);
            }
         }
      }
      dateAndCode[0] = ref;
      dateAndCode[1] = code;

      return dateAndCode;
   }

   @Override
   public Object[] findPostRefDateInDerivesManager(final Object parent){
      final Object[] dateAndCode = new Object[] {null, null};
      Calendar ref = null;
      String code = null;
      Calendar previous = null;
      String codePrevious = null;
      // trouve les derives potentiels, aucun si update
      final List<ProdDerive> derives = transformationManager.findAllDeriveFromParentManager(parent);

      // trouve la date de reference
      for(int i = 0; i < derives.size(); i++){
         if(!derives.get(i).getArchive()){
            if(derives.get(i).getDateTransformation() != null){
               ref = derives.get(i).getDateTransformation();
               code = "date.validation.supDateTransfoEnfant";

            }else if(derives.get(i).getDateStock() != null){
               ref = derives.get(i).getDateStock();
               code = "date.validation.supDateStockDeriveEnfant";
            }

            if(ref != null){
               if(previous != null){
                  if(ValidationUtilities.checkWithDate(ref, null, previous, null, null, null, null, true)){
                     previous = ref;
                     codePrevious = code;
                  }
               }else{
                  previous = ref;
                  codePrevious = code;
               }
            }
         }
      }

      if(previous != null){
         dateAndCode[0] = previous;
         dateAndCode[1] = codePrevious;
      }else{
         dateAndCode[0] = Utils.getCurrentSystemDate();
         dateAndCode[1] = "date.validation.supDateActuelle";
      }

      return dateAndCode;
   }
}
