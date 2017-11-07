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
package fr.aphp.tumorotek.manager.validation.coeur.cession.retour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import fr.aphp.tumorotek.dao.cession.RetourDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.ObjectStatutException;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Retour;

public class RetourValidatorImpl implements RetourValidator {
	
	private RetourDao retourDao;
	private EntiteDao entiteDao;
	private EntiteManager entiteManager;

	public void setEntiteManager(EntiteManager r) {
		this.entiteManager = r;
	}

	public void setRetourDao(RetourDao r) {
		this.retourDao = r;
	}

	public void setEntiteDao(EntiteDao e) {
		this.entiteDao = e;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Retour.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Retour retour = (Retour) target;
		
		//Objet statut -> creation impossible
		TKStockableObject obj = retour.getTkObject();
		if (obj != null && (obj.getObjetStatut().getStatut().equals("EPUISE") 
			// || obj.getObjetStatut().getStatut().equals("RESERVE")
			 || obj.getObjetStatut().getStatut().equals("ENCOURS")) 
			 && retour.getRetourId() == null) {
			throw new ObjectStatutException(entiteDao
				.findByNom(obj.entiteNom()).get(0).getNom(), 
				"évènement de stockage");
		}
		
		
		if (retour.getDateSortie() == null) {
			errors.rejectValue("dateSortie", "validation.syntax.empty");
		}
		//if (retour.getDateRetour() == null) {
		//	errors.rejectValue("dateSortie", "validation.syntax.empty");
		//}
		if (retour.getTempMoyenne() == null) {
			errors.rejectValue("tempMoyenne", "validation.syntax.empty");
		}
		
		// observations valide
		if (retour.getObservations() != null) {
			ValidationUtils
			.rejectIfEmptyOrWhitespace(errors, "observations", 
									"retour.observations.empty");
			
			if (retour.getObservations().length() > 250) {
				errors.rejectValue(
						"observations", "retour.observations.tooLong");
			}
		}
		
		errors.addAllErrors(checkDateSortieCoherence(retour));
		errors.addAllErrors(checkDateRetourCoherence(retour));
	}
	
	@Override
	public Errors checkDateSortieCoherence(Retour retour) {
		
		BindException errs = new BindException(retour, 
						"fr.aphp.tumorotek.model.cession.Retour");
		
		//date de sortie cohérente
		if (retour.getDateSortie() != null) {
			
			TKStockableObject obj = (TKStockableObject) entiteManager
					.findObjectByEntiteAndIdManager(retour.getEntite(), 
							retour.getObjetId());
			
			// limites inf Date Stockage
			if (obj != null && obj.getDateStock() != null) {
				if (retour.getDateSortie().before(obj.getDateStock()) 
						&& obj.getDateStock().getTimeInMillis() != retour.getDateSortie().getTimeInMillis()) {
					errs.rejectValue("dateSortie", "date.validation.infDateStockage");
				}
			}
			
			
			// limites sup date retour
			if (retour.getDateRetour() != null) {
				if (retour.getDateSortie().after(retour.getDateRetour())
						&& retour.getDateSortie().getTimeInMillis() != retour.getDateRetour().getTimeInMillis()) {
					errs.rejectValue("dateSortie", "date.validation.supDateRetour");
				}
				
				// inclusion d'un autre retour
				if (!findByObjInsideDatesManager(retour.getDateSortie(), 
						retour.getDateRetour(), obj, retour.getRetourId()).isEmpty()) {
					errs.rejectValue("dateSortie","date.validation.inclueRetourExistant");
				}
			}
			
			// inclusion dans un autre retour
			if (!findByObjDatesManager(retour.getDateSortie(), obj, 
											retour.getRetourId()).isEmpty()) {
				errs.rejectValue("dateSortie","date.validation.incluDansRetourExistant");
			}
			
			Calendar overTime = Calendar.getInstance();
			overTime.set(Calendar.YEAR, 9999);
			if (retour.getDateSortie().after(overTime)) {
				errs.rejectValue("dateSortie", "retour.dateSortie.illegal");
			}
			
		} else { 
			errs.rejectValue("dateSortie", "retour.dateSortie.empty");
		}
		return errs;
	}

	@Override
	public Errors checkDateRetourCoherence(Retour retour) {
		BindException errs = new BindException(retour, 
				"fr.aphp.tumorotek.model.cession.Retour");

		//date de sortie cohérente
		if (retour.getDateRetour() != null) {

			TKStockableObject obj = (TKStockableObject) entiteManager
					.findObjectByEntiteAndIdManager(retour.getEntite(), 
							retour.getObjetId());


			// limites inf date sortie
			if (retour.getDateSortie() != null) {
				if (retour.getDateRetour().before(retour.getDateSortie())
						&& !retour.getDateSortie().equals(retour.getDateRetour())) {
					errs.rejectValue("dateRetour", 
							"date.validation.infDateSortie");
				}

				// inclusion d'un autre retour
				if (!findByObjInsideDatesManager(retour.getDateSortie(), 
						retour.getDateRetour(), obj, retour.getRetourId()).isEmpty()) {
					errs.rejectValue("dateRetour","date.validation.inclueRetourExistant");
				}
			} else { 
				errs.rejectValue("dateSortie", "retour.dateSortie.empty");
			}

			// inclusion dans un autre retour
			if (!findByObjDatesManager(retour.getDateRetour(), obj, 
					retour.getRetourId()).isEmpty()) {
				errs.rejectValue("dateRetour","date.validation.incluDansRetourExistant");
			}
			
			Calendar overTime = Calendar.getInstance();
			overTime.set(Calendar.YEAR, 9999);
			if (retour.getDateRetour().after(overTime)) {
				errs.rejectValue("dateRetour", "retour.dateRetour.illegal");
			}

		} 
		return errs;
	}
	
	@Override
	public List<Retour> findByObjDatesManager(Calendar cal,
			TKStockableObject obj, Integer exclId) {
		if (obj != null) {
			return retourDao.findByObjDates(cal, obj.listableObjectId(), 
				entiteDao.findByNom(obj.entiteNom()).get(0), 
				exclId != null ? exclId : -1);
		} else {
			return new ArrayList<Retour>();
		}
	}

	@Override
	public List<Retour> findByObjInsideDatesManager(Calendar cal1,
			Calendar cal2, TKStockableObject obj, Integer exclId) {
		if (obj != null) {
			return retourDao.findByObjInsideDates(cal1, cal2, 
					obj.listableObjectId(), 
				entiteDao.findByNom(obj.entiteNom()).get(0), 
					exclId != null ? exclId : -1);
		} else {
			return new ArrayList<Retour>();
		}
	}
}
