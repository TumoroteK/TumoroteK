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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.IncaMaladieExport;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;

public class INCaMaladieExportImpl implements IncaMaladieExport
{

   private final Log log = LogFactory.getLog(IncaMaladieExport.class);

   @Override
   public String getDiagnosticPrincipal(final Connection con, final Prelevement prelevement, final boolean tvgso){

      final String errorMessage = "Code CIM10 maladie manquant";
      String diag = "";
      if(prelevement.getMaladie() != null && prelevement.getMaladie().getCode() != null){
         diag = prelevement.getMaladie().getCode();
      }

      if(tvgso && diag.equals("")){
         boolean isThes = true;
         Statement s = null;
         ResultSet rs = null;
         try{
            s = con.createStatement();
            rs = s.executeQuery("SELECT DATA_TYPE_ID " + "FROM CHAMP_ANNOTATION, TABLE_ANNOTATION, " + "TABLE_ANNOTATION_BANQUE "
               + "where CHAMP_ANNOTATION.NOM like '007%' AND " + "CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
               + "TABLE_ANNOTATION_BANQUE.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION_BANQUE.BANQUE_ID="
               + String.valueOf(prelevement.getBanque().getBanqueId()) + " AND TABLE_ANNOTATION.TABLE_ANNOTATION_ID="
               + "TABLE_ANNOTATION_BANQUE.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2");
            if(rs.next()){
               isThes = (rs.getInt(1) == 7);
            }
            rs.close();
         }catch(final SQLException sqle){
            log.error(sqle);
         }finally{
            if(s != null){
               try{
                  s.close();
               }catch(final SQLException e){
                  s = null;
               }
            }
            if(rs != null){
               try{
                  rs.close();
               }catch(final SQLException e){
                  rs = null;
               }
            }
         }

         if(isThes){
            return ExportCatalogueManagerImpl.fetchItemAsString(con,
               "SELECT ITEM.LABEL FROM ITEM, ANNOTATION_VALEUR, " + "CHAMP_ANNOTATION, TABLE_ANNOTATION"
                  + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
                  + " AND CHAMP_ANNOTATION.NOM like '007%'" + " AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND "
                  + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
                  + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
                  + "AND TABLE_ANNOTATION.ENTITE_ID=2",
               errorMessage, true, "(.+) : .+", null);
         }
         return ExportCatalogueManagerImpl.fetchItemAsString(con,
            "SELECT ANNOTATION_VALEUR.TEXTE FROM ANNOTATION_VALEUR, " + "CHAMP_ANNOTATION, TABLE_ANNOTATION"
               + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
               + " AND CHAMP_ANNOTATION.NOM like '007%' AND " + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
               + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
               + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
            errorMessage, true, "(.+) : .+", null);
      }

      if(diag.equals("")){
         throw new ItemException(2, errorMessage);
      }

      return diag;
   }

   @Override
   public String getDateDiag(final Connection con, final Prelevement prelevement, final boolean tvgso, final DateFormat df){
      String dateDiag = "";
      if(prelevement.getMaladie() != null && prelevement.getMaladie().getDateDiagnostic() != null){
         dateDiag = df.format(prelevement.getMaladie().getDateDiagnostic());
      }

      if(tvgso && dateDiag.equals("")){

         return ExportCatalogueManagerImpl.fetchItemDate(con,
            "SELECT ANNOTATION_VALEUR.ANNO_DATE FROM " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION"
               + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prelevement.getPrelevementId().toString()
               + " AND CHAMP_ANNOTATION.NOM like '008%'" + " AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
               + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
               + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
            null, true, "", df);
      }

      return dateDiag;
   }

   @Override
   public String getVersionCTNM(final Connection con, final Prelevement prel){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION"
            + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prel.getPrelevementId().toString()
            + " AND CHAMP_ANNOTATION.NOM like '009%' AND " + "ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID AND "
            + "CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID=" + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID"
            + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID=" + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID "
            + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, "([4-6]|X).*", "");
   }

   @Override
   public String getTailleTumeur(final Connection con, final Prelevement prel){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION"
            + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prel.getPrelevementId().toString() + " AND CHAMP_ANNOTATION.NOM like '010%' "
            + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID " + "AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, "([0-4]|is|X|Z).*", "");
   }

   @Override
   public String getEnvahGangR(final Connection con, final Prelevement prel){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION"
            + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prel.getPrelevementId().toString() + " AND CHAMP_ANNOTATION.NOM like '011%' "
            + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID" + " AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, "([0-3]|X|Z).*", "");
   }

   @Override
   public String getExtMetastatique(final Connection con, final Prelevement prel){
      return ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ITEM.LABEL FROM ITEM, " + "ANNOTATION_VALEUR, CHAMP_ANNOTATION, TABLE_ANNOTATION"
            + " WHERE ANNOTATION_VALEUR.OBJET_ID=" + prel.getPrelevementId().toString() + " AND CHAMP_ANNOTATION.NOM like '012%' "
            + "AND ITEM.ITEM_ID=ANNOTATION_VALEUR.ITEM_ID" + " AND CHAMP_ANNOTATION.CHAMP_ANNOTATION_ID="
            + "ANNOTATION_VALEUR.CHAMP_ANNOTATION_ID" + " AND CHAMP_ANNOTATION.TABLE_ANNOTATION_ID="
            + "TABLE_ANNOTATION.TABLE_ANNOTATION_ID " + "AND TABLE_ANNOTATION.ENTITE_ID=2",
         null, true, "(0|1|X|Z).*", "");
   }
}
