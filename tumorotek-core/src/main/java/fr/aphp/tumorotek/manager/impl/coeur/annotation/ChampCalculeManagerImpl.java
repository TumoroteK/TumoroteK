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
package fr.aphp.tumorotek.manager.impl.coeur.annotation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import fr.aphp.tumorotek.dao.annotation.ChampCalculeDao;
import fr.aphp.tumorotek.manager.coeur.annotation.ChampCalculeManager;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.ChampCalcule;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.utils.Duree;
import fr.aphp.tumorotek.utils.ConversionUtils;

/**
 *
 * Implémentation du manager du bean de domaine ChampCalcule.
 * Classe créée le 06/03/2018.
 * 
 * @author Answald Bournique
 * @version 2.2.0
 * @since 2.2.0
 *
 */
public class ChampCalculeManagerImpl implements ChampCalculeManager, ApplicationContextAware, InitializingBean
{

   //ODO Revoir les méthodes et typages.

   private final Log log = LogFactory.getLog(GroupementManager.class);

   private ApplicationContext context;
   
   /** Bean Dao ChampCalculeDao. */
   private ChampCalculeDao champCalculeDao;

   /**
    * Bean Manager champManager
    */
   private ChampManager champManager;

   public ChampCalculeManagerImpl(){
      super();
   }

   /**
    * Setter du bean ChampCalculeDao. 
    * @param cDao est le bean Dao.
    */
   public void setChampCalculeDao(final ChampCalculeDao cDao){
      this.champCalculeDao = cDao;
   }

   /**
    * Setter du bean ChampManager. 
    * @param champManager est le bean champManager.
    */
   public void setChampManager(ChampManager champManager){
      this.champManager = champManager;
   }

   @Override
   public void createObjectManager(final ChampCalcule champCalcule){
      // On vérifie que le groupement n'est pas nul
      if(champCalcule == null){
         log.warn("Objet obligatoire ChampCalcule manquant lors " + "de la création d'un objet ChampCalcule");
         throw new RequiredObjectIsNullException("ChampCalcule", "création", "ChampCalcule");
      }

      // On enregsitre d'abord les champs associés
      createChampsAssocies(champCalcule);

      champCalculeDao.createObject(champCalcule);
   }

   /**
    * Creer les champs associés au Champ Calculé
    * @param champCalcule
    */
   private void createChampsAssocies(ChampCalcule champCalcule){
      if(null != champCalcule.getChamp1()){
         champManager.createObjectManager(champCalcule.getChamp1());
      }
      if(null != champCalcule.getChamp2()){
         champManager.createObjectManager(champCalcule.getChamp2());
      }
   }

   @Override
   public void updateObjectManager(final ChampCalcule champCalcule){
      //On vérifie que le groupement n'est pas nul
      if(champCalcule == null){
         log.warn("Objet obligatoire ChampCalcule manquant lors " + "de la modification d'un objet ChampCalcule");
         throw new RequiredObjectIsNullException("ChampCalcule", "modification", "ChampCalcule");
      }

      //On met à jour les champs associés
      updateChampsAssocies(champCalcule);

      champCalculeDao.updateObject(champCalcule);
   }

   /**
    * Met à jour les champs liés au ChampCalcule
    * @param champCalcule le champCalcule
    */
   private void updateChampsAssocies(ChampCalcule champCalcule){
      final Champ oldChamp1 = champCalculeDao.findById(champCalcule.getChampCalculeId()).getChamp1();
      updateChampAssocie(champCalcule.getChamp1(), oldChamp1);

      final Champ oldChamp2 = champCalculeDao.findById(champCalcule.getChampCalculeId()).getChamp2();
      updateChampAssocie(champCalcule.getChamp2(), oldChamp2);
   }

   /**
    * Met à jour un champ associé au champCalcule
    * @param newChamp l'ancien champ
    * @param oldChamp le nouveau champ
    */
   private void updateChampAssocie(Champ newChamp, Champ oldChamp){
      if(null == newChamp && null != oldChamp){ // Suppression du champ
         champManager.removeObjectManager(oldChamp);
      }else if(null != newChamp && null == oldChamp){ // Nouveau champ
         champManager.createObjectManager(newChamp);
      }else if(null != newChamp && null != oldChamp){ // Mise à jour Champ
         newChamp.setChampId(oldChamp.getChampId());
         champManager.updateObjectManager(newChamp);
      }
   }

