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
package fr.aphp.tumorotek.action.thesaurus;

import fr.aphp.tumorotek.action.constraints.ConstWord;

/**
 * Utility class fournissant les contraintes qui seront appliquées dans
 * l'interface par zk.
 * Date: 12/10/2010.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 */
public final class ThesaurusConstraints
{

   private ThesaurusConstraints(){}

   private static ConstWord natureConstraint = new ConstWord();
   static{
      natureConstraint.setNullable(false);
      natureConstraint.setSize(200);
   }

   private static ConstWord prelevementTypeConstraint = new ConstWord();
   static{
      prelevementTypeConstraint.setNullable(false);
      prelevementTypeConstraint.setSize(200);
   }

   private static ConstWord echantillonTypeConstraint = new ConstWord();
   static{
      echantillonTypeConstraint.setNullable(false);
      echantillonTypeConstraint.setSize(200);
   }

   private static ConstWord echanQualiteConstraint = new ConstWord();
   static{
      echanQualiteConstraint.setNullable(false);
      echanQualiteConstraint.setSize(200);
   }

   private static ConstWord prodTypeConstraint = new ConstWord();
   static{
      prodTypeConstraint.setNullable(false);
      prodTypeConstraint.setSize(200);
   }

   private static ConstWord prodQualiteConstraint = new ConstWord();
   static{
      prodQualiteConstraint.setNullable(false);
      prodQualiteConstraint.setSize(200);
   }

   private static ConstWord conditMilieurConstraint = new ConstWord();
   static{
      conditMilieurConstraint.setNullable(false);
      conditMilieurConstraint.setSize(200);
   }

   private static ConstWord conditTypeConstraint = new ConstWord();
   static{
      conditTypeConstraint.setNullable(false);
      conditTypeConstraint.setSize(200);
   }

   private static ConstWord consentTypeConstraint = new ConstWord();
   static{
      consentTypeConstraint.setNullable(false);
      consentTypeConstraint.setSize(200);
   }

   private static ConstWord modePrepaConstraint = new ConstWord();
   static{
      modePrepaConstraint.setNullable(false);
      modePrepaConstraint.setSize(200);
   }

   private static ConstWord cessionExamenConstraint = new ConstWord();
   static{
      cessionExamenConstraint.setNullable(false);
      cessionExamenConstraint.setSize(200);
   }

   private static ConstWord destructionMotifConstraint = new ConstWord();
   static{
      destructionMotifConstraint.setNullable(false);
      destructionMotifConstraint.setSize(200);
   }

   private static ConstWord protocoleTypeConstraint = new ConstWord();
   static{
      protocoleTypeConstraint.setNullable(false);
      protocoleTypeConstraint.setSize(200);
   }

   private static ConstWord protocoleConstraint = new ConstWord();
   static{
      protocoleConstraint.setNullable(false);
      protocoleConstraint.setSize(200);
   }

   private static ConstWord categorieConstraint = new ConstWord();
   static{
      categorieConstraint.setNullable(false);
      categorieConstraint.setSize(200);
   }

   private static ConstWord specialiteConstraint = new ConstWord();
   static{
      specialiteConstraint.setNullable(false);
      specialiteConstraint.setSize(200);
   }

   private static ConstWord conteneurTypeConstraint = new ConstWord();
   static{
      conteneurTypeConstraint.setNullable(false);
      conteneurTypeConstraint.setSize(200);
   }

   private static ConstWord enceinteTypeConstraint = new ConstWord();
   static{
      enceinteTypeConstraint.setNullable(false);
      enceinteTypeConstraint.setSize(200);
   }

   private static ConstWord enceinteTypePrefixeConstraint = new ConstWord();
   static{
      enceinteTypePrefixeConstraint.setNullable(false);
      enceinteTypePrefixeConstraint.setSize(200);
   }

   private static ConstWord risqueConstraint = new ConstWord();
   static{
      risqueConstraint.setNullable(false);
      risqueConstraint.setSize(200);
   }

   private static ConstWord nonConformiteConstraint = new ConstWord();
   static{
      nonConformiteConstraint.setNullable(false);
      nonConformiteConstraint.setSize(200);
   }

   public static ConstWord getNatureConstraint(){
      return natureConstraint;
   }

   public static ConstWord getPrelevementTypeConstraint(){
      return prelevementTypeConstraint;
   }

   public static ConstWord getEchantillonTypeConstraint(){
      return echantillonTypeConstraint;
   }

   public static ConstWord getEchanQualiteConstraint(){
      return echanQualiteConstraint;
   }

   public static ConstWord getProdTypeConstraint(){
      return prodTypeConstraint;
   }

   public static ConstWord getProdQualiteConstraint(){
      return prodQualiteConstraint;
   }

   public static ConstWord getConditMilieurConstraint(){
      return conditMilieurConstraint;
   }

   public static ConstWord getConditTypeConstraint(){
      return conditTypeConstraint;
   }

   public static ConstWord getConsentTypeConstraint(){
      return consentTypeConstraint;
   }

   public static ConstWord getModePrepaConstraint(){
      return modePrepaConstraint;
   }

   public static ConstWord getCessionExamenConstraint(){
      return cessionExamenConstraint;
   }

   public static ConstWord getDestructionMotifConstraint(){
      return destructionMotifConstraint;
   }

   public static ConstWord getProtocoleTypeConstraint(){
      return protocoleTypeConstraint;
   }

   public static ConstWord getCategorieConstraint(){
      return categorieConstraint;
   }

   public static ConstWord getSpecialiteConstraint(){
      return specialiteConstraint;
   }

   public static ConstWord getConteneurTypeConstraint(){
      return conteneurTypeConstraint;
   }

   public static ConstWord getEnceinteTypeConstraint(){
      return enceinteTypeConstraint;
   }

   public static ConstWord getEnceinteTypePrefixeConstraint(){
      return enceinteTypePrefixeConstraint;
   }

   public static ConstWord getRisqueConstraint(){
      return risqueConstraint;
   }

   public static ConstWord getNonConformiteConstraint(){
      return nonConformiteConstraint;
   }

   public static ConstWord getProtocoleConstraint(){
      return protocoleConstraint;
   }

}
