package fr.aphp.tumorotek.manager.test.procedure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import fr.aphp.tumorotek.dao.coeur.patient.PatientDao;
import fr.aphp.tumorotek.dao.utilisateur.UtilisateurDao;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.procedure.ProcedureManager;
import fr.aphp.tumorotek.manager.test.AbstractManagerTest;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

public class ProcedureManagerTest extends AbstractManagerTest
{

   private ProcedureManager procedureManager;
   private PatientDao patientDao;
   private UtilisateurDao utilisateurDao;
   private MaladieManager maladieManager;

   public ProcedureManagerTest(){

   }

   public void setProcedureManager(final ProcedureManager pManager){
      this.procedureManager = pManager;
   }

   public void setPatientDao(final PatientDao pDao){
      this.patientDao = pDao;
   }

   public void setMaladieManager(final MaladieManager mManager){
      this.maladieManager = mManager;
   }

   public void setUtilisateurDao(final UtilisateurDao uDao){
      this.utilisateurDao = uDao;
   }

   public void testExecuteProcedure() throws IOException{
      executeFusionHemato(false);
   }

   /**
    * Exécute la fusion des patients pour le service Hématologie
    * de l'hopital St Louis.
    * @param doFusion
    * @throws IOException
    */
   public void executeFusionHemato(final boolean doFusion) throws IOException{
      final Utilisateur utilisateur = utilisateurDao.findById(1);

      final File file = new File("src/test/java/fr/aphp/tumorotek/manager/" + "test/procedure/tableau_pour_fusion.xls");

      HSSFWorkbook wb;
      HSSFSheet sheet;
      Iterator<Row> rit;
      HSSFRow row = null;

      // fichier de rapport
      final FileWriter fwErrors = new FileWriter("src/test/java/fr/aphp/tumorotek/manager/" + "test/procedure/logErrors.xls");
      final BufferedWriter bwErrors = new BufferedWriter(fwErrors);

      // ouvre fichier xls
      try(FileInputStream fis = new FileInputStream(file);){
         wb = new HSSFWorkbook(fis);
         sheet = wb.getSheetAt(0);
         // on se positionne sur la ligne d'entête
         rit = sheet.rowIterator();
         row = (HSSFRow) rit.next();
         row = (HSSFRow) rit.next();

         while(rit.hasNext()){
            row = (HSSFRow) rit.next();
            final int idPat1 = (int) row.getCell(0).getNumericCellValue();
            final int idPat2 = (int) row.getCell(4).getNumericCellValue();

            final Patient pat1 = patientDao.findById(idPat1);
            final Patient pat2 = patientDao.findById(idPat2);

            final Set<Maladie> malP = maladieManager.getMaladiesManager(pat2);
            final List<Maladie> malA = new ArrayList<>(maladieManager.getMaladiesManager(pat1));
            // Ajoute la maladie si n'existe pas sinon ajoute ses prels
            Maladie maladie;
            final Iterator<Maladie> malIt = malP.iterator();
            final StringBuffer sb = new StringBuffer();
            sb.append("Ligne " + row.getRowNum());
            sb.append("\t");
            sb.append(pat1.getNom());
            sb.append("\t");
            for(int i = 0; i < malA.size(); i++){
               sb.append(malA.get(i).getLibelle());
               if(i < malA.size() - 1){
                  sb.append(", ");
               }else{
                  sb.append("\t");
               }
            }
            boolean multiMaladies = false;
            while(malIt.hasNext()){
               maladie = malIt.next();
               maladie.setPatient(pat1); // pour appliquer equals()
               if(!malA.contains(maladie)){ // ajoute la maladie
                  multiMaladies = true;
                  sb.append(maladie.getLibelle());
                  if(malIt.hasNext()){
                     sb.append(", ");
                  }
               }
            }
            if(multiMaladies){
               bwErrors.write(sb.toString());
               bwErrors.newLine();
            }

            if(pat1 == null){
               final StringBuffer error = new StringBuffer();
               error.append("Ligne ");
               error.append(row.getRowNum() + 1);
               error.append(" : Le patient avec l'id ");
               error.append(idPat1);
               error.append(" est introuvable.");
               System.out.println(error.toString());
            }else if(pat2 == null){
               final StringBuffer error = new StringBuffer();
               error.append("Ligne ");
               error.append(row.getRowNum() + 1);
               error.append(" : Le patient avec l'id ");
               error.append(idPat2);
               error.append(" est introuvable.");
               System.out.println(error.toString());
            }else{
               // on met tous les champs vides à null pour
               // que le patient passe les validations
               if(pat1.getNom() != null && pat1.getNom().equals("")){
                  pat1.setNom(null);
               }
               if(pat1.getNip() != null && pat1.getNip().equals("")){
                  pat1.setNip(null);
               }
               if(pat1.getNomNaissance() != null && pat1.getNomNaissance().equals("")){
                  pat1.setNomNaissance(null);
               }
               if(pat1.getPrenom() != null && pat1.getPrenom().equals("")){
                  pat1.setPrenom(null);
               }
               if(pat1.getPaysNaissance() != null && pat1.getPaysNaissance().equals("")){
                  pat1.setPaysNaissance(null);
               }
               if(pat1.getVilleNaissance() != null && pat1.getVilleNaissance().equals("")){
                  pat1.setVilleNaissance(null);
               }

               if(doFusion){
                  procedureManager.fusionPatientForHematoManager(pat1, pat2, utilisateur,
                     "Nettoyage automatique des " + "doublons de patients");
               }
            }
         }
      }catch(final FileNotFoundException e){
         e.printStackTrace();
      }catch(final IOException e){
         e.printStackTrace();
      }

      bwErrors.close();
      fwErrors.close();
      System.out.println("Fusions OK.");
   }

}
