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
package fr.aphp.tumorotek.manager.impl.io;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.contexte.EChampSupprimePourSerologie;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.contexte.gatsbi.ContexteType;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Fichier;

/**
 *
 * Implémentation du manager du bean de domaine ChampEntite.
 * Classe créée le 29/01/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class ChampEntiteManagerImpl implements ChampEntiteManager
{

   private final Logger log = LoggerFactory.getLogger(AffichageManager.class);

   /** Bean Dao AffichageDao. */
   private ChampEntiteDao champEntiteDao = null;

   public ChampEntiteManagerImpl(){
      super();
   }

   /**
    * Setter du bean ChampEntiteDao.
    * @param ceDao est le bean Dao.
    */
   public void setChampEntiteDao(final ChampEntiteDao ceDao){
      this.champEntiteDao = ceDao;
   }

   /**
    * Recherche un ChampEntite dont l'identifiant est passé en paramètre.
    * @param champEntiteId Identifiant du champEntite que l'on recherche.
    * @return un ChampEntite.
    */
   @Override
   public ChampEntite findByIdManager(final Integer id){
      //On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la recherche par l'identifiant d'un objet ChampEntite");
         throw new RequiredObjectIsNullException("ChampEntite", "recherche par identifiant", "identifiant");
      }
      return champEntiteDao.findById(id);
   }

   /**
    * Recherche tous les Affichages présents dans la BDD.
    * @return Liste d'Affichages.
    */
   @Override
   public List<ChampEntite> findAllObjectsManager(){
      log.debug("Recherche de tous les ChampEntites");
      return champEntiteDao.findAll();
   }

   /**
    * Recherche les champs dont l'entité est passée en paramètre.
    * Les champs retournés sont triés par leur ordre.
    * @param entite Entité à laquelle les champs appartiennent.
    * @return Liste d'Entité.
    */
   @Override
   public List<ChampEntite> findByEntiteManager(final Entite entite){
      log.debug("Recherche des ChampEntites par entité");
      return champEntiteDao.findByEntite(entite);
   }

   @Override
   public List<ChampEntite> findByEntiteAndNomManager(final Entite entite, final String nom){
      log.debug("Recherche des ChampEntites par entité et nom");
      if(entite != null && nom != null){
         return champEntiteDao.findByEntiteAndNom(entite, nom);
      }
      return new ArrayList<>();
   }

   //@TODO : CHT - Ne semble pas / plus appelé... A SUPPRIMER avec la méthode du Dao et la requête...
   @Override
   public List<ChampEntite> findByEntiteAndImportManagerAndDatatype(final Entite entite, final Boolean canImport,
      final List<DataType> dataTypeList){
      if(entite != null && canImport != null && null != dataTypeList){
         return champEntiteDao.findByEntiteAndImportAndDataType(entite, canImport, dataTypeList);
      }
      return new ArrayList<>();
   }

   //@TODO : CHT - Ne semble pas / plus appelé... A SUPPRIMER avec la méthode du Dao et la requête...
   @Override
   public List<ChampEntite> findByEntiteAndImportManagerAndDatatype(final Entite entite, final Boolean canImport,
      final List<DataType> dataTypeList, final Boolean excludeIds){
      List<ChampEntite> champEntiteList = new ArrayList<>();
      if(!excludeIds){
         champEntiteList = findByEntiteAndImportManagerAndDatatype(entite, canImport, dataTypeList);
      }else if(entite != null && canImport != null && null != dataTypeList){
         final List<ChampEntite> champEntiteList2 =
            champEntiteDao.findByEntiteAndImportAndDataType(entite, canImport, dataTypeList);
         for(final ChampEntite champEntite : champEntiteList2){
            if(!"Id".equals(champEntite.getNom().substring(champEntite.getNom().length() - 2))){
               champEntiteList.add(champEntite);
            }
         }
      }
      return champEntiteList;
   }
   
   @Override
   public List<ChampEntite> findByEntiteImportAndIsNullableManager(final Entite entite, final Boolean canImport,
      final Boolean isNullable, EContexte banqueContexte, Contexte gatsbiContexte){

      List<ChampEntite> listChampEntite = new ArrayList<ChampEntite>();

      if(gatsbiContexte == null) {//TK standard :
         //Récupération des données de la base en fonction de isNullable
         if(isNullable == null){
            listChampEntite.addAll(champEntiteDao.findByEntiteAndImport(entite, canImport));
         }
         else {
            listChampEntite.addAll(champEntiteDao.findByEntiteImportObligatoire(entite, canImport, isNullable));            
         }
         
         //suppression de patient identifiant puisque contexte non Gatsbi
         listChampEntite.removeIf(c -> c.getId().equals(272));
         
         //contexte sérologie :
         if(banqueContexte != null && banqueContexte == EContexte.SEROLOGIE) {
            // - suppression des champs non appropriés :
            if(entite.getEntiteId() == 3) {
               EChampSupprimePourSerologie[] tabEChampSupprimePourSerologie = EChampSupprimePourSerologie.values();
               int nbChampsASupprimer = tabEChampSupprimePourSerologie.length;
               for(int i=0; i<nbChampsASupprimer; i++) {
                  listChampEntite.remove(new ChampEntite(entite, tabEChampSupprimePourSerologie[i].getNom(), null));
               }
            }
            //NB : les champs spécifiques au contexte sérologie sont gérés comme des champs délégués et ne sont donc pas remontés
            // par les requêtes ci-dessus faites uniquement sur la table CHAMP_ENTITE
         }
      }
      else {//cas Gatsbi
         listChampEntite.addAll(champEntiteDao.findByEntiteAndImport(entite, canImport));

         // filtre les champs visibles suivant le contexte Gatsbi
         List<Integer> hiddenIds = retrieveHiddenChampEntiteIdsForGatsbiContexte(gatsbiContexte);
         listChampEntite.removeIf(chp -> hiddenIds.contains(chp.getId()));

         filterNullableOrNotChampEntite(isNullable, gatsbiContexte, listChampEntite);
      }

      Collections.sort(listChampEntite, Comparator.comparing(ChampEntite::getId));

      // ajout identifiant au début si il est présent
      if (gatsbiContexte != null && gatsbiContexte.getContexteType().equals(ContexteType.PATIENT)) {
         Optional<ChampEntite> patientIdentifiantChpOpt = 
            listChampEntite.stream().filter(c -> c.getId().equals(272)).findFirst();
         if (patientIdentifiantChpOpt.isPresent()) {
            listChampEntite.remove(patientIdentifiantChpOpt.get());
            listChampEntite.add(0, patientIdentifiantChpOpt.get());
         }
      }

      return listChampEntite;
   }

   @Override
   public void filterNullableOrNotChampEntite(final Boolean isNullable, Contexte gatsbiContexte,
      List<ChampEntite> listChampEntite){
      if(isNullable != null){
         // filtre les champs obligatoires suivant le contexte Gatsbi
         // surcharge la propriété isNullable de manière non persistante
         List<Integer> reqIds = retrieveRequiredChampEntiteIdsForGatsbiContexte(gatsbiContexte);
         if(isNullable){ // champs non obligatoires
            listChampEntite.removeIf(chp -> reqIds.contains(chp.getId()));
            listChampEntite.stream().forEach(chp -> chp.setNullable(true));
         }else{ // obligatoires
            listChampEntite.removeIf(chp -> !reqIds.contains(chp.getId()));
            listChampEntite.stream().forEach(chp -> chp.setNullable(false));
         }
      }
   }
   
   @Override
   public Object getValueForObjectManager(final ChampEntite champ, final Object obj, final boolean prettyFormat){
      Object res = null;
      String value = null;
      // on formate le nom du champ
      String nomChamp = champ.getNom().replaceFirst(".", (champ.getNom().charAt(0) + "").toLowerCase());
      if(nomChamp.endsWith("Id")){
         nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
      }

      // on vérifie que l'objet a bien ce champ
      if(PropertyUtils.isReadable(obj, nomChamp)){
         try{
            // extraction de la valeur
            res = PropertyUtils.getProperty(obj, nomChamp);
         }catch(final IllegalAccessException e){
            log.error(e.getMessage(), e); 
         }catch(final InvocationTargetException e){
            log.error(e.getMessage(), e); 
         }catch(final NoSuchMethodException e){
            log.error(e.getMessage(), e); 
         }
         // si la valeur retournée n'est pas null
         if(res != null){
            // si le champ à extraire n'est pas un thésaurus
            if(champ.getQueryChamp() == null){
               // on va formater la valeur de retour pour
               // obtenir un String
               String type = null;
               try{
                  type = PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType().getSimpleName();
               }catch(final IllegalAccessException e){
                  log.error(e.getMessage(), e); 
               }catch(final InvocationTargetException e){
                  log.error(e.getMessage(), e); 
               }catch(final NoSuchMethodException e){
                  log.error(e.getMessage(), e); 
               }
               if(type != null && prettyFormat){
                  // set d'un string
                  if(type.equals("String")){
                     value = res.toString();
                  }else if(type.equals("Integer")){
                     // si l'attibut est un integer, on caste
                     // la valeur issue de l'objet
                     final Integer tmp = (Integer) res;
                     value = String.valueOf(tmp);
                  }else if(type.equals("Float")){
                     // si l'attibut est un float, on caste
                     // la valeur issue de l'objet
                     final Float tmp = (Float) res;
                     value = String.valueOf(tmp);
                  }else if(type.equals("Boolean")){
                     // si l'attibut est un boolean, on caste
                     // la valeur issue de l'objet
                     final Boolean tmp = (Boolean) res;
                     if(tmp){
                        value = "Oui";
                     }else{
                        value = "Non";
                     }
                  }else if(type.equals("Date")){
                     // si l'attibut est une date, on caste
                     // la valeur issue de l'objet
                     final Date date = (Date) res;
                     final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                     value = sdf.format(date);
                  }else if(type.equals("Calendar")){
                     // si l'attibut est un calendar, on caste
                     // la valeur issue de l'objet
                     final Calendar tmp = (Calendar) res;
                     SimpleDateFormat sdf = null;
                     if(tmp.get(Calendar.HOUR_OF_DAY) > 0 || tmp.get(Calendar.MINUTE) > 0 || tmp.get(Calendar.SECOND) > 0){
                        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                     }else{
                        sdf = new SimpleDateFormat("dd/MM/yyyy");
                     }
                     value = sdf.format(tmp.getTime());
                  }else if(type.equals("Fichier")){
                     value = ((Fichier) res).getNom();
                  }
                  return value;
               }
            }else{
               // sinon, la variable res contient l'objet du
               // thésaurus associé à l'objet. On va alors
               // extraire la valeur sous forme de string

               // on formate le nom du champ de thesaurus
               String nomChampThes =
                  champ.getQueryChamp().getNom().replaceFirst(".", (champ.getQueryChamp().getNom().charAt(0) + "").toLowerCase());
               if(nomChampThes.endsWith("Id")){
                  nomChampThes = nomChampThes.substring(0, nomChampThes.length() - 2);
               }

               // on vérifie que l'objet a bien ce champ
               if(PropertyUtils.isReadable(res, nomChampThes)){
                  Object resThes = null;
                  try{
                     // extraction de la valeur
                     resThes = PropertyUtils.getProperty(res, nomChampThes);
                  }catch(final IllegalAccessException e){
                     log.error(e.getMessage(), e); 
                  }catch(final InvocationTargetException e){
                     log.error(e.getMessage(), e); 
                  }catch(final NoSuchMethodException e){
                     log.error(e.getMessage(), e); 
                  }
                  return (null != resThes) ? resThes.toString() : null;
               }
            }
         }
      }
      return res;
   }

   /**
    * Récupére tous les ids des champs à ne pas afficher pour un contexte Gatsbi.
    * Surcharge l'appel de contexte.getHiddenChampEntiteIds afin d'ajouter 
    * des dépendances spécifiques par entité entre certains champs.
    * @param contexte 
    * @return liste ids
    */
   @Override
   public List<Integer> retrieveHiddenChampEntiteIdsForGatsbiContexte(Contexte gatsbiContexte){
      List<Integer> hiddenIds = new ArrayList<Integer>();
      if(gatsbiContexte != null){
         hiddenIds.addAll(gatsbiContexte.getHiddenChampEntiteIds());
      }
      
      // correctif spécifique champs associés
      addAssociatedChampEntite(gatsbiContexte, hiddenIds);

      return hiddenIds;
   }  
   
   /**
    * Récupére tous les ids des champs obligatoires.
    * @param contexte 
    * @return liste ids
    */
   @Override
   public List<Integer> retrieveRequiredChampEntiteIdsForGatsbiContexte(Contexte gatsbiContexte){
      List<Integer> requiredIds = new ArrayList<Integer>();
      if(gatsbiContexte != null){
         requiredIds.addAll(gatsbiContexte.getRequiredChampEntiteIds());
         // correctif spécifique champs associés
         addAssociatedChampEntite(gatsbiContexte, requiredIds);
      }

      return requiredIds;
   }

   /**
    * Ajoute les champs entites associés au comportement hidden/required 
    * si nécessaire afin d'éviter toute incohérence
    * @param c contexte
    * @param hiddenIds
    */
   private void addAssociatedChampEntite(Contexte c, List<Integer> ids){
      // prelevement: nonconfs
      if (c.getContexteType().equals(ContexteType.PRELEVEMENT)) {
         // ncfs arrive
         if (ids.contains(256)) {
            if (!ids.contains(257)) ids.add(257); // raisons   
         } else {
            ids.remove(Integer.valueOf(257));
         }
      }
      
      // echantillon: quantite et nonconfs
      if (c.getContexteType().equals(ContexteType.ECHANTILLON)) {
         // quantite
         if (ids.contains(61)) {
            if (!ids.contains(62)) ids.add(62); // quantite_init    
            if (!ids.contains(63)) ids.add(63); // quantite_unite    
         } else {
            ids.remove(Integer.valueOf(62));
            ids.remove(Integer.valueOf(63));
         }
         
         // ncfs traitement
         if (ids.contains(243)) {
            if (!ids.contains(261)) ids.add(261); // raisons   
         } else {
            ids.remove(Integer.valueOf(261));
         }
         
         // ncfs cession
         if (ids.contains(244)) {
            if (!ids.contains(262)) ids.add(262); // raisons   
         } else {
            ids.remove(Integer.valueOf(262));
         }
      }
   }


}