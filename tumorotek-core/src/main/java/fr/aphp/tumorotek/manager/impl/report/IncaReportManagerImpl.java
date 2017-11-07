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
package fr.aphp.tumorotek.manager.impl.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.manager.code.TableCodageManager;
import fr.aphp.tumorotek.manager.context.BanqueManager;
import fr.aphp.tumorotek.manager.report.IncaReportManager;
import fr.aphp.tumorotek.model.cession.CessionType;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.BanqueTableCodage;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.utils.Utils;

public class IncaReportManagerImpl implements IncaReportManager {
	
	private Log log = LogFactory.getLog(IncaReportManager.class);
	
	private EchantillonDao echantillonDao;
	private PrelevementDao prelevementDao;
	private TableCodageManager tableCodageManager;
	private BanqueManager banqueManager;
	private PatientDao patientDao;
	private CimMasterManager cimMasterManager;	

	public void setEchantillonDao(EchantillonDao eDao) {
		this.echantillonDao = eDao;
	}

	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}

	public void setTableCodageManager(TableCodageManager tManager) {
		this.tableCodageManager = tManager;
	}

	public void setBanqueManager(BanqueManager bManager) {
		this.banqueManager = bManager;
	}

	public void setCimMasterManager(CimMasterManager cM) {
		this.cimMasterManager = cM;
	}

	@Override
	public List<Long> countSamplesManager(Calendar cal1, Calendar cal2,
										Integer interv, List<Banque> banks,
										boolean cumulative) {
		
		List<Long> counts = null;
		
		if (banks != null && !banks.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			Long jourInMs = ms * interv;
			Calendar interm1 = Calendar.getInstance();
			interm1.setTimeInMillis(cal1.getTimeInMillis());
			Calendar interm2 = Calendar.getInstance();
			// la borne supérieure devient exclusive si on retire 1 ms
			interm2.setTimeInMillis(cal1.getTimeInMillis() + jourInMs
					- 1);
			// ajoute l'intervalle pour réaliser chaque compte
			Long ct;
			
			long ant = 0;
			// compte de toute l'ancienneté des échantillons
			if (cumulative) {
				try {
					Calendar calOrigin = Calendar.getInstance();
					calOrigin.setTime(new SimpleDateFormat("dd/MM/yyyy")
														.parse("01/01/0001"));
					ant = echantillonDao
						.findCountSamplesByDates(calOrigin, 
													interm1, banks).get(0);
				} catch (ParseException e) {
					log.error(e);
				} 		
			}
			
			while (interm2.before(cal2) || interm2.equals(cal2)) {
				ct = echantillonDao
					.findCountSamplesByDates(interm1, interm2, banks).get(0);
				if (!cumulative) {
					counts.add(ct);
				} else {
					if (counts.isEmpty()) {
						counts.add(ant + ct);
					} else {
						counts.add(counts.get(counts.size() - 1) + ct);
					}
				}
				interm1.setTimeInMillis(interm2.getTimeInMillis() + 1);
				interm2.setTimeInMillis(interm2.getTimeInMillis() + jourInMs);
			}
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(cal2)) {
				ct = echantillonDao.findCountSamplesByDates(interm1,
															cal2, banks).get(0);
				if (!cumulative || counts.isEmpty()) {
					counts.add(ct);
				} else {
					counts.add(counts.get(counts.size() - 1) + ct);
				}
			}
		}
		return counts;
	}

	@Override
	public List<Long> countSamplesExtManager(Calendar cal1, Calendar cal2,
			Integer interv, List<Banque> banks, List<Etablissement> exts) {

		List<Long> counts = null;
		if (banks != null && !banks.isEmpty() 
								&& exts != null && !exts.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			
			Long jourInMs = ms * interv;
			
			Calendar interm1 = Calendar.getInstance();
			interm1.setTimeInMillis(cal1.getTimeInMillis());
			Calendar interm2 = Calendar.getInstance();
			// la borne supérieure devient exclusive si on retire 1 ms
			interm2.setTimeInMillis(cal1.getTimeInMillis() 
												+ jourInMs - 1);
			
			// ajoute l'intervalle pour réaliser chaque compte
			while (interm2.before(cal2) || interm2.equals(cal2)) {			
				counts.add(echantillonDao
					.findCountSamplesByDatesExt(interm1, interm2, banks, exts)
					.get(0));
				interm1.setTimeInMillis(interm2.getTimeInMillis() + 1);
				interm2
					.setTimeInMillis(interm2.getTimeInMillis() + jourInMs);
			}
			
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(cal2)) {
				counts.add(echantillonDao
					.findCountSamplesByDatesExt(interm1, cal2, banks, exts)
					.get(0));
			}
		}
		
		return counts;
	}

	
	@Override
	public List<Long> countEchansByCessTypesManager(CessionType type,
			Date d1, Date d2, Integer interv, List<Banque> banks) {
		List<Long> counts = null;
		if (banks != null && !banks.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			Long jourInMs = ms * interv;
			Date interm1 = new Date(d1.getTime());
			// la borne supérieure devient exclusive si on retire 1 ms
			Date interm2 = new Date(d1.getTime() + jourInMs - 1);

			// ajoute l'intervalle pour réaliser chaque compte
			while (interm2.before(d2) || interm2.equals(d2)) {
				counts.add(echantillonDao
					.findCountEchansByCessTypes(type, interm1, interm2, banks)
						.get(0));
				interm1.setTime(interm2.getTime() + 1);
				interm2.setTime(interm2.getTime() + jourInMs);
			}
						
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(d2)) {
				counts.add(echantillonDao
						.findCountEchansByCessTypes(type, interm1, d2, banks)
						.get(0));
			}
		}
		return counts;
	}

	@Override
	public List<Long> countsPrelsAndAssociatesByCimManager(CimMaster code,
			String value,
			Calendar cal1, Calendar cal2, List<Banque> banks,
			List<Nature> sainTypes,
			List<EchantillonType> sainEchanTypes,
			List<Nature> sangTypes,
			List<EchantillonType> sangEchanTypes,
			List<ConsentType> consents) {

		List<Long> res = null;
		
		if (banks != null && !banks.isEmpty())  {
			res = new ArrayList<Long>();
			res.add(new Long(0));
			res.add(new Long(0));
			res.add(new Long(0));
			res.add(new Long(0));
			res.add(new Long(0));
			
			int tot;
			int totYear;
			
			// tissus sains/sang associés
			int totSains;
			int totSangs;
			int totConsents;
			
			Set<CodeCommon> codes = new LinkedHashSet<CodeCommon>();
			List<TableCodage> tables = new ArrayList<TableCodage>();
			List<BanqueTableCodage> btc;
			List<Banque> currBanque = new ArrayList<Banque>();
			for (int i = 0; i < banks.size(); i++) {
				currBanque.clear();
				currBanque.add(banks.get(i));
				tables.clear();
				btc = banqueManager
					.getBanqueTableCodageByBanqueManager(banks.get(i));			
				for (int j = 0; j < btc.size(); j++) {
					tables.add(btc.get(j).getTableCodage());
				}
				
				codes.clear();
				
				if (code != null) {
					// arborescence CIM
					Iterator<CimMaster> cimsIt = cimMasterManager
						.findChildrenCodesManager(code).iterator();
					CodeCommon next;
					while (cimsIt.hasNext()) {
						next = cimsIt.next();
						codes.add(next);
						// transcode
						codes.addAll(tableCodageManager
							.transcodeManager(next, tables, banks));
					}
				} else if (value != null) {
					// transcode
					codes.addAll(tableCodageManager
						.findCodesAndTranscodesFromStringManager(value, 
													tables, banks, true));

				}
				
				tot = 0;
				totYear = 0;
				
				// tissus sains/sang associés
				totSains = 0;
				totSangs = 0;
				totConsents = 0;
					
				if (!codes.isEmpty()) {
					
					Calendar ante = Calendar.getInstance();
					ante.setTimeInMillis(0);
				
					// récupère les prélèvements
					Set<Prelevement> prelsTot = 
						new HashSet<Prelevement>(prelevementDao
							.findByOrganeByDates(tableCodageManager
								.getListCodesFromCodeCommon(
										new ArrayList<CodeCommon>(codes)), 
									ante, Utils.getCurrentSystemCalendar(), 
											currBanque));
					prelsTot.addAll(prelevementDao
						.findByOrganeByDates(tableCodageManager
							.getListLibellesFromCodeCommon(
									new ArrayList<CodeCommon>(codes)), 
									ante, Utils.getCurrentSystemCalendar(), 
									currBanque));
					
					tot = prelsTot.size();
					
					// récupère les prélèvements
					Set<Prelevement> prels = 
						new HashSet<Prelevement>(prelevementDao
							.findByOrganeByDates(tableCodageManager
								.getListCodesFromCodeCommon(
										new ArrayList<CodeCommon>(codes)), 
													cal1, cal2, currBanque));
					prels.addAll(prelevementDao
						.findByOrganeByDates(tableCodageManager
							.getListLibellesFromCodeCommon(
									new ArrayList<CodeCommon>(codes)), 
													cal1, cal2, currBanque));
					
					// compte total
					totYear = prels.size();
					
					// tissus sains/sang associés
					Iterator<Prelevement> it = prels.iterator();
					Prelevement next;
					
					boolean countSainTypes = 
						(sainTypes != null && !sainTypes.isEmpty());
					boolean countSainEchanTypes = 
						(sainEchanTypes != null && !sainEchanTypes.isEmpty());
					boolean countSangTypes = 
						(sangTypes != null && !sangTypes.isEmpty());
					boolean countSangEchanTypes = 
						(sangEchanTypes != null && !sangEchanTypes.isEmpty());
					boolean countConsents = 
						(consents != null && !consents.isEmpty());
					
					while (it.hasNext()) {
						next = it.next();
						if ((countSainTypes && prelevementDao
							.findAssociatePrelsOfType(next.getMaladie(), 
									sainTypes, banks).size() > 0)
							|| (countSainEchanTypes && echantillonDao
								.findAssociateEchansOfType(next.getMaladie(), 
									sainEchanTypes, banks, next).size() > 0)) {
							totSains++;
						}
						if ((countSangTypes && prelevementDao
								.findAssociatePrelsOfType(next.getMaladie(), 
										sangTypes, banks).size() > 0)
								|| (countSangEchanTypes && echantillonDao
								.findAssociateEchansOfType(next.getMaladie(), 
									sangEchanTypes, banks, next).size() > 0)) {
								totSangs++;
						}
						if (countConsents
								&& consents.contains(next.getConsentType())) {
							totConsents++;
						}
					}
				}
				tot = res.get(0).intValue() + tot;
				totYear = res.get(1).intValue() + totYear;
				totSains = res.get(2).intValue() + totSains;
				totSangs = res.get(3).intValue() + totSangs;
				totConsents = res.get(4).intValue() + totConsents;
				res.clear();
				res.add(new Long(tot));
				res.add(new Long(totYear));
				res.add(new Long(totSains));
				res.add(new Long(totSangs));
				res.add(new Long(totConsents));
			}
		}
		
		// passage en pourcentages
		if (res != null && !res.isEmpty() && res.get(1) > 0) {
			res.set(2, (res.get(2).intValue() * 100) / res.get(1));
			res.set(3, (res.get(3).intValue() * 100) / res.get(1));
			res.set(4, (res.get(4).intValue() * 100) / res.get(1));
		}
			
		return res;
	}

	@Override
	public List<Long> countEclConsentByDatesManager(List<ConsentType> types,
			Calendar cal1, Calendar cal2, Integer interv, List<Banque> banks) {
		
		List<Long> counts = null;
		if (banks != null && !banks.isEmpty() 
										&& types != null && !types.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			
			Long jourInMs = ms * interv;
			
			Calendar interm1 = Calendar.getInstance();
			interm1.setTimeInMillis(cal1.getTimeInMillis());
			Calendar interm2 = Calendar.getInstance();
			// la borne supérieure devient exclusive si on retire 1 ms
			interm2.setTimeInMillis(cal1.getTimeInMillis() 
												+ jourInMs - 1);
			
			// ajoute l'intervalle pour réaliser chaque compte
			while (interm2.before(cal2) || interm2.equals(cal2)) {			
				counts.add(prelevementDao
					.findCountEclConsentByDates(types, interm1, interm2, banks)
					.get(0));
				interm1.setTimeInMillis(interm2.getTimeInMillis() + 1);
				interm2
					.setTimeInMillis(interm2.getTimeInMillis() + jourInMs);
			}
			
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(cal2)) {
				counts.add(prelevementDao
					.findCountEclConsentByDates(types, interm1, cal2, banks)
					.get(0));
			}
		}
		
		return counts;
	}

	@Override
	public List<Long> countPrelevedByDatesExtManager(Calendar cal1,
			Calendar cal2, Integer interv, List<Banque> banks,
			List<Etablissement> exts, boolean datePrel) {
		List<Long> counts = null;
		if (banks != null && !banks.isEmpty() 
								&& exts != null && !exts.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			
			Long jourInMs = ms * interv;
			
			Calendar interm1 = Calendar.getInstance();
			interm1.setTimeInMillis(cal1.getTimeInMillis());
			Calendar interm2 = Calendar.getInstance();
			// la borne supérieure devient exclusive si on retire 1 ms
			interm2.setTimeInMillis(cal1.getTimeInMillis() 
												+ jourInMs - 1);
			
			// ajoute l'intervalle pour réaliser chaque compte
			while (interm2.before(cal2) || interm2.equals(cal2)) {	
				if (!datePrel) {
					counts.add(patientDao
					.findCountPrelevedByDatesSaisieExt(interm1, interm2, 
															banks, exts)
						.get(0));
				} else {
					counts.add(patientDao
					.findCountPrelevedByDatesPrelExt(interm1, interm2, 
															banks, exts)
						.get(0));
				}
				interm1.setTimeInMillis(interm2.getTimeInMillis() + 1);
				interm2
					.setTimeInMillis(interm2.getTimeInMillis() + jourInMs);
			}
			
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(cal2)) {
				if (!datePrel) {
					counts.add(patientDao
					.findCountPrelevedByDatesSaisieExt(interm1, cal2, 
																banks, exts)
					.get(0));
				} else {
					counts.add(patientDao
					.findCountPrelevedByDatesPrelExt(interm1, cal2, banks, exts)
					.get(0));
				}
			}
		}
		
		return counts;
	}
	
	@Override
	public List<Long> countPrelevedByDatesManager(Calendar cal1, Calendar cal2,
			Integer interv, List<Banque> banks, boolean cumulative, 
			boolean datePrel) {
		List<Long> counts = null;
		
		if (banks != null && !banks.isEmpty()) {
			counts = new ArrayList<Long>();
			long ms = 86400000;
			Long jourInMs = ms * interv;
			Calendar interm1 = Calendar.getInstance();
			interm1.setTimeInMillis(cal1.getTimeInMillis());
			Calendar interm2 = Calendar.getInstance();
			// la borne supérieure devient exclusive si on retire 1 ms
			interm2.setTimeInMillis(cal1.getTimeInMillis() + jourInMs
					- 1);
			// ajoute l'intervalle pour réaliser chaque compte
			
			long ant = 0;
			// compte de toute l'ancienneté des échantillons
			if (cumulative) {
				try {
					Calendar calOrigin = Calendar.getInstance();
					calOrigin.setTime(new SimpleDateFormat("dd/MM/yyyy")
														.parse("01/01/0001"));
					if (!datePrel) {
						ant = patientDao
							.findCountPrelevedByDatesSaisie(calOrigin, 
													interm1, banks).get(0);
					} else {
						ant = patientDao
							.findCountPrelevedByDatesPrel(calOrigin, 
												interm1, banks).get(0);
					}
				} catch (ParseException e) {
					log.error(e);
				} 		
			}
			
			Long ct;
			while (interm2.before(cal2) || interm2.equals(cal2)) {
				if (!datePrel) {
					ct = patientDao
						.findCountPrelevedByDatesSaisie(interm1, interm2, banks)
							.get(0);
				} else {
					ct = patientDao
					.findCountPrelevedByDatesPrel(interm1, interm2, banks)
						.get(0);
				}
				if (!cumulative) {
					counts.add(ct);
				} else {
					if (counts.isEmpty()) {
						counts.add(ant + ct);
					} else {
						counts.add(counts.get(counts.size() - 1) + ct);
					}
				}
				interm1.setTimeInMillis(interm2.getTimeInMillis() + 1);
				interm2.setTimeInMillis(interm2.getTimeInMillis() + jourInMs);
			}
			// ajoute le dernier intervalle au besoin pour compléter
			if (interm1.before(cal2)) {
				if (!datePrel) {
					ct = patientDao
						.findCountPrelevedByDatesSaisie(interm1, cal2, banks)
						.get(0);
				} else {
					ct = patientDao
						.findCountPrelevedByDatesPrel(interm1, cal2, banks)
						.get(0);
				}
				if (!cumulative || counts.isEmpty()) {
					counts.add(ct);
				} else {
					counts.add(counts.get(counts.size() - 1) + ct);
				}
			}
		}
		return counts;
	}

}
