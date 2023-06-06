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
package fr.aphp.tumorotek.manager.impl.qualite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.qualite.ConformiteTypeDao;
import fr.aphp.tumorotek.dao.qualite.NonConformiteDao;
import fr.aphp.tumorotek.dao.qualite.ObjetNonConformeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.qualite.NonConformiteManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.qualite.NonConformiteValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.qualite.ConformiteType;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

public class NonConformiteManagerImpl implements NonConformiteManager
{

   private final Logger log = LoggerFactory.getLogger(NonConformiteManager.class);

   /** Bean Dao. */
   private NonConformiteDao nonConformiteDao;

   /** Bean Dao. */
   private ConformiteTypeDao conformiteTypeDao;

   /** Bean Dao. */
   private ObjetNonConformeDao objetNonConformeDao;

   /** Bean Dao. */
   private PlateformeDao plateformeDao;

   /** Bean Validator. */
   private NonConformiteValidator nonConformiteValidator;

   /** Bean Manager. */
   private ObjetNonConformeManager objetNonConformeManager;

   public void setNonConformiteDao(final NonConformiteDao nDao){
      this.nonConformiteDao = nDao;
   }

   public void setConformiteTypeDao(final ConformiteTypeDao cDao){
      this.conformiteTypeDao = cDao;
   }

   public void setObjetNonConformeDao(final ObjetNonConformeDao oDao){
      this.objetNonConformeDao = oDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   public void setNonConformiteValidator(final NonConformiteValidator nValidator){
      this.nonConformiteValidator = nValidator;
   }

   public void setObjetNonConformeManager(final ObjetNonConformeManager oManager){
      this.objetNonConformeManager = oManager;
   }

   @Override
   public NonConformite findByIdManager(final Integer nonConformiteId){
      return nonConformiteDao.findById(nonConformiteId);
   }

   @Override
   public List<NonConformite> findAllObjectsManager(){
      log.debug("Recherche de tous les NonConformites");
      return nonConformiteDao.findAll();
   }

   @Override
   public List<NonConformite> findByPlateformeAndTypeObjManager(final Plateforme plateforme, final ConformiteType typeObj){
      log.debug("Recherche de tous les NonConformites en fct du type");
      if(plateforme != null && typeObj != null){
         return nonConformiteDao.findByTypeAndPf(typeObj, plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<NonConformite> findByPlateformeEntiteAndTypeStringManager(final Plateforme plateforme, final String typeStr,
      final Entite e){
      log.debug("Recherche de tous les NonConformites en fct du type");
      if(plateforme != null && typeStr != null){
         final List<ConformiteType> types = conformiteTypeDao.findByEntiteAndType(typeStr, e);
         if(types.size() == 1){
            return nonConformiteDao.findByTypeAndPf(types.get(0), plateforme);
         }else{
            return new ArrayList<>();
         }
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public boolean findDoublonManager(final NonConformite nonConformite){

      if(nonConformite != null){
         if(nonConformite.getId() == null){
            return nonConformiteDao.findAll().contains(nonConformite);
         }else{
            return nonConformiteDao.findByExcludedId(nonConformite.getId()).contains(nonConformite);
         }
      }else{
         return false;
      }
   }

   @Override
   public boolean isUsedObjectManager(final NonConformite nonConformite){

      final List<ObjetNonConforme> objs = objetNonConformeDao.findByNonConformite(nonConformite);

      return (objs.size() > 0);
   }

   @Override
   public void createObjectManager(final NonConformite nonConformite){

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(nonConformite.getPlateforme() == null){
         throw new RequiredObjectIsNullException("NonConformite", "creation", "Plateforme");
      }else{
         nonConformite.setPlateforme(plateformeDao.mergeObject(nonConformite.getPlateforme()));
      }

      // On vérifie que le type n'est pas null. Si c'est le cas on envoie
      // une exception
      if(nonConformite.getConformiteType() == null){
         throw new RequiredObjectIsNullException("NonConformite", "creation", "ConformiteType");
      }else{
         nonConformite.setConformiteType(conformiteTypeDao.mergeObject(nonConformite.getConformiteType()));
      }

      if(findDoublonManager(nonConformite)){
         log.warn("Doublon lors de la creation de l'objet " + "NonConformite : " + nonConformite.toString());
         throw new DoublonFoundException("NonConformite", "creation");
      }else{
         BeanValidator.validateObject(nonConformite, new Validator[] {nonConformiteValidator});
         nonConformiteDao.createObject(nonConformite);
         log.info("Enregistrement de l'objet NonConformite : " + nonConformite.toString());
      }
   }

   @Override
   public void updateObjectManager(final NonConformite nonConformite){

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(nonConformite.getPlateforme() == null){
         throw new RequiredObjectIsNullException("NonConformite", "modification", "Plateforme");
      }else{
         nonConformite.setPlateforme(plateformeDao.mergeObject(nonConformite.getPlateforme()));
      }

      // On vérifie que le type n'est pas null. Si c'est le cas on envoie
      // une exception
      if(nonConformite.getConformiteType() == null){
         throw new RequiredObjectIsNullException("NonConformite", "modification", "ConformiteType");
      }else{
         nonConformite.setConformiteType(conformiteTypeDao.mergeObject(nonConformite.getConformiteType()));
      }

      if(findDoublonManager(nonConformite)){
         log.warn("Doublon lors de la modification de l'objet " + "NonConformite : " + nonConformite.toString());
         throw new DoublonFoundException("NonConformite", "modification");
      }else{
         BeanValidator.validateObject(nonConformite, new Validator[] {nonConformiteValidator});
         nonConformiteDao.updateObject(nonConformite);
         log.info("Modification de l'objet NonConformite : " + nonConformite.toString());
      }
   }

   @Override
   public void removeObjectManager(final NonConformite nonConformite){

      if(nonConformite != null){
         // suppression des objets non conforme
         final List<ObjetNonConforme> objs = objetNonConformeDao.findByNonConformite(nonConformite);
         for(int i = 0; i < objs.size(); i++){
            objetNonConformeManager.removeObjectManager(objs.get(i));
         }

         nonConformiteDao.removeObject(nonConformite.getId());
         log.info("Suppression de l'objet NonConformite : " + nonConformite.toString());
      }
   }

   @Override
   public List<NonConformite> getFromObjetNonConformes(final List<ObjetNonConforme> objs){
      final List<NonConformite> nonConfs = new ArrayList<>();
      if(objs != null){
         final Iterator<ObjetNonConforme> it = objs.iterator();
         while(it.hasNext()){
            nonConfs.add(it.next().getNonConformite());
         }
      }
      return nonConfs;
   }

   @Override
   public List<NonConformite> findByOrderManager(final Plateforme pf){
      return nonConformiteDao.findByPfOrder(pf);
   }

   @Override
   public List<NonConformite> findByOrderManager(){
      return nonConformiteDao.findByOrder();
   }
}