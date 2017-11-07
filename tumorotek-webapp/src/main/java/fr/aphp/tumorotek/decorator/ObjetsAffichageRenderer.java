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
package fr.aphp.tumorotek.decorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.io.ResultatRow;
import fr.aphp.tumorotek.action.io.ResultatRowRenderer;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveDecorator2;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ObjetsAffichageRenderer {

	private List<Object> objets;
	private Affichage affichage;
	private List<List<Object>> matriceObjets;
	private List<List<Object>> matriceAffichable;

	public ObjetsAffichageRenderer(List<Object> objs, Affichage aff) {
		this.objets = objs;
		this.affichage = aff;
	}

	public List<Object> getObjets() {
		return objets;
	}

	public void setObjets(List<Object> objets) {
		this.objets = objets;
	}

	public Affichage getAffichage() {
		return affichage;
	}

	public void setAffichage(Affichage affichage) {
		this.affichage = affichage;
		this.affichage.setResultats(ManagerLocator.getResultatManager()
				.findByAffichageManager(affichage));
	}

	public List<Listitem> renderListitem(Listbox lbox) {
		List<Listitem> li = new ArrayList<Listitem>();

		/** On recupère les objets hauts pour l'affichage. */
		objets = ManagerLocator.getCorrespondanceManager()
				.recupereEntitesPourAffichageManager(objets, affichage);
		
		/** On crée la matrice. */
		matriceObjets = new ArrayList<List<Object>>();
		// On itère la liste d'objets
		Iterator<Object> itObjets = objets.iterator();
		while (itObjets.hasNext()) {
			matriceObjets.add(getListeObjetsCorrespondants(itObjets.next()));
		}

		/** On charge la matrice affichable. */
		loadMatriceAffichable();

		// On supprime les doublons de la liste
		for (int i = 0; i < matriceAffichable.size(); i++) {
			List<Object> lo = matriceAffichable.get(i);
			for (int j = i + 1; j < matriceAffichable.size(); j++) {
				List<Object> loTemp = matriceAffichable.get(j);
				if (lo.size() == loTemp.size()) {
					boolean egale = true;
					for (int k = 0; k < lo.size(); k++) {
						if ((lo.get(k) == null && loTemp.get(k) != null)
								|| (lo.get(k) != null && !lo.get(k).equals(
										loTemp.get(k)))) {
							egale = false;
							break;
						}
					}
					if (egale) {
						matriceAffichable.remove(j);
						matriceObjets.remove(j);
						j--;
					}
				}
			}
		}

		/** On trie les matrices. */
		sortMatrices();

		/** On récupère la liste de Lisitem */
		li = loadListitems(lbox);

		return li;
	}
	
	public void renderGrid(Grid lbox) {
		// List<Listitem> li = new ArrayList<Listitem>();

		/** On recupère les objets hauts pour l'affichage. */
		objets = ManagerLocator.getCorrespondanceManager()
				.recupereEntitesPourAffichageManager(objets, affichage);
		
		/** On crée la matrice. */
		matriceObjets = new ArrayList<List<Object>>();
		// On itère la liste d'objets
		// Iterator<Object> itObjets = objets.iterator();
		for (int i = 0; i < 20; i ++) {
			matriceObjets
				.add(getListeObjetsCorrespondants(objets.get(i)));
		}
		// while (itObjets.hasNext()) {
		//	matriceObjets.add(getListeObjetsCorrespondants(itObjets.next()));
		// }

		/** On charge la matrice affichable. */
		loadMatriceAffichable();
		
		// On supprime les doublons de la liste
		for (int i = 0; i < matriceAffichable.size(); i++) {
			List<Object> lo = matriceAffichable.get(i);
			for (int j = i + 1; j < matriceAffichable.size(); j++) {
				List<Object> loTemp = matriceAffichable.get(j);
				if (lo.size() == loTemp.size()) {
					boolean egale = true;
					for (int k = 0; k < lo.size(); k++) {
						if ((lo.get(k) == null && loTemp.get(k) != null)
								|| (lo.get(k) != null && !lo.get(k).equals(
										loTemp.get(k)))) {
							egale = false;
							break;
						}
					}
					if (egale) {
						matriceAffichable.remove(j);
						matriceObjets.remove(j);
						j--;
					}
				}
			}
		}
		/** On trie les matrices. */
		sortMatrices();

		/** On récupère la liste de Lisitem */
		//loadGridRows(lbox);
		loadGridContent(lbox);
	}

	private List<Object> getListeObjetsCorrespondants(Object o) {
		List<Object> liste = new ArrayList<Object>();
		/* On itère la liste des résultats de l'affichage. */
		Iterator<Resultat> itRes = affichage.getResultats().iterator();
		while (itRes.hasNext()) {
			Resultat res = itRes.next();
			/* On récupère l'entité depuis le champ du Resultat. */
			Entite entite = null;
			if (res != null) {
				if (res.getChamp() != null) {
					if (res.getChamp().getChampEntite() != null) {
						entite = res.getChamp().getChampEntite().getEntite();
						// On récupère l'entité parente s'il y en a !
						Champ parent = res.getChamp().getChampParent();
						while (parent != null) {
							entite = parent.getChampEntite().getEntite();
							parent = parent.getChampParent();
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						entite = res.getChamp().getChampAnnotation()
								.getTableAnnotation().getEntite();
					} else {
						liste.add(null);
					}
				} else {
					liste.add(null);
				}
			} else {
				liste.add(null);
			}
			if (entite != null) {
				List<Object> lObj = new ArrayList<Object>();
				lObj.add(o);
				Object recup = null;
				if (o.getClass().getSimpleName().equals(entite.getNom())) {
					recup = o;
				} else {
					List<Object> recups = ManagerLocator
							.getCorrespondanceManager()
							.recupereEntitesViaDAutres(lObj, entite.getNom());
					if (recups != null && recups.size() >= 1) {
						recup = recups.get(0);
					}
				}
				liste.add(recup);
			}
		}
		return liste;
	}

	private void sortMatrices() {
		if (matriceObjets != null) {
			if (matriceObjets.size() > 1) {
				if (affichage != null) {
					List<Resultat> resultats = new ArrayList<Resultat>();
					resultats.addAll(affichage.getResultats());
					// On trie les résultats par ordre de tri
					if (resultats.size() > 1) {
						for (int i = 1; i < resultats.size(); i++) {
							for (int j = i - 1; j >= 0; j--) {
								if (resultats.get(j + 1).getOrdreTri() < resultats
										.get(j).getOrdreTri()) {
									Resultat temp = resultats.get(j + 1);
									resultats.set(j + 1, resultats.get(j));
									resultats.set(j, temp);
								}
							}
						}
					}

					for (int k = 1; k < matriceObjets.size(); k++) {
						for (int m = k; m > 0; m--) {
							boolean isSwapped = false;
							List<Object> kListObjets = matriceAffichable.get(m);
							List<Object> k2ListObjets = matriceAffichable
									.get(m - 1);
							// On parcourt la liste de résultats
							/*
							 * (Les objets dans getCells sont ordonnés par les
							 * positions des résultats)
							 */
							for (int i = 0; i < resultats.size(); i++) {
								if (kListObjets.get(resultats.get(i)
										.getPosition() - 1) != null
										&& k2ListObjets.get(resultats.get(i)
												.getPosition() - 1) != null) {
									// On diffère selon l'ordre de tri
									// (croissant ou décroissant)
									if (resultats.get(i).getTri()) {
										int compare = ((Comparable) kListObjets
												.get(resultats.get(i)
														.getPosition() - 1))
												.compareTo((Comparable) k2ListObjets
														.get(resultats.get(i)
																.getPosition() - 1));
										if (compare < 0) {
											Collections.swap(matriceAffichable,
													m, m - 1);
											Collections.swap(matriceObjets, m,
													m - 1);
											isSwapped = true;
											break;
										} else if (compare != 0) {
											break;
										}

									} else {
										int compare = ((Comparable) kListObjets
												.get(resultats.get(i)
														.getPosition() - 1))
												.compareTo((Comparable) k2ListObjets
														.get(resultats.get(i)
																.getPosition() - 1));
										if (compare > 0) {
											Collections.swap(matriceAffichable,
													m, m - 1);
											Collections.swap(matriceObjets, m,
													m - 1);
											isSwapped = true;
											break;
										} else if (compare != 0) {
											break;
										}
									}
								}
							}
							if (!isSwapped) {
								break;
							}
						}
					}
				}
			}
		}
	}

	private void loadMatriceAffichable() {
		matriceAffichable = new ArrayList<List<Object>>();
		// On itère la matrice
		Iterator<List<Object>> itMat = matriceObjets.iterator();
		while (itMat.hasNext()) {
			List<Object> listeObjets = itMat.next();
			List<Object> listeStrings = new ArrayList<Object>();
			// On itère les résultats
			Iterator<Resultat> itRes = affichage.getResultats().iterator();
			while (itRes.hasNext()) {
				Resultat res = itRes.next();
				/* On récupère l'entité depuis le champ du Resultat. */
				Entite entite = null;
				Champ parent = null;
				if (res != null) {
					if (res.getChamp() != null) {
						if (res.getChamp().getChampEntite() != null) {
							entite = res.getChamp().getChampEntite()
									.getEntite();
							// On cherche l'entite parente
							Champ temp = res.getChamp().getChampParent();
							while (temp != null) {
								parent = temp;
								entite = parent.getChampEntite().getEntite();
								temp = parent.getChampParent();
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							entite = res.getChamp().getChampAnnotation()
									.getTableAnnotation().getEntite();
						} else {
							listeStrings.add("-");
						}
					} else {
						listeStrings.add("-");
					}
				} else {
					listeStrings.add("-");
				}
				if (entite != null) {
					if (entite.getNom().equals("Patient")) {
						Patient recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof Patient) {
								recup = (Patient) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							if (nomChampEntite.equals("Nip")) {
								listeStrings.add(((Patient) recup).getNip());
							} else if (nomChampEntite.equals("Nom")) {
								listeStrings.add(((Patient) recup).getNom());
							} else if (nomChampEntite.equals("NomNaissance")) {
								listeStrings.add(((Patient) recup)
										.getNomNaissance());
							} else if (nomChampEntite.equals("Prenom")) {
								listeStrings.add(((Patient) recup).getPrenom());
							} else if (nomChampEntite.equals("Sexe")) {
								listeStrings.add(((Patient) recup).getSexe());
							} else if (nomChampEntite.equals("DateNaissance")) {
								//listeStrings.add(((Patient) recup)
										//.getDateNaissance());
								listeStrings.add(ObjectTypesFormatters
										.dateRenderer2(((Patient) recup)
										.getDateNaissance()));
							} else if (nomChampEntite.equals("VilleNaissance")) {
								listeStrings.add(((Patient) recup)
										.getVilleNaissance());
							} else if (nomChampEntite.equals("PaysNaissance")) {
								listeStrings.add(((Patient) recup)
										.getPaysNaissance());
							} else if (nomChampEntite.equals("PatientEtat")) {
								listeStrings.add(((Patient) recup)
										.getPatientEtat());
							} else if (nomChampEntite.equals("DateEtat")) {
								listeStrings.add(((Patient) recup)
										.getDateEtat());
							} else if (nomChampEntite.equals("DateDeces")) {
								listeStrings.add(((Patient) recup)
										.getDateDeces());
							} else if (nomChampEntite.equals("EtatIncomplet")) {
								listeStrings.add(((Patient) recup)
										.getEtatIncomplet());
							} else if (nomChampEntite.equals("Archive")) {
								listeStrings
										.add(((Patient) recup).getArchive());
							} else {
								listeStrings.add("-");
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							listeStrings.add(getChampAnnotationValeur(recup, 
									res.getChamp().getChampAnnotation()));
						} else {
							listeStrings.add("-");
						}
					} else if (entite.getNom().equals("Maladie")) {
						Maladie recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof Maladie) {
								recup = (Maladie) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							MaladieDecorator malDeco = new MaladieDecorator(
									(Maladie) recup);
							if (parent == null) {
								if (nomChampEntite.equals("Libelle")) {
									listeStrings.add(malDeco.getLibelle());
								} else if (nomChampEntite.equals("Code")) {
									listeStrings.add(malDeco.getCode());
								} else if (nomChampEntite
										.equals("DateDiagnostic")) {
									listeStrings.add(malDeco
											.getFormattedDateDiag());
								} else if (nomChampEntite.equals("DateDebut")) {
									listeStrings.add(malDeco
											.getFormattedDateDebut());
								} else {
									listeStrings.add("-");
								}
							} else {
								// Traitement des sousChamps de prélèvement
								if (parent.getChampEntite().getNom().equals(
										"PatientId")) {
									if (recup.getPatient() != null) {
									if (nomChampEntite.equals("Nip")) {
										listeStrings.add(recup.getPatient()
													.getNip());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getPatient()
												.getNom());
									} else if (nomChampEntite
											.equals("NomNaissance")) {
										listeStrings.add(recup.getPatient()
												.getNomNaissance());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup.getPatient()
												.getPrenom());
									} else if (nomChampEntite.equals("Sexe")) {
										listeStrings.add(recup.getPatient()
												.getSexe());
									} else if (nomChampEntite
											.equals("DateNaissance")) {
										//listeStrings.add(recup.getPatient()
											//	.getDateNaissance());
										listeStrings.add(ObjectTypesFormatters
												.dateRenderer2(recup.getPatient()
												.getDateNaissance()));
									} else if (nomChampEntite
											.equals("VilleNaissance")) {
										listeStrings.add(recup.getPatient()
												.getVilleNaissance());
									} else if (nomChampEntite
											.equals("PaysNaissance")) {
										listeStrings.add(recup.getPatient()
												.getPaysNaissance());
									} else if (nomChampEntite
											.equals("PatientEtat")) {
										listeStrings.add(recup.getPatient()
												.getPatientEtat());
									} else if (nomChampEntite
											.equals("DateEtat")) {
										listeStrings.add(recup.getPatient()
												.getDateEtat());
									} else if (nomChampEntite
											.equals("DateDeces")) {
										listeStrings.add(recup.getPatient()
												.getDateDeces());
									} else if (nomChampEntite
											.equals("EtatIncomplet")) {
										listeStrings.add(recup.getPatient()
												.getEtatIncomplet());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getPatient()
												.getArchive());
									}
								} else {
									listeStrings.add("-");
								}
								}
							}
						} else {
							listeStrings.add("-");
						}
					} else if (entite.getNom().equals("Prelevement")) {
						Prelevement recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof Prelevement) {
								recup = (Prelevement) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							PrelevementDecorator2 preDeco = new PrelevementDecorator2(
									(Prelevement) recup);
							if (parent == null) {
								if (nomChampEntite.equals("Code")) {
									listeStrings.add(preDeco.getCode());
								} else if (nomChampEntite.equals("ConsentDate")) {
									listeStrings.add(preDeco
											.getConsentementDate());
								} else if (nomChampEntite
										.equals("DatePrelevement")) {
									listeStrings.add(preDeco
											.getFormattedPrelevementDate());
								} else if (nomChampEntite.equals("ConditNbr")) {
									listeStrings.add(preDeco
											.getNombreConditionnement());
								} else if (nomChampEntite.equals("DateDepart")) {
									listeStrings.add(preDeco
											.getFormattedDateDepart());
								} else if (nomChampEntite
										.equals("TransportTemp")) {
									listeStrings.add(preDeco
											.getTransportTemperature());
								} else if (nomChampEntite.equals("DateArrivee")) {
									listeStrings.add(preDeco
											.getFormattedDateArrivee());
								} else if (nomChampEntite.equals("Quantite")) {
									listeStrings.add(preDeco.getQuantite());
								} else if (nomChampEntite.equals("PatientNda")) {
									listeStrings.add(preDeco.getNdaPatient());
								} else if (nomChampEntite.equals("NumeroLabo")) {
									listeStrings.add(preDeco
											.getNumeroLaboratoire());
								} else if (nomChampEntite.equals("Sterile")) {
									listeStrings.add(preDeco.getSterile());
								} else if (nomChampEntite.equals("ConformeArrivee")) {
									listeStrings.add(ObjectTypesFormatters
											.booleanLitteralFormatter(
											preDeco.getPrelevement()
											.getConformeArrivee()));
								} else if (nomChampEntite
										.equals("EtatIncomplet")) {
									listeStrings
											.add(preDeco.getEtatIncomplet());
								} else if (nomChampEntite.equals("Archive")) {
									listeStrings.add(preDeco.getArchive());
								} else {
									listeStrings.add("-");
								}
							} else {
								// Traitement des sousChamps de prélèvement
								if (parent.getChampEntite().getNom().equals(
										"BanqueId")) {
									if (recup.getBanque() != null) {
									if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getBanque()
												.getNom());
									} else if (nomChampEntite
											.equals("Identification")) {
										listeStrings.add(recup.getBanque()
												.getIdentification());
									} else if (nomChampEntite
											.equals("Description")) {
										listeStrings.add(recup.getBanque()
												.getDescription());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getBanque()
												.getArchive());
									} else if (nomChampEntite
											.equals("DefMaladies")) {
										listeStrings.add(recup.getBanque()
												.getDefMaladies());
									}
									}else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("NatureId")) {
									if (recup.getNature() != null) {
										if (nomChampEntite.equals("Nature")) {
											listeStrings.add(recup.getNature()
													.getNature());
										}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite()
											.getNom().equals("Risques")) {
									Iterator<Risque> risksIt = ManagerLocator
										.getPrelevementManager()
											.getRisquesManager(preDeco.getPrelevement())
											.iterator();
									StringBuffer riskb = new StringBuffer();
									while (risksIt.hasNext()) {
										riskb.append(risksIt.next().getNom());
										if (risksIt.hasNext()) {
											riskb.append(", ");
										} 
									}
									if (riskb.length() > 0) {
										listeStrings.add(riskb.toString());
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ConsentTypeId")) {
									if (recup.getConsentType() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getConsentType()
												.getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ConditTypeId")) {
									if (recup.getConditType() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getConditType()
												.getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ConditMilieuId")) {
									if (recup.getConditMilieu() != null) {
									if (nomChampEntite.equals("Milieu")) {
										listeStrings.add(recup
												.getConditMilieu().getMilieu());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("QuantiteUniteId")) {
									if (recup.getQuantiteUnite() != null) {
									if (nomChampEntite.equals("Unite")) {
										listeStrings.add(recup
												.getQuantiteUnite().getUnite());
									} else if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup
												.getQuantiteUnite().getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("PrelevementTypeId")) {
									if (recup.getPrelevementType() != null) {
									if (nomChampEntite.equals("IncaCat")) {
										listeStrings.add(recup
												.getPrelevementType()
												.getIncaCat());
									} else if (nomChampEntite.equals("Type")) {
										listeStrings
												.add(recup.getPrelevementType()
														.getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("MaladieId")) {
									if (recup.getMaladie() != null) {
									if (nomChampEntite.equals("Code")) {
										listeStrings.add(recup.getMaladie()
												.getCode());
									} else if (nomChampEntite
											.equals("DateDebut")) {
										listeStrings.add(recup.getMaladie()
												.getDateDebut());
									} else if (nomChampEntite
											.equals("DateDiagnostic")) {
										listeStrings.add(recup.getMaladie()
												.getDateDiagnostic());
									} else if (nomChampEntite.equals("Libelle")) {
										listeStrings.add(recup.getMaladie()
												.getLibelle());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("PreleveurId")) {
									if (recup.getPreleveur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getPreleveur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getPreleveur()
												.getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup.getPreleveur()
												.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ServicePreleveurId")) {
									if (recup.getServicePreleveur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup
												.getServicePreleveur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings
												.add(recup
														.getServicePreleveur()
														.getNom());
									}
								} else {
									listeStrings.add("-");
								}
								} else if (parent.getChampEntite().getNom()
										.equals("TransporteurId")) {
									if (recup.getTransporteur() != null) { 
									if (nomChampEntite.equals("Archive")) {
										listeStrings
												.add(recup.getTransporteur()
														.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup
												.getTransporteur().getNom());
									} else if (nomChampEntite
											.equals("ContactFax")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactFax());
									} else if (nomChampEntite
											.equals("ContactMail")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactMail());
									} else if (nomChampEntite
											.equals("ContactNom")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactNom());
									} else if (nomChampEntite
											.equals("ContactPrenom")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactPrenom());
									} else if (nomChampEntite
											.equals("ContactTel")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactTel());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("OperateurId")) {
									if (recup.getOperateur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getOperateur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getOperateur()
												.getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup.getOperateur()
												.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								}
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							listeStrings.add(getChampAnnotationValeur(recup, 
									res.getChamp().getChampAnnotation()));
						} else {
							listeStrings.add("-");
						}
					} else if (entite.getNom().equals("Echantillon")) {
						Echantillon recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof Echantillon) {
								recup = (Echantillon) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							EchantillonDTO echDeco = new EchantillonDTO(
									(Echantillon) recup);
							if (parent == null) {
								if (nomChampEntite.equals("Code")) {
									listeStrings.add(echDeco.getCode());
								} else if (nomChampEntite.equals("DateStock")) {
									listeStrings.add(ObjectTypesFormatters
											.dateRenderer2(echDeco
													.getDateStockage()));
								}  else if (nomChampEntite.equals("Lateralite")) {
									listeStrings.add(echDeco.getLateralite());
								} else if (nomChampEntite.equals("EmplacementId")) {
									listeStrings.add(echDeco
											.getEmplacementAdrl());
								} else if (nomChampEntite.equals("Quantite")) {
									listeStrings.add(echDeco.getQuantite());
								} else if (nomChampEntite
										.equals("QuantiteInit")) {
									listeStrings.add(echDeco
											.getQuantiteInitiale());
								} else if (nomChampEntite.equals("DelaiCgl")) {
									listeStrings.add(echDeco
											.getDelaiDeCongelation());
								} else if (nomChampEntite.equals("Tumoral")) {
									listeStrings.add(echDeco.getTumoral());
								} else if (nomChampEntite.equals("Sterile")) {
									listeStrings.add(echDeco.getSterile());
								} else if (nomChampEntite.equals(
										"ConformeTraitement")) {
									listeStrings.add(ObjectTypesFormatters
											.booleanLitteralFormatter(
											echDeco.getEchantillon()
											.getConformeTraitement()));
								} else if (nomChampEntite.equals(
										"ConformeCession")) {
									listeStrings.add(ObjectTypesFormatters
											.booleanLitteralFormatter(
											echDeco.getEchantillon()
											.getConformeCession()));
								} else if (nomChampEntite
										.equals("EtatIncomplet")) {
									listeStrings
											.add(echDeco.getEtatIncomplet());
								} else if (nomChampEntite
										.equals("Archive")) {
									listeStrings.add(echDeco.getArchive());
								} else if (nomChampEntite
										.equals("CodeOrganes")) {
									List<String> codes = ManagerLocator
										.getCodeAssigneManager()
										.formatCodesAsStringsManager(
											ManagerLocator.getCodeAssigneManager()
											.findCodesOrganeByEchantillonManager(
												echDeco.getEchantillon()));
									StringBuffer sb = new StringBuffer();
									for (int k = 0; k < codes.size(); k++) {
										sb.append(codes.get(k));
										if (k + 1 < codes.size()) {
											sb.append(", ");
										}
									}
									listeStrings.add(sb.toString());
								} else if (nomChampEntite
										.equals("CodeMorphos")) {
									List<String> codes = ManagerLocator
										.getCodeAssigneManager()
										.formatCodesAsStringsManager(
											ManagerLocator.getCodeAssigneManager()
											.findCodesMorphoByEchantillonManager(
												echDeco.getEchantillon()));
									StringBuffer sb = new StringBuffer();
									for (int k = 0; k < codes.size(); k++) {
										sb.append(codes.get(k));
										if (k + 1 < codes.size()) {
											sb.append(", ");
										}
									}
									listeStrings.add(sb.toString());
								} else {
									listeStrings.add("-");
								}
							} else {
								// Traitement des sousChamps d'échantillon
								if (parent.getChampEntite().getNom().equals(
										"PrelevementId")) {
									if (recup.getPrelevement() != null) {
									if (nomChampEntite.equals("Code")) {
										listeStrings.add(recup.getPrelevement()
												.getCode());
									} else if (nomChampEntite
											.equals("ConsentDate")) {
										listeStrings.add(recup.getPrelevement()
												.getConsentDate());
									} else if (nomChampEntite
											.equals("DatePrelevement")) {
										listeStrings.add(recup.getPrelevement()
												.getDatePrelevement());
									} else if (nomChampEntite
											.equals("ConditNbr")) {
										listeStrings.add(recup.getPrelevement()
												.getConditNbr());
									} else if (nomChampEntite
											.equals("DateDepart")) {
										listeStrings.add(recup.getPrelevement()
												.getDateDepart());
									} else if (nomChampEntite
											.equals("TransportTemp")) {
										listeStrings.add(recup.getPrelevement()
												.getTransportTemp());
									} else if (nomChampEntite
											.equals("DateArrivee")) {
										listeStrings.add(recup.getPrelevement()
												.getDateArrivee());
									} else if (nomChampEntite
											.equals("Quantite")) {
										listeStrings.add(recup.getPrelevement()
												.getQuantite());
									} else if (nomChampEntite
											.equals("PatientNda")) {
										listeStrings.add(recup.getPrelevement()
												.getPatientNda());
									} else if (nomChampEntite
											.equals("NumeroLabo")) {
										listeStrings.add(recup.getPrelevement()
												.getNumeroLabo());
									} else if (nomChampEntite.equals("Sterile")) {
										listeStrings.add(recup.getPrelevement()
												.getSterile());
									} else if (nomChampEntite
											.equals("EtatIncomplet")) {
										listeStrings.add(recup.getPrelevement()
												.getEtatIncomplet());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getPrelevement()
												.getArchive());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("BanqueId")) {
									if (recup.getBanque() != null) {
									if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getBanque()
												.getNom());
									} else if (nomChampEntite
											.equals("Identification")) {
										listeStrings.add(recup.getBanque()
												.getIdentification());
									} else if (nomChampEntite
											.equals("Description")) {
										listeStrings.add(recup.getBanque()
												.getDescription());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getBanque()
												.getArchive());
									} else if (nomChampEntite
											.equals("DefMaladies")) {
										listeStrings.add(recup.getBanque()
												.getDefMaladies());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ObjetStatutId")) {
									if (recup.getObjetStatut() != null) {
									if (nomChampEntite.equals("Statut")) {
										listeStrings.add(ObjectTypesFormatters
												.ILNObjectStatut(recup.getObjetStatut()));
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("EchantillonTypeId")) {
									if (recup.getEchantillonType() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings
												.add(recup.getEchantillonType()
														.getType());
									} else if (nomChampEntite.equals("IncaCat")) {
										listeStrings.add(recup
												.getEchantillonType()
												.getIncaCat());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("QuantiteUniteId")) {
									if (recup.getQuantiteUnite() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup
												.getQuantiteUnite().getType());
									} else if (nomChampEntite.equals("Unite")) {
										listeStrings.add(recup
												.getQuantiteUnite().getUnite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ModePrepaId")) {
									if (recup.getModePrepa() != null) {
									if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getModePrepa()
												.getNom());
									} else if (nomChampEntite.equals("NomEn")) {
										listeStrings.add(recup.getModePrepa()
												.getNomEn());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ReservationId")) {
									if (recup.getReservation() != null) {
									if (nomChampEntite.equals("Fin")) {
										listeStrings.add(recup.getReservation()
												.getFin());
									} else if (nomChampEntite.equals("Debut")) {
										listeStrings.add(recup.getReservation()
												.getDebut());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("EchanQualiteId")) {
									if (recup.getEchanQualite() != null) {
									if (nomChampEntite.equals("EchanQualite")) {
										listeStrings.add(recup
												.getEchanQualite()
												.getEchanQualite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("CollaborateurId")) {
									if (recup.getCollaborateur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup
												.getCollaborateur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup
												.getCollaborateur().getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings
												.add(recup.getCollaborateur()
														.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								}
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							listeStrings.add(getChampAnnotationValeur(recup, 
									res.getChamp().getChampAnnotation()));
						} else {
							listeStrings.add("-");
						}
					} else if (entite.getNom().equals("ProdDerive")) {
						ProdDerive recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof ProdDerive) {
								recup = (ProdDerive) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							ProdDeriveDecorator2 prodDeco = new ProdDeriveDecorator2(
									(ProdDerive) recup);
							if (parent == null) {
								if (nomChampEntite.equals("Code")) {
									listeStrings.add(recup.getCode());
								} else if (nomChampEntite.equals("CodeLabo")) {
									listeStrings.add(recup.getCodeLabo());
								} else if (nomChampEntite.equals("EmplacementId")) {
									listeStrings.add(prodDeco
											.getEmplacementAdrl());
								} else if (nomChampEntite.equals("VolumeInit")) {
									listeStrings.add(recup
											.getVolumeInit());
								} else if (nomChampEntite.equals("Volume")) {
									listeStrings.add(recup.getVolume());
								} else if (nomChampEntite.equals("Conc")) {
									listeStrings.add(recup
											.getConc());
								} else if (nomChampEntite.equals("DateStock")) {
									listeStrings.add(recup.getDateStock());
								} else if (nomChampEntite
										.equals("QuantiteInit")) {
									listeStrings.add(recup
											.getQuantiteInit());
								} else if (nomChampEntite.equals("Quantite")) {
									listeStrings.add(recup.getQuantite());
								} else if (nomChampEntite
										.equals("DateTransformation")) {
									listeStrings.add(recup
											.getDateTransformation());
								} else if (nomChampEntite
										.equals("EtatIncomplet")) {
									listeStrings.add(recup
											.getEtatIncomplet());
								} else if (nomChampEntite.equals("Archive")) {
									listeStrings.add(recup.getArchive());
								} else if (nomChampEntite.equals(
										"ConformeTraitement")) {
									listeStrings.add(ObjectTypesFormatters
											.booleanLitteralFormatter(
													recup
											.getConformeTraitement()));
								} else if (nomChampEntite.equals(
										"ConformeCession")) {
									listeStrings.add(ObjectTypesFormatters
											.booleanLitteralFormatter(
													recup
											.getConformeCession()));
								} else {
									listeStrings.add("-");
								}
							} else {
								// Traitement des sousChamps de produit dérivé
								if (parent.getChampEntite().getNom().equals(
										"BanqueId")) {
									if (recup.getBanque() != null) {
									if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getBanque()
												.getNom());
									} else if (nomChampEntite
											.equals("Identification")) {
										listeStrings.add(recup.getBanque()
												.getIdentification());
									} else if (nomChampEntite
											.equals("Description")) {
										listeStrings.add(recup.getBanque()
												.getDescription());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getBanque()
												.getArchive());
									} else if (nomChampEntite
											.equals("DefMaladies")) {
										listeStrings.add(recup.getBanque()
												.getDefMaladies());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ObjetStatutId")) {
									if (recup.getObjetStatut() != null) {
									if (nomChampEntite.equals("Statut")) {
										listeStrings.add(ObjectTypesFormatters
												.ILNObjectStatut(recup.getObjetStatut()));
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ProdTypeId")) {
									if(recup.getProdType() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getProdType()
												.getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ProdQualiteId")) {
									if (recup.getProdQualite() != null) {
									if (nomChampEntite.equals("ProdQualite")) {
										listeStrings.add(recup.getProdQualite()
												.getProdQualite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("QuantiteUniteId")) {
									if (recup.getQuantiteUnite() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup
												.getQuantiteUnite().getType());
									} else if (nomChampEntite.equals("Unite")) {
										listeStrings.add(recup
												.getQuantiteUnite().getUnite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("VolumeUniteId")) {
									if (recup.getVolumeUnite() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getVolumeUnite()
												.getType());
									} else if (nomChampEntite.equals("Unite")) {
										listeStrings.add(recup.getVolumeUnite()
												.getUnite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ConcUniteId")) {
									if (recup.getConcUnite() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getConcUnite()
												.getType());
									} else if (nomChampEntite.equals("Unite")) {
										listeStrings.add(recup.getConcUnite()
												.getUnite());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("CollaborateurId")) {
									if (recup.getCollaborateur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup
												.getCollaborateur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup
												.getCollaborateur().getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings
												.add(recup.getCollaborateur()
														.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ReservationId")) {
									if (recup.getReservation() != null) {
									if (nomChampEntite.equals("Fin")) {
										listeStrings.add(recup.getReservation()
												.getFin());
									} else if (nomChampEntite.equals("Debut")) {
										listeStrings.add(recup.getReservation()
												.getDebut());
									}
									} else {
										listeStrings.add("-");
									}
								}
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							listeStrings.add(getChampAnnotationValeur(recup, 
									res.getChamp().getChampAnnotation()));
						} else {
							listeStrings.add("-");
						}
					} else if (entite.getNom().equals("Cession")) {
						Cession recup = null;
						boolean found = false;
						Iterator<Object> itObj = listeObjets.iterator();
						while (!found && itObj.hasNext()) {
							Object temp = itObj.next();
							if (temp instanceof Cession) {
								recup = (Cession) temp;
								found = true;
							}
						}
						if (found && res.getChamp().getChampEntite() != null) {
							String nomChampEntite = res.getChamp()
									.getChampEntite().getNom();
							if (parent == null) {
								if (nomChampEntite.equals("Numero")) {
									listeStrings.add(recup.getNumero());
								} else if (nomChampEntite.equals("DemandeDate")) {
									listeStrings.add(recup.getDemandeDate());
								} else if (nomChampEntite.equals("EtudeTitre")) {
									listeStrings.add(recup.getEtudeTitre());
								} else if (nomChampEntite.equals("Description")) {
									listeStrings.add(recup.getDescription());
								} else if (nomChampEntite
										.equals("ValidationDate")) {
									listeStrings.add(recup.getValidationDate());
								} else if (nomChampEntite.equals("DepartDate")) {
									listeStrings.add(recup.getDepartDate());
								} else if (nomChampEntite.equals("ArriveeDate")) {
									listeStrings.add(recup.getArriveeDate());
								} else if (nomChampEntite
										.equals("Observations")) {
									listeStrings.add(recup.getObservations());
								} else if (nomChampEntite.equals("Temperature")) {
									listeStrings.add(recup.getTemperature());
								} else if (nomChampEntite
										.equals("DestructionDate")) {
									listeStrings
											.add(recup.getDestructionDate());
								} else if (nomChampEntite
										.equals("EtatIncomplet")) {
									listeStrings.add(recup.getEtatIncomplet());
								} else if (nomChampEntite.equals("Archive")) {
									listeStrings.add(recup.getArchive());
								} else {
									listeStrings.add("-");
								}
							} else {
								// Traitement des sousChamps d'échantillon
								if (parent.getChampEntite().getNom().equals(
										"BanqueId")) {
									if (recup.getBanque() != null) {
									if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getBanque()
												.getNom());
									} else if (nomChampEntite
											.equals("Identification")) {
										listeStrings.add(recup.getBanque()
												.getIdentification());
									} else if (nomChampEntite
											.equals("Description")) {
										listeStrings.add(recup.getBanque()
												.getDescription());
									} else if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getBanque()
												.getArchive());
									} else if (nomChampEntite
											.equals("DefMaladies")) {
										listeStrings.add(recup.getBanque()
												.getDefMaladies());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("CessionTypeId")) {
									if (recup.getCessionType() != null) {
									if (nomChampEntite.equals("Type")) {
										listeStrings.add(recup.getCessionType()
												.getType());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("CessionExamenId")) {
									if (recup.getCessionExamen() != null) {
									if (nomChampEntite.equals("Examen")) {
										listeStrings
												.add(recup.getCessionExamen()
														.getExamen());
									} else if (nomChampEntite
											.equals("ExamenEn")) {
										listeStrings.add(recup
												.getCessionExamen()
												.getExamenEn());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ContratId")) {
									if (recup.getContrat() != null) {
									if (nomChampEntite
											.equals("DateDemandeCession")) {
										listeStrings.add(recup.getContrat()
												.getDateDemandeCession());
									} else if (nomChampEntite
											.equals("DateDemandeRedaction")) {
										listeStrings.add(recup.getContrat()
												.getDateDemandeRedaction());
									} else if (nomChampEntite
											.equals("DateEnvoiContrat")) {
										listeStrings.add(recup.getContrat()
												.getDateEnvoiContrat());
									} else if (nomChampEntite
											.equals("DateSignature")) {
										listeStrings.add(recup.getContrat()
												.getDateSignature());
									} else if (nomChampEntite
											.equals("DateValidation")) {
										listeStrings.add(recup.getContrat()
												.getDateValidation());
									} else if (nomChampEntite
											.equals("Description")) {
										listeStrings.add(recup.getContrat()
												.getDescription());
									} else if (nomChampEntite.equals("Numero")) {
										listeStrings.add(recup.getContrat()
												.getNumero());
									} else if (nomChampEntite
											.equals("TitreProjet")) {
										listeStrings.add(recup.getContrat()
												.getTitreProjet());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("DestinataireId")) {
									if (recup.getDestinataire() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings
												.add(recup.getDestinataire()
														.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup
												.getDestinataire().getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup
												.getDestinataire().getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ServiceDestId")) {
									if (recup.getServiceDest() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getServiceDest()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getServiceDest()
												.getNom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("DemandeurId")) {
									if (recup.getDemandeur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getDemandeur()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getDemandeur()
												.getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup.getDemandeur()
												.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("CessionStatutId")) {
									if (recup.getCessionStatut() != null) {
									if (nomChampEntite.equals("Statut")) {
										listeStrings.add(ObjectTypesFormatters
											.ILNObjectStatut(recup.getCessionStatut()));
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("ExecutantId")) {
									if (recup.getExecutant() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings.add(recup.getExecutant()
												.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup.getExecutant()
												.getNom());
									} else if (nomChampEntite.equals("Prenom")) {
										listeStrings.add(recup.getExecutant()
												.getPrenom());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("TransporteurId")) {
									if (recup.getTransporteur() != null) {
									if (nomChampEntite.equals("Archive")) {
										listeStrings
												.add(recup.getTransporteur()
														.getArchive());
									} else if (nomChampEntite.equals("Nom")) {
										listeStrings.add(recup
												.getTransporteur().getNom());
									} else if (nomChampEntite
											.equals("ContactFax")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactFax());
									} else if (nomChampEntite
											.equals("ContactMail")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactMail());
									} else if (nomChampEntite
											.equals("ContactNom")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactNom());
									} else if (nomChampEntite
											.equals("ContactPrenom")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactPrenom());
									} else if (nomChampEntite
											.equals("ContactTel")) {
										listeStrings.add(recup
												.getTransporteur()
												.getContactTel());
									}
									} else {
										listeStrings.add("-");
									}
								} else if (parent.getChampEntite().getNom()
										.equals("DestructionMotifId")) {
									if (recup.getDestructionMotif() != null) {
									if (nomChampEntite.equals("Motif")) {
										listeStrings.add(recup
												.getDestructionMotif()
												.getMotif());
									}
									} else {
										listeStrings.add("-");
									}
								}
							}
						} else if (res.getChamp().getChampAnnotation() != null) {
							listeStrings.add(getChampAnnotationValeur(recup, 
									res.getChamp().getChampAnnotation()));
						} else {
							listeStrings.add("-");
						}
					} else {
						listeStrings.add("-");
					}
				}
			}
			matriceAffichable.add(listeStrings);
		}
	}

	private List<Listitem> loadListitems(Listbox lbox) {
		List<Listitem> listeLis = new ArrayList<Listitem>();
		// On ordonne les résultats par position

		List<Resultat> resultats = new ArrayList<Resultat>();
		resultats.addAll(affichage.getResultats());
		// On trie les résultats par position
		if (resultats.size() > 1) {
			for (int i = 1; i < resultats.size(); i++) {
				for (int j = i - 1; j >= 0; j--) {
					if (resultats.get(j + 1).getPosition() < resultats.get(j)
							.getPosition()) {
						Resultat temp = resultats.get(j + 1);
						resultats.set(j + 1, resultats.get(j));
						resultats.set(j, temp);
					}
				}
			}
		}

		// On itère la matrice affichable
		Iterator<List<Object>> itRow = matriceAffichable.iterator();
		Iterator<List<Object>> itRowObjet = matriceObjets.iterator();
		while (itRow.hasNext()) {
			Listitem li = new Listitem();
			li.setParent(lbox);
			List<Object> rowAffichable = itRow.next();
			List<Object> rowObjet = itRowObjet.next();
			li.setValue(rowObjet);
			Iterator<Object> itCell = rowAffichable.iterator();
			Iterator<Resultat> itRes = resultats.iterator();
			while (itCell.hasNext()) {
				Object o = itCell.next();
				Resultat res = itRes.next();
				if (o != null) {
					// Si la chaine est vide, on met un -
					String s = o.toString();
					if (s.trim().equals("")) {
						s = "-";
					}
					Listcell cell = new Listcell(s);
					cell.setParent(li);
					// On regarde dans le champ du resultat si c'est un entiteId
					if (res != null && res.getChamp() != null) {
						if (res != null) {
							if (res.getChamp() != null) {
								if (res.getChamp().getChampEntite() != null) {
									Entite entite = res.getChamp()
											.getChampEntite().getEntite();
									String nomChampEntite = res.getChamp()
											.getChampEntite().getNom();
									if (entite.getNom().equals("Patient")
											&& nomChampEntite.equals("Nip")) {
										Patient recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Patient) {
												recup = (Patient) temp;
												cell.addForward(null, cell
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickPatientNip",
														recup);
												cell.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom()
											.equals("Maladie")
											&& nomChampEntite.equals("Code")) {
										Maladie recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Maladie) {
												recup = (Maladie) temp;
												cell.addForward(null, cell
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickMaladieCode",
														recup);
												cell.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"Prelevement")
											&& nomChampEntite.equals("Code")) {
										Prelevement recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Prelevement) {
												recup = (Prelevement) temp;
												cell
														.addForward(
																null,
																cell
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickPrelevementCode",
																recup);
												cell.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"Echantillon")
											&& nomChampEntite.equals("Code")) {
										Echantillon recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Echantillon) {
												recup = (Echantillon) temp;
												cell
														.addForward(
																null,
																cell
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickEchantillonCode",
																recup);
												cell.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"ProdDerive")
											&& nomChampEntite.equals("Code")) {
										ProdDerive recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof ProdDerive) {
												recup = (ProdDerive) temp;
												cell
														.addForward(
																null,
																cell
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickProdDeriveCode",
																recup);
												cell.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom()
											.equals("Cession")
											&& nomChampEntite.equals("Numero")) {
										Cession recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Cession) {
												recup = (Cession) temp;
												cell.addForward(null, cell
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickCessionNumero",
														recup);
												cell.setClass("formLink");
												break;
											}
										}
									}
								}
							}
						}
					}
				} else {
					new Listcell("-").setParent(li);
				}
			}
			listeLis.add(li);
		}
		return listeLis;
	}
	
	private void loadGridContent(Grid lbox) {
		List<ResultatRow> rows = new ArrayList<ResultatRow>();
		for (int i = 0; i < matriceAffichable.size(); i++) {
			rows.add(new ResultatRow(matriceAffichable.get(i),
					matriceObjets.get(i)));
		}
		ListModel ml = new ListModelList(rows);
		lbox.setModel(ml);
		
		List<Resultat> resultats = new ArrayList<Resultat>();
		resultats.addAll(affichage.getResultats());
		// On trie les résultats par position
		if (resultats.size() > 1) {
			for (int i = 1; i < resultats.size(); i++) {
				for (int j = i - 1; j >= 0; j--) {
					if (resultats.get(j + 1).getPosition() < resultats.get(j)
							.getPosition()) {
						Resultat temp = resultats.get(j + 1);
						resultats.set(j + 1, resultats.get(j));
						resultats.set(j, temp);
					}
				}
			}
		}
		ResultatRowRenderer renderer = new ResultatRowRenderer();
		renderer.setResultats(resultats);
		lbox.setRowRenderer(renderer);
	}
	
	private void loadGridRows(Grid lbox) {
		if (lbox.getRows() != null) {
			lbox.getRows().setParent(null);
		}
		Rows rows = new Rows();
		rows.setParent(lbox);
		List<Resultat> resultats = new ArrayList<Resultat>();
		resultats.addAll(affichage.getResultats());
		// On trie les résultats par position
		if (resultats.size() > 1) {
			for (int i = 1; i < resultats.size(); i++) {
				for (int j = i - 1; j >= 0; j--) {
					if (resultats.get(j + 1).getPosition() < resultats.get(j)
							.getPosition()) {
						Resultat temp = resultats.get(j + 1);
						resultats.set(j + 1, resultats.get(j));
						resultats.set(j, temp);
					}
				}
			}
		}

		// On itère la matrice affichable
		Iterator<List<Object>> itRow = matriceAffichable.iterator();
		Iterator<List<Object>> itRowObjet = matriceObjets.iterator();
		while (itRow.hasNext()) {
			Row row = new Row();
			row.setParent(rows);
			List<Object> rowAffichable = itRow.next();
			List<Object> rowObjet = itRowObjet.next();
			Iterator<Object> itCell = rowAffichable.iterator();
			Iterator<Resultat> itRes = resultats.iterator();
			while (itCell.hasNext()) {
				Object o = itCell.next();
				Resultat res = itRes.next();
				if (o != null) {
					// Si la chaine est vide, on met un -
					String s = o.toString();
					if (s.trim().equals("")) {
						s = "-";
					}
					Label label = new Label();
					label.setValue(s);
					label.setParent(row);
					// On regarde dans le champ du resultat si c'est un entiteId
					if (res != null && res.getChamp() != null) {
						if (res != null) {
							if (res.getChamp() != null) {
								if (res.getChamp().getChampEntite() != null) {
									Entite entite = res.getChamp()
											.getChampEntite().getEntite();
									String nomChampEntite = res.getChamp()
											.getChampEntite().getNom();
									if (entite.getNom().equals("Patient")
											&& nomChampEntite.equals("Nip")) {
										Patient recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Patient) {
												recup = (Patient) temp;
												label.addForward(null, label
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickPatientNip",
														recup);
												label.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom()
											.equals("Maladie")
											&& nomChampEntite.equals("Code")) {
										Maladie recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Maladie) {
												recup = (Maladie) temp;
												label.addForward(null, label
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickMaladieCode",
														recup);
												label.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"Prelevement")
											&& nomChampEntite.equals("Code")) {
										Prelevement recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Prelevement) {
												recup = (Prelevement) temp;
												label
														.addForward(
																null,
																label
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickPrelevementCode",
																recup);
												label.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"Echantillon")
											&& nomChampEntite.equals("Code")) {
										Echantillon recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Echantillon) {
												recup = (Echantillon) temp;
												label
														.addForward(
																null,
																label
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickEchantillonCode",
																recup);
												label.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom().equals(
											"ProdDerive")
											&& nomChampEntite.equals("Code")) {
										ProdDerive recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof ProdDerive) {
												recup = (ProdDerive) temp;
												label
														.addForward(
																null,
																label
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent()
																		.getParent(),
																"onClickProdDeriveCode",
																recup);
												label.setClass("formLink");
												break;
											}
										}
									} else if (entite.getNom()
											.equals("Cession")
											&& nomChampEntite.equals("Numero")) {
										Cession recup = null;
										Iterator<Object> itObj = rowObjet
												.iterator();
										while (itObj.hasNext()) {
											Object temp = itObj.next();
											if (temp instanceof Cession) {
												recup = (Cession) temp;
												label.addForward(null, label
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent()
														.getParent(),
														"onClickCessionNumero",
														recup);
												label.setClass("formLink");
												break;
											}
										}
									}
								}
							}
						}
					}
				} else {
					new Label("-").setParent(row);
				}
			}
		}
	}

	
	
	public String getChampAnnotationValeur(TKAnnotableObject obj, 
			ChampAnnotation ca) {
		List<AnnotationValeur> avs = ManagerLocator
			.getAnnotationValeurManager()
				.findByChampAndObjetManager(ca, obj);
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < avs.size(); j++) {
			sb.append(avs.get(j).formateAnnotationValeur());
			if (j + 1 < avs.size()) {
				sb.append(";");
			}
		}
		return sb.toString();
	}
}