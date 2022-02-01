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
package fr.aphp.tumorotek.manager.impl.io.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.io.export.CritereDao;
import fr.aphp.tumorotek.dao.io.export.GroupementDao;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.exception.SearchedObjectIdNotExistException;
import fr.aphp.tumorotek.manager.io.export.CritereManager;
import fr.aphp.tumorotek.manager.io.export.GroupementManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.io.export.GroupementValidator;
import fr.aphp.tumorotek.model.io.export.Critere;
import fr.aphp.tumorotek.model.io.export.Groupement;

/**
 *
 * Implémentation du manager du bean de domaine Groupement. Classe créée le
 * 02/10/09.
 *
 * @author Maxime GOUSSEAU
 * @version 2.0
 *
 */
public class GroupementManagerImpl implements GroupementManager
{

   private final Log log = LogFactory.getLog(GroupementManager.class);

   /** Bean Dao GroupementDao. */
   private GroupementDao groupementDao = null;
   /** Bean Dao CritereDao. */
   private CritereDao critereDao = null;
   /** Bean Manager CritereManager. */
   private CritereManager critereManager = null;
   /** Bean Validator. */
   private GroupementValidator groupementValidator = null;

   public GroupementManagerImpl(){
      super();
   }

   /**
    * Setter du bean GroupementDao.
    * 
    * @param grDao
    *            est le bean Dao.
    */
   public void setGroupementDao(final GroupementDao grDao){
      this.groupementDao = grDao;
   }

   /**
    * Setter du bean CritereDao.
    * 
    * @param cDao
    *            est le bean Dao.
    */
   public void setCritereDao(final CritereDao cDao){
      this.critereDao = cDao;
   }

   /**
    * Setter du bean CritereManager.
    * 
    * @param critManager
    *            est le bean Manager.
    */
   public void setCritereManager(final CritereManager critManager){
      this.critereManager = critManager;
   }

   /**
    * Setter du bean GroupementValidator.
    * 
    * @param groupementValidator
    *            est le bean Validator.
    */
   public void setGroupementValidator(final GroupementValidator validator){
      this.groupementValidator = validator;
   }

   /**
    * Copie un Groupement en BDD.
    * 
    * @param groupement
    *            Groupement à copier.
    * @return le Groupement copié.
    */
   @Override
   public Groupement copyGroupementManager(final Groupement groupement){
      return copyGroupementWithParentManager(groupement, null);
   }

