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
package fr.aphp.tumorotek.manager.impl.impression;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.manager.impression.TemplateManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.impression.TemplateValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.systeme.Entite;

public class TemplateManagerImpl implements TemplateManager {
	
	private Log log = LogFactory.getLog(TemplateManager.class);
	
	/** Bean Dao. */
	private TemplateDao templateDao;
	/** Bean Dao. */
	private BanqueDao banqueDao;
	/** Bean Dao. */
	private EntiteDao entiteDao;
	/** Bean validator. */
	private TemplateValidator templateValidator;
	/** Bean Manager. */
	private BlocImpressionTemplateManager blocImpressionTemplateManager;
	/** Bean Manager. */
	private ChampImprimeManager champImprimeManager;
	/** Bean Manager. */
	private TableAnnotationTemplateManager tableAnnotationTemplateManager;

	public void setTemplateDao(TemplateDao tDao) {
		this.templateDao = tDao;
	}

	public void setBanqueDao(BanqueDao bDao) {
		this.banqueDao = bDao;
	}

	public void setEntiteDao(EntiteDao eDao) {
		this.entiteDao = eDao;
	}

	public void setTemplateValidator(TemplateValidator tValidator) {
		this.templateValidator = tValidator;
	}

	public void setBlocImpressionTemplateManager(
			BlocImpressionTemplateManager bManager) {
		this.blocImpressionTemplateManager = bManager;
	}

	public void setChampImprimeManager(ChampImprimeManager cManager) {
		this.champImprimeManager = cManager;
	}

	public void setTableAnnotationTemplateManager(
			TableAnnotationTemplateManager tManager) {
		this.tableAnnotationTemplateManager = tManager;
	}

	@Override
	public Template findByIdManager(Integer templateId) {
		return templateDao.findById(templateId);
	}
	
	@Override
	public List<Template> findAllObjectsManager() {
		log.debug("Recherche de tous les templates.");
		return templateDao.findAll();
	}
	
	@Override
	public List<Template> findByBanqueManager(Banque banque) {
		log.debug("Recherche de tous les templates d'une banque.");
		if (banque != null) {
			return templateDao.findByBanque(banque);
		} else {
			return new ArrayList<Template>();
		}
	}

	@Override
	public List<Template> findByBanqueEntiteManager(
			Banque banque, Entite entite) {
		log.debug("Recherche de tous les templates d'une banque pour " 
				+ "une entité.");
		if (banque != null && entite != null) {
			return templateDao.findByBanqueEntite(banque, entite);
		} else {
			return new ArrayList<Template>();
		}
	}

	@Override
	public Boolean findDoublonManager(Template template) {
		if (template != null) {
			if (template.getTemplateId() == null) {
				return templateDao.findByBanque(template.getBanque())
					.contains(template);
			} else {
				return templateDao.findByExcludedId(template.getBanque(), 
						template.getTemplateId()).contains(template);
			}
		} else {
			return false;
		}
	}
	
	@Override
	public void createObjectManager(Template template, Banque banque,
			Entite entite, List<BlocImpressionTemplate> blocs,
			List<ChampImprime> champs, 
			List<TableAnnotationTemplate> annotations) {
		
		// banque required
		if (banque != null) { 
			template.setBanque(banqueDao.mergeObject(banque));
		} else {
			log.warn("Objet obligatoire Banque manquant"
							+ " lors de la création d'un Template");
			throw new RequiredObjectIsNullException(
					"Template", "creation", "Banque");
		}
		
		// entite required
		if (entite != null) { 
			template.setEntite(entiteDao.mergeObject(entite));
		} else {
			log.warn("Objet obligatoire Entite manquant"
							+ " lors de la création d'un Template");
			throw new RequiredObjectIsNullException(
					"Template", "creation", "Entite");
		}
		
		//Doublon
		if (!findDoublonManager(template)) {
			
			// validation due l'utilisateur
			BeanValidator.validateObject(
					template, new Validator[]{templateValidator});
			
			// validation des blocs
			if (blocs != null) {
				for (int i = 0; i < blocs.size(); i++) {
					BlocImpressionTemplate obj = blocs.get(i);
					
					blocImpressionTemplateManager
						.validateObjectManager(template, 
									obj.getBlocImpression());
				}
			}
			
			// validation des champs
			if (champs != null) {
				for (int i = 0; i < champs.size(); i++) {
					ChampImprime obj = champs.get(i);
					
					champImprimeManager.validateObjectManager(template, 
							obj.getChampEntite(), obj.getBlocImpression());
				}
			}
			
			// validation des tableannotation
			if (annotations != null) {
				for (int i = 0; i < annotations.size(); i++) {
					TableAnnotationTemplate obj = annotations.get(i);
					
					tableAnnotationTemplateManager.validateObjectManager(
							template, obj.getTableAnnotation());
				}
			}
			
			templateDao.createObject(template);
			log.info("Enregistrement objet Template " 
					+ template.toString());
			
			// enregistrements des associations
			updateAssociations(template, blocs, blocs, 
					champs, champs, annotations, annotations);
				
		} else {
			log.warn("Doublon lors creation objet Template "
					+ template.toString());
			throw new DoublonFoundException("Template", "creation");
		}
	}