   @Override
   public void removeObjectManager(final ChampCalcule champCalcule){
      // On vérifie que le champ n'est pas nul
      if(champCalcule == null){
         log.warn("Objet obligatoire ChampCalcule manquant lors " + "de la suppression d'un objet ChampCalcule");
         throw new RequiredObjectIsNullException("ChampCalcule", "suppression", "ChampCalcule");
      }
      // On vérifie que le champ est en BDD
      if(findByIdManager(champCalcule.getChampCalculeId()) == null){
         throw new SearchedObjectIdNotExistException("ChampCalcule", champCalcule.getChampCalculeId());
      }
      // On supprime d'abord les champs associés
      removeChampsAssocies(champCalcule);

      // On supprime le champCalcule
      champCalculeDao.removeObject(champCalcule.getChampCalculeId());
   }

   /**
    * Supprimes les champs associés au champCalcule
    * @param champCalcule
    */
   private void removeChampsAssocies(ChampCalcule champCalcule){
      if(null != champCalcule.getChamp1()){
         champManager.removeObjectManager(champCalcule.getChamp1());
         //         champCalcule.setChamp1(champDao.mergeObject(champCalcule.getChamp1()));
      }

      if(null != champCalcule.getChamp2()){
         champManager.removeObjectManager(champCalcule.getChamp2());
         //         champCalcule.setChamp2(champDao.mergeObject(champCalcule.getChamp2()));
      }
   }

