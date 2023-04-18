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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.interfacage.sender.StorageRobotSender;
import fr.aphp.tumorotek.interfacage.storageRobot.StorageMovement;
import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.1-IRELEC
 *
 */
public class StorageRobotSenderImpl implements StorageRobotSender
{

   private final Logger log = LoggerFactory.getLogger(StorageRobotSender.class);

   private ProducerTemplate camelTemplate;

   private String camelConfigLocation;

   public void setCamelTemplate(final ProducerTemplate ct){
      this.camelTemplate = ct;
   }

   public void setCamelConfigLocation(final String _c){
      this.camelConfigLocation = _c;
   }

   @Override
   public void sendEmplacements(final Recepteur re, final List<StorageMovement> stoE, final Utilisateur u){
      if(re != null && stoE != null && u != null){
         final InnerEnvVariables vars = new InnerEnvVariables();

         initVarFromStorageRobotBundle(vars);

         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final ByteArrayOutputStream baosM = new ByteArrayOutputStream();

         try{
            makeCSVfromMap(re, baos, stoE, vars.getSeparator());

            final String filename = vars.getFilename()
               + new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss").format(Calendar.getInstance().getTime()) + ".csv";

            writeOneRecetteLine(baosM, filename, u, vars.getSeparator());

            // depot du fichier
            camelTemplate.sendBodyAndHeader("direct:storage-robot-recette", baos, "CamelFileName", filename);

            // append to master
            camelTemplate.sendBody("direct:storage-robot-master", baosM);

         }catch(final Exception e){
            log.error(e.getMessage());
            throw new TKException(e.getMessage());
         }finally{
            if(baos != null){
               try{
                  baos.close();
               }catch(final IOException e){}
            }
            if(baosM != null){
               try{
                  baosM.close();
               }catch(final IOException e){}
            }
         }
      }
   }

   @Override
   public void writeOneRecetteLine(final ByteArrayOutputStream baos, final String filename, final Utilisateur user,
      final String separator) throws IOException{

      if(filename != null && user != null){
         baos.write(filename.getBytes());
         baos.write(separator.getBytes());
         baos.write(user.getLogin().getBytes());
         baos.write("\n".getBytes());
      }
   }

   @Override
   public void makeCSVfromMap(final Recepteur re, final ByteArrayOutputStream baos, final List<StorageMovement> storageMvts,
      final String separator) throws IOException{

      int currentRow = 0;

      for(final StorageMovement stE : storageMvts){
         log.debug("StorageMovement: {}", stE);
         currentRow++;
         // 1st col = increment
         baos.write(String.valueOf(currentRow).getBytes());
         baos.write(separator.getBytes());
         // 2nd col = barcode
         baos.write(stE.getBarcode().getBytes());
         baos.write(separator.getBytes());
         // adrl split
         printAdrl(stE.getAdrl(), re, baos, separator);
         baos.write(separator.getBytes());
         // dest adrl split
         printAdrl(stE.getDestAdrl(), re, baos, separator);
         baos.write(separator.getBytes());
         baos.write("\n".getBytes());
      }

      // complete to 1000 as IRELEC demands
      //		if (nbLinesToBeProvide > 0) {
      //			log.debug("providing additive lines: " + (nbLinesToBeProvide - currentRow));
      //			while (currentRow < nbLinesToBeProvide) {
      //				currentRow++;
      //				baos.write(String.valueOf(currentRow).getBytes());
      //				// writes separators for increment and barcodes
      //				baos.write(writeEmptySeparators(separator, 2).getBytes());
      //				// writes separators for empty adrls
      //				printAdrl(null, re, baos, separator);
      //				baos.write(separator.getBytes());
      //				printAdrl(null, re, baos, separator);
      //				baos.write("\n".getBytes());
      //			}
      //		}
   }