   /**
    * Créé un Groupement en BDD.
    * 
    * @param groupement
    *            Groupement à créer.
    * @param critere1
    *            Premier Critère du groupement.
    * @param critere2
    *            Deuxième Critère du groupement.
    * @param operateur
    *            Opérateur du groupement.
    * @param parent
    *            Groupement parent de la reqûete.
    */
   @Override
   public void createObjectManager(final Groupement groupement, Critere critere1, Critere critere2, final String operateur,
      Groupement parent){
      // On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors " + "de la création d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "création", "Groupement");
      }
      // On vérifie que l'opérateur n'est pas nul
      /*
       * if (operateur == null) {
       * log.warn("Objet obligatoire Operateur manquant lors " +
       * "de la création d'un objet Groupement"); throw new
       * RequiredObjectIsNullException( "Groupement", "création",
       * "Operateur"); }
       */
      // On enregsitre d'abord son parent
      if(parent != null){
         if(parent.getGroupementId() != null){
            parent = groupementDao.save(parent);
         }else{
        	 createObjectManager(parent, parent.getCritere1(), parent.getCritere2(), parent.getOperateur(), parent.getParent());
         }
      }
      groupement.setParent(parent);
      if(critere1 != null){
         if(critere1.getCritereId() != null){
            critere1 = critereDao.save(critere1);
         }else{
            critereManager.createObjectManager(critere1, critere1.getChamp(), critere1.getCombinaison());
         }
      }
      groupement.setCritere1(critere1);
      if(critere2 != null){
         if(critere2.getCritereId() != null){
            critere2 = critereDao.save(critere2);
         }else{
            critereManager.createObjectManager(critere2, critere2.getChamp(), critere2.getCombinaison());
         }
      }
      groupement.setCritere2(critere2);
      groupement.setOperateur(operateur);
      BeanValidator.validateObject(groupement, new Validator[] {groupementValidator});
      groupementDao.save(groupement);
   }

   /**
    * Met à jour un Groupement en BDD.
    * 
    * @param groupement
    *            Groupement à mettre à jour.
    * @param critere1
    *            Premier Critère du groupement.
    * @param critere2
    *            Deuxième Critère du groupement.
    * @param operateur
    *            Opérateur du groupement.
    * @param parent
    *            Groupement parent de la reqûete.
    */
   @Override
   public void updateObjectManager(final Groupement groupement, Critere critere1, Critere critere2, final String operateur,
      Groupement parent){
      //On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors " + "de la modification d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "modification", "Groupement");
      }
      // On met à jour d'abord son parent
      if(parent != null){
         if(parent.getGroupementId() != null){
            parent = groupementDao.save(parent);
         }else{
            createObjectManager(parent, parent.getCritere1(), parent.getCritere2(), parent.getOperateur(), parent.getParent());
         }
      }
      groupement.setParent(parent);
      if(critere1 != null){
         if(groupement.getCritere1() != null && groupement.getCritere1().getCritereId() != null
            && !groupement.getCritere1().getCritereId().equals(critere1.getCritereId())){
            critereDao.deleteById(groupement.getCritere1().getCritereId());
         }
         if(critere1.getCritereId() != null){
            critere1 = critereDao.save(critere1);
         }else{
            critereManager.createObjectManager(critere1, critere1.getChamp(), critere1.getCombinaison());
         }
      }
      groupement.setCritere1(critere1);
      if(critere2 != null){
         if(groupement.getCritere2() != null && groupement.getCritere2().getCritereId() != null
            && !groupement.getCritere2().getCritereId().equals(critere2.getCritereId())){
            critereDao.deleteById(groupement.getCritere2().getCritereId());
         }
         if(critere2.getCritereId() != null){
            critere2 = critereDao.save(critere2);
         }else{
            critereManager.createObjectManager(critere2, critere2.getChamp(), critere2.getCombinaison());
         }
      }
      groupement.setCritere2(critere2);
      groupement.setOperateur(operateur);
      BeanValidator.validateObject(groupement, new Validator[] {groupementValidator});
      groupementDao.save(groupement);
   }

   /**
    * Supprime un Groupement et ses enfants d'abord.
    * 
    * @param groupement
    *            Groupement à supprimer.
    */
   @Override
   public void removeObjectManager(final Groupement groupement){
      // On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors " + "de la suppression d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "suppression", "Groupement");
      }
      // On vérifie que le groupement est en BDD
      if(findByIdManager(groupement.getGroupementId()) == null){
         throw new SearchedObjectIdNotExistException("Groupement", groupement.getGroupementId());
      }
      // On supprime d'abord ses enfants
      final List<Groupement> enfants = groupementDao.findEnfants(groupement);
      final Iterator<Groupement> it = enfants.iterator();
      while(it.hasNext()){
         final Groupement temp = it.next();
         removeObjectManager(temp);
      }

      // On supprime les criteres
      final Critere oldCritere1 = groupement.getCritere1();
      final Critere oldCritere2 = groupement.getCritere2();
      // On supprime le groupement
      groupementDao.deleteById(groupement.getGroupementId());

      // On supprime les criteres
      if(oldCritere1 != null){
         critereManager.removeObjectManager(oldCritere1);
      }
      if(oldCritere2 != null){
         critereManager.removeObjectManager(oldCritere2);
      }
   }

   /**
    * Chercher les Groupements enfants du Groupement passé en paramètre.
    * 
    * @param groupement
    *            Groupement dont on souhaite obtenir la liste d'enfants.
    * @return liste des enfants (Groupements) d'un Groupement.
    */
   @Override
   public ArrayList<Groupement> findEnfantsManager(final Groupement groupement){
      // On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors " + "de la recherche d'enfants d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "recherche d'enfants", "Groupement");
      }
      return groupementDao.findEnfants(groupement);
   }

   /**
    * Recherche les Critères dont le Groupement racine est passé en paramètre.
    * 
    * @param groupementRacine
    *            Groupement racine dont on souhaite obtenir les Critères.
    * @return la liste de tous les Critères descendants du Groupement racine.
    */
   @Override
   public List<Critere> findCriteresManager(final Groupement groupementRacine){
      // On vérifie que le groupementRacine n'est pas nul
      if(groupementRacine == null){
         log.warn("Objet obligatoire Groupement manquant lors de la " + "recherche par de Criteres d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "recherche de Criteres", "Groupement");
      }
      final List<Critere> criteres = new ArrayList<>();
      if(groupementRacine.getCritere1() != null){
         criteres.add(groupementRacine.getCritere1());
      }
      if(groupementRacine.getCritere2() != null){
         criteres.add(groupementRacine.getCritere2());
      }
      final Iterator<Groupement> it = findEnfantsManager(groupementRacine).iterator();
      while(it.hasNext()){
         final Groupement temp = it.next();
         criteres.addAll(findCriteresManager(temp));
         if(temp.getCritere1() != null){
            criteres.add(temp.getCritere1());
         }
         if(temp.getCritere2() != null){
            criteres.add(temp.getCritere2());
         }
      }
      return criteres;
   }

   /**
    * Recherche un Groupement dont l'identifiant est passé en paramètre.
    * 
    * @param groupementId
    *            Identifiant du Groupement que l'on recherche.
    * @return un Groupement.
    */
   @Override
   public Groupement findByIdManager(final Integer id){
      // On vérifie que l'identifiant n'est pas nul
      if(id == null){
         log.warn("Objet obligatoire identifiant manquant lors de la " + "recherche par l'identifiant d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "recherche par identifiant", "identifiant");
      }
      return groupementDao.findById(id).orElse(null);
   }

   /**
    * Recherche tous les Groupements présents dans la BDD.
    * 
    * @return Liste de Groupements.
    */
   @Override
   public List<Groupement> findAllObjectsManager(){
      return IterableUtils.toList(groupementDao.findAll());
   }

   /**
    * Méthode qui permet de vérifier que 2 Groupements sont des copies (et que
    * leurs enfants aussi).
    * 
    * @param g
    *            Groupement premier Groupement à vérifier.
    * @param copie
    *            deuxième Groupement à vérifier.
    * @return true si les 2 Groupements sont des copies, false sinon.
    */
   @Override
   public Boolean isCopyManager(final Groupement g, final Groupement copie){
      if(copie == null){
         return false;
      }
      boolean ok = false;
      if(g.getOperateur() == null){
         if(copie.getOperateur() == null){
            if(g.getCritere1() == null){
               if(copie.getCritere1() == null){
                  if(g.getCritere2() == null){
                     ok = (copie.getCritere2() == null);
                  }else{
                     ok = (g.getCritere2().equals(copie.getCritere2()));
                  }
               }else{
                  ok = false;
               }
            }else if(g.getCritere1().equals(copie.getCritere1())){
               if(g.getCritere2() == null){
                  ok = (copie.getCritere2() == null);
               }else{
                  ok = (g.getCritere2().equals(copie.getCritere2()));
               }
            }else{
               ok = false;
            }
         }else{
            ok = false;
         }
      }else if(g.getOperateur().equals(copie.getOperateur())){
         if(g.getCritere1() == null){
            if(copie.getCritere1() == null){
               if(g.getCritere2() == null){
                  ok = (copie.getCritere2() == null);
               }else{
                  ok = (g.getCritere2().equals(copie.getCritere2()));
               }
            }else{
               ok = false;
            }
         }else if(g.getCritere1().equals(copie.getCritere1())){
            if(g.getCritere2() == null){
               ok = (copie.getCritere2() == null);
            }else{
               ok = (g.getCritere2().equals(copie.getCritere2()));
            }
         }else{
            ok = false;
         }
      }else{
         ok = false;
      }
      if(!ok){
         return false;
      }

      // On vérifie que les enfants sont des copies
      final Iterator<Groupement> it = findEnfantsManager(g).iterator();
      while(it.hasNext()){
         boolean found = false;
         final Groupement temp = it.next();
         final Iterator<Groupement> it2 = findEnfantsManager(g).iterator();
         while(it2.hasNext()){
            final Groupement copyTemp = it2.next();
            if(isCopyManager(temp, copyTemp)){
               found = true;
               break;
            }
         }
         if(!found){
            ok = false;
            break;
         }
      }
      return ok;
   }

   /**
    * Copie un Groupement en BDD.
    * 
    * @param groupement
    *            Groupement à copier.
    * @param parent
    *            Groupement parent de celui à copier.
    * @return le Groupement copié.
    */
   private Groupement copyGroupementWithParentManager(final Groupement groupement, final Groupement parent){
      Groupement temp = null;
      // On vérifie que le groupement n'est pas nul
      if(groupement == null){
         log.warn("Objet obligatoire Groupement manquant lors " + "de la copie d'un objet Groupement");
         throw new RequiredObjectIsNullException("Groupement", "copie", "Groupement");
      }
      BeanValidator.validateObject(groupement, new Validator[] {groupementValidator});
      // On copie les 2 criteres
      Critere critere1 = null;
      if(groupement.getCritere1() != null){
         critere1 = critereManager.copyCritereManager(groupement.getCritere1());
      }
      Critere critere2 = null;
      if(groupement.getCritere2() != null){
         critere2 = critereManager.copyCritereManager(groupement.getCritere2());
      }
      temp = new Groupement(critere1, critere2, groupement.getOperateur(), parent);
      BeanValidator.validateObject(temp, new Validator[] {groupementValidator});
      groupementDao.save(temp);

      // On copie ses enfants
      final Iterator<Groupement> it = findEnfantsManager(temp).iterator();
      while(it.hasNext()){
         final Groupement enfTemp = it.next();
         copyGroupementWithParentManager(enfTemp, temp);
      }
      return temp;
   }
}
