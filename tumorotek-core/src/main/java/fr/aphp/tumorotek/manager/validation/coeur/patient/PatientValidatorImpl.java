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
package fr.aphp.tumorotek.manager.validation.coeur.patient;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import fr.aphp.tumorotek.manager.coeur.patient.PatientValidator;
import fr.aphp.tumorotek.manager.validation.CoherenceDateManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Validator pour le bean domaine Patient.<br>
 * Classe creee le 30/10/09<br>
 * <br>
 * Regles de validation:<br>
 *  - le champ nip doit null ou non vide et valide litteralement et
 * 		de taille inferieure à 20<br>
 * 	- le champ nom doit etre non vide, non null, et valide litteralement et
 * 		de taille inferieure à 50<br>
 *  - le champ nom de naissance doit etre non vide et valide
 * 		litteralement et de taille inferieure à 50<br>
 * 	- le champ prenom doit etre non vide, non null, et valide litteralement et
 * 		de taille inferieure à 50<br>
 * 	- le champ sexe doit etre null ou egal à F, M ou Ind.
 * 	- le champ ville de naissance doit etre null ou non vide et
 * 		valide litteralement et de taille inferieure à 100<br>
 * 	- le champ pays de naissance doit etre null ou non vide et
 * 		valide litteralement et de taille inferieure à 100<br>
 * 	- le champ etat patient doit etre non null ou egal à V, D  ou Inconnu<br>
 * 	- le champ date etat doit etre null, ou supérieure à la date de naissance
 * 		et inférieure ou égale à la date actuelle<br>
 *  - la date de naissance doit être null ou
 *  		inférieure ou égale à la date actuelle <br>
 *  - la date de l'etat doit être null ou
 *  		supérieure ou égale à la date de naissance si non nulle,
 *  		inférieure ou égale à la date actuelle <br>
 *  - la date de deces doit être null ou
 *  		supérieure ou égale à la date de naissance si non nulle,
 *  		inférieure ou égale à la date actuelle
 *  	et cohérente avec l'état décédé<br>
 *
 * @since 2.0.9 prenom, sexe, date naissance NOT NULL
 *
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.9
 */
public class PatientValidatorImpl implements PatientValidator
{

   //	private PrelevementManager prelevementManager;
   //	private PrelevementValidator prelevementValidator;
   //	private MaladieManager maladieManager;

   //	public void setPrelevementManager(PrelevementManager pManager) {
   //		this.prelevementManager = pManager;
   //	}
   //
   //	public void setPrelevementValidator(PrelevementValidator pValidator) {
   //		this.prelevementValidator = pValidator;
   //	}
   //
   //	public void setMaladieManager(MaladieManager mManager) {
   //		this.maladieManager = mManager;
   //	}

   private CoherenceDateManager coherenceDateManager;

   public void setCoherenceDateManager(final CoherenceDateManager cManager){
      this.coherenceDateManager = cManager;
   }

   @Override
   public boolean supports(final Class<?> clazz){
      return Patient.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){

      final Patient patient = (Patient) obj;

      //nip valide
      if(patient.getNip() != null){
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "nip", "patient.nip.empty");
         if(!patient.getNip().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("nip", "patient.nip.illegal");
         }
         if(patient.getNip().length() > 20){
            errs.rejectValue("nip", "patient.nip.tooLong");
         }
      }

      //Nom non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "nom", "patient.nom.empty");
      //nom valide
      if(patient.getNom() != null){
         if(!patient.getNom().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("nom", "patient.nom.illegal");
         }
         if(patient.getNom().length() > 50){
            errs.rejectValue("nom", "patient.nom.tooLong");
         }
      }

