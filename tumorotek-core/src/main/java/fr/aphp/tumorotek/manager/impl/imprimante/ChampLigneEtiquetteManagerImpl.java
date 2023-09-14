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
package fr.aphp.tumorotek.manager.impl.imprimante;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.imprimante.ChampLigneEtiquetteDao;
import fr.aphp.tumorotek.dao.imprimante.LigneEtiquetteDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.systeme.Entite;

public class ChampLigneEtiquetteManagerImpl implements ChampLigneEtiquetteManager
{

   private final Log log = LogFactory.getLog(ChampLigneEtiquetteManager.class);

   /** Beans. */
   private ChampLigneEtiquetteDao champLigneEtiquetteDao;

   private LigneEtiquetteDao ligneEtiquetteDao;

   private EntiteDao entiteDao;

   private ChampManager champManager;

   public void setChampLigneEtiquetteDao(final ChampLigneEtiquetteDao cDao){
      this.champLigneEtiquetteDao = cDao;
   }

   public void setLigneEtiquetteDao(final LigneEtiquetteDao lDao){
      this.ligneEtiquetteDao = lDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   public void setChampManager(final ChampManager cManager){
      this.champManager = cManager;
   }

   @Override
   public ChampLigneEtiquette findByIdManager(final Integer champLigneEtiquetteId){
      return champLigneEtiquetteDao.findById(champLigneEtiquetteId);
   }

   @Override
   public List<ChampLigneEtiquette> findAllObjectsManager(){
      log.debug("Recherche de tous les ChampLigneEtiquettes.");
      return champLigneEtiquetteDao.findAll();
   }

   @Override
   public List<ChampLigneEtiquette> findByLigneEtiquetteManager(final LigneEtiquette ligneEtiquette){
      if(ligneEtiquette != null){
         log.debug("Recherche de tous les ChampLigneEtiquettes " + "d'une LigneEtiquette.");
         return champLigneEtiquetteDao.findByLigneEtiquette(ligneEtiquette);
      }
      return new ArrayList<>();
   }

   @Override
   public List<ChampLigneEtiquette> findByLigneEtiquetteAndEntiteManager(final LigneEtiquette ligneEtiquette,
      final Entite entite){
      if(ligneEtiquette != null && entite != null){
         log.debug("Recherche de tous les ChampLigneEtiquettes " + "d'une LigneEtiquette et d'une Entite.");
         return champLigneEtiquetteDao.findByLigneEtiquetteAndEntite(ligneEtiquette, entite);
      }
      return new ArrayList<>();
   }

   @Override
   public void validateObjectManager(final ChampLigneEtiquette champLigneEtiquette, final LigneEtiquette ligneEtiquette,
      final Entite entite, final Champ champ, final String operation){
      // LigneEtiquette required
      if(ligneEtiquette == null){
         log.warn("Objet obligatoire LigneEtiquette manquant" + " lors de la création d'un ChampLigneEtiquette");
         throw new RequiredObjectIsNullException("ChampLigneEtiquette", operation, "LigneEtiquette");
      }

      // champ required
      if(champ == null){
         log.warn("Objet obligatoire Champ manquant" + " lors de la création d'un ChampLigneEtiquette");
         throw new RequiredObjectIsNullException("ChampLigneEtiquette", operation, "Champ");
      }

      // Entite required
      if(entite == null){
         log.warn("Objet obligatoire Entite manquant" + " lors de la création d'un ChampLigneEtiquette");
         throw new RequiredObjectIsNullException("ChampLigneEtiquette", operation, "Entite");
      }

      // Ordre required
      if(champLigneEtiquette.getOrdre() == null){
         log.warn("Objet obligatoire Ordre manquant" + " lors de la création d'un ChampLigneEtiquette");
         throw new RequiredObjectIsNullException("ChampLigneEtiquette", operation, "Ordre");
      }
   }

   @Override
   public void createObjectManager(final ChampLigneEtiquette champLigneEtiquette, final LigneEtiquette ligneEtiquette,
      final Entite entite, final Champ champ){
      // validation de l'objet
      validateObjectManager(champLigneEtiquette, ligneEtiquette, entite, champ, "creation");

      champLigneEtiquette.setLigneEtiquette(ligneEtiquetteDao.mergeObject(ligneEtiquette));
      champLigneEtiquette.setEntite(entiteDao.mergeObject(entite));

      champManager.createObjectManager(champ, null);
      champLigneEtiquette.setChamp(champ);

      champLigneEtiquetteDao.createObject(champLigneEtiquette);

      log.info("Enregistrement objet ChampLigneEtiquette " + champLigneEtiquette.toString());
   }

   @Override
   public void updateObjectManager(final ChampLigneEtiquette champLigneEtiquette, final LigneEtiquette ligneEtiquette,
      final Entite entite, final Champ champ){
      // validation de l'objet
      validateObjectManager(champLigneEtiquette, ligneEtiquette, entite, champ, "modification");

      champLigneEtiquette.setLigneEtiquette(ligneEtiquetteDao.mergeObject(ligneEtiquette));
      champLigneEtiquette.setEntite(entiteDao.mergeObject(entite));

      // si le champ n'existe pas, on le crée
      if(champ.getChampId() == null){
         champManager.createObjectManager(champ, null);
      }else{
         champManager.updateObjectManager(champ, null);
      }
      champLigneEtiquette.setChamp(champ);

      champLigneEtiquetteDao.updateObject(champLigneEtiquette);

      log.info("Enregistrement objet ChampLigneEtiquette " + champLigneEtiquette.toString());
   }

   @Override
   public void removeObjectManager(final ChampLigneEtiquette champLigneEtiquette){
      if(champLigneEtiquette != null){
         final Champ chp = champLigneEtiquette.getChamp();
         champLigneEtiquetteDao.removeObject(champLigneEtiquette.getChampLigneEtiquetteId());
         log.info("Suppression de l'objet ChampLigneEtiquette : " + champLigneEtiquette.toString());

         champManager.removeObjectManager(chp);
      }else{
         log.warn("Suppression d'un ChampLigneEtiquette null");
      }
   }
}
