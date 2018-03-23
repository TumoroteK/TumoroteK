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
package fr.aphp.tumorotek.interfacage.sgl.view.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import fr.aphp.tumorotek.interfacage.sgl.view.ViewHandler;
import fr.aphp.tumorotek.model.interfacage.BlocExterne;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.ValeurExterne;

public class DaVinciViewHandler implements ViewHandler
{

   private final Logger log = Logger.getLogger(DaVinciViewHandler.class);

   private String camelConfigLocation;

   public void setCamelConfigLocation(final String _c){
      this.camelConfigLocation = _c;
   }

   @Override
   public DossierExterne sendQuery(final Emetteur _e, final String sglNumDos){
      final Map<String, Object> args = new HashMap<>();
      args.put("numDos", sglNumDos);

      final DossierExterne dExt = queryView(sglNumDos);
      if(dExt != null){
         dExt.setEmetteur(_e);
      }
      return dExt;
   }

   //	@Override
   //	public DossierExterne processResult(Map<String, Object> data) {
   //		System.out.println("Processed View id " + data.get("ID") + " Dossier " 
   //				+ data.get("NUM_DOS"));
   //		DossierExterne ext = new DossierExterne();	
   //		ext.setIdentificationDossier((String) data.get("NUM_DOS")); 
   //		ext.setDateOperation(Calendar.getInstance());
   //		ext.setEmetteur(geEmetteur());
   //		
   //		SimpleDateFormat hl7LikeDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   //
   //		// patient
   //		BlocExterne blocPatient = new BlocExterne();
   //		// NIP
   //		if (data.get("NIP") != null) {
   //			ValeurExterne valNip = new ValeurExterne();
   //			valNip.setBlocExterne(blocPatient);
   //			blocPatient.getValeurs().add(valNip);
   //			valNip.setChampEntiteId(2);
   //			valNip.setValeur((String) data.get("NIP"));
   //		}
   //		// NOM
   //		if (data.get("NOM") != null) {
   //			ValeurExterne valNom = new ValeurExterne();
   //			valNom.setBlocExterne(blocPatient);
   //			blocPatient.getValeurs().add(valNom);
   //			valNom.setChampEntiteId(3);
   //			valNom.setValeur((String) data.get("NOM"));
   //		}
   //		// PRENOM
   //		if (data.get("PRENOM") != null) {
   //			ValeurExterne valPrenom = new ValeurExterne();
   //			valPrenom.setBlocExterne(blocPatient);
   //			blocPatient.getValeurs().add(valPrenom);
   //			valPrenom.setChampEntiteId(5);
   //			valPrenom.setValeur((String) data.get("PRENOM"));
   //		}
   //		// SEXE
   //		if (data.get("SEXE") != null) {
   //			ValeurExterne valSexe = new ValeurExterne();
   //			valSexe.setBlocExterne(blocPatient);
   //			blocPatient.getValeurs().add(valSexe);
   //			valSexe.setChampEntiteId(6);
   //			valSexe.setValeur((String) data.get("SEXE"));
   //		}
   //		// DATE_NAISSANCE
   //		if (data.get("DATE_NAISSANCE") != null) {
   //			ValeurExterne valDN = new ValeurExterne();
   //			valDN.setBlocExterne(blocPatient);
   //			blocPatient.getValeurs().add(valDN);
   //			valDN.setChampEntiteId(7);
   //			valDN.setValeur(hl7LikeDateFormat.format((Date) data.get("DATE_NAISSANCE")));
   //		}
   //		
   //		if (!blocPatient.getValeurs().isEmpty()) {
   //			blocPatient.setDossierExterne(ext);
   //			blocPatient.setEntiteId(1);
   //			blocPatient.setOrdre(1);
   //			ext.getBlocExternes().add(blocPatient);
   //		}
   //		
   //		// patient
   //		BlocExterne blocPrel = new BlocExterne();
   //		// DATE PRELEVEMENT
   //		if (data.get("DATE_PRELEVEMENT") != null) {
   //			ValeurExterne valDatePrel = new ValeurExterne();
   //			valDatePrel.setBlocExterne(blocPrel);
   //			blocPrel.getValeurs().add(valDatePrel);
   //			valDatePrel.setChampEntiteId(30);
   //			valDatePrel.setValeur(hl7LikeDateFormat.format((Date) data.get("DATE_PRELEVEMENT")));
   //		}
   //		
   //		if (!blocPrel.getValeurs().isEmpty()) {
   //			blocPrel.setDossierExterne(ext);
   //			blocPrel.setEntiteId(2);
   //			blocPrel.setOrdre(2);
   //			ext.getBlocExternes().add(blocPrel);
   //		}
   //		
   //		// echantillon
   //		BlocExterne blocEchan = new BlocExterne();
   //		// CR
   //		if (data.get("CR") != null) {
   //			ValeurExterne valCR = new ValeurExterne();
   //			valCR.setBlocExterne(blocEchan);
   //			blocEchan.getValeurs().add(valCR);
   //			valCR.setChampEntiteId(255);
   //			valCR.setContenu(((String) data.get("CR")).getBytes());
   //		}
   //		// CODES ORGANES
   //		if (data.get("CODES_ORG") != null) {
   //			ValeurExterne valCodesOrg = new ValeurExterne();
   //			valCodesOrg.setBlocExterne(blocEchan);
   //			blocEchan.getValeurs().add(valCodesOrg);
   //			valCodesOrg.setChampEntiteId(229);
   //			valCodesOrg.setValeur(((String) data.get("CODES_ORG")).replace(",", ";"));
   //		}
   //		// CODES LES
   //		if (data.get("CODES_LES") != null) {
   //			ValeurExterne valCodesLes = new ValeurExterne();
   //			valCodesLes.setBlocExterne(blocEchan);
   //			blocEchan.getValeurs().add(valCodesLes);
   //			valCodesLes.setChampEntiteId(230);
   //			valCodesLes.setValeur(((String) data.get("CODES_LES")).replace(",", ";"));
   //		}
   //		
   //		if (!blocEchan.getValeurs().isEmpty()) {
   //			blocEchan.setDossierExterne(ext);
   //			blocEchan.setEntiteId(3);
   //			blocEchan.setOrdre(3);
   //			ext.getBlocExternes().add(blocEchan);
   //		}
   //		
   //		setDosExt(ext);
   //		
   //		return ext;
   //	}

