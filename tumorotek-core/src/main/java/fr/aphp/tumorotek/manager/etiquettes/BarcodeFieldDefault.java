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
package fr.aphp.tumorotek.manager.etiquettes;

/**
 * Bean non persistant de transport des paramètres ZPL Barcode field defaut.
 * @see instructions BY p147 ZPL II Programming guide
 * Le problème est que "Once a ^BY command is entered into a label format, it stays
 * in effect until another ^BY command is encountered". Donc une impression par TK peux
 * affecter une autre impression en contenant pas BY (cf Databiotec)
 *
 * Date: 23/09/2015
 *
 * @author Mathieu BARTHELEMY
 * @since 2.0.12
 */
public class BarcodeFieldDefault
{

   private Float moduleWidth = new Float(1.0);
   private Float widthRatio = new Float(3.0);
   private Integer barCodeHeight = 10;

   public BarcodeFieldDefault(){}

   public BarcodeFieldDefault(final Float w, final Float r, final Integer h){
      moduleWidth = w;
      widthRatio = r;
      barCodeHeight = h;
   }

   public Float getModuleWidth(){
      return moduleWidth;
   }

   public void setModuleWidth(final Float m){
      this.moduleWidth = m;
   }

   public Float getWidthRatio(){
      return widthRatio;
   }

   public void setWidthRatio(final Float w){
      this.widthRatio = w;
   }

   public Integer getBarCodeHeight(){
      return barCodeHeight;
   }

   public void setBarCodeHeight(final Integer b){
      this.barCodeHeight = b;
   }
}
