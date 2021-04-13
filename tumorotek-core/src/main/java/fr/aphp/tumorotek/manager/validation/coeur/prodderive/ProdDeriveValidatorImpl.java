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
package fr.aphp.tumorotek.manager.validation.coeur.prodderive;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveValidator;
import fr.aphp.tumorotek.manager.validation.CoherenceDateManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Validator pour le bean domaine ProdDerive (de prodderive).
 * Classe creee le 12/10/09
 *
 * Regles de validation:
 * 	- le champ code doit etre non vide, non null<br>
 *  - le champ code labo doit etre non vide<br>
 *  - les champs quantité, quantité_init, volume,<br>
 *    volume_init, conc doivent etre positifs<br>
 *  - la date de stockage doit être null ou<br>
 *  	supérieure ou égale à la date de transformation<br>
 *  	sinon <br>
 *  		si le parent est un derive<br>
 *  		supérieure ou égale à la date de stockage du parent<br>
 *  		sinon <br>
 *  			supérieure ou égale à la date de transformation du parent<br>
 *  		sinon OU si le parent est un echantillon<br>
 *  			supérieure ou égale à la date de stockage de l'echantillon,<br>
 *  		sinon OU si le parent est un prelevement<br>
 *  			supérieure ou égale à la date d'arrivee du prelevement,<br>
 *  			sinon<br>
 *  				supérieure ou égale à la date de départ du
 *  				dernier labo inter si non null,<br>
 *  				sinon<br>
 *  					supérieure ou égale à la date d'arrivee
 *  					du dernier labo inter si non null,<br>
 *  					...recursivite sur labos précédents<br>
 *  					sinon<br>
 *  						supérieure ou égale à la date de départ
 *  						du prelevement si non null,<br>
 *  							sinon<br>
 *  							supérieure ou égale à la date de
 *  							prelevement si non null,<br>
 *  							sinon<br>
 *  								supérieure ou égale à la date de
 *  								naissance,<br>
 *  	inférieure ou égale à la date actuelle <br>
 *  - meme logique pour date transformation avec date stocage dans
 *  les limites sup.
 *
 *  2.0.7 Ajout validation sur STATUT et EMPLACEMENT
 *
 * @author Pierre VENTADOUR
 * @version 2.2.3-genno
 */
public class ProdDeriveValidatorImpl implements ProdDeriveValidator
{

   private CoherenceDateManager coherenceDateManager;

   public void setCoherenceDateManager(final CoherenceDateManager cManager){
      this.coherenceDateManager = cManager;
   }

   //	private TransformationManager transformationManager;
   //	private EchantillonManager echantillonManager;
   //	private PrelevementValidator prelevementValidator;
   //	public void setTransformationManager(TransformationManager tManager) {
   //		this.transformationManager = tManager;
   //	}
   //
   //	public void setPrelevementValidator(PrelevementValidator pValidator) {
   //		this.prelevementValidator = pValidator;
   //	}
   //
   //	public void setEchantillonManager(EchantillonManager eManager) {
   //		this.echantillonManager = eManager;
   //	}

   @Override
   public boolean supports(final Class<?> clazz){
      return ProdDerive.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){
      //code non vide
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "code", "prodDerive.code.empty");

      final ProdDerive derive = (ProdDerive) obj;
      // code valide
      if(derive.getCode() != null){
         if(!derive.getCode().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("code", "prodDerive.code.illegal");
         }
         if(derive.getCode().length() > 50){
            errs.rejectValue("code", "prodDerive.code.tooLong");
         }
      }

      // codeLabo valide
      if(derive.getCodeLabo() != null){
         if(derive.getCodeLabo().matches(ValidationUtilities.ONLYSPACESREGEXP)){
            errs.rejectValue("codeLabo", "prodDerive.codeLabo.empty");
         }
         if(!derive.getCodeLabo().matches(ValidationUtilities.MOTREGEXP)){
            errs.rejectValue("codeLabo", "prodDerive.codeLabo.illegal");
         }
         if(derive.getCodeLabo().length() > 50){
            errs.rejectValue("codeLabo", "prodDerive.codeLabo.tooLong");
         }
      }

