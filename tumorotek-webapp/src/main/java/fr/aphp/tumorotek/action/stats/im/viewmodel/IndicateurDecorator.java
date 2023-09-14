/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2016)
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
package fr.aphp.tumorotek.action.stats.im.viewmodel;

import java.util.Objects;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.model.stats.Indicateur;

/**
 * Decorateur du nom d'un indicateur
 * qui peut être internationalisé ou pas
 * @version 2.2.3-genno
 *
 * @author Mathieu Barthelemy
 *
 */
public class IndicateurDecorator
{

   private Indicateur indicateur;

   IndicateurDecorator(final Indicateur _i){
      this.indicateur = _i;
   }

   public Indicateur getIndicateur(){
      return indicateur;
   }

   public void setIndicateur(final Indicateur _i){
      this.indicateur = _i;
   }

   public String getNom(){
      if(indicateur != null){
         final String iln = Labels.getLabel("Indicateur.".concat(indicateur.getNom()));
         if(iln != null){
            return iln;
         }
         return indicateur.getNom();
      }
      return null;
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((getIndicateur() == null) ? 0 : getIndicateur().hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj){
      if(obj == this){
         return true;
      }
      if(obj == null || obj.getClass() != this.getClass()){
         return false;
      }

      final IndicateurDecorator deco = (IndicateurDecorator) obj;

      return (Objects.equals(getIndicateur(), deco.getIndicateur()));
   }
}
