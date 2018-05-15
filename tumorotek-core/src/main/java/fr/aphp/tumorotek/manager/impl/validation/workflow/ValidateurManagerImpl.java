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

import static fr.aphp.tumorotek.model.validation.OperateursLogiques.OR;
import static fr.aphp.tumorotek.model.validation.OperateursLogiques.AND;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import fr.aphp.tumorotek.manager.exception.TKException;
import fr.aphp.tumorotek.manager.exception.ValidationException;
import fr.aphp.tumorotek.manager.impl.io.utils.RechercheUtilsManager;
import fr.aphp.tumorotek.manager.validation.workflow.NiveauValidationManager;
import fr.aphp.tumorotek.manager.validation.workflow.ValidateurManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.coeur.annotation.DataType;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.validation.Action;
import fr.aphp.tumorotek.model.validation.CritereValidation;
import fr.aphp.tumorotek.model.validation.NiveauValidation;
import fr.aphp.tumorotek.model.validation.OperateursComparaison;
import fr.aphp.tumorotek.model.validation.OperateursLogiques;
import fr.aphp.tumorotek.model.validation.Validation;
import fr.aphp.tumorotek.utils.ConversionUtils;

/**
 * @author Gille Chapelot
 *
 */
public class ValidateurManagerImpl implements ValidateurManager
{

   NiveauValidationManager niveauValidationManager;

   private final Log log = LogFactory.getLog(ValidateurManagerImpl.class);

   /**
    * Constructeur
    * @param validation
    */
   public ValidateurManagerImpl(){}

   /*
    * (non-Javadoc)
    * @see fr.aphp.tumorotek.manager.validation.workflow.ValidateurManager#validerAction(fr.aphp.tumorotek.model.TKValidableObject, fr.aphp.tumorotek.model.validation.Action)
    */
   @Override
   public ResultatValidation validerAction(final TKAnnotableObject entite, final Action action){

      ResultatValidation resultatAction = null;
      
      if(action == null || action.getListValidations() == null || action.getListValidations().isEmpty()) {
         throw new ValidationException("Aucune validation définie");
      }
      
      final List<Validation> listValidation = action.getListValidations();
      
      final NiveauValidation niveauValidationOK = niveauValidationManager.findCriticiteLevelOk();

      //Ordonne la liste de validation selon leur criticité (les plus critiques en premier)
      Collections.sort(listValidation, new Comparator<Validation>()
      {

         @Override
         public int compare(final Validation val0, final Validation val1){

            final Integer criticiteVal0 = val0.getNiveauValidation().getCriticite();
            final Integer criticiteVal1 = val1.getNiveauValidation().getCriticite();

            return criticiteVal0.compareTo(criticiteVal1);
         }

      });

      NiveauValidation currentNiveauValidation = listValidation.get(0).getNiveauValidation();

      final Iterator<Validation> validationIterator = listValidation.iterator();

      Validation validationRacine = validationIterator.next();
      boolean isValidationNiveauOk = niveauValidationOK.equals(validationRacine.getNiveauValidation());

      resultatAction = valider(entite, validationRacine, isValidationNiveauOk);

      while(validationIterator.hasNext()){

         final Validation validation = validationIterator.next();

         isValidationNiveauOk = niveauValidationOK.equals(validation.getNiveauValidation());

         final ResultatValidation resultatValidation = valider(entite, validation, isValidationNiveauOk);

         if(validation.getNiveauValidation().equals(currentNiveauValidation)){
            //Au niveau de validation OK, on veut que toutes les validations soient true. A tous les autres niveaux,
            //si une validation est true, alors on considère que les critères sont remplis et que la validation est true
            //dénotant une anomalie (une validation de niveau non OK à true correspond à une non-conformité)
            OperateursLogiques operateur = isValidationNiveauOk ? AND : OR;
            resultatAction.merge(resultatValidation, operateur);
         }else if(!resultatAction.isValide() && resultatAction.getAnomalie().isEmpty()){
            currentNiveauValidation = validation.getNiveauValidation();
            resultatAction = resultatValidation;
         }else{
            break;
         }

      }

      resultatAction.setNiveauValidation(currentNiveauValidation);

      //Cas d'une validation de niveau inférieur à OK qui renvoie true (=> entité invalide)
      if( resultatAction.isValide() && !niveauValidationOK.equals(currentNiveauValidation) ) {
         resultatAction.setValide(false);
      }
      
      //Cas où l'entité est hors des critères de validation
      else if( !resultatAction.isValide() && niveauValidationOK.equals(currentNiveauValidation) ){
         NiveauValidation niveauHorsCriteres = niveauValidationManager.findCriticiteLevelUndefined();
         resultatAction.setNiveauValidation(niveauHorsCriteres);
      }
      
      //Si défaut de validation de niveau OK, on considère l'entité valide si elle ne présente ni causes d'invalidité, ni anomalies
      else if (!resultatAction.isValide() && resultatAction.getAnomalie().isEmpty() && !niveauValidationOK.equals(currentNiveauValidation)){
         resultatAction.setNiveauValidation(niveauValidationOK);
         resultatAction.setValide(true);
      }

      return resultatAction;

   }

