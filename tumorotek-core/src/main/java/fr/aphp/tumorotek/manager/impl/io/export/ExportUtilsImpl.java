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
package fr.aphp.tumorotek.manager.impl.io.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.ObjetStatutManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.cession.CederObjetManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.patient.MaladieManager;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.export.ExportUtils;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.systeme.EntiteManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.qualite.Operation;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

public class ExportUtilsImpl implements ExportUtils
{
   //TODO JavaDoc
   // private Log log = LogFactory.getLog(ExportUtils.class);

   private MaladieManager maladieManager;

   private PatientManager patientManager;

   private PrelevementManager prelevementManager;

   private EchantillonManager echantillonManager;

   private ProdDeriveManager prodDeriveManager;

   private CederObjetManager cederObjetManager;

   private EntiteManager entiteManager;

   private AnnotationValeurManager annotationValeurManager;

   private OperationManager operationManager;

   private CodeAssigneManager codeAssigneManager;

   private ObjetStatutManager objetStatutManager;

   private OperationTypeDao operationTypeDao;

   private ObjetNonConformeManager objetNonConformeManager;

   private static short CELLSTYLEDATESHORT = 22;

   private static short CELLSTYLEDATELONG = 21;

   public void setMaladieManager(final MaladieManager mManager){
      this.maladieManager = mManager;
   }

   public void setPatientManager(final PatientManager pManager){
      this.patientManager = pManager;
   }

