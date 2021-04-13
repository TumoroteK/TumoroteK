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
package fr.aphp.tumorotek.action.stockage;

import fr.aphp.tumorotek.action.constraints.ConstText;
import fr.aphp.tumorotek.action.constraints.ConstWord;
import fr.aphp.tumorotek.action.constraints.ConstWordNoPunct;

/**
 * Utility class fournissant les contraintes qui seront appliquées dans
 * l'interface par zk sur les éléments de stockage.
 * Date: 15/09/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 */
public final class StockageConstraints
{

   private StockageConstraints(){}

   private static ConstWordNoPunct codeConstraint = new ConstWordNoPunct();
   static{
      codeConstraint.setNullable(false);
      codeConstraint.setSize(5);
   }

   private static ConstWord nomConstraint = new ConstWord();
   static{
      nomConstraint.setNullable(false);
      nomConstraint.setSize(50);
   }

   private static ConstWord incidentConstraint = new ConstWord();
   static{
      incidentConstraint.setNullable(false);
      incidentConstraint.setSize(50);
   }

   private static ConstWordNoPunct enceinteNomConstraint = new ConstWordNoPunct();
   static{
      enceinteNomConstraint.setNullable(false);
      enceinteNomConstraint.setSize(10);
   }

   private static ConstWord nomNullConstraint = new ConstWord();
   static{
      nomNullConstraint.setNullable(true);
      nomNullConstraint.setSize(50);
   }

   private static ConstText descConstraint = new ConstText();
   static{
      descConstraint.setNullable(true);
      descConstraint.setSize(250);
   }

   private static ConstWord pieceConstraint = new ConstWord();
   static{
      pieceConstraint.setNullable(true);
      pieceConstraint.setSize(10);
   }

   private static ConstWordNoPunct terminaleNomConstraint = new ConstWordNoPunct();
   static{
      terminaleNomConstraint.setNullable(false);
      terminaleNomConstraint.setSize(50); // 2.2.1-IRELEC
   }

   public static ConstWordNoPunct getCodeConstraint(){
      return codeConstraint;
   }

   public static ConstWord getNomNullConstraint(){
      return nomNullConstraint;
   }

   public static ConstWord getPieceConstraint(){
      return pieceConstraint;
   }

   public static ConstWord getIncidentConstraint(){
      return incidentConstraint;
   }

   public static ConstWordNoPunct getEnceinteNomConstraint(){
      return enceinteNomConstraint;
   }

   public static ConstWordNoPunct getTerminaleNomConstraint(){
      return terminaleNomConstraint;
   }

   public static ConstWord getNomConstraint(){
      return nomConstraint;
   }

   public static ConstText getDescConstraint(){
      return descConstraint;
   }
}
