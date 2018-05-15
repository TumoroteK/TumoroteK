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
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.interfacage.DossierExterneDao;
import fr.aphp.tumorotek.dao.interfacage.EmetteurDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.interfacage.BlocExterneManager;
import fr.aphp.tumorotek.manager.interfacage.DossierExterneManager;
import fr.aphp.tumorotek.manager.interfacage.ValeurExterneManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.interfacage.DossierExterneValidator;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

/**
 *
 * Implémentation du manager du bean de domaine DossierExterne.
 * Classe créée le 07/10/2011.
 *
 * @author Pierre Ventadour
 * @version 2.1
 *
 */
public class DossierExterneManagerImpl implements DossierExterneManager
{

   private final Log log = LogFactory.getLog(DossierExterneManager.class);

   private DossierExterneDao dossierExterneDao;
   private EmetteurDao emetteurDao;
   private BlocExterneManager blocExterneManager;
   private ValeurExterneManager valeurExterneManager;
   private DossierExterneValidator dossierExterneValidator;

   public void setDossierExterneDao(final DossierExterneDao dDao){
      this.dossierExterneDao = dDao;
   }

   public void setEmetteurDao(final EmetteurDao eDao){
      this.emetteurDao = eDao;
   }

   public void setBlocExterneManager(final BlocExterneManager bManager){
      this.blocExterneManager = bManager;
   }

   public void setValeurExterneManager(final ValeurExterneManager vManager){
      this.valeurExterneManager = vManager;
   }

   public void setDossierExterneValidator(final DossierExterneValidator dValidator){
      this.dossierExterneValidator = dValidator;
   }

   @Override
   public DossierExterne findByIdManager(final Integer dossierExterneId){
      return dossierExterneDao.findById(dossierExterneId);
   }

   @Override
   public List<DossierExterne> findAllObjectsManager(){
      log.debug("Recherche de tous les BlocExternes");
      return dossierExterneDao.findAll();
   }

   @Override
   public List<DossierExterne> findByEmetteurManager(final Emetteur emetteur){
      log.debug("Recherche de tous les DossierExternes d'un emetteur");
      if(emetteur != null){
         return dossierExterneDao.findByEmetteur(emetteur);
      }
      return new ArrayList<>();
   }

   @Override
   public List<DossierExterne> findByEmetteurAndIdentificationManager(final Emetteur emetteur, final String numero){
      log.debug("Recherche de tous les DossierExternes d'un emetteur " + "pour un numéro donné");
      if(emetteur != null && numero != null){
         return dossierExterneDao.findByEmetteurAndIdentification(emetteur, numero);
      }
      return new ArrayList<>();
   }

   @Override
   public List<DossierExterne> findByEmetteurInListAndIdentificationManager(final List<Emetteur> emetteurs,
      final String identification){

      final List<DossierExterne> dossiers = new ArrayList<>();

      if(emetteurs != null && emetteurs.size() > 0 && identification != null){
         dossiers.addAll(dossierExterneDao.findByEmetteurInListAndIdentification(emetteurs, identification));
      }
      return dossiers;
   }

   @Override
   public List<DossierExterne> findByIdentificationManager(final String numero){
      log.debug("Recherche de tous les DossierExternes pour un numéro");
      if(numero != null){
         return dossierExterneDao.findByIdentification(numero);
      }
      return new ArrayList<>();
   }

   @Override
   public boolean findDoublonManager(final DossierExterne dossierExterne){
      if(dossierExterne != null){
         return dossierExterneDao.findByEmetteur(dossierExterne.getEmetteur()).contains(dossierExterne);
      }
      return false;
   }

   @Override
   public void validateDossierExterneManager(final DossierExterne dossierExterne, final Emetteur emetteur,
      final List<BlocExterne> blocExternes, final Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes){
      // emetteur required
      if(emetteur == null){
         log.warn("Objet obligatoire Emetteur manquant" + " lors de la création d'un DossierExterne");
         throw new RequiredObjectIsNullException("DossierExterne", "creation", "Emetteur");
      }

      // validation du dossier
      BeanValidator.validateObject(dossierExterne, new Validator[] {dossierExterneValidator});

      // validation des blocs
      if(blocExternes != null){
         for(int i = 0; i < blocExternes.size(); i++){
            blocExterneManager.validateBlocExterneManager(blocExternes.get(i), dossierExterne);

            // validation des valeurs du bloc
            if(valeurExternes != null && valeurExternes.containsKey(blocExternes.get(i))){
               final List<ValeurExterne> listVals = valeurExternes.get(blocExternes.get(i));

               for(int j = 0; j < listVals.size(); j++){
                  valeurExterneManager.validateValeurExterneManager(listVals.get(j), blocExternes.get(i));
               }
            }
         }
      }
   }

   @Override
   public void createObjectManager(final DossierExterne dossierExterne, final Emetteur emetteur,
      final List<BlocExterne> blocExternes, final Hashtable<BlocExterne, List<ValeurExterne>> valeurExternes, final int max){
      // Validation du dossier
      validateDossierExterneManager(dossierExterne, emetteur, blocExternes, valeurExternes);

      dossierExterne.setEmetteur(emetteurDao.mergeObject(emetteur));

      if(findDoublonManager(dossierExterne)){
         removeObjectManager(
            dossierExterneDao.findByEmetteur(emetteur).get(dossierExterneDao.findByEmetteur(emetteur).indexOf(dossierExterne)));
      }

      dossierExterneDao.createObject(dossierExterne);
      log.info("Enregistrement de l'objet DossierExterne : " + dossierExterne.toString());

      // création des blocs et des valeurs
      if(blocExternes != null){
         for(int i = 0; i < blocExternes.size(); i++){
            List<ValeurExterne> valeurs = null;
            // validation des valeurs du bloc
            if(valeurExternes != null && valeurExternes.containsKey(blocExternes.get(i))){
               valeurs = valeurExternes.get(blocExternes.get(i));
            }
            blocExterneManager.createObjectManager(blocExternes.get(i), dossierExterne, valeurs);
         }
      }

      if(dossierExterneDao.findCountAll().get(0) > max){
         final List<DossierExterne> dos = dossierExterneDao.findFirst();
         if(!dos.isEmpty()){
            removeObjectManager(dos.get(0));
            log.debug("Suppression FIRST IN " + dos.get(0).getIdentificationDossier() + " pour maintenir la taille "
               + " de la table temporaire à " + max);
         }
      }
   }

   @Override
   public void removeObjectManager(final DossierExterne dossierExterne){
      if(dossierExterne != null && dossierExterne.getDossierExterneId() != null){

         // suppression des blocs
         final List<BlocExterne> blocs = blocExterneManager.findByDossierExterneManager(dossierExterne);
         for(int i = 0; i < blocs.size(); i++){
            blocExterneManager.removeObjectManager(blocs.get(i));
         }

         dossierExterneDao.removeObject(dossierExterne.getDossierExterneId());
         log.info("Suppression de l'objet DossierExterne : " + dossierExterne.toString());
      }else{
         log.warn("Suppression d'une DossierExterne null");
      }
   }
}
