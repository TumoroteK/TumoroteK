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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.manager.io.export.standard.IncaPrelevementExport;
import fr.aphp.tumorotek.manager.io.export.standard.TvgsoPrelevementExport;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;

public class TvgsoPrelevementExportImpl extends INCaPrelevementExportImpl implements TvgsoPrelevementExport
{

   private final Log log = LogFactory.getLog(IncaPrelevementExport.class);

   @Override
   public String getCentreStockage(final Connection con, final Echantillon echantillon){
      final String siteStockage = ExportCatalogueManagerImpl.fetchItemAsString(con,
         "SELECT ETABLISSEMENT.NOM FROM ETABLISSEMENT, " + "SERVICE, ECHANTILLON, SERVICE_COLLABORATEUR"
            + " WHERE ECHANTILLON.ECHANTILLON_ID=" + echantillon.getEchantillonId().toString()
            + " AND ECHANTILLON.COLLABORATEUR_ID=" + "SERVICE_COLLABORATEUR.COLLABORATEUR_ID"
            + " AND SERVICE_COLLABORATEUR.SERVICE_ID=SERVICE.SERVICE_ID" + " AND SERVICE.ETABLISSEMENT_ID="
            + "ETABLISSEMENT.ETABLISSEMENT_ID",
         "Centre de stockage non renseigné", true, null, null);

      if(siteStockage != null){
         if(siteStockage.matches(".*BORDEAUX.*")){
            if(siteStockage.matches(".*CHU.*")){
               return "1TGSOCHUBDX";
            }else if(siteStockage.matches(".*CLC.*")){
               return "2TGSOCLCBDX";
            }
         }else if(siteStockage.matches(".*LIMOGES.*")){
            if(siteStockage.matches(".*CHU.*")){
               return "3TGSOCHULIM";
            }
         }
         if(siteStockage.matches(".*MONTPELLIER.*")){
            if(siteStockage.matches(".*CHU.*")){
               return "4TGSOCHUMPL";
            }else if(siteStockage.matches(".*CLC.*")){
               return "5TGSOCLCMPL";
            }
         }else if(siteStockage.matches(".*NIMES.*")){
            if(siteStockage.matches(".*CHU.*")){
               return "6TGSOCHUNIM";
            }
         }else if(siteStockage.matches(".*TOULOUSE.*")){
            if(siteStockage.matches(".*CHU.*")){
               return "7TGSOCHUTLS";
            }else if(siteStockage.matches(".*CLC.*")){
               return "8TGSOCLCTLS";
            }
         }else{
            throw new ItemException(2, "Creation du code pour le centre de stockage GSO :" + siteStockage
               + " impossible pour echantillon " + echantillon.getCode());
         }
      }
      return null;
   }

   @Override
   public String getNumSejour(final Prelevement prelevement){
      return null;
   }

   @Override
   public String getDatePrelevement(final Prelevement prelevement, final DateFormat df){
      try{
         return super.getDatePrelevement(prelevement, df);
      }catch(final ItemException e){
         log.debug("TVGSO specs -> date prelevement non obligatoire");
         return "";
      }
   }

   @Override
   public String getModePrelevement(final Prelevement prelevement){
      String type = super.getModePrelevement(prelevement);
      if(type.equals("L")){
         type = "C";
      }
      return type;
   }

   @Override
   public String getClassif(final Banque bank){

      // String classifs = "";
      // try {
      //	classifs = super.getClassif(bank);
      // } catch (ItemException ie) {
      log.debug("TVGSO specs -> classif recherchee dans nom collection");
      // }
      // if (classifs.equals("")) {
      final Pattern p = Pattern.compile(".+-([AC]).*");
      final Matcher m = p.matcher(bank.getNom());
      final boolean b = m.matches();
      if(b){
         return m.group(1);
      }else{
         throw new ItemException(2, "Aucune classification specifiee pour la banque " + bank.getNom());
      }
      //}

      //return null;
   }

   @Override
   public String getCodeOrgane(final Connection con, final Echantillon echan){

      String code = "";

      try{
         code = super.getCodeOrgane(con, echan, null);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> code organe non obligatoire");
         return "";
      }

      final Pattern p = Pattern.compile("([A-X]{2}) : .+");
      final Matcher m = p.matcher(code);
      final boolean b = m.matches();
      if(b){
         code = m.group(1);
      }

      return code;

   }

   @Override
   public String getTypeLesionnel(final Connection con, final Echantillon echan){
      try{
         return super.getTypeLesionnel(con, echan, null);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> type lesionnel non obligatoire");
         return "";
      }
   }

   @Override
   public String getVersionPTNM(final Connection con, final Prelevement prelevement){
      try{
         return super.getVersionPTNM(con, prelevement);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> version pTNM non obligatoire");
         return "";
      }
   }

   @Override
   public String getTailleTumeurPT(final Connection con, final Prelevement prelevement){
      try{
         return super.getTailleTumeurPT(con, prelevement);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> pT non obligatoire");
         return "";
      }
   }

   @Override
   public String getEnvGangPN(final Connection con, final Prelevement prelevement){
      try{
         return super.getEnvGangPN(con, prelevement);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> pN non obligatoire");
         return "";
      }
   }

   @Override
   public String getExtMetastaticPM(final Connection con, final Prelevement prelevement){
      try{
         return super.getExtMetastaticPM(con, prelevement);
      }catch(final ItemException ie){
         log.debug("TVGSO specs -> pM non obligatoire");
         return "";
      }
   }

}
