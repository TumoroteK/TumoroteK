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
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.BlocImpressionTemplateManager;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.manager.impression.TableAnnotationTemplateManager;
import fr.aphp.tumorotek.manager.impression.TemplateManager;
import fr.aphp.tumorotek.manager.io.export.CleImpressionManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.impression.TemplateValidator;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.impression.BlocImpressionTemplate;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.TableAnnotationTemplate;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.systeme.Entite;

public class TemplateManagerImpl implements TemplateManager
{

   private final Logger log = LoggerFactory.getLogger(TemplateManager.class);

   /** Bean Dao. */
   private TemplateDao templateDao;

   /** Bean Dao. */
   private BanqueDao banqueDao;

   /** Bean Dao. */
   private EntiteDao entiteDao;

   /** Bean validator. */
   private TemplateValidator templateValidator;

   /** Bean Manager. */
   private BlocImpressionTemplateManager blocImpressionTemplateManager;

   /** Bean Manager. */
   private ChampImprimeManager champImprimeManager;

   /** Bean Manager. */
   private CleImpressionManager cleImpressionManager;

   /** Bean Manager. */
   private TableAnnotationTemplateManager tableAnnotationTemplateManager;

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setTemplateValidator(final TemplateValidator tValidator){
      this.templateValidator = tValidator;
   }

   public void setBlocImpressionTemplateManager(final BlocImpressionTemplateManager bManager){
      this.blocImpressionTemplateManager = bManager;
   }

   public void setChampImprimeManager(final ChampImprimeManager cManager){
      this.champImprimeManager = cManager;
   }

   public CleImpressionManager getCleImpressionManager(){
      return cleImpressionManager;
   }

   public void setCleImpressionManager(final CleImpressionManager cleManager){
      this.cleImpressionManager = cleManager;
   }

   public void setTableAnnotationTemplateManager(final TableAnnotationTemplateManager tManager){
      this.tableAnnotationTemplateManager = tManager;
   }

   @Override
   public Template findByIdManager(final Integer templateId){
      return templateDao.findById(templateId);
   }

   @Override
   public List<Template> findAllObjectsManager(){
      log.debug("Recherche de tous les templates.");
      return templateDao.findAll();
   }

