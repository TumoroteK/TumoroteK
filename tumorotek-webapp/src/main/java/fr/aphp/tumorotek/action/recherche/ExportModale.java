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
package fr.aphp.tumorotek.action.recherche;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.controller.AbstractFicheCombineController;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.annotation.TableAnnotation;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * Dessine la fiche modale permettant l'attente lors
 * d'un export.
 * Date: 12/01/2011.
 *
 * @author Pierre VENTADOUR
 * @version 2.0
 *
 */
public class ExportModale extends AbstractFicheCombineController
{

   private static Logger log = LoggerFactory.getLogger(ExportModale.class);

   private static final long serialVersionUID = 3224510698969716417L;

   private String entite;

   private List<Object> objs;

   private List<Banque> banques;

   private boolean isExportAnonyme;

   private Utilisateur user;

   private Progressmeter progress;

   private Row rowQuestion;

   private Row rowButtons;

   private Row rowExport;

   private Label pourcentageLabel;

   private Checkbox exportBox;

   private Button oui;

   private Button non;

   private int pourcentage = 0;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);
      //AnnotateDataBinder binder = new AnnotateDataBinder(comp);
      //binder.loadComponent(comp);
      getBinder().loadAll();
   }

   /**
    * Initialise le composant à partir des paramètres d'affichage.
    * @param message avertissement affiché à l'utilisateur
    * @param fantomable indique si le textbox de commentaire doit
    * être affiché.
    * @param deletable inique si l'avertissement seul doit être affiché.
    * @param component parent ayant demandé la modale.
    */
   public void init(final String ent, final List<Object> o, final List<Banque> b, final boolean e, final Utilisateur u){
      entite = ent;
      objs.addAll(o);
      banques = b;
      isExportAnonyme = e;
      user = u;
      pourcentage = 0;
   }

   public void onClick$oui(final Event ev){
      //getBinder().loadComponent(self);
      rowQuestion.setVisible(false);
      rowButtons.setVisible(false);
      rowExport.setVisible(true);

      //launchExport();
      Events.echoEvent("onLaunchExport", self, null);
   }

   public void onCheck$exportBox(){
      if(exportBox.isChecked()){
         oui.setVisible(false);
         non.setVisible(true);
         //launchExport();
      }else{
         oui.setVisible(true);
         non.setVisible(false);
      }
   }

   public void onClick$non(){
      // fermeture de la fenêtre
      Events.postEvent(new Event("onClose", self.getRoot()));
   }

   public void onLaunchExport(){
      HSSFWorkbook wb = null;
      if(entite.equals("Patient")){

      }else if(entite.equals("Prelevement")){
         wb = exportPrelevements();
      }else if(entite.equals("Echantillon")){

      }else if(entite.equals("ProdDerive")){

      }

      // download du fichier excell
      final StringBuffer sb = new StringBuffer();
      final Calendar cal = Calendar.getInstance();
      final String date = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());
      sb.append(ObjectTypesFormatters.getLabel("export.prelevements.filename", new String[] {date}));
      sb.append(".xls");

      // fermeture de la fenêtre
      //Events.postEvent(new Event("onClose", self.getRoot()));

      downloadExportFile(wb, sb.toString());
   }

   public HSSFWorkbook exportPrelevements(){
      // création de la feuille excell
      final HSSFWorkbook wb = ManagerLocator.getExportUtils().createExcellWorkBook("Prelevements");
      final HSSFSheet sheet = wb.getSheetAt(0);

      // ajout des entêtes du ficher
      int nbRow = addEnteteForExportFile(sheet, "export.prelevements", banques);
      // Récupération des entêtes des colonnes pour le prlvt
      List<String> entetes = new ArrayList<>();
      entetes = addEnteteForPrelevements(entetes);
      // récupération des annotations des prlvts
      Entite entite = ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0);
      final List<ChampAnnotation> casPrlvt = getAnnotationsByBanquesAndEntite(banques, entite);
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPrlvt.size(); i++){
         entetes.add(casPrlvt.get(i).getNom());
      }
      // ajout des entetes pour les patients
      entetes = addEnteteForPatients(entetes);
      // récupération des annotations des patients
      entite = ManagerLocator.getEntiteManager().findByNomManager("Patient").get(0);
      final List<ChampAnnotation> casPatient = getAnnotationsByBanquesAndEntite(banques, entite);
      // ajout du nom des annotations aux entêtes de colonnes
      for(int i = 0; i < casPatient.size(); i++){
         entetes.add(casPatient.get(i).getNom());
      }
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, nbRow, entetes);
      nbRow++;

      pourcentage = 0;
      // pour chaque prlvt sélectionné
      for(int i = 0; i < objs.size(); i++){
         final int tmp = (i * 100) / objs.size();
         if(pourcentage != tmp){
            pourcentage = tmp;
            pourcentageLabel.setValue(String.valueOf(pourcentage));
            getBinder().loadComponent(pourcentageLabel);
         }
         final Prelevement prlvt = (Prelevement) objs.get(i);
         // ajout de ses données dans une nouvelle ligne
         final HSSFRow row = sheet.createRow(i + nbRow);
         ManagerLocator.getExportUtils().addPrelevementData(row, wb, 0, prlvt, casPrlvt, isExportAnonyme, user);

         // si le prlvt a un patient
         if(prlvt.getMaladie() != null){
            // ajout des données du patient
            final int idxCell = row.getLastCellNum();
            ManagerLocator.getExportUtils().addMaladieData(row, wb, idxCell, prlvt.getMaladie(), casPatient, isExportAnonyme,
               null);
         }
      }

      return wb;
   }

   /**
    * Gère le download d'un fichier d'export.
    */
   public static void downloadExportFile(final HSSFWorkbook wb, final String fileName){
      ByteArrayOutputStream out = null;

      try{
         out = new ByteArrayOutputStream();
         wb.write(out);

         final AMedia media = new AMedia(fileName, "xls", "application/vnd.ms-excel", out.toByteArray());
         Filedownload.save(media);
      }catch(final Exception e){
         log.error("An error occurred: {}", e.toString()); 
      }finally{
         if(out != null){
            try{
               out.close();
            }catch(final IOException e){
               out = null;
            }
         }
      }
   }

   /*************************************************************************/
   /************************** EXPORT ***************************************/
   /*************************************************************************/

   /**
    * Ajoute les entêtes du fichier d'export.
    * @param sheet Sheet du fichier excell.
    * @param key Clé pour afficher la 1ère ligne.
    * @return Indice de la ligne.
    */
   public static int addEnteteForExportFile(final HSSFSheet sheet, final String key, final List<Banque> banques){
      final List<String> tmp = new ArrayList<>();
      // Ajoute du titre du fichier
      tmp.add(Labels.getLabel(key));
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 0, tmp);
      // saut d'une ligne
      tmp.clear();
      ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 1, tmp);

      int nbRow;
      // si une seule banque de sélectionnée
      if(banques.size() == 1){
         // ajout du nom de la banque
         tmp.clear();
         tmp.add(ObjectTypesFormatters.getLabel("export.collection", new String[] {banques.get(0).getNom()}));
         ManagerLocator.getExportUtils().addDataToRow(sheet, 0, 2, tmp);
         nbRow = 3;
      }else{
         nbRow = 2;
      }

      return nbRow;
   }

   /**
    * Récupère la liste des champs d'annotations.
    * @param banques
    * @param entite
    * @return
    */
   public static List<ChampAnnotation> getAnnotationsByBanquesAndEntite(final List<Banque> banques, final Entite entite){
      final List<ChampAnnotation> cas = new ArrayList<>();

      for(int i = 0; i < banques.size(); i++){
         final List<TableAnnotation> tas =
            ManagerLocator.getTableAnnotationManager().findByEntiteAndBanqueManager(entite, banques.get(i));

         for(int j = 0; j < tas.size(); j++){
            final Iterator<ChampAnnotation> it =
               ManagerLocator.getTableAnnotationManager().getChampAnnotationsManager(tas.get(j)).iterator();

            while(it.hasNext()){
               final ChampAnnotation ca = it.next();
               if(!cas.contains(ca)){
                  cas.add(ca);
               }
            }
         }
      }

      return cas;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un patient.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public static List<String> addEnteteForPatients(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Maladie.MaladieId"));
      entetes.add(Labels.getLabel("Champ.Maladie.Libelle"));
      entetes.add(Labels.getLabel("Champ.Maladie.Code"));
      entetes.add(Labels.getLabel("Champ.Maladie.DateDebut"));
      entetes.add(Labels.getLabel("Champ.Maladie.DateDiagnostic"));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("maladie.medecin.numero", new String[] {"3"}));
      entetes.add(Labels.getLabel("Champ.Patient.PatientId"));
      entetes.add(Labels.getLabel("Champ.Patient.Nip"));
      entetes.add(Labels.getLabel("Champ.Patient.NomNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.Nom"));
      entetes.add(Labels.getLabel("Champ.Patient.Prenom"));
      entetes.add(Labels.getLabel("Champ.Patient.DateNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.Sexe"));
      entetes.add(Labels.getLabel("Champ.Patient.VilleNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.PaysNaissance"));
      entetes.add(Labels.getLabel("Champ.Patient.PatientEtat"));
      entetes.add(Labels.getLabel("Champ.Patient.DateEtat"));
      entetes.add(Labels.getLabel("Champ.Patient.DateDeces"));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"1"}));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"2"}));
      entetes.add(ObjectTypesFormatters.getLabel("patient.medecin.numero", new String[] {"3"}));
      entetes.add(Labels.getLabel("Champ.Echantillon.Organe"));
      entetes.add(Labels.getLabel("patient.nbPrelevements"));
      entetes.add(Labels.getLabel("patient.date.creation"));
      entetes.add(Labels.getLabel("patient.utilisateur.creation"));

      return entetes;
   }

   /**
    * Ajoute à une liste les entêtes des colonnes pour l'export
    * d'un prlvt.
    * @param entetes Liste des entêtes.
    * @return Liste complétée des entêtes.
    */
   public static List<String> addEnteteForPrelevements(final List<String> entetes){

      entetes.add(Labels.getLabel("Champ.Prelevement.PrelevementId"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Banque"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Code"));
      entetes.add(Labels.getLabel("Champ.Prelevement.NumeroLabo"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Nature"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DatePrelevement"));
      entetes.add(Labels.getLabel("Champ.Prelevement.PrelevementType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Sterile"));
      entetes.add(Labels.getLabel("prelevement.etablissement"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ServicePreleveur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Preleveur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditNbr"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConditMilieu"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConsentType"));
      entetes.add(Labels.getLabel("Champ.Prelevement.ConsentDate"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DateDepart"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Transporteur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.TransportTemp"));
      entetes.add(Labels.getLabel("Champ.Prelevement.DateArrivee"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Operateur"));
      entetes.add(Labels.getLabel("Champ.Prelevement.Quantite"));
      entetes.add(Labels.getLabel("Champ.Prelevement.PatientNda"));
      entetes.add(Labels.getLabel("general.diagnostic"));
      entetes.add(Labels.getLabel("prelevement.nb.total.echantillons"));
      entetes.add(Labels.getLabel("prelevement.nb.echantillons.restants"));
      entetes.add(Labels.getLabel("Champ.Echantillon.Stockes"));
      entetes.add(Labels.getLabel("prelevement.age"));
      entetes.add(Labels.getLabel("prelevement.nbProdDerives"));
      entetes.add(Labels.getLabel("prelevement.date.creation"));
      entetes.add(Labels.getLabel("prelevement.utilisateur.creation"));

      return entetes;
   }

   public Progressmeter getProgress(){
      return progress;
   }

   public void setProgress(final Progressmeter progress){
      this.progress = progress;
   }

   public List<Object> getObjs(){
      return objs;
   }

   public void setObjs(final List<Object> o){
      this.objs = o;
   }

   public List<Banque> getBanques(){
      return banques;
   }

   public void setBanques(final List<Banque> b){
      this.banques = b;
   }

   public boolean isExportAnonyme(){
      return isExportAnonyme;
   }

   public void setExportAnonyme(final boolean e){
      this.isExportAnonyme = e;
   }

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur u){
      this.user = u;
   }

   public String getEntite(){
      return entite;
   }

   public void setEntite(final String e){
      this.entite = e;
   }

   @Override
   public void cloneObject(){}

   @Override
   public void createNewObject(){}

   @Override
   public void onClick$addNewC(){}

   @Override
   public void onClick$editC(){}

   @Override
   public void setEmptyToNulls(){}

   @Override
   public void setFieldsToUpperCase(){}

   @Override
   public void setFocusOnElement(){}

   @Override
   public void switchToStaticMode(){}

   @Override
   public void updateObject(){}

   @Override
   public TKdataObject getObject(){
      return null;
   }

   @Override
   public TKdataObject getParentObject(){
      return null;
   }

   @Override
   public void setParentObject(final TKdataObject obj){}

   public int getPourcentage(){
      return pourcentage;
   }

   public void setPourcentage(final int p){
      this.pourcentage = p;
   }

   @Override
   public String getDeleteWaitLabel(){
      return null;
   }

   @Override
   public boolean prepareDeleteObject(){
      return false;
   }

   @Override
   public void removeObject(final String comments){}

}