   /**
    * Checks adrl is coherent with recepteur and prints it.
    * If adrl matches single integer = transport rack position -> completes with leading 0.0.0.
    * according to IRELEC specs.
    * @param _a adrl
    * @param re recepteur
    * @param baos stream
    * @param separator
    * @throws IOException
    */
   private void printAdrl(String _a, final Recepteur re, final ByteArrayOutputStream baos, final String separator)
      throws IOException{
      if(_a != null){
         log.debug("print adr: " + _a);
         if(_a.matches("[0-9]+")){ // rack transport integer position
            _a = "0.0.0." + _a;
         }
         final String[] splitted = _a.split("\\.");
         // IRELEC check
         if(splitted.length != 4 && re.getLogiciel().getNom().equals("IRELEC")){
            throw new RuntimeException("storage.robot.emplacement.adrl.incompatible");
         }
         for(int i = 0; i < splitted.length; i++){
            baos.write(splitted[i].getBytes());
            if(i < splitted.length - 1){ // skip last separator
               baos.write(separator.getBytes());
            }
         }
      }else{
         baos.write(writeEmptySeparators(separator, 3).getBytes());
      }
   }

   //	@Override
   //	public void makeCSVfromDeplacementMap(Recepteur re, ByteArrayOutputStream baos,
   //			Map<TKStockableObject, EmplacementDecorator> tkEmpls, OperationType oType,
   //			String separator, int nbLinesToBeProvide) throws IOException {
   //
   //		List<StorageEmplacement> stoEmplacements = new ArrayList<StorageEmplacement>();
   //		Iterator<TKStockableObject> it = tkEmpls.keySet().iterator();
   //		TKStockableObject tko;
   //		Emplacement emp;
   //		StorageEmplacement stoEmp;
   //		String[] posAddr;
   //		while (it.hasNext()) {
   //			tko = it.next();
   //			emp = tkEmpls.get(tko);
   //			if (emp != null) {
   //				posAddr = emplacementManager.getAdrlManager(emp, true).split("\\.");
   //				if (posAddr.length < 4 && re.getLogiciel().getNom().equals("IRELEC")) {
   //					throw new RuntimeException("storage.robot.emplacement.adrl.incompatible");
   //				}
   //				stoEmp = new StorageEmplacement(tko.getCode(), posAddr[0], posAddr[1],
   //						posAddr[posAddr.length - 2], posAddr[posAddr.length - 1]);
   //				stoEmplacements.add(stoEmp);
   //			}
   //		}
   //
   //		// sort by adrl to facilitate
   //		// robot handle
   //		Collections.sort(stoEmplacements);
   //		int currentRow = 0;
   //
   //		boolean isStockage = oType == null || oType.getNom().equals("Stockage");
   //
   //		for (StorageEmplacement stE : stoEmplacements) {
   //			if (stE.isComplete()) {
   //				currentRow++;
   //				baos.write(String.valueOf(currentRow).getBytes());
   //				baos.write(separator.getBytes());
   //				if (!isStockage) { // destockage
   //					baos.write(writeEmptySeparators(separator, 5).getBytes());
   //				}
   //				baos.write(stE.getBarcode().getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(stE.getConteneurCode().getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(stE.getRack().getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(stE.getBoite().getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(stE.getPosition().getBytes());
   //				baos.write("\n".getBytes());
   //			}
   //		}
   //		// complete to 1000 as IRELEC demands
   //		if (nbLinesToBeProvide > 0) {
   //			while (currentRow < nbLinesToBeProvide) {
   //				currentRow++;
   //				baos.write(String.valueOf(currentRow).getBytes());
   //				baos.write(separator.getBytes());
   //				if (!isStockage) { // destockage
   //					baos.write(writeEmptySeparators(separator, 5).getBytes());
   //				}
   //				baos.write(separator.getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write(separator.getBytes());
   //				baos.write("\n".getBytes());
   //			}
   //		}
   //	}

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

               // vars.setMasterfile(bundle.getString("masterfile"));
               vars.setFilename(bundle.getString("csvname"));
               vars.setSeparator(bundle.getString("csv.separator"));
               // vars.setNbLinesToBeProvide(Integer.valueOf(bundle.getString("nb.lines.tofill")));

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
            throw new RuntimeException("storageRobot.properties.not.found");
         }
      }
   }

   private class InnerEnvVariables
   {
      // private String masterfile = "NomRecette.csv";
      private String separator = "|";

      private String filename = "Recette_";
      //		private Integer nbLinesToBeProvide = -1;

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
   }
}