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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.TransformationDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.UniteDao;
import fr.aphp.tumorotek.manager.coeur.prodderive.TransformationManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.EntiteObjectIdNotExistException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prodderive.TransformationValidator;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.Transformation;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Unite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine Transformation.
 * Interface créée le 30/09/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class TransformationManagerImpl implements TransformationManager
{

   private final Logger log = LoggerFactory.getLogger(TransformationManager.class);

   /** Bean Dao TransformationDao. */
   private TransformationDao transformationDao;

   /** Bean Dao ProdDeriveDao. */
   private ProdDeriveDao prodDeriveDao;

   /** Bean Dao EntiteDao. */
   private EntiteDao entiteDao;

   /** Bean Manager EntiteManager. */
   private EntiteManager entiteManager;

   /** Bean Dao UniteDao. */
   private UniteDao uniteDao;

   /** Bean Validator. */
   private TransformationValidator transformationValidator;

   public void setTransformationDao(final TransformationDao tDao){
      this.transformationDao = tDao;
   }

   public void setProdDeriveDao(final ProdDeriveDao pDao){
      this.prodDeriveDao = pDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setUniteDao(final UniteDao uDao){
      this.uniteDao = uDao;
   }

   public void setTransformationValidator(final TransformationValidator validator){
      this.transformationValidator = validator;
   }

   /**
    * Recherche une transformation dont l'identifiant est passé en paramètre.
    * @param transformationId Identifiant de la transformation que l'on
    * recherche.
    * @return Une Transformation.
    */
   @Override
   public Transformation findByIdManager(final Integer transformationId){
      return transformationDao.findById(transformationId);
   }

   /**
    * Recherche toutes les transformations présentes dans la base.
    * @return Liste de Transformation.
    */
   @Override
   public List<Transformation> findAllObjectsManager(){
      log.debug("Recherche de toutes les Transformations");
      return transformationDao.findAll();
   }

   /**
    * Recherche la transformation qui est issue d'une entité et d'un ojet.
    * @param entite Entite dont est issue la transformation
    * @param objetId Identifiant de l'objet dont est issue la transformation.
    * @return une liste de transformation.
    */
   @Override
   public List<Transformation> findByEntiteObjetManager(final Entite entite, final Integer objetId){
      log.debug("Recherche Transformation par {} et {}", entite, objetId);
      return transformationDao.findByEntiteObjet(entite, objetId);
   }

   /**
    * Recherche une transformation en fonction de son objet parent.
    * @param parent Objet dont est issue la transformation
    * @return une liste de transformation.
    */
   @Override
   public List<Transformation> findByParentManager(final Object parent){
      Integer id = 0;
      List<Entite> list = new ArrayList<>();
      Entite entite = new Entite();
      final String nomClasse = parent.getClass().getSimpleName();

      // Le parent d'une transformation peut être un prélèvement,
      // un échantillon ou un produit dérivé. Si l'objet parent est
      // bien une de ces classes, on va récupérer son identifiant
      if(nomClasse.equals("Prelevement")){
         final Prelevement tmp = (Prelevement) parent;
         id = tmp.getPrelevementId();
      }else if(nomClasse.equals("Echantillon")){
         final Echantillon tmp = (Echantillon) parent;
         id = tmp.getEchantillonId();
      }else if(nomClasse.equals("ProdDerive")){
         final ProdDerive tmp = (ProdDerive) parent;
         id = tmp.getProdDeriveId();
      }

      // On récupère l'entité correspondant à l'objet parent
      list = entiteManager.findByNomManager(nomClasse);
      if(list.size() == 1){
         entite = list.get(0);
      }else{
         entite = null;
      }

      // Si l'entité et l'identifiant sont valides, on recherche les
      // transformations
      if(entite != null && id != null){
         return transformationDao.findByEntiteObjet(entite, id);
      }else{
         return new ArrayList<>();
      }
   }

   /**
    * Recherche les doublons de la Transformation passé en paramètre.
    * @param transformation Transformation pour laquelle on cherche
    * des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final Transformation transformation){
      if(transformation.getTransformationId() == null){
         return transformationDao.findAll().contains(transformation);
      }else{
         return transformationDao.findByExcludedId(transformation.getTransformationId()).contains(transformation);
      }
   }

   //	/**
   //	 * Test si une transformation est liée à des produits dérivés.
   //	 * @param transformation Transformation que l'on souhaite tester.
   //	 * @return Vrai si la qualité est utilisée.
   //	 */
   //	public Boolean isUsedObjectManager(Transformation transformation) {
   //		List<ProdDerive> list =
   //			prodDeriveDao.findByTransformation(transformation);
   //
   //		return (list.size() > 0);
   //	}

   /**
    * Persist une instance de Transformation dans la base de données.
    * @param transformation Nouvelle instance de l'objet à créer.
    * @param entite Entite parente de la transformation.
    * @param quantiteUnite Unite de la quatité.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   @Override
   public void createObjectManager(final Transformation transformation, final Entite entite, final Unite quantiteUnite){
      // On vérifie que l'entité n'est pas null. Si c'est le cas on envoie
      // une exception
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant lors de la creation d'un objet Transformation");
         throw new RequiredObjectIsNullException("Transformation", "creation", "Entite");
      }else{
         transformation.setEntite(entiteDao.mergeObject(entite));
      }
      // On vérifie que l'unité de quantité n'est pas null
      if(quantiteUnite != null){
         transformation.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));
      }else{
         transformation.setQuantiteUnite(null);
      }

      // on vérifie que le couple Entité/ObjectId référence un objet existant
      if(entiteManager.findObjectByEntiteAndIdManager(entite, transformation.getObjetId()) == null){
         log.warn("Couple Entite : {} - ObjetId : {} inexistant lors de la création d'un objet Transformation",
            entite, transformation.getObjetId());
         throw new EntiteObjectIdNotExistException("Transformation", entite.getNom(), transformation.getObjetId());
      }

      /*if (findDoublonManager(transformation)) {
      	log.warn("Doublon lors de la creation de l'objet Transformation : "
      			+ transformation.toString());
      	throw new DoublonFoundException("Transformation", "creation");
      } else {*/
      BeanValidator.validateObject(transformation, new Validator[] {transformationValidator});
      transformationDao.createObject(transformation);
      log.info("Enregistrement de l'objet Transformation : " + transformation.toString());
      //}

   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param qualite Objet à mettre à jour dans la base.
    * @param entite Entite parente de la transformation.
    * @param quantiteUnite Unite de la quatité.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   @Override
   public void updateObjectManager(final Transformation transformation, final Entite entite, final Unite quantiteUnite){

      // On vérifie que l'entité n'est pas null. Si c'est le cas on envoie
      // une exception
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant lors de la modif d'un objet Transformation");
         throw new RequiredObjectIsNullException("Transformation", "modification", "Entite");
      }else{
         transformation.setEntite(entiteDao.mergeObject(entite));
      }
      // On vérifie que l'unité de quantité n'est pas null
      if(quantiteUnite != null){
         transformation.setQuantiteUnite(uniteDao.mergeObject(quantiteUnite));
      }else{
         transformation.setQuantiteUnite(null);
      }

      // on vérifie que le couple Entité/ObjectId référence un objet existant
      if(entiteManager.findObjectByEntiteAndIdManager(entite, transformation.getObjetId()) == null){
         log.warn("Couple Entite : {} - ObjetId : {} inexistant lors de la modification d'un objet Transformation",
            entite, transformation.getObjetId());
         throw new EntiteObjectIdNotExistException("Transformation", entite.getNom(), transformation.getObjetId());
      }

      /*if (findDoublonManager(transformation)) {
      	log.warn("Doublon lors de la modif de l'objet Transformation : "
      			+ transformation.toString());
      	throw new DoublonFoundException("Transformation", "modification");
      } else {*/
      BeanValidator.validateObject(transformation, new Validator[] {transformationValidator});
      transformationDao.updateObject(transformation);
      log.info("Modification de l'objet Transformation : " + transformation.toString());
      //}
   }

   @Override
   public void removeObjectManager(final Transformation transformation, final String comments, final Utilisateur user){
      //		if (isUsedObjectManager(transformation)) {
      //			log.warn("Objet utilisé lors de la suppression de l'objet "
      //					+ "Transformation : " + transformation.toString());
      //			throw new ObjectUsedException("Transformation", "suppression");
      //		} else {

      transformationDao.removeObject(transformation.getTransformationId());

      log.info("Suppression de l'objet Transformation : " + transformation.toString());
   }

   @Override
   public List<ProdDerive> findAllDeriveFromParentManager(final Object parent){
      final List<ProdDerive> derives = new ArrayList<>();
      final List<Transformation> transfos = findByParentManager(parent);
      List<ProdDerive> derives1 = new ArrayList<>();
      for(int i = 0; i < transfos.size(); i++){
         derives1 = prodDeriveDao.findByTransformation(transfos.get(i));
         derives.addAll(derives1);
         for(int j = 0; j < derives1.size(); j++){
            derives.addAll(findAllDeriveFromParentManager(derives1.get(j)));
         }
      }
      return derives;
   }
}
