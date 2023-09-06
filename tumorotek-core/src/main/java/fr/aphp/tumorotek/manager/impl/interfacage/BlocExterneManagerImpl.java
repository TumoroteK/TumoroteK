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
package fr.aphp.tumorotek.manager.impl.interfacage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.interfacage.BlocExterneDao;
import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.EntiteObjectIdNotExistException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Implémentation du manager du bean de domaine BlocExterne.
 * Classe créée le 07/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class BlocExterneManagerImpl implements BlocExterneManager
{

   private final Logger log = LoggerFactory.getLogger(BlocExterneManager.class);

   /** Bean Dao. */
   private BlocExterneDao blocExterneDao;

   /** Bean Dao. */
   private DossierExterneDao dossierExterneDao;

   /** Bean Dao. */
   private EntiteDao entiteDao;

   /** Bean Manager. */
   private ValeurExterneManager valeurExterneManager;

   public void setBlocExterneDao(final BlocExterneDao bDao){
      this.blocExterneDao = bDao;
   }

   public void setDossierExterneDao(final DossierExterneDao dDao){
      this.dossierExterneDao = dDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setValeurExterneManager(final ValeurExterneManager vManager){
      this.valeurExterneManager = vManager;
   }

   @Override
   public BlocExterne findByIdManager(final Integer blocExterneId){
      return blocExterneDao.findById(blocExterneId);
   }

   @Override
   public List<BlocExterne> findAllObjectsManager(){
      log.debug("Recherche de tous les BlocExternes");
      return blocExterneDao.findAll();
   }

   @Override
   public List<BlocExterne> findByDossierExterneManager(final DossierExterne dossierExterne){
      log.debug("Recherche de tous les BlocExternes pour un dossier");
      if(dossierExterne != null){
         return blocExterneDao.findByDossierExterne(dossierExterne);
      }
      return new ArrayList<>();
   }

   @Override
   public List<BlocExterne> findByDossierExterneAndEntiteManager(final DossierExterne dossierExterne, final Entite entite){
      log.debug("Recherche de tous les BlocExternes pour un dossier  et une entité");
      if(dossierExterne != null && entite != null){
         return blocExterneDao.findByDossierExterneAndEntite(dossierExterne, entite.getEntiteId());
      }
      return new ArrayList<>();
   }

   @Override
   public Entite getEntiteManager(final BlocExterne blocExterne){
      if(blocExterne != null && blocExterne.getEntiteId() != null){
         return entiteDao.findById(blocExterne.getEntiteId());
      }
      return null;
   }

   @Override
   public boolean findDoublonManager(final BlocExterne blocExterne){
      if(blocExterne != null){
         return blocExterneDao.findByDossierExterne(blocExterne.getDossierExterne()).contains(blocExterne);
      }
      return false;
   }

   @Override
   public void validateBlocExterneManager(final BlocExterne blocExterne, final DossierExterne dossierExterne){
      // dossierExterne required
      if(dossierExterne == null){
         log.warn("Objet obligatoire DossierExterne manquant  lors de la création d'un BlocExterne");
         throw new RequiredObjectIsNullException("BlocExterne", "creation", "DossierExterne");
      }

      // entite required
      if(blocExterne.getEntiteId() == null){
         log.warn("Objet obligatoire Entite manquant  lors de la création d'un BlocExterne");
         throw new RequiredObjectIsNullException("BlocExterne", "creation", "Entite");
      }

      // il faut que le entité existe
      if(blocExterne.getEntiteId() != null){
         if(entiteDao.findById(blocExterne.getEntiteId()) == null){
            throw new EntiteObjectIdNotExistException("BlocExterne", "Entite", blocExterne.getEntiteId());
         }
      }

      // ordre required
      if(blocExterne.getOrdre() == null){
         log.warn("Objet obligatoire Ordre manquant  lors de la création d'un BlocExterne");
         throw new RequiredObjectIsNullException("BlocExterne", "creation", "Ordre");
      }
   }

   @Override
   public void createObjectManager(final BlocExterne blocExterne, final DossierExterne dossierExterne,
      final List<ValeurExterne> valeurExternes){
      // Validation du bloc
      validateBlocExterneManager(blocExterne, dossierExterne);

      blocExterne.setDossierExterne(dossierExterneDao.mergeObject(dossierExterne));

      if(findDoublonManager(blocExterne)){
         removeObjectManager(blocExterneDao.findByDossierExterne(dossierExterne)
            .get(blocExterneDao.findByDossierExterne(dossierExterne).indexOf(blocExterne)));
      }
      blocExterneDao.createObject(blocExterne);
      log.info("Enregistrement de l'objet BlocExterne : {}",  blocExterne);

      // création des valeurs
      if(valeurExternes != null){
         for(int i = 0; i < valeurExternes.size(); i++){
            valeurExterneManager.createObjectManager(valeurExternes.get(i), blocExterne);
         }
      }
   }

   @Override
   public void removeObjectManager(final BlocExterne blocExterne){
      if(blocExterne != null && blocExterne.getBlocExterneId() != null){

         // suppression des valeurs
         final List<ValeurExterne> valeurs = valeurExterneManager.findByBlocExterneManager(blocExterne);
         for(int i = 0; i < valeurs.size(); i++){
            valeurExterneManager.removeObjectManager(valeurs.get(i));
         }

         blocExterneDao.removeObject(blocExterne.getBlocExterneId());
         log.info("Suppression de l'objet BlocExterne : {}",  blocExterne);
      }else{
         log.warn("Suppression d'une BlocExterne null");
      }
   }
}