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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.aphp.tumorotek.dao.interfacage.PatientSipDao;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.interfacage.InterfacageParsingUtils;
import fr.aphp.tumorotek.manager.interfacage.PatientSipManager;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * Implémentation du manager du bean de domaine PatientSip.
 * Classe créée le 02/05/11.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.13.3
 *
 */
public class PatientSipManagerImpl implements PatientSipManager {

	private Log log = LogFactory.getLog(PatientSipManager.class);
	
	private PatientSipDao patientSipDao;
	private PatientManager patientManager;
	
	private InterfacageParsingUtils interfacageParsingUtils;
	
	public void setPatientSipDao(PatientSipDao pDao) {
		this.patientSipDao = pDao;
	}

	public void setPatientManager(PatientManager pManager) {
		this.patientManager = pManager;
	}
	
	public void setInterfacageParsingUtils( InterfacageParsingUtils iU) {
		this.interfacageParsingUtils = iU;
	}


	public PatientSipManagerImpl() {
	}
	
	@Override
	public List<PatientSip> findAllObjectsManager() {
		log.debug("Recherche totalite des Patients Sip");
		return patientSipDao.findAll();
	}

	@Override
	public List<PatientSip> findByNipLikeManager(String nip, 
												boolean exactMatch) {
		if (!exactMatch) {
			nip = "%" + nip + "%";
		}
		log.debug("Recherche Patient Sip par nip: " 
				+ nip + " exactMatch " + String.valueOf(exactMatch));
		return patientSipDao.findByNip(nip);
	}

	@Override
	public List<PatientSip> findByNomLikeManager(String nom,
												boolean exactMatch) {
		if (!exactMatch) {
			nom = "%" + nom + "%";
		}
		log.debug("Recherche Patient Sip par nom: " 
				+ nom + " exactMatch " + String.valueOf(exactMatch));
		return patientSipDao.findByNom(nom);
	}
	
	@Override
	public List<PatientSip> findByNumeroSejourManager(String numero,
												boolean exactMatch) {
		if (!exactMatch) {
			numero = "%" + numero + "%";
		}
		return patientSipDao.findByNumeroSejour(numero);
	}

	@Override
	public boolean findDoublonManager(PatientSip patient) {
		return !patientSipDao.findByNip(patient.getNip()).isEmpty();	
	}

	@Override
	public List<Field> isSynchronizedPatientManager(PatientSip sip) {
		List<Field> fields = new ArrayList<Field>();
		if (sip != null) {
			// retrouve la patient par son nip
			List<PatientSip> pats = findByNipLikeManager(sip.getNip(), true);
			if (!pats.isEmpty()) {
				try {
					PatientSip inBase = pats.get(0);
					if (!sip.getNom().equals(inBase.getNom())) {
						fields.add(inBase.getClass()
								.getDeclaredField("nom"));
					}
					if ((sip.getNomNaissance() != null
							&& !sip.getNomNaissance()
								.equals(inBase.getNomNaissance())) 
						|| (sip.getNomNaissance() == null
								&& inBase.getNomNaissance() != null)) {
						fields.add(inBase.getClass()
										.getDeclaredField("nomNaissance"));
					}
					if (!sip.getPrenom().equals(inBase.getPrenom())) {
						fields.add(inBase.getClass()
								.getDeclaredField("prenom"));
					}
					if (!sip.getDateNaissance()
									.equals(inBase.getDateNaissance())) {
						fields.add(inBase.getClass()
								.getDeclaredField("dateNaissance"));
					}
					if (!sip.getSexe().equals(inBase.getSexe())) {
						fields.add(inBase.getClass()
								.getDeclaredField("sexe"));
					}
					if (sip.getVilleNaissance() != null
							&& !sip.getVilleNaissance()
									.equals(inBase.getVilleNaissance())) {
						fields.add(inBase.getClass()
								.getDeclaredField("villeNaissance"));
					}				
					if (sip.getPaysNaissance() != null
								&& !sip.getPaysNaissance()
									.equals(inBase.getPaysNaissance())) {
						fields.add(inBase.getClass()
								.getDeclaredField("paysNaissance"));
					}
					if (!sip.getPatientEtat()
									.equals(inBase.getPatientEtat())) {
						fields.add(inBase.getClass()
								.getDeclaredField("patientEtat"));
					}
					if (sip.getDateEtat() != null
								&& !sip.getDateEtat()
										.equals(inBase.getDateEtat())) {
						fields.add(inBase.getClass()
								.getDeclaredField("dateEtat"));
					}
					if (sip.getDateDeces() != null
							&& !sip.getDateDeces()
										.equals(inBase.getDateDeces())) {
						fields.add(inBase.getClass()
								.getDeclaredField("dateDeces"));
					}
					// MaJ dateDeces à partir fusion dateDeces Passif
					// si dateDeces is null
					if (sip.getDateDecesP() != null
							&& sip.getDateDeces() == null) { 
						fields.add(inBase.getClass()
								.getDeclaredField("dateDecesP"));
					}
				} catch (NoSuchFieldException ne) {
					log.error(ne.getMessage());
				}
			}
		}
		return fields;
	}

