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
package fr.aphp.tumorotek.manager.impl.procedure;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.procedure.ProcedureManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.PatientLien;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ProcedureManagerImpl implements ProcedureManager {
	
	private PatientManager patientManager;
	private MaladieDao maladieDao;
	private MaladieManager maladieManager;
	private PrelevementDao prelevementDao;
	private AnnotationValeurManager annotationValeurManager;
	private PatientDao patientDao;
	private ImportHistoriqueManager importHistoriqueManager;
	private OperationManager operationManager;
	
	public void setPatientManager(PatientManager pManager) {
		this.patientManager = pManager;
	}

	public void setMaladieDao(MaladieDao mDao) {
		this.maladieDao = mDao;
	}

	public void setMaladieManager(MaladieManager mManager) {
		this.maladieManager = mManager;
	}

	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setAnnotationValeurManager(
			AnnotationValeurManager aManager) {
		this.annotationValeurManager = aManager;
	}

	public void setPatientDao(PatientDao pDao) {
		this.patientDao = pDao;
	}

	public void setImportHistoriqueManager(
			ImportHistoriqueManager iManager) {
		this.importHistoriqueManager = iManager;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	public ProcedureManagerImpl() {
		
	}

	@Override
	public void fusionPatientForHematoManager(
			Patient patient, Patient passif, 
			Utilisateur u, String comments) {

		if (patient != null && passif != null) {		

			// recuperation des medecins referents
			List<Collaborateur> medsP = patientManager
				.getMedecinsManager(passif);
			List<Collaborateur> medsA = patientManager
				.getMedecinsManager(patient);
			// ajoute a la liste les nouveaux medecins
			for (int i = 0; i < medsP.size(); i++) {
				if (!medsA.contains(medsP.get(i))) {
					medsA.add(medsP.get(i));
				}
			}

			// recuperation des liens familiaux
			List<PatientLien> liensP = new 
			ArrayList<PatientLien>(patientManager
						.getPatientLiensManager(passif));
			List<PatientLien> liensA = new 
			ArrayList<PatientLien>(patientManager
					.getPatientLiensManager(patient));
			// ajoute a la liste les nouveaux liens
			for (int i = 0; i < liensP.size(); i++) {
				liensP.get(i).getPk().setPatient1(patient);
				if (!liensA.contains(liensP.get(i))) {
					liensA.add(liensP.get(i));
				}
			}

			// gestion des annotations
			List<AnnotationValeur> valeursActives = annotationValeurManager
			.findByObjectManager(patient);
			List<AnnotationValeur> valeursPassives = annotationValeurManager
			.findByObjectManager(passif);
			Hashtable<ChampAnnotation, AnnotationValeur> champsValeurs = 
				new Hashtable<ChampAnnotation, AnnotationValeur>();
			// on classe les valeurs actives en fct de leur champ
			for (int i = 0; i < valeursActives.size(); i++) {
				champsValeurs.put(valeursActives.get(i).getChampAnnotation(), 
						valeursActives.get(i));
			}
			// liste des annotations du passif à conserver
			List<AnnotationValeur> valeursAConserver = 
				new ArrayList<AnnotationValeur>();
			// liste des annotations du passif à supprimer
			List<AnnotationValeur> valeursASupprimer = 
				new ArrayList<AnnotationValeur>();
			for (int i = 0; i < valeursPassives.size(); i++) {
				// si le patient actif a déjà une annotation pour le
				// champ de l'annotation du passif
				if (champsValeurs.containsKey(valeursPassives.get(i)
						.getChampAnnotation())) {
					// si cette valeur est pour la même collection : 
					// on va supprimer cette annotation
					if (valeursPassives.get(i).getBanque().equals(
							champsValeurs.get(valeursPassives.get(i)
									.getChampAnnotation()).getBanque())) {
						valeursASupprimer.add(valeursPassives.get(i));
					} else {
						valeursAConserver.add(valeursPassives.get(i));
					}
				} else {
					valeursAConserver.add(valeursPassives.get(i));
				}
			}

			// mise a jour du Patient actif
			patientManager.createOrUpdateObjectManager(
					patient, null, medsA, liensA, 
					valeursAConserver, 
					null, null, null, u, "fusion", null, false);


			// recuperation des maladies
			Set<Maladie> malP = maladieManager.getMaladiesManager(passif);
			List<Maladie> malA = new ArrayList<Maladie>(maladieManager
					.getMaladiesManager(patient));
			// Ajoute la maladie si n'existe pas sinon ajoute ses prels
			Iterator<Prelevement> prelsIt;
			List<Maladie> malsToRemove = new ArrayList<Maladie>();
			Maladie maladie;
			Prelevement prel;
			Iterator<Maladie> malIt = malP.iterator();
			while (malIt.hasNext()) {
				maladie = malIt.next();
				prelsIt = maladieManager
				.getPrelevementsManager(maladie).iterator();
				maladie.setPatient(patient); // pour appliquer equals()
				if (!malA.contains(maladie)) { // ajoute la maladie
					
					// si la maladie passive est Inconnu, on va 
					// la supprimer
					if (maladie.getLibelle().equals("Inconnu")) {
						maladie.setPatient(passif); //recupere son patient
						while (prelsIt.hasNext()) {
							prel = prelsIt.next();
							prel.setMaladie(malA.get(0));
							prelevementDao.updateObject(prel);
						}
						maladie.getPrelevements().clear();
						malsToRemove.add(maladie);
					} else if (malA.get(0).getLibelle().equals("Inconnu")) {
						// sinon, si c'est la madie active qui était Inconnu
						// on va transférer les infos
						malA.get(0).setLibelle(maladie.getLibelle());
						malA.get(0).setCode(maladie.getCode());
						malA.get(0).setDateDebut(maladie.getDateDebut());
						malA.get(0).setDateDiagnostic(
								maladie.getDateDiagnostic());
						
						maladieDao.updateObject(malA.get(0));
						
						// suppression maladie passive
						maladie.setPatient(passif); //recupere son patient
						while (prelsIt.hasNext()) {
							prel = prelsIt.next();
							prel.setMaladie(malA.get(0));
							prelevementDao.updateObject(prel);
						}
						maladie.getPrelevements().clear();
						malsToRemove.add(maladie);
					} else {
						maladieDao.updateObject(maladie);
					}
					
				} else { // ajoute prelevements a la maladie existante
					int index = malA.indexOf(maladie);
					maladie.setPatient(passif); //recupere son patient
					while (prelsIt.hasNext()) {
						prel = prelsIt.next();
						prel.setMaladie(malA.get(index));
						prelevementDao.updateObject(prel);
					}
					maladie.getPrelevements().clear();
					malsToRemove.add(maladie);
				}
			}


			// fantomization (oh le beau mot) du passif
			for (int i = 0; i < malsToRemove.size(); i++) {
				maladieManager.removeObjectManager(malsToRemove.get(i), 
						comments, u);
				//passif.getMaladies().remove(malsToRemove.get(i));
			}

			// remove patient et objets associes
			patientDao.removeObject(passif.getPatientId());
			//Supprime operations associes
			CreateOrUpdateUtilities.
			removeAssociateOperations(passif, operationManager, 
					comments, u);

			//Supprime importations associes
			CreateOrUpdateUtilities.
				removeAssociateImportations(passif, importHistoriqueManager);
			
			List<File> filesToDelete = new ArrayList<File>();

			//Supprime annotations associes
			annotationValeurManager
			.removeAnnotationValeurListManager(valeursASupprimer, filesToDelete);
		}
	}

}
