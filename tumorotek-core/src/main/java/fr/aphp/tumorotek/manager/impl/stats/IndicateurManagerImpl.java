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
package fr.aphp.tumorotek.manager.impl.stats;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.stats.IndicateurDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.stats.IndicateurManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.stats.IndicateurValidator;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.Subdivision;

public class IndicateurManagerImpl implements IndicateurManager
{

   private final Log log = LogFactory.getLog(IndicateurManager.class);

   private final List<Indicateur> NO_LIST = new ArrayList<>();

   private IndicateurDao indicateurDao;

   // private SModeleDao sModeleDao;
   private IndicateurValidator indicateurValidator;

   public void setIndicateurDao(final IndicateurDao sDao){
      this.indicateurDao = sDao;
   }

   //	public void setSModeleDao(SModeleDao mDao) {
   //		this.sModeleDao = mDao;
   //	}

   public void setIndicateurValidator(final IndicateurValidator sVal){
      this.indicateurValidator = sVal;
   }

   @Override
   public Indicateur findByIdManager(final Integer statementId){
      return indicateurDao.findById(statementId);
   }

   @Override
   public List<Indicateur> findAllObjectsManager(){
      return indicateurDao.findAll();
   }

   @Override
   public List<Indicateur> findNullSubdivisionIndicateursManager(){
      return indicateurDao.findNullSubvivisionIndicateurs();
   }

   @Override
   public List<Indicateur> findBySubdivisionManager(final Subdivision subdivision){
      return indicateurDao.findBySubdivision(subdivision);
   }

   @Override
   public List<Indicateur> findBySModeleManager(final SModele modele){
      if(modele != null){
         return indicateurDao.findBySModele(modele);
      }else{
         return NO_LIST;
      }
   }

   @Override
   public Boolean findDoublonManager(final Indicateur indic){
      if(indic != null){
         if(indic.getIndicateurId() == null){
            return indicateurDao.findAll().contains(indic);
         }else{
            return indicateurDao.findByExcludedId(indic.getIndicateurId()).contains(indic);
         }
      }else{
         return false;
      }
   }

   @Override
   public void createObjectManager(final Indicateur indic){
      // stmt.setsModeleIndicateurs(sModeleDao.mergeObject(modele).getSModeleIndicateurs());
      // Test s'il y a des doublons
      if(findDoublonManager(indic)){
         log.warn("Doublon lors de la creation de l'objet Indicateur : " + indic.getNom());
         throw new DoublonFoundException("Indicateur", "creation");
      }else{ // validation de l'objet
         // validation
         BeanValidator.validateObject(indic, new Validator[] {indicateurValidator});
      }

      // creation
      indicateurDao.createObject(indic);
      log.debug("Enregistrement objet Indicateur " + indic.toString());
   }

   @Override
   public void updateObjectManager(final Indicateur indic){

      // validation
      BeanValidator.validateObject(indic, new Validator[] {indicateurValidator});

      // stmt.setsModeleIndicateurs(sModeleDao.mergeObject(modele).getSModeleIndicateurs());

      indicateurDao.updateObject(indic);
      log.debug("Mise à jour objet Statement " + indic.toString());
   }

   @Override
   public void removeObjectManager(final Indicateur indic){
      if(indic != null){
         indicateurDao.removeObject(indic.getIndicateurId());
         log.debug("Suppression de l'objet Statement : " + indic.toString());
      }else{
         log.warn("Suppression d'un Statement null");
      }
   }
}
