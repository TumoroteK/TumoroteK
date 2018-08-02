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
package fr.aphp.tumorotek.manager.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import fr.aphp.tumorotek.manager.validation.exception.ValidationException;

/**
 * Classe de validation des beans par les Spring Validator.
 * Date: 06/10/2009
 *
 * @author jbriscoe
 * @see http://javaboutique.internet.com/tutorials/validation/index-3.html
 */
public final class BeanValidator
{

   private static Log log = LogFactory.getLog(BeanValidator.class);

   private static List<Validator> validators = new ArrayList<>();

   public static List<Validator> getValidators(){
      return validators;
   }

   /**
    * Constructeur.
    */
   private BeanValidator(){}

   public static void validateObject(final Object arg, final Validator[] vals){
      final List<Errors> errors = new ArrayList<>();

      for(final Validator validator : vals){
         if(validator.supports(arg.getClass())){
            log.debug("Validator supported: " + arg.getClass());
            validateAndAddErrors(arg, validator, errors);
            //inspectObjectProperties(arg, errors);
            //break;
         }
      }

      if(errors.size() > 0){
         log.debug(errors.toString());
         log.warn("Validation error(s) found, " + "throwing ValidationException.");
         throw new ValidationException(errors);
      }

   }

   private static Errors validateAndAddErrors(final Object arg, final Validator validator, final List<Errors> errors){

      final BindException objErrors = new BindException(arg, arg.getClass().getName());
      validator.validate(arg, objErrors);
      if(objErrors.hasErrors()){
         errors.add(objErrors);
      }
      return objErrors;
   }

}
