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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.AnnotationDefautDao;
import fr.aphp.tumorotek.dao.annotation.AnnotationValeurDao;
import fr.aphp.tumorotek.dao.annotation.ChampAnnotationDao;
import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.annotation.ItemDao;
import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur
								.annotation.AnnotationCommonValidator;
import fr.aphp.tumorotek.manager.validation.coeur
								.annotation.ChampAnnotationValidator;
import fr.aphp.tumorotek.manager.validation.coeur.annotation.ItemValidator;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationDefaut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * Implémentation du manager du bean de domaine ChampAnnotation.
 * Classe créée le 02/02/10.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ChampAnnotationManagerImpl implements ChampAnnotationManager {

	private Log log = LogFactory.getLog(ChampAnnotationManagerImpl.class);

	/** Bean Dao AffichageDao. */
	private ChampAnnotationDao champAnnotationDao;
	private ChampAnnotationValidator champAnnotationValidator;
	private DataTypeDao dataTypeDao;
	private OperationManager operationManager;
	private OperationTypeDao operationTypeDao;
	private ItemDao itemDao;
	private AnnotationDefautDao annotationDefautDao;
	private AnnotationValeurDao annotationValeurDao;
	private ItemValidator itemValidator;
	private AnnotationCommonValidator annotationCommonValidator;
	private TableAnnotationDao tableAnnotationDao;
	private BanqueDao banqueDao;

	/** Bean Dao EntityManagerFactory. */
	private EntityManagerFactory entityManagerFactory;


	public ChampAnnotationManagerImpl() {
		super();
	}

	/**
	 * Setter du bean ChampAnnotationDao.
	 * @param caDao est le bean Dao.
	 */
	public void setChampAnnotationDao(ChampAnnotationDao caDao) {
		this.champAnnotationDao = caDao;
	}

	public void setChampAnnotationValidator(
			ChampAnnotationValidator cValidator) {
		this.champAnnotationValidator = cValidator;
	}

	public void setDataTypeDao(DataTypeDao dtDao) {
		this.dataTypeDao = dtDao;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	public void setOperationTypeDao(OperationTypeDao otDao) {
		this.operationTypeDao = otDao;
	}

	public void setItemDao(ItemDao itDao) {
		this.itemDao = itDao;
	}

	public void setAnnotationDefautDao(AnnotationDefautDao annoDDao) {
		this.annotationDefautDao = annoDDao;
	}

	public void setItemValidator(ItemValidator iValidator) {
		this.itemValidator = iValidator;
	}

	public void setAnnotationCommonValidator(
			AnnotationCommonValidator acValidator) {
		this.annotationCommonValidator = acValidator;
	}

	public void setTableAnnotationDao(TableAnnotationDao tDao) {
		this.tableAnnotationDao = tDao;
	}

	/**
	 * Setter du bean EntityManagerFactory.
	 * @param eFactory est l'entityManagerFactory.
	 */
	public void setEntityManagerFactory(EntityManagerFactory eFactory) {
		this.entityManagerFactory = eFactory;
	}

	public void setAnnotationValeurDao(AnnotationValeurDao avDao) {
		this.annotationValeurDao = avDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	@Override
	public List<ChampAnnotation> findByNomManager(String nom) {
		return champAnnotationDao.findByNom(nom);		
	}
	
	@Override
	public ChampAnnotation findByIdManager(Integer champAnnotationId) {
		return champAnnotationDao.findById(champAnnotationId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnnotationValeur> findAnnotationValeurByChampAnnotationManager(
			ChampAnnotation ca) {
		List<AnnotationValeur> objets = null;
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT va FROM AnnotationValeur as av "
				+ "join av.champAnnotation as ca where ca.champAnnotationId = "
				+ ca.getChampAnnotationId());
		/* On exécute la requête. */
		log.debug("findAnnotationValeurByChampAnnotationManager : "
				+ "Exécution de la requête : \n"
				+ sb.toString());
		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createQuery(sb.toString());
		objets = (List<AnnotationValeur>) query.getResultList();		
		return objets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChampAnnotation> findByEntiteManager(Entite e) {
		List<ChampAnnotation> objets = null;
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT DISTINCT ca FROM ChampAnnotation as ca "
				+ "join ca.tableAnnotation.entite as en "
				+ "WHERE en.entiteId = " + e.getEntiteId().intValue());
		/* On exécute la requête. */
		log.debug("findChampAnnotationByEntiteManager : "
				+ "Exécution de la requête : \n"
				+ sb.toString());
		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createQuery(sb.toString());
		objets = (List<ChampAnnotation>) query.getResultList();
		return objets;
	}

	@Override
	public String findAnnotationValeurManager(Object objet,
			ChampAnnotation champAnnotation) {
		Object retour = null;
		/* On récupère l'identifiant de l'objet. */
		Integer id = null;
		if (objet.getClass().getSimpleName().equals("Patient")) {
			id = ((Patient) objet).getPatientId();
		} else if (objet.getClass().getSimpleName().equals("Maladie")) {
			id = ((Maladie) objet).getMaladieId();
		} else if (objet.getClass().getSimpleName().equals("Prelevement")) {
			id = ((Prelevement) objet).getPrelevementId();
		} else if (objet.getClass().getSimpleName().equals("Echantillon")) {
			id = ((Echantillon) objet).getEchantillonId();
		} else if (objet.getClass().getSimpleName().equals("ProdDerive")) {
			id = ((ProdDerive) objet).getProdDeriveId();
		}


		/* On récupère le type de l'annotation. */
		String dataType = champAnnotation.getDataType().getType();
		String type = null;
		if (dataType.equals("num") || dataType.equals("alphanum")) {
			type = "alphanum";
		} else if (dataType.equals("boolean")) {
			type = "bool";
		} else if (dataType.equals("date")) {
			type = "date";
		} else if (dataType.equals("datetime")) {
			type = "date";
		} else if (dataType.equals("texte")) {
			type = "texte";
		} else if (dataType.equals("thesaurus")) {
			type = "item.valeur";
		}

		if (type != null && id != null) {
			StringBuffer sb = new StringBuffer("");
			sb.append("SELECT av." + type + " FROM AnnotationValeur as av "
					+ "join av.champAnnotation as ca "
					+ "join ca.tableAnnotation.entite as en "
					+ "WHERE en.nom like '"	+ objet.getClass().getSimpleName()
					+ "' and av.objetId = " + id
					+ " and ca.champAnnotationId = "
					+ champAnnotation.getChampAnnotationId());
			/* On exécute la requête. */
			log.debug("findAnnotationValeurManager : "
					+ "Exécution de la requête : \n" + sb.toString());
			EntityManager em = entityManagerFactory.createEntityManager();
			Query query = em.createQuery(sb.toString());
			try {
				retour = query.getSingleResult();
			} catch (NoResultException e) {
				retour = null;
			}
		}
		if (retour != null) {
			return retour.toString();
		} else {
			return null;
		}
	}

	@Override
	public List<ChampAnnotation> findAllObjectsManager() {
		log.debug("Recherche totalite des ChampAnnotation");
		return champAnnotationDao.findAll();
	}

	@Override
	public List<ChampAnnotation> findByNomLikeManager(String nom,
			boolean exactMatch) {
		if (!exactMatch) {
			nom = nom + "%";
		}
		log.debug("Recherche ChampAnnotation par nom: " 
				+ nom + " exactMatch " + String.valueOf(exactMatch));
		return champAnnotationDao.findByNom(nom);
	}

	@Override
	public List<ChampAnnotation> findByTableManager(TableAnnotation table) {
		log.debug("Recherche des ChampAnnotation par Table");
		return champAnnotationDao.findByTable(table);
	}

	@Override
	public boolean findDoublonManager(ChampAnnotation champ) {
		if (champ.getChampAnnotationId() == null) {
			return champAnnotationDao.findAll().contains(champ);
		} else {
			return champAnnotationDao.findByExcludedId(
					champ.getChampAnnotationId()).contains(champ);
		}	
	}

	@Override
	public Set<AnnotationDefaut> getAnnotationDefautsManager(
			ChampAnnotation chp) {
		Set<AnnotationDefaut> defauts = new HashSet<AnnotationDefaut>();
		if (chp.getChampAnnotationId() != null) {
			ChampAnnotation champ = champAnnotationDao.mergeObject(chp);
			defauts = champ.getAnnotationDefauts();
			// operation empechant LazyInitialisationException
			defauts.isEmpty(); 
		}
		return defauts;
	}

	@Override
	public Set<AnnotationValeur> getAnnotationValeursManager(
			ChampAnnotation chp) {
		Set<AnnotationValeur> valeurs = new HashSet<AnnotationValeur>();
		if (chp.getChampAnnotationId() != null) {
			ChampAnnotation champ = champAnnotationDao.mergeObject(chp);
			valeurs = champ.getAnnotationValeurs();
			// operation empechant LazyInitialisationException
			valeurs.isEmpty(); 
		}
		return valeurs;
	}

	@Override
	public Set<Item> getItemsManager(ChampAnnotation chp, Banque bank) {
		Set<Item> items = new HashSet<Item>();
		if (chp.getChampAnnotationId() != null) {
			ChampAnnotation champ = champAnnotationDao.mergeObject(chp);
			
			if (champ.getTableAnnotation().getCatalogue() == null) {
				items = champ.getItems();
				// operation empechant LazyInitialisationException
				items.isEmpty(); 
			} else  if (bank != null) {
				items = new LinkedHashSet<Item>(itemDao
					.findByChampAndPlateforme(champ, bank.getPlateforme()));
			}
		}
		return items;
	}

	@Override
	public Integer findMaxItemLength(Set<Item> items) {
		Iterator<Item> it = items.iterator();
		Integer max = 0;
		Integer current = 0;
		while (it.hasNext()) {
			current = it.next().getLabel().length();
			if (current > max) {
				max = current;
			}
		}
		return max;
	}

	@Override
	public void moveChampOrderUpDownManager(ChampAnnotation champ, boolean up) {
//		Set<ChampAnnotation> chps = tableAnnotationManager
//					.getChampAnnotationsManager(champ.getTableAnnotation());
		Set<ChampAnnotation> chps = getAllChampsFromTableManager(champ);

		ChampAnnotation chpPrev = null;
		ChampAnnotation chp = null;
		ChampAnnotation chpNext = null;

		List<ChampAnnotation> chpsList = 
			new ArrayList<ChampAnnotation>(chps);

		int i;
		for (i = 0; i < chpsList.size(); i++) {
			chpPrev = chp;
			chp = chpsList.get(i);
			if (chp.equals(champ)) {
				if (i + 1 < chpsList.size()) {
					chpNext = chpsList.get(i + 1);
				}
				break;
			}
		}

		int newOrdre;
		if (up) {
			if (chpPrev != null) {

				newOrdre = chpPrev.getOrdre();

				// assigne l'ordre au champ n-1
				chpPrev.setOrdre(chp.getOrdre());

				// assigne le newOrdre au champe n
				chp.setOrdre(newOrdre);
			}
		} else {
			if (chpNext != null) {

				newOrdre = chpNext.getOrdre();

				// assigne l'ordre a la table n-1
				chpNext.setOrdre(chp.getOrdre());

				// assigne le newOrdre a la table n
				chp.setOrdre(newOrdre);
			}
		}
		champAnnotationDao.mergeObject(chp);
		champAnnotationDao.mergeObject(chpPrev);
		champAnnotationDao.mergeObject(chpNext);
	}


	@Override
	public void removeObjectManager(ChampAnnotation champ, 
						String comments, Utilisateur user, String baseDir) {
		if (champ != null) {
			// supprime le dossier si annotation fichier
			if (champ.getDataType().getType().equals("fichier")) {
				createOrDeleteFileDirectoryManager(baseDir, champ, true, 
											getBanquesFromTableManager(champ));
			}

			champAnnotationDao.removeObject(champ.getChampAnnotationId());
			log.info("Suppression objet ChampAnnotation " + champ.toString());
			
			//Supprime operations associes
			CreateOrUpdateUtilities.
			removeAssociateOperations(champ, operationManager, 
												comments, user);

		} else {
			log.warn("Suppression d'un ChampAnnotation null");
		}	
	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes 
	 * sont non nulls et lance la validation via le Validator.
	 * @param champ
	 * @param table
	 * @param dataType
	 * @param items
	 * @param defauts
	 * @param operation demandant la verification
	 */
	private void checkRequiredObjectsAndValidate(ChampAnnotation champ, 
			TableAnnotation table,
			DataType dataType, 
			List<Item> items,
			List<AnnotationDefaut> defauts,
			String operation) {
		
		//table required
		if (table != null) { 
			// merge dataType object
			champ.setTableAnnotation(tableAnnotationDao.mergeObject(table));
		} else if (champ.getTableAnnotation() == null) {
			log.warn("Objet obligatoire TableAnnotation manquant"
					+ " lors de la " + operation + " de champ");
			throw new RequiredObjectIsNullException(
					"ChampAnnotation", operation, "TableAnnotation");
		} 	
		
		//DataType required
		if (dataType != null) { 
			// merge dataType object
			champ.setDataType(dataTypeDao.mergeObject(dataType));
		} else if (champ.getDataType() == null) {
			log.warn("Objet obligatoire DataType manquant"
					+ " lors de la " + operation + " de champ");
			throw new RequiredObjectIsNullException(
					"ChampAnnotation", operation, "DataType");
		} 	

		//Validation
		BeanValidator.validateObject(champ, 
				new Validator[]{champAnnotationValidator});

		// on parcourt une premiere fois la liste d'items et de defauts pour 
		// appliquer la validation
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {	
				BeanValidator.validateObject(items.get(i), 
						new Validator[]{itemValidator});
			}
		}

		if (defauts != null) {
			for (int i = 0; i < defauts.size(); i++) {	
				BeanValidator.validateObject(defauts.get(i), 
						new Validator[]{annotationCommonValidator});
			}
		}	
	}

	/**
	 * Cette méthode met à jour les associations entre un champ et
	 * une liste de Item.
	 * @param champ 
	 * @param liste d'items
	 * @param banque courante
	 */
	private void updateItems(ChampAnnotation champAnno, List<Item> items, 
															Banque bank) {

		ChampAnnotation champ = champAnnotationDao.mergeObject(champAnno);

		Set<Item> chpIts = getItemsManager(champAnno, bank);
		
		Iterator<Item> it  = chpIts.iterator();

		List<Item> itemsToRemove = new ArrayList<Item>();
		// on parcourt les items qui sont actuellement associées
		// au champ
		while (it.hasNext()) {
			Item tmp = it.next();
			// si un item n'est pas dans la nouvelle liste, on
			// le conserve afin de le retirer par la suite
			if (!items.contains(tmp)) {
				itemsToRemove.add(tmp);
			}
		}

		// on parcourt la liste la liste des items à retirer de
		// l'association
		for (int i = 0; i < itemsToRemove.size(); i++) {
			Item item = itemDao.mergeObject(itemsToRemove.get(i));
			// on retire l'item de l'association et on le supprime
			champ.getItems().remove(item);
			itemDao.removeObject(item.getItemId());

			log.debug("Suppression de l'association entre le champ : " 
					+ champ.toString() + " et l'item " + item.toString());
		}
		
		Set<Item> its = new LinkedHashSet<Item>();

		for (int i = 0; i < items.size(); i++) {			
			// si un item n'était pas associé au champ
			if (!chpIts.contains(items.get(i))) {
				log.debug("Ajout de l'association entre le champ : " 
						+ champ.toString() + " et l'item : "
						+ items.get(i).toString());
			} else {
				log.debug("Merge/Update de l'item " 
						+ items.get(i).toString() + " pour le champ " 
						+ champ.toString());
			}
			// on ajoute l'item dans l'association
			its.add(itemDao.mergeObject(items.get(i)));
		}
		champAnno.setItems(its);
	}

	/**
	 * Cette méthode met à jour les associations entre un champ et
	 * une liste de valeurs par défaut.
	 * @param champ 
	 * @param liste d'AnnotationDefaut
	 */
	private void updateDefauts(ChampAnnotation champAnno, 
			List<AnnotationDefaut> defauts) {

		ChampAnnotation champ = champAnnotationDao.mergeObject(champAnno);

		Iterator<AnnotationDefaut> it 
					= getAnnotationDefautsManager(champAnno).iterator();

		List<AnnotationDefaut> defautsToRemove 
									= new ArrayList<AnnotationDefaut>();
		// on parcoure les défauts qui sont actuellement associées
		// au champ
		while (it.hasNext()) {
			AnnotationDefaut tmp = it.next();
			// si un defaut n'est pas dans la nouvelle liste, on
			// le conserve afin de le retirer par la suite
			if (!defauts.contains(tmp)) {
				defautsToRemove.add(tmp);
			}
		}

		// on parcourt la liste la liste des defauts à retirer de
		// l'association
		for (int i = 0; i < defautsToRemove.size(); i++) {
			// AnnotationDefaut defaut = annotationDefautDao
			//							.mergeObject(defautsToRemove.get(i));
			// on retire l'item de l'association et on le supprime
			champ.getAnnotationDefauts().remove(defautsToRemove.get(i));
			annotationDefautDao.removeObject(defautsToRemove.get(i)
					.getAnnotationDefautId());

			log.debug("Suppression de l'association entre le champ : " 
					+ champ.toString() + " et la valeur par defaut " 
					+ defautsToRemove.get(i).toString());
		}
		
		Set<AnnotationDefaut> defs = new LinkedHashSet<AnnotationDefaut>();

		// on parcoure la nouvelle liste de valeurs par defaut
		for (int i = 0; i < defauts.size(); i++) {
			// si un defaut n'était pas associé au champ
			if (!champ.getAnnotationDefauts().contains(defauts.get(i))) {
				// on ajoute le defaut dans l'association sauf si elle porte
				// sur un item car deja cascadé
				if (defauts.get(i).getItem() != null) { 
					Iterator<Item> itor = champ.getItems().iterator();
					while (itor.hasNext()) {
						Item item = itor.next();
						if (item.equals(defauts.get(i).getItem())) {
							defauts.get(i).setItem(item);
						}
					}
				}
				log.debug("Ajout de l'association entre le champ : " 
						+ champ.toString() + " et la valeur par defaut : "
						+ defauts.get(i).toString());
			} else {
				log.debug("Modification de la valeur par defaut : "
						+ defauts.get(i).toString());
			}
			defs.add(annotationDefautDao.mergeObject(defauts.get(i)));
		}
		champAnno.setAnnotationDefauts(defs);
	}
	
	@Override
	public void createOrDeleteFileDirectoryManager(String baseDir, 
									ChampAnnotation chp, boolean delete,
									List<Banque> banques) {
		if (chp.getChampAnnotationId() != null) {
			//ChampAnnotation chp = champAnnotationDao.mergeObject(champ);
			String path;
			Banque bank;
//			Set<Banque> banks = tableAnnotationManager
//						.getBanquesManager(chp.getTableAnnotation());
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				bank = it.next();
				path = Utils.writeAnnoFilePath(baseDir, bank, chp, null);
				if (!delete) {
					if (new File(path).mkdirs()) {
						log.debug("Creation file system " + path);
					} else {
						log.error("Erreur dans la creation du systeme "
								+ "de fichier pour le champ " + chp.toString());
					}
				} else {
					// supprime le contenu
					File[] annos = new File(path).listFiles();
					if (annos != null) {
						for (int i = 0; i < annos.length; i++) {
							annos[i].delete();
							log.debug("Supprime " + annos[i].getName());
						}
					}
					// supprime le dossier
					new File(path).delete();
//					tableAnnotationManager
//						.getChampAnnotationsManager(chp.getTableAnnotation())
//										.remove(chp);
//					getAllChampsFromTableManager(chp).remove(chp);
				}
			}
		}
	}

	@Override
	public void createOrUpdateObjectManager(ChampAnnotation champ,
			TableAnnotation table,
			DataType dataType, List<Item> items,
			List<AnnotationDefaut> defauts, 
			Utilisateur utilisateur,
			Banque banque,
			String operation,
			String baseDir) {


		if (operation == null) {
			throw new NullPointerException("operation cannot be "
					+ "set to null for createorUpdateMethod");
		}

		checkRequiredObjectsAndValidate(champ, table, dataType, items,
				defauts, operation);

		//Doublon
		if (!findDoublonManager(champ)) {

			if ((operation.equals("creation") 
					|| operation.equals("modification"))) {
				try {
					if (operation.equals("creation")) {
						champAnnotationDao.createObject(champ);
						log.info("Enregistrement objet ChampAnnotation " 
								+ champ.toString());
						// cree l'arborescence si annotation fichier
						if (champ.getDataType().getType().equals("fichier")) {
							createOrDeleteFileDirectoryManager(baseDir, 
																champ, false, 
											getBanquesFromTableManager(champ));
						}
						CreateOrUpdateUtilities
							.createAssociateOperation(champ, operationManager, 
								operationTypeDao.findByNom("Creation").get(0),
								utilisateur);
					} else {
						champAnnotationDao.updateObject(champ);
						log.info("Modification objet ChampAnnotation "
								+ champ.toString());
						CreateOrUpdateUtilities
						.createAssociateOperation(champ, operationManager, 
							operationTypeDao.findByNom("Modification").get(0),
								utilisateur);
					}
					// ajout association vers items
					if (items != null) {
						updateItems(champ, items, banque);
					}

					// ajout association vers items
					if (defauts != null) {
						updateDefauts(champ, defauts);
					}
				} catch (RuntimeException re) {
					// en cas d'erreur lors enregistrement d'un champ du a 
					// l'acces au filesystem
					// le rollback se fera mais objet aura un id assigne
					// qui déclenchera une TransientException si on essaie 
					// d'enregistrer a nouveau.
					if (operation.equals("creation")) {
						champ.setChampAnnotationId(null);
					}
					throw re;
				}
			} else {
				throw new IllegalArgumentException("Operation must match "
						+ "'creation/modification' values");
			}
		} else {
			log.warn("Doublon lors " + operation + " objet ChampAnnotation "
					+ champ.toString());
			throw new DoublonFoundException("ChampAnnotation", operation);
		}		
	}
	
	@Override
	public Set<ChampAnnotation> getAllChampsFromTableManager(
														ChampAnnotation chp) {
		Set<ChampAnnotation> champs = new HashSet<ChampAnnotation>();
		TableAnnotation tab = chp.getTableAnnotation();
		if (tab != null && tab.getTableAnnotationId() != null) {
			TableAnnotation table = tableAnnotationDao.mergeObject(tab);
			champs = table.getChampAnnotations();
			champs.isEmpty(); // operation empechant LazyInitialisationException
		}
		return champs;
	}
	
	@Override
	public List<Banque> getBanquesFromTableManager(ChampAnnotation chp) {
		List<Banque> banks = new ArrayList<Banque>();
		if (chp != null && chp.getTableAnnotation() != null 
				&& chp.getTableAnnotation().getTableAnnotationId() != null) {
			return banqueDao.findByTableAnnotation(chp.getTableAnnotation());
		}
		return banks;
		
	}

	@Override
	public List<ChampAnnotation> findByEditByCatalogueManager(
												TableAnnotation tab) {
		return champAnnotationDao.findByEditByCatalogue(tab);
	}

	@Override
	public boolean isUsedItemManager(Item item) {
		return annotationValeurDao.findCountByItem(item).get(0) > 0;
	}

	@Override
	public List<ChampAnnotation> findChampsFichiersByTableManager(
											TableAnnotation table) {
		return champAnnotationDao.findByTableAndType(table, 
						dataTypeDao.findByType("fichier").get(0));
	}

	@Override
	public List<? extends Object> isUsedObjectManager(ChampAnnotation c) {
		List<Object> objs =  new ArrayList<Object>();
		Iterator<?> crits = 
			champAnnotationDao.findCriteresByChampAnnotation(c).iterator();
		while (crits.hasNext()) {
			objs.add(crits.next());
		}
		Iterator<?> imports = champAnnotationDao
					.findImportColonnesByChampAnnotation(c).iterator();
		while (imports.hasNext()) {
			objs.add(imports.next());
		}
		Iterator<?> res = champAnnotationDao
				.findResultatsByChampAnnotation(c).iterator();
		while (res.hasNext()) {
			objs.add(res.next());
		}
		Iterator<?> chpL = champAnnotationDao
				.findChpLEtiquetteByChampAnnotation(c).iterator();
		while (chpL.hasNext()) {
			objs.add(chpL.next());
		}
		return objs;
	}

	@Override
	public List<ChampAnnotation> findByImportTemplateAndEntiteManager(
			ImportTemplate ip, Entite e) {
		return champAnnotationDao.findByImportTemplateAndEntite(ip, e);
	}
}
