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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.collection.PersistentSet;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditMilieuDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConditTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.ConsentTypeDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.NatureDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementDao;
import fr.aphp.tumorotek.dao.coeur.prelevement.PrelevementTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.contexte.CollaborateurDao;
import fr.aphp.tumorotek.dao.contexte.ServiceDao;
import fr.aphp.tumorotek.dao.contexte.TransporteurDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.LaboInterValidator;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementValidator;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.Transporteur;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * Implémentation du manager du bean de domaine Prelevement. Classe créée le
 * 13/10/09.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0.13
 * 
 */
public class PrelevementManagerImpl implements PrelevementManager {

	private Log log = LogFactory.getLog(PrelevementManager.class);

	/* Beans injectes par Spring */
	private PrelevementDao prelevementDao;
	private PatientManager patientManager;
	private MaladieManager maladieManager;
	private BanqueDao banqueDao;
	private NatureDao natureDao;
	private MaladieDao maladieDao;
	private ConsentTypeDao consentTypeDao;
	private CollaborateurDao collaborateurDao;
	private ServiceDao serviceDao;
	private PrelevementTypeDao prelevementTypeDao;
	private ConditTypeDao conditTypeDao;
	private ConditMilieuDao conditMilieuDao;
	private TransporteurDao transporteurDao;
	private UniteDao uniteDao;
	private PrelevementValidator prelevementValidator;
	private OperationTypeDao operationTypeDao;
	private OperationManager operationManager;
	private EntityManagerFactory entityManagerFactory;
	private EntiteDao entiteDao;
	private TransformationManager transformationManager;
	private TransformationDao transformationDao;
	private LaboInterManager laboInterManager;
	private LaboInterValidator laboInterValidator;
	private EchantillonDao echantillonDao;
	private EchantillonManager echantillonManager;
	private AnnotationValeurManager annotationValeurManager;
	private ImportHistoriqueManager importHistoriqueManager;
	private ProdDeriveManager prodDeriveManager;
	private CederObjetManager cederObjetManager;
	private DossierExterneDao dossierExterneDao;
	private ObjetNonConformeManager objetNonConformeManager;

	public PrelevementManagerImpl() {
	}

	/* Properties setters */
	public void setPrelevementDao(PrelevementDao pDao) {
		this.prelevementDao = pDao;
	}

	public void setPatientManager(PatientManager pManager) {
		this.patientManager = pManager;
	}

