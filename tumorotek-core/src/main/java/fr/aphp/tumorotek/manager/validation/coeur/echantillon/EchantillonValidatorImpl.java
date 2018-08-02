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
package fr.aphp.tumorotek.manager.validation.coeur.echantillon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import fr.aphp.tumorotek.manager.coeur.cession.RetourManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonValidator;
import fr.aphp.tumorotek.manager.validation.CoherenceDateManager;
import fr.aphp.tumorotek.manager.validation.ValidationUtilities;
import fr.aphp.tumorotek.model.cession.Retour;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.prelevement.LaboInter;

/**
 * Validator pour le bean domaine Echantillon (de echantillon).
 * Classe creee le 09/10/09
 *
 * Regles de validation:
 * 	- le champ code doit etre non vide, non null
 *  - le champ latéralité doit etre non vide
 *  - les champs quantité, quantité_init, volume,
 *    volume_init, delai_cgl doivent etre positifs
 *  - la date de stockage doit être null ou
 *  	supérieure ou égale à la date d'arrivee du prelevement,
 *  	sinon
 *  		supérieure ou égale à la date de départ du dernier labo inter
 *  		si non null,
 *  		sinon
 *  			supérieure ou égale à la date d'arrivee du dernier labo
 *  			si non null,
 *  			...recursivite sur labos précédents
 *  			sinon
 *  				supérieure ou égale à la date de départ du prelevement
 *  				si non null,
 *  				sinon
 *  					supérieure ou égale à la date de prelevement
 *  					si non null,
 *  					sinon
 *  						supérieure ou égale à la date de naissance,
 *  	inférieure ou égale à la date actuelle <br>
 *  - le champs stérile ne peut être TRUE que si la chaine stérilité
 *  Prélèvement et LaboInter est respectée et est spécifiée à TRUE<br>
 *
 *  2.0.7: Ajout validation sur objet statut + emplacement
 *
 *
 * @author Pierre VENTADOUR
 * @version 2.0.7
 */
public class EchantillonValidatorImpl implements EchantillonValidator
{

   //	private ProdDeriveValidator prodDeriveValidator;
   //	private PrelevementValidator prelevementValidator;
   private RetourManager retourManager;
   //
   //	public void setProdDeriveValidator(ProdDeriveValidator pValidator) {
   //		this.prodDeriveValidator = pValidator;
   //	}
   //
   //	public void setPrelevementValidator(PrelevementValidator ptValidator) {
   //		this.prelevementValidator = ptValidator;
   //	}

   private CoherenceDateManager coherenceDateManager;

   public void setCoherenceDateManager(final CoherenceDateManager cManager){
      this.coherenceDateManager = cManager;
   }

   public void setRetourManager(final RetourManager rManager){
      this.retourManager = rManager;
   }

   @Override
   public boolean supports(final Class<?> clazz){
      return Echantillon.class.equals(clazz);
   }

   @Override
   public void validate(final Object obj, final Errors errs){
      //code non vide
      ValidationUtils.rejectIfEmptyOrWhitespace(errs, "code", "echantillon.code.empty");

      final Echantillon echan = (Echantillon) obj;
      // code valide
      if(echan.getCode() != null){
         if(!echan.getCode().matches(ValidationUtilities.CODEREGEXP)){
            errs.rejectValue("code", "echantillon.code.illegal");
         }
         if(echan.getCode().length() > 50){
            errs.rejectValue("code", "echantillon.code.tooLong");
         }
      }
      // quantiteInit >= 0
      if(echan.getQuantiteInit() != null){
         if(echan.getQuantiteInit() < 0){
            errs.rejectValue("quantiteInit", "echantillon.quantiteInit.negative");
         }
      }
      // quantite >= 0
      if(echan.getQuantite() != null){
         if(echan.getQuantite() < 0){
            errs.rejectValue("quantite", "echantillon.quantite.negative");
         }
      }
      // quantiteInit >= quantite
      if(echan.getQuantite() != null && echan.getQuantiteInit() != null){
         if(echan.getQuantite() > echan.getQuantiteInit()){
            errs.rejectValue("quantite", "echantillon.quantite.tooBig");
         }
      }
      // delai_cgl >= 0
      if(echan.getDelaiCgl() != null){
         if(echan.getDelaiCgl() < 0){
            errs.rejectValue("delaiCgl", "echantillon.delaiCgl.negative");
         }
      }
      // lateralite
      if(echan.getLateralite() != null){
         if(!(echan.getLateralite().equals("D") || echan.getLateralite().equals("G") || echan.getLateralite().equals("I")
            || echan.getLateralite().equals("B"))){
            errs.rejectValue("lateralite", "echantillon.lateralite.illegal");
         }
      }

      // 2.0.7: validation statut emplacement
      if(echan.getEmplacement() != null){
         if(echan.getObjetStatut().getStatut().equals("NON STOCKE")
            || (echan.getObjetStatut().getStatut().equals("EPUISE") || (echan.getObjetStatut().getStatut().equals("DETRUIT")))){
            errs.rejectValue("objetStatut", "echantillon.statut.incoherent");
         }
      }else if(echan.getObjetStatut().getStatut().equals("STOCKE") || (echan.getObjetStatut().getStatut().equals("RESERVE"))){
         errs.rejectValue("objetStatut", "echantillon.statut.incoherent");
      }

      //date stockage cohérente -> skip date preleveement
      errs.addAllErrors(checkDateStockageCoherence(echan, true));

      //sterilite cohérente
      errs.addAllErrors(checkSteriliteAntecedence(echan));
   }

