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

import fr.aphp.tumorotek.dao.systeme.CouleurDao;
import fr.aphp.tumorotek.manager.systeme.CouleurManager;
import fr.aphp.tumorotek.model.systeme.Couleur;

/**
 *
 * Implémentation du manager du bean de domaine Couleur.
 * Interface créée le 30/04/2010.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public class CouleurManagerImpl implements CouleurManager
{

   private final Log log = LogFactory.getLog(CouleurManager.class);

   /** Bean Dao CouleurDao. */
   private CouleurDao couleurDao;

   public void setCouleurDao(final CouleurDao cDao){
      this.couleurDao = cDao;
   }

   @Override
   public List<Couleur> findAllObjectsManager(){
      return couleurDao.findAll();
   }

   @Override
   public List<Couleur> findByCouleurLikeManager(String couleur, final boolean exactMatch){
      log.debug("Recherche Couleur par " + couleur + " exactMatch " + String.valueOf(exactMatch));
      if(couleur != null){
         if(!exactMatch){
            couleur = couleur + "%";
         }
         return couleurDao.findByCouleur(couleur);
      }else{
         return new ArrayList<>();
      }
   }

   @Override
   public Couleur findByIdManager(final Integer couleurId){
      return couleurDao.findById(couleurId);
   }

   @Override
   public List<Couleur> findByOrdreVisotubeManager(){
      return couleurDao.findByVisotube();
   }

}
