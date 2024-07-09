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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import eu.medsea.mimeutil.MimeUtil;
import fr.aphp.tumorotek.dao.systeme.FichierDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.systeme.FichierManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.systeme.FichierValidator;
import fr.aphp.tumorotek.model.TKFileSettableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 *
 * Implémentation du manager du bean de domaine CrAnapath.
 * Classe créée le 24/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0.11
 *
 * TODO Migrer File vers NIO Paths pour faciliter les tests
 *
 */
public class FichierManagerImpl implements FichierManager
{

   private final Log log = LogFactory.getLog(FichierManager.class);

   private FichierDao fichierDao;

   private FichierValidator fichierValidator;

   public void setFichierDao(final FichierDao fDao){
      this.fichierDao = fDao;
   }

   public void setFichierValidator(final FichierValidator validator){
      this.fichierValidator = validator;
   }

   @Override
   public Fichier findByIdManager(final Integer fichierId){
      return fichierDao.findById(fichierId);
   }

   @Override
   public List<Fichier> findAllObjectsManager(){
      log.debug("Recherche de tous les Fichiers");
      return fichierDao.findByOrder();
   }

   @Override
   public List<Fichier> findByPathLikeManager(String path, final boolean exactMatch){
      log.debug("Recherche Fichier par " + path + " exactMatch " + String.valueOf(exactMatch));
      if(path != null){
         if(!exactMatch){
            path = path + "%";
         }
         return fichierDao.findByPath(path);
      }
      return new ArrayList<>();
   }

   @Override
   public Boolean findDoublonManager(final Fichier file){
      if(file.getFichierId() == null){
         final List<Fichier> files = fichierDao.findByPath(file.getPath());
         for(final Fichier f : files){
            if(f.equals(file)){
               return true;
            }
         }
      }else{
         final List<Fichier> files = fichierDao.findByPath(file.getPath());
         for(final Fichier f : files){
            if(!f.getFichierId().equals(file.getFichierId()) && f.equals(file)){
               return true;
            }
         }
      }
      return false;
   }

