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
package fr.aphp.tumorotek.manager.impl.io.imports;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonDao;
import fr.aphp.tumorotek.dao.coeur.patient.MaladieDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampAnnotationManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.exception.BadFileFormatException;
import fr.aphp.tumorotek.manager.exception.DeriveBatchSaveException;
import fr.aphp.tumorotek.manager.exception.DeriveImportParentNotFoundException;
import fr.aphp.tumorotek.manager.exception.ErrorsInImportException;
import fr.aphp.tumorotek.manager.exception.FormulaException;
import fr.aphp.tumorotek.manager.exception.HeaderException;
import fr.aphp.tumorotek.manager.exception.UsedPositionException;
import fr.aphp.tumorotek.manager.exception.WrongImportValueException;
import fr.aphp.tumorotek.manager.impl.coeur.echantillon.EchantillonJdbcSuite;
import fr.aphp.tumorotek.manager.io.TKAnnotableObjectDuo;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.io.imports.ImportError;
import fr.aphp.tumorotek.manager.io.imports.ImportHistoriqueManager;
import fr.aphp.tumorotek.manager.io.imports.ImportManager;
import fr.aphp.tumorotek.manager.io.imports.ImportProperties;
import fr.aphp.tumorotek.manager.io.imports.ImportTemplateManager;
import fr.aphp.tumorotek.manager.io.utils.TKAnnotableDuoManager;
import fr.aphp.tumorotek.manager.qualite.NonConformiteManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKThesaurusObject;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.coeur.annotation.Item;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Service;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportHistorique;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.io.imports.Importation;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class ImportManagerImpl implements ImportManager
{

	private final Log log = LogFactory.getLog(ImportManager.class);

	private ImportTemplateManager importTemplateManager;
	private ImportColonneManager importColonneManager;
	private ImportHistoriqueManager importHistoriqueManager;
	private EntiteManager entiteManager;
	private PatientManager patientManager;
	private MaladieManager maladieManager;
	private PrelevementManager prelevementManager;
	private ChampAnnotationManager champAnnotationManager;
	private AnnotationValeurManager annotationValeurManager;
	private EchantillonManager echantillonManager;
	private ProdDeriveManager prodDeriveManager;
	private EmplacementManager emplacementManager;
	private ObjetStatutManager objetStatutManager;
	private MaladieDao maladieDao;
	private EchantillonDao echantillonDao;
	private ProdDeriveDao prodDeriveDao;
	private NonConformiteManager nonConformiteManager;
	private EntityManagerFactory entityManagerFactory;
	private DataSource dataSource;
	private ChampEntiteDao champEntiteDao;
	private ObjetNonConformeManager objetNonConformeManager;
	private TKAnnotableDuoManager tkAnnotableDuoManager;

	private Map<TKAnnotableObject, List<NonConformite>> ncfsPrelevement = null;
	private Map<TKAnnotableObject, List<NonConformite>> ncfsEchanTrait = null;
	private Map<TKAnnotableObject, List<NonConformite>> ncfsEchanCess = null;
	private Map<TKAnnotableObject, List<NonConformite>> ncfsDeriveTrait = null;
	private Map<TKAnnotableObject, List<NonConformite>> ncfsDeriveCess = null;

	public void setImportTemplateManager(final ImportTemplateManager iManager){
		this.importTemplateManager = iManager;
	}

	public void setImportColonneManager(final ImportColonneManager iManager){
		this.importColonneManager = iManager;
	}

	public void setImportHistoriqueManager(final ImportHistoriqueManager iManager){
		this.importHistoriqueManager = iManager;
	}

	public void setEntityManagerFactory(final EntityManagerFactory eFactory){
		this.entityManagerFactory = eFactory;
	}

	public void setEntiteManager(final EntiteManager eManager){
		this.entiteManager = eManager;
	}

	public void setPatientManager(final PatientManager pManager){
		this.patientManager = pManager;
	}

	public void setMaladieManager(final MaladieManager mManager){
		this.maladieManager = mManager;
	}

	public void setPrelevementManager(final PrelevementManager pManager){
		this.prelevementManager = pManager;
	}

	public void setChampAnnotationManager(final ChampAnnotationManager cManager){
		this.champAnnotationManager = cManager;
	}

	public void setAnnotationValeurManager(final AnnotationValeurManager aM){
		this.annotationValeurManager = aM;
	}

	public void setEchantillonManager(final EchantillonManager eManager){
		this.echantillonManager = eManager;
	}

	public void setProdDeriveManager(final ProdDeriveManager pManager){
		this.prodDeriveManager = pManager;
	}

	public void setEmplacementManager(final EmplacementManager eManager){
		this.emplacementManager = eManager;
	}

	public void setObjetStatutManager(final ObjetStatutManager oManager){
		this.objetStatutManager = oManager;
	}

	public void setMaladieDao(final MaladieDao mDao){
		this.maladieDao = mDao;
	}

	public void setEchantillonDao(final EchantillonDao eDao){
		this.echantillonDao = eDao;
	}

	public void setProdDeriveDao(final ProdDeriveDao pDao){
		this.prodDeriveDao = pDao;
	}

	public void setNonConformiteManager(final NonConformiteManager nc){
		this.nonConformiteManager = nc;
	}

	public void setDataSource(final DataSource ds){
		this.dataSource = ds;
	}

	public void setChampEntiteDao(final ChampEntiteDao cDao){
		this.champEntiteDao = cDao;
	}

	public void setObjetNonConformeManager(final ObjetNonConformeManager oM){
		this.objetNonConformeManager = oM;
	}

	public void setTkAnnotableDuoManager(final TKAnnotableDuoManager tM){
		this.tkAnnotableDuoManager = tM;
	}

	@Override
	public String getCellContent(final Cell cell, final boolean isDate, final FormulaEvaluator evaluator){
		String o = "";
		CellType type;

		if(cell != null){
			type = cell.getCellTypeEnum();
			CellValue cellValue = null;
			if(type == CellType.FORMULA){
				if(evaluator != null){
					cellValue = evaluator.evaluate(cell);
				}else{
					throw new FormulaException(cell.getCellFormula().toString());
				}
				if(cellValue != null){
					type = cellValue.getCellTypeEnum();
				}else{
					type = CellType.BLANK;
				}
			}else{
				type = cell.getCellTypeEnum();
			}

			if(type == CellType.STRING){
				o = cellValue == null ? cell.getStringCellValue().trim() : cellValue.getStringValue();
			}else{
				//o = null;
				if(isDate){
					final Date dt = cell.getDateCellValue();
					if(dt != null){
						final Calendar cal = Calendar.getInstance();
						cal.setTime(dt);
						if(cal.get(Calendar.HOUR_OF_DAY) > 0 || cal.get(Calendar.MINUTE) > 0){
							final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
							o = sdf.format(dt);
						}else{
							final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
							o = sdf.format(dt);
						}
					}else{
						o = null;
					}
				}else if(type == CellType.NUMERIC){
					final double db = cellValue == null ? cell.getNumericCellValue() : cellValue.getNumberValue();
					final long lg = (long) db;

					if(lg == db){
						o = String.valueOf(lg);
					}else{
						o = String.valueOf(db);
					}
				}else if(type == CellType.BOOLEAN){
					final Boolean value = cellValue == null ? cell.getBooleanCellValue() : cellValue.getBooleanValue();
					if(value){
						o = "1";
					}else{
						o = "0";
					}
				}else{
					o = null;
				}
			}

		}

		return o;
	}

	@Override
	public Hashtable<String, Integer> initColumnsHeadersManager(final Row row) throws HeaderException{
		final Hashtable<String, Integer> headers = new Hashtable<>();

		final int nbRow = row.getLastCellNum();
		int i = 0;

		try{
			// pour chaque cellule
			for(i = 0; i < nbRow; ++i){
				// on extrait le nom de la colonne
				final String nomColonne = getCellContent(row.getCell(i), false, null);
				// ajout dans la hashtable
				if(!headers.containsKey(nomColonne)){
					headers.put(nomColonne, i);
				}
			}

			return headers;
		}catch(final Exception e){
			throw new HeaderException(i + 1);
		}
	}

	@Override
	public Hashtable<Entite, List<ImportColonne>> initImportColonnesManager(final Hashtable<String, Integer> colonnes,
			final ImportTemplate importTemplate){
		final Hashtable<Entite, List<ImportColonne>> entitesColonnes = new Hashtable<>();

		if(importTemplate != null && colonnes != null){
			// on récupère les entités à importer
			final Set<Entite> entites = importTemplateManager.getEntiteManager(importTemplate);
			// pour chaque entité
			final Iterator<Entite> it = entites.iterator();
			while(it.hasNext()){
				final Entite e = it.next();
				// on trouve les colonnes de l'import liées à cette
				// entité
				final List<ImportColonne> cols = importColonneManager.findByTemplateAndEntiteManager(importTemplate, e);

				// on récupère les annotations liées à cette entité
				final List<ImportColonne> annos = importColonneManager.findByTemplateAndAnnotationEntiteManager(importTemplate, e);

				cols.addAll(annos);

				// on met les colonnes et l'entité dans la hashtable
				entitesColonnes.put(e, cols);

				// on vérifie que chaque colonne du template est bien
				// dans le fichier
				final List<ImportColonne> colsManquantes = new ArrayList<>();
				for(int i = 0; i < cols.size(); i++){
					if(!colonnes.containsKey(cols.get(i).getNom())){
						colsManquantes.add(cols.get(i));
					}
				}
				if(colsManquantes.size() > 0){
					throw new BadFileFormatException(colsManquantes);
				}
			}
		}

		return entitesColonnes;
	}

	@Override
	public Hashtable<String, Object> extractValuesForOneThesaurus(final ChampEntite champEntite, final Banque banque) {
		Hashtable<String, Object> values = new Hashtable<String, Object>();

		if(champEntite != null){
			
			List<Integer> thesValueIds = null;
			// prépare le filtre les valeurs de thésaurus par leur ids suivant les restrictions gatsbi
			if (banque.getEtude() != null) {
				Contexte contexte = banque.getEtude().getContexteForEntite(translateChpIdToFindContexteEntite(champEntite.getId()));
				if (contexte != null) { 
					thesValueIds = contexte
						.getThesaurusValuesForChampEntiteId(translateChpIdToMatchGatsbiContexte(champEntite.getId()))
							.stream().map(v -> v.getThesaurusId()).collect(Collectors.toList());			
				}
			}
			
			if(!champEntite.getNom().matches("Conforme.*Raison")){
				// creation de la requête permettant de récupérer toutes
				// les valeurs du thésaurus
				final StringBuffer sql = new StringBuffer();
				sql.append("SELECT e FROM ");
				sql.append(champEntite.getEntite().getNom());
				sql.append(" as e");

				if(!champEntite.getEntite().getNom().equals("Collaborateur") && !champEntite.getEntite().getNom().equals("Service")
						&& !champEntite.getEntite().getNom().equals("Transporteur") && !champEntite.getEntite().getNom().equals("Unite")
						&& !champEntite.getEntite().getNom().equals("ObjetStatut")){
					sql.append(" where e.plateforme = :pf");
				}

				final EntityManager em = entityManagerFactory.createEntityManager();
				final TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);
				if(!champEntite.getEntite().getNom().equals("Collaborateur") && !champEntite.getEntite().getNom().equals("Service")
						&& !champEntite.getEntite().getNom().equals("Transporteur") && !champEntite.getEntite().getNom().equals("Unite")
						&& !champEntite.getEntite().getNom().equals("ObjetStatut")){
					query.setParameter("pf", banque.getPlateforme());
				}
				final List<Object> objets = query.getResultList();
				em.close();

				// pour chaque valeur
				for(int i = 0; i < objets.size(); i++){
					String value = null;
					// si ce n'est pas un thésaurus sur les collaborateurs
					if(!champEntite.getEntite().getNom().equals("Collaborateur")
							&& !champEntite.getEntite().getNom().equals("Service")){
						// on formate le champ du thésaurus à extraire
						final String nomChamp =
								champEntite.getNom().replaceFirst(".", (champEntite.getNom().charAt(0) + "").toLowerCase());
						try{
							// extraction de la valeur de l'objet
							value = (String) PropertyUtils.getSimpleProperty(objets.get(i), nomChamp);
						}catch(final IllegalAccessException e){
							log.error(e);
						}catch(final InvocationTargetException e){
							log.error(e);
						}catch(final NoSuchMethodException e){
							log.error(e);
						}
					}else if(champEntite.getEntite().getNom().equals("Collaborateur")){
						// si c'est un collaborateur, on extrait une
						// concaténation du nom et du prénom
						value = ((Collaborateur) objets.get(i)).getNomAndPrenom();
					}else if(champEntite.getEntite().getNom().equals("Service")){
						// si c'est un service, on extrait une
						// concaténation du nom de l'établissement et du service
						final StringBuffer sb = new StringBuffer();
						final Service serv = (Service) objets.get(i);
						if(serv.getEtablissement() != null){
							sb.append(serv.getEtablissement().getNom());
							sb.append(" - ");
						}
						sb.append(serv.getNom());

						value = sb.toString();
					}

					// ajout de l'objet de la valeur string dans la
					// hashtable
					if(value != null && !values.containsKey(value)){
						valuesPut(values, value, objets.get(i), thesValueIds);
					}
				}
			}else{ //non conformites
				final Pattern p = Pattern.compile("Conforme(.*)\\.Raison");
				final Matcher m = p.matcher(champEntite.getNom());
				final boolean b = m.matches();
				if(b && m.groupCount() > 0){
					final String cNom = m.group(1);
					final Iterator<NonConformite> ncfs = nonConformiteManager
							.findByPlateformeEntiteAndTypeStringManager(banque.getPlateforme(), cNom, champEntite.getEntite()).iterator();
					NonConformite nc;
					while(ncfs.hasNext()){
						nc = ncfs.next();
						valuesPut(values, nc.getNom().toLowerCase(), nc, thesValueIds);
					}
				}
			}
		}
		return values;
	}
	
	/**
	 * Hackish méthode pour retrouver la valeur entite_id 
	 * à partir du champ_entite_id utilisé par importManager 
	 * qui correspond au QUERY_CHAMP_ID
	 * @param id
	 * @return id
	 */
	private Integer translateChpIdToFindContexteEntite(Integer id) {

		// prelevement
		if (Arrays.asList(111, 113, 116, 144, 118, 247, 257).contains(id))
			return 2;

		return id;
	}

	/**
	 * Hackish méthode pour retrouver le champ_entite_id envoyé 
	 * par le Contexte à partir du champ_entite_id utilisé par importManager 
	 * qui correspond au QUERY_CHAMP_ID
	 * @param id
	 * @return id
	 */
	private Integer translateChpIdToMatchGatsbiContexte(Integer id) {
		
		// retrouve le chpId à partir du query champ id
		// prelevement 
		if (id == 111) return 24; // nature
		if (id == 113) return 26; // consent type
		if (id == 116) return 31; // prelevement type
		if (id == 144) return 32; // condit type
		if (id == 118) return 33; // condit milieu
		if (id == 247) return 249; // risques 		
		// non conformites raisons car chpId uniquement sur booleen dans Gatsbi
		if (id == 257) return 256;
		if (id == 243) return 261;
		if (id == 244) return 262;
		if (id == 251) return 263;
		if (id == 252) return 264;		
		
		return id;
	}


	/**
	 * Ajoute la valeur de thésaurus si elle est configurée par le gestionnaire Gatsbi.
	 * @since 2.3.0-gatsbi
	 * @param values
	 * @param key
	 * @param val
	 * @param allowedIds
	 */
	private void valuesPut(Hashtable<String, Object> values, String key, Object val, List<Integer> allowedIds) {
		if (val != null) {
			if (val instanceof TKThesaurusObject) { // le filtre gatsbi s'applique
				if (allowedIds == null || allowedIds.contains(((TKThesaurusObject) val).getId())) {
					values.put(key, val);
				}
			} else { // val instanceof Service/Collaborateur/Transporteur
				values.put(key, val);
			}
		}
	}

	@Override
	public Hashtable<String, Object> extractValuesForOneAnnotationThesaurus(final ChampAnnotation champAnnotation,
			final Banque banque){
		final Hashtable<String, Object> values = new Hashtable<>();

		if(champAnnotation != null && banque != null){
			final Set<Item> items = champAnnotationManager.getItemsManager(champAnnotation, banque);
			final Iterator<Item> it = items.iterator();
			while(it.hasNext()){
				final Item i = it.next();
				values.put(i.getLabel(), i);
			}
		}

		return values;
	}

	@Override
	public Hashtable<Object, Hashtable<String, Object>> generateThesaurusHashtable(final ImportTemplate importTemplate){
		final Hashtable<Object, Hashtable<String, Object>> thesaurus = new Hashtable<>();

		if(importTemplate != null){
			// on extrait toutes les colonnes du template
			// correspondant à un thésaurus
			final List<ImportColonne> colThes = importColonneManager.findByTemplateAndThesaurusManager(importTemplate);

			// pour chaque colonne
			for(int i = 0; i < colThes.size(); i++){
				// Extraction du ChampEntite correspondant au champ
				// du thésaurus à extraire
				ChampEntite champ;
				if(!colThes.get(i).getChamp().getChampEntite().getNom().matches("Conforme.*Raison")){
					champ = colThes.get(i).getChamp().getChampEntite().getQueryChamp();
					// ajout à la hashtable
					if(!thesaurus.containsKey(champ.getEntite())){
						thesaurus.put(champ.getEntite(), extractValuesForOneThesaurus(champ, importTemplate.getBanque()));
					}
				}else{ // pas extraction du champ correspondant car plusieurs noconfs
					// convergent vers le même champ
					champ = colThes.get(i).getChamp().getChampEntite();
					// ajout à la hashtable
					if(!thesaurus.containsKey(champ.getEntite())){
						thesaurus.put(champ, extractValuesForOneThesaurus(champ, importTemplate.getBanque()));
					}
				}

			}
		}

		return thesaurus;
	}

	@Override
	public Hashtable<ChampAnnotation, Hashtable<String, Object>>
	generateAnnotationsThesaurusHashtable(final ImportTemplate importTemplate){
		final Hashtable<ChampAnnotation, Hashtable<String, Object>> thesaurus = new Hashtable<>();

		if(importTemplate != null){
			// on extrait toutes les colonnes du template
			// correspondant à un thésaurus
			final List<ImportColonne> colThes = importColonneManager.findByTemplateAndAnnotationThesaurusManager(importTemplate);

			// pour chaque colonne
			for(int i = 0; i < colThes.size(); i++){
				// Extraction du ChampAnnotation correspondant au champ
				// du thésaurus à extraire
				final ChampAnnotation champ = colThes.get(i).getChamp().getChampAnnotation();
				// ajout à la hashtable
				if(!thesaurus.containsKey(champ)){
					thesaurus.put(champ, extractValuesForOneAnnotationThesaurus(champ, importTemplate.getBanque()));
				}
			}
		}

		return thesaurus;
	}

	@Override
	public void setPropertyValueForObject(final Object value, final ChampEntite attribut, final Object obj,
			final ImportColonne colonne){
		if(attribut != null && obj != null){
			// on formate l'attribut pour qu'il corresponde à celui
			// de l'objet
			String nomChamp = attribut.getNom().replaceFirst(".", (attribut.getNom().charAt(0) + "").toLowerCase());
			if(nomChamp.endsWith("Id")){
				nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
			}

			try{
				if(value != null){
					// si la valeur à setter n'est pas un thésaurus
					if(value.getClass().getSimpleName().equals("String")){
						final String type = PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType().getSimpleName();
						// set d'un string
						if(type.equals("String")){
							PropertyUtils.setSimpleProperty(obj, nomChamp, value);
						}else if(type.equals("Integer")){
							// si l'attibut est un integer, on caste
							// la valeur issue du fichier
							Integer tmp = null;
							try{
								tmp = Integer.parseInt((String) value);
							}catch(final NumberFormatException n){
								throw new WrongImportValueException(colonne, "Integer");
							}
							PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
						}else if(type.equals("Float")){
							// si l'attibut est un float, on caste
							// la valeur issue du fichier
							Float tmp = null;
							try{
								tmp = Float.parseFloat((String) value);
								tmp = Utils.floor(tmp, 3);
							}catch(final NumberFormatException n){
								throw new WrongImportValueException(colonne, "Float");
							}
							PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
						}else if(type.equals("Boolean")){
							// si l'attibut est un boolean, on caste
							// la valeur issue du fichier
							Boolean tmp = null;
							if(value.equals("1")){
								tmp = true;
							}else if(value.equals("0")){
								tmp = false;
							}else{
								throw new WrongImportValueException(colonne, "Boolean");
							}
							PropertyUtils.setSimpleProperty(obj, nomChamp, tmp);
						}else if(type.equals("Date")){
							// si l'attibut est une date, on caste
							// la valeur issue du fichier
							Date date = null;
							final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							try{
								date = sdf.parse((String) value);
							}catch(final ParseException e){
								throw new WrongImportValueException(colonne, "Date");
							}

							if(date != null && !date.equals("")){
								final Calendar cal = Calendar.getInstance();
								cal.setTime(date);
								if(cal.get(Calendar.YEAR) > 9999){
									throw new WrongImportValueException(colonne, "Date");
								}
							}

							PropertyUtils.setSimpleProperty(obj, nomChamp, date);
						}else if(type.equals("Calendar")){
							// si l'attibut est un calendar, on caste
							// la valeur issue du fichier
							Date date = null;
							SimpleDateFormat sdf = null;
							if(((String) value).contains(":")){
								sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
							}else{
								sdf = new SimpleDateFormat("dd/MM/yyyy");
							}
							try{
								date = sdf.parse((String) value);
							}catch(final ParseException e){
								throw new WrongImportValueException(colonne, "Calendar");
							}
							Calendar cal = Calendar.getInstance();
							if(date != null){
								cal.setTime(date);
								if(cal.get(Calendar.YEAR) > 9999){
									throw new WrongImportValueException(colonne, "Calendar");
								}
							}else{
								cal = null;
							}
							PropertyUtils.setSimpleProperty(obj, nomChamp, cal);
						}
					}else{
						// si l'attibut est un thésaurus, on ajoute
						// la valeur directement
						PropertyUtils.setSimpleProperty(obj, nomChamp, value);
					}
				}else{
					// sinon on set un null
					PropertyUtils.setSimpleProperty(obj, nomChamp, null);
				}
			}catch(final IllegalAccessException e){
				log.error(e);
			}catch(final InvocationTargetException e){
				log.error(e);
			}catch(final NoSuchMethodException e){
				log.error(e);
			}
		}
	}

	@Override
	public void setPropertyValueForAnnotationValeur(final Object value, final ChampAnnotation annotation,
			final AnnotationValeur annoValeur, final ImportColonne colonne){
		if(value != null && annotation != null && annoValeur != null){
			// si la valeur à setter n'est pas un thésaurus
			if(value.getClass().getSimpleName().equals("String")){
				final DataType dt = annotation.getDataType();

				if(dt.getType().equals("alphanum") || dt.getType().equals("num") || dt.getType().equals("hyperlien")
						|| dt.getType().equals("duree")){
					annoValeur.setAlphanum((String) value);
				}else if(dt.getType().equals("boolean")){
					// si l'attibut est un boolean, on caste
					// la valeur issue du fichier
					Boolean tmp = null;
					if(value.equals("1")){
						tmp = true;
					}else if(value.equals("0")){
						tmp = false;
					}else{
						throw new WrongImportValueException(colonne, "Boolean");
					}
					annoValeur.setBool(tmp);
				}else if(dt.getType().equals("date") || dt.getType().equals("datetime")){
					// si l'attibut est un calendar, on caste
					// la valeur issue du fichier
					Date date = null;
					SimpleDateFormat sdf = null;
					if(((String) value).contains(":")){
						sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					}else{
						sdf = new SimpleDateFormat("dd/MM/yyyy");
					}
					try{
						date = sdf.parse((String) value);
					}catch(final ParseException e){
						throw new WrongImportValueException(colonne, "Calendar");
					}
					Calendar cal = Calendar.getInstance();
					if(date != null){
						cal.setTime(date);
						if(cal.get(Calendar.YEAR) > 9999){
							throw new WrongImportValueException(colonne, "Calendar");
						}
					}else{
						cal = null;
					}
					annoValeur.setDate(cal);
				}else if(dt.getType().equals("texte")){
					annoValeur.setTexte((String) value);
				}
			}else{ // Thesaurus Item
				annoValeur.setItem((Item) value);
			}
		}
	}

	@Override
	public void setPropertyForImportColonne(final Object obj, final ImportColonne colonne, final Row row,
			final ImportProperties properties){
		if(obj != null && colonne != null && row != null && properties != null){
			int ind = -1;
			// on récupère l'indice de la colonne dans le fichier
			// correspond à l'objet ImportColonne
			if(properties.getColonnesHeaders().containsKey(colonne.getNom())){
				ind = properties.getColonnesHeaders().get(colonne.getNom());
			}
			// on récupère la valeur dans le fichier
			Object value = null;
			if(ind > -1){
				// on va extraire le type de l'attibut à remplir
				String nomChamp = colonne.getChamp().getChampEntite().getNom().replaceFirst(".",
						(colonne.getChamp().getChampEntite().getNom().charAt(0) + "").toLowerCase());
				if(nomChamp.endsWith("Id")){
					nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
				}

				String type = "";
				try{
					if(PropertyUtils.getPropertyDescriptor(obj, nomChamp) != null
							&& PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType() != null){
						type = PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType().getSimpleName();
					}
				}catch(final IllegalAccessException e){
					log.error(e);
				}catch(final InvocationTargetException e){
					log.error(e);
				}catch(final NoSuchMethodException e){
					log.error(e);
				}

				// on regarde si c'est une date
				boolean isDate = false;
				if(type.equals("Date") || type.equals("Calendar")){
					isDate = true;
				}
				value = getCellContent(row.getCell(ind), isDate, properties.getEvaluator());

				// boolean corresp
				// corr 2.0.13.1
				if(type.equalsIgnoreCase("Boolean")){
					value = translateBoolValue((String) value);
				}
			}
			if(value != null && !((String) value).equals("")){
				// si le champ de la colonne est un thésaurus
				if(colonne.getChamp().getChampEntite().getQueryChamp() != null){
					// Entité liée au thésaurus
					final Entite e = colonne.getChamp().getChampEntite().getQueryChamp().getEntite();
					// on récupère la hashtable contenant les valeurs du
					// thésaurus
					if(properties.getThesaurusValues().containsKey(e)){
						final Hashtable<String, Object> thesValues = properties.getThesaurusValues().get(e);
						// on récupère l'objet correspondant à la valeur
						// présente dans le fichier
						if(thesValues.containsKey(((String) value).toUpperCase())){
							value = thesValues.get(((String) value).toUpperCase());
						}else if(thesValues.containsKey(value)){
							value = thesValues.get(value);
						}else{
							throw new WrongImportValueException(colonne, e.getNom());
						}
					}else{
						throw new WrongImportValueException(colonne, e.getNom());
					}
				}else if(colonne.getChamp().getChampEntite().getNom().equals("EmplacementId")){
					final Emplacement empl = emplacementManager.findByEmplacementAdrlManager((String) value, properties.getBanque());
					if(empl == null){
						throw new WrongImportValueException(colonne, "Emplacement");
					}
					value = empl;
				}
			}else{
				value = null;
			}
			// set de la valeur
			setPropertyValueForObject(value, colonne.getChamp().getChampEntite(), obj, colonne);
		}
	}

	@Override
	public boolean setRisquesForPrelevement(final Prelevement prlvt, final ImportColonne colonne, final Row row,
			final ImportProperties properties){
		boolean addedRisque = false;
		if(prlvt != null && colonne != null && row != null && properties != null){
			if(colonne.getChamp().getChampEntite().getNom().equals("Risques")){
				addedRisque = true;
				int ind = -1;
				// on récupère l'indice de la colonne dans le fichier
				// correspond à l'objet ImportColonne
				if(properties.getColonnesHeaders().containsKey(colonne.getNom())){
					ind = properties.getColonnesHeaders().get(colonne.getNom());
				}
				// on récupère la valeur dans le fichier
				Object value = null;
				if(ind > -1){
					value = getCellContent(row.getCell(ind), false, properties.getEvaluator());
				}
				if(value != null && !((String) value).equals("")){
					// Entité liée au thésaurus Risque
					final Entite e = entiteManager.findByIdManager(62);
					// on récupère la hashtable contenant les valeurs du
					// thésaurus
					Hashtable<String, Object> thesValues = new Hashtable<>();
					if(properties.getThesaurusValues().containsKey(e)){
						thesValues = properties.getThesaurusValues().get(e);
					}else{
						throw new WrongImportValueException(colonne, e.getNom());
					}

					// on va spliter la valeur de la colonne : les
					// risques peuvent être séparés par des virgules
					final Set<Risque> risques = new HashSet<>();
					if(((String) value).contains(";")){
						final String[] split = ((String) value).split(";");
						for(int i = 0; i < split.length; i++){
							// on récupère l'objet correspondant à la valeur
							// présente dans le fichier
							if(thesValues.containsKey(split[i].trim())){
								risques.add((Risque) thesValues.get(split[i].trim()));
							}else{
								throw new WrongImportValueException(colonne, e.getNom());
							}
						}
					}else{
						// on récupère l'objet correspondant à la valeur
						// présente dans le fichier
						if(thesValues.containsKey((value))){
							risques.add((Risque) thesValues.get(((String) value).trim()));
						}else{
							throw new WrongImportValueException(colonne, e.getNom());
						}
					}

					if(risques.size() > 0){
						prlvt.setRisques(risques);
					}
				}
			}
		}
		return addedRisque;
	}

	@Override
	public List<NonConformite> setNonConformites(final TKAnnotableObject obj, final ImportColonne colonne, final Row row,
			final ImportProperties properties, final Map<TKAnnotableObject, List<NonConformite>> ncfsList){

		final List<NonConformite> ncfs = new ArrayList<>();

		if(obj != null && colonne != null && row != null && properties != null){

			int ind = -1;
			// on récupère l'indice de la colonne dans le fichier
			// correspond à l'objet ImportColonne
			if(properties.getColonnesHeaders().containsKey(colonne.getNom())){
				ind = properties.getColonnesHeaders().get(colonne.getNom());
			}
			// on récupère la valeur dans le fichier
			Object value = null;
			if(ind > -1){
				value = getCellContent(row.getCell(ind), false, properties.getEvaluator());
			}
			if(value != null && !((String) value).equals("")){
				// Entité liée au thésaurus NonConformite
				final ChampEntite e = colonne.getChamp().getChampEntite();
				// on récupère la hashtable contenant les valeurs du
				// thésaurus
				Hashtable<String, Object> thesValues = new Hashtable<>();
				if(properties.getThesaurusValues().containsKey(e)){
					thesValues = properties.getThesaurusValues().get(e);
				}else{
					throw new WrongImportValueException(colonne, e.getNom());
				}

				// on va spliter la valeur de la colonne : les
				// non conformites peuvent être séparés par des points-virgules
				if(((String) value).contains(";")){
					final String[] split = ((String) value).split(";");
					for(int i = 0; i < split.length; i++){
						// on récupère l'objet correspondant à la valeur
						// présente dans le fichier
						if(thesValues.containsKey(split[i].trim().toLowerCase())){
							ncfs.add((NonConformite) thesValues.get(split[i].trim().toLowerCase()));
						}else{
							throw new WrongImportValueException(colonne, e.getNom());
						}
					}
				}else{
					// on récupère l'objet correspondant à la valeur
					// présente dans le fichier
					if(thesValues.containsKey(((String) value).trim().toLowerCase())){
						ncfs.add((NonConformite) thesValues.get(((String) value).trim().toLowerCase()));
					}else{
						throw new WrongImportValueException(colonne, e.getNom());
					}
				}
			}
		}
		ncfsList.put(obj, ncfs);

		return ncfs;
	}

	@Override
	public boolean setCodeAssigneForEchantillon(final Echantillon echan, final ImportColonne colonne, final Row row,
			final ImportProperties properties){
		boolean addedCode = false;
		if(echan != null && colonne != null && row != null && properties != null){
			if(colonne.getChamp().getChampEntite().getNom().equals("CodeOrganes")
					|| colonne.getChamp().getChampEntite().getNom().equals("CodeMorphos")){
				addedCode = true;
				int ind = -1;
				// on récupère l'indice de la colonne dans le fichier
				// correspond à l'objet ImportColonne
				if(properties.getColonnesHeaders().containsKey(colonne.getNom())){
					ind = properties.getColonnesHeaders().get(colonne.getNom());
				}
				// on récupère la valeur dans le fichier
				Object value = null;
				if(ind > -1){
					value = getCellContent(row.getCell(ind), false, properties.getEvaluator());
				}
				if(value != null && !((String) value).equals("")){
					final String total = (String) value;
					String code = null;
					String libelle = null;
					if(total.contains("{") && total.contains("}")){
						libelle = total.substring(total.indexOf("{") + 1, total.indexOf("}"));
						code = total.substring(0, total.indexOf("{"));
						if(code.endsWith(" ")){
							code = code.substring(0, code.length() - 1);
						}
					}else{
						code = total;
					}

					if((code == null || code.equals("")) && (libelle != null && !libelle.equals(""))){
						code = libelle;
					}

					final CodeAssigne codeAssigne = new CodeAssigne();
					codeAssigne.setCode(code);
					codeAssigne.setLibelle(libelle);
					codeAssigne.setOrdre(1);
					codeAssigne.setExport(true);
					echan.getCodesAssignes().add(codeAssigne);

					if(colonne.getChamp().getChampEntite().getNom().equals("CodeOrganes")){
						codeAssigne.setIsOrgane(true);
						codeAssigne.setIsMorpho(false);
						//echan.setCodeOrganeExport(codeAssigne);
					}else{
						codeAssigne.setIsOrgane(false);
						codeAssigne.setIsMorpho(true);
						//echan.setCodeLesExport(codeAssigne);
					}
				}
			}
		}
		return addedCode;
	}

	@Override
	public List<AnnotationValeur> setPropertyForAnnotationColonne(final ImportColonne colonne, final Row row,
			final ImportProperties properties){

		final List<AnnotationValeur> annoVals = new ArrayList<>();

		if(colonne != null && row != null && properties != null){
			int ind = -1;
			// on récupère l'indice de la colonne dans le fichier
			// correspond à l'objet ImportColonne
			if(properties.getColonnesHeaders().containsKey(colonne.getNom())){
				ind = properties.getColonnesHeaders().get(colonne.getNom());
			}
			// on récupère la valeur dans le fichier
			Object value = null;
			if(ind > -1){

				final DataType dt = colonne.getChamp().getChampAnnotation().getDataType();
				boolean isDate = false;
				if(dt.getType().matches("date.*")){
					isDate = true;
				}

				value = getCellContent(row.getCell(ind), isDate, properties.getEvaluator());

				// boolean corresp
				if(dt.getType().matches("boolean")){
					value = translateBoolValue((String) value);
				}
			}

			if(value != null && !((String) value).equals("")){
				// si le champ de la colonne est un thésaurus
				if(colonne.getChamp().getChampAnnotation().getDataType().getType().equals("thesaurus")){
					// on récupère la hashtable contenant les valeurs du
					// thésaurus
					if(properties.getAnnotationThesaurusValues().containsKey(colonne.getChamp().getChampAnnotation())){
						final Hashtable<String, Object> thesValues =
								properties.getAnnotationThesaurusValues().get(colonne.getChamp().getChampAnnotation());
						// on récupère l'objet correspondant à la valeur
						// présente dans le fichier
						Object itemVal = null;
						for(final String key : thesValues.keySet()){
							if(key.equalsIgnoreCase((String) value)){
								itemVal = thesValues.get(key);
								break;
							}
						}
						// found in keyset
						if(itemVal != null){
							value = itemVal;
						}else{ // annot value not found!
							throw new WrongImportValueException(colonne, colonne.getChamp().getChampAnnotation().getNom());
						}
					}else{ // annot champ items not found!
						throw new WrongImportValueException(colonne, colonne.getChamp().getChampAnnotation().getNom());
					}
					// since 2.0.13.2
				}else if(colonne.getChamp().getChampAnnotation().getDataType().getType().equals("thesaurusM")){
					// on récupère la hashtable contenant les valeurs du
					// thésaurus
					if(properties.getAnnotationThesaurusValues().containsKey(colonne.getChamp().getChampAnnotation())){
						final Hashtable<String, Object> thesValues =
								properties.getAnnotationThesaurusValues().get(colonne.getChamp().getChampAnnotation());

						// on va spliter la valeur de la colonne : les
						// valeurs peuvent être séparés par des points-virgules
						Object itemVal = null;
						final String[] split = ((String) value).split(";");
						for(int i = 0; i < split.length; i++){

							// présente dans le fichier
							// on récupère l'item correspondant à la valeur
							// présente dans le fichier
							for(final String key : thesValues.keySet()){
								if(key.equalsIgnoreCase(split[i].trim())){
									itemVal = thesValues.get(key);
									break;
								}
							}
							// found in keyset
							if(itemVal != null){
								final AnnotationValeur av = new AnnotationValeur();
								setPropertyValueForAnnotationValeur(itemVal, colonne.getChamp().getChampAnnotation(), av, colonne);
								annoVals.add(av);
							}else{ // annot value not found!
								throw new WrongImportValueException(colonne, colonne.getChamp().getChampAnnotation().getNom());
							}
						}
					}else{ // annot champ items not found!
						throw new WrongImportValueException(colonne, colonne.getChamp().getChampAnnotation().getNom());
					}
					return annoVals;
				}
			}else{
				value = null;
			}

			// s'il y a une valeur pour l'annotation
			if(value != null){
				//	|| properties.getImportTemplate().getIsUpdate()) {
				final AnnotationValeur av = new AnnotationValeur();
				setPropertyValueForAnnotationValeur(value, colonne.getChamp().getChampAnnotation(), av, colonne);
				annoVals.add(av);
			}
		}
		return annoVals;
	}

	private String translateBoolValue(final String value){
		if(value != null){
			if(value.equalsIgnoreCase("OUI") || value.equalsIgnoreCase("O") || value.equalsIgnoreCase("YES")
					|| value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("VRAI") || value.equalsIgnoreCase("V")
					|| value.equalsIgnoreCase("TRUE")){
				return "1";
			}else if(value.equalsIgnoreCase("NON") || value.equalsIgnoreCase("N") || value.equalsIgnoreCase("NO")
					|| value.equalsIgnoreCase("FAUX") || value.equalsIgnoreCase("F") || value.equalsIgnoreCase("FALSE")){
				return "0";
			}
		}
		return value;
	}

	@Override
	public TKAnnotableObjectDuo setAllPropertiesForPatient(final Row row, final ImportProperties properties){

		/*****************/
		final TKAnnotableObjectDuo duo = new TKAnnotableObjectDuo();
		duo.setEntite(entiteManager.findByIdManager(1));
		/*****************/

		Patient patient = null;

		if(row != null && properties != null){
			// nouveau patient
			patient = new Patient();
			/************************/
			duo.setFirstObj(patient);
			List<ImportColonne> colonnes = new ArrayList<>();
			/************************/
			// on récupère les colonnes liées au patient
			if(properties.getColonnesForEntites().containsKey(duo.getEntite())){
				colonnes = properties.getColonnesForEntites().get(duo.getEntite());
				// pour chaque colonne, on set la valeur
				for(int i = 0; i < colonnes.size(); i++){
					// si la colonne correspond à un attribut
					// de l'objet
					if(colonnes.get(i).getChamp().getChampEntite() != null){
						setPropertyForImportColonne(patient, colonnes.get(i), row, properties);
					}else if(colonnes.get(i).getChamp().getChampAnnotation() != null){
						// si la colonne correspond à une annotation
						final List<AnnotationValeur> avs = setPropertyForAnnotationColonne(colonnes.get(i), row, properties);
						for(final AnnotationValeur av : avs){
							av.setChampAnnotation(colonnes.get(i).getChamp().getChampAnnotation());
							av.setBanque(properties.getBanque());
							// avs.add(av);
						}
						duo.getFirstAnnoValsMap().put(colonnes.get(i).getChamp().getChampAnnotation(), avs);
					}
				}
			}
			// on regarde si le patient existe deja en base
			if(patientManager.findDoublonManager(patient)){

				final Patient existingPat = patientManager.getExistingPatientManager(patient);

				if(patient.equals(existingPat)){
					patient = existingPat;
				}
				// }

				// update en modification -> creation du duo
				duo.setSecondObj(existingPat);

				for(final ChampAnnotation chpA : duo.getFirstAnnoValsMap().keySet()){
					duo.getSecondAnnoValsMap().put(chpA, annotationValeurManager.findByChampAndObjetManager(chpA, existingPat));
				}
				/*************************/
			}
		}

		/***********/
		return duo;
		/**********/
	}

	@Override
	public Maladie setAllPropertiesForMaladie(final Row row, final ImportProperties properties, final Patient patient){
		Maladie maladie = null;
		if(row != null && properties != null){
			// nouvealle maladie
			maladie = new Maladie();
			maladie.setPatient(patient);
			final Entite e = entiteManager.findByNomManager("Maladie").get(0);
			List<ImportColonne> colonnes = new ArrayList<>();
			// on récupère les colonnes liées à la maladie
			if(properties.getColonnesForEntites().containsKey(e)){
				colonnes = properties.getColonnesForEntites().get(e);
				// pour chaque colonne, on set la valeur
				for(int i = 0; i < colonnes.size(); i++){
					setPropertyForImportColonne(maladie, colonnes.get(i), row, properties);
				}
			}
			// on regarde si la maladie existe deja en base
			if(maladieManager.findDoublonManager(maladie, patient)){
				// @since 2.2.3-genno optimisation = recherche la maladie par patient
				final List<Maladie> mals = maladieManager.findByLibelleAndPatientManager(maladie.getLibelle(), patient);

				for(int i = 0; i < mals.size(); i++){
					Maladie m = mals.get(i);
					if(!m.getClass().getSimpleName().equals("Maladie")){
						m = maladieDao.mergeObject(mals.get(i));
					}
					if(maladie.equals(m)){
						maladie = m;
					}
				}
			}
		}

		return maladie;
	}

	@Override
	public TKAnnotableObjectDuo setAllPropertiesForPrelevement(final Row row, final ImportProperties properties,
			final Maladie maladie){

		final TKAnnotableObjectDuo duo = new TKAnnotableObjectDuo();
		duo.setEntite(entiteManager.findByIdManager(2));

		Prelevement prlvt = null;

		if(row != null && properties != null){
			// nouveau prlvt
			prlvt = new Prelevement();
			duo.setFirstObj(prlvt);
			prlvt.setMaladie(maladie);
			prlvt.setBanque(properties.getBanque());
			List<ImportColonne> colonnes = new ArrayList<>();
			// on récupère les colonnes liées au prlvt
			if(properties.getColonnesForEntites().containsKey(duo.getEntite())){
				colonnes = properties.getColonnesForEntites().get(duo.getEntite());
				// pour chaque colonne, on set la valeur
				for(int i = 0; i < colonnes.size(); i++){
					// si la colonne correspond à un attribut
					// de l'objet
					if(colonnes.get(i).getChamp().getChampEntite() != null){

						// non conformites
						if(colonnes.get(i).getChamp().getChampEntite().getNom().matches("Conforme.*Raison")){
							if(ncfsPrelevement == null){ // init la liste
								ncfsPrelevement = new HashMap<>();
							}

							duo.getFirstNoConfs().addAll(setNonConformites(prlvt, colonnes.get(i), row, properties, ncfsPrelevement));

							// cette méthode teste si le champ est un risque
							// si c'est la cas, il sera ajouté au prlvt
							// sinon on continue la procédure normale
						}else if(!setRisquesForPrelevement(prlvt, colonnes.get(i), row, properties)){
							setPropertyForImportColonne(prlvt, colonnes.get(i), row, properties);
						}
					}else if(colonnes.get(i).getChamp().getChampAnnotation() != null){
						// si la colonne correspond à une annotation
						final List<AnnotationValeur> avs = setPropertyForAnnotationColonne(colonnes.get(i), row, properties);
						// List<AnnotationValeur> avs = new ArrayList<AnnotationValeur>();
						for(final AnnotationValeur av : avs){
							av.setChampAnnotation(colonnes.get(i).getChamp().getChampAnnotation());
							av.setBanque(properties.getBanque());
							// annotations.add(av);
							// avs.add(av);
						}
						duo.getFirstAnnoValsMap().put(colonnes.get(i).getChamp().getChampAnnotation(), avs);
					}
				}

				// on regarde si le prlvt existe deja en base
				if(prelevementManager.findDoublonManager(prlvt)){
					final List<Prelevement> prlvts =
							prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager(prlvt.getCode(), prlvt.getBanque(), true);
					Prelevement existingPrel = null;

					// TODO getExistintPrelevementManager cf getExistingPatientManager!!

					for(int i = 0; i < prlvts.size(); i++){
						existingPrel = prlvts.get(i);
					}
					// update en modification -> creation du duo
					duo.setSecondObj(existingPrel);
					duo.getSecondNoConfs().addAll(
							nonConformiteManager.getFromObjetNonConformes(objetNonConformeManager.findByObjetManager(existingPrel)));

					for(final ChampAnnotation chpA : duo.getFirstAnnoValsMap().keySet()){
						duo.getSecondAnnoValsMap().put(chpA, annotationValeurManager.findByChampAndObjetManager(chpA, existingPrel));
					}

				}
			}

		}
		return duo;
	}

	@Override
	public Echantillon setAllPropertiesForEchantillon(final Row row, final ImportProperties properties, final Prelevement prlvt){
		Echantillon echan = null;
		if(row != null && properties != null){
			// nouvel échantillon
			echan = new Echantillon();
			echan.setPrelevement(prlvt);
			echan.setBanque(properties.getBanque());
			final Entite e = entiteManager.findByNomManager("Echantillon").get(0);
			List<ImportColonne> colonnes = new ArrayList<>();
			// on récupère les colonnes liées à l'échantillon
			if(properties.getColonnesForEntites().containsKey(e)){
				colonnes = properties.getColonnesForEntites().get(e);
				final List<AnnotationValeur> annotations = new ArrayList<>();
				// pour chaque colonne, on set la valeur
				for(int i = 0; i < colonnes.size(); i++){
					// si la colonne correspond à un attribut
					// de l'objet
					if(colonnes.get(i).getChamp().getChampEntite() != null){

						if(colonnes.get(i).getChamp().getChampEntite().getNom().equals("ConformeTraitement.Raison")){
							if(ncfsEchanTrait == null){ // init la liste
								ncfsEchanTrait = new HashMap<>();
							}
							setNonConformites(echan, colonnes.get(i), row, properties, ncfsEchanTrait);
						}else if(colonnes.get(i).getChamp().getChampEntite().getNom().equals("ConformeCession.Raison")){
							if(ncfsEchanCess == null){ // init la liste
								ncfsEchanCess = new HashMap<>();
							}
							setNonConformites(echan, colonnes.get(i), row, properties, ncfsEchanCess);
							// cette méthode teste si le champ est un code assigné
							// si c'est la cas, il sera ajouté au prlvt
							// sinon on continue la procédure normale
						}else if(!setCodeAssigneForEchantillon(echan, colonnes.get(i), row, properties)){
							setPropertyForImportColonne(echan, colonnes.get(i), row, properties);
						}

					}else if(colonnes.get(i).getChamp().getChampAnnotation() != null){
						// si la colonne correspond à une annotation
						final List<AnnotationValeur> avs = setPropertyForAnnotationColonne(colonnes.get(i), row, properties);
						for(final AnnotationValeur av : avs){
							av.setChampAnnotation(colonnes.get(i).getChamp().getChampAnnotation());
							av.setBanque(properties.getBanque());
							annotations.add(av);
						}
					}
				}
				if(annotations.size() > 0){
					// on met la liste d'annotations dans la hashtable
					properties.getAnnotationsEntite().put(e, annotations);
				}
			}

			// on place le bon statut si il est vide
			if(echan.getObjetStatut() == null){
				if(echan.getEmplacement() != null){
					echan.setObjetStatut(objetStatutManager.findByIdManager(1));
				}else{
					echan.setObjetStatut(objetStatutManager.findByIdManager(4));
				}
			}

			// @since 2.2.2
			// champs calculés
			// delai de stockage
			// long -> float implicit casting
			if (echan.getDelaiCgl() == null) {
				float delaiStock = echantillonManager.calculDelaiStockage(echan, echan.getPrelevement()); 
				if (delaiStock > 0) {
					echan.setDelaiCgl(delaiStock / 60000);
				}
			}

			// on regarde si l'échantillon existe deja en base
			if(echantillonManager.findDoublonManager(echan)){
				final List<Echantillon> liste =
						echantillonManager.findByCodeLikeWithBanqueManager(echan.getCode(), echan.getBanque(), true);

				for(int i = 0; i < liste.size(); i++){
					Echantillon ech = liste.get(i);
					if(!ech.getClass().getSimpleName().equals("Echantillon")){
						ech = echantillonDao.mergeObject(liste.get(i));
					}
					if(echan.equals(ech)){
						echan = ech;
					}
				}
			}

		}
		return echan;
	}

	@Override
	public ProdDerive setAllPropertiesForProdDerive(final Row row, final ImportProperties properties){
		ProdDerive derive = null;
		if(row != null && properties != null){
			// nouvel échantillon
			derive = new ProdDerive();
			derive.setBanque(properties.getBanque());

			final Entite e = entiteManager.findByNomManager("ProdDerive").get(0);
			List<ImportColonne> colonnes = new ArrayList<>();
			// on récupère les colonnes liées au dérivé
			if(properties.getColonnesForEntites().containsKey(e)){
				colonnes = properties.getColonnesForEntites().get(e);
				final List<AnnotationValeur> annotations = new ArrayList<>();
				// pour chaque colonne, on set la valeur
				for(int i = 0; i < colonnes.size(); i++){
					// si la colonne correspond à un attribut
					// de l'objet
					if(colonnes.get(i).getChamp().getChampEntite() != null){
						// non conformites
						if(!colonnes.get(i).getChamp().getChampEntite().getNom().matches("Conforme.*Raison")){

							setPropertyForImportColonne(derive, colonnes.get(i), row, properties);
						}else if(colonnes.get(i).getChamp().getChampEntite().getNom().equals("ConformeTraitement.Raison")){
							if(ncfsDeriveTrait == null){ // init la liste
								ncfsDeriveTrait = new HashMap<>();
							}
							setNonConformites(derive, colonnes.get(i), row, properties, ncfsDeriveTrait);
						}else if(colonnes.get(i).getChamp().getChampEntite().getNom().equals("ConformeCession.Raison")){
							if(ncfsDeriveCess == null){ // init la liste
								ncfsDeriveCess = new HashMap<>();
							}
							setNonConformites(derive, colonnes.get(i), row, properties, ncfsDeriveCess);
						}

					}else if(colonnes.get(i).getChamp().getChampAnnotation() != null){
						// si la colonne correspond à une annotation
						final List<AnnotationValeur> avs = setPropertyForAnnotationColonne(colonnes.get(i), row, properties);
						for(final AnnotationValeur av : avs){
							av.setChampAnnotation(colonnes.get(i).getChamp().getChampAnnotation());
							av.setBanque(properties.getBanque());
							annotations.add(av);
						}
					}
				}
				if(annotations.size() > 0){
					// on met la liste d'annotations dans la hashtable
					properties.getAnnotationsEntite().put(e, annotations);
				}
			}

			// on place le bon statut si il est vide
			if(derive.getObjetStatut() == null){
				if(derive.getEmplacement() != null){
					derive.setObjetStatut(objetStatutManager.findByIdManager(1));
				}else{
					derive.setObjetStatut(objetStatutManager.findByIdManager(4));
				}
			}

			// on regarde si le dérivé existe deja en base
			if(prodDeriveManager.findDoublonManager(derive)){
				final List<ProdDerive> liste =
						prodDeriveManager.findByCodeOrLaboWithBanqueManager(derive.getCode(), derive.getBanque(), true);

				for(int i = 0; i < liste.size(); i++){
					ProdDerive p = liste.get(i);
					if(!p.getClass().getSimpleName().equals("ProdDerive")){
						p = prodDeriveDao.mergeObject(liste.get(i));
					}
					if(derive.equals(p)){
						derive = p;
					}
				}
			}
		}
		return derive;
	}

	@Override
	public void saveObjectsRowManager(final Row row, final List<Object> objects, final List<Importation> importations,
			final Utilisateur utilisateur, final ImportProperties properties, final EchantillonJdbcSuite jdbcSuite,
			final List<DerivesImportBatches> derivesBatches){

		if(objects != null){
			try{
				
				// gatsbi required validation
				// TODO indispensable pour l'instant que pour prepareObjectJDBCManager validation
				// donc échantillons uniquement
				// car sinon la validation gatsbi s'applique directement dans create/update Manager
				List<Integer> requiredChampEntiteIds = new ArrayList<Integer>();
				if (properties.getBanque().getEtude() != null) {
					Contexte echanContexte = properties.getBanque().getEtude().getContexteForEntite(3);
					if (echanContexte != null) {
						requiredChampEntiteIds.addAll(echanContexte.getRequiredChampEntiteIds());
					}
				}

				final List<AnnotationValeur> avs = new ArrayList<>();
				final List<AnnotationValeur> toDelete = new ArrayList<>();
				final List<AnnotationValeur> toUpdate = new ArrayList<>();

				TKAnnotableObject parent = null;
				for(int i = 0; i < objects.size(); i++){
					avs.clear();
					toDelete.clear();
					toUpdate.clear();
					if(objects.get(i).getClass().getSimpleName().equals("TKAnnotableObjectDuo")
							&& ((TKAnnotableObjectDuo) objects.get(i)).getEntite().getNom().equals("Patient")){
						final TKAnnotableObjectDuo patDuo = (TKAnnotableObjectDuo) objects.get(i);

						Patient pat = null;
						// existing object absent
						if(patDuo.getSecondObj() == null){
							pat = (Patient) patDuo.getFirstObj();
							parent = pat;
						}else{
							parent = patDuo.getSecondObj();
						}

						// import modification
						if(properties.getImportTemplate().getIsUpdate() && patDuo.getSecondObj() != null){
							if(tkAnnotableDuoManager.mergeDuoObjectsManager(patDuo,
									tkAnnotableDuoManager.compareObjectsDuoManager(patDuo,
											champEntiteDao.findByImportTemplateAndEntite(properties.getImportTemplate(), patDuo.getEntite()),
											champAnnotationManager.findByImportTemplateAndEntiteManager(properties.getImportTemplate(),
													patDuo.getEntite())))){
								pat = (Patient) patDuo.getSecondObj();
							}
						}

						if(pat != null){
							toUpdate.addAll(patDuo.getFirstAnnoVals());
							toDelete.addAll(patDuo.getSecondAnnoVals());

							// Préparation de l'opération d'importation
							// en attente de l'id
							final Importation imp = new Importation();
							imp.setEntite(patDuo.getEntite());
							imp.setIsUpdate(pat.getPatientId() != null);

							patientManager.createOrUpdateObjectManager(pat, null, null, null, !toUpdate.isEmpty() ? toUpdate : null,
									!toDelete.isEmpty() ? toDelete : null, null, null, utilisateur,
											pat.getPatientId() == null ? "creation" : "modification", null, true);

							imp.setObjetId(pat.getPatientId());
							importations.add(imp);
						}
					}else if(objects.get(i).getClass().getSimpleName().equals("Maladie")){
						final Maladie mal = (Maladie) objects.get(i);
						if(mal.getMaladieId() == null){
							maladieManager.createOrUpdateObjectManager(mal, mal.getPatient(), null, utilisateur, "creation");
						}

					}else if(objects.get(i).getClass().getSimpleName().equals("TKAnnotableObjectDuo")
							&& ((TKAnnotableObjectDuo) objects.get(i)).getEntite().getNom().equals("Prelevement")){
						// Prelevement prlvt = (Prelevement) objects.get(i);
						final TKAnnotableObjectDuo prlvtDuo = (TKAnnotableObjectDuo) objects.get(i);

						Prelevement prlvt = null;
						// existing object absent
						if(prlvtDuo.getSecondObj() == null){
							prlvt = (Prelevement) prlvtDuo.getFirstObj();
							parent = prlvt;
						}else{
							parent = prlvtDuo.getSecondObj();
						}

						// import modification
						if(properties.getImportTemplate().getIsUpdate() && prlvtDuo.getSecondObj() != null){
							if(tkAnnotableDuoManager.mergeDuoObjectsManager(prlvtDuo,
									tkAnnotableDuoManager.compareObjectsDuoManager(prlvtDuo,
											champEntiteDao.findByImportTemplateAndEntite(properties.getImportTemplate(), prlvtDuo.getEntite()),
											champAnnotationManager.findByImportTemplateAndEntiteManager(properties.getImportTemplate(),
													prlvtDuo.getEntite())))){
								prlvt = (Prelevement) prlvtDuo.getSecondObj();
							}
						}

						if(prlvt != null){
							toUpdate.addAll(prlvtDuo.getFirstAnnoVals());

							// Préparation de l'opération d'importation
							// en attente de l'id
							final Importation imp = new Importation();
							imp.setEntite(prlvtDuo.getEntite());
							imp.setIsUpdate(prlvt.getPrelevementId() != null);

							// create
							if(prlvt.getPrelevementId() == null){
								prelevementManager.createObjectWithNonConformitesManager(prlvt, null, prlvt.getNature(),
										prlvt.getMaladie(), prlvt.getConsentType(), prlvt.getPreleveur(), prlvt.getServicePreleveur(),
										prlvt.getPrelevementType(), prlvt.getConditType(), prlvt.getConditMilieu(), prlvt.getTransporteur(),
										prlvt.getOperateur(), prlvt.getQuantiteUnite(), null, !toUpdate.isEmpty() ? toUpdate : null,
												utilisateur, true, null, true, ncfsPrelevement != null ? ncfsPrelevement.get(prlvt) : null);
							}else{ // update Objet

								toDelete.addAll(prlvtDuo.getSecondAnnoVals());

								prelevementManager.updateObjectWithNonConformitesManager(prlvt, prlvt.getBanque(), prlvt.getNature(),
										prlvt.getMaladie(), prlvt.getConsentType(), prlvt.getPreleveur(), prlvt.getServicePreleveur(),
										prlvt.getPrelevementType(), prlvt.getConditType(), prlvt.getConditMilieu(), prlvt.getTransporteur(),
										prlvt.getOperateur(), prlvt.getQuantiteUnite(), null, !toUpdate.isEmpty() ? toUpdate : null,
												!toDelete.isEmpty() ? toDelete : null, utilisateur, null, true, null, true,
														ncfsPrelevement != null ? ncfsPrelevement.get(prlvt) : null);
							}
							imp.setObjetId(prlvt.getPrelevementId());
							importations.add(imp);
						}
					}else if(objects.get(i).getClass().getSimpleName().equals("Echantillon")){
						final Echantillon echan = (Echantillon) objects.get(i);
						parent = echan;
						if(echan.getEchantillonId() == null){
							// on récupère les annotations à créer
							// s'il en existe
							final Entite eEchan = entiteManager.findByNomManager("Echantillon").get(0);
							if(properties.getAnnotationsEntite().containsKey(eEchan)
									&& properties.getAnnotationsEntite().get(eEchan) != null){
								avs.addAll(properties.getAnnotationsEntite().get(eEchan));
								for(final AnnotationValeur av : avs){
									if(!av.isEmpty()){
										toUpdate.add(av);
									}
								}
							}

							final Emplacement empl = echan.getEmplacement();
							// si un emplacement est donné
							if(empl != null){
								if(empl.getEmplacementId() != null){
									// on vérifie qu'il est vide
									if(!empl.getVide()){
										throw new UsedPositionException("Echantillon", "Import", empl.getPosition());
									}
								}else{
									// s'il n'existait pas on le crée
									emplacementManager.createObjectManager(empl, empl.getTerminale(), null);
								}
							}
							// si aucun statut n'a été donné, on met
							// le statut à stocké
							if(empl != null && echan.getObjetStatut() == null){
								final ObjetStatut statut = objetStatutManager.findByStatutLikeManager("STOCKE", true).get(0);
								echan.setObjetStatut(statut);
							}

							if(empl == null && echan.getObjetStatut() == null){
								final ObjetStatut statut = objetStatutManager.findByStatutLikeManager("NON STOCKE", true).get(0);
								echan.setObjetStatut(statut);
							}

							final List<CodeAssigne> codes = new ArrayList<>(echan.getCodesAssignes());
							echan.setCodesAssignes(null);

							// gestion de la qté
							if(echan.getQuantite() == null && echan.getQuantiteInit() != null){
								echan.setQuantite(echan.getQuantiteInit());
							}

							Integer echanId = null;

							// since 2.0.10.6
							// bascule en jdbcInsert pour augmenter la rapidite
							// d'insertions
							if(jdbcSuite == null){
								echantillonManager.createObjectWithNonConformitesManager(echan, echan.getBanque(), echan.getPrelevement(),
										echan.getCollaborateur(), echan.getObjetStatut(), empl, echan.getEchantillonType(), codes,
										echan.getQuantiteUnite(), echan.getEchanQualite(), echan.getModePrepa(), null, null,
										!toUpdate.isEmpty() ? toUpdate : null, utilisateur, true, null, true,
												ncfsEchanTrait != null ? ncfsEchanTrait.get(echan) : null,
														ncfsEchanCess != null ? ncfsEchanCess.get(echan) : null);
								// maj de l'emplacement
								if(empl != null){
									empl.setObjetId(echan.getEchantillonId());
									// empl.setVide(false);
									emplacementManager.updateObjectManager(empl, empl.getTerminale(), eEchan);
								}
							}else{

								echanId = echantillonManager.prepareObjectJDBCManager(jdbcSuite,
										echan, echan.getBanque(), echan.getPrelevement(), echan.getCollaborateur(), echan.getObjetStatut(),
										empl, echan.getEchantillonType(), echan.getQuantiteUnite(), echan.getEchanQualite(),
										echan.getModePrepa(), codes, !toUpdate.isEmpty() ? toUpdate : null,
												ncfsEchanTrait != null ? ncfsEchanTrait.get(echan) : null,
												ncfsEchanCess != null ? ncfsEchanCess.get(echan) : null, utilisateur, true, true, 
												requiredChampEntiteIds);

								// echan.setEchantillonId(echanId);
							}

							// Création de l'opération d'importation
							final Importation imp = new Importation();
							imp.setObjetId(echanId == null ? echan.getEchantillonId() : echanId);
							imp.setEntite(eEchan);
							importations.add(imp);
						}
					}else if(objects.get(i).getClass().getSimpleName().equals("ProdDerive")){

						if(((ProdDerive) objects.get(i)).getProdDeriveId() != null && parent == null){ // dérivé parent
							// pb si import modif derive derivant echantillon par exemple !!!
							parent = (ProdDerive) objects.get(i);
						}else if(((ProdDerive) objects.get(i)).getProdDeriveId() == null){
							addToDeriveBatchesManager(row, (ProdDerive) objects.get(i), derivesBatches, parent, null, null, null,
									properties);
						}
					}

				}
			}catch(final Exception e){
				// rollback create operation
				throw new RuntimeException(e);
			}finally{

			}
		}
	}

	@Override
	public void addToDeriveBatchesManager(final Row row, final ProdDerive derive, final List<DerivesImportBatches> batches,
			final TKAnnotableObject parent, final Float transfoQte, final Calendar dateSortie, final String observations,
			final ImportProperties properties){

		// on récupère les annotations à créer
		// s'il en existe
		List<AnnotationValeur> avs = new ArrayList<>();
		final List<AnnotationValeur> toUpdate = new ArrayList<>();
		final Entite eDerive = entiteManager.findByNomManager("ProdDerive").get(0);
		if(properties.getAnnotationsEntite().containsKey(eDerive) && properties.getAnnotationsEntite().get(eDerive) != null){
			avs = properties.getAnnotationsEntite().get(eDerive);
			for(final AnnotationValeur av : avs){
				if(!av.isEmpty()){
					toUpdate.add(av);
				}
			}
		}else{
			avs = null;
		}

		final Emplacement empl = derive.getEmplacement();
		// si un emplacement est donné
		if(empl != null){
			if(empl.getEmplacementId() != null){
				// on vérifie qu'il est vide
				if(!empl.getVide()){
					throw new UsedPositionException("ProdDerive", "Import", empl.getPosition());
				}
			}
		}
		// si aucun statut n'a été donné, on met
		// le statut à stocké
		if(empl != null && derive.getObjetStatut() == null){
			final ObjetStatut statut = objetStatutManager.findByStatutLikeManager("STOCKE", true).get(0);
			derive.setObjetStatut(statut);
		}

		if(empl == null && derive.getObjetStatut() == null){
			final ObjetStatut statut = objetStatutManager.findByStatutLikeManager("NON STOCKE", true).get(0);
			derive.setObjetStatut(statut);
		}

		// gestion de la qté
		if(derive.getQuantite() == null && derive.getQuantiteInit() != null){
			derive.setQuantite(derive.getQuantiteInit());
		}
		// gestion du volume
		if(derive.getVolume() == null && derive.getVolumeInit() != null){
			derive.setVolume(derive.getVolumeInit());
		}

		//	if (parent != null) {	
		DerivesImportBatches deriveBatch = new DerivesImportBatches(parent, transfoQte, observations, dateSortie);
		if(batches.contains(deriveBatch)){ // un batch de dérivés existe déja pour cette transformation
			deriveBatch = batches.get(batches.indexOf(deriveBatch));
		}else{
			batches.add(deriveBatch);
		}
		deriveBatch.getImportRow().put(derive, row);
		deriveBatch.getDerives().add(derive);
		if(empl != null){
			deriveBatch.getEmplacements().put(derive, empl);
		}
		if(!toUpdate.isEmpty()){
			deriveBatch.getAnnoVals().put(derive, toUpdate);
		}
		if(ncfsDeriveTrait != null && ncfsDeriveTrait.containsKey(derive)){
			deriveBatch.getNcfsTrait().put(derive, ncfsDeriveTrait.get(derive));
		}
		if(ncfsDeriveCess != null && ncfsDeriveCess.containsKey(derive)){
			deriveBatch.getNcfsCess().put(derive, ncfsDeriveCess.get(derive));
		}
		//	}
	}

	@Override
	public void saveDeriveBatchesManager(final List<DerivesImportBatches> batches, final ImportProperties properties,
			final List<Importation> importations, final Utilisateur utilisateur, final String baseDir, final List<ImportError> errors){

		if(batches != null){
			final Entite deriveEntite = entiteManager.findByNomManager("ProdDerive").get(0);
			DerivesImportBatches currBatch = null;
			for(final DerivesImportBatches batch : batches){
				try{
					currBatch = batch;
					prodDeriveManager.createProdDerivesManager(batch.getDerives(), properties.getBanque(), utilisateur,
							batch.getParent(), batch.getAnnoVals(), batch.getEmplacements(), batch.getTransfoQte(),
							batch.getTransfoQteUnite(), batch.getDateSortie(), batch.getObservations(), baseDir, true, batch.getNcfsTrait(),
							batch.getNcfsCess());

					for(final ProdDerive derive : batch.getDerives()){
						// Création des opération d'importations
						final Importation imp = new Importation();
						imp.setObjetId(derive.getProdDeriveId());
						imp.setEntite(deriveEntite);
						importations.add(imp);
					}
				}catch(final DeriveBatchSaveException re){
					final ProdDerive deriveError = re.getDeriveInError();

					if(null != currBatch && deriveError != null){
						final ImportError error = new ImportError();
						error.setException(re.getTargetExeption());
						error.setRow(currBatch.getImportRow().get(deriveError));
						error.setNbRow(currBatch.getImportRow().get(deriveError).getRowNum());
						errors.add(error);
					}else{
						throw re;
					}
				}
			}
		}
	}

	@Override
	public List<String> extractListOfStringFromExcelFile(final InputStream fis, final boolean fixNulls){

		final List<String> res = new ArrayList<>();

		Sheet sheet;
		Iterator<Row> rit;
		Row row = null;

		// ouvre fichier xls
		try( Workbook wb = WorkbookFactory.create(fis);){

			sheet = wb.getSheetAt(0);
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();

			while(rit.hasNext()){
				row = rit.next();
				if(getCellContent(row.getCell(0), false, null) != null){
					res.add(getCellContent(row.getCell(0), false, null));
				}else if(!fixNulls){
					res.add(getCellContent(row.getCell(0), false, null));
				}
			}

		}catch(final Exception e){
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Renvoie false si une des cellules n'est pas vide
	 * @param row
	 * @return
	 */
	private boolean isEmptyRow(final Row row){
		final boolean res = true;
		for(int i = 0; i < row.getLastCellNum(); i++){
			if(row.getCell(i) != null && row.getCell(i).getCellTypeEnum() != CellType.BLANK){
				return false;
			}
		}
		return res;

	}

	@Override
	public ImportHistorique importFileManager(final ImportTemplate importTemplate,
			final Utilisateur utilisateur,
			final Banque banque,
			final InputStream is){
		//SXSSFWorkbook wb = null;
		try( Workbook wb = WorkbookFactory.create(is);){
			//wb = new SXSSFWorkbook( new XSSFWorkbook(is), 100);
			return importFileManager(importTemplate, utilisateur, banque, wb.getSheetAt(0));
		}catch(final IOException e){
			e.printStackTrace();
		}catch(final InvalidFormatException e){
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Map<TKAnnotableObject, List<NonConformite>> getNcfsPrelevement(){
		return ncfsPrelevement;
	}

	@Override
	public Map<TKAnnotableObject, List<NonConformite>> getNcfsEchanTrait(){
		return ncfsEchanTrait;
	}

	@Override
	public Map<TKAnnotableObject, List<NonConformite>> getNcfsEchanCess(){
		return ncfsEchanCess;
	}

	@Override
	public Map<TKAnnotableObject, List<NonConformite>> getNcfsDeriveTrait(){
		return ncfsDeriveTrait;
	}

	@Override
	public Map<TKAnnotableObject, List<NonConformite>> getNcfsDeriveCess(){
		return ncfsDeriveCess;
	}

	@Override
	public ImportHistorique importFileManager(final ImportTemplate importTemplate, 
			final Utilisateur utilisateur,
			final Banque banque,
			final Sheet sheet){ //TODO Refactorer
		final ImportProperties properties = new ImportProperties();
		properties.setBanque(banque != null ? banque : importTemplate.getBanque());
		final List<ImportError> errors = new ArrayList<>();
		ImportHistorique historique = null;
		final List<Importation> importations = new ArrayList<>();
		final List<DerivesImportBatches> derivesBatches = new ArrayList<>();

		Iterator<Row> rit;
		Row row = null;

		ncfsPrelevement = null;
		ncfsEchanTrait = null;
		ncfsEchanCess = null;
		ncfsDeriveTrait = null;
		ncfsDeriveCess = null;

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		EchantillonJdbcSuite jdbcSuite = null;

		try{
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = rit.next();

			// init de toutes les hashtables
			properties.setColonnesHeaders(initColumnsHeadersManager(row));
			properties.setColonnesForEntites(initImportColonnesManager(properties.getColonnesHeaders(), importTemplate));
			properties.setThesaurusValues(generateThesaurusHashtable(importTemplate));
			properties.setAnnotationThesaurusValues(generateAnnotationsThesaurusHashtable(importTemplate));
			properties.setImportTemplate(importTemplate);

			// since 2.0.10.6 FormulaEvaluator
			properties.setEvaluator(sheet.getWorkbook().getCreationHelper().createFormulaEvaluator());

			final Entite ePatient = entiteManager.findByNomManager("Patient").get(0);
			final Entite eMaladie = entiteManager.findByNomManager("Maladie").get(0);
			final Entite ePrlvt = entiteManager.findByNomManager("Prelevement").get(0);
			final Entite eEchan = entiteManager.findByNomManager("Echantillon").get(0);
			final Entite eDerive = entiteManager.findByNomManager("ProdDerive").get(0);

			// bascule automatiquement en mode jdbc insertion si nb lignes > 300
			// 2.0.10.6 ne concerne que les échantillons si pas de derives enfants
			final boolean jdbcInsert = sheet.getLastRowNum() > 300 && !properties.getColonnesForEntites().containsKey(eDerive);

			List<Object> objectsToSave = new ArrayList<>();

			if(jdbcInsert){
				conn = DataSourceUtils.getConnection(dataSource);
				jdbcSuite = new EchantillonJdbcSuite();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select max(echantillon_id) from ECHANTILLON");
				if(rs.next()){
					jdbcSuite.setMaxEchantillonId(rs.getInt(1));
				}else{
					jdbcSuite.setMaxEchantillonId(1);
				}
				// Integer maxEchId = ids.getMaxEchantillonId();
				rs.close();
				rs = stmt.executeQuery("select max(annotation_valeur_id) from ANNOTATION_VALEUR");
				if(rs.next()){
					jdbcSuite.setMaxAnnotationValeurId(rs.getInt(1));
				}else{
					jdbcSuite.setMaxAnnotationValeurId(1);
				}
				// Integer maxAnnoId = ids.getMaxAnnotationValeurId();
				rs.close();
				rs = stmt.executeQuery("select max(code_assigne_id) from CODE_ASSIGNE");
				if(rs.next()){
					jdbcSuite.setMaxCodeAssigneId(rs.getInt(1));
				}else{
					jdbcSuite.setMaxCodeAssigneId(1);
				}
				// Integer maxCdId = ids.getMaxCodeAssigneId();
				rs.close();
				rs = stmt.executeQuery("select max(objet_non_conforme_id) from OBJET_NON_CONFORME");
				if(rs.next()){
					jdbcSuite.setMaxObjetNonConformeId(rs.getInt(1));
				}else{
					jdbcSuite.setMaxObjetNonConformeId(1);
				}
				// Integer maxOncId = ids.getMaxObjetNonConformeId();
				rs.close();

				final String sql = "insert into ECHANTILLON (ECHANTILLON_ID, " + "BANQUE_ID, ECHANTILLON_TYPE_ID, OBJET_STATUT_ID, "
						+ "PRELEVEMENT_ID, COLLABORATEUR_ID, QUANTITE_UNITE_ID, " + "ECHAN_QUALITE_ID, MODE_PREPA_ID, EMPLACEMENT_ID, "
						+ "CODE, DATE_STOCK, QUANTITE, QUANTITE_INIT, " + "LATERALITE, DELAI_CGL, " + "TUMORAL, STERILE, "
						+ "CONFORME_TRAITEMENT, CONFORME_CESSION, " + "ETAT_INCOMPLET, ARCHIVE) "
						+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				jdbcSuite.setPstmt(conn.prepareStatement(sql));

				final String sql2 = "insert into OPERATION (UTILISATEUR_ID, " + "OBJET_ID, ENTITE_ID, OPERATION_TYPE_ID, "
						+ "DATE_, V1)" + "values (?,?,?,?,?,?)";
				jdbcSuite.setPstmtOp(conn.prepareStatement(sql2));

				final String sql3 = "insert into CODE_ASSIGNE (CODE_ASSIGNE_ID, ECHANTILLON_ID, "
						+ "CODE, LIBELLE, CODE_REF_ID, TABLE_CODAGE_ID, IS_ORGANE, " + "IS_MORPHO, ORDRE, EXPORT) "
						+ "values (?,?,?,?,?,?,?,?,?,?)";
				jdbcSuite.setPstmtCd(conn.prepareStatement(sql3));

				final String sql4 =
						"insert into ANNOTATION_VALEUR (ANNOTATION_VALEUR_ID, " + "CHAMP_ANNOTATION_ID, OBJET_ID, BANQUE_ID, "
								+ "ALPHANUM, BOOL, ANNO_DATE, " + "TEXTE, ITEM_ID) " + "values (?,?,?,?,?,?,?,?,?)";
				jdbcSuite.setPstmtAnno(conn.prepareStatement(sql4));

				final String sql5 = "insert into OBJET_NON_CONFORME (OBJET_NON_CONFORME_ID, "
						+ "OBJET_ID, ENTITE_ID, NON_CONFORMITE_ID) " + "values (?,?,?,?)";
				jdbcSuite.setPstmtNc(conn.prepareStatement(sql5));
			}

			int i = 0;

			while(rit.hasNext()){
				i++;
				row = rit.next();
				properties.setAnnotationsEntite(new Hashtable<Entite, List<AnnotationValeur>>());
				// List<Object> objectsToSave = new ArrayList<Object>();
				objectsToSave.clear();
				TKAnnotableObjectDuo patDuo = null;
				Maladie maladie = null;
				TKAnnotableObjectDuo prlvtDuo = null;
				Echantillon echantillon = null;
				ProdDerive derive = null;
				try{
					// teste si la row is empty
					if(!isEmptyRow(row)){
						if(properties.getColonnesForEntites().containsKey(ePatient)){
							patDuo = setAllPropertiesForPatient(row, properties);
							objectsToSave.add(patDuo);
						}
						if(properties.getColonnesForEntites().containsKey(eMaladie)){
							maladie = setAllPropertiesForMaladie(row, properties, patDuo != null
									? ((Patient) (patDuo.getSecondObj() != null ? patDuo.getSecondObj() : patDuo.getFirstObj())) : null);
							objectsToSave.add(maladie);
						}
						if(properties.getColonnesForEntites().containsKey(ePrlvt)){
							prlvtDuo = setAllPropertiesForPrelevement(row, properties, maladie);
							objectsToSave.add(prlvtDuo);
						}
						if(properties.getColonnesForEntites().containsKey(eEchan)){
							echantillon = setAllPropertiesForEchantillon(row, properties,
									prlvtDuo != null
									? ((Prelevement) (prlvtDuo.getSecondObj() != null ? prlvtDuo.getSecondObj() : prlvtDuo.getFirstObj()))
											: null);
							objectsToSave.add(echantillon);
						}
						if(properties.getColonnesForEntites().containsKey(eDerive)){
							derive = setAllPropertiesForProdDerive(row, properties);
							objectsToSave.add(derive);
						}
					} 
				}catch(final WrongImportValueException wve){
					final ImportError error = new ImportError();
					error.setException(wve);
					error.setRow(row);
					error.setNbRow(row.getRowNum());
					errors.add(error);
					objectsToSave = new ArrayList<>();
				}
				try{
					if(objectsToSave.size() > 0){

						saveObjectsRowManager(row, objectsToSave, importations, utilisateur, properties, jdbcSuite, derivesBatches);
					}
				}catch(final RuntimeException re){
					re.printStackTrace();
					final ImportError error = new ImportError();
					error.setException(re);
					error.setRow(row);
					error.setNbRow(row.getRowNum());
					errors.add(error);
				}
				if(errors.isEmpty() && jdbcSuite != null && (i % 500 == 0)){
					jdbcSuite.executeBatches();
					jdbcSuite.clearBatches();
				}
			}
			if(errors.isEmpty() && jdbcSuite != null){
				jdbcSuite.executeBatches();
				jdbcSuite.clearBatches();
			}

			// enregistrement des batches de derives peuplés après lecture de 
			// l'ensemble des lignes du fichier importé
			// baseDir null car l'import tabulé ne permet pas l'import de fichier
			if(!derivesBatches.isEmpty()){
				saveDeriveBatchesManager(derivesBatches, properties, importations, utilisateur, null, errors);
			}

			if(errors.isEmpty() && !importations.isEmpty()){
				historique = new ImportHistorique();
				historique.setImportTemplate(importTemplate);
				historique.setUtilisateur(utilisateur);
				historique.setDate(Calendar.getInstance());
				importHistoriqueManager.createObjectManager(historique, importTemplate, utilisateur, importations);
			}
		}catch(final BadFileFormatException bdfe){
			final ImportError error = new ImportError();
			error.setException(bdfe);
			error.setRow(row);
			error.setNbRow(row.getRowNum());
			errors.add(error);
		}catch(final HeaderException he){
			final ImportError error = new ImportError();
			error.setException(he);
			errors.add(error);
		}catch(final FormulaException fe){
			final ImportError error = new ImportError();
			error.setException(fe);
			errors.add(error);
		}catch(final Exception e){
			e.printStackTrace();
			final ImportError error = new ImportError();
			error.setException(new RuntimeException(e));
			error.setNbRow(row.getRowNum());
			errors.add(error);
		}finally{
			if(null != rs){
				try{
					rs.close();
				}catch(SQLException e){
					rs = null;
					log.error(e);
				}
			}
			if(stmt != null){
				try{
					stmt.close();
				}catch(final Exception ex){
					stmt = null;
				}
			}
			if(conn != null){
				try{
					conn.close();
				}catch(final Exception ex){
					conn = null;
				}
			}
			if(jdbcSuite != null){
				jdbcSuite.closePs();
			}
		}

		if(!errors.isEmpty()){
			throw new ErrorsInImportException(errors);
		}

		return historique;
	}

	@Override
	public ImportHistorique importSubDeriveFileManager(final ImportTemplate importTemplate, 
			final Utilisateur utilisateur,
			final Banque banque,
			final Sheet sheet, final String retourTransfoEpuisement){ //TODO Refactorer
		final ImportProperties properties = new ImportProperties();
		properties.setBanque(banque != null ? banque : importTemplate.getBanque());
		final List<ImportError> errors = new ArrayList<>();
		ImportHistorique historique = null;
		final List<Importation> importations = new ArrayList<>();
		final List<DerivesImportBatches> derivesBatches = new ArrayList<>();

		Iterator<Row> rit;
		Row row = null;

		ncfsDeriveTrait = null;
		ncfsDeriveCess = null;

		// ouvre fichier xls
		try{
			// on se positionne sur la première ligne
			rit = sheet.rowIterator();
			row = rit.next();

			// init de toutes les hashtables
			properties.setColonnesHeaders(initColumnsHeadersManager(row));
			properties.setColonnesForEntites(initImportColonnesManager(properties.getColonnesHeaders(), importTemplate));
			properties.setThesaurusValues(generateThesaurusHashtable(importTemplate));
			properties.setAnnotationThesaurusValues(generateAnnotationsThesaurusHashtable(importTemplate));

			// init subderive spec colonnes			
			final ImportColonne icCodeParent = new ImportColonne();
			icCodeParent.setNom("code.parent");
			icCodeParent.setOrdre(0);
			icCodeParent.setImportTemplate(importTemplate);
			properties.getSubDerivesCols().add(icCodeParent);
			final ImportColonne icTrQte = new ImportColonne();
			icTrQte.setNom("qte.transf");
			icTrQte.setOrdre(0);
			icTrQte.setChamp(new Champ(champEntiteDao.findById(239)));
			icTrQte.setImportTemplate(importTemplate);
			properties.getSubDerivesCols().add(icTrQte);
			final ImportColonne icDs = new ImportColonne();
			icDs.setNom("evt.date");
			icDs.setOrdre(0);
			icDs.setImportTemplate(importTemplate);
			properties.getSubDerivesCols().add(icDs);

			// since 2.0.10.6 FormulaEvaluator
			properties.setEvaluator(sheet.getWorkbook().getCreationHelper().createFormulaEvaluator());
			final Entite eDerive = entiteManager.findByNomManager("ProdDerive").get(0);

			final List<Object> objectsToSave = new ArrayList<>();

			// int i = 0;

			TKAnnotableObject parentDerive = null; // représenté par son code
			Float transfoQuantite = null;
			// Unite transfoQuantiteUnite = null;
			Calendar dateSortie = null;
			ProdDerive childDerive = null;

			while(rit.hasNext()){
				//	i++;
				row = rit.next();
				properties.setAnnotationsEntite(new Hashtable<Entite, List<AnnotationValeur>>());
				objectsToSave.clear();
				// colonnes transformation propres aux imports dérivés
				parentDerive = null; // représenté par son code
				transfoQuantite = null;
				//	transfoQuantiteUnite = null;
				dateSortie = null;
				childDerive = null;
				try{
					// teste si la row is empty
					if(!isEmptyRow(row)){
						if(properties.getColonnesForEntites().containsKey(eDerive)){
							// trouve le derive parent, sinon erreur!						
							parentDerive = findParentDeriveInRow(row, properties, importTemplate.getDeriveParentEntite());
							// trouve les informations de transformation si présentes
							// col2 = transfo quantite
							String cellContent = getCellContent(row.getCell(1), false, properties.getEvaluator());
							if(cellContent != null && !cellContent.trim().equals("")){
								try{
									transfoQuantite = Float.parseFloat(cellContent);
									transfoQuantite = Utils.floor(transfoQuantite, 3);
								}catch(final NumberFormatException n){
									throw new WrongImportValueException(properties.getSubDerivesCols().get(1), "Float");
								}
							}
							cellContent = getCellContent(row.getCell(2), true, properties.getEvaluator());
							if(cellContent != null && !cellContent.trim().equals("")){
								try{
									SimpleDateFormat sdf;
									if(cellContent.contains(":")){
										sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
									}else{
										sdf = new SimpleDateFormat("dd/MM/yyyy");
									}
									dateSortie = Calendar.getInstance();
									dateSortie.setTime(sdf.parse(cellContent));
								}catch(final Exception n){
									throw new WrongImportValueException(properties.getSubDerivesCols().get(2), "Calendar");
								}
							}

							childDerive = setAllPropertiesForProdDerive(row, properties);
							addToDeriveBatchesManager(row, childDerive, derivesBatches, parentDerive, transfoQuantite, dateSortie,
									retourTransfoEpuisement, properties);
						}
					}
				}catch(final RuntimeException wve){
					final ImportError error = new ImportError();
					error.setException(wve);
					error.setRow(row);
					error.setNbRow(row.getRowNum());
					errors.add(error);
					objectsToSave.clear();
				}

			}

			// enregistrement des batches de derives peuplés après lecture de 
			// l'ensemble des lignes du fichier importé
			// baseDir null car l'import tabulé ne permet pas l'import de fichier
			if(!derivesBatches.isEmpty()){
				saveDeriveBatchesManager(derivesBatches, properties, importations, utilisateur, null, errors);
			}

			if(errors.isEmpty() && !importations.isEmpty()){
				historique = new ImportHistorique();
				historique.setImportTemplate(importTemplate);
				historique.setUtilisateur(utilisateur);
				historique.setDate(Calendar.getInstance());
				importHistoriqueManager.createObjectManager(historique, importTemplate, utilisateur, importations);
			}
		}catch(final BadFileFormatException bdfe){
			final ImportError error = new ImportError();
			error.setException(bdfe);
			error.setRow(row);
			error.setNbRow(row.getRowNum());
			errors.add(error);
		}catch(final HeaderException he){
			final ImportError error = new ImportError();
			error.setException(he);
			errors.add(error);
		}catch(final FormulaException fe){
			final ImportError error = new ImportError();
			error.setException(fe);
			errors.add(error);
		}catch(final Exception e){
			e.printStackTrace();
			final ImportError error = new ImportError();
			error.setException(new RuntimeException(e));
			error.setNbRow(row.getRowNum());
			errors.add(error);
		}finally{}

		if(!errors.isEmpty()){
			throw new ErrorsInImportException(errors);
		}

		return historique;
	}

	/**
	 * Extrait le code parent derive 'fixé' par la première colonnes selon 
	 * les specifications  de l'import de dérivé de dérivé afin de trouver 
	 * en base le dérivé parent?
	 * Throws runtime exception si parent not found
	 * @param row
	 * @param ImportProperties
	 * @param Entite subDerive parent
	 * @return TKObject parent
	 */
	private TKAnnotableObject findParentDeriveInRow(final Row row, final ImportProperties props, final Entite deriveParentEntite){
		final String code = getCellContent(row.getCell(0), false, props.getEvaluator());
		if(deriveParentEntite != null){
			final List<TKAnnotableObject> liste = new ArrayList<>();
			if(deriveParentEntite.getNom().equals("Prelevement")){
				liste.addAll(prelevementManager.findByCodeOrNumLaboLikeWithBanqueManager(code, props.getBanque(), true));
			}else if(deriveParentEntite.getNom().equals("Echantillon")){
				liste.addAll(echantillonManager.findByCodeLikeWithBanqueManager(code, props.getBanque(), true));
			}else if(deriveParentEntite.getNom().equals("ProdDerive")){
				liste.addAll(prodDeriveManager.findByCodeOrLaboWithBanqueManager(code, props.getBanque(), true));
			}
			if(!liste.isEmpty()){
				return liste.get(0);
			}
		}
		final DeriveImportParentNotFoundException dpnfe =
				new DeriveImportParentNotFoundException(props.getSubDerivesCols().get(0), code, props.getBanque());
		throw dpnfe;
	}
}
