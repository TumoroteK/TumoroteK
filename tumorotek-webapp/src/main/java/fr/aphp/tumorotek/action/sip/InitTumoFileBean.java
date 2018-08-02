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
package fr.aphp.tumorotek.action.sip;

import java.io.Serializable;

/**
 * classe de mapping des variables passees dans le
 * res_initTumo.properties, pour acces dans objets Java.
 * @author Nico et Serro
 *
 */
public class InitTumoFileBean implements Serializable
{
   private static final long serialVersionUID = 3232887929072603527L;
   /* declaration des variables locales */
   private int maxPatients;
   private int longueurNip;
   private String sip;

   /* constructeur par defaut */
   public InitTumoFileBean(){
      this.maxPatients = 0;
      this.longueurNip = 0;
      this.sip = "";
   }

   /* constructeur value */
   public InitTumoFileBean(final int m, final int l, final String s){
      this.maxPatients = m;
      this.longueurNip = l;
      this.sip = s;
   }

   /* accesseurs */

   /* getters */
   public int getMaxPatients(){
      return this.maxPatients;
   }

   public int getLongueurNip(){
      return this.longueurNip;
   }

   public String getSip(){
      return this.sip;
   }

   /* setters */
   public void setMaxPatients(final int m){
      this.maxPatients = m;
   }

   public void setLongueurNip(final int l){
      this.longueurNip = l;
   }

   public void setSip(final String s){
      this.sip = s;
   }
}
