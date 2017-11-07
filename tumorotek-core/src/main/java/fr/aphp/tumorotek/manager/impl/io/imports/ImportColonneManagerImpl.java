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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.annotation.DataTypeDao;
import fr.aphp.tumorotek.dao.io.imports.ImportColonneDao;
import fr.aphp.tumorotek.dao.io.imports.ImportTemplateDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.imports.ImportColonneManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.imports.ImportColonneValidator;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.imports.ImportColonne;
import fr.aphp.tumorotek.model.io.imports.ImportTemplate;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ImportColonneManagerImpl implements ImportColonneManager {
	
	private Log log = LogFactory.getLog(ImportColonneManager.class);
	
	/** Beans. */
	private ImportColonneDao importColonneDao;
	private ImportTemplateDao importTemplateDao;
	private ChampManager champManager;
	private ImportColonneValidator importColonneValidator;
	private DataTypeDao dataTypeDao;

	public void setImportColonneDao(ImportColonneDao iDao) {
		this.importColonneDao = iDao;
	}

	public void setImportTemplateDao(ImportTemplateDao iDao) {
		this.importTemplateDao = iDao;
	}

	public void setChampManager(ChampManager cManager) {
		this.champManager = cManager;
	}

	public void setImportColonneValidator(
			ImportColonneValidator iValidator) {
		this.importColonneValidator = iValidator;
	}

	public void setDataTypeDao(DataTypeDao dDao) {
		this.dataTypeDao = dDao;
	}

	@Override
	public ImportColonne findByIdManager(Integer importColonneId) {
		return importColonneDao.findById(importColonneId);
	}
	
	@Override
	public List<ImportColonne> findAllObjectsManager() {
		log.debug("Recherche de tous les ImportColonnes.");
		return importColonneDao.findAll();
	}
	
	@Override
	public List<ImportColonne> findByImportTemplateManager(
			ImportTemplate template) {
		if (template != null) {
			log.debug("Recherche de tous les ImportColonnes " 
					+ "d'un ImportTemplate.");
			return importColonneDao.findByTemplateWithOrder(template);
		} else {
			return new ArrayList<ImportColonne>();
		}
	}

	@Override
	public List<ImportColonne> findByTemplateAndDataTypeManager(
			ImportTemplate importTemplate, DataType dataType) {
		if (importTemplate != null && dataType != null) {
			return importColonneDao.findByTemplateAndDataType(
					importTemplate, dataType);
		} else {
			return new ArrayList<ImportColonne>();
		}
	}

	@Override
	public List<ImportColonne> findByTemplateAndEntiteManager(
			ImportTemplate importTemplate, Entite entite) {
		if (importTemplate != null && entite != null) {
			return importColonneDao.findByTemplateAndEntite(
					importTemplate, entite);
		} else {
			return new ArrayList<ImportColonne>();
		}
	}

	@Override
	public List<ImportColonne> findByTemplateAndThesaurusManager(
			ImportTemplate importTemplate) {
		if (importTemplate != null) {
			return importColonneDao.findByTemplateAndThesaurus(
					importTemplate);
		} else {
			return new ArrayList<ImportColonne>();
		}
	}
	
	@Override
	public List<ImportColonne> findByTemplateAndAnnotationEntiteManager(
			ImportTemplate importTemplate, Entite entite) {
		if (importTemplate != null && entite != null) {
			return importColonneDao.findByTemplateAndAnnotationEntite(
					importTemplate, entite);
		} else {
			return new ArrayList<ImportColonne>();
		}
	}

	@Override
	public List<ImportColonne> findByTemplateAndAnnotationThesaurusManager(
			ImportTemplate importTemplate) {
		List<ImportColonne> cols = new ArrayList<ImportColonne>();
		if (importTemplate != null) {
			cols.addAll(importColonneDao.findByTemplateAndAnnotationDatatype(
					importTemplate, 
					dataTypeDao.findByType("thesaurus").get(0)));
			cols.addAll(importColonneDao.findByTemplateAndAnnotationDatatype(
					importTemplate, 
					dataTypeDao.findByType("thesaurusM").get(0)));
		} 
		return cols;
	}

	@Override
	public Boolean findDoublonManager(ImportColonne importColonne) {
		if (importColonne != null 
				&& importColonne.getImportTemplate()
				.getImportTemplateId() != null) {
			if (importColonne.getImportColonneId() == null) {
				return importColonneDao
					.findByTemplateWithOrderSelectNom(
						importColonne.getImportTemplate())
						.contains(importColonne.getNom());
			} else {
				return importColonneDao
					.findByExcludedIdWithTemplateSelectNom(
						importColonne.getImportColonneId(), 
						importColonne.getImportTemplate())
						.contains(importColonne.getNom());
			}
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean findDoublonInListManager(List<ImportColonne> colonnes) {
		Boolean doublon = false;
		
		if (colonnes != null) {
			int i = 0;
			while (i < colonnes.size() && !doublon) {
				int j = i + 1;
				while (j < colonnes.size() && !doublon) {
					if (colonnes.get(i).getNom().equals(
							colonnes.get(j).getNom())) {
						doublon = true;
					}
					++j;
				}
				++i;
			}
		}
		
		return doublon;
	}
	
	@Override
	public void validateObjectManager(ImportColonne importColonne,
			ImportTemplate template, Champ champ,
			String operation) {
		// ImportTemplate required
		if (template == null) {
			log.warn("Objet obligatoire ImportTemplate manquant"
					+ " lors de la création d'un ImportColonne");
			throw new RequiredObjectIsNullException(
					"ImportColonne", operation, "ImportTemplate");
		}
		
		// champ required
		if (champ == null) {
			log.warn("Objet obligatoire Champ manquant"
					+ " lors de la création d'un ImportColonne");
			throw new RequiredObjectIsNullException(
					"ImportColonne", operation, "Champ");
		}
		
		//Doublon
		if (!findDoublonManager(importColonne)) {
			
			// validation due l'utilisateur
			BeanValidator.validateObject(
					importColonne, new Validator[]{
							importColonneValidator});
			
		} else {
			log.warn("Doublon lors creation objet ImportColonne "
					+ importColonne.toString());
			throw new DoublonFoundException("ImportColonne", operation);
		}
	}
	
	@Override
	public void createObjectManager(ImportColonne importColonne,
			ImportTemplate template, Champ champ) {
		
		// validation de l'objet
		validateObjectManager(importColonne, template, champ, "creation");
		
		importColonne.setImportTemplate(
					importTemplateDao.mergeObject(template));

		champManager.createObjectManager(champ, null);
		importColonne.setChamp(champ);
		
		importColonneDao.createObject(importColonne);
		
		log.info("Enregistrement objet ImportColonne " 
				+ importColonne.toString());
		
	}

	@Override
	public void updateObjectManager(ImportColonne importColonne,
			ImportTemplate template, Champ champ) {
		
		// validation de l'objet
		validateObjectManager(importColonne, template, champ, "modification");
		
		importColonne.setImportTemplate(
					importTemplateDao.mergeObject(template));

		// si le champ n'existe pas, on le crée
		if (champ.getChampId() == null) {
			champManager.createObjectManager(champ, null);
		} else {
			champManager.updateObjectManager(champ, null);
		}
		importColonne.setChamp(champ);
		
		importColonneDao.updateObject(importColonne);
		
		log.info("Enregistrement objet ImportColonne " 
				+ importColonne.toString());
		
	}

	@Override
	public void removeObjectManager(ImportColonne importColonne) {
		if (importColonne != null) {
			Champ chp = importColonne.getChamp();
			importColonneDao.removeObject(
					importColonne.getImportColonneId());
			log.info("Suppression de l'objet ImportColonne : " 
					+ importColonne.toString());
			
			champManager.removeObjectManager(chp);
		} else {
			log.warn("Suppression d'un ImportColonne null");
		}
	}
}
