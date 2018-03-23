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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.io.export.ChampEntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.io.ChampEntiteManager;
import fr.aphp.tumorotek.manager.io.export.AffichageManager;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.systeme.Entite;

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

   private final Log log = LogFactory.getLog(AffichageManager.class);

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
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<ChampEntite> findByEntiteAndImportManager(final Entite entite, final Boolean canImport){
      if(entite != null && canImport != null){
         return champEntiteDao.findByEntiteAndImport(entite, canImport);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<ChampEntite> findByEntiteImportAndIsNullableManager(final Entite entite, final Boolean canImport,
      final Boolean isNullable){
      if(entite != null && canImport != null && isNullable != null){
         return champEntiteDao.findByEntiteImportObligatoire(entite, canImport, isNullable);
      }else{
         return new ArrayList<>();
      }
   }
}
