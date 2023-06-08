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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.systeme.TemperatureDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.systeme.TemperatureManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.systeme.TemperatureValidator;
import fr.aphp.tumorotek.model.systeme.Temperature;

public class TemperatureManagerImpl implements TemperatureManager
{

   private final Logger log = LoggerFactory.getLogger(TemperatureManager.class);

   private TemperatureDao temperatureDao;

   private TemperatureValidator temperatureValidator;

   public void setTemperatureDao(final TemperatureDao tDao){
      this.temperatureDao = tDao;
   }

   public void setTemperatureValidator(final TemperatureValidator tValidator){
      this.temperatureValidator = tValidator;
   }

   @Override
   public Temperature findByIdManager(final Integer temperatureId){
      return temperatureDao.findById(temperatureId);
   }

   @Override
   public List<Temperature> findAllObjectsManager(){
      log.debug("Recherche de toutes les Températures");
      return temperatureDao.findAll();
   }

   @Override
   public Boolean findDoublonManager(final Temperature temperature){
      if(temperature != null){
         if(temperature.getTemperatureId() == null){
            return temperatureDao.findAll().contains(temperature);
         }else{
            return temperatureDao.findByExcludedId(temperature.getTemperatureId()).contains(temperature);
         }
      }else{
         return false;
      }
   }

   @Override
   public void createObjectManager(final Temperature temperature){
      // Test s'il y a des doublons
      if(findDoublonManager(temperature)){
         log.warn("Doublon lors de la creation de l'objet Temperature : {}",  temperature);
         throw new DoublonFoundException("Temperature", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(temperature, new Validator[] {temperatureValidator});

         temperatureDao.createObject(temperature);

         log.info("Enregistrement de l'objet Temperature : " + temperature.toString());
      }
   }

   @Override
   public void updateObjectManager(final Temperature temperature){
      // Test s'il y a des doublons
      if(findDoublonManager(temperature)){
         log.warn("Doublon lors de la modification de " + "l'objet Temperature : " + temperature.toString());
         throw new DoublonFoundException("Temperature", "modification");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(temperature, new Validator[] {temperatureValidator});

         temperatureDao.updateObject(temperature);

         log.info("Enregistrement de l'objet Temperature : " + temperature.toString());
      }
   }

   @Override
   public void removeObjectManager(final Temperature temperature){
      if(temperature != null){
         temperatureDao.removeObject(temperature.getTemperatureId());
         log.info("Suppression de l'objet Temperature : " + temperature.toString());
      }else{
         log.warn("Suppression d'une Temperature null");
      }
   }
}