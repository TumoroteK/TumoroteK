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

import fr.aphp.tumorotek.dao.impression.BlocImpressionDao;
import fr.aphp.tumorotek.dao.impression.BlocImpressionTemplateDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplatePK;
import fr.aphp.tumorotek.model.impression.Template;

public class BlocImpressionTemplateManagerImpl implements BlocImpressionTemplateManager
{

   private final Logger log = LoggerFactory.getLogger(BlocImpressionTemplateManager.class);

   /** Bean Dao. */
   private BlocImpressionTemplateDao blocImpressionTemplateDao;

   /** Bean Dao. */
   private BlocImpressionDao blocImpressionDao;

   /** Bean Dao. */
   private TemplateDao templateDao;

   public void setBlocImpressionTemplateDao(final BlocImpressionTemplateDao bDao){
      this.blocImpressionTemplateDao = bDao;
   }

   public void setBlocImpressionDao(final BlocImpressionDao bDao){
      this.blocImpressionDao = bDao;
   }

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   @Override
   public BlocImpressionTemplate findByIdManager(final BlocImpressionTemplatePK pk){
      return blocImpressionTemplateDao.findById(pk);
   }

   @Override
   public List<BlocImpressionTemplate> findAllObjectsManager(){
      log.debug("Recherche de tous les BlocImpressionTemplates");
      return blocImpressionTemplateDao.findAll();
   }

   @Override
   public List<BlocImpressionTemplate> findByExcludedPKManager(final BlocImpressionTemplatePK pk){
      if(pk != null){
         return blocImpressionTemplateDao.findByExcludedPK(pk);
      }
      return blocImpressionTemplateDao.findAll();
   }

   @Override
   public List<BlocImpressionTemplate> findByTemplateManager(final Template template){
      log.debug("Recherche de tous les BlocImpressionTemplates d'un Template.");
      if(template != null){
         return blocImpressionTemplateDao.findByTemplate(template);
      }
      return new ArrayList<>();
   }

   @Override
   public Boolean findDoublonManager(final Template template, final BlocImpression blocImpression){

      final BlocImpressionTemplatePK pk = new BlocImpressionTemplatePK(template, blocImpression);

      return (blocImpressionTemplateDao.findById(pk) != null);
   }

   @Override
   public void validateObjectManager(final Template template, final BlocImpression blocImpression){
      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant  lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "creation", "Template");
      }

      //blocImpression required
      if(blocImpression == null){
         log.warn("Objet obligatoire BlocImpression manquant  lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "creation", "BlocImpression");
      }

      //Doublon
      if(template.getTemplateId() != null){
         if(findDoublonManager(template, blocImpression)){

            log.warn("Doublon lors validation objet BlocImpressionTemplate");
            throw new DoublonFoundException("BlocImpressionTemplate", "creation");
         }
      }
   }

   @Override
   public void createObjectManager(final BlocImpressionTemplate blocImpressionTemplate, final Template template,
      final BlocImpression blocImpression){

      // validation de l'objet à créer
      validateObjectManager(template, blocImpression);

      blocImpressionTemplate.setTemplate(templateDao.mergeObject(template));
      blocImpressionTemplate.setBlocImpression(blocImpressionDao.mergeObject(blocImpression));

      // création
      blocImpressionTemplateDao.createObject(blocImpressionTemplate);

      log.info("Enregistrement objet BlocImpressionTemplate " + blocImpressionTemplate.toString());

   }

   @Override
   public void updateObjectManager(final BlocImpressionTemplate blocImpressionTemplate, final Template template,
      final BlocImpression blocImpression){

      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant  lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "modification", "Template");
      }
      blocImpressionTemplate.setTemplate(templateDao.mergeObject(template));

      //blocImpression required
      if(blocImpression == null){
         log.warn("Objet obligatoire BlocImpression manquant  lors de la validation d'un BlocImpressionTemplate");
         throw new RequiredObjectIsNullException("BlocImpressionTemplate", "modification", "BlocImpression");
      }
      blocImpressionTemplate.setBlocImpression(blocImpressionDao.mergeObject(blocImpression));

      // création
      blocImpressionTemplateDao.updateObject(blocImpressionTemplate);

      log.info("Enregistrement objet BlocImpressionTemplate " + blocImpressionTemplate.toString());
   }

   @Override
   public void removeObjectManager(final BlocImpressionTemplate blocImpressionTemplate){
      if(blocImpressionTemplate != null){
         blocImpressionTemplateDao.removeObject(blocImpressionTemplate.getPk());
         log.info("Suppression de l'objet BlocImpressionTemplate : " + blocImpressionTemplate.toString());
      }else{
         log.warn("Suppression d'un BlocImpressionTemplate null");
      }
   }
}