	@Override
	public void updateObjectManager(Template template, 
			Banque banque,
			Entite entite,
			List<BlocImpressionTemplate> blocs,
			List<BlocImpressionTemplate> blocsToCreate,
			List<ChampImprime> champs,
			List<ChampImprime> champsToCreate,
			List<TableAnnotationTemplate> annotations,
			List<TableAnnotationTemplate> annotationsToCreate) {
		
		// banque required
		if (banque != null) { 
			template.setBanque(banqueDao.mergeObject(banque));
		} else {
			log.warn("Objet obligatoire Banque manquant"
							+ " lors de la modification d'un Template");
			throw new RequiredObjectIsNullException(
					"Template", "modification", "Banque");
		}
		
		// entite required
		if (entite != null) { 
			template.setEntite(entiteDao.mergeObject(entite));
		} else {
			log.warn("Objet obligatoire Entite manquant"
							+ " lors de la modification d'un Template");
			throw new RequiredObjectIsNullException(
					"Template", "modification", "Entite");
		}
		
		//Doublon
		if (!findDoublonManager(template)) {
			
			// validation due l'utilisateur
			BeanValidator.validateObject(
					template, new Validator[]{templateValidator});
			
			// validation des blocs
			if (blocsToCreate != null) {
				for (int i = 0; i < blocsToCreate.size(); i++) {
					BlocImpressionTemplate obj = blocsToCreate.get(i);
					
					blocImpressionTemplateManager
						.validateObjectManager(template, 
									obj.getBlocImpression());
				}
			}
			
			// validation des champs
			if (champsToCreate != null) {
				for (int i = 0; i < champsToCreate.size(); i++) {
					ChampImprime obj = champsToCreate.get(i);
					
					champImprimeManager.validateObjectManager(template, 
							obj.getChampEntite(), obj.getBlocImpression());
				}
			}
			
			// validation des tableannotation
			if (annotationsToCreate != null) {
				for (int i = 0; i < annotationsToCreate.size(); i++) {
					TableAnnotationTemplate obj = annotationsToCreate
						.get(i);
					
					tableAnnotationTemplateManager.validateObjectManager(
							template, obj.getTableAnnotation());
				}
			}
			
			templateDao.updateObject(template);
			log.info("Enregistrement objet Template " 
					+ template.toString());
			
			// enregistrements des associations
			updateAssociations(template, blocs, blocsToCreate, 
					champs, champsToCreate, 
					annotations, annotationsToCreate);
				
		} else {
			log.warn("Doublon lors creation objet Template "
					+ template.toString());
			throw new DoublonFoundException("Template", "modification");
		}
		
	}

	@Override
	public void removeObjectManager(Template template) {
		if (template != null) {
			// suppression des BlocImpressionTemplates
			List<BlocImpressionTemplate> blocs = blocImpressionTemplateManager
				.findByTemplateManager(template);
			for (int i = 0; i < blocs.size(); i++) {
				blocImpressionTemplateManager.removeObjectManager(blocs.get(i));
			}
			
			// suppression des ChampImprimes
			List<ChampImprime> champs = champImprimeManager
				.findByTemplateManager(template);
			for (int i = 0; i < champs.size(); i++) {
				champImprimeManager.removeObjectManager(champs.get(i));
			}
			
			// suppression des TableAnnotationTemplate
			List<TableAnnotationTemplate> tables = 
				tableAnnotationTemplateManager
				.findByTemplateManager(template);
			for (int i = 0; i < tables.size(); i++) {
				tableAnnotationTemplateManager
					.removeObjectManager(tables.get(i));
			}
			
			templateDao.removeObject(template.getTemplateId());
			log.info("Suppression de l'objet Template : " 
					+ template.toString());	
		} else {
			log.warn("Suppression d'un Template null");
		}
	}
	