	public void setMaladieManager(MaladieManager mManager) {
		this.maladieManager = mManager;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setNatureDao(NatureDao nDao) {
		this.natureDao = nDao;
	}

	public void setMaladieDao(MaladieDao mDao) {
		this.maladieDao = mDao;
	}

	public void setPatientDao(PatientDao pDao) {
	}

	public void setConsentTypeDao(ConsentTypeDao ctDao) {
		this.consentTypeDao = ctDao;
	}

	public void setCollaborateurDao(CollaborateurDao cDao) {
		this.collaborateurDao = cDao;
	}

	public void setServiceDao(ServiceDao sDao) {
		this.serviceDao = sDao;
	}

	public void setPrelevementTypeDao(PrelevementTypeDao ptDao) {
		this.prelevementTypeDao = ptDao;
	}

	public void setConditTypeDao(ConditTypeDao ctDao) {
		this.conditTypeDao = ctDao;
	}

	public void setConditMilieuDao(ConditMilieuDao cmDao) {
		this.conditMilieuDao = cmDao;
	}

	public void setTransporteurDao(TransporteurDao tDao) {
		this.transporteurDao = tDao;
	}

	public void setUniteDao(UniteDao uDao) {
		this.uniteDao = uDao;
	}

	public void setPrelevementValidator(PrelevementValidator pValidator) {
		this.prelevementValidator = pValidator;
	}

	public void setOperationManager(OperationManager oManager) {
		this.operationManager = oManager;
	}

	public void setOperationTypeDao(OperationTypeDao otDao) {
		this.operationTypeDao = otDao;
	}

	public void setEntityManagerFactory(EntityManagerFactory emFactory) {
		this.entityManagerFactory = emFactory;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	public void setTransformationManager(TransformationManager tManager) {
		this.transformationManager = tManager;
	}

	public void setTransformationDao(TransformationDao tDao) {
		this.transformationDao = tDao;
	}

	public void setLaboInterManager(LaboInterManager lManager) {
		this.laboInterManager = lManager;
	}

	public void setLaboInterValidator(LaboInterValidator lValidator) {
		this.laboInterValidator = lValidator;
	}

	public void setEchantillonDao(EchantillonDao eDao) {
		this.echantillonDao = eDao;
	}

	public void setEchantillonManager(EchantillonManager eManager) {
		this.echantillonManager = eManager;
	}

	public void setAnnotationValeurManager(AnnotationValeurManager aManager) {
		this.annotationValeurManager = aManager;
	}

	public void setImportHistoriqueManager(ImportHistoriqueManager iManager) {
		this.importHistoriqueManager = iManager;
	}

	public void setProdDeriveManager(ProdDeriveManager pManager) {
		this.prodDeriveManager = pManager;
	}

	public void setCederObjetManager(CederObjetManager cManager) {
		this.cederObjetManager = cManager;
	}

	public void setDossierExterneDao(DossierExterneDao dDao) {
		this.dossierExterneDao = dDao;
	}

	public void setObjetNonConformeManager(ObjetNonConformeManager oM) {
		this.objetNonConformeManager = oM;
	}

	@Override
	public void createObjectManager(Prelevement prelevement, Banque banque,
			Nature nature, Maladie maladie, ConsentType consentType,
			Collaborateur preleveur, Service servicePreleveur,
			PrelevementType prelevementType, ConditType conditType,
			ConditMilieu conditMilieu, Transporteur transporteur,
			Collaborateur operateur, Unite quantiteUnite,
			List<LaboInter> laboInters,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<File> filesCreated,
			Utilisateur utilisateur, boolean doValidation, String baseDir,
			boolean isImport) {

		// flag qui declencheront un revert des ids si le prelevement
		// la maladie et le patient sont créés conjointement avec erreur
		boolean revertMaladie = false;
		boolean revertPatient = false;
		if (maladie != null) {
			revertMaladie = (maladie.getMaladieId() == null);
			if (revertMaladie) {
				revertPatient = (maladie.getPatient().getPatientId() == null);
			}
		}

		try {
			// Verifie required Objects associes et validation
			checkRequiredObjectsAndValidate(prelevement, banque, nature,
					consentType, maladie, laboInters, "creation", utilisateur,
					doValidation, baseDir);

			// Doublon
			if (!findDoublonManager(prelevement)) {
				mergeNonRequiredObjects(prelevement, maladie, preleveur,
						servicePreleveur, prelevementType, conditType,
						conditMilieu, transporteur, operateur, quantiteUnite);

				prelevementDao.createObject(prelevement);
				log.info("Enregistrement objet Prelevement "
						+ prelevement.toString());
				// Enregistrement de l'operation associee
				Operation creationOp = new Operation();
				creationOp.setDate(Utils.getCurrentSystemCalendar());
				operationManager.createObjectManager(creationOp, utilisateur,
						operationTypeDao.findByNom("Creation").get(0),
						prelevement);

				updateLaboInters(laboInters, prelevement);

				// Annotations
				// update les annotations, null operation pour
				// laisser la possibilité création/modification au sein
				// de la liste
				if (listAnnoToCreateOrUpdate != null) {
					annotationValeurManager
					.createAnnotationValeurListManager(listAnnoToCreateOrUpdate, 
						prelevement, utilisateur, null, baseDir, 
						filesCreated, null);
				}

			} else {
				log.warn("Doublon lors creation objet Prelevement "
						+ prelevement.toString());
				throw new DoublonFoundException("Prelevement", "creation", prelevement.getCode(), null);
			}
		} catch (RuntimeException re) {
			log.error(re);
			if (filesCreated != null) {
				for (File f : filesCreated) {
					f.delete();
				}
			} else {
				log.warn("Rollback création fichier n'a pas pu être réalisée");
			}
			
			if (!isImport) {
				prelevement.setPrelevementId(null);
				if (prelevement.getLaboInters() != null) {
					Iterator<LaboInter> it = prelevement.getLaboInters()
							.iterator();
					while (it.hasNext()) {
						it.next().setLaboInterId(null);
					}
				}
				if (revertMaladie) {
					maladie.setMaladieId(null);
				}
				if (revertPatient) {
					maladie.getPatient().setPatientId(null);
				}
			}
			throw re;
		}
	}

	/**
	 * Enregistre les labos ou les modifications portant sur eux. La validation
	 * a été faite dans la validation du prelevement et est donc inutile dans
	 * cette methode.
	 * 
	 * @param laboInters
	 */
	private void updateLaboInters(List<LaboInter> laboInters,
			Prelevement prelevement) {
		// prelevement.setLaboInters(new HashSet<LaboInter>());
		if (laboInters != null) {
			LaboInter labo;
			for (int i = 0; i < laboInters.size(); i++) {
				labo = laboInters.get(i);
				if (labo.getLaboInterId() == null) {
					laboInterManager.createObjectManager(labo, prelevement,
							labo.getService(), labo.getCollaborateur(),
							labo.getTransporteur(), false);
				} else {
					laboInterManager.updateObjectManager(labo, prelevement,
							labo.getService(), labo.getCollaborateur(),
							labo.getTransporteur(), false);
				}
			}
			prelevement.setLaboInters(new HashSet<LaboInter>(laboInters));
		}
	}

	@Override
	public void updateObjectManager(Prelevement prelevement, Banque banque,
			Nature nature, Maladie maladie, ConsentType consentType,
			Collaborateur preleveur, Service servicePreleveur,
			PrelevementType prelevementType, ConditType conditType,
			ConditMilieu conditMilieu, Transporteur transporteur,
			Collaborateur operateur, Unite quantiteUnite,
			List<LaboInter> laboInters,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<AnnotationValeur> listAnnoToDelete, 
			List<File> filesCreated, List<File> filesToDelete, 
			Utilisateur utilisateur,
			Integer cascadeNonSterile, boolean doValidation, String baseDir,
			boolean multiple) {

		// Verifie required Objects associes et validation
		checkRequiredObjectsAndValidate(prelevement, banque, nature,
				consentType, maladie, laboInters, "modification", null,
				doValidation, baseDir);
		// Doublon
		if (findDoublonManager(prelevement)) {	
			log.warn("Doublon lors modification objet Prelevement "
					+ prelevement.toString());
			throw new DoublonFoundException("Prelevement", "modification", prelevement.getCode(), null);
		} else {
			try {
				mergeNonRequiredObjects(prelevement, maladie, preleveur,
						servicePreleveur, prelevementType, conditType,
						conditMilieu, transporteur, operateur, quantiteUnite);

				if (cascadeNonSterile != null) { // cascade non sterile condition
					log.info("Applique la cascade de sterilite"
							+ " depuis le prelevement " + prelevement.toString());
					if (laboInters != null // aucune modif labos
							&& laboInters.size() > 0) {
						cascadeNonSterileManager(prelevement, laboInters,
								cascadeNonSterile, true);
					} else {
						cascadeNonSterileManager(
								prelevement,
								new ArrayList<LaboInter>(
										getLaboIntersWithOrderManager(prelevement)),
								cascadeNonSterile, true);
					}
				}

				prelevementDao.updateObject(prelevement);
				log.info("Modification objet Prelevement " + prelevement.toString());
				// Enregistrement de l'operation associee
				Operation creationOp = new Operation();
				creationOp.setDate(Utils.getCurrentSystemCalendar());
				if (!multiple) {
					operationManager.createObjectManager(creationOp, utilisateur,
							operationTypeDao.findByNom("Modification").get(0),
							prelevement);
				} else {
					operationManager.createObjectManager(creationOp, utilisateur,
							operationTypeDao.findByNom("ModifMultiple").get(0),
							prelevement);
				}

				updateLaboInters(laboInters, prelevement);

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
					annotationValeurManager.createAnnotationValeurListManager(
							listAnnoToCreateOrUpdate, prelevement, utilisateur,
							null, baseDir, filesCreated, filesToDelete);
				}

				// enregistre operation associee annotation
				// si il y a eu des deletes et pas d'updates
				if ((listAnnoToCreateOrUpdate == null || listAnnoToCreateOrUpdate
						.isEmpty())
						&& (listAnnoToDelete != null && !listAnnoToDelete.isEmpty())) {
					CreateOrUpdateUtilities.createAssociateOperation(prelevement,
							operationManager,
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
	public boolean findDoublonManager(Prelevement prelevement) {
		// Banque banque = prelevement.getBanque();
		List<Prelevement> dbls = prelevementDao
			.findByCodeInPlateforme(prelevement.getCode(), 
					prelevement.getBanque() != null ? 
							prelevement.getBanque().getPlateforme() :  null);
			//	.findByCodeOrNumLaboWithBanque(prelevement.getCode(), banque);
		
		if (!dbls.isEmpty()) {
			if (prelevement.getPrelevementId() == null) {
				return true;
			} else {
				for (Prelevement d : dbls) {
					if (!d.getPrelevementId().equals(prelevement.getPrelevementId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<Prelevement> findAllObjectsManager() {
		log.debug("Recherche totalite des Prelevement");
		return prelevementDao.findAll();
	}

	@Override
	public List<Prelevement> findAfterDateConsentementManager(Date date) {
		if (date != null) {
			log.debug("Recherche des Prelevements consentis apres la date "
					+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
		}
		return prelevementDao.findByConsentDateAfterDate(date);
	}

	@Override
	public List<Prelevement> findAfterDatePrelevementManager(Date date) {
		Calendar cal = null;
		if (date != null) {
			log.debug("Recherche des Prelevements preleves apres la date "
					+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
			cal = Calendar.getInstance();
			cal.setTime(date);
		}
		return prelevementDao.findByDatePrelevementAfterDate(cal);
	}

	@Override
	public List<Prelevement> findAfterDatePrelevementWithBanqueManager(
			Date date, List<Banque> banques) {
		Calendar cal = null;
		List<Prelevement> res = new ArrayList<Prelevement>();
		if (date != null && banques != null) {
			log.debug("Recherche des Prelevements preleves apres la date "
					+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date));
			cal = Calendar.getInstance();
			cal.setTime(date);
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				res.addAll(prelevementDao
						.findByDatePrelevementAfterDateWithBanque(cal,
								it.next()));
			}
		}
		return res;
	}

	@Override
	public List<Integer> findAfterDateCreationReturnIdsManager(Calendar date,
			List<Banque> banques) {
		List<Integer> liste = new ArrayList<Integer>();
		if (date != null && banques != null) {
			log.debug("Recherche des Prelevements enregistres apres la date "
					+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date
							.getTime()));
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				liste.addAll(findByOperationTypeAndDateReturnIds(
						operationTypeDao.findByNom("Creation").get(0), date,
						it.next()));
			}
		}
		return liste;
	}

	@Override
	public List<Prelevement> findAfterDateModificationManager(Calendar date,
			Banque banque) {
		List<Prelevement> liste = new ArrayList<Prelevement>();
		if (date != null && banque != null) {
			log.debug("Recherche des Prelevements modifies apres la date "
					+ new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss").format(date
							.getTime()));
			liste = findByOperationTypeAndDate(
					operationTypeDao.findByNom("Modification").get(0), date,
					banque);
		}
		return liste;
	}

	/**
	 * Recherche un prélèvement dont l'identifiant est passé en paramètre.
	 * 
	 * @param prelevementId
	 *            Identifiant du prélèvement que l'on recherche.
	 * @return Un Prelevement.
	 */
	public Prelevement findByIdManager(Integer prelevementId) {
		Prelevement p = prelevementDao.findById(prelevementId);
		return p;
	}

	/**
	 * Recupere la liste de prelevements en fonction du type d'operation et
	 * d'une date a laquelle la date d'enregistrement de l'operation doit etre
	 * superieure ou egale. Dans un premier temps, recupere la liste des
	 * objetIds qui sont ensuite utilises pour recuperer les prelevements.
	 * 
	 * @param oType
	 *            OperationType
	 * @param date
	 * @param banque
	 *            Banque à laquelle appartient le prlvt.
	 * @return List de Prelevement
	 */
	@SuppressWarnings("unchecked")
	private List<Prelevement> findByOperationTypeAndDate(OperationType oType,
			Calendar date, Banque banque) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
				+ "Operation o WHERE o.entite = :entite AND "
				+ "o.operationType = :oType AND date >= :date");
		opQuery.setParameter("entite", entiteDao.findByNom("Prelevement"));
		opQuery.setParameter("oType", oType);
		opQuery.setParameter("date", date);
		List<Integer> ids = opQuery.getResultList();
		Query prelQuery;
		if (ids.size() > 1 && banque != null) { // HQL IN () si liste taille > 1
			prelQuery = em.createQuery("SELECT DISTINCT p FROM Prelevement p "
					+ "WHERE p.prelevementId IN (:ids) "
					+ "AND p.banque = :banque");
			prelQuery.setParameter("ids", ids);
			prelQuery.setParameter("banque", banque);
		} else if (ids.size() == 1 && banque != null) {
			prelQuery = em
					.createQuery("SELECT DISTINCT p FROM Prelevement p "
							+ "WHERE p.prelevementId = :id "
							+ "AND p.banque = :banque");
			prelQuery.setParameter("id", ids.get(0));
			prelQuery.setParameter("banque", banque);
		} else {
			return new ArrayList<Prelevement>();
		}

		return prelQuery.getResultList();
	}

	/**
	 * Recupere la liste d'Ids de prelevements en fonction du type d'operation
	 * et d'une date a laquelle la date d'enregistrement de l'operation doit
	 * etre superieure ou egale. Dans un premier temps, recupere la liste des
	 * objetIds qui sont ensuite utilises pour recuperer les prelevements.
	 * 
	 * @param oType
	 *            OperationType
	 * @param date
	 * @param banque
	 *            Banque à laquelle appartient le prlvt.
	 * @return List de Prelevement
	 */
	@SuppressWarnings("unchecked")
	private List<Integer> findByOperationTypeAndDateReturnIds(
			OperationType oType, Calendar date, Banque banque) {
		EntityManager em = entityManagerFactory.createEntityManager();
		Query opQuery = em.createQuery("SELECT DISTINCT o.objetId FROM "
				+ "Operation o WHERE o.entite = :entite AND "
				+ "o.operationType = :oType AND date >= :date");
		opQuery.setParameter("entite", entiteDao.findByNom("Prelevement"));
		opQuery.setParameter("oType", oType);
		opQuery.setParameter("date", date);
		List<Integer> ids = opQuery.getResultList();
		Query prelQuery;
		if (ids.size() > 1 && banque != null) { // HQL IN () si liste taille > 1
			prelQuery = em.createQuery("SELECT DISTINCT p.prelevementId "
					+ "FROM Prelevement p "
					+ "WHERE p.prelevementId IN (:ids) "
					+ "AND p.banque = :banque");
			prelQuery.setParameter("ids", ids);
			prelQuery.setParameter("banque", banque);
		} else if (ids.size() == 1 && banque != null) {
			prelQuery = em.createQuery("SELECT DISTINCT p.prelevementId "
					+ "FROM Prelevement p " + "WHERE p.prelevementId = :id "
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
	public List<Prelevement> findLastCreationManager(List<Banque> banques,
			int nbResults) {

		List<Prelevement> liste = new ArrayList<Prelevement>();
		if (banques != null && banques.size() > 0 && nbResults > 0) {

			EntityManager em = entityManagerFactory.createEntityManager();
			Query query = em.createQuery("SELECT p " 
					+ "FROM Prelevement p " 
					+ "WHERE p.banque in (:banque) " 
					+ "ORDER BY p.prelevementId DESC");
			query.setParameter("banque", banques);
			query.setFirstResult(0);
			query.setMaxResults(nbResults);
//				Query query = em.createQuery("SELECT p "
//						+ "FROM Prelevement p, Operation o "
//						+ "WHERE o.objetId = p.prelevementId "
//						+ "AND o.entite = :entite "
//						+ "AND o.operationType = :oType "
//						+ "AND p.banque in (:banque) " + "ORDER BY o.date DESC");
//				query.setParameter("entite", entiteDao.findByNom("Prelevement"));
//				query.setParameter("oType", oType);

			liste.addAll(query.getResultList());
			
		}
		return liste;

	}

//	/**
//	 * Récupère une liste de Prelevements en fonction de leur banque et d'un
//	 * type d'opération. Cette liste est ordonnée par la date de l'opération. Sa
//	 * taille maximale est fixée par un paramètre.
//	 * 
//	 * @param oType
//	 *            Type de l'opération.
//	 * @param banque
//	 *            Banque des Prelevements.
//	 * @param nbResults
//	 *            Nombre max de Prelevements souhaités.
//	 * @return Liste de Prelevements.
//	 */
//	@SuppressWarnings("unchecked")
//	private List<Prelevement> findByLastOperationType(OperationType oType,
//			List<Banque> banques, int nbResults) {
//
//		List<Prelevement> prelevements = new ArrayList<Prelevement>();
//
//		if (banques.size() > 0) {
//			EntityManager em = entityManagerFactory.createEntityManager();
//			Query query = em.createQuery("SELECT p " 
//					+ "FROM Prelevement p " 
//					+ "WHERE p.banque in (:banque) " 
//					+ "ORDER BY p.prelevementId DESC");
//			query.setParameter("banque", banques);
//			query.setFirstResult(0);
//			query.setMaxResults(nbResults);
////			Query query = em.createQuery("SELECT p "
////					+ "FROM Prelevement p, Operation o "
////					+ "WHERE o.objetId = p.prelevementId "
////					+ "AND o.entite = :entite "
////					+ "AND o.operationType = :oType "
////					+ "AND p.banque in (:banque) " + "ORDER BY o.date DESC");
////			query.setParameter("entite", entiteDao.findByNom("Prelevement"));
////			query.setParameter("oType", oType);
//
//			prelevements.addAll(query.getResultList());
//		}
//
//		return prelevements;
//
//	}

	@Override
	public List<Prelevement> findByCodeLikeManager(String code,
			boolean exactMatch) {
		if (!exactMatch) {
			code = code + "%";
		}
		log.debug("Recherche Prelevement par code: " + code + " exactMatch "
				+ String.valueOf(exactMatch));
		return prelevementDao.findByCode(code);
	}

	@Override
	public List<Prelevement> findByCodeOrNumLaboLikeWithBanqueManager(
			String code, Banque banque, boolean exactMatch) {
		if (banque != null) {
			if (!exactMatch) {
				code = code + "%";
			}
			log.debug("Recherche Prelevement par code: " + code
					+ " exactMatch " + String.valueOf(exactMatch));
			return prelevementDao.findByCodeOrNumLaboWithBanque(code, banque);
		} else {
			return new ArrayList<Prelevement>();
		}
	}

	@Override
	public List<Integer> findByCodeOrNumLaboLikeBothSideWithBanqueReturnIdsManager(
			String code, List<Banque> banques, boolean exactMatch) {
		List<Integer> res = new ArrayList<Integer>();
		if (banques != null) {
			log.debug("Recherche Prelevement par code: " + code
					+ " exactMatch " + String.valueOf(exactMatch));
			if (!exactMatch) {
				code = "%" + code + "%";
			}
			Iterator<Banque> it = banques.iterator();
			while (it.hasNext()) {
				res.addAll(prelevementDao
						.findByCodeOrNumLaboWithBanqueReturnIds(code, it.next()));
			}
		}

		return res;
	}

	/**
	 * Recherche la maladie dont le prelevement est passé en paramètre.
	 * 
	 * @param prelevement
	 *            Prelevement pour lequel on recherche une maladie.
	 * @return Une Maladie.
	 */
	@Override
	public Maladie getMaladieManager(Prelevement prelevement) {
		prelevement = prelevementDao.mergeObject(prelevement);
		Maladie parent = null;
		if (prelevement != null && prelevement.getMaladie() != null) {
			parent = maladieDao.findById(prelevement.getMaladie()
					.getMaladieId());
			parent = maladieDao.mergeObject(parent);
		}

		return parent;
	}

	@Override
	public List<Prelevement> findByMaladieLibelleLikeManager(String libelle,
			boolean exactMatch) {
		if (!exactMatch) {
			libelle = libelle + "%";
		}
		log.debug("Recherche Prelevement par libelle de maladie: " + libelle
				+ " exactMatch " + String.valueOf(exactMatch));
		return prelevementDao.findByMaladieLibelleLike(libelle);
	}

	@Override
	public List<Prelevement> findByNatureManager(Nature nature) {
		if (nature != null) {
			log.debug("Recherche Prelevement par nature: " + nature.toString());
		}
		return prelevementDao.findByNature(nature);
	}

	@Override
	public List<Prelevement> findByTypeManager(PrelevementType prelevementType) {
		if (prelevementType != null) {
			log.debug("Recherche Prelevement par prelevementType: "
					+ prelevementType.toString());
		}
		return prelevementDao.findByPrelevementType(prelevementType);
	}

	@Override
	public List<Prelevement> findByConsentTypeManager(ConsentType consentType) {
		if (consentType != null) {
			log.debug("Recherche Prelevement par consentType: "
					+ consentType.toString());
		}
		return prelevementDao.findByConsentType(consentType);
	}

	@Override
	public Long findCountCreatedByCollaborateurManager(Collaborateur colla){
		if (colla != null){
			return prelevementDao.findCountCreatedByCollaborateur(colla).get(0);
		}
		else{
			return new Long(0);
		}
	}
	
	@Override
	public Long findCountByPreleveurManager(Collaborateur colla){
		if (colla != null){
			return prelevementDao.findCountByPreleveur(colla).get(0);
		}
		else{
			return new Long(0);
		}
	}
	
	@Override
	public Long findCountByServiceManager(Service serv){
		if (serv != null){
			return prelevementDao.findCountByService(serv).get(0);
		}
		else{
			return new Long(0);
		}
	}

	/**
	 * Recherche la liste des codes utilisés par les prélèvements liés à la
	 * banque passée en paramètre.
	 * 
	 * @param banque
	 *            Banque pour laquelle on recherche les codes.
	 * @return Liste de codes.
	 */
	@Override
	public List<String> findAllCodesForBanqueManager(Banque banque) {
		return prelevementDao.findByBanqueSelectCode(banque);
	}

	@Override
	public List<String> findAllNdasForBanqueManager(Banque banque) {
		return prelevementDao.findByBanqueSelectNda(banque);
	}

	@Override
	public List<Prelevement> findByIdsInListManager(List<Integer> ids) {
		List<Prelevement> prels = new ArrayList<Prelevement>();

		if (ids != null && ids.size() > 0) {
			// 2.0.10.5
			// Oracle split list to prevent ORA-01795 not more than 1000 elements
			// are allowed for 'in' subquery
			List<List<Integer>> chunks = Utils.split(ids, 1000);
		
			for (List<Integer> chks : chunks) {
				prels.addAll(prelevementDao.findByIdInList(chks));
			}
		}
		return prels;
	}

	/**
	 * Recherche une liste d'échantillons dont le prélèvement est passé en
	 * paramètre.
	 * 
	 * @param prelevement
	 *            Prelevement pour lequel on recherche des échantillons.
	 * @return List de Echantillon.
	 */
	@Override
	public Set<Echantillon> getEchantillonsManager(Prelevement prelevement) {
		Set<Echantillon> echans = new HashSet<Echantillon>();

		if (prelevement != null) {
			prelevement = prelevementDao.mergeObject(prelevement);
			echans = prelevement.getEchantillons();
			echans.size();
			return echans;
		} else {
			return new HashSet<Echantillon>();
		}

	}

	/**
	 * Recherche une liste de labo inters dont le prélèvement est passé en
	 * paramètre.
	 * 
	 * @param prelevement
	 *            Prelevement pour lequel on recherche des labos.
	 * @return Set de LaboInter.
	 */
	public Set<LaboInter> getLaboIntersManager(Prelevement prelevement) {
		Set<LaboInter> labos = new HashSet<LaboInter>();

		if (prelevement != null) {
			prelevement = prelevementDao.mergeObject(prelevement);
			labos = prelevement.getLaboInters();
			labos.size();
			return labos;
		} else {
			return new HashSet<LaboInter>();
		}
	}

	/**
	 * Recherche une liste de labo inters dont le prélèvement est passé en
	 * paramètre. Ces labos sont ordonnés par ordre.
	 * 
	 * @param prelevement
	 *            Prelevement pour lequel on recherche des labos.
	 * @return Liste ordonnée de LaboInter.
	 */
	public List<LaboInter> getLaboIntersWithOrderManager(Prelevement prelevement) {
		if (prelevement != null) {
			return laboInterManager.findByPrelevementWithOrder(prelevement);
		} else {
			return new ArrayList<LaboInter>();
		}
	}

	/**
	 * Recherche une liste de dérivés dont le prélèvement est passé en
	 * paramètre.
	 * 
	 * @param prelevement
	 *            Prelevement pour lequel on recherche des dérivés.
	 * @return List de ProdDerive.
	 */
	public List<ProdDerive> getProdDerivesManager(Prelevement prelevement) {

		if (prelevement != null) {
			prelevement = prelevementDao.mergeObject(prelevement);
			List<Transformation> list = transformationManager
					.findByParentManager(prelevement);
			List<ProdDerive> derives = new ArrayList<ProdDerive>();

			for (int i = 0; i < list.size(); ++i) {
				Transformation transfo = transformationDao.mergeObject(list
						.get(i));
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
		} else {
			return new ArrayList<ProdDerive>();
		}
	}

	@Override
	public void removeObjectManager(Prelevement prelevement, String comments,
			Utilisateur u, List<File> filesToDelete) {
		if (prelevement != null) {
			if (!isUsedObjectManager(prelevement)) {
				// Supprime operations associes
				CreateOrUpdateUtilities.removeAssociateOperations(prelevement,
						operationManager, comments, u);

				// Supprime importations associes
				CreateOrUpdateUtilities.removeAssociateImportations(
						prelevement, importHistoriqueManager);

				// Supprime annotations associes
				annotationValeurManager
						.removeAnnotationValeurListManager(annotationValeurManager
								.findByObjectManager(prelevement), filesToDelete);
				
				// Supprime non conformites associees
				CreateOrUpdateUtilities.removeAssociateNonConformites(prelevement, 
															objetNonConformeManager);

				prelevementDao.removeObject(prelevement.getPrelevementId());
				log.info("Suppression objet Prelevement "
						+ prelevement.toString());
			} else {
				log.warn("Suppression Prelevement " + prelevement.toString()
						+ " impossible car Objet est reference "
						+ "(par echantillon/derive)");
				throw new ObjectUsedException(
						"prelevement.deletion.isUsedCascade", true);
			}
		} else {
			log.warn("Suppression d'un Prelevement null");
		}
	}

	@Override
	public void removeObjectCascadeManager(Prelevement prelevement,
			String comments, Utilisateur user, List<File> filesToDelete) {
		if (prelevement != null) {
			log.info("Suppression en cascade depuis objet Prelevement "
					+ prelevement.toString());

			// suppression echantillon en mode cascade
			Iterator<Echantillon> echansIt = getEchantillonsManager(prelevement)
					.iterator();
						
			while (echansIt.hasNext()) {
				echantillonManager.removeObjectCascadeManager(echansIt.next(),
						comments, user, filesToDelete);
			}
			prelevement.setEchantillons(new HashSet<Echantillon>());

			// suppression des dérivés en mode cascade
			Iterator<Transformation> transfIt = transformationManager
					.findByParentManager(prelevement).iterator();
			while (transfIt.hasNext()) {
				prodDeriveManager.removeObjectCascadeManager(transfIt.next(),
						comments, user, filesToDelete);
			}

			removeObjectManager(prelevement, comments, user, filesToDelete);
		}
	}

	@Override
	public boolean isUsedObjectManager(Prelevement prelevement) {
		Prelevement prel = prelevementDao.mergeObject(prelevement);
		// References vers echantillons, derives?

		return prel.getEchantillons().size() > 0
				|| transformationManager.findByParentManager(prelevement)
						.size() > 0;
	}

	/**
	 * Verifie que les Objets devant etre obligatoirement associes sont non
	 * nulls et lance la validation via le Validator. Set la maladie et le
	 * patient en cascade car utilisée dans la validation.
	 * 
	 * @param prelevement
	 * @param banque
	 * @param nature
	 * @param consentType
	 * @param maladie
	 * @param laboInters
	 * @param operation
	 *            demandant la verification
	 * @param utilisateur
	 */
	private void checkRequiredObjectsAndValidate(Prelevement prelevement,
			Banque banque, Nature nature, ConsentType consentType,
			Maladie maladie, List<LaboInter> laboInters, String operation,
			Utilisateur utilisateur, boolean doValidation, String baseDir) {
		// Banque required
		if (banque != null) {
			prelevement.setBanque(banqueDao.mergeObject(banque));
		} else if (prelevement.getBanque() == null) {
			log.warn("Objet obligatoire Banque manquant" + " lors de la "
					+ operation + " d'un Prelevement");
			throw new RequiredObjectIsNullException("Prelevement", operation,
					"Banque");
		}
		// Nature required
		if (nature != null) {
			prelevement.setNature(natureDao.mergeObject(nature));
		} else if (prelevement.getNature() == null) {
			log.warn("Objet obligatoire Nature manquant" + " lors de la "
					+ operation + " d'un Prelevement");
			throw new RequiredObjectIsNullException("Prelevement", operation,
					"Nature");
		}

		// ConsentType required
		if (consentType != null) {
			prelevement.setConsentType(consentTypeDao.mergeObject(consentType));
		} else if (prelevement.getConsentType() == null) {
			log.warn("Objet obligatoire ConsentType manquant" + " lors de la "
					+ operation + " d'un Prelevement");
			throw new RequiredObjectIsNullException("Prelevement", operation,
					"ConsentType");
		}

		// Maladie non required mais utilise dans validation
		if (maladie != null) {
			if (maladie.getMaladieId() == null) { // creation conjointe
				if (maladie.getPatient().getPatientId() == null) {
					patientManager.createOrUpdateObjectManager(
							maladie.getPatient(), null, null, null, null, null,
							null, null,
							utilisateur, "creation", baseDir, false);
				}
				maladieManager.createOrUpdateObjectManager(maladie, null, null,
						utilisateur, "creation");

				maladieManager.getMaladiesManager(maladie.getPatient()).add(
						maladie);

				// // validation maladie
				// BeanValidator.validateObject(maladie,
				// new Validator[]{maladieValidator});
				// // creation conjointe maladie - patient
				// if (maladie.getPatient().getPatientId() == null) {
				// BeanValidator.validateObject(maladie.getPatient(),
				// new Validator[]{patientValidator});
				// maladie.setPatient(patientDao
				// .mergeObject(maladie.getPatient()));
				// }
			}
			prelevement.setMaladie(maladie);
		}
		if (laboInters != null) {
			prelevement.setLaboInters(new HashSet<LaboInter>(laboInters));
			// prelevement.setLaboInters(new HashSet<LaboInter>());
			for (int i = 0; i < laboInters.size(); i++) {
				LaboInter labo = laboInters.get(i);
				labo.setPrelevement(prelevement);
				BeanValidator.validateObject(labo,
						new Validator[] { laboInterValidator });
				// prelevement.getLaboInters().add(labo);
			}
			prelevement.setLaboInters(new HashSet<LaboInter>());
		}

		if (doValidation) {
			// Validation
			BeanValidator.validateObject(prelevement,
					new Validator[] { prelevementValidator });
		}
	}

	/**
	 * Merge et assigne tous les objects associes non obligatoires au
	 * prelevement (sauf maladie car utilisé dans validation).
	 * 
	 * @param prelevement
	 * @param preleveur
	 * @param servicePreleveur
	 * @param prelevementType
	 * @param conditType
	 * @param conditMilieu
	 * @param transporteur
	 * @param operateur
	 * @param quantiteUnite
	 */
	private void mergeNonRequiredObjects(Prelevement prelevement,
			Maladie maladie, Collaborateur preleveur, Service servicePreleveur,
			PrelevementType prelevementType, ConditType conditType,
			ConditMilieu conditMilieu, Transporteur transporteur,
			Collaborateur operateur, Unite quantiteUnite) {

		prelevement.setPrelevementType(prelevementTypeDao
				.mergeObject(prelevementType));
		prelevement.setPreleveur(collaborateurDao.mergeObject(preleveur));
		prelevement.setServicePreleveur(serviceDao
				.mergeObject(servicePreleveur));
		prelevement.setConditMilieu(conditMilieuDao.mergeObject(conditMilieu));
		prelevement.setConditType(conditTypeDao.mergeObject(conditType));
		prelevement.setTransporteur(transporteurDao.mergeObject(transporteur));
		prelevement.setOperateur(collaborateurDao.mergeObject(operateur));
		prelevement.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));
	}

	@Override
	public List<Prelevement> findAllPrelevementsManager(Patient patient) {
		List<Prelevement> prels = new ArrayList<Prelevement>();
		if (patient.getPatientId() != null) {
			// List<Maladie> maladies =
			// new ArrayList<Maladie>(maladieManager
			// .getMaladiesManager(patient));
			// for (int i = 0; i < maladies.size(); i++) {
			// prels.addAll(maladieDao
			// .mergeObject(maladies.get(i)).getPrelevements());
			// }
			prels.addAll(prelevementDao.findByPatient(patient));
		}

		return prels;
	}

	@Override
	public List<LaboInter> cascadeNonSterileManager(Prelevement prelevement,
			List<LaboInter> labos, Integer apresOrdre, boolean cascadeEchans) {

		// laboInter steriles
		// List<LaboInter> labos =
		// new ArrayList<LaboInter>(prelevement.getLaboInters());

		if (labos != null && apresOrdre != null) {
			for (int i = 0; i < labos.size(); i++) {
				if (labos.get(i).getOrdre() >= apresOrdre) {
					labos.get(i).setSterile(false);
				}
			}
		}

		if (cascadeEchans) {
			// prelevement.setLaboInters(null);
			// Echantillons
			List<Echantillon> echans = new ArrayList<Echantillon>(
					getEchantillonsManager(prelevement));
			for (int j = 0; j < echans.size(); j++) {
				echans.get(j).setSterile(false);
				echantillonDao.updateObject(echans.get(j));
			}
			// if (labos != null) {
			// prelevement.setLaboInters(new HashSet<LaboInter>(labos));
			// }
		}
		return labos;
	}

	@Override
	public List<Prelevement> findByMaladieAndBanqueManager(Maladie mal,
			Banque bank) {
		return prelevementDao.findByMaladieAndBanque(mal, bank);
	}

	@Override
	public List<Prelevement> findByMaladieAndOtherBanquesManager(Maladie mal,
			Banque bank) {
		return prelevementDao.findByMaladieAndOtherBanques(mal, bank);
	}

	@Override
	public List<Prelevement> findByBanquesManager(List<Banque> banks) {
		List<Prelevement> prels = new ArrayList<Prelevement>();
		if (banks != null && !banks.isEmpty()) {
			prels = prelevementDao.findByBanques(banks);
		}
		return prels;
	}

	@Override
	public List<Integer> findAllObjectsIdsByBanquesManager(List<Banque> banques) {
		if (banques != null && banques.size() > 0) {
			return prelevementDao.findByBanquesAllIds(banques);
		} else {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public List<Prelevement> findByPatientManager(Patient pat) {
		return prelevementDao.findByPatient(pat);
	}

	@Override
	public List<Integer> findByPatientNomReturnIdsManager(String nom,
			List<Banque> banks, boolean exactMatch) {
		List<Integer> res = new ArrayList<Integer>();
		if (nom != null && banks != null) {
			if (!exactMatch) {
				nom = "%" + nom + "%";
			}
			for (int i = 0; i < banks.size(); i++) {
				res.addAll(prelevementDao.findByPatientNomReturnIds(nom,
						banks.get(i)));
			}
		}

		return res;
	}

	@Override
	public List<Integer> findByCodeOrNumLaboInListManager(
			List<String> criteres, List<Banque> banques) {
		if (criteres != null && banques != null && criteres.size() > 0
				&& banques.size() > 0) {
			return prelevementDao.findByCodeOrNumLaboInListWithBanque(criteres,
					banques);
		} else {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public List<Integer> findByPatientNomOrNipInListManager(
			List<String> criteres, List<Banque> banks) {
		if (criteres != null && banks != null && criteres.size() > 0
				&& banks.size() > 0) {
			return prelevementDao.findByPatientNomOrNipInList(criteres, banks);
		} else {
			return new ArrayList<Integer>();
		}
	}

	@Override
	public List<Prelevement> findByNdaLikeManager(String nda) {
		return prelevementDao.findByNdaLike(nda);
	}

	@Override
	public void createPrelAndEchansManager(Prelevement prelevement,
			List<AnnotationValeur> annosPrel, List<Echantillon> echantillons,
			List<AnnotationValeur> annosEchan, Banque banque, Utilisateur user,
			boolean doValidation, String baseDir) {

		// revert maladie et patient au besoin
		boolean revertMaladie = false;
		boolean revertPatient = false;
		if (prelevement.getMaladie() != null) {
			revertMaladie = prelevement.getMaladie().getMaladieId() == null;
			if (revertMaladie) {
				revertPatient = prelevement.getMaladie().getPatient()
						.getPatientId() == null;
			}
		}
		
		List<File> filesCreated = new ArrayList<File>();

		// enregistrement du prelevement
		createObjectManager(prelevement, banque, prelevement.getNature(),
				prelevement.getMaladie(), prelevement.getConsentType(),
				prelevement.getPreleveur(), prelevement.getServicePreleveur(),
				prelevement.getPrelevementType(), prelevement.getConditType(),
				prelevement.getConditMilieu(), prelevement.getTransporteur(),
				prelevement.getOperateur(), prelevement.getQuantiteUnite(),
				new ArrayList<LaboInter>(prelevement.getLaboInters()),
				annosPrel, filesCreated, user, doValidation, baseDir, false);

		try {
					
			// enregistrement des echantillons
			for (int i = 0; i < echantillons.size(); i++) {
				Echantillon newEchan = echantillons.get(i);

				// création de l'objet
				echantillonManager.createObjectWithCrAnapathManager(newEchan, banque,
						prelevement, newEchan.getCollaborateur(),
						newEchan.getObjetStatut(), newEchan.getEmplacement(),
						newEchan.getEchantillonType(), null,
						newEchan.getQuantiteUnite(),
						newEchan.getEchanQualite(), newEchan.getModePrepa(),
						newEchan.getCrAnapath(), newEchan.getAnapathStream(),
						filesCreated,
						newEchan.getReservation(), annosEchan, user,
						doValidation, baseDir, false);
			}
		} catch (RuntimeException re) {
			if (revertMaladie) {
				prelevement.getMaladie().setMaladieId(null);
				if (revertPatient) {
					prelevement.getMaladie().getPatient().setPatientId(null);
				}
			}

			prelevement.setPrelevementId(null);
			// revert Objects
			Iterator<LaboInter> it = prelevement.getLaboInters().iterator();
			while (it.hasNext()) {
				it.next().setLaboInterId(null);
			}
			Iterator<Echantillon> itE = echantillons.iterator();
			Echantillon e;
			while (itE.hasNext()) {
				e = itE.next();
				if (e.getEchantillonId() != null) {
					e.setEchantillonId(null);
					if (e.getCrAnapath() != null) {
						e.getCrAnapath().setFichierId(null);
					}
				} else { // la boucle arrive a l'echantillon planté.
					break;
				}
			}
			
			// rollback au besoin...
			for (File f : filesCreated) {
				f.delete();
			}
			
			throw re;
		}
	}

	@Override
	public Set<Risque> getRisquesManager(Prelevement prelevement) {
		Set<Risque> risques = new HashSet<Risque>();

		if (prelevement != null) {
			prelevement = prelevementDao.mergeObject(prelevement);
			risques = prelevement.getRisques();
			risques.size();
		}
		return risques;
	}

	@Override
	public List<TKAnnotableObject> getPrelevementChildrenManager(Prelevement p) {
		List<TKAnnotableObject> children = new ArrayList<TKAnnotableObject>();
		Iterator<Echantillon> echansIt = getEchantillonsManager(p).iterator();
		Echantillon echan;
		while (echansIt.hasNext()) {
			echan = echansIt.next();
			children.add(echan);
			children.addAll(prodDeriveManager.findByParentManager(echan, true));
		}
		children.addAll(prodDeriveManager.findByParentManager(p, true));
		return children;
	}

	@Override
	public boolean hasCessedObjectManager(Prelevement prel) {
		Iterator<TKAnnotableObject> childrenIt = getPrelevementChildrenManager(
				prel).iterator();
		while (childrenIt.hasNext()) {
			if (!cederObjetManager.findByObjetManager(childrenIt.next())
					.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void switchBanqueMultiplePrelevementManager(Prelevement[] prlvts,
			Banque bank, boolean doValidation, Utilisateur u) {

		List<File> filesToDelete = new ArrayList<File>();
		
		if (prlvts != null) {
			for (Prelevement p : prlvts) {
				switchBanqueCascadeManager(p, bank, doValidation, u, 
						filesToDelete);
			}
		}
		
		if (filesToDelete != null) {
			for (File f : filesToDelete) {
				f.delete();
			}
		}
	}

	@Override
	public void switchBanqueCascadeManager(Prelevement prel, Banque bank,
			boolean doValidation, Utilisateur u, List<File> filesToDelete) {
		if (bank != null && prel != null && !bank.equals(prel.getBanque())) {

			Iterator<Echantillon> echansIt = getEchantillonsManager(prel)
					.iterator();
			
			while (echansIt.hasNext()) {
				echantillonManager.switchBanqueCascadeManager(echansIt.next(),
						bank, doValidation, u, filesToDelete);
			}

			Iterator<ProdDerive> derivesIt = getProdDerivesManager(prel)
					.iterator();
			while (derivesIt.hasNext()) {
				prodDeriveManager.switchBanqueCascadeManager(derivesIt.next(),
						bank, doValidation, u, filesToDelete);
			}
			

			prel.setBanque(bank);
			
			if (doValidation && findDoublonManager(prel)) {
				log.warn("Doublon lors creation objet Prelevement "
						+ prel.toString());
				throw new DoublonFoundException("Prelevement", "switchBanque", prel.getCode(), null);
			}
			prel = prelevementDao.mergeObject(prel);

			// annotations
			annotationValeurManager.switchBanqueManager(prel, bank, filesToDelete);
			// if (prel.getMaladie() != null) {
			// annotationValeurManager
			// .switchBanqueManager(prel.getMaladie().getPatient(), bank);
			// }

			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager
					.createObjectManager(creationOp, u, operationTypeDao
							.findByNom("ChangeCollection").get(0), prel);
		}
	}

	@Override
	public Calendar getDateCongelationManager(Prelevement prel) {
		if (prel != null) {
			if (prel.getCongDepart() != null && prel.getCongDepart()) {
				return prel.getDateDepart();
			} else if (prel.getCongArrivee() != null && prel.getCongArrivee()) {
				return prel.getDateArrivee();
			} else {
				Iterator<LaboInter> labosIt;
				if (prel.getLaboInters() instanceof PersistentSet) {
					labosIt = getLaboIntersManager(prel).iterator();
				} else {
					labosIt = prel.getLaboInters().iterator();
				}
				LaboInter next;
				while (labosIt.hasNext()) {
					next = labosIt.next();
					if (next.getCongelation() != null && next.getCongelation()) {
						return next.getDateDepart();
					}
				}
			}
		}
		return null;
	}

	@Override
	public void updateMultipleObjectsManager(List<Prelevement> prelevements,
			List<Prelevement> basePrelevements,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<AnnotationValeur> listAnnoToDelete, 
			List<NonConformite> ncf,
			boolean cascadeNonSterile,
			Utilisateur utilisateur, String baseDir) {

		Integer nosterile = null;
		if (cascadeNonSterile) {
			nosterile = 0;
		}
		
		List<File> filesCreated = new ArrayList<File>();
		List<File> filesToDelete = new ArrayList<File>();

		for (int i = 0; i < prelevements.size(); i++) {
			Prelevement prel = prelevements.get(i);
			try {
				updateObjectManager(prel, null, null, null, null,
						prel.getPreleveur(), prel.getServicePreleveur(),
						prel.getPrelevementType(), prel.getConditType(),
						prel.getConditMilieu(), prel.getTransporteur(),
						prel.getOperateur(), prel.getQuantiteUnite(), null,
						null, null, filesCreated, filesToDelete, 
						utilisateur, nosterile, true, baseDir, true);
				
				// enregistrement de la conformité
				objetNonConformeManager
					.createUpdateOrRemoveListObjectManager(
								prel, ncf, "Arrivee");
			} catch (RuntimeException e) {
				if (e instanceof TKException) {
					((TKException) e).setEntiteObjetException("Prelevement");
					((TKException) e).setIdentificationObjetException(prel
							.getCode());
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
							.createFileBatchForTKObjectsManager(basePrelevements, 
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
				annotationValeurManager.createAnnotationValeurListManager(
						listAnnoToCreateOrUpdate, null, utilisateur, null, baseDir, 
						filesCreated, filesToDelete);
			}
			
			if (filesToDelete != null) {
				for (File f : filesToDelete) {
					f.delete();
				}
			}
		} catch (RuntimeException e) {
			if (filesCreated != null) {
				for (File f : filesCreated) {
					f.delete();
				}
			}
		}	
	}

	@Override
	public void switchMaladieManager(Prelevement prel, Maladie maladie,
			Utilisateur usr) {

		if (maladie != null && prel != null && usr != null
				&& !maladie.equals(prel.getMaladie())) {

			prel.setMaladie(maladie);
			prel = prelevementDao.mergeObject(prel);

			Operation creationOp = new Operation();
			creationOp.setDate(Utils.getCurrentSystemCalendar());
			operationManager.createObjectManager(creationOp, usr,
					operationTypeDao.findByNom("Modification").get(0), prel);
		}
	}

	@Override
	public List<Prelevement> findByDossierExternesManager(List<Banque> banques,
			List<Emetteur> emetteurs) {
		List<Prelevement> prlvts = new ArrayList<Prelevement>();

		if (banques != null && banques.size() > 0 && emetteurs != null
				&& emetteurs.size() > 0) {
			List<String> codes = dossierExterneDao
					.findByEmetteurInListSelectIdentification(emetteurs);

			if (codes.size() > 0) {
				// 2.1.0
				// Oracle split list to prevent ORA-01795 not more than 1000 elements
				// are allowed for 'in' subquery
				List<List<String>> chunks = Utils.split(codes, 1000);
			
				for (List<String> chks : chunks) {
					prlvts.addAll(prelevementDao.findByCodesAndBanquesInList(chks,
							banques));
				}
				// prlvts = prelevementDao.findByCodesAndBanquesInList(codes,
				//		banques);
			}
		}

		return prlvts;
	}
	
	@Override
	public void createObjectWithNonConformitesManager(Prelevement prelevement, 
			Banque banque,
			Nature nature, Maladie maladie, ConsentType consentType,
			Collaborateur preleveur, Service servicePreleveur,
			PrelevementType prelevementType, ConditType conditType,
			ConditMilieu conditMilieu, Transporteur transporteur,
			Collaborateur operateur, Unite quantiteUnite,
			List<LaboInter> laboInters,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			Utilisateur utilisateur, boolean doValidation, String baseDir,
			boolean isImport,
			List<NonConformite> noconfs) {
		
		if (noconfs != null && !noconfs.isEmpty()) {
			prelevement.setConformeArrivee(false);
		}
		
		List<File> filesCreated = new ArrayList<File>();
		
		try {
			createObjectManager(prelevement, banque, nature, maladie, consentType, 
					preleveur, servicePreleveur, prelevementType, conditType, 
					conditMilieu, transporteur, operateur, quantiteUnite, 
					laboInters, listAnnoToCreateOrUpdate, filesCreated, utilisateur, 
					doValidation, baseDir, isImport);
			
			objetNonConformeManager.createUpdateOrRemoveListObjectManager(
						prelevement, noconfs, "Arrivee");
		} catch (RuntimeException re) {
				for (File f : filesCreated) {
				f.delete();
			}
			throw(re);
		}
	}
	
	@Override
	public void updateObjectWithNonConformitesManager(Prelevement prelevement, 
			Banque banque,
			Nature nature, Maladie maladie, ConsentType consentType,
			Collaborateur preleveur, Service servicePreleveur,
			PrelevementType prelevementType, ConditType conditType,
			ConditMilieu conditMilieu, Transporteur transporteur,
			Collaborateur operateur, Unite quantiteUnite,
			List<LaboInter> laboInters,
			List<AnnotationValeur> listAnnoToCreateOrUpdate,
			List<AnnotationValeur> listAnnoToDelete, Utilisateur utilisateur,
			Integer cascadeNonSterile, boolean doValidation, String baseDir,
			boolean multiple, 
			List<NonConformite> noconfs) {
		
		if (noconfs != null && !noconfs.isEmpty()) {
			prelevement.setConformeArrivee(false);
		}
		
		List<File> filesCreated = new ArrayList<File>();
		List<File> filesToDelete = new ArrayList<File>();
		
		try {
			updateObjectManager(prelevement, banque, nature, maladie, consentType, 
					preleveur, servicePreleveur, prelevementType, conditType, 
					conditMilieu, transporteur, operateur, quantiteUnite, 
					laboInters, listAnnoToCreateOrUpdate, listAnnoToDelete, 
					filesCreated, filesToDelete, 
					utilisateur, cascadeNonSterile, doValidation, baseDir, multiple);
			
			
			objetNonConformeManager.createUpdateOrRemoveListObjectManager(
					prelevement, noconfs, "Arrivee");
			
			
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
	public void removeListFromIdsManager(List<Integer> ids, String comment, 
			Utilisateur u) {
		if (ids != null) {
			List<File> filesToDelete = new ArrayList<File>();
			Prelevement p;
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
	public List<Prelevement> findByPatientAndAuthorisationsManager(Patient pat, 
			Plateforme pf, Utilisateur utilisateur) {
		
		if (utilisateur != null && pf != null) {
			Set<Banque> banks;
			// banques consultables
			if (!utilisateur.isSuperAdmin()) {
				banks = new HashSet<Banque>(banqueDao
						.findByEntiteConsultByUtilisateur(utilisateur, 
							entiteDao.findByNom("Prelevement").get(0), pf));
				banks.addAll(banqueDao.findByUtilisateurIsAdmin(utilisateur, pf));
	
				Set<Banque> crossBanks = 
					new HashSet<Banque>(banqueDao
									.findByAutoriseCrossPatient(true));
				banks.addAll(crossBanks);		
			} else {
				// @since 2.1 all banks
				banks = new HashSet<Banque>(banqueDao.findByPlateformeAndArchive(pf, false));
				banks.addAll(banqueDao.findByPlateformeAndArchive(pf, true));
			}
	
			if (!banks.isEmpty()) {
				return prelevementDao.findByPatientAndBanques(pat, new ArrayList<Banque>(banks));
			} 
		}
		return new ArrayList<Prelevement>();
	}
	
	@Override
	public List<Prelevement> findByCodeInPlateformeManager(String code, Plateforme pf) {
		return prelevementDao.findByCodeInPlateforme(code, pf);
	}
}
