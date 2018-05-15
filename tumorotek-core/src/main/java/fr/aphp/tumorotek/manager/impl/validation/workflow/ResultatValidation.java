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
package fr.aphp.tumorotek.manager.impl.validation.workflow;

import java.util.HashMap;
import java.util.Map;

import fr.aphp.tumorotek.manager.exception.ValidationException;
import fr.aphp.tumorotek.model.validation.CritereValidation;
import fr.aphp.tumorotek.model.validation.NiveauValidation;
import fr.aphp.tumorotek.model.validation.OperateursLogiques;

/**
 * @author Gille Chapelot
 *
 */
public class ResultatValidation
{

   public class Cause
   {

      private Object valeurReference;
      private Object valeurConstatee;

      private Cause(final Object valeurConstatee, final Object valeurReference){
         this.valeurReference = valeurReference;
         this.valeurConstatee = valeurConstatee;
      }

      public Object getValeurReference(){
         return valeurReference;
      }

      public Object getValeurConstatee(){
         return valeurConstatee;
      }

   }

   private Boolean valide;
   private NiveauValidation niveauValidation;
   private final Map<CritereValidation, Cause> causes;
   private final Map<CritereValidation, String> anomalies;

   /**
    * Constructeur par défaut
    */
   public ResultatValidation(){
      causes = new HashMap<>();
      anomalies = new HashMap<>();
   }

   /**
    * Ajoute une cause de non-validité
    * @param critere critère non-vérifié
    * @param valeurReference valeur de référence
    * @param valeurConstatee valeur constatée
    */
   public void addCause(final CritereValidation critere, final Object valeurConstatee, final Object valeurReference){
      causes.put(critere, new Cause(valeurConstatee, valeurReference));
   }

   /**
    * Ajoute une anomalie de validation
    * @param critere critere en anomalie
    * @param message message d'anomalie
    */
   public void addAnomalie(final CritereValidation critere, final String message){
      anomalies.put(critere, message);
   }

   /**
    * Calcule la combinaison de 2 ResultatsValidation en fonction d'un opérateur
    * @param mergedRes ResultatValidation à fusionner
    * @param operateur opérateur logique
    * @return
    */
   public void merge(final ResultatValidation mergedRes, final OperateursLogiques operateur){

      Boolean valide = null;

      if(mergedRes != null){

         if(this.isValide() == null){
            valide = mergedRes.isValide();
         }else{

            switch(operateur){
               case AND:
                  valide = this.isValide() && mergedRes.isValide();
                  break;
               case OR:
                  valide = this.isValide() || mergedRes.isValide();
                  break;
               default:
                  throw new ValidationException("Opérateur obligatoire");
            }

         }

         this.causes.putAll(mergedRes.getCauses());

      }

      this.valide = valide;
      this.anomalies.putAll(mergedRes.getAnomalie());

   }

   /**
    * @return the resultat
    */
   public Boolean isValide(){
      return valide;
   }

   /**
    * @param valide the valide to set
    */
   public void setValide(final Boolean valide){
      this.valide = valide;
   }

   /**
    * @return the niveauValidation
    */
   public NiveauValidation getNiveauValidation(){
      return niveauValidation;
   }

   /**
    * @param niveauCriticite the niveauValidation to set
    */
   public void setNiveauValidation(NiveauValidation niveauValidation){
      this.niveauValidation = niveauValidation;
   }

   /**
    * @return the causes
    */
   public Map<CritereValidation, Cause> getCauses(){
      return causes;
   }

   /**
    * @return the causes
    */
   public Map<CritereValidation, String> getAnomalie(){
      return anomalies;
   }

}