	public void updateAssociations(Template template, 
			List<BlocImpressionTemplate> blocs,
			List<BlocImpressionTemplate> blocsToCreate,
			List<ChampImprime> champs,
			List<ChampImprime> champsToCreate,
			List<TableAnnotationTemplate> annotations,
			List<TableAnnotationTemplate> annoToCreate) {
		Template temp = templateDao.mergeObject(template);
		
		// gestion des BlocImpressionTemplate
		List<BlocImpressionTemplate> oldBlocs = 
			new ArrayList<BlocImpressionTemplate>();
		List<BlocImpressionTemplate> blocsToRemove = 
			new ArrayList<BlocImpressionTemplate>();
		if (blocs != null) {
			oldBlocs = blocImpressionTemplateManager
				.findByTemplateManager(temp);
			for (int i = 0; i < oldBlocs.size(); i++) {
				if (!blocs.contains(oldBlocs.get(i))) {
					blocsToRemove.add(oldBlocs.get(i));
				}
			}
			
			for (int i = 0; i < blocsToRemove.size(); i++) {
				blocImpressionTemplateManager.removeObjectManager(
						blocsToRemove.get(i));
			}
			
			if (blocsToCreate != null) {
				// enregistrements
				for (int i = 0; i < blocsToCreate.size(); i++) {
					BlocImpressionTemplate obj = blocsToCreate.get(i);
					blocImpressionTemplateManager.createObjectManager(obj, 
							template, obj.getBlocImpression());
				}
				
				// update
				for (int i = 0; i < blocs.size(); i++) {
					if (!blocsToCreate.contains(blocs.get(i))) {
						blocImpressionTemplateManager.updateObjectManager(
								blocs.get(i), template, 
								blocs.get(i).getBlocImpression());
					}
				}
			} else {
				// update
				for (int i = 0; i < blocs.size(); i++) {
					blocImpressionTemplateManager.updateObjectManager(
							blocs.get(i), template, 
							blocs.get(i).getBlocImpression());
				}
			}
		}
		
		// gestion des ChampImprimes
		List<ChampImprime> oldChamps = new ArrayList<ChampImprime>();
		List<ChampImprime> champsToRemove = 
			new ArrayList<ChampImprime>();
		if (champs != null) {
			oldChamps = champImprimeManager.findByTemplateManager(temp);
			for (int i = 0; i < oldChamps.size(); i++) {
				if (!champs.contains(oldChamps.get(i))) {
					champsToRemove.add(oldChamps.get(i));
				}
			}
			
			for (int i = 0; i < champsToRemove.size(); i++) {
				champImprimeManager.removeObjectManager(
						champsToRemove.get(i));
			}
			
			if (champsToCreate != null) {
				// enregistrements 
				for (int i = 0; i < champsToCreate.size(); i++) {
					ChampImprime obj = champsToCreate.get(i);
					champImprimeManager.createObjectManager(obj, 
							template, obj.getChampEntite(),
							obj.getBlocImpression());
				}
				
				// update
				for (int i = 0; i < champs.size(); i++) {
					if (!champsToCreate.contains(champs.get(i))) {
						champImprimeManager.updateObjectManager(
								champs.get(i), template, 
								champs.get(i).getChampEntite(), 
								champs.get(i).getBlocImpression());
					}
				}
			} else {
				// update
				for (int i = 0; i < champs.size(); i++) {
					champImprimeManager.updateObjectManager(
							champs.get(i), template, 
							champs.get(i).getChampEntite(), 
							champs.get(i).getBlocImpression());
				}
			}
		}
		
		// gestion des TableAnnotationTemplate
		List<TableAnnotationTemplate> oldTables = 
			new ArrayList<TableAnnotationTemplate>();
		List<TableAnnotationTemplate> tablesToRemove = 
			new ArrayList<TableAnnotationTemplate>();
		if (annotations != null) {
			oldTables = tableAnnotationTemplateManager
				.findByTemplateManager(temp);
			for (int i = 0; i < oldTables.size(); i++) {
				if (!annotations.contains(oldTables.get(i))) {
					tablesToRemove.add(oldTables.get(i));
				}
			}
			
			for (int i = 0; i < tablesToRemove.size(); i++) {
				tableAnnotationTemplateManager.removeObjectManager(
						tablesToRemove.get(i));
			}
			
			if (annoToCreate != null) {
				// enregistrements 
				for (int i = 0; i < annoToCreate.size(); i++) {
					TableAnnotationTemplate obj = annoToCreate.get(i);
					tableAnnotationTemplateManager.createObjectManager(obj, 
							template, obj.getTableAnnotation());
				}
				// update
				for (int i = 0; i < annotations.size(); i++) {
					if (!annoToCreate.contains(annotations.get(i))) {
						tableAnnotationTemplateManager.updateObjectManager(
								annotations.get(i), template, 
								annotations.get(i).getTableAnnotation());
					}
				}
			} else {
				// update
				for (int i = 0; i < annotations.size(); i++) {
					tableAnnotationTemplateManager.updateObjectManager(
							annotations.get(i), template, 
							annotations.get(i).getTableAnnotation());
				}
			}
		}
	}
}
