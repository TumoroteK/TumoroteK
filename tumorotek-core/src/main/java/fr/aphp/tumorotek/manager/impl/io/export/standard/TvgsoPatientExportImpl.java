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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPatientExport;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

public class TvgsoPatientExportImpl extends INCaPatientExportImpl implements TvgsoPatientExport
{

   private final Log log = LogFactory.getLog(TvgsoPatientExport.class);

   @Override
   public String getPatientCode(final Patient pat){
      log.debug("TVGSO specs -> nip export");
      if(pat.getNip() != null){
         return pat.getNip();
      }else{
         throw new ItemException(2, "Identifiant patient obligatoire manquant");
      }
   }

   @Override
   public String getPatientEtat(final Connection con, final Patient pat, final Banque bank){

      final String etat = super.getPatientEtat(pat);

      if(etat.equals("I")){
         final String fromAnnots = ExportCatalogueManagerImpl.fetchItemAsString(con,
            "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "CHAMP_ANNOTATION, TABLE_ANNOTATION"
               + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + pat.getPatientId().toString() + " AND CHAMP_ANNOTATION.NOM like '005%'"
               + " AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND " + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
               + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
               + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=1 "
               + "AND ANNOTATION_VALEUR.BANQUE_ID=" + bank.getBanqueId().toString(),
            null, true, "(V|D|I) : .+", "");
         if(!fromAnnots.equals("")){
            return fromAnnots;
         }

      }
      return etat;
   }

   @Override
   public String getDateEtat(final Connection con, final Patient pat, final DateFormat df, final Banque bank){

      try{
         return super.getDateEtat(pat, df);
      }catch(final ItemException e){
         log.debug("TVGSO specs -> date etat non obligatoire");

         return ExportCatalogueManagerImpl.fetchItemDate(con,
            "SELECT ANNOTATION_VALEUR.ANNO_DATE FROM " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
               + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + pat.getPatientId().toString() + " AND CHAMP_ANNOTATION.NOM like '006%'"
               + " AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
               + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
               + "AND TABLE_ANNOTATION.ENTITE_ID=1 " + "AND ANNOTATION_VALEUR.BANQUE_ID=" + bank.getBanqueId().toString(),
            null, true, "00000000", df);
      }
   }
}