	@Override
	public void removeObjectManager(PatientSip patient) {
		if (patient != null) {
			patientSipDao.removeObject(patient.getPatientSipId());
				log.info("Suppression objet Patient " + patient.toString());
		} else {
			log.warn("Suppression d'un Patient null");
		}
	}

	@Override
	public Patient doSynchronizePatientManager(PatientSip sipPatient) {
		if (sipPatient != null) {
			// check si le patient existe dans la base
			List<Patient> pats = patientManager
							.findByNipLikeManager(sipPatient.getNip(), true);	
			if (!pats.isEmpty()) {
				if (pats.size() == 1) {
					return pats.get(0);
				} else { // message non traité
					log.error("Synchronization SIP impossible! " 
						+ "Plusieurs patients "
						+ "sont enregistrés dans le système pour ce nip " 
						+ sipPatient.getNip());
				}
			}
		}
		return null;
	}

	@Override
	public void createOrUpdatePatientInTempTableManager(PatientSip sipPatient, 
														int max, boolean isFusion) {
		if (sipPatient != null) {
		
			if (!findDoublonManager(sipPatient)) {
				
				// récupération dateDeces patient fusionné ssi pas date décès enregistréé!
				if (isFusion && sipPatient.getDateDeces() == null 
						&& sipPatient.getDateDecesP() != null) {
					sipPatient.setDateDeces(sipPatient.getDateDecesP());
					sipPatient.setPatientEtat(sipPatient.getDateDeces() != null ? "D" : "V");
				}
				
				if (sipPatient.getDateCreation() == null) {
					sipPatient.setDateCreation(Utils.getCurrentSystemCalendar());
				}
								
				patientSipDao.createObject(sipPatient);
				log.info("Ajout du patient "
						+ sipPatient.getNip() + " a la base temporaire");
				
				if (patientSipDao.findCountAll().get(0) > max) {
					List<PatientSip> sips = patientSipDao.findFirst();
					if (!sips.isEmpty()) {
						patientSipDao.removeObject(sips.get(0).getPatientSipId());
						log.debug("Suppression FIRST IN "
							+ sips.get(0).getNip() + " pour maintenir la taille "
								+ " de la table temporaire à " + max);
					}
				}
				
			} else { 
				updatePatientInTempTableManager(sipPatient, isFusion);
			}
		}
	}

