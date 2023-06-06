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
import fr.aphp.tumorotek.dao.impression.ChampImprimeDao;
import fr.aphp.tumorotek.dao.impression.TemplateDao;
import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.impression.ChampImprimeManager;
import fr.aphp.tumorotek.model.impression.BlocImpression;
import fr.aphp.tumorotek.model.impression.ChampImprime;
import fr.aphp.tumorotek.model.impression.ChampImprimePK;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.ChampEntite;

/**
 *
 * Implémentation du manager ChampImprimeManager.
 * Classe créée le 26/07/2010.
 *
 * @author Pierre Ventadour.
 * @version 2.0
 *
 */
public class ChampImprimeManagerImpl implements ChampImprimeManager
{

   private final Logger log = LoggerFactory.getLogger(ChampImprimeManager.class);

   /** Bean Dao. */
   private ChampImprimeDao champImprimeDao;

   /** Bean Dao. */
   private TemplateDao templateDao;

   /** Bean Dao. */
   private ChampEntiteDao champEntiteDao;

   /** Bean Dao. */
   private BlocImpressionDao blocImpressionDao;

   public void setChampImprimeDao(final ChampImprimeDao cDao){
      this.champImprimeDao = cDao;
   }

   public void setTemplateDao(final TemplateDao tDao){
      this.templateDao = tDao;
   }

   public void setChampEntiteDao(final ChampEntiteDao cDao){
      this.champEntiteDao = cDao;
   }

   public void setBlocImpressionDao(final BlocImpressionDao bDao){
      this.blocImpressionDao = bDao;
   }

   @Override
   public ChampImprime findByIdManager(final ChampImprimePK pk){
      return champImprimeDao.findById(pk);
   }

   @Override
   public List<ChampImprime> findAllObjectsManager(){
      log.debug("Recherche de tous les ChampImprimes");
      return champImprimeDao.findAll();
   }

   @Override
   public List<ChampImprime> findByExcludedPKManager(final ChampImprimePK pk){
      if(pk != null){
         return champImprimeDao.findByExcludedPK(pk);
      }
      return champImprimeDao.findAll();
   }

   @Override
   public List<ChampImprime> findByTemplateManager(final Template template){
      log.debug("Recherche de tous les ChampImprimes d'un " + "Template.");
      if(template != null){
         return champImprimeDao.findByTemplate(template);
      }
      return new ArrayList<>();
   }

   @Override
   public List<ChampImprime> findByTemplateAndBlocManager(final Template template, final BlocImpression bloc){
      log.debug("Recherche de tous les ChampImprimes d'un " + "Template.");
      if(template != null && bloc != null){
         return champImprimeDao.findByTemplateAndBloc(template, bloc);
      }
      return new ArrayList<>();
   }

   @Override
   public Boolean findDoublonManager(final Template template, final ChampEntite champEntite, final BlocImpression bloc){
      final ChampImprimePK pk = new ChampImprimePK(template, champEntite, bloc);

      return (champImprimeDao.findById(pk) != null);
   }

   @Override
   public void validateObjectManager(final Template template, final ChampEntite champEntite, final BlocImpression bloc){
      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "creation", "Template");
      }

      //champEntite required
      if(champEntite == null){
         log.warn("Objet obligatoire ChampEntite manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "creation", "ChampEntite");
      }

      //bloc required
      if(bloc == null){
         log.warn("Objet obligatoire BlocImpression manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "creation", "BlocImpression");
      }

      //Doublon
      if(template.getTemplateId() != null){
         if(findDoublonManager(template, champEntite, bloc)){

            log.warn("Doublon lors validation objet " + "ChampImprime");
            throw new DoublonFoundException("ChampImprime", "creation");
         }
      }
   }

   @Override
   public void createObjectManager(final ChampImprime champImprime, final Template template, final ChampEntite champEntite,
      final BlocImpression bloc){
      // validation de l'objet à créer
      validateObjectManager(template, champEntite, bloc);

      champImprime.setTemplate(templateDao.mergeObject(template));
      champImprime.setChampEntite(champEntiteDao.mergeObject(champEntite));
      champImprime.setBlocImpression(blocImpressionDao.mergeObject(bloc));

      // création
      champImprimeDao.createObject(champImprime);

      log.info("Enregistrement objet ChampImprime " + champImprime.toString());
   }

   @Override
   public void updateObjectManager(final ChampImprime champImprime, final Template template, final ChampEntite champEntite,
      final BlocImpression bloc){

      //template required
      if(template == null){
         log.warn("Objet obligatoire Template manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "modification", "Template");
      }
      champImprime.setTemplate(templateDao.mergeObject(template));

      //champEntite required
      if(champEntite == null){
         log.warn("Objet obligatoire ChampEntite manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "modification", "ChampEntite");
      }
      champImprime.setChampEntite(champEntiteDao.mergeObject(champEntite));

      //bloc required
      if(bloc == null){
         log.warn("Objet obligatoire BlocImpression manquant" + " lors de la validation d'un ChampImprime");
         throw new RequiredObjectIsNullException("ChampImprime", "modification", "BlocImpression");
      }
      champImprime.setBlocImpression(blocImpressionDao.mergeObject(bloc));

      // création
      champImprimeDao.updateObject(champImprime);

      log.info("Enregistrement objet ChampImprime " + champImprime.toString());

   }

   @Override
   public void removeObjectManager(final ChampImprime champImprime){
      if(champImprime != null){
         champImprimeDao.removeObject(champImprime.getPk());
         log.info("Suppression de l'objet ChampImprime : " + champImprime.toString());
      }else{
         log.warn("Suppression d'un ChampImprime null");
      }
   }
}