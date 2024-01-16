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
package fr.aphp.tumorotek.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Collaborateur;
import fr.aphp.tumorotek.model.contexte.Etablissement;
import fr.aphp.tumorotek.model.contexte.Service;

/**
 * gère tous les services et établissement d'un groupe de collaborateurs
 * contient d'une part, les services et les établissements rattachés
 * et d'autre part, les établissements des collaborateurs qui n'ont pas de services
 * @author chuet
 *
 */
public class ServicesEtEtablissementsLiesADesCollaborateurs
{
   private List<Collaborateur> collaborateurs = new ArrayList<Collaborateur>();
   //ensemble des services non archivés de tous les collaborateurs sans les doublons et triés
   private List<Service> servicesActifsDesCollaborateurs = new ArrayList<Service>();
   
   private List<Etablissement> etablissementsActifsDesCollaborateurs = null;

   public ServicesEtEtablissementsLiesADesCollaborateurs() {
   }
   
   public ServicesEtEtablissementsLiesADesCollaborateurs(
         List<Collaborateur> collaborateurs, 
         List<Service> servicesActifsDesCollaborateurs) {
      this.collaborateurs = collaborateurs;
      this.servicesActifsDesCollaborateurs = servicesActifsDesCollaborateurs;
      
   }
   
   public List<Service> getServicesActifsDesCollaborateurs(){
      return servicesActifsDesCollaborateurs;
   }

   public List<Etablissement> getEtablissementsActifsDesCollaborateurs(){
      if(etablissementsActifsDesCollaborateurs == null) {
         etablissementsActifsDesCollaborateurs = new ArrayList<Etablissement>();
         populateAndSortAllEtablissementsActifs();
      }
      return etablissementsActifsDesCollaborateurs;
   }
   
   private void populateAndSortAllEtablissementsActifs() {
      Set<Integer> etablissementIdsAlreadAdded = new HashSet<Integer>();
      for(Service service : servicesActifsDesCollaborateurs) {
         addEtablissementIfNecessary(service.getEtablissement(), etablissementIdsAlreadAdded);
      }
      for(Collaborateur collaborateur : collaborateurs) {
         Etablissement etablissementRattachement = collaborateur.getEtablissement();
         if(etablissementRattachement != null && !etablissementRattachement.getArchive()) {
            addEtablissementIfNecessary(etablissementRattachement, etablissementIdsAlreadAdded);
         }
      }
      
      Collections.sort(etablissementsActifsDesCollaborateurs, Comparator.comparing(Etablissement::getNom));
   }
   
   private void addEtablissementIfNecessary(Etablissement etablissementToAdd, Set<Integer> etablissementIdsAlreadAdded) {
      if(etablissementIdsAlreadAdded == null) {
         etablissementIdsAlreadAdded = new HashSet<Integer>();
      }
      if(etablissementToAdd != null && !etablissementIdsAlreadAdded.contains(etablissementToAdd.getEtablissementId())) {
         etablissementsActifsDesCollaborateurs.add(etablissementToAdd);
         etablissementIdsAlreadAdded.add(etablissementToAdd.getEtablissementId());
      }
   }
}
