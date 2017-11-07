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
package fr.aphp.tumorotek.action.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.prodderive.ProdDeriveDecorator2;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
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
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Classe utilitaire regroupant les methodes optimisées 
 * de récupération des objets et 
 * d'affichage du module de Recherches complexes.
 * Date: 11/03/2013
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class RechercheUtils {

	public static List<Object> getListeObjetsCorrespondants(Object o, 
												Affichage affichage,
												String reservedEntite) {
		List<Object> liste = new ArrayList<Object>();
		/* On itère la liste des résultats de l'affichage. */
		Iterator<Resultat> itRes = affichage.getResultats().iterator();
		Entite entite = null;
		Entite previous = null;
		Object recup = null;
		while (itRes.hasNext()) {
			Resultat res = itRes.next();
			/* On récupère l'entité depuis le champ du Resultat. */
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
				// hack pour Etalissement Preleveur du Prelevement
				if (!entite.getNom().equals("Service")) {
					// ne relance pas la recherche si entite deja recuperee
					if (!entite.equals(previous)) {
						if (o.getClass().getSimpleName().equals(entite.getNom())) {
							recup = o;
						} else {
							List<Object> lObj = new ArrayList<Object>();
							lObj.add(o);
							List<Object> recups = ManagerLocator
									.getCorrespondanceManager()
									.recupereEntitesViaDAutres(lObj, entite.getNom());
							if (recups != null && recups.size() >= 1) {
								recup = recups.get(0);
							}
						}
						if (reservedEntite == null 
								|| entite.getNom().equals(reservedEntite)) {
							liste.add(recup);
						}
					} else {
						if (reservedEntite == null 
								|| entite.getNom().equals(reservedEntite)) {
							liste.add(recup);
						}
					}
					
					previous = entite;
				} else {
					liste.add(null);
				}
			}
		}
		return liste;
	}
	
	public static void loadMatriceAffichable(List<List<Object>> matObjs, 
			List<List<Object>> matAfs, Affichage affichage) {
		// On itère la matrice
		Iterator<List<Object>> itMat = matObjs.iterator();
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
						if (found) {
							listeStrings
								.add(getStringValueFromPatient(recup, res.getChamp()));
						} else {
							listeStrings.add(null);
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
						if (found) {
							listeStrings.add(getStringValueFromMaladie(
									recup, res.getChamp(), parent));
						} else {
							listeStrings.add(null);
						}
					} else if (entite.getNom().equals("Prelevement")
							|| entite.getNom().equals("Service")) {
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
						if (found) {
							listeStrings.add( 
								getStringValueFromPrelevement(
								recup, res.getChamp(), parent));
						} else {
							listeStrings.add(null);
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
						if (found) {
							EchantillonDTO echDeco = new EchantillonDTO(
									(Echantillon) recup);
							listeStrings.add( 
									getStringValueFromEchantillon(
								echDeco, res.getChamp(), parent));
						} else {
							listeStrings.add(null);
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
						if (found) {
							ProdDeriveDecorator2 prodDeco = 
								new ProdDeriveDecorator2((ProdDerive) recup);
							listeStrings.add( 
									getStringValueFromDerive(prodDeco, 
											res.getChamp(), parent));
						} else {
							listeStrings.add(null);
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
						if (found) {
							listeStrings.add( 
									getStringValueFromCession(recup, 
											res.getChamp(), parent));
						} else {
							listeStrings.add(null);
						}
						
					} else {
						listeStrings.add("-");
					}
				}
			}
			matAfs.add(listeStrings);
		}
	}
	
	private static String getStringValueFromPatient(Patient pat, 
													Champ chp) {
		if (pat != null && chp != null) {	
			if (chp.getChampEntite() != null) {
				return ObjectTypesFormatters
					.formatObject(ManagerLocator.getChampManager()
					.getValueForObjectManager(chp, pat, false));
			} else if (chp.getChampAnnotation() != null) {
				return getChampAnnotationValeur(pat, 
						chp.getChampAnnotation());
			}
		}
		return "-";
	}
	
	private static String 
		getStringValueFromMaladie(Maladie maladie, 
									Champ chp, Champ parent) {
		
		if (maladie != null && chp != null) {	
			if (chp.getChampEntite() != null) {
//				String nomChampEntite = 
//						res.getChamp().getChampEntite().getNom();
//	
				if (parent == null) {
					return ObjectTypesFormatters
					.formatObject(ManagerLocator.getChampManager()
					.getValueForObjectManager(chp, 
							maladie, false));
				} else {
					// Traitement des sousChamps de patient
					if (parent.getChampEntite().getNom()
											.equals("PatientId")) {
						return ObjectTypesFormatters
							.formatObject(ManagerLocator.getChampManager()
						.getValueForObjectManager(parent, 
								maladie, false));
//						if (malDeco.getMaladie().getPatient() != null) {
//							return getStringValueFromPatient(malDeco
//									.getMaladie().getPatient(), res);
//						}
					}
				}
			}
		}
		return "-";
	}
	
	private static String 
		getStringValueFromPrelevement(Prelevement prel, 
										Champ chp, Champ parent) {
		if (prel != null && chp != null) {	
			if (chp.getChampEntite() != null) {

				if (parent == null) {
					if (chp.getChampEntite().getNom()
										.equals("EtablissementId")) {
						if (prel.getServicePreleveur() != null) {
						return prel.getServicePreleveur()
								.getEtablissement().getNom();
						} else if (prel.getPreleveur() != null
								&& prel.getPreleveur()
										.getEtablissement() != null) {
							return prel.getPreleveur()
								.getEtablissement().getNom();
						}
					} 
					
					return ObjectTypesFormatters
					.formatObject(ManagerLocator.getChampManager()
					.getValueForObjectManager(chp, 
							prel, false));
 
				} else {
//					if (parent.getChampEntite().getNom().equals("BanqueId")
//							&& preDeco.getPrelevement()
//											.getBanque() != null) {
//							return ObjectTypesFormatters
//								.formatObject(ManagerLocator.getChampManager()
//							.getValueForObjectManager(res.getChamp(), 
//							preDeco.getPrelevement().getBanque(), false));
//					}
					if (parent.getChampEntite()
												.getNom().equals("MaladieId")) {
						return ObjectTypesFormatters
						.formatObject(getStringValueFromMaladie(prel.getMaladie(), 
								 parent, null));
					} else if (parent.getChampEntite().getNom()
							.equals("Risques")) {
						Iterator<Risque> risksIt = ManagerLocator
							.getPrelevementManager()
								.getRisquesManager(prel).iterator();
						StringBuffer sb = new StringBuffer();
						while (risksIt.hasNext()) {
							sb.append(risksIt.next().getNom());
							if (risksIt.hasNext()) {
								sb.append(";");
							}
						}
						return sb.toString();
					} else if (parent.getChampEntite().getNom()
							.matches("ConformeArrivee.Raison")) {
						
						return formatNonConformites(prel, parent.getChampEntite());
					} else {
						return ObjectTypesFormatters
						.formatObject(ManagerLocator.getChampManager()
						.getValueForObjectManager(parent, 
								prel, false));
					}
				}	
			} else if (chp.getChampAnnotation() != null) {
				return getChampAnnotationValeur(prel, 
					chp.getChampAnnotation());
			}
		}
		return "-";
	}
	
	private static String formatNonConformites(Object obj,
			ChampEntite champEntite) {
		String cNom = "";
		Pattern p = Pattern.compile("Conforme(.*)\\.Raison");
		Matcher m = p.matcher(champEntite.getNom());
		boolean b = m.matches();
		if (b && m.groupCount() > 0) {
			cNom = m.group(1);
		}
		Iterator<ObjetNonConforme> ncsIt = ManagerLocator
			.getObjetNonConformeManager()
			.findByObjetAndTypeManager(obj, 
				ManagerLocator.getConformiteTypeManager().
				findByEntiteAndTypeManager(cNom, 
						champEntite.getEntite()).get(0)).iterator();
		StringBuffer sb = new StringBuffer();
		while (ncsIt.hasNext()) {
			sb.append(ncsIt.next().getNonConformite().getNom());
			if (ncsIt.hasNext()) {
				sb.append(";");
			}
		}
		return sb.toString();
	}

	private static String 
		getStringValueFromEchantillon(EchantillonDTO echanDeco, 
									Champ chp, Champ parent) {
		if (echanDeco.getEchantillon() != null && chp != null) {	
			if (chp.getChampEntite() != null) {
				if (parent == null) {
					if (chp.getChampEntite().getNom().equals("EmplacementId")) {
						return echanDeco
								.getEmplacementAdrl();
					// since 2.0.13 temp stock
					} else if (chp.getChampEntite().getNom().equals("TempStock")) {
						return echanDeco.getTempStock();
					} else if (chp.getChampEntite().getNom()
							.equals("CodeOrganes")) {
						List<String> codes = ManagerLocator
						.getCodeAssigneManager()
						.formatCodesAsStringsManager(
								ManagerLocator.getCodeAssigneManager()
								.findCodesOrganeByEchantillonManager(
										echanDeco.getEchantillon()));
						StringBuffer sb = new StringBuffer();
						for (int k = 0; k < codes.size(); k++) {
							sb.append(codes.get(k));
							if (k + 1 < codes.size()) {
								sb.append(", ");
							}
						}
						return sb.toString();
					} else if (chp.getChampEntite().getNom()
							.equals("CodeMorphos")) {
						List<String> codes = ManagerLocator
						.getCodeAssigneManager()
						.formatCodesAsStringsManager(
								ManagerLocator.getCodeAssigneManager()
								.findCodesMorphoByEchantillonManager(
										echanDeco.getEchantillon()));
						StringBuffer sb = new StringBuffer();
						for (int k = 0; k < codes.size(); k++) {
							sb.append(codes.get(k));
							if (k + 1 < codes.size()) {
								sb.append(", ");
							}
						}
						return sb.toString();
					} else {
						return ObjectTypesFormatters
						.formatObject(ManagerLocator.getChampManager()
								.getValueForObjectManager(chp, 
							echanDeco.getEchantillon(), false));
					}
				} else {
//					if (parent.getChampEntite().getNom().equals("BanqueId")
//					&& echanDeco
//					.getEchantillon().getBanque() != null) {
//					return ObjectTypesFormatters
//						.formatObject(ManagerLocator.getChampManager()
//					.getValueForObjectManager(chp, 
//					echanDeco.getEchantillon().getBanque(), false));
//			}
					if (parent.getChampEntite().getNom().equals("PrelevementId")
							&& echanDeco.getEchantillon().getPrelevement() != null) {
						return ObjectTypesFormatters
								.formatObject(getStringValueFromPrelevement(echanDeco
								.getEchantillon().getPrelevement(), parent, null));
					} else if (parent.getChampEntite().getNom()
							.matches("Conforme.*Raison")) {
						return formatNonConformites(echanDeco.getEchantillon(), 
								parent.getChampEntite());
					} else {
						return ObjectTypesFormatters
								.formatObject(ManagerLocator.getChampManager()
										.getValueForObjectManager(parent, 
								echanDeco.getEchantillon(), false));
					}
				}	
			} else if (chp.getChampAnnotation() != null) {
				return getChampAnnotationValeur(echanDeco.getEchantillon(), 
					chp.getChampAnnotation());
			}
		}
		return "-";
	}

	private static String 
			getStringValueFromDerive(ProdDeriveDecorator2 prodDeco, 
											Champ chp, Champ parent) {
		if (prodDeco.getProdDerive() != null && chp != null) {	
			if (chp.getChampEntite() != null) {
				if (parent == null) {
					if (chp.getChampEntite().getNom().equals("EmplacementId")) {
						return prodDeco
								.getEmplacementAdrl();
						// since 2.0.13 temp stock
					} else if (chp.getChampEntite().getNom().equals("TempStock")) {
						return prodDeco.getTempStock();
					} else {
						return ObjectTypesFormatters
						.formatObject(ManagerLocator.getChampManager()
								.getValueForObjectManager(chp, 
							prodDeco.getProdDerive(), false));
					}
				} else {
					if (parent.getChampEntite().getNom()
						.matches("Conforme.*Raison")) {
						return formatNonConformites(prodDeco.getProdDerive(), 
							parent.getChampEntite());
					} 
	//				if (parent.getChampEntite().getNom().equals("BanqueId")
	//				&& echanDeco
	//				.getEchantillon().getBanque() != null) {
	//				return ObjectTypesFormatters
	//					.formatObject(ManagerLocator.getChampManager()
	//				.getValueForObjectManager(chp, 
	//				echanDeco.getEchantillon().getBanque(), false));
	//		}
	
					return ObjectTypesFormatters
					.formatObject(ManagerLocator.getChampManager()
					.getValueForObjectManager(parent, 
							prodDeco.getProdDerive(), false));
				
				}	
			} else if (chp.getChampAnnotation() != null) {
				return getChampAnnotationValeur(prodDeco.getProdDerive(), 
					chp.getChampAnnotation());
			}
		}
		return "-";
	}

		private static String 
			getStringValueFromCession(Cession cession, 
											Champ chp, Champ parent) {
		if (cession != null && chp != null) {	
			if (chp.getChampEntite() != null) {
				if (parent == null) {
					return ObjectTypesFormatters
						.formatObject(ManagerLocator.getChampManager()
								.getValueForObjectManager(chp, 
							cession, false));
					
				} else {
		//				if (parent.getChampEntite().getNom().equals("BanqueId")
		//				&& echanDeco
		//				.getEchantillon().getBanque() != null) {
		//				return ObjectTypesFormatters
		//					.formatObject(ManagerLocator.getChampManager()
		//				.getValueForObjectManager(chp, 
		//				echanDeco.getEchantillon().getBanque(), false));
		//		}
//					if (parent.getChampEntite().getNom()
//							.equals("ContratId")) {
//						if (recup.getContrat() != null) {
//							if (nomChampEntite
//									.equals("DateDemandeCession")) {
//								listeStrings.add(recup.getContrat()
//										.getDateDemandeCession());
//							} else if (nomChampEntite
//									.equals("DateDemandeRedaction")) {
//								listeStrings.add(recup.getContrat()
//										.getDateDemandeRedaction());
//							} else if (nomChampEntite
//									.equals("DateEnvoiContrat")) {
//								listeStrings.add(recup.getContrat()
//										.getDateEnvoiContrat());
//							} else if (nomChampEntite
//									.equals("DateSignature")) {
//								listeStrings.add(recup.getContrat()
//										.getDateSignature());
//							} else if (nomChampEntite
//									.equals("DateValidation")) {
//								listeStrings.add(recup.getContrat()
//										.getDateValidation());
//							} else if (nomChampEntite
//									.equals("Description")) {
//								listeStrings.add(recup.getContrat()
//										.getDescription());
//							} else if (nomChampEntite.equals("Numero")) {
//								listeStrings.add(recup.getContrat()
//										.getNumero());
//							} else if (nomChampEntite
//									.equals("TitreProjet")) {
//								listeStrings.add(recup.getContrat()
//										.getTitreProjet());
//							}
//		
					return ObjectTypesFormatters
					.formatObject(ManagerLocator.getChampManager()
					.getValueForObjectManager(parent, cession, false));
				
				}	
			} else if (chp.getChampAnnotation() != null) {
				return getChampAnnotationValeur(cession, chp.getChampAnnotation());
			}
		}
		return "-";
	}

	
	private static String getChampAnnotationValeur(TKAnnotableObject obj, 
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
	
	/**
	 * Construit un tableau EXCEL d'objets
	 * 
	 * @throws Exception
	 * @return structure html
	 */
	public final static Workbook exportTableExcel(Workbook wb, 
			String nomRecherche, List<List<Object>> matriceAffichable, 
											Affichage affichage) {

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

		Sheet sheet = wb.createSheet();
		
		CellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(HSSFDataFormat
						.getBuiltinFormat("m/d/yy h:mm"));
		
		CellStyle dateStyleShort = wb.createCellStyle();
		dateStyleShort.setDataFormat(HSSFDataFormat
						.getBuiltinFormat("m/d/yy"));

		short cptRow = 0;

		Row row0 = sheet.createRow(0);
		for (short i = 0; i < resultats.size(); i++) {
			Cell cell = row0.createCell(i);
			cell.setCellValue(resultats.get(i).getNomColonne().toString());
		}

		Iterator<List<Object>> itRow = matriceAffichable.iterator();
		while (itRow.hasNext()) {
			cptRow++;
			short cptColumn = 0;
			Row row = sheet.createRow(cptRow);

			List<Object> rowAffichable = itRow.next();
			Iterator<Object> itCell = rowAffichable.iterator();
			while (itCell.hasNext()) {
				Cell cell = row.createCell(cptColumn);
				Object oCell = itCell.next();
				if (oCell == null) {
					oCell = new String("-");
				}
				cell.setCellValue(oCell.toString());
				cptColumn++;
			}

		}
		
		return wb;
	}
}
