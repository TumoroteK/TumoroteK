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

package fr.aphp.tumorotek.manager.impl.contexte;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.OrganismeDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.gatsbi.EtudeDao;
import fr.aphp.tumorotek.manager.context.OrganismeManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.OrganismeValidator;
import fr.aphp.tumorotek.model.contexte.Organisme;
import fr.aphp.tumorotek.model.contexte.Plateforme;


public class OrganismeManagerImpl implements OrganismeManager
{
   private final Logger log = LoggerFactory.getLogger(OrganismeManagerImpl.class);

   private OrganismeDao organismeDao;
   
   private OrganismeValidator organismeValidator;
   
   private PlateformeDao plateformeDao;
   
   private EtudeDao etudeDao;

   public OrganismeManagerImpl(){}
   
   public void setOrganismeDao(OrganismeDao organismeDao){
      this.organismeDao = organismeDao;
   }

   public void setOrganismeValidator(OrganismeValidator organismeDao){
      this.organismeValidator = organismeDao;
   }

   public void setPlateformeDao(PlateformeDao plateformeDao){
      this.plateformeDao = plateformeDao;
   }

   public void setEtudeDao(EtudeDao etudeDao){
      this.etudeDao = etudeDao;
   }

   @Override
   public List<Organisme> findByOrderManager(final Plateforme pf){
      return organismeDao.findByPfOrder(pf);
   }

   @Override
   public Organisme findByIdManager(Integer id){
      return organismeDao.findById(id);
   }

   @Override
   public List<Organisme> findByOrderManager(){
      return organismeDao.findByOrder();
   }

   @Override
   public void createObjectManager(Organisme obj){
      final Organisme organisme = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      Plateforme plateforme = organisme.getPlateforme();
      if( plateforme == null){
         log.warn("Objet obligatoire Plateforme manquant lors de la creation d'un objet Organisme");
         throw new RequiredObjectIsNullException("Organisme", "creation", "Plateforme");
      }
      organisme.setPlateforme(plateformeDao.mergeObject(plateforme));

      BeanValidator.validateObject(organisme, new Validator[] {organismeValidator});
      if(!findDoublonManager(organisme)){
         organismeDao.createObject(organisme);
         log.info("Enregistrement objet Organisme " + organisme.toString());
      }else{
         log.warn("Doublon lors creation objet Organisme {}",  organisme);
         throw new DoublonFoundException("Organisme", "creation");
      }
      
   }

   @Override
   public void updateObjectManager(Organisme obj){
      BeanValidator.validateObject(obj, new Validator[] {organismeValidator});
      if(!findDoublonManager(obj)){
         organismeDao.updateObject(obj);
         log.info("Modification objet Organisme " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet Organisme {}",  obj);
         throw new DoublonFoundException("Organisme", "modification");
      }
      
   }

   @Override
   public boolean findDoublonManager(Organisme o){
      if(o != null){
         final Organisme organisme = o;
         Plateforme plateforme = organisme.getPlateforme();
         if(organisme.getId() == null){
            if(plateforme == null) {
               return organismeDao.findByOrder().contains(organisme);
            }
            return organismeDao.findByPfOrder(plateforme).contains(organisme);
         }
         return organismeDao.findByPfExcludedId(plateforme, organisme.getId()).contains(organisme);
      }
      return false;
   }

   @Override
   public void removeObjectManager(Organisme obj){
      if(obj != null){
         organismeDao.removeObject(obj.getId());
         log.info("Suppression objet Organisme " + obj.toString());
      }else{
         log.warn("Suppression d'un Organisme null");
      }
   }

   @Override
   public boolean isUsedObjectManager(Organisme obj){
      if(obj != null) {
         log.info("Test si des études de la plateforme "+ obj.getPlateforme() +" sont rattachées à l'organisme " + obj.getNom());
         return ( etudeDao.countByPfAndOrganismePromoteurId(obj.getPlateforme(), obj.getId()) > 0);
      }else{
         log.warn("Test nombre d'études sur l'Organisme null");
         return false;
      }
   }
}