   @Override
   public List<Template> findByBanqueManager(final Banque banque){
      log.debug("Recherche de tous les templates d'une banque.");
      if(banque != null){
         return templateDao.findByBanque(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<Template> findByBanqueEntiteManager(final Banque banque, final Entite entite){
      log.debug("Recherche de tous les templates d'une banque pour " + "une entité.");
      if(banque != null && entite != null){
         return templateDao.findByBanqueEntite(banque, entite);
      }
      return new ArrayList<>();
   }

   @Override
   public Boolean findDoublonManager(final Template template){
      if(template != null){
         if(template.getTemplateId() == null){
            return templateDao.findByBanque(template.getBanque()).contains(template);
         }
         return templateDao.findByExcludedId(template.getBanque(), template.getTemplateId()).contains(template);
      }
      return false;
   }

   @Override
   public void createObjectManager(final Template template, final Banque banque, final Entite entite,
      final List<BlocImpressionTemplate> blocs, final List<ChampImprime> champs, final List<TableAnnotationTemplate> annotations){

      // banque required
      if(banque != null){
         template.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la création d'un Template");
         throw new RequiredObjectIsNullException("Template", "creation", "Banque");
      }

      // entite required
      if(entite != null){
         template.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la création d'un Template");
         throw new RequiredObjectIsNullException("Template", "creation", "Entite");
      }

      //Doublon
      if(!findDoublonManager(template)){

         // validation due l'utilisateur
         BeanValidator.validateObject(template, new Validator[] {templateValidator});

         // validation des blocs
         if(blocs != null){
            for(int i = 0; i < blocs.size(); i++){
               final BlocImpressionTemplate obj = blocs.get(i);

               blocImpressionTemplateManager.validateObjectManager(template, obj.getBlocImpression());
            }
         }

         // validation des champs
         if(champs != null){
            for(int i = 0; i < champs.size(); i++){
               final ChampImprime obj = champs.get(i);

               champImprimeManager.validateObjectManager(template, obj.getChampEntite(), obj.getBlocImpression());
            }
         }

         // validation des tableannotation
         if(annotations != null){
            for(int i = 0; i < annotations.size(); i++){
               final TableAnnotationTemplate obj = annotations.get(i);

               tableAnnotationTemplateManager.validateObjectManager(template, obj.getTableAnnotation());
            }
         }

         templateDao.createObject(template);
         log.info("Enregistrement objet Template " + template.toString());

         // enregistrements des associations
         updateAssociations(template, blocs, blocs, champs, champs, annotations, annotations);

      }else{
         log.warn("Doublon lors creation objet Template " + template.toString());
         throw new DoublonFoundException("Template", "creation");
      }
   }

   @Override
   public void createObjectManager(final Template template, final Banque banque, final Entite entite,
      final List<BlocImpressionTemplate> blocs, final List<ChampImprime> champs, final List<TableAnnotationTemplate> annotations,
      final List<CleImpression> cles){

      // banque required
      if(banque != null){
         template.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la création d'un Template");
         throw new RequiredObjectIsNullException("Template", "creation", "Banque");
      }

      // entite required
      if(entite != null){
         template.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la création d'un Template");
         throw new RequiredObjectIsNullException("Template", "creation", "Entite");
      }

      //Doublon
      if(!findDoublonManager(template)){

         // validation due l'utilisateur
         BeanValidator.validateObject(template, new Validator[] {templateValidator});

         // validation des blocs
         if(blocs != null){
            for(int i = 0; i < blocs.size(); i++){
               final BlocImpressionTemplate obj = blocs.get(i);

               blocImpressionTemplateManager.validateObjectManager(template, obj.getBlocImpression());
            }
         }

         // validation des champs
         if(champs != null){
            for(int i = 0; i < champs.size(); i++){
               final ChampImprime obj = champs.get(i);

               champImprimeManager.validateObjectManager(template, obj.getChampEntite(), obj.getBlocImpression());
            }
         }

         // validation des tableannotation
         if(annotations != null){
            for(int i = 0; i < annotations.size(); i++){
               final TableAnnotationTemplate obj = annotations.get(i);

               tableAnnotationTemplateManager.validateObjectManager(template, obj.getTableAnnotation());
            }
         }

         if(null != cles){
            template.setCleImpressionList(cles);
         }

         templateDao.createObject(template);
         log.info("Enregistrement objet Template " + template.toString());

         // enregistrements des associations
         updateAssociations(template, blocs, blocs, champs, champs, annotations, annotations, cles);

      }else{
         log.warn("Doublon lors creation objet Template " + template.toString());
         throw new DoublonFoundException("Template", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Template template, final Banque banque, final Entite entite,
      final List<BlocImpressionTemplate> blocs, final List<BlocImpressionTemplate> blocsToCreate, final List<ChampImprime> champs,
      final List<ChampImprime> champsToCreate, final List<TableAnnotationTemplate> annotations,
      final List<TableAnnotationTemplate> annotationsToCreate){

      // banque required
      if(banque != null){
         template.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la modification d'un Template");
         throw new RequiredObjectIsNullException("Template", "modification", "Banque");
      }

      // entite required
      if(entite != null){
         template.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la modification d'un Template");
         throw new RequiredObjectIsNullException("Template", "modification", "Entite");
      }

      //Doublon
      if(!findDoublonManager(template)){

         // validation due l'utilisateur
         BeanValidator.validateObject(template, new Validator[] {templateValidator});

         // validation des blocs
         if(blocsToCreate != null){
            for(int i = 0; i < blocsToCreate.size(); i++){
               final BlocImpressionTemplate obj = blocsToCreate.get(i);

               blocImpressionTemplateManager.validateObjectManager(template, obj.getBlocImpression());
            }
         }

         // validation des champs
         if(champsToCreate != null){
            for(int i = 0; i < champsToCreate.size(); i++){
               final ChampImprime obj = champsToCreate.get(i);

               champImprimeManager.validateObjectManager(template, obj.getChampEntite(), obj.getBlocImpression());
            }
         }

         // validation des tableannotation
         if(annotationsToCreate != null){
            for(int i = 0; i < annotationsToCreate.size(); i++){
               final TableAnnotationTemplate obj = annotationsToCreate.get(i);

               tableAnnotationTemplateManager.validateObjectManager(template, obj.getTableAnnotation());
            }
         }

         templateDao.updateObject(template);
         log.info("Enregistrement objet Template " + template.toString());

         // enregistrements des associations
         updateAssociations(template, blocs, blocsToCreate, champs, champsToCreate, annotations, annotationsToCreate);

      }else{
         log.warn("Doublon lors creation objet Template " + template.toString());
         throw new DoublonFoundException("Template", "modification");
      }

   }

   @Override
   public void updateObjectManager(final Template template, final Banque banque, final Entite entite,
      final List<BlocImpressionTemplate> blocs, final List<BlocImpressionTemplate> blocsToCreate, final List<ChampImprime> champs,
      final List<ChampImprime> champsToCreate, final List<TableAnnotationTemplate> annotations,
      final List<TableAnnotationTemplate> annotationsToCreate, final List<CleImpression> cles){

      // banque required
      if(banque != null){
         template.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la modification d'un Template");
         throw new RequiredObjectIsNullException("Template", "modification", "Banque");
      }

      // entite required
      if(entite != null){
         template.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la modification d'un Template");
         throw new RequiredObjectIsNullException("Template", "modification", "Entite");
      }

      //Doublon
      if(!findDoublonManager(template)){

         // validation due l'utilisateur
         BeanValidator.validateObject(template, new Validator[] {templateValidator});

         // validation des blocs
         if(blocsToCreate != null){
            for(int i = 0; i < blocsToCreate.size(); i++){
               final BlocImpressionTemplate obj = blocsToCreate.get(i);

               blocImpressionTemplateManager.validateObjectManager(template, obj.getBlocImpression());
            }
         }

         // validation des champs
         if(champsToCreate != null){
            for(int i = 0; i < champsToCreate.size(); i++){
               final ChampImprime obj = champsToCreate.get(i);

               champImprimeManager.validateObjectManager(template, obj.getChampEntite(), obj.getBlocImpression());
            }
         }

         // validation des tableannotation
         if(annotationsToCreate != null){
            for(int i = 0; i < annotationsToCreate.size(); i++){
               final TableAnnotationTemplate obj = annotationsToCreate.get(i);

               tableAnnotationTemplateManager.validateObjectManager(template, obj.getTableAnnotation());
            }
         }

         if(null != cles){
            updateClesImpression(cles);
            template.setCleImpressionList(cles);
         }

         templateDao.updateObject(template);
         log.info("Enregistrement objet Template " + template.toString());

         // enregistrements des associations
         updateAssociations(template, blocs, blocsToCreate, champs, champsToCreate, annotations, annotationsToCreate);

      }else{
         log.warn("Doublon lors creation objet Template " + template.toString());
         throw new DoublonFoundException("Template", "modification");
      }

   }

   @Override
   public void removeObjectManager(final Template template){
      if(template != null){
         // suppression des BlocImpressionTemplates
         final List<BlocImpressionTemplate> blocs = blocImpressionTemplateManager.findByTemplateManager(template);
         for(int i = 0; i < blocs.size(); i++){
            blocImpressionTemplateManager.removeObjectManager(blocs.get(i));
         }

         // suppression des ChampImprimes
         final List<ChampImprime> champs = champImprimeManager.findByTemplateManager(template);
         for(int i = 0; i < champs.size(); i++){
            champImprimeManager.removeObjectManager(champs.get(i));
         }

         // suppression des TableAnnotationTemplate
         final List<TableAnnotationTemplate> tables = tableAnnotationTemplateManager.findByTemplateManager(template);
         for(int i = 0; i < tables.size(); i++){
            tableAnnotationTemplateManager.removeObjectManager(tables.get(i));
         }

         //         // suppression des CleImpression
         //         final List<CleImpression> clesImpression = cleManager.findByTemplateManager(template);
         //         for(int i = 0; i < clesImpression.size(); i++){
         //            cleManager.removeObjectManager(clesImpression.get(i));
         //         }

         templateDao.removeObject(template.getTemplateId());
         log.info("Suppression de l'objet Template : " + template.toString());
      }else{
         log.warn("Suppression d'un Template null");
      }
   }

   public void updateAssociations(final Template template, final List<BlocImpressionTemplate> blocs,
      final List<BlocImpressionTemplate> blocsToCreate, final List<ChampImprime> champsImprime,
      final List<ChampImprime> champsToCreate, final List<TableAnnotationTemplate> annotations,
      final List<TableAnnotationTemplate> annoToCreate){
      final Template temp = templateDao.mergeObject(template);

      // gestion des BlocImpressionTemplate
      List<BlocImpressionTemplate> oldBlocs = new ArrayList<>();
      final List<BlocImpressionTemplate> blocsToRemove = new ArrayList<>();
      if(blocs != null){
         oldBlocs = blocImpressionTemplateManager.findByTemplateManager(temp);
         for(int i = 0; i < oldBlocs.size(); i++){
            if(!blocs.contains(oldBlocs.get(i))){
               blocsToRemove.add(oldBlocs.get(i));
            }
         }

         for(int i = 0; i < blocsToRemove.size(); i++){
            blocImpressionTemplateManager.removeObjectManager(blocsToRemove.get(i));
         }

         if(blocsToCreate != null){
            // enregistrements
            for(int i = 0; i < blocsToCreate.size(); i++){
               final BlocImpressionTemplate obj = blocsToCreate.get(i);
               blocImpressionTemplateManager.createObjectManager(obj, template, obj.getBlocImpression());
            }

            // update
            for(int i = 0; i < blocs.size(); i++){
               if(!blocsToCreate.contains(blocs.get(i))){
                  blocImpressionTemplateManager.updateObjectManager(blocs.get(i), template, blocs.get(i).getBlocImpression());
               }
            }
         }else{
            // update
            for(int i = 0; i < blocs.size(); i++){
               blocImpressionTemplateManager.updateObjectManager(blocs.get(i), template, blocs.get(i).getBlocImpression());
            }
         }
      }

      // gestion des ChampImprimes
      List<ChampImprime> oldChamps = new ArrayList<>();
      final List<ChampImprime> champsToRemove = new ArrayList<>();
      if(champsImprime != null){
         oldChamps = champImprimeManager.findByTemplateManager(temp);
         for(int i = 0; i < oldChamps.size(); i++){
            if(!champsImprime.contains(oldChamps.get(i))){
               champsToRemove.add(oldChamps.get(i));
            }
         }

         for(int i = 0; i < champsToRemove.size(); i++){
            champImprimeManager.removeObjectManager(champsToRemove.get(i));
         }

         if(champsToCreate != null){
            // enregistrements
            for(int i = 0; i < champsToCreate.size(); i++){
               final ChampImprime obj = champsToCreate.get(i);
               champImprimeManager.createObjectManager(obj, template, obj.getChampEntite(), obj.getBlocImpression());
            }

            // update
            for(int i = 0; i < champsImprime.size(); i++){
               if(!champsToCreate.contains(champsImprime.get(i))){
                  champImprimeManager.updateObjectManager(champsImprime.get(i), template, champsImprime.get(i).getChampEntite(),
                     champsImprime.get(i).getBlocImpression());
               }
            }
         }else{
            // update
            for(int i = 0; i < champsImprime.size(); i++){
               champImprimeManager.updateObjectManager(champsImprime.get(i), template, champsImprime.get(i).getChampEntite(),
                  champsImprime.get(i).getBlocImpression());
            }
         }

      }

      // gestion des TableAnnotationTemplate
      List<TableAnnotationTemplate> oldTables = new ArrayList<>();
      final List<TableAnnotationTemplate> tablesToRemove = new ArrayList<>();
      if(annotations != null){
         oldTables = tableAnnotationTemplateManager.findByTemplateManager(temp);
         for(int i = 0; i < oldTables.size(); i++){
            if(!annotations.contains(oldTables.get(i))){
               tablesToRemove.add(oldTables.get(i));
            }
         }

         for(int i = 0; i < tablesToRemove.size(); i++){
            tableAnnotationTemplateManager.removeObjectManager(tablesToRemove.get(i));
         }

         if(annoToCreate != null){
            // enregistrements
            for(int i = 0; i < annoToCreate.size(); i++){
               final TableAnnotationTemplate obj = annoToCreate.get(i);
               tableAnnotationTemplateManager.createObjectManager(obj, template, obj.getTableAnnotation());
            }
            // update
            for(int i = 0; i < annotations.size(); i++){
               if(!annoToCreate.contains(annotations.get(i))){
                  tableAnnotationTemplateManager.updateObjectManager(annotations.get(i), template,
                     annotations.get(i).getTableAnnotation());
               }
            }
         }else{
            // update
            for(int i = 0; i < annotations.size(); i++){
               tableAnnotationTemplateManager.updateObjectManager(annotations.get(i), template,
                  annotations.get(i).getTableAnnotation());
            }
         }
      }

   }

   private void updateClesImpression(final List<CleImpression> cles){
      //Gestion des CleImpression
      if(cles != null){
         // enregistrements
         for(final CleImpression cle : cles){
            if(null != cle.getCleId()){
               cleImpressionManager.updateObjectManager(cle);
            }else{
               cleImpressionManager.createObjectManager(cle);
            }
         }
      }
   }

   public void updateAssociations(final Template template, final List<BlocImpressionTemplate> blocs,
      final List<BlocImpressionTemplate> blocsToCreate, final List<ChampImprime> champsImprime,
      final List<ChampImprime> champsToCreate, final List<TableAnnotationTemplate> annotations,
      final List<TableAnnotationTemplate> annoToCreate, final List<CleImpression> cles){
      //    final Template temp = templateDao.mergeObject(template);

      updateClesImpression(cles);
      //       template.setCleImpressionList(cles);

      updateAssociations(template, blocs, blocsToCreate, champsImprime, champsToCreate, annotations, annoToCreate);

   }

}