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
package fr.aphp.tumorotek.manager.impl.coeur.prodderive;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;
import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ModePrepaDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdQualiteDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.stockage.EmplacementDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.dao.utilisateur.ReservationDao;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectStatutException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.exception.TransformationQuantiteOverDemandException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Reservation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * Implémentation du manager du bean de domaine ProdDerive.
 * Interface créée le 02/10/09.
 * 
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class ProdDeriveManagerImpl implements ProdDeriveManager {
	
	private Log log = LogFactory.getLog(ProdDeriveManager.class);
	
	private ProdDeriveDao prodDeriveDao;
	private BanqueDao banqueDao;
	private ProdTypeDao prodTypeDao;
	private ObjetStatutDao objetStatutDao;
	private CollaborateurDao collaborateurDao;
	private EmplacementDao emplacementDao;
	private UniteDao uniteDao;
	private ProdQualiteDao prodQualiteDao;
	private TransformationDao transformationDao;
	private ReservationDao reservationDao;
	private ProdDeriveValidator prodDeriveValidator;
	private OperationTypeDao operationTypeDao;
	private OperationManager operationManager;
	private TransformationManager transformationManager;
	private EntiteManager entiteManager;
	private EntityManagerFactory entityManagerFactory;
	private EntiteDao entiteDao;
	private EmplacementManager emplacementManager;
	private AnnotationValeurManager annotationValeurManager;
	private CederObjetManager cederObjetManager;
	private ModePrepaDeriveDao modePrepaDeriveDao;
	private ImportHistoriqueManager importHistoriqueManager;
	private ConteneurManager conteneurManager;
	private PrelevementDao prelevementDao;
	private RetourManager retourManager;
	private ObjetNonConformeManager objetNonConformeManager;
	private ObjetStatutManager objetStatutManager;
	private EchantillonDao echantillonDao;

	public void setProdDeriveDao(ProdDeriveDao pDao) {
		this.prodDeriveDao = pDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setProdTypeDao(ProdTypeDao pDao) {
		this.prodTypeDao = pDao;
	}

	public void setObjetStatutDao(ObjetStatutDao oDao) {
		this.objetStatutDao = oDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	public void setEmplacementDao(EmplacementDao eDao) {
		this.emplacementDao = eDao;
	}

	public void setUniteDao(UniteDao uDao) {
		this.uniteDao = uDao;
	}

	public void setProdQualiteDao(ProdQualiteDao pDao) {
		this.prodQualiteDao = pDao;
	}

	public void setTransformationDao(TransformationDao tDao) {
		this.transformationDao = tDao;
	}

	public void setReservationDao(ReservationDao rDao) {
		this.reservationDao = rDao;
	}

	public void setProdDeriveValidator(ProdDeriveValidator validator) {
		this.prodDeriveValidator = validator;
	}

	public void setOperationTypeDao(OperationTypeDao oDao) {
		this.operationTypeDao = oDao;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	/**
	 * Setter du bean TransformationManager.
	 * @param tManager est le bean Manager.
	 */
	public void setTransformationManager(TransformationManager tManager) {
		this.transformationManager = tManager;
	}
	
	/**
	 * Setter du bean EntiteManager.
	 * @param eManager est le bean Manager.
	 */
	public void setEntiteManager(EntiteManager eManager) {
		this.entiteManager = eManager;
	}

	/**
	 * Setter du EntityManagerFactory.
	 * @param eFactory.
	 */
	public void setEntityManagerFactory(EntityManagerFactory eFactory) {
		this.entityManagerFactory = eFactory;
	}

	/**
	 * Setter du bean EntiteDao.
	 * @param eDao est le bean Dao.
	 */
	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	public void setEmplacementManager(EmplacementManager eManager) {
		this.emplacementManager = eManager;
	}

	public void setAnnotationValeurManager(AnnotationValeurManager avManager) {
		this.annotationValeurManager = avManager;
	}

	public void setCederObjetManager(CederObjetManager cManager) {
		this.cederObjetManager = cManager;
	}

	public void setModePrepaDeriveDao(ModePrepaDeriveDao mDao) {
		this.modePrepaDeriveDao = mDao;
	}
	
	public void setImportHistoriqueManager(
			ImportHistoriqueManager iManager) {
		this.importHistoriqueManager = iManager;
	}

	public void setConteneurManager(ConteneurManager cManager) {
		this.conteneurManager = cManager;
	}

	public void setRetourManager(RetourManager rManager) {
		this.retourManager = rManager;
	}

	public void setObjetNonConformeManager(ObjetNonConformeManager oM) {
		this.objetNonConformeManager = oM;
	}
	
	public void setObjetStatutManager(ObjetStatutManager o) {
		this.objetStatutManager = o;
	}

	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setEchantillonDao(EchantillonDao eDao) {
		this.echantillonDao = eDao;
	}

	/**
	 * Recherche un ProdDerive dont l'identifiant est passé en paramètre.
	 * @param prodDeriveId Identifiant du ProdDerive que l'on recherche.
	 * @return Un ProdDerive.
	 */
	public ProdDerive findByIdManager(Integer prodDeriveId) {
		return prodDeriveDao.findById(prodDeriveId);
	}
	
	/**
	 * Recherche tous les Produits Derives présents dans la base.
	 * @return Liste de ProdDerives.
	 */
	public List<ProdDerive> findAllObjectsManager() {
		log.debug("Recherche de tous les ProdDerives");
		return prodDeriveDao.findAll();
	}
	
	@Override
	public List<ProdDerive> findByBanquesManager(List<Banque> banks) {
		List<ProdDerive> derives = new ArrayList<ProdDerive>();
		if (banks != null && banks.size() > 0) {
			derives = prodDeriveDao.findByBanques(banks);
		}
		return derives;
	}
	
	@Override
	public List<Integer> findAllObjectsIdsByBanquesManager(
			List<Banque> banques) {
		if (banques != null && banques.size() > 0) {
			return prodDeriveDao.findByBanquesAllIds(banques);
		} else {
			return new ArrayList<Integer>();
		}
	}
	
	/**
	 * Recherche une liste de produits dérivés dont la date de stockage
	 * est plus récente que celle passée en paramètre.
	 * @param date Date de stockage pour laquelle on recherche des produits
	 * dérivés.
	 * @return Liste de ProdDerive.
	 */
	public List<ProdDerive> findByDateStockAfterDateManager(Date date) {
		Calendar cal = null;
		if (date != null) {
			log.debug("Recherche des produits derives stockes apres la date " 
				+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
			cal = Calendar.getInstance();
			cal.setTime(date);		
		} 
		return prodDeriveDao.findByDateStockAfterDate(cal);
	}
	
	/**
	 * Recherche une liste de produits dérivés dont la date de transformation
	 * est plus récente que celle passée en paramètre.
	 * @param date Date de transformation pour laquelle on recherche des
	 * produits dérivés.
	 * @return Liste de ProdDerive.
	 */
	public List<ProdDerive> findByDateTransfoAfterDateManager(Date date) {
		Calendar cal = null;
		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);					
		} 
		return prodDeriveDao.findByDateTransformationAfterDate(cal);
	}
	
	@Override
	public List<ProdDerive> 
		findByDateTransformationAfterDateWithBanqueManager(Date date, 
														List<Banque> banks) {
		List<ProdDerive> derives = new ArrayList<ProdDerive>();
		Calendar cal = null;
		if (date != null && banks != null && banks.size() > 0) {
			cal = Calendar.getInstance();
			cal.setTime(date);	
			Iterator<Banque> it = banks.iterator();
			while (it.hasNext()) {
				derives.addAll(prodDeriveDao
					.findByDateTransformationAfterDateWithBanque(cal, 
																it.next()));
			}
		} 
		return  derives;
	}
	
	/**
	 * Recherche une liste de produits dérivés dont le code commence comme
	 * celui passé en paramètre.
	 * @param code Code pour lequel on recherche des produits dérivés.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de ProdDerive.
	 */
	public List<ProdDerive> findByCodeLikeManager(
			String code, boolean exactMatch) {
		log.debug("Recherche ProdDerive par " 
				+ code + " exactMatch " + String.valueOf(exactMatch));
		if (!exactMatch) {
			code = code + "%";
		}
		return prodDeriveDao.findByCode(code);
	}
	
	/**
	 * Recherche une liste de produits dérivés dont le code ou le code labo
	 * commence comme celui passé en paramètre.
	 * @param code Code pour lequel on recherche des produits dérivés.
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
	 * exactes.
	 * @return Liste de ProdDerive.
	 */
	public List<ProdDerive> findByCodeOrLaboWithBanqueManager(
			String code, Banque banque, boolean exactMatch) {
		log.debug("Recherche ProdDerive par " 
				+ code + " exactMatch " + String.valueOf(exactMatch));
		if (banque != null) {
			if (!exactMatch) {
				code = code + "%";
			}
			return prodDeriveDao.findByCodeOrLaboWithBanque(code, banque);
		} else {
			return new ArrayList<ProdDerive>();
		}
	}
	
	@Override
	public List<Integer> findByCodeOrLaboBothSideWithBanqueReturnIdsManager(
			String code, List<Banque> banques, boolean exactMatch) {
		List<Integer> derives = new ArrayList<Integer>();
		if (code != null && banques != null) {
			if (!exactMatch) {
				code = "%" + code + "%";
			}
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				derives
					.addAll(prodDeriveDao
							.findByCodeOrLaboWithBanqueReturnIds(
									code, it.next()));
			}
		} 
		return derives;
	}
	
	/**
	 * Recherche la liste des codes utilisés par les prodderives liés à la
	 * banque passée en paramètre.
	 * @param banque Banque pour laquelle on recherche les codes.
	 * @return Liste de codes.
	 */
	public List<String> findAllCodesForBanqueManager(Banque banque) {
		if (banque != null) {
			return prodDeriveDao.findByBanqueSelectCode(banque);
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Recherche la liste des codes utilisés par les échantillons liés à la
	 * banque passée en paramètre et qui sont stockés.
	 * @param banque Banque pour laquelle on recherche les codes.
	 * @return Liste de codes.
	 */
	@Override
	public List<String> findAllCodesForBanqueAndStockesManager(Banque banque) {
		ObjetStatut stocke = objetStatutDao.findByStatut("STOCKE").get(0);
		
		if (stocke != null && banque != null) {
			return prodDeriveDao.findByBanqueStatutSelectCode(banque, stocke);
		} else {
			return new ArrayList<String>();
		}
			
	}
	
	@Override
	public List<String> findAllCodesForMultiBanquesAndStockesManager(
			List<Banque> banques) {
		ObjetStatut stocke = objetStatutDao.findByStatut("STOCKE").get(0);
		
		if (stocke != null && banques != null) {
			return prodDeriveDao
				.findByBanqueInListStatutSelectCode(banques, stocke);
		} else {
			return new ArrayList<String>();
		}
			
	}
	
	@Override
	public List<String> findAllCodesForBanqueAndQuantiteManager(Banque banque) {
		if (banque != null) {
			return prodDeriveDao.findByBanqueAndQuantiteSelectCode(banque);
		} else {
			return new ArrayList<String>();
		}
	}
	
	@Override
	public List<ProdDerive> findByParentAndTypeManager(Object parent,
			String type) {
		if (parent != null && type != null && !type.equals("")) {
			Integer objetId = null;
			Entite entite = null;
			if (parent.getClass().getSimpleName().equals("Prelevement")) {
				objetId = ((Prelevement) parent).getPrelevementId();
				entite = entiteDao.findByNom("Prelevement").get(0);
			} else if (parent.getClass().getSimpleName()
					.equals("Echantillon")) {
				objetId = ((Echantillon) parent).getEchantillonId();
				entite = entiteDao.findByNom("Echantillon").get(0);
			} else if (parent.getClass().getSimpleName().equals("ProdDerive")) {
				objetId = ((ProdDerive) parent).getProdDeriveId();
				entite = entiteDao.findByNom("ProdDerive").get(0);
			}
			
			if (objetId != null && entite != null) {
				return prodDeriveDao.findByParentAndType(
						objetId, entite, type);
			} else {
				return new ArrayList<ProdDerive>();
			}
		} else {
			return new ArrayList<ProdDerive>();
		}
	}
	
	@Override
	public List<Integer> findByPatientNomReturnIdsManager(String nom,
			List<Banque> banks, boolean exactMatch) {
		List<Integer> liste1 = new ArrayList<Integer>();
		List<Integer> liste2 = new ArrayList<Integer>();
		if (nom != null && banks != null) {
			if (!exactMatch) {
				nom = "%" + nom + "%";
			}
			for (int i = 0; i < banks.size(); i++) {
				liste1.addAll(prodDeriveDao
					.findByEchantillonPatientNomReturnIds(nom, banks.get(i)));
				
				liste2.addAll(prodDeriveDao
						.findByPrelevementPatientNomreturnIds(
								nom, banks.get(i)));
			}
			
			for (int i = 0; i < liste2.size(); i++) {
				if (!liste1.contains(liste2.get(i))) {
					liste1.add(liste2.get(i));
				}
			}
		}
		return liste1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findByCodeInListManager(List<String> criteres,
			List<Banque> banques, List<String> notfounds) {
		if (criteres != null && criteres.size() > 0 && notfounds != null
				&& banques != null && banques.size() > 0) {
			List<Object[]> res =  prodDeriveDao
							.findByCodeInListWithBanque(criteres, banques);
			List<Integer> idFounds = new ArrayList<Integer>();
			List<String> codeFounds = new ArrayList<String>();
			for (Object[] objArray : res) {
				idFounds.add((Integer) objArray[0]);
				codeFounds.add((String) objArray[1]);
			}
			
			// since 2.1
			// notfounds
			notfounds.addAll(CollectionUtils.subtract(criteres,codeFounds));
		
			return idFounds;
		} else {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public List<Integer> findByPatientNomOrNipInListManager(
			List<String> criteres, List<Banque> banks) {
		List<Integer> liste1 = new ArrayList<Integer>();
		List<Integer> liste2 = new ArrayList<Integer>();
		if (criteres != null && criteres.size() > 0
				&& banks != null && banks.size() > 0) {
			liste1.addAll(prodDeriveDao
				.findByEchantillonPatientNomInListReturnIds(criteres, banks));
				
			liste2.addAll(prodDeriveDao
				.findByPrelevementPatientNomInListreturnIds(criteres, banks));
			
			for (int i = 0; i < liste2.size(); i++) {
				if (!liste1.contains(liste2.get(i))) {
					liste1.add(liste2.get(i));
				}
			}
		}
		return liste1;
	}
	
	/**
	 * Recherche une liste de produits dérivés dont le prodDerive est 
	 * passé en paramètre.
	 * @param prodDerive ProdDerive pour lequel on recherche des
	 * produits dérivés.
	 * @return List de ProdDerives.
	 */
	@Override
	public List<ProdDerive> getProdDerivesManager(ProdDerive prodDerive) {
		
		List<Transformation> list = 
			transformationManager.findByParentManager(prodDerive);
		List<ProdDerive> derives = new ArrayList<ProdDerive>();
		
		for (int i = 0; i < list.size(); ++i) {
			Transformation transfo = transformationDao.mergeObject(list.get(i));
			Set<ProdDerive> tmp = transfo.getProdDerives();
			Iterator<ProdDerive> it = tmp.iterator();
			while (it.hasNext()) {
				ProdDerive current = it.next();
				if (!derives.contains(current)) {
					derives.add(current);
				}
			}
		}
		
		return derives;
	}
	
	@Override
	public Emplacement getEmplacementManager(ProdDerive prodDerive) {
		if (prodDerive != null) {
			prodDerive = prodDeriveDao.mergeObject(prodDerive);
			Emplacement empl = prodDerive.getEmplacement();
			
			if (empl != null) {
				empl.getPosition();
			}
			return empl;
		} else {
			return null;
		}
	}
	
	@Override
	public String getEmplacementAdrlManager(ProdDerive prodDerive) {
		if (prodDerive != null) {
			prodDerive = prodDeriveDao.mergeObject(prodDerive);
			
			return emplacementManager.getAdrlManager(
					prodDerive.getEmplacement(), false);
			
		} else {
			return "";
		}
	}

	@Override
	public Boolean findDoublonManager(ProdDerive prodDerive) {
		// Banque banque = prodDerive.getBanque();
		List<ProdDerive> dbls = prodDeriveDao
			.findByCodeInPlateforme(prodDerive.getCode(), 
					prodDerive.getBanque() != null ? 
							prodDerive.getBanque().getPlateforme() :  null);
		//		.findByCodeOrLaboWithBanque(prodDerive.getCode(), banque);
		
		if (!dbls.isEmpty()) {
			if (prodDerive.getProdDeriveId() == null) {
				return true;
			} else {
				for (ProdDerive d : dbls) {
					if (!d.getProdDeriveId().equals(prodDerive.getProdDeriveId())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public List<Integer> findAfterDateCreationReturnIdsManager(
			Calendar date, List<Banque> banques) {
		List<Integer> liste = new ArrayList<Integer>();	
		if (date != null && banques != null) {
			log.debug("Recherche des ProdDerives enregistres apres la date " 
				+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss")
				.format(date.getTime()));
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				liste.addAll(findByOperationTypeAndDateReturnIds(
						operationTypeDao
							.findByNom("Creation").get(0), date, it.next()));
			}
		}
		Collections.sort(liste);
		return liste;
	}
	
	/**
	 * Recherche tous les dérivés dont la date de modification systeme est 
	 * posterieure ou egale a celle passee en parametre.
	 * @param date
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return Liste de ProdDerive.
	 */
	public List<ProdDerive> findAfterDateModificationManager(
			Calendar date, Banque banque) {
		List<ProdDerive> liste = new ArrayList<ProdDerive>();
		if (date != null && banque != null) {
			log.debug("Recherche des ProdDerive modifies apres la date " 
				+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss")
				.format(date.getTime()));
			liste = findByOperationTypeAndDate(operationTypeDao
									.findByNom("Modification").get(0), 
									date, banque);	
		}
		return liste;
	}
	
	/**
	 * Recupère l'objet parent d'un produit dérivé.
	 * @param prodDerive ProdDerive dont on souhaite obtenir le parent.
	 * @return L'objet parent du produit dérivé.
	 */
	public Object findParent(ProdDerive prodDerive) {
		Transformation transformation = prodDerive.getTransformation();
		if (transformation != null) {
			return entiteManager.findObjectByEntiteAndIdManager(
					transformation.getEntite(),
					transformation.getObjetId());
		} else {
			return null;
		}
	}
	
	/**
	 * Recupere la liste de dérivés en fonction du type d'operation et 
	 * d'une date a laquelle la date d'enregistrement de l'operation doit
	 * etre superieure ou egale.
	 * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
	 * utilises pour recuperer les échantillons.
	 * @param oType OperationType
	 * @param date
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return List de ProdDerive.
	 */
	@SuppressWarnings("unchecked")
	private List<ProdDerive> findByOperationTypeAndDate(OperationType oType, 
			Calendar date, Banque banque) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
				+ "Operation o WHERE o.entite = :entite AND "
				+ "o.operationType = :oType AND date >= :date");
		opQuery.setParameter("entite", entiteDao.findByNom("ProdDerive"));
		opQuery.setParameter("oType", oType);
		opQuery.setParameter("date", date);
		List<Integer> ids =  opQuery.getResultList();
		Query prelQuery;
		if (ids.size() > 1 && banque != null) { //HQL IN () si liste taille > 1
			prelQuery = em.createQuery("SELECT DISTINCT p FROM ProdDerive p "
					+ "WHERE p.prodDeriveId IN (:ids) " 
					+ "AND p.banque = :banque");
			prelQuery.setParameter("ids", ids);
			prelQuery.setParameter("banque", banque);
		} else if (ids.size() == 1 && banque != null) {
			prelQuery = em.createQuery("SELECT DISTINCT p FROM ProdDerive p "
					+ "WHERE p.prodDeriveId = :id "
					+ "AND p.banque = :banque");
			prelQuery.setParameter("id", ids.get(0));
			prelQuery.setParameter("banque", banque);
		} else {
			return new ArrayList<ProdDerive>();
		}
	
		return prelQuery.getResultList();
	}
	
	/**
	 * Recupere la liste d'Ids de dérivés en fonction du type d'operation et 
	 * d'une date a laquelle la date d'enregistrement de l'operation doit
	 * etre superieure ou egale.
	 * Dans un premier temps, recupere la liste des objetIds qui sont ensuite
	 * utilises pour recuperer les échantillons.
	 * @param oType OperationType
	 * @param date
	 * @param banque Banque à laquelle appartient le dérivé.
	 * @return List de ProdDerive.
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> findByOperationTypeAndDateReturnIds(
			OperationType oType, 
			Calendar date, Banque banque) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
				+ "Operation o WHERE o.entite = :entite AND "
				+ "o.operationType = :oType AND date >= :date");
		opQuery.setParameter("entite", entiteDao.findByNom("ProdDerive"));
		opQuery.setParameter("oType", oType);
		opQuery.setParameter("date", date);
		List<Integer> ids =  opQuery.getResultList();
		Query prelQuery;
		if (ids.size() > 1 && banque != null) { //HQL IN () si liste taille > 1
			prelQuery = em.createQuery("SELECT DISTINCT p.prodDeriveId " 
					+ "FROM ProdDerive p "
					+ "WHERE p.prodDeriveId IN (:ids) " 
					+ "AND p.banque = :banque");
			prelQuery.setParameter("ids", ids);
			prelQuery.setParameter("banque", banque);
		} else if (ids.size() == 1 && banque != null) {
			prelQuery = em.createQuery("SELECT DISTINCT p.prodDeriveId " 
					+ "FROM ProdDerive p "
					+ "WHERE p.prodDeriveId = :id "
					+ "AND p.banque = :banque");
			prelQuery.setParameter("id", ids.get(0));
			prelQuery.setParameter("banque", banque);
		} else {
			return new ArrayList<Integer>();
		}
	
		return prelQuery.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdDerive> findLastCreationManager(
										List<Banque> banques, int nbResults) {
		List<ProdDerive> liste = new ArrayList<ProdDerive>();	
		if (banques != null && banques.size() > 0 && nbResults > 0) {
			log.debug("Recherche des " + nbResults + " derniers Dérivés " 
					+ "enregistres.");
//			EntityManager em = entityManagerFactory.createEntityManager();
//			Query query = em.createQuery("SELECT p " 
//					+ "FROM ProdDerive p " 
//					+ "WHERE p.banque in (:banque) " 
//					+ "ORDER BY p.prodDeriveId DESC");
//			query.setParameter("banque", banques);
//			query.setMaxResults(nbResults);
//			
//			List<ProdDerive> res = query.getResultList();
//			for (int i = 0; i < res.size(); i++) {
//				liste.add(0, res.get(i));
//			}
			EntityManager em = entityManagerFactory.createEntityManager();
//			Query query = em.createQuery("SELECT p " 
//					+ "FROM ProdDerive p, Operation o " 
//					+ "WHERE o.objetId = p.prodDeriveId " 
//					+ "AND o.entite = :entite " 
//					+ "AND o.operationType = :oType " 
//					+ "AND p.banque in (:banque) " 
//					+ "ORDER BY o.date DESC");
//			query.setParameter("entite", entiteDao.findByNom("ProdDerive"));
//			query.setParameter("oType", operationTypeDao
//												.findByNom("Creation").get(0));
			Query query = em.createQuery("SELECT p " 
					+ "FROM ProdDerive p " 
					+ "WHERE p.banque in (:banque) " 
					+ "ORDER BY p.prodDeriveId DESC");
			query.setFirstResult(0);
			query.setParameter("banque", banques);
			query.setMaxResults(nbResults);
			
			liste.addAll(query.getResultList());
		}
		return liste;
	}
	
	/**
	 * Récupère une liste de dérivés en fonction de leur banque et d'un
	 * type d'opération. Cette liste est ordonnée par la date de l'opéartion.
	 * Sa taille maximale est fixée par un paramètre.
	 * @param oType Type de l'opération.
	 * @param banques liste de Banque des produits dérivés.
	 * @param nbResults Nombre max de dérivés souhaités.
	 * @return Liste de ProdDerives.
	 */
//	@SuppressWarnings("unchecked")
//	private List<ProdDerive> findByLastOperationType(OperationType oType, 
//			List<Banque> banques, int nbResults) {
//		
//		List<ProdDerive> derives = new ArrayList<ProdDerive>();
//		
//		if (banques.size() > 0) {
//			EntityManager em = entityManagerFactory.createEntityManager();
//			Query query = em.createQuery("SELECT DISTINCT p " 
//					+ "FROM ProdDerive p, Operation o " 
//					+ "WHERE o.objetId = p.prodDeriveId " 
//					+ "AND o.entite = :entite " 
//					+ "AND o.operationType = :oType " 
//					+ "AND p.banque in (:banque) " 
//					+ "ORDER BY p.prodDeriveId DESC");
//			query.setParameter("entite", entiteDao.findByNom("ProdDerive"));
//			query.setParameter("oType", oType);
//			query.setParameter("banque", banques);
//			query.setMaxResults(nbResults);
//			
//			derives.addAll(query.getResultList());
//		}
//		
//		return derives;
//		
//	}
	
	@Override
	public List<ProdDerive> findByTransformationManager(Transformation 
																	transfo) {
		return prodDeriveDao.findByTransformation(transfo);
	}
	
	@Override
	public Prelevement getPrelevementParent(ProdDerive prodDerive) {
		if (prodDerive != null) {
			Object currentObj = prodDerive;
			
			while (currentObj != null
					&& currentObj.getClass()
					.getSimpleName().equals("ProdDerive")) {
				ProdDerive tmp = (ProdDerive) currentObj;
				currentObj = findParent(tmp);
			}
			
			if (currentObj != null) {
				if (currentObj.getClass()
					.getSimpleName().equals("Echantillon")) {
					List<Prelevement> prels = prelevementDao
						.findByEchantillonId(((Echantillon) currentObj)
												.getEchantillonId());
					if (!prels.isEmpty()) {
						return prels.get(0);
					} else {
						return null;
					}
				} else if (currentObj.getClass()
					.getSimpleName().equals("Prelevement")) {
					return (Prelevement) currentObj;
				} else {
					return null;
				}
			} else {
				return null;
			}
			
		} else {
			return null;
		}
	}
	
	public Patient getPatientParentManager(ProdDerive prodDerive) {
		if (prodDerive != null) {
			Prelevement prlvt = getPrelevementParent(prodDerive);
			if (prlvt != null
					&& prlvt.getMaladie() != null
					&& prlvt.getMaladie().getPatient() != null) {
				return prlvt.getMaladie().getPatient();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public void createObjectManager(ProdDerive prodDerive, Banque banque,
			ProdType type, ObjetStatut statut, Collaborateur collab,
			Emplacement emplacement, Unite volumeUnite, Unite concUnite,
			Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
			ProdQualite qualite, Transformation transfo,
			List<AnnotationValeur> listAnnoToCreateOrUpdate, 
			List<File> filesCreated,
			Reservation reservation, Utilisateur utilisateur, 
			boolean doValidation, String baseDir,
			boolean isImport) {
		
		// On vérifie que la banque n'est pas null. Si c'est le cas on envoie
		// une exception
		if (banque == null) {
			log.warn("Objet obligatoire Banque manquant lors " 
					+ "de la creation " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "creation", "Banque");
		} else {
			prodDerive.setBanque(banqueDao.mergeObject(banque));
		}
		// On vérifie que le type n'est pas null. Si c'est le cas on envoie
		// une exception
		if (type == null) {
			log.warn("Objet obligatoire ProdType manquant lors " 
					+ "de la creation " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "creation", "ProdType");
		} else {
			prodDerive.setProdType(prodTypeDao.mergeObject(type));
		}
		// On vérifie que le statut n'est pas null. Si c'est le cas 
		// on envoie une exception
		if (statut != null) {
			prodDerive.setObjetStatut(
					objetStatutDao.mergeObject(statut));
		} else if (prodDerive.getObjetStatut() == null){
			log.warn("Objet obligatoire ObjetStatut manquant lors " 
					+ "de la creation " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "creation", "ObjetStatut");
		}
		
		// On vérifie que le statut n'est pas null. Si c'est le cas 
		// on envoie une exception
		if (statut != null) {
			prodDerive.setObjetStatut(
					objetStatutDao.mergeObject(statut));
		} else if (prodDerive.getObjetStatut() == null){
			log.warn("Objet obligatoire ObjetStatut manquant lors " 
					+ "de la creation " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "creation", "ObjetStatut");
		}
		
		if (findDoublonManager(prodDerive)) {
			log.warn("Doublon lors de la creation de l'objet ProdDerive : " 
					+ prodDerive.toString());
			throw new DoublonFoundException("ProdDerive", "creation", prodDerive.getCode(), null);
		} else {
			try {
				// On vérifie que le collaborateur n'est pas null
				if (collab != null) {
					prodDerive.setCollaborateur(collaborateurDao
							.mergeObject(collab));
				} else {
					prodDerive.setCollaborateur(null);
				}
				// On vérifie que l'emplacement n'est pas null
				if (emplacement != null 
						&& checkEmplacementOccupied(emplacement, prodDerive)) {
					prodDerive.setEmplacement(emplacementDao
							.mergeObject(emplacement));

				} else {
					prodDerive.setEmplacement(null);
				}
				// On vérifie que l'unité de volume n'est pas null
				if (volumeUnite != null) {
					prodDerive
							.setVolumeUnite(uniteDao.mergeObject(volumeUnite));
				} else {
					prodDerive.setVolumeUnite(null);
				}
				// On vérifie que l'unité de concentration n'est pas null
				if (concUnite != null) {
					prodDerive.setConcUnite(uniteDao.mergeObject(concUnite));
				} else {
					prodDerive.setConcUnite(null);
				}
				// On vérifie que l'unité de quantité n'est pas null
				if (quantiteUnite != null) {
					prodDerive.setQuantiteUnite(uniteDao
							.mergeObject(quantiteUnite));
				} else {
					prodDerive.setQuantiteUnite(null);
				}
				// On vérifie que la préparation n'est pas null
				if (modePrepaDerive != null) {
					prodDerive.setModePrepaDerive(
							modePrepaDeriveDao.mergeObject(
							modePrepaDerive));
				} else {
					prodDerive.setModePrepaDerive(null);
				}
				// On vérifie que la qualité n'est pas null
				if (qualite != null) {
					prodDerive.setProdQualite(prodQualiteDao
							.mergeObject(qualite));
				} else {
					prodDerive.setProdQualite(null);
				}
				// On vérifie que la réservation n'est pas null
				if (reservation != null) {
					prodDerive.setReservation(reservationDao
							.mergeObject(reservation));
				} else {
					prodDerive.setReservation(null);
				}
				if (doValidation) {
					BeanValidator.validateObject(prodDerive,
							new Validator[] { prodDeriveValidator });
				}
				// On vérifie que la transformation n'est pas null
				if (transfo != null) {
					if (transfo.getTransformationId() == null) {
						transformationManager
								.createObjectManager(transfo, transfo
										.getEntite(), transfo
										.getQuantiteUnite());
					} else {
						transformationManager
								.updateObjectManager(transfo, transfo
										.getEntite(), transfo
										.getQuantiteUnite());
					}
					prodDerive.setTransformation(transformationDao
							.mergeObject(transfo));
				} else {
					prodDerive.setTransformation(null);
				}
				prodDeriveDao.createObject(prodDerive);
				log.info("Enregistrement de l'objet ProdDerive : "
						+ prodDerive.toString());
				//Enregistrement de l'operation associee
				Operation creationOp = new Operation();
				creationOp.setDate(Utils.getCurrentSystemCalendar());
				operationManager.createObjectManager(creationOp, utilisateur,
						operationTypeDao.findByNom("Creation").get(0),
						prodDerive);
				
				// cree les annotations, null operation pour
				// laisser la possibilité création/modification au sein 
				// de la liste
				if (listAnnoToCreateOrUpdate != null) {
					annotationValeurManager
						.createAnnotationValeurListManager(
											listAnnoToCreateOrUpdate, 
									prodDerive, utilisateur, null, baseDir, 
									filesCreated, null);
				}
				
			} catch (RuntimeException re) {
				
				if (filesCreated != null) {
					for (File f : filesCreated) {
						f.delete();
					}
				} else {
					log.warn("Rollback création fichier n'a pas pu être réalisée");
				}
				// en cas d'erreur lors enregistrement d'un code ou annotation
				// le rollback se fera mais derive aura un id assigne
				// qui déclenchera une TransientException si on essaie 
				// d'enregistrer a nouveau.
				if (!isImport) {
					if (prodDerive.getProdDeriveId() != null) {
						prodDerive.setProdDeriveId(null);
					}
				}
				throw(re);
			}
		}
	}
	
	@Override
	public void createObjectWithNonConformitesManager(ProdDerive prodDerive, 
			Banque banque,
			ProdType type, ObjetStatut statut, Collaborateur collab,
			Emplacement emplacement, Unite volumeUnite, Unite concUnite,
			Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
			ProdQualite qualite, Transformation transfo,
			List<AnnotationValeur> listAnnoToCreateOrUpdate, 
			Reservation reservation, Utilisateur utilisateur, 
			boolean doValidation, String baseDir,
			boolean isImport, List<NonConformite> noconfsTrait, 
			List<NonConformite> noconfsCess
			) {
		
		List<File> filesCreated = new ArrayList<File>();
		
			try {
				if (noconfsTrait != null && !noconfsTrait.isEmpty()) {
					prodDerive.setConformeTraitement(false);
				}
				
				if (noconfsCess != null && !noconfsCess.isEmpty()) {
					prodDerive.setConformeCession(false);
				}

				createObjectManager(prodDerive, banque, type, statut, collab, 
						emplacement, volumeUnite, concUnite, quantiteUnite, 
						modePrepaDerive, qualite, transfo, listAnnoToCreateOrUpdate, 
						filesCreated,
						reservation, utilisateur, doValidation, baseDir, isImport);

				objetNonConformeManager.createUpdateOrRemoveListObjectManager(
						prodDerive, noconfsTrait, "Traitement");
				objetNonConformeManager.createUpdateOrRemoveListObjectManager(
						prodDerive, noconfsCess, "Cession");
			} catch (RuntimeException re) {
				for (File f : filesCreated) {
					f.delete();
				}
				throw(re);
			}
	}

	@Override
	public void updateObjectManager(ProdDerive prodDerive, Banque banque,
			ProdType type, ObjetStatut statut, Collaborateur collab,
			Emplacement emplacement, Unite volumeUnite, Unite concUnite,
			Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
			ProdQualite qualite, Transformation transfo,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<AnnotationValeur> listAnnoToDelete, 
			List<File> filesCreated, List<File> filesToDelete,
			Reservation reservation, Utilisateur utilisateur, 
			boolean doValidation,
			List<OperationType> operations, String baseDir) {

		// On vérifie que la banque n'est pas null. Si c'est le cas on envoie
		// une exception
		if (banque == null) {
			log.warn("Objet obligatoire Banque manquant lors " 
					+ "de la modif " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "modification", "Banque");
		} else {
			prodDerive.setBanque(banqueDao.mergeObject(banque));
		}
		// On vérifie que le type n'est pas null. Si c'est le cas on envoie
		// une exception
		if (type == null) {
			log.warn("Objet obligatoire ProdType manquant lors " 
					+ "de la modif " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "modification", "ProdType");
		} else {
			prodDerive.setProdType(prodTypeDao.mergeObject(type));
		}
		// On vérifie que le statut n'est pas null. Si c'est le cas 
		// on envoie une exception
		if (statut != null) {
			prodDerive.setObjetStatut(
					objetStatutDao.mergeObject(statut));
		} else if (prodDerive.getObjetStatut() == null){
			log.warn("Objet obligatoire ObjetStatut manquant lors " 
					+ "de la creation " +  "d'un objet ProdDerive");
			throw new RequiredObjectIsNullException(
					"ProdDerive", "creation", "ObjetStatut");
		}
		
		if (findDoublonManager(prodDerive)) {
			log.warn("Doublon lors de la modif de l'objet ProdDerive : " 
					+ prodDerive.toString());
			throw new DoublonFoundException("ProdDerive", "modification", prodDerive.getCode(), null);
		} else {
			try {
				// On vérifie que le collaborateur n'est pas null
				if (collab != null) {
					prodDerive.setCollaborateur(collaborateurDao
							.mergeObject(collab));
				} else {
					prodDerive.setCollaborateur(null);
				}
				// On vérifie que l'emplacement n'est pas null
				if (emplacement != null 
						&& checkEmplacementOccupied(emplacement, prodDerive)) {
					prodDerive.setEmplacement(emplacementDao
							.mergeObject(emplacement));
					
				} else {
					prodDerive.setEmplacement(null);
				}
				// On vérifie que l'unité de volume n'est pas null
				if (volumeUnite != null) {
					prodDerive.setVolumeUnite(uniteDao
										.mergeObject(volumeUnite));
				} else {
					prodDerive.setVolumeUnite(null);
				}
				// On vérifie que l'unité de concentration n'est pas null
				if (concUnite != null) {
					prodDerive.setConcUnite(uniteDao.mergeObject(concUnite));
				} else {
					prodDerive.setConcUnite(null);
				}
				// On vérifie que l'unité de quantité n'est pas null
				if (quantiteUnite != null) {
					prodDerive.setQuantiteUnite(uniteDao
							.mergeObject(quantiteUnite));
				} else {
					prodDerive.setQuantiteUnite(null);
				}
				// On vérifie que la préparation n'est pas null
				if (modePrepaDerive != null) {
					prodDerive
						.setModePrepaDerive(modePrepaDeriveDao
								.mergeObject(modePrepaDerive));
				} else {
					prodDerive.setModePrepaDerive(null);
				}
				// On vérifie que la qualité n'est pas null
				if (qualite != null) {
					prodDerive.setProdQualite(prodQualiteDao
											.mergeObject(qualite));
				} else {
					prodDerive.setProdQualite(null);
				}
				// On vérifie que la réservation n'est pas null
				if (reservation != null) {
					prodDerive.setReservation(reservationDao
							.mergeObject(reservation));
				} else {
					prodDerive.setReservation(null);
				}
				
				if (doValidation) {
					BeanValidator.validateObject(
							prodDerive, new Validator[]{prodDeriveValidator});
				}
				
				// On vérifie que la transformation n'est pas null
				if (transfo != null) {
					if (transfo.getTransformationId() == null) {
						transformationManager.createObjectManager(
								transfo, transfo.getEntite(), 
								transfo.getQuantiteUnite());
					} else {
						transformationManager.updateObjectManager(
								transfo, transfo.getEntite(), 
								transfo.getQuantiteUnite());
					}
					prodDerive.setTransformation(
							transformationDao.mergeObject(transfo));
				} else {
					prodDerive.setTransformation(null);
				}

				prodDeriveDao.updateObject(prodDerive);
				log.info("Modification de l'objet ProdDerive : " 
						+ prodDerive.toString());
				
				//Enregistrement de l'operation associee
				Operation creationOp = new Operation();
				creationOp.setDate(Utils.getCurrentSystemCalendar());
				operationManager.createObjectManager(creationOp, utilisateur,
							operationTypeDao.findByNom("Modification").get(0), 
									prodDerive);
				
				if (operations != null) {
					for (int i = 0; i < operations.size(); i++) {
						//Enregistrement de l'operation associee
						Operation dateOp = new Operation();
						dateOp.setDate(Utils.getCurrentSystemCalendar());
						operationManager.createObjectManager(dateOp, 
								utilisateur,
								operations.get(i), prodDerive);
					}
				}
				
				// Annotations
				// suppr les annotations
				if (listAnnoToDelete != null) {
					annotationValeurManager
						.removeAnnotationValeurListManager(listAnnoToDelete, 
								filesToDelete);
				}
				
				// update les annotations, null operation pour
				// laisser la possibilité création/modification au sein 
				// de la liste
				if (listAnnoToCreateOrUpdate != null) {
					annotationValeurManager
						.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, 
							prodDerive, utilisateur, null, baseDir, 
							filesCreated, filesToDelete);
				}
				
				// enregistre operation associee annotation 
				// si il y a eu des deletes et pas d'updates
				if ((listAnnoToCreateOrUpdate == null 
						|| listAnnoToCreateOrUpdate.isEmpty())
					&& (listAnnoToDelete != null 
							&& !listAnnoToDelete.isEmpty())) {
					CreateOrUpdateUtilities
					.createAssociateOperation(prodDerive, operationManager, 
							operationTypeDao.findByNom("Annotation").get(0), 
																utilisateur);
				}
				
				if (filesToDelete != null) {
					for (File f : filesToDelete) {
						f.delete();
					}
				}
				
			} catch (RuntimeException re) {
				// rollback au besoin...
				if (filesCreated != null) {
					for (File f : filesCreated) {
						f.delete();
					}
				} else {
					log.warn("Rollback création fichier n'a pas pu être réalisée");
				}

				throw(re);
			}
		}
	}
	
	@Override
	public void updateMultipleObjectsManager(List<ProdDerive> prodDerives,
			List<ProdDerive> baseProdDerives,
							List<AnnotationValeur> listAnnoToCreateOrUpdate, 
									List<AnnotationValeur> listAnnoToDelete,
									List<NonConformite> ncfsTrait,
									List<NonConformite> ncfsCess,
												Utilisateur utilisateur, 
												String baseDir) {
		
		List<File> filesCreated = new ArrayList<File>();
		List<File> filesToDelete = new ArrayList<File>();
		
		for (int i = 0; i < prodDerives.size(); i++) {
			ProdDerive derive = prodDerives.get(i);
			try {
				updateObjectManager(derive, 
					derive.getBanque(), 
					derive.getProdType(), 
					derive.getObjetStatut(), 
					derive.getCollaborateur(), 
					derive.getEmplacement(), 
					derive.getVolumeUnite(), 
					derive.getConcUnite(), 
					derive.getQuantiteUnite(), 
					derive.getModePrepaDerive(),
					derive.getProdQualite(), 
					derive.getTransformation(), 
					null, null, filesCreated, filesToDelete,
					derive.getReservation(), 
					utilisateur, 
					true, null, baseDir);
				
				// enregistrement de la conformité
				objetNonConformeManager
					.createUpdateOrRemoveListObjectManager(
								derive, ncfsTrait, "Traitement");
				objetNonConformeManager
				.createUpdateOrRemoveListObjectManager(
							derive, ncfsCess, "Cession");
				
			} catch (RuntimeException e) {
				if (e instanceof TKException) {
					((TKException) e).setEntiteObjetException(
							"ProdDerive");
					((TKException) e).setIdentificationObjetException(
							derive.getCode());
				}
				throw e;
			}
		}
		
		try {
			// suppr les annotations pour tous les prelevements
			annotationValeurManager
					.removeAnnotationValeurListManager(listAnnoToDelete, filesToDelete);
			
			if (listAnnoToCreateOrUpdate != null) {
				// traite en premier et retire les annotations 
				// création de fichiers pour 
				// enregistrement en batch 
				List<AnnotationValeur> fileVals = new ArrayList<AnnotationValeur>();
				for (AnnotationValeur val : listAnnoToCreateOrUpdate) {
					if (val.getFichier() != null && val.getStream() != null) {
						annotationValeurManager
							.createFileBatchForTKObjectsManager(baseProdDerives, 
									val.getFichier(), val.getStream(), 
									val.getChampAnnotation(), val.getBanque(), 
									utilisateur, baseDir, filesCreated);
						fileVals.add(val);
					}
				}
				listAnnoToCreateOrUpdate.removeAll(fileVals);
				
				// update les annotations, null operation pour
				// laisser la possibilité création/modification au sein 
				// de la liste
				annotationValeurManager
					.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, 
						null, utilisateur, null, baseDir, filesCreated, filesToDelete);
			}
			
			for (File f : filesToDelete) {
				f.delete();
			}
		} catch (RuntimeException e) {
			
			for (File f : filesCreated) {
				f.delete();
			}
	
			throw e;
		}	
	}
	
	@Override
	public Boolean isUsedObjectManager(ProdDerive derive) {
		return (transformationManager
					.findByParentManager(derive).size() > 0);
	}
	
	@Override
	public Boolean isCessedObjectManager(ProdDerive derive) {
		return (cederObjetManager
					.findByObjetManager(derive).size() > 0);
	}

	@Override
	public void removeObjectCascadeManager(Transformation transformation, 
				String comments, Utilisateur user, List<File> filesToDelete) {
//		if (isUsedObjectManager(transformation)) {
//			log.warn("Objet utilisé lors de la suppression de l'objet " 
//					+ "Transformation : " + transformation.toString());
//			throw new ObjectUsedException("Transformation", "suppression");
//		} else {
		
		// suppression des dérivés en cascade
		Iterator<ProdDerive> derivesIt = 
			findByTransformationManager(transformation).iterator();
		
		while (derivesIt.hasNext()) {
			removeObjectCascadeManager(derivesIt.next(), 
												comments, user, filesToDelete);
		}
			
		transformationManager
			.removeObjectManager(transformation, comments, user);
			
		log.info("Suppression de l'objet Transformation : " 
					+ transformation.toString());
	}
	
	@Override
	public void removeObjectCascadeManager(ProdDerive derive, String comments,
			Utilisateur user, List<File> filesToDelete) {
		
		if (!isCessedObjectManager(derive)) {
			Iterator<Transformation> transfIt =
				transformationManager.findByParentManager(derive)
																.iterator();
			while (transfIt.hasNext()) {
				removeObjectCascadeManager(transfIt.next(), comments, user, filesToDelete);
			}
			
			removeObjectManager(derive, comments, user, filesToDelete);
		} else {
			throw new ObjectUsedException("derive.cascade.isCessed", false);
		}
	}

	@Override
	public void removeObjectManager(ProdDerive prodDerive, String comments,
											Utilisateur user, List<File> filesToDelete) {
		
		if (prodDerive != null) {
			if (!isUsedObjectManager(prodDerive) 
					&& !isCessedObjectManager(prodDerive)) {
				
				Iterator<Retour> retoursIt = retourManager
					.getRetoursForObjectManager(prodDerive).iterator();
				while (retoursIt.hasNext()) {
					retourManager.removeObjectManager(retoursIt.next());
				}
				
				// Supprime non conformites associees
				CreateOrUpdateUtilities
					.removeAssociateNonConformites(prodDerive, 
												objetNonConformeManager);
				
				prodDeriveDao.removeObject(prodDerive.getProdDeriveId());
				log.info("Suppression de l'objet ProdDerive : " 
						+ prodDerive.toString());
				
				//Supprime operations associees
				CreateOrUpdateUtilities.
					removeAssociateOperations(prodDerive, operationManager, 
													comments, user);
				
				//Supprime importations associes
				CreateOrUpdateUtilities.
					removeAssociateImportations(prodDerive, 
												importHistoriqueManager);
				
				//Supprime annotations associes
				annotationValeurManager
					.removeAnnotationValeurListManager(annotationValeurManager
							.findByObjectManager(prodDerive), filesToDelete);
			} else {
				if (!isCessedObjectManager(prodDerive)) {
					log.warn("Objet utilisé lors de la suppression de l'objet " 
						+ "ProdDerive : " + prodDerive.toString());
					throw new ObjectUsedException("derive.deletion." 
													+ "isUsedCascade", true);
				} else {
					log.warn("Objet cédé lors de la suppression de l'objet " 
							+ "ProdDerive : " + prodDerive.toString());
					throw new ObjectUsedException("derive.deletion."
												+ "isUsedNonCascade", false);
				}
			}
		}
	}
	
	@Override
	public void createDeriveListWithAnnotsManager(List<ProdDerive> listDerives,
				Banque banque, Transformation transfo, Utilisateur utilisateur,
									List<AnnotationValeur> listAnnotations, 
									String baseDir, 
									List<NonConformite> noconfsTrait, 
									List<NonConformite> noconfsCess) {
		
		ObjetStatut nonstocke = objetStatutDao
								.findByStatut("NON STOCKE").get(0);
		
		Boolean cfTrait = null;
		if (noconfsTrait != null && !noconfsTrait.isEmpty()) {
			cfTrait = false;
		}
		Boolean cfCess = null;
		if (noconfsCess != null && !noconfsCess.isEmpty()) {
			cfCess = false;
		}
		
		List<File> filesCreated = new ArrayList<File>();
		
		ProdDerive newDerive = null;
		// création de l'objet
		try {
			for (int i = 0; i < listDerives.size(); i++) {
				newDerive = listDerives.get(i);
				
				// retour etat transient au besoin
				// ex validation erreur
				if (newDerive.getProdDeriveId() != null) {
					newDerive.setProdDeriveId(null);
				}
				if (newDerive.getConformeTraitement() == null) {
					newDerive.setConformeTraitement(cfTrait);
				}
				if (newDerive.getConformeCession() == null) {
					newDerive.setConformeCession(cfCess);
				}
				
				createObjectManager(
						newDerive, 
						banque, 
						newDerive.getProdType(), 
						nonstocke, 
						newDerive.getCollaborateur(), 
						null, 
						newDerive.getVolumeUnite(), 
						newDerive.getConcUnite(), 
						newDerive.getQuantiteUnite(), 
						newDerive.getModePrepaDerive(),
						newDerive.getProdQualite(), 
						transfo, 
						listAnnotations, filesCreated,
						null, 
						utilisateur, 
						true, baseDir, false);
				
				objetNonConformeManager.createUpdateOrRemoveListObjectManager(
						newDerive, noconfsTrait, "Traitement");
				objetNonConformeManager.createUpdateOrRemoveListObjectManager(
						newDerive, noconfsCess, "Cession");
			}
		} catch (RuntimeException e) {
			if (filesCreated != null) {
				for (File f : filesCreated) {
					// fichierManager.removeObjectManager(fichier);
					f.delete();
				}
			} 
			throw new DeriveBatchSaveException(newDerive,e);
		}
	}
	
	@Override
	public List<ProdDerive> findByIdsInListManager(List<Integer> ids) {
		if (ids != null && ids.size() > 0) {
			return prodDeriveDao.findByIdInList(ids);
		} else {
			return new ArrayList<ProdDerive>();
		}
	}
	
//	/**
//	 * Realise une deep copy de la liste des AnnotationValeur. Permet
//	 * de conserver l'etat Transient (no id) pour un enregistrement 
//	 * multiple de même valeurs assignées à différents objets.
//	 * @param list
//	 * @return copy
//	 */
//	private List<AnnotationValeur> deepCopyList(List<AnnotationValeur> list) {
//		List<AnnotationValeur> copy = new ArrayList<AnnotationValeur>();
//		for (int i = 0; i < list.size(); i++) {
//			copy.add(list.get(i).clone());
//		}
//		return copy;
//	}

	@Override
	public List<ProdDerive> findByParentManager(TKAnnotableObject parent, 
														boolean recursive) {
		List<ProdDerive> coll = new ArrayList<ProdDerive>();
		List<ProdDerive> derives = new ArrayList<ProdDerive>();
		if (parent != null) {
			Integer objetId = parent.listableObjectId();
			Entite entite = entiteDao.findByNom(parent.entiteNom()).get(0);
			
			if (objetId != null && entite != null) {
				derives = prodDeriveDao.findByParent(objetId, entite);
				coll.addAll(derives);
				if (recursive) {
					findRecursiveDerivesManager(derives, coll);
				}
			}
		} 
		return coll;
	}
	
	@Override
	public void findRecursiveDerivesManager(List<ProdDerive> drvs, 
												List<ProdDerive> coll) {
		Entite derEnt = entiteDao.findByNom("ProdDerive").get(0);
		List<ProdDerive> derives;
		for (int i = 0; i < drvs.size(); i++) {
			derives = prodDeriveDao
				.findByParent(drvs.get(i).getProdDeriveId(), derEnt);
			coll.addAll(derives);
			
			findRecursiveDerivesManager(derives, coll);
		}
	}

	@Override
	public void switchBanqueCascadeManager(ProdDerive derive, Banque bank, 
									boolean doValidation, Utilisateur u, 
									List<File> filesToDelete) {
		if (bank != null && derive != null
				&& !bank.equals(derive.getBanque())) {
			
			if (doValidation) {
				List<CederObjet> ceds = cederObjetManager.findByObjetManager(derive);
				for (CederObjet cederObjet : ceds) {
					// cross plateforme switchBanqueException
					if (!cederObjet.getCession().getBanque()
							.getPlateforme().equals(bank.getPlateforme())) {
						TKException te = new TKException();
						te.setMessage("derive.switchBanque.isCessed");
						te.setIdentificationObjetException(derive.getCode());
						te.setEntiteObjetException(derive.entiteNom());
						throw te;
					}
				}
				Set<Banque> banks = conteneurManager
				.getBanquesManager(conteneurManager
					.findFromEmplacementManager(derive.getEmplacement()));
				if (!banks.isEmpty() && !banks.contains(bank)) {
					TKException te = new TKException();
					te.setMessage("derive." 
							+ "switchBanque.badBanqueStockage");
					te.setIdentificationObjetException(derive.getCode());
					te.setEntiteObjetException(derive.entiteNom());
					throw te;
				}
			}

			Iterator<ProdDerive> derivesIt = 
				getProdDerivesManager(derive).iterator();
			while (derivesIt.hasNext()) {
				switchBanqueCascadeManager(derivesIt.next(), bank, 
										doValidation, u, filesToDelete);
			}

			derive.setBanque(bank);
			if (findDoublonManager(derive)) {
				throw new DoublonFoundException("ProdDerive", "switchBanque", derive.getCode(), null);
			}
			derive = prodDeriveDao.mergeObject(derive);

			// annotations
			annotationValeurManager.switchBanqueManager(derive, bank, 
					filesToDelete);

			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, u,
				operationTypeDao.findByNom("ChangeCollection").get(0), derive);
		}
	}
	
	@Override
	public void updateObjectWithNonConformitesManager(ProdDerive prodDerive, 
			Banque banque,
			ProdType type, ObjetStatut statut, Collaborateur collab,
			Emplacement emplacement, Unite volumeUnite, Unite concUnite,
			Unite quantiteUnite, ModePrepaDerive modePrepaDerive,
			ProdQualite qualite, Transformation transfo,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<AnnotationValeur> listAnnoToDelete, 
			Reservation reservation, Utilisateur utilisateur, 
			boolean doValidation,
			List<OperationType> operations, String baseDir, 
			List<NonConformite> noconfsTraitement, 
			List<NonConformite> noconfsCession) {
		
		List<File> filesCreated = new ArrayList<File>();
		List<File> filesToDelete = new ArrayList<File>();
		
		try {
			if (noconfsTraitement != null && !noconfsTraitement.isEmpty()) {
				prodDerive.setConformeTraitement(false);
			}
			
			if (noconfsCession != null && !noconfsCession.isEmpty()) {
				prodDerive.setConformeCession(false);
			}
			
			updateObjectManager(prodDerive, banque, type, statut, collab, emplacement, 
					volumeUnite, concUnite, quantiteUnite, modePrepaDerive, qualite, 
					transfo, listAnnoToCreateOrUpdate, listAnnoToDelete, 
					filesCreated, filesToDelete, reservation, 
					utilisateur, doValidation, operations, baseDir);
			
			objetNonConformeManager.createUpdateOrRemoveListObjectManager(
					prodDerive, noconfsTraitement, "Traitement");
			objetNonConformeManager.createUpdateOrRemoveListObjectManager(
					prodDerive, noconfsCession, "Cession");
			
			for (File f : filesToDelete) {
				f.delete();
			}
		} catch (RuntimeException re) {
			for (File f : filesCreated) {
				f.delete();
			}
			throw(re);
		}
	}

//	@Override
//	public String extractValueForChampManager(
//			ProdDerive prodDerive, Champ champ) {
//		if (prodDerive != null && champ != null) {
//			String value = null;
//			if (champ.getChampEntite() != null) {
//				if (champ.getChampEntite().getEntite()
//						.getNom().equals("Patient")) {
//					// on récupère le patient
//					Patient patient = getPatientParentManager(prodDerive);
//					value = champManager.getValueForObjectManager(
//							champ, patient);
//				} else if (champ.getChampEntite().getEntite()
//						.getNom().equals("Maladie")) {
//					// on récupère la maladie
//					Prelevement prlvt = getPrelevementParent(prodDerive);
//					Maladie maladie = null;
//					if (prlvt != null) {
//						maladie = prlvt.getMaladie();
//					}
//					value = champManager.getValueForObjectManager(
//							champ, maladie);
//				} else if (champ.getChampEntite().getEntite()
//						.getNom().equals("Prelevement")) {
//					// on récupère le prelevement
//					Prelevement prlvt = getPrelevementParent(prodDerive);
//					value = champManager.getValueForObjectManager(
//							champ, prlvt);
//				} else if (champ.getChampEntite().getEntite()
//						.getNom().equals("Echantillon")) {
//					// on récupère l'échantillon
//					Object parent = findParent(prodDerive);
//					if (parent.getClass()
//							.getSimpleName().equals("Echantillon")) {
//						value = echantillonManager
//							.extractValueForChampManager(
//									(Echantillon) parent, champ);
//					}
//				} else if (champ.getChampEntite().getEntite()
//						.getNom().equals("ProdDerive")) {
//					// si c'est l'emplacement à extraire
//					if (champ.getChampEntite()
//							.getNom().equals("EmplacementId")) {
//						value = getEmplacementAdrlManager(prodDerive);
//					} else {
//						value = champManager.getValueForObjectManager(
//							champ, prodDerive);
//					}
//				}
//			}
//			return value;
//		} else {
//			return null;
//		}
//	}	
	
	/**
	 * Vérifie que l'emplacement n'est pas occupé par un autre objet. Validation permettant 
	 * d'éviter que la contrainte d'unicité sur emplacement_id soit contournée par la 
	 * présence d'un échantillon ou d'un autre dérivé (si création nouveaux emplacements)
	 * @param emplacement
	 * @param dérivé
	 * @return true si pas TKException lancée
	 * @version 2.0.13.2
	 */
	private boolean checkEmplacementOccupied(Emplacement empl, 
											ProdDerive derive) {
		Emplacement emplacement = empl;
		if (empl.getEmplacementId() != null) {
			emplacement = emplacementDao.mergeObject(empl);
		}
		boolean throwError = false;
		List<TKStockableObject> objs = emplacementManager
				.findObjByEmplacementManager(emplacement, "Echantillon");
		// echantillon if not empty -> error
		throwError = !objs.isEmpty();
		
		if (!throwError) { // test emplacement occupé par derive différent
			objs = emplacementManager
					.findObjByEmplacementManager(emplacement, "ProdDerive");
			throwError = !objs.isEmpty() && !objs.get(0).listableObjectId().equals(derive.getProdDeriveId());
		}
		
		if (throwError) {	
			TKException tke = new TKException();
			tke.setMessage("error.emplacement.notEmpty");
			tke.setIdentificationObjetException(derive.getCode());
			tke.setEntiteObjetException("ProdDerive");
			tke.setTkObj(derive);
			throw tke;
		}
		return true;
	}

	@Override
	public Long findCountCreatedByCollaborateurManager(Collaborateur colla){
		if (colla != null){
			return prodDeriveDao.findCountCreatedByCollaborateur(colla).get(0);
		}
		else{
			return new Long(0);
		}
	}

	@Override
	public Long findCountByOperateurManager(Collaborateur colla){
		if (colla != null){
			return prodDeriveDao.findCountByOperateur(colla).get(0);
		}
		else{
			return new Long(0);
		}
	}
	
	@Override
	public void createProdDerivesManager(List<ProdDerive> derives, 
			Banque b, Utilisateur u, 
			TKAnnotableObject parent, 
			Hashtable<ProdDerive, List<AnnotationValeur>> valeurs, 
			Hashtable<ProdDerive, Emplacement> empls, 
			Float qteTransfo, Unite uniteTransfo,
			Calendar dateS, 
			String retourObservations, 
			String baseDir,
			boolean isImport, 
			Hashtable<ProdDerive, List<NonConformite>> noconfsTrait, 
			Hashtable<ProdDerive, List<NonConformite>> noconfsCess) {
		
		Transformation transfo = null;
		Entite entiteParent = null;
		ObjetStatut statutParent = null;
		Emplacement oldEmplacement = null;
		Retour transfoRetour = null;
		
		ProdDerive key = null;
		
		List<File> filesCreated = new ArrayList<File>();
	
		try {
			if (parent != null) {
				transfo = new Transformation();
				transfo.setQuantite(qteTransfo);
				transfo.setQuantiteUnite(uniteTransfo);
				entiteParent = entiteManager
						.findByNomManager(parent.getClass().getSimpleName())
						.get(0);
				transfo.setEntite(entiteParent);
				transfo.setObjetId(parent.listableObjectId());
				
				// nouveau statut parent (null si prelevement)
				statutParent = prepareUpdatedParentStatut(parent, transfo);
				
				// stockable object -> EPUISEMENT et Retour
				if (parent instanceof TKStockableObject) {	
					
					// Exception si statut incompatible avec la creation de derives
					if (((TKStockableObject) parent).getObjetStatut().getStatut()
																.equals("EPUISE")) {
						throw new ObjectStatutException(parent.entiteNom(), "transformation");
					}
					
					// emplacement actuel parent
					List<Emplacement> emps = emplacementDao
							.findByObjetIdEntite(parent.listableObjectId(),
						entiteManager.findByNomManager(parent.getClass()
												.getSimpleName()).get(0));
					if (!emps.isEmpty()) {
						oldEmplacement = emps.get(0);
					}
					
					// validation retour si epuisement
					// ou si retour incomplete
					if (dateS == null) {
						dateS = Calendar.getInstance();
					}
					
					List<Integer> id = new ArrayList<Integer>();
					id.add(parent.listableObjectId());
					List<Retour> rets = retourManager
							.findByObjectDateRetourEmptyManager(id, entiteParent);
					if (!rets.isEmpty()) {
						transfoRetour = rets.get(0);
					} 
					
					if (statutParent.getStatut().equals("EPUISE") 
							&& !((TKStockableObject) parent)
							.getObjetStatut().getStatut().equals("EPUISE")) {	
						if (transfoRetour == null) { // creation retour epuisement
							transfoRetour = new Retour();
							transfoRetour.setDateSortie(dateS);
							// température arbitraire
							transfoRetour.setTempMoyenne(new Float(20.0));
							transfoRetour.setObjetId(parent.listableObjectId());
							transfoRetour.setEntite(entiteParent);
							transfoRetour.setObjetStatut(((TKStockableObject) parent)
																.getObjetStatut());
						} 
						// else {
						//	transfoRetour.setObjetStatut(null);
						//}
						if (transfoRetour.getObservations() == null) {
							transfoRetour.setObservations(retourObservations);
						}

					} else { // complete le retour existant
						if (transfoRetour != null) {
							transfoRetour.setDateRetour(dateS);
							statutParent = transfoRetour.getObjetStatut();
							transfoRetour.setObjetStatut(null);	
						}
					}
					
					// if (transfoRetour != null) {
					//	BeanValidator.validateObject(transfoRetour, 
					//		new Validator[]{retourValidator});
					//}
				}
			}
			
			createDeriveListWithAnnotsManager(derives, 
					b, transfo, u, null, baseDir, null, null);
			
			if (valeurs != null) {
				List<AnnotationValeur> vals = new ArrayList<AnnotationValeur>();
				Iterator<ProdDerive> itDer = valeurs.keySet().iterator();
				while (itDer.hasNext()) {
					key = itDer.next();
					vals.addAll(valeurs.get(key));
					annotationValeurManager.createAnnotationValeurListManager(
								vals, key, u, null, baseDir, filesCreated, null);
					vals.clear();		
				}
			}
			
			if (noconfsTrait != null) {
				List<NonConformite> ncfs = new ArrayList<NonConformite>();
				Iterator<ProdDerive> itDer = noconfsTrait.keySet().iterator();
				while (itDer.hasNext()) {
					key = itDer.next();
					ncfs.addAll(noconfsTrait.get(key));
					if (ncfs != null && !ncfs.isEmpty()) {
						key.setConformeTraitement(false);
						prodDeriveDao.updateObject(key);
					}
					objetNonConformeManager.createUpdateOrRemoveListObjectManager(
							key, ncfs, "Traitement");
					ncfs.clear();
				}
			}
			if (noconfsCess != null) {
				List<NonConformite> ncfs = new ArrayList<NonConformite>();
				Iterator<ProdDerive> itDer = noconfsCess.keySet().iterator();
				while (itDer.hasNext()) {
					key = itDer.next();
					ncfs.addAll(noconfsCess.get(key));
					if (ncfs != null && !ncfs.isEmpty()) {
						key.setConformeCession(false);
						prodDeriveDao.updateObject(key);
					}
					objetNonConformeManager.createUpdateOrRemoveListObjectManager(
							key, ncfs, "Cession");
					ncfs.clear();
				}
				
			}
			
			if (empls != null && !empls.isEmpty()) {
				List<Emplacement> emplsFinaux = new ArrayList<Emplacement>();
				Iterator<ProdDerive> it = empls.keySet().iterator();
				List<Integer> derivesId = new ArrayList<Integer>();
				ObjetStatut stocke = objetStatutManager
								.findByStatutLikeManager("STOCKE", true).get(0);
				OperationType stockage = operationTypeDao
												.findByNom("Stockage").get(0);
				Entite deriveEntite = entiteDao.findByNom("ProdDerive").get(0);
				while (it.hasNext()) {
					key = it.next();
					Emplacement empl = empls.get(key);
					empl.setEntite(deriveEntite);
					empl.setObjetId(key.getProdDeriveId());
					empl.setVide(false);
					emplsFinaux.add(empl);
				}
				
				// enregistrement des emplacements
				emplacementManager.saveMultiEmplacementsManager(emplsFinaux);	
				it = empls.keySet().iterator();
				while (it.hasNext()) {
					ProdDerive deriveToUpdate = it.next();	
					derivesId.add(deriveToUpdate.getProdDeriveId());
					deriveToUpdate.setEmplacement(empls.get(deriveToUpdate));
					deriveToUpdate.setObjetStatut(stocke);
					prodDeriveDao.updateObject(deriveToUpdate);
				}	
				
				// stockage operation
				operationManager.batchSaveManager(derivesId, u, stockage, Calendar.getInstance(), 
						entiteDao.findByNom("ProdDerive").get(0));
			}
					
			if (parent != null) {
				// creation du retour
				if (transfoRetour != null) {
					// passage temporaire au statut NON STOCKE 
					// afin que ce statut soit enregistré dans le retour correspondant 
					// à l'épuisement de l'échantillon
					// ((TKStockableObject) parent).setObjetStatut(objetStatutManager
					//			.findByStatutLikeManager("NON STOCKE", true).get(0));				
					retourManager.createOrUpdateObjectManager(transfoRetour, 
						(TKStockableObject) parent, 
						oldEmplacement, null, null, transfo,
						 null, u, 
						 transfoRetour.getRetourId() != null ? "modification" : "creation");
					// re-assigne l'object statut avant update parent
					if (transfoRetour.getObjetStatut() != null) {
						((TKStockableObject) parent)
							.setObjetStatut(transfoRetour.getObjetStatut());
					}
				}
					
				// s'il n'y a pas d'erreurs, on met à jour le parent : modif
				// de sa quantité et de son volume
				updateParent(parent, statutParent, u);
			}
		} catch (RuntimeException e) {
			if (filesCreated != null) {
				for (File f : filesCreated) {
					f.delete();
				}
			} 
			if (e instanceof DeriveBatchSaveException) {
				throw e;
			} else {
				ProdDerive deriveInError = null;
				if (key != null) {
					deriveInError = key;
				} else {
					deriveInError = derives.get(0);
				}
				throw new DeriveBatchSaveException(deriveInError, e);
			}
		}
	}
	
	private ObjetStatut prepareUpdatedParentStatut(TKAnnotableObject parent, 
									Transformation tf) {
		
		// si la quantite max n'est pas null (le derive a une quantité)
		if (parent instanceof TKStockableObject) { // echantillon/derive
			if (((TKStockableObject) parent).getQuantite() != null) {
				// si une quantité a été saisie pour la transformation
				if (tf.getQuantite() != null) {
					// maj de la quantité du parent si restant >= 0
					Float tmp = ((TKStockableObject) parent).getQuantite() - tf.getQuantite();
					if (tmp >= 0) {
						((TKStockableObject) parent).setQuantite(tmp);
					} else {
						throw new TransformationQuantiteOverDemandException(tf.getQuantite(), 
								((TKStockableObject) parent).getQuantite());
					}
				} // else {
				//	((TKStockableObject) parent).setQuantite(qteMax);
				//}
				
				// maj du volume si derive
				if (parent instanceof ProdDerive) {
					ProdDerive deriveParent = (ProdDerive) parent;
					if (deriveParent.getQuantiteInit() != null 
						&& !deriveParent.getQuantiteInit().equals(new Float(0.0))) {
						if (deriveParent.getVolumeInit() != null) {
							Float rapport = deriveParent
								.getQuantite() / deriveParent.getQuantiteInit();
							Float newVol = deriveParent.getVolumeInit() * rapport;
							deriveParent.setVolume(newVol);
						}
					} else {
						deriveParent.setVolume(deriveParent.getQuantite());
					}
				}
			}
			
			ObjetStatut statut = objetStatutManager
					.findStatutForTKStockableObject((TKStockableObject) parent);
			
			return statut;
		} else { // prelevement
			// si la quantite max n'est pas null (le prlvt a une quantité)
			if (((Prelevement) parent).getQuantite() != null) {
				// si une quantité a été saisie pour la transformation
				if (tf.getQuantite() != null) {
					// maj de la quantité du prlvt
					Float tmp = ((Prelevement) parent).getQuantite() - tf.getQuantite();
					if (tmp >= 0) {
						((Prelevement) parent).setQuantite(tmp);
					} else {
						throw new TransformationQuantiteOverDemandException(tf.getQuantite(), 
								((Prelevement) parent).getQuantite());
					}
				} // else {
				//	((Prelevement) parent).setQuantite(((Prelevement) parent).getQuantite());
				//}
			}
			return null;
		}
	}
	
	private void updateParent(TKAnnotableObject parent, ObjetStatut statut, 
															Utilisateur u) {
		// echantillon ou derive
		if (parent instanceof TKStockableObject) {
			ObjetStatut oldStatut = ((TKStockableObject) parent).getObjetStatut();
			((TKStockableObject) parent).setObjetStatut(statut);
			List<OperationType> ops = new ArrayList<OperationType>();
			if (statut.getStatut().equals("EPUISE")) {
				Emplacement empl = null;
				List<Emplacement> emps = emplacementDao
					.findByObjetIdEntite(parent.listableObjectId(),
						entiteManager.findByNomManager(parent.entiteNom()).get(0));
				if (!emps.isEmpty()) {
					empl = emps.get(0);
					empl.setEntite(null);
					empl.setObjetId(null);
					empl.setVide(true);
					((TKStockableObject) parent).setEmplacement(null);
					
					emplacementManager.updateObjectManager(empl, empl.getTerminale(), null);
				}
				
				if (oldStatut.getStatut().equals("STOCKE")) {
					ops = operationTypeDao
						.findByNom("Destockage");
				}
			}
			
			if (parent instanceof Echantillon) {
				echantillonDao.updateObject((Echantillon) parent);
			} else {
				prodDeriveDao.updateObject((ProdDerive) parent);
			}
			if (!ops.isEmpty()) { // destockage
				Operation o = new Operation();
				o.setDate(Calendar.getInstance());
				operationManager.createObjectManager(o, u, ops.get(0), parent);
			}
		} else { // Prelevement
			prelevementDao.updateObject((Prelevement) parent);
		}
	}
	
	@Override
	public void removeListFromIdsManager(List<Integer> ids, 
									String comment, Utilisateur u) {
		if (ids != null) {
			List<File> filesToDelete = new ArrayList<File>();
			ProdDerive p;
			for (Integer id : ids) {
				p = findByIdManager(id);
				if (p != null) {
					removeObjectCascadeManager(p, comment, u, filesToDelete);
				}
			}
			
			if (filesToDelete != null) {
				for (File f : filesToDelete) {
					f.delete();
				}
			}
		}
	}

	@Override
	public List<ProdDerive> findByCodeInPlateformeManager(String code, Plateforme pf) {
		return prodDeriveDao.findByCodeInPlateforme(code, pf);
	}
}
