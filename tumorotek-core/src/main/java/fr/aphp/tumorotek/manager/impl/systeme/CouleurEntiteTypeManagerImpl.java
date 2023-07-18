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
package fr.aphp.tumorotek.manager.impl.systeme;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.coeur.echantillon.EchantillonTypeDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdTypeDao;
import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.dao.systeme.CouleurEntiteTypeDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.InvalidMultipleAssociationException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.systeme.CouleurEntiteTypeManager;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Couleur;
import fr.aphp.tumorotek.model.systeme.CouleurEntiteType;

public class CouleurEntiteTypeManagerImpl implements CouleurEntiteTypeManager
{

   private final Logger log = LoggerFactory.getLogger(CouleurEntiteTypeManager.class);

   /** Bean Dao. */
   private CouleurEntiteTypeDao couleurEntiteTypeDao;

   /** Bean Dao. */
   private CouleurDao couleurDao;

   /** Bean Dao. */
   private BanqueDao banqueDao;

   /** Bean Dao. */
   private EchantillonTypeDao echantillonTypeDao;

   /** Bean Dao. */
   private ProdTypeDao prodTypeDao;

   public void setCouleurEntiteTypeDao(final CouleurEntiteTypeDao cDao){
      this.couleurEntiteTypeDao = cDao;
   }

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEchantillonTypeDao(final EchantillonTypeDao eDao){
      this.echantillonTypeDao = eDao;
   }

   public void setProdTypeDao(final ProdTypeDao pDao){
      this.prodTypeDao = pDao;
   }

   @Override
   public CouleurEntiteType findByIdManager(final Integer couleurEntiteTypeId){
      return couleurEntiteTypeDao.findById(couleurEntiteTypeId);
   }

   @Override
   public List<CouleurEntiteType> findAllObjectsManager(){
      return couleurEntiteTypeDao.findAll();
   }

   @Override
   public List<CouleurEntiteType> findAllObjectsByBanqueManager(final Banque banque){
      if(banque != null && banque.getBanqueId() != null){
         return couleurEntiteTypeDao.findByBanque(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<CouleurEntiteType> findAllCouleursForEchanTypeByBanqueManager(final Banque banque){
      if(banque != null && banque.getBanqueId() != null){
         return couleurEntiteTypeDao.findByBanqueAllEchanType(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public List<CouleurEntiteType> findAllCouleursForProdTypeByBanqueManager(final Banque banque){
      if(banque != null && banque.getBanqueId() != null){
         return couleurEntiteTypeDao.findByBanqueAllProdType(banque);
      }
      return new ArrayList<>();
   }

   @Override
   public Boolean findDoublonManager(final CouleurEntiteType couleurEntiteType){
      if(couleurEntiteType != null){
         if(couleurEntiteType.getCouleurEntiteTypeId() == null){
            return couleurEntiteTypeDao.findAll().contains(couleurEntiteType);
         }
         return couleurEntiteTypeDao.findByExcludedId(couleurEntiteType.getCouleurEntiteTypeId()).contains(couleurEntiteType);
      }
      return false;
   }

   @Override
   public void createObjectManager(final CouleurEntiteType couleurEntiteType, final Couleur couleur, final Banque banque,
      final EchantillonType echantillonType, final ProdType prodType){

      //Banque required
      if(banque != null){
         couleurEntiteType.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant  lors de la création d'une  CouleurEntiteType");
         throw new RequiredObjectIsNullException("CouleurEntiteType", "creation", "Banque");
      }

      //Couleur required
      if(couleur != null){
         couleurEntiteType.setCouleur(couleurDao.mergeObject(couleur));
      }else{
         log.warn("Objet obligatoire Couleur manquant  lors de la création d'une  CouleurEntiteType");
         throw new RequiredObjectIsNullException("CouleurEntiteType", "creation", "Couleur");
      }

      // il faut qu'un seul type soit défini pour cette relation
      if(echantillonType != null && prodType != null){
         log.warn("Deux types sont définis  lors de la création d'une CouleurEntiteType");
         throw new InvalidMultipleAssociationException("CouleurEntiteType", "creation", false);
      }else if(echantillonType == null && prodType == null){
         log.warn("Aucun type n'est défini  lors de la création d'une CouleurEntiteType");
         throw new InvalidMultipleAssociationException("CouleurEntiteType", "creation", true);
      }

      couleurEntiteType.setEchantillonType(echantillonTypeDao.mergeObject(echantillonType));
      couleurEntiteType.setProdType(prodTypeDao.mergeObject(prodType));

      // Test s'il y a des doublons
      if(findDoublonManager(couleurEntiteType)){
         log.warn("Doublon lors de la creation de l'objet CouleurEntiteType : {}",  couleurEntiteType);
         throw new DoublonFoundException("CouleurEntiteType", "creation");
      }

      couleurEntiteTypeDao.createObject(couleurEntiteType);

      log.info("Enregistrement de l'objet CouleurEntiteType : {}",  couleurEntiteType);
   }

   @Override
   public void updateObjectManager(final CouleurEntiteType couleurEntiteType, final Couleur couleur, final Banque banque,
      final EchantillonType echantillonType, final ProdType prodType){
      //Banque required
      if(banque != null){
         couleurEntiteType.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant  lors de la modification d'une  CouleurEntiteType");
         throw new RequiredObjectIsNullException("CouleurEntiteType", "modification", "Banque");
      }

      //Couleur required
      if(couleur != null){
         couleurEntiteType.setCouleur(couleurDao.mergeObject(couleur));
      }else{
         log.warn("Objet obligatoire Couleur manquant  lors de la modification d'une  CouleurEntiteType");
         throw new RequiredObjectIsNullException("CouleurEntiteType", "modification", "Couleur");
      }

      // il faut qu'un seul type soit défini pour cette relation
      if(echantillonType != null && prodType != null){
         log.warn("Deux types sont définis  lors de la modification d'une CouleurEntiteType");
         throw new InvalidMultipleAssociationException("CouleurEntiteType", "modification", false);
      }else if(echantillonType == null && prodType == null){
         log.warn("Aucun type n'est défini  lors de la modification d'une CouleurEntiteType");
         throw new InvalidMultipleAssociationException("CouleurEntiteType", "modification", true);
      }

      couleurEntiteType.setEchantillonType(echantillonTypeDao.mergeObject(echantillonType));
      couleurEntiteType.setProdType(prodTypeDao.mergeObject(prodType));

      // Test s'il y a des doublons
      if(findDoublonManager(couleurEntiteType)){
         log.warn("Doublon lors de la modification de l'objet CouleurEntiteType : {}",  couleurEntiteType);
         throw new DoublonFoundException("CouleurEntiteType", "modification");
      }

      couleurEntiteTypeDao.updateObject(couleurEntiteType);

      log.info("Enregistrement de l'objet CouleurEntiteType : {}",  couleurEntiteType);
   }

   @Override
   public void removeObjectManager(final CouleurEntiteType couleurEntiteType){
      if(couleurEntiteType != null){
         couleurEntiteTypeDao.removeObject(couleurEntiteType.getCouleurEntiteTypeId());
         log.info("Suppression de l'objet CouleurEntiteType : {}",  couleurEntiteType);
      }else{
         log.warn("Suppression d'une CouleurEntiteType null");
      }
   }
}