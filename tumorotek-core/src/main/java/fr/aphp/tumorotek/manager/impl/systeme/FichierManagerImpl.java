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
package fr.aphp.tumorotek.manager.impl.systeme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;


import fr.aphp.tumorotek.dao.systeme.FichierDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.systeme.FichierValidator;
import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.systeme.Fichier;
import eu.medsea.mimeutil.MimeUtil;

/**
 * 
 * Implémentation du manager du bean de domaine CrAnapath.
 * Classe créée le 24/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0.11
 *
 */
public class FichierManagerImpl implements FichierManager {
	
	private Log log = LogFactory.getLog(FichierManager.class);
	
	private FichierDao fichierDao;
	private FichierValidator fichierValidator;


	public void setFichierDao(FichierDao fDao) {
		this.fichierDao = fDao;
	}

	public void setFichierValidator(FichierValidator validator) {
		this.fichierValidator = validator;
	}

	@Override
	public Fichier findByIdManager(Integer fichierId) {
		return fichierDao.findById(fichierId);
	}
	
	@Override
	public List<Fichier> findAllObjectsManager() {
		log.debug("Recherche de tous les Fichiers");
		return fichierDao.findByOrder();
	}
	
	@Override
	public List<Fichier> findByPathLikeManager(
			String path, boolean exactMatch) {
		log.debug("Recherche Fichier par " 
				+ path + " exactMatch " + String.valueOf(exactMatch));
		if (path != null) {
			if (!exactMatch) {
				path = path + "%";
			}
			return fichierDao.findByPath(path);
		} else {
			return new ArrayList<Fichier>();
		}
	}
	
	@Override
	public Boolean findDoublonManager(Fichier file) {
		if (file.getFichierId() == null) {
			List<Fichier> files = fichierDao.findByPath(file.getPath());
			for (Fichier f : files) {
				if (f.equals(file)) {
					return true;
				}
			}
		} else {
			List<Fichier> files = fichierDao.findByPath(file.getPath());
			for (Fichier f : files) {
				if (!f.getFichierId().equals(file.getFichierId()) && f.equals(file)) {
					return true;
				}
			}
		}	
		return false;
	}
	
	@Override
	public Boolean isUsedObjectManager(Fichier path) {
		// List<Echantillon> echans = echantillonDao.findByCrAnapath(path);
		// return (echans.size() > 0);
		return isPathSharedManager(path);
	}
//	
//	@Override
//	public Boolean isSharedByEchansObjectManager(Fichier file) {
//		List<Echantillon> echans = echantillonDao.findByCrAnapath(file);
//		return (echans.size() > 1);
//	}

	@Override
	public Boolean isPathSharedManager(Fichier path) {
		if (path != null) {
			return findByPathLikeManager(path.getPath(), true).size() > 1;
//			List<Echantillon> echans = fichierDao
//					.findFilesSharingPathForEchans(path.getPath());
//			List<AnnotationValeur> vals = fichierDao
//					.findFilesSharingPathForAnnos(path.getPath());
//			return echans.size() > 1 || vals.size() > 1;
		}
		return false;
	}
	
	@Override
	public void createObjectManager(Fichier fichier, InputStream stream, 
												List<File> filesCreated) {
		if (findDoublonManager(fichier)) {
			log.warn("Doublon lors de la creation de l'objet Fichier : " 
					+ fichier.toString());
			throw new DoublonFoundException("Fichier", "creation");
		} else {
			BeanValidator.validateObject(
					fichier, new Validator[]{fichierValidator});
//			if (stream == null) {
//				log.warn("Incoherence creation stream vide");
//				throw new RuntimeException("Creation de fichier vide");
//			}
			if (stream != null) {
				fichier.setMimeType(getMimeType(stream));
			}
			if (fichier.getMimeType() == null) { // defaut
				fichier.setMimeType("application/octet-stream");
			}
			fichierDao.createObject(fichier);
			if (stream != null) {
				log.info("Enregistrement de l'objet Fichier : " 
						+ fichier.toString());
				fichier.setPath(fichier.getPath() + "_" + fichier.getFichierId());
				fichierDao.updateObject(fichier);
				storeFile(stream, fichier.getPath(), filesCreated);
				
			} else if (!fichier.getPath().matches(".*_[0-9]+")) {
				fichier.setFichierId(null);
				throw new RuntimeException("fichier.path.illegal");
			}
//			fichierDao.createObject(fichier);
		}
	}
	
