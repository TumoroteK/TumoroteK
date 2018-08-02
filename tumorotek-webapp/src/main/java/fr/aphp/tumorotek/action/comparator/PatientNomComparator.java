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
package fr.aphp.tumorotek.action.comparator;

import java.util.Comparator;

import fr.aphp.tumorotek.model.coeur.patient.Patient;

/**
 * Classe implémentant un comparator sur le nom
 * des patients (et du prénom si nécessaire).
 *
 * @author Pierre Ventadour.
 * Le 03/08/2011.
 *
 */
public class PatientNomComparator implements Comparator<Patient>
{

   private Boolean asc;

   public PatientNomComparator(final Boolean a){
      asc = a;
   }

   @Override
   public int compare(final Patient o1, final Patient o2){
      int v = 1;
      if(o1.getNom() != null && o2.getNom() != null){
         v = o1.getNom().compareToIgnoreCase(o2.getNom());
         if(v == 0){
            if(o1.getPrenom() != null && o2.getPrenom() != null){
               v = o1.getPrenom().compareToIgnoreCase(o2.getPrenom());
            }
         }
      }
      return asc ? v : -v;
   }

   public Boolean getAsc(){
      return asc;
   }

   public void setAsc(final Boolean a){
      this.asc = a;
   }
}
