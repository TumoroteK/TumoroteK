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
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.imprimante.LigneEtiquetteManager;
import fr.aphp.tumorotek.manager.imprimante.ModeleManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.imprimante.ModeleValidator;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;

/**
 *
 * Implémentation du manager du bean de domaine Modele.
 * Classe créée le 21/03/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ModeleManagerImpl implements ModeleManager
{

   private final Log log = LogFactory.getLog(ModeleManager.class);

   /** Bean Dao. */
   private ModeleDao modeleDao;

   /** Bean Dao. */
   private ModeleTypeDao modeleTypeDao;

   /** Bean Dao. */
   private PlateformeDao plateformeDao;

   /** Bean Validator. */
   private ModeleValidator modeleValidator;

   private LigneEtiquetteManager ligneEtiquetteManager;

   public void setModeleDao(final ModeleDao mDao){
      this.modeleDao = mDao;
   }

   public void setModeleTypeDao(final ModeleTypeDao mDao){
      this.modeleTypeDao = mDao;
   }

   public void setPlateformeDao(final PlateformeDao pDao){
      this.plateformeDao = pDao;
   }

   public void setModeleValidator(final ModeleValidator mValidator){
      this.modeleValidator = mValidator;
   }

   public void setLigneEtiquetteManager(final LigneEtiquetteManager lManager){
      this.ligneEtiquetteManager = lManager;
   }

   @Override
   public Modele findByIdManager(final Integer modeleId){
      return modeleDao.findById(modeleId);
   }

   @Override
   public List<Modele> findAllObjectsManager(){
      log.debug("Recherche de tous les Modeles");
      return modeleDao.findAll();
   }

   @Override
   public List<Modele> findByPlateformeManager(final Plateforme plateforme){
      log.debug("Recherche de tous les Modeles d'une Plateforme");
      if(plateforme != null){
         return modeleDao.findByPlateforme(plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Modele> findByNomAndPlateformeManager(final String nom, final Plateforme plateforme){
      if(plateforme != null && nom != null){
         return modeleDao.findByNomAndPlateforme(nom, plateforme);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Modele modele){
      if(modele != null){
         if(modele.getModeleId() == null){
            return modeleDao.findAll().contains(modele);
         }else{
            return modeleDao.findByExcludedId(modele.getModeleId()).contains(modele);
         }
      }else{
         return false;
      }
   }

   @Override
   public Boolean isUsedObjectManager(Modele modele){
      if(modele != null && modele.getModeleId() != null){
         modele = modeleDao.mergeObject(modele);
         return (modele.getAffectationImprimantes().size() > 0);
      }else{
         return false;
      }
   }

   @Override
   public void createObjectManager(final Modele modele, final Plateforme plateforme, final ModeleType modeleType,
      final List<LigneEtiquette> ligneEtiquettes,
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettes){
      // plateforme required
      if(plateforme != null){
         modele.setPlateforme(plateformeDao.mergeObject(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la création d'un Modele");
         throw new RequiredObjectIsNullException("Modele", "creation", "Plateforme");
      }

      // modeleType required
      if(modeleType != null){
         modele.setModeleType(modeleTypeDao.mergeObject(modeleType));
      }else{
         log.warn("Objet obligatoire ModeleType manquant" + " lors de la création d'un Modele");
         throw new RequiredObjectIsNullException("Modele", "creation", "ModeleType");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(modele)){
         log.warn("Doublon lors de la creation de l'objet Modele : " + modele.toString());
         throw new DoublonFoundException("Modele", "creation");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(modele, new Validator[] {modeleValidator});

         if(!modele.getIsDefault() && ligneEtiquettes != null){
            for(int i = 0; i < ligneEtiquettes.size(); i++){
               List<ChampLigneEtiquette> champs = new ArrayList<>();
               if(champLigneEtiquettes.containsKey(ligneEtiquettes.get(i))){
                  champs = champLigneEtiquettes.get(ligneEtiquettes.get(i));
               }else{
                  champs = null;
               }
               ligneEtiquetteManager.validateObjectManager(ligneEtiquettes.get(i), modele, champs, "creation");
            }
         }

         modeleDao.createObject(modele);

         updateAssociations(modele, ligneEtiquettes, null, champLigneEtiquettes, null);

         log.info("Enregistrement de l'objet Modele : " + modele.toString());
      }
   }

   @Override
   public void updateObjectManager(final Modele modele, final Plateforme plateforme, final ModeleType modeleType,
      final List<LigneEtiquette> ligneEtiquettesToCreate, final List<LigneEtiquette> ligneEtiquettesToremove,
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToCreate,
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToRemove){
      // plateforme required
      if(plateforme != null){
         modele.setPlateforme(plateformeDao.mergeObject(plateforme));
      }else{
         log.warn("Objet obligatoire Plateforme manquant" + " lors de la modification d'un Modele");
         throw new RequiredObjectIsNullException("Modele", "modification", "Plateforme");
      }

      // modeleType required
      if(modeleType != null){
         modele.setModeleType(modeleTypeDao.mergeObject(modeleType));
      }else{
         log.warn("Objet obligatoire ModeleType manquant" + " lors de la modification d'un Modele");
         throw new RequiredObjectIsNullException("Modele", "modification", "ModeleType");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(modele)){
         log.warn("Doublon lors de la modification de l'objet Modele : " + modele.toString());
         throw new DoublonFoundException("Modele", "modification");
      }else{

         // validation du Contrat
         BeanValidator.validateObject(modele, new Validator[] {modeleValidator});

         if(!modele.getIsDefault() && ligneEtiquettesToCreate != null){
            for(int i = 0; i < ligneEtiquettesToCreate.size(); i++){
               List<ChampLigneEtiquette> champs = new ArrayList<>();
               if(champLigneEtiquettesToCreate.containsKey(ligneEtiquettesToCreate.get(i))){
                  champs = champLigneEtiquettesToCreate.get(ligneEtiquettesToCreate.get(i));
               }else{
                  champs = null;
               }
               ligneEtiquetteManager.validateObjectManager(ligneEtiquettesToCreate.get(i), modele, champs, "modification");
            }
         }

         modeleDao.updateObject(modele);

         updateAssociations(modele, ligneEtiquettesToCreate, ligneEtiquettesToremove, champLigneEtiquettesToCreate,
            champLigneEtiquettesToRemove);

         log.info("Enregistrement de l'objet Modele : " + modele.toString());
      }
   }

   @Override
   public void removeObjectManager(final Modele modele){
      if(modele != null){
         // suppression des lignes
         final List<LigneEtiquette> lignes = ligneEtiquetteManager.findByModeleManager(modele);
         for(int i = 0; i < lignes.size(); i++){
            ligneEtiquetteManager.removeObjectManager(lignes.get(i));
         }

         modeleDao.removeObject(modele.getModeleId());
         log.info("Suppression de l'objet Modele : " + modele.toString());
      }else{
         log.warn("Suppression d'un Modele null");
      }
   }

   /**
    * Cette méthode met à jour les associations entre un modele
    * et une ligne.
    * @param modele Modele pour lequel on veut mettre à jour
    * les associations.
    * @param ligneEtiquettesToCreate Liste des lignes a creer.
    * @param ligneEtiquettesToremove Liste de lignes à supprimer.
    */
   public void updateAssociations(final Modele modele, final List<LigneEtiquette> ligneEtiquettesToCreate,
      final List<LigneEtiquette> ligneEtiquettesToremove,
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToCreate,
      final Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToRemove){
      // gestion des lignes à supprimer
      if(ligneEtiquettesToremove != null){
         // suppression des lignes
         for(int i = 0; i < ligneEtiquettesToremove.size(); i++){
            ligneEtiquetteManager.removeObjectManager(ligneEtiquettesToremove.get(i));
         }
      }

      if(ligneEtiquettesToCreate != null){
         // ajout ou modif des lignes
         for(int i = 0; i < ligneEtiquettesToCreate.size(); i++){
            // on récup les champs à créer
            List<ChampLigneEtiquette> champs = new ArrayList<>();
            if(champLigneEtiquettesToCreate.containsKey(ligneEtiquettesToCreate.get(i))){
               champs = champLigneEtiquettesToCreate.get(ligneEtiquettesToCreate.get(i));
            }else{
               champs = null;
            }

            // création d'une nouvelle ligne
            if(ligneEtiquettesToCreate.get(i).getLigneEtiquetteId() == null){
               ligneEtiquetteManager.createObjectManager(ligneEtiquettesToCreate.get(i), modele, champs);
            }else{ //update de la ligne
               // on récup les champs à supprimer
               List<ChampLigneEtiquette> champsToRmv = new ArrayList<>();
               if(champLigneEtiquettesToRemove.containsKey(ligneEtiquettesToCreate.get(i))){
                  champsToRmv = champLigneEtiquettesToRemove.get(ligneEtiquettesToCreate.get(i));
               }else{
                  champsToRmv = null;
               }
               // update
               ligneEtiquetteManager.updateObjectManager(ligneEtiquettesToCreate.get(i), modele, champs, champsToRmv);
            }
         }
      }
   }
}
