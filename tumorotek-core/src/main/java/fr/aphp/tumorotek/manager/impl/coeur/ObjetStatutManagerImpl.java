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
package fr.aphp.tumorotek.manager.impl.coeur;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.ObjetStatutValidator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;

/**
 *
 * Implémentation du manager du bean de domaine ObjetStatut.
 * Classe créée le 01/10/09.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class ObjetStatutManagerImpl implements ObjetStatutManager
{

   private final Logger log = LoggerFactory.getLogger(ObjetStatutManager.class);

   private ObjetStatutDao objetStatutDao;

   private ObjetStatutValidator objetStatutValidator;

   private CederObjetManager cederObjetManager;

   public void setObjetStatutDao(final ObjetStatutDao oDao){
      this.objetStatutDao = oDao;
   }

   public void setObjetStatutValidator(final ObjetStatutValidator validator){
      this.objetStatutValidator = validator;
   }

   public void setCederObjetManager(final CederObjetManager c){
      this.cederObjetManager = c;
   }

   /**
    * Recherche une statut dont l'identifiant est passé en paramètre.
    * @param objetStatutId Identifiant du statut que l'on recherche.
    * @return Un ObjetStatut.
    */
   @Override
   public ObjetStatut findByIdManager(final Integer objetStatutId){
      return objetStatutDao.findById(objetStatutId);
   }

   /**
    * Recherche toutes les status présents dans la base.
    * @return Liste d'ObjetStatuts.
    */
   @Override
   public List<ObjetStatut> findAllObjectsManager(){
      log.debug("Recherche de tous les ObjetStatuts");
      return objetStatutDao.findByOrder();
   }

   /**
    * Recherche tous les ObjetStatuts dont le statut commence comme celui
    * passé en paramètre.
    * @param statut Statut que l'on recherche.
    * @param exactMatch True si l'on souhaite seulement récuéprer les matchs
    * exactes.
    * @return Liste d'ObjetStatut.
    */
   @Override
   public List<ObjetStatut> findByStatutLikeManager(String statut, final boolean exactMatch){
      log.debug("Recherche ObjetStatut par {} exactMatch {}", statut, exactMatch);
      if(statut != null){
         if(!exactMatch){
            statut = statut + "%";
         }
         return objetStatutDao.findByStatut(statut);
      }else{
         return new ArrayList<>();
      }
   }

   /**
    * Recherche les doublons d'ObjetStatut passé en paramètre.
    * @param statut ObjetStatut pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   @Override
   public Boolean findDoublonManager(final ObjetStatut statut){
      return objetStatutDao.findAll().contains(statut);
   }

   /**
    * Test si un ObjetStatut est lié à des produits dérivés ou à des
    * échantillons.
    * @param statut ObjetStatut que l'on souhaite tester.
    * @return Vrai si le statut est utilisé.
    */
   @Override
   public Boolean isUsedObjectManager(ObjetStatut statut){
      int nb = 0;
      statut = objetStatutDao.mergeObject(statut);

      nb = nb + statut.getEchantillons().size();
      nb = nb + statut.getProdDerives().size();

      return (nb > 0);
   }

   /**
    * Persist une instance de ObjetStatut dans la base de données.
    * @param statut Nouvelle instance de l'objet à créer.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   @Override
   public void createObjectManager(final ObjetStatut statut){
      if(findDoublonManager(statut)){
         log.warn("Doublon lors de la creation de l'objet ObjetStatut : {}",  statut);
         throw new DoublonFoundException("ObjetStatut", "creation");
      }else{
         BeanValidator.validateObject(statut, new Validator[] {objetStatutValidator});
         objetStatutDao.createObject(statut);
         log.debug("Enregistrement de l'objet ObjetStatut : {}", statut);
      }
   }

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param statut Objet à mettre à jour dans la base.
    * @throws DoublonFoundException Lance une exception si un doublon de
    * l'objet à créer se trouve déjà dans la base.
    */
   @Override
   public void updateObjectManager(final ObjetStatut statut){
      if(findDoublonManager(statut)){
         log.warn("Doublon lors de la modif de l'objet ObjetStatut : {}",  statut);
         throw new DoublonFoundException("ObjetStatut", "modification");
      }else{
         BeanValidator.validateObject(statut, new Validator[] {objetStatutValidator});
         objetStatutDao.updateObject(statut);
         log.debug("Modification de l'objet ObjetStatut : {}", statut);
      }
   }

   /**
    * Supprime un ObjetStatut de la base de données.
    * @param statut ObjetStatut à supprimer de la base de données.
    * @throws DoublonFoundException Lance une exception si l'objet
    * est utilisé par des échantillons.
    */
   @Override
   public void removeObjectManager(final ObjetStatut statut){
      if(isUsedObjectManager(statut)){
         log.warn("Objet utilisé lors de la supperssion de l'objet " + "ObjetStatut : " + statut.toString());
         throw new ObjectUsedException("ObjetStatut", "suppression");
      }else{
         objetStatutDao.removeObject(statut.getObjetStatutId());
         log.debug("Suppression de l'objet ObjetStatut : {}", statut);
      }
   }

   @Override
   public ObjetStatut findStatutForTKStockableObject(final TKStockableObject tkobj){
      final ObjetStatut nonStocke = findByStatutLikeManager("NON STOCKE", true).get(0);

      final ObjetStatut stocke = findByStatutLikeManager("STOCKE", true).get(0);
      ObjetStatut epuise = null;

      final List<Cession> cessionsEnAttente = cederObjetManager.getAllCessionsByStatutAndObjetManager("EN ATTENTE", tkobj);

      if(cessionsEnAttente.size() == 0){
         epuise = findByStatutLikeManager("EPUISE", true).get(0);
         if(tkobj.getObjetStatut() != null && tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
            epuise = findByStatutLikeManager("DETRUIT", true).get(0);
         }
      }else{
         epuise = findByStatutLikeManager("RESERVE", true).get(0);
      }

      final Float zero = (float) 0.0;
      // si la qté est non null
      if(tkobj.getQuantite() != null){

         // si sa valeur est égale a 0 => epuisé
         if(tkobj.getQuantite().equals(zero)){
            return epuise;
         }else{
            if(tkobj.getObjetStatut() != null && !tkobj.getObjetStatut().getStatut().equals("EPUISE")
               && !tkobj.getObjetStatut().getStatut().equals("RESERVE") && !tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
               return tkobj.getObjetStatut();
            }else if(tkobj.getEmplacement() != null){
               return stocke;
            }else{
               return nonStocke;
            }
         }

      }else if(tkobj instanceof ProdDerive && ((ProdDerive) tkobj).getVolume() != null){
         // si sa valeur est égale a 0 => epuisé
         if(((ProdDerive) tkobj).getVolume().equals(zero)){
            return epuise;
         }else{
            if(tkobj.getObjetStatut() != null && !tkobj.getObjetStatut().getStatut().equals("EPUISE")
               && !tkobj.getObjetStatut().getStatut().equals("RESERVE") && !tkobj.getObjetStatut().getStatut().equals("DETRUIT")){
               return tkobj.getObjetStatut();
            }else if(tkobj.getEmplacement() != null){
               return stocke;
            }else{
               return nonStocke;
            }
         }
      }

      return tkobj.getObjetStatut();
   }
}