   @Override
   public ChampCalcule findByIdManager(final Integer id){
      // On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet ChampCalcule");
         throw new RequiredObjectIsNullException("ChampCalcule", "recherche par identifiant", "identifiant");
      }
      return champCalculeDao.findById(id);
   }

   @Override
   public ChampCalcule findByChampAnnotationManager(final ChampAnnotation champAnnotation){
      // On vérifie que l'identifiant n'est pas nul
      if(champAnnotation.getId() == null){
         log.warn(
            "Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet ChampAnnotation");
         throw new RequiredObjectIsNullException("ChampCalcule", "recherche par champAnnotation", "champAnnotation");
      }
      ChampCalcule champCalcule = null;
      List<ChampCalcule> champCalcules = champCalculeDao.findByChampAnnotation(champAnnotation);
      if(null != champCalcules){
         if(champCalcules.size() == 1){
            champCalcule = champCalcules.get(0);
         }else if(champCalcules.size() > 1){
            throw new TKException("Le champAnnotation " + champAnnotation + " est lié à plus d'un ChampCalcule");
         }
      }

      return champCalcule;
   }

   @Override
   public List<ChampCalcule> findAllObjectsManager(){
      return champCalculeDao.findAll();
   }

   @Override
   public Object getValueForObjectManager(final ChampCalcule champCalcule, final Object obj){
      Object val1 = null;
      Object val2 = null;
      Object res = null;

      // Récupère la valeur du champ1
      if(null != champCalcule.getChamp1()){
         List<Object> objetsAssocies = RechercheUtilsManager.getListeObjetsCorrespondants(obj, champCalcule.getChamp1(), null);
         val1 = RechercheUtilsManager.getChampValueFromObjectList(champCalcule.getChamp1(), objetsAssocies);
      }
      // Récupère la valeur du champ2 ou de la valeur saisie
      if(null != champCalcule.getChamp2()){
         List<Object> objetsAssocies = RechercheUtilsManager.getListeObjetsCorrespondants(obj, champCalcule.getChamp2(), null);
         val2 = RechercheUtilsManager.getChampValueFromObjectList(champCalcule.getChamp2(), objetsAssocies);
      }else if(null != champCalcule.getValeur()){
         val2 = champCalcule.getValeur();
      }

      String operateur = champCalcule.getOperateur();

      if(null != val1 && !"".equals(val1) && null != val2 && !"".equals(val2) && null != operateur && !"".equals(operateur)){
         DataType champ1DataType = champCalcule.getChamp1().dataType();
         if("calcule".equals(champ1DataType.getType())){
            champ1DataType = champCalcule.getChamp1().getChampAnnotation().getChampCalcule().getDataType();
         }
         res = calculate(val1, val2, operateur, champ1DataType);
      }

      return res;
   }

   /**
    * Fait le calcul entre les deux valeurs selon le datatype qui doit être retourné
    * @param val1 valeur1
    * @param val2 valeur2
    * @param operateur opérateur
    * @param champ1DataType le datatype du champ1
    * @return le résultat
    */
   private Object calculate(Object val1, Object val2, String operateur, DataType champ1DataType){
      Object res = null;
      switch(operateur){
         case "+":
            switch(champ1DataType.getType()){
               case "num":
                  res = addNum(val1, val2);
                  break;
               case "date":
               case "datetime":
                  res = addDate(val1, val2);
                  break;
               case "duree":
                  res = addDuree(val1, val2);
                  break;
               default:
                  break;
            }
            ;
            break;
         case "-":
            switch(champ1DataType.getType()){
               case "num":
                  res = substractNum(val1, val2);
                  break;
               case "date":
               case "datetime":
                  res = substractDate(val1, val2);
                  break;
               case "duree":
                  res = substractDuree(val1, val2);
                  break;
               default:
                  break;
            }
            ;
            break;
         default:
            break;
      }

      return res;
   }

   /**
    * Addition pour type Date 
    * @param val1 Objet Date ou Calendar
    * @param val2 Objet Date ou Calendar
    * @return Calendar
    */
   private Calendar addDate(Object val1, Object val2){
      Calendar date1 = null;
      Calendar result = null;

      if(Calendar.class.isInstance(val1)){
         date1 = Calendar.class.cast(val1);
      }else if(Date.class.isInstance(val1)){
         Calendar cal = Calendar.getInstance();
         cal.setTime(Date.class.cast(val1));
         date1 = cal;
      }else if(String.class.isInstance(val1)){
         date1 = ConversionUtils.convertToCalendar(val1);
      }

      if(null != date1 && null != val2){
         Long secondes = new Long(val2.toString());
         Duree duree = new Duree(secondes, Duree.SECONDE);
         Long annees = duree.getTemps(Duree.ANNEE);
         duree.addTemps(-annees, Duree.ANNEE);
         Long mois = duree.getTemps(Duree.MOIS);
         duree.addTemps(-mois, Duree.MOIS);
         Long jours = duree.getTemps(Duree.JOUR);
         duree.addTemps(-jours, Duree.JOUR);
         Long heures = duree.getTemps(Duree.HEURE);
         duree.addTemps(-heures, Duree.HEURE);
         Long minutes = duree.getTemps(Duree.MINUTE);
         result = date1;
         result.add(Calendar.MINUTE, new Integer(minutes.toString()));
         result.add(Calendar.HOUR_OF_DAY, new Integer(heures.toString()));
         result.add(Calendar.DAY_OF_MONTH, new Integer(jours.toString()));
         result.add(Calendar.MONTH, new Integer(mois.toString()));
         result.add(Calendar.YEAR, new Integer(annees.toString()));
      }
      return result;
   }

   private Float addNum(Object val1, Object val2){
      return new Float(val1.toString()) + new Float(val2.toString());
   }

   private Float substractNum(Object val1, Object val2){
      return new Float(val1.toString()) - new Float(val2.toString());
   }

   /**
    * Soustraction pour type date
    * @param val1 Objet date ou Calendar
    * @param val2 Objet Date, Calendar ou Number
    * @return Calendar ou Durée
    */
   private Object substractDate(Object val1, Object val2){
      Calendar date1 = null;
      Calendar date2 = null;
      Integer secondes = null;
      Object result = null;
      if(Calendar.class.isInstance(val1)){
         date1 = Calendar.class.cast(val1);
      }else if(Date.class.isInstance(val1)){
         Calendar cal = Calendar.getInstance();
         cal.setTime(Date.class.cast(val1));
         date1 = cal;
      }else if(String.class.isInstance(val1)){
         date1 = ConversionUtils.convertToCalendar(val1);
      }

      if(Calendar.class.isInstance(val2)){
         date2 = Calendar.class.cast(val2);
      }else if(Date.class.isInstance(val2)){
         Calendar cal = Calendar.getInstance();
         cal.setTime(Date.class.cast(val2));
         date2 = cal;
      }else if(Number.class.isInstance(val2)){
         secondes = new Integer(val2.toString());
      }else if(String.class.isInstance(val2)){
         try{
            date2 = ConversionUtils.convertToCalendar(val2);
         }catch(Exception e){
            try{
               secondes = new Integer(val2.toString());
            }catch(Exception e2){
               log.error(e);
               log.error(e2);
               throw new TKException("Mauvais format de retour pour la deuxème opérande");
            }
         }
      }

      if(null != date1){
         if(null != date2){ //Soustraction de deux dates donne une durée
            Duree duree = new Duree(0L, Duree.SECONDE);
            duree.addTemps(new Long(date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR)), Duree.ANNEE);
            duree.addTemps(new Long(date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH)), Duree.MOIS);
            duree.addTemps(new Long(date1.get(Calendar.HOUR_OF_DAY) - date2.get(Calendar.HOUR_OF_DAY)), Duree.HEURE);
            duree.addTemps(new Long(date1.get(Calendar.MINUTE) - date2.get(Calendar.MINUTE)), Duree.MINUTE);
            duree.addTemps(new Long(date1.get(Calendar.DAY_OF_MONTH) - date2.get(Calendar.DAY_OF_MONTH)), Duree.JOUR);
            result = duree.getTemps(Duree.SECONDE);
         }else if(null != secondes){ // Soustraction d'une date à une durée donne une date
            Duree duree = new Duree(new Long(secondes), Duree.SECONDE);
            Long annees = duree.getTemps(Duree.ANNEE);
            duree.addTemps(-annees, Duree.ANNEE);
            Long mois = duree.getTemps(Duree.MOIS);
            duree.addTemps(-mois, Duree.MOIS);
            Long jours = duree.getTemps(Duree.JOUR);
            duree.addTemps(-jours, Duree.JOUR);
            Long heures = duree.getTemps(Duree.HEURE);
            duree.addTemps(-heures, Duree.HEURE);
            Long minutes = duree.getTemps(Duree.MINUTE);
            date1.add(Calendar.MINUTE, -new Integer(minutes.toString()));
            date1.add(Calendar.HOUR_OF_DAY, -new Integer(heures.toString()));
            date1.add(Calendar.DAY_OF_MONTH, -new Integer(jours.toString()));
            date1.add(Calendar.MONTH, -new Integer(mois.toString()));
            date1.add(Calendar.YEAR, -new Integer(annees.toString()));

            result = date1;
         }
      }

      return result;
   }

   /**
    * Addition de deux Duree
    * @param val1 
    * @param val2
    * @return duree
    */
   private Long addDuree(Object val1, Object val2){
      try{
         Long duree1 = new Long(val1.toString());
         Long duree2 = new Long(val2.toString());
         return duree1 + duree2;
      }catch(Exception e){
         log.error(e);
      }
      return null;
   }

   /**
    * Soustraction de deux Duree
    * @param val1
    * @param val2
    * @return duree
    */
   private Long substractDuree(Object val1, Object val2){
      try{
         Long duree1 = new Long(val1.toString());
         Long duree2 = new Long(val2.toString());
         return duree1 - duree2;
      }catch(Exception e){
         log.error(e);
      }
      return null;
   }

   @Override
   //le bean champManager est injecté après l'initialisaton du bean pour éviter les problèmes
   //de dépendance cyclique
   public void afterPropertiesSet() throws Exception{
      champManager = context.getBean(ChampManager.class);
   }

   @Override
   public void setApplicationContext(ApplicationContext context) throws BeansException{
      this.context = context;
   }

}