   /**
    * Applique une validation à une entité
    * @param validation validation à appliquer
    * @param entite entitée à tester
    * @return
    */
   private ResultatValidation valider(final TKAnnotableObject entite, final Validation validation,
      final boolean isNiveauValidationOk){

      ResultatValidation res = null;

      final Set<Validation> children = validation.getEnfants();
      final List<CritereValidation> criteres = validation.getCriteres();
      final OperateursLogiques operateur = validation.getOperateur();

      //Traitement des critères
      if(!criteres.isEmpty()){

         final Iterator<CritereValidation> critereIterator = criteres.iterator();

         final CritereValidation premierCritere = critereIterator.next();
         final ResultatValidation resCriteres = validerCritere(premierCritere, entite, isNiveauValidationOk);

         while(critereIterator.hasNext()){
            resCriteres.merge(validerCritere(critereIterator.next(), entite, isNiveauValidationOk), operateur);
         }

         res = resCriteres;

      }

      //Traitement des validations filles
      if(!children.isEmpty()){

         ResultatValidation resValidations = null;

         final Iterator<Validation> childrenIterator = children.iterator();

         final Validation premiereValidation = childrenIterator.next();
         resValidations = valider(entite, premiereValidation, isNiveauValidationOk);

         while(childrenIterator.hasNext()){
            resValidations.merge(valider(entite, childrenIterator.next(), isNiveauValidationOk), operateur);
         }

         //Si on a un résultat issu du traitement des critères, il est combiné avec le résultat des validations filles
         //sinon, le résultat de la validation est le résultat du traitement des validations filles
         if(res != null){
            res.merge(resValidations, operateur);
         }else{
            res = resValidations;
         }

      }

      if(validation.getNiveauValidation() != null){
         res.setNiveauValidation(validation.getNiveauValidation());
      }

      return res;

   }

   /**
    * Valide un critère de validation
    * @param critere critère à valider
    * @param entite entité à tester
    * @return true si le critère est validé, false dans tous les autres cas
    */
   private ResultatValidation validerCritere(final CritereValidation critere, final TKAnnotableObject entite,
      boolean isNiveauValidationOk){

      final ResultatValidation res = new ResultatValidation();

      boolean valide = false;

      Object valeurTestee = getValeurTestee(critere, entite);
      Object valeurReference = getValeurReference(critere, entite);
      final OperateursComparaison operateur = critere.getOperateur();

      //On prend un raccourci en considérant les valeurs de type String vides comme null
      if(StringUtils.isEmpty(valeurTestee)){
         valeurTestee = null;
      }

      if(StringUtils.isEmpty(valeurReference)){
         valeurReference = null;
      }

      //Tests de valeurs nulles
      if(valeurTestee == null){
         switch(operateur){
            case EGAL:
               valide = valeurReference == null;
               break;
            case DIFFERENT:
               valide = valeurReference != null;
               break;
            default:
               valide = !isNiveauValidationOk;
               break;
         }
      }else if(valeurReference == null){
         switch(operateur){
            case EGAL:
               valide = valeurTestee == null;
               break;
            case DIFFERENT:
               valide = valeurTestee != null;
               break;
            default:
               valide = isNiveauValidationOk;
               break;
         }

         //Test de valeurs non nulles
      }else{

         String datatypeNom = getChampDatatype(critere.getChamp()).getType();

         if("calcule".equals(datatypeNom)){
            datatypeNom = critere.getChamp().getChampAnnotation().getChampCalcule().getDataType().getType();
         }

         try{
            switch(datatypeNom){
               case "alphanum":
               case "thesaurus":
               case "texte":
                  valide = validerAlphanum(valeurTestee.toString(), valeurReference.toString(), critere.getOperateur());
                  break;
               case "num":
               case "duree":
                  valide = validerNum(ConversionUtils.convertToFloat(valeurTestee),
                     ConversionUtils.convertToFloat(valeurReference), critere.getOperateur());
                  break;
               case "boolean":
                  valide = validerBooleen(ConversionUtils.convertToBoolean(valeurTestee),
                     ConversionUtils.convertToBoolean(valeurReference), critere.getOperateur());
                  break;
               case "date":
               case "datetime":
                  valide = validerDate(ConversionUtils.convertToCalendar(valeurTestee),
                     ConversionUtils.convertToCalendar(valeurReference), critere.getOperateur());
                  break;
               default:
                  log.error("Datatype [" + datatypeNom + "] non prévu pour la validation");
                  res.addAnomalie(critere, "Datatype [" + datatypeNom + "] non prévu pour la validation");
                  break;
            }

         }catch(final TKException tke){
            res.addAnomalie(critere, tke.getMessage());
         }

      }

      if((valide && !isNiveauValidationOk) || (!valide && isNiveauValidationOk)){
         final Object valeurTesteeTypee = valeurTestee != null ? valeurTestee : null;
         final Object valeurRefTypee = valeurReference != null ? valeurReference : null;
         res.addCause(critere, valeurTesteeTypee, valeurRefTypee);
      }

      res.setValide(valide);

      return res;

   }