   @Override
   public Errors checkDateStockageCoherence(final Echantillon echantillon, final boolean skipToDatePrel){

      final BindException errs = new BindException(echantillon, "fr.aphp.tumorotek.model.coeur.echantillon.Echantillon");
      if(echantillon.getDateStock() != null){
         // limites inf
         if(echantillon.getPrelevement() != null){
            final Object[] dateAndCode =
               coherenceDateManager.findAntRefDateInPrelevementManager(echantillon.getPrelevement(), skipToDatePrel);
            if(dateAndCode[0] != null){
               if(!ValidationUtilities.checkWithDate(echantillon.getDateStock(), null, dateAndCode[0], null, null, null, null,
                  false)){
                  errs.rejectValue("dateStock", ((String) dateAndCode[1]));
               }
            }
         }
         //limites sup
         final Object[] dateAndCode = coherenceDateManager.findPostRefDateInDerivesManager(echantillon);
         if(!ValidationUtilities.checkWithDate(echantillon.getDateStock(), null, dateAndCode[0], null, null, null, null, true)){
            errs.rejectValue("dateStock", (String) dateAndCode[1]);
         }
      } // else { // melbase hack
        //errs.rejectValue("dateStock", "echantillon.dateStock.empty");
        //}
      return errs;
   }

   @Override
   public Errors checkSteriliteAntecedence(final Echantillon echantillon){

      final BindException errs = new BindException(echantillon, "fr.aphp.tumorotek.model.coeur.echantillon.Echantillon");

      if(echantillon.getSterile() != null && echantillon.getSterile()){
         if(echantillon.getPrelevement() != null){
            final List<LaboInter> labos = new ArrayList<>();
            if(echantillon.getPrelevement().getLaboInters() != null){
               labos.addAll(echantillon.getPrelevement().getLaboInters());
            }
            // dernier labo
            if(labos.size() > 0){
               final Iterator<LaboInter> it = labos.iterator();
               LaboInter listLab = null;
               LaboInter last = labos.get(labos.size() - 1);
               while(it.hasNext()){ // trouve l'ordre le plus grand
                  listLab = it.next();
                  if(listLab.getOrdre() > last.getOrdre()){
                     last = listLab;
                  }
               }

               if(last.getSterile() == null || !last.getSterile()){
                  errs.rejectValue("sterile", "sterile.validation.previousNotSterile");
               }
            }else{
               if(echantillon.getPrelevement().getSterile() == null || !echantillon.getPrelevement().getSterile()){
                  errs.rejectValue("sterile", "sterile.validation.prelevementNotSterile");
               }
            }
         }

         // verifier si un retour a brisé la chaine de stétilité
         final List<Retour> retours = retourManager.getRetoursForObjectManager(echantillon);
         boolean nonSterileRetour = false;
         for(int i = 0; i < retours.size(); i++){
            if(retours.get(i).getSterile() != null && !retours.get(i).getSterile()){
               nonSterileRetour = true;
               break;
            }
         }
         if(nonSterileRetour){
            errs.rejectValue("sterile", "sterile.validation.retourNotSterile");
         }
      }

      return errs;
   }
}
