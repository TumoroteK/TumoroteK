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
package fr.aphp.tumorotek.manager.impl.io.export.standard;

import java.sql.Connection;
import java.text.DateFormat;

import fr.aphp.tumorotek.manager.io.export.standard.IncaPatientExport;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

public class INCaPatientExportImpl implements IncaPatientExport
{

   @Override
   public String getFiness(final Connection con, final Prelevement prel, final Patient pat){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ETABLISSEMENT.NOM " + "FROM ETABLISSEMENT, SERVICE, PRELEVEMENT "
            + "WHERE PRELEVEMENT.SERVICE_PRELEVEUR_ID = SERVICE.SERVICE_ID "
            + "AND ETABLISSEMENT.ETABLISSEMENT_ID = SERVICE.ETABLISSEMENT_ID " + "AND PRELEVEMENT.PRELEVEMENT_ID =  "
            + prel.getPrelevementId().toString(),
         "FINESS manquant pour le patient " + pat.getNip(), true, null, null);
   }

   @Override
   public String getPatientId(final Patient pat){
      if(pat.getPatientId() != null){
         return pat.getPatientId().toString();
      }else{
         throw new ItemException(2, "Identifiant patient obligatoire manquant");
      }
   }

   @Override
   public String getDateNaissance(final Patient pat, final DateFormat df){
      if(pat.getDateNaissance() != null){
         return df.format(pat.getDateNaissance());
      }else{
         throw new ItemException(2, "Date de naissance manquante");
      }
   }

   @Override
   public String getSexe(final Patient pat){

      if(pat.getSexe() != null && pat.getSexe().matches("M|F")){
         return pat.getSexe();
      }else{
         return "I";
      }
   }

   @Override
   public String getPatientEtat(final Patient pat){

      String etat = "I";

      if(pat.getPatientEtat() != null){
         etat = pat.getPatientEtat().substring(0, 1);
      }

      return etat;
   }

   @Override
   public String getDateEtat(final Patient pat, final DateFormat df){

      if(pat.getDateDeces() != null){
         return df.format(pat.getDateDeces());
      }else if(pat.getDateEtat() != null){
         return df.format(pat.getDateEtat());
      }else{
         throw new ItemException(2, "Date etat patient manquante");
      }

   }

   @Override
   public String getCauseDeces(final Connection con, final Patient pat, final Banque bank){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "CHAMP_ANNOTATION, TABLE_ANNOTATION"
            + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + pat.getPatientId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '076 : Cause %'" + " AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND "
            + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
            + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
            + "AND TABLE_ANNOTATION.ENTITE_ID=1 " + "AND ANNOTATION_VALEUR.BANQUE_ID=" + bank.getBanqueId().toString(),
         null, true, "([1-9]) : .+", "");
   }
}
