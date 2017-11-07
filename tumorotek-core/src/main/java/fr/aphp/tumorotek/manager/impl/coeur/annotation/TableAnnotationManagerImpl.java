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
package fr.aphp.tumorotek.manager.impl.coeur.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.CatalogueDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationBanqueDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.annotation.TableAnnotationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation
												.TableAnnotationValidator;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanque;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotationBanquePK;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Implémentation du manager du bean de domaine TableAnnotation.
 * Classe créée le 02/02/10.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class TableAnnotationManagerImpl implements TableAnnotationManager {
	
	private Log log = LogFactory.getLog(TableAnnotationManager.class);

	/* Beans injectes par Spring*/
	private TableAnnotationDao tableAnnotationDao;
	private TableAnnotationValidator tableAnnotationValidator;
	private OperationManager operationManager;
	private OperationTypeDao operationTypeDao;
	private TableAnnotationBanqueDao tableAnnotationBanqueDao;
	private AnnotationValeurManager annotationValeurManager;
	private ChampAnnotationDao champAnnotationDao;
	private ChampAnnotationManager champAnnotationManager;
	private EntiteDao entiteDao;
	private CatalogueDao catalogueDao;
	private PlateformeDao plateformeDao;


	public TableAnnotationManagerImpl() {
	}
	
	public void setTableAnnotationDao(TableAnnotationDao taDao) {
		this.tableAnnotationDao = taDao;
	}

	public void setTableAnnotationValidator(
			TableAnnotationValidator taValidator) {
		this.tableAnnotationValidator = taValidator;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	public void setOperationTypeDao(OperationTypeDao otDao) {
		this.operationTypeDao = otDao;
	}

	public void setTableAnnotationBanqueDao(
			TableAnnotationBanqueDao tabDao) {
		this.tableAnnotationBanqueDao = tabDao;
	}

	public void setAnnotationValeurManager(AnnotationValeurManager avManager) {
		this.annotationValeurManager = avManager;
	}

	public void setChampAnnotationManager(
			ChampAnnotationManager chpManager) {
		this.champAnnotationManager = chpManager;
	}

	public void setChampAnnotationDao(ChampAnnotationDao cDao) {
		this.champAnnotationDao = cDao;
	}

	public void setPlateformeDao(PlateformeDao pDao) {
		this.plateformeDao = pDao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	public void setCatalogueDao(CatalogueDao cDao) {
		this.catalogueDao = cDao;
	}

	@Override
	public TableAnnotation findByIdManager(Integer id) {
		return tableAnnotationDao.findById(id);
	}
	
	@Override
	public void createOrUpdateObjectManager(TableAnnotation table,
										Entite entite, Catalogue catalogue,
										List<ChampAnnotation> champs,
										List<Banque> banques,  Banque current, 
										Utilisateur utilisateur, 
										String operation, 
										String baseDir, 
										Plateforme pf) {
		
		if (operation == null) {
			throw new NullPointerException("operation cannot be "
									+ "set to null for createorUpdateMethod");
		}
		
		//Validation
		checkRequiredObjectsAndValidate(table, entite, operation);
		
		// merge non required
		if (catalogue != null) {
			table.setCatalogue(catalogueDao.mergeObject(catalogue));
		}
		if (pf != null) {
			table.setPlateforme(plateformeDao.mergeObject(pf));
		}

		//Doublon
		if (!findDoublonManager(table)) {
			if ((operation.equals("creation") 
									|| operation.equals("modification"))) {
				try {
					if (operation.equals("creation")) {
						tableAnnotationDao.createObject(table);
						log.info("Enregistrement objet TableAnnotation " 
															+ table.toString());
						CreateOrUpdateUtilities
							.createAssociateOperation(table, operationManager, 
								operationTypeDao.findByNom("Creation").get(0),
																utilisateur);
					} else {
						tableAnnotationDao.updateObject(table);
						log.info("Modification objet TableAnnotation "
															+ table.toString());
						CreateOrUpdateUtilities
							.createAssociateOperation(table, operationManager, 
								operationTypeDao.findByNom("Modification")
													.get(0),   utilisateur);
					}
					// ajout association vers banques
					if (banques != null) {
						updateBanques(table, banques, baseDir);
					}
					// ajout/update association vers champs
					if (champs != null) {
						createOrUpdateChampsManager(table, champs, 
											utilisateur, current, baseDir);
					}
				} catch (RuntimeException re) {
					// en cas d'erreur lors enregistrement d'un champ lors de
					// la creation de la table
					// le rollback se fera mais la table aura un id assigne
					// qui déclenchera une TransientException si on essaie 
					// d'enregistrer a nouveau.
					if (table.getTableAnnotationId() != null 
										&& operation.equals("creation")) {
						List<TableAnnotationBanque> tabs = 
							new ArrayList<TableAnnotationBanque>(
									table.getTableAnnotationBanques());
						for (int i = 0; i < tabs.size(); i++) {
							tableAnnotationBanqueDao
									.removeObject(tabs.get(i).getPk());
						}
						table.getTableAnnotationBanques().clear();
						table.setTableAnnotationId(null);
					} 
					
					throw(re);
				}
			} else {
				throw new IllegalArgumentException("Operation must match "
						+ "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet TableAnnotation "
					+ table.toString());
			throw new DoublonFoundException("TableAnnotation", operation);
		}			
	}

	@Override
	public List<TableAnnotation> findAllObjectsManager() {
		log.debug("Recherche totalite des TableAnnotation");
		return tableAnnotationDao.findAll();
	}

	@Override
	public List<TableAnnotation> findByEntiteAndBanqueManager(Entite entite,
																Banque bank) {
		log.debug("Recherche des TableAnnotation par Entite et Banque");
		return tableAnnotationDao.findByEntiteAndBanque(entite, bank);
	}
	
	@Override
	public List<TableAnnotation> findByEntiteBanqueAndCatalogueManager(
			Entite entite, Banque bank, String catalogue) {
		log.debug("Recherche des TableAnnotation par Entite, Banque et " 
				+ "catalogue");
		return tableAnnotationDao.findByEntiteBanqueAndCatalogue(
				entite, bank, catalogue);
	}

	

	@Override
	public List<TableAnnotation> findByNomLikeManager(String nom,
			boolean exactMatch) {
		if (!exactMatch) {
			nom = nom + "%";
		}
		log.debug("Recherche TableAnnotation par nom: " 
				+ nom + " exactMatch " + String.valueOf(exactMatch));
		return tableAnnotationDao.findByNom(nom);
	}

	@Override
	public boolean findDoublonManager(TableAnnotation table) {
		if (table.getTableAnnotationId() == null) {
			return tableAnnotationDao.findAll().contains(table);
		} else {
			return tableAnnotationDao.findByExcludedId(
				table.getTableAnnotationId()).contains(table);
		}	
	}

	@Override
	public Set<Banque> getBanquesManager(TableAnnotation tab) {
		Set<Banque> banques = new HashSet<Banque>();
		if (tab.getTableAnnotationId() != null) {
			TableAnnotation table = 
							tableAnnotationDao.mergeObject(tab);
			Set<TableAnnotationBanque> tabs = table
										.getTableAnnotationBanques();
			Iterator<TableAnnotationBanque> it = tabs.iterator();
			while (it.hasNext()) {
				TableAnnotationBanque taB = it.next();
				banques.add(taB.getBanque());
			}
		}
		return banques;
	}
	
	@Override
	public Set<TableAnnotationBanque> getTableAnnotationBanquesManager(
			TableAnnotation tab) {
		Set<TableAnnotationBanque> tabs = new HashSet<TableAnnotationBanque>();
		if (tab.getTableAnnotationId() != null) {
			TableAnnotation table = tableAnnotationDao.mergeObject(tab);
			tabs = table.getTableAnnotationBanques();
			tabs.isEmpty(); // operation empechant LazyInitialisationException
		}
		return tabs;
	}
	
	@Override
	public Set<ChampAnnotation> getChampAnnotationsManager(
												TableAnnotation tab) {
		Set<ChampAnnotation> champs = new HashSet<ChampAnnotation>();
		if (tab.getTableAnnotationId() != null) {
			TableAnnotation table = tableAnnotationDao.mergeObject(tab);
			champs = table.getChampAnnotations();
			champs.isEmpty(); // operation empechant LazyInitialisationException
		}
		return champs;
	}

	@Override
	public void removeObjectManager(TableAnnotation table, String comments, 
											Utilisateur usr, String baseDir) {
		if (table != null) {
			Iterator<ChampAnnotation> it 
								= getChampAnnotationsManager(table).iterator();
			while (it.hasNext()) {
				champAnnotationManager.removeObjectManager(it.next(), 
														comments, usr, baseDir);
			}
			table.setChampAnnotations(new HashSet<ChampAnnotation>());
			
			tableAnnotationDao.removeObject(table.getTableAnnotationId());
			log.info("Suppression objet TableAnnotation " + table.toString());
			//Supprime operations associes
			CreateOrUpdateUtilities.
					removeAssociateOperations(table, operationManager, 
															comments, usr);
		} else {
			log.warn("Suppression d'un TableAnnotation null");
		}	
	}

//	@Override
//	public void moveTableOrderUpDownManager(TableAnnotation table, 
//												Banque banque, boolean up) {
//
//		Set<TableAnnotationBanque> tabs = table.getTableAnnotationBanques();
//		
//		TableAnnotationBanque tabPrev = null;
//		TableAnnotationBanque tab = null;
//		TableAnnotationBanque tabNext = null;
//		
//		List<TableAnnotationBanque> tabsList = 
//					new ArrayList<TableAnnotationBanque>(tabs);
//				
//		int i = 0;
//		while (i < tabsList.size()) {
//			tabPrev = tab;
//			tab = tabsList.get(i);
//			if (tab.getPk()
//					.equals(new TableAnnotationBanquePK(banque, table))) {
//				if (i + 1 < tabsList.size()) {
//					tabNext = tabsList.get(i + 1);
//				}
//				break;
//			}
//			i++;
//		}
//		
//		// TableAnnotationBanque correspondante
//		int newOrdre;
//		if (up) {
//			if (tabPrev != null) {
//				
//				newOrdre = tabPrev.getOrdre();
//				
//				// assigne l'ordre a la table n-1
//				tabPrev.setOrdre(tab.getOrdre());
//				
//				// assigne le newOrdre a la table n
//				tab.setOrdre(newOrdre);
//			}
//		} else {
//			if (tabNext != null) {
//				
//				newOrdre = tabNext.getOrdre();
//				
//				// assigne l'ordre a la table n-1
//				tabNext.setOrdre(tab.getOrdre());
//				
//				// assigne le newOrdre a la table n
//				tab.setOrdre(newOrdre);
//			}
//		}
//		tabs = new HashSet<TableAnnotationBanque>();
//		tabs.addAll(tabsList);
//		table.setTableAnnotationBanques(tabs);
//	}
	
	/**
	 * Cette méthode met à jour les associations entre une table et
	 * une liste de TableAnnotationBanque.
	 * @param table 
	 * @param liste de banques
	 * @param base Directory utilisé pour mettre à jour les associations 
	 * entre les chps de type fichier de la table et le file system.
	 */
	private void updateBanques(TableAnnotation tableAnno, 
									List<Banque> banques, String baseDir) {
		
		TableAnnotation table = tableAnnotationDao.mergeObject(tableAnno);
		
		Iterator<TableAnnotationBanque> it = 
								table.getTableAnnotationBanques().iterator();
		
		List<TableAnnotationBanque> banksToRemove 
									= new ArrayList<TableAnnotationBanque>();
		// on parcourt les banques qui sont actuellement associées
		// a la table
		while (it.hasNext()) {
			TableAnnotationBanque tmp = it.next();
			// si une banque n'est pas dans la nouvelle liste, on
			// le conserve afin de le retirer par la suite
			if (!banques.contains(tmp.getBanque())) {
				banksToRemove.add(tmp);
			}
		}
		
		// on parcourt la liste la liste des banques à retirer de
		// l'association
		for (int i = 0; i < banksToRemove.size(); i++) {
			TableAnnotationBanque tab = tableAnnotationBanqueDao
											.mergeObject(banksToRemove.get(i));
			// on retire la TAB de l'association et on la supprime
			table.getTableAnnotationBanques().remove(tab);
			tableAnnotationBanqueDao.removeObject(tab.getPk());
			
			log.debug("Suppression de l'association entre la table : " 
					+ table.toString() + " et suppression de la relation avec"
					+ " la banque: " + tab.getBanque().toString());
			
			removeAnnotationValeursForTableAndBanque(tab, baseDir);
		}
		
		List<Banque> newOnes = new ArrayList<Banque>();
		// on parcourt la nouvelle liste de banques
		for (int i = 0; i < banques.size(); i++) {
			TableAnnotationBanque newtab = new TableAnnotationBanque();
			TableAnnotationBanquePK pk = 
				new TableAnnotationBanquePK(banques.get(i), table);
			newtab.setPk(pk);
			newtab.setOrdre(1);
			// si une banque n'était pas associé à la table
			if (!table.getTableAnnotationBanques().contains(newtab)) {
				// on ajoute la banque dans l'association dans le bon ordre
				table.getTableAnnotationBanques()
					.add(tableAnnotationBanqueDao.mergeObject(newtab));
				
				log.debug("Ajout de l'association entre la table : " 
						+ table.toString() + " et la banque : "
						+ banques.get(i).toString());
				
				newOnes.add(banques.get(i));
				
			} else { // on modifie l'ordre de la table present avec la liste
				newtab = tableAnnotationBanqueDao.findById(pk);
				// newtab.setOrdre(i + 1);
				tableAnnotationBanqueDao.mergeObject(newtab);
			}
		}
		// creation d'un dossier si table contient chp fichier
		// pour les tables nouvellement associées
		Iterator <ChampAnnotation> chpIt = champAnnotationManager
				.findChampsFichiersByTableManager(table).iterator();
		while (chpIt.hasNext()) {
			champAnnotationManager
				.createOrDeleteFileDirectoryManager(baseDir, 
									chpIt.next(), false, newOnes);
		}
	}
	
	/**
	 * Supprime en cascade toutes les valeurs d'annotations lors de la 
	 * suppression de l'assignation d'une table vers une banque.
	 * @param tab TableAnnotationBanque
	 * @param base Directory
	 */
	private void removeAnnotationValeursForTableAndBanque(
							TableAnnotationBanque tab, String baseDir) {
		
		List<AnnotationValeur> valeurs = 
			annotationValeurManager
				.findByTableAndBanqueManager(tab.getTableAnnotation(), 
														tab.getBanque());
		
		List<File> filesToDelete = new ArrayList<File>();
		
		for (int i = 0; i < valeurs.size(); i++) {
			annotationValeurManager.removeObjectManager(valeurs.get(i), filesToDelete);
		}
		log.info("Suppression des valeurs d'annotation pour l'association"
						+ " table "	+ tab.getTableAnnotation() + " et banque " 
						+ tab.getBanque());		
		
		// suppression du dossier
		List<ChampAnnotation> chpFiles = champAnnotationManager
				.findChampsFichiersByTableManager(tab.getTableAnnotation());
		List<Banque> bank = new ArrayList<Banque>();
		bank.add(tab.getBanque());
		for (int i = 0; i < chpFiles.size(); i++) {
			champAnnotationManager
				.createOrDeleteFileDirectoryManager(baseDir, chpFiles.get(i), 
																true, bank);
		}
	}
	
	/**
	 * Verifie que les Objets devant etre obligatoirement associes 
	 * sont non nulls et lance la validation via le Validator.
	 * @param table
	 * @param entite
	 * @param operation demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(TableAnnotation table, 
											Entite entite, String operation) {
		//Entite required
		if (entite != null) { 
			// merge entite object
			table.setEntite(entiteDao.mergeObject(entite));
		} else if (table.getEntite() == null) {
			log.warn("Objet obligatoire Entite manquant"
					+ " lors de la " + operation + " de table annotation");
			throw new RequiredObjectIsNullException(
					"TableAnnotation", operation, "Entite");
		} 	

		//Validation
		BeanValidator.validateObject(table, 
								new Validator[]{tableAnnotationValidator});
	}
	
	@Override
	public void createOrUpdateChampsManager(TableAnnotation tab,
											List<ChampAnnotation> champs, 
														Utilisateur usr, 
														Banque current,
														String baseDir) {
		
		TableAnnotation table = tableAnnotationDao.mergeObject(tab);
		
		// realise une copie de la liste de champs pour rollback
		List<ChampAnnotation> copies = new ArrayList<ChampAnnotation>();
		for (int i = 0; i < champs.size(); i++) {
			copies.add(champs.get(i).clone());
		}
		
		try {
			String operation = "creation";
			// on parcourt la liste de champs
			for (int i = 0; i < champs.size(); i++) {
				operation = "creation";
				List<Item> its = null;
				List<AnnotationDefaut> defs = null;
				// si un champ en modification
				if (champs.get(i).getChampAnnotationId() != null) {
					operation = "modification";
				} 
				its = null;
				// sinon LazyInitialisationException
				if (!champs.get(i).getItems().getClass()
								.getSimpleName().equals("PersistentSet")) {
					its = new ArrayList<Item>();
					its.addAll(champs.get(i).getItems());
					champs.get(i).setItems(new HashSet<Item>());
				}
				
				
				defs = null;			
				// sinon LazyInitialisationException
				if (!champs.get(i).getAnnotationDefauts().getClass()
						.getSimpleName().equals("PersistentSet")) {
					defs = new ArrayList<AnnotationDefaut>();
					defs.addAll(champs.get(i).getAnnotationDefauts());
					champs.get(i)
						.setAnnotationDefauts(new HashSet<AnnotationDefaut>());
				}
				

				champAnnotationManager
					.createOrUpdateObjectManager(champs.get(i), table, null, 
								its, defs, usr, current, operation, baseDir);
				
				if (operation.equals("creation")) {
					table.getChampAnnotations().add(champs.get(i));
				}
			}
		} catch (RuntimeException re) {
			// en cas d'erreur lors creation d'un champ, recupere 
			// les ids enregistre avant creation/modification pour 
			// retrouver etat initial au niveau des ids
			// et des valeurs defauts?
			for (int i = 0; i < champs.size(); i++) {
				if (copies.get(i).getChampAnnotationId() == null) {
					table.getChampAnnotations().remove(champs.get(i));
				}
				champs.get(i)
					.setChampAnnotationId(copies.get(i).getChampAnnotationId());
				champs.get(i)
					.setTableAnnotation(copies.get(i).getTableAnnotation());
				champs.get(i).setItems(copies.get(i).getItems());
				champs.get(i)
					.setAnnotationDefauts(copies.get(i).getAnnotationDefauts());
			}
			throw(re);
		}
	}
	
	@Override
	public void updateChampOrdersManager(List<ChampAnnotation> chps) {
		Iterator<ChampAnnotation> it = chps.iterator();
		ChampAnnotation chp = null;
		int i = 0;
		while (it.hasNext()) {
			i++;
			chp = it.next();
			chp.setOrdre(i);
			champAnnotationDao.mergeObject(chp);
		}
	}

	@Override
	public List<TableAnnotation> 
		findByBanquesManager(List<Banque> banks, boolean catalogue) {
		List<TableAnnotation> tabs = new ArrayList<TableAnnotation>();
		if (banks != null && !banks.isEmpty()) {
			tabs = tableAnnotationDao.findByBanques(banks);
			if (!catalogue) {
				// retire les tables catalogues
				ListIterator<TableAnnotation> it = tabs.listIterator();
				while (it.hasNext()) {
					if (it.next().getCatalogue() != null) {
						it.remove();
					}
				}
			}
		}
		return tabs;
	}

	@Override
	public List<TableAnnotation> findByCataloguesManager(List<Catalogue> 
																catas) {
		List<TableAnnotation> tabs = new ArrayList<TableAnnotation>();
		if (catas != null && !catas.isEmpty()) {
			tabs = tableAnnotationDao.findByCatalogues(catas);
		}	
		return tabs;
	}

	@Override
	public List<TableAnnotation> findByCatalogueAndChpEditManager(
														Catalogue cat) {
		return tableAnnotationDao.findByCatalogueAndChpEdit(cat);
	}

	@Override
	public List<TableAnnotation> findByEntiteAndPlateformeManager(
										Entite entite, Plateforme pf) {
		return tableAnnotationDao.findByEntiteAndPlateforme(entite, pf);
	}

	@Override
	public List<TableAnnotation> findByPlateformeManager(Plateforme pf) {
		return tableAnnotationDao.findByPlateforme(pf);
	}

}
