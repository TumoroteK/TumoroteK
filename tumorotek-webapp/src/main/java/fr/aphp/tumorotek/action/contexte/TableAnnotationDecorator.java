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
package fr.aphp.tumorotek.action.contexte;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.component.SmallObjDecorator;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;

public class TableAnnotationDecorator extends SmallObjDecorator
{

   public TableAnnotationDecorator(final TableAnnotation t){
      setObj(t);
   }

   /**
    * Decore la liste d'objets au plus simple.
    * @param liste d'objets à décorer
    * @return codes décorés.
    */
   public static List<SmallObjDecorator> decorateListe(final List<TableAnnotation> objs){
      final List<SmallObjDecorator> liste = new ArrayList<>();
      final Iterator<TableAnnotation> it = objs.iterator();
      TableAnnotation next;
      TableAnnotationDecorator deco;
      int i = 0;
      while(it.hasNext()){
         next = it.next();
         deco = new TableAnnotationDecorator(next);
         i++;
         deco.setOrdreInit(i);
         deco.setOrdre(i);
         liste.add(deco);
      }
      return liste;
   }

   public static List<TableAnnotation> undecorateListe(final List<SmallObjDecorator> decos){
      final List<TableAnnotation> res = new ArrayList<>();
      if(decos != null){
         for(int i = 0; i < decos.size(); i++){
            res.add((TableAnnotation) decos.get(i).getObj());
         }
      }
      return res;
   }

   @Override
   public Object getObjClone(){
      return ((TableAnnotation) getObj()).clone();
   }

   @Override
   public Integer getObjDbId(){
      return ((TableAnnotation) getObj()).getTableAnnotationId();
   }

   @Override
   public void syncOrdre(){}

   public String getNom(){
      return ((TableAnnotation) getObj()).getNom();
   }

}
