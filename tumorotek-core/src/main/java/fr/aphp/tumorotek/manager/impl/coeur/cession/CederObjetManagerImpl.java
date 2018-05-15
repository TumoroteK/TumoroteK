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
package fr.aphp.tumorotek.manager.impl.coeur.cession;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.cession.CederObjetDao;
import fr.aphp.tumorotek.dao.cession.CessionDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.InvalidPKException;
import fr.aphp.tumorotek.manager.exception.PKNotFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.cession.CederObjetValidator;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;

/**
 *
 * Implémentation du manager du bean de domaine CederObjet.
 * Interface créée le 29/01/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0.10
 *
 */
public class CederObjetManagerImpl implements CederObjetManager
{

   private final Log log = LogFactory.getLog(CederObjetManager.class);

   /** Bean Dao CederObjetDao. */
   private CederObjetDao cederObjetDao;
   /** Bean Dao EntiteDao. */
   private EntiteDao entiteDao;
   /** Bean Dao CessionDao. */
   private CessionDao cessionDao;
   /** Bean Dao UniteDao. */
   private UniteDao uniteDao;
   /** Bean Manager EntiteManager. */
   private EntiteManager entiteManager;
   /** Bean validator. */
   private CederObjetValidator cederObjetValidator;

   /**
    * Setter du bean ProtocoleExtDao.
    * @param cDao est le bean Dao.
    */
   public void setCederObjetDao(final CederObjetDao cDao){
      this.cederObjetDao = cDao;
   }

   /**
    * Setter du bean EntiteDao.
    * @param eDao est le bean Dao.
    */
   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   /**
    * Setter du bean CessionDao.
    * @param cDao est le bean Dao.
    */
   public void setCessionDao(final CessionDao cDao){
      this.cessionDao = cDao;
   }

   /**
    * Setter du bean UniteDao.
    * @param uDao est le bean Dao.
    */
   public void setUniteDao(final UniteDao uDao){
      this.uniteDao = uDao;
   }

   /**
    * Setter du bean EntiteManager.
    * @param eManager est le bean Manager.
    */
   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   /**
    * Setter du bean CederObjetValidator.
    * @param cValidator est le bean Validator.
    */
   public void setCederObjetValidator(final CederObjetValidator cValidator){
      this.cederObjetValidator = cValidator;
   }

   /**
    * Recherche un CederObjet dont l'identifiant est passé en paramètre.
    * @param cederObjetPK Identifiant du CederObjet que l'on recherche.
    * @return Un CederObjet.
    */
   @Override
   public CederObjet findByIdManager(final CederObjetPK cederObjetPK){
      return cederObjetDao.findById(cederObjetPK);
   }

   /**
    * Recherche tous les CederObjets présents dans la base.
    * @return Liste de CederObjets.
    */
   @Override
   public List<CederObjet> findAllObjectsManager(){
      log.debug("Recherche de tous les CederObjets");
      return cederObjetDao.findAll();
   }

   /**
    * Recherche les CederObjets sauf celui dont la clé primaire est
    * passée en paramètre.
    * @param pk CederObjetPK.
    * @return une liste de CederObjets.
    */
   @Override
   public List<CederObjet> findByExcludedPKManager(final CederObjetPK pk){
      if(pk != null){
         return cederObjetDao.findByExcludedPK(pk);
      }
      return cederObjetDao.findAll();
   }

