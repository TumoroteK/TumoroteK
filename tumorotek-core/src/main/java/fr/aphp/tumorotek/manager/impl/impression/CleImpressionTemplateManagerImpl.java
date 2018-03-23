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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.impression.CleImpressionDao;
import fr.aphp.tumorotek.dao.impression.CleImpressionTemplateDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.CleImpressionTemplateManager;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.CleImpressionTemplate;
import fr.aphp.tumorotek.model.impression.CleImpressionTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

public class CleImpressionTemplateManagerImpl implements CleImpressionTemplateManager
{

   private final Log log = LogFactory.getLog(CleImpressionTemplateManager.class);

   /** Bean Dao. */
   private CleImpressionTemplateDao cleImpressionTemplateDao;
   /** Bean Dao. */
   private CleImpressionDao cleImpressionDao;
   /** Bean Dao. */
   private TemplateDao templateDao;

   public void setCleImpressionTemplateDao(final CleImpressionTemplateDao bDao){
      this.cleImpressionTemplateDao = bDao;
   }

   public void setCleImpressionDao(final CleImpressionDao bDao){
      this.cleImpressionDao = bDao;
   }

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   @Override
   public CleImpressionTemplate findByIdManager(final CleImpressionTemplatePK pk){
      return cleImpressionTemplateDao.findById(pk);
   }

   @Override
   public List<CleImpressionTemplate> findAllObjectsManager(){
      log.debug("Recherche de tous les BlocImpressionTemplates");
      return cleImpressionTemplateDao.findAll();
   }

   @Override
   public List<CleImpressionTemplate> findByExcludedPKManager(final CleImpressionTemplatePK pk){
      if(pk != null){
         return cleImpressionTemplateDao.findByExcludedPK(pk);
      }else{
         return cleImpressionTemplateDao.findAll();
      }
   }

   @Override
   public List<CleImpressionTemplate> findByTemplateManager(final Template template){
      log.debug("Recherche de tous les BlocImpressionTemplates d'un " + "Template.");
      if(template != null){
         return cleImpressionTemplateDao.findByTemplate(template);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Template template, final CleImpression cleImpression){

      final CleImpressionTemplatePK pk = new CleImpressionTemplatePK(template, cleImpression);

      return (cleImpressionTemplateDao.findById(pk) != null);
   }

   @Override
   public void validateObjectManager(final Template template, final CleImpression cleImpression){
      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "creation", "Template");
      }

      //blocImpression required
      if(cleImpression == null){
         log.warn("Objet obligatoire BlocImpression manquant" + " lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "creation", "BlocImpression");
      }

      //Doublon
      if(template.getTemplateId() != null){
         if(findDoublonManager(template, cleImpression)){

            log.warn("Doublon lors validation objet " + "BlocImpressionTemplate");
            throw new DoublonFoundException("BlocImpressionTemplate", "creation");
         }
      }
   }

   @Override
   public void createObjectManager(final CleImpressionTemplate cleImpressionTemplate, final Template template,
      final CleImpression cleImpression){

      // validation de l'objet à créer
      validateObjectManager(template, cleImpression);

      cleImpressionTemplate.setTemplate(templateDao.mergeObject(template));
      cleImpressionTemplate.setCleImpression(cleImpressionDao.mergeObject(cleImpression));

      // création
      cleImpressionTemplateDao.createObject(cleImpressionTemplate);

      log.info("Enregistrement objet BlocImpressionTemplate " + cleImpressionTemplate.toString());

   }

   @Override
   public void updateObjectManager(final CleImpressionTemplate cleImpressionTemplate, final Template template,
      final CleImpression cleImpression){

      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "modification", "Template");
      }else{
         cleImpressionTemplate.setTemplate(templateDao.mergeObject(template));
      }

      //blocImpression required
      if(cleImpression == null){
         log.warn("Objet obligatoire BlocImpression manquant" + " lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "modification", "BlocImpression");
      }else{
         cleImpressionTemplate.setCleImpression(cleImpressionDao.mergeObject(cleImpression));
      }

      // création
      cleImpressionTemplateDao.updateObject(cleImpressionTemplate);

      log.info("Enregistrement objet BlocImpressionTemplate " + cleImpressionTemplate.toString());
   }

   @Override
   public void removeObjectManager(final CleImpressionTemplate cleImpressionTemplate){
      if(cleImpressionTemplate != null){
         cleImpressionTemplateDao.removeObject(cleImpressionTemplate.getPk());
         log.info("Suppression de l'objet BlocImpressionTemplate : " + cleImpressionTemplate.toString());
      }else{
         log.warn("Suppression d'un BlocImpressionTemplate null");
      }
   }
}
