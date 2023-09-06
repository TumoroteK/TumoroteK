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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.prelevement.RisqueDao;
import fr.aphp.tumorotek.dao.contexte.PlateformeDao;
import fr.aphp.tumorotek.manager.coeur.prelevement.RisqueManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.prelevement.RisqueValidator;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 *
 * Implémentation du manager du bean de domaine Risque.
 * Classe créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class RisqueManagerImpl implements RisqueManager
{

   private final Logger log = LoggerFactory.getLogger(RisqueManager.class);

   /* Beans injectes par Spring*/
   private RisqueDao risqueDao;

   private RisqueValidator risqueValidator;

   private PlateformeDao plateformeDao;

   private EntityManagerFactory entityManagerFactory;

   public RisqueManagerImpl(){}

   /* Properties setters */
   public void setRisqueDao(final RisqueDao rDao){
      this.risqueDao = rDao;
   }

   public void setRisqueValidator(final RisqueValidator rValidator){
      this.risqueValidator = rValidator;
   }

   public void setPlateformeDao(final PlateformeDao pfDao){
      this.plateformeDao = pfDao;
   }

   public void setEntityManagerFactory(final EntityManagerFactory eF){
      this.entityManagerFactory = eF;
   }

   @Override
   public void createObjectManager(final Risque obj){

      final Risque rs = obj;

      // On vérifie que la pf n'est pas null. Si c'est le cas on envoie
      // une exception
      if(rs.getPlateforme() == null){
         log.warn("Objet obligatoire Plateforme manquant lors de la creation d'un objet Risque");
         throw new RequiredObjectIsNullException("Risque", "creation", "Plateforme");
      }
      rs.setPlateforme(plateformeDao.mergeObject(rs.getPlateforme()));

      BeanValidator.validateObject(rs, new Validator[] {risqueValidator});
      if(!findDoublonManager(rs)){
         risqueDao.createObject(rs);
         log.info("Enregistrement objet Risque {}",  rs);
      }else{
         log.warn("Doublon lors creation objet Risque {}",  rs);
         throw new DoublonFoundException("Risque", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Risque obj){
      BeanValidator.validateObject(obj, new Validator[] {risqueValidator});
      if(!findDoublonManager(obj)){
         risqueDao.updateObject(obj);
         log.info("Modification objet Risque {}",  obj);
      }else{
         log.warn("Doublon lors modification objet Risque {}",  obj);
         throw new DoublonFoundException("Risque", "modification");
      }
   }

   @Override
   public List<Risque> findAllObjectsManager(){
      log.debug("Recherche totalite des Risque");
      return risqueDao.findAll();
   }

   @Override
   public List<Risque> findByNomLikeManager(String nom, final boolean exactMatch){
      if(!exactMatch){
         nom = nom + "%";
      }
      log.debug("Recherche Risque par nom: {} exactMatch {}", nom, exactMatch);
      return risqueDao.findByNom(nom);
   }

   @Override
   public List<Risque> findByInfectieuxManager(final Boolean infectieux){
      log.debug("Recherche Risque par infectiosite: {}",  infectieux);
      return risqueDao.findByInfectieux(infectieux);
   }

   @Override
   public void removeObjectManager(final Risque obj){
      if(obj != null){
         risqueDao.removeObject(obj.getId());
         log.info("Suppression objet Risque {}",  obj);
      }else{
         log.warn("Suppression d'un Risque null");
      }
   }

   @Override
   public boolean findDoublonManager(final Risque o){
      if(o != null){
         final Risque risque = o;
         if(risque.getId() == null){
            return risqueDao.findAll().contains(risque);
         }
         return risqueDao.findByExcludedId(risque.getId()).contains(risque);
      }
      return false;
   }

   @Override
   public boolean isUsedObjectManager(final Risque o){
      final Risque risque = risqueDao.mergeObject(o);
      return risque.getPrelevements().size() > 0;
   }

   @Override
   public List<Risque> findByOrderManager(final Plateforme pf){
      return risqueDao.findByPfOrder(pf);
   }

   @Override
   public List<Risque> findByPatientAndPlateformeManager(final Patient pat, final Plateforme plateforme){

      if(pat != null && plateforme != null){
         log.debug(
            "Recherche des risques associés au patient : " + pat.toString() + " dans la plateforme : " + plateforme.toString());

         final String q =
            "SELECT r FROM Risque r JOIN r.prelevements p " + " WHERE p.banque.plateforme = ? " + "AND p.maladie.patient = ?";

         final EntityManager em = SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
         final TypedQuery<Risque> query = em.createQuery(q, Risque.class);
         query.setParameter(1, plateforme);
         query.setParameter(2, pat);

         final List<Risque> results = query.getResultList();
         if(results.size() > 0){
            return results;
         }
      }
      return null;
   }

   @Override
   public Risque findByIdManager(final Integer id){
      return risqueDao.findById(id);
   }

   @Override
   public List<Risque> findByOrderManager(){
      return risqueDao.findByOrder();
   }
}