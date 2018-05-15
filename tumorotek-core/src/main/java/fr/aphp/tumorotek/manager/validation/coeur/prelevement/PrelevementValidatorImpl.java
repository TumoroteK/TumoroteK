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
package fr.aphp.tumorotek.manager.validation.coeur.prelevement;

import java.util.ArrayList;

import org.apache.commons.validator.routines.FloatValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementValidator;
import fr.aphp.tumorotek.manager.validation.CoherenceDateManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Validator pour le bean domaine Prelevement.<br>
 * Classe creee le 13/10/09<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ code doit etre non vide, non null, et valide litteralement<br>
 *  - le champ conditNbr doit etre null ou >= 0<br>
 *  - le champ transportTemp doit etre null ou compris entre -1000 et +1000<br>
 *  - le champ quantite doit etre null ou >= 0<br>
 *  - le champ volume doit etre null ou >= 0<br>
 *  - le champ patientNDA doit être null ou valide litteralement<br>
 *  - le champ numeroLabo doit être null ou valide litteralement<br>
 *  - la date de prelevement doit être null ou
 *  		supérieure ou égale à la date de naissance,
 *  		inférieure ou égale à la date actuelle <br>
 *  - la date de consentement doit être null ou
 *  		supérieure ou égale à la date de naissance,
 *  		inférieure ou égale à la date actuelle <br>
 *  - la date de congélation doit être null ou
 *  	supérieure ou égale à la date de prelevement si non null,
 *  	sinon
 *  		supérieure ou égale à la date de naissance,
 *  	inférieure ou égale à la date actuelle <br>
 *  - la date de départ doit être null ou
 *  	supérieure ou égale à la date de prelevement si non null,
 *  	sinon
 *  		supérieure ou égale à la date de naissance,
 *  	inférieure à la date d'arrivée si non nulle
 *  	sinon
 *  		inférieure ou égale à la date actuelle <br>
 *
 *  - la date d'arrivée doit être null ou
 *  	supérieure ou égale à la date de départ si non null,
 *  	sinon
 *  		supérieure ou égale à la date de prelevement si non null,
 *  		sinon
 *  			supérieure ou égale à la date de naissance,
 *  	inférieure ou égale à la date actuelle <br>
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class PrelevementValidatorImpl implements PrelevementValidator
{

   //	private PrelevementManager prelevementManager;
   //	private EchantillonManager echantillonManager;
   //	private ProdDeriveValidator prodDeriveValidator;
   //	
   //	public void setPrelevementManager(PrelevementManager pManager) {
   //		this.prelevementManager = pManager;
   //	}
   //
   //	public void setEchantillonManager(EchantillonManager eM) {
   //		this.echantillonManager = eM;
   //	}
   //
   //	public void setProdDeriveValidator(ProdDeriveValidator pdValidator) {
   //		this.prodDeriveValidator = pdValidator;
   //	}

   private CoherenceDateManager coherenceDateManager;

   public void setCoherenceDateManager(final CoherenceDateManager cManager){
      this.coherenceDateManager = cManager;
   }

   @Override
   public boolean supports(final Class<?> clazz){
      return Prelevement.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){

      final Prelevement prel = (Prelevement) obj;

      //code non null
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "code", "prelevement.code.empty");
      //code valide
      if(prel.getCode() != null){
         if(!prel.getCode().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("code", "prelevement.code.illegal");
         }
         if(prel.getCode().length() > 50){
            errs.rejectValue("code", "prelevement.code.tooLong");
         }
      }

      //conditNbr >= 0
      if(prel.getConditNbr() != null){
         if(!(prel.getConditNbr() >= 0)){
            errs.rejectValue("conditNbr", "prelevement.conditNbr.negative");
         }
      }

      //transportTemp float range
      if(prel.getTransportTemp() != null && !FloatValidator.getInstance().isInRange(prel.getTransportTemp(), -1000.0, 1000.0)){
         errs.rejectValue("transportTemp", "prelevement.transportTemp.notInRange");
      }

      //quantite >= 0
      if(prel.getQuantite() != null){
         if(!(prel.getQuantite() >= 0)){
            errs.rejectValue("quantite", "prelevement.quantite.negative");
         }
      }

      //patientNDA valide
      if(prel.getPatientNda() != null){
         //nda non vide
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "patientNda", "prelevement.patientNda.empty");
         if(!prel.getPatientNda().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("patientNda", "prelevement.patientNda.illegal");
         }
         if(prel.getPatientNda().length() > 20){
            errs.rejectValue("patientNda", "prelevement.patientNda.tooLong");
         }
      }

      //numeroLabo valide
      if(prel.getNumeroLabo() != null){
         //numeroLabo non vide
         ValidationUtils.rejectIfEmptyOrWhitespace(errs, "numeroLabo", "prelevement.numeroLabo.empty");

         if(!prel.getNumeroLabo().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("numeroLabo", "prelevement.numeroLabo.illegal");
         }
         if(prel.getNumeroLabo().length() > 50){
            errs.rejectValue("numeroLabo", "prelevement.numeroLabo.tooLong");
         }
      }

      // congelation coherente
      if(prel.getCongDepart() != null && prel.getCongDepart() && prel.getCongArrivee() && prel.getCongArrivee()){
         errs.rejectValue("congDepart", "prelevement.congelation.bothDepartArrivee");
      }

      //date prelevement cohérente
      errs.addAllErrors(checkDatePrelevementCoherence(prel));

      //date consentement cohérente
      errs.addAllErrors(checkDateConsentCoherence(prel));

      //date départ cohérente
      errs.addAllErrors(checkDateDepartCoherence(prel));

      //date arrivee cohérente
      errs.addAllErrors(checkDateArriveeCoherence(prel));
   }

   @Override
   public Errors checkDatePrelevementCoherence(final Prelevement prelevement){

      final BindException errs = new BindException(prelevement, "fr.aphp.tumorotek.model.coeur.prelevement.Prelevement");

      if(prelevement.getDatePrelevement() != null){
         if(prelevement.getMaladie() != null && prelevement.getMaladie().getPatient().getDateNaissance() != null){
            ValidationUtilities.checkWithDate(prelevement.getDatePrelevement(), "datePrelevement",
               prelevement.getMaladie().getPatient().getDateNaissance(), "date", "validation", "DateNaissance", errs, false);
         }

         //limites sup
         if(prelevement.getDateDepart() != null){
            ValidationUtilities.checkWithDate(prelevement.getDatePrelevement(), "datePrelevement", prelevement.getDateDepart(),
               "date", "validation", "DateDepartPrelevement", errs, true);
         }else if(prelevement.getLaboInters() != null && coherenceDateManager
            .findPostRefDateInLabosManager(new ArrayList<>(prelevement.getLaboInters()))[0] != null){
            final Object[] dateAndCode =
               coherenceDateManager.findPostRefDateInLabosManager(new ArrayList<>(prelevement.getLaboInters()));
            if(dateAndCode[0] != null){
               if(!ValidationUtilities.checkWithDate(prelevement.getDatePrelevement(), null, dateAndCode[0], null, null, null,
                  null, true)){
                  errs.rejectValue("datePrelevement", (String) dateAndCode[1]);
               }
            }
         }else if(prelevement.getDateArrivee() != null){
            ValidationUtilities.checkWithDate(prelevement.getDatePrelevement(), "datePrelevement", prelevement.getDateArrivee(),
               "date", "validation", "DateArriveePrelevement", errs, true);
         }else{
            final Object[] dateAndCode = coherenceDateManager.findPostRefDateInEchantillonsManager(prelevement);
            if(!ValidationUtilities.checkWithDate(prelevement.getDatePrelevement(), null, dateAndCode[0], null, null, null, null,
               true)){
               errs.rejectValue("datePrelevement", (String) dateAndCode[1]);
            }
         }
      } //else { // melbase hack
        //	errs.rejectValue("datePrelevement", "prelevement.date.empty");
        //}
      return errs;
   }

   @Override
   public Errors checkDateConsentCoherence(final Prelevement prelevement){

      final BindException errs = new BindException(prelevement, "fr.aphp.tumorotek.model.coeur.prelevement.Prelevement");

      if(prelevement.getConsentDate() != null){
         if(prelevement.getMaladie() != null && prelevement.getMaladie().getPatient().getDateNaissance() != null){
            ValidationUtilities.checkWithDate(prelevement.getConsentDate(), "consentDate",
               prelevement.getMaladie().getPatient().getDateNaissance(), "date", "validation", "DateNaissance", errs, false);
         }

         if(!prelevement.getConsentDate().before(Utils.getCurrentSystemDate())){
            errs.rejectValue("consentDate", "date.validation.supDateActuelle");
         }
      }
      return errs;
   }

   @Override
   public Errors checkDateDepartCoherence(final Prelevement prelevement){

      final BindException errs = new BindException(prelevement, "fr.aphp.tumorotek.model.coeur.prelevement.Prelevement");

      if(prelevement.getDateDepart() != null){
         // limites inf
         if(prelevement.getDatePrelevement() != null){
            ValidationUtilities.checkWithDate(prelevement.getDateDepart(), "dateDepart", prelevement.getDatePrelevement(), "date",
               "validation", "DatePrelevement", errs, false);
         }else{
            if(prelevement.getMaladie() != null && prelevement.getMaladie().getPatient().getDateNaissance() != null){
               ValidationUtilities.checkWithDate(prelevement.getDateDepart(), "dateDepart",
                  prelevement.getMaladie().getPatient().getDateNaissance(), "date", "validation", "DateNaissance", errs, false);
            }
         }
         // limites sup
         if(prelevement.getLaboInters() != null && coherenceDateManager
            .findPostRefDateInLabosManager(new ArrayList<>(prelevement.getLaboInters()))[0] != null){
            final Object[] dateAndCode =
               coherenceDateManager.findPostRefDateInLabosManager(new ArrayList<>(prelevement.getLaboInters()));
            if(dateAndCode[0] != null){
               if(!ValidationUtilities.checkWithDate(prelevement.getDateDepart(), null, dateAndCode[0], null, null, null, null,
                  true)){
                  errs.rejectValue("dateDepart", (String) dateAndCode[1]);
               }
            }
         }else if(prelevement.getDateArrivee() != null){
            ValidationUtilities.checkWithDate(prelevement.getDateDepart(), "datePrelevement", prelevement.getDateArrivee(),
               "date", "validation", "DateArriveePrelevement", errs, true);
         }else{
            // limites sup
            //Object[] dateAndCode = coherenceDateManager.
            //			findPostRefDateInEchantillonsManager(prelevement);
            if(!ValidationUtilities.checkWithDate(prelevement.getDateDepart(), null, Utils.getCurrentSystemDate(), null, null,
               null, null, true)){
               errs.rejectValue("dateArrivee", "date.validation.supDateActuelle");
            }
         }
      }
      return errs;
   }

   @Override
   public Errors checkDateArriveeCoherence(final Prelevement prelevement){

      final BindException errs = new BindException(prelevement, "fr.aphp.tumorotek.model.coeur.prelevement.Prelevement");

      if(prelevement.getDateArrivee() != null){

         // limites inf
         if(prelevement.getDateDepart() != null){
            ValidationUtilities.checkWithDate(prelevement.getDateArrivee(), "dateArrivee", prelevement.getDateDepart(), "date",
               "validation", "DateDepartPrelevement", errs, false);
         }else{
            if(prelevement.getDatePrelevement() != null){
               ValidationUtilities.checkWithDate(prelevement.getDateArrivee(), "dateArrivee", prelevement.getDatePrelevement(),
                  "date", "validation", "DatePrelevement", errs, false);
            }else{
               if(prelevement.getMaladie() != null && prelevement.getMaladie().getPatient().getDateNaissance() != null){
                  ValidationUtilities.checkWithDate(prelevement.getDateArrivee(), "dateArrivee",
                     prelevement.getMaladie().getPatient().getDateNaissance(), "date", "validation", "DateNaissance", errs,
                     false);
               }
            }
         }
         // limites sup
         //Object[] dateAndCode = coherenceDateManager.
         //				findPostRefDateInEchantillonsManager(prelevement);
         if(!ValidationUtilities.checkWithDate(prelevement.getDateArrivee(), null, Utils.getCurrentSystemDate(), null, null, null,
            null, true)){
            errs.rejectValue("dateArrivee", "date.validation.supDateActuelle");
         }
      }
      return errs;
   }

   //	@Override
   //	public Object[] findAntRefDateInPrelevement(Prelevement prelevement, 
   //													boolean skipToDatePrel) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Object ref = null;
   //		String code = null;
   //		if (!skipToDatePrel) {
   //			if (prelevement.getDateArrivee() != null) {
   //				ref = prelevement.getDateArrivee();
   //				code = "date.validation.infDateArriveePrelevement";
   //			} else { 
   //				Object laboInterRef = findLaboInterReferenceDate(prelevement);
   //				if (laboInterRef != null) {
   //					ref = laboInterRef;
   //					code = "date.validation.infLastDateLaboInter";
   //				} else if (prelevement.getDateDepart() != null) {
   //					ref = prelevement.getDateDepart().getTime();
   //					code = "date.validation.infDateDepartPrelevement";
   //				}
   //			}
   //		} 
   //		if (skipToDatePrel || ref == null) {
   //			if (prelevement.getDatePrelevement() != null) {
   //				ref = prelevement.getDatePrelevement();
   //				code = "date.validation.infDatePrelevement";
   //			} else if (prelevement.getMaladie() != null
   //				&&  prelevement.getMaladie().getPatient()
   //									.getDateNaissance() != null) {
   //				ref = prelevement.getMaladie().getPatient()
   //												.getDateNaissance();
   //				code = "date.validation.infDateNaissance";
   //			}
   //		}
   //		dateAndCode[0] = ref;
   //		dateAndCode[1] = code;
   //		
   //		return dateAndCode;
   //	}

   //	/**
   //	 * Parcoure de manière récursive les labos inter 
   //	 * du plus récent au plus ancien afin de trouver la première référence 
   //	 * de date antérieure parmi eux.
   //	 * @param echantillon
   //	 * @return date référence de date.
   //	 */
   //	private Calendar findLaboInterReferenceDate(Prelevement prelevement) {
   //		Calendar ref = null;
   //		if (prelevement.getLaboInters() != null) {
   //			List<LaboInter> list = new ArrayList<LaboInter>();
   //			if (prelevement.getPrelevementId() != null) {
   //				list.addAll(prelevementManager
   //									.getLaboIntersManager(prelevement));
   //			} else {
   //				list.addAll(prelevement.getLaboInters());
   //			}
   //		
   //			int ordre;
   //			// utilisation de previous car aucune certitude sur l'ordre
   //			// des labos dans le set
   //			int previous = 0;
   //			LaboInter labo = null;
   //			for (int i = (list.size() - 1); i >= 0; i--) {
   //				labo = list.get(i);
   //				ordre = labo.getOrdre();
   //				if (ordre > previous) {
   //					if (labo.getDateDepart() != null) {
   //						ref = labo.getDateDepart();
   //						previous = ordre;
   //					} else if (labo.getDateArrivee() != null) {
   //						ref = labo.getDateArrivee();
   //						previous = ordre;
   //					}
   //				}
   //			}
   //		}
   //		
   //		return ref;	
   //	}

   //	@Override
   //	public Object[] findPostRefDateInEchantillons(Prelevement prelevement) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Object ref = null;
   //		String code = null;
   //		Object previous = null;
   //		String codePrevious = null;
   //		Object[] dateAndCodeForEchan;
   //		
   //		// trouve les echantillons
   //		List<Echantillon> echans = new ArrayList<Echantillon>();
   //		if (prelevement.getPrelevementId() != null) {
   //			echans.addAll(echantillonManager
   //					.findByPrelevementManager(prelevement));
   //		} else {
   //			echans.addAll(prelevement.getEchantillons());
   //		}
   //		
   //		// trouve la date de reference
   //		for (int i = 0; i < echans.size(); i++) {
   //			if (!echans.get(i).getArchive()) {
   //				if (echans.get(i).getDateStock() != null) {
   //					ref = echans.get(i).getDateStock();
   //					code = "date.validation.supDateStockEchanEnfant";
   //					
   //				} else {
   //					dateAndCodeForEchan = prodDeriveValidator
   //								.findPostRefDateInDerives(echans.get(i));
   //					ref = dateAndCodeForEchan[0];
   //					if (ref != null) {
   //						code = (String) dateAndCodeForEchan[1];
   //					}
   //				}
   //				
   //				if (ref != null) {
   //					if (previous != null) {
   //						if (ValidationUtilities.checkWithDate(ref, null, 
   //								previous, null, null, null, null, true)) {
   //							previous = ref;
   //							codePrevious = code;
   //						}
   //					} else {
   //						previous = ref;
   //						codePrevious = code;
   //					}
   //				}
   //			}
   //		}
   //		
   //		// trouve parmi les derives
   //		Object[] dateAndCodeForDerives = prodDeriveValidator
   //									.findPostRefDateInDerives(prelevement);
   //		ref = dateAndCodeForDerives[0];
   //		code = (String) dateAndCodeForDerives[1];
   //			
   //		if (ref != null) {
   //			if (previous != null) {
   //				if (ValidationUtilities.checkWithDate(ref, null, 
   //						previous, null, null, null, null, true)) {
   //					previous = ref;
   //					codePrevious = code;
   //				}
   //			} else {
   //				previous = ref;
   //				codePrevious = code;
   //			}
   //		}
   //		
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
   //	
   //	
   //	@Override
   //	public Object[] findPostRefDateInLabos(List<LaboInter> labos) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Object ref = null;
   //		String code = null;
   //		
   //		Collections.sort(labos, new LaboInterComparator());
   //		
   //		for (int i = 0; i < labos.size(); i++) {
   //			if (labos.get(i).getDateArrivee() != null) {
   //				ref = labos.get(i).getDateArrivee();
   //				code = "date.validation.supDateArriveeUnLaboInter";
   //				break;
   //			} else if (labos.get(i).getDateDepart() != null) {
   //				ref = labos.get(i).getDateDepart();
   //				code = "date.validation.supDateDepartUnLaboInter";
   //				break;
   //			}
   //		}
   //		 dateAndCode[0] = ref;
   //		 dateAndCode[1] = code;
   //		
   //		return dateAndCode;
   //	}

}
