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
package fr.aphp.tumorotek.manager.impl.code;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeDossierManager;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.code.CodeDossierValidator;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine CodeDossier.
 * Date: 06/06/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CodeDossierManagerImpl implements CodeDossierManager
{

   private final Logger log = LoggerFactory.getLogger(CodeDossierManager.class);

   private CodeDossierDao codeDossierDao;

   private CodeUtilisateurManager codeUtilisateurManager;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private BanqueDao banqueDao;

   private UtilisateurDao utilisateurDao;

   private CodeDossierValidator codeDossierValidator;

   private CodeSelectManager codeSelectManager;

   public void setCodeDossierValidator(final CodeDossierValidator cDValidator){
      this.codeDossierValidator = cDValidator;
   }

   public void setCodeDossierDao(final CodeDossierDao cDao){
      this.codeDossierDao = cDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setCodeUtilisateurManager(final CodeUtilisateurManager cManager){
      this.codeUtilisateurManager = cManager;
   }

   public void setOperationTypeDao(final OperationTypeDao otDao){
      this.operationTypeDao = otDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setCodeSelectManager(final CodeSelectManager cSManager){
      this.codeSelectManager = cSManager;
   }

   @Override
   public void createOrUpdateManager(final CodeDossier dos, final CodeDossier parent, final Banque bank,
      final Utilisateur utilisateur, final String operation){

      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Validation
      checkRequiredObjectsAndValidate(dos, bank, utilisateur, operation);

      if(parent != null){
         dos.setDossierParent(codeDossierDao.mergeObject(parent));
      }

      //Doublon
      if(!findDoublonManager(dos)){
         if((operation.equals("creation") || operation.equals("modification"))){
            if(operation.equals("creation")){
               codeDossierDao.createObject(dos);
               log.debug("Enregistrement objet CodeDossier {}", dos);
               CreateOrUpdateUtilities.createAssociateOperation(dos, operationManager,
                  operationTypeDao.findByNom("Creation").get(0), dos.getUtilisateur());
            }else{
               codeDossierDao.updateObject(dos);
               log.debug("Modification objet CodeDossier ", dos);
               CreateOrUpdateUtilities.createAssociateOperation(dos, operationManager,
                  operationTypeDao.findByNom("Modification").get(0), dos.getUtilisateur());
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors {} objet CodeDossier {}", operation, dos);
         throw new DoublonFoundException("CodeUtilisateur", operation);
      }

   }

   @Override
   public List<CodeDossier> findAllCodeDossiersManager(){
      return codeDossierDao.findAll();
   }

   @Override
   public List<CodeDossier> findByCodeDossierParentManager(final CodeDossier parent){
      return codeDossierDao.findByCodeDossierParent(parent);
   }

   @Override
   public List<CodeDossier> findByNomLikeManager(String nom, final boolean exactMatch, final Banque bank){
      if(!exactMatch){
         nom = "%" + nom + "%";
      }
      log.debug("Recherche CodeUtilisateur par code: {} exactMatch {}", nom, exactMatch);
      return codeDossierDao.findByNomLike(nom, bank);
   }

   @Override
   public List<CodeDossier> findByRootCodeDossierUtilisateurManager(final Banque bank){
      return codeDossierDao.findByRootCodeDossierUtilisateur(bank);
   }

   @Override
   public List<CodeDossier> findByRootCodeDossierSelectManager(final Utilisateur user, final Banque bank){
      return codeDossierDao.findByRootCodeDossierSelect(user, bank);
   }

   @Override
   public List<CodeDossier> findBySelectUtilisateurAndBanqueManager(final Utilisateur u, final Banque b){
      return codeDossierDao.findBySelectUtilisateurAndBanque(u, b);
   }

   @Override
   public List<CodeDossier> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b){
      return codeDossierDao.findByUtilisateurAndBanque(u, b);
   }

   @Override
   public boolean findDoublonManager(final CodeDossier dos){
      if(dos.getCodeDossierId() == null){
         return codeDossierDao.findAll().contains(dos);
      }
      return codeDossierDao.findByExcludedId(dos.getCodeDossierId()).contains(dos);
   }

   @Override
   public void removeObjectManager(final CodeDossier dos){
      if(dos != null){

         // supprime les codes
         if(!dos.getCodeSelect()){
            final Iterator<CodeUtilisateur> it = codeUtilisateurManager.findByCodeDossierManager(dos).iterator();
            while(it.hasNext()){
               codeUtilisateurManager.removeObjectManager(it.next());
            }
         }else{
            final Iterator<CodeSelect> it = codeSelectManager.findByCodeDossierManager(dos).iterator();
            while(it.hasNext()){
               codeSelectManager.removeObjectManager(it.next());
            }
         }

         // supprime les sous-dossiers
         final Iterator<CodeDossier> itDos = findByCodeDossierParentManager(dos).iterator();
         while(itDos.hasNext()){
            removeObjectManager(itDos.next());
         }

         codeDossierDao.removeObject(dos.getCodeDossierId());
         log.info("Suppression objet CodeDossier " + dos.toString());
         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(dos, operationManager);
      }else{
         log.warn("Suppression d'un CodeDossier null");
      }

   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes
    * sont non nulls et lance la validation via le Validator.
    * @param dos CodeDossier
    * @param bank
    * @param utilisateur
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final CodeDossier dos, final Banque bank, final Utilisateur utilisateur,
      final String operation){
      //Banque required
      if(bank != null){
         // merge banque object
         dos.setBanque(banqueDao.mergeObject(bank));
      }else if(dos.getBanque() == null){
         log.warn("Objet obligatoire Banque manquant lors de la {} du code dossier", operation);
         throw new RequiredObjectIsNullException("CodeDossier", operation, "Banque");
      }

      //Utilisateur required
      if(utilisateur != null){
         // merge utilisateur object
         dos.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      }else if(dos.getUtilisateur() == null){
         log.warn("Objet obligatoire Utilisateur manquant lors de la {} du code utilisateur", operation);
         throw new RequiredObjectIsNullException("CodeDossier", operation, "Utilisateur");
      }

      //Validation
      BeanValidator.validateObject(dos, new Validator[] {codeDossierValidator});
   }

   @Override
   public List<CodeDossier> findByRootDossierBanqueManager(final Banque bank, final Boolean select){
      return codeDossierDao.findByRootDossierBanque(bank, select);
   }

}
