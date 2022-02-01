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
package fr.aphp.tumorotek.manager.impl.ui;

import java.util.List;

import org.springframework.validation.Validator;

import fr.aphp.tumorotek.dao.ui.UiRequeteDao;
import fr.aphp.tumorotek.manager.exception.DoublonFoundException;
import fr.aphp.tumorotek.manager.exception.RequiredObjectIsNullException;
import fr.aphp.tumorotek.manager.ui.UiRequeteManager;
import fr.aphp.tumorotek.manager.validation.BeanValidator;
import fr.aphp.tumorotek.manager.validation.ui.UiRequeteValidator;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.ui.UiCompValue;
import fr.aphp.tumorotek.model.ui.UiRequete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.11
 *
 */
public class UiRequeteManagerImpl implements UiRequeteManager
{

   private UiRequeteDao uiRequeteDao;
   private UiRequeteValidator uiRequeteValidator;

   public void setUiRequeteDao(final UiRequeteDao uDao){
      this.uiRequeteDao = uDao;
   }

   public void setUiRequeteValidator(final UiRequeteValidator uv){
      this.uiRequeteValidator = uv;
   }

   @Override
   public UiRequete findByIdManager(final Integer id){
      return uiRequeteDao.findById(id).orElse(null);
   }

   @Override
   public Boolean findDoublonManager(final UiRequete req){
      if(req != null){
         final List<UiRequete> reqs =
            uiRequeteDao.findByNomUtilisateurAndEntite(req.getUtilisateur(), req.getEntite(), req.getNom());
         if(!reqs.isEmpty()){
            for(final UiRequete uiR : reqs){
               if(!uiR.getUiRequeteId().equals(req.getUiRequeteId())){
                  return true;
               }
            }
         }
      }
      return false;
   }

   @Override
   public List<UiRequete> findByUtilisateurAndEntiteManager(final Utilisateur u, final Entite e){
      return uiRequeteDao.findByUtilisateurAndEntite(u, e);
   }

   @Override
   public void createObjectManager(final String nom, final Utilisateur ut, final Entite et, final Integer ordre,
      final List<UiCompValue> vals){
      final UiRequete requete = new UiRequete(null, ut, et, nom, ordre);
      mergeObjectManager(requete, vals);
   }

   @Override
   public void mergeObjectManager(final UiRequete requete, final List<UiCompValue> vals){
      if(requete != null){
         if(requete.getUtilisateur() == null){
            throw new RequiredObjectIsNullException("Utilisateur", "creation", "UiRequete");
         }else if(requete.getEntite() == null){
            throw new RequiredObjectIsNullException("Entite", "creation", "UiRequete");
         }else if(requete.getOrdre() == null){
            throw new RequiredObjectIsNullException("Ordre", "creation", "UiRequete");
         }

         if(vals != null){
            requete.getUiCompValues().clear();
            for(final UiCompValue uiVal : vals){
               uiVal.setUiRequete(requete);
               requete.getUiCompValues().add(uiVal);
            }
         }

         if(findDoublonManager(requete)){
            throw new DoublonFoundException("UiRequete", "creation");
         }else{
            BeanValidator.validateObject(requete, new Validator[] {uiRequeteValidator});
            if(requete.getUiRequeteId() == null){
               uiRequeteDao.save(requete);
            }else{
               uiRequeteDao.save(requete);
            }
         }
      }
   }

   @Override
   public void removeObjectManager(final UiRequete requete){
      if(requete != null){
         uiRequeteDao.deleteById(requete.getUiRequeteId());
      }
   }
}
