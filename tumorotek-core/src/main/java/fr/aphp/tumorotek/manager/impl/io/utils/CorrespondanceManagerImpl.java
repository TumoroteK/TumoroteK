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
package fr.aphp.tumorotek.manager.impl.io.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.CessionManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceManager;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.systeme.Entite;

public class CorrespondanceManagerImpl implements CorrespondanceManager {

	/* Beans injectes par Spring */
	private MaladieManager maladieManager = null;
	private PrelevementManager prelevementManager = null;
	private EchantillonManager echantillonManager = null;
	private ProdDeriveManager prodDeriveManager = null;
	private EntiteManager entiteManager = null;
	private CessionManager cessionManager = null;
	private CederObjetManager cederObjetManager = null;

	public CorrespondanceManagerImpl() {
	}

	public void setMaladieManager(MaladieManager malManager) {
		this.maladieManager = malManager;
	}

	public void setPrelevementManager(PrelevementManager preManager) {
		this.prelevementManager = preManager;
	}

	public void setEchantillonManager(EchantillonManager echManager) {
		this.echantillonManager = echManager;
	}

	public void setProdDeriveManager(ProdDeriveManager pdManager) {
		this.prodDeriveManager = pdManager;
	}

	public void setEntiteManager(EntiteManager entManager) {
		this.entiteManager = entManager;
	}

	public void setCessionManager(CessionManager cManager) {
		this.cessionManager = cManager;
	}

	public void setCederObjetManager(CederObjetManager cedObjManager) {
		this.cederObjetManager = cedObjManager;
	}

