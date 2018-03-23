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

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.imprimante.AffectationImprimanteDao;
import fr.aphp.tumorotek.dao.imprimante.ImprimanteDao;
import fr.aphp.tumorotek.dao.imprimante.ModeleDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.imprimante.AffectationImprimanteManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimante;
import fr.aphp.tumorotek.model.imprimante.AffectationImprimantePK;
import fr.aphp.tumorotek.model.imprimante.Imprimante;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * Implémentation du manager du bean de domaine AffectationImprimante.
 * Interface créée le 22/03/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class AffectationImprimanteManagerImpl implements AffectationImprimanteManager
{

   private final Log log = LogFactory.getLog(AffectationImprimanteManager.class);

   /** Bean Dao. */
   private AffectationImprimanteDao affectationImprimanteDao;
   /** Bean Dao. */
   private ImprimanteDao imprimanteDao;
   /** Bean Dao. */
   private UtilisateurDao utilisateurDao;
   /** Bean Dao. */
   private BanqueDao banqueDao;
   /** Bean Dao. */
   private ModeleDao modeleDao;

   public void setAffectationImprimanteDao(final AffectationImprimanteDao aDao){
      this.affectationImprimanteDao = aDao;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setImprimanteDao(final ImprimanteDao iDao){
      this.imprimanteDao = iDao;
   }

   public void setModeleDao(final ModeleDao mDao){
      this.modeleDao = mDao;
   }

   @Override
   public AffectationImprimante findByIdManager(final AffectationImprimantePK pk){
      return affectationImprimanteDao.findById(pk);
   }

   @Override
   public List<AffectationImprimante> findAllObjectsManager(){
      log.debug("Recherche de toutes les AffectationImprimantes");
      return affectationImprimanteDao.findAll();
   }

   @Override
   public List<AffectationImprimante> findByBanqueUtilisateurManager(final Banque banque, final Utilisateur utilisateur){
      if(utilisateur != null && banque != null){
         return affectationImprimanteDao.findByBanqueUtilisateur(banque, utilisateur);
      }
         return new ArrayList<>();
      }

   @Override
   public List<AffectationImprimante> findByExcludedPKManager(final AffectationImprimantePK pk){
      if(pk != null){
         return affectationImprimanteDao.findByExcludedPK(pk);
      }
         return affectationImprimanteDao.findAll();
      }

   @Override
   public Boolean findDoublonManager(final Utilisateur utilisateur, final Banque banque, final Imprimante imprimante){

      final AffectationImprimantePK pk = new AffectationImprimantePK(utilisateur, banque, imprimante);

      return (affectationImprimanteDao.findById(pk) != null);

   }

   @Override
   public void validateObjectManager(final Utilisateur utilisateur, final Banque banque, final Imprimante imprimante){

      //utilisateur required
      if(utilisateur == null){
         log.warn("Objet obligatoire Utilisateur manquant" + " lors de la validation d'une AffectationImprimante");
         throw new RequiredObjectIsNullException("AffectationImprimante", "creation", "Utilisateur");
      }

      //OperationType required
      if(banque == null){
         log.warn("Objet obligatoire Banque manquant" + " lors de la validation d'une AffectationImprimante");
         throw new RequiredObjectIsNullException("AffectationImprimante", "creation", "Banque");
      }

      //imprimante required
      if(imprimante == null){
         log.warn("Objet obligatoire Imprimante manquant" + " lors de la validation d'une AffectationImprimante");
         throw new RequiredObjectIsNullException("AffectationImprimante", "creation", "Imprimante");
      }
   }

   @Override
   public void createObjectManager(final AffectationImprimante affectationImprimante, final Utilisateur utilisateur,
      final Banque banque, final Imprimante imprimante, final Modele modele){
      // validation de l'objet à créer
      validateObjectManager(utilisateur, banque, imprimante);

      affectationImprimante.setUtilisateur(utilisateurDao.mergeObject(utilisateur));
      affectationImprimante.setBanque(banqueDao.mergeObject(banque));
      affectationImprimante.setImprimante(imprimanteDao.mergeObject(imprimante));
      if(modele != null){
         affectationImprimante.setModele(modeleDao.mergeObject(modele));
      }else{
         affectationImprimante.setModele(null);
      }

      // si pas de doublon,création de l'objet sinon update
      if(!findDoublonManager(utilisateur, banque, imprimante)){
         // création
         affectationImprimanteDao.createObject(affectationImprimante);

         log.info("Enregistrement objet AffectationImprimante " + affectationImprimante.toString());
      }else{
         // update
         affectationImprimanteDao.updateObject(affectationImprimante);

         log.info("Enregistrement objet AffectationImprimante " + affectationImprimante.toString());
      }
   }

   @Override
   public void removeObjectManager(final AffectationImprimante affectationImprimante){
      if(affectationImprimante != null){
         affectationImprimanteDao.removeObject(affectationImprimante.getPk());
         log.info("Suppression de l'objet AffectationImprimante : " + affectationImprimante.toString());
      }else{
         log.warn("Suppression d'un AffectationImprimante null");
      }
   }
}
