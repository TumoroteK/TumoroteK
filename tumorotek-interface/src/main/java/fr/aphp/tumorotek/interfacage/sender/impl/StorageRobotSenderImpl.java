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
package fr.aphp.tumorotek.interfacage.sender.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.interfacage.sender.StorageRobotSender;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageEmplacement;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;

public class StorageRobotSenderImpl implements StorageRobotSender
{

   private final Log log = LogFactory.getLog(StorageRobotSender.class);

   private EmplacementManager emplacementManager;
   private ProducerTemplate camelTemplate;
   private String camelConfigLocation;

   public void setEmplacementManager(final EmplacementManager _e){
      this.emplacementManager = _e;
   }

   public void setCamelTemplate(final ProducerTemplate ct){
      this.camelTemplate = ct;
   }

   public void setCamelConfigLocation(final String _c){
      this.camelConfigLocation = _c;
   }

   @Override
   public void sendEmplacements(final Recepteur re, final Map<TKStockableObject, Emplacement> tkEmpls, final OperationType oType){
      if(tkEmpls != null){
         final InnerEnvVariables vars = new InnerEnvVariables();

         initVarFromStorageRobotBundle(vars);

         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         try{
            makeCSVfromMap(re, baos, tkEmpls, oType, vars.getSeparator(), vars.getNbLinesToBeProvide());
            camelTemplate.sendBodyAndHeader("direct:storage-robot", baos, "CamelFileName", vars.getFilename());
         }catch(final IOException e){
            log.error(e.getMessage());
         }finally{
            if(baos != null){
               try{
                  baos.close();
               }catch(final IOException e){}
            }
         }
      }
   }

   @Override
   public void makeCSVfromMap(final Recepteur re, final ByteArrayOutputStream baos,
      final Map<TKStockableObject, Emplacement> tkEmpls, final OperationType oType, final String separator,
      final int nbLinesToBeProvide) throws IOException{

      final List<StorageEmplacement> stoEmplacements = new ArrayList<>();
      final Iterator<TKStockableObject> it = tkEmpls.keySet().iterator();
      TKStockableObject tko;
      Emplacement emp;
      StorageEmplacement stoEmp;
      String[] posAddr;
      while(it.hasNext()){
         tko = it.next();
         emp = tkEmpls.get(tko);
         if(emp != null){
            posAddr = emplacementManager.getAdrlManager(emp, true).split("\\.");
            if(posAddr.length < 4 && re.getLogiciel().getNom().equals("IRELEC")){
               throw new RuntimeException("storage.robot.emplacement.adrl.incompatible");
            }
            stoEmp = new StorageEmplacement(tko.getCode(), posAddr[0], posAddr[1], posAddr[posAddr.length - 2],
               posAddr[posAddr.length - 1]);
            stoEmplacements.add(stoEmp);
         }
      }

      // sort by adrl to facilitate
      // robot handle
      Collections.sort(stoEmplacements);
      int currentRow = 0;

      final boolean isStockage = oType == null || oType.getNom().equals("Stockage");

      for(final StorageEmplacement stE : stoEmplacements){
         if(stE.isComplete()){
            currentRow++;
            baos.write(String.valueOf(currentRow).getBytes());
            baos.write(separator.getBytes());
            if(!isStockage){ // destockage
               baos.write(writeEmptySeparators(separator, 5).getBytes());
            }
            baos.write(stE.getBarcode().getBytes());
            baos.write(separator.getBytes());
            baos.write(stE.getConteneurCode().getBytes());
            baos.write(separator.getBytes());
            baos.write(stE.getRack().getBytes());
            baos.write(separator.getBytes());
            baos.write(stE.getBoite().getBytes());
            baos.write(separator.getBytes());
            baos.write(stE.getPosition().getBytes());
            baos.write("\n".getBytes());
         }
      }
      // complete to 1000 as IRELEC demands
      if(nbLinesToBeProvide > 0){
         while(currentRow < nbLinesToBeProvide){
            currentRow++;
            baos.write(String.valueOf(currentRow).getBytes());
            baos.write(separator.getBytes());
            if(!isStockage){ // destockage
               baos.write(writeEmptySeparators(separator, 5).getBytes());
            }
            baos.write(separator.getBytes());
            baos.write(separator.getBytes());
            baos.write(separator.getBytes());
            baos.write(separator.getBytes());
            baos.write("\n".getBytes());
         }
      }
   }

   private String writeEmptySeparators(final String s, final int x){
      String out = "";
      int i = 0;
      while(i < x){
         out = out + s;
         i++;
      }
      return out;
   }

   @Override
   public void sendMessages(final List<TKAnnotableObject> objs, final Integer b){}

   @Override
   public String setFileName(final TKAnnotableObject prel, final boolean isOkFile, final Integer part, final String currtime){
      // ResourceBundle storageRobotBundle = getStorageRobotBundle("storageRobot.properties");
      // if (storageRobotBundle != null) {
      // 	return storageRobotBundle.getString("csvname");
      // }
      return "storageRobotCsv.csv";
   }

   @Override
   public boolean useRecepteur(final Recepteur r){
      if(r != null && r.getLogiciel() != null && r.getLogiciel().getNom().equals("IRELEC")
         && r.getIdentification().matches(".*STORAGE.*")){
         return true;
      }
      return false;
   }

   @Override
   public void sendMessage(final TKAnnotableObject obj, final String dosExtId, final String url){}

   private void initVarFromStorageRobotBundle(final InnerEnvVariables vars){

      final String propFileName = "storage_robot.properties";

      if(camelConfigLocation != null){
         final File file = new File(camelConfigLocation + propFileName);
         FileInputStream fis = null;
         InputStreamReader reader = null;
         ResourceBundle bundle = null;

         if(file.isFile()){ // Also checks for existance
            try{
               fis = new FileInputStream(file);
               reader = new InputStreamReader(fis, Charset.forName("UTF-8"));
               bundle = new PropertyResourceBundle(reader);

               vars.setFilename(bundle.getString("csvname"));
               vars.setSeparator(bundle.getString("csv.separator"));
               vars.setNbLinesToBeProvide(Integer.valueOf(bundle.getString("nb.lines.tofill")));

            }catch(final FileNotFoundException e){
               e.printStackTrace();
            }catch(final IOException e){
               e.printStackTrace();
            }finally{
               if(null != reader){
                  try{
                     reader.close();
                  }catch(final IOException e){
                     reader = null;
                  }
               }
               if(null != fis){
                  try{
                     fis.close();
                  }catch(final IOException e){
                     fis = null;
                  }
               }
            }
         }else{
            throw new RuntimeException("storageRobot.properties.not.found");
         }
      }
   }

   private class InnerEnvVariables
   {
      private String separator = "|";
      private String filename = "storageRobotCsv.csv";
      private Integer nbLinesToBeProvide = -1;

      public String getSeparator(){
         return separator;
      }

      public void setSeparator(final String _s){
         this.separator = _s;
      }

      public String getFilename(){
         return filename;
      }

      public void setFilename(final String _f){
         this.filename = _f;
      }

      public Integer getNbLinesToBeProvide(){
         return nbLinesToBeProvide;
      }

      public void setNbLinesToBeProvide(final Integer _n){
         this.nbLinesToBeProvide = _n;
      }
   }
}