	@Override
	public Fichier updateObjectManager(Fichier fichier, InputStream stream, 
			List<File> filesCreated,
			List<File> filesToDelete) {
		if (findDoublonManager(fichier)) {
			log.warn("Doublon lors de la modification de l'objet Fichier : " 
					+ fichier.toString());
			throw new DoublonFoundException("Fichier", "modification");
		} else {
			BeanValidator.validateObject(
					fichier, new Validator[]{fichierValidator});
			if (stream != null) {
//				fichierDao.updateObject(fichier);
				// suppr ref, recree une autre ref			
				removeObjectManager(fichier, filesToDelete);
				Fichier clone = fichier.clone();
				clone.setFichierId(null);
				fichier.setMimeType(getMimeType(stream));
				clone.setPath(clone.getPath().substring(0, clone.getPath().lastIndexOf("_")));
				createObjectManager(clone, stream, filesCreated);
//				// ecrase path ssi pas partage
//				if (!isPathSharedManager(fichier)) {
//					storeFile(stream, fichier.getPath() 
//								+ "_" + fichier.getFichierId());
//				} else { // recree un nouveau fichier
//					
//				}
				return clone;
			} else {
				// path doit être inchangé
				fichierDao.updateObject(fichier);
			}
			log.info("Modification de l'objet Fichier : " 
					+ fichier.toString());
		}
		return fichier;
	}

	@Override
	public void removeObjectManager(Fichier path, List<File> filesToDelete) {
		if (path != null) { 
			// if (!isUsedObjectManager(path)) {
			// if (remove) {
			if (!isPathSharedManager(path) && path.getPath() != null) {
				log.debug("-> ajout suppression physique du fichier à liste");
				if (filesToDelete == null) { // utile pour les tests
					new File(path.getPath()).delete();
				} else {
					filesToDelete.add(new File(path.getPath()));
				}
			}
			fichierDao.removeObject(path.getFichierId());
			log.debug("Suppression de la reference vers l'objet Fichier : " 
													+ path.toString());
//			} else {
//				log.info("Référence vers fichier non supprimée");
//			}
		}
	}
	
	@Override
	public boolean storeFile(InputStream fis, String path, List<File> filesCreated) {
		boolean success = false;
		
		FileOutputStream fos = null;
		try {
			if (!(new File(path).exists())) {
				// enregistrement du fichier
				fos = new FileOutputStream(path, false);
		        byte[] buf = new byte[1024];
		        int i = 0;
		        while ((i = fis.read(buf)) != -1) {
		            fos.write(buf, 0, i);
		        }
		        success = true;
		        if (filesCreated != null) {
		        	filesCreated.add(new File(path));
		        }
			} else {
				log.info("Fichier existe déja path: " + path);
			}
		}  catch (FileNotFoundException fe) {
			log.error("Annotation fichier: "
	    			+ "Erreur survenue dans la creation du fichier au chemin "
	    			+ "specifie: " + path);
			throw new RuntimeException(fe);
	    }  catch (java.io.IOException e) {
	    	log.error("Annotation fichier: "
	    			+ "Erreur survenue dans l'ecriture fichier");
			throw new RuntimeException(e);
		} finally {
			try { 
				if (fis != null) {
					fis.close();
				}
			}  catch (Exception e) {
				fis = null;
				log.error("Annotation fichier: inputStream not closed");
			}
			try { 
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				fos = null;
				log.error("Annotation fichier: outputStream not closed");
			}
		}
			
		return success;
	}
	
	private String getMimeType(InputStream instr) {
		if (MimeUtil.getMimeDetector("eu.medsea.mimeutil.detector"
									+ ".MagicMimeMimeDetector") == null) {
			MimeUtil
				.registerMimeDetector("eu.medsea.mimeutil.detector"
													+ ".MagicMimeMimeDetector");
		}
        Collection< ? > mimeTypes = MimeUtil.getMimeTypes(instr);
        return mimeTypes.toString();
	}
	
	@Override
	public void createOrUpdateFileForObject(TKFileSettableObject obj, 
			Fichier fileRef, InputStream stream, String pathBase,
			List<File> filesCreated, List<File> filesToDelete) {
		
		if (fileRef != null) {
			if (fileRef.getFichierId() == null) {
				fileRef.setTKFileSettableObject(obj);
				if (fileRef.getPath() == null) {
					fileRef.setPath(pathBase);
				}
				createObjectManager(fileRef, stream, filesCreated);	
			} else {
				fileRef = updateObjectManager(fileRef, stream, 
												filesCreated, filesToDelete);
			}
		} else if (obj.getFile() != null) {
			removeObjectManager(obj.getFile(), filesToDelete);
		}
		obj.setFile(fileRef);
	}
}