	/**
	 * Recupere une liste d'objets depuis une liste d'autres Entites que celui
	 * de la liste d'objets sources (d'un autre typage).
	 * 
	 * @param sources
	 *            Liste d'Entites sources.
	 * @param cible
	 *            Nom de la classe paramètre de la liste destinatrice.
	 * @throws IllegalArgumentException
	 *             si le paramètre de la liste d'Entites ou le paramètre "cible"
	 *             n'est pas égale à Patient, Maladie, Prelevement, Echantillon
	 *             ou ProdDerive
	 */
	@Override
	public List<Object> recupereEntitesViaDAutres(List<Object> sources,
			String cible) {
		List<Object> retours = new ArrayList<Object>();
		if (sources != null) {
			if (sources.size() > 0) {
				if (sources.get(0).getClass().getSimpleName().equals("Patient")) {
					if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByPatients(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByPatients(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByPatients(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByPatients(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByPatients(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByPatients(sources));
					} else {
						throw new IllegalArgumentException();
					}
				} else if (sources.get(0).getClass().getSimpleName().equals(
						"Maladie")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByMaladies(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByMaladies(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByMaladies(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByMaladies(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByMaladies(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByMaladies(sources));
					} else {
						throw new IllegalArgumentException();
					}
				// 2.0.10.3 Hibernate getClass car lazy loading Echantillon.getPrelevement
				// -> class.getName = proxy (prelevement.javassist)
				} else if (Hibernate.getClass(sources.get(0)).getSimpleName().equals(
						"Prelevement")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByPrelevements(sources));
					} else if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByPrelevements(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByPrelevements(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByPrelevements(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByPrelevements(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByPrelevements(sources));
					} else {
						throw new IllegalArgumentException();
					}
				} else if (sources.get(0).getClass().getSimpleName().equals(
						"Echantillon")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByEchantillons(sources));
					} else if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByEchantillons(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByEchantillons(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByEchantillons(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByEchantillons(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByEchantillons(sources));
					} else {
						throw new IllegalArgumentException();
					}
				} else if (sources.get(0).getClass().getSimpleName().equals(
						"ProdDerive")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByProdDerives(sources));
					} else if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByProdDerives(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByProdDerives(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByProdDerives(sources));
					} else if (cible.equals("ProdDerive")) {
						// Un ProdDerive peut en dériver d'autres.
						retours
								.addAll(findProdDeriveEnfantByProdDerives(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByProdDerives(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByProdDerives(sources));
					} else {
						throw new IllegalArgumentException();
					}
				} else if (sources.get(0).getClass().getSimpleName().equals(
						"Cession")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByCessions(sources));
					} else if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByCessions(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByCessions(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByCessions(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByCessions(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(findCederObjetByCessions(sources));
					} else {
						throw new IllegalArgumentException();
					}
				} else if (sources.get(0).getClass().getSimpleName().equals(
						"CederObjet")) {
					if (cible.equals("Patient")) {
						retours.addAll(findPatientByCederObjets(sources));
					} else if (cible.equals("Maladie")) {
						retours.addAll(findMaladieByCederObjets(sources));
					} else if (cible.equals("Prelevement")) {
						retours.addAll(findPrelevementByCederObjets(sources));
					} else if (cible.equals("Echantillon")) {
						retours.addAll(findEchantillonByCederObjets(sources));
					} else if (cible.equals("ProdDerive")) {
						retours.addAll(findProdDeriveByCederObjets(sources));
					} else if (cible.equals("Cession")) {
						retours.addAll(findCessionByCederObjets(sources));
					} else if (cible.equals("CederObjet")) {
						retours.addAll(sources);
					} else {
						throw new IllegalArgumentException();
					}
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
		return retours;
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByMaladies(List<Object> sources) {
		List<Object> patients = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Maladie maladie = (Maladie) it1.next();
			if (maladie != null) {
				if (maladie.getPatient() != null
						&& !patients.contains(maladie.getPatient())) {
					patients.add(maladie.getPatient());
				}
			}
		}
		return patients;
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste de Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByPrelevements(List<Object> sources) {
		return findPatientByMaladies(findMaladieByPrelevements(sources));
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste d'Echantillons.
	 * 
	 * @param sources
	 *            Liste de Echantillons.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByEchantillons(List<Object> sources) {
		return findPatientByPrelevements(findPrelevementByEchantillons(sources));
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste de ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByProdDerives(List<Object> sources) {
		return findPatientByPrelevements(findPrelevementByProdDerives(sources));
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByCessions(List<Object> sources) {
		return findPatientByPrelevements(findPrelevementByCessions(sources));
	}

	/**
	 * Renvoie une liste de Patients correspondant à une liste de CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste de Patients correspondante.
	 */
	private List<Object> findPatientByCederObjets(List<Object> sources) {
		return findPatientByPrelevements(findPrelevementByCederObjets(sources));
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByPatients(List<Object> sources) {
		List<Object> maladies = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Patient patient = (Patient) it1.next();
			if (patient != null) {
				// On récupère les maladies du patient
				Iterator<Maladie> it2 = maladieManager.getMaladiesManager(
						patient).iterator();
				while (it2.hasNext()) {
					Maladie maladie = it2.next();
					if (maladie != null) {
						// On ajoute la maladie dans la liste si elle n'y est
						// pas déjà.
						if (!maladies.contains(maladie)) {
							maladies.add(maladie);
						}
					}
				}
			}
		}
		return maladies;
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste de Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByPrelevements(List<Object> sources) {
		List<Object> maladies = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Prelevement prelevement = (Prelevement) it1.next();
			if (prelevement != null) {
				// On récupère la maladie du prélèvement
				Maladie maladie = prelevement.getMaladie();
				// On ajoute la maladie dans la liste si elle n'y est
				// pas déjà.
				if (maladie != null && !maladies.contains(maladie)) {
					maladies.add(maladie);
				}
			}
		}
		return maladies;
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste d'Echantillons.
	 * 
	 * @param sources
	 *            Liste d'Echantillons.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByEchantillons(List<Object> sources) {
		return findMaladieByPrelevements(findPrelevementByEchantillons(sources));
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste de ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByProdDerives(List<Object> sources) {
		return findMaladieByPrelevements(findPrelevementByProdDerives(sources));
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByCessions(List<Object> sources) {
		return findMaladieByPrelevements(findPrelevementByCessions(sources));
	}

	/**
	 * Renvoie une liste de Maladies correspondant à une liste de CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste de Maladies correspondante.
	 */
	private List<Object> findMaladieByCederObjets(List<Object> sources) {
		return findMaladieByPrelevements(findPrelevementByCederObjets(sources));
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByPatients(List<Object> sources) {
		return findPrelevementByMaladies(findMaladieByPatients(sources));
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByMaladies(List<Object> sources) {
		List<Object> prelevements = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Maladie maladie = (Maladie) it1.next();
			if (maladie != null) {
				// On récupère les prélèvements de la maladie.
				Iterator<Prelevement> it2 = maladieManager
						.getPrelevementsManager(maladie).iterator();
				while (it2.hasNext()) {
					Prelevement prelevement = it2.next();
					if (prelevement != null) {
						// On ajoute le prélèvement dans la liste si il n'y est
						// pas déjà.
						if (!prelevements.contains(prelevement)) {
							prelevements.add(prelevement);
						}
					}
				}
			}
		}
		return prelevements;
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste
	 * d'Echantillons.
	 * 
	 * @param sources
	 *            Liste d'Echantillons.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByEchantillons(List<Object> sources) {
		List<Object> prelevements = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Echantillon echantillon = (Echantillon) it1.next();
			if (echantillon != null) {
				// On récupère le prélèvement de chaque échantillon.
				Prelevement prelevement = echantillonManager
						.getPrelevementManager(echantillon);
				// On ajoute le prélèvement dans la liste si il n'y est
				// pas déjà.
				if (prelevement != null && !prelevements.contains(prelevement)) {
					prelevements.add(prelevement);
				}
			}
		}
		return prelevements;
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste de
	 * ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByProdDerives(List<Object> sources) {
		List<Object> prelevements = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			ProdDerive prodDerive = (ProdDerive) it1.next();
			if (prodDerive != null && prodDerive.getTransformation() != null) {
				boolean isEchantillon = false;
				boolean isPrelevement = false;
				Echantillon echantillon = null;
				Prelevement prelevement = null;
				while (!isEchantillon && !isPrelevement) {
					// On cherche de quelle entité est issu le produit dérivé.
					Object objet = entiteManager
							.findObjectByEntiteAndIdManager(prodDerive
									.getTransformation().getEntite(),
									prodDerive.getTransformation().getObjetId());
					if (objet instanceof Prelevement) {
						// Si le produit dérivé est issu d'un prélèvement, on
						// met isPrelevement à true, on récupère le parent dans
						// prélèvement.
						isPrelevement = true;
						prelevement = (Prelevement) objet;
						break;
					} else if (objet instanceof Echantillon) {
						// Si le produit dérivé est issu d'un échantillon, on
						// met isEchantillon à true, on récupère le parent dans
						// echantillon.
						isEchantillon = true;
						echantillon = (Echantillon) objet;
						break;
					} else if (objet instanceof ProdDerive) {
						// Si le produit dérivé est issu d'un produit dérivé, on
						// récupère le parent dans prodDerive, on boucle sur le
						// parent.
						prodDerive = (ProdDerive) objet;
					} else {
						break;
					}
				}
				if (isPrelevement && prelevement != null) {
					/*
					 * On ajoute le prélèvement s'il n'est pas dans la liste.
					 */
					if (!prelevements.contains(prelevement)) {
						prelevements.add(prelevement);
					}
				} else if (isEchantillon && echantillon != null) {
					/* On récupère le prélèvement de l'échantillon. */
					prelevement = echantillonManager
							.getPrelevementManager(echantillon);
					if (prelevement != null) {
						/*
						 * On ajoute le prélèvement s'il n'est pas dans la
						 * liste.
						 */
						if (!prelevements.contains(prelevement)) {
							prelevements.add(prelevement);
						}
					}
				}
			}
		}
		return prelevements;
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByCessions(List<Object> sources) {
		List<Object> prelevements = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Cession cession = (Cession) it1.next();
			cession.setCederObjets(cessionManager
					.getCederObjetsManager(cession));
			if (cession != null && cession.getCederObjets() != null) {
				Iterator<CederObjet> itCO = cession.getCederObjets().iterator();
				while (itCO.hasNext()) {
					CederObjet cederObj = itCO.next();
					Object objet = entiteManager
							.findObjectByEntiteAndIdManager(cederObj
									.getEntite(), cederObj.getObjetId());
					boolean isEchantillon = false;
					boolean isPrelevement = false;
					boolean isProdDerive = false;
					Echantillon echantillon = null;
					Prelevement prelevement = null;
					ProdDerive prodDerive = null;
					// On cherche quelle entité est cédée par la Cession.
					while (!isEchantillon && !isPrelevement && !isProdDerive) {
						if (objet instanceof Prelevement) {
							// Si la cession concerne un prélèvement, on met
							// isPrelevement à true, on le récupère dans
							// prélèvement.
							isPrelevement = true;
							prelevement = (Prelevement) objet;
							break;
						} else if (objet instanceof Echantillon) {
							// Si la cession concerne un échantillon, on met
							// isEchantillon à true, on le récupère dans
							// echantillon.
							isEchantillon = true;
							echantillon = (Echantillon) objet;
							break;
						} else if (objet instanceof ProdDerive) {
							// Si la cession concerne un produit dérivé, on met
							// isProdDerive à true, on le
							// récupère dans prodDerive.
							isProdDerive = true;
							prodDerive = (ProdDerive) objet;
							break;
						} else {
							break;
						}

					}
					if (isPrelevement && prelevement != null) {
						/*
						 * On ajoute le prélèvement s'il n'est pas dans la
						 * liste.
						 */
						if (!prelevements.contains(prelevement)) {
							prelevements.add(prelevement);
						}
					} else if (isEchantillon && echantillon != null) {
						/* On récupère le prélèvement de l'échantillon. */
						prelevement = echantillonManager
								.getPrelevementManager(echantillon);
						if (prelevement != null) {
							/*
							 * On ajoute le prélèvement s'il n'est pas dans la
							 * liste.
							 */
							if (!prelevements.contains(prelevement)) {
								prelevements.add(prelevement);
							}
						}
					} else if (isProdDerive && prodDerive != null) {
						/* On récupère le prélèvement du prodDerive. */
						List<Object> lTemp = new ArrayList<Object>();
						lTemp.add(prodDerive);
						List<Object> lPrel = findPrelevementByProdDerives(lTemp);
						if (lPrel.size() > 0) {
							prelevement = (Prelevement) lPrel.get(0);
							if (prelevement != null) {
								/*
								 * On ajoute le prélèvement s'il n'est pas dans
								 * la liste.
								 */
								if (!prelevements.contains(prelevement)) {
									prelevements.add(prelevement);
								}
							}
						}
					}
				}
			}
		}
		return prelevements;
	}

	/**
	 * Renvoie une liste de Prelevements correspondant à une liste de
	 * CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste de Prelevements correspondante.
	 */
	private List<Object> findPrelevementByCederObjets(List<Object> sources) {
		List<Object> prelevements = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			//On cherche quelle entité est cédée par l'objet CederObjet.
			CederObjet cederObj = (CederObjet) it1.next();
			Object objet = entiteManager.findObjectByEntiteAndIdManager(
					cederObj.getEntite(), cederObj.getObjetId());
			boolean isEchantillon = false;
			boolean isPrelevement = false;
			boolean isProdDerive = false;
			Echantillon echantillon = null;
			Prelevement prelevement = null;
			ProdDerive prodDerive = null;
			if (objet instanceof Prelevement) {
				// Si la cession concerne un prélèvement, on met
				// isPrelevement à true, on le récupère dans
				// prélèvement.
				isPrelevement = true;
				prelevement = (Prelevement) objet;
			} else if (objet instanceof Echantillon) {
				// Si la cession concerne un échantillon, on met
				// isEchantillon à true, on le récupère dans
				// echantillon.
				isEchantillon = true;
				echantillon = (Echantillon) objet;
			} else if (objet instanceof ProdDerive) {
				// Si la cession concerne un produit dérivé, on met
				// isProdDerive à true, on le récupère dans prodDerive.
				isProdDerive = true;
				prodDerive = (ProdDerive) objet;
			}
			if (isPrelevement && prelevement != null) {
				/*
				 * On ajoute le prélèvement s'il n'est pas dans la liste.
				 */
				if (!prelevements.contains(prelevement)) {
					prelevements.add(prelevement);
				}
			} else if (isEchantillon && echantillon != null) {
				/* On récupère le prélèvement de l'échantillon. */
				prelevement = echantillonManager
						.getPrelevementManager(echantillon);
				if (prelevement != null) {
					/*
					 * On ajoute le prélèvement s'il n'est pas dans la liste.
					 */
					if (!prelevements.contains(prelevement)) {
						prelevements.add(prelevement);
					}
				}
			} else if (isProdDerive && prodDerive != null) {
				/* On récupère le prélèvement du prodDerive. */
				List<Object> lTemp = new ArrayList<Object>();
				lTemp.add(prodDerive);
				List<Object> lPrel = findPrelevementByProdDerives(lTemp);
				if (lPrel.size() > 0) {
					prelevement = (Prelevement) lPrel.get(0);
					if (prelevement != null) {
						/*
						 * On ajoute le prélèvement s'il n'est pas dans la
						 * liste.
						 */
						if (!prelevements.contains(prelevement)) {
							prelevements.add(prelevement);
						}
					}
				}
			}
		}
		return prelevements;
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByPatients(List<Object> sources) {
		return findEchantillonByPrelevements(findPrelevementByPatients(sources));
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByMaladies(List<Object> sources) {
		return findEchantillonByPrelevements(findPrelevementByMaladies(sources));
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de
	 * Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByPrelevements(List<Object> sources) {
		List<Object> echantillons = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Prelevement prelevement = (Prelevement) it1.next();
			if (prelevement != null) {
				// On récupère les échantillons du prélèvement.
				Iterator<Echantillon> it2 = prelevementManager
						.getEchantillonsManager(prelevement).iterator();
				while (it2.hasNext()) {
					Echantillon echantillon = it2.next();
					if (echantillon != null) {
						// On ajoute l'échantillon s'il n'est pas déjà dans la
						// liste.
						if (!echantillons.contains(echantillon)) {
							echantillons.add(echantillon);
						}
					}
				}
			}
		}
		return echantillons;
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de
	 * ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByProdDerives(List<Object> sources) {
		List<Object> echantillons = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			ProdDerive prodDerive = (ProdDerive) it1.next();
			if (prodDerive != null && prodDerive.getTransformation() != null) {
				boolean isEchantillon = false;
				Echantillon echantillon = null;
				while (!isEchantillon) {
					Object objet = entiteManager
							.findObjectByEntiteAndIdManager(prodDerive
									.getTransformation().getEntite(),
									prodDerive.getTransformation().getObjetId());
					// On recherche l'entité dont est issu le produit dérivé.
					if (objet instanceof Echantillon) {
						// S'il s'agit d'un échantillon, on met isEchantillon à
						// true et on place l'échantillon parent dans la
						// variable echantillon.
						isEchantillon = true;
						echantillon = (Echantillon) objet;
						break;
					} else if (objet instanceof ProdDerive) {
						// S'il s'agit d'un produit dérivé, on le produit dérivé
						// parent dans la variable prodDerive, on boucle.
						prodDerive = (ProdDerive) objet;
					} else {
						break;
					}
				}
				if (echantillon != null) {
					// On met l'échantillon dans la liste s'il n'y est pas déjà.
					if (!echantillons.contains(echantillon)) {
						echantillons.add(echantillon);
					}
				}
			}
		}
		return echantillons;
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByCessions(List<Object> sources) {
		List<Object> echantillons = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Cession cession = (Cession) it1.next();
			cession.setCederObjets(cessionManager
					.getCederObjetsManager(cession));
			if (cession != null && cession.getCederObjets() != null) {
				Iterator<CederObjet> itCO = cession.getCederObjets().iterator();
				while (itCO.hasNext()) {
					// On l'entité de l'objet cédé par la cession.
					CederObjet cederObj = itCO.next();
					Object objet = entiteManager
							.findObjectByEntiteAndIdManager(cederObj
									.getEntite(), cederObj.getObjetId());
					boolean isEchantillon = false;
					Echantillon echantillon = null;
					boolean isProdDerive = false;
					ProdDerive prodDerive = null;
					if (objet instanceof Echantillon) {
						// S'il s'agit d'un échantillon, on met isEchantillon à
						// true et on place l'échantillon parent dans la
						// variable echantillon.
						isEchantillon = true;
						echantillon = (Echantillon) objet;
					} else if (objet instanceof ProdDerive) {
						// S'il s'agit d'un produit dérivé, on met isProdDerive
						// à true et on le dans la variable prodDerive, on
						// boucle.
						isProdDerive = true;
						prodDerive = (ProdDerive) objet;
					}

					if (isEchantillon && echantillon != null) {
						if (!echantillons.contains(echantillon)) {
							echantillons.add(echantillon);
						}
					} else if (isProdDerive && prodDerive != null) {
						/* On récupère l'échantillon du prodDerive. */
						List<Object> lTemp = new ArrayList<Object>();
						lTemp.add(prodDerive);
						List<Object> lPrel = findEchantillonByProdDerives(lTemp);
						if (lPrel.size() > 0) {
							echantillon = (Echantillon) lPrel.get(0);
							if (echantillon != null) {
								/*
								 * On ajoute l'échantillon s'il n'est pas dans
								 * la liste.
								 */
								if (!echantillons.contains(echantillon)) {
									echantillons.add(echantillon);
								}
							}
						}
					}
				}
			}
		}
		return echantillons;
	}

	/**
	 * Renvoie une liste d'Echantillons correspondant à une liste de
	 * CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste d'Echantillons correspondante.
	 */
	private List<Object> findEchantillonByCederObjets(List<Object> sources) {
		List<Object> echantillons = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			CederObjet cederObj = (CederObjet) it1.next();
			Object objet = entiteManager.findObjectByEntiteAndIdManager(
					cederObj.getEntite(), cederObj.getObjetId());
			boolean isEchantillon = false;
			Echantillon echantillon = null;
			boolean isProdDerive = false;
			ProdDerive prodDerive = null;
			// On recherche l'entité cédée par l'objet CederObjet.
			if (objet instanceof Echantillon) {
				// S'il s'agit d'un échantillon, on met isEchantillon à
				// true et on place l'échantillon parent dans la
				// variable echantillon.
				isEchantillon = true;
				echantillon = (Echantillon) objet;
			} else if (objet instanceof ProdDerive) {
				// Si la cession concerne un produit dérivé, on met
				// isProdDerive à true, on le récupère dans prodDerive.
				isProdDerive = true;
				prodDerive = (ProdDerive) objet;
			}

			if (isEchantillon && echantillon != null) {
				// On ajoute l'échantillon dans la liste s'il n'y est pas déjà.
				if (!echantillons.contains(echantillon)) {
					echantillons.add(echantillon);
				}
			} else if (isProdDerive && prodDerive != null) {
				/* On récupère l'échantillon du prodDerive. */
				List<Object> lTemp = new ArrayList<Object>();
				lTemp.add(prodDerive);
				List<Object> lPrel = findEchantillonByProdDerives(lTemp);
				if (lPrel.size() > 0) {
					echantillon = (Echantillon) lPrel.get(0);
					if (echantillon != null) {
						/*
						 * On ajoute l'échantillon s'il n'est pas dans la liste.
						 */
						if (!echantillons.contains(echantillon)) {
							echantillons.add(echantillon);
						}
					}
				}
			}
		}
		return echantillons;
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByPatients(List<Object> sources) {
		return findProdDeriveByPrelevements(findPrelevementByPatients(sources));
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByMaladies(List<Object> sources) {
		return findProdDeriveByPrelevements(findPrelevementByMaladies(sources));
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste de
	 * Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByPrelevements(List<Object> sources) {
		List<Object> derives = new ArrayList<Object>();
		// On recherche les produits dérivés directs
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Prelevement prelevement = (Prelevement) it1.next();
			if (prelevement != null) {
				Iterator<Object> itEnfant = findProdDeriveEnfant(prelevement)
						.iterator();
				while (itEnfant.hasNext()) {
					ProdDerive enfant = (ProdDerive) itEnfant.next();
					if (enfant != null) {
						if (!derives.contains(enfant)) {
							derives.add(enfant);
						}
					}
				}
				// On recherche les produits dérivés via les echantillons
				Iterator<Echantillon> it2 = prelevementManager
						.getEchantillonsManager(prelevement).iterator();
				while (it2.hasNext()) {
					Echantillon echantillon = it2.next();
					itEnfant = findProdDeriveEnfant(echantillon).iterator();
					while (itEnfant.hasNext()) {
						ProdDerive enfant = (ProdDerive) itEnfant.next();
						if (enfant != null) {
							if (!derives.contains(enfant)) {
								derives.add(enfant);
							}
						}
					}
				}
			}
		}
		// On recherche les produits dérivés des produits dérivés
		Iterator<Object> it = derives.iterator();
		while (it.hasNext()) {
			ProdDerive enfant = (ProdDerive) it.next();
			if (enfant != null) {
				Iterator<Object> itEnfant = findProdDeriveEnfant(enfant)
						.iterator();
				while (itEnfant.hasNext()) {
					ProdDerive petitEnfant = (ProdDerive) itEnfant.next();
					if (petitEnfant != null) {
						if (!derives.contains(petitEnfant)) {
							derives.add(petitEnfant);
						}
					}
				}
			}
		}
		return derives;
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste
	 * d'Echantillons.
	 * 
	 * @param sources
	 *            Liste d'Echantillons.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByEchantillons(List<Object> sources) {
		List<Object> derives = new ArrayList<Object>();
		// On recherche les produits dérivés directs
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Echantillon echantillon = (Echantillon) it1.next();
			if (echantillon != null) {
				Iterator<Object> itEnfant = findProdDeriveEnfant(echantillon)
						.iterator();
				while (itEnfant.hasNext()) {
					ProdDerive enfant = (ProdDerive) itEnfant.next();
					if (enfant != null) {
						if (!derives.contains(enfant)) {
							derives.add(enfant);
						}
					}
				}
			}
		}
		// On recherche les produits dérivés des produits dérivés
		Iterator<Object> it = derives.iterator();
		while (it.hasNext()) {
			ProdDerive enfant = (ProdDerive) it.next();
			if (enfant != null) {
				Iterator<Object> itEnfant = findProdDeriveEnfant(enfant)
						.iterator();
				while (itEnfant.hasNext()) {
					ProdDerive petitEnfant = (ProdDerive) itEnfant.next();
					if (petitEnfant != null) {
						if (!derives.contains(petitEnfant)) {
							derives.add(petitEnfant);
						}
					}
				}
			}
		}
		return derives;
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByCessions(List<Object> sources) {
		List<Object> prodDerives = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Cession cession = (Cession) it1.next();
			cession.setCederObjets(cessionManager
					.getCederObjetsManager(cession));
			if (cession != null && cession.getCederObjets() != null) {
				Iterator<CederObjet> itCO = cession.getCederObjets().iterator();
				while (itCO.hasNext()) {
					CederObjet cederObj = itCO.next();
					Object objet = entiteManager
							.findObjectByEntiteAndIdManager(cederObj
									.getEntite(), cederObj.getObjetId());
					boolean isProdDerive = false;
					ProdDerive prodDerive = null;
					// On cherche les entitées concernées par la cession.
					if (objet instanceof ProdDerive) {
						// S'il s'agit d'un produit dérivé, on met isProdDerive
						// à true et on le dans la variable prodDerive.
						isProdDerive = true;
						prodDerive = (ProdDerive) objet;
					}

					if (isProdDerive && prodDerive != null) {
						// Si le produit dérivé n'est pas dans la liste, on
						// l'ajoute dedans.
						if (!prodDerives.contains(prodDerive)) {
							prodDerives.add(prodDerive);
						}
					}
				}
			}
		}
		return prodDerives;
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant à une liste de
	 * CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste de ProdDerives correspondante.
	 */
	private List<Object> findProdDeriveByCederObjets(List<Object> sources) {
		List<Object> prodDerives = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			CederObjet cederObj = (CederObjet) it1.next();
			Object objet = entiteManager.findObjectByEntiteAndIdManager(
					cederObj.getEntite(), cederObj.getObjetId());
			boolean isProdDerive = false;
			ProdDerive prodDerive = null;
			// On recherche les entités concernées par l'objet CederObjet.
			if (objet instanceof ProdDerive) {
				// S'il s'agit d'un produit dérivé, on met isProdDerive
				// à true et on le dans la variable prodDerive.
				isProdDerive = true;
				prodDerive = (ProdDerive) objet;
			}

			if (isProdDerive && prodDerive != null) {
				// Si le produit dérivé n'est pas dans la liste, on le met
				// dedans.
				if (!prodDerives.contains(prodDerive)) {
					prodDerives.add(prodDerive);
				}
			}
		}
		return prodDerives;
	}

	/**
	 * Renvoie une liste de ProdDerives correspondant aux descendants de liste
	 * de ProdDerives en paramètre.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de ProdDerives correspondant aux descendants de la liste de
	 *         ProdDerives en paramètre.
	 */
	private List<Object> findProdDeriveEnfantByProdDerives(List<Object> sources) {
		List<Object> enfants = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		/* On parcourt la liste des dérivés sources. */
		while (it1.hasNext()) {
			ProdDerive prodDerive = (ProdDerive) it1.next();
			if (prodDerive != null) {
				/* Pour chaque dérivé, on récupère ses descendants. */
				Iterator<Object> it = findProdDeriveEnfant(prodDerive)
						.iterator();
				while (it.hasNext()) {
					ProdDerive descendant = (ProdDerive) it.next();
					/*
					 * Pour chaque descendant non null, on l'ajoute à la liste
					 * s'il n'y est pas déjà.
					 */
					if (descendant != null) {
						if (!enfants.contains(descendant)) {
							enfants.add(descendant);
						}
					}
				}
			}
		}
		return enfants;
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByPatients(List<Object> sources) {
		return findCessionByPrelevements(findPrelevementByPatients(sources));
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByMaladies(List<Object> sources) {
		return findCessionByPrelevements(findPrelevementByMaladies(sources));
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste de Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByPrelevements(List<Object> sources) {
		List<Object> cessions = new ArrayList<Object>();
		// On recherche les cessions directs
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Prelevement prelevement = (Prelevement) it1.next();
			if (prelevement != null) {
				// On recherche les cession via les echantillons
				Set<Echantillon> echantillons = prelevementManager
						.getEchantillonsManager(prelevement);
				Iterator<Object> itCession = findCessionByEchantillons(
						new ArrayList<Object>(echantillons)).iterator();
				while (itCession.hasNext()) {
					Cession temp = (Cession) itCession.next();
					if (temp != null) {
						if (!cessions.contains(temp)) {
							cessions.add(temp);
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lPre = new ArrayList<Object>();
				lPre.add(prelevement);
				List<Object> prodDerives = findProdDeriveByPrelevements(lPre);
				Iterator<Object> itCession2 = findCessionByProdDerives(
						prodDerives).iterator();
				while (itCession2.hasNext()) {
					Cession temp = (Cession) itCession2.next();
					if (temp != null) {
						if (!cessions.contains(temp)) {
							cessions.add(temp);
						}
					}
				}
			}
		}
		return cessions;
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste d'Echantillon.
	 * 
	 * @param sources
	 *            Liste d'Echantillons.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByEchantillons(List<Object> sources) {
		List<Object> cessions = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Echantillon echantillon = (Echantillon) it1.next();
			if (echantillon != null) {
				// On cherche les cessions directes
				List<CederObjet> lCedObjs = cederObjetManager
						.findByObjetManager(echantillon);
				Iterator<CederObjet> itCedObjs = lCedObjs.iterator();
				while (itCedObjs.hasNext()) {
					CederObjet cedObj = itCedObjs.next();
					if (cedObj != null && cedObj.getCession() != null) {
						if (!cessions.contains(cedObj.getCession())) {
							cessions.add(cedObj.getCession());
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lEch = new ArrayList<Object>();
				lEch.add(echantillon);
				List<Object> prodDerives = findProdDeriveByEchantillons(lEch);
				Iterator<Object> itCession2 = findCessionByProdDerives(
						prodDerives).iterator();
				while (itCession2.hasNext()) {
					Cession temp = (Cession) itCession2.next();
					if (temp != null) {
						if (!cessions.contains(temp)) {
							cessions.add(temp);
						}
					}
				}
			}
		}
		return cessions;
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste de ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByProdDerives(List<Object> sources) {
		List<Object> cessions = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			ProdDerive prodDerive = (ProdDerive) it1.next();
			if (prodDerive != null) {
				List<CederObjet> lCedObjs = cederObjetManager
						.findByObjetManager(prodDerive);
				Iterator<CederObjet> itCedObjs = lCedObjs.iterator();
				while (itCedObjs.hasNext()) {
					CederObjet cedObj = itCedObjs.next();
					if (cedObj != null && cedObj.getCession() != null) {
						if (!cessions.contains(cedObj.getCession())) {
							cessions.add(cedObj.getCession());
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lProdDerive = new ArrayList<Object>();
				lProdDerive.add(prodDerive);
				List<Object> prodDerives = findProdDeriveEnfantByProdDerives(lProdDerive);
				Iterator<Object> itCession2 = findCessionByProdDerives(
						prodDerives).iterator();
				while (itCession2.hasNext()) {
					Cession temp = (Cession) itCession2.next();
					if (temp != null) {
						if (!cessions.contains(temp)) {
							cessions.add(temp);
						}
					}
				}
			}
		}
		return cessions;
	}

	/**
	 * Renvoie une liste de Cessions correspondant à une liste de CederObjets.
	 * 
	 * @param sources
	 *            Liste de CederObjets.
	 * @return Liste de Cessions correspondante.
	 */
	private List<Object> findCessionByCederObjets(List<Object> sources) {
		List<Object> cessions = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			CederObjet cederObjet = (CederObjet) it1.next();
			if (cederObjet != null) {
				if (cederObjet.getCession() != null) {
					if (!cessions.contains(cederObjet.getCession())) {
						cessions.add(cederObjet.getCession());
					}
				}
			}
		}
		return cessions;
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste de Patients.
	 * 
	 * @param sources
	 *            Liste de Patients.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByPatients(List<Object> sources) {
		return findCederObjetByPrelevements(findPrelevementByPatients(sources));
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste de Maladies.
	 * 
	 * @param sources
	 *            Liste de Maladies.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByMaladies(List<Object> sources) {
		return findCederObjetByPrelevements(findPrelevementByMaladies(sources));
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste de
	 * Prelevements.
	 * 
	 * @param sources
	 *            Liste de Prelevements.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByPrelevements(List<Object> sources) {
		List<Object> cederObjets = new ArrayList<Object>();
		// On recherche les cessions directs
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Prelevement prelevement = (Prelevement) it1.next();
			if (prelevement != null) {
				// On recherche les cession via les echantillons
				Set<Echantillon> echantillons = prelevementManager
						.getEchantillonsManager(prelevement);

				Iterator<Echantillon> itEchantillon = echantillons.iterator();
				while (itEchantillon.hasNext()) {
					List<CederObjet> temp = cederObjetManager
							.findByObjetManager(itEchantillon.next());
					if (temp != null) {
						for (int i = 0; i < temp.size(); i++) {
							if (!cederObjets.contains(temp.get(i))) {
								cederObjets.add(temp.get(i));
							}
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lPre = new ArrayList<Object>();
				lPre.add(prelevement);
				List<Object> prodDerives = findProdDeriveByPrelevements(lPre);
				Iterator<Object> itDerives = prodDerives.iterator();
				while (itDerives.hasNext()) {
					List<CederObjet> temp = cederObjetManager
							.findByObjetManager(itDerives.next());
					if (temp != null) {
						for (int i = 0; i < temp.size(); i++) {
							if (!cederObjets.contains(temp.get(i))) {
								cederObjets.add(temp.get(i));
							}
						}
					}
				}
			}
		}
		return cederObjets;
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste d'Echantillon.
	 * 
	 * @param sources
	 *            Liste d'Echantillons.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByEchantillons(List<Object> sources) {
		List<Object> cederObjets = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Echantillon echantillon = (Echantillon) it1.next();
			if (echantillon != null) {
				// On cherche les cessions directes
				List<CederObjet> lCedObjs = cederObjetManager
						.findByObjetManager(echantillon);
				Iterator<CederObjet> itCedObjs = lCedObjs.iterator();
				while (itCedObjs.hasNext()) {
					CederObjet cedObj = itCedObjs.next();
					if (cedObj != null) {
						if (!cederObjets.contains(cedObj)) {
							cederObjets.add(cedObj);
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lEch = new ArrayList<Object>();
				lEch.add(echantillon);
				List<Object> prodDerives = findProdDeriveByEchantillons(lEch);
				for (int i = 0; i < prodDerives.size(); i++) {
					lCedObjs = cederObjetManager.findByObjetManager(prodDerives
							.get(i));
					itCedObjs = lCedObjs.iterator();
					while (itCedObjs.hasNext()) {
						CederObjet cedObj = itCedObjs.next();
						if (cedObj != null) {
							if (!cederObjets.contains(cedObj)) {
								cederObjets.add(cedObj);
							}
						}
					}
				}
			}
		}
		return cederObjets;
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste de
	 * ProdDerives.
	 * 
	 * @param sources
	 *            Liste de ProdDerives.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByProdDerives(List<Object> sources) {
		List<Object> cederObjets = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			ProdDerive prodDerive = (ProdDerive) it1.next();
			if (prodDerive != null) {
				List<CederObjet> lCedObjs = cederObjetManager
						.findByObjetManager(prodDerive);
				Iterator<CederObjet> itCedObjs = lCedObjs.iterator();
				while (itCedObjs.hasNext()) {
					CederObjet cedObj = itCedObjs.next();
					if (cedObj != null) {
						if (!cederObjets.contains(cedObj)) {
							cederObjets.add(cedObj);
						}
					}
				}

				// On recherche les cession via les produits derives
				List<Object> lProdDerive = new ArrayList<Object>();
				lProdDerive.add(prodDerive);
				List<Object> prodDerives = findProdDeriveEnfantByProdDerives(lProdDerive);
				for (int i = 0; i < prodDerives.size(); i++) {
					lCedObjs = cederObjetManager.findByObjetManager(prodDerives
							.get(i));
					itCedObjs = lCedObjs.iterator();
					while (itCedObjs.hasNext()) {
						CederObjet cedObj = itCedObjs.next();
						if (cedObj != null) {
							if (!cederObjets.contains(cedObj)) {
								cederObjets.add(cedObj);
							}
						}
					}
				}
			}
		}
		return cederObjets;
	}

	/**
	 * Renvoie une liste de CederObjets correspondant à une liste de Cessions.
	 * 
	 * @param sources
	 *            Liste de Cessions.
	 * @return Liste de CederObjets correspondante.
	 */
	private List<Object> findCederObjetByCessions(List<Object> sources) {
		List<Object> cederObjets = new ArrayList<Object>();
		Iterator<Object> it1 = sources.iterator();
		while (it1.hasNext()) {
			Cession cession = (Cession) it1.next();
			Set<CederObjet> cederObjs = cessionManager
					.getCederObjetsManager(cession);
			Iterator<CederObjet> itCedObjs = cederObjs.iterator();
			while (itCedObjs.hasNext()) {
				CederObjet cedObj = itCedObjs.next();
				if (cedObj != null) {
					if (!cederObjets.contains(cedObj)) {
						cederObjets.add(cedObj);
					}
				}
			}
		}

		return cederObjets;
	}

	/**
	 * Renvoie la liste de tous les (produits dérivés) descendants d'un objet.
	 * 
	 * @param parent
	 *            Objet dont on souhaite obtenir les produits dérivés enfants.
	 * @return liste des produits dérivés enfants sous forme de liste d'objets.
	 */
	private List<Object> findProdDeriveEnfant(Object parent) {
		List<Object> enfants = new ArrayList<Object>();

		if (parent != null) {
			if (parent.getClass().getSimpleName().equals("ProdDerive")) {
				// On ajoute chaque dérive à la liste si elle ne le contient pas
				// déjà
				List<ProdDerive> derives = prodDeriveManager
						.getProdDerivesManager((ProdDerive) parent);
				if (derives != null) {
					for (Iterator<ProdDerive> it = derives.iterator(); it
							.hasNext();) {
						ProdDerive temp = it.next();
						if (!enfants.contains(temp)) {
							enfants.add(temp);
						}
					}
				}
			} else if (parent.getClass().getSimpleName().equals("Echantillon")) {
				// On ajoute chaque dérive à la liste si elle ne le contient pas
				// déjà
				for (Iterator<ProdDerive> it = echantillonManager
						.getProdDerivesManager((Echantillon) parent).iterator(); it
						.hasNext();) {
					ProdDerive temp = it.next();
					if (!enfants.contains(temp)) {
						enfants.add(temp);
					}
				}
			} else if (parent.getClass().getSimpleName().equals("Prelevement")) {
				// On ajoute chaque dérive à la liste si elle ne le contient pas
				// déjà
				for (Iterator<ProdDerive> it = prelevementManager
						.getProdDerivesManager((Prelevement) parent).iterator(); it
						.hasNext();) {
					ProdDerive temp = it.next();
					if (!enfants.contains(temp)) {
						enfants.add(temp);
					}
				}
			}

			for (int i = 0; i < enfants.size(); i++) {
				// On ajoute tous les sous enfants s'ils ne sont pas déjà dans
				// la liste
				List<Object> derives = findProdDeriveEnfant(enfants.get(i));
				for (int j = 0; j < derives.size(); j++) {
					Object temp = derives.get(j);
					if (!enfants.contains(temp)) {
						enfants.add(temp);
					}
				}
			}
		}
		return enfants;
	}

	public List<Object> recupereEntitesPourAffichageManager(List<Object> liste,
			Affichage affichage) {
		List<Object> l = new ArrayList<Object>();
		if (liste != null && liste.size() > 0) {
			String entiteComparee = liste.get(0).getClass().getSimpleName();
			if (entiteComparee.equals("Cession")) {
				// On récupère la liste de CederObjets correspondante
				l = recupereEntitesViaDAutres(liste, "CederObjet");
			} else {
				/* On itère la liste des résultats de l'affichage. */
				Iterator<Resultat> itRes = affichage.getResultats().iterator();
				while (itRes.hasNext()) {
					/*
					 * On récupère le nom de l'entité du champ de chaque
					 * résultat.
					 */
					Resultat res = itRes.next();
					Entite entite = null;
					if (res.getChamp() != null) {
						Champ champ = res.getChamp();
						if (champ.getChampEntite() != null) {
							entite = champ.getChampEntite().getEntite();
							// On récupère le champ parent si c'est un sous
							// champ
							Champ temp = champ;
							while (temp != null
									&& temp.getChampParent() != null) {
								champ = temp;
								temp = champ.getChampParent();
								entite = champ.getChampEntite().getEntite();
							}
						} else if (champ.getChampAnnotation() != null) {
							entite = champ.getChampAnnotation()
									.getTableAnnotation().getEntite();
						}
					}
					if (entite.getNom() != null) {
						String nomEntite = entite.getNom();
						/*
						 * On compare l'entité à celle de la liste en paramètre.
						 */
						if (nomEntite.equals("Cession")) {
							entiteComparee = "CederObjet";
						} else if (!nomEntite.equals(entiteComparee)) {
							if (entiteComparee.equals("Patient")) {
								if (nomEntite.equals("Maladie")
										|| nomEntite.equals("Prelevement")
										|| nomEntite.equals("Echantillon")
										|| nomEntite.equals("ProdDerive")) {
									entiteComparee = nomEntite;
								}
							} else if (entiteComparee.equals("Maladie")) {
								if (nomEntite.equals("Prelevement")
										|| nomEntite.equals("Echantillon")
										|| nomEntite.equals("ProdDerive")) {
									entiteComparee = nomEntite;
								}
							} else if (entiteComparee.equals("Prelevement")) {
								if (nomEntite.equals("Echantillon")
										|| nomEntite.equals("ProdDerive")) {
									entiteComparee = nomEntite;
								}
							} else if (entiteComparee.equals("Echantillon")) {
								if (nomEntite.equals("ProdDerive")) {
									entiteComparee = nomEntite;
								}
							}

						}
					}
				} // fin while
				if (!entiteComparee.equals(liste.get(0).getClass()
						.getSimpleName())) {
					l = recupereEntitesViaDAutres(liste, entiteComparee);
				} else {
					l = liste;
				}
			}
		}
		return l;
	}

}
