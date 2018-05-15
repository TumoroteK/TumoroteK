package fr.aphp.tumorotek.action.patient.bto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;

import fr.aphp.tumorotek.action.patient.FichePatientEdit;
import fr.aphp.tumorotek.action.patient.LabelCodeItem;
import fr.aphp.tumorotek.action.patient.PatientUtils;
import fr.aphp.tumorotek.model.coeur.patient.PatientMedecin;

public class FichePatientEditBTO extends FichePatientEdit
{

   /**
    * Selim Trabelsi
    */
   private static final long serialVersionUID = 1L;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{

      super.doAfterCompose(comp);

   }

   /**
    * Prepare les valeurs des attributs qui seront sauvées avec le 
    * bean patient.
    * Recupere la liste de referents depuis le composant embarqué.
    * @param boolean specifiant si la liste de medecins doit être passée
    * par le setter (utile si fiche embarquée dans prélèvement)
    */
   @Override
   public void prepareDataBeforeSave(final boolean setMedecins){
      setEmptyToNulls();
      setFieldsToUpperCase();
      recordDateEtatDeces();

      if(patient != null){
         if(patient.getNom() == null || patient.getNom() == ""){
            patient.setNom("Inconnu");
         }
         if(patient.getPrenom() == null){
            patient.setPrenom("Inconnu");
         }
         if(patient.getNomNaissance() == null){
            patient.setNomNaissance("Inconnu");
            // Groupe sanguin pass� en annotations
            //	            if(null != groupeSanguinBox && !groupeSanguinBox.getSelectedItems().isEmpty()){
            //	               patient.setGroupeSanguin(groupeSanguinBox.getSelectedItem().getLabel());
            //	            }
         }
      }

      this.medecins = getReferents().getMedecins();
      if(setMedecins && medecins.size() > 0){
         // cree la liste de PatientMedecins
         final Set<PatientMedecin> pmeds = new HashSet<>();
         PatientMedecin pm;
         for(int i = 0; i < medecins.size(); i++){
            pm = new PatientMedecin();
            pm.setPatient(this.patient);
            pm.setCollaborateur(medecins.get(i));
            pm.setOrdre(i + 1);
            pmeds.add(pm);
         }
         this.patient.setPatientMedecins(pmeds);
      }
   }

   @Override
   public List<LabelCodeItem> getEtats(){
      if(this.selectedSexe == null || !("F".equals(this.selectedSexe.getCode()))){
         final List<LabelCodeItem> l = PatientUtils.getEtats();
         return remove(l);
      }
      final List<LabelCodeItem> l = PatientUtils.getEtats();
      return remove(l);
   }
}
