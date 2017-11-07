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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.validator.routines.FloatValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.validation.CoherenceDateManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;

/**
 * Validator pour le bean domaine LaboInter.<br>
 * Classe creee le 09/10/09<br>
 * <br>
 * Regles de validation:<br>
 * 	- le champ ordre doit etre non vide, non null, et strictement positif<br>
 *  - le champ conservTemp doit etre compris entre -1000 et +1000<br>
 *  - le champ transportTemp doit etre null ou compris entre -1000 et +1000<br>
 *  - la date d'arrivée doit être null ou
 *  	supérieure ou égale à la date de départ du labo precedent si non null,
 *  	sinon
 *  		supérieure ou égale à la date d'arrivee du labo precedent 
 *  		si non null,
 *  		...recursivité sur premiers labos
 *  		sinon
 *  			supérieure ou égale à la date de départ du prelevement 
 *  			si non null,
 *  			sinon
 *  				supérieure ou égale à la date de prelevement si non null,
 *  				sinon
 *  					supérieure ou égale à la date de naissance,
 *  	inferieure ou égale à la date de départ du present labo si non null,
 *  	sinon 
 *  		inférieure ou égale à la date de départ du labo suivant si non null,
 *  		sinon
 *  			infériure ou égale à la date d'arrivee du labo suivant 
 *  			si non null,
 *  			...recursivité sur labos suivants
 *  			sinon
 *  				inférieure ou égale à la date d'arrivée du prelevement 
 *  				si non nulle
 *  				sinon	
 *  					inférieure ou égale à la date actuelle <br>
 *  
 *  - la date de départ doit être null ou
 *  	supérieure ou égale à la date d'arrivee au labo si non null,
 *  	sinon
 *  		supérieure ou égale à la date de départ du labo precedent 
 *  		si non null,
 *  		sinon
 *  			supérieure ou égale à la date d'arrivee du labo precedent 
 *  			si non null,
 *  			...recursivite sur premiers labos
 *  			sinon
 *  				supérieure ou égale à la date de départ du prelevement 
 *  				si non null,
 *  				sinon
 *  					supérieure ou égale à la date de prelevement 
 *  					si non null,
 *  					sinon
 *  						supérieure ou égale à la date de naissance,
 *  	inférieure ou égale à la date de départ du labo suivant si non null,
 *  		sinon
 *  			inférieure ou égale à la date d'arrivee du labo suivant 
 *  			si non null,
 *  			...recursivité sur labos suivants
 *  			sinon
 *  				inférieure ou égale à la date d'arrivée du prelevement 
 *  				si non nulle
 *  				sinon	
 *  					inférieure ou égale à la date actuelle <br>
 *   - le champs stérile ne peut être TRUE que si la chaine stérilité 
 *   Prélèvement et LaboInter precedent est spécifiée à TRUE<br>
 *  	
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public class LaboInterValidatorImpl implements LaboInterValidator {
	
//	private PrelevementValidator prelevementValidator;
//
//	public void setPrelevementValidator(PrelevementValidator pVal) {
//		this.prelevementValidator = pVal;
//	}
	
	private CoherenceDateManager coherenceDateManager;

	public void setCoherenceDateManager(CoherenceDateManager cManager) {
		this.coherenceDateManager = cManager;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LaboInter.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errs) {
		LaboInter labo = (LaboInter) obj; 			
		
		//ordre non null
		if (labo.getOrdre() == null) {
			errs.rejectValue("ordre", "laboInter.ordre.empty");
		} else if (!(labo.getOrdre() > 0)) {
    			errs.rejectValue("ordre", "laboInter.ordre.nonpositive");
		} 
		
		//conservTemp float range
		if (labo.getConservTemp() != null 
				&& !FloatValidator.getInstance()
				.isInRange(labo.getConservTemp(), -1000.0, 1000.0)) {
			errs.rejectValue("conservTemp", "laboInter.conservTemp.notInRange");
		}
		//transportTemp float range
		if (labo.getTransportTemp() != null 
				&& !FloatValidator.getInstance()
				.isInRange(labo.getTransportTemp(), -1000.0, 1000.0)) {
			errs.rejectValue("conservTemp", 
					"laboInter.transportTemp.notInRange");
		}
		
		//date arrivee cohérente
		errs.addAllErrors(checkDateArriveeCoherence(labo));
		
		//date départ cohérente
		errs.addAllErrors(checkDateDepartCoherence(labo));
		
		//sterilite cohérente
		errs.addAllErrors(checkSteriliteAntecedence(labo));
	}
	
	@Override
	public Errors checkDateArriveeCoherence(LaboInter laboInter) {
		
		BindException errs = new BindException(laboInter, 
					"fr.aphp.tumorotek.model.coeur.prelevement.LaboInter");
		// limites inf
		if (laboInter.getDateArrivee() != null) {
			Set<LaboInter> list = new HashSet<LaboInter>();
			list.addAll(laboInter.getPrelevement().getLaboInters());
			LaboInter previous = findPreviousLabo(laboInter, list, true);
			if (previous != null) {
				if (previous.getDateDepart() != null) {
					ValidationUtilities
					.checkWithDate(laboInter.getDateArrivee(), 
													"dateArrivee", 
													previous.getDateDepart(), 
							"laboInter", "dateArrivee", "PreviousDateDepart", 
													errs, false);
				} else if (previous.getDateArrivee() != null) { 
					ValidationUtilities
						.checkWithDate(laboInter.getDateArrivee(), 
													"dateArrivee", 
													previous.getDateArrivee(), 
							"laboInter", "dateArrivee", "PreviousDateArrivee", 
													errs, false);
				}
			} else if (laboInter.getPrelevement().getDateDepart() != null) {
				ValidationUtilities
				.checkWithDate(laboInter.getDateArrivee(), "dateArrivee", 
						laboInter.getPrelevement().getDateDepart(), 
					"date", "validation", "DateDepartPrelevement", 
											errs, false);
			} else if (laboInter.getPrelevement()
										.getDatePrelevement() != null) {
				ValidationUtilities
					.checkWithDate(laboInter.getDateArrivee(), 
													"dateArrivee", 
							laboInter.getPrelevement().getDatePrelevement(), 
							"date", "validation", "DatePrelevement", 
													errs, false);
			} else if (laboInter.getPrelevement().getMaladie() != null 
				&& laboInter.getPrelevement().getMaladie().getPatient()
												.getDateNaissance() != null) {
				ValidationUtilities
					.checkWithDate(laboInter.getDateArrivee(), 
												"dateArrivee", 
						laboInter.getPrelevement().getMaladie()
											.getPatient().getDateNaissance(), 
						"date", "validation", "DateNaissance", 
												errs, false);
			}
			
			//limites sup
			if (laboInter.getDateDepart() != null) {
				ValidationUtilities
					.checkWithDate(laboInter.getDateArrivee(), 
												"dateArrivee", 
												laboInter.getDateDepart(), 
						"laboInter", "dateArrivee", "DateDepart", 
												errs, true);
			} else {
				LaboInter next = findNextLabo(laboInter, list, true);
				if (next != null) {
					if (next.getDateArrivee() != null) {
						ValidationUtilities
							.checkWithDate(laboInter.getDateArrivee(), 
													"dateArrivee", 
													next.getDateArrivee(), 
							"laboInter", "dateArrivee", "NextDateArrivee", 
													errs, true);
					} else if (next.getDateDepart() != null) {
						ValidationUtilities
							.checkWithDate(laboInter.getDateArrivee(), 
												"dateArrivee", 
												next.getDateDepart(), 
						"laboInter", "dateArrivee", "NextDateDepart", 
												errs, true);
					}
				} else if (laboInter.getPrelevement()
												.getDateArrivee() != null) {
					ValidationUtilities
						.checkWithDate(laboInter.getDateArrivee(), 
												"dateArrivee", 
									laboInter.getPrelevement()
												.getDateArrivee(), 
						"date", "validation", "DateArriveePrelevement", 
												errs, true);
				} else {
					Object[] dateAndCode = 
						coherenceDateManager
							.findPostRefDateInEchantillonsManager(laboInter
															.getPrelevement());
					if (!ValidationUtilities
						.checkWithDate(laboInter.getDateArrivee(), null, 
								dateAndCode[0], null, null, null, null, true)) {
						errs.rejectValue("dateArrivee", 
													(String) dateAndCode[1]);
					}
				}
			}
		}
		return errs;
	}
	
	@Override
	public Errors checkDateDepartCoherence(LaboInter laboInter) {
		
		BindException errs = new BindException(laboInter, 
					"fr.aphp.tumorotek.model.coeur.prelevement.LaboInter");
		// limites inf
		if (laboInter.getDateDepart() != null) {
			Set<LaboInter> list = laboInter.getPrelevement().getLaboInters();
			if (laboInter.getDateArrivee() != null) {
				ValidationUtilities
					.checkWithDate(laboInter.getDateDepart(), 
												"dateDepart", 
												laboInter.getDateArrivee(), 
						"laboInter", "dateDepart", "DateArrivee", 
												errs, false);
			} else {
				LaboInter previous = findPreviousLabo(laboInter, list, true);
				if (previous != null) {
					if (previous.getDateDepart() != null) {
						ValidationUtilities
							.checkWithDate(laboInter.getDateDepart(), 
													"dateDepart", 
													previous.getDateDepart(), 
							"laboInter", "dateDepart", "PreviousDateDepart", 
													errs, false);
					} else if (previous.getDateArrivee() != null) {
						ValidationUtilities
						.checkWithDate(laboInter.getDateDepart(), 
												"dateDepart", 
												previous.getDateArrivee(), 
						"laboInter", "dateDepart", "PreviousDateArrivee", 
												errs, false);
					}
				} else if (laboInter.getPrelevement().getDateDepart() != null) {
					if (!(laboInter.getDateDepart()
						.after(laboInter.getPrelevement().getDateDepart())
						|| laboInter.getDateDepart()
							.equals(laboInter.getPrelevement()
														.getDateDepart()))) {
							errs.rejectValue("dateDepart", 
								"date.validation.infDateDepartPrelevement");
					}
				} else if (laboInter.getPrelevement()
										.getDatePrelevement() != null) {
					ValidationUtilities
						.checkWithDate(laboInter.getDateDepart(), 
													"dateDepart", 
							laboInter.getPrelevement().getDatePrelevement(), 
							"date", "validation", "DatePrelevement", 
													errs, false);
				} else if (laboInter.getPrelevement().getMaladie() != null 
						&& laboInter.getPrelevement().getMaladie().getPatient()
												.getDateNaissance() != null) {
					ValidationUtilities
					.checkWithDate(laboInter.getDateDepart(), 
												"dateDepart", 
						laboInter.getPrelevement().getMaladie()
											.getPatient().getDateNaissance(), 
						"date", "validation", "DateNaissance", 
												errs, false);
				}
			}
			
			//limites sup
			LaboInter next = findNextLabo(laboInter, list, true);	
			if (next != null) {
				if (next.getDateArrivee() != null) {
					ValidationUtilities
						.checkWithDate(laboInter.getDateDepart(), "dateDepart", 
													next.getDateArrivee(), 
								"laboInter", "dateDepart", "NextDateArrivee", 
																errs, true);
				} else if (next.getDateDepart() != null) {
					ValidationUtilities
						.checkWithDate(laboInter.getDateDepart(), "dateDepart", 
												next.getDateDepart(), 
							"laboInter", "dateDepart", "NextDateDepart", 
															errs, true);
				}
			} else if (laboInter.getPrelevement()
											.getDateArrivee() != null) {
				ValidationUtilities
				.checkWithDate(laboInter.getDateDepart(), "dateDepart", 
							laboInter.getPrelevement().getDateArrivee(), 
					"date", "validation", "DateArriveePrelevement", 
													errs, true);
			} else {
				Object[] dateAndCode = 
					coherenceDateManager
						.findPostRefDateInEchantillonsManager(laboInter
														.getPrelevement());
				if (!ValidationUtilities
					.checkWithDate(laboInter.getDateDepart(), null, 
							dateAndCode[0], null, null, null, null, true)) {
					errs.rejectValue("dateDepart", 
												(String) dateAndCode[1]);
				}
			}
		}
		return errs;
	}
	
	/**
	 * Parcoure de manière récursive les labos précedents afin de trouver le 
	 * labo n-1.
	 * @param labo
	 * @param liste labo inters
	 * @param boolean specifiant recherche de labo avec une reference de date
	 * non nulle
	 * @return labo précédent.
	 */
	private static LaboInter findPreviousLabo(LaboInter labo, 
										Set<LaboInter> list, boolean findDate) {
	
		LaboInter previous = null;
		
		if ((labo.getOrdre() - 1) > 0) {
			LaboInter listLab = null;
			
			int ordreEcart = labo.getOrdre(); // ecart max
			Iterator<LaboInter> it = list.iterator();
			while (it.hasNext()) { // trouve le plus petit ecart
				listLab = it.next();
				if (listLab.getOrdre() < labo.getOrdre() 
					&& (labo.getOrdre() - listLab.getOrdre()) < ordreEcart) {
					previous = listLab;
					ordreEcart = labo.getOrdre() - listLab.getOrdre();
				}
			}
			if (findDate && previous != null 
					&& previous.getDateArrivee() == null 
										&& previous.getDateDepart() == null) {
				return findPreviousLabo(previous, list, findDate);
			} 
		}
		return previous;	
	}
	
	/**
	 * Parcoure de manière récursive les labos suivants afin de trouver le 
	 * labo n+1.
	 * @param labo
	 * @param liste labo inters
	 * @param boolean specifiant recherche de labo avec une reference de date
	 * non nulle
	 * @return labo suivant.
	 */
	private static LaboInter findNextLabo(LaboInter labo, 
										Set<LaboInter> list, boolean findDate) {
		LaboInter next = null;
		LaboInter listLab = null;
		
		Iterator<LaboInter> it = list.iterator();
		int maxOrdre = 1;
		while (it.hasNext()) { // trouve l'ordre le plus grand
			listLab = it.next();
			if (listLab.getOrdre() > maxOrdre) {
				maxOrdre = listLab.getOrdre();
			}
		}
		if (labo.getOrdre() < maxOrdre) {			
			int ordreEcart = (maxOrdre + 1) - labo.getOrdre(); // ecart max
			it = list.iterator();
				while (it.hasNext()) { // trouve le plus petit ecart
					listLab = it.next();
					if (listLab.getOrdre() > labo.getOrdre() 
						&& (listLab.getOrdre() 
										- labo.getOrdre()) < ordreEcart) {
						next = listLab;
						ordreEcart = listLab.getOrdre() - labo.getOrdre();
					}
				}
				if (findDate && next != null 
						&& next.getDateArrivee() == null 
										&& next.getDateDepart() == null) {
					return findNextLabo(next, list, findDate);
				} 
		}
		return next;	
	}
	
	@Override
	public Errors checkSteriliteAntecedence(LaboInter labo) {
		
		BindException errs = new BindException(labo, 
			"fr.aphp.tumorotek.model.coeur.prelevement.LaboInter");
		
		if (labo.getSterile() != null && labo.getSterile()) {
			Set<LaboInter> list = labo.getPrelevement().getLaboInters();
			
			LaboInter previous = findPreviousLabo(labo, list, false);
			if (previous != null) {
				if (previous.getSterile() == null || !previous.getSterile()) {
					errs.rejectValue("sterile", 
							"sterile.validation.previousNotSterile");
				}
			} else {
				if (labo.getPrelevement().getSterile() == null 
						|| !labo.getPrelevement().getSterile()) {
					errs.rejectValue("sterile", 
							"sterile.validation.prelevementNotSterile");
				}
			}
		}
		
		return errs;
	}
}
