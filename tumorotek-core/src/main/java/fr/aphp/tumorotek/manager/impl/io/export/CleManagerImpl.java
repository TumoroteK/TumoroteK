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
package fr.aphp.tumorotek.manager.impl.io.export;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.io.export.CleDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.export.CleManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.impression.Template;
import fr.aphp.tumorotek.model.io.export.Champ;

/**
 *
 * Implémentation du manager du bean de domaine Champ. Classe créée le
 * 05/05/10.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class CleManagerImpl implements CleManager
{

   private final Log log = LogFactory.getLog(GroupementManager.class);

   /** Bean Dao CleDeao. */
   private CleDao cleDao = null;

   /** Bean Manager Champ */
   private ChampManager champManager;

   public CleManagerImpl(){
      super();
   }

   /**
    * Setter du bean CleDao.
    * @param cDao
    *            est le bean Dao.
    */
   public void setCleDao(final CleDao cDao){
      this.cleDao = cDao;
   }

   /**
    * Setter du bean ChampManager.
    * @param champManager
    *            est le bean champManager.
    */
   public void setChampManager(final ChampManager champManager){
      this.champManager = champManager;
   }

   /**
    * Créé un Champ en BDD.
    *
    * @param cle
    *            Champ à créer.
    * @param parent
    *            Champ parent du champ à créer.
    */
   @Override
   public void createObjectManager(final CleImpression cle){
      // On vérifie que le groupement n'est pas nul
      if(cle == null){
         log.warn("Objet obligatoire Cle manquant lors " + "de la création d'un objet Cle");
         throw new RequiredObjectIsNullException("Cle", "création", "Cle");
      }
      // On enregsitre d'abord les Champs
      if(null != cle.getChamp()){
         champManager.createObjectManager(cle.getChamp(), cle.getChamp().getChampParent());
      }
      cleDao.createObject(cle);
   }

   /**
    * Met à jour une Clé en BDD.
    * @param cle Clé à mettre à jour
    */
   @Override
   public void updateObjectManager(final CleImpression cle){
      //On vérifie que le groupement n'est pas nul
      if(cle == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la modification d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "modification", "Champ");
      }
      //On met à jour le champs
      final Champ oldChamp = cleDao.findById(cle.getCleId()).getChamp();

      if(null == cle.getChamp() && null != oldChamp){ // Suppression du champ
         champManager.removeObjectManager(oldChamp);
      }else if(null != cle.getChamp() && null == cle.getChamp().getChampId()){ // Nouveau champ
         champManager.createObjectManager(cle.getChamp(), cle.getChamp().getChampParent());
         if(null != oldChamp){
            champManager.removeObjectManager(oldChamp);
         }
      }else if(null != cle.getChamp() && null != oldChamp){ // Mise à jour Champ
         champManager.updateObjectManager(cle.getChamp(), cle.getChamp().getChampParent());
      }

      cleDao.updateObject(cle);
   }

   /**
    * Supprime un Champ et son parent d'abord.
    *
    * @param groupement
    *            Champ à supprimer.
    */
   @Override
   public void removeObjectManager(final CleImpression cle){
      // On vérifie que le champ n'est pas nul
      if(cle == null){
         log.warn("Objet obligatoire Champ manquant lors " + "de la suppression d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "suppression", "Champ");
      }
      // On vérifie que le champ est en BDD
      if(findByIdManager(cle.getCleId()) == null){
         throw new SearchedObjectIdNotExistException("Champ", cle.getCleId());
      }
      // On supprime d'abord les champs
      if(null != cle.getChamp()){
         champManager.removeObjectManager(cle.getChamp());
      }
      // On supprime la clé
      cleDao.removeObject(cle.getCleId());
   }

   /**
    * Copie un Champ en BDD.
    *
    * @param champ
    *            Champ à copier.
    */
   /*@Override
   public Champ copyChampManager(Champ champ) {
   	// On vérifie que le groupement n'est pas nul
   	if (champ == null) {
   		log.warn("Objet obligatoire Champ manquant lors "
   				+ "de la copie d'un objet Champ");
   		throw new RequiredObjectIsNullException("Champ", "copie",
   		"Champ");
   	}
   	// On enregistre d'abord son parent
   	Champ copy = champ.copy();
   	Champ parent = copy.getChampParent();
   	if (parent != null) {
   		if (parent.getChampId() != null) {
   			parent = cleDao.mergeObject(parent);
   		} else {
   			parent = copyChampManager(parent);
   		}
   	}
   	copy.setChampParent(parent);
   	cleDao.createObject(copy);
   
   	return copy;
   }*/

   /**
    * Chercher les Champs enfants du Champ passé en paramètre.
    *
    * @param groupement
    *            Champ dont on souhaite obtenir la liste d'enfants.
    * @return liste des enfants (Champs) d'un Champ.
    */
   /*@Override
   public ArrayList<Champ> findEnfantsManager(Champ champ) {
   	// On vérifie que le groupement n'est pas nul
   	if (champ == null) {
   		log.warn("Objet obligatoire Champ manquant lors "
   				+ "de la recherche d'enfants d'un objet Champ");
   		throw new RequiredObjectIsNullException("Champ",
   				"recherche d'enfants", "Champ");
   	}
   	return cleDao.findEnfants(champ);
   }*/

   /**
    * Recherche un Champ dont l'identifiant est passé en paramètre.
    *
    * @param champId
    *            Identifiant du Champ que l'on recherche.
    * @return un Champ.
    */
   @Override
   public CleImpression findByIdManager(final Integer id){
      // On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche par identifiant", "identifiant");
      }
      return cleDao.findById(id);
   }

   @Override
   public List<CleImpression> findByNameManager(final String name){
      // On vérifie que l'identifiant n'est pas nul
      if(name.isEmpty()){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Champ");
         throw new RequiredObjectIsNullException("Champ", "recherche par identifiant", "identifiant");
      }
      return cleDao.findName(name);
   }

   /**
    * Recherche tous les Champs présents dans la BDD.
    *
    * @return Liste de Champs.
    */
   @Override
   public List<CleImpression> findAllObjectsManager(){
      return cleDao.findAll();
   }

   //   @Override
   //   public Object getValueForObjectManager(Champ champ, Object obj, boolean prettyFormat){
   //      Object res = null;
   //      if(champ != null && obj != null){
   //         String value = null;
   //         // si le champ est bien un champ interne à TK
   //         if(champ.getChampEntite() != null){
   //            // on formate le nom du champ
   //            String nomChamp =
   //               champ.getChampEntite().getNom().replaceFirst(".", (champ.getChampEntite().getNom().charAt(0) + "").toLowerCase());
   //            if(nomChamp.endsWith("Id")){
   //               nomChamp = nomChamp.substring(0, nomChamp.length() - 2);
   //            }
   //
   //            // on vérifie que l'objet a bien ce champ
   //            if(PropertyUtils.isReadable(obj, nomChamp)){
   //               try{
   //                  // extraction de la valeur
   //                  res = PropertyUtils.getProperty(obj, nomChamp);
   //               }catch(IllegalAccessException e){
   //                  log.error(e);
   //               }catch(InvocationTargetException e){
   //                  log.error(e);
   //               }catch(NoSuchMethodException e){
   //                  log.error(e);
   //               }
   //               // si la valeur retournée n'est pas null
   //               if(res != null){
   //                  // si le champ à extraire n'est pas un thésaurus
   //                  if(champ.getChampEntite().getQueryChamp() == null){
   //                     // on va formater la valeur de retour pour
   //                     // obtenir un String
   //                     Class<?> type = null;
   //                     try{
   //                        type = PropertyUtils.getPropertyDescriptor(obj, nomChamp).getPropertyType();
   //                     }catch(IllegalAccessException e){
   //                        log.error(e);
   //                     }catch(InvocationTargetException e){
   //                        log.error(e);
   //                     }catch(NoSuchMethodException e){
   //                        log.error(e);
   //                     }
   //                     if(type != null && prettyFormat){
   //                        // set d'un string
   //                        if(type == String.class){
   //                           value = res.toString();
   //                        }else if(type == Integer.class){
   //                           // si l'attibut est un integer, on caste
   //                           // la valeur issue de l'objet
   //                           Integer tmp = (Integer) res;
   //                           value = String.valueOf(tmp);
   //                        }else if(type == Float.class){
   //                           // si l'attibut est un float, on caste
   //                           // la valeur issue de l'objet
   //                           Float tmp = (Float) res;
   //                           value = String.valueOf(tmp);
   //                        }else if(type == Boolean.class){
   //                           // si l'attibut est un boolean, on caste
   //                           // la valeur issue de l'objet
   //                           Boolean tmp = (Boolean) res;
   //                           if(tmp){
   //                              value = "Oui";
   //                           }else{
   //                              value = "Non";
   //                           }
   //                        }else if(type == Date.class){
   //                           // si l'attibut est une date, on caste
   //                           // la valeur issue de l'objet
   //                           Date date = (Date) res;
   //                           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
   //                           value = sdf.format(date);
   //                        }else if(type == Calendar.class){
   //                           // si l'attibut est un calendar, on caste
   //                           // la valeur issue de l'objet
   //                           Calendar tmp = (Calendar) res;
   //                           SimpleDateFormat sdf = null;
   //                           if(tmp.get(Calendar.HOUR_OF_DAY) > 0 || tmp.get(Calendar.MINUTE) > 0 || tmp.get(Calendar.SECOND) > 0){
   //                              sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
   //                           }else{
   //                              sdf = new SimpleDateFormat("dd/MM/yyyy");
   //                           }
   //                           value = sdf.format(tmp.getTime());
   //                        }else if(type == Fichier.class){
   //                           value = ((Fichier) res).getNom();
   //                        }
   //                        return value;
   //                     }
   //                  }else{
   //                     // sinon, la variable res contient l'objet du
   //                     // thésaurus associé à l'objet. On va alors
   //                     // extraire la valeur sous forme de string
   //
   //                     // on formate le nom du champ de thesaurus
   //                     String nomChampThes = champ.getChampEntite().getQueryChamp().getNom().replaceFirst(".",
   //                        (champ.getChampEntite().getQueryChamp().getNom().charAt(0) + "").toLowerCase());
   //                     if(nomChampThes.endsWith("Id")){
   //                        nomChampThes = nomChampThes.substring(0, nomChampThes.length() - 2);
   //                     }
   //
   //                     // on vérifie que l'objet a bien ce champ
   //                     if(PropertyUtils.isReadable(res, nomChampThes)){
   //                        Object resThes = null;
   //                        try{
   //                           // extraction de la valeur
   //                           resThes = PropertyUtils.getProperty(res, nomChampThes);
   //                           return resThes.toString();
   //                        }catch(IllegalAccessException e){
   //                           log.error(e);
   //                        }catch(InvocationTargetException e){
   //                           log.error(e);
   //                        }catch(NoSuchMethodException e){
   //                           log.error(e);
   //                        }
   //                     }
   //                  }
   //               }
   //            }
   //         }
   //      }
   //      return res;
   //   }

   @Override
   public List<CleImpression> findByTemplateManager(final Template template){
      // On vérifie que l'identifiant n'est pas nul
      if(null == template){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Template");
         throw new RequiredObjectIsNullException("Template", "recherche par identifiant", "identifiant");
      }
      return cleDao.findByTemplate(template);
   }
}
