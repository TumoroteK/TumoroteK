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
package fr.aphp.tumorotek.manager.impl.coeur.patient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.coeur.patient.LienFamilialDao;
import fr.aphp.tumorotek.manager.coeur.patient.LienFamilialManager;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.ObjectUsedException;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.coeur.patient.LienFamilialValidator;
import fr.aphp.tumorotek.model.coeur.patient.LienFamilial;

/**
 *
 * Implémentation du manager du bean de domaine LienFamilial.
 * Classe créée le 13/10/09.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class LienFamilialManagerImpl implements LienFamilialManager
{

   private final Log log = LogFactory.getLog(LienFamilialManager.class);

   /* Beans injectes par Spring*/
   private LienFamilialDao lienFamilialDao;
   private LienFamilialValidator lienFamilialValidator;

   public LienFamilialManagerImpl(){}

   /* Properties setters */
   public void setLienFamilialDao(final LienFamilialDao lfDao){
      this.lienFamilialDao = lfDao;
   }

   public void setLienFamilialValidator(final LienFamilialValidator lfValidator){
      this.lienFamilialValidator = lfValidator;
   }

   @Override
   public void createObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {lienFamilialValidator});
      if(!findDoublonManager(obj)){
         final LienFamilial reciproque = new LienFamilial();
         createReciprocity((LienFamilial) obj, reciproque, true);
         lienFamilialDao.createObject((LienFamilial) obj);
         log.info("Enregistrement objet LienFamilial " + obj.toString() + " et son reciproque " + reciproque.toString());

      }else{
         log.warn("Doublon lors creation objet LienFamilial " + obj.toString());
         throw new DoublonFoundException("LienFamilial", "creation");
      }
   }

   @Override
   public void updateObjectManager(final Object obj){
      BeanValidator.validateObject(obj, new Validator[] {lienFamilialValidator});
      if(!findDoublonManager(obj)){
         final LienFamilial reciproque = ((LienFamilial) obj).getReciproque();
         createReciprocity((LienFamilial) obj, reciproque, false);
         lienFamilialDao.updateObject((LienFamilial) obj);
         log.info("Modification objet LienFamilial " + obj.toString() + " et son reciproque " + reciproque.toString());
      }else{
         log.warn("Doublon lors modification objet LienFamilial " + obj.toString());
         throw new DoublonFoundException("LienFamilial", "modification");
      }
   }

   @Override
   public List<LienFamilial> findAllObjectsManager(){
      log.debug("Recherche totalite des LienFamilial");
      return lienFamilialDao.findAll();
   }

   @Override
   public List<LienFamilial> findByNomLikeManager(String nom, final boolean exactMatch){
      if(!exactMatch){
         nom = nom + "%";
      }
      log.debug("Recherche LienFamilial par nom: " + nom + " exactMatch " + String.valueOf(exactMatch));
      return lienFamilialDao.findByNom(nom);
   }

   @Override
   public void removeObjectManager(final Object obj){
      if(obj != null){
         if(!isUsedObjectManager(obj)){
            lienFamilialDao.removeObject(((LienFamilial) obj).getLienFamilialId());
            log.debug("Suppression objet LienFamilial " + obj.toString() + " et son reciproque en cascade");
         }else{
            log.warn("Suppression objet LienFamilial " + obj.toString() + " impossible car est reference (par PatientLien)");
            throw new ObjectUsedException();
         }
      }else{
         log.warn("Suppression d'un LienFamilial null");
      }
   }

   @Override
   public boolean findDoublonManager(final Object o){
      if(((LienFamilial) o).getLienFamilialId() == null){
         return lienFamilialDao.findAll().contains(o);
      }else{
         return lienFamilialDao.findByExcludedId(((LienFamilial) o).getLienFamilialId()).contains(o);
      }
   }

   @Override
   public boolean isUsedObjectManager(final Object o){
      final LienFamilial lienFamilial = lienFamilialDao.mergeObject((LienFamilial) o);
      return ((lienFamilial.getPatientLiens().size() > 0) || (lienFamilial.getReciproque().getPatientLiens().size() > 0));
   }

   /**
    * Cree la reciprocite des champs entre deux objets LienFamilial.
    * Cree la liaison si elle n'existe pas deja.
    * @param obj
    * @param reciproque
    * @param boolean doCreateLink
    */
   private void createReciprocity(final LienFamilial obj, final LienFamilial reciproque, final boolean doCreateLink){
      reciproque.setNom(switchNomReciproque(obj.getNom()));
      if(obj.getAscendant() != null){
         reciproque.setAscendant(!obj.getAscendant());
      }else{
         reciproque.setAscendant(null);
      }
      if(doCreateLink){
         reciproque.setReciproque(obj);
         obj.setReciproque(reciproque);
      }
   }

   /**
    * Cree le nom du lien familial reciproque en inversant les deux
    * membres.
    * @param nom
    * @return nom reciproque
    */
   private String switchNomReciproque(final String nom){
      final Pattern p = Pattern.compile("([A-Za-z]+)-([A-Za-z]+)");
      final Matcher m = p.matcher(nom);
      m.matches(); // toujours TRUE car Validator en amont
      final String g1 = m.group(1);
      final String g2 = m.group(2);
      return g2 + "-" + g1;

   }

}