   /**
    * Recherche les CederObjets dont l'entité est égale au paramètre.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjets.
    */
   @Override
   public List<CederObjet> findByEntiteManager(final Entite entite){
      // si l'entite n'est pas nulle
      if(entite != null){
         log.debug("Recherche CederObjet par entité : " + entite.toString());
         // recherche par entité
         return cederObjetDao.findByEntite(entite);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les tous CederObjets de type Echantillon.
    * @return une liste de CederObjets.
    */
   @Override
   public List<CederObjet> getAllEchantillonsCederObjetsManager(){
      final Entite entite = entiteDao.findByNom("Echantillon").get(0);
      log.debug("Recherche CederObjet par entité : " + entite.toString());
      return cederObjetDao.findByEntite(entite);
   }

   /**
    * Recherche tous les CederObjets de type ProdDerive.
    * @return une liste de CederObjets.
    */
   @Override
   public List<CederObjet> getAllProdDerivesCederObjetsManager(){
      final Entite entite = entiteDao.findByNom("ProdDerive").get(0);
      log.debug("Recherche CederObjet par entité : " + entite.toString());
      return cederObjetDao.findByEntite(entite);
   }

   /**
    * Recherche tous les CederObjet pour l'objet passé en paramètre.
    * @param obj Objet pour lequel on recherche des CederObjets.
    * @return Liste ordonnée de CederObjets.
    */
   @Override
   public List<CederObjet> findByObjetManager(final Object obj){
      // si l'objet n'est pas null, il doit etre de type Echantillon
      // ou ProdDerive
      if(obj != null){
         if(obj.getClass().getSimpleName().equals("Echantillon")){
            // si echantillon, on recupere l'entite correspondante
            // et on fait une recherche
            final Echantillon echan = (Echantillon) obj;
            final Entite entite = entiteDao.findByNom("Echantillon").get(0);
            return cederObjetDao.findByEntiteObjet(entite, echan.getEchantillonId());

         }else if(obj.getClass().getSimpleName().equals("ProdDerive")){
            // si ProdDerive, on recupere l'entite correspondante
            // et on fait une recherche
            final ProdDerive derive = (ProdDerive) obj;
            final Entite entite = entiteDao.findByNom("ProdDerive").get(0);
            return cederObjetDao.findByEntiteObjet(entite, derive.getProdDeriveId());

         }else{
            return new ArrayList<>();
         }
      }
      return new ArrayList<>();
   }

   /**
    * Recherche toutes les Cessions pour l'objet passé en paramètre
    * et qui respecte le statut passé en paramètres.
    * @param statut Statut des cessions recherchées.
    * @param obj Objet pour lequel on recherche des Cessions.
    * @return Liste ordonnée de Cessions.
    */
   @Override
   public List<Cession> getAllCessionsByStatutAndObjetManager(final String statut, final Object obj){
      if(obj != null && statut != null){
         final List<Cession> cessions = new ArrayList<>();
         final List<CederObjet> cedes = findByObjetManager(obj);

         for(int i = 0; i < cedes.size(); i++){
            final CederObjet cede = cedes.get(i);

            if(cede.getCession().getCessionStatut().getStatut().equals(statut) && !cessions.contains(cede.getCession())){
               cessions.add(cede.getCession());
            }
         }

         return cessions;

      }
      return new ArrayList<>();
   }

   /**
    * Recherche les CederObjets dont la cession et l'entité sont
    * passées en paramètres.
    * @param cession Cession des CederObjets recherchés.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   @Override
   public List<CederObjet> findByCessionEntiteManager(final Cession cession, final Entite entite){
      if(cession != null && entite != null){
         return cederObjetDao.findByCessionEntite(cession, entite);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les CederObjets de type échantillon dont la cession est
    * passée en paramètre.
    * @param cession Cession des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   @Override
   public List<CederObjet> getEchantillonsCedesByCessionManager(final Cession cession){
      if(cession != null){
         final Entite entite = entiteDao.findByNom("Echantillon").get(0);
         return cederObjetDao.findByCessionEntite(cession, entite);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les CederObjets de type ProdDerive dont la cession est
    * passée en paramètre.
    * @param cession Cession des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   @Override
   public List<CederObjet> getProdDerivesCedesByCessionManager(final Cession cession){
      if(cession != null){
         final Entite entite = entiteDao.findByNom("ProdDerive").get(0);
         return cederObjetDao.findByCessionEntite(cession, entite);
      }
      return new ArrayList<>();
   }

   /**
    * Recherche les doublons du CederObjet passé en paramètre.
    * @param cederObjet CederObjet pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final CederObjet cederObjet){
      if(cederObjet != null){
         return cederObjetDao.findByObjetId(cederObjet.getObjetId()).contains(cederObjet);
      }
      return false;
   }

   /**
    * Vérifie que la clé PK du CederObjet est valide.
    * @param cederObjet CederObjet que l'on souhaite tester.
    * @return True si la clé est valide.
    */
   @Override
   public Boolean isValidPK(final CederObjet cederObjet){
      if(cederObjet != null && cederObjet.getPk() != null){
         if(!cederObjet.getEntite().getNom().equals("Echantillon") && !cederObjet.getEntite().getNom().equals("ProdDerive")){
            return false;
         }
         return (entiteManager.findObjectByEntiteAndIdManager(cederObjet.getEntite(), cederObjet.getObjetId()) != null);
      }
      return false;
   }

   /**
    * Vérifie qu'un CederObjet est valide.
    * @param cederObjet CederObjet à créer.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   @Override
   public void validateObjectManager(final CederObjet cederObjet, final Cession cession, final Entite entite,
      final Unite quantiteUnite, final String operation){

      //Cession required
      if(cession == null){
         log.warn("Objet obligatoire Cession manquant" + " lors de la validation d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", operation, "Cession");
      }
      cederObjet.setCession(cession);

      //Entite required
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant" + " lors de la validation d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", operation, "Entite");
      }
      cederObjet.setEntite(entiteDao.mergeObject(entite));

      //Validation
      BeanValidator.validateObject(cederObjet, new Validator[] {cederObjetValidator});

      if(isValidPK(cederObjet)){
         if(operation.equals("creation")){
            //Doublon
            if(findDoublonManager(cederObjet)){

               log.warn("Doublon lors validation objet CederObjet " + cederObjet.toString());
               throw new DoublonFoundException("CederObjet", operation);
            }
         }
      }else{

         log.warn("PK non valide lors de la validation d'un CederObjet " + cederObjet.toString());
         throw new InvalidPKException("CederObjet", operation);
      }
   }

   /**
    * Persist une instance de CederObjet dans la base de données.
    * @param cederObjet CederObjet à créer.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   @Override
   public void createObjectManager(final CederObjet cederObjet, final Cession cession, final Entite entite,
      final Unite quantiteUnite){

      //Cession required
      if(cession == null){
         log.warn("Objet obligatoire Cession manquant" + " lors de la creation d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", "creation", "Cession");
      }
      cederObjet.setCession(cessionDao.mergeObject(cession));

      //Entite required
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant" + " lors de la creation d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", "creation", "Entite");
      }
      cederObjet.setEntite(entiteDao.mergeObject(entite));

      //Validation
      BeanValidator.validateObject(cederObjet, new Validator[] {cederObjetValidator});

      if(isValidPK(cederObjet)){
         //Doublon
         if(!findDoublonManager(cederObjet)){
            cederObjet.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));

            cederObjetDao.createObject(cederObjet);

            log.debug("Enregistrement objet CederObjet " + cederObjet.toString());

         }else{
            log.warn("Doublon lors creation objet CederObjet " + cederObjet.toString());
            throw new DoublonFoundException("ProtocoleExt", "creation");
         }
      }else{
         log.warn("PK non valide lors de la création d'un CederObjet " + cederObjet.toString());
         throw new InvalidPKException("CederObjet", "creation");
      }

   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param cederObjet Objet à persister.
    * @param cession Cession du CederObjet.
    * @param entite Entite de l'objet à céder.
    * @param quantiteUnite Unite de quantité du CederObjet.
    */
   @Override
   public void updateObjectManager(final CederObjet cederObjet, final Cession cession, final Entite entite,
      final Unite quantiteUnite){

      //Cession required
      if(cession == null){
         log.warn("Objet obligatoire Cession manquant" + " lors de la modification d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", "modification", "Cession");
      }
      cederObjet.setCession(cessionDao.mergeObject(cession));

      //Entite required
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant" + " lors de la modification d'un CederObjet");
         throw new RequiredObjectIsNullException("CederObjet", "modification", "Entite");
      }
      cederObjet.setEntite(entiteDao.mergeObject(entite));

      //Validation
      BeanValidator.validateObject(cederObjet, new Validator[] {cederObjetValidator});

      if(isValidPK(cederObjet)){
         //Doublon
         if(findDoublonManager(cederObjet)){
            cederObjet.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));

            cederObjetDao.updateObject(cederObjet);

            log.debug("Modification objet CederObjet " + cederObjet.toString());

         }else{
            log.warn("PK inconnue lors de la modification du CederObjet " + cederObjet.toString());
            throw new PKNotFoundException("CederObjet", "modification");
         }
      }else{
         log.warn("PK non valide lors de la modification d'un CederObjet " + cederObjet.toString());
         throw new InvalidPKException("CederObjet", "modification");
      }

   }

   /**
    * Supprime un ProtocoleExt de la base de données.
    * @param protocoleExt ProtocoleExt à supprimer de la base de données.
    */
   @Override
   public void removeObjectManager(final CederObjet cederObjet){
      if(cederObjet != null){
         cederObjetDao.removeObject(cederObjet.getPk());
         log.debug("Suppression de l'objet CederObjet : " + cederObjet.toString());
      }else{
         log.warn("Suppression d'un cederObjet null");
      }
   }

   @Override
   public Long findObjectsCessedCountManager(final Cession cess, final Entite e){
      return cederObjetDao.findObjectsCessedCount(cess, e).get(0);
   }

   @Override
   public List<String> findCodesByCessionEntiteManager(final Cession cession, final Entite entite){
      if(entite != null){
         if(entite.getNom().equals("Echantillon")){
            return cederObjetDao.findCodesEchantillonByCession(cession);
         }else if(entite.getNom().equals("ProdDerive")){
            return cederObjetDao.findCodesDeriveByCession(cession);
         }
      }
      return null;
   }
}