      // quantiteInit >= 0
      if(derive.getQuantiteInit() != null){
         if(derive.getQuantiteInit() < 0){
            errs.rejectValue("quantiteInit", "prodDerive.quantiteInit.negative");
         }
      }
      // quantite >= 0
      if(derive.getQuantite() != null){
         if(derive.getQuantite() < 0){
            errs.rejectValue("quantite", "prodDerive.quantite.negative");
         }
      }
      // quantiteInit >= quantite
      if(derive.getQuantite() != null && derive.getQuantiteInit() != null){
         if(derive.getQuantite() > derive.getQuantiteInit()){
            errs.rejectValue("quantite", "prodDerive.quantite.tooBig");
         }
      }
      // volumeInit >= 0
      if(derive.getVolumeInit() != null){
         if(derive.getVolumeInit() < 0){
            errs.rejectValue("volumeInit", "prodDerive.volumeInit.negative");
         }
      }
      // volume >= 0
      if(derive.getVolume() != null){
         if(derive.getVolume() < 0){
            errs.rejectValue("volume", "prodDerive.volume.negative");
         }
      }
      // volumeInit >= volume
      if(derive.getVolume() != null && derive.getVolumeInit() != null){
         if(derive.getVolume() > derive.getVolumeInit()){
            errs.rejectValue("volume", "prodDerive.volume.tooBig");
         }
      }
      // quantiteInit = volumeInit * concentration
      if(derive.getQuantiteInit() != null && derive.getVolumeInit() != null && derive.getConc() != null){
         Float res = derive.getVolumeInit() * derive.getConc();
         Float floor = Utils.floor(res, 3);
         // TK-290 arrondi peut être supérieur ou inférieur = OK
         if((derive.getQuantiteInit().floatValue() > (floor + 0.001f)) 
        		|| (derive.getQuantiteInit().floatValue() < (floor - 0.001f)) ){
            errs.rejectValue("quantiteInit", "prodDerive.quantiteInit.illegal");
         }
      }
      // unites valides
      if(derive.getVolumeUnite() != null){
         if(derive.getConcUnite() != null){
            if(!derive.getConcUnite().getNom().contains(derive.getVolumeUnite().getNom())){
               errs.rejectValue("concUnite", "prodDerive.concUnite.illegal");
            }

            if(derive.getQuantiteUnite() != null){
               if(!derive.getConcUnite().getNom().contains(derive.getQuantiteUnite().getNom())){
                  errs.rejectValue("concUnite", "prodDerive.concUnite.illegal");
               }
            }
         }
      }
      // delai_cgl >= 0
      if(derive.getConc() != null){
         if(derive.getConc() < 0){
            errs.rejectValue("conc", "prodDerive.conc.negative");
         }
      }

      // 2.0.7: validation statut emplacement
      if(derive.getEmplacement() != null){
         if(derive.getObjetStatut().getStatut().equals("NON STOCKE")
            || (derive.getObjetStatut().getStatut().equals("EPUISE") || (derive.getObjetStatut().getStatut().equals("DETRUIT")))){
            errs.rejectValue("objetStatut", "prodDerive.statut.incoherent");
         }
      }else if(derive.getObjetStatut().getStatut().equals("STOCKE") || (derive.getObjetStatut().getStatut().equals("RESERVE"))){
         errs.rejectValue("objetStatut", "prodDerive.statut.incoherent");
      }

      // date stockage cohérente
      errs.addAllErrors(checkDateStockCoherence(derive));

