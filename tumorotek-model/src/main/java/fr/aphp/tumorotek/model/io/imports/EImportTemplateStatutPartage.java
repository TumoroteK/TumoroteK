/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.io.imports;

public enum EImportTemplateStatutPartage
{
   JAMAIS_PARTAGE(1, "JAMAIS_PARTAGE"),
   PARTAGE_ENCOURS(2, "PARTAGE_ENCOURS"),
   PARTAGE_SUPPRIME(3, "PARTAGE_SUPPRIME");

   private Integer importTemplateStatutPartageCode;

   private String valeur;

   EImportTemplateStatutPartage(Integer importTemplateStatutPartageCode, String valeur){
      this.importTemplateStatutPartageCode = importTemplateStatutPartageCode;
      this.valeur = valeur;
   }

   public Integer getImportTemplateStatutPartageCode(){
      return importTemplateStatutPartageCode;
   }

   public String getValeur(){
      return valeur;
   }

   public static EImportTemplateStatutPartage findByCode(Integer code) {
      for (EImportTemplateStatutPartage statutPartageEnum : values()) {
         if (statutPartageEnum.getImportTemplateStatutPartageCode().equals(code)) {
            return statutPartageEnum;
         }
      }
      return null;
   }

   public static boolean checkCoherenceUpdate(EImportTemplateStatutPartage currentValue, EImportTemplateStatutPartage newValue) {
      if(newValue == EImportTemplateStatutPartage.JAMAIS_PARTAGE) {
         return false;
      }
      if(newValue == EImportTemplateStatutPartage.PARTAGE_ENCOURS) {
         return true;//possibilité de partager un modèle jamais partagé ou un modèle dont le partage avait été supprimé
      }
      if(newValue == EImportTemplateStatutPartage.PARTAGE_SUPPRIME) {
         return currentValue == EImportTemplateStatutPartage.PARTAGE_ENCOURS;
      }
      else {
         return false;
      }
   }
   
   public String toString() {
      String nom = super.toString();
      return new StringBuilder().append(importTemplateStatutPartageCode).append(" - ").append(nom).toString();
   }
}
