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
package fr.aphp.tumorotek.manager.impl.etiquettes;

import java.util.StringTokenizer;

/**
 * @author teamtumo
 *
 * Creation : 04/07/2006
 * Modification : 04/07/2006
 * gere le format des donnees a imprimer
 */
public final class PrinterFormat
{

   public PrinterFormat(){

   }

   public static String formaterHeure(final int heure, final int min){
      String heureStr = "";
      String minStr = "";
      if(heure > -1 && min > -1){
         if(heure < 10){
            heureStr = "0" + heure;
         }else{
            heureStr = "" + heure;
         }
         if(min < 10){
            minStr = "0" + min;
         }else{
            minStr = "" + min;
         }
         heureStr = heureStr + ":" + minStr;
      }

      return heureStr;
   }

   public static String formaterDelai(final float delai){
      String delaiStr = "";
      String heureDelaiStr = "";
      String minDelaiStr = "";
      if(delai > -1){
         final int delaiRound = Math.round(delai);
         if(delaiRound > 59){
            final int heureDelai = delaiRound / 60;
            final int minDelai = delaiRound - (heureDelai * 60);
            heureDelaiStr = heureDelai + " h ";
            minDelaiStr = minDelai + " min";
         }else{
            minDelaiStr = delaiRound + " min";
         }
         delaiStr = heureDelaiStr + minDelaiStr;
      }

      return delaiStr;
   }

   public static String formaterNom(final String nom){
      String nomFormate = "";
      int debut = 0;
      int fin = 0;
      final int lengthNom = nom.length();

      if(lengthNom > 0){
         debut = 1;
      }
      if(lengthNom > 4){
         fin = 4;
      }else{
         fin = lengthNom;
      }

      nomFormate = nom.substring(debut, fin);

      String complete = "";
      for(int i = nomFormate.length(); i < 3; i++){
         complete += "-";
      }

      nomFormate = nomFormate + complete;

      return nomFormate;
   }

   public static String extraireCodePrelevement(final String codeEchantillon){
      String codePrelevement = "";
      final StringTokenizer str = new StringTokenizer(codeEchantillon, ".");

      codePrelevement = str.nextToken();

      return codePrelevement;
   }

   public static String extraireNumeroTube(final String codeEchantillon){
      String codePrelevement = "";
      String numeroTube = "";
      final StringTokenizer str = new StringTokenizer(codeEchantillon, ".");

      codePrelevement = str.nextToken();
      numeroTube = codeEchantillon.replaceFirst(codePrelevement, "");

      return numeroTube;
   }

   public static String formaterUnite(final String unite){
      return unite.replaceAll("&micro;", "µ");
   }

}
