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

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.contexte.ProtocoleDao;
import fr.aphp.tumorotek.manager.context.ProtocoleManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.ProtocoleValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.Protocole;

/**
 *
 * Implémentation du manager du bean de domaine Protocole.
 * Classe créée le 13/02/2012.
 *
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.6
 *
 */
public class ProtocoleManagerImpl implements ProtocoleManager
{

   private final Logger log = LoggerFactory.getLogger(ProtocoleManager.class);

   private ProtocoleDao protocoleDao;

   private ProtocoleValidator protocoleValidator;

   private PlateformeDao plateformeDao;

   public ProtocoleManagerImpl(){}

   /* Properties setters */
   public void setProtocoleDao(final ProtocoleDao pDao){
      this.protocoleDao = pDao;
   }

   public void setProtocoleValidator(final ProtocoleValidator pValidator){
      this.protocoleValidator = pValidator;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   @Override
   public void createObjectManager(final Protocole obj){

      final Protocole pt = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(pt.getPlateforme() == null){
         log.warn("Objet obligatoire Plateforme " + "manquant lors de la creation " + "d'un objet Protocole");
         throw new RequiredObjectIsNullException("Protocole", "creation", "Plateforme");
      }
      pt.setPlateforme(plateformeDao.mergeObject(pt.getPlateforme()));

      BeanValidator.validateObject(pt, new Validator[] {protocoleValidator});
      if(!findDoublonManager(pt)){
         protocoleDao.createObject(pt);
         log.info("Enregistrement objet Protocole " + pt.toString());
      }else{
         log.warn("Doublon lors creation objet Protocole " + pt.toString());
         throw new DoublonFoundException("Protocole", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Protocole obj){
      BeanValidator.validateObject(obj, new Validator[] {protocoleValidator});
      if(!findDoublonManager(obj)){
         protocoleDao.updateObject(obj);
         log.info("Modification objet Protocole " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet Protocole " + obj.toString());
         throw new DoublonFoundException("Protocole", "modification");
      }
   }

   @Override
   public List<Protocole> findAllObjectsManager(){
      return protocoleDao.findAll();
   }

   @Override
   public void removeObjectManager(final Protocole obj){
      if(obj != null){
         protocoleDao.removeObject(obj.getId());
         log.info("Suppression objet Protocole " + obj.toString());
      }else{
         log.warn("Suppression d'un Protocole null");
      }
   }

   @Override
   public boolean findDoublonManager(final Protocole o){
      if(o != null){
         final Protocole pt = o;
         if(pt.getId() == null){
            return protocoleDao.findAll().contains(pt);
         }
         return protocoleDao.findByExcludedId(pt.getId()).contains(pt);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final Protocole o){
      final Protocole pt = protocoleDao.mergeObject(o);
      return pt.getPrelevements().size() > 0;
   }

   @Override
   public List<Protocole> findByOrderManager(final Plateforme pf){
      return protocoleDao.findByPfOrder(pf);
   }

   @Override
   public Protocole findByIdManager(final Integer id){
      return protocoleDao.findById(id);
   }

   @Override
   public List<Protocole> findByOrderManager(){
      return protocoleDao.findByOrder();
   }
}