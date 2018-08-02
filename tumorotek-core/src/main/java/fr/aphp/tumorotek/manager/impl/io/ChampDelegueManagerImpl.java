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
package fr.aphp.tumorotek.manager.impl.io;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import fr.aphp.tumorotek.dao.io.export.ChampDelegueDao;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.io.ChampDelegueManager;
import fr.aphp.tumorotek.model.TKDelegateObject;
import fr.aphp.tumorotek.model.TKDelegetableObject;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * @author Gille Chapelot
 *
 */
public class ChampDelegueManagerImpl implements ChampDelegueManager
{

   private ChampDelegueDao champDelegueDao;

   /* (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.io.ChampDelegueManager#findByEntiteAndContexte(fr.aphp.tumorotek.model.systeme.Entite, fr.aphp.tumorotek.model.contexte.EContexte)
    */
   @Override
   public List<ChampDelegue> findByEntiteAndContexte(Entite entite, EContexte contexte){
      return champDelegueDao.findByEntiteAndContexte(entite, contexte);
   }

   @Override
   public <T> Object getValueForEntite(ChampDelegue cd, TKDelegetableObject<T> entite){

      Object value = null;
      TKDelegateObject<T> delegate = entite.getDelegate();

      String nomChamp = StringUtils.uncapitalize(cd.getNom());
      
      if(null != delegate && PropertyUtils.isReadable(delegate, nomChamp)){
         try{
            value = PropertyUtils.getProperty(delegate, nomChamp);
         }catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            throw new TKException(
               "Propriété " + cd.getNom() + "  de " + delegate.getClass().getSimpleName() + " inconnue ou inaccessible");
         }
      }

      return value;

   }

   @Override
   public List<ChampDelegue> findByNomAndEntiteAndContexte(String nom, Entite entite, EContexte contexte){
      return champDelegueDao.findByNomAndEntiteAndContexte(nom, entite, contexte);
   }
   
   public void setChampDelegueDao(ChampDelegueDao champDelegueDao){
      this.champDelegueDao = champDelegueDao;
   }

}
