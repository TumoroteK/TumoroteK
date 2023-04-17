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
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
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
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet ChampEntite");
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

   @Override
   public List<ChampEntite> findByEntiteAndImportManager(final Entite entite, final Boolean canImport){
      if(entite != null && canImport != null){
         return champEntiteDao.findByEntiteAndImport(entite, canImport);
      }
      return new ArrayList<>();
   }

   @Override
   public List<ChampEntite> findByEntiteAndImportManagerAndDatatype(final Entite entite, final Boolean canImport,
      final List<DataType> dataTypeList){
      if(entite != null && canImport != null && null != dataTypeList){
         return champEntiteDao.findByEntiteAndImportAndDataType(entite, canImport, dataTypeList);
      }
      return new ArrayList<>();
   }

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
      final Boolean isNullable){
      if(entite != null && canImport != null && isNullable != null){
         return champEntiteDao.findByEntiteImportObligatoire(entite, canImport, isNullable);
      }
      return new ArrayList<>();
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
            log.error("An error occurred: {}", e.toString()); 
         }catch(final InvocationTargetException e){
            log.error("An error occurred: {}", e.toString()); 
         }catch(final NoSuchMethodException e){
            log.error("An error occurred: {}", e.toString()); 
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
                  log.error("An error occurred: {}", e.toString()); 
               }catch(final InvocationTargetException e){
                  log.error("An error occurred: {}", e.toString()); 
               }catch(final NoSuchMethodException e){
                  log.error("An error occurred: {}", e.toString()); 
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
                     log.error("An error occurred: {}", e.toString()); 
                  }catch(final InvocationTargetException e){
                     log.error("An error occurred: {}", e.toString()); 
                  }catch(final NoSuchMethodException e){
                     log.error("An error occurred: {}", e.toString()); 
                  }
                  return (null != resThes) ? resThes.toString() : null;
               }
            }
         }
      }
      return res;
   }

}