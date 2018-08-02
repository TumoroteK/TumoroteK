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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.contexte.BanqueDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dao.systeme.NumerotationDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.systeme.NumerotationManager;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.systeme.Numerotation;

/**
 *
 * Implémentation du manager du bean de domaine Numerotation.
 * Interface créée le 18/01/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class NumerotationManagerImpl implements NumerotationManager
{

   private final Log log = LogFactory.getLog(NumerotationManager.class);
   /** Bean Dao. */
   private NumerotationDao numerotationDao;
   private BanqueDao banqueDao;
   private EntiteDao entiteDao;

   public void setNumerotationDao(final NumerotationDao nDao){
      this.numerotationDao = nDao;
   }

   public void setBanqueDao(final BanqueDao bDao){
      this.banqueDao = bDao;
   }

   public void setEntiteDao(final EntiteDao eDao){
      this.entiteDao = eDao;
   }

   @Override
   public Numerotation findByIdManager(final Integer numerotationId){
      return numerotationDao.findById(numerotationId);
   }

   @Override
   public List<Numerotation> findAllObjectsManager(){
      return numerotationDao.findAll();
   }

   @Override
   public List<Numerotation> findByBanqueAndEntiteManager(final Banque banque, final Entite entite){
      if(banque != null && entite != null){
         return numerotationDao.findByBanqueAndEntite(banque, entite);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Entite> findByBanqueSelectEntiteManager(final Banque banque){
      if(banque != null){
         return numerotationDao.findByBanqueSelectEntite(banque);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public List<Numerotation> findByBanquesManager(final List<Banque> banques){
      if(banques != null && banques.size() > 0){
         return numerotationDao.findByBanques(banques);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Boolean findDoublonManager(final Numerotation numerotation){
      if(numerotation != null){
         if(numerotation.getNumerotationId() == null){
            return numerotationDao.findAll().contains(numerotation);
         }else{
            return numerotationDao.findByExcludedId(numerotation.getNumerotationId()).contains(numerotation);
         }
      }else{
         return false;
      }
   }

   @Override
   public String getGeneratedCodeManager(final Numerotation numerotation){
      if(numerotation != null && numerotation.getCodeFormula() != null){
         if(numerotation.getCurrentIncrement() != null){
            String num = String.valueOf(numerotation.getCurrentIncrement());
            if(numerotation.getZeroFill() != null && numerotation.getZeroFill()){
               int indx = 0;
               if(numerotation.getNbChiffres() != null && numerotation.getNbChiffres() > num.length()){
                  indx = numerotation.getNbChiffres() - num.length();
               }
               final StringBuffer sb = new StringBuffer();
               for(int i = 0; i < indx; i++){
                  sb.append("0");
               }
               sb.append(num);
               num = sb.toString();
            }
            return numerotation.getCodeFormula().replace("[]", num);
         }else{
            return numerotation.getCodeFormula();
         }
      }else{
         return null;
      }
   }

   @Override
   public void createObjectManager(final Numerotation numerotation, final Banque banque, final Entite entite){
      //Banque required
      if(banque != null){
         numerotation.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la création d'une Numerotation");
         throw new RequiredObjectIsNullException("Numerotation", "creation", "Banque");
      }

      //Entite required
      if(entite != null){
         numerotation.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la création d'une Numerotation");
         throw new RequiredObjectIsNullException("Numerotation", "creation", "Entite");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(numerotation)){
         log.warn("Doublon lors de la creation de l'objet Numerotation : " + numerotation.toString());
         throw new DoublonFoundException("Numerotation", "creation");
      }else{

         numerotationDao.createObject(numerotation);

         log.info("Enregistrement de l'objet Numerotation : " + numerotation.toString());
      }
   }

   @Override
   public void updateObjectManager(final Numerotation numerotation, final Banque banque, final Entite entite){
      //Banque required
      if(banque != null){
         numerotation.setBanque(banqueDao.mergeObject(banque));
      }else{
         log.warn("Objet obligatoire Banque manquant" + " lors de la modification d'une Numerotation");
         throw new RequiredObjectIsNullException("Numerotation", "modification", "Banque");
      }

      //Entite required
      if(entite != null){
         numerotation.setEntite(entiteDao.mergeObject(entite));
      }else{
         log.warn("Objet obligatoire Entite manquant" + " lors de la modification d'une Numerotation");
         throw new RequiredObjectIsNullException("Numerotation", "modification", "Entite");
      }

      // Test s'il y a des doublons
      if(findDoublonManager(numerotation)){
         log.warn("Doublon lors de la modification de " + "l'objet Numerotation : " + numerotation.toString());
         throw new DoublonFoundException("Numerotation", "modification");
      }else{

         numerotationDao.updateObject(numerotation);

         log.info("Enregistrement de l'objet Numerotation : " + numerotation.toString());
      }
   }

   @Override
   public void removeObjectManager(final Numerotation numerotation){
      numerotationDao.removeObject(numerotation.getNumerotationId());
      log.info("Suppression de l'objet Numerotation : " + numerotation.toString());
   }
}
