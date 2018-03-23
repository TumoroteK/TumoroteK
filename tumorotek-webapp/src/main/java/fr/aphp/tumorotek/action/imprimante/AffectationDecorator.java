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
package fr.aphp.tumorotek.action.imprimante;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class AffectationDecorator
{

   private Utilisateur utilisateur;
   private Banque banque;
   private boolean isFirst;
   private boolean isLast;
   private Integer nbBanquesForUtilisateur;
   private boolean isEdit;

   public AffectationDecorator(){

   }

   public AffectationDecorator(final Utilisateur u, final Banque b, final boolean first, final boolean last, final Integer nb){
      utilisateur = u;
      banque = b;
      isFirst = first;
      isLast = last;
      nbBanquesForUtilisateur = nb;
   }

   public Utilisateur getUtilisateur(){
      return utilisateur;
   }

   public void setUtilisateur(final Utilisateur u){
      this.utilisateur = u;
   }

   public Banque getBanque(){
      return banque;
   }

   public void setBanque(final Banque b){
      this.banque = b;
   }

   public boolean isFirst(){
      return isFirst;
   }

   public void setFirst(final boolean i){
      this.isFirst = i;
   }

   public Integer getNbBanquesForUtilisateur(){
      return nbBanquesForUtilisateur;
   }

   public void setNbBanquesForUtilisateur(final Integer nb){
      this.nbBanquesForUtilisateur = nb;
   }

   public boolean isLast(){
      return isLast;
   }

   public void setLast(final boolean last){
      this.isLast = last;
   }

   public boolean isEdit(){
      return isEdit;
   }

   public void setEdit(final boolean isE){
      this.isEdit = isE;
   }
}