   @Override
   public Boolean isUsedObjectManager(final Fichier path){
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
   public Boolean isPathSharedManager(final Fichier path){
      if(path != null){
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
   public void createObjectManager(final Fichier fichier, final InputStream stream, final List<File> filesCreated){
      if(findDoublonManager(fichier)){
         log.warn("Doublon lors de la creation de l'objet Fichier : " + fichier.toString());
         throw new DoublonFoundException("Fichier", "creation");
      }

      BeanValidator.validateObject(fichier, new Validator[] {fichierValidator});
      //			if (stream == null) {
      //				log.warn("Incoherence creation stream vide");
      //				throw new RuntimeException("Creation de fichier vide");
      //			}
      if(stream != null){
         fichier.setMimeType(getMimeType(stream));
      }
      if(fichier.getMimeType() == null){ // defaut
         fichier.setMimeType("application/octet-stream");
      }
      fichierDao.createObject(fichier);
      if(stream != null){
         log.info("Enregistrement de l'objet Fichier : " + fichier.toString());
         fichier.setPath(fichier.getPath() + "_" + fichier.getFichierId());
         fichierDao.updateObject(fichier);
         storeFile(stream, fichier.getPath(), filesCreated);

      }else if(!fichier.getPath().matches(".*_[0-9]+")){
         fichier.setFichierId(null);
         log.error("fichier.path.illegal : " + fichier.getPath());
         throw new RuntimeException("fichier.path.illegal");
      }
      //			fichierDao.createObject(fichier);
   }

   @Override
   public Fichier updateObjectManager(final Fichier fichier, final InputStream stream, final List<File> filesCreated,
      final List<File> filesToDelete){
      if(findDoublonManager(fichier)){
         log.warn("Doublon lors de la modification de l'objet Fichier : " + fichier.toString());
         throw new DoublonFoundException("Fichier", "modification");
      }

      BeanValidator.validateObject(fichier, new Validator[] {fichierValidator});

      if(stream != null){
         //				fichierDao.updateObject(fichier);
         // suppr ref, recree une autre ref			
         removeObjectManager(fichier, filesToDelete);
         final Fichier clone = fichier.clone();
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
      }

      // path doit être inchangé
      fichierDao.updateObject(fichier);
      log.info("Modification de l'objet Fichier : " + fichier.toString());
      return fichier;
   }

   @Override
   public void removeObjectManager(final Fichier path, final List<File> filesToDelete){
      if(path != null){
         // if (!isUsedObjectManager(path)) {
         // if (remove) {
         if(!isPathSharedManager(path) && path.getPath() != null){
            log.debug("-> ajout suppression physique du fichier à liste");
            if(filesToDelete == null){ // utile pour les tests
               new File(path.getPath()).delete();
            }else{
               filesToDelete.add(new File(path.getPath()));
            }
         }
         fichierDao.removeObject(path.getFichierId());
         log.debug("Suppression de la reference vers l'objet Fichier : " + path.toString());
         //			} else {
         //				log.info("Référence vers fichier non supprimée");
         //			}
      }
   }

   @Override
   public boolean storeFile(InputStream fis, final String path, final List<File> filesCreated){
      boolean success = false;

      FileOutputStream fos = null;
      try{
         if(!(new File(path).exists())){
            // enregistrement du fichier
            fos = new FileOutputStream(path, false);
            final byte[] buf = new byte[1024];
            int i = 0;
            while((i = fis.read(buf)) != -1){
               fos.write(buf, 0, i);
            }
            success = true;
            if(filesCreated != null){
               filesCreated.add(new File(path));
            }
         }else{
            log.info("Fichier existe déjà path: " + path);
         }
      }catch(final FileNotFoundException fe){
         log.error("Annotation fichier: Erreur survenue dans la creation du fichier au chemin specifie: " + path);
         throw new RuntimeException(fe);
      }catch(final java.io.IOException e){
         log.error("Annotation fichier: Erreur survenue dans l'ecriture fichier");
         throw new RuntimeException(e);
      }finally{
         try{
            if(fis != null){
               fis.close();
            }
         }catch(final Exception e){
            fis = null;
            log.error("Annotation fichier: inputStream not closed");
         }
         try{
            if(fos != null){
               fos.close();
            }
         }catch(final IOException e){
            fos = null;
            log.error("Annotation fichier: outputStream not closed");
         }
      }

      return success;
   }

   private String getMimeType(final InputStream instr){
      if(MimeUtil.getMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector") == null){
         MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
      }
      final Collection<?> mimeTypes = MimeUtil.getMimeTypes(instr);
      return mimeTypes.toString();
   }

   @Override
   public void createOrUpdateFileForObject(final TKFileSettableObject obj, Fichier fileRef, final InputStream stream,
      final String pathBase, final List<File> filesCreated, final List<File> filesToDelete){

      if(fileRef != null){
         if(fileRef.getFichierId() == null){
            fileRef.setTKFileSettableObject(obj);
            if(fileRef.getPath() == null){
               fileRef.setPath(pathBase);
            }
            createObjectManager(fileRef, stream, filesCreated);
         }else{
            fileRef = updateObjectManager(fileRef, stream, filesCreated, filesToDelete);
         }
      }else if(obj.getFile() != null){
         removeObjectManager(obj.getFile(), filesToDelete);
      }
      obj.setFile(fileRef);
   }

   @Override
   public void switchBanqueManager(final Fichier file, final Banque dest, final Set<MvFichier> filesToMove){

      if(file != null && dest != null && filesToMove != null){
         log.debug("modification chemin et déplacement du fichier: " + file.getNom());
         final String actualPathStr = file.getPath();
         final String destPathStr = actualPathStr.replaceFirst("coll_\\d+", "coll_" + dest.getBanqueId());

         // programme de déplacement physique du fichier
         filesToMove.add(new MvFichier(Paths.get(actualPathStr), Paths.get(destPathStr)));

         // mise à jour du chemin
         file.setPath(destPathStr);
         fichierDao.updateObject(file);
      }
   }
}