   /**
    * Retourne le DataType associé à un Champ
    * @param champ
    * @return
    */
   private DataType getChampDatatype(final Champ champ){

      DataType res = null;

      if(champ.getChampAnnotation() != null){
         res = champ.getChampAnnotation().getDataType();
      }else if(champ.getChampEntite().getQueryChamp() == null){
         res = champ.getChampEntite().getDataType();
      }else{
         res = champ.getChampEntite().getQueryChamp().getDataType();
      }

      return res;

   }

   /**
    * Récupère la valeur du champ à tester par un critère de validation
    * @param critere critère testé
    * @param entite entité testée
    * @return
    */
   private Object getValeurTestee(final CritereValidation critere, final TKAnnotableObject entite){

      final List<Object> listObjetsLies = RechercheUtilsManager.getListeObjetsCorrespondants(entite, critere.getChamp(), null);
      
      Object res = RechercheUtilsManager.getChampValueFromObjectList(critere.getChamp(), listObjetsLies);
      
      if(res instanceof String && "boolean".equals(getChampDatatype(critere.getChamp()).getType() )) {
         res = StringUtils.isEmpty(res) ? null : "oui".equalsIgnoreCase((String)res);
      }
      
      return res;

   }

   /**
    * Recupère la valeur du champ de référence d'un critère de validation pour l'entité passée en paramètre
    * @param entite entitée testée
    * @param critere critère testé
    * @return
    */
   private Object getValeurReference(final CritereValidation critere, final TKAnnotableObject entite){

      Object res = null;

      if(critere.getValeurRef() != null){
         res = critere.getValeurRef();
      }else if(critere.getChampRef() != null){

         final List<Object> listObjetsLies =
            RechercheUtilsManager.getListeObjetsCorrespondants(entite, critere.getChampRef(), null);
         
         res = RechercheUtilsManager.getChampValueFromObjectList(critere.getChamp(), listObjetsLies);
         
         if(res instanceof String && "boolean".equals(getChampDatatype(critere.getChamp()).getType() )) {
            res = StringUtils.isEmpty(res) ? null : "oui".equalsIgnoreCase((String)res);
         }
         
      }

      return res;
   }

   /**
    * Compare 2 valeurs alphanumériques en fonction d'un opérateur
    * @param valeurTestee valeur 1
    * @param valeurReference valeur 2
    * @param operateur opérateur de comparaison
    * @return
    */
   private boolean validerAlphanum(final String valeurTestee, final String valeurReference,
      final OperateursComparaison operateur){

      boolean res = false;
      //On traite les valeur nulles comme des chaînes vide car on peut vouloir valider une absence de valeur
      final String stringTestee = valeurTestee != null ? valeurTestee.toString() : "";

      switch(operateur){
         case SUPERIEUR:
            res = stringTestee.compareToIgnoreCase(valeurReference) > 0;
            break;
         case SUPERIEUR_EGAL:
            res = stringTestee.compareToIgnoreCase(valeurReference) >= 0;
            break;
         case EGAL:
            res = stringTestee.equalsIgnoreCase(valeurReference);
            break;
         case DIFFERENT:
            res = !stringTestee.equalsIgnoreCase(valeurReference);
            break;
         case INFERIEUR:
            res = stringTestee.compareToIgnoreCase(valeurReference) < 0;
            break;
         case INFERIEUR_EGAL:
            res = stringTestee.compareToIgnoreCase(valeurReference) <= 0;
            break;
         default:
            break;
      }

      return res;

   }