   @Override
   public DossierExterne processResult(final ResultSet rSet) throws SQLException{
      final DossierExterne ext = new DossierExterne();
      ext.setIdentificationDossier(rSet.getString("NUM_ECHAN"));
      ext.setDateOperation(Calendar.getInstance());

      // SimpleDateFormat hl7LikeDateFormat = new SimpleDateFormat("yyyyMMdds");
      // SimpleDateFormat hl7LikeDatetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

      // patient
      final BlocExterne blocPatient = new BlocExterne();
      // 2 - NIP
      if(rSet.getString("NIP") != null && !rSet.getString("NIP").trim().equals("")){
         final ValeurExterne valNip = new ValeurExterne();
         valNip.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNip);
         valNip.setChampEntiteId(2);
         valNip.setValeur(rSet.getString("NIP"));
         log.debug(valNip.getValeur());
      }
      // 4 - NOM -- Num usuel??
      if(rSet.getString("NOM") != null && !rSet.getString("NOM").trim().equals("")){
         final ValeurExterne valNom = new ValeurExterne();
         valNom.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNom);
         valNom.setChampEntiteId(3);
         valNom.setValeur(rSet.getString("NOM"));
         log.debug(valNom.getValeur());
      }
      // 5 - NOM NAISSANCE
      if(rSet.getString("NOM_NAISSANCE") != null && !rSet.getString("NOM_NAISSANCE").trim().equals("")){
         final ValeurExterne valNomNais = new ValeurExterne();
         valNomNais.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valNomNais);
         valNomNais.setChampEntiteId(4);
         valNomNais.setValeur(rSet.getString("NOM_NAISSANCE"));
         log.debug(valNomNais.getValeur());
      }
      // 6 - PRENOM
      if(rSet.getString("PRENOM") != null && !rSet.getString("PRENOM").trim().equals("")){
         final ValeurExterne valPrenom = new ValeurExterne();
         valPrenom.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valPrenom);
         valPrenom.setChampEntiteId(5);
         valPrenom.setValeur(rSet.getString("PRENOM"));
      }
      // 7 - DATE_NAISSANCE
      if(rSet.getString("DATE_NAIS") != null && !rSet.getString("DATE_NAIS").trim().equals("")){
         final ValeurExterne valDN = new ValeurExterne();
         valDN.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valDN);
         valDN.setChampEntiteId(7);
         valDN.setValeur(rSet.getString("DATE_NAIS"));
         log.debug(valDN.getValeur());
      }
      // 8 - SEXE
      if(rSet.getString("SEXE") != null && !rSet.getString("SEXE").trim().equals("")){
         final ValeurExterne valSexe = new ValeurExterne();
         valSexe.setBlocExterne(blocPatient);
         blocPatient.getValeurs().add(valSexe);
         valSexe.setChampEntiteId(6);
         valSexe.setValeur(rSet.getString("SEXE"));
         log.debug(valSexe.getValeur());
      }

      if(!blocPatient.getValeurs().isEmpty()){
         blocPatient.setDossierExterne(ext);
         blocPatient.setEntiteId(1);
         blocPatient.setOrdre(1);
         ext.getBlocExternes().add(blocPatient);
      }

      // prel
      final BlocExterne blocPrel = new BlocExterne();
      // 3 - NDA
      if(rSet.getString("NDA") != null && !rSet.getString("NDA").trim().equals("")){
         final ValeurExterne valNda = new ValeurExterne();
         valNda.setBlocExterne(blocPrel);
         blocPrel.getValeurs().add(valNda);
         valNda.setChampEntiteId(44);
         valNda.setValeur(rSet.getString("NDA"));
         log.debug(valNda.getValeur());
      }
      // 9+10 - DATE HEURE PRELEVEMENT
      if(rSet.getString("DATE_PREL") != null && !rSet.getString("DATE_PREL").trim().equals("")){
         final ValeurExterne valDatePrel = new ValeurExterne();
         valDatePrel.setBlocExterne(blocPrel);
         blocPrel.getValeurs().add(valDatePrel);
         valDatePrel.setChampEntiteId(30);

         String datePrel = rSet.getString("DATE_PREL");
         if(rSet.getString("HEURE_PREL") != null){
            datePrel = datePrel + rSet.getString("HEURE_PREL");
         }

         valDatePrel.setValeur(datePrel);
         log.debug(valDatePrel.getValeur());
      }

      if(!blocPrel.getValeurs().isEmpty()){
         blocPrel.setDossierExterne(ext);
         blocPrel.setEntiteId(2);
         blocPrel.setOrdre(3);
         ext.getBlocExternes().add(blocPrel);
      }

      // echantillon
      final BlocExterne blocEchan = new BlocExterne();
      // CR
      //		if (rSet.getString("CR") != null) {
      //			ValeurExterne valCR = new ValeurExterne();
      //			valCR.setBlocExterne(blocEchan);
      //			blocEchan.getValeurs().add(valCR);
      //			valCR.setChampEntiteId(255);
      //			valCR.setContenu(rSet.getString("CR").getBytes());
      //		}
      // CODES ORGANES
      //		if (rSet.getString("CODES_ORG") != null) {
      //			ValeurExterne valCodesOrg = new ValeurExterne();
      //			valCodesOrg.setBlocExterne(blocEchan);
      //			blocEchan.getValeurs().add(valCodesOrg);
      //			valCodesOrg.setChampEntiteId(229);
      //			valCodesOrg.setValeur(rSet.getString("CODES_ORG").replace(",", ";"));
      //		}
      // 12 CODES LES ADICAP?
      if(rSet.getClob("ADICAP_LESION") != null && !rSet.getString("ADICAP_LESION").trim().equals("")){
         final ValeurExterne valCodesLes = new ValeurExterne();
         valCodesLes.setBlocExterne(blocEchan);
         blocEchan.getValeurs().add(valCodesLes);
         valCodesLes.setChampEntiteId(230);

         final Clob bodyOut = rSet.getClob("ADICAP_LESION");
         final int length = (int) bodyOut.length();
         final String codesLes = bodyOut.getSubString(1, length);
         //bodyOut.free(); // new in JDBC 4.0

         valCodesLes.setValeur(codesLes.replace(",", ";"));
         log.debug("les:" + valCodesLes.getValeur() + ".");
      }

      // 14+15 - DATE HEURE STOCKAGE
      if(rSet.getString("DATE_STOCKAGE") != null && !rSet.getString("DATE_STOCKAGE").trim().equals("")){
         final ValeurExterne valDateStock = new ValeurExterne();
         valDateStock.setBlocExterne(blocPrel);
         blocEchan.getValeurs().add(valDateStock);
         valDateStock.setChampEntiteId(56);

         String dateStock = rSet.getString("DATE_STOCKAGE");
         if(rSet.getString("HEURE_STOCKAGE") != null){
            dateStock = dateStock + rSet.getString("HEURE_STOCKAGE");
         }

         valDateStock.setValeur(dateStock);
         log.debug(valDateStock.getValeur());
      }

      if(!blocEchan.getValeurs().isEmpty()){
         blocEchan.setDossierExterne(ext);
         blocEchan.setEntiteId(3);
         blocEchan.setOrdre(4);
         ext.getBlocExternes().add(blocEchan);
      }

      // maladie
      final BlocExterne blocMaladie = new BlocExterne();
      // 13 CIM
      if(rSet.getClob("CIM") != null && !rSet.getString("CIM").trim().equals("")){
         final ValeurExterne valCim = new ValeurExterne();
         valCim.setBlocExterne(blocMaladie);
         blocMaladie.getValeurs().add(valCim);
         valCim.setChampEntiteId(17);

         final Clob bodyOut = rSet.getClob("CIM");
         final int length = (int) bodyOut.length();
         final String codesCim = bodyOut.getSubString(1, length);
         //bodyOut.free(); // new in JDBC 4.0

         valCim.setValeur(codesCim.replace(",", ";"));
         log.debug(valCim.getValeur());
      }

      if(!blocMaladie.getValeurs().isEmpty()){
         blocMaladie.setDossierExterne(ext);
         blocMaladie.setEntiteId(7);
         blocMaladie.setOrdre(2);
         ext.getBlocExternes().add(blocMaladie);
      }

      return ext;
   }

   public DossierExterne queryView(final String numDos){
      DossierExterne dExt = null;
      final ResourceBundle jdbcBundle = getJdbcBundle("davinci.properties");
      if(jdbcBundle != null){
         Connection con = null;
         PreparedStatement stmt = null;
         ResultSet rSet = null;

         try{
            final java.util.Properties props = new java.util.Properties();
            props.put("user", jdbcBundle.getString("username"));
            props.put("password", jdbcBundle.getString("password"));

            // Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Class.forName(jdbcBundle.getString("driver"));

            con = DriverManager.getConnection(jdbcBundle.getString("jdbcUrl"), props);

            stmt = con.prepareStatement(jdbcBundle.getString("statement"));
            stmt.setString(1, numDos);

            rSet = stmt.executeQuery();
            // one result max
            if(rSet.next()){
               dExt = processResult(rSet);
            }

         }catch(final Exception e){
            e.printStackTrace();
         }finally{
            if(con != null){
               try{
                  con.close();
               }catch(final Exception e){
                  con = null;
               }
            }
            if(stmt != null){
               try{
                  stmt.close();
               }catch(final Exception e){
                  stmt = null;
               }
            }
            if(rSet != null){
               try{
                  rSet.close();
               }catch(final Exception e){
                  rSet = null;
               }
            }
         }
      }
      return dExt;
   }

   private ResourceBundle getJdbcBundle(final String propFileName){

      if(camelConfigLocation != null && propFileName != null){
         final File file = new File(camelConfigLocation + propFileName);
         FileInputStream fis = null;
         InputStreamReader reader = null;
         ResourceBundle bundle = null;

         if(file.isFile()){ // Also checks for existance
            try{
               fis = new FileInputStream(file);
               reader = new InputStreamReader(fis, Charset.forName("UTF-8"));
               bundle = new PropertyResourceBundle(reader);
            }catch(final FileNotFoundException e){
               e.printStackTrace();
            }catch(final IOException e){
               e.printStackTrace();
            }finally{
               try{
                  reader.close();
               }catch(final IOException e){
                  reader = null;
               }
               try{
                  fis.close();
               }catch(final IOException e){
                  fis = null;
               }
            }
         }else{
            throw new RuntimeException("view.jdbc.properties.not.found");
         }
         return bundle;
      }

      return null;
   }

}
