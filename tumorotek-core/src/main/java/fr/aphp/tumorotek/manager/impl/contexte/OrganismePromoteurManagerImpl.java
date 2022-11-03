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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.OrganismePromoteurDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.gatsbi.EtudeDao;
import fr.aphp.tumorotek.manager.context.OrganismePromoteurManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.OrganismePromoteurValidator;
import fr.aphp.tumorotek.model.contexte.OrganismePromoteur;
import fr.aphp.tumorotek.model.contexte.Plateforme;


public class OrganismePromoteurManagerImpl implements OrganismePromoteurManager
{
   private final Log log = LogFactory.getLog(OrganismePromoteurManagerImpl.class);

   private OrganismePromoteurDao organismePromoteurDao;
   
   private OrganismePromoteurValidator organismePromoteurValidator;
   
   private PlateformeDao plateformeDao;
   
   private EtudeDao etudeDao;

   public OrganismePromoteurManagerImpl(){}
   
   public void setOrganismePromoteurDao(OrganismePromoteurDao organismePromoteurDao){
      this.organismePromoteurDao = organismePromoteurDao;
   }

   public void setOrganismePromoteurValidator(OrganismePromoteurValidator organismePromoteurValidator){
      this.organismePromoteurValidator = organismePromoteurValidator;
   }

   public void setPlateformeDao(PlateformeDao plateformeDao){
      this.plateformeDao = plateformeDao;
   }

   public void setEtudeDao(EtudeDao etudeDao){
      this.etudeDao = etudeDao;
   }

   @Override
   public List<OrganismePromoteur> findByOrderManager(final Plateforme pf){
      return organismePromoteurDao.findByPfOrder(pf);
   }

   @Override
   public OrganismePromoteur findByIdManager(Integer id){
      return organismePromoteurDao.findById(id);
   }

   @Override
   public List<OrganismePromoteur> findByOrderManager(){
      return organismePromoteurDao.findByOrder();
   }

   @Override
   public void createObjectManager(OrganismePromoteur obj){
      final OrganismePromoteur organismePromoteur = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      Plateforme plateforme = organismePromoteur.getPlateforme();
      if( plateforme == null){
         log.warn("Objet obligatoire Plateforme " + "manquant lors de la creation " + "d'un objet OrganismePromoteur");
         throw new RequiredObjectIsNullException("OrganismePromoteur", "creation", "Plateforme");
      }
      organismePromoteur.setPlateforme(plateformeDao.mergeObject(plateforme));

      BeanValidator.validateObject(organismePromoteur, new Validator[] {organismePromoteurValidator});
      if(!findDoublonManager(organismePromoteur)){
         organismePromoteurDao.createObject(organismePromoteur);
         log.info("Enregistrement objet OrganismePromoteur " + organismePromoteur.toString());
      }else{
         log.warn("Doublon lors creation objet OrganismePromoteur " + organismePromoteur.toString());
         throw new DoublonFoundException("OrganismePromoteur", "creation");
      }
      
   }

   @Override
   public void updateObjectManager(OrganismePromoteur obj){
      BeanValidator.validateObject(obj, new Validator[] {organismePromoteurValidator});
      if(!findDoublonManager(obj)){
         organismePromoteurDao.updateObject(obj);
         log.info("Modification objet OrganismePromoteur " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet OrganismePromoteur " + obj.toString());
         throw new DoublonFoundException("OrganismePromoteur", "modification");
      }
      
   }

   @Override
   public boolean findDoublonManager(OrganismePromoteur o){
      if(o != null){
         final OrganismePromoteur organismePromoteur = o;
         Plateforme plateforme = organismePromoteur.getPlateforme();
         if(organismePromoteur.getId() == null){
            if(plateforme == null) {
               return organismePromoteurDao.findByOrder().contains(organismePromoteur);
            }
            return organismePromoteurDao.findByPfOrder(plateforme).contains(organismePromoteur);
         }
         return organismePromoteurDao.findByPfExcludedId(plateforme, organismePromoteur.getId()).contains(organismePromoteur);
      }
      return false;
   }

   @Override
   public void removeObjectManager(OrganismePromoteur obj){
      if(obj != null){
         organismePromoteurDao.removeObject(obj.getId());
         log.info("Suppression objet OrganismePromoteur " + obj.toString());
      }else{
         log.warn("Suppression d'un OrganismePromoteur null");
      }
   }

   @Override
   public boolean isUsedObjectManager(OrganismePromoteur obj){
      if(obj != null) {
         log.info("Test si des études de la plateforme "+ obj.getPlateforme() +" sont rattachées à l'organisme promoteur " + obj.getNom());
         return ( etudeDao.countByPfAndOrganismePromoteurId(obj.getPlateforme(), obj.getId()) > 0);
      }else{
         log.warn("Test nombre d'études sur l'OrganismePromoteur null");
         return false;
      }
   }
}
