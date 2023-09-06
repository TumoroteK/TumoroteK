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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.imprimante.LigneEtiquetteDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.imprimante.ChampLigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.imprimante.LigneEtiquetteValidator;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;

public class LigneEtiquetteManagerImpl implements LigneEtiquetteManager
{

   private final Logger log = LoggerFactory.getLogger(LigneEtiquetteManager.class);

   /** Beans. */
   private LigneEtiquetteDao ligneEtiquetteDao;

   private ModeleDao modeleDao;

   private ChampLigneEtiquetteManager champLigneEtiquetteManager;

   private LigneEtiquetteValidator ligneEtiquetteValidator;

   public void setLigneEtiquetteDao(final LigneEtiquetteDao lDao){
      this.ligneEtiquetteDao = lDao;
   }

   public void setModeleDao(final ModeleDao mDao){
      this.modeleDao = mDao;
   }

   public void setChampLigneEtiquetteManager(final ChampLigneEtiquetteManager cManager){
      this.champLigneEtiquetteManager = cManager;
   }

   public void setLigneEtiquetteValidator(final LigneEtiquetteValidator lValidator){
      this.ligneEtiquetteValidator = lValidator;
   }

   @Override
   public LigneEtiquette findByIdManager(final Integer ligneEtiquetteId){
      return ligneEtiquetteDao.findById(ligneEtiquetteId);
   }

   @Override
   public List<LigneEtiquette> findAllObjectsManager(){
      log.debug("Recherche de toutes les LigneEtiquettes.");
      return ligneEtiquetteDao.findAll();
   }

   @Override
   public List<LigneEtiquette> findByModeleManager(final Modele modele){
      log.debug("Recherche de toutes les LigneEtiquettes d'un modèle.");
      if(modele != null){
         return ligneEtiquetteDao.findByModele(modele);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public void validateObjectManager(final LigneEtiquette ligneEtiquette, final Modele modele,
      final List<ChampLigneEtiquette> champLigneEtiquettes, final String operation){
      // modele required
      if(modele == null){
         log.warn("Objet obligatoire Modele manquant  lors de la création d'une LigneEtiquette");
         throw new RequiredObjectIsNullException("LigneEtiquette", operation, "Modele");
      }

      if(champLigneEtiquettes != null){
         for(int i = 0; i < champLigneEtiquettes.size(); i++){
            champLigneEtiquetteManager.validateObjectManager(champLigneEtiquettes.get(i), ligneEtiquette,
               champLigneEtiquettes.get(i).getEntite(), champLigneEtiquettes.get(i).getChamp(), operation);
         }
      }

      // validation due l'utilisateur
      BeanValidator.validateObject(ligneEtiquette, new Validator[] {ligneEtiquetteValidator});
   }

   @Override
   public void createObjectManager(final LigneEtiquette ligneEtiquette, final Modele modele,
      final List<ChampLigneEtiquette> champLigneEtiquettes){
      // validation de l'objet
      validateObjectManager(ligneEtiquette, modele, champLigneEtiquettes, "creation");

      ligneEtiquette.setModele(modeleDao.mergeObject(modele));

      ligneEtiquetteDao.createObject(ligneEtiquette);

      updateAssociations(ligneEtiquette, champLigneEtiquettes, null);

      log.info("Enregistrement objet LigneEtiquette {}",  ligneEtiquette);
   }

   @Override
   public void updateObjectManager(final LigneEtiquette ligneEtiquette, final Modele modele,
      final List<ChampLigneEtiquette> champLigneEtiquettesToCreate, final List<ChampLigneEtiquette> champLigneEtiquettesToremove){
      // validation de l'objet
      validateObjectManager(ligneEtiquette, modele, champLigneEtiquettesToCreate, "modification");

      ligneEtiquette.setModele(modeleDao.mergeObject(modele));

      ligneEtiquetteDao.updateObject(ligneEtiquette);

      updateAssociations(ligneEtiquette, champLigneEtiquettesToCreate, champLigneEtiquettesToremove);

      log.info("Enregistrement objet LigneEtiquette {}",  ligneEtiquette);
   }

   @Override
   public void removeObjectManager(final LigneEtiquette ligneEtiquette){
      if(ligneEtiquette != null){
         // suppression des champs
         final List<ChampLigneEtiquette> champs = champLigneEtiquetteManager.findByLigneEtiquetteManager(ligneEtiquette);
         for(int i = 0; i < champs.size(); i++){
            champLigneEtiquetteManager.removeObjectManager(champs.get(i));
         }

         ligneEtiquetteDao.removeObject(ligneEtiquette.getLigneEtiquetteId());
         log.info("Suppression de l'objet LigneEtiquette : {}",  ligneEtiquette);
      }else{
         log.warn("Suppression d'une LigneEtiquette null");
      }
   }

   /**
    * Cette méthode met à jour les associations entre une ligne
    * et une liste de ChampLigneEtiquette.
    * @param ligneEtiquette LigneEtiquette laquelle on veut mettre à jour
    * les associations.
    * @param champLigneEtiquettesToCreate Liste des champs a creer.
    * @param champLigneEtiquettesToremove Liste de champs à supprimer.
    */
   public void updateAssociations(final LigneEtiquette ligneEtiquette,
      final List<ChampLigneEtiquette> champLigneEtiquettesToCreate, final List<ChampLigneEtiquette> champLigneEtiquettesToremove){
      // gestion des champs à supprimer
      if(champLigneEtiquettesToremove != null){
         // suppression des colonnes
         for(int i = 0; i < champLigneEtiquettesToremove.size(); i++){
            champLigneEtiquetteManager.removeObjectManager(champLigneEtiquettesToremove.get(i));
         }
      }

      if(champLigneEtiquettesToCreate != null){
         // ajout ou modif des champs
         for(int i = 0; i < champLigneEtiquettesToCreate.size(); i++){
            if(champLigneEtiquettesToCreate.get(i).getChampLigneEtiquetteId() == null){
               champLigneEtiquetteManager.createObjectManager(champLigneEtiquettesToCreate.get(i), ligneEtiquette,
                  champLigneEtiquettesToCreate.get(i).getEntite(), champLigneEtiquettesToCreate.get(i).getChamp());
            }else{
               champLigneEtiquetteManager.updateObjectManager(champLigneEtiquettesToCreate.get(i), ligneEtiquette,
                  champLigneEtiquettesToCreate.get(i).getEntite(), champLigneEtiquettesToCreate.get(i).getChamp());
            }
         }
      }
   }
}