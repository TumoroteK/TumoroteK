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
package fr.aphp.tumorotek.manager.exception;

import fr.aphp.tumorotek.model.TKStockableObject;

/**
 * Exception lancée lorsque un TKStockableObject est associé à
 * plusieurs évènements de stockage incomplets (dateRetour is null).
 * Un seul évènement incomplet est accepté. L'exception renvoyée doit
 * alerter les utilisateurs afin de corriger les données.
 *
 * Classe créée le 07/11/13.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class ManyIncompleteRetourException extends TKException
{

   private static final long serialVersionUID = -6748880359935549123L;

   private TKStockableObject obj;

   public TKStockableObject getObj(){
      return obj;
   }

   public void setObj(final TKStockableObject obj){
      this.obj = obj;
   }

   public ManyIncompleteRetourException(){
      super();
   }

   public ManyIncompleteRetourException(final TKStockableObject o){
      super();
      this.obj = o;
   }

   @Override
   public String getMessage(){
      return this.obj.getCode() + " : plusieurs évènements de stockage incomplets";
   }
}
