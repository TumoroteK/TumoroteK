/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.model.contexte.gatsbi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;

public class SchemaVisites implements Serializable
{

private static final long serialVersionUID = 1L;
   
   private List<Visite> visites = new ArrayList<Visite>();
   
   public SchemaVisites(List<Visite> visites){
      super();
      this.visites = visites;
   }

   public List<Visite> getVisites() {
      return visites;
   }

   public void setVisites(List<Visite> _r) {
      this.visites = _r;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        SchemaVisites schema = (SchemaVisites) obj;

        return Objects.equals(visites, schema.getVisites());
   }
   
   @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
      result = prime * result + ((visites == null) ? 0 : visites.hashCode());
      return result;
   }
   
   public List<Maladie> produceMaladiesFromSchema(Patient patient, LocalDateTime fromDate, Banque banque) {
      List<Maladie> maladies = new ArrayList<Maladie>();
      
      if (patient != null && fromDate != null && banque != null) {
         Maladie maladie;
         LocalDateTime visiteDate = fromDate;
         for(Visite visite : visites){
            maladie = new Maladie();
            maladie.setVisite(visite);
            maladie.setBanque(banque);
            maladie.setPatient(patient);
            maladie.setLibelle(visite.getNom());
            switch(visite.getIntervalleType()){
               case JOUR:
                  visiteDate = fromDate.plusDays(visite.getIntervalleDepuisInclusion());
                  break;
               case MOIS:
                  visiteDate = fromDate.plusMonths(visite.getIntervalleDepuisInclusion());
                  break;
               case ANNEE:
                  visiteDate = fromDate.plusYears(visite.getIntervalleDepuisInclusion());
                  break;
               case HEURE:
                  visiteDate = fromDate.plusHours(visite.getIntervalleDepuisInclusion());
                  break;
               default:
                  break;
            }
            maladie.setDateDebut(Date.from(    
               visiteDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            maladies.add(maladie);
            patient.getMaladies().add(maladie);
         }
      }
      
      Collections.sort(maladies, Comparator.comparing(Maladie::getDateDebut));
      
      return maladies;
   }
}
