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
package fr.aphp.tumorotek.manager.validation.coeur.patient.gatsbi;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import fr.aphp.tumorotek.manager.validation.coeur.patient.MaladieValidator;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;

/**
 * Surcharge du validator Maladie permettant de:<br>
 *  - rendre la date de debut obligatoire
 *  - ne pas appliquer la cohérence sur cette date
 *  
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class MaladieValidatorDateCoherenceOverride extends MaladieValidator
{


   @Override
   public Errors checkDateDebutCoherence(final Maladie maladie){

      final BindException errs = new BindException(maladie, "fr.aphp.tumorotek.model.coeur.patient.Maladie");

      if(maladie.getDateDebut() == null){
         errs.rejectValue("dateDebut", "visite.date.empty");
      }
      return errs;
   }
   
   @Override
   public Errors checkDateDiagCoherence(final Maladie maladie){

      final BindException errs = new BindException(maladie, "fr.aphp.tumorotek.model.coeur.patient.Maladie");

      return errs;
   }
}