      //nom naissance valide
      if(patient.getNomNaissance() != null){
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "nomNaissance", "patient.nomNaissance.empty");
         if(!patient.getNomNaissance().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("nomNaissance", "patient.nomNaissance.illegal");
         }
         if(patient.getNomNaissance().length() > 50){
            errs.rejectValue("nomNaissance", "patient.nomNaissance.tooLong");
         }
      }

      //prenom non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "prenom", "patient.prenom.empty");

      //prenom valide
      if(patient.getPrenom() != null){
         //			ValidationUtils
         //			.rejectIfEmptyOrWhitespace(errs, "prenom", 
         //										"patient.prenom.empty");
         if(!patient.getPrenom().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("prenom", "patient.prenom.illegal");
         }
         if(patient.getPrenom().length() > 50){
            errs.rejectValue("prenom", "patient.prenom.tooLong");
         }
      }

      //sexe non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "sexe", "patient.sexe.empty");
      //sexe valide
      if(patient.getSexe() != null){
         //			ValidationUtils
         //			.rejectIfEmptyOrWhitespace(errs, "sexe", 
         //										"patient.sexe.empty");
         if(!patient.getSexe().matches(ValidationUtilities.SEXEREGEXP)){
            errs.rejectValue("sexe", "patient.sexe.illegal");
         }
      }

      //ville naissance valide
      if(patient.getVilleNaissance() != null){
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "villeNaissance", "patient.villeNaissance.empty");
         if(!patient.getVilleNaissance().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("villeNaissance", "patient.villeNaissance.illegal");
         }
         if(patient.getVilleNaissance().length() > 100){
            errs.rejectValue("villeNaissance", "patient.villeNaissance.tooLong");
         }
      }

      //Pays naissance valide
      if(patient.getPaysNaissance() != null){
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "paysNaissance", "patient.paysNaissance.empty");
         if(!patient.getPaysNaissance().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("paysNaissance", "patient.paysNaissance.illegal");
         }
         if(patient.getPaysNaissance().length() > 100){
            errs.rejectValue("paysNaissance", "patient.paysNaissance.tooLong");
         }
      }

      //etat non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "patientEtat", "patient.patientEtat.empty");

      //etat valide
      if(patient.getPatientEtat() != null){
         if(!patient.getPatientEtat().matches(ValidationUtilities.PATIENT_ETAT_REGEXP)){
            errs.rejectValue("patientEtat", "patient.patientEtat.illegal");
         }
      }

      //valide la date de naissance
      errs.addAllErrors(checkDateNaissanceCoherence(patient));
      //valide la date de l'etat
      errs.addAllErrors(checkDateEtatCoherence(patient));
      //valide la date de deces
      errs.addAllErrors(checkDateDecesCoherence(patient));
   }

   @Override
   public Errors checkDateNaissanceCoherence(final Patient patient){

      final BindException errs = new BindException(patient, "fr.aphp.tumorotek.model.coeur.patient.Patient");

      //date de naissance cohérente
      if(patient.getDateNaissance() != null){
         // limites sup
         if(patient.getDateDeces() != null){
            if(!(patient.getDateNaissance().before(patient.getDateDeces())
               || patient.getDateNaissance().equals(patient.getDateDeces()))){
               errs.rejectValue("dateNaissance", "date.validation.supDateDeces");
            }
         }

         final Object[] dateAndCode = coherenceDateManager.findPostRefDateForPatientManager(patient);
         if(ValidationUtilities.checkWithDate(patient.getDateNaissance(), null, dateAndCode[0], null, null, null, null, false)){
            errs.rejectValue("dateNaissance", (String) dateAndCode[1]);
         }
      }else{
         errs.rejectValue("dateNaissance", "patient.dateNaissance.empty");
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de l'état. 
    * @param patient
    * @return Errors
    */
   public static Errors checkDateEtatCoherence(final Patient patient){

      final BindException errs = new BindException(patient, "fr.aphp.tumorotek.model.coeur.patient.Patient");

      if(patient.getDateEtat() != null){
         if(patient.getDateNaissance() != null){
            if(!(patient.getDateEtat().after(patient.getDateNaissance())
               || patient.getDateEtat().equals(patient.getDateNaissance()))){
               errs.rejectValue("dateEtat", "date.validation.infDateNaissance");
            }
         }

         if(!patient.getDateEtat().before(Utils.getCurrentSystemDate())){
            errs.rejectValue("dateEtat", "date.validation.supDateActuelle");
         }
      }
      return errs;
   }

   /**
    * Vérifie la cohérence de la date de deces.
    * Verifie la coherence avec l'état du patient. 
    * @param patient
    * @return Errors
    */
   public static Errors checkDateDecesCoherence(final Patient patient){

      final BindException errs = new BindException(patient, "fr.aphp.tumorotek.model.coeur.patient.Patient");

      if(patient.getDateDeces() != null){
         if(patient.getDateNaissance() != null){
            if(!(patient.getDateDeces().after(patient.getDateNaissance())
               || patient.getDateDeces().equals(patient.getDateNaissance()))){
               errs.rejectValue("dateDeces", "date.validation.infDateNaissance");
            }
         }

         if(!patient.getDateDeces().before(Utils.getCurrentSystemDate())){
            errs.rejectValue("dateDeces", "date.validation.supDateActuelle");
         }

         if(!"D".equals(patient.getPatientEtat())){
            errs.rejectValue("dateDeces", "patient.dateDeces.incoherenceAvecPatientEtat");
         }
      }
      return errs;
   }

   //	@Override
   //	public Object[] findPostRefDateForPatient(Patient patient) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Object ref = null;
   //		String code = null;
   //		Object previous = null;
   //		String codePrevious = null;
   //		Object[] dateAndCodeForEchan;
   //		
   //		// boucle utilisée lors update uniquement
   //		if (patient.getPatientId() != null) {
   //			
   //			// trouve les maladies
   //			List<Maladie> maladies = 
   //					new ArrayList<Maladie>(maladieManager
   //											.getMaladiesManager(patient));
   //			for (int j = 0; j < maladies.size(); j++) {
   //				if (maladies.get(j).getDateDebut() != null) {
   //					ref = maladies.get(j).getDateDebut();
   //					code = "date.validation.supDateDebutUneMaladie";
   //				} else if (maladies.get(j).getDateDiagnostic() != null) {
   //					ref = maladies.get(j).getDateDiagnostic();
   //					code = "date.validation.supDateDiagUneMaladie";
   //				}
   //				if (ref != null) {
   //					if (previous != null) {
   //						if (((Date) ref).before((Date) previous)) {
   //							previous = ref;
   //							codePrevious = code;
   //						}
   //					} else {
   //						previous = ref;
   //						codePrevious = code;
   //					}
   //				}
   //			}
   //					
   //			// trouve les prelevements
   //			List<Prelevement> prels = prelevementManager
   //									.findAllPrelevementsManager(patient);
   //			
   //			// trouve la date de reference
   //			for (int i = 0; i < prels.size(); i++) {
   //				if (!prels.get(i).getArchive()) {
   //					if (prels.get(i).getDatePrelevement() != null) {
   //						ref = prels.get(i).getDatePrelevement();
   //						code = "date.validation"
   //											+ ".supDateUnPrelevement";
   //					} else if (prels.get(i).getDateDepart() != null) {
   //						ref = prels.get(i).getDateDepart();
   //						code = "date.validation"
   //										+ ".supDateDepartUnPrelevement";
   //					} else {
   //						List<LaboInter> list =
   //							new ArrayList<LaboInter>(prelevementManager
   //								.getLaboIntersWithOrderManager(prels.get(i)));
   //							
   //						if (list != null) {
   //							int ordre;
   //							// utilisation de precedent 
   //							// car aucune certitude 
   //							// sur l'ordre
   //							// des labos dans le set
   //							int precedent = list.size() + 1;
   //							LaboInter labo = null;
   //							for (int j = 0; j < list.size(); j++) {
   //								labo = list.get(j);
   //								ordre = labo.getOrdre();
   //								if (ordre < precedent) {
   //									if (labo.getDateArrivee() != null) {
   //										ref = labo.getDateArrivee();
   //										precedent = ordre;
   //										code = "date.validation"
   //										+ ".supDateDateArriveeUnLaboInter";
   //									} else if (labo.
   //												getDateDepart() != null) {
   //										ref = labo.getDateDepart();
   //										precedent = ordre;
   //										code = "date.validation"
   //											+ ".supDateDateDepartUnLaboInter";
   //									}
   //								}
   //							}
   //						}
   //						if (ref == null) {
   //							if (prels.get(i).getDateArrivee() != null) {
   //								ref = prels.get(i).getDateArrivee();
   //								code = "date.validation"
   //											+ ".supDateDepartUnPrelevement";
   //							}						
   //						}
   //						if (ref == null) {
   //							// trouve parmi les echantillons
   //							dateAndCodeForEchan = prelevementValidator
   //								.findPostRefDateInEchantillons(prels.get(i));
   //							ref = dateAndCodeForEchan[0];
   //							if (ref != null) {
   //								code = (String) dateAndCodeForEchan[1];
   //							}
   //						}
   //					}
   //					
   //					if (ref != null) {
   //						if (previous != null) {
   //							if (ValidationUtilities
   //									.checkWithDate(ref, null, 
   //										previous, null, null, null, 
   //														null, true)) {
   //								previous = ref;
   //								codePrevious = code;
   //							}
   //						} else {
   //							previous = ref;
   //							codePrevious = code;
   //						}
   //					}
   //				}
   //			}
   //		}
   //		if (previous != null) {
   //			dateAndCode[0] = previous;
   //			dateAndCode[1] = codePrevious;
   //		} else {
   //			dateAndCode[0] = Utils.getCurrentSystemDate();
   //			dateAndCode[1] = "date.validation.supDateActuelle";
   //		}
   //		
   //		return dateAndCode;
   //	}
}