   public void setPrelevementManager(final PrelevementManager pManager){
      this.prelevementManager = pManager;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setCederObjetManager(final CederObjetManager cManager){
      this.cederObjetManager = cManager;
   }

   public void setEntiteManager(final EntiteManager eManager){
      this.entiteManager = eManager;
   }

   public void setAnnotationValeurManager(final AnnotationValeurManager aManager){
      this.annotationValeurManager = aManager;
   }

   public void setOperationManager(final OperationManager oManager){
      this.operationManager = oManager;
   }

   public void setCodeAssigneManager(final CodeAssigneManager cManager){
      this.codeAssigneManager = cManager;
   }

   public void setObjetStatutManager(final ObjetStatutManager oManager){
      this.objetStatutManager = oManager;
   }

   public void setOperationTypeDao(final OperationTypeDao oDao){
      this.operationTypeDao = oDao;
   }

   public void setObjetNonConformeManager(final ObjetNonConformeManager oManager){
      this.objetNonConformeManager = oManager;
   }

   @Override
   public HSSFWorkbook createExcellWorkBook(final String name){
      final HSSFWorkbook wb = new HSSFWorkbook();
      wb.createSheet(name);

      final HSSFCellStyle dateStyle = wb.createCellStyle();
      dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

      final HSSFCellStyle dateStyleShort = wb.createCellStyle();
      dateStyleShort.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

      return wb;
   }

   @Override
   public int addCell(int indCell, final HSSFRow row, final Object value){
      final HSSFCell cell = row.createCell((short) indCell);
      ++indCell;

      if(value != null){
         if(value instanceof Integer){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Integer) value);
         }else if(value instanceof Float){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Float) value);
         }else if(value instanceof Double){
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue((Double) value);
         }else if(value instanceof Boolean){
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue((Boolean) value);
         }else{ // String
            cell.setCellType(CellType.STRING);
            cell.setCellValue(new HSSFRichTextString((String) value));
         }
      }else{
         cell.setCellType(CellType.BLANK);
         cell.setCellValue(new HSSFRichTextString());
      }
      return indCell;
   }

   @Override
   public int addDateCell(int indCell, final HSSFRow row, final Object value, final HSSFWorkbook wb){
      final HSSFCell cell = row.createCell((short) indCell);
      ++indCell;
      cell.setCellType(CellType.STRING);
      if(value != null){
         if(value instanceof Date){
            cell.setCellStyle(wb.getCellStyleAt(CELLSTYLEDATESHORT));
            cell.setCellValue((Date) value);
         }else if(value instanceof Calendar){
            cell.setCellValue((Calendar) value);
            if(((Calendar) value).get(Calendar.HOUR_OF_DAY) > 0 || ((Calendar) value).get(Calendar.MINUTE) > 0
               || ((Calendar) value).get(Calendar.SECOND) > 0){
               cell.setCellStyle(wb.getCellStyleAt(CELLSTYLEDATELONG));
            }else{
               cell.setCellStyle(wb.getCellStyleAt(CELLSTYLEDATESHORT));
            }
         }
      }else{
         cell.setCellType(CellType.BLANK);
         cell.setCellValue(new HSSFRichTextString());
      }
      return indCell;
   }

   @Override
   public HSSFSheet addDataToRow(final HSSFSheet sheet, int indCell, final int indRow, final List<? extends Object> values){
      final HSSFRow row = sheet.createRow(indRow);

      for(int i = 0; i < values.size(); i++){
         indCell = addCell(indCell, row, values.get(i));
      }

      return sheet;
   }

   @Override
   public void addDataToRowCSV(final BufferedWriter buf, final int indx, final List<String> values, final String fsep,
      final String lsep) throws IOException{

      final StringBuilder bd = new StringBuilder();
      for(int i = 0; i < indx; i++){
         bd.append(fsep);
      }
      final Iterator<String> it = values.iterator();
      String nxt;
      while(it.hasNext()){
         nxt = it.next();
         if(nxt != null){
            bd.append(nxt);
         }else{
            bd.append("");
         }
         if(it.hasNext()){
            bd.append(fsep);
         }
      }
      bd.append(lsep);
      buf.write(bd.toString());
   }

   @Override
   public void addMaladieData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final Maladie maladie,
      final List<ChampAnnotation> champAnnotations, final boolean isAnnonyme, final Utilisateur user){

      if(!maladie.getSystemeDefaut()){
         indCell = addCell(indCell, row, maladie.getMaladieId());
         indCell = addCell(indCell, row, maladie.getLibelle());
         indCell = addCell(indCell, row, maladie.getCode());
         indCell = addDateCell(indCell, row, maladie.getDateDebut(), wb);
         indCell = addDateCell(indCell, row, maladie.getDateDiagnostic(), wb);
      }else{
         indCell = addCell(indCell, row, null);
         indCell = addCell(indCell, row, null);
         indCell = addCell(indCell, row, null);
         indCell = addCell(indCell, row, null);
         indCell = addCell(indCell, row, null);
      }

      final Iterator<Collaborateur> it = maladieManager.getCollaborateursManager(maladie).iterator();
      int cpt = 0;
      if(!maladie.getSystemeDefaut()){
         while(it.hasNext()){
            final Collaborateur medecin = it.next();
            if(cpt < 3){
               indCell = addCell(indCell, row, medecin.getNom());
               ++cpt;
            }
         }
      }
      for(int i = cpt; i < 3; i++){
         indCell = addCell(indCell, row, null);
      }
      // Id du patient
      if(!isAnnonyme){
         indCell = addCell(indCell, row, maladie.getPatient().getPatientId());
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // NIP
      if(!isAnnonyme){
         if(maladie.getPatient().getNip() != null){
            indCell = addCell(indCell, row, maladie.getPatient().getNip());
         }else{
            indCell = addCell(indCell, row, null);
         }
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // Nom de naissance
      if(!isAnnonyme){
         if(maladie.getPatient().getNomNaissance() != null){
            indCell = addCell(indCell, row, maladie.getPatient().getNomNaissance());
         }else{
            indCell = addCell(indCell, row, null);
         }
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // Nom
      if(!isAnnonyme){
         if(maladie.getPatient().getNom() != null){
            indCell = addCell(indCell, row, maladie.getPatient().getNom());
         }else{
            indCell = addCell(indCell, row, null);
         }
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // Prénom
      if(!isAnnonyme){
         if(maladie.getPatient().getPrenom() != null){
            indCell = addCell(indCell, row, maladie.getPatient().getPrenom());
         }else{
            indCell = addCell(indCell, row, null);
         }
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // Date de naissance
      if(!isAnnonyme){
         indCell = addDateCell(indCell, row, maladie.getPatient().getDateNaissance(), wb);
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }

      // Sexe
      if(maladie.getPatient().getSexe() != null){
         indCell = addCell(indCell, row, maladie.getPatient().getSexe());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Ville de naissance
      if(maladie.getPatient().getVilleNaissance() != null){
         indCell = addCell(indCell, row, maladie.getPatient().getVilleNaissance());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Pays de naissance
      if(maladie.getPatient().getPaysNaissance() != null){
         indCell = addCell(indCell, row, maladie.getPatient().getPaysNaissance());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Etat
      if(maladie.getPatient().getPatientEtat() != null){
         indCell = addCell(indCell, row, maladie.getPatient().getPatientEtat());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Date de l'état
      indCell = addDateCell(indCell, row, maladie.getPatient().getDateEtat(), wb);
      // Date de décès
      indCell = addDateCell(indCell, row, maladie.getPatient().getDateDeces(), wb);

      // Médecins
      final List<Collaborateur> medecins = patientManager.getMedecinsManager(maladie.getPatient());
      cpt = 0;
      for(int i = 0; i < medecins.size(); i++){
         if(i < 3){
            indCell = addCell(indCell, row, medecins.get(i).getNom());
            ++cpt;
         }
      }
      for(int i = cpt; i < 3; i++){
         indCell = addCell(indCell, row, null);
      }

      // Organe
      final List<String> codes = codeAssigneManager
         .formatCodesAsStringsManager(codeAssigneManager.findCodesOrgExportedByPatientManager(maladie.getPatient()));
      final StringBuffer sb = new StringBuffer();
      if(codes.size() > 0){
         sb.append(codes.get(0));
      }
      indCell = addCell(indCell, row, sb.toString());

      // Nb de prlvts
      final Long nbPrlvts = patientManager.getTotPrelevementsCountManager(maladie.getPatient());
      indCell = addCell(indCell, row, new Integer(nbPrlvts.intValue()));

      // date de création
      final Calendar dateCreation = operationManager.findDateCreationManager(maladie.getPatient());
      indCell = addDateCell(indCell, row, dateCreation, wb);

      // Utilisateur
      final Operation creation = operationManager.findOperationCreationManager(maladie.getPatient());
      if(creation != null){
         indCell = addCell(indCell, row, creation.getUtilisateur().getLogin());
      }else{
         indCell = addCell(indCell, row, null);
      }

      addAnnotationData(row, wb, indCell, maladie.getPatient(), champAnnotations);

      // Enregistrement de l'operation associee
      if(user != null){
         final Operation exportOp = new Operation();
         exportOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(exportOp, user, operationTypeDao.findByNom("Export").get(0), maladie.getPatient());
      }
   }

   /**
    * Cree et ajoute les cellules contenant les valeurs d'annotations pour un
    * objet donné et les champs passés en paramètres.
    *
    * @param row
    * @param wb
    * @param indCell
    * @param obj
    *            TKAnnotableObject
    * @param champAnnotations
    */
   private void addAnnotationData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final TKAnnotableObject obj,
      final List<ChampAnnotation> champAnnotations){
      // Annotations
      if(champAnnotations != null){
         for(int i = 0; i < champAnnotations.size(); i++){
            final List<AnnotationValeur> avs = annotationValeurManager.findByChampAndObjetManager(champAnnotations.get(i), obj);
            if(!avs.isEmpty()){
               if(champAnnotations.get(i).getDataType().getType().matches("date.*")){
                  indCell = addDateCell(indCell, row, avs.get(0).getValeur(), wb);
               }else if(!champAnnotations.get(i).getDataType().getType().equals("thesaurusM")){
                  indCell = addCell(indCell, row, avs.get(0).getValeur());
               }else{ // thesaurus multiple -> concat item labels
                  final StringBuffer sb = new StringBuffer();
                  for(int j = 0; j < avs.size(); j++){
                     sb.append(avs.get(j).getValeur());
                     if(j + 1 < avs.size()){
                        sb.append(";");
                     }
                  }
                  indCell = addCell(indCell, row, sb.toString());
               }
            }else{
               indCell = addCell(indCell, row, null);
            }
         }
      }
   }

   @Override
   public void addPrelevementData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final Prelevement prelevement,
      final List<ChampAnnotation> champAnnotations, final boolean isAnnonyme, final Utilisateur user){
      // Id du prlvt
      indCell = addCell(indCell, row, prelevement.getPrelevementId());
      // banque
      indCell = addCell(indCell, row, prelevement.getBanque().getNom());
      // Code
      indCell = addCell(indCell, row, prelevement.getCode());
      // Numero labo
      indCell = addCell(indCell, row, prelevement.getNumeroLabo());
      // Nature
      if(prelevement.getNature() != null) {
         indCell = addCell(indCell, row, prelevement.getNature().getNom());
      }
      else {
         indCell = addCell(indCell, row, null);
      }
      // Date prélèvement
      indCell = addDateCell(indCell, row, prelevement.getDatePrelevement(), wb);
      // Type de prélèvement
      if(prelevement.getPrelevementType() != null){
         indCell = addCell(indCell, row, prelevement.getPrelevementType().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Stérilité
      indCell = addCell(indCell, row, prelevement.getSterile());
      // Risques
      final Iterator<Risque> risksIt = prelevementManager.getRisquesManager(prelevement).iterator();
      final StringBuffer riskb = new StringBuffer();
      while(risksIt.hasNext()){
         riskb.append(risksIt.next().getNom());
         if(risksIt.hasNext()){
            riskb.append(", ");
         }
      }
      indCell = addCell(indCell, row, riskb.toString());

      // conformité oui / non
      indCell = addCell(indCell, row, prelevement.getConformeArrivee());
      // conformité raison
      if(prelevement.getConformeArrivee() != null && !prelevement.getConformeArrivee()){
         final StringBuffer nonConf = new StringBuffer();
         final List<ObjetNonConforme> list = objetNonConformeManager.findByObjetAndTypeManager(prelevement, "Arrivee");

         for(int i = 0; i < list.size(); i++){
            nonConf.append(list.get(i).getNonConformite().getNom());
            if(i < list.size() - 1){
               nonConf.append(", ");
            }else{
               nonConf.append(".");
            }
         }
         indCell = addCell(indCell, row, nonConf.toString());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Etablissement
      if(prelevement.getServicePreleveur() != null){
         indCell = addCell(indCell, row, prelevement.getServicePreleveur().getEtablissement().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Service
      if(prelevement.getServicePreleveur() != null){
         indCell = addCell(indCell, row, prelevement.getServicePreleveur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Préleveur
      if(prelevement.getPreleveur() != null){
         indCell = addCell(indCell, row, prelevement.getPreleveur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Type de conditionnement
      if(prelevement.getConditType() != null){
         indCell = addCell(indCell, row, prelevement.getConditType().getType());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Nbr conditionnement
      indCell = addCell(indCell, row, prelevement.getConditNbr());
      // Milieu conditionnement
      if(prelevement.getConditMilieu() != null){
         indCell = addCell(indCell, row, prelevement.getConditMilieu().getMilieu());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Statut juridique
      if(prelevement.getConsentType() != null){
         indCell = addCell(indCell, row, prelevement.getConsentType().getType());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // date de consentement
      indCell = addDateCell(indCell, row, prelevement.getConsentDate(), wb);
      // date départ
      indCell = addDateCell(indCell, row, prelevement.getDateDepart(), wb);
      // transporteur
      if(prelevement.getTransporteur() != null){
         indCell = addCell(indCell, row, prelevement.getTransporteur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // température de transport
      indCell = addCell(indCell, row, prelevement.getTransportTemp());
      // date arrivée
      indCell = addDateCell(indCell, row, prelevement.getDateArrivee(), wb);
      // opérateur
      if(prelevement.getOperateur() != null){
         indCell = addCell(indCell, row, prelevement.getOperateur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // quantité
      String quantite = null;
      if(prelevement.getQuantite() != null){
         quantite = String.valueOf(prelevement.getQuantite());
      }
      if(prelevement.getQuantiteUnite() != null){
         quantite = quantite + prelevement.getQuantiteUnite().getUnite();
      }
      indCell = addCell(indCell, row, quantite);
      // n° dossier
      if(!isAnnonyme){
         if(prelevement.getPatientNda() != null){
            indCell = addCell(indCell, row, prelevement.getPatientNda());
         }else{
            indCell = addCell(indCell, row, null);
         }
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // Diagnostic
      final List<String> codes =
         codeAssigneManager.formatCodesAsStringsManager(codeAssigneManager.findCodesLesExportedByPrelevementManager(prelevement));
      final StringBuffer sb = new StringBuffer();
      if(codes.size() > 0){
         sb.append(codes.get(0));
      }
      indCell = addCell(indCell, row, sb.toString());
      // nb echantillons totaux
      indCell = addCell(indCell, row, new Integer(prelevementManager.getEchantillonsManager(prelevement).size()));
      // nb echantillons restants
      indCell = addCell(indCell, row, new Integer(echantillonManager.findRestantsByPrelevementManager(prelevement).size()));
      // nb echans stockés
      final ObjetStatut stocke = objetStatutManager.findByStatutLikeManager("STOCKE", true).get(0);
      indCell =
         addCell(indCell, row, new Integer(echantillonManager.findByPrelevementAndStatutManager(prelevement, stocke).size()));
      // age au prlvt
      Integer age = null;
      if(prelevement.getMaladie() != null){
         final Patient pat = prelevement.getMaladie().getPatient();
         if(pat.getDateNaissance() != null && prelevement.getDatePrelevement() != null){
            final Calendar calPat = Calendar.getInstance();
            calPat.setTime(pat.getDateNaissance());
            final long diffMillis = prelevement.getDatePrelevement().getTimeInMillis() - calPat.getTimeInMillis();
            // ie 31536000000 = 365*24*60*60*1000
            // soit le nb de millisecondes dans une année
            final long div = 31536000000L;
            final int annees = (int) (diffMillis / div);
            age = new Integer(annees);
         }
      }
      indCell = addCell(indCell, row, age);
      // nb dérivés
      indCell = addCell(indCell, row, new Integer(prelevementManager.getProdDerivesManager(prelevement).size()));
      // date de création
      final Calendar dateCreation = operationManager.findDateCreationManager(prelevement);
      indCell = addDateCell(indCell, row, dateCreation, wb);
      // Utilisateur
      final Operation creation = operationManager.findOperationCreationManager(prelevement);
      if(creation != null){
         indCell = addCell(indCell, row, creation.getUtilisateur().getLogin());
      }else{
         indCell = addCell(indCell, row, null);
      }

      // Annotations
      addAnnotationData(row, wb, indCell, prelevement, champAnnotations);

      // Enregistrement de l'operation associee
      if(user != null){
         final Operation exportOp = new Operation();
         exportOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(exportOp, user, operationTypeDao.findByNom("Export").get(0), prelevement);
      }
   }

   @Override
   public void addEchantillonData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final Echantillon echantillon,
      final List<ChampAnnotation> champAnnotations, final boolean isAnnonyme, final Utilisateur user){
      // Id échantillon
      indCell = addCell(indCell, row, echantillon.getEchantillonId());
      // Banque
      indCell = addCell(indCell, row, echantillon.getBanque().getNom());
      // code
      indCell = addCell(indCell, row, echantillon.getCode());
      // Type
      indCell = addCell(indCell, row, echantillon.getEchantillonType().getType());
      // quantité
      String quantite = "";
      if(echantillon.getQuantite() != null){
         quantite = String.valueOf(echantillon.getQuantite());
      }
      if(echantillon.getQuantiteUnite() != null){
         quantite = quantite + echantillon.getQuantiteUnite().getUnite();
      }
      indCell = addCell(indCell, row, quantite);
      // quantité initiale
      String quantiteInit = "";
      if(echantillon.getQuantiteInit() != null){
         quantiteInit = String.valueOf(echantillon.getQuantiteInit());
      }
      if(echantillon.getQuantiteUnite() != null){
         quantiteInit = quantiteInit + echantillon.getQuantiteUnite().getUnite();
      }
      indCell = addCell(indCell, row, quantiteInit);
      // date stockage
      indCell = addDateCell(indCell, row, echantillon.getDateStock(), wb);
      // delai cgl
      indCell = addCell(indCell, row, echantillon.getDelaiCgl());
      // opérateur
      if(echantillon.getCollaborateur() != null){
         indCell = addCell(indCell, row, echantillon.getCollaborateur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // emplacement
      if(!isAnnonyme){
         indCell = addCell(indCell, row, echantillonManager.getEmplacementAdrlManager(echantillon));
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // statut
      if(echantillon.getObjetStatut() != null){
         indCell = addCell(indCell, row, echantillon.getObjetStatut().getStatut());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // qualité
      if(echantillon.getEchanQualite() != null){
         indCell = addCell(indCell, row, echantillon.getEchanQualite().getEchanQualite());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // mode preparation
      if(echantillon.getModePrepa() != null){
         indCell = addCell(indCell, row, echantillon.getModePrepa().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // sterilité
      indCell = addCell(indCell, row, echantillon.getSterile());
      // conformité traitement oui/non
      indCell = addCell(indCell, row, echantillon.getConformeTraitement());
      // conformité traitement raison
      if(echantillon.getConformeTraitement() != null && !echantillon.getConformeTraitement()){
         final StringBuffer nonConf = new StringBuffer();
         final List<ObjetNonConforme> list = objetNonConformeManager.findByObjetAndTypeManager(echantillon, "Traitement");

         for(int i = 0; i < list.size(); i++){
            nonConf.append(list.get(i).getNonConformite().getNom());
            if(i < list.size() - 1){
               nonConf.append(", ");
            }else{
               nonConf.append(".");
            }
         }

         indCell = addCell(indCell, row, nonConf.toString());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // conformité cession oui/non
      indCell = addCell(indCell, row, echantillon.getConformeCession());
      // conformité cession raison
      if(echantillon.getConformeCession() != null && !echantillon.getConformeCession()){
         final StringBuffer nonConf = new StringBuffer();
         final List<ObjetNonConforme> list = objetNonConformeManager.findByObjetAndTypeManager(echantillon, "Cession");

         for(int i = 0; i < list.size(); i++){
            nonConf.append(list.get(i).getNonConformite().getNom());
            if(i < list.size() - 1){
               nonConf.append(", ");
            }else{
               nonConf.append(".");
            }
         }

         indCell = addCell(indCell, row, nonConf.toString());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // tumoral
      indCell = addCell(indCell, row, echantillon.getTumoral());
      // lateralite
      indCell = addCell(indCell, row, echantillon.getLateralite());

      // Organes
      List<String> codes =
         codeAssigneManager.formatCodesAsStringsManager(codeAssigneManager.findCodesOrganeByEchantillonManager(echantillon));
      int cpt = 0;
      for(int i = 0; i < codes.size(); i++){
         if(cpt < 3){
            indCell = addCell(indCell, row, codes.get(i));
            ++cpt;
         }
      }
      for(int i = cpt; i < 3; i++){
         indCell = addCell(indCell, row, null);
      }
      // Diagnostic
      codes = codeAssigneManager.formatCodesAsStringsManager(codeAssigneManager.findCodesMorphoByEchantillonManager(echantillon));
      cpt = 0;
      for(int i = 0; i < codes.size(); i++){
         if(cpt < 5){
            indCell = addCell(indCell, row, codes.get(i));
            ++cpt;
         }
      }
      for(int i = cpt; i < 5; i++){
         indCell = addCell(indCell, row, null);
      }
      // nb produits dérivés
      indCell = addCell(indCell, row, new Integer(echantillonManager.getProdDerivesManager(echantillon).size()));
      // date de création
      final Calendar dateCreation = operationManager.findDateCreationManager(echantillon);
      indCell = addDateCell(indCell, row, dateCreation, wb);

      // Utilisateur
      final Operation creation = operationManager.findOperationCreationManager(echantillon);
      if(creation != null){
         indCell = addCell(indCell, row, creation.getUtilisateur().getLogin());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Annotations
      addAnnotationData(row, wb, indCell, echantillon, champAnnotations);

      // Enregistrement de l'operation associee
      if(user != null){
         final Operation exportOp = new Operation();
         exportOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(exportOp, user, operationTypeDao.findByNom("Export").get(0), echantillon);
      }
   }

   @Override
   public void addProdDeriveData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final ProdDerive derive,
      final List<ChampAnnotation> champAnnotations, final boolean isAnnonyme, final Utilisateur user){
      // Id derive
      indCell = addCell(indCell, row, derive.getProdDeriveId());
      // Banque
      indCell = addCell(indCell, row, derive.getBanque().getNom());
      // code
      indCell = addCell(indCell, row, derive.getCode());
      // Type
      indCell = addCell(indCell, row, derive.getProdType().getType());
      // date transformation
      indCell = addDateCell(indCell, row, derive.getDateTransformation(), wb);
      // quantité
      String quantiteTransfo = "";
      if(derive.getTransformation() != null){
         if(derive.getTransformation().getQuantite() != null){
            quantiteTransfo = String.valueOf(derive.getTransformation().getQuantite());
         }
         if(derive.getTransformation().getQuantiteUnite() != null){
            quantiteTransfo = quantiteTransfo + derive.getTransformation().getQuantiteUnite();
         }
      }
      indCell = addCell(indCell, row, quantiteTransfo);
      // labo
      indCell = addCell(indCell, row, derive.getCodeLabo());
      // volume
      String volume = "";
      if(derive.getVolume() != null){
         volume = String.valueOf(derive.getVolume());
      }
      if(derive.getVolumeUnite() != null){
         volume = volume + derive.getVolumeUnite().getUnite();
      }
      indCell = addCell(indCell, row, volume);
      // volume init
      String volumeInit = "";
      if(derive.getVolumeInit() != null){
         volumeInit = String.valueOf(derive.getVolumeInit());
      }
      if(derive.getVolumeUnite() != null){
         volumeInit = volumeInit + derive.getVolumeUnite().getUnite();
      }
      indCell = addCell(indCell, row, volumeInit);
      // concentration
      String concentration = "";
      if(derive.getConc() != null){
         concentration = String.valueOf(derive.getConc());
      }
      if(derive.getConcUnite() != null){
         concentration = concentration + derive.getConcUnite().getUnite();
      }
      indCell = addCell(indCell, row, concentration);
      // quantité
      String quantite = "";
      if(derive.getQuantite() != null){
         quantite = String.valueOf(derive.getQuantite());
      }
      if(derive.getQuantiteUnite() != null){
         quantite = quantite + derive.getQuantiteUnite().getUnite();
      }
      indCell = addCell(indCell, row, quantite);
      // quantité init
      String quantiteInit = "";
      if(derive.getQuantiteInit() != null){
         quantiteInit = String.valueOf(derive.getQuantiteInit());
      }
      if(derive.getQuantiteUnite() != null){
         quantiteInit = quantiteInit + derive.getQuantiteUnite().getUnite();
      }
      indCell = addCell(indCell, row, quantiteInit);
      // date de stockage
      indCell = addDateCell(indCell, row, derive.getDateStock(), wb);
      // Préparation
      if(derive.getModePrepaDerive() != null){
         indCell = addCell(indCell, row, derive.getModePrepaDerive().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // qualité
      if(derive.getProdQualite() != null){
         indCell = addCell(indCell, row, derive.getProdQualite().getProdQualite());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // opérateur
      if(derive.getCollaborateur() != null){
         indCell = addCell(indCell, row, derive.getCollaborateur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // emplacement
      if(!isAnnonyme){
         indCell = addCell(indCell, row, prodDeriveManager.getEmplacementAdrlManager(derive));
      }else{
         indCell = addCell(indCell, row, "XXXXXXXX");
      }
      // statut
      if(derive.getObjetStatut() != null){
         indCell = addCell(indCell, row, derive.getObjetStatut().getStatut());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // conformité traitement oui/non
      indCell = addCell(indCell, row, derive.getConformeTraitement());
      // conformité traitement raison
      if(derive.getConformeTraitement() != null && !derive.getConformeTraitement()){
         final StringBuffer nonConf = new StringBuffer();
         final List<ObjetNonConforme> list = objetNonConformeManager.findByObjetAndTypeManager(derive, "Traitement");

         for(int i = 0; i < list.size(); i++){
            nonConf.append(list.get(i).getNonConformite().getNom());
            if(i < list.size() - 1){
               nonConf.append(", ");
            }else{
               nonConf.append(".");
            }
         }

         indCell = addCell(indCell, row, nonConf.toString());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // conformité cession oui/non
      indCell = addCell(indCell, row, derive.getConformeCession());
      // conformité cession raison
      if(derive.getConformeCession() != null && !derive.getConformeCession()){
         final StringBuffer nonConf = new StringBuffer();
         final List<ObjetNonConforme> list = objetNonConformeManager.findByObjetAndTypeManager(derive, "Cession");

         for(int i = 0; i < list.size(); i++){
            nonConf.append(list.get(i).getNonConformite().getNom());
            if(i < list.size() - 1){
               nonConf.append(", ");
            }else{
               nonConf.append(".");
            }
         }

         indCell = addCell(indCell, row, nonConf.toString());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // nb dérivés
      indCell = addCell(indCell, row, new Integer(prodDeriveManager.getProdDerivesManager(derive).size()));
      // date de création
      final Calendar dateCreation = operationManager.findDateCreationManager(derive);
      indCell = addDateCell(indCell, row, dateCreation, wb);
      // Utilisateur
      final Operation creation = operationManager.findOperationCreationManager(derive);
      if(creation != null){
         indCell = addCell(indCell, row, creation.getUtilisateur().getLogin());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Annotations
      addAnnotationData(row, wb, indCell, derive, champAnnotations);

      // Enregistrement de l'operation associee
      if(user != null){
         final Operation exportOp = new Operation();
         exportOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(exportOp, user, operationTypeDao.findByNom("Export").get(0), derive);
      }
   }

   @Override
   public void addCessionData(final HSSFRow row, final HSSFWorkbook wb, int indCell, final Cession cession,
      final List<ChampAnnotation> champAnnotations, final Utilisateur user){
      // Id cession
      indCell = addCell(indCell, row, cession.getCessionId());
      // Banque
      indCell = addCell(indCell, row, cession.getBanque().getNom());
      // code
      indCell = addCell(indCell, row, cession.getNumero());
      // Type
      indCell = addCell(indCell, row, cession.getCessionType().getType());
      // Echantillons
      final List<CederObjet> echansCedes = cederObjetManager.getEchantillonsCedesByCessionManager(cession);
      final StringBuffer echans = new StringBuffer();
      for(int i = 0; i < echansCedes.size(); i++){
         final Echantillon e = (Echantillon) entiteManager.findObjectByEntiteAndIdManager(echansCedes.get(i).getEntite(),
            echansCedes.get(i).getObjetId());
         if(e != null){
            echans.append(e.getCode());
            if(i + 1 < echansCedes.size()){
               echans.append("; ");
            }
         }
      }
      indCell = addCell(indCell, row, echans.toString());
      // nb échantillons
      indCell = addCell(indCell, row, new Integer(echansCedes.size()));
      // Derives
      final List<CederObjet> derivesCedes = cederObjetManager.getProdDerivesCedesByCessionManager(cession);
      final StringBuffer derives = new StringBuffer();
      for(int i = 0; i < derivesCedes.size(); i++){
         final ProdDerive d = (ProdDerive) entiteManager.findObjectByEntiteAndIdManager(derivesCedes.get(i).getEntite(),
            derivesCedes.get(i).getObjetId());
         if(d != null){
            derives.append(d.getCode());
            if(i + 1 < derivesCedes.size()){
               derives.append("; ");
            }
         }
      }
      indCell = addCell(indCell, row, derives.toString());
      // nb dérivés
      indCell = addCell(indCell, row, new Integer(derivesCedes.size()));
      // demandeur
      if(cession.getDemandeur() != null){
         indCell = addCell(indCell, row, cession.getDemandeur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // date de demande
      indCell = addDateCell(indCell, row, cession.getDemandeDate(), wb);
      // contrat
      if(cession.getContrat() != null){
         indCell = addCell(indCell, row, cession.getContrat().getNumero());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // etude
      indCell = addCell(indCell, row, cession.getEtudeTitre());
      // examen
      if(cession.getCessionExamen() != null){
         indCell = addCell(indCell, row, cession.getCessionExamen().getExamen());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // motif
      if(cession.getDestructionMotif() != null){
         indCell = addCell(indCell, row, cession.getDestructionMotif().getMotif());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // desc
      indCell = addCell(indCell, row, cession.getDescription());
      // etablissement
      if(cession.getDestinataire() != null && cession.getDestinataire().getEtablissement() != null){
         indCell = addCell(indCell, row, cession.getDestinataire().getEtablissement().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // service
      if(cession.getServiceDest() != null){
         indCell = addCell(indCell, row, cession.getServiceDest().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // destinataire
      if(cession.getDestinataire() != null){
         indCell = addCell(indCell, row, cession.getDestinataire().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // dateValidation
      indCell = addDateCell(indCell, row, cession.getValidationDate(), wb);
      // dateDestruction
      indCell = addDateCell(indCell, row, cession.getDestructionDate(), wb);
      // statut
      if(cession.getCessionStatut() != null){
         indCell = addCell(indCell, row, cession.getCessionStatut().getStatut());

      }else{
         indCell = addCell(indCell, row, null);
      }
      // executant
      if(cession.getExecutant() != null){
         indCell = addCell(indCell, row, cession.getExecutant().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // dateDepart
      indCell = addDateCell(indCell, row, cession.getDepartDate(), wb);
      // dateDestruction
      indCell = addDateCell(indCell, row, cession.getArriveeDate(), wb);
      // transporteur
      if(cession.getTransporteur() != null){
         indCell = addCell(indCell, row, cession.getTransporteur().getNom());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // temp
      indCell = addCell(indCell, row, cession.getTemperature());
      // obs
      indCell = addCell(indCell, row, cession.getObservations());
      // date de création
      final Calendar dateCreation = operationManager.findDateCreationManager(cession);
      indCell = addDateCell(indCell, row, dateCreation, wb);

      // Utilisateur
      final Operation creation = operationManager.findOperationCreationManager(cession);
      if(creation != null){
         indCell = addCell(indCell, row, creation.getUtilisateur().getLogin());
      }else{
         indCell = addCell(indCell, row, null);
      }
      // Annotations
      addAnnotationData(row, wb, indCell, cession, champAnnotations);

      // Enregistrement de l'operation associee
      if(user != null){
         final Operation exportOp = new Operation();
         exportOp.setDate(Utils.getCurrentSystemCalendar());
         operationManager.createObjectManager(exportOp, user, operationTypeDao.findByNom("Export").get(0), cession);
      }
   }

   /**************************************************************/
   /************************** FORMATTERS ************************/
   /**************************************************************/

   @Override
   public String dateRenderer(final Object date){
      if(date != null){
         SimpleDateFormat df;

         // par defaut
         df = new SimpleDateFormat("dd/MM/yyyy");

         if(date instanceof Calendar){
            if(((Calendar) date).get(Calendar.HOUR_OF_DAY) > 0 || ((Calendar) date).get(Calendar.MINUTE) > 0
               || ((Calendar) date).get(Calendar.SECOND) > 0){
               df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            }
            return df.format(((Calendar) date).getTime());
         }
         return df.format(date);
      }
      return null;
   }

   @Override
   public String booleanLitteralFormatter(final Boolean b){
      if(b != null){
         if(b.booleanValue()){
            return "Oui";
         }
         return "Non";
      }
      return null;
   }
}
