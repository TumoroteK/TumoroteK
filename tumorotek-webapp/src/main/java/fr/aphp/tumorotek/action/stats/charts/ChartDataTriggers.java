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
package fr.aphp.tumorotek.action.stats.charts;

public class ChartDataTriggers
{
   Boolean patientPfModelTrg;
   Boolean prelevementPfModelTrg;
   Boolean echanPfModelTrg;
   Boolean derivePfModelTrg;
   Boolean cessionPfModelTrg;

   Boolean patientCollModelTrg;
   Boolean prelevementCollModelTrg;
   Boolean echanCollModelTrg;
   Boolean deriveCollModelTrg;
   Boolean cessionCollModelTrg;

   Boolean prelTypeCollModelTrg;
   Boolean prelEtabCollModelTrg;
   Boolean prelConsentCollModelTrg;

   Boolean echanTypeCollModelTrg;
   Boolean echanCimCollModelTrg;
   Boolean echanOrgCollModelTrg;

   Boolean deriveTypeCollModelTrg;

   Boolean echansCedesCollModelTrg;
   //
   Boolean derivesCedesCollModelTrg;
   Boolean cessionTypeCollModelTrg;

   public Boolean getPatientPfModelTrg(){
      return patientPfModelTrg;
   }

   public Boolean getPrelevementPfModelTrg(){
      return prelevementPfModelTrg;
   }

   public Boolean getEchanPfModelTrg(){
      return echanPfModelTrg;
   }

   public Boolean getDerivePfModelTrg(){
      return derivePfModelTrg;
   }

   public Boolean getCessionPfModelTrg(){
      return cessionPfModelTrg;
   }

   public void setPatientPfModelTrg(final Boolean patientPfModelTrg){
      this.patientPfModelTrg = patientPfModelTrg;
   }

   public void setPrelevementPfModelTrg(final Boolean prelevementPfModelTrg){
      this.prelevementPfModelTrg = prelevementPfModelTrg;
   }

   public void setEchanPfModelTrg(final Boolean echanPfModelTrg){
      this.echanPfModelTrg = echanPfModelTrg;
   }

   public void setDerivePfModelTrg(final Boolean derivePfModelTrg){
      this.derivePfModelTrg = derivePfModelTrg;
   }

   public void setCessionPfModelTrg(final Boolean cessionPfModelTrg){
      this.cessionPfModelTrg = cessionPfModelTrg;
   }

   public Boolean getPatientCollModelTrg(){
      return patientCollModelTrg;
   }

   public Boolean getPrelevementCollModelTrg(){
      return prelevementCollModelTrg;
   }

   public Boolean getEchanCollModelTrg(){
      return echanCollModelTrg;
   }

   public Boolean getDeriveCollModelTrg(){
      return deriveCollModelTrg;
   }

   public Boolean getCessionCollModelTrg(){
      return cessionCollModelTrg;
   }

   public Boolean getEchansCedesCollModelTrg(){
      return echansCedesCollModelTrg;
   }

   //
   public Boolean getDerivesCedesCollModelTrg(){
      return derivesCedesCollModelTrg;
   }

   public Boolean getPrelTypeCollModelTrg(){
      return prelTypeCollModelTrg;
   }

   public Boolean getPrelEtabCollModelTrg(){
      return prelEtabCollModelTrg;
   }

   public Boolean getPrelConsentCollModelTrg(){
      return prelConsentCollModelTrg;
   }

   public Boolean getEchanTypeCollModelTrg(){
      return echanTypeCollModelTrg;
   }

   public Boolean getEchanCimCollModelTrg(){
      return echanCimCollModelTrg;
   }

   public Boolean getEchanOrgCollModelTrg(){
      return echanOrgCollModelTrg;
   }

   public Boolean getDeriveTypeCollModelTrg(){
      return deriveTypeCollModelTrg;
   }

   public Boolean getCessionTypeCollModelTrg(){
      return cessionTypeCollModelTrg;
   }

   public void setPatientCollModelTrg(final Boolean patientCollModelTrg){
      this.patientCollModelTrg = patientCollModelTrg;
   }

   public void setPrelevementCollModelTrg(final Boolean prelevementCollModelTrg){
      this.prelevementCollModelTrg = prelevementCollModelTrg;
   }

   public void setEchanCollModelTrg(final Boolean echanCollModelTrg){
      this.echanCollModelTrg = echanCollModelTrg;
   }

   public void setDeriveCollModelTrg(final Boolean deriveCollModelTrg){
      this.deriveCollModelTrg = deriveCollModelTrg;
   }

   public void setCessionCollModelTrg(final Boolean cessionCollModelTrg){
      this.cessionCollModelTrg = cessionCollModelTrg;
   }

   public void setEchansCedesCollModelTrg(final Boolean echansCedesCollModelTrg){
      this.echansCedesCollModelTrg = echansCedesCollModelTrg;
   }

   //
   public void setDerivesCedesCollModelTrg(final Boolean derivesCedesCollModelTrg){
      this.derivesCedesCollModelTrg = derivesCedesCollModelTrg;
   }

   public void setPrelTypeCollModelTrg(final Boolean prelTypeCollModelTrg){
      this.prelTypeCollModelTrg = prelTypeCollModelTrg;
   }

   public void setPrelEtabCollModelTrg(final Boolean prelEtabCollModelTrg){
      this.prelEtabCollModelTrg = prelEtabCollModelTrg;
   }

   public void setPrelConsentCollModelTrg(final Boolean prelConsentCollModelTrg){
      this.prelConsentCollModelTrg = prelConsentCollModelTrg;
   }

   public void setEchanTypeCollModelTrg(final Boolean echanTypeCollModelTrg){
      this.echanTypeCollModelTrg = echanTypeCollModelTrg;
   }

   public void setEchanCimCollModelTrg(final Boolean echanCimCollModelTrg){
      this.echanCimCollModelTrg = echanCimCollModelTrg;
   }

   public void setEchanOrgCollModelTrg(final Boolean echanOrgCollModelTrg){
      this.echanOrgCollModelTrg = echanOrgCollModelTrg;
   }

   public void setDeriveTypeCollModelTrg(final Boolean deriveTypeCollModelTrg){
      this.deriveTypeCollModelTrg = deriveTypeCollModelTrg;
   }

   public void setCessionTypeCollModelTrg(final Boolean cessionTypeCollModelTrg){
      this.cessionTypeCollModelTrg = cessionTypeCollModelTrg;
   }

   //	GraphesModele patientCollModel;
   //	GraphesModele prelevementCollModel;
   //	GraphesModele echanCollModel;
   //	GraphesModele deriveCollModel;
   //	GraphesModele cessionCollModel;
   //	
   //	GraphesModele echansCedesCollModel;
   //	GraphesModele derivesCedesCollModel;
   //
   //	GraphesModele prelTypeCollModel;
   //	GraphesModele prelEtabCollModel;
   //	GraphesModele prelConsentCollModel;
   //
   //	GraphesModele echanTypeCollModel;
   //	GraphesModele echanCimCollModel;
   //	GraphesModele echanOrgCollModel;
   //
   //	GraphesModele deriveTypeCollModel;
   //
   //	GraphesModele cessionTypeCollModel;
}
