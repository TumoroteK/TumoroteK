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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.code.CodeDossierDao;
import fr.aphp.tumorotek.dao.code.CodeSelectDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.code.CodeSelectManager;
import fr.aphp.tumorotek.manager.code.CommonUtilsManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impl.coeur.CreateOrUpdateUtilities;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine CodeSelect.
 * Date: 02/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class CodeSelectManagerImpl implements CodeSelectManager
{

   private final Log log = LogFactory.getLog(CodeSelectManager.class);

   private CodeSelectDao codeSelectDao;

   private CodeDossierDao codeDossierDao;

   private OperationManager operationManager;

   private OperationTypeDao operationTypeDao;

   private BanqueDao banqueDao;

   private UtilisateurDao utilisateurDao;

   private CommonUtilsManager commonUtilsManager;

   public void setCodeSelectDao(final CodeSelectDao cDao){
      this.codeSelectDao = cDao;
   }

   public void setCodeDossierDao(final CodeDossierDao cDDao){
      this.codeDossierDao = cDDao;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
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

   public void setCommonUtilsManager(final CommonUtilsManager cM){
      this.commonUtilsManager = cM;
   }

   @Override
   public List<CodeSelect> findAllObjectsManager(){
      return codeSelectDao.findAll();
   }

   @Override
   public List<CodeSelect> findByCodeDossierManager(final CodeDossier parent){
      return codeSelectDao.findByCodeDossier(parent);
   }

   @Override
   public List<CodeCommon> findCodesFromSelectByDossierManager(final CodeDossier parent){
      return extractCodeCommonFromCodeSelect(codeSelectDao.findByCodeDossier(parent), null);
   }

   @Override
   public List<CodeCommon> findByRootDossierManager(final Utilisateur u, final Banque bank){
      final List<CodeSelect> codes = codeSelectDao.findByRootDossier(u, bank);
      return extractCodeCommonFromCodeSelect(codes, null);
   }

   @Override
   public List<CodeSelect> findByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b){
      return codeSelectDao.findByUtilisateurAndBanque(u, b);
   }

   @Override
   public List<CodeCommon> findCodesFromSelectByUtilisateurAndBanqueManager(final Utilisateur u, final Banque b){
      return extractCodeCommonFromCodeSelect(codeSelectDao.findByUtilisateurAndBanque(u, b), null);
   }

   @Override
   public boolean findDoublonManager(final CodeSelect code){
      if(code.getCodeSelectId() == null){
         return codeSelectDao.findAll().contains(code);
      }
      return codeSelectDao.findByExcludedId(code.getCodeSelectId()).contains(code);
   }

   @Override
   public void createOrUpdateManager(final CodeSelect code, final CodeDossier dos, final Banque bank,
      final Utilisateur utilisateur, final String operation){
      if(operation == null){
         throw new NullPointerException("operation cannot be " + "set to null for createorUpdateMethod");
      }

      //Validation
      checkRequiredObjectsAndValidate(code, bank, utilisateur, operation);

      // merge non required
      if(dos != null){
         code.setCodeDossier(codeDossierDao.mergeObject(dos));
      }

      //Doublon
      if(!findDoublonManager(code)){
         if((operation.equals("creation") || operation.equals("modification"))){
            if(operation.equals("creation")){
               codeSelectDao.createObject(code);
               log.info("Enregistrement objet CodeSelect " + code.toString());
               CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
                  operationTypeDao.findByNom("Creation").get(0), code.getUtilisateur());
            }else{
               codeSelectDao.updateObject(code);
               log.info("Modification objet CodeSelect " + code.toString());
               CreateOrUpdateUtilities.createAssociateOperation(code, operationManager,
                  operationTypeDao.findByNom("Modification").get(0), code.getUtilisateur());
            }
         }else{
            throw new IllegalArgumentException("Operation must match " + "'creation/modification' values");
         }
      }else{
         log.warn("Doublon lors " + operation + " objet CodeSelect " + code.toString());
         throw new DoublonFoundException("CodeSelect", operation);
      }
   }

   /**
    * Verifie que les Objets devant etre obligatoirement associes
    * sont non nulls.
    * @param code CodeSelect
    * @param bank
    * @param utilisateur
    * @param operation demandant la verification
    */
   private void checkRequiredObjectsAndValidate(final CodeSelect code, final Banque bank, final Utilisateur utilisateur,
      final String operation){
      //Banque required
      if(bank != null){
         // merge banque object
         code.setBanque(banqueDao.mergeObject(bank));
      }else if(code.getBanque() == null){
         log.warn("Objet obligatoire Banque manquant" + " lors de la " + operation + " du code favori");
         throw new RequiredObjectIsNullException("CodeSelect", operation, "Banque");
      }

      //Utilisateur required
      if(utilisateur != null){
         // merge utilisateur object
         code.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      }else if(code.getUtilisateur() == null){
         log.warn("Objet obligatoire Utilisateur manquant" + " lors de la " + operation + " du code favori");
         throw new RequiredObjectIsNullException("CodeSelect", operation, "Utilisateur");
      }
   }

   @Override
   public void removeObjectManager(final CodeSelect code){
      if(code != null){
         codeSelectDao.removeObject(code.getCodeSelectId());
         log.info("Suppression objet CodeSelect " + code.toString());
         //Supprime operations associes
         CreateOrUpdateUtilities.removeAssociateOperations(code, operationManager);
      }else{
         log.warn("Suppression d'un CodeUtilisateur null");
      }
   }

   @Override
   public List<CodeCommon> findByCodeOrLibelleLikeManager(String codeOrLibelle, final boolean exactMatch, final Utilisateur u,
      final Banque b){

      if(!exactMatch){
         codeOrLibelle = ".*" + codeOrLibelle + ".*";
      }

      final List<CodeSelect> codeUserBanque = new ArrayList<>();
      if(u == null){
         codeUserBanque.addAll(codeSelectDao.findByBanque(b));
      }else{
         codeUserBanque.addAll(findByUtilisateurAndBanqueManager(u, b));
      }

      return extractCodeCommonFromCodeSelect(codeUserBanque, codeOrLibelle);
   }

   /**
    * Extrait les codes (CodeCommon) pour affichage
    * a partir d'une liste de code favoris.
    * Filtre eventuellement sur le code et libelle.
    * Embarque une back reference vers l'objet code select.
    * @param codes
    * @param codeOrLibelle
    * @return liste de CodeCommon pour affichage
    */
   private List<CodeCommon> extractCodeCommonFromCodeSelect(final List<CodeSelect> codes, final String codeOrLibelle){
      final List<CodeCommon> res = new ArrayList<>();

      final Iterator<CodeSelect> it = codes.iterator();
      CodeSelect next;
      CodeCommon ref;
      while(it.hasNext()){
         next = it.next();
         ref = commonUtilsManager.findCodeByTableCodageAndIdManager(next.getCodeId(), next.getTableCodage());

         if(ref != null){
            ref.setCodeSelect(next);
            if(codeOrLibelle != null){
               if(ref.getCode().matches(codeOrLibelle) || ref.getLibelle().matches(codeOrLibelle)){
                  res.add(ref);
               }
            }else{
               res.add(ref);
            }
         }
      }
      return res;
   }

   @Override
   public List<CodeCommon> findByRootDossierAndBanqueManager(final Banque bank){
      return extractCodeCommonFromCodeSelect(codeSelectDao.findByRootDossierAndBanque(bank), null);
   }
}
