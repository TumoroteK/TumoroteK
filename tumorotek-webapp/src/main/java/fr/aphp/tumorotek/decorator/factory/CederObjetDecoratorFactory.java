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
package fr.aphp.tumorotek.decorator.factory;

import java.util.ArrayList;
import java.util.List;

import fr.aphp.tumorotek.decorator.CederObjetDecorator;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.coeur.cession.CederObjet;

/**
 *
 * Factory CederObjetDecorator
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class CederObjetDecoratorFactory implements TKDecoratorFactory
{

   private boolean isAnonyme;

   // @since 2.1 
   // objects ids scan checked
   private List<Integer> checkedObjectIds = new ArrayList<>();

   public boolean isAnonyme(){
      return isAnonyme;
   }

   public void setAnonyme(final boolean a){
      this.isAnonyme = a;
   }

   public CederObjetDecoratorFactory(final boolean a, final List<Integer> _ids){
      isAnonyme = a;
      if(_ids != null){
         checkedObjectIds = _ids;
      }
   }

   /**
    * Decore une liste de cederobjets.
    * @param cederobjets
    * @return CederObjets décorées.
    */
   @Override
   public List<CederObjetDecorator> decorateListe(final List<? extends Object> objets){
      final List<CederObjetDecorator> liste = new ArrayList<>();
      for(final Object cO : objets){
         liste.add(new CederObjetDecorator((CederObjet) cO, false, isAnonyme(),
            checkedObjectIds.contains(((CederObjet) cO).getObjetId())));
      }
      return liste;
   }

   public List<TKStockableObject> undecorateListe(final List<CederObjetDecorator> decos){
      final List<TKStockableObject> stObjs = new ArrayList<>();
      for(final CederObjetDecorator cedeDeco : decos){
         stObjs.add(cedeDeco.getTKobj());
      }
      return stObjs;
   }

   public List<Integer> getCheckedObjectIds(){
      return checkedObjectIds;
   }
}