	@Override
	public void updatePatientInTempTableManager(PatientSip sipPatient, boolean isFusion) {
		List<Field> fields = isSynchronizedPatientManager(sipPatient); 
		
		try {
			PatientSip local = 
				patientSipDao.findByNip(sipPatient.getNip()).get(0);
			if (!fields.isEmpty()) { // synchronisation
				String fieldsNames = "";
				Iterator<Field> fieldsIt = fields.iterator();
				String fieldName;
				while (fieldsIt.hasNext()) {
					fieldName = ((Field) fieldsIt.next()).getName();
					
					if (isFusion && fieldName.equals("patientEtat")) { // do nothing
					} else if (fieldName.equals("dateDecesP") && local.getDateDeces() == null) {
						local.setDateDeces(sipPatient.getDateDecesP());
						local.setPatientEtat(local.getDateDeces() != null ? "D" : "V");
					} else {
						PropertyUtils.setSimpleProperty(local, fieldName, 
								PropertyUtils.getSimpleProperty(sipPatient, fieldName));
					}
					
					fieldsNames = fieldsNames + fieldName;
					if (fieldsIt.hasNext()) {
						fieldsNames = fieldsNames + ", ";
					}
				}
				log.info("Mise à jour du patient dans la table temporaire " 
						+ sipPatient.getNip() + " pour les champs: " + fieldsNames);
			}
			
			for (PatientSipSejour sj : sipPatient.getSejours()) {
				sj.setPatientSip(local);
				local.getSejours().add(sj);
			}
			local.setDateModification(Utils.getCurrentSystemCalendar());
				
			// update
			patientSipDao.updateObject(local);
				
				
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		} catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}	
	}

	@Override
	public void updatePatientSystem(PatientSip sipPatient, Patient pat) {
		List<Field> fields = patientManager
						.isSynchronizedPatientManager(sipPatient, pat); 
		
		if (!fields.isEmpty()) { // synchronisation
			try {
				String fieldsNames = "";
				Iterator<Field> fieldsIt = fields.iterator();
				String fieldName;
				while (fieldsIt.hasNext()) {
					fieldName = ((Field) fieldsIt.next()).getName();
					PropertyUtils.setSimpleProperty(pat, fieldName, 
						PropertyUtils.getSimpleProperty(sipPatient, fieldName));
					
					fieldsNames = fieldsNames + fieldName;
					if (fieldsIt.hasNext()) {
						fieldsNames = fieldsNames + ", ";
					}
				}
				
				// update
				patientManager
					.createOrUpdateObjectManager(pat, null, null, null, null, 
							null, 
							null, null, 
							null, "synchronisation", null, false);
				
				log.info("Synchronisation du patient " + sipPatient.getNip()
					+ " pour les champs: " + fieldsNames);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage());
			} catch (NoSuchMethodException e) {
				log.error(e.getMessage());
			} catch (InvocationTargetException e) {
				log.error(e.getMessage());
			}
		} else {
			log.debug("Aucun changement impliquant la synchronisation "
					+ "du patient avec celui venant du SIP");
		}
	}
	
	@Override
	public void fusionPatientSystemManager(Patient actif, Patient passif) {
		patientManager.fusionPatientManager(actif, passif, null, 
				"fusion avec " + actif.getNip() + " déclenchée par le SIP");
	}
	
	@Override
	public PatientSip parseHl7MessagePID(String message) throws IOException {
		
		ConfigurationParsing config = new ConfigurationParsing();
		config.setSeparateurChamps("|");
		config.setSeparateurComposants("^");
		config.setSeparateurSousComposants("~");
		
		
		// on parse le message pour le transformer en hashtable
		Hashtable<String, List<String>> contenu = interfacageParsingUtils
									.parseFileToInjectInTk(config, message);
		
		// init du PatientSip à remplir
		PatientSip pSip = new PatientSip();
		
		pSip.setNip(interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "3.1"));
		
		// since 2.0.10.6
		// extraction différents noms depuis bloc PID-5
		String familyName = null;
		String maidenName = null;
		String usualName = null;
		
		String pid5 = interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "5");
		List<String> names = Arrays
				.asList(pid5.split(config.getSeparateurSousComposants()));
		
		String[] splt;
		String nameType;
		for (String st : names) {
			splt = st.split("\\" + config.getSeparateurComposants());
			if (splt.length == 7) {
				nameType = splt[6];
				if (nameType != null) { 
					if (nameType.equals("L")) {
						familyName = splt[0];
					} else if (nameType.equals("D")) {
						usualName = splt[0];
					} else if (nameType.equals("M")) {
						maidenName = splt[0];
					}
				}
			}
		}
		
		// il faut au moins un nom usuel
		if (usualName != null && !usualName.trim().equals("")) {
			pSip.setNom(usualName);
		} else if (familyName != null && !familyName.trim().equals("")) {
			pSip.setNom(familyName);
		} else {
			pSip.setNom(interfacageParsingUtils
					.getValueFromBlocAndEmplacement(contenu, config, "PID", "5.1"));
		}
		// nom naissoance
		if (maidenName != null && !maidenName.trim().equals("")) {
			pSip.setNomNaissance(maidenName);
		} else if (familyName != null && !familyName.trim().equals("") 
				&& !familyName.equals(pSip.getNom())) {
			pSip.setNomNaissance(familyName);
		}
		
		pSip.setPrenom(interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "5.2"));
	
		pSip.setDateNaissance(interfacageParsingUtils
				.parseHl7Date(interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "7")));
			
		String sexe = interfacageParsingUtils
					.getValueFromBlocAndEmplacement(contenu, config, "PID", "8");
		if (sexe != null && sexe.matches("F|M")) {
			pSip.setSexe(sexe);
		} else {
			pSip.setSexe("Ind");
		}
		
		// ville pays naissance?
		
		pSip.setDateDeces(interfacageParsingUtils
				.parseHl7Date(interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "29")));
		
		String etat = interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "PID", "30");
		
		if ((etat != null && etat.equals("Y")) || pSip.getDateDeces() != null) {
			pSip.setPatientEtat("D");
		} else {
			pSip.setPatientEtat("V");
		}
		
		// numero de sejour
		PatientSipSejour sj = new PatientSipSejour();
		sj.setNumero(interfacageParsingUtils
			.getValueFromBlocAndEmplacement(contenu, config, "PV1", "19.1"));
		// si le segment existe
		if (sj.getNumero() != null) {
			sj.setDateSejour(interfacageParsingUtils
					.parseHl7Date(interfacageParsingUtils
					.getValueFromBlocAndEmplacement(contenu, config, "PV1", "44")));
			sj.setPatientSip(pSip);
			pSip.getSejours().add(sj);
		}

		return pSip;
	}
	
	@Override
	public PatientSip parseHl7MessageMRG(String message) throws IOException {
		
		ConfigurationParsing config = new ConfigurationParsing();
		config.setSeparateurChamps("|");
		config.setSeparateurComposants("^");
		config.setSeparateurSousComposants("~");
		
		
		// on parse le message pour le transformer en hashtable
		Hashtable<String, List<String>> contenu = interfacageParsingUtils
									.parseFileToInjectInTk(config, message);
		
		// init du PatientSip passif à remplir
		PatientSip pSip = new PatientSip();
		
		pSip.setNip(interfacageParsingUtils
				.getValueFromBlocAndEmplacement(contenu, config, "MRG", "1.1"));

		return pSip;
	}
}
