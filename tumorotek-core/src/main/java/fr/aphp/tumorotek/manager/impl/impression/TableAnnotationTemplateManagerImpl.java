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
package fr.aphp.tumorotek.manager.impl.impression;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.annotation.TableAnnotationDao;
import fr.aphp.tumorotek.dao.impression.TableAnnotationTemplateDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

public class TableAnnotationTemplateManagerImpl implements TableAnnotationTemplateManager
{

   private final Logger log = LoggerFactory.getLogger(TableAnnotationTemplateManager.class);

   /** Bean Dao. */
   private TableAnnotationTemplateDao tableAnnotationTemplateDao;

   /** Bean Dao. */
   private TemplateDao templateDao;

   /** Bean Dao. */
   private TableAnnotationDao tableAnnotationDao;

   public void setTableAnnotationTemplateDao(final TableAnnotationTemplateDao tDao){
      this.tableAnnotationTemplateDao = tDao;
   }

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   public void setTableAnnotationDao(final TableAnnotationDao tDao){
      this.tableAnnotationDao = tDao;
   }

   @Override
   public TableAnnotationTemplate findByIdManager(final TableAnnotationTemplatePK pk){
      return tableAnnotationTemplateDao.findById(pk);
   }

   @Override
   public List<TableAnnotationTemplate> findAllObjectsManager(){
      log.debug("Recherche de tous les TableAnnotationTemplates");
      return tableAnnotationTemplateDao.findAll();
   }

   @Override
   public List<TableAnnotationTemplate> findByExcludedPKManager(final TableAnnotationTemplatePK pk){
      if(pk != null){
         return tableAnnotationTemplateDao.findByExcludedPK(pk);
      }else{
         return tableAnnotationTemplateDao.findAll();
      }
   }

   @Override
   public List<TableAnnotationTemplate> findByTemplateManager(final Template template){
      log.debug("Recherche de tous les TableAnnotationTemplates d'un " + "Template.");
      if(template != null){
         return tableAnnotationTemplateDao.findByTemplate(template);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Template template, final TableAnnotation tableAnnotation){

      final TableAnnotationTemplatePK pk = new TableAnnotationTemplatePK(template, tableAnnotation);

      return (tableAnnotationTemplateDao.findById(pk) != null);
   }

   @Override
   public void validateObjectManager(final Template template, final TableAnnotation tableAnnotation){

      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un TableAnnotationTemplate");
         throw new RequiredObjectIsNullException("TableAnnotationTemplate", "creation", "Template");
      }

      //tableAnnotation required
      if(tableAnnotation == null){
         log.warn("Objet obligatoire TableAnnotation manquant" + " lors de la validation d'un TableAnnotationTemplate");
         throw new RequiredObjectIsNullException("TableAnnotationTemplate", "creation", "TableAnnotation");
      }

      //Doublon
      if(template.getTemplateId() != null){
         if(findDoublonManager(template, tableAnnotation)){

            log.warn("Doublon lors validation objet " + "TableAnnotationTemplate");
            throw new DoublonFoundException("TableAnnotationTemplate", "creation");
         }
      }
   }

   @Override
   public void createObjectManager(final TableAnnotationTemplate tableAnnotationTemplate, final Template template,
      final TableAnnotation tableAnnotation){

      // validation de l'objet à créer
      validateObjectManager(template, tableAnnotation);

      tableAnnotationTemplate.setTemplate(templateDao.mergeObject(template));
      tableAnnotationTemplate.setTableAnnotation(tableAnnotationDao.mergeObject(tableAnnotation));

      // création
      tableAnnotationTemplateDao.createObject(tableAnnotationTemplate);

      log.info("Enregistrement objet TableAnnotationTemplate " + tableAnnotationTemplate.toString());
   }

   @Override
   public void updateObjectManager(final TableAnnotationTemplate tableAnnotationTemplate, final Template template,
      final TableAnnotation tableAnnotation){
      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un TableAnnotationTemplate");
         throw new RequiredObjectIsNullException("TableAnnotationTemplate", "modification", "Template");
      }else{
         tableAnnotationTemplate.setTemplate(templateDao.mergeObject(template));
      }

      //tableAnnotation required
      if(tableAnnotation == null){
         log.warn("Objet obligatoire TableAnnotation manquant" + " lors de la validation d'un TableAnnotationTemplate");
         throw new RequiredObjectIsNullException("TableAnnotationTemplate", "modification", "TableAnnotation");
      }else{
         tableAnnotationTemplate.setTableAnnotation(tableAnnotationDao.mergeObject(tableAnnotation));
      }

      // création
      tableAnnotationTemplateDao.updateObject(tableAnnotationTemplate);

      log.info("Enregistrement objet TableAnnotationTemplate " + tableAnnotationTemplate.toString());
   }

   @Override
   public void removeObjectManager(final TableAnnotationTemplate tableAnnotationTemplate){
      if(tableAnnotationTemplate != null){
         tableAnnotationTemplateDao.removeObject(tableAnnotationTemplate.getPk());
         log.info("Suppression de l'objet TableAnnotationTemplate : " + tableAnnotationTemplate.toString());
      }else{
         log.warn("Suppression d'un TableAnnotationTemplate null");
      }
   }
}