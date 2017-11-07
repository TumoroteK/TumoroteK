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
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveDecorator2;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Classe 'Decorateur' qui reprend les attributs d'un objet (entité) pour les
 * afficher par l'interface dans une liste d'objets entités.
 * Date: 29/01/10
 * 
 * @version 2.0
 * @author GOUSSEAU Maxime
 * 
 */
public class ObjetDecorator implements Comparable<Object> {

	private Object objet;
	private Affichage affichage;

	public ObjetDecorator(Object obj, Affichage affichage) {
		this.objet = obj;
		this.affichage = affichage;
	}

	public Object getObjet() {
		return objet;
	}

	public Affichage getAffichage() {
		return affichage;
	}

	/**
	 * Méthode qui ajoute dans une liste d'objets les champs d'un objet à 
	 * afficher.
	 * @return Liste d'objets affichables.
	 */
	public List<Object> getCells() {
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
					} else if (res.getChamp().getChampAnnotation() != null) {
						entite = res.getChamp().getChampAnnotation()
								.getTableAnnotation().getEntite();
					} else {
						liste.add("-");
					}
				} else {
					liste.add("-");
				}
			} else {
				liste.add("-");
			}
			if (entite != null) {
				List<Object> lObj = new ArrayList<Object>();
				lObj.add(this.objet);
				Object recup = null;
				if (objet.getClass().getSimpleName().equals(entite.getNom())) {
					recup = objet;
				} else {
					List<Object> recups = ManagerLocator
							.getCorrespondanceManager()
							.recupereEntitesViaDAutres(lObj, entite.getNom());
					if (recups == null || recups.size() < 1) {
						liste.add("-");
					} else {
						recup = recups.get(0);
					}
				}

				if (entite.getNom().equals("Patient")) {
					if (res.getChamp().getChampEntite() != null) {
						String nomChampEntite = res.getChamp().getChampEntite()
								.getNom();
						if (nomChampEntite.equals("Nip")) {
							liste.add(((Patient) recup).getNip());
						} else if (nomChampEntite.equals("Nom")) {
							liste.add(((Patient) recup).getNom());
						} else if (nomChampEntite.equals("NomNaissance")) {
							liste.add(((Patient) recup).getNomNaissance());
						} else if (nomChampEntite.equals("Prenom")) {
							liste.add(((Patient) recup).getPrenom());
						} else if (nomChampEntite.equals("Sexe")) {
							liste.add(((Patient) recup).getSexe());
						} else if (nomChampEntite.equals("DateNaissance")) {
							liste.add(((Patient) recup).getDateNaissance());
						} else if (nomChampEntite.equals("VilleNaissance")) {
							liste.add(((Patient) recup).getVilleNaissance());
						} else if (nomChampEntite.equals("PaysNaissance")) {
							liste.add(((Patient) recup).getPaysNaissance());
						} else if (nomChampEntite.equals("PatientEtat")) {
							liste.add(((Patient) recup).getPatientEtat());
						} else if (nomChampEntite.equals("DateEtat")) {
							liste.add(((Patient) recup).getDateEtat());
						} else if (nomChampEntite.equals("DateDeces")) {
							liste.add(((Patient) recup).getDateDeces());
						} else if (nomChampEntite.equals("EtatIncomplet")) {
							liste.add(((Patient) recup).getEtatIncomplet());
						} else if (nomChampEntite.equals("Archive")) {
							liste.add(((Patient) recup).getArchive());
						} else {
							liste.add("-");
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						liste.add(ManagerLocator.getChampAnnotationManager()
								.findAnnotationValeurManager(recup,
										res.getChamp().getChampAnnotation()));
					} else {
						liste.add("-");
					}
				} else if (entite.getNom().equals("Maladie")) {
					if (res.getChamp().getChampEntite() != null) {
						String nomChampEntite = res.getChamp().getChampEntite()
								.getNom();
						MaladieDecorator malDeco = new MaladieDecorator(
								(Maladie) recup);
						if (nomChampEntite.equals("PatientId")) {
							liste.add(malDeco.getPatientNomAndPrenom());
						} else if (nomChampEntite.equals("Libelle")) {
							liste.add(malDeco.getLibelle());
						} else if (nomChampEntite.equals("Code")) {
							liste.add(malDeco.getCode());
						} else if (nomChampEntite.equals("DateDiagnostic")) {
							liste.add(malDeco.getFormattedDateDiag());
						} else if (nomChampEntite.equals("DateDebut")) {
							liste.add(malDeco.getFormattedDateDebut());
						} else {
							liste.add("-");
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						liste.add(ManagerLocator.getChampAnnotationManager()
								.findAnnotationValeurManager(recup,
										res.getChamp().getChampAnnotation()));
					} else {
						liste.add("-");
					}
				} else if (entite.getNom().equals("Prelevement")) {
					if (res.getChamp().getChampEntite() != null) {
						String nomChampEntite = res.getChamp().getChampEntite()
								.getNom();
						PrelevementDecorator2 preDeco = new PrelevementDecorator2(
								(Prelevement) recup);
						if (nomChampEntite.equals("BanqueId")) {
							liste.add(preDeco.getBanque());
						} else if (nomChampEntite.equals("Code")) {
							liste.add(preDeco.getCode());
						} else if (nomChampEntite.equals("NatureId")) {
							liste.add(preDeco.getNature());
						} else if (nomChampEntite.equals("MaladieId")) {
							liste.add(preDeco.getMaladie());
						} else if (nomChampEntite.equals("ConsentTypeId")) {
							liste.add(preDeco.getConsentement());
						} else if (nomChampEntite.equals("ConsentDate")) {
							liste.add(preDeco.getConsentementDate());
						} else if (nomChampEntite.equals("PreleveurId")) {
							liste.add(preDeco.getPreleveurNomAndPrenom());
						} else if (nomChampEntite.equals("ServicePreleveurId")) {
							liste.add(preDeco.getServicePreleveur());
						} else if (nomChampEntite.equals("DatePrelevement")) {
							liste.add(preDeco.getFormattedPrelevementDate());
						} else if (nomChampEntite.equals("PrelevementTypeId")) {
							liste.add(preDeco.getType());
						} else if (nomChampEntite.equals("ConditTypeId")) {
							liste.add(preDeco.getTypeConditionnement());
						} else if (nomChampEntite.equals("ConditMilieuId")) {
							liste.add(preDeco.getMilieuConditionnement());
						} else if (nomChampEntite.equals("ConditNbr")) {
							liste.add(preDeco.getNombreConditionnement());
						} else if (nomChampEntite.equals("DateDepart")) {
							liste.add(preDeco.getFormattedDateDepart());
						} else if (nomChampEntite.equals("TransporteurId")) {
							liste.add(preDeco.getTransporteur());
						} else if (nomChampEntite.equals("TransportTemp")) {
							liste.add(preDeco.getTransportTemperature());
						} else if (nomChampEntite.equals("DateArrivee")) {
							liste.add(preDeco.getFormattedDateArrivee());
						} else if (nomChampEntite.equals("OperateurId")) {
							liste.add(preDeco.getOperateurNomAndPrenom());
						} else if (nomChampEntite.equals("Quantite")) {
							liste.add(preDeco.getQuantite());
						} else if (nomChampEntite.equals("PatientNda")) {
							liste.add(preDeco.getNdaPatient());
						} else if (nomChampEntite.equals("NumeroLabo")) {
							liste.add(preDeco.getNumeroLaboratoire());
						} else if (nomChampEntite.equals("Sterile")) {
							liste.add(preDeco.getSterile());
						} else if (nomChampEntite.equals("EtatIncomplet")) {
							liste.add(preDeco.getEtatIncomplet());
						} else if (nomChampEntite.equals("Archive")) {
							liste.add(preDeco.getArchive());
						} else {
							liste.add("-");
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						liste.add(ManagerLocator.getChampAnnotationManager()
								.findAnnotationValeurManager(recup,
										res.getChamp().getChampAnnotation()));
					} else {
						liste.add("-");
					}
				} else if (entite.getNom().equals("Echantillon")) {
					if (res.getChamp().getChampEntite() != null) {
						String nomChampEntite = res.getChamp().getChampEntite()
								.getNom();
						EchantillonDTO echDeco = new EchantillonDTO(
								(Echantillon) recup);
						if (nomChampEntite.equals("BanqueId")) {
							liste.add(echDeco.getBanque());
						} else if (nomChampEntite.equals("PrelevementId")) {
							liste.add(echDeco.getPrelevement());
						} else if (nomChampEntite.equals("CollaborateurId")) {
							liste.add(echDeco.getCollaborateurNomAndPrenom());
						} else if (nomChampEntite.equals("Code")) {
							liste.add(echDeco.getCode());
						} else if (nomChampEntite.equals("ObjetStatutId")) {
							liste.add(Labels.getLabel("Statut." + echDeco.getStatut().getStatut()));
						} else if (nomChampEntite.equals("DateStock")) {
							liste.add(ObjectTypesFormatters
									.dateRenderer2(echDeco.getDateStockage()));
						} else if (nomChampEntite.equals("EmplacementId")) {
							liste.add(echDeco.getEmplacementAdrl());
						} else if (nomChampEntite.equals("EchantillonTypeId")) {
							liste.add(echDeco.getType());
						} else if (nomChampEntite.equals("AdicapOrganeId")) {
							liste.add(echDeco.getAdicapOrgane());
						} else if (nomChampEntite.equals("Lateralite")) {
							liste.add(echDeco.getLateralite());
						} else if (nomChampEntite.equals("Quantite")) {
							liste.add(echDeco.getQuantite());
						} else if (nomChampEntite.equals("QuantiteInit")) {
							liste.add(echDeco.getQuantiteInitiale());
						} else if (nomChampEntite.equals("DelaiCgl")) {
							liste.add(echDeco.getDelaiDeCongelation());
						} else if (nomChampEntite.equals("EchanQualiteId")) {
							liste.add(echDeco.getQualite());
						} else if (nomChampEntite.equals("Tumoral")) {
							liste.add(echDeco.getTumoral());
						} else if (nomChampEntite.equals("ModePrepaId")) {
							liste.add(echDeco.getModeDePreparation());
						} else if (nomChampEntite.equals("Sterile")) {
							liste.add(echDeco.getSterile());
					//	}  else if (nomChampEntite.equals("ReservationId")) {
					//		liste.add(echDeco.getReservation());
						} else if (nomChampEntite.equals("EtatIncomplet")) {
							liste.add(echDeco.getEtatIncomplet());
						} else if (nomChampEntite.equals("EtatIncomplet")) {
							liste.add(echDeco.getArchive());
						} else if (nomChampEntite.equals("Fichier")) {
							//liste.add(echDeco.getFichier());
						} else {
							liste.add("-");
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						liste.add(ManagerLocator.getChampAnnotationManager()
								.findAnnotationValeurManager(recup,
										res.getChamp().getChampAnnotation()));
					} else {
						liste.add("-");
					}
				} else if (entite.getNom().equals("ProdDerive")) {
					if (res.getChamp().getChampEntite() != null) {
						String nomChampEntite = res.getChamp().getChampEntite()
								.getNom();
						ProdDeriveDecorator2 prodDeco = new ProdDeriveDecorator2(
								(ProdDerive) recup);
						if (nomChampEntite.equals("BanqueId")) {
							liste.add(prodDeco.getBanque());
						} else if (nomChampEntite.equals("ProdTypeId")) {
							liste.add(prodDeco.getType());
						} else if (nomChampEntite.equals("Code")) {
							liste.add(prodDeco.getCode());
						} else if (nomChampEntite.equals("CodeLabo")) {
							liste.add(prodDeco.getCodeLabo());
						} else if (nomChampEntite.equals("ObjetStatutId")) {
							liste.add(prodDeco.getStatut());
						} else if (nomChampEntite.equals("CollaborateurId")) {
							liste.add(prodDeco.getCollaborateurNomAndPrenom());
						} else if (nomChampEntite.equals("VolumeInit")) {
							liste.add(prodDeco.getVolumeInitial());
						} else if (nomChampEntite.equals("Volume")) {
							liste.add(prodDeco.getVolume());
						} else if (nomChampEntite.equals("Conc")) {
							liste.add(prodDeco.getConcentration());
						} else if (nomChampEntite.equals("DateStock")) {
							liste.add(prodDeco.getDateStock());
						} else if (nomChampEntite.equals("EmplacementId")) {
							liste.add(prodDeco.getEmplacementAdrl());
						} else if (nomChampEntite.equals("QuantiteInit")) {
							liste.add(prodDeco.getQuantiteInitiale());
						} else if (nomChampEntite.equals("Quantite")) {
							liste.add(prodDeco.getQuantite());
						} else if (nomChampEntite.equals("ProdQualiteId")) {
							liste.add(prodDeco.getQualite());
						} else if (nomChampEntite.equals("TransformationId")) {
							liste.add(prodDeco.getTransformation());
						} else if (nomChampEntite
								.equals("DateTransformation")) {
							liste.add(prodDeco
									.getFormattedDateTransformation());
						} else if (nomChampEntite.equals("ReservationId")) {
							liste.add(prodDeco.getReservation());
						} else if (nomChampEntite.equals("EtatIncomplet")) {
							liste.add(prodDeco.getEtatIncomplet());
						} else if (nomChampEntite.equals("Archive")) {
							liste.add(prodDeco.getArchive());
						} else {
							liste.add("-");
						}
					} else if (res.getChamp().getChampAnnotation() != null) {
						liste.add(ManagerLocator.getChampAnnotationManager()
								.findAnnotationValeurManager(recup,
										res.getChamp().getChampAnnotation()));
					} else {
						liste.add("-");
					}
				} else {
					liste.add("-");
				}
			}
		}
		return liste;
	}

	/**
	 * Decore une liste d'objets.
	 * 
	 * @param objets
	 * @return objets décorés.
	 */
	public static List<ObjetDecorator> decorateListe(List<Object> objs,
			Affichage aff) {
		List<ObjetDecorator> liste = new ArrayList<ObjetDecorator>();
		Iterator<Object> it = objs.iterator();
		while (it.hasNext()) {
			liste.add(new ObjetDecorator(it.next(), aff));
		}
		return liste;
	}

	@Override
	public int compareTo(Object o) {
		ObjetDecorator obj = (ObjetDecorator) o;
		if (affichage != null) {
			List<Resultat> resultats = new ArrayList<Resultat>();
			resultats.addAll(affichage.getResultats());
			//On trie les résultats par ordre de position
			if (resultats.size() > 1) {
				for (int i = 1; i < resultats.size(); i++) {
					for (int j = i - 1; j >= 0; j--) {
						if (resultats.get(j+1).getOrdreTri() < resultats.get(
								j).getOrdreTri()) {
							Resultat temp = resultats.get(j + 1);
							resultats.set(j + 1, resultats.get(j));
							resultats.set(j, temp);
						}
					}
				}
			}
			
			List<Object> thisListObjets = this.getCells();
			List<Object> objListObjets = obj.getCells();
			
			//On parcourt la liste de résultats
			/*
			 * (Les objets dans getCells sont ordonnés par les positions des
			 * résultats)
			 */
			for (int i = 0; i < resultats.size(); i++) {
				if (thisListObjets.get(resultats.get(i).getPosition() - 1) != null
							&& objListObjets
									.get(resultats.get(i).getPosition() - 1) != null) {
					if (resultats.get(i).getTri()) {
					
						if (((Comparable) thisListObjets.get(resultats.get(i)
								.getPosition() - 1))
								.compareTo((Comparable) objListObjets
										.get(resultats.get(i).getPosition() - 1)) != 0) {
							return ((Comparable) thisListObjets.get(resultats
									.get(i).getPosition() - 1))
									.compareTo((Comparable) objListObjets
											.get(resultats.get(i).getPosition() - 1));
						}

					} else {
						if (((Comparable) thisListObjets.get(resultats.get(i)
								.getPosition() - 1))
								.compareTo((Comparable) objListObjets
										.get(resultats.get(i).getPosition() - 1)) != 0) {
							return -((Comparable) thisListObjets.get(resultats
									.get(i).getPosition() - 1))
									.compareTo((Comparable) objListObjets
											.get(resultats.get(i).getPosition() - 1));
						}
					}
				}
			}
		}
		return 0;
	}
}
