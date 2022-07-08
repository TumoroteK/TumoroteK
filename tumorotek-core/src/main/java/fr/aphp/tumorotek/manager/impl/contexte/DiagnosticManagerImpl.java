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

import fr.aphp.tumorotek.dao.contexte.DiagnosticDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.context.DiagnosticManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.contexte.DiagnosticValidator;
import fr.aphp.tumorotek.model.contexte.Diagnostic;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine Diagnostic.
 * Classe créée le 13/02/2012.
 *
 *
 * @author Mathieu BARTHELEMY
 * @since2.0.6
 * @version 2.2.0
 *
 */
public class DiagnosticManagerImpl implements DiagnosticManager
{

   private final Log log = LogFactory.getLog(DiagnosticManager.class);

   private DiagnosticDao diagnosticDao;

   private DiagnosticValidator diagnosticValidator;

   private PlateformeDao plateformeDao;

   public DiagnosticManagerImpl(){}

   /* Properties setters */
   public void setDiagnosticDao(final DiagnosticDao pDao){
      this.diagnosticDao = pDao;
   }

   public void setDiagnosticValidator(final DiagnosticValidator pValidator){
      this.diagnosticValidator = pValidator;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   @Override
   public void createObjectManager(final Diagnostic obj){

      final Diagnostic pt = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(pt.getPlateforme() == null){
         log.warn("Objet obligatoire Plateforme " + "manquant lors de la creation " + "d'un objet Diagnostic");
         throw new RequiredObjectIsNullException("Diagnostic", "creation", "Plateforme");
      }
      pt.setPlateforme(plateformeDao.mergeObject(pt.getPlateforme()));

      BeanValidator.validateObject(pt, new Validator[] {diagnosticValidator});
      if(!findDoublonManager(pt)){
         diagnosticDao.createObject(pt);
         log.info("Enregistrement objet Diagnostic " + pt.toString());
      }else{
         log.warn("Doublon lors creation objet Diagnostic " + pt.toString());
         throw new DoublonFoundException("Diagnostic", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Diagnostic obj){
      BeanValidator.validateObject(obj, new Validator[] {diagnosticValidator});
      if(!findDoublonManager(obj)){
         diagnosticDao.updateObject(obj);
         log.info("Modification objet Diagnostic " + obj.toString());
      }else{
         log.warn("Doublon lors modification objet Diagnostic " + obj.toString());
         throw new DoublonFoundException("Diagnostic", "modification");
      }
   }

   @Override
   public List<Diagnostic> findAllObjectsManager(){
      return diagnosticDao.findAll();
   }

   @Override
   public void removeObjectManager(final Diagnostic obj){
      if(obj != null){
         diagnosticDao.removeObject(obj.getId());
         log.info("Suppression objet Diagnostic " + obj.toString());
      }else{
         log.warn("Suppression d'un Diagnostic null");
      }
   }

   @Override
   public boolean findDoublonManager(final Diagnostic o){
      if(o != null){
         final Diagnostic pt = o;
         if(pt.getId() == null){
            return diagnosticDao.findAll().contains(pt);
         }
         return diagnosticDao.findByExcludedId(pt.getId()).contains(pt);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final Diagnostic o){
      final Diagnostic pt = diagnosticDao.mergeObject(o);
      return pt.getMaladies().size() > 0;
   }

   @Override
   public List<Diagnostic> findByOrderManager(final Plateforme pf){
      return diagnosticDao.findByPfOrder(pf);
   }

   @Override
   public Diagnostic findByIdManager(final Integer id){
      return null;
   }

   @Override
   public List<Diagnostic> findByOrderManager(){
      return diagnosticDao.findByOrder();
   }
}
