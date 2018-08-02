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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoComplementExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Banque;

public class TvgsoComplementExportImpl extends INCaComplementExportImpl implements TvgsoComplementExport
{

   private ProdDeriveManager prodDeriveManager;

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   @Override
   public String getDonneesClinBase(final Connection con, final Prelevement prelevement){
      final String res = ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ANNOTATION_VALEUR.BOOL " + "FROM ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '055%' " + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, null, "");
      // boolean true getString -> "1"
      // boolean false getString -> "0"
      if(res.equals("1")){
         return "O";
      }else if(res.equals("0")){
         return "N";
      }
      return res;
   }

   @Override
   public String getInclusionTherap(final Connection con, final Prelevement prelevement){
      final String res = ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ANNOTATION_VALEUR.BOOL " + "FROM ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '056%' " + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, null, "");
      // boolean true getString -> "1"
      // boolean false getString -> "0"
      if(res.equals("1")){
         return "O";
      }else if(res.equals("0")){
         return "N";
      }
      return res;
   }

   @Override
   public String getNomProtocoleTherap(final Connection con, final Prelevement prelevement){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "TABLE_ANNOTATION, CHAMP_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '057%' " + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
            + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
            + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
            + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, null, "");
   }

   @Override
   public String getAnomalieGenomiqueDescr(final Connection con, final Patient patient, final Banque bank){
      final String descr = super.getAnomalieGenomiqueDescr(con, patient, bank);
      if(descr.equals("")){
         return ExportCatalogueManagerImpl.fetchItemAsString(con,
            "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "CHAMP_ANNOTATION, TABLE_ANNOTATION"
               + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + patient.getPatientId().toString()
               + " AND CHAMP_ANNOTATION.NOM like '061%'" + " AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND "
               + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
               + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
               + "AND TABLE_ANNOTATION.ENTITE_ID=1 " + "AND ANNOTATION_VALEUR.BANQUE_ID=" + bank.getBanqueId().toString(),
            null, true, null, "");
      }
      return descr;
   }

   @Override
   public String getControleQualite(final Echantillon echantillon){

      String qual = "";
      qual = super.getControleQualite(echantillon);

      if(qual.equals("")){
         final Iterator<ProdDerive> derivesIt = prodDeriveManager.findByParentManager(echantillon, false).iterator();
         ProdDerive derive;
         while(derivesIt.hasNext()){
            derive = derivesIt.next();
            if(derive.getProdQualite() != null){
               return "O";
            }
         }
      }
      return qual;
   }

   @Override
   public String getInclusionProtocoleRech(final Connection con, final Echantillon echantillon){
      final String res = ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ANNOTATION_VALEUR.BOOL " + "FROM ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + echantillon.getEchantillonId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '063%' " + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=3",
         null, true, null, "");
      // boolean true getString -> "1"
      // boolean false getString -> "0"
      if(res.equals("1")){
         return "O";
      }else if(res.equals("0")){
         return "N";
      }
      return res;
   }

   @Override
   public String getNomProtocoleRech(final Connection con, final Echantillon echantillon){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "TABLE_ANNOTATION, CHAMP_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + echantillon.getEchantillonId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '064%' " + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID "
            + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
            + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
            + "AND TABLE_ANNOTATION.ENTITE_ID=3",
         null, true, null, "");
   }

   @Override
   public String getChampSpecCancer(final Connection con, final Patient patient, final Banque banque){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "TABLE_ANNOTATION, CHAMP_ANNOTATION "
            + "WHERE ANNOTATION_VALEUR.OBJET_ID=" + patient.getPatientId().toString() + " AND CHAMP_ANNOTATION.NOM like '065%' "
            + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID " + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=1 " + "AND ANNOTATION_VALEUR.BANQUE_ID="
            + banque.getBanqueId().toString(),
         null, true, null, "");
   }

   @Override
   public String getConsentement(final Prelevement prelevement, final String oType, final String nType){
      if(prelevement.getConsentType() != null){
         final Pattern oP = Pattern.compile(oType, Pattern.CASE_INSENSITIVE);
         final Pattern nP = Pattern.compile(nType, Pattern.CASE_INSENSITIVE);
         final Matcher oM = oP.matcher(prelevement.getConsentType().getType());
         final Matcher nM = nP.matcher(prelevement.getConsentType().getType());
         if(nM.matches()){
            return "N";
         }else if(oM.matches()){
            return "O";
         }
      }
      return "";
   }

   @Override
   public String getContactShort(final Banque banque){

      final List<String> res = super.getContact(banque);

      return res.get(0) + " " + res.get(1);
   }
}
