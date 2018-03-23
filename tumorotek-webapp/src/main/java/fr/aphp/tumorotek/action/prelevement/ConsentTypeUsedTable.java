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
package fr.aphp.tumorotek.action.prelevement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import fr.aphp.tumorotek.decorator.PrelevementDecorator2;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;

/**
 * Extension de HashTable pour gérer une table ConsentType : Liste de prélèvement
 * qui a pour objectif de contenir pour un patient donné les consentements
 * déja attribués à des prélèvements consultables par l'utilisateur.
 *
 * @author Mathieu BARTHELEMY
 * @versin 2.0.13
 *
 */
public class ConsentTypeUsedTable extends Hashtable<ConsentType, List<PrelevementDecorator2>>
{

   private static final long serialVersionUID = 1L;

   private Patient patient;

   public Patient getPatient(){
      return patient;
   }

   public void setPatient(final Patient p){
      this.patient = p;
   }

   @Override
   public synchronized void clear(){
      setPatient(null);
      super.clear();
   }

   public String getSize(){
      return String.valueOf(size());
   }

   public List<PrelevementDecorator2> getPrels(){
      final List<PrelevementDecorator2> prels = new ArrayList<>();
      final List<ConsentType> keySet = new ArrayList<>(keySet());
      Collections.sort(keySet);
      for(final ConsentType keyC : keySet){
         prels.addAll(this.get(keyC));
      }
      return prels;
   }
}