      // date transformation incohérente
      errs.addAllErrors(checkDateTransfoCoherence(derive));

   }

   @Override
   public Errors checkDateStockCoherence(final ProdDerive derive){

      final BindException errs = new BindException(derive, "fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive");
      // limites inf
      if(derive.getDateStock() != null){
         if(derive.getDateTransformation() != null){
            ValidationUtilities.checkWithDate(derive.getDateStock(), "dateStock", derive.getDateTransformation(), "date",
               "validation", "DateTransfo", errs, false);
         }
         Object[] dateAndCode = coherenceDateManager.findAntRefDateForDeriveManager(derive);
         if(dateAndCode[0] != null){
            if(!ValidationUtilities.checkWithDate(derive.getDateStock(), null, dateAndCode[0], null, null, null, null, false)){
               errs.rejectValue("dateStock", (String) dateAndCode[1]);
            }
         }

         //limites sup
         dateAndCode = coherenceDateManager.findPostRefDateInDerivesManager(derive);
         if(!ValidationUtilities.checkWithDate(derive.getDateStock(), null, dateAndCode[0], null, null, null, null, true)){
            errs.rejectValue("dateStock", (String) dateAndCode[1]);
         }
      } // else { // melbase hack
        //	errs.rejectValue("dateStock", "derive.dateStock.empty");
        //}
      return errs;
   }

   @Override
   public Errors checkDateTransfoCoherence(final ProdDerive derive){

      final BindException errs = new BindException(derive, "fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive");
      // limites inf
      if(derive.getDateTransformation() != null){
         Object[] dateAndCode = coherenceDateManager.findAntRefDateForDeriveManager(derive);
         if(dateAndCode[0] != null){
            if(!ValidationUtilities.checkWithDate(derive.getDateTransformation(), null, dateAndCode[0], null, null, null, null,
               false)){
               errs.rejectValue("dateTransformation", (String) dateAndCode[1]);
            }
         }

         //limites sup
         if(derive.getDateStock() != null){
            ValidationUtilities.checkWithDate(derive.getDateTransformation(), "dateTransformation", derive.getDateStock(), "date",
               "validation", "DateStock", errs, true);
         }
         dateAndCode = coherenceDateManager.findPostRefDateInDerivesManager(derive);
         if(!ValidationUtilities.checkWithDate(derive.getDateTransformation(), null, dateAndCode[0], null, null, null, null,
            true)){
            errs.rejectValue("dateTransformation", (String) dateAndCode[1]);
         }
      }
      return errs;
   }

   //	/**
   //	 * Parcoure les dates des objets précédant un dérivé jusqu'a la date 
   //	 * de naissance du patient, pour trouver la date de référence la plus 
   //	 * récente.
   //	 * Permet la récursivite sur les dérivés de dérivés.
   //	 * @param dérivé
   //	 * @paral object parent
   //	 * @return object[] couple (date reference, code qui sera utilisé pour l'
   //	 * internationalisation du message d'erreur)
   //	 */
   //	public Object[] findAntRefDateForDerive(ProdDerive derive) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Object ref = null;
   //		String code = null;
   //		//trouve le parent
   //		if (derive.getTransformation() != null) {
   //			Object parent = entiteManager
   //				.findObjectByEntiteAndIdManager(derive.getTransformation()
   //															.getEntite(), 
   //											derive.getTransformation()
   //															.getObjetId());	
   //			if (parent.getClass().getSimpleName().equals("ProdDerive")) {
   //				if (((ProdDerive) parent).getDateStock() != null) {
   //					ref = ((ProdDerive) parent).getDateStock();
   //					code = "date.validation.infDateStockDeriveParent";
   //				} else if (((ProdDerive) parent)
   //										.getDateTransformation() != null) {
   //					ref = ((ProdDerive) parent).getDateTransformation();
   //					code = "date.validation.infDateTransfoDeriveParent"; 
   //				} else {
   //					return findAntRefDateForDerive((ProdDerive) parent);
   //				}
   //			} else if (parent.getClass()
   //					.getSimpleName().equals("Echantillon")) {
   //				if (((Echantillon) parent).getDateStock() != null) {
   //					ref = ((Echantillon) parent).getDateStock();
   //					code = "date.validation.infDateStockEchan";
   //				} else if (echantillonManager.getPrelevementManager(
   //						(Echantillon) parent) != null) {
   //					return prelevementValidator
   //						.findAntRefDateInPrelevement(echantillonManager
   //								.getPrelevementManager(
   //								(Echantillon) parent), true);
   //				}
   //			} else {
   //				return prelevementValidator
   //					.findAntRefDateInPrelevement((Prelevement) parent, true);
   //			}
   //		}
   //		dateAndCode[0] = ref;
   //		dateAndCode[1] = code;
   //		
   //		return dateAndCode;
   //	}
   //	
   //	@Override
   //	public Object[] findPostRefDateInDerives(Object parent) {
   //		Object[] dateAndCode = new Object[]{null, null};
   //		Calendar ref = null;
   //		String code = null;
   //		Calendar previous = null;
   //		String codePrevious = null;
   //		// trouve les derives potentiels, aucun si update
   //		List<ProdDerive> derives = transformationManager
   //									.findAllDeriveFromParentManager(parent);
   //		
   //		// trouve la date de reference
   //		for (int i = 0; i < derives.size(); i++) {
   //			if (!derives.get(i).getArchive()) {
   //				if (derives.get(i).getDateTransformation() != null) {
   //					ref = derives.get(i).getDateTransformation();
   //					code = "date.validation.supDateTransfoEnfant";
   //					
   //				} else if (derives.get(i).getDateStock() != null) {
   //					ref = derives.get(i).getDateStock();
   //					code = "date.validation.supDateStockDeriveEnfant";
   //				}
   //				
   //				if (ref != null) {
   //					if (previous != null) {
   //						if (ValidationUtilities
   //								.checkWithDate(ref, null, previous, 
   //										null, null, null, null, true)) {
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