   /**
    * Compare 2 valeurs numériques en fonction d'un opérateur
    * @param valeurTestee valeur 1
    * @param valeurReference valeur 2
    * @param operateur opérateur de comparaison
    * @return
    */
   private boolean validerNum(final Float valeurTestee, final Float valeurReference, final OperateursComparaison operateur){

      boolean res = false;

      //Cas du test de la présence (ou de l'absence) d'une valeur
      if(valeurReference == null){
         switch(operateur){
            case EGAL:
               res = valeurTestee == null;
               break;
            case DIFFERENT:
               res = valeurTestee != null;
               break;
            default:
               throw new ValidationException("Opérateur" + operateur + "interdit pour le contrôle de la présence d'une valeur");
         }
      }
      //Cas standard (si la valeur testée est null et que la valeur référence ne l'est pas, on renvoie toujours false)
      else if(valeurTestee != null){

         switch(operateur){
            case SUPERIEUR:
               res = valeurTestee.compareTo(valeurReference) > 0;
               break;
            case SUPERIEUR_EGAL:
               res = valeurTestee.compareTo(valeurReference) >= 0;
               break;
            case EGAL:
               res = valeurTestee.compareTo(valeurReference) == 0;
               break;
            case DIFFERENT:
               res = valeurTestee.compareTo(valeurReference) != 0;
               break;
            case INFERIEUR_EGAL:
               res = valeurTestee.compareTo(valeurReference) <= 0;
               break;
            case INFERIEUR:
               res = valeurTestee.compareTo(valeurReference) < 0;
               break;
         }
      }

      return res;

   }

   /**
    * Compare 2 valeurs booléennes en fonction d'un opérateur logique
    * @param valeurTestee valeur 1
    * @param valeurReference valeur 2
    * @param operateur opérateur de comparaison
    * @return
    */
   private boolean validerBooleen(final Boolean valeurTestee, final Boolean valeurReference,
      final OperateursComparaison operateur){

      boolean res = false;

      //Cas du test de la présence (ou de l'absence) d'une valeur
      if(valeurReference == null){
         switch(operateur){
            case EGAL:
               res = valeurTestee == null;
               break;
            case DIFFERENT:
               res = valeurTestee != null;
               break;
            default:
               throw new ValidationException("Opérateur" + operateur + "interdit pour le contrôle de la présence d'une valeur");
         }
      }
      //Cas standard (si la valeur testée est null et que la valeur référence ne l'est pas, on renvoie toujours false)
      else if(valeurTestee != null){

         switch(operateur){
            case EGAL:
               res = valeurTestee.equals(valeurReference);
               break;
            case DIFFERENT:
               res = !valeurTestee.equals(valeurReference);
               break;
            default:
               throw new ValidationException(
                  "Opérateur [" + operateur.getLabel() + "] non supporté pour la validation de booléens");
         }
      }

      return res;

   }

   /**
    * Compare 2 dates en fonction d'un opérateur logique (les secondes et millisecondes sont ignorées)
    * @param valeurTestee valeur 1
    * @param valeurReference valeur 2
    * @param operateur opérateur de comparaison
    * @return
    */
   private boolean validerDate(final Calendar valeurTestee, final Calendar valeurReference,
      final OperateursComparaison operateur){

      boolean res = false;

      //Cas du test de la présence (ou de l'absence) d'une valeur
      if(valeurReference == null){
         switch(operateur){
            case EGAL:
               res = valeurTestee == null;
               break;
            case DIFFERENT:
               res = valeurTestee != null;
               break;
            default:
               throw new ValidationException("Opérateur" + operateur + "interdit pour le contrôle de la présence d'une valeur");
         }
      }
      //Cas standard (si la valeur testée est null et que la valeur référence ne l'est pas, on renvoie toujours false)
      else if(valeurTestee != null){

         //Les secondes et millisecondes ne sont pas prises en compte dans la comparaison
         valeurReference.set(Calendar.SECOND, 0);
         valeurTestee.set(Calendar.SECOND, 0);
         valeurReference.set(Calendar.MILLISECOND, 0);
         valeurTestee.set(Calendar.MILLISECOND, 0);

         switch(operateur){
            case SUPERIEUR:
               res = valeurTestee.after(valeurReference);
               break;
            case SUPERIEUR_EGAL:
               res = valeurTestee.after(valeurReference) || valeurTestee.equals(valeurReference);
               break;
            case EGAL:
               res = valeurTestee.equals(valeurReference);
               break;
            case DIFFERENT:
               res = !valeurTestee.equals(valeurReference);
               break;
            case INFERIEUR:
               res = valeurTestee.before(valeurReference);
               break;
            case INFERIEUR_EGAL:
               res = valeurTestee.before(valeurReference) || valeurTestee.equals(valeurReference);
               break;
         }
      }

      return res;
   }

   /**
    * @return the niveauValidationManager
    */
   public NiveauValidationManager getNiveauValidationManager(){
      return niveauValidationManager;
   }

   /**
    * @param niveauValidationManager the niveauValidationManager to set
    */
   public void setNiveauValidationManager(NiveauValidationManager niveauValidationManager){
      this.niveauValidationManager = niveauValidationManager;
   }

}
