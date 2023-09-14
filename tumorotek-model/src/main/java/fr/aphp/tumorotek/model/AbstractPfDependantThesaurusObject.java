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
package fr.aphp.tumorotek.model;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import fr.aphp.tumorotek.model.contexte.Plateforme;

/**
 * Classe représentant un item d'un thésaurus dépendant de la plateforme
 * @author 7009917
 *
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractPfDependantThesaurusObject extends AbstractThesaurusObject implements PfDependantObject
{

   private Plateforme plateforme;

   @Override
   @ManyToOne
   @JoinColumn(name = "PLATEFORME_ID", nullable = false)
   public Plateforme getPlateforme(){
      return this.plateforme;
   }

   @Override
   public void setPlateforme(final Plateforme pf){
      this.plateforme = pf;
   }

   @Override
   public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.getNom() == null) ? 0 : this.getNom().hashCode());
      result = prime * result + ((plateforme == null) ? 0 : plateforme.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj){
      if(this == obj){
         return true;
      }
      if((obj == null) || (getClass() != obj.getClass())){
         return false;
      }
      final AbstractPfDependantThesaurusObject other = (AbstractPfDependantThesaurusObject) obj;
      if(this.getNom() == null){
         if(other.getNom() != null){
            return false;
         }
      }else if(!this.getNom().equals(other.getNom())){
         return false;
      }
      if(plateforme == null){
         if(other.getPlateforme() != null){
            return false;
         }
      }else if(!plateforme.equals(other.getPlateforme())){
         return false;
      }
      return true;
   }

}
